package 수강신청프로그램;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class Professor {
private String name;
private String department;
private static int profIDcnt=1000;
private int profID;
public Professor() {
    profID = profIDcnt;
    profIDcnt++;
    name = "이름없음";
    department = "컴퓨터공학과";
}

public Professor(String name) {
    profID = profIDcnt;
    profIDcnt++;
    this.name = name;
    department = "컴퓨터공학과";
}

public Professor(String name, String department) {
    profID = profIDcnt;
    profIDcnt++;
    this.name = name;
    this.department = department;
}

public String getName() {
    return name;
}

public int getID() {
    return profID;
}

public String getDepartment() {
    return department;
}

public static Professor findProfessor(int profID, ArrayList<Professor> professors) {
    for (Professor professor : professors) {
        if (professor.getID() == profID) {
            return professor;
        }
    }
    return null; // 해당 ID에 해당하는 교수를 찾지 못한 경우
	}
}


class Student {
private String name;
private String department;
private static int stdIDcnt = 2000;
private int stdID;

public Student() {
    stdID = stdIDcnt;
    stdIDcnt++;
    name = "이름없음";
    department = "컴퓨터공학과";
}

public Student(String name) {
    stdID = stdIDcnt;
    stdIDcnt++;
    this.name = name;
    department = "컴퓨터공학과";
}

public Student(String name, String department) {
    stdID = stdIDcnt;
    stdIDcnt++;
    this.name = name;
    this.department = department;
}

public String getName() {
    return name;
}

public int getID() {
    return stdID;
}

public String getDepartment() {
    return department;
}

public static Student findStudent(int stdID, ArrayList<Student> students) {
    for (Student student : students) {
        if (student.getID() == stdID) {
            return student;
        }
    }
    return null; // 해당 ID에 해당하는 학생을 찾지 못한 경우
}
}
class Classroom {
    private static int classroomIDCounter = 3000;
    private int classroomID;
    private String name;
    private int[][] time_classroom;
    String[] days = {"일", "월", "화", "수", "목", "금", "토"};

    public Classroom(String name) {
        this.classroomID=classroomIDCounter;
        classroomIDCounter++;
        this.name = name;
        time_classroom = new int[7][10]; //  일 월 화 수~토요일, 0~9교시까지
    }

    public int getClassroomID() {
        return classroomID;
    }

    public String getName() {
        return name;
    }

    public Classroom classroomReserv(int courseID, int day, int time) {
        boolean isAvailable = true;
        for (int i = time; i < time + 3; i++) {
            if (time_classroom[day][i] != 0) {
                isAvailable = false;
                break;
            }
        }
        // 모든 시간이 예약되지 않은 경우에만 업데이트
        if (isAvailable) {
            for (int i = time; i < time + 3; i++) {
                time_classroom[day][i] = courseID;
            }
            System.out.println(
                    days[day] + "요일 " + time + ", " + (time+1) + ", "
                            + (time + 2) + "시에 수업이 추가되었습니다.");
            return this;

        } else {
        	 System.out.println("강의실을 사용할 수 없습니다.");
            return null;
        }
    }

    public String getTime(int courseIndex) {
        StringBuilder time = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 10; j++) {
                if (time_classroom[i][j] == courseIndex) {
                    time.append(days[i]).append("요일 ").append(j + 1).append(" 교시, ");
                }
            }
        }
        if (time.length() > 0) {
            time.delete(time.length() - 2, time.length()); // 마지막 ", " 제거
        }
        return time.toString();
    }

    public static Classroom findClassroom(int classroomID, ArrayList<Classroom> classrooms) {
        for (Classroom classroom : classrooms) {
            if (classroom.getClassroomID() == classroomID) {
                return classroom;
            }
        }
        return null; // 해당 ID에 해당하는 강의실을 찾지 못한 경우
    }

}
class Course {
    private static int courseIDCounter = 4000;
    private int courseID;
    private String courseName;
    private Professor professor;
    private Classroom classroom;
    private ArrayList<Student> enrolledStudents;
    private final int max = 30; // 한 강의실 최대 수강인원 30명

    public Course(String courseName, Professor professor, Classroom classroom, int day, int time) {
        this.courseID = courseIDCounter++;
        this.courseName = courseName;
        this.professor = professor;
        this.classroom = classroom;
        classroom.classroomReserv(courseID, day, time);
        enrolledStudents = new ArrayList<>();
    }

    public int getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public Professor getProfessor() {
        return professor;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public String getSchedule() {
        return classroom.getTime(courseID);
    }

    public boolean dropStudent(Student student) {
        System.out.println("수강 취소되었습니다.");
        return enrolledStudents.remove(student);
    }

    public boolean enrollmentStudent(Student student) {
        if (enrolledStudents.size() < max) {
            for (Student enrolledStudent : enrolledStudents) {
                if (enrolledStudent.equals(student)) {
                    System.out.println("이미 신청한 과목입니다. 수강취소하시겠습니까?(y/n)");
                    String choice = EnrollmentSystem.scan.nextLine();
                    if (choice.equalsIgnoreCase("y")) {
                        dropStudent(student);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            enrolledStudents.add(student);
            System.out.println(student.getName() + " 학생이 " + this.getCourseName() + " 과목을 수강 신청하였습니다.");
            return true;
        } else {
            System.out.println("수강인원이 꽉 찼습니다.");
            return false;
        }
    }

    public String displayEnrolledStudents() {
        StringBuilder enrolledStudentsList = new StringBuilder("수강 신청한 학생 목록: ");
        for (Student student : enrolledStudents) {
            enrolledStudentsList.append(student.getName()).append(", ");
        }
        enrolledStudentsList.delete(enrolledStudentsList.length() - 2, enrolledStudentsList.length()); // 맨 마지막 ", " 제거
        enrolledStudentsList.append(" 학생");
        return enrolledStudentsList.toString();
    }

    public static Course findCourse(int courseID, ArrayList<Course> courses) {
        for (Course course : courses) {
            if (course.getCourseID() == courseID) {
                return course;
            }
        }
        return null; // 해당 ID에 해당하는 강의를 찾지 못한 경우
    }
}

public class EnrollmentSystem {			// 수강신청 시스템 클래스(주 클래스)
	private ArrayList<Professor> professors;  // 교수 리스트
	private ArrayList<Course> courses;        // 과목 리스트 객체 생성
	private ArrayList<Classroom> classrooms;
	private ArrayList<Student> students;      // 학생 리스트
    int nowID;	// 아이디 	
	boolean profchk = false;	// 교수 여부 확인
	int viewOption=0;				// 뷰 선택 
	public static Scanner scan = new Scanner(System.in);

	    public EnrollmentSystem() {
	        students = new ArrayList<>();
	        professors = new ArrayList<>();
	        courses = new ArrayList<>();
	        classrooms = new ArrayList<>();

	        professors.add(new Professor("홍길동 교수님"));
	        professors.add(new Professor("김박사 교수님"));
	        professors.add(new Professor("이교수 교수님"));

	        classrooms.add(new Classroom("혜화관"));
	        classrooms.add(new Classroom("명진관"));
	        classrooms.add(new Classroom("학림관"));

	        for (char c = 'A'; c <= 'Z'; c++) {
	            String name = String.valueOf(c);
	            students.add(new Student(name));
	        }

	        courses.add(new Course("운영체제", Professor.findProfessor(1000, professors),
	                Classroom.findClassroom(3000, classrooms), 1, 1));
	        courses.add(new Course("인공지능", Professor.findProfessor(1001, professors),
	                Classroom.findClassroom(3001, classrooms), 2, 1));
	        courses.add(new Course("객체지향언어", Professor.findProfessor(1002, professors),
	                Classroom.findClassroom(3002, classrooms), 3, 1));

	        for (int i = 0; i < 5; i++) {
	            Course.findCourse(4000, courses).enrollmentStudent(Student.findStudent(2000 + i, students));
	            Course.findCourse(4001, courses).enrollmentStudent(Student.findStudent(2000 + i, students));
	            Course.findCourse(4002, courses).enrollmentStudent(Student.findStudent(2000 + i, students));
	        }
	    }

	    public void run() {
	        int chk;
	        System.out.println("번호를 입력하시오(1.학생 2.교직원)");
	        do {
	            System.out.print("번호 입력>>");
	            chk = 0;
	            try {
	                chk = scan.nextInt();
	                if (chk < 1 || chk > 2) {
	                    System.out.println("1 또는 2를 입력하세요.");
	                }
	            } catch (InputMismatchException e) {
	                System.out.println("정수 번호로 입력하세요.");
	                scan.nextLine();
	            }
	        } while (chk < 1 || chk > 2);

	        profchk = chk == 1 ? false : true;
	        System.out.println("ID를 입력하세요.");
	        while (true) {
	            try {
	                System.out.print("ID 입력>>");
	                nowID = scan.nextInt();
	                scan.nextLine();
	                boolean idExists = false;
	                if (profchk) {
	                    for (Professor professor : professors) {
	                        if (professor.getID() == nowID) {
	                            idExists = true;
	                            System.out.println("ID 확인");
	                            System.out.println("신분: 교직원");
	                            System.out.println("이름: " + professor.getName());
	                            System.out.println("ID: " + nowID);
	                            break;
	                        }
	                    }
	                } else {
	                    for (Student student : students) {
	                        if (student.getID() == nowID) {
	                            idExists = true;
	                            System.out.println("ID 확인");
	                            System.out.println("신분: 학생");
	                            System.out.println("이름: " + student.getName());
	                            System.out.println("ID: " + nowID);
	                            break;
	                        }
	                    }
	                }
	                if (!idExists) {
	                    System.out.println("해당 아이디는 존재하지 않습니다. 다시 입력하세요.");
	                } else {
	                    break;
	                }
	            } catch (InputMismatchException e) {
	                System.out.println("유효한 숫자를 입력하세요.");
	                scan.nextLine();
	            }
	        }

	        try {
	            System.out.println();
	            int choice;
	            do {
	                System.out.println("==== 수강신청 프로그램 시작 ====");
	                displayCourseList();
	                System.out.println(profchk ? "1. 강의 등록" : "1. 수강 신청 및 정정");
	                System.out.println("2. 정렬 순서 변경");
	                System.out.println("3. 과목 검색");
	                System.out.println("4. 프로그램 종료");
	                System.out.print("선택 번호: ");
	                choice = scan.nextInt();
	                switch (choice) {
	                    case 1:
	                        if (profchk) addNewCourse();
	                        else enrollStudent();
	                        break;
	                    case 2:
	                        sortCourseList();
	                        break;
	                    case 3:
	                        searchCourse();
	                        break;
	                    case 4:
	                        System.out.println("프로그램을 종료합니다.");
	    	                scan.close();
	                        break;
	                    default:
	                        System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
	                        break;
	                }

	            } while (choice != 4);
	        } finally {
	        }
	    }

    private void displayCourseList() {
        switch (viewOption) {
            case 1:
                System.out.println(">>> 학생 뷰 <<<");
                for (Course course : courses) {
                    System.out.printf("%s\t%s\t%s\t%s\t%s%n", course.displayEnrolledStudents(), course.getProfessor().getName(),
                    		course.getCourseName(), course.getClassroom().getName(),  course.getSchedule());
                }
                break;
            case 2:
                System.out.println(">>> 교수 뷰 <<<");
                for (Course course : courses) {
                    System.out.printf("%s\t%s\t%s\t%s\t%s%n",  course.getProfessor().getName(), course.getClassroom().getName(), 
                            course.getCourseName(), course.getSchedule(), course.displayEnrolledStudents());
                }
                break;
            case 3:
                System.out.println(">>> 강의실 뷰 <<<");
                for (Course course : courses) {
                    System.out.printf("%s\t%s\t%s\t%s\t%s%n", course.getClassroom().getName(), course.getSchedule(), course.getProfessor().getName() 
                    		, course.getCourseName(), course.displayEnrolledStudents());
                }
                break;
            default:
                System.out.println(">>> 수강 신청 현황 (기본: 과목 뷰) <<<");
                for (Course course : courses) {
                    System.out.printf("%s\t%s\t%s\t%s\t%s%n", course.getCourseName(), course.getProfessor().getName(),
                            course.getClassroom().getName(), course.getSchedule(), course.displayEnrolledStudents());
                }
                break;
        }
    }

    private void sortCourseList() {
    	System.out.println("출력 뷰를 선택하세요.");
        System.out.println("1. 학생 뷰");
        System.out.println("2. 교수 뷰");
        System.out.println("3. 강의실 뷰");
        System.out.println("4. 과목 뷰");
    	viewOption=scan.nextInt();  	
        scan.nextLine();
    }

    private void searchCourse() {
        int chk = 0;
        // 과목 검색 기능 구현
        System.out.print("교수님 이름이나 과목명, 학수번호를 입력하세요: ");
        String searchKeyword = scan.next();
        scan.nextLine();
        System.out.println("\"" + searchKeyword + "\" 검색 결과:");
        for (Course course : courses) {
            if (course.getProfessor().getName().contains(searchKeyword)||course.getCourseName().contains(searchKeyword)||String.valueOf(course.getCourseID()).contains(searchKeyword)) {	//키워드 포함시 출력
                chk++;
                System.out.printf("%s\t%s\t%s\t%s\t%s%n", course.getCourseName(), course.getProfessor().getName(),
                        course.getClassroom().getName(), course.getSchedule(), course.displayEnrolledStudents());
            }
        }
        if (chk == 0)
            System.out.println("없음");
        else
            System.out.println(chk + "개의 검색 결과");
    }
    

    private void addNewCourse() {
        // 강의 신설 기능 구현
        System.out.println("과목명을 입력하세요: ");
        String courseName = scan.next();
        scan.nextLine();
        
        System.out.println("강의를 진행할 교실 호실을 입력하세요: ");
        for(Classroom classroom : classrooms) {
        	System.out.println(classroom.getName()+"(강의실: "+classroom.getClassroomID()+"호)");        	
        }
        int classNum = scan.nextInt();  
        scan.nextLine();
        System.out.println("해당 강의실에서 강의를 진행할 날짜와 시간을 숫자로 입력하세요: ");
        System.out.print("요일 입력(월:1,화:2, ..., 일:7)>>");
        int day = scan.nextInt();
        scan.nextLine();
        System.out.println("시간 입력(1~9(교시))>>");
        int time = scan.nextInt();
        scan.nextLine();
        
        courses.add(new Course(courseName, Professor.findProfessor(nowID, professors),Classroom.findClassroom(classNum, classrooms) ,day, time));
    }

    private void enrollStudent() {
        // 수강 신청 기능 구현      
        System.out.println("수강할 과목의 학수번호를 입력하세요: ");
        for(Course course : courses) {
        	System.out.println(course.getCourseName()+"(학수번호: "+course.getCourseID()+")");        	
        }
        
        int courseID = scan.nextInt();
        scan.nextLine();

        Course.findCourse(courseID, courses).enrollmentStudent(Student.findStudent(nowID, students));        

    }

    public static void main(String[] args) {
        EnrollmentSystem enrollmentSystem = new EnrollmentSystem();
        enrollmentSystem.run();
        return;
    }
}
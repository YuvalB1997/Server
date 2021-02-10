package bgu.spl.net.impl.BGRSServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the bgu.spl.net.impl.BGSServer.Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {



	private static class DatabaseHolder {
		private static Database instance = new Database();
	}

	private ConcurrentHashMap<String, Student> registeredStudents;
	private ConcurrentHashMap<String, Admin> registeredAdmins;
	private ConcurrentHashMap<Integer, Course> courses;


	/**
	 * {@param registerStudent} {@param ConncurrentHashMap} that maps a  {@param String} userName with the corresponding
	 * 					    {@param Student}.
	   {@param registerAdmin} {@param ConncurrentHashMap} that maps a  {@param String} userName with the corresponding
	 * 	  				    {@param Admin}.
	 * {@param courses} {@param ConncurrentHashMap} that maps a  {@param Integer} courseNum with the corresponding
	 * 		  		       {@param Course}.
	 */
	private Database(){
		registeredStudents = new ConcurrentHashMap<>();
		registeredAdmins = new ConcurrentHashMap<>();
		courses = new ConcurrentHashMap<>();
	}

	/**
	 * The method checks whether {@param courses} contains {@param course}.
	 * @return true if it does, false otherwise.
	 */
	public boolean containsCourse(Integer course){ return courses.containsKey(course); }
	/**
	 * The method checks whether {@param registredStudents} contains {@param student}.
	 * @return true if it does, false otherwise.
	 */
	public boolean containsStudent(String student){ return registeredStudents.containsKey(student); }
	/**
	 * The method checks whether {@param registeredAdmin} contains {@param admin}.
	 * @return true if it does, false otherwise.
	 */
	public boolean containsAdmin(String admin) { return registeredAdmins.containsKey(admin);}

	/**
	 * Register a Student to the system.
	 * @param userName the username of the student.
	 * @param passWord the password of the student.
	 * @return true if successful, false otherwise
	 */
	public boolean regStudent(String userName, String passWord)  {
				if (registeredStudents.containsKey(userName) || registeredAdmins.containsKey(userName))
					return false;
				Student student = new Student(userName, passWord);
				registeredStudents.put(userName, student);
				return true;
			}

	/**
	 * check if the username and match the password.
	 * @param userName the username being checked.
	 * @param password the password being checked.
	 * @return true if they match, false otherwise.
	 */

	public boolean checkPassword(String userName, String password) {
		synchronized (registeredStudents) {
			synchronized (registeredAdmins) {
				if (containsStudent(userName)) {
					if (registeredStudents.get(userName).getPassWord().equals(password))
						return true;
				}
				if (containsAdmin(userName))
					return registeredAdmins.get(userName).getPassWord().equals(password);
				return false;
			}
		}
	}

	/**
	 * Register an Admin to the system.
	 * @param userName the username of the Admin.
	 * @param passWord the password of the Admin.
	 * @return true if successful, false otherwise.
	 */
	public boolean regAdmin(String userName,String passWord) {
		if(registeredAdmins.containsKey(userName) || registeredStudents.containsKey(userName))
			return false;
		Admin admin= new Admin(userName, passWord);
		registeredAdmins.put(userName, admin);
		return true;
	}

	/**
	 * Check whether a user is an admin.
	 * @param userName the userName of the user we want to check.
	 * @return true if the user is admin, false otherwise.
	 */
	public boolean isAdmin(String userName) {
		return registeredAdmins.containsKey(userName);
	}

	/**
	 * Check whether a user is registered to a course.
	 * @param userName the username of the user we want to check.
	 * @param courseNum the course we want to check if the user registered too.
	 * @return true if the user is registered to the course, false otherwise.
	 */
	public boolean isRegistered(String userName, int courseNum){
		synchronized (registeredStudents) {
			return (registeredStudents.get(userName)).isRegistered(courseNum);
		}
	}
	private void regCourse(Course course){
		courses.put(course.getCourseNum(), course);
	}

	/**
	 * check if the course and the students are in the database, then if the student can register to the course
	 *         and the course has room, the function register him.
	 * @param student String which represent the userName of  {@param student}
	 * @param course Integer that represent the bgu.spl.net.impl.BGSServer.Course Num to which register {@param student}
	 * @return true if successful, false otherwise.
	 */
	public boolean regToCourse(String student,Integer course)  {
		synchronized (registeredStudents) {
			synchronized (courses) {
				if (registeredStudents.containsKey(student) && courses.containsKey(course)) {
					Student tempStudent = registeredStudents.get(student);
					Course tempCourse = courses.get(course);
					if (!tempCourse.isFull())
						if (tempStudent.registerCourse(tempCourse)) {
							tempCourse.Register(student);
							return true;
						}
				}
			}
		}

		return false;
	}

	/**
	 * check and return the amount of available seats in a course.
	 * @param courseNum the course we want to check
	 * @return the available seats in the course in {@param String} format
	 */
	public String avaiSeats(Integer courseNum) {
		synchronized (courses) {
			return courses.get(courseNum).avaiSeats();
		}
	}

	/**
	 * Return the course registered students in string format.
	 * @param courseNum the course we want to check for.
	 * @return {@param String} of the course registered students if there are students registered to the course
	 *            or "[]" otherwise.
	 */
	public String getRegStudents(Integer courseNum) {
		synchronized (courses) {
			if (courses.containsKey(courseNum)) {
				return courses.get(courseNum).getRegStudents().toString();
			}
			return "[]";
		}
	}

	/**
	 * Returns the course name
	 * @param courseNum the number of the course we want to check.
	 * @return {@param String} representing the course name
	 */
	public String getCourseName(Integer courseNum){
			return courses.get(courseNum).getCourseName();
	}

	/**
	 * Unregister the student from the course.
	 * @param student unReg {@param student} from {@param course}.
	 * @param course as seen above.
	 * @return true if successful, false otherwise.
	 */
	public boolean unRegCourse(String student, String course) {
		synchronized (registeredStudents) {
			synchronized (courses) {
				Integer courseNum = Integer.parseInt(course);
				if (registeredStudents.containsKey(student) && courses.containsKey(courseNum)) {
					Student tempStudent = registeredStudents.get(student);
					Course tempCourse = courses.get(courseNum);
					if (tempStudent.unRegister(courseNum)) {
						tempCourse.unRegister(student);
						return true;
					}
				}
			}
		}
				return false;
	}

			/**
			 * Return a string presantion of kdam-courses  of the course.
			 * @param courseNum the course we want to get the kdam-courses of.
			 * @return {@param string} of the kdam-courses.
			 */
			public String kdamCheck(Integer courseNum){
				synchronized (courses) {
						return courses.get(courseNum).getKdamCourseString();
					}
				}

			/**
			 * Return the courses a student is registered too in string format.
			 * @param userName the username of the user we want to get the registered courses.
			 * @return {@param String} of the student registered courses.
			 */
			public String getRegCourses(String userName) {
				synchronized (registeredStudents) {
					if (registeredStudents.get(userName).getRegisteredCourses().isEmpty())
						return "[]";
					else
						return registeredStudents.get(userName).getRegisteredCourses().toString();
				}
			}
			/**
			 * Retrieves the single instance of this class.
			 */
			public static Database getInstance() {
				return DatabaseHolder.instance;
			}

			/**
			 * Loades the courses from the file path specified
			 * into the bgu.spl.net.impl.BGSServer.Database, returns true if successful.
			 */
			boolean initialize(String coursesFilePath) throws IOException {
				FileReader input = new FileReader(coursesFilePath);
				BufferedReader bufRead = new BufferedReader(input);
				String myLine = null;
				while((myLine=bufRead.readLine())!=null){
					String[] fullInfo = myLine.split("\\|");
					int courseNum = Integer.parseInt(fullInfo[0]);
					String courseName = fullInfo[1];
					String[] listOfKdamCourses= fullInfo[2].substring(1,fullInfo[2].length()-1).split(",");
					int[] KdamCourses = new int [listOfKdamCourses.length];
					for(int i=0;i<listOfKdamCourses.length;i++)
						if(!listOfKdamCourses[i].equals(""))
							KdamCourses[i]=Integer.parseInt(listOfKdamCourses[i]);
					int numOfMaxStudent = 	Integer.parseInt(fullInfo[3]);
					Course newCourse = new Course(courseNum,courseName,KdamCourses,numOfMaxStudent);
					regCourse(newCourse);

				}
				return true;
			}
		}


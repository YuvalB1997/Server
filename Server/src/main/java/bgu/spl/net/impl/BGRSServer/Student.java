package bgu.spl.net.impl.BGRSServer;

import java.util.Vector;

/**
 * {@link  Student} extends {@link User} represent a student in the system.
 */
public class Student extends User {
    private Vector<Integer> registeredCourses;
    private Vector<Integer> passedCourses;


    /**
     * Constructor.
     * @param userName defines the userName of the student.
     * @param passWord defines the passWord of the student.
     * {@param registeredCourses} {@param vector} that represent the courses a student is
     *                 registered to.
     *{@param passedCourses} {@param vector} that represent the courses a student has passed.
     */

    public Student(String userName, String passWord) {
        super(userName, passWord);
        registeredCourses = new Vector<>();
        passedCourses= new Vector<>();

    }
    /**
     * Getters
     */

    public Vector<Integer> getPassedCourses() {
        return passedCourses;
    }

    public Vector<Integer> getRegisteredCourses() {
        return registeredCourses;
    }

    /**
     * @param courseNum check if the {@link Student} is registered to the course with {@param courseNum}
     * @return True if the student is registered to the course false otherwise.
     */
    public boolean isRegistered(int courseNum){
        return registeredCourses.contains(courseNum);
    }

    /**
     *
     * @param course Register a Student to the course.
     * @return true if successful, false otherwise.
     */
    public boolean registerCourse(Course course) {
        if(isRegistered(course.getCourseNum()) || course.isFull())
            return false;
        int[] kdamCourses = course.getKdamCoursesList();
        if (kdamCourses[0] != 0) { // check if all courses in the kdamCourses have been passed by the student
            for (int c : kdamCourses) {
                if (!passedCourses.contains(c)){
                return false;
            }
        }
    }
        registeredCourses.add(course.getCourseNum());
        passedCourses.add(course.getCourseNum());
        return true;
    }

    /**
     *
     * @param courseNum Unregister a {@link Student} from the {@link Course}.
     * @return true if successful, false otherwise.
     */
    public boolean unRegister(Integer courseNum){
        if(!registeredCourses.contains(courseNum))
            return false;
        registeredCourses.remove(courseNum);
        passedCourses.remove(courseNum);
        return true;
    }
}

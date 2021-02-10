package bgu.spl.net.impl.BGRSServer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Course {
    private int courseNum;
    private String courseName;
    private int[] KdamCoursesList;
    private int numOfMaxStudent;
    private AtomicInteger numRegisteredStudents;
    private Vector<String> regStudents;

    public Course(int courseNum, String courseName, int[] KdamCoursesList, int numOfMaxStudent){
        this.courseNum = courseNum;
        this.courseName = courseName;
        this.KdamCoursesList= KdamCoursesList;
        this.numOfMaxStudent = numOfMaxStudent;
        this.numRegisteredStudents = new AtomicInteger(0);
        regStudents = new Vector<String>();

    }
    /**
     * Getters!
     */
    public Vector<String> getRegStudents(){
        return regStudents;
    }
    public int getCourseNum() {
        return courseNum;
    }

    public String getCourseName() {
        return courseName;
    }
    public String getKdamCourseString() {
        String array = Arrays.toString(KdamCoursesList);
        if(array.equals("[0]"))
            array="[]";
        return  array ;
    }

    public int[] getKdamCoursesList() {
        if (KdamCoursesList == null)
            return new int[0];
        return KdamCoursesList;
    }
    public int getNumOfMaxStudent() {
        return numOfMaxStudent;
    }

    /**
     * @return true if the {@link Course} is full, false otherwise.
     */
    public boolean isFull(){
        return numOfMaxStudent == numRegisteredStudents.get();
    }

    /**
     * Threadsafe way to register/unRegister a student to the course.
     * @return boolean if the student got registered.
     */
    public boolean Register(String userName){
        int val = numRegisteredStudents.get();
        boolean registered;
        do {
            registered= numRegisteredStudents.compareAndSet(val, val+1);
            regStudents.add(userName);
            Collections.sort(regStudents);

        }while (!isFull() && (!registered));
        return  registered;
    }

    /**
     * Unregister a student from this course.
     * @param userName the username of the student we want to unregister.
     */
    public void unRegister(String userName){
        int val;
        do {
            val = numRegisteredStudents.get();
            regStudents.remove(userName);
        }while (!numRegisteredStudents.compareAndSet(val, val-1));
    }

    /**
     * The method return a string representing the available seats in x/y format
     * where x is the available seats and y is the max seats.
     * @return {@param String} that represent the amount of available seats.
     */
    public String avaiSeats(){
        Integer numOfSeatsAvailable = numOfMaxStudent - numRegisteredStudents.get();
        return numOfSeatsAvailable.toString() + "/" + numOfMaxStudent;
    }
}

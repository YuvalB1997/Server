package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.Process;
import bgu.spl.net.api.MessagingProtocol;

import java.util.HashMap;

/**
 * The MessagingProtocol holds a boolean indicator whether it needs to be terminated and an instance of the DataBase,
 * its also holds the userName of the user currently logged into it,
 * and whether this user is an admin, we used a hashmap to "link" each OPcode with its corresponding lambda.
 */
public class MessagingProtocolImp implements MessagingProtocol<RSMessage>{
    private boolean shouldTerminate = false;
    private HashMap<String, Process<String[]>> processMap;
    private Database database;
    private String user = null;
    private Boolean isAdmin;
    public MessagingProtocolImp(){
        database = Database.getInstance();
        isAdmin = false;
        processMap = new HashMap<String, Process<String[]>>();

        processMap.put("1",(data)->{// ADMINREG
            if(database.regAdmin(data[1],data[2])) {
                return new RSMessage(new String[]{"12", "1"});
            }
            return new RSMessage(new String[]{"13", "1"});
            });
        processMap.put("2",(data) -> {// STUDENTREG
           if(database.regStudent(data[1],data[2]))
               return  new RSMessage(new String[]{"12","2"});
           return new RSMessage(new String[]{"13","2"});
        });
        processMap.put("3",(data) -> {// LOGIN
         if(database.checkPassword(data[1], data[2])){
            if(user==null) {
                 user = data[1];
                 if (database.isAdmin(data[1]))
                     isAdmin = true;
             return new RSMessage(new String[]{"12", "3"});
            }
         }
         return new RSMessage(new String[]{"13","3"});
        });

        processMap.put("4",(data) -> {// LOGOUT
            if(!(user == null)) {
                shouldTerminate = true;
                user = null;
                if (isAdmin)
                    isAdmin = false;
                return new RSMessage(new String[]{"12", "4"});
            }
            return new RSMessage(new String[]{"13", "4"});
        });
        processMap.put("5",(data) -> {// COURSEREG
            Integer courseNum = Integer.parseInt(data[1]);
            if(user!=null && !isAdmin) {
                if (database.regToCourse(user,courseNum))
                    return new RSMessage(new String[]{"12","5"});
            }
            return new RSMessage(new String[]{"13", "5"});

        });
        processMap.put("6",(data) -> { // KDAMCHECK
            Integer courseNum = Integer.parseInt(data[1]);
            if(user == null || !database.containsCourse(courseNum))
                return new RSMessage(new String[] {"13","6"});
            String kdamCoursesTemp = database.kdamCheck(courseNum);
            return new RSMessage(new String[] {"12","6",kdamCoursesTemp});
        });
        processMap.put("7",(data) -> { // COURSESTAT
            Integer course = Integer.parseInt(data[1]);
            if(isAdmin && database.containsCourse(course)) {
                return new RSMessage(new String[]{"12","7","(" + course +")"+ " " +database.getCourseName(course)
                        , database.avaiSeats(course), database.getRegStudents(course)});
            }
            return new RSMessage(new String[]{"13","7"});


        });
        processMap.put("8",(data) -> {// STUDENTSTAT
            if(!isAdmin || !database.containsStudent(data[1])) {
                return new RSMessage(new String[]{"13", "8"});
            }
            String regCourses =  database.getRegCourses(data[1]);
            return new RSMessage(new String[]{"12","8",data[1],regCourses});
        });
        processMap.put("9",(data) ->{ // ISREGISTERED
           if(!isAdmin && user != null) {
               if (database.isRegistered(user,Integer.parseInt(data[1]))) {
                   return new RSMessage(new String[]{"12", "9", "REGISTERED"});
               }
               return new RSMessage(new String[]{"12","9","NOT REGISTERED"});
           }
           return new RSMessage(new String[]{"13","9"});
        });
        processMap.put("10",(data)->{ // UNREGISTERED
            if(user !=null && !isAdmin) {
                if(database.unRegCourse(user, data[1]))
                    return new RSMessage(new String[]{"12", "10"});
            }
            return new RSMessage(new String[]{"13","10"});
        });
        processMap.put("11", data ->{ // MYCOURSES
           if(user == null || isAdmin)
               return new RSMessage(new String[]{"13","11"});
           String regCourses =  database.getRegCourses(user);
           return new RSMessage(new String[]{"12","11",regCourses});
        });
    }


    @Override
    public RSMessage process(RSMessage msg) {
            String opCode = msg.getOpCode();
            return processMap.get(opCode).func(msg.getMessage());

    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}

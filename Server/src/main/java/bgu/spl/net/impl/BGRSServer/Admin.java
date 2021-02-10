package bgu.spl.net.impl.BGRSServer;

public class Admin extends User {
    /**
     * Constructor.
     * {@param isAdmin} is true since this user is an Admin.
     * @param userName defines the userName of the Admin.
     * @param passWord defines the passWord of the Admin.
     */
        public Admin(String userName, String passWord){
            super(userName, passWord);
            isAdmin=true;
    }
}

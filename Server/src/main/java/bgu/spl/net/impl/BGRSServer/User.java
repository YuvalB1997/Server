package bgu.spl.net.impl.BGRSServer;

public abstract class User {
    /**
     * {@param UserName} string that represent the username of the user.
     * {@param passWord} string that represent the password of the user.
     * {@param isAdmin} boolean that indicates if the user is admin.
     */
    String userName;
    String passWord;
    Boolean isAdmin;
    /**
     *
     * @param userName defines the userName of the user.
     * @param passWord defines the passWord of the user
     */
    public User(String userName, String passWord){
        this.userName = userName;
        this.passWord = passWord;
        isAdmin= false;
    }

    /**
     * getter.
     */
    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    /**
     * @return whether the user is admin.
     */
    public Boolean isAdmin() {
        return isAdmin;
    }
}

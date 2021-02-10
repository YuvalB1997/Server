package bgu.spl.net.impl.BGRSServer;

public class RSMessage {
    private String[] message;
    private String opCode;

    /**
     * Constructor
     * @param message set the message to {@param message}
     *                the first string in the message array represent the opcode of the message.
     */
    public RSMessage(String [] message){
        this.message = message;
        opCode= message[0];
    }

    /**
     * getters
     */
    public String[] getMessage() {
        return message;
    }

    public String getOpCode() {
        return opCode;
    }
}

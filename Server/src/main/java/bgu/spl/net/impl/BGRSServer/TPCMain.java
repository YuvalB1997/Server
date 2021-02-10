package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.srv.Server;

import java.io.IOException;

public class TPCMain {

    public static void main(String[] args) throws IOException {
        Database database = Database.getInstance();
        database.initialize("./Courses.txt");
        try(Server<RSMessage> server = Server.threadPerClient(Integer.parseInt(args[0]), () -> new MessagingProtocolImp()
        , () -> new MessageEncoderDecoderImp())){
            server.serve();
        }
        catch (IOException exp){exp.printStackTrace();}
    }
}

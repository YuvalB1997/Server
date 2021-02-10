package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.srv.*;

import javax.xml.crypto.Data;
import java.io.IOException;

public class ReactorMain {
   public static void main(String[] args) throws IOException {
       Database database = Database.getInstance();
       database.initialize("./Courses.txt");
       try(Reactor<RSMessage> reactor =  new Reactor( Integer.parseInt(args[1]),Integer.parseInt(args[0]), () -> new MessagingProtocolImp()
               ,() -> new MessageEncoderDecoderImp())){
           reactor.serve();
       }
       catch (IOException exp){exp.printStackTrace();}


    }
}

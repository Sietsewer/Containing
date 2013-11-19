/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Hendrik
 */
public class ContainingController {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final Server server;
        try {

            server = new Server(new Controller());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    server.Run();
                }
            }).start();
            
     
while(true){      Message m = new Message();
           m.setCommand(2);
           m.setParameters(new String[]{"Hallo"});
            server.SendCommand(m);}
        } catch (IOException ex) {
            Logger.getLogger(ContainingController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

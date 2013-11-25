/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author Hendrik
 */
@XmlRootElement(name = "message")
@XmlSeeAlso({CustomVector3f.class, Date.class,SimContainer.class })
public class Message {

    private int command;//Command number
    private Object[] parameters;//Parameters for command

    /**
     * Empty message
     */
    public Message() {
    }

    /**
     * Filled message
     * @param command
     * @param parameters
     */
    public Message(int command, Object[] parameters) {
        this.command = command;
        this.parameters = parameters;
    }

    /**
     *
     * @return command number
     */
    public int getCommand() {
        return command;
    }

    /**
     *
     * @param command
     */
    public void setCommand(int command) {
        this.command = command;
    }

    /**
     * parameters toevoegen
     *
     * @return parameters
     */
    @XmlElementWrapper(name = "parameters")
    @XmlElement(name = "parameter")
    public Object[] getParameters() {
        return parameters;
    }

    /**
     * parameters toevoegen aan de command
     *
     * @param parameters
     */
    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
    
    /**
     * Get message object from xml string
     * @param XML
     * @return
     */
    static  public Message decodeMessage(String XML)
    {
                try {
           JAXBContext context = JAXBContext.newInstance(Message.class);
           Unmarshaller um = context.createUnmarshaller();
           Message message = (Message) um.unmarshal(new StringReader(XML));
           return message;
       } catch (JAXBException ex) {           
           Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);           return null;
       }

    }
    /**
     * encode message from object to xml
     * @param message
     * @return
     */
    static   public String encodeMessage(Message message)
    {
        try {
            JAXBContext context = JAXBContext.newInstance(Message.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            String xml;
            StringWriter xmlString = new StringWriter();
            m.marshal(message, xmlString);
            xml = xmlString.toString();
           xml= xml.replace("\n", "");
            
            return xml;
        } catch (PropertyException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (JAXBException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
}

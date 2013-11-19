/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Hendrik
 */
@XmlRootElement(name = "message")
public class Message {
    
  private int command;

  private Object[] parameters;
  public int getCommand() {
    return command;
  }

  public void setCommand(int command) {
      this.command = command;
  }
    @XmlElementWrapper(name = "parameters")
  // XmlElement sets the name of the entities
  @XmlElement(name = "parameter")
  public Object[] getParameters() {
    return parameters;
  }

  public void setParameters(Object[] parameters) {
    this.parameters = parameters;
  }

  
}

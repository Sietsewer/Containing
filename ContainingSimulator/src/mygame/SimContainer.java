/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author Hendrik
 */
@XmlRootElement(name = "simcontainer")
@XmlSeeAlso({mygame.CustomVector3f.class, Date.class})
public class SimContainer {

    private  String id;
    private mygame.CustomVector3f indexPosition;

    public SimContainer() {
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public mygame.CustomVector3f getIndexPosition() {
        return indexPosition;
    }

    public void setIndexPosition(mygame.CustomVector3f indexPosition) {
        this.indexPosition = indexPosition;
    }
}

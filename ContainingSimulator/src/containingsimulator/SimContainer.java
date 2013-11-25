/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author Hendrik
 */
@XmlRootElement(name = "simcontainer")
@XmlSeeAlso({containingsimulator.CustomVector3f.class, Date.class})
public class SimContainer {

    private  String id;
    private containingsimulator.CustomVector3f indexPosition;

    public SimContainer(String id, CustomVector3f indexPosition) {
        this.id = id;
        this.indexPosition = indexPosition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public containingsimulator.CustomVector3f getIndexPosition() {
        return indexPosition;
    }

    public void setIndexPosition(containingsimulator.CustomVector3f indexPosition) {
        this.indexPosition = indexPosition;
    }
}

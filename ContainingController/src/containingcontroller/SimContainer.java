/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author Hendrik
 */
@XmlRootElement(name = "simcontainer")
@XmlSeeAlso({CustomVector3f.class, Date.class})
public class SimContainer {

    private String id;
    private CustomVector3f indexPosition;

    /**
     *
     */
    public SimContainer() {
    }

    SimContainer(Container c) {
        this.id = c.getId();
        this.indexPosition = c.getPosition();
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public CustomVector3f getIndexPosition() {
        return indexPosition;
    }

    /**
     *
     * @param indexPosition
     */
    public void setIndexPosition(CustomVector3f indexPosition) {
        this.indexPosition = indexPosition;
    }
}

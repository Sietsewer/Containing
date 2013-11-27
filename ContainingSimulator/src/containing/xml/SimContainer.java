/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containing.xml;


import org.simpleframework.xml.*;

/**
 *
 * @author Hendrik
 */
@Root
public class SimContainer {

    @Attribute
    private String id;
    @Element
    private CustomVector3f indexPosition;

    /**
     *
     */
    public SimContainer() {
    }
    public SimContainer(String id, CustomVector3f pos)
    {
        this.id = id;
        this.indexPosition = pos;
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
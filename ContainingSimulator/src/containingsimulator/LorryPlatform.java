/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Sietse
 */
public class LorryPlatform extends Node {

    public String id;
    public ParkingSpot parkingSpot;
    private Container container;
    private String agvID;
    private Spatial crane;

    public LorryPlatform(Spatial crane) {
      this.crane = crane;
      this.attachChild(crane);
    }
    
    public LorryPlatform(String id) {
        this.id = id;
        parkingSpot.translation = new Vector3f(this.getWorldTranslation().x, this.getWorldTranslation().y, this.getWorldTranslation().z);
    }

    public void fromLorry(Transporter lorry) {

        this.container = lorry.getContainer(new Vector3f(0f, 0f, 0f));
    }

    public void toLorry(Transporter lorry) {

        lorry.setContainer(container);
        this.container = null;
    }

    public void fromAGV(AGV agv) {
        this.container = agv.getContainer();
    }

    public void toAGV(AGV agv) {
        agv.setContainer(this.container);
        this.container = null;
    }
    
    public void debugRender(Spatial agv, Spatial transporter){
        this.attachChild(agv);
        this.attachChild(transporter);
        transporter.setLocalTranslation(0f, 0f, 20f);
    }
}

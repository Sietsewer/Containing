/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Sietse
 */
public class LorryPlatform extends Node {

    public String id;
    public ParkingSpot parkingSpot;
    private Container container;
    private String agvID;
    private AGV agv;
    private Transporter lorry;

    public LorryPlatform() {
      
    
    }
    public LorryPlatform(String id) {
        this.id = id;
        parkingSpot.translation = new Vector3f(this.getWorldTranslation().x, this.getWorldTranslation().y, this.getWorldTranslation().z);
    }

    public void lorryArrive() {
    }

    public void lorryDepart() {
    }

    public void agvArrived(AGV arrivedAGV) {
    }

    public void agvDepard() {
    }

    public void fromLorry() {

        this.container = lorry.getContainer(new Vector3f(0f, 0f, 0f));
    }

    public void toLorry() {

        this.lorry.setContainer(container);
        this.container = null;
    }

    public void fromAGV() {
      
    }
    

    public void fromAGV(AGV agv) {
        this.container = agv.getContainer();
    }

    public void toAGV() {
    }

    

    public void toAGV(AGV agv) {
        agv.setContainer(this.container);
        this.container = null;
    }
}

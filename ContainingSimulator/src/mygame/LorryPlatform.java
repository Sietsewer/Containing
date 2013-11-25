/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Sietse
 */
public class LorryPlatform extends Node{
    
    public String id;
    public ParkingSpot parkingSpot;
            
    private Container container;
    private Transporter lorry;
    
    public LorryPlatform(String id){
        this.id = id;
        parkingSpot.translation = new Vector3f(this.getWorldTranslation().x,this.getWorldTranslation().y,this.getWorldTranslation().z);
    }

    public void fromLorry(){
        this.container = lorry.getContainer(new Vector3f(0f,0f,0f));
    }

    public void toLorry(){
        this.lorry.setContainer(container);
        this.container = null;
    }

    public void fromAGV(AGV agv){
        this.container = agv.getContainer();
    }

    public void toAGV(AGV agv){
        agv.setContainer(this.container);
        this.container = null;
    }
}

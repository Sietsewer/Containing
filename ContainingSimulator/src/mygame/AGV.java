/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import mygame.Container;
import mygame.Waypoint;

/**
 *
 * @author Sietse
 */
public class AGV extends Node {

    public Container container;
    public String id;
    public float speed;
    
    private ArrayList<Waypoint> waypointList;
    private float deltaX;
    private float deltaY;
    
    private Spatial viewModel;

    public AGV(String ID, Spatial viewModel) {
        this.id = ID;
        this.viewModel = viewModel.clone();
    }
    
    /**
     * 
     * @param tpf Duration of a frame, will be multiplied for time modifier.
     */
    public void updateLocation(float tpf, float timeSpeed){
        this.setLocalTranslation(((deltaX*timeSpeed)*speed)*tpf, 10f, ((deltaY*timeSpeed)*speed)*tpf);
    }
    /**
     * Adds waypoints to the current list of waypoints.
     * @param waypoints The waypoints to be added.
     */
    public void addWaypoints(ArrayList<Waypoint> waypoints){
        this.waypointList.addAll(waypoints);
    }
    /**
     * Removes the current waypoint, the one reached, and makes the AGV snap towards the next waypoint on the list.
     */
    private void nextWaypoint(){
        this.waypointList.remove(0);
        this.lookAt(this.waypointList.get(0).getLocalTranslation(), com.jme3.math.Vector3f.UNIT_Y);
        deltaX = (float)Math.sin(this.getLocalRotation().toAngles(null)[1]);
        deltaY = (float)Math.cos(this.getLocalRotation().toAngles(null)[1]);
    }
    /**
     * When called, places a container on the AGV. null can be sent.
     * @param container The container to be placed on the AGV.
     * @return True if the container was received, False if there already is a container.
     */
    public boolean setContainer(Container container){
        if(this.container == null){
            this.container = container;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Gets the container currently on the AGV, and removes it from the AGV.
     * @return the container that was once on the AGV. Null if the AGV was empty.
     */
    public Container getContainer(){
        Container tempCont;
        tempCont = (Container)this.container.clone();
        this.container = null;
        return tempCont;
    }
    /**
     * @return null if there is no container. Returns the ID if there is one.
     */
    public String getContainerID(){
        return container == null ? null : container.id;
    }
}
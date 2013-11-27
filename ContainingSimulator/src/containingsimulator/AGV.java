/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Quaternion;
import com.jme3.math.Spline;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;

/**
 *
 * @author Sietse
 */
public class AGV extends Node implements MotionPathListener{

    public Container container;
    public String id;
    
    
    private MotionEvent motionEvent;
    private ArrayList<String> waypointList;
    private float deltaX;
    private float deltaY;
    
    MotionPath path = new MotionPath();
    
    
    private Spatial viewModel;

    public AGV(String ID, Spatial viewModel) {
        this.waypointList = new ArrayList();
        this.id = ID;
        this.viewModel = viewModel.clone();
        path.setCycle(false);
        path.setPathSplineType(Spline.SplineType.Linear);
        this.path.addListener(this);
        motionEvent = new MotionEvent(this,this.path);
        this.attachChild(viewModel);
    }
    
    /**
     * Adds waypoints to the current list of waypoints.
     * @param waypoints The waypoints to be added.
     */
    public void addWaypoints(String[] waypoints){
        this.path.clearWayPoints();
        
        for(String pathID : waypoints){
            path.addWayPoint(Path.getVector(pathID));
            waypointList.add(pathID);
        }
        
        this.motionEvent.play();
    }
    /**
     * Removes the current waypoint, the one reached, and makes the AGV snap towards the next waypoint on the list.
     */
    private void nextWaypoint(int wayPointIndex){
        this.waypointList.remove(0);
        //this.lookAt(this.waypointList.get(1).location, com.jme3.math.Vector3f.UNIT_Y);
        this.motionEvent.setSpeed((this.container == null?0.6f:.05f));//Sets full speed (20km/h) if empty, half if full(in m/s)
        
    }
    /**
     * When called, places a container on the AGV. null can be sent.
     * @param container The container to be placed on the AGV.
     * @return True if the container was received, False if there already is a container.
     */
    public boolean setContainer(Container container){
        if(this.container == null){
            this.container = container;
            this.attachChild(container);
            this.container.setLocalTranslation(0f, 3f, 0f);
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
        this.detachChild(this.container);
        this.container = null;
        
        return tempCont;
    }
    /**
     * @return null if there is no container. Returns the ID if there is one.
     */
    public String getContainerID(){
        return container == null ? null : container.id;
    }

    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        if(waypointList.size() > 2){
            nextWaypoint(wayPointIndex);
        } else {
            jumpToPark(null);//NOG NIET KLAAR <<<<<<<<<
            Main.sendReady(id);
        }
        
    }
    
    private void jumpToPark(ParkingSpot spot){
        this.setLocalTranslation(spot.translation);
        this.setLocalRotation(new Quaternion().fromAngles(0f, spot.rotation, 0f));
    }
}
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
import com.jme3.math.Vector3f;
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
     * 
     * @param tpf Duration of a frame, will be multiplied for time modifier.
     */
    public void updateLocation(float tpf, float timeSpeed){
        //this.setLocalTranslation(((deltaX*timeSpeed)*speed)*tpf, 10f, ((deltaY*timeSpeed)*speed)*tpf);
        
    }
    /**
     * Adds waypoints to the current list of waypoints.
     * @param waypoints The waypoints to be added.
     */
    public void addWaypoints(String[] waypoints){
        this.path.clearWayPoints();
        for(int i = 0; i < waypoints.length; i++){
            waypointList.add(waypoints[i]);
        }
        for(String wp : waypoints){
            path.addWayPoint(Path.getVector(wp));
        }
        
        this.motionEvent.play();
    }
    /**
     * Removes the current waypoint, the one reached, and makes the AGV snap towards the next waypoint on the list.
     */
    private void nextWaypoint(int wayPointIndex){
        this.waypointList.remove(0);
        this.lookAt(Vector3f.ZERO, com.jme3.math.Vector3f.UNIT_Y);
        this.motionEvent.setSpeed(0f);
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
        if(waypointList.size() < 2){
            jumpToPark(null);//NOG NIET KLAAR <<<<<<<<<
        } else {
            nextWaypoint(wayPointIndex);
        }
        
    }
    
    private void jumpToPark(ParkingSpot spot){
        this.setLocalTranslation(spot.translation);
        this.setLocalRotation(new Quaternion().fromAngles(0f, spot.rotation, 0f));
    }
}
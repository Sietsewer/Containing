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
 * Class which represents the AGV in simulator.
 * @author Sietse
 */
public class AGV extends Node implements MotionPathListener{

    public Container container;//The contianer that is mounted on the AGV.
    public String id;//ID string of the AGV
    public ParkingSpot pSpot;//Holds the current parking spot of the AGV.
    boolean up;//holds (if the parking spot is in a buffer) if the parking spot is in the up, or down position.
    
    private Crane targetCrane;//The crane at the end of the path.
    private MotionEvent motionEvent;//Controls the motionpath that the AGV uses.
    private MotionPath path;//Holds the path of the AGV.
    private Spatial viewModel;//Model of the AGV.
    private boolean pathWasPlaying = false;
    /**
     * Constructor. Inits all variable required.
     * @param ID ID of the AGV.
     * @param viewModel Model of the AGV.
     */
    public AGV(String ID, Spatial viewModel) {
        this.id = ID;
        this.viewModel = viewModel.clone();
        path = new MotionPath();
        path.setCycle(false);
        path.setPathSplineType(Spline.SplineType.Linear);
        path.setCurveTension(1f);
        this.path.addListener(this);
        motionEvent = new MotionEvent(this,this.path);
        this.attachChild(viewModel);
    }
    
    /**
     * Adds waypoints to the current list of waypoints.
     * @param waypoints The waypoints to be added.
     */
 public void addWaypoints(String[] waypoints,Crane targetCrane){
        this.path.clearWayPoints();
        
        for (int i = 0; i < waypoints.length; i++) {
            path.addWayPoint(Path.getVector(waypoints[i]));
        }
        this.targetCrane = targetCrane;
        motionEvent.setInitialDuration(path.getLength()/11.11f/Main.globalSpeed);
        this.motionEvent.play();
        pathWasPlaying = true;
        if(this.pSpot != null){
            this.pSpot.occupied = false;
        }
    }
    /**
     * Removes the current waypoint, the one reached, and makes the AGV snap towards the next waypoint on the list.
     */
private void nextWaypoint(int wayPointIndex){
        this.lookAt(path.getWayPoint(wayPointIndex+1),Vector3f.UNIT_Y);
        this.motionEvent.setSpeed((this.container == null?2f:1f));
        
        /**
         * NOTE:
         * Beware when setting speed. Speed is calculated by dividing the speed var with base speed.
         * actualSpeed = motionPath.initialSpeed/motionPath.speed
         * 
         * 11.11 m/s = 40 kph
         * 5.55  m/s = 20 kph
         */
        
    }
    /**
     * When called, places a container on the AGV. null can be sent.
     * @param container The container to be placed on the AGV.
     * @return True if the container was received, False if there already is a container.
     */
    public void globalSpeedChanged(){
        motionEvent.setInitialDuration(path.getLength()/11.11f/Main.globalSpeed);
    }
    public void pausePlay(boolean pause)
    {
            if(pathWasPlaying)
            { 
                if(!pause)
                {
                    motionEvent.play();
                }
                else
            {
                motionEvent.pause();
            }
            }
            
            
    }
    public boolean setContainer(Container container){
        if(this.container == null){
            this.container = container;
            this.attachChild(container);
            this.container.setLocalTranslation(0f, 4f, 0f);
            this.container.setLocalRotation(this.viewModel.getLocalRotation());
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
    
    public Container getContainerObject()
    {
        return this.container;
    }
    /**
     * @return null if there is no container. Returns the ID if there is one.
     */
    public String getContainerID(){
        return container == null ? null : container.id;
    }

    /**
     * Moves AGV to next waypoint, stops AGV and sends READY to controller once
     * the end is reached
     * @param motionControl MotionEvent class
     * @param wayPointIndex current index of list of waypoints
     */
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        if(wayPointIndex+1 == path.getNbWayPoints()){
            if(targetCrane instanceof BufferCrane){
                jumpToPark(((BufferCrane)targetCrane).getBuffer().getBestParkingSpot(up));
            }else{
                 jumpToPark(targetCrane.getParkingspot());
            }
            pathWasPlaying = false;
            Main.sendReady(id);
        } else {
            nextWaypoint(wayPointIndex);
        }

    }
    
    /**
     * Snaps AGV to parking spot location and sets its rotation
     * @param spot the parking spot which to snap the AGV to
     */
    public void jumpToPark(ParkingSpot spot){
        this.setLocalTranslation(spot.translation);
        this.setLocalRotation(new Quaternion().fromAngles(0f, spot.rotation, 0f));
        this.pSpot = spot;
        pSpot.occupied = true;
    }
}
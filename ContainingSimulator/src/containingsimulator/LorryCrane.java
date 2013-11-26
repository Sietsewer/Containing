/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.animation.LoopMode;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Len
 */
public class LorryCrane extends Crane implements MotionPathListener {

    public ParkingSpot parkingSpot = new ParkingSpot(Vector3f.ZERO,0);
    private Container container;
    private String agvID;
    private AGV agv;
    
    
    public LorryCrane(String id, Vector3f pos, Spatial base, Spatial hook) 
    {
         super(id,pos,base,hook);
        
         this.hook = this.hook.scale(0.4f);
         this.attachChild(this.hook);
         this.hook.setLocalTranslation(new Vector3f(0,6.8f,0));
    }
    
      public boolean isbusy()
    {
        return this.busy;
    }
    
    public Container getContainer()
    {
        return this.container;
    }
    public String getId()
    {
        return id;
    }
    public boolean isMoving()
    {
        return this.action!=0;
    }

    @Override
    public  void loadContainer(Transporter transporter)
    {
        target = transporter.getWorldTranslation();
    }

    //updates crane motion
    public void update(float tpf)
    {
        if(target==null)
        {
            return;
        }
        
        updateSpeed();

        switch(action)
        {
            case 0: //nothing
                return;
            case 1: 
                if(!baseControl.isEnabled())
                {
                   moveBase();
                }
                break;
            case 2:
                if(!hookControl.isEnabled())
                {
                   moveHook();
                }
                break;
            case 3:
                if(!hookControl.isEnabled())
                {
                    moveHook2();
                }
                break;
            case 4:
                if(!baseControl.isEnabled())
                {
                   moveBase2();
                }
                break;
            case 5:
             System.out.println("crane is back in position");
             action = 0;
             busy = false;
             target = null;
                break;
        }
        
        
    }
    
 
    
    private void updateSpeed()
    {
        baseControl.setInitialDuration(baseDur/Main.globalSpeed);
        hookControl.setInitialDuration(hookDur/Main.globalSpeed);
    }
    
    private void moveBase()
    {
         basePath.clearWayPoints();
         basePath.addWayPoint(this.getLocalTranslation());
         basePath.addWayPoint(target);
         baseControl.play();
    }
    
    private void moveBase2()
    {
         basePath.addWayPoint(basePath.getWayPoint(0));
         basePath.removeWayPoint(0);
         baseControl.play();
    }

    private void moveHook()
    {
        hookPath.clearWayPoints();
        hookPath.addWayPoint(hook.getLocalTranslation());
        hookPath.addWayPoint(new Vector3f(hook.getLocalTranslation().x,target.y-this.getLocalTranslation().y,hook.getLocalTranslation().z));
        hookControl.play();
    }
    
    private void moveHook2()
    {
        hookPath.addWayPoint(hookPath.getWayPoint(0));
        hookPath.removeWayPoint(0);
        hookControl.play();
    }

   public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) 
    {
       action+=wayPointIndex;
    }


    
    public void debugRender(Spatial agv, Spatial transporter){
        this.attachChild(agv);
        this.attachChild(transporter);
        transporter.setLocalTranslation(0f, 0f, 20f);
    }
}

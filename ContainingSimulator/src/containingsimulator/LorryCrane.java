/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.animation.LoopMode;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Len
 */
public class LorryCrane extends Crane implements MotionPathListener {

    private Vector3f parkingSpot;
    private Container container;
    private String agvID;
    
    private Spatial hook;
    private Spatial base;
    private int action = 0;
    private Vector3f target;
    
    
    private static final float baseDur = 2f;
    private static final float hookDur = 2f;


    
    private AGV agv;
    //motionpaths for moving objects
    private MotionPath basePath = new MotionPath();
    private MotionPath hookPath = new MotionPath();
    
    private MotionEvent baseControl;
    private MotionEvent hookControl;
    private boolean busy = false;

    public LorryCrane(String id, Vector3f pos, Spatial base, Spatial hook) 
    {
        this.id = id;
         this.position = pos;
         this.base = base.clone();
         this.hook = hook.clone();
         
         this.attachChild(this.base);
         this.attachChild(this.hook);
         
         this.hook.setLocalTranslation(new Vector3f(0,25,0));

         baseControl = new MotionEvent(this,basePath,baseDur/Main.globalSpeed,LoopMode.DontLoop);
         hookControl = new MotionEvent(this.hook,hookPath,hookDur/Main.globalSpeed,LoopMode.DontLoop);
       
         basePath.setCycle(false);
         hookPath.setCycle(false);

         basePath.addListener(this);
         hookPath.addListener(this);
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
    @Override
    public void getContainer(Transporter transporter,Container cont)
    {
        if(cont != null && this.container == null)
        {
        this.container = cont;
        this.target = cont.realPosition;
        action = 1;
        busy = true;
        }
    }
   
    public void getContainer(Vector3f pos)
    {
        if(pos != null)
        {
      //  this.container = cont;
        this.target = pos;
        action = 1;
        busy = true;
        }
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
    
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) 
    {
       action+=wayPointIndex;
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




    
    public void debugRender(Spatial agv, Spatial transporter){
        this.attachChild(agv);
        this.attachChild(transporter);
        transporter.setLocalTranslation(0f, 0f, 20f);
    }
}

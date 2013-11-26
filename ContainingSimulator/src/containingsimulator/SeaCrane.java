/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.PlayState;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author User
 */
public class SeaCrane extends Crane implements MotionPathListener{

    private int action = 0;
    private Vector3f target;
    private Container container;
    
    private static final float sliDur = 2f;
    private static final float baseDur = 2f;
    private static final float hookDur = 2f;

    private Node sNode = new Node();
    private  Spatial base;
    private  Spatial slider;
    private  Spatial hook;
    
    private AGV agv;
    //motionpaths for moving objects
    private MotionPath basePath = new MotionPath();
    private MotionPath sliderPath = new MotionPath();
    private MotionPath hookPath = new MotionPath();
    
    private MotionEvent baseControl;
    private MotionEvent sliderControl;
    private MotionEvent hookControl;
    private boolean busy = false;


    public SeaCrane(String id, Vector3f basePos, Spatial base, Spatial slider, Spatial hook)
    {
         this.id = id;
         this.position = basePos;
         this.base = base.clone();
         this.slider = slider.clone();
         this.hook = hook.clone();
         
         this.attachChild(this.base);
         sNode.attachChild(this.slider);
         sNode.attachChild(this.hook);
         this.attachChild(this.sNode);
         
         this.hook.setLocalTranslation(new Vector3f(0,25,0));

         baseControl = new MotionEvent(this,basePath,baseDur/Main.globalSpeed,LoopMode.DontLoop);
         sliderControl = new MotionEvent(this.sNode,sliderPath,sliDur/Main.globalSpeed,LoopMode.DontLoop);
         hookControl = new MotionEvent(this.hook,hookPath,hookDur/Main.globalSpeed,LoopMode.DontLoop);
       
         basePath.setCycle(false);
         sliderPath.setCycle(false);
         hookPath.setCycle(false);

         basePath.addListener(this);
         sliderPath.addListener(this);
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
    public  void loadContainer(Container cont)
    {
        
    }
    @Override
    public void getContainer(Container cont)
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
                if(!sliderControl.isEnabled())
                {
                   moveSlider();
                }
                break;
            case 3:
                if(!hookControl.isEnabled())
                {
                   moveHook();
                }
                break;
            case 4:
                if(!hookControl.isEnabled())
                {
                    moveHook2();
                }
                break;
            case 5:
                 if(!sliderControl.isEnabled())
                {
                    moveSlider2();
                }
                break;
            case 6:
                if(!baseControl.isEnabled())
                {
                   moveBase2();
                }
                break;
            case 7:
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
        sliderControl.setInitialDuration(sliDur/Main.globalSpeed);
        hookControl.setInitialDuration(sliDur/Main.globalSpeed);
    }
    
    private void moveBase()
    {
         basePath.clearWayPoints();
         basePath.addWayPoint(this.getLocalTranslation());
         basePath.addWayPoint(new Vector3f(this.getLocalTranslation().x,this.getLocalTranslation().y,target.z));
         baseControl.play();
    }
    
    private void moveBase2()
    {
         basePath.addWayPoint(basePath.getWayPoint(0));
         basePath.removeWayPoint(0);
         baseControl.play();
    }
    
    private void moveSlider()
    {
         sliderPath.clearWayPoints();
         sliderPath.addWayPoint(sNode.getLocalTranslation());
         sliderPath.addWayPoint(new Vector3f(
                 target.x-this.getWorldTranslation().x
                 ,sNode.getLocalTranslation().y,
                 sNode.getLocalTranslation().z));
         sliderControl.play();
    }
    
    private void moveSlider2()
    {
        sliderPath.addWayPoint(sliderPath.getWayPoint(0));
        sliderPath.removeWayPoint(0);
        sliderControl.play();
    }
    
    private void moveHook()
    {
        hookPath.clearWayPoints();
        hookPath.addWayPoint(hook.getLocalTranslation());
        hookPath.addWayPoint(new Vector3f(target.x-sNode.getWorldTranslation().x,target.y-sNode.getWorldTranslation().y,sNode.getLocalTranslation().z));
        hookControl.play();
    }
    
    private void moveHook2()
    {
        hookPath.addWayPoint(hookPath.getWayPoint(0));
        hookPath.removeWayPoint(0);
        hookControl.play();
    }
}


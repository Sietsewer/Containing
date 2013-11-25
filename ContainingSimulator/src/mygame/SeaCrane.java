/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.LoopMode;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author User
 */
public class SeaCrane extends Crane implements MotionPathListener{
    
    


    private float baseInterval = 50f;

    private boolean working;
    private Vector3f defPosCable;
    private int action = 0;
    private Vector3f target;

    private static float baseSpeed = 1f;
    private static float sliderSpeed = 2f;
    private static float hookSpeed = 2f;
    
    private  Spatial base;
    private  Spatial slider;
    private  Spatial hook;
    private Container container;
    private AGV agv;
    
    //motionpaths for moving objects
    private MotionPath basePath = new MotionPath();
    private MotionPath sliderPath = new MotionPath();
    private MotionPath hookPath = new MotionPath();
    
    private MotionEvent baseControl;
    private MotionEvent sliderControl;
    private MotionEvent hookControl;

    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        System.out.println(wayPointIndex);
        if(motionControl.equals(baseControl))
        { System.out.println("asdad");
            switch(wayPointIndex)
            {
                case 0: //start
                    break;
                case 2:
                    action = 2;
                    motionControl.stop();
                    break;
                case 1:
                    break;
            }
            
        }
        else if(motionControl.equals(sliderControl))
        {
            switch(wayPointIndex)
            { 
                case 0:
                    break;
                case 1:
                    action = 3;
                    motionControl.stop();
                    break;
                
            }
            
        }
        else if(motionControl.equals(hookControl))
        {
            switch(wayPointIndex)
            {
                case 0:
                    break;
                case 1:
                    action = 4;
                    motionControl.stop();
                    break;
                
            }
        }
    }
    
    
    /*
     0 = default, crane is not in action
     1 = go to position of container
     2 = move cable above container
     3 = attach cable to container
     4 = move cable up
     5 = move cable back to default pos
     6 = move container down
     7 = detach container
     8 = move cable up to default pos
     */
    public SeaCrane(String id, Vector3f basePos, Spatial base, Spatial slider, Spatial hook)
    {
         this.id = id;
         this.position = basePos;
         this.base = base.clone();
         this.slider = slider.clone();
         this.hook = hook.clone();
         
        
         
         this.attachChild(this.base);
         this.attachChild(this.slider);
         this.attachChild(this.hook);
         this.hook.setLocalTranslation(new Vector3f(0,25,0));
        
        
         
         
         baseControl = new MotionEvent(this.base,basePath,baseSpeed,LoopMode.DontLoop);
         sliderControl = new MotionEvent(this.slider,sliderPath,sliderSpeed,LoopMode.DontLoop);
        hookControl = new MotionEvent(this.hook,hookPath,hookSpeed,LoopMode.DontLoop);
       
       
        
        basePath.addListener(this);
        sliderPath.addListener(this);
        hookPath.addListener(this);
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
    
    private void init_MotionPaths()
    {
        if(target!=null)
        {
            basePath.clearWayPoints();
            basePath.addWayPoint(base.getWorldTranslation());
            basePath.addWayPoint(new Vector3f(this.getWorldTranslation().x,this.getWorldTranslation().y,target.z));
            
            sliderPath.clearWayPoints();
            sliderPath.addWayPoint(new Vector3f(slider.getWorldTranslation().x,slider.getWorldTranslation().y,target.z));
            
            hookPath.clearWayPoints();
            hookPath.addWayPoint(new Vector3f(hook.getWorldTranslation().x,target.y,target.z));
        }
    }


   

    
    @Override
    public  void loadContainer(Container cont)
    {
        
    }
    @Override
    public void getContainer(Container cont)
    {
        if(this.container == null)
        {
        this.container = cont;
        init_MotionPaths();
        action = 1;
        }
    }
   
    public void getContainer(Vector3f pos)
    {
        if(pos != null)
        {
        
      //  this.container = cont;
        this.target =pos;
        init_MotionPaths();
        action = 1;
        }
    }
    
    
    //updates crane motion
    public void update(float tpf)
    {
        if(target==null) //do nothing
        {
        return;
        }
        
        switch(action)
        {
            case 0: //nothing
            
                break;
            case 1: //move crane to position infront of container
                if(!baseControl.isEnabled())
                {
                baseControl.play();
                System.out.println("crane " + this.id + " arrived at position");
                }
                break;
            case 2: 
                if(!sliderControl.isEnabled())
                {
                sliderControl.play();
                }
                break;
            case 3://cable in position
                if(!hookControl.isEnabled())
                {
                hookControl.play();
                    System.out.println("crane " + this.id + " attached container");
                }
                break;
        }
    }
    
    
    
   
}


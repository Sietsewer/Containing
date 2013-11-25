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


    private static float baseSpeed = 15f;
    private static float sliderSpeed = 25f;
    private static float hookSpeed = 25f;
    
    private static Spatial base;
    private static Spatial slider;
    private static Spatial hook;
    private Container container;
    private AGV agv;
    
    //motionpaths for moving objects
    private MotionPath basePath;
    private MotionPath sliderPath;
    private MotionPath hookPath;
    
    private MotionEvent baseControl = new MotionEvent(base,basePath,baseSpeed,LoopMode.DontLoop);
    private MotionEvent sliderControl = new MotionEvent(slider,sliderPath,sliderSpeed,LoopMode.DontLoop);
    private MotionEvent hookControl = new MotionEvent(hook,hookPath,hookSpeed,LoopMode.DontLoop);

    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        
        if(motionControl.equals(baseControl))
        {
            switch(wayPointIndex)
            {
                case 0: //start
                    break;
                case 1:
                    action = 2;
                    motionControl.stop();
                    break;
                case 2:
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
    public SeaCrane(String id, Vector3f basePos)
    {
         this.id = id;
         this.position = basePos;
         
         init_MotionPaths();
         
         this.attachChild(base);
         this.attachChild(slider);
         this.attachChild(hook);
         this.setLocalTranslation(this.position);
        
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
        if(container!=null)
        {
            basePath.clearWayPoints();
            basePath.addWayPoint(this.getWorldTranslation());
            basePath.addWayPoint(new Vector3f(this.getWorldTranslation().x,this.getWorldTranslation().y,container.getWorldTranslation().z));
            
            sliderPath.clearWayPoints();
            sliderPath.addWayPoint(new Vector3f(slider.getWorldTranslation().x,slider.getWorldTranslation().y,container.getWorldTranslation().z));
            
            hookPath.clearWayPoints();
            hookPath.addWayPoint(new Vector3f(hook.getWorldTranslation().x,container.getWorldTranslation().y+container.size.y,container.getWorldTranslation().z));
        }
    }


    //ugly crane attached to node
    public static void init_Models(Spatial b, Spatial s, Spatial h)
    {
      base = b;
      slider = s;
      hook = h;
      
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
    
    
    //updates crane motion
    public void update(float tpf)
    {
        if(container==null) //do nothing
        {
        return;
        }
        
        switch(action)
        {
            case 0: //nothing
            
                break;
            case 1: //move crane to position infront of container
                baseControl.play();
                System.out.println("crane " + this.id + " arrived at position");
                break;
            case 2: 
                sliderControl.play();
                break;
            case 3://cable in position
                hookControl.play();
                    System.out.println("crane " + this.id + " attached container");
                break;
        }
    }
    
    
    
   
}


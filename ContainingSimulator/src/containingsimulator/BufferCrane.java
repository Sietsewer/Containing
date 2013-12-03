/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author User
 */
public class BufferCrane extends Crane {
    
    Buffer buffer;
    
    public BufferCrane(String id, Vector3f basePos, Spatial base, Spatial slider, Spatial hook, Buffer buffer)
    {
        super(id, basePos,base,slider, hook);
        this.buffer = buffer;
        System.out.println(buffer.pSpots.length);
        hNode.setLocalTranslation(new Vector3f(0,18.5f,0));
        this.hook.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        
        this.defPosBase = this.getWorldTranslation().clone();
        this.defPosHook = hNode.getWorldTranslation().clone();
        this.defPosSlider = sNode.getWorldTranslation().clone();
    }

     @Override
    protected void updateGet()
    {
         switch(action)
        {
            case 1: 
                if(!baseControl.isEnabled())
                {
                   moveBase(false);
                }
                break;
            case 2: 
                if(!sliderControl.isEnabled())
                {
                   moveSlider(false);
                }
                break;
            case 3:
                if(!hookControl.isEnabled())
                {
                   moveHook(false);
                }
                break;
            case 4:
               if (!hookControl.isEnabled()) {
                   contToHook();
                    moveHook(true);
                }
                break;
            case 5:
                 if(!sliderControl.isEnabled())
                {
                    moveSlider(true);
                }
                break;
            case 6:
                if(!baseControl.isEnabled())
                {
                   moveBase(true);
                }
                break;
            case 7:
                  if(readyToLoad())
                  {
                      if (!hookControl.isEnabled())
                    {
                        moveHook(false);
                    }
                  }
                break;
            case 8:
                 contOffHook();
                break;
            case 9:
             this.resetAll();
                break;
         }
    }
     
    @Override
    protected void updatePickup()
    {
        switch(action)
        {
            case 1: 
                if(!baseControl.isEnabled())
                {
                   moveBase(false);
                }
                break;
            case 2: 
                if(!sliderControl.isEnabled())
                {
                   moveSlider(false);
                }
                break;
            case 3:
                if(!hookControl.isEnabled())
                {
                   moveHook(false);
                }
                break;
            case 4:
               if (!hookControl.isEnabled()) {
                   contToHook();
                    moveHook(true);
                }
                break;
            case 5:
                 if(!sliderControl.isEnabled())
                {
                    moveSlider(true);
                }
                break;
            case 6:
                if(!baseControl.isEnabled())
                {
                   moveBase(true);
                }
                break;
            case 7:
                  if(readyToLoad())
                  {
                      if (!hookControl.isEnabled())
                    {
                        moveHook(false);
                    }
                  }
                break;
            case 8:
                 contOffHook();
                break;
            case 9:
             this.resetAll();
                break;
        }
    }

    @Override
    protected void moveBase(boolean reversed)
    {
        basePath.clearWayPoints();
        if(reversed)
        {
            
        }
        else
        {
         basePath.addWayPoint(this.getLocalTranslation().add(0.1f,0,0));
         basePath.addWayPoint(new Vector3f(this.getLocalTranslation().x,this.getLocalTranslation().y,target.z));    
        }
        
         baseControl.play();
    }
    
    @Override
    protected void moveSlider(boolean reversed)
    {
         sliderPath.clearWayPoints();
         if(reversed)
         {
             
         }
         else
         {
         sliderPath.addWayPoint(sNode.getLocalTranslation());
         sliderPath.addWayPoint(new Vector3f(
                 target.x-this.getWorldTranslation().x
                 ,sNode.getLocalTranslation().y,
                 sNode.getLocalTranslation().z));    
         }
         
         sliderControl.play();
    }
    
    @Override
     protected void moveHook(boolean reversed)
    {
          hookPath.clearWayPoints();
        
          if(reversed)
          {
              hookPath.addWayPoint(new Vector3f(target.x-sNode.getWorldTranslation().x,target.y-sNode.getWorldTranslation().y,sNode.getLocalTranslation().z));
              hookPath.addWayPoint(hNode.getLocalTranslation());
          }
          else
          {
              hookPath.addWayPoint(hNode.getLocalTranslation());
              hookPath.addWayPoint(new Vector3f(target.x-sNode.getWorldTranslation().x,target.y-sNode.getWorldTranslation().y,sNode.getLocalTranslation().z));
          }
      
       
        hookControl.play();
    }

    @Override
    public ParkingSpot getParkingspot() {
        return buffer.getBestParkingSpot();
    }
}


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
        
        this.defPosBase = this.position.clone();
        this.defPosHook = hNode.getWorldTranslation().clone();
        this.defPosSlider = sNode.getWorldTranslation().clone();
    }

    private void commonSteps()
    {
        switch(action)
        {
         case 1: 
                doAction(1,false);
                break;
            case 2: 
                doAction(2,false);
                break;
            case 3:
                doAction(3,false);
                break;
            case 4:
               if(doAction(3,true))
               {
                    contToHook();
               }
                break;
         }
     }

     @Override
    protected void updateGet()
    {
         switch(action)
        {
             default:
                 commonSteps();
                 break;
            case 5:
                  if(readyToLoad())
                  {
                    if (doAction(1,false))
                    {  
                        resetPos(2);
                    }
                  }
                break;
            case 6:
                doAction(2,false);
                break;
            case 7:
                doAction(3,false);
                break;
            case 8:
                if(doAction(3,true))
                {
                   this.buffer.addContainer(cont.indexPosition, cont);
                   this.hNode.detachChild(cont);
                }
                break; 
            case 9:
                resetPos(2);
                resetPos(3);
             this.resetAll();
                break;
         }
    }
 
     
    @Override
    protected void updatePickup()
    {
        switch(action)
        {
            default:
                commonSteps();
                break;
            case 5:
                doAction(2,true);
                break;
            case 6:
                doAction(1,true);
                break;
            case 7: //nog niet af vanaf hier
                  if(readyToLoad())
                  {
                    doAction(3,false);
                  }
                break;
            case 8:
                 contOffHook();
                break;
            case 9:
                resetPos(1);
                resetPos(2);
                resetPos(3);
             this.resetAll();
                break;
        }
    }

    @Override
    protected void moveBase(boolean reversed)
    {
        basePath.clearWayPoints();
        
       Vector3f startPos = this.getLocalTranslation().add(0,0,0.1f);
        if(reversed)
        {
        basePath.addWayPoint(startPos);    
        basePath.addWayPoint(defPosBase);
        }
        else
        {
         basePath.addWayPoint(startPos);
         basePath.addWayPoint(new Vector3f(startPos.x,startPos.y,target.z));    
        }
        
         baseControl.play();
    }
    
    @Override
    protected void moveSlider(boolean reversed)
    {
         sliderPath.clearWayPoints();
        Vector3f startPoint = sNode.getLocalTranslation();
        
        if(reversed)
        {
            sliderPath.addWayPoint(startPoint);
            sliderPath.addWayPoint(defPosSlider);
        }
         else
         {
         sliderPath.addWayPoint(startPoint);
         sliderPath.addWayPoint(new Vector3f(
                 target.x-this.getWorldTranslation().add(0.1f,0,0).x
                 ,startPoint.y,
                 startPoint.z));    
         }
         
         sliderControl.play();
    }
    
    @Override
     protected void moveHook(boolean reversed)
    {
          hookPath.clearWayPoints();
        
          Vector3f startPoint = this.hNode.getLocalTranslation();
        
        if(reversed)
        {
         hookPath.addWayPoint(startPoint);
         hookPath.addWayPoint(defPosHook);
        }
          else
          {
              hookPath.addWayPoint(startPoint);
              hookPath.addWayPoint(new Vector3f(target.x-sNode.getWorldTranslation().x,target.y-sNode.getWorldTranslation().y,sNode.getLocalTranslation().z));
          }
      
       
        hookControl.play();
    }

    @Override
    public ParkingSpot getParkingspot() {
        return buffer.getBestParkingSpot();
    }
}


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
public class SeaCrane extends Crane {

    //motionpaths for moving objects
   
    public SeaCrane(String id, Vector3f basePos, Spatial base, Spatial slider, Spatial hook) {
        
        super(id, basePos, base,slider, hook);
        hNode.setLocalTranslation(new Vector3f(0,25,0));
        
        this.defPosBase =  this.position.add(0.1f,0,0);
        this.defPosHook = hNode.getWorldTranslation().clone();
        this.defPosSlider = sNode.getWorldTranslation().clone();
        
        
        this.baseDur = 1.1f;
        this.baseDurLoaded = this.baseDur;
        this.sliDur = 1.8f;
        this.sliDurLoaded = 1.8f;
        this.hookDur = 1.8f;
        this.hookDurLoaded = 1.8f;
    }

   @Override
    protected void updateGet()
    {
         switch (action) {
             default:
                 commonActions();
                break;
            case 5:
                if(readyToLoad())
                {
                    if(doAction(2,false))
                    {
                       resetPos(3);
                    }
                }
                break;
            case 6:
                doAction(3,false);
                break;
           case 7:
                if(doAction(3,true))
                {
                   contOffHook();
                }
                break; 
            case 8:
                if(doAction(2,true)){
                    resetPos(3);
                }
                break;
            case 9:
             this.resetAll();
                break;
         }
    }
    private void commonActions()
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
                    this.contToHook();
                }
                break;
        }
    }

    @Override
    protected void updatePickup() 
    {
        switch (action) {
            default:
               commonActions();
                break;
            case 5:
                if (!doAction(2,true))
                {
                    resetPos(3);
                }
                break;
            case 6:
                if(readyToLoad())
                {
                    if (doAction(3,false))
                    {
                    resetPos(2);
                    }
                }
                break;
            case 7:
                if(doAction(3,true))
                {
                    contOffHook();
                   
                }
                break;
            case 8:
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
        
        Vector3f startPos = this.getLocalTranslation().add(0.1f,0,0.1f);
        if(reversed)
        {
        basePath.addWayPoint(startPos);    
        basePath.addWayPoint(defPosBase);
        }
        else
        {
        basePath.addWayPoint(startPos);
        basePath.addWayPoint(new Vector3f(startPos.x,startPos.y, target.z));     
        }
        baseControl.play();
    }

    @Override
    protected void moveSlider(boolean reversed) {
        
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
          sliderPath.addWayPoint(new Vector3f(target.x - sNode.getWorldTranslation().x, startPoint.y,startPoint.z));
        }
        sliderControl.play();
    }

   @Override
    protected void moveHook(boolean reversed) {
        
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
        hookPath.addWayPoint(new Vector3f(startPoint.x, target.y - sNode.getWorldTranslation().y, startPoint.z));
        }
        
        hookControl.play();
    }

    public ParkingSpot getParkingspot() {
        return new ParkingSpot(this.getWorldTranslation(), (float) Math.PI / 2f);
    }
}

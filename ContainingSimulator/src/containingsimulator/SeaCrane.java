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
        
        this.defPosBase = this.getWorldTranslation().clone();
        this.defPosHook = hNode.getWorldTranslation().clone();
        this.defPosSlider = sNode.getWorldTranslation().clone();
      
        
    }

   @Override
    protected void updateGet()
    {
        switch(action)
        {
            
        }
    }

    @Override
    protected void updatePickup() 
    {
        switch (action) {
            case 1:
                if (!baseControl.isEnabled()) {
                    moveBase(false);
                }
                break;
            case 2:
                if (!sliderControl.isEnabled()) {
                    moveSlider(false);
                }
                break;
            case 3:
                if (!hookControl.isEnabled()) {
                    moveHook(false);
                }
                break;
            case 4:
                if (!hookControl.isEnabled()) {
                    
                    this.contToHook();
                    moveHook(true);
                }
                break;
            case 5:
                if (!sliderControl.isEnabled()) {
                     this.hNode.setLocalTranslation(hNode.getLocalTranslation().x,this.defPosHook.y,hNode.getLocalTranslation().z);
                    moveSlider(true);
                }
                break;
            case 6:
                if(readyToLoad())
                {
                    if (!hookControl.isEnabled())
                    {                     this.sNode.setLocalTranslation(sNode.getLocalTranslation().x,this.defPosSlider.y,sNode.getLocalTranslation().z);
                        moveHook(false);
                    }
                }
                break;
            case 7:
                contOffHook();
                if (!hookControl.isEnabled())
                    {
                        moveHook(true);
                    }
                break;
            case 8:
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

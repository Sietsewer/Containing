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

    public ParkingSpot parkingSpot;
    
    public LorryCrane(String id, Vector3f pos, Spatial base, Spatial slider, Spatial hook) 
    {
        super(id,pos,base,slider.clone().scale(0.4f),hook);
       sNode.setLocalTranslation(new Vector3f(0, -10, 0));
        this.slider.setLocalTranslation(new Vector3f(0, 6.5f, 0));
        this.hNode.setLocalTranslation(new Vector3f(0, 16.5f, 0));
      
        this.parkingSpot = new ParkingSpot(new Vector3f(pos.x,pos.y,pos.z-25f),0f);
        
         this.defPosBase = this.base.getWorldTranslation().clone();
        this.defPosHook = this.hNode.getLocalTranslation().clone();
        this.defPosSlider = this.sNode.getWorldTranslation().clone();
    }

    public void update(float tpf)
    {
        if(target==null)
        {
            return;
        }
        updateSpeed();
        
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
                    cont.setLocalTranslation(cont.getLocalTranslation().subtract(0,1.5f,0));
                    moveHook(true);
                }
                break;
            case 5:
                if(!baseControl.isEnabled())
                { 
                    this.hNode.setLocalTranslation(hNode.getLocalTranslation().x,this.defPosHook.y,hNode.getLocalTranslation().z);
                    target = parkingSpot.translation;
                  moveBase(false);
                }
                break;
            case 6:
                 if(readyToLoad())
                 {
                     if (!hookControl.isEnabled())
                    {this.sNode.setLocalTranslation(sNode.getLocalTranslation().x,this.defPosSlider.y,sNode.getLocalTranslation().z);
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
                if(!baseControl.isEnabled())
                {
                  target = this.position;
                  moveBase(false);
                }
                break;
            case 9:
                this.resetAll();
                break;
        }
    }



    @Override
    protected void moveHook(boolean reversed)
    {
        hookPath.clearWayPoints();
       Vector3f startPoint = hNode.getLocalTranslation().add(0.1f,0,0);
        
        if(reversed)
        {
         hookPath.addWayPoint(startPoint);
         hookPath.addWayPoint(defPosHook);
        }
        else
        {
        
        hookPath.addWayPoint(startPoint);
        hookPath.addWayPoint(new Vector3f(startPoint.x,target.y-sNode.getWorldTranslation().y,startPoint.z));
        // hookPath.addWayPoint(new Vector3f(target.x - sNode.getWorldTranslation().x, target.y - sNode.getWorldTranslation().y, sNode.getLocalTranslation().z));
        }
        hookControl.play();
    }
    protected void moveBase(boolean reversed)
    {
         basePath.clearWayPoints();
        Vector3f startPos = this.getLocalTranslation().add(0.1f,0,0);
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
        Vector3f startPoint = sNode.getLocalTranslation().add(0.1f,0,0);
       
        if(reversed)
        {
            sliderPath.addWayPoint(startPoint);
            sliderPath.addWayPoint(defPosSlider);
        }
        else
        {
             sliderPath.addWayPoint(startPoint);
         sliderPath.addWayPoint(new Vector3f(startPoint.x ,startPoint.y, target.z-this.getWorldTranslation().add(0,0,0.1f).z));  
        }
        sliderControl.play();
        
    }

    public void debugRender(Spatial agv, Spatial transporter){
        this.attachChild(agv);
        this.attachChild(transporter);
        transporter.setLocalTranslation(0f, 0f, 20f);
    }

    @Override
    public ParkingSpot getParkingspot() {
        return this.parkingSpot;
    }
}

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
        
        this.defPosBase = this.position.add(0,0,0.1f);
        this.defPosHook = this.hNode.getLocalTranslation().clone();
        this.defPosSlider = this.sNode.getWorldTranslation().clone();
        
        this.baseDur = 5.5f;
       // this.baseDurLoaded = 4.16f;
        this.sliDur = 5f;
       // this.sliDurLoaded = 5f;
        this.hookDur = 5f;
       // this.hookDurLoaded = 5f;
        
    }

    @Override
    protected void updateGet()
    {
        switch(action)
        {
            case 1:
                target = parkingSpot.translation;
               doAction(1,false);;
                break;
            case 2:
                if(readyToLoad())
                 {
                     if (doAction(3,false))
                    {
                       resetPos(2);
                    }
                 }
                break;
            case 3:
                doAction(1,false);
                break;
            case 4:
                doAction(3,false);
                break;
            case 5:
                if(doAction(3,true))
                {
                    contOffHook();
                    //cont.setLocalTranslation(cont.getLocalTranslation().subtract(0,1.5f,0));
                }
                break;
            case 6:
                doAction(1,true);
                break;
            case 7:
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
            case 1: 
                doAction(1,false);
                break;
            case 2:
                doAction(3,false);
                break;
            case 3:
                if(doAction(3,true))
                {
                    contToHook();
                    cont.setLocalTranslation(cont.getLocalTranslation().subtract(0,1.5f,0));
                }
                break;
            case 4:
                target = parkingSpot.translation;
                if(doAction(1,false))
                { 
                   resetPos(3);
                }
                break;
            case 5:
                 if(readyToLoad())
                 {
                     target = agv.getWorldTranslation().add(0,cont.size.y*2,0);     
                     doAction(3,false);
                 }
                break;
            case 6:
                 if (!doAction(3,true))
                    {  
                        contOffHook();
                    }
                break;
            case 7:
                target = this.position;
                doAction(1,false);
                break;
            case 8:
                resetPos(2);
                resetPos(3);
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

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
public class TrainCrane extends Crane implements MotionPathListener{
    
    public TrainCrane(String id, Vector3f basePos, Spatial base, Spatial slider, Spatial hook)
    {
        super(id, basePos,base,slider, hook);
        this.base.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        this.hook.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        hNode.setLocalTranslation(new Vector3f(0,12,0));
        
       
        this.defPosHook = this.hNode.getWorldTranslation().clone();
        this.defPosSlider = this.sNode.getWorldTranslation().clone();
        
         this.baseDur = 12f;
       // this.baseDurLoaded = 2f;
        this.sliDur = 6f;
       // this.sliDurLoaded = 5f;
        this.hookDur = 6f;
       // this.hookDurLoaded = 5f;
    }

    
    @Override
    protected void updateGet()
    {
        
        
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
                doAction(2,false);
                break;
            case 3:
                doAction(3,false);
                break;
            case 4:
                 if(doAction(3,true)){
                    contToHook();
                 }
                break;
            case 5:
                 if(doAction(2,true))
                 {
                    resetPos(3);
                 }
                break;
            case 6:
                  if(readyToLoad())
                  {
                      target = agv.getWorldTranslation().add(0,cont.size.y*2,0); 
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
                finishActions();
                break;
        }
    }
    
    @Override
    public ParkingSpot getParkingspot() {
        return new ParkingSpot(this.base.getWorldTranslation(),(float)Math.PI/2f);
    }
}

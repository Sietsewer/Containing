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
    }
    
    @Override
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
                  this.attachProcess();
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
                  waitProcess();
                 break;
            case 8:
                 dropProcess();
                break;
            case 9:
             this.resetAll();
                break;
        }
    }

    private void moveBase()
    {
         basePath.clearWayPoints();
         basePath.addWayPoint(this.getLocalTranslation());
         basePath.addWayPoint(new Vector3f(target.x,this.getLocalTranslation().y,this.getLocalTranslation().z));
         baseControl.play();
    }
    private void moveSlider()
    {
         sliderPath.clearWayPoints();
         sliderPath.addWayPoint(sNode.getLocalTranslation());
         sliderPath.addWayPoint(new Vector3f(
                 sNode.getLocalTranslation().x
                 ,sNode.getLocalTranslation().y,
                 target.z-this.getWorldTranslation().z));
         sliderControl.play();
    }
    
    @Override
    protected void moveHook()
    {
        hookPath.clearWayPoints();
        hookPath.addWayPoint(hNode.getLocalTranslation());
        hookPath.addWayPoint(new Vector3f(hook.getLocalTranslation().x,target.y-sNode.getWorldTranslation().y, hook.getLocalTranslation().z));
        hookControl.play();
    }

    @Override
    public ParkingSpot getParkingspot() {
        return new ParkingSpot(this.getWorldTranslation(),(float)Math.PI/2f);
    }
}

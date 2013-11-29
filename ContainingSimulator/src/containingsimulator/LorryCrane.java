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
                   moveBase();
                }
                break;
            case 2:
                if(!hookControl.isEnabled())
                {
                   moveHook();
                }
                break;
            case 3:
                 this.attachProcess();
                break;
            case 4:
                if(!baseControl.isEnabled())
                {
                   moveBase2();
                }
                break;
            case 5:
                waitProcess();
                break;
            case 6:
                dropProcess();
                break;
            case 7:
                this.resetAll();
                break;
        }
    }

    private void moveBase()
    {
         basePath.clearWayPoints();
         basePath.addWayPoint(this.getLocalTranslation());
         basePath.addWayPoint(new Vector3f(this.getLocalTranslation().x,this.getLocalTranslation().y,target.z));
         baseControl.play();
    }

    @Override
    protected void moveHook()
    {
        hookPath.clearWayPoints();
        hookPath.addWayPoint(hNode.getLocalTranslation());
        hookPath.addWayPoint(new Vector3f(hook.getLocalTranslation().x,target.y-sNode.getWorldTranslation().y,hook.getLocalTranslation().z));
        // hookPath.addWayPoint(new Vector3f(target.x - sNode.getWorldTranslation().x, target.y - sNode.getWorldTranslation().y, sNode.getLocalTranslation().z));
        hookControl.play();
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

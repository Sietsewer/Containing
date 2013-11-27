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
    }

    

    private void dettachFromHook() {
        this.detachChild(cont);
        transporter.setContainer(cont);
    }

    private void dettachFromHook(int test) {
        this.detachChild(cont);
    }

    //updates crane motion
    public void update(float tpf) {
       
        if (target == null) 
        {
            return;
        }
        updateSpeed();

        switch (action) {
            case 1:
                if (!baseControl.isEnabled()) {
                    moveBase();
                }
                break;
            case 2:
                if (!sliderControl.isEnabled()) {
                    moveSlider();
                }
                break;
            case 3:
                if (!hookControl.isEnabled()) {
                    moveHook();
                }
                break;
            case 4:
                 attachProcess();
                break;
            case 5:
                if (!sliderControl.isEnabled()) {
                    moveSlider2();
                }
                break;
            case 6:
                waitProcess();
                break;
            case 7:
               dropProcess();
                break;
            case 8:
                if (!baseControl.isEnabled()) 
                {
                    moveBase2();
                }
                break;
            case 9:
                this.resetAll();
                break;
        }
    }

    
    private void moveBase() {
        basePath.clearWayPoints();
        basePath.addWayPoint(this.getLocalTranslation());
        basePath.addWayPoint(new Vector3f(this.getLocalTranslation().x, this.getLocalTranslation().y, target.z));
        baseControl.play();
    }

    private void moveSlider() {
        sliderPath.clearWayPoints();
        sliderPath.addWayPoint(sNode.getLocalTranslation());
        sliderPath.addWayPoint(new Vector3f(
                target.x - sNode.getWorldTranslation().x, sNode.getLocalTranslation().y,
                sNode.getLocalTranslation().z));
        sliderControl.play();
    }

    @Override
    protected void moveHook() {
        hookPath.clearWayPoints();
        hookPath.addWayPoint(hNode.getLocalTranslation());
        hookPath.addWayPoint(new Vector3f(hook.getLocalTranslation().x, target.y - sNode.getWorldTranslation().y, hook.getLocalTranslation().z));
        hookControl.play();
    }

    public ParkingSpot getParkingspot() {
        return new ParkingSpot(this.getWorldTranslation(), (float) Math.PI / 2f);
    }
}

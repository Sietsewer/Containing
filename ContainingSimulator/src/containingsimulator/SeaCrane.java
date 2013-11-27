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
public class SeaCrane extends Crane implements MotionPathListener {

    private Container container;
    private static final float sliDur = 2f;
    private Node sNode = new Node();
    private Spatial slider;
    private AGV agv;
    //motionpaths for moving objects
    private MotionPath sliderPath = new MotionPath();
    private MotionEvent sliderControl;

    public SeaCrane(String id, Vector3f basePos, Spatial base, Spatial slider, Spatial hook) {
        super(id, basePos, base, hook);
        this.slider = slider.clone();

        sNode.attachChild(this.slider);
        sNode.attachChild(this.hook);
        this.attachChild(this.sNode);

        this.hook.setLocalTranslation(new Vector3f(0, 25, 0));
        sliderControl = new MotionEvent(this.sNode, sliderPath, sliDur / Main.globalSpeed, LoopMode.DontLoop);
        sliderPath.setCycle(false);

        sliderPath.addListener(this);
    }

    public boolean isMoving() {
        return this.action != 0;
    }

    @Override
    public void loadContainer(Transporter transporter) {
        //sNode.attachChild(transporter.getContainer(cont.indexPosition));
        this.target = this.position;
        loadContainer = true;
        this.sendReady();
    }

    private void attachToHook() {
        this.attachChild(transporter.getContainer(cont.indexPosition));
        this.target = this.position;
        loadContainer = true;
    }

    private void attachToHook(int test) {

        sNode.attachChild(cont);
        this.target = this.position;
        loadContainer = true;
    }

    private void dettachFromHook() {
        this.detachChild(container);
        transporter.setContainer(container);
    }

    private void dettachFromHook(int test) {
        this.detachChild(container);

    }

    //updates crane motion
    public void update(float tpf) {
        if (target == null) {
            return;
        }

        updateSpeed();

        switch (action) {
            case 0: //nothing
                return;
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
                if (!hookControl.isEnabled()) {//this.attachToHook(0);
                    moveHook2();
                }
                break;
            case 5:
                if (!sliderControl.isEnabled()) {
                    moveSlider2();
                    // loadContainer(null);
                }
                break;
            case 6:
                if (readyForL && !loadContainer) {
                    break;
                } else if (!readyForL && !loadContainer) {
                    loadContainer(null);
                } else if (readyForL && loadContainer) {

                    if (!hookControl.isEnabled()) {
                        System.out.println("wtf");
                        moveHook();
                    }
                }
                break;
            case 7:
                if (!hookControl.isEnabled()) {
                    
                    //drop container
                    this.sendReady();
                    moveHook2();
                }
                break;
            case 8:
                if (!baseControl.isEnabled()) {
                    moveBase2();
                }
                break;
            case 9:
                this.resetAll();
                break;
        }


    }

    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {

        action += wayPointIndex;



    }

    private void updateSpeed() {
        baseControl.setInitialDuration(baseDur / Main.globalSpeed);
        sliderControl.setInitialDuration(sliDur / Main.globalSpeed);
        hookControl.setInitialDuration(hookDur / Main.globalSpeed);
    }

    private void moveBase() {
        basePath.clearWayPoints();
        basePath.addWayPoint(this.getLocalTranslation());
        basePath.addWayPoint(new Vector3f(this.getLocalTranslation().x, this.getLocalTranslation().y, target.z));
        baseControl.play();
    }

    private void moveBase2() {
        basePath.addWayPoint(basePath.getWayPoint(0));
        basePath.removeWayPoint(0);
        baseControl.play();
    }

    private void moveSlider() {
        sliderPath.clearWayPoints();
        sliderPath.addWayPoint(sNode.getLocalTranslation());
        sliderPath.addWayPoint(new Vector3f(
                target.x - this.getWorldTranslation().x, sNode.getLocalTranslation().y,
                sNode.getLocalTranslation().z));
        sliderControl.play();
    }

    private void moveSlider2() {
        sliderPath.addWayPoint(sliderPath.getWayPoint(0));
        sliderPath.removeWayPoint(0);
        sliderControl.play();
    }

    private void moveHook() {
        hookPath.clearWayPoints();
        hookPath.addWayPoint(hook.getLocalTranslation());
        hookPath.addWayPoint(new Vector3f(target.x - sNode.getWorldTranslation().x, target.y - sNode.getWorldTranslation().y, sNode.getLocalTranslation().z));
        hookControl.play();
    }

    private void moveHook2() {
        hookPath.addWayPoint(hookPath.getWayPoint(0));
        hookPath.removeWayPoint(0);
        hookControl.play();
    }

    public ParkingSpot getParkingspot() {
        return new ParkingSpot(this.getWorldTranslation(), (float) Math.PI / 2f);
    }
}

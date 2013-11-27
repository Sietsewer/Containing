/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.animation.LoopMode;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Ruben
 */
public abstract class Crane extends Node implements MotionPathListener {

    /**
     * Crane ID
     */
    public String id;
    /**
     * Crane Position
     */
    public Vector3f position;
    /**
     *
     * @param cont
     */
    protected int action = 0;
    protected MotionPath basePath = new MotionPath();
    protected MotionPath hookPath = new MotionPath();
    protected MotionPath sliderPath = new MotionPath();
    protected Spatial base;
    protected Spatial hook;
    protected Spatial slider;
    protected Node sNode = new Node();
    protected Node hNode = new Node();
    protected MotionEvent baseControl;
    protected MotionEvent hookControl;
    protected MotionEvent sliderControl;
    
    protected boolean busy = false;
    protected boolean readyForL = false;
    protected boolean loadContainer = false;
    protected Container cont;
    protected static final float baseDur = 2f;
    protected static final float hookDur = 2f;
    protected static final float sliDur = 2f;
    protected Vector3f target;
    protected Transporter transporter;
    protected Node transportNode;

    public Crane()
    {
    }
    
    public Crane(String id, Vector3f pos, Spatial base, Spatial slider, Spatial hook) {
       
        this.id = id;
        this.position = pos;
        this.base = base.clone();
        this.slider = slider.clone();
        this.hook = hook.clone();
        this.attachChild(this.base);
        
        hNode.attachChild(this.hook.clone());
        sNode.attachChild(hNode);
        sNode.attachChild(this.slider);

        this.attachChild(this.sNode);
        hNode.setLocalTranslation(new Vector3f(0,25,0));
        
        baseControl = new MotionEvent(this, basePath, baseDur / Main.globalSpeed, LoopMode.DontLoop);
        hookControl = new MotionEvent(this.hNode, hookPath, hookDur / Main.globalSpeed, LoopMode.DontLoop);

        basePath.setCycle(false);
        hookPath.setCycle(false);

        basePath.addListener(this);
        hookPath.addListener(this);
        
        sliderControl = new MotionEvent(this.sNode, sliderPath, sliDur / Main.globalSpeed, LoopMode.DontLoop);
        sliderPath.setCycle(false);

        sliderPath.addListener(this);
    }

    public String getId() {
        return this.id;
    }

    public boolean isbusy() {
        return this.busy;
    }

    public abstract void loadContainer(Transporter transporter);

    /**
     *
     * @param cont
     */
    protected void containerIsReady()
    {
         readyForL = true;
        // Main.sendReady(this.id);
         System.out.println("container ready for drop");
    }
    protected void transferFinished()
    {
       // Main.sendReady(this.id);
        System.out.println("transfer finished");
    }
    protected void resetAll()
    {
        System.out.println("crane is back to idle");
         this.action = 0;
         this.busy = false;
         this.readyForL = false;
         this.loadContainer = false;
         this.target = null;
    }

    public void getContainer(Container cont, Node trans) {
        this.transporter = null;
        this.transportNode = trans;
        this.cont = cont;
        this.target = cont.getWorldTranslation();
        action = 1;
        busy = true;
    }

    public void getContainer(Vector3f pos) {
        if (pos != null) {
            this.target = pos;
            action = 1;
            busy = true;
        }
    }
     public void loadContainer(Node node) {
        //sNode.attachChild(transporter.getContainer(cont.indexPosition));
        this.target = this.position;
        loadContainer = true;
       // this.containerIsReady();
    }
     
     private void detachContainer()
     {
         Vector3f pos = cont.getWorldTranslation();
         transportNode.attachChild(cont);
         //transporter.setContainer(cont);
         cont.setLocalTranslation(pos);
     }
     
     protected void attachProcess()
     {
           if (!readyForL && !loadContainer) 
            {
                this.containerIsReady();
            } 
            else if (readyForL && loadContainer)
            {
                if (!hookControl.isEnabled())
                {
                        moveHook();
                }
            }
     }
     protected void dropProcess()
     {
          if (!hookControl.isEnabled()) {
                    
                    //drop container
                    detachContainer();
                    this.transferFinished();
                    moveHook2();
          }
     }
     
     
     protected abstract void moveHook();
     protected abstract void moveHook2();

    public abstract void update(float tpf);

    public abstract ParkingSpot getParkingspot();
}

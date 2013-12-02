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
    
    protected MotionEvent baseControl;
    protected MotionEvent hookControl;
    protected MotionEvent sliderControl;
    
    protected Spatial base;
    protected Spatial hook;
    protected Spatial slider;
    
    protected Node sNode = new Node();
    protected Node hNode = new Node();
    
    protected boolean busy = false;
    protected boolean readyForL = false;
    protected boolean loadContainer = false;
    
    protected static final float baseDur = 2f;
    protected static final float hookDur = 2f;
    protected static final float sliDur = 2f;
    
    protected Vector3f target = null;
    protected Container cont = null;
    protected Transporter transporter = null;
    protected Node transportNode= null;
    protected AGV agv = null;

    public Crane(String id, Vector3f pos, Spatial base, Spatial slider, Spatial hook) {
        super(id);
        this.id = id;
        this.position = pos;
        this.base = base.clone();
        this.slider = slider.clone();
        this.hook = hook.clone();
        this.attachChild(this.base);
        
        hNode.attachChild(this.hook);
        sNode.attachChild(hNode);
        sNode.attachChild(this.slider);

        this.attachChild(this.sNode);
        
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
    public abstract void update(float tpf);
    public abstract ParkingSpot getParkingspot();

    /**
     *
     * @param cont
     */
    protected void containerIsReady()
    {
         readyForL = true;
         Main.sendReady(this.id);
         System.out.println("container ready for drop");
    }
    
    protected void transferFinished()
    {
        Main.sendReady(this.id);
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
         this.cont = null;
         this.transporter = null;
         this.transportNode= null;
         this.transferFinished();
    }

    public void getContainer(Container cont, Node trans) {
        
        this.transporter = null;
        this.transportNode = trans;
        this.cont = cont;
        this.target = cont.getWorldTranslation();
        action = 1;
        busy = true;
    }
    public void getContainer(Container cont, Transporter trans)
    {
        if(this.cont==null&&this.transporter==null&&this.target==null)
        {
        this.transporter = trans;
      //  this.transportNode = trans;
        this.cont = cont;
        this.target = cont.getWorldTranslation();
        action = 1;
        busy = true;
        }
       
    }
/*
    public void getContainer(Vector3f pos) 
    {
        if (pos != null) 
        {
            this.target = pos;
            action = 1;
            busy = true;
        }
    }*/
     public void loadContainer(Node node) 
     {
        this.target = this.position;
        loadContainer = true;
     }
     
     public void loadContainer(Transporter trans) 
     {
        //agv.getContainer(cont.indexPosition);
        this.target = trans.getWorldTranslation();
        loadContainer = true;
     }
     public void loadContainer(AGV agv)
     {
         this.agv = agv;
         this.target = agv.getWorldTranslation();
         loadContainer = true;
     }
     
     private void detachContainer()
     {
     
       //  transportNode.attachChild(cont);
         //transporter.setContainer(cont);
         if(agv!=null)
         {
          this.agv.setContainer(cont);
          agv = null;
         }
         else if(transporter!=null)
         {
             transporter.setContainer(cont);
             transporter = null;
         }
     }
     
    private void attachToHook() 
    {
        float y = base.getWorldRotation().toAngles(null)[1];
        hNode.attachChild(cont);
        cont.setLocalTranslation(hook.getLocalTranslation().add(new Vector3f(0,cont.size.y,0)));
        if(transporter != null){
            //System.out.println(cont.indexPosition.toString());
            transporter.getContainer(cont.indexPosition);
        }
        cont.rotate(0, y, 0);
    }
     
     protected void waitProcess()
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
                 
                    moveHook2();
          }
     }
     
     protected void attachProcess()
     {
         if (!hookControl.isEnabled()) {
                    this.attachToHook();
                    moveHook2();
                }
     }
     
     
     protected abstract void moveHook();
     
     protected  void moveHook2()
     {
        hookPath.addWayPoint(hookPath.getWayPoint(0));
        hookPath.removeWayPoint(0);
        hookControl.play();
     }
     
     protected void moveSlider2()
     {
        sliderPath.addWayPoint(sliderPath.getWayPoint(0));
        sliderPath.removeWayPoint(0);
        sliderControl.play();
     }
     
     protected void moveBase2()
     {
         basePath.addWayPoint(basePath.getWayPoint(0));
         basePath.removeWayPoint(0);
         baseControl.play();
     }
     
     protected void updateSpeed() 
     {
        baseControl.setInitialDuration(baseDur / Main.globalSpeed);
        sliderControl.setInitialDuration(sliDur / Main.globalSpeed);
        hookControl.setInitialDuration(hookDur / Main.globalSpeed);
     }
     
     @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {

        action += wayPointIndex;
    }

}

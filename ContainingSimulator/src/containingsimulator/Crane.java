/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.animation.LoopMode;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Quaternion;
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
    
    protected boolean pickupContainer;
    
    protected Vector3f defPosHook;
    protected Vector3f defPosSlider;
    protected Vector3f defPosBase;



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


    /**
     *
     * @param cont
     */


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
         this.sNode.setLocalTranslation(this.defPosSlider);
         this.hNode.setLocalTranslation(this.defPosHook);
         sendMessage("transfer finished");
    }
    
    protected void sendMessage(String message)
    {
        System.out.println(message);
          Main.sendReady(this.id);
    }

    //only for point click testing
    public void pickupContainer(Container cont, Node trans) {
        
        this.transporter = null;
        this.transportNode = trans;
        this.cont = cont;
        this.target = cont.getWorldTranslation();
        action = 1;
        busy = true;
        pickupContainer = true;
    }
    
    //from transport/buffer to agv
    public void pickupContainer(Container cont, Transporter trans)
    {
        this.transporter = trans;
      //  this.transportNode = trans;
        this.cont = cont;
        this.target = cont.getWorldTranslation();
        action = 1;
        busy = true;
        pickupContainer = true;
    }
    
    //From agv to transport/buffer
    public void getContainer(AGV agv)
    {
        pickupContainer = false;
        this.cont = agv.getContainerObject();
        this.target = this.cont.getWorldTranslation();
        action = 1;
        busy = true;
        pickupContainer = false;
    }

    
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
     public void putContainer(Vector3f nextPosition,Vector3f indexPosition)
     {
         this.target = nextPosition;
         this.cont.setIndexPosition(indexPosition);
         loadContainer = true;
         
     }
     
     protected void contOffHook()
     {
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
    
     
    protected void contToHook() 
    {
        float y = base.getWorldRotation().toAngles(null)[1];
        hNode.attachChild(cont);
        cont.setLocalTranslation(hook.getLocalTranslation().add(new Vector3f(0,cont.size.y,0)));
        
        if(transporter != null){
            transporter.getContainer(cont.indexPosition);
            transporter = null;
        }
        else if(agv!= null)
        {
            agv.getContainer();
            agv = null;
        }
        
        cont.rotate(0, y, 0);
    }
     
     protected boolean readyToLoad()
     {
           if (!readyForL && !loadContainer) 
            {
                readyForL = true;
                sendMessage("container ready for drop");
            } 
            else if (readyForL && loadContainer)
            {
                return true;
            }
           return false;
     }
     

     protected abstract void moveHook(boolean reversed);
     protected abstract void moveBase(boolean reversed);
     protected abstract void moveSlider(boolean reversed);
     protected abstract void updateGet();
     protected abstract void updatePickup();
     public abstract ParkingSpot getParkingspot();
     
     public void update(float tpf)
     {
         if(target!=null)
         {
         updateSpeed();

         if(pickupContainer)
         {
          updatePickup();
         }
         else
         {
          updateGet();
         }
         
         }

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

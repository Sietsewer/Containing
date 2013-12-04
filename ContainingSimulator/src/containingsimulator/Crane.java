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
 * @author Len
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
    
    /**
     * path of this node
     */
    protected MotionPath basePath = new MotionPath();
    /**
     *path of hook-spatial
     */
    protected MotionPath hookPath = new MotionPath();
    /**
     *path of slider-spatial
     */
    protected MotionPath sliderPath = new MotionPath();
    
    /**
     *MotionEvent of basePath
     */
    protected MotionEvent baseControl;
    /**
     *MotionEvent of hookPath
     */
    protected MotionEvent hookControl;
    /**
     *
     *MotionEvent of sliderPath
     */
    protected MotionEvent sliderControl;
    
    /**
     *viewmodel of the base
     */
    protected Spatial base;
    /**
     *viewmodel of the hook
     */
    protected Spatial hook;
    /**
     *viewmodel of the slider
     */
    protected Spatial slider;
    
    /**
     *node of slider (has hNode as childnode)
     */
    protected Node sNode = new Node();
    /**
     *node of hook (has container as childnode when needed)
     */
    protected Node hNode = new Node();
    
    /**
     *status of this crane
     */
    protected boolean busy = false;
    /**
     *substatus of this crane
     */
    protected boolean readyForL = false;
    /**
     *substatus of this crane
     */
    protected boolean loadContainer = false;
    
    /**
     * Duration of moving the base from a to b
     */
    protected float baseDur = 2f;
    /**
     *Duration of moving the hook from a to b
     */
    protected float hookDur = 2f;
    /**
     *Duration of moving the sldier from a to b
     */
    protected float sliDur = 2f;
    
    protected float  sliSpeedLoaded = 1f;
    
    protected float hookSpeedLoaded = 1f;
    
    protected  float baseSpeedLoaded = 1f;
    /**
     *Next vector in simulator where the crane should go to
     */
    protected Vector3f target = null;
    /**
     *Container that the crane needs to load/unload into/from a transporter or buffer
     */
    protected Container cont = null;
    /**
     * Transporter object for communication, when necessary
     */
    protected Transporter transporter = null;

    /**
     *
     */
    protected AGV agv = null;
    
    /**
     *AGV object for communication, when necessary
     */
    protected boolean pickupContainer;
    
    /**
     *default position of the hook at start, usefull for resetting the hook its position
     */
    protected Vector3f defPosHook;
    /**
     *default position of the slider at start, usefull for resetting the slider its position
     */
    protected Vector3f defPosSlider;
    /**
     *default position of the base at start, usefull for resetting the base its position
     */
    protected Vector3f defPosBase;

    protected boolean loaded = false;

    /**
     *
     * @param id
     * @param pos
     * @param base
     * @param slider
     * @param hook
     */
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

    /**
     *returns the ID
     * @return 
     */
    public String getId() {
        return this.id;
    }

    /**
     * returns the state of this crane
     * @return
     */
    public boolean isbusy() {
        return this.busy;
    }

    /**
     *resets all variables and sends end-messsage to Controller
     */
    protected void resetAll()
    {
         this.action = 0;
         this.busy = false;
         this.readyForL = false;
         this.loadContainer = false;
         this.target = null;
         this.cont = null;
         this.transporter = null;
         sendMessage(this.id + " transfer finished");
    }
    
    /**
     *sends message to Controller
     * prints message in console for debugging-info
     * @param message
     */
    protected void sendMessage(String message)
    {
        System.out.println(message);
          Main.sendReady(this.id);
    }
    
    /**
     * calls classmethod for playing a motionpath, based on which globalAction is given and if is reversed.
     * @param globalAction
     * @param reversed
     * @return
     */
    protected boolean doAction(int globalAction, boolean reversed)
    {
        switch(globalAction)
        {
            case 1: //move base
                 if(!baseControl.isEnabled())
                {
                   moveBase(reversed);
                   return true;
                }
                break;
            case 2: //move slider
                if(!sliderControl.isEnabled())
                {
                   moveSlider(reversed);
                   return true;
                }
                break;
            case 3:
                 if(!hookControl.isEnabled())
                {
                   moveHook(reversed);
                   return true;
                }
                break;
        }
        return false;
    }

    /**
     * First method to be called from main for 
     * picking up a container from a transporter or buffer.
     * Initializes variables needed for starting this action
     * @param cont
     * @param trans
     */
    public void pickupContainer(Container cont, Transporter trans)
    {
        if(!busy )
        {
            if(cont!= null)
            {
                if(trans != null)
                {
        pickupContainer = true;
        this.transporter = trans;
        this.cont = cont;
        initializeStartUp();
                }
                else
                {
                    debugMessage(2,"pickupContainer");
                }
            }
            else
            {
                 debugMessage(3,"pickupContainer");
            }
        }
         else
        {
              debugMessage(4,"pickupContainer");
        }
    }
    
    
    private void initializeStartUp()
    {
        this.target = cont.getWorldTranslation();
        action = 1;
        busy = true;
    }

        
    /**First method to be called from main for picking up a container from an AGV
     *and transfering it to an transporter or buffer
     * @param agv
     */
    public void getContainer(AGV agv)
    {
        if(!busy)
        {
        pickupContainer = false;
        
        if(agv != null)
        {
        this.agv = agv;
        this.cont = this.agv.getContainerObject();
        
        if(cont!=null)
        {
        initializeStartUp();
        }
        else
        {
             debugMessage(3,"getContainer");
        }
        }
        else
        {
             debugMessage(1,"getContainer");
        }
        }
        else
        {
              debugMessage(4,"getContainer");
        }
    }
    
     /**
     *method to be called for loading a container from an agv
     * @param agv
     */
    public void loadContainer(AGV agv)
     {
        if(!loadContainer)
        {
            if(agv!= null)
            {
         this.agv = agv;
         this.target = agv.getWorldTranslation();
         loadContainer = true;
            }
            else
            {
               debugMessage(1,"getContainer");
            }
        }
        else
         {
              debugMessage(4,"loadContainer");
         }
    }
     
    
     private void debugMessage(int option, String message)
     {
         switch(option)
         {
             case 1: //agv is null
                  System.out.println(this.id + " - agv is null - " + message);
                 break;
             case 2: //transporter is null
                  System.out.println(this.id + " - transporter is null - " + message);
                 break;
             case 3: // container is null
                 System.out.println(this.id + " - container is null - " + message);
                 break;
             case 4: //already received
                 System.out.println(this.id + " - message already received - " + message);
                 break;
         }
     }
    
     /**
     * Method to be called for putting a container on a transporter/buffer
     * @param nextPosition
     * @param indexPosition
     */
    public void putContainer(Vector3f nextPosition,Vector3f indexPosition)
     {
         
         if(cont !=null)
         {
         this.target = nextPosition;
         System.out.println(nextPosition + " " + indexPosition);
         this.cont.setIndexPosition(indexPosition);
         loadContainer = true;
         }
         else
         {
             debugMessage(1,"putContainer");
         }
     }
     
     /**
     * Process of detaching the container from the hook
     */
    protected void contOffHook()
     {
         if(pickupContainer && this.agv!= null)
         {
          this.agv.setContainer(cont);
          agv = null;
         }
         else if(!pickupContainer && this.transporter!= null)
         {
             transporter.setContainer(cont);
             transporter = null;
         }
         loaded = false;
     }
     
    /**
     *Process of attaching a container to the hook
     */
    protected void contToHook() 
    {
        hNode.attachChild(cont);
        cont.setLocalTranslation(hook.getLocalTranslation().add(new Vector3f(0,cont.size.y,0)));
        
        if(pickupContainer && transporter != null){
            transporter.getContainer(cont.indexPosition);
            transporter = null;
        }
        else if(!pickupContainer && agv!= null)
        {
            agv.getContainer();//this.cont=
            agv = null;
        } 
        else if(this instanceof BufferCrane)
        {
            ((BufferCrane)this).buffer.removeContainer(cont.indexPosition);
        }
        cont.rotate(0, base.getWorldRotation().toAngles(null)[1], 0);
        loaded = true;
    }
    
       /**
     *
     * Resets default position of chosen option
     * @param option
     */
    protected void resetPos(int option)
       {
        switch(option)
        {
            case 1:
                break;
            case 2:
                  this.sNode.setLocalTranslation(defPosSlider.add(0.1f,0,0.1f));
                break;
            case 3:
                 this.hNode.setLocalTranslation(defPosHook.add(0.1f,0,0.1f));
                break;
        }
       }
     
     /**
      * sets the crane ready for load if not done already, and sends a message to controller.
     * returns true if the crane is ready and the controller has given permission to load
     * @return
     */
    protected boolean readyToLoad()
     {
           if (!readyForL) 
            {
                readyForL = true;
                sendMessage("container ready for drop");
            } 
            return (readyForL && loadContainer);
     }
     
     /**
     * move the crane to its default position
     */
    public void moveToHome()
     {
         
        doAction(1,true);
     }
     
     /**
     * initializes the motionpath of the hook and plays the animation
     * @param reversed
     */
    protected abstract void moveHook(boolean reversed);
     /**
     *initializes the motionpath of the base and plays the animation
     * @param reversed
     */
    protected abstract void moveBase(boolean reversed);
     /**
     *initializes the motionpath of the slider and plays the animation
     * @param reversed
     */
    protected abstract void moveSlider(boolean reversed);
     /**
     *update the actions for getting a container from an AGV
     */
    protected abstract void updateGet();
     /**
     *update the actions for getting a container from a transporter/buffer
     */
    protected abstract void updatePickup();
     
     /**
     * returns the parkingspot for an AGV from this crane
     * @return
     */
    public abstract ParkingSpot getParkingspot();
     
     /**
     *  update method to be called from Main
     * updates this cranes actions
     * @param tpf
     */
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
     /**
     *updates the current speed
     */
    protected void updateSpeed() 
     {
        baseControl.setSpeed(loaded ? 0.5f : 1);
        sliderControl.setSpeed(loaded ? .5f : 1);
        hookControl.setSpeed(loaded ? 0.5f : 1);
     }
    

     
     /**
     * Keeping track of where the motionpath is
     * every motionpath has 2 points. When index 1 is reached, the action
     * is incremented with 1, which modifies the current action.
     * @param motionControl
     * @param wayPointIndex
     */
    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {

        action += wayPointIndex;
    }

}

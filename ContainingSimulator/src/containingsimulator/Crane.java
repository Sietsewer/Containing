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

    protected void resetAll()
    {
        
         this.action = 0;
         this.busy = false;
         this.readyForL = false;
         this.loadContainer = false;
         this.target = null;
         this.cont = null;
         this.transporter = null;
         this.transportNode= null;
         sendMessage(this.id + " transfer finished");
    }
    
    protected void sendMessage(String message)
    {
        System.out.println(message);
          Main.sendReady(this.id);
    }
    
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
    //from transport/buffer to agv
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

        
    //From agv to transport/buffer
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
        
    /* public void loadContainer(Transporter trans) 
     {
         if(!loadContainer)
         {
        this.target = trans.getWorldTranslation();
        loadContainer = true;
         }
         else
         {
              debugMessage(4,"loadContainer");
         }
     }*/
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
     }
     
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
    }
    
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
     
     protected boolean readyToLoad()
     {
           if (!readyForL) 
            {
                readyForL = true;
                sendMessage("container ready for drop");
            } 
            return (readyForL && loadContainer);
     }
     
     public void moveToHome()
     {
         
        doAction(1,true);
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

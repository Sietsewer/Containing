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
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Len
 */
public abstract class Crane extends Node implements MotionPathListener {

    private String id;
    private Vector3f position;
    private MotionPath basePath = new MotionPath();
    private MotionPath hookPath = new MotionPath();
    private MotionPath sliderPath = new MotionPath();
    private MotionEvent baseControl;
    private MotionEvent hookControl;
    private MotionEvent sliderControl;
    private Vector3f defPosBase;
    private boolean loaded = false;
    private boolean goToHome = false;
    private boolean busy = false;
    private boolean readyForL = false;
    private boolean loadContainer = false;
    private boolean pickupContainer;
    
    protected Vector3f defPosHook;
    protected Vector3f defPosSlider;
    protected int action = 0;
    protected Spatial base;
    protected Spatial hook;
    protected Spatial slider;
    protected Node sNode = new Node();
    protected Node hNode = new Node();
    protected float baseDur = 2f;
    protected float hookDur = 2f;
    protected float sliDur = 2f;
    protected float attachDur = 30f;
    protected float detachDur = 10f;
    protected float baseLP = 1f;
    protected float sliderLP = 1f;
    protected float hookLP = 1f;
    protected Vector3f target = null;
    protected Container cont = null;
    protected Transporter transporter = null;
    protected AGV agv = null;
    protected float timeTarget = 0;
    protected float timeCount = 0;
    private boolean[]pathWasPlaying = new boolean[3];
    protected boolean waitingOnTimer = false;
    protected Vector3f parkingVector = null;
    protected boolean onlyMoveToPos = false;
    

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
        this.defPosBase = this.position.clone();
         
        this.attachChild(this.base);
        
        hNode.attachChild(this.hook);
        sNode.attachChild(hNode);
        sNode.attachChild(this.slider);

        this.attachChild(this.sNode);
        
        baseControl = new MotionEvent(this, basePath, baseDur / Main.globalSpeed, LoopMode.DontLoop);
        hookControl = new MotionEvent(this.hNode, hookPath, hookDur / Main.globalSpeed, LoopMode.DontLoop);
        sliderControl = new MotionEvent(this.sNode, sliderPath, sliDur / Main.globalSpeed, LoopMode.DontLoop);
        
        basePath.setCycle(false);
        hookPath.setCycle(false);
        sliderPath.setCycle(false);

        basePath.addListener(this);
        hookPath.addListener(this);
        sliderPath.addListener(this);
        
        basePath.setPathSplineType(Spline.SplineType.Linear);
        sliderPath.setPathSplineType(Spline.SplineType.Linear);
        hookPath.setPathSplineType(Spline.SplineType.Linear);
        for(int i = 0; i < pathWasPlaying.length;i++)
        {
            pathWasPlaying[i] = false;
        }
        this.parkingVector = position;
      
    }
    
       
    public String getID()
    {
        return this.id;
    }
    
    public Vector3f getPos()
    {
        return this.position;
    }
    
    public String getId() 
    {
        return this.id;
    }
  
    public boolean getIsBusy()
    {
        return this.busy;
    }

    public void moveToHome()
    {
         goToHome = true;
    }
    public Quaternion getBaseRotation()
    {
        return this.base.getLocalRotation();
    }
    
     /**
     * returns the parkingspot for an AGV from this crane
     * @return
     */
    public abstract ParkingSpot getParkingspot();
    
   /**
     * update method to be called from Main
     * updates this cranes actions
     * @param tpf
    */

    
    
    
    public void update(float tpf)
     {
         if(target!=null && action != 0)
         {
         updateSpeed();
         
         if(waitingOnTimer)
         {
          timeCount+= tpf*Main.globalSpeed;
          if(timeCount>=timeTarget)
         {
            timeTarget = 0;            
            waitingOnTimer = false;
            action++;
         }
          else{return;}
         }
         
         if(onlyMoveToPos)
         {
             updateMoveToPos();
         }else
         if(pickupContainer)
         {
          updatePickup();
         }
         else
         {
          updateGet();
         }
         }
         else if (goToHome && !busy)
         {
           goToHome = false;
           doAction(1,true);
         }
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
    public void moveToPos(Vector3f realPosition)
    {
        if(!busy)
        {
            System.out.println(this.id + " move to Pos: " + realPosition);
           target = realPosition;
           onlyMoveToPos = true;
           initializeStartUp();
        }else{System.out.println(this.id + " is already busy");}
    }
    private void updateMoveToPos()
    {
        System.out.println(this.id + " moves to position: " + target + ", is at action:" + action );
        switch(action)
        {
            case 1:
                doAction(1,false);
                break;
            case 2:
                onlyMoveToPos = false;
                finishActions();

                break;
        }
    }
    
     /**
     * First method to be called from main for 
     * picking up a container from a transporter or buffer.
     * Initializes variables needed for starting this action
     * @param cont
     * @param trans
     */
    protected boolean pickupContainer(Container cont)
    {
         if(busy)
         {
           debugMessage(4,"pickupContainer");
           return false;
         }
         
          pickupContainer = true;
          this.cont = cont;
          initializeStartUp();
          
          return true;
         
    }
    public void pickupContainer(Container cont, Transporter trans)
    {
       if(pickupContainer(cont))
       {
          this.transporter = trans;   
       }
    }

    
     /**
     *method to be called for loading a container from an agv
     * @param agv
     */
    public void loadContainer(AGV agv)
     {
        if(!loadContainer && this.cont != null)
        {
   
         this.agv = agv;
         this.target = agv.getWorldTranslation().add(0,cont.size.y*2,0);
         loadContainer = true;
        }
        else
         {
              debugMessage(4,"loadContainer");
         }
    }
    
     /**
     * Method to be called for putting a container on a transporter/buffer
     * @param nextPosition
     * @param indexPosition
     */
    public void putContainer(Vector3f realPosition,Vector3f indexPosition)
     {
         if(cont !=null)
         {
         this.target = realPosition;
         this.cont.setIndexPosition(indexPosition);
         loadContainer = true;
         }
         else
         {
             debugMessage(1,"putContainer");
         }
     }
    public void putContainer(Vector3f realPosition, Vector3f indexPosition, Transporter trans)
    {
        if(trans!=null){
        this.transporter = trans;
        this.putContainer(realPosition, indexPosition);}
        else
        {
            debugMessage(2,"putContainer");
        }
                
    }
    
       /**
     *update the actions for getting a container from an AGV
     */
    protected abstract void updateGet();
     /**
     *update the actions for getting a container from a transporter/buffer
     */
    protected abstract void updatePickup();

     /**
     *updates the current speed
     */
    
    /**
     *resets all variables and sends end-messsage to Controller
     */
    protected void finishActions()
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

    private void sendMessage(String message)
    {
        System.out.println(message);
          Main.sendReady(this.id);
    }
    
    /**
     * calls classmethod for playing a motionpath, based on which globalAction is given and if is toDefault.
     * @param globalAction
     * @param toDefault
     * @return
     */
    protected boolean doAction(int globalAction, boolean toDefault)
    {
        switch(globalAction)
        {
            case 1: //move base
                 if(!baseControl.isEnabled())
                { 
                   moveBase(toDefault);
                   return true;
                }
                break;
            case 2: //move slider
                if(!sliderControl.isEnabled())
                {
                   moveSlider(toDefault);
                   return true;
                }
                break;
            case 3:
                 if(!hookControl.isEnabled())
                {
                   moveHook(toDefault);
                   return true;
                }
                break;
        }
        return false;
    }
    
    protected void commonActions()
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
                if(!waitingOnTimer){
                 setTimer(attachDur);}   
                break;
            case 5:
                if(doAction(3,true))
                {
                    this.contToHook();
                }
                break;              
        }
    }
    protected void setTimer(float maxTime)
    {
        timeTarget = maxTime;
        timeCount = 0;
        waitingOnTimer = true;
    }
    
         /**
     * Process of detaching the container from the hook
     */
    protected void contOffHook()
     {
         if(pickupContainer && this.agv != null)
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
        
        if(pickupContainer)
        {
        if(transporter != null)
        {
            transporter.getContainer(cont.indexPosition);
            transporter = null;
        }
        else if(this instanceof BufferCrane)
        {
            ((BufferCrane)this).getBuffer().removeContainer(cont.indexPosition);
        }
        
        }
        else if(!pickupContainer)
        {
            if(agv!= null){
            agv.getContainer();//this.cont=
            agv = null;}
            else{System.out.println(this.id + " has agv that is null at getContainer");}
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
            case 2:
                 this.sNode.setLocalTranslation(defPosSlider);
                break;
            case 3:
                 this.hNode.setLocalTranslation(defPosHook);
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
                sendMessage(this.id + " has container ready for drop");
            } 
            return (readyForL && loadContainer);
     }

    private void initializeStartUp()
    {
        if(!onlyMoveToPos)
        {
        this.target = cont.getWorldTranslation().subtract(new Vector3f(0,cont.size.y,0));
        }
        action = 1;
        busy = true;
        Vector3f temp = null;
        if(!((this instanceof BufferCrane)||(this instanceof LorryCrane)))
        {
        
        if(this instanceof TrainCrane || this instanceof BargeCrane)
        {
        temp = new Vector3f(target.x,this.getLocalTranslation().y,this.getLocalTranslation().z);
        }
        else
        {
        temp= new Vector3f(this.getLocalTranslation().x,this.getLocalTranslation().y,target.z); 
        }
        Path.updatePath(this.id, temp);
        }
         this.parkingVector = temp;
        
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
    
     //initialize new motionpath/play animation
    private boolean moveSpatial(MotionEvent mC, MotionPath mP, Float duration, Vector3f startPos, Vector3f destPos)
    {
        if(startPos.distance(destPos)>0) //if next position is not current position
        {
        mP.clearWayPoints();
        mP.addWayPoint(startPos);
        mP.addWayPoint(destPos);
        setEventDuration(mC, mP, duration);
        mC.play();
        return true;
        }
        else //movement not necessary
        {
        return false;
        }
    }
    
    //move the base of the crane
      private void moveBase(boolean toDefault)
    {
        Vector3f startPos = this.getLocalTranslation();
        Vector3f destPos;
        if(toDefault)
        {
        destPos = defPosBase;
        }
        else
        {
        if(this instanceof TrainCrane || this instanceof BargeCrane)
        {
        destPos = new Vector3f(target.x,startPos.y,startPos.z);
        }
        else
        {
        destPos = new Vector3f(startPos.x,startPos.y,target.z); 
        }
        }

         if(!moveSpatial(baseControl,basePath,baseDur,startPos,destPos))
        {
            action++;
        }
        else
        {
            pathWasPlaying[0] = true;
        }
        
    }
      //move the slide of the crane
      private void moveSlider(boolean toDefault)
    {
        Vector3f startPos = sNode.getLocalTranslation();
        Vector3f destPos;
  
        if(toDefault)
        {
          destPos = defPosSlider;
        }
        else
        {
            if(this instanceof TrainCrane || this instanceof BargeCrane)
            {
                destPos = new Vector3f(new Vector3f(startPos.x ,startPos.y, target.z-sNode.getWorldTranslation().z));
            }
            else
            {
                destPos = new Vector3f(new Vector3f(target.x-sNode.getWorldTranslation().x ,startPos.y, startPos.z));
            }
        }
        if(!moveSpatial(sliderControl,sliderPath,sliDur,startPos,destPos))
        {
            action++;
        }
        else
        {
            pathWasPlaying[1] = true;
        }
    }
    //move the hook of the crane
    private void moveHook(boolean toDefault)
    {
        Vector3f startPos = hNode.getLocalTranslation();
        Vector3f destPos;
        
        if(toDefault)
        {
         destPos = defPosHook;
        }
        else
        {
        destPos = new Vector3f(startPos.x, target.y - sNode.getWorldTranslation().y, startPos.z);
        }
        if(!moveSpatial(hookControl,hookPath,hookDur,startPos,destPos))
        {
           action++;
        }
        else
        {
            pathWasPlaying[2] = true;
        }
    }
    
   //update speed/duration of motionpaths/motionevents
    private void updateSpeed() 
     {
        setEventDuration(baseControl, basePath, baseDur);
        setEventDuration(sliderControl,sliderPath,sliDur);
        setEventDuration(hookControl,hookPath,hookDur);

        baseControl.setSpeed(loaded ? baseLP : 1);
        sliderControl.setSpeed(loaded ? sliderLP : 1);
        hookControl.setSpeed(loaded ? hookLP : 1);
     }
    public void pausePlay(boolean pause)
    {
        for(int i = 0; i <pathWasPlaying.length;i++)
        {
                if(pathWasPlaying[i])
                {
                switch(i)
                {
                    case 0:
                        if(!pause){
                        baseControl.play();}
                        else
                        {
                            baseControl.pause();
                        }
                        break;
                    case 1:
                        if(!pause){
                        sliderControl.play();}
                        else
                        {
                            sliderControl.pause();
                        }
                        break;
                    case 2:
                        if(!pause){
                        hookControl.play();}
                        else
                        {
                            hookControl.pause();
                        }
                        break;   
                }
                }
        }
    }
    //set duration of motionevents
    private void setEventDuration(MotionEvent event, MotionPath path, float defDur)
    {
        if(path.getNbWayPoints()>1)
        {
            event.setInitialDuration(path.getLength()/(defDur*Main.globalSpeed));
        }
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
        if(wayPointIndex == 1)
        {
            
            if(motionControl.equals(baseControl))
            {
                pathWasPlaying[0] = false;
            }
            else if(motionControl.equals(hookControl))
            {
                pathWasPlaying[2] = false;
            }
            else if(motionControl.equals(sliderControl))
            {
                pathWasPlaying[1] = false;
            }
            
        }
    }
    

}

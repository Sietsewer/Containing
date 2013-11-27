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
public abstract class Crane extends Node implements MotionPathListener{
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
    protected Spatial base;
    protected Spatial hook;
    protected MotionEvent baseControl;
    protected MotionEvent hookControl;
    protected boolean busy = false;
    protected boolean readyForL = false;
    protected boolean loadContainer = false;
    protected Container cont;
    protected static final float baseDur = 2f;
    protected static final float hookDur = 2f;
    protected Vector3f target;
    protected Transporter transporter;
    
    
    public Crane(String id, Vector3f pos, Spatial base, Spatial hook)
    {
         this.id = id;
         this.position = pos;
         this.base = base.clone();
         this.hook = hook.clone();
       
         this.attachChild(this.base);
 
         baseControl = new MotionEvent(this,basePath,baseDur/Main.globalSpeed,LoopMode.DontLoop);
         hookControl = new MotionEvent(this.hook,hookPath,hookDur/Main.globalSpeed,LoopMode.DontLoop);
       
         basePath.setCycle(false);
         hookPath.setCycle(false);

         basePath.addListener(this);
         hookPath.addListener(this);
    }
    
    public String getId()
    {
        return this.id;
    }
    
    public boolean isbusy()
    {
        return this.busy;
    }
    

    
    public abstract void loadContainer(Transporter transporter);
    /**
     *
     * @param cont
     */
       
    public void getContainer(Container cont)
    {
       
       
        
        this.cont = cont;
        this.target = cont.getWorldTranslation();
        action = 1;
        busy = true;
        
    }
    public void getContainer(Vector3f pos)
    {
        if(pos != null)
        {
        this.target = pos;
        action = 1;
        busy = true;
        }
    }
    
    public abstract void update(float tpf);
    
            
}

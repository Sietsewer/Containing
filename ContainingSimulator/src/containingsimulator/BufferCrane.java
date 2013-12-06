/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author User
 */
public class BufferCrane extends Crane {
    
    private Buffer buffer;

    public BufferCrane(String id, Vector3f basePos, Spatial base, Spatial slider, Spatial hook, Buffer buffer)
    {
        super(id, basePos,base,slider, hook);
        this.buffer = buffer;
        hNode.setLocalTranslation(new Vector3f(0,18.5f,0));
        this.hook.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        
        this.defPosHook = hNode.getWorldTranslation().clone();
        this.defPosSlider = sNode.getWorldTranslation().clone();
        
        this.baseDur = 15f;
        this.sliDur = 15f;
        this.hookDur = 15f;
    }
    public Buffer getBuffer()
    {
        return this.buffer;
    }

    private void commonSteps()
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
               if(doAction(3,true))
               {
                    contToHook();
               }
                break;
         }
     }

     @Override
    protected void updateGet()
    {
         switch(action)
        {
             default:
                 commonSteps();
                 break;
            case 5:
                  if(readyToLoad())
                  {
                    if (doAction(1,false))
                    {  
                        resetPos(3);
                    }
                  }
                break;
            case 6:
                doAction(2,false);
                break;
            case 7:
                doAction(3,false);
                break;
            case 8:
                if(doAction(3,true))
                {
                   this.buffer.addContainer(cont.indexPosition, cont);
                   this.hNode.detachChild(cont);
                }
                break; 
            case 9:
                resetPos(2);
                resetPos(3);
                finishActions();
                break;
         }
    }
 
     
    @Override
    protected void updatePickup()
    {
        switch(action)
        {
            default:
                commonSteps();
                break;
            case 5:
                doAction(2,true);
                break;
            case 6:
                doAction(1,true);
                break;
            case 7: //nog niet af vanaf hier
                  if(readyToLoad())
                  {
                    doAction(3,false);
                  }
                break;
            case 8:
                 contOffHook();
                break;
            case 9:
                resetPos(2);
                resetPos(3);
                finishActions();
                break;
        }
    }
    
    @Override
    public ParkingSpot getParkingspot() {
        return buffer.getBestParkingSpot();
    }
}


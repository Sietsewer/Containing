/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import containing.xml.SimContainer;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;

/**
 *
 * @author Ruben
 */
public class Transporter extends Node implements MotionPathListener {

    /**
     * Container array
     */
    private Container[][][] containers;
    /**
     * position of transporter
     */
    public Vector3f position;
    /**
     * transporter type
     */
    public int type;
    /**
     * transporter id
     */
    public String id;
    /**
     * SEASHIP Geometry
     */
    public static Geometry SEASHIP;
    /**
     * BARGE Geometry
     */
    public static Geometry BARGE;
    /**
     * TROLLEY Geometry
     */
    public static Geometry LORRY;
    /**
     * TRAIN Geometry
     */
    public static Geometry TRAIN;
    /**
     * SEASHIP Box
     */
    public static Box SEASHIPb;
    /**
     * BARGE Box
     */
    public static Box BARGEb;
    /**
     * TROLLEY Box
     */
    public static Box LORRYb;
    /**
     * TRAIN Box
     */
    public static Box TRAINb;
    private MotionEvent motionEvent;
    MotionPath path = new MotionPath();

    /**
     * Constructor
     *
     * @param containersList
     * @param position
     * @param type
     */
    public Transporter(String id, ArrayList<SimContainer> containersList, Vector3f position, int type) {
        super(id);
        this.id = id;
        this.position = position;
        this.type = type;

        Geometry currentGeometry;
        Vector3f size;
        

        switch (type) {
            case TransportTypes.SEASHIP:
                containers = new Container[20][6][16];
                currentGeometry = SEASHIP.clone();
                size = new Vector3f(SEASHIPb.xExtent, SEASHIPb.yExtent, SEASHIPb.yExtent);
                position.y -= 10f;
                position.x -= 40f;
                break;
            case TransportTypes.BARGE:
                containers = new Container[12][4][4];
                currentGeometry = BARGE.clone();
                size = new Vector3f(BARGEb.xExtent, BARGEb.yExtent, BARGEb.yExtent);
                this.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
                position.y -= 10f;
                position.z += 20f;
                break;
            case TransportTypes.TRAIN:
                containers = new Container[29][1][1];
                currentGeometry = TRAIN.clone();
                size = new Vector3f(TRAINb.xExtent, TRAINb.yExtent, TRAINb.yExtent);
                position.z -= 8f;
                this.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
                 
                break;
            default:
            case TransportTypes.LORRY:
                containers = new Container[2][1][1];
                currentGeometry = LORRY.clone();
                size = new Vector3f(LORRYb.xExtent, LORRYb.yExtent, LORRYb.yExtent);
                
                break;
        }
        
        Vector3f first = new Vector3f(position);
        
        switch (type) {
            case TransportTypes.SEASHIP:
                first.z -= 200f; 
                break;
            case TransportTypes.BARGE:
                first.x -= 200f;
                break;
            case TransportTypes.TRAIN:
                first.x += 1000f;
                break;
            default:
            case TransportTypes.LORRY:
                first.z += 50f; 
                break;
        }
            

        for (SimContainer container : containersList) {
       
            try {
                this.containers[(int) container.getIndexPosition().x][(int) container.getIndexPosition().y][(int) container.getIndexPosition().z] = new Container(container);
            } catch (Exception e) {
                System.err.println(e.getMessage() + " " + container.getIndexPosition());
            }

        }
        path.setCycle(false);
        path.setPathSplineType(Spline.SplineType.Linear);
        path.addListener(this);
        motionEvent = new MotionEvent(this,this.path);

        path.addWayPoint(first);
        path.addWayPoint(position);


        for (int z = 0; z < containers[0][0].length; z++) {
            for (int x = 0; x < containers.length; x++) {
                for (int y = 0; y < containers[0].length; y++) {
                    if (containers[x][y][z] != null) {
                        Container con = containers[x][y][z];
                        Vector3f vec = con.getLocalTranslation();
                        vec.y += 1.5f;
                        vec.x -= size.x - 1.22f;
                        vec.z += size.z * 2;

                        this.attachChild(con);
                    }
                }
            }
        }
        setRendering();

        this.setLocalTranslation(position);
        
        this.attachChild(currentGeometry);
        this.motionEvent.play();
    }

    public Transporter(String id, SimContainer container, Vector3f position) {
        this.id = id;
        containers = new Container[1][1][1];
        Container con = new Container(container);
        Vector3f vec = con.getLocalTranslation();
        vec.y += 1.72f;

        containers[0][0][0] = con;
        
        path.setCycle(false);
        path.setPathSplineType(Spline.SplineType.Linear);
        this.path.addListener(this);
        motionEvent = new MotionEvent(this,this.path);
        
        Vector3f first = new Vector3f(position);
        first.z += 10f; 
        path.addWayPoint(first);
        path.addWayPoint(position);
        
        this.attachChild(con);
        this.attachChild(LORRY.clone());
        this.motionEvent.play();
    }

    /**
     * When called, places a container on the Transporter. null can be sent.
     *
     * @param container The container to be placed on the Transporter.
     * @return True if the container was received, False if there already is a
     * container.
     */
    public boolean setContainer(Container container) {
        Vector3f pos = new Vector3f(container.indexPosition.x, container.indexPosition.y, container.indexPosition.z);
        if (containers[(int) pos.x][(int) pos.y][(int) pos.z] == null) {
            containers[(int) pos.x][(int) pos.y][(int) pos.z] = container;
            this.attachChild(container);
            Vector3f vec = container.getLocalTranslation();
            vec.y += 1.72f;
            setRendering();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the container currently on the Transporter, and removes it from the
     * Transporter.
     *
     * @return the container that was once on the AGV. Null if the Transporter
     * was empty.
     */
    public Container getContainer(Vector3f position) {
        Container tempCont = null;
        if (this.containers[(int) position.x][(int) position.y][(int) position.z] != null) {
            tempCont = (Container) this.containers[(int) position.x][(int) position.y][(int) position.z].clone();
            this.detachChild(this.containers[(int) position.x][(int) position.y][(int) position.z]);
            this.containers[(int) position.x][(int) position.y][(int) position.z] = null;
            setRendering();
        }

        return tempCont;
    }
    
    public Container getContainerByID(String id){
        Container tempCont = null;
        for(int i = 0; i < containers.length; i++){
            for(int j = 0; j < containers[0].length; j++){
                for(int k = 0; k < containers[0][0].length; k++){
                    if(containers[i][j][k] != null && containers[i][j][k].id.equalsIgnoreCase(id)){
                        tempCont = containers[i][j][k];
                        return tempCont;
                    }
                }
            }
        }
        return tempCont;
    }

    /**
     * Set which geometries have to be drawn and which not
     */
    private void setRendering() {
        //cull walled-in containers
        for (int i = 0; i < containers.length; i++) {
            for (int j = 0; j < containers[0].length; j++) {
                for (int k = 0; k < containers[0][0].length; k++) {
                    if (containers[i][j][k] != null) {
                        containers[i][j][k].updateRendering(containers);
                    }
                }
            }
        }
    }
    
    private void nextWaypoint(int wayPointIndex){
        this.lookAt(path.getWayPoint(wayPointIndex),Vector3f.UNIT_Y);
        this.motionEvent.setSpeed(.5f*Main.globalSpeed);
        
    }
    
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        if(wayPointIndex == path.getNbWayPoints()-1){
            path.clearWayPoints();
            Main.sendReady(id);
        } else {
            nextWaypoint(wayPointIndex);
        }
    }

    /**
     * Prepare Geometry object IMPORTANT: call this before instantiating a
     * Transporter object!
     *
     * @param am the AssetManager to load materials from
     */
    public static void makeGeometry(AssetManager am) {
        SEASHIPb = new Box(new Vector3f(0, 0, 128f), 18.3f, 0.5f, 134.1f); //container size in m divided by 2 because box size grows both ways
        SEASHIP = new Geometry("Seaship", SEASHIPb);
        BARGEb = new Box(new Vector3f(0, 0, 80f), 4.88f, 0.5f, 85f); //container size in m divided by 2 because box size grows both ways
        BARGE = new Geometry("Barge", BARGEb);

        LORRYb = new Box(Vector3f.ZERO, 1.22f, 0.5f, 6.705f); //container size in m divided by 2 because box size grows both ways
        LORRY = new Geometry("Lorry", LORRYb);
        TRAINb = new Box(new Vector3f(0, 0, 190f), 1.22f, 0.5f, 200f); //container size in m divided by 2 because box size grows both ways
        TRAIN = new Geometry("Train", TRAINb);

        Material mat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);



        SEASHIP.setMaterial(mat);
        BARGE.setMaterial(mat);
        LORRY.setMaterial(mat);
        TRAIN.setMaterial(mat);
    }

    /**
     * Load containers
     *
     * @param container
     */
    public void loadContainer(Container container) {
    }

    
}
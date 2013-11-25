/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;

/**
 *
 * @author Ruben
 */
public class Transporter extends Node {

    /**
     * Container array
     */
    public Container[][][] containers;
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

    /**
     * Constructor
     * @param containersList
     * @param position
     * @param type
     */
    public Transporter(ArrayList<SimContainer> containersList, Vector3f position, int type) {
        
        this.position = position;
        this.type = type;

        Geometry currentGeometry;
        Vector3f size;

        switch (type) {
            case TransportTypes.SEASHIP:
                containers = new Container[20][15][5];
                currentGeometry = SEASHIP.clone();
                size = new Vector3f(SEASHIPb.xExtent, SEASHIPb.yExtent, SEASHIPb.yExtent);
                break;
            case TransportTypes.BARGE:
                containers = new Container[12][3][2];
                currentGeometry = BARGE.clone();
                size = new Vector3f(BARGEb.xExtent, BARGEb.yExtent, BARGEb.yExtent);
                break;
            case TransportTypes.TRAIN:
                containers = new Container[29][1][1];
                currentGeometry = TRAIN.clone();
                size = new Vector3f(TRAINb.xExtent, TRAINb.yExtent, TRAINb.yExtent);
                break;
            default:
            case TransportTypes.LORRY:
                containers = new Container[2][1][1];
                currentGeometry = LORRY.clone();
                size = new Vector3f(LORRYb.xExtent, LORRYb.yExtent, LORRYb.yExtent);
                break;
        }
        
        for (SimContainer container : containersList) {
            this.containers[(int) container.getIndexPosition().x][(int) container.getIndexPosition().y][(int) container.getIndexPosition().z] = new Container(container);
        }


        for (int z = 0; z < containers[0][0].length; z++) {
            for (int x = 0; x < containers[0].length; x++) {
                for (int y = 0; y < containers.length; y++) {
                    if (containers[x][y][z] != null) {
                        Container con = containers[x][y][z];
                        Vector3f vec = con.getLocalTranslation();
                        vec.y += 1.72f;
                        this.attachChild(con);
                    }
                }
            }
        }

        currentGeometry.setLocalTranslation(position);
        this.attachChild(currentGeometry);
    }
    
    public Transporter(SimContainer container, Vector3f position) {
        containers = new Container[1][1][1];
        container.setIndexPosition(new CustomVector3f(0,0,0));
        Container con = new Container(container);
        Vector3f vec = con.getLocalTranslation();
        vec.y += 1.72f;
        
        containers[0][0][0] = con;
        this.attachChild(con);
        this.attachChild(LORRY.clone());
    }
    
    /**
     * When called, places a container on the Transporter. null can be sent.
     * @param container The container to be placed on the Transporter.
     * @return True if the container was received, False if there already is a container.
     */
    public boolean setContainer(Container container){
        Vector3f pos = new Vector3f(container.indexPosition.x, container.indexPosition.y, container.indexPosition.z);
        if(containers[(int)pos.x][(int)pos.y][(int)pos.z] == null){
            containers[(int)pos.x][(int)pos.y][(int)pos.z] = container;
            this.attachChild(container);
            Vector3f vec = container.getLocalTranslation();
            vec.y += 1.72f;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Gets the container currently on the Transporter, and removes it from the Transporter.
     * @return the container that was once on the AGV. Null if the Transporter was empty.
     */
    public Container getContainer(Vector3f position){
        Container tempCont;
        tempCont = (Container)this.containers[(int)position.x][(int)position.x][(int)position.z].clone();
        this.detachChild(this.containers[(int)position.x][(int)position.x][(int)position.z]);
        this.containers[(int)position.x][(int)position.x][(int)position.z] = null;
        
        return tempCont;
    }

    /**
     * Prepare Geometry object IMPORTANT: call this before instantiating a
     * Transporter object!
     *
     * @param am the AssetManager to load materials from
     */
    public static void makeGeometry(AssetManager am) {
        SEASHIPb = new Box(Vector3f.ZERO, 16.15f, 0.5f, 152.4f); //container size in m divided by 2 because box size grows both ways
        SEASHIP = new Geometry("Seaship", SEASHIPb);
        BARGEb = new Box(Vector3f.ZERO, 8.64f, 0.5f, 47.565f); //container size in m divided by 2 because box size grows both ways
        BARGE = new Geometry("Barge", BARGEb);
        
        LORRYb = new Box(Vector3f.ZERO, 1.22f, 0.5f, 6.705f); //container size in m divided by 2 because box size grows both ways
        LORRY = new Geometry("Lorry", LORRYb);
        TRAINb = new Box(Vector3f.ZERO, 1.22f, 0.5f, 700f); //container size in m divided by 2 because box size grows both ways
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
     * @param container
     */
    public void loadContainer(Container container) {
    }
}

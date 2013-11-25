/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

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
    public SimContainer[][][] containers;
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
    public static Geometry TROLLEY;
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
    public static Box TROLLEYb;
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
                containers = new SimContainer[15][15][26];
                currentGeometry = SEASHIP.clone();
                size = new Vector3f(SEASHIPb.xExtent, SEASHIPb.yExtent, SEASHIPb.yExtent);
                break;
            case TransportTypes.BARGE:
                containers = new SimContainer[15][15][26];
                currentGeometry = BARGE.clone();
                size = new Vector3f(BARGEb.xExtent, BARGEb.yExtent, BARGEb.yExtent);
                break;
            case TransportTypes.TRAIN:
                containers = new SimContainer[1][1][50];
                currentGeometry = TRAIN.clone();
                size = new Vector3f(TRAINb.xExtent, TRAINb.yExtent, TRAINb.yExtent);
                break;
            default:
            case TransportTypes.TROLLEY:
                containers = new SimContainer[1][1][1];
                currentGeometry = TROLLEY.clone();
                size = new Vector3f(TROLLEYb.xExtent, TROLLEYb.yExtent, TROLLEYb.yExtent);
                break;
        }
        
        for (SimContainer container : containersList) {
            this.containers[(int) container.getIndexPosition().x][(int) container.getIndexPosition().y][(int) container.getIndexPosition().z] = container;
        }


        for (int z = 0; z < containers[0][0].length; z++) {
            for (int x = 0; x < containers[0].length; x++) {
                for (int y = 0; y < containers.length; y++) {
                    if (containers[x][y][z] != null) {
                        Container con = new Container(containers[x][y][z]);
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
        
        TROLLEYb = new Box(Vector3f.ZERO, 1.22f, 0.5f, 6.705f); //container size in m divided by 2 because box size grows both ways
        TROLLEY = new Geometry("Trolley", TROLLEYb);
        TRAINb = new Box(Vector3f.ZERO, 1.22f, 0.5f, 250f); //container size in m divided by 2 because box size grows both ways
        TRAIN = new Geometry("Train", TRAINb);
        
        Material mat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        
        SEASHIP.setMaterial(mat);
        BARGE.setMaterial(mat);
        TROLLEY.setMaterial(mat);
        TRAIN.setMaterial(mat);
    }

    /**
     * Load containers
     * @param container
     */
    public void loadContainer(Container container) {
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import containing.xml.SimContainer;
import containing.xml.CustomVector3f;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.Random;

/**
 *
 * @author Wessel
 */
public class Container extends Node{
    static Box b;               //container geometry, static to reduce memory load
    static Material mat;        //container material, static to reduce memory load
    static Geometry geom;       //geometry object
    static ColorRGBA[] colors;  //color array
    static Random r;            //RNG for color assignment
    
    String id;                  //container ID
    Vector3f realPosition;      //actual position in the simulation
    Vector3f indexPosition;     //position in transport/buffer
    Vector3f size;              //size in m
    
    /**
     * Constructor
     * @param id container ID
     * @param realPosition actual position inside simulation
     * @param size size of this container
     */
    public Container(String id, Vector3f realPosition, Vector3f size){
        //set properties
        this.id = id;
        this.realPosition = realPosition;
        this.size = size;
        
        setGeometry();
    }


    /**
     * Constructor for SimContainer
     * @param sc SimContainer you want to create a Container for
     */
    public Container(SimContainer sc){
        this.id = sc.getId();
        CustomVector3f cv = sc.getIndexPosition();
        setIndexPosition(new Vector3f(cv.x, cv.y, cv.z));
        this.size = new Vector3f(1.22f, 1.22f, 6.705f);
        this.realPosition = new Vector3f(indexPosition.z * 2 * size.x,
                indexPosition.y * 2 * size.y, indexPosition.x * 2 * size.z);
        
        setGeometry();
    }
    
    /**
     * Change indexPosition when moving container to buffer or back
     * @param indexPosition position to set this container's index to
     */
    public void setIndexPosition(Vector3f indexPosition){
        this.indexPosition = indexPosition;
    }
    
    /**
     * assign color, geometry, etc. to this object
     */
    private void setGeometry(){
        this.setCullHint(Spatial.CullHint.Dynamic);
        this.setLocalTranslation(realPosition);
        //attach geometry to this object
        mat.setColor("Color", colors[r.nextInt(colors.length)]);
        
        geom.setMaterial(mat);
        this.attachChild(geom.clone());
    }
    
    /**
     * Count adjacent neighbours in buffer
     * @param buffer the array in which to check for neighbours
     * @return 
     */
    private int getNeighbours(Container[][][] buffer){
        int neighbours = 0;
        if(indexPosition.x + 1 < buffer.length && buffer[(int)indexPosition.x + 1][(int)indexPosition.y][(int)indexPosition.z] != null){
            neighbours++;
        }
        if(indexPosition.x - 1 >= 0 && buffer[(int)indexPosition.x - 1][(int)indexPosition.y][(int)indexPosition.z] != null){
            neighbours++;
        }
        if(indexPosition.y + 1 < buffer[0].length && buffer[(int)indexPosition.x][(int)indexPosition.y + 1][(int)indexPosition.z] != null){
            neighbours++;
        }
        if(indexPosition.y - 1 >= 0 && buffer[(int)indexPosition.x][(int)indexPosition.y - 1][(int)indexPosition.z] != null){
            neighbours++;
        }
        if(indexPosition.z + 1 < buffer[0][0].length && buffer[(int)indexPosition.x][(int)indexPosition.y][(int)indexPosition.z + 1] != null){
            neighbours++;
        }
        if(indexPosition.z - 1 >= 0 && buffer[(int)indexPosition.x][(int)indexPosition.y][(int)indexPosition.z - 1] != null){
            neighbours++;
        }
        return neighbours;
    }
    
    /**
     * Cull containers with 6 neighbours for optimization
     * @param buffer buffer array in which to search for neighbours
     */
    public void updateRendering(Container[][][] buffer){
        if(getNeighbours(buffer) < 6){
            this.setCullHint(Spatial.CullHint.Dynamic);
        }else{
            this.setCullHint(Spatial.CullHint.Always);
            //System.out.println("Cull at " + this.indexPosition.toString()); //debug
        }
    }
    
    /**
     * Prepare Geometry object
     * IMPORTANT: call this before instantiating a Container object!
     * @param am the AssetManager to load materials from
     */
    public static void makeGeometry(AssetManager am){
        b = new Box(Vector3f.ZERO, 1.22f, 1.22f, 6.705f); //container size in m divided by 2 because box size grows both ways
        geom = new Geometry("Box", b);
        mat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        
        colors = new ColorRGBA[9];
        colors[0] = new ColorRGBA(0.729f, 0.729f, 0.729f, 1);
        colors[1] = new ColorRGBA(0.839f, 0.196f, 0, 1);
        colors[2] = new ColorRGBA(0.075f, 0.082f, 0.204f, 1);
        colors[3] = new ColorRGBA(0.082f, 0.482f, 0.09f, 1);
        colors[4] = new ColorRGBA(0.463f, 0, 0, 1);
        colors[5] = new ColorRGBA(0.463f, 0.227f, 0, 1);
        colors[6] = new ColorRGBA(0.051f, 0.051f, 0.051f, 1);
        colors[7] = new ColorRGBA(0.176f, 0.114f, 0.051f, 1);
        colors[8] = new ColorRGBA(0.314f, 0.314f, 0.314f, 1);
        
        r = new Random();

        mat.setColor("Color", ColorRGBA.Red);
        
        geom.setMaterial(mat);
    }

    @Override
    public String toString() {
        return "Container{" + "id=" + id + ", realPosition=" + realPosition + ", indexPosition=" + indexPosition + ", size=" + size + '}';
    }
    
}
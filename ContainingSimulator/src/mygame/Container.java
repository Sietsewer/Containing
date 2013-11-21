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
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Wessel
 */
public class Container extends Node{
    static Box b;               //container geometry, static to reduce memory load
    static Material mat;        //container material, static to reduce memory load
    static Geometry geom;       //geometry object
    
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
        this.setCullHint(Spatial.CullHint.Always);
        this.setLocalTranslation(realPosition);
        //attach geometry to this object
        mat.setColor("Color", ColorRGBA.randomColor());
        
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
        b = new Box(Vector3f.ZERO, 2.44f, 2.44f, 13.41f);
        geom = new Geometry("Box", b);
        
        //assign random color on instantiaton to differentiate between containers
        if(mat == null){
            mat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        }
        mat.setColor("Color", ColorRGBA.Red);
        
        geom.setMaterial(mat);
    }
}
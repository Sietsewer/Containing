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

/**
 *
 * @author Wessel
 */
public class SimContainer extends Node{
    String id;                  //container ID
    Vector3f realPosition;      //actual position in the simulation
    Vector3f indexPosition;     //posittion in transport/buffer
    Vector3f size;              //size in m
    
    //SimContainer with Container as constructor argument
    public SimContainer(Container c, AssetManager am){
        //set properties
        this.id = c.id;
        this.indexPosition = c.position;
        this.size = c.size;
        //align SimContainer with other containers
        this.realPosition = new Vector3f(c.position.x * 2 * size.x, c.position.y * 2 * size.y, c.position.z * 2 * size.z);
        
        setGeometry(am);
    }
    
    //old constructor, feel free to remove this
    public SimContainer(String id, Vector3f realPosition, Vector3f size, AssetManager am){
        //set properties
        this.id = id;
        this.realPosition = realPosition;
        this.size = size;
        
        setGeometry(am);
    }
    
    //change indexPosition when moving container to buffer ir back
    public void setIndexPosition(Vector3f indexPosition){
        this.indexPosition = indexPosition;
    }
    
    //assign color, geometry, etc. to this object
    private void setGeometry(AssetManager am){
        Box b = new Box(realPosition, size.x, size.y, size.z);
        Geometry geom = new Geometry("Box", b);
        
        //assign random color on instantiaton to differentiate between containers
        Material mat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.randomColor());
        geom.setMaterial(mat);
        
        //attach geometry to this object
        this.attachChild(geom);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Ruben
 */
public class Container extends Node {
    //Properties
    public String id;           //Container id
    private Spatial model;      //Container model
    public Vector3f position;   //Container position in transport or buffer
    
    
    //Constructor
    /**
     *
     * @param id
     * @param model 
     * @param position  
     */
    public Container(String id, Spatial model, Vector3f position){
        this.id = id;
        this.model = model;
        this.position = position;
        this.attachChild(this.model);
    }
    
}

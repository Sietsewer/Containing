/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Sietse
 */
class Waypoint extends Node {
    
    public float speed;
    
    public Waypoint(Vector3f location, float speed){
        this.setLocalTranslation(location);
        this.speed = speed;
    }
}

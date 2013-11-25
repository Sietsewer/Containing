/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;

/**
 *
 * @author Sietse
 */
public class ParkingSpot {
    public Vector3f translation;        //local translation of parking space
    public float rotation;              //rotation of parking space
    
    /**
     * Constructor
     * @param translation
     * @param rotation 
     */
    public ParkingSpot(Vector3f translation, float rotation){
        this.translation = translation;
        this.rotation = rotation;
    }

}

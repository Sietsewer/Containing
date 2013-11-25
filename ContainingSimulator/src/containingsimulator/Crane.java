/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Ruben
 */
public abstract class Crane extends Node{
    /**
     * Crane ID
     */
    public String id;
    /**
     * Crane Position
     */
    public Vector3f position;

    /**
     *
     * @param cont
     */
    public abstract void loadContainer(Container cont);
    /**
     *
     * @param cont
     */
    public abstract void getContainer(Container cont);
}

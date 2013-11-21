/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

/**
 *
 * @author Ruben
 */
public abstract class Crane{
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

/**
 *
 * @author Ruben
 */
public class Crane {

    /**
     * Crane ID
     */
    public String id;
    /**
     * waypoint node of crane
     */
    public PathNode node;

    /**
     * create crane with id
     *
     * @param id
     */
    public Crane(String id) {
        this.id = id;
    }

    /**
     *
     * @param cont
     */
    public void loadContainer(Container cont) {
    }

    /**
     *
     * @param cont
     */
    public void getContainer(Container cont) {
    }
    PathNode pathNode;
}

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
    Container container;
    /**
     * start from range
     */
    public int startRange =0;
    /**
     * Range for crane
     */
    public int range=0;
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
    boolean ready =true;
    boolean getReady() {
        return ready;
    }

    void setIsReady(boolean b) {
        ready = b;
         }

    @Override
    public String toString() {
        return "Crane{" + "id=" + id + ", node=" + node + ", ready=" + ready + '}';
    }
    
}

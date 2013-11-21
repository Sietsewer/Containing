/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.ArrayList;

/**
 *
 * @author Ruben
 */
public class AGV{
    /**
     * Container that the AGV carries
     */
    public Container container;
    /**
     * AGV position
     */
    public Vector3f position;
    /**
     * List with waypoints
     */
    public ArrayList<Waypoint> waypoints;
    /**
     * Name of the home buffer
     */
    public String home;
    /**
     * Name of the AGV
     */
    public String name;

    /**
     * Constructor
     * @param position
     * @param home
     * @param name
     */
    public AGV(Vector3f position, String home, String name) {
        this.position = position;
        this.home = home;
        this.name = name;
    }
    
    /**
     * Command the AGV to pick up and deliver a container
     * @param source
     * @param waypoints
     * @param destination
     */
    public void move(Crane source, ArrayList<Waypoint> waypoints, Crane destination) {
        
    }
}

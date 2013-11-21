/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ruben
 */
public class Buffer {
    /**
     * Id of buffer
     */
    public String id;
    /**
     * List of all containers
     */
    public ArrayList<Container> containers;
    /**
     * List of all owned AGVs
     */
    public ArrayList<AGV> ownedAGV;
    /**
     * List of the 2 cranes
     */
    public ArrayList<Crane> cranes;
    /**
     * Hashmap for reserved spaces
     */
    public HashMap<String,CustomVector3f> reservedSpace;
    /**
     * Position of buffer
     */
    public CustomVector3f position;

    /**
     * Constructor
     * @param position
     * @param containers
     * @param ownedAGV
     * @param cranes
     */
    public Buffer(CustomVector3f position, ArrayList<Container> containers, ArrayList<AGV> ownedAGV, ArrayList<Crane> cranes) {
        this.containers = new ArrayList<>(containers);
        this.ownedAGV = new ArrayList<>(ownedAGV);
        this.cranes = new ArrayList<>(cranes);
        this.reservedSpace = new HashMap<>();
        this.position = position;
    }
    
    /**
     * Checks the list if there are containers to depart
     * @return
     */
    public Container checkDepartingContainers() {
        return null;
    }
    
    /**
     * Checks the best place for container
     * @param container
     * @return Vector
     */
    public CustomVector3f findBestBufferPlace(Container container) {
        return null;
    }
    
    /**
     * Adds a container to buffer
     * @param container
     */
    public void addContainer(Container container) {
        
    }
}
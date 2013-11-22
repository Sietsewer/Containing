/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.ArrayList;
import java.util.Date;
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
     * Array of all containers
     */
    public Container[][][] containers;
    /**
     * List of all owned AGVs
     */
    public ArrayList<AGV> ownedAGV;
    /**
     * Buffer crane
     */
    public Crane crane;
    /**
     * Hashmap for reserved spaces
     */
    public HashMap<CustomVector3f, Container> reservedSpace;
    /**
     * PathNode
     */
    public PathNode pathNodeUp;
    /**
     * PathNode
     */
    public PathNode pathNodeDown;

    /**
     * Constructor
     *
     * @param containers
     * @param crane 
     * @param ownedAGV
     */
    public Buffer(ArrayList<AGV> ownedAGV, Crane crane) {
        this.containers = new Container[6][6][26];
        this.ownedAGV = new ArrayList<>(ownedAGV);
        this.reservedSpace = new HashMap<>();
        this.crane = crane;
    }

    /**
     * Checks the list if there are containers to depart
     *
     * @param date 
     * @return ArrayList<Container>
     */
    public ArrayList<Container> checkDepartingContainers(Date date) {
        ArrayList<Container> departingContainers = new ArrayList<>();
        for (Container[][] containerArray3 : containers) {
            for (Container[] containerArray2 : containerArray3) {
                for (Container container : containerArray2) {
                    if (container.getDateDeparture().after(date) || container.getDateDeparture().equals(date)) {
                        departingContainers.add(container);
                    }
                }
            }
        }
        return departingContainers;
    }

    /**
     * Checks the best place for container
     *
     * @param container
     * @return Vector3f
     */
    public CustomVector3f findBestBufferPlace(Container container) {
        for (int z = 0; z < 26; z++) {
            for (int x = 0; x < 6; x++) {
                for (int y = 6; y >= 0; y--) {
                    if (containers[x][y][z] == null) {
                        if (y > 0 && containers[x][y - 1][z] != null) {
                            if (containers[x][y - 1][z].getDateDeparture().after(container.getDateDeparture())) {
                                return new CustomVector3f(x, y, z);
                            }
                        } else {
                            return new CustomVector3f(x, y, z);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Adds a container to buffer
     *
     * @param container
     */
    public void addContainer(Container container) {
        if (reservedSpace.get(container.getBufferPosition()) == container) {
            containers[(int) container.getBufferPosition().x][(int) container.getBufferPosition().y][(int) container.getBufferPosition().z] = container;
            reservedSpace.remove(container.getBufferPosition());
        }
    }

    /**
     * Removes container
     * 
     * @param container
     */
    public void removeContainer(Container container) {
        for (int z = 0; z < 26; z++) {
            for (int x = 0; x < 6; x++) {
                for (int y = 0; y < 6; y++) {
                    if (containers[x][y][z].getId().equals(container.getId())) {
                        containers[x][y][z] = null;
                    }
                }
            }
        }
    }

    /**
     * Reserves container position
     * 
     * @param container
     */
    public void reservePosition(Container container) {
        if(containers[(int) container.getBufferPosition().x][(int) container.getBufferPosition().y][(int) container.getBufferPosition().z] == null) {
            reservedSpace.put(container.getBufferPosition(), container);
        }
    }
}
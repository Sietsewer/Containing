/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

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
    public HashMap<CustomVector3f, Container> containers;
    /**
     * List of all owned AGVs
     */
    public ArrayList<AGV> ownedAGV;
    /**
     * List of the cranes
     */
    public ArrayList<Crane> cranes;
    /**
     * Hashmap for reserved spaces
     */
    public HashMap<String, CustomVector3f> reservedSpace;
    /**
     * Position of buffer
     */
    public CustomVector3f position;
    public PathNode pathNodeUp;
    public PathNode pathNodeDown;
    private final static CustomVector3f size = new CustomVector3f(6,6,26);
    /**
     * Constructor
     *
     * @param position
     * @param containers
     * @param ownedAGV
     * @param cranes
     */
    public Buffer(CustomVector3f position, ArrayList<Container> containers, ArrayList<AGV> ownedAGV, ArrayList<Crane> cranes) {
        this.containers = new HashMap<>(936);
        this.ownedAGV = new ArrayList<>(ownedAGV);
        this.cranes = new ArrayList<>(cranes);
        this.reservedSpace = new HashMap<>(936);
        this.position = position;
    }

    /**
     * Checks the list if there are containers to depart
     *
     * @return ArrayList<Container>
     */
    public ArrayList<Container> checkDepartingContainers(Date date) {
        ArrayList<Container> departingContainers = new ArrayList<>();

        for (Entry<CustomVector3f, Container> entry : containers.entrySet()) {
            Container container = entry.getValue();

            if (container.getDateDeparture().after(date) || container.getDateDeparture().equals(date)) {
                departingContainers.add(container);
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
        for (int z = 0; z < size.z; z++) {
            for (int x = 0; size.x < 6; x++) {
                for (int y = 0; size.y < 6; y++) {
                    if (containers.get(new CustomVector3f(x, y, z)) != null) {
                        continue;
                    }
                    if (y > 0) {
                        if (containers.get(new CustomVector3f(x, y -1, z)).getDateDeparture().after(container.getDateDeparture())) {
                            return new CustomVector3f(x, y, z);
                        } else {
                            continue;
                        }
                    } else {
                        return new CustomVector3f(x, y, z);
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
    public void addContainer(Container container, CustomVector3f position) {
        containers.put(position, container);
    }

    public void removeContainer(Container container) {
        for (Entry<CustomVector3f, Container> entry : containers.entrySet()) {
            CustomVector3f key = entry.getKey();
            Container mapContainer = entry.getValue();

            if (container == mapContainer) {
                containers.remove(key);
                break;
            }
        }
    }
}
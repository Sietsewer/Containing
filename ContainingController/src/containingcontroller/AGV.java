/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.ArrayList;
import java.util.List;
import javax.xml.soap.Node;

/**
 *
 * @author Ruben
 */
public class AGV {

    static private int id = 1;
    /**
     * Indicating if cart is home
     */
    public boolean isHome;
    /**
     * Container that the AGV carries
     */
    public Container container;
    /**
     * Name of the home buffer
     */
    public PathNode home;
    /**
     * the home of the agv
     */
    public Buffer homeBuffer;
    /**
     * Name of the AGV
     */
    public String name;

    /**
     * Constructor
     *
     * @param home
     * @param homeBuffer
     * @param name
     */
    public AGV(PathNode home, Buffer homeBuffer, String name) {
        this.home = home;
        this.name = name;
        this.homeBuffer = homeBuffer;
    }

    AGV(PathNode upperNode, Buffer b) {
        this(upperNode, b, "AGV" + String.format("%03d", id++));
    }

    /**
     * Command the AGV to move to crane
     *
     * @param c
     * @param destination
     */
    public void moveToCrane(Crane destination, Controller c) {
        /*  PathFinder finder = new PathFinder();
         List<PathNode> path = finder.getShortestPath(home, destination.pathNode);
         Message moveMessage = new Message(Commands.MOVE_CONTAINER, null);
         ArrayList<String> nodeIds = new ArrayList<>();
         for (PathNode node : path) {
         nodeIds.add(node.getId());
         }
         moveMessage.setParameters(new Object[]{nodeIds});*/
    }

    /**
     * Send AGV home
     *
     * @param source
     * @param c
     */
    public void moveToHome(Crane source, Controller c) {
        /*     PathFinder finder = new PathFinder();
         List<PathNode> path = finder.getShortestPath(source, home);
         Message moveMessage = new Message(Commands.MOVE_CONTAINER, null);
         ArrayList<String> nodeIds = new ArrayList<>();
         for (PathNode node : path) {
         nodeIds.add(node.getId());
         }
         moveMessage.setParameters(new Object[]{nodeIds});*/
    }

    @Override
    public String toString() {
        return "AGV{" + "name=" + name + ", isHome=" + isHome + ", container=" + container + ", home=" + home.getId() + ", homeBuffer=" + homeBuffer.id + '}';
    }
}

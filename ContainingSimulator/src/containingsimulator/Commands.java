/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

/**
 *
 * @author Hendrik
 */
public class Commands {
    /**
     *Device is ready
     */
    static public int READY = 0;
    /**
     *Container must be moved by AGV
     */
    //static public int MOVE_CONTAINER = 1; SCRAPPED!
    /**
     *Crane needs to pickup CONTAINER
     */
    static public int PICKUP_CONTAINER = 2;
    /**
     *Crane needs to put CONTAINER down
     */
    static public int PUT_CONTAINER = 3;
    /**
     *Give container to AGV
     */
    static public int GIVE_CONTAINER = 4;
    
    static public int MOVE_TRANSPORTER = 5;
    
    static public int MOVE = 6;
}

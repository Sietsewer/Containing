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
     *Move AGV
     */
    static public int MOVE = 1;
    
    /**
     *Crane needs to pickup CONTAINER from buffer or transport
     */
    static public int PICKUP_CONTAINER = 2;
    
    /**
     *Give container to AGV
     */
    static public int GIVE_CONTAINER = 3;
    
    /**
     *Crane needs to put CONTAINER down on buffer or transport
     */
    static public int PUT_CONTAINER = 4;
    
    /**
     * Crane gets container from AGV
     */
    static public int GET_CONTAINER = 5;
    
    /**
     * Simulator creates a new transport
     */
    static public int CREATE_TRANSPORTER = 6;
    
    /**
     * Remove transporter from simulation
     */
    static public int REMOVE_TRANSPORTER = 7;
}
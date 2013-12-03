/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

/**
 *
 * @author Hendrik
 */
public class Commands {

    /**
     * Device is ready
     */
    final static public int READY = 0;
    
    final static public int SHUTDOWN = 9;
    /**
     * Container must be moved by AVG
     */
    static public int MOVE = 1;
    /**
     * Crane needs to pickup CONTAINER
     */
    static public int PICKUP_CONTAINER = 2;
    /**
     * Crane needds to give CONTAINER to AGV
     */
    static public int GIVE_CONTAINER = 3;
    /**
     * Create transporter with containers
     */
    static public int PUT_CONTAINER = 4;
    /**
     * Remove transporter
     */
    static public int GET_CONTAINER = 5;
    /**
     * Container must be moved by AVG
     */
    static public int CREATE_TRANSPORTER = 6;
    
    static public int REMOVE_TRANSPORTER = 7;
}

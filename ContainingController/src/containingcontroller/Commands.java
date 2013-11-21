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
     *Device is ready
     */
    final static public int READY = 0;
    /**
     *Container must be moved by AVG
     */
    static public int MOVE_CONTAINER = 1;
    /**
     *Crane needs to pickup CONTAINER
     */
    static public int PICKUP_CONTAINER = 2;
    /**
     *Crane needds to put CONTAINER down
     */
    static public int PUT_CONTAINER = 3;
    /**
     * Create transporter with containers
     */
    static public int CREATE_TRANSPORTER = 4;
    /**
     * Remove transporter
     */
    static public int REMOVE_TRANSPORTER = 5;
}

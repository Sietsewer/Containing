/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

/**
 *
 * @author Hendrik
 */
public class TransportTypes {

    /**
     * SEASHIP identifier
     */
    static public int SEASHIP = 0;
    /**
     * BARGE identifier
     */
    static public int BARGE = 1;
    /**
     * TROLLEY identifier
     */
    static public int TROLLEY = 2;
    /**
     * TRAIN identifier
     */
    static public int TRAIN = 3;

    /**
     * Get transporttype by type text
     *
     * @param type expects vrachtauto, trein, zeeship or binnenschip
     * @return returns 0, if non of the above is entered
     */
    public static int getTransportType(String type) {
        if (type.toLowerCase().contains("vrachtauto")) {
            return TROLLEY;
        } else if (type.toLowerCase().contains("trein")) {
            return TRAIN;
        } else if (type.toLowerCase().contains("zeeschip")) {
            return SEASHIP;
        } else if (type.toLowerCase().contains("binnenschip")) {
            return BARGE;
        }
        return 0;

    }

    /**
     * Get the text for a type
     *
     * @param type number between 0 and 3
     * @return
     */
    public static String getTransportType(int type) {
        if (type == TROLLEY) {
            return "vrachtauto";
        } else if (type == TRAIN) {
            return "trein";
        } else if (type == SEASHIP) {
            return "zeeschip";
        } else if (type == BARGE) {
            return "binnenschip";
        }
        return "";

    }
}

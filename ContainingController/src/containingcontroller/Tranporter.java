/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hendrik
 */
class Transporter {

    private List<Container> containers;
    private int transportType;
    String id;
    static int tranporterID =0;
    public Transporter(int transportType) {
        this.transportType = transportType;
        this.id = "TRS"+ tranporterID++;
        
    }

    public int getTransportType() {
        return transportType;
    }

    public Vector3f getFreeLocation() {
        if (transportType == TransportTypes.SEASHIP) {
            for (int z = 0; z < Integer.MAX_VALUE; z++) {
                for (int x = 0; x < 20; x++) {
                    for (int y = 0; y < 8; y++) {
                        Vector3f currentposition = new Vector3f(x, y, z);
                        boolean posistionTaken = false;
                        for (Container c : containers) {
                            if (c.getPosition().x == currentposition.x
                                    && c.getPosition().y == currentposition.y
                                    && c.getPosition().z == currentposition.z) {
                                posistionTaken = true;
                                break;
                            }
                        }
                        if (!posistionTaken) {
                            return currentposition;
                        }
                    }
                }
            }
        } else if (transportType == TransportTypes.BARGE) {
            for (int z = 0; z < Integer.MAX_VALUE; z++) {
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) {
                        Vector3f currentposition = new Vector3f(x, y, z);
                        boolean posistionTaken = false;
                        for (Container c : containers) {
                            if (c.getPosition().x == currentposition.x
                                    && c.getPosition().y == currentposition.y
                                    && c.getPosition().z == currentposition.z) {
                                posistionTaken = true;
                                break;
                            }
                        }
                        if (!posistionTaken) {
                            return currentposition;
                        }
                    }
                }
            }

        } else if (transportType == TransportTypes.TRAIN) {
            for (int z = 0; z < Integer.MAX_VALUE; z++) {
                for (Container c : containers) {
                    if (c.getPosition().z != z) {
                        return new Vector3f(0, 0, z);
                    }
                }
            }
        } else if (transportType == TransportTypes.TROLLEY) {
            if (containers.size() > 0) {
                return null;
            } else {
                return new Vector3f(0, 0, 0);
            }
        } else {
            return null;
        }
        return null;
    }

    public void loadContainer(Container container) {
        if (containers == null) {
            containers = new ArrayList<>();
        }

        boolean posistionTaken = false;
        for (Container c : containers) {
            if (c.getPosition().x == container.getPosition().x
                    && c.getPosition().y == container.getPosition().y
                    && c.getPosition().z == container.getPosition().z) {
                posistionTaken = true;
                break;
            }
        }
        if (!posistionTaken) {
            if (container.getDateArrival().before(container.getDateDeparture())) {
                containers.add(container);
            }
        }
    }

    public int getContainerCount() {
        if (containers != null) {
            return containers.size();
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        String s = "Transporter{ ID="+id + ",containers={";

        for (Container c : containers) {
            s += c.toString() + ",";
        }
        s = s.substring(0, s.length() - 1);
        s += "}, transportType=" + TransportTypes.getTransportType(transportType) + '}';
        return s;
    }
}

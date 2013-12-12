/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import containing.xml.CustomVector3f;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Hendrik
 */
class Transporter {

    private List<Container> containers;
    private int transportType;
    String id;
    static int tranporterID = 0;
    private Crane dockingPoint;
    private Date dateArrival;

    public Transporter(int transportType, Date dateArrival) {
        this.transportType = transportType;
        this.id = "TRS" + String.format("%03d", tranporterID++);
        this.dateArrival = dateArrival;
    }

    public int getTransportType() {
        return transportType;
    }

    public CustomVector3f getFreeLocation() {
       /* if (transportType == TransportTypes.SEASHIP) {
            for (int z = 0; z < Integer.MAX_VALUE; z++) {
                for (int x = 0; x < 20; x++) {
                    for (int y = 0; y < 8; y++) {
                        CustomVector3f currentposition = new CustomVector3f(x, y, z);
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
                        CustomVector3f currentposition = new CustomVector3f(x, y, z);
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
                        return new CustomVector3f(0, 0, z);
                    }
                }
            }
        } else if (transportType == TransportTypes.LORREY) {
            if (containers.size() > 0) {
                return null;
            } else {
                return new CustomVector3f(0, 0, 0);
            }
        } else {
            return null;
        }*/
        return null;
    }

    public void loadContainer(Container container) {
        if (containers == null) {
            containers = new ArrayList<Container>();
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
        String s = "Transporter{ ID=" + id + ",containers={";

        if (containers != null) {
            for (Container c : containers) {
                s += c.toString() + ",";
            }
        }
        s = s.substring(0, s.length() - 1);
        s += "}, transportType=" + TransportTypes.getTransportType(transportType) + '}';
        return s;
    }

    /**
     * Get containers from transporter
     *
     * @return
     */
    public List<Container> getContainers() {
        return containers;
    }

    /**
     * Get specific container from transporter
     *
     * @param i
     * @return
     */
    public Container getContainer(int i) {
        if (containers != null && i < containers.size() && i >= 0) {
            return containers.get(i);
        } else {
            return null;
        }
    }

    public Crane getDockingPoint() {
        return dockingPoint;
    }

    public void setDockingPoint(Crane dockingPoint) {
        this.dockingPoint = dockingPoint;
    }

    int getLenghtTransporter() {
        int maxX = 0;
        for (Container c : containers) {
            if (c.getPosition().x > maxX) {
                maxX = (int) c.getPosition().x;
            }
        }
        return maxX;
    }

    public Date getDateArrival() {
        return this.dateArrival;
    }
}

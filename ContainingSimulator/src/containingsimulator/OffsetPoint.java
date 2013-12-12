/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.math.Vector3f;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sietse
 */
public class OffsetPoint {

    Map<String, OffsetWay> wayMap;

    public OffsetPoint() {
        wayMap = new HashMap();
    }

    public void addWay(String nextID, String lastID, Vector3f slowLane, Vector3f fastLane) {
        wayMap.put(((nextID == null) ? "NONE" : nextID) + ";" + ((lastID == null) ? "NONE" : lastID), new OffsetWay(slowLane, fastLane));
    }

    public void addWay(String nextID, String lastID, Vector3f lane) {
        addWay(nextID, lastID, lane, lane);
    }

    public Vector3f getPoint(String nextPoint, String lastPoint, int lane) {
        if (wayMap.containsKey(nextPoint + ";" + lastPoint)) {
            return wayMap.get(nextPoint + ";" + lastPoint).getPoint(lane);
        } else if (wayMap.containsKey("NONE" + ";" + lastPoint)) {
            return wayMap.get("NONE" + ";" + lastPoint).getPoint(lane);
        } else if(wayMap.containsKey(nextPoint + ";" + "NONE")) {
            return wayMap.get(nextPoint + ";" + "NONE").getPoint(lane);
        }
        return Vector3f.ZERO;
    }

    private class OffsetWay {

        private Vector3f fastLane;
        private Vector3f slowLane;

        public OffsetWay(Vector3f slowLane, Vector3f fastLane) {
            this.slowLane = slowLane;
            this.fastLane = fastLane;
        }

        public Vector3f getPoint(int lane) {
            if (lane == 1) {
                return slowLane;
            }
            return fastLane;
        }
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author Sietse
 */
public class OffsetRoute {

    static float modulo(float a, float b) {
        return (((-a % b) + b) % b);
    }

    static float getAngle(Vector3f start, Vector3f end) {
        return modulo(FastMath.atan2(end.z - start.z, end.x - start.x), (FastMath.PI * 2));
    }

    static ArrayList<Vector3f> applyOffset(String[] ids, boolean fastLane) {
        float eightRad = (FastMath.PI / 4);
        float fourthRad = (FastMath.PI / 2);
        int lane = fastLane ? 2 : 1;
        ArrayList<Vector3f> returnList = new ArrayList();
        ArrayList<Vector3f> points = new ArrayList();
        for (int i = 0; i < ids.length; i++) {
            points.add(Path.getVector(ids[i]));
        }
        for (int i = 0; i < points.size() - 1; i++) {
            float angle = getAngle(points.get(i), points.get(i + 1));
            returnList.add(getVectorAtLane(ids[i], ids[i + 1], angle, lane));
        }
        returnList.add(getVectorAtLane(ids[ids.length - 1], "", getAngle(points.get(points.size() - 2), points.get(points.size() - 1)), lane));
        return returnList;
    }

    public static Vector3f getVectorAtLane(String id, String idNext, float direction, int lane) {
        Vector3f returnVector = new Vector3f();
        returnVector = Path.getVector(id).clone();
        if (id.charAt(0) == 'b') {
            if ((direction > (FastMath.PI * .5f)) && (direction < (FastMath.PI * 1.5f))) {
                returnVector.setZ(returnVector.z - (lane == 1 ? 3f : 9f));
            } else {
                returnVector.setZ(returnVector.z + 3f * lane);
            }
        } else if (id.charAt(0) == 'c') {
            if (id.charAt(1) == 's') {
                if (direction > FastMath.PI) {
                    returnVector.setX(returnVector.x - 3f);
                } else {
                    returnVector.setX(returnVector.x + 3f);
                }
            } else if (id.charAt(1) == 'b') {
                if ((direction > (FastMath.PI * .5f)) && (direction < (FastMath.PI * 1.5f))) {
                    returnVector.setZ(returnVector.z - 3f);
                } else {
                    returnVector.setZ(returnVector.z + 3f);
                }
            }
        } else if (id.charAt(0) == 'm') {
            if (id.length() == 2) {
                switch (id.charAt(1)) {
                    case ('1'):
                        break;
                    case ('2'):
                        break;
                    case ('3'):
                        break;
                    case ('4'):
                        break;
                    case ('5'):
                        break;
                    case ('6'):
                        break;
                    case ('7'):
                        break;
                    case ('8'):
                        break;
                    case ('9'):
                        break;
                }
            } else {
                switch (id.charAt(2)) {
                    case ('0'):
                        break;
                    case ('1'):
                        break;
                    case ('2'):
                        if (direction == FastMath.PI * .5) {
                            returnVector = new Vector3f(120.5f, 10f, 50f);
                        } else if ((direction > (FastMath.PI * 1.25)) || (direction < (FastMath.PI * 0.25))) {
                            if (lane == 2) {//98.5, 63.5
                                returnVector = new Vector3f(127.5f, 10f, 69.5f);
                            } else {//104.5, 69.5
                                returnVector = new Vector3f(127.5f, 10f, 63.5f);
                            }
                        } else {
                            if (lane == 2) {//92.5, 57.5
                                returnVector = new Vector3f(127.5f, 10f, 51.5f);
                            } else {//86.5, 51.5
                                returnVector = new Vector3f(127.5f, 10f, 57.5f);
                            }
                        }
                        break;
                    case ('3'):
                        if (idNext.equalsIgnoreCase("m14") || idNext.equalsIgnoreCase("m16")) {
                            if (lane == 2) {//92.5, 57.5
                                returnVector = new Vector3f(86.5f, 10f, 51.5f);
                            } else {//86.5, 51.5
                                returnVector = new Vector3f(92.5f, 10f, 57.5f);
                            }
                        } else {
                            if (lane == 2) {//98.5, 63.5
                                returnVector = new Vector3f(104.5f, 10f, 69.5f);
                            } else {//104.5, 69.5
                                returnVector = new Vector3f(98.5f, 10f, 63.5f);
                            }
                        }
                        break;
                    case ('4'):
                        if (direction == FastMath.PI * 1.5f) {
                            returnVector = new Vector3f(61.5f, 10f, 51.5f);
                        } else {
                            returnVector = new Vector3f(68.5f, 10f, 58.5f);
                        }
                        break;
                    case ('5'):
                        break;
                    case ('6'):
                        break;
                }
            }
        }
        return returnVector;
    }
}
/*
      map.put("m1", new Vector3f(95.5f,10,503.5f));
      map.put("m2",new Vector3f(90,10,536));
      map.put("m3",new Vector3f(765,10,536));
      map.put("m4",new Vector3f(765,10,503.5f));
      map.put("m5",new Vector3f(846,10,503.5f));
      map.put("m6",new Vector3f(1064,10,503.5f));
      map.put("m7",new Vector3f(1526.5f,10,503.5f));
      map.put("m8",new Vector3f(1525,10,60.5f));
      map.put("m9",new Vector3f(1488,10,60.5f));
      map.put("m10",new Vector3f(1488,10,30));
      map.put("m11",new Vector3f(127.5f,10,30.5f));
      map.put("m12",new Vector3f(127.5f,10,60.5f));
      map.put("m13",new Vector3f(95.5f,10,60.5f));
      map.put("m14",new Vector3f(65,10,55));
      map.put("m15",new Vector3f(65,10,475));
      map.put("m16",new Vector3f(95.5f,10,472));
 */
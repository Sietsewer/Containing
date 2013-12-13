/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sietse
 */
public class OffsetRoute {

    private static Map<String, OffsetPoint> offsetPoints = new HashMap();

    public static void init() {
        OffsetPoint temp;
        temp = buildWay("m1");
        
        temp.addWay("m16", "m2", new Vector3f(3f,0f,-3f),new Vector3f(9f,0f,-9f));
        temp.addWay("m16", "bfb001", new Vector3f(3f,0f,-3f),new Vector3f(9f,0f,-9f));
        temp.addWay("m2", "m16", new Vector3f(-3f,0f,3f),new Vector3f(-9f,0f,9f));
        temp.addWay("m2", "bfb001", new Vector3f(3f,0f,-3f),new Vector3f(9f,0f,-9f));
        temp.addWay("bfb001", "m2", new Vector3f(-3f,0f,3f),new Vector3f(-9f,0f,9f));
        temp.addWay("bfb001", "m16", new Vector3f(-3f,0f,3f),new Vector3f(-9f,0f,9f));
        
        
        temp = buildWay("m2");
        
        temp.addWay("cba001", "m1", new Vector3f(-3.5f,0f,3.5f));
        temp.addWay("m1", "cba001", new Vector3f(3.5f,0f,-3.5f));
        
        
        temp = buildWay("m3");
        
        temp.addWay("cba008", "m4", new Vector3f(3.5f,0f,-3.5f));
        temp.addWay("m4", "cba008", new Vector3f(3.5f,0f,3.5f));
        
        
        temp = buildWay("m4");
        
        temp.addWay("m3", "bfb031", new Vector3f(-4f,0f,-3f), new Vector3f(-4f,0f,-9f));
        temp.addWay("m3", "bfb030", new Vector3f(-4f,0f,3f), new Vector3f(-4f,0f,9f));
        temp.addWay("bfb031", "m3", new Vector3f(4f,0f,-3f), new Vector3f(4f,0f,-9f));
        temp.addWay("bfb031", "bfb030", new Vector3f(-4f,0f,3f), new Vector3f(-4f,0f,9f));
        temp.addWay("bfb030", "m3", new Vector3f(4f,0f,3f), new Vector3f(4f,0f,9f));
        temp.addWay("bfb030", "bfb031", new Vector3f(-4f,0f,-3f), new Vector3f(-4f,0f,-9f));
        
        
        temp = buildWay("m5");
        
        temp.addWay("bfb034", "clo001", new Vector3f(0f,0f,-3f), new Vector3f(0f,0f,-9f));
        temp.addWay("bfb034", "bfb035", new Vector3f(0f,0f,-3f), new Vector3f(0f,0f,-9f));
        temp.addWay("bfb035", "clo001", new Vector3f(0f,0f,3f), new Vector3f(0f,0f,9f));
        temp.addWay("bfb035", "bfb034", new Vector3f(0f,0f,3f), new Vector3f(0f,0f,9f));
        temp.addWay("clo001", "bfb035", new Vector3f(0f,0f,-3f), new Vector3f(0f,0f,-9f));
        temp.addWay("clo001", "bfb034", new Vector3f(0f,0f,3f), new Vector3f(0f,0f,9f));
        
        
        temp = buildWay("m6");
        
        //NOT IMPLEMENTED
        
        
        temp = buildWay("m7");
        
        temp.addWay("m8", "bfb063", new Vector3f(3f,0f,3f), new Vector3f(9f,0f,9f));
        temp.addWay("bfb063", "m8", new Vector3f(-3f,0f,-3f), new Vector3f(-9f,0f,-9f));
        
       
        temp = buildWay("m8");
        
        temp.addWay("m9", "m7", new Vector3f(3f,0f,-3f), new Vector3f(9f,0f,-9f));
        temp.addWay("m7", "m9", new Vector3f(-3f,0f,3f), new Vector3f(-9f,0f,9f));
        
        
        temp = buildWay("m9");
        
        temp.addWay("m10", "bfa063", new Vector3f(4f,0f,-3f), new Vector3f(4f,0f,-9f));
        temp.addWay("m10", "m8", new Vector3f(4f,0f,3f), new Vector3f(4f,0f,9f));
        temp.addWay("bfa063", "m10", new Vector3f(-4f,0f,-3f), new Vector3f(-4f,0f,-9f));
        temp.addWay("bfa063", "m8", new Vector3f(-4f,0f,-3f), new Vector3f(4f,0f,-9f));
        temp.addWay("m8", "m10", new Vector3f(-4f,0f,3f), new Vector3f(-4f,0f,9f));
        temp.addWay("m8", "bfa063", new Vector3f(4f,0f,3f), new Vector3f(4f,0f,9f));
        
        
        temp = buildWay("m10");
        
        temp.addWay("crt001", "m9", new Vector3f(3f,0f,-3f));
        temp.addWay("m9", "crt001", new Vector3f(3f,0f,-3f));
        
        
        temp = buildWay("m11");
        
        temp.addWay("m12", "ctr001", new Vector3f(-3.5f,0f,-3.5f));
        temp.addWay("ctr001", "m12", new Vector3f(3.5f,0f,3.5f));
        
        
        temp = buildWay("m12");
        
        temp.addWay("m11", "bfa001", new Vector3f(4f,0f,-3f), new Vector3f(4f,0f,-9f));
        temp.addWay("m11", "bfa002", new Vector3f(4f,0f,3f), new Vector3f(4f,0f,9f));
        temp.addWay("bfa001", "m11", new Vector3f(-4f,0f,-3f), new Vector3f(-4f,0f,-9f));
        temp.addWay("bfa001", "bfa002", new Vector3f(-4f,0f,-3f), new Vector3f(4f,0f,-9f));
        temp.addWay("bfa002", "m11", new Vector3f(-4f,0f,3f), new Vector3f(-4f,0f,9f));
        temp.addWay("bfa002", "bfa001", new Vector3f(4f,0f,3f), new Vector3f(4f,0f,9f));
        
        
        temp = buildWay("m13");
        
        temp.addWay("m16", "bfa001", new Vector3f(-3f,0f,-3f),new Vector3f(-9f,0f,-9f));
        temp.addWay("m14", "bfa001", new Vector3f(-3f,0f,-3f),new Vector3f(-9f,0f,-9f));
        temp.addWay("bfa001", "m16", new Vector3f(3f,0f,3f),new Vector3f(9f,0f,9f));
        temp.addWay("m14", "m16", new Vector3f(3f,0f,3f),new Vector3f(9f,0f,9f));
        temp.addWay("bfa001", "m14", new Vector3f(3f,0f,3f),new Vector3f(9f,0f,9f));
        temp.addWay("m16", "m14", new Vector3f(3f,0f,3f),new Vector3f(9f,0f,9f));
        
        
        temp = buildWay("m14");
        
        temp.addWay("cse001", "m13", new Vector3f(-3.5f,0f,-3.5f));
        temp.addWay("m13", "cse001", new Vector3f(3.5f,0f,3.5f));
        
        
        temp = buildWay("m15");
        
        temp.addWay("m16", "cse010", new Vector3f(-3.5f,0f,3.5f));
        temp.addWay("cse010", "m16", new Vector3f(3.5f,0f,-3.5f));
        
        
        temp = buildWay("m16");

        temp.addWay("m15", "m1", new Vector3f(-3f,0f,-4f), new Vector3f(-9f,0f,-4f));
        temp.addWay("m15", "m13", new Vector3f(3f,0f,-4f), new Vector3f(9f,0f,-4f));
        temp.addWay("m13", "m1", new Vector3f(-3f,0f,4f), new Vector3f(-9f,0f,4f));
        temp.addWay("m13", "m15", new Vector3f(-3f,0f,4f), new Vector3f(-9f,0f,4f));
        temp.addWay("m1", "m13", new Vector3f(3f,0f,-4f), new Vector3f(9f,0f,-4f));
        temp.addWay("m1", "m15", new Vector3f(3f,0f,4f), new Vector3f(9f,0f,4f));

        
    }

    private static OffsetPoint buildWay(String id) {
        offsetPoints.put(id, new OffsetPoint());
        return offsetPoints.get(id);
    }

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
        float angle = 0f;
        for (int i = 0; i < points.size(); i++) {

            if (i <= points.size() - 2) {
                angle = getAngle(points.get(i), points.get(i + 1));
            }
            returnList.add(getVectorAtLane(ids[i], i < ids.length - 1 ? ids[i + 1] : "", i > 0 ? ids[i - 1] : "", angle, lane));
        }
        return returnList;
    }

    public static Vector3f getVectorAtLane(String id, String idNext, String idLast, float direction, int lane) {
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
            } else if (id.charAt(1) == 't') {
                if ((direction > (FastMath.PI * .5f)) && (direction < (FastMath.PI * 1.5f))) {
                    returnVector.setZ(returnVector.z - 3f);
                } else {
                    returnVector.setZ(returnVector.z + 3f);
                }
            } else if (id.charAt(1) == 'l') {
                if ((direction > (FastMath.PI * .5f)) && (direction < (FastMath.PI * 1.5f))) {
                    returnVector.setZ(returnVector.z - 3f);
                } else {
                    returnVector.setZ(returnVector.z + 3f);
                }
            }
        } else if (id.charAt(0) == 'm') {
            returnVector = returnVector.add(offsetPoints.get(id).getPoint(idNext, idLast, lane));
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
      * 
      * 
      * -4, 3 | 3, -4
 */
/*
             if (id.length() == 2) {
             switch (id.charAt(1)) {
             case ('1'):
             if (idNext.equalsIgnoreCase("m16") || idNext.equalsIgnoreCase("m2")) {
             if (lane == 2) {
             returnVector = new Vector3f(86.5f, 10f, 512.5f);
             } else {
             returnVector = new Vector3f(92.5f, 10f, 506.5f);
             }
             } else {
             if (lane == 2) {
             returnVector = new Vector3f(104.5f, 10f, 494.5f);
             } else {
             returnVector = new Vector3f(98.5f, 10f, 500.5f);
             }
             }
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
             if (idLast.equalsIgnoreCase("bfa001")) {
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
             if (idNext.equalsIgnoreCase("m16")) {
             returnVector = returnVector.add(-4f,0f,3f);
             } else {
             returnVector = returnVector.add(3f,0f,-4f);
             }
             break;
             case ('6'):
             if (idNext.equalsIgnoreCase("m13")) {
             if (lane == 2) {
             returnVector = returnVector.add(4f,0f,9f);
             } else {
             returnVector = returnVector.add(4f,0f,3f);
             }
             } else if (idNext.equalsIgnoreCase("m1")) {
             if (lane == 2) {
             returnVector = returnVector.add(4f,0f,9f);
             } else {
             returnVector = returnVector.add(4f,0f,3f);
             }
             } else {
             if (idLast.equalsIgnoreCase("m1")) {
             if (lane == 2) {
             returnVector = returnVector.add(-4f,0f,3f);
             } else {
             returnVector = returnVector.add(-4f,0f,9f);
             }
             } else {
             if (lane == 2) {
             returnVector = returnVector.add(-4f,0f,-3f);
             } else {
             returnVector = returnVector.add(-4f,0f,-9f);
             }
             }
             }
             break;
             }
             }*/
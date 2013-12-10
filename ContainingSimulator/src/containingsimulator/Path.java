/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.math.Vector3f;
import java.util.HashMap;

/**
 *
 * @author User
 */
public class Path {
    
    private static HashMap<String, Vector3f> map = new HashMap();
    private static String clo = "clo";
    private static String cse = "cse";
    private static String cba = "cba";
    private static String ctr = "ctr";
    private static String bfa = "bfa";
    private static String bfb = "bfb";
    private static String m = "m";

    /**
     * Constructor creates path
     */
    public Path() {
    }
    
    public static String getMainPathID() {
        return m;
    }
    
    public static String getLorryID() {
        return clo;
    }
    
    public static String getSeaID() {
        return cse;
    }
    
    public static String getBargeID() {
        return cba;
    }
    
    public static String getBufferAID() {
        return bfa;
    }
    
    public static String getBufferBID() {
        return bfb;
    }
    
    public static String getTrainID() {
        return ctr;
    }
    
    public static void createPath() {
        createM();
        createBF();
        createCSE();
        createCTR();
        createCBA();
        createCLO();
    }
    public static HashMap<String,Vector3f>getPath()
    {

         return map;
        
    }
    /**
     * returns vector3f for id
     * @param id
     * @return
     */
    public static Vector3f getVector(String id)
  {
        return map.get(id);
    }
        
  private static void createM()
  {
      map.put("m1", new Vector3f(95.5f,10,505.5f));
      map.put("m2",new Vector3f(90,10,536));
      map.put("m3",new Vector3f(765,10,536));
      map.put("m4",new Vector3f(765,10,505.5f));
      map.put("m5",new Vector3f(846,10,505.5f));
      map.put("m6",new Vector3f(1064,10,505.5f));
      map.put("m7",new Vector3f(1527.5f,10,505.5f));
      map.put("m8",new Vector3f(1527.5f,10,60.5f));
      map.put("m9",new Vector3f(1488,10,60.5f));
      map.put("m10",new Vector3f(1488,10,30));
      map.put("m11",new Vector3f(123.5f,10,30.5f));
      map.put("m12",new Vector3f(123.5f,10,60.5f));
      map.put("m13",new Vector3f(95.5f,10,60.5f));
      map.put("m14",new Vector3f(65,10,55));
      map.put("m15",new Vector3f(65,10,478));
      map.put("m16",new Vector3f(95.5f,10,478));
    }
  public static void updatePath(String id, Vector3f vec)
  { 
      if(map.containsKey(id)){
          
      map.remove(id);
      map.put(id, vec);
      }
  }
    
    private static void createBF() {
        int dist = 22;
        Vector3f vecBFA = new Vector3f(118, 10, 60.5f);
        Vector3f vecBFB = new Vector3f(118, 10, 505.5f);
        
        String bfa = "bfa";
        String bfb = "bfb";
        map.put(bfa + String.format("%03d", 1), vecBFA);
        map.put(bfb + String.format("%03d", 1), vecBFB);
        
        for (int i = 2; i <= 63; i++) {
            vecBFA = vecBFA.add(dist, 0, 0);
            vecBFB = vecBFB.add(dist, 0, 0);
            map.put(bfa + String.format("%03d", i), vecBFA);
            map.put(bfb + String.format("%03d", i), vecBFB);
        }
        
    }
    
    private static void createCSE() {
        int dist = 40;
        Vector3f vecCSE = new Vector3f(65, 10, 85);
        String cse = "cse";
        map.put(cse + String.format("%03d", 1), vecCSE);
        for (int i = 2; i <= 10; i++) {
            vecCSE = vecCSE.add(new Vector3f(0, 0, dist));
            map.put(cse + String.format("%03d", i), vecCSE);
        }
    }
    
    private static void createCTR() {
        int dist = 22 * 18; //was 22*18
        Vector3f vecCTR = new Vector3f(167, 10, 30); //x was 167
        String ctr = "ctr";
        map.put(ctr + String.format("%03d", 1), vecCTR);
        for (int i = 2; i <= 4; i++) {
            vecCTR = vecCTR.add(new Vector3f(dist, 0, 0));
            map.put(ctr + String.format("%03d", i), vecCTR);
        }
    }
    
    private static void createCBA() {
        int dist = 66;
        Vector3f vecCBA = new Vector3f(222, 10, 537);
        map.put(cba + String.format("%03d", 1), vecCBA);
        for (int i = 2; i <= 8; i++) {
            vecCBA = vecCBA.add(new Vector3f(dist, 0, 0));
            map.put(cba + String.format("%03d", i), vecCBA);
        }
    }
    
    private static void createCLO() {
        int dist = 11;
        Vector3f vecCLO = new Vector3f(850.5f, 10, 520);//z was 560
        String clo = "clo";
        map.put(clo + String.format("%03d", 1), vecCLO);
        for (int i = 2; i <= 20; i++) {
            vecCLO = vecCLO.add(new Vector3f(dist, 0, 0));
            map.put(clo + String.format("%03d", i), vecCLO);
        }
    }
}

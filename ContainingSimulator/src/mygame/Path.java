/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import java.util.HashMap;

/**
 *
 * @author User
 */
public class Path {
    
  private HashMap<String,Vector3f>map = new HashMap();
     
    /**
     *Constructor creates path
     */
    public Path()
  {
      createPath();
  }

    private void createPath()
  {
      createM();
      createBF();
      createCSE();
      createCTR();
      createCBA();
      createCLO();
  }
    /**
     * returns vector3f for id
     * @param id
     * @return
     */
    public Vector3f getVector(String id)
  {
      return map.get(id);
  }
  
  private void createM()
  {
      map.put("m1", new Vector3f(96,10,503));
      map.put("m2",new Vector3f(90,10,534));
      map.put("m3",new Vector3f(765,10,534));
      map.put("m4",new Vector3f(765,10,504));
      map.put("m5",new Vector3f(846,10,504));
      map.put("m6",new Vector3f(1064,10,504));
      map.put("m7",new Vector3f(1525,10,504));
      map.put("m8",new Vector3f(1525,10,60));
      map.put("m9",new Vector3f(1489,10,60));
      map.put("m10",new Vector3f(1489,10,30));
      map.put("m11",new Vector3f(123,10,30));
      map.put("m12",new Vector3f(123,10,60));
      map.put("m13",new Vector3f(97,10,60));
      map.put("m14",new Vector3f(65,10,56));
      map.put("m15",new Vector3f(65,10,476));
      map.put("m16",new Vector3f(94,10,477));
  }
  
  
  private void createBF()
  {
      int dist = 22;
      Vector3f vecBFA = new Vector3f(12,10,71);
      Vector3f vecBFB= new Vector3f(12,10,495);
      String bfa = "bfa";
      String bfb = "bfb";
      for(int i = 1;i<=63;i++)
      {
          vecBFA = vecBFA.add(dist,0,0);
          vecBFB = vecBFB.add(dist,0,0);
          map.put(bfa+i,vecBFA);
          map.put(bfb+i,vecBFB);
      }
      
  }
  private void createCSE()
  {
      int dist = 55;
             Vector3f vecCSE = new Vector3f(65,10,111);
             String cse = "cse";
             
             for(int i =1;i<=10;i++)
             {
              vecCSE = vecCSE.add(new Vector3f(0,0,dist));
              map.put(cse+i,vecCSE);
             }
  }
  private void createCTR()
  {
           int dist = 11*18;
             Vector3f vecCTR = new Vector3f(167,10,30);
             String ctr = "ctr";
             for(int i =1; i <=4;i++)
             {
                 vecCTR = vecCTR.add(new Vector3f(dist,0,0));
                 map.put(ctr+i, vecCTR);
             }
  }
  
  private void createCBA()
  {
      int dist = 66;
             Vector3f vecCBA = new Vector3f(222,10,534);
             String cba = "cba";
             for(int i=1;i<=8;i++)
             {
                 vecCBA.add(new Vector3f(dist,0,0));
                 map.put(cba+i, vecCBA);
             }
  }
  
  private void createCLO()
  {
      int dist = 11;
             Vector3f vecCLO = new Vector3f(846,10,526);
             String clo = "clo";
             for(int i=1;i<=20;i++)
             {
                 vecCLO.add(new Vector3f(dist,0,0));
                 map.put(clo+i, vecCLO);
             }
  }    
}

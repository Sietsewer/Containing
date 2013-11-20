/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class PathFinder {
    
    List<PathNode> map = new ArrayList();
    
    public PathFinder()
    {
        
    }
    
    public List<PathNode> getShortestPath(String srce, String dest) throws Exception
    {
       List<PathNode>alreadyChecked = new ArrayList();
       List<PathNode>shortestRoute = new ArrayList();
       List<PathNode>unSettledNodes = new ArrayList();
       
       PathNode srceNode = null;
       PathNode destNode = null;
       
       for(int i =0; i < map.size();i++)
       {
           PathNode curNode = map.get(i);
           if(curNode.getId().equals(srce))
           {
            srceNode = curNode;
           }
           else if (curNode.getId().equals(dest))
           {
            destNode = curNode;   
           }
       }
       
       if(srceNode !=null && destNode != null)
       {
       srceNode.setCost(0, null); //start cost}
       unSettledNodes.add(srceNode);
       
       while(unSettledNodes.size()>0)
       {
           PathNode node =  getMinimum(unSettledNodes);
           alreadyChecked.add(node);
           unSettledNodes.remove(node);
           
          for(PathNode neighbour : node.getNeighbours())
          {
              if(alreadyChecked.contains(neighbour))
              {
                  continue;
              }
              if(neighbour.getCost()==-1||neighbour.getCost() > (node.getCost() + node.getCostNeighb(neighbour)))
              {
                  neighbour.setCost((node.getCost() + node.getCostNeighb(neighbour)),node);
              }
              unSettledNodes.add(neighbour);
       }
      }     
       shortestRoute.add(destNode);
       destNode.getRoute(shortestRoute);

       return shortestRoute;
    }
       else
       {
           throw new Exception("source and destination are not connected!");
       }
       
    }
    
     public PathNode getMinimum(List<PathNode> neighbours)
     {
     PathNode minnode = null;
     float mindistance = 9999f;
      for(PathNode node : neighbours)
      {
          if(node.getCost()<  mindistance)
          {
              mindistance = node.getCost();
              minnode = node;
          }
      }
      return minnode;
     }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author User
 */
public class PathNode {
    
    private String id;
    private float cost = -1f;
    private PathNode parent;
    private List<PathNode>neighbours;
    
    private HashMap<PathNode,Float>neighb; //neighbor with cost to this node
    
    public PathNode(String id)
    {
        this.id = id;
        neighbours = new ArrayList();
    }
    
    public String getParentId()
    {
        if(parent!= null)
        {
            return parent.id;
        }
        return " ";
    }
    public String getId()
    {
        return this.id;
    }
    
    public void setCost(float cost, PathNode parent)
    {
        this.cost = cost; 
        this.parent = parent;
    }
    
    public void getRoute(List<PathNode>route)
    {
       if(parent!=null)
       {    
           route.add(parent);
           parent.getRoute(route);
       }
    }
    public float getCostNeighb(PathNode neighbour)
    {
        if(neighb.size()>0)
        {
        return neighb.get(neighbour);
        }
        else
        {
            return 1000;
        }
    }
    
    public float getCost()
    {
     return this.cost;   
    }
    
    public void addNeighbour(PathNode neighbour, float cost)
    {
        if(!neighbours.contains(neighbour))
        {
            neighbours.add(neighbour);
            neighb.put(neighbour, cost);
        }
    }
    public List<PathNode> getNeighbours()
    {
        return this.neighbours;
    }

    
}

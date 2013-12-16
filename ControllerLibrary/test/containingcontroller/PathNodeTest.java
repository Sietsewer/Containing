/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Wessel
 */
public class PathNodeTest {
    
    public PathNodeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getId method, of class PathNode.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        PathNode instance = new PathNode("testID");
        String expResult = "testID";
        String result = instance.getId();
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of getCost method, of class PathNode.
     */
    @Test
    public void testGetCost() {
        System.out.println("getCost");
        PathNode parent = new PathNode("testID");
        PathNode instance = new PathNode("testID2");
        instance.setCost(0.5f, parent);
        float expResult = 0.5f;
        float result = instance.getCost();
        assertEquals(expResult, result, 0.0);
        
        
    }

    /**
     * Test of getCostNeighb method, of class PathNode.
     */
    @Test
    public void testGetCostNeighb() {
        System.out.println("getCostNeighb");
        PathNode neighbour = new PathNode("neighbour");
        PathNode instance = new PathNode("testID");
        
        neighbour.setCost(10, instance);
        instance.setCost(5, neighbour);
        instance.addNeighbour(neighbour, 10);
        
        float expResult = 10;
        float result = instance.getCostNeighb(neighbour);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getNeighbours method, of class PathNode.
     */
    @Test
    public void testGetNeighbours() {
        System.out.println("getNeighbours");
        
        PathNode instance = new PathNode("testID");
        PathNode n1 = new PathNode("n1");
        PathNode n2 = new PathNode("n2");
        PathNode n3 = new PathNode("n3");
        instance.addNeighbour(n1, 1);
        instance.addNeighbour(n2, 2);
        instance.addNeighbour(n3, 3);
        
        List<PathNode> neighbours = new ArrayList();
        neighbours.add(n1);
        neighbours.add(n2);
        neighbours.add(n3);
        
        List expResult = neighbours;
        List result = instance.getNeighbours();
        assertEquals(expResult, result);
        
        
    }
}
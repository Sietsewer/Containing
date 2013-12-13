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
public class PathFinderTest {
    
    public PathFinderTest() {
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
     * Test of getShortestPath method, of class PathFinder.
     */
    @Test
    public void testGetShortestPath_PathNode_PathNode() throws Exception {
        System.out.println("getShortestPath");
        
        PathFinder instance = new PathFinder();
        List<PathNode> map = instance.getMapCSE();
        PathNode srce = map.get(0);
        PathNode dest = map.get(1);
        
        List<PathNode> res = new ArrayList();
        res.add(srce);
        res.add(dest);
        
        List expResult = res;
        List result = instance.getShortestPath(srce, dest);
        assertEquals(expResult, result);
        
        fail("The test case is a prototype.");
    }

    /**
     * Test of getShortestPath method, of class PathFinder.
     */
    @Test
    public void testGetShortestPath_3args() throws Exception {
        System.out.println("getShortestPath");
        
        PathFinder instance = new PathFinder();
        List<PathNode> map = instance.getMapCSE();
        PathNode srce = map.get(0);
        PathNode dest = map.get(1);
        boolean optimize = true;
        
        List<PathNode> res = new ArrayList();
        res.add(srce);
        res.add(dest);
        
        List expResult = res;
        List result = instance.getShortestPath(srce, dest, optimize);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
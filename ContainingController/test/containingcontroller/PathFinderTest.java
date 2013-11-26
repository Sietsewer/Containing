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
 * @author Hendrik
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
    public void testGetShortestPath_3args() throws Exception {
        System.out.println("getShortestPath");
        boolean optimize = false;
        PathFinder instance = new PathFinder();
        instance.createMap();
        PathNode srce = instance.getMap().get(1);
        PathNode dest = instance.getMap().get(3);
        List<PathNode> expResult =new ArrayList();
        expResult.add(srce);
        expResult.add(instance.getMap().get(2));
        expResult.add(dest);
        List<PathNode> result = instance.getShortestPath(srce, dest, optimize);
        assertEquals(expResult, result);
    }

}
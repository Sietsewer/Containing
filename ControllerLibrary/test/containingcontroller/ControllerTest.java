/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.Date;
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
public class ControllerTest {
    
    public ControllerTest() {
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
     * Test of getPathFinder method, of class Controller.
     */
    @Test
    public void testGetPathFinder() {
        System.out.println("getPathFinder");
        Controller instance = new Controller(new WindowTest());
        PathFinder expResult = new PathFinder();
        expResult.createMap();
        PathFinder result = instance.getPathFinder();
        assertEquals(expResult, result);
        
        fail("The test case is a prototype.");
    }
    
    public class WindowTest implements IWindow
    {

        @Override
        public void WriteLogLine(String message) {
       }

        @Override
        public void setTime(Date simTime) {
        }
        
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

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
public class CraneTest {
    
    public CraneTest() {
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
     * Test of getReady method, of class Crane.
     */
    @Test
    public void testGetReady() {
        System.out.println("getReady");
        Crane instance = new Crane("testID", 0);
        instance.setIsReady(true);
        boolean expResult = true;
        boolean result = instance.getReady();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    /**
     * Test of toString method, of class Crane.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Crane instance = new Crane("testID", 0);
        instance.setIsReady(true);
        String expResult = "Crane{" + "id=testID, node=" + instance.node + ", ready=" + instance.getReady() + '}';
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
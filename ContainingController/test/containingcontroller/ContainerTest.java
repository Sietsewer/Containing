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
 * @author Hendrik
 */
public class ContainerTest {

    public ContainerTest() {
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
     * Test of equals method, of class Container.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");

        Container instance = new Container();
        Object obj = instance;
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
        obj = null;
        result = instance.equals(obj);
        assertEquals(false, result);

    }
}
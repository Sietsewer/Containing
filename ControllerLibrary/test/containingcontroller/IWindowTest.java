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
public class IWindowTest {
    
    public IWindowTest() {
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
     * Test of WriteLogLine method, of class IWindow.
     */
    @Test
    public void testWriteLogLine() {
        System.out.println("WriteLogLine");
        String message = "";
        IWindow instance = new IWindowImpl();
        instance.WriteLogLine(message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTime method, of class IWindow.
     */
    @Test
    public void testSetTime() {
        System.out.println("setTime");
        Date simTime = null;
        IWindow instance = new IWindowImpl();
        instance.setTime(simTime);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class IWindowImpl implements IWindow {

        public void WriteLogLine(String message) {
        }

        public void setTime(Date simTime) {
        }
    }
}
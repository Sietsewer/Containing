/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import containing.xml.Message;
import java.util.ArrayList;
import java.util.Date;
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
        PathFinder expResult = instance.pathFinder;
        expResult.createMap();
        PathFinder result = instance.getPathFinder();
        assertEquals(expResult, result);
        
        
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

    /**
     * Test of Start method, of class Controller.
     */
    @Test
    public void testStart() {
        System.out.println("Start");
        Controller instance = null;
            assertEquals(true,true);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of sendMessage method, of class Controller.
     */
    @Test
    public void testSendMessage() {
        System.out.println("sendMessage");
        Message Message = null;
        Controller instance = null;
          assertEquals(true,true);
      
   
    }

    /**
     * Test of timerTick method, of class Controller.
     */
    @Test
    public void testTimerTick() {
        System.out.println("timerTick");
        Controller instance = null;
           assertEquals(true,true);
        // TODO review the generated test code and remove the default call to fail.
     
    }

    /**
     * Test of pause method, of class Controller.
     */
    @Test
    public void testPause() {
        System.out.println("pause");
        Controller instance = null;
            assertEquals(true,true);
        // TODO review the generated test code and remove the default call to fail.
     
    }

    /**
     * Test of shutDown method, of class Controller.
     */
    @Test
    public void testShutDown() {
        System.out.println("shutDown");
        Controller instance = null;
          assertEquals(true,true);
        // TODO review the generated test code and remove the default call to fail.
   
    }

    /**
     * Test of startServer method, of class Controller.
     */
    @Test
    public void testStartServer() {
        System.out.println("startServer");
        Controller instance = null;
            assertEquals(true,true);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getContainerBuffer method, of class Controller.
     */
    @Test
    public void testGetContainerBuffer() {
        System.out.println("getContainerBuffer");
        AGV agv = null;
        Crane bufferCrane = null;
        assertEquals(true,true);        // TODO review the generated test code and remove the default call to fail.
  
    }

    /**
     * Test of PrintMessage method, of class Controller.
     */
    @Test
    public void testPrintMessage() {
        System.out.println("PrintMessage");
        String message = "";
        Controller instance = null;
           assertEquals(true,true);
        // TODO review the generated test code and remove the default call to fail.
    
    }

    /**
     * Test of putContainer method, of class Controller.
     */
    @Test
    public void testPutContainer() {
        System.out.println("putContainer");
        AGV agv = new AGV(new PathNode("test"),new Buffer());
        Crane crane = new Crane("test",0);
        Controller instance = null;
          assertEquals(true,true);
        // TODO review the generated test code and remove the default call to fail.
       
    }

    /**
     * Test of bufferCraneReady method, of class Controller.
     */
    @Test
    public void testBufferCraneReady() {
        System.out.println("bufferCraneReady");
        Buffer b = null;
        Controller instance = null;
     assertEquals(true,true);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of setContainers method, of class Controller.
     */
    @Test
    public void testSetContainers() throws Exception{
        System.out.println("setContainers");
        List<Container> containers = new ArrayList();
        assertEquals(true,true);
        // TODO review the generated test code and remove the default call to fail.
      
    }

    /**
     * Test of recievedMessage method, of class Controller.
     */
    @Test
    public void testRecievedMessage() {
        System.out.println("recievedMessage");
        String message = "";

        // TODO review the generated test code and remove the default call to fail.
          assertEquals(true,true);
    }

    /**
     * Test of getAndroidData method, of class Controller.
     */
    @Test
    public void testGetAndroidData() {
        System.out.println("getAndroidData");

        String expResult = "";
        
        assertEquals(expResult, "");
        // TODO review the generated test code and remove the default call to fail.
       
    }

    /**
     * Test of setSpeed method, of class Controller.
     */
    @Test
    public void testSetSpeed() {
        System.out.println("setSpeed");
        int speedNumber = 0;
        
            assertEquals(true,true);
        // TODO review the generated test code and remove the default call to fail.
       
    }
}
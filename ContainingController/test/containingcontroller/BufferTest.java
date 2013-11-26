/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.ArrayList;
import java.util.Calendar;
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
public class BufferTest {
    
    public BufferTest() {
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
     * Test of checkDepartingContainers method, of class Buffer.
     */
    @Test
    public void testCheckDepartingContainers() {
        System.out.println("checkDepartingContainers");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2004);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 4);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date  = cal.getTime();
        Buffer instance = new Buffer();
        Container c =new Container();
        c.setDateDeparture(date);
        instance.containers[0][0][0] = c;
        ArrayList<Container> expResult = new ArrayList<>();
        expResult.add(c);
        ArrayList<Container> result = instance.checkDepartingContainers(date);
        assertEquals(expResult, result);
    }

    /**
     * Test of findBestBufferPlace method, of class Buffer.
     */
    @Test
    public void testFindBestBufferPlace() {
        System.out.println("findBestBufferPlace");
        Container container = null;
        Buffer instance = new Buffer();
        CustomVector3f expResult = new CustomVector3f(0,0,0);
        CustomVector3f result = instance.findBestBufferPlace(container);
        assertEquals(expResult, result);
    }

    /**
     * Test of addContainer method, of class Buffer.
     */
    @Test
    public void testAddContainer() {
        System.out.println("addContainer");
        Container container = new Container();
        CustomVector3f vec =new CustomVector3f(0,0,0);
        container.setBufferPosition(vec);
        Buffer instance = new Buffer();
        instance.reservedSpace.put(vec, container);
        instance.addContainer(container);
       int expResult = 1;
       assertEquals(expResult, instance.getContainerCount());

    }

    /**
     * Test of removeContainer method, of class Buffer.
     */
    @Test
    public void testRemoveContainer() {
        System.out.println("removeContainer");
        Container container = new Container();
        Buffer instance = new Buffer();
        instance.containers[0][1][2]=container;
        instance.removeContainer(container);
        int expResult =0;
        assertEquals(expResult, instance.getContainerCount());
    }

    /**
     * Test of reservePosition method, of class Buffer.
     */
    @Test
    public void testReservePosition() {
        System.out.println("reservePosition");
        Container container =new Container();
        CustomVector3f vec= new CustomVector3f(0,0,0);
        container.setBufferPosition(vec);
        Buffer instance = new Buffer();
        instance.reservePosition(container);
        assertEquals(true, instance.reservedSpace.containsKey(vec));
 
    }


    /**
     * Test of AGVAvailable method, of class Buffer.
     */
    @Test
    public void testAGVAvailable() {
        System.out.println("AGVAvailable");
        Buffer instance = new Buffer();
        AGV a =new AGV(null,instance);
        instance.ownedAGV.add(a);
        AGV expResult = a;
        AGV result = instance.AGVAvailable();
        assertEquals(expResult, result);
    }
}
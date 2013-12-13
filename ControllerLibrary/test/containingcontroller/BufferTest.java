/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import containing.xml.CustomVector3f;
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
 * @author Wessel
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
        
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2004);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 22);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date.setTime(cal.getTimeInMillis());
        
        Container c = new Container();
        c.setDateDeparture(date);
        Buffer instance = new Buffer();
        instance.addContainer(c);
        ArrayList<Container> clist = new ArrayList<Container>();
        clist.add(c);
        
        ArrayList expResult = (ArrayList)clist.clone();
        ArrayList result = instance.checkDepartingContainers(date);
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of findBestBufferPlace method, of class Buffer.
     */
    @Test
    public void testFindBestBufferPlace() {
        System.out.println("findBestBufferPlace");
        
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2004);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 22);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date.setTime(cal.getTimeInMillis());
        
        Container container = new Container();
        container.setDateDeparture(date);
        
        boolean up = true;
        Buffer instance = new Buffer();
        CustomVector3f expResult = new CustomVector3f(0, 0, 0);
        CustomVector3f result = instance.findBestBufferPlace(container, up);
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of checkSpaceReserved method, of class Buffer.
     */
    @Test
    public void testCheckSpaceReserved() {
        System.out.println("checkSpaceReserved");
        float x = 0.0f;
        float y = 0.0f;
        float z = 0.0f;
        Buffer instance = new Buffer();
        Container c = new Container();
        c.setBufferPosition(new CustomVector3f(0, 0, 0));
        instance.addContainer(c);
        
        instance.checkSpaceReserved(x, y, z);
        boolean expResult = false;
        boolean result = instance.checkSpaceReserved(x, y, z);
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of toString method, of class Buffer.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Buffer instance = new Buffer();
        String expResult = "Buffer{" + "id=" + instance.id + ", containers=" + instance.containers + ", crane=" + instance.crane.id + ", reservedSpace=" + instance.reservedSpace + ", pathNodeUp=" + instance.pathNodeUp.getId() + ", pathNodeDown=" + instance.pathNodeDown.getId() + '}';
        String result = instance.toString();
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of AGVAvailable method, of class Buffer.
     */
    @Test
    public void testAGVAvailable() {
        System.out.println("AGVAvailable");
        boolean up = true;
        Buffer instance = new Buffer();
        AGV agv = new AGV(instance.pathNodeUp, instance);
        instance.ownedAGV.add(agv);
        
        AGV expResult = agv;
        AGV expResult2 = null;
        AGV result = instance.AGVAvailable(up);
        AGV result2 = instance.AGVAvailable(!up);
        assertEquals(expResult, result);
        assertEquals(expResult2, result2);
        
        
    }

    /**
     * Test of getContainerCount method, of class Buffer.
     */
    @Test
    public void testGetContainerCount() {
        System.out.println("getContainerCount");
        Buffer instance = new Buffer();
        
        int count = 10;
        
        for (int i = 0; i < count; i++){
            Container cont = new Container();
            cont.setBufferPosition(new CustomVector3f(i, 0, 0));
            instance.reservePosition(cont);
            instance.addContainer(cont);
        }
        
        int expResult = count;
        int result = instance.getContainerCount();
        assertEquals(expResult, result);
        
        
    }
}
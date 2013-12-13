/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import containing.xml.CustomVector3f;
import java.util.ArrayList;
import java.util.Calendar;
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
public class TransporterTest {
    
    Date date;
    
    public TransporterTest() {
        date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2004);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 22);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date.setTime(cal.getTimeInMillis());
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
     * Test of getTransportType method, of class Transporter.
     */
    @Test
    public void testGetTransportType() {
        System.out.println("getTransportType");
        Transporter instance = new Transporter(1, date);
        int expResult = 1;
        int result = instance.getTransportType();
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of getFreeLocation method, of class Transporter.
     */
    @Test
    public void testGetFreeLocation() {
        System.out.println("getFreeLocation");
        int start = 0;
        int range = 2;
        
        Transporter instance = new Transporter(0, date);
        Container c = new Container();
        c.setPosition(new CustomVector3f(0, 0, 0));
        instance.reservePosition(c);
        instance.loadContainer(c);
        
        CustomVector3f expResult = new CustomVector3f(0, 1, 0);
        CustomVector3f result = instance.getFreeLocation(start, range);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of checkSpaceReserved method, of class Transporter.
     */
    @Test
    public void testCheckSpaceReserved() {
        System.out.println("checkSpaceReserved");
        float x = 0.0F;
        float y = 0.0F;
        float z = 0.0F;
        Container c = new Container();
        c.setPosition(new CustomVector3f(0, 0, 0));
        
        Transporter instance = new Transporter(0, date);
        instance.reservePosition(c);
        
        boolean expResult = true;
        boolean result = instance.checkSpaceReserved(x, y, z);
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of getContainerCount method, of class Transporter.
     */
    @Test
    public void testGetContainerCount() {
        System.out.println("getContainerCount");
        Transporter instance = new Transporter(0, date);
        
        for(int i = 0; i < 5; i++){
            Container c = new Container();
            CustomVector3f cv = new CustomVector3f(i, 0, 0);
            c.setPosition(cv);
            instance.reservePosition(c);
            instance.loadContainer(c);
        }
        
        int expResult = 5;
        int result = instance.getContainerCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getContainers method, of class Transporter.
     */
    @Test
    public void testGetContainers() {
        System.out.println("getContainers");
        Transporter instance = new Transporter(0, date);
        List expResult = new ArrayList<Container>();
        
        for(int i = 0; i < 5; i++){
            Container c = new Container();
            CustomVector3f cv = new CustomVector3f(i, 0, 0);
            c.setPosition(cv);
            instance.reservePosition(c);
            instance.loadContainer(c);
            expResult.add(c);
        }
        
        List result = instance.getContainers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getContainer method, of class Transporter.
     */
    @Test
    public void testGetContainer() {
        System.out.println("getContainer");
        int i = 1;
        Transporter instance = new Transporter(0, date);
        Container c1 = new Container();
        Container c2 = new Container();
        c1.setPosition(new CustomVector3f(0, 0, 0));
        c2.setPosition(new CustomVector3f(1, 0, 0));
        instance.reservePosition(c1);
        instance.reservePosition(c2);
        instance.loadContainer(c1);
        instance.loadContainer(c2);
        
        Container expResult = c2;
        Container result = instance.getContainer(i);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getDockingPoint method, of class Transporter.
     */
    @Test
    public void testGetDockingPoint() {
        System.out.println("getDockingPoint");
        Transporter instance = new Transporter(0, date);
        Crane c = new Crane("testID", 2);
        instance.setDockingPoint(c);
        Crane expResult = c;
        Crane result = instance.getDockingPoint();
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of getLenghtTransporter method, of class Transporter.
     */
    @Test
    public void testGetLenghtTransporter() {
        System.out.println("getLenghtTransporter");
        Transporter instance = new Transporter(0, date);
        
        for(int i = 0; i < 7; i++){
            Container c = new Container();
            CustomVector3f cv = new CustomVector3f(i, 0, 0);
            c.setPosition(cv);
            instance.reservePosition(c);
            instance.loadContainer(c);
        }
        
        int expResult = 6;
        int result = instance.getLenghtTransporter();
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of reservePosition method, of class Transporter.
     */
    @Test
    public void testReservePosition() {
        System.out.println("reservePosition");
        Container container = new Container();
        container.setPosition(new CustomVector3f(0, 0, 0));
        Transporter instance = new Transporter(0, date);
        instance.reservePosition(container);
        boolean expResult = false;
        boolean result = instance.reservePosition(container);
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of getDateArrival method, of class Transporter.
     */
    @Test
    public void testGetDateArrival() {
        System.out.println("getDateArrival");
        Transporter instance = new Transporter(0, date);
        Date expResult = date;
        Date result = instance.getDateArrival();
        assertEquals(expResult, result);
        
        
    }
}
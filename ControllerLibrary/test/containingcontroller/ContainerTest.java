/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import containing.xml.CustomVector3f;
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
     * Test of getId method, of class Container.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        Container instance = new Container();
        instance.setId("testID");
        String expResult = "testID";
        String result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getDateArrival method, of class Container.
     */
    @Test
    public void testGetDateArrival() {
        System.out.println("getDateArrival");
        Container instance = new Container();
        
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
        instance.setDateArrival(date);
        
        Date expResult = date;
        Date result = instance.getDateArrival();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getDateDeparture method, of class Container.
     */
    @Test
    public void testGetDateDeparture() {
        System.out.println("getDateDeparture");
        Container instance = new Container();
        
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
        instance.setDateDeparture(date);
        
        Date expResult = date;
        Date result = instance.getDateDeparture();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getTransportTypeArrival method, of class Container.
     */
    @Test
    public void testGetTransportTypeArrival() {
        System.out.println("getTransportTypeArrival");
        Container instance = new Container();
        instance.setTransportTypeArrival(0);
        int expResult = 0;
        int result = instance.getTransportTypeArrival();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getTransportTypeDeparture method, of class Container.
     */
    @Test
    public void testGetTransportTypeDeparture() {
        System.out.println("getTransportTypeDeparture");
        Container instance = new Container();
        instance.setTransportTypeDeparture(0);
        int expResult = 0;
        int result = instance.getTransportTypeDeparture();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getCargoCompanyArrival method, of class Container.
     */
    @Test
    public void testGetCargoCompanyArrival() {
        System.out.println("getCargoCompanyArrival");
        Container instance = new Container();
        instance.setCargoCompanyArrival("testCompany");
        String expResult = "testCompany";
        String result = instance.getCargoCompanyArrival();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getCargoCompanyDeparture method, of class Container.
     */
    @Test
    public void testGetCargoCompanyDeparture() {
        System.out.println("getCargoCompanyDeparture");
        Container instance = new Container();
        instance.setCargoCompanyDeparture("testCompany");
        String expResult = "testCompany";
        String result = instance.getCargoCompanyDeparture();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getPosition method, of class Container.
     */
    @Test
    public void testGetPosition() {
        System.out.println("getPosition");
        Container instance = new Container();
        instance.setPosition(new CustomVector3f(0, 0, 0));
        CustomVector3f expResult = new CustomVector3f(0, 0, 0);
        CustomVector3f result = instance.getPosition();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getBufferPosition method, of class Container.
     */
    @Test
    public void testGetBufferPosition() {
        System.out.println("getBufferPosition");
        Container instance = new Container();
        instance.setPosition(new CustomVector3f(0, 0, 0));
        instance.setBufferPosition(new CustomVector3f(0, 0, 0));
        CustomVector3f expResult = new CustomVector3f(0, 0, 0);
        CustomVector3f result = instance.getBufferPosition();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getOwner method, of class Container.
     */
    @Test
    public void testGetOwner() {
        System.out.println("getOwner");
        Container instance = new Container();
        instance.setOwner("testOwner");
        String expResult = "testOwner";
        String result = instance.getOwner();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getContainerNumber method, of class Container.
     */
    @Test
    public void testGetContainerNumber() {
        System.out.println("getContainerNumber");
        Container instance = new Container();
        instance.setContainerNumber(123);
        int expResult = 123;
        int result = instance.getContainerNumber();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getHeight method, of class Container.
     */
    @Test
    public void testGetHeight() {
        System.out.println("getHeight");
        Container instance = new Container();
        instance.setHeight("testHeight");
        String expResult = "testHeight";
        String result = instance.getHeight();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getWidth method, of class Container.
     */
    @Test
    public void testGetWidth() {
        System.out.println("getWidth");
        Container instance = new Container();
        instance.setWidth("testWidth");
        String expResult = "testWidth";
        String result = instance.getWidth();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getLenght method, of class Container.
     */
    @Test
    public void testGetLenght() {
        System.out.println("getLenght");
        Container instance = new Container();
        instance.setLenght("testLength");
        String expResult = "testLength";
        String result = instance.getLenght();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getWeightEmpty method, of class Container.
     */
    @Test
    public void testGetWeightEmpty() {
        System.out.println("getWeightEmpty");
        Container instance = new Container();
        instance.setWeightEmpty(15);
        int expResult = 15;
        int result = instance.getWeightEmpty();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getWeightLoaded method, of class Container.
     */
    @Test
    public void testGetWeightLoaded() {
        System.out.println("getWeightLoaded");
        Container instance = new Container();
        instance.setWeightLoaded(40);
        int expResult = 40;
        int result = instance.getWeightLoaded();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getContents method, of class Container.
     */
    @Test
    public void testGetContents() {
        System.out.println("getContents");
        Container instance = new Container();
        instance.setContents("testContents");
        String expResult = "testContents";
        String result = instance.getContents();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getContentType method, of class Container.
     */
    @Test
    public void testGetContentType() {
        System.out.println("getContentType");
        Container instance = new Container();
        instance.setContentType("testContentType");
        String expResult = "testContentType";
        String result = instance.getContentType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getContentDanger method, of class Container.
     */
    @Test
    public void testGetContentDanger() {
        System.out.println("getContentDanger");
        Container instance = new Container();
        instance.setContentDanger("testContentDanger");
        String expResult = "testContentDanger";
        String result = instance.getContentDanger();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getIso method, of class Container.
     */
    @Test
    public void testGetIso() {
        System.out.println("getIso");
        Container instance = new Container();
        instance.setIso("testISO");
        String expResult = "testISO";
        String result = instance.getIso();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of equals method, of class Container.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Container obj = new Container();
        Container instance = new Container();
        obj.setContainerNumber(321);
        instance.setContainerNumber(321);
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }
}
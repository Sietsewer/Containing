/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Wessel
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({containingcontroller.PathFinderTest.class, containingcontroller.ControllerTest.class, containingcontroller.ContainerDepartureComparerTest.class, containingcontroller.TransporterComparerTest.class, containingcontroller.BufferTest.class, containingcontroller.TransportTypesTest.class, containingcontroller.TransporterTest.class, containingcontroller.AGVTest.class, containingcontroller.PathNodeTest.class, containingcontroller.ContainerTest.class, containingcontroller.ContainerComparerTest.class, containingcontroller.CraneTest.class})
public class ContainingcontrollerSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
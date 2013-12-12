/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
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
public class XMLParserTest {

    public XMLParserTest() {
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
     * Test of parseXMLFile method, of class XMLParser.
     */
    @Test
    public void testParseXMLFile() {
        System.out.println("parseXMLFile");
        try {
            InputStream File = new FileInputStream("C:\\Users\\Hendrik\\Documents\\XML\\xml1.xml");
            int expResult = 10;
            List result = XMLParser.parseXMLFile(File);
            assertEquals(expResult, result.size());
        } catch (Exception e) {
            // TODO review the generated test code and remove the default call to fail.
            fail("Test failed file not found.");
        }
    }
}
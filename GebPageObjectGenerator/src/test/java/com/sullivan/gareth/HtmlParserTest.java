package com.sullivan.gareth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

/**
 * This class now superseded by the spock equivalent test class @see HtmlParserSpockTest.groovy
 * @author GSULLIVA
 *
 */
public class HtmlParserTest {

    private static final String TEST_HTML_FILE_LOCATION = "src//test//resources//example.html";

    private HtmlParser classToTest = null;
    
    private static List<String> inputFieldsIdList = new ArrayList<String>();
    
    private static final String myNameId = "myName";
    
    private static final String myAgeId = "myAge";
    
    private Document doc;
    
    static
    {
        inputFieldsIdList.add(myNameId);
        inputFieldsIdList.add(myAgeId);
    }
    
    @Before
    public void setup()
    {
        classToTest = new HtmlParser();
        assertNotNull(classToTest);
        doc = (Document) classToTest.parse(TEST_HTML_FILE_LOCATION);
    }
    
    @Test
    public void testGetFileContent() {
        assertNull(classToTest.getFileContent(""));
        assertNull(classToTest.getFileContent(null));
        assertNull(classToTest.getFileContent("file.ht"));
        assertNull(classToTest.getFileContent("file.htmlhmtl"));
        String fileContent = classToTest.getFileContent(TEST_HTML_FILE_LOCATION);
        assertNotNull(fileContent);
        assertEquals(fileContent, HtmlContent.EXAMPLE_FORM);
    }

    @Test
    public void testParse()
    {
        assertNotNull(doc);
        assertNull(classToTest.parse(null));
        //This will test groovy implementation where jsoup parses directly from a file
        //Document doc = (Document) classToTest.parse(TEST_HTML_FILE_LOCATION);
        //System.out.println("Doc "+doc);
    }
    
    @Test
    public void testGetInputFieldsIterator()
    {
        List<String> inputFields = classToTest.getInputFieldsIterator();
        assertNotNull(inputFields);
        assertEquals(2, inputFields.size());
        assertTrue(inputFields.containsAll(inputFieldsIdList));
    }
    
    @Test
    public void testGetTitle()
    {
        assertEquals(HtmlContent.TITLE,classToTest.getTitle());
    }
}

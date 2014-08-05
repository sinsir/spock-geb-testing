package com.sullivan.gareth
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

/**
 * Parses a HTML file and allows retrieval of 
 * 
 * <ul>
 *  <li>id of input fields</li>
 *  <li>page url</li>
 *  <li>page title</li>
 * </ul>
 * @author GSULLIVA
 *
 */
class HtmlParser {

    /** Permitted HTML suffix */
    def static HTML_SUFFIX = "html";
    
    /** Permitted HTM suffix */
    def static HTM_SUFFIX = "htm";
     
    /**
     * Returns the String representation of a HTML/HTM file
     * @param path the path of the file to load
     * @return content of the file
     */
    String getFileContent(path)
    {
        def fileContent
        if (path !=null && path != ""  && isValidSuffix(path))
        {
            fileContent = new File(path).text
        }
        return fileContent
    }
    
    /**
     * Returns true if suffix is HTML or HTM (case insensitive), false otherwise
     * @param suffix
     * @return
     */
    boolean isValidSuffix(suffix)
    {
        return suffix.toLowerCase().endsWith(HTML_SUFFIX) || suffix.toLowerCase().endsWith(HTM_SUFFIX)
    }
    
    /**
     * Given a String containing HTML markup, returns a JSoup (see http://jsoup.org/) Document
     * 
     * @param htmlString String representation of a HTML document
     * @return JSoup Document representation of the HTML doc
     */
    Document parse(htmlString)
    {
        Document doc
        if (htmlString != null)
        {
            doc = Jsoup.parse(htmlString);
            //alternatively we could use jsoup to parse straight from a file
            //doc = Jsoup.parse(new File(htmlString), "UTF-8", "http://example.com/")
        }
        return doc
    }
    
    /**
     * Returns a list of the ids of all input elements found in the supplied Document object
     * @param doc JSoup Document representation of the HTML doc
     * @return List of strings - ids of all input elements
     */
    List getInputFieldsIterator(doc)
    {
        def inputFields = doc.select("input").toList()
        def inputFieldsAsStrings = []
        inputFields.each{inputFieldsAsStrings.add(it.id())}
        
        return inputFieldsAsStrings 
    }
    
    /**
     * Returns the title of the HTML document
     * @param doc JSoup Document representation of the HTML doc
     * @return title of the HTML doc
     */
    String getTitle(doc)
    {
        return doc.title()
    }
}
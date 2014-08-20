package com.sullivan.gareth

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Parses a HTML file and allows retrieval of 
 * 
 * <ul>
 *  <li>ids of input fields</li>
 *  <li>page url</li>
 *  <li>page title</li>
 *  <li>ids of submit button fields</li> 
 * </ul>
 * @author GSULLIVA
 *
 */
class HtmlParser {

    /** Permitted HTML suffix */
    static final HTML_SUFFIX = 'html'
    
    /** Permitted HTM suffix */
    static final HTM_SUFFIX = 'htm'
    
    private filePath
     
    private doc
    
    /**
     * Returns the String representation of a HTML/HTM file
     * @param path the path of the file to load
     * @return content of the file
     */
    String getFileContent(path) {
        def fileContent
        if (path != null && path != ''  && isValidSuffix(path)) {
            fileContent = new File(path).text
        }
    }
    
    /**
     * Returns true if suffix is HTML or HTM (case insensitive), false otherwise
     * @param suffix
     * @return true if suffix is HTML or HTM (case insensitive), false otherwise
     */
    private boolean isValidSuffix(suffix) {
        suffix.toLowerCase().endsWith(HTML_SUFFIX) || suffix.toLowerCase().endsWith(HTM_SUFFIX)
    }
    
    /**
     * Given a String containing HTML markup, returns a JSoup (see http://jsoup.org/) Document
     * 
     * @param path String representation of a HTML document
     * @return JSoup Document representation of the HTML doc
     */
    Document parse(path) {
        filePath = path
        def htmlString = getFileContent(path)
        if (htmlString != null) {
            doc = Jsoup.parse(htmlString, filePath)
            //alternatively we could use jsoup to parse straight from a file
            //doc = Jsoup.parse(new File(htmlString), "UTF-8", "http://example.com/")
        }
    }
    
    /**
     * Returns a list of the ids of all input elements
     * @return List of strings - ids of all input elements
     */
    List getInputFieldsIterator() {
        if (doc != null) {
            convertElementsToIdList(doc.select('input').toList())
        }
    }
    
    /**
     * Returns the title of the HTML document
     * @return title of the HTML doc
     */
    String getTitle() {
        if (doc != null) {
            doc.title()
        }
    }
    
    /**
     * Returns the URL this HTML document was parsed from
     * @return url the HTML doc
     */
    String getUrl() {
        if (doc != null) {
            doc.location()
        }
    }
    
    /**
     * Returns a list of the ids of all button elements with type=submit
     * @return List of strings - ids of all input elements
     */
    List getSubmitButtonIds() {
        if (doc != null) {
            convertElementsToIdList(doc.select('button[type=submit]').toList())
        }
    }
    
    String getPageName() {
        if (doc != null) {
            new File(filePath).name.tokenize('.').first().capitalize()
        }
    }
    
    String getPackageName() {
        if (doc != null) {
            def packageList = new File(filePath).path.tokenize(File.separator)
            
            packageList.take(packageList.size() - 1).join('.')
        }
    }
    
    String getFormAction() {
        if (doc != null) {
            def action = doc.select('form').first()
            action.attr('action')
        }
    }
    
    /**
     * Returns a list of the ids of all tables
     * @return List of strings - ids of all table elements
     */
    List getTableIds()
    {
        if (doc != null) {
            convertElementsToIdList(doc.select('body > table').toList())
        }
    }
    
    List getTableHeaders()
    {
        if (doc != null) {
            def stringList = []
            doc.select('body > table th').toList().each { stringList.add(it.text().minus(" ").toLowerCase()) }
            stringList
        }
    }
   
    private List convertElementsToIdList(elementList) {
        def stringList = []
        elementList.each { stringList.add(it.id()) }
        stringList
    }
}

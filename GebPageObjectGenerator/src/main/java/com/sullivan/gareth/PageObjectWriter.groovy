package com.sullivan.gareth

import groovy.text.SimpleTemplateEngine

class PageObjectWriter {
    
    static final templateText = new File('src' + File.separator + 'main' + File.separator + 'resources' + File.separator + 'GebPageObjectTemplate.txt').text
    
    def htmlFilePath
    
    /**
     * Creates and returns a Map of key/value pairs
     * 
     * Keys are the tokens in a substitution file, values are inserted into these tokens 
     * at substitution time
     * 
     * @return Map of String key value/pairs
     */
    Map getSubstitutionBinding()
    {
        def htmlParser = new HtmlParser()
        htmlParser.parse(htmlFilePath)
        [packageString: htmlParser.packageName,
                       htmlPageName: htmlParser.pageName,
                       url: htmlParser.url,
                       title: htmlParser.title,
                       inputFields: htmlParser.inputFieldsIterator,
                       submitButton: htmlParser.submitButtonIds ]
    }
    
    /**
     * Creates the template engine using a template page object text file, and substitutes tags with values.
     * @return String representation of the document
     */
    String populateTemplate()
    {
        def engine = new SimpleTemplateEngine()
        engine.createTemplate(templateText).make(getSubstitutionBinding())
    }
    
    void writeGebFile(path)
    {
        File file = new File(path).write(populateTemplate())
    }
}

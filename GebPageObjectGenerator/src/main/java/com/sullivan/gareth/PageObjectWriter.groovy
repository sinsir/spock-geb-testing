package com.sullivan.gareth

import groovy.text.SimpleTemplateEngine

class PageObjectWriter {
    
    InputStream is = getClass().getResourceAsStream('/GebPageObjectTemplate.txt')
    
    File templateText = new File(is.getText())
    
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
        engine.createTemplate(templateText.toString()).make(getSubstitutionBinding())
    }
    
    void writeGebFile(path)
    {
        File file = new File(path).write(populateTemplate())
    }
    
    static boolean isArgsValid(args)
    {
        if (args.size() != 2)
        {
            println "Usage: java -jar GebPageObjectGenerator-x.x.x.jar htmlFilePath outputPath"
            return false
        }
        return true
    }
    
    static void main(String[] args)
    {
        if (args != null && PageObjectWriter.isArgsValid(args))
        {
            PageObjectWriter writer = new PageObjectWriter(htmlFilePath:args[0])
            writer.writeGebFile(args[1])
        }
    }
}

package com.sullivan.gareth

import groovy.text.SimpleTemplateEngine

class PageObjectWriter {
    
    InputStream is = getClass().getResourceAsStream('/GebPageObjectTemplate.txt')
    
    File templateText = new File(is.getText())
    
    def htmlFilePath
    
    def gebFileName
    
    final def htmlParser = new HtmlParser()
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
        htmlParser.parse(htmlFilePath)
        gebFileName = htmlParser.pageName
        [packageString: htmlParser.packageName,
                       htmlPageName: htmlParser.pageName,
                       url: htmlParser.url,
                       title: htmlParser.title,
                       formAction: htmlParser.formAction,
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
    
    void writeGebFile()
    {
        def populatedTemplate = populateTemplate()
        makeGebDirectory()
        def gebFilePath = "geb" + File.separator + gebFileName + ".groovy"
        File file = new File(gebFilePath).write(populatedTemplate)
    }
    
    private makeGebDirectory()
    {
        File gebDir = new File('geb')
        if (!gebDir.exists())
        {
            gebDir.mkdir()
        }
    }
    
    static boolean isArgsValid(args)
    {
        if (args.size() != 1)
        {
            println "Usage: java -jar GebPageObjectGenerator-x.x.x.jar htmlFilePath"
            return false
        }
        return true
    }
    
    static void main(String[] args)
    {
        if (args != null && PageObjectWriter.isArgsValid(args))
        {
            PageObjectWriter writer = new PageObjectWriter(htmlFilePath:args[0])
            writer.writeGebFile()
        }
    }
}

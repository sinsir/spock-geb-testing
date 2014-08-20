package com.sullivan.gareth

import groovy.text.SimpleTemplateEngine

/**
 * Loads a Geb Page object template file containing substitution tokens, and populates them using values 
 * parsed from a HTML file using  the HtmlParser
 * 
 * @author GSULLIVA
 */
class PageObjectWriter extends ObjectWriter{
    
    /** Page Object template text file */
    String templateText = new File(getClass().getResourceAsStream('/GebPageObjectTemplate.txt').text)
    
    /** Path to the html file to be parsed into a geb page object */
    def htmlFilePath
    
    /** filename of the converted geb object */
    def gebFileName
    
    /** instance of the parser */
    final htmlParser = new HtmlParser()
    
    
    
    /**
     * Creates and returns a Map of key/value pairs
     * 
     * Keys are the tokens in a substitution file, values are inserted into these tokens 
     * at substitution time
     * 
     * @return Map of String key value/pairs
     */
    Map getSubstitutionBinding(String fileName) {
        htmlParser.parse(fileName)
        gebFileName = htmlParser.pageName
        [packageString: htmlParser.packageName,
                       htmlPageName: htmlParser.pageName,
                       url: htmlParser.url,
                       title: htmlParser.title,
                       formAction: htmlParser.formAction,
                       inputFields: htmlParser.inputFieldsIterator,
                       submitButton: htmlParser.submitButtonIds,
                       tables: htmlParser.tableIds ]
    }
    
    def createModule()
    {
        def tableIds = htmlParser.tableIds
        if (!tableIds?.empty)
        {
            for (int i=0; i < tableIds.size(); i ++){
                def moduleWriter = new ModuleTemplateWriter(htmlParser:htmlParser, filePath:gebFileName)
                moduleWriter.writeGebFiles()
            }
        }
    }
    
    /**
     * Creates the template engine using a template page object text file, and substitutes tokens with values 
     * retrieved from a parsed HTML file
     * 
     * Can process single files
     * @return List with single String element if processing a single file, or List of Strings representing 
     * multiple files if processing a directory containing more than one html file
     */
    List populateTemplate() {
        def engine = new SimpleTemplateEngine().createTemplate(templateText)
        List populatedTemplateList = []
        if (isProcessingDir(htmlFilePath)) {
            def fileList = getFileNamesFromDirectory(htmlFilePath)
            for (int i=0; i < fileList.size(); i ++) {
                
                def binding = getSubstitutionBinding(fileList[i].absolutePath)
                populatedTemplateList.add(
                    new CompletedTemplate(fileName:gebFileName, gebString:engine.make(binding).toString()))
                createModule()
            }
        }
        else {
            def binding = getSubstitutionBinding(htmlFilePath)
            populatedTemplateList[0] = 
                new CompletedTemplate(fileName:gebFileName, gebString:engine.make(binding).toString())
                createModule()
        }
        
        populatedTemplateList
    }
    

    /**
     * Check if expected number of command line arguments were passed in
     * 
     * @param args String array of command line arguments
     * @return true if valid, false otherwise
     */
    static boolean isArgsValid(args) {
        if (args.size() != 1) {
            println 'Usage: java -jar GebPageObjectGenerator-x.x.x.jar htmlFilePath'
            return false
        }
        true
    }
    
    
    
    /**
     * Entry point into the code
     * 
     * @param args arguments passed in on command line
     */
    static void main(String[] args) {
        if (args != null && PageObjectWriter.isArgsValid(args)) {
           new PageObjectWriter(htmlFilePath:args[0]).writeGebFiles()
        }
    }
}

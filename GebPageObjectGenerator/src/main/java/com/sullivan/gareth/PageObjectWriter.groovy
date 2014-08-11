package com.sullivan.gareth

import groovy.text.SimpleTemplateEngine

/**
 * Loads a Geb Page object template file containing substitution tokens, and populates them using values 
 * parsed from a HTML file using  the HtmlParser
 * 
 * @author GSULLIVA
 */
class PageObjectWriter {
    
    /** Page Object template text file */
    String templateText = new File(getClass().getResourceAsStream('/GebPageObjectTemplate.txt').text)
    
    /** Path to the html file to be parsed into a geb page object */
    def htmlFilePath
    
    /** filename of the converted geb object */
    def gebFileName
    
    /** instance of the parser */
    final htmlParser = new HtmlParser()
    
    private static final String GEB_DIR = 'geb'
    
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
                       submitButton: htmlParser.submitButtonIds ]
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
        if (isProcessingDir()) {
            def fileList = fileNamesFromDirectory
            for (int i=0; i < fileList.size(); i ++) {
                def binding = getSubstitutionBinding(fileList[i].absolutePath)
                populatedTemplateList.add(
                    new CompletedTemplate(fileName:gebFileName, gebString:engine.make(binding).toString()))
            }
        }
        else {
            def binding = getSubstitutionBinding(htmlFilePath)
            populatedTemplateList[0] = 
                new CompletedTemplate(fileName:gebFileName, gebString:engine.make(binding).toString())
        }
        
        populatedTemplateList
    }
    
    /**
     * Filename filter to search for *.htm or *.html files
     * 
     * @author GSULLIVA
     *
     */
    private final class HtmlFileFilter implements FilenameFilter {
        /**
         * Returns true if filename has a .htm/.hmtl suffix, false otherwise
         * 
         * @return  true if filename has a .htm/.hmtl suffix, false otherwise
         */
        boolean accept(File f, String filename) {
                 filename.toLowerCase().endsWith('.htm') || filename.toLowerCase().endsWith('.html')
            }
        }
    
    /**
     * Returns an array of File objects representing htm/html files in the directory 
     * specified when creating this object
     * 
     * @return array of html File objects
     */
    private getFileNamesFromDirectory() {
        new File(htmlFilePath).listFiles(new HtmlFileFilter())
    }
    
    /**
     * Converts html file(s) to geb, and writes them to disk as .groovy files
     */
    void writeGebFiles() {
        List completedTemplates = populateTemplate()
        makeGebDirectory()
        for (int i = 0; i  < completedTemplates.size(); i ++) {
            File file = new File(GEB_DIR + File.separator + completedTemplates[i].fileName + '.groovy')
            file.write(completedTemplates[i].gebString)
        }
    }
    
    /**
     * If it doesn't already exist, creates a geb directory to store output groovy files
     */
    private void makeGebDirectory() {
        File gebDir = new File(GEB_DIR)
        if (!gebDir.exists()) {
            gebDir.mkdir()
        }
    }
    
    /**
     * Checks whether html path argument passed in on command line is a directory
     * 
     * @return true if directory, false otherwise
     */
    private boolean isProcessingDir() {
        new File(htmlFilePath).isDirectory()
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
     * Bean class to store groovy output fileName and geb page object string for a html 
     * file thats been converted to a geb groovy file
     * 
     * @author GSULLIVA
     *
     */
    private class CompletedTemplate {
        String fileName
        String gebString
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

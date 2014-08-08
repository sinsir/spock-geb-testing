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
    Map getSubstitutionBinding(String fileName)
    {
        println 'getSubstitutionBinding fileName ' + fileName
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
     * Creates the template engine using a template page object text file, and substitutes tags with values.
     * @return String representation of the document
     */
    List populateTemplate()
    {
        def engine = new SimpleTemplateEngine().createTemplate(templateText.toString())
        List populatedTemplateList = []
        if (isProcessingDir())
        {
            println "processing a dir"
            def fileList = getFileNamesFromDirectory()
            println "fileList " + fileList
            //fileList.each { file-> println file.getAbsolutePath() }
            fileList.each { file-> populatedTemplateList.add(engine.make(getSubstitutionBinding(file.getAbsolutePath()))) }
            
            
        }
        else
        {
            populatedTemplateList[0] = engine.make(getSubstitutionBinding(htmlFilePath))
        }
        
        println "populatedTemplateList " + populatedTemplateList
        return populatedTemplateList
        //engine.createTemplate(templateText.toString()).make(getSubstitutionBinding())
    }
    
    private final class HtmlFileFilter implements FilenameFilter {
            boolean accept(File f, String filename) {
                 filename.toLowerCase().endsWith(".htm") || filename.toLowerCase().endsWith(".html")
            }
        }
    
    private def getFileNamesFromDirectory()
    {
        
        new File(htmlFilePath).listFiles(new HtmlFileFilter())
        
        
//        new File(htmlFilePath).list(new HtmlFileFilter())
    }
    
    void writeGebFile()
    {
        def populatedTemplate = populateTemplate()
        makeGebDirectory()
        def gebFilePath = "geb" + File.separator + gebFileName + ".groovy"
        populatedTemplate.each { template -> new File(gebFilePath).write(template.toString()) }
    }
    
    private makeGebDirectory()
    {
        File gebDir = new File('geb')
        if (!gebDir.exists())
        {
            gebDir.mkdir()
        }
    }
    
    private boolean isProcessingDir()
    {
        new File(htmlFilePath).isDirectory()
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

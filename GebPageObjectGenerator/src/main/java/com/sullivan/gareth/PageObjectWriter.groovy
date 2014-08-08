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
            def fileList = getFileNamesFromDirectory()
            for (int i=0; i < fileList.size(); i ++)
            {
                def binding = getSubstitutionBinding(fileList[i].getAbsolutePath())
                populatedTemplateList.add(new CompletedTemplate(fileName:gebFileName,gebString:engine.make(binding).toString()))
            }
        }
        else
        {
            def binding = getSubstitutionBinding(htmlFilePath)
            populatedTemplateList[0] = new CompletedTemplate(fileName:gebFileName,gebString:engine.make(binding).toString())
        }
        
        return populatedTemplateList
    }
    
    private final class HtmlFileFilter implements FilenameFilter {
            boolean accept(File f, String filename) {
                 filename.toLowerCase().endsWith(".htm") || filename.toLowerCase().endsWith(".html")
            }
        }
    
    private def getFileNamesFromDirectory()
    {
        new File(htmlFilePath).listFiles(new HtmlFileFilter())
    }
    
    void writeGebFiles()
    {
        List completedTemplates = populateTemplate()
        makeGebDirectory()
       // def gebFilePath = 'geb' + File.separator + gebFileName + '.groovy'
        for (int i = 0; i  < completedTemplates.size(); i ++)
        {
            File file = new File('geb' + File.separator + completedTemplates[i].fileName+'.groovy')
            file.write(completedTemplates[i].gebString)
        }
        //completedTemplates.each { template -> new File(template.fileName).write(template.gebString) }
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
    
    private class CompletedTemplate
    {
        String fileName
        String gebString
    }
    static void main(String[] args)
    {
        if (args != null && PageObjectWriter.isArgsValid(args))
        {
            PageObjectWriter writer = new PageObjectWriter(htmlFilePath:args[0])
            writer.writeGebFiles()
        }
    }
}

package com.sullivan.gareth

import groovy.text.SimpleTemplateEngine

import java.util.Map;

class ModuleTemplateWriter extends ObjectWriter {

    /** instance of the parser */
    def htmlParser = new HtmlParser()
    
    String filePath
    
    String gebFileName
    
    /** Page Object template text file */
    String templateText = new File(getClass().getResourceAsStream('/ModuleTemplate.txt').text)

    List populateTemplate() {
        def engine = new SimpleTemplateEngine().createTemplate(templateText)
        List populatedTemplateList = []

        def binding = getSubstitutionBinding()
        populatedTemplateList[0] =
                new CompletedTemplate(fileName:gebFileName, gebString:engine.make(binding).toString())

        populatedTemplateList
    }

    /**
     * Creates and returns a Map of key/value pairs
     *
     * Keys are the tokens in a substitution file, values are inserted into these tokens
     * at substitution time
     *
     * @return Map of String key value/pairs
     */
    Map getSubstitutionBinding() {
        htmlParser.parse(filePath)
        gebFileName = htmlParser.pageName + 'Module'
        [packageString: htmlParser.packageName,
            moduleName: gebFileName,
            tableHeaders: htmlParser.tableHeaders ]
    }
}

package com.sullivan.gareth

import spock.lang.Specification

class PageObjectWriterSpockTest extends Specification {

    private final String testFileLocation = 
        'src' + File.separator + 'test' + File.separator + 'resources' + File.separator + 'example.html'
        
    private final String testFileOutputLocation =
        'target' + File.separator + 'testOutput.groovy'
    
    private final PageObjectWriter pow = new PageObjectWriter(htmlFilePath:testFileLocation)
    
    def 'binding returns valid values'()
    {
        when: 'we create a binding'
            def bindings = pow.substitutionBinding
        then: 'check that it contains what we expect'
            bindings != null
            bindings.size() == 7
            bindings.keySet().asList() == 
                ['packageString', 'htmlPageName', 'url', 'title', 'formAction', 'inputFields', 'submitButton']
            bindings.values().asList() == 
                ['src.test.resources', 'Example', testFileLocation, 'Name And Age Submission Form', '#', ['myName', 'myAge'], ['mySubmitButton']]
    }
    
    def "template is populated with values from binding"()
    {
        when: 'we populate the template file'
            String template = pow.populateTemplate()
        then: 'check that it has substituted all tokens with the expected values specified in the binding'
            template != null
            // check that the populated template contains all the string valued from the binding
            // created by parsing the HTML
            pow.substitutionBinding.values().asList().each { item -> template.contains(item.toString()) }
    }
    
    def "file is written to disk"()
    {
        when: 'we save the template to a file'
            pow.writeGebFile()
        then: 'check that the file exists'
            def file = new File('geb' + File.separator + 'Example.groovy')
            file.exists()
            // need to check that the file contains substituted values
    }   
}

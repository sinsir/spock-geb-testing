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
            def bindings = pow.getSubstitutionBinding(testFileLocation)
        then: 'check that it contains what we expect'
            bindings != null
            bindings.size() == 7
            bindings.keySet().asList() == 
                ['packageString', 'htmlPageName', 'url', 'title', 'formAction', 'inputFields', 'submitButton']
            bindings.values().asList() == 
                ['src.test.resources', 'Example', testFileLocation, 'Name And Age Submission Form', '#', ['myName', 'myAge'], ['mySubmitButton']]
    }
    
    def "a single template is populated with values from binding"()
    {
        when: 'we populate the template file'
            List template = pow.populateTemplate()
        then: 'check that it has substituted all tokens with the expected values specified in the binding'
            template != null
            template.size() == 1
            // check that the populated template contains all the string valued from the binding
            // created by parsing the HTML
            pow.getSubstitutionBinding(testFileLocation).values().asList().each { item -> template.contains(item.toString()) }
    }
    
    
    def "mutliple templates are populated with values from binding"()
    {
        def testHtmlSrc = 'target' + File.separator + 'testHtmlFiles'
        File testDir = new File(testHtmlSrc)
        setup: 'create a directory containing 5 html files'
            testDir.mkdir()
            for (int i=0; i<5; i++)
            {
                new File(testHtmlSrc + File.separator + 'Test'+(i+1)+'.html').write(HtmlContent.EXAMPLE_FORM)
            }          
            def pageObjectWriter = new PageObjectWriter(htmlFilePath:testHtmlSrc)  
        when: 'we create and populate multiple templates'
            List templates = pageObjectWriter.populateTemplate()
        then: 'check that 5 templates have been created and have been substituted with values'
            templates != null
            templates.size() == 5
            templates.each { item -> item.toString().contains('Example')}
         cleanup: 'delete the 5 html file and their parent directory'
             testDir.listFiles().each {t -> t.delete()}
             testDir.delete()
    }
    
    def "a single file is written to disk"()
    {
        when: 'we save the template to a file'
            pow.writeGebFiles()
        then: 'check that the file exists'
            def file = new File('geb' + File.separator + 'Example.groovy')
            file.exists()
            // need to check that the file contains substituted values
        cleanup:
            File gebDir = new File('geb')
            gebDir.listFiles().each {t -> t.delete()}
            gebDir.delete()
            
    }
    
    def "multiple file are written to disk"()
    {
        def testHtmlSrc = 'target' + File.separator + 'testHtmlFiles'
        File testDir = new File(testHtmlSrc)
        setup: 'create a directory containing 5 html files'
            testDir.mkdir()
            for (int i=0; i<5; i++)
            {
                new File(testHtmlSrc + File.separator + 'Test'+(i+1)+'.html').write(HtmlContent.EXAMPLE_FORM)
            }
            def pageObjectWriter = new PageObjectWriter(htmlFilePath:testHtmlSrc)
        when: 'we write 5 geb files'
            List templates = pageObjectWriter.writeGebFiles()
        then: 'check that 5 files have been written to disk and contain substituted values'
            File gebDir = new File('geb')
            gebDir.exists() && gebDir.isDirectory()
            gebDir.listFiles().size() == 5
            gebDir.listFiles().each { file -> file.name.startsWith('Test') && file.name.endsWith('.groovy') }
         cleanup: 'delete the 5 html file and their parent directory'
             gebDir.listFiles().each {t -> t.delete()}
             gebDir.delete()
    }
}
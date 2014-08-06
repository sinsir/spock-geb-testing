package com.sullivan.gareth

import spock.lang.Specification

class HtmlParserSpockTest extends Specification {
    private static final TEST_HTML_FILE_LOCATION = 'src//test//resources//example.html'
    
    def htmlParser = new HtmlParser()
    
    def "valid file content is stored as a String"()
    {
       expect:
           htmlParser.getFileContent(TEST_HTML_FILE_LOCATION) == HtmlContent.EXAMPLE_FORM
    }
    
    def "specifying invalid file extension returns null"()
    {
       expect:
           htmlParser.getFileContent('file.doc') == null
           htmlParser.getFileContent('') == null
    }
    
    def "html is parsed succesfully"()
    {
        expect:
            htmlParser.parse(HtmlContent.EXAMPLE_FORM) != null
            htmlParser.parse(null) == null
    }
    
    def "input elements are returned succesfully"()
    {
        when:
            def doc = htmlParser.parse(HtmlContent.EXAMPLE_FORM)
        then:
            def inputFieldsList = htmlParser.getInputFieldsIterator(doc)
        expect:
            inputFieldsList.size == 2
            inputFieldsList == ['myName', 'myAge']  
    }
    
    def "parser returns html document title"()
    {
        setup:
            def doc = htmlParser.parse(HtmlContent.EXAMPLE_FORM)
        when:
            def title = htmlParser.getTitle(doc)
        then:
            title == HtmlContent.TITLE
    }
}

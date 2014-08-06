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
            htmlParser.parse(TEST_HTML_FILE_LOCATION) != null
            htmlParser.parse(null) == null
    }
    
    def "input elements are returned succesfully"()
    {
        when:
            def doc = htmlParser.parse(TEST_HTML_FILE_LOCATION)
        then:
            def inputFieldsList = htmlParser.getInputFieldsIterator()
        expect:
            inputFieldsList.size == 2
            inputFieldsList == ['myName', 'myAge']  
    }
    
    def "parser returns html document title"()
    {
        setup:
            def doc = htmlParser.parse(TEST_HTML_FILE_LOCATION)
        when:
            def title = htmlParser.getTitle()
        then:
            title == HtmlContent.TITLE
    }
    
    def "parser returns submit button id"()
    {
        when:
            def doc = htmlParser.parse(TEST_HTML_FILE_LOCATION)
        then:
            def idList = htmlParser.getSubmitButtonIds()
        expect:
            idList != null
            idList.size == 1
            idList[0] == 'mySubmitButton'
     }
}

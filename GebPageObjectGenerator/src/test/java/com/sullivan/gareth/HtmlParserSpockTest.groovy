package com.sullivan.gareth

import spock.lang.Specification

class HtmlParserSpockTest extends Specification {
    private static final TEST_HTML_FILE_LOCATION = 'src//test//resources//example.html'
    
    HtmlParser htmlParser = new HtmlParser()
    
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
    
    def "accessing document elements before it has been parsed returns null"()
    {
        expect:
            htmlParser.inputFieldsIterator == null
            htmlParser.submitButtonIds == null
            htmlParser.title == null
            htmlParser.url == null
    }
    
    def "input elements are returned succesfully, after parsing document"()
    {
        setup:
            htmlParser.parse(TEST_HTML_FILE_LOCATION)
        when:
            def inputFieldsList = htmlParser.getInputFieldsIterator()
        then:
            inputFieldsList.size == 2
            inputFieldsList == ['myName', 'myAge']  
    }
    
    def "parser returns html document title, after parsing document"()
    {
        setup:
            htmlParser.parse(TEST_HTML_FILE_LOCATION)
        when:
            def title = htmlParser.title
        then:
            title == HtmlContent.TITLE
    }
    
    def "parser returns submit button id, after parsing document"()
    {
        setup:
            htmlParser.parse(TEST_HTML_FILE_LOCATION)
        when:
            def idList = htmlParser.submitButtonIds
        then:
            idList != null
            idList.size == 1
            idList[0] == 'mySubmitButton'
     }
    
    def "parser returns html document url, after parsing document"()
    {
        setup:
           htmlParser.parse(TEST_HTML_FILE_LOCATION)
        when:
            def url = htmlParser.url
        then:
            url != null
            url != ""
    }
    
    def "parser returns html form action element"()
    {
        setup:
            htmlParser.parse(TEST_HTML_FILE_LOCATION)
        when:
            def formAction = htmlParser.formAction
        then:
            formAction != null 
    }
    
    def "parser returns html table id"(){
        setup:
            htmlParser.parse(TEST_HTML_FILE_LOCATION)
        when:
            def tableIds = htmlParser.tableIds
        then:
            tableIds != null && tableIds.size() > 0
//            println tableIds
    }
}

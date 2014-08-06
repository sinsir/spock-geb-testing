package com.sullivan.gareth

class PageObjectWriter {
    
    static final templateText = new File('src\\main\\resources\\GebPageObjectTemplate.txt').text
    
    def htmlFilePath
    
    Map getBinding()
    {
        def htmlParser = new HtmlParser()
        htmlParser.parse(htmlFilePath)
        def binding = [htmlPageName: new File(htmlFilePath).name.tokenize('.').first().capitalize(),
                       url:"testUrl",
                       title : htmlParser.title,
                       mylist : htmlParser.inputFieldsIterator  ]
    }
    
    String populateTemplate()
    {
        def engine = new groovy.text.SimpleTemplateEngine()
        
        def template = engine.createTemplate(templateText).make(getBinding())
    }
    
    static void main(String[] args)
    {
        def pow = new PageObjectWriter(htmlFilePath:"src//test//resources//example.html")
        println "Populated template is " + pow.populateTemplate()
    }

}

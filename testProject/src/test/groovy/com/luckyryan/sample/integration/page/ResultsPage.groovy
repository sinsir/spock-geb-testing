package com.luckyryan.sample.integration.page

import geb.Page

class ResultsPage extends Page {

    static url = "/results"
    static at = { title == "Results Page" }
    static content = {
        heading { $("h1").text() }
    }

}

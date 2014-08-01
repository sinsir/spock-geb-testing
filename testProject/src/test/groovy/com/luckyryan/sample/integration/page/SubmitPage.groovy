package com.luckyryan.sample.integration.page

import geb.Page

class SubmitPage extends Page
{

    static url = "/"
    static at = { title == "Name And Age Submission Form" }
    static content = {
        signupForm { $("form[id=signupForm]") }
        nameField { $("input[id=myName]") }
        ageField { $("input[id=myAge]") }
        submitButton(to: ResultsPage) { $("button[type=submit]") }
    }


}

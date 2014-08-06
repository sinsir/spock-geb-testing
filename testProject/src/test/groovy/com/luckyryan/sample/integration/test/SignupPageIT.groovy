package com.luckyryan.sample.integration.test

import com.luckyryan.sample.integration.page.SubmitPage
import com.luckyryan.sample.integration.page.ResultsPage
import geb.spock.GebReportingSpec
import spock.lang.Stepwise

@Stepwise
class SignupPageIT extends GebReportingSpec {

    def "Signup Test Happy Path"() {

        given: "I'm at the sign up form"
        to SubmitPage

        when: "I signup as a valid user"
        nameField = "firstname"
        ageField = "19"
        submitButton.click()

        then: "I'm at the result page "
        at ResultsPage

    }

    def "Signup Test for Invalid User"() {

        given: "I'm at the sign up form"
        to SubmitPage

        when: "I signup with an invalid age (<18)"
        nameField = "firstname"
        ageField = "17"
        submitButton.click(SubmitPage)

        then: "I'm at the sign up page again "
        at SubmitPage
    }


}

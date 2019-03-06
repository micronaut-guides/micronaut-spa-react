package example.micronaut

import spock.lang.Specification
import spock.util.environment.RestoreSystemProperties

class SendGridEmailConditionSpec extends Specification {

    @RestoreSystemProperties
    def "condition is true if system properties are present"() {
        given:
        System.setProperty("sendgrid.apikey", "XXXX")
        System.setProperty("sendgrid.fromemail", "me@micronaut.example")
        SendGridEmailCondition condition = new SendGridEmailCondition()

        expect:
        condition.matches(null)
    }

    def "condition is false if system properties are not present"() {
        given:
        SendGridEmailCondition condition = new SendGridEmailCondition()

        expect:
        !condition.matches(null)
    }
}

package example.micronaut

import spock.lang.Specification
import spock.util.environment.RestoreSystemProperties

class AwsSesMailConditionSpec extends Specification {

    @RestoreSystemProperties
    def "condition is true if system properties are present"() {
        given:
        System.setProperty("aws.region", "XXXX")
        System.setProperty("aws.sourceemail", "me@micronaut.example")
        AwsSesMailCondition condition = new AwsSesMailCondition()

        expect:
        condition.matches(null)
    }

    def "condition is false if system properties are not present"() {
        given:
        AwsSesMailCondition condition = new AwsSesMailCondition()

        expect:
        !condition.matches(null)
    }
}

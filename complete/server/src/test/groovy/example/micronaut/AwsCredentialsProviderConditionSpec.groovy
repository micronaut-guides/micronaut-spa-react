package example.micronaut

import spock.lang.Specification
import spock.util.environment.RestoreSystemProperties

class AwsCredentialsProviderConditionSpec extends Specification {

    @RestoreSystemProperties
    def "condition is true if system properties are present"() {
        given:
        System.setProperty("aws.accesskeyid", "XXXX")
        System.setProperty("aws.secretkey", "YYYY")

        AwsCredentialsProviderCondition condition = new AwsCredentialsProviderCondition()

        expect:
        condition.matches(null)
    }

    def "condition is false if system properties are not present"() {
        given:
        AwsCredentialsProviderCondition condition = new AwsCredentialsProviderCondition()

        expect:
        !condition.matches(null)
    }
}

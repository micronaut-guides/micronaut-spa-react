package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.exceptions.NoSuchBeanException
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.Specification
import spock.util.environment.RestoreSystemProperties

class AwsSesMailServiceSpec extends Specification {

    def "AwsSesMailService is not loaded if system property is not present"() {
        given:
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

        when:
        embeddedServer.applicationContext.getBean(AwsSesMailService)

        then:
        thrown(NoSuchBeanException)

        cleanup:
        embeddedServer.close()
    }

    @RestoreSystemProperties
    def "AwsSesMailService is loaded if system properties are present"() {
        given:
        System.setProperty("aws.region", "XXXX")
        System.setProperty("aws.sourceemail", "me@micronaut.example")
        System.setProperty("aws.accesskeyid", "XXXX")
        System.setProperty("aws.secretkey", "YYYY")
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

        when:
        embeddedServer.applicationContext.getBean(AwsCredentialsProviderService)
        AwsSesMailService bean = embeddedServer.applicationContext.getBean(AwsSesMailService)

        then:
        noExceptionThrown()
        bean.sourceEmail == 'me@micronaut.example'
        bean.awsRegion == 'XXXX'

        cleanup:
        embeddedServer.close()
    }
}

package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.exceptions.NoSuchBeanException
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.Specification
import spock.util.environment.RestoreSystemProperties

class AwsCredentialsProviderServiceSpec extends Specification {

    def "AwsCredentialsProviderService is not loaded if system property is not present"() {
        given:
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

        when:
        embeddedServer.applicationContext.getBean(AwsCredentialsProviderService)

        then:
        thrown(NoSuchBeanException)

        cleanup:
        embeddedServer.close()
    }

    @RestoreSystemProperties
    def "AwsCredentialsProviderService is loaded if system properties are present"() {
        given:
        System.setProperty("aws.accesskeyid", "XXXX")
        System.setProperty("aws.secretkey", "YYYY")
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

        when:
        AwsCredentialsProviderService bean = embeddedServer.applicationContext.getBean(AwsCredentialsProviderService)

        then:
        noExceptionThrown()
        bean.secretKey == 'YYYY'
        bean.accessKey == 'XXXX'

        cleanup:
        embeddedServer.close()
    }
}

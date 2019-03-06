package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.exceptions.NoSuchBeanException
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.Specification
import spock.util.environment.RestoreSystemProperties

class SendGridEmailServiceSpec extends Specification {

    def "SendGridEmailService is not loaded if system property is not present"() {
        given:
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

        when:
        embeddedServer.applicationContext.getBean(SendGridEmailService)

        then:
        thrown(NoSuchBeanException)

        cleanup:
        embeddedServer.close()
    }

    @RestoreSystemProperties
    def "SendGridEmailService is loaded if system properties are present"() {
        given:
        System.setProperty("sendgrid.apikey", "XXXX")
        System.setProperty("sendgrid.fromemail", "me@micronaut.example")
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

        when:
        SendGridEmailService bean = embeddedServer.applicationContext.getBean(SendGridEmailService)

        then:
        noExceptionThrown()
        bean.apiKey == 'XXXX'
        bean.fromEmail == 'me@micronaut.example'

        cleanup:
        embeddedServer.close()
    }
}

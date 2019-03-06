package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import javax.annotation.Nullable

class MailControllerSpec extends Specification {

    @Shared
    @AutoCleanup // <1>
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer, [
            'spec.name': 'mailcontroller',
    ], 'test')  // <2>

    @Shared
    @AutoCleanup
    RxHttpClient client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL()) // <3>

    def "/mail/send interacts once email service"() {
        given:
        EmailCmd cmd = new EmailCmd(subject: 'Test',
                recipient: 'delamos@grails.example',
                textBody: 'Hola hola')
        HttpRequest request = HttpRequest.POST('/mail/send', cmd) // <4>

        when:
        Collection emailServices = embeddedServer.applicationContext.getBeansOfType(EmailService)

        then:
        !emailServices.any { it == SendGridEmailService.class}
        !emailServices.any { it == AwsSesMailService.class}

        when:
        EmailService emailService = embeddedServer.applicationContext.getBean(EmailService)

        then:
        emailService instanceof MockEmailService

        when:
        HttpResponse rsp = client.toBlocking().exchange(request)

        then:
        rsp.status.code == 200
        ((MockEmailService)emailService).emails.size() == old(((MockEmailService)emailService).emails.size()) + 1 // <5>
    }
}
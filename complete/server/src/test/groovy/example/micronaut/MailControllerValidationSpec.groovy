package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification


class MailControllerValidationSpec extends Specification {

    @Shared
    @AutoCleanup
    // <1>
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer, [
            'spec.name': 'mailcontroller',
    ], 'test')  // <2>

    @Shared
    @AutoCleanup
    RxHttpClient client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL()) // <3>


    def "/mail/send cannot be invoked without subject"() {
        given:
        EmailCmd cmd = new EmailCmd(
                recipient: 'delamos@micronaut.example',
                textBody: 'Hola hola')
        HttpRequest request = HttpRequest.POST('/mail/send', cmd) // <4>

        when:
        client.toBlocking().exchange(request)

        then:
        HttpClientResponseException e = thrown(HttpClientResponseException)
        e.status.code == 400
    }

    def "/mail/send cannot be invoked without recipient"() {
        given:
        EmailCmd cmd = new EmailCmd(
                subject: 'Hola',
                textBody: 'Hola hola')
        HttpRequest request = HttpRequest.POST('/mail/send', cmd) // <4>

        when:
        client.toBlocking().exchange(request)

        then:
        HttpClientResponseException e = thrown(HttpClientResponseException)
        e.status.code == 400
    }

    def "/mail/send cannot be invoked without either textBody or htmlBody"() {
        given:
        EmailCmd cmd = new EmailCmd(
                subject: 'Hola',
                recipient: 'delamos@micronaut.example',)
        HttpRequest request = HttpRequest.POST('/mail/send', cmd) // <4>

        when:
        client.toBlocking().exchange(request)

        then:
        HttpClientResponseException e = thrown(HttpClientResponseException)
        e.status.code == 400
    }

    def "/mail/send can be invoked without textBody and not htmlBody"() {
        given:
        EmailCmd cmd = new EmailCmd(
                subject: 'Hola',
                recipient: 'delamos@micronaut.example',
                textBody: 'Hello')
        HttpRequest request = HttpRequest.POST('/mail/send', cmd) // <4>

        when:
        HttpResponse rsp = client.toBlocking().exchange(request)

        then:
        rsp.status().code == 200
    }

    def "/mail/send can be invoked without htmlBody and not textBody"() {
        given:
        EmailCmd cmd = new EmailCmd(
                subject: 'Hola',
                recipient: 'delamos@micronaut.example',
                htmlBody: '<h1>Hello</h1>')
        HttpRequest request = HttpRequest.POST('/mail/send', cmd) // <4>

        when:
        HttpResponse rsp = client.toBlocking().exchange(request)

        then:
        rsp.status().code == 200
    }
}
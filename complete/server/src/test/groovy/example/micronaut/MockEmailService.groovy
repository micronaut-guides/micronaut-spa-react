package example.micronaut

import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Requires

import javax.inject.Singleton

@Primary
@Requires(property = 'spec.name', value = 'mailcontroller')
@Singleton
class MockEmailService implements EmailService {

    List<Email> emails = []

    @Override
    void send(Email email) {
        emails << email
    }
}

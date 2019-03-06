package example.micronaut;

import com.amazonaws.services.simpleemail.model.SendEmailResult;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;


@Singleton // <1>
@Requires(beans = AwsCredentialsProviderService.class) // <2>
@Requires(condition = AwsSesMailCondition.class)  // <3>
@Primary // <4>
public class AwsSesMailService implements EmailService {
    private static final Logger LOG = LoggerFactory.getLogger(AwsSesMailService.class);

    protected final String awsRegion;

    protected final String sourceEmail;

    protected final AwsCredentialsProviderService awsCredentialsProviderService;

    public AwsSesMailService(@Value("${AWS_REGION:none}") String awsRegionEnv, // <5>
                      @Value("${AWS_SOURCE_EMAIL:none}") String sourceEmailEnv,
                      @Value("${aws.region:none}") String awsRegionProp,
                      @Value("${aws.sourceemail:none}") String sourceEmailProp,
                      AwsCredentialsProviderService awsCredentialsProviderService
                      ) {
        this.awsRegion = awsRegionEnv != null && !awsRegionEnv.equals("none") ? awsRegionEnv : awsRegionProp;
        this.sourceEmail = sourceEmailEnv != null && !sourceEmailEnv.equals("none") ? sourceEmailEnv : sourceEmailProp;
        this.awsCredentialsProviderService = awsCredentialsProviderService;
    }

    private Body bodyOfEmail(Email email) {
        if (email.getHtmlBody() != null && !email.getHtmlBody().isEmpty()) {
            Content htmlBody = new Content().withData(email.getHtmlBody());
            return new Body().withHtml(htmlBody);
        }
        if (email.getTextBody() != null && !email.getTextBody().isEmpty()) {
            Content textBody = new Content().withData(email.getTextBody());
            return new Body().withHtml(textBody);
        }
        return new Body();
    }

    @Override
    public void send(Email email) {

        if ( awsCredentialsProviderService == null ) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("AWS Credentials provider not configured");
            }
            return;
        }

        Destination destination = new Destination().withToAddresses(email.getRecipient());
        if ( email.getCc() !=null) {
            destination = destination.withCcAddresses(email.getCc());
        }
        if ( email.getBcc() != null ) {
            destination = destination.withBccAddresses(email.getBcc());
        }
        Content subject = new Content().withData(email.getSubject());
        Body body = bodyOfEmail(email);
        Message message = new Message().withSubject(subject).withBody(body);

        SendEmailRequest request = new SendEmailRequest()
                .withSource(sourceEmail)
                .withDestination(destination)
                .withMessage(message);

        if ( email.getReplyTo() != null ) {
            request = request.withReplyToAddresses();
        }

        try {
            if (LOG.isInfoEnabled()) {
                LOG.info("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");
            }

            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                    .withCredentials(awsCredentialsProviderService)
                    .withRegion(awsRegion)
                    .build();

            SendEmailResult sendEmailResult = client.sendEmail(request);

            if (LOG.isInfoEnabled()) {
                LOG.info("Email sent! {}", sendEmailResult.toString());
            }
        } catch (Exception ex) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("The email was not sent.");
                LOG.warn("Error message: {}", ex.getMessage());
            }
        }
    }
}
package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.Valid;

@Controller("/mail") // <1>
@Validated // <2>
public class MailController {
    private static final Logger LOG = LoggerFactory.getLogger(MailController.class);

    protected final EmailService emailService;

    public MailController( EmailService  emailService) { // <3>
        this.emailService = emailService;
    }

    @Post("/send") // <4>
    public HttpResponse send(@Body @Valid EmailCmd cmd) { // <5>

        emailService.send(cmd);
        return HttpResponse.ok();  // <6>
    }
}
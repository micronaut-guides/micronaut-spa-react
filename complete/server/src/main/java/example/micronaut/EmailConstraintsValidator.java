package example.micronaut;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailConstraintsValidator implements ConstraintValidator<EmailConstraints, EmailCmd> {

    @Override
    public boolean isValid(EmailCmd email, ConstraintValidatorContext context) {
        return email !=null &&
                (notBlankAndNotNull(email.getTextBody()) || notBlankAndNotNull(email.getHtmlBody()));
    }

    private boolean notBlankAndNotNull(String str) {
        return str != null && !str.equals("");
    }
}

package example.micronaut;

import io.micronaut.context.condition.Condition;
import io.micronaut.context.condition.ConditionContext;

public class SendGridEmailCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context) {
        return envOrSystemProperty("SENDGRID_APIKEY", "sendgrid.apikey") &&
                envOrSystemProperty("SENDGRID_FROM_EMAIL", "sendgrid.fromemail");
    }

    private boolean envOrSystemProperty(String env, String prop) {
        return notBlankAndNotNull(System.getProperty(prop)) || notBlankAndNotNull(System.getenv(env));
    }

    private boolean notBlankAndNotNull(String str) {
        return str != null && !str.equals("");
    }
}

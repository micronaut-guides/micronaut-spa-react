package example.micronaut;

import io.micronaut.context.condition.Condition;
import io.micronaut.context.condition.ConditionContext;

public class AwsCredentialsProviderCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context) {
        return envOrSystemProperty("AWS_ACCESS_KEY_ID", "aws.accesskeyid") &&
                envOrSystemProperty("AWS_SECRET_KEY", "aws.secretkey");
    }

    private boolean envOrSystemProperty(String env, String prop) {
        return notBlankAndNotNull(System.getProperty(prop)) || notBlankAndNotNull(System.getenv(env));
    }

    private boolean notBlankAndNotNull(String str) {
        return str != null && !str.equals("");
    }
}

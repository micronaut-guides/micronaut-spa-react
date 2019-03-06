package example.micronaut;

import io.micronaut.context.condition.Condition;
import io.micronaut.context.condition.ConditionContext;

public class AwsSesMailCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context) {
        return (notBlankAndNotNull(System.getProperty("aws.sourceemail")) || notBlankAndNotNull(System.getenv("AWS_SOURCE_EMAIL"))) &&
                (notBlankAndNotNull(System.getProperty("aws.region")) ||  notBlankAndNotNull(System.getenv("AWS_REGION")));
    }

    private boolean notBlankAndNotNull(String str) {
        return str != null && !str.equals("");
    }
}
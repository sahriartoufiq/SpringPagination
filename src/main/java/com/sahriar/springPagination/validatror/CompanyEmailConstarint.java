package com.sahriar.springPagination.validatror;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CompanyEmailvalidators.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CompanyEmailConstarint {
    String message() default "Should be DSINNOVATORS mail";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

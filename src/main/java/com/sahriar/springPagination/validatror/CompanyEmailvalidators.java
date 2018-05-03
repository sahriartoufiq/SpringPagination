package com.sahriar.springPagination.validatror;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CompanyEmailvalidators implements ConstraintValidator<CompanyEmailConstarint, String> {
    @Override
    public void initialize(CompanyEmailConstarint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return email != null && email.toLowerCase().endsWith("@dsinnovators.com");
    }
}

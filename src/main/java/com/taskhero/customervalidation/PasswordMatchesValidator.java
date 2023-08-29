package com.taskhero.customervalidation;

import com.taskhero.user.dto.UserRegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

  @Override
  public void initialize(PasswordMatches constraintAnnotation) {
    // initialize validation
  }

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
    UserRegisterRequest userRegisterRequest = (UserRegisterRequest) object;
    boolean valid =
        userRegisterRequest.getPassword().equals(userRegisterRequest.getMatchPassword());

    if (!valid) {
      constraintValidatorContext.disableDefaultConstraintViolation();
      constraintValidatorContext
          .buildConstraintViolationWithTemplate("password don't match")
          .addPropertyNode("password")
          .addConstraintViolation();
    }
    return valid;
  }
}

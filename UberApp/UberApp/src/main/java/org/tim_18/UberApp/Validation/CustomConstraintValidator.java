package org.tim_18.UberApp.Validation;

import jakarta.validation.ConstraintValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomConstraintValidator implements ConstraintValidator<CustomConstraint, String> {
    /*
    poziva se svaki put pre upotrebe instance validatora
     */
    @Override
    public void initialize(CustomConstraint string) {

    }

    /*
    vrsi validaciju custom polja koje je anotirano
     */
    @Override
    public boolean isValid(String customField, ConstraintValidatorContext ctx) {

        if (customField == null) {
            return false;
        }
        return customField.matches("[0-9]{13}");
    }

}

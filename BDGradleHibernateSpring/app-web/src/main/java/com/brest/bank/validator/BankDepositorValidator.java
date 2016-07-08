package com.brest.bank.validator;

import com.brest.bank.domain.BankDepositor;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class BankDepositorValidator implements Validator {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public boolean supports(Class<?> clazz) {
        return BankDepositor.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BankDepositor depositor = (BankDepositor)target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositorName", "depositorName.validation.empty", "Err014");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositorDateDeposit", "label.validate.depositorDateDepositEmpty","Err016");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositorDateReturnDeposit", "label.validate.depositorDateReturnDepositEmpty","Err017");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositorAmountDeposit", "label.validate.depositorAmountDepositEmpty","Err018");

        if (depositor.getDepositorAmountDeposit() == 0) {
            errors.rejectValue("depositorAmountDeposit", "label.validate.depositorAmountDepositEmpty", "Err018");
        }
        if (depositor.getDepositorAmountDeposit() < 0) {
            errors.rejectValue("depositorAmountDeposit", "depositorAmountDeposit.validation.negative", "Err019");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositorAmountPlusDeposit", "label.validate.depositorAmountPlusDepositEmpty","Err020");

        if (depositor.getDepositorAmountPlusDeposit() < 0) {
            errors.rejectValue("depositorAmountPlusDeposit", "depositorAmountPlusDeposit.validation.negative", "Err021");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositorAmountMinusDeposit", "label.validate.depositorAmountMinusDepositEmpty","Err022");

        if (depositor.getDepositorAmountMinusDeposit() < 0) {
            errors.rejectValue("depositorAmountMinusDeposit", "depositorAmountMinusDeposit.validation.negative", "Err023");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositorMarkReturnDeposit", "label.validate.depositorMarkReturnDepositEmpty","Err024");

        if (depositor.getDepositorMarkReturnDeposit() < 0 || depositor.getDepositorMarkReturnDeposit() > 1) {
            errors.rejectValue("depositorMarkReturnDeposit", "depositorMarkReturnDeposit.validation.negative", "Err025");
        }
    }
}

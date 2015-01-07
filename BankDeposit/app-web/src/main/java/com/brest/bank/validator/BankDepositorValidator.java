package com.brest.bank.validator;

import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositorService;

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

    private BankDepositorService depositorService;

    @Override
    public boolean supports(Class<?> clazz) {
        return BankDepositor.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BankDepositor depositor = (BankDepositor) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositorName", "depositorName.empty", "Depositor name must not be empty.");
        ValidationUtils.rejectIfEmpty(errors, "depositorName", "depositorName.empty", "Depositor name must not be empty.");
        if (depositor.getDepositorName().length() > 255) {
            errors.rejectValue("depositorName", "depositorName.tooLong", "Depositor name must not more than 255 characters.");
        }

        if (depositor.getDepositorName().length() == 0) {
            errors.rejectValue("depositorName", "depositorName.validation.negative", "Name depositor is required - length must be > 0");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositorDateDeposit", "label.validate.depositorDateDepositEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositorDateReturnDeposit", "label.validate.depositorDateReturnDepositEmpty");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositorAmountDeposit", "label.validate.depositorAmountDepositEmpty");

        if (depositor.getDepositorAmountDeposit() == 0) {
            errors.rejectValue("depositorAmountDeposit", "depositorAmountDeposit.validation.negative", "The amount of bank deposit is required");
        }
        if (depositor.getDepositorAmountDeposit() < 0) {
            errors.rejectValue("depositorAmountDeposit", "depositorAmountDeposit_validation.negative", "must be > 0");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositorAmountPlusDeposit", "label.validate.depositorAmountPlusDepositEmpty");

        if (depositor.getDepositorAmountPlusDeposit() < 0) {
            errors.rejectValue("depositorAmountPlusDeposit", "depositorAmountPlusDeposit_validation.negative", "must be > 0");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositorAmountMinusDeposit", "label.validate.depositorAmountMinusDepositEmpty");

        if (depositor.getDepositorAmountMinusDeposit() < 0) {
            errors.rejectValue("depositorAmountMinusDeposit", "depositorAmountMinusDeposit_validation.negative", "must be > 0");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositorMarkReturnDeposit", "label.validate.depositorMarkReturnDepositEmpty");
        ValidationUtils.rejectIfEmpty(errors, "depositorMarkReturnDeposit", "label.validate.depositorMarkReturnDepositEmpty");
        if (depositor.getDepositorMarkReturnDeposit() < 0 || depositor.getDepositorMarkReturnDeposit() > 1) {
            errors.rejectValue("depositorMarkReturnDeposit", "depositorMarkReturnDeposit_validation.negative", "must be 0 or 1");
        }
    }
}

package com.brest.bank.validator;

import com.brest.bank.domain.BankDeposit;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class BankDepositValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return BankDeposit.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BankDeposit deposit = (BankDeposit)target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositName", "depositName.validation.empty", "Err001");

        if (deposit.getDepositName().length() > 255) {
            errors.rejectValue("depositName", "depositName.validation.tooLong", "Err002");
        }

        if (deposit.getDepositName().length() == 0) {
            errors.rejectValue("depositName", "depositName.validation.negative", "Err003");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositCurrency", "label.validate.depositCurrencyEmpty", "Err004");

        if (deposit.getDepositCurrency().length() < 3) {
            errors.rejectValue("depositCurrency", "depositCurrency.validation.negative", "Err005");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositAddConditions", "label.validate.depositAddConditionsEmpty", "Err006");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositMinTerm", "label.validate.depositMinTermEmpty", "Err007");

        try {
            if (deposit.getDepositMinTerm() == 0) {
                errors.rejectValue("depositMinTerm", "label.validate.depositMinTermEmpty", "Err007");
            }

            if (deposit.getDepositMinTerm() < 0) {
                errors.rejectValue("depositMinTerm", "depositMinTerm.validation.negative", "Err008");
            }
            if (deposit.getDepositMinTerm() > 120) {
                errors.rejectValue("depositMinTerm", "depositMinTerm.validation.negative", "Err008");
            }
        } catch(IllegalArgumentException e) {
            errors.rejectValue("depositMinTerm", "label.validate.depositMinTermEmpty", "Err007");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositMinAmount", "label.validate.depositMinAmountEmpty","Err009");

        if (deposit.getDepositMinAmount() == 0){
            errors.rejectValue("depositMinAmount", "label.validate.depositMinAmountEmpty", "Err009");
        }
        if (deposit.getDepositMinAmount() < 0) {
            errors.rejectValue("depositMinAmount", "depositMinAmount.validation.negative", "Err010");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositInterestRate", "label.validate.depositInterestRateEmpty","Err011");

        if (deposit.getDepositInterestRate() == 0) {
            errors.rejectValue("depositInterestRate", "label.validate.depositInterestRateEmpty", "Err011");
        }

        if (deposit.getDepositInterestRate() < 0) {
            errors.rejectValue("depositInterestRate", "depositInterestRate.validation.negative", "Err012");
        }

        if (deposit.getDepositInterestRate() > 100) {
            errors.rejectValue("depositInterestRate", "depositInterestRate.validation.negative", "Err012");
        }
    }
}

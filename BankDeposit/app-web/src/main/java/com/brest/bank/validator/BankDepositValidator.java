package com.brest.bank.validator;

import com.brest.bank.service.BankDepositService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.brest.bank.domain.BankDeposit;

@Component
public class BankDepositValidator /*implements Validator*/ {

    private static final Logger LOGGER = LogManager.getLogger();

    private BankDepositService depositService;

    public boolean supports(Class<?> clazz) {
        return BankDeposit.class.isAssignableFrom(clazz);
    }

    public void validate(BankDeposit deposit, Errors errors) {
        //BankDeposit deposit = (BankDeposit)target;
        //LOGGER.debug("BankDeposit deposit = (BankDeposit)target({},{})",target, deposit);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositName", "depositName.empty", "Deposit name must not be empty.");
        String depositName = deposit.getDepositName();
        if ((depositName.length()) > 255) {
            errors.rejectValue("depositName", "depositName.tooLong", "Deposit name must not more than 255 characters.");
        }
        /*if (!errors.hasFieldErrors("depositName")) {
            BankDeposit existingDeposit = depositService.getBankDepositByName(deposit.getDepositName());
            if (existingDeposit != null) {
                errors.rejectValue("depositName", "validation.exists", "exists");
            }
        }*/
        if (deposit.getDepositName().length() == 0) {
            errors.rejectValue("depositName", "depositName.validation.negative", "Name deposit is required - length must be > 0");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositCurrency", "label.validate.depositCurrencyEmpty", "Currency bank deposit is required");
        if (deposit.getDepositCurrency().length() < 3) {
            errors.rejectValue("depositCurrency", "depositCurrency.validation.negative", "Currency bank deposit is required, length must be 3 symbols");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositAddConditions", "label.validate.depositAddConditionsEmpty", "Additional agreements bank deposit is required");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositMinTerm", "label.validate.depositMinTermEmpty");

        try {
            Integer MinTerm = deposit.getDepositMinTerm();
            if (MinTerm == 0) {
                errors.rejectValue("depositMinTerm", "depositMinTerm.validation.negative", "Minimum term bank deposit is required");
            }

            if (MinTerm < 0) {
                errors.rejectValue("depositMinTerm", "depositMinTerm.negative.validation", "must be > 0");
            }
            if (MinTerm > 120) {
                errors.rejectValue("depositMinTerm", "depositMinTerm_validation.negative", "must be <= 120");
            }
        } catch(NumberFormatException e) {
            errors.rejectValue("depositMinTerm", "depositMinTerm.validation.negative", "Minimum term bank deposit is required");
        }



        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositMinAmount", "label.validate.depositMinAmountEmpty");
        if (deposit.getDepositMinAmount() == 0){
            errors.rejectValue("depositMinAmount", "depositMinAmount.validation.negative", "The minimum amount of bank deposit is required");
        }
        if (deposit.getDepositMinAmount() < 0) {
            errors.rejectValue("depositMinAmount", "depositMinAmount_validation.negative", "must be > 0");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "depositInterestRate", "label.validate.depositInterestRateEmpty");
        if (deposit.getDepositInterestRate() == 0) {
            errors.rejectValue("depositInterestRate", "depositInterestRate.validation.negative", "The interest rate of bank deposit is required");
        }

        if (deposit.getDepositInterestRate() < 0) {
            errors.rejectValue("depositInterestRate", "depositInterestRate_validation.negative", "must be > 0");
        }

        if (deposit.getDepositInterestRate() > 100) {
            errors.rejectValue("depositInterestRate", "depositInterestRate.negative.validation", "must be <= 100");
        }
    }
}

package com.brest.bank.validator;

import java.beans.PropertyEditorSupport;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.service.BankDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankDepositTypeEditor extends PropertyEditorSupport {

    private BankDepositService depositService;

    public BankDepositTypeEditor() {}

    @Autowired
    public void setDepositService(BankDepositService depositService) {
        this.depositService = depositService;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        BankDeposit deposit= depositService.getBankDepositById(Long.parseLong(text));
        this.setValue(deposit);
    }

    @Override
    public String getAsText() {
        BankDeposit deposit = (BankDeposit) this.getValue();
        return Long.toString(deposit.getDepositId());
    }
}

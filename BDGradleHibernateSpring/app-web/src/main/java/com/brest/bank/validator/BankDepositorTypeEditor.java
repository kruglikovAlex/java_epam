package com.brest.bank.validator;

import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

@Component
public class BankDepositorTypeEditor extends PropertyEditorSupport {

    private BankDepositorService depositorService;

    public BankDepositorTypeEditor() {}

    @Autowired
    public void setDepositorService(BankDepositorService depositorService) {
        this.depositorService = depositorService;
    }

    @Override
    public String getAsText() {
        BankDepositor depositor = (BankDepositor) this.getValue();
        return Long.toString(depositor.getDepositorId());
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        BankDepositor depositor = depositorService.getBankDepositorById(Long.parseLong(text));
        this.setValue(depositor);
    }
}

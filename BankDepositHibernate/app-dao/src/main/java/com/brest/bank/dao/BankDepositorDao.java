package com.brest.bank.dao;

import com.brest.bank.domain.BankDepositor;

public interface BankDepositorDao {

    public Long addBankDepositor(BankDepositor depositor);
    public void updateBankDepositor(BankDepositor depositor);
    public void removeBankDepositor(Long depositorId);

}

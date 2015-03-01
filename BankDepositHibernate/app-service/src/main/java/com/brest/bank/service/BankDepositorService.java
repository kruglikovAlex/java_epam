package com.brest.bank.service;

import com.brest.bank.domain.BankDepositor;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BankDepositorService {
    //--- get:
        //--- all
    public List<BankDepositor> getBankDepositors();
        //--- single
    public BankDepositor getBankDepositorById(Long id);
    public BankDepositor getBankDepositorByName(String depositorName);
        //--- list of entity
    public List<BankDepositor> getBankDepositorsBetweenDateDeposit(Date startDate, Date endDate);
    //--- CRUD:
        //--- create
    public void addBankDepositor(Long depositId, BankDepositor depositor);
        //--- update
    public void updateBankDepositor(BankDepositor depositor);
        //--- delete
    public void removeBankDepositor(Long id);
}

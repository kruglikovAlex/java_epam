package com.brest.bank.service;

import com.brest.bank.domain.BankDeposit;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BankDepositService {
    //--- get:
        //--- all
    public List<BankDeposit> getBankDeposits();
        //--- single
    public BankDeposit getBankDepositById(Long id);
    public BankDeposit getBankDepositByName(String depositName);
        //--- list of entity
    public List<BankDeposit> getBankDepositsBetweenDateDeposit(Date startDate, Date endDate);
    //--- CRUD:
        //--- create
    public void addBankDeposit(BankDeposit deposit);
        //--- update
    public void updateBankDeposit(BankDeposit deposit);
        //--- delete
    public void removeBankDeposit(Long id);
}

package com.brest.bank.service;

import com.brest.bank.domain.BankDeposit;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BankDepositService {
    //--- connection
    public void setSession(SessionFactory sessionFactory);
    //--- get:
        //--- all
    public List<BankDeposit> getBankDeposits();
        //--- single
    public BankDeposit getBankDepositById(Long id);
    public BankDeposit getBankDepositByName(String depositName);
    //--- create
    public void addBankDeposit(BankDeposit deposit);
    //--- update
    public void updateBankDeposit(BankDeposit deposit);
    //--- delete
    public void deleteBankDeposit(Long id);
}

package com.brest.bank.service;

import com.brest.bank.domain.BankDepositor;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BankDepositorService {
    //--- connection
    public void setSession(SessionFactory sessionFactory);
    //--- get:
        //--- all
    public List<BankDepositor> getBankDepositors();
        //--- single
    public BankDepositor getBankDepositorById(Long id);
    public BankDepositor getBankDepositorByName(String depositorName);
    //--- create
    public void addBankDepositor(Long depositId, BankDepositor depositor);
    //--- update
    public void updateBankDepositor(BankDepositor depositor);
    //--- delete
    public void removeBankDepositor(Long id);
}

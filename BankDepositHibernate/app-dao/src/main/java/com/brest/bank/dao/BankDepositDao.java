package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import org.hibernate.SessionFactory;

import java.util.List;

public interface BankDepositDao {
    //--- connection
    public void setSession(SessionFactory sessionFactory);
    //---- get
        //---- all
    public List<BankDeposit> getBankDepositsCriteria();
        //---- single
    public BankDeposit getBankDepositByIdCriteria(Long id);
    public BankDeposit getBankDepositByNameCriteria(String depositName);
    //---- create
    public void addBankDeposit(BankDeposit deposit);
    //---- update
    public void updateBankDeposit(BankDeposit deposit);
    //---- delete
    public void deleteBankDeposit(Long id);
}

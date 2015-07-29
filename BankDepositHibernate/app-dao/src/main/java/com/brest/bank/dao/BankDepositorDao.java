package com.brest.bank.dao;

import com.brest.bank.domain.BankDepositor;
import org.hibernate.SessionFactory;

import java.util.List;

public interface BankDepositorDao {
    //--- connection
    public void setSession(SessionFactory sessionFactory);
    //---- get
        //---- all
    public List<BankDepositor> getBankDepositorsCriteria();
        //---- single
    public BankDepositor getBankDepositorByIdCriteria(Long depositorId);
    public BankDepositor getBankDepositorByNameCriteria(String depositorName);
    //---- create
    public void addBankDepositor(Long depositId, BankDepositor depositor);

    public void updateBankDepositor(BankDepositor depositor);

    public void removeBankDepositor(Long id);
}

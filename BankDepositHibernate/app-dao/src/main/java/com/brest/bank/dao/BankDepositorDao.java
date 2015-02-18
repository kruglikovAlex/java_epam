package com.brest.bank.dao;

import com.brest.bank.domain.BankDepositor;
import java.util.List;

public interface BankDepositorDao {
    //---- close connection
    public void closeConnection();
    //---- get
    public List<BankDepositor> getBankDepositorsSQL();
    public List<BankDepositor> getBankDepositorsCriteria();
    public BankDepositor getBankDepositorByIdGet(Long depositorId);
    public BankDepositor getBankDepositorByIdLoad(Long depositorId);
    public BankDepositor getBankDepositorByIdCriteria(Long depositorId);
    public BankDepositor getBankDepositorByNameSQL(String depositorName);
    public BankDepositor getBankDepositorByNameCriteria(String depositorName);
    //---- CRUD:
        //---- create
    public void addBankDepositor(Long depositId, BankDepositor depositor);
        //---- update
    public void updateBankDepositor(BankDepositor depositor);
        //---- delete
    public void removeBankDepositor(Long id);

}

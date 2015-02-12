package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import java.util.Date;
import java.util.List;

public interface BankDepositDao {
    public void closeConnection();
    //---- get
    public List<BankDeposit> getBankDepositsSQL();
    public List<BankDeposit> getBankDepositsCriteria();
    public BankDeposit getBankDepositByIdGet(Long depositId);
    public BankDeposit getBankDepositByIdLoad(Long depositId);
    public BankDeposit getBankDepositByIdCriteria(Long id);
    public BankDeposit getBankDepositByNameSQL(String depositName);
    public BankDeposit getBankDepositByNameCriteria(String depositName);
    public BankDeposit getBankDepositByNameByNaturalIdCriteria(String name);
    public List<BankDeposit> getBankDepositBetweenMinTermCriteria(Integer minTerm, Integer maxTerm);
    public List<BankDeposit> getBankDepositsAllDepositorsBetweenDateDeposit(Date startDate, Date endDate);
    //---- CRUD:
        //---- create
    public void addBankDeposit(BankDeposit deposit);
        //---- update
    public void updateBankDeposit(BankDeposit deposit);
        //---- delete
    public void removeBankDeposit(Long id);
}

package com.brest.bank.dao;

import com.brest.bank.domain.BankDepositor;

import java.util.Date;
import java.util.List;

public interface BankDepositorDao {
    //---- close connection
    public void closeConnection();
    //---- get
        //---- all
    public List<BankDepositor> getBankDepositorsSQL();
    public List<BankDepositor> getBankDepositorsCriteria();
        //---- single
    public BankDepositor getBankDepositorByIdGet(Long depositorId);
    public BankDepositor getBankDepositorByIdLoad(Long depositorId);
    public BankDepositor getBankDepositorByIdCriteria(Long depositorId);
    public BankDepositor getBankDepositorByNameSQL(String depositorName);
    public BankDepositor getBankDepositorByNameCriteria(String depositorName);
    public BankDepositor getBankDepositorMinAmount();
    public BankDepositor getBankDepositorByIdDepositMinAmount(Long id);
    public BankDepositor getBankDepositorMaxAmount();
    public BankDepositor getBankDepositorByIdDepositMaxAmount(Long id);
    //---- list of entity
    public List<BankDepositor> getBankDepositorByIdDepositCriteria(Long id);
    public List<BankDepositor> getBankDepositorByIdDepositBetweenDateDeposit(Long id, Date startDate, Date endDate);
    public List<BankDepositor> getBankDepositorByIdDepositBetweenDateReturnDeposit(Long id, Date startDate, Date endDate);
    public List<BankDepositor> getBankDepositorBetweenDateDeposit(Date startDate, Date endDate);
    public List<BankDepositor> getBankDepositorBetweenDateReturnDeposit(Date startDate, Date endDate);
    public List<BankDepositor> getBankDepositorByIdDepositByMarkReturn(Long id, Integer mark);
    public List<BankDepositor> getBankDepositorByMarkReturn(Integer mark);
    public List<BankDepositor> getBankDepositorBetweenAmountDeposit(Integer start, Integer end);
    //---- list object aggregation and grouping
    public BankDepositor getBankDepositorSumAll();
    public BankDepositor getBankDepositorByIdDepositSum(Long id);
    public BankDepositor getBankDepositorBetweenDateDepositSum(Date startDate, Date endDate);
    public BankDepositor getBankDepositorBetweenDateReturnDepositSum(Date startDate, Date endDate);
    //---- CRUD:
        //---- create
    public void addBankDepositor(Long depositId, BankDepositor depositor);
        //---- update
    public void updateBankDepositor(BankDepositor depositor);
        //---- delete
    public void removeBankDepositor(Long id);

}

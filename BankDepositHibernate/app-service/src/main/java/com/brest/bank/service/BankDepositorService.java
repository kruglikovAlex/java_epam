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

    public BankDepositor getBankDepositorByIdDepositMinAmount(Long id);
    public BankDepositor getBankDepositorByIdDepositMaxAmount(Long id);
        //--- list of entity
    public List<BankDepositor> getBankDepositorsByIdDeposit(Long id);
    public List<BankDepositor> getBankDepositorsByIdDepositBetweenDateDeposit(Long id, Date startDate, Date endDate);
    public List<BankDepositor> getBankDepositorsByIdDepositBetweenDateReturnDeposit(Long id, Date startDate, Date endDate);

    public List<BankDepositor> getBankDepositorsBetweenDateDeposit(Date startDate, Date endDate);
    public List<BankDepositor> getBankDepositorsBetweenDateReturnDeposit(Date startDate, Date endDate);

    public List<BankDepositor> getBankDepositorsByIdDepositByMarkReturn(Long id, Integer mark);
    public List<BankDepositor> getBankDepositorsByMarkReturn(Integer mark);
    public List<BankDepositor> getBankDepositorsBetweenAmountDeposit(Integer start, Integer end);
        //---- object aggregation and grouping
    public BankDepositor getBankDepositorSumAll();
    public BankDepositor getBankDepositorByIdDepositSum(Long id);
    public BankDepositor getBankDepositorBetweenDateDepositSum(Date startDate, Date endDate);
    public BankDepositor getBankDepositorBetweenDateReturnDepositSum(Date startDate, Date endDate);
    //--- CRUD:
        //--- create
    public void addBankDepositor(Long depositId, BankDepositor depositor);
        //--- update
    public void updateBankDepositor(BankDepositor depositor);
        //--- delete
    public void removeBankDepositor(Long id);
}

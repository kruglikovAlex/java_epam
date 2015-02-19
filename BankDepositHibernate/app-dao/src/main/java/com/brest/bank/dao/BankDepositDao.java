package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BankDepositDao {
    //--- close connection
    public void closeConnection();
    //---- get
    public List<BankDeposit> getBankDepositsSQL();
    public List<BankDeposit> getBankDepositsCriteria();
    public BankDeposit getBankDepositByIdGet(Long depositId);
    public BankDeposit getBankDepositByIdLoad(Long depositId);
    public BankDeposit getBankDepositByIdCriteria(Long id);
    public BankDeposit getBankDepositByNameSQL(String depositName);
    public BankDeposit getBankDepositByNameCriteria(String depositName);
    public List<BankDeposit> getBankDepositByCurrencyCriteria(String currency);
    public BankDeposit getBankDepositByNameByNaturalIdCriteria(String name);
    public List<BankDeposit> getBankDepositBetweenMinTermCriteria(Integer minTerm, Integer maxTerm);
    public List<BankDeposit> getBankDepositsBetweenDateDeposit(Date startDate, Date endDate);
    public List<BankDeposit> getBankDepositsBetweenDateReturnDeposit(Date startDate, Date endDate);
    public List<Map> getBankDepositByCurrencyWithDepositors(String currency);
    public List<Map> getBankDepositsBetweenDateDepositWithDepositors(Date startDate, Date endDate);
    public List<Map> getBankDepositByNameBetweenDateDepositWithDepositors(String name,Date startDate, Date endDate);
    public List<Map> getBankDepositsBetweenDateReturnDepositWithDepositors(Date startDate, Date endDate);
    public List<Map> getBankDepositByNameBetweenDateReturnDepositWithDepositors(String name,Date startDate, Date endDate);
    //---- CRUD:
        //---- create
    public void addBankDeposit(BankDeposit deposit);
        //---- update
    public void updateBankDeposit(BankDeposit deposit);
        //---- delete
    public void removeBankDeposit(Long id);
}

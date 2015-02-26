package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BankDepositDao {
    //--- close connection
    public void closeConnection();
    //---- get
        //---- all
    public List<BankDeposit> getBankDepositsSQL();
    public List<BankDeposit> getBankDepositsCriteria();
        //---- single
    public BankDeposit getBankDepositByIdGet(Long depositId);
    public BankDeposit getBankDepositByIdLoad(Long depositId);
    public BankDeposit getBankDepositByIdCriteria(Long id);
    public BankDeposit getBankDepositByNameSQL(String depositName);
    public BankDeposit getBankDepositByNameCriteria(String depositName);
    public BankDeposit getBankDepositByNameByNaturalIdCriteria(String name);
        //---- list of entity
    public List<BankDeposit> getBankDepositsByCurrencyCriteria(String currency);
    public List<BankDeposit> getBankDepositsByInterestRateCriteria(Integer rate);
    public List<BankDeposit> getBankDepositsBetweenMinTermCriteria(Integer fromTerm, Integer toTerm);
    public List<BankDeposit> getBankDepositsBetweenInterestRateCriteria(Integer startRate, Integer endRate);
    public List<BankDeposit> getBankDepositsBetweenDateDeposit(Date startDate, Date endDate);
    public List<BankDeposit> getBankDepositsBetweenDateReturnDeposit(Date startDate, Date endDate);
        //---- list object aggregation and grouping
    public List<Map> getBankDepositByCurrencyWithDepositors(String currency);
    public List<Map> getBankDepositByCurrencyBetweenDateDepositWithDepositors(String currency,Date startDate, Date endDate);
    public List<Map> getBankDepositByCurrencyBetweenDateReturnDepositWithDepositors(String currency,Date startDate, Date endDate);

    public List<Map> getBankDepositByNameWithDepositors(String name);
    public List<Map> getBankDepositByNameBetweenDateDepositWithDepositors(String name,Date startDate, Date endDate);
    public List<Map> getBankDepositByNameBetweenDateReturnDepositWithDepositors(String name,Date startDate, Date endDate);

    public List<Map> getBankDepositByInterestRateWithDepositors(Integer rate);
    public List<Map> getBankDepositBetweenInterestRateWithDepositors(Integer startRate, Integer endRate);
    public List<Map> getBankDepositBetweenInterestRateBetweenDateDepositWithDepositors(Integer startRate, Integer endRate,Date startDate, Date endDate);
    public List<Map> getBankDepositBetweenInterestRateBetweenDateReturnDepositWithDepositors(Integer startRate, Integer endRate,Date startDate, Date endDate);

    public List<Map> getBankDepositsBetweenDateDepositWithDepositors(Date startDate, Date endDate);
    public List<Map> getBankDepositsBetweenDateReturnDepositWithDepositors(Date startDate, Date endDate);
    //---- CRUD:
        //---- create
    public void addBankDeposit(BankDeposit deposit);
        //---- update
    public void updateBankDeposit(BankDeposit deposit);
        //---- delete
    public void removeBankDeposit(Long id);
}

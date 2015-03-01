package com.brest.bank.service;

import com.brest.bank.domain.BankDeposit;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BankDepositService {
    //--- get:
        //--- all
    public List<BankDeposit> getBankDeposits();
        //--- single
    public BankDeposit getBankDepositById(Long id);
    public BankDeposit getBankDepositByName(String depositName);
        //--- list of entity
    public List<BankDeposit> getBankDepositsBetweenDateDeposit(Date startDate, Date endDate);
        //---- list object aggregation and grouping entity BankDepositor
    public List<Map> getBankDepositByCurrencyAll(String currency);
    public List<Map> getBankDepositByCurrencyBetweenDateDepositAll(String currency,Date startDate, Date endDate);
    public List<Map> getBankDepositByCurrencyBetweenDateReturnDepositAll(String currency,Date startDate, Date endDate);

    public List<Map> getBankDepositByNameAll(String name);
    public List<Map> getBankDepositByNameBetweenDateDepositAll(String name,Date startDate, Date endDate);
    public List<Map> getBankDepositByNameBetweenDateReturnDepositAll(String name,Date startDate, Date endDate);

    public List<Map> getBankDepositByInterestRateAll(Integer rate);
    public List<Map> getBankDepositBetweenInterestRateAll(Integer startRate, Integer endRate);
    public List<Map> getBankDepositBetweenInterestRateBetweenDateDepositAll(Integer startRate, Integer endRate,Date startDate, Date endDate);
    public List<Map> getBankDepositBetweenInterestRateBetweenDateReturnDepositAll(Integer startRate, Integer endRate,Date startDate, Date endDate);

    public List<Map> getBankDepositsBetweenDateDepositAll(Date startDate, Date endDate);
    public List<Map> getBankDepositsBetweenDateReturnDepositAll(Date startDate, Date endDate);
    //--- CRUD:
        //--- create
    public void addBankDeposit(BankDeposit deposit);
        //--- update
    public void updateBankDeposit(BankDeposit deposit);
        //--- delete
    public void removeBankDeposit(Long id);
}

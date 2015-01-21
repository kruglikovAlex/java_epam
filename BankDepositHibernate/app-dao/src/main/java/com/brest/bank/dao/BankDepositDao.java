package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import org.hibernate.Session;

import java.util.List;

public interface BankDepositDao {

    //---- init
    public void startConnection();
    public void closeConnection();
    public Session getCurrentSession();
    //---- get
    public List<BankDeposit> getBankDepositsSQL();
    public List<BankDeposit> getBankDepositsCriteria();
    public BankDeposit getBankDepositByIdGet(Long depositId);
    public BankDeposit getBankDepositByIdLoad(Long depositId);
    public BankDeposit getBankDepositByIdCriteria(Long id);
    public BankDeposit getBankDepositByNameObject(String depositName);
    public BankDeposit getBankDepositByNameList(String depositName);

    //---- CRUD:
        //---- create
    public void addBankDeposit(BankDeposit deposit);
        //---- update
    public void updateBankDeposit(BankDeposit deposit);
        //---- delete
    public void removeBankDeposit(Long id);
}

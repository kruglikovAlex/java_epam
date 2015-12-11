package com.brest.bank.service;

import com.brest.bank.domain.BankDeposit;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BankDepositService {

    public void setSession(SessionFactory sessionFactory);

    /**
     * Get all Bank Deposits
     *
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     */
    List<BankDeposit> getBankDepositsCriteria();

    /**
     * Get Bank Deposit by ID
     *
     * @param id Long - id of the Bank Deposit to return
     * @return BankDeposit with the specified id from the database
     */
    BankDeposit getBankDepositByIdCriteria(Long id);

    /**
     * Get Bank Deposit by NAME
     *
     * @param depositName String - name of the Bank Deposit to return
     * @return BankDeposit with the specified depositName from the database
     */
    BankDeposit getBankDepositByNameCriteria(String depositName);

    /**
     * Get Bank Deposit by Currency
     *
     * @param currency String - currency of the Bank Deposits to return
     * @return List<BankDeposit> - a list containing all of the Bank Deposits with the specified
     * currency in the database
     */
    List<BankDeposit> getBankDepositsByCurrencyCriteria(String currency);


}

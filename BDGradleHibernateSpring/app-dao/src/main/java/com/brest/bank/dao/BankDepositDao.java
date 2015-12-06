package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BankDepositDao {

    /**
     * Set Hibernate session factory
     * @param sessionFactory SessionFactory
     */
    public void setSession(SessionFactory sessionFactory);

    /**
     * Get all Bank Deposits
     * @return List<BankDeposit>
     */
    public List<BankDeposit> getBankDepositsCriteria();

    /**
     * Get Bank Depodit by ID
     * @param id Long
     * @return BankDeposit
     */
    public BankDeposit getBankDepositByIdCriteria(Long id);

    /**
     * Get Bank Deposit by NAME
     * @param depositName String
     * @return BankDeposit
     */
    public BankDeposit getBankDepositByNameCriteria(String depositName);

    /**
     * Get Bank Deposit by Currency
     * @param currency String
     * @return List<BankDeposit>
     */
    public List<BankDeposit> getBankDepositsByCurrencyCriteria(String currency);

    /**
     * Get Bank Deposits by INTEREST RATE
     * @param rate Integer
     * @return List<BankDeposit>
     */
    public List<BankDeposit> getBankDepositsByInterestRateCriteria(Integer rate);

    /**
     * Get Bank Deposits from-to MIN TERM values
     * @param fromTerm Integer
     * @param toTerm Integer
     * @return List<BankDeposit>
     */
    public List<BankDeposit> getBankDepositsFromToMinTermCriteria(Integer fromTerm, Integer toTerm);

    /**
     * Get Bank Deposits from-to INTEREST RATE values
     * @param startRate Integer
     * @param endRate Integer
     * @return List<BankDeposit>
     */
    public List<BankDeposit> getBankDepositsFromToInterestRateCriteria(Integer startRate, Integer endRate);

    /**
     * Get Bank Deposits from-to Date Deposit values
     * @param startDate Date
     * @param endDate Date
     * @return List<BankDeposit>
     */
    public List<BankDeposit> getBankDepositsFromToDateDeposit(Date startDate, Date endDate);

    /**
     * Get Bank Deposits from-to Date Return Deposit values
     * @param startDate Date
     * @param endDate Date
     * @return List<BankDeposit>
     */
    public List<BankDeposit> getBankDepositsFromToDateReturnDeposit(Date startDate, Date endDate);

    /**
     * Get Bank Deposits by NAME with depositors
     * @param name String
     * @return List<Map>
     */
    public List<Map> getBankDepositByNameWithDepositors(String name);

    /**
     * Get Bank Deposits by NAME with depositors from-to Date Deposit values
     * @param name String
     * @param startDate Date
     * @param endDate Date
     * @return List<Map>
     */
    public List<Map> getBankDepositByNameFromToDateDepositWithDepositors(String name,Date startDate, Date endDate);

    /**
     * Get Bank Deposits by NAME with depositors from-to Date Return Deposit values
     * @param name
     * @param startDate
     * @param endDate
     * @return List<Map>
     */
    public List<Map> getBankDepositByNameFromToDateReturnDepositWithDepositors(String name,Date startDate, Date endDate);
    /*
    public List<Map> getBankDepositByIdWithDepositors(String name);
    public List<Map> getBankDepositByIdFromToDateDepositWithDepositors(String name,Date startDate, Date endDate);
    public List<Map> getBankDepositByIdFromToDateReturnDepositWithDepositors(String name,Date startDate, Date endDate);

    public List<Map> getBankDepositsFromToDateDepositWithDepositors(Date startDate, Date endDate);
    public List<Map> getBankDepositsFromToDateReturnDepositWithDepositors(Date startDate, Date endDate);

    public List<Map> getBankDepositByCurrencyWithDepositors(String currency);
    public List<Map> getBankDepositByCurrencyFromToDateDepositWithDepositors(String currency,Date startDate, Date endDate);
    public List<Map> getBankDepositByCurrencyFromToDateReturnDepositWithDepositors(String currency,Date startDate, Date endDate);
     */

    /**
     * Adding Bank Deposit
     * @param deposit BankDeposit
     */
    public void addBankDeposit(BankDeposit deposit);

    /**
     * Updating Bank Deposit
     * @param deposit BankDeposit
     */
    public void updateBankDeposit(BankDeposit deposit);

    /**
     * Deleting Bank Deposit by ID
     * @param id Long
     */
    public void deleteBankDeposit(Long id);
}

package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;

public interface BankDepositDao {
    /**
     * Set Hibernate session factory
     * @param sessionFactory
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
     * @param currency
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
     * @param startRate
     * @param endRate
     * @return
     */
    public List<BankDeposit> getBankDepositsFromToInterestRateCriteria(Integer startRate, Integer endRate);
    /*
    public List<BankDeposit> getBankDepositsBetweenDateDeposit(Date startDate, Date endDate);
    public List<BankDeposit> getBankDepositsBetweenDateReturnDeposit(Date startDate, Date endDate);
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

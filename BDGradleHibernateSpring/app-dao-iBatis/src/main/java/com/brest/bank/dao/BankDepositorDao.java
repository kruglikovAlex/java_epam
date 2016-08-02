package com.brest.bank.dao;

import com.brest.bank.domain.BankDepositor;

import java.util.Date;
import java.util.List;

public interface BankDepositorDao {

    /**
     * Get all Bank Depositors
     *
     * @return List<BankDepositor> - a list containing all of the Bank Depositors in the database
     */
    List<BankDepositor> getBankDepositorsCriteria();

    /**
     * Get all Bank Depositors from-to Date Deposit
     *
     * @param start Date - start value of the date deposit (startDate < endDate)
     * @param end Date - end value of the date deposit (startDate < endDate)
     * @return List<BankDepositors> a list of all bank depositors with the specified task`s date deposit
     */
    //List<BankDepositor> getBankDepositorsFromToDateDeposit(Date start, Date end);

    /**
     * Get all Bank Depositors from-to Date return Deposit
     *
     * @param start Date - start value of the date return deposit (startDate < endDate)
     * @param end Date - end value of the date return deposit (startDate < endDate)
     * @return List<BankDepositors> a list of all bank depositors with the specified task`s date return deposit
     */
    //List<BankDepositor> getBankDepositorsFromToDateReturnDeposit(Date start, Date end);

    /**
     * Get Bank Depositor by ID
     *
     * @param depositorId  Long - id of the Bank Depositor to return
     * @return BankDepositor with the specified id from the database
     */
    BankDepositor getBankDepositorByIdCriteria(Long depositorId);

    /**
     * Get Bank Depositors by ID Bank Deposit
     *
     * @param depositId  Long - id of the Bank Deposit
     * @return List<BankDepositor> with the specified id bank deposit from the database
     */
    //List<BankDepositor> getBankDepositorByIdDepositCriteria(Long depositId);

    /**
     * Get Bank Depositor by Name
     *
     * @param depositorName  String - name of the Bank Depositor to return
     * @return BankDepositor with the specified id from the database
     */
    BankDepositor getBankDepositorByNameCriteria(String depositorName);

    /**
     * Adding Bank Depositor
     *
     * @param depositId Long - id of the Bank Deposit
     * @param depositor BankDepositor - Bank Depositor to be inserted to the database
     */
    void addBankDepositor(Long depositId, BankDepositor depositor);

    /**
     * Updating Bank Depositor
     *
     * @param depositor BankDepositor - Bank Depositor to be stored in the database
     */
    void updateBankDepositor(BankDepositor depositor);

    /**
     * Deleting Bank Depositor by ID
     *
     * @param id Long - id of the Bank Depositor to be removed
     */
    void removeBankDepositor(Long id);

    /**
     *
     * @return
     */
    Integer rowCount();
}

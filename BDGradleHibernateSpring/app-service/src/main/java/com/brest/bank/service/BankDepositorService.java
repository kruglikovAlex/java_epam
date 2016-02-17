package com.brest.bank.service;

import com.brest.bank.dao.BankDepositorDao;
import com.brest.bank.domain.BankDepositor;

import java.util.Date;
import java.util.List;

public interface BankDepositorService {

    /**
     * Get all Bank Depositors
     *
     * @return List<BankDepositor> - a list containing all of the Bank Depositors in the database
     */
    List<BankDepositor> getBankDepositors();

    /**
     * Get all Bank Depositors from-to Date Deposit
     *
     * @param start Date - start value of the date deposit (startDate < endDate)
     * @param end Date - end value of the date deposit (startDate < endDate)
     * @return List<BankDepositors> a list of all bank depositors with the specified task`s date deposit
     */
    List<BankDepositor> getBankDepositorsFromToDateDeposit(Date start, Date end);

    /**
     * Get all Bank Depositors from-to Date Return Deposit
     *
     * @param start Date - start value of the date return deposit (startDate < endDate)
     * @param end Date - end value of the date deposit (startDate < endDate)
     * @return List<BankDepositors> a list of all bank depositors with the specified task`s date return deposit
     */
    List<BankDepositor> getBankDepositorsFromToDateReturnDeposit(Date start, Date end);

    /**
     * Get Bank Depositor by ID
     *
     * @param depositorId  Long - id of the Bank Depositor to return
     * @return BankDepositor with the specified id from the database
     */
    BankDepositor getBankDepositorById(Long depositorId);

    /**
     * Get all Bank Depositors by id Bank Deposit
     *
     * @param depositId Long - id of the Bank Deposit
     * @return List<BankDepositor> - a list containing all of the Bank Depositors in the database by id Deposit
     */
    List<BankDepositor> getBankDepositorByIdDeposit(Long depositId);

    /**
     * Get Bank Depositor by Name
     *
     * @param depositorName  String - name of the Bank Depositor to return
     * @return BankDepositor with the specified id from the database
     */
    BankDepositor getBankDepositorByName(String depositorName);

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
}

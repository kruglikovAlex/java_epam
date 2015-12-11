package com.brest.bank.dao;

import com.brest.bank.domain.BankDepositor;

import java.util.List;

public interface BankDepositorDao {

    /**
     * Get all Bank Depositors
     *
     * @return List<BankDepositor> - a list containing all of the Bank Depositors in the database
     */
    public List<BankDepositor> getBankDepositorsCriteria();

    /**
     * Get Bank Depositor by ID
     *
     * @param depositorId  Long - id of the Bank Depositor to return
     * @return BankDepositor with the specified id from the database
     */
    public BankDepositor getBankDepositorByIdCriteria(Long depositorId);

    /**
     * Get Bank Depositor by Name
     *
     * @param depositorName  String - name of the Bank Depositor to return
     * @return BankDepositor with the specified id from the database
     */
    public BankDepositor getBankDepositorByNameCriteria(String depositorName);

    /**
     * Adding Bank Depositor
     *
     * @param depositId Long - id of the Bank Deposit
     * @param depositor BankDepositor - Bank Depositor to be inserted to the database
     */
    public void addBankDepositor(Long depositId, BankDepositor depositor);

    /**
     * Updating Bank Depositor
     *
     * @param depositor BankDepositor - Bank Depositor to be stored in the database
     */
    public void updateBankDepositor(BankDepositor depositor);

    /**
     * Deleting Bank Depositor by ID
     *
     * @param id Long - id of the Bank Depositor to be removed
     */
    public void removeBankDepositor(Long id);
}

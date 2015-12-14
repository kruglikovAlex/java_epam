package com.brest.bank.service;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DataFixture {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Get new Bank Deposit with fixed parameters
     *
     * @return BankDeposit with fixed parameters for tests
     */
    public static BankDeposit getNewDeposit(){
        BankDeposit deposit = new BankDeposit(null,"depositName1",12,1000,"usd",4,"conditions1",new HashSet());
        return deposit;
    }

    /**
     * Get an exists Bank Deposit with fixed parameters
     *
     * @param id Long - id of the Bank Deposit to return
     * @return BankDeposit with fixed parameters for tests
     */
    public static BankDeposit getExistDeposit(Long id){
        BankDeposit deposit = new BankDeposit(id,"depositName1",12,1000,"usd",4,"conditions1",new HashSet());
        return deposit;
    }

    /**
     * Get all Bank Deposits
     *
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     */
    public static List<BankDeposit> getDeposits(){
        List<BankDeposit> deposits = new ArrayList<BankDeposit>();
        deposits.add(getNewDeposit());
        return deposits;
    }

    /**
     * Get new Bank Depositor with fixed parameters
     *
     * @return BankDepositor with fixed parameters for tests
     */
    public static BankDepositor getNewDepositor() throws ParseException{
        BankDepositor depositor = new BankDepositor(null,"depositorName1",
                dateFormat.parse("2015-01-01"),1000,100,100,dateFormat.parse("2015-09-09"),0,null);
        return depositor;
    }

    /**
     * Get an exists Bank Depositor with fixed parameters
     *
     * @param id Long - id of the Bank Depositor to return
     * @return BankDepositor with fixed parameters for tests
     */
    public static BankDepositor getExistDepositor(Long id) throws ParseException{
        BankDepositor depositor = new BankDepositor(id,"depositorName1",
                dateFormat.parse("2015-01-01"),1000,100,100,dateFormat.parse("2015-09-09"),0,null);
        return depositor;
    }

    /**
     * Get all Bank Depositors
     *
     * @return List<BankDepositor> - a list containing all of the Bank Depositors in the database
     */
    public static List<BankDepositor> getDepositors() throws ParseException{
        List<BankDepositor> depositors = new ArrayList<BankDepositor>();
        depositors.add(getNewDepositor());
        return depositors;
    }
}
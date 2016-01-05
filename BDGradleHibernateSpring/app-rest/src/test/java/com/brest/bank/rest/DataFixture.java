package com.brest.bank.rest;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataFixture {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Get new Bank Deposit with fixed parameters
     *
     * @return BankDeposit with fixed parameters for tests
     */
    public static BankDeposit getNewDeposit(){
        return new BankDeposit(null,"depositName1",12,1000,"usd",4,"conditions1",new HashSet());
    }

    /**
     * Get an exists Bank Deposit with fixed parameters
     *
     * @param id Long - id of the Bank Deposit to return
     * @return BankDeposit with fixed parameters for tests
     */
    public static BankDeposit getExistDeposit(Long id){
        return new BankDeposit(id,"depositName1",12,1000,"usd",4,"conditions1",new HashSet());
    }

    /**
     * Get a Bank Deposit with null parameters
     *
     * @return BankDeposit with null parameters
     */
    public static BankDeposit getNullDeposit() {
        return null;
    }

    /**
     * Get empty Bank Deposit
     *
     * @return BankDeposit empty
     */
    public static BankDeposit getEmptyDeposit() {
        return new BankDeposit();
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
     * Get exist Bank Deposits
     *
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     */
    public static List<BankDeposit> getExistDeposits(){
        List<BankDeposit> deposits = new ArrayList<BankDeposit>();
        deposits.add(getExistDeposit(1L));
        return deposits;
    }

    /**
     * Get exist Bank Deposit with all Bank Depositors
     *
     * @param id Long - id of Bank Deposit
     * @param idd Long - id of Bank Depositor
     * @returne Map - a bank deposit with a report on all relevant
     * bank depositors
     * @throws ParseException
     */
    public static Map getExistDepositAllDepositors(Long id, Long idd) throws ParseException{
        BankDeposit deposit = getExistDeposit(id);
        BankDepositor depositor = getExistDepositor(idd);
        Map<String, Object> list = new HashMap<String, Object>(11);
            list.put("depositId", deposit.getDepositId());
            list.put("depositName", deposit.getDepositName());
            list.put("depositMinTerm", deposit.getDepositMinTerm());
            list.put("depositMinAmount", deposit.getDepositMinAmount());
            list.put("depositCurrency", deposit.getDepositCurrency());
            list.put("depositInterestRate", deposit.getDepositInterestRate());
            list.put("depositAddConditions", deposit.getDepositAddConditions());
            list.put("sumAmount", depositor.getDepositorAmountDeposit());
            list.put("sumPlusAmount", depositor.getDepositorAmountPlusDeposit());
            list.put("sumMinusAmount", depositor.getDepositorAmountMinusDeposit());
            list.put("numDepositors", 1);
        return list;
    }

    /**
     * Get all Bank Deposits with all Bank Depositors
     *
     * @return List<Map> - a list of bank deposits with a report on all relevant
     * bank depositors
     * @throws ParseException
     */
    public static List<Map> getExistAllDepositsAllDepositors() throws ParseException{
        List<Map> deposits = new ArrayList<Map>();
        deposits.add(getExistDepositAllDepositors(1L, 1L));
        return deposits;
    }

    /**
     * Get new Bank Depositor with fixed parameters
     *
     * @return BankDepositor with fixed parameters for tests
     */
    public static BankDepositor getNewDepositor() throws ParseException{
        return new BankDepositor(null,"depositorName1",
                dateFormat.parse("2015-01-01"),1000,100,100,dateFormat.parse("2015-09-09"),0,null);
    }

    /**
     * Get a Bank Depositor with null parameters
     *
     * @return BankDepositor with null parameters
     */
    public static BankDepositor getNullDepositor() {
        return null;
    }

    /**
     * Get empty Bank Depositor
     *
     * @return BankDepositor empty
     */
    public static BankDepositor getEmptyDepositor() {
        return new BankDepositor(null,null,null,0,0,0,null,0,null);
    }

    /**
     * Get an exists Bank Depositor with fixed parameters
     *
     * @param id Long - id of the Bank Depositor to return
     * @return BankDepositor with fixed parameters for tests
     */
    public static BankDepositor getExistDepositor(Long id) throws ParseException{
        return new BankDepositor(id,"depositorName1",
                dateFormat.parse("2015-01-01"),1000,100,100,dateFormat.parse("2015-09-09"),0,null);
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

    /**
     * Get exist Bank Depositors
     *
     * @return List<BankDepositor> - a list containing all of the Bank Depositors in the database
     * @throws ParseException
     */
    public static List<BankDepositor> getExistDepositors() throws ParseException{
        List<BankDepositor> depositors = new ArrayList<BankDepositor>();
            depositors.add(getExistDepositor(1L));
            depositors.add(getExistDepositor(2L));
        return depositors;
    }
}
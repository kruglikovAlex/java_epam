package com.brest.bank.service;

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
     * Get a Bank Deposit with null parameters
     *
     * @return BankDeposit with null parameters
     */
    public static BankDeposit getNullDeposit() {
        BankDeposit deposit = new BankDeposit(null,null,null,null,null,null,null,null);
        return deposit;
    }

    /**
     * Get empty Bank Deposit
     *
     * @return BankDeposit empty
     */
    public static BankDeposit getEmptyDeposit() {
        BankDeposit deposit = new BankDeposit();
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

    public static List<BankDepositor> getExistDepositors() throws ParseException{
        List<BankDepositor> depositors = new ArrayList<BankDepositor>();
            depositors.add(getExistDepositor(1L));
            depositors.add(getExistDepositor(2L));
        return depositors;
    }
}
package com.brest.bank.client;

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
        return new BankDeposit(id,"depositName"+id,12,1000,"usd",4,"conditions"+id,new HashSet());
    }

    /**
     * Get an exists Bank Deposits
     *
     * @return BankDeposit with fixed parameters for tests
     */
    public static BankDeposit[] getExistDeposits(){
        BankDeposit[] deposits = new BankDeposit[2];
        deposits[0] = getExistDeposit(1L);
        deposits[1] = getExistDeposit(2L);
        return deposits;
    }

    /**
     * Get an exists Bank Deposits
     *
     * @return BankDeposit with fixed parameters for tests
     */
    public static ArrayList<BankDeposit> getExistListDeposits(){
        ArrayList<BankDeposit> deposits = new ArrayList<>();
        deposits.add(getExistDeposit(1L));
        deposits.add(getExistDeposit(2L));

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
    public static LinkedHashMap getExistDepositAllDepositors(Long id, Long idd) throws ParseException{
        BankDeposit deposit = getExistDeposit(id);
        BankDepositor depositor = getExistDepositor(idd);
        LinkedHashMap<String, Object> list = new LinkedHashMap<>(11);
            list.put("depositId", deposit.getDepositId());
            list.put("depositName", deposit.getDepositName());
            list.put("depositMinTerm", deposit.getDepositMinTerm());
            list.put("depositMinAmount", deposit.getDepositMinAmount());
            list.put("depositCurrency", deposit.getDepositCurrency());
            list.put("depositInterestRate", deposit.getDepositInterestRate());
            list.put("depositAddConditions", deposit.getDepositAddConditions());
            list.put("depositorCount", 1);
            list.put("depositorAmountSum", depositor.getDepositorAmountDeposit());
            list.put("depositorAmountPlusSum", depositor.getDepositorAmountPlusDeposit());
            list.put("depositorAmountMinusSum", depositor.getDepositorAmountMinusDeposit());

        return list;
    }

    /**
     * Get exist Bank Deposit with all Bank Depositors
     *
     * @returne Map - a bank deposit with a report on all relevant
     * bank depositors
     * @throws ParseException
     */
    public static LinkedHashMap[] getExistDepositsWithDepositors() throws ParseException{
        LinkedHashMap[] listMap = new LinkedHashMap[2];
        LinkedHashMap<String, Object> list = new LinkedHashMap<String, Object>(11);
        list.put("depositId", 1L);
        list.put("depositName", "depositName1");
        list.put("depositMinTerm", 12);
        list.put("depositMinAmount", 100);
        list.put("depositCurrency", "usd");
        list.put("depositInterestRate", 4);
        list.put("depositAddConditions", "condition1");
        list.put("depositorCount", 2);
        list.put("depositorAmountSum", 2000);
        list.put("depositorAmountPlusSum", 200);
        list.put("depositorAmountMinusSum", 200);
        listMap[0]=list;
        list = new LinkedHashMap<String, Object>(11);
        list.put("depositId", 2L);
        list.put("depositName", "depositName2");
        list.put("depositMinTerm", 12);
        list.put("depositMinAmount", 100);
        list.put("depositCurrency", "usd");
        list.put("depositInterestRate", 4);
        list.put("depositAddConditions", "condition2");
        list.put("depositorCount", 2);
        list.put("depositorAmountSum", 2000);
        list.put("depositorAmountPlusSum", 200);
        list.put("depositorAmountMinusSum", 200);
        listMap[1]=list;
        return listMap;
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
     * Get an exists Bank Depositor with fixed parameters
     *
     * @param id Long - id of the Bank Depositor to return
     * @return BankDepositor with fixed parameters for tests
     */
    public static BankDepositor getExistDepositor(Long id) throws ParseException{
        return new BankDepositor(id,"depositorName"+id,
                dateFormat.parse("2015-01-01"),1000,100,100,dateFormat.parse("2015-09-09"),0,null);
    }

    /**
     * Get an exists Bank Depositors with fixed parameters
     *
     * @return BankDepositor with fixed parameters for tests
     */
    public static BankDepositor[] getExistDepositors() throws ParseException{
        BankDepositor[] depositors = new BankDepositor[3];
        depositors[0] = getExistDepositor(1L);
        depositors[1] = getExistDepositor(2L);
        depositors[2] = getExistDepositor(3L);
        return depositors;
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
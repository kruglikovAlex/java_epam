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
            list.put("depositorCount", 1);
            list.put("depositorAmountSum", depositor.getDepositorAmountDeposit());
            list.put("depositorAmountPlusSum", depositor.getDepositorAmountPlusDeposit());
            list.put("depositorAmountMinusSum", depositor.getDepositorAmountMinusDeposit());

        return list;
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
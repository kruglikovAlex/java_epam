package com.brest.bank.web;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataConfig {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Get new Bank Deposit with fixed parameters
     *
     * @return BankDeposit with fixed parameters for tests
     */
    public static BankDeposit getEmptyDeposit(){
        return new BankDeposit(null,"",0,0,"",0,"",new HashSet());
    }

    /**
     * Get all Bank Deposits
     *
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     */
    public static List<BankDeposit> getEmptyDeposits(){
        List<BankDeposit> deposits = new ArrayList<BankDeposit>();
        deposits.add(getEmptyDeposit());
        return deposits;
    }

    /**
     * Get new Bank Depositor with fixed parameters
     *
     * @return BankDepositor with fixed parameters for tests
     */
    public static BankDepositor getEmptyDepositor() throws ParseException{
        String date = dateFormat.format(Calendar.getInstance().getTime());
        return new BankDepositor(null,"",
                dateFormat.parse(date),0,0,0,
                dateFormat.parse(date),0,null);
    }

    /**
     * Get all Bank Depositors
     *
     * @return List<BankDepositor> - a list containing all of the Bank Depositors in the database
     */
    public static List<BankDepositor> getEmptyDepositors() throws ParseException{
        List<BankDepositor> depositors = new ArrayList<BankDepositor>();
        depositors.add(getEmptyDepositor());
        return depositors;
    }

    /**
     * Get empty Bank Deposit with all Bank Depositors
     *
     * @returne Map - a bank deposit with a report on all relevant
     * bank depositors
     * @throws ParseException
     */
    public static Map getEmptyDepositAllDepositors() throws ParseException{
        BankDeposit deposit = getEmptyDeposit();
        BankDepositor depositor = getEmptyDepositor();

        Map<String, Object> list = new HashMap<String, Object>(11);
            list.put("depositId", deposit.getDepositId());
            list.put("depositName", deposit.getDepositName());
            list.put("depositMinTerm", deposit.getDepositMinTerm());
            list.put("depositMinAmount", deposit.getDepositMinAmount());
            list.put("depositCurrency", deposit.getDepositCurrency());
            list.put("depositInterestRate", deposit.getDepositInterestRate());
            list.put("depositAddConditions", deposit.getDepositAddConditions());
            list.put("depositorCount", 0);
            list.put("depositorAmountSum", depositor.getDepositorAmountDeposit());
            list.put("depositorAmountPlusSum", depositor.getDepositorAmountPlusDeposit());
            list.put("depositorAmountMinusSum", depositor.getDepositorAmountMinusDeposit());
        return list;
    }

    /**
     * Get all Bank Deposits with all Bank Depositors
     *
     * @return List<Map> - a list of bank deposits with a report on all relevant
     * bank depositors
     * @throws ParseException
     */
    public static List<Map> getEmptyAllDepositsAllDepositors() throws ParseException{
        List<Map> deposits = new ArrayList<Map>();
        deposits.add(getEmptyDepositAllDepositors());
        return deposits;
    }
}
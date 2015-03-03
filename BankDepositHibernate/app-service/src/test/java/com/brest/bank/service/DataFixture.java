package com.brest.bank.service;

import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

public class DataFixture {


    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //--- BankDeposit
    public static BankDeposit getNewDeposit(){
        BankDeposit deposit = new BankDeposit();
        deposit.setDepositName("depositName1");
        deposit.setDepositMinTerm(12);
        deposit.setDepositMinAmount(100);
        deposit.setDepositCurrency("usd");
        deposit.setDepositInterestRate(4);
        deposit.setDepositAddConditions("condition1");
        return deposit;
    }

    public static BankDeposit getExistDeposit(Long id){
        BankDeposit deposit = new BankDeposit();
        deposit.setDepositId(id);
        deposit.setDepositName("depositName1");
        deposit.setDepositMinTerm(12);
        deposit.setDepositMinAmount(100);
        deposit.setDepositCurrency("usd");
        deposit.setDepositInterestRate(4);
        deposit.setDepositAddConditions("condition1");
        return deposit;
    }

    public static BankDeposit getNullDeposit() {
        BankDeposit deposit = new BankDeposit(null,null,null,null,null,null,null);
        return deposit;
    }

    public static BankDeposit getNotNullIdDeposit() {
        BankDeposit deposit = new BankDeposit(7L,null,null,null,null,null,null);
        return deposit;
    }

    public static BankDeposit getEmptyDeposit() {
        BankDeposit deposit = new BankDeposit();
        return deposit;
    }

    public static List<BankDeposit> getDeposits() {
        List<BankDeposit> deposits = new ArrayList<BankDeposit>();
        for (int i = 0; i < 4; i++) {
            BankDeposit deposit = new BankDeposit();
            deposit.setDepositId((long)(i+1));
            deposit.setDepositName("depositName" + i);
            deposit.setDepositMinTerm(12+i);
            deposit.setDepositMinAmount(100+100*i);
            deposit.setDepositCurrency("usd");
            deposit.setDepositInterestRate(4+i);
            deposit.setDepositAddConditions("condition" + i);
            deposits.add(deposit);
        }
        return deposits;
    }

    public static List<Map> getMapDeposits() {
        List<Map> deposits = new ArrayList<Map>();
        for (int i = 0; i < 4; i++) {
            Map<String, Object> list = new HashMap<String, Object>(11);

            list.put("depositId", (long) (i + 1));
            list.put("depositName", "depositName" + i);
            list.put("depositMinTerm", 12 + i);
            list.put("depositMinAmount", 100 * (i + 1));
            list.put("depositCurrency", "usd");
            list.put("depositInterestRate", 4 + i);
            list.put("depositAddConditions", "condition" + i);
            list.put("depositorCount", 2);
            list.put("depositorAmountSum", 200 * (i + 1));
            list.put("depositorAmountPlusSum", 20 * (i + 1));
            list.put("depositorAmountMinusSum", 10 * (i + 1));
            deposits.add(list);
        }
        return deposits;
    }

    public static List<Map> getExitsMapDeposit() {
        List<Map> deposit = new ArrayList<Map>();
            Map<String, Object> list = new HashMap<String, Object>(11);

            list.put("depositId", 1L);
            list.put("depositName", "depositName1");
            list.put("depositMinTerm", 12);
            list.put("depositMinAmount", 100);
            list.put("depositCurrency", "usd");
            list.put("depositInterestRate", 4);
            list.put("depositAddConditions", "condition1");
            list.put("depositorCount", 2);
            list.put("depositorAmountSum", 200);
            list.put("depositorAmountPlusSum", 20);
            list.put("depositorAmountMinusSum", 10);
            deposit.add(list);

        return deposit;
    }

    public static List<BankDepositor> getDepositors() throws ParseException{
        String date, returnDate;
        List<BankDepositor> depositors = new ArrayList<BankDepositor>();
        for (int i = 1; i < 5; i++) {
            date = "2014-12-0"+i;
            returnDate = "2014-12-0"+(i+1);

            BankDepositor depositor = new BankDepositor();
            depositor.setDepositorName("depositorName" + i);
            depositor.setDepositorDateDeposit(dateFormat.parse(date));
            depositor.setDepositorAmountDeposit(1000+i);
            depositor.setDepositorAmountPlusDeposit(10 * (i + 1));
            depositor.setDepositorAmountMinusDeposit(10 * (i + 1));
            depositor.setDepositorDateReturnDeposit(dateFormat.parse(returnDate));
            depositor.setDepositorMarkReturnDeposit(0);
            depositors.add(depositor);
        }
        return depositors;
    }

    public static BankDepositor getExistDepositor(Long id) throws ParseException{
        BankDepositor depositor = new BankDepositor();
        depositor.setDepositorId(id);
        depositor.setDepositorName("depositorName1");
        depositor.setDepositorDateDeposit(dateFormat.parse("2014-12-01"));
        depositor.setDepositorAmountDeposit(10000);
        depositor.setDepositorAmountPlusDeposit(1000);
        depositor.setDepositorAmountMinusDeposit(11000);
        depositor.setDepositorDateReturnDeposit(dateFormat.parse("2014-12-01"));
        depositor.setDepositorMarkReturnDeposit(0);
        return depositor;
    }

    public static BankDepositor getDepositorSum(){
        BankDepositor depositor = new BankDepositor();
        depositor.setDepositorAmountDeposit(10000);
        depositor.setDepositorAmountPlusDeposit(1000);
        depositor.setDepositorAmountMinusDeposit(11000);
        return depositor;
    }

    public static BankDepositor getNewDepositor() throws ParseException {
        BankDepositor depositor = new BankDepositor();
        depositor.setDepositorName("depositorName2");
        depositor.setDepositorDateDeposit(dateFormat.parse("2014-12-01"));
        depositor.setDepositorAmountDeposit(10000);
        depositor.setDepositorAmountPlusDeposit(1000);
        depositor.setDepositorAmountMinusDeposit(100);
        depositor.setDepositorDateReturnDeposit(dateFormat.parse("2014-12-01"));
        depositor.setDepositorMarkReturnDeposit(0);
        return depositor;
    }
}

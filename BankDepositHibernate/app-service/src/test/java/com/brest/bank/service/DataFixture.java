package com.brest.bank.service;

import java.util.ArrayList;
import java.text.SimpleDateFormat;

import java.util.List;

import com.brest.bank.domain.BankDeposit;

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
}

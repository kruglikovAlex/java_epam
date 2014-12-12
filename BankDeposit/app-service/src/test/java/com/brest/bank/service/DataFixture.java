package com.brest.bank.service;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.List;

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
    	deposits.add(new BankDeposit());
    	return deposits;
    }
    
    //--- BankDepositor
    public static BankDepositor getNewDepositor() throws ParseException {
        BankDepositor depositor = new BankDepositor();
        depositor.setDepositorName("depositorName1");
        depositor.setDepositorIdDeposit(1L);
        depositor.setDepositorDateDeposit(dateFormat.parse("2014-12-01"));
        depositor.setDepositorAmountDeposit(10000);
        depositor.setDepositorAmountPlusDeposit(1000);
        depositor.setDepositorAmountMinusDeposit(100);
        depositor.setDepositorDateReturnDeposit(dateFormat.parse("2014-12-01"));
        depositor.setDepositorMarkReturnDeposit(0);
        return depositor;
    }
    
    public static BankDepositor getExistDepositor(Long id) throws ParseException{
    	BankDepositor depositor = new BankDepositor();
    	depositor.setDepositorId(id);
        depositor.setDepositorName("depositorName1");
        depositor.setDepositorIdDeposit(1L);
        depositor.setDepositorDateDeposit(dateFormat.parse("2014-12-01"));
        depositor.setDepositorAmountDeposit(10000);
        depositor.setDepositorAmountPlusDeposit(1000);
        depositor.setDepositorAmountMinusDeposit(11000);
        depositor.setDepositorDateReturnDeposit(dateFormat.parse("2014-12-01"));
        depositor.setDepositorMarkReturnDeposit(0);
        return depositor;
    }

    public static BankDepositor getExistDepositorIdDeposit(Long id, Long idD) throws ParseException{
        BankDepositor depositor = new BankDepositor();
        depositor.setDepositorId(id);
        depositor.setDepositorName("depositorName1");
        depositor.setDepositorIdDeposit(idD);
        depositor.setDepositorDateDeposit(dateFormat.parse("2014-12-01"));
        depositor.setDepositorAmountDeposit(10000);
        depositor.setDepositorAmountPlusDeposit(1000);
        depositor.setDepositorAmountMinusDeposit(11000);
        depositor.setDepositorDateReturnDeposit(dateFormat.parse("2014-12-01"));
        depositor.setDepositorMarkReturnDeposit(0);
        return depositor;
    }
    
	public static BankDepositor getNullDepositor() {
        BankDepositor depositor = new BankDepositor(null,null,null,null,null,null,null,null,null);
        return depositor;
    }

    public static BankDepositor getNotNullIdDepositor() {
        BankDepositor depositor = new BankDepositor(7L,null,null,null,null,null,null,null,null);
        return depositor;
    }

    public static BankDepositor getEmptyDepositor() {
        BankDepositor depositor = new BankDepositor();
        return depositor;
    }
    
    public static List<BankDepositor> getDepositors() throws ParseException{
        List<BankDepositor> depositors = new ArrayList<BankDepositor>();
        depositors.add(new BankDepositor());
        return depositors;
    }

    public static List<BankDepositor> getExistDepositors() throws ParseException{
        List<BankDepositor> depositors = new ArrayList<BankDepositor>();
        depositors.add(getExistDepositorIdDeposit(1L,1L));
        depositors.add(getExistDepositorIdDeposit(2L,1L));
        return depositors;
    }
}

package com.brest.bank.web;

import com.brest.bank.domain.BankDepositor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFixture {

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static List<Map> getDepositWithAllDepositors(){
        Map<String, Object> list = new HashMap<String, Object>(12);
        list.put("depositId", 1L);
        list.put("depositName", "name1");
        list.put("depositMinTerm", 12);
        list.put("depositMinAmount", 100);
        list.put("depositCurrency", "usd");
        list.put("depositInterestRate", 4);
        list.put("depositAddConditions", "condition1");
        list.put("depId",1L);
        list.put("sumAmount",100);
        list.put("sumPlusAmount",10);
        list.put("sumMinusAmount",10);
        list.put("numDepositors",1);
        List<Map> depositsAllDepositors = new ArrayList<Map>();
        depositsAllDepositors.add(list);
        return depositsAllDepositors;
    }

    public static List<BankDepositor> getExistDepositors() throws ParseException{
        List<BankDepositor> depositors = new ArrayList<BankDepositor>();
        depositors.add(new BankDepositor(1L,"depositorName1",1L,dateFormat.parse("2014-02-02"),100,10,10,dateFormat.parse("2014-09-09"),0));
        return depositors;
    }
}

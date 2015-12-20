package com.brest.bank.dao;

import com.brest.bank.domain.BankDepositor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataFixture {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Create new BankDepositor
     * @param name String
     * @return BankDepositor
     * @throws ParseException
     */
    public static BankDepositor getNewDepositor(String name) throws ParseException {
        BankDepositor depositor = new BankDepositor();
            depositor.setDepositorName(name);
            depositor.setDepositorDateDeposit(dateFormat.parse("2015-11-02"));
            depositor.setDepositorAmountDeposit(1000);
            depositor.setDepositorAmountPlusDeposit(10);
            depositor.setDepositorAmountMinusDeposit(10);
            depositor.setDepositorDateReturnDeposit(dateFormat.parse("2015-12-03"));
            depositor.setDepositorMarkReturnDeposit(0);
        return depositor;
    }

    public static Set<BankDepositor> getDepositors() throws ParseException{
        Set<BankDepositor> depositors = new HashSet<BankDepositor>();
        depositors.add(getNewDepositor("depositorName"));
        return depositors;
    }
}

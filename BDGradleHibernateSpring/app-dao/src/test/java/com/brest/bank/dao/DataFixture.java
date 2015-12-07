package com.brest.bank.dao;

import com.brest.bank.domain.BankDepositor;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
}

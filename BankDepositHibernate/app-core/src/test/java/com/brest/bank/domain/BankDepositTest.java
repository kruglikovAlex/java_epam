package com.brest.bank.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BankDepositTest {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    BankDeposit deposit;

    @Before
    public void setUp() throws Exception {
        deposit = new BankDeposit();
    }

    @Test
    public void testGetDepositId() throws Exception {
        Long id = (long)(Math.random()*10);
        LOGGER.debug("testGetDepositId({})", id);
        deposit.setDepositId(id);

        assertEquals(id, deposit.getDepositId());
    }

    @Test
    public void testGetDepositName() throws Exception {
        LOGGER.debug("testGetDepositName({})", "Life is Fine");
        deposit.setDepositName("Life is Fine");

        assertEquals("Life is Fine", deposit.getDepositName());
    }

    @Test
    public void testGetDepositMinTerm() throws Exception {
        LOGGER.debug("testGetDepositMinTerm, (month-{})", 12);
        deposit.setDepositMinTerm(12);

        assertTrue(12 == deposit.getDepositMinTerm());
    }

    @Test
    public void testGetDepositMinAmount() throws Exception {
        LOGGER.debug("testGetDepositMinAmount({})", 100);
        deposit.setDepositMinAmount(100);

        assertTrue(100 == deposit.getDepositMinAmount());
    }

    @Test
    public void testGetDepositCurrency() throws Exception {
        LOGGER.debug("testGetDepositCurrency({})", "usd");
        deposit.setDepositCurrency("usd");

        assertEquals("usd", deposit.getDepositCurrency());
    }

    @Test
    public void testGetDepositInterestRate() throws Exception {
        LOGGER.debug("testGetDepositInterestRate({}%)", 9);
        deposit.setDepositInterestRate(9);

        assertTrue(9 == deposit.getDepositInterestRate());
    }

    @Test
    public void testGetDepositAddConditions() throws Exception {
        LOGGER.debug("testGetDepositAddConditions({})", "conditions");
        deposit.setDepositAddConditions("conditions");

        assertEquals("conditions", deposit.getDepositAddConditions());
    }

    @Test
    public void testGetDepositors() throws Exception {
        Set depositors = new HashSet<BankDepositor>();
        depositors.add(new BankDepositor(1L,"name",dateFormat.parse("2015-01-01"),100,10,10,dateFormat.parse("2015-10-10"),0));

        LOGGER.debug("testGetDepositors({})",depositors);
        deposit.setDepositors(depositors);

        assertEquals(depositors, deposit.getDepositors());
    }

    @Test
    public void testToString() throws Exception {
        LOGGER.debug("testToString({})",deposit);

        assertEquals("BankDeposit: { depositId=null, depositName=null, depositMinTerm=null, " +
                "depositMinAmount=null, depositCurrency=null, depositInterestRate=null, " +
                "depositAddConditions=null}",deposit.toString());
    }
}
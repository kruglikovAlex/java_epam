package com.brest.bank.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class BankDepositorTest {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    BankDepositor depositors;

    @Before
    public void setUp() throws Exception {
        depositors = new BankDepositor();
    }

    @Test
    public void testGetDepositorId() throws Exception {
        Long id = (long)(Math.random()*10);
        LOGGER.debug("testGetDepositorId({})", id);
        depositors.setDepositorId(id);

        assertEquals(id, depositors.getDepositorId());
    }

    @Test
    public void testGetDepositorName() throws Exception {
        LOGGER.debug("testGetDepositorName({})","Wiessman MF5 Roadster");
        depositors.setDepositorName("Wiessman MF5 Roadster");

        assertEquals("Wiessman MF5 Roadster", depositors.getDepositorName());
    }

    @Test
    public void testGetDepositorDateDeposit() throws Exception {
        LOGGER.debug("testGetDepositorDateDeposit({})","2015-01-01");
        depositors.setDepositorDateDeposit(dateFormat.parse("2015-01-01"));

        assertEquals("2015-01-01",dateFormat.format(depositors.getDepositorDateDeposit()));
        assertEquals(dateFormat.parse("2015-01-01"),depositors.getDepositorDateDeposit());
        assertTrue(depositors.getDepositorDateDeposit().compareTo(dateFormat.parse("2015-01-01")) == 0);
    }

    @Test
    public void testGetDepositorAmountDeposit() throws Exception {
        LOGGER.debug("testGetDepositorAmountDeposit({})", 100);
        depositors.setDepositorAmountDeposit(100);

        assertTrue(100 == depositors.getDepositorAmountDeposit());
    }

    @Test
    public void testGetDepositorAmountPlusDeposit() throws Exception {
        LOGGER.debug("testGetDepositorAmountPlusDeposit({})", 10);
        depositors.setDepositorAmountPlusDeposit(10);

        assertTrue(10 == depositors.getDepositorAmountPlusDeposit());
    }

    @Test
    public void testGetDepositorAmountMinusDeposit() throws Exception {
        LOGGER.debug("testGetDepositorAmountMinusDeposit({})", 20);
        depositors.setDepositorAmountMinusDeposit(20);

        assertTrue(20 == depositors.getDepositorAmountMinusDeposit());
    }

    @Test
    public void testGetDepositorDateReturnDeposit() throws Exception {
        LOGGER.debug("testGetDepositorDateReturnDeposit({})","2015-01-01");
        depositors.setDepositorDateReturnDeposit(dateFormat.parse("2015-01-01"));

        assertEquals("2015-01-01",dateFormat.format(depositors.getDepositorDateReturnDeposit()));
        assertEquals(dateFormat.parse("2015-01-01"),depositors.getDepositorDateReturnDeposit());
        assertTrue(depositors.getDepositorDateReturnDeposit().compareTo(dateFormat.parse("2015-01-01")) == 0);
    }

    @Test
    public void testGetDepositorMarkReturnDeposit() throws Exception {
        LOGGER.debug("testGetDepositorMarkReturnDeposit({})", 0);
        depositors.setDepositorMarkReturnDeposit(0);

        assertTrue(0 == depositors.getDepositorMarkReturnDeposit());
    }

    @Test
    public void testGetBankDeposit() throws Exception {
        BankDeposit deposit = new BankDeposit();
        LOGGER.debug("testGetBankDeposit({})", deposit);
        depositors.setDeposit(deposit);

        assertEquals(deposit, depositors.getDeposit()) ;
    }

    @Test
    public void testToStringNullDepositor() throws Exception {
        LOGGER.debug("testToString({})",depositors.toString());

        assertEquals("BankDepositor: { depositorId=null, depositorName=null, " +
                "depositorDateDeposit=null, depositorAmountDeposit=null, " +
                "depositorAmountPlusDeposit=null, depositorAmountMinusDeposit=null, " +
                "depositorDateReturnDeposit=null, depositorMarkReturnDeposit=null}",depositors.toString());
    }

    @Test
    public void testToString() throws Exception {
        depositors = new BankDepositor(1L,"name",dateFormat.parse("2015-01-01"),100,10,30,dateFormat.parse("2015-10-10"),0);
        LOGGER.debug("testToString({})", depositors.toString());

        assertEquals("BankDepositor: { depositorId=1, depositorName=name, " +
                "depositorDateDeposit=2015-01-01, depositorAmountDeposit=100, " +
                "depositorAmountPlusDeposit=10, depositorAmountMinusDeposit=30, " +
                "depositorDateReturnDeposit=2015-10-10, depositorMarkReturnDeposit=0}",depositors.toString());
    }
}
package com.brest.bank.dao;

import com.brest.bank.domain.BankDepositor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})
public class BankDepositorDaoImplTest {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String ERROR_EMPTY_BD = "Data Base is empty";
    private static final String ERROR_SIZE = "Size can not be 0";
    private static final String ERROR_NULL = "The parameter can not be NULL";

    @Autowired
    BankDepositorDao depositorDao;
    Object result;
    Integer sizeBefore = 0, sizeAfter = 0;
    private BankDepositor depositor;
    private List<BankDepositor> depositors;

    @Before
    public void setUp() throws Exception {
        depositor = new BankDepositor();
        depositors = new ArrayList<BankDepositor>();
    }

    @After
    public void endUp() throws Exception {
    }

    @Test
    public void testGetBankDepositorsCriteria() throws Exception {
        depositors = depositorDao.getBankDepositorsCriteria();
        LOGGER.debug("depositors.size()= {}", depositors.size());

        assertFalse(depositors.isEmpty());
        assertNotNull(depositors);
    }

    @Test
    public void testGetBankDepositorsFromToDateDeposit() throws ParseException {
        Date startDate = dateFormat.parse("2015-11-02");
        Date endDate = dateFormat.parse("2015-12-04");

        depositors = depositorDao.getBankDepositorsFromToDateDeposit(startDate, endDate);
        LOGGER.debug("depositors.size()= {}", depositors.size());

        assertFalse(depositors.isEmpty());
        assertNotNull(depositors);
    }

    @Test
    public void testGetBankDepositorsFromToDateReturnDeposit() throws ParseException {
        Date startDate = dateFormat.parse("2015-11-02");
        Date endDate = dateFormat.parse("2015-12-04");

        depositors = depositorDao.getBankDepositorsFromToDateReturnDeposit(startDate, endDate);
        LOGGER.debug("depositors.size()= {}", depositors.size());

        assertFalse(depositors.isEmpty());
        assertNotNull(depositors);
    }

    @Test
    public void testGetBankDepositorByIdCriteria() throws Exception {
        depositor = depositorDao.getBankDepositorByIdCriteria(1L);
        LOGGER.debug("depositor = {}", depositor);

        assertEquals("BankDepositor: { depositorId=1, depositorName=depositorName6," +
                " depositorDateDeposit=2015-12-06, depositorAmountDeposit=999, " +
                "depositorAmountPlusDeposit=60, depositorAmountMinusDeposit=60, " +
                "depositorDateReturnDeposit=2015-12-07, depositorMarkReturnDeposit=0}", depositor.toString());
    }

    @Test
    public void testGetBankDepositorByIdDepositCriteria() throws Exception {
        depositors = depositorDao.getBankDepositorByIdDepositCriteria(1L);
        LOGGER.debug("depositors = {}", depositors);

        assertFalse(ERROR_EMPTY_BD, depositors.isEmpty());
        assertThat(ERROR_SIZE, depositors.size(), is(not(0)));
        assertNotNull(ERROR_NULL, depositors);
    }

    @Test
    public void testGetBankDepositorByNameCriteria() throws Exception {
        depositor = depositorDao.getBankDepositorByNameCriteria("depositorName1");
        LOGGER.debug("depositor = {}", depositor);

        assertEquals("BankDepositor: { depositorId=0, depositorName=depositorName1, depositorDateDeposit=2015-12-01, " +
                "depositorAmountDeposit=1001, depositorAmountPlusDeposit=20, " +
                "depositorAmountMinusDeposit=20, depositorDateReturnDeposit=2015-12-02, " +
                "depositorMarkReturnDeposit=0}", depositor.toString());
    }

    @Test
    public void testAddBankDepositor() {
        depositor = depositorDao.getBankDepositorByIdCriteria(1L);
        depositor.setDepositorId(null);
        depositor.setDepositorName("New");
        depositorDao.addBankDepositor(1L, depositor);

        assertNotEquals(depositors.size(), depositorDao.getBankDepositorsCriteria().size());

    }
}
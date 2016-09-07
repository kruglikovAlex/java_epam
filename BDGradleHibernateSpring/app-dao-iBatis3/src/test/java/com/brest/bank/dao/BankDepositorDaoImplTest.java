package com.brest.bank.dao;

import com.brest.bank.domain.BankDepositor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
@ContextConfiguration(locations = {"classpath:/spring-dao-ibatis-test.xml"})
public class BankDepositorDaoImplTest {

    private static final Logger LOGGER = LogManager.getLogger(BankDepositDaoImplTest.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String ERROR_EMPTY_BD = "Data Base is empty";
    private static final String ERROR_SIZE = "Size can not be 0";
    private static final String ERROR_NULL = "The parameter can not be NULL";
    private static final String ERROR_EMPTY_RESULT = "The result can not be empty";
    public Object result;
    public Integer sizeBefore = 0, sizeAfter = 0;

    @Autowired
    private BankDepositorDao depositorDao;

    @Autowired
    private BankDepositDao depositDao;

    private BankDepositor depositor;
    private List<BankDepositor> depositors;

    @Before
    public void setUp() throws Exception {
        depositor = new BankDepositor();
        depositors = new ArrayList<BankDepositor>();
        depositors = depositorDao.getBankDepositorsCriteria();
    }

    @Test
    public void testGetBankDepositorsCriteria() throws Exception {
        LOGGER.debug("depositors.size()= {}", depositors.size());

        assertFalse(ERROR_EMPTY_BD,depositors.isEmpty());
        assertThat(ERROR_SIZE,depositors.size(), is(not(0)));
        assertNotNull(ERROR_NULL,depositors);
    }

    @Test
    public void testGetBankDepositorByIdCriteria() throws Exception {
        depositor = depositorDao.getBankDepositorByIdCriteria(1L);
        LOGGER.debug("depositor = {}", depositor);

        assertNotNull(ERROR_NULL,depositor);
        assertEquals("BankDepositor: { depositorId=1, depositorName=depositorName6, depositorDateDeposit=2015-12-06, " +
                "depositorAmountDeposit=999, depositorAmountPlusDeposit=60, depositorAmountMinusDeposit=60, " +
                "depositorDateReturnDeposit=2015-12-07, depositorMarkReturnDeposit=0}",depositor.toString());
    }

    @Test
    public void testGetBankDepositorByNameCriteria() throws Exception {
        depositor = depositorDao.getBankDepositorByNameCriteria("depositorName2");
        LOGGER.debug("depositor = {}", depositor);

        assertNotNull(ERROR_NULL,depositor);
        assertEquals("BankDepositor: { depositorId=2, depositorName=depositorName2, depositorDateDeposit=2015-12-02, " +
                "depositorAmountDeposit=1002, depositorAmountPlusDeposit=30, depositorAmountMinusDeposit=30, " +
                "depositorDateReturnDeposit=2015-12-03, depositorMarkReturnDeposit=0}",depositor.toString());
    }

    @Test
    public void testGetBankDepositorByIdDepositCriteria() throws Exception {
        depositors = depositorDao.getBankDepositorByIdDepositCriteria(1L);
        LOGGER.debug("depositors = {}", depositors);

        assertFalse(ERROR_EMPTY_BD,depositors.isEmpty());
        assertThat(ERROR_SIZE,depositors.size(), is(not(0)));
        assertNotNull(ERROR_NULL,depositors);
    }

    @Test
    public void testGetBankDepositorsFromToDateDeposit() throws ParseException {
        Date startDate = dateFormat.parse("2015-11-02");
        Date endDate = dateFormat.parse("2015-12-04");

        depositors = depositorDao.getBankDepositorsFromToDateDeposit(startDate,endDate);
        LOGGER.debug("depositors.size()= {}", depositors.size());

        assertFalse(depositors.isEmpty());
        assertNotNull(depositors);
    }

    @Test
    public void testGetBankDepositorsFromToDateReturnDeposit() throws ParseException{
        Date startDate = dateFormat.parse("2015-11-02");
        Date endDate = dateFormat.parse("2015-12-04");

        depositors = depositorDao.getBankDepositorsFromToDateReturnDeposit(startDate, endDate);
        LOGGER.debug("depositors.size()= {}", depositors.size());

        assertFalse(depositors.isEmpty());
        assertNotNull(depositors);
    }

    @Test
    public void testAddBankDepositor() throws Exception {
        depositor = new BankDepositor();
            depositor.setDepositorName("newName");
            depositor.setDepositorDateDeposit(dateFormat.parse("2015-12-02"));
            depositor.setDepositorAmountDeposit(1000);
            depositor.setDepositorAmountPlusDeposit(10);
            depositor.setDepositorAmountMinusDeposit(10);
            depositor.setDepositorDateReturnDeposit(dateFormat.parse("2015-12-03"));
            depositor.setDepositorMarkReturnDeposit(0);
            depositor.setDepositId(0L);
        LOGGER.debug("new deposit - {}",depositor);

        sizeBefore = depositorDao.rowCount();
        LOGGER.debug("size before add = {}",sizeBefore);

        LOGGER.debug("start added new deposit - {}",depositor);
        depositorDao.addBankDepositor(0L,depositor);
        LOGGER.debug("new deposit was added - {}",depositor);

        sizeAfter = depositorDao.rowCount();
        LOGGER.debug("size after add = {}",sizeAfter);

        assertTrue(sizeAfter == sizeBefore+1);
        assertTrue(depositorDao.getBankDepositorByIdDepositCriteria(0L)!=null);
        LOGGER.debug("depositors by id deposit: {}",depositorDao.getBankDepositorByIdDepositCriteria(0L));
        LOGGER.debug("deposit with new depositor: {}",depositDao.getBankDepositByIdWithDepositors(0L));
    }

    @Test
    public void testUpdateBankDepositor() throws Exception {
        String testDepositor;

        depositor = depositors.get(4);
        Long id = depositor.getDepositorId();

        testDepositor = depositor.toString();

        depositor.setDepositorName("UpdateDepositorName");
        depositor.setDepositorAmountDeposit(555);
        depositor.setDepositorMarkReturnDeposit(1);
        depositor.setDepositorDateReturnDeposit(dateFormat.parse("2016-08-08"));

        depositorDao.updateBankDepositor(depositor);

        assertNotEquals(depositorDao.getBankDepositorByIdCriteria(id).toString(),testDepositor);
        assertEquals(depositorDao.getBankDepositorByIdCriteria(id).toString(),depositor.toString());
    }

    @Test
    public void testRemoveBankDepositor() throws Exception {
        assertNotNull(depositors);
        assertFalse(depositors.isEmpty());
        sizeBefore = depositors.size();
        LOGGER.debug("size before - {}",sizeBefore);
        depositorDao.removeBankDepositor(3L);
        depositors = depositorDao.getBankDepositorsCriteria();
        LOGGER.debug("size after - {}",depositors.size());
        assertEquals(sizeBefore-1,depositors.size());
    }
}
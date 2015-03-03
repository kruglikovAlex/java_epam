package com.brest.bank.service;

import com.brest.bank.dao.BankDepositorDaoImpl;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.dao.BankDepositorDao;
import com.brest.bank.service.BankDepositorService;

import com.brest.bank.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.easymock.EasyMock;

import org.junit.After;
import org.junit.Before;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import static org.easymock.EasyMock.*;

public class DepositorServiceImplMockTest {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER = LogManager.getLogger();

    private BankDepositorDao depositorDao;
    private BankDepositorServiceImpl depositorService = new BankDepositorServiceImpl();

    @Before
    public void setUp() throws Exception {
        depositorDao = EasyMock.createMock(BankDepositorDaoImpl.class);
        depositorService.setBankDepositorDao(depositorDao);
    }

    @After
    public void clean() {
        reset(depositorDao);
    }

    @Test
    public void testGetDepositors() throws ParseException{
        LOGGER.debug("testGetDepositors() - run");

        List<BankDepositor> depositors = DataFixture.getDepositors();
        LOGGER.info("depositors: {}", depositors);

        expect(depositorDao.getBankDepositorsCriteria()).andReturn(depositors);
        replay(depositorDao);

        List<BankDepositor> resultDepositors  = depositorService.getBankDepositors();
        LOGGER.info("resultDepositors: {}", resultDepositors);
        verify(depositorDao);

        assertEquals(depositors, resultDepositors);
        assertSame(depositors, resultDepositors);
    }

    @Test
    public void testGetBankDepositorById() throws ParseException{
        LOGGER.debug("testGetBankDepositorById() - run");

        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        LOGGER.info("depositor: {}", depositor);

        expect(depositorDao.getBankDepositorByIdCriteria(1L)).andReturn(depositor);
        replay(depositorDao);

        BankDepositor resultDepositor = depositorService.getBankDepositorById(1L);
        LOGGER.info("resultDepositor: {}", resultDepositor);

        verify(depositorDao);

        assertEquals(depositor,resultDepositor);
        assertSame(depositor,resultDepositor);
    }

    @Test
    public void testGetBankDepositorByName() throws ParseException{
        LOGGER.debug("testGetBankDepositorById() - run");

        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        LOGGER.info("depositor: {}", depositor);

        expect(depositorDao.getBankDepositorByNameCriteria("depositorName1")).andReturn(depositor);
        replay(depositorDao);

        BankDepositor resultDepositor = depositorService.getBankDepositorByName("depositorName1");
        LOGGER.info("resultDepositor: {}", resultDepositor);

        verify(depositorDao);

        assertEquals(depositor,resultDepositor);
        assertSame(depositor,resultDepositor);
    }

    @Test
    public void testGetBankDepositorBetweenDateDeposit() throws ParseException{
        LOGGER.debug("testGetBankDepositorBetweenDateDeposit() - run");

        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-05");
        List<BankDepositor> depositors = DataFixture.getDepositors();
        LOGGER.info("depositors: {}", depositors);

        expect(depositorDao.getBankDepositorBetweenDateDeposit(startDate,endDate)).andReturn(depositors);
        replay(depositorDao);

        List<BankDepositor> resultDepositors = depositorService.getBankDepositorsBetweenDateDeposit(startDate,endDate);
        LOGGER.info("resultDepositors: {}", resultDepositors);

        verify(depositorDao);

        assertEquals(depositors,resultDepositors);
        assertSame(depositors,resultDepositors);
    }

    @Test
    public void testGetBankDepositorBetweenDateReturnDeposit() throws ParseException{
        LOGGER.debug("testGetBankDepositorBetweenDateReturnDeposit() - run");

        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-05");
        List<BankDepositor> depositors = DataFixture.getDepositors();
        LOGGER.info("depositors: {}", depositors);

        expect(depositorDao.getBankDepositorBetweenDateReturnDeposit(startDate, endDate)).andReturn(depositors);
        replay(depositorDao);

        List<BankDepositor> resultDepositors = depositorService.getBankDepositorsBetweenDateReturnDeposit(startDate, endDate);
        LOGGER.info("resultDepositors: {}", resultDepositors);

        verify(depositorDao);

        assertEquals(depositors,resultDepositors);
        assertSame(depositors,resultDepositors);
    }

    @Test
    public void testGetBankDepositorByIdDepositMinAmount() throws ParseException{
        LOGGER.debug("testGetBankDepositorByIdDepositMinAmount() - run");

        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        LOGGER.info("depositor: {}", depositor);

        expect(depositorDao.getBankDepositorByIdDepositMinAmount(1L)).andReturn(depositor);
        replay(depositorDao);

        BankDepositor resultDepositor = depositorService.getBankDepositorByIdDepositMinAmount(1L);
        LOGGER.info("resultDepositor: {}", resultDepositor);

        verify(depositorDao);

        assertEquals(depositor,resultDepositor);
        assertSame(depositor,resultDepositor);
    }

    @Test
    public void testGetBankDepositorsByIdDeposit() throws ParseException{
        LOGGER.debug("testGetBankDepositorsByIdDeposit() - run");

        List<BankDepositor> depositors = DataFixture.getDepositors();
        LOGGER.info("depositors: {}", depositors);

        expect(depositorDao.getBankDepositorByIdDepositCriteria(1L)).andReturn(depositors);
        replay(depositorDao);

        List<BankDepositor> resultDepositors = depositorService.getBankDepositorsByIdDeposit(1L);
        LOGGER.info("resultDepositors: {}", resultDepositors);

        verify(depositorDao);

        assertEquals(depositors,resultDepositors);
        assertSame(depositors,resultDepositors);
    }

    @Test
    public void testGetBankDepositorByIdDepositMaxAmount() throws ParseException{
        LOGGER.debug("testGetBankDepositorByIdDepositMaxAmount() - run");

        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        LOGGER.info("depositor: {}", depositor);

        expect(depositorDao.getBankDepositorByIdDepositMaxAmount(1L)).andReturn(depositor);
        replay(depositorDao);

        BankDepositor resultDepositor = depositorService.getBankDepositorByIdDepositMaxAmount(1L);
        LOGGER.info("resultDepositor: {}", resultDepositor);

        verify(depositorDao);

        assertEquals(depositor,resultDepositor);
        assertSame(depositor,resultDepositor);
    }

    @Test
    public void testGetBankDepositorsByIdDepositBetweenDateDeposit() throws ParseException{
        LOGGER.debug("testGetBankDepositorsByIdDepositBetweenDateDeposit() - run");

        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-05");
        List<BankDepositor> depositors = DataFixture.getDepositors();
        LOGGER.info("depositors: {}", depositors);

        expect(depositorDao.getBankDepositorByIdDepositBetweenDateDeposit(1L,startDate, endDate)).andReturn(depositors);
        replay(depositorDao);

        List<BankDepositor> resultDepositors = depositorService.getBankDepositorsByIdDepositBetweenDateDeposit(1L,startDate, endDate);
        LOGGER.info("resultDepositors: {}", resultDepositors);

        verify(depositorDao);

        assertEquals(depositors,resultDepositors);
        assertSame(depositors,resultDepositors);
    }

    @Test
    public void testGetBankDepositorsByIdDepositBetweenDateReturnDeposit() throws ParseException{
        LOGGER.debug("testGetBankDepositorsByIdDepositBetweenDateReturnDeposit() - run");

        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-05");
        List<BankDepositor> depositors = DataFixture.getDepositors();
        LOGGER.info("depositors: {}", depositors);

        expect(depositorDao.getBankDepositorByIdDepositBetweenDateReturnDeposit(1L, startDate, endDate)).andReturn(depositors);
        replay(depositorDao);

        List<BankDepositor> resultDepositors = depositorService.getBankDepositorsByIdDepositBetweenDateReturnDeposit(1L, startDate, endDate);
        LOGGER.info("resultDepositors: {}", resultDepositors);

        verify(depositorDao);

        assertEquals(depositors,resultDepositors);
        assertSame(depositors,resultDepositors);
    }

    @Test
    public void testGetBankDepositorsByIdDepositByMarkReturn() throws ParseException{
        LOGGER.debug("testGetBankDepositorsByIdDepositByMarkReturn() - run");

        List<BankDepositor> depositors = DataFixture.getDepositors();
        LOGGER.info("depositors: {}", depositors);

        expect(depositorDao.getBankDepositorByIdDepositByMarkReturn(1L,0)).andReturn(depositors);
        replay(depositorDao);

        List<BankDepositor> resultDepositors = depositorService.getBankDepositorsByIdDepositByMarkReturn(1L,0);
        LOGGER.info("resultDepositors: {}", resultDepositors);

        verify(depositorDao);

        assertEquals(depositors,resultDepositors);
        assertSame(depositors,resultDepositors);
    }

    @Test
    public void testGetBankDepositorsByMarkReturn() throws ParseException{
        LOGGER.debug("testGetBankDepositorsByMarkReturn() - run");

        List<BankDepositor> depositors = DataFixture.getDepositors();
        LOGGER.info("depositors: {}", depositors);

        expect(depositorDao.getBankDepositorByMarkReturn(0)).andReturn(depositors);
        replay(depositorDao);

        List<BankDepositor> resultDepositors = depositorService.getBankDepositorsByMarkReturn(0);
        LOGGER.info("resultDepositors: {}", resultDepositors);

        verify(depositorDao);

        assertEquals(depositors,resultDepositors);
        assertSame(depositors,resultDepositors);
    }

    @Test
    public void testGetBankDepositorSumAll() throws ParseException{
        LOGGER.debug("testGetBankDepositorSumAll() - run");

        BankDepositor depositor = DataFixture.getDepositorSum();
        LOGGER.info("depositor: {}", depositor);
        List<BankDepositor> depositors = DataFixture.getDepositors();
        LOGGER.info("depositors: {}", depositors);

        expect(depositorDao.getBankDepositorsCriteria()).andReturn(depositors);

        expect(depositorDao.getBankDepositorSumAll()).andReturn(depositor);
        replay(depositorDao);

        BankDepositor resultDepositor = depositorService.getBankDepositorSumAll();
        LOGGER.info("resultDepositor: {}", resultDepositor);

        verify(depositorDao);

        assertEquals(depositor,resultDepositor);
        assertSame(depositor,resultDepositor);
    }

    @Test
    public void testGetBankDepositorByIdDepositSum() throws ParseException{
        LOGGER.debug("testGetBankDepositorByIdDepositSum() - run");

        BankDepositor depositor = DataFixture.getDepositorSum();
        LOGGER.info("depositor: {}", depositor);
        List<BankDepositor> depositors = DataFixture.getDepositors();
        LOGGER.info("depositors: {}", depositors);

        expect(depositorDao.getBankDepositorByIdDepositCriteria(1L)).andReturn(depositors);

        expect(depositorDao.getBankDepositorByIdDepositSum(1L)).andReturn(depositor);
        replay(depositorDao);

        BankDepositor resultDepositor = depositorService.getBankDepositorByIdDepositSum(1L);
        LOGGER.info("resultDepositor: {}", resultDepositor);

        verify(depositorDao);

        assertEquals(depositor,resultDepositor);
        assertSame(depositor,resultDepositor);
    }

    @Test
    public void testGetBankDepositorBetweenDateDepositSum() throws ParseException{
        LOGGER.debug("testGetBankDepositorBetweenDateDepositSum() - run");

        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-05");
        BankDepositor depositor = DataFixture.getDepositorSum();
        LOGGER.info("depositor: {}", depositor);
        List<BankDepositor> depositors = DataFixture.getDepositors();
        LOGGER.info("depositors: {}", depositors);

        expect(depositorDao.getBankDepositorsCriteria()).andReturn(depositors);

        expect(depositorDao.getBankDepositorBetweenDateDepositSum(startDate,endDate)).andReturn(depositor);
        replay(depositorDao);

        BankDepositor resultDepositor = depositorService.getBankDepositorBetweenDateDepositSum(startDate,endDate);
        LOGGER.info("resultDepositor: {}", resultDepositor);

        verify(depositorDao);

        assertEquals(depositor,resultDepositor);
        assertSame(depositor,resultDepositor);
    }

    @Test
    public void testGetBankDepositorBetweenDateReturnDepositSum() throws ParseException{
        LOGGER.debug("testGetBankDepositorBetweenDateReturnDepositSum() - run");

        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-05");
        BankDepositor depositor = DataFixture.getDepositorSum();
        LOGGER.info("depositor: {}", depositor);
        List<BankDepositor> depositors = DataFixture.getDepositors();
        LOGGER.info("depositors: {}", depositors);

        expect(depositorDao.getBankDepositorsCriteria()).andReturn(depositors);

        expect(depositorDao.getBankDepositorBetweenDateReturnDepositSum(startDate,endDate)).andReturn(depositor);
        replay(depositorDao);

        BankDepositor resultDepositor = depositorService.getBankDepositorBetweenDateReturnDepositSum(startDate,endDate);
        LOGGER.info("resultDepositor: {}", resultDepositor);

        verify(depositorDao);

        assertEquals(depositor,resultDepositor);
        assertSame(depositor,resultDepositor);
    }

    @Test
    public void testGetBankDepositorsBetweenAmountDeposit() throws ParseException{
        LOGGER.debug("testGetBankDepositorsBetweenAmountDeposit() - run");

        List<BankDepositor> depositors = DataFixture.getDepositors();
        LOGGER.info("depositors: {}", depositors);

        expect(depositorDao.getBankDepositorBetweenAmountDeposit(100, 300)).andReturn(depositors);
        replay(depositorDao);

        List<BankDepositor> resultDepositors = depositorService.getBankDepositorsBetweenAmountDeposit(100, 300);

        verify(depositorDao);

        assertEquals(depositors,resultDepositors);
        assertSame(depositors,resultDepositors);
    }

    @Test
    public void testAddBankDepositor() throws ParseException{
        LOGGER.debug("testAddBankDepositor() - run");

        BankDepositor depositor = DataFixture.getNewDepositor();
        LOGGER.info("depositor: {}", depositor);

        depositorDao.getBankDepositorByNameCriteria(depositor.getDepositorName());
        expectLastCall().andReturn(null);

        depositorDao.addBankDepositor(1L, depositor);
        expectLastCall();

        replay(depositorDao);

        depositorService.addBankDepositor(1L, depositor);
        verify(depositorDao);
    }

    @Test
    public void testUpdateBankDeposit() throws ParseException{
        LOGGER.debug("testUpdateBankDeposit() - run");

        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        LOGGER.info("depositor: {}", depositor);

        expect(depositorDao.getBankDepositorByIdCriteria(depositor.getDepositorId())).andReturn(depositor);

        depositorDao.updateBankDepositor(depositor);
        expectLastCall();

        replay(depositorDao);

        depositorService.updateBankDepositor(depositor);
        verify(depositorDao);
    }

    @Test
    public void testRemoveBankDeposit() throws ParseException{
        LOGGER.debug("testRemoveBankDeposit() - run");

        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        LOGGER.info("depositor: {}", depositor);

        expect(depositorDao.getBankDepositorByIdCriteria(depositor.getDepositorId())).andReturn(depositor);

        depositorDao.removeBankDepositor(1L);
        expectLastCall();

        replay(depositorDao);

        depositorService.removeBankDepositor(1L);
        verify(depositorDao);
    }
}

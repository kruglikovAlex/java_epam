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

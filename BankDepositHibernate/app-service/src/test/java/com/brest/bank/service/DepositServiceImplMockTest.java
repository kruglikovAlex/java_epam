package com.brest.bank.service;

import com.brest.bank.dao.BankDepositDaoImpl;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.dao.BankDepositDao;
import com.brest.bank.service.BankDepositService;

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

public class DepositServiceImplMockTest {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER = LogManager.getLogger();

    private BankDepositDao depositDao;
    private BankDepositServiceImpl depositService = new BankDepositServiceImpl();

    @Before
    public void setUp() throws Exception {
        depositDao = EasyMock.createMock(BankDepositDaoImpl.class);
        depositService.setBankDepositDao(depositDao);
    }

    @After
    public void clean() {
        reset(depositDao);
    }

    @Test
    public void testGetDeposits() {
        LOGGER.debug("testGetDeposits() - run");
        List<BankDeposit> deposits = DataFixture.getDeposits();

        expect(depositDao.getBankDepositsCriteria()).andReturn(deposits);
        replay(depositDao);

        List<BankDeposit> resultDeposits  = depositService.getBankDeposits();

        verify(depositDao);

        assertEquals(deposits, resultDeposits);
        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositById(){
        LOGGER.debug("testGetBankDepositById() - run");
        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        LOGGER.info("deposit: {}", deposit);

        expect(depositDao.getBankDepositByIdCriteria(1L)).andReturn(deposit);
        replay(depositDao);

        BankDeposit resultDeposit = depositService.getBankDepositById(1L);
        LOGGER.info("resultDeposit: {}", resultDeposit);

        verify(depositDao);

        assertEquals(deposit, resultDeposit);
        assertSame(deposit, resultDeposit);
    }

    @Test
    public void testGetBankDepositByName(){
        LOGGER.debug("testGetBankDepositByName() - run");
        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        LOGGER.info("deposit: {}", deposit);

        expect(depositDao.getBankDepositByNameCriteria("depositName1")).andReturn(deposit);
        replay(depositDao);

        BankDeposit resultDeposit = depositService.getBankDepositByName("depositName1");
        LOGGER.info("resultDeposit: {}", resultDeposit);

        verify(depositDao);

        assertEquals(deposit, resultDeposit);
        assertSame(deposit, resultDeposit);
    }

    @Test
    public void testGetBankDepositsBetweenDateDeposit() throws ParseException{
        LOGGER.debug("testGetBankDepositsBetweenDateDeposit() - run");
        List<BankDeposit> deposits = DataFixture.getDeposits();
        LOGGER.info("deposits: {}", deposits);

        expect(depositDao.getBankDepositsBetweenDateDeposit(dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-01"))).andReturn(deposits);
        replay(depositDao);

        List<BankDeposit> resultDeposits = depositService.getBankDepositsBetweenDateDeposit(dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-01"));
        LOGGER.info("resultDeposits: {}", resultDeposits);

        verify(depositDao);

        assertEquals(deposits, resultDeposits);
        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testAddBankDeposit(){
        LOGGER.debug("testAddBankDeposit() - run");
        BankDeposit deposit = DataFixture.getNewDeposit();
        LOGGER.info("deposit: {}", deposit);

        depositDao.getBankDepositByNameCriteria(deposit.getDepositName());
        expectLastCall().andReturn(null);

        depositDao.addBankDeposit(deposit);
        expectLastCall();

        replay(depositDao);

        depositService.addBankDeposit(deposit);

        verify(depositDao);
    }

    @Test
    public void testUpdateBankDeposit(){
        LOGGER.debug("testUpdateBankDeposit() - run");
        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        LOGGER.info("deposit: {}", deposit);

        expect(depositDao.getBankDepositByIdCriteria(1L)).andReturn(deposit);

        depositDao.updateBankDeposit(deposit);
        expectLastCall();

        replay(depositDao);

        depositService.updateBankDeposit(deposit);

        verify(depositDao);
    }

    @Test
    public void testRemoveBankDeposit(){
        LOGGER.debug("testUpdateBankDeposit() - run");
        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        LOGGER.info("deposit: {}", deposit);

        expect(depositDao.getBankDepositByIdCriteria(1L)).andReturn(deposit);

        depositDao.removeBankDeposit(1L);
        expectLastCall();

        replay(depositDao);

        depositService.removeBankDeposit(1L);

        verify(depositDao);
    }
}

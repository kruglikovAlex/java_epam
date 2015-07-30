package com.brest.bank.service.UnitilsTest;

import com.brest.bank.dao.BankDepositDao;
import com.brest.bank.dao.BankDepositDaoImpl;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.service.BankDepositServiceImpl;
import com.brest.bank.service.DataFixture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Before;
import org.junit.Test;
import org.unitils.mock.Mock;
import org.unitils.mock.MockUnitils;
import org.unitils.mock.core.MockObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import org.unitils.mock.core.proxy.ProxyInvocation;
import org.unitils.mock.mockbehavior.MockBehavior;
import org.unitils.reflectionassert.ReflectionComparatorMode;

public class DepositServiceImplUnitilsMockTest {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER = LogManager.getLogger();

    private Mock<BankDepositDao> depositDao;
    private BankDepositServiceImpl depositService = new BankDepositServiceImpl();

    @Before
    public void setUp() throws Exception {
        depositDao = new MockObject<BankDepositDao>(BankDepositDaoImpl.class,this);
        depositService.setBankDepositDao(depositDao.getMock());
    }



    @Test
    public void testGetDeposits() {
        LOGGER.debug("testGetDeposits() - run");

        List<BankDeposit> deposits = DataFixture.getDeposits();
        depositDao.returns(deposits).getBankDepositsCriteria();

        List<BankDeposit> resultDeposits = depositService.getBankDeposits();

        depositDao.assertInvoked().getBankDepositsCriteria();
        MockUnitils.assertNoMoreInvocations();
        depositDao.assertNotInvoked().getBankDepositsCriteria();

        assertEquals(deposits, resultDeposits);
        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositById(){
        LOGGER.debug("testGetBankDepositById() - run");

        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        LOGGER.info("deposit: {}", deposit);

        depositDao.returns(deposit).getBankDepositByIdCriteria(1L);

        BankDeposit resultDeposit = depositService.getBankDepositById(1L);
        LOGGER.info("resultDeposit: {}", resultDeposit);

        depositDao.assertInvoked().getBankDepositByIdCriteria(1L);
        depositDao.assertNotInvoked().getBankDepositByIdCriteria(1L);
        MockUnitils.assertNoMoreInvocations();

        assertEquals(deposit, resultDeposit);
        assertSame(deposit, resultDeposit);
    }

    @Test
    public void testAddBankDeposit(){
        LOGGER.debug("testAddBankDeposit() - run");
        BankDeposit deposit = DataFixture.getNewDeposit();
        LOGGER.info("deposit: {}", deposit);

        depositDao.returns(null).getBankDepositByNameCriteria(deposit.getDepositName());

        depositDao.performs(new MockBehavior() {
            public Object execute(ProxyInvocation proxyInvocation) throws Throwable {
                return null;
            }
        }).addBankDeposit(deposit);

        depositDao.returns(null).addBankDeposit(deposit);

        depositService.addBankDeposit(deposit);

        depositDao.assertInvokedInSequence().getBankDepositByNameCriteria(deposit.getDepositName());
        depositDao.assertNotInvoked().getBankDepositByNameCriteria(deposit.getDepositName());
        depositDao.assertInvokedInSequence().addBankDeposit(deposit);
        depositDao.assertNotInvoked().addBankDeposit(deposit);
        MockUnitils.assertNoMoreInvocations();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExistsBankDeposit(){
        LOGGER.debug("testAddBankDeposit() - run");
        BankDeposit deposit = DataFixture.getNewDeposit();
        LOGGER.info("deposit: {}", deposit);

        depositDao.returns(deposit).getBankDepositByNameCriteria(deposit.getDepositName());

        depositDao.performs(new MockBehavior() {
            public Object execute(ProxyInvocation proxyInvocation) throws Throwable {
                return null;
            }
        }).addBankDeposit(deposit);

        depositDao.returns(null).addBankDeposit(deposit);

        depositService.addBankDeposit(deposit);

        depositDao.assertInvokedInSequence().getBankDepositByNameCriteria(deposit.getDepositName());
        depositDao.assertNotInvoked().getBankDepositByNameCriteria(deposit.getDepositName());
        depositDao.assertInvokedInSequence().addBankDeposit(deposit);
        depositDao.assertNotInvoked().addBankDeposit(deposit);
        MockUnitils.assertNoMoreInvocations();
    }
}

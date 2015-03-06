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
    public void testGetBankDepositsBetweenDateDeposit() throws ParseException{
        LOGGER.debug("testGetBankDepositsBetweenDateDeposit() - run");

        List<BankDeposit> deposits = DataFixture.getDeposits();
        LOGGER.info("deposits: {}", deposits);

        depositDao.returns(deposits).getBankDepositsBetweenDateDeposit(dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-01"));

        List<BankDeposit> resultDeposits = depositService.getBankDepositsBetweenDateDeposit(dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-01"));
        LOGGER.info("resultDeposits: {}", resultDeposits);

        depositDao.assertInvoked().getBankDepositsBetweenDateDeposit(dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-01"));
        depositDao.assertNotInvoked().getBankDepositsBetweenDateDeposit(dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-01"));
        MockUnitils.assertNoMoreInvocations();

        assertEquals(deposits, resultDeposits);
        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositByCurrencyAll(){
        LOGGER.debug("testGetBankDepositByCurrencyAll() - run");

        List<Map> deposits = DataFixture.getMapDeposits();
        LOGGER.info("deposits: {}", deposits);

        depositDao.returns(deposits).getBankDepositByCurrencyWithDepositors("usd");

        List<Map> resultDeposits = depositService.getBankDepositByCurrencyAll("usd");

        depositDao.assertInvoked().getBankDepositByCurrencyWithDepositors("usd");
        depositDao.assertNotInvoked().getBankDepositByCurrencyWithDepositors("usd");
        MockUnitils.assertNoMoreInvocations();

        assertEquals(deposits, resultDeposits);
        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositByCurrencyBetweenDateDepositAll() throws ParseException{
        LOGGER.debug("testGetBankDepositByCurrencyBetweenDateDepositAll() - run");
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");

        List<Map> deposits = DataFixture.getMapDeposits();
        LOGGER.info("deposits: {}", deposits);

        depositDao.returns(deposits).getBankDepositByCurrencyBetweenDateDepositWithDepositors("usd", startDate, endDate);

        List<Map> resultDeposits = depositService.getBankDepositByCurrencyBetweenDateDepositAll("usd", startDate, endDate);

        depositDao.assertInvoked().getBankDepositByCurrencyBetweenDateDepositWithDepositors("usd",startDate,endDate);
        depositDao.assertNotInvoked().getBankDepositByCurrencyBetweenDateDepositWithDepositors("usd",startDate,endDate);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositByNameAll(){
        LOGGER.debug("testGetBankDepositByNameAll() - run");

        List<Map> deposit = DataFixture.getExitsMapDeposit();
        LOGGER.info("deposits: {}", deposit);

        depositDao.returns(deposit).getBankDepositByNameWithDepositors("depositName1");

        List<Map> resultDeposit = depositService.getBankDepositByNameAll("depositName1");

        depositDao.assertInvoked().getBankDepositByNameWithDepositors("depositName1");
        depositDao.assertNotInvoked().getBankDepositByNameWithDepositors("depositName1");
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposit, resultDeposit);
    }

    @Test
    public void testGetBankDepositByCurrencyBetweenDateReturnDepositAll() throws ParseException{
        LOGGER.debug("testGetBankDepositByCurrencyBetweenDateDepositAll() - run");
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");

        List<Map> deposits = DataFixture.getMapDeposits();
        LOGGER.info("deposits: {}", deposits);

        depositDao.returns(deposits).getBankDepositByCurrencyBetweenDateReturnDepositWithDepositors("usd", startDate, endDate);

        List<Map> resultDeposits = depositService.getBankDepositByCurrencyBetweenDateReturnDepositAll("usd", startDate, endDate);

        depositDao.assertInvoked().getBankDepositByCurrencyBetweenDateReturnDepositWithDepositors("usd", startDate, endDate);
        depositDao.assertNotInvoked().getBankDepositByCurrencyBetweenDateReturnDepositWithDepositors("usd", startDate, endDate);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositByNameBetweenDateDepositAll() throws ParseException{
        LOGGER.debug("testGetBankDepositByNameBetweenDateDepositAll() - run");
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");

        List<Map> deposit = DataFixture.getExitsMapDeposit();
        LOGGER.info("deposits: {}", deposit);

        depositDao.returns(deposit).getBankDepositByNameBetweenDateDepositWithDepositors("depositName1", startDate, endDate);

        List<Map> resultDeposit = depositService.getBankDepositByNameBetweenDateDepositAll("depositName1", startDate, endDate);

        depositDao.assertInvoked().getBankDepositByNameBetweenDateDepositWithDepositors("depositName1", startDate, endDate);
        depositDao.assertNotInvoked().getBankDepositByNameBetweenDateDepositWithDepositors("depositName1", startDate, endDate);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposit, resultDeposit);
    }

    @Test
    public void testGetBankDepositByNameBetweenDateReturnDepositAll() throws ParseException{
        LOGGER.debug("testGetBankDepositByNameBetweenDateReturnDepositAll() - run");
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");

        List<Map> deposit = DataFixture.getExitsMapDeposit();
        LOGGER.info("deposits: {}", deposit);

        depositDao.returns(deposit).getBankDepositByNameBetweenDateReturnDepositWithDepositors("depositName1", startDate, endDate);

        List<Map> resultDeposit = depositService.getBankDepositByNameBetweenDateReturnDepositAll("depositName1", startDate, endDate);

        depositDao.assertInvoked().getBankDepositByNameBetweenDateReturnDepositWithDepositors("depositName1", startDate, endDate);
        depositDao.assertNotInvoked().getBankDepositByNameBetweenDateReturnDepositWithDepositors("depositName1", startDate, endDate);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposit, resultDeposit);
    }

    @Test
    public void testGetBankDepositBetweenInterestRateAll(){
        LOGGER.debug("testGetBankDepositBetweenInterestRateAll() - run");

        List<Map> deposits = DataFixture.getMapDeposits();
        LOGGER.info("deposits: {}", deposits);

        depositDao.returns(deposits).getBankDepositBetweenInterestRateWithDepositors(4, 7);

        List<Map> resultDeposits = depositService.getBankDepositBetweenInterestRateAll(4,7);

        depositDao.assertInvoked().getBankDepositBetweenInterestRateWithDepositors(4,7);
        depositDao.assertNotInvoked().getBankDepositBetweenInterestRateWithDepositors(4,7);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositByInterestRateAll(){
        LOGGER.debug("testGetBankDepositByInterestRateAll() - run");

        List<Map> deposits = DataFixture.getMapDeposits();
        LOGGER.info("deposits: {}", deposits);

        depositDao.returns(deposits).getBankDepositByInterestRateWithDepositors(4);

        List<Map> resultDeposits = depositService.getBankDepositByInterestRateAll(4);
        LOGGER.info("resultDeposit: {}", resultDeposits);

        depositDao.assertInvoked().getBankDepositByInterestRateWithDepositors(4);
        depositDao.assertNotInvoked().getBankDepositByInterestRateWithDepositors(4);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositBetweenInterestRateBetweenDateDepositAll() throws ParseException{
        LOGGER.debug("testGetBankDepositBetweenInterestRateBetweenDateDepositAll()");
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");

        List<Map> deposits = DataFixture.getMapDeposits();
        LOGGER.info("deposits: {}", deposits);

        depositDao.returns(deposits).getBankDepositBetweenInterestRateBetweenDateDepositWithDepositors(4, 6, startDate, endDate);

        List<Map> resultDeposits = depositService.getBankDepositBetweenInterestRateBetweenDateDepositAll(4,6,startDate,endDate);
        LOGGER.info("resultDeposit: {}", resultDeposits);

        depositDao.assertInvoked().getBankDepositBetweenInterestRateBetweenDateDepositWithDepositors(4,6,startDate,endDate);
        depositDao.assertNotInvoked().getBankDepositBetweenInterestRateBetweenDateDepositWithDepositors(4, 6, startDate, endDate);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositBetweenInterestRateBetweenDateReturnDepositAll() throws ParseException{
        LOGGER.debug("testGetBankDepositBetweenInterestRateBetweenDateReturnDepositAll()");
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");

        List<Map> deposits = DataFixture.getMapDeposits();
        LOGGER.info("deposits: {}", deposits);

        depositDao.returns(deposits).getBankDepositBetweenInterestRateBetweenDateReturnDepositWithDepositors(4, 6, startDate, endDate);

        List<Map> resultDeposits = depositService.getBankDepositBetweenInterestRateBetweenDateReturnDepositAll(4, 6, startDate, endDate);
        LOGGER.info("resultDeposit: {}", resultDeposits);

        depositDao.assertInvoked().getBankDepositBetweenInterestRateBetweenDateReturnDepositWithDepositors(4, 6, startDate, endDate);
        depositDao.assertNotInvoked().getBankDepositBetweenInterestRateBetweenDateReturnDepositWithDepositors(4, 6, startDate, endDate);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositBetweenDateDepositAll() throws ParseException{
        LOGGER.debug("testGetBankDepositBetweenDateDepositAll()");
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");

        List<Map> deposits = DataFixture.getMapDeposits();
        LOGGER.info("deposits: {}", deposits);

        depositDao.returns(deposits).getBankDepositsBetweenDateDepositWithDepositors(startDate, endDate);

        List<Map> resultDeposits = depositService.getBankDepositsBetweenDateDepositAll(startDate,endDate);
        LOGGER.info("resultDeposit: {}", resultDeposits);

        depositDao.assertInvoked().getBankDepositsBetweenDateDepositWithDepositors(startDate,endDate);
        depositDao.assertNotInvoked().getBankDepositsBetweenDateDepositWithDepositors(startDate,endDate);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositBetweenDateReturnDepositAll() throws ParseException{
        LOGGER.debug("testGetBankDepositBetweenDateReturnDepositAll()");
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");

        List<Map> deposits = DataFixture.getMapDeposits();
        LOGGER.info("deposits: {}", deposits);

        depositDao.returns(deposits).getBankDepositsBetweenDateReturnDepositWithDepositors(startDate, endDate);

        List<Map> resultDeposits = depositService.getBankDepositsBetweenDateReturnDepositAll(startDate,endDate);
        LOGGER.info("resultDeposit: {}", resultDeposits);

        depositDao.assertInvoked().getBankDepositsBetweenDateReturnDepositWithDepositors(startDate,endDate);
        depositDao.assertNotInvoked().getBankDepositsBetweenDateReturnDepositWithDepositors(startDate,endDate);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposits, resultDeposits);
        assertReflectionEquals(deposits, resultDeposits, ReflectionComparatorMode.LENIENT_ORDER);
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

    @Test
    public void testUpdateBankDeposit(){
        LOGGER.debug("testUpdateBankDeposit() - run");
        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        LOGGER.info("deposit: {}", deposit);

        depositDao.returns(deposit).getBankDepositByIdCriteria(1L);

        depositDao.performs(new MockBehavior() {
            public Object execute(ProxyInvocation proxyInvocation) throws Throwable {

                return null;
            }
        }).updateBankDeposit(deposit);


        depositDao.returns(null).updateBankDeposit(deposit);

        depositService.updateBankDeposit(deposit);

        depositDao.assertInvokedInSequence().getBankDepositByIdCriteria(1L);
        depositDao.assertNotInvoked().getBankDepositByIdCriteria(1L);
        depositDao.assertInvokedInSequence().updateBankDeposit(deposit);
        depositDao.assertNotInvoked().updateBankDeposit(deposit);
    }

    @Test
    public void testRemoveBankDeposit(){
        LOGGER.debug("testUpdateBankDeposit() - run");
        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        LOGGER.info("deposit: {}", deposit);

        depositDao.returns(deposit).getBankDepositByIdCriteria(1L);

        depositDao.performs(new MockBehavior() {
            public Object execute(ProxyInvocation proxyInvocation) throws Throwable {

                return null;
            }
        }).removeBankDeposit(1L);

        depositDao.returns(null).removeBankDeposit(1L);

        depositService.removeBankDeposit(1L);

        depositDao.assertInvokedInSequence().getBankDepositByIdCriteria(1L);
        depositDao.assertNotInvoked().getBankDepositByIdCriteria(1L);
        depositDao.assertInvokedInSequence().removeBankDeposit(1L);
        depositDao.assertNotInvoked().removeBankDeposit(1L);
        MockUnitils.assertNoMoreInvocations();
    }
}

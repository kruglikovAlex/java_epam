package com.brest.bank.service;

import com.brest.bank.dao.BankDepositDao;
import com.brest.bank.dao.BankDepositDaoImpl;
import com.brest.bank.domain.BankDeposit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.unitils.mock.Mock;
import org.unitils.mock.MockUnitils;
import org.unitils.mock.core.MockObject;
import org.unitils.mock.core.proxy.ProxyInvocation;
import org.unitils.mock.mockbehavior.MockBehavior;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-services-unitils-mock-test.xml"})
public class BankDepositServiceImplUnitilsMockTest{

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    BankDepositService depositService;

    private Mock<BankDepositDao> depositMockDao;

    @Before
    public void setUp() throws Exception {
        depositMockDao = new MockObject<BankDepositDao>(BankDepositDaoImpl.class, this);
        depositService.setDepositDao(depositMockDao.getMock());
    }

    @Test
    public void testGetDeposits() {
        LOGGER.debug("testGetDeposits() - run");

        List<BankDeposit> deposits = DataFixture.getDeposits();
        depositMockDao.returns(deposits).getBankDepositsCriteria();

        List<BankDeposit> resultDeposits = depositService.getBankDeposits();

        depositMockDao.assertInvoked().getBankDepositsCriteria();
        MockUnitils.assertNoMoreInvocations();
        depositMockDao.assertNotInvoked().getBankDepositsCriteria();

        assertEquals(deposits, resultDeposits);
        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositById(){
        LOGGER.debug("testGetBankDepositById() - run");

        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        LOGGER.info("deposit: {}", deposit);

        depositMockDao.returns(deposit).getBankDepositByIdCriteria(1L);

        BankDeposit resultDeposit = depositService.getBankDepositById(1L);
        LOGGER.info("resultDeposit: {}", resultDeposit);

        depositMockDao.assertInvoked().getBankDepositByIdCriteria(1L);
        depositMockDao.assertNotInvoked().getBankDepositByIdCriteria(1L);
        MockUnitils.assertNoMoreInvocations();

        assertEquals(deposit, resultDeposit);
        assertSame(deposit, resultDeposit);
    }

    @Test
    public void testGetBankDepositsBetweenDateDeposit() throws ParseException {
        LOGGER.debug("testGetBankDepositsBetweenDateDeposit() - run");

        List<BankDeposit> deposits = DataFixture.getDeposits();
        LOGGER.info("deposits: {}", deposits);

        depositMockDao.returns(deposits).getBankDepositsFromToDateDeposit(  dateFormat.parse("2014-12-01"),
                                                                        dateFormat.parse("2014-12-01"));

        List<BankDeposit> resultDeposits = depositService.getBankDepositsFromToDateDeposit( dateFormat.parse("2014-12-01"),
                                                                                            dateFormat.parse("2014-12-01"));
        LOGGER.info("resultDeposits: {}", resultDeposits);

        depositMockDao.assertInvoked().getBankDepositsFromToDateDeposit(dateFormat.parse("2014-12-01"),
                                                                    dateFormat.parse("2014-12-01"));
        depositMockDao.assertNotInvoked().getBankDepositsFromToDateDeposit( dateFormat.parse("2014-12-01"),
                                                                        dateFormat.parse("2014-12-01"));
        MockUnitils.assertNoMoreInvocations();

        assertEquals(deposits, resultDeposits);
        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositByCurrencyAll() throws ParseException{
        LOGGER.debug("testGetBankDepositByCurrencyAll() - run");

        List<Map> deposits = DataFixture.getExistAllDepositsAllDepositors();
        LOGGER.info("deposits: {}", deposits);

        depositMockDao.returns(deposits).getBankDepositsByCurrencyWithDepositors("usd");

        List<Map> resultDeposits = depositService.getBankDepositsByCurrencyWithDepositors("usd");

        depositMockDao.assertInvoked().getBankDepositsByCurrencyWithDepositors("usd");
        depositMockDao.assertNotInvoked().getBankDepositsByCurrencyWithDepositors("usd");
        MockUnitils.assertNoMoreInvocations();

        assertEquals(deposits, resultDeposits);
        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositByCurrencyBetweenDateDepositAll() throws ParseException{
        LOGGER.debug("testGetBankDepositByCurrencyBetweenDateDepositAll() - run");
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");

        List<Map> deposits = DataFixture.getExistAllDepositsAllDepositors();
        LOGGER.info("deposits: {}", deposits);

        depositMockDao.returns(deposits)
                .getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd", startDate, endDate);

        List<Map> resultDeposits = depositService
                .getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd", startDate, endDate);

        depositMockDao.assertInvoked()
                .getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd", startDate, endDate);
        depositMockDao.assertNotInvoked()
                .getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd", startDate, endDate);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositByNameAll() throws ParseException{
        LOGGER.debug("testGetBankDepositByNameAll() - run");

        BankDeposit existDeposit = DataFixture.getExistDeposit(1L);
        LOGGER.info("existDeposit: {}", existDeposit);
        Map deposit = DataFixture.getExistDepositAllDepositors(1L,1L);
        LOGGER.info("deposit: {}", deposit);

        depositMockDao.returns(existDeposit).getBankDepositByNameCriteria("depositName1");

        depositMockDao.returns(deposit).getBankDepositByNameWithDepositors("depositName1");

        Map resultDeposit = depositService.getBankDepositByNameWithDepositors("depositName1");
        LOGGER.info("resultDeposit: {}", resultDeposit);

        depositMockDao.assertInvoked().getBankDepositByNameCriteria("depositName1");
        depositMockDao.assertNotInvoked().getBankDepositByNameCriteria("depositName1");
        depositMockDao.assertInvoked().getBankDepositByNameWithDepositors("depositName1");
        depositMockDao.assertNotInvoked().getBankDepositByNameWithDepositors("depositName1");
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposit, resultDeposit);
    }

    @Test
    public void testGetBankDepositByCurrencyBetweenDateReturnDepositAll() throws ParseException{
        LOGGER.debug("testGetBankDepositByCurrencyBetweenDateDepositAll() - run");
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");

        List<Map> deposits = DataFixture.getExistAllDepositsAllDepositors();
        LOGGER.info("deposits: {}", deposits);

        depositMockDao.returns(deposits)
                .getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd", startDate, endDate);

        List<Map> resultDeposits = depositService
                .getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd", startDate, endDate);

        depositMockDao.assertInvoked()
                .getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd", startDate, endDate);
        depositMockDao.assertNotInvoked()
                .getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd", startDate, endDate);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositByNameBetweenDateDepositAll() throws ParseException{
        LOGGER.debug("testGetBankDepositByNameBetweenDateDepositAll() - run");

        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");
        BankDeposit existDeposit = DataFixture.getExistDeposit(1L);
        LOGGER.info("existDeposit: {}", existDeposit);
        Map deposit = DataFixture.getExistDepositAllDepositors(1L,1L);
        LOGGER.info("deposit: {}", deposit);

        depositMockDao.returns(existDeposit).getBankDepositByNameCriteria("depositName1");

        depositMockDao.returns(deposit)
                .getBankDepositByNameFromToDateDepositWithDepositors("depositName1", startDate, endDate);

        Map resultDeposit = depositService.getBankDepositByNameFromToDateDepositWithDepositors( "depositName1",
                                                                                                startDate,
                                                                                                endDate);
        LOGGER.info("resultDeposit: {}", resultDeposit);

        depositMockDao.assertInvoked().getBankDepositByNameCriteria("depositName1");
        depositMockDao.assertNotInvoked().getBankDepositByNameCriteria("depositName1");
        depositMockDao.assertInvoked()
                .getBankDepositByNameFromToDateDepositWithDepositors("depositName1", startDate, endDate);
        depositMockDao.assertNotInvoked()
                .getBankDepositByNameFromToDateDepositWithDepositors("depositName1", startDate, endDate);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposit, resultDeposit);
    }

    @Test
    public void testGetBankDepositByNameBetweenDateReturnDepositAll() throws ParseException{
        LOGGER.debug("testGetBankDepositByNameBetweenDateReturnDepositAll() - run");

        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");
        BankDeposit existDeposit = DataFixture.getExistDeposit(1L);
        LOGGER.info("existDeposit: {}", existDeposit);
        Map deposit = DataFixture.getExistDepositAllDepositors(1L,1L);
        LOGGER.info("deposit: {}", deposit);

        depositMockDao.returns(existDeposit).getBankDepositByNameCriteria("depositName1");

        depositMockDao.returns(deposit)
                .getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1", startDate, endDate);

        Map resultDeposit = depositService.getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1",
                startDate,
                endDate);
        LOGGER.info("resultDeposit: {}", resultDeposit);

        depositMockDao.assertInvoked().getBankDepositByNameCriteria("depositName1");
        depositMockDao.assertNotInvoked().getBankDepositByNameCriteria("depositName1");
        depositMockDao.assertInvoked()
                .getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1", startDate, endDate);
        depositMockDao.assertNotInvoked()
                .getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1", startDate, endDate);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposit, resultDeposit);
    }

    @Test
    public void testGetBankDepositBetweenInterestRate(){
        LOGGER.debug("testGetBankDepositBetweenInterestRateAll() - run");

        List<BankDeposit> deposits = DataFixture.getExistDeposits();
        LOGGER.info("deposits: {}", deposits);

        depositMockDao.returns(deposits).getBankDepositsFromToInterestRateCriteria(4, 7);

        List<BankDeposit> resultDeposits = depositService.getBankDepositsFromToInterestRate(4,7);

        depositMockDao.assertInvoked().getBankDepositsFromToInterestRateCriteria(4, 7);
        depositMockDao.assertNotInvoked().getBankDepositsFromToInterestRateCriteria(4, 7);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositByInterestRate(){
        LOGGER.debug("testGetBankDepositByInterestRateAll() - run");

        List<BankDeposit> deposits = DataFixture.getExistDeposits();
        LOGGER.info("deposits: {}", deposits);

        depositMockDao.returns(deposits).getBankDepositsByInterestRateCriteria(4);

        List<BankDeposit> resultDeposits = depositService.getBankDepositsByInterestRate(4);
        LOGGER.info("resultDeposit: {}", resultDeposits);

        depositMockDao.assertInvoked().getBankDepositsByInterestRateCriteria(4);
        depositMockDao.assertNotInvoked().getBankDepositsByInterestRateCriteria(4);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositBetweenDateDepositAll() throws ParseException{
        LOGGER.debug("testGetBankDepositBetweenDateDepositAll()");
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");

        List<Map> deposits = DataFixture.getExistAllDepositsAllDepositors();
        LOGGER.info("deposits: {}", deposits);

        depositMockDao.returns(deposits).getBankDepositsFromToDateDepositWithDepositors(startDate, endDate);

        List<Map> resultDeposits = depositService.getBankDepositsFromToDateDepositWithDepositors(startDate,endDate);
        LOGGER.info("resultDeposit: {}", resultDeposits);

        depositMockDao.assertInvoked().getBankDepositsFromToDateDepositWithDepositors(startDate,endDate);
        depositMockDao.assertNotInvoked().getBankDepositsFromToDateDepositWithDepositors(startDate,endDate);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositBetweenDateReturnDepositAll() throws ParseException{
        LOGGER.debug("testGetBankDepositBetweenDateReturnDepositAll()");
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");

        List<Map> deposits = DataFixture.getExistAllDepositsAllDepositors();
        LOGGER.info("deposits: {}", deposits);

        depositMockDao.returns(deposits).getBankDepositsFromToDateReturnDepositWithDepositors(startDate, endDate);

        List<Map> resultDeposits = depositService.getBankDepositsFromToDateReturnDepositWithDepositors(startDate, endDate);
        LOGGER.info("resultDeposit: {}", resultDeposits);

        depositMockDao.assertInvoked().getBankDepositsFromToDateReturnDepositWithDepositors(startDate, endDate);
        depositMockDao.assertNotInvoked().getBankDepositsFromToDateReturnDepositWithDepositors(startDate, endDate);
        MockUnitils.assertNoMoreInvocations();

        assertSame(deposits, resultDeposits);
        assertReflectionEquals(deposits, resultDeposits, ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test
    public void testAddBankDeposit(){
        LOGGER.debug("testAddBankDeposit() - run");
        BankDeposit deposit = DataFixture.getNewDeposit();
        LOGGER.info("deposit: {}", deposit);

        depositMockDao.returns(null).getBankDepositByNameCriteria(deposit.getDepositName());

        depositMockDao.performs(new MockBehavior() {
            public Object execute(ProxyInvocation proxyInvocation) throws Throwable {
                return null;
            }
        }).addBankDeposit(deposit);

        depositMockDao.returns(null).addBankDeposit(deposit);

        depositService.addBankDeposit(deposit);

        depositMockDao.assertInvokedInSequence().getBankDepositByNameCriteria(deposit.getDepositName());
        depositMockDao.assertNotInvoked().getBankDepositByNameCriteria(deposit.getDepositName());
        depositMockDao.assertInvokedInSequence().addBankDeposit(deposit);
        depositMockDao.assertNotInvoked().addBankDeposit(deposit);
        MockUnitils.assertNoMoreInvocations();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExistsBankDeposit(){
        LOGGER.debug("testAddBankDeposit() - run");
        BankDeposit deposit = DataFixture.getNewDeposit();
        LOGGER.info("deposit: {}", deposit);

        depositMockDao.returns(deposit).getBankDepositByNameCriteria(deposit.getDepositName());

        depositMockDao.performs(new MockBehavior() {
            public Object execute(ProxyInvocation proxyInvocation) throws Throwable {
                return null;
            }
        }).addBankDeposit(deposit);

        depositMockDao.returns(null).addBankDeposit(deposit);

        depositService.addBankDeposit(deposit);

        depositMockDao.assertInvokedInSequence().getBankDepositByNameCriteria(deposit.getDepositName());
        depositMockDao.assertNotInvoked().getBankDepositByNameCriteria(deposit.getDepositName());
        depositMockDao.assertInvokedInSequence().addBankDeposit(deposit);
        depositMockDao.assertNotInvoked().addBankDeposit(deposit);
        MockUnitils.assertNoMoreInvocations();
    }

    @Test
    public void testUpdateBankDeposit(){
        LOGGER.debug("testUpdateBankDeposit() - run");
        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        LOGGER.info("deposit: {}", deposit);

        depositMockDao.returns(deposit).getBankDepositByIdCriteria(1L);

        depositMockDao.performs(new MockBehavior() {
            public Object execute(ProxyInvocation proxyInvocation) throws Throwable {

                return null;
            }
        }).updateBankDeposit(deposit);


        depositMockDao.returns(null).updateBankDeposit(deposit);

        depositService.updateBankDeposit(deposit);

        depositMockDao.assertInvokedInSequence().getBankDepositByIdCriteria(1L);
        depositMockDao.assertNotInvoked().getBankDepositByIdCriteria(1L);
        depositMockDao.assertInvokedInSequence().updateBankDeposit(deposit);
        depositMockDao.assertNotInvoked().updateBankDeposit(deposit);
    }

    @Test
    public void testRemoveBankDeposit(){
        LOGGER.debug("testUpdateBankDeposit() - run");
        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        LOGGER.info("deposit: {}", deposit);

        depositMockDao.returns(deposit).getBankDepositByIdCriteria(1L);

        depositMockDao.performs(new MockBehavior() {
            public Object execute(ProxyInvocation proxyInvocation) throws Throwable {

                return null;
            }
        }).deleteBankDeposit(1L);

        depositMockDao.returns(null).deleteBankDeposit(1L);

        depositService.deleteBankDeposit(1L);

        depositMockDao.assertInvokedInSequence().getBankDepositByIdCriteria(1L);
        depositMockDao.assertNotInvoked().getBankDepositByIdCriteria(1L);
        depositMockDao.assertInvokedInSequence().deleteBankDeposit(1L);
        depositMockDao.assertNotInvoked().deleteBankDeposit(1L);
        MockUnitils.assertNoMoreInvocations();
    }
}

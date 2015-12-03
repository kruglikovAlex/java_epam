package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import com.brest.bank.util.HibernateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.criterion.Projections;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})
public class BankDepositDaoImplTest {

    @Autowired
    private BankDepositDao depositDao;

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private BankDeposit deposit;
    private List<BankDeposit> deposits;

    public Object result;
    public Integer sizeBefore = 0, sizeAfter = 0;

    @Before
    public void setUp() throws Exception {
        deposit = new BankDeposit();
        deposits = new ArrayList<BankDeposit>();
        deposits = depositDao.getBankDepositsCriteria();
    }

    @Test
    public void testGetBankDepositsCriteria() throws Exception {
        LOGGER.debug("deposits.size()= {}", deposits.size());

        assertFalse(deposits.isEmpty());
        assertThat(deposits.size(), is(not(0)));
        assertNotNull(deposits);
    }

    @Test
    public void testGetBankDepositByIdCriteria() throws Exception {
        deposit = depositDao.getBankDepositByIdCriteria(3L);
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, " +
                "depositMinAmount=300, depositCurrency=usd, depositInterestRate=6, " +
                "depositAddConditions=condition2}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByNameCriteria() throws Exception {
        deposit = depositDao.getBankDepositByNameCriteria("depositName2");
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, " +
                "depositMinAmount=300, depositCurrency=usd, depositInterestRate=6, " +
                "depositAddConditions=condition2}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByCurrencyCriteria(){
        deposits = depositDao.getBankDepositsByCurrencyCriteria("usd");
        LOGGER.debug("deposits: {}", deposits);

        assertFalse(deposits.isEmpty());
        assertThat(deposits.size(), is(not(0)));
        assertNotNull(deposits);

        assertEquals("[BankDeposit: { depositId=1, depositName=depositName0, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition0}, " +
                "BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}]",deposits.toString());
    }

    @Test
    public void testGetBankDepositsByInterestRateCriteria(){
        deposits = depositDao.getBankDepositsByInterestRateCriteria(6);
        LOGGER.debug("deposits: {}",deposits);

        assertFalse(deposits.isEmpty());
        assertThat(deposits.size(), is(not(0)));
        assertNotNull(deposits);

        assertEquals("[BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}]",deposits.toString());
    }

    @Test
    public void testGetBankDepositsFromToMinTermCriteria(){
        deposits = depositDao.getBankDepositsFromToMinTermCriteria(13, 14);
        LOGGER.debug("deposits: {}",deposits);

        assertFalse(deposits.isEmpty());
        assertThat(deposits.size(), is(not(0)));
        assertNotNull(deposits);

        assertEquals("[BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}]",deposits.toString());
    }

    @Test(expected = AssertionError.class)
    public void testGetBankDepositsFromToMinTermCriteriaNullFirstArgs(){
        Integer start = null;
        deposits = depositDao.getBankDepositsFromToMinTermCriteria(start, 14);
    }

    @Test(expected = AssertionError.class)
    public void testGetBankDepositsFromToMinTermCriteriaNullSecondArgs(){
        Integer end = null;
        deposits = depositDao.getBankDepositsFromToMinTermCriteria(13, end);
    }

    @Test
    public void testGetBankDepositsFromToInterestRateCriteria(){
        deposits = depositDao.getBankDepositsFromToInterestRateCriteria(5,6);
        LOGGER.debug("deposits: {}",deposits);

        assertFalse(deposits.isEmpty());
        assertThat(deposits.size(), is(not(0)));
        assertNotNull(deposits);

        assertEquals("[BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}]",deposits.toString());
    }

    @Test
    public void testAddBankDeposit() throws Exception {
        deposit = new BankDeposit();
            deposit.setDepositName("name");
            deposit.setDepositMinTerm(24);
            deposit.setDepositMinAmount(1000);
            deposit.setDepositCurrency("grb");
            deposit.setDepositInterestRate(4);
            deposit.setDepositAddConditions("condition");
        LOGGER.debug("new deposit - {}",deposit);

        sizeBefore = rowCount(BankDeposit.class);
        LOGGER.debug("size before add = {}",sizeBefore);

        LOGGER.debug("start added new deposit - {}",deposit);
        depositDao.addBankDeposit(deposit);
        LOGGER.debug("new deposit was added - {}",deposit);

        assertTrue(deposit.getDepositId()!= null);
        assertEquals(deposit.toString(),depositDao.getBankDepositByIdCriteria(deposit.getDepositId()).toString());

        sizeAfter = rowCount(BankDeposit.class);
        LOGGER.debug("size after add = {}",sizeAfter);

        assertTrue(sizeAfter == sizeBefore+1);
    }

    @Test
    public void testUpdateBankDeposit(){
        String testDeposit;

        deposit = deposits.get(3);
        Long id = deposit.getDepositId();

        testDeposit = deposit.toString();

        deposit.setDepositName("UpdateDepositName");
        deposit.setDepositMinAmount(10);
        deposit.setDepositCurrency("grb");
        deposit.setDepositAddConditions("UpdateConditions");

        depositDao.updateBankDeposit(deposit);

        assertNotEquals(depositDao.getBankDepositByIdCriteria(id).toString(),testDeposit);
        assertEquals(depositDao.getBankDepositByIdCriteria(id).toString(),deposit.toString());

    }

    @Test
    public void testRemoveBankDeposit() {
        assertNotNull(deposits);
        assertFalse(deposits.isEmpty());
        sizeBefore = deposits.size();
        LOGGER.debug("size before - {}",sizeBefore);
        depositDao.deleteBankDeposit(5L);
        deposits = depositDao.getBankDepositsCriteria();
        LOGGER.debug("size after - {}",deposits.size());
        assertEquals(sizeBefore-1,deposits.size());
    }

    @Test
    public void testRemoveBankDeposit2() throws Exception{
        sizeBefore = rowCount(BankDeposit.class);
        LOGGER.debug("sizeBefore = {}", sizeBefore);

        int sizeDepositorsBefore = rowCount(BankDepositor.class);
        LOGGER.debug("sizeDepositorsBefore = {}", sizeDepositorsBefore);

        deposit = depositDao.getBankDepositByIdCriteria(2L);

        BankDepositor depositor = new BankDepositor();
            depositor.setDepositorName("newName");
            depositor.setDepositorDateDeposit(dateFormat.parse("2014-12-02"));
            depositor.setDepositorAmountDeposit(1000);
            depositor.setDepositorAmountPlusDeposit(10);
            depositor.setDepositorAmountMinusDeposit(10);
            depositor.setDepositorDateReturnDeposit(dateFormat.parse("2014-12-03"));
            depositor.setDepositorMarkReturnDeposit(0);

        Set depositors = new HashSet();
        depositors.add(depositor);
        deposit.setDepositors(depositors);
        depositDao.updateBankDeposit(deposit);

        sizeDepositorsBefore = rowCount(BankDepositor.class);
        LOGGER.debug("sizeDepositorsBefore = {}", sizeDepositorsBefore);

        depositDao.deleteBankDeposit(2L);

        assertNull(depositDao.getBankDepositByIdCriteria(2L));

        sizeAfter = rowCount(BankDeposit.class);
        LOGGER.debug("sizeAfter = {}", sizeAfter);

        int sizeDepositorsAfter = rowCount(BankDepositor.class);
        LOGGER.debug("sizeDepositorsAfter = {}", sizeDepositorsAfter);

        assertTrue(sizeAfter == sizeBefore - 1);
        assertTrue(sizeDepositorsAfter == sizeDepositorsBefore - 1);
    }

    public Integer rowCount(Class<?> name) throws ClassNotFoundException{

        HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        //--- query
        result = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(name)
                .setProjection(Projections.rowCount()).uniqueResult();

        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        return Integer.parseInt(result.toString());
    }
}
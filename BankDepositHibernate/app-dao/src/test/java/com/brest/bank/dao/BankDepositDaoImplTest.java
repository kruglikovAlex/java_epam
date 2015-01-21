package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BankDepositDaoImplTest {

    private static final Logger LOGGER = LogManager.getLogger();

    BankDeposit deposit = new BankDeposit();
    BankDepositDao depositDao = new BankDepositDaoImpl();
    List<BankDeposit> deposits;
    Object result;
    Integer sizeBefore = 0;

    @Before
    public void setUp() throws Exception {
        depositDao.startConnection();
        //depositDao.getCurrentSession().getTransaction().begin();
        DateFixture.init(depositDao);
    }

    @After
    public void tearDown() throws Exception {
        depositDao.closeConnection();
    }

    @Test
    public void testGetBankDepositsSQL() throws Exception {
        depositDao.getCurrentSession().getTransaction().begin();

        deposits = depositDao.getBankDepositsSQL();
        LOGGER.debug("deposits.size()= {}", deposits.size());

        assertFalse(deposits.isEmpty());
        assertNotNull(deposits);
    }

    @Test
    public void testGetBankDepositsCriteria() throws Exception {
        depositDao.getCurrentSession().getTransaction().begin();

        deposits = depositDao.getBankDepositsCriteria();
        LOGGER.debug("deposits.size()= {}", deposits.size());

        assertFalse(deposits.isEmpty());
        assertNotNull(deposits);
    }

    @Test
    public void testGetBankDepositByIdGet() throws Exception {
        depositDao.getCurrentSession().getTransaction().begin();

        deposit = depositDao.getBankDepositByIdGet(4L);
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=4, depositName=depositName4, depositMinTerm=9, " +
                "depositMinAmount=100, depositCurrency=eur, depositInterestRate=5, " +
                "depositAddConditions=condition4}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByIdLoad() throws Exception {
        depositDao.getCurrentSession().getTransaction().begin();

        deposit = depositDao.getBankDepositByIdLoad(4L);
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=4, depositName=depositName4, depositMinTerm=9, " +
                "depositMinAmount=100, depositCurrency=eur, depositInterestRate=5, " +
                "depositAddConditions=condition4}",deposit.toString());
    }

   @Test
    public void testGetBankDepositByIdCriteria() throws Exception {
        depositDao.getCurrentSession().getTransaction().begin();

        deposit = depositDao.getBankDepositByIdCriteria(4L);
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=4, depositName=depositName4, depositMinTerm=9, " +
                "depositMinAmount=100, depositCurrency=eur, depositInterestRate=5, " +
                "depositAddConditions=condition4}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByNameObject() throws Exception {
        depositDao.getCurrentSession().getTransaction().begin();

        deposit = depositDao.getBankDepositByNameObject("depositName4");
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=4, depositName=depositName4, depositMinTerm=9, " +
                "depositMinAmount=100, depositCurrency=eur, depositInterestRate=5, " +
                "depositAddConditions=condition4}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByNameList() throws Exception {
        depositDao.getCurrentSession().getTransaction().begin();

        deposit = depositDao.getBankDepositByNameList("depositName4");
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=4, depositName=depositName4, depositMinTerm=9, " +
                "depositMinAmount=100, depositCurrency=eur, depositInterestRate=5, " +
                "depositAddConditions=condition4}",deposit.toString());
    }

    @Test
    public void testAddBankDeposit() throws Exception {
        depositDao.getCurrentSession().getTransaction().begin();
        result = depositDao.getCurrentSession().createCriteria(BankDeposit.class)
                .setProjection(Projections.rowCount()).uniqueResult();
        sizeBefore = Integer.parseInt(result.toString());

        deposit = new BankDeposit();
        deposit.setDepositName("name");
        deposit.setDepositMinTerm(24);
        deposit.setDepositMinAmount(1000);
        deposit.setDepositCurrency("grb");
        deposit.setDepositInterestRate(4);
        deposit.setDepositAddConditions("condition");

        LOGGER.debug("added new deposit - {}",deposit);
        depositDao.addBankDeposit(deposit);

        assertTrue(deposit.getDepositId()!= null);

        result = depositDao.getCurrentSession().createCriteria(BankDeposit.class)
                .setProjection(Projections.rowCount()).uniqueResult();
        Integer count = Integer.parseInt(result.toString());

        assertTrue(count == sizeBefore+1);

        assertEquals(deposit.toString(),depositDao.getCurrentSession().createCriteria(BankDeposit.class)
                .add(Restrictions.eq("depositId", deposit.getDepositId())).uniqueResult().toString());
    }

    //@Test
    public void testUpdateBankDeposit() throws Exception {
        depositDao.getCurrentSession().getTransaction().begin();
        deposit = depositDao.getBankDepositByIdLoad(1L);
        LOGGER.debug("deposit before update - {}",deposit);
        deposit.setDepositName("name");

        depositDao.updateBankDeposit(deposit);
        LOGGER.debug("deposit after update - {}",depositDao.getBankDepositByIdCriteria(1L));

        assertEquals(deposit.toString(),depositDao.getBankDepositByIdCriteria(1L).toString());
    }

    @Test
    public void testRemoveBankDeposit() throws Exception {
        depositDao.getCurrentSession().getTransaction().begin();
        result = depositDao.getCurrentSession().createCriteria(BankDeposit.class)
                .setProjection(Projections.rowCount()).uniqueResult();
        sizeBefore = Integer.parseInt(result.toString());
        LOGGER.debug("sizeBefore = {}", sizeBefore);

        depositDao.removeBankDeposit(2L);

        result = depositDao.getCurrentSession().createCriteria(BankDeposit.class)
                .setProjection(Projections.rowCount()).uniqueResult();
        Integer sizeAfter = Integer.parseInt(result.toString());
        LOGGER.debug("sizeAfter = {}", sizeAfter);

        assertTrue(sizeAfter == sizeBefore - 1);

        assertNull(depositDao.getCurrentSession().createCriteria(BankDeposit.class)
                .add(Restrictions.eq("depositId", 2L)).uniqueResult());
    }

}
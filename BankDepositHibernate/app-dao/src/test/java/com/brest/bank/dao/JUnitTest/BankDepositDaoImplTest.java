package com.brest.bank.dao.JUnitTest;

import com.brest.bank.dao.BankDepositDao;
import com.brest.bank.dao.BankDepositDaoImpl;
import com.brest.bank.dao.DataFixture;
import com.brest.bank.dao.JUnitRule;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.util.HibernateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.junit.*;
import org.junit.rules.ExternalResource;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

public class BankDepositDaoImplTest {

    public BankDepositDao depositDao = new BankDepositDaoImpl();

    private static final Logger LOGGER = LogManager.getLogger();

    public Session session;

    private BankDeposit deposit = new BankDeposit();
    private List<BankDeposit> deposits;

    public Object result;
    public Integer sizeBefore = 0, sizeAfter = 0, sizeDepositorsBefore = 0, sizeDepositorsAfter = 0;

    @ClassRule
    public static ExternalResource resource = new ExternalResource() {
        @Override
        public void before() throws Throwable {
            LOGGER.debug("JUnit rule - before() - run method DataFixture.init() for DaoImplTest: ");
            DataFixture.init();
        };
        @Override
        public void after(){
            LOGGER.debug("JUnit rule - after()");
        };
    };

    @Rule
    public JUnitRule rule = new JUnitRule();

    @Test
    public void testGetBankDepositsSQL() throws Exception {
        deposits = depositDao.getBankDepositsSQL();
        LOGGER.debug("deposits.size()= {}", deposits.size());

        assertFalse(deposits.isEmpty());
        assertThat(deposits.size(), is(not(0)));
        assertNotNull(deposits);
    }

    @Test
    public void testGetBankDepositsCriteria() throws Exception {
        deposits = depositDao.getBankDepositsCriteria();
        LOGGER.debug("deposits.size()= {}", deposits.size());

        assertFalse(deposits.isEmpty());
        assertThat(deposits.size(), is(not(0)));
        assertNotNull(deposits);
    }

    @Test
    public void testGetBankDepositByIdGet() throws Exception {
        deposit = depositDao.getBankDepositByIdGet(4L);
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=4, depositName=depositName3, depositMinTerm=12, " +
                "depositMinAmount=100, depositCurrency=usd, depositInterestRate=4, " +
                "depositAddConditions=condition3}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByIdLoad() throws Exception {
        deposit = depositDao.getBankDepositByIdLoad(4L);
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=4, depositName=depositName3, depositMinTerm=12, " +
                "depositMinAmount=100, depositCurrency=usd, depositInterestRate=4, " +
                "depositAddConditions=condition3}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByIdCriteria() throws Exception {
        deposit = depositDao.getBankDepositByIdCriteria(4L);
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=4, depositName=depositName3, depositMinTerm=12, " +
                "depositMinAmount=100, depositCurrency=usd, depositInterestRate=4, " +
                "depositAddConditions=condition3}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByNameSQL() throws Exception {
        deposit = depositDao.getBankDepositByNameSQL("depositName3");
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=4, depositName=depositName3, depositMinTerm=12, " +
                "depositMinAmount=100, depositCurrency=usd, depositInterestRate=4, " +
                "depositAddConditions=condition3}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByNameCriteria() throws Exception {
        deposit = depositDao.getBankDepositByNameCriteria("depositName3");
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=4, depositName=depositName3, depositMinTerm=12, " +
                "depositMinAmount=100, depositCurrency=usd, depositInterestRate=4, " +
                "depositAddConditions=condition3}",deposit.toString());
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
        assertEquals(deposit.toString(),depositDao.getBankDepositByIdGet(deposit.getDepositId()).toString());

        sizeAfter = rowCount(BankDeposit.class);
        LOGGER.debug("size after add = {}",sizeAfter);

        assertTrue(sizeAfter == sizeBefore+1);
    }

    @Test
    public void testUpdateBankDeposit() throws Exception {
        deposit = depositDao.getBankDepositByIdCriteria(1L);
        LOGGER.debug("deposit before update - {}",deposit);
        deposit.setDepositName("name");

        depositDao.updateBankDeposit(deposit);

        BankDeposit testDeposit = depositDao.getBankDepositByIdCriteria(1L);
        LOGGER.debug("deposit after update - {}",testDeposit);

        assertEquals(deposit.toString(),testDeposit.toString());
    }

    @Test
    public void testRemoveBankDeposit() throws Exception{
        sizeBefore = rowCount(BankDeposit.class);
        LOGGER.debug("sizeBefore = {}", sizeBefore);

        sizeDepositorsBefore = rowCount(BankDepositor.class);
        LOGGER.debug("sizeDepositorsBefore = {}", sizeDepositorsBefore);

        depositDao.removeBankDeposit(2L);

        assertNull(depositDao.getBankDepositByIdCriteria(2L));

        sizeAfter = rowCount(BankDeposit.class);
        LOGGER.debug("sizeAfter = {}", sizeAfter);

        sizeDepositorsAfter = rowCount(BankDepositor.class);
        LOGGER.debug("sizeDepositorsAfter = {}", sizeDepositorsAfter);

        assertTrue(sizeAfter == sizeBefore - 1);
        assertTrue(sizeDepositorsAfter == sizeDepositorsBefore - 1);
    }

    public Integer rowCount(Class<?> name) throws ClassNotFoundException{
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        result = session.createCriteria(name)
                .setProjection(Projections.rowCount()).uniqueResult();
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        return Integer.parseInt(result.toString());
    }

}
package com.brest.bank.dao.JUnitTest;

import com.brest.bank.dao.*;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.util.HibernateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.junit.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

public class BankDepositDaoImplTest {

    //public static SessionFactory sessionFactory;

    private static final Logger LOGGER = LogManager.getLogger();

    public static BankDepositDao depositDao = new BankDepositDaoImpl();
    public static DataFixture dataFixture = new DataFixture();

    public BankDeposit deposit = new BankDeposit();
    public List<BankDeposit> deposits = new ArrayList<BankDeposit>();

    public Object result;
    public Integer sizeBefore = 0, sizeAfter = 0;

    @BeforeClass
    public static void initAllTests() throws ParseException, ClassNotFoundException{
        //sessionFactory = HibernateUtil.getSessionFactory();
        dataFixture.init();
    }

    //@Before
    /*public void initEveryTest(){
        depositDao.setSession(sessionFactory);
    }

    //@After
    public void afterEveryTest(){
        sessionFactory.getCurrentSession().getTransaction().commit();
    }

    //@AfterClass
    public static void afterAllTests(){
        //sessionFactory.close();
    }*/

    @Test
    public void testGetBankDepositsCriteria() throws Exception {
        deposits = depositDao.getBankDepositsCriteria();
        LOGGER.debug("deposits.size()= {}", deposits.size());

        assertFalse(deposits.isEmpty());
        assertThat(deposits.size(), is(not(0)));
        assertNotNull(deposits);
    }

    @Test
    public void testGetBankDepositByIdCriteria() throws Exception {
        deposit = depositDao.getBankDepositByIdCriteria(3L);
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByNameCriteria() throws Exception {
        deposit = depositDao.getBankDepositByNameCriteria("depositName2");
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}",deposit.toString());
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

    public Integer rowCount(Class<?> name) throws ClassNotFoundException{

        HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        //--- query
        result = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(name)
                .setProjection(Projections.rowCount()).uniqueResult();

        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        return Integer.parseInt(result.toString());
    }
}
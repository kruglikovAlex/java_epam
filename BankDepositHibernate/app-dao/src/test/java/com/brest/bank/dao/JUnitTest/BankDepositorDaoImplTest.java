package com.brest.bank.dao.JUnitTest;

import com.brest.bank.dao.BankDepositorDao;
import com.brest.bank.dao.BankDepositorDaoImpl;
import com.brest.bank.dao.DataFixture;
import com.brest.bank.dao.JUnitRule;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.util.HibernateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.junit.*;
import org.junit.rules.ExternalResource;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import java.text.SimpleDateFormat;

public class BankDepositorDaoImplTest {

    public static BankDepositorDao depositorDao = new BankDepositorDaoImpl();
    public static DataFixture dataFixture = new DataFixture();


    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public BankDeposit deposit = new BankDeposit();
    public BankDepositor depositor = new BankDepositor();
    public List<BankDepositor> depositors = new ArrayList<BankDepositor>();

    Object result;
    Integer sizeBefore = 0, sizeAfter = 0, sizeDepositorsBefore = 0, sizeDepositorsAfter = 0;

    @BeforeClass
    public static void initAllTests() throws ParseException, ClassNotFoundException{
        dataFixture.init();
    }

    @Test
    public void testGetBankDepositorsCriteria() throws Exception {
        depositors = depositorDao.getBankDepositorsCriteria();
        LOGGER.debug("depositors.size()= {}", depositors.size());

        assertFalse(depositors.isEmpty());
        assertNotNull(depositors);
    }

    @Test
    public void testGetBankDepositorByIdCriteria() throws Exception {
        depositor = depositorDao.getBankDepositorByIdCriteria(1L);
        LOGGER.debug("depositor = {}", depositor);

        assertEquals("BankDepositor: { depositorId=1, depositorName=depositorName1, depositorDateDeposit=2014-12-01, " +
                "depositorAmountDeposit=1001, depositorAmountPlusDeposit=20, depositorAmountMinusDeposit=20, " +
                "depositorDateReturnDeposit=2014-12-02, depositorMarkReturnDeposit=0}",depositor.toString());
    }

    @Test
    public void testGetBankDepositorByNameCriteria() throws Exception {
        depositor = depositorDao.getBankDepositorByNameCriteria("depositorName1");
        LOGGER.debug("depositor = {}", depositor);

        assertEquals("BankDepositor: { depositorId=1, depositorName=depositorName1, depositorDateDeposit=2014-12-01, " +
                "depositorAmountDeposit=1001, depositorAmountPlusDeposit=20, depositorAmountMinusDeposit=20, " +
                "depositorDateReturnDeposit=2014-12-02, depositorMarkReturnDeposit=0}",depositor.toString());
    }

    @Test
    public void testAddBankDepositor() throws Exception {
        depositor = new BankDepositor();
            depositor.setDepositorName("newName");
            depositor.setDepositorDateDeposit(dateFormat.parse("2014-12-02"));
            depositor.setDepositorAmountDeposit(1000);
            depositor.setDepositorAmountPlusDeposit(10);
            depositor.setDepositorAmountMinusDeposit(10);
            depositor.setDepositorDateReturnDeposit(dateFormat.parse("2014-12-03"));
            depositor.setDepositorMarkReturnDeposit(0);
        LOGGER.debug("add new depositor - {}", depositor);

        sizeBefore = rowCount(BankDepositor.class);
        LOGGER.debug("size before add = {}", sizeBefore);

        depositorDao.addBankDepositor(1L, depositor);

        assertTrue(depositor.getDepositorId() != null);
        assertEquals(depositor.toString(), depositorDao.getBankDepositorByIdCriteria(depositor.getDepositorId()).toString());

        sizeAfter = rowCount(BankDepositor.class);
        LOGGER.debug("size after add = {}", sizeAfter);

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
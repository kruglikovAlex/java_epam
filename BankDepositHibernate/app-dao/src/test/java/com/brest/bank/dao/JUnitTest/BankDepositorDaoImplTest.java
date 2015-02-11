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
import org.hibernate.criterion.Projections;
import org.junit.*;
import org.junit.rules.ExternalResource;

import java.util.List;

import static org.junit.Assert.*;
import java.text.SimpleDateFormat;

public class BankDepositorDaoImplTest {
    public BankDepositorDao depositorDao = new BankDepositorDaoImpl();

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Session session;

    BankDeposit deposit = new BankDeposit();
    BankDepositor depositor = new BankDepositor();
    List<BankDepositor> depositors;
    Object result;
    Integer sizeBefore = 0, sizeAfter = 0, sizeDepositorsBefore = 0, sizeDepositorsAfter = 0;

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
    public void testGetBankDepositorsSQL() throws Exception {
        depositors = depositorDao.getBankDepositorsSQL();
        LOGGER.debug("depositors.size()= {}", depositors.size());
        LOGGER.debug("depositors: {}", depositors);

        assertFalse(depositors.isEmpty());
        assertNotNull(depositors);
    }

    @Test
    public void testGetBankDepositorsCriteria() throws Exception {
        depositors = depositorDao.getBankDepositorsCriteria();
        LOGGER.debug("depositors.size()= {}", depositors.size());

        assertFalse(depositors.isEmpty());
        assertNotNull(depositors);
    }

    @Test
    public void testGetBankDepositorByIdGet() throws Exception {
        depositor = depositorDao.getBankDepositorByIdGet(4L);
        LOGGER.debug("depositor = {}", depositor);

        assertEquals("BankDepositor: { depositorId=4, depositorName=depositorName4, depositorDateDeposit=2014-12-02, " +
                "depositorAmountDeposit=1000, depositorAmountPlusDeposit=50, depositorAmountMinusDeposit=50, " +
                "depositorDateReturnDeposit=2014-12-03, depositorMarkReturnDeposit=0}",depositor.toString());
    }

    @Test
    public void testGetBankDepositorByIdLoad() throws Exception {
        depositor = depositorDao.getBankDepositorByIdLoad(4L);
        LOGGER.debug("depositor = {}", depositor);

        assertEquals("BankDepositor: { depositorId=4, depositorName=depositorName4, depositorDateDeposit=2014-12-02, " +
                "depositorAmountDeposit=1000, depositorAmountPlusDeposit=50, depositorAmountMinusDeposit=50, " +
                "depositorDateReturnDeposit=2014-12-03, depositorMarkReturnDeposit=0}",depositor.toString());
    }

    @Test
    public void testGetBankDepositorByIdCriteria() throws Exception {
        depositor = depositorDao.getBankDepositorByIdCriteria(4L);
        LOGGER.debug("depositor = {}", depositor);

        assertEquals("BankDepositor: { depositorId=4, depositorName=depositorName4, depositorDateDeposit=2014-12-02, " +
                "depositorAmountDeposit=1000, depositorAmountPlusDeposit=50, depositorAmountMinusDeposit=50, " +
                "depositorDateReturnDeposit=2014-12-03, depositorMarkReturnDeposit=0}",depositor.toString());
    }

    @Test
    public void testGetBankDepositorByNameSQL() throws Exception {
        depositor = depositorDao.getBankDepositorByNameSQL("depositorName4");
        LOGGER.debug("depositor = {}", depositor);

        assertEquals("BankDepositor: { depositorId=4, depositorName=depositorName4, depositorDateDeposit=2014-12-02, " +
                "depositorAmountDeposit=1000, depositorAmountPlusDeposit=50, depositorAmountMinusDeposit=50, " +
                "depositorDateReturnDeposit=2014-12-03, depositorMarkReturnDeposit=0}",depositor.toString());
    }

    @Test
    public void testGetBankDepositorByNameCriteria() throws Exception {
        depositor = depositorDao.getBankDepositorByNameCriteria("depositorName4");
        LOGGER.debug("depositor = {}", depositor);

        assertEquals("BankDepositor: { depositorId=4, depositorName=depositorName4, depositorDateDeposit=2014-12-02, " +
                "depositorAmountDeposit=1000, depositorAmountPlusDeposit=50, depositorAmountMinusDeposit=50, " +
                "depositorDateReturnDeposit=2014-12-03, depositorMarkReturnDeposit=0}",depositor.toString());
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
        assertEquals(depositor.toString(), depositorDao.getBankDepositorByIdGet(depositor.getDepositorId()).toString());

        sizeAfter = rowCount(BankDepositor.class);
        LOGGER.debug("size after add = {}", sizeAfter);

        assertTrue(sizeAfter == sizeBefore+1);
    }

    @Test
    public void testUpdateBankDepositor() throws Exception {
        depositor = depositorDao.getBankDepositorByIdCriteria(3L);
        LOGGER.debug("depositor before update - {}",depositor);
        depositor.setDepositorName("name");

        depositorDao.updateBankDepositor(depositor);

        BankDepositor testDepositor = depositorDao.getBankDepositorByIdCriteria(3L);
        LOGGER.debug("depositor after update - {}",testDepositor);

        assertEquals(depositor.toString(),testDepositor.toString());
    }

    @Test
    public void testRemoveBankDepositor() throws Exception{
        sizeDepositorsBefore = rowCount(BankDepositor.class);
        LOGGER.debug("sizeDepositorsBefore = {}", sizeDepositorsBefore);

        depositorDao.removeBankDepositor(1L);

        assertNull(depositorDao.getBankDepositorByIdCriteria(1L));

        sizeDepositorsAfter = rowCount(BankDepositor.class);
        LOGGER.debug("sizeDepositorsAfter = {}", sizeDepositorsAfter);

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
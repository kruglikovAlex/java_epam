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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
        depositor = depositorDao.getBankDepositorByIdGet(8L);
        LOGGER.debug("depositor = {}", depositor);

        assertEquals("BankDepositor: { depositorId=8, depositorName=depositorName9, depositorDateDeposit=2014-12-09, " +
                "depositorAmountDeposit=996, depositorAmountPlusDeposit=90, depositorAmountMinusDeposit=90, " +
                "depositorDateReturnDeposit=2014-12-10, depositorMarkReturnDeposit=0}",depositor.toString());
    }

    @Test
    public void testGetBankDepositorByIdLoad() throws Exception {
        depositor = depositorDao.getBankDepositorByIdLoad(1L);
        LOGGER.debug("depositor = {}", depositor);

        assertEquals("BankDepositor: { depositorId=1, depositorName=depositorName1, depositorDateDeposit=2014-12-01, " +
                "depositorAmountDeposit=1001, depositorAmountPlusDeposit=20, depositorAmountMinusDeposit=20, " +
                "depositorDateReturnDeposit=2014-12-02, depositorMarkReturnDeposit=0}",depositor.toString());
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
    public void testGetBankDepositorByNameSQL() throws Exception {
        depositor = depositorDao.getBankDepositorByNameSQL("depositorName1");
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
    public void testGetBankDepositorByIdDepositCriteria() throws Exception {
        depositors = depositorDao.getBankDepositorByIdDepositCriteria(3L);
        LOGGER.debug("depositors = {}", depositors);

        assertEquals("[BankDepositor: { depositorId=5, depositorName=depositorName3, depositorDateDeposit=2014-12-03, " +
                "depositorAmountDeposit=1003, depositorAmountPlusDeposit=40, depositorAmountMinusDeposit=40, " +
                "depositorDateReturnDeposit=2014-12-04, depositorMarkReturnDeposit=0}, " +
                "BankDepositor: { depositorId=6, depositorName=depositorName8, depositorDateDeposit=2014-12-08, " +
                "depositorAmountDeposit=997, depositorAmountPlusDeposit=80, depositorAmountMinusDeposit=80, " +
                "depositorDateReturnDeposit=2014-12-09, depositorMarkReturnDeposit=0}]",depositors.toString());
    }

    @Test
    public void testGetBankDepositorByIdDepositBetweenDateDeposit() throws Exception {
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-05");
        depositors = depositorDao.getBankDepositorByIdDepositBetweenDateDeposit(4L, startDate, endDate);
        LOGGER.debug("depositors = {}", depositors);

        assertEquals("[BankDepositor: { depositorId=7, depositorName=depositorName4, depositorDateDeposit=2014-12-04," +
                " depositorAmountDeposit=1004, depositorAmountPlusDeposit=50, depositorAmountMinusDeposit=50, " +
                "depositorDateReturnDeposit=2014-12-05, depositorMarkReturnDeposit=0}]",depositors.toString());
    }

    @Test
    public void testGetBankDepositorByIdDepositBetweenDateReturnDeposit() throws Exception {
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-09");
        depositors = depositorDao.getBankDepositorByIdDepositBetweenDateReturnDeposit(4L, startDate, endDate);
        LOGGER.debug("depositors = {}", depositors);

        assertEquals("[BankDepositor: { depositorId=7, depositorName=depositorName4, depositorDateDeposit=2014-12-04, " +
                "depositorAmountDeposit=1004, depositorAmountPlusDeposit=50, depositorAmountMinusDeposit=50, " +
                "depositorDateReturnDeposit=2014-12-05, depositorMarkReturnDeposit=0}]",depositors.toString());
    }

    @Test
    public void testGetBankDepositorSumAll() {
        Integer sum=0, sumPlus=0, sumMinus = 0;
        depositor = depositorDao.getBankDepositorSumAll();
        LOGGER.debug("depositor: {}", depositor);

        depositors = depositorDao.getBankDepositorsCriteria();
        for(BankDepositor d: depositors){
            sum += d.getDepositorAmountDeposit();
            sumPlus += d.getDepositorAmountPlusDeposit();
            sumMinus += d.getDepositorAmountMinusDeposit();
        }

        assertTrue(sum == depositor.getDepositorAmountDeposit());
        assertTrue(sumPlus == depositor.getDepositorAmountPlusDeposit());
        assertTrue(sumMinus == depositor.getDepositorAmountMinusDeposit());
    }

    @Test
    public void testGetBankDepositorByIdSumAll() {
        Integer sum=0, sumPlus=0, sumMinus = 0;
        depositor = depositorDao.getBankDepositorByIdDepositSum(4L);
        LOGGER.debug("depositor: {}", depositor);

        depositors = depositorDao.getBankDepositorByIdDepositCriteria(4L);
        for(BankDepositor d: depositors){
            sum += d.getDepositorAmountDeposit();
            sumPlus += d.getDepositorAmountPlusDeposit();
            sumMinus += d.getDepositorAmountMinusDeposit();
        }

        assertTrue(sum == depositor.getDepositorAmountDeposit());
        assertTrue(sumPlus == depositor.getDepositorAmountPlusDeposit());
        assertTrue(sumMinus == depositor.getDepositorAmountMinusDeposit());
    }

    @Test
    public void testGetBankDepositorBetweenDateDepositSum() throws ParseException{
        Date startDate = dateFormat.parse("2014-12-04");
        Date endDate = dateFormat.parse("2014-12-09");
        Integer sum=0, sumPlus=0, sumMinus = 0;
        depositor = depositorDao.getBankDepositorBetweenDateDepositSum(startDate, endDate);
        LOGGER.debug("depositor: {}", depositor);

        depositors = depositorDao.getBankDepositorBetweenDateDeposit(startDate,endDate);
        for(BankDepositor d: depositors){
            sum += d.getDepositorAmountDeposit();
            sumPlus += d.getDepositorAmountPlusDeposit();
            sumMinus += d.getDepositorAmountMinusDeposit();
        }

        assertTrue(sum == depositor.getDepositorAmountDeposit());
        assertTrue(sumPlus == depositor.getDepositorAmountPlusDeposit());
        assertTrue(sumMinus == depositor.getDepositorAmountMinusDeposit());
    }

    @Test
    public void testGetBankDepositorBetweenDateReturnDepositSum() throws ParseException{
        Date startDate = dateFormat.parse("2014-12-04");
        Date endDate = dateFormat.parse("2014-12-09");
        Integer sum=0, sumPlus=0, sumMinus = 0;
        depositor = depositorDao.getBankDepositorBetweenDateReturnDepositSum(startDate, endDate);
        LOGGER.debug("depositor: {}", depositor);

        depositors = depositorDao.getBankDepositorBetweenDateReturnDeposit(startDate, endDate);
        for(BankDepositor d: depositors){
            sum += d.getDepositorAmountDeposit();
            sumPlus += d.getDepositorAmountPlusDeposit();
            sumMinus += d.getDepositorAmountMinusDeposit();
        }

        assertTrue(sum == depositor.getDepositorAmountDeposit());
        assertTrue(sumPlus == depositor.getDepositorAmountPlusDeposit());
        assertTrue(sumMinus == depositor.getDepositorAmountMinusDeposit());
    }

    @Test
    public void testGetBankDepositorByMarkReturn(){
        depositors = depositorDao.getBankDepositorByMarkReturn(0);
        LOGGER.debug("depositors: {}", depositors);
        LOGGER.debug("depositors.size: {}", depositors.size());
        List<BankDepositor> test = depositorDao.getBankDepositorsCriteria();
        LOGGER.debug("test.size: {}", test.size());

        assertTrue(depositors.size() == test.size());
        assertEquals(depositors.toString(), depositorDao.getBankDepositorsCriteria().toString());
    }

    @Test
    public void testGetBankDepositorMaxAmount(){
        depositor = depositorDao.getBankDepositorMaxAmount();
        LOGGER.debug("depositor: {}", depositor);

        assertEquals("BankDepositor: { depositorId=7, depositorName=depositorName4, depositorDateDeposit=2014-12-04, " +
                "depositorAmountDeposit=1004, depositorAmountPlusDeposit=50, depositorAmountMinusDeposit=50, " +
                "depositorDateReturnDeposit=2014-12-05, depositorMarkReturnDeposit=0}",depositor.toString());
    }

    @Test
    public void testGetBankDepositorMinAmount(){
        depositor = depositorDao.getBankDepositorMinAmount();
        LOGGER.debug("depositor: {}", depositor);

        assertEquals("BankDepositor: { depositorId=8, depositorName=depositorName9, depositorDateDeposit=2014-12-09, " +
                "depositorAmountDeposit=996, depositorAmountPlusDeposit=90, depositorAmountMinusDeposit=90, " +
                "depositorDateReturnDeposit=2014-12-10, depositorMarkReturnDeposit=0}", depositor.toString());
    }

    @Test
    public void testGetBankDepositorByIdDepositMinAmount(){
        depositor = depositorDao.getBankDepositorByIdDepositMinAmount(3L);
        LOGGER.debug("depositor: {}", depositor);

        assertEquals("BankDepositor: { depositorId=6, depositorName=depositorName8, depositorDateDeposit=2014-12-08, " +
                "depositorAmountDeposit=997, depositorAmountPlusDeposit=80, depositorAmountMinusDeposit=80, " +
                "depositorDateReturnDeposit=2014-12-09, depositorMarkReturnDeposit=0}",depositor.toString());
    }

    @Test
    public void testGetBankDepositorByIdDepositMaxAmount(){
        depositor = depositorDao.getBankDepositorByIdDepositMaxAmount(3L);
        LOGGER.debug("depositor: {}", depositor);

        assertEquals("BankDepositor: { depositorId=5, depositorName=depositorName3, depositorDateDeposit=2014-12-03, " +
                "depositorAmountDeposit=1003, depositorAmountPlusDeposit=40, depositorAmountMinusDeposit=40, " +
                "depositorDateReturnDeposit=2014-12-04, depositorMarkReturnDeposit=0}",depositor.toString());
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
        depositor = depositorDao.getBankDepositorByIdCriteria(5L);
        LOGGER.debug("depositor before update - {}",depositor);
        depositor.setDepositorName("name");

        depositorDao.updateBankDepositor(depositor);

        BankDepositor testDepositor = depositorDao.getBankDepositorByIdCriteria(5L);
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
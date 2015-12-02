package com.brest.bank.dao;

import com.brest.bank.domain.BankDepositor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})
public class BankDepositorDaoImplDBUnitTest {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private IDataSet expectedData, actualData;
    private ITable expectedTable, actualTable;
    IDataSet beforeData;

    @Autowired
    private BankDepositorDao depositorDao;

    @Autowired
    private IDatabaseTester databaseTester;

    private BankDepositor depositor = new BankDepositor();
    public List<BankDepositor> depositors = new ArrayList<BankDepositor>();

    @Before
    public void setUp() throws Exception {

        // Get the XML and set it on the databaseTester
        // Optional: get the DTD and set it on the databaseTester
        beforeData = getDataSet();
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setTearDownOperation(DatabaseOperation.NONE);
        databaseTester.setDataSet(beforeData);
        databaseTester.onSetup();
    }

    //--- для возврата тестовых значений реализуем метод по умолчанию (необходим по шаблону)
    //--- getDataSet() - возвращает значение типа IDataSet
    protected IDataSet getDataSet() throws Exception{
        return new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositInit-data.xml"));
    }

    @Test
    public void testGetBankDepositorsCriteria() throws Exception{
        LOGGER.debug("testGetBankDepositorsCriteria() - run");
        depositors = depositorDao.getBankDepositorsCriteria();

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositInit-data.xml"));
        expectedTable = expectedData.getTable("bankdepositor");

        actualData = databaseTester.getConnection().createDataSet();
        actualTable = actualData.getTable("bankdepositor");

        Assertion.assertEquals(expectedTable, actualTable);
        Assert.assertEquals(expectedData.getTable("bankdepositor").getRowCount(), depositors.size());
    }

    @Test
    public void testGetBankDepositorByIdCriteria() throws Exception{
        LOGGER.debug("testGetBankDepositorByIdCriteria() - run");
        depositor = depositorDao.getBankDepositorByIdCriteria(4L);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositFind-data.xml"));
        expectedTable = expectedData.getTable("bankdepositor");

        Assert.assertEquals(expectedTable.getValue(0,"depositorName").toString(),depositor.getDepositorName());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositorAmountDeposit").toString()) == depositor.getDepositorAmountDeposit());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositorAmountPlusDeposit").toString()) == depositor.getDepositorAmountPlusDeposit());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositorAmountPlusDeposit").toString()) == depositor.getDepositorAmountPlusDeposit());
    }

    @Test
    public void testGetBankDepositorByNameCriteria() throws Exception{
        LOGGER.debug("testGetBankDepositorByNameSQL() - run");
        depositor = depositorDao.getBankDepositorByNameCriteria("depositorName7");

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositFind-data.xml"));
        expectedTable = expectedData.getTable("bankdepositor");

        Assert.assertEquals(expectedTable.getValue(0,"depositorName").toString(),depositor.getDepositorName());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositorAmountDeposit").toString()) == depositor.getDepositorAmountDeposit());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositorAmountPlusDeposit").toString()) == depositor.getDepositorAmountPlusDeposit());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositorAmountPlusDeposit").toString()) == depositor.getDepositorAmountPlusDeposit());
    }


    @Test
    public void testAddBankDepositor() throws Exception{
        LOGGER.debug("testAddBankDepositor() - run");
        depositor = new BankDepositor();
            depositor.setDepositorName("AddDepositorName9");
            depositor.setDepositorDateDeposit(dateFormat.parse("2015-12-09"));
            depositor.setDepositorAmountDeposit(1005);
            depositor.setDepositorAmountPlusDeposit(60);
            depositor.setDepositorAmountMinusDeposit(90);
            depositor.setDepositorDateReturnDeposit(dateFormat.parse("2015-12-10"));
            depositor.setDepositorMarkReturnDeposit(0);

        LOGGER.debug("new depositor - {}",depositor);

        depositorDao.addBankDepositor(4L,depositor);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositAdd-data.xml"));
        expectedTable = expectedData.getTable("bankdepositor");

        actualData = databaseTester.getConnection().createDataSet();
        actualTable = actualData.getTable("bankdepositor");

        Assertion.assertEquals(expectedTable, actualTable);
        Assert.assertEquals(expectedData.getTable("bankdepositor").getRowCount(), actualData.getTable("bankdepositor").getRowCount());
    }

    @Test
    public void testUpdateBankDepositor() throws Exception{
        LOGGER.debug("testUpdateBankDepositor() - run");
        depositor = depositorDao.getBankDepositorByIdCriteria(3L);
        LOGGER.debug("depositor for update: {}",depositor.toString());
        depositor.setDepositorName("depositorName2Update");
        depositor.setDepositorDateDeposit(dateFormat.parse("2015-12-02"));
        depositor.setDepositorDateReturnDeposit(dateFormat.parse("2015-12-03"));

        depositorDao.updateBankDepositor(depositor);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositUpdate-data.xml"));
        expectedTable = expectedData.getTable("bankdepositor");

        actualData = databaseTester.getConnection().createDataSet();
        actualTable = actualData.getTable("bankdepositor");

        Assertion.assertEquals(expectedTable,actualTable);
    }

    @Test
    public void testRemoveBankDepositor() throws Exception{
        LOGGER.debug("testRemoveBankDepositor() - run");

        depositorDao.removeBankDepositor(3L);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositorRemove-data.xml"));
        expectedTable = expectedData.getTable("bankdepositor");

        actualData = databaseTester.getConnection().createDataSet();
        actualTable = actualData.getTable("bankdepositor");

        Assertion.assertEquals(expectedTable,actualTable);
        Assert.assertEquals(expectedData.getTable("bankdeposit").getRowCount(), actualData.getTable("bankdeposit").getRowCount());
        Assert.assertEquals(expectedData.getTable("bankdepositor").getRowCount(), actualData.getTable("bankdepositor").getRowCount());
    }

}

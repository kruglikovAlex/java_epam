package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.*;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import org.dbunit.operation.DatabaseOperation;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-DBUnit-test.xml"})
public class BankDepositDaoImplDBUnitTest {

    private static final Logger LOGGER = LogManager.getLogger();
    IDataSet expectedData, actualData;
    ITable expectedTable, actualTable;
    private IDataSet beforeData;

    @Autowired
    private BankDepositDao depositDao;

    @Autowired
    private IDatabaseTester databaseTester;

    private BankDeposit deposit = new BankDeposit();;
    public List<BankDeposit> deposits = new ArrayList<BankDeposit>();

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
    public void testGetBankDepositsCriteria() throws Exception{
        LOGGER.debug("testGetBankDepositsCriteria() - run");
        deposits = depositDao.getBankDepositsCriteria();

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositInit-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        actualData = databaseTester.getConnection().createDataSet();
        actualTable = actualData.getTable("bankdeposit");

        Assertion.assertEquals(expectedTable, actualTable);
        Assert.assertEquals(expectedData.getTable("bankdeposit").getRowCount(), deposits.size());
    }

    @Test
    public void testGetBankDepositByIdCriteria() throws Exception{
        LOGGER.debug("testGetBankDepositByIdCriteria() - run");
        deposit = depositDao.getBankDepositByIdCriteria(4L);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositFind-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        Assert.assertEquals(expectedTable.getValue(0,"depositName").toString(),deposit.getDepositName());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositMinTerm").toString()) == deposit.getDepositMinTerm());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositMinAmount").toString()) == deposit.getDepositMinAmount());
        Assert.assertEquals(expectedTable.getValue(0,"depositCurrency").toString(), deposit.getDepositCurrency());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositInterestRate").toString()) == deposit.getDepositInterestRate());
        Assert.assertEquals(expectedTable.getValue(0,"depositAddConditions").toString(), deposit.getDepositAddConditions());
    }

    @Test
    public void testGetBankDepositByNameCriteria() throws Exception{
        LOGGER.debug("testGetBankDepositByNameSQL() - run");
        deposit = depositDao.getBankDepositByNameCriteria("depositName3");

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositFind-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        Assert.assertEquals(expectedTable.getValue(0,"depositName").toString(),deposit.getDepositName());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositMinTerm").toString()) == deposit.getDepositMinTerm());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositMinAmount").toString()) == deposit.getDepositMinAmount());
        Assert.assertEquals(expectedTable.getValue(0,"depositCurrency").toString(), deposit.getDepositCurrency());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositInterestRate").toString()) == deposit.getDepositInterestRate());
        Assert.assertEquals(expectedTable.getValue(0,"depositAddConditions").toString(), deposit.getDepositAddConditions());
    }


    @Test
    public void testAddBankDeposit() throws Exception{
        LOGGER.debug("testAddBankDeposit() - run");
        deposit = new BankDeposit();
        deposit.setDepositName("name1");
        deposit.setDepositMinTerm(24);
        deposit.setDepositMinAmount(1000);
        deposit.setDepositCurrency("grb");
        deposit.setDepositInterestRate(4);
        deposit.setDepositAddConditions("condition");
        LOGGER.debug("new deposit - {}",deposit);

        depositDao.addBankDeposit(deposit);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositAdd-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        actualData = databaseTester.getConnection().createDataSet();
        actualTable = actualData.getTable("bankdeposit");

        Assertion.assertEquals(expectedTable, actualTable);
        Assert.assertEquals(expectedData.getTable("bankdeposit").getRowCount(), actualData.getTable("bankdeposit").getRowCount());
        Assert.assertEquals(expectedData.getTable("bankdepositor").getRowCount(), actualData.getTable("bankdepositor").getRowCount());
    }

}

package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;

import com.brest.bank.domain.BankDepositor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.*;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import org.dbunit.operation.DatabaseOperation;
import org.junit.*;

import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.util.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})
public class BankDepositDaoImplDBUnitTest {

    private static final Logger LOGGER = LogManager.getLogger(BankDepositDaoImplDBUnitTest.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    IDataSet expectedData, actualData;
    ITable expectedTable, actualTable;
    private IDataSet beforeData;

    @Autowired
    private BankDepositDao depositDao;

    @Autowired
    private BankDepositorDao depositorDao;

    @Autowired
    private IDatabaseTester databaseTester;

    private BankDeposit deposit = new BankDeposit();
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
    public void testGetBankDepositByCurrencyBetweenDateDepositWithDepositors() throws Exception{
        LOGGER.debug("testGetBankDepositByCurrencyBetweenDateDepositWithDepositors() - run");

        Date startDate = dateFormat.parse("2015-12-01");
        Date endDate = dateFormat.parse("2015-12-03");

        List<Map> list = depositDao.getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd",startDate,endDate);
        LOGGER.debug("list.size = {}\n{}", list.size(),list);

        Assert.assertTrue(list.size() != 0);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositCurrencyBetweenDate-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        for (int i=0; i<list.size(); i++){
            Assert.assertEquals(expectedTable.getValue(i,"depositName").toString(),list.get(i).get("depositName"));
            Assert.assertTrue(Integer.parseInt(expectedTable.getValue(i, "depositMinTerm").toString()) == Integer.parseInt(list.get(i).get("depositMinTerm").toString()));
            Assert.assertTrue(Integer.parseInt(expectedTable.getValue(i, "depositMinAmount").toString()) == Integer.parseInt(list.get(i).get("depositMinAmount").toString()));
            Assert.assertEquals(expectedTable.getValue(i, "depositCurrency").toString(), list.get(i).get("depositCurrency"));
            Assert.assertTrue(Integer.parseInt(expectedTable.getValue(i, "depositInterestRate").toString()) == Integer.parseInt(list.get(i).get("depositInterestRate").toString()));
            Assert.assertEquals(expectedTable.getValue(i,"depositAddConditions").toString(), list.get(i).get("depositAddConditions"));
        }

        Assert.assertTrue(Integer.parseInt(list.get(0).get("depositorCount").toString()) == 1);
        Assert.assertTrue(Integer.parseInt(list.get(1).get("depositorCount").toString()) == 1);

        expectedTable = expectedData.getTable("bankdepositor");
        for (int i=0; i<list.size(); i++){
            Assert.assertTrue((Integer.parseInt(expectedTable.getValue(i,"depositorAmountDeposit").toString()))==Integer.parseInt(list.get(i).get("depositorAmountSum").toString()));
            Assert.assertTrue((Integer.parseInt(expectedTable.getValue(i,"depositorAmountPlusDeposit").toString()))==Integer.parseInt(list.get(i).get("depositorAmountPlusSum").toString()));
            Assert.assertTrue((Integer.parseInt(expectedTable.getValue(i,"depositorAmountMinusDeposit").toString()))==Integer.parseInt(list.get(i).get("depositorAmountMinusSum").toString()));
        }
    }

    @Test
    public void testGetBankDepositByCurrencyBetweenDateReturnDepositWithDepositors() throws Exception{
        LOGGER.debug("testGetBankDepositByCurrencyBetweenDateReturnDepositWithDepositors() - run");

        Date startDate = dateFormat.parse("2015-12-02");
        Date endDate = dateFormat.parse("2015-12-05");

        List<Map> list = depositDao.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd",startDate,endDate);
        LOGGER.debug("list.size = {}\n{}", list.size(),list);

        Assert.assertTrue(list.size() != 0);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositCurrencyBetweenDateReturn-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        int i = 0;
        for (Map aList:list){
            Assert.assertEquals(expectedTable.getValue(i,"depositName").toString(),aList.get("depositName"));
            Assert.assertTrue(Integer.parseInt(expectedTable.getValue(i, "depositMinTerm").toString()) == Integer.parseInt(aList.get("depositMinTerm").toString()));
            Assert.assertTrue(Integer.parseInt(expectedTable.getValue(i, "depositMinAmount").toString()) == Integer.parseInt(aList.get("depositMinAmount").toString()));
            Assert.assertEquals(expectedTable.getValue(i, "depositCurrency").toString(), aList.get("depositCurrency"));
            Assert.assertTrue(Integer.parseInt(expectedTable.getValue(i, "depositInterestRate").toString()) == Integer.parseInt(aList.get("depositInterestRate").toString()));
            Assert.assertEquals(expectedTable.getValue(i,"depositAddConditions").toString(), aList.get("depositAddConditions"));
            i++;
        }

        for(Map aList:list){
            Assert.assertTrue(Integer.parseInt(aList.get("depositorCount").toString()) == 1);
        }

        expectedTable = expectedData.getTable("bankdepositor");
        i = 0;
        for (Map aList:list){
            Assert.assertTrue((Integer.parseInt(expectedTable.getValue(i,"depositorAmountDeposit").toString()))==Integer.parseInt(aList.get("depositorAmountSum").toString()));
            Assert.assertTrue((Integer.parseInt(expectedTable.getValue(i,"depositorAmountPlusDeposit").toString()))==Integer.parseInt(aList.get("depositorAmountPlusSum").toString()));
            Assert.assertTrue((Integer.parseInt(expectedTable.getValue(i,"depositorAmountMinusDeposit").toString()))==Integer.parseInt(aList.get("depositorAmountMinusSum").toString()));
            i++;
        }
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
    }

    @Test
    public void testUpdateBankDeposit() throws Exception{
        LOGGER.debug("testUpdateBankDeposit() - run");
        deposit = depositDao.getBankDepositByIdCriteria(2L);
        LOGGER.debug("deposit for update: {}",deposit);
        deposit.setDepositName("depositName1Update");
        deposit.setDepositMinTerm(24);
        deposit.setDepositCurrency("rub");
        deposit.setDepositMinAmount(1000000);

        depositDao.updateBankDeposit(deposit);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositUpdate-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        actualData = databaseTester.getConnection().createDataSet();
        actualTable = actualData.getTable("bankdeposit");

        Assertion.assertEquals(expectedTable,actualTable);
    }

    @Test
    public void testRemoveBankDeposit() throws Exception{
        LOGGER.debug("testRemoveBankDeposit() - run");

        depositDao.deleteBankDeposit(4L);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositRemove-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        actualData = databaseTester.getConnection().createDataSet();
        actualTable = actualData.getTable("bankdeposit");

        Assertion.assertEquals(expectedTable,actualTable);
        Assert.assertEquals(expectedData.getTable("bankdeposit").getRowCount(), actualData.getTable("bankdeposit").getRowCount());
        Assert.assertEquals(expectedData.getTable("bankdepositor").getRowCount(), actualData.getTable("bankdepositor").getRowCount());
    }

}

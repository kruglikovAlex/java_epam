package com.brest.bank.client;

import com.brest.bank.domain.*;
import com.brest.bank.wsdl.*;
import com.brest.bank.wsdl.BankDeposit;
import com.brest.bank.wsdl.BankDepositor;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataFixture {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Get new Bank Deposit with fixed parameters
     *
     * @return BankDeposit with fixed parameters for tests
     */
    public static com.brest.bank.domain.BankDeposit getNewDeposit(){
        com.brest.bank.domain.BankDeposit deposit = new com.brest.bank.domain.BankDeposit();
        deposit.setDepositName("depositName1");
        deposit.setDepositMinTerm(12);
        deposit.setDepositMinAmount(1000);
        deposit.setDepositCurrency("usd");
        deposit.setDepositInterestRate(4);
        deposit.setDepositAddConditions("conditions1");

        return deposit;
    }

    public static com.brest.bank.domain.BankDeposit getAddDeposit(){
        com.brest.bank.domain.BankDeposit deposit = new com.brest.bank.domain.BankDeposit();
        deposit.setDepositName("testAdd");
        deposit.setDepositMinTerm(6);
        deposit.setDepositMinAmount(1000);
        deposit.setDepositCurrency("eur");
        deposit.setDepositInterestRate(4);
        deposit.setDepositAddConditions("conditions2");
        return deposit;
    }

    /**
     * Get an exists Bank Deposit with fixed parameters
     *
     * @param id Long - id of the Bank Deposit to return
     * @return BankDeposit with fixed parameters for tests
     */
    public static BankDeposit getExistDeposit(Long id){
        BankDeposit deposit = new BankDeposit();
        deposit.setDepositId(id);
        deposit.setDepositName("depositName1");
        deposit.setDepositMinTerm(12);
        deposit.setDepositMinAmount(1000);
        deposit.setDepositCurrency("usd");
        deposit.setDepositInterestRate(4);
        deposit.setDepositAddConditions("conditions1");

        return deposit;
    }

    /**
     * Get an exists Bank Deposit with fixed parameters
     *
     * @param id Long - id of the Bank Deposit to return
     * @return BankDeposit with fixed parameters for tests
     */
    public static GetBankDepositByIdResponse getExistDepositWsdl(Long id){
        GetBankDepositByIdResponse getBankDepositByIdResponse = new GetBankDepositByIdResponse();
        BankDeposit deposit = new BankDeposit();
        deposit.setDepositId(id);
        deposit.setDepositName("depositName0");
        deposit.setDepositMinTerm(12);
        deposit.setDepositMinAmount(100);
        deposit.setDepositCurrency("usd");
        deposit.setDepositInterestRate(4);
        deposit.setDepositAddConditions("condition0");
        getBankDepositByIdResponse.setBankDeposit(deposit);

        return getBankDepositByIdResponse;
    }

    /**
     * Get an exists Bank Deposit with fixed parameters
     *
     * @param id Long - id of the Bank Deposit to return
     * @return BankDeposit with fixed parameters for tests
     */
    public static AddBankDepositResponse getAddDepositWsdl(Long id){
        AddBankDepositResponse addBankDepositResponse = new AddBankDepositResponse();
        BankDeposit deposit = new BankDeposit();
        deposit.setDepositId(id);
        deposit.setDepositName("testAdd");
        deposit.setDepositMinTerm(6);
        deposit.setDepositMinAmount(1000);
        deposit.setDepositCurrency("eur");
        deposit.setDepositInterestRate(4);
        deposit.setDepositAddConditions("conditions2");
        addBankDepositResponse.setBankDeposit(deposit);

        return addBankDepositResponse;
    }

    /**
     * Get an exists Bank Deposit with fixed parameters
     *
     * @param name String - id of the Bank Deposit to return
     * @return BankDeposit with fixed parameters for tests
     */
    public static GetBankDepositByNameWithDepositorsResponse getExistDepositByNameWithDepositorsWsdl(String name){
        GetBankDepositByNameWithDepositorsResponse getBankDepositByNameWithDepositorsResponse
                = new GetBankDepositByNameWithDepositorsResponse();
        BankDepositReport deposit = new BankDepositReport();
        deposit.setDepositId(1L);
        deposit.setDepositName(name);
        deposit.setDepositMinTerm(12);
        deposit.setDepositMinAmount(100);
        deposit.setDepositCurrency("usd");
        deposit.setDepositInterestRate(4);
        deposit.setDepositAddConditions("condition0");
        deposit.setDepositorAmountSum(200);
        deposit.setDepositorAmountPlusSum(200);
        deposit.setDepositorAmountMinusSum(200);
        getBankDepositByNameWithDepositorsResponse.setBankDepositReport(deposit);

        return getBankDepositByNameWithDepositorsResponse;
    }

    /**
     * Get an exists Bank Deposits with fixed parameters
     *
     * @param currency String - currency of the Bank Deposits to return
     * @return BankDeposits with fixed parameters for tests
     */
    public static GetBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse getExistDepositByCurrencyWsdl(String currency){
        GetBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse getBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse
                = new GetBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse();
        BankDepositReport deposit = new BankDepositReport();
        deposit.setDepositId(1L);
        deposit.setDepositName("depositName");
        deposit.setDepositMinTerm(12);
        deposit.setDepositMinAmount(100);
        deposit.setDepositCurrency(currency);
        deposit.setDepositInterestRate(4);
        deposit.setDepositAddConditions("condition0");
        deposit.setDepositorAmountSum(200);
        deposit.setDepositorAmountPlusSum(200);
        deposit.setDepositorAmountMinusSum(200);

        BankDepositsReport bankDepositsReport = new BankDepositsReport();
        bankDepositsReport.getBankDepositReport().add(deposit);
        getBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse.setBankDepositsReport(bankDepositsReport);

        return getBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse;
    }

    /**
     * Get an exists update Bank Deposit with fixed parameters
     *
     * @param id Long - id of the Bank Deposit to return
     * @return BankDeposit with fixed parameters for tests
     */
    public static com.brest.bank.domain.BankDeposit getExistUpdateDomainDeposit(Long id){
        com.brest.bank.domain.BankDeposit deposit = new com.brest.bank.domain.BankDeposit();
        deposit.setDepositName("updateName1");
        deposit.setDepositMinTerm(12);
        deposit.setDepositMinAmount(1000);
        deposit.setDepositCurrency("usd");
        deposit.setDepositInterestRate(4);
        deposit.setDepositAddConditions("conditions1");

        deposit.setDepositId(id);

        return deposit;
    }

    /**
     * Get an exists update Bank Deposit with fixed parameters
     *
     * @param id Long - id of the Bank Deposit to return
     * @return BankDeposit with fixed parameters for tests
     */
    public static BankDeposit getExistUpdateDeposit(Long id){
        BankDeposit deposit = new BankDeposit();
        deposit.setDepositName("updateName1");
        deposit.setDepositMinTerm(12);
        deposit.setDepositMinAmount(1000);
        deposit.setDepositCurrency("usd");
        deposit.setDepositInterestRate(4);
        deposit.setDepositAddConditions("conditions1");

        deposit.setDepositId(id);

        return deposit;
    }

    /**
     * Get a Bank Deposit with null parameters
     *
     * @return BankDeposit with null parameters
     */
    public static BankDeposit getNullDeposit() {
        return null;
    }

    /**
     * Get empty Bank Deposit
     *
     * @return BankDeposit empty
     */
    public static BankDeposit getEmptyDeposit() {
        return new BankDeposit();
    }

    /**
     * Get all Bank Deposits
     *
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     */
    public static List<com.brest.bank.domain.BankDeposit> getDeposits(){
        List<com.brest.bank.domain.BankDeposit> deposits = new ArrayList<com.brest.bank.domain.BankDeposit>();
        deposits.add(getNewDeposit());
        return deposits;
    }

    /**
     * Get all Bank Deposits
     *
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     */
    public static GetBankDepositsResponse getDepositsWsdl(){
        GetBankDepositsResponse getBankDepositsResponse = new GetBankDepositsResponse();
        BankDeposits bankDeposits = new BankDeposits();
        BankDeposit bankDeposit = new BankDeposit();
        bankDeposit.setDepositId(1L);
        bankDeposit.setDepositName("depositName0");
        bankDeposit.setDepositMinTerm(12);
        bankDeposit.setDepositMinAmount(100);
        bankDeposit.setDepositCurrency("usd");
        bankDeposit.setDepositInterestRate(4);
        bankDeposit.setDepositAddConditions("condition0");
        bankDeposits.getBankDeposit().add(bankDeposit);

        getBankDepositsResponse.setBankDeposits(bankDeposits);
        return getBankDepositsResponse;
    }

    /**
     * Get all Bank Deposits
     *
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     */
    public static GetBankDepositorsResponse getDepositorsWsdl() throws DatatypeConfigurationException{
        GetBankDepositorsResponse getBankDepositorsResponse = new GetBankDepositorsResponse();
        BankDepositors bankDepositors = new BankDepositors();
        BankDepositor bankDepositor = new BankDepositor();
        bankDepositor.setDepositorId(1L);
        bankDepositor.setDepositorName("depositorName0");
        bankDepositor.setDepositorDateDeposit(DatatypeFactory.newInstance().newXMLGregorianCalendar("2015-01-01"));
        bankDepositor.setDepositorDateReturnDeposit(DatatypeFactory.newInstance().newXMLGregorianCalendar("2017-01-01"));
        bankDepositor.setDepositorAmountDeposit(1000);
        bankDepositor.setDepositorAmountPlusDeposit(1000);
        bankDepositor.setDepositorAmountMinusDeposit(1000);
        bankDepositor.setDepositorMarkReturnDeposit(0);
        bankDepositors.getBankDepositor().add(bankDepositor);

        getBankDepositorsResponse.setBankDepositors(bankDepositors);
        return getBankDepositorsResponse;
    }

    /**
     * Get exist Bank Deposits
     *
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     */
    public static List<BankDeposit> getExistDeposits(){
        List<BankDeposit> deposits = new ArrayList<BankDeposit>();
        deposits.add(getExistDeposit(1L));
        return deposits;
    }


    /**
     * Get exist Bank Deposit with all Bank Depositors
     *
     * @param id Long - id of Bank Deposit
     * @param idd Long - id of Bank Depositor
     * @returne Map - a bank deposit with a report on all relevant
     * bank depositors
     * @throws ParseException
     */
    public static Map getExistDepositAllDepositors(Long id, Long idd) throws ParseException, DatatypeConfigurationException{
        BankDeposit deposit = getExistDeposit(id);
        com.brest.bank.domain.BankDepositor depositor = getExistDepositor(idd);
        Map<String, Object> list = new HashMap<String, Object>(11);
            list.put("depositId", deposit.getDepositId());
            list.put("depositName", deposit.getDepositName());
            list.put("depositMinTerm", deposit.getDepositMinTerm());
            list.put("depositMinAmount", deposit.getDepositMinAmount());
            list.put("depositCurrency", deposit.getDepositCurrency());
            list.put("depositInterestRate", deposit.getDepositInterestRate());
            list.put("depositAddConditions", deposit.getDepositAddConditions());
            list.put("depositorCount", 1);
            list.put("depositorAmountSum", depositor.getDepositorAmountDeposit());
            list.put("depositorAmountPlusSum", depositor.getDepositorAmountPlusDeposit());
            list.put("depositorAmountMinusSum", depositor.getDepositorAmountMinusDeposit());

        return list;
    }

    /**
     * Get all Bank Deposits with all Bank Depositors
     *
     * @return List<Map> - a list of bank deposits with a report on all relevant
     * bank depositors
     * @throws ParseException
     */
    public static List<Map> getExistAllDepositsAllDepositors() throws ParseException, DatatypeConfigurationException{
        List<Map> deposits = new ArrayList<Map>();
        deposits.add(getExistDepositAllDepositors(1L, 1L));
        return deposits;
    }

    /**
     * Get new Bank Depositor with fixed parameters
     *
     * @return BankDepositor with fixed parameters for tests
     */
    public static com.brest.bank.domain.BankDepositor getNewDepositor() throws ParseException, DatatypeConfigurationException {
        com.brest.bank.domain.BankDepositor depositor = new com.brest.bank.domain.BankDepositor();
        depositor.setDepositorName("depositorName1");
        depositor.setDepositorDateDeposit(dateFormat.parse("2015-01-01"));
        depositor.setDepositorAmountDeposit(1000);
        depositor.setDepositorAmountPlusDeposit(100);
        depositor.setDepositorAmountMinusDeposit(100);
        depositor.setDepositorDateDeposit(dateFormat.parse("2015-09-09"));
        depositor.setDepositorMarkReturnDeposit(0);

        return depositor;
    }

    /**
     * Get a Bank Depositor with null parameters
     *
     * @return BankDepositor with null parameters
     */
    public static BankDepositor getNullDepositor() {
        return null;
    }

    /**
     * Get empty Bank Depositor
     *
     * @return BankDepositor empty
     */
    public static BankDepositor getEmptyDepositor() {
        return new BankDepositor();
    }

    /**
     * Get an exists Bank Depositor with fixed parameters
     *
     * @param id Long - id of the Bank Depositor to return
     * @return BankDepositor with fixed parameters for tests
     */
    public static com.brest.bank.domain.BankDepositor getExistDepositor(Long id) throws ParseException{
        com.brest.bank.domain.BankDepositor depositor = new com.brest.bank.domain.BankDepositor();
        depositor.setDepositorId(id);
        depositor.setDepositorName("depositorName"+id);
        depositor.setDepositorDateDeposit(dateFormat.parse("2015-01-01"));
        depositor.setDepositorAmountDeposit(1000);
        depositor.setDepositorAmountPlusDeposit(100);
        depositor.setDepositorAmountMinusDeposit(100);
        depositor.setDepositorDateReturnDeposit(dateFormat.parse("2015-09-09"));
        depositor.setDepositorMarkReturnDeposit(0);

        return depositor;
    }

    /**
     * Get an exists update Bank Depositor with fixed parameters
     *
     * @param id Long - id of the Bank Depositor to return
     * @return BankDepositor with fixed parameters for tests
     */
    public static com.brest.bank.domain.BankDepositor getExistUpdateDepositor(Long id) throws ParseException{
        com.brest.bank.domain.BankDepositor depositor = new com.brest.bank.domain.BankDepositor();
        depositor.setDepositorId(id);
        depositor.setDepositorName("updateName"+id);
        depositor.setDepositorDateDeposit(dateFormat.parse("2015-01-01"));
        depositor.setDepositorAmountDeposit(1000);
        depositor.setDepositorAmountPlusDeposit(100);
        depositor.setDepositorAmountMinusDeposit(100);
        depositor.setDepositorDateDeposit(dateFormat.parse("2015-09-09"));
        depositor.setDepositorMarkReturnDeposit(0);

        return depositor;
    }

    /**
     * Get all Bank Depositors
     *
     * @return List<BankDepositor> - a list containing all of the Bank Depositors in the database
     */
    public static List<com.brest.bank.domain.BankDepositor> getDepositors() throws ParseException, DatatypeConfigurationException{
        List<com.brest.bank.domain.BankDepositor> depositors = new ArrayList<com.brest.bank.domain.BankDepositor>();
        depositors.add(getNewDepositor());
        return depositors;
    }

    /**
     * Get exist Bank Depositors
     *
     * @return List<BankDepositor> - a list containing all of the Bank Depositors in the database
     * @throws ParseException
     */
    public static List<com.brest.bank.domain.BankDepositor> getExistDepositors() throws ParseException{
        List<com.brest.bank.domain.BankDepositor> depositors = new ArrayList<com.brest.bank.domain.BankDepositor>();
            depositors.add(getExistDepositor(1L));
            depositors.add(getExistDepositor(2L));
        return depositors;
    }
}
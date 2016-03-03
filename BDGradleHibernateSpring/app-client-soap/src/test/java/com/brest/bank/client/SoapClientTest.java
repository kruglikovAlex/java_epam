package com.brest.bank.client;

import com.brest.bank.util.BankDeposit;
import com.brest.bank.util.BankDepositReport;
import com.brest.bank.util.BankDeposits;

import com.brest.bank.util.BankDepositsReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.xml.transform.StringSource;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

import static org.springframework.ws.test.client.RequestMatchers.*;
import static org.springframework.ws.test.client.ResponseCreators.*;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-soap-client-test.xml"})
public class SoapClientTest {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

    //private static final String HOST = "http://host/";

    @Autowired
    private SoapClient soapClient;

    @Autowired
    private Jaxb2Marshaller marshaller;

    private MockWebServiceServer mockServer;

    @Before
    public void setUp() throws Exception {
        //soapClient.setDefaultUri(HOST);
        //soapClient.setMarshaller(marshaller);
        //soapClient.setUnmarshaller(marshaller);
        mockServer = MockWebServiceServer.createServer(soapClient);
    }

    @After
    public void tearDown() throws Exception {
        mockServer.verify();
    }

    @Test
    public void testGetBankDeposits(){
        Source requestPayload = new StringSource(
                "<getBankDepositsRequest xmlns='http://bank.brest.com/soap'>" +
                "</getBankDepositsRequest>");

        Source responsePayload = new StringSource(
                "<getBankDepositsResponse xmlns=\"http://bank.brest.com/soap\">" +
                        "<bankDeposits>" +
                            "<bankDeposit>" +
                                "<depositId>1</depositId>" +
                                "<depositName>depositName1</depositName>" +
                                "<depositMinTerm>12</depositMinTerm>" +
                                "<depositMinAmount>1000</depositMinAmount>" +
                                "<depositCurrency>usd</depositCurrency>" +
                                "<depositInterestRate>4</depositInterestRate>" +
                                "<depositAddConditions>conditions1</depositAddConditions>" +
                            "</bankDeposit>"+
                        "</bankDeposits>" +
                "</getBankDepositsResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDeposits deposits = soapClient.getBankDeposits();
        LOGGER.debug("Response - deposits - {}",xmlEntityToString(deposits.getBankDeposit().get(0)));

        assertEquals(DataFixture.getExistDeposit(1L).toString(),xmlEntityToString(deposits.getBankDeposit().get(0)));
    }

    @Test
    public void testGetBankDepositById() throws Exception {
        Source requestPayload = new StringSource(
                "<ns2:getBankDepositByIdRequest xmlns:ns2='http://bank.brest.com/soap'>" +
                        "<ns2:depositId>1</ns2:depositId>" +
                "</ns2:getBankDepositByIdRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositByIdResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDeposit>" +
                            "<ns2:depositId>1</ns2:depositId>" +
                            "<ns2:depositName>depositName1</ns2:depositName>" +
                            "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                            "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                            "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                            "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                            "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                        "</ns2:bankDeposit>" +
                "</ns2:getBankDepositByIdResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDeposit deposit = soapClient.getBankDepositById(1L);
        LOGGER.debug("Response - deposit - {}",xmlEntityToString(deposit));

        assertEquals(DataFixture.getExistDeposit(1L).getDepositId(),deposit.getDepositId());
        assertEquals(DataFixture.getExistDeposit(1L).getDepositName(),deposit.getDepositName());
        assertEquals(DataFixture.getExistDeposit(1L).toString(),xmlEntityToString(deposit));
    }

    @Test
    public void testGetBankDepositByName() throws Exception {
        Source requestPayload = new StringSource(
                "<ns2:getBankDepositByNameRequest xmlns:ns2='http://bank.brest.com/soap'>" +
                        "<ns2:depositName>depositName1</ns2:depositName>" +
                "</ns2:getBankDepositByNameRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositByNameResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDeposit>" +
                            "<ns2:depositId>1</ns2:depositId>" +
                            "<ns2:depositName>depositName1</ns2:depositName>" +
                            "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                            "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                            "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                            "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                            "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                        "</ns2:bankDeposit>" +
                "</ns2:getBankDepositByNameResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDeposit deposit = soapClient.getBankDepositByName("depositName1");
        LOGGER.debug("Response - deposit - {}",xmlEntityToString(deposit));

        assertNotNull(deposit);
        assertEquals(DataFixture.getExistDeposit(1L).getDepositId(),deposit.getDepositId());
        assertEquals(DataFixture.getExistDeposit(1L).getDepositName(),deposit.getDepositName());
        assertEquals(DataFixture.getExistDeposit(1L).toString(),xmlEntityToString(deposit));
    }

    @Test
    public void testGetBankDepositsByCurrency(){
        Source requestPayload = new StringSource(
                "<getBankDepositsByCurrencyRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositCurrency>usd</depositCurrency>" +
                        "</getBankDepositsByCurrencyRequest>");

        Source responsePayload = new StringSource(
                "<getBankDepositsByCurrencyResponse xmlns=\"http://bank.brest.com/soap\">" +
                        "<bankDeposits>" +
                            "<bankDeposit>" +
                                "<depositId>1</depositId>" +
                                "<depositName>depositName1</depositName>" +
                                "<depositMinTerm>12</depositMinTerm>" +
                                "<depositMinAmount>1000</depositMinAmount>" +
                                "<depositCurrency>usd</depositCurrency>" +
                                "<depositInterestRate>4</depositInterestRate>" +
                                "<depositAddConditions>conditions1</depositAddConditions>" +
                            "</bankDeposit>"+
                        "</bankDeposits>" +
                "</getBankDepositsByCurrencyResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDeposits deposits = soapClient.getBankDepositsByCurrency("usd");
        LOGGER.debug("Response - deposits - {}",xmlEntityToString(deposits.getBankDeposit().get(0)));

        assertEquals(DataFixture.getExistDeposit(1L).toString(),xmlEntityToString(deposits.getBankDeposit().get(0)));
    }

    @Test
    public void testGetBankDepositsByInterestRate(){
        Source requestPayload = new StringSource(
                "<getBankDepositsByInterestRateRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositInterestRate>4</depositInterestRate>" +
                "</getBankDepositsByInterestRateRequest>");

        Source responsePayload = new StringSource(
                "<getBankDepositsByInterestRateResponse xmlns=\"http://bank.brest.com/soap\">" +
                        "<bankDeposits>" +
                        "<bankDeposit>" +
                        "<depositId>1</depositId>" +
                        "<depositName>depositName1</depositName>" +
                        "<depositMinTerm>12</depositMinTerm>" +
                        "<depositMinAmount>1000</depositMinAmount>" +
                        "<depositCurrency>usd</depositCurrency>" +
                        "<depositInterestRate>4</depositInterestRate>" +
                        "<depositAddConditions>conditions1</depositAddConditions>" +
                        "</bankDeposit>"+
                        "</bankDeposits>" +
                "</getBankDepositsByInterestRateResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDeposits deposits = soapClient.getBankDepositsByInterestRate(4);
        LOGGER.debug("Response - deposits - {}",xmlEntityToString(deposits.getBankDeposit().get(0)));

        assertEquals(DataFixture.getExistDeposit(1L).toString(),xmlEntityToString(deposits.getBankDeposit().get(0)));

    }

    @Test
    public void testGetBankDepositsFromToMinTerm(){
        Source requestPayload = new StringSource(
                "<getBankDepositsFromToMinTermRequest xmlns='http://bank.brest.com/soap'>" +
                        "<fromTerm>10</fromTerm>" +
                        "<toTerm>12</toTerm>" +
                "</getBankDepositsFromToMinTermRequest>");

        Source responsePayload = new StringSource(
                "<getBankDepositsFromToMinTermResponse xmlns=\"http://bank.brest.com/soap\">" +
                        "<bankDeposits>" +
                            "<bankDeposit>" +
                                "<depositId>1</depositId>" +
                                "<depositName>depositName1</depositName>" +
                                "<depositMinTerm>12</depositMinTerm>" +
                                "<depositMinAmount>1000</depositMinAmount>" +
                                "<depositCurrency>usd</depositCurrency>" +
                                "<depositInterestRate>4</depositInterestRate>" +
                                "<depositAddConditions>conditions1</depositAddConditions>" +
                            "</bankDeposit>"+
                        "</bankDeposits>" +
                "</getBankDepositsFromToMinTermResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDeposits deposits = soapClient.getBankDepositsFromToMinTerm(10,12);
        LOGGER.debug("Response - deposits - {}",xmlEntityToString(deposits.getBankDeposit().get(0)));

        assertEquals(DataFixture.getExistDeposit(1L).toString(),xmlEntityToString(deposits.getBankDeposit().get(0)));

    }

    @Test
    public void testGetBankDepositsFromToInterestRate(){
        Source requestPayload = new StringSource(
                "<getBankDepositsFromToInterestRateRequest xmlns='http://bank.brest.com/soap'>" +
                        "<startRate>2</startRate>" +
                        "<endRate>4</endRate>" +
                        "</getBankDepositsFromToInterestRateRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositsFromToInterestRateResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDeposits>" +
                            "<ns2:bankDeposit>" +
                                "<ns2:depositId>1</ns2:depositId>" +
                                "<ns2:depositName>depositName1</ns2:depositName>" +
                                "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                                "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                                "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                                "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                                "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                            "</ns2:bankDeposit>" +
                        "</ns2:bankDeposits>" +
                "</ns2:getBankDepositsFromToInterestRateResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDeposits deposits = soapClient.getBankDepositsFromToInterestRate(2,4);
        LOGGER.debug("Response - deposits - {}",xmlEntityToString(deposits.getBankDeposit().get(0)));

        assertEquals(DataFixture.getExistDeposit(1L).toString(),xmlEntityToString(deposits.getBankDeposit().get(0)));
    }

    @Test
    public void testGetBankDepositsFromToDateDeposit() throws DatatypeConfigurationException, ParseException{
        Source requestPayload = new StringSource(
                "<getBankDepositsFromToDateDepositRequest xmlns='http://bank.brest.com/soap'>" +
                        "<startDate>2015-01-01</startDate>" +
                        "<endDate>2015-02-02</endDate>" +
                "</getBankDepositsFromToDateDepositRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositsFromToDateDepositResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDeposits>" +
                            "<ns2:bankDeposit>" +
                                "<ns2:depositId>1</ns2:depositId>" +
                                "<ns2:depositName>depositName1</ns2:depositName>" +
                                "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                                "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                                "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                                "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                                "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                            "</ns2:bankDeposit>" +
                        "</ns2:bankDeposits>" +
                "</ns2:getBankDepositsFromToDateDepositResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDeposits deposits = soapClient.getBankDepositsFromToDateDeposit(dateFormat.parse("2015-01-01"),
                dateFormat.parse("2015-02-02"));
        LOGGER.debug("Response - deposits - {}",xmlEntityToString(deposits.getBankDeposit().get(0)));

        assertEquals(DataFixture.getExistDeposit(1L).toString(),xmlEntityToString(deposits.getBankDeposit().get(0)));
    }

    @Test
    public void testGetBankDepositsFromToDateReturnDeposit() throws DatatypeConfigurationException, ParseException{
        Source requestPayload = new StringSource(
                "<getBankDepositsFromToDateReturnDepositRequest xmlns='http://bank.brest.com/soap'>" +
                        "<startDate>2016-02-15</startDate>" +
                        "<endDate>2016-02-20</endDate>" +
                "</getBankDepositsFromToDateReturnDepositRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositsFromToDateReturnDepositResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDeposits>" +
                            "<ns2:bankDeposit>" +
                                "<ns2:depositId>1</ns2:depositId>" +
                                "<ns2:depositName>depositName1</ns2:depositName>" +
                                "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                                "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                                "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                                "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                                "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                            "</ns2:bankDeposit>" +
                        "</ns2:bankDeposits>" +
                "</ns2:getBankDepositsFromToDateReturnDepositResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDeposits deposits = soapClient.getBankDepositsFromToDateReturnDeposit(dateFormat.parse("2016-02-15"),
                dateFormat.parse("2016-02-20"));
        LOGGER.debug("Response - deposits - {}",xmlEntityToString(deposits.getBankDeposit().get(0)));

        assertEquals(DataFixture.getExistDeposit(1L).toString(),xmlEntityToString(deposits.getBankDeposit().get(0)));
    }

    @Test
    public void testGetBankDepositByNameWithDepositors() throws ParseException{
        Source requestPayload = new StringSource(
                "<getBankDepositByNameWithDepositorsRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositName>depositName1</depositName>" +
                "</getBankDepositByNameWithDepositorsRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositByNameWithDepositorsResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDepositReport>" +
                            "<ns2:depositId>1</ns2:depositId>" +
                            "<ns2:depositName>depositName1</ns2:depositName>" +
                            "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                            "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                            "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                            "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                            "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                            "<ns2:depositorCount>1</ns2:depositorCount>" +
                            "<ns2:depositorAmountSum>1000</ns2:depositorAmountSum>" +
                            "<ns2:depositorAmountPlusSum>100</ns2:depositorAmountPlusSum>" +
                            "<ns2:depositorAmountMinusSum>100</ns2:depositorAmountMinusSum>" +
                        "</ns2:bankDepositReport>" +
                "</ns2:getBankDepositByNameWithDepositorsResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDepositReport deposit = soapClient.getBankDepositByNameWithDepositors("depositName1");
        LOGGER.debug("Response - deposit - {}",xmlEntityToString(deposit));

        assertNotNull(deposit);
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositId"),deposit.getDepositId());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositName"),deposit.getDepositName());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorCount"),deposit.getDepositorCount());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountSum"),deposit.getDepositorAmountSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountPlusSum"),deposit.getDepositorAmountPlusSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountMinusSum"),deposit.getDepositorAmountMinusSum());
    }

    @Test
    public void getBankDepositByNameFromToDateDepositWithDepositors()
            throws ParseException, DatatypeConfigurationException{
        Source requestPayload = new StringSource(
                "<getBankDepositByNameFromToDateDepositWithDepositorsRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositName>depositName1</depositName>" +
                        "<startDate>2015-01-01</startDate>" +
                        "<endDate>2016-02-10</endDate>" +
                "</getBankDepositByNameFromToDateDepositWithDepositorsRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositByNameFromToDateDepositWithDepositorsResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDepositReport>" +
                            "<ns2:depositId>1</ns2:depositId>" +
                            "<ns2:depositName>depositName1</ns2:depositName>" +
                            "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                            "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                            "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                            "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                            "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                            "<ns2:depositorCount>1</ns2:depositorCount>" +
                            "<ns2:depositorAmountSum>1000</ns2:depositorAmountSum>" +
                            "<ns2:depositorAmountPlusSum>100</ns2:depositorAmountPlusSum>" +
                            "<ns2:depositorAmountMinusSum>100</ns2:depositorAmountMinusSum>" +
                        "</ns2:bankDepositReport>" +
                "</ns2:getBankDepositByNameFromToDateDepositWithDepositorsResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDepositReport deposit = soapClient.getBankDepositByNameFromToDateDepositWithDepositors("depositName1",
                dateFormat.parse("2015-01-01"), dateFormat.parse("2016-02-10"));
        LOGGER.debug("Response - deposit - {}",xmlEntityToString(deposit));

        assertNotNull(deposit);
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositId"),deposit.getDepositId());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositName"),deposit.getDepositName());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorCount"),deposit.getDepositorCount());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountSum"),deposit.getDepositorAmountSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountPlusSum"),deposit.getDepositorAmountPlusSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountMinusSum"),deposit.getDepositorAmountMinusSum());
    }

    @Test
    public void getBankDepositByNameFromToDateReturnDepositWithDepositors()
            throws ParseException, DatatypeConfigurationException{
        Source requestPayload = new StringSource(
                "<getBankDepositByNameFromToDateReturnDepositWithDepositorsRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositName>depositName1</depositName>" +
                        "<startDate>2015-05-06</startDate>" +
                        "<endDate>2016-01-12</endDate>" +
                "</getBankDepositByNameFromToDateReturnDepositWithDepositorsRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositByNameFromToDateReturnDepositWithDepositorsResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDepositReport>" +
                            "<ns2:depositId>1</ns2:depositId>" +
                            "<ns2:depositName>depositName1</ns2:depositName>" +
                            "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                            "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                            "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                            "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                            "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                            "<ns2:depositorCount>1</ns2:depositorCount>" +
                            "<ns2:depositorAmountSum>1000</ns2:depositorAmountSum>" +
                            "<ns2:depositorAmountPlusSum>100</ns2:depositorAmountPlusSum>" +
                            "<ns2:depositorAmountMinusSum>100</ns2:depositorAmountMinusSum>" +
                        "</ns2:bankDepositReport>" +
                "</ns2:getBankDepositByNameFromToDateReturnDepositWithDepositorsResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDepositReport deposit = soapClient.getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1",
                dateFormat.parse("2015-05-06"), dateFormat.parse("2016-01-12"));
        LOGGER.debug("Response - deposit - {}",xmlEntityToString(deposit));

        assertNotNull(deposit);
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositId"),deposit.getDepositId());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositName"),deposit.getDepositName());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorCount"),deposit.getDepositorCount());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountSum"),deposit.getDepositorAmountSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountPlusSum"),deposit.getDepositorAmountPlusSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountMinusSum"),deposit.getDepositorAmountMinusSum());
    }

    @Test
    public void testGetBankDepositByIdWithDepositors() throws ParseException{
        Source requestPayload = new StringSource(
                "<getBankDepositByIdWithDepositorsRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositId>1</depositId>" +
                        "</getBankDepositByIdWithDepositorsRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositByIdWithDepositorsResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDepositReport>" +
                        "<ns2:depositId>1</ns2:depositId>" +
                        "<ns2:depositName>depositName1</ns2:depositName>" +
                        "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                        "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                        "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                        "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                        "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                        "<ns2:depositorCount>1</ns2:depositorCount>" +
                        "<ns2:depositorAmountSum>1000</ns2:depositorAmountSum>" +
                        "<ns2:depositorAmountPlusSum>100</ns2:depositorAmountPlusSum>" +
                        "<ns2:depositorAmountMinusSum>100</ns2:depositorAmountMinusSum>" +
                        "</ns2:bankDepositReport>" +
                        "</ns2:getBankDepositByIdWithDepositorsResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDepositReport deposit = soapClient.getBankDepositByIdWithDepositors(1L);
        LOGGER.debug("Response - deposit - {}",xmlEntityToString(deposit));

        assertNotNull(deposit);
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositId"),deposit.getDepositId());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositName"),deposit.getDepositName());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorCount"),deposit.getDepositorCount());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountSum"),deposit.getDepositorAmountSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountPlusSum"),deposit.getDepositorAmountPlusSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountMinusSum"),deposit.getDepositorAmountMinusSum());
    }

    @Test
    public void getBankDepositByIdFromToDateDepositWithDepositors() throws ParseException{
        Source requestPayload = new StringSource(
                "<getBankDepositByIdFromToDateDepositWithDepositorsRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositId>1</depositId>" +
                        "<startDate>2015-01-01</startDate>" +
                        "<endDate>2015-03-03</endDate>" +
                "</getBankDepositByIdFromToDateDepositWithDepositorsRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositByIdFromToDateDepositWithDepositorsResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDepositReport>" +
                            "<ns2:depositId>1</ns2:depositId>" +
                            "<ns2:depositName>depositName1</ns2:depositName>" +
                            "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                            "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                            "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                            "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                            "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                            "<ns2:depositorCount>1</ns2:depositorCount>" +
                            "<ns2:depositorAmountSum>1000</ns2:depositorAmountSum>" +
                            "<ns2:depositorAmountPlusSum>100</ns2:depositorAmountPlusSum>" +
                            "<ns2:depositorAmountMinusSum>100</ns2:depositorAmountMinusSum>" +
                        "</ns2:bankDepositReport>" +
                "</ns2:getBankDepositByIdFromToDateDepositWithDepositorsResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDepositReport deposit = soapClient.getBankDepositByIdFromToDateDepositWithDepositors(1L,
                dateFormat.parse("2015-01-01"), dateFormat.parse("2015-03-03"));
        LOGGER.debug("Response - deposit - {}",xmlEntityToString(deposit));

        assertNotNull(deposit);
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositId"),deposit.getDepositId());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositName"),deposit.getDepositName());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorCount"),deposit.getDepositorCount());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountSum"),deposit.getDepositorAmountSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountPlusSum"),deposit.getDepositorAmountPlusSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountMinusSum"),deposit.getDepositorAmountMinusSum());
    }

    @Test
    public void getBankDepositByIdFromToDateReturnDepositWithDepositors() throws ParseException{
        Source requestPayload = new StringSource(
                "<getBankDepositByIdFromToDateReturnDepositWithDepositorsRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositId>1</depositId>" +
                        "<startDate>2015-01-01</startDate>" +
                        "<endDate>2015-02-02</endDate>" +
                "</getBankDepositByIdFromToDateReturnDepositWithDepositorsRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositByIdFromToDateReturnDepositWithDepositorsResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDepositReport>" +
                            "<ns2:depositId>1</ns2:depositId>" +
                            "<ns2:depositName>depositName1</ns2:depositName>" +
                            "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                            "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                            "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                            "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                            "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                            "<ns2:depositorCount>1</ns2:depositorCount>" +
                            "<ns2:depositorAmountSum>1000</ns2:depositorAmountSum>" +
                            "<ns2:depositorAmountPlusSum>100</ns2:depositorAmountPlusSum>" +
                            "<ns2:depositorAmountMinusSum>100</ns2:depositorAmountMinusSum>" +
                        "</ns2:bankDepositReport>" +
                "</ns2:getBankDepositByIdFromToDateReturnDepositWithDepositorsResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDepositReport deposit = soapClient.getBankDepositByIdFromToDateReturnDepositWithDepositors(1L,
                dateFormat.parse("2015-01-01"), dateFormat.parse("2015-02-02"));
        LOGGER.debug("Response - deposit - {}",xmlEntityToString(deposit));

        assertNotNull(deposit);
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositId"),deposit.getDepositId());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositName"),deposit.getDepositName());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorCount"),deposit.getDepositorCount());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountSum"),deposit.getDepositorAmountSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountPlusSum"),deposit.getDepositorAmountPlusSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountMinusSum"),deposit.getDepositorAmountMinusSum());
    }

    @Test
    public void testGetBankDepositsWithDepositors() throws ParseException{
        Source requestPayload = new StringSource(
                "<getBankDepositsWithDepositorsRequest xmlns='http://bank.brest.com/soap'>" +
                "</getBankDepositsWithDepositorsRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositsWithDepositorsResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDepositsReport>" +
                            "<ns2:bankDepositReport>" +
                                "<ns2:depositId>1</ns2:depositId>" +
                                "<ns2:depositName>depositName1</ns2:depositName>" +
                                "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                                "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                                "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                                "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                                "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                                "<ns2:depositorCount>1</ns2:depositorCount>" +
                                "<ns2:depositorAmountSum>1000</ns2:depositorAmountSum>" +
                                "<ns2:depositorAmountPlusSum>100</ns2:depositorAmountPlusSum>" +
                                "<ns2:depositorAmountMinusSum>100</ns2:depositorAmountMinusSum>" +
                            "</ns2:bankDepositReport>" +
                        "</ns2:bankDepositsReport>" +
                "</ns2:getBankDepositsWithDepositorsResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDepositsReport deposits = soapClient.getBankDepositsWithDepositors();
        LOGGER.debug("Response - deposits - {}",xmlEntityToString(deposits.getBankDepositReport().get(0)));

        assertNotNull(deposits);
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositId"),deposits.getBankDepositReport().get(0).getDepositId());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositName"),deposits.getBankDepositReport().get(0).getDepositName());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorCount"),deposits.getBankDepositReport().get(0).getDepositorCount());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountSum"),deposits.getBankDepositReport().get(0).getDepositorAmountSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountPlusSum"),deposits.getBankDepositReport().get(0).getDepositorAmountPlusSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountMinusSum"),deposits.getBankDepositReport().get(0).getDepositorAmountMinusSum());
    }

    @Test
    public void testGetBankDepositsFromToDateDepositWithDepositors() throws ParseException{
        Source requestPayload = new StringSource(
                "<getBankDepositsFromToDateDepositWithDepositorsRequest xmlns='http://bank.brest.com/soap'>" +
                        "<startDate>2015-11-25</startDate>" +
                        "<endDate>2015-12-26</endDate>" +
                "</getBankDepositsFromToDateDepositWithDepositorsRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositsFromToDateDepositWithDepositorsResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDepositsReport>" +
                            "<ns2:bankDepositReport>" +
                                "<ns2:depositId>1</ns2:depositId>" +
                                "<ns2:depositName>depositName1</ns2:depositName>" +
                                "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                                "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                                "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                                "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                                "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                                "<ns2:depositorCount>1</ns2:depositorCount>" +
                                "<ns2:depositorAmountSum>1000</ns2:depositorAmountSum>" +
                                "<ns2:depositorAmountPlusSum>100</ns2:depositorAmountPlusSum>" +
                                "<ns2:depositorAmountMinusSum>100</ns2:depositorAmountMinusSum>" +
                            "</ns2:bankDepositReport>" +
                        "</ns2:bankDepositsReport>" +
                "</ns2:getBankDepositsFromToDateDepositWithDepositorsResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDepositsReport deposits = soapClient.getBankDepositsFromToDateDepositWithDepositors(
                dateFormat.parse("2015-11-25"), dateFormat.parse("2015-12-26"));
        LOGGER.debug("Response - deposits - {}",xmlEntityToString(deposits.getBankDepositReport().get(0)));

        assertNotNull(deposits);
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositId"),deposits.getBankDepositReport().get(0).getDepositId());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositName"),deposits.getBankDepositReport().get(0).getDepositName());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorCount"),deposits.getBankDepositReport().get(0).getDepositorCount());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountSum"),deposits.getBankDepositReport().get(0).getDepositorAmountSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountPlusSum"),deposits.getBankDepositReport().get(0).getDepositorAmountPlusSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountMinusSum"),deposits.getBankDepositReport().get(0).getDepositorAmountMinusSum());
    }

    @Test
    public void testGetBankDepositsFromToDateReturnDepositWithDepositors() throws ParseException{
        Source requestPayload = new StringSource(
                "<getBankDepositsFromToDateReturnDepositWithDepositorsRequest xmlns='http://bank.brest.com/soap'>" +
                        "<startDate>2015-11-25</startDate>" +
                        "<endDate>2015-12-26</endDate>" +
                "</getBankDepositsFromToDateReturnDepositWithDepositorsRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositsFromToDateReturnDepositWithDepositorsResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDepositsReport>" +
                            "<ns2:bankDepositReport>" +
                                "<ns2:depositId>1</ns2:depositId>" +
                                "<ns2:depositName>depositName1</ns2:depositName>" +
                                "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                                "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                                "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                                "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                                "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                                "<ns2:depositorCount>1</ns2:depositorCount>" +
                                "<ns2:depositorAmountSum>1000</ns2:depositorAmountSum>" +
                                "<ns2:depositorAmountPlusSum>100</ns2:depositorAmountPlusSum>" +
                                "<ns2:depositorAmountMinusSum>100</ns2:depositorAmountMinusSum>" +
                                "</ns2:bankDepositReport>" +
                        "</ns2:bankDepositsReport>" +
                "</ns2:getBankDepositsFromToDateReturnDepositWithDepositorsResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDepositsReport deposits = soapClient.getBankDepositsFromToDateReturnDepositWithDepositors(
                dateFormat.parse("2015-11-25"), dateFormat.parse("2015-12-26"));
        LOGGER.debug("Response - deposits - {}",xmlEntityToString(deposits.getBankDepositReport().get(0)));

        assertNotNull(deposits);
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositId"),deposits.getBankDepositReport().get(0).getDepositId());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositName"),deposits.getBankDepositReport().get(0).getDepositName());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorCount"),deposits.getBankDepositReport().get(0).getDepositorCount());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountSum"),deposits.getBankDepositReport().get(0).getDepositorAmountSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountPlusSum"),deposits.getBankDepositReport().get(0).getDepositorAmountPlusSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountMinusSum"),deposits.getBankDepositReport().get(0).getDepositorAmountMinusSum());
    }

    @Test
    public void testGetBankDepositsByCurrencyWithDepositors() throws ParseException{
        Source requestPayload = new StringSource(
                "<getBankDepositsByCurrencyWithDepositorsRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositCurrency>usd</depositCurrency>" +
                "</getBankDepositsByCurrencyWithDepositorsRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositsByCurrencyWithDepositorsResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDepositsReport>" +
                            "<ns2:bankDepositReport>" +
                                "<ns2:depositId>1</ns2:depositId>" +
                                "<ns2:depositName>depositName1</ns2:depositName>" +
                                "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                                "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                                "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                                "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                                "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                                "<ns2:depositorCount>1</ns2:depositorCount>" +
                                "<ns2:depositorAmountSum>1000</ns2:depositorAmountSum>" +
                                "<ns2:depositorAmountPlusSum>100</ns2:depositorAmountPlusSum>" +
                                "<ns2:depositorAmountMinusSum>100</ns2:depositorAmountMinusSum>" +
                            "</ns2:bankDepositReport>" +
                        "</ns2:bankDepositsReport>" +
                "</ns2:getBankDepositsByCurrencyWithDepositorsResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDepositsReport deposits = soapClient.getBankDepositsByCurrencyWithDepositors("usd");
        LOGGER.debug("Response - deposits - {}",xmlEntityToString(deposits.getBankDepositReport().get(0)));

        assertNotNull(deposits);
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositId"),deposits.getBankDepositReport().get(0).getDepositId());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositName"),deposits.getBankDepositReport().get(0).getDepositName());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorCount"),deposits.getBankDepositReport().get(0).getDepositorCount());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountSum"),deposits.getBankDepositReport().get(0).getDepositorAmountSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountPlusSum"),deposits.getBankDepositReport().get(0).getDepositorAmountPlusSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountMinusSum"),deposits.getBankDepositReport().get(0).getDepositorAmountMinusSum());
    }

    @Test
    public void testGetBankDepositsByCurrencyFromToDateDepositWithDepositors() throws ParseException{
        Source requestPayload = new StringSource(
                "<getBankDepositsByCurrencyFromToDateDepositWithDepositorsRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositCurrency>usd</depositCurrency>" +
                        "<startDate>2015-01-01</startDate>" +
                        "<endDate>2015-02-02</endDate>" +
                "</getBankDepositsByCurrencyFromToDateDepositWithDepositorsRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDepositsReport>" +
                            "<ns2:bankDepositReport>" +
                                "<ns2:depositId>1</ns2:depositId>" +
                                "<ns2:depositName>depositName1</ns2:depositName>" +
                                "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                                "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                                "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                                "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                                "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                                "<ns2:depositorCount>1</ns2:depositorCount>" +
                                "<ns2:depositorAmountSum>1000</ns2:depositorAmountSum>" +
                                "<ns2:depositorAmountPlusSum>100</ns2:depositorAmountPlusSum>" +
                                "<ns2:depositorAmountMinusSum>100</ns2:depositorAmountMinusSum>" +
                            "</ns2:bankDepositReport>" +
                        "</ns2:bankDepositsReport>" +
                "</ns2:getBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDepositsReport deposits = soapClient.getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd",
                dateFormat.parse("2015-01-01"),dateFormat.parse("2015-02-02"));
        LOGGER.debug("Response - deposits - {}",xmlEntityToString(deposits.getBankDepositReport().get(0)));

        assertNotNull(deposits);
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositId"),deposits.getBankDepositReport().get(0).getDepositId());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositName"),deposits.getBankDepositReport().get(0).getDepositName());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorCount"),deposits.getBankDepositReport().get(0).getDepositorCount());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountSum"),deposits.getBankDepositReport().get(0).getDepositorAmountSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountPlusSum"),deposits.getBankDepositReport().get(0).getDepositorAmountPlusSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountMinusSum"),deposits.getBankDepositReport().get(0).getDepositorAmountMinusSum());
    }

    @Test
    public void testGetBankDepositsByCurrencyFromToDateReturnDepositWithDepositors() throws ParseException{
        Source requestPayload = new StringSource(
                "<getBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositCurrency>usd</depositCurrency>" +
                        "<startDate>2015-01-01</startDate>" +
                        "<endDate>2015-02-02</endDate>" +
                "</getBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsRequest>");

        Source responsePayload = new StringSource(
                "<ns2:getBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDepositsReport>" +
                            "<ns2:bankDepositReport>" +
                                "<ns2:depositId>1</ns2:depositId>" +
                                "<ns2:depositName>depositName1</ns2:depositName>" +
                                "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                                "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                                "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                                "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                                "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                                "<ns2:depositorCount>1</ns2:depositorCount>" +
                                "<ns2:depositorAmountSum>1000</ns2:depositorAmountSum>" +
                                "<ns2:depositorAmountPlusSum>100</ns2:depositorAmountPlusSum>" +
                                "<ns2:depositorAmountMinusSum>100</ns2:depositorAmountMinusSum>" +
                            "</ns2:bankDepositReport>" +
                        "</ns2:bankDepositsReport>" +
                "</ns2:getBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        BankDepositsReport deposits = soapClient.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd",
                dateFormat.parse("2015-01-01"),dateFormat.parse("2015-02-02"));
        LOGGER.debug("Response - deposits - {}",xmlEntityToString(deposits.getBankDepositReport().get(0)));

        assertNotNull(deposits);
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositId"),deposits.getBankDepositReport().get(0).getDepositId());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositName"),deposits.getBankDepositReport().get(0).getDepositName());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorCount"),deposits.getBankDepositReport().get(0).getDepositorCount());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountSum"),deposits.getBankDepositReport().get(0).getDepositorAmountSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountPlusSum"),deposits.getBankDepositReport().get(0).getDepositorAmountPlusSum());
        assertEquals(DataFixture.getExistDepositAllDepositors(1L,1L).get("depositorAmountMinusSum"),deposits.getBankDepositReport().get(0).getDepositorAmountMinusSum());
    }

    public String xmlEntityToString(BankDeposit deposit){
        return new String("BankDeposit: { depositId="+deposit.getDepositId()
               + ", depositName="+deposit.getDepositName()
               + ", depositMinTerm=" +deposit.getDepositMinTerm()
               + ", depositMinAmount=" +deposit.getDepositMinAmount()
               + ", depositCurrency=" +deposit.getDepositCurrency()
               + ", depositInterestRate=" +deposit.getDepositInterestRate()
               + ", depositAddConditions=" +deposit.getDepositAddConditions()
               + "}");
    }

    public String xmlEntityToString(BankDepositReport deposit){
        return new String("{depositId="+deposit.getDepositId()
                + ", depositName="+deposit.getDepositName()
                + ", depositMinTerm=" +deposit.getDepositMinTerm()
                + ", depositMinAmount=" +deposit.getDepositMinAmount()
                + ", depositCurrency=" +deposit.getDepositCurrency()
                + ", depositInterestRate=" +deposit.getDepositInterestRate()
                + ", depositAddConditions=" +deposit.getDepositAddConditions()
                + ", depositorCount=" +deposit.getDepositorCount()
                + ", depositorAmountSum=" +deposit.getDepositorAmountSum()
                + ", depositorAmountPlusSum=" +deposit.getDepositorAmountPlusSum()
                + ", depositorAmountMinusSum=" +deposit.getDepositorAmountMinusSum()
                + "}");
    }
}
package com.brest.bank.client;

import com.brest.bank.util.BankDeposit;
import com.brest.bank.util.BankDeposits;

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

import javax.xml.transform.Result;
import javax.xml.transform.Source;

import static org.springframework.ws.test.client.RequestMatchers.*;
import static org.springframework.ws.test.client.ResponseCreators.*;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-soap-client-test.xml"})
public class SoapClientTest {

    public static final Logger LOGGER = LogManager.getLogger();

    private static final String HOST = "http://host/";

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
}
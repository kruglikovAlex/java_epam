package com.brest.bank.soap;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.easymock.EasyMock.*;

import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.RequestCreator;
import org.springframework.ws.test.server.RequestCreators;
import org.springframework.ws.test.server.ResponseMatchers;

import org.springframework.xml.transform.StringSource;

import javax.xml.transform.Source;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-soap-Mock-test.xml"})
public class DepositSoapEndpointTest {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    BankDepositService depositService;

    @Autowired
    BankDepositorService depositorService;

    @Autowired
    DepositSoapEndpoint depositSoapEndpoint;

    @Autowired
    ApplicationContext applicationContext;

    private MockWebServiceClient mockClient;

    @Before
    public void createClient() {
        depositSoapEndpoint = new DepositSoapEndpoint(depositService, depositorService);
        mockClient = MockWebServiceClient.createClient(applicationContext);
    }

    @After
    public void tearDown(){
        reset(depositService);
        reset(depositorService);
    }

    @Test
    public void testGetBankDeposits() throws Exception {
        LOGGER.debug("testGetBankDeposits() - run");

        expect(depositService.getBankDeposits()).andReturn(DataFixture.getExistDeposits());
        replay(depositService);

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

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositors() throws Exception {
        LOGGER.debug("testGetBankDepositors() - run");

        expect(depositorService.getBankDepositors()).andReturn(DataFixture.getExistDepositors());
        replay(depositorService);

        Source requestPayload = new StringSource(
                "<getBankDepositorsRequest xmlns='http://bank.brest.com/soap'>" +
                        "</getBankDepositorsRequest>");

        Source responsePayload = new StringSource(
                "<getBankDepositorsResponse xmlns=\"http://bank.brest.com/soap\">" +
                        "<bankDepositors>" +
                            "<bankDepositor>" +
                                "<depositorId>1</depositorId>" +
                                "<depositorName>depositorName1</depositorName>" +
                                "<depositorDateDeposit>2015-01-01Z</depositorDateDeposit>" +
                                "<depositorAmountDeposit>1000</depositorAmountDeposit>" +
                                "<depositorAmountPlusDeposit>100</depositorAmountPlusDeposit>" +
                                "<depositorAmountMinusDeposit>100</depositorAmountMinusDeposit>" +
                                "<depositorDateReturnDeposit>2015-09-09Z</depositorDateReturnDeposit>" +
                                "<depositorMarkReturnDeposit>0</depositorMarkReturnDeposit>" +
                            "</bankDepositor>" +
                            "<bankDepositor>" +
                                "<depositorId>2</depositorId>" +
                                "<depositorName>depositorName1</depositorName>" +
                                "<depositorDateDeposit>2015-01-01Z</depositorDateDeposit>" +
                                "<depositorAmountDeposit>1000</depositorAmountDeposit>" +
                                "<depositorAmountPlusDeposit>100</depositorAmountPlusDeposit>" +
                                "<depositorAmountMinusDeposit>100</depositorAmountMinusDeposit>" +
                                "<depositorDateReturnDeposit>2015-09-09Z</depositorDateReturnDeposit>" +
                                "<depositorMarkReturnDeposit>0</depositorMarkReturnDeposit>" +
                            "</bankDepositor>" +
                        "</bankDepositors>" +
                "</getBankDepositorsResponse>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositorService);
    }

    @Test
    public void testGetBankDepositById() throws Exception {
        LOGGER.debug("testGetBankDepositById() - run");

        expect(depositService.getBankDepositById(1L)).andReturn(DataFixture.getExistDeposit(1L));
        replay(depositService);

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

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositByName() throws Exception {
        LOGGER.debug("testGetBankDepositByName() - run");

        expect(depositService.getBankDepositByName("depositName1")).andReturn(DataFixture.getExistDeposit(1L));
        replay(depositService);

        Source requestPayload = new StringSource(
                "<getBankDepositByNameRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositName>depositName1</depositName>" +
                        "</getBankDepositByNameRequest>");

        Source responsePayload = new StringSource(
                "<getBankDepositByNameResponse xmlns=\"http://bank.brest.com/soap\">" +
                        "<bankDeposit>" +
                            "<depositId>1</depositId>" +
                            "<depositName>depositName1</depositName>" +
                            "<depositMinTerm>12</depositMinTerm>" +
                            "<depositMinAmount>1000</depositMinAmount>" +
                            "<depositCurrency>usd</depositCurrency>" +
                            "<depositInterestRate>4</depositInterestRate>" +
                            "<depositAddConditions>conditions1</depositAddConditions>" +
                        "</bankDeposit>" +
                "</getBankDepositByNameResponse>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositService);
    }

    @Test
    public void testInvalidRequestGetBankDepositById() {
        LOGGER.debug("testInvalidGetBankDepositById() - run");

        Source requestPayload = new StringSource(
                "<getBankDepositByIdRequest xmlns='http://bank.brest.com/soap' " +
                        "depositId='1'/>");

        Source responsePayload = new StringSource(
                "<SOAP-ENV:Fault xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<faultcode>SOAP-ENV:Server</faultcode>" +
                        "<faultstring xml:lang=\"en\">java.lang.NullPointerException</faultstring>" +
                "</SOAP-ENV:Fault>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));
    }

    @Test
    public void testInvalidIdGetBankDepositById() {
        LOGGER.debug("testInvalidGetBankDepositById() - run");

        depositService.getBankDepositById(-1L);
        expectLastCall().andThrow(new IllegalArgumentException("The parameter can not be NULL"));
        replay(depositService);


        Source requestPayload = new StringSource(
                "<getBankDepositByIdRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositId>-1</depositId>" +
                "</getBankDepositByIdRequest>");

        Source responsePayload = new StringSource(
                "<SOAP-ENV:Fault xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<faultcode>SOAP-ENV:Server</faultcode>" +
                        "<faultstring xml:lang=\"en\">The parameter can not be NULL</faultstring>" +
                "</SOAP-ENV:Fault>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsByCurrency() throws Exception {
        LOGGER.debug("testGetBankDepositsByCurrency() - run");

        expect(depositService.getBankDepositsByCurrency("usd")).andReturn(DataFixture.getExistDeposits());
        replay(depositService);

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

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsByInterestRate() throws Exception {
        LOGGER.debug("testGetBankDepositsByInterestRate() - run");

        expect(depositService.getBankDepositsByInterestRate(4)).andReturn(DataFixture.getExistDeposits());
        replay(depositService);

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

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsFromToMinTerm() throws Exception {
        LOGGER.debug("testGetBankDepositsFomToMinTerm() - run");

        expect(depositService.getBankDepositsFromToMinTerm(10,12)).andReturn(DataFixture.getExistDeposits());
        replay(depositService);

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

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsInvalidFromToMinTerm() throws Exception {
        LOGGER.debug("testGetBankDepositsInvalidFomToMinTerm() - run");

        Source requestPayload = new StringSource(
                "<getBankDepositsFromToMinTermRequest xmlns='http://bank.brest.com/soap'>" +
                        "<fromTerm>12</fromTerm>" +
                        "<toTerm>11</toTerm>" +
                "</getBankDepositsFromToMinTermRequest>");

        Source responsePayload = new StringSource(
                "<SOAP-ENV:Fault xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<faultcode>SOAP-ENV:Server</faultcode>" +
                        "<faultstring xml:lang=\"en\">The first parameter should be less than the second</faultstring>" +
                "</SOAP-ENV:Fault>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));
    }

    @Test
    public void testGetBankDepositsNullFromToMinTerm() throws Exception {
        LOGGER.debug("testGetBankDepositsNullFomToMinTerm() - run");

        Source requestPayload = new StringSource(
                "<getBankDepositsFromToMinTermRequest xmlns='http://bank.brest.com/soap'>" +
                        "<fromTerm>null</fromTerm>" +
                        "<toTerm>12</toTerm>" +
                "</getBankDepositsFromToMinTermRequest>");

        Source responsePayload = new StringSource(
                "<SOAP-ENV:Fault xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<faultcode>SOAP-ENV:Server</faultcode>" +
                        "<faultstring xml:lang=\"en\">The parameter can not be NULL</faultstring>" +
                "</SOAP-ENV:Fault>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));
    }

    @Test
    public void testGetBankDepositsFromNullToMinTerm() throws Exception {
        LOGGER.debug("testGetBankDepositsFromNullToMinTerm() - run");

        Source requestPayload = new StringSource(
                "<getBankDepositsFromToMinTermRequest xmlns='http://bank.brest.com/soap'>" +
                        "<fromTerm>10</fromTerm>" +
                        "<toTerm>null</toTerm>" +
                "</getBankDepositsFromToMinTermRequest>");

        Source responsePayload = new StringSource(
                "<SOAP-ENV:Fault xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<faultcode>SOAP-ENV:Server</faultcode>" +
                        "<faultstring xml:lang=\"en\">The parameter can not be NULL</faultstring>" +
                "</SOAP-ENV:Fault>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));
    }

    @Test
    public void testGetBankDepositsFromToInterestRate() throws Exception {
        LOGGER.debug("testGetBankDepositsFomToInterestRate() - run");

        expect(depositService.getBankDepositsFromToInterestRate(2,4)).andReturn(DataFixture.getExistDeposits());
        replay(depositService);

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

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsInvalidFromToInterestRate() throws Exception {
        LOGGER.debug("testGetBankDepositsInvalidFomToInterestRate() - run");

        Source requestPayload = new StringSource(
                "<getBankDepositsFromToInterestRateRequest xmlns='http://bank.brest.com/soap'>" +
                        "<startRate>4</startRate>" +
                        "<endRate>2</endRate>" +
                "</getBankDepositsFromToInterestRateRequest>");

        Source responsePayload = new StringSource(
                "<SOAP-ENV:Fault xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<faultcode>SOAP-ENV:Server</faultcode>" +
                        "<faultstring xml:lang=\"en\">The first parameter should be less than the second</faultstring>" +
                "</SOAP-ENV:Fault>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));
    }

    @Test
    public void testGetBankDepositsNullFromToInterestRate() throws Exception {
        LOGGER.debug("testGetBankDepositsNullFomToInterestRate() - run");

        Source requestPayload = new StringSource(
                "<getBankDepositsFromToInterestRateRequest xmlns='http://bank.brest.com/soap'>" +
                        "<startRate>null</startRate>" +
                        "<endRate>4</endRate>" +
                "</getBankDepositsFromToInterestRateRequest>");

        Source responsePayload = new StringSource(
                "<SOAP-ENV:Fault xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<faultcode>SOAP-ENV:Server</faultcode>" +
                        "<faultstring xml:lang=\"en\">The parameter can not be NULL</faultstring>" +
                "</SOAP-ENV:Fault>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));
    }

    @Test
    public void testGetBankDepositsFromNullToInterestRate() throws Exception {
        LOGGER.debug("testGetBankDepositsFromNullToInterestRate() - run");

        Source requestPayload = new StringSource(
                "<getBankDepositsFromToInterestRateRequest xmlns='http://bank.brest.com/soap'>" +
                        "<startRate>2</startRate>" +
                        "<endRate>null</endRate>" +
                "</getBankDepositsFromToInterestRateRequest>");

        Source responsePayload = new StringSource(
                "<SOAP-ENV:Fault xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<faultcode>SOAP-ENV:Server</faultcode>" +
                        "<faultstring xml:lang=\"en\">The parameter can not be NULL</faultstring>" +
                "</SOAP-ENV:Fault>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));
    }

    @Test
    public void testAddBankDeposit() throws Exception {
        LOGGER.debug("testAddBankDeposit() - run");

        depositService.addBankDeposit(anyObject(BankDeposit.class));
        expectLastCall();

        expect(depositService.getBankDepositByName("depositName1"))
                .andReturn(DataFixture.getExistDeposit(1L));

        replay(depositService);

        Source requestPayload = new StringSource(
                "<addBankDepositRequest xmlns='http://bank.brest.com/soap'>" +
                        "<bankDeposit>" +
                            "<depositId></depositId>" +
                            "<depositName>depositName1</depositName>" +
                            "<depositMinTerm>12</depositMinTerm>" +
                            "<depositMinAmount>1000</depositMinAmount>" +
                            "<depositCurrency>usd</depositCurrency>" +
                            "<depositInterestRate>4</depositInterestRate>" +
                            "<depositAddConditions>conditions1</depositAddConditions>" +
                        "</bankDeposit>" +
                "</addBankDepositRequest>");

        Source responsePayload = new StringSource(
                "<addBankDepositResponse xmlns=\"http://bank.brest.com/soap\">" +
                        "<bankDeposit>" +
                            "<depositId>1</depositId>" +
                            "<depositName>depositName1</depositName>" +
                            "<depositMinTerm>12</depositMinTerm>" +
                            "<depositMinAmount>1000</depositMinAmount>" +
                            "<depositCurrency>usd</depositCurrency>" +
                            "<depositInterestRate>4</depositInterestRate>" +
                            "<depositAddConditions>conditions1</depositAddConditions>" +
                        "</bankDeposit>" +
                "</addBankDepositResponse>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositService);
    }

    @Test
    public void testRemoveBankDeposit(){
        LOGGER.debug("testRemoveBankDeposit() - run");

        expect(depositService.getBankDepositById(1L)).andReturn(DataFixture.getExistDeposit(1L));

        depositService.deleteBankDeposit(1L);
        expectLastCall();

        expect(depositService.getBankDepositById(1L)).andReturn(null);

        replay(depositService);

        Source requestPayload = new StringSource(
                "<deleteBankDepositRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositId>1</depositId>" +
                "</deleteBankDepositRequest>");

        Source responsePayload = new StringSource(
                "<ns2:deleteBankDepositResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:result>Bank Deposit removed</ns2:result>" +
                "</ns2:deleteBankDepositResponse>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositService);
    }

    @Test
    public void testRemoveEmptyBankDeposit(){
        LOGGER.debug("testRemoveEmptyBankDeposit() - run");

        expect(depositService.getBankDepositById(1L)).andReturn(null);

        replay(depositService);

        Source requestPayload = new StringSource(
                "<deleteBankDepositRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositId>1</depositId>" +
                "</deleteBankDepositRequest>");

        Source responsePayload = new StringSource(
                "<ns2:deleteBankDepositResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:result>In the database there is no Deposit with such parameters</ns2:result>" +
                "</ns2:deleteBankDepositResponse>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositService);
    }

    @Test
    public void testInvalidRemovedBankDeposit(){
        LOGGER.debug("testInvalidRemovedBankDeposit() - run");

        expect(depositService.getBankDepositById(1L)).andReturn(DataFixture.getExistDeposit(1L));

        depositService.deleteBankDeposit(1L);
        expectLastCall();

        expect(depositService.getBankDepositById(1L)).andReturn(DataFixture.getExistDeposit(1L));

        replay(depositService);

        Source requestPayload = new StringSource(
                "<deleteBankDepositRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositId>1</depositId>" +
                "</deleteBankDepositRequest>");

        Source responsePayload = new StringSource(
                "<ns2:deleteBankDepositResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:result>Bank Deposit don't removed</ns2:result>" +
                "</ns2:deleteBankDepositResponse>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositService);
    }

    @Test
    public void testRemovedNullIdBankDeposit(){
        LOGGER.debug("testRemovedNullIdBankDeposit() - run");

        Long depositId = null;

        Source requestPayload = new StringSource(
                "<deleteBankDepositRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositId>" +
                        depositId +
                        "</depositId>" +
                "</deleteBankDepositRequest>");

        Source responsePayload = new StringSource(
                "<SOAP-ENV:Fault xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<faultcode>SOAP-ENV:Server</faultcode>" +
                        "<faultstring xml:lang=\"en\">The parameter can not be NULL</faultstring>" +
                "</SOAP-ENV:Fault>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

    }

    @Test
    public void testUpdateBankDeposit() throws Exception {
        LOGGER.debug("testUpdateBankDeposit() - run");

        BankDeposit updateDeposit = DataFixture.getExistDeposit(1L);
        updateDeposit.setDepositName("updateName1");

        depositService.updateBankDeposit(anyObject(BankDeposit.class));
        expectLastCall();

        expect(depositService.getBankDepositById(1L)).andReturn(updateDeposit);

        replay(depositService);

        Source requestPayload = new StringSource(
                "<updateBankDepositRequest xmlns='http://bank.brest.com/soap'>" +
                        "<bankDeposit>" +
                            "<depositId>1</depositId>" +
                            "<depositName>updateName1</depositName>" +
                            "<depositMinTerm>12</depositMinTerm>" +
                            "<depositMinAmount>1000</depositMinAmount>" +
                            "<depositCurrency>usd</depositCurrency>" +
                            "<depositInterestRate>4</depositInterestRate>" +
                            "<depositAddConditions>conditions1</depositAddConditions>" +
                        "</bankDeposit>" +
                "</updateBankDepositRequest>");

        Source responsePayload = new StringSource(
                "<ns2:updateBankDepositResponse xmlns:ns2=\"http://bank.brest.com/soap\">" +
                        "<ns2:bankDeposit>" +
                            "<ns2:depositId>1</ns2:depositId>" +
                            "<ns2:depositName>updateName1</ns2:depositName>" +
                            "<ns2:depositMinTerm>12</ns2:depositMinTerm>" +
                            "<ns2:depositMinAmount>1000</ns2:depositMinAmount>" +
                            "<ns2:depositCurrency>usd</ns2:depositCurrency>" +
                            "<ns2:depositInterestRate>4</ns2:depositInterestRate>" +
                            "<ns2:depositAddConditions>conditions1</ns2:depositAddConditions>" +
                        "</ns2:bankDeposit>" +
                "</ns2:updateBankDepositResponse>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositService);
    }

    @Test
    public void testUpdateBankDepositNullId() throws Exception {
        LOGGER.debug("testUpdateBankDepositNullId() - run");

        Source requestPayload = new StringSource(
                "<updateBankDepositRequest xmlns='http://bank.brest.com/soap'>" +
                        "<bankDeposit>" +
                            "<depositId></depositId>" +
                            "<depositName>updateName1</depositName>" +
                            "<depositMinTerm>12</depositMinTerm>" +
                            "<depositMinAmount>1000</depositMinAmount>" +
                            "<depositCurrency>usd</depositCurrency>" +
                            "<depositInterestRate>4</depositInterestRate>" +
                            "<depositAddConditions>conditions1</depositAddConditions>" +
                        "</bankDeposit>" +
                "</updateBankDepositRequest>");

        Source responsePayload = new StringSource(
                "<SOAP-ENV:Fault xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<faultcode>SOAP-ENV:Server</faultcode>" +
                        "<faultstring xml:lang=\"en\">The parameter can not be NULL- depositId</faultstring>" +
                "</SOAP-ENV:Fault>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));
    }


    @Test
    public void testGetBankDepositorById() throws java.text.ParseException{
        LOGGER.debug("testGetBankDepositorById() - run");

        expect(depositorService.getBankDepositorById(1L)).andReturn(DataFixture.getExistDepositor(1L));
        replay(depositorService);

        Source requestPayload = new StringSource(
                "<getBankDepositorByIdRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositorId>1</depositorId>" +
                "</getBankDepositorByIdRequest>");

        Source responsePayload = new StringSource(
                "<getBankDepositorByIdResponse xmlns=\"http://bank.brest.com/soap\">" +
                        "<bankDepositor>" +
                            "<depositorId>1</depositorId>" +
                            "<depositorName>depositorName1</depositorName>" +
                            "<depositorDateDeposit>2015-01-01Z</depositorDateDeposit>" +
                            "<depositorAmountDeposit>1000</depositorAmountDeposit>" +
                            "<depositorAmountPlusDeposit>100</depositorAmountPlusDeposit>" +
                            "<depositorAmountMinusDeposit>100</depositorAmountMinusDeposit>" +
                            "<depositorDateReturnDeposit>2015-09-09Z</depositorDateReturnDeposit>" +
                            "<depositorMarkReturnDeposit>0</depositorMarkReturnDeposit>" +
                        "</bankDepositor>" +
                "</getBankDepositorByIdResponse>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositorService);
    }

    @Test
    public void testGetBankDepositorByName() throws java.text.ParseException{
        LOGGER.debug("testGetBankDepositorByName() - run");

        expect(depositorService.getBankDepositorByName("depositorName1")).andReturn(DataFixture.getExistDepositor(1L));
        replay(depositorService);

        Source requestPayload = new StringSource(
                "<getBankDepositorByNameRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositorName>depositorName1</depositorName>" +
                "</getBankDepositorByNameRequest>");

        Source responsePayload = new StringSource(
                "<getBankDepositorByNameResponse xmlns=\"http://bank.brest.com/soap\">" +
                        "<bankDepositor>" +
                            "<depositorId>1</depositorId>" +
                            "<depositorName>depositorName1</depositorName>" +
                            "<depositorDateDeposit>2015-01-01Z</depositorDateDeposit>" +
                            "<depositorAmountDeposit>1000</depositorAmountDeposit>" +
                            "<depositorAmountPlusDeposit>100</depositorAmountPlusDeposit>" +
                            "<depositorAmountMinusDeposit>100</depositorAmountMinusDeposit>" +
                            "<depositorDateReturnDeposit>2015-09-09Z</depositorDateReturnDeposit>" +
                            "<depositorMarkReturnDeposit>0</depositorMarkReturnDeposit>" +
                        "</bankDepositor>" +
                "</getBankDepositorByNameResponse>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositorService);
    }
}

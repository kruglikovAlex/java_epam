package com.brest.bank.soap;

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
                "<getBankDepositByIdRequest xmlns='http://bank.brest.com/soap'>" +
                    "<depositId>1</depositId>" +
                "</getBankDepositByIdRequest>");

        Source responsePayload = new StringSource(
                "<getBankDepositByIdResponse xmlns=\"http://bank.brest.com/soap\">" +
                        "<bankDeposit>" +
                            "<depositId>1</depositId>" +
                            "<depositName>depositName1</depositName>" +
                            "<depositMinTerm>12</depositMinTerm>" +
                            "<depositMinAmount>1000</depositMinAmount>" +
                            "<depositCurrency>usd</depositCurrency>" +
                            "<depositInterestRate>4</depositInterestRate>" +
                            "<depositAddConditions>conditions1</depositAddConditions>" +
                        "</bankDeposit>" +
                "</getBankDepositByIdResponse>");

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
        expectLastCall().andThrow(new IllegalArgumentException());
        replay(depositService);


        Source requestPayload = new StringSource(
                "<getBankDepositByIdRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositId>-1</depositId>" +
                "</getBankDepositByIdRequest>");

        Source responsePayload = new StringSource(
                "<SOAP-ENV:Fault xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<faultcode>SOAP-ENV:Server</faultcode>" +
                        "<faultstring xml:lang=\"en\">java.lang.IllegalArgumentException</faultstring>" +
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

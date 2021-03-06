package com.brest.bank.client;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.web.SoapController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

import org.springframework.test.web.servlet.MockMvc;

import javax.wsdl.Definition;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class SoapControllerTest {

    private static final Logger LOGGER = LogManager.getLogger(SoapControllerTest.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

    @Mock
    SoapClient soapClient;

    private Map wsdlServices = new HashMap();
    private String[] soapResponse = {"",""};
    private String soapRequest = "";
    private String wsdlLocation = "http://localhost:8080/SpringHibernateBDeposit-1.0/soap/soapService.wsdl";
    private Definition wsdl;

    @InjectMocks
    private SoapController soapController = new SoapController();

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = standaloneSetup(soapController).build();
    }

    @Test
    public void testPostSoapQueryGetBankDeposits() throws Exception {
        LOGGER.debug("postSoapQuery(): getBankDeposits - start");

        soapRequest = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://bank.brest.com/soap\">\n" +
                "\t<x:Header/>\n" +
                "\t<x:Body>\n" +
                "\t\t<soa:getBankDepositsRequest>\n" +
                "\t\t</soa:getBankDepositsRequest>\n" +
                "\t</x:Body>\n" +
                "</x:Envelope>";

        when(soapClient.getBankDeposits()).thenReturn(DataFixture.getDepositsWsdl());
        mockMvc.perform(post("/client/submitSoapQuery?soapQuery={soapQuery}&submit={submit}","getBankDeposits","Generate+Request"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("services","requests","responses"))
                .andExpect(model().attribute("requests",soapRequest))
                .andExpect(model().attribute("responses", notNullValue()))
                .andExpect(status().isOk())
                .andDo(log())
                .andDo(print());

        verify(soapClient,times(1)).getBankDeposits();
        verifyNoMoreInteractions(soapClient);
    }

    @Test
    public void testPostSoapQueryGetBankDepositors() throws Exception {
        LOGGER.debug("postSoapQuery(): getBankDepositors - start");

        soapRequest = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://bank.brest.com/soap\">\n" +
                "\t<x:Header/>\n" +
                "\t<x:Body>\n" +
                "\t\t<soa:getBankDepositorsRequest>\n" +
                "\t\t</soa:getBankDepositorsRequest>\n" +
                "\t</x:Body>\n" +
                "</x:Envelope>";

        when(soapClient.getBankDepositors()).thenReturn(DataFixture.getDepositorsWsdl());
        mockMvc.perform(post("/client/submitSoapQuery?soapQuery={soapQuery}&submit={submit}","getBankDepositors","Generate+Request"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("services","requests","responses"))
                .andExpect(model().attribute("requests",soapRequest))
                .andExpect(model().attribute("responses", notNullValue()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(soapClient,times(1)).getBankDepositors();
        verifyNoMoreInteractions(soapClient);
    }

    @Test
    public void testPostSoapQueryGetBankDepositById() throws Exception {
        LOGGER.debug("postSoapQuery(): getBankDepositById - start");

        soapRequest = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://bank.brest.com/soap\">\n" +
                "\t<x:Header/>\n" +
                "\t<x:Body>\n" +
                "\t\t<soa:getBankDepositByIdRequest>\n" +
                "\t\t\t<soa:depositId>1</soa:depositId>\n" +
                "\t\t</soa:getBankDepositByIdRequest>\n" +
                "\t</x:Body>\n" +
                "</x:Envelope>";

        soapResponse[0] = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<getBankDepositByIdResponse xmlns=\"http://bank.brest.com/soap\">\n" +
                "    <bankDeposit>\n" +
                "        <depositId>1</depositId>\n" +
                "        <depositName>depositName0</depositName>\n" +
                "        <depositMinTerm>12</depositMinTerm>\n" +
                "        <depositMinAmount>100</depositMinAmount>\n" +
                "        <depositCurrency>usd</depositCurrency>\n" +
                "        <depositInterestRate>4</depositInterestRate>\n" +
                "        <depositAddConditions>condition0</depositAddConditions>\n" +
                "    </bankDeposit>\n" +
                "</getBankDepositByIdResponse>\n";
        soapResponse[1] = "{\"getBankDepositByIdResponse\": {\n" +
                "    \"bankDeposit\": {\n" +
                "        \"depositMinTerm\": 12,\n" +
                "        \"depositInterestRate\": 4,\n" +
                "        \"depositName\": \"depositName0\",\n" +
                "        \"depositCurrency\": \"usd\",\n" +
                "        \"depositId\": 1,\n" +
                "        \"depositMinAmount\": 100,\n" +
                "        \"depositAddConditions\": \"condition0\"\n" +
                "    },\n" +
                "    \"xmlns\": \"http://bank.brest.com/soap\"\n" +
                "}}";

        when(soapClient.getBankDepositById(1L)).thenReturn(DataFixture.getExistDepositWsdl(1L));
        mockMvc.perform(post("/client/submitSoapQuery?soapQuery={soapQuery}&depositId={id}&submit={submit}","getBankDepositById",1,"Generate+Request"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("services","requests","responses"))
                .andExpect(model().attribute("requests",soapRequest))
                .andExpect(model().attribute("responses", notNullValue()))
                .andExpect(model().attribute("responses",is(soapResponse)))
                .andExpect(status().isOk())
                .andDo(log())
                .andDo(print());

        verify(soapClient,times(1)).getBankDepositById(1L);
        verifyNoMoreInteractions(soapClient);
    }

    @Test
    public void testPostSoapQueryGetBankDepositByNameWithDepositors() throws Exception {
        LOGGER.debug("postSoapQuery(): getBankDepositByNameWithDepositors - start");

        soapResponse[0] = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<getBankDepositByNameWithDepositorsResponse xmlns=\"http://bank.brest.com/soap\">\n" +
                "    <bankDepositReport>\n" +
                "        <depositId>1</depositId>\n" +
                "        <depositName>depositName1</depositName>\n" +
                "        <depositMinTerm>12</depositMinTerm>\n" +
                "        <depositMinAmount>100</depositMinAmount>\n" +
                "        <depositCurrency>usd</depositCurrency>\n" +
                "        <depositInterestRate>4</depositInterestRate>\n" +
                "        <depositAddConditions>condition0</depositAddConditions>\n" +
                "        <depositorCount xsi:nil=\"true\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>\n" +
                "        <depositorAmountSum>200</depositorAmountSum>\n" +
                "        <depositorAmountPlusSum>200</depositorAmountPlusSum>\n" +
                "        <depositorAmountMinusSum>200</depositorAmountMinusSum>\n" +
                "    </bankDepositReport>\n" +
                "</getBankDepositByNameWithDepositorsResponse>\n";
        soapResponse[1] = "{\"getBankDepositByNameWithDepositorsResponse\": {\n" +
                "    \"bankDepositReport\": {\n" +
                "        \"depositMinTerm\": 12,\n" +
                "        \"depositInterestRate\": 4,\n" +
                "        \"depositorAmountPlusSum\": 200,\n" +
                "        \"depositName\": \"depositName1\",\n" +
                "        \"depositorAmountSum\": 200,\n" +
                "        \"depositorAmountMinusSum\": 200,\n" +
                "        \"depositorCount\": {\n" +
                "            \"xsi:nil\": true,\n" +
                "            \"xmlns:xsi\": \"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "        },\n" +
                "        \"depositCurrency\": \"usd\",\n" +
                "        \"depositId\": 1,\n" +
                "        \"depositMinAmount\": 100,\n" +
                "        \"depositAddConditions\": \"condition0\"\n" +
                "    },\n" +
                "    \"xmlns\": \"http://bank.brest.com/soap\"\n" +
                "}}";
        soapRequest = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://bank.brest.com/soap\">\n" +
                "\t<x:Header/>\n" +
                "\t<x:Body>\n" +
                "\t\t<soa:getBankDepositByNameWithDepositorsRequest>\n" +
                "\t\t\t<soa:depositName>depositName1</soa:depositName>\n" +
                "\t\t</soa:getBankDepositByNameWithDepositorsRequest>\n" +
                "\t</x:Body>\n" +
                "</x:Envelope>";

        when(soapClient.getBankDepositByNameWithDepositors("depositName1"))
                .thenReturn(DataFixture.getExistDepositByNameWithDepositorsWsdl("depositName1"));
        mockMvc.perform(post("/client/submitSoapQuery?soapQuery={soapQuery}&depositName={name}&submit={submit}","getBankDepositByNameWithDepositors","depositName1","Generate+Request"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("services","requests","responses"))
                .andExpect(model().attribute("requests",soapRequest))
                .andExpect(model().attribute("responses", notNullValue()))
                .andExpect(model().attribute("responses",is(soapResponse)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(log());

        verify(soapClient, times(1)).getBankDepositByNameWithDepositors("depositName1");
        verifyNoMoreInteractions(soapClient);
    }

    @Test
    public void testPostSoapQueryAddBankDeposit() throws Exception {
        LOGGER.debug("postSoapQuery(): addBankDeposit - start");

        soapRequest = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://bank.brest.com/soap\">\n" +
                "\t<x:Header/>\n" +
                "\t<x:Body>\n" +
                "\t\t<soa:addBankDepositRequest>\n" +
                "\t\t\t<soa:bankDeposit>\n" +
                "\t\t\t\t<soa:bankDepositId>null</soa:bankDepositId>\n" +
                "\t\t\t\t<soa:bankDepositName>testAdd</soa:bankDepositName>\n" +
                "\t\t\t\t<soa:bankDepositMinTerm>6</soa:bankDepositMinTerm>\n" +
                "\t\t\t\t<soa:bankDepositMinAmount>1000</soa:bankDepositMinAmount>\n" +
                "\t\t\t\t<soa:bankDepositCurrency>eur</soa:bankDepositCurrency>\n" +
                "\t\t\t\t<soa:bankDepositInterestRate>4</soa:bankDepositInterestRate>\n" +
                "\t\t\t\t<soa:bankDepositAddConditions>conditions2</soa:bankDepositAddConditions>\n" +
                "\t\t\t</soa:bankDeposit>\n" +
                "\t\t</soa:addBankDepositRequest>\n" +
                "\t</x:Body>\n" +
                "</x:Envelope>";

        BankDeposit deposit = DataFixture.getAddDeposit();
        when(soapClient.addBankDeposit(deposit));
        mockMvc.perform(post("/client/submitSoapQuery?soapQuery={soapQuery}" +
                "&depositId={id}" +
                "&depositName={name}" +
                "&depositMinTerm={term}"+
                "&depositMinAmount={amount}"+
                "&depositCurrency={currency}"+
                "&depositInterestRate={rate}"+
                "&depositAddConditions={condition}"+
                "&submit={submit}","addBankDeposit",null,"testAdd",6,1000,"eur",4,"conditions2","Generate+Request"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("services","requests","responses"))
                .andExpect(model().attribute("requests",soapRequest))
                .andExpect(model().attribute("responses",notNullValue()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(log());

        verify(soapClient, times(1)).addBankDeposit(deposit);
        verifyNoMoreInteractions(soapClient);
    }

    @Test
    public void testPostSoapQueryUpdateBankDeposit() throws Exception {
        LOGGER.debug("postSoapQuery(): updateBankDeposit - start");

        soapRequest = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://bank.brest.com/soap\">\n" +
                "\t<x:Header/>\n" +
                "\t<x:Body>\n" +
                "\t\t<soa:updateBankDepositRequest>\n" +
                "\t\t\t<soa:bankDeposit>\n" +
                "\t\t\t\t<soa:bankDepositId>1</soa:bankDepositId>\n" +
                "\t\t\t\t<soa:bankDepositName>testAdd</soa:bankDepositName>\n" +
                "\t\t\t\t<soa:bankDepositMinTerm>6</soa:bankDepositMinTerm>\n" +
                "\t\t\t\t<soa:bankDepositMinAmount>1000</soa:bankDepositMinAmount>\n" +
                "\t\t\t\t<soa:bankDepositCurrency>eur</soa:bankDepositCurrency>\n" +
                "\t\t\t\t<soa:bankDepositInterestRate>4</soa:bankDepositInterestRate>\n" +
                "\t\t\t\t<soa:bankDepositAddConditions>conditions2</soa:bankDepositAddConditions>\n" +
                "\t\t\t</soa:bankDeposit>\n" +
                "\t\t</soa:updateBankDepositRequest>\n" +
                "\t</x:Body>\n" +
                "</x:Envelope>";

        BankDeposit deposit = DataFixture.getAddDeposit();
        deposit.setDepositId(1L);
        when(soapClient.updateBankDeposit(deposit));
        mockMvc.perform(post("/client/submitSoapQuery?soapQuery={soapQuery}" +
                "&depositId={id}" +
                "&depositName={name}" +
                "&depositMinTerm={term}"+
                "&depositMinAmount={amount}"+
                "&depositCurrency={currency}"+
                "&depositInterestRate={rate}"+
                "&depositAddConditions={condition}"+
                "&submit={submit}","updateBankDeposit",1,"testAdd",6,1000,"eur",4,"conditions2","Generate+Request"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("services","requests","responses"))
                .andExpect(model().attribute("requests",soapRequest))
                .andExpect(model().attribute("responses",notNullValue()))
                .andExpect(status().isOk())
                .andDo(log())
                .andDo(print());

        verify(soapClient, times(1)).updateBankDeposit(deposit);
        verify(soapClient, only()).updateBankDeposit(deposit);
        verifyNoMoreInteractions(soapClient);
    }

    @Test
    public void testPostSoapQueryUpdateBankDepositor() throws Exception {
        LOGGER.debug("postSoapQuery(): updateBankDepositor- start");

        soapRequest = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://bank.brest.com/soap\">\n" +
                "\t<x:Header/>\n" +
                "\t<x:Body>\n" +
                "\t\t<soa:updateBankDepositorRequest>\n" +
                "\t\t\t<soa:bankDepositor>\n" +
                "\t\t\t\t<soa:bankDepositorId>1</soa:bankDepositorId>\n" +
                "\t\t\t\t<soa:bankDepositorName>depositNameUpdate</soa:bankDepositorName>\n" +
                "\t\t\t\t<soa:bankDepositorDateDeposit>2017-01-06</soa:bankDepositorDateDeposit>\n" +
                "\t\t\t\t<soa:bankDepositorAmountDeposit>1000</soa:bankDepositorAmountDeposit>\n" +
                "\t\t\t\t<soa:bankDepositorAmountPlusDeposit>1000</soa:bankDepositorAmountPlusDeposit>\n" +
                "\t\t\t\t<soa:bankDepositorAmountMinusDeposit>1000</soa:bankDepositorAmountMinusDeposit>\n" +
                "\t\t\t\t<soa:bankDepositorDateReturnDeposit>2017-01-06</soa:bankDepositorDateReturnDeposit>\n" +
                "\t\t\t\t<soa:bankDepositorMarkReturnDeposit>0</soa:bankDepositorMarkReturnDeposit>\n" +
                "\t\t\t</soa:bankDepositor>\n" +
                "\t\t</soa:updateBankDepositorRequest>\n" +
                "\t</x:Body>\n" +
                "</x:Envelope>";

        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        when(soapClient.updateBankDepositor(depositor));
        mockMvc.perform(post("/client/submitSoapQuery?soapQuery={soapQuery}" +
                "&depositorId={id}" +
                "&depositorName={name}" +
                "&depositorDateDeposit={startDate}"+
                "&depositorAmountDeposit={amount}"+
                "&depositorAmountPlusDeposit={amountPlus}"+
                "&depositorAmountMinusDeposit={amountMinus}"+
                "&depositorDateReturnDeposit={endDate}"+
                "&depositorMarkReturnDeposit={mark}"+
                "&submit={submit}","updateBankDepositor",1,"depositNameUpdate","2017-01-06",1000,1000,1000,"2017-01-06",0,"Generate+Request"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("services","requests","responses"))
                .andExpect(model().attribute("requests",soapRequest))
                .andExpect(model().attribute("responses",notNullValue()))
                .andDo(log())
                .andDo(print());

        verify(soapClient, times(1)).updateBankDepositor(depositor);
        verify(soapClient, only()).updateBankDepositor(depositor);
        verifyNoMoreInteractions(soapClient);
    }

    @Test
    public void testPostSoapQueryDeleteBankDeposit() throws Exception {
        LOGGER.debug("postSoapQuery(): deleteBankDeposit- start");

        soapRequest = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://bank.brest.com/soap\">\n" +
                "\t<x:Header/>\n" +
                "\t<x:Body>\n" +
                "\t\t<soa:deleteBankDepositRequest>\n" +
                "\t\t\t<soa:depositId>2</soa:depositId>\n" +
                "\t\t</soa:deleteBankDepositRequest>\n" +
                "\t</x:Body>\n" +
                "</x:Envelope>";

        soapResponse[0] = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<deleteBankDepositResponse xmlns=\"http://bank.brest.com/soap\">\n" +
                "    <result>Bank Deposit removed</result>\n" +
                "</deleteBankDepositResponse>\n";
        soapResponse[1] = "{\"deleteBankDepositResponse\": {\n" +
                "    \"result\": \"Bank Deposit removed\",\n" +
                "    \"xmlns\": \"http://bank.brest.com/soap\"\n" +
                "}}";

        when(soapClient.deleteBankDeposit(2L));
        mockMvc.perform(post("/client/submitSoapQuery?soapQuery={soapQuery}" +
                "&depositId={id}" +
                "&submit={submit}","deleteBankDeposit",2,"Generate+Request"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("services","requests","responses"))
                .andExpect(model().attribute("requests",soapRequest))
                //.andExpect(model().attribute("responses", is(soapResponse)))
                .andExpect(model().attribute("responses", notNullValue()))
                .andExpect(status().isOk())
                .andDo(print());


        verify(soapClient, times(1)).deleteBankDeposit(2L);
        verify(soapClient, only()).deleteBankDeposit(2L);
        verifyNoMoreInteractions(soapClient);
    }

    @Test
    public void testPostSoapQueryGetBankDepositsByCurrencyFromToDateDepositWithDepositors() throws Exception{
        LOGGER.debug("postSoapQuery() - start");
        soapRequest = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://bank.brest.com/soap\">\n" +
                "\t<x:Header/>\n" +
                "\t<x:Body>\n" +
                "\t\t<soa:getBankDepositsByCurrencyFromToDateDepositWithDepositorsRequest>\n" +
                "\t\t\t<soa:depositCurrency>usd</soa:depositCurrency>\n" +
                "\t\t\t<soa:startDate>Thu Jan 01 00:01:00 MSK 2015</soa:startDate>\n" +
                "\t\t\t<soa:endDate>Tue Jan 12 00:12:00 MSK 2016</soa:endDate>\n" +
                "\t\t</soa:getBankDepositsByCurrencyFromToDateDepositWithDepositorsRequest>\n" +
                "\t</x:Body>\n" +
                "</x:Envelope>";
        soapResponse[0] = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<getBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse xmlns=\"http://bank.brest.com/soap\">\n" +
                "    <bankDepositsReport>\n" +
                "        <bankDepositReport>\n" +
                "            <depositId>1</depositId>\n" +
                "            <depositName>depositName</depositName>\n" +
                "            <depositMinTerm>12</depositMinTerm>\n" +
                "            <depositMinAmount>100</depositMinAmount>\n" +
                "            <depositCurrency>usd</depositCurrency>\n" +
                "            <depositInterestRate>4</depositInterestRate>\n" +
                "            <depositAddConditions>condition0</depositAddConditions>\n" +
                "            <depositorCount xsi:nil=\"true\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>\n" +
                "            <depositorAmountSum>200</depositorAmountSum>\n" +
                "            <depositorAmountPlusSum>200</depositorAmountPlusSum>\n" +
                "            <depositorAmountMinusSum>200</depositorAmountMinusSum>\n" +
                "        </bankDepositReport>\n" +
                "    </bankDepositsReport>\n" +
                "</getBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse>\n";
        soapResponse[1] = "{\"getBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse\": {\n" +
                "    \"bankDepositsReport\": {\"bankDepositReport\": {\n" +
                "        \"depositMinTerm\": 12,\n" +
                "        \"depositInterestRate\": 4,\n" +
                "        \"depositorAmountPlusSum\": 200,\n" +
                "        \"depositName\": \"depositName\",\n" +
                "        \"depositorAmountSum\": 200,\n" +
                "        \"depositorAmountMinusSum\": 200,\n" +
                "        \"depositorCount\": {\n" +
                "            \"xsi:nil\": true,\n" +
                "            \"xmlns:xsi\": \"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "        },\n" +
                "        \"depositCurrency\": \"usd\",\n" +
                "        \"depositId\": 1,\n" +
                "        \"depositMinAmount\": 100,\n" +
                "        \"depositAddConditions\": \"condition0\"\n" +
                "    }},\n" +
                "    \"xmlns\": \"http://bank.brest.com/soap\"\n" +
                "}}";

        when(soapClient.getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd",
                dateFormat.parse("2015-01-01"),
                dateFormat.parse("2016-12-12")))
                .thenReturn(DataFixture.getExistDepositByCurrencyWsdl("usd"));

        mockMvc.perform(post("/client/submitSoapQuery?soapQuery={soapQuery}" +
                "&depositCurrency={currency}" +
                "&startDate={startDate}" +
                "&endDate={endDate}" +
                "&submit={submit}","getBankDepositsByCurrencyFromToDateDepositWithDepositors",
                "usd","2015-01-01","2016-12-12","Generate+Request"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("services","requests","responses"))
                .andExpect(model().attribute("requests",soapRequest))
                .andExpect(model().attribute("responses",notNullValue()))
                .andExpect(model().attribute("responses",is(soapResponse)))
                .andExpect(status().isOk())
                .andDo(log())
                .andDo(print());

        verify(soapClient, times(1)).getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd",
                dateFormat.parse("2015-01-01"),
                dateFormat.parse("2016-12-12"));
        verify(soapClient, only()).getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd",
                dateFormat.parse("2015-01-01"),
                dateFormat.parse("2016-12-12"));
        verifyNoMoreInteractions(soapClient);

    }

    @Test
    public void testGetSoapViewWithRequestRaram() throws Exception {
        LOGGER.debug("getSoapView() - start");

        when(soapClient.readWSDLFile(wsdlLocation)).thenReturn(wsdl);
        mockMvc.perform(get("/client/main?wsdlLoc=http://localhost:8080/SpringHibernateBDeposit-1.0/soap/soapService.wsdl"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("services","requests","responses"))
                .andExpect(model().attribute("requests",""))
                .andExpect(model().attribute("responses",notNullValue()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(soapClient).readWSDLFile(wsdlLocation);
    }

    @Test
    public void testGetSoapViewWithoutRequestRaram() throws Exception {
        LOGGER.debug("getSoapView() without Request Raram - start");

        wsdlServices.put("",null);

        mockMvc.perform(get("/client/main"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("services"))
                .andExpect(model().attribute("services",wsdlServices))
                .andExpect(model().attributeExists("requests"))
                .andExpect(model().attribute("requests",""))
                .andExpect(model().attributeExists("responses"))
                .andExpect(model().attribute("responses",soapResponse))
                .andExpect(status().isOk())
                .andDo(log())
                .andDo(print());

        verify(soapClient,never()).readWSDLFile(wsdlLocation);
    }

}
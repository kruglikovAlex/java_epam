package com.brest.bank.client;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RestClientTest {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static private final String HOST = "http://host";
    String test;
    private RestClient client;
    private MockRestServiceServer mockServer;

    public String depositForTestToString(BankDeposit deposit){
        return "BankDeposit: { depositId=" + deposit.getDepositId() +
                ", depositName=" + deposit.getDepositName() +
                ", depositMinTerm=" + deposit.getDepositMinTerm() +
                ", depositMinAmount=" + deposit.getDepositMinAmount() +
                ", depositCurrency=" + deposit.getDepositCurrency() +
                ", depositInterestRate=" + deposit.getDepositInterestRate() +
                ", depositAddConditions=" + deposit.getDepositAddConditions() + '}';
    }

    public String depositorForTestToString(BankDepositor depositor){
    return "BankDepositor: { depositorId ="+depositor.getDepositorId()+
            ", depositorName ="+ depositor.getDepositorName()+
            ", depositorIdDeposit ="+ depositor.getDepositorIdDeposit()+
            ", depositorDateDeposit ="+ dateFormat.format(depositor.getDepositorDateDeposit())+
            ", depositorAmountDeposit ="+ depositor.getDepositorAmountDeposit()+
            ", depositorAmountPlusDeposit ="+ depositor.getDepositorAmountPlusDeposit()+
            ", depositorAmountMinusDeposit ="+ depositor.getDepositorAmountMinusDeposit()+
            ", depositorDateReturnDeposit ="+ dateFormat.format(depositor.getDepositorDateReturnDeposit())+
            ", depositorMarkReturnDeposit ="+ depositor.getDepositorMarkReturnDeposit()  + '}';
    }

    @Before
    public void setUp(){
        client = new RestClient(HOST);
        mockServer = MockRestServiceServer.createServer(client.getRestTemplate());

    }

    @After
    public void check() {
        mockServer.verify();
    }

    @Test
    public void versionTest(){
        mockServer.expect(requestTo(HOST + "/version"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(withSuccess("123", MediaType.APPLICATION_JSON));

        String version = client.getRestVesrsion();
        assertEquals("123",version);
    }

    @Test
    public void addBankDepositTest(){
        mockServer.expect(requestTo(HOST+"/deposits/"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("{\"depositId\":null,\"depositName\":\"depositName1\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"}"))
                .andRespond(withSuccess("4",MediaType.APPLICATION_JSON));

        BankDeposit deposit = new BankDeposit();
            deposit.setDepositName("depositName1");
            deposit.setDepositMinTerm(12);
            deposit.setDepositMinAmount(100);
            deposit.setDepositCurrency("usd");
            deposit.setDepositInterestRate(4);
            deposit.setDepositAddConditions("condition1");
        long id = client.addBankDeposit(deposit);
        assertEquals(4, id);
    }

    @Test
    public void getBankDepositByIdTest(){
        mockServer.expect(requestTo(HOST+"/deposits/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"}",MediaType.APPLICATION_JSON));

        BankDeposit deposit = client.getBankDepositById(1L);

        assertNotNull(deposit);

        assertNotNull(deposit.getDepositId());
        assertEquals(new Long(1),deposit.getDepositId());
        assertNotNull(deposit.getDepositName());
        assertEquals("depositName1",deposit.getDepositName());
        assertNotNull(deposit.getDepositMinTerm());
        assertEquals(12,deposit.getDepositMinTerm());
        assertNotNull(deposit.getDepositMinAmount());
        assertEquals(100,deposit.getDepositMinAmount());
        assertNotNull(deposit.getDepositCurrency());
        assertEquals("usd",deposit.getDepositCurrency());
        assertNotNull(deposit.getDepositInterestRate());
        assertEquals(4,deposit.getDepositInterestRate());
        assertNotNull(deposit.getDepositAddConditions());
        assertEquals("condition1",deposit.getDepositAddConditions());
        test = "BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1}";
        assertEquals(test,deposit.toString());
    }

    @Test
    public void getBankDepositByNameTest(){
        mockServer.expect(requestTo(HOST+"/deposits/name/depositName1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"}",MediaType.APPLICATION_JSON));
// проверить со строковой переменной

        BankDeposit deposit = client.getBankDepositByName("depositName1");

        assertNotNull(deposit);

        assertNotNull(deposit.getDepositId());
        assertEquals(new Long(1),deposit.getDepositId());
        assertNotNull(deposit.getDepositName());
        assertEquals("depositName1",deposit.getDepositName());
        assertNotNull(deposit.getDepositMinTerm());
        assertEquals(12,deposit.getDepositMinTerm());
        assertNotNull(deposit.getDepositMinAmount());
        assertEquals(100,deposit.getDepositMinAmount());
        assertNotNull(deposit.getDepositCurrency());
        assertEquals("usd",deposit.getDepositCurrency());
        assertNotNull(deposit.getDepositInterestRate());
        assertEquals(4,deposit.getDepositInterestRate());
        assertNotNull(deposit.getDepositAddConditions());
        assertEquals("condition1",deposit.getDepositAddConditions());
        test = "BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1}";
        assertEquals(test,deposit.toString());
    }

    @Test
    public void getUsers() {
        mockServer.expect(requestTo(HOST + "/deposits/"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"}," +
                                         "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition2\"}," +
                                         "{\"depositId\":3,\"depositName\":\"depositName3\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition3\"}]",
                        MediaType.APPLICATION_JSON));

        BankDeposit[] deposits = client.getBankDeposits();

        assertEquals(3, deposits.length);
        assertNotNull(deposits[0]);
        assertNotNull(deposits[1]);
        assertNotNull(deposits[2]);

        assertNotNull(deposits[0].getDepositId());
        assertNotNull(deposits[1].getDepositId());
        assertNotNull(deposits[2].getDepositId());

        assertNotNull(deposits[0].getDepositName());
        assertNotNull(deposits[1].getDepositName());
        assertNotNull(deposits[2].getDepositName());

        assertNotNull(deposits[0].getDepositMinTerm());
        assertNotNull(deposits[1].getDepositMinTerm());
        assertNotNull(deposits[2].getDepositMinTerm());

        assertEquals(new Long(1), deposits[0].getDepositId());
        assertEquals(new Long(2), deposits[1].getDepositId());
        assertEquals(new Long(3), deposits[2].getDepositId());

        assertEquals("depositName1", deposits[0].getDepositName());
        assertEquals("depositName2", deposits[1].getDepositName());
        assertEquals("depositName3", deposits[2].getDepositName());

        assertEquals("BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1}",deposits[0].toString());
        assertEquals("BankDeposit: { depositId=2, depositName=depositName2, depositMinTerm=12, depositMinAmount=100, depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition2}",deposits[1].toString());
        assertEquals("BankDeposit: { depositId=3, depositName=depositName3, depositMinTerm=12, depositMinAmount=100, depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition3}",deposits[2].toString());
    }

    @Test
    public void updateBankDeposit() {
        mockServer.expect(requestTo(HOST+"/deposits/"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("{\"depositId\":1,\"depositName\":\"newDepositName1\",\"depositMinTerm\":9,\"depositMinAmount\":1000,\"depositCurrency\":\"eur\",\"depositInterestRate\":6,\"depositAddConditions\":\"newCondition1\"}"))
                .andRespond(withSuccess("",MediaType.APPLICATION_JSON));
        BankDeposit deposit = new BankDeposit();
            deposit.setDepositId(1L);
            deposit.setDepositName("newDepositName1");
            deposit.setDepositMinTerm(9);
            deposit.setDepositMinAmount(1000);
            deposit.setDepositCurrency("eur");
            deposit.setDepositInterestRate(6);
            deposit.setDepositAddConditions("newCondition1");

        client.updateBankDeposit(deposit);
    }

    @Test
    public void removeBankDeposit() {
        mockServer.expect(requestTo(HOST + "/deposits/1"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));
        client.removeBankDeposit(1L);
    }
    //=============================
    @Test
    public void getBankDepositorByIdTest(){
        mockServer.expect(requestTo(HOST+"/depositors/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"depositorId\":1,\"depositorName\":\"depositorName1\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}",MediaType.APPLICATION_JSON));

        BankDepositor depositor = client.getBankDepositorById(1L);

        assertNotNull(depositor);

        assertNotNull(depositor.getDepositorId());
        assertEquals(new Long(1),depositor.getDepositorId());

        assertNotNull(depositor.getDepositorName());
        assertEquals("depositorName1",depositor.getDepositorName());

        assertNotNull(depositor.getDepositorIdDeposit());
        assertEquals(new Long(1),depositor.getDepositorIdDeposit());

        assertNotNull(depositor.getDepositorDateDeposit());
        assertEquals("2014-12-01",dateFormat.format(depositor.getDepositorDateDeposit()));

        assertNotNull(depositor.getDepositorAmountDeposit());
        assertEquals(10000,depositor.getDepositorAmountDeposit());

        assertNotNull(depositor.getDepositorAmountPlusDeposit());
        assertEquals(1000,depositor.getDepositorAmountPlusDeposit());

        assertNotNull(depositor.getDepositorAmountMinusDeposit());
        assertEquals(11000,depositor.getDepositorAmountMinusDeposit());

        assertNotNull(depositor.getDepositorDateReturnDeposit());
        assertEquals("2014-12-02",dateFormat.format(depositor.getDepositorDateReturnDeposit()));

        assertNotNull(depositor.getDepositorMarkReturnDeposit());
        assertEquals(0,depositor.getDepositorMarkReturnDeposit());

        test = "BankDepositor: { depositorId =1, depositorName =depositorName1, depositorIdDeposit =1, depositorDateDeposit =2014-12-01, depositorAmountDeposit =10000, depositorAmountPlusDeposit =1000, depositorAmountMinusDeposit =11000, depositorDateReturnDeposit =2014-12-02, depositorMarkReturnDeposit =0}";
        assertEquals(test,depositor.toString());
    }

    @Test
    public void getBankDepositorByIdDepositTest(){
        mockServer.expect(requestTo(HOST+"/depositors/deposit/1"))
                .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess("[{\"depositorId\":1,\"depositorName\":\"depositorName1\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}," +
                "{\"depositorId\":2,\"depositorName\":\"depositorName2\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}," +
                "{\"depositorId\":3,\"depositorName\":\"depositorName3\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}]"
                , MediaType.APPLICATION_JSON));

        BankDepositor[] depositors = client.getBankDepositorByIdDeposit(1L);

        assertEquals(3, depositors.length);
        assertNotNull(depositors[0]);
        assertNotNull(depositors[1]);
        assertNotNull(depositors[2]);


        assertNotNull(depositors[0].getDepositorId());
        assertEquals(new Long(1),depositors[0].getDepositorId());

        assertNotNull(depositors[0].getDepositorName());
        assertEquals("depositorName1",depositors[0].getDepositorName());

        assertNotNull(depositors[0].getDepositorIdDeposit());
        assertEquals(new Long(1),depositors[0].getDepositorIdDeposit());

        assertNotNull(depositors[0].getDepositorDateDeposit());
        assertEquals("2014-12-01",dateFormat.format(depositors[0].getDepositorDateDeposit()));

        assertNotNull(depositors[0].getDepositorAmountDeposit());
        assertEquals(10000,depositors[0].getDepositorAmountDeposit());

        assertNotNull(depositors[0].getDepositorAmountPlusDeposit());
        assertEquals(1000,depositors[0].getDepositorAmountPlusDeposit());

        assertNotNull(depositors[0].getDepositorAmountMinusDeposit());
        assertEquals(11000,depositors[0].getDepositorAmountMinusDeposit());

        assertNotNull(depositors[0].getDepositorDateReturnDeposit());
        assertEquals("2014-12-02",dateFormat.format(depositors[0].getDepositorDateReturnDeposit()));

        assertNotNull(depositors[0].getDepositorMarkReturnDeposit());
        assertEquals(0,depositors[0].getDepositorMarkReturnDeposit());

        test = "BankDepositor: { depositorId =1, depositorName =depositorName1, depositorIdDeposit =1, depositorDateDeposit =2014-12-01, depositorAmountDeposit =10000, depositorAmountPlusDeposit =1000, depositorAmountMinusDeposit =11000, depositorDateReturnDeposit =2014-12-02, depositorMarkReturnDeposit =0}";
        assertEquals(test,depositors[0].toString());
        test = "BankDepositor: { depositorId =2, depositorName =depositorName2, depositorIdDeposit =1, depositorDateDeposit =2014-12-01, depositorAmountDeposit =10000, depositorAmountPlusDeposit =1000, depositorAmountMinusDeposit =11000, depositorDateReturnDeposit =2014-12-02, depositorMarkReturnDeposit =0}";
        assertEquals(test,depositors[1].toString());
        test = "BankDepositor: { depositorId =3, depositorName =depositorName3, depositorIdDeposit =1, depositorDateDeposit =2014-12-01, depositorAmountDeposit =10000, depositorAmountPlusDeposit =1000, depositorAmountMinusDeposit =11000, depositorDateReturnDeposit =2014-12-02, depositorMarkReturnDeposit =0}";
        assertEquals(test,depositors[2].toString());

    }

    @Test
    public void getBankDepositorByNameTest(){
        mockServer.expect(requestTo(HOST+"/depositors/name/depositorName1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"depositorId\":1,\"depositorName\":\"depositorName1\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}",MediaType.APPLICATION_JSON));

        BankDepositor depositor = client.getBankDepositorByName("depositorName1");

        assertNotNull(depositor);

        assertNotNull(depositor.getDepositorId());
        assertEquals(new Long(1),depositor.getDepositorId());

        assertNotNull(depositor.getDepositorName());
        assertEquals("depositorName1",depositor.getDepositorName());

        assertNotNull(depositor.getDepositorIdDeposit());
        assertEquals(new Long(1),depositor.getDepositorIdDeposit());

        assertNotNull(depositor.getDepositorDateDeposit());
        assertEquals("2014-12-01",dateFormat.format(depositor.getDepositorDateDeposit()));

        assertNotNull(depositor.getDepositorAmountDeposit());
        assertEquals(10000,depositor.getDepositorAmountDeposit());

        assertNotNull(depositor.getDepositorAmountPlusDeposit());
        assertEquals(1000,depositor.getDepositorAmountPlusDeposit());

        assertNotNull(depositor.getDepositorAmountMinusDeposit());
        assertEquals(11000,depositor.getDepositorAmountMinusDeposit());

        assertNotNull(depositor.getDepositorDateReturnDeposit());
        assertEquals("2014-12-02",dateFormat.format(depositor.getDepositorDateReturnDeposit()));

        assertNotNull(depositor.getDepositorMarkReturnDeposit());
        assertEquals(0,depositor.getDepositorMarkReturnDeposit());

        test = "BankDepositor: { depositorId =1, depositorName =depositorName1, depositorIdDeposit =1, depositorDateDeposit =2014-12-01, depositorAmountDeposit =10000, depositorAmountPlusDeposit =1000, depositorAmountMinusDeposit =11000, depositorDateReturnDeposit =2014-12-02, depositorMarkReturnDeposit =0}";
        assertEquals(test,depositor.toString());
    }

    @Test
    public void getBankDepositorBetweenDateDepositTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/depositors/date/2014-12-01/2014-12-02"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositorId\":1,\"depositorName\":\"depositorName1\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}," +
                        "{\"depositorId\":2,\"depositorName\":\"depositorName2\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}," +
                        "{\"depositorId\":3,\"depositorName\":\"depositorName3\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}]"
                        ,MediaType.APPLICATION_JSON));


        BankDepositor[] depositors = client.getBankDepositorBetweenDateDeposit(dateFormat.parse("2014-12-01"),dateFormat.parse("2014-12-02"));

        assertEquals(3, depositors.length);
        assertNotNull(depositors[0]);
        assertNotNull(depositors[1]);
        assertNotNull(depositors[2]);


        assertNotNull(depositors[0].getDepositorId());
        assertEquals(new Long(1),depositors[0].getDepositorId());

        assertNotNull(depositors[0].getDepositorName());
        assertEquals("depositorName1",depositors[0].getDepositorName());

        assertNotNull(depositors[0].getDepositorIdDeposit());
        assertEquals(new Long(1),depositors[0].getDepositorIdDeposit());

        assertNotNull(depositors[0].getDepositorDateDeposit());
        assertEquals("2014-12-01",dateFormat.format(depositors[0].getDepositorDateDeposit()));

        assertNotNull(depositors[0].getDepositorAmountDeposit());
        assertEquals(10000,depositors[0].getDepositorAmountDeposit());

        assertNotNull(depositors[0].getDepositorAmountPlusDeposit());
        assertEquals(1000,depositors[0].getDepositorAmountPlusDeposit());

        assertNotNull(depositors[0].getDepositorAmountMinusDeposit());
        assertEquals(11000,depositors[0].getDepositorAmountMinusDeposit());

        assertNotNull(depositors[0].getDepositorDateReturnDeposit());
        assertEquals("2014-12-02",dateFormat.format(depositors[0].getDepositorDateReturnDeposit()));

        assertNotNull(depositors[0].getDepositorMarkReturnDeposit());
        assertEquals(0,depositors[0].getDepositorMarkReturnDeposit());

        test = "BankDepositor: { depositorId =1, depositorName =depositorName1, depositorIdDeposit =1, depositorDateDeposit =2014-12-01, depositorAmountDeposit =10000, depositorAmountPlusDeposit =1000, depositorAmountMinusDeposit =11000, depositorDateReturnDeposit =2014-12-02, depositorMarkReturnDeposit =0}";
        assertEquals(test,depositors[0].toString());
        test = "BankDepositor: { depositorId =2, depositorName =depositorName2, depositorIdDeposit =1, depositorDateDeposit =2014-12-01, depositorAmountDeposit =10000, depositorAmountPlusDeposit =1000, depositorAmountMinusDeposit =11000, depositorDateReturnDeposit =2014-12-02, depositorMarkReturnDeposit =0}";
        assertEquals(test,depositors[1].toString());
        test = "BankDepositor: { depositorId =3, depositorName =depositorName3, depositorIdDeposit =1, depositorDateDeposit =2014-12-01, depositorAmountDeposit =10000, depositorAmountPlusDeposit =1000, depositorAmountMinusDeposit =11000, depositorDateReturnDeposit =2014-12-02, depositorMarkReturnDeposit =0}";
        assertEquals(test,depositors[2].toString());
    }

    @Test
    public void getBankDepositorBetweenDateReturnDepositTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/depositors/date/return/2014-12-01/2014-12-02"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositorId\":1,\"depositorName\":\"depositorName1\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}," +
                        "{\"depositorId\":2,\"depositorName\":\"depositorName2\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}," +
                        "{\"depositorId\":3,\"depositorName\":\"depositorName3\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}]"
                        ,MediaType.APPLICATION_JSON));


        BankDepositor[] depositors = client.getBankDepositorBetweenDateReturnDeposit(dateFormat.parse("2014-12-01"),dateFormat.parse("2014-12-02"));

        assertEquals(3, depositors.length);
        assertNotNull(depositors[0]);
        assertNotNull(depositors[1]);
        assertNotNull(depositors[2]);


        assertNotNull(depositors[0].getDepositorId());
        assertEquals(new Long(1),depositors[0].getDepositorId());

        assertNotNull(depositors[0].getDepositorName());
        assertEquals("depositorName1",depositors[0].getDepositorName());

        assertNotNull(depositors[0].getDepositorIdDeposit());
        assertEquals(new Long(1),depositors[0].getDepositorIdDeposit());

        assertNotNull(depositors[0].getDepositorDateDeposit());
        assertEquals("2014-12-01",dateFormat.format(depositors[0].getDepositorDateDeposit()));

        assertNotNull(depositors[0].getDepositorAmountDeposit());
        assertEquals(10000,depositors[0].getDepositorAmountDeposit());

        assertNotNull(depositors[0].getDepositorAmountPlusDeposit());
        assertEquals(1000,depositors[0].getDepositorAmountPlusDeposit());

        assertNotNull(depositors[0].getDepositorAmountMinusDeposit());
        assertEquals(11000,depositors[0].getDepositorAmountMinusDeposit());

        assertNotNull(depositors[0].getDepositorDateReturnDeposit());
        assertEquals("2014-12-02",dateFormat.format(depositors[0].getDepositorDateReturnDeposit()));

        assertNotNull(depositors[0].getDepositorMarkReturnDeposit());
        assertEquals(0,depositors[0].getDepositorMarkReturnDeposit());

        test = "BankDepositor: { depositorId =1, depositorName =depositorName1, depositorIdDeposit =1, depositorDateDeposit =2014-12-01, depositorAmountDeposit =10000, depositorAmountPlusDeposit =1000, depositorAmountMinusDeposit =11000, depositorDateReturnDeposit =2014-12-02, depositorMarkReturnDeposit =0}";
        assertEquals(test,depositors[0].toString());
        test = "BankDepositor: { depositorId =2, depositorName =depositorName2, depositorIdDeposit =1, depositorDateDeposit =2014-12-01, depositorAmountDeposit =10000, depositorAmountPlusDeposit =1000, depositorAmountMinusDeposit =11000, depositorDateReturnDeposit =2014-12-02, depositorMarkReturnDeposit =0}";
        assertEquals(test,depositors[1].toString());
        test = "BankDepositor: { depositorId =3, depositorName =depositorName3, depositorIdDeposit =1, depositorDateDeposit =2014-12-01, depositorAmountDeposit =10000, depositorAmountPlusDeposit =1000, depositorAmountMinusDeposit =11000, depositorDateReturnDeposit =2014-12-02, depositorMarkReturnDeposit =0}";
        assertEquals(test,depositors[2].toString());
    }

    @Test
    public void addBankDepositorTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/depositors/"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("{\"depositorId\":null,\"depositorName\":\"depositorName2\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}"))
                .andRespond(withSuccess("4",MediaType.APPLICATION_JSON));


        BankDepositor depositor = new BankDepositor();
        depositor.setDepositorName("depositorName2");
        depositor.setDepositorIdDeposit(1L);
        depositor.setDepositorDateDeposit(dateFormat.parse("2014-12-01"));
        depositor.setDepositorAmountDeposit(10000);
        depositor.setDepositorAmountPlusDeposit(1000);
        depositor.setDepositorAmountMinusDeposit(11000);
        depositor.setDepositorDateReturnDeposit(dateFormat.parse("2014-12-02"));
        depositor.setDepositorMarkReturnDeposit(0);
        long id = client.addBankDepositor(depositor);
        assertEquals(4, id);
    }

    @Test
    public void updateBankDepositor() throws ParseException{
        mockServer.expect(requestTo(HOST+"/depositors/"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("{\"depositorId\":2,\"depositorName\":\"newDepositorName1\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}"))
                .andRespond(withSuccess("",MediaType.APPLICATION_JSON));
        BankDepositor depositor = new BankDepositor();
        depositor.setDepositorId(2L);
        depositor.setDepositorName("newDepositorName1");
        depositor.setDepositorIdDeposit(1L);
        depositor.setDepositorDateDeposit(dateFormat.parse("2014-12-01"));
        depositor.setDepositorAmountDeposit(10000);
        depositor.setDepositorAmountPlusDeposit(1000);
        depositor.setDepositorAmountMinusDeposit(11000);
        depositor.setDepositorDateReturnDeposit(dateFormat.parse("2014-12-02"));
        depositor.setDepositorMarkReturnDeposit(0);

        client.updateBankDepositor(depositor);
    }

    @Test
    public void removeBankDepositor() {
        mockServer.expect(requestTo(HOST + "/depositors/1"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));
        client.removeBankDepositor(1L);
    }

}


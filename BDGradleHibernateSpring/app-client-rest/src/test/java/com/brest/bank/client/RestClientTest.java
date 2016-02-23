package com.brest.bank.client;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.util.Assert;

import javax.xml.crypto.Data;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class RestClientTest {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String HOST = "http://host";

    private RestClient restClient;
    private MockRestServiceServer mockServer;

    @Before
    public void setUp() {
        restClient = new RestClient(HOST);
        mockServer = MockRestServiceServer.createServer(restClient.getRestTemplate());
    }

    @After
    public void check() {
        mockServer.verify();
    }

    @Test
    public void versionTest() {
        mockServer.expect(requestTo(HOST + "/version"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("1.0", MediaType.APPLICATION_JSON));

        String version = restClient.getRestVersion();
        assertEquals("1.0", version);
    }

    @Test
    public void getBankDepositsTest() {
        mockServer.expect(requestTo(HOST + "/deposit/all"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"}," +
                                "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition2\"}," +
                                "{\"depositId\":3,\"depositName\":\"depositName3\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition3\"}]",
                        MediaType.APPLICATION_JSON));

        BankDeposit[] deposits = restClient.getBankDeposits();

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

        assertEquals("BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1}", deposits[0].toString());
        assertEquals("BankDeposit: { depositId=2, depositName=depositName2, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition2}", deposits[1].toString());
        assertEquals("BankDeposit: { depositId=3, depositName=depositName3, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition3}", deposits[2].toString());
    }

    @Test
    public void getDepositByIdTest() {
        mockServer.expect(requestTo(HOST + "/deposit/id/1"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"}",
                        MediaType.APPLICATION_JSON));

        BankDeposit deposit = restClient.getDepositById(1L);

        assertNotNull(deposit);

        assertNotNull(deposit.getDepositId());
        assertEquals(new Long(1), deposit.getDepositId());
        assertNotNull(deposit.getDepositName());
        assertEquals("depositName1", deposit.getDepositName());
        assertNotNull(deposit.getDepositMinTerm());
        assertEquals(12, deposit.getDepositMinTerm());
        assertNotNull(deposit.getDepositMinAmount());
        assertEquals(100, deposit.getDepositMinAmount());
        assertNotNull(deposit.getDepositCurrency());
        assertEquals("usd", deposit.getDepositCurrency());
        assertNotNull(deposit.getDepositInterestRate());
        assertEquals(4, deposit.getDepositInterestRate());
        assertNotNull(deposit.getDepositAddConditions());
        assertEquals("condition1", deposit.getDepositAddConditions());
        String test = "BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1}";
        assertEquals(test, deposit.toString());
    }

    @Test
    public void getDepositByNameTest() {
        mockServer.expect(requestTo(HOST + "/deposit/name/depositName1"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"}",
                        MediaType.APPLICATION_JSON));

        BankDeposit deposit = restClient.getDepositByName("depositName1");

        assertNotNull(deposit);

        assertNotNull(deposit.getDepositId());
        assertEquals(new Long(1), deposit.getDepositId());
        assertNotNull(deposit.getDepositName());
        assertEquals("depositName1", deposit.getDepositName());
        assertNotNull(deposit.getDepositMinTerm());
        assertEquals(12, deposit.getDepositMinTerm());
        assertNotNull(deposit.getDepositMinAmount());
        assertEquals(100, deposit.getDepositMinAmount());
        assertNotNull(deposit.getDepositCurrency());
        assertEquals("usd", deposit.getDepositCurrency());
        assertNotNull(deposit.getDepositInterestRate());
        assertEquals(4, deposit.getDepositInterestRate());
        assertNotNull(deposit.getDepositAddConditions());
        assertEquals("condition1", deposit.getDepositAddConditions());
        String test = "BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1}";
        assertEquals(test, deposit.toString());
    }

    @Test
    public void getBankDepositsByCurrencyTest() {
        mockServer.expect(requestTo(HOST + "/deposit/currency/usd"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"}," +
                                "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition2\"}]",
                        MediaType.APPLICATION_JSON));

        BankDeposit[] deposits = restClient.getBankDepositsByCurrency("usd");

        assertEquals(2, deposits.length);
        assertNotNull(deposits[0]);
        assertNotNull(deposits[1]);

        assertNotNull(deposits[0].getDepositId());
        assertNotNull(deposits[1].getDepositId());

        assertNotNull(deposits[0].getDepositName());
        assertNotNull(deposits[1].getDepositName());

        assertNotNull(deposits[0].getDepositMinTerm());
        assertNotNull(deposits[1].getDepositMinTerm());

        assertEquals(new Long(1), deposits[0].getDepositId());
        assertEquals(new Long(2), deposits[1].getDepositId());

        assertEquals("depositName1", deposits[0].getDepositName());
        assertEquals("depositName2", deposits[1].getDepositName());

        assertEquals("BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1}", deposits[0].toString());
        assertEquals("BankDeposit: { depositId=2, depositName=depositName2, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition2}", deposits[1].toString());
    }

    @Test
    public void getBankDepositsByInterestRateTest() {
        mockServer.expect(requestTo(HOST + "/deposit/rate/4"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"}," +
                                "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition2\"}]",
                        MediaType.APPLICATION_JSON));

        BankDeposit[] deposits = restClient.getBankDepositsByInterestRate(4);

        assertEquals(2, deposits.length);
        assertNotNull(deposits[0]);
        assertNotNull(deposits[1]);

        assertNotNull(deposits[0].getDepositId());
        assertNotNull(deposits[1].getDepositId());

        assertNotNull(deposits[0].getDepositName());
        assertNotNull(deposits[1].getDepositName());

        assertNotNull(deposits[0].getDepositMinTerm());
        assertNotNull(deposits[1].getDepositMinTerm());

        assertEquals(new Long(1), deposits[0].getDepositId());
        assertEquals(new Long(2), deposits[1].getDepositId());

        assertEquals("depositName1", deposits[0].getDepositName());
        assertEquals("depositName2", deposits[1].getDepositName());

        assertEquals("BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1}", deposits[0].toString());
        assertEquals("BankDeposit: { depositId=2, depositName=depositName2, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition2}", deposits[1].toString());
    }

    @Test
    public void getBankDepositsFromToMinTermTest() {
        mockServer.expect(requestTo(HOST + "/deposit/term/11,12"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"}," +
                                "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition2\"}]",
                        MediaType.APPLICATION_JSON));

        BankDeposit[] deposits = restClient.getBankDepositsFromToMinTerm(11, 12);

        assertEquals(2, deposits.length);
        assertNotNull(deposits[0]);
        assertNotNull(deposits[1]);

        assertNotNull(deposits[0].getDepositId());
        assertNotNull(deposits[1].getDepositId());

        assertNotNull(deposits[0].getDepositName());
        assertNotNull(deposits[1].getDepositName());

        assertNotNull(deposits[0].getDepositMinTerm());
        assertNotNull(deposits[1].getDepositMinTerm());

        assertEquals(new Long(1), deposits[0].getDepositId());
        assertEquals(new Long(2), deposits[1].getDepositId());

        assertEquals("depositName1", deposits[0].getDepositName());
        assertEquals("depositName2", deposits[1].getDepositName());

        assertEquals("BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1}", deposits[0].toString());
        assertEquals("BankDeposit: { depositId=2, depositName=depositName2, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition2}", deposits[1].toString());

    }

    @Test
    public void getBankDepositsFromToInterestRateTest() {
        mockServer.expect(requestTo(HOST + "/deposit/rateBetween/3,5"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"}," +
                                "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition2\"}]",
                        MediaType.APPLICATION_JSON));

        BankDeposit[] deposits = restClient.getBankDepositsFromToInterestRate(3, 5);

        assertEquals(2, deposits.length);
        assertNotNull(deposits[0]);
        assertNotNull(deposits[1]);

        assertNotNull(deposits[0].getDepositId());
        assertNotNull(deposits[1].getDepositId());

        assertNotNull(deposits[0].getDepositName());
        assertNotNull(deposits[1].getDepositName());

        assertNotNull(deposits[0].getDepositMinTerm());
        assertNotNull(deposits[1].getDepositMinTerm());

        assertEquals(new Long(1), deposits[0].getDepositId());
        assertEquals(new Long(2), deposits[1].getDepositId());

        assertEquals("depositName1", deposits[0].getDepositName());
        assertEquals("depositName2", deposits[1].getDepositName());

        assertEquals("BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1}", deposits[0].toString());
        assertEquals("BankDeposit: { depositId=2, depositName=depositName2, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition2}", deposits[1].toString());

    }

    @Test
    public void getBankDepositsFromToDateDepositTest() throws ParseException {
        mockServer.expect(requestTo(HOST + "/deposit/date/2015-01-01,2015-02-02"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"}," +
                                "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition2\"}]",
                        MediaType.APPLICATION_JSON));

        BankDeposit[] deposits = restClient.getBankDepositsFromToDateDeposit(dateFormat.parse("2015-01-01"), dateFormat.parse("2015-02-02"));

        assertEquals(2, deposits.length);
        assertNotNull(deposits[0]);
        assertNotNull(deposits[1]);

        assertNotNull(deposits[0].getDepositId());
        assertNotNull(deposits[1].getDepositId());

        assertNotNull(deposits[0].getDepositName());
        assertNotNull(deposits[1].getDepositName());

        assertNotNull(deposits[0].getDepositMinTerm());
        assertNotNull(deposits[1].getDepositMinTerm());

        assertEquals(new Long(1), deposits[0].getDepositId());
        assertEquals(new Long(2), deposits[1].getDepositId());

        assertEquals("depositName1", deposits[0].getDepositName());
        assertEquals("depositName2", deposits[1].getDepositName());

        assertEquals("BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1}", deposits[0].toString());
        assertEquals("BankDeposit: { depositId=2, depositName=depositName2, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition2}", deposits[1].toString());
    }

    @Test
    public void getBankDepositsFromToDateReturnDepositTest() throws ParseException {
        mockServer.expect(requestTo(HOST + "/deposit/returnDate/2015-01-01,2015-02-02"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"}," +
                                "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition2\"}]",
                        MediaType.APPLICATION_JSON));

        BankDeposit[] deposits = restClient.getBankDepositsFromToDateReturnDeposit(dateFormat.parse("2015-01-01"), dateFormat.parse("2015-02-02"));

        assertEquals(2, deposits.length);
        assertNotNull(deposits[0]);
        assertNotNull(deposits[1]);

        assertNotNull(deposits[0].getDepositId());
        assertNotNull(deposits[1].getDepositId());

        assertNotNull(deposits[0].getDepositName());
        assertNotNull(deposits[1].getDepositName());

        assertNotNull(deposits[0].getDepositMinTerm());
        assertNotNull(deposits[1].getDepositMinTerm());

        assertEquals(new Long(1), deposits[0].getDepositId());
        assertEquals(new Long(2), deposits[1].getDepositId());

        assertEquals("depositName1", deposits[0].getDepositName());
        assertEquals("depositName2", deposits[1].getDepositName());

        assertEquals("BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1}", deposits[0].toString());
        assertEquals("BankDeposit: { depositId=2, depositName=depositName2, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition2}", deposits[1].toString());

    }

    @Test
    public void getBankDepositByNameWithDepositorsTest(){
        mockServer.expect(requestTo(HOST+"/deposit/report/name/depositName1"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                        "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}",
                        MediaType.APPLICATION_JSON));

        Map deposit = restClient.getBankDepositByNameWithDepositors("depositName1");

        assertEquals("{depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1, depositorCount=2, " +
                "depositorAmountSum=2000, depositorAmountPlusSum=200, depositorAmountMinusSum=200}", deposit.toString());
    }

    @Test
    public void getBankDepositByNameFromToDateDepositWithDepositorsTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/deposit/report/nameDate/depositName1,2015-01-01,2015-02-02"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                                "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                                "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                                "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}",
                        MediaType.APPLICATION_JSON));

        Map deposit = restClient.getBankDepositByNameFromToDateDepositWithDepositors("depositName1",
                dateFormat.parse("2015-01-01"),dateFormat.parse("2015-02-02"));

        assertEquals("{depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1, depositorCount=2, " +
                "depositorAmountSum=2000, depositorAmountPlusSum=200, depositorAmountMinusSum=200}", deposit.toString());
    }

    @Test
    public void getBankDepositByNameFromToDateReturnDepositWithDepositorsTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/deposit/report/nameDateReturn/depositName1,2015-01-01,2015-02-02"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                                "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                                "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                                "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}",
                        MediaType.APPLICATION_JSON));

        Map deposit = restClient.getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1",
                dateFormat.parse("2015-01-01"),dateFormat.parse("2015-02-02"));

        assertEquals("{depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1, depositorCount=2, " +
                "depositorAmountSum=2000, depositorAmountPlusSum=200, depositorAmountMinusSum=200}", deposit.toString());
    }

    @Test
    public void getBankDepositByIdWithDepositorsTest(){
        mockServer.expect(requestTo(HOST+"/deposit/report/id/1"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                                "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                                "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                                "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}",
                        MediaType.APPLICATION_JSON));
        Map deposit = restClient.getBankDepositByIdWithDepositors(1L);

        assertEquals("{depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1, depositorCount=2, " +
                "depositorAmountSum=2000, depositorAmountPlusSum=200, depositorAmountMinusSum=200}", deposit.toString());
    }

    @Test
    public void getBankDepositByIdFromToDateDepositWithDepositorsTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/deposit/report/idDate/1,2015-01-01,2015-02-02"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                                "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                                "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                                "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}",
                        MediaType.APPLICATION_JSON));

        Map deposit = restClient.getBankDepositByIdFromToDateDepositWithDepositors(1L,
                dateFormat.parse("2015-01-01"),dateFormat.parse("2015-02-02"));

        assertEquals("{depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1, depositorCount=2, " +
                "depositorAmountSum=2000, depositorAmountPlusSum=200, depositorAmountMinusSum=200}", deposit.toString());
    }

    @Test
    public void getBankDepositByIdFromToDateReturnDepositWithDepositorsTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/deposit/report/idDateReturn/1,2015-01-01,2015-02-02"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                                "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                                "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                                "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}",
                        MediaType.APPLICATION_JSON));

        Map deposit = restClient.getBankDepositByIdFromToDateReturnDepositWithDepositors(1L,
                dateFormat.parse("2015-01-01"),dateFormat.parse("2015-02-02"));

        assertEquals("{depositId=1, depositName=depositName1, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition1, depositorCount=2, " +
                "depositorAmountSum=2000, depositorAmountPlusSum=200, depositorAmountMinusSum=200}", deposit.toString());
    }

    @Test
    public void getBankDepositsWithDepositorsTest(){
        mockServer.expect(requestTo(HOST+"/deposit/report/all"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                        "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}," +
                        "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                        "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}]",
                        MediaType.APPLICATION_JSON));

        Map[] deposits = restClient.getBankDepositsWithDepositors();

        Assert.isTrue(deposits.length == 2);

        assertEquals(1,deposits[0].get("depositId"));
        assertEquals(2,deposits[1].get("depositId"));

        assertEquals("depositName1",deposits[0].get("depositName"));
        assertEquals("depositName2",deposits[1].get("depositName"));

        assertEquals(2000,deposits[0].get("depositorAmountSum"));
        assertEquals(2000,deposits[1].get("depositorAmountSum"));
    }

    @Test
    public void getBankDepositsFromToDateDepositWithDepositorsTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/deposit/report/allDate/2015-01-01,2015-02-02"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                                "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                                "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                                "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}," +
                                "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12," +
                                "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                                "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                                "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}]",
                        MediaType.APPLICATION_JSON));

        Map[] deposits = restClient.getBankDepositsFromToDateDepositWithDepositors(dateFormat.parse("2015-01-01"),
                dateFormat.parse("2015-02-02"));

        Assert.isTrue(deposits.length == 2);

        assertEquals(1,deposits[0].get("depositId"));
        assertEquals(2,deposits[1].get("depositId"));

        assertEquals("depositName1",deposits[0].get("depositName"));
        assertEquals("depositName2",deposits[1].get("depositName"));

        assertEquals(2000,deposits[0].get("depositorAmountSum"));
        assertEquals(2000,deposits[1].get("depositorAmountSum"));
    }

    @Test
    public void getBankDepositsFromToDateReturnDepositWithDepositorsTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/deposit/report/allDateReturn/2015-01-01,2015-02-02"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                                "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                                "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                                "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}," +
                                "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12," +
                                "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                                "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                                "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}]",
                        MediaType.APPLICATION_JSON));

        Map[] deposits = restClient.getBankDepositsFromToDateReturnDepositWithDepositors(dateFormat.parse("2015-01-01"),
                dateFormat.parse("2015-02-02"));

        Assert.isTrue(deposits.length == 2);

        assertEquals(1,deposits[0].get("depositId"));
        assertEquals(2,deposits[1].get("depositId"));

        assertEquals("depositName1",deposits[0].get("depositName"));
        assertEquals("depositName2",deposits[1].get("depositName"));

        assertEquals(2000,deposits[0].get("depositorAmountSum"));
        assertEquals(2000,deposits[1].get("depositorAmountSum"));
    }

    @Test
    public void getBankDepositsByCurrencyWithDepositorsTest(){
        mockServer.expect(requestTo(HOST+"/deposit/report/currency/usd"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                                "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                                "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                                "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}," +
                                "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12," +
                                "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                                "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                                "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}]",
                        MediaType.APPLICATION_JSON));

        Map[] deposits = restClient.getBankDepositsByCurrencyWithDepositors("usd");

        Assert.isTrue(deposits.length == 2);

        assertEquals(1,deposits[0].get("depositId"));
        assertEquals(2,deposits[1].get("depositId"));

        assertEquals("depositName1",deposits[0].get("depositName"));
        assertEquals("depositName2",deposits[1].get("depositName"));

        assertEquals(2000,deposits[0].get("depositorAmountSum"));
        assertEquals(2000,deposits[1].get("depositorAmountSum"));
    }

    @Test
    public void getBankDepositsByCurrencyFromToDateDepositWithDepositorsTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/deposit/report/currencyDate/usd,2015-01-01,2015-02-02"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                                "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                                "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                                "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}," +
                                "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12," +
                                "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                                "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                                "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}]",
                        MediaType.APPLICATION_JSON));

        Map[] deposits = restClient.getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd",
                dateFormat.parse("2015-01-01"),dateFormat.parse("2015-02-02"));

        Assert.isTrue(deposits.length == 2);

        assertEquals(1,deposits[0].get("depositId"));
        assertEquals(2,deposits[1].get("depositId"));

        assertEquals("depositName1",deposits[0].get("depositName"));
        assertEquals("depositName2",deposits[1].get("depositName"));

        assertEquals(2000,deposits[0].get("depositorAmountSum"));
        assertEquals(2000,deposits[1].get("depositorAmountSum"));
    }

    @Test
    public void getBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/deposit/report/currencyDateReturn/usd,2015-01-01,2015-02-02"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                                "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                                "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                                "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}," +
                                "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12," +
                                "\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                                "\"depositAddConditions\":\"condition1\",\"depositorCount\":2,\"depositorAmountSum\":2000," +
                                "\"depositorAmountPlusSum\":200,\"depositorAmountMinusSum\":200}]",
                        MediaType.APPLICATION_JSON));

        Map[] deposits = restClient.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd",
                dateFormat.parse("2015-01-01"),dateFormat.parse("2015-02-02"));

        Assert.isTrue(deposits.length == 2);

        assertEquals(1,deposits[0].get("depositId"));
        assertEquals(2,deposits[1].get("depositId"));

        assertEquals("depositName1",deposits[0].get("depositName"));
        assertEquals("depositName2",deposits[1].get("depositName"));

        assertEquals(2000,deposits[0].get("depositorAmountSum"));
        assertEquals(2000,deposits[1].get("depositorAmountSum"));
    }

    @Test
    public void addDepositTest(){
        mockServer.expect(requestTo(HOST+"/deposit"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockRestRequestMatchers.content()
                        .string("{\"depositId\":null,\"depositName\":\"depositName1\",\"depositMinTerm\":12,\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"conditions1\",\"depositors\":[]}"))
                .andRespond(withSuccess("",MediaType.APPLICATION_JSON));

        restClient.addDeposit(DataFixture.getNewDeposit());
    }

    @Test
    public void updateDepositTest(){
        mockServer.expect(requestTo(HOST+"/deposit"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.PUT))
                .andExpect(MockRestRequestMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockRestRequestMatchers.content()
                        .string("{\"depositId\":1,\"depositName\":\"update\",\"depositMinTerm\":12,\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"conditions1\",\"depositors\":[]}"))
                .andRespond(withSuccess("",MediaType.APPLICATION_JSON));

        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        deposit.setDepositName("update");
        restClient.updateDeposit(deposit);
    }

    @Test
    public void removeDepositTest(){
        mockServer.expect(requestTo(HOST+"/deposit/id/1"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                .andRespond(withSuccess("",MediaType.APPLICATION_JSON));

        restClient.removeDeposit(1L);
    }

    @Test
    public void getBankDepositorsTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/depositor/all"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositorId\":1,\"depositorName\":\"depositorName1\",\"depositorDateDeposit\":\"2015-01-01\",\"depositorAmountDeposit\":1000,\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100,\"depositorDateReturnDeposit\":\"2015-09-09\",\"depositorMarkReturnDeposit\":0}," +
                        "{\"depositorId\":2,\"depositorName\":\"depositorName2\",\"depositorDateDeposit\":\"2015-01-01\",\"depositorAmountDeposit\":1000,\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100,\"depositorDateReturnDeposit\":\"2015-09-09\",\"depositorMarkReturnDeposit\":0}," +
                        "{\"depositorId\":3,\"depositorName\":\"depositorName3\",\"depositorDateDeposit\":\"2015-01-01\",\"depositorAmountDeposit\":1000,\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100,\"depositorDateReturnDeposit\":\"2015-09-09\",\"depositorMarkReturnDeposit\":0}]",
                        MediaType.APPLICATION_JSON));

        BankDepositor[] depositors = restClient.getBankDepositors();

        Assert.isTrue(depositors.length == 3);
        assertNotNull(depositors[0]);
        assertNotNull(depositors[1]);
        assertNotNull(depositors[2]);


        assertNotNull(depositors[0].getDepositorId());
        assertEquals(new Long(1),depositors[0].getDepositorId());

        assertNotNull(depositors[0].getDepositorName());
        assertEquals("depositorName1",depositors[0].getDepositorName());

        assertNotNull(depositors[0].getDepositorDateDeposit());
        assertEquals("2015-01-01",dateFormat.format(depositors[0].getDepositorDateDeposit()));

        assertNotNull(depositors[0].getDepositorAmountDeposit());
        assertEquals(1000,depositors[0].getDepositorAmountDeposit());

        assertNotNull(depositors[0].getDepositorAmountPlusDeposit());
        assertEquals(100,depositors[0].getDepositorAmountPlusDeposit());

        assertNotNull(depositors[0].getDepositorAmountMinusDeposit());
        assertEquals(100,depositors[0].getDepositorAmountMinusDeposit());

        assertNotNull(depositors[0].getDepositorDateReturnDeposit());
        assertEquals("2015-09-09",dateFormat.format(depositors[0].getDepositorDateReturnDeposit()));

        assertNotNull(depositors[0].getDepositorMarkReturnDeposit());
        assertEquals(0,depositors[0].getDepositorMarkReturnDeposit());

        assertEquals(DataFixture.getExistDepositor(1L).toString(),depositors[0].toString());
        assertEquals(DataFixture.getExistDepositor(2L).toString(),depositors[1].toString());
        assertEquals(DataFixture.getExistDepositor(3L).toString(),depositors[2].toString());
    }

    @Test
    public void getBankDepositorsFromToDateDepositTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/depositor/allDate/2015-01-01,2015-02-02"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositorId\":1,\"depositorName\":\"depositorName1\",\"depositorDateDeposit\":\"2015-01-01\",\"depositorAmountDeposit\":1000,\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100,\"depositorDateReturnDeposit\":\"2015-09-09\",\"depositorMarkReturnDeposit\":0}," +
                                "{\"depositorId\":2,\"depositorName\":\"depositorName2\",\"depositorDateDeposit\":\"2015-01-01\",\"depositorAmountDeposit\":1000,\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100,\"depositorDateReturnDeposit\":\"2015-09-09\",\"depositorMarkReturnDeposit\":0}," +
                                "{\"depositorId\":3,\"depositorName\":\"depositorName3\",\"depositorDateDeposit\":\"2015-01-01\",\"depositorAmountDeposit\":1000,\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100,\"depositorDateReturnDeposit\":\"2015-09-09\",\"depositorMarkReturnDeposit\":0}]",
                        MediaType.APPLICATION_JSON));

        BankDepositor[] depositors = restClient
                .getBankDepositorsFromToDateDeposit(dateFormat.parse("2015-01-01"),dateFormat.parse("2015-02-02"));

        Assert.isTrue(depositors.length == 3);
        assertNotNull(depositors[0]);
        assertNotNull(depositors[1]);
        assertNotNull(depositors[2]);


        assertNotNull(depositors[0].getDepositorId());
        assertEquals(new Long(1),depositors[0].getDepositorId());

        assertNotNull(depositors[0].getDepositorName());
        assertEquals("depositorName1",depositors[0].getDepositorName());

        assertNotNull(depositors[0].getDepositorDateDeposit());
        assertEquals("2015-01-01",dateFormat.format(depositors[0].getDepositorDateDeposit()));

        assertNotNull(depositors[0].getDepositorAmountDeposit());
        assertEquals(1000,depositors[0].getDepositorAmountDeposit());

        assertNotNull(depositors[0].getDepositorAmountPlusDeposit());
        assertEquals(100,depositors[0].getDepositorAmountPlusDeposit());

        assertNotNull(depositors[0].getDepositorAmountMinusDeposit());
        assertEquals(100,depositors[0].getDepositorAmountMinusDeposit());

        assertNotNull(depositors[0].getDepositorDateReturnDeposit());
        assertEquals("2015-09-09",dateFormat.format(depositors[0].getDepositorDateReturnDeposit()));

        assertNotNull(depositors[0].getDepositorMarkReturnDeposit());
        assertEquals(0,depositors[0].getDepositorMarkReturnDeposit());

        assertEquals(DataFixture.getExistDepositor(1L).toString(),depositors[0].toString());
        assertEquals(DataFixture.getExistDepositor(2L).toString(),depositors[1].toString());
        assertEquals(DataFixture.getExistDepositor(3L).toString(),depositors[2].toString());
    }

    @Test
    public void getBankDepositorsFromToDateReturnDepositTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/depositor/allDateReturn/2015-01-01,2015-02-02"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositorId\":1,\"depositorName\":\"depositorName1\",\"depositorDateDeposit\":\"2015-01-01\",\"depositorAmountDeposit\":1000,\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100,\"depositorDateReturnDeposit\":\"2015-09-09\",\"depositorMarkReturnDeposit\":0}," +
                                "{\"depositorId\":2,\"depositorName\":\"depositorName2\",\"depositorDateDeposit\":\"2015-01-01\",\"depositorAmountDeposit\":1000,\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100,\"depositorDateReturnDeposit\":\"2015-09-09\",\"depositorMarkReturnDeposit\":0}," +
                                "{\"depositorId\":3,\"depositorName\":\"depositorName3\",\"depositorDateDeposit\":\"2015-01-01\",\"depositorAmountDeposit\":1000,\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100,\"depositorDateReturnDeposit\":\"2015-09-09\",\"depositorMarkReturnDeposit\":0}]",
                        MediaType.APPLICATION_JSON));

        BankDepositor[] depositors = restClient
                .getBankDepositorsFromToDateReturnDeposit(dateFormat.parse("2015-01-01"),dateFormat.parse("2015-02-02"));

        Assert.isTrue(depositors.length == 3);
        assertNotNull(depositors[0]);
        assertNotNull(depositors[1]);
        assertNotNull(depositors[2]);


        assertNotNull(depositors[0].getDepositorId());
        assertEquals(new Long(1),depositors[0].getDepositorId());

        assertNotNull(depositors[0].getDepositorName());
        assertEquals("depositorName1",depositors[0].getDepositorName());

        assertNotNull(depositors[0].getDepositorDateDeposit());
        assertEquals("2015-01-01",dateFormat.format(depositors[0].getDepositorDateDeposit()));

        assertNotNull(depositors[0].getDepositorAmountDeposit());
        assertEquals(1000,depositors[0].getDepositorAmountDeposit());

        assertNotNull(depositors[0].getDepositorAmountPlusDeposit());
        assertEquals(100,depositors[0].getDepositorAmountPlusDeposit());

        assertNotNull(depositors[0].getDepositorAmountMinusDeposit());
        assertEquals(100,depositors[0].getDepositorAmountMinusDeposit());

        assertNotNull(depositors[0].getDepositorDateReturnDeposit());
        assertEquals("2015-09-09",dateFormat.format(depositors[0].getDepositorDateReturnDeposit()));

        assertNotNull(depositors[0].getDepositorMarkReturnDeposit());
        assertEquals(0,depositors[0].getDepositorMarkReturnDeposit());

        assertEquals(DataFixture.getExistDepositor(1L).toString(),depositors[0].toString());
        assertEquals(DataFixture.getExistDepositor(2L).toString(),depositors[1].toString());
        assertEquals(DataFixture.getExistDepositor(3L).toString(),depositors[2].toString());
    }

    @Test
    public void getBankDepositorByIdTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/depositor/id/1"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("{\"depositorId\":1,\"depositorName\":\"depositorName1\",\"depositorDateDeposit\":\"2015-01-01\",\"depositorAmountDeposit\":1000,\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100,\"depositorDateReturnDeposit\":\"2015-09-09\",\"depositorMarkReturnDeposit\":0}",
                        MediaType.APPLICATION_JSON));

        BankDepositor depositor = restClient.getBankDepositorById(1L);

        assertEquals(DataFixture.getExistDepositor(1L).toString(),depositor.toString());
    }

    @Test
    public void getBankDepositorByIdDepositTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/depositor/idDeposit/1"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"depositorId\":1,\"depositorName\":\"depositorName1\",\"depositorDateDeposit\":\"2015-01-01\",\"depositorAmountDeposit\":1000,\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100,\"depositorDateReturnDeposit\":\"2015-09-09\",\"depositorMarkReturnDeposit\":0}," +
                                "{\"depositorId\":2,\"depositorName\":\"depositorName2\",\"depositorDateDeposit\":\"2015-01-01\",\"depositorAmountDeposit\":1000,\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100,\"depositorDateReturnDeposit\":\"2015-09-09\",\"depositorMarkReturnDeposit\":0}," +
                                "{\"depositorId\":3,\"depositorName\":\"depositorName3\",\"depositorDateDeposit\":\"2015-01-01\",\"depositorAmountDeposit\":1000,\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100,\"depositorDateReturnDeposit\":\"2015-09-09\",\"depositorMarkReturnDeposit\":0}]",
                        MediaType.APPLICATION_JSON));

        BankDepositor[] depositors = restClient.getBankDepositorByIdDeposit(1L);

        Assert.isTrue(depositors.length == 3);
        assertNotNull(depositors[0]);
        assertNotNull(depositors[1]);
        assertNotNull(depositors[2]);

        assertEquals(DataFixture.getExistDepositor(1L).toString(),depositors[0].toString());
        assertEquals(DataFixture.getExistDepositor(2L).toString(),depositors[1].toString());
        assertEquals(DataFixture.getExistDepositor(3L).toString(),depositors[2].toString());
    }

    @Test
    public void getBankDepositorByNameTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/depositor/name/depositorName1"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(withSuccess("{\"depositorId\":1,\"depositorName\":\"depositorName1\",\"depositorDateDeposit\":\"2015-01-01\",\"depositorAmountDeposit\":1000,\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100,\"depositorDateReturnDeposit\":\"2015-09-09\",\"depositorMarkReturnDeposit\":0}",
                        MediaType.APPLICATION_JSON));

        BankDepositor depositor = restClient.getBankDepositorByName("depositorName1");

        assertEquals(DataFixture.getExistDepositor(1L).toString(),depositor.toString());
    }

    @Test
    public void addBankDepositorTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/depositor/1"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockRestRequestMatchers.content()
                        .string("{\"depositorId\":null,\"depositorName\":\"depositorName1\",\"depositorDateDeposit\":1420059600000,\"depositorAmountDeposit\":1000,\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100,\"depositorDateReturnDeposit\":1441746000000,\"depositorMarkReturnDeposit\":0,\"depositId\":null,\"deposit\":null}"))
                .andRespond(withSuccess("",MediaType.APPLICATION_JSON));

        restClient.addBankDepositor(1L,DataFixture.getNewDepositor());
    }

    @Test
    public void updateBankDepositorTest() throws ParseException{
        mockServer.expect(requestTo(HOST+"/depositor"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.PUT))
                .andExpect(MockRestRequestMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockRestRequestMatchers.content()
                        .string("{\"depositorId\":1,\"depositorName\":\"update\",\"depositorDateDeposit\":1420059600000,\"depositorAmountDeposit\":1000,\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100,\"depositorDateReturnDeposit\":1441746000000,\"depositorMarkReturnDeposit\":0,\"depositId\":null,\"deposit\":null}"))
                .andRespond(withSuccess("",MediaType.APPLICATION_JSON));

        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        depositor.setDepositorName("update");
        restClient.updateBankDepositor(depositor);
    }

    @Test
    public void removeBankDepositorTest(){
        mockServer.expect(requestTo(HOST+"/depositor/1"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                .andRespond(withSuccess("",MediaType.APPLICATION_JSON));

        restClient.removeBankDepositor(1L);
    }
}

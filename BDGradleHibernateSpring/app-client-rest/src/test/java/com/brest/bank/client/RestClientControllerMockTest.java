package com.brest.bank.client;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.web.RestClientController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.easymock.Mock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

import static org.easymock.EasyMock.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-rest-client-controller-test.xml"})
public class RestClientControllerMockTest {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER = LogManager.getLogger();

    @Resource
    RestClientController restClientController;
    @Autowired
    RestClient restClient;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Before
    public void setUp(){
        this.mockMvc = standaloneSetup(restClientController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @After
    public void tearDown(){
        reset(restClient);
    }


    @Test
    public void testGetDeposits() throws Exception{
        expect(restClient.getBankDeposits()).andReturn(DataFixture.getExistDeposits());
        replay(restClient);

        this.mockMvc.perform(get("/deposit/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(log())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":1000,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"conditions1\",\"depositors\":[]}," +
                        "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions2\",\"depositors\":[]}]"));


        verify(restClient);
    }

    @Test
    public void testGetDepositById() throws Exception{
        expect(restClient.getDepositById(1L)).andReturn(DataFixture.getExistDeposit(1L));
        expectLastCall().once();
        replay(restClient);

        this.mockMvc.perform(get("/deposit/id/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(log())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions1\",\"depositors\":[]}"));

        verify(restClient);
    }

    @Test
    public void testGetDepositByName() throws Exception{
        expect(restClient.getDepositByName("depositName1")).andReturn(DataFixture.getExistDeposit(1L));
        replay(restClient);

        this.mockMvc.perform(get("/deposit/name/depositName1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions1\",\"depositors\":[]}"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositsByCurrency() throws Exception{
        expect(restClient.getBankDepositsByCurrency("usd")).andReturn(DataFixture.getExistDeposits());
        replay(restClient);

        this.mockMvc.perform(get("/deposit/currency/usd")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":1000,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"conditions1\",\"depositors\":[]}," +
                        "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions2\",\"depositors\":[]}]"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositsByInterestRate() throws Exception{
        expect(restClient.getBankDepositsByInterestRate(4)).andReturn(DataFixture.getExistDeposits());
        replay(restClient);

        this.mockMvc.perform(get("/deposit/rate/4")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":1000,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"conditions1\",\"depositors\":[]}," +
                        "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions2\",\"depositors\":[]}]"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositsFromToMinTerm() throws Exception{
        expect(restClient.getBankDepositsFromToMinTerm(11,12)).andReturn(DataFixture.getExistDeposits());
        replay(restClient);

        this.mockMvc.perform(get("/deposit/term/11,12")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":1000,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"conditions1\",\"depositors\":[]}," +
                        "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions2\",\"depositors\":[]}]"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositsFromToInterestRate() throws Exception{
        expect(restClient.getBankDepositsFromToInterestRate(4,5)).andReturn(DataFixture.getExistDeposits());
        replay(restClient);

        this.mockMvc.perform(get("/deposit/rateBetween/4,5")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":1000,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"conditions1\",\"depositors\":[]}," +
                        "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions2\",\"depositors\":[]}]"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositsFromToDateDeposit() throws Exception{
        expect(restClient.getBankDepositsFromToDateDeposit("2016-01-01","2017-01-01"))
                .andReturn(DataFixture.getExistDeposits());
        replay(restClient);

        this.mockMvc.perform(get("/deposit/date/2016-01-01,2017-01-01")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":1000,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"conditions1\",\"depositors\":[]}," +
                        "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions2\",\"depositors\":[]}]"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositsFromToDateReturnDeposit() throws Exception{
        expect(restClient.getBankDepositsFromToDateReturnDeposit("2015-01-01","2015-02-02"))
                .andReturn(DataFixture.getExistDeposits());
        replay(restClient);

        this.mockMvc.perform(get("/deposit/returnDate/2015-01-01,2015-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":1000,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"conditions1\",\"depositors\":[]}," +
                        "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions2\",\"depositors\":[]}]"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositByNameWithDepositors() throws Exception{
        expect(restClient.getBankDepositByNameWithDepositors("depositName1"))
                .andReturn(DataFixture.getExistDepositAllDepositors(1L, 1L));
        replay(restClient);

        this.mockMvc.perform(get("/deposit/report/name/depositName1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":1000,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"conditions1\"," +
                        "\"depositorCount\":1,\"depositorAmountSum\":1000,\"depositorAmountPlusSum\":100," +
                        "\"depositorAmountMinusSum\":100}"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositByNameFromToDateDepositWithDepositors() throws Exception{
        expect(restClient.getBankDepositByNameFromToDateDepositWithDepositors("depositName1",
                "2016-01-01","2017-02-02"))
                .andReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        replay(restClient);

        this.mockMvc.perform(get("/deposit/report/nameDate/depositName1,2016-01-01,2017-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":1000,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"conditions1\"," +
                        "\"depositorCount\":1,\"depositorAmountSum\":1000,\"depositorAmountPlusSum\":100," +
                        "\"depositorAmountMinusSum\":100}"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositByNameFromToDateReturnDepositWithDepositors() throws Exception{
        expect(restClient.getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1",
                "2015-01-01","2015-02-02"))
                .andReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        replay(restClient);

        this.mockMvc.perform(get("/deposit/report/nameDateReturn/depositName1,2015-01-01,2015-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":1000,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"conditions1\"," +
                        "\"depositorCount\":1,\"depositorAmountSum\":1000,\"depositorAmountPlusSum\":100," +
                        "\"depositorAmountMinusSum\":100}"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositByIdWithDepositors() throws Exception{
        expect(restClient.getBankDepositByIdWithDepositors(1L))
                .andReturn(DataFixture.getExistDepositAllDepositors(1L, 1L));
        replay(restClient);

        this.mockMvc.perform(get("/deposit/report/id/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":1000,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"conditions1\"," +
                        "\"depositorCount\":1,\"depositorAmountSum\":1000,\"depositorAmountPlusSum\":100," +
                        "\"depositorAmountMinusSum\":100}"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositByIdFromToDateDepositWithDepositors() throws Exception{
        expect(restClient.getBankDepositByIdFromToDateDepositWithDepositors(1L,
                "2015-01-01","2015-02-02"))
                .andReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        replay(restClient);

        this.mockMvc.perform(get("/deposit/report/idDate/1,2015-01-01,2015-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":1000,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"conditions1\"," +
                        "\"depositorCount\":1,\"depositorAmountSum\":1000,\"depositorAmountPlusSum\":100," +
                        "\"depositorAmountMinusSum\":100}"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositByIdFromToDateReturnDepositWithDepositors() throws Exception{
        expect(restClient.getBankDepositByIdFromToDateReturnDepositWithDepositors(1L,
                "2015-01-01", "2015-02-02"))
                .andReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        replay(restClient);

        this.mockMvc.perform(get("/deposit/report/idDateReturn/1,2015-01-01,2015-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":1000,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"conditions1\"," +
                        "\"depositorCount\":1,\"depositorAmountSum\":1000,\"depositorAmountPlusSum\":100," +
                        "\"depositorAmountMinusSum\":100}"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositsWithDepositors() throws Exception{
        expect(restClient.getBankDepositsWithDepositors())
                .andReturn(DataFixture.getExistDepositsWithDepositors());
        replay(restClient);

        this.mockMvc.perform(get("/deposit/report/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"," +
                        "\"depositorCount\":2,\"depositorAmountSum\":2000,\"depositorAmountPlusSum\":200," +
                        "\"depositorAmountMinusSum\":200},{\"depositId\":2,\"depositName\":\"depositName2\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"condition2\"," +
                        "\"depositorCount\":2,\"depositorAmountSum\":2000,\"depositorAmountPlusSum\":200," +
                        "\"depositorAmountMinusSum\":200}]"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositsFromToDateDepositWithDepositors() throws Exception{
        expect(restClient.getBankDepositsFromToDateDepositWithDepositors(
                "2015-01-01","2015-02-02"))
                .andReturn(DataFixture.getExistDepositsWithDepositors());
        replay(restClient);

        this.mockMvc.perform(get("/deposit/report/allDate/2015-01-01,2015-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"," +
                        "\"depositorCount\":2,\"depositorAmountSum\":2000,\"depositorAmountPlusSum\":200," +
                        "\"depositorAmountMinusSum\":200},{\"depositId\":2,\"depositName\":\"depositName2\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"condition2\"," +
                        "\"depositorCount\":2,\"depositorAmountSum\":2000,\"depositorAmountPlusSum\":200," +
                        "\"depositorAmountMinusSum\":200}]"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositsFromToDateReturnDepositWithDepositors() throws Exception{
        expect(restClient.getBankDepositsFromToDateReturnDepositWithDepositors(
                "2016-01-01", "2017-02-02"))
                .andReturn(DataFixture.getExistDepositsWithDepositors());
        replay(restClient);

        this.mockMvc.perform(get("/deposit/report/allDateReturn/2016-01-01,2017-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"," +
                        "\"depositorCount\":2,\"depositorAmountSum\":2000,\"depositorAmountPlusSum\":200," +
                        "\"depositorAmountMinusSum\":200},{\"depositId\":2,\"depositName\":\"depositName2\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"condition2\"," +
                        "\"depositorCount\":2,\"depositorAmountSum\":2000,\"depositorAmountPlusSum\":200," +
                        "\"depositorAmountMinusSum\":200}]"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositsByCurrencyWithDepositors() throws Exception{
        expect(restClient.getBankDepositsByCurrencyWithDepositors("usd"))
                .andReturn(DataFixture.getExistDepositsWithDepositors());
        replay(restClient);

        this.mockMvc.perform(get("/deposit/report/currency/usd")
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"," +
                        "\"depositorCount\":2,\"depositorAmountSum\":2000,\"depositorAmountPlusSum\":200," +
                        "\"depositorAmountMinusSum\":200},{\"depositId\":2,\"depositName\":\"depositName2\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"condition2\"," +
                        "\"depositorCount\":2,\"depositorAmountSum\":2000,\"depositorAmountPlusSum\":200," +
                        "\"depositorAmountMinusSum\":200}]"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositsByCurrencyFromToDateDepositWithDepositors() throws Exception{
        expect(restClient.getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd",
                "2016-01-01","2017-02-02"))
                .andReturn(DataFixture.getExistDepositsWithDepositors());
        replay(restClient);

        this.mockMvc.perform(get("/deposit/report/currencyDate/usd,2016-01-01,2017-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"," +
                        "\"depositorCount\":2,\"depositorAmountSum\":2000,\"depositorAmountPlusSum\":200," +
                        "\"depositorAmountMinusSum\":200},{\"depositId\":2,\"depositName\":\"depositName2\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"condition2\"," +
                        "\"depositorCount\":2,\"depositorAmountSum\":2000,\"depositorAmountPlusSum\":200," +
                        "\"depositorAmountMinusSum\":200}]"));

        verify(restClient);
    }

    @Test
    public void testGetBankDepositsByCurrencyFromToDateReturnDepositWithDepositors() throws Exception{
        expect(restClient.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd",
                "2016-01-01", "2017-02-02"))
                .andReturn(DataFixture.getExistDepositsWithDepositors());
        replay(restClient);

        this.mockMvc.perform(get("/deposit/report/currencyDateReturn/usd,2016-01-01,2017-02-02")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"," +
                        "\"depositorCount\":2,\"depositorAmountSum\":2000,\"depositorAmountPlusSum\":200," +
                        "\"depositorAmountMinusSum\":200},{\"depositId\":2,\"depositName\":\"depositName2\"," +
                        "\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositInterestRate\":4,\"depositAddConditions\":\"condition2\"," +
                        "\"depositorCount\":2,\"depositorAmountSum\":2000,\"depositorAmountPlusSum\":200," +
                        "\"depositorAmountMinusSum\":200}]"));

        verify(restClient);
    }

    @Test
    public void testAddDeposit() throws Exception{
        BankDeposit deposit = DataFixture.getNewDeposit();
        expect(restClient.addDeposit(anyObject(BankDeposit.class))).andReturn(anyString());

        objectMapper = new ObjectMapper();
        String depositJson = objectMapper.writeValueAsString(deposit);

        ResultActions result = this.mockMvc.perform(post("/deposit/")
                    .content(depositJson)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("\"a bank deposit created\""));

    }

    @Test
    public void testAddExistDeposit() throws Exception{
        restClient.addDeposit(anyObject(BankDeposit.class));
        expectLastCall().andThrow(new IllegalArgumentException("Bank Deposit is present in DB"));
        replay(restClient);

        objectMapper = new ObjectMapper();
        String existDeposit = objectMapper.writeValueAsString(DataFixture.getExistDeposit(1L));

        this.mockMvc.perform(post("/deposit/")
                    .content(existDeposit)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotModified())
                .andExpect(content().string("\"Bank Deposit is present in DB\""));

        verify(restClient);
    }

    @Test
    public void testAddEmptyDeposit() throws Exception{
        objectMapper = new ObjectMapper();
        String emptyDeposit = objectMapper.writeValueAsString(new BankDeposit(null,null,0,0,null,0,null,null));

        this.mockMvc.perform(post("/deposit/")
                    .content(emptyDeposit)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotModified())
                .andExpect(content().string("\"Can not be added to the database Empty deposit\""));
    }

    @Test
    public void testUpdateDeposit() throws Exception{
        restClient.updateDeposit(anyObject(BankDeposit.class));
        expectLastCall();
        replay(restClient);

        objectMapper = new ObjectMapper();
        String depositJson = objectMapper.writeValueAsString(DataFixture.getExistDeposit(1L));

        ResultActions result = this.mockMvc.perform(put("/deposit/")
                .content(depositJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("\"a bank Deposit updated\""));

        verify(restClient);
    }

    @Test
    public void testUpdateEmptyDeposit() throws Exception{
        objectMapper = new ObjectMapper();
        String emptyDeposit = objectMapper.writeValueAsString(new BankDeposit(null,null,0,0,null,0,null,null));

        ResultActions result = this.mockMvc.perform(put("/deposit/")
                .content(emptyDeposit)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isNotModified())
                .andExpect(content().string("\"Can not be updated Empty deposit\""));
    }

    @Test
    public void tesRemoveDeposit() throws Exception{
        restClient.removeDeposit(1L);
        expectLastCall();
        replay(restClient);

        ResultActions result = this.mockMvc.perform(delete("/deposit/id/1")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("\"a bank Deposit removed\""));

        verify(restClient);
    }

    @Test
    public void testRemoveNullIdDeposit() throws Exception{

        this.mockMvc.perform(delete("/deposit/id/null")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRemoveErrorIdDeposit() throws Exception{

        this.mockMvc.perform(delete("/deposit/id/-1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Id Deposit - incorrect\""));
    }

}

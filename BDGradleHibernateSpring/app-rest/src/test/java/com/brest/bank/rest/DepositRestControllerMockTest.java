package com.brest.bank.rest;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.service.BankDepositService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.easymock.EasyMock.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-rest-mock-test.xml"})
public class DepositRestControllerMockTest {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Resource
    DepositRestController depositRestController;
    @Autowired
    BankDepositService depositService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Before
    public void setUp(){
        this.mockMvc = standaloneSetup(depositRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @After
    public void tearDown(){
        reset(depositService);
    }

    @Test
    public void testGetDeposits() throws Exception{
        expect(depositService.getBankDeposits()).andReturn(DataFixture.getExistDeposits());
        replay(depositService);

        this.mockMvc.perform(get("/deposit/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions1\",\"depositors\":[]}]"));

        verify(depositService);
    }

    @Test
    public void testGetDepositById() throws Exception{
        expect(depositService.getBankDepositById(1L)).andReturn(DataFixture.getExistDeposit(1L));
        replay(depositService);

        this.mockMvc.perform(get("/deposit/id/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions1\",\"depositors\":[]}"));

        verify(depositService);
    }

    @Test
    public void testGetDepositByName() throws Exception{
        expect(depositService.getBankDepositByName("depositName1")).andReturn(DataFixture.getExistDeposit(1L));
        replay(depositService);

        this.mockMvc.perform(get("/deposit/name/depositName1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions1\",\"depositors\":[]}"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsByCurrency() throws Exception{
        expect(depositService.getBankDepositsByCurrency("usd")).andReturn(DataFixture.getExistDeposits());
        replay(depositService);

        this.mockMvc.perform(get("/deposit/currency/usd")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions1\",\"depositors\":[]}]"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsByInterestRate() throws Exception{
        expect(depositService.getBankDepositsByInterestRate(4)).andReturn(DataFixture.getExistDeposits());
        replay(depositService);

        this.mockMvc.perform(get("/deposit/rate/4")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions1\",\"depositors\":[]}]"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsFromToMinTerm() throws Exception{
        expect(depositService.getBankDepositsFromToMinTerm(11,12)).andReturn(DataFixture.getExistDeposits());
        replay(depositService);

        this.mockMvc.perform(get("/deposit/term/11,12")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions1\",\"depositors\":[]}]"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsFromToInterestRate() throws Exception{
        expect(depositService.getBankDepositsFromToInterestRate(4,5)).andReturn(DataFixture.getExistDeposits());
        replay(depositService);

        this.mockMvc.perform(get("/deposit/rateBetween/4,5")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions1\",\"depositors\":[]}]"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsFromToDateDeposit() throws Exception{
        expect(depositService.getBankDepositsFromToDateDeposit(dateFormat.parse("2015-01-01"),
                dateFormat.parse("2015-02-02"))).andReturn(DataFixture.getExistDeposits());
        replay(depositService);

        this.mockMvc.perform(get("/deposit/date/2015-01-01,2015-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions1\",\"depositors\":[]}]"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsFromToDateReturnDeposit() throws Exception{
        expect(depositService.getBankDepositsFromToDateReturnDeposit(dateFormat.parse("2015-01-01"),
                dateFormat.parse("2015-02-02"))).andReturn(DataFixture.getExistDeposits());
        replay(depositService);

        this.mockMvc.perform(get("/deposit/returnDate/2015-01-01,2015-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions1\",\"depositors\":[]}]"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositByNameWithDepositors() throws Exception{
        expect(depositService.getBankDepositByNameWithDepositors("depositName1"))
                .andReturn(DataFixture.getExistDepositAllDepositors(1L, 1L));
        replay(depositService);

        this.mockMvc.perform(get("/deposit/report/name/depositName1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositMinTerm\":12,\"depositInterestRate\":4," +
                        "\"depositName\":\"depositName1\",\"sumMinusAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositId\":1,\"sumPlusAmount\":100,\"depositMinAmount\":1000,\"numDepositors\":1," +
                        "\"sumAmount\":1000,\"depositAddConditions\":\"conditions1\"}"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositByNameFromToDateDepositWithDepositors() throws Exception{
        expect(depositService.getBankDepositByNameFromToDateDepositWithDepositors("depositName1",
                dateFormat.parse("2015-01-01"),dateFormat.parse("2015-02-02")))
                .andReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        replay(depositService);

        this.mockMvc.perform(get("/deposit/report/nameDate/depositName1,2015-01-01,2015-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositMinTerm\":12,\"depositInterestRate\":4," +
                        "\"depositName\":\"depositName1\",\"sumMinusAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositId\":1,\"sumPlusAmount\":100,\"depositMinAmount\":1000,\"numDepositors\":1," +
                        "\"sumAmount\":1000,\"depositAddConditions\":\"conditions1\"}"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositByNameFromToDateReturnDepositWithDepositors() throws Exception{
        expect(depositService.getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1",
                dateFormat.parse("2015-01-01"), dateFormat.parse("2015-02-02")))
                .andReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        replay(depositService);

        this.mockMvc.perform(get("/deposit/report/nameDateReturn/depositName1,2015-01-01,2015-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositMinTerm\":12,\"depositInterestRate\":4," +
                        "\"depositName\":\"depositName1\",\"sumMinusAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositId\":1,\"sumPlusAmount\":100,\"depositMinAmount\":1000,\"numDepositors\":1," +
                        "\"sumAmount\":1000,\"depositAddConditions\":\"conditions1\"}"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositByIdWithDepositors() throws Exception{
        expect(depositService.getBankDepositByIdWithDepositors(1L))
                .andReturn(DataFixture.getExistDepositAllDepositors(1L, 1L));
        replay(depositService);

        this.mockMvc.perform(get("/deposit/report/id/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositMinTerm\":12,\"depositInterestRate\":4," +
                        "\"depositName\":\"depositName1\",\"sumMinusAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositId\":1,\"sumPlusAmount\":100,\"depositMinAmount\":1000,\"numDepositors\":1," +
                        "\"sumAmount\":1000,\"depositAddConditions\":\"conditions1\"}"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositByIdFromToDateDepositWithDepositors() throws Exception{
        expect(depositService.getBankDepositByIdFromToDateDepositWithDepositors(1L,
                dateFormat.parse("2015-01-01"),dateFormat.parse("2015-02-02")))
                .andReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        replay(depositService);

        this.mockMvc.perform(get("/deposit/report/idDate/1,2015-01-01,2015-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositMinTerm\":12,\"depositInterestRate\":4," +
                        "\"depositName\":\"depositName1\",\"sumMinusAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositId\":1,\"sumPlusAmount\":100,\"depositMinAmount\":1000,\"numDepositors\":1," +
                        "\"sumAmount\":1000,\"depositAddConditions\":\"conditions1\"}"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositByIdFromToDateReturnDepositWithDepositors() throws Exception{
        expect(depositService.getBankDepositByIdFromToDateReturnDepositWithDepositors(1L,
                dateFormat.parse("2015-01-01"), dateFormat.parse("2015-02-02")))
                .andReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        replay(depositService);

        this.mockMvc.perform(get("/deposit/report/idDateReturn/1,2015-01-01,2015-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositMinTerm\":12,\"depositInterestRate\":4," +
                        "\"depositName\":\"depositName1\",\"sumMinusAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositId\":1,\"sumPlusAmount\":100,\"depositMinAmount\":1000,\"numDepositors\":1," +
                        "\"sumAmount\":1000,\"depositAddConditions\":\"conditions1\"}"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsWithDepositors() throws Exception{
        expect(depositService.getBankDepositsWithDepositors())
                .andReturn(DataFixture.getExistAllDepositsAllDepositors());
        replay(depositService);

        this.mockMvc.perform(get("/deposit/report/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositMinTerm\":12,\"depositInterestRate\":4," +
                        "\"depositName\":\"depositName1\",\"sumMinusAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositId\":1,\"sumPlusAmount\":100,\"depositMinAmount\":1000,\"numDepositors\":1," +
                        "\"sumAmount\":1000,\"depositAddConditions\":\"conditions1\"}]"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsFromToDateDepositWithDepositors() throws Exception{
        expect(depositService.getBankDepositsFromToDateDepositWithDepositors(
                dateFormat.parse("2015-01-01"),dateFormat.parse("2015-02-02")))
                .andReturn(DataFixture.getExistAllDepositsAllDepositors());
        replay(depositService);

        this.mockMvc.perform(get("/deposit/report/allDate/2015-01-01,2015-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositMinTerm\":12,\"depositInterestRate\":4," +
                        "\"depositName\":\"depositName1\",\"sumMinusAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositId\":1,\"sumPlusAmount\":100,\"depositMinAmount\":1000,\"numDepositors\":1," +
                        "\"sumAmount\":1000,\"depositAddConditions\":\"conditions1\"}]"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsFromToDateReturnDepositWithDepositors() throws Exception{
        expect(depositService.getBankDepositsFromToDateReturnDepositWithDepositors(
                dateFormat.parse("2015-01-01"), dateFormat.parse("2015-02-02")))
                .andReturn(DataFixture.getExistAllDepositsAllDepositors());
        replay(depositService);

        this.mockMvc.perform(get("/deposit/report/allDateReturn/2015-01-01,2015-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositMinTerm\":12,\"depositInterestRate\":4," +
                        "\"depositName\":\"depositName1\",\"sumMinusAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositId\":1,\"sumPlusAmount\":100,\"depositMinAmount\":1000,\"numDepositors\":1," +
                        "\"sumAmount\":1000,\"depositAddConditions\":\"conditions1\"}]"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsByCurrencyWithDepositors() throws Exception{
        expect(depositService.getBankDepositsByCurrencyWithDepositors("usd"))
                .andReturn(DataFixture.getExistAllDepositsAllDepositors());
        replay(depositService);

        this.mockMvc.perform(get("/deposit/report/currency/usd")
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositMinTerm\":12,\"depositInterestRate\":4," +
                        "\"depositName\":\"depositName1\",\"sumMinusAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositId\":1,\"sumPlusAmount\":100,\"depositMinAmount\":1000,\"numDepositors\":1," +
                        "\"sumAmount\":1000,\"depositAddConditions\":\"conditions1\"}]"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsByCurrencyFromToDateDepositWithDepositors() throws Exception{
        expect(depositService.getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd",
                dateFormat.parse("2015-01-01"),dateFormat.parse("2015-02-02")))
                .andReturn(DataFixture.getExistAllDepositsAllDepositors());
        replay(depositService);

        this.mockMvc.perform(get("/deposit/report/currencyDate/usd,2015-01-01,2015-02-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositMinTerm\":12,\"depositInterestRate\":4," +
                        "\"depositName\":\"depositName1\",\"sumMinusAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositId\":1,\"sumPlusAmount\":100,\"depositMinAmount\":1000,\"numDepositors\":1," +
                        "\"sumAmount\":1000,\"depositAddConditions\":\"conditions1\"}]"));

        verify(depositService);
    }

    @Test
    public void testGetBankDepositsByCurrencyFromToDateReturnDepositWithDepositors() throws Exception{
        expect(depositService.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd",
                dateFormat.parse("2015-01-01"), dateFormat.parse("2015-02-02")))
                .andReturn(DataFixture.getExistAllDepositsAllDepositors());
        replay(depositService);

        this.mockMvc.perform(get("/deposit/report/currencyDateReturn/usd,2015-01-01,2015-02-02")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositMinTerm\":12,\"depositInterestRate\":4," +
                        "\"depositName\":\"depositName1\",\"sumMinusAmount\":100,\"depositCurrency\":\"usd\"," +
                        "\"depositId\":1,\"sumPlusAmount\":100,\"depositMinAmount\":1000,\"numDepositors\":1," +
                        "\"sumAmount\":1000,\"depositAddConditions\":\"conditions1\"}]"));

        verify(depositService);
    }

    @Test
    public void testAddDeposit() throws Exception{
        depositService.addBankDeposit(anyObject(BankDeposit.class));
        expectLastCall();
        replay(depositService);

        objectMapper = new ObjectMapper();
        String depositJson = objectMapper.writeValueAsString(DataFixture.getNewDeposit());

        this.mockMvc.perform(post("/deposit/")
                    .content(depositJson)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("\"a bank deposit created\""));

        verify(depositService);
    }

    @Test
    public void testAddExistDeposit() throws Exception{
        depositService.addBankDeposit(anyObject(BankDeposit.class));
        expectLastCall().andThrow(new IllegalArgumentException("Bank Deposit is present in DB"));
        replay(depositService);

        objectMapper = new ObjectMapper();
        String existDeposit = objectMapper.writeValueAsString(DataFixture.getExistDeposit(1L));

        this.mockMvc.perform(post("/deposit/")
                    .content(existDeposit)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotModified())
                .andExpect(content().string("\"Bank Deposit is present in DB\""));

        verify(depositService);
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
        depositService.updateBankDeposit(anyObject(BankDeposit.class));
        expectLastCall();
        replay(depositService);

        objectMapper = new ObjectMapper();
        String depositJson = objectMapper.writeValueAsString(DataFixture.getExistDeposit(1L));

        ResultActions result = this.mockMvc.perform(put("/deposit/")
                .content(depositJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("\"a bank Deposit updated\""));

        verify(depositService);
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
        depositService.deleteBankDeposit(1L);
        expectLastCall();
        replay(depositService);

        ResultActions result = this.mockMvc.perform(delete("/deposit/id/1")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("\"a bank Deposit removed\""));

        verify(depositService);
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

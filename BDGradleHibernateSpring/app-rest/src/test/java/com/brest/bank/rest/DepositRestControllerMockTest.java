package com.brest.bank.rest;

import com.brest.bank.service.BankDepositService;

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

import static org.easymock.EasyMock.*;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-rest-mock-test.xml"})
public class DepositRestControllerMockTest {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private MockMvc mockMvc;

    @Resource
    DepositRestController depositRestController;

    @Autowired
    BankDepositService depositService;

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
}

package com.brest.bank.rest;

import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositorService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-rest-mock-test.xml"})
public class DepositorRestControllerMockTest {

    private static final Logger LOGGER = LogManager.getLogger();
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    @Resource
    DepositorRestController depositorRestController;

    @Autowired
    BankDepositorService depositorService;

    @Before
    public void setUp(){
        this.mockMvc = standaloneSetup(depositorRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @After
    public void tearDown(){
        reset(depositorService);
    }

    @Test
    public void testGetBankDepositors() throws Exception{
        expect(depositorService.getBankDepositors()).andReturn(DataFixture.getExistDepositors());
        replay(depositorService);

        this.mockMvc.perform(get("/depositor/all")
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositorId\":1,\"depositorName\":\"depositorName1\"," +
                        "\"depositorDateDeposit\":1420059600000,\"depositorAmountDeposit\":1000," +
                        "\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100," +
                        "\"depositorDateReturnDeposit\":1441746000000,\"depositorMarkReturnDeposit\":0," +
                        "\"depositId\":null,\"deposit\":null}," +
                        "{\"depositorId\":2,\"depositorName\":\"depositorName1\"," +
                        "\"depositorDateDeposit\":1420059600000,\"depositorAmountDeposit\":1000," +
                        "\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100," +
                        "\"depositorDateReturnDeposit\":1441746000000,\"depositorMarkReturnDeposit\":0," +
                        "\"depositId\":null,\"deposit\":null}]"));

        verify(depositorService);
    }

    @Test
    public void testGetBankDepositorsFromToDateDeposit() throws Exception{
        expect(depositorService.getBankDepositorsFromToDateDeposit(dateFormat.parse("2015-01-01"),
                dateFormat.parse("2015-02-02"))).andReturn(DataFixture.getExistDepositors());
        replay(depositorService);

        this.mockMvc.perform(get("/depositor/allDate/2015-01-01,2015-02-02")
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("[{\"depositorId\":1,\"depositorName\":\"depositorName1\"," +
                        "\"depositorDateDeposit\":1420059600000,\"depositorAmountDeposit\":1000," +
                        "\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100," +
                        "\"depositorDateReturnDeposit\":1441746000000,\"depositorMarkReturnDeposit\":0," +
                        "\"depositId\":null,\"deposit\":null}," +
                        "{\"depositorId\":2,\"depositorName\":\"depositorName1\"," +
                        "\"depositorDateDeposit\":1420059600000,\"depositorAmountDeposit\":1000," +
                        "\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100," +
                        "\"depositorDateReturnDeposit\":1441746000000,\"depositorMarkReturnDeposit\":0," +
                        "\"depositId\":null,\"deposit\":null}]"));

        verify(depositorService);
    }

    @Test
    public void testGetBankDepositorById() throws Exception{
        expect(depositorService.getBankDepositorById(1L)).andReturn(DataFixture.getExistDepositor(1L));
        replay(depositorService);

        this.mockMvc.perform(get("/depositor/id/1")
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositorId\":1,\"depositorName\":\"depositorName1\"," +
                        "\"depositorDateDeposit\":1420059600000,\"depositorAmountDeposit\":1000," +
                        "\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100," +
                        "\"depositorDateReturnDeposit\":1441746000000,\"depositorMarkReturnDeposit\":0," +
                        "\"depositId\":null,\"deposit\":null}"));

        verify(depositorService);
    }

    @Test
    public void testGetBankDepositorByName() throws Exception{
        expect(depositorService.getBankDepositorByName("depositorName1"))
                .andReturn(DataFixture.getExistDepositor(1L));
        replay(depositorService);

        this.mockMvc.perform(get("/depositor/name/depositorName1")
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("{\"depositorId\":1,\"depositorName\":\"depositorName1\"," +
                        "\"depositorDateDeposit\":1420059600000,\"depositorAmountDeposit\":1000," +
                        "\"depositorAmountPlusDeposit\":100,\"depositorAmountMinusDeposit\":100," +
                        "\"depositorDateReturnDeposit\":1441746000000,\"depositorMarkReturnDeposit\":0," +
                        "\"depositId\":null,\"deposit\":null}"));

        verify(depositorService);
    }

    @Test
    public void testAddBankDepositor() throws Exception{
        depositorService.addBankDepositor(anyLong(),anyObject(BankDepositor.class));
        expectLastCall();
        replay(depositorService);

        ObjectMapper objectMapper = new ObjectMapper();
        String depositorJson = objectMapper.writeValueAsString(DataFixture.getNewDepositor());

        this.mockMvc.perform(
                post("/depositor/1")
                        .content(depositorJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("\"a bank depositor created\""));
        verify(depositorService);
    }

    @Test
    public void addExistBankDepositorRestTest() throws Exception {
        depositorService.addBankDepositor(anyLong(),anyObject(BankDepositor.class));
        expectLastCall().andThrow(new IllegalArgumentException("Depositor already EXISTS in the database"));
        replay(depositorService);

        ObjectMapper objectMapper = new ObjectMapper();
        String depositorJson = objectMapper.writeValueAsString(DataFixture.getExistDepositor(1L));

        this.mockMvc.perform(
                post("/depositor/1")
                        .content(depositorJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Depositor already EXISTS in the database\""));
        verify(depositorService);
    }

    @Test
    public void addEmptyBankDepositorRestTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String depositorJson = objectMapper.writeValueAsString(DataFixture.getEmptyDepositor());
        LOGGER.debug("depositorJson: {}",depositorJson);

        this.mockMvc.perform(
                post("/depositor/1")
                        .content(depositorJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Can not be added to the database Empty depositor\""));
    }

    @Test
    public void addBankDepositorErrorDepositIdRestTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String depositorJson = objectMapper.writeValueAsString(DataFixture.getNewDepositor());

        this.mockMvc.perform(
                post("/depositor/-1")
                        .content(depositorJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Id Deposit - incorrect\""));
    }

    @Test
    public void testUpdateDepositor() throws Exception{
        depositorService.updateBankDepositor(anyObject(BankDepositor.class));
        expectLastCall();
        replay(depositorService);

        objectMapper = new ObjectMapper();
        String depositorJson = objectMapper.writeValueAsString(DataFixture.getExistDepositor(1L));

        ResultActions result = this.mockMvc.perform(put("/depositor/")
                .content(depositorJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("\"a bank depositor updated\""));

        verify(depositorService);
    }

    @Test
    public void testUpdateEmptyDepositor() throws Exception{

        objectMapper = new ObjectMapper();
        String depositorJson = objectMapper.writeValueAsString(DataFixture.getEmptyDepositor());

        ResultActions result = this.mockMvc.perform(put("/depositor/")
                .content(depositorJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isNotModified())
                .andExpect(content().string("\"Can not be updated Empty depositor\""));
    }

    @Test
    public void testUpdateNotExistDepositor() throws Exception{
        depositorService.updateBankDepositor(anyObject(BankDepositor.class));
        expectLastCall().andThrow(new IllegalArgumentException("The parameter can not be NULL - depositorId"));
        replay(depositorService);

        objectMapper = new ObjectMapper();
        String depositorJson = objectMapper.writeValueAsString(DataFixture.getNewDepositor());

        ResultActions result = this.mockMvc.perform(put("/depositor/")
                .content(depositorJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isNotModified())
                .andExpect(content().string("\"The parameter can not be NULL - depositorId\""));

        verify(depositorService);
    }

    @Test
    public void testRemoveBankDepositor() throws Exception{
        depositorService.removeBankDepositor(1L);
        expectLastCall();
        replay(depositorService);

        ResultActions result = this.mockMvc.perform(delete("/depositor/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("\"a bank depositor removed\""));

        verify(depositorService);
    }

    @Test
    public void testRemoveBankDepositorErrorIdRestTest() throws Exception {

        ResultActions result = this.mockMvc.perform(
                delete("/depositor/-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        result.andExpect(status().isBadRequest())
                .andExpect(content().string("\"Id Depositor - incorrect\""));
    }

    @Test
    public void testRemoveBankDepositorNotExist() throws Exception{
        depositorService.removeBankDepositor(1L);
        expectLastCall().andThrow(new IllegalArgumentException("In the database there is no Depositor with such parameters"));
        replay(depositorService);

        ResultActions result = this.mockMvc.perform(delete("/depositor/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"In the database there is no Depositor with such parameters\""));

        verify(depositorService);
    }
}

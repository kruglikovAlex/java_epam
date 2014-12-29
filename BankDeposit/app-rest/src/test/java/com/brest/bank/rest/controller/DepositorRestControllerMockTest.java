package com.brest.bank.rest.controller;

import com.brest.bank.domain.BankDepositor;
import com.brest.bank.rest.DepositorRestController;
import com.brest.bank.rest.exception.NotFoundException;
import com.brest.bank.service.BankDepositorService;
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

import javax.annotation.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-rest-mock-test.xml"})

public class DepositorRestControllerMockTest {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private MockMvc mockMvc;

    @Resource
    private DepositorRestController depositorRestController;

    @Autowired
    private BankDepositorService depositorService;

    @Before
    public void setUp() {
        this.mockMvc = standaloneSetup(depositorRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @After
    public void tearDown() throws Exception {
        reset(depositorService);
    }

    @Test //--- Search by ID does not EXIST
    public void getDepositorByIdNotFoundRestTest() throws Exception {
        expect(depositorService.getBankDepositorById(5L))
                .andThrow(new NotFoundException("Depositor not found for depositorId=", "5"));
        replay(depositorService);

        this.mockMvc.perform(
                get("/depositors/5")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(depositorService);
    }

    @Test //--- search by NAME does not EXIST
    public void getDepositorByNameNotExistRestTest() throws Exception {
        expect(depositorService.getBankDepositorByName("depositorName12"))
                .andThrow(new NotFoundException("Depositor not found for depositorName=", "depositorName12"));
        replay(depositorService);

        this.mockMvc.perform(
                get("/depositors/name/depositorName12")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(depositorService);
    }

    @Test //--- search by Name
    public void getDepositorByNameRestTest() throws Exception {
        expect(depositorService.getBankDepositorByName("depositorName2"))
                .andReturn(DataFixture.getExistDepositor(2L));
        replay(depositorService);

        this.mockMvc.perform(
                get("/depositors/name/depositorName2")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"depositorId\":2,\"depositorName\":\"depositorName2\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}"));
        verify(depositorService);
    }

    @Test //--- search by ID
    public void getBankDepositorByIdRestTest() throws Exception {
        expect(depositorService.getBankDepositorById(1L))
                .andReturn(DataFixture.getExistDepositor(1L));
        replay(depositorService);

        this.mockMvc.perform(
                get("/depositors/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"depositorId\":1,\"depositorName\":\"depositorName1\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}"));
        verify(depositorService);
    }

    @Test //--- search by ID Deposit
    public void getBankDepositorByIdDepositRestTest() throws Exception {
        expect(depositorService.getBankDepositorByIdDeposit(1L))
                .andReturn(DataFixture.getSampleDepositorList());
        replay(depositorService);

        this.mockMvc.perform(
                get("/depositors/deposit/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"depositorId\":1,\"depositorName\":\"depositorName1\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}," +
                        "{\"depositorId\":2,\"depositorName\":\"depositorName2\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}," +
                        "{\"depositorId\":3,\"depositorName\":\"depositorName3\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}]"));
        verify(depositorService);
    }

    @Test //--- search Depositor between dates of deposit from -> to
    public void getBankDepositorBetweenDateDepositRestTest() throws Exception {
        expect(depositorService.getBankDepositorBetweenDateDeposit(dateFormat.parse("2014-12-01"),dateFormat.parse("2014-12-02")))
                .andReturn(DataFixture.getSampleDepositorList());
        replay(depositorService);

        this.mockMvc.perform(
                get("/depositors/date/2014-12-01/2014-12-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"depositorId\":1,\"depositorName\":\"depositorName1\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}," +
                        "{\"depositorId\":2,\"depositorName\":\"depositorName2\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}," +
                        "{\"depositorId\":3,\"depositorName\":\"depositorName3\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}]"));
        verify(depositorService);
    }

    @Test //--- search Depositor between dates Return of deposit from -> to
    public void getBankDepositorBetweenDateReturnDepositRestTest() throws Exception {
        expect(depositorService.getBankDepositorBetweenDateReturnDeposit(dateFormat.parse("2014-12-01"),dateFormat.parse("2014-12-02")))
                .andReturn(DataFixture.getSampleDepositorList());
        replay(depositorService);

        this.mockMvc.perform(
                get("/depositors/date/return/2014-12-01/2014-12-02")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"depositorId\":1,\"depositorName\":\"depositorName1\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}," +
                        "{\"depositorId\":2,\"depositorName\":\"depositorName2\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}," +
                        "{\"depositorId\":3,\"depositorName\":\"depositorName3\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}]"));
        verify(depositorService);
    }

    @Test //--- search ALL
    public void getBankDepositorsRestTest() throws Exception {
        expect(depositorService.getBankDepositors()).andReturn(DataFixture.getSampleDepositorList());
        replay(depositorService);

        this.mockMvc.perform(
                get("/depositors")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"depositorId\":1,\"depositorName\":\"depositorName1\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}," +
                        "{\"depositorId\":2,\"depositorName\":\"depositorName2\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}," +
                        "{\"depositorId\":3,\"depositorName\":\"depositorName3\",\"depositorIdDeposit\":1,\"depositorDateDeposit\":1417381200000,\"depositorAmountDeposit\":10000,\"depositorAmountPlusDeposit\":1000,\"depositorAmountMinusDeposit\":11000,\"depositorDateReturnDeposit\":1417467600000,\"depositorMarkReturnDeposit\":0}]"));
        verify(depositorService);
    }

    @Test //--- add
    public void addBankDepositorRestTest() throws Exception {
        expect(depositorService.addBankDepositor(anyObject(BankDepositor.class)))
                .andReturn(Long.valueOf(1L));
        replay(depositorService);

        ObjectMapper objectMapper = new ObjectMapper();
        String depositorJson = objectMapper.writeValueAsString(DataFixture.getNewDepositor());

        this.mockMvc.perform(
                post("/depositors")
                        .content(depositorJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
        verify(depositorService);
    }

    @Test //--- add EXIST 
    public void addExistBankDepositorRestTest() throws Exception {
        expect(depositorService.addBankDepositor(anyObject(BankDepositor.class)))
                .andThrow(new IllegalArgumentException("Depositor already EXISTS in the database"));
        replay(depositorService);

        ObjectMapper objectMapper = new ObjectMapper();
        String depositorJson = objectMapper.writeValueAsString(DataFixture.getNewDepositor());

        this.mockMvc.perform(
                post("/depositors")
                        .content(depositorJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Depositor already EXISTS in the database\""));
        verify(depositorService);
    }

    @Test //--- add Empty
    public void addEmptyBankDepositorRestTest() throws Exception {
        expect(depositorService.addBankDepositor(DataFixture.getEmptyDepositor()))
                .andThrow(new IllegalArgumentException("Can not be added to the database Empty depositor"));
        replay(depositorService);

        ObjectMapper objectMapper = new ObjectMapper();
        String depositorJson = objectMapper.writeValueAsString(DataFixture.getEmptyDepositor());

        this.mockMvc.perform(
                post("/depositors")
                        .content(depositorJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Can not be added to the database Empty depositor\""));
    }

    @Test //--- add NULL
    public void addNullBankDepositorRestTest() throws Exception {
        expect(depositorService.addBankDepositor(null))
                .andThrow(new IllegalArgumentException("Can not be added to the database NULL depositor"));
        replay(depositorService);

        ObjectMapper objectMapper = new ObjectMapper();
        String depositorJson = objectMapper.writeValueAsString(DataFixture.getNullDepositor());

        this.mockMvc.perform(
                post("/depositors")
                        .content(depositorJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Can not be added to the database NULL depositor\""));
        //verify(depositorService);
    }

    @Test //--- update
    public void updateBankDepositorRestTest() throws Exception {
        depositorService.updateBankDepositor(anyObject(BankDepositor.class));
        replay(depositorService);

        ObjectMapper objectMapper = new ObjectMapper();
        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        depositor.setDepositorName("modified");
        String depositorJson = objectMapper.writeValueAsString(depositor);

        ResultActions result = this.mockMvc.perform(
                put("/depositors")
                        .content(depositorJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        result.andExpect(status().isOk());
        verify(depositorService);
    }

    @Test //--- update Empty
    public void updateEmptyDepositorRestTest() throws Exception {
        depositorService.updateBankDepositor(DataFixture.getEmptyDepositor());
        expectLastCall()
                .andThrow(new IllegalArgumentException("You can not upgrade EMPTY depositor"));
        replay(depositorService);

        ObjectMapper objectMapper = new ObjectMapper();
        String depositorJson = objectMapper.writeValueAsString(DataFixture.getEmptyDepositor());

        ResultActions result = this.mockMvc.perform(
                put("/depositors")
                        .content(depositorJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        result.andExpect(status().isBadRequest());
    }

    @Test //--- update NULL
    public void updateNullDepositRestTest() throws Exception {
        depositorService.updateBankDepositor(null);
        expectLastCall()
                .andThrow(new IllegalArgumentException("You can not upgrade NULL depositor"));
        replay(depositorService);

        ObjectMapper objectMapper = new ObjectMapper();
        String depositorJson = objectMapper.writeValueAsString(DataFixture.getNullDepositor());

        ResultActions result = this.mockMvc.perform(
                put("/depositors")
                        .content(depositorJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        result.andExpect(status().isBadRequest());
    }

    @Test //--- remove
    public void deleteBankDepositorRestTest() throws Exception {
        depositorService.removeBankDepositor(1L);
        replay(depositorService);

        ResultActions result = this.mockMvc.perform(
                delete("/depositors/1"))
                .andDo(print());
        result.andExpect(status().isOk());
        verify(depositorService);
    }

    @Test //--- remove depositor with NULL ID
    public void deleteBankDepositorNullIdRestTest() throws Exception {
        depositorService.removeBankDepositor(null);
        expectLastCall()
                .andThrow(new NumberFormatException("Depositor can not be removed with null id"));
        replay(depositorService);

        ResultActions result = this.mockMvc.perform(
                delete("/depositors/null")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        result.andExpect(status().isBadRequest());
    }

    @Test //--- remove depositor with Error ID
    public void deleteBankDepositErrorIdRestTest() throws Exception {
        depositorService.removeBankDepositor(Long.valueOf(-1));
        expectLastCall()
                .andThrow(new NumberFormatException("Id Depositor - incorrect"));
        replay(depositorService);

        ResultActions result = this.mockMvc.perform(
                delete("/depositors/-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        result.andExpect(status().isBadRequest())
                .andExpect(content().string("\"Id Depositor - incorrect\""));

    }

    @Test //--- remove depositor with NULL ID
    public void deleteBankDepositorNullIdDepositRestTest() throws Exception {
        depositorService.removeBankDepositorByIdDeposit(null);
        expectLastCall()
                .andThrow(new NumberFormatException("Depositor can not be removed with null ID deposit"));
        replay(depositorService);

        ResultActions result = this.mockMvc.perform(
                delete("/depositors/deposit/null")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        result.andExpect(status().isBadRequest());
    }

    @Test //--- remove depositor with Error ID
    public void deleteBankDepositErrorIdDepositRestTest() throws Exception {
        depositorService.removeBankDepositorByIdDeposit(Long.valueOf(-1));
        expectLastCall()
                .andThrow(new NumberFormatException("Id Deposit of depositor - incorrect"));
        replay(depositorService);

        ResultActions result = this.mockMvc.perform(
                delete("/depositors/deposit/-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        result.andExpect(status().isBadRequest())
                .andExpect(content().string("\"Id Deposit of depositor - incorrect\""));
    }

    public static class DataFixture {

        public static BankDepositor getEmptyDepositor() {
            BankDepositor depositor = new BankDepositor(null,null,null,null,0,0,0,null,0);
            return depositor;
        }

        public static BankDepositor getNullDepositor() {
            BankDepositor depositor = null;
            return depositor;
        }

        public static BankDepositor getNewDepositor() throws ParseException{
            BankDepositor depositor = new BankDepositor();
                depositor.setDepositorName("depositorName");
                depositor.setDepositorIdDeposit(1L);
                depositor.setDepositorDateDeposit(dateFormat.parse("2014-12-01"));
                depositor.setDepositorAmountDeposit(10000);
                depositor.setDepositorAmountPlusDeposit(1000);
                depositor.setDepositorAmountMinusDeposit(11000);
                depositor.setDepositorDateReturnDeposit(dateFormat.parse("2014-12-02"));
                depositor.setDepositorMarkReturnDeposit(0);
            return depositor;
        }

        public static BankDepositor getNewDepositor(Long depositorId) throws ParseException{
            BankDepositor depositor = new BankDepositor();
                depositor.setDepositorId(depositorId);
                depositor.setDepositorName("depositorName" + depositorId);
                depositor.setDepositorIdDeposit(1L);
                depositor.setDepositorDateDeposit(dateFormat.parse("2014-12-01"));
                depositor.setDepositorAmountDeposit(10000);
                depositor.setDepositorAmountPlusDeposit(1000);
                depositor.setDepositorAmountMinusDeposit(11000);
                depositor.setDepositorDateReturnDeposit(dateFormat.parse("2014-12-02"));
                depositor.setDepositorMarkReturnDeposit(0);
            return depositor;
        }

        public static BankDepositor getExistDepositor(Long depositorId) throws ParseException{
            BankDepositor depositor = new BankDepositor();
                depositor.setDepositorId(depositorId);
                depositor.setDepositorName("depositorName"+depositorId);
                depositor.setDepositorIdDeposit(1L);
                depositor.setDepositorDateDeposit(dateFormat.parse("2014-12-01"));
                depositor.setDepositorAmountDeposit(10000);
                depositor.setDepositorAmountPlusDeposit(1000);
                depositor.setDepositorAmountMinusDeposit(11000);
                depositor.setDepositorDateReturnDeposit(dateFormat.parse("2014-12-02"));
                depositor.setDepositorMarkReturnDeposit(0);
            return depositor;
        }

        public static List<BankDepositor> getSampleDepositorList() throws ParseException{
            List list = new ArrayList(3);
            list.add(DataFixture.getNewDepositor(1L));
            list.add(DataFixture.getNewDepositor(2L));
            list.add(DataFixture.getNewDepositor(3L));
            return list;
        }
    }
}
package com.brest.bank.rest.controller;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.rest.DepositRestController;
import com.brest.bank.rest.VersionRestController;
import com.brest.bank.rest.exception.NotFoundException;
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

import javax.annotation.Resource;

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

public class DepositRestControllerMockTest {

    private MockMvc mockMvc;

    @Resource
    private DepositRestController depositRestController;

    @Autowired
    private BankDepositService depositService;

    @Before
    public void setUp() {
        this.mockMvc = standaloneSetup(depositRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @After
    public void tearDown() throws Exception {
        reset(depositService);
    }

    @Test //--- Search by ID does not EXIST
    public void getDepositByIdNotFoundRestTest() throws Exception {
        expect(depositService.getBankDepositById(5L))
        		.andThrow(new NotFoundException("Deposit not found for depositId=", "5"));
        replay(depositService);

        this.mockMvc.perform(
        		get("/deposits/5")
                	.accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(depositService);
    }

    @Test //--- search by NAME does not EXIST
    public void getDepositByNameNotExistRestTest() throws Exception {
        expect(depositService.getBankDepositByName("depositName12"))
        		.andThrow(new NotFoundException("Deposit not found for depositName=", "depositName12"));
        replay(depositService);

        this.mockMvc.perform(
        		get("/deposits/name/depositName12")
                	.accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(depositService);
    }
    
    @Test //--- search by Name
    public void getDepositByNameRestTest() throws Exception {
        expect(depositService.getBankDepositByName("depositName2"))
        		.andReturn(DataFixture.getExistDeposit(2L));
        replay(depositService);

        this.mockMvc.perform(
        		get("/deposits/name/depositName2")
                	.accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition2\"}"));
        verify(depositService);
    }
    
    @Test //--- search by ID
    public void getBankDepositByIdRestTest() throws Exception {
        expect(depositService.getBankDepositById(1L))
        		.andReturn(DataFixture.getExistDeposit(1L));
        replay(depositService);

        this.mockMvc.perform(
        		get("/deposits/1")
                	.accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"}"));
        verify(depositService);
    }

    @Test //--- search ALL
    public void getBankDepositsRestTest() throws Exception {
        expect(depositService.getBankDeposits()).andReturn(DataFixture.getSampleDepositList()); 
        replay(depositService);

        this.mockMvc.perform(
                get("/deposits")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition1\"}," +
                        					 "{\"depositId\":2,\"depositName\":\"depositName2\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition2\"}," +
                        					 "{\"depositId\":3,\"depositName\":\"depositName3\",\"depositMinTerm\":12,\"depositMinAmount\":100,\"depositCurrency\":\"usd\",\"depositInterestRate\":4,\"depositAddConditions\":\"condition3\"}]"));
        verify(depositService);
    }
    
    @Test //--- add
    public void addBankDepositRestTest() throws Exception {
        expect(depositService.addBankDeposit(anyObject(BankDeposit.class)))
        		.andReturn(Long.valueOf(1L));
        replay(depositService);

        ObjectMapper objectMapper = new ObjectMapper();
        String depositJson = objectMapper.writeValueAsString(DataFixture.getNewDeposit());

        this.mockMvc.perform(
                post("/deposits")
                        .content(depositJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
        verify(depositService);
    }
    
    @Test //--- add EXIST 
    public void addExistBankDepositRestTest() throws Exception {
        expect(depositService.addBankDeposit(anyObject(BankDeposit.class)))
        		.andThrow(new IllegalArgumentException("Deposit already EXISTS in the database"));
        replay(depositService);

        ObjectMapper objectMapper = new ObjectMapper();
        String depositJson = objectMapper.writeValueAsString(DataFixture.getNewDeposit());

        this.mockMvc.perform(
                post("/deposits")
                        .content(depositJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotModified())
                .andExpect(content().string("\"Deposit already EXISTS in the database\""));
        verify(depositService);
    }

    @Test //--- add Empty
    public void addEmptyBankDepositRestTest() throws Exception {
        expect(depositService.addBankDeposit(DataFixture.getEmptyDeposit()))
                .andThrow(new IllegalArgumentException("Can not be added to the database EmptyL deposit"));
        replay(depositService);

        ObjectMapper objectMapper = new ObjectMapper();
        String depositJson = objectMapper.writeValueAsString(DataFixture.getEmptyDeposit());

        this.mockMvc.perform(
                post("/deposits")
                        .content(depositJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotModified())
                .andExpect(content().string("\"Can not be added to the database Empty deposit\""));
    }

    @Test //--- add NULL
    public void addNullBankDepositRestTest() throws Exception {
        expect(depositService.addBankDeposit(null))
                .andThrow(new IllegalArgumentException("Can not be added to the database NULL deposit"));
        replay(depositService);

        ObjectMapper objectMapper = new ObjectMapper();
        String depositJson = objectMapper.writeValueAsString(DataFixture.getNullDeposit());

        this.mockMvc.perform(
                post("/deposits")
                        .content(depositJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotModified())
                .andExpect(content().string("\"Can not be added to the database NULL deposit\""));
        //verify(depositService);
    }

    @Test //--- update
    public void updateBankDepositRestTest() throws Exception {
        depositService.updateBankDeposit(anyObject(BankDeposit.class));
        replay(depositService);

        ObjectMapper objectMapper = new ObjectMapper();
        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        deposit.setDepositName("modified");
        String depositJson = objectMapper.writeValueAsString(deposit);

        ResultActions result = this.mockMvc.perform(
                	put("/deposits")
                        .content(depositJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                    .andDo(print());
        result.andExpect(status().isOk());
        verify(depositService);
    }

    @Test //--- update Empty
    public void updateEmptyDepositRestTest() throws Exception {
        depositService.updateBankDeposit(DataFixture.getEmptyDeposit());
        expectLastCall()
                .andThrow(new IllegalArgumentException("You can not upgrade EMPTY deposit"));
        replay(depositService);

        ObjectMapper objectMapper = new ObjectMapper();
        String depositJson = objectMapper.writeValueAsString(DataFixture.getEmptyDeposit());

        ResultActions result = this.mockMvc.perform(
                put("/deposits")
                        .content(depositJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        result.andExpect(status().isNotModified());
    }
    
    @Test //--- update NULL
    public void updateNullDepositRestTest() throws Exception {
        depositService.updateBankDeposit(null);
        expectLastCall()
                .andThrow(new IllegalArgumentException("You can not upgrade NULL deposit"));
        replay(depositService);

        ObjectMapper objectMapper = new ObjectMapper();
        String depositJson = objectMapper.writeValueAsString(DataFixture.getNullDeposit());

        ResultActions result = this.mockMvc.perform(
                put("/deposits")
                        .content(depositJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        result.andExpect(status().isNotModified());
    }
    
    @Test //--- remove
    public void deleteBankDepositRestTest() throws Exception {
        depositService.removeBankDeposit(1L);
        replay(depositService);

        ResultActions result = this.mockMvc.perform(
                delete("/deposits/1"))
                .andDo(print());
        result.andExpect(status().isOk());
        verify(depositService);
    }
    
    @Test //--- remove deposit with NULL ID
    public void deleteBankDepositNullIdRestTest() throws Exception {
        depositService.removeBankDeposit(null);
        expectLastCall()
                .andThrow(new NumberFormatException("Deposit can not be removed with null id"));
        replay(depositService);

        ResultActions result = this.mockMvc.perform(
                delete("/deposits/null")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        result.andExpect(status().isBadRequest());
    }

    @Test //--- remove deposit with Error ID
    public void deleteBankDepositEmptyIdRestTest() throws Exception {
        depositService.removeBankDeposit(Long.valueOf(-1));
        expectLastCall()
                .andThrow(new NumberFormatException("Id Deposit - incorrect"));
        replay(depositService);

        ResultActions result = this.mockMvc.perform(
                delete("/deposits/-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        result.andExpect(status().isBadRequest())
                .andExpect(content().string("\"Id Deposit - incorrect\""));

    }
    
    public static class DataFixture {

    	public static BankDeposit getEmptyDeposit() {
            BankDeposit deposit = new BankDeposit(null,null,0,0,null,0,null);
            return deposit;
        }

        public static BankDeposit getNullDeposit() {
            BankDeposit deposit = null;
            return deposit;
        }

    	public static BankDeposit getNewDeposit() {
            BankDeposit deposit = new BankDeposit();
            	deposit.setDepositName("depositName1");
            	deposit.setDepositMinTerm(12);
            	deposit.setDepositMinAmount(100);
            	deposit.setDepositCurrency("usd");
            	deposit.setDepositInterestRate(4);
            	deposit.setDepositAddConditions("condition1");
            return deposit;
        }
        
        public static BankDeposit getNewDeposit(Long depositId) {
            BankDeposit deposit = new BankDeposit();
            	deposit.setDepositId(depositId);
            	deposit.setDepositName("depositName"+depositId);
            	deposit.setDepositMinTerm(12);
            	deposit.setDepositMinAmount(100);
            	deposit.setDepositCurrency("usd");
            	deposit.setDepositInterestRate(4);
            	deposit.setDepositAddConditions("condition"+depositId);
        	return deposit;
        }
        
        public static BankDeposit getExistDeposit(Long depositId) {
            BankDeposit deposit = new BankDeposit();
            	deposit.setDepositId(depositId);
            	deposit.setDepositName("depositName"+depositId);
            	deposit.setDepositMinTerm(12);
            	deposit.setDepositMinAmount(100);
            	deposit.setDepositCurrency("usd");
            	deposit.setDepositInterestRate(4);
            	deposit.setDepositAddConditions("condition"+depositId);
            return deposit;
        }
        
        public static List<BankDeposit> getSampleDepositList() {
            List list = new ArrayList(3);
            	list.add(DataFixture.getNewDeposit(1L));
            	list.add(DataFixture.getNewDeposit(2L));
            	list.add(DataFixture.getNewDeposit(3L));
            return list;
        }
    }
}
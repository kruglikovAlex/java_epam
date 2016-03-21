package com.brest.bank.web;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.text.SimpleDateFormat;

@RunWith(MockitoJUnitRunner.class)
public class BankDepositControllerMockTest {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER = LogManager.getLogger();

    @Mock
    private BankDeposit deposit;

    @Mock
    private BankDepositor depositor;

    @Mock
    private BankDepositService depositService;

    @Mock
    private BankDepositorService depositorService;

    @InjectMocks
    private BankDepositController depositController = new BankDepositController();

    private MockMvc mockMvc;

    @Before
    public void setUp(){
        mockMvc = standaloneSetup(depositController).build();
    }

    @Test
    public void testGetInputFormDeposit() throws Exception{
        LOGGER.debug("testGetInputFormDeposit() - start");
        mockMvc.perform(get("/deposit/inputDeposit"))
                .andExpect(view().name("depositFrame"))
                .andExpect(model().attributeExists("deposit"))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostInputFormDeposit() throws Exception{
        LOGGER.debug("testPostInputFormDeposit() - start");
        depositService.addBankDeposit(deposit);
        mockMvc.perform(post("/deposit/submitDataDeposit")
                .param("depositName","name1")
                .param("depositMinTerm","12")
                .param("depositMinAmount","1000")
                .param("depositCurrency","usd")
                .param("depositInterestRate","4")
                .param("depositAddConditions","conditions1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/deposit/main"));

        verify(depositService).addBankDeposit(deposit);
    }

    @Test
    public void testGetUpdateFormDeposit() throws Exception{
        LOGGER.debug("testGetUpdateFormDeposit() - start");
        when(depositService.getBankDepositById(1L)).thenReturn(DataFixture.getExistDeposit(1L));
        mockMvc.perform(get("/deposit/updateDeposit?depositId={id}",1L))
                .andExpect(view().name("updateFormDeposit"))
                .andExpect(model().attributeExists("deposit"))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositById(1L);
    }

    @Test
    public void testPostUpdateFormDeposit() throws Exception{
        LOGGER.debug("testPostUpdateFormDeposit() - start");
        depositService.updateBankDeposit(deposit);
        mockMvc.perform(post("/deposit/updateDeposit")
                .param("depositId", "1")
                .param("depositName","name1")
                .param("depositMinTerm","12")
                .param("depositMinAmount","1000")
                .param("depositCurrency","usd")
                .param("depositInterestRate","4")
                .param("depositAddConditions","conditions1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/deposit/main"));

        verify(depositService).updateBankDeposit(deposit);
    }

    @Test
    public void testDeleteDeposit() throws Exception{
        LOGGER.debug("testDeleteDeposit() - start");
        mockMvc.perform(delete("/deposit/deleteDeposit?depositId={id}",1L))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/deposit/main"));

        verify(depositService).deleteBankDeposit(1L);
    }

    @Test
    public void testGetListDepositsView() throws Exception{
        LOGGER.debug("testGetListDepositsView() - start");
        when(depositService.getBankDepositsWithDepositors()).thenReturn(DataConfig.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositors()).thenReturn(DataConfig.getEmptyDepositors());
        mockMvc.perform(get("/deposit/main"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositsWithDepositors();
        verify(depositorService).getBankDepositors();
    }
}

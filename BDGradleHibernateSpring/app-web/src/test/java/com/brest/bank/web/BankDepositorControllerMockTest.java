package com.brest.bank.web;

import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class BankDepositorControllerMockTest {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER = LogManager.getLogger();

    @Mock
    private BankDepositor depositor;

    @Mock
    private BankDepositorService depositorService;

    @InjectMocks
    private BankDepositorController depositorController = new BankDepositorController();

    private MockMvc mockMvc;

    @Before
    public void setUp(){
        mockMvc = standaloneSetup(depositorController).build();
    }

    @Test
    public void testGetInputFormDepositor() throws Exception{
        LOGGER.debug("testGetInputFormDepositor() - start");
        mockMvc.perform(get("/depositor/inputDepositor?idDeposit={id}",1L))
                .andExpect(view().name("depositorFrame"))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attributeExists("depositor"))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostInputFormDepositor() throws Exception{
        LOGGER.debug("testPostInputFormDepositor() - start");
        BankDepositor dep = DataFixture.getNewDepositor();
        depositorService.addBankDepositor(1L,dep);
        LOGGER.debug("depositor-{}",dep);
        mockMvc.perform(post("/depositor/submitDataDepositor")
                .param("idDeposit","1")
                .param("depositorId", "")
                .param("depositorName", "depositorName1")
                .param("depositorDateDeposit", "2015-01-01")
                .param("depositorAmountDeposit", "1000")
                .param("depositorAmountPlusDeposit","100")
                .param("depositorAmountMinusDeposit","100")
                .param("depositorDateReturnDeposit","2015-09-09")
                .param("depositorMarkReturnDeposit","0"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/deposit/main?idDeposit=1"));

        verify(depositorService).addBankDepositor(1L,dep);
    }

    @Test
    public void testGetUpdateFormDepositor() throws Exception{
        LOGGER.debug("testGetUpdateFormDepositor() - start");
        when(depositorService.getBankDepositorById(1L)).thenReturn(DataFixture.getExistDepositor(1L));
        mockMvc.perform(get("/depositor/updateDepositor?depositorId={id}",1L))
                .andExpect(view().name("updateFormDepositor"))
                .andExpect(model().attributeExists("depositor"))
                .andExpect(status().isOk());

        verify(depositorService).getBankDepositorById(1L);
    }

    @Test
    public void testPostUpdateFormDepositor() throws Exception{
        LOGGER.debug("testPostUpdateFormDepositor() - start");
        depositorService.updateBankDepositor(depositor);
        mockMvc.perform(post("/depositor/updateDepositor")
                .param("depositorId", "1")
                .param("depositorName", "depositorName1")
                .param("depositorDateDeposit", "2015-01-01")
                .param("depositorAmountDeposit", "1000")
                .param("depositorAmountPlusDeposit","100")
                .param("depositorAmountMinusDeposit","100")
                .param("depositorDateReturnDeposit","2015-09-09")
                .param("depositorMarkReturnDeposit","0"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/deposit/main"));

        verify(depositorService).updateBankDepositor(depositor);
    }

    @Test
    public void testDeleteDepositor() throws Exception{
        LOGGER.debug("testDeleteDepositor() - start");
        mockMvc.perform(get("/depositor/deleteDepositor?depositorId={id}",1L))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/deposit/main"));

        verify(depositorService).removeBankDepositor(1L);
    }

}

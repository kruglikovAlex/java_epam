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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.never;
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
    private BankDeposit deposit;

    @Mock
    private BankDepositor depositor;

    @Mock
    private BankDepositService depositService;

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

    @Test
    public void testFilterById() throws Exception{
        LOGGER.debug("testFilterById() - start");
        when(depositService.getBankDepositByDepositorIdWithDepositors(1L)).thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorById(1L)).thenReturn(DataFixture.getExistDepositor(1L));
        mockMvc.perform(get("/depositor/filterById?depositorId={id}",1L))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",
                                dateFormat.format(DataFixture.getExistDepositors()
                                        .get(0).getDepositorDateDeposit()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositByDepositorIdWithDepositors(1L);
        verify(depositorService).getBankDepositorById(1L);
    }

    @Test
    public void testFilterByIdEmptyBD() throws Exception{
        LOGGER.debug("testFilterByIdEmptyBD() - start");
        when(depositService.getBankDepositByDepositorIdWithDepositors(1L)).thenReturn(DataConfig.getEmptyDepositAllDepositors());
        mockMvc.perform(get("/depositor/filterById?depositorId={id}",1L))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositByDepositorIdWithDepositors(1L);
    }

    @Test
    public void testFilterByIdEmptyDepositors() throws Exception{
        LOGGER.debug("testFilterByIdEmptyDepositors() - start");
        when(depositService.getBankDepositByDepositorIdWithDepositors(2L)).thenReturn(DataFixture.getExistDepositAllNullDepositors(2L));
        when(depositorService.getBankDepositorById(2L)).thenReturn(DataConfig.getEmptyDepositor());
        mockMvc.perform(get("/depositor/filterById?depositorId={id}",2L))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",2L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositByDepositorIdWithDepositors(2L);
        verify(depositorService).getBankDepositorById(2L);
    }

    @Test
    public void testFilterByName() throws Exception{
        LOGGER.debug("testFilterByName() - start");
        when(depositService.getBankDepositByDepositorNameWithDepositors("depositorName1")).thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByName("depositorName1")).thenReturn(DataFixture.getExistDepositor(1L));
        mockMvc.perform(get("/depositor/filterByName?depositorName={name}","depositorName1"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",
                                dateFormat.format(DataFixture.getExistDepositors()
                                        .get(0).getDepositorDateDeposit()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositByDepositorNameWithDepositors("depositorName1");
        verify(depositorService).getBankDepositorByName("depositorName1");
    }

    @Test
    public void testFilterByNameEmptyBD() throws Exception{
        LOGGER.debug("testFilterByNameEmptyBD() - start");
        when(depositService.getBankDepositByDepositorNameWithDepositors("depositorName1")).thenReturn(DataConfig.getEmptyDepositAllDepositors());
        mockMvc.perform(get("/depositor/filterByName?depositorName={name}","depositorName1"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositByDepositorNameWithDepositors("depositorName1");
    }

    @Test
    public void testFilterByNameEmptyDepositors() throws Exception{
        LOGGER.debug("testFilterByNameEmptyDepositors() - start");
        when(depositService.getBankDepositByDepositorNameWithDepositors("depositorName1")).thenReturn(DataFixture.getExistDepositAllNullDepositors(1L));
        when(depositorService.getBankDepositorByName("depositorName1")).thenReturn(DataConfig.getEmptyDepositor());
        mockMvc.perform(get("/depositor/filterByName?depositorName={name}","depositorName1"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositByDepositorNameWithDepositors("depositorName1");
        verify(depositorService).getBankDepositorByName("depositorName1");
    }

    @Test
    public void testFilterByAmount() throws Exception{
        LOGGER.debug("testFilterByAmount() - start");
        when(depositService.getBankDepositsByDepositorAmountWithDepositors(1000,1100))
                .thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/depositor/filterByAmount?fromAmountDepositor={from}&toAmountDepositor={to}", 1000,1100))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",
                                dateFormat.format(DataFixture.getExistDepositors()
                                        .get(0).getDepositorDateDeposit()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositsByDepositorAmountWithDepositors(1000,1100);
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByAmountEmptyFirstRequestParameter() throws Exception{
        LOGGER.debug("testFilterByAmountEmptyFirstRequestParameter() - start");
        when(depositService.getBankDepositsByDepositorAmountWithDepositors(null,1100))
                .thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/depositor/filterByAmount?fromAmountDepositor={from}&toAmountDepositor={to}", null,1100))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",
                                dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService,never())
                .getBankDepositsByDepositorAmountWithDepositors(null,1100);
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByAmountErrorFromToParameter() throws Exception{
        LOGGER.debug("testFilterByAmountErrorFromToParameter() - start");
        when(depositService.getBankDepositsByDepositorAmountWithDepositors(1100,1000))
                .thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/depositor/filterByAmount?fromAmountDepositor={from}&toAmountDepositor={to}", 1100,1000))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",
                                dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService,never())
                .getBankDepositsByDepositorAmountWithDepositors(1100,1000);
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByAmountEmptySecondRequestParameter() throws Exception{
        LOGGER.debug("testFilterByAmountEmptySecondRequestParameter() - start");
        when(depositService.getBankDepositsByDepositorAmountWithDepositors(1000,null))
                .thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/depositor/filterByAmount?fromAmountDepositor={from}&toAmountDepositor={to}", 1000,null))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",
                                dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService,never())
                .getBankDepositsByDepositorAmountWithDepositors(1000,null);
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByAmountEmptyBD() throws Exception{
        LOGGER.debug("testFilterByAmountEmptyBD() - start");
        when(depositService.getBankDepositsByDepositorAmountWithDepositors(1000,1100))
                .thenReturn(DataConfig.getEmptyAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/depositor/filterByAmount?fromAmountDepositor={from}&toAmountDepositor={to}", 1000,1100))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",
                                dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService)
                .getBankDepositsByDepositorAmountWithDepositors(1000,1100);
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByAmountEmptyDepositors() throws Exception{
        LOGGER.debug("testFilterByAmountEmptyDepositors() - start");
        when(depositService.getBankDepositsByDepositorAmountWithDepositors(1000,1100))
                .thenReturn(DataFixture.getExistAllDepositsAllNullDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataConfig.getEmptyDepositors());
        mockMvc.perform(get("/depositor/filterByAmount?fromAmountDepositor={from}&toAmountDepositor={to}", 1000,1100))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year","2016"))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService)
                .getBankDepositsByDepositorAmountWithDepositors(1000,1100);
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }
}

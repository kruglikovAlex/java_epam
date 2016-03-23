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

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class BankDepositControllerMockTest {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER = LogManager.getLogger();

    private List<Map> deposits = new ArrayList<Map>();
    private List<BankDepositor> depositors = new ArrayList<BankDepositor>();

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
    public void testFilterByIdEmptyBD() throws Exception{
        LOGGER.debug("testFilterByIdEmptyBD() - start");
        when(depositService.getBankDepositByIdWithDepositors(1L)).thenReturn(DataConfig.getEmptyDepositAllDepositors());
        mockMvc.perform(get("/deposit/filterById?depositId={id}",1L))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositByIdWithDepositors(1L);
    }

    @Test
    public void testFilterByIdEmptyDepositors() throws Exception{
        LOGGER.debug("testFilterByIdEmptyDepositors() - start");
        when(depositService.getBankDepositByIdWithDepositors(2L)).thenReturn(DataFixture.getExistDepositAllNullDepositors(2L));
        when(depositorService.getBankDepositorByIdDeposit(2L)).thenReturn(DataConfig.getEmptyDepositors());
        mockMvc.perform(get("/deposit/filterById?depositId={id}",2L))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",2L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositByIdWithDepositors(2L);
        verify(depositorService).getBankDepositorByIdDeposit(2L);
    }

    @Test
    public void testFilterById() throws Exception{
        LOGGER.debug("testFilterById() - start");
        when(depositService.getBankDepositByIdWithDepositors(1L)).thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterById?depositId={id}",1L))
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

        verify(depositService).getBankDepositByIdWithDepositors(1L);
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByNameEmptyRequestParameter() throws Exception{
        LOGGER.debug("testFilterByNameEmptyRequestParameter() - start");
        when(depositService.getBankDepositByNameWithDepositors("")).thenReturn(DataConfig.getEmptyDepositAllDepositors());
        mockMvc.perform(get("/deposit/filterByName?depositName={name}",""))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService,never()).getBankDepositByNameWithDepositors("");
    }

    @Test
    public void testFilterByNameEmptyBD() throws Exception{
        LOGGER.debug("testFilterByNameEmptyBD() - start");
        when(depositService.getBankDepositByNameWithDepositors("depositName1")).thenReturn(DataConfig.getEmptyDepositAllDepositors());
        mockMvc.perform(get("/deposit/filterByName?depositName={name}","depositName1"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositByNameWithDepositors("depositName1");
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByNameEmptyDepositors() throws Exception{
        LOGGER.debug("testFilterByNameEmptyDepositors() - start");
        when(depositService.getBankDepositByNameWithDepositors("depositName2")).thenReturn(DataFixture.getExistDepositAllNullDepositors(2L));
        when(depositorService.getBankDepositorByIdDeposit(2L)).thenReturn(DataConfig.getEmptyDepositors());
        mockMvc.perform(get("/deposit/filterByName?depositName={name}","depositName2"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",2L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositByNameWithDepositors("depositName2");
        verify(depositorService).getBankDepositorByIdDeposit(2L);
    }

    @Test
    public void testFilterByName() throws Exception{
        LOGGER.debug("testFilterByName() - start");
        when(depositService.getBankDepositByNameWithDepositors("depositName1")).thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByName?depositName={name}","depositName1"))
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

        verify(depositService).getBankDepositByNameWithDepositors("depositName1");
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrencyEmptyRequestParameter() throws Exception{
        LOGGER.debug("testFilterByCurrencyEmptyRequestParameter() - start");
        when(depositService.getBankDepositsByCurrencyWithDepositors("")).thenReturn(DataConfig.getEmptyAllDepositsAllDepositors());
        mockMvc.perform(get("/deposit/filterByCurrency?depositCurrency={currency}",""))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService,never()).getBankDepositsByCurrencyWithDepositors("");
    }

    @Test
    public void testFilterByCurrencyEmptyBD() throws Exception{
        LOGGER.debug("testFilterByCurrencyEmptyBD() - start");
        when(depositService.getBankDepositsByCurrencyWithDepositors("usd")).thenReturn(DataConfig.getEmptyAllDepositsAllDepositors());
        mockMvc.perform(get("/deposit/filterByCurrency?depositCurrency={currency}","usd"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositsByCurrencyWithDepositors("usd");
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrencyEmptyDepositors() throws Exception{
        LOGGER.debug("testFilterByCurrencyEmptyDepositors() - start");
        when(depositService.getBankDepositsByCurrencyWithDepositors("usd")).thenReturn(DataFixture.getExistAllDepositsAllNullDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataConfig.getEmptyDepositors());
        mockMvc.perform(get("/deposit/filterByCurrency?depositCurrency={currency}","usd"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositsByCurrencyWithDepositors("usd");
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrency() throws Exception{
        LOGGER.debug("testFilterByCurrency() - start");
        when(depositService.getBankDepositsByCurrencyWithDepositors("usd")).thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByCurrency?depositCurrency={currency}","usd"))
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

        verify(depositService).getBankDepositsByCurrencyWithDepositors("usd");
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testGetListDepositsViewEmptyBD() throws Exception{
        LOGGER.debug("testGetListDepositsViewEmptyBD() - start");
        when(depositService.getBankDepositsWithDepositors()).thenReturn(DataConfig.getEmptyAllDepositsAllDepositors());
        mockMvc.perform(get("/deposit/main"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositsWithDepositors();
        verify(depositorService,never()).getBankDepositors();
    }

    @Test
    public void testGetListDepositsViewEmptyDepositors() throws Exception{
        LOGGER.debug("testGetListDepositsViewEmptyDepositors() - start");
        when(depositService.getBankDepositsWithDepositors()).thenReturn(DataFixture.getExistAllDepositsAllNullDepositors());
        when(depositorService.getBankDepositors()).thenReturn(DataConfig.getEmptyDepositors());
        mockMvc.perform(get("/deposit/main"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositsWithDepositors();
        verify(depositorService).getBankDepositors();
    }

    @Test
    public void testGetListDepositsView() throws Exception{
        LOGGER.debug("testGetListDepositsView() - start");
        when(depositService.getBankDepositsWithDepositors()).thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositors()).thenReturn(DataFixture.getExistDepositors());
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

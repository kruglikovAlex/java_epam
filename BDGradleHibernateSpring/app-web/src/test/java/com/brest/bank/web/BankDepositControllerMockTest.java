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

import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.text.SimpleDateFormat;
import java.util.*;

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
        mockMvc.perform(get("/deposit/deleteDeposit?depositId={id}",1L))
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
    public void testFilterByIdFromToDateDeposit() throws Exception{
        LOGGER.debug("testFilterByIdFromToDateDeposit() - start");
        when(depositService.getBankDepositByIdFromToDateDepositWithDepositors(1L
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByIdFromToDateDeposit?depositId={id}&startDateDeposit={start}&endDateDeposit={end}",1L,"2015-01-01","2015-06-06"))
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

        verify(depositService)
                .getBankDepositByIdFromToDateDepositWithDepositors(1L,
                        dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByIdFromToDateDepositEmptyFirstRequestParameter() throws Exception{
        LOGGER.debug("testFilterByIdFromToDateDepositEmptyFirstRequestParameter() - start");
        when(depositService.getBankDepositByIdFromToDateDepositWithDepositors(null
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByIdFromToDateDeposit?depositId={id}&startDateDeposit={start}&endDateDeposit={end}",null,"2015-01-01","2015-06-06"))
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
                .getBankDepositByIdFromToDateDepositWithDepositors(null,
                        dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByIdFromToDateDepositErrorFromToParameter() throws Exception{
        LOGGER.debug("testFilterByIdFromToDateDepositErrorFromToParameter() - start");
        when(depositService.getBankDepositByIdFromToDateDepositWithDepositors(1L
                ,dateFormat.parse("2015-06-06")
                ,dateFormat.parse("2015-01-01")))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByIdFromToDateDeposit?depositId={id}&startDateDeposit={start}&endDateDeposit={end}",1L,"2015-06-06","2015-01-01"))
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
                .getBankDepositByIdFromToDateDepositWithDepositors(1L,
                        dateFormat.parse("2015-06-06"),dateFormat.parse("2015-01-01"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByIdFromToDateDepositEmptySecondRequestParameter() throws Exception{
        LOGGER.debug("testFilterByIdFromToDateDepositEmptySecondRequestParameter() - start");
        when(depositService.getBankDepositByIdFromToDateDepositWithDepositors(1L
                ,null
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByIdFromToDateDeposit?depositId={id}&startDateDeposit={start}&endDateDeposit={end}",1L,"","2015-06-06"))
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
                .getBankDepositByIdFromToDateDepositWithDepositors(1L,null,dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByIdFromToDateDepositEmptyThirdRequestParameter() throws Exception{
        LOGGER.debug("testFilterByIdFromToDateDepositEmptyThirdRequestParameter() - start");
        when(depositService.getBankDepositByIdFromToDateDepositWithDepositors(1L
                ,dateFormat.parse("2015-01-01")
                ,null))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByIdFromToDateDeposit?depositId={id}&startDateDeposit={start}&endDateDeposit={end}",1L,"2015-01-01",""))
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
                .getBankDepositByIdFromToDateDepositWithDepositors(1L,dateFormat.parse("2015-01-01"),null);
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByIdFromToDateDepositEmptyBD() throws Exception{
        LOGGER.debug("testFilterByIdFromToDateDepositEmptyBD() - start");
        when(depositService.getBankDepositByIdFromToDateDepositWithDepositors(1L
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataConfig.getEmptyDepositAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByIdFromToDateDeposit?depositId={id}&startDateDeposit={start}&endDateDeposit={end}",1L,"2015-01-01","2015-06-06"))
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
                .getBankDepositByIdFromToDateDepositWithDepositors(1L,dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByIdFromToDateDepositEmptyDepositors() throws Exception{
        LOGGER.debug("testFilterByIdFromToDateDepositEmptyBD() - start");
        when(depositService.getBankDepositByIdFromToDateDepositWithDepositors(1L
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistDepositAllNullDepositors(1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataConfig.getEmptyDepositors());
        mockMvc.perform(get("/deposit/filterByIdFromToDateDeposit?depositId={id}&startDateDeposit={start}&endDateDeposit={end}",1L,"2015-01-01","2015-06-06"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year","2015"))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService)
                .getBankDepositByIdFromToDateDepositWithDepositors(1L,dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByIdFromToDateReturnDeposit() throws Exception{
        LOGGER.debug("testFilterByIdFromToDateReturnDeposit() - start");
        when(depositService.getBankDepositByIdFromToDateReturnDepositWithDepositors(1L
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByIdFromToDateReturnDeposit?depositId={id}&startDateReturnDeposit={start}&endDateReturnDeposit={end}",1L,"2015-01-01","2015-06-06"))
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

        verify(depositService)
                .getBankDepositByIdFromToDateReturnDepositWithDepositors(1L,
                        dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByIdFromToDateReturnDepositEmptyFirstRequestParameter() throws Exception{
        LOGGER.debug("testFilterByIdFromToDateReturnDepositEmptyFirstRequestParameter() - start");
        when(depositService.getBankDepositByIdFromToDateDepositWithDepositors(null
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByIdFromToDateReturnDeposit?depositId={id}&startDateReturnDeposit={start}&endDateReturnDeposit={end}",null,"2015-01-01","2015-06-06"))
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
                .getBankDepositByIdFromToDateReturnDepositWithDepositors(null,
                        dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByIdFromToDateReturnDepositErrorFromToParameter() throws Exception{
        LOGGER.debug("testFilterByIdFromToDateReturnDepositErrorFromToParameter() - start");
        when(depositService.getBankDepositByIdFromToDateReturnDepositWithDepositors(1L
                ,dateFormat.parse("2015-06-06")
                ,dateFormat.parse("2015-01-01")))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByIdFromToDateReturnDeposit?depositId={id}&startDateReturnDeposit={start}&endDateReturnDeposit={end}",1L,"2015-06-06","2015-01-01"))
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
                .getBankDepositByIdFromToDateReturnDepositWithDepositors(1L,
                        dateFormat.parse("2015-06-06"),dateFormat.parse("2015-01-01"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByIdFromToDateReturnDepositEmptySecondRequestParameter() throws Exception{
        LOGGER.debug("testFilterByIdFromToDateReturnDepositEmptySecondRequestParameter() - start");
        when(depositService.getBankDepositByIdFromToDateReturnDepositWithDepositors(1L
                ,null
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByIdFromToDateReturnDeposit?depositId={id}&startDateReturnDeposit={start}&endDateReturnDeposit={end}",1L,"","2015-06-06"))
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
                .getBankDepositByIdFromToDateReturnDepositWithDepositors(1L,null,dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByIdFromToDateReturnDepositEmptyThirdRequestParameter() throws Exception{
        LOGGER.debug("testFilterByIdFromToDateReturnDepositEmptyThirdRequestParameter() - start");
        when(depositService.getBankDepositByIdFromToDateReturnDepositWithDepositors(1L
                ,dateFormat.parse("2015-01-01")
                ,null))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByIdFromToDateReturnDeposit?depositId={id}&startDateReturnDeposit={start}&endDateReturnDeposit={end}",1L,"2015-01-01",""))
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
                .getBankDepositByIdFromToDateReturnDepositWithDepositors(1L,dateFormat.parse("2015-01-01"),null);
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByIdFromToDateReturnDepositEmptyBD() throws Exception{
        LOGGER.debug("testFilterByIdFromToDateReturnDepositEmptyBD() - start");
        when(depositService.getBankDepositByIdFromToDateReturnDepositWithDepositors(1L
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataConfig.getEmptyDepositAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByIdFromToDateReturnDeposit?depositId={id}&startDateReturnDeposit={start}&endDateReturnDeposit={end}",1L,"2015-01-01","2015-06-06"))
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
                .getBankDepositByIdFromToDateReturnDepositWithDepositors(1L,dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByIdFromToDateReturnDepositEmptyDepositors() throws Exception{
        LOGGER.debug("testFilterByIdFromToDateReturnDepositEmptyBD() - start");
        when(depositService.getBankDepositByIdFromToDateReturnDepositWithDepositors(1L
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistDepositAllNullDepositors(1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataConfig.getEmptyDepositors());
        mockMvc.perform(get("/deposit/filterByIdFromToDateReturnDeposit?depositId={id}&startDateReturnDeposit={start}&endDateReturnDeposit={end}",1L,"2015-01-01","2015-06-06"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year","2015"))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService)
                .getBankDepositByIdFromToDateReturnDepositWithDepositors(1L,dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
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
    public void testFilterByNameFromToDateDeposit() throws Exception{
        LOGGER.debug("testFilterByNameFromToDateDeposit() - start");
        when(depositService.getBankDepositByNameFromToDateDepositWithDepositors("depositName1"
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByNameFromToDateDeposit?depositName={name}&startDateDeposit={start}&endDateDeposit={end}", "depositName1","2015-01-01","2015-06-06"))
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

        verify(depositService)
                .getBankDepositByNameFromToDateDepositWithDepositors("depositName1",
                        dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByNameFromToDateDepositEmptyFirstRequestParameter() throws Exception{
        LOGGER.debug("testFilterByNameFromToDateDepositEmptyFirstRequestParameter() - start");
        when(depositService.getBankDepositByNameFromToDateDepositWithDepositors(""
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByNameFromToDateDeposit?depositName={name}&startDateDeposit={start}&endDateDeposit={end}","","2015-01-01","2015-06-06"))
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
                .getBankDepositByNameFromToDateDepositWithDepositors("",
                        dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByNameFromToDateDepositErrorFromToParameter() throws Exception{
        LOGGER.debug("testFilterByNameFromToDateDepositErrorFromToParameter() - start");
        when(depositService.getBankDepositByNameFromToDateDepositWithDepositors("depositName1"
                ,dateFormat.parse("2015-06-06")
                ,dateFormat.parse("2015-01-01")))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByNameFromToDateDeposit?depositName={name}&startDateDeposit={start}&endDateDeposit={end}","depositName1","2015-06-06","2015-01-01"))
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
                .getBankDepositByNameFromToDateDepositWithDepositors("depositName1",
                        dateFormat.parse("2015-06-06"),dateFormat.parse("2015-01-01"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByNameFromToDateDepositEmptySecondRequestParameter() throws Exception{
        LOGGER.debug("testFilterByNameFromToDateDepositEmptySecondRequestParameter() - start");
        when(depositService.getBankDepositByNameFromToDateDepositWithDepositors("depositName1"
                ,null
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByNameFromToDateDeposit?depositName={name}&startDateDeposit={start}&endDateDeposit={end}","depositName1","","2015-06-06"))
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
                .getBankDepositByNameFromToDateDepositWithDepositors("depositName1",null,dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByNameFromToDateDepositEmptyThirdRequestParameter() throws Exception{
        LOGGER.debug("testFilterByNameFromToDateDepositEmptyThirdRequestParameter() - start");
        when(depositService.getBankDepositByNameFromToDateDepositWithDepositors("depositName1"
                ,dateFormat.parse("2015-01-01")
                ,null))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByNameFromToDateDeposit?depositName={name}&startDateDeposit={start}&endDateDeposit={end}","depositName1","2015-01-01",""))
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
                .getBankDepositByNameFromToDateDepositWithDepositors("depositName1",dateFormat.parse("2015-01-01"),null);
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByNameFromToDateDepositEmptyBD() throws Exception{
        LOGGER.debug("testFilterByNameFromToDateDepositEmptyBD() - start");
        when(depositService.getBankDepositByNameFromToDateDepositWithDepositors("depositName1"
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataConfig.getEmptyDepositAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByNameFromToDateDeposit?depositName={name}&startDateDeposit={start}&endDateDeposit={end}","depositName1","2015-01-01","2015-06-06"))
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
                .getBankDepositByNameFromToDateDepositWithDepositors("depositName1"
                        ,dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByNameFromToDateDepositEmptyDepositors() throws Exception{
        LOGGER.debug("testFilterByNAmeFromToDateDepositEmptyBD() - start");
        when(depositService.getBankDepositByNameFromToDateDepositWithDepositors("depositName1"
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistDepositAllNullDepositors(1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataConfig.getEmptyDepositors());
        mockMvc.perform(get("/deposit/filterByNameFromToDateDeposit?depositName={name}&startDateDeposit={start}&endDateDeposit={end}","depositName1","2015-01-01","2015-06-06"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year","2015"))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService)
                .getBankDepositByNameFromToDateDepositWithDepositors("depositName1"
                        ,dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByNameFromToDateReturnDeposit() throws Exception{
        LOGGER.debug("testFilterByNameFromToDateReturnDeposit() - start");
        when(depositService.getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1"
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByNameFromToDateReturnDeposit?depositName={name}&startDateReturnDeposit={start}&endDateReturnDeposit={end}","depositName1","2015-01-01","2015-06-06"))
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

        verify(depositService)
                .getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1",
                        dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByNameFromToDateReturnDepositEmptyFirstRequestParameter() throws Exception{
        LOGGER.debug("testFilterByNameFromToDateReturnDepositEmptyFirstRequestParameter() - start");
        when(depositService.getBankDepositByNameFromToDateDepositWithDepositors(""
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByNameFromToDateReturnDeposit?depositName={name}&startDateReturnDeposit={start}&endDateReturnDeposit={end}","","2015-01-01","2015-06-06"))
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
                .getBankDepositByNameFromToDateReturnDepositWithDepositors("",
                        dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByNameFromToDateReturnDepositErrorFromToParameter() throws Exception{
        LOGGER.debug("testFilterByNameFromToDateReturnDepositErrorFromToParameter() - start");
        when(depositService.getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1"
                ,dateFormat.parse("2015-06-06")
                ,dateFormat.parse("2015-01-01")))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByNameFromToDateReturnDeposit?depositName={name}&startDateReturnDeposit={start}&endDateReturnDeposit={end}","depositName1","2015-06-06","2015-01-01"))
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
                .getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1",
                        dateFormat.parse("2015-06-06"),dateFormat.parse("2015-01-01"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByNameFromToDateReturnDepositEmptySecondRequestParameter() throws Exception{
        LOGGER.debug("testFilterByNameFromToDateReturnDepositEmptySecondRequestParameter() - start");
        when(depositService.getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1"
                ,null
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByNameFromToDateReturnDeposit?depositName={name}&startDateReturnDeposit={start}&endDateReturnDeposit={end}","depositName1","","2015-06-06"))
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
                .getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1",null,dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByNameFromToDateReturnDepositEmptyThirdRequestParameter() throws Exception{
        LOGGER.debug("testFilterByNameFromToDateReturnDepositEmptyThirdRequestParameter() - start");
        when(depositService.getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1"
                ,dateFormat.parse("2015-01-01")
                ,null))
                .thenReturn(DataFixture.getExistDepositAllDepositors(1L,1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByNameFromToDateReturnDeposit?depositName={name}&startDateReturnDeposit={start}&endDateReturnDeposit={end}","depositName1","2015-01-01",""))
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
                .getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1"
                        ,dateFormat.parse("2015-01-01"),null);
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByNameFromToDateReturnDepositEmptyBD() throws Exception{
        LOGGER.debug("testFilterByNameFromToDateReturnDepositEmptyBD() - start");
        when(depositService.getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1"
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataConfig.getEmptyDepositAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByNameFromToDateReturnDeposit?depositName={name}&startDateReturnDeposit={start}&endDateReturnDeposit={end}","depositName1","2015-01-01","2015-06-06"))
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
                .getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1"
                        ,dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByNameFromToDateReturnDepositEmptyDepositors() throws Exception{
        LOGGER.debug("testFilterByNameFromToDateReturnDepositEmptyBD() - start");
        when(depositService.getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1"
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistDepositAllNullDepositors(1L));
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataConfig.getEmptyDepositors());
        mockMvc.perform(get("/deposit/filterByNameFromToDateReturnDeposit?depositName={name}&startDateReturnDeposit={start}&endDateReturnDeposit={end}","depositName1","2015-01-01","2015-06-06"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year","2015"))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService)
                .getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1"
                        ,dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
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
    public void testFilterByTermEmptyRequestParameter() throws Exception{
        LOGGER.debug("testFilterByTermEmptyRequestParameter() - start");
        Long id = null;
        when(depositService.getBankDepositsByTermWithDepositors(null)).thenReturn(DataConfig.getEmptyAllDepositsAllDepositors());
        mockMvc.perform(get("/deposit/filterByTerm?depositMinTerm={term}",id))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService,never()).getBankDepositsByTermWithDepositors(null);
    }

    @Test
    public void testFilterByTermEmptyBD() throws Exception{
        LOGGER.debug("testFilterByTermEmptyBD() - start");
        when(depositService.getBankDepositsByTermWithDepositors(10)).thenReturn(DataConfig.getEmptyAllDepositsAllDepositors());
        mockMvc.perform(get("/deposit/filterByTerm?depositMinTerm={term}",10))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositsByTermWithDepositors(10);
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByTermEmptyDepositors() throws Exception{
        LOGGER.debug("testFilterByTermEmptyDepositors() - start");
        when(depositService.getBankDepositsByTermWithDepositors(12)).thenReturn(DataFixture.getExistAllDepositsAllNullDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataConfig.getEmptyDepositors());
        mockMvc.perform(get("/deposit/filterByTerm?depositMinTerm={term}",12))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositsByTermWithDepositors(12);
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByTerm() throws Exception{
        LOGGER.debug("testFilterByTerm() - start");
        when(depositService.getBankDepositsByTermWithDepositors(11)).thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByTerm?depositMinTerm={term}",11))
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

        verify(depositService).getBankDepositsByTermWithDepositors(11);
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByAmountEmptyRequestParameter() throws Exception{
        LOGGER.debug("testFilterByAmountEmptyRequestParameter() - start");
        Integer amount = null;
        when(depositService.getBankDepositsByAmountWithDepositors(null)).thenReturn(DataConfig.getEmptyAllDepositsAllDepositors());
        mockMvc.perform(get("/deposit/filterByAmount?depositMinAmount={amount}",amount))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService,never()).getBankDepositsByAmountWithDepositors(null);
    }

    @Test
    public void testFilterByAmountEmptyBD() throws Exception{
        LOGGER.debug("testFilterByAmountEmptyBD() - start");
        when(depositService.getBankDepositsByAmountWithDepositors(1000)).thenReturn(DataConfig.getEmptyAllDepositsAllDepositors());
        mockMvc.perform(get("/deposit/filterByAmount?depositMinAmount={amount}",1000))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositsByAmountWithDepositors(1000);
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByAmountEmptyDepositors() throws Exception{
        LOGGER.debug("testFilterByAmountEmptyDepositors() - start");
        when(depositService.getBankDepositsByAmountWithDepositors(1000)).thenReturn(DataFixture.getExistAllDepositsAllNullDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataConfig.getEmptyDepositors());
        mockMvc.perform(get("/deposit/filterByAmount?depositMinAmount={amount}",1000))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositsByAmountWithDepositors(1000);
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByAmount() throws Exception{
        LOGGER.debug("testFilterByAmount() - start");
        when(depositService.getBankDepositsByAmountWithDepositors(1000)).thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByAmount?depositMinAmount={amount}",1000))
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

        verify(depositService).getBankDepositsByAmountWithDepositors(1000);
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByRateEmptyRequestParameter() throws Exception{
        LOGGER.debug("testFilterByRateEmptyRequestParameter() - start");
        Integer rate = null;
        when(depositService.getBankDepositsByRateWithDepositors(null)).thenReturn(DataConfig.getEmptyAllDepositsAllDepositors());
        mockMvc.perform(get("/deposit/filterByRate?depositInterestRate={rate}",rate))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService,never()).getBankDepositsByRateWithDepositors(null);
    }

    @Test
    public void testFilterByRateEmptyBD() throws Exception{
        LOGGER.debug("testFilterByRateEmptyBD() - start");
        when(depositService.getBankDepositsByRateWithDepositors(4)).thenReturn(DataConfig.getEmptyAllDepositsAllDepositors());
        mockMvc.perform(get("/deposit/filterByRate?depositInterestRate={rate}",4))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositsByRateWithDepositors(4);
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByRateEmptyDepositors() throws Exception{
        LOGGER.debug("testFilterByRateEmptyDepositors() - start");
        when(depositService.getBankDepositsByRateWithDepositors(4)).thenReturn(DataFixture.getExistAllDepositsAllNullDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataConfig.getEmptyDepositors());
        mockMvc.perform(get("/deposit/filterByRate?depositInterestRate={rate}",4))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year",dateFormat.format(Calendar.getInstance().getTime()).substring(0,4)))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService).getBankDepositsByRateWithDepositors(4);
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByRate() throws Exception{
        LOGGER.debug("testFilterByRate() - start");
        when(depositService.getBankDepositsByRateWithDepositors(4)).thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByRate?depositInterestRate={rate}",4))
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

        verify(depositService).getBankDepositsByRateWithDepositors(4);
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrencyFromToDateDeposit() throws Exception{
        LOGGER.debug("testFilterByCurrencyFromToDateDeposit() - start");
        when(depositService.getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd"
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByCurrencyFromToDateDeposit?depositCurrency={currency}&startDateDeposit={start}&endDateDeposit={end}", "usd","2015-01-01","2015-06-06"))
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

        verify(depositService)
                .getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd",
                        dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrencyFromToDateDepositEmptyFirstRequestParameter() throws Exception{
        LOGGER.debug("testFilterByCurrencyFromToDateDepositEmptyFirstRequestParameter() - start");
        when(depositService.getBankDepositsByCurrencyFromToDateDepositWithDepositors(""
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByCurrencyFromToDateDeposit?depositCurrency={currency}&startDateDeposit={start}&endDateDeposit={end}","","2015-01-01","2015-06-06"))
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
                .getBankDepositsByCurrencyFromToDateDepositWithDepositors("",
                        dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrencyFromToDateDepositErrorFromToParameter() throws Exception{
        LOGGER.debug("testFilterByCurrencyFromToDateDepositErrorFromToParameter() - start");
        when(depositService.getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd"
                ,dateFormat.parse("2015-06-06")
                ,dateFormat.parse("2015-01-01")))
                .thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByCurrencyFromToDateDeposit?depositCurrency={currency}&startDateDeposit={start}&endDateDeposit={end}","usd","2015-06-06","2015-01-01"))
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
                .getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd",
                        dateFormat.parse("2015-06-06"),dateFormat.parse("2015-01-01"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrencyFromToDateDepositEmptySecondRequestParameter() throws Exception{
        LOGGER.debug("testFilterByCurrencyFromToDateDepositEmptySecondRequestParameter() - start");
        when(depositService.getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd"
                ,null
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByCurrencyFromToDateDeposit?depositCurrency={currency}&startDateDeposit={start}&endDateDeposit={end}","usd","","2015-06-06"))
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
                .getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd",null,dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrencyFromToDateDepositEmptyThirdRequestParameter() throws Exception{
        LOGGER.debug("testFilterByCurrencyFromToDateDepositEmptyThirdRequestParameter() - start");
        when(depositService.getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd"
                ,dateFormat.parse("2015-01-01")
                ,null))
                .thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByCurrencyFromToDateDeposit?depositCurrency={currency}&startDateDeposit={start}&endDateDeposit={end}","usd","2015-01-01",""))
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
                .getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd",dateFormat.parse("2015-01-01"),null);
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrencyFromToDateDepositEmptyBD() throws Exception{
        LOGGER.debug("testFilterByCurrencyFromToDateDepositEmptyBD() - start");
        when(depositService.getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd"
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataConfig.getEmptyAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByCurrencyFromToDateDeposit?depositCurrency={currency}&startDateDeposit={start}&endDateDeposit={end}","usd","2015-01-01","2015-06-06"))
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
                .getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd"
                        ,dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrencyFromToDateDepositEmptyDepositors() throws Exception{
        LOGGER.debug("testFilterByCurrencyFromToDateDepositEmptyDepositors() - start");
        when(depositService.getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd"
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistAllDepositsAllNullDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataConfig.getEmptyDepositors());
        mockMvc.perform(get("/deposit/filterByCurrencyFromToDateDeposit?depositCurrency={currency}&startDateDeposit={start}&endDateDeposit={end}","usd","2015-01-01","2015-06-06"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year","2015"))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService)
                .getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd"
                        ,dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCriteria() throws Exception{
        LOGGER.debug("testFilterByCriteria() - start");
        Object[] args = {"depositCurrency","usd"
                ,"depositorDateDeposit","2015-01-01"
                ,"depositorDateDeposit","2015-06-06"};
        when(depositService.getBankDepositsByVarArgs(args))
                .thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByCriteria?depositCurrency={currency}&depositorDateDeposit={start}&depositorDateDeposit={end}", "usd","2015-01-01","2015-06-06"))
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

        verify(depositService)
                .getBankDepositsByVarArgs(args);
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrencyFromToDateReturnDeposit() throws Exception{
        LOGGER.debug("testFilterByCurrencyFromToDateReturnDeposit() - start");
        when(depositService.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd"
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByCurrencyFromToDateReturnDeposit?depositCurrency={currency}&startDateReturnDeposit={start}&endDateReturnDeposit={end}","usd","2015-01-01","2015-06-06"))
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

        verify(depositService)
                .getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd",
                        dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrencyFromToDateReturnDepositEmptyFirstRequestParameter() throws Exception{
        LOGGER.debug("testFilterByCurrencyFromToDateReturnDepositEmptyFirstRequestParameter() - start");
        when(depositService.getBankDepositsByCurrencyFromToDateDepositWithDepositors(""
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByCurrencyFromToDateReturnDeposit?depositCurrency={currency}&startDateReturnDeposit={start}&endDateReturnDeposit={end}","","2015-01-01","2015-06-06"))
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
                .getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("",
                        dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrencyFromToDateReturnDepositErrorFromToParameter() throws Exception{
        LOGGER.debug("testFilterByCurrencyFromToDateReturnDepositErrorFromToParameter() - start");
        when(depositService.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd"
                ,dateFormat.parse("2015-06-06")
                ,dateFormat.parse("2015-01-01")))
                .thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByCurrencyFromToDateReturnDeposit?depositCurrency={currency}&startDateReturnDeposit={start}&endDateReturnDeposit={end}","usd","2015-06-06","2015-01-01"))
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
                .getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd",
                        dateFormat.parse("2015-06-06"),dateFormat.parse("2015-01-01"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrencyFromToDateReturnDepositEmptySecondRequestParameter() throws Exception{
        LOGGER.debug("testFilterByCurrencyFromToDateReturnDepositEmptySecondRequestParameter() - start");
        when(depositService.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd"
                ,null
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByCurrencyFromToDateReturnDeposit?depositCurrency={currency}&startDateReturnDeposit={start}&endDateReturnDeposit={end}","usd","","2015-06-06"))
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
                .getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd",null,dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrencyFromToDateReturnDepositEmptyThirdRequestParameter() throws Exception{
        LOGGER.debug("testFilterByCurrencyFromToDateReturnDepositEmptyThirdRequestParameter() - start");
        when(depositService.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd"
                ,dateFormat.parse("2015-01-01")
                ,null))
                .thenReturn(DataFixture.getExistAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByCurrencyFromToDateReturnDeposit?depositCurrency={currency}&startDateReturnDeposit={start}&endDateReturnDeposit={end}","usd","2015-01-01",""))
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
                .getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd"
                        ,dateFormat.parse("2015-01-01"),null);
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrencyFromToDateReturnDepositEmptyBD() throws Exception{
        LOGGER.debug("testFilterByCurrencyFromToDateReturnDepositEmptyBD() - start");
        when(depositService.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd"
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataConfig.getEmptyAllDepositsAllDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataFixture.getExistDepositors());
        mockMvc.perform(get("/deposit/filterByCurrencyFromToDateReturnDeposit?depositCurrency={currency}&startDateReturnDeposit={start}&endDateReturnDeposit={end}","usd","2015-01-01","2015-06-06"))
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
                .getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd"
                        ,dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
        verify(depositorService,never()).getBankDepositorByIdDeposit(1L);
    }

    @Test
    public void testFilterByCurrencyFromToDateReturnDepositEmptyDepositors() throws Exception{
        LOGGER.debug("testFilterByCurrencyFromToDateReturnDepositEmptyBD() - start");
        when(depositService.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd"
                ,dateFormat.parse("2015-01-01")
                ,dateFormat.parse("2015-06-06")))
                .thenReturn(DataFixture.getExistAllDepositsAllNullDepositors());
        when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(DataConfig.getEmptyDepositors());
        mockMvc.perform(get("/deposit/filterByCurrencyFromToDateReturnDeposit?depositCurrency={currency}&startDateReturnDeposit={start}&endDateReturnDeposit={end}","usd","2015-01-01","2015-06-06"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("deposits"))
                .andExpect(model().attributeExists("depositors"))
                .andExpect(model().attributeExists("year"))
                .andExpect(model()
                        .attribute("year","2015"))
                .andExpect(model().attributeExists("idDeposit"))
                .andExpect(model().attribute("idDeposit",1L))
                .andExpect(status().isOk());

        verify(depositService)
                .getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd"
                        ,dateFormat.parse("2015-01-01"),dateFormat.parse("2015-06-06"));
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

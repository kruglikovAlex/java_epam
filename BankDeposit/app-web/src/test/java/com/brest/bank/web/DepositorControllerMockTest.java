package com.brest.bank.web;

import java.text.SimpleDateFormat;
import java.util.*;

import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.service.BankDepositService;
import com.brest.bank.validator.BankDepositorValidator;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class DepositorControllerMockTest {

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Mock
	private BankDepositService depositService;

	@Mock
	private BankDepositorService depositorService;

	@Mock
	private BankDepositorValidator depositorValidator;

	@Mock
	private BankDepositor depositor;

	@InjectMocks
	private DepositorController depositorController = new DepositorController();

	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {
		mvc = standaloneSetup(depositorController).build();
		when(depositorValidator.supports(BankDepositor.class)).thenReturn(true);
	}

	@Test //
	public void testGetInputFormDeposit() throws Exception{
		when(depositorService.addBankDepositor(depositor)).thenReturn(1L);
		mvc.perform(post("/depositors/submitDataDepositor")
				.param("depositorName", "name1")
				.param("depositorDateDeposit", "2014-02-02")
				.param("depositorAmountDeposit", "100")
				.param("depositorAmountPlusDeposit","10")
				.param("depositorAmountMinusDeposit","10")
				.param("depositorDateReturnDeposit","2014-09-09")
				.param("depositorMarkReturnDeposit","0"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/deposits/"));
	}

	@Test //
	public void testGetUpdateForm() throws Exception{
		BankDepositor depositor = new BankDepositor(null,"name1",1L,dateFormat.parse("2014-02-02"),100,10,10,dateFormat.parse("2014-09-09"),0);
		mvc.perform(post("/depositors/updateFormDepositor?depositorIdDeposit={id}",1L)
				.param("depositorName", "name1")
				.param("depositorDateDeposit", "2014-02-02")
				.param("depositorAmountDeposit", "100")
				.param("depositorAmountPlusDeposit","10")
				.param("depositorAmountMinusDeposit","10")
				.param("depositorDateReturnDeposit","2014-09-09")
				.param("depositorMarkReturnDeposit","0"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/deposits/"));
	}

	@Test //
	public void testLaunchDeleteForm() throws Exception{
		mvc.perform(get("/depositors/delete?depositorId={id}",1L))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/deposits/"));
		verify(depositorService).removeBankDepositor(1L);
	}

	@Test //
	public void testGetFilterDepositorById() throws Exception {
		when(depositorService.getBankDepositorById(1L)).thenReturn(new BankDepositor(1L,"name1",1L,dateFormat.parse("2014-02-02"),100,10,10,dateFormat.parse("2014-09-09"),0));
		mvc.perform(get("/depositors/filterByIdDepositor?depositorById={id}",1L))
				.andExpect(view().name("depositsList"))
				.andExpect(model().attributeExists("deposits"))
				.andExpect(model().attributeExists("depositors"))
				.andExpect(model().attributeExists("depositorSum"))
				.andExpect(status().isOk());
	}

	@Test //
	public void testGetFilterDepositorByIdDeposit() throws Exception {
		List<Map> depositsAllDepositors = DataFixture.getDepositWithAllDepositors();
		List<BankDepositor> depositors = DataFixture.getExistDepositors();

		when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(depositors);
		when(depositService.getBankDepositByIdAllDepositors(1L)).thenReturn(depositsAllDepositors);
		mvc.perform(get("/depositors/filterByIdDepositDepositor?depositorByIdDeposit={id}",1L))
				.andExpect(view().name("depositsList"))
				.andExpect(model().attributeExists("deposits"))
				.andExpect(model().attributeExists("depositors"))
				.andExpect(model().attributeExists("depositorSum"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetFilterDepositorByName() throws Exception {
		List<Map> depositsAllDepositors = DataFixture.getDepositWithAllDepositors();

		when(depositorService.getBankDepositorByName("name1")).thenReturn(new BankDepositor(1L, "name1", 1L, dateFormat.parse("2014-02-02"), 100, 10, 10, dateFormat.parse("2014-09-09"), 0));
		when(depositService.getBankDepositByIdAllDepositors(1L)).thenReturn(depositsAllDepositors);

		mvc.perform(get("/depositors/filterByNameDepositor?depositorByName={name}","name1"))
				.andExpect(view().name("depositsList"))
				.andExpect(model().attributeExists("deposits"))
				.andExpect(model().attributeExists("depositors"))
				.andExpect(model().attributeExists("depositorSum"))
				.andExpect(status().isOk());
	}

	@Test //
	public void testGetFilterBetweenDateDeposit() throws Exception{
		List<Map> depositsAllDepositors = DataFixture.getDepositWithAllDepositors();
		List<BankDepositor> depositors = DataFixture.getExistDepositors();

		when(depositService.getBankDepositsAllDepositorsBetweenDateDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositsAllDepositors);
		when(depositorService.getBankDepositorBetweenDateDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositors);

		mvc.perform(get("/depositors/filterBetweenDateDeposit?StartDateDeposit={StartDate}&EndDateDeposit={EndDate}", "2014-02-02", "2014-09-09")
				.param("StartDateDeposit", "2014-02-02")
				.param("EndDateDeposit","2014-09-09"))
				.andExpect(status().isOk())
				.andExpect(view().name("depositsList"));

		verify(depositService).getBankDepositsAllDepositorsBetweenDateDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"));
		verify(depositorService).getBankDepositorBetweenDateDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"));
	}

	@Test //
	public void testGetFilterBetweenDateReturnDeposit() throws Exception{
		List<Map> depositsAllDepositors = DataFixture.getDepositWithAllDepositors();
		List<BankDepositor> depositors = DataFixture.getExistDepositors();

		when(depositService.getBankDepositsAllDepositorsBetweenDateReturnDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositsAllDepositors);
		when(depositorService.getBankDepositorBetweenDateReturnDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositors);

		mvc.perform(get("/depositors/filterBetweenDateReturnDeposit?StartDateReturnDeposit={StartDate}&EndDateReturnDeposit={EndDate}","2014-02-02","2014-09-09")
				.param("StartDateReturnDeposit","2014-02-02")
				.param("EndDateReturnDeposit","2014-09-09"))
				.andExpect(status().isOk())
				.andExpect(view().name("depositsList"));

		verify(depositService).getBankDepositsAllDepositorsBetweenDateReturnDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"));
		verify(depositorService).getBankDepositorBetweenDateReturnDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"));
	}
}

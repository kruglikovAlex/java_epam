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
import com.brest.bank.validator.BankDepositValidator;

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
public class DepositControllerMockTest {

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Mock
	private BankDepositService depositService;

	@Mock
	private BankDepositorService depositorService;

	@Mock
	private BankDepositValidator depositValidator;

	@Mock
	private BankDeposit deposit;

	@InjectMocks
	private DepositController depositController = new DepositController();

	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {
		mvc = standaloneSetup(depositController).build();
		when(depositValidator.supports(BankDeposit.class)).thenReturn(true);
	}

	@Test
	public void testDepositList() throws Exception {
        when(depositService.getBankDeposits()).thenReturn(new ArrayList<BankDeposit>());
        mvc.perform(get("/deposits/"))
				.andExpect(view().name("depositsList"))
				.andExpect(model().attributeExists("deposits"))
				.andExpect(model().attributeExists("depositors"))
				.andExpect(model().attributeExists("depositorSum"))
				.andExpect(status().isOk());
    }

	@Test
	public void testGetInputFormDeposit() throws Exception{
		when(depositService.addBankDeposit(deposit)).thenReturn(1L);
		mvc.perform(post("/deposits/submitDataDeposit")
				.param("depositName","name1")
				.param("depositMinTerm","12")
				.param("depositMinAmount","100")
				.param("depositCurrency","usd")
				.param("depositInterestRate","4")
				.param("depositAddConditions","condition1"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/deposits/"));
	}

	@Test
	public void testGetUpdateForm() throws Exception{
		mvc.perform(post("/deposits/updateFormDeposit")
				.param("depositName", "name1")
				.param("depositMinTerm","12")
				.param("depositMinAmount","100")
				.param("depositCurrency","usd")
				.param("depositInterestRate","4")
				.param("depositAddConditions","condition1"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/deposits/"));
	}

	@Test
	public void testLaunchDeleteForm() throws Exception{
		mvc.perform(get("/deposits/deleteDeposit?depositId={id}",1L))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/deposits/"));
		verify(depositService).removeBankDeposit(1L);
	}

	@Test
	public void testGetFilterDepositById() throws Exception {
		when(depositService.getBankDepositById(1L)).thenReturn(new BankDeposit(1L,"name1",12,100,"usd",4,"condition1"));
		mvc.perform(get("/deposits/filterByIdDeposit?depositById={id}",1L))
				.andExpect(view().name("depositsList"))
				.andExpect(model().attributeExists("deposits"))
				.andExpect(model().attributeExists("depositors"))
				.andExpect(model().attributeExists("depositorSum"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetFilterDepositByIdBetweenDateDeposit() throws Exception{
		List<Map> depositsAllDepositors = DataFixture.getDepositWithAllDepositors();
		List<BankDepositor> depositors = DataFixture.getExistDepositors();
		when(depositService.getBankDepositByIdWithAllDepositorsBetweenDateDeposit(1L, dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositsAllDepositors);
		when(depositorService.getBankDepositorByIdDepositBetweenDateDeposit(1L, dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositors);

		mvc.perform(get("/deposits/filterByIdDepositBetweenDateDeposit?depositById={Id}&StartDateDeposit={StartDate}&EndDateDeposit={EndDate}",1L,"2014-02-02","2014-09-09")
				.param("depositById","1L")
				.param("StartDateDeposit","2014-02-02")
				.param("EndDateDeposit","2014-09-09"))
				.andExpect(status().isOk())
				.andExpect(view().name("depositsList"));

		verify(depositService).getBankDepositByIdWithAllDepositorsBetweenDateDeposit(1L, dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"));
		verify(depositorService).getBankDepositorByIdDepositBetweenDateDeposit(1L, dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"));
	}

	@Test
	public void testGetFilterDepositByIdBetweenDateReturnDeposit() throws Exception{
		List<Map> depositsAllDepositors = DataFixture.getDepositWithAllDepositors();
		List<BankDepositor> depositors = DataFixture.getExistDepositors();
		when(depositService.getBankDepositByIdWithAllDepositorsBetweenDateReturnDeposit(1L, dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositsAllDepositors);
		when(depositorService.getBankDepositorByIdDepositBetweenDateReturnDeposit(1L, dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositors);

		mvc.perform(get("/deposits/filterByIdDepositBetweenDateReturnDeposit?depositById={Id}&StartDateReturnDeposit={StartDate}&EndDateReturnDeposit={EndDate}",1L,"2014-02-02","2014-09-09")
				.param("depositById","1L")
				.param("StartDateReturnDeposit","2014-02-02")
				.param("EndDateReturnDeposit","2014-09-09"))
				.andExpect(status().isOk())
				.andExpect(view().name("depositsList"));

		verify(depositService).getBankDepositByIdWithAllDepositorsBetweenDateReturnDeposit(1L, dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"));
		verify(depositorService).getBankDepositorByIdDepositBetweenDateReturnDeposit(1L, dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"));
	}

	@Test
	public void testGetFilterDepositByName() throws Exception {
		List<Map> depositsAllDepositors = DataFixture.getDepositWithAllDepositors();
		List<BankDepositor> depositors = DataFixture.getExistDepositors();
		when(depositService.getBankDepositByName("name1")).thenReturn(new BankDeposit(1L,"name1",12,100,"usd",4,"condition1"));
		when(depositService.getBankDepositByNameAllDepositors("name1")).thenReturn(depositsAllDepositors);
		when(depositorService.getBankDepositorByIdDeposit(1L)).thenReturn(depositors);
		mvc.perform(get("/deposits/filterByNameDeposit?depositByName={name}","name1"))
				.andExpect(view().name("depositsList"))
				.andExpect(model().attributeExists("deposits"))
				.andExpect(model().attributeExists("depositors"))
				.andExpect(model().attributeExists("depositorSum"))
				.andExpect(status().isOk());

	}

	@Test
	public void testGetFilterDepositByNameBetweenDateDeposit() throws Exception{
		List<Map> depositsAllDepositors = DataFixture.getDepositWithAllDepositors();
		List<BankDepositor> depositors = DataFixture.getExistDepositors();
		BankDepositor depositorsSum = new BankDepositor(null,null,null,null,100,10,10,null,null);

		when(depositService.getBankDepositByNameWithAllDepositorsBetweenDateDeposit("name1", dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositsAllDepositors);
		when(depositorService.getBankDepositorByIdDepositBetweenDateDeposit(1L, dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositors);
		when(depositorService.getBankDepositorsSummAmountByIdDepositBetweenDateDeposit(1L, dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositorsSum);

		mvc.perform(get("/deposits/filterByNameDepositBetweenDateDeposit?depositByName={name}&StartDateDeposit={StartDate}&EndDateDeposit={EndDate}","name1","2014-02-02","2014-09-09"))
				.andExpect(status().isOk())
				.andExpect(view().name("depositsList"));

		verify(depositService).getBankDepositByNameWithAllDepositorsBetweenDateDeposit("name1", dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"));
		verify(depositorService).getBankDepositorByIdDepositBetweenDateDeposit(1L, dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"));
	}

	@Test
	public void testGetFilterDepositByNameBetweenDateReturnDeposit() throws Exception{
		List<Map> depositsAllDepositors = DataFixture.getDepositWithAllDepositors();
		List<BankDepositor> depositors = DataFixture.getExistDepositors();
		BankDepositor depositorsSum = new BankDepositor(null,null,null,null,100,10,10,null,null);

		when(depositService.getBankDepositByNameWithAllDepositorsBetweenDateReturnDeposit("name1", dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositsAllDepositors);
		when(depositorService.getBankDepositorByIdDepositBetweenDateReturnDeposit(1L, dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositors);
		when(depositorService.getBankDepositorsSummAmountByIdDepositBetweenDateDeposit(1L, dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositorsSum);

		mvc.perform(get("/deposits/filterByNameDepositBetweenDateReturnDeposit?depositByName={name}&StartDateReturnDeposit={StartDate}&EndDateReturnDeposit={EndDate}", "name1", "2014-02-02", "2014-09-09"))
				.andExpect(status().isOk())
				.andExpect(view().name("depositsList"));

		verify(depositService).getBankDepositByNameWithAllDepositorsBetweenDateReturnDeposit("name1", dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"));
		verify(depositorService).getBankDepositorByIdDepositBetweenDateReturnDeposit(1L, dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"));
	}

	@Test
	public void testGetFilterDepositBetweenDateDeposit() throws Exception{
		List<Map> depositsAllDepositors = DataFixture.getDepositWithAllDepositors();
		List<BankDepositor> depositors = DataFixture.getExistDepositors();
		BankDepositor depositorsSum = new BankDepositor(null,null,null,null,100,10,10,null,null);

		when(depositService.getBankDepositsAllDepositorsBetweenDateDeposit( dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositsAllDepositors);
		when(depositorService.getBankDepositorBetweenDateDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositors);
		when(depositorService.getBankDepositorSummAmountDepositBetweenDateDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositorsSum);
		mvc.perform(get("/deposits/filterBetweenDateDeposit?StartDateDeposit={StartDate}&EndDateDeposit={EndDate}","2014-02-02","2014-09-09"))
				.andExpect(status().isOk())
				.andExpect(view().name("depositsList"));

		verify(depositService).getBankDepositsAllDepositorsBetweenDateDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"));
		verify(depositorService).getBankDepositorBetweenDateDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"));
	}

	@Test
	public void testGetFilterDepositBetweenDateReturnDeposit() throws Exception{
		List<Map> depositsAllDepositors = DataFixture.getDepositWithAllDepositors();
		List<BankDepositor> depositors = DataFixture.getExistDepositors();
		BankDepositor depositorsSum = new BankDepositor(null,null,null,null,100,10,10,null,null);

		when(depositService.getBankDepositsAllDepositorsBetweenDateReturnDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositsAllDepositors);
		when(depositorService.getBankDepositorBetweenDateReturnDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositors);
		when(depositorService.getBankDepositorSummAmountDepositBetweenDateReturnDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"))).thenReturn(depositorsSum);
		mvc.perform(get("/deposits/filterBetweenDateReturnDeposit?StartDateReturnDeposit={StartDate}&EndDateReturnDeposit={EndDate}","2014-02-02","2014-09-09"))
				.andExpect(status().isOk())
				.andExpect(view().name("depositsList"));

		verify(depositService).getBankDepositsAllDepositorsBetweenDateReturnDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"));
		verify(depositorService).getBankDepositorBetweenDateReturnDeposit(dateFormat.parse("2014-02-02"), dateFormat.parse("2014-09-09"));
	}

}

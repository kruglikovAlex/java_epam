package com.brest.bank.web;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.service.BankDepositService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class DepositControllerStandaloneTest {

	@Mock
	private BankDepositService depositService;

	@InjectMocks
	private DepositController depositController = new DepositController();

	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {
		mvc = standaloneSetup(depositController).build();

	}

	@Test
	public void testDepositList() throws Exception {
        when(depositService.getBankDeposits()).thenReturn(new ArrayList<BankDeposit>());
        mvc.perform(get("/deposits/")).andExpect(status().isOk())
                .andExpect(view().name("depositsList"));


    }

}

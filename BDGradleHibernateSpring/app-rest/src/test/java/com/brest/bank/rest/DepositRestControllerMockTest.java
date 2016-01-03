package com.brest.bank.rest;

import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.DataFixture;
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

import static org.easymock.EasyMock.*;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-rest-mock-test.xml"})
public class DepositRestControllerMockTest {

    private MockMvc mockMvc;

    @Resource
    DepositRestController depositRestController;

    @Autowired
    BankDepositService depositService;

    @Before
    public void setUp(){
        this.mockMvc = standaloneSetup(depositRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @After
    public void tearDown(){
        reset(depositService);
    }

    @Test
    public void testGetDeposits() throws Exception{
        expect(depositService.getBankDeposits()).andReturn(DataFixture.getExistDeposits());
        replay(depositService);

        this.mockMvc.perform(
                get("/deposit/all")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"depositId\":1,\"depositName\":\"depositName1\",\"depositMinTerm\":12," +
                        "\"depositMinAmount\":1000,\"depositCurrency\":\"usd\",\"depositInterestRate\":4," +
                        "\"depositAddConditions\":\"conditions1\",\"depositors\":[]}]"));

        verify(depositService);
    }
}

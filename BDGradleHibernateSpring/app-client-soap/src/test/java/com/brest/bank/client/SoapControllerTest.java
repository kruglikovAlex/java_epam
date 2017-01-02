package com.brest.bank.client;

import com.brest.bank.web.SoapController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
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

import javax.wsdl.Definition;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SoapControllerTest {

    private static final Logger LOGGER = LogManager.getLogger(SoapControllerTest.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    @Mock
    SoapClient soapClient;
    private Map wsdlServices = new HashMap();
    private String[] soapResponse = {"",""};
    private String soapRequest = "";
    private String wsdlLocation = "http://localhost:8080/SpringHibernateBDeposit-1.0/soap/soapService.wsdl";
    private Definition wsdl;
    @InjectMocks
    private SoapController soapController = new SoapController();

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = standaloneSetup(soapController).build();
    }

    @Test
    public void postSoapQuery() throws Exception {
        LOGGER.debug("postSoapQuery() - start");

        soapRequest = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://bank.brest.com/soap\">\n" +
                "\t<x:Header/>\n" +
                "\t<x:Body>\n" +
                "\t\t<soa:getBankDepositsRequest>\n" +
                "\t\t</soa:getBankDepositsRequest>\n" +
                "\t</x:Body>\n" +
                "</x:Envelope>";

        when(soapClient.getBankDeposits()).thenReturn(DataFixture.getDepositsWsdl());
        mockMvc.perform(post("/client/submitSoapQuery?soapQuery={soapQuery}&submit={submit}","getBankDeposits","Generate+Request"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("services"))
                .andExpect(model().attributeExists("requests"))
                .andExpect(model().attribute("requests",soapRequest))
                .andExpect(model().attributeExists("responses"))
                .andExpect(status().isOk());

        verify(soapClient).getBankDeposits();
    }

    @Test
    public void testGetSoapViewWithRequestRaram() throws Exception {
        LOGGER.debug("getSoapView() - start");

        when(soapClient.readWSDLFile(wsdlLocation)).thenReturn(wsdl);
        mockMvc.perform(get("/client/main?wsdlLoc=http://localhost:8080/SpringHibernateBDeposit-1.0/soap/soapService.wsdl"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("services"))
                .andExpect(model().attributeExists("requests"))
                .andExpect(model().attribute("requests",""))
                .andExpect(model().attributeExists("responses"))
                .andExpect(status().isOk());

        verify(soapClient).readWSDLFile(wsdlLocation);
    }

    @Test
    public void testGetSoapViewWithoutRequestRaram() throws Exception {
        LOGGER.debug("getSoapView() without Request Raram - start");

        wsdlServices.put("",null);

        mockMvc.perform(get("/client/main"))
                .andExpect(view().name("mainFrame"))
                .andExpect(model().attributeExists("services"))
                .andExpect(model().attribute("services",wsdlServices))
                .andExpect(model().attributeExists("requests"))
                .andExpect(model().attribute("requests",""))
                .andExpect(model().attributeExists("responses"))
                .andExpect(model().attribute("responses",soapResponse))
                .andExpect(status().isOk());

        verify(soapClient,never()).readWSDLFile(wsdlLocation);
    }

}
package com.brest.bank.client;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class RestClient {

    private static final Logger LOGGER = LogManager.getLogger();

    private String host;
    private RestTemplate restTemplate = new RestTemplate();

    //--- Setter Dependency Injection
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    //--- Constructor Dependency Injection
    public RestClient(String host) {
        this.host = host;
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(new MappingJackson2HttpMessageConverter());
        restTemplate.setMessageConverters(converters);
    }

    //--- output
    
    //--- show version 
    //--- http://<host:port>/version/
    //--- request method - GET -> .getForObject()
    public String getRestVesrsion() {
        LOGGER.debug("getRestVesrsion {}", host);
        return restTemplate.getForObject(host + "/version", String.class);
    }

    //--- all deposit & deposit

    //--- get all deposits 
    //--- http://<name_host:port>/deposits/
    //--- request method - GET -> .getForObject()
    public BankDeposit[] getBankDeposits() {
        LOGGER.debug("getBankDeposits {}", host);
        return restTemplate.getForObject(host + "/deposits/", BankDeposit[].class);
    }
    //--- get all depositors
    //--- http://<name_host:port>/depositors/
    //--- request method - GET -> .getForObject()
    public BankDepositor[] getBankDepositors() {
        LOGGER.debug("getBankDepositors {}", host);
        return restTemplate.getForObject(host + "/depositors/", BankDepositor[].class);
    }

    //--- get deposit by depositId 
    //--- http://<name_host:port>/deposits/{depositId}
    //--- request method - GET -> .getForObject()
    public BankDeposit getBankDepositById(Long depositId) {
        LOGGER.debug("getBankDepositById {}", depositId);
        return restTemplate.getForObject(host+"/deposits/"+depositId, BankDeposit.class);
    }
    //--- get depositor by depositorId
    //--- http://<name_host:port>/depositors/{depositorId}
    //--- request method - GET -> .getForObject()
    public BankDepositor getBankDepositorById(Long depositorId) {
        LOGGER.debug("getBankDepositorById {}", depositorId);
        return restTemplate.getForObject(host+"/depositors/"+depositorId, BankDepositor.class);
    }
    //--- get depositor by depositorIdDeposit
    //--- http://<name_host:port>/depositors/deposit/{depositorIdDeposit}
    //--- request method - GET -> .getForObject()
    public BankDepositor[] getBankDepositorByIdDeposit(Long depositorIdDeposit) {
        LOGGER.debug("getBankDepositorByIdDeposit {}", depositorIdDeposit);
        return restTemplate.getForObject(host+"/depositors/deposit/"+depositorIdDeposit, BankDepositor[].class);
    }

    //--- get deposit by depositName 
    //--- http://<name_host:port>/deposits/name/{depositName}
    //--- request method - GET -> .getForObject()
    public BankDeposit getBankDepositByName(String depositName) {
        LOGGER.debug("getBankDepositByName {}", depositName);
        return restTemplate.getForObject(host+"/deposits/name/"+depositName, BankDeposit.class);
    }
    //--- get depositor by depositorName
    //--- http://<name_host:port>/depositors/name/{depositorName}
    //--- request method - GET -> .getForObject()
    public BankDepositor getBankDepositorByName(String depositorName) {
        LOGGER.debug("getBankDepositorByName {}", depositorName);
        return restTemplate.getForObject(host+"/depositors/name/"+depositorName, BankDepositor.class);
    }

    //--- get depositor between dates open deposit from ... to
    //--- http://<name_host:port>/depositors/date/{DateDeposit1}/{DateDeposit2}
    //--- request method - GET -> .getForObject()
    public BankDepositor[] getBankDepositorBetweenDateDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositorBetweenDateDeposit(Date:{}{})", startDate, endDate);
        return restTemplate.getForObject(host+"/depositors/date/"+startDate+"/"+endDate, BankDepositor[].class);
    }
    //--- get depositor between dates of the alleged liquidation or full liquidation deposit from ... to
    //--- http://<name_host:port>/depositors/date/return/{DateDeposit1}/{DateDeposit2}
    //--- request method - GET -> .getForObject()
    public BankDepositor[] getBankDepositorBetweenDateReturnDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositorBetweenDateReturnDeposit(Date:{}{})", startDate, endDate);
        return restTemplate.getForObject(host+"/depositors/date/return/"+startDate+"/"+endDate, BankDepositor[].class);
    }

    //--- change data
    
    //--- add deposit
    //--- http://<name_host:port>/deposits/{"depositName":"depositName","":"",...,"":""}
    //--- request method - POST -> .postForObject()
    public long addBankDeposit(BankDeposit deposit) {
        LOGGER.debug("addBankDeposit {}", deposit);
        return restTemplate.postForObject(host+"/deposits/", deposit, Long.class);
    }
    //--- add depositor
    //--- http://<name_host:port>/depositors/{"depositorName":"depositorName","":"",...,"":""}
    //--- request method - POST -> .postForObject()
    public long addBankDepositor(BankDepositor depositor) {
        LOGGER.debug("addBankDepositor {}", depositor);
        return restTemplate.postForObject(host+"/depositors/", depositor, Long.class);
    }

    //--- update deposit
    //--- http://<name_host:port>/deposits/{"depositId":0,"depositName":"depositName",...,"":""}
    //--- request method - PUT -> .put()
    public void updateBankDeposit(BankDeposit deposit) {
        LOGGER.debug("updateBankDeposit {}", deposit);
        restTemplate.put(host + "/deposits/", deposit);
    }
    //--- update depositor
    //--- http://<name_host:port>/depositors/{"depositorId":0,"depositorName":"depositorName",...,"":""}
    //--- request method - PUT -> .put()
    public void updateBankDepositor(BankDepositor depositor) {
        LOGGER.debug("updateBankDepositor {}", depositor);
        restTemplate.put(host + "/depositors/", depositor);
    }

    //--- delete deposit
    //--- http://<name_host:port>/deposits/{depositId}
    //--- request method - DELETE -> .delete()
    public void removeBankDeposit(Long depositId) {
        LOGGER.debug("removeBankDeposit {}", depositId);
        restTemplate.delete(host + "/deposits/" + depositId);
    }
    //--- delete depositor
    //--- http://<name_host:port>/depositors/{depositorId}
    //--- request method - DELETE -> .delete()
    public void removeBankDepositor(Long depositorId) {
        LOGGER.debug("removeBankDepositor {}", depositorId);
        restTemplate.delete(host + "/depositors/" + depositorId);
    }
    //--- delete depositor by id deposit
    //--- http://<name_host:port>/depositors/deposit/{depositorIdDeposit}
    //--- request method - DELETE -> .delete()
    public void removeBankDepositorByIdDeposit(Long depositorIdDeposit) {
        LOGGER.debug("removeBankDepositorByIdDeposit {}", depositorIdDeposit);
        restTemplate.delete(host + "/depositors/deposit/" + depositorIdDeposit);
    }
}

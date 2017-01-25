package com.brest.bank.client;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RestClient {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final String ERROR_FROM_TO_PARAM = "The first parameter should be less than the second";
    private static final Logger LOGGER = LogManager.getLogger();

    private String host;
    private RestTemplate restTemplate = new RestTemplate();

    public RestClient(){
        //this.host = host;
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(new MappingJackson2HttpMessageConverter());
        restTemplate.setMessageConverters(converters);
    }

    public String getHost(){
        return host;
    }

    public void setHost(String host){
        this.host = host;
    }

    public RestTemplate getRestTemplate(){
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    /**
     * Show rest client version
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/version
     * @return String
     */
    public String getRestVersion(){
        LOGGER.debug("getRestVersion {}",host);
        return restTemplate.getForObject(host+"/version", String.class);
    }

    /**
     * Get all Bank deposits
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/all
     * @return BankDeposit[]
     */
    public BankDeposit[] getBankDeposits(){
        LOGGER.debug("getBankDeposits {}",host+"/deposit/all");
        return restTemplate.getForObject(host+"/deposit/all",BankDeposit[].class);
    }

    /**
     * Get Bank Deposit by id deposit
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/id/{depositId}
     * @param depositId Long - id of the Bank Deposit to return
     * @return BankDeposit with the specified id from the database
     */
    public BankDeposit getDepositById(Long depositId){
        LOGGER.debug("getDepositById {}",host+"/deposit/id/"+depositId);
        return restTemplate.getForObject(host+"/deposit/id/"+depositId, BankDeposit.class);
    }

    /**
     * Get Bank Deposit by name
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/name/{depositName}
     * @param depositName String - name of the Bank Deposit to return
     * @return BankDeposit with the specified depositName from the database
     */
    public BankDeposit getDepositByName(String depositName){
        LOGGER.debug("getDepositByName {}", host+"/deposit/name/"+depositName);
        return restTemplate.getForObject(host+"/deposit/name/"+depositName, BankDeposit.class);
    }

    /**
     * Get Bank Deposits by currency
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/currency/{depositCurrency}
     *
     * @param depositCurrency String - currency of the Bank Deposit to return
     * @return BankDeposit[]
     */
    public BankDeposit[] getBankDepositsByCurrency(String depositCurrency){
        LOGGER.debug("getBankDepositsByCurrency {}", host+"/deposit/currency/"+depositCurrency);
        return restTemplate.getForObject(host+"/deposit/currency/"+depositCurrency, BankDeposit[].class);
    }

    /**
     * Get Bank Deposits by interest rate
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/rate/{depositInterestRate}
     * @param depositInterestRate Integer - interest rate of the Bank Deposits to return
     * @return BankDeposit[]
     */
    public BankDeposit[] getBankDepositsByInterestRate(Integer depositInterestRate){
        LOGGER.debug("getBankDepositsByInterestRate {}",host+"/deposit/rate/"+depositInterestRate);
        return restTemplate.getForObject(host+"/deposit/rate/"+depositInterestRate, BankDeposit[].class);
    }

    /**
     * Get Bank Deposits from-to MIN TERM values
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/term/{start},{end}
     * @param start Integer - start value of the min term (count month)
     * @param end Integer - end value of the min term (count month)
     * @return BankDeposit[]
     */
    public BankDeposit[] getBankDepositsFromToMinTerm(Integer start, Integer end){
        LOGGER.debug("getBankDepositsFromToMinTerm {}", host+"/deposit/term/"+start+","+end);
        Assert.isTrue(start<=end,ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/term/"+start+","+end,BankDeposit[].class);
    }

    /**
     * Get Bank Deposits from-to INTEREST RATE values
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/rateBetween/{start},{end}
     * @param start Integer - start value of the interest rate (0% < startRate <= 100%)
     * @param end Integer - end value of the interest rate (0% < endRate <= 100%)
     * @return BankDeposit[]
     */
    public BankDeposit[] getBankDepositsFromToInterestRate(Integer start, Integer end){
        LOGGER.debug("getBankDepositsFromToInterestRate {}",host+"/deposit/rateBetween/"+start+","+end);
        Assert.isTrue(start<=end,ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/rateBetween/"+start+","+end,BankDeposit[].class);
    }

    /**
     * Get Bank Deposits from-to Date Deposit values
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/date/{start},{end}
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (endDate > startDate)
     * @return BankDeposit[]
     */
    public BankDeposit[] getBankDepositsFromToDateDeposit(String startDate, String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositsFromToDateDeposit {}",host+"/deposit/date/"+startDate+","+endDate);
        Assert.isTrue(dateFormat.parse(startDate).before(dateFormat.parse(endDate))||
                dateFormat.parse(startDate).equals(dateFormat.parse(endDate)),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/date/"+startDate+","+endDate,BankDeposit[].class);
    }

    /**
     * Get Bank Deposits from-to Date return Deposit values
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/returnDate/{start},{end}
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date return deposit (endDate > startDate)
     * @return BankDeposit[]
     */
    public BankDeposit[] getBankDepositsFromToDateReturnDeposit(String startDate, String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositsFromToDateReturnDeposit {}",host+"/deposit/returnDate/"+startDate+","+endDate);
        Assert.isTrue(dateFormat.parse(startDate).before(dateFormat.parse(endDate))||
                dateFormat.parse(startDate).equals(dateFormat.parse(endDate)),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/returnDate/"+startDate+","+endDate,BankDeposit[].class);
    }

    /**
     * Get Bank Deposits by NAME with depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/name/{depositName}
     * @param depositName String - name of the Bank Deposit to return
     * @return LinkedHashMap<K,V>
     */
    public LinkedHashMap getBankDepositByNameWithDepositors(String depositName){
        LOGGER.debug("getBankDepositByNameWithDepositors {}",host+"/deposit/report/name/"+depositName);
        return restTemplate.getForObject(host+"/deposit/report/name/"+depositName, LinkedHashMap.class);
    }

    /**
     * Get Bank Deposits by NAME with depositors from-to Date Deposit values
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/nameDate/{depositName},{start},{end}
     * @param depositName String - name of the Bank Deposit to return
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (endDate > startDate)
     * @return LinkedHashMap<K,V>
     */
    public LinkedHashMap getBankDepositByNameFromToDateDepositWithDepositors(String depositName, String startDate, String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositByNameFromToDateDepositWithDepositors {}",
                host+"/deposit/report/nameDate/"+depositName+","+startDate+","+endDate);
        Assert.isTrue(dateFormat.parse(startDate).before(dateFormat.parse(endDate))||
                dateFormat.parse(startDate).equals(dateFormat.parse(endDate)),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/report/nameDate/"+depositName+","+startDate+","+endDate,LinkedHashMap.class);
    }

    /**
     * Get Bank Deposits by NAME with depositors from-to Date return Deposit values
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/nameDateReturn/{depositName},{start},{end}
     * @param depositName String - name of the Bank Deposit to return
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date return deposit (endDate > startDate)
     * @return LinkedHashMap<K,V>
     */
    public LinkedHashMap getBankDepositByNameFromToDateReturnDepositWithDepositors(String depositName, String startDate, String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositByNameFromToDateReturnDepositWithDepositors {}",
                host+"/deposit/report/nameDateReturn/"+depositName+","+startDate+","+endDate);
        Assert.isTrue(dateFormat.parse(startDate).before(dateFormat.parse(endDate))||
                dateFormat.parse(startDate).equals(dateFormat.parse(endDate)),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/report/nameDateReturn/"+depositName+","+startDate+","+endDate,LinkedHashMap.class);
    }

    /**
     * Get Bank Deposits by ID with depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/id/{depositId}
     * @param depositId Long - depositId of the Bank Deposit to return
     * @return LinkedHashMap<K,V>
     */
    public LinkedHashMap getBankDepositByIdWithDepositors(Long depositId){
        LOGGER.debug("getBankDepositByIdWithDepositors {}",host+"/deposit/report/id/"+depositId);
        return restTemplate.getForObject(host+"/deposit/report/id/"+depositId,LinkedHashMap.class);
    }

    /**
     * Get Bank Deposits by ID with depositors from-to Date Deposit values
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/idDate/{depositId},{start},{end}
     * @param depositId Long - depositId of the Bank Deposit to return
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (endDate > startDate)
     * @return LinkedHashMap<K,V>
     */
    public LinkedHashMap getBankDepositByIdFromToDateDepositWithDepositors(Long depositId, String startDate, String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositByIdFromToDateDepositWithDepositors {}",
                host+"/deposit/report/idDate/"+depositId+","+startDate+","+endDate);
        Assert.isTrue(dateFormat.parse(startDate).before(dateFormat.parse(endDate))||
                dateFormat.parse(startDate).equals(dateFormat.parse(endDate)),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/report/idDate/"+depositId+","+startDate+","+endDate,LinkedHashMap.class);
    }

    /**
     * Get Bank Deposits by ID with depositors from-to Date return Deposit values
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/idDateReturn/{depositId},{start},{end}
     * @param depositId Long - depositId of the Bank Deposit to return
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date return deposit (endDate > startDate)
     * @return LinkedHashMap<K,V>
     */
    public LinkedHashMap getBankDepositByIdFromToDateReturnDepositWithDepositors(Long depositId, String startDate, String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositByIdFromToDateReturnDepositWithDepositors {}",
                host+"/deposit/report/idDate/"+depositId+","+startDate+","+endDate);
        Assert.isTrue(dateFormat.parse(startDate).before(dateFormat.parse(endDate))||
                dateFormat.parse(startDate).equals(dateFormat.parse(endDate)),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/report/idDateReturn/"+depositId+","+startDate+","+endDate,LinkedHashMap.class);
    }

    /**
     * Get Bank Deposit with depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/all
     * @return LinkedHashMap[]
     */
    public LinkedHashMap[] getBankDepositsWithDepositors(){
        LOGGER.debug("getBankDepositsWithDepositors {}", host+"/deposit/report/all");
        return restTemplate.getForObject(host+"/deposit/report/all",LinkedHashMap[].class);
    }

    /**
     * Get Bank Deposit from-to Date Deposit with depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/allDate/{start},{end}
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (startDate < endDate)
     * @return LinkedHashMap[]
     */
    public LinkedHashMap[] getBankDepositsFromToDateDepositWithDepositors(String startDate, String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositsFromToDateDepositWithDepositors {}",
                host+"/deposit/report/allDate/"+startDate+","+endDate);
        Assert.isTrue(dateFormat.parse(startDate).before(dateFormat.parse(endDate))||
                dateFormat.parse(startDate).equals(dateFormat.parse(endDate)),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/report/allDate/"+startDate+","+endDate,LinkedHashMap[].class);
    }

    /**
     * Get Bank Deposit from-to Date return Deposit with depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/allDateReturn/{start},{end}
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date return deposit (startDate < endDate)
     * @return LinkedHashMap[]
     */
    public LinkedHashMap[] getBankDepositsFromToDateReturnDepositWithDepositors(String startDate, String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositsFromToDateReturnDepositWithDepositors {}",
                host+"/deposit/report/allDateReturn/"+startDate+","+endDate);
        Assert.isTrue(dateFormat.parse(startDate).before(dateFormat.parse(endDate))||
                dateFormat.parse(startDate).equals(dateFormat.parse(endDate)),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/report/allDateReturn/"+startDate+","+endDate,LinkedHashMap[].class);
    }

    /**
     * Get Bank Deposit by Currency with depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/currency/{depsitCurrency}
     * @param depositCurrency String - Currency of the Bank Deposit to return
     * @return LinkedHashMap[]
     */
    public LinkedHashMap[] getBankDepositsByCurrencyWithDepositors(String depositCurrency){
        LOGGER.debug("getBankDepositsByCurrencyWithDepositors {}",host+"/deposit/report/currency/"+depositCurrency);
        return  restTemplate.getForObject(host+"/deposit/report/currency/"+depositCurrency, LinkedHashMap[].class);
    }

    /**
     * Get Bank Deposit from-to Date Deposit by Currency with depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/currencyDate/{depsitCurrency},{start},{end}
     * @param currency String - Currency of the Bank Deposit to return
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (startDate < endDate)
     * @return LinkedHashMap[]
     */
    public LinkedHashMap[] getBankDepositsByCurrencyFromToDateDepositWithDepositors(String currency, String startDate, String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositsByCurrencyFromToDateDepositWithDepositors {}",
                host+"/deposit/report/currencyDate/"+currency+","+startDate+","+endDate);
        Assert.isTrue(dateFormat.parse(startDate).before(dateFormat.parse(endDate))||
                dateFormat.parse(startDate).equals(dateFormat.parse(endDate)),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/report/currencyDate/"+currency+","+startDate+","+endDate,LinkedHashMap[].class);
    }

    /**
     * Get Bank Deposit from-to Date return Deposit by Currency with depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/currencyDateReturn/{depsitCurrency},{start},{end}
     * @param currency String - Currency of the Bank Deposit to return
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date return deposit (startDate < endDate)
     * @return Map[]
     */
    public LinkedHashMap[] getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(String currency, String startDate, String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors {}",
                host+"/deposit/report/currencyDateReturn/"+currency+","+startDate+","+endDate);
        Assert.isTrue(dateFormat.parse(startDate).before(dateFormat.parse(endDate))||
                dateFormat.parse(startDate).equals(dateFormat.parse(endDate)),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/report/currencyDateReturn/"+currency+","+startDate+","+endDate,LinkedHashMap[].class);
    }

    /**
     * Add Bank Deposit
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit
     * @param deposit BankDeposit - Bank Deposit to be stored in the database
     */
    public Object addDeposit(BankDeposit deposit){
        LOGGER.debug("addDeposit {}, {}",host+"/deposit",deposit);
        return restTemplate.postForObject(host+"/deposit",deposit,String.class);
    }

    /**
     * Updating Bank Deposit
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit
     * @param deposit BankDeposit - Bank Deposit to be stored in the database
     */
    public void updateDeposit(BankDeposit deposit){
        LOGGER.debug("updateDeposit {}, {}",host+"/deposit",deposit);
        restTemplate.put(host+"/deposit",deposit);
    }

    /**
     * Deleting Bank Deposit by ID
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/id/{deposiId}
     * @param depositId Long - id of the Bank Deposit to be removed
     */
    public void removeDeposit(Long depositId){
        LOGGER.debug("removeDeposit {}",host+"/deposit/id/"+depositId);
        restTemplate.delete(host+"/deposit/id/"+depositId);
    }

    /**
     * Get all Bank Depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/depositor/all
     * @return
     */
    public BankDepositor[] getBankDepositors(){
        LOGGER.debug("getBankDepositors {}",host+"/depositor/all");
        return restTemplate.getForObject(host+"/depositor/all",BankDepositor[].class);
    }

    /**
     * Get all Bank Depositors from-to Date Deposit
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/depositor/allDate/{start},{end}
     * @param start Date - start value of the date deposit (startDate < endDate)
     * @param end Date - end value of the date deposit (startDate < endDate)
     * @return BankDepositor[]
     */
    public BankDepositor[] getBankDepositorsFromToDateDeposit(Date start, Date end){
        LOGGER.debug("getBankDepositorsFromToDateDeposit {}",
                host+"/depositor/allDate/"+dateFormat.format(start)+","+dateFormat.format(end));
        Assert.isTrue(start.before(end)||start.equals(end),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/depositor/allDate/"+dateFormat.format(start)+","+dateFormat.format(end),BankDepositor[].class);
    }

    /**
     * Get all Bank Depositors from-to Date return Deposit
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/depositor/allDateReturn/{start},{end}
     * @param start Date - start value of the date return deposit (startDate < endDate)
     * @param end Date - end value of the date return deposit (startDate < endDate)
     * @return BankDepositor[]
     */
    public BankDepositor[] getBankDepositorsFromToDateReturnDeposit(Date start, Date end){
        LOGGER.debug("getBankDepositorsFromToDateReturnDeposit {}",
                host+"/depositor/allDateReturn/"+dateFormat.format(start)+","+dateFormat.format(end));
        Assert.isTrue(start.before(end)||start.equals(end),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/depositor/allDateReturn/"+dateFormat.format(start)+","+dateFormat.format(end),BankDepositor[].class);
    }

    /**
     * Get Bank Depositor by ID
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/depositor/id/{depositorId}
     * @param depositorId Long - id of the Bank Depositor to return
     * @return BankDepositor
     */
    public BankDepositor getBankDepositorById(Long depositorId){
        LOGGER.debug("getBankDepositorById {}",host+"/depositor/id/"+depositorId);

        return restTemplate.getForObject(host+"/depositor/id/"+depositorId,BankDepositor.class);
    }

    /**
     * Get Bank Depositor by ID Deposit
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/depositor/idDeposit/{depositId}
     * @param depositId Long - id of the Bank Deposit
     * @return BankDepositor[]
     */
    public BankDepositor[] getBankDepositorByIdDeposit(Long depositId){
        LOGGER.debug("getBankDepositorByIdDeposit {}",host+"/depositor/idDeposit/"+depositId);

        return restTemplate.getForObject(host+"/depositor/idDeposit/"+depositId,BankDepositor[].class);
    }

    /**
     * Get Bank Depositor by Name Deposit
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/depositor/name/{depositorName}
     * @param depositorName String - name of the Bank Depositor to return
     * @return BankDepositor
     */
    public BankDepositor getBankDepositorByName(String depositorName){
        LOGGER.debug("getBankDepositorByName {}",host+"/depositor/name/"+depositorName);

        return restTemplate.getForObject(host+"/depositor/name/"+depositorName,BankDepositor.class);
    }

    /**
     * Adding Bank Depositor
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/depositor/{depositId}
     * @param depositId id of Bank Deposit
     * @param depositor BankDepositor - Bank Depositor to be inserted to the database
     */
    public void addBankDepositor(Long depositId, BankDepositor depositor){
        LOGGER.debug("addBankDepositor {}, {}",host+"/depositor/"+depositId, depositor);
        restTemplate.postForObject(host+"/depositor/"+depositId,depositor,Object.class);
    }

    /**
     * Updating Bank Depositor
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/depositor
     * @param depositor BankDepositor - Bank Depositor to be stored in the database
     */
    public void updateBankDepositor(BankDepositor depositor){
        LOGGER.debug("updateBankDepositor {}, {}",host+"/depositor", depositor);
        restTemplate.put(host+"/depositor",depositor,Object.class);
    }

    /**
     * Deleting Bank Depositor by ID
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/depositor/{depositorId}
     * @param depositorId Long - id of the Bank Depositor to be removed
     */
    public void removeBankDepositor(Long depositorId){
        LOGGER.debug("removeBankDepositor {}",host+"/depositor/"+depositorId);
        restTemplate.delete(host+"/depositor/"+depositorId,Object.class);
    }
}

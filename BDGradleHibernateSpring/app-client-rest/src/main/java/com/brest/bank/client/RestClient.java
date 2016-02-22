package com.brest.bank.client;

import com.brest.bank.domain.BankDeposit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RestClient {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final String ERROR_FROM_TO_PARAM = "The first parameter should be less than the second";
    private static final Logger LOGGER = LogManager.getLogger();
    private String host;

    private RestTemplate restTemplate = new RestTemplate();

    public RestClient(String host){
        this.host = host;
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(new MappingJackson2HttpMessageConverter());
        restTemplate.setMessageConverters(converters);
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
     * @param start Date - start value of the date deposit (startDate < endDate)
     * @param end Date - end value of the date deposit (endDate > startDate)
     * @return BankDeposit[]
     */
    public BankDeposit[] getBankDepositsFromToDateDeposit(Date start, Date end){
        LOGGER.debug("getBankDepositsFromToDateDeposit {}",host+"/deposit/date/"+dateFormat.format(start)+","+dateFormat.format(end));
        Assert.isTrue(start.before(end),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/date/"+dateFormat.format(start)+","+dateFormat.format(end),BankDeposit[].class);
    }

    /**
     * Get Bank Deposits from-to Date return Deposit values
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/returnDate/{start},{end}
     * @param start Date - start value of the date return deposit (startDate < endDate)
     * @param end Date - end value of the date return deposit (endDate > startDate)
     * @return BankDeposit[]
     */
    public BankDeposit[] getBankDepositsFromToDateReturnDeposit(Date start, Date end){
        LOGGER.debug("getBankDepositsFromToDateReturnDeposit {}",host+"/deposit/returnDate/"+dateFormat.format(start)+","+dateFormat.format(end));
        Assert.isTrue(start.before(end)||start.equals(end),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/returnDate/"+dateFormat.format(start)+","+dateFormat.format(end),BankDeposit[].class);
    }

    /**
     * Get Bank Deposits by NAME with depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/name/{depositName}
     * @param depositName String - name of the Bank Deposit to return
     * @return Map<K,V>
     */
    public Map getBankDepositByNameWithDepositors(String depositName){
        LOGGER.debug("getBankDepositByNameWithDepositors {}",host+"/deposit/report/name/"+depositName);
        return restTemplate.getForObject(host+"/deposit/report/name/"+depositName,Map.class);
    }

    /**
     * Get Bank Deposits by NAME with depositors from-to Date Deposit values
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/nameDate/{depositName},{start},{end}
     * @param depositName String - name of the Bank Deposit to return
     * @param start Date - start value of the date deposit (startDate < endDate)
     * @param end String - end value of the date deposit (endDate > startDate)
     * @return Map<K,V>
     */
    public Map getBankDepositByNameFromToDateDepositWithDepositors(String depositName, Date start, Date end){
        LOGGER.debug("getBankDepositByNameFromToDateDepositWithDepositors {}",
                host+"/deposit/report/nameDate/"+depositName+","+dateFormat.format(start)+","+dateFormat.format(end));
        Assert.isTrue(start.before(end)||start.equals(end));

        return restTemplate.getForObject(host+"/deposit/report/nameDate/"+depositName+","+dateFormat.format(start)+","+dateFormat.format(end),Map.class);
    }

    /**
     * Get Bank Deposits by NAME with depositors from-to Date return Deposit values
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/nameDateReturn/{depositName},{start},{end}
     * @param depositName String - name of the Bank Deposit to return
     * @param start Date - start value of the date return deposit (startDate < endDate)
     * @param end String - end value of the date return deposit (endDate > startDate)
     * @return Map<K,V>
     */
    public Map getBankDepositByNameFromToDateReturnDepositWithDepositors(String depositName, Date start, Date end){
        LOGGER.debug("getBankDepositByNameFromToDateReturnDepositWithDepositors {}",
                host+"/deposit/report/nameDateReturn/"+depositName+","+dateFormat.format(start)+","+dateFormat.format(end));
        Assert.isTrue(start.before(end)||start.equals(end),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/report/nameDateReturn/"+depositName+","+dateFormat.format(start)+","+dateFormat.format(end),Map.class);
    }

    /**
     * Get Bank Deposits by ID with depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/id/{depositId}
     * @param depositId Long - depositId of the Bank Deposit to return
     * @return Map<K,V>
     */
    public Map getBankDepositByIdWithDepositors(Long depositId){
        LOGGER.debug("getBankDepositByIdWithDepositors {}",host+"/deposit/report/id/"+depositId);
        return restTemplate.getForObject(host+"/deposit/report/id/"+depositId,Map.class);
    }

    /**
     * Get Bank Deposits by ID with depositors from-to Date Deposit values
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/idDate/{depositId},{start},{end}
     * @param depositId Long - depositId of the Bank Deposit to return
     * @param start Date - start value of the date deposit (startDate < endDate)
     * @param end Date - end value of the date deposit (endDate > startDate)
     * @return
     */
    public Map getBankDepositByIdFromToDateDepositWithDepositors(Long depositId, Date start, Date end){
        LOGGER.debug("getBankDepositByIdFromToDateDepositWithDepositors {}",
                host+"/deposit/report/idDate/"+depositId+","+dateFormat.format(start)+","+dateFormat.format(end));
        Assert.isTrue(start.before(end)||start.equals(end),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/report/idDate/"+depositId+","+dateFormat.format(start)+","+dateFormat.format(end),Map.class);
    }

    /**
     * Get Bank Deposits by ID with depositors from-to Date return Deposit values
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/idDateReturn/{depositId},{start},{end}
     * @param depositId Long - depositId of the Bank Deposit to return
     * @param start Date - start value of the date return deposit (startDate < endDate)
     * @param end Date - end value of the date return deposit (endDate > startDate)
     * @return
     */
    public Map getBankDepositByIdFromToDateReturnDepositWithDepositors(Long depositId, Date start, Date end){
        LOGGER.debug("getBankDepositByIdFromToDateReturnDepositWithDepositors {}",
                host+"/deposit/report/idDate/"+depositId+","+dateFormat.format(start)+","+dateFormat.format(end));
        Assert.isTrue(start.before(end)||start.equals(end),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/report/idDateReturn/"+depositId+","+dateFormat.format(start)+","+dateFormat.format(end),Map.class);
    }

    /**
     * Get Bank Deposit with depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/all
     * @return Map[]
     */
    public Map[] getBankDepositsWithDepositors(){
        LOGGER.debug("getBankDepositsWithDepositors {}", host+"/deposit/report/all");
        return restTemplate.getForObject(host+"/deposit/report/all",Map[].class);
    }

    /**
     * Get Bank Deposit from-to Date Deposit with depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/allDate/{start},{end}
     * @param start Date - start value of the date deposit (startDate < endDate)
     * @param end Date - end value of the date deposit (startDate < endDate)
     * @return Map[]
     */
    public Map[] getBankDepositsFromToDateDepositWithDepositors(Date start, Date end){
        LOGGER.debug("getBankDepositsFromToDateDepositWithDepositors {}",
                host+"/deposit/report/allDate/"+dateFormat.format(start)+","+dateFormat.format(end));
        Assert.isTrue(start.before(end)||start.equals(end),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/report/allDate/"+dateFormat.format(start)+","+dateFormat.format(end),Map[].class);
    }

    /**
     * Get Bank Deposit from-to Date return Deposit with depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/allDateReturn/{start},{end}
     * @param start Date - start value of the date return deposit (startDate < endDate)
     * @param end Date - end value of the date return deposit (startDate < endDate)
     * @return Map[]
     */
    public Map[] getBankDepositsFromToDateReturnDepositWithDepositors(Date start, Date end){
        LOGGER.debug("getBankDepositsFromToDateReturnDepositWithDepositors {}",
                host+"/deposit/report/allDateReturn/"+dateFormat.format(start)+","+dateFormat.format(end));
        Assert.isTrue(start.before(end)||start.equals(end),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/report/allDateReturn/"+dateFormat.format(start)+","+dateFormat.format(end),Map[].class);
    }

    /**
     * Get Bank Deposit by Currency with depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/currency/{depsitCurrency}
     * @param depositCurrency String - Currency of the Bank Deposit to return
     * @return Map[]
     */
    public Map[] getBankDepositsByCurrencyWithDepositors(String depositCurrency){
        LOGGER.debug("getBankDepositsByCurrencyWithDepositors {}",host+"/deposit/report/currency/"+depositCurrency);
        return  restTemplate.getForObject(host+"/deposit/report/currency/"+depositCurrency, Map[].class);
    }

    /**
     * Get Bank Deposit from-to Date Deposit by Currency with depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/currencyDate/{depsitCurrency},{start},{end}
     * @param currency String - Currency of the Bank Deposit to return
     * @param start Date - start value of the date deposit (startDate < endDate)
     * @param end Date - end value of the date deposit (startDate < endDate)
     * @return Map[]
     */
    public Map[] getBankDepositsByCurrencyFromToDateDepositWithDepositors(String currency, Date start, Date end){
        LOGGER.debug("getBankDepositsByCurrencyFromToDateDepositWithDepositors {}",
                host+"/deposit/report/currencyDate/"+currency+","+dateFormat.format(start)+","+dateFormat.format(end));
        Assert.isTrue(start.before(end)||start.equals(end),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/report/currencyDate/"+currency+","+dateFormat.format(start)+","+dateFormat.format(end),Map[].class);
    }

    /**
     * Get Bank Deposit from-to Date return Deposit by Currency with depositors
     *
     * @link http://<host:port>/<project_name-version>/<client_name>/deposit/report/currencyDateReturn/{depsitCurrency},{start},{end}
     * @param currency String - Currency of the Bank Deposit to return
     * @param start Date - start value of the date return deposit (startDate < endDate)
     * @param end Date - end value of the date return deposit (startDate < endDate)
     * @return Map[]
     */
    public Map[] getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(String currency, Date start, Date end){
        LOGGER.debug("getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors {}",
                host+"/deposit/report/currencyDateReturn/"+currency+","+dateFormat.format(start)+","+dateFormat.format(end));
        Assert.isTrue(start.before(end)||start.equals(end),ERROR_FROM_TO_PARAM);

        return restTemplate.getForObject(host+"/deposit/report/currencyDateReturn/"+currency+","+dateFormat.format(start)+","+dateFormat.format(end),Map[].class);
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
}

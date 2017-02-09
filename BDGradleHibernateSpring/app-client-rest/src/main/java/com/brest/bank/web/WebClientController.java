package com.brest.bank.web;

import com.brest.bank.client.RestClient;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.json.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/deposit")
public class WebClientController {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    private static final Logger LOGGER = LogManager.getLogger();
    @Autowired
    RestClient restClient;;
    private BankDeposit deposit = new BankDeposit(null,"",0,0,"",0,"",new HashSet());;
private BankDepositor depositor = new BankDepositor(null,"", new Date(),0,0,0, new Date(),0,null);
    private LinkedHashMap<String, Object> deposits;
    private List<BankDepositor> depositors;
    private String restRequest = "";
    private String host;

    /**
     * Get all Bank deposits
     *
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     */
    @ResponseBody
    @RequestMapping(value="/all",method= RequestMethod.GET)
    public ModelAndView getDeposits() {
        LOGGER.debug("getDeposits()");
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("deposit",deposit);
            view.addObject("depositor",depositor);
        try {
            BankDeposit[] deposits = restClient.getBankDeposits();
            JsonObject jsonDeposit;
            JsonArray jsonDeposits = new JsonArray();
            try {
                for (BankDeposit d : deposits) {
                    jsonDeposit = new JsonObject();
                    jsonDeposit.addProperty("depositId", d.getDepositId());
                    jsonDeposit.addProperty("depositName", d.getDepositName());
                    jsonDeposit.addProperty("depositMinTerm", d.getDepositMinTerm());
                    jsonDeposit.addProperty("depositMinAmount", d.getDepositMinAmount());
                    jsonDeposit.addProperty("depositCurrency", d.getDepositCurrency());
                    jsonDeposit.addProperty("depositInterestRate", d.getDepositInterestRate());
                    jsonDeposit.addProperty("depositAddConditions", d.getDepositAddConditions());

                    jsonDeposits.add(jsonDeposit);
                }
            }catch (Exception e){
                jsonDeposits.add("JSON create error: \n" + e.getMessage());
            }

            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",Arrays.asList(deposits));
            view.addObject("responseJson",jsonDeposits);

        } catch (Exception e){
            LOGGER.error("getDeposits(), Exception:{}", e.toString());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());

        }
        return view;
    }

    /**
     * Get Bank Deposit by id deposit
     *
     * @param depositId Long - id of the Bank Deposit to return
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     */
    @RequestMapping(value = "/id/{depositId}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getDepositById(@PathVariable Long depositId) {
        LOGGER.debug("getDepositById(id={})",depositId);
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor",depositor);
        try {
            deposit = restClient.getDepositById(depositId);
            JsonObject jsonDeposit = new JsonObject();
            try {
                jsonDeposit.addProperty("depositId", deposit.getDepositId());
                jsonDeposit.addProperty("depositName", deposit.getDepositName());
                jsonDeposit.addProperty("depositMinTerm", deposit.getDepositMinTerm());
                jsonDeposit.addProperty("depositMinAmount", deposit.getDepositMinAmount());
                jsonDeposit.addProperty("depositCurrency", deposit.getDepositCurrency());
                jsonDeposit.addProperty("depositInterestRate", deposit.getDepositInterestRate());
                jsonDeposit.addProperty("depositAddConditions", deposit.getDepositAddConditions());
            }catch (Exception e){
                jsonDeposit.addProperty("JSON create error: ", e.getMessage());
            }
            view.addObject("deposit",deposit);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",deposit);
            view.addObject("responseJson",jsonDeposit);
        } catch (Exception e) {
            LOGGER.error("getDepositById({}), Exception:{}", depositId, e.toString());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposit by name
     *
     * @param depositName String - name of the Bank Deposit to return
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     */
    @RequestMapping(value = "/name/{depositName}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getDepositByName(@PathVariable String depositName) {
        LOGGER.debug("getDepositByName(name={})",depositName);
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor",depositor);
        try {
            deposit = restClient.getDepositByName(depositName);
            JsonObject jsonDeposit;
            jsonDeposit = new JsonObject();
            try {
                jsonDeposit.addProperty("depositId", deposit.getDepositId());
                jsonDeposit.addProperty("depositName", deposit.getDepositName());
                jsonDeposit.addProperty("depositMinTerm", deposit.getDepositMinTerm());
                jsonDeposit.addProperty("depositMinAmount", deposit.getDepositMinAmount());
                jsonDeposit.addProperty("depositCurrency", deposit.getDepositCurrency());
                jsonDeposit.addProperty("depositInterestRate", deposit.getDepositInterestRate());
                jsonDeposit.addProperty("depositAddConditions", deposit.getDepositAddConditions());
            }catch (Exception e){
                jsonDeposit.addProperty("JSON create error: ", e.getMessage());
            }
            view.addObject("deposit",deposit);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",deposit);
            view.addObject("responseJson",jsonDeposit);
        } catch (Exception e) {
            LOGGER.error("getDepositByName({}), Exception:{}", depositName, e.toString());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposits by currency
     *
     * @param currency String - currency of the Bank Deposit to return
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     * currency in the database
     */
    @ResponseBody
    @RequestMapping(value = "/currency/{currency}",method = RequestMethod.GET)
    public ModelAndView getBankDepositsByCurrency(@PathVariable String currency){
        LOGGER.debug("getBankDepositsByCurrency(currency={})",currency);
        ObjectMapper mapper = new ObjectMapper();
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor",depositor);
        try{
            BankDeposit[] deposits = restClient.getBankDepositsByCurrency(currency);
            String jsonInString = "";
            try {
                // Convert object to JSON string
                mapper.writerFor(BankDeposit.class);

                // Convert object to JSON string
                jsonInString = mapper.writeValueAsString(deposits);

            } catch (JsonGenerationException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            view.addObject("deposit",deposits[0]);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",Arrays.asList(deposits));
            view.addObject("responseJson",jsonInString);
        }catch (Exception e){
            LOGGER.error("getBankDepositsByCurrency(currency={}), Exception:{}",currency,e.toString());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposits by interest rate
     *
     * @param rate - Integer - interest rate of the Bank Deposits to return
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     */
    @ResponseBody
    @RequestMapping(value = "/rate/{rate}",method = RequestMethod.GET)
    public ModelAndView getBankDepositsByInterestRate(@PathVariable Integer rate){
        LOGGER.debug("getBankDepositsByInterestRate(rate={})",rate);
        JSONArray depositsJson = new JSONArray();
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor",depositor);
        try{
            BankDeposit[] deposits = restClient.getBankDepositsByInterestRate(rate);
            try{
                for (BankDeposit d:deposits
                     ) {
                    depositsJson.put(d);
                }
            }catch (Exception e){
                LOGGER.error("getBankDepositsByInterestRate(rate={}), \nError parsing JSON. Exception:{}",rate,e.getMessage());
            }
            view.addObject("deposit",deposits[0]);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",Arrays.asList(deposits));
            view.addObject("responseJson",depositsJson);
        }catch (Exception e){
            LOGGER.error("getBankDepositsByInterestRate(rate={}), Exception:{}",rate,e.getMessage());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposits from-to MIN TERM values
     *
     * @param fromTerm Integer - start value of the min term (count month)
     * @param toTerm Integer - end value of the min term (count month)
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     */
    @ResponseBody
    @RequestMapping(value = "/term/{fromTerm},{toTerm}", method = RequestMethod.GET)
    public ModelAndView getBankDepositsFromToMinTerm(@PathVariable Integer fromTerm,
                                                     @PathVariable Integer toTerm){
        LOGGER.debug("getBankDepositsFromToMinTerm(fromTerm={}, toTerm={})",fromTerm,toTerm);
        JSONArray depositsJson = new JSONArray();
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor",depositor);
        try{
            BankDeposit[] deposits = restClient.getBankDepositsFromToMinTerm(fromTerm, toTerm);
            try{
                for (BankDeposit d:deposits
                        ) {
                    depositsJson.put(d);
                }
            }catch (Exception e){
                LOGGER.error("getBankDepositsFromToMinTerm(fromTerm={}, toTerm={}), Error parsing JSON. Exception:{}",fromTerm,
                        toTerm,e.getMessage());
            }
            view.addObject("deposit",deposits[0]);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",Arrays.asList(deposits));
            view.addObject("responseJson",depositsJson);
        }catch (Exception e){
            LOGGER.error("getBankDepositsFromToMinTerm(fromTerm={}, toTerm={}), Exception:{}",fromTerm,
                    toTerm,e.getMessage());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposits from-to INTEREST RATE values
     *
     * @param startRate Integer - start value of the interest rate (0% < startRate <= 100%)
     * @param endRate Integer - end value of the interest rate (0% < endRate <= 100%)
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     */
    @ResponseBody
    @RequestMapping(value = "/rateBetween/{startRate},{endRate}", method = RequestMethod.GET)
    public ModelAndView getBankDepositsFromToInterestRate(@PathVariable Integer startRate,
                                                          @PathVariable Integer endRate) throws ParseException{
        LOGGER.debug("getBankDepositsFromToInterestRate(from={}, to={})",startRate,endRate);
        JSONArray depositsJson = new JSONArray();
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor", depositor);
        try{
            BankDeposit[] deposits = restClient.getBankDepositsFromToInterestRate(startRate, endRate);
            try{
                for (BankDeposit d:deposits
                    ) {
                        depositsJson.put(d);
                }
            }catch (Exception e){
                LOGGER.error("getBankDepositsFromInterestRate(from={}, to={}), Error parsing JSON. Exception:{}",startRate,
                        endRate,e.getMessage());
            }
            view.addObject("deposit",deposits[0]);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",Arrays.asList(deposits));
            view.addObject("responseJson",depositsJson);
        }catch (Exception e){
            LOGGER.error("getBankDepositsFromInterestRate(from={}, to={}), Exception:{}",startRate,
                    endRate,e.getMessage());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposits from-to Date Deposit values
     *
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (endDate > startDate)
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     */
    @ResponseBody
    @RequestMapping(value = "/date/{startDate},{endDate}", method = RequestMethod.GET)
    public ModelAndView getBankDepositsFromToDateDeposit(@PathVariable String startDate,
                                                         @PathVariable String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositsFromToDateDeposit(startDate={},endDate={})",startDate,endDate);
        JSONArray depositsJson = new JSONArray();
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor", depositor);
        try{
            BankDeposit[] deposits = restClient.getBankDepositsFromToDateDeposit(startDate,endDate);
            try{
                for (BankDeposit d:deposits
                        ) {
                    depositsJson.put(d);
                }
            }catch (Exception e){
                LOGGER.error("getBankDepositsFromToDateDeposit(startDate={},endDate={}), Error parsing JSON. Exception:{}",startDate,
                        endDate,e.getMessage());
            }
            view.addObject("deposit",deposits[0]);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",Arrays.asList(deposits));
            view.addObject("responseJson",depositsJson);
        }catch (Exception e){
            LOGGER.error("getBankDepositsFromToDateDeposit(startDate={},endDate={}), Exception:{}",startDate,
                    endDate,e.getMessage());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposits from-to Date Return Deposit values
     *
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date return deposit (endDate > startDate)
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/returnDate/{startDate},{endDate}", method = RequestMethod.GET)
    public ModelAndView getBankDepositsFromToDateReturnDeposit(@PathVariable String startDate,
                                                               @PathVariable String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositsFromToDateReturnDeposit(startDate={},endDate={})",startDate,endDate);
        JSONArray depositsJson = new JSONArray();
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor", depositor);
        try{
            BankDeposit[] deposits = restClient
                    .getBankDepositsFromToDateReturnDeposit(startDate,endDate);
            try{
                for (BankDeposit d:deposits
                        ) {
                    depositsJson.put(d);
                }
            }catch (Exception e){
                LOGGER.error("getBankDepositsFromToDateReturnDeposit(startDate={},endDate={}), Error parsing JSON. Exception:{}",startDate,
                        endDate,e.getMessage());
            }
            view.addObject("deposit",deposits[0]);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",Arrays.asList(deposits));
            view.addObject("responseJson",depositsJson);
        }catch (Exception e){
            LOGGER.error("getBankDepositsFromToDateReturnDeposit(startDate={},endDate={}), Exception:{}",
                    startDate,endDate,e.getMessage());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposits by NAME with depositors
     *
     * @param name String - name of the Bank Deposit to return
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     */
    @ResponseBody
    @RequestMapping(value = "/report/name/{name}",method = RequestMethod.GET)
    public ModelAndView getBankDepositByNameWithDepositors(@PathVariable String name){
        LOGGER.debug("getBankDepositByNameWithDepositors(name={})",name);
        JSONObject jsonObject = new JSONObject();
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor", depositor);
        try{
            LinkedHashMap depositReport = restClient.getBankDepositByNameWithDepositors(name);
            try{
                jsonObject.put("BankDepositReport",depositReport);
            }catch (Exception e){
                LOGGER.error("getBankDepositByNameWithDepositors(name={}), Error parsing JSON. Exception:{}",name,e.getMessage());
            }
            view.addObject("deposit",deposit);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",depositReport);
            view.addObject("responseJson",jsonObject);

        }catch (Exception e){
            LOGGER.error("getBankDepositByNameWithDepositors(name={}),Exception:{}",name,e.getMessage());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposits by NAME with depositors from-to Date Deposit values
     *
     * @param name String - name of the Bank Deposit to return
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (endDate > startDate)
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "report/nameDate/{name},{startDate},{endDate}",method = RequestMethod.GET)
    public ModelAndView getBankDepositByNameFromToDateDepositWithDepositors(@PathVariable String name,
                                                                            @PathVariable String startDate,
                                                                            @PathVariable String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositByNameFromToDateDepositWithDepositors(name={},startDate={},endDate={})",
                name,startDate,endDate);
        JSONObject jsonObject = new JSONObject();
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor", depositor);
        try{
            LinkedHashMap depositReport = restClient.getBankDepositByNameFromToDateDepositWithDepositors(name,
                    startDate,endDate);

            try{
                jsonObject.put("BankDepositReport",depositReport);
            }catch (Exception e){
                LOGGER.error("getBankDepositByNameFromToDateDepositWithDepositors(name={},startDate={},endDate={}), " +
                        "Error parsing JSON. Exception:{}",name,startDate,endDate,e.getMessage());
            }
            view.addObject("deposit",deposit);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",depositReport);
            view.addObject("responseJson",jsonObject);
        }catch (Exception e){
            LOGGER.error("getBankDepositByNameFromToDateDepositWithDepositors(name={},startDate={},endDate={})," +
                    "Exception:{}",name,startDate,endDate,e.getMessage());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposits by NAME with depositors from-to Date Return Deposit values
     *
     * @param name String - name of the Bank Deposit to return
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date return deposit (endDate > startDate)
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/report/nameDateReturn/{name},{startDate},{endDate}", method = RequestMethod.GET)
    public ModelAndView getBankDepositByNameFromToDateReturnDepositWithDepositors(@PathVariable String name,
                                                                                  @PathVariable String startDate,
                                                                                  @PathVariable String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositByNameFromToDateReturnDepositWithDepositors(name={},startDate={},endDate={})",
                name,startDate,endDate);
        JSONObject jsonObject = new JSONObject();
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor", depositor);
        try{
            LinkedHashMap depositReport = restClient.getBankDepositByNameFromToDateReturnDepositWithDepositors(name,
                    startDate,endDate);
            try{
                jsonObject.put("BankDepositReport",depositReport);
            }catch (Exception e){
                LOGGER.error("getBankDepositByNameFromToDateReturnDepositWithDepositors(name={},startDate={},endDate={}), " +
                        "Error parsing JSON. Exception:{}",name,startDate,endDate,e.getMessage());
            }
            view.addObject("deposit",deposit);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",depositReport);
            view.addObject("responseJson",jsonObject);
        }catch (Exception e){
            LOGGER.error("getBankDepositByNameFromToDateReturnDepositWithDepositors(name={},startDate={}," +
                    "endDate={}),Exception:{}",name,startDate,endDate,e.getMessage());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposits by ID with depositors
     *
     * @param id Long - depositId of the Bank Deposit to return
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     * bank depositors
     */
    @ResponseBody
    @RequestMapping(value = "/report/id/{id}",method = RequestMethod.GET)
    public ModelAndView getBankDepositByIdWithDepositors(@PathVariable Long id){
        LOGGER.debug("getBankDepositByIdWithDepositors(id={})",id);
        JSONObject jsonObject = new JSONObject();
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor", depositor);
        try{
            LinkedHashMap depositReport = restClient.getBankDepositByIdWithDepositors(id);
            try{
                jsonObject.put("BankDepositReport",depositReport);
            }catch (Exception e){
                LOGGER.error("getBankDepositByIdWithDepositors(id={}), " +
                        "Error parsing JSON. Exception:{}",id,e.getMessage());
            }
            view.addObject("deposit",deposit);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",depositReport);
            view.addObject("responseJson",jsonObject);
        }catch (Exception e){
            LOGGER.error("getBankDepositByIdWithDepositors(id={}), Exception:{}",id,e.getMessage());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposits by ID with depositors from-to Date Deposit values
     *
     * @param id Long - depositId of the Bank Deposit to return
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (endDate > startDate)
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/report/idDate/{id},{startDate},{endDate}", method = RequestMethod.GET)
    public ModelAndView getBankDepositByIdFromToDateDepositWithDepositors(@PathVariable Long id,
                                                                          @PathVariable String startDate,
                                                                          @PathVariable String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositByIdFromToDateDepositWithDepositors(id={},startDate={},endDate={})",
                id, startDate, endDate);
        JSONObject jsonObject = new JSONObject();
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor", depositor);
        try{
            LinkedHashMap depositReport = restClient.getBankDepositByIdFromToDateDepositWithDepositors(id,
                            startDate,endDate);
            try{
                jsonObject.put("BankDepositReport",depositReport);
            }catch (Exception e){
                LOGGER.error("getBankDepositByIdFromToDateDepositWithDepositors(id={},startDate={},endDate={}), " +
                        "Error parsing JSON. Exception:{}",id,startDate,endDate,e.getMessage());
            }
            view.addObject("deposit",deposit);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",depositReport);
            view.addObject("responseJson",jsonObject);
        }catch (Exception e){
            LOGGER.error("getBankDepositByIdFromToDateDepositWithDepositors(id={},startDate={},endDate={}), " +
                    "Exception:{}",id,startDate,endDate,e.getMessage());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposits by ID with depositors from-to Date Return Deposit values
     *
     * @param id Long - depositId of the Bank Deposit to return
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date return deposit (endDate > startDate)
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/report/idDateReturn/{id},{startDate},{endDate}", method = RequestMethod.GET)
    public ModelAndView getBankDepositByIdFromToDateReturnDepositWithDepositors(@PathVariable Long id,
                                                                                @PathVariable String startDate,
                                                                                @PathVariable String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositByIdFromToDateReturnDepositWithDepositors(id={},startDate={},endDate={})",
                id, startDate, endDate);
        JSONObject jsonObject = new JSONObject();
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor", depositor);
        try{
            LinkedHashMap depositReport = restClient.getBankDepositByIdFromToDateReturnDepositWithDepositors(id,
                    startDate, endDate);
            try{
                jsonObject.put("BankDepositReport",depositReport);
            }catch (Exception e){
                LOGGER.error("getBankDepositByIdFromToDateReturnDepositWithDepositors(id={},startDate={},endDate={}), " +
                        "Error parsing JSON. Exception:{}",id,startDate,endDate,e.getMessage());
            }
            view.addObject("deposit",deposit);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",depositReport);
            view.addObject("responseJson",jsonObject);
        }catch (Exception e){
            LOGGER.error("getBankDepositByIdFromToDateReturnDepositWithDepositors(id={},startDate={},endDate={}), " +
                    "Exception:{}",id,startDate,endDate,e.getMessage());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposit with depositors
     *
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     */
    @ResponseBody
    @RequestMapping(value = "/report/all",method = RequestMethod.GET)
    public ModelAndView getBankDepositsWithDepositors(){
        LOGGER.debug("getBankDepositsWithDepositors()");
        JSONArray depositsJson = new JSONArray();
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor", depositor);
        try{
            LinkedHashMap[] deposits = restClient.getBankDepositsWithDepositors();
            try{
                for (LinkedHashMap d:deposits
                        ) {
                    depositsJson.put(d);
                }
            }catch (Exception e){
                LOGGER.error("getBankDepositsWithDepositors(), Error parsing JSON. Exception:{}", e.getMessage());
            }
            view.addObject("deposit",deposit);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",Arrays.asList(deposits));
            view.addObject("responseJson",depositsJson);
        }catch (Exception e){
            LOGGER.error("getBankDepositsWithDepositors(), Exception;{}",e.getMessage());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposit from-to Date Deposit with depositors
     *
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (startDate < endDate)
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value="/report/allDate/{startDate},{endDate}",method = RequestMethod.GET)
    public ModelAndView getBankDepositsFromToDateDepositWithDepositors(@PathVariable String startDate,
                                                                                          @PathVariable String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositsFromToDateDepositWithDepositors(startDate={},endDate={})",startDate,endDate);
        JSONArray depositsJson = new JSONArray();
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor", depositor);
        try{
            LinkedHashMap[] deposits =
                    restClient.getBankDepositsFromToDateDepositWithDepositors(startDate, endDate);
            try{
                for (LinkedHashMap d:deposits
                        ) {
                    depositsJson.put(d);
                }
            }catch (Exception e){
                LOGGER.error("getBankDepositsFromToDateDepositWithDepositors(startDate={},endDate={}), Error parsing JSON. Exception:{}", startDate,endDate, e.getMessage());
            }
            view.addObject("deposit",deposit);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",Arrays.asList(deposits));
            view.addObject("responseJson",depositsJson);
        }catch (Exception e){
            LOGGER.error("getBankDepositsFromToDateDepositWithDepositors(startDate={},endDate={}), Exception:{}",
                    startDate,endDate,e.getMessage());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposit from-to Date Return Deposit with depositors
     *
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date return deposit (startDate < endDate)
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/report/allDateReturn/{startDate},{endDate}",method = RequestMethod.GET)
    public ModelAndView getBankDepositsFromToDateReturnDepositWithDepositors(@PathVariable String startDate,
                                                                                                @PathVariable String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositsFromToDateReturnDepositWithDepositors(startDate={},endDate={})",startDate,endDate);
        JSONArray depositsJson = new JSONArray();
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor", depositor);
        try{
            LinkedHashMap[] deposits =
                    restClient.getBankDepositsFromToDateReturnDepositWithDepositors(startDate,endDate);
            try{
                for (LinkedHashMap d:deposits
                        ) {
                    depositsJson.put(d);
                }
            }catch (Exception e){
                LOGGER.error("getBankDepositsFromToDateReturnDepositWithDepositors(startDate={},endDate={}), Error parsing JSON. Exception:{}", startDate,endDate, e.getMessage());
            }
            view.addObject("deposit",deposit);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",Arrays.asList(deposits));
            view.addObject("responseJson",depositsJson);

        }catch (Exception e){
            LOGGER.error("getBankDepositsFromToDateReturnDepositWithDepositors(startDate={},endDate={}), Exception:{}",
                    startDate,endDate,e.getMessage());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposit by Currency with depositors
     *
     * @param currency String - Currency of the Bank Deposit to return
     * @return ModelAndView - "indexRestMain" with current deposit, list of all Bank Deposit and Depositors
     */
    @ResponseBody
    @RequestMapping(value = "/report/currency/{currency}",method = RequestMethod.GET)
    public ModelAndView getBankDepositsByCurrencyWithDepositors(@PathVariable String currency){
        LOGGER.debug("getBankDepositsByCurrencyWithDepositors(currency={})",currency);
        JSONArray depositsJson = new JSONArray();
        ModelAndView view = new ModelAndView("indexRestMain");
            view.addObject("HOST", host);
            view.addObject("jsonRequest", "");
            view.addObject("depositor", depositor);
        try{
            LinkedHashMap[] deposits = restClient.getBankDepositsByCurrencyWithDepositors(currency);
            try{
                for (LinkedHashMap d:deposits
                        ) {
                    depositsJson.put(d);
                }
            }catch (Exception e){
                LOGGER.error("getBankDepositsByCurrencyWithDepositors(currency={}), Error parsing JSON. Exception:{}", currency, e.getMessage());
            }
            view.addObject("deposit",deposit);
            view.addObject("responseHeader", "HTTP " + HttpStatus.OK);
            view.addObject("responseRaw",Arrays.asList(deposits));
            view.addObject("responseJson",depositsJson);
        }catch (Exception e){
            LOGGER.error("getBankDepositsByCurrencyWithDepositors(currency={}), Exception:{}",currency,e.getMessage());
            view.addObject("deposit",new BankDeposit());
            view.addObject("responseHeader", HttpStatus.NOT_FOUND);
            view.addObject("responseRaw",e.getMessage());
            view.addObject("responseJson",e.getMessage());
        }
        return view;
    }

    /**
     * Get Bank Deposit from-to Date Deposit by Currency with depositors
     *
     * @param currency String - Currency of the Bank Deposit to return
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (startDate < endDate)
     * @return ResponseEntity(LinkedHashMap[]) a list of all bank deposits with a report on all relevant
     * bank depositors with the specified task`s date deposit
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/report/currencyDate/{currency},{startDate},{endDate}", method = RequestMethod.GET)
    public ResponseEntity<LinkedHashMap[]> getBankDepositsByCurrencyFromToDateDepositWithDepositors(@PathVariable String currency,
                                                                                              @PathVariable String startDate,
                                                                                              @PathVariable String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositsByCurrencyFromToDateDepositWithDepositors(currency={},startDate={},endDate={})",
                currency,startDate,endDate);
        try{
            LinkedHashMap[] deposits = restClient.getBankDepositsByCurrencyFromToDateDepositWithDepositors(currency,
                    startDate, endDate);
            return new ResponseEntity<LinkedHashMap[]>(deposits,HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositsByCurrencyFromToDateDepositWithDepositors(currency={},startDate={},endDate={}), " +
                    "Exception:{}",currency,startDate,endDate);
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Deposit from-to Date Return Deposit by Currency with depositors
     *
     * @param currency String - Currency of the Bank Deposit to return
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (startDate < endDate)
     * @return ResponseEntity(LinkedHashMap[]) a list of all bank deposits with a report on all relevant
     * bank depositors with the specified task`s date deposit
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/report/currencyDateReturn/{currency},{startDate},{endDate}", method = RequestMethod.GET)
    public ResponseEntity<LinkedHashMap[]> getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(@PathVariable String currency,
                                                                                                          @PathVariable String startDate,
                                                                                                          @PathVariable String endDate)
            throws ParseException{
        LOGGER.debug("getBankDepositsByCurrencyFromToDateDepositWithDepositors(currency={},startDate={},endDate={})",
                currency,startDate,endDate);
        try{
            LinkedHashMap[] deposits = restClient.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(currency,
                    startDate, endDate);
            return new ResponseEntity<LinkedHashMap[]>(deposits,HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(currency={},startDate={},endDate={}), " +
                    "Exception:{}",currency,startDate,endDate);
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Adding Bank Deposit
     *
     * @param deposit BankDeposit - Bank Deposit to be inserted to the database
     * @return ResponseEntity
     */
    @ResponseBody
    @RequestMapping(method= RequestMethod.POST)
    public ResponseEntity addDeposit(@RequestBody BankDeposit deposit) {
        LOGGER.debug("addDeposit({})", deposit);
        try {
            Assert.notNull(deposit,"Can not be added to the database NULL deposit");
            Assert.notNull(deposit.getDepositName(),"Can not be added to the database Empty deposit");
            restClient.addDeposit(deposit);
            return new ResponseEntity("a bank deposit created", HttpStatus.CREATED);
        }catch (Exception e){
            LOGGER.error("addDeposit({}), Exception:{}", deposit, e.toString());
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }

    /**
     * Updating Bank Deposit
     *
     * @param deposit BankDeposit - Bank Deposit to be stored in the database
     * @return ResponseEntity
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateDeposit(@RequestBody BankDeposit deposit) {
        LOGGER.debug("updateDeposit({})", deposit);
        try {
            Assert.notNull(deposit,"Can not be updated NULL deposit");
            Assert.notNull(deposit.getDepositName(),"Can not be updated Empty deposit");
            restClient.updateDeposit(deposit);
            return new ResponseEntity("a bank Deposit updated", HttpStatus.OK);
        } catch (Exception e){
            LOGGER.error("updateDeposit({}), Exception:{}", deposit, e.toString());
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }

    /**
     * Deleting Bank Deposit by ID
     *
     * @param depositId Long - id of the Bank Deposit to be removed
     * @return ResponseEntity
     */
    @RequestMapping(value = "/id/{depositId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity removeDeposit(@PathVariable Long depositId) {
        LOGGER.debug("removeDeposit({})", depositId);
        try {
            Assert.isTrue(Long.valueOf(depositId) > 0, "Id Deposit - incorrect");
            Assert.notNull(depositId,"The parameter can not be NULL");
            restClient.removeDeposit(depositId);
            return new ResponseEntity("a bank Deposit removed", HttpStatus.OK);
        }catch (Exception e){
            LOGGER.error("removeDeposit({}), Exception:{}", depositId, e.toString());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Sending a POST request
     *
     * @param redirectAttributes RedirectAttributes - to transfer the so-called flash-attributes,
     *                           that is, values that will be available only to the following request, e.t. errors
     * @param model ModelMap - This facility is intended to convey information to the JSP page.
     * Variables "services","requests" and "responses" will be passed on mainFrame.jsp page.
     * @param status SessionStatus - status code of the query
     * @param queryMap LinkedHashMap<String, String> - parameters of a POST request
     * @return ModelAndView - "mainFrame" with SOAP services, xml request, xml and json response
     */
    @RequestMapping(value={"/submitRestQuery"}, method = RequestMethod.POST)
    public String postSoapQuery( RedirectAttributes redirectAttributes,
                                 ModelMap model,
                                 SessionStatus status,
                                 @RequestParam LinkedHashMap<String, String> queryMap
    ) throws ParseException
    {
        LOGGER.debug("postSoapQuery(queryMap - {})", queryMap.toString());

        status.setComplete();

        Object[] param = new Object[queryMap.size()-1];
        int i=0;
        for (Object p:queryMap.values()
                ) {
            if(!p.toString().equals(queryMap.get("httpMethod"))&
                    !p.toString().equals(queryMap.get("action"))){
                param[i] = p;
                i++;
            }
        }

        host = queryMap.get("HOST");
        restRequest = queryMap.get("URL");

        String jsonRequest = queryMap.get("JsonRequest");

        deposit.setDepositName(queryMap.get("depositName"));
        deposit.setDepositMinTerm(Integer.parseInt(queryMap.get("depositMinTerm")));
        deposit.setDepositMinAmount(Integer.parseInt(queryMap.get("depositMinAmount")));
        deposit.setDepositCurrency(queryMap.get("depositCurrency"));
        deposit.setDepositInterestRate(Integer.parseInt(queryMap.get("depositInterestRate")));
        deposit.setDepositAddConditions(queryMap.get("depositAddConditions"));

        try {
            depositor.setDepositorName(queryMap.get("depositorName"));
            depositor.setDepositorDateDeposit(dateFormat.parse(queryMap.get("depositorDateDeposit")));
            depositor.setDepositorDateReturnDeposit(dateFormat.parse(queryMap.get("depositorDateReturnDeposit")));
            depositor.setDepositorAmountDeposit(Integer.parseInt(queryMap.get("depositorAmountDeposit")));
            depositor.setDepositorAmountPlusDeposit(Integer.parseInt(queryMap.get("depositorAmountPlusDeposit")));
            depositor.setDepositorAmountMinusDeposit(Integer.parseInt(queryMap.get("depositorAmountMinusDeposit")));
            depositor.setDepositorMarkReturnDeposit(Integer.parseInt(queryMap.get("depositorMarkReturnDeposit")));
        }catch (Exception e){
            depositor = new BankDepositor();
        }


        /*
        soapRequest = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://bank.brest.com/soap\">\n" +
                "\t<x:Header/>\n" +
                "\t<x:Body>\n"+
                "\t\t<soa:"+queryMap.get("soapQuery")+"Request>\n";

        try{
            Class clientClass = soapClient.getClass();
            Method[] clientClassMethods = clientClass.getMethods();
            Method clientClassMethod = null;
            for(int j=0; j<clientClassMethods.length;j++){
                if(clientClassMethods[j].getName().equals(queryMap.get("soapQuery"))){
                    clientClassMethod = clientClassMethods[j];
                }
            }

            Object[] sortParam = new Object[clientClassMethod.getParameterTypes().length];

            if(param.length>0){
                Class clientRequestClass =
                        Class.forName(clientClassMethod.getReturnType().getName().replace("Response","Request"));

                for(int j=0; j<clientRequestClass.getDeclaredFields().length; j++){
                    switch (clientRequestClass.getDeclaredFields()[j].getType().getName()){
                        case "java.lang.String":{
                            sortParam[j] = queryMap.get(clientRequestClass.getDeclaredFields()[j].getName()).toString();
                            soapRequest = soapRequest
                                    + "\t\t\t<soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">"
                                    + sortParam[j]
                                    + "</soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">\n";
                            break;
                        }
                        case "java.lang.Integer":{
                            sortParam[j] = Integer.parseInt(queryMap.get(clientRequestClass.getDeclaredFields()[j].getName()));
                            soapRequest = soapRequest
                                    + "\t\t\t<soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">"
                                    + sortParam[j]
                                    + "</soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">\n";
                            break;
                        }
                        case "int":{
                            sortParam[j] = Integer.parseInt(queryMap.get(clientRequestClass.getDeclaredFields()[j].getName()));
                            soapRequest = soapRequest
                                    + "\t\t\t<soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">"
                                    + sortParam[j]
                                    + "</soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">\n";
                            break;
                        }
                        case "java.lang.Long":{
                            sortParam[j] = Long.parseLong(queryMap.get(clientRequestClass.getDeclaredFields()[j].getName()));
                            soapRequest = soapRequest
                                    + "\t\t\t<soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">"
                                    + sortParam[j]
                                    + "</soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">\n";
                            break;
                        }
                        case "long":{
                            sortParam[j] = Long.parseLong(queryMap.get(clientRequestClass.getDeclaredFields()[j].getName()));
                            soapRequest = soapRequest
                                    + "\t\t\t<soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">"
                                    + sortParam[j]
                                    + "</soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">\n";
                            break;
                        }
                        case "javax.xml.datatype.XMLGregorianCalendar":{
                            sortParam[j] = dateFormat.parse(queryMap.get(clientRequestClass.getDeclaredFields()[j].getName()));
                            soapRequest = soapRequest
                                    + "\t\t\t<soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">"
                                    + sortParam[j]
                                    + "</soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">\n";
                            break;
                        }
                        case "com.brest.bank.wsdl.BankDepositor":{
                            com.brest.bank.domain.BankDepositor bankDepositor = new com.brest.bank.domain.BankDepositor();
                            if (queryMap.get("depositorId").isEmpty()){
                                bankDepositor.setDepositorId(null);
                            } else {
                                bankDepositor.setDepositorId(Long.parseLong(queryMap.get("depositorId")));
                            }
                            bankDepositor.setDepositorName(queryMap.get("depositorName"));

                            Date xmlStartDate,xmlEndDate;
                            try {
                                xmlStartDate = dateFormat.parse(queryMap.get("depositorDateDeposit"));
                                xmlEndDate = dateFormat.parse(queryMap.get("depositorDateReturnDeposit"));
                                LOGGER.debug("xmlStartDate-{}, xmlEndDate-{}",xmlStartDate,xmlEndDate);
                            }
                            catch (  ParseException e) {
                                throw new RuntimeException(e);
                            }

                            bankDepositor.setDepositorDateDeposit(xmlStartDate);
                            bankDepositor.setDepositorDateReturnDeposit(xmlEndDate);
                            bankDepositor.setDepositorAmountDeposit(Integer.parseInt(queryMap.get("depositorAmountDeposit")));
                            bankDepositor.setDepositorAmountPlusDeposit(Integer.parseInt(queryMap.get("depositorAmountPlusDeposit")));
                            bankDepositor.setDepositorAmountMinusDeposit(Integer.parseInt(queryMap.get("depositorAmountMinusDeposit")));
                            bankDepositor.setDepositorMarkReturnDeposit(Integer.parseInt(queryMap.get("depositorMarkReturnDeposit")));

                            soapRequest = soapRequest
                                    + "\t\t\t<soa:bankDepositor>\n"
                                    + "\t\t\t\t<soa:bankDepositorId>" +bankDepositor.getDepositorId()+"</soa:bankDepositorId>\n"
                                    + "\t\t\t\t<soa:bankDepositorName>" +bankDepositor.getDepositorName()+"</soa:bankDepositorName>\n"
                                    + "\t\t\t\t<soa:bankDepositorDateDeposit>" +dateFormat.format(bankDepositor.getDepositorDateDeposit())+"</soa:bankDepositorDateDeposit>\n"
                                    + "\t\t\t\t<soa:bankDepositorAmountDeposit>" +bankDepositor.getDepositorAmountDeposit()+"</soa:bankDepositorAmountDeposit>\n"
                                    + "\t\t\t\t<soa:bankDepositorAmountPlusDeposit>" +bankDepositor.getDepositorAmountPlusDeposit()+"</soa:bankDepositorAmountPlusDeposit>\n"
                                    + "\t\t\t\t<soa:bankDepositorAmountMinusDeposit>" +bankDepositor.getDepositorAmountMinusDeposit()+"</soa:bankDepositorAmountMinusDeposit>\n"
                                    + "\t\t\t\t<soa:bankDepositorDateReturnDeposit>" +dateFormat.format(bankDepositor.getDepositorDateReturnDeposit())+"</soa:bankDepositorDateReturnDeposit>\n"
                                    + "\t\t\t\t<soa:bankDepositorMarkReturnDeposit>" +bankDepositor.getDepositorMarkReturnDeposit()+"</soa:bankDepositorMarkReturnDeposit>\n"
                                    + "\t\t\t</soa:bankDepositor>\n";

                            sortParam[j] = bankDepositor;
                            break;
                        }
                        case "com.brest.bank.wsdl.BankDeposit":{
                            BankDeposit bankDeposit = new BankDeposit();
                            if (queryMap.get("depositId").isEmpty()){
                                bankDeposit.setDepositId(null);
                            } else {
                                bankDeposit.setDepositId(Long.parseLong(queryMap.get("depositId")));
                            }
                            bankDeposit.setDepositName(queryMap.get("depositName"));
                            bankDeposit.setDepositMinTerm(Integer.parseInt(queryMap.get("depositMinTerm")));
                            bankDeposit.setDepositMinAmount(Integer.parseInt(queryMap.get("depositMinAmount")));
                            bankDeposit.setDepositCurrency(queryMap.get("depositCurrency"));
                            bankDeposit.setDepositInterestRate(Integer.parseInt(queryMap.get("depositInterestRate")));
                            bankDeposit.setDepositAddConditions(queryMap.get("depositAddConditions"));

                            soapRequest = soapRequest
                                    + "\t\t\t<soa:bankDeposit>\n"
                                    + "\t\t\t\t<soa:bankDepositId>" +bankDeposit.getDepositId()+"</soa:bankDepositId>\n"
                                    + "\t\t\t\t<soa:bankDepositName>" +bankDeposit.getDepositName()+"</soa:bankDepositName>\n"
                                    + "\t\t\t\t<soa:bankDepositMinTerm>" +bankDeposit.getDepositMinTerm()+"</soa:bankDepositMinTerm>\n"
                                    + "\t\t\t\t<soa:bankDepositMinAmount>" +bankDeposit.getDepositMinAmount()+"</soa:bankDepositMinAmount>\n"
                                    + "\t\t\t\t<soa:bankDepositCurrency>" +bankDeposit.getDepositCurrency()+"</soa:bankDepositCurrency>\n"
                                    + "\t\t\t\t<soa:bankDepositInterestRate>" +bankDeposit.getDepositInterestRate()+"</soa:bankDepositInterestRate>\n"
                                    + "\t\t\t\t<soa:bankDepositAddConditions>" +bankDeposit.getDepositAddConditions()+"</soa:bankDepositAddConditions>\n"
                                    + "\t\t\t</soa:bankDeposit>\n";

                            sortParam[j] = bankDeposit;
                            break;
                        }
                    }
                }
            }

            soapRequest = soapRequest + "\t\t</soa:" + queryMap.get("soapQuery") + "Request>\n"
                    +"\t</x:Body>\n" +
                    "</x:Envelope>";

            StringWriter sw = new StringWriter();
            sw.write(soapRequest);

            soapRequest = sw.toString();

            JAXBContext contextXML = JAXBContext.newInstance(clientClassMethod.getReturnType());

            sw = new StringWriter();

            Marshaller marshaller = contextXML.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            try{
                marshaller.marshal(clientClassMethod.invoke(soapClient,sortParam), sw);
            }catch (Exception e){
                sw.write("<SOAP-ENV:Fault xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "\t<faultcode>SOAP-ENV:Server</faultcode>" +
                        "\t<faultstring xml:lang=\"en\">" + e.getMessage()+"\n"+ e.toString()+"\n"+ e.getStackTrace() + "</faultstring>" +
                        "</SOAP-ENV:Fault>");
            }

            soapResponse[0] = sw.toString();

            LOGGER.debug("soapResponse[0] : {}", soapResponse[0]);

            JSONObject xmlJSONObj = XML.toJSONObject(sw.toString());
            String jsonString = xmlJSONObj.toString(4);

            soapResponse[1] = jsonString;

        }catch(Exception e) {
            LOGGER.debug("postSoapQuery(), Exception:{}", e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            soapResponse[0] = "postSoapQuery(), Exception:" + e.toString();
        }
*/
        return "redirect:../"+restRequest+"?";//+updateFormDeposit?depositId="+deposit.getDepositId();
    }

    /**
     * Sending a GET request
     *
     * @param status SessionStatus - status code of the query
     * @return ModelAndView - "mainFrame" with current deposit, list of all Bank Deposit and Depositors
     * @throws ParseException
     */
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView getSoapView(SessionStatus status
        ) throws ParseException
    {
        LOGGER.debug("getSoapView()");

        status.setComplete();

        ModelAndView view = new ModelAndView("indexRestMain");
        view.addObject("jsonRequest", "");
        view.addObject("deposit",deposit);
        view.addObject("depositor",depositor);

        return view;
    }

}

//Phaser
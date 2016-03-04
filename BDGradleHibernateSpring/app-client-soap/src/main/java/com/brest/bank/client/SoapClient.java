package com.brest.bank.client;

import com.brest.bank.domain.*;
import com.brest.bank.util.*;

import com.brest.bank.util.BankDeposit;
import com.brest.bank.util.BankDepositor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.util.Assert;
import org.springframework.ws.client.core.SourceExtractor;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.xml.transform.StringSource;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.*;

public class SoapClient extends WebServiceGatewaySupport{

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String ERROR_FROM_TO_PARAM = "The first parameter should be less than the second";

    public String host;

    SourceExtractor<GetBankDepositsResponse> responseExtractor = new SourceExtractor<GetBankDepositsResponse>() {
        @Override
        public GetBankDepositsResponse extractData(Source inSource) throws IOException, TransformerException
        {
            return (GetBankDepositsResponse) (getWebServiceTemplate().getUnmarshaller().unmarshal(inSource));
        }
    };
    SourceExtractor<GetBankDepositsWithDepositorsResponse> responseReportExtractor = new SourceExtractor<GetBankDepositsWithDepositorsResponse>() {
        @Override
        public GetBankDepositsWithDepositorsResponse extractData(Source inSource) throws IOException, TransformerException
        {
            return (GetBankDepositsWithDepositorsResponse) (getWebServiceTemplate().getUnmarshaller().unmarshal(inSource));
        }
    };

    SourceExtractor<GetBankDepositorsResponse> responseExtractorDepositor = new SourceExtractor<GetBankDepositorsResponse>() {
        @Override
        public GetBankDepositorsResponse extractData(Source inSource) throws IOException, TransformerException
        {
            return (GetBankDepositorsResponse) (getWebServiceTemplate().getUnmarshaller().unmarshal(inSource));
        }
    };


    /**
     *Get all Bank deposits
     *
     * @return XmlElement BankDeposits
     */
    public BankDeposits getBankDeposits(){
        Source requestPayload = new StringSource(
                "<getBankDepositsRequest xmlns='http://bank.brest.com/soap'>" +
                "</getBankDepositsRequest>");

        LOGGER.debug("getBankDepositsRequest - \n{}",requestPayload);

        GetBankDepositsResponse response = getWebServiceTemplate()
                .sendSourceAndReceive(requestPayload,
                        new SoapActionCallback("getBankDepositsResponse"),
                        responseExtractor);

        return response.getBankDeposits();
    }

    /**
     * Get Bank Deposit by id deposit
     *
     * @param depositId Long - id of the Bank Deposit to return
     * @return XmlElement BankDeposit with the specified id from the database
     */
    public BankDeposit getBankDepositById(Long depositId){
        LOGGER.debug("getBankDepositByIdRequest(depositId={})",depositId);
        GetBankDepositByIdRequest request = new GetBankDepositByIdRequest();
        request.setDepositId(depositId);

        GetBankDepositByIdResponse response = (GetBankDepositByIdResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositByIdResponse"));

        return response.getBankDeposit();
    }

    /**
     * Get Bank Deposit by id deposit
     *
     * @param depositName String - name of the Bank Deposit to return
     * @return XmlElement BankDeposit with the specified id from the database
     */
    public BankDeposit getBankDepositByName(String depositName){
        LOGGER.debug("getBankDepositByNameRequest(depositName={})",depositName);
        GetBankDepositByNameRequest request = new GetBankDepositByNameRequest();
        request.setDepositName(depositName);

        GetBankDepositByNameResponse response = (GetBankDepositByNameResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositByNameResponse"));

        return response.getBankDeposit();
    }

    /**
     * Get Bank Deposits by currency
     *
     * @param currency String - currency of the Bank Deposit to return
     * @return XmlElement BankDeposits with the specified depositCurrency from the database
     */
    public BankDeposits getBankDepositsByCurrency(String currency){
        LOGGER.debug("getBankDepositsByCurrencyRequest(depositCurrency={})",currency);
        GetBankDepositsByCurrencyRequest request = new GetBankDepositsByCurrencyRequest();
        request.setDepositCurrency(currency);

        GetBankDepositsByCurrencyResponse response = (GetBankDepositsByCurrencyResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositsByCurrencyResponse"));

        return response.getBankDeposits();
    }

    /**
     * Get Bank Deposits by interest rate
     *
     * @param rate Integer - interest rate of the Bank Deposits to return
     * @return XmlElement BankDeposits with the specified depositInterestRate from the database
     */
    public BankDeposits getBankDepositsByInterestRate(Integer rate){
        LOGGER.debug("getBankDepositsByInterestRateRequest(depositInterestRate={})",rate);
        GetBankDepositsByInterestRateRequest request = new GetBankDepositsByInterestRateRequest();
        request.setDepositInterestRate(rate);

        GetBankDepositsByInterestRateResponse response = (GetBankDepositsByInterestRateResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositsByInterestRateResponse"));

        return response.getBankDeposits();
    }

    /**
     * Get Bank Deposits from-to MIN TERM values
     *
     * @param fromTerm Integer - start value of the min term (count month)
     * @param toTerm Integer - end value of the min term (count month)
     * @return XmlElement BankDeposits with the specified depositMinTerm from the database
     */
    public BankDeposits getBankDepositsFromToMinTerm(Integer fromTerm, Integer toTerm){
        LOGGER.debug("getBankDepositsFromToMinTermRequest(from={}, to={})",fromTerm,toTerm);
        Assert.isTrue(fromTerm<=toTerm,ERROR_FROM_TO_PARAM);

        GetBankDepositsFromToMinTermRequest request = new GetBankDepositsFromToMinTermRequest();
            request.setFromTerm(fromTerm);
            request.setToTerm(toTerm);

        GetBankDepositsFromToMinTermResponse response = (GetBankDepositsFromToMinTermResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositsFromToMinTermResponse"));

        return response.getBankDeposits();
    }

    /**
     * Get Bank Deposits from-to INTEREST RATE values
     *
     * @param fromRate Integer - start value of the interest rate (0% < startRate <= 100%)
     * @param toRate Integer - end value of the interest rate (0% < endRate <= 100%)
     * @return XmlElement BankDeposits with the specified depositInterestRate from the database
     */
    public BankDeposits getBankDepositsFromToInterestRate(Integer fromRate, Integer toRate){
        LOGGER.debug("getBankDepositsFromToInterestRateRequest(from={}, to={})",fromRate,toRate);
        Assert.isTrue(fromRate<=toRate,ERROR_FROM_TO_PARAM);

        GetBankDepositsFromToInterestRateRequest request = new GetBankDepositsFromToInterestRateRequest();
        request.setStartRate(fromRate);
        request.setEndRate(toRate);

        GetBankDepositsFromToInterestRateResponse response = (GetBankDepositsFromToInterestRateResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositsFromToInterestRateResponse"));

        return response.getBankDeposits();
    }

    /**
     * Get Bank Deposits from-to Date Deposit values
     *
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (endDate > startDate)
     * @return XmlElement BankDeposits with the specified depositorDateDeposit from the database
     */
    public BankDeposits getBankDepositsFromToDateDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsFromToDateDepositRequest(start={}, end={})",dateFormat.format(startDate),
                dateFormat.format(endDate));
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);

        XMLGregorianCalendar xmlStartDate,xmlEndDate;
        String strStartDate = dateFormat.format(startDate);
        String strEndDate = dateFormat.format(endDate);
        LOGGER.debug("strStartDate-{}, strEndDate-{}",strStartDate,strEndDate);
        try {
            xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strStartDate);
            xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strEndDate);
            LOGGER.debug("xmlStartDate-{}, xmlEndDate-{}",xmlStartDate,xmlEndDate);
        }
        catch (  DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }

        GetBankDepositsFromToDateDepositRequest request = new GetBankDepositsFromToDateDepositRequest();
        request.setStartDate(xmlStartDate);
        request.setEndDate(xmlEndDate);

        GetBankDepositsFromToDateDepositResponse response = (GetBankDepositsFromToDateDepositResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositsFromToDateDepositResponse"));

        return response.getBankDeposits();
    }

    /**
     * Get Bank Deposits from-to Date Return Deposit values
     *
     * @param startDate Date - start value of the date return deposit (startDate < endDate)
     * @param endDate Date - end value of the date return deposit (endDate > startDate)
     * @return XmlElement BankDeposits with the specified depositorDateReturnDeposit from the database
     */
    public BankDeposits getBankDepositsFromToDateReturnDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsFromToDateReturnDepositRequest(start={}, end={})",dateFormat.format(startDate),
                dateFormat.format(endDate));
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);

        XMLGregorianCalendar xmlStartDate,xmlEndDate;
        String strStartDate = dateFormat.format(startDate);
        String strEndDate = dateFormat.format(endDate);
        LOGGER.debug("strStartDate-{}, strEndDate-{}",strStartDate,strEndDate);
        try {
            xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strStartDate);
            xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strEndDate);
            LOGGER.debug("xmlStartDate-{}, xmlEndDate-{}",xmlStartDate,xmlEndDate);
        }
        catch (  DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }

        GetBankDepositsFromToDateReturnDepositRequest request = new GetBankDepositsFromToDateReturnDepositRequest();
        request.setStartDate(xmlStartDate);
        request.setEndDate(xmlEndDate);

        GetBankDepositsFromToDateReturnDepositResponse response = (GetBankDepositsFromToDateReturnDepositResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositsFromToDateReturnDepositResponse"));

        return response.getBankDeposits();
    }

    /**
     * Get Bank Deposits by NAME with depositors
     *
     * @param depositName String - name of the Bank Deposit to return
     * @return XmlElement BankDepositReport
     */
    public BankDepositReport getBankDepositByNameWithDepositors(String depositName){
        LOGGER.debug("getBankDepositByNameWithDepositorsRequest(depositName={})",depositName);

        GetBankDepositByNameWithDepositorsRequest request = new GetBankDepositByNameWithDepositorsRequest();
        request.setDepositName(depositName);

        GetBankDepositByNameWithDepositorsResponse response = (GetBankDepositByNameWithDepositorsResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositByNameWithDepositorsResponse"));

        return response.getBankDepositReport();
    }

    /**
     * Get Bank Deposits by NAME with depositors from-to Date Deposit values
     *
     * @param depositName String - name of the Bank Deposit to return
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (endDate > startDate)
     * @return XmlElement BankDepositReport
     */
    public BankDepositReport getBankDepositByNameFromToDateDepositWithDepositors(String depositName,
                                                                                 Date startDate,
                                                                                 Date endDate){
        LOGGER.debug("getBankDepositByNameFromToDateDepositWithDepositorsRequest(name={}, startDate={}, endDate={})",
                depositName, dateFormat.format(startDate), dateFormat.format(endDate));
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);

        XMLGregorianCalendar xmlStartDate,xmlEndDate;
        String strStartDate = dateFormat.format(startDate);
        String strEndDate = dateFormat.format(endDate);
        LOGGER.debug("strStartDate-{}, strEndDate-{}",strStartDate,strEndDate);
        try {
            xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strStartDate);
            xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strEndDate);
            LOGGER.debug("xmlStartDate-{}, xmlEndDate-{}",xmlStartDate,xmlEndDate);
        }
        catch (  DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }

        GetBankDepositByNameFromToDateDepositWithDepositorsRequest request =
                new GetBankDepositByNameFromToDateDepositWithDepositorsRequest();
        request.setDepositName(depositName);

        request.setStartDate(xmlStartDate);
        request.setEndDate(xmlEndDate);

        GetBankDepositByNameFromToDateDepositWithDepositorsResponse response =
                (GetBankDepositByNameFromToDateDepositWithDepositorsResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositByNameFromToDateDepositWithDepositorsResponse"));

        return response.getBankDepositReport();
    }

    /**
     * Get Bank Deposits by NAME with depositors from-to Date Return Deposit values
     *
     * @param depositName String - name of the Bank Deposit to return
     * @param startDate Date - start value of the date return deposit (startDate < endDate)
     * @param endDate Date - end value of the date return deposit (endDate > startDate)
     * @return XmlElement BankDepositReport
     */
    public BankDepositReport getBankDepositByNameFromToDateReturnDepositWithDepositors(String depositName,
                                                                                 Date startDate,
                                                                                 Date endDate){
        LOGGER.debug("getBankDepositByNameFromToDateReturnDepositWithDepositorsRequest(name={}, startDate={}, endDate={})",
                depositName, dateFormat.format(startDate), dateFormat.format(endDate));
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);

        XMLGregorianCalendar xmlStartDate,xmlEndDate;
        String strStartDate = dateFormat.format(startDate);
        String strEndDate = dateFormat.format(endDate);
        LOGGER.debug("strStartDate-{}, strEndDate-{}",strStartDate,strEndDate);
        try {
            xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strStartDate);
            xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strEndDate);
            LOGGER.debug("xmlStartDate-{}, xmlEndDate-{}",xmlStartDate,xmlEndDate);
        }
        catch (  DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }

        GetBankDepositByNameFromToDateReturnDepositWithDepositorsRequest request =
                new GetBankDepositByNameFromToDateReturnDepositWithDepositorsRequest();
        request.setDepositName(depositName);

        request.setStartDate(xmlStartDate);
        request.setEndDate(xmlEndDate);

        GetBankDepositByNameFromToDateReturnDepositWithDepositorsResponse response =
                (GetBankDepositByNameFromToDateReturnDepositWithDepositorsResponse)getWebServiceTemplate()
                        .marshalSendAndReceive(request,
                                new SoapActionCallback("getBankDepositByNameFromToDateReturnDepositWithDepositorsResponse"));

        return response.getBankDepositReport();
    }

    /**
     * Get Bank Deposits by ID with depositors
     *
     * @param depositId ong - depositId of the Bank Deposit to return
     * @return XmlElement BankDepositReport
     */
    public BankDepositReport getBankDepositByIdWithDepositors(Long depositId){
        LOGGER.debug("getBankDepositByIdWithDepositorsRequest(depositId={})",depositId);

        GetBankDepositByIdWithDepositorsRequest request = new GetBankDepositByIdWithDepositorsRequest();
        request.setDepositId(depositId);

        GetBankDepositByIdWithDepositorsResponse response = (GetBankDepositByIdWithDepositorsResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositByIdWithDepositorsResponse"));

        return response.getBankDepositReport();
    }

    /**
     * Get Bank Deposits by Id with depositors from-to Date Deposit values
     *
     * @param depositId Long - depositId of the Bank Deposit to return
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (endDate > startDate)
     * @return XmlElement BankDepositReport
     */
    public BankDepositReport getBankDepositByIdFromToDateDepositWithDepositors(Long depositId,
                                                                               Date startDate,
                                                                               Date endDate){
        LOGGER.debug("getBankDepositByIdFromToDateDepositWithDepositorsRequest(id={}, startDate={}, endDate={})",
                depositId, dateFormat.format(startDate), dateFormat.format(endDate));
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);

        XMLGregorianCalendar xmlStartDate,xmlEndDate;
        String strStartDate = dateFormat.format(startDate);
        String strEndDate = dateFormat.format(endDate);
        LOGGER.debug("strStartDate-{}, strEndDate-{}",strStartDate,strEndDate);
        try {
            xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strStartDate);
            xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strEndDate);
            LOGGER.debug("xmlStartDate-{}, xmlEndDate-{}",xmlStartDate,xmlEndDate);
        }catch (DatatypeConfigurationException e){
            throw new RuntimeException(e);
        }

        GetBankDepositByIdFromToDateDepositWithDepositorsRequest request =
                new GetBankDepositByIdFromToDateDepositWithDepositorsRequest();
        request.setDepositId(depositId);

        request.setStartDate(xmlStartDate);
        request.setEndDate(xmlEndDate);

        GetBankDepositByIdFromToDateDepositWithDepositorsResponse response =
                (GetBankDepositByIdFromToDateDepositWithDepositorsResponse)getWebServiceTemplate()
                        .marshalSendAndReceive(request,
                                new SoapActionCallback("getBankDepositByIdFromToDateDepositWithDepositorsResponse"));

        return response.getBankDepositReport();
    }

    /**
     * Get Bank Deposits by Id with depositors from-to Date Return Deposit values
     *
     * @param depositId Long - depositId of the Bank Deposit to return
     * @param startDate Date - start value of the date return deposit (startDate < endDate)
     * @param endDate Date - end value of the date return deposit (endDate > startDate)
     * @return XmlElement BankDepositReport
     */
    public BankDepositReport getBankDepositByIdFromToDateReturnDepositWithDepositors(Long depositId,
                                                                                       Date startDate,
                                                                                       Date endDate) {
        LOGGER.debug("getBankDepositByIdFromToDateReturnDepositWithDepositorsRequest(id={}, startDate={}, endDate={})",
                depositId, dateFormat.format(startDate), dateFormat.format(endDate));
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);

        XMLGregorianCalendar xmlStartDate,xmlEndDate;
        String strStartDate = dateFormat.format(startDate);
        String strEndDate = dateFormat.format(endDate);
        LOGGER.debug("strStartDate-{}, strEndDate-{}",strStartDate,strEndDate);
        try {
            xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strStartDate);
            xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strEndDate);
            LOGGER.debug("xmlStartDate-{}, xmlEndDate-{}",xmlStartDate,xmlEndDate);
        }
        catch (  DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }

        GetBankDepositByIdFromToDateReturnDepositWithDepositorsRequest request =
                new GetBankDepositByIdFromToDateReturnDepositWithDepositorsRequest();
        request.setDepositId(depositId);

        request.setStartDate(xmlStartDate);
        request.setEndDate(xmlEndDate);

        GetBankDepositByIdFromToDateReturnDepositWithDepositorsResponse response =
                (GetBankDepositByIdFromToDateReturnDepositWithDepositorsResponse)getWebServiceTemplate()
                        .marshalSendAndReceive(request,
                                new SoapActionCallback("getBankDepositByIdFromToDateReturnDepositWithDepositorsResponse"));

        return response.getBankDepositReport();
    }

    /**
     * Get Bank Deposit with depositors
     *
     * @return XmlElement BankDepositsReport
     */
    public BankDepositsReport getBankDepositsWithDepositors(){
        Source requestPayload = new StringSource(
                "<getBankDepositsWithDepositorsRequest xmlns='http://bank.brest.com/soap'>" +
                "</getBankDepositsWithDepositorsRequest>");

        LOGGER.debug("getBankDepositsWithDepositorsRequest - \n{}",requestPayload);

        GetBankDepositsWithDepositorsResponse response = getWebServiceTemplate()
                .sendSourceAndReceive(requestPayload,
                        new SoapActionCallback("getBankDepositsWithDepositorsResponse"),
                        responseReportExtractor);

        return response.getBankDepositsReport();
    }

    /**
     * Get Bank Deposit from-to Date Deposit with depositors
     *
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (startDate < endDate)
     * @return XmlElement BankDepositsReport
     */
    public BankDepositsReport getBankDepositsFromToDateDepositWithDepositors(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsFromToDateDepositWithDepositorsRequest(start={}, end={})",
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);

        XMLGregorianCalendar xmlStartDate,xmlEndDate;
        String strStartDate = dateFormat.format(startDate);
        String strEndDate = dateFormat.format(endDate);
        LOGGER.debug("strStartDate-{}, strEndDate-{}",strStartDate,strEndDate);
        try {
            xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strStartDate);
            xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strEndDate);
            LOGGER.debug("xmlStartDate-{}, xmlEndDate-{}",xmlStartDate,xmlEndDate);
        }
        catch (  DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }

        GetBankDepositsFromToDateDepositWithDepositorsRequest request =
                new GetBankDepositsFromToDateDepositWithDepositorsRequest();

        request.setStartDate(xmlStartDate);
        request.setEndDate(xmlEndDate);

        GetBankDepositsFromToDateDepositWithDepositorsResponse response =
                (GetBankDepositsFromToDateDepositWithDepositorsResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositsFromToDateDepositWithDepositorsResponse"));

        return response.getBankDepositsReport();
    }

    /**
     * Get Bank Deposit from-to Date Return Deposit with depositors
     *
     * @param startDate Date - start value of the date return deposit (startDate < endDate)
     * @param endDate Date - end value of the date return deposit (startDate < endDate)
     * @return XmlElement BankDepositsReport
     */
    public BankDepositsReport getBankDepositsFromToDateReturnDepositWithDepositors(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsFromToDateReturnDepositWithDepositorsRequest(start={}, end={})",
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);

        XMLGregorianCalendar xmlStartDate,xmlEndDate;
        String strStartDate = dateFormat.format(startDate);
        String strEndDate = dateFormat.format(endDate);
        LOGGER.debug("strStartDate-{}, strEndDate-{}",strStartDate,strEndDate);
        try {
            xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strStartDate);
            xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strEndDate);
            LOGGER.debug("xmlStartDate-{}, xmlEndDate-{}",xmlStartDate,xmlEndDate);
        }
        catch (  DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }

        GetBankDepositsFromToDateReturnDepositWithDepositorsRequest request =
                new GetBankDepositsFromToDateReturnDepositWithDepositorsRequest();

        request.setStartDate(xmlStartDate);
        request.setEndDate(xmlEndDate);

        GetBankDepositsFromToDateReturnDepositWithDepositorsResponse response =
                (GetBankDepositsFromToDateReturnDepositWithDepositorsResponse)getWebServiceTemplate()
                        .marshalSendAndReceive(request,
                                new SoapActionCallback("getBankDepositsFromToDateReturnDepositWithDepositorsResponse"));

        return response.getBankDepositsReport();
    }

    /**
     * Get Bank Deposit by Currency with depositors
     *
     * @param depositCurrency String - Currency of the Bank Deposit to return
     * @return XmlElement BankDepositsReport
     */
    public BankDepositsReport getBankDepositsByCurrencyWithDepositors(String depositCurrency){
        LOGGER.debug("getBankDepositsByCurrencyWithDepositorsRequest(currency={})",depositCurrency);

        GetBankDepositsByCurrencyWithDepositorsRequest request =
                new GetBankDepositsByCurrencyWithDepositorsRequest();
        request.setDepositCurrency(depositCurrency);

        GetBankDepositsByCurrencyWithDepositorsResponse response =
                (GetBankDepositsByCurrencyWithDepositorsResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositsByCurrencyWithDepositorsResponse"));

        return response.getBankDepositsReport();
    }

    /**
     * Get Bank Deposit from-to Date Deposit by Currency with depositors
     *
     * @param depositCurrency String - Currency of the Bank Deposit to return
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (startDate < endDate)
     * @return XmlElement BankDepositsReport
     */
    public BankDepositsReport getBankDepositsByCurrencyFromToDateDepositWithDepositors(String depositCurrency,
                                                                                       Date startDate,
                                                                                       Date endDate){
        LOGGER.debug("getBankDepositsByCurrencyFromToDateDepositWithDepositorsRequest(currency={}, " +
                "start={}, end={}",depositCurrency,dateFormat.format(startDate),dateFormat.format(endDate));

        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);

        XMLGregorianCalendar xmlStartDate,xmlEndDate;
        String strStartDate = dateFormat.format(startDate);
        String strEndDate = dateFormat.format(endDate);
        LOGGER.debug("strStartDate-{}, strEndDate-{}",strStartDate,strEndDate);
        try {
            xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strStartDate);
            xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strEndDate);
            LOGGER.debug("xmlStartDate-{}, xmlEndDate-{}",xmlStartDate,xmlEndDate);
        }
        catch (  DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }

        GetBankDepositsByCurrencyFromToDateDepositWithDepositorsRequest request =
                new GetBankDepositsByCurrencyFromToDateDepositWithDepositorsRequest();
        request.setDepositCurrency(depositCurrency);
        request.setStartDate(xmlStartDate);
        request.setEndDate(xmlEndDate);

        GetBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse response =
                (GetBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse"));

        return response.getBankDepositsReport();
    }

    /**
     * Get Bank Deposit from-to Date Return Deposit by Currency with depositors
     *
     * @param depositCurrency String - Currency of the Bank Deposit to return
     * @param startDate Date - start value of the date return deposit (startDate < endDate)
     * @param endDate Date - end value of the date return deposit (startDate < endDate)
     * @return XmlElement BankDepositsReport
     */
    public BankDepositsReport getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(String depositCurrency,
                                                                                       Date startDate,
                                                                                       Date endDate){
        LOGGER.debug("getBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsRequest(currency={}, " +
                "start={}, end={}",depositCurrency,dateFormat.format(startDate),dateFormat.format(endDate));

        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);

        XMLGregorianCalendar xmlStartDate,xmlEndDate;
        String strStartDate = dateFormat.format(startDate);
        String strEndDate = dateFormat.format(endDate);
        LOGGER.debug("strStartDate-{}, strEndDate-{}",strStartDate,strEndDate);
        try {
            xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strStartDate);
            xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strEndDate);
            LOGGER.debug("xmlStartDate-{}, xmlEndDate-{}",xmlStartDate,xmlEndDate);
        }
        catch (  DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }

        GetBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsRequest request =
                new GetBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsRequest();
        request.setDepositCurrency(depositCurrency);
        request.setStartDate(xmlStartDate);
        request.setEndDate(xmlEndDate);

        GetBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsResponse response =
                (GetBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsResponse)getWebServiceTemplate()
                        .marshalSendAndReceive(request,
                                new SoapActionCallback("getBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsResponse"));

        return response.getBankDepositsReport();
    }

    /**
     * Add Bank Deposit
     *
     * @param deposit BankDeposit - Bank Deposit to be stored in the database
     * @return XmlElement BankDeposit
     */
    public BankDeposit addDeposit(com.brest.bank.domain.BankDeposit deposit){
        LOGGER.debug("addBankDepositRequest(deposit={})",deposit);

        BankDeposit xmlDeposit = new BankDeposit();
            xmlDeposit.setDepositId(deposit.getDepositId());
            xmlDeposit.setDepositName(deposit.getDepositName());
            xmlDeposit.setDepositMinTerm(deposit.getDepositMinTerm());
            xmlDeposit.setDepositMinAmount(deposit.getDepositMinAmount());
            xmlDeposit.setDepositCurrency(deposit.getDepositCurrency());
            xmlDeposit.setDepositInterestRate(deposit.getDepositInterestRate());
            xmlDeposit.setDepositAddConditions(deposit.getDepositAddConditions());

        AddBankDepositRequest request = new AddBankDepositRequest();
        request.setBankDeposit(xmlDeposit);

        AddBankDepositResponse response = (AddBankDepositResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("addBankDepositResponse"));

        return response.getBankDeposit();
    }

    /**
     * Updating Bank Deposit
     *
     * @param deposit BankDeposit - Bank Deposit to be stored in the database
     * @return XmlElement BankDeposit
     */
    public BankDeposit updateDeposit(com.brest.bank.domain.BankDeposit deposit){
        LOGGER.debug("updateBankDepositRequest(deposit={})",deposit);

        BankDeposit xmlDeposit = new BankDeposit();
        xmlDeposit.setDepositId(deposit.getDepositId());
        xmlDeposit.setDepositName(deposit.getDepositName());
        xmlDeposit.setDepositMinTerm(deposit.getDepositMinTerm());
        xmlDeposit.setDepositMinAmount(deposit.getDepositMinAmount());
        xmlDeposit.setDepositCurrency(deposit.getDepositCurrency());
        xmlDeposit.setDepositInterestRate(deposit.getDepositInterestRate());
        xmlDeposit.setDepositAddConditions(deposit.getDepositAddConditions());

        UpdateBankDepositRequest request = new UpdateBankDepositRequest();
        request.setBankDeposit(xmlDeposit);

        UpdateBankDepositResponse response = (UpdateBankDepositResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("updateBankDepositResponse"));

        return response.getBankDeposit();
    }

    /**
     * Deleting Bank Deposit by ID
     *
     * @param depositId Long - id of the Bank Deposit to be removed
     * @return result String
     */
    public String removeDeposit(Long depositId){
        LOGGER.debug("deleteBankDepositRequest(depositId={})",depositId);

        DeleteBankDepositRequest request = new DeleteBankDepositRequest();
        request.setDepositId(depositId);

        DeleteBankDepositResponse response = (DeleteBankDepositResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("deleteBankDepositResponse"));

        return response.getResult();
    }

    /**
     *Get all Bank depositors
     *
     * @return XmlElement BankDepositors
     */
    public BankDepositors getBankDepositors(){
        Source requestPayload = new StringSource(
                "<getBankDepositorsRequest xmlns='http://bank.brest.com/soap'>" +
                "</getBankDepositorsRequest>");

        LOGGER.debug("getBankDepositorsRequest - \n{}",requestPayload);

        GetBankDepositorsResponse response = getWebServiceTemplate()
                .sendSourceAndReceive(requestPayload,
                        new SoapActionCallback("getBankDepositorsResponse"),
                        responseExtractorDepositor);

        return response.getBankDepositors();
    }

    /**
     * Get all Bank Depositors from-to Date Deposit
     *
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (startDate < endDate)
     * @return XmlElement BankDepositors
     */
    public BankDepositors getBankDepositorsFromToDateDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositorsFromToDateDepositRequest(start={}, end={}", dateFormat.format(startDate),
                dateFormat.format(endDate));

        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);

        XMLGregorianCalendar xmlStartDate,xmlEndDate;
        String strStartDate = dateFormat.format(startDate);
        String strEndDate = dateFormat.format(endDate);
        LOGGER.debug("strStartDate-{}, strEndDate-{}",strStartDate,strEndDate);
        try {
            xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strStartDate);
            xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strEndDate);
            LOGGER.debug("xmlStartDate-{}, xmlEndDate-{}",xmlStartDate,xmlEndDate);
        }
        catch (  DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }

        GetBankDepositorsFromToDateDepositRequest request = new GetBankDepositorsFromToDateDepositRequest();
        request.setStartDate(xmlStartDate);
        request.setEndDate(xmlEndDate);

        GetBankDepositorsFromToDateDepositResponse response =
                (GetBankDepositorsFromToDateDepositResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositorsFromToDateDepositResponse"));

        return response.getBankDepositors();
    }

    /**
     * Get all Bank Depositors from-to Date Return Deposit
     *
     * @param startDate Date - start value of the date return deposit (startDate < endDate)
     * @param endDate Date - end value of the date return deposit (startDate < endDate)
     * @return XmlElement BankDepositors
     */
    public BankDepositors getBankDepositorsFromToDateReturnDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositorsFromToDateReturnDepositRequest(start={}, end={}", dateFormat.format(startDate),
                dateFormat.format(endDate));

        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);

        XMLGregorianCalendar xmlStartDate,xmlEndDate;
        String strStartDate = dateFormat.format(startDate);
        String strEndDate = dateFormat.format(endDate);
        LOGGER.debug("strStartDate-{}, strEndDate-{}",strStartDate,strEndDate);
        try {
            xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strStartDate);
            xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strEndDate);
            LOGGER.debug("xmlStartDate-{}, xmlEndDate-{}",xmlStartDate,xmlEndDate);
        }
        catch (  DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }

        GetBankDepositorsFromToDateReturnDepositRequest request = new GetBankDepositorsFromToDateReturnDepositRequest();
        request.setStartDate(xmlStartDate);
        request.setEndDate(xmlEndDate);

        GetBankDepositorsFromToDateReturnDepositResponse response =
                (GetBankDepositorsFromToDateReturnDepositResponse)getWebServiceTemplate()
                        .marshalSendAndReceive(request,
                                new SoapActionCallback("getBankDepositorsFromToDateReturnDepositResponse"));

        return response.getBankDepositors();
    }

    /**
     * Get Bank Depositor by ID
     *
     * @param depositorId Long - id of the Bank Depositor to return
     * @return XmlElement BankDepositor
     */
    public BankDepositor getBankDepositorById(Long depositorId){
        LOGGER.debug("getBankDepositorByIdRequest(id={})",depositorId);

        GetBankDepositorByIdRequest request = new GetBankDepositorByIdRequest();
        request.setDepositorId(depositorId);

        GetBankDepositorByIdResponse response = (GetBankDepositorByIdResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositorByIdResponse"));

        return response.getBankDepositor();
    }
}

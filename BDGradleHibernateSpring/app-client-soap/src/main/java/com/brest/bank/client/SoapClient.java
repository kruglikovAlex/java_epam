package com.brest.bank.client;


import com.brest.bank.util.*;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.dom4j.io.XMLResult;
import org.springframework.util.Assert;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.SourceExtractor;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.text.ParseException;
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
     * @param start Date - start value of the date deposit (startDate < endDate)
     * @param end Date - end value of the date deposit (endDate > startDate)
     * @return XmlElement BankDeposits with the specified depositorDateDeposit from the database
     */
    public BankDeposits getBankDepositsFromToDateDeposit(Date start, Date end) throws DatatypeConfigurationException, ParseException{
        LOGGER.debug("getBankDepositsFromToDateDepositRequest(start={}, end={})",dateFormat.format(start),
                dateFormat.format(end));
        Assert.isTrue(start.before(end)||start.equals(end));

        GregorianCalendar dateStart = new GregorianCalendar();
        GregorianCalendar dateEnd = new GregorianCalendar();
        dateStart.setTimeInMillis(start.getTime());
        dateStart.setTimeZone(TimeZone.getDefault());
        dateEnd.setTimeInMillis(end.getTime());
        dateEnd.setTimeZone(TimeZone.getDefault());
        XMLGregorianCalendar dateStartXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateStart);
        XMLGregorianCalendar dateEndXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateEnd);

        GetBankDepositsFromToDateDepositRequest request = new GetBankDepositsFromToDateDepositRequest();
        request.setStartDate(dateStartXml);
        request.setEndDate(dateEndXml);

        GetBankDepositsFromToDateDepositResponse response = (GetBankDepositsFromToDateDepositResponse)getWebServiceTemplate()
                .marshalSendAndReceive(request,
                        new SoapActionCallback("getBankDepositsFromToDateDepositResponse"));

        return response.getBankDeposits();
    }

    /**
     * Get Bank Deposits from-to Date Return Deposit values
     *
     * @param start Date - start value of the date return deposit (startDate < endDate)
     * @param end Date - end value of the date return deposit (endDate > startDate)
     * @return XmlElement BankDeposits with the specified depositorDateReturnDeposit from the database
     */
    public BankDeposits getBankDepositsFromToDateReturnDeposit(Date start, Date end) throws DatatypeConfigurationException, ParseException{
        LOGGER.debug("getBankDepositsFromToDateReturnDepositRequest(start={}, end={})",dateFormat.format(start),
                dateFormat.format(end));
        Assert.isTrue(start.before(end)||start.equals(end));

        GregorianCalendar dateStart = new GregorianCalendar();
        GregorianCalendar dateEnd = new GregorianCalendar();
        dateStart.setTimeInMillis(start.getTime());
        dateStart.setTimeZone(TimeZone.getDefault());
        dateEnd.setTimeInMillis(end.getTime());
        dateEnd.setTimeZone(TimeZone.getDefault());
        XMLGregorianCalendar dateStartXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateStart);
        XMLGregorianCalendar dateEndXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateEnd);

        GetBankDepositsFromToDateReturnDepositRequest request = new GetBankDepositsFromToDateReturnDepositRequest();
        request.setStartDate(dateStartXml);
        request.setEndDate(dateEndXml);

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
     * @throws DatatypeConfigurationException
     */
    public BankDepositReport getBankDepositByNameFromToDateDepositWithDepositors(String depositName,
                                                                                 Date startDate,
                                                                                 Date endDate) throws DatatypeConfigurationException{
        LOGGER.debug("getBankDepositByNameFromToDateDepositWithDepositorsRequest(name={}, startDate={}, endDate={})",
                depositName, dateFormat.format(startDate), dateFormat.format(endDate));

        GetBankDepositByNameFromToDateDepositWithDepositorsRequest request =
                new GetBankDepositByNameFromToDateDepositWithDepositorsRequest();
        request.setDepositName(depositName);

        GregorianCalendar dateStart = new GregorianCalendar();
        GregorianCalendar dateEnd = new GregorianCalendar();
        dateStart.setTimeInMillis(startDate.getTime());
        dateStart.setTimeZone(TimeZone.getDefault());
        dateEnd.setTimeInMillis(endDate.getTime());
        dateEnd.setTimeZone(TimeZone.getDefault());
        XMLGregorianCalendar dateStartXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateStart);
        XMLGregorianCalendar dateEndXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateEnd);

        request.setStartDate(dateStartXml);
        request.setEndDate(dateEndXml);

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
     * @throws DatatypeConfigurationException
     */
    public BankDepositReport getBankDepositByNameFromToDateReturnDepositWithDepositors(String depositName,
                                                                                 Date startDate,
                                                                                 Date endDate) throws DatatypeConfigurationException{
        LOGGER.debug("getBankDepositByNameFromToDateReturnDepositWithDepositorsRequest(name={}, startDate={}, endDate={})",
                depositName, dateFormat.format(startDate), dateFormat.format(endDate));

        GetBankDepositByNameFromToDateReturnDepositWithDepositorsRequest request =
                new GetBankDepositByNameFromToDateReturnDepositWithDepositorsRequest();
        request.setDepositName(depositName);

        GregorianCalendar dateStart = new GregorianCalendar();
        GregorianCalendar dateEnd = new GregorianCalendar();
        dateStart.setTimeInMillis(startDate.getTime());
        dateStart.setTimeZone(TimeZone.getDefault());
        dateEnd.setTimeInMillis(endDate.getTime());
        dateEnd.setTimeZone(TimeZone.getDefault());
        XMLGregorianCalendar dateStartXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateStart);
        XMLGregorianCalendar dateEndXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateEnd);

        request.setStartDate(dateStartXml);
        request.setEndDate(dateEndXml);

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
     * @throws DatatypeConfigurationException
     */
    public BankDepositReport getBankDepositByIdFromToDateDepositWithDepositors(Long depositId,
                                                                               Date startDate,
                                                                               Date endDate) throws DatatypeConfigurationException{
        LOGGER.debug("getBankDepositByIdFromToDateDepositWithDepositorsRequest(id={}, startDate={}, endDate={})",
                depositId, dateFormat.format(startDate), dateFormat.format(endDate));

        GetBankDepositByIdFromToDateDepositWithDepositorsRequest request =
                new GetBankDepositByIdFromToDateDepositWithDepositorsRequest();
        request.setDepositId(depositId);

        GregorianCalendar dateStart = new GregorianCalendar();
        GregorianCalendar dateEnd = new GregorianCalendar();
        dateStart.setTimeInMillis(startDate.getTime());
        dateStart.setTimeZone(TimeZone.getDefault());
        dateEnd.setTimeInMillis(endDate.getTime());
        dateEnd.setTimeZone(TimeZone.getDefault());
        XMLGregorianCalendar dateStartXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateStart);
        XMLGregorianCalendar dateEndXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateEnd);

        request.setStartDate(dateStartXml);
        request.setEndDate(dateEndXml);

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
     * @throws DatatypeConfigurationException
     */
    public BankDepositReport getBankDepositByIdFromToDateReturnDepositWithDepositors(Long depositId,
                                                                                       Date startDate,
                                                                                       Date endDate) throws DatatypeConfigurationException{
        LOGGER.debug("getBankDepositByIdFromToDateReturnDepositWithDepositorsRequest(id={}, startDate={}, endDate={})",
                depositId, dateFormat.format(startDate), dateFormat.format(endDate));

        GetBankDepositByIdFromToDateReturnDepositWithDepositorsRequest request =
                new GetBankDepositByIdFromToDateReturnDepositWithDepositorsRequest();
        request.setDepositId(depositId);

        GregorianCalendar dateStart = new GregorianCalendar();
        GregorianCalendar dateEnd = new GregorianCalendar();
        dateStart.setTimeInMillis(startDate.getTime());
        dateStart.setTimeZone(TimeZone.getDefault());
        dateEnd.setTimeInMillis(endDate.getTime());
        dateEnd.setTimeZone(TimeZone.getDefault());
        XMLGregorianCalendar dateStartXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateStart);
        XMLGregorianCalendar dateEndXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateEnd);

        request.setStartDate(dateStartXml);
        request.setEndDate(dateEndXml);

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

}

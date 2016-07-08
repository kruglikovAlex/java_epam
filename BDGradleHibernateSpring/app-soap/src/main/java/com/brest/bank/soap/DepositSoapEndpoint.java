package com.brest.bank.soap;

import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositorService;

import com.brest.bank.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.Assert;

import org.springframework.ws.server.endpoint.annotation.*;

import javax.xml.bind.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Endpoint
public class DepositSoapEndpoint {

    public static final String ERROR_NULL_RESPONSE = "Response is NULL";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_NULL_PARAM = "The parameter must be NULL";
    public static final String ERROR_FROM_TO_PARAM = "The first parameter should be less than the second";
    public static final String ERROR_DEPOSIT = "In the database there is no Deposit with such parameters";
    public static final String ERROR_EMPTY_RESPONSE = "Response is EMPTY";

    private static final Logger LOGGER = LogManager.getLogger();

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final String NAMESPACE_URI = "http://bank.brest.com/soap";

    @Autowired
    BankDepositService depositService;

    @Autowired
    BankDepositorService depositorService;

    private BankDeposit deposit;
    private BankDepositReport depositReport;
    private BankDeposits deposits;
    private BankDepositor depositor;
    private BankDepositors depositors;
    private BankDepositsReport depositsReport;

    @Autowired
    public DepositSoapEndpoint(BankDepositService depositService,BankDepositorService depositorService){
        this.depositService = depositService;
        this.depositorService = depositorService;
    }

    /**
     * Get all Bank Deposits
     *
     * @return XmlElement List<BankDeposit>
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBankDepositsRequest")
    @ResponsePayload
    public GetBankDepositsResponse getBankDeposits(){
        LOGGER.debug("getBankDepositsRequest");

        deposits = new BankDeposits();
        GetBankDepositsResponse response = new GetBankDepositsResponse();
        int i = 0;
        for (com.brest.bank.domain.BankDeposit dd:depositService.getBankDeposits()
             ) {
            deposits.getBankDeposit().add(i,depositDaoToXml(dd));
            i++;
        }
        response.setBankDeposits(deposits);
        Assert.notEmpty(response.getBankDeposits().getBankDeposit(),ERROR_NULL_RESPONSE);

        return response;
    }

    /**
     * Get all Bank Depositors
     *
     * @return XmlElement List<BankDepositor>
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBankDepositorsRequest")
    @ResponsePayload
    public GetBankDepositorsResponse getBankDepositors() throws DatatypeConfigurationException{
        LOGGER.debug("getBankDepositorsRequest");

        depositors = new BankDepositors();
        GetBankDepositorsResponse response = new GetBankDepositorsResponse();
        int i = 0;
        for (com.brest.bank.domain.BankDepositor dd:depositorService.getBankDepositors()
                ) {
            depositors.getBankDepositor().add(i,depositorDaoToXml(dd));
            i++;
        }
        response.setBankDepositors(depositors);
        Assert.notEmpty(response.getBankDepositors().getBankDepositor(),ERROR_NULL_RESPONSE);

        return response;
    }

    /**
     * Get BankDeposit by depositId
     * @param request XmlElement depositId
     * @return XmlElement BankDeposit
     */
    @PayloadRoot(namespace = NAMESPACE_URI,  localPart = "getBankDepositByIdRequest")
    @ResponsePayload
    public GetBankDepositByIdResponse getBankDepositById(@RequestPayload GetBankDepositByIdRequest request)
            throws JAXBException{
        LOGGER.debug("getBankDepositByIdRequest - depositId={}",request.getDepositId());
        Assert.notNull(request.getDepositId(),ERROR_METHOD_PARAM);

        GetBankDepositByIdResponse response = new GetBankDepositByIdResponse();
        response.setBankDeposit(depositDaoToXml(depositService.getBankDepositById(request.getDepositId())));

        LOGGER.debug("getBankDepositByIdResponse - depositId={}",response.getBankDeposit().getDepositId());
        Assert.notNull(response.getBankDeposit(),ERROR_NULL_RESPONSE);

        return response;
    }

    /**
     * Get Bank Deposit by depositName
     *
     * @param request XmlElement depositName
     * @return XmlElement BankDeposit
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBankDepositByNameRequest")
    @ResponsePayload
    public GetBankDepositByNameResponse getBankDepositByName(@RequestPayload GetBankDepositByNameRequest request){
        LOGGER.debug("getBankDepositByNameRequest(depositName={})", request.getDepositName());
        Assert.notNull(request.getDepositName(),ERROR_METHOD_PARAM);

        GetBankDepositByNameResponse response = new GetBankDepositByNameResponse();
        response.setBankDeposit(depositDaoToXml(depositService.getBankDepositByName(request.getDepositName())));

        LOGGER.debug("getBankDepositByNameResponse - depositName={}",response.getBankDeposit().getDepositName());
        Assert.notNull(response.getBankDeposit(),ERROR_NULL_RESPONSE);

        return response;
    }

    /**
     * Get Bank Deposits by depositCurrency
     *
     * @param request XmlElement depositCurrency
     * @return XmlElement List<BankDeposit>
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBankDepositsByCurrencyRequest")
    @ResponsePayload
    public GetBankDepositsByCurrencyResponse getBankDepositsByCurrency(@RequestPayload GetBankDepositsByCurrencyRequest request) {
        LOGGER.debug("getBankDepositsByCurrencyRequest(depositCurrency={})",request.getDepositCurrency());
        Assert.notNull(request.getDepositCurrency(),ERROR_METHOD_PARAM);

        deposits = new BankDeposits();
        GetBankDepositsByCurrencyResponse response = new GetBankDepositsByCurrencyResponse();
        int i = 0;
        for (com.brest.bank.domain.BankDeposit dd:depositService.getBankDepositsByCurrency(request.getDepositCurrency())) {
            deposits.getBankDeposit().add(i,depositDaoToXml(dd));
            i++;
        }
        response.setBankDeposits(deposits);
        Assert.notEmpty(response.getBankDeposits().getBankDeposit(),ERROR_NULL_RESPONSE);

        return response;
    }

    /**
     * Get Bank Deposits by depositInterestRate
     *
     * @param request XmlElement depositInterestRate
     * @return XmlElement List<BankDeposit>
     */
    @PayloadRoot(localPart = "getBankDepositsByInterestRateRequest",namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositsByInterestRateResponse getBankDepositsByInterestRate(@RequestPayload GetBankDepositsByInterestRateRequest request) {
        LOGGER.debug("getBankDepositsByInterestRateRequest(depositInterestRate={})",request.getDepositInterestRate());
        Assert.notNull(request.getDepositInterestRate(),ERROR_METHOD_PARAM);

        deposits = new BankDeposits();
        GetBankDepositsByInterestRateResponse response = new GetBankDepositsByInterestRateResponse();
        int i = 0;
        for(com.brest.bank.domain.BankDeposit dd:depositService
                .getBankDepositsByInterestRate(request.getDepositInterestRate())){
            deposits.getBankDeposit().add(i,depositDaoToXml(dd));
            i++;
        }
        response.setBankDeposits(deposits);
        Assert.notNull(response.getBankDeposits(),ERROR_NULL_RESPONSE);

        return response;
    }

    /**
     * Get Bank Deposits from-to MIN TERM values
     *
     * @param request XmlElements: fromTerm and toTerm
     * @return XmlElement - List<BankDeposit>
     */
    @PayloadRoot(localPart = "getBankDepositsFromToMinTermRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositsFromToMinTermResponse getBankDepositsFromToMinTerm(@RequestPayload GetBankDepositsFromToMinTermRequest request){
        LOGGER.debug("getBankDepositsFromToMinTermRequest(from={}, to={})",request.getFromTerm(),request.getToTerm());
        Assert.notNull(request.getFromTerm(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getToTerm(),ERROR_METHOD_PARAM);
        Assert.isTrue(request.getFromTerm()<=request.getToTerm(),ERROR_FROM_TO_PARAM);

        deposits = new BankDeposits();
        GetBankDepositsFromToMinTermResponse response = new GetBankDepositsFromToMinTermResponse();
        int i = 0;
        for(com.brest.bank.domain.BankDeposit dd:depositService
                .getBankDepositsFromToMinTerm(request.getFromTerm(),request.getToTerm())){
            deposits.getBankDeposit().add(i,depositDaoToXml(dd));
            i++;
        }
        response.setBankDeposits(deposits);
        Assert.notEmpty(response.getBankDeposits().getBankDeposit(),ERROR_EMPTY_RESPONSE);
        Assert.notNull(response.getBankDeposits(),ERROR_NULL_RESPONSE);

        return response;
    }

    /**
     * Get Bank Deposits from-to Interest Rate values
     *
     * @param request XmlElement: startRate and endRate
     * @return response XmlElement Bank Deposits
     */
    @PayloadRoot(localPart = "getBankDepositsFromToInterestRateRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositsFromToInterestRateResponse getBankDepositsFromToInterestRate(@RequestPayload GetBankDepositsFromToInterestRateRequest request){
        LOGGER.debug("getBankDepositsFromToInterestRateRequest(from={}, to={})", request.getStartRate(),request.getEndRate());
        Assert.notNull(request.getStartRate(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getEndRate(),ERROR_METHOD_PARAM);
        Assert.isTrue(request.getStartRate()<=request.getEndRate(),ERROR_FROM_TO_PARAM);

        deposits = new BankDeposits();
        GetBankDepositsFromToInterestRateResponse response = new GetBankDepositsFromToInterestRateResponse();
        int i = 0;
        for(com.brest.bank.domain.BankDeposit dd:depositService
                .getBankDepositsFromToInterestRate(request.getStartRate(),request.getEndRate())){
            deposits.getBankDeposit().add(i,depositDaoToXml(dd));
            i++;
        }
        response.setBankDeposits(deposits);
        Assert.notEmpty(response.getBankDeposits().getBankDeposit(),ERROR_EMPTY_RESPONSE);
        Assert.notNull(response.getBankDeposits(),ERROR_NULL_RESPONSE);

        return response;
    }

    /**
     * Get Bank Deposits from-to date Bank Deposits
     *
     * @param request XmlElement: startDate and endDate
     * @return response XmlElement Bank Deposits
     */
    @PayloadRoot(localPart = "getBankDepositsFromToDateDepositRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositsFromToDateDepositResponse getBankDepositsFromToDateDeposit(@RequestPayload GetBankDepositsFromToDateDepositRequest request){
        LOGGER.debug("getBankDepositsFromToDateDepositRequest(from={}, to={})",request.getStartDate(),request.getEndDate());
        Assert.notNull(request.getStartDate(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getEndDate(),ERROR_METHOD_PARAM);

        Date dateStart = new Date(), dateEnd = new Date();
        dateStart.setTime(request.getStartDate().toGregorianCalendar().getTimeInMillis());
        dateEnd.setTime(request.getEndDate().toGregorianCalendar().getTimeInMillis());
        LOGGER.debug("dateStart={}, dateEnd={}",dateStart,dateEnd);
        Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

        deposits = new BankDeposits();
        GetBankDepositsFromToDateDepositResponse response = new GetBankDepositsFromToDateDepositResponse();
        int i = 0;
        for(com.brest.bank.domain.BankDeposit dd:depositService.getBankDepositsFromToDateDeposit(dateStart,dateEnd)){
            deposits.getBankDeposit().add(i,depositDaoToXml(dd));
        }
        response.setBankDeposits(deposits);
        Assert.notEmpty(response.getBankDeposits().getBankDeposit(),ERROR_EMPTY_RESPONSE);
        Assert.notNull(response.getBankDeposits(),ERROR_NULL_RESPONSE);

        return response;
    }

    /**
     * Get Bank Deposits from-to date return Bank Deposits
     *
     * @param request XmlElement: startDate and endDate
     * @return response XmlElement Bank Deposits
     */
    @PayloadRoot(localPart = "getBankDepositsFromToDateReturnDepositRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositsFromToDateReturnDepositResponse getBankDepositsFromToDateReturnDeposit(@RequestPayload GetBankDepositsFromToDateReturnDepositRequest request){
        LOGGER.debug("getBankDepositsFromToDateDepositRequest(from={}, to={})",request.getStartDate(),request.getEndDate());
        Assert.notNull(request.getStartDate(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getEndDate(),ERROR_METHOD_PARAM);

        Date dateStart = new Date(), dateEnd = new Date();
        dateStart.setTime(request.getStartDate().toGregorianCalendar().getTimeInMillis());
        dateEnd.setTime(request.getEndDate().toGregorianCalendar().getTimeInMillis());
        LOGGER.debug("dateStart={}, dateEnd={}",dateStart,dateEnd);
        Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

        deposits = new BankDeposits();
        GetBankDepositsFromToDateReturnDepositResponse response = new GetBankDepositsFromToDateReturnDepositResponse();
        int i = 0;
        for(com.brest.bank.domain.BankDeposit dd:depositService.getBankDepositsFromToDateReturnDeposit(dateStart,dateEnd)){
            deposits.getBankDeposit().add(i,depositDaoToXml(dd));
        }
        response.setBankDeposits(deposits);
        Assert.notEmpty(response.getBankDeposits().getBankDeposit(),ERROR_EMPTY_RESPONSE);
        Assert.notNull(response.getBankDeposits(),ERROR_NULL_RESPONSE);

        return response;
    }

    /**
     * Get Bank Deposit by depositName with report about all bank depositors
     *
     * @param request XmlElement depositName
     * @return XmlElement BankDepositReport
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBankDepositByNameWithDepositorsRequest")
    @ResponsePayload
    public GetBankDepositByNameWithDepositorsResponse getBankDepositByNameWithDepositors(@RequestPayload GetBankDepositByNameWithDepositorsRequest request){
        LOGGER.debug("getBankDepositByNameWithDepositorsRequest(depositName={})", request.getDepositName());
        Assert.notNull(request.getDepositName(),ERROR_METHOD_PARAM);

        GetBankDepositByNameWithDepositorsResponse response = new GetBankDepositByNameWithDepositorsResponse();
        response.setBankDepositReport(depositReportDaoToXml(depositService.getBankDepositByNameWithDepositors(request.getDepositName())));

        LOGGER.debug("getBankDepositByNameWithDepositorsResponse - depositName={}",response.getBankDepositReport().getDepositName());
        Assert.notNull(response.getBankDepositReport(),ERROR_NULL_RESPONSE);

        return response;
    }

    /**
     * Get Bank Deposits by depositName from-to date Bank Deposit of depositors
     *
     * @param request XmlElements: depositName, startDate deposit, endDate deposit
     * @return response XmlElement BankDepositReport
     */
    @PayloadRoot(localPart = "getBankDepositByNameFromToDateDepositWithDepositorsRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositByNameFromToDateDepositWithDepositorsResponse getBankDepositByNameFromToDateDepositWithDepositors(
            @RequestPayload GetBankDepositByNameFromToDateDepositWithDepositorsRequest request){
        LOGGER.debug("getBankDepositByNameFromToDateDepositWithDepositorsRequest(deposiName={}, startDate={}, endDate={})",
                request.getDepositName(),request.getStartDate(),request.getEndDate());
        Assert.notNull(request.getDepositName(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getStartDate(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getEndDate(),ERROR_METHOD_PARAM);

        Date dateStart=new Date(), dateEnd=new Date();
        dateStart.setTime(request.getStartDate().toGregorianCalendar().getTimeInMillis());
        dateEnd.setTime(request.getEndDate().toGregorianCalendar().getTimeInMillis());
        Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

        GetBankDepositByNameFromToDateDepositWithDepositorsResponse response =
                new GetBankDepositByNameFromToDateDepositWithDepositorsResponse();

        response.setBankDepositReport(depositReportDaoToXml(depositService
                .getBankDepositByNameFromToDateDepositWithDepositors(request.getDepositName(),dateStart,dateEnd)));
        LOGGER.debug("getBankDepositByNameFromToDateDepositWithDepositorsResponse - depositName={}",
                response.getBankDepositReport().getDepositName());
        Assert.notNull(response.getBankDepositReport(),ERROR_NULL_RESPONSE);

        return response;
    }

    /**
     * Get Bank Deposits by depositName from-to date return Bank Deposit of depositors
     *
     * @param request XmlElements: depositName, startDate deposit, endDate deposit
     * @return response XmlElement BankDepositReport
     */
    @PayloadRoot(localPart = "getBankDepositByNameFromToDateReturnDepositWithDepositorsRequest",
            namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositByNameFromToDateReturnDepositWithDepositorsResponse getBankDepositByNameFromToDateReturnDepositWithDepositors(
            @RequestPayload GetBankDepositByNameFromToDateReturnDepositWithDepositorsRequest request){
        LOGGER.debug("getBankDepositByNameFromToDateReturnDepositWithDepositorsRequest(deposiName={}, startDate={}, endDate={})",
                request.getDepositName(),request.getStartDate(),request.getEndDate());
        Assert.notNull(request.getDepositName(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getStartDate(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getEndDate(),ERROR_METHOD_PARAM);

        Date dateStart=new Date(), dateEnd=new Date();
        dateStart.setTime(request.getStartDate().toGregorianCalendar().getTimeInMillis());
        dateEnd.setTime(request.getEndDate().toGregorianCalendar().getTimeInMillis());
        Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

        GetBankDepositByNameFromToDateReturnDepositWithDepositorsResponse response =
                new GetBankDepositByNameFromToDateReturnDepositWithDepositorsResponse();

        response.setBankDepositReport(depositReportDaoToXml(depositService
                .getBankDepositByNameFromToDateReturnDepositWithDepositors(request.getDepositName(),dateStart,dateEnd)));
        LOGGER.debug("getBankDepositByNameFromToDateDepositWithDepositorsResponse - depositName={}",
                response.getBankDepositReport().getDepositName());
        Assert.notNull(response.getBankDepositReport(),ERROR_NULL_RESPONSE);

        return response;
    }

    /**
     * Get Bank Deposit by depositId with report about all bank depositors
     *
     * @param request XmlElement: depositId
     * @return response XmlElement BankDepositReport
     */
    @PayloadRoot(localPart = "getBankDepositByIdWithDepositorsRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositByIdWithDepositorsResponse getBankDepositByIdWithDepositors(
            @RequestPayload GetBankDepositByIdWithDepositorsRequest request){
        LOGGER.debug("getBankDepositByIdWithDepositorsRequest(depositId={})",request.getDepositId());
        Assert.notNull(request.getDepositId(),ERROR_METHOD_PARAM);

        GetBankDepositByIdWithDepositorsResponse response = new GetBankDepositByIdWithDepositorsResponse();

        response.setBankDepositReport(depositReportDaoToXml(
                depositService.getBankDepositByIdWithDepositors(request.getDepositId())));
        Assert.notNull(response.getBankDepositReport(),ERROR_NULL_RESPONSE);
        LOGGER.debug("getBankDepositByIdWithDepositorsResponse - depositName={}",
                response.getBankDepositReport().getDepositName());

        return response;
    }

    /**
     * Get Bank Deposit by depositId from-to depositorDateDeposit with report about all bank depositors
     *
     * @param request XmlElements: depositId, startDate deposit, endDate deposit
     * @return response XmlElement BankDepositReport
     */
    @PayloadRoot(localPart = "getBankDepositByIdFromToDateDepositWithDepositorsRequest",namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositByIdFromToDateDepositWithDepositorsResponse getBankDepositByIdFromToDateDepositWithDepositors(
            @RequestPayload GetBankDepositByIdFromToDateDepositWithDepositorsRequest request){
        LOGGER.debug("getBankDepositByIdFromToDateDepositWithDepositorsRequest(depositId={},startDate={},endDate={})",
                request.getDepositId(),request.getStartDate(),request.getEndDate());
        Assert.notNull(request.getDepositId(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getStartDate(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getEndDate(),ERROR_METHOD_PARAM);

        Date dateStart=new Date(), dateEnd=new Date();
        dateStart.setTime(request.getStartDate().toGregorianCalendar().getTimeInMillis());
        dateEnd.setTime(request.getEndDate().toGregorianCalendar().getTimeInMillis());
        Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

        GetBankDepositByIdFromToDateDepositWithDepositorsResponse response =
                new GetBankDepositByIdFromToDateDepositWithDepositorsResponse();

        response.setBankDepositReport(depositReportDaoToXml(
                depositService.getBankDepositByIdFromToDateDepositWithDepositors(request.getDepositId(),dateStart,dateEnd)));
        Assert.notNull(response.getBankDepositReport());
        LOGGER.debug("getBankDepositByIdFromToDateDepositWithDepositorsResponse - depositName={}",
                response.getBankDepositReport().getDepositName());

        return response;
    }

    /**
     * Get Bank Deposit by depositId from-to depositorDateReturnDeposit with report about all bank depositors
     *
     * @param request XmlElements: depositId, startDate deposit, endDate deposit
     * @return response XmlElement BankDepositReport
     */
    @PayloadRoot(localPart = "getBankDepositByIdFromToDateReturnDepositWithDepositorsRequest",namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositByIdFromToDateReturnDepositWithDepositorsResponse getBankDepositByIdFromToDateReturnDepositWithDepositors(
            @RequestPayload GetBankDepositByIdFromToDateReturnDepositWithDepositorsRequest request){
        LOGGER.debug("getBankDepositByIdFromToDateReturnDepositWithDepositorsRequest(depositId={},startDate={},endDate={})",
                request.getDepositId(),request.getStartDate(),request.getEndDate());
        Assert.notNull(request.getDepositId(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getStartDate(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getEndDate(),ERROR_METHOD_PARAM);

        Date dateStart=new Date(), dateEnd=new Date();
        dateStart.setTime(request.getStartDate().toGregorianCalendar().getTimeInMillis());
        dateEnd.setTime(request.getEndDate().toGregorianCalendar().getTimeInMillis());
        Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

        GetBankDepositByIdFromToDateReturnDepositWithDepositorsResponse response =
                new GetBankDepositByIdFromToDateReturnDepositWithDepositorsResponse();

        response.setBankDepositReport(depositReportDaoToXml(
                depositService.getBankDepositByIdFromToDateReturnDepositWithDepositors(request.getDepositId(),dateStart,dateEnd)));
        Assert.notNull(response.getBankDepositReport());
        LOGGER.debug("getBankDepositByIdFromToDateReturnDepositWithDepositorsResponse - depositName={}",
                response.getBankDepositReport().getDepositName());

        return response;
    }

    /**
     * Get all Bank Deposits with report about all Bank Depositors
     *
     * @return XmlElement List<Map>
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBankDepositsWithDepositorsRequest")
    @ResponsePayload
    public GetBankDepositsWithDepositorsResponse getBankDepositsWithDepositors(){
        LOGGER.debug("getBankDepositsWithDepositorsRequest");

        depositsReport = new BankDepositsReport();
        GetBankDepositsWithDepositorsResponse response = new GetBankDepositsWithDepositorsResponse();
        int i = 0;
        for (Map dd:depositService.getBankDepositsWithDepositors()
                ) {
            depositsReport.getBankDepositReport().add(i,depositReportDaoToXml(dd));
            i++;
        }
        response.setBankDepositsReport(depositsReport);
        Assert.notNull(response.getBankDepositsReport(),ERROR_NULL_RESPONSE);
        Assert.notEmpty(response.getBankDepositsReport().getBankDepositReport(),ERROR_EMPTY_RESPONSE);

        return response;
    }

    /**
     * Get all Bank Deposits with report about all Bank Depositors from-to date Bank Deposit
     *
     * @param request XmlElements: startDate deposit, endDate deposit
     * @return XmlElement List<Map>
     */
    @PayloadRoot(localPart = "getBankDepositsFromToDateDepositWithDepositorsRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositsFromToDateDepositWithDepositorsResponse getBankDepositsFromToDateDepositWithDepositors(
            @RequestPayload GetBankDepositsFromToDateDepositWithDepositorsRequest request){
        LOGGER.debug("getBankDepositsFromToDateDepositWithDepositorsRequest(startDate={}, endDate={})",
                request.getStartDate(),request.getEndDate());

        Assert.notNull(request.getStartDate(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getEndDate(),ERROR_METHOD_PARAM);

        Date dateStart=new Date(), dateEnd=new Date();
        dateStart.setTime(request.getStartDate().toGregorianCalendar().getTimeInMillis());
        dateEnd.setTime(request.getEndDate().toGregorianCalendar().getTimeInMillis());

        Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

        depositsReport = new BankDepositsReport();

        GetBankDepositsFromToDateDepositWithDepositorsResponse response =
                new GetBankDepositsFromToDateDepositWithDepositorsResponse();

        int i = 0;
        for(Map dd: depositService.getBankDepositsFromToDateDepositWithDepositors(dateStart,dateEnd)
                ){
            depositsReport.getBankDepositReport().add(i,depositReportDaoToXml(dd));
            i++;
        }
        response.setBankDepositsReport(depositsReport);
        Assert.notNull(response.getBankDepositsReport(),ERROR_NULL_RESPONSE);
        Assert.notEmpty(response.getBankDepositsReport().getBankDepositReport(),ERROR_EMPTY_RESPONSE);

        return response;
    }

    /**
     * Get all Bank Deposits with report about all Bank Depositors from-to date return Bank Deposit
     *
     * @param request XmlElements: startDate deposit, endDate deposit
     * @return XmlElement List<Map>
     */
    @PayloadRoot(localPart = "getBankDepositsFromToDateReturnDepositWithDepositorsRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositsFromToDateReturnDepositWithDepositorsResponse getBankDepositsFromToDateReturnDepositWithDepositors(
            @RequestPayload GetBankDepositsFromToDateReturnDepositWithDepositorsRequest request){
        LOGGER.debug("getBankDepositsFromToDateReturnDepositWithDepositorsRequest(startDate={}, endDate={})",
                request.getStartDate(),request.getEndDate());

        Assert.notNull(request.getStartDate(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getEndDate(),ERROR_METHOD_PARAM);

        Date dateStart=new Date(), dateEnd=new Date();
        dateStart.setTime(request.getStartDate().toGregorianCalendar().getTimeInMillis());
        dateEnd.setTime(request.getEndDate().toGregorianCalendar().getTimeInMillis());

        Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

        depositsReport = new BankDepositsReport();

        GetBankDepositsFromToDateReturnDepositWithDepositorsResponse response =
                new GetBankDepositsFromToDateReturnDepositWithDepositorsResponse();

        int i = 0;
        for(Map dd: depositService.getBankDepositsFromToDateReturnDepositWithDepositors(dateStart,dateEnd)
                ){
            depositsReport.getBankDepositReport().add(i,depositReportDaoToXml(dd));
            i++;
        }
        response.setBankDepositsReport(depositsReport);
        Assert.notNull(response.getBankDepositsReport(),ERROR_NULL_RESPONSE);
        Assert.notEmpty(response.getBankDepositsReport().getBankDepositReport(),ERROR_EMPTY_RESPONSE);

        return response;
    }

    /**
     * Get Bank Deposits by currency with report about all Bank Depositors
     *
     * @param request XmlElement: depositCurrency
     * @return XmlElement List<Map>
     */
    @PayloadRoot(localPart = "getBankDepositsByCurrencyWithDepositorsRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositsByCurrencyWithDepositorsResponse getBankDepositsByCurrencyWithDepositors(
            @RequestPayload GetBankDepositsByCurrencyWithDepositorsRequest request){
        LOGGER.debug("getBankDepositsByCurrencyWithDepositorsRequest(currency={})",request.getDepositCurrency());

        Assert.notNull(request.getDepositCurrency(),ERROR_METHOD_PARAM);

        depositsReport = new BankDepositsReport();

        GetBankDepositsByCurrencyWithDepositorsResponse response =
                new GetBankDepositsByCurrencyWithDepositorsResponse();

        int i = 0;
        for(Map dd: depositService.getBankDepositsByCurrencyWithDepositors(request.getDepositCurrency())
                ){
            depositsReport.getBankDepositReport().add(i,depositReportDaoToXml(dd));
            i++;
        }
        response.setBankDepositsReport(depositsReport);
        Assert.notNull(response.getBankDepositsReport(),ERROR_NULL_RESPONSE);
        Assert.notEmpty(response.getBankDepositsReport().getBankDepositReport(),ERROR_EMPTY_RESPONSE);

        return response;
    }

    /**
     * Get Bank Deposits by currency with report about all Bank Depositors from-to date Bank Deposit
     *
     * @param request XmlElement: depositCurrency, startDate deposit, endDate deposit
     * @return XmlElement List<Map>
     */
    @PayloadRoot(localPart = "getBankDepositsByCurrencyFromToDateDepositWithDepositorsRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse getBankDepositsByCurrencyFromToDateDepositWithDepositors(
            @RequestPayload GetBankDepositsByCurrencyFromToDateDepositWithDepositorsRequest request){
        LOGGER.debug("getBankDepositsByCurrencyFromToDateDepositWithDepositorsRequest(currency={}, startDate={}, endDate={})",
                request.getDepositCurrency(),request.getStartDate(),request.getEndDate());

        Assert.notNull(request.getDepositCurrency(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getStartDate(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getEndDate(),ERROR_METHOD_PARAM);

        Date dateStart=new Date(), dateEnd=new Date();
        dateStart.setTime(request.getStartDate().toGregorianCalendar().getTimeInMillis());
        dateEnd.setTime(request.getEndDate().toGregorianCalendar().getTimeInMillis());

        Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

        depositsReport = new BankDepositsReport();

        GetBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse response =
                new GetBankDepositsByCurrencyFromToDateDepositWithDepositorsResponse();

        int i = 0;
        for(Map dd: depositService.getBankDepositsByCurrencyFromToDateDepositWithDepositors(request.getDepositCurrency(),dateStart,dateEnd)
                ){
            depositsReport.getBankDepositReport().add(i,depositReportDaoToXml(dd));
            i++;
        }
        response.setBankDepositsReport(depositsReport);
        Assert.notNull(response.getBankDepositsReport(),ERROR_NULL_RESPONSE);
        Assert.notEmpty(response.getBankDepositsReport().getBankDepositReport(),ERROR_EMPTY_RESPONSE);

        return response;
    }

    /**
     * Get Bank Deposits by currency with report about all Bank Depositors from-to date Bank Deposit
     *
     * @param request XmlElement: depositCurrency, startDate deposit, endDate deposit
     * @return XmlElement List<Map>
     */
    @PayloadRoot(localPart = "getBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsRequest",
            namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsResponse getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(
            @RequestPayload GetBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsRequest request){
        LOGGER.debug("getBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsRequest(currency={}, startDate={}, endDate={})",
                request.getDepositCurrency(),request.getStartDate(),request.getEndDate());

        Assert.notNull(request.getDepositCurrency(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getStartDate(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getEndDate(),ERROR_METHOD_PARAM);

        Date dateStart=new Date(), dateEnd=new Date();
        dateStart.setTime(request.getStartDate().toGregorianCalendar().getTimeInMillis());
        dateEnd.setTime(request.getEndDate().toGregorianCalendar().getTimeInMillis());

        Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

        depositsReport = new BankDepositsReport();

        GetBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsResponse response =
                new GetBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsResponse();

        int i = 0;
        for(Map dd: depositService.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(
                                                request.getDepositCurrency(),
                                                dateStart,
                                                dateEnd)
                ){
            depositsReport.getBankDepositReport().add(i,depositReportDaoToXml(dd));
            i++;
        }
        response.setBankDepositsReport(depositsReport);
        Assert.notNull(response.getBankDepositsReport(),ERROR_NULL_RESPONSE);
        Assert.notEmpty(response.getBankDepositsReport().getBankDepositReport(),ERROR_EMPTY_RESPONSE);

        return response;
    }

    /**
     * Adding Bank Deposit
     *
     * @param request XmlElement BankDeposit
     * @return XmlElement added BankDeposit
     * @throws IOException
     */
    @PayloadRoot(localPart = "addBankDepositRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public AddBankDepositResponse addBankDeposit(@RequestPayload AddBankDepositRequest request) throws IOException{
        LOGGER.debug("addBankDepositRequest(depositId={})",request.getBankDeposit().getDepositId());
        Assert.notNull(request.getBankDeposit(),ERROR_METHOD_PARAM);
        Assert.isNull(request.getBankDeposit().getDepositId(),ERROR_NULL_PARAM);

        AddBankDepositResponse response = new AddBankDepositResponse();

        depositService.addBankDeposit(xmlToDepositDao(request.getBankDeposit()));

        response.setBankDeposit(depositDaoToXml(depositService.getBankDepositByName(request.getBankDeposit().getDepositName())));

        Assert.notNull(response.getBankDeposit(),ERROR_NULL_RESPONSE);
        LOGGER.debug("addBankDepositResponse(depositId={})",response.getBankDeposit().getDepositId());

        return response;
    }

    /**
     * Removing Bank Deposit
     *
     * @param request XmlElement Bank Deposit
     * @return XmlElement result String
     */
    @PayloadRoot(localPart = "deleteBankDepositRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public DeleteBankDepositResponse deleteBankDeposit(@RequestPayload DeleteBankDepositRequest request){
        LOGGER.debug("deleteBankDepositRequest(depositId={})",request.getDepositId());
        Assert.notNull(request.getDepositId(),ERROR_METHOD_PARAM);

        DeleteBankDepositResponse response = new DeleteBankDepositResponse();
        try{
            Assert.notNull(depositService.getBankDepositById(request.getDepositId()),ERROR_DEPOSIT);
            depositService.deleteBankDeposit(request.getDepositId());
            Assert.isNull(depositService.getBankDepositById(request.getDepositId()),"Bank Deposit don't removed");
            response.setResult("Bank Deposit removed");
        } catch (Exception e){
            response.setResult(e.getMessage());
        }
        return response;
    }

    /**
     * Updating Bank Deposit
     *
     * @param request XmlElement Bank Deposit
     * @return response XmlElement Bank Deposit
     */
    @PayloadRoot(localPart = "updateBankDepositRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public UpdateBankDepositResponse updateBankDeposit(@RequestPayload UpdateBankDepositRequest request){
        LOGGER.debug("updateBankDepositRequest(depositId={})",request.getBankDeposit().getDepositId());
        Assert.notNull(request,ERROR_METHOD_PARAM +"- Bank Deposit");
        Assert.notNull(request.getBankDeposit().getDepositId(),ERROR_METHOD_PARAM+"- depositId");

        UpdateBankDepositResponse response = new UpdateBankDepositResponse();

        depositService.updateBankDeposit(xmlToDepositDao(request.getBankDeposit()));

        response.setBankDeposit(depositDaoToXml(depositService.getBankDepositById(request.getBankDeposit().getDepositId())));

        Assert.notNull(response.getBankDeposit(),ERROR_NULL_RESPONSE);
        LOGGER.debug("updateBankDepositResponse(depositId={})",response.getBankDeposit().getDepositId());

        return response;
    }

    /**
     * Get Bank Depositor by depositorId
     *
     * @param request XmlElement depositorId
     * @return XmlElement BankDepositor
     * @throws DatatypeConfigurationException
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBankDepositorByIdRequest")
    @ResponsePayload
    public GetBankDepositorByIdResponse getBankDepositorById(@RequestPayload GetBankDepositorByIdRequest request)
                                                                            throws DatatypeConfigurationException{
        LOGGER.debug("getBankDepositorByIdRequest - depositorId={}",request.getDepositorId());
        Assert.notNull(request.getDepositorId(),ERROR_METHOD_PARAM);

        GetBankDepositorByIdResponse response = new GetBankDepositorByIdResponse();
        response.setBankDepositor(depositorDaoToXml(depositorService.getBankDepositorById(request.getDepositorId())));
        LOGGER.debug("getBankDepositorByIdResponse - depositorId={}",response.getBankDepositor().getDepositorId());

        Assert.notNull(response.getBankDepositor(),ERROR_NULL_RESPONSE);
        return response;
    }

    /**
     * Get Bank Depositor by depositId
     *
     * @param request XmlElement depositId
     * @return XmlElement BankDepositor
     * @throws DatatypeConfigurationException
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBankDepositorByIdDepositRequest")
    @ResponsePayload
    public GetBankDepositorByIdDepositResponse getBankDepositorByIdDeposit(@RequestPayload GetBankDepositorByIdDepositRequest request)
            throws DatatypeConfigurationException{
        LOGGER.debug("getBankDepositorByIdDepositRequest - deposiId={}",request.getDepositId());
        Assert.notNull(request.getDepositId(),ERROR_METHOD_PARAM);

        depositors = new BankDepositors();

        GetBankDepositorByIdDepositResponse response = new GetBankDepositorByIdDepositResponse();
        int i = 0;
        for(com.brest.bank.domain.BankDepositor dd:depositorService.getBankDepositorByIdDeposit(request.getDepositId())){
            depositors.getBankDepositor().add(i,depositorDaoToXml(dd));
        }
        response.setBankDepositors(depositors);

        Assert.notEmpty(response.getBankDepositors().getBankDepositor(),ERROR_EMPTY_RESPONSE);
        Assert.notNull(response.getBankDepositors(),ERROR_NULL_RESPONSE);

        return response;
    }

    /**
     * Get Bank Depositor by depositorName
     *
     * @param request XmlElement depositorName
     * @return XmlElement BankDepositor
     * @throws DatatypeConfigurationException
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBankDepositorByNameRequest")
    @ResponsePayload
    public GetBankDepositorByNameResponse getBankDepositorByName(@RequestPayload GetBankDepositorByNameRequest request)
            throws DatatypeConfigurationException{
        LOGGER.debug("getBankDepositorByNameRequest - depositorName={}",request.getDepositorName());
        Assert.notNull(request.getDepositorName(),ERROR_METHOD_PARAM);

        GetBankDepositorByNameResponse response = new GetBankDepositorByNameResponse();
        response.setBankDepositor(depositorDaoToXml(depositorService.getBankDepositorByName(request.getDepositorName())));
        LOGGER.debug("getBankDepositorByNameResponse - depositorName={}",response.getBankDepositor().getDepositorName());

        Assert.notNull(response.getBankDepositor(),ERROR_NULL_RESPONSE);
        return response;
    }

    /**
     * Get Bank Depositors from-to date return Bank Deposits
     *
     * @param request XmlElement: startDate and endDate
     * @return response XmlElement Bank Depositors
     */
    @PayloadRoot(localPart = "getBankDepositorsFromToDateDepositRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositorsFromToDateDepositResponse getBankDepositorsFromToDateDeposit(@RequestPayload GetBankDepositorsFromToDateDepositRequest request)
            throws DatatypeConfigurationException{
        LOGGER.debug("getBankDepositorsFromToDateDepositRequest(from={}, to={})",request.getStartDate(),request.getEndDate());
        Assert.notNull(request.getStartDate(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getEndDate(),ERROR_METHOD_PARAM);

        Date dateStart = new Date(), dateEnd = new Date();
        dateStart.setTime(request.getStartDate().toGregorianCalendar().getTimeInMillis());
        dateEnd.setTime(request.getEndDate().toGregorianCalendar().getTimeInMillis());
        LOGGER.debug("dateStart={}, dateEnd={}",dateStart,dateEnd);
        Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

        depositors = new BankDepositors();
        GetBankDepositorsFromToDateDepositResponse response = new GetBankDepositorsFromToDateDepositResponse();
        int i = 0;
        for(com.brest.bank.domain.BankDepositor dd:depositorService.getBankDepositorsFromToDateDeposit(dateStart,dateEnd)){
            depositors.getBankDepositor().add(i,depositorDaoToXml(dd));
        }
        response.setBankDepositors(depositors);
        Assert.notEmpty(response.getBankDepositors().getBankDepositor(),ERROR_EMPTY_RESPONSE);
        Assert.notNull(response.getBankDepositors(),ERROR_NULL_RESPONSE);

        return response;
    }

    /**
     * Get Bank Depositors from-to date return Bank Deposits
     *
     * @param request XmlElement: startDate and endDate
     * @return response XmlElement Bank Depositors
     */
    @PayloadRoot(localPart = "getBankDepositorsFromToDateReturnDepositRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetBankDepositorsFromToDateReturnDepositResponse getBankDepositorsFromToDateReturnDeposit(@RequestPayload GetBankDepositorsFromToDateReturnDepositRequest request)
            throws DatatypeConfigurationException{
        LOGGER.debug("getBankDepositorsFromToDateReturnDepositRequest(from={}, to={})",request.getStartDate(),request.getEndDate());
        Assert.notNull(request.getStartDate(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getEndDate(),ERROR_METHOD_PARAM);

        Date dateStart = new Date(), dateEnd = new Date();
        dateStart.setTime(request.getStartDate().toGregorianCalendar().getTimeInMillis());
        dateEnd.setTime(request.getEndDate().toGregorianCalendar().getTimeInMillis());
        LOGGER.debug("dateStart={}, dateEnd={}",dateStart,dateEnd);
        Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

        depositors = new BankDepositors();
        GetBankDepositorsFromToDateReturnDepositResponse response = new GetBankDepositorsFromToDateReturnDepositResponse();
        int i = 0;
        for(com.brest.bank.domain.BankDepositor dd:depositorService.getBankDepositorsFromToDateReturnDeposit(dateStart,dateEnd)){
            depositors.getBankDepositor().add(i,depositorDaoToXml(dd));
        }
        response.setBankDepositors(depositors);
        Assert.notEmpty(response.getBankDepositors().getBankDepositor(),ERROR_EMPTY_RESPONSE);
        Assert.notNull(response.getBankDepositors(),ERROR_NULL_RESPONSE);

        return response;
    }

    /**
     * Adding Bank Depositor
     *
     * @param request XmlElement: depositId, BankDepositor
     * @return XmlElement added BankDepositor
     * @throws IOException
     */
    @PayloadRoot(localPart = "addBankDepositorRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public AddBankDepositorResponse addBankDepositor(@RequestPayload AddBankDepositorRequest request) throws DatatypeConfigurationException{
        LOGGER.debug("addBankDepositorRequest(depositId={}, depositorId={})",request.getDepositId(),request.getBankDepositor().getDepositorId());
        Assert.notNull(request.getDepositId(),ERROR_METHOD_PARAM);
        Assert.notNull(request.getBankDepositor(),ERROR_METHOD_PARAM);
        Assert.isNull(request.getBankDepositor().getDepositorId(),ERROR_NULL_PARAM +"- depositorId");

        AddBankDepositorResponse response = new AddBankDepositorResponse();

        depositorService.addBankDepositor(request.getDepositId(),xmlToDepositorDao(request.getBankDepositor()));

        response.setBankDepositor(depositorDaoToXml(depositorService.getBankDepositorByName(request.getBankDepositor().getDepositorName())));

        Assert.notNull(response.getBankDepositor(),ERROR_NULL_RESPONSE);
        LOGGER.debug("addBankDepositorResponse(depositorId={})",response.getBankDepositor().getDepositorId());

        return response;
    }

    /**
     * Removing Bank Depositor
     *
     * @param request XmlElement Bank Depositor
     * @return XmlElement result String
     */
    @PayloadRoot(localPart = "deleteBankDepositorRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public DeleteBankDepositorResponse deleteBankDepositor(@RequestPayload DeleteBankDepositorRequest request){
        LOGGER.debug("deleteBankDepositorRequest(depositorId={})",request.getDepositorId());
        Assert.notNull(request.getDepositorId(),ERROR_METHOD_PARAM);

        DeleteBankDepositorResponse response = new DeleteBankDepositorResponse();
        try{
            Assert.notNull(depositorService.getBankDepositorById(request.getDepositorId()),ERROR_DEPOSIT);
            depositorService.removeBankDepositor(request.getDepositorId());
            Assert.isNull(depositorService.getBankDepositorById(request.getDepositorId()),"Bank Depositor don't removed");
            response.setResult("Bank Depositor with ID="+request.getDepositorId()+" - removed");
        } catch (Exception e){
            response.setResult(e.getMessage());
        }
        return response;
    }

    /**
     * Updating Bank Depositor
     *
     * @param request XmlElement Bank Depositor
     * @return response XmlElement Bank Depositor
     */
    @PayloadRoot(localPart = "updateBankDepositorRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public UpdateBankDepositorResponse updateBankDepositor(@RequestPayload UpdateBankDepositorRequest request)
            throws DatatypeConfigurationException{
        LOGGER.debug("updateBankDepositorRequest(depositorId={})",request.getBankDepositor().getDepositorId());
        Assert.notNull(request,ERROR_METHOD_PARAM +"- Bank Deposit");
        Assert.notNull(request.getBankDepositor().getDepositorId(),ERROR_METHOD_PARAM +"- depositorId");

        UpdateBankDepositorResponse response = new UpdateBankDepositorResponse();

        depositorService.updateBankDepositor(xmlToDepositorDao(request.getBankDepositor()));

        response.setBankDepositor(depositorDaoToXml(depositorService.getBankDepositorById(request.getBankDepositor().getDepositorId())));

        Assert.notNull(response.getBankDepositor(),ERROR_NULL_RESPONSE);
        LOGGER.debug("updateBankDepositorResponse(depositorId={})",response.getBankDepositor().getDepositorId());

        return response;
    }


    /**
     * Convert object of domain BankDeposit to soap BankDeposit
     *
     * @param depositDao - an entity of domain BankDeposit.class
     * @return XmlElement BankDeposit
     */
    public BankDeposit depositDaoToXml(com.brest.bank.domain.BankDeposit depositDao){
        deposit = new BankDeposit();
            deposit.setDepositId(depositDao.getDepositId());
            deposit.setDepositName(depositDao.getDepositName());
            deposit.setDepositMinTerm(depositDao.getDepositMinTerm());
            deposit.setDepositMinAmount(depositDao.getDepositMinAmount());
            deposit.setDepositCurrency(depositDao.getDepositCurrency());
            deposit.setDepositInterestRate(depositDao.getDepositInterestRate());
            deposit.setDepositAddConditions(depositDao.getDepositAddConditions());

        return deposit;
    }

    /**
     * Convert object of domain BankDeposit to soap BankDeposit
     *
     * @param depositReportDao - Map  - a bank deposit with a report on all relevant
     * bank depositors
     * @return XmlElement BankDepositReport
     */
    public BankDepositReport depositReportDaoToXml(Map depositReportDao){
        depositReport = new BankDepositReport();
            depositReport.setDepositId((Long)depositReportDao.get("depositId"));
            depositReport.setDepositName(depositReportDao.get("depositName").toString());
            depositReport.setDepositMinTerm((Integer)depositReportDao.get("depositMinTerm"));
            depositReport.setDepositMinAmount((Integer)depositReportDao.get("depositMinAmount"));
            depositReport.setDepositCurrency(depositReportDao.get("depositCurrency").toString());
            depositReport.setDepositInterestRate((Integer)depositReportDao.get("depositInterestRate"));
            depositReport.setDepositAddConditions(depositReportDao.get("depositAddConditions").toString());
            depositReport.setDepositorCount(Integer.parseInt(depositReportDao.get("depositorCount").toString()));
            depositReport.setDepositorAmountSum(Integer.parseInt(depositReportDao.get("depositorAmountSum").toString()));
            depositReport.setDepositorAmountPlusSum(Integer.parseInt(depositReportDao.get("depositorAmountPlusSum").toString()));
            depositReport.setDepositorAmountMinusSum(Integer.parseInt(depositReportDao.get("depositorAmountMinusSum").toString()));

        return depositReport;
    }

    public com.brest.bank.domain.BankDeposit xmlToDepositDao(BankDeposit depositXml){
        com.brest.bank.domain.BankDeposit deposit = new com.brest.bank.domain.BankDeposit();
            deposit.setDepositId(depositXml.getDepositId());
            deposit.setDepositName(depositXml.getDepositName());
            deposit.setDepositMinTerm(depositXml.getDepositMinTerm());
            deposit.setDepositMinAmount(depositXml.getDepositMinAmount());
            deposit.setDepositCurrency(depositXml.getDepositCurrency());
            deposit.setDepositInterestRate(depositXml.getDepositInterestRate());
            deposit.setDepositAddConditions(depositXml.getDepositAddConditions());

        LOGGER.debug("depositDao - {}",deposit.toString());
        return deposit;
    }

    /**
     * Convert object of domain BankDepositor to soap BankDepositor
     *
     * @param depositorDao - an entity of domain BankDepositor.class
     * @return XmlElement List<BankDepositor>
     * @throws DatatypeConfigurationException
     */
    public BankDepositor depositorDaoToXml(com.brest.bank.domain.BankDepositor depositorDao)
                                                        throws DatatypeConfigurationException{
        GregorianCalendar dateDeposit = new GregorianCalendar();
        GregorianCalendar dateReturnDeposit = new GregorianCalendar();

        dateDeposit.setTime(depositorDao.getDepositorDateDeposit());
        dateReturnDeposit.setTime(depositorDao.getDepositorDateReturnDeposit());

        XMLGregorianCalendar xmlDateDeposit = DatatypeFactory
                .newInstance()
                .newXMLGregorianCalendarDate(dateDeposit.get(Calendar.YEAR),
                        dateDeposit.get(Calendar.MONTH)+1,
                        dateDeposit.get(Calendar.DAY_OF_MONTH), 0);

        XMLGregorianCalendar xmlDateReturnDeposit = DatatypeFactory
                .newInstance()
                .newXMLGregorianCalendarDate(dateReturnDeposit.get(Calendar.YEAR),
                        dateReturnDeposit.get(Calendar.MONTH)+1,
                        dateReturnDeposit.get(Calendar.DAY_OF_MONTH),0);

        depositor = new BankDepositor();
            depositor.setDepositorId(depositorDao.getDepositorId());
            depositor.setDepositorName(depositorDao.getDepositorName());
            depositor.setDepositorDateDeposit(xmlDateDeposit);
            depositor.setDepositorAmountDeposit(depositorDao.getDepositorAmountDeposit());
            depositor.setDepositorAmountPlusDeposit(depositorDao.getDepositorAmountPlusDeposit());
            depositor.setDepositorAmountMinusDeposit(depositorDao.getDepositorAmountMinusDeposit());
            depositor.setDepositorDateReturnDeposit(xmlDateReturnDeposit);
            depositor.setDepositorMarkReturnDeposit(depositorDao.getDepositorMarkReturnDeposit());

        return depositor;
    }

    public com.brest.bank.domain.BankDepositor xmlToDepositorDao(BankDepositor depositorXml){
        Date depositorDate, depositorDateReturn;

        depositorDate = depositorXml.getDepositorDateDeposit().toGregorianCalendar().getTime();
        depositorDateReturn = depositorXml.getDepositorDateReturnDeposit().toGregorianCalendar().getTime();

        com.brest.bank.domain.BankDepositor depositor = new com.brest.bank.domain.BankDepositor();
            depositor.setDepositorId(depositorXml.getDepositorId());
            depositor.setDepositorName(depositorXml.getDepositorName());
            depositor.setDepositorDateDeposit(depositorDate);
            depositor.setDepositorAmountDeposit(depositorXml.getDepositorAmountDeposit());
            depositor.setDepositorAmountPlusDeposit(depositorXml.getDepositorAmountPlusDeposit());
            depositor.setDepositorAmountMinusDeposit(depositorXml.getDepositorAmountMinusDeposit());
            depositor.setDepositorDateReturnDeposit(depositorDateReturn);
            depositor.setDepositorMarkReturnDeposit(depositorXml.getDepositorMarkReturnDeposit());


        LOGGER.debug("depositorDao - {}",depositor.toString());
        return depositor;
    }

/*
    private static BankDeposit jaxbXMLToObject() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BankDeposit.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringBuffer xmlStr = new StringBuffer( );
            Object o = unmarshaller.unmarshal( new StreamSource( new StringReader( xmlStr.toString() ) ) );
            //BankDeposit deposit = (BankDeposit) un.unmarshal(new File(FILE_NAME));
            return deposit;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /*

    //------
    if(isRequestValid(accountBalanceRequest)) {
			response = createDummyResponse();
		} else {
			throw new AccountNumberNotFoundException(
					"Account number " + accountBalanceRequest.getNumber() + " not valid.");
		}
    //------
    private boolean isRequestValid(final AccountBalanceRequest accountBalanceRequest) {
		final boolean valid;

		if(accountBalanceRequest.getNumber().startsWith("9")) {
			valid = false;
		} else {
			valid = true;
		}

		return valid;
	}
     */
}

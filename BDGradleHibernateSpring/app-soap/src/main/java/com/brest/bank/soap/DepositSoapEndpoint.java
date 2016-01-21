package com.brest.bank.soap;

import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositorService;

import com.brest.bank.util.BankDeposit;
import com.brest.bank.util.BankDepositor;

import org.apache.commons.io.input.XmlStreamReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.util.Assert;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.*;

import com.brest.bank.util.*;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.test.client.RequestMatchers;
import org.springframework.ws.test.server.RequestCreator;
import org.springframework.ws.test.server.ResponseMatchers;
import org.springframework.xml.transform.StringSource;

import javax.xml.bind.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

@Endpoint
@ContextConfiguration(locations = {"classpath:/spring-soap.xml"})
public class DepositSoapEndpoint {

    public static final String ERROR_NULL_RESPONSE = "Response is NULL";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_NULL_PARAM = "The parameter must be NULL";
    public static final String ERROR_FROM_TO_PARAM = "The first parameter should be less than the second";
    public static final String ERROR_DEPOSIT = "In the database there is no Deposit with such parameters";
    public static final String ERROR_EMPTY_RESPONSE = "Response is EMPTY";

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String NAMESPACE_URI = "http://bank.brest.com/soap";

    @Autowired
    BankDepositService depositService;

    @Autowired
    BankDepositorService depositorService;
    ObjectFactory objectFactory = new ObjectFactory();
    private BankDeposit deposit;
    private BankDeposits deposits;
    private BankDepositor depositor;
    private BankDepositors depositors;

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
        Assert.notNull(request,ERROR_METHOD_PARAM+"- Bank Deposit");
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
}

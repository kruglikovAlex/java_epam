package com.brest.bank.soap;

import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositorService;

import com.brest.bank.util.BankDeposit;
import com.brest.bank.util.BankDepositor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.util.Assert;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.brest.bank.util.*;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Endpoint
@ContextConfiguration(locations = {"classpath:/spring-soap.xml"})
public class DepositSoapEndpoint {

    public static final String ERROR_DB_EMPTY = "There is no RECORDS in the DataBase";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_NULL_PARAM = "The parameter must be NULL";
    public static final String ERROR_FROM_TO_PARAM = "The first parameter should be less than the second";
    public static final String ERROR_DEPOSIT = "In the database there is no Deposit with such parameters";
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String NAMESPACE_URI = "http://bank.brest.com/soap";

    @Autowired
    BankDepositService depositService;

    @Autowired
    BankDepositorService depositorService;

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
            deposits.getBankDeposit().add(i,depositDaoToSoap(dd));
            i++;
        }
        response.setBankDeposits(deposits);
        Assert.notEmpty(response.getBankDeposits().getBankDeposit(),"The service was return Null Bank Deposit");

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
            depositors.getBankDepositor().add(i,depositorDaoToSoap(dd));
            i++;
        }
        response.setBankDepositors(depositors);
        Assert.notEmpty(response.getBankDepositors().getBankDepositor(),"The service was return Null Bank Depositor");

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
        response.setBankDeposit(depositDaoToSoap(depositService.getBankDepositById(request.getDepositId())));

        LOGGER.debug("getBankDepositByIdResponse - depositId={}",response.getBankDeposit().getDepositId());
        Assert.notNull(response.getBankDeposit(),"The service was return Null Bank Deposit");

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
        LOGGER.debug("getBankDepositByNameRequest - depositName={}", request.getDepositName());
        Assert.notNull(request.getDepositName(),ERROR_METHOD_PARAM);

        GetBankDepositByNameResponse response = new GetBankDepositByNameResponse();
        response.setBankDeposit(depositDaoToSoap(depositService.getBankDepositByName(request.getDepositName())));

        LOGGER.debug("getBankDepositByNameResponse - depositName={}",response.getBankDeposit().getDepositName());
        Assert.notNull(response.getBankDeposit(),"The service was return Null Bank Deposit");

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
        response.setBankDepositor(depositorDaoToSoap(depositorService.getBankDepositorById(request.getDepositorId())));
        LOGGER.debug("getBankDepositorByIdResponse - depositorId={}",response.getBankDepositor().getDepositorId());

        Assert.notNull(response.getBankDepositor(),"The service was return Null Bank Depositor");
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
        response.setBankDepositor(depositorDaoToSoap(depositorService.getBankDepositorByName(request.getDepositorName())));
        LOGGER.debug("getBankDepositorByNameResponse - depositorName={}",response.getBankDepositor().getDepositorName());

        Assert.notNull(response.getBankDepositor(),"The service was return Null Bank Depositor");
        return response;
    }

    /**
     * Convert object of domain BankDeposit to soap BankDeposit
     *
     * @param depositDao - an entity of domain BankDeposit.class
     * @return XmlElement BankDeposit
     */
    public BankDeposit depositDaoToSoap(com.brest.bank.domain.BankDeposit depositDao){
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
     * Convert object of domain BankDepositor to soap BankDepositor
     *
     * @param depositorDao - an entity of domain BankDepositor.class
     * @return XmlElement List<BankDepositor>
     * @throws DatatypeConfigurationException
     */
    public BankDepositor depositorDaoToSoap(com.brest.bank.domain.BankDepositor depositorDao)
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
    * public static String marshal(Car car) throws JAXBException {
        StringWriter stringWriter = new StringWriter();

        JAXBContext jaxbContext = JAXBContext.newInstance(Car.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // format the XML output
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        QName qName = new QName("info.source4code.jaxb.model", "car");
        JAXBElement<Car> root = new JAXBElement<Car>(qName, Car.class, car);

        jaxbMarshaller.marshal(root, stringWriter);

        String result = stringWriter.toString();
        LOGGER.info(result);
        return result;
    }*/
}

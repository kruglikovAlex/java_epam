package com.brest.bank.soap;

import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

import static com.brest.bank.soap.*;

import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Endpoint
@ContextConfiguration(locations = {"classpath:/spring-soap.xml"})
public class DepositSoapEndpoint {

    private static final String NAMESPACE_URI = "http://bank.brest.com/soap";

    @Autowired
    private BankDepositService depositService;

    @Autowired
    private BankDepositorService depositorService;

    private BankDeposit deposit;
    private BankDepositor depositor;
    private BankDeposits deposits;
    private com.brest.bank.domain.BankDeposit depositDao;

    @Autowired
    public DepositSoapEndpoint(BankDepositService depositService,BankDepositorService depositorService){
        this.depositService = depositService;
        this.depositorService = depositorService;
    }

    /**
     * Get all Bank Deposits
     *
     * @return
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBankDepositsRequest")
    @ResponsePayload
    public GetBankDepositsResponse getBankDeposits(){
        GetBankDepositsResponse response = new GetBankDepositsResponse();
        int i = 0;
        for (com.brest.bank.domain.BankDeposit dd:depositService.getBankDeposits()
             ) {
            deposits.bankDeposit.add(i,depositDaoToSoap(dd));
            i++;
        }
        response.setBankDeposits(deposits);
        return response;
    }
    /**
     * Get BankDeposit by depositId
     * @param request
     * @return
     */
    @PayloadRoot(namespace = NAMESPACE_URI,  localPart = "getBankDepositByIdRequest")
    @ResponsePayload
    public GetBankDepositByIdResponse getBankDeposit(@RequestPayload GetBankDepositByIdRequest request){
        GetBankDepositByIdResponse response = new GetBankDepositByIdResponse();
        response.setBankDeposit(depositDaoToSoap(depositService.getBankDepositById(request.depositId)));
        return response;
    }

    /**
     * Get Bank Depositor by depositorId
     *
     * @param request
     * @return
     * @throws DatatypeConfigurationException
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBankDepositorByIdRequest")
    @ResponsePayload
    public GetBankDepositorByIdResponse getBankDepositorById(@RequestPayload GetBankDepositorByIdRequest request)
                                                                            throws DatatypeConfigurationException{
        GetBankDepositorByIdResponse response = new GetBankDepositorByIdResponse();
        response.setBankDepositor(depositorDaoToSoap(depositorService.getBankDepositorById(request.depositorId)));
        return response;
    }

    /**
     * Convert object of domain BankDeposit to soap BankDeposit
     *
     * @param depositDao
     * @return
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
     * @param depositorDao
     * @return
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
}

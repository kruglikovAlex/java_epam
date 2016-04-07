package com.brest.bank.soap;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;


@SuppressWarnings("serial")
@SoapFault(faultCode = FaultCode.RECEIVER)
public class DepositSoapNotFoundException extends RuntimeException{

    public DepositSoapNotFoundException(String message){
        super(message);
    }
}

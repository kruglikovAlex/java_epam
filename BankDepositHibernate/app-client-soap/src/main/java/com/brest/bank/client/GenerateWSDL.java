package com.brest.bank.client;

/**
 * Created by alexander on 11.8.15.
 */
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.brest.bank.domain.BankDeposit;
//import com.predic8.wsdl.soap11.SOAPOperation;
import com.predic8.wsdl.soap11.SOAPBinding;
import com.predic8.wsdl.soap11.SOAPOperation;
import com.predic8.wsdl.soap12.SOAPBody;
import com.sun.org.apache.xml.internal.utils.NameSpace;
import groovy.xml.MarkupBuilder;

import com.predic8.wsdl.*;
import com.predic8.wsdl.creator.*;
import com.predic8.schema.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.plugin2.message.transport.Transport;

import javax.jws.soap.SOAPBinding.Style;
import javax.xml.soap.SOAPElement;

import static com.predic8.schema.Schema.*;

public class GenerateWSDL {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        dumpWSDL(createWSDL());
    }

    public static Definitions createWSDL() {
        Schema schema = new Schema("http://client.bank.brest.com/Soap/");

            schema.newElement("getDepositsRequest").newComplexType().newSequence().newElement("summand", INT).setMaxOccurs("unbounded");
            schema.newElement("getDepositsResponse").newComplexType().newSequence().newElement("number", INT);

            schema.newElement("getDepositByIdRequest").newComplexType().newSequence().newElement("summand", INT).setMaxOccurs("unbounded");
            schema.newElement("getDepositByIdResponse").newComplexType().newSequence().newElement("number", INT);

            schema.newElement("getDepositByNameRequest").newComplexType().newSequence().newElement("summand", INT).setMaxOccurs("unbounded");
            schema.newElement("getDepositByNameResponse").newComplexType().newSequence().newElement("number", INT);

            schema.newElement("addDepositRequest").newComplexType().newSequence().newElement("summand", INT).setMaxOccurs("unbounded");
            schema.newElement("addDepositResponse").newComplexType().newSequence().newElement("number", INT);

            schema.newElement("updateDepositRequest").newComplexType().newSequence().newElement("summand", INT).setMaxOccurs("unbounded");
            schema.newElement("updateDepositResponse").newComplexType().newSequence().newElement("number", INT);

            schema.newElement("deleteDepositRequest").newComplexType().newSequence().newElement("summand", INT).setMaxOccurs("unbounded");
            schema.newElement("deleteDepositResponse").newComplexType().newSequence().newElement("number", INT);

        Definitions wsdl = new Definitions("http://client.bank.brest.com/Soap/", "SoapService");
        //<definitions targetNamespace='http://thomas-bayer.com/blz/' xmlns:wsdl='http://schemas.xmlsoap.org/wsdl/' xmlns='http://schemas.xmlsoap.org/wsdl/' xmlns:wsaw='http://www.w3.org/2006/05/addressing/wsdl' xmlns:tns='http://thomas-bayer.com/blz/' xmlns:http='http://schemas.xmlsoap.org/wsdl/http/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:mime='http://schemas.xmlsoap.org/wsdl/mime/' xmlns:soap='http://schemas.xmlsoap.org/wsdl/soap/' xmlns:soap12='http://schemas.xmlsoap.org/wsdl/soap12/'>

        wsdl.addSchema(schema);

        Element el1 = new Element();
        el1.setName("getDepositsRequest");

        Element el2 = new Element();
        el2.setName("getDepositsResponse");

        Message msgGetAllIn  = wsdl.newMessage("getDepositsRequestMessage");
        msgGetAllIn.newPart("part", schema.getElement("getDepositsRequest"));

        Message msgGetAllOut = wsdl.newMessage("getDepositsResponseMessage");
        msgGetAllOut.newPart("part",schema.getElement("getDepositsResponse"));

        Message msgGetByIdIn  = wsdl.newMessage("getDepositByIdRequestMessage");
        msgGetByIdIn.newPart("part", schema.getElement("getDepositByIdRequest"));

        Message msgGetByIdOut = wsdl.newMessage("getDepositByIdResponseMessage");
        msgGetByIdOut.newPart("part",schema.getElement("getDepositByIdResponse"));

        Message msgGetByNameIn  = wsdl.newMessage("getDepositByNameRequestMessage");
        msgGetByNameIn.newPart("part", schema.getElement("getDepositByNameRequest"));

        Message msgGetByNameOut = wsdl.newMessage("getDepositByNameResponseMessage");
        msgGetByNameOut.newPart("part",schema.getElement("getDepositByNameResponse"));

        Message msgAddIn  = wsdl.newMessage("addDepositRequestMessage");
        msgAddIn.newPart("part", schema.getElement("addDepositRequest"));

        Message msgAddOut = wsdl.newMessage("addDepositResponseMessage");
        msgAddOut.newPart("part",schema.getElement("addDepositResponse"));

        Message msgUpdateIn  = wsdl.newMessage("updateDepositRequestMessage");
        msgUpdateIn.newPart("part", schema.getElement("updateDepositRequest"));

        Message msgUpdateOut = wsdl.newMessage("updateDepositResponseMessage");
        msgUpdateOut.newPart("part",schema.getElement("updateDepositResponse"));

        Message msgDeleteIn  = wsdl.newMessage("deleteDepositRequestMessage");
        msgDeleteIn.newPart("part", schema.getElement("deleteDepositRequest"));

        Message msgDeleteOut = wsdl.newMessage("deleteDepositResponseMessage");
        msgDeleteOut.newPart("part",schema.getElement("deleteDepositResponse"));

        PortType pt = wsdl.newPortType("DepositServicePortType");

        Operation opGetAll = pt.newOperation("getDeposits");
        opGetAll.newInput("getDepositsRequestMessage").setMessage(msgGetAllIn);
        opGetAll.newOutput("getDepositsResponseMessage").setMessage(msgGetAllOut);

        Operation opGetById = pt.newOperation("getDepositById");
        opGetById.newInput("getDepositByIdRequestMessage").setMessage(msgGetAllIn);
        opGetById.newOutput("getDepositByIdResponseMessage").setMessage(msgGetByIdOut);

        Operation opGetByName = pt.newOperation("getDepositByName");
        opGetByName.newInput("getDepositByNameRequestMessage").setMessage(msgGetByNameIn);
        opGetByName.newOutput("getDepositByNameResponseMessage").setMessage(msgGetByNameOut);

        Operation opAdd = pt.newOperation("addDeposit");
        opAdd.newInput("addDepositRequestMessage").setMessage(msgAddIn);
        opAdd.newOutput("addDepositResponseMessage").setMessage(msgAddOut);

        Operation opUpd = pt.newOperation("updateDeposit");
        opUpd.newInput("updateDepositRequestMessage").setMessage(msgUpdateIn);
        opUpd.newOutput("updateDepositResponseMessage").setMessage(msgUpdateOut);

        Operation opDel = pt.newOperation("deleteDeposit");
        opDel.newInput("deleteDepositRequestMessage").setMessage(msgDeleteIn);
        opDel.newOutput("deleteDepositResponseMessage").setMessage(msgDeleteOut);


        //<wsdl:binding name="DepositServiceBinding" type="tns:DepositServicePortType">
        Binding bd = wsdl.newBinding("DepositServiceBinding");
        bd.setType(pt);

        //<soap:binding transport="http://schemas.xmlsoap.org/soap/http" style="document"/>
        //SOAPBinding soapBinding =
        //bd.newSOAP11Binding("http://schemas.xmlsoap.org/soap/http","document");

        //soapBinding.setTransport("http://schemas.xmlsoap.org/soap/http");

        BindingOperation binOp = bd.newBindingOperation("getDeposits");

        SOAPOperation soapOp = binOp.newSOAP11Operation();
        soapOp.setSoapAction("getDeposits");

        BindingInput in = binOp.newInput();
        SOAPBody soapBodyIn = in.newSOAP12Body();
        soapBodyIn.setName("soap");
        Part p = new Part();
        p.setElement(el1);
        List<Part> list = new ArrayList<Part>();
        list.add(p);
        p.setElement(el2);
        list.add(p);
        soapBodyIn.setParts(list);

        BindingOutput out = binOp.newOutput();
        SOAPBody soapBodyOut = out.newSOAP12Body();

        return wsdl;
    }

    public static void dumpWSDL(Definitions wsdl) {
        StringWriter writer = new StringWriter();
        WSDLCreator creator = new WSDLCreator();
        creator.setBuilder(new MarkupBuilder(writer));
        wsdl.create(creator, new WSDLCreatorContext());
        System.out.println(writer);
        //return writer;
    }
}

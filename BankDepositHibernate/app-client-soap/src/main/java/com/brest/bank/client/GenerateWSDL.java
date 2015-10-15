package com.brest.bank.client;

/**
 * Created by alexander on 11.8.15.
 */
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.brest.bank.domain.BankDeposit;
import com.predic8.wsdl.soap12.SOAPOperation;
import com.predic8.wsdl.soap12.SOAPBody;
import groovy.xml.MarkupBuilder;

import com.predic8.wsdl.*;
import com.predic8.wsdl.creator.*;
import com.predic8.schema.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        Definitions wsdl = new Definitions("http://client.bank.brest.com/Soap/Services", "SoapService");

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

        Binding bd = wsdl.newBinding("DepositServiceBinding");
        bd.setType(pt);

        BindingOperation binOpGetAll = bd.newBindingOperation("getDeposits");

        SOAPOperation soapOpGetAll = binOpGetAll.newSOAP12Operation();
        soapOpGetAll.setSoapAction("http://localhost:8080/BankDeposit/soap/client/"+"getDeposits");
        soapOpGetAll.setStyle("rpc");

        SOAPBody inputOpBodyGetAll = binOpGetAll.newInput().newSOAP12Body();
        inputOpBodyGetAll.setUse("encoded");
        inputOpBodyGetAll.setNamespace(wsdl.getTargetNamespace());
        inputOpBodyGetAll.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

        SOAPBody outputOpBodyGetAll = binOpGetAll.newOutput().newSOAP12Body();
        outputOpBodyGetAll.setUse("encoded");
        outputOpBodyGetAll.setNamespace(wsdl.getTargetNamespace());
        outputOpBodyGetAll.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

        BindingOperation binOpGetById = bd.newBindingOperation("getDepositById");

        SOAPOperation soapOpGetById = binOpGetById.newSOAP12Operation();
        soapOpGetById.setSoapAction("http://localhost:8080/BankDeposit/soap/client/"+"getBankDepositById");
        soapOpGetById.setStyle("rpc");

        SOAPBody inputOpBodyGetById = binOpGetById.newInput().newSOAP12Body();
        inputOpBodyGetById.setUse("encoded");
        inputOpBodyGetById.setNamespace(wsdl.getTargetNamespace());
        inputOpBodyGetById.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

        SOAPBody outputOpBodyGetById = binOpGetById.newOutput().newSOAP12Body();
        outputOpBodyGetById.setUse("encoded");
        outputOpBodyGetById.setNamespace(wsdl.getTargetNamespace());
        outputOpBodyGetById.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

        BindingOperation binOpGetByName = bd.newBindingOperation("getDepositByName");

        SOAPOperation soapOpGetByName = binOpGetByName.newSOAP12Operation();
        soapOpGetByName.setSoapAction("http://localhost:8080/BankDeposit/soap/client/"+"getBankDepositByName");
        soapOpGetByName.setStyle("rpc");

        SOAPBody inputOpBodyGetByName = binOpGetByName.newInput().newSOAP12Body();
        inputOpBodyGetByName.setUse("encoded");
        inputOpBodyGetByName.setNamespace(wsdl.getTargetNamespace());
        inputOpBodyGetByName.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

        SOAPBody outputOpBodyGetByName = binOpGetByName.newOutput().newSOAP12Body();
        outputOpBodyGetByName.setUse("encoded");
        outputOpBodyGetByName.setNamespace(wsdl.getTargetNamespace());
        outputOpBodyGetByName.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

        BindingOperation binOpAdd = bd.newBindingOperation("addDeposit");

        SOAPOperation soapOpAdd = binOpAdd.newSOAP12Operation();
        soapOpAdd.setSoapAction("http://localhost:8080/BankDeposit/soap/client/"+"addBankDeposit");
        soapOpAdd.setStyle("rpc");

        SOAPBody inputOpBodyAdd = binOpAdd.newInput().newSOAP12Body();
        inputOpBodyAdd.setUse("encoded");
        inputOpBodyAdd.setNamespace(wsdl.getTargetNamespace());
        inputOpBodyAdd.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

        SOAPBody outputOpBodyAdd = binOpAdd.newOutput().newSOAP12Body();
        outputOpBodyAdd.setUse("encoded");
        outputOpBodyAdd.setNamespace(wsdl.getTargetNamespace());
        outputOpBodyAdd.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

        BindingOperation binOpUpdate = bd.newBindingOperation("updateDeposit");

        SOAPOperation soapOpUpdate = binOpUpdate.newSOAP12Operation();
        soapOpUpdate.setSoapAction("http://localhost:8080/BankDeposit/soap/client/"+"updateBankDeposit");
        soapOpUpdate.setStyle("rpc");

        SOAPBody inputOpBodyUpdate = binOpUpdate.newInput().newSOAP12Body();
        inputOpBodyUpdate.setUse("encoded");
        inputOpBodyUpdate.setNamespace(wsdl.getTargetNamespace());
        inputOpBodyUpdate.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

        SOAPBody outputOpBodyUpdate = binOpUpdate.newOutput().newSOAP12Body();
        outputOpBodyUpdate.setUse("encoded");
        outputOpBodyUpdate.setNamespace(wsdl.getTargetNamespace());
        outputOpBodyUpdate.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

        BindingOperation binOpDelete = bd.newBindingOperation("deleteDeposit");

        SOAPOperation soapOpDelete = binOpDelete.newSOAP12Operation();
        soapOpDelete.setSoapAction("http://localhost:8080/BankDeposit/soap/client/"+"deleteBankDeposit");
        soapOpDelete.setStyle("rpc");

        SOAPBody inputOpBodyDelete = binOpDelete.newInput().newSOAP12Body();
        inputOpBodyDelete.setUse("encoded");
        inputOpBodyDelete.setNamespace(wsdl.getTargetNamespace());
        inputOpBodyDelete.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

        SOAPBody outputOpBodyDelete = binOpDelete.newOutput().newSOAP12Body();
        outputOpBodyDelete.setUse("encoded");
        outputOpBodyDelete.setNamespace(wsdl.getTargetNamespace());
        outputOpBodyDelete.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

        Service service = wsdl.newService("DepositService");

        Port port = service.newPort("DepositServicePort");
        port.setBinding(bd);
        port.newSOAP12Address("http://localhost:8080/BankDeposit/soap/client");

        return wsdl;
    }

    public static StringWriter dumpWSDL(Definitions wsdl) {
        StringWriter writer = new StringWriter();
        WSDLCreator creator = new WSDLCreator();
        creator.setBuilder(new MarkupBuilder(writer));
        wsdl.create(creator, new WSDLCreatorContext());
        System.out.println(writer);
        return writer;
    }
}

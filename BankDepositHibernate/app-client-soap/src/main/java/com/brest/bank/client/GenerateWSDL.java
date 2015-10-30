package com.brest.bank.client;

/**
 * Created by alexander on 11.8.15.
 */
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.brest.bank.domain.BankDeposit;

import com.predic8.schema.Import;
import com.predic8.schema.restriction.Restriction;
import com.predic8.schema.restriction.facet.*;
import com.predic8.wsdl.soap12.SOAPBinding;
import com.predic8.wsdl.soap12.SOAPOperation;
import com.predic8.wsdl.soap12.SOAPBody;
import com.predic8.wsdl.*;
import com.predic8.wsdl.creator.*;
import com.predic8.schema.*;
import static com.predic8.schema.Schema.*;

import com.sun.org.apache.xml.internal.utils.NameSpace;
import groovy.xml.MarkupBuilder;

import groovy.xml.QName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GenerateWSDL {

    private static final Logger LOGGER = LogManager.getLogger();

    private static Restriction depositRestriction;
    private static List<Facet> facets;
    private static MaxInclusiveFacet maxDMT, maxDIR;
    private static MinInclusiveFacet minDMT, minDMA, minDIR;
    private static PatternFacet pattern;

    public static void main(String[] args) {
        dumpWSDL(createWSDL());
    }

    public static Definitions createWSDL() {
        Schema schema = new Schema("http://client.bank.brest.com/soap/1/");

            ComplexType bankDepositType = schema.newComplexType("bankDeposit_type");
                Sequence sequenceBDT = bankDepositType.newSequence();

                sequenceBDT.newElement("depositId", new QName("xsd:long")).setMaxOccurs("unbounded");

                Element depositName = sequenceBDT.newElement("depositName");
                    SimpleType simpleTypeDN = new SimpleType();
                    pattern = new PatternFacet();
                    pattern.setValue("[A-Z][a-z]+[0-9]*");
                    facets = new ArrayList<>();
                    facets.add(pattern);
                    depositRestriction = new Restriction();
                    depositRestriction.setBase(STRING);
                    depositRestriction.setFacets(facets);
                    simpleTypeDN.setRestriction(depositRestriction);
                    depositName.setEmbeddedType(simpleTypeDN);

                Element depositMinTerm = sequenceBDT.newElement("depositMinTerm");
                    SimpleType simpleTypeDMT = new SimpleType();
                    minDMT = new MinInclusiveFacet();
                    minDMT.setValue("1");
                    maxDMT = new MaxInclusiveFacet();
                    maxDMT.setValue("480");
                    facets = new ArrayList<>();
                    facets.add(minDMT);
                    facets.add(maxDMT);
                    depositRestriction = new Restriction();
                    depositRestriction.setBase(INT);
                    depositRestriction.setFacets(facets);
                    simpleTypeDMT.setRestriction(depositRestriction);
                    depositMinTerm.setEmbeddedType(simpleTypeDMT);

                Element depositMinAmount = sequenceBDT.newElement("depositMinAmount");
                    SimpleType simpleTypeDMA = new SimpleType();
                    minDMA = new MinInclusiveFacet();
                    minDMA.setValue("100");
                    facets = new ArrayList<>();
                    facets.add(minDMA);
                    depositRestriction = new Restriction();
                    depositRestriction.setBase(INT);
                    depositRestriction.setFacets(facets);
                    simpleTypeDMA.setRestriction(depositRestriction);
                    depositMinAmount.setEmbeddedType(simpleTypeDMA);

                Element depositCurrency = sequenceBDT.newElement("depositCurrency");
                    SimpleType simpleTypeDC = new SimpleType();
                    pattern = new PatternFacet();
                    pattern.setValue("[A-Z]{3}");
                    facets = new ArrayList<>();
                    facets.add(pattern);
                    depositRestriction = new Restriction();
                    depositRestriction.setBase(STRING);
                    depositRestriction.setFacets(facets);
                    simpleTypeDC.setRestriction(depositRestriction);
                    depositCurrency.setEmbeddedType(simpleTypeDC);

                Element depositInterestRate = sequenceBDT.newElement("depositInterestRate");
                    SimpleType simpleTypeDIR = new SimpleType();
                    minDIR = new MinInclusiveFacet();
                    minDIR.setValue("1");
                    maxDIR = new MaxInclusiveFacet();
                    maxDIR.setValue("100");
                    facets = new ArrayList<>();
                    facets.add(minDIR);
                    facets.add(maxDIR);
                    depositRestriction = new Restriction();
                    depositRestriction.setBase(INT);
                    depositRestriction.setFacets(facets);
                    simpleTypeDIR.setRestriction(depositRestriction);
                    depositInterestRate.setEmbeddedType(simpleTypeDIR);

                Element depositAddConditions = sequenceBDT.newElement("depositAddConditions");
                    SimpleType simpleTypeDAC = new SimpleType();
                    pattern = new PatternFacet();
                    pattern.setValue("[A-Z][a-z]+[0-9]*");
                    facets = new ArrayList<>();
                    facets.add(pattern);
                    depositRestriction = new Restriction();
                    depositRestriction.setBase(STRING);
                    depositRestriction.setFacets(facets);
                    simpleTypeDAC.setRestriction(depositRestriction);
                    depositAddConditions.setEmbeddedType(simpleTypeDAC);

            ComplexType bankDepositsType = schema.newComplexType("bankDeposits_type");
                Sequence sequenceBDsT = bankDepositsType.newSequence();
                Element bankDeposit = sequenceBDsT.newElement("bankDeposit", "bankDeposit_type");
                bankDeposit.setMinOccurs("0");
                bankDeposit.setMaxOccurs("unbounded");

            schema.newElement("getDepositsRequest").setMaxOccurs("unbounded");

            schema.newElement("getDepositsResponse").newComplexType().newSequence().newElement("bankDeposits", "bankDeposits_type");

            schema.newElement("getDepositByIdRequest").newComplexType().newSequence().newElement("bankDepositId", new QName("xsd:long")).setMaxOccurs("unbounded");

            schema.newElement("getDepositByIdResponse").newComplexType().newSequence().newElement("bankDeposit", "bankDeposit_type");

            Element getDepositByNameRequest = schema.newElement("getDepositByNameRequest").newComplexType().newSequence().newElement("bankDepositName");//, STRING).setMaxOccurs("unbounded");
                SimpleType simpleTypeGDBNR = new SimpleType();
                pattern = new PatternFacet();
                pattern.setValue("[A-Z][a-z]+[0-9]*");
                facets = new ArrayList<>();
                facets.add(pattern);
                depositRestriction = new Restriction();
                depositRestriction.setBase(STRING);
                depositRestriction.setFacets(facets);
                simpleTypeGDBNR.setRestriction(depositRestriction);
                getDepositByNameRequest.setEmbeddedType(simpleTypeGDBNR);


            schema.newElement("getDepositByNameResponse").newComplexType().newSequence().newElement("bankDeposit","bankDeposit_type");

            schema.newElement("addDepositRequest").newComplexType().newSequence().newElement("bankDeposit","bankDeposit_type");

            schema.newElement("addDepositResponse").newComplexType().newSequence().newElement("bankDeposit","bankDeposit_type");

            schema.newElement("updateDepositRequest").newComplexType().newSequence().newElement("bankDeposit","bankDeposit_type");

            schema.newElement("updateDepositResponse").newComplexType().newSequence().newElement("bankDeposit","bankDeposit_type");

            schema.newElement("deleteDepositRequest").newComplexType().newSequence().newElement("bankDepositId", new QName("xsd:long")).setMaxOccurs("unbounded");

            schema.newElement("deleteDepositResponse").newComplexType().newSequence().newElement("result", STRING);

        Definitions wsdl = new Definitions("http://client.bank.brest.com/soap/service/1/","service");

        wsdl.addSchema(schema);


        wsdl.newMessage("getDepositsRequestMessage")
                .newPart("getDepositsRequest", schema.getElement("getDepositsRequest"));

        wsdl.newMessage("getDepositsResponseMessage")
                .newPart("getDepositsResponse", schema.getElement("getDepositsResponse"));

        wsdl.newMessage("getDepositByIdRequestMessage")
                .newPart("getDepositByIdRequest", schema.getElement("getDepositByIdRequest"));

        Message msgGetByIdOut = wsdl.newMessage("getDepositByIdResponseMessage");
        msgGetByIdOut.newPart("getDepositByIdResponse",schema.getElement("getDepositByIdResponse"));

        Message msgGetByNameIn  = wsdl.newMessage("getDepositByNameRequestMessage");
        msgGetByNameIn.newPart("getDepositByNameRequest", schema.getElement("getDepositByNameRequest"));

        Message msgGetByNameOut = wsdl.newMessage("getDepositByNameResponseMessage");
        msgGetByNameOut.newPart("getDepositByNameResponse",schema.getElement("getDepositByNameResponse"));

        Message msgAddIn  = wsdl.newMessage("addDepositRequestMessage");
        msgAddIn.newPart("addDepositRequest", schema.getElement("addDepositRequest"));

        Message msgAddOut = wsdl.newMessage("addDepositResponseMessage");
        msgAddOut.newPart("addDepositResponse",schema.getElement("addDepositResponse"));

        Message msgUpdateIn  = wsdl.newMessage("updateDepositRequestMessage");
        msgUpdateIn.newPart("updateDepositRequest", schema.getElement("updateDepositRequest"));

        Message msgUpdateOut = wsdl.newMessage("updateDepositResponseMessage");
        msgUpdateOut.newPart("updateDepositResponse",schema.getElement("updateDepositResponse"));

        Message msgDeleteIn  = wsdl.newMessage("deleteDepositRequestMessage");
        msgDeleteIn.newPart("deleteDepositRequest", schema.getElement("deleteDepositRequest"));

        Message msgDeleteOut = wsdl.newMessage("deleteDepositResponseMessage");
        msgDeleteOut.newPart("deleteDepositResponse",schema.getElement("deleteDepositResponse"));

        PortType pt = wsdl.newPortType("DepositServicePortType");

        Operation opGetAll = pt.newOperation("getBankDeposits");
        opGetAll.newInput("getDepositsRequestMessage").setMessage(wsdl.getMessage("getDepositsRequestMessage"));
        opGetAll.newOutput("getDepositsResponseMessage").setMessage(wsdl.getMessage("getDepositsResponseMessage"));

        Operation opGetById = pt.newOperation("getBankDepositById");
        opGetById.newInput("getDepositByIdRequestMessage").setMessage(wsdl.getMessage("getDepositByIdRequestMessage"));
        opGetById.newOutput("getDepositByIdResponseMessage").setMessage(msgGetByIdOut);

        Operation opGetByName = pt.newOperation("getBankDepositByName");
        opGetByName.newInput("getDepositByNameRequestMessage").setMessage(msgGetByNameIn);
        opGetByName.newOutput("getDepositByNameResponseMessage").setMessage(msgGetByNameOut);

        Operation opAdd = pt.newOperation("addBankDeposit");
        opAdd.newInput("addDepositRequestMessage").setMessage(msgAddIn);
        opAdd.newOutput("addDepositResponseMessage").setMessage(msgAddOut);

        Operation opUpd = pt.newOperation("updateBankDeposit");
        opUpd.newInput("updateDepositRequestMessage").setMessage(msgUpdateIn);
        opUpd.newOutput("updateDepositResponseMessage").setMessage(msgUpdateOut);

        Operation opDel = pt.newOperation("deleteBankDeposit");
        opDel.newInput("deleteDepositRequestMessage").setMessage(msgDeleteIn);
        opDel.newOutput("deleteDepositResponseMessage").setMessage(msgDeleteOut);

        Binding bd = wsdl.newBinding("DepositServiceBinding");
        bd.setType(pt);

        BindingOperation binOpGetAll = bd.newBindingOperation("getBankDeposits");

        SOAPOperation soapOpGetAll = binOpGetAll.newSOAP12Operation();
        soapOpGetAll.setSoapAction("http://localhost:8080/BankDeposit/soap/client/" + "getBankDeposits");
        soapOpGetAll.setStyle("rpc");

        SOAPBody inputOpBodyGetAll = binOpGetAll.newInput().newSOAP12Body();
        inputOpBodyGetAll.setUse("encoded");
        inputOpBodyGetAll.setNamespace(binOpGetAll.getNamespaceUri());//wsdl.getTargetNamespace());
        inputOpBodyGetAll.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

        SOAPBody outputOpBodyGetAll = binOpGetAll.newOutput().newSOAP12Body();
        outputOpBodyGetAll.setUse("encoded");
        outputOpBodyGetAll.setNamespace(wsdl.getTargetNamespace());
        outputOpBodyGetAll.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

        BindingOperation binOpGetById = bd.newBindingOperation("getBankDepositById");

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

        BindingOperation binOpGetByName = bd.newBindingOperation("getBankDepositByName");

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

        BindingOperation binOpAdd = bd.newBindingOperation("addBankDeposit");

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

        BindingOperation binOpUpdate = bd.newBindingOperation("updateBankDeposit");

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

        BindingOperation binOpDelete = bd.newBindingOperation("deleteBankDeposit");

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

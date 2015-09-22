package com.brest.bank.client;

/**
 * Created by alexander on 11.8.15.
 */
import java.io.StringWriter;
import java.io.Writer;

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
        Schema schema = new Schema("http://predic8.com/add/1/");

        schema.newElement("add").newComplexType().newSequence().newElement("summand", INT).setMaxOccurs("unbounded");
        schema.newElement("addResponse").newComplexType().newSequence().newElement("number", INT);

        Definitions wsdl = new Definitions("http://predic8.com/wsdl/AddService/1/", "AddService");
        wsdl.addSchema(schema);

        Element el1 = new Element();
        el1.setName("tns:add");

        Element el2 = new Element();
        el2.setName("tns:addResponse");

        Message msgIn  = wsdl.newMessage("add");
        msgIn.newPart("parameters", schema.getElement("add"));

        Message msgOut = wsdl.newMessage("addResponse");
        msgOut.newPart("parameters",schema.getElement("addResponse"));

        PortType pt = wsdl.newPortType("AddPortType");
        Operation op = pt.newOperation("add");

        op.newInput("add").setMessage(msgIn);
        op.newOutput("addResponse").setMessage(msgOut);
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

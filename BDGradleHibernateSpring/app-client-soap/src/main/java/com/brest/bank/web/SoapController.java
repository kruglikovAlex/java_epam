package com.brest.bank.web;

import com.brest.bank.client.SoapClient;
import com.brest.bank.domain.BankDeposit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONObject;

import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.wsdl.Definition;
import javax.xml.bind.*;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/client")
public class SoapController {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    private static final Logger LOGGER = LogManager.getLogger();
    private static String serviceAddress;
    @Autowired
    SoapClient soapClient;
    private Map wsdlServices = new HashMap();
    private String[] soapResponse = {"",""};
    private String soapRequest = "";
    private String wsdlLocation = "http://localhost:8080/SpringHibernateBDeposit-1.0/soap/soapService.wsdl";

    /**
     * Get a list of SOAP client methods
     *
     * @param client - object of SoapClient.class
     * @return an array of Method.class elements
     */
    private static Method[] getServiceMethods(SoapClient client){
        LOGGER.debug("getServiceMethods({})",client);

        Method[] methods = client.getClass().getMethods();
        for (Method m:methods
             ) {
            LOGGER.debug("method name: {}",m.getName());
        }
        return methods;
    }

    /**
     * Get a list of services from a WSDL document
     *
     * @param wsdl - a definition WSDL scheme
     * @param methods - a list of SOAP client methods
     * @return a HashMap of services
     */
    private static Map getServices(Definition wsdl, Method[] methods) throws ClassNotFoundException, IllegalAccessException, InstantiationException{
        LOGGER.debug("getServices({},{})",wsdl,methods);
        Map services = new HashMap();

        for (Method method:methods
             ) {

            Iterator itr = wsdl.getMessages().values().iterator();

            while(itr.hasNext()) {
                String element = itr.next().toString();
                if(element.contains(method.getName())){
                    String nameResponse = method.getReturnType().getName();
                    String[] nameParam = null;
                    int i = 0;
                    try{
                        for (Field f:Class.forName(nameResponse.replace("Response","Request")).getDeclaredFields()
                                ) {
                            if(f.getName().equals("bankDeposit")){
                                nameParam = new String[Class.forName(nameResponse.replace("Response","Request")).getDeclaredFields().length+6];
                            }else if (f.getName().equals("bankDepositor")){
                                nameParam = new String[Class.forName(nameResponse.replace("Response","Request")).getDeclaredFields().length+7];
                            }else{
                                nameParam = new String[Class.forName(nameResponse.replace("Response","Request")).getDeclaredFields().length];
                            }
                        }
                        for (Field f:Class.forName(nameResponse.replace("Response","Request")).getDeclaredFields()
                                ) {
                            if(f.getName().equals("bankDeposit")){
                                nameParam[i] = "depositId";
                                nameParam[i+1] = "depositName";
                                nameParam[i+2] = "depositMinTerm";
                                nameParam[i+3] = "depositMinAmount";
                                nameParam[i+4] = "depositCurrency";
                                nameParam[i+5] = "depositInterestRate";
                                nameParam[i+6] = "depositAddConditions";
                                i = i+7;
                            } else if(f.getName().equals("bankDepositor")){
                                nameParam[i] = "depositorId";
                                nameParam[i+1] = "depositorName";
                                nameParam[i+2] = "depositorDateDeposit";
                                nameParam[i+3] = "depositorAmountDeposit";
                                nameParam[i+4] = "depositorAmountPlusDeposit";
                                nameParam[i+5] = "depositorAmountMinusDeposit";
                                nameParam[i+6] = "depositorDateReturnDeposit";
                                nameParam[i+7] = "depositorMarkReturnDeposit";
                                i = i+8;
                            } else{
                                nameParam[i] = f.getName();
                                i++;
                            }
                        }
                    }catch (Exception e){
                        System.out.println("A query is used without parameters");
                    }
                    services.put(method.getName(),nameParam);
                }
            }
        }

        Iterator itr = wsdl.getServices().values().iterator();

        while(itr.hasNext()) {
            String element = itr.next().toString();
            serviceAddress = element.substring(element.lastIndexOf("locationURI")+12);
        }

        return services;
    }

    /**
     * Sending a POST request
     *
     * @param redirectAttributes RedirectAttributes - to transfer the so-called flash-attributes,
     *                           that is, values that will be available only to the following request, e.t. errors
     * @param model ModelMap - This facility is intended to convey information to the JSP page.
     * Variables "services","requests" and "responses" will be passed on mainFrame.jsp page.
     * @param status SessionStatus - status code of the query
     * @param queryMap LinkedHashMap<String, String> - parameters of a POST request
     * @return ModelAndView - "mainFrame" with SOAP services, xml request, xml and json response
     */
    @RequestMapping(value={"/submitSoapQuery"}, method = RequestMethod.POST)
    public ModelAndView postSoapQuery( RedirectAttributes redirectAttributes,
                                       ModelMap model,
                                       SessionStatus status,
                                       @RequestParam LinkedHashMap<String, String> queryMap
                                       )
    {
        LOGGER.debug("postSoapQuery(queryMap - {})", queryMap.toString());

        status.setComplete();

        Object[] param = new Object[queryMap.size()-2];
        int i=0;
        for (Object p:queryMap.values()
             ) {
            if(!p.toString().equals(queryMap.get("soapQuery"))&
                    !p.toString().equals(queryMap.get("submit"))){
                param[i] = p;
                i++;
            }
        }

        soapRequest = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://bank.brest.com/soap\">\n" +
                "\t<x:Header/>\n" +
                "\t<x:Body>\n"+
                "\t\t<soa:"+queryMap.get("soapQuery")+"Request>\n";

        try{
            Class clientClass = soapClient.getClass();
            Method[] clientClassMethods = clientClass.getMethods();
            Method clientClassMethod = null;
            for(int j=0; j<clientClassMethods.length;j++){
                if(clientClassMethods[j].getName().equals(queryMap.get("soapQuery"))){
                    clientClassMethod = clientClassMethods[j];
                }
            }

            Object[] sortParam = new Object[clientClassMethod.getParameterTypes().length];

            if(param.length>0){
                Class clientRequestClass =
                        Class.forName(clientClassMethod.getReturnType().getName().replace("Response","Request"));

                for(int j=0; j<clientRequestClass.getDeclaredFields().length; j++){
                    switch (clientRequestClass.getDeclaredFields()[j].getType().getName()){
                        case "java.lang.String":{
                            sortParam[j] = queryMap.get(clientRequestClass.getDeclaredFields()[j].getName()).toString();
                            soapRequest = soapRequest
                                    + "\t\t\t<soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">"
                                    + sortParam[j]
                                    + "</soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">\n";
                            break;
                        }
                        case "java.lang.Integer":{
                            sortParam[j] = Integer.parseInt(queryMap.get(clientRequestClass.getDeclaredFields()[j].getName()));
                            soapRequest = soapRequest
                                    + "\t\t\t<soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">"
                                    + sortParam[j]
                                    + "</soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">\n";
                            break;
                        }
                        case "int":{
                            sortParam[j] = Integer.parseInt(queryMap.get(clientRequestClass.getDeclaredFields()[j].getName()));
                            soapRequest = soapRequest
                                    + "\t\t\t<soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">"
                                    + sortParam[j]
                                    + "</soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">\n";
                            break;
                        }
                        case "java.lang.Long":{
                            sortParam[j] = Long.parseLong(queryMap.get(clientRequestClass.getDeclaredFields()[j].getName()));
                            soapRequest = soapRequest
                                    + "\t\t\t<soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">"
                                    + sortParam[j]
                                    + "</soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">\n";
                            break;
                        }
                        case "long":{
                            sortParam[j] = Long.parseLong(queryMap.get(clientRequestClass.getDeclaredFields()[j].getName()));
                            soapRequest = soapRequest
                                    + "\t\t\t<soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">"
                                    + sortParam[j]
                                    + "</soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">\n";
                            break;
                        }
                        case "javax.xml.datatype.XMLGregorianCalendar":{
                            sortParam[j] = dateFormat.parse(queryMap.get(clientRequestClass.getDeclaredFields()[j].getName()));
                            soapRequest = soapRequest
                                    + "\t\t\t<soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">"
                                    + sortParam[j]
                                    + "</soa:" + clientRequestClass.getDeclaredFields()[j].getName() + ">\n";
                            break;
                        }
                        case "com.brest.bank.wsdl.BankDepositor":{
                            com.brest.bank.domain.BankDepositor bankDepositor = new com.brest.bank.domain.BankDepositor();
                            if (queryMap.get("depositorId").isEmpty()){
                                bankDepositor.setDepositorId(null);
                            } else {
                                bankDepositor.setDepositorId(Long.parseLong(queryMap.get("depositorId")));
                            }
                            bankDepositor.setDepositorName(queryMap.get("depositorName"));

                            Date xmlStartDate,xmlEndDate;
                            try {
                                xmlStartDate = dateFormat.parse(queryMap.get("depositorDateDeposit"));
                                xmlEndDate = dateFormat.parse(queryMap.get("depositorDateReturnDeposit"));
                                LOGGER.debug("xmlStartDate-{}, xmlEndDate-{}",xmlStartDate,xmlEndDate);
                            }
                            catch (  ParseException e) {
                                throw new RuntimeException(e);
                            }

                            bankDepositor.setDepositorDateDeposit(xmlStartDate);
                            bankDepositor.setDepositorDateReturnDeposit(xmlEndDate);
                            bankDepositor.setDepositorAmountDeposit(Integer.parseInt(queryMap.get("depositorAmountDeposit")));
                            bankDepositor.setDepositorAmountPlusDeposit(Integer.parseInt(queryMap.get("depositorAmountPlusDeposit")));
                            bankDepositor.setDepositorAmountMinusDeposit(Integer.parseInt(queryMap.get("depositorAmountMinusDeposit")));
                            bankDepositor.setDepositorMarkReturnDeposit(Integer.parseInt(queryMap.get("depositorMarkReturnDeposit")));

                            soapRequest = soapRequest
                                    + "\t\t\t<soa:bankDepositor>\n"
                                    + "\t\t\t\t<soa:bankDepositorId>" +bankDepositor.getDepositorId()+"</soa:bankDepositorId>\n"
                                    + "\t\t\t\t<soa:bankDepositorName>" +bankDepositor.getDepositorName()+"</soa:bankDepositorName>\n"
                                    + "\t\t\t\t<soa:bankDepositorDateDeposit>" +dateFormat.format(bankDepositor.getDepositorDateDeposit())+"</soa:bankDepositorDateDeposit>\n"
                                    + "\t\t\t\t<soa:bankDepositorAmountDeposit>" +bankDepositor.getDepositorAmountDeposit()+"</soa:bankDepositorAmountDeposit>\n"
                                    + "\t\t\t\t<soa:bankDepositorAmountPlusDeposit>" +bankDepositor.getDepositorAmountPlusDeposit()+"</soa:bankDepositorAmountPlusDeposit>\n"
                                    + "\t\t\t\t<soa:bankDepositorAmountMinusDeposit>" +bankDepositor.getDepositorAmountMinusDeposit()+"</soa:bankDepositorAmountMinusDeposit>\n"
                                    + "\t\t\t\t<soa:bankDepositorDateReturnDeposit>" +dateFormat.format(bankDepositor.getDepositorDateReturnDeposit())+"</soa:bankDepositorDateReturnDeposit>\n"
                                    + "\t\t\t\t<soa:bankDepositorMarkReturnDeposit>" +bankDepositor.getDepositorMarkReturnDeposit()+"</soa:bankDepositorMarkReturnDeposit>\n"
                                    + "\t\t\t</soa:bankDepositor>\n";

                            sortParam[j] = bankDepositor;
                            break;
                        }
                        case "com.brest.bank.wsdl.BankDeposit":{
                            BankDeposit bankDeposit = new BankDeposit();
                            if (queryMap.get("depositId").isEmpty()){
                                bankDeposit.setDepositId(null);
                            } else {
                                bankDeposit.setDepositId(Long.parseLong(queryMap.get("depositId")));
                            }
                            bankDeposit.setDepositName(queryMap.get("depositName"));
                            bankDeposit.setDepositMinTerm(Integer.parseInt(queryMap.get("depositMinTerm")));
                            bankDeposit.setDepositMinAmount(Integer.parseInt(queryMap.get("depositMinAmount")));
                            bankDeposit.setDepositCurrency(queryMap.get("depositCurrency"));
                            bankDeposit.setDepositInterestRate(Integer.parseInt(queryMap.get("depositInterestRate")));
                            bankDeposit.setDepositAddConditions(queryMap.get("depositAddConditions"));

                            soapRequest = soapRequest
                                    + "\t\t\t<soa:bankDeposit>\n"
                                    + "\t\t\t\t<soa:bankDepositId>" +bankDeposit.getDepositId()+"</soa:bankDepositId>\n"
                                    + "\t\t\t\t<soa:bankDepositName>" +bankDeposit.getDepositName()+"</soa:bankDepositName>\n"
                                    + "\t\t\t\t<soa:bankDepositMinTerm>" +bankDeposit.getDepositMinTerm()+"</soa:bankDepositMinTerm>\n"
                                    + "\t\t\t\t<soa:bankDepositMinAmount>" +bankDeposit.getDepositMinAmount()+"</soa:bankDepositMinAmount>\n"
                                    + "\t\t\t\t<soa:bankDepositCurrency>" +bankDeposit.getDepositCurrency()+"</soa:bankDepositCurrency>\n"
                                    + "\t\t\t\t<soa:bankDepositInterestRate>" +bankDeposit.getDepositInterestRate()+"</soa:bankDepositInterestRate>\n"
                                    + "\t\t\t\t<soa:bankDepositAddConditions>" +bankDeposit.getDepositAddConditions()+"</soa:bankDepositAddConditions>\n"
                                    + "\t\t\t</soa:bankDeposit>\n";

                            sortParam[j] = bankDeposit;
                            break;
                        }
                    }
                }
            }

            soapRequest = soapRequest + "\t\t</soa:" + queryMap.get("soapQuery") + "Request>\n"
                    +"\t</x:Body>\n" +
                    "</x:Envelope>";

            StringWriter sw = new StringWriter();
            sw.write(soapRequest);

            soapRequest = sw.toString();

            JAXBContext contextXML = JAXBContext.newInstance(clientClassMethod.getReturnType());

            sw = new StringWriter();

            Marshaller marshaller = contextXML.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            try{
                marshaller.marshal(clientClassMethod.invoke(soapClient,sortParam), sw);
            }catch (Exception e){
                sw.write("<SOAP-ENV:Fault xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "\t<faultcode>SOAP-ENV:Server</faultcode>" +
                        "\t<faultstring xml:lang=\"en\">" + e.getMessage()+"\n"+ e.toString()+"\n"+ e.getStackTrace() + "</faultstring>" +
                        "</SOAP-ENV:Fault>");
            }

            soapResponse[0] = sw.toString();

            LOGGER.debug("soapResponse[0] : {}", soapResponse[0]);

            JSONObject xmlJSONObj = XML.toJSONObject(sw.toString());
            String jsonString = xmlJSONObj.toString(4);

            soapResponse[1] = jsonString;

        }catch(Exception e) {
            LOGGER.debug("postSoapQuery(), Exception:{}", e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            soapResponse[0] = "postSoapQuery(), Exception:" + e.toString();
        }

        ModelAndView view = new ModelAndView("mainFrame");
        view.addObject("services",wsdlServices);
        view.addObject("requests",soapRequest);
        view.addObject("responses",soapResponse);

        return view;
    }

    /**
     * Sending a GET request
     *
     * @param redirectAttributes RedirectAttributes - to transfer the so-called flash-attributes,
     *                           that is, values that will be available only to the following request, e.t. errors
     * @param wsdlLoc String - http address of the wsdl document
     * @param status SessionStatus - status code of the query
     * @return ModelAndView - "mainFrame" with SOAP services, xml request, xml and json response
     * @throws ParseException
     */
    @RequestMapping(value = "/main")
    public ModelAndView getSoapView(RedirectAttributes redirectAttributes,
                                    @RequestParam(name = "wsdlLoc", required=false, defaultValue = "") String wsdlLoc,
                                    SessionStatus status) throws ParseException
    {
        LOGGER.debug("getSoapView()");

        status.setComplete();
        if (!wsdlLoc.isEmpty()) {
            wsdlLocation = wsdlLoc;
            try {
                LOGGER.debug("getBankDepositsWithDepositors()");
                Definition wsdl = soapClient.readWSDLFile(wsdlLocation);
                wsdlServices = getServices(wsdl, getServiceMethods(soapClient));
                LOGGER.debug("services - {}", wsdlServices.get("name"));

                soapResponse[0] = wsdl.toString();
                soapResponse[1] = "";
                soapRequest = "";

                soapClient.setDefaultUri(serviceAddress);
            } catch (Exception e) {
                LOGGER.debug("getSoapView(), Exception:{}", e.toString());
                redirectAttributes.addFlashAttribute("message", e.getMessage());

                soapResponse[0] = e.getMessage() + "\n" + e.toString();
            }
        } else{
            wsdlServices.put("",null);
        }

        ModelAndView view = new ModelAndView("mainFrame");
        view.addObject("services",wsdlServices);
        view.addObject("requests",soapRequest);
        view.addObject("responses",soapResponse);

        return view;
    }

    /*
        // JSON
        // Create properties
        Map<String, Object> propertiesJSON = new HashMap<String, Object>();
        propertiesJSON.put("eclipselink.media-type", "application/json");
        // Create a JaxBContext
        JAXBContext contextJSON
                = JAXBContextFactory.createContext(
                        new Class[]{GetBankDepositsResponse.class},
                        propertiesJSON);
        // Create the Marshaller Object using the JaxB Context
        Marshaller marshallerJSON = contextJSON.createMarshaller();
        // Set it to true if you need to include the JSON root element in the JSON output
        marshallerJSON.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
        // Set it to true if you need the JSON output to formatted
        marshallerJSON.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        // Marshal the employee object to JSON and print the output to console
        marshallerJSON.marshal(response, System.out);
    */
}

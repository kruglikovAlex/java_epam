<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Tell the JSP Page that please do not ignore Expression Language -->
<%@ page isELIgnored="false"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="en">
    <head>
        <title>SOAP Client</title>
    </head>
    <body>
        <link href='<c:url value="/resources/css/jquery-ui.css"/>' rel="stylesheet">
        <link href='<c:url value="/resources/css/bankDeposit.css"/>' rel="stylesheet">

        <div id="dialog-form" title="Get WSDL:">
        	<form>
        		<fieldset>
        			<label for="wsdlUrl">URL:</label>
        			<input type="text" name="WSDL" id="wsdlURL" size="75" class="text ui-widget-content ui-corner-all">
        			<!-- Allow form submission with keyboard without duplicating the dialog button -->
        			<input type="submit" tabindex="-1" style="position:absolute; top:-1000px">
        		</fieldset>
        	</form>
        </div>

        <div id="wsdlURL-contain" class="ui-widget">
	        <legend style="font-weight: bold">
		        WSDL: <button id="get-wsdl">Get</button>
	        </legend>
        </div>

        <table id="wsdlRows" frame="hsides" rules="cols">
            <thead>
                <tr>
                    <th><label>Services</label></th>
                    <th><label>SOAP Request, SOAP/JSON Response:</label></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="vertical-align: top">
                            <div id="accordion">
                                <c:forEach items="${services}" var="service">
                                    <h3>${service.key}</h3>
                                    <div>
                                        <form method="post" id="${service.key}" action='<c:url value="/soap/client/submitSoapQuery?soapQuery=${service.key}"/>' modelAttribute="query">
                                            <ul>
                                                <c:forEach items="${service.value}" var="parameters">
                                                    <li>
                                                        ${parameters}:
                                                        <input type="text" id="${parameters}" name="${parameters}"/>
                                                        <br/>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                            <input id="submit" type="submit" value="Generate Request" name="submit"/>
                                        </form>
                                    </div>
                                </c:forEach>
                            </div>
                    </td>
                    <td style="vertical-align: top">
                        <div id="tabs">
                          <ul>
                            <li><a href="#tabs-1">Request</a></li>
                            <c:forEach begin="0" end="${fn:length(responses) - 1}" var="index">
                                <li><a href="#tabs-${index+2}">Response ${index+2}</a></li>
                            </c:forEach>
                          </ul>
                          <div id="tabs-1">
                             <textarea id="soapRequest" name="soapRequest" ><c:out value="${requests}"/></textarea>
                          </div>
                          <c:forEach begin="0" end="${fn:length(responses) - 1}" var="index">
                            <div id="tabs-${index+2}">
                                 <textarea id="soapResponse${index}" name="soapResponse${index}"><c:out value="${responses[index]}"/></textarea>
                            </div>
                          </c:forEach>
                        </div>


                    </td>
                </tr>
            </tbody>

        </table>

        <script src='<c:url value="/resources/jquery-3.1.1.js"/>'></script>
        <script src='<c:url value="/resources/jquery-ui.js"/>'></script>
        <script src='<c:url value="/resources/mainWeb.js"/>'></script>

    </body>
</html>

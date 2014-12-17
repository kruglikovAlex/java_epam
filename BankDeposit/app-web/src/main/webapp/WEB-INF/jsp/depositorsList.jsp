<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Tell the JSP Page that please do not ignore Expression Language -->
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
<script>

 $('#start').live('click', function(){
     $('table .Am').each(function(){
         total += Number($(this).text());
     });
     $('table .total').html( total );

 });

</script>

</head>

<body>
<link href="<c:url value="/resources/css/bankDeposit.css" />" rel="stylesheet">

<b><a href='<spring:url value="/depositsList" />' > <spring:message code="deposit.create" /></a></b><br>
<b><a href='<spring:url value="/inputFormDepositor" />' > <spring:message code="depositor.create" /></a></b>

<form:form method="get" modelAttribute="depositors">
		<h1><spring:message code="depositor.list" /></h1>
        	<ul>
            	<table class="dep" frame="hsides" rules="cols">
                       			<th>
                          	    	<td><b>Id</td>
                                	<td><b>Name</td>
                                	<td><b>Id Deposit</td>
                                	<td><b>Date Deposit</td>
                                	<td><b>Amount Deposit</td>
                                    <td><b>Amount Plus</td>
                                    <td><b>Amount Minus</td>
                                    <td><b>Date Return</td>
                                    <td><b>Mark Return</td>
                                    <td class="add" COLSPAN="2" align=middle><a href='<spring:url value="/inputFormDepositor" >  <spring:param name="id" value="${depositor.depositorId}"/>   </spring:url>' class="buttonAdd"><b>ADD</b></a></td>
                            	</th>
                            	<c:forEach items="${depositors}" var="depositor">
                           		<tr>
                                	<td/>
                                	<td>${depositor.depositorId}</td>
                                	<td>${depositor.depositorName}</td>
                                	<td>${depositor.depositorIdDeposit}</td>
                                	<td>${depositor.depositorDateDeposit}</td>
                                	<td class="Am">${depositor.depositorAmountDeposit}</td>
                                	<td class="Pl">${depositor.depositorAmountPlusDeposit}</td>
                                	<td class="Min">${depositor.depositorAmountMinusDeposit}</td>
                                	<td>${depositor.depositorDateReturnDeposit}</td>
                                	<td>${depositor.depositorMarkReturnDeposit}</td>
                                	<td class="upd"><a href='<spring:url value="/updateFormDepositor" >  <spring:param name="depositorId" value="${depositor.depositorId}"/>   </spring:url>' class="buttonUpd"><b>UPDATE</b></a></td>
                                	<td class="del"><a href='<spring:url value="/delete" >  <spring:param name="depositorId" value="${depositor.depositorId}"/>   </spring:url>' class="buttonDel"><b>DELETE</b></a></td>
                            	</tr>
                            	</c:forEach>
                            	<TFOOT>
                            	<tr>
                            		  <th/>
                                      <th><b>Summ:</th>
                                      <th COLSPAN="3"></th>
                                      <th class="total"><b></th>
                                      <th class="sPl_total"><b></th>
                                      <th class="SMin_total"><b></th>
                                      <th COLSPAN="2"></th>
									</tr>
                            	</TFOOT>
                        	</table>
<input type="button" id="start" value="посчитать вес">


			</ul>
</form:form>

<script src="/resources/js/jquery-1.11.1.js"></script>
</body>
</html>
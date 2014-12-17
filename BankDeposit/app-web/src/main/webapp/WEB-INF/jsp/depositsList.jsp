<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Tell the JSP Page that please do not ignore Expression Language -->
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<body>

<link href="<c:url value="/resources/css/bankDeposit.css" />" rel="stylesheet">

<a href='<spring:url value="/inputFormDeposit" />'> <spring:message code="deposit.create" /></a>

<form:form method="get" modelAttribute="deposits">
    <h1><spring:message code="deposit.list" /></h1>
        <ul>
            <table frame="hsides" rules="cols">
                <th>
          	        <td><b>Id</td>
                    <td><b>Name</td>
                    <td><b>Min Term</td>
                    <td><b>Min Amount</td>
                    <td><b>Currency</td>
                    <td><b>Interest Rate, (%)</td>
                    <td><b>Add Conditions</td>
                    <td class="add" COLSPAN="2" align=middle><a href='<spring:url value="/inputFormDeposit" >  <spring:param name="id" value="${deposit.depositId}"/>   </spring:url>' class="buttonAdd"><b>ADD</b></a></td>
                </th>
                <c:forEach items="${deposits}" var="deposit">
                    <tr>
                            <td/>
                            <td>${deposit.depositId}</td>
                            <td>${deposit.depositName}</td>
                            <td>${deposit.depositMinTerm}</td>
                            <td>${deposit.depositMinAmount}</td>
                            <td>${deposit.depositCurrency}</td>
                            <td>${deposit.depositInterestRate}</td>
                            <td>${deposit.depositAddConditions}</td>

                            <td class="upd"><a href='<spring:url value="/updateFormDeposit" >  <spring:param name="depositId" value="${deposit.depositId}"/>   </spring:url>' class="buttonUpd"><b>UPDATE</b></a></td>
                            <td class="del"><a href='<spring:url value="/deleteDeposit" >  <spring:param name="depositId" value="${deposit.depositId}"/>   </spring:url>' class="buttonDel"><b>DELETE</b></a></td>

                    </tr>
                </c:forEach>
            </table>
        </ul>

        <a href='<spring:url value="/inputFormDepositor" />' > <spring:message code="depositor.create" /></a>

        <h1><spring:message code="depositor.list" /></h1>
        <ul>
            <table frame="hsides" rules="cols">
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
                                            	<td>${depositor.depositorAmountDeposit}</td>
                                            	<td>${depositor.depositorAmountPlusDeposit}</td>
                                            	<td>${depositor.depositorAmountMinusDeposit}</td>
                                            	<td>${depositor.depositorDateReturnDeposit}</td>
                                            	<td>${depositor.depositorMarkReturnDeposit}</td>
                                            	<td class="upd"><a href='<spring:url value="/updateFormDepositor" >  <spring:param name="depositorId" value="${depositor.depositorId}"/>   </spring:url>' class="buttonUpd"><b>UPDATE</b></a></td>
                                            	<td class="del"><a href='<spring:url value="/delete" >  <spring:param name="depositorId" value="${depositor.depositorId}"/>   </spring:url>' class="buttonDel"><b>DELETE</b></a></td>
                                        	</tr>
                                        	</c:forEach>
                                        	<TFOOT>
                                        	<td/>
                                                  <td><b>Summ:</td>
                                                  <td COLSPAN="3"></td>
                                                  <td><b>Amount</td>
                                                  <td><b>Amount Plus</td>
                                                  <td><b>Amount Minus</td>
                                                  <td COLSPAN="2"></td>

                                        	</TFOOT>
                                    	</table>
        </ul>
</form:form>

</body>
</html>
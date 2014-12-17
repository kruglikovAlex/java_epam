<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Tell the JSP Page that please do not ignore Expression Language -->
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
<script>
$(document).ready(function(){
    var summ = 0;
    $('td.summ1').mouseenter(function(){
        alert("Указатель мыши на td.summ1");
    });
    $('td.summ1').click(function(){
        $('td.Am').each(function(){
            summ += Number($(this).text());
        });
        $('td.summ1').text(summ);
    });
});
</script>
</head>
<body>
<p><h1>OOO "New Age Bank"</h1></p>
<link href="<c:url value="/resources/css/bankDeposit.css" />" rel="stylesheet">

<form:form method="get" modelAttribute="deposits">
    <h2><spring:message code="deposit.list" /></h2>
        <ul>
            <table frame="hsides" rules="cols">
                <th>
          	        <td align=middle><b>Id</td>
                    <td><b>Name</td>
                    <td><b>Min Term</td>
                    <td><b>Min Amount</td>
                    <td><b>Currency</td>
                    <td><b>Interest Rate, (%)</td>
                    <td><b>Add Conditions</td>
                    <td class="add" COLSPAN="2" align=middle><a href='<spring:url value="/inputFormDeposit" >  <spring:param name="id" value="${deposit.depositId}"/>   </spring:url>' class="buttonAdd"><b>ADD DEPOSIT</b></a></td>
                </th>
                <c:forEach items="${deposits}" var="deposit">
                    <tr>
                        <td/>
                        <td><a href='<spring:url value="/inputFormDepositor" >  <spring:param name="depositorIdDeposit" value="${deposit.depositId}"/>   </spring:url>' class="buttonAdd"><b>ADD_DEPOSITOR_(${deposit.depositId})</td>
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

        <h2><spring:message code="depositor.list" /></h2>
        <ul>
        <a href='<spring:url value="/depositsList" > </spring:url>' class="buttonFilter">Clean filter</a>
        <form action="/filterBetweenDateDeposit" method="POST">
            <label path="depositorDateDeposit">Date deposit, (yyyy-mm-dd)- from :</label>
                <input  id="MyDate1" type="text" name="StartDateDeposit"/>
            <label path="depositorDateDeposit"> to :</label>
                <input id="MyDate2" type="text" name="EndDateDeposit"/>
            <input type="submit" name="Submit" value="filter go">
        </form>

        <form action="/filterBetweenDateReturnDeposit" method="POST">
            <label path="depositorDateReturnDeposit">Date Return deposit, (yyyy-mm-dd)- from :</label>
                <input id="MyDate3" type="text" name="StartDateReturnDeposit"/>
            <label path="depositorDateReturnDeposit"> to :</label>
                <input id="MyDate4" type="text" name="EndDateReturnDeposit"/>
            <input type="submit" name="Submit" value="filter go">
        </form>
        <p>
            <table frame="hsides" rules="cols">
                <th>
                    <td align=middle><b>Id</td>
                    <td><b>Name</td>
                    <td><b>Id Deposit</td>
                    <td><b>Date_Deposit</td>
                    <td><b>Amount Deposit</td>
                    <td><b>Amount Plus</td>
                    <td><b>Amount Minus</td>
                    <td><b>Date_Return Deposit</td>
                    <td><b>Mark Return</td>
                    <td class="add" COLSPAN="2"></td>
                </th>
                <c:forEach items="${depositors}" var="depositor">
                    <tr>
                        <td/>
                        <td>${depositor.depositorId}</td>
                        <td>${depositor.depositorName}</td>
                        <td>${depositor.depositorIdDeposit}</td>
                        <td>${depositor.depositorDateDeposit}</td>
                        <td class="Am">${depositor.depositorAmountDeposit}</td>
                        <td class="Plus">${depositor.depositorAmountPlusDeposit}</td>
                        <td class="Minus">${depositor.depositorAmountMinusDeposit}</td>
                        <td>${depositor.depositorDateReturnDeposit}</td>
                        <td>${depositor.depositorMarkReturnDeposit}</td>
                        <td class="upd"><a href='<spring:url value="/updateFormDepositor" >  <spring:param name="depositorId" value="${depositor.depositorId}"/>   </spring:url>' class="buttonUpd"><b>UPDATE</b></a></td>
                        <td class="del"><a href='<spring:url value="/delete" >  <spring:param name="depositorId" value="${depositor.depositorId}"/>   </spring:url>' class="buttonDel"><b>DELETE</b></a></td>
                    </tr>
                </c:forEach>
                <TFOOT>
                    <td/>
                    <td>Summ:</td>
                    <td COLSPAN="3"></td>
                    <td class="summ1"></td>
                    <td class="summ2"></td>
                    <td class="summ3"></td>
                    <td COLSPAN="4"></td>
                </TFOOT>
            </table>
        </p>
        </ul>
</form:form>

<script src="/resources/js/jquery-1.11.1.js"></script>
<script src="/resources/js/jquery.maskedinput.js"></script>
<script src="/resources/js/bankDeposit.js"></script>
</body>
</html>
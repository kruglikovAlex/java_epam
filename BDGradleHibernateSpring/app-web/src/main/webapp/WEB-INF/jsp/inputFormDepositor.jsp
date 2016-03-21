<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<style>
    .error {
        color: red; font-weight: bold;
    }
</style>
<body>
    <form action='<spring:url value="/depositors/submitDataDepositor" > </spring:url>' method="post" modelAttribute="depositor" commandName="depositor">
    	<form:form method="GET" modelAttribute="depositor">
			<h1><spring:message code="depositor.create" /></h1>
        	<ul>
        	    <label path="depositorIdDeposit">Id Deposit:</label>
                     <input type="text" name="depositorIdDeposit" value='<c:out value="${depositor.depositorIdDeposit}"/>' hidden/>
                        <span class="error">
                            <form:errors path="depositorIdDeposit" size="100"/>
                        </span><br/>
  	            <label path="depositorName"><b>Full name Depositor:</b></label>
  	                <input type="text" id="depositorName" name="depositorName" value='<c:out value="${depositor.depositorName}"/>' size="30"/>
                        <span class="error">
                            <form:errors id="errordepositorName" path="depositorName" size="100"/>
                        </span><br/>
                <label path="depositorDateDeposit">Date Deposit, (yyyy-mm-dd):</label>
                    <input type="text" id="depositorDateDeposit" name="depositorDateDeposit"
                    pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size="10" value='<fmt:formatDate value="${depositor.depositorDateDeposit}" type="both" pattern="yyyy-MM-dd"/>' />
                        <span class="error">
                            <form:errors id="errordepositorDateDeposit" path="depositorDateDeposit" size="100"/>
                        </span><br/>
                <label path="depositorAmountDeposit">Amount:</label>
                    <input type="text" id="depositorAmountDeposit" name="depositorAmountDeposit" value='<c:out value="${depositor.depositorAmountDeposit}"/>'/>
                        <span class="error">
                            <form:errors id="errordepositorAmountDeposit" path="depositorAmountDeposit" size="100"/>
                        </span><br/>
                <label path="depositorAmountPlusDeposit">Amount Plus:</label>
                    <input type="text" id="depositorAmountPlusDeposit" name="depositorAmountPlusDeposit" value='<c:out value="${depositor.depositorAmountPlusDeposit}"/>'/>
                        <span class="error">
                            <form:errors id="errordepositorAmountPlusDeposit" path="depositorAmountPlusDeposit" size="100"/>
                        </span><br/>
                <label path="depositorAmountMinusDeposit">Amount Minus:</label>
                    <input type="text" id="depositorAmountMinusDeposit" name="depositorAmountMinusDeposit" value='<c:out value="${depositor.depositorAmountMinusDeposit}"/>'/>
                        <span class="error">
                            <form:errors id="errordepositorAmountMinusDeposit" path="depositorAmountMinusDeposit" size="100"/>
                        </span><br/>
                <label path="depositorDateReturnDeposit">Date Return, (yyyy-mm-dd):</label>
                    <input type="text" id="depositorDateReturnDeposit" name="depositorDateReturnDeposit"
                    pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size="10" value='<fmt:formatDate value="${depositor.depositorDateReturnDeposit}" type="both" pattern="yyyy-MM-dd"/>'/>
                        <span class="error">
                            <form:errors id="errordepositorDateReturnDeposit" path="depositorDateReturnDeposit" size="100"/>
                        </span><br/>
                <label path="depositorMarkReturnDeposit:">Mark Return:</label>
                    <input type="text" id="depositorMarkReturnDeposit" name="depositorMarkReturnDeposit" value='<c:out value="${depositor.depositorMarkReturnDeposit}"/>' size="1"/>
                        <span class="error">
                            <form:errors id="errordepositorMarkReturnDeposit" path="depositorMarkReturnDeposit" size="100"/>
                        </span><br/>
            </ul>
            <input id="submit" type="submit" name="submit">
        </form:form>
    </form>

    <script src='<c:url value="/resources/js/jquery-1.11.1.js"/>'></script>
    <script src='<c:url value="/resources/js/jquery.maskedinput.js"/>'></script>
    <script src='<c:url value="/resources/js/bankDeposit.js"/>'></script>

    <script>
        var nullInt = function () {
            if(this.value == null || this.value == '') {
                document.getElementById('error'+this.name).style.border=  "1px dotted red"
                document.getElementById('error'+this.name).style.fontSize = "x-large";
                alert('The fild '+ this.name+' is empty!!!');
            }
        }

        depositorName.onchange = nullInt;
        depositorName.onkeyup = nullInt;
        depositorAmountDeposit.onchange = nullInt;
        depositorAmountDeposit.onkeyup = nullInt;
        depositorAmountPlusDeposit.onchange = nullInt;
        depositorAmountPlusDeposit.onkeyup = nullInt;
        depositorAmountMinusDeposit.onchange = nullInt;
        depositorAmountMinusDeposit.onkeyup = nullInt;
        depositorMarkReturnDeposit.onchange = nullInt;
        depositorMarkReturnDeposit.onkeyup = nullInt;

    </script>

    <script>
        var nullIntSubmit = function () {
            if(depositorName.value == null || depositorName.value == '') {
                depositorName.value = "Input Full name of depositor!";
            }
            if(depositorAmountDeposit.value == null || depositorAmountDeposit.value == '') {
                depositorAmountDeposit.value = "0";
            }
            if(depositorAmountPlusDeposit.value == null || depositorAmountPlusDeposit.value == '') {
                depositorAmountPlusDeposit.value = "0";
            }
            if(depositorAmountMinusDeposit.value == null || depositorAmountMinusDeposit.value == '') {
                depositorAmountMinusDeposit.value = "0";
            }
            if(depositorMarkReturnDeposit.value == null || depositorMarkReturnDeposit.value == '') {
                depositorMarkReturnDeposit.value = "0";
            }
        }
        submit.onclick = nullIntSubmit;

    </script>

</body>
</html>
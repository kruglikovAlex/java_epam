<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
    <link href='<c:url value="/resources/css/jquery-ui.css" />' rel="stylesheet">
 	<form action='<spring:url value="/depositor/updateDepositor" > </spring:url>' method="POST" modelAttribute="depositor" commandName="depositor">
    	<form:form method="GET" modelAttribute="depositor">
			<h1><spring:message code="update.depositor" /></h1>
        	<ul>
        	    <label path="depositorId:">Id:</label>
        	        <input type="text" id="depositorId" name="depositorId" value='<c:out value="${depositor.depositorId}"/>' hidden/><br/>
            	<label path="depositorName:">Full name Depositor:</label>
            	    <input type="text" id="depositorName" name="depositorName" value='<c:out value="${depositor.depositorName}"/>'/>
            	        <span class="error">
            	            <form:errors id="errordepositorName" path="depositorName" size="100"/>
            	        </span><br/>
                <label path="depositorDateDeposit:">Date Deposit:</label>
                	<input type="text" id="depositorDateDeposit" name="depositorDateDeposit"
                	pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size="10" value='<fmt:formatDate value="${depositor.depositorDateDeposit}" type="both" pattern="yyyy-MM-dd"/>' class="datepicker"/>
                	    <span class="error">
                	        <form:errors id="errordepositorDateDeposit" path="depositorDateDeposit" size="100"/>
                	    </span><br/>
                <label path="depositorAmountDeposit:">Amount:</label>
                	<input type="text" id="depositorAmountDeposit" name="depositorAmountDeposit" value='<c:out value="${depositor.depositorAmountDeposit}"/>'/>
                	    <span class="error">
                	        <form:errors id="errordepositorAmountDeposit" path="depositorAmountDeposit" size="100"/>
                	    </span><br/>
                <label path="depositorAmountPlusDeposit:">Amount Plus:</label>
                	<input type="text" id="depositorAmountPlusDeposit" name="depositorAmountPlusDeposit" value='<c:out value="${depositor.depositorAmountPlusDeposit}"/>'/>
                	    <span class="error">
                	        <form:errors id="errordepositorAmountPlusDeposit" path="depositorAmountPlusDeposit" size="100"/>
                	    </span><br/>
                <label path="depositorAmountMinusDeposit:">Amount Minus:</label>
                	<input type="text" id="depositorAmountMinusDeposit" name="depositorAmountMinusDeposit" value='<c:out value="${depositor.depositorAmountMinusDeposit}"/>'/>
                	    <span class="error">
                	        <form:errors id="errordepositorAmountMinusDeposit" path="depositorAmountMinusDeposit" size="100"/>
                	    </span><br/>
                <label path="depositorDateReturnDeposit:">Date Return:</label>
                	<input type="text" id="depositorDateReturnDeposit" name="depositorDateReturnDeposit"
                    pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size="10" value='<fmt:formatDate value="${depositor.depositorDateReturnDeposit}" type="both" pattern="yyyy-MM-dd"/>' class="datepicker"/>
                	    <span class="error">
                	        <form:errors path="depositorDateReturnDeposit" size="100"/>
                	    </span><br/>
                <label path="depositorMarkReturnDeposit:">Mark Return:</label>
                	<input type="text" id="depositorMarkReturnDeposit" name="depositorMarkReturnDeposit" value='<c:out value="${depositor.depositorMarkReturnDeposit}"/>' size="1"/>
                	    <span class="error">
                	        <form:errors id="errordepositorMarkReturnDeposit" path="depositorMarkReturnDeposit" size="100"/>
                	    </span><br/>
			</ul>
    		<input id="submit" type="submit" name="submit"></form:form>
    </form>

    <script src='<c:url value="/resources/js/jquery-1.11.1.js"/>'></script>
    <script src='<c:url value="/resources/js/jquery.maskedinput.js"/>'></script>
    <script src='<c:url value="/resources/js/bankDeposit.js"/>'></script>
    <script src='<c:url value="/resources/js/depositorFrame.js"/>'></script>
    <script src='<c:url value="/resources/js/jquery-ui.js"/>'></script>
    <script src='<c:url value="/resources/js/jquery-ui.min.js"/>'></script>

    <script>
        var nullInt = function () {
            if(this.value == null || this.value == '') {
                document.getElementById('error'+this.name).style.border=  "1px dotted red"
                document.getElementById('error'+this.name).style.fontSize = "x-large";
                alert('The field '+ this.name+' is empty!!!');
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
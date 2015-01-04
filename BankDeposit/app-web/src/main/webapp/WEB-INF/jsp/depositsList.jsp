<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Tell the JSP Page that please do not ignore Expression Language -->
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
</head>
<body>
<link href="<c:url value="/resources/css/bankDeposit.css" />" rel="stylesheet">
<fieldset style="width:80% " >
	<legend style="font-weight: bold">DEPOSITS. </legend>
    <div>
        <form:form method="get" modelAttribute="deposits">
             <div>
                 <table style="width: 80%" border=0 rules="none">
                     <tr>
                         <td>
                             <form id="form_id" action='<spring:url value="/deposits/filterByIdDeposit" > </spring:url>' method="POST">
                                 <label path="depositById">Find by id :</label>
                                    <input  id="depositById" type="text" name="depositById" />
                                 <input type="submit" name="Submit" value="<-Find">
                             </form>
                         </td>
                         <td ROWSPAN="2">
                             <a href="#" id="linkId" class="buttonFilterBig">&#8656;</br>Find</br>&#8656;</a></br>
                         </td>
                         <td ROWSPAN="2">
                             <a href="#" id="linkIdReturn" class="buttonFilterBig">&#8656;</br>Find</br>&#8195;&#8658;</a></br>
                         </td>
                         <td ROWSPAN="2" >
                             <a href="#" id="linkName" class="buttonFilterBig">&#8195;&#8658;</br>Find</br>&#8656;</a></br>
                         </td>
                         <td ROWSPAN="2" >
                             <a href="#" id="linkNameReturn" class="buttonFilterBig">&#8195;&#8658;</br>Find</br>&#8195;&#8658;</a></br>
                         </td>
                         <td ROWSPAN="2">&#8195;</td>
                         <td>
                             <form action='<spring:url value="/deposits/filterByNameDeposit" > </spring:url>' method="POST">
                                 <label path="depositByName">Find by name :</label>
                                     <input id="depositByName" type="text" name="depositByName" >
                                 <input type="submit" name="Submit" value="<-Find">
                             </form>
                         </td>
                         <td ROWSPAN="2">
                             <a href='<spring:url value="/deposits/" > </spring:url>' class="buttonFilter">Clean filter</a>
                         </td>
                     </tr>
                     <tr>
                         <td>
                              <form action='<spring:url value="/deposits/filterBetweenDateDeposit" > </spring:url>' method="POST">
                                  <label path="depositorDateDeposit">Date deposit,(yyyy-mm-dd)</label></br>
                                       from :<input  id="StartDateDeposit" type="text" name="StartDateDeposit"
                                             pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size=10/>
                                  <label path="depositorDateDeposit"> to :</label>
                                       <input id="EndDateDeposit" type="text" name="EndDateDeposit"
                                             pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size=10/>
                                  <input type="submit" name="Submit" value="<-Find">
                              </form>
                         </td>
                         <td>
                              <form action='<spring:url value="/deposits/filterBetweenDateReturnDeposit" > </spring:url>' method="POST">
                                  <label path="depositorDateReturnDeposit">Date Return deposit,(yyyy-mm-dd)</label></br>
                                        from :<input id="StartDateReturnDeposit" type="text" name="StartDateReturnDeposit"
                                              pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size=10/>
                                  <label path="depositorDateReturnDeposit"> to :</label>
                                        <input id="EndDateReturnDeposit" type="text" name="EndDateReturnDeposit"
                                              pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size=10/>
                                  <input type="submit" name="Submit" value="<-Find">
                              </form>
                         </td>
                     </tr>
                 </table>
             <div>
            <h3><spring:message code="deposit.list" /></h3>
            <table class="scrolling-table" >
                <thead>
          	        <td align=middle COLSPAN="2"><b>Id</td>
                    <td COLSPAN="2"><b>Name</td>
                    <td><b>Min Term</td>
                    <td><b>Min Amount</td>
                    <td><b>Currency</td>
                    <td><b>Interest Rate, (%)</td>
                    <td COLSPAN="2"><b>Add Conditions</td>
                    <td>test Id Deposit</td>
                    <td>Summ amount</td>
                    <td>Summ plus</td>
                    <td>Summ minus</td>
                    <td>Count depositors</td>
                    <td class="add" COLSPAN="2" align=middle><a href='<spring:url value="/deposits/inputFormDeposit" > </spring:url>' class="buttonAdd"><b>ADD DEPOSIT</b></a></td>
                </thead>
                <tbody>
                <c:forEach items="${deposits}" var="deposit">
                    <tr>
                        <td COLSPAN="2"><a href='<spring:url value="/depositors/inputFormDepositor" >  <spring:param name="depositorIdDeposit" value="${deposit.depositId}"/>   </spring:url>' class="buttonAdd"><b>ADD_DEPOSITOR_(${deposit.depositId})</td>
                        <td COLSPAN="2">${deposit.depositName}</td>
                        <td>${deposit.depositMinTerm}</td>
                        <td>${deposit.depositMinAmount}</td>
                        <td>${deposit.depositCurrency}</td>
                        <td>${deposit.depositInterestRate}</td>
                        <td COLSPAN="2">${deposit.depositAddConditions}</td>
                        <td>${deposit.depId}</td>
                        <td>${deposit.sumAmount}</td>
                        <td>${deposit.sumPlusAmount}</td>
                        <td>${deposit.sumMinusAmount}</td>
                        <td>${deposit.numDepositors}</td>
                        <td class="upd"><a href='<spring:url value="/deposits/updateFormDeposit" >  <spring:param name="depositId" value="${deposit.depositId}"/>   </spring:url>' class="buttonUpd"><b>UPDATE</b></a></td>
                        <td class="del"><a href='<spring:url value="/deposits/deleteDeposit" >  <spring:param name="depositId" value="${deposit.depositId}"/>   </spring:url>' class="buttonDel"><b>DELETE</b></a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
    </div>
</fieldset>
<fieldset style="width: 60% " >
	<legend style="font-weight: bold">DEPOSITORS. </legend>
        <a href='<spring:url value="/deposits/" > </spring:url>' class="buttonFilter">Clean filter</a>
        <div>
            <table style="width: 90%" border=0 rules="none">
                <tr>
                    <td>
                        <form action='<spring:url value="/depositors/filterByIdDepositor" > </spring:url>' method="POST">
                            <label path="depositorById">Find by id :</label>
                               <input  id="depositorById" type="text" name="depositorById" />
                            <input type="submit" name="Submit" value="<-Find">
                        </form>
                    </td>
                    <td>
                        <form action='<spring:url value="/depositors/filterByNameDepositor" > </spring:url>' method="POST">
                            <label path="depositorByName">Find by name :</label>
                                <input id="depositorByName" type="text" name="depositorByName" />
                            <input type="submit" name="Submit" value="<-Find">
                        </form>
                    </td>
                    <td>
                        <form action='<spring:url value="/depositors/filterByIdDepositDepositor" > </spring:url>' method="POST">
                             <label path="depositorByIdDeposit">Find by id deposit:</label>
                             <input  id="depositorByIdDeposit" type="text" name="depositorByIdDeposit" />
                             <input type="submit" name="Submit" value="<-Find">
                        </form>
                    </td>
                </tr>
                <tr>
                    <td>
                        <form action='<spring:url value="/depositors/filterBetweenDateDeposit" > </spring:url>' method="POST">
                              <label path="depositorDateDeposit">Date deposit,(yyyy-mm-dd)</label></br>
                                 from :<input  id="MyDate1" type="text" name="StartDateDeposit"
                                        pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size=10/>
                              <label path="depositorDateDeposit"> to :</label>
                                 <input id="MyDate2" type="text" name="EndDateDeposit"
                                        pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size=10/>
                              <input type="submit" name="Submit" value="<-Find">
                            </form>
                    </td>
                    <td>
                        <form action='<spring:url value="/depositors/filterBetweenDateReturnDeposit" > </spring:url>' method="POST">
                            <label path="depositorDateReturnDeposit">Date Return deposit,(yyyy-mm-dd)</label></br>
                               from :<input id="MyDate3" type="text" name="StartDateReturnDeposit"
                                        pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size=10/>
                            <label path="depositorDateReturnDeposit"> to :</label>
                               <input id="MyDate4" type="text" name="EndDateReturnDeposit"
                                        pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size=10/>
                            <input type="submit" name="Submit" value="<-Find">
                        </form>
                    </td>
                </tr>
            </table>
        </div>
        <h3><spring:message code="depositor.list" /></h3>
            <table class="scrolling-table" >
                <thead>
                    <td align=middle><b>Id</td>
                    <td><b>Name</td>
                    <td><b>Id Deposit</td>
                    <td><b>Date Deposit</td>
                    <td><b>Amount Deposit</td>
                    <td><b>Amount Plus</td>
                    <td><b>Amount Minus</td>
                    <td><b>Date Return Deposit</td>
                    <td><b>Mark Return</td>
                    <td class="add" COLSPAN="2"></td>
                </thead><tbody>
                <c:forEach items="${depositors}" var="depositor">
                    <tr>
                        <td>${depositor.depositorId}</td>
                        <td>${depositor.depositorName}</td>
                        <td>${depositor.depositorIdDeposit}</td>
                        <td>${depositor.depositorDateDeposit}</td>
                        <td class="Am">${depositor.depositorAmountDeposit}</td>
                        <td class="Plus">${depositor.depositorAmountPlusDeposit}</td>
                        <td class="Minus">${depositor.depositorAmountMinusDeposit}</td>
                        <td>${depositor.depositorDateReturnDeposit}</td>
                        <td>${depositor.depositorMarkReturnDeposit}</td>
                        <td class="upd"><a href='<spring:url value="/depositors/updateFormDepositor" >  <spring:param name="depositorId" value="${depositor.depositorId}"/>   </spring:url>' class="buttonUpd"><b>UPDATE</b></a></td>
                        <td class="del"><a href='<spring:url value="/depositors/delete" >  <spring:param name="depositorId" value="${depositor.depositorId}"/>   </spring:url>' class="buttonDel"><b>DELETE</b></a></td>
                    </tr>
                </c:forEach>
                </tbody>
                <TFOOT>
                <c:forEach items="${depositorSum}" var="depositor">
                    <tr>
                         <td>Summ:</td>
                         <td COLSPAN="3"></td>
                         <td>${depositor.depositorAmountDeposit}</td>
                         <td>${depositor.depositorAmountPlusDeposit}</td>
                         <td>${depositor.depositorAmountMinusDeposit}</td>
                         <td COLSPAN="4"></td>
                    </tr>
                </c:forEach>
                </TFOOT>
            </table>

        </form:form>
</fieldset>

<script src='<c:url value="/resources/js/jquery-1.11.1.js"/>'></script>
<script src='<c:url value="/resources/js/jquery.maskedinput.js"/>'></script>
<script src='<c:url value="/resources/js/bankDeposit.js"/>'></script>
<script>
    var ntext = function () {
        var sh1 = '<spring:url value="';
        var sh2 = +'"></spring:url>';
        linkId.href = sh1+"/deposits/filterByIdDepositBetweenDateDeposit?depositById="+depositById.value+"&StartDateDeposit="+StartDateDeposit.value+"&EndDateDeposit="+this.value+sh2;
        linkName.href = sh1+"/deposits/filterByNameDepositBetweenDateDeposit?depositByName="+depositByName.value+"&StartDateDeposit="+StartDateDeposit.value+"&EndDateDeposit="+this.value+sh2;
        }
    EndDateDeposit.onchange = ntext;
    EndDateDeposit.onkeyup = ntext;

</script>
<script>
    var rtext = function () {
        var sh1 = '<spring:url value="';
        var sh2 = +'"></spring:url>';
        linkIdReturn.href = sh1+"/deposits/filterByIdDepositBetweenDateReturnDeposit?depositById="+depositById.value+"&StartDateReturnDeposit="+StartDateReturnDeposit.value+"&EndDateReturnDeposit="+this.value+sh2;
        linkNameReturn.href = sh1+"/deposits/filterByNameDepositBetweenDateReturnDeposit?depositByName="+depositByName.value+"&StartDateReturnDeposit="+StartDateReturnDeposit.value+"&EndDateReturnDeposit="+this.value+sh2;
        }
    EndDateReturnDeposit.onchange = rtext;
    EndDateReturnDeposit.onkeyup = rtext;

</script>

</body>
</html>
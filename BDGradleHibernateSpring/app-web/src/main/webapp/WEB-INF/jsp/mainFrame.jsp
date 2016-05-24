<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Tell the JSP Page that please do not ignore Expression Language -->
<%@ page isELIgnored="false"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="en">
    <head>
        <title>Deposits/Depositors list</title>
    </head>
    <body>
        <link href='<c:url value="/resources/css/bankDeposit.css" />' rel="stylesheet">
        <link href='<c:url value="/resources/css/jquery-ui.css" />' rel="stylesheet">

        <div id="tabs">
            <ul>
                <li><a href="#tabs-1"><b>View/Edit</b></a></li>
                <li><a href="#tabs-2"><b>Find</b></a></li>
                <li><a href="#tabs-3"><b>Report</b></a></li>
            </ul>
            <div id="tabs-1">
                <form method="GET" id="dForm" modelAttribute="deposits">
                    <table>
                        <tr>
                            <td COLSPAN="2">
                                Year:<input id="year" type="text" name="year" value="${year}" size="4"/>
                                Id:<input id="dId" type="text" name="Id" size="15"/>
                            </td>
                        </tr>
                        <tr>
                            <td><b>Deposits list:</b>
                                <select name="depositId" size="3" multiple="true">
                                    <c:forEach var="deposit" items="${deposits}">
                                        <c:choose>
                                            <c:when test="${deposit.depositId==idDeposit}">
                                                <option value="${deposit.depositId}" selected>
                                                    <c:out value="${deposit.depositId} | ${deposit.depositName}"/>
                                                </option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${deposit.depositId}">
                                                    <c:out value="${deposit.depositId} | ${deposit.depositName}"/>
                                                </option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="controlDeposit" align=left COLSPAN="5">
                                <a href='<spring:url value="/deposit/inputDeposit" > </spring:url>' class="buttonAdd">Add</a>
                                <a href="#" id="aUpdate"></a>
                                <a href="#" id="aDelete"></a>
                            </td>
                        </tr>
                        <tr>
                            <td COLSPAN="6">
                                <div class="formDeposit">
                                </div>
                            </td>
                        </tr>
                    </table>

                    <p/><b>List depositors for choose parameters:<b></br>
                    <table class="scrolling-table" border='1'>
                        <thead>
                            <th>&nbsp;</th>
                            <th>Depositor Name</th>
                            <th>Date of deposit</th>
                            <th>Amount</th>
                            <th>Amount plus</th>
                            <th>Amount minus</th>
                            <th>Date return</th>
                            <th>Mark return</th>
                        </thead>
                        <tbody>
                            <c:forEach items="${depositors}" var="depositor">
                                <tr>
                                    <td><input type="radio" id="deprId" name="depositorId" value="${depositor.depositorId}"></td>
                                    <td><c:out value="${depositor.depositorName}"/></td>
                                    <td>
                                        <fmt:formatDate value="${depositor.depositorDateDeposit}" type="both" pattern="yyyy-MM-dd"/>
                                    </td>
                                    <td><c:out value="${depositor.depositorAmountDeposit}"/></td>
                                    <td><c:out value="${depositor.depositorAmountPlusDeposit}"/></td>
                                    <td><c:out value="${depositor.depositorAmountMinusDeposit}"/></td>
                                    <td>
                                        <fmt:formatDate value="${depositor.depositorDateReturnDeposit}" type="both" pattern="yyyy-MM-dd"/>
                                    </td>
                                    <td><c:out value="${depositor.depositorMarkReturnDeposit}"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <table>
                        <tr>
                            <td class="addDep" align=middle><a id="aAddDep"></a></td>
                            <td class="updateDep" align=middle><a id="aUpdDep"></a></td>
                            <td class="deleteDep" align=middle><a id="aDelDep"></a></td>
                        </tr>
                    </table>
                </form>
            </div>
            <div id="tabs-2">
                <p><b>Deposit:</b></p>
                <input type="checkbox" name="idDeposit"/>Id:<input type="hidden" id="fId" name="depositId" value= "" size="15" />
                    <a href="#" id="aFindById"></a></br>
                <input type="checkbox" name="nameDeposit"/>Name:<input type="hidden" id="fName" name="depositName" value= "" size="15"/>
                    <a href="#" id="aFindByName"></a></br>
                <input type="checkbox" name="termDeposit"/>Term:<input type="hidden" id="fTerm" name="depositMinTerm" value= "" size="3"/>
                    <a href="#" id="aFindByTerm"></a></br>
                <input type="checkbox" name="amountDeposit"/>Amount:<input type="hidden" id="fAmount" name="depositMinAmount" value= "" size="15"/>
                    <a href="#" id="aFindByAmount"></a></br>
                <input type="checkbox" name="rateDeposit"/>Interest Rate:<input type="hidden" id="fRate" name="depositInterestRate" value= "" size="3"/>
                    <a href="#" id="aFindByRate"></a></br>
                <input type="checkbox" name="currencyDeposit"/>Currency:<input type="hidden" id="fCurrency" name="depositCurrency" value= "" size="3" />
                    <a href="#" id="aFindByCurrency"></a></br>
                <input type="checkbox" name="dateDeposit"/>Date deposit - start:<input type="hidden" id="fStartDateDeposit" name="startDateDeposit" required
                    pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size="10" class="datepicker"/>
                end:<input type="hidden" id="fEndDateDeposit" name="endDateDeposit" value= "" size="10" class="datepicker"/>
                    <a href="#" id="aFindByDateDeposit"></a></br>
                <input type="checkbox" name="dateReturnDeposit"/>Date return deposit - start:<input type="hidden" id="fStartDateReturnDeposit" name="startDateReturnDeposit" value= "" size="10" class="datepicker"/>
                end:<input type="hidden" id="fEndDateReturnDeposit" name="endDateReturnDeposit" required size="10" class="datepicker"/>
                    <a href="#" id="aFindByDateReturnDeposit"></a></br>
                <p><b>Depositor:</b></p>
                <input type="checkbox" name="idDepositor"/>Id:<input type="hidden" id="fIdD" name="depositorId" value= "" size="15" />
                    <a href="#" id="aFindByIdD"></a></br>
                <input type="checkbox" name="nameDepositor"/>Name:<input type="hidden" id="fNameD" name="depositorName" value= "" size="15"/>
                    <a href="#" id="aFindByNameD"></a></br>
                <input type="checkbox" name="amountDepositor"/>Amount - from:<input type="hidden" id="fFromAmount" name="fromAmountDepositor" value= "" size="15"/>
                to:<input type="hidden" id="fToAmount" name="toAmountDepositor" value= "" size="15"/>
                    <a href="#" id="aFindByAmountDepositor"></a></br>

                <input type="checkbox" name="markReturnDeposit"/>Mark return deposit:
                <input type="radio" name="markReturn" value='0' checked/>No
                <input type="radio" name="markReturn" value='1'/>Yes
                <a href="#" id="aFindByMarkReturnDepositor"></a></br>
                <a href="#" id="aFindByCriteria"></a>
            </div>
            <div id="tabs-3">
                <p>vzcxzxvvxzcvxzcvzz</p>
            </div>
        </div>

        <script src='<c:url value="/resources/js/jquery.min.js"/>'></script>
        <script src='<c:url value="/resources/js/jquery.maskedinput.js"/>'></script>
        <script src='<c:url value="/resources/js/bankDeposit.js"/>'></script>
        <script src='<c:url value="/resources/js/mainFrame.js"/>'></script>
        <script src='<c:url value="/resources/js/jquery-ui.js"/>'></script>
        <script src='<c:url value="/resources/js/date.format.js"/>'></script>
        <script>
            $('select').bind('click',function () {
                $('#dId').val($('select option:selected').val());
                $('#id').val($('select option:selected').val());
                $('legend').text('Deposit ( '+$('select option:selected').val()+' )');

                <c:forEach var="deposit" items="${deposits}">
                    if($('select option:selected').val() == "${deposit.depositId}"){
                        $('#name').val("${deposit.depositName}");
                        $('#term').val("${deposit.depositMinTerm}");
                        $('#amount').val("${deposit.depositMinAmount}");
                        $('#currency').val("${deposit.depositCurrency}");
                        $('#rate').val("${deposit.depositInterestRate}");
                        $('#conditions').val("${deposit.depositAddConditions}");
                        $('#count').val("${deposit.depositorCount}");
                        $('#sumA').val("${deposit.depositorAmountSum}");
                        $('#sumP').val("${deposit.depositorAmountPlusSum}");
                        $('#sumM').val("${deposit.depositorAmountMinusSum}");
                    }
                </c:forEach>

                var aUpdateHref='updateDeposit?depositId= '+$('#dId').val();
                var aDeleteHref='deleteDeposit?depositId= '+$('#dId').val();
                var aAddDepositorHref='../depositor/inputDepositor?idDeposit='+$('#dId').val();

                $('#aUpdate').text('Edit').attr('href',aUpdateHref).addClass('buttonUpd');
                $('#aDelete').text('Delete').attr('href',aDeleteHref).addClass('buttonDel');
                $('#aAddDep').text('Add').attr('href',aAddDepositorHref).addClass('buttonAdd');
            });
        </script>
    </body>
</html>
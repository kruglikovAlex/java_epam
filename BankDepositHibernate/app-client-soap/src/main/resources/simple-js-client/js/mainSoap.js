// The root URL for the RESTful services
var REST_URL = "http://localhost:8080/BankDeposit/soap/client/";

var currentDeposit;
var currentDepositor;

findAllDeposits();
findAllDepositors();

$(document).ready(function(){
    jQuery(function($){
            $("#MyDate1,#MyDate2,#MyDate3,#MyDate4,#depositorDateDeposit,#depositorDateReturnDeposit")
            .mask("9999-99-99",{completed:function(){}});
        });
        //alert("You typed the following: "+this.val());

    $('#depositForm').hide();
    $('#depositorForm').hide();
    $('#JsonRequest').hide();

    $('#btnResponseRaw').hide();
    $('#btnResponseJson').hide();
    $('#responseRaw').hide();
    $('#responseJson').hide();


});
// Register listeners
$('#btnClear').click(function () {
    newDeposit();
    newDepositor();
    return false;
});

$('#depositList').on('click', 'a', function () {
    var id= $(this).data('identity');
    newDeposit();
    //$('#depositorIdDeposit').val(id);
    findDepositById($(this).data('identity'), 'getBankDepositById');
});
$('#depositorList').on('click', 'a', function () {
    newDepositor();
    findDepositorById($(this).data('identity'));
});
$('#btnRequestRaw').click(function () {
    $('#depositForm').hide();
    $('#depositorForm').hide();
    $('#JsonRequest').show();
    return false;
});
$('#btnRequestForm').click(function () {
    $('#depositForm').show();
    $('#JsonRequest').hide();
    $('#depositorForm').hide();
    return false;
});
$('#btnRequestFormDepositor').click(function () {
    $('#depositorForm').show();
    $('#JsonRequest').hide();
    $('#depositForm').hide();
    return false;
});
$('#btnResponseRaw').click(function () {
    $('#responseRaw').show();
    $('#responseJson').hide();
    return false;
});
$('#btnResponseJson').click(function () {
    $('#responseRaw').hide();
    $('#responseJson').show();
    return false;
});
$('#btnSendMessage').click(function () {
    var method = Array('GET','POST');
    var s;

    for (i=0;i<$(":radio[name=httpMethod]").length; i++){
        if($(":radio[name=httpMethod]").get(i).checked)
            s=method[i];
    }
    if (s=="GET") sendMessage($('#URL').val(), s ," ","Get");
    else {
        if (s=="POST") {
            if ($('#JsonRequest').is(':visible'))
                sendMessage($('#URL').val(), s ,$("#JsonRequest").val(),"Create");
            else {
                if ($('#depositForm').is(':visible')){
                    if ($('#depositId').val() == ""){
                        sendMessage($('#URL').val(), 'addBankDeposit' ,formDepositToSOAP(),"Deposit create");
                    } else {
                        sendMessage($('#URL').val(), 'updateBankDeposit' ,formDepositToSOAP(),"Deposit update");
                    }
                }
                else {
                    if ($('#depositorForm').is(':visible')){
                       if ($('#depositorId').val() == ""){
                          sendMessage($('#URL').val(), 'addBankDepositor' ,formDepositorToSOAP(),"Depositor create");
                       } else {
                          sendMessage($('#URL').val(), 'updateBankDepositor' ,formDepositorToSOAP(),"Depositor update");
                       }
                    } else alert("Bad request");
                }
            }
        }
    }

    return false;
});


//Declaration function
function renderDepositList(data) {
// JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
    var list = data == null ? [] : (data instanceof Array ? data : [data]);
    $('#depositList td').remove();
    $.each(list, function (index, deposit) {
        $('#depositList').append('<tr><td><a href="#" data-identity="' + deposit.depositId + '">' +deposit.depositId+"</td><td>"+ deposit.depositName + "</td><td>"+ deposit.depositMinTerm + "</td><td>"+ deposit.depositMinAmount + "</td><td>"+ deposit.depositCurrency + "</td><td>"+ deposit.depositInterestRate + "</td><td>"+ deposit.depositAddConditions + '</a></td></tr>');
    });
}
function renderDepositorList(data) {
    var list = data == null ? [] : (data instanceof Array ? data : [data]);
    $('#depositorList td').remove();
    $.each(list, function (index, depositor) {
        $('#depositorList').append('<tr><td><a href="#" data-identity="' + depositor.depositorId + '">' +depositor.depositorId+"</td><td>"+ depositor.depositorName + "</td><td>"+ depositor.depositorDateDeposit + "</td><td>"+ depositor.depositorAmountDeposit + "</td><td>"+ depositor.depositorAmountPlusDeposit + "</td><td>"+ depositor.depositorAmountMinusDeposit + "</td><td>"+ depositor.depositorDateReturnDeposit + "</td><td>"+ depositor.depositorMarkReturnDeposit + '</a></td></tr>');
    });
}

function newDeposit() {
    currentDeposit = {};
    renderDepositDetails(currentDeposit); // Display empty form
}

function newDepositor() {
    currentDepositor = {};
    renderDepositorDetails(currentDepositor); // Display empty form
}

function renderDepositDetails(data) {
    var list = data == null ? [] : (data instanceof Array ? data : [data]);
    $.each(list, function(index,deposit){
        $('#depositId').val(deposit.depositId);
        $('#depositName').val(deposit.depositName);
        $('#depositMinTerm').val(deposit.depositMinTerm);
        $('#depositMinAmount').val(deposit.depositMinAmount);
        $('#depositCurrency').val(deposit.depositCurrency);
        $('#depositInterestRate').val(deposit.depositInterestRate);
        $('#depositAddConditions').val(deposit.depositAddConditions);
    });


    /*if (deposit.depositId == undefined) {
        $('#btnRemoveDdeposit').hide();
    } else {
        $('#btnRemoveDeposit').show();
    }*/
}

function renderDepositorDetails(data) {
    var list = data == null ? [] : (data instanceof Array ? data : [data]);
    $.each(list, function(index,depositor){
        $('#depositorId').val(depositor.depositorId);
        $('#depositorName').val(depositor.depositorName);
        $('#depositorIdDeposit').val(depositor.depositorIdDeposit);
        $('#depositorDateDeposit').val(depositor.depositorDateDeposit);
        $('#depositorAmountDeposit').val(depositor.depositorAmountDeposit);
        $('#depositorAmountPlusDeposit').val(depositor.depositorAmountPlusDeposit);
        $('#depositorAmountMinusDeposit').val(depositor.depositorAmountMinusDeposit);
        $('#depositorDateReturnDeposit').val(depositor.depositorDateReturnDeposit);
        $('#depositorMarkReturnDeposit').val(depositor.depositorMarkReturnDeposit);
    });
    /*if (depositor.depositorId == undefined) {
        $('#btnRemoveDdepositor').hide();
    } else {
        $('#btnRemoveDepositor').show();
    }*/
}

function sendMessage(url,method, data,log) {
    if ((url == '') | (method == '') | (data == '') | (log=='')){
        alert("Bad request");
    } else {
        send(url,method, data, log);
    }
}

function findDepositById(depositId,serviceMethod) {
    console.log('findDepositById: ' + depositId);
    $.soap({
        url: REST_URL,
        method: 'getBankDepositById',
        data: {
        		depositId: depositId
        	},
        data: function(SOAPObject) {					// function returning an instance of the SOAPObject class
            return new SOAPObject('soap:Envelope')
                    .addNamespace('soap', 'http://schemas.xmlsoap.org/soap/envelope/')
                	.newChild('soap:Body')
                	.newChild(serviceMethod)
                	.addParameter("depositId",depositId)
                	.end()
        },
        success: function (soapResponse) {
            console.log('findDepositById success: ' + soapResponse);

            $('#responseHeader').val('Status: '+soapResponse.status+
                                        "\nStatus code: "+soapResponse.httpCode+
                                        "\n\nHeaders: "+soapResponse.headers+
                                        "\nHttpText: "+soapResponse.httpText
                                    );
            $('#btnResponseRaw').show();
            $('#btnResponseJson').show();
            $('#responseJson').hide();
            $('#responseRaw').show();

            $('#responseJson').val(XML2jsobj(soapResponse.content.documentElement));

            $('#responseRaw').val(formatXml(soapResponse));

            var data = [];
            $(soapResponse.toXML()).find('BankDeposit').each(function(){
                var depId = $(this).find('depositId').text();
                data.push({
                    "depositId":depId,
                    "depositName":$(this).find('depositName').text(),
                    "depositMinTerm":$(this).find('depositMinTerm').text(),
                    "depositMinAmount":$(this).find('depositMinAmount').text(),
                    "depositCurrency":$(this).find('depositCurrency').text(),
                    "depositInterestRate":$(this).find('depositInterestRate').text(),
                    "depositAddConditions":$(this).find('depositAddConditions').text()
                });
                $('#depositId').val(depId);
                renderDepositList(data);
            });

            currentDeposit = data;
            renderDepositDetails(currentDeposit);
            renderDepositList(currentDeposit);
        },
        error: function(soapResponse) {
            console.log(soapResponse);
            alert('findDepositById: ' + soapResponse);
        }
    });
}
function findDepositorById(depositorId, serviceMethod) {
    console.log('findDepositorById: ' + depositorId);
    $.soap({
        url: REST_URL,
        method: 'getBankDepositorById',
        data: function(SOAPObject) {					// function returning an instance of the SOAPObject class
            return new SOAPObject('soap:Envelope')
                .addNamespace('soap', 'http://schemas.xmlsoap.org/soap/envelope/')
                .newChild('soap:Body')
                .newChild(serviceMethod)
                .addParameter("depositorId",depositorId)
                .end()
        },
        success: function (soapResponse) {
            console.log('findDepositorById success: ' + soapResponse);

            $('#responseHeader').val('Status: '+soapResponse.status+
                                        "\nStatus code: "+soapResponse.httpCode+
                                        "\n\nHeaders: "+soapResponse.headers+
                                        "\nHttpText: "+soapResponse.httpText
                                    );
            $('#btnResponseRaw').show();
            $('#btnResponseJson').show();
            $('#responseJson').hide();
            $('#responseRaw').show();

            $('#responseJson').val(XML2jsobj(soapResponse.content.documentElement));

            $('#responseRaw').val(formatXml(soapResponse));

            var data = [];
            $(soapResponse.toXML()).find('BankDepositor').each(function(){
                var depId = $(this).find('depositorId').text();
                data.push({
                    "depositorId":$(this).find('depositorId').text(),
                    "depositorName":$(this).find('depositorName').text(),
                    "depositorDateDeposit":$(this).find('depositorDateDeposit').text(),
                    "depositorAmountDeposit":$(this).find('depositorAmountDeposit').text(),
                    "depositorAmountPlusDeposit":$(this).find('depositorAmountPlusDeposit').text(),
                    "depositorAmountMinusDeposit":$(this).find('depositorAmountMinusDeposit').text(),
                    "depositorDateReturnDeposit":$(this).find('depositorDateReturnDeposit').text(),
                    "depositorMarkReturnDeposit":$(this).find('depositorMarkReturnDeposit').text()
                });
                $('#depositId').val(depId);
                renderDepositList(data);
            });
            currentDepositor = data;
            renderDepositorDetails(currentDepositor);
            renderDepositorList(currentDepositor);
        },
        error: function(soapResponse) {
            console.log(soapResponse);
            alert('findDepositorById: ' + soapResponse);
        }
    });
}

function findDepositByName(nameDep, serviceMethod) {
    console.log('findDepositByName: ' + nameDep);
    $.soap({
        url: REST_URL,
        method: 'getBankDepositByName',
        data: function(SOAPObject) {					// function returning an instance of the SOAPObject class
            return new SOAPObject('soap:Envelope')
                .addNamespace('soap', 'http://schemas.xmlsoap.org/soap/envelope/')
                .newChild('soap:Body')
                .newChild(serviceMethod)
                .addParameter("depositName",nameDep)
                .end()
        },
        success: function (soapResponse) {
            console.log('findByName success: ' + soapResponse);
            $('#responseHeader').val('Status: '+soapResponse.status+
                                        "\nStatus code: "+soapResponse.httpCode+
                                        "\n\nHeaders: "+soapResponse.headers+
                                        "\nHttpText: "+soapResponse.httpText
                                    );
            $('#btnResponseRaw').show();
            $('#btnResponseJson').show();
            $('#responseJson').hide();
            $('#responseRaw').show();

            $('#responseJson').val(XML2jsobj(soapResponse.content.documentElement));

            $('#responseRaw').val(formatXml(soapResponse));

            var data = [];
            $(soapResponse.toXML()).find('BankDeposit').each(function(){
                var depId = $(this).find('depositId').text();
                data.push({
                    "depositId":depId,
                    "depositName":$(this).find('depositName').text(),
                    "depositMinTerm":$(this).find('depositMinTerm').text(),
                    "depositMinAmount":$(this).find('depositMinAmount').text(),
                    "depositCurrency":$(this).find('depositCurrency').text(),
                    "depositInterestRate":$(this).find('depositInterestRate').text(),
                    "depositAddConditions":$(this).find('depositAddConditions').text()
                });
                $('#depositId').val(depId);
                renderDepositList(data);
            });
            currentDeposit = data;
            renderDepositDetails(currentDeposit);
            renderDepositList(currentDeposit);
        },
        error: function(soapResponse) {
            console.log(soapResponse);
            alert('findDepositByName: ' + soapResponse);
        }
    });
}

function findDepositorByName(nameDep, serviceMethod) {
    console.log('findDepositorByName: ' + nameDep);
    $.soap({
        url: REST_URL,
        method: 'getBankDepositorByName',
        data: function(SOAPObject) {					// function returning an instance of the SOAPObject class
            return new SOAPObject('soap:Envelope')
                .addNamespace('soap', 'http://schemas.xmlsoap.org/soap/envelope/')
                .newChild('soap:Body')
                .newChild(serviceMethod)
                .addParameter("depositorName",nameDep)
                .end()
        },
        success: function (soapResponse) {
            console.log('findByName success: ' + soapResponse);
            $('#responseHeader').val('Status: '+soapResponse.status+
                                        "\nStatus code: "+soapResponse.httpCode+
                                        "\n\nHeaders: "+soapResponse.headers+
                                        "\nHttpText: "+soapResponse.httpText
                                    );
            $('#btnResponseRaw').show();
            $('#btnResponseJson').show();
            $('#responseJson').hide();
            $('#responseRaw').show();

            $('#responseJson').val(XML2jsobj(soapResponse.content.documentElement));

            $('#responseRaw').val(formatXml(soapResponse));

            var data = [];
            $(soapResponse.toXML()).find('BankDepositor').each(function(){
                var depId = $(this).find('depositorId').text();
                data.push({
                    "depositorId":$(this).find('depositorId').text(),
                    "depositorName":$(this).find('depositorName').text(),
                    "depositorDateDeposit":$(this).find('depositorDateDeposit').text(),
                    "depositorAmountDeposit":$(this).find('depositorAmountDeposit').text(),
                    "depositorAmountPlusDeposit":$(this).find('depositorAmountPlusDeposit').text(),
                    "depositorAmountMinusDeposit":$(this).find('depositorAmountMinusDeposit').text(),
                    "depositorDateReturnDeposit":$(this).find('depositorDateReturnDeposit').text(),
                    "depositorMarkReturnDeposit":$(this).find('depositorMarkReturnDeposit').text()
                });
                $('#depositId').val(depId);
                renderDepositList(data);
            });
            currentDepositor = data;
            renderDepositorDetails(currentDepositor);
            renderDepositorList(currentDepositor);
        },
        error: function(soapResponse) {
            console.log(soapResponse);
            alert('findByName: ' + soapResponse);
        }
    });
}

function findAllDeposits() {
    console.log('findAllDeposits');
    $.soap({
        url: REST_URL,
        method: 'getBankDeposits',
        data: {},
        success: function (soapResponse) {
            alert('findAllDeposits successfully');
            $('#responseHeader').val('findAllDeposits'+ ':\nStatus: '+soapResponse.status+
                                    "\nStatus code: "+soapResponse.httpCode+
                                    "\n\nHeaders: "+soapResponse.headers+
                                    "\nHttpText: "+soapResponse.httpText
                                    );
            $('#btnResponseRaw').show();
            $('#btnResponseJson').show();
            $('#responseJson').hide();
            $('#responseRaw').show();

            // parse XML
            var data = [];
            $(soapResponse.toXML()).find('BankDeposit').each(function(){
                data.push({
                    "depositId":$(this).find('depositId').text(),
                    "depositName":$(this).find('depositName').text(),
                    "depositMinTerm":$(this).find('depositMinTerm').text(),
                    "depositMinAmount":$(this).find('depositMinAmount').text(),
                    "depositCurrency":$(this).find('depositCurrency').text(),
                    "depositInterestRate":$(this).find('depositInterestRate').text(),
                    "depositAddConditions":$(this).find('depositAddConditions').text()
                });
                renderDepositList(data);
            });

            $('#responseRaw').val(formatXml(soapResponse));
        },
        error: function(soapResponse) {
            //console.log(soapResponse);
            alert('findAllDeposits: ' + soapResponse);
        }
    });
}

function findAllDepositors() {
    console.log('findAllDepositors');
    $.soap({
            url: REST_URL,
            method: 'getBankDepositors',
            data: {},
            success: function (soapResponse) {
                alert('findAllDepositors successfully');
                $('#responseHeader').val('findAllDepositors'+ ':\nStatus: '+soapResponse.status+
                                        "\nStatus code: "+soapResponse.httpCode+
                                        "\n\nHeaders: "+soapResponse.headers+
                                        "\nHttpText: "+soapResponse.httpText
                                        );
                $('#btnResponseRaw').show();
                $('#btnResponseJson').show();
                $('#responseJson').hide();
                $('#responseRaw').show();

                // parse XML
                var data = [];
                $(soapResponse.toXML()).find('BankDepositor').each(function(){
                    data.push({
                        "depositorId":$(this).find('depositorId').text(),
                        "depositorName":$(this).find('depositorName').text(),
                        "depositorDateDeposit":$(this).find('depositorDateDeposit').text(),
                        "depositorAmountDeposit":$(this).find('depositorAmountDeposit').text(),
                        "depositorAmountPlusDeposit":$(this).find('depositorAmountPlusDeposit').text(),
                        "depositorAmountMinusDeposit":$(this).find('depositorAmountMinusDeposit').text(),
                        "depositorDateReturnDeposit":$(this).find('depositorDateReturnDeposit').text(),
                        "depositorMarkReturnDeposit":$(this).find('depositorMarkReturnDeposit').text()
                    });
                    renderDepositorList(data);
                });
                $('#responseRaw').val($('#responseRaw').val()+formatXml(soapResponse));
            }
        });
    }

function addDeposit() {
    console.log('addDeposit');
    $.soap({
        url: REST_URL,
        method: 'addBankDeposit',
        data: formDepositToSOAP,
        success: function (soapResponse) {
            alert('Deposit created successfully: '+soapResponse);
            //$('#depositId').val(data.depositId);
            findAllDeposits();
        },
        error: function (soapResponse) {
            alert('addDeposit error: ' + soapResponse);
        }
    });
}

function addDepositor() {
    console.log('addDepositor');
    $.soap({
        url: REST_URL,
        method: 'addBankDepositor',
        data: formDepositorToSOAP,
        success: function (soapResponse) {
            alert('Depositor created successfully: '+soapResponse);
            //$('#depositorId').val(data.depositorId);
            findAllDepositors();
        },
        error: function (soapResponse) {
            alert('addDepositor error: ' + soapResponse);
        }
    });
}
function updateDeposit() {
    console.log('updateDeposit');
    $.soap({
        url: REST_URL,
        method: 'updateBankDeposit',
        data: formDepositToSOAP,
        success: function (soapResponse) {
            alert('Deposit updated successfully: '+soapResponse);
            findAllDeposits();
        },
        error: function (soapResponse) {
            alert('updateDeposit error: ' + soapResponse);
        }
    });
}
function updateDepositor() {
    console.log('updateDepositor');
    $.soap({
        url: REST_URL,
        method:'updateBankDepositor',
        data: formDepositorToSOAP,
        success: function (soapResponse) {
            alert('Depositor updated successfully: '+soapResponse);
            findAllDepositors();
        },
        error: function (soapResponse) {
            alert('updateDepositor error: ' + soapResponse);
        }
    });
}

function removeDeposit(depId) {
    console.log('removeDeposit');
    $.soap({
        url: REST_URL,
        method: 'deleteBankDeposit',
        data:function(SOAPObject) {					// function returning an instance of the SOAPObject class
                return new SOAPObject('soap:Envelope')
                    .addNamespace('soap', 'http://schemas.xmlsoap.org/soap/envelope/')
                    .newChild('soap:Body')
                    .newChild('removeDeposit')
                    .addParameter("depositId",depId)
                    .end()
        },
        success: function (soapResponse) {
            alert('Deposit removed successfully: '+soapResponse);
            findAllDeposits();
            currentDeposit = {};
            renderDepositDetails(currentDeposit);
        },
        error: function (soapResponse) {
            alert('removeDeposit error: ' + soapResponse);
        }
    });
}

function removeDepositor() {
    console.log('removeDepositor');
    $.soap({
        url: REST_URL,
        method: 'removeBankDepositor',
        data:function(SOAPObject) {					// function returning an instance of the SOAPObject class
                return new SOAPObject('soap:Envelope')
                    .addNamespace('soap', 'http://schemas.xmlsoap.org/soap/envelope/')
                    .newChild('soap:Body')
                    .newChild('removeDepositor')
                    .addParameter("depositorId",depId)
                    .end()
        },
        success: function (soapResponse) {
            alert('Depositor removed successfully: '+soapResponse);
            findAllDepositors();
            currentDepositor = {};
            renderDepositorDetails(currentDepositor);
        },
        error: function (soapResponse) {
            alert('removeDepositor error: ' + soapResponse);
        }
    });
}


function send(url,serviceMethod,dataSOAP,log) {
    console.log(log);
    $.soap({
        url: REST_URL,
        method: serviceMethod,
        /*data: {
            depositId: depId == "" ? null : depId,
            depositName: $('#depositName').val(),
            depositMinTerm: parseInt($('#depositMinTerm').val()),
            depositMinAmount: parseInt($('#depositMinAmount').val()),
            depositCurrency: $('#depositCurrency').val(),
            depositInterestRate: parseInt($('#depositInterestRate').val()),
            depositAddConditions: $('#depositAddConditions').val()
        },*/
        /*data: function(SOAPObject) {					// function returning an instance of the SOAPObject class
        		return new SOAPObject('soap:Envelope')
        			.addNamespace('soap', 'http://schemas.xmlsoap.org/soap/envelope/')
        			.newChild('soap:Body')
        			.newChild(serviceMethod)
        			.addParameter("depositId",depId == "" ? null : depId)
        			.addParameter("depositName", $('#depositName').val())
        			.addParameter("depositMinTerm", parseInt($('#depositMinTerm').val()))
        			.addParameter("depositMinAmount", parseInt($('#depositMinAmount').val()))
        			.addParameter("depositCurrency", $('#depositCurrency').val())
        			.addParameter("depositInterestRate", parseInt($('#depositInterestRate').val()))
        			.addParameter("depositAddConditions", $('#depositAddConditions').val())
        			.end()
        },*/
        data: dataSOAP, //formDepositToSOAP(serviceMethod),
        /*<soap:Envelope
        	xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
        	<soap:Body>
        		<helloWorld>
        			<name>Remy Blom</name>
        			<msg>Hi!</msg>
        		</helloWorld>
        	</soap:Body>
        </soap:Envelope>*/

        success: function (soapResponse) {
            alert(log+' successfully');
            $('#responseHeader').val(log+ ':\nStatus: '+soapResponse.status+
                                    "\nStatus code: "+soapResponse.httpCode+
                                    "\n\nHeaders: "+soapResponse.headers+
                                    "\nHttpText: "+soapResponse.httpText
                                    );
            $('#btnResponseRaw').show();
            $('#btnResponseJson').show();
            $('#responseJson').hide();
            $('#responseRaw').show();

            $('#responseJson').val(XML2jsobj(soapResponse.content.documentElement));

            $('#responseRaw').val(formatXml(soapResponse));

            $(soapResponse.toXML()).find('BankDeposit').each(function(){
                var depId = $(this).find('depositId').text();
                var data = [];
                data.push({
                    "depositId":depId,
                    "depositName":$(this).find('depositName').text(),
                    "depositMinTerm":$(this).find('depositMinTerm').text(),
                    "depositMinAmount":$(this).find('depositMinAmount').text(),
                    "depositCurrency":$(this).find('depositCurrency').text(),
                    "depositInterestRate":$(this).find('depositInterestRate').text(),
                    "depositAddConditions":$(this).find('depositAddConditions').text()
                });
                $('#depositId').val(depId);
                renderDepositList(data);
            });
            $(soapResponse.toXML()).find('BankDepositor').each(function(){
                var depId = $(this).find('depositorId').text();
                var data = [];
                data.push({
                    "depositorId":$(this).find('depositorId').text(),
                    "depositorName":$(this).find('depositorName').text(),
                    "depositorDateDeposit":$(this).find('depositorDateDeposit').text(),
                    "depositorAmountDeposit":$(this).find('depositorAmountDeposit').text(),
                    "depositorAmountPlusDeposit":$(this).find('depositorAmountPlusDeposit').text(),
                    "depositorAmountMinusDeposit":$(this).find('depositorAmountMinusDeposit').text(),
                    "depositorDateReturnDeposit":$(this).find('depositorDateReturnDeposit').text(),
                    "depositorMarkReturnDeposit":$(this).find('depositorMarkReturnDeposit').text()
                });
                $('#depositorId').val(depId);
                renderDepositorList(data);
            });
        },
        error: function (soapResponse) {
            alert(log+' error');
            $('#responseHeader').val(log+ ':\nStatus: '+soapResponse.status+
                    "\nStatus code: "+soapResponse.httpCode+
                    "\n\nHeaders: \n"+soapResponse.headers+
                    "\nHttpText: "+soapResponse.httpText);
            $('#btnResponseRaw').show();
            $('#btnResponseJson').show();
            $('#responseRaw').show();
            $('#responseRaw').val(soapResponse.content.toString());
        }
    });
}


// Helper function to serialize all the form fields into a JSON string
function formDepositToJSON() {
    var depositId = $('#depositId').val();
    return JSON.stringify({
        "depositId": depositId == "" ? null : depositId,
        "depositName": $('#depositName').val(),
        "depositMinTerm": parseInt($('#depositMinTerm').val()),
        "depositMinAmount": parseInt($('#depositMinAmount').val()),
        "depositCurrency": $('#depositCurrency').val(),
        "depositInterestRate": parseInt($('#depositInterestRate').val()),
        "depositAddConditions": $('#depositAddConditions').val()
    });
}

function formDepositorToJSON() {
    Date.prototype.format = function (mask, utc) {
        return dateFormat(this, mask, utc);
    };
    var depositorId = $('#depositorId').val();
    var stDate = new Date($('#depositorDateDeposit').val());
    var endDate = new Date($('#depositorDateReturnDeposit').val());
    return JSON.stringify({
         "depositorId":depositorId == "" ? null : depositorId,
         "depositorName":$('#depositorName').val(),
         "depositorDateDeposit":stDate.format("mmm dd, yyyy HH:MM:ss TT"),              //"Jan 1, 2014 12:00:00 AM",
         "depositorAmountDeposit":parseInt($('#depositorAmountDeposit').val()),
         "depositorAmountPlusDeposit":parseInt($('#depositorAmountPlusDeposit').val()),
         "depositorAmountMinusDeposit":parseInt($('#depositorAmountMinusDeposit').val()),
         "depositorDateReturnDeposit":endDate.format("mmm dd, yyyy HH:MM:ss TT"),
         "depositorMarkReturnDeposit":parseInt($('#depositorMarkReturnDeposit').val())
    });
}

// Helper function to serialize all the form fields into a SOAP string
function formDepositToSOAP(method) {
    var depId = $('#depositId').val();
    var s = function(SOAPObject) {					// function returning an instance of the SOAPObject class
        return new SOAPObject('soap:Envelope')
            .addNamespace('soap', 'http://schemas.xmlsoap.org/soap/envelope/')
            .newChild('soap:Body')
            .newChild(method)
            .addParameter("depositId",depId == "" ? null : depId)
            .addParameter("depositName", $('#depositName').val())
            .addParameter("depositMinTerm", parseInt($('#depositMinTerm').val()))
            .addParameter("depositMinAmount", parseInt($('#depositMinAmount').val()))
            .addParameter("depositCurrency", $('#depositCurrency').val())
            .addParameter("depositInterestRate", parseInt($('#depositInterestRate').val()))
            .addParameter("depositAddConditions", $('#depositAddConditions').val())
            .end()
        };
    return s;
}

function formDepositorToSOAP(method) {
    Date.prototype.format = function (mask, utc) {
        return dateFormat(this, mask, utc);
    };
    var depositorId = $('#depositorId').val();
    var stDate = new Date($('#depositorDateDeposit').val());
    var endDate = new Date($('#depositorDateReturnDeposit').val());
    var s = function(SOAPObject) {
        return new SOAPObject('soap:Envelope')
           .addNamespace('soap', 'http://schemas.xmlsoap.org/soap/envelope/')
           .newChild('soap:Body')
           .newChild(method)
           .addParameter("depositorId",depositorId == "" ? null : depositorId)
           .addParameter("depositorName", $('#depositorName').val())
           .addParameter("depositorDateDeposit", $('#depositorDateDeposit').val())//stDate.format("mmm dd, yyyy HH:MM:ss TT"))
           .addParameter("depositorAmountDeposit", parseInt($('#depositorAmountDeposit').val()))
           .addParameter("depositorAmountPlusDeposit", parseInt($('#depositorAmountPlusDeposit').val()))
           .addParameter("depositorAmountMinusDeposit", parseInt($('#depositorAmountMinusDeposit').val()))
           .addParameter("depositorDateReturnDeposit", $('#depositorDateReturnDeposit').val())//endDate.format("mmm dd, yyyy HH:MM:ss TT"))
           .addParameter("depositorMarkReturnDeposit", parseInt($('#depositorMarkReturnDeposit').val()))
           .end()
        };
    return s;
}

function fromSoapXmlToText(soapResponse){
    var text = "<?xml version = \"1.0\" encoding=\"UTF-8\"?>\n";
    $(soapResponse.toXML()).find('BankDeposit').each(function(){
        text += "<BankDeposit>\n"+
                "\t<depositId>"+$(this).find('depositId').text()+"</depositId>\n"+
                "\t<depositName>"+$(this).find('depositName').text()+"</depositName>\n"+
                "\t<depositMinTerm>"+$(this).find('depositMinTerm').text()+"</depositMinTerm>\n"+
                "\t<depositMinAmount>"+$(this).find('depositMinAmount').text()+"</depositMinAmount>\n"+
                "\t<depositCurrency>"+$(this).find('depositCurrency').text()+"</depositCurrency>\n"+
                "\t<depositInterestRate>"+$(this).find('depositInterestRate').text()+"</depositInterestRate>\n"+
                "\t<depositAddConditions>"+$(this).find('depositAddConditions').text()+"</depositAddConditions>\n"+
                "</BankDeposit>\n"
    });
    return text;
}

function formatXml(xml) {
      var formatted = '';
      var reg = /(>)(<)(\/*)/g;
      xml = xml.toString().replace(reg, '$1\r\n$2$3');
      var pad = 0;
      jQuery.each(xml.split('\r\n'), function(index, node){
         var indent = 0;
         if (node.match( /.+<\/\w[^>]*>$/ )){
            indent = 0;
         }else if (node.match( /^<\/\w/ )){
            if (pad != 0){
               pad -= 1;
            }
         }else if (node.match( /^<\w[^>]*[^\/]>.*$/ )){
            indent = 1;
         }else{
            indent = 0;
         }
         var padding = '';
         for (var i = 0; i < pad; i++){
            padding += '  ';
         }
         formatted += padding + node + '\r\n';
         pad += indent;
      });

      return formatted;
}
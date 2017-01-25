// The root URL for the RESTful services
var REST_URL = "";

var currentDeposit;
var currentDepositor;

$(function () {
    $('#myTab a:last').tab('show');
})

$(function() {
    $( ".datepicker").datepicker({
        appendText:"(yyyy-mm-dd)",
        dateFormat:"yy-mm-dd",
        changeMonth:true,
        changeYear:true
    });
});

$(function() {
    $( "#accordionMy" ).accordion({
        heightStyle: "content"
    });
});

$( function() {
      var dialog, form,
          hostURL = $( "#hostURL" ),
          allFields = $( [] ).add( hostURL );

      function addHostUrl() {
            goString = hostURL.val();
            $('#HOST').val(goString).prop( "disabled", true );
            REST_URL = goString;
            dialog.dialog( "close" );
      }

      dialog = $( "#dialog-form" ).dialog({
        autoOpen: false,
        height: 200,
        width: 750,
        modal: true,
        buttons: {
            "GET": addHostUrl,
            Cancel: function() {
            dialog.dialog( "close" );
          }
        },
        close: function() {
          form[ 0 ].reset();
          allFields.removeClass( "ui-state-error" );
        }
      });

      form = dialog.find( "form" ).on( "submit", function( event ) {
        event.preventDefault();
        addHostUrl();
      });

      $( "#get-Host" ).button().on( "click", function() {
        dialog.dialog( "open" );
      });
  });

// Register listeners
$('#btnClear').click(function () {
    newDeposit();
    newDepositor();
    return false;
});

$('#depositList').on('click', 'a', function () {
    var id= $(this).data('identity');
    newDepositor();
    $('#depositorIdDeposit').val(id);
    findDepositById($(this).data('identity'));
});
$('#depositorList').on('click', 'a', function () {
    findDepositorById($(this).data('identity'));
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
    var method = Array('GET','PUT','POST','DELETE');
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
                if ($('#depositForm').is(':visible'))
                    sendMessage($('#URL').val(), s ,formDepositToJSON(),"Deposit create");
                else {
                    if ($('#depositorForm').is(':visible'))
                        sendMessage($('#URL').val(), s ,formDepositorToJSON(),"Depositor create");
                    else alert("Bad request");
                }
            }
        }
        else {
            if(s=="DELETE"){

                sendMessage($('#URL').val(), s ," ","Remove");
            }
            else {
                if ($('#depositForm').is(':visible'))
                    sendMessage($('#URL').val(), s ,formDepositToJSON(),"Deposit updated");
                else {
                    if ($('#JsonRequest').is(':visible'))
                       sendMessage($('#URL').val(), s ,$("#JsonRequest").val(),"Update");
                    else {
                        if ($('#depositorForm').is(':visible'))
                            sendMessage($('#URL').val(), s ,formDepositorToJSON(),"Depositor updated");
                    }
                }
            }
        }
    }
    return false;
});

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

function renderDepositDetails(deposit) {
    $('#depositId').val(deposit.depositId);
    $('#depositName').val(deposit.depositName);
    $('#depositMinTerm').val(deposit.depositMinTerm);
    $('#depositMinAmount').val(deposit.depositMinAmount);
    $('#depositCurrency').val(deposit.depositCurrency);
    $('#depositInterestRate').val(deposit.depositInterestRate);
    $('#depositAddConditions').val(deposit.depositAddConditions);
}

function renderDepositorDetails(depositor) {
    $('#depositorId').val(depositor.depositorId);
    $('#depositorName').val(depositor.depositorName);
    $('#depositorIdDeposit').val(depositor.depositorIdDeposit);
    $('#depositorDateDeposit').val(depositor.depositorDateDeposit);
    $('#depositorAmountDeposit').val(depositor.depositorAmountDeposit);
    $('#depositorAmountPlusDeposit').val(depositor.depositorAmountPlusDeposit);
    $('#depositorAmountMinusDeposit').val(depositor.depositorAmountMinusDeposit);
    $('#depositorDateReturnDeposit').val(depositor.depositorDateReturnDeposit);
    $('#depositorMarkReturnDeposit').val(depositor.depositorMarkReturnDeposit);
}

function sendMessage(url,method, data,log) {
    if ((url == '') | (method == '') | (data == '') | (log=='')){
        alert("Bad request");
    } else {
        send(url,method, data, log);
    }
}

function findAllDeposits() {
    console.log('findAllDeposits');
    $.ajax({
        type: 'GET',
        url: REST_URL+"deposit/all",
        success: function (data, textStatus, jqXHR) {
                    alert(log+' successfully');
                    $('#responseHeader').val("Status:\n "+textStatus+", Code: "+jqXHR.status+" OK"+"\n"+
                                             "\nRequest headers:"+"\n"+
                                             " User-Agent: BankDepositHibernate 1.0.0"+"\n"+
                                             " Http request: "+method+"\n"+
                                             " Data type: json"+"\n"+
                                             "\nResponse headers:" +"\n"+
                                             " Server: "+jqXHR.getResponseHeader("Server")+
                                             "\n Content-Type: "+jqXHR.getResponseHeader("Content-Type")+
                                             "\n Transfer-Encoding: "+jqXHR.getResponseHeader("Transfer-Encoding")+
                                             "\n Date: "+jqXHR.getResponseHeader("Date")
                                             );
                    $('#btnResponseRaw').show();
                    $('#btnResponseJson').show();
                    $('#responseJson').show();
                    $('#responseRaw').hide();
                    $('#responseRaw').val(jqXHR.responseText);
                    $('#responseJson').val(JSON.stringify(jqXHR.responseJSON,"/"," "));
                    renderDepositList;
                    findAllDeposits();
                    findAllDepositors();
                },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(jqXHR, textStatus, errorThrown);
            alert('findAllDeposits: ' + textStatus);
        }
    });
}

function findAllDepositors() {
    console.log('findAllDepositors');
    $.ajax({
        type: 'GET',
        url: REST_URL+"depositor/all",
        success: renderDepositorList,
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(jqXHR, textStatus, errorThrown);
            alert('findAllDepositors: ' + textStatus);
        }
    });
}

function findDepositById(depositId) {
    console.log('findDepositById: ' + depositId);
    $.ajax({
        type: 'GET',
        url: REST_URL + 'deposit/id/' + depositId,
        //dataType: "json",
        success: function (data) {
            console.log('findDepositById success: ' + data.depositId);
            currentDeposit = data;
            renderDepositDetails(currentDeposit);
            renderDepositList(currentDeposit);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(jqXHR, textStatus, errorThrown);
            alert('findDepositById: ' + textStatus);
        }
    });
}
function findDepositorById(depositorId) {
    console.log('findDepositorById: ' + depositorId);
    $.ajax({
        type: 'GET',
        url: REST_URL + 'depositor/id/' + depositorId,
        dataType: "json",
        success: function (data) {
            console.log('findDepositorById success: ' + data.depositorId);
            currentDepositor = data;
            renderDepositorDetails(currentDepositor);
            renderDepositorList(currentDepositor);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(jqXHR, textStatus, errorThrown);
            alert('findDepositorById: ' + textStatus);
        }
    });
}

function send(url,method,dataJson,log) {
    console.log(log);
    var res = $.ajax({
        url: REST_URL+url,
        type: method,
        data: dataJson,
        dataType: "json",
        contentType: "application/json;charset=UTF-8",
        beforeSend: function(jqXHR){
            if (method != "DELETE"){
                jqXHR.setRequestHeader("Accept","application/json");
                jqXHR.setRequestHeader("Origin","http://localhost:8080");
                }
        },
        success: function (data, textStatus, jqXHR) {
            alert(log+" - "+jqXHR.status);
            $('#accordionMy').accordion('option', 'active', 1);
            $('#responseHeader').val("Status:\n "+textStatus+", Code: "+jqXHR.status+" OK"+"\n"+
                                     "\nRequest headers:"+"\n"+
                                     " User-Agent: BankDepositHibernate 1.0.0"+"\n"+
                                     " Http request: "+method+"\n"+
                                     " Data type: json"+"\n"+
                                     "\nResponse headers:" +"\n"+
                                     " Server: "+jqXHR.getResponseHeader("Server")+
                                     "\n Content-Type: "+jqXHR.getResponseHeader("Content-Type")+
                                     "\n Transfer-Encoding: "+jqXHR.getResponseHeader("Transfer-Encoding")+
                                     "\n Date: "+jqXHR.getResponseHeader("Date")
                                     );
            $('#btnResponseRaw').show();
            $('#btnResponseJson').show();
            $('#responseJson').show();
            $('#responseRaw').hide();
            $('#responseRaw').val(jqXHR.responseText);
            $('#responseJson').val(JSON.stringify(jqXHR.responseJSON,"/"," "));
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(log+" - "+jqXHR.status+"\n"+errorThrown);
            $('#accordionMy').accordion('option', 'active', 1);
            $('#responseHeader').val("Status:\n "+textStatus+", Code: "+jqXHR.status+" OK"+"\n"+
                                     "\nRequest headers:"+"\n"+
                                     " User-Agent: BankDepositHibernate 1.0.0"+"\n"+
                                     " Http request: "+method+"\n"+
                                     " Data type: json"+"\n"+
                                     "\nResponse headers:" +"\n"+
                                     " Server: "+jqXHR.getResponseHeader("Server")+
                                     "\n Content-Type: "+jqXHR.getResponseHeader("Content-Type")+
                                     "\n Transfer-Encoding: "+jqXHR.getResponseHeader("Transfer-Encoding")+
                                     "\n Date: "+jqXHR.getResponseHeader("Date")
                                     );
            $('#btnResponseRaw').show();
            $('#btnResponseJson').show();
            $('#responseJson').show();
            $('#responseRaw').hide();
            $('#responseRaw').val(jqXHR.responseText);
            $('#responseJson').val(JSON.stringify(jqXHR.responseJSON,"/"," "));
        }
    });
    return res;
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

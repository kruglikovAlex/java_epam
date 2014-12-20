// The root URL for the RESTful services
//var REST_URL = "http://localhost:8080/deposits";
var REST_URL = "http://localhost:8080";

var currentDeposit;
var currentDepositor;

findAllDeposits();
findAllDepositors();
$(document).ready(function(){
    jQuery(function($){
            $("#MyDate1,#MyDate2,#MyDate3,#MyDate4,#depositorDateDeposit,#depositorDateReturnDeposit")
            .mask("9999-99-99",{completed:function(){alert("You typed the following: "+this.val());}});
        });
});
// Register listeners
$('#btnSearchByIdDeposit').click(function () {
    searchByIdDeposit($('#searchKeyIdDeposit').val());
    return false;
});

$('#btnSearchByNameDeposit').click(function () {
    searchByNameDeposit($('#searchKeyNameDeposit').val());
    return false;
});

$('#btnSearchByIdDepositor').click(function () {
    searchByIdDepositor($('#searchKeyIdDepositor').val());
    return false;
});

$('#btnSearchByIdDepositDepositor').click(function () {
    searchByIdDepositDepositor($('#searchKeyIdDepositDepositor').val());
    return false;
});

$('#btnSearchByNameDepositor').click(function () {
    searchByNameDepositor($('#searchKeyNameDepositor').val());
    return false;
});

$('#btnSearchBetweenDateDepositor').click(function () {
    SearchBetweenDateDepositor($('#MyDate1').val(),$('#MyDate2').val());
    return false;
});

$('#btnSearchBetweenDateReturnDepositor').click(function () {
    SearchBetweenDateReturnDepositor($('#MyDate3').val(),$('#MyDate4').val());
    return false;
});

$('#btnAddDeposit').click(function () {
    newDeposit();
    return false;
});

$('#btnAddDepositor').click(function () {
    newDepositor();
    return false;
});

$('#btnSaveDeposit').click(function () {
    if ($('#depositId').val() == '')
        addDeposit();
    else
        updateDeposit();
    return false;
});
$('#btnSaveDepositor').click(function () {
    if ($('#depositorId').val() == '')
        {
        if ($('#depositorIdDeposit').val() == '')
            {
                alert('Deposit Id is empty: ');
            }
        else addDepositor();
        }
    else
        updateDepositor();
    return false;
});

$('#btnRemoveDeposit').click(function () {
    removeDeposit();
    return false;
});
$('#btnRemoveDepositor').click(function () {
    removeDepositor();
    return false;
});
$('#btnRemoveDepositor').click(function () {
    removeDepositor();
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



//Declaration function
function addDeposit() {
    console.log('addDeposit');
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: REST_URL+'/deposits/',
        dataType: "json",
        data: formDepositToJSON(),
        success: function (data, textStatus, jqXHR) {
            alert('Deposit created successfully');
            $('#depositId').val(data.depositId);
            //findAllDeposits();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert('addDeposit error: ' + textStatus);
        }
    });
}

function addDepositor() {
    console.log('addDepositor');
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: REST_URL+'/depositors/',
        data: formDepositorToJSON(),
        success: function (data, textStatus, jqXHR) {
            alert('Depositor created successfully');
            $('#depositorId').val(data.depositorId);
            //findAllDepositors();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert('addDepositor error: ' + textStatus);
        }
    });
}
function updateDeposit() {
    console.log('updateDeposit');
    $.ajax({
        type: 'PUT',
        contentType: 'application/json',
        url: REST_URL+'/deposits/',
        data: formDepositToJSON(),
        success: function (data, textStatus, jqXHR) {
            alert('Deposit updated successfully');
            //findAllDeposits();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert('updateDeposit error: ' + textStatus);
        }
    });
}
function updateDepositor() {
    console.log('updateDepositor');
    $.ajax({
        type: 'PUT',
        contentType: 'application/json',
        url: REST_URL+'/depositors/',
        data: formDepositorToJSON(),
        success: function (data, textStatus, jqXHR) {
            alert('Depositor updated successfully');
            //findAllDepositors();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert('updateDepositor error: ' + textStatus);
        }
    });
}

function removeDeposit() {
    console.log('removeDeposit');
    $.ajax({
        type: 'DELETE',
        url: REST_URL + '/deposits/' + $('#depositId').val(),
        success: function (data, textStatus, jqXHR) {
            alert('Deposit removed successfully');
            findAllDeposits();
            currentDeposit = {};
            renderDepositDetails(currentDeposit);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert('removeDeposit error: ' + textStatus);
        }
    });
}

function removeDepositor() {
    console.log('removeDepositor');
    $.ajax({
        type: 'DELETE',
        url: REST_URL + '/depositors/' + $('#depositorId').val(),
        success: function (data, textStatus, jqXHR) {
            alert('Depositor removed successfully');
            findAllDepositors();
            currentDepositor = {};
            renderDepositorDetails(currentDepositor);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert('removeDepositor error: ' + textStatus);
        }
    });
}

function findAllDeposits() {
    console.log('findAllDeposits');
    $.ajax({
        type: 'GET',
        url: REST_URL+"/deposits",
        dataType: "json", // data type of response
        success: renderDepositList,
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
        url: REST_URL+"/depositors",
        dataType: "json", // data type of response
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
        url: REST_URL + '/deposits/' + depositId,
        dataType: "json",
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
        url: REST_URL + '/depositors/' + depositorId,
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

function findDepositorByIdDeposit(depositorIdDeposit) {
    console.log('findDepositorByIdDeposit: ' + depositorIdDeposit);
    $.ajax({
        type: 'GET',
        url: REST_URL + '/depositors/deposit/' + depositorIdDeposit,
        dataType: "json",
        success: function (data) {
            console.log('findDepositorByIdDeposit success: ' + data.depositorIdDeposit);
            currentDepositor = data;
            renderDepositorDetails(currentDepositor);
            renderDepositorList(currentDepositor);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(jqXHR, textStatus, errorThrown);
            alert('findDepositorByIdDeposit: ' + textStatus);
        }
    });
}

function findDepositorBetweenDateDeposit(start,end) {
    console.log('findDepositorBetweenDateDeposit: ' + start+' '+end);
    $.ajax({
        type: 'GET',
        url: REST_URL + '/depositors/date/' + start+"/"+end,
        dataType: "json",
        success: function (data) {
            console.log('findDepositorBetweenDateDeposit success: from' + start+' to '+end);
            currentDepositor = data;
            renderDepositorDetails(currentDepositor);
            renderDepositorList(currentDepositor);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(jqXHR, textStatus, errorThrown);
            alert('findDepositorBetweenDateDeposit: ' + textStatus);
        }
    });
}

function findDepositorBetweenDateReturnDeposit(start,end) {
    console.log('findDepositorBetweenDateReturnDeposit: ' + start+' '+end);
    $.ajax({
        type: 'GET',
        url: REST_URL + '/depositors/date/return/' + start+"/"+end,
        dataType: "json",
        success: function (data) {
            console.log('findDepositorBetweenDateReturnDeposit success: from' + start+' to '+end);
            currentDepositor = data;
            renderDepositorDetails(currentDepositor);
            renderDepositorList(currentDepositor);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(jqXHR, textStatus, errorThrown);
            alert('findDepositorBetweenDateReturnDeposit: ' + textStatus);
        }
    });
}

function findDepositByName(name) {
    console.log('findDepositByName: ' + name);
    $.ajax({
        type: 'GET',
        url: REST_URL + '/deposits/name/' + name,
        dataType: "json",
        success: function (data) {
            console.log('findByName success: ' + data.depositName);
            currentDeposit = data;
            renderDepositDetails(currentDeposit);
            renderDepositList(currentDeposit);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(jqXHR, textStatus, errorThrown);
            alert('findDepositByName: ' + textStatus);
        }
    });
}
function findDepositorByName(name) {
    console.log('findDepositorByName: ' + name);
    $.ajax({
        type: 'GET',
        url: REST_URL + '/depositors/name/' + name,
        dataType: "json",
        success: function (data) {
            console.log('findByName success: ' + data.depositorName);
            currentDepositor = data;
            renderDepositorDetails(currentDepositor);
            renderDepositorList(currentDepositor);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(jqXHR, textStatus, errorThrown);
            alert('findByName: ' + textStatus);
        }
    });
}

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
        $('#depositorList').append('<tr><td><a href="#" data-identity="' + depositor.depositorId + '">' +depositor.depositorId+"</td><td>"+ depositor.depositorName + "</td><td>"+ depositor.depositorIdDeposit + "</td><td>"+ depositor.depositorDateDeposit + "</td><td>"+ depositor.depositorAmountDeposit + "</td><td>"+ depositor.depositorAmountPlusDeposit + "</td><td>"+ depositor.depositorAmountMinusDeposit + "</td><td>"+ depositor.depositorDateReturnDeposit + "</td><td>"+ depositor.depositorMarkReturnDeposit + '</a></td></tr>');
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
    if (deposit.depositId == undefined) {
        $('#btnRemoveDdeposit').hide();
    } else {
        $('#btnRemoveDeposit').show();
    }
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
    if (depositor.depositorId == undefined) {
        $('#btnRemoveDdepositor').hide();
    } else {
        $('#btnRemoveDepositor').show();
    }
}

function searchByNameDeposit(searchKeyNameDeposit) {
    if (searchKeyNameDeposit == '') {
        findAllDeposits();
    } else {
        findDepositByName(searchKeyNameDeposit);
    }
}
function searchByNameDepositor(searchKeyNameDepositor) {
    if (searchKeyNameDepositor == '') {
        findAllDepositors();
    } else {
        findDepositorByName(searchKeyNameDepositor);
    }
}

function searchByIdDeposit(searchKeyIdDeposit) {
    if (searchKeyIdDeposit == '') {
        findAllDeposits();
    } else {
        findDepositById(searchKeyIdDeposit);
    }
}
function searchByIdDepositor(searchKeyIdDepositor) {
    if (searchKeyIdDepositor == '') {
        findAllDepositors();
    } else {
        findDepositorById(searchKeyIdDepositor);
    }
}
function searchByIdDepositDepositor(searchKeyIdDepositDepositor) {
    if (searchKeyIdDepositDepositor == '') {
        findAllDepositors();
    } else {
        findDepositorByIdDeposit(searchKeyIdDepositDepositor);
    }
}
function SearchBetweenDateDepositor(MyDate1,MyDate2) {
    if ((MyDate1 == '') |(MyDate2 == '')){
        findAllDepositors();
    } else {
        findDepositorBetweenDateDeposit(MyDate1,MyDate2);
    }
}
function SearchBetweenDateReturnDepositor(MyDate3,MyDate4) {
    if ((MyDate3 == '') |(MyDate4 == '')){
        findAllDepositors();
    } else {
        findDepositorBetweenDateReturnDeposit(MyDate3,MyDate4);
    }
}

function findAllDeposits() {
    console.log('findAllDeposits');
    $.ajax({
        type: 'GET',
        url: REST_URL+"/deposits/",
        dataType: "json", // data type of response
        success: renderDepositList,
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(jqXHR, textStatus, errorThrown);
            alert('findAllDeposit: ' + textStatus);
        }
    });
}

// Helper function to serialize all the form fields into a JSON string
function formDepositToJSON() {
    var depositId = $('#depositId').val();
    return JSON.stringify({
        "depositId": depositId == "" ? null : depositId,
        "depositName": $('#depositName').val(),
        "depositMinTerm": $('#depositMinTerm').val(),
        "depositMinAmount": $('#depositMinAmount').val(),
        "depositCurrency": $('#depositCurrency').val(),
        "depositInterestRate": $('#depositInterestRate').val(),
        "depositAddConditions": $('#depositAddConditions').val()
    });
}

function formDepositorToJSON() {
    var depositorId = $('#depositorId').val();
    return JSON.stringify({
        "depositorId": depositorId == "" ? null : depositorId,
        "depositorName": $('#depositorName').val(),
        "depositorIdDeposit": $('#depositorIdDeposit').val(),
        "depositorDateDeposit": $('#depositorDateDeposit').val(),
        "depositorAmountDeposit": $('#depositorAmountDeposit').val(),
        "depositorAmountPlusDeposit": $('#depositorAmountPlusDeposit').val(),
        "depositorAmountMinusDeposit": $('#depositorAmountMinusDeposit').val(),
        "depositorDateReturnDeposit": $('#depositorDateReturnDeposit').val(),
        "depositorMarkReturnDeposit": $('#depositorMarkReturnDeposit').val()

    });
}

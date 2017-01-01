// The root URL for the SOAP services
var SOAP_URL = "http://localhost:8080/SpringHibernateBDeposit-1.0/soap";

var wsdlInputMessage = [];
var xsdMessage = [];
var soapMessage = [];
var envelopeXmlns = 'xmlns:x="http://schemas.xmlsoap.org/soap/envelope/"';
var requestXmlns;
/*
$(document).ready(function(){
    jQuery(function($){
            $("#depositorDateDeposit,#depositorDateReturnDeposit")
            .mask("9999-99-99",{completed:function(){}});
        });
});
*/
// Register listeners
$('#sendMessage').click(function () {
    var soapUrl = $('#URL').val();
    var soapSendMessage = $('#requestRaw1').val();
    var soapAction = $("li[class$='ui-selected']").text()+'Request';
    sendSoapMessage(soapUrl, soapAction, soapSendMessage.replace(/\s+/g,' '));
    return false;
});

function input(){
    $('#requestRaw1').css('color', 'black');
}

// Declaration of functions
$( function() {
    var dialog, form,
        wsdlURL = $( "#wsdlURL" ),
        allFields = $( [] ).add( wsdlURL );

    function addWsdlUrl() {
        getWSDL(wsdlURL.val());
        dialog.dialog( "close" );
    }

    dialog = $( "#dialog-form" ).dialog({
      autoOpen: false,
      height: 200,
      width: 750,
      modal: true,
      buttons: {
        "GET": addWsdlUrl,
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
      addWsdlUrl();
    });

    $( "#get-wsdl" ).button().on( "click", function() {
      dialog.dialog( "open" );
    });
});


$( function() {
    $( "#selectable" ).selectable({
        selected: function (event, ui) {
            var requestSelect;
            var selected = $("li[class$='ui-selected']");
            alert("you selected " + selected.length + " items! Name - " + selected.text());
            $( "#tabs" ).tabs( "option", "active", 0 );
            for(var i=0; i<soapMessage.length; i++){
                if(soapMessage[i][0] == selected.text()+'Request'){
                    requestSelect = '<x:Envelope '+envelopeXmlns+' '+requestXmlns+'>'+
                                            '<x:Header/>'+
                                            '<x:Body>'+
                                                '<soa:'+soapMessage[i][0]+'>';
                    for(var j=1; j<soapMessage[i].length; j++){
                        requestSelect = requestSelect +'<soa:'+soapMessage[i][j]+'></soa:'+soapMessage[i][j]+'>';
                    }
                    requestSelect = requestSelect+'</soa:'+soapMessage[i][0]+'>'+
                                            '</x:Body>'+
                                         '</x:Envelope>';
                }
                if(soapMessage[i] == selected.text()+'Request') {
                    requestSelect = '<x:Envelope '+envelopeXmlns+' '+requestXmlns+'>'+
                                        '<x:Header/>'+
                                        '<x:Body>'+
                                            '<soa:'+soapMessage[i]+'>'+
                                            '</soa:'+soapMessage[i]+'>'+
                                        '</x:Body>'+
                                    '</x:Envelope>';
                }
            }
            console.log('soapRequest - '+requestSelect);
            $('#requestRaw1').val(formatXml(requestSelect)).css({"color":"#920","font-size": "100%"});
        }
    });
} );

$( function() {
    $( "#tabs" ).tabs();
} );

function getWSDL(url) {
    console.log('getWSDL');
    $.ajax({
        type: 'GET',
        url: url,// +"/soapService.wsdl",
        dataType: "html", // data type of response
        success: function (data, textStatus, jqXHR) {
            alert('getWSDL successfully');
            //$('#responseRaw').val(data);//formatXml(data));

            var xmlDoc = $.parseXML( data );

            //<wsdl:types>, <wsdl:message>, <wsdl:portType>, <wsdl:binding>, <wsdl:service>
            $(xmlDoc).children().children().each(function(){
                // <wsdl:service>
                if((this).nodeName == 'wsdl:service') {
                    $('#URL').val($(this).children().children().get(0).getAttribute('location'));
                }
                // <wsdl:types>
                if((this).nodeName == 'wsdl:types') {
                    // <xsd:schema>
                    $(this).children().each(function(){
                        // <xsd:schema xmlns: >
                        requestXmlns = 'xmlns:soa="' + $(this).get(0).getAttribute('xmlns:tns')+'"';
                        console.log('xmlns: '+ requestXmlns);

                        // <wsdl:element>
                        var j = 0;
                        $(this).children().each(function(){
                            if((this).nodeName == 'xsd:element'){
                                xsdMessage[j] = (this).getAttribute('name');
                                var n = 1;

                                if($(this).children().length > 0){
                                    var xmlNode = $(this).children();
                                    while(xmlNode.get(0).nodeName != 'xsd:element'){
                                        xmlNode = xmlNode.children();
                                    }
                                    var xsdElements = [];
                                    xsdElements[0] = xsdMessage[j];
                                    for (k = 0; k < xmlNode.length; k++) {
                                        xsdElements[n] = xmlNode.get(k).getAttribute('name');
                                        ++n;
                                    }
                                    soapMessage[j] = new Array();
                                    for(var i=0; i<xsdElements.length; i++){
                                        soapMessage[j][i] = xsdElements[i];
                                    }
                                }else {
                                    soapMessage[j] = xsdMessage[j];
                                }
                                ++j;
                            }
                        });
                    });
                    for(i = 0; i<soapMessage.length;i++){
                        console.log('soapMessage['+i+'] - ' + soapMessage[i]);
                    }
                }
                // <wsdl:binding>
                if((this).nodeName == 'wsdl:binding'){
                    var i = 0;
                    //<wsdl:operation>
                    $(this).children().each(function(){
                        if((this).nodeName == 'wsdl:operation'){
                            $('#selectable').append('<li id='+(this).getAttribute("name")+'>'+(this).getAttribute("name")+'</li>');
                            $('#'+(this).getAttribute("name")).addClass('ui-widget-content');
                            //<wsdl:operation> -> <wsdl:input>
                            $(this).children().each(function(){
                                if((this).nodeName == 'wsdl:input'){
                                    wsdlInputMessage[i] = (this).getAttribute('name');
                                    console.log('wsdlInputMessage['+i+'] - '+wsdlInputMessage[i]);
                                    i = i+1;
                                }
                            });
                        }
                    })

                    $('div#tabs-1').append('<textarea class="requestRaw1" onclick="input()" id="requestRaw1" name="requestRaw1"  ></textarea>');
                    $('div#tabs-2').append('<textarea class="responseHeader1" id="responseHeader1" name="responseHeader1" rows="8"  ></textarea><br>');
                    $('div#tabs-2').append('<textarea class="responseRaw1" id="responseRaw1" name="responseRaw1"  ></textarea>');

                    $('#responseRaw1').val(formatXml(data));
                    $('#responseHeader1').val("Status: "+textStatus+", Code: "+jqXHR.status+"\n"+
                                                         "\nServer: "+jqXHR.getResponseHeader("Server")+
                                                         "\nContent-Type: "+jqXHR.getResponseHeader("Content-Type")+
                                                         "\nTransfer-Encoding: "+jqXHR.getResponseHeader("Transfer-Encoding")+
                                                         "\nAccess-Control-Allow-Origin: "+jqXHR.getResponseHeader("Access-Control-Allow-Origin")+
                                                         "\nAccess-Control-Allow-Origin: "+jqXHR.getResponseHeader("Origin")+
                                                         "\nAccept: "+jqXHR.getResponseHeader("Accept")+
                                                         "\nContent: "+jqXHR.getResponseHeader("Content")+
                                                         "\nContent-Length: "+jqXHR.getResponseHeader("Content-Length")+
                                                         "\nAccess-Control-Allow-Methods: "+jqXHR.getResponseHeader("Access-Control-Allow-Methods")+
                                                         "\nServer: "+jqXHR.getResponseHeader("server")
                                                        );

                    //var length = $('#selectable').outerHeight()-$('#tabs').outerHeight();
                    //$('#requestRaw1').outerHeight(length-$('#URL').outerHeight()-$('#sendMessage').outerHeight());
                    //$('#responseRaw1').outerHeight(length-$('#responseHeader1').outerHeight()-$('#URL').outerHeight()-$('#sendMessage').outerHeight());
                    $('#tabs').height($('#selectable').outerHeight(true)-$('#URL').outerHeight(true));
                    var length = $('#requestRaw1').outerHeight(true)-$('#requestRaw1').height();
                    $('#requestRaw1').height($('#tabs').height());
                    $('#responseRaw1').height($('#tabs').height() - $('#responseHeader1').outerHeight(true));
                    $( "#tabs" ).tabs( "option", "active", 1 );
                }
            });
        },
        error: function(jqXHR, textStatus, errorThrown) {
           $('#responseHeader1').val("Status: "+textStatus+", Code: "+jqXHR.status+"\n"+
                                                                         "\nServer: "+jqXHR.getResponseHeader("Server")+
                                                                         "\nContent-Type: "+jqXHR.getResponseHeader("Content-Type")+
                                                                         "\nTransfer-Encoding: "+jqXHR.getResponseHeader("Transfer-Encoding")+
                                                                         "\nDate: "+jqXHR.getResponseHeader("Date")+
                                                                         "\nAccess-Control-Allow-Origin: "+jqXHR.getResponseHeader("Access-Control-Allow-Origin")+
                                                                         "\nAccess-Control-Allow-Origin: "+jqXHR.getResponseHeader("Origin")+
                                                                         "\nAccept: "+jqXHR.getResponseHeader("Accept")+
                                                                         "\nContent: "+jqXHR.getResponseHeader("Content")+
                                                                         "\nContent-Length: "+jqXHR.getResponseHeader("Content-Length")+
                                                                         "\nAccess-Control-Allow-Methods: "+jqXHR.getResponseHeader("Access-Control-Allow-Methods")+
                                                                         "\nServer: "+jqXHR.getResponseHeader("server")
                                    );
           $('#responseRaw1').show();
           $('#responseRaw1').val(jqXHR.responseText);
           $( "#tabs" ).tabs( "option", "active", 1 );
        }
    });
}

// send SOAP message
function sendSoapMessage(url,soapAction,dataSOAP) {
    console.log('send SOAP message: ' + soapAction);
    console.log('soapAction: '+ url+'/'+soapAction);
    console.log('soapMessage: '+ dataSOAP);
    $.soap({
        url: url,
        type: "POST",
        dataType: "xml",
        crossDomain: true,
        contentType: "text/xml; charset=\"utf-8\"",
        headers: {
            SOAPAction: url+'/'+soapAction
        },
        data: dataSOAP,
        success: function (soapResponse) {
            alert('send SOAP message: successfully');
            $('#responseHeader1').val('POST:\nStatus: '+soapResponse.status+
                                    "\nStatus code: "+soapResponse.httpCode+', '+soapResponse.httpText+
                                    "\n\nHeaders: "+soapResponse.headers[0]
                                    );
            //console.log(('  HTTPHeaders: ' + HTTPHeaders);
            $('#responseRaw1').show();
            console.log('soapResponse: ' + formatXml(soapResponse));
            $('#responseRaw1').val(formatXml(soapResponse));
            $( "#tabs" ).tabs( "option", "active", 1 );
        },
        error: function (soapResponse) {
            alert('send SOAP message: error');
            $('#responseHeader1').val('POST:\nStatus: '+soapResponse.status+
                    "\nStatus code: "+soapResponse.httpCode+
                    "\n\nHeaders: \n"+soapResponse.headers+
                    "\nHttpText: "+soapResponse.httpText);
            $('#responseRaw1').show();
            $('#responseRaw1').val(formatXml(soapResponse));
            $( "#tabs" ).tabs( "option", "active", 1 );
        }
    });
}

// Helper function to format data to XML form
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
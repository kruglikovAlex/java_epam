$(document).ready(function(){
    $('.formDeposit').append('<fieldset id="depositRequest" style="width: 100%" class="fsDeposit"></fieldset>');
    $('.fsDeposit').append('<legend style="font-weight: bold">Deposit ()</legend>');
    $('.fsDeposit').append('Id:<input type="text" id="id"  name="depositId" />');
    $('.fsDeposit').append('Name:<input type="text" id="name" name="depositName" size="70" /></br>');
    $('.fsDeposit').append('Term:<input type="text" id="term" name="depositMinTerm" size="3"/>');
    $('.fsDeposit').append(' Amount:<input type="text" id="amount" name="depositMinAmount" />');
    $('.fsDeposit').append(' Currency:<input type="text" id="currency" name="depositCurrency" size="3"/>');
    $('.fsDeposit').append(' Rate:<input type="text" id="rate" name="depositInterestRate" size="3"/></br>');
    $('.fsDeposit').append('Conditions:<textarea rows="3" cols="70" id="conditions" name="depositAddConditions"/></br>');
    $('.fsDeposit').append('Count depositors:<input type="text" id="count" name="depositorCount" size="3"/></br>');
    $('.fsDeposit').append('Sum - Amount:<input type="text" id="sumA" name="depositorAmountSum"/>');
    $('.fsDeposit').append(' Plus:<input type="text" id="sumP" name="depositorAmountPlusSum"/>');
    $('.fsDeposit').append(' Minus:<input type="text" id="sumM" name="depositorAmountMinusSum"/>');

    $(function() {
        $( "#tabs" ).tabs();
    });

    $(function() {
        $( ".datepicker").datepicker({
            appendText:"(yyyy-mm-dd)",
            dateFormat:"yy-mm-dd",
            changeMonth:true,
            changeYear:true
        });
    });
});

$('#tabs-2').click(function(){

    $('#fId')
        .attr('type',"text")
        .bind('change',function () {
            var aFindByIdHref='../deposit/filterById?depositId='+$('#fId').val();
            $('#aFindById').text('Find').attr('href',aFindByIdHref).addClass('buttonFilter');
    });

    $('#fIdD')
        .attr('type',"text")
        .bind('change',function () {
            var aFindByIdDHref='../depositor/filterById?depositorId='+$('#fIdD').val();
            $('#aFindByIdD').text('Find').attr('href',aFindByIdDHref).addClass('buttonFilter');
    });

    $('#fName')
        .attr('type',"text")
        .bind('change',function () {
            var aFindByNameHref='../deposit/filterByName?depositName='+$('#fName').val();
            $('#aFindByName').text('Find').attr('href',aFindByNameHref).addClass('buttonFilter');
    });

    $('#fNameD')
        .attr('type',"text")
        .bind('change',function () {
            var aFindByNameDHref='../depositor/filterByName?depositorName='+$('#fNameD').val();
            $('#aFindByNameD').text('Find').attr('href',aFindByNameDHref).addClass('buttonFilter');
    });

    $('#fTerm')
        .attr('type',"text")
        .bind('change',function () {
            var aFindByTermHref='../deposit/filterByTerm?depositMinTerm='+$('#fTerm').val();
            $('#aFindByTerm').text('Find').attr('href',aFindByTermHref).addClass('buttonFilter');
    });

    $('#fAmount')
        .attr('type',"text")
        .bind('change',function () {
            var aFindByAmountHref='../deposit/filterByAmount?depositMinAmount='+$('#fAmount').val();
            $('#aFindByAmount').text('Find').attr('href',aFindByAmountHref).addClass('buttonFilter');
    });

    $('#fRate')
        .attr('type',"text")
        .bind('change',function () {
            var aFindByRateHref='../deposit/filterByRate?depositInterestRate='+$('#fRate').val();
            $('#aFindByRate').text('Find').attr('href',aFindByRateHref).addClass('buttonFilter');
    });

    $('#fCurrency')
        .attr('type',"text")
        .bind('change',function () {
            var aFindByCurrencyHref='../deposit/filterByCurrency?depositCurrency='+$('#fCurrency').val();
            $('#aFindByCurrency').text('Find').attr('href',aFindByCurrencyHref).addClass('buttonFilter');
    });

    $('#fStartDateDeposit')
        .attr('type',"datetime");
    $('#fEndDateDeposit')
        .attr('type',"datetime")
        .bind('change',function () {
            var aFindByDateDepositHref;
            if($('input:checkbox[name=idDeposit]').is(':checked')){
                aFindByDateDepositHref='../deposit/filterByIdFromToDateDeposit?depositId='+$('#fId').val()+'&startDateDeposit='+$('#fStartDateDeposit').val()+'&endDateDeposit='+$('#fEndDateDeposit').val();
                $('#aFindByDateDeposit').text('Find').attr('href',aFindByDateDepositHref).addClass('buttonFilter');
            }else{
                if($('input:checkbox[name=nameDeposit]').is(':checked')){
                    aFindByDateDepositHref='../deposit/filterByNameFromToDateDeposit?depositName='+$('#fName').val()+'&startDateDeposit='+$('#fStartDateDeposit').val()+'&endDateDeposit='+$('#fEndDateDeposit').val();
                    $('#aFindByDateDeposit').text('Find').attr('href',aFindByDateDepositHref).addClass('buttonFilter');
                }else{
                    if($('input:checkbox[name=currencyDeposit]').is(':checked')){
                        aFindByDateDepositHref='../deposit/filterByCurrencyFromToDateDeposit?depositCurrency='+$('#fCurrency').val()+'&startDateDeposit='+$('#fStartDateDeposit').val()+'&endDateDeposit='+$('#fEndDateDeposit').val();
                        $('#aFindByDateDeposit').text('Find').attr('href',aFindByDateDepositHref).addClass('buttonFilter');
                    }else{
                        aFindByDateDepositHref='../deposit/filterByDateDeposit?startDateDeposit='+$('#fStartDateDeposit').val()+'&endDateDeposit='+$('#fEndDateDeposit').val();
                        $('#aFindByDateDeposit').text('Find').attr('href',aFindByDateDepositHref).addClass('buttonFilter');
                    }
                }

            }
    });

    $('#fStartDateReturnDeposit')
        .attr('type',"datetime");
    $('#fEndDateReturnDeposit')
        .attr('type',"datetime")
        .bind('change',function () {
            var aFindByDateReturnDepositHref;
            if($('input:checkbox[name=idDeposit]').is(':checked')){
                aFindByDateReturnDepositHref='../deposit/filterByIdFromToDateReturnDeposit?depositId='+$('#fId').val()+'&startDateReturnDeposit='+$('#fStartDateReturnDeposit').val()+'&endDateReturnDeposit='+$('#fEndDateReturnDeposit').val();
                $('#aFindByDateReturnDeposit').text('Find').attr('href',aFindByDateReturnDepositHref).addClass('buttonFilter');
            }else{
                if($('input:checkbox[name=nameDeposit]').is(':checked')){
                    aFindByDateReturnDepositHref='../deposit/filterByNameFromToDateReturnDeposit?depositName='+$('#fName').val()+'&startDateReturnDeposit='+$('#fStartDateReturnDeposit').val()+'&endDateReturnDeposit='+$('#fEndDateReturnDeposit').val();
                    $('#aFindByDateReturnDeposit').text('Find').attr('href',aFindByDateReturnDepositHref).addClass('buttonFilter');
                }else{
                    if($('input:checkbox[name=currencyDeposit]').is(':checked')){
                        aFindByDateReturnDepositHref='../deposit/filterByCurrencyFromToDateReturnDeposit?depositCurrency='+$('#fCurrency').val()+'&startDateReturnDeposit='+$('#fStartDateReturnDeposit').val()+'&endDateReturnDeposit='+$('#fEndDateReturnDeposit').val();
                        $('#aFindByDateReturnDeposit').text('Find').attr('href',aFindByDateReturnDepositHref).addClass('buttonFilter');
                    }else{
                        aFindByDateReturnDepositHref='../deposit/filterByDateReturnDeposit?startDateReturnDeposit='+$('#fStartDateReturnDeposit').val()+'&endDateReturnDeposit='+$('#fEndDateReturnDeposit').val();
                        $('#aFindByDateReturnDeposit').text('Find').attr('href',aFindByDateReturnDepositHref).addClass('buttonFilter');
                    }
                }

            }
    });

    $('#fFromAmount')
        .attr('type',"text");
    $('#fToAmount')
        .attr('type',"text")
        .bind('change',function () {
            var aFindByAmountDepositorHref='../depositor/filterByAmount?fromAmountDepositor='+$('#fFromAmount').val()+'&toAmountDepositor='+$('#fToAmount').val();
            $('#aFindByAmountDepositor').text('Find').attr('href',aFindByAmountDepositorHref).addClass('buttonFilter');
    });

    $('input:radio')
        .bind('click',function () {
            var aFindByMarkReturnHref;
            var param = Array('0','1');

            for (i=0;i<$(":radio[name=markReturn]").length; i++){
                if($(":radio[name=markReturn]").get(i).checked)
                    aFindByMarkReturnHref='../depositor/filterByMarkReturn?depositorMarkReturnDeposit='+param[i];
            }
            $('#aFindByMarkReturnDepositor').text('Find').attr('href',aFindByMarkReturnHref).addClass('buttonFilter');
    });
});

$('input:checkbox').click(function(){
    if($('input:checkbox:checked').length>0){
        $('#aFindByCriteria').text(' Find by Criteria ').addClass('buttonFilter').attr('disabled','');
    }else{
        $('#FindByCriteria').attr('disabled','disabled');
    }
});

$('#aFindByCriteria').click(function(){
    var url;

    if($('input:checkbox:checked').length == 1){
        url = '';
        if($('input:checkbox[name=idDeposit]').is(':checked')){
            url='../deposit/filterById?depositId='+$('#fId').val();
        }
        if($('input:checkbox[name=nameDeposit]').is(':checked')){
            url='../deposit/filterByName?depositName='+$('#fName').val();
        }
        if($('input:checkbox[name=termDeposit]').is(':checked')){
            url='../deposit/filterByTerm?depositMinTerm='+$('#fTerm').val();
        }
        if($('input:checkbox[name=amountDeposit]').is('checked')){
            url='../deposit/filterByAmount?depositMinAmount='+$('#fAmount').val();
        }
        if($('input:checkbox[name=rateDeposit]').is(':checked')){
            url='../deposit/filterByRate?depositInterestRate='+$('#fRate').val();
        }
        if($('input:checkbox[name=currencyDeposit]').is(':checked')){
            url='../deposit/filterByCurrency?depositCurrency='+$('#fCurrency').val();
        }
        if($('input:checkbox[name=dateDeposit]').is(':checked')){
            url='../deposit/filterByDateDeposit?startDateDeposit='+$('#fStartDateDeposit').val()+'&endDateDeposit='+$('#fEndDateDeposit').val();
        }
        if($('input:checkbox[name=dateReturnDeposit]').is(':checked')){
            url='../deposit/filterByDateReturnDeposit?startDateReturnDeposit='+$('#fStartDateReturnDeposit').val()+'&endDateReturnDeposit='+$('#fEndDateReturnDeposit').val();
        }
        if($('input:checkbox[name=idDepositor]').is(':checked')){
            url='../depositor/filterById?depositorId='+$('#fIdD').val();
        }
        if($('input:checkbox[name=nameDepositor]').is(':checked')){
            url='../depositor/filterByName?depositorName='+$('#fNameD').val();
        }
        if($('input:checkbox[name=amountDepositor]').is(':checked')){
            url='../depositor/filterByAmount?fromAmountDepositor='+$('#fFromAmount').val()+'&toAmountDepositor='+$('#fToAmount').val();
        }
    } else {
        url = '../deposit/filterByCriteria';
        var len = url.length;
        if($('input:checkbox[name=idDeposit]').is(':checked')){
            if(url.length == len) url+='?';
            else url+="&";
            url+='depositId='+$('#fId').val();
        }
        if($('input:checkbox[name=nameDeposit]').is(':checked')){
            if(url.length == len) url+='?';
            else url+="&";
            url+='depositName='+$('#fName').val();
        }
        if($('input:checkbox[name=termDeposit]').is(':checked')){
            if(url.length == len) url+='?';
            else url+="&";
            url+='depositMinTerm='+$('#fTerm').val();
        }
        if($('input:checkbox[name=amountDeposit]').is(':checked')){
            if(url.length == len) url+='?';
            else url+="&";
            url+='depositMinAmount='+$('#fAmount').val();
        }
        if($('input:checkbox[name=rateDeposit]').is(':checked')){
            if(url.length == len) url+='?';
            else url+="&";
            url+='depositInterestRate='+$('#fRate').val();
        }
        if($('input:checkbox[name=currencyDeposit]').is(':checked')){
            if(url.length == len) url+='?';
            else url+="&";
            url+='depositCurrency='+$('#fCurrency').val();
        }
        if($('input:checkbox[name=dateDeposit]').is(':checked')){
            if(url.length == len) url+='?';
            else url+="&";
            url+='startDateDeposit='+$('#fStartDateDeposit').val()+'&endDateDeposit='+$('#fEndDateDeposit').val();
        }
        if($('input:checkbox[name=dateReturnDeposit]').is(':checked')){
            if(url.length == len) url+='?';
            else url+="&";
            url+='startDateReturnDeposit='+$('#fStartDateReturnDeposit').val()+'&endDateReturnDeposit='+$('#fEndDateReturnDeposit').val();
        }
        if($('input:checkbox[name=idDepositor]').is(':checked')){
            url='../depositor/filterById?depositorId='+$('#fIdD').val();
        }
        if($('input:checkbox[name=nameDepositor]').is(':checked')){
            url='../depositor/filterByName?depositorName='+$('#fNameD').val();
        }
        if($('input:checkbox[name=amountDepositor]').is(':checked')){
            if(url.length == len) url+='?';
            else url+="&";
            url+='../depositor/fromAmountDepositor='+$('#fFromAmount').val()+'&toAmountDepositor='+$('#fToAmount').val();
        }
    }

    $('#aFindByCriteria').attr('href',url);
});

$('input:radio').click(function(){
    var aUpdateDepositorHref='../depositor/updateDepositor?depositorId='+$('input:radio:checked').val();
    var aDeleteDepositorHref='../depositor/deleteDepositor?depositorId='+$('input:radio:checked').val();
    $('#aUpdDep').text('Edit').attr('href',aUpdateDepositorHref).addClass('buttonUpd');
    $('#aDelDep').text('Delete').attr('href',aDeleteDepositorHref).addClass('buttonDel');
});

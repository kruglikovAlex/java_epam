$(document).ready(function(){
    $(function() {
        $( ".datepicker").datepicker({
            appendText:"(yyyy-mm-dd)",
            dateFormat:"yy-mm-dd",
            changeMonth:true,
            changeYear:true
        });
    });
});

$(function() {
    $('input:text[name=depositorAmountDeposit], input:text[name=depositorAmountPlusDeposit], input:text[name=depositorAmountMinusDeposit], input:radio[name=depositorMarkReturnDeposit]').bind('change',function () {
        if(this.value == null || this.value == '') {
            alert('The field '+ this.name+' is empty!!!');
            if(document.getElementById('error'+this.name)!=null){
                document.getElementById('error'+this.name).style.border=  "1px dotted red"
                document.getElementById('error'+this.name).style.fontSize = "x-large";
            }
            this.value = 0;
        }
    });
});

$(function() {
    $('input:text[name=depositorName]').bind('change',function () {
        if(this.value == null || this.value == '') {
            alert('The field '+ this.name+' is empty!!!');
            if(document.getElementById('error'+this.name)!=null){
                document.getElementById('error'+this.name).style.border=  "1px dotted red"
                document.getElementById('error'+this.name).style.fontSize = "x-large";
            }
            this.value = "Input Full name of depositor!";
        }
    });
});

$('#submit').on('click', 'a', function () {
    if($('input:text[name=depositorName]').value == null || $('input:text[name=depositorName]').value == '') {
        $('input:text[name=depositorName]').value = "Input Full name of depositor!";
    }
    if($('input:text[name=depositorAmountDeposit]').value == null || $('input:text[name=depositorAmountDeposit]').value == '') {
        $('input:text[name=depositorAmountDeposit]').value = "0";
    }
    if($('input:text[name=depositorAmountPlusDeposit]').value == null || $('input:text[name=depositorAmountPlusDeposit]').value == '') {
        $('input:text[name=depositorAmountPlusDeposit]').value = "0";
    }
    if($('input:text[name=depositorAmountMinusDeposit]').value == null || $('input:text[name=depositorAmountMinusDeposit]').value == '') {
        $('input:text[name=depositorAmountMinusDeposit]').value = "0";
    }
    if($('input:radio[name=depositorMarkReturnDeposit]').value == null || $('input:radio[name=depositorMarkReturnDeposit]').value == '') {
        $('input:radio[name=depositorMarkReturnDeposit]').value = "0";
    }
});


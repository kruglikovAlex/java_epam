$(document).ready(function(){
    $(function() {
        $('input:text[name=depositMinTerm], input:text[name=depositMinAmount], input:text[name=depositInterestRate]').bind('change',function () {
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

    $('input:submit[name=action]').click(function () {
        if($('input:text[name=depositMinTerm]').value == null || $('input:text[name=depositMinTerm]').value == '') {
            $('input:text[name=depositMinTerm]').value = "0";
        }
        if($('input:text[name=depositMinAmount]').value == null || $('input:text[name=depositMinAmount]').value == '') {
            $('input:text[name=depositMinAmount]').value = "0";
        }
        if($('input:text[name=depositInterestRate]').value == null || $('input:text[name=depositInterestRate]').value == '') {
            $('input:text[name=depositInterestRate]').value = "0";
        }
    });
});


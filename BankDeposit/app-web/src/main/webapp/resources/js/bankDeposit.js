var REST_URL = "http://localhost:8080/depositsList";

function changeFunc() {
    var pattern=/['A-z']/;
    if(!pattern.test(this.value)) alert('Check the correct fields - Find by name ');
}

$(document).ready(function(){
    var summ = 0;


<!-- on open document -->
    $('td.Am').each(function(){
        summ += Number($(this).text());
    });
    $('td.summ1').text(summ);
    summ = 0;

    $('td.Plus').each(function(){
        summ += Number($(this).text());
    });
    $('td.summ2').text(summ);
    summ = 0;

    $('td.Minus').each(function(){
        summ += Number($(this).text());
    });
    $('td.summ3').text(summ);
    summ = 0;

<!-- on click -->
    $('td.summ1').click(function(){
        $('td.Am').each(function(){
            summ += Number($(this).text());
        });
        $('td.summ1').text(summ);
        summ = 0;
    });
    $('td.summ2').click(function(){
        $('td.Plus').each(function(){
            summ += Number($(this).text());
        });
        $('td.summ2').text(summ);
        summ = 0;
    });
    $('td.summ3').click(function(){
        $('td.Minus').each(function(){
            summ += Number($(this).text());
        });
        $('td.summ3').text(summ);
        summ = 0;
    });
    jQuery(function($){
        $("#MyDate1,#MyDate2,#MyDate3,#MyDate4,#MyDate5,#MyDate6,#MyDate7,#MyDate8")
        .mask("9999-99-99",{completed:function(){}});
    });

});
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


$('select').click(function () {
        $('#dId').val($('select option:selected').val());
        var aUpdateHref='updateDeposit?depositId= '+$('#dId').val();
        var aDeleteHref='deleteDeposit?depositId= '+$('#dId').val();
        var aAddDepositorHref='../depositor/inputDepositor?idDeposit='+$('#dId').val();
        $('#aUpdate').text('Edit').attr('href',aUpdateHref).addClass('buttonUpd');
        $('#aDelete').text('Delete').attr('href',aDeleteHref).addClass('buttonDel');
        $('#aAddDep').text('Add').attr('href',aAddDepositorHref).addClass('buttonAdd');
    });

$('input:radio').click(function(){
        var aUpdateDepositorHref='../depositor/updateDepositor?depositorId='+$('input:radio:checked').val();
        var aDeleteDepositorHref='../depositor/deleteDepositor?depositorId='+$('input:radio:checked').val();
        $('#aUpdDep').text('Edit').attr('href',aUpdateDepositorHref).addClass('buttonUpd');
        $('#aDelDep').text('Delete').attr('href',aDeleteDepositorHref).addClass('buttonDel');
    });
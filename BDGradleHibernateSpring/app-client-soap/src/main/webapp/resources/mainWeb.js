  $( function() {
      var dialog, form,
          wsdlURL = $( "#wsdlURL" ),
          allFields = $( [] ).add( wsdlURL );

      function addWsdlUrl() {
          goString = 'main?wsdlLoc='+wsdlURL.val();
          window.location.replace(goString);
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
    $( "#accordion" ).accordion({autoHeight:false,navigation: true});
  } );

  $( function() {
    $( "#tabs" ).tabs();
  } );
/*
  $( function() {
    $("#tabs").css("width",$("textarea").offsetWidth);
  })
  */


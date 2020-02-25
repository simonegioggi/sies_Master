<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html>
    <head>
    	<title>Cancellazione Foglio Complementare</title>
    	<link rel="stylesheet" type="text/css" href="table.css" />
		<style type="text/css">
 			input.submit {
 				font-size: 10px;
 				font-weight: bold;
 				color: #014C97;
				border: 1px solid #7B9EBD;
			}
 		</style>
        <script type="text/javascript" src="jquery-1.6.2.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                var idEvento = $.getUrlVar('idEvento');
                //MEV 35253
                var idUtente = $.getUrlVar('idUtente');
                //MEV_06
                var tipoOperazione = $.getUrlVar('tipoOperazione');

              	// MEV 16: aggiunte variabili e passate come parametro nella url
                var tipoWS 			= $.getUrlVar('tipoWS');
                var idSoggetto 		= $.getUrlVar('idSoggetto');
                var idSentenza 		= $.getUrlVar('idSentenza');
                var idFascicoloSiep = $.getUrlVar('idFascicoloSiep');
                
                //Check sui parametri
                //se non sono presenti si presenta pagina d'errore
                if (idEvento == undefined)
                    $('#errorParameter').fadeIn(1500);
                else {
                    $('#loading').fadeIn(1500);
 					
 					//MEV 35253
 					$.ajax({
					    url: "cancellazioneContestuale?idEvento="+idEvento+"&action=DELETE&idUtente="+idUtente+"&tipoOperazione="+tipoOperazione+
					    		// MEV 16
					    		"&tipoWS="+tipoWS+"&idSoggetto="+idSoggetto+"&idSentenza="+idSentenza+"&idFascicoloSiep="+idFascicoloSiep,
					    cache: false,
					    dataType: "html",
					    success: function(data) {
					    	$("#result").html(data);
						    //FADE IN DEL RISULTATO
							$('#loading').fadeOut(1500, function(){
				            	$('#result').fadeIn(1500);
				            });
					    }
					});
 					
                }
                    
            });
            
            $.extend({
                getUrlVars: function(){
                    var vars = [], hash;
                    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
                    for(var i = 0; i < hashes.length; i++)
                    {
                        hash = hashes[i].split('=');
                        vars.push(hash[0]);
                        vars[hash[0]] = hash[1];
                    }
                    return vars;
                },
                getUrlVar: function(name){
                    return $.getUrlVars()[name];
                }
            });
 
        </script>
    </head>
    <body>
        <div id="loading" style="display:none;">
            <center>
                <br/>
                <h3>Operazione in corso...</h3>
                <img src="images/ajax-loader.gif"/>
            </center>
        </div>
        <div id="errorParameter" style="display:none;">
            <center>
                <br/>
                <h3>Errore: parametri mancanti</h3>
                <img src="images/error.png"/>
            </center>
        </div>
        <div id="result" style="display:none;"></div>
    </body>
</html>
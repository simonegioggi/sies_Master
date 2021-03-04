<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<html>
    <head>
    	<title>Trasferimento Foglio Complementare</title>
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
        <script language="JavaScript1.2">
            function resizeWin(myWidth,myHeight){
                window.resizeTo(myWidth, myHeight);
            }
        </script>
        <script type="text/javascript">
 
            $(document).ready(function(){
                var action = $.getUrlVar('action');
                var idEvento = $.getUrlVar('idEvento');
                //MEV 35253
                var idUtente = $.getUrlVar('idUtente');
                //MEV_06
                var tipoOperazione = $.getUrlVar('tipoOperazione');
             	// MEV INTEGRAZIONE SIES ADN: aggiunta impostazione di variabile userAdn
                var userAdn = $.getUrlVar('userAdn');
                
                //Check sui parametri
                //se non sono presenti si presenta pagina d'errore
                if ((idEvento == undefined)||(action == undefined))
                    $('#errorParameter').fadeIn(1500);
                else {
                    $('#loading').fadeIn(1500);
                    
                 	// MEV 16 : WS to NSC
    				var tipoWS 				= $.getUrlVar('tipoWS');
    				var idSoggetto 			= $.getUrlVar('idSoggetto');
    				var idSentenza 			= $.getUrlVar('idSentenza');
    				var idFascicoloSiep 	= $.getUrlVar('idFascicoloSiep');
    				// MEV 16 CUMULO: aggiunte variabili e riportate ovunque serva
    				var idSinonimo 			= $.getUrlVar('idSinonimo');
    				var idSoggettoNSC		= $.getUrlVar('idSoggettoNSC');
    				var azioneTrasfCumulo	= $.getUrlVar('azioneTrasfCumulo');
    				if ('siepToNsc' == tipoWS) {
    					$.ajax({
						    url: "trasferisciFoglioComplementareSIEP?action="+action+"&idEvento="+idEvento+"&idUtente="+idUtente+"&userAdn="+userAdn+
						    		"&tipoOperazione="+tipoOperazione+"&idSoggetto="+idSoggetto+"&idSentenza="+idSentenza+"&idFascicoloSiep="+idFascicoloSiep+
						    		"&idSinonimo="+idSinonimo+"&idSoggettoNSC="+idSoggettoNSC+"&azioneTrasfCumulo="+azioneTrasfCumulo,
						    cache: false,
						    dataType: "html",
						    success: function(data) {
						    	$("#result").html(data);
							    // FADE IN DEL RISULTATO
								$('#loading').fadeOut(1500, function() {
					            	$('#result').fadeIn(1500);
					            });
						    }
						});
    				} else {
	 					//MEV 35253
	 					$.ajax({
						    url: "trasferimento?action="+action+"&idEvento="+idEvento+"&idUtente="+idUtente+"&userAdn="+userAdn+"&tipoOperazione="+tipoOperazione,
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
                <br/>
                <br/>
                <h3>Operazione in corso...</h3>
                <img src="images/ajax-loader.gif"/>
            </center>
        </div>
        <div id="errorParameter" style="display:none;">

            <center>
                <br/>
                <br/>
                <br/>
                <h3>Errore: parametri mancanti</h3>
                <img src="images/error.png"/>
            </center>
        </div>
        
        <div id="result" style="display:none;"></div>
        
    </body>
</html>
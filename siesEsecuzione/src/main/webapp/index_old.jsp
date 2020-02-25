<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<html>
    <head>
        <script type="text/javascript" src="jquery-1.6.2.min.js"></script>
        <script language="JavaScript1.2">
            <!--
            function resizeWin(myWidth,myHeight){
                window.resizeTo(myWidth, myHeight);
            }
            -->
        </script>
        <script type="text/javascript">
 
            $(document).ready(function(){
 
                var action = $.getUrlVar('action');
                var idEvento = $.getUrlVar('idEvento');
                //var idFascicolo = $.getUrlVar('idFascicolo');
                
                //Check sui parametri
                //se non sono presenti si presenta pagina d'errore
                if ((idEvento == undefined)||(action == undefined))
                    $('#errorParameter').fadeIn(1500);
                else {
                    $('#loading').fadeIn(1500);
                    $.getJSON('trasferimento?action='+action+'&idEvento='+idEvento, function(data) {
                        var thePage = $("body");
                        
                        if (data.result) {
                            
                            
                            if (data.estratto=="0") {
                            	$("#esitoNoData").text(data.esito);
                            	
                            	$('#loading').fadeOut(1500, function(){
	                                $('#completedNoData').fadeIn(1500);
	                            });
                            }
                            else {
                            	$("#esito").text(data.esito);
                            	
                            	thePage.html(
	                                thePage.html().replace("#id", data.id)
	                            );
	                            
	                            $('#loading').fadeOut(1500, function(){
	                                $('#completed').fadeIn(1500);
	                            });
                            }
                        }
                        else {
                            $("#esitoErrore").text(data.esito);
                            
                            $('#loading').fadeOut(1500, function(){
                                $('#errorCompleted').fadeIn(1500);
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
    <body onclick="resize(400,400)">
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
        <div id="completed" style="display:none;">

            <center>
                <br/>
                <br/>
                <br/>
                <h3 id="esito">#esito</h3>
                <img src="images/pdf_icon.jpg"/><a href="load?id=#id">Visualizza estratto</a>
            </center>
        </div>
        <div id="completedNoData" style="display:none;">

            <center>
                <br/>
                <br/>
                <br/>
                <h3 id="esitoNoData">#esitoNoData</h3>
            </center>
        </div>
        <div id="errorCompleted" style="display:none;">

            <center>
                <br/>
                <br/>
                <br/>
                <h3 id="esitoErrore">#esito</h3>
                <img src="images/error.png"/>
            </center>
        </div>


    </body>
</html>
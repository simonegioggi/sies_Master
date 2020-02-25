<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Trasferimento Massivo</title>
    <link rel="stylesheet" type="text/css" href="table.css" />
    <script type="text/javascript" src="jquery-1.6.2.min.js"></script>
    <script type="text/javascript">
    	
  		$(document).ready(function () {
  			$('#result').empty();
  			$('#result').hide();
		});
    	
    	$(function(){
		
		  $('#submit').click(function(){
		  	$('#result').empty();
		  	//FADE IN DEL LOADING
		  	$('#content').fadeOut(1500,function(){
		  		$('#loading').fadeIn(1500);
		  	});
		  	
		  	var tipologia=$('input:radio[name="tipologia"]:checked').val();
		  	var randomNumber = Math.floor(Math.random()*2);
		  	
		  	$.ajax({
			    url: "trasferimentoMassivo?tipologia="+tipologia,
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
		  	
		  });
		  
		});
	</script>
	<script language="JavaScript1.2">
		function resizeWin(myWidth,myHeight){
            window.resizeTo(myWidth, myHeight);
        }
    </script>
</head>

<body>
	<center>
    <h1>Trasferimento Massivo</h1>
    <hr/>
	<div id="content">
	    <fieldset>
	        <legend>Dati da trasferire</legend>
	        <label for="tipologia">Tipologia: </label><br/>
            <input type=RADIO name="tipologia" value="TDS" CHECKED >Dati Tribunale Sorveglianza<BR>
			<input type=RADIO name="tipologia" value="UDS">Dati Ufficio Sorveglianza<BR>
            <br/>
            <input id="submit" type="submit" value="Avvia trasferimento massivo"/>
	    </fieldset>
    </div>
    <div id="loading" style="display:none;">
			<center>
                <br/>
                <br/>
                <br/>
                <h3>Operazione in corso...</h3>
                <img src="images/ajax-loader.gif"/>
            </center>
    </div>
    <div id="result" style="display:none;">
    </div>
    <br/>
	</center>
</body>
</html>
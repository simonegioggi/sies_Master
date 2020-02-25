<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.scadenzario.model.ScadenzarioModel"%>
<%@ page import="siap.siep.scadenzario.action.ICostantiScadenzario"%>

<%
//==============================================================================
// Form utilizzata nella ricerca SCADENZARIO
// (In particolare per le Pene Sospese - Termine Ottemperanza Obblighi) 
//==============================================================================
%>

<html>
<head>
  <title> [S.I.E.S.] - Scadenzario : Termini di Ottemperanza Obblighi - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>" ></script>
  <script language="JavaScript">
  
	function verifica()
  	{
      	if(document.f.tipo[1].checked)
      	{
        	if(document.f.<%= ICostantiScadenzario.CAMPO_GIORNI_SCADENZA %>.value == "" && document.f.<%= ICostantiScadenzario.CAMPO_MESI_SCADENZA %>.value == "")
        	{
          		alert("Inserire periodo di ricerca della Scadenza Termini(scadenza entro nn giorni da oggi)");
          		return false;
        	}
      	}
      
    	if(!(document.f.tipo[0].checked || document.f.tipo[1].checked || document.f.tipo[2].checked|| document.f.tipo[3].checked) )
    	{
      		alert("E' obbligatorio selezionare almeno un elemento");
	      	return false;
    	}
    	
    	mostraAttesa('Attendere: elaborazione in corso');
    	return true;
   	}

		// accetta una stringa di testo non html da mostrare
		function mostraAttesa(testo) 
		{
	    	var puntini = 0,
	    	testoIntrattenimento = prendiElementoDaId("testo-temporaneo"),
	    	animaTesto = function() 
	    	{
	      		var testoAggiunto = "";
	      		for(var a = 0; a < puntini; a++)
	        		testoAggiunto += ".";
	      			testoIntrattenimento.nodeValue = testo + testoAggiunto;
		
	      			if(puntini < 4)
	        			puntini++;
	      			else
	       	 			puntini = 0;
		
	      		setTimeout(animaTesto, 300);
	   		}
		
			if(testoIntrattenimento.firstChild) 
			{
		    	animaTesto = function(){};
		    	testoIntrattenimento.removeChild(testoIntrattenimento.firstChild);
			}
			else 
			{
		    	testoIntrattenimento = document.createTextNode(testo);
		    	prendiElementoDaId("testo-temporaneo").appendChild(testoIntrattenimento);
		    	animaTesto();
			}
		
		} // chiude function mostraAttesa	  
		
		function prendiElementoDaId(id_elemento) 
		{
			var elemento;
			if(document.getElementById)
				elemento = document.getElementById(id_elemento);
			else
				elemento = document.all[id_elemento];
			return elemento;
		}
		
		function toggleDiv(id_elemento)
		{			
			var div = prendiElementoDaId(id_elemento);
			
			if( document.f.tipo[1].checked )
			{
			  div.style.display = "block";
			}
			else
			{
			  div.style.display = "none";
			}
		}	
	</script>
 </head>
 
<%
  ScadenzarioModel lModel = new ScadenzarioModel();
%>

<body class="corpo" onLoad="toggleDiv('divAAMMGG');">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f">

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.scadenzario.action.ActRicercaScadenzarioOttemperaObblighi">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Consultazione Scadenzario Termini Ottemperanza obblighi</font></td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2 width="100%">
      <tr>
        <td colspan="4" class="Titolo" width="100%">Consultazione Scadenzario Termini Ottemperanza obblighi</td>
      </tr>
      <tr>
        <td class="l" width="15%">Tutti</td>
        <td class="l" width="15%">
       		<input onClick="toggleDiv('divAAMMGG');" type="radio" name="tipo" value="Tutti" ></td>
       	<td class="l" width="70%" colspan=2></td>
      </tr>
      <tr>
        <td class="l">In scadenza</td>
        <td class="l">
        	<input onClick="toggleDiv('divAAMMGG');" type="radio" name="tipo" value="sette" >
        </td>
        <td class="L" width="10%" id="divAAMMGG" style="display:none">entro:
                 Mesi
                 <input title="Mesi" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getNumMesi()) %>" type="text" name="<%= ICostantiScadenzario.CAMPO_MESI_SCADENZA%>"  >
                 Giorni
                 <input title="Giorni" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getNumGiorni()) %>" type="text" name="<%= ICostantiScadenzario.CAMPO_GIORNI_SCADENZA %>"  >
        </td>
      </tr>
        <tr>
         <td class="l">In scadenza Oggi</td>
         <td class="l">
         		<input onClick="toggleDiv('divAAMMGG');" type="radio" name="tipo" value="oggi" >
         </td>
      </tr>
      <tr>
         <td class="l">Scaduti</td>
         <td class="l">
           <input onClick="toggleDiv('divAAMMGG');" type="radio" name="tipo" value="scaduto" >
         </td>
      </tr>
		<tr><td>&nbsp;</td></tr>
    <tr>
    	<td colspan="2">
      	<INPUT class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="">
      </td>
    </tr>
  </table>
</form>
	 <div> 
	 <table width="100%" >
	  <tr>
	    <td width="35%">
	        &nbsp;    
	    </td>   
	    <td width="30%" class="lrosso">
	        <p id="testo-temporaneo"></p>     
	    </td> 
	    <td width="35%">
	     &nbsp;
	    </td>     
	  </tr>
	 </table>
	</div>
	<script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");
    
    frmvalidator.addValidation("<%=ICostantiScadenzario.CAMPO_MESI_SCADENZA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiScadenzario.CAMPO_GIORNI_SCADENZA%>","numeric");
    
    frmvalidator.setAddnlValidationFunction("verifica");
  </script>
</body>
</html>
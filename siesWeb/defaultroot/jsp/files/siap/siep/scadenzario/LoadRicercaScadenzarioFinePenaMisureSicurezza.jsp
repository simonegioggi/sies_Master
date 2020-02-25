<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: modificata pagina --%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.scadenzario.model.ScadenzarioModel"%>
<%@ page import="siap.siep.scadenzario.action.ICostantiScadenzario"%>

<html>
	<head>
	  	<title> [S.I.E.S.] - Scadenzario Fine Pena per Inizio Misura Sicurezza - </title>
	  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	  	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
	  	<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
	  	<script language="JavaScript">
	  	function verifica() {
	  		if (document.LoadRicercaScadenzarioFinePena.tipo[1].checked) {
	  			if (document.LoadRicercaScadenzarioFinePena.<%=ICostantiScadenzario.CAMPO_ANNI_SCADENZA%>.value == ""
	  					&& document.LoadRicercaScadenzarioFinePena.<%=ICostantiScadenzario.CAMPO_GIORNI_SCADENZA%>.value == ""
	  					&& document.LoadRicercaScadenzarioFinePena.<%=ICostantiScadenzario.CAMPO_MESI_SCADENZA%>.value == "") {
	  				alert("Inserire Periodo Istanze in Scadenza ");
	         		return false;
	         	}
	      	}
	    	if (!(document.LoadRicercaScadenzarioFinePena.tipo[0].checked
	    			|| document.LoadRicercaScadenzarioFinePena.tipo[1].checked
	    			|| document.LoadRicercaScadenzarioFinePena.tipo[2].checked
	    			|| document.LoadRicercaScadenzarioFinePena.tipo[3].checked)) {
	    		alert("E' obbligatorio selezionare almeno un elemento");
	      		return false;
	    	}
	    	return true;
	   	}

	  	function pulisciCampi() {
	  		document.LoadRicercaScadenzarioFinePena.<%=ICostantiScadenzario.CAMPO_ANNI_SCADENZA%>.value = "";
  			document.LoadRicercaScadenzarioFinePena.<%=ICostantiScadenzario.CAMPO_GIORNI_SCADENZA%>.value = "";
  			document.LoadRicercaScadenzarioFinePena.<%=ICostantiScadenzario.CAMPO_MESI_SCADENZA%>.value = "";
	  	}
	  	</script>
 	</head>
<body class="corpo">
  	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaScadenzarioFinePena">
    	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.scadenzario.action.ActRicercaScadenzarioFinePenaMisureSicurezza">
    	<table>
      		<tr>
      			<td class="LBG">
      				<a href="Javascript:window.print();">
      					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
      				</a>
      			</td>
        		<td class="LBG">
        			<font class="label">Funzione: </font>
        			<font class="campo">Consultazione Scadenzario - Data Scadenza Comunicazione Misura Sicurezza</font>
        		</td>
      		</tr>
    	</table>
    	<br>
    	<table cellspacing="2" cellpadding="2" width="100%">
      		<tr>
        		<td colspan="3" class="Titolo" width="100%">Consultazione Scadenzario - Data Scadenza Comunicazione Misura Sicurezza</td>
      		</tr>
      		<tr>
       			<td class="l" width="15%">Tutti</td>
       			<td class="l" colspan="2"><input type="radio" name="tipo" value="Tutti" onclick="pulisciCampi();"></td>
      		</tr>
        	<tr>
        		<td class="l">In scadenza</td>
        		<td class="l" width="5%"><input type="radio" name="tipo" value="sette"></td>
        		<td class="L">entro:
					&nbsp;Anni&nbsp;
                  	<input title="Anni" size="2" maxlength="2" value="" type="text" name="<%=ICostantiScadenzario.CAMPO_ANNI_SCADENZA%>">
                  	&nbsp;Mesi&nbsp;
                  	<input title="Mesi" size="2" maxlength="2" value="" type="text" name="<%=ICostantiScadenzario.CAMPO_MESI_SCADENZA%>">
                  	&nbsp;Giorni&nbsp;
                  	<input title="Giorni" size="2" maxlength="2" value="" type="text" name="<%=ICostantiScadenzario.CAMPO_GIORNI_SCADENZA%>">
              	</td>
      		</tr>
        	<tr>
         		<td class="l">In scadenza Oggi</td>
         		<td class="l" colspan="2"><input type="radio" name="tipo" value="oggi" onclick="pulisciCampi();"></td>
      		</tr>
			<tr>
			   	<td class="l">Scaduti</td>
			   	<td class="l" colspan="2"><input type="radio" name="tipo" value="scaduto" onclick="pulisciCampi();"></td>
			</tr>
			<br>
    		<tr>
          		<td colspan="2">
              		<INPUT class="bottone" type="submit" name="RICERCA" value="Ricerca">
          		</td>
        	</tr>
    	</table>
 	</form>
	<script language="JavaScript" type="text/javascript">
	    var frmvalidator = new Validator("LoadRicercaScadenzarioFinePena");
	    frmvalidator.addValidation("<%=ICostantiScadenzario.CAMPO_ANNI_SCADENZA%>", "numeric");
	    frmvalidator.addValidation("<%=ICostantiScadenzario.CAMPO_MESI_SCADENZA%>", "numeric");
	    frmvalidator.addValidation("<%=ICostantiScadenzario.CAMPO_GIORNI_SCADENZA%>", "numeric");
	    frmvalidator.setAddnlValidationFunction("verifica");
  	</script>
</body>
</html>
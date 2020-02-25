<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina --%>
<%@page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.scadenzario.action.ICostantiScadenzario"%>

<jsp:useBean id="tipo"				scope="request" class="java.lang.String"/>
<jsp:useBean id="giorniScadenza"	scope="request" class="java.lang.String"/>
<jsp:useBean id="mesiScadenza"		scope="request" class="java.lang.String"/>
<jsp:useBean id="anniScadenza"		scope="request" class="java.lang.String"/>

<html>
<head>
  	<title> [S.I.E.S.] - Scadenzario Differimento Misure Sicurezza</title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  	<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
  	<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
  	<script language="JavaScript">
	  	function verifica() {
	    	if (document.LoadRicercaScadenzarioDifferimentoMS.tipo[1].checked) {
	      		if (document.LoadRicercaScadenzarioDifferimentoMS.<%=ICostantiScadenzario.CAMPO_ANNI_SCADENZA%>.value == ""
	      				&& document.LoadRicercaScadenzarioDifferimentoMS.<%=ICostantiScadenzario.CAMPO_GIORNI_SCADENZA%>.value == ""
	      				&& document.LoadRicercaScadenzarioDifferimentoMS.<%=ICostantiScadenzario.CAMPO_MESI_SCADENZA%>.value == "") {
	      			alert("Inserire Periodo Istanze in Scadenza ");
	         		return false;
	      		}
	   	 	}
	    	if (!(document.LoadRicercaScadenzarioDifferimentoMS.tipo[0].checked
	    			|| document.LoadRicercaScadenzarioDifferimentoMS.tipo[1].checked
	    			|| document.LoadRicercaScadenzarioDifferimentoMS.tipo[2].checked
	    			|| document.LoadRicercaScadenzarioDifferimentoMS.tipo[3].checked)) {
	    		alert("E' obbligatorio selezionare almeno un elemento");
	      		return false;
	    	}
	    	return true;
	   	}

	  	function pulisciCampi() {
	  		document.LoadRicercaScadenzarioDifferimentoMS.<%=ICostantiScadenzario.CAMPO_ANNI_SCADENZA%>.value = "";
  			document.LoadRicercaScadenzarioDifferimentoMS.<%=ICostantiScadenzario.CAMPO_GIORNI_SCADENZA%>.value = "";
  			document.LoadRicercaScadenzarioDifferimentoMS.<%=ICostantiScadenzario.CAMPO_MESI_SCADENZA%>.value = "";
	  	}

	  	function riempiCampi() {
			<%
			if (Utils.isPresent(tipo)) { 
				if ("Tutti".equals(tipo)) {
			%>
				document.LoadRicercaScadenzarioDifferimentoMS.tipo[0].checked = true;
			<%
				} else if ("sette".equals(tipo)) {
			%>
				document.LoadRicercaScadenzarioDifferimentoMS.tipo[1].checked = true;
			<%
				} else if ("oggi".equals(tipo)) {
			%>
				document.LoadRicercaScadenzarioDifferimentoMS.tipo[2].checked = true;
			<%
				} else if ("scaduto".equals(tipo)) {
			%>
				document.LoadRicercaScadenzarioDifferimentoMS.tipo[3].checked = true;
			<%
				}
			}
			if (Utils.isPresent(giorniScadenza)) {
			%>
				document.LoadRicercaScadenzarioDifferimentoMS.<%=ICostantiScadenzario.CAMPO_GIORNI_SCADENZA%>.value = <%=giorniScadenza%>;
			<%
			}
			if (Utils.isPresent(mesiScadenza)) {
			%>
				document.LoadRicercaScadenzarioDifferimentoMS.<%=ICostantiScadenzario.CAMPO_MESI_SCADENZA%>.value = <%=mesiScadenza%>;
			<%
			}
			if (Utils.isPresent(anniScadenza)) {
			%>
				document.LoadRicercaScadenzarioDifferimentoMS.<%=ICostantiScadenzario.CAMPO_ANNI_SCADENZA%>.value = <%=anniScadenza%>;
			<%
			}
			%>
	  	}
  	</script>
</head>

<body class="corpo" onload="javascript: riempiCampi();">
  	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaScadenzarioDifferimentoMS">
	    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.scadenzario.action.ActRicercaScadenzarioDifferimentoMisureSicurezza">
	    <table>
	      	<tr>
		        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
		        <td class="LBG">
		        	<font class="label">Funzione: </font>
		        	<font class="campo">Consultazione Scadenzario Differimento Misure Sicurezza</font>
		        </td>
	      	</tr>
		</table>
	    <br>
	    <table cellspacing="2" cellpadding="2" width="100%">
	      	<tr>
	        	<td colspan="3" class="Titolo">Consultazione Scadenzario Differimento Misure Sicurezza</td>
	      	</tr>
	      	<tr>
				<td class="l" width="15%">Tutti</td>
				<td class="l" colspan="2"><input type="radio" name="tipo" value="Tutti" onclick="pulisciCampi();"></td>
	      	</tr>
	      	<tr>
		        <td class="l">In scadenza</td>
		        <td class="l"><input type="radio" name="tipo" value="sette"></td>
		        <td class="L">
		        	entro:&nbsp;Anni
					<input title="Anni" size="2" maxlength="2" value="" type="text" name="<%=ICostantiScadenzario.CAMPO_ANNI_SCADENZA%>" onkeypress="return TicTabNumField(this,event)">
					Mesi
					<input title="Mesi" size="2" maxlength="2" value="" type="text" name="<%=ICostantiScadenzario.CAMPO_MESI_SCADENZA%>" onkeypress="return TicTabNumField(this,event)">
					Giorni
					<input title="Giorni" size="2" maxlength="2" value="" type="text" name="<%=ICostantiScadenzario.CAMPO_GIORNI_SCADENZA%>" onkeypress="return TicTabNumField(this,event)">
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
		</table>
		<br>
		<table>
	      	<tr>
	       		<td class="lNoBord">
	          		<INPUT class="bottone" type="submit" name="RICERCA" value="Ricerca">
	        	</td>
	      	</tr>
    	</table>
	</form>

	<script language="JavaScript" type="text/javascript">
		var frmvalidator = new Validator("LoadRicercaScadenzarioDifferimentoMS");
	    frmvalidator.addValidation("<%=ICostantiScadenzario.CAMPO_ANNI_SCADENZA%>","numeric");
	    frmvalidator.addValidation("<%=ICostantiScadenzario.CAMPO_MESI_SCADENZA%>","numeric");
	    frmvalidator.addValidation("<%=ICostantiScadenzario.CAMPO_GIORNI_SCADENZA%>","numeric");
	    frmvalidator.setAddnlValidationFunction("verifica");
	</script>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina --%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.parametro.action.ICostantiParametro"%>
<jsp:useBean id="anni_scadenza" scope="request" class="java.lang.String" />
<jsp:useBean id="mesi_scadenza" scope="request" class="java.lang.String" />
<jsp:useBean id="giorni_scadenza" scope="request" class="java.lang.String" />


<html>
<head>
  	<title> [S.I.E.S.] - Aggiorna Scadenza Misura di Sicurezza</title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  	<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
  	<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
  	<script language="JavaScript">
	  	function verifica() {
      		if ((document.LoadAggiornaScadenzaMS.<%=ICostantiParametro.CAMPO_ANNI%>.value == ""
      					|| document.LoadAggiornaScadenzaMS.<%=ICostantiParametro.CAMPO_ANNI%>.value == "0")
      				&& (document.LoadAggiornaScadenzaMS.<%=ICostantiParametro.CAMPO_MESI%>.value == ""
      						|| document.LoadAggiornaScadenzaMS.<%=ICostantiParametro.CAMPO_MESI%>.value == "0")
      				&& (document.LoadAggiornaScadenzaMS.<%=ICostantiParametro.CAMPO_GIORNI%>.value == ""
      						|| document.LoadAggiornaScadenzaMS.<%=ICostantiParametro.CAMPO_GIORNI%>.value == "0")) {
      			alert("Attenzione! Inserire almeno un valore tra Anni, Mesi o Giorni");
      			document.LoadAggiornaScadenzaMS.<%=ICostantiParametro.CAMPO_ANNI%>.focus();
         		return false;
      		}
	    	return true;
	   	}
  	</script>
</head>

<body class="corpo">
  	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadAggiornaScadenzaMS">
	    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.scadenzario.action.ActAggiornaScadenzaMisuraSicurezza">
	    <table>
	      	<tr>
		        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
		        <td class="LBG">
		        	<font class="label">Funzione:</font>&nbsp;
		        	<font class="campo">Aggiorna Scadenza Misura di Sicurezza</font>
		        </td>
	      	</tr>
		</table>
	    <br>
	    <table cellspacing="2" cellpadding="2" width="100%">
	      	<tr>
	        	<td colspan="3" class="Titolo">Aggiorna Scadenza Misura di Sicurezza</td>
	      	</tr>
	      	<tr>
		        <td class="l">Determina la Scadenza entro:
		        	&nbsp;Anni&nbsp;
					<input title="Anni" size="2" maxlength="2" value="<%=anni_scadenza%>" type="text" name="<%=ICostantiParametro.CAMPO_ANNI%>" onkeypress="return TicTabNumField(this,event)">
					&nbsp;Mesi&nbsp;
					<input title="Mesi" size="2" maxlength="2" value="<%=mesi_scadenza%>" type="text" name="<%=ICostantiParametro.CAMPO_MESI%>" onkeypress="return TicTabNumField(this,event)">
					&nbsp;Giorni&nbsp;
					<input title="Giorni" size="2" maxlength="2" value="<%=giorni_scadenza%>" type="text" name="<%=ICostantiParametro.CAMPO_GIORNI%>" onkeypress="return TicTabNumField(this,event)">
	        	</td>
	      	</tr>
		</table>
		<br>
		<table>
	      	<tr>
	       		<td class="lNoBord">
	          		<INPUT class="bottone" type="submit" name="Conferma" value="Conferma">
	        	</td>
	      	</tr>
    	</table>
	</form>

	<script language="JavaScript" type="text/javascript">
		var frmvalidator = new Validator("LoadAggiornaScadenzaMS");
	    frmvalidator.addValidation("<%=ICostantiParametro.CAMPO_ANNI%>","numeric");
	    frmvalidator.addValidation("<%=ICostantiParametro.CAMPO_MESI%>","numeric");
	    frmvalidator.addValidation("<%=ICostantiParametro.CAMPO_GIORNI%>","numeric");
	    frmvalidator.setAddnlValidationFunction("verifica");
	</script>
</body>
</html>
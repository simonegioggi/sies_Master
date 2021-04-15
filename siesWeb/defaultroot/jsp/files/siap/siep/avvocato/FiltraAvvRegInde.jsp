<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_21: aggiunta pagina per chiamata a WS per individuare lista avvocato in ReGIndE --%>

<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>

<jsp:useBean id="foro"	scope="request" class="java.lang.String"/>

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>> </script>

<script language="JavaScript">
function trim(string) {
	return string.replace(/(^\s*)|(\s*$)/g,'');
}

function Verify(id) {
	var ritorno = true;
	if ((trim(document.f.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value).length < 2
			|| trim(document.f.<%=ICostantiAvvocato.CAMPO_FORO%>.value).length < 2)
			&& trim(document.f.<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>.value).length != 16) {
    	alert("Occorre inserire il FORO ed almeno 2 caratteri iniziali del COGNOME!");
    	ritorno = false;
  	}
	if (id.id == "sies") {
		document.f.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.avvocato.action.ActRicercaAvvocato";
	} else {
		document.f.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.avvocato.action.ActRicercaAvvocatoRegInde";
	}
  	return ritorno;
}

function gestisciBottoniRicerca() {
	document.f.go.disabled = false;
	document.f.sies.style.visibility = "hidden";
}
</script>

<title>[S.I.E.S.] - Lista Difensori su ReGIndE</title>
</head>

<body class="corpo">
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f" target="listaAvvocatiRegInde">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.avvocato.action.ActRicercaAvvocatoRegInde">
<input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
<input type="HIDDEN" name="modalita" value="LUNGO">
<table style="width: 100%;">
	<tr>
    	<td class=LBG colspan="2"><font class="campo">Filtra la lista per:</font></td>
   	</tr>
	<tr>
    	<td class="l" width="20%">Cognome: </td>
   		<td class="l"><input type="text" name="<%=ICostantiAvvocato.CAMPO_COGNOME%>" value="" onFocus="javascript:gestisciBottoniRicerca();" size="25"></td>
 	</tr>
	<tr>
    	<td class="l">Nome: </td>
   		<td class="l"><input type="text" name="<%=ICostantiAvvocato.CAMPO_NOME%>" value="" onFocus="javascript:gestisciBottoniRicerca();" size="25"></td>
 	</tr>
	<tr id="cf" style="display: none;">
    	<td class="l">Codice Fiscale: </td>
   		<td class="l"><input type="text" name="<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>" value="" onFocus="javascript:gestisciBottoniRicerca();" size="25"></td>
 	</tr>
	<tr>
		<td class="l">Foro:</td>
		<td class="l">
			<select name="<%=ICostantiAvvocato.CAMPO_FORO%>" size="1" onFocus="javascript:gestisciBottoniRicerca();">
				<%=foro%>
     		</select>
     		&nbsp;&nbsp;oppure Tutti i Fori&nbsp;&nbsp;<input type="checkbox" name="<%=ICostantiAvvocato.CAMPO_FLAG_TUTTI_FORI%>" onfocus="javascript:gestisciBottoniRicerca();">
  		</td>
  	</tr>
  	<tr>
		<td colspan="2">
			<input type="submit" id="go" name="go" value="Cerca su ReGIndE" onclick="Javascript:return Verify(this);" class="bottone">
			&nbsp;&nbsp;&nbsp;
			<input style="visibility: hidden;" type="submit" id="sies" name="sies" value="Cerca su Sies" onclick="Javascript:return Verify(this);" class="bottone">
		</td>
	</tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("f");
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
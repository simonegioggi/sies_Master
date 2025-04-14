<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>

<jsp:useBean id="modalita"		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAvvocato" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocato"    	scope="request" class="siap.siep.avvocato.model.AvvocatoModel"/>

<html>
<head>
<title>[S.I.E.S.] - GestioneAvvocato </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript">
var desktop;

function ListaAvvocati(a_formname) {
  	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname, "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=550,height=500");
}

function Verify() {
	var tipo=document.LoadInserisciAvvocato.<%= ICostantiAvvocato.CAMPO_COD_TIPO%>[document.LoadInserisciAvvocato.<%= ICostantiAvvocato.CAMPO_COD_TIPO%>.selectedIndex].value;
   	if (tipo =="-") {
     	alert("Campo Tipo Avvocato è obbligatorio");
     	return false;
    }
    return true;
}

<%-- MEV_21: aggiunta chiamata a WS per individuare lista avvocato in RegInde --%>
function ListaAvvocatiRegInde(a_formname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocatoRegInde&formname="+a_formname,"Ricerca_Avvocato_RegInde","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=550,height=600");
}
</script>
</head>
<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
		<td class=LBG>
			<font class="label">Funzione :</font>&nbsp;&nbsp;
 <%
AvvocatoModel lAvvocato = null;
String lAction = new String();
if ("I".equals(modalita)) {
	lAction = "siap.siep.avvocato.action.ActInserisciAvvocato";
	lAvvocato = new AvvocatoModel();
%>

			<font class="campo">Inserimento Difensore</font>
<%
} else if ("M".equals(modalita)) {

lAction = "siap.siep.avvocato.action.ActModificaAvvocato";
lAvvocato = new AvvocatoModel(avvocato);
%>
			<font class="campo">Modifica Difensore</font>
<%
}
%>
		</td>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciAvvocato">
<%
String lRedir = request.getParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE);
if (lRedir != null && !"".equals(lRedir)) {
%>
<input type=hidden name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value="<%=lRedir%>">
<%
}
%>
<table cellspacing=2 cellpadding=2>
  	<tr>
    	<td class="l">
    		<%-- MEV_21: aggiunta chiamata a WS per individuare lista avvocato in RegInde --%>
<!--       		<a href="Javascript:ListaAvvocati('LoadInserisciAvvocato');"> -->
<!--         		Seleziona dalla lista <img src="/images/filefolder.gif" border=0> -->
<!--       		</a> -->
			<a href="Javascript:ListaAvvocatiRegInde('LoadInserisciAvvocato');">
         		Seleziona da RegInde <img src="/images/filefolder.gif" border=0>
       		</a>
    	</td>
  	</tr>
</table>

<table cellspacing=2 cellpadding=2>
	<tr>
		<td class="l">Cognome <font class=ob>(*)</font></td>
		<td class="l"><input  size=35 maxlength=35 value="<%=lAvvocato.getCognome()%>" title="Cognome" type="text" name="<%=ICostantiAvvocato.CAMPO_COGNOME %>"></td>
	</tr>
	<tr>
		<td class="l">Nome</td>
		<td class="l"><input size=35 maxlength=35 value="<%=lAvvocato.getNome()%>" title="Nome" type="text" name="<%=ICostantiAvvocato.CAMPO_NOME %>"></td>
	</tr>
	<tr>
		<td class="l">Foro <font class=ob>(*)</font></td>
		<td class="l"><input value="<%=lAvvocato.getForo() %>" title="Foro" type="text" name="<%=ICostantiAvvocato.CAMPO_FORO%>"></td>
	</tr>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="l"><input size=80 maxlength=200 value="<%=lAvvocato.getIndirizzo()%>" title="Indirizzo" type="text" name="<%=ICostantiAvvocato.CAMPO_INDIRIZZO %>"></td>
	</tr>
	<tr>
		<td class="l">Telefono</td>
		<td class="l"><input size=12 maxlength=12 value="<%=lAvvocato.getTelefono()%>" title="Telefono" type="text" name="<%=ICostantiAvvocato.CAMPO_TELEFONO %>"></td>
	</tr>
	<tr>
		<td class="l">Fax</td>
		<td class="l"><input size=12 maxlength=12 value="<%=lAvvocato.getFax()%>" title="Fax" type="text" name="<%=ICostantiAvvocato.CAMPO_FAX %>"></td>
	</tr>
	<tr>
		<td class="l">e-mail</td>
		<td class="l"><input size=50 maxlength=50 value="<%=lAvvocato.getEMail()%>" title="e-mail" type="text" name="<%=ICostantiAvvocato.CAMPO_E_MAIL %>"></td>
	</tr>
	<%-- MEV_21: aggiunto campo per chiamata a WS per individuare lista avvocato in RegInde --%>	
    <tr>
		<td class="l">pec</td>
		<td class="l"><input size=50 maxlength=50 value="<%=StringUtils.toStringJSP(lAvvocato.getPec())%>" title="pec" type="text" name="<%=ICostantiAvvocato.CAMPO_PEC%>"></td>
	</tr>
	<tr>
		<td class="l">Codice Fiscale</td>
		<td class="l"><input size=20 maxlength=16 value="<%=lAvvocato.getCodiceFiscale()%>" title="Codice Fiscale" type="text" name="<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE %>"></td>
	</tr>
	<tr>
		<td class="l">Tipo <font class=ob>(*)</font></td>
		<td class="l">
			<select  name="<%=ICostantiAvvocato.CAMPO_COD_TIPO%>">
				<%=tipoAvvocato%>
			</select>
		</td>
	</tr>
  	<tr>
      	<td colspan=2>
      		<input value="<%=StringUtils.zeroIfNull(lAvvocato.getIdAvvocato())%>" type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO %>">
			<input type="Hidden" name="Action" value="<%=lAction%>">
    		<input class=bottone  type="submit" value="Conferma" >
    	</td>
	</tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadInserisciAvvocato");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_COGNOME %>","req","Il campo Cognome Avvocato è obbligatorio");
// frmvalidator.addValidation("<-%= ICostantiAvvocato.CAMPO_COGNOME %->","alpha");

// frmvalidator.addValidation("<-%= ICostantiAvvocato.CAMPO_NOME %->","req","Il campo Nome Avvocato è obbligatorio");
// frmvalidator.addValidation("<-%= ICostantiAvvocato.CAMPO_NOME %->","alpha");

frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_FORO %>","req","Il campo Foro  è obbligatorio");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_FORO %>","alpha");

frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
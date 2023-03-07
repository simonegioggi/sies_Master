<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-13_ aggiunta pagina di caricamento dati civilmente obbligato --%>
<%@page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.pagoPA.action.ICostantiPagoPA"%>

<jsp:useBean id="modalita" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="codPersona"  	scope="request" class="java.lang.String"/>

<%
String titolo = "";
if (modalita.equals("I")) {
	titolo = "Inserimento";
} else if (modalita.equals("M")) {
	titolo = "Modifica";
}
%>

<html>
<head>
<title>[S.I.E.S.] - <%=titolo%> Civilmente Obbligato Pena Pecuniaria</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=ICostantiPagoPA.JS_TIPO_PERSONA%>"></script>

<script language="JavaScript">
function Init() {
	alert("ciao " + <%=codPersona%>);
<%
if (Utils.isPresent(codPersona)) {
%>
	alert(<%=codPersona%>);
	document.LoadInserisciCivilmenteObbligato.<%=ICostantiPagoPA.RADIO_COD_PERSONA%>[0].checked = true;
	if ("G" == <%=codPersona%>)
		document.LoadInserisciCivilmenteObbligato.<%=ICostantiPagoPA.RADIO_COD_PERSONA%>[1].checked = true;
<%
}
%>
	if (document.LoadInserisciCivilmenteObbligato.<%=ICostantiPagoPA.RADIO_COD_PERSONA%>[0].checked) {
		VisualizzaPersonaFisica();
    } else {
    	VisualizzaPersonaGiuridica();
    }
}
</script>
</head>

<body class="corpo" onLoad="Init();VisualizzaSecondoTutore();">
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciCivilmenteObbligato">
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo"><%=titolo%> Civilmente Obbligato Pena Pecuniaria</font></td>
  		<!-- BOTTONE DI RITORNO -->
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
</table>

<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="c">
			Persona Fisica <input type="radio" name="<%=ICostantiPagoPA.RADIO_COD_PERSONA%>" value="F" onClick="VisualizzaPersonaFisica();" checked>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
		   	Persona Giuridica <input type="radio" name="<%=ICostantiPagoPA.RADIO_COD_PERSONA%>" value="G" onClick="VisualizzaPersonaGiuridica();">
      	</td>
	</tr>
</table>
</FORM>

<div id="tipoPersona" style="position: relative; top: 0; left: 0; visibility: visible;">
	<div id="PersonaFisicaDiv" style="position: relative; top: 0; left: 0; visibility: visible; width: 100%;">
		<jsp:include page="<%=ICostantiPagoPA.DIV_PERSONA_FISICA%>"/>
  	</div>
  	<div id="PersonaGiuridicaDiv" style="position: absolute; top: 0; left: 0; visibility: hidden; width: 100%;">
    	<jsp:include page="<%=ICostantiPagoPA.DIV_PERSONA_GIURIDICA%>"/>
  	</div>
</div>
</body>
</html>
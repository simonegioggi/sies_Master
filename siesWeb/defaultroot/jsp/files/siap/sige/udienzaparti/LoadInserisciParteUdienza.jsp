<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@ page import="siap.sige.udienzaparti.action.ICostantiPartiUdienza"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>

<jsp:useBean id="codTipoParte" scope="request" class="java.lang.String" />
<jsp:useBean id="idEventoUdienza" scope="request" class="java.lang.String" />
<jsp:useBean id="idUdienzaSige" scope="request" class="java.lang.String" />
<jsp:useBean id="idUdienzaProcedimentoSige" scope="request" class="java.lang.String" />

<html>
<head>
<title>[S.I.E.S.] - Inserimento Parte Offesa/Civile</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=ICostantiPartiUdienza.JS_PARTE_UDIENZA%>"></script>

<script language="JavaScript">
	function Init()
	{
		if (document.LoadInserisciParteUdienza.<%=ICostantiPartiUdienza.RADIO_COD_PARTE%>[0].checked) {
			VisualizzaPersonaFisica();
	    } else {
	    	VisualizzaPersonaGiuridica();
	    }
	}
</script>

</head>

<%
  // Variabile utilizzata per distinguere se trattasi di Parte Offesa oppure Parte Civile
  String tipoParte = "";
  if (codTipoParte != null && codTipoParte.equalsIgnoreCase("O")){
	  tipoParte = "Offesa";
  } else {
	  tipoParte = "Civile";
  }
%>

<body class="corpo" onLoad="Init();">

	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"
		name="LoadInserisciParteUdienza">
		<table>
			<tr>
				<td class="LBG"><a href="Javascript:window.print();"><img
						align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
						alt="Stampa questa videata" border=0></a></td>
				<td class="LBG"><font class="label">Funzione :</font>&nbsp;<font
					class="campo">Inserimento Parte <%=tipoParte%></font></td>

				<!-- BOTTONE DI RITORNO -->
				<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>" />
			</tr>
		</table>

		<jsp:include
			page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>" />

		<table width="87%">
			<tr>
				<td class="c">Persona Fisica <input type="radio"
					name="<%=ICostantiPartiUdienza.RADIO_COD_PARTE%>" value="F"
					onClick="VisualizzaPersonaFisica();" checked>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					Persona Giuridica <input type="radio"
					name="<%=ICostantiPartiUdienza.RADIO_COD_PARTE%>" value="G"
					onClick="VisualizzaPersonaGiuridica();">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
		</table>

	</FORM>

	<div id="parteUdienza"
		style="position: relative; top: 0; left: 0; visibility: visible;">
		<div id="PersonaFisicaDiv"
			style="position: relative; top: 0; left: 0; visibility: visible;">
			<jsp:include page="<%=ICostantiPartiUdienza.DIV_PERSONA_FISICA%>" />
		</div>
		<div id="PersonaGiuridicaDiv"
			style="position: absolute; top: 0; left: 0; visibility: hidden;">
			<jsp:include page="<%=ICostantiPartiUdienza.DIV_PERSONA_GIURIDICA%>" />
		</div>
	</div>

</body>
</html>
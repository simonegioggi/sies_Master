<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="strFunzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloNotInSession" scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - <%=strFunzione%></title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript1.2">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
function over_effect(e, state) {
	if (document.all)
    	source4 = event.srcElement;
  	else if (document.getElementById)
    	source4 = e.target;
  	if (source4.className == "menulines")
    	source4.style.borderStyle = state;
  	else {
    	while (source4.tagName != "TABLE") {
      		source4 = document.getElementById ? source4.parentNode : source4.parentElement;
      		if (source4.className == "menulines")
        		source4.style.borderStyle = state;
    	}
  	}
}
</script>

<STYLE>
.menulines {
	border:2.5px solid #BEC6FC;
	text-align : center;
	font-family: 'Tahoma';
	color : Navy;
	font-size : 13px;
	text-decoration : none;
	height:100%;
}

.menulines a {
	text-align : center;
	text-decoration:none;
	color:black;
	font-family: 'Tahoma';
	color : Navy;
	font-size : 13px;
	width:100%;
	height:100%;
}
</STYLE>
</head>

<body class="corpo">
<table>
  	<tr>
    	<td class="LBG">
			<font class="label">Funzione :</font>&nbsp;
			<font class="campo"><%=strFunzione%></font>
    	</td>
	</tr>
</table>
<br>
<%
// Se il Fascicolo è in Sessione fa l'include del DettaglioSoggettoSentenza
if (!fascicoloNotInSession.equals("S")) {
%>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<%
}
%>
<br>
<table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
	<tr>
  		<td colspan=3 class="Titolonocap">Altre Sanzioni</td>
	</tr>
	<tr>
  		<td width="32%" class="menulines" nowrap>
    		<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActGestioneSanzioniSostitutive">Sanzioni Sostitutive</a>
		</td>      
		<td width="32%" class="menulines" nowrap>
  			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActGestioneMisureSicurezza">Misure Sicurezza</a>
		</td>
		<td width="32%" class="menulines" nowrap>
  			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">Sanzioni Giudice di Pace</a>
  		</td>
	</tr>
	<tr>
  		<td width="32%" class="menulines" nowrap>
    		<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">Sanzioni Amministrative da Reato</a>
		</td>      
		<td width="32%" class="menulines" nowrap>
  			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">Pene Sospese</a>
		</td>
		<td width="32%" class="menulines" nowrap>
  			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">Sanzioni Amministrative</a>
  		</td>
	</tr>
	<tr>
  		<td width="32%" class="menulines" nowrap>
    		<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penapecuniaria.action.ActGestioneConversione">Conversione Pene Pecuniarie</a>
		</td>
	</tr>
</table>
<br>
<table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
	<tr>
      	<td colspan="2" class="Titolonocap">Pene Pecuniarie / Pene Sostitutive Brevi</td>
    </tr>
    <tr>
		<td width="48%" class="menulines" nowrap>
  			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActGrigliaRiscossionePP">Riscossione Pene Pecuniarie</a>
		</td>
      	<td width="48%" class="menulines" nowrap>
			<%-- Ticket#20230628013 - 12.5.1.0 correzione su voce "Esecuzione Pene Sostitutive Brevi" --%>
			<%-- Attivato con la MEV_2023-33 step 1 --%>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActGrigliaEsecuzionePeneSostitutiveBrevi">Esecuzione Pene Sostitutive Brevi</a>
    	</td>
  	</tr>
  <tr>
    <td width="48%" class="menulines" nowrap>
      <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.pagoPA.action.ActVerificaErroriPagopa">Cruscotto Errori su PagoPa Pene Pecuniarie</a>
    </td>
  </tr>  
</table>
</body>
</html>
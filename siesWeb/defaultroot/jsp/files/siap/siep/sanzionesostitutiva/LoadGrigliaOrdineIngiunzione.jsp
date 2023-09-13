<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-13: aggiunta pagina di caricamento griglia degli Ordini di Ingiunzione --%>
<%@ page import="f3b.web.IWebConstants"%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Ordine Ingiunzione</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript1.2">
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
	        <font class="campo">Gestione Ordine di Ingiunzione</font>
      	</td>
        <td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActGrigliaRiscossionePP">
            <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>      
      </td>       	
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
	<tr>
  		<td width="32%" class="menulines" nowrap>
    		<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActLoadNotificheOrdineIngiunzione">Notifiche</a>
		</td>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActLoadInserisciRinnovoRicercheOIPP">Rinnovo Ricerche per Omesse Notifiche</a>
		</td>
		<td width="32%" class="menulines" nowrap>
  			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActLoadInserisciRichInfoComma5">Richiesta Informazioni comma 5</a>
		</td>
 	</tr>
	<tr>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActLoadInserisciRinnovazioneOIPP">Rinnovazione Notifica successiva alla Richiesta Informazioni</a>
		</td>
		<td width="32%" class="menulines" nowrap>
	  		<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">Solleciti</a>
		</td>
	</tr>
</table>
</body>
</html>
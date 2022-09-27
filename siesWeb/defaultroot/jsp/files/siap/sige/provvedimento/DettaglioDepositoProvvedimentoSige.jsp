<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Stack"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sius.documentoallegato.action.ICostantiDocumentoAllegato"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>

<jsp:useBean id="ProvvedimentoEvento"	scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"/>
<jsp:useBean id="documentoAllegato" 	scope="request" class="siap.sius.documentoallegato.model.DocumentoAllegatoModel"/>
<jsp:useBean id="StackDiRitorno" 		scope="session" class="java.util.Stack"/>
<jsp:useBean id="notifiche" 			scope="request" class="java.util.Vector"/>
<jsp:useBean id="TornaQui"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"     			scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="Trasferibile" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="Stampabile" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="Modificabile" 			scope="request" class="java.lang.String"/>

<%
boolean flagChiaveDecreto = true;
String codiceEsito = evento.getCodEsito();
if (codiceEsito != null && codiceEsito.equals("0601"))
	flagChiaveDecreto = false;
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Deposito <%=ProvvedimentoEvento.getProvvedimento().getDescrTipoProvvedimento()%></title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
<script language="JavaScript">	
function lookUpload() {
	var node;
	node = document.getElementById('upld');
	node.style.visibility = 'visible';
}
</script>
</head>

<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
        <td class="LBG">
        	<font class="label">Funzione : </font>
        	<font class="campo">Dettaglio Deposito <%=ProvvedimentoEvento.getProvvedimento().getDescrTipoProvvedimento() %></font>&nbsp;
<%
// presenza del Link per il bottone di ritorno
boolean retFlag = false;
retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
// Abilitazione pulsante di stampa
if (Stampabile.compareTo("SI") == 0) {
%>
			<!-- BOTTONE DI STAMPA -->
			<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIGE%>">
				<jsp:param name="CampoIdEntita" value="IdDocumentoAllegato"/>
				<jsp:param name="ValoreIdEntita" value="<%=documentoAllegato.getIdDocumentoAllegato()%>"/>
				<jsp:param name="IdEvento" value="<%=ProvvedimentoEvento.getProvvedimento().getIdEventoGenerato()%>"/>
			</jsp:include>
<% 
}
// Nel caso di Evento già validato con stampa, si consente di riprodurre il documento.  		
if (Stampabile.compareTo("NO") == 0
		&& ProvvedimentoEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() != null
		&& ProvvedimentoEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0) {
%>
			<!-- BOTTONE DI DOWNLOAD DOCUMENTO -->
		<td class="LBG">
   			<a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sius.documentoallegato.action.ActLoadDocumentoAllegato&<%=ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO%>=<%=documentoAllegato.getIdDocumentoAllegato()%>')">
    			<img src="/images/print24.gif" alt="Visualizza Deposito" width="24" height="24" border="0">
    		</a>
  		</td>
<%
}
// Abilitazione pulsanti alla modifica.
if (Modificabile.compareTo("SI") == 0) {
%>    
		<td class="LBG">
			<a href="/jsp/Main.jsp?Action=siap.sige.provvedimento.action.ActLoadInserisciDataDeposito&Aggiungi=yes&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=ProvvedimentoEvento.getProvvedimento().getIdEventoGenerato()%>&<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>=<%=ProvvedimentoEvento.getProvvedimento().getIdProvvedimentoSige()%><%=retParam%>">
		  		<img src="/images/new24.gif" alt="Iscrizione Altri Destinatari" width="24" height="24" border="0">
		  	</a>
		</td>
		<td class="LBG">
			<a href="/jsp/Main.jsp?Action=siap.sige.provvedimento.action.ActLoadModificaDataDeposito&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=ProvvedimentoEvento.getProvvedimento().getIdEventoGenerato()%>&<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>=<%=ProvvedimentoEvento.getProvvedimento().getIdProvvedimentoSige()%>&Aggiungi=yes<%=retParam%>">
		  		<img src="/images/modifica24.gif" alt="Modifica Deposito Provvedimento" width="24" height="24" border="0">
		  	</a>
		</td>
<%
}
// Abilitazione al trasferimento.
// Ticket#202208040112 - SIGE - Trasferimento ordinanza	--> OSCURATA ICONA
// Ticket#202209260112 - Patch mensili - Test- SIES versione 12.4.23.0 ticket 202208040112 - Sige Trasferimento ordinanza --> RIPRISTINO ICONA
if (Trasferibile.compareTo("SI") == 0) {
%>
		<!-- BOTTONE DI TRASFERIMENTO -->
		<td class="LBG">
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.provvedimento.action.ActLoadTrasferisciProvvedimento&<%=ICostantiProvvedimentoSige.CAMPO_ID_EVENTO_GENERATO%>=<%=ProvvedimentoEvento.getProvvedimento().getIdEventoGenerato()%>">
				<img src="<%=IWebConstants.IMAGES_DIR%>net24.gif" alt="Trasferisci" width="24" height="24" border="0">
		  	</a>
		</td>
<%
}
%>
		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	</tr>
	<tr>
    	<jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
  	</tr>
</table>

<table cellspacing="4" cellpadding="4">
	<tr>
    	<td>&nbsp;
    		<input Title="Id Evento" type="hidden" name="<%= ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=ProvvedimentoEvento.getProvvedimento().getIdEventoGenerato()%>">
    	</td>
  	</tr>
<%
if (flagChiaveDecreto) {
%>
	<tr>
	    <td class="l">Anno / Numero <%=ProvvedimentoEvento.getProvvedimento().getDescrTipoProvvedimento()%></td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(ProvvedimentoEvento.getProvvedimento().getChiaveAnno())%> / <%=StringUtils.toStringJSP(ProvvedimentoEvento.getProvvedimento().getChiaveProgr())%></font></td>
  	</tr>
<%
}
%>
	<tr>
	    <td class="l"> Data Emissione</td>
	    <td class="l"> <font class="campo"> <%=DateUtils.getDateToString(ProvvedimentoEvento.getProvvedimento().getDataEmissione(),"dd/MM/yyyy")%></font></td>
  	</tr>
  	<tr>
	    <td class="l"> Data Deposito in Cancelleria</td>
	    <td class="l"><font class="campo">  <%=DateUtils.getDateToString(ProvvedimentoEvento.getProvvedimento().getDataDeposito(),"dd/MM/yyyy")%></font></td>
  	</tr>
  	<tr>
    	<td class="l"> Stato del deposito</td>
<% 
if (evento.getFlagDocumentoRegistrato() != null && evento.getFlagDocumentoRegistrato().equalsIgnoreCase("A")) { 
%>
    	<td class="l"><font class="cRosso">ANNULLATO</font></td>
<% 
} else if (documentoAllegato.getFlagDocumentoRegistrato() != null && documentoAllegato.getFlagDocumentoRegistrato().compareTo("S") == 0) {
%>
    	<td class="l"><font class="campo">Validato</font></td>
<%
} else {
%>
    	<td class="l"><font class="campo">Da Validare</font></td>
<% 
} 
%>
	</tr>
<%
Iterator itx2 = notifiche.iterator();
if (itx2.hasNext()) {
	NotificaModel notifica = (NotificaModel) itx2.next();
%>
	<tr>
	    <td class="l">Data Trasferimento Atti</td>
	    <td class="l"><font class="campo"><%=DateUtils.getDateToString(notifica.getDataInvio() ,"dd/MM/yyyy")%></font></td>
    </tr>
<%
}
%>
	<tr>
      	<td colspan=2>&nbsp;</td>
  	</tr>
</table>
<%
if (notifiche != null && !notifiche.isEmpty()) {
%>
<jsp:include page="<%=ICostantiUdienzaSige.PG_LOAD_DESTINATARI%>"/>
<%
}
%>
<div align="left" style="visibility:hidden" id="upld">
<FORM name="comandi" enctype="multipart/form-data" method="post">
<table>
	<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>"/>
	<tr>
		<td class="L">
      		<input class="bottone" type="submit" value="Conferma">
        	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.documentoallegato.action.ActUploadDocumentoAllegato">
          	<input type="HIDDEN" name="<%=ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO%>" value="<%=documentoAllegato.getIdDocumentoAllegato()%>">
          	<input type="HIDDEN" name="<%=ICostantiDocumentoAllegato.CAMPO_AZIONE_DETTAGLIO%>" value="siap.sige.provvedimento.action.ActLoadDettaglioDataDeposito">
      	</td> 
	</tr>
</table>
</FORM>
</div>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.siepe.attivita.model.AttivitaModel"%>
<%@ page import="siap.siepe.assistentesociale.model.AssistenteSocialeModel"%>
<%@ page import="siap.siepe.attivita.action.ICostantiAttivita"%>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe"%>
<%@ page import="siap.siepe.relazione.action.ICostantiRelazione"%>
<%@ page import="siap.siepe.espertoattivita.action.ICostantiEspertoAttivita"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>

<jsp:useBean id="attivita" 	      scope="request" class="siap.siepe.attivita.model.AttivitaModel"/>
<jsp:useBean id="assistentesociale"	scope="request"	class="siap.siepe.assistentesociale.model.AssistenteSocialeModel"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="Upload" scope="request" class="java.lang.String"/>
<jsp:useBean id="Modificabile" scope="request" class="java.lang.String"/>

<jsp:useBean id="UtenteProprietario" scope="request" class="java.lang.String"/>


<%
String NomeFunzione = "Dettaglio Attivita";

boolean isTrasferimento = false;
boolean isChiusura = false;
boolean isRelazione = false;
boolean isEsperto = false;
boolean isAssistente = false;
boolean isProprietario = false;

if (  UtenteProprietario.equalsIgnoreCase("SI"))
	isProprietario = true;


if (   modalita.equalsIgnoreCase("T"))
{
    NomeFunzione = "Trasferimento Attivita";
    isTrasferimento = true;
}
else if (modalita.equalsIgnoreCase("C"))
{
    NomeFunzione = "Chiusura Attivita";
    isChiusura = true;
}
else if (modalita.equalsIgnoreCase("R"))
{
    NomeFunzione = "Inserimento Relazione per Attivita";
    isRelazione = true;
}
else if (modalita.equalsIgnoreCase("E"))
{
    NomeFunzione = "Gestione Esperti per Attivita";
    isEsperto = true;
}
else if (modalita.equalsIgnoreCase("A"))
{
    NomeFunzione = "Elenco Storico Assistenti Sociali per Attivita";
    isAssistente = true;
}


%>

<html>
	<head>
		<title>[S.I.A.P.] - Dettaglio Attivita </title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<script language="JavaScript" src="/html/conferma.js"></script>
	</head>

<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo"><%=NomeFunzione%></font>
      </td>
  	<!-- BOTTONE DI STAMPA -->
    		<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIUS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiAttivita.CAMPO_ID_ATTIVITA%>" />
          <jsp:param name="ValoreIdEntita" value="<%=attivita.getIdAttivita()%>"/>
        </jsp:include>
 <%if (isProprietario) {%>       
  	<!-- BOTTONE DI Modifica e Cancellazione -->
    		<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_MOD_CANC%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiAttivita.CAMPO_ID_ATTIVITA%>" />
          <jsp:param name="ValoreIdEntita" value="<%=attivita.getIdAttivita()%>"/>
        </jsp:include>
  <%} %>
  		<!-- BOTTONE DI Inserimento Relazione -->
    	<%--
				<jsp:include page="<%=ICostantiRelazione.PG_BUTTON_INSERISCIRELAZIONE%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiAttivita.CAMPO_ID_ATTIVITA%>" />
          <jsp:param name="ValoreIdEntita" value="<%=attivita.getIdAttivita()%>"/>
        </jsp:include>
    	--%>
      <td class="LBG">
    	<!-- Combo List delle Funzioni -->
    		<jsp:include page="<%=IWebConstants.PG_TOOLBAR_COMBO%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiAttivita.CAMPO_ID_ATTIVITA%>" />
          <jsp:param name="ValoreIdEntita" value="<%=attivita.getIdAttivita()%>"/>
					<jsp:param name="Modificabile" value="<%=Modificabile%>"/>
       </jsp:include>
      </td>
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
   </tr>
 </table>
 	 <br>
    <jsp:include page="<%=ICostantiFascicoloSiepe.PG_SINTESI_SOGG_FASCICOLI%>"/>

	</FORM>
	<table cellspacing=4 cellpadding=4>
		<tr>
				<td class="l">Data Inizio</td>
				<td class="l"><font class="campo"><%=DateUtils.getDateToString(attivita.getDataInizio(),"dd-MM-yyyy")%> </font></td>
		</tr>
<% if (!isChiusura) { %>
		<tr>
				<td class="l">Data Chiusura</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(attivita.getDataChiusura(),"dd-MM-yyyy"),"-")%> </font></td>
		</tr>
<% } %>
		<tr>
				<td class="l">Tipo Attività</td>
				<td class="l"><font class="campo"><%=attivita.getDescrTipoAttivita()%></font></td>
		</tr>
<% if (!isChiusura) { %>
		<tr>
				<td class="l">Esito Attività</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(attivita.getDescrEsitoAttivita(),"-")%></font></td>
		</tr>
		<tr>
				<td class="l">Nota di Chiusura</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP( attivita.getNotaChiusura())%></font></td>
		</tr>

<% } %>
		<tr>
				<td class="l">Assistente Sociale</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(assistentesociale.getCognome(),"") + " " + StringUtils.toStringJSP(assistentesociale.getNome(),"")%></font></td>
		</tr>
		<tr>
				<td class="l">Note</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP( attivita.getNote()) %></font></td>
		</tr>
	</table>
<% if(!isEsperto) { %>
    <jsp:include page="<%=ICostantiEspertoAttivita.PG_ELENCO_ESPERTI_ATTIVI%>"/>
<% } %>
    <jsp:include page="<%=ICostantiRelazione.PG_ELENCO_RELAZIONI%>"/>

<% if(isTrasferimento) { %>
    <jsp:include page="<%=ICostantiAttivita.PG_LOAD_TRASFERISCIATTIVITA%>"/>
<%} else if(isChiusura) { %>
    <jsp:include page="<%=ICostantiAttivita.PG_LOAD_CHIUSURA_ATTIVITA%>"/>
<%} else if(isRelazione) { %>
		<jsp:include page="<%=ICostantiRelazione.PG_LOAD_INSERISCI_RELAZIONE%>">
			<jsp:param name="keyField" value="<%=ICostantiRelazione.CAMPO_ATT_ID_ATTIVITA%>"/>
			<jsp:param name="id" value="<%=attivita.getIdAttivita()%>"/>
			<jsp:param name="dateRef" value="<%=DateUtils.getDateToString(attivita.getDataInizio(),"dd/MM/yyyy")%>"/>
		</jsp:include>
<%} else if(isEsperto) { %>
    <jsp:include page="<%=ICostantiAttivita.PG_LOAD_GESTIONE_ESPERTI%>"/>
<%} else if(isAssistente) { %>
    <jsp:include page="<%=ICostantiAttivita.PG_ELENCO_ASSISTENTI_SOCIALI%>"/>
<% }
   else
   { // La combo dei template viene visualizzata solo se si può effettuare una nuova stampa
     if (Upload.length() < 2)
   {
  %>
 <form name="dettaglio">
      <jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
	</form>
<% } %>
  <jsp:include page="<%=ISIAPCostantiWeb.DIV_FORM_UPLOAD%>">
    <jsp:param name="Azione" value="siap.siepe.attivita.action.ActUploadDocument"/>
    <jsp:param name="Id" value="<%=attivita.getIdAttivita()%>" />
  </jsp:include>
<%} %>
</body>
</html>
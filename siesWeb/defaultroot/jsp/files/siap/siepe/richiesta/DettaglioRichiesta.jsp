<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.siepe.richiesta.model.RichiestaModel"%>
<%@ page import="siap.siepe.richiesta.action.ICostantiRichiesta"%>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe"%>
<%@ page import="siap.siepe.relazione.action.ICostantiRelazione"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.siepe.attivita.action.ICostantiAttivita"%>

<jsp:useBean id="richiesta"	scope="request" class="siap.siepe.richiesta.model.RichiestaModel"/>
<jsp:useBean id="validata" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="Modificabile" scope="request" class="java.lang.String"/>
<jsp:useBean id="Upload" scope="request" class="java.lang.String"/>

<%
	String lNomeFunzione = "Dettaglio Richiesta"; // Impostazione di default ( Dettaglio Richiesta )
	boolean isTrasferimento = false; // Impostazione di default ( false )
	boolean isRelazione = false; // Impostazione di default ( false )

	// Verifica la Modalità ( T -> Trasmissione )
	if ( modalita.equalsIgnoreCase("T"))
	{
    lNomeFunzione = "Trasferimento Richiesta"; isTrasferimento = true;
	}
	// Verifica la Modalità ( R -> Relazione )
	else if (modalita.equalsIgnoreCase("R"))
	{
    lNomeFunzione = "Inserimento Relazione per Richiesta"; isRelazione = true;
	}
%>

<html>
	<head>
		<title>[S.I.E.S.] - <%= lNomeFunzione %></title>
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
        		<font class="campo"><%= lNomeFunzione %></font>
      		</td>
  				<!-- BOTTONE DI STAMPA -->
    			<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIUS%>">
          	<jsp:param name="CampoIdEntita" value="<%=ICostantiRichiesta.CAMPO_ID_RICHIESTA%>" />
          	<jsp:param name="ValoreIdEntita" value="<%=richiesta.getIdRichiesta()%>"/>
      		</jsp:include>

        	<!-- BOTTONI DI Modifica e Cancellazione -->
    			<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_MOD_CANC%>">
          	<jsp:param name="CampoIdEntita" value="<%=ICostantiRichiesta.CAMPO_ID_RICHIESTA%>" />
          	<jsp:param name="ValoreIdEntita" value="<%=richiesta.getIdRichiesta()%>"/>
          </jsp:include>

  				<!-- BOTTONE DI Inserimento Relazione -->
    			<%--
          <jsp:include page="<%=ICostantiRelazione.PG_BUTTON_INSERISCIRELAZIONE%>">
          	<jsp:param name="CampoIdEntita" value="<%=ICostantiRichiesta.CAMPO_ID_RICHIESTA%>" />
          	<jsp:param name="ValoreIdEntita" value="<%=richiesta.getIdRichiesta()%>"/>
          </jsp:include>
					--%>
					<td class="LBG">
          	<!-- Combo List delle Funzioni -->
          	<jsp:include page="<%=IWebConstants.PG_TOOLBAR_COMBO%>">
          		<jsp:param name="CampoIdEntita" value="<%=ICostantiRichiesta.CAMPO_ID_RICHIESTA%>" />
          		<jsp:param name="ValoreIdEntita" value="<%=richiesta.getIdRichiesta()%>"/>
							<jsp:param name="Modificabile" value="<%=Modificabile%>"/>
       			</jsp:include>
					</td>
					<!-- Inserisce il pulsante di ritorno -->
					<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    		</tr>
 			</table>
			<!-- Include la sintesi del Soggetto/Fascicolo -->
    	<jsp:include page="<%=ICostantiFascicoloSiepe.PG_SINTESI_SOGG_FASCICOLI%>"/>
		</FORM>

		<table cellspacing="4" cellpadding="4">
			<tr>
      	<td class="l">Stato della Richiesta </td>
				<td class="l"><font class="campo"><%=( validata.equalsIgnoreCase("SI") ? "VALIDATA" : "NON VALIDATA" )%> </font></td>
			</tr>

			<tr>
				<td class="l">Data Richiesta </td>
				<td class="l"><font class="campo"><%=DateUtils.getDateToString(richiesta.getDataRichiesta(),"dd-MM-yyyy")%> </font></td>
			</tr>

			<tr>
				<td class="l">Tipo Richiesta </td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(richiesta.getDescrTipoRichiesta())%></font></td>
			</tr>

			<tr>
				<td class="l">Presentata da </td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(richiesta.getDescrTipoRichiedente())%></font></td>
			</tr>

			<tr>
				<td class="l">Ufficio Destinatario </td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(richiesta.getDescrUfficioDestinatario()) + " - " + StringUtils.toStringJSP(richiesta.getDescrSedeUfficioDestinatario()) %></font></td>
			</tr>

    	<tr>
				<td class="l">Note </td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP( richiesta.getNote() )%></font></td>
			</tr>
		</table>

  	<jsp:include page="<%=ICostantiRelazione.PG_ELENCO_RELAZIONI%>"/>

		<%
				if(isTrasferimento)
    	 	{
		%>
    			<jsp:include page="<%=ICostantiRichiesta.PG_LOAD_TRASFERISCIRICHIESTA%>"/>
		<%
				}
				else if(isRelazione)
				{
		%>
    			<jsp:include page="<%=ICostantiRelazione.PG_LOAD_INSERISCI_RELAZIONE%>">
						<jsp:param name="keyField" value="<%=ICostantiRelazione.CAMPO_RIC_ID_RICHIESTA%>"/>
						<jsp:param name="id" value="<%=richiesta.getIdRichiesta()%>"/>
						<jsp:param name="dateRef" value="<%=DateUtils.getDateToString(richiesta.getDataRichiesta(),"dd/MM/yyyy")%>"/>
        	</jsp:include>
		<%
				}
				else
   { // La combo dei template viene visualizzata solo se si può effettuare una nuova stampa
     if (Upload.equalsIgnoreCase("SI"))
   {
  %>
          <form name="dettaglio">
          <jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
         </form>
<% } %>
  				<jsp:include page="<%=ISIAPCostantiWeb.DIV_FORM_UPLOAD%>">
    				<jsp:param name="Azione" value="siap.siepe.richiesta.action.ActUploadDocument"/>
    				<jsp:param name="Id" value="<%=richiesta.getIdRichiesta()%>" />
  				</jsp:include>
  	<% } %>

	</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.siepe.relazione.model.RelazioneModel"%>
<%@ page import="siap.siepe.relazione.action.ICostantiRelazione"%>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe"%>
<%@ page import="siap.siepe.relazione.action.ICostantiRelazione"%>

<%@ page import="siap.siepe.attivita.action.ICostantiAttivita"%>

<jsp:useBean id="relazione"	scope="request" class="siap.siepe.relazione.model.RelazioneModel"/>
<jsp:useBean id="validata" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="Modificabile" scope="request" class="java.lang.String"/>

<%
	String lNomeFunzione = "Dettaglio Relazione"; // Impostazione di default ( Dettaglio Relazione )
	boolean isTrasferimento = false; // Impostazione di default ( false )
	boolean isRelazione = false; // Impostazione di default ( false )

	// Verifica la Modalità ( T -> Trasmissione )
	if ( modalita.equalsIgnoreCase("T"))
	{
    lNomeFunzione = "Trasferimento Relazione"; isTrasferimento = true;
	}
	// Verifica la Modalità ( R -> Relazione )
	else if (modalita.equalsIgnoreCase("R"))
	{
    lNomeFunzione = "Inserimento Relazione per Relazione"; isRelazione = true;
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
          	<jsp:param name="CampoIdEntita" value="<%=ICostantiRelazione.CAMPO_ID_RELAZIONE%>" />
          	<jsp:param name="ValoreIdEntita" value="<%=relazione.getIdRelazione()%>"/>
      		</jsp:include>

        	<!-- BOTTONI DI Modifica e Cancellazione -->
    			<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_MOD_CANC%>">
          	<jsp:param name="CampoIdEntita" value="<%=ICostantiRelazione.CAMPO_ID_RELAZIONE%>" />
          	<jsp:param name="ValoreIdEntita" value="<%=relazione.getIdRelazione()%>"/>
          </jsp:include>

					<td class="LBG">
          	<!-- Combo List delle Funzioni -->
          	<jsp:include page="<%=IWebConstants.PG_TOOLBAR_COMBO%>">
          		<jsp:param name="CampoIdEntita" value="<%=ICostantiRelazione.CAMPO_ID_RELAZIONE%>" />
          		<jsp:param name="ValoreIdEntita" value="<%=relazione.getIdRelazione()%>"/>
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
      	<td class="l">Stato della Relazione </td>
				<td class="l"><font class="campo"><%=( validata.equalsIgnoreCase("SI") ? "VALIDATA" : "NON VALIDATA" )%> </font></td>
			</tr>

			<tr>
				<td class="l">Data Relazione </td>
				<td class="l"><font class="campo"><%=DateUtils.getDateToString(relazione.getDataInserimento(),"dd-MM-yyyy")%> </font></td>
			</tr>

			<tr>
				<td class="l">Ufficio Destinatario </td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(relazione.getDescrUfficioInserimento()) + " - " + StringUtils.toStringJSP(relazione.getDescrSedeUfficioDestinatario()) %></font></td>
			</tr>

    	<tr>
				<td class="l">Note </td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP( relazione.getNote() )%></font></td>
			</tr>
		</table>

		<%
    if(isTrasferimento)
    {	%>
      <jsp:include page="<%=ICostantiRelazione.PG_TRASFERISCI_RELAZIONE%>"/>
		<%}else{%>
	      <jsp:include page="<%=ISIAPCostantiWeb.DIV_FORM_UPLOAD%>">
  	      <jsp:param name="Azione" value="siap.siepe.relazione.action.ActUploadDocument"/>
    	    <jsp:param name="Id" value="<%=relazione.getIdRelazione()%>" />
      	</jsp:include>
  	<%}%>

	</body>
</html>
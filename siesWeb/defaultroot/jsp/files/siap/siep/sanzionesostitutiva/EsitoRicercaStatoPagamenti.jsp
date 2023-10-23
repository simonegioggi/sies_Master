<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-33: aggiunta pagina --%>

<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%> 

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.RicercaStatoPagamentiModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>


<jsp:useBean id="fascicoloNotInSession" scope="request" class="java.lang.String"/>
<jsp:useBean id="criteriRicerca"  		scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="descTipoRicerca" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoFascicoli" 		scope="request" class="java.util.Vector"/>

<html>
<head>
<title>[S.I.E.S.] - Ricerca Stato Pagamenti</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"> </script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
</head>

<body class="corpo">
<table>
  	<tr>
    	<td class="LBG">
      		<a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
    	</td>
    	<td class="LBG">
			<font class="label">Funzione :</font>&nbsp;
			<font class="campo">Elenco Procedimenti Pene Pecuniarie In Base a Stato Pagamenti</font>
    	</td>
	</tr>
</table>
<br>
<%
if (!fascicoloNotInSession.equals("S")) {
%>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<%
}
%>
<br>

<table>
	<tr><td class="titolo" colspan="4">Criteri Selezionati</td></tr>
	<tr>
		<td class="L">Anno/Numero Iniziale:</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(criteriRicerca.getChiaveAnnoIniziale(),"____")%>/<%=StringUtils.toStringJSP(criteriRicerca.getChiaveProgrIniziale(),"______")%></font></td>
		<td class="L">Anno/Numero Finale:</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(criteriRicerca.getChiaveAnnoFinale(),"____")%>/<%=StringUtils.toStringJSP(criteriRicerca.getChiaveProgrFinale(),"______")%></font></td>
	</tr>
	<tr>
		<td class="L">Data Iscrizione Iniziale:</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(criteriRicerca.getDataIscrizioneIniziale(),"dd-MM-yyyy"),"&nbsp;")%></font></td>
		<td class="L">Data Iscrizione Finale:</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(criteriRicerca.getDataIscrizioneFinale(),"dd-MM-yyyy"),"&nbsp;")%></font></td>
	</tr>
	<tr>
		<td class="L">Tipologia Statistica:</td>
		<td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(descTipoRicerca)%></font></td>
  	</tr>
</table>  

<br>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<br>

<table cellspacing=2 cellpadding=2>
	<tr>
		<td class="int" style="padding-left:5px;padding-right:5px;">Numero SIEP</td>
		<td class="int" style="padding-left:5px;padding-right:5px;">Data Iscrizione</td>
		<td class="int" style="padding-left:5px;padding-right:5px;">Cognome</td>
		<td class="int" style="padding-left:5px;padding-right:5px;">Nome</td>
		<td class="int" style="padding-left:5px;padding-right:5px;">Importo da Pagare</td>
		<td class="int" style="padding-left:5px;padding-right:5px;">Importo Pagato</td>
		<td class="int" style="padding-left:5px;padding-right:5px;">Data Ultima Scadenza</td>
		<td class="int" style="padding-left:5px;padding-right:5px;">Azioni</td>
	</tr>

<%
Iterator itx = elencoFascicoli.iterator();
while (itx.hasNext()) {
	RicercaStatoPagamentiModel fascicolo = (RicercaStatoPagamentiModel) itx.next();
	String tipoRat = "";
	if ("U".equals(fascicolo.getTipoRateizzazione())) 
		tipoRat = " (U.S.)";
	else if ("R".equals(fascicolo.getTipoRateizzazione())) 
		tipoRat = " (Rat.)";
%>
	<tr>
  		<td class="c">
  			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>" title="Procedimento">
  				<%=StringUtils.toStringJSP(fascicolo.getChiaveAnno()) + " / " + StringUtils.toStringJSP(fascicolo.getChiaveProgr())%>
  			</a>
		</td>
		<td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIscrizione(),"dd-MM-yyyy"))%></font></td>
		<td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getCognome())%></font></td>
		<td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getNome())%></font></td>
		<td class="r"><font class="label"><%=StringUtils.toEuroFormat(fascicolo.getImportoDaPagare())+tipoRat%></font></td>
		<td class="r"><font class="label"><%=StringUtils.toEuroFormat(fascicolo.getImportoPagato())%></font></td>
		<td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataUltimaScadenza(),"dd-MM-yyyy"),"&nbsp;")%></font></td>
		<td class="c">
  			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActVerificaStatoPagamenti&<%=ICostantiEvento.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>">
				<img src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
    		</a>
  		</td>
  	</tr>
<%
}
%>
</table>

</body>
</html>
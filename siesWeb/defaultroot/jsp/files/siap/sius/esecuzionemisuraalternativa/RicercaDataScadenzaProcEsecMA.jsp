<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2025-48: aggiunta pagina per Scadenzario monitoraggio misure alternative espiate --%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.esecuzionemisuraalternativa.action.ICostantiEsecuzioneMA"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.esecuzionemisuraalternativa.model.EMAFascGPModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="elencoProcedimenti"	scope="request" class="java.util.Vector"/>
<jsp:useBean id="TornaQui"				scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="anni"					scope="request" class="java.lang.String"/>
<jsp:useBean id="mesi"					scope="request" class="java.lang.String"/>
<jsp:useBean id="giorni"				scope="request" class="java.lang.String"/>
<jsp:useBean id="rpm"   				scope="session" class="siap.sius.statistiche.model.RicercaProcedimentoModel"/>

<%
// presenza del Link per il bottone di ritorno
boolean retFlag = false;
retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<title>[S.I.E.S.] - Elenco Procedimenti di Esecuzione Misure Alternative con Data Scadenza</title>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
</head>
<BODY class="corpo">
<FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
<table>
  	<tr>
  		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
    	<td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Procedimenti di Esecuzione Misure Alternative con Data Scadenza</font></td>
		<td class="LBG">
          	<a href="javascript:history.back()">
            	<img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          	</a>
        </td>
		<td class="LBG">
  			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.esecuzionemisuraalternativa.action.ActRicercaDataScadenzaProcEsecMAExcel&Anni=<%=anni%>&Mesi=<%=mesi%>&Giorni=<%=giorni%>">
  				<img src="/images/printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0">
  			</a>
  		</td>
 	</tr>
</table>
<br>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<br>
<%
if (rpm != null) {
%>
<table cellspacing="2" cellpadding="2" width="95%">
   	<tr><td class="titolo">Criteri di Ricerca selezionati:</td></tr>
<%
	if ((rpm.getAnnoInizio() != null && rpm.getNumeroInizio() != null)
			|| (rpm.getAnnoFine() != null &&rpm.getNumeroFine() != null)) {
%>
   	<tr>
     	<td class="lVerdeNB">Intervallo Estremi Procedimenti&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;
<%
		if (rpm.getAnnoInizio() != null) {
%>          	
			Da <%=rpm.getAnnoInizio()%> 
<%
		}
%>
			/
<%
		if (rpm.getNumeroInizio() != null) {
%>          	
			<%=rpm.getNumeroInizio()%>&nbsp;&nbsp; 
<%
		}
%>
			&nbsp;&nbsp;&nbsp;
<%
		if (rpm.getAnnoFine() != null) {
%>          	
			&nbsp;A&nbsp;<%=rpm.getAnnoFine()%> 
<%
		}
%>
			/
<%
		if (rpm.getNumeroFine() != null) {
%>          	
			<%=rpm.getNumeroFine()%>&nbsp;&nbsp; 
<%
		}
%>
		</td>
	</tr>
<%
	}
	if (rpm.getDataIscrizioneInizio() != null || rpm.getDataIscrizioneFine() != null) {
%>
	<tr>
 	<td class="lVerdeNB">Procedimenti con Data Iscrizione :&nbsp;&nbsp;
<%
		if (rpm.getDataIscrizioneInizio() != null) {
%>          	
		Dal <%=DateUtils.getDateToString(rpm.getDataIscrizioneInizio(), "dd-MM-yyyy" )%>&nbsp;&nbsp; 
<%
		}
		if (rpm.getDataIscrizioneFine() != null) {
%>
		&nbsp;Al&nbsp;&nbsp;<%=DateUtils.getDateToString(rpm.getDataIscrizioneFine(),"dd-MM-yyyy")%>
	</td>
<%
		}
%>
</tr>
<%
	}
%>
	<tr>
		<td class="lVerdeNB">Con Data Scadenza : <%=titolo%>
<%
	if ("intervallo".equals(tipo)) {
%>
			&nbsp;entro:&nbsp;&nbsp;Anni&nbsp;<%=Utils.isPresent(anni) ? anni : "-"%>&nbsp;&nbsp;Mesi&nbsp;<%=Utils.isPresent(mesi) ? mesi : "-"%>&nbsp;&nbsp;Giorni&nbsp;<%=Utils.isPresent(giorni) ? giorni : "-"%>
<%
	}
%>
		</td>
	</tr>
</table>
<br>
<%
}
%>
<table cellspacing="2" cellpadding="2" width="95%">
<%
Iterator itx = elencoProcedimenti.iterator();
if (itx.hasNext()) {
%>
	<tr>
		<td class="int">Procedimento SIUS</td>
		<td class="int">Stato Procedimento</td>
		<td class="int">Soggetto</td>
		<td class="int">Ordinanza</td>
		<td class="int">TDS Emittente</td>
		<td class="int">Data Emissione</td>
		<td class="int">Misura da eseguire</td>
		<td class="int">Data inizio misura</td>
		<td class="int">Data fine misura</td>
		<td class="int">Giorni residui</td>
		<td class="int">Procedimento SIEP</td>
	</tr>
<%
} else {
%>
	<tr><td class="titolo">Nessun Procedimento di Esecuzione Misure Alternative con Data Scadenza trovato</td></tr>
<%
}
while (itx.hasNext()) {
	EMAFascGPModel emafgpm = (EMAFascGPModel)itx.next();
%>
	<tr>
		<td class="c">
			<font class="label">
	          	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=emafgpm.getFascicoloSiusModel().getIdFascicoloSius()%><%=retParam%>" Title="Dettaglio Procedimento SIUS">
	            	<%=emafgpm.getFascicoloSiusModel().getChiaveAnno()%>/<%=emafgpm.getFascicoloSiusModel().getChiaveProgr()%>
	          	</a>
          	</font>
        </td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(emafgpm.getFascicoloSiusModel().getDescrStatoFascicolo(), "-")%></font></td>
        <td class="c"><font class="label"><%=emafgpm.getFascicoloSiusModel().getSoggetto().getCognome()%>&nbsp;<%=emafgpm.getFascicoloSiusModel().getSoggetto().getNome()%></font></td>
        <td class="c"><font class="label"><%=emafgpm.getEsecuzioneMAModel().getAnnoS07()%>/<%=emafgpm.getEsecuzioneMAModel().getProgrS07()%></font></td>
        <td class="c"><font class="label"><%=emafgpm.getEsecuzioneMAModel().getDescrLuogoAutoritaEmittOrd()%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(emafgpm.getEsecuzioneMAModel().getDataOrdinanza(), "dd-MM-yyyy"), "-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(emafgpm.getGeneraleProcedimentoModel().getDescrDefinizione(), "-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(emafgpm.getEsecuzioneMAModel().getDataInizioMisura(), "dd-MM-yyyy"), "-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(emafgpm.getEsecuzioneMAModel().getDataTermineAttuale(), "dd-MM-yyyy"), "-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(!Utils.isNullObj(emafgpm.getEsecuzioneMAModel().getDataTermineAttuale()) ? DateUtils.getIntervallo(new Date(), emafgpm.getEsecuzioneMAModel().getDataTermineAttuale()) : "-", "-")%></font></td>
        <td class="c">
			<font class="label">
	          	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=emafgpm.getFascicoloSiusModel().getFasSieIdFascicoloSiep()%><%=retParam%>" Title="Dettaglio Procedimento SIEP">
	            	<%=emafgpm.getFascicoloSiusModel().getChiaveAnnoSIEP()%>/<%=emafgpm.getFascicoloSiusModel().getChiaveProgrSIEP()%>
	          	</a>
          	</font>
        </td>
	</tr>
<%
}
%>
</table>
</FORM>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2019-09: aggiunta pagina per le statistiche --%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.RedirectTo"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"%>
<%@ page import="siap.sius.statistiche.model.EveFasGepSogProvModel"%>
<%@ page import="siap.sius.statistiche.model.RicercaProcedimentoModel"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>

<jsp:useBean id="ricercaProcedimenti"   scope="session" class="siap.sius.statistiche.model.RicercaProcedimentoModel"/>
<jsp:useBean id="elencoProcedimenti"    scope="request" class="java.util.ArrayList<EveFasGepSogProvModel>"/>
<jsp:useBean id="TornaQui"              scope="request" class="java.lang.String"/>

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<title>[S.I.E.S.] - Elenco Procedimenti Misure Alternative (Art. 678 Comma 1 Ter c.p.p.)</title>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
</head>

<%
// presenza del Link per il bottone di ritorno
boolean retFlag = false;
retFlag = (TornaQui != null && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<BODY class="corpo">
<table>
	<tr>
		<td class="LBG">
        	<a href="Javascript:window.print();">
        		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        	</a>
		</td>
		<td class="LBG">
			<font class=label>Funzione :</font>&nbsp;
			<font class="campo">Elenco Procedimenti Misure Alternative (Art. 678 Comma 1 Ter c.p.p.)</font>
		</td>
		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
		<!-- BOTTONE PER STAMPA EXCEL --> 
		<td class=l>
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.statistiche.action.ActRicercaStatisticaMisureAlter678c1tercppExcel">
				<img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0">
			</a>
		</td>
	</tr>
</table>
<br>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<table cellspacing="2" cellpadding="2">
<%
if (ricercaProcedimenti != null) {
%>
	<tr>
 		<td class="Cliccabile">Criteri di Ricerca selezionati:</td>
	</tr>
<%
	if (ricercaProcedimenti.getDataIscrizioneInizio() != null || ricercaProcedimenti.getDataIscrizioneFine() != null) {
%>
	<tr>
	 	<td class="lVerdeNB">Procedimenti con Data Iscrizione :&nbsp;&nbsp;
<%
		if (ricercaProcedimenti.getDataIscrizioneInizio() != null) {
%>          	
			Dal <%=DateUtils.getDateToString(ricercaProcedimenti.getDataIscrizioneInizio(), "dd-MM-yyyy" )%>&nbsp;&nbsp; 
<%
		}
		if (ricercaProcedimenti.getDataIscrizioneFine() != null) {
%>
			&nbsp;Al&nbsp;&nbsp;<%=DateUtils.getDateToString(ricercaProcedimenti.getDataIscrizioneFine(),"dd-MM-yyyy")%>
		</td>
<%
		}
%>
	</tr>
<%
	}
	if (ricercaProcedimenti.getAnnoInizio() != null && ricercaProcedimenti.getNumeroInizio() != null
			|| ricercaProcedimenti.getAnnoFine() != null && ricercaProcedimenti.getNumeroFine() != null) {
%>
	<tr>
		<td class="lVerdeNB">Procedimenti con Anno Numero :&nbsp;&nbsp;
<%
		if (ricercaProcedimenti.getAnnoInizio() != null) {
%>          	
			Dal <%=ricercaProcedimenti.getAnnoInizio()%> 
<%
		}
%>
			/
<%
		if (ricercaProcedimenti.getNumeroInizio() != null) {
%>          	
			<%=ricercaProcedimenti.getNumeroInizio()%>&nbsp;&nbsp; 
<%
		}
%>
			&nbsp;&nbsp;&nbsp;
<%
		if (ricercaProcedimenti.getAnnoFine() != null) {
%>          	
			Al <%=ricercaProcedimenti.getAnnoFine()%> 
<%
		}
%>
			/
<%
		if (ricercaProcedimenti.getNumeroFine() != null) {
%>          	
			<%=ricercaProcedimenti.getNumeroFine()%>&nbsp;&nbsp; 
<%
		}
%>
		</td>
	</tr>
<%
	}
	if (ricercaProcedimenti.getStatoProcedimento() == 0) {
%>
	<tr>
	 	<td class="lVerdeNB">Ordinanze Non Emesse - Atti al Presidente</td>
	</tr>
<%
	} else if (ricercaProcedimenti.getStatoProcedimento() == 1) {
%>
	<tr>
	 	<td class="lVerdeNB">Procedimenti con Magistrato Designato - Ordinanze Non Emesse</td>
	</tr>
<%
	} else if (ricercaProcedimenti.getStatoProcedimento() == 2) {
%>
	<%-- MEV_2024-092: cambio messaggio da Provvisoria a Misure Alternative Dl 123/2018 --%>
	<tr>
	 	<td class="lVerdeNB">Ordinanze Applicazione Misure Alternative Dl 123/2018 Emesse ma prive di Data di Esecutivita&#768;</td>
	</tr>
<%
	} else if (ricercaProcedimenti.getStatoProcedimento() == 3) {
%>
	<tr>
	 	<td class="lVerdeNB">Ordinanze Emesse con Data Esecutività Inserita ma Prive di Decisione del Collegio</td>
	</tr>
<%
	} else if (ricercaProcedimenti.getStatoProcedimento() == 4) {
%>
	<tr>
	 	<td class="lVerdeNB">Procedimenti Privi di Provvedimenti</td>
	</tr>
<%
	}
}
%>
</table>
<br>
<table cellspacing="2" cellpadding="2">
	<tr>
		<td class="int">Numero SIUS</td>
		<td class="int">Data Iscrizione</td>
		<td class="int">Cognome</td>
		<td class="int">Nome</td>
		<td class="int">Data Emissione</td>
		<td class="int">Provvedimento</td>
		<td class="int">Esito</td>
		<td class="int">Azioni</td>
	</tr>
<%
RedirectTo lRedirect = null;
Iterator<EveFasGepSogProvModel> itx = elencoProcedimenti.iterator();
while (itx.hasNext()) {
	EveFasGepSogProvModel procedimento = (EveFasGepSogProvModel) itx.next();
%>
	<tr>
		<%-- 1) Numero SIUS --%>
		<td class="c">
<%
	lRedirect = new RedirectTo();
	lRedirect.setPage(IWebConstants.PG_MAIN);
	lRedirect.setAction("siap.sius.fascicolo.action.ActLoadDettaglioFascicolo");
	lRedirect.setParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS, "" + procedimento.getFascicoloSius().getIdFascicoloSius());							
	if (TornaQui != null && TornaQui.trim().length() > 1)
		lRedirect.setParameter("TornaQui", TornaQui);
%>
			<font class="label">
				<a class="cliccabile" href="<%=lRedirect.toString()%>" title="<%=procedimento.getFascicoloSius().getDescrTipoUfficio()%>&nbsp;<%=procedimento.getFascicoloSius().getDescrComuneUfficio()%> - Dettaglio Procedimento">
					<%=procedimento.getFascicoloSius().getChiaveAnno()%>/<%=procedimento.getFascicoloSius().getChiaveProgr()%>
  				</a>
			</font>
		</td>
		<%-- 2) Data Iscrizione --%>
		<td class="c">
			<font class="label">
				<%=StringUtils.toStringJSP(DateUtils.getDateToString(procedimento.getFascicoloSius().getDataIscrizione(),"dd-MM-yyyy"), "-")%>
			</font>
		</td>
		<%-- 3) Cognome --%>
		<td class="c">
			<font class="label"><%=procedimento.getFascicoloSius().getSoggetto().getCognome()%></font>
		</td>
		<%-- 4) Nome --%>
		<td class="c">
			<font class="label"><%=procedimento.getFascicoloSius().getSoggetto().getNome()%></font>
		</td>
		<%-- 5) Data Emissione --%>
		<td class="c">
<%
		String lDataEmissione = "-";		
		if (procedimento.getEvento() != null && procedimento.getEvento().getDataEmissione() != null)
			lDataEmissione = StringUtils.toStringJSP(DateUtils.getDateToString(procedimento.getEvento().getDataEmissione(), "dd-MM-yyyy"), "-") ;				
%>      
			<font class="label"><%=lDataEmissione%></font>
		</td>
		<%-- 6) Provvedimento --%>
		<td class="c">
<% 			
		String lDescrProvvedimento = "-";
		String lDescrEsitoProvvedimento = "-";
		if (procedimento.getEvento() != null && procedimento.getEvento().getDescrTipoProvvedimento() != null) {
			lDescrProvvedimento = procedimento.getEvento().getDescrTipoProvvedimento() + " " + procedimento.getEvento().getDescrMotivo();
			lDescrEsitoProvvedimento = procedimento.getEvento().getDescrEsito();
		}
%>
			<font class="label"><%=lDescrProvvedimento%></font>
		</td>
		<%-- 7) Esito --%>
		<td class="c">
			<font class="label"><%=lDescrEsitoProvvedimento%></font>
		</td>
		<%-- 8) Azioni --%>
		<td class="c">
<% 
		if (procedimento.getEvento() != null && procedimento.getEvento().getIdEvento() != null) {
			lRedirect = new RedirectTo();
			lRedirect.setPage(IWebConstants.PG_MAIN);
			lRedirect.setAction("siap.sius.provvedimento.action.ActDettaglioProvvedimentoByIdEvento");
			lRedirect.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, ""+procedimento.getEvento().getIdEvento());
			if (TornaQui != null && TornaQui.trim().length() > 1)
				lRedirect.setParameter("TornaQui", TornaQui);
%>
			<a href="<%=lRedirect.toString()%>">
				<img src="/images/dettagli.gif" alt="Dettaglio Provvedimento" width="12" height="12" border="0">
			</a>
<%
		} else {
%>
			-
<%
		}
%>
		</td>
</tr>
<%
} // end while
%>
</table>
</body>
</html>
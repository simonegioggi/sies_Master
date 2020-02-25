<%-- MEV_65: aggiunta pagina per gestire nuova funzionalita' --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>
<%@ page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="fascicoli"	scope="request" class="java.util.Vector"/>
<jsp:useBean id="model"		scope="request" class="siap.sige.fascicolo.model.RicercaFascicoloSigeModel"/>
<jsp:useBean id="TornaQui"	scope="request" class="java.lang.String"/>

<html>
<%
// presenza del Link per il bottone di ritorno
boolean retFlag = false;
retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>
<head>
	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Procedimenti Sige Con Ricorso/Opposizione</title>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
    <script type="text/javascript">
	function creaLink(tr) {
		var st = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActRicercaProcedimentiSigeConRicorsoOpposizione";
		st += "&tipoRicorso=" + tr;
<%
if (model.getChiaveProgrRicorso() != null) {
%>
		st += "&numeroRicorso=" + '<%=model.getChiaveProgrRicorso().toString()%>';
<%
}
if (model.getChiaveProgrRicorso() != null) {
%>
		st += "&annoRicorso=" + '<%=model.getChiaveAnnoRicorso().toString()%>';
<%
}
if (model.getChiaveAnnoIniziale() != null) {
%>
		st += "&AnnoIni=" + '<%=model.getChiaveAnnoIniziale().toString()%>';
<%
}
if (model.getChiaveProgrIniziale() != null) {
%>
		st += "&NumIni=" + '<%=model.getChiaveProgrIniziale().toString()%>';
<%
}
if (model.getChiaveAnnoFinale() != null) {
%>
		st += "&AnnoFine=" + '<%=model.getChiaveAnnoFinale().toString()%>';
<%
}
if (model.getChiaveProgrFinale() != null) {
%>
		st += "&NumFine=" + '<%=model.getChiaveProgrFinale().toString()%>';
<%
}
if (model.getDataArrivoCancelleriaIniziale() != null) {
%>
		st += "&GiornoIniziale=" + '<%=DateUtils.getDayToString(model.getDataArrivoCancelleriaIniziale())%>';
		st += "&MeseIniziale=" + '<%=DateUtils.getMonthToString(model.getDataArrivoCancelleriaIniziale())%>';
		st += "&AnnoIniziale=" + '<%=DateUtils.getYearToString(model.getDataArrivoCancelleriaIniziale())%>';
<%
}
if (model.getDataArrivoCancelleriaFinale() != null) {
%>		
		st += "&GiornoFinale=" + '<%=DateUtils.getDayToString(model.getDataArrivoCancelleriaFinale())%>';
		st += "&MeseFinale=" + '<%=DateUtils.getMonthToString(model.getDataArrivoCancelleriaFinale())%>';
		st += "&AnnoFinale=" + '<%=DateUtils.getYearToString(model.getDataArrivoCancelleriaFinale())%>';
<%
}
%>
		st += "&TipoRicerca=" + '<%=model.getStatoValidazione()%>';
// 		st += "&StoTornando=1";
		st += "<%=retParam%>";
		// LINK
		window.location.href = st;
	}
	</script>
</head>

<BODY class="corpo">
<FORM method="POST" name="rssppg" action="<%=IWebConstants.PG_MAIN%>">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
      	<td class="LBG"><font class=label>Funzione:</font>&nbsp;<font class="campo">Ricerca Procedimenti Sige Con Ricorso/Opposizione</font></td>
		<!-- BOTTONE DI STAMPA  -->
		<td class=l>
			<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
				<jsp:param name="CampoIdEntita" value="N" />
				<jsp:param name="ValoreIdEntita" value="N" />
			</jsp:include>
		</td>
		<!-- NUOVO BOTTONE PER STAMPA EXCEL -->
		<td class=l>
			<a class="cliccabile" href="javascript:stampa2('<%=ISIAPCostantiWeb.PG_STATISTICA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sige.statistiche.action.ActExportProcedimentiSigeConRicorsoOpposizioneInExcel')">
				<img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0">
			</a>
		</td>
		<!-- BOTTONE DI RITORNO -->
<!--     	<td class="LBG"> -->
<!-- 			<a href="Javascript:history.go(-1);"> -->
<%--             	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"> --%>
<!--           	</a> -->
<!--         </td> -->
		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
</table>
<table>
	<tr>
		<td class="lNoBord"><font class="label">Criteri di Ricerca selezionati:</font></td>
	</tr>
	<tr>
		<td class="lVerdeNB">Tipo di Ricorso/Opposizione:&nbsp;<%=model.getDescTipoRicorso()%></td>
	</tr>
<%
if (model.getChiaveAnnoRicorso() != null) {
%>
	<tr>
		<td class="lVerdeNB">Anno/Numero Ricorso/Opposizione:&nbsp;<%=model.getChiaveAnnoRicorso()%>/<%=model.getChiaveProgrRicorso()%></td>
	</tr>
<%
}
if (model.getChiaveAnnoIniziale() != null) {
%>
	<tr>
		<td class="lVerdeNB">Estremi Ricorso/Opposizione dal:&nbsp;<%=model.getChiaveAnnoIniziale()%>/<%=model.getChiaveProgrIniziale()%>&nbsp;al&nbsp;<%=model.getChiaveAnnoFinale()%>/<%=model.getChiaveProgrFinale()%></td>
	</tr>
<%
}
if (model.getDataArrivoCancelleriaIniziale() != null) {
%>
	<tr>
		<td class="lVerdeNB">Data Arrivo in Cancelleria dal:&nbsp;<%=DateUtils.getDateToString(model.getDataArrivoCancelleriaIniziale(),"dd/MM/yyyy")%>&nbsp;al&nbsp;<%=DateUtils.getDateToString(model.getDataArrivoCancelleriaFinale(),"dd/MM/yyyy")%></td>
	</tr>
<%
}
%>
	<tr>
	  	<td class="lVerdeNB">Stato Validazione:&nbsp;<%=model.getStatoValidazione()%></td>
	</tr>
</table>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<br>
<table cellspacing="2" cellpadding="2">
<%
if ("00".equals(model.getTipoRicorso()) && fascicoli.size() > 1) {
%>
	<tr>
		<td colspan="5">&nbsp;<td>
		<td align="right"><img src="/images/QuadratinoVerde.gif"></td>
		<td class="campo">
			<a class="cliccabile" href="javascript:creaLink('04')">Opposizioni</a>
		</td>
		<td align="right"><img src="/images/QuadratinoRosso.gif"></td>
		<td class="campo">
			<a class="cliccabile" href="javascript:creaLink('01')">Ricorsi</a>
		</td>
	</tr>
<%
}
%>
	<tr>
		<td class="int">Anno/Numero Ricorso/Opposizione</td>
		<td class="int">Anno/Numero SIGE</td>
		<td class="int">Cognome Nome</td>
		<td class="int">Data Arrivo in Cancelleria</td>
		<td class="int">Data Provvedimento Impugnato</td>
		<td class="int">Estremi Provvedimento Impugnato</td>
		<td class="int">Parte che Propone Ricorso/Opposizione</td>
		<td class="int">Data Decisione Esito</td>
		<td class="int">Tipo</td>
		<td class="int">Tenore Decisione</td>
	</tr>
<%
Iterator<FascicoloSigeEstesoModel> i = fascicoli.iterator();
String dataArrivoCancelleria = "", dataProvvImpugnato = "", dataDecisioneEsito = "";
String epi = "", pcpro = "", tipo = "", td = "";
while (i.hasNext()) {
	FascicoloSigeEstesoModel fsem = (FascicoloSigeEstesoModel) i.next();
	dataArrivoCancelleria = (fsem.getImpugnazioneSige().getDataArrivoCancelleria() != null) ? 
			DateUtils.getDateToString(fsem.getImpugnazioneSige().getDataArrivoCancelleria(),"dd/MM/yyyy") : "-";
	dataProvvImpugnato = (fsem.getProvvedimentoEventoSige().getProvvedimento().getDataEmissione() != null) ? 
			DateUtils.getDateToString(fsem.getProvvedimentoEventoSige().getProvvedimento().getDataEmissione(),"dd/MM/yyyy") : "-";
	dataDecisioneEsito = (fsem.getImpugnazioneSige().getDataDecisione()!= null) ? 
			DateUtils.getDateToString(fsem.getImpugnazioneSige().getDataDecisione(),"dd/MM/yyyy") : "-";
	epi = (Utils.isPresent(fsem.getProvvedimentoEventoSige().getProvvedimento().getDescrTipoProvvedimentoSige())) ?
			fsem.getProvvedimentoEventoSige().getProvvedimento().getDescrTipoProvvedimentoSige() + " N. " + fsem.getProvvedimentoEventoSige().getProvvedimento().getChiaveAnno() + "/" + fsem.getProvvedimentoEventoSige().getProvvedimento().getChiaveProgr() : "-";
	pcpro = (Utils.isPresent(fsem.getImpugnazioneSige().getDescrSoggettoImpugnante())) ?
			fsem.getImpugnazioneSige().getDescrSoggettoImpugnante() : "-";
	tipo = (Utils.isPresent(fsem.getImpugnazioneSige().getDescrTipoImpugnazione())) ?
			fsem.getImpugnazioneSige().getDescrTipoImpugnazione() : "-";
	td = (Utils.isPresent(fsem.getImpugnazioneSige().getDescrTenoreDecisione())) ?
			fsem.getImpugnazioneSige().getDescrTenoreDecisione() : "-";
%>
	<tr>
		<td class="c">
			<font class="label">
				<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.impugnazione.action.ActLoadDettaglioImpugnazioneSige
						&<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE%>=<%=fsem.getImpugnazioneSige().getIdImpugnazioneSige()%>
						&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=fsem.getFascicoloSige().getIdFascicoloSige()%><%=retParam%>">
					<%=fsem.getImpugnazioneSige().getAnnoS7()%>/<%=fsem.getImpugnazioneSige().getProgrS7()%>
				</a>
			</font>
		</td>
		<td class="c">
			<font class="label">
				<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=fsem.getFascicoloSige().getIdFascicoloSige()%><%=retParam%>">
					<%=fsem.getFascicoloSige().getChiaveAnno()%>/<%=fsem.getFascicoloSige().getChiaveProgr()%>
				</a>
			</font>
		</td>
        <td class="c">
        	<font class="label">
        		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=fsem.getSoggetto().getIdSoggetto()%><%=retParam%>">
					<%=fsem.getSoggetto().getCognome()%>&nbsp;<%=fsem.getSoggetto().getNome()%>
				</a>
        	</font>
        </td>
        <td class="c"><font class="label"><%=dataArrivoCancelleria%></font></td>
        <td class="c"><font class="label"><%=dataProvvImpugnato%></font></td>
        <td class="c"><font class="label"><%=epi%></font></td>
        <td class="c"><font class="label"><%=pcpro%></font></td>
        <td class="c"><font class="label"><%=dataDecisioneEsito%></font></td>
        <td class="c"><font class="label"><%=tipo%></font></td>
        <td class="c"><font class="label"><%=td%></font></td>
	</tr>
<%
}
%>
</table>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<input type="hidden" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">
</FORM>
</body>
</html>
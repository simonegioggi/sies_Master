<%-- MEV_65: aggiunta pagina per gestire nuova funzionalita' --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="fascicoli"	scope="request" class="java.util.Vector"/>
<jsp:useBean id="model"		scope="request" class="siap.sige.fascicolo.model.RicercaFascicoloSigeModel"/>

<%-- <jsp:useBean id="TornaQui"     				scope="request" class="java.lang.String"/> --%>

<%
// presenza del Link per il bottone di ritorno
// boolean retFlag = false;
// retFlag = (TornaQui != null && TornaQui.trim().length() > 1);
// String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<html>
<head>
	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Soggetti Sige per Posizione Giuridica</title>
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
      	<td class="LBG"><font class=label>Funzione:</font>&nbsp;<font class="campo">Ricerca Soggetti Sige per Posizione Giuridica</font></td>
		<!-- BOTTONE DI RITORNO -->
<%--     	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/> --%>
		<!-- NUOVO BOTTONE PER STAMPA EXCEL -->
		<td class=l>
			<a class="cliccabile" href="javascript:stampa2('<%=ISIAPCostantiWeb.PG_STATISTICA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sige.statistiche.action.ActExportSoggettiSigePerPosizioneGiuridicaInExcel')"><img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0"></a>
		</td>
    	<td class="LBG">
			<a href="Javascript:history.go(-1);">
            	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          	</a>
        </td>
    </tr>
</table>
<table>
	<tr>
		<td class="lNoBord"><font class="label">Criteri di Ricerca selezionati:</font></td>
	</tr>
<%
if (model.getDataIscrizioneIniziale() != null) {
%>
	<tr>
		<td class="lVerdeNB">Fascicoli Sige iscritti dal:&nbsp;<%=DateUtils.getDateToString(model.getDataIscrizioneIniziale(),"dd/MM/yyyy")%>&nbsp;al&nbsp;<%=DateUtils.getDateToString(model.getDataIscrizioneFinale(),"dd/MM/yyyy")%></td>
	</tr>
<%
}
if (model.getChiaveAnnoIniziale() != null) {
%>
	<tr>
		<td class="lVerdeNB">Fascicoli Sige dal:&nbsp;<%=model.getChiaveAnnoIniziale()%>/<%=model.getChiaveProgrIniziale()%>&nbsp;al&nbsp;<%=model.getChiaveAnnoFinale()%>/<%=model.getChiaveProgrFinale()%></td>
	</tr>
<%
}
%>
	<tr>
	  	<td class="lVerdeNB">Posizione Giuridica:&nbsp;<%=model.getDescPosizioneGiuridica()%></td>
	</tr>
<%
String statoProcedimento = (model.getDataFinePendenza() != null) ? "Solo Pendenti fino al " + DateUtils.getDateToString(model.getDataFinePendenza(),"dd/MM/yyyy") : "Tutti";
%>
	<tr>
	  	<td class="lVerdeNB">Stato Procedimento:&nbsp;<%=statoProcedimento%></td>
	</tr>
<%
if (Utils.isPresent(model.getDescMagistrato())) {
%>
	<tr>
		<td class="lVerdeNB">Magistrato:&nbsp;<%=model.getDescMagistrato()%></td>
	</tr>
<%
}
if (Utils.isPresent(model.getDescSezione())) {
%>
	<tr>
		<td class="lVerdeNB">Sezione:&nbsp;<%=model.getDescSezione()%></td>
	</tr>
<%
}
if (Utils.isPresent(model.getDescNazione())) {
%>
	<tr>
		<td class="lVerdeNB">Nazionalita':&nbsp;<%=model.getDescNazione()%></td>
	</tr>
<%
}
%>
</table>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<br>
<table cellspacing="2" cellpadding="2">
	<tr>
		<td class="int">Numero SIGE</td>
		<td class="int">Cognome</td>
		<td class="int">Nome</td>
		<td class="int">Data di nascita</td>
		<td class="int">Luogo di nascita</td>
		<td class="int">Posizione Giuridica</td>
		<td class="int">Data Fine Pena</td>
		<td class="int">Nazionalita'</td>
		<td class="int">Azioni</td>
	</tr>
<%
Iterator<FascicoloSigeEstesoModel> i = fascicoli.iterator();
String dataNascita = "";
String dataFinePena = "";
String descComune = "";
String nazionalita = "";
while (i.hasNext()) {
	FascicoloSigeEstesoModel fsem = (FascicoloSigeEstesoModel) i.next();
	dataNascita = (fsem.getSoggetto().getDataNascita() != null) ? 
			DateUtils.getDateToString(fsem.getSoggetto().getDataNascita(),"dd/MM/yyyy") : "-";
	dataFinePena = (fsem.getFascicoloSige().getDataFinePena() != null) ? 
			DateUtils.getDateToString(fsem.getFascicoloSige().getDataFinePena(),"dd/MM/yyyy") : "-";
	descComune = (Utils.isPresent(fsem.getSoggetto().getDescrComuneNascita())
			&& !"-".equals(fsem.getSoggetto().getDescrComuneNascita())) ? fsem.getSoggetto().getDescrComuneNascita() : 
				Utils.isPresent(fsem.getSoggetto().getDescComuneNascitaEstero()) ? fsem.getSoggetto().getDescComuneNascitaEstero() : "-";
	nazionalita = Utils.isPresent(fsem.getSoggetto().getDescrStatoNascita()) ? fsem.getSoggetto().getDescrStatoNascita() : "-";
	%>
	<tr>
		<td class="c"><font class="label"><%=fsem.getFascicoloSige().getChiaveAnno()%>/<%=fsem.getFascicoloSige().getChiaveProgr()%></font></td>
        <td class="c"><font class="label"><%=fsem.getSoggetto().getCognome()%></font></td>
        <td class="c"><font class="label"><%=fsem.getSoggetto().getNome()%></font></td>
        <td class="c"><font class="label"><%=dataNascita%></font></td>
        <td class="c"><font class="label"><%=descComune%></font></td>
        <td class="c"><font class="label"><%=fsem.getFascicoloSige().getDescrPosizioneGiuridica()%></font></td>
        <td class="c"><font class="label"><%=dataFinePena%></font></td>
        <td class="c"><font class="label"><%=nazionalita%></font></td>
        <td class="c">
	        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
	           	<jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>"/>
	           	<jsp:param name="ValoreIdEntita" value="<%=fsem.getFascicoloSige().getIdFascicoloSige()%>"/>
	        </jsp:include>
		</td>
	</tr>
<%
}
%>
</table>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
</FORM>
</body>
</html>
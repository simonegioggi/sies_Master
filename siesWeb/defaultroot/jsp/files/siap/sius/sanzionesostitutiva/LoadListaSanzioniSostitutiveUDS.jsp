<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel"%>
<%@ page import="siap.sius.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>

<jsp:useBean id="listaSanzioniSius" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="eventoUltimoPAS" 	scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="fascicoloSiusGP" 	scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>

<%
// MEV_2023-35: recupero info sul fascicolo per oggetto procedimento
String codOggettoProcedimento = "", tipoSostituzione = "Sanzione";
GeneraleProcedimentoModel gpm = new GeneraleProcedimentoModel();
if (!Utils.isNullObj(fascicoloSiusGP.getGeneraleProcedimentoModel()))
	gpm = fascicoloSiusGP.getGeneraleProcedimentoModel();
if (Utils.isPresent(gpm.getCodOggettoProcedimento()))
	codOggettoProcedimento = gpm.getCodOggettoProcedimento();
if ("U134".equals(codOggettoProcedimento) || "U126".equals(codOggettoProcedimento))
	tipoSostituzione = "Pena";
%>

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<title>[S.I.U.S.] - <%=tipoSostituzione%> Sostitutiva</title>
<%-- 16/05/2008 richiesta conferma di cancellazione PAS in caso di Provvedimento validato --%>
<script language="JavaScript">
function cancella(idEvento, flagMotivo, flagDocumentoRegistrato, idPeriodoAltraSanzione, idFascicoloSius) {
	if (flagDocumentoRegistrato == "S" && flagMotivo == "03") {
		if (window.confirm('Attenzione: Periodo agganciato ad un Decreto o Ordinanza. <br> Si vuole procedere con la cancellazione?')) {
        	str = "/jsp/Main.jsp?Action=siap.sius.sanzionesostitutiva.action.ActLoadCancellaInizioSanzioneSostitutivaUDS&<%=ICostantiSanzioneSostitutiva.CAMPO_ID_PERIODO_ALTRA_SANZIONE%>="+idPeriodoAltraSanzione+"&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>="+idFascicoloSius;
        	window.location.href=str;
      	}
	} else {
        str = "/jsp/Main.jsp?Action=siap.sius.sanzionesostitutiva.action.ActLoadCancellaInizioSanzioneSostitutivaUDS&<%=ICostantiSanzioneSostitutiva.CAMPO_ID_PERIODO_ALTRA_SANZIONE%>="+idPeriodoAltraSanzione+"&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>="+idFascicoloSius;
        window.location.href=str;
    }
}
</script>
</head>

<body class="corpo">
<form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
<table>
  	<tr>
    	<td class="LBG">
      		<a href="Javascript:window.print();">
        		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
      		</a>
    	</td>
    	<td class="LBG">
			<font class="label">Funzione :</font>
			<font class="campo"> Elenco Periodi <%=tipoSostituzione%> Sostitutiva</font>
    	</td>
	</tr>
</table>
<br>
<table>
  	<tr>
	    <td class="int">Data Decorrenza</td>
	    <td class="int">Data Scadenza </td>
	    <td class="int">Durata <%=tipoSostituzione%> Sostitutiva</td>
	    <td class="int">Note</td>
	    <td class="int">Motivo</td>
<%
Object lObj = null;
lObj = session.getAttribute("fascicoloSiusGP");
if (lObj != null) {
%>
		<td class="int">Azione</td>
<%
}
%>
</tr>
<%
if (!listaSanzioniSius.isEmpty()) {
	Iterator itx = listaSanzioniSius.iterator();
  	for (int i = 0; itx.hasNext(); i++) {
		PeriodoAltraSanzioneModel lPeriodoAltraSanzioneModel = (PeriodoAltraSanzioneModel) itx.next();
%>
	<tr>
  		<td class="c">
   			<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodoAltraSanzioneModel.getDataInizioEsecuzione(), "dd-MM-yyyy"), "-")%>
		</td>
		<td class="c">
		  	<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodoAltraSanzioneModel.getDataScadenza(),"dd-MM-yyyy"), "-")%>
		</td>
<%
		CalendarModel lCal = new CalendarModel();
		lCal.setDataInizio(lPeriodoAltraSanzioneModel.getDataInizioEsecuzione());
		lCal.setDataFine(lPeriodoAltraSanzioneModel.getDataScadenza());
		CalendarUtil lCalUtil = new CalendarUtil();
		lCal = lCalUtil.CalcolaNumGiorniMesiAnni(lCal, false);
		// metto false altrimenti in fase di elenco periodi SS sbaglia il calcolo
		// ossia manca un giorno  paolo c. 23/04/2010
		if (CalendarUtil.getTotGiorni(lCal) >= 0) {
			// MEV_2023-35: aggiunto controllo descrizione motivo
			if (lPeriodoAltraSanzioneModel.getDescrMotivo().contains("Inizio Sanzione Sostitutiva"))
				lPeriodoAltraSanzioneModel.setDescrMotivo(lPeriodoAltraSanzioneModel.getDescrMotivo().replace("Inizio Sanzione Sostitutiva", "Inizio Pena Sostitutiva"));
%>
		<td class="c">
		    <font class="label">Anni</font>
		    <font class="campo"><%=lCal.getNumAnni()%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=lCal.getNumMesi()%></font>
			<font class="label">Giorni</font>
			<font class="campo"> <%=lCal.getNumGiorni()%></font>
		 </td>
		<td class="c">
	  		<%=StringUtils.toStringJSP(lPeriodoAltraSanzioneModel.getMotivazione(), "-")%>
		</td>
	 	<td class="c">
	  		<%=StringUtils.toStringJSP(lPeriodoAltraSanzioneModel.getDescrMotivo(), "-")%>
		</td>
		<td class="c">
<%
			if (i == (listaSanzioniSius.size() -1)) {
				if (lObj != null) {
%>
<%--a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.sanzionesostitutiva.action.ActLoadCancellaInizioSanzioneSostitutivaUDS&<%=ICostantiSanzioneSostitutiva.CAMPO_ID_PERIODO_ALTRA_SANZIONE%>=<%=lPeriodoAltraSanzioneModel.getIdPeriodoAltraSanzione()%>&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=lPeriodoAltraSanzioneModel.getFasSiuIdFascicoloSius()%>">
<img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
</a--%>
			<a class="cliccabile" href="javascript:cancella('<%=lPeriodoAltraSanzioneModel.getEveIdEvento()%>', '<%=lPeriodoAltraSanzioneModel.getFlagMotivo()%>', '<%=eventoUltimoPAS.getFlagDocumentoRegistrato()%>', '<%=lPeriodoAltraSanzioneModel.getIdPeriodoAltraSanzione()%>', '<%=lPeriodoAltraSanzioneModel.getFasSiuIdFascicoloSius()%>');" title="Cancella">
				<img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
			</a>
<%
				}
			}
%>
		</td>
	</tr>
<%
    	}
	}
}
%>
</table>
</form>
</body>
</html>
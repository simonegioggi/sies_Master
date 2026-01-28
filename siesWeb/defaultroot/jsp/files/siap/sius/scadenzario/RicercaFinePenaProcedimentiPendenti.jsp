<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- @since MEV_2026-1 --%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.lang.String"%>

<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sius.scadenzario.action.ICostantiScadenzarioSius"%>
<%@ page import="siap.sius.scadenzario.model.ScadenzarioSiusModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloSiusModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="scadenzari"	scope="request" class="java.util.Vector<ScadenzarioSiusModel>"/>
<jsp:useBean id="intestazione" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"		scope="request" class="java.lang.String"/>

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<title>[S.I.E.S.] - Consultazione Scadenzario Ricerca Fine Pena Procedimenti Pendenti</title>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
</head>

<%
// presenza del Link per il bottone di ritorno
boolean retFlag = false;
retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<BODY class="corpo">
<FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
<table>
  	<tr>
  		<td class="LBG">
  			<a href="Javascript:window.print();">
  				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
  			</a>
  		</td>
    	<td class="LBG">
    		<font class="label">Funzione :</font>&nbsp;
    		<font class="campo">Elenco Procedimenti Pendenti con Data Inizio e Fine Pena</font>
    	</td>
		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
		<!-- BOTTONE PER STAMPA EXCEL --> 
		<td class=l>
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.scadenzario.action.ActRicercaFinePenaProcedimentiPendentiExcel">
				<img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0">
			</a>
		</td>
	</tr>
</table>
<br>
<table cellpadding="2" cellspacing="2" width="95%">
	<tr><td class="cverde" style="font-size: 12px;"><%=intestazione%></td></tr>
</table>
<br>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<table cellpadding="2" cellspacing="2" width="95%">
<%
if (scadenzari.size() == 0) {
%>
	<tr><td class="cverde" style="font-size: 12px;">Nessun procedimento trovato con i criteri di ricerca selezionati</td></tr>
<%
} else {
%>
	<tr>
		<td class="int">Procedimento SIUS</td>
		<td class="int">Soggetto</td>
		<td class="int">Luogo Nascita</td>
		<td class="int">Data Nascita</td>
		<td class="int">Posizione Giuridica</td>
		<td class="int">Contenuto</td>
		<td class="int">Data Inizio Pena</td>
		<td class="int">Data Fine Pena</td>
		<td class="int">Giorni Residui</td>
		<td class="int">Fine Pena Virtuale</td>
		<td class="int">Giorni Residui</td>
		<td class="int">Procedimento SIEP</td>
	</tr>
<%
	Iterator<ScadenzarioSiusModel> itx = scadenzari.iterator();
	while (itx.hasNext()) {
		ScadenzarioSiusModel ssm = (ScadenzarioSiusModel) itx.next();
		int giorniResidui = Utils.isNullObj(ssm.getGiorniResidui()) ? 0 : ssm.getGiorniResidui().intValue();
		int giorniResiduiVirtuali = Utils.isNullObj(ssm.getGiorniResiduiVirtuali()) ? 0 : ssm.getGiorniResiduiVirtuali().intValue();
		String straniero = (Utils.isPresent(ssm.getFascicoloSius().getSoggetto().getDescComuneNascitaEstero())
				&& !"-".equals(ssm.getFascicoloSius().getSoggetto().getDescComuneNascitaEstero())) 
				? ssm.getFascicoloSius().getSoggetto().getDescComuneNascitaEstero() : "";
		String luogoNascitaStraniero = (Utils.isPresent(straniero)) 
				? straniero + " (" + ssm.getFascicoloSius().getSoggetto().getDescrStatoNascita() + ")" 
				: ssm.getFascicoloSius().getSoggetto().getDescrStatoNascita();
		String luogoNascita = (Utils.isPresent(ssm.getFascicoloSius().getSoggetto().getDescrComuneNascita())
				&& !"-".equals(ssm.getFascicoloSius().getSoggetto().getDescrComuneNascita())) 
				? ssm.getFascicoloSius().getSoggetto().getDescrComuneNascita() : luogoNascitaStraniero;
		String style = "c";
		if (!Utils.isNullObj(ssm.getDataFinePenaVirtuale()))
			style = "crosso";
%>
	<tr>
		<td class="C">
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=ssm.getFascicoloSius().getIdFascicoloSius()%><%=retParam%>" title="Procedimento SIUS">
				<%=ssm.getFascicoloSius().getChiaveAnno()%>/<%=ssm.getFascicoloSius().getChiaveProgr()%>
			</a>
		</td>
		<td class="C"><%=StringUtils.toStringJSP(ssm.getFascicoloSius().getSoggetto().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(ssm.getFascicoloSius().getSoggetto().getNome())%></td>
		<td class="C"><%=StringUtils.toStringJSP(luogoNascita, "-")%></td>
		<td class="C"><%=StringUtils.toStringJSP(DateUtils.getDateToString(ssm.getFascicoloSius().getSoggetto().getDataNascita(),"dd/MM/yyyy"))%></td>
		<td class="C"><%=StringUtils.toStringJSP(ssm.getPosizioneGiuridica().getDescrPosizioneGiuridica(), "-")%></td>
		<td class="C"><%=StringUtils.toStringJSP(ssm.getGeneraleProcedimento().getDescrOggettoProcedimento(), "-")%></td>
		<td class="C"><%=StringUtils.toStringJSP(DateUtils.getDateToString(ssm.getDataInizioScadenza(), "dd/MM/yyyy"), "-")%></td>
		<td class="C"><%=StringUtils.toStringJSP(DateUtils.getDateToString(ssm.getDataFineScadenza(), "dd/MM/yyyy"), "-")%></td>
		<td class="C"><%=StringUtils.toStringJSP((Utils.isNullObj(ssm.getDataFineScadenza())) ? "-" : giorniResidui)%></td>
		<td class="<%=style%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(ssm.getDataFinePenaVirtuale(), "dd/MM/yyyy"), "-")%></td>
		<td class="C"><%=StringUtils.toStringJSP((Utils.isNullObj(ssm.getDataFinePenaVirtuale())) ? "-" : giorniResiduiVirtuali)%></td>
		<td class="C">
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=ssm.getRiferimentoFascicoloSiep().getFasSieIdFascicoloSiep()%><%=retParam%>" title="Procedimento SIEP">
				<%=ssm.getRiferimentoFascicoloSiep().getAnnoFascicoloSiep()%>/<%=ssm.getRiferimentoFascicoloSiep().getProgrFascicoloSiep()%>
			</a>
			<br>(<%=StringUtils.toStringJSP(ssm.getRiferimentoFascicoloSiep().getCodUffFascicoloSiep())%>&nbsp;<%=StringUtils.toStringJSP(ssm.getRiferimentoFascicoloSiep().getDescrUffFascicoloSiep())%>)
		</td>
	</tr>
<%
	}
}
%>
</table>
</FORM>
</body>
</html>
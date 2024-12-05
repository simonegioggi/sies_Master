<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<jsp:useBean id="datiOrdinanza"			scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="PeriodoAltraSanzione" 	scope="request" class="siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel"/>

<%
UtenteModel lUteMod = (UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
UfficioModel lUffMod = lUteMod.getUfficioUtente();
String CodUff = new String(lUffMod.getCodTipoUfficio());
String labelUfficio = "";
if (CodUff.equals("TDSM") || CodUff.equals("UDSM")) {
	labelUfficio = "Tribunale per i Minorenni in funzione di Tribunale Sorveglianza";
} else {
	labelUfficio = "Tribunale di Sorveglianza";
}
// MEV_2023-35: recupero info sul fascicolo per oggetto procedimento
String codOggettoProcedimento = "", tipoSostituzione = "Sanzione Sostitutiva";
if (datiOrdinanza != null && datiOrdinanza.getOrdinanza() != null && datiOrdinanza.getOrdinanza().getOggettoProcedimento() != null)
	codOggettoProcedimento = datiOrdinanza.getOrdinanza().getOggettoProcedimento();
if (!Utils.isPresent(codOggettoProcedimento)) {
	FascicoloGPModel fgpm = (FascicoloGPModel) session.getAttribute("fascicoloSiusGP");
	GeneraleProcedimentoModel gpm = new GeneraleProcedimentoModel();
	if (!Utils.isNullObj(fgpm.getGeneraleProcedimentoModel()))
		gpm = fgpm.getGeneraleProcedimentoModel();
	if (Utils.isPresent(gpm.getCodOggettoProcedimento()))
		codOggettoProcedimento = gpm.getCodOggettoProcedimento();
}
if ("U134".equals(codOggettoProcedimento))
	tipoSostituzione = "Pena Sostitutiva";
else if (ICostantiDepositoOrdinanzaPc.COD_OGGETTO_SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE_UDS.equals(codOggettoProcedimento)
		|| ICostantiDepositoOrdinanzaPc.COD_OGGETTO_SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE_TDS.equals(codOggettoProcedimento))
	tipoSostituzione = "Pena Accessoria";
%>
<table cellspacing="2" cellpadding="2">
<%
if (ICostantiDepositoOrdinanzaPc.COD_OGGETTO_SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE_UDS.equals(codOggettoProcedimento)
		|| ICostantiDepositoOrdinanzaPc.COD_OGGETTO_SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE_TDS.equals(codOggettoProcedimento)) {
%>
	<tr>
	    <td class="l">Pena Accessoria Sospesa</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getDescrTipoPenaAccessoria(), "-")%></font></td>
  	</tr>
	<tr>
	    <td class="l">Durata</td>
	    <td class="l">
	    	<font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getDescrDurata(), "-")%></font>&nbsp;&nbsp;per
	    	&nbsp;Anni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getNumAnni(), "-")%></font>
	    	&nbsp;Mesi&nbsp;<font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getNumMesi(), "-")%></font>
	    	&nbsp;Giorni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getNumGiorni(), "-")%></font>
	    </td>
  	</tr>
<%
}
// MEV_2023-35: richiesta durante il collaudo la possibilità di visualizzare sempre!
// if (!Utils.isNullObj(datiOrdinanza.getOrdinanza().getDataSospensioneSS())) {
%>
	<tr><td>&nbsp;</td></tr>
    <tr>
        <td class="Titolo" colspan="2"> Sospensione Esecuzione <%=tipoSostituzione%> <td>
    </tr>
  	<tr>
      	<td class="l">Eventuale Motivazione</td>
    	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento(), "-")%></font></td>
  	</tr>
<%
%>
  	<tr>
	    <td class="l"><%=labelUfficio%> Competente</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getDescrUffTdsConcessoRiduzione(), "-")%></font></td>
	</tr>
<%
// 	if (datiOrdinanza.getOrdinanza().getDataSospensioneSS() != null) {
%>
	<tr>
	    <td class="l">Data Decorrenza Sospensione</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataSospensioneSS(), "dd/MM/yyyy"), "-")%></font></td>
  	</tr>
<%
// 	}
// 	if (datiOrdinanza.getOrdinanza().getSospensioneAASS().compareTo(new BigDecimal(0)) != 0
// 			|| datiOrdinanza.getOrdinanza().getSospensioneMMSS().compareTo(new BigDecimal(0)) != 0
// 			|| datiOrdinanza.getOrdinanza().getSospensioneGGSS().compareTo(new BigDecimal(0)) != 0) {
%>
  	<tr>
	    <td class="l">Periodo Sospensione</td>
	    <td class="l"><font class="campo">ANNI <%=datiOrdinanza.getOrdinanza().getSospensioneAASS()%> MESI <%=datiOrdinanza.getOrdinanza().getSospensioneMMSS()%> GIORNI <%=datiOrdinanza.getOrdinanza().getSospensioneGGSS()%></font></td>
  	</tr>
<%
// 	}
// 	if (datiOrdinanza.getOrdinanza().getDataScadenzaSospensioneSS() != null) {
%>
   	<tr>
	    <td class="l">Fino al</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataScadenzaSospensioneSS(),"dd/MM/yyyy"), "-")%></font></td>
  	</tr>
<%
// 	}
	// Controllo per visualizzazione campo "Giorni da recuperare"
	if (datiOrdinanza.getOrdinanza().getFlagRecuperoSS() != null
			&& datiOrdinanza.getOrdinanza().getFlagRecuperoSS().equals("S")) {
%>
  	<tr>
	    <td class="l">Giorni da Recuperare</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getGiorniRecuperoSS())%></font></td>
 	</tr>
<%
	}
	if (PeriodoAltraSanzione != null && PeriodoAltraSanzione.getIdPeriodoAltraSanzione() != null) {
%>
  	<tr>
	    <td class="l"><%=tipoSostituzione%> Espiata</td>
	    <td class="l"><font class="campo">ANNI <%=PeriodoAltraSanzione.getEspiataAA()%> MESI <%=PeriodoAltraSanzione.getEspiataMM()%> GIORNI <%=PeriodoAltraSanzione.getEspiataGG()%></font></td>
  	</tr>
  	<tr>
	    <td class="l"><%=tipoSostituzione%> residua da Espiare</td>
	    <td class="l"><font class="campo">ANNI <%=PeriodoAltraSanzione.getResiduaAA()%> MESI <%=PeriodoAltraSanzione.getResiduaMM()%> GIORNI <%=PeriodoAltraSanzione.getResiduaGG()%></font></td>
  	</tr>
<%
	}
// }
%>
</table>
<br>
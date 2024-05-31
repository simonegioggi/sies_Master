<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>

<%
UtenteModel lUteMod = (UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
UfficioModel lUffMod = lUteMod.getUfficioUtente();
String CodUff = new String(lUffMod.getCodTipoUfficio());
String labelUfficio = "";
if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){
	labelUfficio = "Tribunale per i Minorenni in funzione di Tribunale Sorveglianza";
} else {
	labelUfficio = "Tribunale di Sorveglianza";
}
// MEV_35: recupero info sul fascicolo per oggetto procedimento
String codOggettoProcedimento = "", tipoSostituzione = "Sanzione";;
if (datiOrdinanza != null && datiOrdinanza.getOrdinanza() != null && datiOrdinanza.getOrdinanza().getOggettoProcedimento() != null)
	codOggettoProcedimento = datiOrdinanza.getOrdinanza().getOggettoProcedimento();
if (!Utils.isPresent(codOggettoProcedimento)) {
	FascicoloGPModel fgpm = (FascicoloGPModel) session.getAttribute("fascicoloSiusGP");
	GeneraleProcedimentoModel gpm = new GeneraleProcedimentoModel();
	if (!Utils.isNullObj(fgpm.getGeneraleProcedimentoModel()))
		gpm = fgpm.getGeneraleProcedimentoModel();
	if (!Utils.isNullObj(gpm.getCodOggettoProcedimento()))
		codOggettoProcedimento = gpm.getCodOggettoProcedimento();
}
if ("U134".equals(codOggettoProcedimento))
	tipoSostituzione = "Pena";
%>	
<jsp:useBean id="PeriodoAltraSanzione" scope="request" class="siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel"/>
<br>
<table cellspacing="2" cellpadding="2">
    <tr>
        <td class="Titolo" colspan=6> Sospensione Esecuzione <%=tipoSostituzione%> Sostitutiva <td>
    </tr>
  	<tr>
      	<td class="l"> Eventuale Motivazione</td>
    	<td class="l"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento())%></font></td>
  	</tr>
  	<tr>
	    <td class="l"><%=labelUfficio%> Competente</td>
	    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getDescrUffTdsConcessoRiduzione())%></font></td>
	</tr>
	<tr>
	    <td class="l">Data Decorrenza Sospensione</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataSospensioneSS(),"dd/MM/yyyy"))%></font></td>
  	</tr>
<%
if (datiOrdinanza.getOrdinanza().getSospensioneAASS().compareTo(new BigDecimal(0)) != 0
		|| datiOrdinanza.getOrdinanza().getSospensioneMMSS().compareTo(new BigDecimal(0)) != 0
		|| datiOrdinanza.getOrdinanza().getSospensioneGGSS().compareTo(new BigDecimal(0)) != 0) {
%>
  	<tr>
	    <td class="l"> Periodo Sospensione</td>
	    <td class="l"><font class="campo">ANNI <%=datiOrdinanza.getOrdinanza().getSospensioneAASS() %> MESI <%=datiOrdinanza.getOrdinanza().getSospensioneMMSS() %> GIORNI <%=datiOrdinanza.getOrdinanza().getSospensioneGGSS() %></font></td>
  	</tr>
<%
}
if (datiOrdinanza.getOrdinanza().getDataScadenzaSospensioneSS() != null) {
%>
   	<tr>
	    <td class="l"> Fino al</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataScadenzaSospensioneSS(),"dd/MM/yyyy"))%> </font>&nbsp;</td>
  	</tr>
<%
}
// Controllo per visualizzazione campo "Giorni da recuperare"
if (datiOrdinanza.getOrdinanza().getFlagRecuperoSS() != null
		&& datiOrdinanza.getOrdinanza().getFlagRecuperoSS().equals("S")) {
%>
  	<tr>
	    <td class="l">Giorni da Recuperare</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getGiorniRecuperoSS())%>&nbsp;</font></td>
 	</tr>
<%
} 
if (PeriodoAltraSanzione != null && PeriodoAltraSanzione.getIdPeriodoAltraSanzione() != null) {
%>
  	<tr>
	    <td class="l"><%=tipoSostituzione%> Sostitutiva Espiata</td>
	    <td class="l"><font class="campo">ANNI <%=PeriodoAltraSanzione.getEspiataAA() %> MESI <%=PeriodoAltraSanzione.getEspiataMM() %> GIORNI <%=PeriodoAltraSanzione.getEspiataGG() %></font></td>
  	</tr>
  	<tr>
	    <td class="l"><%=tipoSostituzione%> Sostitutiva residua da Espiare</td>
	    <td class="l"><font class="campo">ANNI <%=PeriodoAltraSanzione.getResiduaAA() %> MESI <%=PeriodoAltraSanzione.getResiduaMM() %> GIORNI <%=PeriodoAltraSanzione.getResiduaGG() %></font></td>
  	</tr>
<%
} 
%>
</table>
<br>
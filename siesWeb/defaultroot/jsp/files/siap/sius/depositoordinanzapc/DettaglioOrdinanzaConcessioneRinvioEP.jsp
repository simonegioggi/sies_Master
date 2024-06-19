<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>

<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>

<%
UtenteModel lUteMod = (UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
UfficioModel lUffMod = lUteMod.getUfficioUtente();
String CodUff = new String(lUffMod.getCodTipoUfficio());
String labelUfficio = "";
if (CodUff.equals("TDSM") || CodUff.equals("UDSM")) {
	labelUfficio = "Ufficio di Sorveglianza presso il Tribunale per Minorenni emittente";
} else {
	labelUfficio = "Ufficio di Sorveglianza emittente";
}

// MEV_2023-35: aggiunti controlli sul contenuto
String codOggettoProcedimento = "", tipoContenuto = "";
FascicoloGPModel fgpm = (FascicoloGPModel) session.getAttribute("fascicoloSiusGP");
GeneraleProcedimentoModel gpm = new GeneraleProcedimentoModel();
if (!Utils.isNullObj(fgpm.getGeneraleProcedimentoModel()))
	gpm = fgpm.getGeneraleProcedimentoModel();
if (Utils.isPresent(gpm.getCodOggettoProcedimento()))
	codOggettoProcedimento = gpm.getCodOggettoProcedimento();
%>
<table cellspacing="4" cellpadding="4" width="95%">
	<tr>
	  	<td class="l" width="30%">Ulteriore descrizione della decisione</td>
	  	<td class="l"><font class="campo"><%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
   	</tr>
	<tr><td>&nbsp;</td></tr>
    <tr>
        <td class="Titolo" colspan="2"> Concessione Rinvio Esecuzione della Pena <td>
    </tr>
	<tr>
    	<td class="Titolo" colspan="2"> Estremi decreto Magistrato Sorveglianza: <td>
  	</tr>
    <tr>
      	<td class="l">Data Emissione</td>
      	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataTrasmissione(), "dd/MM/yyyy"), "-")%></font></td>
    </tr>
    <tr>
      	<td class="l"><%=labelUfficio%></td>
      	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp(), "-")%></font></td>
    </tr>
<%
// MEV_2023-35: aggiunto importo della pena pecuniaria convertita
if ("C065".equals(codOggettoProcedimento)) {
%>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="l">Pena Pecuniaria Convertita</td>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(datiOrdinanza.getOrdinanza().getSommaRisarcimento()), "-")%></font>
		</td>
	</tr>
<%
}
// FINE MEV_2023-35
%>
   	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="l">Data Inizio Periodo</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataInizioPeriodo(), "dd/MM/yyyy"), "-")%></font></td>
    </tr>
	<tr>
		<td class="l">Data Termine Periodo</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataFineMisura(), "dd/MM/yyyy"), "-")%></font></td>
 	</tr>
	<tr>
		<td class="l">Durata sospensione (AA-MM-GG)</td>
		<td class="l">
			<font class="campo">
		   		<%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getNumAnniDetenzioneDom())%> - <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumMesiDetenzioneDom())%> - <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumGiorniDetenzioneDom())%>
			</font>
		</td>
    </tr>
    <tr><td>&nbsp;</td></tr>
</table>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>

<jsp:useBean id="PeriodoAltraSanzione" 	scope="request"	class="siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel"/>
<jsp:useBean id="lCalMode" 				scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="lCalModre" 			scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="istitutodetenzione" 	scope="request"	class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="fascicoloSiusGP" 		scope="session"	class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>

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
<%
if (modalita.equals("D")) {
%>
<title> Dettaglio Periodo Altra <%=tipoSostituzione%> </title>
<%
} else if (modalita.equals("C")) {
%>
<title> Cancellazione Periodo Altra <%=tipoSostituzione%> </title>
<%
}
%>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript">
function Verify() {
	var msgConfirm = "Si vuole procedere con la cancellazione dei dati?";
  	if (window.confirm(msgConfirm))
    	return true;
  	else
    	return false;
}
</script>
</head>

<body class="corpo">
<FORM name="comandi" >
<table>
	<tr>
      	<td class="LBG">
        	<a href="Javascript:window.print();">
          		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        	</a>
      	</td>
      	<td class="LBG">
        	<font class="label">Funzione :</font>&nbsp;
<%
if (modalita.equals("D")) {
%>
        	<font class="campo">Dettaglio Sospensione <%=tipoSostituzione%> Sostitutiva</font>
<%
} else if (modalita.equals("C")) {
%>
        	<font class="campo">Cancellazione Sospensione <%=tipoSostituzione%> Sostitutiva</font>
<%
}
%>
		</td>
      	<td class="LBG">
			<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
				<jsp:param name="CampoIdEntita" value="<%=ICostantiSanzioneSostitutiva.CAMPO_ID_PERIODO_ALTRA_SANZIONE%>"/>
				<jsp:param name="ValoreIdEntita" value="<%=PeriodoAltraSanzione.getIdPeriodoAltraSanzione()%>"/>
			</jsp:include>
     	</td>
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	</tr>
</table>
</FORM>

<table>
	<tr>
		<jsp:include
			page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
	</tr>
	<tr>
		<td class="L">
			<font class="campo">
<%
if (fascicoloSiusGP.getTenori() != null) {
	int lSize = fascicoloSiusGP.getTenori().length;
 	if (lSize == 0)
%>
			-&nbsp;
<%
	for (int x = 0; x < lSize; x++) {
%>
			<font class="label"> <%=fascicoloSiusGP.getTenori()[x].getDescrOggettoTenore()%>
<%
		if (fascicoloSiusGP.getTenori()[x].getCodDettaglioOggetto().length() > 1) {
%>
			</font> <font class="descr"> - <%=fascicoloSiusGP.getTenori()[x].getDescrDettaglioOggetto()%></font>
<%
		}
	}
} else {
%>
			-&nbsp;
<%
}
%>
			</font>
		</td>
	</tr>
</table>
<table>
	<tr>
		<td class="l" colspan=2>Autorità competente che ha inviato il verbale:</td>
	</tr>
<%
if (istitutodetenzione != null) {
	if (istitutodetenzione.getIdIstitutoDetenzione().trim().length() > 0) {
%>
	<tr>
		<td class="l">Istituto Detenzione</td>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(istitutodetenzione.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(istitutodetenzione.getDescrComune())%></font>
		</td>
	</tr>
<%
	}
}
if (!PeriodoAltraSanzione.getCodTipoAutorita().equals("-")) {
%>
	<tr>
		<td class="l">Autorità</td>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(PeriodoAltraSanzione.getDescrTipoAutorita())%> di <%=StringUtils.toStringJSP(PeriodoAltraSanzione.getDescrLuogoAutorita())%></font>
		</td>
	</tr>
<%
} else {
	if (!PeriodoAltraSanzione.getCodTipoUfficioSosp().equals("-")) {
%>
	<tr>
		<td class="l">Autorità</td>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(PeriodoAltraSanzione.getDescrTipoUfficio())%> di <%=StringUtils.toStringJSP(PeriodoAltraSanzione.getDescrLuogoAutorita())%></font>
		</td>
	</tr>
<%
	}
}
%>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Note</td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(PeriodoAltraSanzione.getMotivazione())%></font></td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Data Sospensione</td>
		<td class="l">
			<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoAltraSanzione.getDataInizioEsecuzione(), "dd/MM/yyyy"))%></font>
		</td>
	</tr>
<%
if (PeriodoAltraSanzione.getDaRecuperare().equals("1")) {
%>
 	<tr>
 		<td class="l">Da Recuperare</td>
		<td class="c">
			<font class="label">Anni</font>
			<font class="campo"><%=StringUtils.toStringJSP(PeriodoAltraSanzione.getDaRecuperareAA(), "-")%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=StringUtils.toStringJSP(PeriodoAltraSanzione.getDaRecuperareMM(), "-")%></font>
			<font class="label">Giorni</font>
			<font class="campo"> <%=StringUtils.toStringJSP(PeriodoAltraSanzione.getDaRecuperareGG(), "-")%></font>
		</td>
  	</tr>
<%
}
if (PeriodoAltraSanzione.getDataScadenza() != null) {
%>
	<tr>
		<td class="l">Data Fine Sospensione</td>
		<td class="l">
			<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoAltraSanzione.getDataScadenza(), "dd/MM/yyyy"))%></font>
		</td>
	</tr>
<%
}
%>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td class="l"><%=tipoSostituzione%> Sostitutiva Espiata</td>
		<td class="c">
			<font class="label">Anni</font>
			<font class="campo"><%=lCalMode.getNumAnni()%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=lCalMode.getNumMesi()%></font>
			<font class="label">Giorni</font>
			<font class="campo"> <%=lCalMode.getNumGiorni()%></font>
		</td>
	</tr>
	<tr>
		<td class="l"><%=tipoSostituzione%> Sostitutiva residua da Espiare</td>
		<td class="c">
			<font class="label">Anni</font>
			<font class="campo"><%=lCalModre.getNumAnni()%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=lCalModre.getNumMesi()%></font>
			<font class="label">Giorni</font>
			<font class="campo"> <%=lCalModre.getNumGiorni()%></font>
		</td>
	</tr>
<%
if (modalita.equals("C")) {
%>
	<tr>
	  	<td align="center">
	    	<input class="bottone" type="submit" name="conferma" value="Conferma" onclick="Javascript: return Verify();">
	  	</td>
	</tr>
<%
}
%>
</table>
</body>
</html>
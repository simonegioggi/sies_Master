<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-13_ aggiunta pagina di dettaglio civilmente obbligato --%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.pagoPA.action.ICostantiPagoPA"%>
<%@ page import="siap.siep.pagoPA.model.CivilmenteObbligatoModel"%>

<jsp:useBean id="civilmenteObbligati" 	scope="request" class="java.util.Vector<CivilmenteObbligatoModel>"/>
<jsp:useBean id="idFascicoloSiep" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="Modificabile" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="Cancellabile" 			scope="request" class="java.lang.String"/>

<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Civilmente Obbligato</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
</head>

<body class="corpo">
<FORM name="comandi">
<table>
  	<tr>
  		<td class="LBG">
  			<a href="Javascript:window.print();">
  				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
  			</a>
  		</td>
       	<td class="LBG">
			<font class="label">Funzione :</font>&nbsp;<font class="campo">Dettaglio Civilmente Obbligato</font>
     	</td>
<%
if (!"S".equals(fascicolo.getFlagValidato())) {
%>
     	<!-- BOTTONE DI MODIFICA -->
     	<td class="LBG">
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.pagoPA.action.ActLoadModificaCivilmenteObbligato&<%=ICostantiPagoPA.CAMPO_ID_FASCICOLO_SIEP%>=<%=idFascicoloSiep%>">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
			</a>
		</td>
    	<!-- BOTTONE DI CANCELLAZIONE -->
     	<td class="LBG">
			<a href="Javascript:conferma('siap.siep.pagoPA.action.ActCancellaCivilmenteObbligato','<%=ICostantiPagoPA.CAMPO_ID_FASCICOLO_SIEP%>','<%=idFascicoloSiep%>');">
            	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
          	</a>
		</td>
<%
}
%>
		<!-- BOTTONE DI RITORNO -->
    	<td class="LBG">
          	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penacomplessiva.action.ActLoadDettaglioPenaComplessiva&ChiaveFascicolo=<%=idFascicoloSiep%>">
            	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          	</a>
        </td>
	</tr>
</table>
</FORM>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
<%
Iterator<CivilmenteObbligatoModel> itx = civilmenteObbligati.iterator();
while (itx.hasNext()) {
	CivilmenteObbligatoModel com = (CivilmenteObbligatoModel) itx.next();
%>
	<tr>
		<td class="Titolo" colspan="4">Dati Civilmente Obbligato</td>
	</tr>
    <tr>
      	<td class="L">Qualifica:</td>
      	<td class="l">
        	<font class="campo"><%=com.getDescrTutore()%></font>
        </td>
        <td class="l" colspan="2">&nbsp;</td>
    </tr>
<%
	if ("G".equals(com.getCodPersona())) {
%>
	<tr>
		<td class="l">Società</td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(com.getDenominazione())%></font></td>
		<td class="l">Ragione Sociale</td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(com.getRagSociale())%></font></td>
	</tr>
	<tr>
		<td class="l">Provincia</td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(com.getDescrProvincia())%></font></td>
		<td class="l">Partita IVA/Codice Fiscale</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(com.getCodFiscale())%></font></td>
	</tr>
	<tr>
		<td class="l">Sede Legale</td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(com.getIndSedeLegale())%></font></td>
		<td class="l">Sede Operativa/Indirizzo Attività</td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(com.getIndSedeOperativa())%></font></td>
	</tr>
	<tr>
		<td class="Titolo" colspan="4">Dettaglio Legale Rappresentente</td>
	</tr>
<%
	}
%>
	<tr>
      	<td class="L">Cognome:</td>
      	<td class="l">
        	<font class="campo"><%=com.getCognome()%></font>
        </td>
        <td class="L">Nome:</td>
      	<td class="l">
        	<font class="campo"><%=com.getNome()%></font>
        </td>
    </tr>
    <tr>
      	<td class="L">Sesso:</td>
      	<td class="l">
        	<font class="campo"><%=com.getSesso()%></font>
        </td>
        <td class="l" colspan="2">&nbsp;</td>
    </tr>
    <tr>
      	<td class="L">Data di Nascita:</td>
      	<td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(StringUtils.toStringJSP(DateUtils.getDateToString(com.getDataNascita(), "dd-MM-yyyy")))%>
        	</font>
        </td>
        <td class="L">Comune di Nascita:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(com.getDescComuneNascita())%></font>
        </td>
    </tr>
    <tr>
      	<td class="L">Stato di Nascita:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(com.getDescrStatoNascita())%></font>
        </td>
        <td class="L">Comune di Nascita Estero:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(com.getDescComuneNascitaEstero())%></font>
        </td>
    </tr>
    <tr>
      	<td class="L">Codice Fiscale:</td>
      	<td class="l">
        	<font class="campo"><%="G".equals(com.getCodPersona()) ? StringUtils.toStringJSP(com.getCodFiscaleRap()) : StringUtils.toStringJSP(com.getCodFiscale())%></font>
        </td>
        <td class="l" colspan="2">&nbsp;</td>
    </tr>
    <tr>
      	<td class="L">Pec:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(com.getPec())%></font>
        </td>
        <td class="L">Email:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(com.getEmail())%></font>
        </td>
    </tr>
<%
	if (com.getResidenza() != null) {
%>
    <tr>
		<td class="Titolo" colspan="4">Residenza/Domicilio</td>
	</tr>
	<tr>
      	<td class="L">Indirizzo:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(com.getResidenza().getIndirizzo())%></font>
        </td>
        <td class="L">Luogo:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(com.getResidenza().getDescrComune())%></font>
        </td>
    </tr>
    <tr>
      	<td class="L">CAP:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(com.getResidenza().getCap())%></font>
        </td>
        <td class="L">Comune Estero:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(com.getResidenza().getDescComuneEstero())%></font>
        </td>
    </tr>
    <tr>
      	<td class="L">Stato:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(com.getResidenza().getDescrStato())%></font>
        </td>
        <td class="l" colspan="2">&nbsp;</td>
    </tr>
    <tr><td>&nbsp;</td></tr>
<%
	}
}
%>
	<tr><td>&nbsp;</td></tr>
	<tr>
        <td class="l">
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">Gestione Difensore</a>
        </td>
	</tr>
</table>
</body>
</html>
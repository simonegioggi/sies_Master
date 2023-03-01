<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-13_ aggiunta pagina di dettaglio civilmente obbligato --%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="civilmenteObbligato" scope="request" class="siap.siep.pagoPA.model.CivilmenteObbligatoModel"/>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Civilmente Obbligato</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
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
		<!-- BOTTONE DI RITORNO -->
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	</tr>
</table>
</FORM>

<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
      	<td class="L">Cognome:</td>
      	<td class="l">
        	<font class="campo"><%=civilmenteObbligato.getCognome()%></font>
        </td>
        <td class="L">Nome:</td>
      	<td class="l">
        	<font class="campo"><%=civilmenteObbligato.getNome()%></font>
        </td>
    </tr>
    <tr>
      	<td class="L">Sesso:</td>
      	<td class="l">
        	<font class="campo"><%=civilmenteObbligato.getSesso()%></font>
        </td>
        <td class="l" colspan="2">&nbsp;</td>
    </tr>
    <tr>
      	<td class="L">Data di Nascita:</td>
      	<td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(StringUtils.toStringJSP(DateUtils.getDateToString(civilmenteObbligato.getDataNascita(), "dd-MM-yyyy")))%>
        	</font>
        </td>
        <td class="L">Comune di Nascita:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(civilmenteObbligato.getDescComuneNascita())%></font>
        </td>
    </tr>
    <tr>
      	<td class="L">Stato di Nascita:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(civilmenteObbligato.getDescrStatoNascita())%></font>
        </td>
        <td class="L">Comune di Nascita Estero:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(civilmenteObbligato.getDescComuneNascitaEstero())%></font>
        </td>
    </tr>
    <tr>
      	<td class="L">Codice Fiscale:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(civilmenteObbligato.getCodFiscale())%></font>
        </td>
        <td class="l" colspan="2">&nbsp;</td>
    </tr>
    <tr>
      	<td class="L">Pec:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(civilmenteObbligato.getPec())%></font>
        </td>
        <td class="L">Email:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(civilmenteObbligato.getEmail())%></font>
        </td>
    </tr>
<%
if (civilmenteObbligato.getResidenza() != null) {
%>
    <tr>
		<td class="Titolo" colspan="4">Residenza/Domicilio</td>
	</tr>
	<tr>
      	<td class="L">Indirizzo:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(civilmenteObbligato.getResidenza().getIndirizzo())%></font>
        </td>
        <td class="L">Luogo:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(civilmenteObbligato.getResidenza().getDescrComune())%></font>
        </td>
    </tr>
    <tr>
      	<td class="L">CAP:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(civilmenteObbligato.getResidenza().getCap())%></font>
        </td>
        <td class="L">Comune Estero:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(civilmenteObbligato.getResidenza().getDescComuneEstero())%></font>
        </td>
    </tr>
    <tr>
      	<td class="L">Stato:</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(civilmenteObbligato.getResidenza().getDescrStato())%></font>
        </td>
        <td class="l" colspan="2">&nbsp;</td>
    </tr>
<%
}
%>
</table>
</body>
</html>
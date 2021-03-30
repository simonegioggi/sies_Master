<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="java.util.Vector"%>
<%@ page import="f3b.util.StringUtils"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoAvvocato" scope="request" class="java.lang.String" />
<jsp:useBean id="avvocato" scope="request" class="siap.siep.avvocato.model.AvvocatoModel" />
<jsp:useBean id="flagModifica" scope="request" class="java.lang.String" />

<html>
<head>
<title>[S.I.E.S.] - GestioneAvvocato</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript">
function conferma(a_action, a_parameter, a_entityname, a_entityvalue, a_destnname,  a_destvalue, a_destnnameforo,  a_destvalueforo, a_destvaluef) {
	str = "/jsp/Main.jsp?Action=" + a_action + "&" +a_parameter +"=" + a_entityname + "&" +a_entityvalue +  "=" + a_destnname + "&" +a_destvalue + "=" +a_destnnameforo+ "&" +a_destvalueforo+ "="+a_destvaluef;
	if (window.confirm('Confermi la cancellazione ?')) {
		window.location.href=str;
	}
}
</script>
</head>
<%
AvvocatoModel lAvvocato = new AvvocatoModel(avvocato);
%>
<body class="corpo">
<table>
	<tr>
		<td class=LBG>
			<font class="label">Funzione :</font>&nbsp;&nbsp;
			<font class="campo">Dettaglio Avvocato</font>
		</td>
		<td class="LBG"><jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
			<jsp:param name="CampoIdEntita" value="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" />
			<jsp:param name="ValoreIdEntita" value="<%=avvocato.getIdAvvocato()%>" />
		    <jsp:param name="valoreufficio" value="<%=lAvvocato.getCodUffAppartenenza()%>" />
		</jsp:include></td>
	</tr>
</table>

<br>
<table cellspacing=2 cellpadding=2>
	<tr>
		<td class="l"><font class="label">Cognome</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getCognome())%></font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Nome</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getNome())%>&nbsp;</font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Luogo Nascita</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getDescLuogoNascita())%>&nbsp;</font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Data Nascita</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
							lAvvocato.getDataNascita(), "dd-MM-yyyy"))%>&nbsp;</font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Foro</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getForo())%></font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Indirizzo</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getIndirizzo())%></font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l"><font class="label">Con Studio in</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getDescComuneResidenza())%></font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l"><font class="label">Telefono</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getTelefono())%></font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l"><font class="label">Fax</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getFax())%></font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l"><font class="label">EMail</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getEMail())%></font>&nbsp;</td>
	</tr>
	<%-- MEV_21: aggiunto campo per chiamata a WS per individuare lista avvocato in RegInde --%>
    <tr>
		<td class="l"><font class="label">Pec</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getPec())%></font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l"><font class="label">Codice Fiscale</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getCodiceFiscale())%></font>&nbsp;</td>
	</tr>
<%
if (flagModifica.equals("S")) {
%>
	<tr>
		<td class="l"><font class="label">Sospeso fino al</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvvocato.getDataSospensione(), "dd-MM-yyyy"))%></font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l"><font class="label">Radiato dal</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvvocato.getDataRadiazione(), "dd-MM-yyyy"))%></font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l"><font class="label">Non in attività per </font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getDescrNonAttivita())%></font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l"><font class="label">Note </font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getNote())%></font>&nbsp;</td>
	</tr>
<%
}
%>
</table>
</body>
</html>
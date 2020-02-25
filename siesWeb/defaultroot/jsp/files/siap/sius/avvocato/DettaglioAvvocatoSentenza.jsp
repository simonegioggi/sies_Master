<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoFascicoloSiusModel"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoSiusModel"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocato"%>
<%@ page import="java.util.Vector"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Avvocato</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/conferma.js"></script>
</head>

<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="avvocatoFascSius" scope="request"
	class="siap.sius.avvocato.model.AvvocatoSiusModel" />
<%
			AvvocatoModel lAvvocato = new AvvocatoModel(avvocatoFascSius
			.getAvvocato());

	System.out
			.println(" mi trovo nella JSP Dettaglio Avvocato sentenza ");
%>

<body class="corpo">
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();">
		<img align="middle"
			src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
			alt="Stampa questa videata" border=0></a></td>
		<td class=LBG><font class="label">Funzione :</font>&nbsp;&nbsp; <font
			class="campo">Dettaglio Avvocato</font></td>
		<td class="LBG"><jsp:include
			page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
			<jsp:param name="CampoIdEntita"
				value="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" />
			<jsp:param name="ValoreIdEntita"
				value="<%=lAvvocato.getIdAvvocato()%>" />
		</jsp:include> <!-- BOTTONE DI RITORNO --> <jsp:include
			page="<%=IWebConstants.PG_RETURN_BUTTON%>" /></td>
	</tr>
</table>
<br>
<jsp:include
	page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>" />
<br>



<%--br>
<jsp:include page="/jsp/files/siap/sius/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br--%>
<table cellspacing=2 cellpadding=2>

	<tr>
		<td class="l"><font class="label">Cognome</font></td>
		<td class="l"><font class="campo"><%=lAvvocato.getCognome()%></font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Nome</font></td>
		<td class="l"><font class="campo"><%=lAvvocato.getNome()%>
		&nbsp;</font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Foro</font></td>
		<td class="l"><font class="campo"><%=lAvvocato.getForo()%></font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Indirizzo</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getIndirizzo(), "-")%></font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Telefono</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getTelefono(), "-")%></font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Fax</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getFax(), "-")%></font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">EMail</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getEMail(), "-")%></font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Codice Fiscale</font></td>
		<td class="l"><font class="campo"><%=StringUtils
							.toStringJSP(lAvvocato.getCodiceFiscale(), "-")%></font></td>
	</tr>

	<tr>
		<td class="l"><font class="label">Tipo</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getDescrTipo(), "-")%></font></td>
	</tr>
</table>
</body>
</html>
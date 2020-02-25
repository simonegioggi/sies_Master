<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.regesies.regescarti.model.RegeFileModel"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.regesies.regescarti.action.ICostantiRegeFile"%>
<%@ page import="siap.regesies.regescarti.model.RegeFileModel"%>

<jsp:useBean id="regefile" scope="request" class="siap.regesies.regescarti.model.RegeFileModel"/>
<html>
	<head>
		<title>[S.I.A.P.] - Dettaglio file scartato REGE </title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<script language="JavaScript" src="/html/conferma.js"></script>
	</head>

	<body class="corpo">
		<FORM name="comandi" >
		    <table>
		      <tr>
		        <td class="LBG">
		        <font class="label">Funzione :</font>&nbsp;
		        <font class="campo">Dettaglio File Scartato REGE</font>
		      </td>
			  <!-- Inserimento bottoni e combo -->
		      <td class="LBG">
		        <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
		        	<jsp:param name="CampoIdEntita" value="<%=ICostantiRegeFile.CAMPO_ID_FILE%>" />
					<jsp:param name="ValoreIdEntita" value="<%=regefile.getIdFile()%>" />
		       	</jsp:include>
		     </td>
		   </tr>
		 </table>
		</FORM>

		<table cellspacing=4 cellpadding=4>
			<tr>
				<td class="l" nowrap>Data Inserimento</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(regefile.getDataInserimento(),"dd-MM-yyyy hh.mm.ss"))%> </font></td>
			</tr>
			<tr>
				<td class="l">Nome File</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(regefile.getIdFile()) %></font></td>
			</tr>
			<tr>
				<td class="l">Comune</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(regefile.getDescrComune()) %></font></td>
			</tr>
			<tr>
				<td class="l" nowrap>Descrizione Errore</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(regefile.getDescErr()) %></font></td>
			</tr>

		</table>
	</body>
</html>
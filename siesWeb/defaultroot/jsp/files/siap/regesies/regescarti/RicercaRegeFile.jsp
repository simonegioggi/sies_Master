<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Vector" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="siap.regesies.regescarti.action.ICostantiRegeFile"%>
<%@ page import="siap.regesies.regescarti.model.RegeFileModel"%>

<jsp:useBean id="elencoregefile" scope="request" class="java.util.Vector" />
<html>
	<head>
		<title>[S.I.A.P.] - Elenco file scartati REGE </title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<script language="JavaScript" src="/html/conferma.js"></script>
	</head>

	<body  class="corpo">
		<!-- INTESTAZIONE -->
		<FORM name="comandi" >
			<table>
    			<tr>
			        <td class="LBG">
				        <font class="label">Funzione :</font>&nbsp;
				        <font class="campo">Elenco file scartati REGE</font>
			      	</td>

   				</tr>
 			</table>
		</FORM>
		<!-- Include Per la PAGINAZIONE -->
		<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
		<br />
		<div align=center>
			<table width="100%">
				<tr>
					<td class=titolo colspan="4">File Scartati</td>
				</tr>
				<tr>
				    <td class="int">Data Inserimento</td>
				    <td class="int">Nome File</td>
				    <td class="int">Comune</td>
					<td class="int">Azioni</td>
				</tr>

				<%
   				Iterator itx = elencoregefile.iterator();
  				while ( itx.hasNext())
  				{
					RegeFileModel nFile = (RegeFileModel)itx.next();
				%>
					<tr>
						<td class="l" nowrap><%=StringUtils.toStringJSP(DateUtils.getDateToString(nFile.getDataInserimento(), "dd-MM-yyyy hh.mm.ss") )%>&nbsp;</td>
						<td class="l"><%=StringUtils.toStringJSP(nFile.getIdFile()) %>&nbsp;</td>
						<td class="l"><%=StringUtils.toStringJSP(nFile.getDescrComune()) %>&nbsp;</td>
						<!-- Inserimento bottoni per la visualizzione dettaglio e la cancellazione -->
						<td class="l">
							<jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           						<jsp:param name="CampoIdEntita" value="<%=ICostantiRegeFile.CAMPO_ID_FILE%>" />
								<jsp:param name="ValoreIdEntita" value="<%=nFile.getIdFile()%>" />
        					</jsp:include>
						</td>
					</tr>
				<%
				}
				%>


			</table>

		</div>

	</body>
</html>
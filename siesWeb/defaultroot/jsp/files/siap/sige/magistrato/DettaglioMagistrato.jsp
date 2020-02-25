<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sige.magistrato.model.MagistratoModel"%>
<%@ page import="siap.sige.magistrato.action.ICostantiMagistrato"%>

<jsp:useBean id="magistrato" scope="request" class="siap.sige.magistrato.model.MagistratoModel"/>

<html>

	<head>
		<title>[S.I.E.S.] - Dettaglio Magistrato </title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<script language="JavaScript" src="/html/conferma.js">
		</script>

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
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Magistrato</font>
        </td>
      	<td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
          	<jsp:param name="CampoIdEntita" value="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" />
          	<jsp:param name="ValoreIdEntita" value="<%=magistrato.getCodMagistrato()%>" />
       		</jsp:include>
     		</td>
        <!-- BOTTONE DI RITORNO -->
				<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
   		</tr>
 		</table>
</FORM>
	<table cellspacing=2 cellpadding=2>
		<tr>
			<td class="l">Codice Magistrato</td>
			<td class="l">
				<font class="campo"><%=magistrato.getCodMagistrato()%></font>
			</td>
		</tr>
		<tr>
			<td class="l">Cognome</td>
			<td class="l">
				<font class="campo"><%=magistrato.getCognome()%></font>
			</td>
		</tr>
		<tr>
			<td class="l">Nome</td>
			<td class="l">
				<font class="campo"><%=magistrato.getNome()%></font>
			</td>
		</tr>
	    <tr>
			<td class="l">Email Ufficio</td>
			<td class="l">
				<font class="campo"><%=StringUtils.toStringJSP(magistrato.getEMailUfficio(),"-")%></font>
			</td>
		</tr>
	    <tr>
			<td class="l">Email Privata</td>
			<td class="l">
				<font class="campo"><%=StringUtils.toStringJSP(magistrato.getEMailPrivata(),"-")%></font>
			</td>
		</tr>
	    <tr>
			<td class="l">Num. Cell.</td>
			<td class="l">
				<font class="campo"><%=StringUtils.toStringJSP(magistrato.getNumCellulare(),"-")%></font>
			</td>
		</tr>
    	<tr>
			<td class="l">Disponibilità</td>
			<td class="l">
				<font class="campo"><%=magistrato.getDescrFlagStato()%></font>				
			</td>
			<td class="l">Data Fine Validità</td>
			<td class="l">
				<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(magistrato.getDataFineValidita(),"dd-MM-yyyy"),"-")%> </font>
			</td>
    	</tr>
    	
    	<tr><td>&nbsp;</td></tr>

   	<%
		if( magistrato.getMagistratoSezioni() != null)
		{
		  for( int i=0; i<magistrato.getMagistratoSezioni().length; i++ )
		  {
			  if(magistrato.getMagistratoSezioni()[i].getSezione().getCodice() != null)
			  {
	%>
				<tr>
					<td class="l">Sezione</td>
					<td class="l">
						<font class="campo">
							<%=magistrato.getMagistratoSezioni()[i].getSezione().getCodice()%>
							-
							<%=magistrato.getMagistratoSezioni()[i].getSezione().getDescrizione()%>
						</font>
					</td>
					<td class="l">Data Inizio Assegnazione</td>
					<td class="l">
						<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(magistrato.getMagistratoSezioni()[i].getDataInizioAssegnazione(),"dd-MM-yyyy"),"-")%> </font>
					</td>
				
					<td class="l">Data Fine Assegnazione</td>
					<td class="l">
						<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(magistrato.getMagistratoSezioni()[i].getDataFineAssegnazione(),"dd-MM-yyyy"),"-")%> </font>
					</td>
				</tr>
	<% 
			  }
		  }
		}
	%>	
		
	</table>
</body>
</html>
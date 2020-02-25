<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="Parametro" scope="request" class="siap.siep.parametro.model.ParametroModel"/>

<html>
<head>
	<title>[S.I.E.S.] - Dettaglio Periodo Feriale </title>
	
	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	
	<script language="JavaScript" src="/html/conferma.js"></script>
	</head>
	
	<body class="corpo">
		<table>
	  	<tr>
	  		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
	      <td class="LBG">
	      	<font class="label">Funzione :</font>&nbsp;
	        <font class="campo">Dettaglio Periodo Feriale</font>
	      </td>
	    </tr>
	 	</table>
		<br>
		<table cellspacing=2 cellpadding=2>
			<tr>
      	<td class="int" width=30%>Ufficio</td>
      	<td class="l"><%=UtenteConnesso.getUfficioUtente().getDescrTipoUfficio() + " DI <font color=navy>" + UtenteConnesso.getUfficioUtente().getDescrComune() %></td>
			</tr>
			<tr>
      	<td class="int">Data Inizio Periodo</td>
      	<td class="l">
      		<%= StringUtils.toStringJSP(DateUtils.getDateToString(Parametro.getDataInizioValidita(),"dd-MM-yyyy")) %></td>
			</tr>
			<tr>
       	<td class="int">Data Fine Periodo</td>
       	<td class="l"><%= StringUtils.toStringJSP(DateUtils.getDateToString(Parametro.getDataFineValidita(),"dd-MM-yyyy")) %></td>
			</tr>
		</table>
</body>
</html>
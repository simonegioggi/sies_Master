<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.parametro.model.ParametroModel"%>

<jsp:useBean id="parametro" scope="request" class="siap.siep.parametro.model.ParametroModel"/>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Parametro </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>


<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Scadenza Vane Ricerche</font>
      </td>
   </tr>
 </table>
</FORM>

<table cellspacing=4 cellpadding=4>
	<tr>
		<td class="L" > Paramentro Scadenza : </td>
		<td class="l"><font class="campo"><%=parametro.getNomeParametro()%></font> </td>
    </tr>
	<tr>
      	<td class="L" > Scadenza entro : </td>
      	<td class="l"> &nbsp;Anni&nbsp;
        	<font class="campo"><%=parametro.getAnni() %></font>
		 				&nbsp;Mesi&nbsp;
        	<font class="campo"><%=parametro.getMesi() %></font>
 		 				&nbsp;Giorni&nbsp;
        	<font class="campo"><%=parametro.getGiorni() %></font>
        </td>
    </tr>
</table>
  </body>
</html>
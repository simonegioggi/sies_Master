<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="ufficio" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<html>
<head>
<title>[S.I.A.P.] - Dettaglio Ufficio </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>


<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Ufficio</font>
      </td>
      <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiUfficio.CAMPO_COD_UFFICIO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=ufficio.getCodUfficio()%>" />
       </jsp:include>
     </td>
   </tr>
 </table>
</FORM>
		 <table cellspacing=4 cellpadding=4 width="99%">
		<tr>
				<td class="l">Ufficio</td>
				<td class="l"><font class="campo"><%=ufficio.getDescrTipoUfficio() + " di " +ufficio.getDescrComune() + " (" + ufficio.getCodProvincia() + ")" %></font></td>
		</tr>
		<tr>
				<td class="l">Indirizzo</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(ufficio.getIndirizzo(), " ") %></font></td>
		</tr>
		<tr>
				<td class="l">Cap</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(ufficio.getCap(), " ") %></font></td>
		</tr>
		<tr>
				<td class="l">Telefono</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(ufficio.getTelefono(), " ") %></font></td>
		</tr>
		<tr>
				<td class="l">Fax</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(ufficio.getFax(), " ") %></font></td>
		</tr>
		<tr>
				<td class="l">EMail</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(ufficio.getEMail(), " ") %></font></td>
		</tr>
		</table>	</body>
</html>
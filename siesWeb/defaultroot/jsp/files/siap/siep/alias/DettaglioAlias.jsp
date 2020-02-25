<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.alias.model.AliasModel"%>
<%@ page import="siap.siep.alias.action.ICostantiAlias"%>

<jsp:useBean id="alias" scope="request" class="siap.siep.alias.model.AliasModel"/>

<% // n.b. JSP NON UTILIZZATA (13/03/2009) %>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Alias </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>


<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Alias</font>
      </td>
      <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiAlias.CAMPO_ID_ALIAS%>" />
          <jsp:param name="ValoreIdEntita" value="<%=alias.getIdAlias()%>" />
       </jsp:include>
     </td>
   </tr>
 </table>
</FORM>
		 <table cellspacing=4 cellpadding=4>
		<tr>
				<td class="l">IdAlias</td>
				<td class="l"><font class="campo"><%=alias.getIdAlias() %></font></td>
		</tr>
		<tr>
				<td class="l">Cognome</td>
				<td class="l"><font class="campo"><%=alias.getCognome() %></font></td>
		</tr>
		<tr>
				<td class="l">Nome</td>
				<td class="l"><font class="campo"><%=alias.getNome() %></font></td>
		</tr>
		<tr>
				<td class="l">Paternita</td>
				<td class="l"><font class="campo"><%=alias.getPaternita() %></font></td>
		</tr>
		<tr>
				<td class="l">CodFiscale</td>
				<td class="l"><font class="campo"><%=alias.getCodFiscale() %></font></td>
		</tr>
		<tr>
				<td class="l">CodCs</td>
				<td class="l"><font class="campo"><%=alias.getCodCs() %></font></td>
		</tr>
		<tr>
				<td class="l">CodAfis</td>
				<td class="l"><font class="campo"><%=alias.getCodAfis() %></font></td>
		</tr>
		<tr>
				<td class="l">AttoNascita</td>
				<td class="l"><font class="campo"><%=alias.getAttoNascita() %></font></td>
		</tr>
		<tr>
				<td class="l">Sesso</td>
				<td class="l"><font class="campo"><%=alias.getSesso() %></font></td>
		</tr>
		<tr>
				<td class="l">CodComuneNascita</td>
				<td class="l"><font class="campo"><%=alias.getCodComuneNascita() %></font></td>
		</tr>
		<tr>
				<td class="l">CodProvinciaNascita</td>
				<td class="l"><font class="campo"><%=alias.getCodProvinciaNascita() %></font></td>
		</tr>
		<tr>
				<td class="l">CodStatoNascita</td>
				<td class="l"><font class="campo"><%=alias.getCodStatoNascita() %></font></td>
		</tr>
		<tr>
				<td class="l">DataNascita</td>
				<td class="l"><font class="campo"><%=DateUtils.getDateToString(alias.getDataNascita(),"dd-MM-yyyy")%> </font></td>
		</tr>
		<tr>
				<td class="l">Note</td>
				<td class="l"><font class="campo"><%=alias.getNote() %></font></td>
		</tr>
		<tr>
				<td class="l">CodOperatoreInserimento</td>
				<td class="l"><font class="campo"><%=alias.getCodOperatoreInserimento() %></font></td>
		</tr>
		<tr>
				<td class="l">DataInserimento</td>
				<td class="l"><font class="campo"><%=DateUtils.getDateToString(alias.getDataInserimento(),"dd-MM-yyyy")%> </font></td>
		</tr>
		<tr>
				<td class="l">CodUfficioInserimento</td>
				<td class="l"><font class="campo"><%=alias.getCodUfficioInserimento() %></font></td>
		</tr>
		<tr>
				<td class="l">CodOperatoreAggiornamento</td>
				<td class="l"><font class="campo"><%=alias.getCodOperatoreAggiornamento() %></font></td>
		</tr>
		<tr>
				<td class="l">DataAggiornamento</td>
				<td class="l"><font class="campo"><%=DateUtils.getDateToString(alias.getDataAggiornamento(),"dd-MM-yyyy")%> </font></td>
		</tr>
		<tr>
				<td class="l">CodUfficioAggiornamento</td>
				<td class="l"><font class="campo"><%=alias.getCodUfficioAggiornamento() %></font></td>
		</tr>
		<tr>
				<td class="l">SogIdSoggetto</td>
				<td class="l"><font class="campo"><%=alias.getSogIdSoggetto() %></font></td>
		</tr>
		</table>	</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.scarti.model.WScartiModel"%>
<%@ page import="siap.siep.scarti.action.ICostantiWScarti"%>

<jsp:useBean id="wscarti" scope="request" class="siap.siep.scarti.model.WScartiModel"/>

<% // Mai utilizzata (13_03_2009) %>

<html>
<head>		
<title>[S.I.A.P.] - Dettaglio WScarti </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>


		<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio WScarti</font>
      </td>
      <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiWScarti.CAMPO_ID_SCARTI%>" />
          <jsp:param name="ValoreIdEntita" value="<%=wscarti.getIdScarti()%>" />
       </jsp:include>
     </td>
   </tr>
 </table>
</FORM>
		 <table cellspacing=4 cellpadding=4>
		<tr>
				<td class="l">IdScarti</td>
				<td class="l"><font class="campo"><%=wscarti.getIdScarti() %></font></td>
		</tr>
		<tr>
				<td class="l">Tabella</td>
				<td class="l"><font class="campo"><%=wscarti.getTabella() %></font></td>
		</tr>
		<tr>
				<td class="l">AnnRes</td>
				<td class="l"><font class="campo"><%=wscarti.getAnnRes() %></font></td>
		</tr>
		<tr>
				<td class="l">NumRes</td>
				<td class="l"><font class="campo"><%=wscarti.getNumRes() %></font></td>
		</tr>
		<tr>
				<td class="l">LetRes</td>
				<td class="l"><font class="campo"><%=wscarti.getLetRes() %></font></td>
		</tr>
		<tr>
				<td class="l">ChiaveAlternativa</td>
				<td class="l"><font class="campo"><%=wscarti.getChiaveAlternativa() %></font></td>
		</tr>
		<tr>
				<td class="l">NoteScarto</td>
				<td class="l"><font class="campo"><%=wscarti.getNoteScarto() %></font></td>
		</tr>
		<tr>
				<td class="l">CausaScarto</td>
				<td class="l"><font class="campo"><%=wscarti.getCausaScarto() %></font></td>
		</tr>
		</table>	</body>
</html>
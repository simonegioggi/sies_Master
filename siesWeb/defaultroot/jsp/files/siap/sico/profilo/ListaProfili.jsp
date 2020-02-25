<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.profilo.model.ProfiloModel"%>
<%@ page import="siap.sico.profilo.action.ICostantiProfilo"%>
<jsp:useBean id="Profili" scope="request" class="java.util.Vector"/>
<html>
<head>
<title>[S.I.E.S.] - Dettaglio Profilo </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>


		<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Lista Profili</font>
      </td>

   </tr>
 </table>
</FORM>
		 <table cellspacing=4 cellpadding=4>
                 <tr>
				<td class="int">Descrizione</td>
				<td class="int">Data Fine Validita</font></td>
                                <td class="int">Azioni</td>
		</tr>
<%
ProfiloModel profilo=new ProfiloModel();
for (int i=0;i<Profili.size();i++)
{
   profilo=(ProfiloModel)Profili.get(i);
%>

		<tr>

				<td class="l"><font class="campo"><%=profilo.getDescrizione() %></font></td>
                                <td class="c"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(profilo.getDataFineValidita(),"dd-MM-yyyy"),"-")%> </font></td>
                                <td class="c">
                                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.profilo.action.ActLoadModificaProfilo&<%=ICostantiProfilo.CAMPO_COD_PROFILO%>=<%=profilo.getCodProfilo()%>"><img src="/images/modifica.gif" border=0></a>
                                <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
                                <%--<a href="Javascript:conferma('siap.sico.profilo.action.ActCancellaProfilo','< %=ICostantiProfilo.CAMPO_COD_PROFILO%>','< %=profilo.getCodProfilo()%>')"><img src="/images/delete.gif" border=0></a>--%>
                                </td>
		</tr>

<%}%>
		</table>	</body>
</html>
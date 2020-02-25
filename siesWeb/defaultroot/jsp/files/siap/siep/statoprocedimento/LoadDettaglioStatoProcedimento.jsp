<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.statoprocedimento.action.ICostantiStatoProcedimento"%>
<%@ page import="siap.siep.statoprocedimento.model.StatoProcedimentoModel"%>

<html>

<jsp:useBean id="statoprocedimento" scope="request" class="java.util.Vector"/>

<head>
<title>[S.I.E.S.] - Stato del Procedimento </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>
<body class="corpo">
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
     <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <font class="campo">Dettaglio Assegnazione Stato del Procedimento</font>
     </td>
    </tr>
   </table>
    <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
		 <table cellspacing=2 cellpadding=2>
 <%
    Iterator iter = statoprocedimento.iterator();
    while (iter.hasNext())
    {
      StatoProcedimentoModel lStatoPr =(StatoProcedimentoModel) iter.next();
 %>

      <tr>
         <td class="l">Stato del Procedimento </td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(lStatoPr.getDescrStatoProcedimento())%></font></td>
     </tr>

		 <tr>
				<td class="l">Data</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lStatoPr.getData(),"dd-MM-yyyy"))%></font></td>
     </tr>
<%
    }
%>
</table>
		</form>
	</body>
</html>
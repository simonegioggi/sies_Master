<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<%
	boolean cancellabile = true;
	
	if(request.getParameter("Cancellabile").equals("SI")){
		cancellabile = true;
	} else {
		cancellabile = false;
	}
%>

  <table>
    <tr>
 <% if(cancellabile){ %>

       <td>
         <a href="Javascript:conferma('siap.sico.soggettodattilo.action.ActCancellaSoggettoDattilo','<%=request.getParameter("CampoIdSoggetto")%>','<%=request.getParameter("ValoreIdSoggetto")%>','<%=request.getParameter("CampoIdDattilo")%>','<%=request.getParameter("ValoreIdDattilo")%>');">
           <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
         </a>
       </td>
<%  } %>
       <td>
         <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggettodattilo.action.ActDownloadSoggettoDattilo&<%=request.getParameter("CampoIdDattilo")%>=<%=request.getParameter("ValoreIdDattilo")%>')">
           <img src="/images/print.gif" alt="Stampa" width="12" height="12" border="0">
         </a>
       </td>
    </tr>
  </table>
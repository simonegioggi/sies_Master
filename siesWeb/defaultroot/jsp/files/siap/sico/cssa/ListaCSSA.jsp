<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA" %>
<%@ page import="siap.sico.cssa.model.CSSAModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.model.DecodeModel" %>
<%@ page import="f3b.util.StringUtils" %>

<jsp:useBean id="ListaCSSA" scope="request" class="java.util.Vector" />

<%@page import="f3b.log.LogF3B"%>
<html>
  <head>
    <title>[S.I.E.S.] - Lista Uffici di Esecuzione Penale Esterna</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <script language="JavaScript">
      function insertIT(str, aIdCSSA)
      {
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=str;
<%      if (request.getParameter("fieldcode")!= null )
        {%>
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldcode")%>.value=aIdCSSA;
       <%}%>
        window.parent.close();
      }
    </script>
  </head>

  <body onload="focus();">
   <table>
      <tr>
        <td class=LBG>Elenco Uffici di Esecuzione Penale Esterna</td>
      </tr>
   </table>
   <Table width="100%">
<%
      Iterator itx = ListaCSSA.iterator();

      while ( itx.hasNext())
      {
        CSSAModel cssa = (CSSAModel)itx.next();
        String tipoSede = (cssa.getTipo().indexOf("UEPESS")>=0 ? " (Sede di Servizio) " : "");
%>
        <tr>
          <td class=l><%=cssa.getComune()+tipoSede%></td>
          <td class=c><a href="Javascript:insertIT('<%=StringUtils.cStrForJS(cssa.getComune())%>', '<%=StringUtils.cStrForJS(cssa.getIdCSSA().toString())%>')"><img align="middle" src="/images/fileselected.gif" border=0></a></td>
        </tr>
<%
    }
%>
  </table>

  </body>
</html>
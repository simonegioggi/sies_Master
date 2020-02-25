<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.model.DecodeModel" %>
<%@ page import="f3b.util.StringUtils" %>

<jsp:useBean id="ListaUDS" scope="request" class="java.util.Vector" />

<%
    String NomeLista = request.getParameter("NomeLista");

    // Valorizzazione del titolo
    if ((NomeLista == null ) || (NomeLista.length() <1)) {
      NomeLista = "Lista Uffici di Sorveglianza";
    }
%>


<html>
  <head>
    <title>[S.I.E.S.] - Lista Uffici di Sorveglianza </title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <script language="JavaScript">
      function insertIT(str)
      {
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=str;
        window.parent.close();
      }
    </script>
  </head>

  <body onload="focus();">
   <table>
      <tr>
        <td class=LBG><%=NomeLista%></td>
      </tr>
   </table>
   <Table width="100%">
<%
      Iterator itx = ListaUDS.iterator();

      while ( itx.hasNext())
      {
        UfficioModel ufficio = (UfficioModel)itx.next();
%>
        <tr>
          <td class=l><%=ufficio.getDescrComune()%></td>
          <td class=c><a href="Javascript:insertIT('<%=StringUtils.cStrForJS(ufficio.getDescrComune() )%>')"><img align="middle" src="/images/fileselected.gif" border=0></a></td>
        </tr>
<%
    }
%>
  </table>

  </body>

<script language="Javascript">
window.focus();
</script>

</html>
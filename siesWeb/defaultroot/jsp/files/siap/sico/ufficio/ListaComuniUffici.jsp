<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="ListaUffici" scope="request" class="java.util.Vector" />
<jsp:useBean id="Funzione" scope="request" class="java.lang.String" />

<html>
  <head>
    <title>[S.I.E.S.] - Lista Uffici - </title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <script language="JavaScript">
      function insertIT(str,competente)
      {
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=str;
        if (window.parent.opener.loadUfficiAccorpati) {
            window.parent.opener.loadUfficiAccorpati(competente);
        } 
        window.parent.close();
      }
    </script>
  </head>

  <body onload="focus();">
   <table>
      <tr>
        <td class="LBG"><%=Funzione%></td>
      </tr>
   </table>
   <table width="100%">
<%
      Iterator itx = ListaUffici.iterator();

      while ( itx.hasNext())
      {
        UfficioModel ufficio = (UfficioModel)itx.next();
%>
        <tr>
          <td class="l"><%=ufficio.getDescrComune()%></td>
          <td class="c"><a href="Javascript:insertIT('<%=StringUtils.cStrForJS(ufficio.getDescrComune())%>','<%=StringUtils.cStrForJS(ufficio.getCodUfficio())%>')"><img align="middle" src="/images/fileselected.gif" border=0></a></td>
        </tr>
<%
    }
%>
  </table>

  </body>
  <script language=javascript>
    window.focus();
  </script>
</html>
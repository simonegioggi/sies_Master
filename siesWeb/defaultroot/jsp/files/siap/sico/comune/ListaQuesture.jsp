<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel" %>

<jsp:useBean id="ListaQuesture" scope="request" class="java.util.Vector" />


<%
//==============================================================================
// Pop Up con la lista delle Questure
// Carica nella form chiamante il Nome del Comune
// ListaQuesture = Vettore di DecodificheModel 
// DecodificheModel.getCode() = CG_REF_CODES.RV_HIGH_VALUE (COMUNE.COD_COMUNE)
// DecodificheModel.getDescription() = Nome comune: CG_REF_CODES.RV_MEANING (COMUNE.DESCRIZIONE)
//==============================================================================
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>> </script>
    <script language="JavaScript">
      function insertIT(str)
      {
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=str;
        window.parent.close();
      }
    </script>
    <title>[S.I.E.S.] - Elenco delle Questure</title>
  </head>



<body class="corpo" onload="javascript:focus();">
  <table>
    <tr>
      <td class="LBG">Elenco delle Questure</td>
    </tr>
  </table>

  <table width="100%">

  <%
    Iterator itx = ListaQuesture.iterator();

    while ( itx.hasNext())
    {
      DecodificheModel lQuestura = (DecodificheModel)itx.next();
    %>
    <tr>
        <td class=l><%=lQuestura.getDescription()%></td>
        <td class=c>
          <a href="Javascript:insertIT('<%=StringUtils.cStrForJS(lQuestura.getDescription())%>')">
            <img align="middle" src="/images/fileselected.gif" border="0">
          </a>
        </td>
    </tr>
<% } %>
  </table>
</body>
</html>
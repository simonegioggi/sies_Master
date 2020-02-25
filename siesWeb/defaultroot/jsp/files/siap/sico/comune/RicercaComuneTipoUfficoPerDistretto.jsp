<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>

<jsp:useBean id="Lista" scope="request" class="java.util.Vector" />
<jsp:useBean id="comune" scope="request" class="java.lang.String" />
<html>
  <head>
    <title>[S.I.E.S.] - Lista Comuni</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <script language="JavaScript">
    function insertIT(str,codDistretto)
    {
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=str;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname2")%>.value=codDistretto;

   window.parent.close();
    }

    function checkListaComuni()
    {
      if ("<%=Lista.size()%>" == 0)
        alert('Attenzione! Nessun Comune trovato.');
      if ("<%=Lista.size()%>" == 200)
        alert('Attenzione! Visualizzati solo i primi 200 comuni individuati. Perfezionare la ricerca!');
    }
    </script>

  </head>

  <body class="corpo" onload="javascript:focus();checkListaComuni();">
    <table>
      <tr>
        <td class=LBG>Elenco Uffici
<%        if ((comune !=" ") || comune.length()>1 ) // Stabilisco che ho fatto la ricerca per filtra comune.
          {%> con filtro '<%=comune%>'
        <%} else
          {%> della provincia di <%=( (UfficioModel)Lista.elementAt(0) ).getDescrComune()%>
        <%}%>
        </td>
      </tr>
    </table>
    <Table width="100%">
<%

      Iterator itx = Lista.iterator();
      while ( itx.hasNext())
      {
        UfficioModel lufficio = (UfficioModel)itx.next();
%>
        <tr>
        <%if ( Lista.size()>0 )
          {%>
            <td class=l><%=lufficio.getDescrTipoUfficio()%></td>
            <td class=l><%=lufficio.getDescrComune()%></td>

        <%}%>
        <td class=c><a href="Javascript:insertIT('<%=StringUtils.cStrForJS(lufficio.getDescrComune())%>','<%=StringUtils.cStrForJS(lufficio.getCodDistretto())%>')"><img align="middle" src="/images/fileselected.gif" border=0></a></td></tr>
        <%
      }
%>
    </table>
  </body>
</html>
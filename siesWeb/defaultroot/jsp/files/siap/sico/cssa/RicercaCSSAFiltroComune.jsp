<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sico.cssa.action.ICostantiCSSA" %>
<%@ page import="siap.sico.cssa.model.CSSAModel" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>

<jsp:useBean id="ListaComuni" scope="request" class="java.util.Vector" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="formname" scope="request" class="java.lang.String" />

<%
    String NomeFunzione = request.getParameter("NomeFunzione");

    // Valorizzazione del titolo
    if ((NomeFunzione == null ) || (NomeFunzione.length() <1)) {
    	NomeFunzione = "Elenco Uffici di Esecuzione Penale Esterna";
    }
%>

<html>
  <head>
    <title>[S.I.E.S.] - Lista UEPE</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <% if (! modalita.equals("NoPop"))
    {
    %>
    <script language="JavaScript">
        function insertIT(id,comuneind)
        {
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=id;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("field2")%>.value=comuneind;
          window.parent.close();
          return;
        }
    </script>
    <%
    }
    %>
    <script language="JavaScript">

 function focus()
    {
      if ("<%=ListaComuni.size()%>" == 0)
        alert('Attenzione! Nessun UEPE trovato.');
      if ("<%=ListaComuni.size()%>" == 200)
        alert('Attenzione! Visualizzati solo i primi 200 UEPE individuati. Perfezionare la ricerca!');
    }
    </script>

  </head>

  <body class=corpo onload="focus();">
    <table>
      <tr>
        <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"><%=NomeFunzione%></font></td>
      </tr>
    </table>
<%if(ListaComuni.size()>0){%>
    <Table width="100%">
    <tr>
    <td class=int>Indirizzo</td>
    <td class=int>Comune</td>
    <% if (! modalita.equals("NoPop"))
    { %>
      <td class=int>Seleziona</td>
    <%
    } %>
    </tr>
    <%
    Iterator itx = ListaComuni.iterator();


    while ( itx.hasNext() )
    {
      CSSAModel lCssa = (CSSAModel)itx.next();

    %>
      <tr>
        <td class=l><%=StringUtils.toStringJSP(lCssa.getIndirizzo(),"-")%></td>
        <td class=l><%=StringUtils.toStringJSP(lCssa.getComune(),"-")%></td>
      <%
      if (! modalita.equals("NoPop"))
      { %>
        <td class=c><a href="Javascript:insertIT('<%=lCssa.getIdCSSA()%>','<%=StringUtils.cStrForJS(lCssa.getComune()) + "-" + StringUtils.cStrForJS(lCssa.getIndirizzo())%>');"> <img align="middle" src="/images/fileselected.gif" border=0></a></td>
        <% } %>
      </tr>
    <%
    }
    %>
    </table>
<%}%>
  </body>
</html>
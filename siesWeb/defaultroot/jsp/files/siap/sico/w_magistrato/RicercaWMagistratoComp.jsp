<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato" %>
<%@ page import="siap.sico.w_magistrato.model.WMagistratoModel" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="wmagistrati" scope="request" class="java.util.Vector" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="formname" scope="request" class="java.lang.String" />
<html>
  <head>
    <title>[S.I.E.S.] - Lista Magistrati</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <% if (! modalita.equals("NoPop"))
    {
    %>
    <script language="JavaScript">
        function insertIT(cod_comp,cognome_comp,nome_comp)
        {
          window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>_comp.value=cod_comp;
          window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COGNOME%>_comp.value=cognome_comp;
          window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_NOME%>_comp.value=nome_comp;
          window.parent.close();
          return;
        }
    </script>
    <%
    }
    %>
  </head>

  <body class=corpo>
    <table>
      <tr>
        <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Magistrati CSM</font></td>
      </tr>
    </table>

    <Table width="100%">
    <tr>
    <td class=int>Cod</td>
    <td class=int>Nome</td>
    <td class=int>Data Nascita</td>
    <% if (! modalita.equals("NoPop"))
    { %>
      <td class=int>Seleziona</td>
    <%
    } %>
    </tr>
    <%
    Iterator itx = wmagistrati.iterator();

    while ( itx.hasNext())
    {
      WMagistratoModel lMag = (WMagistratoModel)itx.next();
    %>
      <tr>
        <td class=l><%=StringUtils.toStringJSP(lMag.getCodMagistrato(),"-")%></td>
        <td class=l><%=StringUtils.toStringJSP(lMag.getCognome(),"-") + " " + StringUtils.toStringJSP(lMag.getNome(),"-")%></td>
        <td class=l><%=DateUtils.getDateToString(lMag.getDataNascita(),"dd-MM-yyyy")%> </td>

      <%
      if (! modalita.equals("NoPop"))
      { %>
        <td class=c><a href="Javascript:insertIT('<%=StringUtils.cStrForJS(lMag.getCodMagistrato())%>','<%=StringUtils.cStrForJS(lMag.getCognome())%>','<%=StringUtils.cStrForJS(lMag.getNome())%>');"> <img align="middle" src="/images/fileselected.gif" border=0></a></td>
        <% } %>
      </tr>
    <%
    }
    %>
    </table>
  </body>
</html>
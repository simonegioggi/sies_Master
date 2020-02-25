<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.siepe.attivita.model.AttivitaModel"%>
<%@ page import="siap.siepe.attivita.action.ICostantiAttivita"%>
<%@ page import="siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel"%>

<jsp:useBean id="FascicoloSiepeEsteso" scope="session" class="siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>


<html>
  <head>
    <title>[S.I.E.S.] - Elenco Attività collegate a Fascicolo SIEPE</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  </head>
  <body class="corpo">

  <table>
    <tr><td class="Titolo" > Elenco Attività </td></tr>
    <%
    Iterator itx = (FascicoloSiepeEsteso.getElencoAttivita()).iterator();

    while ( itx.hasNext() )
    {
      AttivitaModel lAttivita = (AttivitaModel)itx.next();
%>
    <tr>
      <td class="L" width=100% >
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siepe.attivita.action.ActLoadDettaglioAttivita&<%=ICostantiAttivita.CAMPO_ID_ATTIVITA%>=<%=lAttivita.getIdAttivita()%>&TornaQui=<%=TornaQui%>">
        <%=lAttivita.getDescrTipoAttivita()%>
        </a>
      </font>
     </td>
    </tr>
<% } %>
  </table>
</body>
</html>
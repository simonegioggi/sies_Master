<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.siepe.attivita.model.AttivitaModel"%>
<%@ page import="siap.siepe.attivita.action.ICostantiAttivita"%>
<%@ page import="siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel"%>
<%@ page import="siap.siepe.espertoattivita.model.EspertoAttivitaEspertoModel" %>
<%@ page import="siap.sius.esperto.action.ICostantiEsperto" %>

<jsp:useBean id="FascicoloSiepeEsteso" scope="session" class="siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="espertiAttivi" scope="request" class="java.util.Vector"/>
<jsp:useBean id="attivita" 	    scope="request" class="siap.siepe.attivita.model.AttivitaModel"/>


<html>
  <head>
    <title>[S.I.E.S.] - Elenco Esperti associati ad una Attività</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  </head>
  <body class="corpo">
<%
if (espertiAttivi.size() > 0)
{
%>
  <table>
    <tr><td class="Titolo" > Esperti </td></tr>
<%
  Iterator itx = espertiAttivi.iterator();
  while ( itx.hasNext())
  {
    EspertoAttivitaEspertoModel esperto = (EspertoAttivitaEspertoModel)itx.next();
%>
    <tr>
      <td class="L" width=100% >
      <font class="campo">
        <%=esperto.getNome() + " " + esperto.getCognome()%>
      </font>
     </td>
    </tr>
<% } %>
  </table>
<%
  }
%>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva" %>
<%@ page import="siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel" %>

<jsp:useBean id="dettaglioPenaComplessiva" scope="request" class="siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel" />

<% // Mai utilizzata (13_03_2009)   %>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Pena Complessiva</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font> <font class=campo>Elenco Posizioni Giuridiche</font></td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <div align=center>
  <table>
    <tr><td class="Titolo" colspan=4>Pena</td></tr>
    <tr>
      <td class="int">Posizione Giuridica</td>
      <td class="int">Data Decorrenza</td>
      <td class="int">Data Fine</td>
      <td class="int">Posizione Processuale</td>
      <td class="int" width=5%>Azioni</td>
    </tr>

  </table>
  </div>
  </body>
</html>
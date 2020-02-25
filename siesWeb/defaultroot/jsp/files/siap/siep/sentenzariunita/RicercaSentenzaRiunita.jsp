<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.siep.sentenzariunita.action.ICostantiSentenzaRiunita" %>
<%@ page import="siap.siep.sentenzariunita.model.SentenzaRiunitaModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="sentenzeriunite" scope="request" class="java.util.Vector" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Sentenza Riunita in Appello</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">
  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Elenco Sentenze Riunite in Appello</font></td>
      </tr>
    </table>

    <br>

    <table cellpadding="4" cellspacing="4">

    <tr>
      <td class="int">Data Sentenza</td>
      <td class="int">Anno/Numero Sentenza</td>
      <td class="int">Autorità</td>
      <td class="int">Luogo</td>
      <td class="int">Azioni</td>
    </tr>

<%
    Iterator itx = sentenzeriunite.iterator();
    while ( itx.hasNext())
    {
      SentenzaRiunitaModel lSen = (SentenzaRiunitaModel)itx.next();
%>
    <tr>
      <td class=C><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSen.getDataSentenza(),"dd-MM-yyyy"))%></td>
      <td class=C><%=StringUtils.toStringJSP(lSen.getAnnoSentenza())%>/<%=StringUtils.toStringJSP(lSen.getNumeroSentenza())%></td>
      <td class=C><%=StringUtils.toStringJSP(lSen.getDescrTipoAutoritaEmittente())%></td>
      <td class=C><%=StringUtils.toStringJSP(lSen.getDescrLuogoEmittente())%></td>
      <td class=C>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiSentenzaRiunita.CAMPO_ID_SENTENZA_RIUNITA%>"/>
           <jsp:param name="ValoreIdEntita" value="<%=lSen.getIdSentenzaRiunita()%>"/>
        </jsp:include>
      </td>
    </tr>
<%
    }
%>
    </table>
  </FORM>
  <br>

</body>
</html>
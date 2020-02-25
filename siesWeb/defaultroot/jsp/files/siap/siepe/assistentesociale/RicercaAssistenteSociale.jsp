<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siepe.assistentesociale.action.ICostantiAssistenteSociale" %>
<%@ page import="siap.siepe.assistentesociale.model.AssistenteSocialeModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<%@ page import="siap.sico.decodifiche.util.DecodificheUtils" %>

<jsp:useBean id="assistentisociali" scope="request" class="java.util.Vector"/>


<%@ page import="java.util.Collection"%>

<% 
Collection flags =(Collection) request.getAttribute("flags");
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Assistenti Sociali</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"> <font class=label>Funzione :</font>
      <font class=campo> Elenco Assistenti Sociali  </font> </td>
    </tr>
  </table>

  <br>

  <table>
    <div align=center>
      <tr>
        <td class="int" width=20%>Cognome</td>
        <td class="int" width=20%>Nome</td>
        <td class="int" width=15%>Cod. Fiscale</td>
        <td class="int" width=10%>Inizio Validità</td>
        <td class="int" width=10%>Fine Validità</td>
        <td class="int" width=10%>Disponibilità</td>
        <td class="int" width=5%>Azioni</td>
      </tr>
    </div>
<%
  Iterator itx = assistentisociali.iterator();
  while ( itx.hasNext())
  {
    AssistenteSocialeModel assistentesociale = (AssistenteSocialeModel)itx.next();
%>
    <tr>
      <td class=l><%=assistentesociale.getCognome()%></td>
      <td class=l><%=assistentesociale.getNome()%></td>
      <td class=l><%=StringUtils.toStringJSP(assistentesociale.getCodiceFiscale(),"-")%></td>
      <td class=l><%=StringUtils.toStringJSP(DateUtils.getDateToString(assistentesociale.getDataInizioValidita(),"dd-MM-yyyy"), "-")%> </td>
      <td class=l><%=StringUtils.toStringJSP(DateUtils.getDateToString(assistentesociale.getDataFineValidita(),"dd-MM-yyyy"), "-")%> </td>
      <td class=l><%=StringUtils.toStringJSP( assistentesociale.getDescrFlagStato())%></td>

     <td class=c>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiAssistenteSociale.CAMPO_ID_ASSISTENTE_SOCIALE%>" />
          <jsp:param name="ValoreIdEntita" value="<%=assistentesociale.getIdAssistenteSociale()%>" />
        </jsp:include>
      </td>
    </tr>
<%
  }
%>
    </table>
  </form>
  <br>
  </body>
</html>
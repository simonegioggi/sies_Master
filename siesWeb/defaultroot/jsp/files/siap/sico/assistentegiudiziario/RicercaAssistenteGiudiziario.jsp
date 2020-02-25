<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Collection" %>
<%@ page import="siap.sico.assistentegiudiziario.action.ICostantiAssistenteGiudiziario" %>
<%@ page import="siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<%@ page import="siap.sico.decodifiche.util.DecodificheUtils" %>

<jsp:useBean id="assistenti" scope="request" class="java.util.Vector"/>



<% 
Collection flags =(Collection) request.getAttribute("flags");

%>


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Assistenti Giudiziari</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"> <font class=label>Funzione :</font>
        <font class=campo> Elenco Assistenti Udienza  </font> </td>
      </tr>
    </table>
    <br>
    <table>
    <div align=center>
      <tr>
        <td class="int" width=20%>Cognome</td>
        <td class="int" width=20%>Nome</td>
        <td class="int" width=10%>Inizio Validità</td>
        <td class="int" width=10%>Fine Validità</td>
        <td class="int" width=10%>Disponibilità</td>
        <td class="int" width=10%>Azioni</td>
      </tr>
    </div>
<%
  Iterator itx = assistenti.iterator();
  while ( itx.hasNext())
  {
    AssistenteGiudiziarioModel lAssistenteGiudiziario = (AssistenteGiudiziarioModel)itx.next();
%>
    <tr>
      <td class=l><%=lAssistenteGiudiziario.getCognome()%></td>
      <td class=l><%=lAssistenteGiudiziario.getNome()%></td>

      <td class=l><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAssistenteGiudiziario.getDataInizioValidita(),"dd-MM-yyyy"))%> </td>
      <td class=l><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAssistenteGiudiziario.getDataFineValidita(),"dd-MM-yyyy"))%> </td>
      <td class=l><%=DecodificheUtils.getDescbyCode(flags,lAssistenteGiudiziario.getFlagStato())%></td>

     <td class=c>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiAssistenteGiudiziario.CAMPO_ID_ASSISTENTE_GIUDIZIARIO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lAssistenteGiudiziario.getIdAssistenteGiudiziario()%>" />
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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato" %>
<%@ page import="siap.sico.magistrato.model.MagistratoModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="magistrati" scope="request" class="java.util.Vector"/>

<%@ page import="java.util.Collection" %>

<% 
Collection flags =(Collection) request.getAttribute("flags");

%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Magistrati</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class=label>Funzione :</font> <font class=campo> Elenco Magistrati  </font> </td>
      </tr>
    </table>
    <br>
  <br>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<br>

  <table>
     <div align=center>
        <tr>
          <td class="int" width=10%>Cod. C.S.M.</td>
          <td class="int" width=30%>Cognome</td>
          <td class="int" width=30%>Nome</td>
          <td class="int" width=10%>Disponibilità</td>
          <td class="int" width=10%>Azioni</td>
        </tr>
      </div>
<%
  Iterator itx = magistrati.iterator();
  while ( itx.hasNext())
  {
    MagistratoModel magistrato = (MagistratoModel)itx.next();
%>
    <tr>
      <td class=c><%=magistrato.getCodMagistrato()%></td>
      <td class=l><%=magistrato.getCognome()%></td>
      <td class=l><%=magistrato.getNome()%></td>
      <td class=l><%=DecodificheUtils.getDescbyCode(flags,magistrato.getFlagStato())%></td>

      <td class=c>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" />
           <jsp:param name="ValoreIdEntita" value="<%=magistrato.getCodMagistrato()%>" />
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
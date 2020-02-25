<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel"%>

<%@ page import="siap.sius.cancelleriaassegnataria.action.ICostantiCancelleriaAssegnataria"%>


<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="elenco" scope="request" class="java.util.Vector"/>


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Cancellerie Assegnatarie</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class=label>Funzione :</font> <font class=campo> Ricerca Cancellazione Assegnataria</font> </td>
      </tr>
    </table>
    <br>
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

  <table>
     <div align=center>
        <tr>
          <td class="int" width=10%>Codice</td>
          <td class="int" width=30%>Descrizione</td>
          <td class="int" width=30%>Ufficio</td>
          <td class="int" width=10%>Azioni</td>
        </tr>
      </div>
<%
  Iterator itx = elenco.iterator();
  while ( itx.hasNext())
  {
    CancelleriaAssegnatariaModel lCancAss = (CancelleriaAssegnatariaModel) itx.next();
%>
    <tr>
      <td class=c><%=lCancAss.getCodCancelleriaAssegnataria()%></td>
      <td class=l><%=lCancAss.getDescCancelleriaAssegnataria()%></td>
      <td class=l><%=lCancAss.getDescrUfficio()%></td>

      <td class=c>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiCancelleriaAssegnataria.CAMPO_COD_CANCELLERIA_ASSEGNATARIA%>"/>
           <jsp:param name="ValoreIdEntita" value="<%=lCancAss.getCodCancelleriaAssegnataria()%>" />
           <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiCancelleriaAssegnataria.CAMPO_COD_UFFICIO%>"/>
           <jsp:param name="ValoreIdEntitaProvv" value="<%=lCancAss.getCodUfficio()%>" />
        </jsp:include>
      </td>
    </tr>
<%
  }
%>
    </table>
  <br>
  </body>
</html>
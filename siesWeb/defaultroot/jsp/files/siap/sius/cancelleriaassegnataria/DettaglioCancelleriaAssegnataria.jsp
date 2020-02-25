<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.siep.posizionemateriale.action.ICostantiPosizioneMateriale"%>
<%@ page import="siap.sius.cancelleriaassegnataria.action.ICostantiCancelleriaAssegnataria"%>
<%@ page import="siap.sico.utente.model.UtenteModel" %>

<jsp:useBean id="cancelleriaassegnataria"       scope="request" class="siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>


<%
  String lModificabile= "SI";
  // 29/02/2012 Correzione errore segnalato dal TDS di Roma - Utenti diversi non potevano gestire le cancellerie in ambito Ufficio.
  //if (cancelleriaassegnataria.getCodOperatoreInserimento().compareTo( UtenteConnesso.getUserId()) == 0)
  if (cancelleriaassegnataria.getCodUfficio().compareTo( UtenteConnesso.getUfficioUtente().getCodUfficio()) == 0)
      lModificabile= "SI";
  else
      lModificabile= "NO";
%>

<html>
<head>
<title>[S.I.A.P.] - Dettaglio Cancelleria Assegnataria </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>

<body class="corpo">
    <table>
      <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Cancelleria Assegnataria</font>
      </td>
      <td class="LBG">
          <jsp:include page="<%=ICostantiPosizioneMateriale.PG_TOOLBAR_HEADER%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiCancelleriaAssegnataria.CAMPO_COD_CANCELLERIA_ASSEGNATARIA%>"/>
           <jsp:param name="ValoreIdEntita" value="<%=cancelleriaassegnataria.getCodCancelleriaAssegnataria()%>" />
           <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiCancelleriaAssegnataria.CAMPO_COD_UFFICIO%>"/>
           <jsp:param name="ValoreIdEntitaProvv" value="<%=cancelleriaassegnataria.getCodUfficio()%>" />
           <jsp:param name="Modificabile" value="<%=lModificabile%>" />
       </jsp:include>
     </td>
  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
   </tr>
 </table>
<BR>
<table cellspacing=4 cellpadding=4>
  <tr>
      <td class="int">Codice Cancelleria Assegnataria</td>
    <td class="l"><font class="campo"><%=cancelleriaassegnataria.getCodCancelleriaAssegnataria() %></font></td>
  </tr>
  <tr>
  <td class="int">Descrizione Cancelleria Assegnataria</td>
    <td class="l"><font class="campo"><%=cancelleriaassegnataria.getDescCancelleriaAssegnataria() %></font></td>
  </tr>
  <tr>
    <td class="int">Ufficio</td>
    <td class="l"><font class="campo"><%=cancelleriaassegnataria.getDescrUfficio()%></font></td>
  </tr>

  </table>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.stampadocumenti.model.StampaDocumentiModel"%>
<%@ page import="siap.siep.stampadocumenti.action.ICostantiStampaDocumenti"%>

<jsp:useBean id="stampadocumenti" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_ricerca" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> Ricerca Stampa Documenti </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="/html/gestisciUploadStampa2.js"></script>
</head>

<body class="corpo">
<FORM method="POST" action="Main.jsp" name="RicercaStampaDocumenti">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco Richieste Stampe Inizio Esecuzione</font>
      </td>
    </tr>
  </table>

  <% //=============================================== 
     // Include della jsp che gestisce la paginazione 
     //=============================================== %>
  <%
  if (tipo_ricerca.equals("paginata")) { %>
    <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <%}
  %>
<br>
<div>
  <table align="center">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int">Id Stampa</td>
      <td class="int">Data</td>
      <td class="int">Stato</td>
      <td class="int">Descrizione</td>
      <td class="int">Azioni</td>
    </tr>
    <%
      Iterator itx = stampadocumenti.iterator();
      while ( itx.hasNext()) {
        StampaDocumentiModel lStampaDocumenti = (StampaDocumentiModel)itx.next();
        
        String lColore = "c";
        String lDicitura = "Stampa in elaborazione...";
        if(lStampaDocumenti.getStato().equals("1"))
        {
        	lColore = "cVerde";
        	lDicitura = "Stampa Terminata";
        }
        if(lStampaDocumenti.getStato().equals("2"))
        {
        	lColore = "cRosso";
        	lDicitura = "Stampa terminata con errore, si prega di riprodurre nuovamente la stampa.";
        }
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class=<%=lColore%>>&nbsp;<%=StringUtils.toStringJSP(lStampaDocumenti.getIdStampa(),"&nbsp;")%></td>
      <td class=<%=lColore%>>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lStampaDocumenti.getData(),"dd-MM-yyyy HH:mm:ss"),"&nbsp;")%></td>
      <td class=<%=lColore%>><%=lDicitura%></td>
      <td class=<%=lColore%>>&nbsp;<%=StringUtils.toStringJSP(lStampaDocumenti.getDescrizione(),"&nbsp;")%></td>
      <td class=c>
      <%if(lStampaDocumenti.getStato().equals("1")){ %>
           <a href="Javascript:stampa2( '/jsp/files/Stampa.jsp', 'Action=siap.siep.stampadocumenti.action.ActLoadDocumento&IdStampa=<%=lStampaDocumenti.getIdStampa() %>');">
                  <img src="/images/print.gif" alt="Stampa" width="12" height="12" border="0"></a>
           <a href="Javascript:conferma('siap.siep.stampadocumenti.action.ActCancellaStampaDocumenti','IdStampa','<%=lStampaDocumenti.getIdStampa() %>');">
                  <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0"></a>
          <%} %>
          <%if(lStampaDocumenti.getStato().equals("2")){ %>
                <a href="Javascript:conferma('siap.siep.stampadocumenti.action.ActCancellaStampaDocumenti','IdStampa','<%=lStampaDocumenti.getIdStampa() %>');">
                  <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                </a>
          <%} %>
     
      </td>
    </tr>
    <% } // end while su iterator %>
  </table>
</div>
</FORM>
</body>
</html>
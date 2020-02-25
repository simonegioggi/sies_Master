<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.scambiosanzione.model.ScambioSanzioneModel"%>
<%@ page import="siap.siep.scambiosanzione.action.ICostantiScambioSanzione"%>

<jsp:useBean id="scambiosanzione" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_ricerca" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> Ricerca ScambioSanzione </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
</head>

<body class="corpo">
<FORM method="POST" action="Main.jsp" name="RicercaScambioSanzione">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco ScambioSanzione</font>
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

<div>
  <table align="center">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int">Id Scambio Sanzione</td>
      <td class="int">Cod Tipo Decisione</td>
      <td class="int">Cod Natura Sanzione</td>
      <td class="int">Cod Tipo Sanzione</td>
      <td class="int">Data Inizio</td>
      <td class="int">Data Fine</td>
      <td class="int">Note</td>
      <td class="int">Anno Registro</td>
      <td class="int">Numero Registro</td>
      <td class="int">Chiave Anno Fascicolo Sius</td>
      <td class="int">Chiave Progr Fascicolo Sius</td>
      <td class="int">Cod Ufficio Sorveglianza</td>
      <td class="int">Cod Ufficio Emittente</td>
      <td class="int">Cod Operatore Inserimento</td>
      <td class="int">Data Inserimento</td>
      <td class="int">Cod Ufficio Inserimento</td>
      <td class="int">Cod Operatore Aggiornamento</td>
      <td class="int">Data Aggiornamento</td>
      <td class="int">Cod Ufficio Aggiornamento</td>
      <td class="int">Data Emissione</td>
      <td class="int">Eve Id Evento</td>
      <td class="int">Azioni</td>
    </tr>
    <%
      Iterator itx = scambiosanzione.iterator();
      while ( itx.hasNext()) {
        ScambioSanzioneModel lScambioSanzione = (ScambioSanzioneModel)itx.next();
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScambioSanzione.getIdScambioSanzione(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScambioSanzione.getCodTipoDecisione(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScambioSanzione.getCodNaturaSanzione(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScambioSanzione.getCodTipoSanzione(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataInizio(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataFine(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScambioSanzione.getNote(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScambioSanzione.getAnnoRegistro(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScambioSanzione.getNumeroRegistro(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScambioSanzione.getChiaveAnnoFascicoloSius(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScambioSanzione.getChiaveProgrFascicoloSius(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScambioSanzione.getCodUfficioSorveglianza(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScambioSanzione.getCodUfficioEmittente(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScambioSanzione.getCodOperatoreInserimento(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataInserimento(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScambioSanzione.getCodUfficioInserimento(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScambioSanzione.getCodOperatoreAggiornamento(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataAggiornamento(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScambioSanzione.getCodUfficioAggiornamento(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataEmissione(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScambioSanzione.getEveIdEvento(),"&nbsp;")%></td>
      <td class=c>
      <%
       String modificabile = "SI";
       // inserire qui i criteri in base ai quali il campo è modificabile 

      %>
      <table>
       <tr>
 
                 <td>
               
                   <a href="Main.jsp?Action=siap.siep.scambiosanzione.action.ActLoadDettaglioScambioSanzione&IdScambioSanzione=<%=lScambioSanzione.getIdScambioSanzione()%>&TornaQui=20">
                     <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                   </a>
 
               
                 </td>
 
                 <td>
                   <a href="Main.jsp?Action=siap.siep.scambiosanzione.action.ActLoadModificaScambioSanzione&IdScambioSanzione=<%=lScambioSanzione.getIdScambioSanzione()%>&TornaQui=20">
                     <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                   </a>
                 </td>
 
                 <td>
                   <a href="Javascript:conferma('siap.siep.scambiosanzione.action.ActLoadCancellaScambioSanzione','IdScambioSanzione','<%=lScambioSanzione.getIdScambioSanzione()%>');">
                     <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                   </a>
                 </td>
 
       </tr>
     </table>
       
      </td>
    </tr>
    <% } // end while su iterator %>
  </table>
</div>
</FORM>
</body>
</html>
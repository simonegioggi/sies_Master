<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.bdmc.notifichesies.model.NotificheSiesModel"%>
<%@ page import="siap.bdmc.notifichesies.action.ICostantiNotificheSies"%>

<jsp:useBean id="notifichesies" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_ricerca" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> Ricerca NotificheSies </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
</head>

<body class="corpo">
<FORM method="POST" action="Main.jsp" name="RicercaNotificheSies">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco NotificheSies</font>
      </td>
    </tr>
  </table>

  <% //=============================================== 
     // Include della jsp che gestisce la paginazione 
     //=============================================== %>
  <%
  if (tipo_ricerca.equals("paginata")) { %>
    <jsp:include page="<%=ISIAPCostantiWeb.PAGINAZIONE_RICERCA%>"></jsp:include>
  <%}
  %>

<div>
  <table align="center">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int">Id Notifiche Sies</td>
      <td class="int">Anno Siep</td>
      <td class="int">Prog Siep</td>
      <td class="int">Ufficio Siep</td>
      <td class="int">Anno Fasc Bdmc</td>
      <td class="int">Ufficio Fasc Bdmc</td>
      <td class="int">Numero Fasc Bdmc</td>
      <td class="int">Tipo Notifica</td>
      <td class="int">Data Notifica</td>
      <td class="int">Stato Trasmissione</td>
      <td class="int">Data Trasmissione</td>
      <td class="int">Id Pren</td>
      <td class="int">Prog Peri Pres</td>
      <td class="int">Cod Operatore Inserimento</td>
      <td class="int">Data Inserimento</td>
      <td class="int">Cod Ufficio Inserimento</td>
      <td class="int">Azioni</td>
    </tr>
    <%
      Iterator itx = notifichesies.iterator();
      while ( itx.hasNext()) {
        NotificheSiesModel lNotificheSies = (NotificheSiesModel)itx.next();
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lNotificheSies.getIdNotificheSies(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lNotificheSies.getAnnoSiep(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lNotificheSies.getProgSiep(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lNotificheSies.getUfficioSiep(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lNotificheSies.getAnnoFascBdmc(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lNotificheSies.getUfficioFascBdmc(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lNotificheSies.getNumeroFascBdmc(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lNotificheSies.getTipoNotifica(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificheSies.getDataNotifica(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lNotificheSies.getStatoTrasmissione(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificheSies.getDataTrasmissione(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lNotificheSies.getIdPren(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lNotificheSies.getProgPeriPres(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lNotificheSies.getCodOperatoreInserimento(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificheSies.getDataInserimento(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lNotificheSies.getCodUfficioInserimento(),"&nbsp;")%></td>
      <td class=c>
      <%
       String modificabile = "SI";
       // inserire qui i criteri in base ai quali il campo è modificabile 

      %>
      <table>
       <tr>
 
                 <td>
               
                   <a href="Main.jsp?Action=siap.bdmc.notifichesies.action.ActLoadDettaglioNotificheSies&IdNotificheSies=<%=lNotificheSies.getIdNotificheSies()%>&TornaQui=20">
                     <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                   </a>
 
               
                 </td>
 
                 <td>
                   <a href="Main.jsp?Action=siap.bdmc.notifichesies.action.ActLoadModificaNotificheSies&IdNotificheSies=<%=lNotificheSies.getIdNotificheSies()%>&TornaQui=20">
                     <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                   </a>
                 </td>
 
                 <td>
                   <a href="Javascript:conferma('siap.bdmc.notifichesies.action.ActLoadCancellaNotificheSies','IdNotificheSies','<%=lNotificheSies.getIdNotificheSies()%>');">
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
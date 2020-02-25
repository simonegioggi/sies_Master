<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants" %>

<%@ page import="siap.bdmc.sbviewnotifiche.model.SbViewNotificheModel"%>
<%@ page import="siap.bdmc.sbviewnotifiche.action.ICostantiSbViewNotifiche"%>

<jsp:useBean id="sbviewnotifiche" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_ricerca" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> Ricerca SbViewNotifiche </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
</head>

<body class="corpo">
<FORM method="POST" action="Main.jsp" name="RicercaSbViewNotifiche">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco Notifiche</font>
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
  <table width="100%" align="center">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td width="12%" class="int">Codice notifica</td>
      <td width="36%" class="int">Descrizione codice notifica</td>
      <td width="15%" class="int">Data registrazione notifica</td>
      <td width="15%" class="int">Data validità notifica</td>
      <td width="9%" class="int">Attivazione</td>
      <td width="9%" class="int">Tipo modifica</td>
      <td width="4%" class="int">Det</td>
    </tr>
    <%
      Iterator itx = sbviewnotifiche.iterator();
      while ( itx.hasNext()) {
        SbViewNotificheModel lSbViewNotifiche = (SbViewNotificheModel)itx.next();
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewNotifiche.getCodiNoti(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewNotifiche.getDescrizione(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataRegiNoti(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataValiNoti(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <%if (lSbViewNotifiche.getFlagTras() != null)  {%>
      <td class=c>&nbsp;<%=lSbViewNotifiche.getFlagTras()%></td>
      <% } else  { %>
       <td class=c>&nbsp;-</td>
     
      <% } %>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewNotifiche.getFlagModi(),"&nbsp;")%></td>
      <td class=c>
      <%
       String modificabile = "SI";
       // inserire qui i criteri in base ai quali il campo è modificabile 

      %>
      <table>
       <tr>

                 <td>
               
                   <a href="Main.jsp?Action=siap.bdmc.sbviewnotifiche.action.ActLoadDettaglioSbViewNotifiche&ProgNoti=<%=lSbViewNotifiche.getProgNoti()%>&TornaQui=20">
                     <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                   </a>
 
               
                 </td>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--  
                 <td>
                   <a href="Main.jsp?Action=siap.bdmc.sbviewnotifiche.action.ActLoadModificaSbViewNotifiche&ProgNoti=<\\%=lSbViewNotifiche.getProgNoti()%>&TornaQui=20">
                     <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                   </a>
                 </td>
 
                 <td>
                   <a href="Javascript:conferma('siap.bdmc.sbviewnotifiche.action.ActLoadCancellaSbViewNotifiche','ProgNoti','<\\%=lSbViewNotifiche.getProgNoti()%>');">
                     <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                   </a>
                 </td>
--%>
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
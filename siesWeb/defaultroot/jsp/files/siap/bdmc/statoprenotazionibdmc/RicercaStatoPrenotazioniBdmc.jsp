<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants" %>

<%@ page import="siap.bdmc.statoprenotazionibdmc.model.StatoPrenotazioniBdmcModel"%>
<%@ page import="siap.bdmc.statoprenotazionibdmc.action.ICostantiStatoPrenotazioniBdmc"%>

<jsp:useBean id="statoprenotazionibdmc" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_ricerca" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> Stato Trasmissioni verso Bdmc  </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
</head>

<body class="corpo">
<FORM method="POST" action="Main.jsp" name="RicercaStatoPrenotazioniBdmc">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Stato Trasmissioni verso Bdmc </font>
      </td>
    </tr>
  </table>

  <% //=============================================== 
     // Include della jsp che gestisce la paginazione 
     //=============================================== %>
  <%
  if (tipo_ricerca.equals("paginata")) {
%>
    <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <%}
  %>

<div>
  <table align="center">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int">Codice Trasmissione</td>
     <!-- <td class="int">Id Misura Cautelare Bdmc</td>  --> 
      
      <td class="int">Data Trasmissione</td>
       <td class="int">Esito</td>
      <td class="int">Cod. Errore</td>
      <td class="int">Messaggio</td>
      <td class="int">Numero Prenotazione</td>
      <td class="int">Prog. Periodo</td>
      <td class="int">Tipo </td>
      <td class="int">Azioni</td>
    </tr>
    <%
      Iterator itx = statoprenotazionibdmc.iterator();
      while ( itx.hasNext()) {
        StatoPrenotazioniBdmcModel lStatoPrenotazioniBdmc = (StatoPrenotazioniBdmcModel)itx.next();
    %>
    <tr>
      <%-- Inserire qui le get dei campi da visualizzare --%>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lStatoPrenotazioniBdmc.getStatoPrenotazioniBdmc(),"&nbsp;")%></td>
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    	<%--  <td class=c>&nbsp;< %=StringUtils.toStringJSP(lStatoPrenotazioniBdmc.getIdMisuraCautelareBdmc(),"&nbsp;")%></td> --%> 
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lStatoPrenotazioniBdmc.getDataTrasmissione(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <%if (lStatoPrenotazioniBdmc.getEsitoId().compareTo(new BigDecimal(0)) !=0) {  %>
      	<td class=c>&nbsp;Negativo</td>
      	<td class=c>&nbsp;<%=StringUtils.toStringJSP(lStatoPrenotazioniBdmc.getEsitoId(),"&nbsp;")%></td>
      <% }else { %>
        <td class=c>&nbsp;Positivo</td>
      	<td class=c>&nbsp;-</td>
       <%}  %>
	 <td class=c>&nbsp;<%=StringUtils.toStringJSP(lStatoPrenotazioniBdmc.getEsitoMsg(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lStatoPrenotazioniBdmc.getIdPrenotazione(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lStatoPrenotazioniBdmc.getProgPeriPres(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lStatoPrenotazioniBdmc.getTipoTrasmissione(),"&nbsp;")%></td>
      <td class=c>
     
      <table>
       <tr>
 
                 <td>
                 
                 <% if (lStatoPrenotazioniBdmc.getTipoTrasmissione().compareTo("A") == 0) { %>
                   <a href="Main.jsp?Action=siap.bdmc.statoprenotazionibdmc.action.ActLoadDettaglioAssociazioneBdmc&IdFascicoloBdmc=<%=lStatoPrenotazioniBdmc.getIdMisuraCautelareBdmc() %>&TornaQui=20">
                     <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                   </a>
                    <% }else { %>
	                   <a href="Main.jsp?Action=siap.bdmc.statoprenotazionibdmc.action.ActLoadDettaglioPeriodiBdmc&IdMisuraCautelare=<%=lStatoPrenotazioniBdmc.getIdMisuraCautelareBdmc() %>&TornaQui=20">
	                     <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
	                   </a>
                    <%}  %>
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
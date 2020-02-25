<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="magistratoassegnatario" scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="magistratoprecedente" scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="magistrato" scope="request" class="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel"/>
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Magistrato Assegnatario </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>

<body class="corpo">
  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Assegnazione/Cambio Magistrato</font>
          <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>" />
         </td>
        </td>

   </tr>
 </table>
<br>
    <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
<br>


</FORM>
 <FORM name="DettaglioMagistratoAss">


   <table cellspacing=2 cellpadding=2>
    <tr>
       <td class="Titolo" colspan=6> Magistrato Assegnatario </td>
     </tr>

     <tr>

     <td class="l">Cognome :
        <font class="campo"><%=magistratoassegnatario.getCognome() %></font></td>
        <td class="l">Nome :
        <font class="campo"><%=magistratoassegnatario.getNome() %></font></td>
     </tr>

</table>
<br>


<table cellspacing=2 cellpadding=2>
<%
     if(magistratoprecedente.getCodMagistrato() !=null && !magistratoprecedente.getCodMagistrato().equals(""))
     {
%>
       <tr>
        <td class="l">
         Competenza Trasferita dal Magistrato  <font class="campo"><%=magistratoprecedente.getNome()%>
         &nbsp;<%=magistratoprecedente.getCognome()%> </font>
         al Magistrato <font class="campo"><%=magistratoassegnatario.getNome()%>
         &nbsp;<%=magistratoassegnatario.getCognome()%> </font>
         il <%=DateUtils.getDateToString(magistrato.getDataInizio(),"dd/MM/yyyy")%>
        </td>
       </tr>
   <%}%>

</table>
</FORM>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza"%>
<%@ page import="siap.siep.reato.action.ICostantiReato"%>
<%@ page import="siap.sige.sentenza.model.SentenzaSigeModel"%>

<jsp:useBean id="reati" scope="request" class="java.util.Vector" />
<jsp:useBean id="sentenza" scope="request" class="siap.sige.sentenza.model.SentenzaSigeModel" />
<jsp:useBean id="Modificabile"     scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.A.P.] - Dettaglio Altro Titolo Esecutivo </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

</head>
 
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Titolo assegnato a Procedimento SIGE</font>
 	 </td>     
       <td class="LBG">
           <jsp:include page="<%=ICostantiFasSigeSentenza.PG_BUTTONS_MOD_CANC%>">     
           <jsp:param name="CampoIdEntita" value="<%=ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA%>" />
          <jsp:param name="ValoreIdEntita" value="<%=sentenza.getIdFasSigeSentenza()%>" />
       </jsp:include>
      <jsp:include page="<%=IWebConstants.PG_TOOLBAR_COMBO%>">
      <jsp:param name="Modificabile" value="<%=Modificabile%>"/>
      </jsp:include>
     </td>
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
   </tr>
 </table>
</FORM>
 <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>

 <jsp:include page="<%=ICostantiFasSigeSentenza.PG_INCLUDE_SENTENZA%>"/>
 <br>
  <table>
<!-- Modifica del 24/11/2016 MEV_15_S4 
	 Nel caso di Cumulo/Ordinanza/Decreto archiviazione la Data Irrevocabilità non viene visualizzata (Richiesta di Michele)
 -->
<% if (sentenza != null && sentenza.getCodTipoProvvedimento() != null && (sentenza.getCodTipoProvvedimento().equals("13")
		|| sentenza.getCodTipoProvvedimento().equals("03") || sentenza.getCodTipoProvvedimento().equals("63"))
	){ %>

<% } else { %>
    <tr>
      <td>
        <font class="label">Data di irrevocabilità: </font>&nbsp;
        <font class="campo">
          <%=sentenza.getDataIrrevocabilita()!=null
           ? DateUtils.getDateToString(sentenza.getDataIrrevocabilita(), "dd-MM-yyyy") : "-"%>
 				</font>
 	  </td>
 	</tr>
<% } %>
 <%if(sentenza.getFlagCompetenza() != null && sentenza.getFlagCompetenza().equalsIgnoreCase("S")) { %>
     <tr>
       <td>
          <font class="campo"> Titolo esecutivo di Competenza</font>
       </td>
    </tr>
 <%} %>
 
 <%if (reati != null && reati.size() > 0 ) { %>
<BR>
<jsp:include page="<%=ICostantiReato.PG_RICERCAREATO%>"/>
<%} %>
 </body>
</html>
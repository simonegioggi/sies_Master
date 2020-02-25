<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.bdmc.sbperipren.model.SbPeriprenModel"%>
<%@ page import="siap.bdmc.sbperipren.action.ICostantiSbPeripren"%>

<jsp:useBean id="sbperipren" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_ricerca" scope="request" class="java.lang.String"/>

<% // n.b. JSP NON UTILIZZATA (13/03/2009) %>


<html>
<head>
  <title> Ricerca SbPeripren </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
</head>

<body class="corpo">
<FORM method="POST" action="Main.jsp" name="RicercaSbPeripren">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco SbPeripren</font>
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
      <td class="int">Data Iniz Peri</td>
      <td class="int">Data Fine Peri</td>
      <td class="int">Prog Peri Pres</td>
      <td class="int">Id Pren</td>
      <td class="int">Cod Uffi Sies</td>
      <td class="int">Anno Fasc Siep</td>
      <td class="int">Nume Fasc Siep</td>
      <td class="int">Codi Sede Inst</td>
      <td class="int">Anno Fasc Bdmc</td>
      <td class="int">Nume Fasc Bdmc</td>
      <td class="int">Cod Stat Pren Peri</td>
      <td class="int">Cod Tipo Peri</td>
      <td class="int">Data Pren Peri</td>
      <td class="int">Azioni</td>
    </tr>
    <%
      Iterator itx = sbperipren.iterator();
      while ( itx.hasNext()) {
        SbPeriprenModel lSbPeripren = (SbPeriprenModel)itx.next();
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPeripren.getDataInizPeri(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPeripren.getDataFinePeri(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbPeripren.getProgPeriPres(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbPeripren.getIdPren(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbPeripren.getCodiUffiSies(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbPeripren.getAnnoFascSiep(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbPeripren.getNumeFascSiep(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbPeripren.getCodiSedeInst(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbPeripren.getAnnoFascBdmc(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbPeripren.getNumeFascBdmc(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbPeripren.getCodStatPrenPeri(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPeripren.getDataPrenPeri(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>
      <%
       String modificabile = "SI";
       // inserire qui i criteri in base ai quali il campo è modificabile 

      %>
      <table>
       <tr>
 
                 <td>
               
                   <a href="Main.jsp?Action=siap.bdmc.sbperipren.action.ActLoadDettaglioSbPeripren&IdSbPeripren=<%=lSbPeripren.getProgPeriPres()%>&TornaQui=20">
                     <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                   </a>
 
               
                 </td>
 
                 <td>
                   <a href="Main.jsp?Action=siap.bdmc.sbperipren.action.ActLoadModificaSbPeripren&IdSbPeripren=<%=lSbPeripren.getProgPeriPres()%>&TornaQui=20">
                     <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                   </a>
                 </td>
 
                 <td>
                   <a href="Javascript:conferma('siap.bdmc.sbperipren.action.ActLoadCancellaSbPeripren','IdSbPeripren','<%=lSbPeripren.getProgPeriPres()%>');">
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
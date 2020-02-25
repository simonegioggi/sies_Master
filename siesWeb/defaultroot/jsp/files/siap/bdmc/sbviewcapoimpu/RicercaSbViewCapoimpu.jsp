<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel"%>
<%@ page import="siap.bdmc.sbviewcapoimpu.action.ICostantiSbViewCapoimpu"%>

<jsp:useBean id="sbviewcapoimpu" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_ricerca" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> Ricerca SbViewCapoimpu </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
</head>

<body class="corpo">
<FORM method="POST" action="Main.jsp" name="RicercaSbViewCapoimpu">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco SbViewCapoimpu</font>
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
      <td class="int">Flag Arti 0056</td>
      <td class="int">Flag Arti 0061</td>
      <td class="int">Arti 0061 Comm</td>
      <td class="int">Flag Arti 0081</td>
      <td class="int">Arti 0081 Comm</td>
      <td class="int">Flag Art 0110</td>
      <td class="int">Flag Arti 0112</td>
      <td class="int">Arti 0112 Commi</td>
      <td class="int">Flag Arti 0113</td>
      <td class="int">Flag Arti 0114</td>
      <td class="int">Flag Arti 0116</td>
      <td class="int">Flag Arti 0117</td>
      <td class="int">Luog Reat</td>
      <td class="int">Flag Peri Temp</td>
      <td class="int">Data Reat 0101</td>
      <td class="int">Data Reat 0202</td>
      <td class="int">Desc Peri Temp</td>
      <td class="int">Nume Prog Capo Impu</td>
      <td class="int">Id Pren</td>
      <td class="int">Anno Fasc Bdmc</td>
      <td class="int">Nume Fasc Bdmc</td>
      <td class="int">Codi Sede Inst</td>
      <td class="int">Azioni</td>
    </tr>
    <%
      Iterator itx = sbviewcapoimpu.iterator();
      while ( itx.hasNext()) {
        SbViewCapoimpuModel lSbViewCapoimpu = (SbViewCapoimpuModel)itx.next();
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArti0056(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArti0061(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getArti0061Comm(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArti0081(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getArti0081Comm(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArt0110(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArti0112(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getArti0112Commi(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArti0113(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArti0114(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArti0116(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArti0117(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getLuogReat(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagPeriTemp(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewCapoimpu.getDataReat0101(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewCapoimpu.getDataReat0202(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getDescPeriTemp(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getNumeProgCapoImpu(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getIdPren(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getAnnoFascBdmc(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getNumeFascBdmc(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewCapoimpu.getCodiSedeInst(),"&nbsp;")%></td>
      <td class=c>
      <%
       String modificabile = "SI";
       // inserire qui i criteri in base ai quali il campo è modificabile 

      %>
      <table>
       <tr>
 
                 <td>
               
                   <a href="Main.jsp?Action=siap.bdmc.sbviewcapoimpu.action.ActLoadDettaglioSbViewCapoimpu&IdSbViewCapoimpu=<%=lSbViewCapoimpu.getIdPren()%>&TornaQui=20">
                     <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                   </a>
 
               
                 </td>
 
                 <td>
                   <a href="Main.jsp?Action=siap.bdmc.sbviewcapoimpu.action.ActLoadModificaSbViewCapoimpu&IdSbViewCapoimpu=<%=lSbViewCapoimpu.getIdPren()%>&TornaQui=20">
                     <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                   </a>
                 </td>
 
                 <td>
                   <a href="Javascript:conferma('siap.bdmc.sbviewcapoimpu.action.ActLoadCancellaSbViewCapoimpu','IdSbViewCapoimpu','<%=lSbViewCapoimpu.getIdPren()%>');">
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
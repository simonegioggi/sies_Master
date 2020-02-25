<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.bdmc.sbpren.model.SbPrenModel"%>
<%@ page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>
<%@ page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>

<jsp:useBean id="sbpren" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_ricerca" scope="request" class="java.lang.String"/>


<html>
<head>
  <title> Ricerca Prenotazione </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
</head>

<body class="corpo">
<FORM method="POST" action="Main.jsp" name="RicercaSbPren">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco Prenotazioni</font>
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
  <table width="100%" align="center">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td width="9%" class="int">Data Prenotazione</td>
      <td width="5%" class="int">Num. Prenotazione</td>
      <td width="9%"class="int">Num./Anno Fasc. Bdmc</td>
      <td width="36%"class="int">Autorità</td>        
      <td width="13%" class="int">Soggetto</td>
      <td width="15%" class="int">Data Nascita</td>
      <td width="10%" class="int">Luogo Nascita</td>
      <td width="3%" class="int">Det</td>
    </tr>
    <%
      Iterator itx = sbpren.iterator();
      while ( itx.hasNext()) {
        SbPrenModel lSbPren = (SbPrenModel)itx.next();
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td width="9%" class=c><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPren.getDataPren(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td width="9%" class=c><%=StringUtils.toStringJSP(lSbPren.getIdPren(),"&nbsp;")%></td>
      <td width="9%" class=c><%=StringUtils.toStringJSP(lSbPren.getNumeFascBdmc(),"&nbsp;")%>/<%=StringUtils.toStringJSP(lSbPren.getAnnoFascBdmc(),"&nbsp;")%></td>
      <td width="36%" class=c><%=StringUtils.toStringJSP(lSbPren.getDescriSedeInst(),"&nbsp;")%></td>
      <td width="13%" class=c><%=StringUtils.toStringJSP(lSbPren.getCognSogg(),"&nbsp;")%>&nbsp;<%=StringUtils.toStringJSP(lSbPren.getNomeSogg(),"&nbsp;")%></td>
      <td width="15%" class=c><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPren.getDataNasc(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td width="10%" class=c><%=StringUtils.toStringJSP(lSbPren.getLuogNasc(),"&nbsp;")%></td>
      <td width="3%"class=c>
      <%
       String modificabile = "SI";
       // inserire qui i criteri in base ai quali il campo è modificabile 

      %>
      <table>
       <tr>
 
                 <td>
               
                   <a href="Main.jsp?Action=siap.bdmc.sbpren.action.ActLoadDettaglioSbPren&IdPren=<%=lSbPren.getIdPren() %>&TornaQui=20">
                     <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
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
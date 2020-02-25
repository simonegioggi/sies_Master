<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.ufficio.controller.UfficioUtils"%>
<%@ page import="siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel"%>
<%@ page import="siap.bdmc.fascicolosiepbdmc.action.ICostantiFascicoloSiepBdmc"%>

<jsp:useBean id="fascicolosiepbdmc" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_ricerca" scope="request" class="java.lang.String"/>
<jsp:useBean id="descr_ufficio_siep" scope="request" class="java.lang.String"/>


<html>
<head>
  <title> Ricerca Associazione Fascicolo Siep-Bdmc </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
</head>

<body class="corpo">
<FORM method="POST" action="Main.jsp" name="RicercaFascicoloSiepBdmc">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco Associazione Fascicolo Siep-Bdmc</font>
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
  <table align="center" width="100%">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td colspan="3"class="int">Estremi Bdmc</td>
      <td colspan="3"class="int">Estremi Siep</td>
      <td class="int"></td>
      <td class="int"></td>
    </tr>
	<tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int">Anno</td>
      <td class="int">Ufficio</td>
      <td class="int">Numero</td>
      <td class="int">Anno</td>
      <td class="int">Ufficio</td>
      <td class="int">Numero</td>
      <td class="int">Trasmissione</td>
      <td class="int">Azioni</td>
    </tr>
    <%
      Iterator itx = fascicolosiepbdmc.iterator();
      while ( itx.hasNext()) {
        FascicoloSiepBdmcModel lFascicoloSiepBdmc = (FascicoloSiepBdmcModel)itx.next();
    %>
    <tr>
      <%-- Inserire qui le get dei campi da visualizzare --%>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lFascicoloSiepBdmc.getChiaveAnnoBdmc(),"&nbsp;")%></td>
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lFascicoloSiepBdmc.getChiaveUfficioBdmc(),"&nbsp;")%></td> --%>
  	  <td class=c>&nbsp;<%=UfficioUtils.getDescTipoUffByCodUfficio(lFascicoloSiepBdmc.getChiaveUfficioBdmc())%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lFascicoloSiepBdmc.getChiaveProgrBdmc(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lFascicoloSiepBdmc.getChiaveAnnoSiep(),"&nbsp;")%></td>
<%--  	<td class=c>&nbsp;<%=StringUtils.toStringJSP(lFascicoloSiepBdmc.getChiaveUfficioSiep(),"&nbsp;")%></td>  --%>
      <td class=c>&nbsp;<%=descr_ufficio_siep%>&nbsp;</td>

      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lFascicoloSiepBdmc.getChiaveProgrSiep(),"&nbsp;")%></td>
      
      <% String flag = StringUtils.toStringJSP(lFascicoloSiepBdmc.getFlagTrasmissione()); %>
      <% if (flag.equalsIgnoreCase("S")){%>
      <td class=c>&nbsp;SI&nbsp;</td>
      <%}%>
      <%if(flag.equalsIgnoreCase("N")){%>
      <td class=c>&nbsp;NO&nbsp;</td>
      <% }%>
      <%if(flag.equalsIgnoreCase("")){%>
      <td class=c>&nbsp;</td>
      <% }%>
      <td class=c>
      <%
       String modificabile = "SI";
       // inserire qui i criteri in base ai quali il campo è modificabile 
      %>
      <table>
       <tr>
          <td>
            <a href="Main.jsp?Action=siap.bdmc.fascicolosiepbdmc.action.ActLoadModificaFascicoloSiepBdmc&IdFascicoloBdmc=<%=lFascicoloSiepBdmc.getIdFascicoloBdmc()%>&TornaQui=20">
              <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
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
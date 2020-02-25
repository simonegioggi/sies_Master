<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel"%>
<%@ page import="siap.siep.tipoeventibdmc.action.ICostantiTipoEventiBdmc"%>

<jsp:useBean id="tipoeventibdmc" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_ricerca" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> Ricerca TipoEventiBdmc </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
</head>

<body class="corpo">
<FORM method="POST" action="Main.jsp" name="RicercaTipoEventiBdmc">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco TipoEventiBdmc</font>
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
      <td class="int">Id Tipo Eventi Bdmc</td>
      <td class="int">Cod Tipo Evento</td>
      <td class="int">Cod Provvedimento</td>
      <td class="int">Cod Motivo</td>
      <td class="int">Azioni</td>
    </tr>
    <%
      Iterator itx = tipoeventibdmc.iterator();
      while ( itx.hasNext()) {
        TipoEventiBdmcModel lTipoEventiBdmc = (TipoEventiBdmcModel)itx.next();
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lTipoEventiBdmc.getIdTipoEventiBdmc(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lTipoEventiBdmc.getCodTipoEvento(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lTipoEventiBdmc.getCodProvvedimento(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lTipoEventiBdmc.getCodMotivo(),"&nbsp;")%></td>
      <td class=c>
      <%
       String modificabile = "SI";
       // inserire qui i criteri in base ai quali il campo è modificabile 

      %>
      <table>
       <tr>
 
                 <td>
               
                   <a href="Main.jsp?Action=siap.siep.tipoeventibdmc.action.ActLoadDettaglioTipoEventiBdmc&IdTipoEventiBdmc=<%=lTipoEventiBdmc.getIdTipoEventiBdmc()%>&TornaQui=20">
                     <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                   </a>
 
               
                 </td>
 
                 <td>
                   <a href="Main.jsp?Action=siap.siep.tipoeventibdmc.action.ActLoadModificaTipoEventiBdmc&IdTipoEventiBdmc=<%=lTipoEventiBdmc.getIdTipoEventiBdmc()%>&TornaQui=20">
                     <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                   </a>
                 </td>
 
                 <td>
                   <a href="Javascript:conferma('siap.siep.tipoeventibdmc.action.ActLoadCancellaTipoEventiBdmc','IdTipoEventiBdmc','<%=lTipoEventiBdmc.getIdTipoEventiBdmc()%>');">
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
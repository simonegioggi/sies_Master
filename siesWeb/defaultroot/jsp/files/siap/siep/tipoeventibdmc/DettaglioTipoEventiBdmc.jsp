<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel"%>
<%@ page import="siap.siep.tipoeventibdmc.action.ICostantiTipoEventiBdmc"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoeventibdmc" scope="request" class="siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel"/>

<html>
<head>
  <%  if( modalita.equals("D") )  {%> 
  <title> Dettaglio TipoEventiBdmc </title>
  <%} else if( modalita.equals("C") ) { %> 
  <title> Cancellazione TipoEventiBdmc </title>
  <%}%> 
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
  <script language="JavaScript" >
    function Verify() { 
      var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
      if (window.confirm(msgConfirm)) 
        return true; 
      else 
        return false; 
    } 
  </script>
</head>

<body class="corpo">

<FORM name="comandi" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"> 
          <img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> 
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
      <%  if( modalita.equals("D") )  {%> 
        <font class="campo">Dettaglio TipoEventiBdmc</font>
      <%} else if( modalita.equals("C") ) { %> 
        <font class="campo">Cancellazione TipoEventiBdmc</font>
      <%}%> 
      </td>
      <td class="LBG">
         <a href="Main.jsp?Action=siap.siep.tipoeventibdmc.action.ActLoadInserisciTipoEventiBdmc&TornaQui=10">
       <img align="middle" src="/images/new24.gif" alt="Inserisci" width="24" height="24" border="0">
    </a>
 
       <a href="Main.jsp?Action=siap.siep.tipoeventibdmc.action.ActLoadModificaTipoEventiBdmc&IdTipoEventiBdmc=<%=tipoeventibdmc.getIdTipoEventiBdmc()%>&TornaQui=10">
         <img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
        </a>
 
         <a href="Javascript:conferma('siap.siep.tipoeventibdmc.action.ActLoadCancellaTipoEventiBdmc','IdTipoEventiBdmc','<%=tipoeventibdmc.getIdTipoEventiBdmc()%>');">
         <img align="middle" src="/images/delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
 
      </td>
    </tr>
  </table>
</FORM>

<%  if( modalita.equals("C") )  {%> 
<FORM method="POST" action="Main.jsp" name="CancellaTipoEventiBdmc">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.siep.tipoeventibdmc.action.ActCancellaTipoEventiBdmc">
  <input type="HIDDEN" name="<%=ICostantiTipoEventiBdmc.CAMPO_ID_TIPO_EVENTI_BDMC%>" value="<%=StringUtils.toStringJSP(tipoeventibdmc.getIdTipoEventiBdmc()) %>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
<%}%> 
<table cellspacing=4 cellpadding=4>
  <tr>
    <td class="l">Id Tipo Eventi Bdmc</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(tipoeventibdmc.getIdTipoEventiBdmc()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Tipo Evento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(tipoeventibdmc.getCodTipoEvento()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Provvedimento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(tipoeventibdmc.getCodProvvedimento()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Motivo</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(tipoeventibdmc.getCodMotivo()) %></font>&nbsp;</td>
  </tr>

<%  if( modalita.equals("C") )  {%> 
  <tr>
    <td align="center">
      <input class="bottone" type="submit" name="conferma" value="Conferma"  onclick="Javascript: return Verify();">
    </td>
  </tr>
<%}%>

</table>
<%  if( modalita.equals("C") )  {%> 
</form>
<%}%>
</body>
</html>
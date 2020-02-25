<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.sbviewreat.model.SbViewReatModel"%>
<%@ page import="siap.bdmc.sbviewreat.action.ICostantiSbViewReat"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="sbviewreat" scope="request" class="siap.bdmc.sbviewreat.model.SbViewReatModel"/>

<html>
<head>
  <%  if( modalita.equals("D") )  {%> 
  <title> Dettaglio SbViewReat </title>
  <%} else if( modalita.equals("C") ) { %> 
  <title> Cancellazione SbViewReat </title>
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
        <font class="campo">Dettaglio SbViewReat</font>
      <%} else if( modalita.equals("C") ) { %> 
        <font class="campo">Cancellazione SbViewReat</font>
      <%}%> 
      </td>
      <td class="LBG">
         <a href="Main.jsp?Action=siap.bdmc.sbviewreat.action.ActLoadInserisciSbViewReat&TornaQui=10">
       <img align="middle" src="/images/new24.gif" alt="Inserisci" width="24" height="24" border="0">
    </a>
 
       <a href="Main.jsp?Action=siap.bdmc.sbviewreat.action.ActLoadModificaSbViewReat&IdSbViewReat=<%=sbviewreat.getNumeProgReat() %>&TornaQui=10">
         <img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
        </a>
 
         <a href="Javascript:conferma('siap.bdmc.sbviewreat.action.ActLoadCancellaSbViewReat','IdSbViewReat','<%=sbviewreat.getNumeProgReat()%>');">
         <img align="middle" src="/images/delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
 
      </td>
    </tr>
  </table>
</FORM>

<%  if( modalita.equals("C") )  {%> 
<FORM method="POST" action="Main.jsp" name="CancellaSbViewReat">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.bdmc.sbviewreat.action.ActCancellaSbViewReat">
  <input type="HIDDEN" name="<%=ICostantiSbViewReat.CAMPO_NUME_PROG_REAT%>" value="<%=StringUtils.toStringJSP(sbviewreat.getNumeProgReat()) %>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
<%}%> 
<table cellspacing=4 cellpadding=4>
  <tr>
    <td class="l">Nume Prog Capo Impu</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewreat.getNumeProgCapoImpu()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Prog Reat</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewreat.getNumeProgReat()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Codi Font Giur</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewreat.getCodiFontGiur()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Font Giur</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewreat.getAnnoFontGiur()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Font Giur</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewreat.getNumeFontGiur()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Arti Font Giur</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewreat.getArtiFontGiur()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Commi Arti Font</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewreat.getCommiArtiFont()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Lett Arti Font</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewreat.getLettArtiFont()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Arti Font</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewreat.getNumeArtiFont()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Arti Qual Font</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewreat.getArtiQualFont()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Id Pren</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewreat.getIdPren()) %></font>&nbsp;</td>
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
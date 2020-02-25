<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel"%>
<%@ page import="siap.bdmc.sbviewprocpena.action.ICostantiSbViewProcpena"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="sbviewprocpena" scope="request" class="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel"/>

<html>
<head>
   
  <title> Dettaglio Sentenza Bdmc </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  </head>

<body class="corpo">
<%
String isVALIGN = "top";
String isBorder = "0";
String lWidth = "96%";
String largh = "8%";
String resto = "92%";

%>
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
       
        <font class="campo">Dettaglio Sentenza Bdmc</font>
      </td>
       <td class="LBG">
          <a href="javascript:history.back()">
            <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
        </tr>
  </table>
</FORM>
<jsp:include page="/jsp/files/siap/bdmc/sbpren/DettaglioSbPrenInclude.jsp"/>
<table  cellspacing=2 cellpadding=2>

<tr>
    <td class="l">Ufficio Bdmc</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getDescriSedeInst()) %> di <%=StringUtils.toStringJSP(sbviewprocpena.getDescriComuSedeInst()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno/Numero Facicolo Bdmc</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoFascBdmc()) %>/<%=StringUtils.toStringJSP(sbviewprocpena.getNumeFascBdmc()) %></font>&nbsp;</td>
  </tr>
 
  <tr><td class="Titolo" colspan=6>Registro Generale</td> </tr>
  <tr>
    <td class="l">Autorita Pm.</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getDescriUffiPmpm()) %> di <%=StringUtils.toStringJSP(sbviewprocpena.getDescriComuUffiPmpm()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Numero Reg. Pm.</td>
    <td class="L" colspan=5 ><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoRegiPmpm()) %>/<%=StringUtils.toStringJSP(sbviewprocpena.getNumeRegiPmpm()) %></font>&nbsp;</td>
  </tr>
  <tr><td class="Titolo" colspan=6>Sentenza Gip</td> </tr>
  <tr>
    <td class="l">Autorità Emittente</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getDescriUffiGipp()) %> di <%=StringUtils.toStringJSP(sbviewprocpena.getDescriComuUffiGipp()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Numero Reg. Gip</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoRegiGipp()) %>/<%=StringUtils.toStringJSP(sbviewprocpena.getNumeRegiGipp()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Sententa </td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataSentGippGupp(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno/Numero Sentenza</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoSentGippGupp()) %>/<%=StringUtils.toStringJSP(sbviewprocpena.getNumeSentGippGupp()) %></font>&nbsp;</td>
  </tr>
  <tr><td class="Titolo" colspan=6>Sentenza Dib</td> </tr>
  <tr>
    <td class="l">Autorita Emittente</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getDescriUffiDibb()) %> di <%=StringUtils.toStringJSP(sbviewprocpena.getDescriComuUffiDibb()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Numero Reg.</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoRegiDibb()) %>/<%=StringUtils.toStringJSP(sbviewprocpena.getNumeRegiDibb()) %></font>&nbsp;</td>
  </tr>
    <tr>
    <td class="l">Data Sententa</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataSent1gra(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno/Numero Sentenza</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoSent1gra()) %>/<%=StringUtils.toStringJSP(sbviewprocpena.getNumeSent1gra()) %></font>&nbsp;</td>
  </tr>
   <tr><td class="Titolo" colspan=6>Sentenza Secondo Grado</td> </tr>
  <tr>
    <td class="l">Autorita Emittente</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getDescriUffiCoap()) %> di <%=StringUtils.toStringJSP(sbviewprocpena.getDescriComuUffiCoap()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Numero Reg. </td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getNumeRegiCoap()) %>/<%=StringUtils.toStringJSP(sbviewprocpena.getAnnoRegiCoap()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Sentenza </td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataSent2gra(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno/Numero Sentenza</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoSent2gra()) %>/<%=StringUtils.toStringJSP(sbviewprocpena.getNumeSent2gra()) %></font>&nbsp;</td>
  </tr>
   <tr><td class="Titolo" colspan=6>Sentenza Cassazione</td> </tr>
  <tr>
    <td class="l">Data Sentenza </td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataDeciCass(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno/Numero Sentenza</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoDeciCass()) %>/<%=StringUtils.toStringJSP(sbviewprocpena.getNumeDeciCass()) %></font>&nbsp;</td>
  </tr>
   <tr><td class="Titolo" colspan=6>Irrevocabilità</td> </tr>
  <tr>
    <td class="l">Data Irrevocabilita</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataPassGiud(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  
  

</table>
</body>
</html>
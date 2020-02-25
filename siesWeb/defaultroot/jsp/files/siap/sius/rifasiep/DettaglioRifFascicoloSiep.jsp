<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel"%>
<%@ page import="siap.sius.rifasiep.action.ICostantiRifFascicoloSiep"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="RiferimentoFascicoloSiep" scope="request" class="siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel"/>
<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Riferimento Altro Titolo Esecutivo </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="/html/conferma.js"></script>
</head>

  <body class="corpo">
  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Riferimento Altro Titolo Esecutivo</font>
        </td>

        <%-- BOTTONE DI INSERIMENTO NUOVO RIF. FASCICOLO SIEP --%>
        <td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.sius.rifasiep.action.ActLoadInserisciRifFascicoloSiep&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=RiferimentoFascicoloSiep.getFasSiuIdFascicoloSius()%>">
            <img  align="middle" src="/images/new24.gif" alt="Iscrizione Riferimenti altri titoli esecutiviDestinatari" width="24" height="24" border="0">
          </a>
        </td>
        <%-- BOTTONE DI CANCELLAZIONE --%>
        <td class="LBG">
          <a href="Javascript:conferma('siap.sius.rifasiep.action.ActCancellaRifFascicoloSiep','<%=ICostantiRifFascicoloSiep.CAMPO_ID_RIFERIMENTO_FASCICOLO_SIEP%>','<%=RiferimentoFascicoloSiep.getIdRiferimentoFascicoloSiep()%>','<%=ICostantiRifFascicoloSiep.ACTION_DOPO_CANCELLAZIONE%>','siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=RiferimentoFascicoloSiep.getFasSiuIdFascicoloSius()%>');">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
        </td>
        <!-- BOTTONE DI RITORNO -->
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

      </tr>
    </table>
    <input type="HIDDEN" name="IdFascicoloSius" value="<%=RiferimentoFascicoloSiep.getFasSiuIdFascicoloSius()%>">
  </FORM>

  <table>
    <tr>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table>

  <table cellspacing=2 cellpadding=2>

<% 
	if(RiferimentoFascicoloSiep.getFlagMS() == null || RiferimentoFascicoloSiep.getFlagMS().equals("") || RiferimentoFascicoloSiep.getFlagMS().equals("N")) {
%>

  <tr>
    <td class="l">Anno/Numero Procedimento SIEP</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(RiferimentoFascicoloSiep.getAnnoFascicoloSiep() )%> / <%=StringUtils.toStringJSP(RiferimentoFascicoloSiep.getProgrFascicoloSiep() )%> </font></td>
  </tr>
  
<% 
	} else {
		if (RiferimentoFascicoloSiep.getAnnoFascicoloSiep() == null && RiferimentoFascicoloSiep.getProgrFascicoloSiep() == null){
			if(RiferimentoFascicoloSiep.getFlagMS().equals("M")){
%>
			  <tr>
			    <td class="l">Anno/Numero Es. Mis. Sic.</td>
			    <td class="l"></td>
			  </tr>
<%
			} else {
%>
			  <tr>
			    <td class="l">Anno/Numero Es. Pene Pec.</td>
			    <td class="l"></td>
			  </tr>
<%				
			}
		} else {
			if(RiferimentoFascicoloSiep.getFlagMS().equals("M")){
%>
			  <tr>
			    <td class="l">Anno/Numero Es. Mis. Sic.</td>
			    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(RiferimentoFascicoloSiep.getAnnoFascicoloSiep() )%> / <%=StringUtils.toStringJSP(RiferimentoFascicoloSiep.getProgrFascicoloSiep() )%> MS </font></td>
			  </tr>
<%
			} else {
%>
			  <tr>
			    <td class="l">Anno/Numero Es. Pene Pec.</td>
			    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(RiferimentoFascicoloSiep.getAnnoFascicoloSiep() )%> / <%=StringUtils.toStringJSP(RiferimentoFascicoloSiep.getProgrFascicoloSiep() )%> PP </font></td>
			  </tr>
<%				
			}
		}
	}
%>
  
  <tr>
    <td class="l">Autorità competente</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(RiferimentoFascicoloSiep.getDescrUffFascicoloSiep() )%> </font></td>
  </tr>
  <tr>
    <td class="l">Tipo Provvedimento</td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(RiferimentoFascicoloSiep.getDescrTipoProvvedimento() )%></font></td>
  </tr>

  <tr>
    <td class="l">Data Provvedimento</td>
    <td class="l"><font class="campo"> <%=DateUtils.getDateToString(RiferimentoFascicoloSiep.getDataProvvedimento(), "dd-MM-yyyy" )%></font></td>
  </tr>

<% 
	if(RiferimentoFascicoloSiep.getFlagMS() == null || RiferimentoFascicoloSiep.getFlagMS().equals("") || RiferimentoFascicoloSiep.getFlagMS().equals("N")) {
%>
  <tr>
    <td class="l">Anno/Numero Provvedimento</td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(RiferimentoFascicoloSiep.getAnnoProvvedimento() )%> / <%=StringUtils.toStringJSP(RiferimentoFascicoloSiep.getNumeroProvvedimento() )%> </font></td>
  </tr>
<% 
	} 
%>
  <tr>
    <td class="l">Definitivo in data</td>
<%
    if(RiferimentoFascicoloSiep.getDataIrrevocabilita() != null){
%>
    	<td class="l"><font class="campo"> <%=DateUtils.getDateToString(RiferimentoFascicoloSiep.getDataIrrevocabilita(), "dd-MM-yyyy" )%></font></td>
<%
    } else {
%>  
		<td class="l">&nbsp;</td>
<%
    } 
%>
  
  </tr>
  <tr>
    <td class="l">Autorità Emittente</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(RiferimentoFascicoloSiep.getDescrTipoAutoritaEmittente() )%> di <%=StringUtils.toStringJSP(RiferimentoFascicoloSiep.getDescrLuogoEmittente() )%></font></td>
  </tr>
  <tr>
    <td class="l">Note</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(RiferimentoFascicoloSiep.getNote() )%> </font></td>
  </tr>

  </table>
  </body>
</html>
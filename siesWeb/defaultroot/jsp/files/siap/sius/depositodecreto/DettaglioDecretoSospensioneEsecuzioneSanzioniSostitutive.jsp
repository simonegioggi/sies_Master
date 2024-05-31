<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="f3b.util.Utils"%>
<%@page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sius.avvocatura.action.ICostantiAvvisiAvvocato"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="depositoDecretoMotivazioni" scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="tenori"                     scope="request" class="java.util.Vector"/>
<jsp:useBean id="PeriodoAltraSanzione" 	     scope="request" class="siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel"/>

<%
UtenteModel lUteMod = (UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
UfficioModel lUffMod = lUteMod.getUfficioUtente();
String CodUff = new String(lUffMod.getCodTipoUfficio());
String labelUfficio = "";
if (CodUff.equals("TDSM") || CodUff.equals("UDSM")) {
	labelUfficio = "Tribunale per i Minorenni in funzione di Tribunale Sorveglianza";
} else {
	labelUfficio = "Tribunale di Sorveglianza";
}
// MEV_35: recupero info sul fascicolo per oggetto procedimento
String codOggettoProcedimento = "", tipoSostituzione = "Sanzione";
FascicoloGPModel fgpm = (FascicoloGPModel) session.getAttribute("fascicoloSiusGP");
GeneraleProcedimentoModel gpm = new GeneraleProcedimentoModel();
if (!Utils.isNullObj(fgpm.getGeneraleProcedimentoModel()))
	gpm = fgpm.getGeneraleProcedimentoModel();
if (!Utils.isNullObj(gpm.getCodOggettoProcedimento()))
	codOggettoProcedimento = gpm.getCodOggettoProcedimento();
if ("U134".equals(codOggettoProcedimento))
	tipoSostituzione = "Pena";
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Decreto Sospensione Esecuzione <%=tipoSostituzione%> Sostitutiva</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript" src="/html/gestisciUploadStampa.js"></script>
</head>
<body class="corpo">
<form name="dettaglio">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
        <td class="LBG">
        	<font class="label">Funzione : </font>
        	<font class="campo">Dettaglio Decreto Sospensione Esecuzione <%=tipoSostituzione%> Sostitutiva</font>
        </td>
        <jsp:include page="<%=ICostantiDepositoDecreto.BOTTONI_DETTAGLIO_DECRETO%>"/>
	</tr>
</table>
<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
<jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>"/>
<jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>"/>

<table cellspacing=4 cellpadding=4 width=95%>
	<tr>
		<td>
    		<input Title="Id Evento" type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>">
    	</td>
  	</tr>
  	<tr>
		<jsp:include page="<%=ICostantiDepositoDecreto.PG_DETTAGLIO_DATA%>"/>
  	</tr>
  	<tr>
	    <td class="l"> Eventuale Motivazione </td>
	    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getNote(),"-")%></font></td>
  	</tr>
  	<tr>
	    <td class="l"> <%=labelUfficio%> Competente</td>
	    <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getDescrTdsComp(),"-")%></font></td>
  	</tr>
  	<tr>
	    <td class="l"> Data Decorrenza Sospensione</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataSospensioneSS(),"dd/MM/yyyy"))%> </font>&nbsp;</td>
  	</tr>
<%
if (depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneAASS().compareTo(new BigDecimal(0)) !=0 || 
		  depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneMMSS().compareTo(new BigDecimal(0)) !=0  || 
		  	depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneGGSS().compareTo(new BigDecimal(0)) !=0)
  	{%>
  <tr>
    <td class="l"> Periodo Sospensione</td>
    <td class="l"><font class="campo">ANNI <%=depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneAASS() %> MESI <%=depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneMMSS() %> GIORNI <%=depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneGGSS() %></font></td>
  </tr>
  <%}%>
  <%if(depositoDecretoMotivazioni.getDepositoDecreto().getDataScadenzaSospensioneSS() != null)
	{%>
   <tr>
    <td class="l"> Fino al</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataScadenzaSospensioneSS(),"dd/MM/yyyy"))%> </font>&nbsp;</td>
  </tr>
  <%}
  // Controllo per visualizzazione campo "Giorni da recuperare"
  if(depositoDecretoMotivazioni.getDepositoDecreto().getFlagRecuperoSS()!= null && depositoDecretoMotivazioni.getDepositoDecreto().getFlagRecuperoSS().equals("S"))
  {%>
   <tr>
   	<td class="l"> Giorni da recuperare</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getGiorniRecuperoSS())%> </font>&nbsp;</td>
  </tr>
<%} 
  if(PeriodoAltraSanzione != null && PeriodoAltraSanzione.getIdPeriodoAltraSanzione() != null)
  {
%>
  <tr>
    <td colspan=2>&nbsp;</td>
  </tr>
  <tr>
    <td class="l"><%=tipoSostituzione%> Sostitutiva Espiata</td>
    <td class="l"><font class="campo">ANNI <%=PeriodoAltraSanzione.getEspiataAA() %> MESI <%=PeriodoAltraSanzione.getEspiataMM() %> GIORNI <%=PeriodoAltraSanzione.getEspiataGG() %></font></td>
  </tr>
  <tr>
    <td class="l"><%=tipoSostituzione%> Sostitutiva residua da Espiare</td>
    <td class="l"><font class="campo">ANNI <%=PeriodoAltraSanzione.getResiduaAA() %> MESI <%=PeriodoAltraSanzione.getResiduaMM() %> GIORNI <%=PeriodoAltraSanzione.getResiduaGG() %></font></td>
  </tr>
 <%} 
%>
  <tr>
    <td colspan=2>&nbsp;</td>
  </tr>
  <tr>
    <td class="Titolo" colspan=2> Esiti</td>
  </tr>
</table>

<table cellspacing=4 cellpadding=4 width=95%>

<%
  Iterator lInd = tenori.iterator();
  while (lInd.hasNext())
  {
%>
   <tr>
      <%
        TenoreModel lTen = (TenoreModel) lInd.next();
      %>
      <td class="l" width=70% ><%=lTen.getDescrOggettoTenore()%></td>
      <td class="l" width=30% ><%=lTen.getDescrEsitoTenore()%> </td>
  </tr>
<%
  }
%>
  <tr>
    <td  colspan=2> &nbsp;</td>
  </tr>
  </table>
  
  <jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
</form>

 <div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post">
  <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
    <tr>
      <td class="L">
       <input class="bottone"  type="submit" value="Conferma">
       <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
       <input type="HIDDEN" name="IdEvento"  value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>">
       <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito">
       <input type="HIDDEN" name="FlagAvvocatura" value="<%=ICostantiAvvisiAvvocato.EMISSIONE_DECRETO%>">
      </td>
    </tr>
  </table>

  </FORM>
  </div>

</body>

</html>
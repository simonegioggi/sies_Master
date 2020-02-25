<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata" %>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.avvocatura.action.ICostantiAvvisiAvvocato" %>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="depositoDecretoMotivazioni"  scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="tenori"                      scope="request" class="java.util.Vector"/>
<jsp:useBean id="prescrizioni"                scope="request" class="java.util.Vector"/>
<jsp:useBean id="AutoTemplate"                scope="request" class="java.lang.String"/>
<jsp:useBean id="StatoPermesso"               scope="request" class="java.util.Vector"/>
<jsp:useBean id="revoca"                     scope="request" class="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"/>
<jsp:useBean id="decretoRevocato"  scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>
<jsp:useBean id="LicenzePeriodi"      scope="request" class="java.util.Vector"/>

<%
String nomeFunzione="";
if(depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto().compareTo(ICostantiDepositoOrdinanzaPc.VIOLAZIONE_CEDU)== 0)
{
	  nomeFunzione = "Dettaglio Decreto Rimedi Risarcitori Vioalzione Art.3 CEDU";
}

// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//siesLogger.debug(" Dettaglio Decreto Revoca - getCodTipoDecreto = "+depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto());
%>

<!-- 	DettaglioDecretoViolazioneCEDU	 -->

<html>
<head>
<title>[S.I.A.P.] - Dettaglio Decreto  </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript" src="/html/gestisciUploadStampa.js"></script>

</head>
  <body class="corpo">
  <form name="dettaglio">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione : </font>
          <font class="campo"><%=nomeFunzione%></font>&nbsp;
        </td>
        <jsp:include page="<%=ICostantiDepositoDecreto.BOTTONI_DETTAGLIO_DECRETO%>"/>
      </tr>
    </table>
    <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    <br>
    <jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>"/>
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--  jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>"/ --%>
    <%--  jsp:include page="<%=ICostantiDepositoDecreto.PG_DETTAGLIO_DATA%>"/ --%>
    
    <jsp:include page="<%=ICostantiDepositoDecreto.PG_DECRETO_VIOLAZIONE_CEDU_RIEPILOGO%>"/>


   <table cellspacing=4 cellpadding=4  width=98%>  
      <tr>
       <td class="Titolo" colspan="3"> Esiti</td>
     </tr>  
  <%
    //Elenco Tenori.
    
    int lSize = tenori.size();
  	Iterator lInd = tenori.iterator();
  	while (lInd.hasNext())
  	{  
  		TenoreModel lTen = (TenoreModel) lInd.next();
  %>
        <tr>
          <td class="l" width=43% ><%= lTen.getDescrOggettoTenore()%></td>
          <td class="l" width=55% ><%= lTen.getDescrEsitoTenore()%></td>
        </tr>
  <%
    }  // Chiude Iterator Tenori
%>
    <tr>
      <td  colspan="2"> &nbsp;</td>
    </tr>

   </table> 
 
    	 <tr>
      		<input Title="Id Evento" type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>" >
    	 </tr>
   
	<table cellspacing=4 cellpadding=4>
	    <tr>
	      <td class="l">Ulteriore descrizione della decisione </td>
	      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( depositoDecretoMotivazioni.getDepositoDecreto().getNote() , "-")%></font></td>
	    </tr>
	      <tr> <td> <br></td></tr>
	</table>  
	
	<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_DETTAGLIO_ORDINANZA_RISARCIMENTO_VIOLAZIONE_CEDU%>"/>

<!--  Modelli di STAMPA -->
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
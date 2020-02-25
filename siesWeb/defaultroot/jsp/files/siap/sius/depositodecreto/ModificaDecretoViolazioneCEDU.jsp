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
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="depositoDecretoMotivazioni"  scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="tenori"                      scope="request" class="java.util.Vector"/>
<jsp:useBean id="prescrizioni"                scope="request" class="java.util.Vector"/>
<jsp:useBean id="AutoTemplate"                scope="request" class="java.lang.String"/>
<jsp:useBean id="StatoPermesso"               scope="request" class="java.util.Vector"/>
<jsp:useBean id="revoca"                     scope="request" class="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"/>
<jsp:useBean id="decretoRevocato"  	scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>
<jsp:useBean id="TornaQui" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="StackDiRitorno" 	scope="session" class="java.util.Stack"/>
<jsp:useBean id="LicenzePeriodi"    scope="request" class="java.util.Vector"/>

<jsp:useBean id="CodTipoProvvedimento" scope="request" class="java.lang.String"/>

<%
String nomeFunzione="";

if(depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto().compareTo(ICostantiDepositoOrdinanzaPc.VIOLAZIONE_CEDU)== 0)
{
	  nomeFunzione = "Modifica Decreto Rimedi Risarcitori Vioalzione Art.3 CEDU";
}

boolean retFlag = false;
retFlag = ( (TornaQui != null) && TornaQui.trim().length() > 1 );
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//siesLogger.debug(" Modifica Decreto CEDU --------------->  ");
%>

<!-- 	ModificaDecretoViolazioneCEDU	 -->

<html>
<head>
<title>[S.I.A.P.] - Modifica Decreto  </title>

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
      <!-- BOTTONE DI RITORNO -->
		<td class="LBG">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito&<%=ICostantiEvento.CAMPO_ID_EVENTO %>=<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%><%=retParam%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
      </tr>
    </table>
    <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    <br>
    <jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>"/>
    
    <jsp:include page="<%=ICostantiDepositoDecreto.PG_DECRETO_VIOLAZIONE_CEDU_RIEPILOGO%>"/>
	<input Title="Id Evento" type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>" >

  </form>
  
  <jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_MODIFICA_ORDINANZA_RISARCIMENTO_VIOLAZIONE_CEDU%>"/>
 
</body>
</html>
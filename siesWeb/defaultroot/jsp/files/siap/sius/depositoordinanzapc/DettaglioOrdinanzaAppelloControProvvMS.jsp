<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina di dettaglio appello contro provv su MS --%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sius.avvocatura.action.ICostantiAvvisiAvvocato"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>

<jsp:useBean id="Stampabile"         		scope="request" class="java.lang.String"/>
<jsp:useBean id="ElencoTemplate"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="datiOrdinanza"      		scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="TornaQui"           		scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"           		scope="request" class="java.lang.String"/>
<jsp:useBean id="misuresicurezza"     		scope="request" class="java.util.Vector"/>
<jsp:useBean id="esecuzionemisurasicurezza" scope="request" class="siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel" />
<jsp:useBean id="misuraSicurezza" 			scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>

<%
BigDecimal IdEvento = (BigDecimal) request.getAttribute("IdEvento");
%>

<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<%
boolean retFlag = false;
retFlag = (TornaQui != null && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
String titolo = "Dettaglio Ordinanza";
%>

<html>
	<head>
  	<title>[S.I.E.S.] - Dettaglio Ordinanza Appello Contro Provvedimento su Misura di Sicurezza</title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="/html/conferma.js"></script>
	</head>

	<body class="corpo">
  	<table>
    	<tr>
    		<td class="LBG">
    			<a href="Javascript:window.print();">
    				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
    			</a>
    		</td>
      		<td class=LBG>
        		<font class="label">Funzione: </font>
        		<font class="campo"><%=titolo%></font>
      		</td>

<%
if (Stampabile == null || Stampabile.trim().length() < 1)
	Stampabile = "SI";

if (Stampabile.compareTo("SI") == 0) {
    // Deve esistere il template : da list o predefinito.
    if ((ElencoTemplate != null && ElencoTemplate.trim().length() > 0)
    		|| (datiOrdinanza.getEvento().getTemIdTemplate() != null && datiOrdinanza.getEvento().getTemIdTemplate().trim().length() > 1)) {
%>
	    	<!-- BOTTONE DI STAMPA -->
	    	<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIUS%>">
	      		<jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"/>
	      		<jsp:param name="ValoreIdEntita" value="<%=IdEvento%>"/>
	    	</jsp:include>
<%
	} /* endif esistenza ElencoTemplate */
} /* endif Stampabile = SI */
%>
<%
if (Stampabile.compareTo("NO") != 0) {
%>
			<!-- BOTTONE DI MODIFICA -->
			<td class="LBG">
    			<a href="/jsp/Main.jsp?Action=siap.sius.provvedimento.action.ActLoadModificaProvvedimento&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=IdEvento%>&<%=ICostantiProvvedimento.CAMPO_TIPO_PROVVEDIMENTO%>=03<%=retParam%>">
        			<img  align="middle" src="/images/modifica24.gif" alt="Modifica Ordinanza" width="24" height="24" border="0">
      			</a>
    		</td>
    		<!-- BOTTONE DI CANCELLAZIONE -->
    		<td class="LBG">
      			<a href="Javascript:conferma('siap.sius.depositoordinanzapc.action.ActCancellaEmissioneOrdinanza','<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=IdEvento%>','TornaQui','<%=TornaQui%>');">
        			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
      			</a>
    		</td>
<%
}
%>
      		<!-- BOTTONE DI RITORNO -->
      		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    	</tr>
    	<tr>
      		<td>&nbsp;</td>
    	</tr>
    	<tr>
     		<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    	</tr>
    	<tr>
      		<jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>"/>
    	</tr>
	</table>

  	<table cellspacing="2" cellpadding="2"  width="95%">
    	<tr>
      		<td>&nbsp;</td>
      		<input Title="Id Evento" type="hidden" name="<%= ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=IdEvento%>">
    	</tr>
  		<tr>
    		<td class="l" width="25%">Tipo di Ordinanza</td>
    		<td class="l">
    			<font class="campo">
    				<%=(datiOrdinanza != null && datiOrdinanza.getOrdinanza() != null && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza() != null)
    				? DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoOrdinanza(), datiOrdinanza.getOrdinanza().getCodTipoOrdinanza())
    						: ""%></font>
			</td>
  		</tr>
    	<tr>
      		<td class="L"><font class="label">Data Emissione</font></td>
      		<td class="L"><font class="campo"><%=DateUtils.getDateToString(datiOrdinanza.getEvento().getDataEmissione(),"dd/MM/yyyy")%></font></td>
    	</tr>
  		<tr>
    		<td class="l"> Stato del provvedimento</td>
<%
if (datiOrdinanza.getEvento().getFlagDocumentoRegistrato() != null && datiOrdinanza.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("A")) {
%>
    		<td class="l"><font class="cRosso">ANNULLATO</font></td>
<%
} else if (datiOrdinanza.getEvento().getFlagDocumentoRegistrato() != null && datiOrdinanza.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("S")) {
%>
    		<td class="l"><font class="campo">Validato</font></td>
<%
} else {
%>
    		<td class="l"><font class="campo">Da Validare </font></td>
<%
}
%>
		</tr>
<!--     	<tr> -->
<!--       		<td class="L"><font class="label">Dispositivo</font></td> -->
<%--       		<td class="L"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento(),"-")%></font></td> --%>
<!--     	</tr> -->
    	<tr>
      		<td class="l">Ulteriore descrizione della decisione </td>
      		<td class="l"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
    	</tr>
  	</table>
  	<br/>
	<table cellspacing="2" cellpadding="2"  width="95%">
<%
// Elenco Tenori.
int lSize = datiOrdinanza.getTenori().length;
for (int x = 0; x < lSize; x++) {
%>
		<tr>
       		<td class="Titolo" colspan="2"> Esiti</td>
      	</tr>
        <tr>
          <td class="l" width="35%"><%=datiOrdinanza.getTenori()[x].getDescrOggettoTenore()%></td>
          <td class="l"><%=datiOrdinanza.getTenori()[x].getDescrEsitoTenore()%></td>
        </tr>
<%
}
%>
	</table>
	<br/>
	<table cellspacing="2" cellpadding="2"  width="95%">
		<tr>
			<td class="Titolo" colspan="2">Misura di Sicurezza in Esecuzione</td>
		</tr>
<%
String descrTipo = "";
BigDecimal anni = new BigDecimal(0);
BigDecimal mesi = new BigDecimal(0);
BigDecimal giorni = new BigDecimal(0);
if (misuresicurezza != null && misuresicurezza.size() > 0) {
	descrTipo = ((MisuraSicurezzaModel) misuresicurezza.get(0)).getDescrTipo();
	anni = ((MisuraSicurezzaModel) misuresicurezza.get(0)).getNumAnni();
	mesi = ((MisuraSicurezzaModel) misuresicurezza.get(0)).getNumMesi();
	giorni = ((MisuraSicurezzaModel) misuresicurezza.get(0)).getNumGiorni();
} else {
	descrTipo = esecuzionemisurasicurezza.getDescrTipoMisura();
	anni = esecuzionemisurasicurezza.getNumAnniMisura();
	mesi = esecuzionemisurasicurezza.getNumMesiMisura();
	giorni = esecuzionemisurasicurezza.getNumGiorniMisura();
}
%>
		<tr>
            <td class="l" width="25%">Tipo:</td>
            <td class="l"><font class="campo"><%=descrTipo%></font></td>
		</tr>
		<tr>
            <td class="l">Durata:</td>
            <td class="l">
            	<font class="campo">
	            	Anni: <%=anni == null ? 0 : anni%>&nbsp;
	            	Mesi: <%=mesi == null ? 0 : mesi%>&nbsp;
	            	Giorni: <%=giorni == null ? 0 : giorni%>
            	</font>
            </td>
		</tr>
<%
if (datiOrdinanza.getTenori() != null && datiOrdinanza.getTenori().length > 0) {
	for (int y = 0; y < lSize; y++) {
	    if ("0181".equals(datiOrdinanza.getTenori()[y].getCodEsitoTenore())) {
	    	String dataDecorrenza = DateUtils.getDateToString(misuraSicurezza.getDataDecorrenza(), "dd/MM/yyyy");
%>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td class="Titolo" colspan="2">Dati Nuova Misura di Sicurezza</td>
		</tr>
		<tr>
            <td class="l">Tipo:</td>
            <td class="l"><font class="campo"><%=misuraSicurezza.getDescrTipo()%></font></td>
		</tr>
		<% if (Utils.isPresent(dataDecorrenza)) { %>
		<tr>
            <td class="l">Data Decorrenza:</td>
            <td class="l"><font class="campo"><%=dataDecorrenza%></font></td>
		</tr>
		<% } %>
		<tr>
            <td class="l">Durata:</td>
            <td class="l">
            	<font class="campo">
	            	Anni: <%=(misuraSicurezza.getNumAnni() == null ? "0" : misuraSicurezza.getNumAnni())%>&nbsp;
	            	Mesi: <%=(misuraSicurezza.getNumMesi() == null ? "0" : misuraSicurezza.getNumMesi())%>&nbsp;
	            	Giorni: <%=(misuraSicurezza.getNumGiorni()==null ? "0" : misuraSicurezza.getNumGiorni())%>
            	</font>
            </td>
		</tr>
<% 
			break;
		} // end if interno
	} // end for
} // end if esterno
%>
	</table>
	<br/>
    <jsp:include page="<%=ICostantiPrescrizione.PG_INCLUDE_PRESCRIZIONI%>">
      	<jsp:param name="EveIdEvento" value="<%=IdEvento%>" />
      	<jsp:param name="nextaction" value="siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza"/>
    </jsp:include>
	<br/>
	<form name="dettaglio">
<%
if (datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().trim().length() > 1) {
	// Combo template di stampa solo sulle Emissioni di Ordinanza Nuove
	// E se non esiste il template predefinito.
	if (datiOrdinanza.getEvento().getTemIdTemplate() == null || datiOrdinanza.getEvento().getTemIdTemplate().trim().length() < 2) {
%>
		<jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
<%
	}
}
%>
	</form>

	<div align=left style="visibility:hidden" id="upld">
    	<FORM name="comandi" enctype="multipart/form-data" method="post">
   			<table>
          		<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
       	 		<tr>
          			<td class="L">
			            <input  class=bottone  type="submit" value="Conferma">
			            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
			            <input type="HIDDEN" name="IdEvento" value="<%=IdEvento%>">
			            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza">
          				<input type="HIDDEN" name="FlagAvvocatura" value="<%=ICostantiAvvisiAvvocato.EMISSIONE_ORDINANZA%>">
          			</td>
        		</tr>
        	</table>
		</FORM>
	</div>
	</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.rateizzazionepp.model.RateizzazionePPModel"%>
<%@ page import="siap.siep.rateizzazionepp.action.ICostantiRateizzazionePP"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>
<%@ page import="siap.siep.rateizzazionepp.model.EventoRateizzazionePPModel"%>

<jsp:useBean id="fascicolo"         				scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="dettaglioPenaComplessiva"  		scope="request" class="siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel"/>
<%-- MEV_2023-33: aggiunto useBean di storicizzazione --%>
<jsp:useBean id="isEventoRateizzazioneAnnullato" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="listaRateizzazioniLibere"  		scope="request" class="java.util.Vector<RateizzazionePPModel>"/>
<jsp:useBean id="listaOrdiniIngiunzione"    		scope="request" class="java.util.Vector<EventoRateizzazionePPModel>"/>
<html>
<head>
<title>[S.I.E.S.] - Rateizzazione Pena Pecuniaria</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
function cancellaRate() {
	var lAzione = "siap.siep.rateizzazionepp.action.ActCancellaRateizzazione";
  	var msgConfirm = "Si vuole procedere alla cancellazione della Modalità di pagamento non collegata ad alcun provvedimento?"; 
  	msgConfirm = msgConfirm + " Gli eventuali bollettini gia' emessi verranno cancellati."
	if (window.confirm(msgConfirm)) {
	    document.RateizzazionePP.Action.value = lAzione;
	    document.RateizzazionePP.submit();
  	}
}

function modificaRate() {
	var lAzione = "siap.siep.rateizzazionepp.action.ActLoadModificaRateizzazione";
	document.RateizzazionePP.Action.value = lAzione;
	document.RateizzazionePP.submit();
}

function aggiungiRate() {
	var lAzione = "siap.siep.rateizzazionepp.action.ActLoadInserisciRateizzazione";
	document.RateizzazionePP.inserimento.value = "true";
	document.RateizzazionePP.Action.value = lAzione;
	document.RateizzazionePP.submit();
}
</script>

<STYLE>
.menulines {
	border:2.5px solid #BEC6FC;
	text-align : center;
	font-family: 'Tahoma';
	color : Navy;
	font-size : 13px;
	text-decoration : none;
	height:100%;
	background : url("/images/fondoL.gif") bottom;
}

.menulines a {
	text-align : center;
	text-decoration:none;
	color:black;
	font-family: 'Tahoma';
	color : Navy;
	font-size : 13px;
	width:100%;
	height:100%;
	background : url("/images/fondoL.gif") bottom;
}
</STYLE>
</head>

<body class="corpo" >
<table>
	<tr>
    	<td class="LBG">
      		<a href="Javascript:window.print();">
        		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
      		</a>
    	</td>
    	<td class="LBG">
			<font class="label">Funzione :</font>&nbsp;&nbsp;
			<font class="campo">Dettaglio Modalita' pagamento Pena Pecuniaria</font>
    	</td>
    	<td class="LBG">
<%
if (listaRateizzazioniLibere.size() > 0) {
%>
			<a href="javascript:modificaRate()">
        		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica Rate" width="24" height="24" border="0">
      		</a>
      		<a href="javascript:cancellaRate()" >
        		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella Rate" width="24" height="24" border="0">
      		</a>
<%
} else {
%>
      		<a href="javascript:aggiungiRate()" >
        		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Aggiungi Rate" width="24" height="24" border="0">
      		</a>
<%
}
%>
		</td>
    	<td class="LBG">
      		<a href="<%=IWebConstants.PG_MAIN %>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penacomplessiva.action.ActLoadDettaglioPenaComplessiva&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP %>=<%=fascicolo.getIdFascicoloSiep()%>">
        		<img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
      		</a>
    	</td>
	</tr>
</table>

<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="RateizzazionePP">
	<input type="HIDDEN" name="Action" value="">
	<input type="HIDDEN" name="inserimento" value="">
</form>

<%
PenaComplessivaModel     lPenCom = (dettaglioPenaComplessiva != null && dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva() != null) ? dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva() : null;
SanzioneSostitutivaModel lSanSos = (dettaglioPenaComplessiva != null && dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva() != null) ? dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva().getSanzioneSostitutiva() : null;
%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="L" width="20%">
			<font class="label">Pena Pecuniaria:</font>
		</td>
		<td class="L">
			<font class="label">MULTA</font> <font class="campo"><%=StringUtils.toEuroFormat((lPenCom != null ? lPenCom.getImportoMulta() : null) )%></font>&nbsp;<font class="label">&euro;</font>
			<font class="label">,&nbsp;AMMENDA</font> <font class="campo"><%=StringUtils.toEuroFormat((lPenCom != null ? lPenCom.getImportoAmmenda() : null))%> </font>&nbsp;<font class="label">&euro;</font>  
		</td>
	</tr>
	<tr>
		<td class="L">
			<font class="label">Pena Pecuniaria Sostitutiva:</font>
		</td>
		<td class="L">
			<font class="label">MULTA</font> <font class="campo"><%=StringUtils.toEuroFormat((lSanSos != null ? lSanSos.getSanzionePecuniariaMulta() : null))%></font>&nbsp;<font class="label">&euro;</font>
			<font class="label">,&nbsp;AMMENDA</font> <font class="campo"><%=StringUtils.toEuroFormat((lSanSos != null ? lSanSos.getSanzionePecuniariaAmmenda() : null))%> </font>&nbsp;<font class="label">&euro;</font>  
		</td>
	</tr>
</table>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
<%
Iterator<EventoRateizzazionePPModel> itx = listaOrdiniIngiunzione.iterator();
while (itx.hasNext()) {
	EventoRateizzazionePPModel erppm = (EventoRateizzazionePPModel) itx.next();
  	EventoModel em = erppm.getEvento();
	String lTipoRateizzazione = erppm.getListaRateizzazioniPP().elementAt(0).getTipoRateizzazione();
	BigDecimal lImportoDaPagare = erppm.getListaRateizzazioniPP().elementAt(0).getImportoDaPagare();
%>  
  	<tr style="background-color: green;">
    	<td class="int" colspan="8">
      		<%=StringUtils.toStringJSP(em.getDescrTipoProvvedimento()) + " " + StringUtils.toStringJSP(em.getDescrMotivo())%>
			&nbsp;del&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(em.getDataEmissione(), "dd/MM/yyyy"))%>
    	</td>
	</tr>
	<tr>
		<td class="L" colspan="8">
			<font class="label">Importo da pagare</font>
			<font class="campo"><%=StringUtils.toEuroFormat(lImportoDaPagare)%> &euro;</font>
		</td>
	</tr>
	<tr>
<%
	if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_UNICA)) {
%>
		<td class="Titolo" colspan="6"> Pagamento in una Unica Soluzione </td>
<%
	} else if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE)) {
%>
        <td class="Titolo" colspan="6"> Pagamento Rateizzato </td>
<%
	}
%>
		<td class="Titolo" colspan="1">Emessi bollettini</td>
		<td class="Titolo" colspan="1">Emesso Provvedimento</td>
  	</tr>
<%
	// Recupero le rateizzazioni
	Vector<RateizzazionePPModel> listaRateizzazioni = erppm.getListaRateizzazioniPP();
	Iterator<RateizzazionePPModel> IteRate = listaRateizzazioni.iterator();
	int conta = 0;
	while (IteRate.hasNext()) {
		conta++;
		RateizzazionePPModel rata = (RateizzazionePPModel) IteRate.next();
		String spunta = "V";
		if ("A".equals(em.getFlagDocumentoRegistrato())) {
			spunta = "TickRed";
		}
  		String actDettaglio = "";
  		if ("0622".equals(em.getCodMotivo()))
    		actDettaglio = "siap.siep.sanzionesostitutiva.action.ActLoadDettaglioOrdineIngiunzione";
  		else if ("1307".equals(em.getCodMotivo()))
    		actDettaglio = "siap.siep.rateizzazionepp.action.ActDettaglioRideterminazionePP";
  		else if ("1308".equals(em.getCodMotivo()))
    		actDettaglio = "siap.siep.rateizzazionepp.action.ActDettaglioAvvisoMancatoPagamento";
		if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_UNICA)) {
%>
	<tr>
		<td class="L" nowrap><font class="label">Rata unica da</font>&nbsp;<font class="campo"><%=StringUtils.toEuroFormat(rata.getImportoRata())%> &euro;</font></td>      
		<td class="R" nowrap><font class="label">termine di pagamento fissato entro </font></td>
		<td class="R"><font class="campo"><%=StringUtils.toStringJSP(rata.getScadenzaGiorni(),"&nbsp;")%></font></td>
		<td class="L" colspan="3"><font class="label">giorni dalla notifica dell'avviso di pagamento</font></td>
<%
			if (rata.getListaBollettini() != null && rata.getListaBollettini().size() > 0) {
%>   
		<td class="C" nowrap><img src="<%=IWebConstants.IMAGES_DIR%><%=spunta%>.gif"></td>
<%
			} else {
%>
		<td class="r">&nbsp;</td>
<%
			}
			if (em.getFlagDocumentoRegistrato() == null) {
%>
		<td class="C" nowrap><a href="<%=IWebConstants.PG_MAIN %>?<%=IWebConstants.ACTION_FIELD%>=<%=actDettaglio%>&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=em.getIdEvento()%>">
			<img alt="Dettaglio" src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" border="0"><br>In Inserimento
			</a>
		</td>
<%
			} else if ("S".equals(em.getFlagDocumentoRegistrato())) {
%>
		<td class="C" nowrap><img src="<%=IWebConstants.IMAGES_DIR%>V.gif"></td>
<%
			} else if ("A".equals(em.getFlagDocumentoRegistrato())) {
%>
		<td class="C" nowrap><img src="<%=IWebConstants.IMAGES_DIR%>TickRed.gif"><br>(annullato)</td>
<%
			}
%>      
	</tr>
<%
  		} else if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE)) {
%>
	<tr>
		<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(rata.getNumeroRate(),"&nbsp;")%></font>
			<font class="label"> rate da </font>
			<font class="campo"><%=StringUtils.toEuroFormat(rata.getImportoRata())%> &euro;</font>
		</td>
<%
			if (conta == 1) {
%>
		<td class="R"><font class="label">termine di pagamento della prima rata fissato entro </font></td>
		<td class="R"><font class="campo"><%=StringUtils.toStringJSP(rata.getScadenzaGiorni(),"&nbsp;")%></font></td>
		<td class="L" colspan="3"><font class="label">giorni dalla notifica dell'avviso di pagamento </font></td>
<%
			} else {
%>
		<td class="R" colspan="5">&nbsp;</td>
<%
			}
			if (rata.getListaBollettini() != null && rata.getListaBollettini().size() > 0) {
%>   
		<td class="C" nowrap><img src="<%=IWebConstants.IMAGES_DIR%><%=spunta%>.gif"></td>
<%
			} else {
%>
		<td class="r">&nbsp;</td>
<%
			}
			if (em.getFlagDocumentoRegistrato() == null) {
%>
		<td class="C" nowrap>
			<a href="<%=IWebConstants.PG_MAIN %>?<%=IWebConstants.ACTION_FIELD%>=<%=actDettaglio%>&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=em.getIdEvento()%>">
				<img alt="Dettaglio" src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" border="0"><br>In Inserimento
			</a>
		</td>
<%
			} else if ("S".equals(em.getFlagDocumentoRegistrato())) {
%>
		<td class="C" nowrap><img src="<%=IWebConstants.IMAGES_DIR%>V.gif"></td>
<%
			} else if ("A".equals(em.getFlagDocumentoRegistrato())) {
%>
		<td class="C" nowrap><img src="<%=IWebConstants.IMAGES_DIR%>TickRed.gif"><br>(annullato)</td>
<%
			}
%>
	</tr>
<%
		}
	}
%>
	<tr>
		<td colspan="8">&nbsp;<td>
	</tr>
<%
} // End iter sugli ordini di ingiunzione

// ======================================================================
//  Rate libere
// ======================================================================
%>
<% 
if (listaRateizzazioniLibere.size() > 0) {
	String lTipoRateizzazione = listaRateizzazioniLibere.elementAt(0).getTipoRateizzazione();
    BigDecimal lImportoDaPagare = listaRateizzazioniLibere.elementAt(0).getImportoDaPagare();
%>
	<tr>
		<td colspan="8">&nbsp;<td>
	</tr>  
	<tr>
		<td colspan="8">&nbsp;<td>
	</tr>  
	<tr>
		<td class="L" colspan="8">
			<font class="label">Importo da pagare</font>
			<font class="campo"><%=StringUtils.toEuroFormat(lImportoDaPagare)%> &euro;</font>
		</td>
	</tr>
	<tr>
<%
	if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_UNICA)) {
%>
		<td class="Titolo" colspan="6"> Pagamento in una Unica Soluzione </td>
<%
	} else if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE)) {
%>
		<td class="Titolo" colspan="6"> Pagamento Rateizzato </td>
<%
	}
%>
		<td class="Titolo" colspan="1">Emessi bollettini</td>
		<td class="Titolo" colspan="1">Emesso Provvedimento</td>
	</tr>
<%
	Iterator<RateizzazionePPModel> IteRate = listaRateizzazioniLibere.iterator();
	int conta = 0;
	while (IteRate.hasNext()) {
		conta++;
		RateizzazionePPModel rata = (RateizzazionePPModel) IteRate.next();
		String spunta = "V";
		if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_UNICA)) {
%>
	<tr>
		<td class="L" nowrap><font class="label">Rata unica da</font>&nbsp;<font class="campo"><%=StringUtils.toEuroFormat(rata.getImportoRata())%> &euro;</font></td>      
		<td class="R" nowrap><font class="label">termine di pagamento fissato entro </font></td>
		<td class="R"><font class="campo"><%=StringUtils.toStringJSP(rata.getScadenzaGiorni(),"&nbsp;")%></font></td>
		<td class="L" colspan="3"><font class="label">giorni dalla notifica dell'avviso di pagamento</font></td>
<%
			if (rata.getListaBollettini() != null && rata.getListaBollettini().size() > 0) {
%>   
		<td class="C" nowrap><img src="<%=IWebConstants.IMAGES_DIR%><%=spunta%>.gif"></td>
<%
			} else {
%>
		<td class="r">&nbsp;</td>
<%
			}
			if (rata.getEveIdEvento() != null) {
%>   
		<td class="C" nowrap><img src="<%=IWebConstants.IMAGES_DIR%><%=spunta%>.gif"></td>
<%
			} else {
%>
		<td class="r">&nbsp;</td>
<%
			}
%>       
	</tr>
<%
		} else if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE)) {
%>
	<tr>
		<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(rata.getNumeroRate(),"&nbsp;")%></font>
			<font class="label"> rate da </font>
			<font class="campo"><%=StringUtils.toEuroFormat(rata.getImportoRata())%> &euro;</font>
		</td>
<%
			if (conta == 1) {
%>
		<td class="R"><font class="label">termine di pagamento della prima rata fissato entro </font></td>
		<td class="R"><font class="campo"><%=StringUtils.toStringJSP(rata.getScadenzaGiorni(),"&nbsp;")%></font></td>
		<td class="L" colspan="3"><font class="label">giorni dalla notifica dell'avviso di pagamento </font></td>
<%
			} else {
%>
		<td class="R" colspan="5">&nbsp;</td>
<%
			}
			if (rata.getListaBollettini() != null && rata.getListaBollettini().size() > 0) {
%>   
		<td class="C" nowrap><img src="<%=IWebConstants.IMAGES_DIR%><%=spunta%>.gif"></td>
<%
			} else {
%>
		<td class="r">&nbsp;</td>
<%
			}
			if (rata.getEveIdEvento() != null) {
%>   
		<td class="C" nowrap><img src="<%=IWebConstants.IMAGES_DIR%><%=spunta%>.gif"></td>
<%
			} else {
%>
		<td class="r">&nbsp;</td>
<%
			}
%>
	</tr>    
<%
		} // END if TIPO_RATEIZZAZIONE_UNICA ... else TIPO_RATEIZZAZIONE_RATEALE
	} // END while (IteRate.hasNext())
%>
</table>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td width="30%">&nbsp;</td>
		<td width="15%" class="menulines">
    		<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActLoadInserisciOrdineIngiunzione">Ordine Ingiunzione</a>
		</td>
		<td width="5%">&nbsp;</td>
		<td width="20%" class="menulines">
  			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.rateizzazionepp.action.ActLoadInserisciRideterminazionePP">Rideterminazione della Pena Pecuniaria</a>
  		</td>
  		<td width="30%">&nbsp;</td>
	</tr>
<%
} // END if (listaRateizzazioniLibere.size() > 0)
%>
</table>
</body>
</html>
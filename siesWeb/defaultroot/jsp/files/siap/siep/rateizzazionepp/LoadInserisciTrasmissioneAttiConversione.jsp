<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-33: aggiunta pagina --%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.controller.IEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.util.SICOLookupRemote"%>
<%@ page import="siap.siep.rateizzazionepp.model.RateizzazionePPModel"%>
<%@ page import="siap.siep.rateizzazionepp.action.ICostantiRateizzazionePP"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.pagoPA.model.CivilmenteObbligatoModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.istitutodetenzione.action.ICostantiIstitutoDetenzione"%>

<jsp:useBean id="listaRateizzazioni"        scope="request" class="java.util.Vector<RateizzazionePPModel>"/>
<jsp:useBean id="civilmenteObbligati"       scope="request" class="java.util.Vector<CivilmenteObbligatoModel>"/>
<jsp:useBean id="magistrato"         		scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="posizioneluogoaltra" 		scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="avvocati"           		scope="request" class="java.util.Vector<AvvocatoSiepModel>"/>
<jsp:useBean id="autoritaEsternaN"   		scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaE"   		scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaCivilObb"   scope="request" class="java.lang.String"/>
<jsp:useBean id="eventonotifica"   			scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="tipoprovvedimento" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroMinorenni" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"         			scope="request" class="java.lang.String"/>
<jsp:useBean id="annotazioneManuale"   		scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"/>
<jsp:useBean id="isImportoPagatoMinore" 	scope="request" class="java.lang.Boolean"/>
<jsp:useBean id="importoDaPagare"			scope="request" class="java.math.BigDecimal"/>
<jsp:useBean id="tipoRateizzazione"			scope="request" class="java.lang.String"/>
<jsp:useBean id="isProvvedimentoEmissibile" scope="request" class="java.lang.Boolean"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

Date dataEmissione    = DateUtils.getSysDate();
Date dataTrasmissione = DateUtils.getSysDate();
BigDecimal idEventoNotifica = new BigDecimal(0);
if (eventonotifica.getEvento().getIdEvento() != null) {
	dataEmissione    = eventonotifica.getEvento().getDataEmissione();
	dataTrasmissione = eventonotifica.getNotifiche()[0].getDataInvio();
	idEventoNotifica = eventonotifica.getEvento().getIdEvento();
}
%>
<html>
<head>
<title>[S.I.E.S.] - Gestione Provvedimento Rideterminazione Pena Pecuniaria </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>  
<script language="JavaScript">
function ListaComuni(a_formname,a_fieldname) {
	var desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio) {
	var desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,top=170,left=90,width=300,height=500");
}

function ListaMagistrati(a_formname) {
	var desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
}

function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
	var desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

function Verify() {
	var dataOdierna = "<%=DateUtils.getSysDate("dd")%>-<%=DateUtils.getSysDate("MM")%>-<%=DateUtils.getSysDate("yyyy")%>";
	// Data Emissione
  	if (document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length == 1)
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value = '0' +
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
	if (document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length == 1)
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value = '0' +
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

	var data_to_verify = document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value
		+ '/' + document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value
		+ '/' + document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

	if (!ControllaData(data_to_verify)) {
		alert('Data di emissione non valida');
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		return false;
	}
	if (!CompareDate(data_to_verify, dataOdierna)) {
		alert('La Data Emissione non può essere superiore alla data odierna');
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		return false;
	}

	// Data Trasmissione
	if (document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length == 1)
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value = '0' +
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
	if (document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length == 1)
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value = '0' +
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

	var data_to_verify = document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value
		+ '/' + document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value
		+ '/' + document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

	if (!ControllaData(data_to_verify)) {
		alert('Data di Trasmissione non valida');
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
		return false;
	}
	if (!CompareDate(data_to_verify, dataOdierna)) {
		alert('La Data di Trasmissione non può essere superiore alla data odierna');
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
		return false;
	}

	// Autorita x la Notifica
	if (typeof document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%> !== "undefined") {
  		if (document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-") {
			alert("Selezionare l'autorita' per la notifica al condannato");
			document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
			return false; 
		}
		if (document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value == "") {
			alert("Selezionare la sede dell'autorita' per la notifica al condannato");
			document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
			return false; 
  		}
	} else if (typeof document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%> !== "undefined") {
		if (document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "") {
			alert("Selezionare l'istituto di detenzione per la notifica al condannato");
			document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.focus();
			return false; 
  		}
	} else if (typeof document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%> !== "undefined") {
		if (document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "") {
			alert("Selezionare l'istituto di detenzione per la notifica al condannato");
			document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.focus();
			return false; 
    	}          
 	} else {
	    alert("destinatario sconosciuto");
	    return false;
 	}
	return true;
}

function caricaNotifiche() {
<%
if ("M".equals(modalita)) {
	NotificaModel[] nmArray = eventonotifica.getNotifiche();
	for (int i = 0; i < nmArray.length; i++) {
    	NotificaModel nm = nmArray[i];
		String codTipoAutorita = "";
		String sedeAutorita = "";
		String indirizzoAutorita = "";
		String descIstituto = "";
		String idIstituto = "";
    	if (nm.getAutoritaEsterna() != null) {
			codTipoAutorita = nm.getAutoritaEsterna().getCodTipoAutorita();
			sedeAutorita    = StringUtils.toStringJSP(nm.getAutoritaEsterna().getDescrSede(),"");
			indirizzoAutorita = StringUtils.toStringJSP(nm.getNote(),"");
      		if ("-".equals(sedeAutorita))
      			sedeAutorita = "";
    	} else if (nm.getIstDetIdIstitutoDetenzione() != null) {
	        descIstituto = nm.getIstitutoDetenzione().getDescrTipoIstituto() + " di " + nm.getIstitutoDetenzione().getDescrComune();
	        idIstituto   = nm.getIstitutoDetenzione().getIdIstitutoDetenzione();
    	}
		if (nm.getIdCivilmenteObbligato() == null && nm.getAvvIdAvvocatoFascicoloSiep() == null) {
			if (nm.getAutoritaEsterna() != null) {
%>
	$('#<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%> option[value="<%=codTipoAutorita%>"]').attr("selected", "selected");
	$('#<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>').val("<%=sedeAutorita%>");
	$('#<%=ICostantiNotifica.CAMPO_NOTE_E%>').val("<%=indirizzoAutorita%>");
<%
			} else {
%>
	$('#descIstituto').val("<%=descIstituto%>");
	$('#<%=ICostantiIstitutoDetenzione.CAMPO_ID_ISTITUTO_DETENZIONE%>').val("<%=idIstituto%>");
<%
			}
		}
		if (nm.getIdCivilmenteObbligato() != null) {
%>
	$('#<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>_CO_<%=nm.getIdCivilmenteObbligato()%> option[value="<%=codTipoAutorita%>"]').attr("selected", "selected");
	$('#<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>_CO_<%=nm.getIdCivilmenteObbligato()%>').val("<%=sedeAutorita%>");
	$('#<%=ICostantiNotifica.CAMPO_NOTE_E%>_CO_<%=nm.getIdCivilmenteObbligato()%>').val("<%=indirizzoAutorita%>");
<%
		}
		if (nm.getAvvIdAvvocatoFascicoloSiep() != null) {
%>
	$('#<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_AVV_<%=nm.getAvvIdAvvocatoFascicoloSiep()%> option[value="<%=codTipoAutorita%>"]').attr("selected", "selected");
	$('#<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>_AVV_<%=nm.getAvvIdAvvocatoFascicoloSiep()%>').val("<%=sedeAutorita%>");
	$('#<%=ICostantiNotifica.CAMPO_NOTE%>_AVV_<%=nm.getAvvIdAvvocatoFascicoloSiep()%>').val("<%=indirizzoAutorita%>");
<%
		}
	}
}
%>
}
</script>
</head>
<body class="corpo" onLoad="caricaNotifiche();">
<table>
	<tr>
		<td class="LBG">
		  	<a href="Javascript:window.print();">
		    	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
		    </a>
		</td>
		<td class="LBG">
		  	<font class="label">Funzione :</font>&nbsp;&nbsp;
<%
if ("I".equals(modalita)) {
%>
			<font class="campo">Inserimento Provvedimento Rideterminazione della Pena Pecuniaria</font>
<%
} else if ("M".equals(modalita)) {
%>
			<font class="campo">Modifica Provvedimento Rideterminazione della Pena Pecuniaria</font>
<%
}
%>
		</td>
		<td class="LBG">
			<a href="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActGrigliaOrdineIngiunzioneAltriProvvedimenti">
				<img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
			</a>      
		</td>
	</tr>
</table>
  
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
  
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="l" width="20%">Posizione Giuridica</td>
		<td class="L">
		  	<font class="campo">
<%
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
%>
				DETENUTO PER ALTRA CAUSA - <%=lAltraCausa.getDescrTipoPosGiuridica()%>
<%
} else {
%>
				<%=lPosizione.getDescrPosizioneGiuridica()%>
<%
}
%>
			</font>
			<input type="HIDDEN" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>">
		</td>
	</tr>
</table>
<FORM method="POST" name="LoadInserisciTrasmissioneAttiConversione" action="<%=IWebConstants.PG_MAIN%>">
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
      	<td class="L">
<%
if ("I".equals(modalita)) {
%>
    		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.rateizzazionepp.action.ActInserisciRideterminazionePP">
<%
} else if ("M".equals(modalita)) {
%>
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.rateizzazionepp.action.ActModificaRideterminazionePP">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>">
<%
}
%>
	        <font class="label">Importo da pagare</font>
	        <font class="campo"><%=StringUtils.toEuroFormat(importoDaPagare)%> &euro;</font>
	        <font class="label">con le seguenti modalita'</font>
      	</td>
    </tr>
</table>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
<%
if (tipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_UNICA)) {
%>
		<td class="Titolo" colspan="5"> Pagamento in una Unica Soluzione </td>
<%
} else if (tipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE)) {
%>
       	<td class="Titolo" colspan="5"> Pagamento Rateizzato </td>
<%
}
%>
	</tr>
<%
Iterator<RateizzazionePPModel> IteRate = listaRateizzazioni.iterator();
int conta = 0;
int contaLibere = 0;
String storia = "";
String descrMotivo = "";
while (IteRate.hasNext()) {
	RateizzazionePPModel rata = (RateizzazionePPModel) IteRate.next();
	storia = rata.isStoricizzato() ? " Storicizzato" : "";
  	conta++;
	if (rata.getEveIdEvento() == null)
    	contaLibere++;
	else {
		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoModel em = ie.ExRicercaEventoByKey(rata.getEveIdEvento());
		descrMotivo = em.getDescrMotivo();
	}
  	if ((tipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_UNICA)
  			|| ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_UNICA.equals(rata.getTipoRateizzazione()))
  			&& "".equals(storia)) {
%>
	<tr>
		<td class="c" nowrap><font class="label">Rata unica da</font>&nbsp;<font class="campo"><%=StringUtils.toEuroFormat(rata.getImportoRata())%> &euro;</font></td>      
		<td class="c" nowrap><font class="label">termine di pagamento fissato entro </font></td>
		<td class="c" nowrap><font class="campo"><%=StringUtils.toStringJSP(rata.getScadenzaGiorni(),"&nbsp;")%></font></td>
		<td class="L" nowrap><font class="label">giorni dalla notifica dell'avviso di pagamento</font></td>
<%
		if ((rata.getEveIdEvento() != null && "I".equals(modalita))
				|| ("M".equals(modalita) && eventonotifica.getEvento().getIdEvento().compareTo(rata.getEveIdEvento()) != 0)) {
%>
        <td class="c" nowrap><font class="label" style="color:red;">Emesso Provvedimento <%=descrMotivo%><%=storia%></font></td>
<%
		} else {
%>
		<td class="c">&nbsp;</td>
<%
		}
%>         
	</tr>
<%
	} else if ((tipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE)
    		  || ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE.equals(rata.getTipoRateizzazione()))
			&& "".equals(storia)) {
%>
	<tr>
		<td class="c" nowrap>
			<font class="campo"><%=StringUtils.toStringJSP(rata.getNumeroRate(),"&nbsp;")%></font>
			<font class="label"> rate da </font>
			<font class="campo"><%=StringUtils.toEuroFormat(rata.getImportoRata())%> &euro;</font>
		</td>      
<%
		if (conta == 1 || ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE.equals(rata.getTipoRateizzazione())) {
%>
		<td class="c" nowrap><font class="label">termine di pagamento della prima rata fissato entro </font></td>
		<td class="c" nowrap><font class="campo"><%=StringUtils.toStringJSP(rata.getScadenzaGiorni(),"&nbsp;")%></font></td>
		<td class="L" nowrap><font class="label">giorni dalla notifica dell'avviso di pagamento </font></td>
<%
		} else {
%>
		<td class="c" colspan="3">&nbsp;</td>
<%
		}
        if ((rata.getEveIdEvento() != null && "I".equals(modalita))
        		|| ("M".equals(modalita) && eventonotifica.getEvento().getIdEvento().compareTo(rata.getEveIdEvento()) != 0)) {
%>
		<td class="c" nowrap><font class="label" style="color:red;">Emesso Provvedimento <%=descrMotivo%><%=storia%></font></td>
<%
		} else {
%>
		<td class="c">&nbsp;</td>
<%
		}
%>       
	</tr>
<%
	}
    if ((rata.getEveIdEvento() == null
    		|| ("M".equals(modalita) && eventonotifica.getEvento().getIdEvento().compareTo(rata.getEveIdEvento()) == 0))
    		|| isImportoPagatoMinore) {
%>
	<tr><td><input type="HIDDEN" name="<%=ICostantiRateizzazionePP.CAMPO_EVE_ID_EVENTO%>" value="<%=rata.getIdRateizzazionePP()%>"></td></tr>
<%
	}
}
%>
</table>
<br>
<%
if ("I".equals(modalita) && contaLibere == 0 && (!isImportoPagatoMinore || !isProvvedimentoEmissibile)) {
%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
	    <td class="L" width="20%">Attenzione</td>
	    <td class="L"><font class="label">E' gia' stato emesso Provvedimento Rideterminazione Pena Pecuniaria per tutte le rate previste</font></td>
  	</tr>
</table>
<%
} else {
%>
<table cellspacing="0" cellpadding="0" width="95%">
<%
	Iterator<CivilmenteObbligatoModel> itx = civilmenteObbligati.iterator();
	while (itx.hasNext()) {
		CivilmenteObbligatoModel com = (CivilmenteObbligatoModel) itx.next();
%>
	<tr>
		<td class="l" width="20%">Civilmente Obbligato:</td>
		<td class="l">
			<font class="campo"><%=com.getCognome()%></font>&nbsp;<font class="campo"><%=com.getNome()%></font>
<%
		if ("G".equals(com.getCodPersona())) {
%>
			<font class="label"> in qualita' di Legale Rappresentante di </font>
			<font class="campo"><%=com.getDenominazione()%></font>
<%
		}
%>
		</td>
	</tr>
<%
	}
%>
</table>
<br> <%-- Dati Provvedimento di Rideterminazione Pena --%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
	  	<td class="Titolo" colspan="4">Dati Provvedimento di Rideterminazione Pena</td>
	</tr>
	<tr>
		<td class="L" width="20%" nowrap>Data Emissione Provvedimento <font class="ob">(*)</font></td>
		<td class="L">
    		<input type="text" Title="Giorno emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(annotazioneManuale.getDataIscrizioneSiep(), "dd"), "")%>" name="<%=ICostantiRateizzazionePP.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
			/
			<input type="text" Title="Mese emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(annotazioneManuale.getDataIscrizioneSiep(), "MM"), "")%>" name="<%=ICostantiRateizzazionePP.CAMPO_MESE_DATA_EMISSIONE%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
			/
			<input type="text" Title="Anno emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(annotazioneManuale.getDataIscrizioneSiep(), "yyyy"), "")%>" name="<%=ICostantiRateizzazionePP.CAMPO_ANNO_DATA_EMISSIONE%>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
		</td>
		<td class="L" width="20%">Anno / Numero provvedimento</td>
		<td class="L">
			<input type="text" Title="Anno Provvedimento" value="<%=StringUtils.toStringJSP(annotazioneManuale.getAnnoSiep())%>" name="<%=ICostantiRateizzazionePP.CAMPO_ANNO_PROVVEDIMENTO%>" size="4" maxlength="4">
			/
			<input type="text" Title="Numero Provvedimento" value="<%=StringUtils.toStringJSP(annotazioneManuale.getNumeroSiep())%>" name="<%=ICostantiRateizzazionePP.CAMPO_NUMERO_PROVVEDIMENTO%>" size="6" maxlength="6">
  		</td>
	</tr>
	<tr>
  		<td class="l">Tipo provvedimento <font class="ob">(*)</font></td>
  		<td class="l" colspan="3">
    		<select title="Tipo Provvedimento" name="<%=ICostantiRateizzazionePP.CAMPO_COD_TIPO_PROVVEDIMENTO%>">
				<%=tipoprovvedimento%>
    		</select>
  		</td>
	</tr>
	<tr>
	  	<td class="l">Autorità Emittente <font class="ob">(*)</font></td>
	  	<td class="l">
			<select title="Autorità Emittente" name="<%=ICostantiRateizzazionePP.CAMPO_COD_AUTORITA_PROVVEDIMENTO%>">
        		<%=autorita%>
			</select>
	    </td>
	    <td class="l" colspan="2">
			Sede <font class="ob">(*)</font>&nbsp;
			<input type="text" title="Sede Autorita" value="<%=StringUtils.toStringJSP(annotazioneManuale.getDescrLuogoUfficioSiep())%>" name="<%=ICostantiRateizzazionePP.CAMPO_SEDE_AUTORITA_PROVVEDIMENTO%>" maxlength="35" size="35">
			<a href="Javascript:ListaUfficiPerTipo('LoadInserisciTrasmissioneAttiConversione','<%=ICostantiRateizzazionePP.CAMPO_SEDE_AUTORITA_PROVVEDIMENTO%>',document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiRateizzazionePP.CAMPO_COD_AUTORITA_PROVVEDIMENTO%>[document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiRateizzazionePP.CAMPO_COD_AUTORITA_PROVVEDIMENTO%>.selectedIndex].value);">
				<img src="/images/filefolder.gif" border="0">
       		</a>
	  	</td>
	</tr>
</table>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="L" width="20%">Data Emissione</td>
		<td class="L">
			<input value="<%=DateUtils.getDateToString(dataEmissione, "dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getDateToString(dataEmissione, "MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getDateToString(dataEmissione, "yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
		<td class="L" width="20%">Data Trasmissione</td>
		<td class="L">
			<input value="<%=DateUtils.getDateToString(dataTrasmissione, "dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getDateToString(dataTrasmissione, "MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getDateToString(dataTrasmissione, "yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	  	</td>
	</tr>
</table>
<%
//=======================================================================
//                     Magistrato
//=======================================================================
%>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
	  	<td class="Titolo" colspan="2">Magistrato</td>
	</tr>
	<tr>
		<td class="l" width="20%">Magistrato</td>
		<td class="L">
			<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25">
			<input readonly title= "Nome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="25">
			<a href="Javascript:ListaMagistrati('LoadInserisciTrasmissioneAttiConversione');">
				<img src="/images/filefolder.gif" border=0>
			</a>
			<input type="HIDDEN" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato())%>" name="<%=ICostantiEvento.CAMPO_COD_MAGISTRATO%>">
		</td>
	</tr>
</table>
<br>
<%
//=======================================================================
//                     Notifica al condannato
//=======================================================================
%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="Titolo" colspan="4">Notifica al Condannato</td>
	</tr>
<%
	if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
  		// Se detenuto altra causa in Custodia Cautelare
  		if (posizioneluogoaltra.getAltraCausa() != null
  				&& (posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("23") // Custodia Cautelare in Regime di Arresti Domiciliari
					|| posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("78") // Custodia Cautelare per AC
					|| posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("79") // Custodia Cautelare per AC
					|| posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("80") // Custodia Cautelare per AC
					|| posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("81"))) { // Custodia Cautelare per AC
%>
	<tr>
		<td class="L" width="20%">Autorita' Destinazione <font class="ob">(*)</font></td>
    	<td class="L" colspan="3">
       		<select title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>"
					id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
        		<%=autoritaEsternaE%>
       		</select>
		</td>
    </tr>
	<tr>
      	<td class="L">Sede <font class="ob">(*)</font></td>
      	<td class="L" width="30%">
        	<input title="Sede Autorita Esterna" value="" type="text" maxlength="35" size="35"
                	name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>" 
                  	id="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>">
       		<a href="Javascript:ListaComuni('LoadInserisciTrasmissioneAttiConversione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');">
         		<img src="/images/filefolder.gif" border=0>
        	</a>
		</td>
      	<td class="L" width="20%">Indirizzo</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" id="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols="30"></textarea>
      	</td>
	</tr>
<%
		} else {
%>
	<tr>
		<td class="l" width="20%">Autorita' Destinazione <font class=ob>(*)</font></td>
<%
			if (posizioneluogoaltra != null
					&& posizioneluogoaltra.getAltraCausa() != null
					&& posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione() != null) {
%>
		<td class="l">
        	<input readonly title="Istituto" name="Comune" id="descIstituto" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione().getDescrComune())%>" size=50>
        	<input type="hidden" name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" 
					id="<%=ICostantiIstitutoDetenzione.CAMPO_ID_ISTITUTO_DETENZIONE%>"
               		value="<%=posizioneluogoaltra.getAltraCausa().getIstDetIdIstitutoDetenzione()%>">
       		<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciTrasmissioneAttiConversione','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          		<img src="/images/filefolder.gif" border=0>
          	</a>
		</td>
<%
			} else {
%>
		<td class="l">
			<input readonly title="Istituto" name="Comune" id="descIstituto" value="" size="50">
          	<input type="hidden" name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" 
					id="<%=ICostantiIstitutoDetenzione.CAMPO_ID_ISTITUTO_DETENZIONE%>" value="">
          	<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciTrasmissioneAttiConversione','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          		<img src="/images/filefolder.gif" border=0>
          	</a>
		</td>
<%
			}
%>
      	<td class="l" width="20%">Note</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols="35"></textarea>
      	</td>
	</tr>
<%
		}
	} else { // Fine IF detenuto Altra causa   
		// ALTRA_CAUSA
		// Espiazione pena per Altra Causa in Regime di Detenzione
		// Espiazione pena per Altra Causa in Misura Sicurezza Detentiva (Internato)
		// Custodia Cautelare per Altra Causa in Regime di Detenzione
		// Espiazione pena per Altra Causa in Misura di Sicurezza Applicata in Via Provvisoria
		if (lPosizione.getCodPosizioneGiuridica().equals("74") || lPosizione.getCodPosizioneGiuridica().equals("75")
				|| lPosizione.getCodPosizioneGiuridica().equals("76")
				|| lPosizione.getCodPosizioneGiuridica().equals("77")) {
%>
	<tr>
		<td class="l" width="20%">Istituto di Detenzione <font class=ob>(*)</font></td>
<%
		} else {
%>
	<tr>
     	<td class="l" width="20%">Autorita' Destinazione <font class=ob>(*)</font></td>
<%
		}
		if (lPosizione.getCodPosizioneGiuridica().equals("07") || lPosizione.getCodPosizioneGiuridica().equals("10") 
				|| lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")
				|| lPosizione.getCodPosizioneGiuridica().equals("16") || lPosizione.getCodPosizioneGiuridica().equals("20") 
				|| lPosizione.getCodPosizioneGiuridica().equals("46") || lPosizione.getCodPosizioneGiuridica().equals("47")   
				|| lPosizione.getCodPosizioneGiuridica().equals("78") || lPosizione.getCodPosizioneGiuridica().equals("79") 
				|| lPosizione.getCodPosizioneGiuridica().equals("80") || lPosizione.getCodPosizioneGiuridica().equals("81")
				|| lPosizione.getCodPosizioneGiuridica().equals("70") || lPosizione.getCodPosizioneGiuridica().equals("71")
				|| lPosizione.getCodPosizioneGiuridica().equals("72")) {
%>
		<td class="L" colspan="3">
       	<select title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>" 
				id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
        	<%=autoritaEsternaE%>
       	</select>
		</td>
	</tr>
    <tr>
      	<td class="l" width="20%">Sede <font class=ob>(*)</font></td>
      	<td class="L" width="30%">
        	<input title="Sede Autorita Esterna" value="" type="text" maxlength="35" size="35"
        			name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>" 
                 	id="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>">
        	<a href="Javascript:ListaComuni('LoadInserisciTrasmissioneAttiConversione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');">
          		<img src="/images/filefolder.gif" border=0>
       		</a>
     	</td>
      	<td class="l" width="20%">Indirizzo</td>
      	<td class="L">
			<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" id="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols="30"></textarea>
      	</td>
	</tr>
<%
		} else {
			// Detenuto    
    		if (lLuogoDetenzione != null && lLuogoDetenzione.getIstitutoDetenzione() != null) {
%>
		<td class="l">
        	<input readonly title="Istituto" name="Comune" id="descIstituto" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size=50>
        	<input type="hidden" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" 
					id="<%=ICostantiIstitutoDetenzione.CAMPO_ID_ISTITUTO_DETENZIONE%>"
               		value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>">
        	<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciTrasmissioneAttiConversione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          		<img src="/images/filefolder.gif" border=0>
          	</a>
		</td>
<!-- 	</tr> -->
<%
			} else {
%>
		<td class="l">
        	<input readonly title="Istituto" name="Comune" id="descIstituto"  value="" size="50">
        	<input type="hidden" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" 
					id="<%=ICostantiIstitutoDetenzione.CAMPO_ID_ISTITUTO_DETENZIONE%>" value="">
        	<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciTrasmissioneAttiConversione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          		<img src="/images/filefolder.gif" border=0>
          	</a>
		</td>
<!-- 	</tr> -->
<%
			}
%>
<!-- 	<tr> -->
      	<td class="l" width="20%">Note</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols=35></textarea>
      	</td>
	</tr>
    <tr><td>&nbsp;</td></tr>
<%
		}
	} // Fine ELSE detenuto Altra causa  
%>
</table>
<%
//=======================================================================
//                      Notifica al Difensore
//=======================================================================
%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
	  	<td class="Titolo" colspan="4">Notifica al Difensore</td>
	</tr>
</table>
<%
	int lIdxAvv = 0;
	int lNumAvvocati = avvocati.size();
	Iterator<AvvocatoSiepModel> lItxAvv = avvocati.iterator();
	while (lItxAvv.hasNext()) {
		AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="l" width="20%">Per Avvocato</td>
	  	<td class="L" colspan="3">
	    	<input type="hidden" name="indexAvvocati" value="<%=lIdxAvv%>">
			<font class="campo">
	  			<%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
			</font>
			&nbsp;Foro di&nbsp;
			<font class="campo">
	  			<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
			</font>
			&nbsp;Difensore di&nbsp;
			<font class="campo">
	  			<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
			</font>
			<input type="HIDDEN" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>">
	  	</td>
	</tr>        
	<tr>
	  	<td class="l">Autorita' Destinazione</td>
	  	<td class="L" colspan="3">
			<select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" 
					id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_AVV_<%=lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep()%>">
				<%=autoritaEsternaN%>
	      	</select>
		</td>
	</tr>
	<tr>
	  	<td class="l" width="20%">Sede</td>
	  	<td class="L" width="30%">
	   		<input title="Sede Foro Avvocato" type="text" maxlength="35" size="35"
				name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" 
				id="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>_AVV_<%=lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep()%>">
<%
		if (lNumAvvocati < 2) {
%>
			<a href="Javascript:ListaComuni('LoadInserisciTrasmissioneAttiConversione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
				<img src="/images/filefolder.gif" border="0">
	  		</a>
<%
		} else {
%>
			<a href="Javascript:ListaComuni('LoadInserisciTrasmissioneAttiConversione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
				<img src="/images/filefolder.gif" border="0">
	  		</a>
<%
		}
%>
		</td>
	 	<td class="l" width="20%">Note</td>
	 	<td class="L">
	    	<textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" id="<%=ICostantiNotifica.CAMPO_NOTE%>_AVV_<%=lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep()%>" cols="35"></textarea>
	   </td>
	</tr>
	<tr><td>&nbsp;</td></tr>
</table>
<%
		lIdxAvv++;
  	} // END while (lItxAvv.hasNext())
%>
<%
//=======================================================================
//                  Notifica al Civilmente Obbligato
//=======================================================================
%>
<%
	Iterator<CivilmenteObbligatoModel> itx1 = civilmenteObbligati.iterator();
	while (itx1.hasNext()) {
		CivilmenteObbligatoModel lObbligatoModel = (CivilmenteObbligatoModel) itx1.next();
%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="Titolo" colspan="4">Notifica al Civilmente Obbligato</td>
	</tr>
	<tr>
	  	<td class="L" width="20%">Civilmente Obbligato: </td>
	  	<td class="L" colspan="3">
	    	<font class="campo"><%=lObbligatoModel.getCognome()%></font>&nbsp;
			<font class="campo"><%=lObbligatoModel.getNome()%></font>&nbsp;
			<font class="label">nato a</font>&nbsp;<font class="campo"><%=lObbligatoModel.getDescComuneNascita()%></font>&nbsp;
			<font class="label">il</font>&nbsp;<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lObbligatoModel.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
<%
		if ("G".equals(lObbligatoModel.getCodPersona())) {
%>
			<font class="label"> in qualita' di Legale Rappresentante di </font>
			<font class="campo"><%=lObbligatoModel.getDenominazione()%></font>
<%
		}
%>
		</td> 
	</tr>    
	<tr>
	  	<td class="L">Autorita' Destinazione</td>
	  	<td class="L" colspan="3">
	   		<select Title="Autorita Esterna" class="small" 
					id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>"
					name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>">
	 			<%=autoritaEsternaCivilObb%>
	   		</select>
		</td>
	</tr>
	<tr>
	  	<td class="L" width="20%">Sede</td>
	  	<td class="L" width="30%">
	    	<input type="text" maxlength="35" size="35" title="Sede Autorita Esterna"
					id="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>"
					name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>">
			<a href="Javascript:ListaComuni('LoadInserisciTrasmissioneAttiConversione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>');">
				<img src="/images/filefolder.gif" border="0">
			</a>
		</td>
		<td class="L" width="20%">Indirizzo</td>
		<td class="L">
	  		<TEXTAREA title="Note" cols="30" id="<%=ICostantiNotifica.CAMPO_NOTE_E%>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>"></textarea>
		</td>
	</tr>
</table>
<%
	} // END while (itx1.hasNext())
%>
<%
//=======================================================================
//        Notifica al Difensore del Civilmente Obbligato
//=======================================================================
/*
<table cellspacing="0" cellpadding="0" width="95%">
  <tr><td class="Titolo" colspan="6">Notifica al Difensore del Civilmente Obbligato</td></tr>
</table>
*/
%>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
  	<tr>
    	<td class="lNoBord"><INPUT class="bottone" type="submit" value="Conferma"></td>
  	</tr>
</table>
<%
} // end ContaLibere != 0
%>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadInserisciTrasmissioneAttiConversione");  
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

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

<jsp:useBean id="listaRateizzazioni"        scope="request" class="java.util.Vector"/>
<jsp:useBean id="civilmenteObbligati"       scope="request" class="java.util.Vector"/>
<jsp:useBean id="magistrato"         		scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="posizioneluogoaltra" 		scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="avvocati"           		scope="request" class="java.util.Vector"/>
<jsp:useBean id="autoritaEsternaN"   		scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaE"   		scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaCivilObb"   scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"         			scope="request" class="java.lang.String"/>
<jsp:useBean id="eventonotifica"   			scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

Date dataEmissione    = DateUtils.getSysDate();
Date dataTrasmissione = DateUtils.getSysDate();

if (eventonotifica.getEvento().getIdEvento() != null) {
	dataEmissione    = eventonotifica.getEvento().getDataEmissione();
	dataTrasmissione = eventonotifica.getNotifiche()[0].getDataInvio();
}
%>
<html>
<head>
<title>[S.I.E.S.] - Gestione evento </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>  
<script language="JavaScript">
function ListaComuni(a_formname,a_fieldname) {
	var desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function ListaMagistrati(a_formname) {
	var desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
}

function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
	var desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

function Verify() {
	var dataOdierna = "<%=DateUtils.getSysDate("dd")%>-<%=DateUtils.getSysDate("MM")%>-<%=DateUtils.getSysDate("yyyy")%>";
	// Aggiungere i controlli
  	if (document.LoadInserisciOrdineIngiunzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
		document.LoadInserisciOrdineIngiunzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineIngiunzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
	if (document.LoadInserisciOrdineIngiunzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
		document.LoadInserisciOrdineIngiunzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineIngiunzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

	var data_to_verify = document.LoadInserisciOrdineIngiunzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value
		+'/'+ document.LoadInserisciOrdineIngiunzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value
		+'/'+ document.LoadInserisciOrdineIngiunzione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

	if (!ControllaData(data_to_verify)) {
		alert('Data di emissione non valida');
		document.LoadInserisciOrdineIngiunzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		return false;
	}
	if (!CompareDate(data_to_verify, dataOdierna)) {
		alert('La Data Emissione non può essere superiore alla data odierna');
		document.LoadInserisciOrdineIngiunzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		return false;
	}

	// Data Trasmissione
	if (document.LoadInserisciOrdineIngiunzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
		document.LoadInserisciOrdineIngiunzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineIngiunzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
	if (document.LoadInserisciOrdineIngiunzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
		document.LoadInserisciOrdineIngiunzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineIngiunzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

	var data_to_verify = document.LoadInserisciOrdineIngiunzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value
		+'/'+ document.LoadInserisciOrdineIngiunzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value
		+'/'+ document.LoadInserisciOrdineIngiunzione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

	if (!ControllaData(data_to_verify)) {
		alert('Data di Trasmissione non valida');
		document.LoadInserisciOrdineIngiunzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
		return false;
	}
	if (!CompareDate(data_to_verify, dataOdierna)) {
		alert('La Data di Trasmissione non può essere superiore alla data odierna');
		document.LoadInserisciOrdineIngiunzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
		return false;
	}

	// Autorita x la Notifica
	if (typeof document.LoadInserisciOrdineIngiunzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%> !== "undefined") {
  		if (document.LoadInserisciOrdineIngiunzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value=="-") {
			alert("Selezionare l'autorita' per la notifica al condannato");
			document.LoadInserisciOrdineIngiunzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
			return false; 
		}
		if (document.LoadInserisciOrdineIngiunzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value == "") {
			alert("Selezionare la sede dell'autorita' per la notifica al condannato");
			document.LoadInserisciOrdineIngiunzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
			return false; 
  		}
	} else if (typeof document.LoadInserisciOrdineIngiunzione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%> !== "undefined") {
		if (document.LoadInserisciOrdineIngiunzione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "") {
			alert("Selezionare l'istituto di detenzione per la notifica al condannato");
			document.LoadInserisciOrdineIngiunzione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.focus();
			return false; 
  		}
	} else if (typeof document.LoadInserisciOrdineIngiunzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %> !== "undefined") {
		if (document.LoadInserisciOrdineIngiunzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "") {
			alert("Selezionare l'istituto di detenzione per la notifica al condannato");
			document.LoadInserisciOrdineIngiunzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.focus();
			return false; 
    	}          
 	} else {
	    alert("destinatario sconosciuto");
	    return false;
 	}
	return true;
}

function caricaNotifiche () {
<%
if ("M".equals(modalita)) {
	NotificaModel[] lNotifiche = eventonotifica.getNotifiche();
	for (int i = 0; i < lNotifiche.length; i++) {
    	NotificaModel lNotifica = lNotifiche[i];
		String codTipoAutorita = "";
		String sedeAutorita = "";
		String indirizzoAutorita = "";
		String descIstituto = "";
		String idIstituto = "";
    	if (lNotifica.getAutoritaEsterna() != null) {
			codTipoAutorita = lNotifica.getAutoritaEsterna().getCodTipoAutorita();
			sedeAutorita    = StringUtils.toStringJSP(lNotifica.getAutoritaEsterna().getDescrSede(),"");
			indirizzoAutorita = StringUtils.toStringJSP(lNotifica.getNote(),"");
      		if ("-".equals(sedeAutorita))
      			sedeAutorita = "";
    	} else if (lNotifica.getIstDetIdIstitutoDetenzione() != null) {
	        descIstituto = lNotifica.getIstitutoDetenzione().getDescrTipoIstituto()+" di "+lNotifica.getIstitutoDetenzione().getDescrComune();
	        idIstituto   = lNotifica.getIstitutoDetenzione().getIdIstitutoDetenzione();
    	}
		if (lNotifica.getIdCivilmenteObbligato() == null && lNotifica.getAvvIdAvvocatoFascicoloSiep() == null) {
			if (lNotifica.getAutoritaEsterna() != null) {
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
		if (lNotifica.getIdCivilmenteObbligato() != null) {
%>
	$('#<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>_CO_<%=lNotifica.getIdCivilmenteObbligato()%> option[value="<%=codTipoAutorita%>"]').attr("selected", "selected");
	$('#<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>_CO_<%=lNotifica.getIdCivilmenteObbligato()%>').val("<%=sedeAutorita%>");
	$('#<%=ICostantiNotifica.CAMPO_NOTE_E%>_CO_<%=lNotifica.getIdCivilmenteObbligato()%>').val("<%=indirizzoAutorita%>");
<%
		}
		if (lNotifica.getAvvIdAvvocatoFascicoloSiep() != null) {
%>
	$('#<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_AVV_<%=lNotifica.getAvvIdAvvocatoFascicoloSiep()%> option[value="<%=codTipoAutorita%>"]').attr("selected", "selected");
	$('#<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>_AVV_<%=lNotifica.getAvvIdAvvocatoFascicoloSiep()%>').val("<%=sedeAutorita%>");
	$('#<%=ICostantiNotifica.CAMPO_NOTE%>_AVV_<%=lNotifica.getAvvIdAvvocatoFascicoloSiep()%>').val("<%=indirizzoAutorita%>");
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
			<font class="campo">Ordine di Ingiunzione al Pagamento Pena Pecuniaria</font>
<%
} else if ("M".equals(modalita)) {
%>
			<font class="campo">Modifica Ordine di Ingiunzione al Pagamento Pena Pecuniaria</font>
<%
}
%>
		</td>
			<td class="LBG">
			  <a href="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActGrigliaOrdineIngiunzioneAltriProvvedimenti">
			    <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>      
			</td> 		
	</tr>
</table>
  
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
  
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="l" width="20%">Posizione Giuridica</td>
		<td class="L" colspan=5>
		  	<font class="campo">
<%
if(lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
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
			<input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
		</td>
	</tr>
</table>
<%
// Sezione con l'importo da pagare a la rateizzazione
// MEV_2023-33: aggiunto controllo per storicizzazione evento OIP
Iterator iterLR = listaRateizzazioni.iterator();
String lTipoRateizzazione = "";
BigDecimal lImportoDaPagare = null;
while (iterLR.hasNext()) {
	RateizzazionePPModel rata = (RateizzazionePPModel) iterLR.next();
	// RateizzazionePPModel primarata = (RateizzazionePPModel) listaRateizzazioni.elementAt(0);
	if (!rata.isStoricizzato()) {
		lTipoRateizzazione = rata.getTipoRateizzazione();
		lImportoDaPagare = rata.getImportoDaPagare();
		break;
	}
}
%>
<br>
<FORM method="POST" name="LoadInserisciOrdineIngiunzione" action="<%= IWebConstants.PG_MAIN%>">
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td>
<%
if ("I".equals(modalita)) {
%>
    		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActInserisciOrdineIngiunzione">
<%
} else if ("M".equals(modalita)) {
%>
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActModificaOrdineIngiunzione">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
<%
}
%>
		</td>
	</tr>
</table>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
      	<td class="L">
	        <font class="label">Importo da pagare</font>
	        <font class="campo"><%=StringUtils.toEuroFormat(lImportoDaPagare)%> &euro;</font>
	        <font class="label">con le seguenti modalita'</font>
      	</td>
    </tr>
</table>
<table cellspacing="0" cellpadding="0" width="95%">
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
	</tr>
<%
Iterator IteRate = listaRateizzazioni.iterator();
int conta = 0;
int contaLibere = 0;
String storia = "";
while (IteRate.hasNext()) {
	RateizzazionePPModel rata = (RateizzazionePPModel) IteRate.next();
	storia = rata.isStoricizzato() ? " Storicizzato" : "";
  	conta++;
	if (rata.getEveIdEvento() == null)
    	contaLibere++;
  	if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_UNICA)
  			|| ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_UNICA.equals(rata.getTipoRateizzazione())) {
%>
	<tr>
		<td class="c" width="20%" nowrap>
			<font class="label">Rata unica da</font>&nbsp;<font class="campo"><%=StringUtils.toEuroFormat(rata.getImportoRata())%> &euro;</font>
		</td>      
		<td class="c" nowrap><font class="label">termine di pagamento fissato entro </font></td>
		<td class="c" nowrap><font class="campo"><%=StringUtils.toStringJSP(rata.getScadenzaGiorni(),"&nbsp;")%></font></td>
		<td class="c" nowrap><font class="label">giorni dalla notifica dell'avviso di pagamento</font></td>
<%
		if ((rata.getEveIdEvento() != null && "I".equals(modalita))
				|| ("M".equals(modalita) && eventonotifica.getEvento().getIdEvento().compareTo(rata.getEveIdEvento()) != 0)) {
%> 
        <td class="c" nowrap><font class="label" style="color:red;">Emesso ordine di ingiunzione<%=storia%></font></td>
<%
		} else {
%>
		<td class="c">&nbsp;</td>
<%
		}
%>         
	</tr>
<%
	} else if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE)
    		  || ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE.equals(rata.getTipoRateizzazione())) {
%>
	<tr>
		<td class="c" width="20%" nowrap>
			<font class="campo"><%=StringUtils.toStringJSP(rata.getNumeroRate(),"&nbsp;")%></font>
			<font class="label"> rate da </font>
			<font class="campo"><%=StringUtils.toEuroFormat(rata.getImportoRata())%> &euro;</font>
		</td>      
<%
		if (conta == 1 || ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE.equals(rata.getTipoRateizzazione())) {
%>
		<td class="c" nowrap><font class="label">termine di pagamento della prima rata fissato entro </font></td>
		<td class="c" nowrap><font class="campo"><%=StringUtils.toStringJSP(rata.getScadenzaGiorni(),"&nbsp;")%></font></td>
		<td class="c" nowrap><font class="label">giorni dalla notifica dell'avviso di pagamento </font></td>
<%
		} else {
%>
		<td class="c" colspan="3">&nbsp;</td>
<%
		}
        if ((rata.getEveIdEvento() != null && "I".equals(modalita))
        		|| ("M".equals(modalita) && eventonotifica.getEvento().getIdEvento().compareTo(rata.getEveIdEvento()) != 0)) {
%>
		<td class="c" nowrap><font class="label" style="color:red;">Emesso ordine di ingiunzione<%=storia%></font></td>
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
    if (rata.getEveIdEvento() == null
    		|| ("M".equals(modalita) && eventonotifica.getEvento().getIdEvento().compareTo(rata.getEveIdEvento()) == 0)) {
%>      
	<tr><td><input type="HIDDEN" name="<%=ICostantiRateizzazionePP.CAMPO_EVE_ID_EVENTO%>" value="<%=rata.getIdRateizzazionePP()%>"></td></tr>
<%
	}
}
%>
</table>
<br>
<%
if ("I".equals(modalita) && contaLibere == 0) {
%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
	    <td class="L">Attenzione</td>
	    <td class="L"><font class="label">E' gia' stato emesso Ordine di Ingiunzione per tutte le rate previste</font></td>
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
<br>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="L" width="20%">Data Emissione</td>
		<td class="L" colspan="2" >
			<input value="<%=DateUtils.getDateToString(dataEmissione, "dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getDateToString(dataEmissione, "MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getDateToString(dataEmissione, "yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
		</td>
		<td class="L">Data Trasmissione</td>
		<td class="L" colspan="2">
			<input value="<%=DateUtils.getDateToString(dataTrasmissione, "dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > -
			<input value="<%=DateUtils.getDateToString(dataTrasmissione, "MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getDateToString(dataTrasmissione, "yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
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
	  	<td class="Titolo" colspan="3"> Magistrato </td>
	</tr>
	<tr>
		<td class="l" width="20%">Magistrato</td>
		<td class="L">
			<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome())%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25">
			<input readonly title= "Nome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome())%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="25">
			<a href="Javascript:ListaMagistrati('LoadInserisciOrdineIngiunzione');">
				<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
		<td>
			<input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
		</td>
	</tr>
</table>

<%
//=======================================================================
//                     Notifica al condannato
//=======================================================================
%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="Titolo" colspan="8">Notifica al Condannato</td>
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
       		<select title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>" id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
        		<%=autoritaEsternaE%>
       		</select>
		</td>
    </tr>
    <tr>
      	<td class="L">Sede <font class="ob">(*)</font></td>
      	<td class="L">
        	<input title="Sede Autorita Esterna" value="" type="text" maxlength="35" size="35" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>" id="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>">
        		<a href="Javascript:ListaComuni('LoadInserisciOrdineIngiunzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
          			<img src="/images/filefolder.gif" border=0>
        		</a>
      	</td>
      	<td class="L">Indirizzo</td>
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
	if (posizioneluogoaltra != null && posizioneluogoaltra.getAltraCausa() != null
			&& posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione() != null) {
%>
		<td class="l">
       		<input readonly Title="Istituto" name="Comune" id="descIstituto" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione().getDescrComune())%>" size=50>
        	<input type="hidden"  Title="Istituto" name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" id="<%=ICostantiIstitutoDetenzione.CAMPO_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getAltraCausa().getIstDetIdIstitutoDetenzione()%>" size="50">
       		<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineIngiunzione','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          		<img src="/images/filefolder.gif" border=0>
          	</a>
		</td>
<%
	} else {
%>
		<td class="l">
          	<input readonly Title="Istituto" name="Comune" id="descIstituto" value="" size="50">
          	<input type="hidden"  Title="Istituto" name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" id="<%=ICostantiIstitutoDetenzione.CAMPO_ID_ISTITUTO_DETENZIONE%>" value="">
          	<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineIngiunzione','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          		<img src="/images/filefolder.gif" border=0>
          	</a>
		</td>
<%
	}
%>
		<td class="l">Note</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols="35"></textarea>
      	</td>
   </tr>
<%
	}
// Fine detenuto Altra causa
} else {
%>  
	<tr>
<%
	if (lPosizione.getCodPosizioneGiuridica().equals("74") || lPosizione.getCodPosizioneGiuridica().equals("75")
			|| lPosizione.getCodPosizioneGiuridica().equals("76") || lPosizione.getCodPosizioneGiuridica().equals("77")) {
      // ALTRA_CAUSA
      // Espiazione pena per Altra Causa in Regime di Detenzione
      // Espiazione pena per Altra Causa in Misura Sicurezza Detentiva (Internato)
      // Custodia Cautelare per Altra Causa in Regime di Detenzione
      // Espiazione pena per Altra Causa in Misura di Sicurezza Applicata in Via Provvisoria
%>
		<td class="l" width="20%">Istituto di Detenzione <font class=ob>(*)</font></td>
<%
	} else {
%>
     	<td class="l" width="20%">Autorita' Destinazione <font class=ob>(*)</font></td>
<%
	}


if ("L".equals(lPosizione.getCodMascheraCG()) || "EA".equals(lPosizione.getCodMascheraCG()))
{   
    /*
	if (lPosizione.getCodPosizioneGiuridica().equals("07") || lPosizione.getCodPosizioneGiuridica().equals("10")
			|| lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")
      		|| lPosizione.getCodPosizioneGiuridica().equals("16") || lPosizione.getCodPosizioneGiuridica().equals("20") 
      		|| lPosizione.getCodPosizioneGiuridica().equals("46") || lPosizione.getCodPosizioneGiuridica().equals("47")   
			|| lPosizione.getCodPosizioneGiuridica().equals("78") || lPosizione.getCodPosizioneGiuridica().equals("79") 
			|| lPosizione.getCodPosizioneGiuridica().equals("80") || lPosizione.getCodPosizioneGiuridica().equals("81")
			|| lPosizione.getCodPosizioneGiuridica().equals("70") || lPosizione.getCodPosizioneGiuridica().equals("71")
			|| lPosizione.getCodPosizioneGiuridica().equals("50")
			|| lPosizione.getCodPosizioneGiuridica().equals("72")) {
	  */	
%>
		<td class="L" colspan="3">
	       	<select title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>" id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
	        	<%=autoritaEsternaE%>
	       	</select>
		</td>
	</tr>
	<tr>
      	<td class="l">Sede <font class=ob>(*)</font></td>
      	<td class="L">
        	<input title="Sede Autorita Esterna" value="" type="text" maxlength="35" size="35" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>" id="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>">
       		<a href="Javascript:ListaComuni('LoadInserisciOrdineIngiunzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
          		<img src="/images/filefolder.gif" border=0>
        	</a>
      	</td>
      	<td class="l">Indirizzo</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" id="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols="30"></textarea>
      	</td>
<%
	} else {
    	// Detenuto    
		if (lLuogoDetenzione != null && lLuogoDetenzione.getIstitutoDetenzione() != null) {
%>
		<td class="l">
        	<input readonly Title="Istituto" name="Comune" id="descIstituto"  value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size=50>
        	<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" id="<%=ICostantiIstitutoDetenzione.CAMPO_ID_ISTITUTO_DETENZIONE%>" value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>">
        	<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineIngiunzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          		<img src="/images/filefolder.gif" border=0>
          	</a>
    	</td>
<%
		} else {
%>
	    <td class="l">
        	<input readonly Title="Istituto" name="Comune" id="descIstituto"  value="" size="50">
        	<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" id="<%=ICostantiIstitutoDetenzione.CAMPO_ID_ISTITUTO_DETENZIONE%>" value="">
        	<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineIngiunzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          		<img src="/images/filefolder.gif" border=0>
          	</a>
    	</td>
<%
		}
%>
	</tr>
	<tr>
      	<td class="l">Note</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols=35></textarea>
      	</td>
    </tr>
    <tr><td>&nbsp;</td></tr>
<%
	}
}
%>
</table>

<%
//=======================================================================
//                      Notifica al Difensore
//=======================================================================
%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
    	<td class="Titolo" colspan="6">Notifica al Difensore</td>
  	</tr>
</table>
<%
int lIdxAvv = 0;
int lNumAvvocati = avvocati.size();
Iterator lItxAvv = avvocati.iterator();
while (lItxAvv.hasNext()) {
	AvvocatoSiepModel lAvv =  (AvvocatoSiepModel) lItxAvv.next();
%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="l" width="20%">Per Avvocato </td>
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
            <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
		</td>
	</tr>
    <tr>
      	<td class="l">Autorita' Destinazione </td >
      	<td class="L" colspan="3">
         	<select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_AVV_<%=lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep()%>">
           		<%=autoritaEsternaN%>
           	</select>
		</td>
	</tr>
	<tr>
		<td class="l">Sede </td>
		<td class="L">
            <input title="Sede Foro Avvocato" type="text" maxlength="35" size="35" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" 
                   id="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>_AVV_<%=lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep()%>">
            <% if( lNumAvvocati < 2 ) { %>
			<a href="Javascript:ListaComuni('LoadInserisciOrdineIngiunzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>');">
				<img src="/images/filefolder.gif" border="0">
			</a>
            <% } else {%>
             <a href="Javascript:ListaComuni('LoadInserisciOrdineIngiunzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
				<img src="/images/filefolder.gif" border="0">
			</a>
            <% } %>
		</td>
		<td class="l">Note</td>
		<td class="L">
			<textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols="35" id="<%=ICostantiNotifica.CAMPO_NOTE%>_AVV_<%=lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep()%>"></textarea>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
</table>
<%
	lIdxAvv++;
}
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
  <tr><td class="Titolo" colspan="4">Notifica al Civilmente Obbligato </td></tr>
  <tr>
    <td class="L" width="20%">Civilmente Obbligato: </td>
    <td class="L" colspan="3">
      <font class="campo"><%=lObbligatoModel.getCognome()%></font>&nbsp;
      <font class="campo"><%=lObbligatoModel.getNome()%></font>&nbsp;
      <font class="label">nato a</font>&nbsp;<font class="campo"><%=lObbligatoModel.getDescComuneNascita()%></font>&nbsp;
      <font class="label">il</font>&nbsp;<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lObbligatoModel.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
      <% if ("G".equals(lObbligatoModel.getCodPersona())) { %>
      <font class="label"> in qualita' di Legale Rappresentante di </font>
      <font class="campo"><%=lObbligatoModel.getDenominazione()%></font>
      <% } %>    
    </td> 
  </tr>    
  
  <tr>
    <td class="L">Autorita' Destinazione</td>
    <td class="L" colspan="3">
     <select Title="Autorita Esterna" class="small" 
             id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>"
             name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>"
     >
      <%=autoritaEsternaCivilObb%>
     </select>
    </td>
  </tr>
  
  <tr>
    <td class="L">Sede</td>
    <td class="L">
      <input type="text" maxlength="35" size="35" title="Sede Autorita Esterna"
             id="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>"
             name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>"  >
      <a href="Javascript:ListaComuni('LoadInserisciOrdineIngiunzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>');">
        <img src="/images/filefolder.gif" border="0">
      </a>
    </td>
    <td class="L">Indirizzo</td>
    <td class="L">
      <TEXTAREA title="Note" cols="30" id="<%=ICostantiNotifica.CAMPO_NOTE_E%>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>"></textarea>
    </td>
  </tr>  
</table>  
<%
	}
%>
  
<%
//=======================================================================
//        Notifica al Difensore del Civilmente Obbligato
//=======================================================================
/*
<table width="95%">
  <tr><td class="Titolo" colspan="6">Notifica al Difensore del Civilmente Obbligato </td></tr>
</table>
*/
%>

<table cellspacing="0" cellpadding="0" width="95%">
  	<tr>
    	<td class="lNoBord" colspan="2">
      		<INPUT class="bottone" type="submit" value="Conferma">
    	</td>
  	</tr>
</table>  
<%
} // end ContaLibere !0
%>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadInserisciOrdineIngiunzione");  
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
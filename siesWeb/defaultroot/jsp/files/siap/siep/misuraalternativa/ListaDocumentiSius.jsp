<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaEventoModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>

<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>

<jsp:useBean id="documentiSius" scope="request" class="java.util.ArrayList"/>
<%-- MEV_9-SIEP --%>
<jsp:useBean id="TipoMA"   		scope="request" class="java.lang.String"/>
<jsp:useBean id="NaturaMA" 		scope="request" class="java.lang.String"/>
<%-- FINE MEV_9-SIEP --%>

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<title>[S.I.E.S.] - Decisioni Sorveglianza</title>

<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript">
function controlla() {
<%
boolean esistonoDati = false;
if (!documentiSius.isEmpty())
	esistonoDati = true;
%>
	if (<%=!esistonoDati%>) {
		alert("Nessun dato presente");
        window.parent.close();
	}
}

function insertIT(
		id,
  		annoSius,
		numeroSius,
		annoOrdinanza,
		numeroOrdinanza,
		tipoAutorita,
		sedeAutorita,
		oggetto,
		natura,
		giorno,
		mese,
		anno,
		luogoProva,
		giornoInizioMisura,
		meseInizioMisura,
		annoInizioMisura,
		giornoInizioRevocaMisura,
		meseInizioRevocaMisura,
		annoInizioRevocaMisura,
		anniRevocaReclusione,
		mesiRevocaReclusione,
		giorniRevocaReclusione,
		anniRevocaArresto,
		mesiRevocaArresto,
		giorniRevocaArresto,
		note,
		tipodecisione,
		flagScarcerato,
		giornoScarcerazione,
		meseScarcerazione,
		annoScarcerazione,                         
		giornoFineMisura,
		meseFineMisura,
		annoFineMisura,
		giorniMisura,
		mesiMisura,
		anniMisura,
		flagDecisioneTribunale,
		sedeTdsCompetente,
		<%-- MEV_9 (x3) --%>
		giornoEsecutivita, 
		meseEsecutivita,
		annoEsecutivita,
		<%-- MEV_9-SIEP (x7) --%>
		giornoDecisione, 
		meseDecisione,
		annoDecisione,
		annoOrdinanzaProvvisoria,
		numeroOrdinanzaProvvisoria,
		idOrdinanzaProvvisoria,
		isOrdinanzaProvvisoria) {
	if (annoSius != '-')
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>.value = annoSius;
	else
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>.value = "";
	if (numeroSius != '-')
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>.value = numeroSius;
	else
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>.value = "";

	if (annoOrdinanza != '-')
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>.value = annoOrdinanza;
	else
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>.value = "";

	if (numeroOrdinanza != '-')
 		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>.value = numeroOrdinanza;
	else
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>.value = "";

	if (tipoAutorita != '-')
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value = tipoAutorita;
	else
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value = "";

	try {
		// non utilizzabile
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.onchange();
	} catch (err) { }

	if (sedeAutorita != '-')
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>.value = sedeAutorita;
	else
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>.value = "";

	if (oggetto != '-')
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value = oggetto;
	else
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value = "";

	if (giorno != '-')
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value = giorno;
	else
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value = "";

	if (mese != '-' )
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value = mese;
	else
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value = "";

	if (anno != '-')
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.value = anno;
	else
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.value = "";

	try {
  		if (luogoProva != '-')
    		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA%>.value = luogoProva;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA%>.value = "";
	} catch (err) { }

	try {
 		if (tipodecisione != '-')
   			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>.value = tipodecisione;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>.value = "";
	} catch (err) { }

	// MEV_9 si aggiunge la data esecutivita
	try {
    	if (giornoEsecutivita != '-')
      		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_ESECUTIVITA%>.value = giornoEsecutivita;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_ESECUTIVITA%>.value = "";

		if (meseEsecutivita != '-' )
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_ESECUTIVITA%>.value = meseEsecutivita;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_ESECUTIVITA%>.value = "";

		if (annoEsecutivita != '-')
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_ESECUTIVITA%>.value = annoEsecutivita;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_ESECUTIVITA%>.value = "";
	} catch (err) { }

	// MEV_9-SIEP: aggiunti campi in estrazione
	try {
    	if (giornoDecisione != '-')
      		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.value = giornoDecisione;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.value = "";

		if (meseDecisione != '-' )
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>.value = meseDecisione;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>.value = "";

		if (annoDecisione != '-')
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>.value = annoDecisione;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>.value = "";

		if (annoOrdinanzaProvvisoria != '-')
	  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT%>.value = annoOrdinanzaProvvisoria;
		else
	  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT%>.value = "";

		if (numeroOrdinanzaProvvisoria != '-')
	 		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT%>.value = numeroOrdinanzaProvvisoria;
		else
	  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT%>.value = "";

  		if (idOrdinanzaProvvisoria != '-')
	 		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_EVE_ID_EVENTO%>.value = idOrdinanzaProvvisoria;
		else
	  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_EVE_ID_EVENTO%>.value = "";

	  	if (isOrdinanzaProvvisoria != 'false') {
<%-- 	  		window.parent.opener.document.<%=request.getParameter("formname")%>.getElementById('divOrdinanzaProvvisoria').style.visibility = 'visible'; --%>
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT%>.disabled = false;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT%>.disabled = false;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.disabled = false;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>.disabled = false;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>.disabled = false;
	  	} else {
<%--   			window.parent.opener.document.<%=request.getParameter("formname")%>.getElementById('divOrdinanzaProvvisoria').style.visibility = 'hidden'; --%>
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT%>.disabled = true;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT%>.disabled = true;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.disabled = true;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>.disabled = true;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>.disabled = true;
	  	}
	} catch (err) { }
	// FINE MEV_9-SIEP

	// PP = PROSECUZIONE_PROVVISORIA
	// PC = PROSECUZIONE_PROVVISORIA_CUMULO
	if (natura != '-' && (natura == 'PP' || natura == 'PC' || natura == 'ED' || natura == 'EC')) {
  		try {
    		if (giornoInizioMisura != '-')
      			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value = giornoInizioMisura;
			else
  				window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value = "";

			if (meseInizioMisura != '-' )
				window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value = meseInizioMisura;
			else
				window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value = "";

			if (annoInizioMisura != '-')
				window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value = annoInizioMisura;
			else
				window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value = "";
		} catch (err) { }
        
//         if (tipodecisione != '-')
<%--            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>.value = tipodecisione; --%>
//         else
<%--            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>.value = ""; --%>
	}

	//==================
	// RE = REVOCA
	//==================
	if (natura != '-' && natura == 'RE' && (oggetto == '0086' || oggetto == '0014' || oggetto == '0015' // AFFIDAMENTO IN PROVA
			|| oggetto == '0196')) { // INDULTINO (L.207/2003 )
 		if (giornoInizioRevocaMisura != '-')
    		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>.value = giornoInizioRevocaMisura;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>.value = "";

		if (meseInizioRevocaMisura != '-' )
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>.value = meseInizioRevocaMisura;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>.value = "";

		if (annoInizioRevocaMisura != '-')
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA%>.value = annoInizioRevocaMisura;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA%>.value = "";

		if (anniRevocaReclusione != '-')
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE%>.value = anniRevocaReclusione;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE%>.value = "";

		if (mesiRevocaReclusione != '-')
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE%>.value = mesiRevocaReclusione;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE%>.value = "";

		if (giorniRevocaReclusione != '-')
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE%>.value = giorniRevocaReclusione;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE%>.value = "";

		if (anniRevocaArresto != '-')
 			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO%>.value = anniRevocaArresto;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO%>.value = "";

		if (mesiRevocaArresto != '-')
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO%>.value = mesiRevocaArresto;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO%>.value = "";

		if (giorniRevocaArresto != '-')
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO%>.value = giorniRevocaArresto;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO%>.value = "";
	}

	if (note != '-')
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NOTE%>.value = note;
	else
  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NOTE%>.value = "";

	// CO = concessione SOSPENSIONE ESECUZIONE PENA
	if (natura != '-' && natura == 'CO' && (oggetto == '2000' || oggetto == '2001' || oggetto == '2480')) {
		if (tipodecisione != '-')
		  	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>.value = tipodecisione;
		else
		  	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>.value = "";
	}

	// new Ammissione Provvisoria Affidamento in Prova a seguito del DL 146 2013 prevede sia Ordinanza che Decreto (prima prevedeva solo il Decreto)
	if (natura != '-' && natura == 'CO'&& (oggetto == '2006' || oggetto == '2008' )) {
 		if (tipodecisione != '-')
   			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>.value = tipodecisione;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>.value = "";
	}

	//===============================================================
	// CO = concessione DIFFERIMENTO PROVVISORIO/DEFINITIVO -- oggetto 2140 --> concessione espulsione
	//===============================================================

	if (natura != '-' && natura == 'CO'
			&& (oggetto == '2010' || oggetto == '2011' || oggetto == '0030' || oggetto == '0031' || oggetto == '0032'
					|| oggetto == '0033' || oggetto == '0201' || oggetto == '0202' || oggetto == '2140'
					<%-- MEV_9-SIEP: aggiunti nuovi oggetti ed aggiunto try..catch x AFFIDAMENTO --%>
					|| oggetto == '0720' || oggetto == '0721' || oggetto == '0730' || oggetto == '0731' || oggetto == '0732')) {
		try {
	  		// Quantum di differimento
	  		if (anniMisura != '-')
	    		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>.value = anniMisura;
			else
	  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>.value = "";
	
			if (mesiMisura != '-')
			  	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>.value = mesiMisura;
			else
			  	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>.value = "";
	
			if (giorniMisura != '-')
			  	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>.value = giorniMisura;
			else
			 	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>.value = "";
		} catch (err) { }

	 	if (oggetto != '2140') {
  			if (giornoInizioMisura != '-')
    			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value = giornoInizioMisura;
			else
			 	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value = "";

			if (meseInizioMisura != '-' )
			  	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value = meseInizioMisura;
			else
			  	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value = "";

			if (annoInizioMisura != '-')
			  	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value = annoInizioMisura;
			else
			  	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value = "";

		  	// Da scarcerare/già scarcerato
			try {
				if (flagScarcerato=='SORV') {
			     	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_UFFICIO_SCARCERAZIONE%>[1].checked = true;
					window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_UFFICIO_SCARCERAZIONE%>[0].checked = false;
				} else if (flagScarcerato=='PROC') {
			  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_UFFICIO_SCARCERAZIONE%>[0].checked = true;
					window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_UFFICIO_SCARCERAZIONE%>[1].checked = false;
			   	}
			} catch (err) { }

			try {
				// Data fine differimento
				if (giornoFineMisura != "" && giornoFineMisura != "-") {
					window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.value = giornoFineMisura;
					window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>.value = meseFineMisura;
					window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>.value = annoFineMisura;
				} else {
					window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.value = "";
					window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>.value = "";
					window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>.value = "";
				}
			} catch (err) { }

			// Fino alla decisione del TDS
			try {
			  	if (flagDecisioneTribunale=='S') {
			    	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE%>.checked = true;
				} else {
			  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE%>.checked = false;
			  	}
			} catch (err) { }

			// TDS Competente
			try {
			  	if (sedeTdsCompetente != '-')
			   		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TDS_COMPETENTE%>.value = sedeTdsCompetente;
				else
			  		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TDS_COMPETENTE%>.value = "";
		    } catch (err) { }
		} // end if (oggetto != '2140')
	} // end if differimento

	//======================================================================
	// Prosecuzione della Misura in corso disposta dal TDS (1204) o MDS (2286) 
	// nel caso di Differimento nelle forme della Detenzione domiciliare 
	//======================================================================
	if (oggetto == '1204') {
  		if (flagScarcerato == 'SORV') {
			window.parent.opener.document.<%=request.getParameter("formname")%>.tipo[1].checked = true;
			window.parent.opener.document.<%=request.getParameter("formname")%>.tipo[0].checked = false;
			try {
			  	window.parent.opener.document.<%=request.getParameter("formname")%>.tipo[1].onclick();
			} catch (err) { }
			if (giornoScarcerazione != '-') {
				window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value = giornoScarcerazione;
				window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value = meseScarcerazione;
				window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value = annoScarcerazione;
	  		}
		} else if (flagScarcerato=='PROC') {
			window.parent.opener.document.<%=request.getParameter("formname")%>.tipo[0].checked = true;
			window.parent.opener.document.<%=request.getParameter("formname")%>.tipo[1].checked = false;
			try {
			  	window.parent.opener.document.<%=request.getParameter("formname")%>.tipo[0].onclick();
		    } catch (err) { }            
		}
	}

	if (oggetto == '1204' || oggetto == '2286') {
  		if (anniMisura != '-')
    		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>.value = anniMisura;
		else
  			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>.value = "";

		if (mesiMisura != '-')
		  	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>.value = mesiMisura;
		else
		  	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>.value = "";
		
		if (giorniMisura != '-')
		  	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>.value = giorniMisura;
		else
		  	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>.value = "";

		//== Data fine misura ==
		if (giornoFineMisura != "" && giornoFineMisura != "-") {
		  	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.value = giornoFineMisura;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>.value = meseFineMisura;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>.value = annoFineMisura;
		} else {
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.value = "";
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>.value = "";
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>.value = "";
		}
	}
	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>.value=id;
	window.parent.close();
}
</script>
</head>

<body class="corpo" onload="controlla();">
<form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
<table>
	<tr>
      	<td class="LBG">
        	<a href="Javascript:window.print();">
          		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        	</a>
      	</td>
      	<td class="LBG">
        	<font class="label">Funzione :</font>
        	<font class="campo">Decisioni Sorveglianza
<%
if (!documentiSius.isEmpty()) {
	MisuraAlternativaEventoModel lPrimaMisura = (MisuraAlternativaEventoModel) documentiSius.get(0);
	if (lPrimaMisura.getMisuraAlternativa() != null && lPrimaMisura.getMisuraAlternativa().getCodTipoMisura() != null
			&& (lPrimaMisura.getMisuraAlternativa().getCodTipoMisura().equals("2000")
					|| lPrimaMisura.getMisuraAlternativa().getCodTipoMisura().equals("2001")
					|| lPrimaMisura.getMisuraAlternativa().getCodTipoMisura().equals("2480"))) {
%>
				- Sospensione Esecuzione Pena
<%
	} else if (lPrimaMisura.getMisuraAlternativa() != null
			&& lPrimaMisura.getMisuraAlternativa().getCodTipoMisura() != null
            && lPrimaMisura.getMisuraAlternativa().getCodTipoMisura().equals("2140")) {
%>
              	- Decreti di Espulsione
<%
	} else if (lPrimaMisura.getMisuraAlternativa() != null
			&& lPrimaMisura.getMisuraAlternativa().getCodTipoMisura() != null
            && lPrimaMisura.getMisuraAlternativa().getCodTipoMisura().equals("0029")) {
%>
              	- <%=lPrimaMisura.getMisuraAlternativa().getDescrNaturaDecisione()%> 
               	Opposizione su Espulsione
<%
	} else if (lPrimaMisura.getMisuraAlternativa() != null
			&& lPrimaMisura.getMisuraAlternativa().getDescrNaturaDecisione() != null) {
%>
              	- <%=lPrimaMisura.getMisuraAlternativa().getDescrNaturaDecisione()%>
              	Misure Alternative
<%
	}
}
%>
        	</font>
		</td>
	</tr>
</table>
<br>
<table>
    <tr>
		<td class="int">Anno/Numero Sius</td>
		<td class="int">Autorità Emittente</td>
		<td class="int">Oggetto</td>
		<td class="int">Anno/Numero Ordinanza Provvisoria</td>	<%-- MEV_9-SIEP: aggiunta colonna --%>
		<td class="int" width=5%>Azioni</td>
    </tr>
<%
if (!documentiSius.isEmpty()) {
	Iterator<?> itx = documentiSius.iterator();
	while (itx.hasNext()) {
		MisuraAlternativaEventoModel misuraEventoModel = (MisuraAlternativaEventoModel) itx.next();
		MisuraAlternativaModel misuraModel = misuraEventoModel.getMisuraAlternativa();
		EventoModel eventoModel = misuraEventoModel.getEvento();
        // String lUfficio = StringUtils.toStringJSP(eventoModel.getDescrUfficioEmittente(), "-");
        // if (lUfficio.toUpperCase().startsWith("TRIB")) {
        //	lUfficio = "TDS";
        // } else if (lUfficio.toUpperCase().startsWith("UFF")) {
        //  lUfficio = "UDS";
        // }
        // MEV 10 S3
        // Il codice precedente ribalta in modo non corretto l'Autorità Emittente,
        // poichè non individua i codici TDSM e UDSM
        String lUfficio = StringUtils.toStringJSP(eventoModel.getCodTipoUfficioEmittente(), "-");
        // MEV_9-SIEP: aggiunto controllo per escludere dalla visualizzazione
        if (!"AMMISSIONE_PROVVISORIA".equals(NaturaMA)) {
			if (("TDS".equals(lUfficio)
					&& ("0680".equals(misuraModel.getCodTipoMisura())					// AFFIDAMENTO
							|| "0681".equals(misuraModel.getCodTipoMisura())			// AFFIDAMENTO
							|| "0682".equals(misuraModel.getCodTipoMisura())			// DETENZIONE DOMICILIARE
							|| "0683".equals(misuraModel.getCodTipoMisura())			// SEMILIBERTA'
							|| "0684".equals(misuraModel.getCodTipoMisura())))			// SOSPENSIONE
					|| ("TDSM".equals(lUfficio)
							&& ("0690".equals(misuraModel.getCodTipoMisura())			// AFFIDAMENTO
									|| "0691".equals(misuraModel.getCodTipoMisura())	// AFFIDAMENTO
									|| "0692".equals(misuraModel.getCodTipoMisura())	// AFFIDAMENTO
									|| "0693".equals(misuraModel.getCodTipoMisura())	// DETENZIONE DOMICILIARE
									|| "0694".equals(misuraModel.getCodTipoMisura())	// SEMILIBERTA'
									|| "0695".equals(misuraModel.getCodTipoMisura())))	// SOSPENSIONE
					&& !"0270".equals(eventoModel.getCodEsito()))
				continue;
		}

        // MEV_9-SIEP per le sospe 678 provvisorie prendo solo quelle con esito 0270 Applica provvisoriamente 
        if ("CONCESSIONE_SOSPENSIONE678".equals(NaturaMA)) {  
        	if (!"0270".equals(eventoModel.getCodEsito()))
        		continue;
        }
        
        
%>
	<tr>
		<td class="c">
			<%=StringUtils.toStringJSP(misuraModel.getChiaveAnnoFascicoloSius(), "-")%>
			/
			<%=StringUtils.toStringJSP(misuraModel.getChiaveProgrFascicoloSius(), "-")%>
		</td>
		<td class="l">
	  		<%=StringUtils.toStringJSP(eventoModel.getDescrUfficioEmittente(), "-")%> di <%=StringUtils.toStringJSP(eventoModel.getDescrLuogoEmittente(), "-")%>
			<br>del&nbsp;
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataEmissione(), "dd-MM-yyyy"), "-")%>
<%
		if (misuraModel != null && "P".equalsIgnoreCase(misuraModel.getFlagUfficioInserimento())) {
%>
        	<br>(<font class="cRosso">-- iscritto da SIEP --</font>)
<%
      	}
%>
		</td>
		<td class="l">
<%
		if (misuraModel != null  && misuraModel.getCodTipoMisura() != null
				&& (misuraModel.getCodTipoMisura().equals("2000")
						|| misuraModel.getCodTipoMisura().equals("2001")
						|| misuraModel.getCodTipoMisura().equals("2480")
						// Ammissione Provvisoria Affidamento in prova (x2)
                  		|| misuraModel.getCodTipoMisura().equals("2006")
                  		|| misuraModel.getCodTipoMisura().equals("2008"))) {
%>
			<%=StringUtils.toStringJSP(misuraModel.getDescrTipoDecisione(), "-")%>
<%
		}
		// MEV_9-SIEP: aggiunto controllo x varie tipologie
		if ("0680".equals(misuraModel.getCodTipoMisura())			// AFFIDAMENTO
				|| "0681".equals(misuraModel.getCodTipoMisura())	// AFFIDAMENTO
				|| "0682".equals(misuraModel.getCodTipoMisura())	// DETENZIONE DOMICILIARE
				|| "0683".equals(misuraModel.getCodTipoMisura())	// SEMILIBERTA'
				|| "0684".equals(misuraModel.getCodTipoMisura())	// SOSPENSIONE
				|| "0690".equals(misuraModel.getCodTipoMisura())	// AFFIDAMENTO
				|| "0691".equals(misuraModel.getCodTipoMisura())	// AFFIDAMENTO
				|| "0692".equals(misuraModel.getCodTipoMisura())	// AFFIDAMENTO
				|| "0693".equals(misuraModel.getCodTipoMisura())	// DETENZIONE DOMICILIARE
				|| "0694".equals(misuraModel.getCodTipoMisura())	// SEMILIBERTA'
				|| "0695".equals(misuraModel.getCodTipoMisura())) {	// SOSPENSIONE
%>
			Applicazione Provvisoria 
<%
		}
%>
			<%=StringUtils.toStringJSP(eventoModel.getDescrMotivo(), "-")%>
		</td>
<%
		// MEV_9-SIEP: aggiunti campi in estrazione
		boolean isOrdinanzaProvvisoria = false;
		String descrLuogoProva = misuraModel.getDescrLuogoProva();
		// "0271".equals(eventoModel.getCodEsito()) && // 'CO' - Conferma Decisione del Magistrato Relatore
		if ("0720".equals(misuraModel.getCodTipoMisura())			// AFFIDAMENTO
				|| "0721".equals(misuraModel.getCodTipoMisura())	// AFFIDAMENTO
				|| "0722".equals(misuraModel.getCodTipoMisura())	// DETENZIONE DOMICILIARE
				|| "0723".equals(misuraModel.getCodTipoMisura())	// SEMILIBERTA'
				|| "0724".equals(misuraModel.getCodTipoMisura())	// SOSPENSIONE
				|| "0680".equals(misuraModel.getCodTipoMisura())	// AFFIDAMENTO
				|| "0681".equals(misuraModel.getCodTipoMisura())	// AFFIDAMENTO
				|| "0682".equals(misuraModel.getCodTipoMisura())	// DETENZIONE DOMICILIARE
				|| "0683".equals(misuraModel.getCodTipoMisura())	// SEMILIBERTA'
				|| "0684".equals(misuraModel.getCodTipoMisura())	// SOSPENSIONE
				|| "0730".equals(misuraModel.getCodTipoMisura())	// AFFIDAMENTO
				|| "0731".equals(misuraModel.getCodTipoMisura())	// AFFIDAMENTO
				|| "0732".equals(misuraModel.getCodTipoMisura())	// AFFIDAMENTO
				|| "0733".equals(misuraModel.getCodTipoMisura())	// DETENZIONE DOMICILIARE
				|| "0734".equals(misuraModel.getCodTipoMisura())	// SEMILIBERTA'
				|| "0735".equals(misuraModel.getCodTipoMisura())	// SOSPENSIONE
				|| "0690".equals(misuraModel.getCodTipoMisura())	// AFFIDAMENTO
				|| "0691".equals(misuraModel.getCodTipoMisura())	// AFFIDAMENTO
				|| "0692".equals(misuraModel.getCodTipoMisura())	// AFFIDAMENTO
				|| "0693".equals(misuraModel.getCodTipoMisura())	// DETENZIONE DOMICILIARE
				|| "0694".equals(misuraModel.getCodTipoMisura())	// SEMILIBERTA'
				|| "0695".equals(misuraModel.getCodTipoMisura())) {	// SOSPENSIONE
			isOrdinanzaProvvisoria = true;
			Date dataOrdinanzaProvvisoria = null;
			BigDecimal annoOrdinanzaProvvisoria = null;
			BigDecimal numeroOrdinanzaProvvisoria = null;
			BigDecimal idFascicoloSius = eventoModel.getFasSiuIdFascicoloSius();
			BigDecimal idOrdinanzaProvvisoria = null;
			Iterator<?> iter = documentiSius.iterator();
			while (iter.hasNext()) {
				MisuraAlternativaEventoModel maem = (MisuraAlternativaEventoModel) iter.next();
				MisuraAlternativaModel mam = maem.getMisuraAlternativa();
				EventoModel em = maem.getEvento();
				if (!Utils.isNullObj(em.getFasSiuIdFascicoloSius())
						&& !Utils.isNullObj(idFascicoloSius)
						&& idFascicoloSius.compareTo(em.getFasSiuIdFascicoloSius()) == 0
						&& ("0680".equals(em.getCodMotivo())			// AFFIDAMENTO
								|| "0681".equals(em.getCodMotivo())		// AFFIDAMENTO
								|| "0682".equals(em.getCodMotivo())		// DETENZIONE DOMICILIARE
								|| "0683".equals(em.getCodMotivo())		// SEMILIBERTA'
								|| "0684".equals(em.getCodMotivo())		// SOSPENSIONE
								|| "0690".equals(em.getCodMotivo())		// AFFIDAMENTO
								|| "0691".equals(em.getCodMotivo())		// AFFIDAMENTO
								|| "0692".equals(em.getCodMotivo())		// AFFIDAMENTO
								|| "0693".equals(em.getCodMotivo())		// DETENZIONE DOMICILIARE
								|| "0694".equals(em.getCodMotivo())		// SEMILIBERTA'
								|| "0695".equals(em.getCodMotivo()))	// SOSPENSIONE
						&& "0270".equals(em.getCodEsito())) {
					dataOrdinanzaProvvisoria = mam.getDataDecisione();
					annoOrdinanzaProvvisoria = mam.getAnnoRegistro();
					numeroOrdinanzaProvvisoria = mam.getNumeroRegistro();
					idOrdinanzaProvvisoria = mam.getEveIdEvento();
					descrLuogoProva = mam.getDescrLuogoProva();
					misuraModel.setDataInizioMisura(mam.getDataInizioMisura());
					break;
				}
			}
%>
		<td class="c">
			<%=StringUtils.toStringJSP(annoOrdinanzaProvvisoria, "-")%>
			/
			<%=StringUtils.toStringJSP(numeroOrdinanzaProvvisoria, "-")%>
		</td>
		<td class="c">
			<a href="Javascript:insertIT('<%=StringUtils.toStringJSP(eventoModel.getIdEvento(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getChiaveAnnoFascicoloSius(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getChiaveProgrFascicoloSius(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getAnnoRegistro(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumeroRegistro(), "-")%>',
                                         '<%=StringUtils.toStringJSP(lUfficio)%>',
                                         '<%=StringUtils.cStrForJS(eventoModel.getDescrLuogoEmittente())%>',
                                         '<%=StringUtils.toStringJSP(eventoModel.getCodMotivo(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getCodNaturaDecisione(), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataEmissione(), "dd"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataEmissione(), "MM"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataEmissione(), "yyyy"), "-")%>',
                                         '<%=StringUtils.cStrForJS(descrLuogoProva)%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioMisura(), "dd"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioMisura(), "MM"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioMisura(), "yyyy"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioRevoca(), "dd"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioRevoca(), "MM"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioRevoca(), "yyyy"), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumAnniRevocaReclusione(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumMesiRevocaReclusione(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumGiorniRevocaReclusione(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumAnniRevocaArresto(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumMesiRevocaArresto(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumGiorniRevocaArresto(), "-")%>',
                                         '<%=StringUtils.cStrForJS(misuraModel.getNote())%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getCodTipoDecisione(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getCodTipoUfficioScarcerazione(), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataScarcerazione(), "dd"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataScarcerazione(), "MM"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataScarcerazione(), "yyyy"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataFineMisura(), "dd"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataFineMisura(), "MM"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataFineMisura(), "yyyy"), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumGiorniMisura(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumMesiMisura(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumAnniMisura(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getFlagDecisioneTribunale(), "-")%>',
                                         '<%=StringUtils.cStrForJS(misuraModel.getDescSedeTdsCompetente())%>',
                                         <%-- MEV_9 --%>
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataEsecutivita(), "dd"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataEsecutivita(), "MM"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataEsecutivita(), "yyyy"), "-")%>',
                                         <%-- MEV_9-SIEP: aggiunti campi in estrazione: ID + anno/numero ordinanza provvisoria + data emissione provvedimento --%>
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataOrdinanzaProvvisoria, "dd"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataOrdinanzaProvvisoria, "MM"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataOrdinanzaProvvisoria, "yyyy"), "-")%>',
                                         '<%=StringUtils.toStringJSP(annoOrdinanzaProvvisoria, "-")%>',
                                         '<%=StringUtils.toStringJSP(numeroOrdinanzaProvvisoria, "-")%>',
                                         '<%=StringUtils.toStringJSP(idOrdinanzaProvvisoria, "-")%>',
                                         '<%=StringUtils.toStringJSP(isOrdinanzaProvvisoria, "-")%>'
                                         );">
				<img align="middle" src="/images/fileselected.gif" border=0>
			</a>
<%
		} else {
%>
		<td class="c">-</td>
		<td class="c">
			<a href="Javascript:insertIT('<%=StringUtils.toStringJSP(eventoModel.getIdEvento(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getChiaveAnnoFascicoloSius(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getChiaveProgrFascicoloSius(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getAnnoRegistro(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumeroRegistro(), "-")%>',
                                         '<%=StringUtils.toStringJSP(lUfficio)%>',
                                         '<%=StringUtils.cStrForJS(eventoModel.getDescrLuogoEmittente())%>',
                                         '<%=StringUtils.toStringJSP(eventoModel.getCodMotivo(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getCodNaturaDecisione(), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataEmissione(), "dd"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataEmissione(), "MM"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataEmissione(), "yyyy"), "-")%>',
                                         '<%=StringUtils.cStrForJS(descrLuogoProva)%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioMisura(), "dd"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioMisura(), "MM"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioMisura(), "yyyy"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioRevoca(), "dd"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioRevoca(), "MM"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioRevoca(), "yyyy"), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumAnniRevocaReclusione(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumMesiRevocaReclusione(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumGiorniRevocaReclusione(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumAnniRevocaArresto(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumMesiRevocaArresto(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumGiorniRevocaArresto(), "-")%>',
                                         '<%=StringUtils.cStrForJS(misuraModel.getNote())%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getCodTipoDecisione(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getCodTipoUfficioScarcerazione(), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataScarcerazione(), "dd"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataScarcerazione(), "MM"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataScarcerazione(), "yyyy"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataFineMisura(), "dd"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataFineMisura(), "MM"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataFineMisura(), "yyyy"), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumGiorniMisura(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumMesiMisura(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumAnniMisura(), "-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getFlagDecisioneTribunale(), "-")%>',
                                         '<%=StringUtils.cStrForJS(misuraModel.getDescSedeTdsCompetente())%>',
                                         <%-- MEV_9 --%>
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataEsecutivita(), "dd"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataEsecutivita(), "MM"), "-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataEsecutivita(), "yyyy"), "-")%>',
                                         <%-- MEV_9-SIEP: aggiunti campi in estrazione: ID + anno/numero ordinanza provvisoria + data emissione provvedimento --%>
                                         '',
                                         '',
                                         '',
                                         '',
                                         '',
                                         '',
                                         '<%=StringUtils.toStringJSP(isOrdinanzaProvvisoria, "-")%>'
                                         );">
				<img align="middle" src="/images/fileselected.gif" border=0>
			</a>
<%
		}
%>
		</td>
	</tr>
<%
	}
}
%>
</table>
</form>
</body>
</html>
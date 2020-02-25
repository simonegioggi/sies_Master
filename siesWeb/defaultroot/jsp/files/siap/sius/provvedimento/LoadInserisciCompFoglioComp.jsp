<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.security.model.FunctionModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.model.FunzioneModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>
<%@ page import="siap.sius.documentoallegato.action.ICostantiDocumentoAllegato"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="documentoAllegato" scope="request"
	class="siap.sius.documentoallegato.model.DocumentoAllegatoModel" />
<jsp:useBean id="depositoDecreto" scope="request"
	class="siap.sius.depositodecreto.model.DepositoDecretoModel" />
<jsp:useBean id="depositoordinanzapc" scope="request"
	class="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel" />
<jsp:useBean id="evento" scope="request"
	class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="UtenteConnesso" scope="session"
	class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="motivoNonInvio" scope="request"
	class="java.lang.String" />
<jsp:useBean id="fascicoloSiusGP" scope="session"
	class="siap.sius.fascicolo.model.FascicoloGPModel" />
<%-- MEV10-s3: aggiunto useBean per gestire la sentenza per i minori --%>
<jsp:useBean id="depositoSentenza" scope="request"
	class="siap.sius.depositosentenza.model.DepositoSentenzaModel" />

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<html>
<head>
<title>[S.I.E.S.] - Compilazione Foglio Complementare</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<%
	String lAzione = new String();
	if (modalita.equalsIgnoreCase("M"))
		lAzione = "siap.sius.provvedimento.action.ActModificaCompFoglioComp";
	else
		lAzione = "siap.sius.provvedimento.action.ActInserisciCompFoglioComp";
%>

<script language="JavaScript">
  	// variabile utilizzata per poter abilitare o disabilitare le icone
  	// di trasmissione, modifica o cancellazione del foglio complementare da SIC
  	var abilitaImgTrasmissioneFC = true;
  
	function openPopup(url) {
		if(abilitaImgTrasmissioneFC){
		  	newwindow=window.open(url,'name','height=570,width=920,top=200,left=200,location=0,menubar=0,status=0,resizable=0,scrollbars=1');
		  	if (window.focus) {newwindow.focus()}
		} else {
			alert("Operazione non consentita");
		}
  	}

	function openPopupAnnulla(url) {
		newwindow=window.open(url,'name','height=570,width=920,top=200,left=200,location=0,menubar=0,status=0,resizable=0,scrollbars=1');
		if (window.focus) {newwindow.focus()}
	}
	  
 	function AbilitaIconeTrasmissione() {
	  	// Disabilita i pulsanti Trasmissione/Modifica/Cancellazione del Foglio Complementare 
	  	// alla selezione della motivazione non invio "Iscritto Manualmente su NSC".
	  	if (document.LoadInserisciCompFoglioComp.<%=ICostantiDocumentoAllegato.CAMPO_FLAG_MOTIVO_NON_INVIO%>.value=='01'){
		  	abilitaImgTrasmissioneFC = false;
	  	} else {
		  	abilitaImgTrasmissioneFC = true;
	  	}
  	}

	function Verify() {
		if (document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value.length==1)
       		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value='0'+document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value;

    	if (document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_TRASMISSIONE%>.value.length==1)
        	document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_TRASMISSIONE%>.value='0'+document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_TRASMISSIONE%>.value;

   		// Controllo validità data Emissione.
   		var dataCompilTrasm=document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value +'/'+
        		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_TRASMISSIONE%>.value +'/'+
           		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_TRASMISSIONE%>.value;

    	if (! ControllaData(dataCompilTrasm)) {
      		alert('Data Compilazione/Trasmissione non valida!');
     		return false;
    	}

    	// Controllo validità data Inserimento Manuale.
    	var dataInsmanuale=document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>.value +'/'+
        		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>.value +'/'+
                document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>.value;
    	if (! ControllaDataPassaVuota(dataInsmanuale)) {
      		alert('Data Inserimento Manuale non valida!');
      		return false;
    	}
    	return true;
  	}

	function AbilitaCampi() {
		// Disabilita i pulsanti Trasmissione/Modifica/Cancellazione del Foglio Complementare 
		// alla selezione della motivazione non invio "Iscritto Manualmente su NSC".
		if (document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_COD_MOTIVO_NON_INVIO%>.value=='01'){
			abilitaImgTrasmissioneFC = false;
		} else {
			abilitaImgTrasmissioneFC = true;
		}
		  
	  	// Modifica del 30/07/2014
	  	// Ho commentato il codice seguente poichè anche se il foglio complementare è
	 	// stato trasmesso (con errore) si da all'utente la possibilità di compilare
	  	// le motivazioni del non avvenuto invio
	  	// se il Foglio Complementare è stato inviato i campi "Motivazione non Inviato",
	 	// "Descrizione" e "Data Inserimento Manuale" non sono editabili
	  	//var dataTrasmissione = document.LoadInserisciCompFoglioComp.<//%=ICostantiDocumentoAllegato.CAMPO_FLAG_FOGLIO_TRASMESSO%>.value;
	  	//if (dataTrasmissione.length >5)
	  	//{  
	  	//	  document.LoadInserisciCompFoglioComp.<//%=ICostantiProvvedimento.CAMPO_COD_MOTIVO_NON_INVIO%>.disabled = true;
  	  	//	  document.LoadInserisciCompFoglioComp.<//%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>.disabled = true;
	  	//      document.LoadInserisciCompFoglioComp.<//%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>.disabled = true;
	  	//	  document.LoadInserisciCompFoglioComp.<//%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>.disabled = true;
	  	//	  document.LoadInserisciCompFoglioComp.<//%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>.disabled = true;
      	//}		  
		  
		if (document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_COD_MOTIVO_NON_INVIO%>.value=='-') {
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>.disabled = true;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>.disabled = true;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>.disabled = true;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>.disabled = true;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>.value = "";
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>.value = "";
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>.value = "";
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>.value = "";
	  	   //ISCRITTO MANUALMENTE DA NSC
		 } else if (document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_COD_MOTIVO_NON_INVIO%>.value=='01') {
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>.disabled = false;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>.disabled = false;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>.disabled = false;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>.disabled = false;
	  	} else {
	  	    //ALTRO
	  		//document.LoadInserisciCompFoglioComp.%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>.value = "";
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>.value = "";
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>.value = "";
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>.value = "";
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>.disabled = false;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>.disabled = true;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>.disabled = true;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>.disabled = true;
	  	}
  	}

	function VerifyConferma() {
		// Controllo obbligatorietà Data Inserimento Manuale oppure Descrizione
        // se viene selezionato dalla lista "Inserito Manualmente su NSC".
	    if (document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_COD_MOTIVO_NON_INVIO%>.value == '01'){
			var dataInserimentoManuale=document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>.value +'/'+
		    		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>.value +'/'+
		            document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>.value;
		    var descriz = document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>.value;
		    if( dataInserimentoManuale != null && dataInserimentoManuale.length<3 && descriz == ""){
	          	alert ("Valorizzare Descrizione oppure Data Inserimento Manuale");
	          	document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>.focus();
	          	return false;
		    }
	    }

        // Controllo obbligatorietà Descrizione se viene selezionato dalla lista "Altro".
	    if (document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_COD_MOTIVO_NON_INVIO%>.value == '02'){
            var descrizione = document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>.value;
            if (descrizione == "") {
	          	alert ("Descrizione è un campo obbligatorio");
	          	document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>.focus();
	          	return false;
	        }
	    }
    }
  	</script>
</head>

<body class="corpo">
	<link rel="STYLESHEET" type="text/css" href="/css/style.css">
	<table>
		<tr>
			<td class="LBG"><a href="Javascript:window.print();">
				<img align="middle"
					src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
					alt="Stampa questa videata" border=0></a></td>
			<td class="LBG"><font class="label">Funzione :</font>&nbsp; <font
				class="campo"><%=modalita.equalsIgnoreCase("M") ? "Modifica " : "Compilazione "%>Foglio Complementare</font></td>

			<td class="LBG"><a
				href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sius.provvedimento.action.ActStampaFoglioComp&<%=ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO%>=<%=documentoAllegato.getIdDocumentoAllegato()%>')">
					<img id="generaStampa" align="middle" src="/images/print24.gif"
					alt="Generazione Stampa" width="24" height="24" border="0">
			</a></td>
			<%
				java.math.BigDecimal idEventoInterconnessione;
				if (depositoordinanzapc != null
						&& depositoordinanzapc.getIdDepositoOrdinanzaPc() != null
						&& depositoordinanzapc.getIdEventoGenerato() != null)
					idEventoInterconnessione = depositoordinanzapc.getIdEventoGenerato();
				// MEV10-s3: aggiunto controllo per la sentenza per i minori
				else if (depositoSentenza != null
						&& depositoSentenza.getIdDepositoSentenza() != null
						&& depositoSentenza.getIdEventoGenerato() != null)
					idEventoInterconnessione = depositoSentenza.getIdEventoGenerato();
				else
					idEventoInterconnessione = depositoDecreto.getIdEventoGenerato();

				String idUtente = UtenteConnesso.getUserId();
			%>

			<%-- MEV10-s3: l'invio del FC al sic non è possibile per le sentenze dei minori --%>
			<% 
			String codTipoUfficio = UtenteConnesso.getUfficioUtente().getCodTipoUfficio();
			if (!("TDSM".equalsIgnoreCase(codTipoUfficio) || "UDSM".equalsIgnoreCase(codTipoUfficio))) { %>
				<td class="LBG"><a href="#"
					onClick="openPopup('/siesEsecuzione/index.jsp?action=INSERT&idEvento=<%=idEventoInterconnessione%>&idUtente=<%=idUtente%>');return(false);">
						<img id="TrasmissioneFC" align="middle"
						src="/images/insertWS.png"
						alt="Trasmissione Foglio Complementare al SIC" width="24"
						height="24" border="0">
				</a></td>
				<td class="LBG"><a href="#"
					onClick="openPopup('/siesEsecuzione/index.jsp?action=UPDATE&idEvento=<%=idEventoInterconnessione%>&idUtente=<%=idUtente%>');return(false);">
						<img id="ModificaFC" align="middle" src="/images/updateWS.png"
						alt="Modifica Foglio Complementare sul SIC" width="24" height="24"
						border="0">
				</a></td>
				<td class="LBG"><a href="#"
					onClick="openPopup('/siesEsecuzione/search?idEvento=<%=idEventoInterconnessione%>&idUtente=<%=idUtente%>');return(false);">
						<img id="Storico" align="middle" src="/images/certificatoWS.png"
						alt="Storico invio trasmissioni al SIC" width="24" height="24"
						border="0">
				</a></td>
				<td class="LBG"><a href="#"
					onClick="openPopupAnnulla('/siesEsecuzione/index.jsp?action=DELETE&idEvento=<%=idEventoInterconnessione%>&idUtente=<%=idUtente%>&tipoOperazione=ANNULLA');return(false);">
						<img id="Annulla" align="middle" src="/images/delete24.gif"
						alt="Annulla" width="24" height="24" border="0">
				</a></td>
			<% } %>

			<!-- BOTTONE DI RITORNO -->
			<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>" />
		</tr>
	</table>
	<br>
	<%
		if (fascicoloSiusGP != null) {
	%>
	<table>
		<tr>
			<jsp:include
				page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>" />
		</tr>
	</table>
	<%
		} // endif fascicoloSiusGP
	%>
	<br>
	<!-- LISTA DEI FOGLI ANNULLATI -->
	<jsp:include page="<%=ICostantiProvvedimento.PG_LISTA_CFC_ANNULLATI%>">
		<jsp:param name="ActionLink" value="siap.sius.provvedimento.action.ActLoadDettaglioCompFoglioComp" />
	</jsp:include>

	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"
		name="LoadInserisciCompFoglioComp">
		<table cellspacing=2 cellpadding=2>
			<tr>
				<td class="Titolo" colspan=6>Estremi Provvedimento</td>
			</tr>

			<%
				if (depositoDecreto != null	&& depositoDecreto.getIdDepositoDecreto() != null) {
			%>
			<tr>
				<td class="l">DECRETO n° <font class="campo"><%=StringUtils.toStringJSP(depositoDecreto.getAnnoS72())%></font>
					<font class="l">/</font> <font class="campo"><%=StringUtils.toStringJSP(depositoDecreto.getNumS72())%></font>
					<font class="l">del</font> <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
						depositoDecreto.getDataEmissione(), "dd-MM-yyyy"))%></font>
			</tr>
			<%
				}
			%>

			<%
				if (depositoordinanzapc != null && depositoordinanzapc.getIdDepositoOrdinanzaPc() != null) {
			%>
			<tr>
				<td class="l">ORDINANZA n° <font class="campo"><%=StringUtils.toStringJSP(depositoordinanzapc
						.getAnnoS3())%></font>
					<font class="l">/</font> <font class="campo"><%=StringUtils.toStringJSP(depositoordinanzapc
						.getNumS3())%></font>
					<font class="l">del</font> <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
						depositoordinanzapc.getDataDeposito(), "dd-MM-yyyy"))%></font>
			</tr>
			<%
				}
				// MEV10-s3: aggiunto codice per gestire la sentenza per i minori
				if (depositoSentenza != null && depositoSentenza.getIdDepositoSentenza() != null) {
			%>
			<tr>
				<td class="l" colspan="2">SENTENZA n° <font class="campo"><%=depositoSentenza.getAnnoSentenza()%></font>
					<font class="l">/</font> <font class="campo"><%=depositoSentenza.getNumSentenza()%></font>
					<font class="l">del</font> <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(depositoSentenza.getDataDeposito(), "dd-MM-yyyy"))%></font>
				</td>
				<td class="campo"><a
					href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActLoadDocumento&amp;<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=depositoSentenza.getIdEventoGenerato()%>')">
						<img src="/images/print.gif" alt="Stampa Sentenza" width="12"
						height="12" border="0">
				</a></td>
			</tr>
			<%
				}
			%>

			<tr>
				<td class="l">Anno/Numero F.C.
				<td class="L"><font class="campo"><%=StringUtils.toStringJSP(documentoAllegato.getAnnoFoglioComplementare())%></font>
					<font class="l">/</font> <font class="campo"><%=StringUtils.toStringJSP(documentoAllegato.getProgrFoglioComplementare())%></font>
			</tr>

			<tr>
				<td class="l">Data Compilazione/Trasmissione <font class=ob>(*)</font></td>
				<td class="L">
					<%
						// se il FC non è stato trasmesso visualizzo la data odierna
						if (documentoAllegato.getDataTrasmissione() == null) {
					%> <input
					value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DateUtils.getSysDate(), "dd"))%>"
					type="text" size="2" maxlength="2"
					name="<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_TRASMISSIONE%>"
					onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> / <input
					value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DateUtils.getSysDate(), "MM"))%>"
					type="text" size="2" maxlength="2"
					name="<%=ICostantiProvvedimento.CAMPO_MESE_DATA_TRASMISSIONE%>"
					onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> / <input
					value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DateUtils.getSysDate(), "yyyy"))%>"
					type="text" size="4" maxlength="4"
					name="<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_TRASMISSIONE%>"
					onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)"> <%
 	// se il FC è stato trasmesso visualizzo la Data Trasmissione
 	} else {
 %> <input
					value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataTrasmissione(), "dd"))%>"
					type="text" size="2" maxlength="2"
					name="<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_TRASMISSIONE%>"
					onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> / <input
					value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataTrasmissione(), "MM"))%>"
					type="text" size="2" maxlength="2"
					name="<%=ICostantiProvvedimento.CAMPO_MESE_DATA_TRASMISSIONE%>"
					onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> / <input
					value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataTrasmissione(), "yyyy"))%>"
					type="text" size="4" maxlength="4"
					name="<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_TRASMISSIONE%>"
					onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)"> <%
 	}
 %>
				</td>
			</tr>

			<tr>
				<td class="l">Motivazione non Inviato</td>
				<td class="L"><select title="motivoNonInvio" class=small
					name="<%=ICostantiProvvedimento.CAMPO_COD_MOTIVO_NON_INVIO%>"
					onChange="AbilitaCampi()">
						<%=motivoNonInvio%>
				</select></td>
				<td class="l">Descrizione</td>
				<td class="L">
					<%
						if (documentoAllegato != null && documentoAllegato.getDescrizioneNonInvio() != null) {
					%> <input value="<%=documentoAllegato.getDescrizioneNonInvio()%>"
					type="text" size="30" maxlength="250"
					name="<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>">
					<%
						} else {
					%> <input value="" type="text" size="30" maxlength="250"
					name="<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>">
					<%
						}
					%>
				</td>
			</tr>

			<tr>
				<td class="l">Data Inserimento Manuale</td>
				<td class="L"><input
					value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataInsMan(), "dd"))%>"
					type="text" size="2" maxlength="2"
					name="<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>"
					onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> / <input
					value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataInsMan(), "MM"))%>"
					type="text" size="2" maxlength="2"
					name="<%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>"
					onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> / <input
					value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataInsMan(), "yyyy"))%>"
					type="text" size="4" maxlength="4"
					name="<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>"
					onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)"></td>
			</tr>

			<tr>
				<td><font class="L"> </font>&nbsp;</td>
			</tr>

			<tr>
				<td><input class="bottone" type="submit" value="Conferma"
					onClick="javascript:return VerifyConferma();"></td>
			</tr>

		</table>

		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"
			value="<%=lAzione%>"> <input type="HIDDEN"
			value="<%=StringUtils.toStringJSP(evento.getIdEvento(), "")%>"
			name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"> <input
			type="HIDDEN"
			value="<%=StringUtils.toStringJSP(documentoAllegato.getIdDocumentoAllegato(), "")%>"
			name="<%=ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO%>">
		<input type="HIDDEN"
			value="<%=documentoAllegato.getDataTrasmissione()%>"
			name="<%=ICostantiDocumentoAllegato.CAMPO_FLAG_FOGLIO_TRASMESSO%>">
		<input type="HIDDEN" value=""
			name="<%=ICostantiDocumentoAllegato.CAMPO_FLAG_MOTIVO_NON_INVIO%>">

	</FORM>
	<script language="JavaScript" type="text/javascript">

    var frmvalidator = new Validator("LoadInserisciCompFoglioComp");
    // Controllo data Compilazione/Trasmissione.
    frmvalidator.addValidation("<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_TRASMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiProvvedimento.CAMPO_MESE_DATA_TRASMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_TRASMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_TRASMISSIONE%>","minlen=4", "La lunghezza del campo Anno Data Compilazione/Trasmissione deve essere di 4 caratteri");

    // Controllo data inserimento manuale.
    frmvalidator.addValidation("<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>", "minlen=4", "La lunghezza del campo Anno Data Inserimento Manuale deve essere di 4 caratteri");

	//Chiama la funzione di Verify().
	frmvalidator.setAddnlValidationFunction("Verify");
	</script>

</body>
</html>
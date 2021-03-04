<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %> 
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.documentoallegato.action.ICostantiDocumentoAllegato" %>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento" %>
<%@ page import="siap.siep.beneficio.action.ICostantiBeneficio"%>

<jsp:useBean id="documentoAllegato"   	scope="request" class="siap.sius.documentoallegato.model.DocumentoAllegatoModel"/>
<jsp:useBean id="fascicoloSiusGP"     	scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="depositoDecreto"     	scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>
<jsp:useBean id="depositoordinanzapc" 	scope="request" class="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel"/>
<jsp:useBean id="UtenteConnesso"      	scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="provenienza"         	scope="request" class="java.lang.String"/>
<jsp:useBean id="motivoNonInvio"      	scope="request" class="java.lang.String"/>
<jsp:useBean id="evento" 			 	scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="fascicolo" 		  	scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="azioneChiamante"     	scope="request" class="java.lang.String"/>

<html>
<head>
  	<title>[S.I.E.S.] - Compilazione Foglio Complementare</title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<!--   	<link rel="STYLESHEET" type="text/css" href="/css/style.css"> -->
	<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<%
String lAzione = new String();
lAzione = "siap.siep.fogliocomplementare.action.ActModificaCompFoglioComp";
String lNota = "N.B.: Il Foglio Complementare è già stato creato. Inserire eventualmente le indicazioni relative all'inserimento manuale e premere il tasto Conferma.";
java.math.BigDecimal idEventoInterconnessione;
if (depositoordinanzapc != null && depositoordinanzapc.getIdDepositoOrdinanzaPc() != null && depositoordinanzapc.getIdEventoGenerato() != null)
	idEventoInterconnessione = depositoordinanzapc.getIdEventoGenerato();
else if(depositoDecreto != null && depositoDecreto.getIdDepositoDecreto() != null && depositoDecreto.getIdEventoGenerato() != null)
	idEventoInterconnessione = depositoDecreto.getIdEventoGenerato();
else 
	idEventoInterconnessione = evento.getIdEvento();
String idUtente = UtenteConnesso.getUserId();
// MEV INTEGRAZIONE SIES ADN: aggiunta impostazione di variabile userAdn
String userAdn = UtenteConnesso.getUserAdn();
%>

<script type="text/javascript">
// variabile utilizzata per poter abilitare o disabilitare le icone
// di trasmissione, modifica o cancellazione del foglio complementare da SIC
var abilitaImgTrasmissioneFC = true;
	      
function openPopup(url) {
	if (abilitaImgTrasmissioneFC) {
		newwindow = window.open(url,'name','height=570,width=920,top=200,left=200,location=0,menubar=0,status=0,resizable=0,scrollbars=1');
  		if (window.focus)
  			newwindow.focus();
	} else
		alert("Operazione non consentita");
}

function openPopupAnnulla(/*url*/) {
	// MEV 16: aggiunta funzione per disabilitare l'icona di annullamento fc
	// newwindow=window.open(url,'name','height=570,width=920,top=200,left=200,location=0,menubar=0,status=0,resizable=0,scrollbars=1');
	// if (window.focus) {newwindow.focus()}
	if (!abilitaImgTrasmissioneFC)
		alert("Operazione non consentita");
	else
		confermaAnnullamento('siap.siep.fogliocomplementare.action.ActLoadAnnullaCFC&tipoOperazione=ANNULLANSC&tipoWS=siepToNsc&idSoggetto=<%=fascicolo.getSoggetto().getIdSoggetto()%>&idSentenza=<%=fascicolo.getSenIdSentenza()%>&idFascicoloSiep=<%=fascicolo.getIdFascicoloSiep()%>','IdEvento','<%=idEventoInterconnessione%>','CampoFill','fill');
}

function AbilitaIconeTrasmissione() {
	// Disabilita i pulsanti Trasmissione/Modifica/Cancellazione del Foglio Complementare 
	// alla selezione della motivazione non invio "Iscritto Manualmente su NSC".
	if (document.LoadInserisciCompFoglioComp.<%=ICostantiDocumentoAllegato.CAMPO_FLAG_MOTIVO_NON_INVIO%>.value == '01') {
  		abilitaImgTrasmissioneFC = false;
	} else {
		abilitaImgTrasmissioneFC = true;
 	}
}

function VerifyConferma() {
	// Controllo obbligatorietà Data Inserimento Manuale oppure Descrizione
   	// se viene selezionato dalla lista "Inserito Manualmente su NSC".
  	if (document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_COD_MOTIVO_NON_INVIO%>.value == '01') {
		var dataInserimentoManuale=document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>.value +'/'+
        document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>.value +'/'+
        document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>.value;
		var descriz = document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>.value;
		if (dataInserimentoManuale != null && dataInserimentoManuale.length<3 && descriz == "") {
    		alert ("Valorizzare Descrizione oppure Data Inserimento Manuale");
    		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE %>.focus();
     		return false;
		}
	}

  	// Controllo obbligatorietà Descrizione se viene selezionato dalla lista "Altro".
	if (document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_COD_MOTIVO_NON_INVIO%>.value == '02') {
		var descrizione = document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>.value;
		if (descrizione == "") {
			alert ("Descrizione è un campo obbligatorio");
			document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO %>.focus();
       		return false;
     	}
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

	if (!ControllaData(dataCompilTrasm)) {
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

	// se il Foglio Complementare è stato inviato i campi "Motivazione non Inviato",
	// "Descrizione" e "Data Inserimento Manuale" non sono editabili
	var dataTrasmissione = document.LoadInserisciCompFoglioComp.<%=ICostantiDocumentoAllegato.CAMPO_FLAG_FOGLIO_TRASMESSO%>.value;
	if (dataTrasmissione.length > 5) {  
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_COD_MOTIVO_NON_INVIO%>.disabled = true;
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>.disabled = true;
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>.disabled = true;
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>.disabled = true;
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>.disabled = true;
   	}

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
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>.value = "";
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>.value = "";
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>.value = "";
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>.value = "";
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>.disabled = false;
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>.disabled = false;
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>.disabled = false;
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>.disabled = false;
	} else {
	    //ALTRO
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>.value = "";
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>.value = "";
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>.value = "";
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>.value = "";
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>.disabled = false;
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>.disabled = true;
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>.disabled = true;
		document.LoadInserisciCompFoglioComp.<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>.disabled = true;
	}
}

<%-- MERGE v10: aggiunta funzione che effettua un controllo preventivo --%>
function checkTipoProvv() {
<%
String codTipoProvvedimento = evento.getCodTipoProvvedimento();
if ("01".equals(codTipoProvvedimento)
		|| "02".equals(codTipoProvvedimento)
		|| "03".equals(codTipoProvvedimento)
		|| "04".equals(codTipoProvvedimento)) {
%>
	return true;
<%
} else {
%>
	alert("Per la Stampa consultare lo storico invio trasmissioni al SIC!");	
	return false;
<%
}
%>
}
</script>
</head>

<%
if (provenienza != null && provenienza.equals("InsertFC")) {
%>
<body class="corpo" onload="AbilitaCampi()">
<%
} else {
%>
<body class="corpo" onload="AbilitaIconeTrasmissione()">
<%
}
%>		

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
  		<td class="LBG">
		    <font class="label">Funzione :</font>&nbsp;
		    <font class="campo">Dettaglio Foglio Complementare</font>
  		</td>
		<td class="LBG">
			<%-- MERGE v10: aggiunta chiamata a funzione che effettua un controllo preventivo --%>
			<a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActStampaFoglioComp&<%=ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO%>=<%=documentoAllegato.getIdDocumentoAllegato()%>')" onclick="return checkTipoProvv();">
    			<img id="generaStampa" align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
  			</a>
		</td>

		<!-- Gestione Foglio Complementare -->
		<%-- MEV 16: aggiunti parametri di passaggio per INSERT, UPDATE e DELETE --%>
 		<td class="LBG">
			<a href="#" onClick="openPopup('/siesEsecuzione/index.jsp?action=INSERT&idEvento=<%=idEventoInterconnessione%>&idUtente=<%=idUtente%>&userAdn=<%=userAdn%>&tipoWS=siepToNsc&idSoggetto=<%=fascicolo.getSoggetto().getIdSoggetto()%>&idSentenza=<%=fascicolo.getSenIdSentenza()%>&idFascicoloSiep=<%=fascicolo.getIdFascicoloSiep()%>');return(false);">
				<img id="TrasmissioneFC" align="middle" src="/images/insertWS.png" alt="Trasmissione Foglio Complementare al SIC" width="24" height="24" border="0">
  			</a>
		</td>
		<td class="LBG">
 			<a href="#" onClick="openPopup('/siesEsecuzione/index.jsp?action=UPDATE&idEvento=<%=idEventoInterconnessione%>&idUtente=<%=idUtente%>&userAdn=<%=userAdn%>&tipoWS=siepToNsc&idSoggetto=<%=fascicolo.getSoggetto().getIdSoggetto()%>&idSentenza=<%=fascicolo.getSenIdSentenza()%>&idFascicoloSiep=<%=fascicolo.getIdFascicoloSiep()%>');return(false);" >
				<img id="ModificaFC" align="middle" src="/images/updateWS.png" alt="Modifica Foglio Complementare sul SIC" width="24" height="24" border="0">
  			</a>
		</td>
		<td class="LBG">
 			<a href="#" onClick="openPopup('/siesEsecuzione/search?idEvento=<%=idEventoInterconnessione%>&idUtente=<%=idUtente%>&userAdn=<%=userAdn%>');return(false);">
				<img id="Storico" align="middle" src="/images/certificatoWS.png" alt="Storico invio trasmissioni al SIC" width="24" height="24" border="0">
  			</a>
		</td>

<%
if (documentoAllegato.getDataAnnullamento() == null) {
	// il foglio complementare non è annullato ==> deve essere presente il bottone "X" annulla FC
%>
		<%-- MEV 16: aggiunta funzione di controllo --%>
	    <td class="LBG">  
			<a onClick="openPopupAnnulla();">
	        	<img id="Annulla" align="middle" src="/images/delete24.gif" alt="Annulla" width="24" height="24" border="0">
	       </a>
	    </td>
<%
}
%>

  		<!-- BOTTONE DI RITORNO -->
    	<%-- <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/> --%>
<%
if (azioneChiamante != null && !azioneChiamante.equals("")) {
%>
	    <td class="LBG">  		
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=azioneChiamante%>&IdEvento=<%=idEventoInterconnessione%>">
				<img src="/images/arrowleft24.gif" width="30" height="30" alt="Indietro" border="0"> 
			</a>
		</td>
<%		  
} else {
%>    
	    <td class="LBG">  		
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.ordineesecuzione.action.ActRicercaProvvedimenti&IdEvento=<%=idEventoInterconnessione%>">
				<img src="/images/arrowleft24.gif" width="30" height="30" alt="Ritorno Elenco Provvedimenti" border="0"> 
			</a>
		</td>
<%
}
%>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciCompFoglioComp">
<table cellspacing=2 cellpadding=2>
	<tr>
		<td class="Titolo" colspan=6>Estremi Provvedimento</td>
    </tr>

<%
if (documentoAllegato.getDataAnnullamento() != null
		/*&& documentoAllegato.getFlagAnnullamento() != null && impugnazione.getFlagAnnullamento().equalsIgnoreCase("S") */) {
%>
 	<tr>
		<td class="l"><font class="cRosso"> Foglio Complementare ANNULLATO</font></td>
 	</tr>
	<tr>
        <td class="l"> Data di annullamento </td>
        <td class="L"><%=DateUtils.getDateToString(documentoAllegato.getDataAnnullamento(),"dd-MM-yyyy")%></td>
	</tr>
	<tr>
        <td class="l">Motivo annullamento</td>
        <td class="l"><%=(documentoAllegato.getMotivoAnnullamento() == null ) ? "-" : documentoAllegato.getMotivoAnnullamento()%></td>
	</tr>
<%
}
%>

    <tr>
		<td class="l">Anno/Numero F.C.
		<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(documentoAllegato.getAnnoFoglioComplementare()  )%></font>
			<font class="l">/</font>
			<font class="campo"><%=StringUtils.toStringJSP(documentoAllegato.getProgrFoglioComplementare() )%></font>
		</td>
    </tr>

<%   
if (provenienza != null && (provenienza.equals("InsertFC") || provenienza.equals("ModificaFC"))) {
%>
	<!-- Sono nell'inserimento del Foglio Complementare -->
	<tr>
		<td class="l">Data Compilazione/Trasmissione <font class=ob>(*)</font></td>
	  	<td class="L">
	    	<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataEmissione(), "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_TRASMISSIONE%>"  
				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataEmissione(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiProvvedimento.CAMPO_MESE_DATA_TRASMISSIONE%>"  
				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataEmissione(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_TRASMISSIONE%>"  
				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
</table>
<br/>
<table cellspacing=2 cellpadding=2>
	<tr>
        <td class="lVerdeNB"><%=lNota%></td>
    </tr>
</table>
<br/>
<table cellspacing=2 cellpadding=2>	
    <tr>
		<td class="l">Motivazione non Inviato</td>
      	<td class="L">
			<select title="motivoNonInvio" class=small name="<%=ICostantiProvvedimento.CAMPO_COD_MOTIVO_NON_INVIO%>" onChange="AbilitaCampi()" >
				<%= motivoNonInvio %>
       	  	</select> 	      
		</td>
		<td class="l">Descrizione</td>
		<td class="L" >
			<input value="" type="text" size="30" maxlength="250" name="<%=ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO%>" > 
   	  	</td>
	</tr>
    <tr>
      	<td class="l">Data Inserimento Manuale</td>
      	<td class="L">
        	<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>"  
   				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
        	<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>"  
   				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
        	<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>"  
  				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
    </tr>
    <tr>
		<td colspan="2">
        	<font class="L">&nbsp;</font>
      	</td>
    </tr>
    <tr>
      	<td colspan="2">
        	<input class="bottone" type="submit" value="Conferma Inserimento Manuale" onClick="javascript:return VerifyConferma();">
      	</td>
    </tr>
<%
} else {
%>
    <!-- Sono nel Dettaglio Foglio Complementare -->
    <tr>
      	<td class="l">Data Compilazione/Trasmissione </td>
      	<td class="L">
          	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataTrasmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      	</td>
    </tr>
    <tr>
      	<td class="l">Motivazione non Inviato </td>
      	<td class="L">
          	<font class="campo"><%=StringUtils.toStringJSP(documentoAllegato.getDescrMotivazioneNonInvio())%></font>&nbsp;
      	</td>
      	<td class="l">Descrizione </td>
      	<td class="L">
          	<font class="campo"><%=StringUtils.toStringJSP(documentoAllegato.getDescrizioneNonInvio())%></font>&nbsp;
      	</td>
   	</tr>
    <tr>
      	<td class="l">Data Inserimento Manuale </td>
      	<td class="L">
          	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataInsMan(),"dd-MM-yyyy"))%></font>&nbsp;
      	</td>
    </tr>
<%
}
%>
</table>

<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
<input type="HIDDEN" value="<%=StringUtils.toStringJSP( evento.getIdEvento(), "" )%>" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>">
<input type="HIDDEN" value="<%=StringUtils.toStringJSP( documentoAllegato.getIdDocumentoAllegato(), "" )%>" name="<%=ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO%>">
<input type="HIDDEN" value="<%=documentoAllegato.getDataTrasmissione()%>" name="<%=ICostantiDocumentoAllegato.CAMPO_FLAG_FOGLIO_TRASMESSO%>">
<input type="HIDDEN" value="<%=documentoAllegato.getCodMotivazioneNonInvio()%>" name="<%=ICostantiDocumentoAllegato.CAMPO_FLAG_MOTIVO_NON_INVIO%>">
<input type="HIDDEN" name="<%=ICostantiBeneficio.CAMPO_PROVENIENZA%>" value="<%=provenienza%>">
  
</FORM>
<%
if (provenienza != null && provenienza.equals("InsertFC")) {
%>
<script language="JavaScript" type="text/javascript">

var frmvalidator = new Validator("LoadInserisciCompFoglioComp");
// Controllo data Compilazione/Trasmissione.
frmvalidator.addValidation("<%= ICostantiProvvedimento.CAMPO_GIORNO_DATA_TRASMISSIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiProvvedimento.CAMPO_MESE_DATA_TRASMISSIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiProvvedimento.CAMPO_ANNO_DATA_TRASMISSIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiProvvedimento.CAMPO_ANNO_DATA_TRASMISSIONE%>","minlen=4","La lunghezza del campo Anno Data Compilazione/Trasmissione deve essere di 4 caratteri");

// Controllo data inserimento manuale.
frmvalidator.addValidation("<%= ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE%>","numeric");
frmvalidator.addValidation("<%= ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE%>","numeric");
frmvalidator.addValidation("<%= ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>","numeric");
frmvalidator.addValidation("<%= ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE%>","minlen=4","La lunghezza del campo Anno Data Inserimento Manuale deve essere di 4 caratteri");

//Chiama la funzione di Verify().
frmvalidator.setAddnlValidationFunction("Verify");
<%
}
%>
</script>
</body>
</html>
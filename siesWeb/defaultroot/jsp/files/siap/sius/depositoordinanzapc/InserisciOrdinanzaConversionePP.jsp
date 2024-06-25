<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.AbstractList"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria"%>
<%@ page import="siap.siep.penapecuniaria.model.RichiestaConversioneModel"%>
<%@ page import="siap.siep.penapecuniaria.model.RichiestaConversioneEstesaModel"%>
<%@ page import="siap.siep.rateizzazionepp.action.ICostantiRateizzazionePP"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>
<%@ page import="siap.sius.penapecuniaria.action.ICostantiSiusPenaPecuniaria"%>

<jsp:useBean id="fascicoloSiusGP"		scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="contenuto"				scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"			scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"		scope="request" class="java.util.Date"/>
<jsp:useBean id="richiesteconversioni"	scope="request" class="java.util.Vector"/>
<jsp:useBean id="TornaQui"				scope="request" class="java.lang.String"/>
<%-- MEV_2023-35: aggiunto useBean --%>
<jsp:useBean id="rataMancatoPagamento"	scope="request" class="siap.siep.rateizzazionepp.model.RateizzazionePPModel"/>

<%
TenoreModel[] tenori = (TenoreModel[]) request.getAttribute("tenori");
String[] esiti = (String[]) request.getAttribute("esiti");
String lCodTipoProv = ICostantiProvvedimento.COD_ORDINANZA;
// presenza del Link per il bottone di ritorno
 boolean retFlag = false;
retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
String lAction = new String();
lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaConversioneRateizzazionePP";

// MEV_2023-35: aggiunta diversificazione per contenuto e gestito nella pagina il nuovo contenuto
// 2470 (Conversione pena pecuniaria - U070) 	--> 3180+3181 (U142)	&&	1773 (U070) 		--> 3210 (U142)	-->	3228 (U145)
// 2471 (Rateizzazione pena pecuniaria - U070)	--> 3180+3181 (U142)	&&	1770+1771 (U070)	--> 3205+3206+3207+3208 (U142) --> 3217 (U143)
String descrContenuto = "", tipoConversione = "Conversione";
if ("U142".equals(contenuto))
	descrContenuto = " principali per mancato pagamento";
else if  ("U143".equals(contenuto))
	descrContenuto = " irrogate dal GdP";
else if ("U145".equals(contenuto))
	tipoConversione = "Rateizzazione";
%>
<html>
<head>
<title>[S.I.E.S.] - Emissione Ordinanza <%=tipoConversione%> Pene Pecuniarie<%=descrContenuto%></title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="/html/verifyCombo.js"></script>

<script language="JavaScript">
// Controllo obbligatoriet&agrave; esiti.
function Verify() {
	var lEsiti = document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
	var ritorno = VerifyCombo(lEsiti, "Esito");
  	// Verifica congruenza Oggetto-Esito.
	if ((document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value in { '1770':1, '1771':1 }
			&& document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>.value == 2471)
			|| (document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value == '1773'
					&& document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>.value == 2470)) {
	    alert("Esito selezionato incompatibile con l'oggetto Procedimento");
	    return false;
	}
<%
// MEV_2023-35: aggiunto controllo preventivo
if ("U142".equals(contenuto)) {
	BigDecimal importoDaPagare = new BigDecimal(0);
 	if (rataMancatoPagamento.getFasSieIdFascicoloSiep() != null)
		importoDaPagare = rataMancatoPagamento.getImportoDaPagare();
%>
	if (document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value == '3210') {
		var importoDaPagare = document.InserisciOrdinanzaConversionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_I%>.value
			+ "." + document.InserisciOrdinanzaConversionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_D%>.value;
		if (importoDaPagare == "." || parseFloat(importoDaPagare) == "0.0") {
			alert("Totale Importo Rateizzato (intero e decimale) obbligatorio per l'esito scelto!");
			document.InserisciOrdinanzaConversionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_I%>.focus();
		    return false;
		} else {
			var importo = <%=importoDaPagare%>;
			var intero = document.InserisciOrdinanzaConversionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_I%>.value;
			var decimale = document.InserisciOrdinanzaConversionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_D%>.value;
			var importoRat = intero + "." + decimale;
			if (importo < importoRat) {
				alert("Totale Importo Rateizzato non può essere superiore od uguale all'importo da rateizzare richiesto!");
				document.InserisciOrdinanzaConversionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_I%>.focus();
			    return false;
			}
		}
		// DISCORSO RATE!!!
		var sommaRate = 0.0;
		for (i = 0; i < <%=ICostantiRateizzazionePP.NUM_MAX_RATE%>; i++) {
	   		idrata = "rata_" + i;
	   		display = document.getElementById(idrata).style.display;
	   		if (display == "block") {
	     		Rigo = i;
	     		var numRate = document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_NUM_RATE%>_' + Rigo).value;
	     		if (isNaN(numRate) || numRate < 1) {
					alert("Indicare il numero di rate per la rata N. " + (Rigo + 1));
					document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_NUM_RATE%>_' + Rigo).focus();
					return false;
	     		}
	     		var valoreRata = document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I%>_' + Rigo).value
					+ "." + document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D%>_' + Rigo).value;
	     		if (valoreRata == "." || parseFloat(valoreRata) == "0.0") {
					alert("Indicare l'importo della rata per la rata N. " + (Rigo + 1));
					document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I%>_' + Rigo).focus();
					return false;
	     		}
	     		sommaRate = sommaRate + (numRate*parseFloat(valoreRata));
	   		}
		}
		if (sommaRate < parseFloat(importoDaPagare)) {
			var msg = "Attenzione la somma delle rate da pagare (" + sommaRate + " euro) "
					+ "risulta inferiore al valore indicato come Importo Da Pagare " + parseFloat(importoDaPagare) + " euro. "
		           	+ "Si vuole procedere comunque?";
		  	if (!window.confirm(msg))
				return false;
	  	} else if (sommaRate > parseFloat(importoDaPagare)) {
			var msg = "Attenzione la somma delle rate da pagare (" + sommaRate + " euro) "
					+ "risulta superiore al valore indicato come Importo Da Pagare " + parseFloat(importoDaPagare) + " euro. "
	               	+ "Si vuole procedere comunque?";
	      	if (!window.confirm(msg))
				return false;
	  	}
<%
}
%>
	}
	return ritorno;
}

function caricamento() {
	node = document.getElementById("divConversione");
	node.style.display = 'none';
	node = document.getElementById("divRateizzazione");
	node.style.display = 'none';
	node = document.getElementById("divFinale");
	node.style.display = 'none';
}

function selectEsito() {
	var sizeTenori = <%=tenori.length%>;
	// Esito 'Dispone conversione...'
	if (document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value in { '1770':1, '1771':1 }
			// MEV_2023-35: aggiunti esisti
			|| document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value in { '3205':1, '3206':1, '3207':1, '3208':1, '3217':1 }) {
		node = document.getElementById("divConversione");
		node.style.display = 'block';
		node = document.getElementById("divRateizzazione");
		node.style.display = 'none';
		node = document.getElementById("divFinale");
		node.style.display = 'block';
	}
	// Esito 'Rateizza...'
	else if (document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value == '1773'
			// MEV_2023-35: aggiunta or condition sempre per l'esito "Rateizza Pagamento"
			|| document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value == '3210'
			|| document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value == '3228') {
		node = document.getElementById("divConversione");
		node.style.display = 'none';
		node = document.getElementById("divRateizzazione");
		node.style.display = 'block';
		node = document.getElementById("divFinale");
		node.style.display = 'block';
	}
	// Tutti gli altri Esiti.
	else {
		node = document.getElementById("divConversione");
		node.style.display = 'none';
		node = document.getElementById("divRateizzazione");
		node.style.display = 'none';
		node = document.getElementById("divFinale");
		node.style.display = 'block';
	}
<%
for (int i = 0; i < richiesteconversioni.size(); i++) {
%> 			
	if (document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value == '1770') {
		document.InserisciOrdinanzaConversionePP.<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%><%=i%>[0].checked = true;
		document.InserisciOrdinanzaConversionePP.<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%><%=i%>[0].disabled = false;
		document.InserisciOrdinanzaConversionePP.<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%><%=i%>[1].disabled = false;
	}
	if (document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value == '1771') {
		document.InserisciOrdinanzaConversionePP.<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%><%=i%>[1].checked = true;
		document.InserisciOrdinanzaConversionePP.<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%><%=i%>[1].disabled = false;
		document.InserisciOrdinanzaConversionePP.<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%><%=i%>[0].disabled = false;
	}
<%
}
%>
	if (sizeTenori > 1) {
		document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE%>[0].focus();
	} else {
		document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE%>.focus();
	}
}

// MEV_2023-35: aggiunta funzione per ulteriore rata
function addUlterioreRata() {
	for (i = 0; i < <%=ICostantiRateizzazionePP.NUM_MAX_RATE%>; i++) {
		idrata = "rata_" + i;
		display = document.getElementById(idrata).style.display;
		if (display == "none") {
			Rigo = i;
			document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_NUM_RATE%>_' + Rigo).disabled = false ;
			document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I%>_' + Rigo).disabled = false ;
			document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D%>_' + Rigo).disabled = false ;
			strTdCancella = '<a href="Javascript:cancellaRata(\''+Rigo+'\');"><img src="/images/delete.gif" border="0" title="Cancella rata"></a>';
			document.getElementById('tdCancella_' + Rigo).innerHTML = strTdCancella;
			if (Rigo > 1)
			 	document.getElementById('tdCancella_' + (Rigo - 1)).innerHTML = '<br>';
			document.getElementById(idrata).style.display = "block";
			break;
		}
	}
}

function cancellaRata(Rigo) {
	document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_NUM_RATE%>_'+Rigo).value = "" ;
	document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I%>_'+Rigo).value = "" ;
	document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D%>_'+Rigo).value = "" ;
	document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_NUM_RATE%>_'+Rigo).disabled = true ;
	document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I%>_'+Rigo).disabled = true ;
	document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D%>_'+Rigo).disabled = true ;
	if (Rigo > 1) {
    	strTdCancella = '<a href="Javascript:cancellaRata(\'' + (Rigo -1 ) + '\');"><img src="/images/delete.gif" border="0" title="Cancella Rata"></a>';
   		document.getElementById('tdCancella_' + (Rigo - 1)).innerHTML = strTdCancella;
    	document.getElementById('tdCancella_' + (Rigo)).innerHTML = '<br>';
  	}
	document.getElementById("rata_" + Rigo).style.display = "none";
}
</script>
</head>
<body class="corpo" onLoad="caricamento();">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
      	<td class=LBG>
      		<font class="label">Funzione : </font> <font class="campo">Emissione Ordinanza <%=tipoConversione%> Pene Pecuniarie<%=descrContenuto%></font>
      	</td>
	</tr>
    <tr>
		<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
</table>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaConversionePP">
<table width="35%">
	<tr>
		<td class="l" width="30%">Data Emissione</td>
		<td class="l"><%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
	</tr>
</table>
<table cellspacing="2" cellpadding="2" style="width: 90%;">
 	<tr><td>&nbsp;</td></tr>
	<tr>
     	<td class="Titolo" colspan="2"> Specificare esito per ciascun oggetto: </td>
 	</tr>
 	<tr>
		<td class="l"> Oggetto </td>
		<td class="l"> Esito </td>
 	</tr>
<%
for (int i = 0; i < tenori.length; i++) {
%>
	<tr>
		<td class="l">
		  	<input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE%>" value="<%=tenori[i].getDescrOggettoTenore()%>" readonly size="85%">
			<input type="hidden" name="<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>" value="<%=tenori[i].getCodOggettoTenore()%>">
			<input type="hidden" name="<%=ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=tenori[i].getCodDettaglioOggetto()%>">
		</td>
		<td class="l">
		   	<select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onChange="selectEsito();"><%=esiti[i]%></select>
		</td>
	</tr>
<%
}
%>
</table>
<table cellspacing="2" cellpadding="2" style="width: 90%;">
	<tr>
		<td class="l">Motivazione</td>
	 	<td class="l"><TEXTAREA title="Motivazione della decisione" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE%>" cols="70" rows="4"></textarea></td>
	</tr>
	<tr>
		<td class="l">Ulteriore descrizione della decisione</td>
	  	<td class="l"><TEXTAREA title="Ulteriore Descrizione della decisione" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE%>" cols="70" rows="4"></textarea></td>
	</tr>
</table>
<%
int indice = -1;
Iterator itx = richiesteconversioni.iterator();
String lTitoloRichiestaCPP = "";
%> 
<div id="divConversione" style="position: relative; top: 0; left: 0;">
<%
if (!("U142".equals(contenuto) || "U143".equals(contenuto))) {
%>
<table cellspacing="2" cellpadding="2" width="90%">
<%
while (itx.hasNext()) {
	indice++;
 	RichiestaConversioneEstesaModel lRicConEstesa = (RichiestaConversioneEstesaModel)itx.next();
	if (lRicConEstesa.getRichiestaConversione().getFasSieIdFascicoloSiep() != null
			&& lRicConEstesa.getRichiestaConversione().getFasSiuIdFascicoloSius() == null)
		lTitoloRichiestaCPP = "Pena Pecuniaria relativa al N. SIEP " + lRicConEstesa.getFasSiep().getChiaveAnno() + " / " + lRicConEstesa.getFasSiep().getChiaveProgr();
	else
		lTitoloRichiestaCPP = "Pena Pecuniaria inserita dall' UDS";
%>
	<tr>
		<td class="Titolo" colspan="6"> <%=lTitoloRichiestaCPP%></td>
	</tr>
	<tr>
		<td colspan="3" width="20%" class="l">
			<input type="hidden" name="<%=ICostantiPenaPecuniaria.CAMPO_ID_RICHIESTA_CONVERSIONE%>" value="<%=lRicConEstesa.getRichiestaConversione().getIdRichiestaConversione()%>">
			<font class="label"> Multa</font>&nbsp;
<%
	if (lRicConEstesa.getRichiestaConversione().getImportoMulta() != null) {
%>
			<font class="campo"><%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoMulta())%></font><br>
<%
	} else {
%>
			-
<%
	}
%>
			<br>
			<font class="label">Ammenda</font>&nbsp;
<%
	if (lRicConEstesa.getRichiestaConversione().getImportoAmmenda() != null) {
%>
			<font class="campo"> <%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoAmmenda())%></font>
<%
	} else {
%>
			-
<%
	}
%>
		</td>
		<td colspan="3" class="l"> 
			<font class="label"> Q u a n t u m  &nbsp;&nbsp;  S a n z i o n e &nbsp;&nbsp;  S o s t i t u t i v a  <br></font>
			<font class="label">Anni</font>
			<font class="campo"> <input title="Anni" type="text" size="2" maxlength="2" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_NUM_ANNI_SS%>"></font>
			<font class="label"> Mesi</font>
			<font class="campo"> <input title="Mesi" type="text" size="2" maxlength="2" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_NUM_MESI_SS%>"></font>
			<font class="label"> Giorni</font>&nbsp;&nbsp;&nbsp;
			<font class="campo"> <input title="Giorni" type="text" size="4" maxlength="4" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_NUM_GIORNI_SS%>"></font>
			<font class="label"> Libert&agrave; controllata </font>
			<input value="01" type="radio" checked name="<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%><%=indice%>">&nbsp;&nbsp;
			<font class="label"> o Lavoro sostitutivo </font>
			<input value="02" type="radio" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%><%=indice%>">
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
<%
}
%>
</table>
<%
} else {
%>
<table cellspacing="2" cellpadding="2" width="90%">
<%
 	if (rataMancatoPagamento.getFasSieIdFascicoloSiep() != null) 
		lTitoloRichiestaCPP = "Pena Pecuniaria relativa al N. SIEP " + fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnnoSIEP() + "/" + fascicoloSiusGP.getFascicoloSiusModel().getChiaveProgrSIEP() + " - ";
	lTitoloRichiestaCPP += "Pena Pecuniaria inserita dall'UDS";
%>
	<tr>
		<td class="Titolo" colspan="2"><%=lTitoloRichiestaCPP%></td>
	</tr>
	<tr>
		<td width="20%" class="l" colspan="2"> 
			<font class="label">Importo da convertire richiesto:</font>&nbsp;
			<font class="campo"><%=StringUtils.toEuroFormat(rataMancatoPagamento.getImportoDaPagare())%></font>&nbsp;&nbsp;&nbsp;-&nbsp;
			<font class="label">Importo Convertito</font>&nbsp;
			<font class="campo"> 
				<input Title="Importo Rateizzato" size="7" maxlength="16" type="text" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_INTERO_IMPORTO_MULTA%>" onkeypress="return TicTabNumField(this,event)">,
				<input Title="Importo Rateizzato" size="2" maxlength="2" type="text" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_DECIMALE_IMPORTO_MULTA%>" onkeypress="return TicTabNumField(this,event)">
			</font>&euro;
		</td>
	</tr>
	<tr>
		<td colspan="3" class="l"> 
			<font class="label"> Q u a n t u m  &nbsp;&nbsp;  Pena &nbsp;&nbsp;  S o s t i t u t i v a  <br></font>
			<font class="label">Anni</font>
			<font class="campo"> <input title="Anni" type="text" size="2" maxlength="2" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_NUM_ANNI_SS%>"></font>
			<font class="label"> Mesi</font>
			<font class="campo"> <input title="Mesi" type="text" size="2" maxlength="2" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_NUM_MESI_SS%>"></font>
			<font class="label"> Giorni</font>&nbsp;&nbsp;&nbsp;
			<font class="campo"> <input title="Giorni" type="text" size="4" maxlength="4" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_NUM_GIORNI_SS%>"></font>
<%
	if (!"U143".equals(contenuto)) {
%>
			<font class="label"> Semilibert&agrave; </font>
			<input value="01" type="radio" checked name="<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%>">&nbsp;&nbsp;
			<font class="label"> Detenzione Domiciliare </font>
			<input value="02" type="radio" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%>">&nbsp;&nbsp;
			<font class="label"> Lavoro Pubblica Utilit&agrave; </font>
			<input value="03" type="radio" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%>">
<%
	} else {
%>
			<font class="label"> Permanenza Domiciliare </font>
			<input value="01" type="radio" checked name="<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%>">
<%
	}
%>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
</table>
<%
}
%>
</div>
<%
itx = richiesteconversioni.iterator();
%> 
<div id="divRateizzazione" style="position: relative; top: 0; left: 0;">
<%
if (!("U142".equals(contenuto) || "U145".equals(contenuto))) {
%>
<table cellspacing="2" cellpadding="2" width="90%">
<%
while (itx.hasNext()) {
  	RichiestaConversioneEstesaModel lRicConEstesa = (RichiestaConversioneEstesaModel)itx.next();
 	if (lRicConEstesa.getRichiestaConversione().getFasSieIdFascicoloSiep() != null
 			&& lRicConEstesa.getRichiestaConversione().getFasSiuIdFascicoloSius() == null) 
		lTitoloRichiestaCPP = "Pena Pecuniaria relativa al N. SIEP " + lRicConEstesa.getFasSiep().getChiaveAnno() + " / " + lRicConEstesa.getFasSiep().getChiaveProgr();
	else
		lTitoloRichiestaCPP = "Pena Pecuniaria inserita dall'UDS";
%>
	<tr>
		<td class="Titolo" colspan="6"><%=lTitoloRichiestaCPP%></td>
	</tr>
	<tr>
		<td colspan="2" width="20%" class="l"> 
			<font class="label">Multa</font>&nbsp;
<%
	if (lRicConEstesa.getRichiestaConversione().getImportoMulta() != null) {
%>
			<font class="campo"><%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoMulta())%></font>&nbsp;&nbsp;&nbsp;
<%
	} else {
%>
			-
<%
	}
%>
			<br>
			<font class="label">Ammenda</font>&nbsp;
<%
	if (lRicConEstesa.getRichiestaConversione().getImportoAmmenda() != null) {
%>
			<font class="campo"><%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoAmmenda())%></font>
<%
	} else {
%>
			-
<%
	}
%>
		</td>
		<td colspan="4" class="l"> 
			<font class="label">N.ro rate</font>
			<font class="campo"> <input title="Rate" type="text" size="2" maxlength="2" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_NUMERO_RATE%>"></font>
			<font class="label">&nbsp;da &euro;&nbsp;</font>
			<font class="campo"> 
				<input Title="Multa" size="7" maxlength="16" type="text" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_INTERO_IMPORTO_MULTA%>" onkeypress="return TicTabNumField(this,event)">,
				<input Title="Multa" size="2" maxlength="2" type="text" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_DECIMALE_IMPORTO_MULTA%>" onkeypress="return TicTabNumField(this,event)">
			</font>
			<font class="label">&nbsp;&nbsp;+ 1 rata da &euro;&nbsp;</font>
			<font class="campo"> 
				<input Title="Multa" size="7" maxlength="16" type="text" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_INTERO_IMPORTOFINALE_MULTA%>" onkeypress="return TicTabNumField(this,event)">,
				<input Title="Multa" size="2" maxlength="2" type="text" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_DECIMALE_IMPORTOFINALE_MULTA%>" onkeypress="return TicTabNumField(this,event)">
			</font>
		</td>
	</tr>
<%
}
%>
	<tr>
		<td colspan="2" width="20%" class="l"> 
			<font class="label"> Termine pagamento 1° Rata : </font>
		</td>
		<td colspan="3" class="l"> 
			<font class="label">entro il </font>
			<font class="campo"> 
				<input Title="Data Termine Pagamento" type="text" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_GIORNO_DATA_TERMINE_PAG%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
				/
				<input Title="Data Termine Pagamento" type="text" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_MESE_DATA_TERMINE_PAG%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
				/
				<input Title="Data Termine Pagamento" type="text" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_ANNO_DATA_TERMINE_PAG%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			</font>
        	<font class="label">oppure entro </font>
			<font class="campo"><input type="text" size="4" maxlength="4" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_NUM_GIORNI_PER_PAGAMENTO%>"></font>           		
      		<font class="label">giorni dalla data di notifica</font>
			<input value="03" type="hidden" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%>">&nbsp;&nbsp;
		</td>
  	</tr>
	<tr><td>&nbsp;</td></tr>
</table>
<%
} else {
%>
<table cellspacing="2" cellpadding="2" width="90%">
<%
	BigDecimal importoDaPagare = new BigDecimal(0);
 	if (rataMancatoPagamento.getFasSieIdFascicoloSiep() != null) {
		lTitoloRichiestaCPP = "Pena Pecuniaria relativa al N. SIEP " + fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnnoSIEP() + "/" + fascicoloSiusGP.getFascicoloSiusModel().getChiaveProgrSIEP() + " - ";
		importoDaPagare = rataMancatoPagamento.getImportoDaPagare();
 	} /*else {
 		importoDaPagare = "Pena Pecuniaria inserita dall'UDS"???
 	}*/
	lTitoloRichiestaCPP += "Pena Pecuniaria inserita dall'UDS";
%>
	<tr>
		<td class="Titolo" colspan="2"><%=lTitoloRichiestaCPP%></td>
	</tr>
	<tr>
		<td width="20%" class="l" colspan="2"> 
			<font class="label">Importo da rateizzare richiesto:</font>&nbsp;
			<font class="campo"><%=StringUtils.toEuroFormat(importoDaPagare)%></font>&nbsp;&nbsp;&nbsp;-&nbsp;
			<font class="label">Totale Importo Rateizzato</font>&nbsp;
			<font class="campo"> 
				<input Title="Importo Rateizzato" size="7" maxlength="16" type="text" name="<%=ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_I%>" onkeypress="return TicTabNumField(this,event)">,
				<input Title="Importo Rateizzato" size="2" maxlength="2" type="text" name="<%=ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_D%>" onkeypress="return TicTabNumField(this,event)">
			</font>&euro;
		</td>
	</tr>
	<tr>
		<td class="Titolo" colspan="2">Modalit&agrave; di Pagamento</td>
	</tr>
<%
for (int i = 0; i < ICostantiRateizzazionePP.NUM_MAX_RATE; i++) {
	String display = "";
	if (i == 0)
		display = "block";
	else if (i > 0)
		display = "none";
%>
	<tr style="display:<%=display%>" id="rata_<%=i%>">
		<td class="l" <%if (i == 0) {%> colspan="2"<%}%>> 
			<font class="label">N.ro rate</font>
			<font class="campo"> <input title="Rate" type="text" size="2" maxlength="2" name="<%=ICostantiRateizzazionePP.CAMPO_NUM_RATE%>_<%=i%>" id="<%=ICostantiRateizzazionePP.CAMPO_NUM_RATE%>_<%=i%>"></font>&nbsp;&nbsp;&nbsp;
			<font class="label">Importo ciascuna rata</font>&nbsp;
			<font class="campo"> 
				<input Title="Multa" size="7" maxlength="16" type="text" name="<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I%>_<%=i%>" id="<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I%>_<%=i%>" onkeypress="return TicTabNumField(this,event)">,
				<input Title="Multa" size="2" maxlength="2" type="text" name="<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D%>_<%=i%>" id="<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D%>_<%=i%>" onkeypress="return TicTabNumField(this,event)">
			</font>&euro;
		</td>
<%
	if (i != 0) {
%>
		<td valign="middle" class="c" id="tdCancella_<%=i%>" width="15px">&nbsp;</td>
<%
	}
%>
	</tr>
<%
}
%>
	<tr>
		<td class="l" colspan="2">
			<a href="Javascript:addUlterioreRata();">Aggiungi ulteriore rata</a>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
</table>
<%
}
%>
</div>
<div id="divFinale" style="position: relative; top: 0; left: 0;">
<table cellspacing="2" cellpadding="2" width="90%">
	<tr>
		<td class="label">Inserimento Prescrizioni <input value="06" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_PRESCRIZIONI%>"></td>
	</tr>
</table>
<br>
<table>
	<tr>
   		<td>
      		<input class="bottone" type="submit" value="Conferma" onclick="javascript:return Verify();">
     	</td>
   	</tr>
</table>
</div>
<br>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>">
<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>">
<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%>>
<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP%>">
<input type="HIDDEN" name="numeroRichiesteCPP" value="<%=richiesteconversioni.size()%>">
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("InserisciOrdinanzaConversionePP");
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
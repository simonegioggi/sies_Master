<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.siep.penaaccessoria.action.ICostantiPenaAccessoria"%>

<jsp:useBean id="contenuto"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"    		scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"  		scope="request" class="java.util.Date"/>
<jsp:useBean id="Action"     			scope="request" class="java.lang.String"/>
<%-- MEV_2023-35: aggiunti useBean per gestione Sospensione esecuzione pene accessorie --%>
<jsp:useBean id="TipoPenaAccessoria"	scope="request" class="java.lang.String"/>
<jsp:useBean id="DurataPeneAccessorie"	scope="request" class="java.lang.String"/>

<%
TenoreModel[] tenori = (TenoreModel[]) request.getAttribute("tenori");
String[] esiti = (String[]) request.getAttribute("esiti");

UtenteModel lUteMod = (UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
UfficioModel lUffMod = lUteMod.getUfficioUtente();
String CodUff = new String(lUffMod.getCodTipoUfficio());
String labelUfficio = "";
if (CodUff.equals("TDSM") || CodUff.equals("UDSM")) {
	labelUfficio = "Tribunale per i Minorenni in funzione di Tribunale Sorveglianza";
} else {
	labelUfficio = "Tribunale di Sorveglianza";
}

// MEV_2023-35: aggiunti controlli sul contenuto
String tipoSosp = "Sanzione Sostitutiva";
if ("U134".equals(contenuto))
	tipoSosp = "Pena Sostitutiva";
else if ("U137".equals(contenuto))
	tipoSosp = "Lavoro Pubblica Utilit&agrave; Sostitutivo";
else if (ICostantiDepositoOrdinanzaPc.COD_OGGETTO_SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE_UDS.equals(contenuto)
		|| ICostantiDepositoOrdinanzaPc.COD_OGGETTO_SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE_TDS.equals(contenuto))
	tipoSosp = "Esecuzione Pena Accessoria";

// Imposta varibabili se Decreto o Ordinanza
String lAction = new String();
String lFunctionName = new String();

boolean lFlagOrdinanza = false;
if (Action.trim().length() > 1) {
	lAction = Action;
	lFlagOrdinanza = true;
	lFunctionName = "Emissione Ordinanza Sospensione " + tipoSosp;
} else {
	lAction = "siap.sius.depositodecreto.action.ActInserisciEmissioneDecretoDeposito";
	lFunctionName = "Emissione Decreto Sospensione " + tipoSosp;
}
%>

<html>
<head>
<title>[S.I.E.S.] - <%=lFunctionName%></title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="/html/verifyCombo.js"></script>

<script language="JavaScript">
var desktop;
// Chiamata lista Procure
function ListaProcure(a_formname,a_fieldname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Elenco Tribunali di Sorveglianza", "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}

function visualizza_campo() {
	var valoreScelta;
    var campoScelta = document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_FLAG_RECUPERO_SS%>;                       
	if (campoScelta && campoScelta.length > 0) {
    	for (a = 0; a < campoScelta.length; a++) {
        	if (campoScelta[a] != null && campoScelta[a].checked == true) {
            	valoreScelta = campoScelta[a].value;
            	if (valoreScelta == "S") {
            		// visualizza il campo
            		document.getElementById("numGiorni").style.display = "block";
            	} else {
          			// nasconde il campo
          			document.getElementById("numGiorni").style.display = "none";
          			// Svuota il campo
          			document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_GIORNI_RECUPERO_SS%>.value = '';
               	}
			}
		}
	}	
}
 	
function Verify() {
	// Controllo obbligatorietà esiti
	var lEsiti = document.InserisciDecretoSospensioneSS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
	var ritorno = VerifyCombo(lEsiti, "Esito");
	if (ritorno == false) {
		return false;
	}

	// Carica la Data Decorrenza e la Data Fino per poi usarle nei controlli  
	var data_inizio = document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>.value
		+ '/' + document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS%>.value
		+ '/' + document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS%>.value;      		
	var data_fine = document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>.value
		+ '/' + document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS%>.value
		+ '/' + document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS%>.value;

	// Carica i valori del Periodo Sospensione
	var chk_valore_periodo = Number(document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>.value)
		+ Number(document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS%>.value)
		+ Number(document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS%>.value);

	// Controlla i valori della Data Decorrenza Sospensione
	var chk_valore_data_inizio = false;
	if (document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>.value != ''
			&& document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS%>.value != ''
			&& document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS%>.value != '') {
		chk_valore_data_inizio = true;
	}

	// Controlla i valori della data Fino al
	var chk_valore_data_fine = false;
	if (document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>.value != ''
			&& document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS%>.value != ''
			&& document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS%>.value != '') {
		chk_valore_data_fine = true;
	}
   		
	// Controllo validità Data Decorrenza Sospensione
	if (!ControllaDataPassaVuota(data_inizio)) {
		alert('Data Decorrenza Sospensione non valida');
		return false;
	}

	// Controlla che non venga inserito il "Periodo Sospensione" e la Data "Fino al"" 			
	if (chk_valore_data_fine == true && chk_valore_periodo > 0) {
		alert('Inserire solo il "Periodo Sospensione" o solo la Data "Fino al"');
		return false;
	}

	// Controlla che per inserire il "Periodo Sospensione" ci sia la "Data Decorrenza Sospensione" 			
	if (chk_valore_data_inizio == false && chk_valore_periodo > 0) {
		alert('Per impostare il "Periodo Sospensione" è necessario impostare la "Data Decorrenza Sospensione"');
		return false;
	}

	// Controlla che per inserire la data "Fino al" ci sia la "Data Decorrenza Sospensione" 			
	if (chk_valore_data_fine == true && chk_valore_data_inizio == false) {
		alert('Per impostare la Data "Fino al" è necessario impostare la "Data Decorrenza Sospensione"');
		return false;
	}
	
	// Controllo validità Data Fino al
	if (! ControllaDataPassaVuota(data_fine)) {
		alert('Data Fino al non valida');
		return false;
	}
	
	// Controlla che la Data "Fino al" si maggiore della "Data Decorrenza Sospensione" 
	if (CompareDate(data_fine,data_inizio)) {
		alert('La data "Fino al" deve essere maggiore della "Data Decorrenza Sospensione"');
		return false;
	}

	// Controllo campo Numero Giorni
	campoScelta = document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_FLAG_RECUPERO_SS%>;
	if (campoScelta) {
		if (campoScelta[1] != null && campoScelta[1].checked == true) {
			if (document.InserisciDecretoSospensioneSS.<%=ICostantiDepositoDecreto.CAMPO_GIORNI_RECUPERO_SS%>.value == '') {
				alert('Il campo Numero Giorni deve essere impostato');
				return false;
			}
			// Controlla il campo Data Decorrenza Sospensione per vedere se è vuoto
	    	if (chk_valore_data_inizio == false) {
	    		alert('Per impostare il campo Numero Giorni è necessario impostare la Data Decorrenza Sospensione');
				return false;
	    	}
	    	// Controlla il campo "Periodo Sospensione" e "Fino al" per vedere se sono vuoti
	    	if (chk_valore_data_fine == false && chk_valore_periodo < 1) {
	    		alert('Per impostare il campo Numero Giorni è necessario impostare il "Periodo Sospensione" o "Fino al"');
				return false;
			}
       	}
	}
<%
// MEV_2023-35: aggiunto contenuto "Sospensione Esecuzione Pena Accessoria" (x2)
if (ICostantiDepositoOrdinanzaPc.COD_OGGETTO_SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE_UDS.equalsIgnoreCase(contenuto)
		|| ICostantiDepositoOrdinanzaPc.COD_OGGETTO_SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE_TDS.equalsIgnoreCase(contenuto)) {
%>
    var tipoPA = document.InserisciDecretoSospensioneSS.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA%>.value;
    if (tipoPA == "-") {
		alert('Tipo Pena Accessoria Obbligatorio');
		document.InserisciDecretoSospensioneSS.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA%>.focus();
		return false;
    }
<%
}
%>

	return true;
}
</script>
</head>

<body class="corpo" onload="visualizza_campo()">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
		<td class=LBG>
			<font class="label">Funzione : </font><font class="campo"><%=lFunctionName%></font>
	 	</td>
	</tr>
	<tr>
	 	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
	</tr>
</table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciDecretoSospensioneSS">
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
		<td class="l" width="20%"> Data Emissione</td>
		<td class="l"><%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
	</tr>
 	<tr>
		<td class="l"> Eventuale motivazione</td>
		<td class="l"><Textarea title="Note" name="<%=ICostantiDepositoDecreto.CAMPO_NOTE%>" cols="88" rows="3"></Textarea></td>
  	</tr>
</table>
<br>
<table cellspacing="2" cellpadding="2" width="95%">
    <tr>
        <td class="Titolo" colspan="2">Specificare esito per ciascun oggetto:</td>
    </tr>
    <tr>
        <td class="l" width="20%">Oggetto</td>
        <td class="l">Esito <font class=ob>(*)</font></td>
    </tr>
<%
// 2008-02-21 commentato poichè come richiesto il controllo è da effettuarsi sul cod contenuto
for (int i = 0; i < tenori.length; i++) {
	// 2008-02-21 commentato poichè come richiesto il controllo è da effettuarsi sul cod contenuto
	// controllo per visualizzare i campi di inseriemnto per i giorni da recuperare
	// if (tenori[i].getCodOggettoTenore().equals("2380")
	// 		|| tenori[i].getCodOggettoTenore().equals("2381")
	// 		|| tenori[i].getCodOggettoTenore().equals("2382"))
	//	chk_Oggetto = true;
%>
	<tr>
	 	<td class="l">
	   		<input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE%>" value="<%=tenori[i].getDescrOggettoTenore()%>" readonly size="100">
			<input Title="Cod Oggetto" type="hidden" name="<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>" value="<%=tenori[i].getCodOggettoTenore()%>">
			<input Title="Cod Dettaglio Oggetto" type="hidden" name="<%=ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=tenori[i].getCodDettaglioOggetto()%>">
		</td>
	  	<td class="l">
	   		<select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>"><%=esiti[i]%></select>
	 	</td>
	</tr>
<%
}
%>            
</table>
<br>
<%
// MEV_2023-35: aggiunto controllo preventivo per far vedere la tabella
if (!("U134".equals(contenuto) || "U137".equals(contenuto)
		|| ICostantiDepositoOrdinanzaPc.COD_OGGETTO_SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE_UDS.equals(contenuto)
		|| ICostantiDepositoOrdinanzaPc.COD_OGGETTO_SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE_TDS.equals(contenuto))) {
%>
<table cellspacing="2" cellpadding="2">
    <tr>
		<td class="l"><%=labelUfficio%> Competente</td>
      	<td class="l">
        	<input Title="<%=labelUfficio%>" name="<%=ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_TDS_COMP%>" value="" size="35">
       		<a href="Javascript:ListaProcure('InserisciDecretoSospensioneSS','<%=ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_TDS_COMP%>');">
       			<img src="/images/filefolder.gif" border="0">
       		</a>
        </td>
	</tr>
</table>
<br>
<%
}
// MEV_2023-35: aggiunto contenuto "Sospensione Esecuzione Pena Accessoria" (x2)
if (ICostantiDepositoOrdinanzaPc.COD_OGGETTO_SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE_UDS.equalsIgnoreCase(contenuto)
		|| ICostantiDepositoOrdinanzaPc.COD_OGGETTO_SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE_TDS.equalsIgnoreCase(contenuto)) {
%>
<table cellspacing="2" cellpadding="2" width="95%">
    <tr>
        <td class="Titolo" colspan="2">Pena Accessoria Sospesa</td>
    </tr>
	<tr>
		<td class="l" width="20%">Tipo di Pena Accessoria</td>
		<td class="l">
			<select class="small" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA%>"><%=TipoPenaAccessoria%></select>
		</td>
	</tr>
	<tr>
		<td class="l">Tipo Durata</td>
		<td class="l">
			<select name="<%=ICostantiPenaAccessoria.CAMPO_DURATA%>"><%=DurataPeneAccessorie%></select>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Durata&nbsp;&nbsp;&nbsp;
			Anni <input maxlength="2" size="2" Title="Anni Durata" value="" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_NUM_ANNI%>" onkeypress="return TicTabNumField(this,event)">
			Mesi <input maxlength="2" size="2" Title="Mesi Durata" value="" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_NUM_MESI%>" onkeypress="return TicTabNumField(this,event)">
			Giorni <input maxlength="2" size="2" Title="Giorni Durata" value="" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_NUM_GIORNI%>" onkeypress="return TicTabNumField(this,event)">
		</td>
	</tr>
    <tr>
        <td class="Titolo" colspan="2">&nbsp;</td>
    </tr>
</table>
<br>
<%
}
%>
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
		<td class="l" width="20%">Data Decorrenza Sospensione</td>
		<td class="L">
      		<input title="Giorno Data Decorrenza Sospensione" value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>" <%=IWebConstants.UTIL_DATA%>>
			/
			<input title="Mese Data Decorrenza Sospensione" value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS%>" <%=IWebConstants.UTIL_DATA%>>
			/
			<input title="Anno Data Decorrenza Sospensione" value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
    	</td>
	</tr>
	<tr>
		<td class="l">Periodo Sospensione</td>
		<td class="L">
			Anni <input title="Anni Sospensione" size="2" maxlength="2" value="" type="text" name="<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>" onkeypress="return TicTabNumField(this,event)">
			Mesi <input title="Mesi Sospensione" size="2" maxlength="2" value="" type="text" name="<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS%>" onkeypress="return TicTabNumField(this,event)">
			Giorni <input title="Giorni Sospensione" size="2" maxlength="2" value="" type="text" name="<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS%>" onkeypress="return TicTabNumField(this,event)">
      	</td>
	</tr>
	<tr>
		<td class="l">Fino al</td>
		<td class="L">
      		<input title="Giorno Data Scadenza Sospensione" value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>" <%=IWebConstants.UTIL_DATA%>>
			/
			<input title="Mese Data Scadenza Sospensione" value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS%>" <%=IWebConstants.UTIL_DATA%>>
			/
			<input title="Anno Data Scadenza Sospensione" value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
    	</td>
	</tr>
</table>
<%
// 2008-02-21 commentato poichè come richiesto il controllo è da effettuarsi sul cod contenuto
// if (chk_Oggetto)
boolean chkContenuto = false;
// Come richiesto il 28/01/2008 Si inibisce la visualizzazione dei giorni da recuperare per il codice contenuto
// pari a U060 e lo si abilita per il codice U061 e/o eventuali altri codici non specificati.
if ("U060".equalsIgnoreCase(contenuto) // Sopensione Esecuzione Sanzioni Sostitutive
		|| "U134".equalsIgnoreCase(contenuto) // MEV_2023-35: aggiunto contenuto "Sopensione Esecuzione Pene Sostitutive"
		|| "U137".equalsIgnoreCase(contenuto) // MEV_2023-35: aggiunto contenuto "Sospensione lavoro di pubblica utilita' sostitutivo"
		|| ICostantiDepositoOrdinanzaPc.COD_OGGETTO_SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE_UDS.equalsIgnoreCase(contenuto) // MEV_2023-35: aggiunto contenuto "Sospensione Esecuzione Pena Accessoria" (x2)
		|| ICostantiDepositoOrdinanzaPc.COD_OGGETTO_SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE_TDS.equalsIgnoreCase(contenuto))
	chkContenuto = true;
if (!chkContenuto) {
%>

<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
		<td class="l">
			da non recuperare <input type="radio" name="<%=ICostantiDepositoDecreto.CAMPO_FLAG_RECUPERO_SS%>" value="N" checked onClick="javascript:visualizza_campo();">
			da recuperare <input type="radio" name="<%=ICostantiDepositoDecreto.CAMPO_FLAG_RECUPERO_SS%>" value="S" onClick="javascript:visualizza_campo();">
		<td>
		<td class="l">
			<div id="numGiorni" style="display:none">Numero Giorni <input title="Numero giorni" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoDecreto.CAMPO_GIORNI_RECUPERO_SS%>" onkeypress="return TicTabNumField(this,event)"></div>
		<td>		
	</tr>
</table>
<%
}
%>
<br>
<table>
 	<tr>
 		<td>
     		<input class="bottone" type="submit" value="Conferma">
   		</td>
 	</tr>
</table>
 
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>">
<input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_COD_TIPO_DECRETO%>" value="<%=tipo_decreto%>">
<input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%>>
<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>">
<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%>>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("InserisciDecretoSospensioneSS");
// CONTROLLI DATE
// Data Decorrenza Sospensione
frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>","numeric");
frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>","gt=1");
frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>","lt=31");

frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS%>","numeric");
frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS%>","gt=1");
frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS%>","lt=12");

frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS%>","numeric");
frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS%>","gt=1900");

// Data Fino al
frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>","numeric");
frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>","gt=1");
frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>","lt=31");

frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS%>","numeric");
frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS%>","gt=1");
frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS%>","lt=12");

frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS%>","numeric");
frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS%>","gt=1900");

frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>

<jsp:useBean id="contenuto"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"		scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"  	scope="request" class="java.util.Date"/>

<%
TenoreModel[] tenori = (TenoreModel[]) request.getAttribute("tenori");
String[] esiti	= (String[]) request.getAttribute("esiti");

String lAction = new String();
lAction = "siap.sius.depositodecreto.action.ActInserisciEmissioneDecretoDeposito";

// MEV_2023-35: aggiunti controlli sul contenuto
String tipoContenuto = "";
if ("U135".equals(contenuto))
	tipoContenuto = " Pene Sostitutive";
%>

<html>
<head>
<title>[S.I.E.S.] - Emissione Decreto di Licenza<%=tipoContenuto%></title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="/html/verifyCombo.js"></script>

<script language="JavaScript">
// STUB 21/07/2004 Controllo obbligatorietà esiti.
function Verify() {
	var ritorno = true;
	var lEsiti = document.InserisciDecretoLicenza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
	if (!VerifyCombo(lEsiti,"Esito"))
		return false;
	var esito = document.InserisciDecretoLicenza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.options[document.InserisciDecretoLicenza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.options.selectedIndex].text;
	esito = esito.toUpperCase();
	if (esito == 'CONCEDE') {
    	ritorno = controlloCampi();
 	} else {
		// ritorno = false;
		// N.ro giorni a 0 indica che la licenza non è stata concessa
		// N.ro mesi a 0 indica che la licenza non è stata concessa
		document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI%>.value = 0;
		document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_MESI%>.value = 0;
  	}
	return ritorno;
}

function controlloCampi() {
	var ritorno = false;
   	ritorno = controlloGiorni();
   	if (ritorno)
		ritorno = controlloDate();
	// if (ritorno)
	//	ritorno = controlloOrologio();
	return ritorno;
}

/* Controllo del campo n.ro giorni licenza */
function controlloGiorni() {
	var ritorno = false;
	var giorni = 0;
	var ore = "";
	var mesi = 0;
	giorni = document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI%>.value;
	ore = document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE%>.value;
	mesi = document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_MESI%>.value;
	// 21/02/2012 Segnalazione TDS Napoli - Gestione di un Limite giorni di Licenza pari a 180 nel caso di
	// Misure di Sicurezza - Decreto di Licenza finale per Esperimento. 
   codOggettoTenore = document.InserisciDecretoLicenza.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>.value;
 	var limiteGiorni = 45;
    if (codOggettoTenore == '2450')
  		limiteGiorni = 180;
	if (mesi == "" && giorni != "" && (giorni < 0 || giorni > limiteGiorni))
		alert("Il numero di giorni concessi deve essere compreso tra 1 e "+limiteGiorni);
    else if (mesi == "" && giorni == "" && (ore == ""))
     	alert("Valorizzare i giorni e/o le ore di licenza");
	else
		ritorno = true;
	return ritorno;
}

/* controllo correttezza di un campo di tipo ora */
function controlloOra(ora, minuti) {
	var ritorno = false;
    // L'ora è opzionale
    if (ora == "")
		ritorno = true;
    else if (ora >= 0 && ora < 24)
        ritorno = controlloMinuti(minuti);
    else
        alert("L'ora deve essere espressi da un numero compreso tra 0 e 23");
    return ritorno;
 }

/* controllo correttezza di un campo di tipo minuti */
function controlloMinuti(min) {
	var ritorno = false;
    if (min == "")
        ritorno = true;
    else if (min >= 0 && min < 60)
        ritorno = true;
    else
        alert("I minuti devono essere espressi da un numero compreso tra 0 e 59");
	return ritorno;
}

/* Controllo dei campi ore e minuti */
function controlloOrologio() {
	var ret = true;
	var ora0 = document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_ORA_INIZIO%>.value;
	var ora1 = document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_ORA_FINE%>.value;
	var minuti0 = document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_MINUTI_INIZIO%>.value;
	var minuti1 = document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_MINUTI_FINE%>.value;
	if (minuti0 == "")
		document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_MINUTI_INIZIO%>.value = "00";
	if (minuti1 == "")
		document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_MINUTI_FINE%>.value = "00";
	ret = controlloOra(ora0, minuti0);
/*      if ( ret == true)
         ret = controlloMinuti(minuti0); */
	if (ret == true)
		ret = controlloOra(ora1,minuti1);
/*      if ( ret == true )
         ret = controlloMinuti(minuti1); */
	return ret;
}

/* Controllo delle date    */
function controlloDate() {
	var ret = true;
	var gg0 = FillDM(document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_INIZIO%>.value);
	var mm0 = FillDM(document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_INIZIO%>.value);
	var aa0 = document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_INIZIO%>.value;
	var ora0 = document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_ORA_INIZIO%>.value;
	var minuti0 = document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_MINUTI_INIZIO%>.value;
	var dataIni = gg0 + "/" + mm0 + "/" + aa0;
	var gg1 = FillDM(document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_FINE%>.value);
	var mm1 = FillDM(document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_FINE%>.value);
	var aa1 = document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_FINE%>.value;
	var ora1 = document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_ORA_FINE%>.value;
	var minuti1 = document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_MINUTI_FINE%>.value;
	var dataFine = gg1 + "/" + mm1 + "/" + aa1;
	if (dataIni.length == 2 && dataFine.length == 2) {
		// Date non più obbligatorie
		ret = true;
		resetOrologio();
   	} else if (dataIni.length == 2) {
		ret = false;
		alert ("Data di inizio licenza mancante");
	} else if (dataFine.length == 2) {
		ret = false;
		alert ("Data di fine licenza mancante");
   	} else if (ControllaData (dataIni) == false) {
		/* entrambe le date valorizzate */
     	ret = false;
     	alert ("Errore nella data : " + dataIni);
   	} else if (ControllaData (dataFine) == false) {
		ret = false;
     	alert ("Errore nella data : " + dataFine);
   	} else if (CompareDate(dataIni,dataFine) == false) {
		ret = false;
     	alert ("Data di Fine minore di Data inizio licenza");
   	} else if (controlloOrologio() == false) {
		ret = false;
   	} else {
		/* OK Date presenti */
		var DataIni = new Date(aa0, mm0-1, gg0, ora0, minuti0);
		var DataFine  = new Date(aa1, mm1-1, gg1, ora1, minuti1);
	}
   	return ret;
}

function resetOrologio() {
	document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_ORA_INIZIO%>.value = "";
	document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_ORA_FINE%>.value= "";
	document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_MINUTI_INIZIO%>.value= "";
	document.InserisciDecretoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_MINUTI_FINE%>.value= "";
}
</script>
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
      		<font class="label">Funzione : </font> <font class="campo">Emissione Decreto Licenza<%=tipoContenuto%></font>
      	</td>
    </tr>
    <tr>
		<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
</table>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciDecretoLicenza">
<table width="35%">
	<tr>
	  	<td class="l" width="30%">Data Emissione</td>
	  	<td class="l" width="70%"><%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
	</tr>
</table>
<br>
<table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr>
        <td class="Titolo" colspan="6"> Specificare esito per ciascun oggetto: </td>
    </tr>
    <tr>
        <td class="l" colspan=2 > Oggetto </td>
        <td class="l" colspan=2 > Esito </td>
    </tr>
<%
for (int i = 0; i < tenori.length; i++) {
%>
	<tr>
 		<td class="l"colspan=2 >
			<input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE%>" value="<%=tenori[i].getDescrOggettoTenore()%>" readonly size="60%">
			<input Title="Cod Oggetto" type="hidden" name="<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>" value="<%=tenori[i].getCodOggettoTenore()%>">
			<input Title="Cod Dettaglio Oggetto" type="hidden" name="<%=ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=tenori[i].getCodDettaglioOggetto()%>">
		</td>
  		<td class="l"colspan=2 >
   			<select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>"><%=esiti[i]%></select>
  		</td>
	</tr>
<%
}
%>
</table>
<br>
<table cellspacing="2" cellpadding="2" style="width: 90%;">
	<tr>
		<td class="l">Durata Licenza</td>
		<td class="l"> mesi &nbsp;
			<input Title="Mesi Licenza" name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_MESI%>" value="" size="2" maxlength="3">
			&nbsp;&nbsp;&nbsp;giorni &nbsp;
			<input Title="Giorni Licenza" name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI%>" value="" size="2" maxlength="4">
			&nbsp;&nbsp;&nbsp;e/o ore
			<input Title="Ore Licenza" name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE%>" value="" size="2" maxlength="4">
		</td>	
	</tr>
	<tr>
		<td class="l">Data inizio licenza</td>
		<td class="L">(gg/mm/aa)
		  	<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_INIZIO%>" onBlur="javascript:value=FillDM(value)"> /
			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_INIZIO%>" onBlur="javascript:value=FillDM(value)"> /
			<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_INIZIO%>">
			&nbsp;&nbsp; (hh,mm)
			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ORA_INIZIO%>" onBlur="javascript:value=FillDM(value)"> ,
			<input value="" type="text" size="2" maxlength="4" name="<%=ICostantiLicenzaLibanticipata.CAMPO_MINUTI_INIZIO%>" onBlur="javascript:value=FillDM(value)">
  		</td>
	</tr>
	<tr>
		<td class="l">Data fine licenza</td>
		<td class="L">(gg/mm/aa)
			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_FINE%>" onBlur="javascript:value=FillDM(value)"> /
			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_FINE%>" onBlur="javascript:value=FillDM(value)"> /
			<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_FINE%>">
			&nbsp; &nbsp;(hh,mm)
			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ORA_FINE%>" onBlur="javascript:value=FillDM(value)"> ,
			<input value="" type="text" size="2" maxlength="4" name="<%=ICostantiLicenzaLibanticipata.CAMPO_MINUTI_FINE%>" onBlur="javascript:value=FillDM(value)">
		</td>
	</tr>
	<tr>
		<td class="l">Luogo svolgimento licenza</td>
		<td class="l">
    		<input Title="Luogo" name="<%=ICostantiLicenzaLibanticipata.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>" value="" size="75">
    	</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
    <tr>
      	<td class="l">Inserimento Prescrizioni <input value="06" type="checkbox" name="<%=ICostantiDepositoDecreto.CAMPO_CK_PRESCRIZIONI%>"></td>
	</tr>
 	<tr><td>&nbsp;</td></tr>
	<tr>
     	<td>
       		<input class="bottone" type="submit" value="Conferma">
		</td>
	</tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>">
<input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_COD_TIPO_DECRETO%>" value="<%=tipo_decreto%>">
<input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione, "dd/MM/yyyy")%>>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("InserisciDecretoLicenza");
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
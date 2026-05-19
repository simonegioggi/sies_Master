<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>

<html>
<head>
<title> [S.I.E.S.] - Relazione Trimestrale Permessi/Licenze</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
function Verify() {
	var ritorno = false;
	ritorno = controlloDate();
	return ritorno;
}

// Controllo delle date.
function controlloDate() {
	var ret = true;
	var docRef = document.LoadRelazioneTrimestralePermessoLicenza;
	var gg = FillDM(docRef.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO_INIZIALE%>.value);
	var mm = FillDM(docRef.<%=ICostantiDepositoDecreto.CAMPO_MESE_DATA_DEPOSITO_INIZIALE%>.value);
	var aa = docRef.<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_DEPOSITO_INIZIALE%>.value;
	var dataDepositoIniziale = gg + "/" + mm + "/" + aa;
	gg = FillDM(docRef.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO_FINALE%>.value);
	mm = FillDM(docRef.<%=ICostantiDepositoDecreto.CAMPO_MESE_DATA_DEPOSITO_FINALE%>.value);
	aa = docRef.<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_DEPOSITO_FINALE%>.value;
	var dataDepositoFinale = gg + "/" + mm + "/" + aa;
   	if (dataDepositoIniziale.length < 10) {
   		ret = false;
     	alert ('Data di deposito iniziale mancante!');
   	} else if (!ControllaData (dataDepositoIniziale)) {
		ret = false;
		alert ('Data dei deposito iniziale non valida : ' + dataDepositoIniziale);
   	}
	if (!ret)
		docRef.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO_INIZIALE%>.focus();	
	// Controlla data deposito finale.
	if (ret) {	
		if (dataDepositoFinale.length < 10) {
			ret = false;
			alert ('Data di deposito finale mancante!');
    	} else if (!ControllaData (dataDepositoFinale))	{
			ret = false;
			alert ('Data dei deposito finale non valida : ' + dataDepositoFinale);
    	}
    	if (!ret)
			docRef.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO_FINALE%>.focus();	
	}
	if (ret) {
		if (CompareDate(dataDepositoIniziale,dataDepositoFinale) == false) {
			ret = false;
			alert ('Data Iniziale maggiore della Data Finale');
 		}     		
 		if (!ret)
			docRef.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO_INIZIALE%>.focus();	
	}
	if (ret) {
		ret = differenzaDate();
 		if (!ret)
			docRef.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO_INIZIALE%>.focus();	
	}
	return ret;
}
  
function differenzaDate() {
  	var docRef = document.LoadRelazioneTrimestralePermessoLicenza;
	dataInizio = new Date();
	dataInizio.setYear(docRef.<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_DEPOSITO_INIZIALE%>.value);
	dataInizio.setMonth(FillDM(docRef.<%=ICostantiDepositoDecreto.CAMPO_MESE_DATA_DEPOSITO_INIZIALE%>.value)-1);
	dataInizio.setDate(FillDM(docRef.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO_INIZIALE%>.value));
  	dataFine = new Date();      	
	dataFine.setYear(docRef.<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_DEPOSITO_FINALE%>.value);      	
	dataFine.setMonth(FillDM(docRef.<%=ICostantiDepositoDecreto.CAMPO_MESE_DATA_DEPOSITO_FINALE%>.value)-1);
	dataFine.setDate(FillDM(docRef.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO_FINALE%>.value));

	// 20260112 [SG] : segnalazione di UM dal 01/07/2025 al 31/12/2025 --> diff = 183 --> diff > 183
// 	var diff = dataFine.getTime() - dataInizio.getTime();
// 	diff = Math.floor(diff / (1000 * 60 * 60 * 24));
// 	if (diff > 182.5) {
//   	alert ('Il range di date non può superare i 6 mesi!');
// 	 	return false;
// 	}
	// Calcolo della differenza in mesi
	var mesiDiff = (dataFine.getFullYear() - dataInizio.getFullYear()) * 12;
	mesiDiff += dataFine.getMonth() - dataInizio.getMonth();
	// Se la differenza in mesi è maggiore di 6,
	// oppure se sono esattamente 6 mesi, controlla che il giorno finale non sia maggiore del giorno iniziale
	if (mesiDiff > 6 ||
			(mesiDiff == 6 && dataFine.getDate() >= dataInizio.getDate())) {
  		alert ('Il range di date non pu\u00F2 superare i 6 mesi!');
	 	return false;
	}
	return true;
}
</script>  	
</head>
<body class="corpo" onLoad="javascript:document.LoadRelazioneTrimestralePermessoLicenza.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO_INIZIALE%>.focus()">
<FORM method="POST" name="LoadRelazioneTrimestralePermessoLicenza" action="<%=IWebConstants.PG_MAIN%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.permesso.action.ActRelazioneTrimestralePermessoLicenza">
<table>
    <tr>
    	<td class="LBG">
    		<a href="Javascript:window.print();">
    			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
    		</a>
    	</td>
		<td class="LBG">
	      	<font class="label">Funzione :</font>&nbsp;
	      	<font class="campo">Ricerca Provvedimenti di Concessione Permesso Licenza Depositati</font>
      	</td>
    </tr>
</table>
<br>
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
  		<td class="Titolo" colspan="6">Intervallo date di deposito dei provvedimenti di concessione permessi/licenze:</td>
  	</tr>    
    <tr>    	
    	<td class="L" width="20%"><font class="label">Data Iniziale</font></td>
		<td class="l">
	      	<input type="text" title="Giorno Iniziale" 
	      			 name="<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO_INIZIALE%>" 
	      			 maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" 
	      			 onkeypress="return TicTabNumField(this,event)"  
	      			 onBlur="javascript:value=FillDM(value)">-
	      	<input type="text" title="Mese Iniziale" 
				name="<%=ICostantiDepositoDecreto.CAMPO_MESE_DATA_DEPOSITO_INIZIALE%>" 
				maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
				onkeypress="return TicTabNumField(this,event)"
				onBlur="javascript:value=FillDM(value)">-
	      	<input type="text" title="Anno Iniziale" 
				name="<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_DEPOSITO_INIZIALE%>" 
				maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
				onkeypress="return TicTabNumField(this,event)"  
				onBlur="javascript:value=FillYear(value)">
		</td>
      	<td class="L"><font class="label">Data Finale</font></td>
      	<td class="l">
      		<input type="text" title="Giorno Finale" 
				name="<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO_FINALE%>" 
				maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" 
				onkeypress="return TicTabNumField(this,event)"  
				onBlur="javascript:value=FillDM(value)">-
        	<input type="text" title="Mese Finale" 
				name="<%=ICostantiDepositoDecreto.CAMPO_MESE_DATA_DEPOSITO_FINALE%>" 
				maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
				onkeypress="return TicTabNumField(this,event)"
				onBlur="javascript:value=FillDM(value)">-
        	<input type="text" title="Anno Finale" 
				name="<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_DEPOSITO_FINALE%>" 
				maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
				onkeypress="return TicTabNumField(this,event)"  
				onBlur="javascript:value=FillYear(value)">
      	</td>
    </tr>
    <tr><td>&nbsp;</td></tr>
  	<tr>
		<td class="L">Selezione oggetto del provvedimento:</td>
		<td class="L">Permesso</td>
		<td class="l">
			<input type="radio" name="tipoRicerca" value="ALLP" checked>Tutti <br>
			<input type="radio" name="tipoRicerca" value="PN">Necessità <br>
			<input type="radio" name="tipoRicerca" value="PP">Premio <br>
			<input type="radio" name="tipoRicerca" value="PI">Internati
		</td>
  	</tr>
  	<tr>
		<td>&nbsp;</td>
		<td class="L">Licenza</td>
		<td class="l">
			<input type="radio" name="tipoRicerca" value="LC">Semilibertà <br>
			<input type="radio" name="tipoRicerca" value="LI">Internati <br>
			<%-- MEV_2023-35: aggiungo Licenza pene sostitutive (LP) --%>
			<input type="radio" name="tipoRicerca" value="LP">Pene Sostitutive
		</td>
  	</tr>
    <tr><td>&nbsp;</td></tr>
	<tr>
    	<td><input class="bottone" type="submit" name="RICERCA" value="Ricerca"></td>
  	</tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadRelazioneTrimestralePermessoLicenza");
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
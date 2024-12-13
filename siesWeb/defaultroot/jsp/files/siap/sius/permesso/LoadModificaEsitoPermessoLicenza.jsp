<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.permesso.action.ICostantiPermesso"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>

<jsp:useBean id="permessoDepDecr"	scope="request" class="siap.sius.permesso.model.DepositoDecretoMotivazioniLicenzaModel"/>
<jsp:useBean id="esiti"				scope="request" class="java.lang.String"/>
<%
LicenzaLibAnticipataModel lLic = permessoDepDecr.getLicenza(); 
String lDescrTipoLicenza = "";

if (lLic.getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.LICENZA))
	lDescrTipoLicenza = "Licenza";
else if (lLic.getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.LICENZA_INTERNATO))
	lDescrTipoLicenza = "Licenza per Internato";
// MEV_2023-35: aggiunti codici
else if (lLic.getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.LICENZA_PENE_SOSTITUTIVE))
	lDescrTipoLicenza = "Licenza Pene Sostitutive";
else if (lLic.getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.ESCLUSIONE_COMPUTO_LICENZA))
	lDescrTipoLicenza = "Esclusione Computo Licenza";
else if (lLic.getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.REVOCA_LICENZA))
	lDescrTipoLicenza = "Revoca Licenza";
else if (lLic.getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.PERMESSO_PREMIO))
	lDescrTipoLicenza = "Permesso";
else if (lLic.getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.PERMESSO_INTERNATO))
	lDescrTipoLicenza = "Permesso per Internato";
// MEV_2023-35: aggiunti codici
else if (lLic.getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.REVOCA_PERMESSO))
	lDescrTipoLicenza = "Revoca Permesso";
else if (lLic.getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.ESCLUSIONE_COMPUTO_PERMESSO))
	lDescrTipoLicenza = "Esclusione Computo Permesso";

int lNumGiorniConcessi = lLic.getNumeroGiorni() == null ? 0 : lLic.getNumeroGiorni().intValue();
int lNumOreConcesse = lLic.getNumeroOre() == null ? 0 : lLic.getNumeroOre().intValue();
int lTotOreConcesse = ( lNumGiorniConcessi * 24 ) + lNumOreConcesse;

// Recupera la data di deposito
permessoDepDecr.getDepositoDecreto().getDataDeposito();
%>	

<html>
<head>
<title>[S.I.E.S.] - Esito <%=lDescrTipoLicenza%></title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
// Controllo di verifica.
function Verify() {
	if (!controlloGiorni())
		return false;
	if (!controlloDate())
		return false;
   	return true;
}

function controlloGiorni() {
	var ret 			= true;
	var docRef			= document.LoadModificaEsitoPermessoLicenza;
	var totOreConcesse 	= parseInt(<%=lTotOreConcesse%>, 10);
	var giorniNoFruiti 	= parseInt(docRef.<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI_NO_FRUITI%>.value, 10);
	var oreNoFruite 	= parseInt(docRef.<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE_NO_FRUITE%>.value, 10);
	var totOreNoFruite 	= (giorniNoFruiti * 24) + oreNoFruite;

	if (totOreNoFruite > totOreConcesse) {
		ret = false;
		alert ('Giorni ed ore non fruite maggiori di quelle concesse.');
		docRef.<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI_NO_FRUITI%>.focus();
	}		         
	return ret;
}

// Controllo delle date.
function controlloDate() {
	var ret 	= true;
	var docRef	= document.LoadModificaEsitoPermessoLicenza;
	var gg 		= FillDM(docRef.<%=ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_ANNOTAZIONE_ESITO%>.value);
	var mm 		= FillDM(docRef.<%=ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_ANNOTAZIONE_ESITO%>.value);
	var aa 		= docRef.<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_ANNOTAZIONE_ESITO%>.value;
	
	var dataAnnotazione = gg + "/" + mm + "/" + aa;
	var dataSistema 	= '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
	var dataDeposito 	= '<%=DateUtils.getDateToString(permessoDepDecr.getDepositoDecreto().getDataDeposito(), "dd/MM/yyyy")%>';

    if (dataAnnotazione.length < 10) {
		ret = false;
       	alert ('Data di annotazione esito mancante!');
    } else if (!ControllaData(dataAnnotazione)) {
       	ret = false;
       	alert ('Data di annotazione esito non valida : ' + dataAnnotazione);
    } else if (!CompareDate(dataAnnotazione,dataSistema)) {
   		ret = false;
   		alert('Data annotazione esito maggiore della data attuale!');
	} else if (!CompareDate(dataDeposito,dataAnnotazione)) {
		ret = false;
   		alert('Data annotazione esito deve essere maggiore o uguale alla data deposito!');
	}

	if (!ret)
		docRef.<%=ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_ANNOTAZIONE_ESITO%>.focus();
	return ret;
}
</script>
</head>

<body class="corpo" onLoad="javascript:document.LoadModificaEsitoPermessoLicenza.<%=ICostantiLicenzaLibanticipata.CAMPO_COD_ESITO%>.focus()">
<table>
  	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
      	<td class="LBG">
      		<font class="label">Funzione : </font>
      		<font class="campo">Modifica Esito <%=lDescrTipoLicenza%></font>
      	</td>
		<!-- Inserisce il pulsante di ritorno -->
		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	</tr>
</table>

<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
<br>
<jsp:include page="<%=ICostantiPermesso.PG_SINTESI_DATI_PERMESSOLICENZA%>"/>    	
<br>
<FORM method=post action="<%= IWebConstants.PG_MAIN %>" name ="LoadModificaEsitoPermessoLicenza" >
<table>  		
	<tr>
		<td class="l">Esito <%=lDescrTipoLicenza%>&nbsp;<font class="ob">(*)</font></td>
		<td class="L">
			<select title="esito" name="<%=ICostantiLicenzaLibanticipata.CAMPO_COD_ESITO%>"><%=esiti%></select>
  		</td>
	</tr>
	<tr>
		<td class="l">GIORNI e/o ORE non fruite</td>
		<td class="L">
			giorni <input value="<%=StringUtils.toStringJSP(lLic.getNumeroGiorniNoFruiti())%>" 
				type="text" size="2" maxlength="2" 
				name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI_NO_FRUITI%>" 
				onFocus="javascript:textboxSelect(this)" 
				onkeypress="return TicTabNumField(this,event)"> /
			ore	<input value="<%=StringUtils.toStringJSP(lLic.getNumeroOreNoFruite())%>" 
				type="text" size="2" maxlength="2" 
				name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE_NO_FRUITE%>" 
				onFocus="javascript:textboxSelect(this)" 
				onkeypress="return TicTabNumField(this,event)">
		</td>
	</tr>
	<tr>
		<td class="l">Data annotazione esito&nbsp;<font class="ob">(*)</font>&nbsp;(gg/mm/aa)</td>
		<td class="L">
			<input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lLic.getDataAnnotazioneEsito(), "dd"))%>" 
				type="text" size="2" maxlength="2" 
				name="<%=ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_ANNOTAZIONE_ESITO%>" 
				onFocus="javascript:textboxSelect(this)" 
				onkeypress="return TicTabNumField(this,event)"  
				onBlur="javascript:value=FillDM(value)" > /
			<input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lLic.getDataAnnotazioneEsito(), "MM"))%>" 
				type="text" size="2" maxlength="2" 
				name="<%=ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_ANNOTAZIONE_ESITO%>" 
				onFocus="javascript:textboxSelect(this)" 
				onkeypress="return TicTabNumField(this,event)"  
				onBlur="javascript:value=FillDM(value)" > /
			<input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lLic.getDataAnnotazioneEsito(), "yyyy"))%>" 
				type="text" size="4" maxlength="4" 
				name="<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_ANNOTAZIONE_ESITO%>" 
				onFocus="javascript:textboxSelect(this)" 
				onkeypress="return TicTabNumField(this,event)" 
				onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
  		<td>
  			<input class="bottone" type="submit" value="Conferma">
   			<input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.sius.permesso.action.ActModificaEsitoPermessoLicenza">
			<input type="HIDDEN" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA%>" value="<%=lLic.getIdLicenzaLibanticipata()%>">
		</td>
	</tr>
</table>
</FORM>

<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadModificaEsitoPermessoLicenza");
frmvalidator.setAddnlValidationFunction("Verify");
// Controllo campo titolo relazione.
// frmvalidator.addValidation("<--%= ICostantiRelazione.CAMPO_NOTE%>","req", "Il campo titolo relazione è obbligatorio");	
</script>
</body>
</html>
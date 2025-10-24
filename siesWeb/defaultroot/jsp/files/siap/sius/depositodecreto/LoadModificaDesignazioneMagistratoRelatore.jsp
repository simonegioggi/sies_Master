<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2019-09: creata nuova pagina di caricamento dati --%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="depositoDecretoMotivazioni" 	scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="magistratorelatore"   			scope="request" class="siap.sius.magistratorelatore.model.MagistratoRelatoreModel"/>
<jsp:useBean id="codContenuto"          		scope="request" class="java.lang.String"/>
<jsp:useBean id="codOggetti"            		scope="request" class="java.lang.String"/>
<jsp:useBean id="codDettagli"   				scope="request" class="java.lang.String"/>
<jsp:useBean id="descOggetti"          			scope="request" class="java.lang.String"/>
<jsp:useBean id="dataArrivoCancelleria"   		scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Modifica Decreto Designazione Magistrato Relatore</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
var desktop;

// Funzione dei controlli formali della form
function Verify() {
	// Controllo obbligatorieta' campo magistrato relatore
	if (document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value == ""
			|| document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value == "-") {
    	alert('Magistrato Relatore obbligatorio!');
    	document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_COGNOME%>.focus();
    	return false;
	} else {
<%
String codMagistratoOld = "";
if (magistratorelatore != null && (magistratorelatore.getEsperto() != null || magistratorelatore.getMagistrato() != null)) {
	if (magistratorelatore.getEsperto() != null)
		codMagistratoOld = magistratorelatore.getEsperto().getIdEsperto().toString();
	else
		codMagistratoOld = magistratorelatore.getMagistrato().getCodMagistrato();
}
if (Utils.isPresent(codMagistratoOld)) {
%>
		if (document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value != '<%=codMagistratoOld%>') {
			if (!confirm("Attenzione: si sta inserendo un Magistrato Relatore diverso da quello già assegnato al fascicolo! Procedere con l'inserimento del nuovo Magistrato Relatore?"))
				return false;
		}/* else {
			alert('Attenzione: si sta inserendo lo stesso Magistrato Relatore già assegnato al fascicolo!');
			return false;
		}*/
<%
}
%>
	}

	// Controllo validità data Emissione
	var dataEmissione = document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value + '/' +
    					document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value + '/' +
    					document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

	var dataSistema = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

  	if (!ControllaData(dataEmissione)) {
	    alert('Data Emissione non valida!');
	    document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
	    return false;
  	}
  	if (!CompareDate(dataEmissione, dataSistema)) {
  		alert('Data Emissione non può essere superiore alla data odierna!');
  		document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
	    return false;
    }
  	var dataArrivoCancelleria = '<%=dataArrivoCancelleria%>';
  	if (!CompareDate(dataArrivoCancelleria, dataEmissione)) {
  		alert('Data Emissione non può essere inferiore alla Data Arrivo in Cancellaria (' + dataArrivoCancelleria + ')!');
  		document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
	    return false;
    }

	// Controllo validità data Termine
	var dataTermine =
		document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_TERMINE_EMISSIONE%>.value + '/' +
		document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiDepositoDecreto.CAMPO_MESE_DATA_TERMINE_EMISSIONE%>.value + '/' +
		document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_TERMINE_EMISSIONE%>.value;

	var dte = false;
	var ngte = false;
	if (dataTermine != "//") {
		dte = true;
	  	if (!ControllaData(dataTermine)) {
		    alert('Data Termine non valida!');
		    document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_TERMINE_EMISSIONE%>.focus();
		    return false;
	  	}
	 	// Data Termine deve essere >= Data Emissione
        if (!CompareDate(dataEmissione, dataTermine)) {
          	alert('Data Termine deve essere maggiore od uguale Data Emissione!');
          	document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_TERMINE_EMISSIONE%>.focus();
          	return false;
        }
	} /*else {
		if (!confirm("Attenzione: data Termine non valorizzata! Procedere con l'inserimento del Decreto di Designazione Magistrato Relatore?"))
			return false;
	}*/

	// Data Termine oppure Numero Giorni Termine obbligatori
	if (document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiDepositoDecreto.CAMPO_NUMERO_GIORNI_TERMINE_EMISSIONE%>.value != "") {
		if (document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiDepositoDecreto.CAMPO_NUMERO_GIORNI_TERMINE_EMISSIONE%>.value == 0) {
			alert('Numero Giorni Termine deve essere maggiore di zero!');
		    document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiDepositoDecreto.CAMPO_NUMERO_GIORNI_TERMINE_EMISSIONE%>.focus();
		    return false;
		}
		ngte = true;
	}
	if (dte && ngte) {
		alert('Valorizzare Data Termine oppure Numero Giorni Termine!');
		document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_TERMINE_EMISSIONE%>.focus();
		return false;
	} else if (!dte && !ngte) {
		if (!confirm("Attenzione: Procedere con l'emissione del Decreto Designazione Magistrato Relatore senza valorizzare Data Termine oppure Numero Giorni Termine?")) {
			document.LoadModificaDesignazioneMagistratoRelatore.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_TERMINE_EMISSIONE%>.focus();
			return false;
		}
	}

   	return true;
}

function ListaMagistratiRelatori(a_formname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}
</script>
</head>

<%
// Imposta l'azione da Chiamare.
String azione = "siap.sius.depositodecreto.action.ActModificaDesignazioneMagistratoRelatore";
%>

<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
		<td class="LBG">
			<font class="label">Funzione:</font>&nbsp;
			<font class="campo">Modifica Decreto Designazione Magistrato Relatore</font>
		</td>
		<!-- BOTTONE DI RITORNO -->
		<%-- <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/> --%>
	</tr>
</table>
<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
<jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>"/>
<jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>"/>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadModificaDesignazioneMagistratoRelatore">
<table cellspacing="4" cellpadding="4" width="95%">
	<tr>
		<td class="l" width="30%">Data Emissione <font class="ob">(*)</font></td>
		<td class="L" colspan="3">
			<input value="<%=DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataEmissione(), "dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="<%=DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataEmissione(), "MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="<%=DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataEmissione(), "yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    	</td>
    </tr>
  	<tr><td colspan="4">&nbsp;</td></tr>
	<tr><td class="Titolo" colspan="4">Oggetti</td></tr>
	<tr>
		<td class="l" colspan="4"><%=descOggetti%></td>
		<!-- ESITO = Designa Magistrato art. 678 1-ter -->
  	</tr>
	<tr><td colspan="4">&nbsp;</td></tr>
	<tr>
		<td class="l">Magistrato Relatore <font class="ob">(*)</font></td>
    	<td class="l"><input value="<%=StringUtils.toStringJSP(magistratorelatore.getMagistrato().getCognome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" readonly="readonly" size="25"></td>
		<td class="l"><input value="<%=StringUtils.toStringJSP(magistratorelatore.getMagistrato().getNome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>" readonly="readonly" size="25"></td>
	  	<td class="l">
	    	<a href="Javascript:ListaMagistratiRelatori('LoadModificaDesignazioneMagistratoRelatore');">
	    		Seleziona dalla lista&nbsp;<img src="/images/filefolder.gif" title="Elenco di tutti i Magistrati Relatori dell'Ufficio" border="0">
	   		</a>
	   		<input type="hidden" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" value="<%=StringUtils.toStringJSP(magistratorelatore.getMagistrato().getCodMagistrato())%>">
	  	</td>
	</tr>
	<tr><td colspan="4">&nbsp;</td></tr>
	<tr>
		<td class="l">Emissione Ordinanza Ammissione Provvisoria / Restituzione Atti al Presidente (entro)</td>
		<td class="L" colspan="3">Data Termine&nbsp;&nbsp;&nbsp;
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataTermineEmissione(), "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_TERMINE_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataTermineEmissione(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_MESE_DATA_TERMINE_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataTermineEmissione(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_TERMINE_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			&nbsp;&nbsp;&nbsp;oppure Numero Giorni Termine&nbsp;&nbsp;&nbsp;
    		<input value="<%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getNumGiorniTermineEmissione())%>" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoDecreto.CAMPO_NUMERO_GIORNI_TERMINE_EMISSIONE%>" onkeypress="return TicTabNumField(this,event)">
    	</td>
	</tr>
    <tr><td colspan="4">&nbsp;</td></tr>
	<tr><td><input class="bottone" type="submit" value="Conferma" onclick="Javascript:return Verify();"></td></tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=azione%>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>" value="<%=codOggetti%>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=codDettagli%>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=codContenuto%>">
<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>">
<input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO%>" value="<%=depositoDecretoMotivazioni.getDepositoDecreto().getIdDepositoDecreto()%>">
</FORM>

<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadModificaDesignazioneMagistratoRelatore");
// Controllo data emissione.
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req", "Il campo Giorno Data Emissione è obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req", "Il campo Mese Data Emissione é obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req", "Il campo Anno Data Emissione é obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno Data Emissione deve essere di 4 caratteri");

frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_TERMINE_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_MESE_DATA_TERMINE_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_TERMINE_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_TERMINE_EMISSIONE%>","minlen=4","La lunghezza del campo Anno Data Termine deve essere di 4 caratteri");
</script>

</body>
</html>
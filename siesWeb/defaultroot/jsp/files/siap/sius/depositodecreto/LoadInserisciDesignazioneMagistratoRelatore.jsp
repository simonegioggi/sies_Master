<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_9: creata nuova pagina di inserimento dati --%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.provvedimento.util.RicercaProvvedimentiUtil"%>

<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />

<jsp:useBean id="contenuto"             scope="request" class="java.lang.String"/>
<jsp:useBean id="codContenuto"          scope="request" class="java.lang.String"/>
<jsp:useBean id="codOggetti"            scope="request" class="java.lang.String"/>
<jsp:useBean id="descOggetti"           scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratorelatore"	scope="request" class="siap.sius.magistratorelatore.model.MagistratoRelatoreModel"/>
<jsp:useBean id="codDettagli"   		scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocato"           	scope="request" class="java.util.Vector"/>

<%
/* Check sospensione */
String lFascSospeso = "NO";
RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(fascicoloSiusGP.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());  
if (lRicerca.verificaEsistenzaSospensione())
	lFascSospeso = "SI";
%>

<html>
<head>
<title>[S.I.E.S.] - Emissione Decreto Designazione Magistrato Relatore</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
var desktop;

// Funzione dei controlli formali della form
function Verify() {
	var fascSospeso = '<%=lFascSospeso%>';
	// Controllo sospensione fascicolo
	if (fascSospeso == 'SI') {
		if (!confirm("Attenzione: per questo procedimento è presente un'ordinanza di rimessione atti. Procedere con l'emissione di un nuovo provvedimento?"))
			return false;
	}
<%
int numAvvocati = avvocato.size();
if (numAvvocati == 0) {
%>
	alert('Avvocato obbligatorio!');
	return false;
<%
}
%>
	// Controllo obbligatorieta' campo magistrato relatore
	if (document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value == ""
			|| document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value == "-") {
    	alert('Magistrato Relatore obbligatorio!');
    	document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_COGNOME%>.focus();
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
		if (document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value != '<%=codMagistratoOld%>') {
			if (!confirm("Attenzione: si sta inserendo un Magistrato Relatore diverso da quello già assegnato al fascicolo! Procedere con l'inserimento del nuovo Magistrato Relatore?"))
				return false;
		} else {
			alert('Attenzione: si sta inserendo lo stesso Magistrato Relatore già assegnato al fascicolo!');
			return false;
		}
<%
}
%>
	}

	// Controllo validità data Emissione
	var dataEmissione = document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value + '/' +
    					document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value + '/' +
    					document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

  	if (!ControllaData(dataEmissione)) {
	    alert('Data Emissione non valida!');
	    return false;
  	}

	// Controllo validità data Termine Emissione
	var dataTermineEmissione =
		document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_TERMINE_EMISSIONE%>.value + '/' +
		document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiDepositoDecreto.CAMPO_MESE_DATA_TERMINE_EMISSIONE%>.value + '/' +
		document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_TERMINE_EMISSIONE%>.value;

	if (dataTermineEmissione != "//") {
	  	if (!ControllaData(dataTermineEmissione)) {
		    alert('Data Termine Emissione non valida!');
		    document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_TERMINE_EMISSIONE%>.focus();
		    return false;
	  	}
	 	// Data Termine Emissione deve essere >= Data Emissione
        if (!CompareDate(dataEmissione, dataTermineEmissione)) {
          	alert('Data Termine Emissione deve essere maggiore od uguale Data Emissione!');
          	document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_TERMINE_EMISSIONE%>.focus();
          	return false;
        }
	} else {
		if (!confirm("Attenzione: data Termine Emissione non valorizzata! Procedere con l'inserimento del Decreto di Designazione Magistrato Relatore?"))
			return false;
	}

   	return true;
}

// Chiamata funzione lista Oggetti
function ListaOggetti(a_formname,a_field_contenuto, a_fieldname, a_fieldcodes, a_fieldcodesdet, i_fieldcodes, i_fieldcodesdet) {
	// Compone il link URL per passare i parametri alla ElencoUdienza.JSP
  	var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaOggetti";
	aLink += "&formname="+a_formname;
	aLink += "&field_contenuto="+a_field_contenuto;
	aLink += "&fieldname="+a_fieldname;
	aLink += "&fieldcodes="+a_fieldcodes;
	aLink += "&fieldcodesdet="+a_fieldcodesdet;
	aLink += "&ifieldcodes="+i_fieldcodes;
	aLink += "&ifieldcodesdet="+i_fieldcodesdet;
  	desktop = window.open(aLink, "Lista_Oggetti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
}

function ListaMagistratiRelatori(a_formname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}
</script>
</head>

<%
// Imposta l'azione da Chiamare.
String azione = "siap.sius.depositodecreto.action.ActInserisciDesignazioneMagistratoRelatore";
%>

<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
      	<td class="LBG"><font class="label">Funzione:</font>&nbsp;
        	<font class="campo">Emissione Decreto Designazione Magistrato Relatore</font>
      	</td>
	</tr>
    <tr>
       	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    <tr>
    	<jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>">
       		<jsp:param name="MagRelRitorno" value="siap.sius.depositodecreto.action.ActLoadInserisciDesignazioneMagistratoRelatore"/>
    	</jsp:include>
	</tr>
</table>
<jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>">
	<jsp:param name="AvvRitorno" value="siap.sius.depositodecreto.action.ActLoadInserisciDesignazioneMagistratoRelatore"/>
</jsp:include>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciDesignazioneMagistratoRelatore">
<table cellspacing="2" cellpadding="2" style="width: 95%;">
<!-- Sezione Contenuto Oggetti -->
	<tr>
		<td class="l" width="20%">Data Emissione <font class="ob">(*)</font></td>
		<td class="L" colspan="3">
			<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    	</td>
	</tr>
	<tr>
		<td class="l">Contenuto</td>
		<td class="L" colspan="3"><%=contenuto%></td>
	</tr>
	<tr>
    	<td class="l">Oggetto <font class="ob">(*)</font></td>
      	<td class="l" colspan="3">
        	<Textarea title="Oggetto" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO%>" cols=100 rows=3 readonly><%=descOggetti%></Textarea>
 			<a href="Javascript:ListaOggetti('LoadInserisciDesignazioneMagistratoRelatore',document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value, '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
 				<img src="/images/fileselected.gif" title="Oggetti per il Contenuto selezionato" border="0">
 			</a>
 			&nbsp;
 			<a href="Javascript:ListaOggetti('LoadInserisciDesignazioneMagistratoRelatore','-', '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciDesignazioneMagistratoRelatore.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
      			<img src="/images/filefolder.gif" title="Elenco di tutti gli Oggetti Selezionabili" border="0">
      		</a>
    	</td>
	</tr>
	<tr>
     	<td class="l">Magistrato Relatore <font class="ob">(*)</font></td>
		<td class="l"><input value="" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" readonly="readonly" size="25"></td>
		<td class="l"><input value="" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>" readonly="readonly" size="25"></td>
	  	<td class="l">
	    	<a href="Javascript:ListaMagistratiRelatori('LoadInserisciDesignazioneMagistratoRelatore');">
	    		Seleziona dalla lista&nbsp;<img src="/images/filefolder.gif" title="Elenco di tutti i Magistrati Relatori dell'Ufficio" border="0">
	   		</a>
	  	</td>
	</tr>
  	<tr>
		<td class="l">Data Termine Emissione (Ammissione Provvisoria)</td>
		<td class="L" colspan="3">
			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_TERMINE_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_MESE_DATA_TERMINE_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_TERMINE_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    	</td>
	</tr>
	<tr>
  		<td>
    		<input class="bottone" type="submit" value="Conferma" onclick="Javascript:return Verify();">
  		</td>
	</tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=azione%>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>" value="<%=codOggetti%>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=codDettagli%>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=codContenuto%>">
<input type="hidden" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" value="">
</form>

<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadInserisciDesignazioneMagistratoRelatore");
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
frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_TERMINE_EMISSIONE%>","minlen=4","La lunghezza del campo Anno Data Termine Emissione deve essere di 4 caratteri");

// Controllo campo oggetto.
frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO%>", "req","É necessario selezionare almeno un oggetto");
</script>

</body>
</html>
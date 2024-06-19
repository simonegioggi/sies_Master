<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="contenuto"      	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"   	scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"	scope="request" class="java.util.Date"/>
<jsp:useBean id="decreto"        	scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>

<%
TenoreModel[] tenori = (TenoreModel[]) request.getAttribute("tenori");
String[] esiti  = (String[]) request.getAttribute("esiti");

UtenteModel lUteMod = (UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
UfficioModel lUffMod = lUteMod.getUfficioUtente();
String codUff = new String(lUffMod.getCodTipoUfficio());
String labelUfficio = "";
if (codUff.equals("TDSM") || codUff.equals("UDSM")) {
	labelUfficio = "Ufficio di Sorveglianza presso il Tribunale per Minorenni";
} else {
	labelUfficio = "Ufficio di Sorveglianza";
}

String lAction = new String();
lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS";

// MEV_2023-35: aggiunti controlli sul contenuto
String tipoContenuto = "", isReadOnly = "";
if ("C064".equals(contenuto) || "C065".equals(contenuto)) {
	if ("C064".equals(contenuto))
		tipoContenuto = " Sostitutiva";
	else
		tipoContenuto = " Sostitutiva Derivante da Conversione Pene Pecuniarie";
	isReadOnly = "readonly";
}
%>

<html>
<head>
<title>[S.I.E.S.] - Emissione Ordinanza Concessione Rinvio Esecuzione Pena<%=tipoContenuto%></title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="/html/verifyCombo.js"></script>
<script language="JavaScript">
function Init() {
<%
if (decreto != null && decreto.getIdDepositoDecreto() != null) {
%>
	InitDecreto();
<%
}
%>
	return;
}

function InitDecreto() {
	document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value = "<%=DateUtils.getDateToString(decreto.getDataEmissione(), "dd")%>";
	document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_TRASMISSIONE%>.value = "<%=DateUtils.getDateToString(decreto.getDataEmissione(), "MM")%>";
	document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_TRASMISSIONE%>.value = "<%=DateUtils.getDateToString(decreto.getDataEmissione(), "yyyy")%>";
	document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>.value = "<%=decreto.getDescrUfficioInserimento()%>";
}

// STUB 21/07/2004 Controllo obbligatorietà esiti.
function Verify() {
	var lEsiti = document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
	if (!VerifyCombo(lEsiti,"Esito") )
	  	return false;
	var ritorno = true;
	ritorno = ControlliDate();
	return ritorno;
}

function ControlliDate() {
	var ritorno = true;
	var data_inizio_periodo = document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value+'/'+document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value+'/'+document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>.value;
	var data_termine_periodo = document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value;
	var data_emissione_decreto = document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value+'/'+document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_TRASMISSIONE%>.value+'/'+document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_TRASMISSIONE%>.value;
	if (data_emissione_decreto.length > 2)
    	ritorno = ConfrontaConDataEmissione(data_emissione_decreto, "data emissione decreto");
 	if (ritorno) {
   		if (data_termine_periodo.length > 2) {
      		ritorno = ControllaData(data_termine_periodo);
	      	if (!ritorno)
	       		alert("Data termine periodo non valida!");
	      	// ritorno = ConfrontaConDataEmissione(data_termine_periodo, "data termine periodo");
	      	if (ritorno)
	      		ritorno =  data_termine_periodo.length > 2 && ControllaData(data_inizio_periodo);
	      	if (!ritorno)
	       		alert("Data inizio periodo non valida!");
	        // ritorno = ConfrontaConDataEmissione(data_inizio_periodo, "data inizio periodo");
	      	if (ritorno) {
	       		if (CompareDate(data_termine_periodo,data_inizio_periodo)) {
					alert( "La data inizio periodo deve precedere la data termine periodo");
					ritorno =  false;
	       		}
	     	}
	   	} else if (data_inizio_periodo.length > 2) {
     		ritorno =  ControllaData(data_inizio_periodo);
      		if (!ritorno)
       			alert( "data inizio periodo non valida");
     		// ritorno = ConfrontaConDataEmissione(data_inizio_periodo, "data inizio periodo");
   		}
 	}
 	return ritorno;
}

function ConfrontaConDataEmissione(data, nome) {
	var ritorno = true;
	var data_emissione = '<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%>';
   	if (data.length == 2 || !ControllaData(data)) {
	    alert( "" + nome + " non valida");
	    ritorno =  false;
   	} else if (CompareDate(data_emissione, data)) {
	    alert( ""  + nome + "  deve precedere Data Emissione Ordinanza" );
	    ritorno =  false;
   	}
  	return ritorno;
}

// Chiamata all'elenco degli UDS
function ListaUDS(a_formname, a_fieldname, a_typename) {
	var desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}

// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
function updateCkCtrlE() {
	if (document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[0].checked == true) {
		document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[1].checked = false;
  	} 
}

function updateCkCtrlT() {
	if (document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[1].checked == true) {
		document.InserisciOrdinanzaConcessioneRinvioEP.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[0].checked = false;
  	} 
}
</script>
</head>
<body class="corpo" onLoad="Javascript:Init();">
<table>
	<tr>
  		<td class="LBG">
  			<a href="Javascript:window.print();">
  				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
  			</a>
  		</td>
  		<td class="LBG">
  			<font class="label">Funzione : </font>
  			<font class="campo">Emissione Ordinanza Rinvio esecuzione della pena<%=tipoContenuto%></font>
  		</td>
	</tr>
	<tr>
   		<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
	</tr>
</table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaConcessioneRinvioEP">
<table width="35%">
	<tr>
		<td class="l">Data Emissione</td>
		<td class="l"><%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
   	</tr>
</table>
<br>
<table cellspacing="2" cellpadding="2" style="width: 95%;">
	<tr>
		<td class="Titolo" colspan="6"> Specificare esito per ciascun oggetto: </td>
    </tr>
    <tr>
        <td class="l" colspan="2"> Oggetto </td>
        <td class="l" colspan="2"> Esito </td>
    </tr>
<%
for (int i = 0; i < tenori.length; i++) {
%>
	<tr>
 		<td class="l" colspan="2">
   			<input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE%>" value="<%=tenori[i].getDescrOggettoTenore()%>" readonly size="90%">
			<input type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>" value="<%=tenori[i].getCodOggettoTenore()%>">
			<input type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=tenori[i].getCodDettaglioOggetto()%>">
		</td>
		<td class="l"colspan="2">
			<select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>"><%=esiti[i]%></select>
  		</td>
	</tr>
<%
}
%>
</table>
<br>
<table cellspacing="2" cellpadding="2" style="width: 95%;">
	<tr>
  		<td class="l" width="15%">Ulteriore descrizione della decisione</td>
   		<td class="l" colspan="3"><TEXTAREA title="Ulteriore descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4"></textarea></td>
  	</tr>
  	<tr><td>&nbsp;</td></tr>
  	<tr>
    	<td class="Titolo" colspan="4"> Estremi decreto Magistrato Sorveglianza: <td>
  	</tr>
    <tr>
		<td class="l" width="15%">Data Emissione<br>(gg-mm-aaaa)</td>
		<td class="L" width="20%">
		  	<input readonly="<%=isReadOnly%>" value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_TRASMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input readonly="<%=isReadOnly%>" value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_TRASMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input readonly="<%=isReadOnly%>" value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_TRASMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
		<td class="l" width="20%"><%=labelUfficio%></td>
		<td class="l">
		  	<input readonly="<%=isReadOnly%>" title="<%=labelUfficio%>" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>" value="" size="35">
			<a href="Javascript:ListaUDS('InserisciOrdinanzaConcessioneRinvioEP','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>','<%=codUff%>');">
		  		<img src="/images/filefolder.gif" border="0">
		  	</a>
		</td>
    </tr>
<%
// MEV_2023-35: aggiunto importo della pena pecuniaria convertita
if ("C065".equals(contenuto)) {
%>
    <tr><td>&nbsp;</td></tr>
	<tr>
		<td class="l" colspan="4">Pena Pecuniaria Convertita:&nbsp;&nbsp;&nbsp;
	        <input type="text" size="10" maxlength="7" Title="Pena Pecuniaria Convertita" value=""
	        	name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_PENA_PECUNIARIA_CONVERTITA%>"
	        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">,
	        <input type="text" size="2" maxlength="2" Title="Pena Pecuniaria Convertita" value=""
	        	name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_PENA_PECUNIARIA_CONVERTITA%>"
	        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
	        &euro;
    	</td>
    </tr>
    <tr><td>&nbsp;</td></tr>
<%
}
%>
	<tr>
		<td class="Titolo" colspan="4"> In caso di Concessione indicare: <td>
  	</tr>
	<tr>
		<td class="l">Data Inizio Periodo<br>(gg-mm-aaaa)</td>
		<td class="L">
			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
		<td class="l">Data Termine Periodo<br>(gg-mm-aaaa)</td>
		<td class="L">
			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
	<tr>
  		<td class="Titolo" colspan="4"> oppure: <td>
	</tr>
	<tr>
		<td class="l">Durata sospensione<br>(AA-MM-GG)</td>
		<td class="L">
			<input value="" title="Numero Anni Detenzione" type="text" size="3" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM%>"> -
			<input value="" title="Numero Mesi Detenzione" type="text" size="3" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM%>"> -
			<input value="" title="Numero Giorni Detenzione" type="text" size="4" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM%>">
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
  		<td class="l" colspan="4">
    		Controllo tramite mezzi elettronici 
    		<input value="E" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>" onClick="javascript:updateCkCtrlE()"/> 
			&nbsp;&nbsp;&nbsp;
			Controllo tramite altri strumenti tecnici 
			<input value="T" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>" onClick="javascript:updateCkCtrlT()"/> 
  		</td>
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
<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>">
<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%>>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("InserisciOrdinanzaConcessioneRinvioEP");
frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM%>", "numeric");
frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM%>", "numeric");
frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM%>", "numeric");
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
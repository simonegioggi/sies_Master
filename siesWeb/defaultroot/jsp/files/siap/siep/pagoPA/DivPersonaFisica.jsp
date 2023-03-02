<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-13_ aggiunta pagina di caricamento dati civilmente obbligato --%>
<%@page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>

<%@ page import="siap.siep.pagoPA.action.ICostantiPagoPA"%>
<%@ page import="siap.siep.pagoPA.model.CivilmenteObbligatoModel"%>

<jsp:useBean id="modalita"          	scope="request" class="java.lang.String"/>
<jsp:useBean id="sesso"             	scope="request" class="java.lang.String"/>
<jsp:useBean id="nazioni"           	scope="request" class="java.lang.String"/>
<jsp:useBean id="nazioniResidenza"  	scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"          	scope="request" class="java.lang.String"/>
<jsp:useBean id="idFascicolSiep"		scope="request" class="java.lang.String"/>
<jsp:useBean id="civilmenteObbligati" 	scope="request" class="java.util.Vector<siap.siep.pagoPA.model.CivilmenteObbligatoModel>"/>

<%
String action = "";
String titolo = "";
String azioneChiamante = "";

if (modalita.equals("I")) {
	action = "siap.siep.pagoPA.action.ActInserisciCivilmenteObbligato";
	titolo = "Inserimento Civilmente Obbligato Pena Pecuniaria";
	azioneChiamante = "siap.siep.pagoPA.action.ActLoadInserisciCivilmenteObbligato";
} else if (modalita.equals("M")) {
	action = "siap.siep.pagoPA.action.ActModificaCivilmenteObbligato";
	titolo = "Modifica Civilmente Obbligato Pena Pecuniaria";
	azioneChiamante = "siap.siep.pagoPA.action.ActLoadModificaCivilmenteObbligato";
}
%>
<script language="JavaScript">
var desktop;
function ListaComuni(a_formname,a_fieldname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function calendario(a_formname,a_field_year,a_field_month,a_field_day) {
	desktop = window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
}

function Verify() {
	if (document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_COD_STATO_NASCITA%>[document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value == '039') {
		if (document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_COD_COMUNE_NASCITA%>.value.length == 0) {
			alert('Il Comune di Nascita è obbligatorio se lo Stato di Nascita è Italia');
			document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_COD_COMUNE_NASCITA%>.focus;
			return false;
  		}
	} else {
  		document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_COD_COMUNE_NASCITA%>.value = '';
	}
	if (document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>.value.length == 1)
 		document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>.value = '0'+
 		document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>.value;
	if (document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>.value.length == 1)
 		document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>.value = '0' +
 		document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>.value;
	var data_to_verify = document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>.value
		+ '/' + document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>.value
		+ '/' + document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>.value;
	if (! ControllaData(data_to_verify)) {
	    alert('Data di nascita non valida');
	    return false;
	}
	<%-- EVENTUALE SECONDO TUTORE --%>
	var secondoTutore = document.getElementById("secondoTutore");
	if (document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CHECK_COD_TUTORE%>.checked == true) {
		if (document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_COD_STATO_NASCITA%>_ST[document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_COD_STATO_NASCITA%>_ST.selectedIndex].value == '039') {
			if (document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_COD_COMUNE_NASCITA%>_ST.value.length == 0) {
				alert('Il Comune di Nascita del secondo Tutore è obbligatorio se lo Stato di Nascita è Italia');
				document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_COD_COMUNE_NASCITA%>_ST.focus;
				return false;
	  		}
		} else {
	  		document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_COD_COMUNE_NASCITA%>_ST.value = '';
		}
		if (document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>_ST.value.length == 1)
	 		document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>_ST.value = '0'+
	 		document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>_ST.value;
		if (document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>_ST.value.length == 1)
	 		document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>_ST.value = '0' +
	 		document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>_ST.value;
		var data_to_verify_ST = document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>_ST.value
			+ '/' + document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>_ST.value
			+ '/' + document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>_ST.value;
		if (! ControllaData(data_to_verify_ST)) {
		    alert('Data di nascita del secondo Tutore non valida');
		    return false;
		}
	}
	return true;
}

function VisualizzaSecondoTutore() {
	var secondoTutore = document.getElementById("secondoTutore");
	if (document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CHECK_COD_TUTORE%>.checked == true) {
		secondoTutore.style.display = "block";
	} else {
		secondoTutore.style.display = "none";
  	}
}
</script>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"	name="LoadInserisciPersonaFisica">
<table cellspacing="0" cellpadding="0" width="95%">
<%
// se è vuoto vengo da insert altrimenti da update
Iterator<CivilmenteObbligatoModel> itx = civilmenteObbligati.iterator();
while (itx.hasNext()) {
	CivilmenteObbligatoModel civilmenteObbligato = (CivilmenteObbligatoModel) itx.next();
%>
	<tr>
		<td class="Titolo" colspan="4"><%=titolo%></td>
	</tr>
	<tr>
		<td class="l">Cognome <font class=ob>(*)</font></td>
		<td class="L">
			<input type="hidden" name="<%=ICostantiPagoPA.CAMPO_ID_CIVILMENTE_OBBLIGATO%>" value="<%=civilmenteObbligato.getIdCivilmenteObbligato()%>">
			<input title="Cognome" value="<%=civilmenteObbligato.getCognome()%>" type="text" name="<%=ICostantiPagoPA.CAMPO_COGNOME%>" maxlength="100" size="35">
		</td>
		<td class="l">Nome <font class=ob>(*)</font></td>
		<td class="L">
			<input title="Nome" value="<%=civilmenteObbligato.getNome() %>" type="text" name="<%=ICostantiPagoPA.CAMPO_NOME%>" maxlength="100" size="35">
		</td>
	</tr>
	<tr>
		<td class="l">Sesso <font class=ob>(*)</font></td>
		<td class="L"><select title="Sesso" name="<%=ICostantiPagoPA.CAMPO_SESSO%>"> <%=sesso%></select></td>
		<td class="l" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Data di Nascita <font class=ob>(*)</font></td>
        <td class="l">
<%
if(modalita.equals("I")) {
%>
            <input type="text" title="Giorno Data di nascita" name="<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" title="Mese Data di nascita" name="<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" title="Anno Data di nascita" name="<%=ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
} else {
%>
			<input title="Giorno Data di nascita" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(civilmenteObbligato.getDataNascita(), "dd"))%>" type="text" name="<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			/
			<input title="Mese Data di nascita" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(civilmenteObbligato.getDataNascita(), "MM"))%>" type="text" name="<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			/
			<input title="Anno Data di nascita" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(civilmenteObbligato.getDataNascita(), "yyyy"))%>" type="text" name="<%=ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
}
%>
			<a href="javascript:calendario('LoadInserisciPersonaFisica','<%=ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>','<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>','<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
		</td>
        <td class="l">Comune di Nascita <font class=ob>(*)</font></td>
        <td class="L">
          	<input title="Comune di Nascita" value="<%=StringUtils.toStringJSP(civilmenteObbligato.getDescComuneNascita())%>" type="text" name="<%=ICostantiPagoPA.CAMPO_COD_COMUNE_NASCITA%>" size="35">
          	<a href="Javascript:ListaComuni('LoadInserisciPersonaFisica','<%=ICostantiPagoPA.CAMPO_COD_COMUNE_NASCITA%>');">
            	<img src="/images/filefolder.gif" border=0>
          	</a>
        </td>
	</tr>
	<tr>
		<td class="l">Stato di Nascita</td>
		<td class="L">
       		<select  title="Stato di Nascita" name="<%=ICostantiPagoPA.CAMPO_COD_STATO_NASCITA%>"><%=nazioni%></select>
        </td>
		<td class="l">Comune di Nascita Estero</td>
		<td class="L">
			<input title="Comune di Nascita Estero" value="<%=StringUtils.toStringJSP(civilmenteObbligato.getDescComuneNascitaEstero())%>" type="text" name="<%=ICostantiPagoPA.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>" maxlength="200" size="35">
        </td>
	</tr>
	<tr>
        <td class="l">Codice Fiscale</td>
        <td class="l"><input title="Codice Fiscale" value="<%=StringUtils.toStringJSP(civilmenteObbligato.getCodFiscale())%>" type="text" name="<%=ICostantiPagoPA.CAMPO_COD_FISCALE%>" maxlength="16" size="25">
        </td>
        <td class="l" colspan="2">&nbsp;</td>
	</tr>
	<tr>
        <td class="l">Pec</td>
        <td class="l">
        	<input title="Pec" value="<%=StringUtils.toStringJSP(civilmenteObbligato.getPec())%>" type="text" name="<%=ICostantiPagoPA.CAMPO_PEC%>" maxlength="200" size="50">
        </td>
        <td class="l">Email</td>
        <td class="l">
        	<input title="Email" value="<%=StringUtils.toStringJSP(civilmenteObbligato.getEmail())%>" type="text" name="<%=ICostantiPagoPA.CAMPO_EMAIL%>" maxlength="200" size="50">
        </td>
	</tr>
	<%-- EVENTUALE SECONDO TUTORE --%>
	<tr>
		<td class="l">Tutore&nbsp;<input type="checkbox" name="<%=ICostantiPagoPA.CHECK_COD_TUTORE%>" onclick="VisualizzaSecondoTutore();"></td>
	</tr>
</table>
<div id="secondoTutore" style="display: none;">
<br>
<table cellspacing="0" cellpadding="0" width="95%">
<tr>
		<td class="Titolo" colspan="4">Eventuale Secondo Tutore</td>
	</tr>
<tr>
		<td class="l">Cognome <font class=ob>(*)</font></td>
		<td class="L">
			<input type="hidden" name="<%=ICostantiPagoPA.CAMPO_ID_CIVILMENTE_OBBLIGATO%>_ST" value="<%=civilmenteObbligato.getIdCivilmenteObbligato()%>">
			<input title="Cognome Secondo Tutore" value="<%=civilmenteObbligato.getCognome()%>" type="text" name="<%=ICostantiPagoPA.CAMPO_COGNOME%>_ST" maxlength="100" size="35">
		</td>
		<td class="l">Nome <font class=ob>(*)</font></td>
		<td class="L">
			<input title="Nome Secondo Tutore" value="<%=civilmenteObbligato.getNome()%>" type="text" name="<%=ICostantiPagoPA.CAMPO_NOME%>_ST" maxlength="100" size="35">
		</td>
	</tr>
	<tr>
		<td class="l">Sesso <font class=ob>(*)</font></td>
		<td class="L"><select title="Sesso Secondo Tutore" name="<%=ICostantiPagoPA.CAMPO_SESSO%>_ST"><%=sesso%></select></td>
		<td class="l" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Data di Nascita <font class=ob>(*)</font></td>
        <td class="l">
<%
if (modalita.equals("I")) {
%>
            <input type="text" title="Giorno Data di nascita Secondo Tutore" name="<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>_ST" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" title="Mese Data di nascita Secondo Tutore" name="<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>_ST" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" title="Anno Data di nascita Secondo Tutore" name="<%=ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>_ST" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
} else {
%>
			<input title="Giorno Data di nascita Secondo Tutore" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(civilmenteObbligato.getDataNascita(), "dd"))%>" type="text" name="<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>_ST" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			/
			<input title="Mese Data di nascita Secondo Tutore" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(civilmenteObbligato.getDataNascita(), "MM"))%>" type="text" name="<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>_ST" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			/
			<input title="Anno Data di nascita Secondo Tutore" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(civilmenteObbligato.getDataNascita(), "yyyy"))%>" type="text" name="<%=ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>_ST" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
}
%>
			<a href="javascript:calendario('LoadInserisciPersonaFisica','<%=ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>_ST','<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>_ST','<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>_ST');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
		</td>
        <td class="l">Comune di Nascita <font class=ob>(*)</font></td>
        <td class="L">
          	<input title="Comune di Nascita Secondo Tutore" value="<%=StringUtils.toStringJSP(civilmenteObbligato.getDescComuneNascita())%>" type="text" name="<%=ICostantiPagoPA.CAMPO_COD_COMUNE_NASCITA%>_ST" size="35">
          	<a href="Javascript:ListaComuni('LoadInserisciPersonaFisica','<%=ICostantiPagoPA.CAMPO_COD_COMUNE_NASCITA%>_ST');">
            	<img src="/images/filefolder.gif" border=0>
          	</a>
        </td>
	</tr>
	<tr>
		<td class="l">Stato di Nascita</td>
		<td class="L">
       		<select  title="Stato di Nascita Secondo Tutore" name="<%=ICostantiPagoPA.CAMPO_COD_STATO_NASCITA%>_ST"><%=nazioni%></select>
        </td>
		<td class="l">Comune di Nascita Estero</td>
		<td class="L">
			<input title="Comune di Nascita Estero Secondo Tutore" value="<%=StringUtils.toStringJSP(civilmenteObbligato.getDescComuneNascitaEstero())%>" type="text" name="<%=ICostantiPagoPA.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>_ST" maxlength="200" size="35">
        </td>
	</tr>
	<tr>
        <td class="l">Codice Fiscale</td>
        <td class="l"><input title="Codice Fiscale Secondo Tutore" value="<%=StringUtils.toStringJSP(civilmenteObbligato.getCodFiscale())%>" type="text" name="<%=ICostantiPagoPA.CAMPO_COD_FISCALE%>_ST" maxlength="16" size="25">
        </td>
        <td class="l" colspan="2">&nbsp;</td>
	</tr>
	<tr>
        <td class="l">Pec</td>
        <td class="l">
        	<input title="Pec Secondo Tutore" value="<%=StringUtils.toStringJSP(civilmenteObbligato.getPec())%>" type="text" name="<%=ICostantiPagoPA.CAMPO_PEC%>_ST" maxlength="200" size="50">
        </td>
        <td class="l">Email</td>
        <td class="l">
        	<input title="Email Secondo Tutore" value="<%=StringUtils.toStringJSP(civilmenteObbligato.getEmail())%>" type="text" name="<%=ICostantiPagoPA.CAMPO_EMAIL%>_ST" maxlength="200" size="50">
        </td>
	</tr>
</table>
<br>
</div>
<%-- FINE DIV SECONDO TUTORE --%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="Titolo" colspan="4">Residenza/Domicilio</td>
	</tr>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="l"><input size="50" maxlength="200" value="<%if (civilmenteObbligato.getResidenza() != null)%><%=StringUtils.toStringJSP(civilmenteObbligato.getResidenza().getIndirizzo())%>" title="Indirizzo" type="text" name="<%=ICostantiPagoPA.CAMPO_INDIRIZZO%>">
			<input type="HIDDEN" name="<%=ICostantiPagoPA.CAMPO_ID_RESIDENZA%>" value="<%if (civilmenteObbligato.getResidenza() != null)%><%=civilmenteObbligato.getResidenza().getIdResidenza()%>">
		</td>
        <td class="l">Luogo</td>
        <td class="L">
          	<input title="Comune di Residenza" value="<%if (civilmenteObbligato.getResidenza() != null)%><%=StringUtils.toStringJSP(civilmenteObbligato.getResidenza().getDescrComune())%>" type="text" name="<%=ICostantiPagoPA.CAMPO_COD_COMUNE_RESIDENZA%>" maxlength="35" size="35">
          	<a href="Javascript:ListaComuni('LoadInserisciPersonaFisica','<%=ICostantiPagoPA.CAMPO_COD_COMUNE_RESIDENZA%>');">
            	<img src="/images/filefolder.gif" border=0>
          	</a>
		</td>
    </tr>
	<tr>
		<td class="l">CAP</td>
		<td class="l">
			<input size=5 maxlength=5 value="<%if (civilmenteObbligato.getResidenza() != null)%><%=StringUtils.toStringJSP(civilmenteObbligato.getResidenza().getCap())%>" title="Cap" type="text" name="<%=ICostantiPagoPA.CAMPO_CAP_RESIDENZA%>">
		</td>
		<td class="l">Comune Estero</td>
		<td class="l">
			<input size=50 maxlength=200 value="<%if (civilmenteObbligato.getResidenza() != null)%><%=StringUtils.toStringJSP(civilmenteObbligato.getResidenza().getDescComuneEstero())%>" title="ComuneEstero" type="text" name="<%=ICostantiPagoPA.CAMPO_DESC_COMUNE_ESTERO_RESIDENZA%>">
		</td>
	</tr>
	<tr>
		<td class="l">Stato</td>
		<td class="L">
       		<select title="Stato di Residenza" name="<%=ICostantiPagoPA.CAMPO_COD_STATO_RESIDENZA%>"><%=nazioniResidenza%></select>
        </td>
		<td class="l" colspan="2">&nbsp;</td>
	</tr>
<%
}
%>
</table>
<br>
<table cellspacing="0" cellpadding="0">	
	<tr>
		<td>
			<INPUT class="bottone" type="submit" name="INSERISCI" value="Conferma">
		</td>
	</tr>
</table>

<input type="HIDDEN" name="Action" value="<%=action%>"> 
<input type="HIDDEN" name="<%=ICostantiPagoPA.RADIO_COD_PERSONA%>" value="F">
<input type="HIDDEN" name="<%=ICostantiPagoPA.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=idFascicolSiep%>">
<input type="HIDDEN" name="modalita" value="<%=modalita%>">
</form>

<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadInserisciPersonaFisica");

frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_COGNOME%>","req","Il campo Cognome è obbligatorio");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_COGNOME%>","maxlen=100","La lunghezza massima per il cognome è di 100 caratteri");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_COGNOME%>","alpha");

frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_NOME%>","req","Il campo Nome è obbligatorio");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_NOME%>","maxlen=100","La lunghezza massima per il nome è di 100 caratteri");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_NOME%>","alpha");

frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>","req","Il campo Giorno di Nascita è obbligatorio");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>","numeric");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>","gt=1");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>","lt=31");

frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>","req","Il campo Mese di Nascita è obbligatorio");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>","numeric");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>","gt=1");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>","lt=12");

frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>","req","Il campo Anno di Nascita è obbligatorio");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>","numeric");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>","lt=3000");

frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_COD_FISCALE%>","alphanumeric");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>","alpha");

frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_CAP_RESIDENZA%>","numeric");
frmvalidator.addValidation("<%=ICostantiPagoPA.CAMPO_DESC_COMUNE_ESTERO_RESIDENZA%>","alpha");

frmvalidator.setAddnlValidationFunction("Verify");
</script>
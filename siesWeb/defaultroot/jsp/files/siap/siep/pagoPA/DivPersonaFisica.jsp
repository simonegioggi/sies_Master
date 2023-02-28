<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-13_ aggiunta pagina di caricamento dati civilmente obbligato --%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>


<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>

<%@ page import="siap.siep.pagoPA.action.ICostantiPagoPA"%>

<jsp:useBean id="modalita"          	scope="request" class="java.lang.String"/>
<jsp:useBean id="sesso"             	scope="request" class="java.lang.String"/>
<jsp:useBean id="nazioni"           	scope="request" class="java.lang.String"/>
<jsp:useBean id="nazioniResidenza"  	scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"          	scope="request" class="java.lang.String"/>
<jsp:useBean id="civilmenteObbligato" 	scope="request" class="siap.siep.pagoPA.model.CivilmenteObbligatoModel"/>

<%
String lAction = "";
String lTitolo = "";
String azioneChiamante = "";

if (modalita.equals("I")) {
	lAction = "siap.siep.pagoPA.action.ActInserisciCivilmenteObbligato";
	lTitolo = "Inserimento Civilmente Obbligato Pena Pecuniaria";
	azioneChiamante = "siap.siep.pagoPA.action.ActLoadInserisciCivilmenteObbligato";
} else if (modalita.equals("M")) {
	lAction = "siap.siep.pagoPA.action.ActModificaCivilmenteObbligato";
	lTitolo = "Modifica Civilmente Obbligato Pena Pecuniaria";
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

function ListaUffici(a_formname,a_fieldname) {
  	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}

function cancellaCodComuneReale() {
	document.LoadInserisciPersonaFisica.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
}

function checkSNT(idEle) {
  	var flag = document.getElementById("flagSNT_"+idEle);
  	var select = document.getElementById("<%=ICostantiPagoPA.CAMPO_COD_DESTINATARIO%>_"+idEle);
	var sede = document.getElementById("<%=ICostantiPagoPA.CAMPO_SEDE%>_"+idEle);
  	var autRow = document.getElementById("AutDestRow_"+idEle);
  	var sedeRow = document.getElementById("SedeDestRow_"+idEle);
  	if (flag.checked) {
		select.options[0].setAttribute("selected", "selected");
  		sede.value = "";
  		autRow.style.display="none";
  		sedeRow.style.display="none";
  	} else {
  		// SNT off
		autRow.style.display="block";
		sedeRow.style.display="block";
  	}
}

function Verify() {
	if (document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_COD_STATO_NASCITA%>[document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value=='039') {
		if (document.LoadInserisciPersonaFisica.<%= ICostantiPagoPA.CAMPO_COD_COMUNE_NASCITA %>.value.length == 0) {
			alert('Il Comune di Nascita è obbligatorio se lo Stato di Nascita è Italia');
			document.LoadInserisciPersonaFisica.<%= ICostantiPagoPA.CAMPO_COD_COMUNE_NASCITA %>.focus;
			return false;
  		}
	} else {
  		document.LoadInserisciPersonaFisica.<%= ICostantiPagoPA.CAMPO_COD_COMUNE_NASCITA %>.value='';
     	cancellaCodComuneReale();
	}
	if (document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
 		document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>.value;
	if (document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
 		document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>.value;
	var data_to_verify = document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>.value
		+ '/' + document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>.value
		+ '/' + document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>.value;
	if (! ControllaData(data_to_verify)) {
	    alert('Data di nascita non valida');
	    return false;
	}
	if (typeof(document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE%>) != "undefined") {
		if (document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE%>.checked == false) {
			// se il civilmente obbligato non è domiciliato presso il difensore
			// bisogna compilare la sezione "Notifica al Soggetto"
    		if ( document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_COD_IST_DETENZIONE%>.value == "-"
					|| document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_COD_IST_DETENZIONE%>.value == ""
					|| document.LoadInserisciPersonaFisica.<%=ICostantiPagoPA.CAMPO_COD_LUOGO_DETENZIONE%>.value == "") {
				alert('Scegliere Autorità di Destinazione e Sede per il destinario Soggetto!');
				return false;
 	  		}
   		}
  	}
	return true;
}
</script>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"	name="LoadInserisciPersonaFisica">
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="Titolo" colspan="4"><%=lTitolo%></td>
	</tr>
	<tr>
		<td class="l">Cognome <font class=ob>(*)</font></td>
		<td class="L">
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
          	<input title="Comune di Nascita" value="<%=StringUtils.toStringJSP(civilmenteObbligato.getDescComuneNascita())%>" type="text" name="<%=ICostantiPagoPA.CAMPO_COD_COMUNE_NASCITA%>" size="35" onChange="cancellaCodComuneReale();">
          	<a href="Javascript:ListaComuni('LoadInserisciPersonaFisica','<%=ICostantiPagoPA.CAMPO_COD_COMUNE_NASCITA%>');">
            	<img src="/images/filefolder.gif" border=0>
          	</a>
        </td>
	</tr>
	<tr>
		<td class="l">Stato di Nascita</td>
		<td class="L">
       		<select  title="Stato di Nascita" name="<%=ICostantiPagoPA.CAMPO_COD_STATO_NASCITA%>">
   				<%= nazioni %>
       		</select>
        </td>
		<td class="l">Comune di Nascita Estero</td>
		<td class="L">
			<input title="Comune di Nascita Estero" value="<%=StringUtils.toStringJSP(civilmenteObbligato.getDescComuneNascitaEstero())%>" type="text" name="<%=ICostantiPagoPA.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>" maxlength="200" size="35">
        </td>
	</tr>
	<tr>
        <td class="l">Codice Fiscale</td>
        <td class="l"><input title="Codice Fiscale" id=<%=ICostantiPagoPA.CAMPO_COD_FISCALE%> value="<%=StringUtils.toStringJSP(civilmenteObbligato.getCodFiscale()) %>" type="text" name="<%=ICostantiPagoPA.CAMPO_COD_FISCALE%>" maxlength="16" size="25">
        </td>
        <td class="l" colspan="2">&nbsp;</td>
	</tr>
	<tr>
        <td class="l">Pec</td>
        <td class="l">
        	<input title="Pec" id=<%=ICostantiPagoPA.CAMPO_PEC%> value="<%=StringUtils.toStringJSP(civilmenteObbligato.getPec())%>" type="text" name="<%=ICostantiPagoPA.CAMPO_PEC%>" maxlength="200" size="50">
        </td>
        <td class="l">Email</td>
        <td class="l">
        	<input title="Email" id=<%=ICostantiPagoPA.CAMPO_EMAIL%> value="<%=StringUtils.toStringJSP(civilmenteObbligato.getEmail())%>" type="text" name="<%=ICostantiPagoPA.CAMPO_EMAIL%>" maxlength="200" size="50">
        </td>
	</tr>
	<tr>
		<td class="Titolo" colspan="4">Residenza/Domicilio</td>
	</tr>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="l"><input size="50" maxlength="200" value="<%if(civilmenteObbligato.getResidenza()!= null)%><%=StringUtils.toStringJSP(civilmenteObbligato.getResidenza().getIndirizzo())%>" title="Indirizzo" type="text" name="<%=ICostantiPagoPA.CAMPO_INDIRIZZO%>">
			<input type="HIDDEN" name="<%=ICostantiPagoPA.CAMPO_ID_RESIDENZA%>" value="<%if (civilmenteObbligato.getResidenza() != null)%><%=civilmenteObbligato.getResidenza().getIdResidenza()%>">
		</td>
        <td class="l">Luogo</td>
        <td class="L">
          	<input title="Comune di Residenza" value="<%if(civilmenteObbligato.getResidenza()!= null)%><%=StringUtils.toStringJSP(civilmenteObbligato.getResidenza().getDescrComune())%>" type="text" name="<%=ICostantiPagoPA.CAMPO_COD_COMUNE_RESIDENZA%>" maxlength="35" size="35">
          	<a href="Javascript:ListaComuni('LoadInserisciPersonaFisica','<%=ICostantiPagoPA.CAMPO_COD_COMUNE_RESIDENZA%>');">
            	<img src="/images/filefolder.gif" border=0>
          	</a>
		</td>
    </tr>
	<tr>
		<td class="l">CAP</td>
		<td class="l">
			<input size=5 maxlength=5 value="<%if(civilmenteObbligato.getResidenza() != null)%><%=StringUtils.toStringJSP(civilmenteObbligato.getResidenza().getCap())%>" title="Cap" type="text" name="<%=ICostantiPagoPA.CAMPO_CAP_RESIDENZA%>">
		</td>
		<td class="l">Comune Estero</td>
		<td class="l">
			<input size=50 maxlength=200 value="<%if(civilmenteObbligato.getResidenza() != null)%><%=StringUtils.toStringJSP(civilmenteObbligato.getResidenza().getDescComuneEstero())%>" title="ComuneEstero" type="text" name="<%=ICostantiPagoPA.CAMPO_DESC_COMUNE_ESTERO_RESIDENZA%>">
		</td>
	</tr>
	<tr>
		<td class="l">Stato</td>
		<td class="L">
       		<select title="Stato di Residenza" name="<%=ICostantiPagoPA.CAMPO_COD_STATO_RESIDENZA%>">
   				<%= nazioniResidenza %>
       		</select>
        </td>
		<td class="l" colspan="2">&nbsp;</td>
	</tr>
</table>
<br>
<table cellspacing="0" cellpadding="0">	
	<tr>
		<td>
			<INPUT class="bottone" type="submit" name="INSERISCI" value="Conferma">
		</td>
	</tr>
</table>

<input type="HIDDEN" name="Action" value="<%=lAction%>"> 
<input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">
<input type="HIDDEN" name="<%=ICostantiPagoPA.RADIO_COD_PERSONA %>" value="F">
<input type="HIDDEN" name="<%=ICostantiPagoPA.CAMPO_ID_SOGGETTO%>" value="<%=civilmenteObbligato.getIdSoggetto()%>">
<input type="HIDDEN" name="modalita" value="<%=modalita%>">
<input type="HIDDEN" name="<%=ICostantiPagoPA.CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE%>" value="<%if(civilmenteObbligato.getResidenza()!= null)%><%=civilmenteObbligato.getResidenza().getFlgDomicilioDifensore()%>">
</form>

<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadInserisciPersonaFisica");

frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_COGNOME %>","req","Il campo Cognome è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_COGNOME %>","maxlen=100","La lunghezza massima per il cognome è di 100 caratteri");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_COGNOME %>","alpha");

frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_NOME %>","req","Il campo Nome è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_NOME %>","maxlen=100","La lunghezza massima per il nome è di 100 caratteri");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_NOME %>","alpha");

frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA %>","req","Il campo Giorno di Nascita è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>","numeric");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>","gt=1");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_GIORNO_DATA_NASCITA%>","lt=31");

frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA %>","req","Il campo Mese di Nascita è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>","numeric");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>","gt=1");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_MESE_DATA_NASCITA%>","lt=12");

frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA %>","req","Il campo Anno di Nascita è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>","numeric");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_ANNO_DATA_NASCITA%>","lt=3000");

frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_COD_FISCALE %>","alphanumeric");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>","alpha");

frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_CAP_RESIDENZA%>","numeric");
frmvalidator.addValidation("<%= ICostantiPagoPA.CAMPO_DESC_COMUNE_ESTERO_RESIDENZA%>","alpha");

frmvalidator.setAddnlValidationFunction("Verify");
</script>
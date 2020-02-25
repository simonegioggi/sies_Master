<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina di caricamento inserimento restituzione ordine di consegna --%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaNotificataModel"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>

<jsp:useBean id="dataeditabile" 		scope="request" class="java.lang.String" />
<jsp:useBean id="StrdataInizioPena" 	scope="request" class="java.lang.String" />
<jsp:useBean id="penaresidua" 			scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel" />
<jsp:useBean id="magistrato" 			scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel" />

<!-- LoadInserisciRestituzioneOrdinediConsegna -->
<html>
<head>
<title>[S.I.E.S.] - Gestione Misure sicurezza - Esecuzione MS - Inserimento Restituzione Ordine di Consegna</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
var desktop;

// Lista dei MAGISTRATI
function ListaMagistrati(a_formname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
}

function Verify() {
<%
if (penaresidua.getFlagErgastolo() == null
		|| (penaresidua.getFlagErgastolo() != null
			&& !penaresidua.getFlagErgastolo().equals("S")
			&& !penaresidua.getFlagErgastolo().equals("D"))) {
	if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
	if (document.roc.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length == 1)
		document.roc.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value = '0'
			+ document.roc.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
	if (document.roc.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length == 1)
		document.roc.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value = '0'
			+ document.roc.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;

	data_to_verify = document.roc.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value
		+ '/' + document.roc.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value
		+ '/' + document.roc.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;

	if (!ControllaData(data_to_verify)) {
		alert('Data fine pena non valida');
		document.roc.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.focus();
		return false;
	}
<%
	}
}

List msNotificate = (List) request.getAttribute("elencoMisureNotificate");
if (!msNotificate.isEmpty()) {
%>
	// controllo consistenza campi obbligatori
	var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
	// CONTROLLO DATA TRASMISSIONE
	if (document.roc.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value.length == 1)
		document.roc.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value = '0'
			+ document.roc.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value;
	if (document.roc.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value.length == 1)
		document.roc.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value = '0'
			+ document.roc.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value;
	var data_to_verify = document.roc.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value
		+ '/'  +document.roc.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value
		+ '/' + document.roc.<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>.value;
   	if (!ControllaData(data_to_verify)) {
   		alert("Data Trasmissione NON Valida");
   		document.roc.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.focus();
 		return false;
	}
 	// 1) Controllo: data di sistema deve essere >= Data Trasmissione
 	if (!CompareDate(data_to_verify, data_sistema)) {
		alert("Data Trasmissione non può essere superiore alla data odierna!");
		document.roc.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.focus();
		return false;
 	}

 	// CONTROLLO DATA EMISSIONE
	if (document.roc.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length == 1)
		document.roc.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value = '0'
			+ document.roc.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
	if (document.roc.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length == 1)
		document.roc.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value = '0'
		+ document.roc.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
	data_to_verify = document.roc.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value
		+ '/' + document.roc.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value
		+ '/' + document.roc.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
	if (!ControllaData(data_to_verify)) {
		alert("Data di emissione non valida: scegliere un Ordine di Esecuzione dalla Lista");
   		document.roc.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
 		return false;
	}
 	// 1) Controllo: data di sistema deve essere >= Data Emissione
 	if (!CompareDate(data_to_verify, data_sistema)) {
		alert("Data Emissione non può essere superiore alla data odierna!");
		document.roc.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		return false;
	}

 	// Controllo su AUTORITA per esecuzione	
	if (document.roc.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value == "-"
			|| document.roc.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value == "") {
		alert("ERRORE: Inserire la Tipologia Provvedimento nell'Ordine di Esecuzione scelto dalla lista");
		return false;
	}
	if (document.roc.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value == "-"
			|| document.roc.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value == "") {
		alert("ERRORE: Inserire l'Oggetto Provvedimento nell'Ordine di Esecuzione scelto dalla lista");
		document.roc.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
		return false;
	}
<%
}
%>
} <%-- Chiude function Verify --%>

function insertIT (dataEmissione, codTipoProvvedimento, descrTipoProvvedimento, codMotivo, descrMotivo, codSede,
		descrSede, codTipoAutorita, descrTipoAutorita, idAutoritaEsterna, idNotifica, idEvento, indirizzo) {
	var d = dataEmissione.split("-");
	var giornoDataEmissione = d[0];
	var meseDataEmissione = d[1];
	var annoDataEmissione = d[2];
	document.roc.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value = giornoDataEmissione;
	document.roc.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value = meseDataEmissione;
	document.roc.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value = annoDataEmissione;
	document.roc.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value = codTipoProvvedimento;
    // @emma intervento post collaudo 11.3 (deve essere preimpostato sempre al valore 'Richiesta')	
	//document.roc.descrTipoProvvedimento.value = descrTipoProvvedimento;
	document.roc.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value = codMotivo;
	document.roc.descrMotivo.value = codMotivo;
	document.roc.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = codSede;
	document.roc.<%=ICostantiAutoritaEsterna.CAMPO_DESC_SEDE%>.value = descrSede;
	document.roc.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value = codTipoAutorita;
	document.roc.descrTipoAutorita.value = descrTipoAutorita;
	document.roc.<%=ICostantiAutoritaEsterna.CAMPO_ID_AUTORITA_ESTERNA%>.value = idAutoritaEsterna;
	document.roc.<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>.value = idNotifica;
	document.roc.<%=ICostantiEvento.CAMPO_EVE_ID_EVENTO%>.value = idEvento;
	document.roc.<%=ICostantiAutoritaEsterna.CAMPO_NOTE_E%>.value = indirizzo;
	window.parent.close();
}
</script>
</head>
<body class="corpo">
	<table>
		<tr>
			<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
			<td class="LBG"><font class="label">Funzione:</font>&nbsp;&nbsp;<font class="campo">Inserimento Restituzione Ordine di Consegna per Esecuzione Misure Sicurezza</font></td>
		</tr>
	</table>
	<br>
	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
	<br>
	<FORM method="POST" name="roc" action="<%=IWebConstants.PG_MAIN%>">
		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActInserisciRestituzioneOrdineConsegna">
		<input type="HIDDEN" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica())%>" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>">
		<table cellspacing=0 cellpadding=0 width="95%">
			<tr>
				<td class="l" width="20%">Posizione Giuridica</td>
				<td class="L" colspan="5">
					<font class="campo"><%=posizioneluogoaltra.getPosizioneGiuridica().getDescrPosizioneGiuridica()%></font>
				</td>
			</tr>
<%
if (penaresidua.getIdPenaResidua() != null
		&& (penaresidua.getFlagErgastolo() == null
			|| (penaresidua.getFlagErgastolo() != null
				&& !penaresidua.getFlagErgastolo().equals("S")
				&& !penaresidua.getFlagErgastolo().equals("D")))) {
	if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0)) {
	} else {
%>
			<tr>
				<td class="l">Reclusione</td>
				<td class="l" colspan=2>
					<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(), "0")%>&nbsp;</font>
					<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(), "0")%>&nbsp;</font>
					<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(), "0")%></font>
				</td>
				<td class="l">Multa</td>
				<td class="l" colspan=2>
					<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;
					<font class="l">Euro</font></td>
			</tr>
<%
	}
	if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0)) {
	} else {
%>
			<tr>
				<td class="l">Arresto</td>
				<td class="l" colspan=2>
					<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(), "0")%>&nbsp;</font>
					<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(), "0")%>&nbsp;</font>
					<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(), "0")%></font>
				</td>
				<td class="l">Ammenda</td>
				<td class="l" colspan=2>
					<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;
					<font class="l">Euro</font>
				</td>
			</tr>
<%
	}
} // CHIUDO if (penaresidua...)
%>
			<tr>
<%
if (penaresidua.getDataInizio() != null) {
%>
				<td class="l">Data Decorrenza Pena</td>
				<td class="L"><font class="campo"><%=StrdataInizioPena%>&nbsp;</font></td>
<%
}
if (penaresidua.getFlagErgastolo() != null) {
	if (penaresidua.getFlagErgastolo().equals("S")) {
%>
				<td class="l">Pena Detentiva</td>
				<td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
	} else if (penaresidua.getFlagErgastolo().equals("D")) {
%>
				<td class="l">Pena Detentiva</td>
				<td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
	}
}

if (penaresidua.getFlagErgastolo() == null
		|| (penaresidua.getFlagErgastolo() != null
		&& !penaresidua.getFlagErgastolo().equals("S")
		&& !penaresidua.getFlagErgastolo().equals("D"))) {
	if (dataeditabile.equals("S")
			&& penaresidua.getDataFinePresunta() != null) {
%>
				<td class="l">Data Fine Pena</td>
				<td class="L" colspan=2>
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd"))%>"type="text" size="2" maxlength="2"
						name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>"
						onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"MM"))%>"type="text" size="2" maxlength="2"
						name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>"
						onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"yyyy"))%>" type="text" size="4" maxlength="4"
						name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>"
						onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
				</td>
<%
	} else if (penaresidua.getDataFine() != null) {
		if (penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
%>
				<td class="l">Data Fine Pena</td>
				<td class="L" colspan=2>
					<font class="campo">
						<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd"))%>-
					 	<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"MM"))%>-
					 	<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"yyyy"))%>
					 </font>
					<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>">
					<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),	"MM"))%>" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>"> 
					<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),	"yyyy"))%>" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>">
				</td>
<%
		} else {
%>
				<td class="l">Data Fine Pena</td>
				<td class="lRosso" colspan=2>
					<font class="lRosso">
						<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd"))%>-
					   	<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"MM"))%>-
					   	<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"yyyy"))%>
					</font>
					<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>">
					<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>"> 
					<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%>" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>">
				</td>
<%
		}
	}
}
%>
			</tr>
		</table>
		<!-- Misure di Sicurezza già presenti -->
		<table cellspacing=0 cellpadding=0 width="95%">
<%
List lMisure = (List) request.getAttribute("listaMisure");
MisuraSicurezzaModel lMis = null;
if (lMisure.size() > 0) {
	Iterator itx = lMisure.iterator();
	while (itx.hasNext()) {
		lMis = (MisuraSicurezzaModel) itx.next();
 %>
			<tr>
				<td class=l width="20%">Misura di Sicurezza da espiare</td>
				<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%></font></td>
				<td class=l>Anni</td>
				<td class=c><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(), "0")%></font></td>
				<td class=l>Mesi</td>
				<td class=c><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(), "0")%></font></td>
				<td class=l>Giorni</td>
				<td class=c><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(), "0")%></font></td>
			</tr>
<%
	}
}
%>
		</table>
		<br>
		<!--Magistrato Firmatario -->
		<table cellspacing=0 cellpadding=0 width="95%">
			<tr>
				<td class="Titolo" colspan="2">Magistrato Firmatario</td>
			</tr>
			<tr>
				<td class="L" width="20%">Magistrato Firmatario <font class=ob>(*)</font></td>
				<td class="L">
					<input readonly value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="35"> 
					<input readonly value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="35"> 
					<a href="Javascript:ListaMagistrati('roc');">
						<img src="/images/filefolder.gif" border="0">
					</a>
					<input type="HIDDEN" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato())%>" type="text" name="<%=ICostantiEvento.CAMPO_COD_MAGISTRATO%>">
				</td>
			</tr>
		</table>
		<br>
		<table cellspacing=0 cellpadding=0 width="95%">
			<tr>
				<td class="Titolo" colspan="4">Lista Ordini di Esecuzione</td>
			</tr>
<%
if (Utils.isPresent(msNotificate)) {
%>
			<tr>
      			<td class="int">Data Emissione</td>
      			<td class="int">Oggetto</td>
      			<td class="int">Destinatari per l'esecuzione</td>
      			<td class="int" width=5%>Azioni</td>
    		</tr>
<%
	Iterator iter = msNotificate.iterator();
	MisuraSicurezzaNotificataModel msnm = null;
	while (iter.hasNext()) {
		msnm = (MisuraSicurezzaNotificataModel) iter.next();
%>
    		<tr>
       			<td class="c">
            		<%=StringUtils.toStringJSP(DateUtils.getDateToString(msnm.getDataEmissione(),"dd-MM-yyyy"), "-")%>
          		</td>
          		<td class="c">
<%
		if (msnm.getCodMotivoProvvedimento() != null) {
%>
          			<%=StringUtils.toStringJSP(msnm.getDescrTipoProvvedimento(), "-")%>&nbsp;<%=StringUtils.toStringJSP(msnm.getDescrMotivoProvvedimento(), "-")%>
<%
		}
%>
          		</td>
          		<td class="c">
            		<%=StringUtils.toStringJSP(msnm.getDescrTipoAutorita(), "-")%> di <%=StringUtils.toStringJSP(msnm.getDescrComune(), "-")%>
          		</td>
          		<td class="c">
            		<a href="Javascript:insertIT(
            				'<%=StringUtils.cStrForJS(DateUtils.getDateToString(msnm.getDataEmissione(), "dd-MM-yyyy"))%>',
                   			'<%=StringUtils.cStrForJS(msnm.getCodTipoProvvedimento())%>',
                   			'<%=StringUtils.cStrForJS(msnm.getDescrTipoProvvedimento())%>',
                   			'<%=StringUtils.cStrForJS(msnm.getCodMotivoProvvedimento())%>',
                   			'<%=StringUtils.cStrForJS(msnm.getDescrMotivoProvvedimento())%>',
                   			'<%=StringUtils.cStrForJS(msnm.getCodComune())%>',
                   			'<%=StringUtils.cStrForJS(msnm.getDescrComune())%>',
                   			'<%=StringUtils.cStrForJS(msnm.getCodTipoAutorita())%>',
                   			'<%=StringUtils.cStrForJS(msnm.getDescrTipoAutorita())%>',
                   			'<%=msnm.getAutEstIdAutoritaEsterna()%>',
                   			'<%=msnm.getIdNotifica()%>',
                   			'<%=msnm.getIdEvento()%>',
                   			'<%=StringUtils.cStrForJS(msnm.getNote())%>');">
						<img align="middle" src="/images/fileselected.gif" border="0" Title="Seleziona Ordine Esecuzione per la Restituzione">
		            </a>
          		</td>
        	</tr>
<%
	}
} else {
%>
			<tr>
      			<td class="c" colspan="4">Nessun Ordine di Esecuzione presente</td>
      		</tr>
<%
}
%>
		</table>
<%
if (Utils.isPresent(msNotificate)) {
%>
    	<br>
		<table cellspacing=0 cellpadding=0 width="95%">
			<tr>
				<td class="Titolo" colspan="4">Restituzione Ordine di Esecuzione</td>
			</tr>
		</table>
		<!-- Date del Provvedimento -->
		<table cellspacing=0 cellpadding=0 width="95%">
			<tr>
				<td class="l" width="20%">Data Emissione <font class="ob">(*)</font></td>
				<td class="L">
					<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" readonly="readonly"> - 
					<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" readonly="readonly"> - 
					<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" readonly="readonly"> &nbsp;
				</td>
				<td class="l">Data Trasmissione <font class="ob">(*)</font></td>
				<td class="L">
					<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
					<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
					<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
				</td>
			</tr>
			<tr>
				<td class="l">Tipologia Provvedimento <font class="ob">(*)</font></td>
				<td class="L" colspan="3">
					<input value="" type="hidden" name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>">
					<input value="" type="hidden" name="<%=ICostantiEvento.CAMPO_EVE_ID_EVENTO%>">
					<!-- @emma intervento post collaudo 11.3 (deve essere preimpostato sempre al valore 'Richiesta') -->
					<select  name="descrTipoProvvedimento">
          				<option value = "Richiesta"  />Richiesta
					</select>				
				</td>
			</tr>
			<tr>
				<td class="l">Oggetto <font class="ob">(*)</font></td>
			<td class="l">
			<input value="" type="hidden" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>">	
			<select id="descrMotivo"  name="descrMotivo" >
					<option value="defaultValue"  disabled="disabled">-</option>
					<option value="1131"  disabled="disabled">Esecuzione Misure Sicurezza</option>
					<option value="1126"  disabled="disabled">Ordine di Consegna per Esecuzione Misure Sicurezza</option>    
					<option value="1128"  disabled="disabled">Ordine Esecuzione per Internamento</option>   
		   </select> 
		   </td>
			</tr>
			<tr>
				<td class="L">Autorità Destinazione</td>
				<td class="L" colspan="3">
					<input type="hidden" value="" name="<%=ICostantiAutoritaEsterna.CAMPO_ID_AUTORITA_ESTERNA%>">
					<input type="hidden" value="" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
					<input type="hidden" value="" name="<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>">
					<input readonly="readonly" value="" type="text" name="descrTipoAutorita" size="100">
				</td>
			</tr>
			<tr>
				<td class="l">Sede</td>
				<td class="L">
					<input value="" type="hidden" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>">
					<input readonly="readonly" value="" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_DESC_SEDE%>" size="50">
				</td>
				<td class="l">Indirizzo</td>
    			<td class="L">
      				<TEXTAREA readonly="readonly" title="Indirizzo" name="<%=ICostantiAutoritaEsterna.CAMPO_NOTE_E%>" cols="35"></textarea>
    			</td>
			</tr>
			<tr>
        		<td class="l">Note</td>
        		<td class="L" colspan="3">
          			<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols="100" rows="3"></textarea>
        		</td>
      		</tr>
		</table>
		<br>
		<!-- Bottone di Conferma -->
		<table>
			<tr>
				<td class="lNoBord">
					<INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
				</td>
			</tr>
		</table>
<%
}
%>
	</form>
	<script language="JavaScript" type="text/javascript">
 	var frmvalidator = new Validator("roc");
<%
if (Utils.isPresent(msNotificate)) {
%>
  	frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","req","Il campo Giorno Emissione dell''Atto è obbligatorio");
  	frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","numeric");
  	frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","req","Il campo Mese Emissione dell''Atto è obbligatorio");
  	frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","numeric");
  	frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","req","Il campo Anno Emissione dell''Atto è obbligatorio");
  	frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","numeric");
  	frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","gt=1900");
  	frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","lt=2050");
<%
}
if (penaresidua.getFlagErgastolo() == null
		|| (penaresidua.getFlagErgastolo() != null
			&& !penaresidua.getFlagErgastolo().equals("S")
			&& !penaresidua.getFlagErgastolo().equals("D"))) {
	if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
		frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena è obbligatorio");
	  	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");
		frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena è obbligatorio");
		frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");
		frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena è obbligatorio");
		frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
		frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
		frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2050");
<%
	}
}
%>
	</script>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina di caricamento modifica restituzione ordine di consegna --%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaNotificataModel"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>

<jsp:useBean id="eventonotifica"	scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizione"			scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="magistrato" 		scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="misuraNotificata"	scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaNotificataModel"/>

<!-- LoadModificaRestituzioneOrdineConsegna -->
<html>
<head>
<title>[S.I.E.S.] - Gestione Misure sicurezza - Esecuzione MS - Modifica Restituzione Ordine di Consegna</title>
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
     		<td class="LBG">
     			<a href="Javascript:window.print();">
     				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
     			</a>
     		</td>
	 		<td class="LBG">
	 			<font class="label">Funzione:</font>&nbsp;&nbsp;
	 			<font class="campo">Modifica Restituzione Ordine di Consegna per Esecuzione Misure Sicurezza</font>
	 		</td>
	 		<td class="LBG">
				<a href="javascript:history.go(-1);">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
				</a>
			</td>
		</tr>
 	</table>
	<br>
	<jsp:include page="/jsp/files/siap/siep/misurasicurezza/TestataSoggettoperModificheMS.jsp"/>
	<br>
	<FORM method="POST" name="roc" action="<%=IWebConstants.PG_MAIN%>">
 		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActModificaRestituzioneOrdineConsegna">
 		<input type="HIDDEN" value="<%=eventonotifica.getEvento().getIdEvento()%>" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>">
		<table cellspacing=0 cellpadding=0 width="95%">
  			<tr>
				<td class="l" width="20%">Posizione Giuridica </td>
    			<td class="L">
       				<font class="campo">
       					<%=posizione.getDescrPosizioneGiuridica()%>
       				</font>
    			</td>
   			</tr>
   		</table>
   		<!-- Misure di Sicurezza presenti -->
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
		 		<td class="l" width="20%">Misura di Sicurezza da espiare</td>
		 		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%></font></td>
		 		<td class="l">Anni</td>
		 		<td class="c"><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(), "0")%></font></td>
				<td class="l">Mesi</td>
		 		<td class="c"><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(), "0")%></font></td>
		 		<td class="l">Giorni</td>
		 		<td class="c"><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(), "0")%></font></td>
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
					<input readonly value="<%=StringUtils.toStringJSP(magistrato.getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="35">
					<input readonly value="<%=StringUtils.toStringJSP(magistrato.getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>" maxlength="35" size="35">
	     			<a href="Javascript:ListaMagistrati('roc');">
		       			<img src="/images/filefolder.gif" border="0">
		     		</a>
		    		<input type="hidden" value="<%=StringUtils.toStringJSP(magistrato.getCodMagistrato() )%>" name="<%= ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  >
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
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" readonly="readonly"> - 
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" readonly="readonly"> - 
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" readonly="readonly"> &nbsp;
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
					<input value="<%=StringUtils.toStringJSP(misuraNotificata.getCodTipoProvvedimento())%>" type="hidden" name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>">
					<input value="<%=StringUtils.toStringJSP(eventonotifica.getEvento().getEveIdEvento())%>" type="hidden" name="<%=ICostantiEvento.CAMPO_EVE_ID_EVENTO%>">
					<!-- @emma intervento post collaudo 11.3 (deve essere preimpostato sempre al valore 'Richiesta') -->					
					<select  name="descrTipoProvvedimento">
          				<option value = "Richiesta"  />Richiesta
					</select>	
				</td>
			</tr>
			<tr>
				<td class="l">Oggetto <font class="ob">(*)</font></td>
				<td class="L" colspan="3">
					<input value="<%=StringUtils.toStringJSP(misuraNotificata.getCodMotivoProvvedimento())%>" type="hidden" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>">
					
					<select id="descrMotivo"  name="descrMotivo" >
					<option value="defaultValue" disabled="disabled">
					<%
					if(misuraNotificata.getCodMotivoProvvedimento()!=null && "1128".equals(misuraNotificata.getCodMotivoProvvedimento())){
						
					%>
					<%= StringUtils.toStringJSP(misuraNotificata.getDescrTipoProvvedimento(), "-")%>&nbsp; 
					<%} %>
					<%=StringUtils.toStringJSP(misuraNotificata.getDescrMotivoProvvedimento(), "-")%></option>
					<option value="1131" disabled="disabled" >Esecuzione Misure Sicurezza</option>
					<option value="1126" disabled="disabled" >Ordine di Consegna per Esecuzione Misure Sicurezza</option>    
					<option value="1128" disabled="disabled" >Ordine Esecuzione per Internamento</option>   
<%--           				<option value="<%=StringUtils.toStringJSP(misuraNotificata.getCodMotivoProvvedimento(), "-")%>"  /><%=StringUtils.toStringJSP(misuraNotificata.getDescrMotivoProvvedimento(), "-")%>
 --%>			   </select>
					
				</td>
			</tr>
<%
	String indirizzo = "", autorita = "", sede = "", note = "";
	String idNotifica = "", codTipoAutorita = "", idAutoritaEsterna = "", codSede = "";
	if (Utils.isPresent(eventonotifica.getNotifiche())
			&& eventonotifica.getNotifiche().length > 0) {
		indirizzo = eventonotifica.getNotifiche()[0].getNote();
		if (eventonotifica.getNotifiche()[0].getAutoritaEsterna() != null) {
			autorita = eventonotifica.getNotifiche()[0].getAutoritaEsterna().getDescrTipoAutorita();
			sede = eventonotifica.getNotifiche()[0].getAutoritaEsterna().getDescrSede();
			codSede = eventonotifica.getNotifiche()[0].getAutoritaEsterna().getCodSede();
			codTipoAutorita = eventonotifica.getNotifiche()[0].getAutoritaEsterna().getCodTipoAutorita();
			idAutoritaEsterna = "" + eventonotifica.getNotifiche()[0].getAutoritaEsterna().getIdAutoritaEsterna();
		} else {
			if (eventonotifica.getNotifiche()[0].getIstitutoDetenzione() != null) {
				autorita = "Istituto Detenzione " + eventonotifica.getNotifiche()[0].getIstitutoDetenzione().getDescrTipoIstituto();
				sede = eventonotifica.getNotifiche()[0].getIstitutoDetenzione().getDescrizione();
				codSede = eventonotifica.getNotifiche()[0].getIstitutoDetenzione().getCodComune();
				codTipoAutorita = eventonotifica.getNotifiche()[0].getIstitutoDetenzione().getCodTipoIstituto();
				idAutoritaEsterna = eventonotifica.getNotifiche()[0].getIstitutoDetenzione().getIdIstitutoDetenzione();
			}
		}
		idNotifica = "" + eventonotifica.getNotifiche()[0].getIdNotifica();
	}
	if (Utils.isPresent(eventonotifica.getCampoNote())
			&& eventonotifica.getCampoNote().length > 0)
		note = eventonotifica.getCampoNote()[0].getDescr();
%>
			<tr>
				<td class="L">Autorità Destinazione</td>
				<td class="L" colspan="3">
					<input type="hidden" value="<%=idAutoritaEsterna%>" name="<%=ICostantiAutoritaEsterna.CAMPO_ID_AUTORITA_ESTERNA%>">
					<input type="hidden" value="<%=codTipoAutorita%>" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
					<input type="hidden" value="<%=idNotifica%>" name="<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>">
					<input readonly="readonly" value="<%=autorita%>" type="text" name="descrTipoAutorita" size="100">
				</td>
			</tr>
			<tr>
				<td class="l">Sede</td>
				<td class="L">
					<input value="<%=codSede%>" type="hidden" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>">
					<input readonly="readonly" value="<%=sede%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_DESC_SEDE%>" size="50">
				</td>
				<td class="l">Indirizzo</td>
				<td class="L">
      				<TEXTAREA readonly="readonly" title="Indirizzo" name="<%=ICostantiAutoritaEsterna.CAMPO_NOTE_E%>" cols="35"><%=indirizzo%></textarea>
    			</td>
			</tr>
			<tr>
        		<td class="l">Note</td>
        		<td class="L" colspan="3">
          			<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols="100" rows="3"><%=note%></textarea>
        		</td>
      		</tr>
		</table>
		<br>
		<!-- Bottone di Conferma -->
		<table>
			<tr>
				<td class="lNoBord">
				<INPUT class="bottone" type="submit" name="M" value="Conferma" onClick="javascript:return Verify();"></td>
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
%>
	</script>
</body>
</html>
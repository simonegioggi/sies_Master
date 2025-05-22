<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-33: aggiunta pagina --%>
<%@page import="f3b.util.Utils"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.istitutodetenzione.action.ICostantiIstitutoDetenzione"%>
<%@ page import="siap.siep.rateizzazionepp.action.ICostantiRateizzazionePP"%>

<jsp:useBean id="magistrato"         	scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="eventonotifica"   		scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="autorita" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"         		scope="request" class="java.lang.String"/>
<jsp:useBean id="importoDaPagare"		scope="request" class="java.math.BigDecimal"/>
<jsp:useBean id="domicilio"        		scope="request" class="java.lang.String"/>
<jsp:useBean id="comuneUDS"				scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUDS"             	scope="request" class="java.lang.String"/>

<%
FascicoloSiepModel fsm = (FascicoloSiepModel) session.getAttribute("fascicolo");
PosizioneGiuridicaModel pgm = posizioneluogoaltra.getPosizioneGiuridica();
AltraCausaModel acm = posizioneluogoaltra.getAltraCausa();

Date dataEmissione    = DateUtils.getSysDate();
Date dataTrasmissione = DateUtils.getSysDate();
BigDecimal idEventoNotifica = new BigDecimal(0);
if (eventonotifica.getEvento().getIdEvento() != null) {
	dataEmissione    = eventonotifica.getEvento().getDataEmissione();
	dataTrasmissione = eventonotifica.getNotifiche()[0].getDataInvio();
	idEventoNotifica = eventonotifica.getEvento().getIdEvento();
}

if (pgm == null)
	pgm = new PosizioneGiuridicaModel();

if (acm == null)
	acm = new AltraCausaModel();
%>
<html>
<head>
<title>[S.I.E.S.] - Gestione Provvedimento Trasmissione Atti per la Conversione</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>  
<script language="JavaScript">
function ListaMagistrati(a_formname) {
	var desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
}

function ListaComuniUfficio(a_formname,a_fieldname) {
	codTipoUfficio = document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.value;
	if (codTipoUfficio == '-') {
		alert("Selezionare il tipo di ufficio emittente");
    	document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.focus();
  	} else {
    	desktop = window.open("/jsp/Main.jsp?Action=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&TipoUfficio="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  	}
}

function Verify() {
	var dataOdierna = "<%=DateUtils.getSysDate("dd")%>-<%=DateUtils.getSysDate("MM")%>-<%=DateUtils.getSysDate("yyyy")%>";
	// Data Emissione
  	if (document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length == 1)
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value = '0' +
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
	if (document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length == 1)
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value = '0' +
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

	var data_to_verify = document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value
		+ '/' + document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value
		+ '/' + document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

	if (!ControllaData(data_to_verify)) {
		alert('Data di emissione non valida');
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		return false;
	}
	if (!CompareDate(data_to_verify, dataOdierna)) {
		alert('La Data Emissione non può essere superiore alla data odierna');
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		return false;
	}

	// Data Trasmissione
	if (document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length == 1)
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value = '0' +
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
	if (document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length == 1)
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value = '0' +
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

	var data_to_verify = document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value
		+ '/' + document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value
		+ '/' + document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

	if (!ControllaData(data_to_verify)) {
		alert('Data di Trasmissione non valida');
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
		return false;
	}
	if (!CompareDate(data_to_verify, dataOdierna)) {
		alert('La Data di Trasmissione non può essere superiore alla data odierna');
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
		return false;
	}

	//=============================================================
	// controllo obbligatorieta' magistrato
	//=============================================================
	if (document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_COD_MAGISTRATO%>.value == ""
			|| document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiEvento.CAMPO_COD_MAGISTRATO%>.value == "-") {
		alert("Il Magistrato è obbligatorio");
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiMagistrato.CAMPO_COGNOME%>.focus();
		return false;
	}
	if (document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiRateizzazionePP.CAMPO_COD_SEDE_UDS%>.value == "") {
		alert("La sede del Magistrato di Sorveglianza è obbligatoria");
		document.LoadInserisciTrasmissioneAttiConversione.<%=ICostantiRateizzazionePP.CAMPO_COD_SEDE_UDS%>.focus();
		return false;
 	}
	return true;
}
</script>
</head>
<body class="corpo">
<table>
	<tr>
		<td class="LBG">
		  	<a href="Javascript:window.print();">
		    	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
		    </a>
		</td>
		<td class="LBG">
		  	<font class="label">Funzione :</font>&nbsp;&nbsp;
<%
if ("I".equals(modalita)) {
%>
			<font class="campo">Inserimento Provvedimento Trasmissione Atti per la Conversione</font>
<%
} else if ("M".equals(modalita)) {
%>
			<font class="campo">Modifica Provvedimento Trasmissione Atti per la Conversione</font>
<%
}
%>
		</td>
		<td class="LBG">
			<a href="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActGrigliaOrdineIngiunzioneAltriProvvedimenti">
				<img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
			</a>      
		</td>
	</tr>
</table>
  
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
  
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="l" width="25%">Posizione Giuridica</td>
		<td class="L">
		  	<font class="campo">
<%
if (fsm.getFlagAltraCausa() != null && fsm.getFlagAltraCausa().equals("S")) {
%>
				DETENUTO PER ALTRA CAUSA - <%=acm.getDescrTipoPosGiuridica()%>
<%
} else {
%>
				<%=pgm.getDescrPosizioneGiuridica()%>
<%
}
%>
			</font>
			<input type="HIDDEN" value="<%=StringUtils.toStringJSP(pgm.getCodPosizioneGiuridica())%>" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>">
		</td>
	</tr>
</table>
<FORM method="POST" name="LoadInserisciTrasmissioneAttiConversione" action="<%=IWebConstants.PG_MAIN%>">
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
      	<td class="L">
<%
if ("I".equals(modalita)) {
%>
    		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.rateizzazionepp.action.ActInserisciTrasmissioneAttiConversione">
<%
} else if ("M".equals(modalita)) {
%>
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.rateizzazionepp.action.ActModificaTrasmissioneAttiConversione">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>">
<%
}
%>
      	</td>
    </tr>
</table>
<%
if (Utils.isPresent(domicilio)) {
%>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
	  	<td class="Titolo" colspan="4">Dati Relativi alla Residenza del Soggetto</td>
	</tr>
	<tr>
		<td class="L" width="25%">Residenza</td>
		<td class="L">
  			<font class="campo">Domicilio Imposto in:&nbsp;<%=domicilio%></font>
		</td>
	</tr>
</table>
<%
}
%>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
	  	<td class="Titolo" colspan="2">Dati Richiesta Conversione</td>
	</tr>
	<tr>
	  	<td class="l" width="25%">Pena Pecuniaria Sostitutiva: Importo</td>
	  	<td class="L">
  			<font class="campo"><%=StringUtils.toEuroFormat(importoDaPagare)%></font>
		</td>
	</tr>
</table>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
	  	<td class="Titolo" colspan="2">Magistrato Firmatario</td>
	</tr>
	<tr>
      	<td class="l" width="25%">Magistrato<font class="ob">(*)</font></td>
      	<td class="L">
        	<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25">
        	<input readonly title= "Nome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="25">
       		<a href="Javascript:ListaMagistrati('LoadInserisciTrasmissioneAttiConversione');">
         		<img src="/images/filefolder.gif" border=0>
       		</a>
       	</td>
   		<td>
       		<input type="hidden" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato())%>" name="<%=ICostantiEvento.CAMPO_COD_MAGISTRATO%>">
   		</td>
	</tr>
</table>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
	  	<td class="Titolo" colspan="3">Destinatari</td>
	</tr>
	<tr>
	  	<td class="l" width="25%">Magistrato di Sorveglianza&nbsp;<font class="ob">(*)</font></td>
		<td class="L">
			<select Title="Magistrato di Sorveglianza" class="small" name="<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>">
				<%=tipoUDS%>
			</select>
		</td>
		<td class="L">
			<input type="text" title="ufficio" value="<%=StringUtils.toStringJSP(comuneUDS, "")%>" name="<%=ICostantiRateizzazionePP.CAMPO_COD_SEDE_UDS%>" maxlength="35" size="25">
			<a href="Javascript:ListaComuniUfficio('LoadInserisciTrasmissioneAttiConversione','<%=ICostantiRateizzazionePP.CAMPO_COD_SEDE_UDS%>');">
				<img src="/images/filefolder.gif" border="0">
			</a>
		</td>
	</tr>
</table>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="L" width="25%">Data Emissione</td>
		<td class="L">
			<input value="<%=DateUtils.getDateToString(dataEmissione, "dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getDateToString(dataEmissione, "MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getDateToString(dataEmissione, "yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
		<td class="L" width="25%">Data Trasmissione</td>
		<td class="L">
			<input value="<%=DateUtils.getDateToString(dataTrasmissione, "dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getDateToString(dataTrasmissione, "MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getDateToString(dataTrasmissione, "yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	  	</td>
	</tr>
</table>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
  	<tr>
    	<td class="lNoBord"><INPUT class="bottone" type="submit" value="Conferma"></td>
  	</tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadInserisciTrasmissioneAttiConversione");  
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
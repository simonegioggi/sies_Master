<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>

<jsp:useBean id="penaComplessivaSanzioneSostitutiva" scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"/>
<jsp:useBean id="evento"             scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="modalita"           scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="autoritaEsternaE"   scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaN"   scope="request" class="java.lang.String"/>

<jsp:useBean id="magistrato"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>

<jsp:useBean id="tipoIstituto"       scope="request" class="java.lang.String"/>
<jsp:useBean id="StrdataInizioPena"  scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>
<jsp:useBean id="StrdataFinePenaA"   scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="dataeditabile"      scope="request" class="java.lang.String"/>

<jsp:useBean id="IdEvento"      scope="request" class="java.lang.String"/>

<%-- 20191002 [SG]: intervento post collaudo 11.3 -- refactoring pagina --%>
<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
if (lPosizione == null)
	lPosizione = new PosizioneGiuridicaModel();
if (lLuogoDetenzione == null)
	lLuogoDetenzione = new LuogoDetenzioneModel();
if (lAltraCausa == null)
	lAltraCausa = new AltraCausaModel();
%>
<html>
<head>
<title>[S.I.E.S.] - Gestione evento </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
var desktop;
function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

function ListaComuni(a_formname,a_fieldname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

// Chiamata lista Avvocati.
function ListaAvvocati(a_formname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname+"&modalita=BREVE", "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
}

function Verify() {
	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
		document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
		document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
	var data_to_verify = document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
  	if (!ControllaData(data_to_verify)) {
    	alert('Data di emissione non valida');
 		return false;
	}
<%
if ((!lPosizione.getCodPosizioneGiuridica().equals("07") && !lPosizione.getCodPosizioneGiuridica().equals("10")
		&& lPosizione.getCodPosizioneGiuridica().equals("16") && lPosizione.getCodPosizioneGiuridica().equals("20")
    	&& lPosizione.getCodPosizioneGiuridica().equals("46") && lPosizione.getCodPosizioneGiuridica().equals("47"))
		|| (((lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
				&& (penaresidua.getDataFinePresunta()!= null && penaresidua.getDataFine() == null)))) {
	if (((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null
			&& !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))) {
    	if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==1)
		document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==1)
		document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;
	var data_to_verifica = document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;
  	if (!ControllaData(data_to_verifica)) {
		alert('Data fine pena non valida');
		return false;
	}
<%
		}
  	}
}
%>
	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
		document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
		document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;
	var data_to_verify = document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="") {
		alert("Il Cognome del Magistrato è obbligatorio");
		return false;
	}
	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="") {
		alert("Il Nome del Magistrato è obbligatorio");
		return false;
	}
	var campo = document.LoadInserisciOrdineEsecuzione.<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>.value;
	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '10'
			&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '07'
			&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '02'
			&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '04'
			&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '16'
			&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '20'
			&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '46'
			&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '47'
			&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '12') {
		if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "") {
			alert("Autorità Destinazione obbligatorio");
			return false;
  		}
	}
<%
if (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")
		&& !lAltraCausa.getCodTipoPosGiuridica().equals("23")) {
%>
	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "") {
		alert("Autorità Destinazione obbligatorio");
		return false;
	}
<%
}
if (lPosizione.getCodPosizioneGiuridica().equals("07") || lPosizione.getCodPosizioneGiuridica().equals("10")
		|| lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")
		|| lPosizione.getCodPosizioneGiuridica().equals("16") || lPosizione.getCodPosizioneGiuridica().equals("20")
		|| lPosizione.getCodPosizioneGiuridica().equals("46") || lPosizione.getCodPosizioneGiuridica().equals("47")
		|| lPosizione.getCodPosizioneGiuridica().equals("12")) {
	if (lFascicoloAssociato.getFlagAltraCausa()== null || (lFascicoloAssociato.getFlagAltraCausa() !=null
			&& lFascicoloAssociato.getFlagAltraCausa().equals("N"))) {
%>
	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-") {
		alert("Autorità Destinazione obbligatoria");
		return false;
	}
<%
	}
}
if (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
	if (lAltraCausa.getCodTipoPosGiuridica().equals("23")) {
%>
	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-") {
		alert("Autorità Destinazione obbligatoria");
		return false;
	}
<%
	}
}
if (lPosizione.getCodPosizioneGiuridica().equals("12")) {
%>
	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>.value == "-") {
		alert("Altra autorità di polizia obbligatoria");
		return false;
	}
<%
}
%>
}

function ListaMagistrati(a_formname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
}

function ListaUDS(a_formname,a_fieldname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
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
EventoModel lProvvedimento = new EventoModel();
String lAzione = new String();
if (modalita.equals("I")) {
	lProvvedimento = new EventoModel(evento);
	lAzione = "siap.siep.ordineesecuzione.action.ActInserisciOEDetenutoQC";
    if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
%>
			<font class="campo">Emissione Ordine di Esecuzione - DETENUTO PER ALTRA CAUSA</font>
<%
	} else {
%>
			<font class="campo">Emissione Ordine di Esecuzione - <%=lPosizione.getDescrPosizioneGiuridica()%></font>
<%
	}
} else if (modalita.equals("M")) {
	lProvvedimento = new EventoModel(evento);
	lAzione = "siap.siep.ordineesecuzione.action.ActModificaOEDetenutoQC";
%>
          	<font class="campo">Modifica di Ordine di Esecuzione</font>
<%
}
%>
		</td>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<form method="POST" name="LoadInserisciOrdineEsecuzione" action="<%=IWebConstants.PG_MAIN%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordineesecuzione.action.ActInserisciOrdineEsecuzioneSanSos">
<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(IdEvento)%>">
<table>
	<tr>
		<td class="l">Posizione Giuridica</td>
        <td class="L" colspan=5>
          	<font class="campo">
<%
if (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
%>
				DETENUTO PER ALTRA CAUSA 
<% 
	if (lAltraCausa != null && lAltraCausa.getDescrTipoPosGiuridica() != null
			&& !"".equals(lAltraCausa.getDescrTipoPosGiuridica()) && !"-".equals(lAltraCausa.getDescrTipoPosGiuridica())) {
							    
%>              
				<%=lAltraCausa.getDescrTipoPosGiuridica()%>
<%
	}
} else {
%>
              	<%=lPosizione.getDescrPosizioneGiuridica()%>
<%
}
%>
			</font>
          	<input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
		</td>
	</tr>
<%
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
	// modifica relativa al tipo istituto
    if (lAltraCausa.getIstitutoDetenzione() != null) {
%>
	<tr>
	  	<td class="l">Detenuto presso</td>
	  	<td class="L" colspan=5>
	  		<font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
			&nbsp;di&nbsp;<font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
        </td>
	</tr>
<%
	}
	if (lAltraCausa.getAltroLuogo() != null) {
%>
	<tr>
	  	<td class="l">Altro Luogo </td >
	  	<td class="L" colspan=5>
	    	<font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
	  	</td>
	</tr>
<%
	}
} else if (lLuogoDetenzione.getIstitutoDetenzione() != null && lLuogoDetenzione.getDescrTipoIstituto() != null
		&& !"".equals(lLuogoDetenzione.getDescrTipoIstituto()) && !"-".equals(lLuogoDetenzione.getDescrTipoIstituto())) {
%>
	<tr>
	 	<td class="l">Detenuto presso </td>
	 	<td class="L" colspan=5>
	  		<font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
			&nbsp;di&nbsp;<font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
        </td>
	</tr>
<%
}
// Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
if (lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02")
		|| lPosizione.getCodPosizioneGiuridica().equals("04"))) {
	if (lLuogoDetenzione.getAltroLuogo() != null) {
%>
	<tr>
	  	<td class="l">Altro Luogo </td>
	  	<td class="L" colspan=5>
	    	<font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>
		</td>
	</tr>
<%
	}
}
%>
	<tr>
<%
// fine modifica relativa al tipo istituto
if ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S")
		&& !penaresidua.getFlagErgastolo().equals("D"))) {
	if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0)
			&& (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0)
			&& (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)) {
		// NOTHING TO DO???
	} else {
%>
		<td class="l">Reclusione</td>
		<td class="l" colspan=2>
		  	<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
		</td>
		<td class="l">Multa</td>
		<td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
	}
%>
	</tr>
   	<tr>
<%
	if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0)
			&& (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0)
			&& (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0)) {
		// NOTHING TO DO???
	} else {
%>
		<td class="l" >Arresto</td>
      	<td class="l" colspan=2>
			<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         	<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         	<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
     	</td>
      	<td class="l">Ammenda</td>
      	<td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
	}
}
%>
	<tr>
<%
if ((!lPosizione.getCodPosizioneGiuridica().equals("07") && !lPosizione.getCodPosizioneGiuridica().equals("10")
		&& !lPosizione.getCodPosizioneGiuridica().equals("16") && !lPosizione.getCodPosizioneGiuridica().equals("20")
		&& !lPosizione.getCodPosizioneGiuridica().equals("46")
        && !lPosizione.getCodPosizioneGiuridica().equals("47")) || (lFascicoloAssociato.getFlagAltraCausa() != null
        &&  lFascicoloAssociato.getFlagAltraCausa().equals("S"))) {
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
}
if ((!lPosizione.getCodPosizioneGiuridica().equals("07")
		&& !lPosizione.getCodPosizioneGiuridica().equals("10")
        && !lPosizione.getCodPosizioneGiuridica().equals("16")
        && !lPosizione.getCodPosizioneGiuridica().equals("20")
        && !lPosizione.getCodPosizioneGiuridica().equals("46")
        && !lPosizione.getCodPosizioneGiuridica().equals("47"))
        || (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))) {
	if ((penaresidua.getFlagErgastolo() == null)
			|| (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S")
			&& !penaresidua.getFlagErgastolo().equals("D"))) {
		if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan=2>
		  	<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
			-
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
			-
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
<%
		} else if (penaresidua.getDataFine() != null) {
			if (penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan=2>
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%></font>
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
		</td>
<%
			} else {
%>
		<td class="l">Data Fine Pena</td>
		<td class="lRosso" colspan=2>
		  	<font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%></font>
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
		</td>
<%
			}
		}
	}
}
%>
		<input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
	</tr>
	<tr>
  		<td class="l">Data Emissione</td>
  		<td class="L" colspan=2>
    		<input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
		</td>
		<td class="l">Data Trasmissione</td>
		<td class="L"colspan=2>
			<input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > -
			<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
 	</tr>
</table>
<table width=100%>
	<tr><td class="Titolo" colspan=6> Magistrato </td></tr>
	<tr>
		<td class="l">Magistrato</td>
		<td class="L" colspan="3">
			<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
			<input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="25">
			<a href="Javascript:ListaMagistrati('LoadInserisciOrdineEsecuzione');">
			  	<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
		<td>
			<input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato())%>" type="text" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
		</td>
	</tr>
	<tr>
  		<td class="Titolo" colspan=6>Destinatario per Esecuzione </td>
	</tr>
<%
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
%>
	<tr>
 		<td class="l" width=20%>Autorità Destinazione <font class=ob>(*)</font></td>
<%
	if (lAltraCausa.getCodTipoPosGiuridica().equals("23")) {
%>
 		<td class="L" colspan="3">
       		<select  Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
        		<%=autoritaEsternaE%>
       		</select>
      	</td>
	</tr>
	<tr>
      	<td class="l">Sede <font class=ob>(*)</font></td>
      	<td class="L">
<% // modifica relativa al tipo istituto %>
	        <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>"  maxlength="35" size="35">
<% // fine modifica relativa al tipo istituto %>
       		<a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
          		<img src="/images/filefolder.gif" border=0>
        	</a>
		</td>
      	<td class="l">Indirizzo</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=30 ></textarea>
      	</td>
<%
	} else {
		if (posizioneluogoaltra != null && lAltraCausa != null && lAltraCausa.getIstitutoDetenzione() != null) {
%>
		<td class="l">
			<input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getDescrComune())%>" size=50>
			<input type="hidden"  Title="Istituto" name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lAltraCausa.getIstDetIdIstitutoDetenzione()%>" size=50>
			<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
				<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
<%
		} else {
%>
		<td class="l">
			<input readonly Title="Istituto" name="Comune" value="" size=50>
			<input type="hidden"  Title="Istituto" name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
			<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
				<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
<%
		}
%>
		<td class="l">Note</td>
		<td class="L">
			<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=35></textarea>
		</td>
<%
	}
%>
	</tr>
	<tr><td>&nbsp;</td></tr>
<%
} else {
%>
	<tr>
		<td class="l" width=20%>Autorità Destinazione <font class=ob>(*)</font></td>
<%
	if (lPosizione.getCodPosizioneGiuridica().equals("07") || lPosizione.getCodPosizioneGiuridica().equals("10")
			|| lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")
			|| lPosizione.getCodPosizioneGiuridica().equals("16") || lPosizione.getCodPosizioneGiuridica().equals("20") || lPosizione.getCodPosizioneGiuridica().equals("46")
			|| lPosizione.getCodPosizioneGiuridica().equals("47") || lPosizione.getCodPosizioneGiuridica().equals("12")) {
%>
     	<td class="L" colspan="3">
       		<select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
        		<%=autoritaEsternaE%>
       		</select>
     	</td>
	</tr>
    <tr>
      	<td class="l">Sede <font class=ob>(*)</font></td>
      	<td class="L">
        	<input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>"  maxlength="35" size="35">
       		<a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
          		<img src="/images/filefolder.gif" border=0>
       		</a>
      	</td>
     	<td class="l">Indirizzo</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=30></textarea>
      	</td>
<%
	} else {
		// modifica relativa al tipo istituto
  		if (lLuogoDetenzione.getIstitutoDetenzione() == null) {
%>
		<td class="l">
			<input readonly Title="Istituto" name="Comune" value="" size=50>
			<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
			<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
				<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
<%
		} else {
%>
		<td class="l">
			<input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size=50>
			<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>" size=50>
			<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
				<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
<%
		}
%>
		<td class="l">Note</td>
		<td class="L">
		  	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols=35></textarea>
		</td>
<%
	}
%>
	</tr>
    <tr><td>&nbsp;</td></tr>
<%
}
if (lPosizione.getCodPosizioneGiuridica().equals("12")) {
%>
	<tr>
		<td class="L">Magistrato di Sorveglianza</td>
		<td class="L" colspan="3">
	      	<input title="Sede Magistrato Sorveglianza" value="" type="text" name="<%= ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS %>"  maxlength="35" size="35">
	   		<a href="Javascript:ListaUDS('LoadInserisciOrdineEsecuzione','<%=ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS %>');">
	     		<img src="/images/filefolder.gif" border=0>
	     	</a>
		</td>
	</tr>
<%
}
%>
	<tr>
		<td class="Titolo" colspan=6>Destinatario per Notifica</td>
	</tr>
<%
int lIdxAvv = 0;
Iterator lItxAvv = avvocati.iterator();
while(lItxAvv.hasNext()) {
	AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
</table>
<table width=100%>
	<tr>
		<td class="l">Per Avvocato&nbsp;
	    	<font class="campo">
	      		<%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
			</font>
			&nbsp;Foro di&nbsp;
			<font class="campo">
	  			<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
			</font>
			&nbsp;Difensore di&nbsp;
			<font class="campo">
	  			<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
			</font>
			<input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
		</td>
	</tr>
</table>
<table width=100%>
	<tr>
		<td class="l">Autorità Destinazione</td >
	  	<td class="L" colspan=3>
	    	<select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
				<%=autoritaEsternaN%>
	        </select>
	    </td>
	</tr>
	<tr>
		<td class="l">Sede </td><td class="L">
	   		<input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
			<a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
	    		<img src="/images/filefolder.gif" border=0>
	  		</a>
		</td>
	  	<td class="l">Note</td>
	 	<td class="L">
	    	<textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols=35 ></textarea>
	   	</td>
	</tr>
	<tr><td>&nbsp;</td>
<%
	lIdxAvv++;
}
%>
	</tr>
<%
if (lPosizione.getCodPosizioneGiuridica().equals("12")) {
%>
   	<tr>
		<td class="L">Altra Autorità di polizia <font class=ob>(*)</font></td>
     	<td class="L" colspan="3">
       		<select  Title="Altra Autorità di polizia" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>">
        		<%=autoritaEsternaE%>
       		</select>
     	</td>
	</tr>
    <tr>
      	<td class="l">Sede <font class=ob>(*)</font></td>
      	<td class="L">
        	<input title="Sede Altra Autorità di polizia" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>"  maxlength="35" size="35">
       		<a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>');">
          		<img src="/images/filefolder.gif" border=0>
        	</a>
		</td>
     	<td class="l">Indirizzo</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_C%>" cols=30></textarea>
      	</td>
	</tr>
<%
}
%>
	<tr>
  		<td class="lNoBord" colspan="2">
    		<br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    	</td>
  	</tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadInserisciOrdineEsecuzione");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");

frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","req","Il campo Giorno Invio dell'Atto è obbligatorio");
frmvalidator.addValidation("<%=  ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");

frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","req","Il campo Mese Invio dell'Atto è obbligatorio");
frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");

frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","req","Il campo Anno Invio dell'Atto è obbligatorio");
frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2050");

<%
if ((!lPosizione.getCodPosizioneGiuridica().equals("07") && !lPosizione.getCodPosizioneGiuridica().equals("10")
		&& !lPosizione.getCodPosizioneGiuridica().equals("16") && !lPosizione.getCodPosizioneGiuridica().equals("20")
		&& !lPosizione.getCodPosizioneGiuridica().equals("46")
		&& !lPosizione.getCodPosizioneGiuridica().equals("47")) || (((lFascicoloAssociato.getFlagAltraCausa() != null
		&& lFascicoloAssociato.getFlagAltraCausa().equals("S")) && (penaresidua.getDataFinePresunta()!= null
		&& penaresidua.getDataFine() == null)))) {
	if (((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null
			&& !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))) {
  		if (dataeditabile.equals("S")  && penaresidua.getDataFinePresunta() != null) {
 %>
frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");

frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");

frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2050");
<%
		}
 	}
}
if (lPosizione.getCodPosizioneGiuridica().equals("07") || lPosizione.getCodPosizioneGiuridica().equals("10")
		|| lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")
		|| lPosizione.getCodPosizioneGiuridica().equals("16") || lPosizione.getCodPosizioneGiuridica().equals("20") || lPosizione.getCodPosizioneGiuridica().equals("46")
		|| lPosizione.getCodPosizioneGiuridica().equals("47" )|| lPosizione.getCodPosizioneGiuridica().equals("12")) {
	if (lFascicoloAssociato.getFlagAltraCausa()== null || (lFascicoloAssociato.getFlagAltraCausa() != null
			&& lFascicoloAssociato.getFlagAltraCausa().equals("N"))) {
%>
frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","req","Luogo Autorità Destinazione obbligatoria");
frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","alphabetic");
<%
	}
}
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
	if (lAltraCausa.getCodTipoPosGiuridica().equals("23")) {
%>
frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","req","Luogo Autorità Destinazione obbligatoria");
frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","alphabetic");
<%
	}
}
%>
frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>","req","Luogo Autorità Destinazione obbligatoria");
frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>","alphabetic");
<%
if (lPosizione.getCodPosizioneGiuridica().equals("12")) {
%>
frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>","req","Luogo Altra Autorità di polizia obbligatoria");
frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>","alphabetic");
<%
}
%>
</script>
</body>
</html>
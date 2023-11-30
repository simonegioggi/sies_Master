<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-33: aggiunta pagina per Gestione Trasmissione Atti per l'Esecuzione (pena sostitutiva) --%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.istitutodetenzione.action.ICostantiIstitutoDetenzione"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel"%>
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="magistratocompetente" 	scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         	scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="penaCompPenaSost"		scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"/>
<jsp:useBean id="misurecautelari"		scope="request" class="java.util.Vector"/>
<jsp:useBean id="lAnnotazione"        	scope="request" class="java.lang.String"/>
<jsp:useBean id="lSedeUfficio"        	scope="request" class="java.lang.String"/>
<jsp:useBean id="residenzaassociata"  	scope="request" class="siap.sico.residenza.model.ResidenzaAssociataModel"/>
<jsp:useBean id="tipoAutoritaPolizia"	scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"           	scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipoAutoritaC0"   		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAutoritaAll"  		scope="request" class="java.lang.String"/>

<%
FascicoloSiepModel fsm = (FascicoloSiepModel) session.getAttribute("fascicolo");

PosizioneGiuridicaModel pgm = posizioneluogoaltra.getPosizioneGiuridica();

String codPosizioneGiuridica = "";
if (pgm == null)
	pgm = new PosizioneGiuridicaModel();
else if (pgm.getCodPosizioneGiuridica() != null)
	codPosizioneGiuridica = pgm.getCodPosizioneGiuridica().trim();

LuogoDetenzioneModel ldm = posizioneluogoaltra.getLuogoDetenzione();
if (ldm == null)
	ldm = new LuogoDetenzioneModel();

AltraCausaModel acm = posizioneluogoaltra.getAltraCausa();
if (acm == null)
	acm = new AltraCausaModel();

PenaComplessivaSanzioneSostitutivaModel pcssm = penaCompPenaSost;
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Trasmissione Atti per l'Esecuzione Pena Sostitutiva</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
var desktop;
function ListaUDS(a_formname, a_fieldname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  }

function ListaUDSMIN(a_formname, a_fieldname, a_typename) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename,"Ricerca_UDSMIN", "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}

function sceltaLista() {
	if (document.getElementById('<%=MinorMask.ComboMagistratoId%>').value == 'UDSM')
		ListaUDSMIN('LoadTrasmissioneAttiEsecuzione','<%=ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO%>', document.getElementById('<%=MinorMask.ComboMagistratoId%>').value);
	else
		ListaUDS('LoadTrasmissioneAttiEsecuzione','<%=ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO%>');
}

function ListaMagistrati(a_formname, a_fieldname, a_field2, a_field3) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
}

function ListaComuni(a_formname, a_fieldname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function ListaIstitutoDetenzione(a_formname, a_fieldname, a_field2) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

function Verify() {
	var dataOdierna = "<%=DateUtils.getSysDate("dd")%>-<%=DateUtils.getSysDate("MM")%>-<%=DateUtils.getSysDate("yyyy")%>";

	// Data Emissione
  	if (document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length == 1)
		document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value = '0'
		+ document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
	if (document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length == 1)
		document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value = '0'
		+ document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

	var data_to_verify = document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value
		+ '/' + document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value
		+ '/' + document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

	if (!ControllaData(data_to_verify)) {
		alert('Data di emissione non valida');
		document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		return false;
	}
	if (!CompareDate(data_to_verify, dataOdierna)) {
		alert('La Data Emissione non può essere superiore alla data odierna');
		document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		return false;
	}

	// Data Trasmissione
	if (document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length ==1 )
		document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value = '0'
		+ document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
	if (document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length == 1)
		document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value = '0'
		+ document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

	var data_to_verify = document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value
		+ '/' + document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value
		+ '/' + document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

	if (!ControllaData(data_to_verify)) {
		alert('Data di Trasmissione non valida');
		document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
		return false;
	}
	if (!CompareDate(data_to_verify, dataOdierna)) {
		alert('La Data di Trasmissione non può essere superiore alla data odierna');
		document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
		return false;
	}

	// Autorita x la Notifica
	if (typeof document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%> !== "undefined") {
  		if (document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-") {
			alert("Selezionare l'autorita' per la notifica al condannato");
			document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
			return false; 
		}
		if (document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value == "") {
			alert("Selezionare la sede dell'autorita' per la notifica al condannato");
			document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
			return false; 
  		}
	} else if (typeof document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%> !== "undefined") {
		if (document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "") {
			alert("Selezionare l'istituto di detenzione per la notifica al condannato");
			document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.focus();
			return false; 
    	}          
 	} /*else {
	    alert("destinatario sconosciuto");
	    return false;
 	}*/

	// SEDE UFFICIO
	if (document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO%>.value == '') {
		alert("Il Campo Sede è obbligatorio");
		document.LoadTrasmissioneAttiEsecuzione.<%=ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO%>.focus();
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
   				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
   			</a>
   		</td>
   		<td class="LBG">
			<font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Trasmissione Atti per l'Esecuzione Pena Sostitutiva</font>
  	 	</td>
   	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<FORM method="POST" name="LoadTrasmissioneAttiEsecuzione" action="<%=IWebConstants.PG_MAIN%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActTrasmissioneAttiEsecuzione">
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="l" width="20%">Posizione Giuridica</td>
		<td class="L" colspan="5">
   			<font class="campo">
<%
if (fsm.getFlagAltraCausa() != null && fsm.getFlagAltraCausa().equals("S")) {
%>
				DETENUTO PER ALTRA CAUSA
<%
} else {
%>
         		<%=pgm.getDescrPosizioneGiuridica()%>
<%
}
%>
			</font>
<%
if (pgm.isLibero() && residenzaassociata != null && residenzaassociata.getResidenza() != null) {
%>   
 			&nbsp;Residenza&nbsp;
          	<font class="campo"><%=residenzaassociata.getResidenza().getIndirizzo()%>&nbsp;<%=residenzaassociata.getResidenza().getDescrComune()%></font>
<%
}
%>           
		</td>
	</tr>
<%
if (fsm.getFlagAltraCausa() != null && fsm.getFlagAltraCausa().equals("S")) {
	if (acm.getIstitutoDetenzione() != null) {
%>
	<tr>
	  	<td class="l">Detenuto presso </td>
	  	<td class="L" colspan="5"><font class="campo"><%=acm.getIstitutoDetenzione().getDescrTipoIstituto()%></font></td>
	</tr>
<%
		if (acm.getAltroLuogo() != null) {
%>
	<tr>
		<td class="l">Altro Luogo</td >
		<td class="L" colspan="5"><font class="campo"><%=StringUtils.toStringJSP(acm.getAltroLuogo())%></font></td>
	</tr>
<%
		}
	}
} else if (ldm.getIstitutoDetenzione() != null) {
%>
	<tr>
	 	<td class="l">Detenuto presso </td>
	 	<td class="L" colspan="5">
	  		<font class="campo"><%=ldm.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
			&nbsp;di&nbsp;<font class="campo"><%=ldm.getIstitutoDetenzione().getDescrComune()%></font>
  		</td>
	</tr>
<%
}
// Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
if (codPosizioneGiuridica != null && (codPosizioneGiuridica.equals("02") || codPosizioneGiuridica.equals("04"))) {
	if (ldm.getAltroLuogo() != null) {
%>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="L" colspan="5">
			<font class="campo"><%=StringUtils.toStringJSP(ldm.getAltroLuogo())%></font>
		</td>
	</tr>
<%
	}
}
PenaComplessivaModel lPenCompMod = pcssm.getPenaComplessiva();
if (lPenCompMod != null) {
%>
	<tr>
		<td class="L"><font class="label">Pena irrogata in sentenza:</font></td>
		<td class="L" colspan="5">
<%
	if ((lPenCompMod.getNumAnniReclusione() != null && lPenCompMod.getNumAnniReclusione().compareTo(new BigDecimal(0)) != 0)
			|| (lPenCompMod.getNumMesiReclusione() != null && lPenCompMod.getNumMesiReclusione().compareTo(new BigDecimal(0)) != 0)
			|| (lPenCompMod.getNumGiorniReclusione() != null && lPenCompMod.getNumGiorniReclusione().compareTo(new BigDecimal(0)) != 0)) {
%>
			<font class="campo">Reclusione</font>
			<font class="label">Anni</font>
			<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniReclusione(),"0")%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiReclusione(),"0")%></font>
			<font class="label">Giorni</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;
<%
	}
	if (lPenCompMod.getImportoMulta() != null && lPenCompMod.getImportoMulta().compareTo(new BigDecimal(0)) != 0) {
%>
          	<font class="label">Multa </font>
          	<font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoMulta())%></font>&nbsp;&euro;&nbsp;
<%
	}
	if ((lPenCompMod.getNumAnniArresto() != null && lPenCompMod.getNumAnniArresto().compareTo(new BigDecimal(0)) != 0)
			|| (lPenCompMod.getNumMesiArresto() != null && lPenCompMod.getNumMesiArresto().compareTo(new BigDecimal(0)) != 0)
			|| (lPenCompMod.getNumGiorniArresto() != null && lPenCompMod.getNumGiorniArresto().compareTo(new BigDecimal(0)) != 0)) {
%>
			<font class="campo">Arresto</font>
			<font class="label">Anni</font>
			<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniArresto(),"0")%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiArresto(),"0")%></font>
			<font class="label">Giorni</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;
<%
	}
	if (lPenCompMod.getImportoAmmenda() != null && lPenCompMod.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0) {
%>
          	<font class="label">Ammenda </font>
          	<font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;
<%
	}
	if (lPenCompMod.getCodTipoPenaDetentiva().equals("03") || lPenCompMod.getCodTipoPenaDetentiva().equals("04")) {
%>
          	<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getDescrTipoPenaDetentiva())%></font>
<%
		if (lPenCompMod.getCodTipoPenaDetentiva().equals("04")) {
			if (lPenCompMod.getNumAnniIsolamentoDiurno() != null) {
%>
			<font class="label">Anni</font>
			<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniIsolamentoDiurno(),"0")%></font>
<%
			}
			if (lPenCompMod.getNumMesiIsolamentoDiurno() != null) {
%>
			<font class="label">Mesi</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiIsolamentoDiurno(),"0")%></font>
<%
			}
			if (lPenCompMod.getNumGiorniIsolamentoDiurno() != null) {
%>
			<font class="label">Giorni</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniIsolamentoDiurno(),"0")%></font>
<%
			}
		}
	}
%>
		</td>
    </tr>
<%
}
if (pcssm != null) {
	SanzioneSostitutivaModel ssm = pcssm.getSanzioneSostitutiva();
	if (ssm != null && ssm.getIdSanzioneSostitutiva() != null) {
%>
	<tr>
		<td class="L"><font class="label">Pena Sostitutiva applicata:</font></td>
		<td class="L" colspan="5">
<%
		if ((ssm.getNumAnni() != null && ssm.getNumAnni().compareTo(new BigDecimal(0)) != 0)
				|| (ssm.getNumMesi() != null && ssm.getNumMesi().compareTo(new BigDecimal(0)) != 0)
				|| (ssm.getNumGiorni() != null && ssm.getNumGiorni().compareTo(new BigDecimal(0)) != 0)) {
%>
			<font class="campo"><%=StringUtils.toStringJSP(ssm.getDescrTipoSanzione())%>&nbsp;</font>
			<font class="label">Anni:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(ssm.getNumAnni(), "0")%>&nbsp;</font>
			<font class="label">Mesi:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(ssm.getNumMesi(), "0")%>&nbsp;</font>
			<font class="label">Giorni:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(ssm.getNumGiorni(), "0")%></font>
<%
		}
		if (ssm.getSanzionePecuniariaMulta() != null && ssm.getSanzionePecuniariaMulta().intValue() != 0) {
%>
			<font class="label">&nbsp;Pena&nbsp;Pec.&nbsp;Multa&nbsp;</font>
			<font class="campo"><%=StringUtils.toEuroFormat(ssm.getSanzionePecuniariaMulta())%>&nbsp;</font>&euro;
			<br>
<%
		}
		if (ssm.getSanzionePecuniariaAmmenda() != null && ssm.getSanzionePecuniariaAmmenda().intValue() != 0) {
%>
			<font class="label">&nbsp;Pena&nbsp;Pec.&nbsp;Ammenda&nbsp;</font>
			<font class="campo"><%=StringUtils.toEuroFormat(ssm.getSanzionePecuniariaAmmenda())%>&nbsp;</font>&euro;
<%
		}
%>
		</td>
	</tr>
<%
	}
}
if (misurecautelari != null && !misurecautelari.isEmpty()) {
%>
	<tr>
		<td class="l">Misure Cautelari Computate:</td>
<%
	MisuraCautelareModel lMisCauMod = null;
	Iterator lItx = misurecautelari.iterator();
	int conta = 0;
	while (lItx.hasNext()) {
	  	lMisCauMod = (MisuraCautelareModel) lItx.next();
	  	if (lMisCauMod.getFlagComputabile().equals("S") && lMisCauMod.getDataFine() != null) {
	    	conta++;
	    	if (conta == 1) {
%>
		<td class="l" colspan="5">
			<font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getDescrTipoMisura())%>&nbsp;</font>
			<font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getNumAnni(), "0")%>&nbsp;</font>
			<font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getNumMesi(), "0")%>&nbsp;</font>
			<font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getNumGiorni(), "0")%></font>
		</td>
<%
			} else { // devo scrivere un nuovo rigo
%>
	</tr>
	<tr>
		<td class="l">&nbsp;</td>
		<td class="l" colspan="5">
		    <font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getDescrTipoMisura())%>&nbsp;</font>
		    <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getNumAnni(), "0")%>&nbsp;</font>
		    <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getNumMesi(), "0")%>&nbsp;</font>
		    <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getNumGiorni(), "0")%></font>
  		</td>
<%
			} 
		} 
	}
%>   
	</tr>  
<%
}
if (penaresidua.getIdPenaResidua() != null) {
%>
	<tr>
  		<td class="l">Pena da espiare:</td>
<% 
	if (penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0) {
		
	} else {
%>
		<td class="l" colspan=2>Reclusione
			<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
         	&nbsp;Multa&nbsp;
          	<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font>
		</td>
<%
	}
    if (penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0
    		&& penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0
    		&& penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0) {
    	
    } else {
%>
		<td class="l" >Arresto
			<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      		&nbsp;Ammenda&nbsp;
      		<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
      	</td>
<%
	}
%>
	</tr>
<%
}
if (penaresidua != null && penaresidua.getFlagSanzioneSostitutiva() != null && "S".equals(penaresidua.getFlagSanzioneSostitutiva())) {
%>
	<tr>
		<td class="l">Pena sostitutiva da espiare:</td>  
		<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(penaresidua.getDescrTipoSanzione())%>&nbsp;</font>
			<font class="label">Anni:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniSS(), "0")%>&nbsp;</font>
			<font class="label">Mesi:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiSS(), "0")%>&nbsp;</font>
			<font class="label">Giorni:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniSS(), "0")%></font>
<% 
	if ((penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0)
			|| (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0)) {
%>
          	<font class="label">&nbsp;Sanz.&nbsp;Pec.</font>
<%
		if (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0) {
%>
          	<font class="campo">&nbsp;Multa&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoMultaSS())%>&nbsp;</font>&euro;
<%
		}
		if (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0) {
%>
			<font class="campo">&nbsp;Ammenda&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoAmmendaSS())%>&nbsp;</font>&euro;
<%
		}
	}
%> 
		</td>
    </tr>
<%
}
%>
</table>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="l" width="20%">Data Emissione</td>
        <td class="L">
			<input title = "Giorno Data Emissione" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title = "Mese Data Emissione" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title = "Anno Data Emissione" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
        </td>
        <td class="l">Data Trasmissione</td>
        <td class="L">
			<input title = "Giorno Data Trasmissione" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title = "Mese Data Trasmissione" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title = "Anno Data Trasmissione" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
        </td>
	</tr>
   	<tr>
		<td class="Titolo" width="100%" colspan=6> Magistrato </td>
   </tr>
   <tr>
		<td class="l">Magistrato
		<td class="L" colspan="3">
			<input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato())%>" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>">
			<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25">
			<input readonly title= "Nome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="25">
			<a href="Javascript:ListaMagistrati('LoadTrasmissioneAttiEsecuzione','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%=ICostantiMagistrato.CAMPO_COGNOME %>','<%=ICostantiMagistrato.CAMPO_NOME %>');">
				<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
	</tr>      
   	<tr>
      	<td class="l" colspan="4">
<%
if (lAnnotazione != null && lAnnotazione.equals("S")) {
%>
			<input type="checkbox" checked name="ritrasmissione" value="S"> Ritrasmissione in seguito a restituzione atti
<%
} else {
%>
			<input type="checkbox" name="ritrasmissione" value="S"> Ritrasmissione in seguito a restituzione atti
<%
}
%>
		</td>
    </tr>
    <tr><td>&nbsp;</td></tr>
   	<tr>
		<td class="l">Destinatario</td>
      	<td class="l" colspan="3">
			<%=MinorMask.comboMagistrato("true", MinorMask.SorveglianzaUfficio)%>
       	</td>
	</tr>
    <tr>
		<td class="l">Sede</td>
      	<td class="l" colspan="3">
        	<font class="campo">
          		<input Title="Luogo Ufficio Sorveglianza" value="<%=lSedeUfficio%>" name="<%=ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO%>" size=35 type="text">
          		<a href="Javascript: sceltaLista();">
            		<img src="/images/filefolder.gif" border=0>
          		</a>
        	</font>
      	</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
<%-- SEZIONE DESTINATARI:
LIBERO: 7, 10, 16, 17, 46, 47, 89, 90 											--> ufficio di sorveglianza + Avvocato + Altro destinatario
CONDANNATO IN ARRESTI DOMICILIARI PER QUESTA CAUSA: 2, 23, 70, 71, 72 			--> ufficio di sorveglianza + Avvocato + Altro destinatario + Forze di polizia per notifica condannato
CUSTODIA CAUTELARE PER QUESTA CAUSA IN REGIME DI DETENZIONE: 1, 22, 55, 73 		--> ufficio di sorveglianza + Avvocato + Altro destinatario + Istituto di detenzione
CUSTODIA CAUTELARE ALTRA CAUSA REGIME DI ARRESTI DOMICILIARI: 78, 79, 80, 81 	--> ufficio di sorveglianza + Avvocato + Altro destinatario
CUSTODIA CAUTELARE ALTRA CAUSA IN REGIME DI DETENZIONE: 75, 76, 77 				--> ufficio di sorveglianza + Avvocato + Altro destinatario
dove: Forze di polizia per notifica condannato --> select * from cg_ref_codes t where t.rv_domain = 'TIPO_AUTORITA' and t.RV_ALT2_VALUE = 'AP' (30 rows)
--%>
<%
List<String> libero = Arrays.asList("07", "10", "16", "17", "46", "47", "89", "90"); 	// UDS + AVV + AD
List<String> ciadpqc = Arrays.asList("02", "23", "70", "71", "72");						// UDS + AVV + AD + FP
List<String> ccpqcrdad = Arrays.asList("01", "22", "55", "73");							// UDS + AVV + AD + ID
List<String> ccacrdad = Arrays.asList("78", "79", "80", "81");							// UDS + AVV + AD
List<String> ccacird = Arrays.asList("75", "76", "77");									// UDS + AVV + AD
if (libero.contains(codPosizioneGiuridica)
		|| ciadpqc.contains(codPosizioneGiuridica)
		|| ccpqcrdad.contains(codPosizioneGiuridica)
		|| ccacrdad.contains(codPosizioneGiuridica)
		|| ccacird.contains(codPosizioneGiuridica)) {
%>
	<tr>
    	<td class="Titolo" colspan="6">Notifica al Difensore</td>
  	</tr>
<%
int cont = 0;
int numAvvocati = avvocati.size();
Iterator iter = avvocati.iterator();
while (iter.hasNext()) {
	AvvocatoSiepModel lAvv = (AvvocatoSiepModel) iter.next();
%>
	<tr>
		<td class="l" width="20%">Per Avvocato</td>
		<td class="L" colspan="3">
			<input type="hidden" name="indexAvvocati" value="<%=cont%>">
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
            <input type="HIDDEN" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>">
		</td>
	</tr>
    <tr>
      	<td class="l">Autorita' Destinazione</td>
      	<td class="L" colspan="3">
         	<select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_AVV_<%=lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep()%>">
           		<%=tipoAutoritaC0%>
           	</select>
		</td>
	</tr>
	<tr>
		<td class="l">Sede </td>
		<td class="L">
            <input title="Sede Foro Avvocato" type="text" maxlength="35" size="35" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" 
                   id="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>_AVV_<%=lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep()%>">
<%
	if (numAvvocati < 2) {
%>
			<a href="Javascript:ListaComuni('LoadTrasmissioneAttiEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
				<img src="/images/filefolder.gif" border="0">
			</a>
<%
	} else {
%>
             <a href="Javascript:ListaComuni('LoadTrasmissioneAttiEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[<%=cont%>]');">
				<img src="/images/filefolder.gif" border="0">
			</a>
<%
	}
%>
		</td>
		<td class="l">Note</td>
		<td class="L">
			<textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols="35" id="<%=ICostantiNotifica.CAMPO_NOTE%>_AVV_<%=lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep()%>"></textarea>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
<%
	cont++;
}
%>
	<tr>
    	<td class="Titolo" colspan="6">Notifica altro Destinatario</td>
  	</tr>
  	<tr>
    	<td class="L">Autorita' Destinazione</td>
    	<td class="L" colspan="3">
	     	<select Title="Autorita Altro Destinatario" class="small" id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>">
	      		<%=tipoAutoritaAll%>
	     	</select>
    	</td>
	</tr>
	<tr>
	    <td class="L">Sede</td>
	    <td class="L">
      		<input type="text" maxlength="35" size="35" title="Sede Autorita Altro Destinatario" id="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>">
	      	<a href="Javascript:ListaComuni('LoadTrasmissioneAttiEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>');">
	        	<img src="/images/filefolder.gif" border="0">
	      	</a>
		</td>
	    <td class="L">Indirizzo</td>
	    <td class="L">
	      	<TEXTAREA title="Note Altro Destinatario" cols="30" id="<%=ICostantiNotifica.CAMPO_NOTE_C%>" name="<%=ICostantiNotifica.CAMPO_NOTE_C%>"></textarea>
	    </td>
	</tr>
<%
	if (ciadpqc.contains(codPosizioneGiuridica)) { // Forze di Polizia
%>
	<tr><td>&nbsp;</td></tr>
	<tr>
    	<td class="Titolo" colspan="6">Notifica al Condannato</td>
  	</tr>
  	<tr>
    	<td class="L">Autorita' Destinazione</td>
    	<td class="L" colspan="3">
	     	<select Title="Autorita Esterna" class="small" id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
	      		<%=tipoAutoritaPolizia%>
	     	</select>
    	</td>
	</tr>
	<tr>
	    <td class="L">Sede</td>
	    <td class="L">
      		<input type="text" maxlength="35" size="35" title="Sede Autorita Esterna" id="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>">
	      	<a href="Javascript:ListaComuni('LoadTrasmissioneAttiEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');">
	        	<img src="/images/filefolder.gif" border="0">
	      	</a>
		</td>
	    <td class="L">Indirizzo</td>
	    <td class="L">
	      	<TEXTAREA title="Note Autorita Esterna" cols="30" id="<%=ICostantiNotifica.CAMPO_NOTE_E%>" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"></textarea>
	    </td>
	</tr>
<%
	}
	if (ccpqcrdad.contains(codPosizioneGiuridica)) { // Istituto di Detenzione
%>
	<tr><td>&nbsp;</td></tr>
	<tr>
      	<td class="l" width="20%">Istituto Detenzione <font class=ob>(*)</font></td>
<%
		if (ldm != null && ldm.getIstitutoDetenzione() != null) {
%>
		<td class="l">
       		<input readonly Title="Istituto" name="Comune" id="descIstituto" value="<%=StringUtils.toStringJSP(ldm.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(ldm.getIstitutoDetenzione().getDescrComune())%>" size="60">
        	<input type="hidden" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" id="<%=ICostantiIstitutoDetenzione.CAMPO_ID_ISTITUTO_DETENZIONE%>" value="<%=ldm.getIstDetIdIstitutoDetenzione()%>">
       		<a href="Javascript:ListaIstitutoDetenzione('LoadTrasmissioneAttiEsecuzione','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>','Comune');">
          		<img src="/images/filefolder.gif" border=0>
          	</a>
		</td>
<%
		} else {
%>
		<td class="l">
          	<input readonly Title="Istituto" name="Comune" id="descIstituto" value="" size="60">
          	<input type="hidden" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" id="<%=ICostantiIstitutoDetenzione.CAMPO_ID_ISTITUTO_DETENZIONE%>" value="">
          	<a href="Javascript:ListaIstitutoDetenzione('LoadTrasmissioneAttiEsecuzione','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>','Comune');">
          		<img src="/images/filefolder.gif" border=0>
          	</a>
		</td>
<%
		}
%>
		<td class="l">Note</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_IST%>" cols="35" readonly="readonly"></textarea>
      	</td>
   </tr>
<%
	}
}
%>
    <tr>
		<td class="lNoBord" colspan="2">
			<br><br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
		</td>
	</tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadTrasmissioneAttiEsecuzione");
// Controlli Data Emissione
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  
// Controlli Data Trasmissione
frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");
frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","gt=1");
frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","lt=31");

frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");
frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","gt=1");
frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","lt=12");

frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2099");
</script>
</body>
</html>
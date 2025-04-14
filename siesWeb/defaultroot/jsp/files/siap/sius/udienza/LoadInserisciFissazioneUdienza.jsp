<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.RedirectTo"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sius.udienza.model.UdienzaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.sius.generaleprocedimento.action.ICostantiGeneraleProcedimento"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<%@ page import="siap.sius.avvocato.model.AvvocatoSiusModel" %>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>

<%@ page import="siap.sius.esperto.action.ICostantiEsperto"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>

<%@ page import="siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento"%>
<%@ page import="siap.sius.curatore.action.ICostantiCuratoreSius"%>
<%@ page import="siap.sige.curatore.action.ICostantiCuratore"%>

<%@ page import="f3b.log.LogF3B"%>


<!-- STUB 12/11/2003 Modifiche per la gestione del dettaglio oggetto (vedi fieldcodesdet). -->

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="magistrato"    		scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="esperto"       		scope="request" class="siap.sius.esperto.model.EspertoModel"/>
<jsp:useBean id="modalita"      		scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"        		scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="tipoAutorita"  		scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="codOggetti"    		scope="request" class="java.lang.String"/>
<jsp:useBean id="descOggetti"   		scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti1"   		scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstitutiSog" 		scope="request" class="java.lang.String"/>
<!--jsp:useBean id="avvocato"     scope="request" class="java.util.ArrayList"/-->
<jsp:useBean id="luogodet"        		scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>
<jsp:useBean id="avvocato"        		scope="request" class="java.util.Vector" />
<jsp:useBean id="fascicoloSiusGP" 		scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="codDettagli"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="UtenteConnesso"        scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="IdUdienzaProcedimento" scope="request" class="java.lang.String"/>
<jsp:useBean id="UdienzaPreFissata"     scope="request" class="siap.sius.udienza.model.UdienzaModel"/>
<jsp:useBean id="fascSospeso"           scope="request" class="java.lang.String"/>
<jsp:useBean id="curatore"              scope="request" class="siap.sius.curatore.model.CuratoreSiusModel"/>
<jsp:useBean id="udienza"               scope="request" class="siap.sius.udienza.model.UdienzaModel"/>

<%
String strTipoDest = "Procuratore Generale";
String lCodOggettoProc = "";
if (UtenteConnesso.getUfficioUtente().getCodTipoUfficio().compareTo("UDS") == 0)
	strTipoDest = "Procuratore della Repubblica";
else if (UtenteConnesso.getUfficioUtente().getCodTipoUfficio().compareTo("TDSM") == 0
		|| UtenteConnesso.getUfficioUtente().getCodTipoUfficio().compareTo("UDSM") == 0)
	strTipoDest="Procuratore della Repubblica Presso Tribunale Minorenni";
String oldIdUdienza = (fascicoloSiusGP.getGeneraleProcedimentoModel() != null
		&& fascicoloSiusGP.getGeneraleProcedimentoModel().getUdiIdUdienza() != null) ? fascicoloSiusGP.getGeneraleProcedimentoModel().getUdiIdUdienza().toString() : "";
if (fascicoloSiusGP.getGeneraleProcedimentoModel() == null)
	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug( "Generale Procedimento null" );
else {
	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug( "ID Generale Procedimento ->" + fascicoloSiusGP.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
	lCodOggettoProc = fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
    if (fascicoloSiusGP.getGeneraleProcedimentoModel().getUdiIdUdienza() == null)
       // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
       siesLogger.debug( "UDI_ID_UDIENZA null" );
    else
       // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
       siesLogger.debug( "UDI_ID_UDIENZA -> "  + fascicoloSiusGP.getGeneraleProcedimentoModel().getUdiIdUdienza().toString());
}
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
siesLogger.debug( "modalita -> " + modalita);
boolean isNotPrefissata = true;
if (UdienzaPreFissata.getIdUdienza() != null)
	isNotPrefissata = false;
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Udienza </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
var desktop;
function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

function ListaUffici(a_formname,a_fieldname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}

function Verify() {
	if (document.LoadInserisciFissazioneUdienza.<%= ICostantiGeneraleProcedimento.CAMPO_ANNOTAZIONE%>.value == "-"
			|| document.LoadInserisciFissazioneUdienza.<%= ICostantiGeneraleProcedimento.CAMPO_ANNOTAZIONE%>.value == "") {
		alert('Inserire un Luogo svolgimento udienza!');
      	return false;
    }

    // Controllo obbligatorietà contenuto.
    var contenuto = document.LoadInserisciFissazioneUdienza.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>[document.LoadInserisciFissazioneUdienza.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.selectedIndex].value;
    if (contenuto == "-") {
		alert("Il Campo Contenuto è obbligatorio");
      	return false;
    }

    // Controlla che sia inserito il destinatario per il soggetto
<%
if (luogodet.getIdLuogoDetenzione() == null) {
%>
	if (document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienza.CAMPO_COD_IST_DETENZIONE%>.value == "-"
			|| document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienza.CAMPO_COD_IST_DETENZIONE%>.value == ""
            || document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienza.CAMPO_COD_LUOGO_DETENZIONE%>.value == "") {
		alert('Scegliere Autorità di Destinazione e Sede per il destinario Soggetto!');
		return false;
	}
<%
}
%>
	if (!ControlloAvvocato())
        return false;
    if (document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length == 1)
		document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value = '0'
			+ document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
    if (document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length == 1)
        document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value = '0'
        	+ document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

    // Controllo validità data Emissione.
    var dataEmissione = document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value + '/' +
		document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value + '/' +
		document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    if (!ControllaData(dataEmissione)) {
      	alert('Data emissione non valida!');
      	return false;
    }

    if (document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>.value.length == 1)
        document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>.value = '0'
        	+ document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>.value;

    if (document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>.value.length == 1)
        document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>.value = '0'
        	+ document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>.value;

    // Controllo validità data Udienza.
    var dataUdienza = document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>.value + '/' +
		document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>.value + '/' +
		document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>.value;

    if (!ControllaData(dataUdienza)) {
      	alert('Data Udienza non valida!');
      	return false;
    }
<%
// Controllo solo se non esiste prefissata
if (isNotPrefissata) {
%>
	// Controllo cambiamento Udienza
    var IdUdienzaOld = "<%=oldIdUdienza%>";
    var IdUdienza = document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienza.CAMPO_ID_UDIENZA%>.value;
    if (IdUdienzaOld == IdUdienza) {
		if (!confirm("Si sta rifissando la stessa udienza! Si vuole continuare ?"))
      		return false;
    }
<%
}
%>
	return true;
}

// Verifica che esista almeno un avvocato associato al fascicolo
function ControlloAvvocato() {
<%
int numAvvocati = avvocato.size();
if (numAvvocati == 0) {
%>
	alert('Il Difensore è obbligatorio!');
	return false;
<%
} else {
%>
	return true;
<%
}
%>
}

// Controllo sospensione fascicolo
function ControlloSospensione() {
<%
if (fascSospeso.compareTo("SI") == 0) {
%>
	if (!confirm("Attenzione: per questo procedimento è presente un'ordinanza di rimessione atti. Procedere con l'emissione di un nuovo provvedimento ?")) {
   		//str = "/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActCancellaProvvedimento&" +a_parameter +"=" + a_entityname;
    	str = "/jsp/Main.jsp?Action=siap.sius.udienza.action.ActLoadFSPFissazioneUdienza&";
        window.location.href = str;
        return false
	} else
   		return true
<%
}
%>
  	return true;
}

function ControlloSospensioneEdAvvocato() {
	if (ControlloSospensione())
 		return ControlloAvvocato();
 	else
  		return false;
}  

// Chiamata funzione lista Oggetti
function ListaOggetti(a_formname,a_field_contenuto, a_fieldname, a_fieldcodes, a_fieldcodesdet, i_fieldcodes, i_fieldcodesdet) {
	// Compone il link URL per passare i parametri alla ElencoUdienza.JSP
  	var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaOggetti";
	aLink += "&formname=" + a_formname;
	aLink += "&field_contenuto=" + a_field_contenuto;
	aLink += "&fieldname=" + a_fieldname;
	aLink += "&fieldcodes=" + a_fieldcodes;
	aLink += "&fieldcodesdet=" + a_fieldcodesdet;
	aLink += "&ifieldcodes=" + i_fieldcodes;
	aLink += "&ifieldcodesdet=" + i_fieldcodesdet;
	desktop = window.open(aLink, "Lista_Oggetti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
}

// Chiamata funzione elenco Udienze.
function ListaUdienze(aNomeForm, aNomeCampoGG, aNomeCampoMM, aNomeCampoAA, aNomeCampoLuogo, aNomeCampoIdUdienza, aNomeCampoCollegio) {
  	// Compone il link URL per passare i parametri alla ElencoUdienza.JSP
 	var lLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.udienza.action.ActLoadRicercaUdienzaXProcedimenti";
	lLink += "&formname=" + aNomeForm;
	lLink += "&campoGG=" + aNomeCampoGG;
	lLink += "&campoMM=" + aNomeCampoMM;
	lLink += "&campoAA=" + aNomeCampoAA;
	lLink += "&campoLuogo=" + aNomeCampoLuogo;
	lLink += "&campoID=" + aNomeCampoIdUdienza;
	lLink += "&campoColl=" + aNomeCampoCollegio;
  	desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=800,height=550" );
}

function EListaUdienze(aNomeForm, aNomeCampoGG, aNomeCampoMM, aNomeCampoAA, aNomeCampoLuogo, aNomeCampoIdUdienza, aNomeCampoCollegio) {
	var lLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.udienza.action.ActElencoUdienzePerData";
	lLink += "&formname="+ aNomeForm;
	lLink += "&campoGG=" + aNomeCampoGG;
	lLink += "&campoMM=" + aNomeCampoMM;
	lLink += "&campoAA=" + aNomeCampoAA;
	lLink += "&campoLuogo=" + aNomeCampoLuogo;
	lLink += "&campoID=" + aNomeCampoIdUdienza;
	lLink += "&campoColl=" + aNomeCampoCollegio;
	desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

// Chiamata funzione lista dei comuni.
function ListaComuni(a_formname,a_fieldname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}
</script>
</head>

<body class="corpo" onload="Javascript:return ControlloSospensioneEdAvvocato();">
<table >
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
		<td class="LBG">
      		<font class="label"> Funzione :</font>&nbsp;
<%
String lAzione = new String();
UdienzaModel lUdienza = new UdienzaModel();
if (UdienzaPreFissata.getIdUdienza() != null) {
	lUdienza = UdienzaPreFissata;
	// vengono caricati i dati dell'udinza inserita con il link "Inserimento Udienza"
	// presente nella pagina di "Inserimento Fissazione Udienza"
	// così da preimpostare i campi quando si torna nella pagina 
	} else if (udienza.getIdUdienza() != null) {
		lUdienza = udienza;
}
lAzione = "siap.sius.udienza.action.ActInserisciFissazioneUdienza";
%>
        	<font class="campo">Inserimento Fissazione Udienza</font>
		</td>
	</tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
</table>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciFissazioneUdienza">
<table cellspacing=2 cellpadding=2 width="100%">
	<tr>
     	<td class="Titolo" colspan=3 > Magistrato Relatore </td>
   	</tr>
	<tr>
		<td class="L" colspan=3>
       		<%=StringUtils.toStringJSP(magistrato.getCognome())%>&nbsp;
			<%=StringUtils.toStringJSP(magistrato.getNome())%>&nbsp;
			<%=StringUtils.toStringJSP(esperto.getCognome())%>&nbsp;
			<%=StringUtils.toStringJSP(esperto.getNome())%>
		</td>
	</tr>
   	<tr>
     	<td class="L" colspan=3>
       		<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.magistratorelatore.action.ActLoadInserisciMagistratoRelatore&acdest=siap.sius.udienza.action.ActLoadInserisciFissazioneUdienza">
      			Assegnazione/Cambio Magistrato Relatore
    		</a>
  		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
</table>

<jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>">
	<jsp:param name="AvvRitorno" value="pippo"/>
</jsp:include>

<table cellspacing=2 cellpadding=2 width="100%">
	<tr>
  		<td class="Titolo" colspan=6>Dati Fissazione Udienza</td>
	</tr>
	<tr>
  		<td class="l">Data Emissione <font class="ob">(*)</font></td>
  		<td class="L">
   			<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
			<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
			<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
  		</td>
	</tr>

<!-- Sezione Contenuto Oggetti -->
	<tr>
		<td class="l">Contenuto </td>
		<td class="L">
    		<select title="contenuto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>">
				<%=contenuto%>
    		</select>
	</tr>
	<tr>
  		<td class="l">Oggetto<font class="ob">(*)</font></td>
    	<td class="l">
      		<Textarea Title="Oggetto" name="<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>" cols=95 rows=3 readonly><%=descOggetti%></Textarea>
			<a href="Javascript:ListaOggetti('LoadInserisciFissazioneUdienza',document.LoadInserisciFissazioneUdienza.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>[document.LoadInserisciFissazioneUdienza.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.selectedIndex].value, '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciFissazioneUdienza.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciFissazioneUdienza.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
				<img src="/images/fileselected.gif" title="Oggetti per il Contenuto selezionato" border=0>
			</a>
			&nbsp;
			<a href="Javascript:ListaOggetti('LoadInserisciFissazioneUdienza','-', '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciFissazioneUdienza.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciFissazioneUdienza.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
      			<img src="/images/filefolder.gif" title="Elenco di tutti gli Oggetti Selezionabili" border=0>
      		</a>
  		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
  		<td class="Titolo" colspan=6>Udienza</td>
	</tr>
	<tr>
  		<td class="l">Data <font class="ob">(*)</font></td>
  		<td class="l">
    		<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lUdienza.getDataUdienza(),"dd")) %>" type="text" name="<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA %>" maxlength="2" size="2" readonly>
			/
			<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lUdienza.getDataUdienza(),"MM")) %>" type="text" name="<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA %>" maxlength="2" size="2" readonly>
			/
			<input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lUdienza.getDataUdienza(),"yyyy")) %>" type="text" name="<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>" maxlength="4" size="4" readonly>
			&nbsp;Num. Coll. <font class="ob">(*)</font>
			<input value="<%=( (lUdienza.getNumCollegio() != null) ? StringUtils.toStringJSP( lUdienza.getNumCollegio()) : "") %>" name="Collegio" maxlength="2" size="2" readonly>
			&nbsp;
			<a href="Javascript:ListaUdienze('LoadInserisciFissazioneUdienza',
					'<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA %>',
					'<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA %>',
					'<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA %>',
					'<%=ICostantiGeneraleProcedimento.CAMPO_ANNOTAZIONE%>',
					'<%=ICostantiUdienza.CAMPO_ID_UDIENZA%>',
					'Collegio');">
				Lista udienze
				<img src="/images/filefolder.gif" border=0>
			</a>
<%
RedirectTo lRedir = new RedirectTo();
lRedir.setPage(IWebConstants.PG_MAIN);
// Link 20 indica Action Chiamante
lRedir.setParameter("TornaQui", "20");
// MEV10-s3: aggiunta or condition per gestire uffici minorenni
if ("UDS".equals(UtenteConnesso.getUfficioUtente().getCodTipoUfficio())
		|| "UDSM".equals(UtenteConnesso.getUfficioUtente().getCodTipoUfficio())) {
 	lRedir.setAction("siap.sius.udienza.action.ActLoadInserisciUdienzaUDS&CheckInsFissUdienza=S");
} else {
	lRedir.setAction("siap.sius.udienza.action.ActLoadInserisciUdienza&CheckInsFissUdienza=S");
}
%>
      		&nbsp;<a class="cliccabile" href="<%=lRedir%>">Inserimento Udienza</a>
		</td>
	</tr>
	<tr>
  		<td class="l">Luogo svolgimento<font class="ob">(*)</font></td>
  		<td class="l">  <input type="text" name="<%=ICostantiGeneraleProcedimento.CAMPO_ANNOTAZIONE%>"  value= "<%=StringUtils.toStringJSP( lUdienza.getLuogoUdienza() , "")%>" size="95"></td>
	</tr>
	<tr>
  		<td class="l">Note</td>
  		<td class="L">
    		<TEXTAREA title="Note" name="<%= ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO %>"  cols=95 rows=2 ></textarea>
 		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
  		<td class="Titolo" colspan=6>Destinatari</td>
	</tr>
	<tr>
  		<td class="l" colspan=6>
    		Per la comunicazione al&nbsp;<%=strTipoDest%>&nbsp;<input type="checkbox" checked name="<%= ICostantiUdienza.CAMPO_PROCURA_GENERALE %>">
      	</td>
    </tr>
	<tr><td colspan=6>&nbsp;</td></tr>
    <tr>
      	<td class="l" colspan=6>Per la notifica al Soggetto</td>
    </tr>
<%
if (luogodet.getIdLuogoDetenzione() == null
		|| luogodet.getDataFineDetenzione() != null
		|| luogodet.getIstitutoDetenzione() == null) {
%>
	<tr>
	 	<td class="l">Autorità Destinazione <font class="ob">(*)</font></td>
	 	<td class="l">
	   		<select title="Destinatario" name="<%=ICostantiUdienza.CAMPO_COD_IST_DETENZIONE%>">
				<%= tipoAutorita %>
	   		</select>
	 	</td>
	</tr>
	<tr>
	 	<td class="l">Sede <font class="ob">(*)</font></td>
	 	<td class="l">
	    	<input Title="Sede " name="<%=ICostantiUdienza.CAMPO_COD_LUOGO_DETENZIONE%>"
				value="" type="text" maxlength="35" size="35">
			<a href="Javascript:ListaComuni('LoadInserisciFissazioneUdienza','<%=ICostantiUdienza.CAMPO_COD_LUOGO_DETENZIONE%>');">
	       		<img src="/images/filefolder.gif" border=0>
	        </a>
	 	</td>
	</tr>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="L" colspan=3>
			<input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
	  	</td>
	</tr>
<%
} else {
%>
	<tr>
	  	<td class="l">Tipo Istituto</td>
	  	<td class="l">
	  		<input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(luogodet.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(luogodet.getIstitutoDetenzione().getDescrComune())%>" size=50>
			<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=luogodet.getIstDetIdIstitutoDetenzione()%>" size=50>
			<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciFissazioneUdienza','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
				<img src="/images/filefolder.gif" border=0>
			</a>
			<input type="hidden" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" >
		</td>
	</tr>
<%
}
%>
	<tr><td colspan=6>&nbsp;</td></tr>
<%
Iterator<?> itxAvv = avvocato.iterator();
int num_sede = 0;
while (itxAvv.hasNext()) {
	AvvocatoSiusModel lAvv = (AvvocatoSiusModel)itxAvv.next();
%>
	<tr style="width: 100%;" >
		<td class=l colspan=6>Per la notifica all'avvocato&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getAvvocato().getNome(),"-")%>&nbsp;Foro&nbsp;di&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo(),"-")%>&nbsp;Difensore&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo(),"-")%></td>
	</tr>
    <tr>
      	<td class="l">Autorità Destinazione</td>
      	<td class="l">
        	<select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
          		<%= TipiIstituti1 %>
        	</select>
      	</td>
	</tr>
    <tr>
      	<td class="l">Sede</td>
      	<td class="l">
      	<%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
			<input Title="Sede Procura" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
					value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo(),"-")%>" type="text" maxlength="35" size="35">
			<a href="Javascript:ListaUffici('LoadInserisciFissazioneUdienza','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=num_sede%>]');">
           		<img src="/images/filefolder.gif" border=0>
       		</a>
      	</td>
	</tr>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="L" colspan=3>
			<input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80"/>
			<input name="<%=ICostantiUdienza.CAMPO_COD_AVVOCATO%>" value="<%=lAvv.getAvvocatoFascicoloSiusModel().getIdAvvocatoFascicoloSius()%>" type="hidden" >
		</td>
	</tr>
<!--
	<tr>
  		<td class="l">Notifica Via Fax: </td>
  		<td class="L" colspan=3>
   			<input title="Notifica Via Fax" name="<//%=ICostantiRichiestaAtti.CAMPO_NOTIFICA_VIA_FAX %>" value="1" type="checkbox"  />
  		</td>
	</tr>
-->
<%
	num_sede++;
}
%>
<tr><td colspan=6>&nbsp;</td></tr>
<%
if (curatore != null && curatore.getCuratore() != null && curatore.getCurIdCuratore() != null) {
%>
	<tr style="width: 100%;" >
		<td class=l colspan=6>Per la notifica al&nbsp;<%=curatore.getDescrTipo()%>&nbsp;<%=curatore.getCuratore().getCognome()%>&nbsp;<%=curatore.getCuratore().getNome()%></td>
    </tr>
    <tr>
      	<td class="l">Autorità Destinazione</td>
      	<td class="l">
        	<select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
          		<%= TipiIstituti1 %>
        	</select>
      	</td>
	</tr>
    <tr>
      	<td class="l">Sede</td>
      	<td class="l">
			<input Title="Sede Procura" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>" type="text" maxlength="35" size="35"
				value="<%=StringUtils.toStringJSP(curatore.getCuratore().getDescrUfficioAppartenenza(),"-")%>">
			<a href="Javascript:ListaUffici('LoadInserisciFissazioneUdienza','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=num_sede%>]');">
				<img src="/images/filefolder.gif" border=0>
			</a>
      	</td>
	</tr>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="L" colspan=3>
		 	<input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
		 	<input name="<%=ICostantiUdienza.CAMPO_COD_AVVOCATO%>" value="C" type="hidden" >
    		<input name="<%=ICostantiCuratore.CAMPO_ID_CURATORE%>" value="<%=curatore.getCuratore().getIdCuratore()%>" type="hidden" >
	  	</td>
	</tr>
<%
	num_sede++;
}
%>
	<tr><td colspan=6>&nbsp;</td></tr>
    <tr style="width: 100%;" >
      	<td class=l colspan=6>Per la  <input type='radio' name="<%=ICostantiUdienza.CAMPO_TIPONOTIFICA%>" value='N' >Notifica /<input type='radio' name="<%=ICostantiUdienza.CAMPO_TIPONOTIFICA%>" value='C' checked>Comunicazione ad altro destinatario</td>
    </tr>
    <tr>
      	<td class="l">Destinatario</td>
      	<td class="l">
        	<select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
          		<%= tipoAutorita %>
        	</select>
      	</td>
    </tr>
    <tr>
      	<td class="l">Sede </td>
      	<td class="l">
			<input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
            	value="" type="text" maxlength="35" size="35">
            <a href="Javascript:ListaComuni('LoadInserisciFissazioneUdienza','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=num_sede%>]');">
            	<img src="/images/filefolder.gif" border=0>
            </a>
      	</td>
    <tr>
	<tr>
	  	<td class="l">Indirizzo</td>
	  	<td class="L" colspan=3>
	   		<input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
	   		<input name="<%=ICostantiUdienza.CAMPO_COD_AVVOCATO%>" value="" type="hidden">
		</td>
	</tr>
	<tr><td colspan=6>&nbsp;</td></tr>
    <tr>
      	<td>
        	<input class="bottone" type="submit" value="Conferma">
      	</td>
    </tr>
</table>

<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
<input type="HIDDEN" value="<%=StringUtils.toStringJSP( lUdienza.getIdUdienza(), "" )%>" name="<%= ICostantiUdienza.CAMPO_ID_UDIENZA %>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>" value="<%=codOggetti%>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=codDettagli%>">
<input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" value="">
<%
// Passaggio dell'eventuale ID UDIENZA_PROCEDIMENTO
if (request.getAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO) != null) {
%>
<input type="HIDDEN" value="<%=StringUtils.toStringJSP( request.getAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO), "0" )%>" name="<%=ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO%>"  >
<%
}
%>
</form>

<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadInserisciFissazioneUdienza");
// Controllo data emissione.
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req", "Il campo Giorno Data Emissione è obbligatorio");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req", "Il campo Mese Data Emissione é obbligatorio");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req", "Il campo Anno Data Emissione é obbligatorio");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

// Controllo campo oggetto.
frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO%>", "req","E' necessario selezionare almeno un oggetto");

// Controllo data udienza.
frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>","req", "Il campo Giorno Data Udienza è obbligatorio");
frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>","numeric");
frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>","req", "Il campo Mese Data Udienza é obbligatorio");
frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>","numeric");
frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>","req", "Il campo Anno Data Udienza é obbligatorio");
frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>","numeric");
frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

//Chiama la funzione di Verify().
frmvalidator.setAddnlValidationFunction("Verify");
</script>

</body>
</html>
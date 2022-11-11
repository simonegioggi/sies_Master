<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>

<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneReatoModel"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.reato.model.ReatoModel"%>

<jsp:useBean id="evento"               scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="posizioneluogoaltra"  scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"          scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="codiceAutoritaE"      scope="request" class="java.lang.String"/>

<jsp:useBean id="annotazioneordinanza"  scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaModel"/>
<jsp:useBean id="listaannotazionireati" scope="request" class="java.util.Vector"/>
<jsp:useBean id="codmotivo"             scope="request" class="java.lang.String"/>
<jsp:useBean id="daticssa"              scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="UffUDS"                scope="request" class="java.lang.String"/>

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

boolean lOrdGEPresente = false;
if (annotazioneordinanza != null && annotazioneordinanza.getEvento() != null
		&& annotazioneordinanza.getAnnotazioneManuale() != null) {
	lOrdGEPresente = true;
}

BigDecimal lIdEveProvv = null;
if (listaannotazionireati != null && !listaannotazionireati.isEmpty()) {
	AnnotazioneReatoModel lAnnReaModel = (AnnotazioneReatoModel)listaannotazionireati.firstElement();
 	lIdEveProvv = lAnnReaModel.getAnnotazioneManuale().getEveIdEvento();
}
%>
<html>
<head>
<title>[S.I.E.S.] - Ordine Scarcerazione per nuova scadenza pena </title>
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

function pulisciCodMagistrato() {
	document.LoadInserisciOrdineScarcerazione.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value = '';
}

function Verify() {
	// DATA EMISSIONE
   	if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length == 1)
		document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
	if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length == 1)
		document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

	var data_emissione = document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

  	if (!ControllaData(data_emissione)) {
	    alert('Data di emissione non valida');
	    document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
	    return false;
	}

  	// DATA TRASMISSIONE
  	if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length == 1)
		document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
	if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length == 1)
		document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

	var data_trasmissione = document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

  	if (!ControllaData(data_trasmissione)) {
	    alert('Data di trasmissione non valida');
	    return false;
	}

  	if (document.LoadInserisciOrdineScarcerazione.<%= ICostantiMagistrato.CAMPO_COGNOME %>.value == ""
  			&& document.LoadInserisciOrdineScarcerazione.<%=ICostantiMagistrato.CAMPO_NOME %>.value == "") {
		alert("Il Magistrato competente è obbligatorio");
		document.LoadInserisciOrdineScarcerazione.<%= ICostantiMagistrato.CAMPO_COGNOME%>.focus();
    	return false;
  	}
}

function ListaCSSA(a_formname,a_fieldname,a_field2) {
  	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

// magistrato competente
function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
}

function ListaComuniTds(formname,fieldname) {
  	desktop = window.open("<%= IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function ListaUDS(a_formname,a_fieldname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=370, height=500");
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
        	<font class="campo">Ordine di scarcerazione per nuova scadenza pena</font>
      	</td>
        <td class="LBG">
          	<a href="Javascript:history.go(-1);">
            	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          	</a>
        </td>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciOrdineScarcerazione">
<INPUT type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActInserisciOrdineScarcerazionePerNuovaScadenzaPena">
<INPUT type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="<%=codmotivo%>">
<INPUT type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_EVE_ID_EVENTO%>" value="<%=lIdEveProvv%>">
<table>
	<tr>
      	<td class="l">Posizione Giuridica </td>
      	<td class="L" colspan=5>
        	<font class="campo">
<%
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
%>
            DETENUTO PER ALTRA CAUSA
<%
} else {
%>
            <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
}
%>
         	</font>
		</td>
	</tr>
<%
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
	if (lAltraCausa.getIstitutoDetenzione() != null) {
%>
	<tr>
		<td class="l">Detenuto presso </td>
		<td class="L" colspan=5>
			<font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            &nbsp;di&nbsp;<font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
        </td>
	</tr>
<%
		if (lAltraCausa.getAltroLuogo() != null) {
%>
	<tr>
		<td class="l">Altro Luogo </td >
		<td class="L" colspan=5>
	  		<font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>
	  	</td>
	</tr>
<%
		}
	}
} else if (lLuogoDetenzione.getIstitutoDetenzione() != null) {
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
if (lPosizione.getCodPosizioneGiuridica() != null
		&& (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04"))) {
	if (lLuogoDetenzione.getIstitutoDetenzione() != null) {
%>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="L" colspan=5>
	  		<font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>
	  	</td>
	</tr>
<%
	}
}
%>
   	<tr>
<%
if (penaresidua.getIdPenaResidua() != null
		&& ((penaresidua.getFlagErgastolo() == null)
				|| (penaresidua.getFlagErgastolo() != null
				&& !penaresidua.getFlagErgastolo().equals("S")
				&& !penaresidua.getFlagErgastolo().equals("D")))) {
	if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0)) {
		
	} else {
%>
		<td class="l">Reclusione</td>
		<td class="l" colspan=2>
		  	<font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
		</td>
		<td class="l">Multa</td>
		<td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
	}
%>
	</tr>
   	<tr>
<%
	if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0)) {
		
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
	</tr>
<%
if ((!lPosizione.getCodPosizioneGiuridica().equals("07") && !lPosizione.getCodPosizioneGiuridica().equals("10"))) {
%>
	<tr>
<%
	if (penaresidua.getDataInizio() != null) {
%>
		<td class="l">Data Decorrenza Pena</td>
		<td class="L">
		  	<font class="campo">
		    	<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>
		  	</font>
		</td>
<%
	}
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
	<td class="l">Data Fine Pena Automatica</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"))%> &nbsp;</font></td>
--%>
<%
	if (penaresidua.getFlagErgastolo() != null) {
		if (penaresidua.getFlagErgastolo().equals("S")) {
%>
		<td class="l">Pena Detentiva</td>
		<td class="L"><font class="campo">ERGASTOLO</font></td>
<%
		} else if (penaresidua.getFlagErgastolo().equals("D")) {
%>
		<td class="l">Pena Detentiva</td>
		<td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font></td>
<%
        }
	}
	if (((penaresidua.getFlagErgastolo() == null)
			|| (penaresidua.getFlagErgastolo() != null
			&& !penaresidua.getFlagErgastolo().equals("S")
			&& !penaresidua.getFlagErgastolo().equals("D")))
			&& penaresidua.getDataFine() != null) {
		String lClassTd="l";
		String lClassFont="campo";
		if (penaresidua.getDataFine() != null
				&& !penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
			lClassTd = "lRosso";
           	lClassFont = "lRosso";
		}
%>
		<td class="l">Data Fine Pena</td>
		<td class="<%=lClassTd%>">
			<font class="<%=lClassFont%>">
				<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd-MM-yyyy"))%>
			</font>
		</td>
<%
	}
%>
	</tr>
<%
}
%>
	<tr>
		<td class="l">Data Emissione</td>
        <td class="L">
        	<input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
          	<input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          	<input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      		<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
        <td class="l">Data Trasmissione</td>
        <td class="L">
          	<input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          	<input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          	<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
</table>
<%
//==============================================================================
//                    Sezione con i dati dell'Ordinanza
//==============================================================================
%>
<table style="width: 95%;">
<%
if (lOrdGEPresente) {
%>
	<tr>
      	<td class="Titolo" colspan=4> Ordinanza GE </td>
    </tr>
    <tr>
		<td class="l">
        	<font class="label">Anno / Numero</font>
      	</td>
      	<td class="l">
        	<font class="campo">
          		<%=StringUtils.toStringJSP(annotazioneordinanza.getAnnotazioneManuale().getAnnoGe() )%>/<%=StringUtils.toStringJSP(annotazioneordinanza.getAnnotazioneManuale().getNumeroGe())%>
        	</font>
      	</td>
		<td class="l">
			<font class="label">in data </font>
		</td>
      	<td class="l">
			<font class="campo">
          		<%=StringUtils.toStringJSP(DateUtils.getDateToString(annotazioneordinanza.getAnnotazioneManuale().getDataGE(), "dd-MM-yyyy"))%>&nbsp;
        	</font>
      	</td>
    </tr>
    <tr>
		<td class="l">Ufficio</td>
      	<td class="l">
        	<font class="campo">
          		<%=StringUtils.toStringJSP(annotazioneordinanza.getEvento().getDescrUfficioEmittente())%>
        	</font>
      	</td>
      	<td class="l">Sede</td>
      	<td class="l">
        	<font class="campo">
          		<%=StringUtils.toStringJSP(annotazioneordinanza.getEvento().getDescrLuogoEmittente())%>
        	</font>
      	</td>
	</tr>
	<tr>
      	<td class="l">Motivazioni</td>
      	<td class="l" colspan=3>
        	<font class="campo">
          		<%=StringUtils.toStringJSP(annotazioneordinanza.getAnnotazioneManuale().getMotivazioni())%>
        	</font>
      	</td>
	</tr>
<%
}
%>
</table>
<%
//==============================================================================
//                                  Magistrato
//==============================================================================
%>
<table style="width: 95%;">
	<tr>
      	<td class="Titolo" colspan=6> Magistrato </td>
    </tr>
    <tr>
      	<td class="l">Magistrato Competente
      	<td class="L">
        	<input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
        	<input title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25" onChange="pulisciCodMagistrato()">
        	<input title= "Nome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25" onChange="pulisciCodMagistrato()">
        	<a href="Javascript:ListaMagistrati('LoadInserisciOrdineScarcerazione','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
          		<img src="/images/filefolder.gif" border=0>
        	</a>
		</td>
	</tr>
</table>
<%
//==============================================================================
//                       Sezione con Aumenti e Riduzione
//==============================================================================
%>
<table style="width: 95%;">
<%
if (listaannotazionireati != null && !listaannotazionireati.isEmpty()) {
%>
	<tr>
		<td class="Titolo" colspan=6>Aumenti e Riduzioni</td>
    </tr>
<%
}
Iterator lIter = listaannotazionireati.iterator();
AnnotazioneReatoModel lAnnRea = null;
AnnotazioneManualeModel lAnn = null;
ReatoModel lReato = null;
while (lIter.hasNext()) {
	lAnnRea = (AnnotazioneReatoModel)lIter.next();
	lAnn = lAnnRea.getAnnotazioneManuale();
	lAnn.calcolaStringaReclusione();
	lAnn.calcolaStringaArresto();
  	lReato = lAnnRea.getReato();
%>
	<tr>
		<td class="l">
			<li>
            	<font class="campo">
<%
	if (lAnn.getFlagPiuMeno() != null) {
		if (lAnn.getFlagPiuMeno().equals("-")) { // CONCEDE
%>
				<font class="label">Concede:</font>
<%
		}
		if (lAnn.getFlagPiuMeno().equals("+")) { // REVOCA
%>
				<font class="label">Revoca:</font>
<%
		}
	}
	if (lAnn.getStringaReclusione() != null) {
%>
				<%=StringUtils.toStringJSP(lAnn.getStringaReclusione())%>
                <font class="label">&nbsp;di reclusione&nbsp;</font>
<%
	}
	if (lAnn.getImportoMulta() != null && lAnn.getImportoMulta().intValue() != 0) {
%>
                <font class="label">Multa&nbsp;</font><%=StringUtils.toEuroFormat(lAnn.getImportoMulta())%>&nbsp;€&nbsp;
<%
	}
	if (lAnn.getStringaArresto() != null) {
%>
                <%=StringUtils.toStringJSP(lAnn.getStringaArresto())%>
                <font class="label">&nbsp;di arresto&nbsp;</font>
<%
	}
	if (lAnn.getImportoAmmenda() != null && lAnn.getImportoAmmenda().intValue() != 0) {
%>
				<font class="label">Ammenda&nbsp;</font><%=StringUtils.toEuroFormat(lAnn.getImportoAmmenda())%>&nbsp;€&nbsp;
<%
	}
%>
				</font>
		     	&nbsp;
		      	<font class="label">
		        	RIFERITO A :
		      	</font>
		      	&nbsp;
<%
	if (lReato != null && lReato.getIdReato() != null) {
		boolean lFlagAnnoNumero = false;
        if (lReato.getAnnoFonte() != null
        		&& !"".equals(lReato.getAnnoFonte().toString())
            	&& lReato.getNumeroFonte() != null
            	&& !lReato.getNumeroFonte().equals("")) {
			lFlagAnnoNumero = true;
		}
%>
				<font class="campo">
<%
		// REATO
		if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals("")) {
%>
				<font class="campoNoCap">
<%
					out.println("Reato N." + lReato.getProgrNumeroManuale()+": ");
%>
                </font>
<%
		} else {
                	out.println("Reato N." + lReato.getProgrReato()+": ");
		}
		if (lFlagAnnoNumero) {
		  	if (lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
		    	out.println(lReato.getDescrFonte()+" ");
		  	if (lReato.getAnnoFonte() != null && !"".equals(lReato.getAnnoFonte().toString()))
		    	out.println(lReato.getAnnoFonte());
		  	if (lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
		    	out.println("/"+lReato.getNumeroFonte());
		}
		if (lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
			out.println("art."+lReato.getArticolo());
		if (lReato.getDescrSottonumerazione() != null
				&& !lReato.getDescrSottonumerazione().equals("")
				&& !lReato.getDescrSottonumerazione().equals("-"))
			out.println(" "+lReato.getDescrSottonumerazione());
		if (!lFlagAnnoNumero) {
			if (lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
		    	out.println(lReato.getDescrFonte());
		}
		if (lReato.getComma() != null && !lReato.getComma().equals(""))
			out.println(" c. "+lReato.getComma());
		if (lReato.getLettera() != null && !lReato.getLettera().equals(""))
		  	out.println(" l. "+lReato.getLettera());
		if (lReato.getNumero() != null && !lReato.getNumero().equals(""))
		  	out.println(" n. "+lReato.getNumero());
%>
				</font>
<%
	} else {
%>
		        <font class="campo">
		          	PENA COMPLESSIVA
		        </font>
<%
	}
	// IN CONFORMITA'
	if (lAnn.getFlagConforme() != null && lAnn.getFlagConforme().equals("C")) {
%>
       			<font class="campo">
          			<font class="label"> -- </font>IN CONFORMITA'
        		</font>
<%
	// IN DIFFORMITA'
	} else if (lAnn.getFlagConforme() != null && lAnn.getFlagConforme().equals("D")) {
%>
        		<font class="campo">
          			<font class="label"> -- </font>IN DIFFORMITA'
        		</font>
<%
	}
%>
				&nbsp;
<%
	if (lAnn.getCodDpr() != null   // AMNISTIA / INDULTO
			&& !lAnn.getCodDpr().equals("")
			&& !lAnn.getCodDpr().equals("-")) {
%>
        		<font class="label">DPR </font>
        		<font class="campo">
          			<%=StringUtils.toStringJSP(lAnn.getDescrDpr())%>
        		</font>
<%
	} else if(lAnn.getCodTipoAnnotazione() != null
			&& (lAnn.getCodTipoAnnotazione().equals("004") // DEPENALIZZAZIONE
			// MEV 37 - Inizio	
			|| 	lAnn.getCodTipoAnnotazione().equals("017"))) { // ILLECITO AMMINISTRATIVO
			// MEV 37 - Fine
%>
        		<font class="label">FONTE NORMATIVA CHE HA DISPOSTO LA DEPENALIZZAZIONE: </font>
        		<br>&nbsp;&nbsp;&nbsp;&nbsp;
<%
		boolean lFlagAnnoNumero = false;
        if (lAnn.getAnnoFonte() != null
				&& !"".equals(lAnn.getAnnoFonte().toString())
				&& lAnn.getNumeroFonte() != null
				&& !lAnn.getNumeroFonte().equals("")) {
			lFlagAnnoNumero = true;
        }
%>
           		<font class="campo">
<%
		// REATO ANNOTAZIONE MANUALE
		if (lFlagAnnoNumero) {
  			if (lAnn.getDescrFonte() != null && !lAnn.getDescrFonte().equals("") && !lAnn.getDescrFonte().equals("-"))
    			out.println(lAnn.getDescrFonte()+" ");
  			if (lAnn.getAnnoFonte() != null && !"".equals(lAnn.getAnnoFonte().toString()))
    			out.println(lAnn.getAnnoFonte());
  			if (lAnn.getNumeroFonte() != null && !lAnn.getNumeroFonte().equals(""))
    			out.println("/"+lAnn.getNumeroFonte());
		}
		if (lAnn.getArticolo() != null && !lAnn.getArticolo().equals(""))
  			out.println("art."+lAnn.getArticolo());
		if (lAnn.getDescrSottonumerazione() != null && !lAnn.getDescrSottonumerazione().equals("") && !lAnn.getDescrSottonumerazione().equals("-"))
  			out.println(" "+lAnn.getDescrSottonumerazione());
		if (!lFlagAnnoNumero) {
  			if (lAnn.getDescrFonte() != null && !lAnn.getDescrFonte().equals("") && !lAnn.getDescrFonte().equals("-"))
    			out.println(lAnn.getDescrFonte());
		}
		if (lAnn.getComma() != null && !lAnn.getComma().equals(""))
  			out.println(" c. "+lAnn.getComma());
		if (lAnn.getLettera() != null && !lAnn.getLettera().equals(""))
  			out.println(" l. "+lAnn.getLettera());
		if (lAnn.getNumero() != null && !lAnn.getNumero().equals(""))
  			out.println(" n. "+lAnn.getNumero());
%>
          		</font>
<%
   	} else if(lAnn.getCodTipoAnnotazione() != null
			&& lAnn.getCodTipoAnnotazione().equals("013")) { // INCOSTITUZIONALITA'
%>
        		<font class="label">
          			DICHIARAZIONE DI ILLEGITTIMITA' COSTITUZIONALE:&nbsp;&nbsp;
          			Anno / Numero&nbsp;
		        </font>
		        <font class="campo">
		          	<%=StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(lAnn.getAnnoCc()),"")%>/<%=StringUtils.toStringJSP(lAnn.getNumeroCc())%>
		        </font>
		        <font class="label">&nbsp;in data</font>
		        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnn.getDataCC(),"dd-MM-yyyy"))%>&nbsp;</font>
<%
	}
%>
			</li>
		</td>
	</tr>
<%
}
%>
</table>
<%
//==============================================================================
//                       Sezione con i destinatari
//==============================================================================
%>
<table style="width: 95%;">
<%
//*** IN MISURA ALTERNATIVA ***
if (lPosizione.isMisAlt()) {
%>
	<tr>
      	<td class="Titolo" colspan=6> Destinatario per l'esecuzione </td>
    </tr>
    <tr>
		<!--autorità di polizia-->
      	<td class="l">Autorità Destinazione</td>
      	<td class="L">
        	<input type="hidden" name="autoritaE" value="S">
        	<select  Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
          		<%=codiceAutoritaE%>
        	</select>
        	<input type="hidden" name="notificaPolizia" value="E">
      	</td>
      	<td rowspan=2 class="l">Note</td>
      	<td rowspan=2 class="L">
      		<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols=20 rows=5></textarea>
      	</td>
    </tr>
	<tr>
      	<td class="l">Sede<font class=ob>(*)</font></td>
      	<td class="L">
        	<input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
        	<a href="Javascript:ListaComuni('LoadInserisciOrdineScarcerazione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
          		<img src="/images/filefolder.gif" border=0>
        	</a>
      	</td>
    </tr>
    <tr>
      	<td class="Titolo" colspan=6> Notifica UEPE </td>
    </tr>
	<!--UEPE competente-->
    <tr>
      	<td class="l">UEPE Competente</td>
      	<td class="l">
	        <input type="hidden" name="cssa" value="S">
	        <input type="hidden" name="notifica" value="C">
	        <input readonly Title="UEPE Competente" name="Indirizzo" value="<%=StringUtils.toStringJSP(daticssa.getComune())%>-<%=StringUtils.toStringJSP(daticssa.getIndirizzo())%>" size=60 >
	        <input type="hidden" Title="UEPE Competente" name="<%=ICostantiCSSA.CAMPO_ID_CSSA %>" value="<%=StringUtils.toStringJSP(daticssa.getIdCSSA())%>" size=35 >
        	<a href="Javascript:ListaCSSA('LoadInserisciOrdineScarcerazione','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
          		<img src="/images/filefolder.gif" border=0>
        	</a>
      	</td>
      	<td class="l">Note</td>
      	<td class="L">
        	<textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_CSSA%>" cols=20 rows=5></textarea>
      	</td>
   	</tr>
    <tr>
      	<td class="Titolo" colspan=6> Ufficio Preposto al Controllo </td>
    </tr>
    <tr>
      	<td class="l">Ufficio di Sorveglianza</td>
      	<td class="L">
	        <input type="hidden" name="magSorv" value="S">
	        <input type="hidden" name="notificaMagistrato" value="C">
	        <input title="ufficio" value="<%=UffUDS%>" type="text" name="<%=ICostantiNotifica.CAMPO_UFF_COD_UFFICIO%>" maxlength="35" size="25">
	        <a href="Javascript:ListaUDS('LoadInserisciOrdineScarcerazione','<%=ICostantiNotifica.CAMPO_UFF_COD_UFFICIO%>');">
          		<img src="/images/filefolder.gif" border=0>
        	</a>
      	</td>
		<td class="l">Note</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_UFF%>" cols=20 rows=5></textarea>
       		<input type="HIDDEN" name="FlagMisuraAlternativa" value="S">
      	</td>
	</tr>
<%
}
%>
	<tr>
		<td class="Titolo" colspan=6>Destinatario per l'esecuzione</td>
    </tr>
    <tr>
      	<td class="l">Istituto di Detenzione</td>
<%
if (posizioneluogoaltra != null && posizioneluogoaltra.getLuogoDetenzione() != null && posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione() != null) {
%>
		<td class="l">
          	<input type="hidden" name="istituto" value="S">
          	<input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
          	<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
          	<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineScarcerazione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
            	<img src="/images/filefolder.gif" border=0>
          	</a>
		</td>
<%
} else {
%>
        <td class="l">
          	<input type="hidden" name="istituto" value="S">
          	<input readonly Title="Istituto" name="Comune" value="" size=50>
          	<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
          	<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineScarcerazione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
            	<img src="/images/filefolder.gif" border=0>
          	</a>
		</td>
<%
}
%>
      	<td class="l">Note</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiLuogoDetenzione.CAMPO_NOTE%>" cols=20 rows=5></textarea>
      	</td>
	</tr>
</table>
<table>
	<tr>
      	<td class="lNoBord" colspan="2">
        	<INPUT class="bottone" type="submit" name="I" value="Conferma">
      	</td>
    </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadInserisciOrdineScarcerazione");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
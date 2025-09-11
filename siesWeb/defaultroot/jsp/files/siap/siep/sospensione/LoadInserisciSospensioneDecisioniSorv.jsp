<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.sospensione.action.ICostantiSospensione"%>
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistratocompetente"	scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="penaresidua"        	scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="misuraalternativa"		scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="motivoProvv"			scope="request" class="java.lang.String"/>
<jsp:useBean id="UfficioEmittente"		scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="dataeditabile"			scope="request" class="java.lang.String"/>
<jsp:useBean id="nuovapenaresidua"		scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="distretto"				scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioSIUS" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="IdDocumentoSius"		scope="request" class="java.lang.String"/>
<jsp:useBean id="sospensione"        	scope="request" class="siap.siep.sospensione.model.SospensioneModel"/>
<jsp:useBean id="tipoprovvedimento"   	scope="request" class="java.lang.String"/>
<jsp:useBean id="flagergastolo"   		scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceAutorita"    	scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroMinorenni" 		scope="request" class="java.lang.String"/>
<%-- MEV10-s3: aggiunto useBean --%>
<jsp:useBean id="codiceTipoUfficio" 	scope="request" class="java.lang.String"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");

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
<title>[S.I.E.S.] - Gestione Sospensione Esecuzione Pena</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
var desktop;

function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

function ListaComuni(a_formname,a_fieldname) {
  	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

// STUB 14/12/2005 Lista Uffici della Sorveglianza per Distretti ( TDS oppure UDS)
function ListaTDS_UDS(a_formname,a_fieldname) {
	var valore = document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value;
	var i = document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.selectedIndex;
	if (i == 1) {
  		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Uffici di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
	} else {
  		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  	}
}

function ListaDocumentiSius(a_formname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>=<%=ICostantiMisuraAlternativa.CONCESSIONE_SOSPENSIONE%>", "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
}

function pulisciId() {
	document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>.value = "";
}

function Verify() {
<%
if (nuovapenaresidua != null && nuovapenaresidua.getDataFine() == null && nuovapenaresidua.getDataFinePresunta() != null) {
%>
	if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length == 1)
		document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value = '0'
		+ document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
	if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length == 1)
		document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value = '0'
		+ document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;

	var data_to_verifica = document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value
		+ '/' + document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value
		+ '/' + document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;

	if (!ControllaData(data_to_verifica)) {
		alert('Data fine pena non valida');
		return false;
	}
<%
}
if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
	if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length == 1)
		document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value = '0'
		+ document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
	if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length == 1)
		document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value = '0'
		+ document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

	var data_to_verify = document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value
		+ '-' + document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value
		+ '-' + document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

  	if (!ControllaData(data_to_verify)) {
		alert('Data di emissione non valida');
		return false;
    }

   	if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length == 1)
		document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value = '0'
		+ document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
	if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length == 1)
		document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value = '0'
		+ document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

	var data_to_verify = document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value
		+ '-' + document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value
		+ '-' + document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
	if (!ControllaData(data_to_verify)) {
		alert('Data di trasmissione non valida');
		return false;
	}
<%
}
%>
	var campo = document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>.value;



 if(document.LoadInserisciSospensioneDecisioniSorv.flagmisura.value=="N")
 {
    if(document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.value=="")
    {
			alert("La Sede dell'Autorità Emittente è obbligatoria");
			document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.focus();
			return false;
		}
		if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.value == ""
				|| document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>.value == ""
				|| document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO%>.value == "") {
			alert("La data di Sospensione esecuzione è obbligatoria");
			document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.focus();
			return false;
		}

		if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.value != ""
				|| document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>.value != ""
				|| document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO%>.value != "") {
  			if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.value.length == 1)
				document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.value = '0'
				+ document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.value;
			if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>.value.length == 1)
				document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>.value = '0'
				+ document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>.value;

			var data_to_verify = document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.value + '-' + document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>.value + '-' + document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO%>.value;
			if (!ControllaData(data_to_verify)) {
				alert('Data Sospensione esecuzione non valida');
				document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.focus();
				return false;
      		}
   		}
	}
<%
if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
	if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMagistrato.CAMPO_COGNOME%>.value == ""
			&& document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMagistrato.CAMPO_NOME%>.value == "") {
		alert("Il  Magistrato Firmatario è obbligatorio");
		document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMagistrato.CAMPO_COGNOME%>.focus();
		return false;
	}
	var istituto = true;
	if (!document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled) {
 		istituto = false;
 		if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "") {
    		istituto = true;
  		}
 	}
	var cssa = true;
	if (!document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled) {
 		cssa = false;
 		if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value == ""
 				|| document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value == "-") {
    		cssa = true;
  		}
 	}
	var uds = false;
	if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS %>.value == "") {
    	uds = true;
 	}
	var tds = false;
	if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>.value == "") {
    	tds = true;
 	}
	var polizia = true;
	if (!document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled) {
 		polizia = false;
 		if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>[document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.selectedIndex].value == '-') {
			polizia = true;
 		}
	}
	if (istituto && cssa && uds && tds && polizia) {
		alert("Inserire almeno un destinatario!");
		return false;
  	}
<%
}
%>
}

function ListaMagistrati(a_formname, a_fieldname, a_field2, a_field3) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
}

function ListaComuniTds(formname,fieldname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function ListaUDS(a_formname,a_fieldname) {
  	// MEV10-s3: aggiunto parametro di passaggio
	var a_typename = document.getElementById('<%=MinorMask.ComboMagistratoId%>').value;
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename,"Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}

function ListaCSSA(a_formname,a_fieldname,a_field2) {
	<%-- MEV10-s3: aggiunto controllo preventivo --%>
	var a_typename = document.getElementById('<%=MinorMask.ComboCSSAId%>').value;
	if (a_typename == "-")  {
        alert("Selezionare il Destinatario dell'UEPE/USSM");
	} else {
		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&typename="+a_typename, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
	}
}

function radio() {
	var pos = document.LoadInserisciSospensioneDecisioniSorv.CodPosizioneGiuridica.value;
	var nodesor = document.getElementById('divsorveglianza');
	var nodecssa = document.getElementById('divcssa');
	var nodeistituto = document.getElementById('divistituto');
	var nodeautorita = document.getElementById('divautoritacompetente');
	var nodebottone = document.getElementById('divbottone');
<%
if (misuraalternativa!= null && misuraalternativa.getCodTipoMisura() != null) {
 	if (misuraalternativa.getCodTipoMisura().equals("2000")
 			|| misuraalternativa.getCodTipoMisura().equals("2001")) {
%>
	document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
	document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
	document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
	document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = true;
	if (document.LoadInserisciSospensioneDecisioniSorv.tipo[0].checked == true) {
   		nodeistituto.style.visibility = 'visible';
   		document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = false;
		nodesor.style.top = '-33px';
		nodeistituto.style.top = '0px';
		nodebottone.style.top = '-70px';
	} else if (document.LoadInserisciSospensioneDecisioniSorv.tipo[1].checked == true) {
		nodeistituto.style.visibility = 'hidden';
		document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
		nodesor.style.top = '-63px';
		nodebottone.style.top = '-100px';
	}
<%
	} else if (misuraalternativa.getCodTipoMisura().equals("2480")) {
%>
	if (pos == "02" || pos == "04") {
 		document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
 		if (document.LoadInserisciSospensioneDecisioniSorv.tipo[0].checked == true) {
			nodeistituto.style.visibility = 'hidden';
			nodeautorita.style.visibility = 'visible';
			nodecssa.style.visibility = 'visible';
			nodesor.style.top = '-30px';
			nodeautorita.style.top = '-30px';
			nodecssa.style.top = '-30px';
			nodebottone.style.top = '-30px';
		} else if (document.LoadInserisciSospensioneDecisioniSorv.tipo[1].checked == true) {
			nodeistituto.style.visibility = 'hidden';
			nodeautorita.style.visibility = 'visible';
			nodecssa.style.visibility = 'visible';
			nodesor.style.top = '-30px';
			nodeautorita.style.top = '-30px';
			nodecssa.style.top = '-30px';
			nodebottone.style.top = '-30px';
 		}
	} else {
 		if (document.LoadInserisciSospensioneDecisioniSorv.tipo[0].checked == true) {
			nodeistituto.style.visibility = 'visible';
			nodeautorita.style.visibility = 'visible';
			nodecssa.style.visibility = 'visible';
			nodesor.style.top = '0px';
			nodeautorita.style.top = '0px';
			nodecssa.style.top = '0px';
			nodebottone.style.top = '0px';
			document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = false;
		} else if (document.LoadInserisciSospensioneDecisioniSorv.tipo[1].checked == true) {
			nodeistituto.style.visibility = 'hidden';
			nodeautorita.style.visibility = 'visible';
			nodecssa.style.visibility = 'visible';
			nodesor.style.top = '-30px';
			nodeautorita.style.top = '-30px';
			nodecssa.style.top = '-30px';
			nodebottone.style.top = '-30px';
			document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
 		}
	}
<%
	}
}
%>
}
</script>
<jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>
</head>
<%
if (misuraalternativa!= null && misuraalternativa.getIdMisuraAlternativa() != null) {
%>
<body class="corpo" onLoad="radio();">
<%
} else {
%>
<body class="corpo">
<%
}
%>
<table>
	<tr>
	  	<td class="LBG">
	  		<a href="Javascript:window.print();">
	  			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
	  		</a>
	  	</td>
		<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
	   		<font class="campo">Sospensione dell'esecuzione della pena</font>
	  	</td>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciSospensioneDecisioniSorv">
<%
BigDecimal lIdOrdinanzaSius = null;
if (misuraalternativa != null && misuraalternativa.getEveIdEvento() != null) {
	lIdOrdinanzaSius = misuraalternativa.getEveIdEvento();
}
%>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActInserisciSospensioneDecisioniSorv">
<input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="<%=StringUtils.toStringJSP(lIdOrdinanzaSius)%>">
<input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA%>" value="">
<input type="HIDDEN" name="posizionegiuridica" value="<%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>">
<input type="HIDDEN" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>">
<input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA%>" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>">
<%
if (misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa() == null) {
%>
<input type="HIDDEN" name="flagmisura" value="N">
<%
} else {
%>
<input type="HIDDEN" name="flagmisura" value="S">
<%
}

String scarcerato = null;
String scarcerare = "checked";

if (misuraalternativa != null && misuraalternativa.getCodTipoUfficioScarcerazione() != null) {
	if (misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV")) {
   		scarcerato ="checked";
  	} else if (misuraalternativa.getCodTipoUfficioScarcerazione().equals("PROC")) {
   		scarcerare ="checked";
  	}
}
%>
<table cellspacing="0" cellpadding="0" width="95%">
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
	  	<td class="l">Detenuto presso</td>
	  	<td class="L" colspan="5">
	  		<font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
		if (lAltraCausa.getIstitutoDetenzione().getDescrComune() != null) {
%>
			&nbsp;di&nbsp;<font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
<%
		}
%>
		</td>
	</tr>
<%
		if (lAltraCausa != null && lAltraCausa.getAltroLuogo() != null) {
%>
	<tr>
		<td class="l">Altro Luogo </td>
		<td class="L" colspan="5">
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
		<td class="L" colspan="5">
			<font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
	if (lLuogoDetenzione.getIstitutoDetenzione().getDescrComune() != null) {
%>
			&nbsp;di&nbsp;<font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
<%
	}
%>
		</td>
	</tr>
<%
}
// Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
if (lPosizione.getCodPosizioneGiuridica() != null
		&& (lPosizione.getCodPosizioneGiuridica().equals("02")
			|| lPosizione.getCodPosizioneGiuridica().equals("04"))) {
	if (lLuogoDetenzione.getIstitutoDetenzione() != null) {
%>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="L" colspan="5">
			<font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>
		</td>
	</tr>
<%
	}
}
%>
	<tr>
<%
if (penaresidua.getIdPenaResidua() != null && ((penaresidua.getFlagErgastolo() == null)
		|| (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S")
		&& !penaresidua.getFlagErgastolo().equals("D")))) {
	if (penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0) {
		// do Nothing
	} else {
%>
		<td class="l">Reclusione</td>
		<td class="l" colspan=2>
			<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
		</td>
<%
		if (penaresidua.getImportoMulta().compareTo((new BigDecimal(0))) != 0) {
%>
		<td class="l">Multa</td>
		<td class="l" colspan="2">
			<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font>
		</td>
<%
		}
	}
%>
	</tr>
	<tr>
<%
    if (penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0
    		&& penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0) {
    	// do Nothing
    } else {
%>
		<td class="l">Arresto</td>
		<td class="l" colspan=2>
			<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
		</td>
<%
		if (penaresidua.getImportoAmmenda().compareTo((new BigDecimal(0))) != 0) {
%>
      	<td class="l">Ammenda</td>
      	<td class="l" colspan="2">
      		<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
      	</td>
<%
      	}
	}
}
%>
	<tr>
<%
if (penaresidua.getDataInizio() != null) {
%>
		<td class="l">Data Decorrenza Pena</td>
		<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%></font>
		</td>
<%
}
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
if ((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa() != null
		&& lFascicoloAssociato.getFlagAltraCausa().equals("S"))) {
	if ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null
			&& !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) {
		if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan=2>
		  	<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			-
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			-
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
<%
		} else if (penaresidua.getDataFine() != null) {
			if (penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan="2">
		  	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%></font>
		</td>
<%
			} else {
%>
		<td class="l">Data Fine Pena</td>
		<td class="lRosso" colspan=2>
		  	<font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%></font>
		</td>
<%
          	}
		}
	}
}
if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
	</tr>
	<tr>
		<td class="l">Data Emissione</td>
		<td class="L">
			<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
		<td class="l">Data Trasmissione</td>
		<td class="L">
			<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
<%
}
%>
</table>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
	  	<td class="Titolo" colspan="4">Dati Provvedimento di Sospensione del Magistrato/Tribunale di Sorveglianza</td>
	</tr>
<%
if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
	<tr>
  		<td class="l" width="15%">Anno / Numero Sius</td>
      	<td class="l" width="20%">
        	<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
   		</td>
		<td class="l" width='25%'>Anno / Numero Provvedimento</td>
  		<td class="l" width=20%>
   			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
      	</td>
	</tr>
	<tr>
		<td class="l">Tipo provvedimento</td>
		<td class="l" colspan="3">
    		<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoDecisione())%></font>
   		</td>
  	</tr>
	<tr>
   		<td class="l">Autorità Emittente</td>
   		<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
<%
	String descrTipoUfficio = StringUtils.toStringJSP(UfficioEmittente.getDescrTipoUfficio());
	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
			"UDSM".equals(UfficioEmittente.getCodTipoUfficio())) {
		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
	}
%>
		<td class="l" colspan="3">
			<font class="campo"><%=descrTipoUfficio%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(UfficioEmittente.getDescrComune())%></font>
		</td>
	</tr>
	<tr>
 		<td class="l">Oggetto Decisione</td>
     	<td class="l" colspan="3">
     		<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>
   		</td>
	</tr>
	<tr>
		<td class="l">Data Emissione Provvedimento </td>
		<td class="l" colspan="3">
       		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(), "dd-MM-yyyy"))%></font>
    	</td>
	</tr>
	<tr>
	  	<td class="l">Motivazioni</td>
	  	<td class="L" colspan="3">
	    	<TEXTAREA title="Motivazioni" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE%>" cols="80" rows="2"><%=StringUtils.toStringJSP(misuraalternativa.getNote())%></textarea>
	  	</td>
	</tr>
<%
} else {
%>
	<tr>
      	<td class="l" colspan="4">
        	<a href="Javascript:ListaDocumentiSius('LoadInserisciSospensioneDecisioniSorv');">
          		Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0>
        	</a>
      	</td>
	</tr>
    <tr>
      	<td class="l">Anno / Numero SIUS</td>
      	<td class="l">
        	<input Title="Anno Fascicolo Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" 
				onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
               type="text" size="4" maxlength="4" onChange="pulisciId();">
        	/
        <input Title="Numero Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>" type="text" size="6" maxlength="6" onChange="pulisciId();">
      	</td>
      	<td class="l">Anno / Numero Provvedimento</td>
      	<td class="l">
	        <input Title="Anno Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" 
				onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
               type="text" size="4" maxlength="4" onChange="pulisciId();">
	        /
        <input Title="Numero Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" type="text" size="6" maxlength="6" onChange="pulisciId();">
		</td>
	</tr>
   	<tr>
      	<td class="l">Tipo provvedimento </td>
      	<td class="l" colspan="3">
       		<select Title="Tipo Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>" onChange="pulisciId();">
         		<%=tipoprovvedimento%>
       		</select>
      	</td>
	</tr>
    <tr>
      	<td class="l">Autorità Emittente</td>
      	<td class="l" colspan="3">
          	<%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff, "onChange='pulisciId();'", ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA, tipoUfficioSIUS)%>
      	</td>
    </tr>
    <tr>
      	<td class="l">Sede Autorità Emittente <font class=ob>(*)</font></td>
      	<td class="l" colspan="3">
        	<font class="campo">
          <input Title="Luogo Ufficio Sorveglianza" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>" size=35 type="text" onChange="pulisciId();">
          		<a href="Javascript:ListaComuniEmitUTMinor('LoadInserisciSospensioneDecisioniSorv','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');">
            		<img src="/images/filefolder.gif" border=0>
          		</a>
        	</font>
      	</td>
	</tr>
    <tr>
      	<td class="l">Oggetto Provvedimento</td>
      	<td class="L" colspan="3">
        	<select  Title="Codice Motivo" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" onChange="pulisciId();">
          		<%=motivoProvv%>
        	</select>
      	</td>
    </tr>
    <tr>
      	<td class="l">Data Emissione Provvedimento</td>
      	<td class="l" colspan="3">
        	<font class="campo">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();">
        	</font>
      	</td>
    </tr>

    <tr>
     	<td class="l">Motivazioni</td>
      	<td class="L" colspan="3">
        <TEXTAREA title="Motivazioni" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE %>" cols=80 rows=2></textarea>
      	</td>
	</tr>
<%
}
%>
</table>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
<%
if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
		<td  class="l" width="20%">Data Sospensione Esecuzione</td>
      	<td class="l">
      		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataScarcerazione(),"dd-MM-yyyy"))%></font>
      	</td>
      	<td class="l">
      		Da Scarcerare &nbsp;<input type="radio" name="tipo" value="scarcerare" <%=StringUtils.toStringJSP(scarcerare)%> onclick="radio();">
      		&nbsp;Libero per avvenuta Scarcerazione&nbsp;<input type="radio" name="tipo" value="scarcerato" <%=StringUtils.toStringJSP(scarcerato)%> onclick="radio();">
      	</td>
<%
} else {
%>
		<td class="l" width="20%">Data Sospensione Esecuzione <font class=ob>(*)</font></td>
		<td class="l">
			<input type="text" size="2" maxlength="2" value=""  name="<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        	<input type="text" size="2" maxlength="2" value=""  name="<%= ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        	<input type="text" size="4" maxlength="4" value=""  name="<%= ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
<%
}
%>
	</tr>
</table>
<%
if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
      	<td class="Titolo" colspan="8">Dati della Pena</td>
    </tr>
<%
	if (sospensione.getNumAnniPenaEspiata().intValue() != 0
			|| sospensione.getNumMesiPenaEspiata().intValue() != 0
			|| sospensione.getNumGiorniPenaEspiata().intValue() != 0) {
%>
	<tr>
		<td class="l" width="20%">
			<font class="label">Pena Espiata</font>
		</td>
		<td class="l">
			<font class="label">Anni</font>&nbsp;
			<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaEspiata(), "0")%></font>&nbsp;
			<font class="label">Mesi</font>&nbsp;
			<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaEspiata(), "0")%></font>&nbsp;
			<font class="label">Giorni</font>&nbsp;
			<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaEspiata(), "0")%></font>
<%
		if (sospensione.getMultaEspiata() != null && sospensione.getMultaEspiata().compareTo(new BigDecimal(0)) != 0) {
%>
            €&nbsp;<font class="label">Multa</font>€&nbsp;
            <font class="campo"><%=StringUtils.toEuroFormat(sospensione.getMultaEspiata())%></font>
<%
		}
		if (sospensione.getAmmendaEspiata() != null && sospensione.getAmmendaEspiata().compareTo(new BigDecimal(0)) != 0) {
%>
            &nbsp;<font class="label">Ammenda</font>&nbsp;
            <font class="campo"><%=StringUtils.toEuroFormat(sospensione.getAmmendaEspiata())%></font>
<%
		}
%>
		</td>
	</tr>
<%
	}
	if (flagergastolo.equals("N") && (sospensione.getNumAnniPenaResiduaReclus().intValue() != 0
			|| sospensione.getNumMesiPenaResiduaReclus().intValue() != 0
	  	 	|| sospensione.getNumGiorniPenaResiduaReclus().intValue() != 0
       		|| sospensione.getNumAnniPenaResiduaArres().intValue() != 0
	  	 	|| sospensione.getNumMesiPenaResiduaArres().intValue() != 0
	  	 	|| sospensione.getNumGiorniPenaResiduaArres().intValue() != 0)) {
%>
	<tr>
		<td class="l">
			<font class="label">Pena Residua</font>
		</td>
		<td class="l">
<%
		if (flagergastolo.equals("N") && (sospensione.getNumAnniPenaResiduaReclus().intValue() != 0
				|| sospensione.getNumMesiPenaResiduaReclus().intValue() != 0
             	|| sospensione.getNumGiorniPenaResiduaReclus().intValue() != 0)) {
%>
            <font class="label">Reclusione:</font>&nbsp;
            <font class="label">Anni</font>&nbsp;
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaReclus(), "0")%></font>&nbsp;
            <font class="label">Mesi</font>&nbsp;
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaReclus(), "0")%></font>&nbsp;
            <font class="label">Giorni</font>&nbsp;
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaReclus(), "0")%></font>
<%
			if (sospensione.getMultaResidua() != null && sospensione.getMultaResidua().compareTo(new BigDecimal(0)) != 0) {
%>
			&nbsp;<font class="label">Multa</font>&nbsp;
			<font class="campo"><%=StringUtils.toEuroFormat(sospensione.getMultaResidua())%></font>
<%
            }
		}
		if (flagergastolo.equals("N") && (sospensione.getNumAnniPenaResiduaArres().intValue() != 0
				|| sospensione.getNumMesiPenaResiduaArres().intValue() != 0
             	|| sospensione.getNumGiorniPenaResiduaArres().intValue() != 0)) {
%>
            &nbsp;<font class="label"> Arresto:</font>&nbsp;
            <font class="label">Anni</font>&nbsp;
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaArres(), "0")%></font>&nbsp;
            <font class="label">Mesi</font>&nbsp;
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaArres(), "0")%></font>&nbsp;
            <font class="label">Giorni</font>&nbsp;
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaArres(), "0")%></font>
<%
            if (sospensione.getAmmendaResidua() != null && sospensione.getAmmendaResidua().compareTo(new BigDecimal(0)) != 0) {
%>
			&nbsp;<font class="label">Ammenda </font>&nbsp;
			<font class="campo"><%=StringUtils.toEuroFormat(sospensione.getAmmendaResidua())%></font>
<%
            }
		}
        if (flagergastolo.equals("N") && (sospensione.getNumAnniPenaResiduaReclus().intValue() != 0
        		|| sospensione.getNumMesiPenaResiduaReclus().intValue() != 0
				|| sospensione.getNumGiorniPenaResiduaReclus().intValue() != 0
				|| sospensione.getNumAnniPenaResiduaArres().intValue() != 0
				|| sospensione.getNumMesiPenaResiduaArres().intValue() != 0
				|| sospensione.getNumGiorniPenaResiduaArres().intValue() != 0)) {
%>
		</td>
	</tr>
<%
		}
    }
    if (flagergastolo.equals("S")) {
%>
	<tr>
	  	<td class="l">
	    	<font class="label">Pena Complessiva</font>
	  	</td>
	  	<td class="l">
	    	<font class="campo">ERGASTOLO</font>
		</td>
	</tr>
<%
	} else if (flagergastolo.equals("D")) {
%>
	<tr>
		<td class="l">
			<font class="label">Pena Complessiva</font>
		</td>
		<td class="l">
			<font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font>
		</td>
	</tr>
<%
	}
	if (nuovapenaresidua != null && nuovapenaresidua.getDataInizio() != null) {
%>
	<tr>
		<td class="l">Data Decorrenza Pena</td>
		<td class="l" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataInizio(),"dd-MM-yyyy"))%></font>
		</td>
<%
	}
	if (nuovapenaresidua != null && nuovapenaresidua.getDataFine() == null && nuovapenaresidua.getDataFinePresunta() != null) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan=2>
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFinePresunta(), "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			-
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFinePresunta(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			-
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFinePresunta(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
<%
	} else if (nuovapenaresidua.getDataFine() != null) {
		if (nuovapenaresidua.getDataFine().equals(nuovapenaresidua.getDataFinePresunta())) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan="2">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "dd-MM-yyyy"))%></font>
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "dd"))%>" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "MM"))%>" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "yyyy"))%>" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>">
		</td>
<%
		} else {
%>
		<td class="l">Data Fine Pena</td>
		<td class="lRosso" colspan=2>
			<font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(),"dd-MM-yyyy"))%></font>
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "dd"))%>" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "MM"))%>" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "yyyy"))%>" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>">
		</td>

<%
		}
	}
%>
</tr>
</table>
<%
}
%>
<table cellspacing="0" cellpadding="0" width="95%">
<%
if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
	<tr>
		<td class="Titolo" colspan="6">Magistrato Firmatario</td>
   	</tr>
  	<tr>
   		<%-- MEV10-s3: aggiunta proprietà per la larghezza del campo --%>
 		<td class="l" width="20%">Magistrato Firmatario</td>
   		<td class="L">
   			<input type="HIDDEN" name="presenzanuovopenaricalcolata" value="S">
       		<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato())%>">
       		<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25">
       		<input readonly title= "Nome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="25">
        	<a href="Javascript:ListaMagistrati('LoadInserisciSospensioneDecisioniSorv','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%=ICostantiMagistrato.CAMPO_COGNOME%>','<%=ICostantiMagistrato.CAMPO_NOME%>');">
        		<img src="/images/filefolder.gif" border=0>
        	</a>
      	</td>
	</tr>
   	<tr>
      	<td class="Titolo" colspan=6>Destinatari</td>
   	</tr>
</table>
<div id="divistituto" style="visibility:hidden; position:relative;">
<table cellspacing="0" cellpadding="0" width="95%">
<tr>
  <td class="l" width ="20%">Istituto di Detenzione</td>
  <td class="l" colspan="3">
     <input readonly Title="Istituto" name="Comune" value="" size="50">
     <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
     <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciSospensioneDecisioniSorv','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>','Comune');">
     <img src="/images/filefolder.gif" border=0></a>
  </td>
</tr>
</table>
</div>
<div id="divcssa" style="visibility:hidden; position:relative;">
<table cellspacing="0" cellpadding="0" width="95%">
	<%-- MEV10-s3: modificato layout con aggiunta etichette --%>
	<tr>
		<td class="Titolo" colspan="4">UEPE/USSM</td>
	</tr>
	<tr>
		<td class="l" width="20%">Destinatario</td>
		<td class="l" colspan="3"><%=MinorMask.comboCSSATrattino(MinorMask.ComboCSSAId)%></td>
	</tr>
    <tr>
    	<td class="l">Sede</td>
       	<td class="l" colspan="3">
        	<input readonly Title="Sede UEPE Competente" name="Indirizzo" value="" size=60 >
        	<input type="hidden" Title="Sede UEPE Competente" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="" size=35 >
        	<a href="Javascript:ListaCSSA('LoadInserisciSospensioneDecisioniSorv','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
         		<img src="/images/filefolder.gif" border=0>
        	</a>
      	</td>
	</tr>
</table>
</div>
<div id="divsorveglianza" style="visibility:visible; position:relative;">
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="Titolo" colspan="4">Ufficio / Magistrato di Sorveglianza</td>
	</tr>
  	<tr>
    	<td class="l" width="20%">Destinatario</td>
    	<td class="l" colspan="3"><%=MinorMask.comboMagistratoTrattino()%></td>
    </tr>
    <tr>
    	<td class="l">Sede</td>
      	<td class="L" colspan="3">
      		<input title="ufficio" value="" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS %>" maxlength="35" size="25">
      		<a href="Javascript:ListaUDS('LoadInserisciSospensioneDecisioniSorv','<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>');">
      			<img src="/images/filefolder.gif" border=0>
      		</a>
      	</td>
	</tr>

	<tr>
		<td class="Titolo" colspan="4">Tribunale di Sorveglianza</td>
	</tr>
	<tr>
		<td class="l" width="30%">Destinatario</td>
        <td class="L" colspan="3"><%=MinorMask.comboTribunaleTrattino()%></td>
    </tr>
  	<tr>
	   	<td class="l">Sede</td>
     	<td class="l" colspan="3">
     		<input type="hidden" value="TDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>">
     		<input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>" maxlength="35" size="35">
     		<a href="Javascript:ListaComuniTds('LoadInserisciSospensioneDecisioniSorv','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>');">
     			<img src="/images/filefolder.gif" border=0>
     		</a>
     	</td>
	</tr>
</table>
</div>
<div id="divautoritacompetente" style="visibility:hidden; position:relative; ">
<table cellspacing="0" cellpadding="0" width="95%">
  	<tr>
    	<td class="l" width="20%">Autorità di polizia</td>
		<td class="L" colspan="3">
      		<select  Title="Autorita Esterna" class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>">
        		<%=codiceAutorita%>
      		</select>
      	</td>
  	</tr>
  	<tr>
		<td class="l">Sede</td>
		<td class="L">
	    	<input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>" maxlength="35" size="35">
			<a href="Javascript:ListaComuni('LoadInserisciSospensioneDecisioniSorv','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>');">
	    		<img src="/images/filefolder.gif" border=0>
	  		</a>
		</td>
		<td class="l">Indirizzo</td>
		<td class="L">
	  		<TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>" cols="30"></textarea>
		</td>
	</tr>
</table>
</div>
<%
}
%>
<div id="divbottone" style="visibility:visible; position:relative;">
<table>
	<tr>
	    <td class="lNoBord" colspan="2">
	      	<br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
	    </td>
	</tr>
</table>
</div>
</form>
<script language="JavaScript" type="text/javascript">
	var frmvalidator  = new Validator("LoadInserisciSospensioneDecisioniSorv");
<%
if (nuovapenaresidua != null && nuovapenaresidua.getDataFine() == null && nuovapenaresidua.getDataFinePresunta() != null) {
%>
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena è obbligatorio");
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");

	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena è obbligatorio");
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");

	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena è obbligatorio");
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2099");
<%
}
if (misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa() == null) {
%>
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","lt=2099");
	
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","lt=2099");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>","numeric");
	
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","req","Il campo Giorno Data Emissione Ordinanza è obbligatorio");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","numeric");
	
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","req","Il campo Mese Data Emissione Ordinanza è obbligatorio");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","numeric");
	
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","req","Il campo Anno Data Emissione Ordinanza è obbligatorio");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","lt=2099");
<%
	if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
	frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");
	frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");
	frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
	frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2099");
<%
	}
}
if (misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa() == null) {
%>
  	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>","alphabetic");
<%
}
%>
</script>
</body>
</html>
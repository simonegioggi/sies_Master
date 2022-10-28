<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sospensione.action.ICostantiSospensione"%>
<%@ page import="siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep"%>
<%@ page import="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>

<jsp:useBean id="eventonotifica"     	scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="modalita"           	scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"        	scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="codiceAutoritaE"   	scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaAvv"  	scope="request" class="java.lang.String"/>
<jsp:useBean id="dataeditabile"   		scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente"	scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="avvocati"   			scope="request" class="java.util.Vector"/>
<jsp:useBean id="contenuto"  			scope="request" class="java.lang.String"/>
<jsp:useBean id="decretoordinanza" 		scope="request" class="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"/>
<jsp:useBean id="flagdecretoordinanza" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="uffTDS"   				scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="Istituto"   			scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="SedeUGC"      			scope="request" class="java.lang.String"/>
<jsp:useBean id="Sedeautorita"      	scope="request" class="java.lang.String"/>
<jsp:useBean id="SedeGE"      			scope="request" class="java.lang.String"/>
<jsp:useBean id="Cssa"   				scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="eventoOE"      		scope="request" class="java.lang.String"/>
<jsp:useBean id="esisteOE"   			scope="request" class="java.lang.String"/>
<%-- MEV_66: aggiunti useBean per gestione combo tds ed uds ed uepe --%>
<jsp:useBean id="ufficioMdS"   			scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioTdS"   			scope="request" class="java.lang.String"/>
<jsp:useBean id="uepe"		   			scope="request" class="java.lang.String"/>

<% 
	Collection lColAutEmi = (Collection) request.getAttribute("lColAutEmi");
	
	FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

	// Per tutti i provvedimenti che lo gestiscono
	// aggiungere campo OBBLIGATORIO editabile CAMPO_CASELLARIO
	//       1. precaricato a '-' se lo stato nascita dell'imputato e' blank
	//       2. altrimenti COD_UFFICIO dell'utente collegato
	// ad eccezione dei quattro provvedimento sotto elencati.
	// SOLO per questi 4 provvedimenti e se l'imputato e' straniero (stato nascita diverso da ITALIA) --> casellario = ROMA
	//   -- Computo fungibilita'
	//   -- Unificazione delle pene concorrenti
	//   -- Rideterminazione della pena
	//   -- Sospensione pena 656 
	SoggettoModel lSoggettoAssociato = lFascicoloAssociato.getSoggetto();
	
	UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
	UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
	 
	String lCasellario = lUfficioUtenteConnesso.getDescrComune();
	
	if( lSoggettoAssociato != null 
	    && 
	    	(    lSoggettoAssociato.getCodStatoNascita() == null
	    	  ||  "".equals(lSoggettoAssociato.getCodStatoNascita()) 
	    	  || "-".equals(lSoggettoAssociato.getCodStatoNascita())
	    	 )
	   )
	{
	  lCasellario = "-";
	}
%>

<%
//==============================================================================
// Questa jsp viene invocata due volte:
// - la prima in fase di inserimento
//==============================================================================
%>


<%
  //FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
  DecretoOrdinanzaSiepModel lDecOrdMod = (decretoordinanza != null) ? decretoordinanza : new DecretoOrdinanzaSiepModel();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

  String difensore = null;
  String interessato = null;
  String ufficio = null;

  if(lDecOrdMod.getFlagPresentanteIstanza() != null)
  {
    if(lDecOrdMod.getFlagPresentanteIstanza().equals("D"))
    {
      difensore ="checked";
    }
    else if(lDecOrdMod.getFlagPresentanteIstanza().equals("I"))
    {
      interessato ="checked";
    }
    else if(lDecOrdMod.getFlagPresentanteIstanza().equals("U"))
    {
      ufficio ="checked";
    }
  }
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione evento</title>
<!-- ActLoadInserisciSospensioneEsecPenaDispPm -->
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
	function Verifica() {
		// Se il "Foglio Complementare" e' selezionato, il "Casellario Giudiziale" e' obbligatorio
	  	if (document.LoadInserisciSospensione.<%=ICostantiDecretoOrdinanzaSiep.FOGLIO_COMPLEMENTARE%>.checked == true) {
      		if (document.LoadInserisciSospensione.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.value == '-'
      				|| document.LoadInserisciSospensione.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.value == '') {
      			alert("Il campo Casellario Giudiziale e' obbligatorio!");
      			document.LoadInserisciSospensione.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.focus();
           		return false;
         	}		  	
		}

    	if (!document.LoadInserisciSospensione.istanzapresentata[0].checked
    			&& !document.LoadInserisciSospensione.istanzapresentata[1].checked
    			&& !document.LoadInserisciSospensione.istanzapresentata[2].checked) {
    		alert("Il campo Istanza presentata e' obbligatorio!");
      		return false;
    	}

    	// Se il radio-button selezionato e' ‘di ufficio’ la data di deposito non e' obbligatoria
    	if (!document.LoadInserisciSospensione.istanzapresentata[2].checked) {
      		if (document.LoadInserisciSospensione.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_DEPOSITO_ISTANZA%>.value.length==1)
        		document.LoadInserisciSospensione.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_DEPOSITO_ISTANZA%>.value='0'+document.LoadInserisciSospensione.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_DEPOSITO_ISTANZA%>.value;
      		if (document.LoadInserisciSospensione.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_DEPOSITO_ISTANZA%>.value.length==1)
        		document.LoadInserisciSospensione.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_DEPOSITO_ISTANZA%>.value='0'+document.LoadInserisciSospensione.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_DEPOSITO_ISTANZA%>.value;
      		var data_to_verify_de = document.LoadInserisciSospensione.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_DEPOSITO_ISTANZA%>.value+'-'+document.LoadInserisciSospensione.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_DEPOSITO_ISTANZA%>.value+'-'+document.LoadInserisciSospensione.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_DEPOSITO_ISTANZA%>.value;
      		if (!ControllaData(data_to_verify_de)) {
        		alert('Data di Deposito istanza non valida');
        		document.LoadInserisciSospensione.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_DEPOSITO_ISTANZA%>.focus();
        		return false;
      		}
    	}

    	// DATA SOSPENSIONE/REVOCA
    	if (document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.value.length==1)
      		document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.value='0'+document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.value;
    	if (document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>.value.length==1)
      		document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>.value='0'+document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>.value;
    	var data_to_verify_so = document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.value+'-'+document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>.value+'-'+document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO%>.value;
    	// Se il contenuto e' di tipo ‘sospensione’ la data sospensione e' obbligatoria;
    	// se il contenuto e' di tipo ’revoca’ la data revoca non e' obbligatoria;
    	if (document.LoadInserisciSospensione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=='0900'
    			|| document.LoadInserisciSospensione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=='0901'
       			|| document.LoadInserisciSospensione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=='0902'
       			|| document.LoadInserisciSospensione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=='0903'
      			|| document.LoadInserisciSospensione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=='0947'
       			|| document.LoadInserisciSospensione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=='0952') {
      		if (!ControllaData(data_to_verify_so)) {
        		alert('Data Sospensione non valida');
        		document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.focus();
        		return false;
      		}
    	} else if (document.LoadInserisciSospensione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=='0920'
    			|| document.LoadInserisciSospensione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=='0921') {
      		if (!ControllaDataPassaVuota(data_to_verify_so)) {
        		alert('Data Revoca non valida');
        		document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.focus();
        		return false;
      		}
    	}

    	if (document.LoadInserisciSospensione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>[document.LoadInserisciSospensione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.selectedIndex].value == '-') {
      		alert("Il campo Contenuto Decreto e' obbligatorio!");
      		document.LoadInserisciSospensione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.focus();
      		return false;
    	}

    	if (document.LoadInserisciSospensione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
      		document.LoadInserisciSospensione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciSospensione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
    	if (document.LoadInserisciSospensione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
      		document.LoadInserisciSospensione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciSospensione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

   		var data_to_verify = document.LoadInserisciSospensione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.LoadInserisciSospensione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.LoadInserisciSospensione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

    	if (!ControllaData(data_to_verify)) {
      		alert('Data di Trasmissione non valida');
      		document.LoadInserisciSospensione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
      		return false;
   		}

    	<%-- MEV_66: modificata gestione controlli obbligatorieta' campi --%>
<%--     	if (!document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>.disabled) { --%>
		if (document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>.value != '') {
      		if (document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>[document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>.selectedIndex].value == '-') {
        		alert("Il campo Autorita' per Esecuzione/Restituzione e' obbligatorio, se la Sede e' valorizzata!");
        		document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>.focus();
        		return false;
      		}
    	}

<%--     	if (!document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>.disabled) { --%>
		if (document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>[document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>.selectedIndex].value != '-') {
     		if (document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>.value == '') {
        		alert("Il campo Sede Autorita' per Esecuzione/Restituzione e' obbligatorio, se l'Autorita' per Esecuzione/Restituzione e' valorizzata!");
        		document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>.focus();
        		return false;
      		}
    	}

<%--     	if (!document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_UGCONDANNATO%>.disabled) { --%>
		if (document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_UGCONDANNATO%>.value != '') {
     		if (document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_UGCONDANNATO%>[document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_UGCONDANNATO%>.selectedIndex].value == '-') {
        		alert("Il campo Notifica al condannato e' obbligatorio, se la Sede e' valorizzata!");
        		document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_UGCONDANNATO%>.focus();
        		return false;
      		}
    	}

<%--     	if (!document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_UGCONDANNATO%>.disabled) { --%>
		if (document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_UGCONDANNATO%>[document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_UGCONDANNATO%>.selectedIndex].value != '-') {
     		if (document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_UGCONDANNATO%>.value == '') {
		        alert("Il campo Sede Notifica al condannato e' obbligatorio, se il campo Notifica al condannato e' valorizzato!");
		        document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_UGCONDANNATO%>.focus();
		        return false;
      		}
    	}

//     	if (!document.LoadInserisciSospensione.ufficioTds.disabled) {
		if (document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_TDS%>.value != '') {
  			if (document.LoadInserisciSospensione.ufficioTds[document.LoadInserisciSospensione.ufficioTds.selectedIndex].value == '-') {
		        alert("Il campo Tribunale di Sorveglianza e' obbligatorio, se la Sede e' valorizzata!");
		        document.LoadInserisciSospensione.ufficioTds.focus();
		        return false;
      		}
    	}

<%--     	if (!document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_TDS%>.disabled) { --%>
		if (document.LoadInserisciSospensione.ufficioTds[document.LoadInserisciSospensione.ufficioTds.selectedIndex].value != '-') {
  			if (document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_TDS%>.value == '') {
		        alert("Il campo Sede Tribunale di Sorveglianza e' obbligatorio, se il campo Tribunale di Sorveglianza e' valorizzato!");
		        document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_TDS%>.focus();
		        return false;
      		}
    	}

//     	if (!document.LoadInserisciSospensione.ufficioMdS.disabled) {
		if (document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_UDS%>.value != '') {
  			if (document.LoadInserisciSospensione.ufficioMdS[document.LoadInserisciSospensione.ufficioMdS.selectedIndex].value == '-') {
		        alert("Il campo Magistrato di Sorveglianza e' obbligatorio, se la Sede e' valorizzata!");
		        document.LoadInserisciSospensione.ufficioMdS.focus();
		        return false;
      		}
    	}

<%--     	if (!document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_UDS%>.disabled) { --%>
		if (document.LoadInserisciSospensione.ufficioMdS[document.LoadInserisciSospensione.ufficioMdS.selectedIndex].value != '-') {
  			if (document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_UDS%>.value == '') {
		        alert("Il campo Sede Magistrato di Sorveglianza e' obbligatorio, se il campo Magistrato di Sorveglianza e' valorizzato!");
		        document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_UDS%>.focus();
		        return false;
      		}
    	}

<%--     	if (!document.LoadInserisciSospensione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.disabled) { --%>
		if (document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_GE%>.value != '') {
     		if (document.LoadInserisciSospensione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>[document.LoadInserisciSospensione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.selectedIndex].value == '-') {
        		alert("Il campo GE Competente e' obbligatorio, se la Sede e' valorizzata!");
        		document.LoadInserisciSospensione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.focus();
        		return false;
      		}
    	}

<%--     	if (!document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_GE%>.disabled) { --%>
		if (document.LoadInserisciSospensione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>[document.LoadInserisciSospensione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.selectedIndex].value != '-') {
     		if (document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_GE%>.value == '') {
		        alert("Il campo Sede GE Competente e' obbligatorio, se il campo GE Competente e' valorizzato!");
		        document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_GE%>.focus();
		        return false;
      		}
   		}

<%--     	if (!document.LoadInserisciSospensione.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled) { --%>
		if (document.LoadInserisciSospensione.Indirizzo.value != '') {
			if (document.LoadInserisciSospensione.uepe[document.LoadInserisciSospensione.uepe.selectedIndex].value == '-') {
        		alert("Il campo UEPE e' obbligatorio, se la Sede e' valorizzata!");
        		document.LoadInserisciSospensione.uepe.focus();
        		return false;
      		}
    	}

<%--     	if (!document.LoadInserisciSospensione.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled) { --%>
		if (document.LoadInserisciSospensione.uepe[document.LoadInserisciSospensione.uepe.selectedIndex].value != '-') {
      		if (document.LoadInserisciSospensione.Indirizzo.value == '') {
        		alert("Il campo Sede UEPE e' obbligatorio, se il campo UEPE e' valorizzato!");
        		document.LoadInserisciSospensione.Indirizzo.focus();
        		return false;
      		}
    	}

    	if (document.LoadInserisciSospensione.<%=ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF%>.value != "") {
    		if (document.LoadInserisciSospensione.<%=ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF%>[document.LoadInserisciSospensione.<%=ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF%>.selectedIndex].value == '-') {
				alert("Se la Sede e' valorizzata, il campo Autorita' Destinazione deve essere valorizzato!");
				document.LoadInserisciSospensione.<%=ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF%>.focus();
	        	return false;
    		}
    	}

    	if (document.LoadInserisciSospensione.<%=ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF%>[document.LoadInserisciSospensione.<%=ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF%>.selectedIndex].value != '-') {
    		if (document.LoadInserisciSospensione.<%=ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF%>.value == "") {
				alert("Se il campo Autorita' Destinazione e' valorizzato, la Sede deve essere valorizzata!");
				document.LoadInserisciSospensione.<%=ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF%>.focus();
	        	return false;
    		}
    	}

    	//==========================================================================
    	// Se non esiste un ordine di esecuzione e si sta effettuando:
    	// 0920 - Richiesta restituzione ordine di esecuzione della pena detentiva
    	// 0921 - Revoca e contestuale restituzione ordine di esecuzione della pena detentiva
    	//
   	 	//==========================================================================
    	var esisteOE = <%= ("S".equals(eventoOE) ? "true" : "false") %>
    	if (!esisteOE
    			&& (document.LoadInserisciSospensione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=='0920'
    					|| document.LoadInserisciSospensione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=='0921')) {
      		return confirm("Ordine di esecuzione non registrato, procedere?");
    	}
  	}

  	var desktop;

  	function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  	}

  	function ListaComuni(a_formname,a_fieldname) {
    	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  	}

  	function ListaAvvocati(a_formname) {
   		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname+"&modalita=BREVE", "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
  	}

  	function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3) {
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
  	}

  	function ListaComuniTds(formname,fieldname) {
  		<%-- MEV_66: aggiunto parametro di passaggio = typename --%>
  		var codTipoSede = document.LoadInserisciSospensione.ufficioTds[document.LoadInserisciSospensione.ufficioTds.selectedIndex].value;
    	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname+"&typename="+codTipoSede, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  	}
  
  	function ListaUDS(a_formname,a_fieldname) {
  		<%-- MEV_66: aggiunto parametro di passaggio = typename --%>
  		var codTipoSede = document.LoadInserisciSospensione.ufficioMdS[document.LoadInserisciSospensione.ufficioMdS.selectedIndex].value;
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+codTipoSede, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  	}

  	function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio) {
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  	}

  	function ListaCSSA(a_formname,a_fieldname,a_field2) {
  		<%-- MEV_66: aggiunto parametro di passaggio = typename --%>
  		var codTipoSede = document.LoadInserisciSospensione.uepe[document.LoadInserisciSospensione.uepe.selectedIndex].value;
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&typename="+codTipoSede, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  	}

  	<%-- MEV_66: la funzione js 'GestioneDestinatari()' non ha più senso --%>
	// Funzione di gestione destinatari.
//   	function GestioneDestinatari() {
//   		// Posizione Giuridica
//      	var pos = document.LoadInserisciSospensione.CodPosizioneGiuridica.value;
//      	var esisteOrdineE = document.LoadInserisciSospensione.esisteOE.value;
// 	 	// Codice Motivo
<%-- 	 	var codMotivo = document.LoadInserisciSospensione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value; --%>
	
// 		// TDS-UDS nell'unica DIV Contenitore
//      	var nodesor = document.getElementById('divsor');
//      	var nodeuds = document.getElementById('divuds');
//      	var nodeConTDS = document.getElementById('divContenitore');
//      	nodeConTDS.style.top='0px';
     
//      	// Istituto di detenzione
// 		var nodeistituto = document.getElementById('divistituto');
// 		// Autorita' Esecuzione
// 		var nodeautoritaE = document.getElementById('divautoritacompetenteE');
// 		// Notifica ufficiale al condannato
// 		var nodeufficialicondannato = document.getElementById('divufficialecondannato');
// 		// GE competente
// 		var nodege = document.getElementById('divge');
// 		// UEPE
// 		var nodecssa = document.getElementById('divcssa');
// 		// Destinatario per Notifica
// 		var nodeavvocati = document.getElementById('divavvocati');
// 		// Bottone
// 		var nodebottone = document.getElementById('divbottone');
  
//   		// Contenitore Destinatario Notifica  + Bottone
//    		var nodeNotifica = document.getElementById('contenitoreNotifica');

// 		// Default ( tutti i campi nascosti tranne Notifica)
// 		nodesor.style.visibility='hidden';
// 		nodeuds.style.visibility='hidden';
// 		nodeistituto.style.visibility='hidden';
// 		nodeautoritaE.style.visibility='hidden';
// 		nodeufficialicondannato.style.visibility='hidden';
// 		nodege.style.visibility='hidden';
// 		nodecssa.style.visibility='hidden';
// 		nodeavvocati.style.visibility='visible';
// 		nodebottone.style.visibility='visible';
// 		nodeNotifica.style.visibility='visible';
		
// 		// Nessuno shift nei campi
// 		nodeConTDS.style.top='0px';
// 		nodeistituto.style.top='0px';
// 		nodeautoritaE.style.top='0px';
// 		nodeufficialicondannato.style.top='0px';
// 		nodege.style.top='0px';
// 		nodecssa.style.top='0px';
// 		nodeavvocati.style.top='0px';
// 		nodebottone.style.top='0px';
// 		nodeNotifica.style.top='0px';

// 		// Esegue test del codice Motivo
//     	if (codMotivo == '0900'
//     			|| codMotivo =='0901'
//        			|| codMotivo == '0903'
//        			|| codMotivo == '0947'
//        			|| codMotivo == '0952'
//     	  		|| codMotivo == '1020'
//     	  		|| codMotivo == '1021') {
//     		nodecssa.style.visibility='hidden';
<%--        		document.LoadInserisciSospensione.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled=true; --%>

// 			// Esegue codice Posizione Guiridica
//       		if ((pos == "10")
//       				|| (pos == "07")
// 					|| (pos == "16")
// 					|| (pos == "17")
// 					|| (pos == "46")
// 					|| (pos == "47")
// 					|| (pos == "20")
// 					|| (pos == "26")
// 					|| (pos == "30")) { //libero
//       			nodeufficialicondannato.style.visibility='visible';
// 				nodeufficialicondannato.style.top='-10%';
// 				nodeistituto.style.visibility='hidden';
// 				nodege.style.visibility='hidden';
        
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_UGCONDANNATO%>.disabled=false; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_UGCONDANNATO%>.disabled=false; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_NOTE_UGCONDANNATO%>.disabled=false; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>.disabled=true; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>.disabled=true; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_NOTE_POLIZIA%>.disabled=true; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.disabled=true; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_GE%>.disabled=true; --%>
		
// 				nodeNotifica.style.top='-30%';
//         		if (codMotivo == '0901'|| codMotivo=='0947' || codMotivo=='0952' || codMotivo=='1020' || codMotivo=='1021') {
// 					nodeautoritaE.style.visibility='visible';
// 					nodeufficialicondannato.style.visibility='visible';
// 					nodeufficialicondannato.style.top='0';
// 					nodeConTDS.style.visibility='visible';
// 		  			nodeConTDS.style.top='-5%';
// 		  			if (codMotivo=='0900' || codMotivo=='0901') {
//         				nodesor.style.visibility='visible';
//           			} else {
// 	        			nodesor.style.visibility='hidden';
//            				nodeuds.style.visibility='visible';
//           			}

<%-- 					document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>.disabled=false; --%>
<%-- 					document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>.disabled=false; --%>
<%-- 					document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_NOTE_POLIZIA%>.disabled=false; --%>
<%-- 					document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_TDS%>.disabled=false; --%>
          
//           			nodeNotifica.style.top='-15%';
//         		}
        
//         		if (esisteOrdineE == 'true') { //con ordine esecuzione
//           			if (codMotivo=='0900' || codMotivo=='0901' || codMotivo=='0947' || codMotivo=='0952' || codMotivo=='1020' || codMotivo=='1021') {
//           				nodeConTDS.style.visibility='visible';
//           				nodeConTDS.style.top='-5%';
//           				if (codMotivo=='0900' || codMotivo=='0901') {
//         					nodesor.style.visibility='visible';
//         				} else {
// 	          				nodesor.style.visibility='hidden';
//            	  				nodeuds.style.visibility='visible';
//           				}
// 						nodeautoritaE.style.visibility='visible';
// 						nodeufficialicondannato.style.visibility='visible';
// 						nodeufficialicondannato.style.top='0';
// 						nodeNotifica.style.top='-15%';
						
<%-- 						document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_TDS%>.disabled=false; --%>
<%-- 						document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>.disabled=false; --%>
<%-- 						document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>.disabled=false; --%>
<%-- 						document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_NOTE_POLIZIA%>.disabled=false; --%>
//           			}
//         		}
// 			} else { //diverso da libero
// 				nodeautoritaE.style.visibility='hidden';
// 				nodeufficialicondannato.style.visibility='hidden';
// 				nodeistituto.style.visibility='visible';
// 				nodege.style.visibility='hidden';
	
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>.disabled=true; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>.disabled=true; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_NOTE_POLIZIA%>.disabled=true; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=false; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.disabled=true; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_GE%>.disabled=true; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_UGCONDANNATO%>.disabled=true; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_UGCONDANNATO%>.disabled=true; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_NOTE_UGCONDANNATO%>.disabled=true; --%>
	
// 	     		if (codMotivo == '0900'
// 	     				|| codMotivo == '0901'
// 	        			|| codMotivo == '0947'
// 	         			|| codMotivo == '0952'
// 	        			|| codMotivo == '1020'
// 	           			|| codMotivo == '1021') {
// 	      	 		if (codMotivo == '0900' || codMotivo == '0901') {
// 	        			nodesor.style.visibility='visible';
// 	        			nodeuds.style.visibility='hidden';
// 	         		} else {
// 	         			nodesor.style.visibility='hidden';
// 	            		nodeuds.style.visibility='visible';
// 	         		}
	        
// 					nodeautoritaE.style.visibility='visible';
// 					nodeistituto.style.visibility='visible';
// 					nodeistituto.style.top='-15%';
// 					nodeConTDS.style.visibility='visible';
// 					nodeConTDS.style.top='-15%';
// 					nodeNotifica.style.top='-25%';
	   		
<%-- 			        document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>.disabled=false; --%>
<%-- 			        document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>.disabled=false; --%>
<%-- 			        document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_NOTE_POLIZIA%>.disabled=false; --%>
<%-- 			        document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_TDS%>.disabled=false; --%>
// 	      		} else {
// 			        nodesor.style.visibility='hidden';
// 			        nodeufficialicondannato.style.visibility='visible';
// 			        nodeufficialicondannato.style.top='-10%';
// 			        nodeistituto.style.visibility='visible';
// 			        nodeistituto.style.top='-10%';
// 			       	nodeNotifica.style.top='-25%';
	
<%-- 			        document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_UGCONDANNATO%>.disabled=false; --%>
<%-- 			        document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_UGCONDANNATO%>.disabled=false; --%>
<%-- 			        document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_NOTE_UGCONDANNATO%>.disabled=false; --%>
<%-- 			        document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_TDS%>.disabled=true; --%>
// 	      		}
// 	   		}
// 		} else if (codMotivo=='0902') {
// 			nodeautoritaE.style.visibility='hidden';
// 			nodeufficialicondannato.style.visibility='hidden';
// 			nodeistituto.style.visibility='hidden';
// 			nodege.style.visibility='visible';
// 			nodesor.style.visibility='hidden';
// 			nodeuds.style.visibility='hidden';
// 			nodecssa.style.visibility='hidden';
	
<%-- 			document.LoadInserisciSospensione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.disabled=false; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_GE%>.disabled=false; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_UGCONDANNATO%>.disabled=true; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_UGCONDANNATO%>.disabled=true; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_NOTE_UGCONDANNATO%>.disabled=true; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_TDS%>.disabled=true; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>.disabled=true; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>.disabled=true; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_NOTE_POLIZIA%>.disabled=true; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled=true; --%>

// 	    	if (pos == "03") { //espiazione pena
// 				nodeistituto.style.visibility='visible';
// 				nodeistituto.style.top='-30%';
// 				nodege.style.top='-40%';
// 				nodeNotifica.style.top='-40%';
	
<%-- 	      		document.LoadInserisciSospensione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=false; --%>
// 	    	} else if (pos == "13") { // affidato in prova
// 	      		nodecssa.style.visibility='visible';
<%-- 				document.LoadInserisciSospensione.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled=false; --%>
// 				nodeistituto.style.top='-30%';
// 				nodege.style.top='-30%';
// 				nodecssa.style.top='-30%';
// 				nodeNotifica.style.top='-30%';
// 	    	} else if ((pos == "02")
// 	    			|| (pos == "04")
// 					|| (pos == "15")
// 					|| (pos == "14")
// 					|| (pos == "11")
// 					|| (pos == "12")
// 					|| (pos == "13")
// 					|| (pos == "25")
// 					|| (pos == "27")
// 					|| (pos == "29")
// 					|| (pos == "49")
// 					|| (pos == "31")
// 					|| (pos == "32")
// 					|| (pos == "33")
// 					|| (pos == "34")
// 					|| (pos == "35")
// 					|| (pos == "36")
// 					|| (pos == "37")
// 					|| (pos == "38")
// 					|| (pos == "39")
// 					|| (pos == "40")
// 					|| (pos == "41")
// 					|| (pos == "42")
// 					|| (pos == "43")
// 					|| (pos == "45")) { //arresti domiciliari o misura alternativa
// 				nodeautoritaE.style.visibility='visible';
// 	       		nodeistituto.style.visibility='visible';
// 		   		nodeistituto.style.top='-15%';
// 		   		nodege.style.top='-20%';
// 		   		nodeNotifica.style.top='-25%';
	
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>.disabled=false; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>.disabled=false; --%>
<%-- 				document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_NOTE_POLIZIA%>.disabled=false; --%>
// 	     	} else {
// 	 	   		nodege.style.top='-35%';
// 		   		nodeNotifica.style.top='-40%';
// 	     	}
// 		} else if (codMotivo=='0920' || codMotivo=='0921') {
// 			nodeautoritaE.style.visibility='visible';
// 			nodeufficialicondannato.style.visibility='visible';
// 			nodeistituto.style.visibility='hidden';
// 			nodesor.style.visibility='hidden';
// 			nodege.style.visibility='hidden';
// 			nodecssa.style.visibility='hidden';
// 			nodeNotifica.style.top='-20%';
			
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>.disabled=false; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>.disabled=false; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_NOTE_POLIZIA%>.disabled=false; --%>
			
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_UGCONDANNATO%>.disabled=false; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_UGCONDANNATO%>.disabled=false; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_NOTE_UGCONDANNATO%>.disabled=false; --%>
			
<%-- 			document.LoadInserisciSospensione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.disabled=true; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_GE%>.disabled=true; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_TDS%>.disabled=true; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled=true; --%>
// 		} else if (codMotivo == '-') {
// 			// Tutti i campi Destinatari visibili
// 	     	nodesor.style.visibility='visible';
// 	   		// MEV_66: rendo visibile questa sezione (era hidden) e riposiziono i div
// 		 	nodeuds.style.visibility='visible';
// 		 	nodeuds.style.top='8%';
// 		 	nodege.style.top='8%';
// 		 	nodecssa.style.top='8%';
// 		 	nodeNotifica.style.top='9%';
// 		 	nodeistituto.style.visibility='visible';
// 		 	nodeautoritaE.style.visibility='visible';
// 		 	nodeufficialicondannato.style.visibility='visible';
// 		 	nodege.style.visibility='visible';
// 		 	nodecssa.style.visibility='visible';
// 	     	nodeavvocati.style.visibility='visible';
// 	     	nodebottone.style.visibility='visible';
	   
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>.disabled=false; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>.disabled=false; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_NOTE_POLIZIA%>.disabled=false; --%>
// 		} else if (codMotivo == '0937') { // Gestione Codice motivo 0937 
// 			nodesor.style.visibility='hidden';
// 			nodeistituto.style.visibility='hidden';
// 			nodecssa.style.visibility='hidden';
// 			nodege.style.visibility='visible';
// 			nodeautoritaE.style.visibility='visible';
// 			nodeufficialicondannato.style.visibility='visible';
// 			nodeavvocati.style.visibility='visible';
// 			nodege.style.top='-12%';
// 			nodeNotifica.style.top='-15%';
	
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>.disabled=false; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>.disabled=false; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_NOTE_POLIZIA%>.disabled=false; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_COD_UGCONDANNATO%>.disabled=false; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_UGCONDANNATO%>.disabled=false; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_NOTE_UGCONDANNATO%>.disabled=false; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.disabled=false; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_GE%>.disabled=false; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiSospensione.CAMPO_SEDE_TDS%>.disabled=true; --%>
<%-- 			document.LoadInserisciSospensione.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled=true; --%>
// 		}
// 	}

	function toggleComuneCasellario() {
		vistaLabel = (document.getElementById("labelCasellario").style.display == 'none') ? 'block' : 'none';
		document.getElementById("labelCasellario").style.display = vistaLabel;
		vistaInput = (document.getElementById("inputCasellario").style.display == 'none') ? 'block' : 'none';
		document.getElementById("inputCasellario").style.display = vistaInput;
	}
</script>
</head>

<%-- MEV_66: esclusa la funzione js onload='GestioneDestinatari();' --%>
<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
      	<a href="Javascript:window.print();">
      		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
      	</a>
      </td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Sospensione/Revoca dell'esecuzione della pena disposta dal PM</font>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciSospensione">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActInserisciSospensioneEsecPenaDispPm">
  <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%=ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA%>">
  <input type="HIDDEN" name="esisteOE" value="<%=esisteOE%>">
<%
    String lIdDecretoOrdinanza = "";
    if(flagdecretoordinanza.equals("S"))
    {
      lIdDecretoOrdinanza = "" + decretoordinanza.getIdDecretoOrdinanzaSiep();
    }
%>
    <input type="HIDDEN" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ID_DECRETO_ORDINANZA_SIEP%>" value="<%=lIdDecretoOrdinanza%>">

  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
        <font class="campo">
<%      if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
%>
          DETENUTO PER ALTRA CAUSA
<%      }
        else
        {
%>
          <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
        }
%>

<%
        if (eventoOE != null && "S".equals(eventoOE))
%>
          - EMESSO ORDINE DI ESECUZIONE
      </font>
    </td>
  </tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if( lAltraCausa.getIstitutoDetenzione()!= null)
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
           </tr>
<%
               if (lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td >
                  <td class="L" colspan=5>
                    <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                  </td>
                </tr>
<%
               }
            }
        }
        else if( lLuogoDetenzione.getIstitutoDetenzione()!= null )
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
          </tr>
<%
        }%>

   <input type="HIDDEN" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>">

<% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getAltroLuogo() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
              </td>
            </tr>
<%
          }
        }
%>
   <tr>


<%
//==============================================================================
//              Sezione per la visualizzazione della PENA
// Se non ergastolo: viene visualizzata la pena
//==============================================================================
    if(   penaresidua.getIdPenaResidua() != null
       && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
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
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}else{
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
    <tr>
<%
      if (penaresidua.getDataInizio() != null)
      {
%>
        <td class="l">Data Decorrenza Pena</td>
        <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
      }

      if ( penaresidua.getFlagErgastolo() != null)
      {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
        }
        else if(penaresidua.getFlagErgastolo().equals("D"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
        }
      }

    if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
    {
      if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
      {
        if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
        {
%>
         <td class="l">Data Fine Pena</td>
         <td class="L" colspan=2>
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
         </td>
<%
          }
          else if( penaresidua.getDataFine() != null)
          {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
%>
             <td class="l">Data Fine Pena</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>
<%
          }
          else
          {
%>
            <td class="l">Data Fine Pena</td>
            <td class="lRosso" colspan=2>
             <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
             <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
             <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
             <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>
<%        }
       }
     }
   }
%>
    <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
  </tr>
</table>

<table style="width: 90%;">
  <tr>
    <td class="l">Su istanza presentata da
      <font class=ob> (*)</font>:&nbsp;
    </td>
    <td class="l">
      <input type="radio" name="istanzapresentata" value="D" <%=difensore%>>dal difensore &nbsp;
      <input type="radio" name="istanzapresentata" value="I" <%=interessato%>>dall'interessato &nbsp;
    </td>
    <td class="l">Depositata in data <font class=ob> (*)</font>
<%
  if(lDecOrdMod != null && lDecOrdMod.getDataDepositoIstanza() != null)
  {
%>
    <input type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDecOrdMod.getDataDepositoIstanza(),"dd"))%>"  name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_DEPOSITO_ISTANZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
    <input type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDecOrdMod.getDataDepositoIstanza(),"MM"))%>"  name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_DEPOSITO_ISTANZA%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
    <input type="text" size="4" maxlength="4" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDecOrdMod.getDataDepositoIstanza(),"yyyy"))%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_DEPOSITO_ISTANZA%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
  }
  else
  {
%>
    <input type="text" size="2" maxlength="2" value="" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_DEPOSITO_ISTANZA%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
    <input type="text" size="2" maxlength="2" value="" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_DEPOSITO_ISTANZA%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
    <input type="text" size="4" maxlength="4" value="" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_DEPOSITO_ISTANZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
<%
  }
%>
  </td>
</tr>
<tr>
  <td class="l">&nbsp;</td>
  <td class="l">
    <input type="radio" name="istanzapresentata" value="U" <%=ufficio%>>di ufficio &nbsp;
  </td>
  <td class="l">&nbsp;</td>
</tr>
<tr>
  <td class="l">
    <input type="checkbox" onClick="toggleComuneCasellario()" name="<%=ICostantiDecretoOrdinanzaSiep.FOGLIO_COMPLEMENTARE%>">
    Foglio Complementare
  </td>
 	<td class="l" id="labelCasellario" style="display:none;">Casellario Giudiziale</td>
 	<td class="l" id="inputCasellario" style="display:none;">
   	<input title="Sede Casellario Giudiziale" value="<%= StringUtils.toStringJSP( lCasellario ) %>" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>"  maxlength="35" size="35">
   	<a href="Javascript:ListaUfficiComuni('LoadInserisciSospensione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>','DIB');">
     	<img src="/images/filefolder.gif" border=0>
   	</a>
 	</td>
</tr>
</table>
<table style="width: 90%;">
<tr>
	<td class="l">Contenuto Decreto <font class=ob>(*)</font></td>
    <td class="L" colspan="3">
    	<%-- MEV_66: esclusa la funzione js onChange="GestioneDestinatari();" --%>
     	<select class="small" Title="Contenuto decreto" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>">
       		<%=contenuto%>
     	</select>
  	</td>
</tr>
<tr>
  <td class="l">Motivazioni</td>
  <td class="l" colspan="3">
    <font class="campo">
      <TEXTAREA Title="Annotazioni" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MOTIVAZIONI%>" cols=80></textarea>
    </font>
  </td>
</tr>
<tr>
  <td class="l">Data sospensione/revoca</td>
  <td class="l" colspan=3>
<%
    if(lDecOrdMod != null && lDecOrdMod.getDataDepositoIstanza() != null)
    {
%>
      <input type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDecOrdMod.getDataSospensioneEsecuzione(),"dd"))%>"  name="<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"> -
      <input type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDecOrdMod.getDataSospensioneEsecuzione(),"MM"))%>"  name="<%=ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
      <input type="text" size="4" maxlength="4" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDecOrdMod.getDataSospensioneEsecuzione(),"yyyy"))%>" name="<%=ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
    }
    else
    {
%>
      <input type="text" size="2" maxlength="2" value="" name="<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>"   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > -
      <input type="text" size="2" maxlength="2" value="" name="<%=ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > -
      <input type="text" size="4" maxlength="4" value="" name="<%=ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)"  >
<%
    }
%>
  </td>
</tr>
<tr>
  <td class="l">Data Emissione</td>
<%
    if(eventonotifica!= null && eventonotifica.getEvento()!= null && eventonotifica.getEvento().getDataEmissione()!= null)
    {
%>
      <td class="L" >
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd"))%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"MM"))%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"yyyy"))%>"  type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
<%
    }
    else
    {
%>
      <td class="L" >
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"    onBlur="javascript:value=FillYear(value)">
      </td>
<%
    }
%>
    <td class="l">Data Trasmissione</td>
<%
    if(eventonotifica != null && eventonotifica.getNotifiche()!= null && eventonotifica.getNotifiche().length !=0 && eventonotifica.getNotifiche()[0] != null && eventonotifica.getNotifiche()[0].getDataInvio()!= null)
    {
%>
      <td class="L">
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd"))%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"MM"))%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
<%
    }
    else
    {
%>
      <td class="L">
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
<%
    }
%>
  </tr>
</table>
<table style="width: 100%;">
  <tr>
    <td class="Titolo" colspan="6"> Magistrato</td>
  </tr>
  <tr>
    <td class="l" width="25%">Magistrato
    <td class="L">
    <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
    <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
    <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('LoadInserisciSospensione','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  <tr>
    <td class="Titolo" colspan=6>Destinatari</td>
  </tr>
</table>
<div id="contenitore" style="width: 100%; visibility: visible; position: relative;">
<%-- MEV_66: Sulla maschera devono rimanere tutti i destinatari indipendentemente dalla posizione giuridica
	e dalla voce selezionata nel campo contenuto decreto --> visibility:hidden; --%>
<div id="divautoritacompetenteE" style="width: 100%; visibility: visible; position: relative;">
  <table style="width: 100%;">
    <tr>
      <td class="l" width=25%>Autorita' per Esecuzione/Restituzione</td>
      <td class="L" colspan="3">
        <select  Title="Autorita' per Esecuzione o Restituzione"  class="small" name="<%=ICostantiSospensione.CAMPO_COD_POLIZIA%>">
          <%=codiceAutoritaE%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="L">
<%
if (/*Sedeautorita != null && */!Sedeautorita.equals("")) {
%>
          <input title="Sede Autorita polizia" value="<%=Sedeautorita%>" type="text" name="<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>"  maxlength="35" size="35">
<%
} else {
%>
          <input title="Sede Autorita polizia"  type="text" name="<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>"  maxlength="35" size="35">
<%
}
%>
        <a href="Javascript:ListaComuni('LoadInserisciSospensione','<%=ICostantiSospensione.CAMPO_SEDE_POLIZIA%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
        <TEXTAREA title="Indirizzo" name="<%=ICostantiSospensione.CAMPO_NOTE_POLIZIA%>" cols="30"></textarea>
      </td>
    </tr>
  </table>
</div>

<div id="divufficialecondannato" style="width: 100%; visibility: visible; position: relative;">
  <table style="width: 100%;">
    <tr>
      <td class="l" width=25%>Notifica al condannato</td>
      <td class="L" colspan="3">
        <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiSospensione.CAMPO_COD_UGCONDANNATO%>">
          <%=autoritaEsternaAvv%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="L">
        <input title="Sede Autorita Esterna" value="<%=SedeUGC%>" type="text" name="<%=ICostantiSospensione.CAMPO_SEDE_UGCONDANNATO%>" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciSospensione','<%=ICostantiSospensione.CAMPO_SEDE_UGCONDANNATO%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
        <TEXTAREA title="Indirizzo" name="<%=ICostantiSospensione.CAMPO_NOTE_UGCONDANNATO%>"  cols=30 ></textarea>
      </td>
    </tr>
  </table>
</div>

<div id="divistituto" style="width: 100%; visibility: visible; position: relative;">
	<table style="width: 100%;">
		<tr>
      		<td class="l" width=25%>Istituto di Detenzione</td>
      		<td class="l">
<%
if (posizioneluogoaltra != null &&  posizioneluogoaltra.getLuogoDetenzione() != null && posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione() != null) {
%>
				<input readonly title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
          		<input type="hidden" title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
          		<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciSospensione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
            		<img src="/images/filefolder.gif" border=0>
          		</a>
<%
} else {
	if (Istituto != null && !"".equals(Istituto.getDescrTipoIstituto())) {
%>
           		<input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(Istituto.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(Istituto.getDescrComune())%>" size=50>
            	<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=Istituto.getIdIstitutoDetenzione()%>" size=35>
            	<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciSospensione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              		<img src="/images/filefolder.gif" border=0>
           		</a>
<%
	} else {
%>
            	<input readonly Title="Istituto" name="Comune" value="" size=50>
            	<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
            	<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciSospensione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              		<img src="/images/filefolder.gif" border=0>
            	</a>
<%
	}
}
%>
      		</td>
    	</tr>
	</table>
</div>
<div id="divContenitore" style="visibility: visible; position: relative; top: 0; left: 0;">
	<div id="divsor" style="width: 100%; visibility: visible; position: relative; top: 0; left: 0;">
	  	<table style="width: 100%;">
	    	<tr>
	    		<td class="L" width=25%>Tribunale di Sorveglianza</td>
	    		<%-- MEV_66: aggiunte combo per gestione tds ed uds ed uepe --%>
	      		<td class="L">
	      			<select Title="Tribunale di Sorveglianza" name="ufficioTds"><%=ufficioTdS%></select>
	      			&nbsp;Sede&nbsp;
<%
if (uffTDS != null && uffTDS.getDescrComune() != null) {
%>
          			<input title="Sede Tribunale Sorveglianza" value="<%=uffTDS.getDescrComune()%>" type="text" name="<%=ICostantiSospensione.CAMPO_SEDE_TDS%>" maxlength="35" size="35">
<%
} else {
%>
          			<input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%=ICostantiSospensione.CAMPO_SEDE_TDS%>" maxlength="35" size="35">
<%
}
%>
	        		<a href="Javascript:ListaComuniTds('LoadInserisciSospensione','<%=ICostantiSospensione.CAMPO_SEDE_TDS%>');">
	          			<img src="/images/filefolder.gif" border="0">
	        		</a>
	      		</td>
	    	</tr>
		</table>
	</div>

	<div id="divuds" style="width: 100%; visibility: visible; position: relative; top: 0; left: 0;">
	  	<table style="width: 100%;">
	    	<tr>
	     		<td class="L" width=25%>Magistrato di Sorveglianza</td>
	      		<td class="L">
	      			<select title="Magistrato di Sorveglianza" name="ufficioMdS"><%=ufficioMdS%></select>
	      			&nbsp;Sede&nbsp;
	          		<input title="Sede Magistrato Sorveglianza" name="<%=ICostantiSospensione.CAMPO_SEDE_UDS%>" maxlength="35" size="35" type="text" value="">
	          		<a href="Javascript:ListaUDS('LoadInserisciSospensione','<%=ICostantiSospensione.CAMPO_SEDE_UDS%>');">
	            		<img src="/images/filefolder.gif" border="0">
	          		</a>
	       		</td>
    		</tr>
	  	</table>
	</div>
</div>

<div id="divge" style="width: 100%; visibility: visible; position: relative;">
  	<table style="width: 100%;">
  		<tr>
    		<td class="l" width=25%>GE Competente</td>
    		<td class="l">
      			<select Title="Autorita' emittente" name="<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>">
      				<option value="-">-
<%
Iterator lIter = lColAutEmi.iterator();
while (lIter.hasNext()) {
	DecodificheModel lDecMod = (DecodificheModel)lIter.next();
%>
					<option value="<%=lDecMod.getCodiceAlternativo()%>"/><%=lDecMod.getDescription()%>
<%
}
%>
     			</select>
      			&nbsp;Sede&nbsp;
      			<input title="Sede Autorita emittente" type="text" value="<%=SedeGE%>" name="<%=ICostantiSospensione.CAMPO_SEDE_GE%>" maxlength="35" size="35">
      			<a href="Javascript:ListaUfficiComuni('LoadInserisciSospensione','<%=ICostantiSospensione.CAMPO_SEDE_GE%>',document.LoadInserisciSospensione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>[document.LoadInserisciSospensione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.options.selectedIndex].value);">
        			<img src="/images/filefolder.gif" border=0>
      			</a>
    		</td>
  		</tr>
	</table>
</div>

<div id="divcssa" style="width: 100%; visibility: visible; position: relative;">
  	<table style="width: 100%;">
    	<tr>
      		<td class="l"width=25%>UEPE</td>
      		<td class="l">
      			<select title="UEPE" name="uepe"><%=uepe%></select>
      			&nbsp;Sede&nbsp;
      			<%-- <%=StringUtils.toStringJSP(Cssa.getComune())%>-<%=StringUtils.toStringJSP(Cssa.getIndirizzo())%> --%>
        		<input readonly title="UEPE Competente" name="Indirizzo" value="" maxlength="35" size="35">
        		<input type="hidden" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="<%=Cssa.getIdCSSA()%>">
        		<a href="Javascript:ListaCSSA('LoadInserisciSospensione','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
          			<img src="/images/filefolder.gif" border=0>
        		</a>
      		</td>
    	</tr>
 	</table>
</div>

<div id="contenitoreNotifica" style="width: 100%; visibility:visible; position:relative;">
	<div id="divavvocati" style="width: 100%; visibility:visible; position:relative;">
  		<table width='100%'>
    		<tr>
      			<td class="Titolo" colspan="6">Destinatario per Notifica</td>
    		</tr>
<%
int lIdxAvv = 0;
Iterator lItxAvv = avvocati.iterator();
while (lItxAvv.hasNext()) {
	AvvocatoSiepModel lAvv = (AvvocatoSiepModel) lItxAvv.next();
%>
  		</table>
  		<table>
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
         			<input type="HIDDEN" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>">
      			</td>
    		</tr>
  		</table>
  		<table>
    		<tr>
				<td class="l">Autorita' Destinazione</td>
		      	<td class="L">
        			<select title="Autorita Esterna" class="small" name="<%=ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF%>">
          				<%=autoritaEsternaAvv%>
        			</select>
      			</td>
      			<td rowspan=2 class="l">Note</td>
      			<td rowspan=2 class="L">
        			<textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols="50" rows="4"></textarea>
      			</td>
    		</tr>
   			<tr>
      			<td class="l">Sede </td>
      			<td class="L">
        			<input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF%>" maxlength="35" size="35">
        			<a href="Javascript:ListaComuni('LoadInserisciSospensione','<%=ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF %>[<%=lIdxAvv%>]');">
          				<img src="/images/filefolder.gif" border=0>
        			</a>
      			</td>
    		</tr>
    		<tr><td>&nbsp;</td>
<%
	lIdxAvv++;
}
%>
 			</tr>
		</table>
	</div>

	<div id="divbottone" style="width: 100%; visibility:visible; position:relative;">
  		<table style="width: 100%;">
    		<tr>
      			<td class="lNoBord" colspan="2">
        			<br><INPUT class="bottone" type="submit" name="I" value="Conferma" onclick="javascript:return Verifica();">
     			</td>
    		</tr>
  		</table>
	</div>
</div>
</div>
</form>
<script language="JavaScript" type="text/javascript">
	var frmvalidator  = new Validator("LoadInserisciSospensione");

	frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto e' obbligatorio");
	frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
	
	frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto e' obbligatorio");
	frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
	
	frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto e' obbligatorio");
	frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");
  
<%
if ((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S"))) {
	if ((penaresidua.getFlagErgastolo() == null)
			|| (penaresidua.getFlagErgastolo() != null
				&& !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) {
		if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
			frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena e' obbligatorio");
			frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");
			
			frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena e' obbligatorio");
			frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");
			
			frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena e' obbligatorio");
			frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
			frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
			frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2099");
<%
		}
	}
}
%>
</script>
</body>
</html>
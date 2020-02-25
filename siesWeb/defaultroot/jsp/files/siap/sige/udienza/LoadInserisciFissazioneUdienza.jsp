<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="f3b.web.RedirectTo"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="f3b.web.html.Option"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<%@ page import="siap.sige.magistratoassegnatario.action.ICostantiMagistratoAssegnatario"%>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>
<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel"%>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocatoFascicoloSige"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel"%>
<%@ page import="siap.sige.udienzaparti.action.ICostantiPartiUdienza"%>
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.aula.model.AulaUdienzaModel"%>
<%@ page import="siap.sige.aula.action.ICostantiAula"%>

<jsp:useBean id="tipoAutoritaSogg" scope="request"
	class="java.lang.String" />
<jsp:useBean id="tipoAutoritaAltro" scope="request"
	class="java.lang.String" />
<jsp:useBean id="TipiIstitutiColl" scope="request"
	class="java.lang.Object" />
<jsp:useBean id="luogodet" scope="request"
	class="siap.sige.detenzione.model.FasSigeDetenzioneModel" />
<jsp:useBean id="tipoGiudizio" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoGiudizioVal" scope="request"
	class="java.lang.String" />
<jsp:useBean id="avvocato" scope="request" class="java.util.Vector" />
<jsp:useBean id="UdienzaPreFissata" scope="request"
	class="siap.sige.udienza.model.UdienzaSigeModel" />
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />
<jsp:useBean id="TipoDest" scope="request" class="java.lang.String" />
<jsp:useBean id="tenori" scope="session" class="java.util.Vector" />
<jsp:useBean id="UdienzaSige" scope="request"
	class="siap.sige.udienza.model.UdienzaSigeModel" />
<jsp:useBean id="FascicoloSigeEsteso" scope="session"
	class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="aulaUdienza" scope="request"
	class="siap.sige.aula.model.AulaUdienzaModel" />
<jsp:useBean id="magistratoassegnatario" scope="request"
	class="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel" />

<jsp:useBean id="notificaComunicazione" scope="request"
	class="java.lang.String" />
<jsp:useBean id="vectNotAvv" scope="request" class="java.util.Vector" />
<jsp:useBean id="notSogg" scope="request"
	class="siap.siep.notifica.model.NotificaModel" />
<jsp:useBean id="notAltro" scope="request"
	class="siap.siep.notifica.model.NotificaModel" />

<jsp:useBean id="IdEvento" scope="request" class="java.lang.String" />
<jsp:useBean id="IdUdienzaSige" scope="request" class="java.lang.String" />
<jsp:useBean id="IdUdienzaProcedimentoSige" scope="request"
	class="java.lang.String" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="elencoSezioniUdienza" scope="request"
	class="java.lang.String" />
<jsp:useBean id="eventoNotifica" scope="request"
	class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="provvedimentoSige" scope="request"
	class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />
<jsp:useBean id="indirizzoUfficio" scope="request"
	class="java.lang.String" />
<%-- 20171004: [SG] aggiunto useBean --%>
<jsp:useBean id="ufficioConSezioni" scope="request" class="java.lang.String" />
<jsp:useBean id="provEsitoImpugnaz"       scope="request" class="java.lang.String"/>

<%
	String lAzione = "siap.sige.udienza.action.ActInserisciFissazioneUdienza";
	
	AulaUdienzaModel lAulaUdienza = new AulaUdienzaModel();
	if (aulaUdienza!=null){
		lAulaUdienza = aulaUdienza;
	}

	EventoNotificaModel lEventoNotifica = new EventoNotificaModel();
	if (eventoNotifica!=null){
		lEventoNotifica = eventoNotifica;
	}
	
	ProvvedimentoSigeEventoModel lProvvedimentoSigeEvento = new ProvvedimentoSigeEventoModel();
	if (provvedimentoSige!=null){
		lProvvedimentoSigeEvento = provvedimentoSige;
	}
	
	UdienzaSigeModel lUdienzaSige = new UdienzaSigeModel();
	if( UdienzaPreFissata.getIdUdienzaSige() != null  ) {
		lUdienzaSige = UdienzaPreFissata;
	} else if( UdienzaSige.getIdUdienzaSige() != null ) {
		lUdienzaSige = UdienzaSige;
	}

	boolean isNotPrefissata = true;
	String oldIdUdienza = "";
	if (UdienzaPreFissata.getIdUdienzaSige() != null) {
		oldIdUdienza = UdienzaPreFissata.getIdUdienzaSige().toString();
		isNotPrefissata = false;
	}

	// Link alla Gestione Oggetti 
	RedirectTo lRedir = new RedirectTo();
	lRedir.setPage(IWebConstants.PG_MAIN);
	lRedir.setAction("siap.sige.tenore.action.ActLoadDettaglioOggetti");
	lRedir.setParameter("TornaQui", TornaQui);
	String lLinkOggettiSessione = lRedir.toString();

	//
	// Preparazione Link Gestione udienze
	//
	String actUdiMono = "siap.sige.udienzamonocratica.action.ActLoadInserisciUdienzaMonocraticaSige";
	String actUdiColle = "siap.sige.udienzacollegiale.action.ActLoadInserisciUdienzaCollegiale";
	String idUdiSige = "";
	boolean udienzaSigeDefinita = false;
	if( lUdienzaSige.getIdUdienzaSige() != null  ) {
		actUdiMono = "siap.sige.udienzamonocratica.action.ActLoadDettaglioUdienzaMonocraticaSige";
		actUdiColle = "siap.sige.udienzacollegiale.action.ActLoadDettaglioUdienzaCollegiale";
		idUdiSige = lUdienzaSige.getIdUdienzaSige().toString();
		udienzaSigeDefinita = true;
	}

	// Monocratica 
	lRedir.setAction(actUdiMono);
	lRedir.setParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE, idUdiSige);
	lRedir.setParameter(ICostantiCollegio.FORM_DEF_COLLEGIO, "yes");
	// [EC] intervento per versione 11.2.1
	lRedir.setParameter("IdEvento", IdEvento);
	lRedir.setParameter("TornaQui", TornaQui);
	String lLinkUdienzaMonocratica = lRedir.toString();

	// Collegiale
	RedirectTo lRedir2 = new RedirectTo();
	lRedir2.setAction(actUdiColle);
	lRedir2.setParameter(ICostantiCollegio.FORM_DEF_COLLEGIO, "yes");	
	// [EC] intervento per versione 11.2.1
	// 20190518 [SG]: aggiunta impostazione proprieta' + lRedir2 invece che lRedir su idEvento
	lRedir2.setParameter("IdEvento", IdEvento);
	lRedir2.setParameter("TornaQui", TornaQui);
	String lLinkUdienzaCollegiale = lRedir2.toString();

	//Fascicolo SIGE in sessione
	FascicoloSigeModel lFascicolo = FascicoloSigeEsteso.getFascicoloSige();
	
	String lDataIscrizione = "";
	if (FascicoloSigeEsteso.getFascicoloSige().getDataIscrizione() != null){
		lDataIscrizione = (DateUtils.getDateToString (FascicoloSigeEsteso.getFascicoloSige().getDataIscrizione(), "dd/MM/yyyy"));
	}

	lRedir.setAction("siap.sige.udienzaparti.action.ActLoadVisualizzaParti&codTipoParte=O&IdEvento="+IdEvento+"&IdUdienzaSige="+IdUdienzaSige+"&IdUdienzaProcedimentoSige="+IdUdienzaProcedimentoSige+"&ChiaveAnno=" + lFascicolo.getChiaveAnno() + "&ChiaveProgr=" + lFascicolo.getChiaveProgr());
	lRedir.setParameter("TornaQui", TornaQui);
	String lLinkParteOffesa = lRedir.toString();

	lRedir.setAction("siap.sige.udienzaparti.action.ActLoadVisualizzaParti&codTipoParte=C&IdEvento="+IdEvento+"&IdUdienzaSige="+IdUdienzaSige+"&IdUdienzaProcedimentoSige="+IdUdienzaProcedimentoSige+"&ChiaveAnno=" + lFascicolo.getChiaveAnno() + "&ChiaveProgr=" + lFascicolo.getChiaveProgr());
	lRedir.setParameter("TornaQui", TornaQui);
	String lLinkParteCivile = lRedir.toString();

	java.util.Date myDate = null;
	if (lEventoNotifica.getEvento()!=null){
		myDate = lEventoNotifica.getEvento().getDataEmissione();
	}
	
	String titolo = "";
	if ("R".equals(modalita)) {
		titolo = "Modifica Fissazione Udienza";
	} else {
		titolo = "Inserimento Fissazione Udienza";
	}
	
	
	// sies 11.2.1  porto il seguente script a livello di pagina
	String myLuogo = "";
	String myFlagCheck = "";
	if (lProvvedimentoSigeEvento.getProvvedimento()!=null){
	 	myLuogo = lProvvedimentoSigeEvento.getProvvedimento().getLuogoSvolgimento();	
	 	String myFlag = lProvvedimentoSigeEvento.getProvvedimento().getFlagOrdineTraduzione();
	 	if ("S".equals(myFlag)){
	 		myFlagCheck = "checked";
	 	}
	} else {
	 	myLuogo = indirizzoUfficio;
	}
	// 20170907: [SG] ottimizzazione per scrittura a video (se fosse null)
	myLuogo = StringUtils.toStringJSP(myLuogo);
	 
%>

<html>
<head>
<title>[S.I.E.S.] - Inserisci Fissazione Udienza</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
    var desktop;

    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }

    function ListaUffici(a_formname,a_fieldname)    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }

    function ListaComuni(a_formname,a_fieldname)    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

	function ListaAule(a_formname, a_fieldval) {
		if ( a_fieldval == "" || a_fieldval == "-" ) {
			alert('Selezionare una sezione!');
	    } else {
			desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.aula.action.ActRicercaAula&<%=IWebConstants.POPUP_PAGE%>=yes&formname="+a_formname+"&<%=ICostantiAula.CAMPO_ID_SEZIONE%>="+a_fieldval, "Ricerca_Aule", "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
	    }
	}

    function calendario(a_formname,a_field_year,a_field_month,a_field_day)    {
      desktop = window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }

    function cleanLuogoSvolgimento()  {
		document.LoadInserisciFissazioneUdienza.<%=ICostantiProvvedimentoSige.CAMPO_LUOGO_SVOLGIMENTO%>.value = "";
    }

	function checkSNT(idEle) {
	  	var flag = document.getElementById("flagSNT_"+idEle);
	  	var select = document.getElementById("<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>_"+idEle);
	  	var sede = document.getElementById("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>_"+idEle);
	  	var autRow = document.getElementById("AutDestRow_"+idEle);
	  	var sedeRow = document.getElementById("SedeDestRow_"+idEle);
	  	if (flag.checked) {
	  		// SNT on
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
      	var data_iscrizione = '<%=lDataIscrizione%>';
      	// Controllo obbligatorietà Tipo Giudizio.
      	if ( document.LoadInserisciFissazioneUdienza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value == "-"
        		|| document.LoadInserisciFissazioneUdienza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value == "" )    {
        	alert('Inserire il Tipo Rito');
        	return false;
      	}
      
    	// Controllo obbligatorietà presenza almeno un Oggetto.
      	var numOggetti=<%=tenori.size()%>;
      	if (numOggetti ==0)    {
          	alert("Occorre individuare almeno un oggetto del Procedimento SIGE!");
          	return false;
      	}
      
      	if (document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          	document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;

      	if (document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          	document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
          
      	var dataSistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
      	var dataEmissione=document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value +'/'+
				document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value +'/'+
				document.LoadInserisciFissazioneUdienza.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
          
     	if (dataEmissione.length>2) {
          	if (! ControllaData(dataEmissione)) {
              	alert('Data emissione non valida!');
        	  	return false;
          	}
          	if (!CompareDate( dataEmissione, dataSistema)) {
              	alert('Data Emissione maggiore della Data di sistema!');
        	  	return false;
          	}
          	if (!CompareDate( data_iscrizione, dataEmissione)) {
             	alert('Data Emissione minore della Data di iscrizione!');
        	  	return false;
          	}
      	}
      
      	if (document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value.length==1)
          	document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value='0'+document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value;

      	if (document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value.length==1)
          	document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value='0'+document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value;

      	// Controllo validità data Udienza.
      	var dataUdienza= document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+
				document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value +'/'+
				document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value;

      	if (dataUdienza.length>2) {
          	if (! ControllaData(dataUdienza)) {
              	alert('Data Udienza non valida!');
              	return false;
          	}    
      	}

      	if (dataUdienza != '//' && !CompareDate( data_iscrizione, dataUdienza )) {
			// Controllo data Emissione >= Data Iscrizione (se non è presente una data udienza).
    	    alert('Data Udienza minore della Data di Iscrizione!');
    	    return false;
      	}
      
      	// Controllo solo se non esiste prefissata
      	if (<%=isNotPrefissata%>) {
          	// Controllo cambiamento Udienza
          	var IdUdienzaOld = "<%=oldIdUdienza%>";
          	var IdUdienza = document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>.value;
          	if ( (IdUdienzaOld != "") && (IdUdienzaOld == IdUdienza)) {
              	alert('Non è possibile rifissare la stessa udienza!');
              	return false;
          	}
      	}

      	// 20171003: [SG] aggiunto controllo sulla sezione
      	if (document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>.value == ""
      			&& dataUdienza != "//"
      			&& document.LoadInserisciFissazioneUdienza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value == 'C'
      			&& <%=ufficioConSezioni%>) {
<%--       			&& document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>.value == "-") { --%>
			alert("Attenzione prima di confermare occorre inserire l'udienza\nda apposita funzione (Inserimento Udienza - Visualizza)");
			return false;
	    }
	  	return true;
  	}

    // Chiamata funzione elenco Udienze.
    function ListaUdienze( aNomeForm, aNomeCampoGG, aNomeCampoMM, aNomeCampoAA, aNomeCampoLuogo, aNomeCampoIdUdienza, aNomeCampoCollegio, aTipoRito) {
    	var tipoRito = document.LoadInserisciFissazioneUdienza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value;
    	if (tipoRito == '-') {
      		alert('Valorizzare il Tipo Rito!');
		} else {
	      	// Compone il link URL per passare i parametri alla ElencoUdienza.JSP
	      	var lLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.udienza.action.ActLoadRicercaUdienzePerProcedimento";
	          	lLink += "&formname="+ aNomeForm;
	          	lLink += "&campoGG=" + aNomeCampoGG;
	          	lLink += "&campoMM=" + aNomeCampoMM;
	          	lLink += "&campoAA=" + aNomeCampoAA;
	          	lLink += "&campoLuogo=" + aNomeCampoLuogo;
	          	lLink += "&campoID=" + aNomeCampoIdUdienza;
	          	lLink += "&campoColl=" + aNomeCampoCollegio;
	          	lLink += "&tipoRito=" + aTipoRito;
			desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=800,height=550" );
		}
	}

    //
    // Funzione JS per chiamta azione inserimento udienza
    //   
	function VerificaChiamataInserisciUdienza(){
        
    	// Controllo validità data Udienza.
	    if (document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value.length==1)
	        document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value='0'+document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value;

	    if (document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value.length==1)
	        document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value='0'+document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value;
    	
	    var dataUdienza= document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+
	                     document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value +'/'+
	                     document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value;
	    
	    // [EC] 20171019 : recuero la sezione e se specificata la passo alla funzione InserisciUdienza
	    var idSezione = '';
	    if(document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>.value != '-'){
	    	idSezione = document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>.value;
	    }	    				

	    if (dataUdienza.length>2) {
	    	if (! ControllaData(dataUdienza)) {
	    		alert('Data Udienza non valida!');
	    	} else {
	    		// ok la data è corretta
		        InserisciUdienza(document.LoadInserisciFissazioneUdienza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value, dataUdienza, idSezione);
	    	}
	    } else {
	    	// la data non è stata inserita
	        InserisciUdienza(document.LoadInserisciFissazioneUdienza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value, '', idSezione);
	    }

    }
    
	function InserisciUdienza( tipoRito, dataUdienza, idSezione){
		var lLink;
		if( tipoRito == 'M' ){
			lLink = "<%=lLinkUdienzaMonocratica%>&DataUdienza="+dataUdienza + "&<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>="+idSezione;
			desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=750,height=500" );
		} else if( tipoRito == 'C' ){
			var idUdienza=document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>.value;
			lLink = "<%=lLinkUdienzaCollegiale%>&from=jsp&PopUp=Y&DataUdienza=" + dataUdienza + "&<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>="+idUdienza + "&<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>="+idSezione;
			desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=750,height=500" );
		} else {
			alert("Definire il tipo rito");
			return;
		}
		//alert (lLink);
		//window.location=lLink;
	}

    //
    // Funzione JS per chiamta azione inserimento parti offese/civili
    //   
	function InserisciParte( tipoParte ){
		var lLink;
		if( tipoParte == 'O' ){
			lLink = "<%=lLinkParteOffesa%>";
		} else {
			lLink = "<%=lLinkParteCivile%>";
		}		 
		window.location=lLink;	
	}

	<%-- 20171005: [SG] aggiunto parametro in più --%>
	<%-- intervento per 11.2.1 aggiunti i parametri codMagistrato, idAula, descrAula,ingresso, piano,oraInizio,minutoInizio,oraFine,minutoFine, luogoUdi --%>
/*     function setUdienza(idUdienza, dataUdienza, idCollegio, idSezione, codMagistrato, idAula, descrAula, ingresso, piano, oraInizio, minutoInizio, oraFine, minutoFine, luogoUdi) {*/    	 
	function setUdienza(idUdienza, dataUdienza, idCollegio, idSezione, codMagistrato) {    
		// 20190517 [SG]: aggiunto controllo preventivo se dalla popup torno indietro senza aver inserito
		if (dataUdienza != "null") {
    	var dataSplitted = dataUdienza.split("-");
    	document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value = dataSplitted[0];
    	document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value = dataSplitted[1];
    	document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value = dataSplitted[2];
		}
		document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>.value = idUdienza;
    	document.LoadInserisciFissazioneUdienza.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>.value = idCollegio;
    	var idSezioneSelected = document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>.value;
    	if (idSezione != null) {
    		document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>.value = idSezione;
    		if (idSezione != null && idSezione != idSezioneSelected) {
    		// svuoto i campi aula, ingresso e piano
    		document.LoadInserisciFissazioneUdienza.<%=ICostantiAula.CAMPO_DESCRIZIONE_AULA%>.value = "";
    		document.LoadInserisciFissazioneUdienza.<%=ICostantiAula.CAMPO_DESCRIZIONE_INGRESSO%>.value = "";
    		document.LoadInserisciFissazioneUdienza.<%=ICostantiAula.CAMPO_NUMERO_PIANO%>.value = "";
    	}
    }
    	<%-- intervento per 11.2.1 --%>
    	if (codMagistrato != null) {
    		document.LoadInserisciFissazioneUdienza.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value = codMagistrato;    		
    	}
    	// 20190519 [SG]: aggiunta gestione idUdienzaSige
		if (idUdienza != null) {
			document.LoadInserisciFissazioneUdienza.idUdiSig.value = idUdienza;
		}
    	
    	<%-- if (idAula != null) {
    		document.LoadInserisciFissazioneUdienza.<%=ICostantiAula.CAMPO_ID_AULA%>.value = idAula;    		
    	}
    	if (descrAula != null) {
    		document.LoadInserisciFissazioneUdienza.<%=ICostantiAula.CAMPO_DESCRIZIONE_AULA%>.value = descrAula;    		
    	}
    	if (ingresso != null) {
    		document.LoadInserisciFissazioneUdienza.<%=ICostantiAula.CAMPO_DESCRIZIONE_INGRESSO%>.value = ingresso;    		
    	}
    	if (piano != null) {
    		document.LoadInserisciFissazioneUdienza.<%=ICostantiAula.CAMPO_NUMERO_PIANO%>.value = piano;    		
    	}
    	if (oraInizio != null) {
    		document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_ORA_INIZIO%>.value = oraInizio;    		
    	}
    	if (minutoInizio != null) {
    		document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_MIN_INIZIO%>.value = minutoInizio;    		
    	}
    	if (oraFine != null) {
    		document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_ORA_FINE%>.value = oraFine;    		
    	}
    	if (minutoFine != null) {
    		document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_MIN_FINE%>.value = minutoFine;    		
    	}
    	if (luogoUdi != null) {
    		document.LoadInserisciFissazioneUdienza.<%=ICostantiProvvedimentoSige.CAMPO_LUOGO_SVOLGIMENTO%>.value = luogoUdi;    		
    	} --%>
    }

	<%-- 20190519 [SG]: aggiunta funzione --%>
	function gestioneOggetti() {
    	var lLink;
		var idUdienza = document.LoadInserisciFissazioneUdienza.idUdiSig.value;
		if (idUdienza == undefined)
			idUdienza = "";
		lLink = "<%=lLinkOggettiSessione%>&idUdiSig=" + idUdienza;
		window.location = lLink;
	}
    </script>
</head>

<body class="corpo" onload="Javascript:initFlagSNT();">

	<table>
		<tr>
			<td class="LBG"><a href="Javascript:window.print();"><img
					src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
					alt="Stampa questa videata" border=0></a></td>
			<td class="LBG"><font class="label"> Funzione :</font>&nbsp;<font
				class="campo"><%=titolo%></font></td>

			<% if (provEsitoImpugnaz != null && provEsitoImpugnaz.equals("EsitoImpugnazione")){ %>
	        <!-- BOTTONE DI RITORNO -->
	        <td class="LBG">
	          <a href="javascript:history.go(-1);">
	            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
	          </a>
	        </td>
		<% } else { %>
			<!-- BOTTONE DI RITORNO -->
			<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
		<% } %>
		</tr>

		<tr>
			<jsp:include
				page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>" />
		</tr>
    </table>

		<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"
			name="LoadInserisciFissazioneUdienza">
			
			<input type="hidden" name="from" value="jsp"/> 
			
			<% if (lUdienzaSige.getCollegio() != null && lUdienzaSige.getCollegio().getIdCollegio() != null) {
			%>	
				<input type="hidden" name="<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>" value="<%=StringUtils.toStringJSP( lUdienzaSige.getCollegio().getIdCollegio(), "" )%>"/>
			<%
				} else{
			%>
			<input type="hidden" name="<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>">
			<%
				} 
			%>

			<jsp:include
				page="<%=ICostantiMagistratoAssegnatario.PG_SINTESIMAGISTRATOASSEGNATARIO%>">
				<jsp:param name="MagAssRitorno"
					value="siap.sige.provvedimento.action.ActLoadEmissioneOrdinanza" />
			</jsp:include>

			<jsp:include
				page="<%=ICostantiAvvocatoFascicoloSige.PG_INCLUDE_AVVOCATI%>">
				<jsp:param name="AvvRitorno"
					value="siap.sige.provvedimento.action.ActLoadEmissioneOrdinanza" />
			</jsp:include>

			<%
				if ("R".equals(modalita)) {
			%>
			<table cellspacing=2 cellpadding=2 style="width: 95%;">
				<tr>
					<td class="Titolo" colspan=6>PARTI CIVILI</td>
				</tr>
				<jsp:include
					page="<%=ICostantiPartiUdienza.PG_LOAD_VISUALSUMMARY_PARTI_UDIENZA%>">
					<jsp:param name="tipoParte" value="C" />
				</jsp:include>
				<tr>
					<td class="label"><a class="cliccabile"
						href="Javascript:InserisciParte('C');">Gestione Parti Civili</a></td>
				</tr>
			</table>

			<table cellspacing=2 cellpadding=2 style="width: 95%;">
				<tr>
					<td class="Titolo" colspan=6>PARTI OFFESE</td>
				</tr>
				<jsp:include
					page="<%=ICostantiPartiUdienza.PG_LOAD_VISUALSUMMARY_PARTI_UDIENZA%>">
					<jsp:param name="tipoParte" value="O" />
				</jsp:include>
				<tr>
					<td class="label"><a class="cliccabile"
						href="Javascript:InserisciParte('O');">Gestione Parti Offese</a></td>
				</tr>
			</table>
			<%
				}
			%>

			<table cellspacing=2 cellpadding=2 style="width: 100%;">

				<tr>
					<td class="label">Tipo Rito <font class="ob">(*)</font>
						&nbsp;&nbsp;&nbsp;&nbsp; <%
 	if (udienzaSigeDefinita) {
 %> <select title="Tipo Rito"
						name="<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>_CBX"
						disabled="disabled">
							<%=tipoGiudizio%>
					</select> <input type="HIDDEN"
						name="<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>"
						value="<%=tipoGiudizioVal%>"> <%
 	} else {
 %> <select title="Tipo Rito"
						name="<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>">
							<%=tipoGiudizio%>
					</select> <%
 	}
 %>
					</td>
				</tr>

			</table>

			<table cellspacing=2 cellpadding=2 style="width: 100%;">
				<tr>
					<td class="Titolo" colspan="4">Dati Fissazione Udienza</td>
				</tr>
				<tr>
					<td class="l">Data Emissione Decreto</td>
					<td class="L" colspan="3"><input type="text"
						name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>"
						value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(myDate,"dd"))%>"
						size="2" maxlength="2" onFocus="javascript:textboxSelect(this)"
						onkeypress="return TicTabNumField(this,event)"
						onBlur="javascript:value=FillDM(value)"> / <input
						type="text"
						name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>"
						value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(myDate,"MM"))%>"
						size="2" maxlength="2" onFocus="javascript:textboxSelect(this)"
						onkeypress="return TicTabNumField(this,event)"
						onBlur="javascript:value=FillDM(value)"> / <input
						type="text"
						name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>"
						value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(myDate,"yyyy"))%>"
						size="4" maxlength="4" onFocus="javascript:textboxSelect(this)"
						onkeypress="return TicTabNumField(this,event)"
						onBlur="javascript:value=FillYear(value)"> <!-- MEV 15 - Revisione SIGE -->
						<a
						href="javascript:calendario('LoadInserisciFissazioneUdienza','<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>');">
							<img src="/images/calendario.gif" border=0>
					</a></td>
				</tr>

				<!-- Sezione Oggetti -->
				<tr>
					<td class="L">
  		  				<table cellspacing=1 cellpadding=1 style="width: 100%; border: 0;">
							<tr>
								<td class="label" width=15% colspan=2>
									<%-- 20190519 [SG]: aggiunta gestione idUdienzaSige --%>
<%-- 									<a class="cliccabile" href="<%=lLinkOggettiSessione%>">Oggetti</a> --%>
									<a class="cliccabile" href="javascript:gestioneOggetti();">Oggetti</a>
								</td>
							</tr>
						</table>
					</td>

					<td class="L" colspan="3">
  						<div id="elenco1" style="width: 100%; display:block">
							<jsp:include page="/jsp/files/siap/sige/tenore/ElencoTenoriSige.jsp" />
						</div>
					</td>
				</tr>

				<tr>
					<td>&nbsp;</td>
				</tr>

				<tr>
					<td class="Titolo" colspan="4">Udienza</td>
				</tr>
				<tr>
					<%-- 20170914: [SG] aggiunta finta obbligatorietà e parola visualizza --%>
					<td class="l">Data Udienza <font class="ob">(*)</font></td>
					<td class="l" colspan="3"><input
						value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lUdienzaSige.getDataUdienza(),"dd"))%>"
						type="text"
						name="<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>"
						onFocus="javascript:textboxSelect(this)"
						onkeypress="return TicTabNumField(this,event)"
						onBlur="javascript:value=FillDM(value)" maxlength="2" size="2">
						/ <input
						value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lUdienzaSige.getDataUdienza(),"MM"))%>"
						type="text"
						name="<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>"
						onFocus="javascript:textboxSelect(this)"
						onkeypress="return TicTabNumField(this,event)"
						onBlur="javascript:value=FillDM(value)" maxlength="2" size="2">
						/ <input
						value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lUdienzaSige.getDataUdienza(),"yyyy"))%>"
						type="text"
						name="<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>"
						onFocus="javascript:textboxSelect(this)"
						onkeypress="return TicTabNumField(this,event)"
						onBlur="javascript:value=FillDM(value)" maxlength="4" size="4">
						&nbsp; <a class="cliccabile"
						href="Javascript:VerificaChiamataInserisciUdienza();">Inserimento
							Udienza - Visualizza</a></td>
				</tr>
	</table>


		<table>				
				<tr>
					<td class="l">Note</td>
					<%
						String myNote = "";
					if( lEventoNotifica.getCampoNote() != null && lEventoNotifica.getCampoNote().length > 0 ){
						myNote = lEventoNotifica.getCampoNote()[0].getDescr();
					}
					%>
					<td class="l" colspan="3"><TEXTAREA title="Note"
							name="<%=ICostantiProvvedimentoSige.CAMPO_AGGIUNTIVO%>" cols=95
							rows=2><%=StringUtils.toStringJSP( myNote )%></textarea></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<%
					HashSet<String> hs = new HashSet<String>();
				hs.add("01");
				hs.add("03");
				hs.add("22");
				hs.add("24");
				hs.add("50");
				if( hs.contains(lFascicolo.getCodPosizioneGiuridica()) ){
				%>
				<tr>
					<td class="l" colspan="4">Ordina la traduzione del condannato
						&nbsp; <input type="checkbox"
						name="<%=ICostantiProvvedimentoSige.CAMPO_TRADUZIONE%>"
						<%=myFlagCheck%>>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<%
					}
				%>
				<tr>
					<td>&nbsp;</td>
				</tr>
			</table>

			<script>
function effettoTree(){
	node=document.getElementById("frameDestina");
	node.style.display = (node.style.display == "none")? "block" : "none";
	document.images["imageDestina"].src = (node.style.display == "none")? "/images/expand.gif" : "/images/collapse.gif";
	return false;
}
</script>

			<table style="width: 100%;" cellpadding="2" cellspacing="2">
				<tr>
					<td class="Titolo">
						<div align="left">
							<a><img name="imageDestina" src="/images/expand.gif"
								onClick="return effettoTree();" alt="" border=0></a>Destinatari
						</div>
					</td>
				</tr>

				<tr id="frameDestina" style="display: none">
					<td>
						<table>

							<tr>
								<td class="l" colspan=6>Per la comunicazione al <%=TipoDest%>
									<input type="checkbox"
									name="<%=ICostantiUdienzaSige.CAMPO_PROCURA_GENERALE%>"
									<%=notificaComunicazione%>>
								</td>
							</tr>
							<tr>
								<td colspan=6>&nbsp;</td>
							</tr>
							<tr>
								<td class="l" colspan=6>Per la notifica al Soggetto</td>
							</tr>

							<%
								if (luogodet.getLuogoDetenzione() == null || luogodet.getLuogoDetenzione().getIdLuogoDetenzione() == null  || luogodet.getLuogoDetenzione().getDataFineDetenzione() != null || luogodet.getLuogoDetenzione().getIstitutoDetenzione() == null ) {
							%>
							<tr>
								<td class="l">Autorità Destinazione</td>
								<td class="l"><select title="Destinatario"
									name="<%=ICostantiUdienzaSige.CAMPO_COD_IST_DETENZIONE%>">
										<%=tipoAutoritaSogg%>
								</select></td>
							</tr>
							<tr>
								<td class="l">Sede</td>
								<td class="l"><input type="text"
									name="<%=ICostantiUdienzaSige.CAMPO_COD_LUOGO_DETENZIONE%>"
									value="<%=StringUtils.toStringJSP((notSogg.getAutoritaEsterna()!=null)?notSogg.getAutoritaEsterna().getDescrSede():null)%>"
									maxlength="35" size="35" title="Sede"> <a
									href="Javascript:ListaComuni('LoadInserisciFissazioneUdienza','<%=ICostantiUdienzaSige.CAMPO_COD_LUOGO_DETENZIONE%>');"><img
										src="/images/filefolder.gif" border=0></a></td>
							</tr>
							<tr>
								<td class="l">Indirizzo</td>
								<td class="L" colspan=3><input type="text"
									name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>"
									value="<%=StringUtils.toStringJSP(notSogg.getNote())%>"
									maxlength="80" size="80" title="Indirizzo"></td>
							</tr>
							<%
								}else{
							%>
							<tr>
								<td class="l">Tipo Istituto</td>
								<td class="l"><input type="text" name="Comune"
									value="<%=StringUtils.toStringJSP(luogodet.getLuogoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(luogodet.getLuogoDetenzione().getDescrLuogo())%>"
									size="50" title="Istituto" readonly> <a
									href="Javascript:ListaIstitutoDetenzione('LoadInserisciFissazioneUdienza','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>','Comune');">
										<img src="/images/filefolder.gif" border=0>
								</a> <input type="hidden"
									name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>"
									value="<%=luogodet.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>">
									<input type="hidden"
									name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="">
								</td>
							</tr>
							<%
								}
							%>
							<tr>
								<td colspan=6>&nbsp;</td>
							</tr>

							<%
								//Collection<Object> collTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
							int lIdxAvv = 0;
							Iterator itxAvv = avvocato.iterator();
							while ( itxAvv.hasNext()) {
								AvvocatoSigeModel lAvv = (AvvocatoSigeModel)itxAvv.next();
							%>
							<tr>
								<td class=l colspan=6>Per la notifica all' avvocato <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getAvvocato().getNome(),"-")%>
									Foro di <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo(),"-")%>
									Difensore <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo(),"-")%>
									<input type="hidden"
									name="<%=ICostantiUdienzaSige.CAMPO_COD_AVVOCATO%>"
									value="<%=lAvv.getAvvocato().getIdAvvocato()%>">
								</td>
							</tr>

							<%
								String checkSNT = "";

							String descSede =lAvv.getAvvocato().getForo(); 
							NotificaModel modNotAvv = null;
							Iterator itxNotAvv = vectNotAvv.iterator();
							while ( itxNotAvv.hasNext()) {
								NotificaModel nm = (NotificaModel)itxNotAvv.next();
								if (lAvv.getAvvocatoFascicoloSigeModel().getIdAvvocatoFascicoloSige().equals(nm.getAvvIdAvvocatoFascicoloSige())){
									modNotAvv = nm;
									// se non c'è autorità esterna allora è una notifica telematica
									if (nm.getAutoritaEsterna()==null){
								checkSNT = "checked";
									}
								}
							}

							String comboDefVal = ""; 
							if ( modNotAvv != null && modNotAvv.getAutoritaEsterna()!=null && modNotAvv.getAutoritaEsterna().getDescrSede()!=null && !"".equals(modNotAvv.getAutoritaEsterna().getDescrSede()) ) {
								descSede = modNotAvv.getAutoritaEsterna().getDescrSede();
								comboDefVal = modNotAvv.getAutoritaEsterna().getCodTipoAutorita();
							}

							String[] lStringFilter = { "-", "22" };
							Option lOptionAvv = new Option((Collection<Object>)TipiIstitutiColl, comboDefVal, 75);
							lOptionAvv.setFilter(lStringFilter);
							String comboTipiIstituti = lOptionAvv.toString();
							%>

							<tr>
								<td class="l" colspan=6><input type="checkbox"
									name="flagSNT" id="flagSNT_<%=lIdxAvv%>" value="<%=lIdxAvv%>"
									onclick="javascript:checkSNT('<%=lIdxAvv%>');" <%=checkSNT%>>
									S.N.T. (Sistema Notifiche Telematiche)</td>
							</tr>

							<tr id="AutDestRow_<%=lIdxAvv%>">
								<td class="l">Autorità Destinazione</td>
								<td class="l"><select
									name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>"
									id="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>_<%=lIdxAvv%>"
									title="Destinatario">
										<%=comboTipiIstituti%>
								</select></td>
							</tr>

							<tr id="SedeDestRow_<%=lIdxAvv%>">
								<td class="l">Sede</td>
								<td class="l"><input type="text"
									name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
									id="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>_<%=lIdxAvv%>"
									value="<%=StringUtils.toStringJSP(descSede)%>" maxlength="35"
									size="35" title="Sede Procura"> <a
									href="Javascript:ListaUffici('LoadInserisciFissazioneUdienza','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=lIdxAvv%>]');"><img
										src="/images/filefolder.gif" border=0> </a></td>
							</tr>
							<%
								lIdxAvv++;
							}
							%>
							<tr>
								<td colspan=6>&nbsp;</td>
							</tr>

							<%
								String comboNotifica = "";
							String comboComunica = "checked";
							if ("N".equals(notAltro.getCodTipoNotifica()) ){
								comboNotifica = "checked";
								comboComunica = "";
							}
							%>
							<tr>
								<td class=l colspan=6>Per la <input type='radio'
									name="<%=ICostantiUdienzaSige.CAMPO_TIPONOTIFICA%>" value='N'
									<%=comboNotifica%>>Notifica / <input type='radio'
									name="<%=ICostantiUdienzaSige.CAMPO_TIPONOTIFICA%>" value='C'
									<%=comboComunica%>>Comunicazione ad altro destinatario
								</td>
							</tr>

							<tr>
								<td class="l">Destinatario</td>
								<td class="l"><select title="Destinatario"
									name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>"><%=tipoAutoritaAltro%></select>
									<input type="hidden"
									name="<%=ICostantiUdienzaSige.CAMPO_COD_AVVOCATO%>" value="">
								</td>
							</tr>

							<tr>
								<td class="l">Sede</td> 
								<td class="l"><input type="text"
									name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
									value="<%=StringUtils.toStringJSP((notAltro.getAutoritaEsterna()!=null)?notAltro.getAutoritaEsterna().getDescrSede():null)%>"
									maxlength="35" size="35" title="Sede">
									<% if(avvocato.isEmpty()) { %>
									 <a
									href="Javascript:ListaComuni('LoadInserisciFissazioneUdienza','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>');"><img
										src="/images/filefolder.gif" border=0> </a>
									<%}
									else{
									%>
									<a
									href="Javascript:ListaComuni('LoadInserisciFissazioneUdienza','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=lIdxAvv%>]');"><img
										src="/images/filefolder.gif" border=0> </a>
									<%
									}
									%>
									</td>
							</tr>
							<tr>
								<td class="l">Indirizzo</td>
								<td class="L" colspan=3><input type="text"
									name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>"
									value="<%=StringUtils.toStringJSP(notAltro.getNote())%>"
									maxlength="80" size="80" title="Indirizzo"></td>
							</tr>
							<tr>
								<td colspan=6>&nbsp;</td>
							</tr>

						</table>
					</td>
				</tr>

				<tr>
					<td>&nbsp;</td>
				</tr>

				<tr>
					<td><input class="bottone" type="submit" value="Conferma">
					</td>
				</tr>

			</table>

<div id="AltriDatiUdienzaDiv" style="position:relative;  top: 0; left: 0;   visibility:hidden;" >  
  	<table>
				<tr>
					<td class="l">Sezione</td>
					<td class="l"><select title="sezione"
						name="<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>" > 
							<%=elencoSezioniUdienza%>
					</select></td>
					<td class="l" colspan="2">Aula <input type="text"
						name="<%=ICostantiAula.CAMPO_DESCRIZIONE_AULA%>"
						value="<%=StringUtils.toStringJSP(lAulaUdienza.getDescrizioneAula())%>"
						size="12" maxlength="30" readonly="readonly">&nbsp;&nbsp;
						Ingresso <input type="text"
						name="<%=ICostantiAula.CAMPO_DESCRIZIONE_INGRESSO%>"
						value="<%=StringUtils.toStringJSP(lAulaUdienza.getDescrizioneIngresso())%>"
						size="30" maxlength="30" readonly="readonly">&nbsp;&nbsp;
						Piano <input type="text"
						name="<%=ICostantiAula.CAMPO_NUMERO_PIANO%>"
						value="<%=StringUtils.toStringJSP(lAulaUdienza.getNumeroPiano())%>"
						size="12" maxlength="30" readonly="readonly">&nbsp;&nbsp;
						<input type="HIDDEN" name="<%=ICostantiAula.CAMPO_ID_AULA%>"
						value="<%=StringUtils.toStringJSP(lAulaUdienza.getIdAula())%>">
						<%-- <a
						href="Javascript:ListaAule('LoadInserisciFissazioneUdienza', document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>.value);"><img
							src="/images/filefolder.gif" border=0></a> --%>
					</td>
				</tr>

				<tr>
					<td class="l">Orario Inizio (ora:min)</td>
					<td class="l"><input type="text" maxlength="2" size="2"
						onFocus="javascript:textboxSelect(this)"
						onkeypress="return TicTabNumField(this,event)"
						onBlur="javascript:value=FillDM(value)"
						value="<%=StringUtils.toStringJSP(lUdienzaSige.getOraInizio())%>"
						name="<%=ICostantiUdienzaSige.CAMPO_ORA_INIZIO%>"  readonly="readonly"> : <input
						type="text" maxlength="2" size="2"
						onFocus="javascript:textboxSelect(this)"
						onkeypress="return TicTabNumField(this,event)"
						onBlur="javascript:value=FillDM(value)"
						value="<%=StringUtils.toStringJSP(lUdienzaSige.getMinInizio())%>"
						name="<%=ICostantiUdienzaSige.CAMPO_MIN_INIZIO%>"  readonly="readonly" ></td>
					<td class="l">Orario Fine (ora:min)</td>
					<td class="l"><input type="text" maxlength="2" size="2"
						onFocus="javascript:textboxSelect(this)"
						onkeypress="return TicTabNumField(this,event)"
						onBlur="javascript:value=FillDM(value)"
						value="<%=StringUtils.toStringJSP(lUdienzaSige.getOraFine())%>"
						name="<%=ICostantiUdienzaSige.CAMPO_ORA_FINE%>"  readonly="readonly"> : <input
						type="text" maxlength="2" size="2"
						onFocus="javascript:textboxSelect(this)"
						onkeypress="return TicTabNumField(this,event)"
						onBlur="javascript:value=FillDM(value)"
						value="<%=StringUtils.toStringJSP(lUdienzaSige.getMinFine())%>"
						name="<%=ICostantiUdienzaSige.CAMPO_MIN_FINE%>"  readonly="readonly"></td>
				</tr>

				<tr>
					<td class="l">Luogo svolgimento</td>
					
					<td class="l" colspan="3">
						<%-- 20170907: [SG] prevenzione nullpointer: prima si mette la stringa di confronto poi la variabile!!! --%>
						<%
							if("".equals(myLuogo)){
						%> <input type="text"
									name="<%=ICostantiProvvedimentoSige.CAMPO_LUOGO_SVOLGIMENTO%>"
									value="<%=StringUtils.toStringJSP( lUdienzaSige.getLuogoUdienza() , "")%>"
									size="95"> <%
							} else { 
								     if(lUdienzaSige.getLuogoUdienza() != null && !lUdienzaSige.getLuogoUdienza().equals("")){
								 %> <input type="text"
														name="<%=ICostantiProvvedimentoSige.CAMPO_LUOGO_SVOLGIMENTO%>"
														value="<%=StringUtils.toStringJSP( lUdienzaSige.getLuogoUdienza() , "")%>"
														size="95"  readonly="readonly"> <%
								 	} else {
								 %> <input type="text"
														name="<%=ICostantiProvvedimentoSige.CAMPO_LUOGO_SVOLGIMENTO%>"
														value="<%=myLuogo%>" size="95"  readonly="readonly"> <%
								 	}
							}
								 %> <a href="Javascript:cleanLuogoSvolgimento();"> <img
															src="/images/delete.gif" width="12" height="12"
															alt="Pulisci Campo" border="0">
													</a>
					</td>
				</tr>
		</table>
 </div>
  
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"	value="<%=lAzione%>">
			<input type="HIDDEN" name="modalita" value="<%=modalita%>"> 

			<input type="HIDDEN" name="<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>" value="<%=StringUtils.toStringJSP( lUdienzaSige.getIdUdienzaSige(), "" )%>">
			<input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>"       value="">
			<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>"  value=""> 
			<%-- 20190519 [SG]: aggiunta gestione idUdienzaSige --%>
			<input type="HIDDEN" name="idUdiSig" value="">

			<%
				// Passaggio dell'eventuale ID UDIENZA_PROCEDIMENTO
					if (request.getAttribute(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE) != null) {
			%>
			<input type="HIDDEN"
				name="<%=ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE%>"
				value="<%=StringUtils.toStringJSP( request.getAttribute(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE), "0" )%>">
			<input type="HIDDEN" name="Cancellabile"
				value="<%=StringUtils.toStringJSP( request.getAttribute("Cancellabile"), "" )%>">
			<%
				}
			%>

		</form>

		<script language="JavaScript" type="text/javascript">
  function initFlagSNT() {
<%
for (int t = 0; t < lIdxAvv; t++) {
%>
	  	checkSNT('<%=t%>');
<%
}
%>
	  }

    var frmvalidator = new Validator("LoadInserisciFissazioneUdienza");
    // Controllo data emissione.
<%--     frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req", "Il campo Giorno Data Emissione è obbligatorio"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req", "Il campo Mese Data Emissione é obbligatorio"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req", "Il campo Anno Data Emissione é obbligatorio"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri"); --%>

    // Controllo campo oggetto.
    <%-- frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_DESCR_OGGETTO%>", "req","E' necessario selezionare almeno un oggetto"); --%>

    // Controllo data udienza.
<%--     frmvalidator.addValidation("<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>","req", "Il campo Giorno Data Udienza è obbligatorio"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>","numeric"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>","req", "Il campo Mese Data Udienza é obbligatorio"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>","numeric"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>","req", "Il campo Anno Data Udienza é obbligatorio"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>","numeric"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri"); --%>

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");

  </script>
</body>
</html>
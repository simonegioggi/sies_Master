<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.RedirectTo"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocatoFascicoloSige"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.istruttoria.action.ICostantiIstruttoria"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.tenore.action.ICostantiTenoreSige"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sige.magistratoassegnatario.action.ICostantiMagistratoAssegnatario"%>
<%@ page import="siap.sige.udienzacollegiale.action.ICostantiUdienzaCollegiale"%>
<%@ page import="siap.sico.ufficio.controller.UfficioUtils"%>
<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel" %>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="f3b.web.html.Option"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>

<jsp:useBean id="tipoAutorita"        scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti1"       scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"            scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita"            scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"              scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="ProvvedimentoEvento" scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="tipoUfficioUtente"   scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoGiudizio"        scope="request" class="java.lang.String"/>
<jsp:useBean id="TenoriSige" 	      scope="session" class="java.util.Vector"/>
<jsp:useBean id="avvocato"		      scope="request" class="java.util.Vector" />
<jsp:useBean id="TornaQui" 		      scope="request" class="java.lang.String" />
<jsp:useBean id="UdienzaSige"	      scope="request" class="siap.sige.udienza.model.UdienzaSigeModel"/> 
<jsp:useBean id="ufficioCompetenteCorteSuprema"  scope="request" class="java.lang.String"/>

<%
	String isVALIGN = "top";
	String isBorder = "0";
	String lWidth = "96%";

	// Switch tra Emissione e Modifica
	boolean modifica = false;
	String lAction = "siap.sige.provvInterlocutori.action.ActInserisciOrdinanzaConflittoCompetenza";
	String lNomeFunzione = "Emissione Ordinanza";
	String lDataEmiGG = "", lDataEmiMM = "", lDataEmiAA = "";
	String lSedeDest = "";
    String lCodTipoUff = "-";
    String lSedeUffCompCorteSuprema = "";
    String lCodTipoUffCompCorteSuprema = "-";    
    String lMotivazione = "";
    // Ufficio destinatario
    Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficio());
    lOption.setFilter( new String[] {"-", "CAP", "CAS", "CASAP", "CAPMI", "CAPMID", "CSS", "GIPMI", "GIP", "GIPM", "GP", "GUP", "GUPM", "GUPMI", "PT", "TRIBSD", "CAPSM", "TMI", "DIB", "DIBM"} ); //solo le Autorità Emittenti.

    // Ufficio G.E. (Giudice di Esecuzione) coinvolto
    Option lOptionGE = new Option( DecodificheManager.getInstance().getTipoUfficio());
    //@emma 13072018 intervento post COLLAUDO 11.2 (Aggiungo autorita CAPSM)
    lOptionGE.setFilter( new String[] {"-","CAP","CAS","CASAP","CAPSM","GIP","GIPM","GP","GUP","GUPM","DIB","DIBM","TRIBSD"} );

        if( modalita.equalsIgnoreCase("I") )
         {
        	lAction = "siap.sige.provvInterlocutori.action.ActInserisciOrdinanzaConflittoCompetenza";
	      	lNomeFunzione = "Emissione Ordinanza Conflitto di Competenza";
 
		}
        else if( modalita.equalsIgnoreCase("M") )
        {
          modifica = true;
    	  lNomeFunzione = "Modifica Ordinanza Conflitto di Competenza";
    	  lDataEmiGG = DateUtils.getDateToString(ProvvedimentoEvento.getProvvedimento().getDataEmissione(),"dd");
    	  lDataEmiMM =  DateUtils.getDateToString(ProvvedimentoEvento.getProvvedimento().getDataEmissione(),"MM");
    	  lDataEmiAA = DateUtils.getDateToString(ProvvedimentoEvento.getProvvedimento().getDataEmissione(),"yyyy");
    	  lAction = "siap.sige.provvedimento.action.ActModificaOrdinanza";
    	  lMotivazione =   StringUtils.toStringJSP(ProvvedimentoEvento.getProvvedimento().getNote());
    	  lCodTipoUff = ProvvedimentoEvento.getProvvedimento().getCodUfficioDestinatario();
    	  if (ProvvedimentoEvento.getProvvedimento().getCodUfficioDestinatario() != null && ProvvedimentoEvento.getProvvedimento().getCodUfficioDestinatario().trim().length() > 0)
    	  {
    	    	lSedeDest = UfficioUtils.getUfficioByCodUfficio(ProvvedimentoEvento.getProvvedimento().getCodUfficioDestinatario()).getDescrComune();
    	 		lCodTipoUff = UfficioUtils.getUfficioByCodUfficio(ProvvedimentoEvento.getProvvedimento().getCodUfficioDestinatario()).getCodTipoUfficio();
    	        lOption.setSelected(lCodTipoUff);
    	        lOptionGE.setSelected(lCodTipoUff);
     	  }
    	  if (ProvvedimentoEvento.getProvvedimento().getCodUffCompCorteSuprema() != null && ProvvedimentoEvento.getProvvedimento().getCodUffCompCorteSuprema().trim().length() > 0)
    	  {
          		lSedeUffCompCorteSuprema = UfficioUtils.getUfficioByCodUfficio(ProvvedimentoEvento.getProvvedimento().getCodUffCompCorteSuprema()).getDescrComune();
          		lCodTipoUffCompCorteSuprema = UfficioUtils.getUfficioByCodUfficio(ProvvedimentoEvento.getProvvedimento().getCodUffCompCorteSuprema()).getCodTipoUfficio();
     	  }
        }
	    String tipoUfficioCompetente = lOption.toString();
	    String tipoUfficioGE = lOptionGE.toString();

        
	// Se viene passato nella request la lista con le opzioni Tipo Giudizio 
	// occorre visualizzare la combo per la scelta del Tipo Giudizio per il Fascicolo.
	boolean defTipoGiudizio = false;
	if (tipoGiudizio != null && tipoGiudizio.trim().length() > 0)
		defTipoGiudizio = true;

	//Magistrato Assegnatario
	MagistratoAssegnatarioModel magistratoassegnatario = FascicoloSigeEsteso.getMagAssegnatario();

	/* Estrazione della data udienza */
	String lDataUdienza = "";
	if ((FascicoloSigeEsteso.getUdienzaProcedimento() != null) &&
			(FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige()!=null) )
		lDataUdienza = (DateUtils.getDateToString (FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige(), "dd/MM/yyyy"));

	// Link alla Gestione Oggetti 
	RedirectTo lRedir = new RedirectTo();
	lRedir.setPage(IWebConstants.PG_MAIN);
	lRedir.setAction("siap.sige.tenore.action.ActLoadDettaglioOggetti");
	lRedir.setParameter("TornaQui", TornaQui );

	String lLinkOggettiSessione = lRedir.toString();
	
	UdienzaSigeModel lUdienzaSige = new UdienzaSigeModel();
	if( UdienzaSige!=null && UdienzaSige.getIdUdienzaSige() != null  ) {
		lUdienzaSige = UdienzaSige;
	}
	
	//
	// Preparazione Link Gestione udienze
	//
	String actUdiMono = "siap.sige.udienzamonocratica.action.ActLoadInserisciUdienzaMonocraticaSige";
	String actUdiColle = "siap.sige.udienzacollegiale.action.ActLoadInserisciUdienzaCollegiale";
	String idUdiSige = "";
	if( lUdienzaSige.getIdUdienzaSige() != null  ) {
		actUdiMono = "siap.sige.udienzamonocratica.action.ActLoadDettaglioUdienzaMonocraticaSige";
		actUdiColle = "siap.sige.udienzacollegiale.action.ActLoadDettaglioUdienzaCollegiale";
		idUdiSige = lUdienzaSige.getIdUdienzaSige().toString();
	}
	
	// Monocratica 
	lRedir.setAction(actUdiMono);
	lRedir.setParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE, idUdiSige);
	lRedir.setParameter(ICostantiCollegio.FORM_DEF_COLLEGIO, "yes");
	lRedir.setParameter("TornaQui", TornaQui);
	String lLinkUdienzaMonocratica = lRedir.toString();

	// Collegiale
	lRedir.setAction(actUdiColle);
	lRedir.setParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE, idUdiSige);
	lRedir.setParameter(ICostantiCollegio.FORM_DEF_COLLEGIO, "yes");
	lRedir.setParameter("TornaQui", TornaQui);
	lRedir.setParameter("PopUp", "yes");
	String lLinkUdienzaCollegiale = lRedir.toString();
%>


<html>
  <head>
    <title>[S.I.E.S.] - Ordinanza Conflitto di Competenza </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript">
		var desktop;
        // Chiamata funzione lista Oggetti
        function ListaOggetti(a_formname,a_field_contenuto, a_fieldname, a_fieldcodes, a_fieldcodesdet, i_fieldcodes, i_fieldcodesdet) {
          	// Compone il link URL per passare i parametri alla ElencoUdienza.JSP
          	var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaOggetti";
			aLink += "&formname="+a_formname;
			aLink += "&field_contenuto="+a_field_contenuto;
            aLink += "&fieldname="+a_fieldname;
            aLink += "&fieldcodes="+a_fieldcodes;
            aLink += "&fieldcodesdet="+a_fieldcodesdet;
            aLink += "&ifieldcodes="+i_fieldcodes;
            aLink += "&ifieldcodesdet="+i_fieldcodesdet;
          	desktop = window.open(aLink, "Lista_Oggetti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
        }

        function Init() {
        	// VERSIONE 11: commentata chiamata a funzione che non esiste (errore js)
        	<%-- if (defTipoGiudizio) { %>
        		Visualizza(document.LoadOrdConfCompetenza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value);
        	 <%} --%>
        	Verifica();
        }

		// Verifica del Magistrato Assegnatario
        function  Verifica() {
          	var ritorno = true;
          	<% if (magistratoassegnatario == null )	{ %>
				ritorno = false;
          	<% } %>
          	if (! ritorno)
          		alert (" Magistrato non assegnato!");
          	return ritorno;
		}

		function Verify() {
	        var ritorno = true;
	        var data_udienza = '<%=lDataUdienza%>';
	        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

			// Controllo della data emissione.
			var data_emissione = document.LoadOrdConfCompetenza.<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadOrdConfCompetenza.<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadOrdConfCompetenza.<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE%>.value;
			// controllo Magistrato
	 		ritorno = Verifica();
	 
	 		if (document.LoadOrdConfCompetenza.<%=ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFF_COMP_CORTE_SUPREMA%>.value == "-"
	 				|| document.LoadOrdConfCompetenza.<%=ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFF_COMP_CORTE_SUPREMA%>.value == "") {
		     	alert("La Sede dell'Ufficio Competente è obbligatoria");
		      	return false;
     		}
	 		 
      		// Controllo data di sistema >= Data Emissione .
      		if (!CompareDate( data_emissione, data_sistema)) {
		        alert('Data Emissione maggiore della Data di sistema!');
		        ritorno =  false;
      		}
      		// Controllo della data deposito <= data  di udienza
      		else if ((ControllaData(data_emissione)) && (!CompareDate(data_udienza, data_emissione))) {
		        alert('Data Emissione minore della Data di Udienza!');
		        ritorno =  false;
      		}
			<% if (defTipoGiudizio) { %>
     			// Controllo obbligatorietà Collegio in caso di Tipo Giudizio impostato.
    			if (document.LoadOrdConfCompetenza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value != "-") {
    				if (document.LoadOrdConfCompetenza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value == "C"){
    					<%-- Ticket20191210011 ( uniformo il controllo come all'ordinanza generica ) --%>
   			   	    	 <%
   			   	    	 boolean test = false;
   			   	    	 if ("CAS".equals(tipoUfficioUtente) || "CASAP".equals(tipoUfficioUtente) || "CAP".equals(tipoUfficioUtente) || "CAPSM".equals(tipoUfficioUtente)
   			   	    			 || "DIBM".equals(tipoUfficioUtente) || "DIB".equals(tipoUfficioUtente) || "GUPM".equals(tipoUfficioUtente))
   			   	    		 test = true;
   			   	    	 %>
   			   	    	 if (!<%=test%>)
   			   	   	     	return checkObblCollegio();    					
    				}
  				<% } %>
     			return ritorno;
    		}

			// Controllo obbligatorietà Tipo Giudizio.
    		if (document.LoadOrdConfCompetenza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value == "-"
    				|| document.LoadOrdConfCompetenza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value == "") {
	      		alert('Inserire il Tipo Rito');
	      		return false;
    		}

			if (document.LoadOrdConfCompetenza.<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
				document.LoadOrdConfCompetenza.<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadOrdConfCompetenza.<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  	if (document.LoadOrdConfCompetenza.<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  	document.LoadOrdConfCompetenza.<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadOrdConfCompetenza.<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>.value;

			var data_to_verify = document.LoadOrdConfCompetenza.<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadOrdConfCompetenza.<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadOrdConfCompetenza.<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      		if (!ControllaData(data_to_verify)) {
       			alert('Data di emissione non valida');
			 	return false;
		  	}	
  		}
		
		// Check Obbl. Collegio.
		function checkObblCollegio() {
			var ritorno = true;
			var collegio = document.LoadOrdConfCompetenza.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>.value;
			if (collegio == '') {
	    		alert("Collegio obbligatorio.");
	      		ritorno = false;
	    	}
			return ritorno;  
		}

  		function ListaUDS(a_formname,a_fieldname) {
   			desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  		}
    
    	function ListaUffici(a_formname,a_fieldname) {
      		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    	}
    
    	function ListaComuni(a_formname,a_fieldname) {
     		desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    	}

    	function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio) {
  			desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    	}

	  	function ListaMagistrati(a_formname) {
        	var a_codnum = document.LoadOrdConfCompetenza.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_VECCHIO %>.value;
        	var desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sige.magistrato.action.ActLoadRicercaMagistratoAssegnazioneLista&formname="+a_formname, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      	}

		function calendario(a_formname,a_field_year,a_field_month,a_field_day) {
        	desktop = window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
     	}
      
      	// Funzione JS per chiamta azione inserimento udienza
  		function InserisciUdienza( tipoRito ) {
  			var dataUdienza = document.LoadOrdConfCompetenza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+
  				document.LoadOrdConfCompetenza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value +'/'+ 
                document.LoadOrdConfCompetenza.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value;
      		var lLink;
  			if (tipoRito == 'M') {
	  			lLink = "<%=lLinkUdienzaMonocratica%>";
	  			// 20190506 [SG]: resize
	  			desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=750,height=500" );
  			} else if (tipoRito == 'C') {
  				var idUdienza=document.LoadOrdConfCompetenza.<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE %>.value;
  				lLink = "<%=lLinkUdienzaCollegiale%>&DataUdienza=" + dataUdienza + "&<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE %>="+idUdienza;
  				// 20190506 [SG]: resize
  				desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=750,height=500" );
  			} else {
	  			alert("Definire il tipo rito");  			
	  			return;
  			}		 
  		}
      
  	// 20190506 [SG]: aggiunti parametri di passaggio e controllo sul codMagistrato
	function setUdienza (idUdienza, dataUdienza, idCollegio, idSezione, codMagistrato) {
		// 20190517 [SG]: aggiunto controllo preventivo se dalla popup torno indietro senza aver inserito
		if (dataUdienza != "null") {
			var dataSplitted=dataUdienza.split("-");
	     	document.LoadOrdConfCompetenza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA %>.value=dataSplitted[0];
	     	document.LoadOrdConfCompetenza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA %>.value=dataSplitted[1];
	     	document.LoadOrdConfCompetenza.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA %>.value=dataSplitted[2];
	     	document.LoadOrdConfCompetenza.<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE %>.value=idUdienza;
	     	document.LoadOrdConfCompetenza.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>.value=idCollegio;
	     	if (codMagistrato != null) {
	    		document.LoadOrdConfCompetenza.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value = codMagistrato;
		}
	    	// 20190519 [SG]: aggiunta gestione idUdienzaSige
			if (idUdienza != null) {
				document.LoadOrdConfCompetenza.idUdiSig.value = idUdienza;
			}
		}
	}

	<%-- 20190519 [SG]: aggiunta funzione --%>
	function gestioneOggetti() {
    	var lLink;
		var idUdienza = document.LoadOrdConfCompetenza.idUdiSig.value;
		if (idUdienza == undefined)
			idUdienza = "";
		lLink = "<%=lLinkOggettiSessione%>&idUdiSig=" + idUdienza;
		window.location = lLink;
	}
  	</script>
</head>

<body class="corpo" onLoad="Init();">
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font> &nbsp;&nbsp;

       <font class="campo"><%=lNomeFunzione%></font>
      </td>
    </tr>
    </table>
    
 <br>
    <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
  <br>
    
   
   <table  width="95%" ><tr>
	    <td class="Titolo">Magistrato</td>
 </tr></table>
   <table><tr>
    <jsp:include page="<%=ICostantiMagistratoAssegnatario.PG_SINTESIMAGISTRATOASSEGNATARIO%>">
          <jsp:param name="MagAssRitorno" value="siap.sige.provvedimento.action.ActLoadEmissioneOrdinanzaIncompetenza"/>
    </jsp:include>
    </tr>
      <jsp:include page="<%=ICostantiAvvocatoFascicoloSige.PG_INCLUDE_AVVOCATI%>">
      <jsp:param name="AvvRitorno" value="siap.sige.provvedimento.action.ActLoadEmissioneOrdinanzaIncompetenza"/>
      </jsp:include>
   </table>
   
  <FORM method="POST" name="LoadOrdConfCompetenza" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
  	<%-- 20190506 [SG]: aggiunto campo nascosto --%>
  	<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" value="">
	<%-- 20190519 [SG]: aggiunta gestione idUdienzaSige --%>
	<input type="HIDDEN" name="idUdiSig" value="">
<%if (modifica) { %>
 <input type="HIDDEN" name="<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>" value="<%=ProvvedimentoEvento.getProvvedimento().getIdProvvedimentoSige().toString()%>">
 <input type="HIDDEN" name="<%=ICostantiProvvedimentoSige.CAMPO_ID_EVENTO_GENERATO%>" value="<%=ProvvedimentoEvento.getProvvedimento().getIdEventoGenerato()%>">
<%} %>
  	  <br/>
  	  <jsp:include page="<%=ICostantiProvvedimentoSige.INC_TIPO_GIUDIZIO_COLLEGIO_CONFLITTO_COMP%>" />
  	  <br/>
   <table  width="95%" >
   <tr>
	    <td class="Titolo">Estremi Ordinanza</td>
	  </tr>
</table>    

<table>
	<tr>
        <td class="l">Data Emissione <font class="ob">(*)</font></td >
        <td class="L">
          <input type="text" size="2" maxlength="2" name="<%= ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" value = "<%=lDataEmiGG%>" > /
          <input type="text" size="2" maxlength="2" name="<%= ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  value = "<%=lDataEmiMM%>"> /
          <input type="text" size="4" maxlength="4" name="<%= ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" value = "<%=lDataEmiAA%>">

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadOrdConfCompetenza','<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE%>','<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>');">
       		  <img src="/images/calendario.gif" border=0>
          </a>
        </td>
    </tr>
 </table>
<% if (modifica) { %>
	<table style="width: 95%;">
    	<tr>
      		<td class="Titolo">Oggetti</td>
      		<td class="Titolo">Esito</td>
      		<td class="Titolo">Dettaglio</td>  
    	</tr>

    	<tr>
  			<div id="elenco1" style="width: 100%; display:block">
  				<jsp:include page="/jsp/files/siap/sige/tenore/ElencoDettaglioTenoriExtra.jsp"/>
  			</div>
		</tr>
	</table>
<% } else { %>
 	<table>  
    	<tr>
  			<td class="L">
  		  		<table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
  					<tr>
    		 			<td class="label" width=15% colspan=2>
             				<%-- 20190519 [SG]: aggiunta gestione idUdienzaSige --%>
<%-- 							<a class="cliccabile" href="<%=lLinkOggettiSessione%>">Oggetti</a> --%>
							<a class="cliccabile" href="javascript:gestioneOggetti();">Oggetti</a>
      		 			</td>
  					</tr>
  				</table>
  			</td>
  			<td  class="L">
  				<div id="elenco1" style="width: 100%; display:block">
  					<jsp:include page="/jsp/files/siap/sige/tenore/ElencoTenoriSige.jsp"/>
  				</div>
	 		</td>
		</tr>
	</table>
<% } %>
<table>
	<!-- Campo Note  -->
	<tr>
		<td class="l">Motivazioni </td>
		<td class="L" colspan=3>
           	<TEXTAREA title="Note" name="<%= ICostantiProvvedimentoSige.CAMPO_NOTE%>" cols=80 rows=2 ><%=lMotivazione%></textarea>
		</td>
	</tr>
	<tr>
	  	<td class="l">Ufficio Competente</td>
	  	<td class="L" colspan=3>
		  	<select title="Ufficio Competente" name="<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_UFF_COMP_CORTE_SUPREMA%>">
	        	<%=ufficioCompetenteCorteSuprema%>
	      	</select>
     	</td>
   	</tr>
   	<tr>
       	<td class="l">Sede </td>
       	<td class="l" colspan="3">
			<input Title="Sede" name="<%=ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFF_COMP_CORTE_SUPREMA%>" type="text" maxlength="35" size="35"  value="<%=lSedeUffCompCorteSuprema%>">
			<a href="Javascript:ListaUfficiPerTipo('LoadOrdConfCompetenza','<%=ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFF_COMP_CORTE_SUPREMA%>',document.LoadOrdConfCompetenza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_UFF_COMP_CORTE_SUPREMA%>[document.LoadOrdConfCompetenza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_UFF_COMP_CORTE_SUPREMA%>.selectedIndex].value);">
				<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
	</tr>
   	<br>
   	<tr>
   		<td class="l">G.E. Coinvolto </td>
   		<td class="L">
     		<select title="tipoUfficioCompetente" class=small name="<%=ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO%>">
       			<%=tipoUfficioGE%>
     		</select>
     	</td>
	</tr>
	<tr>
   		<td class="l">Sede</td>
   		<td>
   			<input Title="Sede" name="<%=ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO%>" type="text" maxlength="35" size="35"  value="<%=lSedeDest%>">
         		<a href="Javascript:ListaUfficiPerTipo('LoadOrdConfCompetenza','<%=ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO%>',document.LoadOrdConfCompetenza.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO%>[document.LoadOrdConfCompetenza.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO%>.selectedIndex].value);">
         			<img src="/images/filefolder.gif" border=0>
         		</a>
   		</td>
   	</tr>
  	<tr>
   		<td class="lNoBord" colspan="2">
   			<br><br><INPUT class="bottone" type="submit" name="I" value="Conferma">
   		</td>
	</tr>
</table>

			<script language="JavaScript" type="text/javascript">
				var frmvalidator  = new Validator("LoadOrdConfCompetenza");
			
			  	frmvalidator.addValidation("<%= ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
			  	frmvalidator.addValidation("<%= ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
			  	frmvalidator.addValidation("<%= ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
			  	frmvalidator.addValidation("<%= ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");
			
			  	frmvalidator.addValidation("<%= ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
			  	frmvalidator.addValidation("<%= ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
			  	frmvalidator.addValidation("<%= ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
			  	frmvalidator.addValidation("<%= ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");
			
			  	frmvalidator.addValidation("<%= ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
			  	frmvalidator.addValidation("<%= ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
			  	frmvalidator.addValidation("<%= ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
			  	frmvalidator.addValidation("<%= ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2999");
			  
			  	frmvalidator.setAddnlValidationFunction("Verify");
			</script>
		</form>
	</body>
</html>
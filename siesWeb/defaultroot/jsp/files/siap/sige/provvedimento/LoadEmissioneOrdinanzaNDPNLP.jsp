<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sige.magistrato.action.ICostantiMagistrato"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.web.RedirectTo"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sige.avvocato.action.ICostantiAvvocatoFascicoloSige"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel"%>
<%@ page import="siap.sige.richiesta.model.RichiestaSigeModel"%>
<%@ page import="siap.sige.tenore.action.ICostantiTenoreSige"%>
<%@ page import="siap.sige.magistratoassegnatario.action.ICostantiMagistratoAssegnatario"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel"%>
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.udienzacollegiale.action.ICostantiUdienzaCollegiale"%>
<%@ page import="siap.sico.ufficio.controller.UfficioUtils"%>
<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="f3b.web.html.Option"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sige.motivazioneprovvedimento.model.MotivazioneProvvedimentoSigeModel"%>
<%@ page import="siap.sige.motivazioneprovvedimento.action.ICostantiMotivazioneProvvedimento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="FascicoloSigeEsteso" 	scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="tipoUfficioUtente"   	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoGiudizio"  	  	scope="request" class="java.lang.String"/>
<jsp:useBean id="TenoriSige" 		  	scope="session" class="java.util.Vector"/>
<jsp:useBean id="avvocato"			  	scope="request" class="java.util.Vector" />
<jsp:useBean id="TornaQui" 			 	scope="request" class="java.lang.String" />
<jsp:useBean id="modalita"            	scope="request" class="java.lang.String"/>
<jsp:useBean id="ProvvedimentoEvento" 	scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />
<jsp:useBean id="UdienzaSige"	      	scope="request" class="siap.sige.udienza.model.UdienzaSigeModel"/>
<jsp:useBean id="tipoUfficioCompetente" scope="request" class="java.lang.String"/>
<%
	String isVALIGN = "top";
	String isBorder = "0";
	String lWidth = "96%";

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
	
	// Modifica del 08/03/2017 INIZIO ******
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
	// Modifica del 08/03/2017 FINE ******	
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza di NDP/NLP </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript">
        var desktop;
        function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
        {
           desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
        }

        // Chiamata funzione lista Oggetti
        function ListaOggetti(a_formname,a_field_contenuto, a_fieldname, a_fieldcodes, a_fieldcodesdet, i_fieldcodes, i_fieldcodesdet )
        {
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
        
        function Init()
        {
        	<%-- [SG] 20190312: risolto errore js --%>
<%--         	<% if (defTipoGiudizio) { %> --%>
<%--         		Visualizza(document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value); --%>
<%--         	 <%} %> --%>
        	Verifica();
        }
				// Verifica del Magistrato Assegnatario
        function  Verifica()
        {
          var ritorno = true;

          <% if (magistratoassegnatario == null )	{ %>
              ritorno = false;
          <% } %>

          if (! ritorno)
          alert (" Magistrato non assegnato!");

          return ritorno;
        }
    </script>

    <script language="JavaScript">
    function Verify()
    {
        var ritorno = true;
        //alert("Verify");
        var data_udienza = '<%=lDataUdienza%>';
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

      	// Controllo della data emissione.
      	var data_emissione = document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      	//alert("data emissione ->" + data_emissione);
	 
	 	// controllo Magistrato
	 	ritorno = Verifica();
	 
      if (ritorno && ! ControllaData(data_emissione))
      {
        alert('Data emissione non valida!');
        ritorno =  false;
      }
      // Controllo data di sistema >= Data Emissione .
      else if( !CompareDate( data_emissione, data_sistema) )
      {
        alert('Data Emissione maggiore della Data di sistema!');
        ritorno =  false;
      }
      // Controllo della data deposito <= data  di udienza
     //alert("data_udienza ->" + data_udienza);
      else if ( ( ControllaData(data_emissione)) && ( !CompareDate( data_udienza, data_emissione) ) )
      {
        alert('Data Emissione minore della Data di Udienza!');
        ritorno =  false;
      }

	  // I campi "Ufficio Competente" e "Ufficio di Sorveglianza Competente", sono esclusivi
	  // poichè entrambi i valori vengono inseriti nello stesso campo "COD_UFFICIO_DESTINATARIO"
	  // nella tabella PROVVEDIMENTO_SIGE.
	  if(document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO%>.value != null && 
		 document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO%>.value != "-"  && 
		 document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiProvvedimentoSige.SEDE_MAGISTRATO_COMPETENTE %>.value != null &&
		 document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiProvvedimentoSige.SEDE_MAGISTRATO_COMPETENTE %>.value != ""){
	        alert('I campi Ufficio Competente e Ufficio di Sorveglianza Competente sono esclusivi!');
	        ritorno =  false;
	  }

<%	if (defTipoGiudizio) { %>
     //Controllo obbligatorietà Collegio in caso di Tipo Giudizio impostato.
    	if ( document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value != "-" )
    	{
    	if (document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value == "C"){
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
  	<%}%>
     	return ritorno;
    }
     
    	// Check Obbl. Collegio.
		function checkObblCollegio()
		{
			var ritorno = true;
			var collegio = document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>.value;
			if ( collegio == '' ) 
	    {
	    	alert("Collegio obbligatorio.");
	      ritorno = false;
	    }
				return ritorno;  
		}
  }

  function ListaUDS(a_formname,a_fieldname)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  }

  function calendario(a_formname,a_field_year,a_field_month,a_field_day)
  {
    desktop = 
        window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
  }

    // Modifica del 08/03/2017 INIZIO *****
    // Funzione JS per chiamta azione inserimento udienza
    //   
	function InserisciUdienza( tipoRito ){
		var dataUdienza= document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+
	                     document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value +'/'+
	                     document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value;
	   	
	   	var lLink;
		if( tipoRito == 'M' ){
			lLink = "<%=lLinkUdienzaMonocratica%>";
			// 20190506 [SG]: resize
			desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=750,height=500" );
		} else if( tipoRito == 'C' ){
			var idUdienza=document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE %>.value;
			lLink = "<%=lLinkUdienzaCollegiale%>&DataUdienza=" + dataUdienza + "&<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE %>="+idUdienza;
			// 20190506 [SG]: resize
			desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=750,height=500" );
		} else {
			alert("Definire il tipo rito");
			
			return;
		}		 
		//window.location=lLink;	
	} 
	
	// 20190506 [SG]: aggiunti parametri di passaggio e controllo sul codMagistrato
	function setUdienza (idUdienza, dataUdienza, idCollegio, idSezione, codMagistrato) {
	 	if (dataUdienza != "null") {
			 var dataSplitted=dataUdienza.split("-");
		     document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA %>.value=dataSplitted[0];
		     document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA %>.value=dataSplitted[1];
		     document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA %>.value=dataSplitted[2];
		     document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE %>.value=idUdienza;
		     document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>.value=idCollegio;
			if (codMagistrato != null) {
				document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value = codMagistrato;
			}
			// 20190519 [SG]: aggiunta gestione idUdienzaSige
			if (idUdienza != null) {
				document.LoadEmissioneOrdinanzaNDPNLP.idUdiSig.value = idUdienza;
	 	} 
	 }
	 }
	 // Modifica del 08/03/2017 FINE *****    
	 
	<%-- 20190519 [SG]: aggiunta funzione --%>
	function gestioneOggetti() {
    	var lLink;
		var idUdienza = document.LoadEmissioneOrdinanzaNDPNLP.idUdiSig.value;
		if (idUdienza == undefined)
			idUdienza = "";
		lLink = "<%=lLinkOggettiSessione%>&idUdiSig=" + idUdienza;
		window.location = lLink;
	}
  </script>

  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  </head>

  <body class="corpo" onLoad="Init();" >
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :  </font>&nbsp;
<%

	// Switch tra Emissione e Modifica
	boolean modifica = false;
	String lAction = "siap.sige.provvedimento.action.ActInserisciOrdinanzaNDPNLP";
	String lNomeFunzione = "Emissione Ordinanza di NDP/NLP";
	String lDataEmiGG = "", lDataEmiMM = "", lDataEmiAA = "";
	String lMotivazione = "";
	String lSedeUDS = "";
	String lCodTipoUff = "";
	String lCodTipoMotivazione = "C0";
    String lAltraMotiv = "";
    String lDescMotiv = "";
	
	if (modalita.equalsIgnoreCase("M"))
	{
		modifica = true;
	    lAction = "siap.sige.provvedimento.action.ActModificaOrdinanza";
	    lNomeFunzione = "Modifica Ordinanza di NDP/NLP";
	    lDataEmiGG = DateUtils.getDateToString(ProvvedimentoEvento.getProvvedimento().getDataEmissione(),"dd");
	    lDataEmiMM =  DateUtils.getDateToString(ProvvedimentoEvento.getProvvedimento().getDataEmissione(),"MM");
	    lDataEmiAA = DateUtils.getDateToString(ProvvedimentoEvento.getProvvedimento().getDataEmissione(),"yyyy");
	    lMotivazione = ProvvedimentoEvento.getProvvedimento().getNote() != null  ? ProvvedimentoEvento.getProvvedimento().getNote() : "";
	 	if (ProvvedimentoEvento.getProvvedimento().getCodUfficioDestinatario() != null && ProvvedimentoEvento.getProvvedimento().getCodUfficioDestinatario().trim().length() > 0)
	 	{
	    	lSedeUDS = UfficioUtils.getUfficioByCodUfficio(ProvvedimentoEvento.getProvvedimento().getCodUfficioDestinatario()).getDescrComune();
	 		lCodTipoUff = UfficioUtils.getUfficioByCodUfficio(ProvvedimentoEvento.getProvvedimento().getCodUfficioDestinatario()).getCodTipoUfficio();
	 	}
	
	 	// Modifica del 08/03/2017 INIZIO ******
	    // Si Imposta l'Ufficio Competente.
	    if (ProvvedimentoEvento.getProvvedimento().getCodUfficioDestinatario() != null && ProvvedimentoEvento.getProvvedimento().getCodUfficioDestinatario().trim().length() > 0)
	    {
	     	lSedeUDS = UfficioUtils.getUfficioByCodUfficio(ProvvedimentoEvento.getProvvedimento().getCodUfficioDestinatario()).getDescrComune();
	   	lCodTipoUff = UfficioUtils.getUfficioByCodUfficio(ProvvedimentoEvento.getProvvedimento().getCodUfficioDestinatario()).getCodTipoUfficio();
	    }    
	    Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficio());
	    lOption.setFilter( new String[] {"-", "CAP", "CAS", "CASAP", "CAPMI", "CAPMID", "CSS", "GIPMI", "GIP", "GIPM", "GP", "GUP", "GUPM", "GUPMI", "PT", "TRIBSD", "CAPSM", "TMI", "DIB", "DIBM"} ); //solo le Autorità Emittenti.
	    lOption.setSelected(lCodTipoUff);
	    tipoUfficioCompetente = lOption.toString();
	
	}

	// Motivazione
	if( ProvvedimentoEvento != null && ProvvedimentoEvento.getMotiviProvvedSige() != null && ProvvedimentoEvento.getMotiviProvvedSige().size() > 0  )
	{
		MotivazioneProvvedimentoSigeModel motivoProvvedimento = (MotivazioneProvvedimentoSigeModel) (ProvvedimentoEvento.getMotiviProvvedSige().firstElement());
		if (motivoProvvedimento != null)
		{
			if (motivoProvvedimento.getCodTipoMotivazione() != null && motivoProvvedimento.getCodTipoMotivazione().trim().length() > 1)
				lCodTipoMotivazione = motivoProvvedimento.getCodTipoMotivazione();
			if(motivoProvvedimento.getAltraMotivazione() != null && motivoProvvedimento.getAltraMotivazione().trim().length() > 0)
				lAltraMotiv = motivoProvvedimento.getAltraMotivazione();
	 		if (lCodTipoMotivazione.equalsIgnoreCase("C0") && motivoProvvedimento.getDescrMotivazione() != null && motivoProvvedimento.getDescrMotivazione().trim().length() > 0)
				lDescMotiv = motivoProvvedimento.getDescrMotivazione();
		}
	}
%>
        <font class="campo"><%=lNomeFunzione%></font>
      </td>
        <!-- BOTTONE DI RITORNO -->
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>     
     </tr>

    <tr>
       <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    </tr>
    <tr>
    <jsp:include page="<%=ICostantiMagistratoAssegnatario.PG_SINTESIMAGISTRATOASSEGNATARIO%>">
          <jsp:param name="MagAssRitorno" value="siap.sige.provvedimento.action.ActLoadEmissioneOrdinanzaNDPNLP"/>
    </jsp:include>

    </tr>

      <jsp:include page="<%=ICostantiAvvocatoFascicoloSige.PG_INCLUDE_AVVOCATI%>">
      <jsp:param name="AvvRitorno" value="siap.sige.provvedimento.action.ActLoadEmissioneOrdinanzaNDPNLP"/>
      </jsp:include>

  </table>

<%if (modifica) { %>
   <table width="90%">
    <tr>
      <td class="Titolo">Oggetti</td>
      <td class="Titolo">Esito</td>
    </tr>

    <tr>
  			<div id="elenco1" style="width: 100%; display:block">
  				<jsp:include page="/jsp/files/siap/sige/tenore/ElencoDettaglioTenori.jsp"/>
  			</div>
		</tr>
  </table>

<%} %>


  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadEmissioneOrdinanzaNDPNLP">
  
    <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Data Emissione<font class="ob">(*)</font></td>
      <td class="L">
        <input type="text" size="2" maxlength="2" name="<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" value = "<%=lDataEmiGG%>"> /
        <input type="text" size="2" maxlength="2" name="<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  value = "<%=lDataEmiMM%>"> /
        <input type="text" size="4" maxlength="4" name="<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" value = "<%=lDataEmiAA%>">

		<!-- MEV 15 - Revisione SIGE -->
		<a href="javascript:calendario('LoadEmissioneOrdinanzaNDPNLP','<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE%>','<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>');">
      	    <img src="/images/calendario.gif" border=0>
       	</a>
      </td>
    </tr>
<%if (!modifica){ %>
    <tr>
  		<td class="L">
  		  <table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
  			<tr>
    		 <td class="label" width=15% colspan=2>
             			<%-- 20190519 [SG]: aggiunta gestione idUdienzaSige --%>
<%-- 						<a class="cliccabile" href="<%=lLinkOggettiSessione%>">Oggetti</a> --%>
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
<%} %>
  </table>
  
  <%-- Si visualizzano i dati del Collegio. --%> 
  <%-- br>
 		<jsp:include page="<%=ICostantiProvvedimentoSige.INC_TIPO_GIUDIZIO_COLLEGIO%>"/--%>

  <%-- La sezione è stata adeguata alla maschera dell'ordinanza ordinaria
  <table cellspacing=2 cellpadding=2 width="95%">
  	<tr>
      <td class="Titolo" colspan=8 > Definizione Tipo Giudizio del Procedimento </td>
 		</tr>
  
  	<tr>
    	<td class="label">Tipo Rito &nbsp;
      	<select title="Tipo Rito" name="<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>" onchange="Visualizza (this.value)">
        	<%=tipoGiudizio%>
      	</select>
    	</td>
   	</tr>
	</table>

  	<jsp:include page="<%=ICostantiUdienzaCollegiale.PG_INCLUDE_INSERISCI_COLLEGIO%>">
          <jsp:param name="form_name" value="LoadEmissioneOrdinanzaNDPNLP"/>
          <jsp:param name="tipo_ufficio" value="<%=tipoUfficioUtente%>"/>
     </jsp:include>
  --%>
  
    <%-- Modifica del 08/03/2017 --%>
    <jsp:include page="<%=ICostantiProvvedimentoSige.INC_TIPO_GIUDIZIO_COLLEGIO_ORDINANZA_NDP_NLP%>" />
 
  <%-- Motivivazioni ed eventuale trasmissione/restituzione atti --%> 
  <table cellspacing="2" cellpadding="2" width="95%">
	  <tr>
	    <td class="Titolo" colspan="2" >Motivazioni</td>
	  </tr>
	  <tr>
	    <td class="l" colspan="2">
	      <%-- <Textarea Title="Motivazioni" name="<%=ICostantiProvvedimentoSige.CAMPO_NOTE %>"; cols=123 rows=4><%=lMotivazione%></textarea>  --%>
		  <Textarea Title="Motivazioni" name="<%=ICostantiMotivazioneProvvedimento.CAMPO_DESCR_MOTIVAZIONE %>" cols="123" rows="4"> <%=lAltraMotiv%></textarea>
	    </td>
	  </tr>

    <tr>
    	<td class="label" colspan=2> Dispone:</td>
    </tr>

    <tr>
    	<td class="l"><input value="C1" type="radio" name="<%=ICostantiMotivazioneProvvedimento.CAMPO_CK_01%>" <%if(lCodTipoMotivazione.equalsIgnoreCase("C1")){%> checked <%}%>></td>
      <td class="l">Restituzione degli atti</td>
    </tr>

    <tr>
      <td class="l"><input value="C2" type="radio" name="<%=ICostantiMotivazioneProvvedimento.CAMPO_CK_01%>"<%if(lCodTipoMotivazione.equalsIgnoreCase("C2")){%> checked <%}%>></td>
      <td class="l">Trasmissione degli atti</td>
    </tr>

    <tr>
      <td class="l"><input value="C0" type="radio" name="<%=ICostantiMotivazioneProvvedimento.CAMPO_CK_01%>" <% if(lCodTipoMotivazione.equalsIgnoreCase("C0")){%> checked <%}%> > </td>
      <td class="l">
        <input title="AltraMotivazione" name="<%=ICostantiMotivazioneProvvedimento.CAMPO_ALTRA_MOTIVAZIONE%>" value="<%=lDescMotiv%>" size="60" maxlength="60" >
      </td>
    </tr>

    <tr>
       <td class="l">Ufficio Competente</td>
       <td class="L">
         <select title="tipoUfficioCompetente" class=small name="<%=ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO%>">
           <%=tipoUfficioCompetente%>
         </select>

          <input Title="Sede Procura" name="<%=ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO%>" type="text" maxlength="35" size="35"  value="<%=lSedeUDS%>">
             <a href="Javascript:ListaComuni('LoadEmissioneOrdinanzaNDPNLP','<%=ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO%>',LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO%>[document.LoadEmissioneOrdinanzaNDPNLP.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO%>.selectedIndex].value);">
             <img src="/images/filefolder.gif" border=0> </a>
       </td>
     </tr>	  
  </table>
  <br>
  
  <table cellspacing="2" cellpadding="2">
		<tr>	  
    	<td class="label" colspan="3">In caso di indulto ed in presenza di misure di sicurezza:&nbsp;
    </tr>
		<tr>	  
    	<td class="l">Ufficio di Sorveglianza Competente&nbsp;</td>

      <td class="l" >
        <font class="campo">
          <input Title="Magistrato di sorveglianza Competente" name="<%=ICostantiProvvedimentoSige.SEDE_MAGISTRATO_COMPETENTE %>" size=35 type="text" onChange="pulisciId();" value="<%=lSedeUDS%>">
          <a href="Javascript:ListaUDS('LoadEmissioneOrdinanzaNDPNLP','<%=ICostantiProvvedimentoSige.SEDE_MAGISTRATO_COMPETENTE%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </font>
      </td>
    </tr>
  </table>

  <table cellspacing="2" cellpadding="2">
	<tr><td>&nbsp;</td></tr>
  <tr>
    <td>
      <input class="bottone" type="submit" value="Conferma">
    </td>
  </tr>
  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
<%-- 20190506 [SG]: aggiunto campo nascosto --%>
<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" value="">
<%-- 20190519 [SG]: aggiunta gestione idUdienzaSige --%>
<input type="HIDDEN" name="idUdiSig" value="">

<%if (modifica) { %>
 <input type="HIDDEN" name="<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>" value="<%=ProvvedimentoEvento.getProvvedimento().getIdProvvedimentoSige().toString()%>">
 <input type="HIDDEN" name="<%=ICostantiProvvedimentoSige.CAMPO_ID_EVENTO_GENERATO%>" value="<%=ProvvedimentoEvento.getProvvedimento().getIdEventoGenerato()%>">
<%} %>

  </FORM>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadEmissioneOrdinanzaNDPNLP");
    frmvalidator.addValidation("<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno  della Data Emissione è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese  della Data Emissione è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno della Data Emissione è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

  </body>
</html>
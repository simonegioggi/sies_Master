<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sige.magistrato.action.ICostantiMagistrato"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.web.RedirectTo"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
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
<%@ page import="f3b.web.html.Option"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@page import="siap.sige.motivazioneprovvedimento.model.MotivazioneProvvedimentoSigeModel"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel" %>
<%@ page import="siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@page import="siap.sige.motivazioneprovvedimento.action.ICostantiMotivazioneProvvedimento"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>  

<jsp:useBean id="FascicoloSigeEsteso" 			scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="tipoUfficioUtente"  			scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoGiudizio"  				scope="request" class="java.lang.String"/>
<jsp:useBean id="TenoriSige" 					scope="session" class="java.util.Vector"/>
<jsp:useBean id="tenori" 						scope="session" class="java.util.Vector"/>
<jsp:useBean id="avvocato"						scope="request" class="java.util.Vector" />
<jsp:useBean id="TornaQui" 						scope="request" class="java.lang.String" />
<jsp:useBean id="modalita"  					scope="request" class="java.lang.String"/>
<jsp:useBean id="ProvvedimentoEvento"			scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />
<jsp:useBean id="provvDaSospendere"				scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />
<jsp:useBean id="tipoAutorita"  				scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti1" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="luogodet"      				scope="request" class="siap.sige.detenzione.model.FasSigeDetenzioneModel"/>
<jsp:useBean id="UdienzaSige"	      			scope="request" class="siap.sige.udienza.model.UdienzaSigeModel"/>
<jsp:useBean id="ufficioCompetenteCorteSuprema" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoDest" 			  			scope="request" class="java.lang.String" />
<jsp:useBean id="DescrLuogoDetenzione" 			scope="request" class="java.lang.String" />
<jsp:useBean id="tipoAutoSogg" 			  		scope="request" class="java.lang.String" />
<jsp:useBean id="decrTipoAutoSogg" 			  	scope="request" class="java.lang.String" />
<jsp:useBean id="sedeAuto" 			  			scope="request" class="java.lang.String" />
<jsp:useBean id="tipoAuto" 			  			scope="request" class="java.lang.String" />
<jsp:useBean id="decrTipoAuto" 			  		scope="request" class="java.lang.String" />



<%
	String isVALIGN = "top";
	String isBorder = "0";
	String lWidth = "96%";
    String lSedeUffCompCorteSuprema = "";
    String lCodTipoUffCompCorteSuprema = "-";   
    
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

	/* Estrazione della data di emissione dell'ordinanza da sospendere */
	String lDataEmissioneOrdSospesa = (DateUtils.getDateToString (provvDaSospendere.getProvvedimento().getDataEmissione() , "dd/MM/yyyy"));
	
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
	lRedir.setParameter("PopUp", "yes");
	lRedir.setParameter("TornaQui", TornaQui);
	String lLinkUdienzaCollegiale = lRedir.toString();
	
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza di Sospensione </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript">
    var desktop;
    // Chiamata funzione lista dei comuni.
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    </script>
    <script language="JavaScript">
    var desktop;
    function ListaUffici(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
  </script>
    
	<script language="JavaScript">
       
        function Init() {
        	<%-- [SG] 20190312: risolto errore js --%>
<%--         	<% if (defTipoGiudizio) { %> --%>
<%--         		Visualizza(document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value); --%>
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
				
        function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio) {
      		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
        }
        
    </script>

    <script language="JavaScript">
    function Verify()
    {
        var ritorno = true;
        //alert("Verify");
        var data_udienza = '<%=lDataUdienza%>';
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
        var data_emissione_ord_sospesa = '<%=lDataEmissioneOrdSospesa%>';

      // Controllo della data emissione.
      var data_emissione = document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE%>.value;
	 
	 		// controllo Magistrato
	 		ritorno = Verifica();


	 	    // Controlla che sia inserito il destinatario per il soggetto
	 	    <%if (luogodet.getLuogoDetenzione()== null || luogodet.getLuogoDetenzione().getIdLuogoDetenzione() == null) 
	 	    {%>
	 	      if (   document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiUdienzaSige.CAMPO_COD_IST_DETENZIONE%>.value == "-"
	 	          || document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiUdienzaSige.CAMPO_COD_IST_DETENZIONE%>.value == ""
	 	          || document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiUdienzaSige.CAMPO_COD_LUOGO_DETENZIONE%>.value == "" )
	 	          {
	 	             alert('Scegliere Autorità di Destinazione e Sede per il destinario Soggetto!');
	 	             return false;
	 	          }
	 	    <%}%>
	 		 		 
     if (ritorno && ! ControllaData(data_emissione))
      {
        alert('Data emissione non valida!');
        return false;
      }
      // Controllo data di sistema >= Data Emissione .
      else if( !CompareDate( data_emissione, data_sistema) )
      {
        alert('Data Emissione maggiore della Data di sistema!');
        return false;
      }
      // Controllo della data deposito <= data  di udienza
     //alert("data_udienza ->" + data_udienza);
      else if ( ( ControllaData(data_emissione)) && ( !CompareDate( data_udienza, data_emissione) ) )
      {
        alert('Data Emissione minore della Data di Udienza!');
        return false;
      }
      // Controllo della data di emissione ordinanza sospesa <= data  di emissione
      else if ( ( ControllaData(data_emissione)) && ( !CompareDate( data_emissione_ord_sospesa, data_emissione) ) )
      {
        alert('Data Emissione Ordinanza da Sospendere maggiore della Data Ordinanza di Sospensione !');
        return false;
      }
     
	 if ( document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFF_COMP_CORTE_SUPREMA%>.value == "-"
	        || document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFF_COMP_CORTE_SUPREMA%>.value == "" )
	 {
	     	alert("La Sede dell'Ufficio Competente è obbligatoria");
	      	return false;
	 }
	 
<%	if (defTipoGiudizio) { %>
     //Controllo obbligatorietà Collegio in caso di Tipo Giudizio impostato.
    	if ( document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value != "-" )
    	{
    	if (document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value == "C")
    		ritorno = checkObblCollegio();
  	<%}%>
     	return ritorno;
    }
     
    	// Check Obbl. Collegio.
		function checkObblCollegio()
		{
			var ritorno = true;
			var collegio = document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>.value;
			if ( collegio == '' ) 
	    {
	    	alert("Collegio obbligatorio.");
	      ritorno = false;
	    }
				return ritorno;  
		}
  }

    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
    {
      desktop = 
          window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }
    
    //
    // Funzione JS per chiamta azione inserimento udienza
    //   
	function InserisciUdienza( tipoRito ){
		var dataUdienza= document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+
                         document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value +'/'+ 
                         document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value;
    	
    	var lLink;
		if( tipoRito == 'M' ){
			lLink = "<%=lLinkUdienzaMonocratica%>";
			// 20190506 [SG]: resize
			desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=750,height=500" );
		} else if( tipoRito == 'C' ){
			var idUdienza=document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE %>.value;
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
		// 20190517 [SG]: aggiunto controllo preventivo se dalla popup torno indietro senza aver inserito
		if (dataUdienza != "null") {
	     var dataSplitted=dataUdienza.split("-");
	     document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA %>.value=dataSplitted[0];
	     document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA %>.value=dataSplitted[1];
	     document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA %>.value=dataSplitted[2];
	     document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE %>.value=idUdienza;
	     document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>.value=idCollegio;
	     	if (codMagistrato != null) {
				document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value = codMagistrato;
	}
		}
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
        String lAction = new String();


//      Switch tra Emissione e Modifica
     boolean modifica = false;
     lAction = "siap.sige.provvedimento.action.ActInserisciOrdinanzaSospensione";
     String lNomeFunzione = "Emissione Ordinanza di Sospensione precedente Ordinanza";
     String lDataEmiGG = "", lDataEmiMM = "", lDataEmiAA = "";
     String lMotivazione = "";
    
     if (modalita.equalsIgnoreCase("M"))
     {
     	modifica = true;
         lAction = "siap.sige.provvedimento.action.ActModificaOrdinanzaSospensione";
         lNomeFunzione = "Modifica Ordinanza di Sospensione precedente Ordinanza";
         lDataEmiGG = DateUtils.getDateToString(ProvvedimentoEvento.getProvvedimento().getDataEmissione(),"dd");
         lDataEmiMM =  DateUtils.getDateToString(ProvvedimentoEvento.getProvvedimento().getDataEmissione(),"MM");
         lDataEmiAA = DateUtils.getDateToString(ProvvedimentoEvento.getProvvedimento().getDataEmissione(),"yyyy");
         if (ProvvedimentoEvento.getProvvedimento().getNote() != null)
         	lMotivazione = ProvvedimentoEvento.getProvvedimento().getNote();
         if (ProvvedimentoEvento.getProvvedimento().getCodUffCompCorteSuprema() != null && ProvvedimentoEvento.getProvvedimento().getCodUffCompCorteSuprema().trim().length() > 0)
   	  	 {
         	lSedeUffCompCorteSuprema = UfficioUtils.getUfficioByCodUfficio(ProvvedimentoEvento.getProvvedimento().getCodUffCompCorteSuprema()).getDescrComune();
         	lCodTipoUffCompCorteSuprema = UfficioUtils.getUfficioByCodUfficio(ProvvedimentoEvento.getProvvedimento().getCodUffCompCorteSuprema()).getCodTipoUfficio();
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
    </table>
    <table>
    <tr>
    <jsp:include page="<%=ICostantiMagistratoAssegnatario.PG_SINTESIMAGISTRATOASSEGNATARIO%>">
          <jsp:param name="MagAssRitorno" value="siap.sige.provvedimento.action.ActLoadEmissioneOrdinanzaSospensione"/>
    </jsp:include>

    </tr>
	  
      <jsp:include page="<%=ICostantiAvvocatoFascicoloSige.PG_INCLUDE_AVVOCATI%>">
      <jsp:param name="AvvRitorno" value="siap.sige.provvedimento.action.ActLoadEmissioneOrdinanzaSospensione"/>
      </jsp:include>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadEmissioneOrdinanzaSospensione">

   <table cellspacing=2 cellpadding=2 width="95%">
  	<tr>
      <td class="Titolo" colspan=8 > Provvedimento di Sospensione </td>
 		</tr>
    </table>
    <table>
    <tr>
       <td class="crosso">Sospensione Ordinanza n. <%=provvDaSospendere.getProvvedimento().getChiaveProgr()%>/<%=provvDaSospendere.getProvvedimento().getChiaveAnno()%> del <%=DateUtils.getDateToString(provvDaSospendere.getProvvedimento().getDataDeposito(), "dd-MM-yyyy")%></td>
    </tr>
    </table>
   
    <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Data Emissione<font class="ob">(*)</font></td>
      <td class="L">
        <input type="text" size="2" maxlength="2" name="<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" value = "<%=lDataEmiGG%>"> /
        <input type="text" size="2" maxlength="2" name="<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  value = "<%=lDataEmiMM%>"> /
        <input type="text" size="4" maxlength="4" name="<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" value = "<%=lDataEmiAA%>">
		
		<!-- MEV 15 - Revisione SIGE -->
		<a href="javascript:calendario('LoadEmissioneOrdinanzaSospensione','<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE%>','<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>');">
      	    <img src="/images/calendario.gif" border=0>
       	</a>
      </td>
    </tr>

    <tr>
  		<td class="L">
  		  <table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
  			<tr>
    		 <td class="label" width=15% colspan=2>
             	<a class="cliccabile" href="<%=lLinkOggettiSessione%>">
       	 			Oggetti
      			</a>
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
   

  
  <%-- Si visualizzano i dati del Collegio. --%> 
  <%-- br>
 		<jsp:include page="<%=ICostantiProvvedimentoSige.INC_TIPO_GIUDIZIO_COLLEGIO%>"/--%>

  <table cellspacing=2 cellpadding=2 width="95%">
  	<br/>
  		<jsp:include page="<%=ICostantiProvvedimentoSige.INC_TIPO_GIUDIZIO_COLLEGIO_SOSP_PREC_ORDINANZA%>" /> 
  	<br/>
  </table>

  <%-- Motivi di Sospensione ed Eventuale trasmissione/restituzione atti --%> 
  <table cellspacing="2" cellpadding="2" width="95%">
	  <tr>
	    <td class="Titolo" colspan=6>Motivazioni</td>
	  </tr>
	  <tr>
	    <td class="l" colspan=6>
	      <Textarea Title="AltreMotivazioni" name="<%=ICostantiMotivazioneProvvedimento.CAMPO_DESCR_MOTIVAZIONE_SOSPENSIONE %>" cols=123 rows=4> <%=lMotivazione%></textarea>
	    </td>
	  </tr>
  </table>
  
  <table cellspacing="2" cellpadding="2" width="95%">  
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
	          <a href="Javascript:ListaUfficiPerTipo('LoadEmissioneOrdinanzaSospensione','<%=ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFF_COMP_CORTE_SUPREMA%>',document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_UFF_COMP_CORTE_SUPREMA%>[document.LoadEmissioneOrdinanzaSospensione.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_UFF_COMP_CORTE_SUPREMA%>.selectedIndex].value);">
	            <img src="/images/filefolder.gif" border=0>
	          </a>
	        </td>
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
        <div align="left"><a><img name="imageDestina" src="/images/expand.gif"  onClick="return effettoTree();" alt="" border=0></a>Destinatari </div>
      </td>
    </tr>

    <tr id="frameDestina" style="display:none"> 
      <td> 
        <table>

		    <tr>
		      <td class="l" colspan=6>
		        Per la comunicazione al <%=TipoDest%> <input type="checkbox" checked name="<%= ICostantiUdienzaSige.CAMPO_PROCURA_GENERALE %>">
		      </td>
		    </tr>
			<tr><td colspan=6>&nbsp;</td></tr>
		    <tr>
		      <td class="l" colspan=6>
		        Per la notifica al Soggetto
		      </td>
		    </tr>
		
		<%    
				if (luogodet.getLuogoDetenzione() == null || luogodet.getLuogoDetenzione().getIdLuogoDetenzione() == null  || luogodet.getLuogoDetenzione().getDataFineDetenzione() != null || luogodet.getLuogoDetenzione().getIstitutoDetenzione() == null ) {%>
		       <tr>
		        <td class="l">Autorità Destinazione <font class="ob">(*)</font></td>
		        <td class="l">
		          <select title="Destinatario" name="<%=ICostantiUdienzaSige.CAMPO_COD_IST_DETENZIONE%>">
		          <%if(tipoAutoSogg!= null){ %>
		             <option value = "<%=tipoAutoSogg %>" SELECTED ><%= decrTipoAutoSogg %></option>
		             <%} %>
		            <%= tipoAutorita %>
		          </select>
		        </td>
		       </tr>
		       <tr>
		        <td class="l">Sede <font class="ob">(*)</font></td>
		        <td class="l">
		           <input Title="Sede " name="<%=ICostantiUdienzaSige.CAMPO_COD_LUOGO_DETENZIONE%>"
		              value="<%=DescrLuogoDetenzione %>" type="text" maxlength="35" size="35">
		              <a href="Javascript:ListaComuni('LoadEmissioneOrdinanzaSospensione','<%=ICostantiUdienzaSige.CAMPO_COD_LUOGO_DETENZIONE%>');">
		              <img src="/images/filefolder.gif" border=0> </a>
		        </td>
		       </tr>
		          <tr>
		            <td class="l">Indirizzo</td>
		            <td class="L" colspan=3>
		             <input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
		            </td>
		          </tr>
		   <%}else{%>
		      <tr>
		        <td class="l">Tipo Istituto</td>
		        <td class="l">
		        <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(luogodet.getLuogoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(luogodet.getLuogoDetenzione().getDescrLuogo())%>" size=50>
		        <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=luogodet.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
		        <a href="Javascript:ListaIstitutoDetenzione('LoadEmissioneOrdinanzaSospensione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
		        <img src="/images/filefolder.gif" border=0></a>
		        <input type="hidden" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" >
		        </td>
		      </tr>
		   <%}%>
				<tr><td colspan=6>&nbsp;</td></tr>
		<%
		        Iterator itxAvv = avvocato.iterator();
		        int num_sede = 0;
		        while ( itxAvv.hasNext())
		        {
		         AvvocatoSigeModel lAvv = (AvvocatoSigeModel)itxAvv.next();
		%>
		         <tr>
		           <td class=l colspan=6>Per la notifica all' avvocato <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getAvvocato().getNome(),"-")%>  Foro di <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo(),"-")%> Difensore <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo(),"-")%></td>
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
		           <input Title="Sede Procura" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
		              value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo(),"-")%>" type="text" maxlength="35" size="35">
		              <a href="Javascript:ListaUffici('LoadEmissioneOrdinanzaSospensione','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=num_sede%>]');">
		              <img src="/images/filefolder.gif" border=0> </a>
		      </td>
		    </tr>
		      <tr>
		        <td class="l">Indirizzo</td>
		        <td class="L" colspan=3>
		         	<input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
		 			 		<input type="hidden" name="<%=ICostantiUdienzaSige.CAMPO_COD_AVVOCATO%>" value="<%=lAvv.getAvvocatoFascicoloSigeModel().getIdAvvocatoFascicoloSige()%>" >
		        </td>
		      </tr>
		<%
		      num_sede++;
		     }
		%>
				<tr><td colspan=6>&nbsp;</td></tr>
		
		    <tr>
		      <td class=l colspan=6>Per la  <input type='radio' name="<%=ICostantiUdienzaSige.CAMPO_TIPONOTIFICA%>" value='N' >Notifica /<input type='radio' name="<%=ICostantiUdienzaSige.CAMPO_TIPONOTIFICA%>" value='C' checked>Comunicazione ad altro destinatario</td>
		    </tr>
		
		    <tr>
		      <td class="l">Destinatario</td>
		      <td class="l">
		        <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
		        <%if(tipoAuto!= null){ %>
		             <option value = "<%=tipoAuto %>" SELECTED ><%= decrTipoAuto %></option>
		             <%} %>
		          <%= tipoAutorita %>
		        </select>
		      </td>
		    </tr>
		
		    <tr>
		      <td class="l">Sede </td>
		      <td class="l">
		         <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
		            value="<%=sedeAuto %>" type="text" maxlength="35" size="35">
		            <a href="Javascript:ListaComuni('LoadEmissioneOrdinanzaSospensione','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=num_sede%>]');">
		            <img src="/images/filefolder.gif" border=0> </a>
		      </td>
		    </tr>
		    <tr>
		      <td class="l">Indirizzo</td>
		      <td class="L" colspan=3>
		       <input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
		      </td>
		    </tr>
		    <tr></tr>
				<tr><td colspan=6>&nbsp;
		    	<input type="hidden" name="<%=ICostantiUdienzaSige.CAMPO_COD_AVVOCATO%>" value="" >
				</td></tr>
		  
		</table>
      </td>
    </tr>
	
	<tr><td>&nbsp;</td></tr>	  
		  
	<tr>
	    <td>
	      <input class="bottone" type="submit" value="Conferma">
	    </td>
	</tr>
		  
  </table>

 	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
 	<input type="HIDDEN" name="<%=ICostantiProvvedimentoSige.CAMPO_PROVV_ID_PROVVEDIMENTO_SIGE%>" value="<%=provvDaSospendere.getProvvedimento().getIdProvvedimentoSige().toString()%>">
 	<input type="HIDDEN" name="<%=ICostantiMotivazioneProvvedimento.CAMPO_DESCR_MOTIVAZIONE%>" value="">
	<%-- 20190506 [SG]: aggiunto campo nascosto --%>
  	<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" value="">

<%
if (modifica) { 
%> 
 <input type="HIDDEN" name="<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>" value="<%=ProvvedimentoEvento.getProvvedimento().getIdProvvedimentoSige().toString()%>">
 <input type="HIDDEN" name="<%=ICostantiProvvedimentoSige.CAMPO_ID_EVENTO_GENERATO%>" value="<%=ProvvedimentoEvento.getProvvedimento().getIdEventoGenerato()%>">
 <input type="HIDDEN" name="<%=ICostantiProvvedimentoSige.CAMPO_COD_TIPO_PROV_SIGE%>" value="<%=ICostantiProvvedimentoSige.COD_ORDINANZA_SOSPENSIONE%>">
<%} %>

  </FORM>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadEmissioneOrdinanzaSospensione");
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
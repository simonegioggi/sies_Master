<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sige.aula.model.AulaUdienzaModel"%>
<%@ page import="siap.sige.aula.action.ICostantiAula"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.collegio.util.CollegioUtils"%>
<%@ page import="siap.sige.collegio.model.CollegioModel"%>
<%@ page import="siap.sige.sezione.action.ICostantiSezione"%>
<%@ page import="siap.sige.sezione.model.SezioneModel"%>
<%@ page import="siap.sige.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.giudicepopolare.action.ICostantiGiudicePopolare"%>
<%@ page import="siap.sige.giudicepopolare.model.GiudicePopolareModel"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="udienzasige" scope="request"  class="siap.sige.udienza.model.UdienzaSigeModel"/>
<jsp:useBean id="magistratiArray" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="procuraDesc" scope="request" class="java.lang.String"/>
<jsp:useBean id="giudiciPopolari" scope="request" class="java.lang.String"/>

<jsp:useBean id="elencoProcuratori" scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoAssistenti"  scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoSezioni" scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoCodiciCollegi" scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoGiudiciPopolari" scope="request" class="java.util.Vector"/>
<jsp:useBean id="PopUp" scope="request" class="java.lang.String"/>
<%-- 20171004: [SG] aggiungo useBean per impostare la data udienza --%>
<jsp:useBean id="dataUdienzaImpostata" scope="request" class="java.lang.String"/>
<!-- aggiungo pe intervento 11.2.1 -->
<jsp:useBean id="numProcePerUdienza" scope="request"  class="java.lang.String"/>
<jsp:useBean id="aulaUdienza"        scope="request"  class="siap.sige.aula.model.AulaUdienzaModel" />
<jsp:useBean id="indirizzoUfficio"   scope="request"  class="java.lang.String"/>
<jsp:useBean id="magAssegnatario"    scope="request"  class="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel" />


<% 
String readonly = ""; 
String disSezAula="";
//<!-- intervento per 11.1.2 -->
if ( "M".equals(modalita) && !numProcePerUdienza.equals("0") ){ 
  readonly  = "disabled=disabled";
  disSezAula= "disabled=disabled";
} 

//<!-- intervento per 11.1.2 -->
AulaUdienzaModel lAulaUdienza = new AulaUdienzaModel();
if (aulaUdienza!=null){
	lAulaUdienza = aulaUdienza;
}
%> 

<html>
<head>
  <title>[S.I.E.S.] Gestione Udienza Collegiale Fix</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript">
	var desktop;

	// init per focus sul primo campo
	function init()  	{
		document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.focus();
	}
	
	function calendario(a_formname,a_field_year,a_field_month,a_field_day)    {
		desktop = window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
	}

	function listaMagistrati(a_formname, a_idfieldnum) {
	 	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sige.magistrato.action.ActLoadRicercaMagistratoAssegnazioneLista&formname="+a_formname+"&idfieldnum="+a_idfieldnum,
	 			"Ricerca_Magistrato", "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
	}

	// Verifica della data Udienza.
	function checkDataUdienza()		{
		var ritorno = true;
		var dataUdienza = 
			document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+ 
			document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value +'/'+ 
			document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value;
        											
		if (dataUdienza.length > 2) {
			if (!ControllaData(dataUdienza)) {
				alert('Data udienza non corretta.');
				document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.focus();
				ritorno = false;
			}
		} else {
			alert('Data udienza obbligatoria.');
			document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.focus();
			ritorno = false;
		}
			return ritorno;
	}

	// Check Obbl. Collegio.
	function checkObblCollegio() {
		var ritorno = true;				
		if ( document.LoadInserisciUdienzaCollegiale.<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>.value == '' ) {
    		alert("Collegio obbligatorio.");
    		//document.LoadInserisciUdienzaCollegiale.<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>.focus();
      		ritorno = false;
    	}
	  
		return ritorno;  
	}

	/*controllo correttezza di un campo di tipo ora */
	function controlloOra(ora, minuti)
	  {
	  	//alert ("controlloOra: " + ora);
	    var ritorno = false;
	    //alert ("ora ->" + ora);
	    // L'ora è opzionale
	    if( ora == "")
	    	ritorno = true;
	    else if (ora >= 0 && ora < 24)
	      ritorno = controlloMinuti(minuti);
	    else
	      alert("L'ora deve essere espressi da un numero compreso tra 0 e 23");
	    return ritorno;
	    
	 }

    /* controllo correttezza di un campo di tipo minuti */
 	function controlloMinuti(min)
  	{
       //alert ("controlloMinuti: " + min);
       var ritorno = false;
       //alert ("minuti ->" + min);
       if (min == "")
           ritorno = true;
       else if (min >= 0 && min < 60)
           ritorno = true;
       else
           alert("I minuti devono essere espressi da un numero compreso tra 0 e 59");
       return ritorno;
 	}
    
	// Esecuzione delle funzioni di verifica.
	function Verify()    {  
		// Controllo della data Udienza.  
		if( !checkDataUdienza() )
			return false;
		
    	// Controllo Obbligatorietà Collegio.  
    	//if( !checkObblCollegio() )
      	//	return false;

    	   	//controllo orario inizio e fine
    	
    	var oraInizio = document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_ORA_INIZIO%>.value;    
        var minInizio = document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_MIN_INIZIO%>.value;
        var oraFine   = document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_ORA_FINE%>.value;
        var minFine   = document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_MIN_FINE%>.value;

  		// Controlla l'ora e minuti d'inizio
       
        if( !controlloOra(oraInizio, minInizio) )
          	return false;
        
        if( !controlloOra(oraFine,minFine) )
          	return false;

		return true;
	}

    function selezionaDeselezionaTutti()
    {
  	  if( document.LoadInserisciUdienzaCollegiale.flagTutti.checked == true ){
  		  if (typeof (document.LoadInserisciUdienzaCollegiale.<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>.length) == "undefined")
  	      {
  			document.LoadInserisciUdienzaCollegiale.<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>.checked = true;
  	      } else {
	    		  for (var i = 0; i < document.LoadInserisciUdienzaCollegiale.<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>.length; i++)
	        	  {
	    			  document.LoadInserisciUdienzaCollegiale.<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>[i].checked = true;
	        	  }
  	      }
    	  } 
  	  
  	  if( document.LoadInserisciUdienzaCollegiale.flagTutti.checked == false ){
  		  if (typeof (document.LoadInserisciUdienzaCollegiale.<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>.length) == "undefined")
  	      {
  			document.LoadInserisciUdienzaCollegiale.<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>.checked = false;
  	      } else {
    		  for (var i = 0; i < document.LoadInserisciUdienzaCollegiale.<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>.length; i++)
        	  {
    			  document.LoadInserisciUdienzaCollegiale.<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>[i].checked = false;
        	  }
  	      }
      } 
    }

    function confermaBack () {
        try {
    	    var dataUdienza='<%=DateUtils.getDateToString(udienzasige.getDataUdienza(), "dd-MM-yyyy") %>';
        	var idUdienza=<%=udienzasige.getIdUdienzaSige() %>;
        	var idCollegio=<%=(udienzasige.getCollegio()!=null?udienzasige.getCollegio().getIdCollegio():"") %>
        	window.opener.setUdienza (idUdienza, dataUdienza, idCollegio);
        	self.close();
        } catch (e) {
        	history.back();	
        }
    }
    
    
    //aggiungo la funzione per intervento nuove richieste della 11.1.2 
    function ListaAule(a_formname, a_fieldval) {
  	if ( a_fieldval == "" || a_fieldval == "-" ) {
  		alert('Selezionare una sezione!');
      } else {
  		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.aula.action.ActRicercaAula&<%=IWebConstants.POPUP_PAGE%>=yes&formname="+a_formname+"&<%=ICostantiAula.CAMPO_ID_SEZIONE%>="+a_fieldval, "Ricerca_Aule", "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
    }
    
    //aggiungo la funzione per intervento nuove richieste della 11.1.2 
    function svuotaAula() {    	
      document.LoadInserisciUdienzaCollegiale.<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>.value=document.LoadInserisciUdienzaCollegiale.SezIdSezione_CBX.value;         		 
  	  document.LoadInserisciUdienzaCollegiale.<%=ICostantiAula.CAMPO_DESCRIZIONE_AULA%>.value='';
  	  document.LoadInserisciUdienzaCollegiale.<%=ICostantiAula.CAMPO_DESCRIZIONE_INGRESSO%>.value='';
  	  document.LoadInserisciUdienzaCollegiale.<%=ICostantiAula.CAMPO_NUMERO_PIANO%>.value='';
  	  document.LoadInserisciUdienzaCollegiale.<%=ICostantiAula.CAMPO_ID_AULA%>.value='';
 	  document.LoadInserisciUdienzaCollegiale.<%=ICostantiAula.CAMPO_DESCRIZIONE_STANZA%>.value='';
    } 
    
    //aggiungo la funzione per intervento nuove richieste della 11.1.2 
	function VerifyConferma() {
    	
		var codMagAssegnatarioFascicolo = '<%=magAssegnatario.getMagCodMagistrato()%>';				
		var found = false;
    	var listaMagCollegio = document.LoadInserisciUdienzaCollegiale.CodMagistrato.length; 
    	var countSelezionati = 0;
    	
    	// Controllo che Data udienza <= di Data Odierna
   	    var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
	    var dataUdienzaM = 
			document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+ 
			document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value +'/'+ 
			document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value;
	  
			// 31/10/2019: eliminiamo il controllo di blocco sulla data udienza (richiesta Da: Nunziata Alfieri <nunziata.alfieri@giustizia.it> 
		    // Inviato: giovedì 31 ottobre 2019 10:48) - ticket otrs 20191031015 
			
			/* if (dataUdienzaM!= '//' && !CompareDate(data_od, dataUdienzaM)) {
	           	alert('Udienza non modificabile e/o associabile! La Data Udienza è minore della Data Odierna!');           	
	           	return false;
	   		} */
		
    	// metto >=3 perchè ad esempio entrando come CAPSM, il collegio è composto da 5 giudici
    	if(listaMagCollegio >= 3){			
			// Ciclo for
			for( i=0; i<3; i++ ){
				var magCode = 
					document.LoadInserisciUdienzaCollegiale.CodMagistrato.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>[i].value;		
				if(magCode!=null && magCode!=""){ 
					countSelezionati = countSelezionati+1;					
				}				 
				// Verifica se almeno un magistrato selezionato sia l'assegnatario del fascicolo.	
				if (magCode == codMagAssegnatarioFascicolo) {
				    found = true;
				    break;
				}
								
			} // end for      			
			if(found==false && countSelezionati >= 3){
				alert("Attenzione! Il magistrato assegnatario del fascicolo deve essere uno dei componenti del collegio (Presidente, Giudice e Giudice)!");
				return false;
			}
    	}		  
    }
    
  </script>
</head>

<body class="corpo" onload="Javascript:init();">
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
	UdienzaSigeModel lUdienzaSige = new UdienzaSigeModel();
	lUdienzaSige.setCollegio( new CollegioModel() );
	lUdienzaSige.getCollegio().setSezione( new SezioneModel() );
	String lAzione = "";
	String idUdiSige = "";
	
	if( modalita.equals("I") ) {
		lAzione = "siap.sige.udienzacollegiale.action.ActInserisciUdienzaCollegiale";
		// 20171004: [SG] aggiunto set di proprietà
    	if (Utils.isPresent(dataUdienzaImpostata))
    		lUdienzaSige.setDataUdienza(DateUtils.getDate(dataUdienzaImpostata, "dd/MM/yyyy"));
	%>
        	<font class="campo">Inserimento Udienza Collegiale</font>
	<%
	} else if( modalita.equals("X") ) {
		lAzione = "siap.sige.udienzacollegiale.action.ActInserisciUdienzaCollegiale";
		lUdienzaSige = udienzasige;
	%>
        	<font class="campo">Inserimento per copia Udienza Collegiale</font>
	<%
	} else if( modalita.equals("M") ) {
		lAzione = "siap.sige.udienzacollegiale.action.ActModificaUdienzaCollegiale";
		lUdienzaSige = udienzasige;
		idUdiSige = udienzasige.getIdUdienzaSige().toString();
	%>
        	<font class="campo">Modifica Udienza Collegiale</font>
	<%
	}
	
	CollegioModel collegio = lUdienzaSige.getCollegio();
	BigDecimal idCollegio=null;
	String codCollegio="";
	BigDecimal idSezione=null;
	if (collegio != null) {
		idCollegio=collegio.getIdCollegio();
		codCollegio=collegio.getCodCollegio();
		idSezione=collegio.getSezIdSezione();
	}
	
	%>
      </td>
        <!-- BOTTONE DI RITORNO -->
        <td>
  			<%--<a href="javascript:self.close()">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
			</a> --%>

		<a href="javascript:confermaBack()">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
			</a>
		</td>	
    </tr>
  </table>

	<FORM method="POST" action="Main.jsp" name="LoadInserisciUdienzaCollegiale">
  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" />
  	<input type="HIDDEN" name="<%=ICostantiCollegio.FORM_DEF_COLLEGIO%>" value="yes" />
    <%-- Ticket#20210728014: errore di overflow su idCollegio.intValue(): è un BigDecimal 
  	<input type="HIDDEN" name="<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>" value="<%=(idCollegio==null?"":String.valueOf(idCollegio.intValue()))%>" />
    --%>  	
    <input type="HIDDEN" name="<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>" value="<%=(idCollegio==null ? "" : idCollegio)%>" />
  	<%-- Ticket#20210728014: FINE --%>


  	<input type="HIDDEN" name="<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>" value="<%=codCollegio%>" />
    <input type="HIDDEN" name="<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>" value="<%=idUdiSige%>" />
    <input type="HIDDEN" name="PopUp" value="<%=PopUp%>" />
	
  <table>
    <tr>
      <td class="l">Data Udienza <font class=ob>(*)</font></td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lUdienzaSige.getDataUdienza(),"dd"))%>" 
               name="<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>" 
               onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							 onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lUdienzaSige.getDataUdienza(),"MM"))%>" 
               name="<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>" 
               onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							 onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lUdienzaSige.getDataUdienza(),"yyyy"))%>" 
               name="<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>" 
               onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							 onBlur="javascript:value=FillYear(value)">
        
				<a href="javascript:calendario('LoadInserisciUdienzaCollegiale','<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>','<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>','<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>');">
       		<img src="/images/calendario.gif" border=0>
        </a>
      </td>
    </tr>
    
    <tr>
      <td class="l">Sezione</td>
      <td class="l">
      	<%-- 20170926: [SG] aggiunta funzione onchange --%>
        <select title="sezione" name="<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>_CBX"  onChange="svuotaAula();"  <%=readonly%> >
        	<%=elencoSezioni%>
        </select>
<%-- Ticket#20210728014: errore di overflow su idCollegio.intValue(): è un BigDecimal 
        <input type="HIDDEN" name="<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>" value="<%=(idSezione == null ? "" : String.valueOf(idSezione.intValue()))%>">
--%>
        <input type="HIDDEN" name="<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>" value="<%=(idSezione == null ? "" : idSezione)%>">
<%-- Ticket#20210728014: FINE --%>
      </td>
    </tr>
    

<%
if (magistratiArray!=null){
	for (int i = 0; i < magistratiArray.size(); i++) {
		String magDesc = (String)magistratiArray.get(i);
		// intervento per 11.2.1
		String cognomeMa = "";
		String nomeMa = "";
		String codMa = "";
		//if(magAssegnatario!=null && i==0){
			//cognomeMa = magAssegnatario.getMagistrato().getCognome();
			//nomeMa = magAssegnatario.getMagistrato().getNome();
			//codMa = magAssegnatario.getMagistrato().getCodMagistrato();
		//}
%>
    <tr>
      <td class="l"><%=magDesc%></td>
      <td class="l">
		<input title="Cognome" readonly type="text" 
					 name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25" 
					 value="<%=(collegio != null && CollegioUtils.isValidSize( collegio.getCollegioMagistrati(), i)) ? 
					     collegio.getCollegioMagistrati()[i].getMagistrato().getCognome() : cognomeMa %>">
		<input title="Nome" readonly type="text" 
					 name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="25" 
					 value="<%=(collegio != null && CollegioUtils.isValidSize( collegio.getCollegioMagistrati(), i)) ? 
					     collegio.getCollegioMagistrati()[i].getMagistrato().getNome() : nomeMa %>">
		<input title="CodMagistrato" type="hidden" 
					 name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" 
					 value="<%=(collegio != null && CollegioUtils.isValidSize( collegio.getCollegioMagistrati(), i)) ? 
					     collegio.getCollegioMagistrati()[i].getMagistrato().getCodMagistrato() : codMa %>">
		<a href="Javascript:listaMagistrati('LoadInserisciUdienzaCollegiale', '<%=i%>');"><img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>
<%
	}
}
%>

<%
if ("yes".equals(giudiciPopolari)) {
%>
    <tr>
        <td colspan="2">&nbsp;</td>
      </tr>
      <tr>
        <td class="Titolo" colspan="2">Giudici Popolari</td>
      </tr>
     <tr>
       <td colspan="2">
			<table width=100%>
				<tr>
					<td class="int">Nominativo Giudice Popolare</td>
					<td class="int">Ruolo</td>
					<td class="int">Azioni</td>
        		</tr>
			    <tr>
					<td class=c>Tutti</td>
					<td class=c>&nbsp;</td>
					<td class=c><input type="checkbox" name="flagTutti" value="1" onClick="Javascript:selezionaDeselezionaTutti();"></td>
				</tr>
<%
	if (elencoGiudiciPopolari!=null){
	Iterator itx = elencoGiudiciPopolari.iterator();
	while ( itx.hasNext()) {
		GiudicePopolareModel giudicepopolare = (GiudicePopolareModel)itx.next();

		String checked = "";
		if( collegio.getCodCollegio()!= null ) {

			if( collegio.getCollegioGiudiciPopolari()!= null ) {
				for( int i=0; i<collegio.getCollegioGiudiciPopolari().length; i++ ) {
					if (giudicepopolare.getIdGiudicePopolare().compareTo(collegio.getCollegioGiudiciPopolari()[i].getGiudicePopolare().getIdGiudicePopolare()) == 0) {
						checked = "checked";
						break;
					}
				}
			}

		} else {

			if ("T".equals( giudicepopolare.getCodRuolo())) {
				//checked = "checked";
			}

		}
		
%>
   	<tr>
   		<td class=c>
   			<%-- 20170908: [SG] aggiunto spazio tra nome e cognome --%>
   			<%=giudicepopolare.getCognome()%>&nbsp;<%=giudicepopolare.getNome()%>
   		</td>
      	<td class=c>
      		<%=StringUtils.toStringJSP(giudicepopolare.getDescrRuolo())%>
      	</td>
      	<td class=c>
      		<input type="checkbox" name="<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>" value="<%=giudicepopolare.getIdGiudicePopolare()%>" <%=checked%>/>
      	</td>
	</tr>
<%
	} // end while
	} // end if 
		%>
<!-- 	else { -->
<!-- 		%> -->
<!-- 	    <tr> -->
<!-- 	      <td class=c colspan=3>Elenco Vuoto</td> -->
<!-- 	    </tr> -->
<%-- 	<% --%>
<!-- // 	} -->
<%-- %> --%>
			</table>
		</td>
	</tr>
<%
} else {
%>
    <tr>
      <td class="l"><%=procuraDesc%></td>
      <td class="l">
        <select title="procuratore" name="<%=ICostantiUdienzaSige.CAMPO_COD_PROCURATORE%>" >
        	<%=elencoProcuratori%>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Cancelliere </td>
      <td class="l">
        <select title="cancelliere" name="<%=ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE%>" >
        	<%=elencoAssistenti%>
        </select>
      </td>
    </tr>
<%
}
%>

        <!-- inizio intervento per 11.1.2 (aggiungo aula) -->          
    <tr>
    	<td class="l" colspan="2">Aula <input type="text"
						name="<%=ICostantiAula.CAMPO_DESCRIZIONE_AULA%>"
						value="<%=StringUtils.toStringJSP(lAulaUdienza.getDescrizioneAula())%>"
						size="12" maxlength="30" readonly="readonly"  <%=disSezAula%>  >&nbsp;&nbsp;
						Ingresso <input type="text"
						name="<%=ICostantiAula.CAMPO_DESCRIZIONE_INGRESSO%>"
						value="<%=StringUtils.toStringJSP(lAulaUdienza.getDescrizioneIngresso())%>"
						size="30" maxlength="30"  readonly="readonly" <%=disSezAula%> >&nbsp;&nbsp;
						Stanza <input type="text"
						name="<%=ICostantiAula.CAMPO_DESCRIZIONE_STANZA%>"
						value="<%=StringUtils.toStringJSP(lAulaUdienza.getDescrizioneStanza())%>"
						size="30" maxlength="30"  readonly="readonly" <%=disSezAula%> >&nbsp;&nbsp;
						Piano <input type="text"
						name="<%=ICostantiAula.CAMPO_NUMERO_PIANO%>"
						value="<%=StringUtils.toStringJSP(lAulaUdienza.getNumeroPiano())%>"
						size="12" maxlength="30"  readonly="readonly" <%=disSezAula%> >&nbsp;&nbsp;
						<input type="HIDDEN" name="<%=ICostantiAula.CAMPO_ID_AULA%>"
						value="<%=StringUtils.toStringJSP(lAulaUdienza.getIdAula())%>">
						<%if(numProcePerUdienza.equals("0")) {%><a
							href="Javascript:ListaAule('LoadInserisciUdienzaCollegiale', document.LoadInserisciUdienzaCollegiale.<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>_CBX.value);"><img
								src="/images/filefolder.gif" border=0 ></a> 
						 <%} %>
		</td>
    </tr>
    <!-- inizio intervento per 11.1.2 (aggiungo luogo svolgimento) -->   
 	<tr>
      <td class="l">Luogo Svolgimento</td>
      <td class="l"> 
      <%if(lUdienzaSige.getLuogoUdienza() != null && !"".equals(lUdienzaSige.getLuogoUdienza())){ %>
        <input type="text" maxlength="100" size="50" 
               value="<%=StringUtils.toStringJSP(lUdienzaSige.getLuogoUdienza())%>"
               name="<%=ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA%>"  
               <%=readonly%>> 
      <%}else{ %>
      
       <input type="text" maxlength="100" size="50" 
               value="<%=indirizzoUfficio%>"
               name="<%=ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA%>"  
               <%=readonly%>> 
       <%} %>         
      </td> 
    </tr>
    <!-- inizio intervento per 11.1.2 (aggiungo orario) -->   
    <tr>
      <td class="l">Orario Inizio (ora:min)</td>
      <td class="l"> 
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(lUdienzaSige.getOraInizio())%>"
               name="<%=ICostantiUdienzaSige.CAMPO_ORA_INIZIO%>"  
               <%=readonly%> onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							 onBlur="javascript:value=FillDM(value)">
			:  
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(lUdienzaSige.getMinInizio())%>"
               name="<%=ICostantiUdienzaSige.CAMPO_MIN_INIZIO%>"  
               <%=readonly%>  onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							  onBlur="javascript:value=FillDM(value)">
    </tr>
	<!-- inizio intervento per 11.1.2 (aggiungo orario) -->
    <tr>
      <td class="l">Orario Fine (ora:min)</td>
      <td class="l"> 
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(lUdienzaSige.getOraFine())%>"
               name="<%=ICostantiUdienzaSige.CAMPO_ORA_FINE%>"  
               <%=readonly%>  onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							  onBlur="javascript:value=FillDM(value)"> 
				: 
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(lUdienzaSige.getMinFine())%>"
               name="<%=ICostantiUdienzaSige.CAMPO_MIN_FINE%>"  
               <%=readonly%>  onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							  onBlur="javascript:value=FillDM(value)">
    </tr>
    <tr>
      	<td>
        	<input class="bottone" type="submit" name="conferma" value="Conferma" onClick="javascript:return VerifyConferma();">
		</td>
	</tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadInserisciUdienzaCollegiale");

//================================================================
// Aggiungere le opportune chiamate al genvalidator 
//================================================================
//frmvalidator.addValidation("","req","Il campo XXXX è obbligatorio");
//frmvalidator.addValidation("","numeric","Il XXXX è un campo numerico");
//frmvalidator.addValidation("","maxlen=4","La lunghezza massima per XXXX è di 4 caratteri");
//frmvalidator.addValidation("","minlen=4","La lunghezza minima per XXXX è di 4 caratteri");
//frmvalidator.addValidation("","gt=1900");
//frmvalidator.addValidation("","lt=3000");
//frmvalidator.addValidation("","alphanumeric");
//frmvalidator.addValidation("","numeric");
//frmvalidator.addValidation("","alpha");
//frmvalidator.addValidation("","alnumhyphen");
//frmvalidator.addValidation("","email");
//frmvalidator.addValidation("","regexp");
//frmvalidator.addValidation("","dontselect");

frmvalidator.setAddnlValidationFunction("Verify"); 
</script>
</body>
</html>
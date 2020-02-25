<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>
<%@ page import="siap.sige.aula.model.AulaUdienzaModel"%>
<%@ page import="siap.sige.aula.action.ICostantiAula"%>
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="udienzamonocraticasige" scope="request" 
						 class="siap.sige.udienza.model.UdienzaSigeModel"/>

<jsp:useBean id="StackDiRitorno" scope="session" class="java.util.Stack"/>
<jsp:useBean id="elencoGiudici" scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoMagAsseg" scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoProcuratori" scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoAssistenti" scope="request" class="java.lang.String"/>
<!-- intervento per 11.1.2 -->
<jsp:useBean id="elencoSezioni"      scope="request"  class="java.lang.String"/>
<jsp:useBean id="aulaUdienza"        scope="request"  class="siap.sige.aula.model.AulaUdienzaModel" />
<jsp:useBean id="numProcePerUdienza" scope="request"  class="java.lang.String"/>
<jsp:useBean id="codMagis" scope="request"  class="java.lang.String"/>
<jsp:useBean id="indirizzoUfficio" scope="request"  class="java.lang.String"/>

<% 
	//=========================================================================== 
	// Inserire la condizione in base alla quale i campi non sono modificabili 
	// (se esiste) 
	//=========================================================================== 
// INTERVENTI PER NUOVA GESTIONE UDIENZE MONOCRATICHE/COLLEGIALI PER LA VERSIONE 11.2.1
String readonly = ""; 
String disSezAula="";

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
  <title>[S.I.E.S.] Gestione Udienza Monocratica da menu Funzioni di Supporto</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript">
  	// init per focus sul primo campo
  	function init()
  	{
	  	document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.focus();
  	}

    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
    {
      desktop = window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }
    	
		// Verifica della data Udienza.
		function checkDataUdienza()
		{
			var ritorno = true;
    	var dataUdienza = 
    			document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+ 
    			document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value +'/'+ 
    			document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value;
        											
    	if (dataUdienza.length > 2)
    	{
    		if (!ControllaData(dataUdienza))
      	{
      		alert('Data udienza non corretta.');
      		document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.focus();
        	ritorno = false;
      	}
    	}
    	else
    	{
    		alert('Data udienza obbligatoria.');
      	document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.focus();
      	ritorno = false;
	  	}
	    
			return ritorno;
		}

		// Check Obbl. Giudice.
		function checkObblGiudice()
		{
			var ritorno = true;				
			if ( document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_COD_GIUDICE%>.value == '-' ) 
	    {
	    	alert("Giudice obbligatorio.");
	    	document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_COD_GIUDICE%>.focus();
	      ritorno = false;
	    }
			return ritorno;  
		}

		// Check Obbl. Procuratore.
		function checkObblProcuratore()
		{
			var ritorno = true;				
			if ( document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_COD_PROCURATORE%>.value == '-' ) 
	    {
	    	alert("Procuratore della Repubblica obbligatorio.");
	    	document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_COD_PROCURATORE%>.focus();
	      ritorno = false;
	    }
			return ritorno;  
		}

		// Check Obbl. Cancelliere - Assistente Giudiziario.
		function checkObblAssistente()
		{
			var ritorno = true;				
			if ( document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE%>.value == '-' ) 
	    {
	    	alert("Cancelliere obbligatorio.");
	    	document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE%>.focus();
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
    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================
    
		// Esecuzione delle funzioni di verifica.
   	function Verify()
    {  
    	// Controllo della data Udienza.  
    	if( !checkDataUdienza() )
      	return false;

    	// Controllo Obbligatorietà Giudice.  
    	if( !checkObblGiudice() )
      	return false;

    	// Controllo Obbligatorietà Giudice Assegnatario in Modifica.
    	<%if( "M".equals(modalita) && !numProcePerUdienza.equals("0")) {%>   	    
    	
	    	// Controllo che Data udienza <= di Data Odierna
	   	    var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
		    var dataUdienzaM = 
				document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+ 
				document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value +'/'+ 
				document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value;
				
				// 31/10/2019: eliminiamo il controllo di blocco sulla data udienza (richiesta Da: Nunziata Alfieri <nunziata.alfieri@giustizia.it> 
			    // Inviato: giovedì 31 ottobre 2019 10:48) - ticket otrs 20191031015 
/* 			if (!CompareDate(data_od, dataUdienzaM)) {
	           	alert('Udienza non modificabile! La Data Udienza è minore della Data Odierna!');           	
	           	return false;
	   		} */

	    	if( !checkObblGiudiceAssegnatario() )
	      	return false;
    	
	        var confRet = window.confirm ("Attenzione! Questa operazione di modifica interesserà tutti i procedimenti con la data udienza selezionata!");
	        if (confRet) {
      return true;
    }
	        else {
	           return false;
	        }   
	        
    	<%}%>
    	
    	//controllo orario inizio e fine
    	var ret = true;
    	var oraInizio = document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_ORA_INIZIO%>.value;    
        var minInizio = document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_MIN_INIZIO%>.value;
        var oraFine   = document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_ORA_FINE%>.value;
        var minFine   = document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_MIN_FINE%>.value;

  			// Controlla l'ora e minuti d'inizio
        ret = controlloOra(oraInizio, minInizio);
  			
 		// Controlla l'ora e minuti di fine, nel caso in cui
 		// il conrollo precedente sia andato a buon fine.
 		if( ret == true )
       	ret = controlloOra(oraFine,minFine);
    	
        return ret;
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
	  document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiAula.CAMPO_DESCRIZIONE_AULA%>.value='';
	  document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiAula.CAMPO_DESCRIZIONE_INGRESSO%>.value='';
	  document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiAula.CAMPO_NUMERO_PIANO%>.value='';
	  document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiAula.CAMPO_ID_AULA%>.value='';
 	  document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiAula.CAMPO_DESCRIZIONE_STANZA%>.value='';

  } 
  

  
	// Check Obbl. Giudice.Assegnatario
	function checkObblGiudiceAssegnatario()
	{
		var ritorno = true;				
		if ( document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaProcedimentoSige.CAMPO_COD_MAGISTRATO%>.value == '-' ) 
		  {
		  	alert("Giudice Assegnatario obbligatorio.");
		  	document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiUdienzaProcedimentoSige.CAMPO_COD_MAGISTRATO%>.focus();
		    ritorno = false;
		  }
		return ritorno;  
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
        UdienzaSigeModel lUdienzaMonocraticaSige = new UdienzaSigeModel(); 
        String lAzione = new String();
        if( modalita.equals("I") ) {
        	lAzione = "siap.sige.udienzamonocratica.action.ActInserisciUdienzaMonocraticaSige"; 
        %>
        <font class="campo">Inserimento Udienza Monocratica</font>
        <%
        }
        else if( modalita.equals("X") ) {
        	lAzione = "siap.sige.udienzamonocratica.action.ActInserisciUdienzaMonocraticaSige";
        	lUdienzaMonocraticaSige = udienzamonocraticasige;
        %>
        <font class="campo">Inserimento per copia Udienza Monocratica</font>
        <%
        }
        else if( modalita.equals("M") ) {
          lAzione = "siap.sige.udienzamonocratica.action.ActModificaUdienzaMonocraticaSige";
          lUdienzaMonocraticaSige = udienzamonocraticasige;
        %>
        <font class="campo">Modifica Udienza Monocratica</font>
        <%
        }
        %>
      </td>
        <!-- BOTTONE DI RITORNO -->
  			<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>

	<FORM method="POST" action="Main.jsp" name="LoadInserisciUdienzaMonocraticaSige">
  	 <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  	 <input type="HIDDEN" name="<%=ICostantiUdienzaSige.CAMPO_COD_MAG_ASS%>" value="<%=codMagis%>">
	
	<%
	// Gestione del pulsante di ritorno nel caso che la funzione
	// venga richiamata dalla fissazione udienza. 
	%>
	<%if( request.getParameter(IWebConstants.LINK_RITORNO) != null ) {%>
		 <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" 
						 value="<%=request.getParameter(IWebConstants.LINK_RITORNO)%>">	
	<%}%>	

  <%if( modalita.equals("M") ) {%>
		 <input type="HIDDEN" name="<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>" 
						 value="<%=lUdienzaMonocraticaSige.getIdUdienzaSige()%>">
		 <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  <%}%>
  
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
	    	<td class="l">Fascicoli interessati dalla modifica :<font class="campo">
						<%=numProcePerUdienza%>
					</font>&nbsp;
				</td>
	</tr>
    <tr>
      <td class="l">Data Udienza <font class=ob>(*)</font></td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lUdienzaMonocraticaSige.getDataUdienza(),"dd"))%>" 
               name="<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>" 
               onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							 onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lUdienzaMonocraticaSige.getDataUdienza(),"MM"))%>" 
               name="<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>" 
               onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							 onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lUdienzaMonocraticaSige.getDataUdienza(),"yyyy"))%>" 
               name="<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>" 
               onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							 onBlur="javascript:value=FillYear(value)">
        
				<a href="javascript:calendario('LoadInserisciUdienzaMonocraticaSige','<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>','<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>','<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>');">
       		<img src="/images/calendario.gif" border=0>
        </a>
      </td>
    </tr>

    <tr>
      <td class="l">Giudice Udienza <font class=ob>(*)</font></td>
      <td class="l">
        <select title="giudice" name="<%=ICostantiUdienzaSige.CAMPO_COD_GIUDICE%>" <%=readonly%>>
        	<%=elencoGiudici%>
        </select>
      </td>
    </tr>
    <!-- intervento per 11.1.2 -->
     <%if( "M".equals(modalita) && !numProcePerUdienza.equals("0")) {%>
	     <tr>
	       <td class="l">Giudice Assegnatario <font class=ob>(*)</font></td>
	      <td class="l">
	        <select title="giudice" name="<%=ICostantiUdienzaProcedimentoSige.CAMPO_COD_MAGISTRATO%>"  >
	        	<%=elencoMagAsseg%>
	        </select>
	      </td>
	    </tr>
    <%} %>
    <tr>
      <td class="l">Sezione</td>
      <td class="l">
        <select title="sezione" name="<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>" onChange="svuotaAula();" <%=readonly%>>
        	<%=elencoSezioni%>
        </select>
      </td>
    </tr>

    <tr>
    <td class="l" colspan="2">Aula <input type="text"
						name="<%=ICostantiAula.CAMPO_DESCRIZIONE_AULA%>"
						value="<%=StringUtils.toStringJSP(lAulaUdienza.getDescrizioneAula())%>"
						size="12" maxlength="30" readonly="readonly" <%=disSezAula%>>&nbsp;&nbsp;
						Ingresso <input type="text"
						name="<%=ICostantiAula.CAMPO_DESCRIZIONE_INGRESSO%>"
						value="<%=StringUtils.toStringJSP(lAulaUdienza.getDescrizioneIngresso())%>"
						size="30" maxlength="30"  readonly="readonly" <%=disSezAula%>>&nbsp;&nbsp;
						Stanza <input type="text"
						name="<%=ICostantiAula.CAMPO_DESCRIZIONE_STANZA%>"
						value="<%=StringUtils.toStringJSP(lAulaUdienza.getDescrizioneStanza())%>"
						size="30" maxlength="30"  readonly="readonly" <%=disSezAula%>>&nbsp;&nbsp;
						Piano <input type="text"
						name="<%=ICostantiAula.CAMPO_NUMERO_PIANO%>"
						value="<%=StringUtils.toStringJSP(lAulaUdienza.getNumeroPiano())%>"
						size="12" maxlength="30"  readonly="readonly" <%=disSezAula%>>&nbsp;&nbsp;
						<input type="HIDDEN" name="<%=ICostantiAula.CAMPO_ID_AULA%>"
						value="<%=StringUtils.toStringJSP(lAulaUdienza.getIdAula())%>">
						<%if(numProcePerUdienza.equals("0")) {%><a
							href="Javascript:ListaAule('LoadInserisciUdienzaMonocraticaSige', document.LoadInserisciUdienzaMonocraticaSige.<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>.value);"><img
								src="/images/filefolder.gif" border=0 ></a> 
						 <%} %>
					</td>
    </tr>
     <!-- intervento per 11.1.2 -->
    <tr>
      <td class="l">Procuratore della Repubblica </td>
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
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%-- 20090402 Come richiesto dall' amministrazione
    <tr>
      <td class="l">Numero Max Fascicoli</td>
      <td class="l">
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lUdienzaMonocraticaSige.getNumeroMaxFascicoli())%>"
               name="<%=ICostantiUdienzaSige.CAMPO_NUMERO_MAX_FASCICOLI%>"  
               <%=readonly%>> 
      </td>
    </tr>
--%>
    <tr>
      <td class="l">Luogo Svolgimento</td>
      <td class="l"> 
      <%if(lUdienzaMonocraticaSige.getLuogoUdienza() != null && !"".equals(lUdienzaMonocraticaSige.getLuogoUdienza())){ %>
        <input type="text" maxlength="100" size="50" 
               value="<%=StringUtils.toStringJSP(lUdienzaMonocraticaSige.getLuogoUdienza())%>"
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

    <tr>
      <td class="l">Orario Inizio (ora:min)</td>
      <td class="l"> 
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(lUdienzaMonocraticaSige.getOraInizio())%>"
               name="<%=ICostantiUdienzaSige.CAMPO_ORA_INIZIO%>"  
               <%=readonly%> onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							  onBlur="javascript:value=FillDM(value)" >
			:  
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(lUdienzaMonocraticaSige.getMinInizio())%>"
               name="<%=ICostantiUdienzaSige.CAMPO_MIN_INIZIO%>"  
               <%=readonly%>  onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							  onBlur="javascript:value=FillDM(value)" >
    </tr>

    <tr>
      <td class="l">Orario Fine (ora:min)</td>
      <td class="l"> 
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(lUdienzaMonocraticaSige.getOraFine())%>"
               name="<%=ICostantiUdienzaSige.CAMPO_ORA_FINE%>"  
               <%=readonly%> onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							  onBlur="javascript:value=FillDM(value)" > 
				: 
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(lUdienzaMonocraticaSige.getMinFine())%>"
               name="<%=ICostantiUdienzaSige.CAMPO_MIN_FINE%>"  
               <%=readonly%> onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							  onBlur="javascript:value=FillDM(value)" >
    </tr>

    <tr>
      <td>
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>

  </table>
</form>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciUdienzaMonocraticaSige");


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
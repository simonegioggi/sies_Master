<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sige.aula.model.AulaUdienzaModel"%>
<%@ page import="siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel"%>
<%@ page import="siap.sige.aula.action.ICostantiAula"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.collegio.util.CollegioUtils"%>
<%@ page import="siap.sige.collegio.model.CollegioModel"%>
<%@ page import="siap.sige.sezione.action.ICostantiSezione"%>
<%@ page import="siap.sige.sezione.model.SezioneModel"%>
<%@ page import="siap.sige.magistrato.action.ICostantiMagistrato"%>


<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="udienzasige" scope="request" 
						 class="siap.sige.udienza.model.UdienzaSigeModel"/>

<jsp:useBean id="elencoProcuratori" scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoAssistenti"  scope="request" class="java.lang.String"/>
<!-- intervento per 11.1.2 -->
<jsp:useBean id="elencoSezioni"      scope="request"  class="java.lang.String"/>
<jsp:useBean id="aulaUdienza"        scope="request"  class="siap.sige.aula.model.AulaUdienzaModel" />
<jsp:useBean id="numProcePerUdienza" scope="request"  class="java.lang.String"/>
<jsp:useBean id="codMagis" scope="request"  class="java.lang.String"/>
<jsp:useBean id="indirizzoUfficio" scope="request"  class="java.lang.String"/>
<jsp:useBean id="listaProcedimenti" scope="request"  class="java.util.Vector"/>

<% 
	//=========================================================================== 
	// Inserire la condizione in base alla quale i campi non sono modificabili 
	// (se esiste) 
	//=========================================================================== 
//INTERVENTI PER NUOVA GESTIONE UDIENZE MONOCRATICHE/COLLEGIALI PER LA VERSIONE 11.2.1
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
  <title>[S.I.E.S.] Gestione Udienza Collegiale CAP</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript">
  	// init per focus sul primo campo
  	function init()
  	{
			document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.focus();
  	}

    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
    {
      desktop = 
          window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }

    function ListaCollegi(a_formname)
    {
      var desktop;
      desktop = 
          window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sige.collegio.action.ActLoadRicercaCollegioLista&formname="+a_formname, "Ricerca_Collegio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=600,height=550");
    }
  
		// Verifica della data Udienza.
		function checkDataUdienza()
		{
			var ritorno = true;
    	var dataUdienza = 
    			document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+ 
    			document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value +'/'+ 
    			document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value;
        											
    	if (dataUdienza.length > 2)
    	{
    		if (!ControllaData(dataUdienza))
      	{
      		alert('Data udienza non corretta.');
      		document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.focus();
        	ritorno = false;
      	}
    	}
    	else
    	{
    		alert('Data udienza obbligatoria.');
      	document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.focus();
      	ritorno = false;
	  	}
	    
			return ritorno;
		}
		<!-- eliminiare il collegio (richiesta per 11.2.1) -->
		// Check Obbl. Collegio.
<%-- 		function checkObblCollegio()
		{
			var ritorno = true;				
			if ( document.LoadInserisciUdienzaCollegiale.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>.value == '' ) 
	    {
	    	alert("Collegio obbligatorio.");
	    	//document.LoadInserisciUdienzaCollegiale.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>.focus();
	      ritorno = false;
	    }
		  
			return ritorno;  
		} --%>

		// Check Obbl. Procuratore.
		function checkObblProcuratore()
		{
			var ritorno = true;				
			if ( document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_COD_PROCURATORE%>.value == '-' ) 
	    {
	    	alert("Procuratore Generale obbligatorio.");
	    	document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_COD_PROCURATORE%>.focus();
	      ritorno = false;
	    }
		    
			return ritorno;  
		}

		// Check Obbl. Cancelliere - Assistente Giudiziario.
		function checkObblAssistente()
		{
			var ritorno = true;				
			if ( document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE%>.value == '-' ) 
	    {
	    	alert("Cancelliere obbligatorio.");
	    	document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE%>.focus();
	      ritorno = false;
	    }
		    
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

    	// Controllo Obbligatorietà Collegio.  
    	<!-- eliminiare il collegio (richiesta per 11.2.1) -->
/*     	if( !checkObblCollegio() )
      	return false;
 */
    	// Controllo Obbligatorietà Procuratore.  
    	//if( !checkObblProcuratore() )
      //	return false;

    	// Controllo Obbligatorietà Assistente.  
    	//if( !checkObblAssistente() )
      //	return false;
    <!-- intervento per richiesta per 11.2.1 -->
   	<%if( "M".equals(modalita) && !numProcePerUdienza.equals("0")) {%>   	    
   	
    	// Controllo che Data udienza <= di Data Odierna
   	    var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
	    var dataUdienzaM = 
			document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+ 
			document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value +'/'+ 
			document.LoadInserisciUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value;
			
		// 31/10/2019: eliminiamo il controllo di blocco sulla data udienza (richiesta Da: Nunziata Alfieri <nunziata.alfieri@giustizia.it> 
		    // Inviato: giovedì 31 ottobre 2019 10:48) - ticket otrs 20191031015 
		/* if (!CompareDate(data_od, dataUdienzaM)) {
           	alert('Udienza non modificabile! La Data Udienza è minore della Data Odierna!');           	
           	return false;
   		} */
    	
        var confRet = window.confirm ("Attenzione! Questa operazione di modifica interesserà tutti i procedimenti con la data udienza selezionata!");
        if (confRet) {
        	return true;
        }
        else {
           return false;
        }   
    	
   	<%}%>	
      return true;
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
 	  document.LoadInserisciUdienzaCollegiale.<%=ICostantiAula.CAMPO_DESCRIZIONE_AULA%>.value='';
 	  document.LoadInserisciUdienzaCollegiale.<%=ICostantiAula.CAMPO_DESCRIZIONE_INGRESSO%>.value='';
 	  document.LoadInserisciUdienzaCollegiale.<%=ICostantiAula.CAMPO_NUMERO_PIANO%>.value='';
 	  document.LoadInserisciUdienzaCollegiale.<%=ICostantiAula.CAMPO_ID_AULA%>.value='';
 	  document.LoadInserisciUdienzaCollegiale.<%=ICostantiAula.CAMPO_DESCRIZIONE_STANZA%>.value='';
   } 
	// Elenco popup dei Magistrati
	function ListaMagistrati(a_formname, a_idfieldnum)
	{
	 var desktop;
	 desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sige.magistrato.action.ActLoadRicercaMagistratoAssegnazioneLista&formname="+a_formname+"&idfieldnum="+a_idfieldnum, 
			 									 "Ricerca_WMagistrato",
			 									 "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
	}
  </script>
</head>

<body class="corpo" onload="Javascript:init();">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" 
							 alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <%
        UdienzaSigeModel lUdienzaSige = new UdienzaSigeModel();
        lUdienzaSige.setCollegio( new CollegioModel() );
        lUdienzaSige.getCollegio().setSezione( new SezioneModel() );
        String lAzione = new String();
        if( modalita.equals("I") ) {
        	lAzione = "siap.sige.udienzacollegiale.action.ActInserisciUdienzaCollegiale"; 
        %>
        	<font class="campo">Inserimento Udienza Collegiale</font>
        <%
        }
        else if( modalita.equals("X") ) {
        	lAzione = "siap.sige.udienzacollegiale.action.ActInserisciUdienzaCollegiale"; 
          lUdienzaSige = udienzasige;
        %>
        	<font class="campo">Inserimento per copia Udienza Collegiale</font>
        <%
        }
        else if( modalita.equals("M") ) {
          lAzione = "siap.sige.udienzacollegiale.action.ActModificaUdienzaCollegiale";
          lUdienzaSige = udienzasige;
        %>
        	<font class="campo">Modifica Udienza Collegiale</font>
        <%
        }
        %>
      </td>
        <!-- BOTTONE DI RITORNO -->
  			<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>

	<FORM method="POST" action="Main.jsp" name="LoadInserisciUdienzaCollegiale">
  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">

		<%
		// Gestione del pulsante di ritorno nel caso che la funzione
		// venga richiamata dalla fissazione udienza. 
		%>
		<%if( request.getParameter(IWebConstants.LINK_RITORNO) != null ) {%>
		 	<input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" 
						 value="<%=request.getParameter(IWebConstants.LINK_RITORNO)%>">	
		<%}%>	

    <%
    	if( modalita.equals("M") ) {
    %>
			<input type="HIDDEN" name="<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>" 
						 value="<%=lUdienzaSige.getIdUdienzaSige()%>">
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
    <%}%>
  
  <%
  
  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  	siesLogger.debug("Inizio JSP ");
  
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
     <%     	if( modalita.equals("M") ) {
    %>
    <tr>
	    	<td class="l">Fascicoli interessati dalla modifica :<font class="campo">
						<%=numProcePerUdienza%>
					</font>&nbsp;
				</td>
	</tr>
	  <%}%>
	  
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

  <%
  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  	siesLogger.debug("Inizio JSP step 1");
  %>
<input type="hidden" maxlength="10" size="10"  value="<%=StringUtils.toStringJSP(lUdienzaSige.getColIdCollegio())%>"   name="<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>"  readonly >
<input type="hidden" maxlength="10" size="10"  value="<%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getCodCollegio())%>"   name="<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>"  readonly >  
<!-- eliminiare il collegio (richiesta per 11.2.1) -->
<%--     <tr>
      <td class="l">Collegio <font class=ob>(*)</font></td>
			<td class="l">
        <input type="text" maxlength="10" size="10" 
               value="<%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getCodCollegio()) %>"
               name="<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>"  
               readonly>

        <input type="hidden" maxlength="10" size="10" 
               value="<%=StringUtils.toStringJSP(lUdienzaSige.getColIdCollegio())%>"
               name="<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>"  
               readonly>  
      	 <a href="Javascript:ListaCollegi('LoadInserisciUdienzaCollegiale');">
        	Seleziona dalla lista <img src="/images/filefolder.gif" border=0>
         </a>
      </td>
    </tr> --%>



<!--     intervento per 11.2.1 (la sezione diventa una combo)
 --> 
    <tr>
      <td class="l">Sezione</td>
			<td class="l">
        <select title="sezione" name="<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>" onChange="svuotaAula();" <%=readonly%>>
        	<%=elencoSezioni%>
        </select>
      </td>
    </tr>

    <tr>
		<td class="l">Presidente</font></td>
		<td class="L">
			<input title="Cognome" readonly type="text" 
						 name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25" 
						 value="<%=CollegioUtils.isValidSize( lUdienzaSige.getCollegio().getCollegioMagistrati(), 0) ? 
								 lUdienzaSige.getCollegio().getCollegioMagistrati()[0].getMagistrato().getCognome() : "" %>">
			<input title="Nome" readonly type="text" 
						 name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="25" 
						 value="<%=CollegioUtils.isValidSize( lUdienzaSige.getCollegio().getCollegioMagistrati(), 0) ? 
								 lUdienzaSige.getCollegio().getCollegioMagistrati()[0].getMagistrato().getNome() : "" %>">
			<input title="CodMagistrato" type="hidden" 
						 name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" 
							 value="<%=CollegioUtils.isValidSize( lUdienzaSige.getCollegio().getCollegioMagistrati(), 0) ? 
								 lUdienzaSige.getCollegio().getCollegioMagistrati()[0].getMagistrato().getCodMagistrato() : "" %>">
			<a href="Javascript:ListaMagistrati('LoadInserisciUdienzaCollegiale', '0');">
				<img src="/images/filefolder.gif" border=0>
			</a>
      </td>
    </tr>

    <tr>
      <td class="l">Consigliere</td>
		  <td class="L">
		  	<input title="Cognome" readonly type="text" 
							 name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25" 
							 value="<%=CollegioUtils.isValidSize(lUdienzaSige.getCollegio().getCollegioMagistrati(), 1) ? 
									 lUdienzaSige.getCollegio().getCollegioMagistrati()[1].getMagistrato().getCognome() : "" %>">
		    <input title= "Nome" readonly type="text" 
							 name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="25"
							 value="<%=CollegioUtils.isValidSize(lUdienzaSige.getCollegio().getCollegioMagistrati(), 1) ? 
									 lUdienzaSige.getCollegio().getCollegioMagistrati()[1].getMagistrato().getNome() : "" %>">
				<input title="CodMagistrato" type="hidden" 
							 name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>"
							 value="<%=CollegioUtils.isValidSize( lUdienzaSige.getCollegio().getCollegioMagistrati(), 1) ? 
									 lUdienzaSige.getCollegio().getCollegioMagistrati()[1].getMagistrato().getCodMagistrato() : "" %>">
		     <a href="Javascript:ListaMagistrati('LoadInserisciUdienzaCollegiale','1');">
		        	<img src="/images/filefolder.gif" border=0>
		     </a>
      </td>
    </tr>

    <tr>
      <td class="l">Consigliere</td>
		  <td class="L">
		  	<input title="Cognome" readonly type="text" 
							 name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25" 
							 value="<%=CollegioUtils.isValidSize( lUdienzaSige.getCollegio().getCollegioMagistrati(), 2) ? 
									 lUdienzaSige.getCollegio().getCollegioMagistrati()[2].getMagistrato().getCognome() : "" %>">
		    <input title= "Nome" readonly type="text" 
							 name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="25"
							 value="<%=CollegioUtils.isValidSize( lUdienzaSige.getCollegio().getCollegioMagistrati(), 2) ? 
									 lUdienzaSige.getCollegio().getCollegioMagistrati()[2].getMagistrato().getNome() : "" %>">
				<input title="CodMagistrato" type="hidden" 
							 name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>"
							 value="<%=CollegioUtils.isValidSize( lUdienzaSige.getCollegio().getCollegioMagistrati(), 2) ? 
									 lUdienzaSige.getCollegio().getCollegioMagistrati()[2].getMagistrato().getCodMagistrato() : "" %>">
        		<a href="Javascript:ListaMagistrati('LoadInserisciUdienzaCollegiale','2');">
          		<img src="/images/filefolder.gif" border=0>
        		</a>
      </td>
    </tr>

    <tr>
      <td class="l">Procuratore Generale </td>
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
               value="<%=StringUtils.toStringJSP(lUdienzaSige.getNumeroMaxFascicoli())%>"
               name="<%=ICostantiUdienzaSige.CAMPO_NUMERO_MAX_FASCICOLI%>"  
               <%=readonly%>> 
      </td> 
    </tr>
--%>
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
							href="Javascript:ListaAule('LoadInserisciUdienzaCollegiale', document.LoadInserisciUdienzaCollegiale.<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>.value);"><img
								src="/images/filefolder.gif" border=0 ></a> 
						 <%} %>
					</td>
    </tr>
    
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

    <tr>
      <td class="l">Orario Inizio (ora:min)</td>
      <td class="l"> 
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(lUdienzaSige.getOraInizio())%>"
               name="<%=ICostantiUdienzaSige.CAMPO_ORA_INIZIO%>"  
               <%=readonly%>>
			:  
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(lUdienzaSige.getMinInizio())%>"
               name="<%=ICostantiUdienzaSige.CAMPO_MIN_INIZIO%>"  
               <%=readonly%>>
    </tr>

    <tr>
      <td class="l">Orario Fine (ora:min)</td>
      <td class="l"> 
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(lUdienzaSige.getOraFine())%>"
               name="<%=ICostantiUdienzaSige.CAMPO_ORA_FINE%>"  
               <%=readonly%>> 
				: 
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(lUdienzaSige.getMinFine())%>"
               name="<%=ICostantiUdienzaSige.CAMPO_MIN_FINE%>"  
               <%=readonly%>>
    </tr>

        <!-- intervento per 11.2.1 -->
 <% if( listaProcedimenti!= null && listaProcedimenti.size()>0) {  %>
 
</table>
<br>
<table>
 <tr>
      <td class="Titolo" colspan="4">Gestione Magistrati Assegnatari</td>
    </tr>
<%
	 for (int i=0; i<listaProcedimenti.size(); i++ ){
		 ProcedimentixUdienzaModel proc = (ProcedimentixUdienzaModel)listaProcedimenti.get(i);
	 
 %>
    <tr>

       <td class=l>
			<input type="checkbox" id="<%=i%>"  name="<%=ICostantiMagistrato.CAMPO_CHECK_MAGISTRATO_ASS%>" value="<%=proc.getIdFasSIGE()%>"  disabled="disabled" onclick="Javascript:toggle(this);" />&nbsp;&nbsp;&nbsp;&nbsp;<%=proc.getChiaveAnnoFasSIGE()%>/<%=proc.getChiaveProgrFasSIGE()%>
				
		</td>
		<td>
		<input title="CognomeMagAss" readonly disabled="disabled"  type="text" 
							 name="<%=ICostantiMagistrato.CAMPO_COGNOME_ASS%>" maxlength="35" size="25"  value="<%=proc.getCognomeMagistrato()%> ">
		
		<input title="NomeMagAss" readonly disabled="disabled" type="text" 
							  name="<%=ICostantiMagistrato.CAMPO_NOME_ASS%>" maxlength="35" size="25" 
							 value="<%=proc.getNomeMagistrato()%>">
							 
		 <input title="CodMagistratoAss" type="hidden"  name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_ASS%>"  value="<%=proc.getCodMagistrato() %>" disabled="disabled">
		</td>	
	  </tr>
	
<%}}%>

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
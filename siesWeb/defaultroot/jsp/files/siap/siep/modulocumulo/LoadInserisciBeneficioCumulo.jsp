<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.siep.tipologiaorario.action.ICostantiTipologiaOrario"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiBeneficiCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.BeneficioCumuloModel"%>

<jsp:useBean id="IstruttoriaCumulo"     scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"        scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="tipoFormBeneficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" 			scope="request" class="java.lang.String"/>

<jsp:useBean id="beneficioCumulo"       scope="request" class="siap.siep.modulocumulo.model.BeneficioCumuloModel"/>
<jsp:useBean id="beneficioNonMenzione"  scope="request" class="siap.siep.modulocumulo.model.BeneficioCumuloModel"/>
<jsp:useBean id="tipologiaOrario"  		scope="request" class="java.util.Vector"/>

<%// Sospensione Pena / Non Menzione %>
<jsp:useBean id="sospensioneSubordinata" scope="request" class="java.lang.String"/>
<jsp:useBean id="sottotipoBeneficio"     scope="request" class="java.lang.String"/>


<% 
//============================================================================== 
// Form per l'inserimento e la modifica Dei Benefici Disposti in sentenza 
// Gestisce sia la Sospensione Condizionale/Non Menzione che l'indulto/Amnistia 
//============================================================================== 

String lNomeForm = "formInserisciBeneficio";

BeneficioCumuloModel lBeneficioCumulo = new BeneficioCumuloModel(); 
if ( modalita.equals("M"))
  lBeneficioCumulo = beneficioCumulo;

String lNonMenzione = null;
if(  "02".equals(lBeneficioCumulo.getCodTipoBeneficio()) 
   ||( beneficioNonMenzione != null && beneficioNonMenzione.getIdBeneficioCumulo() != null))
{
  lNonMenzione = "checked";
}

%> 

<!-- 		LoadInserisciBeneficioCumulo		 -->
<html>
<head>
  <title> Gestione Benefici Cumulo </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" >
  
    //====================================================
    function checkAbilitaDisabilitaCampi(checkObject){
      var jQueryObj = $(checkObject);
      
      if (jQueryObj.prop('checked')){
        jQueryObj.closest('td').next('td').find('input').prop('disabled',false);
        jQueryObj.closest('td').next('td').prop('disabled',false);
      }
      else {
        jQueryObj.closest('td').next('td').find('input').prop('disabled',true);
        jQueryObj.closest('td').next('td').prop('disabled',true);
      }
    }
    
    function sottotipo()
    {
      if (  $('#<%=ICostantiBeneficiCumulo.CAMPO_COD_SOTTOTIPO_BENEFICIO %>').val()== '03')
      {
        $('#divAttivitaSubordinata').show();
        //abilitasottotipo();
        //subordinata();
      }
      else
      {
        $('#divAttivitaSubordinata').hide();
        $('#divAttivitaNonRetribuita').hide();
        $('#divTipologiaOraria').hide();
        
        //disabilitasottotipo();
        //disabilitasub();  
        //disabilitadet();  
      }
    }
    
    function disabilitasottotipo(){}
    function disabilitasub(){}
    function disabilitadet(){}
    
    
    function subordinata(){
      if ( $('#<%=ICostantiBeneficiCumulo.CAMPO_COD_TIPO_SOSP_SUBORDINATA %>').val()=='08'){
        // Alla Prestazione di attività non retribuita a favore della collettività
        $('#divAttivitaNonRetribuita').show();
      }
      else {
        $('#divAttivitaNonRetribuita').hide();
        $('#divTipologiaOraria').hide();
      }
    }
    
    function checkFrequenzaSettimanale(checkObject){
      if ( $(checkObject).prop('value')=='1') {
        $('#divTipologiaOraria').hide();
      }
      else {
        $('#divTipologiaOraria').show();
      }
    }
    
    
    
    //==========================================================================
    // Ritorna alla lista delle Misure di Sicurezza per il Titolo
    //==========================================================================
    function eseguiFunzione(action)
    {
      document.<%=lNomeForm%>.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.<%=lNomeForm%>.submit();
    }
    
    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================
    function Verify() {
    	// alert('Verify');
    	
    	// Inserire Almeno uno tra SOSPENSIONE e/o NON MENZIONE
    	if( $('#<%=ICostantiBeneficiCumulo.CAMPO_COD_SOTTOTIPO_BENEFICIO %>').val()== '-' && 
 		    $('input:checkbox[name=<%=ICostantiBeneficiCumulo.CAMPO_FLAG_NON_MENZIONE %>]').prop('checked')== false )  
    	{
    			alert('Inserire Tipo Sospensione e/o Non Menzione');
      			$("#<%=ICostantiBeneficiCumulo.CAMPO_COD_SOTTOTIPO_BENEFICIO %>").focus();
      			return false;
    	}
    	
    	// Controllo su Frequenza Determinata
    	if( $('input:radio[name=<%=ICostantiBeneficiCumulo.CAMPO_FLAG_FREQUENZA_SETTIMANALE %>][value=2]').prop('checked') )  
    	{
    		if( $('input:checkbox[name=<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_LUN %>]').prop('checked')== false &&
   				$('input:checkbox[name=<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_MAR %>]').prop('checked')== false &&
   				$('input:checkbox[name=<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_MER %>]').prop('checked')== false &&
   				$('input:checkbox[name=<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_GIOV %>]').prop('checked')== false &&
   				$('input:checkbox[name=<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_VEN %>]').prop('checked')== false &&
   				$('input:checkbox[name=<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_SAB %>]').prop('checked')== false && 
   				$('input:checkbox[name=<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_DOM %>]').prop('checked')== false  )
			{ 
    			alert('Inserire Tipologia Orario');
    			$('input:checkbox[name=<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_LUN %>]').focus();
      			return false;
    		}
    	}
    	
    	// Controlli su Tipologia orario: LUNEDI 
    	if($('input:checkbox[name=<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_LUN %>]').prop('checked') )
    	{
    		if( $('#<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_LUN %>').val() == '' && 
      			$('#<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_LUN %>').val() == ''	)    		
    		{
   				alert('Inserire Orario per il LUNEDI');
   				$('#<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_LUN %>').focus();
   				return false;
    		}
    	}
    	
    	// Controlli su Tipologia orario: MARTEDI 
    	if($('input:checkbox[name=<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_MAR %>]').prop('checked') )
    	{
    		if( $('#<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_MAR %>').val() == '' && 
    			$('#<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_MAR %>').val() == ''	)
    		{
    			alert('Inserire Orario per il MARTEDI');
    			$('#<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_MAR %>').focus();
      			return false;
    		}
    	}
    	
    	// Controlli su Tipologia orario: MERCOLEDI 
    	if($('input:checkbox[name=<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_MER %>]').prop('checked') )
    	{
    		if( $('#<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_MER %>').val() == '' && 
    			$('#<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_MER %>').val() == ''	)
    		{
    			alert('Inserire Orario per il MERCOLEDI');
    			$('#<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_MER %>').focus();
      			return false;
    		}
    	}
    	
    	// Controlli su Tipologia orario: GIOVEDI 
    	if($('input:checkbox[name=<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_GIOV %>]').prop('checked') )
    	{
    		if( $('#<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_GIOV %>').val() == '' && 
    			$('#<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_GIOV %>').val() == ''	)
    		{
    			alert('Inserire Orario per il GIOVEDI');
    			$('#<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_GIOV %>').focus();
      			return false;
    		}
    	}
    	
    	// Controlli su Tipologia orario: VENERDI 
    	if($('input:checkbox[name=<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_VEN %>]').prop('checked') )
    	{
    		if( $('#<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_VEN %>').val() == '' && 
    			$('#<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_VEN %>').val() == ''	)
    		{
    			alert('Inserire Orario per il VENERDI');
    			$('#<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_VEN %>').focus();
      			return false;
    		}
    	}
    	
    	// Controlli su Tipologia orario: SABATO 
    	if($('input:checkbox[name=<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_SAB %>]').prop('checked') )
    	{
    		if( $('#<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_SAB %>').val() == '' && 
    			$('#<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_SAB %>').val() == ''	)
    		{
    			alert('Inserire Orario per il SABATO');
    			$('#<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_SAB %>').focus();
      			return false;
    		}
    	}
    	
    	// Controlli su Tipologia orario: DOMENICA 
    	if($('input:checkbox[name=<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_DOM %>]').prop('checked') )
    	{
    		if( $('#<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_DOM %>').val() == '' && 
    			$('#<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_DOM %>').val() == ''	)
    		{
    			alert('Inserire Orario per il DOMENICA');
    			$('#<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_DOM %>').focus();
      			return false;
    		}
    	}
      
    	return true; 
    }
    
    //================================================
    // Funzione richiamata al caricamento della form
    //================================================
    $(document).ready( function() {
    	sottotipo();
    	subordinata();
    	
   <% 	if(beneficioCumulo!=null && beneficioCumulo.getIdBeneficioCumulo()!=null && beneficioCumulo.getFlagFrequenzaSettimanale()!=null )
   		{ 
   			if(beneficioCumulo.getFlagFrequenzaSettimanale().compareTo("D")==0 )
   			{	%>
   				$('#divTipologiaOraria').show();
   	<%		}
   			else
   			{	%>	
   				$('#divTipologiaOraria').hide();
   <%		}
   		}	
   		else
   		{ %>
   			$('#divTipologiaOraria').hide();
   	<%	}  %>
   	
      	//Inizilaizzazione delle check
       	$('input[type=checkbox]').each( function (index) {
         		checkAbilitaDisabilitaCampi(this);
       	});
    });
    
    
    
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
        <% if( modalita.equals("I") ) {%>
        <font class="campo">Inserimento Beneficio In Sentenza</font>
        <% } else if( modalita.equals("M") ) { %>
        <font class="campo">Modifica Beneficio In Sentenza</font>
        <%}%>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaBeneficiCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

  <% // INCLUDE DEL DETTAGLIO DEL TITOLO e DELL'ISTRUTTORIA%>
  <br>
  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>  
  	<tr>
  	  <td>	
    	<jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
      </td>
    </tr>  	
  </table>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="<%=lNomeForm%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciBeneficiCumulo">
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO %>" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getIdBeneficioCumulo()) %>">
  <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_FLAG_STATO %>"          value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getFlagStato()) %>">

  <input type="hidden" name="<%=ICostantiModuloCumulo.MODALITA %>" value="<%=modalita%>">
  <input type="hidden" name="<%=ICostantiBeneficiCumulo.TIPO_FORM_BENEFICIO %>" value="<%=tipoFormBeneficio%>">



  <table cellspacing="2" cellpadding="2" width="90%">
    <tr>
      <td class="l" colspan="2" width="10%">Tipo Sospensione</td>
      <td class="l" colspan="2">
        <select name="<%=ICostantiBeneficiCumulo.CAMPO_COD_SOTTOTIPO_BENEFICIO %>" 
                id="<%=ICostantiBeneficiCumulo.CAMPO_COD_SOTTOTIPO_BENEFICIO %>" 
                onChange="Javascript:sottotipo()">
        <%=sottotipoBeneficio%>
        </select>
      </td>
    </tr>
    
    <tr>
      <td class="l" colspan=2>Non Menzione</td>
      <td class="l" ><input <%=lNonMenzione%> type="checkbox" name="<%=ICostantiBeneficiCumulo.CAMPO_FLAG_NON_MENZIONE %>" value="1"></td>
    </tr>
     
    <tr><td>&nbsp;</td></tr>
      
    <tr><td class="titolo" width=30%>Durata Sospensione</td></tr> 
    <tr>
      <td class="c">
        Anni <BR> 
        <input type="text" size="3" maxlength="3" 
               name="<%=ICostantiBeneficiCumulo.CAMPO_NUM_ANNI_SOSPENSIONE %>" 
               value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getNumAnniSospensione()) %>"  
               onkeypress="return TicTabNumField(this,event)" >
     </td>
    </tr>
       
    <tr><td>&nbsp;</td></tr>
  </table>


<%
//==============================================================================
// DIV con i campi degli eventuzli obblighi sospensione condizionata
//==============================================================================
%>
<div id="divAttivitaSubordinata" style="display:block; position:relative; width:100%;" >
  <table width=90%>  
    <tr>
      <td class="l" colspan=2>Obblighi del condannato ex art 165 c.p.  </td>
      <td class="l" colspan=2>
        <select class="small" 
                name="<%=ICostantiBeneficiCumulo.CAMPO_COD_TIPO_SOSP_SUBORDINATA %>" 
                id="<%=ICostantiBeneficiCumulo.CAMPO_COD_TIPO_SOSP_SUBORDINATA %>" 
                onChange="Javascript:subordinata()">
        <%=sospensioneSubordinata%>
        </select>
      </td>
    </tr>
  </table>    
    
  <table width=90%>  
    <tr>
      <td class="l">Tipologia Obbligo</td>
      <td class="l">
         <textarea cols="40"  rows="4" name="<%= ICostantiBeneficiCumulo.CAMPO_NOTE %>"><%=StringUtils.toStringJSP(lBeneficioCumulo.getNote()) %></textarea>
      </td>
      <td class="c" colspan=2>
        <table>  
          <tr><td class="titolo" colspan="3" >Termine Adempimento Obbligo</td></tr> 
          <tr>
            <td align="center">  
              <font class="label"> Anni <BR> </font>
              <input size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getNumAnniAdempimento(),"") %>"  type="text" name="<%=ICostantiBeneficiCumulo.CAMPO_NUM_ANNI_ADEMPIMENTO %>" onkeypress="return TicTabNumField(this,event)" >               
            </td>
            <td align="center">  
              <font class="label"> Mesi</font> <BR> 
              <input size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getNumMesiAdempimento(),"") %>"  type="text" name="<%=ICostantiBeneficiCumulo.CAMPO_NUM_MESI_ADEMPIMENTO %>" onkeypress="return TicTabNumField(this,event)" >              
            </td>
            <td align="center">  
              <font class="label"> Giorni <BR> </font>
              <input size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getNumGiorniAdempimento(),"") %>"  type="text" name="<%=ICostantiBeneficiCumulo.CAMPO_NUM_GIORNI_ADEMPIMENTO %>" onkeypress="return TicTabNumField(this,event)" >            
            </td>
          </tr>
        </table> 
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
  </table>
</div>

<%
//==============================================================================
// DIV con i dati Della Prestazione Attività non retribuita
//==============================================================================
	
	String lCheckNonDeterminata = "";
	String lCheckDeterminata    = "";
	
	if(beneficioCumulo!= null && beneficioCumulo.getIdBeneficioCumulo()!=null)
	{
		if(beneficioCumulo.getFlagFrequenzaSettimanale()!=null)
		{
			if(beneficioCumulo.getFlagFrequenzaSettimanale().compareTo("N")==0 )
			{
				lCheckNonDeterminata = "checked";
			}
			else if(beneficioCumulo.getFlagFrequenzaSettimanale().compareTo("D")==0)
			{
				lCheckDeterminata = "checked";
			}
		}
		
	}

%>
<div id="divAttivitaNonRetribuita" style="display:block; position:relative; width:100%;" >
  <table width="90%">
    <tr>
      <td class="c" colspan="1">
        <table>
          <tr><td class="titolo" colspan=3 >Durata Prestazione Attività Non Retribuita</td></tr> 
          <tr>
            <td align="center">  
              <font class="label"> Mesi <BR> </font><input size="3" maxlength="3" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getNumMesiPrestazione(),"") %>"  type="text" name="<%=ICostantiBeneficiCumulo.CAMPO_NUM_MESI_PRESTAZIONE%>" onkeypress="return TicTabNumField(this,event)" >
            </td>  
            <td align="center">  
              <font class="label"> Giorni <BR> </font><input size="3" maxlength="3" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getNumGiorniPrestazione(),"") %>"  type="text" name="<%=ICostantiBeneficiCumulo.CAMPO_NUM_GIORNI_PRESTAZIONE %>" onkeypress="return TicTabNumField(this,event)" >
            </td>
          </tr>
        </table> 
      </td>
      <td class="c" colspan="1">
        <table>
          <tr><td class="titolo" colspan="3" >Modalità Esecuzione</td></tr> 
          <tr>
            <td align="center">  
              <font class="label"> Ore Settimanali <BR> </font><input size="3" maxlength="3" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getNumOreSettimanali(),"") %>"  type="text" name="<%=ICostantiBeneficiCumulo.CAMPO_NUM_ORE_SETTIMANALI %>" onkeypress="return TicTabNumField(this,event)" >
            </td>
          </tr>
        </table>
      </td> 
      <td class="c" colspan="1">
        <table>
          <tr><td class="titolo" colspan=3 >Frequenza Settimanale</td></tr> 
          <tr>
            <td align="left">  
              <font class="label"> Non Determinata </font><input type="radio" <%=lCheckNonDeterminata%> name="<%=ICostantiBeneficiCumulo.CAMPO_FLAG_FREQUENZA_SETTIMANALE %>" value="1" onClick="Javascript:checkFrequenzaSettimanale(this)">               
            </td>
          </tr>
          <tr>
            <td align="left">  
              <font class="label"> Determinata  </font><input type="radio" <%=lCheckDeterminata%> name="<%=ICostantiBeneficiCumulo.CAMPO_FLAG_FREQUENZA_SETTIMANALE%>" value="2" onClick="Javascript:checkFrequenzaSettimanale(this)">             
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td class="c" colspan="1">Ente Incaricato dei controlli</td>
      <td class="l" colspan="2">
         <textarea cols="60" rows="2" name="<%= ICostantiTipologiaOrario.CAMPO_ENTE_INCARICATO %>"><%=StringUtils.toStringJSP(lBeneficioCumulo.getEnteIncaricato()) %></textarea>
      </td>
    </tr>    
  </table>
</div>

<%
//==============================================================================
// DIV con i dati Della Tipologia Orario
//==============================================================================
%>
<div id="divTipologiaOraria" style="display:block; position:relative; width:100%;">
  <table width="90%">      
    <tr><td class="titolo" colspan=10>Tipologia Orario</td></tr>
    <tr>
      <td class="l"><input type="checkbox" value="01" <%=lBeneficioCumulo.isGiornoTipologia("01")?"checked":""%> name="<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_LUN  %>" onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
        Lunedì
      </td>
      <td class="l"> 
        <font class="label"> dalle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getDalleOre("01"))%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_LUN %>" id="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_LUN %>" >               
        <font class="label"> alle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getAlleOre("01"))%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_LUN %>" id="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_LUN %>" >               
      </td>
      <td class="l"><input type="checkbox" value="05" <%=lBeneficioCumulo.isGiornoTipologia("05")?"checked":""%> name="<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_VEN  %>" onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
        Venerdì 
      </td>
      <td class="l">
        <font class="label"> dalle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getDalleOre("05"))%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_VEN %>" id="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_VEN %>">                
        <font class="label"> alle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getAlleOre("05"))%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_VEN %>" id="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_VEN %>" >               
      </td>
    </tr>
    <tr>
      <td class="l"><input type="checkbox" value="02" <%=lBeneficioCumulo.isGiornoTipologia("02")?"checked":""%> name="<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_MAR  %>" onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
      Martedì
      </td>
      <td class="l">
         <font class="label"> dalle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getDalleOre("02"))%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_MAR %>" id="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_MAR %>" >
         <font class="label"> alle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getAlleOre("02"))%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_MAR %>" id="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_MAR %>" >
      </td>
      <td class="l"><input type="checkbox" value="06" <%=lBeneficioCumulo.isGiornoTipologia("06")?"checked":""%> name="<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_SAB  %>" onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
      Sabato
      </td>
      <td class="l">
        <font class="label"> dalle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getDalleOre("06"))%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_SAB %>" id="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_SAB %>" >
        <font class="label"> alle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getAlleOre("06"))%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_SAB %>" id="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_SAB %>" >               
      </td> 
    </tr>
    <tr>
      <td class="l"><input type="checkbox" value="03" <%=lBeneficioCumulo.isGiornoTipologia("03")?"checked":""%> name="<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_MER  %>" onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
      Mercoledì
      </td>
      <td class="l">
        <font class="label"> dalle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getDalleOre("03"))%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_MER %>" id="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_MER %>" >               
        <font class="label"> alle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getAlleOre("03"))%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_MER %>" id="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_MER %>" >               
      </td> 
      <td class="l"><input type="checkbox" value="07" <%=lBeneficioCumulo.isGiornoTipologia("07")?"checked":""%> name="<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_DOM %>" onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
      Domenica
      </td>
      <td class="l">
         <font class="label"> dalle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getDalleOre("07"))%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_DOM %>" id="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_DOM %>" >
        <font class="label"> alle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getAlleOre("07"))%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_DOM %>" id="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_DOM %>" >
      </td>
    </tr>  
    <tr>
      <td class="l"><input type="checkbox" value="04" <%=lBeneficioCumulo.isGiornoTipologia("04")?"checked":""%> name="<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_GIOV %>" onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
      Giovedì 
      </td>
      <td class="l">
        <font class="label"> dalle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getDalleOre("04"))%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_GIOV %>" id="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_GIOV %>" >
        <font class="label"> alle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lBeneficioCumulo.getAlleOre("04"))%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_GIOV %>" id="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_GIOV %>" >
      </td> 
    </tr>
  </table>
</div>


  <table cellspacing="2" cellpadding="2" width="90%">

    <%
    //==========================================================================
    // Descrizione dello stato in cui si trova il dato (solo per modifica)
    //==========================================================================
    if (lBeneficioCumulo!=null && lBeneficioCumulo.getIdBeneficioCumulo()!=null)
    {
      String lDescStato = "";
      if      ( lBeneficioCumulo.getFlagStato().equals("E")){lDescStato = "Dato Estratto dal fascicolo originale";}
      else if ( lBeneficioCumulo.getFlagStato().equals("I")){lDescStato = "Dato Inserito manualmente dopo l'estrazione";}
      else if ( lBeneficioCumulo.getFlagStato().equals("M")){lDescStato = "Dato estratto modificato";}
      else if ( lBeneficioCumulo.getFlagStato().equals("C")){lDescStato = "Dato estratto cancellato";}
    %>
    <tr>
      <td class="l"><center>Situazione</center></td>
      <td class="l"> <%=lDescStato %></td> 
    </tr>
    <% } %>
  
    <%
    //========================================================================== 
    // Campo note visualizzato sia in inserimento sia in modifica dove l'utente
    // può motivare l'intervento sui dati su cui sta intervenendo
    //========================================================================== 
    %>
    <tr>
      <td class="l">Motivo Inserimento/Modifica</td>
      <td class="l">
        <textarea cols="100" rows="6" name="<%=ICostantiBeneficiCumulo.CAMPO_MOTIVO_MODIFICA%>">
        	<%=StringUtils.toStringJSP(lBeneficioCumulo.getMotivoModifica()) %>
        </textarea>
      </td>
    </tr>
    
<%	if(modalita.equals("I") )
	{	%>     
	    <tr>
	      <td align="left">
	        <input class="bottone" type="submit" name="conferma" title="Inserisci Beneficio" value="Conferma">
	      </td>
	    </tr>
<%	}
	else if(modalita.equals("M") )
	{	%>
	    <tr>
	      <td align="left">
	        <input class="bottone" type="submit" name="conferma" title="Modifica beneficio" value="Conferma">
	      </td>
	    </tr>		    
<%	} %>
  </table>
</form>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("<%=lNomeForm%>");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
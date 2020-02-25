<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo "%>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="RichiestaSORV"    		scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="Titolo"	    		scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>  
<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>

<!-- 				LoadInsRichiestaSORVAltro				 --> 
<% 
//============================================================================== 
// Form per l'inserimento e la modifica delle richieste alla SORV - Altre Richieste
//============================================================================== 

String lSentenza="";
if("M".equals(modalita) && Titolo!=null && Titolo.getIdTitoloCumulato()!=null)
{
	lSentenza += Titolo.getDescrTipoProvvedimento()+" N. "+Titolo.getAnnoSentenza() +"/"+Titolo.getNumeroSentenza();
	lSentenza += "  Emessa da "+Titolo.getDescrTipoAutoritaEmittente()+" di "+Titolo.getDescrLuogoEmittente();
	lSentenza += "  il "+DateUtils.getDateToString(Titolo.getDataProvvedimento(),"dd-MM-yyyy");
	
	if(Titolo.getDataIrrevocabilita() != null)
	{
		lSentenza += "  Definitiva il "+DateUtils.getDateToString(Titolo.getDataIrrevocabilita(),"dd-MM-yyyy");
	}
}

%>

<html>
<head>
  <title> Gestione Altre Richieste alla SORV </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" >

    function eseguiFunzione(action)
    {
      document.InsRicSORValtro.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.InsRicSORValtro.submit();
    }
   
    var lmodo = '<%=modalita%>';
    
    function ElencoTitoliPopup (a_form_name, a_form_type)
    {
		//alert('ElencoTitoliPopup - lmodo = '+lmodo);   	 	
   	 	if(lmodo == "I")
   	 	{	
	   	 	if(document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_TITOLO_CUMULATO%>.value != "")
	 	 	{	
	 	 		$('#divdatiTitolo').hide();
	 	 		$('#tabdatiTitolo').hide();
	 	 		document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_TITOLO_CUMULATO%>.value = "";
	 	 	}
	 	 	else
	 	 	{
	 	 		$('#divdatiTitolo').show();
	 	 		$('#tabdatiTitolo').show();
		      
		      <%
		      	String lStrParametri = "";
		      	lStrParametri +="&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo();
		      %>
		      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActCaricaElencoTitoliPerSelezione&ParentFormName="+a_form_name+"&ParentFormType="+a_form_type+"<%=lStrParametri%>"
		                          , "Elenco_Titoli"
		                          , "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=yes, width=950, height=400");
	 	 	}  
   	 	}
   	 	else if(lmodo == "M")
   	 	{
   	 		<%
	      	String lStrParametriM = "";
	      	lStrParametriM +="&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo();
	       %>
	      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActCaricaElencoTitoliPerSelezione&ParentFormName="+a_form_name+"&ParentFormType="+a_form_type+"<%=lStrParametriM%>"
	                          , "Elenco_Titoli"
	                          , "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=yes, width=950, height=400");
   	 	}	
    }     

    function Verify() 
    { 
   		// Data Emissione
       if (document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
         	 document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
       if (document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
           	document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value;

       var data_to_verify = document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE%>.value;
       if (data_to_verify=='//' )
       {
           alert('Indicare la Data Richiesta');
           document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       }
       
       if (!ControllaData(data_to_verify) )
       {
           alert('Data Richiesta non valida');
           document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       } 
       
       // Data Emissione deve essere <= Data del Giorno
       var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
 	   if(!CompareDate(data_to_verify, data_od))
 	   {
 			alert('Data Richiesta NON può essere superiore alla Data Odierna');
 			document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
         	return false;
 	   }
 	   
 		// Campo Descrizione Richiesta
	 	if(document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI %>.value == "" || 
	   	   document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI %>.value.length == 0	)
	    {
	   		alert("Descrizione Richiesta Obbligatoria");
	   		document.InsRicSORValtro.<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI%>.focus();
	   		return false;
	    }
 	   
 		// Titolo Cumulato di Riferimento
	 	if(document.InsRicSORValtro.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO %>.value == "" || 
	   	   document.InsRicSORValtro.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO %>.value.length == 0	)
	    {
	   		alert("scegliere Titolo cumulato di Riferimento, \ncliccando il Link 'Seleziona Titolo Esecutivo'");
	   		return false;
	    }
      
      	return true; 
    } 
    
    function Inizia()
    {
   	 	//alert('inizia - lmodo = '+lmodo);
   	 	if(lmodo == "M")
   	 	{
   	 		$('#divdatiTitolo').show();
    	 	$('#tabdatiTitolo').show();
   	 	}	
    }

  </script>
</head>

<body class="corpo" onLoad="javascript:Inizia();">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;

        <% if (modalita.equals("I") ) { %>
        <font class="campo">Inserimento Richiesta alla SORVEGLIANZA - Altre Richieste &nbsp;</font>
        <% } else if( modalita.equals("M") ) { %>
        <font class="campo">Modifica Richiesta alla SORVEGLIANZA - Altre Richieste &nbsp;</font>
        <% } %>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
 
  <br>
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>
 
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InsRicSORValtro">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRichiestaSORVAltro">
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">

  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA%>" value="02">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>" value="014">

  <%
  if("M".equals(modalita) )
  {	%>
  	<input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO%>" value="<%=RichiestaSORV.getIdRichiestePmInCumulo() %> ">
  	<input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO %>"	value="<%=RichiestaSORV.getTitIdTitoloCumulato() %>" >
<%}
  else if("I".equals(modalita) )
  {	 
	// in Input  valorizzato dalla Popup	%>
	<input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO %>" value="" >
<%} %>
  
  <%
  //============================================================================
  // Seleziona dalla lista
  //============================================================================
  %>
  <table width="95%" align="center">
    <tr>
      <td class="l" colspan="1">
        <a href="Javascript:ElencoTitoliPopup('InsRicSORValtro','<%=ICostantiRichiestePmInCumulo.FORM_TYPE_RIC_APPL_PA%>');">
          Seleziona Titolo Esecutivo<img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>
  </table>
  
  <div id="divdatiTitolo" style="display:block">
    <table width="95%" align="center" id="tabdatiTitolo" style="display:none">
      <tr><td colspan="2" class="Titolonocap">Dati Titolo</td></tr>
      <tr>
      	<td class="l" width="15%" >In Relazione al Titolo </td>
		<td class="L" >
 		  <input type="text" Title="Estremi sentenza" value="<%=lSentenza%>" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_TITOLO_CUMULATO%>" size="135" maxlength="135" >
        </td>
      </tr>
    </table>
  </div> 
  
  <%
  //============================================================================
  // 
  //============================================================================
  %>
  <br>  
  	<table width="95%" align="center">
      <tr><td class="Titolo" colspan="4">Dati Richiesta al Magistrato di Sorveglianza</td></tr>
      <tr>
      	<td class="l" width="15%" >Data Richiesta </td>
 <% if (modalita.equals("I") ) 
 	{ %>     	
      	<td class="L" >
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      	</td>
 <% }
 	else if (modalita.equals("M") ) 
 	{ %>
 		<td class="L" >
          <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaSORV.getDataEmissione(), "dd"),"" )%>"   
          	type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaSORV.getDataEmissione(), "MM"), "" )%>"   
          	type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaSORV.getDataEmissione(), "yyyy"), "" )%>" 
          	type="text" size="4" maxlength="4" name="<%= ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      	</td>
 <% }  %> 	     	
      </tr>
	  <tr><td>&nbsp;</td></tr>
	  	<tr>
      	<td class="l" colspan="1" width="15%" > Descrizione </td>
      	<td class="l" colspan="3" >
          <textarea cols="100" rows="3" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI %>" ><%=StringUtils.toStringJSP(RichiestaSORV.getMotivazioni(), "" )%></textarea>
      	</td>
      </tr>
    </table>  	
  <br>
  
  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>
  </table>


</FORM>

</body>
</html>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("InsRicSORValtro");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
 
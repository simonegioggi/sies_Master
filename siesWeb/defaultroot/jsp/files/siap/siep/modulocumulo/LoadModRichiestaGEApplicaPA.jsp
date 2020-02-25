<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="RichiestaAlGE"    		 scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="TitoloPA"	    		 scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>   
<jsp:useBean id="TipoAnnotazione" 		 scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoPenaAccessoria"     scope="request" class="java.lang.String"/>
<jsp:useBean id="DurataPenaAcc"		     scope="request" class="java.lang.String"/> 
<jsp:useBean id="TipoAnnotazioneIndulto" scope="request" class="java.lang.String"/>
<jsp:useBean id="listaDPR"               scope="request" class="java.lang.String"/>

<!-- 						LoadModRichiestaGEApplicaPA						 -->  
<%
  //============================================================================== 
  // Form per l'inserimento e la modifica delle richieste al GE di 
  // Applicazione di Pena Accessoria
  //============================================================================== 

  %>

<html>
<head>
  <title> Gestione Cumulo - Richieste al GE - Applicazione Pena Accessoria</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" >

    function eseguiFunzione(action)
    {
      document.ModRicGEApplPA.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.ModRicGEApplPA.submit();
    }
   
    
    function ElencoTitoliPopup (a_form_name, a_form_type)
    {
 		$('#divTitoloOld').hide();
 		$('#tabTitoloOld').hide();

 		$('#divdatiTitolo').show();
 		$('#tabdatiTitolo').show();
   	 		
 		<%String lStrParametri = "";
      	lStrParametri +="&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo();%>
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActCaricaElencoTitoliPerSelezione&ParentFormName="+a_form_name+"&ParentFormType="+a_form_type+"<%=lStrParametri%>"
  	                          , "Elenco_Titoli"
   	                          , "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=yes, width=990, height=400");
    }     

    function selTipoApplicazione(obj) 
    {
      if (obj.value=="-" || obj.value=="029") {
        $('#divIndulto').hide();
      }
      else if (obj.value=="030") {
        $('#divIndulto').show();
      }
    }  
    
    function Verify() 
    { 

   		// Data Emissione
       if (document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
         	 document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
       if (document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
           	document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value;

       var data_to_verify = document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE%>.value;
       if (data_to_verify=='//' )
       {
           alert('Indicare la Data Richiesta');
           document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       }
       
       if (!ControllaData(data_to_verify) )
       {
           alert('Data Richiesta non valida');
           document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       } 
       
       // Data Emissione deve essere <= Data del Giorno
       var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
 	   if(!CompareDate(data_to_verify, data_od))
 	   {
 			alert('Data Richiesta NON può essere superiore alla Data Odierna');
 			document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
         	return false;
 	   }
 	   
 		// Tipo Richiesta
 	   if(document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>.value == "-" && 
 	   	  document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>.value.length < 2	)
   	   {
 	   		alert('Selezionare Tipo Richiesta');
 	   		document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>.focus();
 	   		return false;
   	   }
 	   
 		// Tipo Pena Accessoria
 	   if(document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>.value == "-" && 
 	   	  document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>.value.length < 2	)
   	   {
 	   		alert('Selezionare Tipo Pena Accessoria da Applicare');
 	   		document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>.focus();
 	   		return false;
   	   }
 	   
 		// DPR (solo se scelta = CONDONO)
 		if(document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>.value == "030")
   	 	{
	 		if(document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>.value == "-" && 
	  	   	   document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>.value.length < 2	)
	    	{
	  	   		alert('Selezionare Estremi Beneficio DPR');
	  	   		document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>.focus();
	  	   		return false;
	    	}
   	 	}
 		
 		// Titolo Cumulato di Riferimento
	 	if(document.ModRicGEApplPA.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO %>.value == "" || 
	   	   document.ModRicGEApplPA.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO %>.value.length == 0	)
	    {
	   		alert("scegliere Titolo cumulato di Riferimento, \ncliccando il Link 'Seleziona Titolo Esecutivo'");
	   		return false;
	    }
      
      	return true; 
    }
    
    function seleziona(valore) 
    {
   	  if(valore=="-" || valore=="029")
   	  {
   		  $('#divIndulto').hide();
   	  }
   	  else if(valore=="030")
   	  {
   		  $('#divIndulto').show();
   	  }	

    }

  </script>
</head>

<body class="corpo" onLoad="javascript:seleziona(document.ModRicGEApplPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>.value);">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Modifica Richiesta Applicazione di Pena Accessoria &nbsp;</font>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE')">
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
 
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ModRicGEApplPA">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRichiestaGEApplicaPACum">
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA%>" value="01">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO%>" value="<%=RichiestaAlGE.getIdRichiestePmInCumulo()%>">
  
  <%//	 Campi Hidden valorizzati dalla Popup  %>
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO %>" value="<%=TitoloPA.getIdTitoloCumulato()%>" >
  
  <%
//============================================================================
// Seleziona dalla lista
//============================================================================
  %>
  <table width="95%" align="center">
    <tr>
       <td class="l" colspan="1" id="linkTitolo">
        <a href="Javascript:ElencoTitoliPopup('ModRicGEApplPA','<%=ICostantiRichiestePmInCumulo.FORM_TYPE_RIC_APPL_PA%>');">
          Seleziona Titolo Esecutivo<img src="/images/filefolder.gif" border=0></a>
      </td>
     </tr>
  </table>
  
<!-- 				ESTREMI TITOLO di riferimento SCELTO IN INSERIMENTO 					 -->
  <div id="divTitoloOld" style="display:block">	  
    <table width="95%" align="center" id="tabTitoloOld" style="display:block">
      <tr><td colspan="2" class="Titolonocap">Titolo di riferimento</td></tr>
<%
	String AnnoNumeroRif = TitoloPA.getAnnoSentenza() +"/"+TitoloPA.getNumeroSentenza();
%>      
      <tr>
        <td class="l" colspan="8">
          <font class="label"><%=StringUtils.toStringJSP(TitoloPA.getDescrTipoProvvedimento() )%>&nbsp;N. </font>
          <font class="campo"><%=AnnoNumeroRif%></font>&nbsp;
          &nbsp;<font class="label"> del </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(TitoloPA.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
          &nbsp;<font class="label"> Emessa da </font>
          <font class="campo"><%=StringUtils.toStringJSP(TitoloPA.getDescrTipoAutoritaEmittente(), "") %></font>
          <font class="label">&nbsp;di&nbsp; </font>
          <font class="campo"><%=StringUtils.toStringJSP(TitoloPA.getDescrLuogoEmittente(), "") %></font>

          &nbsp;<font class="label"> Irrevocabile il  </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(TitoloPA.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
        </td>
      </tr>
    </table>
  </div>

<!-- 				ESTREMI TITOLO di riferimento EVENTUALMENTE MODIFICATO 					 -->     
  <div id="divdatiTitolo" style="display:block">
    <table width="95%" align="center" id="tabdatiTitolo" style="display:none">
      <tr><td colspan="2" class="Titolonocap">Titolo di riferimento</td></tr>
      <tr>
      	<td class="l" width="15%" >In Relazione al Titolo </td>
		<td class="L" >
 		  <input type="text" Title="Estremi sentenza" value="" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_TITOLO_CUMULATO%>" size="135" maxlength="135" >
        </td>
      </tr>
    </table>
  </div>  
  
  <br>
  <table width="95%" align="center">
    <tr><td class="Titolo" colspan="4">Dati Richiesta al Giudice dell' Esecuzione</td></tr>
    <tr>
      <td class="l" width="15%" >Data Richiesta </td>
      <td class="L" >
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaAlGE.getDataEmissione(),"dd" ), "" )%>"   
        	type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaAlGE.getDataEmissione(),"MM" ), "" )%>"   
        	type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaAlGE.getDataEmissione(),"yyyy" ), "" )%>" 
        	type="text" size="4" maxlength="4" name="<%= ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l" >Tipo richiesta &nbsp;</td>
      <td class="l" >
        <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>" onChange="javascript:selTipoApplicazione(this);">
			<%=TipoAnnotazione%>
        </select>
      </td>
    </tr>
    <tr><td class="Titolo" colspan="4">Estremi Pena Accessoria da Applicare</td></tr>
    <tr>
      <td class="l" colspan="1" width="160px">Tipo di Pena Accessoria: &nbsp;</td>
      <td class="l" colspan="3">
        <select class="small" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>" >
          <%=TipoPenaAccessoria%>
        </select>
      </td>
    </tr> 
    
    <tr>
      <td class="l" >Tipo Durata &nbsp;</td>
      <td class="l">
        <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_DURATA_PA %>">
			<%=DurataPenaAcc%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Durata</td>
      <td class="l">
        Anni &nbsp;<input maxlength=2 size=2 Title="Anni Durata" 
                          value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumAnniPa(), "" )%>" 
                          onkeypress="return TicTabNumField(this,event)"
                          type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_PA %>">&nbsp;&nbsp;
        Mesi &nbsp;<input maxlength=2 size=2 Title="Mesi Durata" 
                          value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumMesiPa(), "" )%>" 
                          onkeypress="return TicTabNumField(this,event)"
                          type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_PA %>">&nbsp;&nbsp;
        Giorni &nbsp;<input maxlength=2  size=2 Title="Giorni Durata" 
                            value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumGiorniPa(), "" )%>" 
                            onkeypress="return TicTabNumField(this,event)"
                            type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_PA %>">&nbsp;&nbsp;
      </td>
    </tr>    
  </table> 
  
  
  <%
  //============================================================================
  // Amnistia/Indulto
  //============================================================================
  %>
  <div id="divIndulto"  style="display:none">
    <table width="95%" align="center">
      <tr>
        <td colspan="3" class="Titolonocap">Estremi del condono</td>
      </tr>
      <tr>
        <td class="l">Estremi Beneficio <font class="ob">(*)</font> :&nbsp;</td>
        <td class="l">
          <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_INDULTO %>">
            <%=TipoAnnotazioneIndulto%>
          </select>
        </td>
        <td class="l">
          <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>" >
            <%=listaDPR%>
          </select>
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
	<tr>
      <td class="l" colspan="1" width="15%" > Motivazioni </td>
      <td class="l" colspan="3" >
        <textarea cols="100" rows="3" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI %>"><%=StringUtils.toStringJSP(RichiestaAlGE.getMotivazioni(), "" )%></textarea>
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
  var frmvalidator  = new Validator("ModRicGEApplPA");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
 
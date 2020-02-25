<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaAccessoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiesteInviateCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="IstruttoriaCumulo" 	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="modalita" 			   	scope="request" class="java.lang.String"/>
<jsp:useBean id="RichiestaGE"     		scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="TitoloPA"     			scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="ProvvGECum"     		scope="request" class="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"/>
<jsp:useBean id="UfficioEmittente"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoPenaAccessoria_D" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="DurataPenaAcc_D"     	scope="request" class="java.lang.String"/>


<!-- 							LoadInsDecisioneDelGESostPenaAcc								 -->
<%
//==============================================================================
//  Form con le funzioni di Inserimento/Modifica della decisione del G.E.
//	a seguito di Richiesta da parte del P.M. di :
//  Sostituzione Pena Accessoria (Gestione Cumulo)
//==============================================================================

RichiesteInviateCumModel lRicInv = null;
if(RichiestaGE!=null && RichiestaGE.getRichiesteInviateCum()!=null)
{
	lRicInv = (RichiesteInviateCumModel)RichiestaGE.getRichiesteInviateCum();
	//LogF3B.getLogger().debug(" --XX-- Richiesta Inviata = "+lRicInv);
}

//Data Richiesta al GE
String lDataRich = StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaGE.getDataEmissione(),"dd/MM/yyyy"));

%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo - Decisione del GE su Richiesta del PM </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script> 
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="JavaScript">
  	var isCaricamento = "S";
  	
	//Apre la finestra con la Lista delle Sedi uffici in base alla tipologia di Ufficio Selezionata
  	function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
  	{
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  	}
	
    //==========================================================================
    // Ritorna alla Griglia Delle Richieste al GE
    //==========================================================================
    function tornaIndietro(action)
    {
      document.indietroForm.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.indietroForm.submit();
    }
    
    function caricaDatiRichiesta(){
      //alert ("isCaricamento = "+isCaricamento);
      
      document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>.value = "<%=StringUtils.toStringJSP(RichiestaGE.getCodTipoPenaAccessoria(), "-" )%>" ; 
      document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_DURATA_PA %>.value = "<%=StringUtils.toStringJSP(RichiestaGE.getCodTipoDurataPa(), "-" )%>" ; 
      
      document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_PA %>.value = "<%=StringUtils.toStringJSP(RichiestaGE.getNumAnniPa(), "" )%>" ;
      document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_PA %>.value = "<%=StringUtils.toStringJSP(RichiestaGE.getNumMesiPa(), "" )%>" ;
      document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_PA %>.value = "<%=StringUtils.toStringJSP(RichiestaGE.getNumGiorniPa(), "" )%>" ; 
    }
    
    function tipoDecisione(){
      
      var tipoDec = $('input[name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>"]:checked').val();
      //alert ("tipoDecisione = "+tipoDec);
      //alert ("isCaricamento = "+isCaricamento);
      
      if(tipoDec == "C" || tipoDec == "D")
      {
        //disable
        document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>.disabled = false ; 
        document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_DURATA_PA %>.disabled = false ;
        
        document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_PA %>.disabled = false ;
        document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_PA %>.disabled = false ;
        document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_PA %>.disabled = false ;
        
        if (tipoDec == "C" && isCaricamento=="N") 
          caricaDatiRichiesta();
        
      }
      else {
        // Rigetto - Inammissibilità
        document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>.value = "-" ; 
        document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_DURATA_PA %>.value = "-" ; 
        
        document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_PA %>.value = "" ;
        document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_PA %>.value = "" ;
        document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_PA %>.value = "" ; 
        
        //disable
        document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>.disabled = true ; 
        document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_DURATA_PA %>.disabled = true ;
        
        document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_PA %>.disabled = true ;
        document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_PA %>.disabled = true ;
        document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_PA %>.disabled = true ;      
        
      }

      isCaricamento = "N";
      
    }
    
    
    function Verify() 
    { 
   		// Anno e Numero Procedimento SIGE
        if(document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.value == '' && 
           document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.value != ''	 )
        {
        	alert('Anno Procedimento G.E. NON valido');
            document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.focus();
            return false;
        } 
         
        if( document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.value != '' && 
        	document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.value == ''	 )
        {
        	alert('Numero Procedimento G.E. NON valido');
            document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.focus();
            return false;
        }
         
        if(document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.value == '' && 
           document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.value == ''	 )
        {
  			alert('digitare Anno e Numero Procedimento G.E.');
            document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.focus();
            return false;
        }

 	   // Data Emissione Procedimento SIGE
       if ( document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value.length==1)
         	document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value='0'+document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value;
       if ( document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value.length==1)
         	document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value='0'+document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value;

       var data_to_verify = document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value+'/'+document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value+'/'+document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE_D%>.value;
       if (data_to_verify=='//' )
       {
           alert('Indicare la Data Emissione Procedimento');
           document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.focus();
           return false;
       }
       
       if (!ControllaData(data_to_verify) )
       {
           alert('Data Emissione Procedimento NON valida');
           document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.focus();
           return false;
       }
       
    	// Data Emissione Procedimento SIGE deve essere <= Data del Giorno
    	var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
		if(!CompareDate(data_to_verify, data_od))
		{
			alert('Data Emissione Procedimento SIGE NON può essere superiore alla Data Odierna');
			document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D %>.focus();
      		return false;
		}
		
		// Data della Richiesta deve essere <= Data Emissione Procedimento SIGE 
	 	var data_Ric = '<%=lDataRich%>';
	 	if(!CompareDate(data_Ric, data_to_verify))
	 	{
			alert('Data Emissione Procedimento SIGE DEVE essere superiore o uguale \nalla Data Richiesta del PM al G.E.');
			document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D %>.focus();
     		return false;
	 	}
       
       // Tipo Ufficio e sede Ufficio Emittente
       if(document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.value == '-' || 
      	 document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.value.length == 1	 )
       {
      	 	alert('Ufficio Emittente non valido');
          	document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.focus();
          	return false;
       } 
       
       if(document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.value == '-' || 
      	 document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.value.length == 1 ||
      	 document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.value.length == 0 )
       {
      	 	alert('Sede Ufficio Emittente non valida');
          	document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
          	return false;
       }
       
       // Tipo Pena Accessoria
       var tipoDec = $('input[name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>"]:checked').val();

       if( tipoDec == "C" || tipoDec == "D")
       {
         if( document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>.value == "-" && 
             document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>.value.length < 2 )
         {
            alert('Selezionare Tipo Pena Accessoria Applicata in Sostituzione');
            document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>.focus();
            return false;
         }
       }
       
       return true;
    }
    
        // script eseguito sulla onload della pagina
    $(document).ready(function(){
      tipoDecisione();      
    });
    
  </script>

</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
<%	if("I".equals(modalita) )
	{	%>
        	<font class="campo">Inserimento Decisione del G.E. su Richiesta Sostituzione Pena Accessoria</font>
<% 	}	
	else if("M".equals(modalita) )
	{	%>
			<font class="campo">Modifica Decisione del G.E. su Richiesta Sostituzione Pena Accessoria</font>		
<%	}	%>	        
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia delle Richieste al GE-->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>

<% // INCLUDE DEL DETTAGLIO FASCICOLO%>
	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>

<% // INCLUDE DEL DETTAGLIO DELL'ISTRUTTORIA %>
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>


<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="indietroForm">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
</form>


<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="LoadInsDecGESostPA">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciDecisioneDelGECumulo">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="<%=RichiestaGE.getIdRichiestePmInCumulo() %>" >
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA %>" value="01" >
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PROVVEDIMENTO %>" value="03" >
  <input type="hidden" name="modalita" value="<%=modalita%>" >

<%	// =======================================================
	//		Dati sui Titoli coinvolti nella Richiesta	
	// =======================================================
%>

  <table cellpadding="2" cellspacing="2" width="98%" align="center" style="border:0;">
     <tr><td class="Titolo" colspan="100%">In relazione al Titolo</td></tr>
<%      	
  String AnnoNumero ="";
  AnnoNumero = TitoloPA.getAnnoSentenza() +"/"+TitoloPA.getNumeroSentenza();
%> 
    <tr>
      <td class="l" colspan="8">
        <font class="label"><%=StringUtils.toStringJSP(TitoloPA.getDescrTipoProvvedimento() )%>&nbsp;N. &nbsp; </font>
        <font class="campo"><%=AnnoNumero%></font>&nbsp;
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
<%
    Iterator itx = TitoloPA.getPeneAccessorieCumulo().iterator();
    while(itx.hasNext())
    {
      PenaAccessoriaCumuloModel lPACum = (PenaAccessoriaCumuloModel)itx.next();	
%>	
      <tr>
	     <input type="hidden" name="<%=ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO %>" value="<%=StringUtils.toStringJSP(lPACum.getIdPenaAccessoriaCumulo()) %>" >
	     <td class="l" >
	       <font class="label" style="color:red; text-align:left"> Pena Accessoria: </font>
	       &nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPACum.getDescrTipoPenaAccessoria(),"") %></font>&nbsp;
	       
<%		if(lPACum.getDurata()!= null && !"P".equals(lPACum.getDurata() ) )
		{ 	%>         
        	<font class="label"> per la durata </font>
       <%	if("-".equals(lPACum.getDescrDurata()) ) 
       		{	%>
       			<font class="label"> di: </font>&nbsp;&nbsp; 
       <%	}
       		else
       		{	%>			
        		&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPACum.getDescrDurata() )%></font>&nbsp;&nbsp;
		<%  }
       	
       		if(lPACum.getNumAnni()!=null && lPACum.getNumAnni().compareTo(BigDecimal.ZERO) > 0)
	  	  	{ %> 	
         		Anni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPACum.getNumAnni() ) %></font>&nbsp;&nbsp;
        <% 	}
       	
       		if(lPACum.getNumMesi()!=null && lPACum.getNumMesi().compareTo(BigDecimal.ZERO) > 0)
	   	  	{	%> 	
         		Mesi&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPACum.getNumMesi() ) %></font>&nbsp;&nbsp;
      <%	}
       	
       		if(lPACum.getNumGiorni()!=null && lPACum.getNumGiorni().compareTo(BigDecimal.ZERO) > 0)
	   	  	{	%>  	
         		Giorni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPACum.getNumGiorni() ) %></font>&nbsp;&nbsp;
       <%	} %>  	
<%		}
		else if(lPACum.getDurata()!= null && "P".equals(lPACum.getDurata()) )
  		{	%>
				<font class="label"> Durata: </font>&nbsp;
				<font class="campoLow"><%=StringUtils.toStringJSP(lPACum.getDescrDurata() )%></font>&nbsp;
<%		}	%>	       
 			          
	      </td>
	    </tr>		
<%	}  %>
   	
    <tr><td></td></tr>   
<%
  //========================================================================
  //  Dati esclusivi della Richiesta
  //========================================================================
  %>
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap">Dati della richiesta</td></tr>
    <tr>
      <td class="l" width="200px">Data Richiesta</td>
      <td class="l" colspan="2">
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaGE.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>
      </td>
    </tr>  	
    <tr>
      <td class="l" width="200px">Tipo Richiesta:</td>
      <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrTipoAnnotazione() )%></font>
      </td>
    </tr>
    
<%	if("025".equals(RichiestaGE.getCodTipoAnnotazione()) )
	{	%>     
	  <tr>
      	<td class="l" width="200px">Estremi Beneficio</td>
      	<td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrBeneficio())%>&nbsp;&nbsp;&nbsp;<%=StringUtils.toStringJSP(RichiestaGE.getDescrDpr())%></font>
      	</td>
      </tr>
<%	} %>         

    <tr>
      <td class="l">Pena Accessoria in Sostituzione</td>
	  <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrTipoPenaAccessoria())%></font>
      </td>	      
    </tr>
	<tr>
      <td class="l">Durata (Tipo / Periodo):</td>
	  <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrTipoDurataPa())%></font>&nbsp;&nbsp;/&nbsp;
 <%	if(RichiestaGE.getNumAnniPa()!=null && RichiestaGE.getNumAnniPa().compareTo(BigDecimal.ZERO) > 0)
  	{ %> 	
 		Anni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(RichiestaGE.getNumAnniPa() ) %></font>&nbsp;&nbsp;
<%  }
	
	if(RichiestaGE.getNumMesiPa()!=null && RichiestaGE.getNumMesiPa().compareTo(BigDecimal.ZERO) > 0)
  	{	%> 	
 		Mesi&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(RichiestaGE.getNumMesiPa() ) %></font>&nbsp;&nbsp;
<%	}
	
	if(RichiestaGE.getNumGiorniPa()!=null && RichiestaGE.getNumGiorniPa().compareTo(BigDecimal.ZERO) > 0)
  	{	%>  	
 		Giorni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(RichiestaGE.getNumGiorniPa() ) %></font>&nbsp;&nbsp;
<%	} %>       
      </td>	      
    </tr>
    <%	if(RichiestaGE.getMotivazioni()!=null && !RichiestaGE.getMotivazioni().equals("") )
	{	%>
	<tr>
      <td class="l" colspan="1">Motivazioni :  </td>
      <td class="l" colspan="3">
      	<font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getMotivazioni(),"" )%></font>
      </td>
    </tr>  		
<%	} %>	
  </table>
  
 <%
  //=======================================================
  //  				Eventuale Richiesta Inviata
  //=======================================================
 %>
  
<%	if(lRicInv!=null && lRicInv.getIdRichiesteInviateCum()!=null )
	{	%>  
  <table width="95%" align="center" style="display:block">
  	<tr><td> </td></tr>
    <tr>
      <td class="l" colspan="1" width="200px">Inviata a : </td>
      <td class="l" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(lRicInv.getDescrUfficioDest(),"")%></font>
      	 di <font class="campo"><%=StringUtils.toStringJSP(lRicInv.getDescrLuogoDest(),"")%></font>
      </td>
      <td class="l" colspan="1">in data: <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRicInv.getDataEmissione(),"dd-MM-yyyy"),"-")%></font></td>
    </tr>
<%	  if( !("null").equals(lRicInv.getContenuto()) )
	  {		%>
	  <tr>
	  	<td class="l" colspan="1" width="200px">Contenuto </td>
        <td class="l" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lRicInv.getContenuto(),"")%></font></td>
      </tr>
 <%	  } %>           
  </table>
<%	} %>  
  	
  	<br>
  
<!-- 							Inizio Dati Inseribili/Modificabili										 -->  
  <%
  //========================================================================
  //  Dati esclusivi della Decisione da Inserire / Modificare
  //========================================================================
  %>  
<tr>
  <td colspan="100%" align="center">

<div id="idDivDecisioneGE"  style="display:block" >   
  <table width="95%" align="center">
    <tr><td colspan="3" class="Titolonocap">Decisione del GE</td></tr>      
    <tr>
      <td class="l">Ordinanza <font class="ob">(*)</font> :</td>
	  <td class="l">
        Anno/Numero Procedimento SIGE
        <input type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>" size=4 maxlength=4 value="<%=StringUtils.toStringJSP(ProvvGECum.getAnnoProvv(),"") %>" 
          		onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        /
        <input type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>" size=8 maxlength=6 value="<%=StringUtils.toStringJSP(ProvvGECum.getNumeroProvv(),"") %>" >
      </td>
      <td class="l">
        <font class="label">Data emissione Procedimento</font>
          &nbsp;&nbsp;
         <input type="text"  title="Giorno di arrivo documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D %>" maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvGECum.getDataD(), "dd"), "") %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
         /
         <input type="text"  title="Mese di arrivo documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D %>" maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvGECum.getDataD(), "MM"), "") %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
         /
         <input type="text"  title="Anno di arrivo documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE_D %>" maxlength="4" size="4" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvGECum.getDataD(), "yyyy"), "") %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
      </td>	
    </tr>
    
    <tr>
      <td class="l">Ufficio Emittente <font class="ob">(*)</font> :</td>
      <td class="l" colspan="1">
          <select Title="Ufficio Emittente" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>">
            <%=UfficioEmittente%>
          </select>
      </td>
      <td class="l">Sede Ufficio Emittente <font class="ob">(*)</font> :
          <font class="campo">
            <input type="text" Title="Luogo Ufficio Emittente" size="35"
                   name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%=StringUtils.toStringJSP(ProvvGECum.getDescrLuogoEmittente(), "") %>" >
            <a href="Javascript:ListaUfficiComuni('LoadInsDecGESostPA','<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>',
            					document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>[document.LoadInsDecGESostPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.options.selectedIndex].value);">
              <img src="/images/filefolder.gif" border=0>
            </a>
          </font>
      </td>
    </tr>
  </table>
    
    <table width="95%" align="center">
      <tr>
        <td class="l" colspan=3>
<% 
		String checkC = "";
      	String checkD = "";
      	String checkR = "";
      	String checkI = "";
      
      	if("C".equals(ProvvGECum.getFlagConforme()) ){
        	checkC = "checked";
      	}
      	else if("D".equals(ProvvGECum.getFlagConforme()) ) { 
        	checkD = "checked";
      	}
      	else if("I".equals(ProvvGECum.getFlagConforme()) ) { 
        	checkI = "checked";
      	}
      	else if("R".equals(ProvvGECum.getFlagConforme()) ) { 
        	checkR = "checked";
      	}
      	else { checkC = "checked"; }
%>  
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="C" <%=checkC%> onClick="javascript:tipoDecisione()">in conformita' alla richiesta del PM &nbsp;&nbsp;	<!-- Conforme -->
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="D" <%=checkD%> onClick="javascript:tipoDecisione()">in difformita' alla richiesta del PM &nbsp;&nbsp;	<!-- Difforme -->
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="R" <%=checkR%> onClick="javascript:tipoDecisione()">rigetta &nbsp;&nbsp;	<!-- Rigetta -->
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="I" <%=checkI%> onClick="javascript:tipoDecisione()">dichiara inammissibile &nbsp;&nbsp;	<!-- Inammissibile -->	
	        
        </td>
      </tr>
    </table>
    
  <table width="95%" align="center">
    <tr>
      <td colspan=4>
        <hr width="100%">
      </td>
    </tr>
    
    <tr><td class="Titolo" colspan="4" style="text-align:left" >Pena  Accessoria  Applicata  in  Sostituzione</td></tr>  
    <tr>
      <td class="l" colspan="1" width="160px">Tipo  &nbsp;</td>
      <td class="l" colspan="3">
        <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>" >
          <%=TipoPenaAccessoria_D%>
        </select>
      </td>
    </tr>
    
    <tr>  
      <td class="l" colspan="1">Tipo Durata &nbsp;</td>
      <td class="l" colspan="1">
        <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_DURATA_PA %>" >
		   <%=DurataPenaAcc_D%>
        </select>
      </td>

      <td class="l" colspan="1" style="text-align:center" >Durata</td>
      <td class="l" colspan="1" >
        Anni &nbsp;<input maxlength=2 size=2 Title="Anni Durata" value="<%=StringUtils.toStringJSP(ProvvGECum.getNumAnniPaD(), "" )%>" 
        	type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_PA %>">&nbsp;&nbsp;
        Mesi &nbsp;<input maxlength=2 size=2 Title="Mesi Durata" value="<%=StringUtils.toStringJSP(ProvvGECum.getNumMesiPaD(), "" )%>" 
        	type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_PA %>">&nbsp;&nbsp;
        Giorni &nbsp;<input maxlength=2  size=2 Title="Giorni Durata" value="<%=StringUtils.toStringJSP(ProvvGECum.getNumGiorniPaD(), "" )%>" 
        	type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_PA %>">&nbsp;&nbsp;
      </td>
    </tr>
    

    <tr>
      <td class="l" colspan="1" > Motivazioni </td>
      <td class="l" colspan="3" >
        <textarea cols="100" rows="3" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI_D %>"><%=StringUtils.toStringJSP(ProvvGECum.getMotivazioniD(),"")%></textarea>
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
     
</div>

</td>
</tr>
<!-- 					end dati inseribili / modificabili													-->
  
</form>

</body>

<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("LoadInsDecGESostPA");
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>

</html>



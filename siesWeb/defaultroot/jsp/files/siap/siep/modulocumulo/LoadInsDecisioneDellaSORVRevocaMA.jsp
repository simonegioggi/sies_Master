<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiesteInviateCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="IstruttoriaCumulo" 	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="modalita" 			   	scope="request" class="java.lang.String"/>
<jsp:useBean id="RichiestaSORV"    		scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="Titolo"     			scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="ProvvSORVCum"     		scope="request" class="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"/>
<jsp:useBean id="UfficioEmittente"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoProvvedimento"	   	scope="request" class="java.lang.String"/>


<!-- 							LoadInsDecisioneDellaSORVRevocaMA								 -->
<%
//==============================================================================
//  Form con le funzioni di Inserimento/Modifica della decisione della SORVEGLIANZA
//	a seguito di: Richiesta di Revoca M.A. - (cod = 031) 
//  (Gestione Cumulo)
//==============================================================================

RichiesteInviateCumModel lRicInv = null;
if(RichiestaSORV!=null && RichiestaSORV.getRichiesteInviateCum()!=null)
{
	lRicInv = (RichiesteInviateCumModel)RichiestaSORV.getRichiesteInviateCum();
	//LogF3B.getLogger().debug(" --XX-- Richiesta Inviata = "+lRicInv);
}

//Data Richiesta al GE
String lDataRich = StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaSORV.getDataEmissione(),"dd/MM/yyyy"));

// Conformità Decisione del GE
String lconforme="";
if("M".equals(modalita))
{
	if(ProvvSORVCum.getFlagConforme()!=null)
		lconforme = ProvvSORVCum.getFlagConforme();
}

%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo - Decisione della SORVEGLIANZA su Richieste di Revoca M.A.</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript">
  	
	//Apre la finestra con la Lista delle Sedi uffici in base alla tipologia di Ufficio Selezionata
  	function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
  	{
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  	}
	
    //==========================================================================
    // Ritorna alla Griglia Delle Richieste alla SORVEGLIANZA
    //==========================================================================
    function tornaIndietro(action)
    {
      document.LoadInsDecSORVRevoMA.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.LoadInsDecSORVRevoMA.submit();
    }
    
    function Verify() 
    { 
        // Tipo provvedimento SORV
		if(document.LoadInsDecSORVRevoMA.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO %>.value == '-')
		{
			alert('selezionare Tipo provvedimento della Sorveglianza');
			document.LoadInsDecSORVRevoMA.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO %>.focus();
			return false;
		}	

 	   // Data Emissione Provvedimento SIUS
       if ( document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value.length==1)
         	document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value='0'+document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value;
       if ( document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value.length==1)
         	document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value='0'+document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value;

       var data_to_verify = document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value+'/'+document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value+'/'+document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE_D%>.value;
       if (data_to_verify=='//' )
       {
           alert('Indicare la Data Emissione Provvedimento');
           document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.focus();
           return false;
       }
       
       if (!ControllaData(data_to_verify) )
       {
           alert('Data Emissione Provvedimento NON valida');
           document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.focus();
           return false;
       }
       
    	// Data Emissione Provvedimento SORV deve essere <= Data del Giorno
    	var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
		if(!CompareDate(data_to_verify, data_od))
		{
			alert('Data Emissione Provvedimento SIGE NON può essere superiore alla Data Odierna');
			document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D %>.focus();
      		return false;
		}
		
		// Data della Richiesta deve essere <= Data Emissione Procedimento SIGE 
	 	var data_Ric = '<%=lDataRich%>';
	 	if(!CompareDate(data_Ric, data_to_verify))
	 	{
			alert('Data Emissione Provvedimento SIUS DEVE essere superiore o uguale \nalla Data della Richiesta del PM');
			document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D %>.focus();
     		return false;
	 	}
       
	  // Anno e Numero Provvedimento SORV
      if(document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.value == '' && 
         document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.value != ''	 )
      {
      		alert('Anno Provvedimento NON valido');
          document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.focus();
          return false;
      } 
       
      if( document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.value != '' && 
      	  document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.value == ''	 )
      {
      	alert('Numero Provvedimento NON valido');
          document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.focus();
          return false;
      }
      
   	  // Anno e Numero Procedimento SIUS
      if(document.LoadInsDecSORVRevoMA.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.value == '' && 
         document.LoadInsDecSORVRevoMA.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.value != ''	 )
      {
      		alert('Anno Procedimento SIUS NON valido');
          document.LoadInsDecSORVRevoMA.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.focus();
          return false;
      } 
       
      if( document.LoadInsDecSORVRevoMA.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.value != '' && 
      	  document.LoadInsDecSORVRevoMA.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.value == ''	 )
      {
      	alert('Numero Procedimento SIUS NON valido');
          document.LoadInsDecSORVRevoMA.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.focus();
          return false;
      }
       
       // Tipo Ufficio e sede Ufficio Emittente
       if(document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.value == '-' || 
      	 document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.value.length == 1	 )
       {
      	 	alert('Ufficio Emittente non valido');
          	document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.focus();
          	return false;
       } 
       
       if(document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.value == '-' || 
      	 document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.value.length == 1 ||
      	 document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.value.length == 0 )
       {
      	 	alert('Sede Ufficio Emittente non valida');
          	document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
          	return false;
       }
       
    	// Data Decorrenza Revoca 
       if ( document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_REVOCA%>.value.length==1)
         	document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_REVOCA%>.value='0'+document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_REVOCA%>.value;
       if ( document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_REVOCA%>.value.length==1)
         	document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_REVOCA%>.value='0'+document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_REVOCA%>.value;

       var data_to_verify = document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_REVOCA%>.value+'/'+document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_REVOCA%>.value+'/'+document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_REVOCA%>.value;
       
    	// Data Decorrenza Revoca OBBLIGATORIA SOLO se Decisione della SORV. è CONFORME e/o DIFFORME
       if(document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME%>[0].checked == true ||
      	  document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME%>[1].checked == true )
       {
	       if (data_to_verify=='//' )
	       {
	           alert('Indicare la Data Decorrenza Revoca');
	           document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_REVOCA%>.focus();
	           return false;
	       }
	       
	       if (!ControllaData(data_to_verify) )
	       {
	           alert('Data Decorrenza Revoca NON valida');
	           document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_REVOCA%>.focus();
	           return false;
	       }
       }    
       else
       {	 
    		if (data_to_verify !='//' )
	        {
	       		if (!ControllaData(data_to_verify) )
	       		{
	           		alert('Data Decorrenza Revoca NON valida');
	           		document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_REVOCA%>.focus();
	           		return false;
	       		}
	        }	
       }	
       
        return true;

    }
    
  </script>

</head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
<%	if("I".equals(modalita) )
	{	%>
        	<font class="campo">Inserimento Decisione della SORVEGLIANZA - Revoca Misura Alternativa</font>
<% 	}	
	else if("M".equals(modalita) )
	{	%>
			<font class="campo">Modifica Decisione della SORVEGLIANZA - Revoca Misura Alternativa</font>		
<%	}	%>	        
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia delle Richieste alla SORV-->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV')">
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


<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="LoadInsDecSORVRevoMA">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciDecisioneDellaSORVCumulo">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="<%=RichiestaSORV.getIdRichiestePmInCumulo() %>" >
  <input type="hidden" name="modalita" value="<%=modalita%>" >

<%	// =======================================================
	//		Dati sui Titoli coinvolti nella Richiesta	
	// =======================================================
%>

  <table cellpadding="2" cellspacing="2" width="95%" align="center" style="border:0;">
     <tr><td class="Titolo" colspan="100%">In relazione al Titolo</td></tr>
<%      	
  String AnnoNumero ="";
  AnnoNumero = Titolo.getAnnoSentenza() +"/"+Titolo.getNumeroSentenza();
%> 
    <tr>
      <td class="l" colspan="8">
        <font class="label"><%=StringUtils.toStringJSP(Titolo.getDescrTipoProvvedimento() )%>&nbsp;N. &nbsp; </font>
        <font class="campo"><%=AnnoNumero%></font>&nbsp;
        &nbsp;<font class="label"> del </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Titolo.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
        &nbsp;<font class="label"> Emessa da </font>
	 	<font class="campo"><%=StringUtils.toStringJSP(Titolo.getDescrTipoAutoritaEmittente(), "") %></font>
        <font class="label">&nbsp;di&nbsp; </font>
        <font class="campo"><%=StringUtils.toStringJSP(Titolo.getDescrLuogoEmittente(), "") %></font>

        &nbsp;<font class="label"> Irrevocabile il  </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Titolo.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
      </td>
    </tr>
    <%	StatoEsecTitoloCumulatoModel lStatoMod = (StatoEsecTitoloCumulatoModel)Titolo.getStatoEsecTitoloCumulato();  %>
	  
	  <tr>
      	<td class="l" >
          <font class="label" style="color:red; text-align:left"> Misura Concessa: </font>
          &nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lStatoMod.getDescrMotivo(),"") %></font>&nbsp;
          &nbsp;<font class="label"> Concessa da </font>
          <font class="campo"><%=StringUtils.toStringJSP(lStatoMod.getDescrUfficioEmittente(), "") %></font>
          <font class="label">&nbsp;di&nbsp; </font>
      	  <font class="campo"><%=StringUtils.toStringJSP(lStatoMod.getDescrLuogoEmittente(), "") %></font>
      	  &nbsp;<font class="label">&nbsp;con&nbsp; </font>
      	  <font class="campo"><%=StringUtils.toStringJSP(lStatoMod.getDescrTipoProvvedimento(), "") %></font>
      	  &nbsp;<font class="label"> del </font>
      	  <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lStatoMod.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>&nbsp;
        </td>
      </tr>
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
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaSORV.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>
      </td>
    </tr>  	
    <tr>
      <td class="l" width="200px">Tipo Richiesta:</td>
      <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaSORV.getDescrTipoAnnotazione() )%></font>
      </td>
    </tr>
    <tr>
      <td class="l" width="250px" >Misura da Revocare </td>
      <td class="L" >
      	<font class="campo"><%=StringUtils.toStringJSP(lStatoMod.getDescrMotivo(),"") %></font>
      </td>
    </tr>

<%	if(RichiestaSORV.getMotivazioni()!=null && !RichiestaSORV.getMotivazioni().equals("") )
	{	%>
	<tr>
      <td class="l" colspan="1">Motivazioni :  </td>
      <td class="l" colspan="3">
      	<font class="campo"><%=StringUtils.toStringJSP(RichiestaSORV.getMotivazioni(),"" )%></font>
      </td>
    </tr>  		
<%	} %>
  </table>
  
 <%
  //=======================================================
  //  		Eventuale Richiesta Inviata
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
  //  Dati esclusivi della Decisione SORV da Inserire / Modificare
  //========================================================================
  %>  
<tr>
  <td colspan="100%" align="center">
  
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap">Dati Provvedimento della Sorveglianza</td></tr>      

    <tr>
	  <td class="l" width="15%" >Tipo <font class=ob>(*)</font></td>

	  <td class="l">
      	<select  name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO %>" Title="Tipo Provvedimento" >
		  <%=TipoProvvedimento%>
       	</select>
  	  </td>

      <td class="l" width="15%" >Data emissione <font class=ob>(*)</font></td>
      <td class="l">
         <input type="text"  title="Giorno di emissione documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D %>" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvSORVCum.getDataD(), "dd"), "") %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
           /
         <input type="text"  title="Mese di emissione documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D %>" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvSORVCum.getDataD(), "MM"), "") %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
         /
         <input type="text"  title="Anno di emissione documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE_D %>" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvSORVCum.getDataD(), "yyyy"), "") %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>
       
	<tr>      
      <td class="l">Anno / Numero Provvedimento</td>
      <td class="l">
         <input Title="Anno Provvedimento"   value="<%=StringUtils.toStringJSP(ProvvSORVCum.getAnnoProvv(), "") %>"
         	name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>" type="text" size="4" maxlength="4"   
         	onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
         <input Title="Numero Provvedimento" value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumeroProvv(), "") %>" 
         	name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>" type="text" size="6" maxlength="6" 
         	onkeypress="return TicTabNumField(this,event)">
      </td>
      
      <td class="l">Anno / Numero SIUS</td>
      <td class="l">
        <input Title="Anno Fascicolo Sius" value="<%=StringUtils.toStringJSP(ProvvSORVCum.getAnnoSIUS(), "") %>" 
        name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>" type="text" size="4" maxlength="4"  <%=IWebConstants.UTIL_DATA_ANNO%>>
        /
        <input Title="Numero Sius" value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumeroSIUS(), "") %>" 
        name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>" type="text" size="6" maxlength="6">
      </td>
    </tr>    

    <tr>
      <td class="l">Ufficio <font class="ob">(*)</font></td>
      <td class="l">
        <select Title="Autorità Emittente" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>">
          <%=UfficioEmittente%>
        </select>
      </td>
      <td class="l" width="15%" >Sede <font class="ob">(*)</font> &nbsp; </td>
      <td class="l" >
        <input title="Sede Autorita"  type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>"  
        	value="<%=StringUtils.toStringJSP(ProvvSORVCum.getDescrLuogoEmittente(), "") %>" maxlength="30" size="30">
        <a href="Javascript:ListaUfficiComuni('LoadInsDecSORVRevoMA','<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>',
        			document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>[document.LoadInsDecSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border="0">
        </a>
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
      
      	if("C".equals(ProvvSORVCum.getFlagConforme()) ){
        	checkC = "checked";
      	}
      	else if("D".equals(ProvvSORVCum.getFlagConforme()) ) { 
        	checkD = "checked";
      	}
      	else if("I".equals(ProvvSORVCum.getFlagConforme()) ) { 
        	checkI = "checked";
      	}
      	else if("R".equals(ProvvSORVCum.getFlagConforme()) ) { 
        	checkR = "checked";
      	}
      	else { checkC = "checked"; }
%>  
		  <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="C" <%=checkC%> >in conformita' alla richiesta del PM &nbsp;&nbsp;	<!-- Conforme -->
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="D" <%=checkD%> >in difformita' alla richiesta del PM &nbsp;&nbsp;	<!-- Difforme -->
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="R" <%=checkR%> >rigetta &nbsp;&nbsp;	<!-- Rigetta -->
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="I" <%=checkI%> >dichiara inammissibile &nbsp;&nbsp;	<!-- Inammissibile -->	
	        
        </td>
      </tr>
    </table>
    
    <table width="95%" align="center">
      <tr>
    	<td class="l" width="25%" >Data Decorrenza Revoca <font class=ob>(*)</font></td>
      	<td class="l">
          <input type="text"  title="Giorno data Revoca" name="<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_REVOCA %>" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvSORVCum.getDataRevoca(), "dd"), "") %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
           /
           <input type="text"  title="Mese data Revoca" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_REVOCA %>" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvSORVCum.getDataRevoca(), "MM"), "") %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
           /
           <input type="text"  title="Anno data Revoca" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_REVOCA %>" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvSORVCum.getDataRevoca(), "yyyy"), "") %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
      	</td>
      </tr>
     
      <tr>
     	<td class="l" width="25%" >Pena Rideterminata (AA-MM-GG)&nbsp;&nbsp;</td>
     	<td class="l">
     	  <font class="label">Reclusione: </font>&nbsp;
	      <input type="text" maxlength="2" size="2"
	               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_D%>" 
	               value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumAnniReclusioneD(),"")%>"
	               onkeypress="return TicTabNumField(this,event)">&nbsp;
	      -         
	      <input type="text" maxlength="2" size="2"
	               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_D %>"  
	               value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumMesiReclusioneD(),"") %>"
	               onkeypress="return TicTabNumField(this,event)">&nbsp;
	      -         
	      <input type="text" maxlength="4" size="4"
	               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_D %>"                 
	               value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumGiorniReclusioneD(),"") %>"
	               onkeypress="return TicTabNumField(this,event)">
	               
	    </td>
	    <td class="l">
	    
     	  <font class="label">Arresto: </font>&nbsp;
	      <input type="text" maxlength="2" size="2"
	               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_D%>" 
	               value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumAnniArrestoD(),"")%>"
	               onkeypress="return TicTabNumField(this,event)">&nbsp;
	      -         
	      <input type="text" maxlength="2" size="2"
	               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_D %>"  
	               value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumMesiArrestoD(),"") %>"
	               onkeypress="return TicTabNumField(this,event)">&nbsp;
	      -         
	      <input type="text" maxlength="4" size="4"
	               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_D %>"                 
	               value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumGiorniArrestoD(),"") %>"
	               onkeypress="return TicTabNumField(this,event)">
	    </td>
	  </tr>
	</table>      	
     
  <table width="95%" align="center">
    <tr>
      <td class="l" colspan="1" > Motivazioni </td>
      <td class="l" colspan="3" >
        <textarea cols="100" rows="3" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI_D %>"><%=StringUtils.toStringJSP(ProvvSORVCum.getMotivazioniD(),"")%></textarea>
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

</td>
</tr>
<!-- 					end dati inseribili / modificabili													-->
</table>  
</form>

</body>

<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("LoadInsDecSORVRevoMA");
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>

</html>



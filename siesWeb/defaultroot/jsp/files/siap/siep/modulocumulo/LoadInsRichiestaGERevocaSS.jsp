<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiSanzioneSostitutivaCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel" %>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="VectorTitoli"          scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="RichiestaAlGE"			scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
  
<!-- 						LoadInsRichiestaGERevocaSS							 -->  
<%
    //============================================================================== 
    // Form per l'inserimento e la modifica delle richieste al GE di Revoca
    // Sanzioni Sostitutive
    //==============================================================================
    %>

<html>
<head>
  <title> Gestione Cumulo - Richieste Revoca Sanzione Sostitutiva</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" >


    function tornaIndietro(action)
    {
      document.indietroForm.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.indietroForm.submit();
    }
   
    
    function Verify() 
    { 
   	 	// Solo 1 Titolo e 1 SanSost presenti nella form
   	 	if (typeof (document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO%>[0]) =="undefined" )
     	{
   	 		if (typeof (document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_SANZIONE_SOST_SELEZIONATA%>[0]) =="undefined" )
   	 		{
   	 			// dati OK	
   	 		}
   	 		else
   	 		{
   	 			alert("Errore - I dati presenti nella form non sono Corretti ");
   	 			document.RicRevSS.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE";
   	 	      	document.RicRevSS.submit();
   	 		}	
     	}
   	 	else	// n Titoli e SalSost presenti
   	 	{
   	 		var idTlen = document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO%>.length;
   	 		var idSlen = document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_SANZIONE_SOST_SELEZIONATA%>.length;
   	 		
   	 		if( idTlen != idSlen)
   	 		{
   	 			alert("Errore - I dati nella form non sono Corretti ");
   	 			document.RicRevSS.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE";
   	 	      	document.RicRevSS.submit();
   	 		}	
   	 	}	
   	 	
   		// Data Emissione
         if (document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
           	 document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
         if (document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
             document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value;

         var data_to_verify = document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE%>.value;
         if (data_to_verify=='//' )
         {
             alert('Indicare la Data Richiesta');
             document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
             return false;
         }
         
         if (!ControllaData(data_to_verify) )
         {
             alert('Data Richiesta non valida');
             document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
             return false;
         } 
         
         // Data Emissione deve essere <= Data del Giorno
         var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
   		if(!CompareDate(data_to_verify, data_od))
   		{
   			alert('Data Richiesta NON può essere superiore alla Data Odierna');
   			document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           	return false;
   		}

        var segnoPresente = true;
        var quantumPresenti = true;

        if(document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_R%>.value == ''  )
          segnoPresente = false;

        if ( ( document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>.value == '' ||
               document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>.value.length == 0 )  &&
             ( document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_R%>.value == '' ||
               document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_R%>.value.length == 0  ) &&  
             ( document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_R%>.value == '' ||
               document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_R%>.value.length == 0 ) &&
               
             ( document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_R%>.value == '' ||
               document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_R%>.value.length == 0 ) &&
             ( document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_R%>.value == '' ||
               document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_R%>.value.length == 0 ) &&
             ( document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_R%>.value == '' ||
               document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_R%>.value.length == 0 ) &&  
               
             ( document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT.value.length == 0 ) &&
             ( document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>DEC.value.length == 0 ) &&  
             ( document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT.value.length == 0 ) &&
             ( document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>DEC.value.length == 0 ) 
           )  
       {
          quantumPresenti = false;
       }

      if (segnoPresente && !quantumPresenti) {
        alert('Se si indica il segno (+ / -), vanno specificati anche i quantum di pena');
        document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>.focus();
        return false;
      }
      else if (!segnoPresente && quantumPresenti) {
        alert('Se si indicano i quantum di pena va indicato anche il segno');
        document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_R%>.focus();
        return false;
      }


      // Quantum
      var pos = document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT.value.indexOf('.');
      if(pos >= 0)
      {
        alert('inserire correttamente il valore INTERO della Multa');
        document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT.focus();
        return false;
      }
              
      pos = document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT.value.indexOf('.');
      if(pos >= 0)
      {
        alert('inserire correttamente il valore INTERO della Ammenda');
        document.RicRevSS.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT.focus();
        return false;
      }
      
      return true; 
    } 

  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;

        <%
          if (modalita.equals("I") ) {
        %>
        <font class="campo">Inserimento Richiesta Revoca di Sanzione Sostitutiva &nbsp;</font>
        <%
          } else if( modalita.equals("M") ) {
        %>
        <font class="campo">Modifica Richiesta Revoca di Sanzione Sostitutiva &nbsp;</font>
        <%
          }
        %>
      </td>
      <td class="LBG">
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE')">
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
 
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="indietroForm">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
</form> 
 
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="RicRevSS">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRichiestaGERevocaSSCum">
  
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA %>" value="01" >
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>" value="023" >
  
  <%
  if("M".equals(modalita) )
  {	%>
  	<input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="<%=RichiestaAlGE.getIdRichiestePmInCumulo()%>">
<%} %>  
  
  <%
  //============================================================================
  // Sezione con l'elenco delle SS per titolo
  //============================================================================
  %>
  <table width="95%" align="center">
    <tr><td colspan="1" class="Titolonocap"><font class="campo">Elenco delle SANZIONI SOSTITUTIVE da Revocare</font></td></tr>
   	<table width="95%" align="center">
   	
<% 	int conta=0;
	String AnnoNumero = "";
	Iterator itx = VectorTitoli.iterator();
	while(itx.hasNext())
	{	
	  TitoloCumulatoModel lTitolo = (TitoloCumulatoModel)itx.next();
	  AnnoNumero = lTitolo.getAnnoSentenza() +"/"+lTitolo.getNumeroSentenza();	
	  conta = conta + 1;	%>
	  <tr><td colspan="1" class="Titolonocap" >in relazione al Titolo Esecutivo</td></tr>
  	  <tr>	
    	<input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO%>" value="<%=StringUtils.toStringJSP(lTitolo.getIdTitoloCumulato()) %>" >
		<td class="l" >
	  	<font class="label"><%=StringUtils.toStringJSP(lTitolo.getDescrTipoProvvedimento() )%>&nbsp;N. </font>
      	<font class="campo"><%=AnnoNumero%></font>&nbsp;
      	&nbsp;<font class="label"> del </font>
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitolo.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;	
         
      	&nbsp;<font class="label"> Emessa da </font>
      	<font class="campo"><%=StringUtils.toStringJSP(lTitolo.getDescrTipoAutoritaEmittente(), "") %></font>
      	<font class="label">&nbsp;di&nbsp; </font>
      	<font class="campo"><%=StringUtils.toStringJSP(lTitolo.getDescrLuogoEmittente(), "") %></font>
          
      	&nbsp;<font class="label"> Irrevocabile il  </font>
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitolo.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
    	</td>
  	</tr>
  	    
<% 	  if(lTitolo.getSanzioneSostitutivaCumulo()!=null && lTitolo.getSanzioneSostitutivaCumulo().getIdSanzioneSostitutivaCum()!=null)
	  {	
		  SanzioneSostitutivaCumuloModel lSSCum = (SanzioneSostitutivaCumuloModel) lTitolo.getSanzioneSostitutivaCumulo();		  
%>  
    <tr>
      <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_SANZIONE_SOST_SELEZIONATA %>" value="<%=StringUtils.toStringJSP(lSSCum.getIdSanzioneSostitutivaCum()) %>" >
      <td class="l" >
         <font class="label" style="color:red; text-align:left"> Sanzione Sostitutiva: </font>
         &nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lSSCum.getDescrTipoSanzione(),"") %></font>&nbsp;
<%		if(!"P".equals(lSSCum.getCodTipoSanzione()) )
		{ 	%>         
         	<font class="label"> per la durata di </font>&nbsp;&nbsp;
          	Anni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lSSCum.getNumAnni(), "-") %></font>&nbsp;&nbsp;
          	Mesi&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lSSCum.getNumMesi(), "-") %></font>&nbsp;&nbsp;
          	Giorni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lSSCum.getNumGiorni(), "-") %></font>&nbsp;&nbsp;
<%		}
		else
		{	
			if(lSSCum.getSanzionePecuniariaMulta() !=null && lSSCum.getSanzionePecuniariaMulta().compareTo(BigDecimal.ZERO ) > 0 )
         	{	%>
				&nbsp;<font class="label"> Multa </font>&nbsp;
				<font class="campoLow"><%=StringUtils.toEuroFormat(lSSCum.getSanzionePecuniariaMulta()) %></font>&nbsp;&euro;&nbsp;
<%			}
			
			if(lSSCum.getSanzionePecuniariaAmmenda() != null && lSSCum.getSanzionePecuniariaAmmenda().compareTo(BigDecimal.ZERO) > 0 )
			{	%>
				&nbsp;<font class="label"> Ammenda </font>&nbsp;
				<font class="campoLow"><%=StringUtils.toEuroFormat(lSSCum.getSanzionePecuniariaAmmenda()) %></font>&nbsp;&euro;&nbsp;
<%			}
		}	%>	          
      </td>
    </tr>
<%	  } 
	} %>  
  </table>
</table>
  <%
  //============================================================================
  // Sezione Data Richiestae e Motivazioni
  //============================================================================
  %>
  <br>  
  <table width="95%" align="center">
    <tr>
      <td class="l" width="15%" >Data Richiesta </td>
<%	if("I".equals(modalita))
	{	%>      
      <td class="L" >
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
<%	}
	else if("M".equals(modalita))
	{	%>
	  <td class="L" >
      	<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaAlGE.getDataEmissione(), "dd"), "") %>"   type="text" size="2" maxlength="2" 
      		name="<%= ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE %>" 
      		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaAlGE.getDataEmissione(), "MM"), "") %>"   type="text" size="2" maxlength="2" 
        	name="<%= ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaAlGE.getDataEmissione(), "yyyy"), "") %>" type="text" size="4" maxlength="4" 
        	name="<%= ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>	
<%	} %>	      
    </tr>
    <tr>
      <td class="l" width="15%" >Motivazioni </td>  	
      <td class="c" style="text-align:left">
        <textarea cols="100" rows="3" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI%>"><%=StringUtils.toStringJSP(RichiestaAlGE.getMotivazioni(),"") %></textarea>
      </td>
    </tr>
  </table>
    
  <%
  //============================================================================
  // Sezione con i quantum: duranta sanzione e importo Multa / Ammenda
  //============================================================================
  %>
<div id="divQuantum"  style="display:block" >
  <table width="95%" align="center" >
      <tr>
        <td class="Titolonocap" colspan="3" >Sanzione Sostitutiva da Revocare nella misura di</td>
      </tr>
  </table> 
  
  <table width="95%" align="center" >
    <tr>
      <td colspan=6>
        <hr width="100%">
      </td>
    </tr>
    <tr>
      <td valign="middle" class="c" rowspan=2>+/- <font class="ob">(*)</font><br>
<%
String piuSelected="";
String menoSelected="";
if("M".equals(modalita)){
  if( "+".equals(RichiestaAlGE.getFlagPiuMenoR()) ) 
    piuSelected="selected";
  if( "-".equals(RichiestaAlGE.getFlagPiuMenoR()) ) 
    menoSelected="selected";
}
%>    
     <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_R %>">
  	  <option value=""></option>
      <option value="+" <%=piuSelected%>>+</option>
      <option value="-" <%=menoSelected%>>-</option>
          </select>
        </td>
        <td class="titolo" colspan=2>Reclusione</td>
        <td width="25">&nbsp;</td>
        <td class="titolo" colspan=2>Arresto</td>
      </tr>
      <tr>
        <td class="c">
          <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
          <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
          <font class="label">Giorni</font><br>
          <input type="text" maxlength="2" size="2"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>" 
               value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumAnniReclusioneR())%>"
               onkeypress="return TicTabNumField(this,event)">&nbsp;
          <input type="text" maxlength="2" size="2"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_R %>"  
               value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumMesiReclusioneR()) %>"
               onkeypress="return TicTabNumField(this,event)">&nbsp;
          <input type="text" maxlength="4" size="4"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_R %>"                 
               value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumGiorniReclusioneR()) %>"
               onkeypress="return TicTabNumField(this,event)">
        </td>
        <td class="c">
           <font  class="label">Multa</font><br>
           <input type="text" maxlength="7" size="7" style="align:right"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT"
               value="<%=StringUtils.getParteIntera(RichiestaAlGE.getImportoMultaR()) %>"
               onkeypress="return TicTabNumField(this,event)" >&nbsp;
           ,
           <input type="text" maxlength="2" size="2"  style="align:right"
               name="<%= ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R %>DEC"
               value="<%=StringUtils.getParteDecimale(RichiestaAlGE.getImportoMultaR()) %>"
               onkeypress="return TicTabNumField(this,event)">&nbsp;Euro
        </td>
        
        <td width="25">&nbsp;</td>
        <td class=c>
          <font  class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
          <font  class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
          <font  class="label">Giorni</font><br>
          <input type="text" maxlength="2" size="2"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_R %>"  
               value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumAnniArrestoR()) %>"
               onkeypress="return TicTabNumField(this,event)" >&nbsp;
          <input type="text" maxlength="2" size="2" 
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_R %>"  
               value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumMesiArrestoR()) %>"
               onkeypress="return TicTabNumField(this,event)" >&nbsp;
          <input type="text" maxlength="4" size="4"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_R %>"          
               value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumGiorniArrestoR()) %>"
               onkeypress="return TicTabNumField(this,event)">
        </td>
        <td class="c">
          <font  class="label">Ammenda</font><br>
        <input type="text" maxlength="7" size="7" style="align:right"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT"         
               value="<%=StringUtils.getParteIntera(RichiestaAlGE.getImportoAmmendaR()) %>"               
               onkeypress="return TicTabNumField(this,event)">&nbsp;
        ,
        <input type="text" maxlength="2" size="2" style="align:right" 
               name="<%= ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R %>DEC"  
               value="<%=StringUtils.getParteDecimale(RichiestaAlGE.getImportoAmmendaR()) %>"
               onkeypress="return TicTabNumField(this,event)">&nbsp;Euro
        </td>
      </tr>
  </table>
  
  <table width="95%" align="center">
    <tr>
      <td class="l">
        Anticipazione degli effetti&nbsp;&nbsp;
      
        <input type="checkbox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_APP_PROVVISORIA %>" value="A" 
        	<%="A".equals(RichiestaAlGE.getFlagAppProvvisoria())?"checked":""%> > 
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

</FORM>

</body>
</html>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("RicRevSS");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator "%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaAccessoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"%>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloListaPA"    		scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="RichiestaAlGE"			scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="TipoPenaAccessoria" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="DurataPenaAcc"		 	scope="request" class="java.lang.String"/>

<jsp:useBean id="TipoAnnotazioneManuale" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="listaDPR"               	scope="request" class="java.lang.String"/> 

<!-- 			LoadInsRichiestaGESostPA				 -->  
<%
   //============================================================================== 
   // Form per l'inserimento e la modifica delle richieste al GE di 
   //	Sostituzione di Pena Accessoria
   //============================================================================== 
  
   String CodRic = "";
   if("M".equals(modalita))
   {
   		CodRic = RichiestaAlGE.getCodTipoAnnotazione();
   }
 %>

<html>
<head>
  <title> Gestione Richieste Sostituzione Pena Accessoria</title>
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
    
    function mettiCondono()
    {
      //alert('mettiCondono');
      if(document.LoadinsRicGESPA.TipoRichiesta.value == "025")
      {
        $('#tabrichCondon').show();
        $('#<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_INDULTO %>').prop('disabled',false);
        $('#<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>').prop('disabled',false);        
      }
      else
      {
        $('#tabrichCondon').hide();
        $('#<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_INDULTO %>').prop('disabled',true);
        $('#<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>').prop('disabled',true);
      }
     
      document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>.value = document.LoadinsRicGESPA.TipoRichiesta.value;
    }

    function carica()
    {
      var modo = '<%=modalita%>';
      var lcod = '<%=CodRic%>';
      //alert('carica - modo = '+modo+' - lCodRic = '+lcod);
      
      if(modo == 'M')
      {
        document.LoadinsRicGESPA.TipoRichiesta.value = lcod;
        if(document.LoadinsRicGESPA.TipoRichiesta.value == "025")
        {
          $('#tabrichCondon').show();
          $('#<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_INDULTO %>').prop('disabled',false);
          $('#<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>').prop('disabled',false);        
        }
        else
        {
          $('#tabrichCondon').hide();
          $('#<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_INDULTO %>').prop('disabled',true);
          $('#<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>').prop('disabled',true);
        }
        
        document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>.value = lcod;
      } 
    }
    
    function Verify() 
    { 
 	   // Data Emissione
       if (document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
         	 document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
       if (document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
           document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value;

       var data_to_verify = document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE%>.value;
       if (data_to_verify=='//' )
       {
           alert('Indicare la Data Richiesta');
           document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       }
       
       if (!ControllaData(data_to_verify) )
       {
           alert('Data Richiesta non valida');
           document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       } 
       
       // Data Emissione deve essere <= Data del Giorno
       var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
 	   if(!CompareDate(data_to_verify, data_od))
 	   {
 			alert('Data Richiesta NON può essere superiore alla Data Odierna');
 			document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
         	return false;
 	   }
 	   
 	   // Tipo Richiesta
 	   if(document.LoadinsRicGESPA.TipoRichiesta.value == "-" && 
 	   	  document.LoadinsRicGESPA.TipoRichiesta.value.length < 2	)
   	   {
 	   		alert('Selezionare Tipo Richiesta');
 	   		document.LoadinsRicGESPA.TipoRichiesta.focus();
 	   		return false;
   	   }
 	   
 		// Tipo Pena Accessoria
 	   if(document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>.value == "-" && 
 	   	  document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>.value.length < 2	)
   	   {
 	   		alert('Selezionare Tipo Pena Accessoria in Sostituzione');
 	   		document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>.focus();
 	   		return false;
   	   }
 	   
 		// DPR (solo se scelta = CONDONO)
 		if(document.LoadinsRicGESPA.TipoRichiesta.value == "025")
   	 	{
	 		if(document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>.value == "-" && 
	  	   	   document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>.value.length < 2	)
	    	{
	  	   		alert('Selezionare Estremi Beneficio DPR');
	  	   		document.LoadinsRicGESPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>.focus();
	  	   		return false;
	    	}
   	 	}
 		
       return true; 
    } 

  </script>
</head>

<body class="corpo" onLoad="javascript:carica();">
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
        <font class="campo">Inserimento Richiesta Sostituzione di Pena Accessoria &nbsp;</font>
        <%
          } else if( modalita.equals("M") ) {
        %>
        <font class="campo">Modifica Richiesta Sostituzione di Pena Accessoria &nbsp;</font>
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
 
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadinsRicGESPA">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRichiestaGESostPenaAcc">
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA %>" value="01" >
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>" value="" >
<%
   if("M".equals(modalita) )
   {	%>
   		<input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="<%=RichiestaAlGE.getIdRichiestePmInCumulo() %>" >
<% } %>     

  
  <%
  //============================================================================
  // Sezione con l'elenco delle PA per titolo
  //============================================================================
  %>
  <table width="95%" align="center">
    <tr><td colspan="1" class="Titolonocap"><font class="campo">Elenco delle PENE ACCESSORIE da Sostituire</font></td></tr>
   	<table width="95%" align="center">
   	
<% 	int conta=0;
	String AnnoNumero = "";
	AnnoNumero = TitoloListaPA.getAnnoSentenza() +"/"+TitoloListaPA.getNumeroSentenza();	
	conta = conta + 1;	%>
	<tr><td colspan="1" class="Titolonocap" >in relazione al Titolo Esecutivo</td></tr>
  	<tr>	
      <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO%>" value="<%=StringUtils.toStringJSP(TitoloListaPA.getIdTitoloCumulato()) %>" >
		<td class="l" >
	  	<font class="label"><%=StringUtils.toStringJSP(TitoloListaPA.getDescrTipoProvvedimento() )%>&nbsp;N. </font>
      	<font class="campo"><%=AnnoNumero%></font>&nbsp;
      	&nbsp;<font class="label"> del </font>
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(TitoloListaPA.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;	
         
      	&nbsp;<font class="label"> Emessa da </font>
      	<font class="campo"><%=StringUtils.toStringJSP(TitoloListaPA.getDescrTipoAutoritaEmittente(), "") %></font>
      	<font class="label">&nbsp;di&nbsp; </font>
      	<font class="campo"><%=StringUtils.toStringJSP(TitoloListaPA.getDescrLuogoEmittente(), "") %></font>
          
      	&nbsp;<font class="label"> Irrevocabile il  </font>
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(TitoloListaPA.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
    	</td>
  	</tr>
  	    
<% 	if(TitoloListaPA.getPeneAccessorieCumulo()!=null && TitoloListaPA.getPeneAccessorieCumulo().size() > 0 )
	{	
	  Iterator itx = TitoloListaPA.getPeneAccessorieCumulo().iterator();
	  while(itx.hasNext())
	  {	  
		 PenaAccessoriaCumuloModel lPACum = (PenaAccessoriaCumuloModel) itx.next();		  
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
          	Anni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPACum.getNumAnni(), "-") %></font>&nbsp;&nbsp;
         <% }
        	
        	if(lPACum.getNumMesi()!=null && lPACum.getNumMesi().compareTo(BigDecimal.ZERO) > 0)
	   	  	{	%> 	
          	Mesi&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPACum.getNumMesi(), "-") %></font>&nbsp;&nbsp;
       <%	}
        	
        	if(lPACum.getNumGiorni()!=null && lPACum.getNumGiorni().compareTo(BigDecimal.ZERO) > 0)
	   	  	{	%>  	
          	Giorni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPACum.getNumGiorni(), "-") %></font>&nbsp;&nbsp;
        <%	} %>  	
<%		}
		else if(lPACum.getDurata()!= null && "P".equals(lPACum.getDurata()) )
   		{	%>
				<font class="label"> Durata: </font>&nbsp;
				<font class="campoLow"><%=StringUtils.toStringJSP(lPACum.getDescrDurata() )%></font>&nbsp;
<%		}	%>
	          
      </td>
    </tr>
<%	  } 
	} %>  
  </table>
</table>
  
  <br>
  
  <table width="95%" align="center">
    <tr><td class="Titolo" colspan="4">Richiesta al Giudice dell' Esecuzione</td></tr>
    
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
      <td class="l" colspan="1" width="160px">Tipo richiesta &nbsp;</td>
      <td class="l" colspan="3">
        <select name="TipoRichiesta" onChange="javascript:mettiCondono();" >
     <%	if("I".equals(modalita)) 
     	{	%>   
          <option value = "-"  />-
          <option value = "024"  />Sostituzione Pena Accessoria
          <option value = "025"  />Sostituzione e condono Pena Accessoria
    <%	}
     	else
     	{	
     		if("024".equals(RichiestaAlGE.getCodTipoAnnotazione()) ) 
     		{	%>
     		<option value = "-"  />-
          	<option value = "024"  selected/>Sostituzione Pena Accessoria
          	<option value = "025"  />Sostituzione e condono Pena Accessoria
    <%		}
     		else if("025".equals(RichiestaAlGE.getCodTipoAnnotazione()) )
     		{	%>      	
     		<option value = "-"  />-
          	<option value = "024"  />Sostituzione Pena Accessoria
          	<option value = "025"  selected/>Sostituzione e condono Pena Accessoria
   <%		}
     	}	%>	    
        </select>
      </td>
    </tr>
  </table>
    

  <table id="tabrichCondon" width="95%" align="center"  style="display:none">
    <tr>
      <td class="l" width="20%" colspan="1">Estremi Beneficio <font class="ob">(*)</font> :&nbsp;</td>
      <td class="l" colspan="1">
        <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_INDULTO %>"
                id="<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_INDULTO %>">
          <%=TipoAnnotazioneManuale%>
        </select>
      </td>
      <td class="l" colspan="2">
        <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>"
                id="<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>">
          <%=listaDPR%>
        </select>
      </td>
    </tr>  
  </table>
  
  
  <br>
  <table id="tabSost" width="95%" align="center" >
    <tr><td class="Titolo" colspan="4" style="text-align:left" >Pena Accessoria in Sostituzione</td></tr>  
    <tr>
      <td class="l" colspan="1" width="160px">Tipo di Pena Accessoria: &nbsp;</td>
      <td class="l" colspan="3">
        <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>" >
          <%=TipoPenaAccessoria%>
        </select>
      </td>
    </tr>
    
    <tr>  
      <td class="l" colspan="1">Tipo Durata &nbsp;</td>
      <td class="l" colspan="1">
        <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_DURATA_PA %>" >
		   <%=DurataPenaAcc%>
        </select>
      </td>

      <td class="l" colspan="1" style="text-align:center" >Durata</td>
      <td class="l" colspan="1" >
        Anni &nbsp;<input type="text" maxlength="2" size="2" Title="Anni Durata" 
                          value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumAnniPa(), "" )%>" 
                          onkeypress="return TicTabNumField(this,event)"
                          name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_PA %>">&nbsp;&nbsp;
        Mesi &nbsp;<input type="text" maxlength="2" size="2" Title="Mesi Durata" 
                          value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumMesiPa(), "" )%>" 
                          onkeypress="return TicTabNumField(this,event)"
                          name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_PA %>">&nbsp;&nbsp;
        Giorni &nbsp;<input type="text" maxlength="2"  size="2" Title="Giorni Durata" 
                            value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumGiorniPa(), "" )%>" 
                            onkeypress="return TicTabNumField(this,event)"
                            name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_PA %>">&nbsp;&nbsp;
      </td>
    </tr>    
  </table> 
  
  <%
  //============================================================================
  // 
  //============================================================================
  %>
  <br>  
  <table width="95%" align="center">
    <tr>
      <td class="l" colspan="1" > Motivazioni </td>
      <td class="l" colspan="3" >
        <textarea cols="100" rows="3" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI %>"><%=StringUtils.toStringJSP(RichiestaAlGE.getMotivazioni(),"")%></textarea>
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
  var frmvalidator  = new Validator("LoadinsRicGESPA");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
 
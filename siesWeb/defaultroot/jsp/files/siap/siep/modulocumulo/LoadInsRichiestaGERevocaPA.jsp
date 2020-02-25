<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator "%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaAccessoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="ListaTitoliPA"         scope="request" class="java.util.Vector"/>

<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoAnnotazione" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoAnnotazioneIndu" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="listaDPR"              scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiFontiReato"       	scope="request" class="java.lang.String"/> 
<jsp:useBean id="TipiSottonumerazione" 	scope="request" class="java.lang.String"/>

<jsp:useBean id="RichiestaAlGE"			scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/> 
  
<!-- 				LoadInsRichiestaGERevocaPA 				-->  
<%
   //============================================================================== 
   // Form per l'inserimento delle richieste al GE di Revoca di Pena Accessoria
   //============================================================================== 

%>

<html>
<head>
  <title> Gestione Cumulo - Richieste al GE - Revoca Pena Accessoria</title>
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
    
    function selTipoRevoca(obj) 
    {
      if (obj.value=="-" || obj.value=="026") {
        $('#divDepen').hide();
        $('#divDepen input[type=text]').prop('disabled',true);
        $('#divDepen select').prop('disabled',true); 

        $('#divIndulto').hide();
        $('#divIndulto select').prop('disabled',true); 
      }
      else if (obj.value=="027") {
        $('#divDepen').show();
        $('#divDepen input[type=text]').prop('disabled',false);
        $('#divDepen select').prop('disabled',false); 
        
        $('#divIndulto').hide();
        $('#divIndulto select').prop('disabled',true); 
      }
      else if (obj.value=="028") { 
        $('#divDepen').hide();
        $('#divDepen input[type=text]').prop('disabled',true);
        $('#divDepen select').prop('disabled',true); 
        
        $('#divIndulto').show();
        $('#divIndulto select').prop('disabled',false);
      }
    }     


    function Verify() 
    { 
   		// Data Emissione
       if (document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
         	 document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
       if (document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
           	document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value;

       var data_to_verify = document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE%>.value;
       if (data_to_verify=='//' )
       {
           alert('Indicare la Data Richiesta');
           document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       }
       
       if (!ControllaData(data_to_verify) )
       {
           alert('Data Richiesta non valida');
           document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       } 
       
       // Data Emissione deve essere <= Data del Giorno
       var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
 	   if(!CompareDate(data_to_verify, data_od))
 	   {
 			alert('Data Richiesta NON può essere superiore alla Data Odierna');
 			document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
         	return false;
 	   }
 	   
 	   // Tipo Richiesta
 	   if(document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>.value == "-" && 
 	   	  document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>.value.length < 2	)
   	   {
 	   		alert('Selezionare Tipo Richiesta');
 	   		document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>.focus();
 	   		return false;
   	   }
 	   
 		// Controllo valori per Depenalizzazione (solo se Tipo_Richiesta = DEPENALIZZAZIONE PENA ACCESSORIA)
      	if( document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>.value == "027" ) 
 	  	{
      		if( document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_FONTE%>.value == "-" && 
      			(document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_FONTE%>.value == "" ||
      			 document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_FONTE%>.value.length == 0 ) &&
      			(document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_FONTE%>.value == "" ||
            	 document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_FONTE%>.value.length == 0 ) && 
            	(document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ARTICOLO%>.value == "" ||
            	 document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ARTICOLO%>.value.length == 0 ) 
              )	 
      		{
      			alert('digitare correttamente la norma Depenalizzante');
            	document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_FONTE%>.focus();
            	return false;	
      		}			
      	
 	  	}
 		
      	// DPR (solo se Tipo_Richiesta = CONDONO PENA ACCESSORIA)
    	if(document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>.value == "028")
      	{
   	 		if(document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>.value == "-" && 
   	  	   	   document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>.value.length < 2	)
   	    	{
   	  	   		alert('Selezionare Estremi Beneficio DPR');
   	  	   		document.RicGERevPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>.focus();
   	  	   		return false;
   	    	}
      	}
      
      	return true; 
    } 

    $(document).ready(function(){
        $('#divDepen input[type=text]').prop('disabled',true);
        $('#divDepen select').prop('disabled',true); 

        $('#divIndulto select').prop('disabled',true);     
    });

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
        <font class="campo">Inserimento Richiesta Revoca di Pena Accessoria &nbsp;</font>
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
 
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="RicGERevPA">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRichiestaGERevocaPenaAcc">
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA %>" value="01" >
  <%
  //============================================================================
  // Sezione con l'elenco delle PA per titolo
  //============================================================================
  %>
    <table width="95%" align="center">
    <tr><td colspan="1" class="Titolonocap"><font class="campo">Elenco delle PENE ACCESSORIE da Revocare</font></td></tr>
   	<table width="95%" align="center">
   	
<% 	int conta=0;
	String AnnoNumero = "";
	String Valscelto = "";
	
	Iterator itxT = ListaTitoliPA.iterator();
	while(itxT.hasNext())
	{	
		TitoloCumulatoModel TitoloListaPA = (TitoloCumulatoModel)itxT.next();
		AnnoNumero = TitoloListaPA.getAnnoSentenza() +"/"+TitoloListaPA.getNumeroSentenza();	
		conta = conta + 1;	%>
		<tr><td colspan="1" class="Titolonocap" >in relazione al Titolo Esecutivo</td></tr>
	  	<tr>	
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
			 Valscelto = StringUtils.toStringJSP(TitoloListaPA.getIdTitoloCumulato())+";"+StringUtils.toStringJSP(lPACum.getIdPenaAccessoriaCumulo()); 
	%>  
	    <tr>
	      <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_PA_SELEZIONATI %>"   value="<%=Valscelto %>" >
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
		} 
	}	%>  
  </table>
</table>
  
  <br>
  <table width="95%" align="center">
    <tr><td class="Titolo" colspan="4">Richiesta al Giudice dell' Esecuzione</td></tr>
    <tr>
      <td class="l" width="15%" >Data Richiesta </td>
      <td class="L" >
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>    
    <tr>
      <td class="l" >Tipo richiesta &nbsp;</td>
      <td class="l" >
        <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>" onChange="javascript:selTipoRevoca(this);">
			<%=TipoAnnotazione%>
        </select>
      </td>
    </tr>
  </table> 
  
  <%
  //============================================================================
  // Amnistia/Indulto
  //============================================================================
  %>
  <div id="divIndulto"  style="display:none" >
    <table id="tabIndulto" width="95%" align="center" >
      <tr>
        <td colspan="3" class="Titolonocap">Estremi del condono</td>
      </tr>
      <tr>
        <td class="l" width="15%" colspan="1">Estremi Beneficio <font class="ob">(*)</font> :&nbsp;</td>
        <td class="l" colspan="1" >
          <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_INDULTO %>">
            <%=TipoAnnotazioneIndu%>
          </select>
        </td>
        <td class="l" colspan="2">
        <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_CONDONO_DPR %>">
          <%=listaDPR%>
        </select>
        </td>
      </tr>
    </table>  
  </div>
  
  <div id="divDepen" style="display:none" >
    <table id="tabDepen" width="95%" align="center"  >
    <tr><td class="Titolonocap" colspan=8> Norma Depenalizzante </td></tr>

  	<tr>
    	<td class="Titolo" width="15%">Fonte</td>
	    <td class="Titolo">Anno</td>
	    <td class="Titolo">Numero</td>
	    <td class="Titolo">Articolo</td>
	    <td class="Titolo">Art.qualificante</td>
	    <td class="Titolo">Comma</td>
	    <td class="Titolo">Lettera</td>
	    <td class="Titolo">Numero</td>
  	</tr>
  	<tr>
    	<td class="c" width="15%">
      	  <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_FONTE%>">
          	<%=TipiFontiReato%>
      	  </select>
    	</td>
	    <td class="c">
	      <input type="text"  size=4 maxlength=4 title="Anno Fonte" value="" 
	             name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_FONTE%>"
	             onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
	             >
	    </td>
	    <td class="c">
	      <input size=6 maxlength=6 title="Numero Fonte" value="" type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_FONTE%>">
	    </td>
	    <td class="c">
	      <input size=5 maxlength=5 title="Articolo Fonte" value="" type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ARTICOLO%>">
	    </td>
	    <td class="c">
	      <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_SOTTONUMERAZIONE%>">
	        <%=TipiSottonumerazione%>
	      </select>
	    </td>
	    <td class="c">
	      <input size=10 maxlength=10 title="Comma" value="" type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COMMA%>">
	    </td>
	    <td class="c">
	      <input size=2 maxlength=2 title="Lettera" value="" type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_LETTERA%>">
	    </td>
	    <td class="c">
	      <input size=2 maxlength=2 title="Numero" value="" type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO%>">
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
        <textarea cols="100" rows="3" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI %>"></textarea>
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
  var frmvalidator  = new Validator("RicGERevPA");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
 
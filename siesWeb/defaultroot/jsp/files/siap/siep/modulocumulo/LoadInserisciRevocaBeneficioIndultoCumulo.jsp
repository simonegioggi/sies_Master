<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiBeneficiCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.BeneficioCumuloModel"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"     scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"        scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="tipoFormBeneficio" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"           	scope="request" class="java.lang.String"/>
<jsp:useBean id="listaDPR"           	scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoProvvedimento"  	scope="request" class="java.lang.String"/>
<jsp:useBean id="beneficioCumulo"       scope="request" class="siap.siep.modulocumulo.model.BeneficioCumuloModel"/>
<jsp:useBean id="AutoritaEmittente"	 	scope="request" class="java.lang.String"/>
<%
//==============================================================================
// Form utilizzata nell'inserimento/Modifica dei benefici revocati in CUMULO
// (In particolare Revoca INDULTO di ALTRO PROVVEDIMENTO) 
//==============================================================================

%>

<!-- 			LoadInserisciRevocaBeneficioIndultoCumulo   		 -->
<html>
<head>
<title>[S.I.E.S.] -Gestione Cumulo - Revoca Beneficio Indulto - </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>

<script language="JavaScript">

    //==========================================================================
    // Ritorna alla lista delle Revoche
    //==========================================================================
    function eseguiFunzione(action)
    {
      var stringaChiamata = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>="+action;
      stringaChiamata+="&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>";
      stringaChiamata+="&<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>=<%=TitoloInCumulo.getIdTitoloCumulato()%>";
      document.location.href=stringaChiamata;
    }
    
    
  	function verify()
  	{
		var Tiprov=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_COD_TIPO_PROVVEDIMENTO%>.value;
		var CodAut=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_COD_TIPO_AUTORITA_EMITTENTE%>.value;
		var LuoAut=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_COD_LUOGO_EMITTENTE%>.value;

// Data Provvedimento    	
    	var AAprovv=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_PROVVEDIMENTO%>.value;
    	var MMprovv=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_MESE_PROVVEDIMENTO%>.value;
    	var GGprovv=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_PROVVEDIMENTO%>.value;
    	var data_to_verify = GGprovv+'/'+MMprovv+'/'+AAprovv;
     	if (!ControllaData(data_to_verify) && data_to_verify.length>2)
      	{ 
        	alert('Data Provvedimento NON corretta'); 
        	document.RevocaIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_PROVVEDIMENTO%>.focus(); 
        	return false; 
      	}     	

// Data Irrevocabilità    	
		if (Tiprov != "03")  // Sentenza o Decreto
   	  	{
	 		var AAirr=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_IRREVOCABILITA%>.value;
	    	var MMirr=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_MESE_IRREVOCABILITA%>.value;
	    	var GGirr=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_IRREVOCABILITA%>.value;  
	    	
	    	var data_to_verify = GGirr+'/'+MMirr+'/'+AAirr;
	    	
	     	if (!ControllaData(data_to_verify) && data_to_verify.length>2)
	      	{ 
	        	alert('Data Irrevocabilità NON corretta'); 
	        	document.RevocaIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_IRREVOCABILITA%>.focus(); 
	        	return false; 
	      	}
	     	
	  		 if((AAirr == "") ||
   	  			(MMirr == "") ||
   	  			(GGirr == "" ))
   	  		 {
      			alert("IL campo DEFINITIVO in DATA è OBBLIGATORIO ");
      			document.RevocaIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_IRREVOCABILITA%>.focus();
     			return false;
   	  		 }
   	  	}
	
		var Motivo=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_NOTE %>.value;
		var Dpr=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_COD_DPR %>.value;
		
		var AArecl=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_NUM_ANNI_RECLUSIONE %>.value;
		var MMrecl=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_NUM_MESI_RECLUSIONE %>.value;
		var GGrecl=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE %>.value;
		var AAarr=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_NUM_ANNI_ARRESTO %>.value;
		var MMarr=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_NUM_MESI_ARRESTO %>.value;
		var GGarr=document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_NUM_GIORNI_ARRESTO %>.value;
		
   		var Multa_INT = document.RevocaIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_IMPORTO_MULTA%>INT.value;
    	var Multa_DEC = document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_IMPORTO_MULTA%>DEC.value;
   		var Ammenda_INT = document.RevocaIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_IMPORTO_AMMENDA%>INT.value;
    	var Ammenda_DEC = document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_IMPORTO_AMMENDA%>DEC.value;    				

      	if(CodAut == "-")
      	{
  	      	alert("Il campo AUTORITA EMITTENTE è OBBLIGATORIO");
  	      	document.RevocaIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_COD_TIPO_AUTORITA_EMITTENTE%>.focus();
   	      	return false;
   	  	}
      	
      	if(LuoAut == "")
      	{
  	      	alert("Il campo LUOGO AUTORITA EMITTENTE è OBBLIGATORIO");
  	      	document.RevocaIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_COD_LUOGO_EMITTENTE%>.focus();
   	      	return false;
   	  	}
	

      	if(Dpr == "-")
      	{
  	      	alert("Il Campo PROVVEDIMENTO DI CONCESSIONE è OBBLIGATORIO");
  	      	document.RevocaIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_COD_DPR%>.focus();
   	     	return false;
   	  	}
   	  	
   	  	if((Multa_INT == "") &&
   	  	   (Multa_DEC != "")) 
  	  	{
    		alert("Inserire la Parte Intera (prima della virgola) del campo MULTA");
    		document.RevocaIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_IMPORTO_MULTA%>INT.focus();
    		return false;
   	  	}
   	  		
  	  	if((Ammenda_INT == "") &&
   	  	   (Ammenda_DEC != "")) 
  	  	{
    		alert("Inserire la Parte Intera (prima della virgola) del campo AMMENDA");
    		document.RevocaIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_IMPORTO_AMMENDA%>INT.focus();
    		return false;
   	  	} 

		return true;   	  	
   	  																															    	  		 		
	} //- - - - - - - - - - --  Chiude function verify

 	function ListaProcAssociatiIndu(a_formname)
    {
 		<%
 	    String lStrParametri = "";
 	    lStrParametri +="&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo();
 	    lStrParametri +="&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO+"="+TitoloInCumulo.getIdTitoloCumulato();
 	    %>
    	var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActLoadListaBeneficiDiAltroTitoloCumulato&formname="+a_formname+"<%=lStrParametri%>&<%=ICostantiBeneficiCumulo.TIPO_FORM_BENEFICIO %>=<%=tipoFormBeneficio%>", "Lista_Procedimenti_Associati", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=1000, height=450");
    }

    function ListaComuni(a_formname,a_fieldname)
    {
      	var desktop1;
      	desktop1 = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function EliminaAssociazione()
    {
    		document.RevocaIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_DEASSOCIA_TITOLO%>.value = "SI";
    		document.RevocaIndultoCumulo.testodeassocia.disabled = false;
    }
    
    function Inizia()
    {
   	 	var modo = "<%=modalita%>";
   	 	//alert('Inizia - modo = '+modo);
    	var valueSel = document.RevocaIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_COD_TIPO_PROVVEDIMENTO%>.options.value;
       	var nodeDadef =document.getElementById('divDadef');
       	var nodeList =document.getElementById('divList');
       	var nodeDeasso =document.getElementById('divDeassocia');
       	
       	if(modo == "I")
       	{	
	       	if(valueSel == '03') 	// Ordinanza
	       	{
	       		 nodeDadef.style.display='none';
	             nodeList.style.display='none';
	 			 document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_IRREVOCABILITA%>.disabled = true;
	 			 document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_MESE_IRREVOCABILITA%>.disabled = true;
	 			 document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_IRREVOCABILITA%>.disabled = true;	
	           	         	
	        }
			else	// Sentenza / Decerto
			{
				nodeDadef.style.display='block';
	        	nodeList.style.display='block';
				document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_IRREVOCABILITA%>.disabled = false;
				document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_MESE_IRREVOCABILITA%>.disabled = false;
				document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_IRREVOCABILITA%>.disabled = false; 
	        }
       	}
       	else if(modo == "M" )
       	{
       		nodeList.style.display='none';
       		
   		<%  if(beneficioCumulo.getTitIdTitoloCumulatoCollegato()!=null)
   			{  %>
   		
   				nodeDeasso.style.display='block';

   		<%	}
			else
			{ %>
				
				nodeDeasso.style.display='none';
			
		<%	} %>
       		
       		if(valueSel == '03') // Ordinanza
 			{
 				nodeDadef.style.display='none';
			 	document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_IRREVOCABILITA%>.disabled = true;
			 	document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_MESE_IRREVOCABILITA%>.disabled = true;
			 	document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_IRREVOCABILITA%>.disabled = true;
			       		
 			}
       		else		// Sentenza / Decerto
       		{
       			nodeDadef.style.display='block';
   				document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_IRREVOCABILITA%>.disabled = false;
   				document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_MESE_IRREVOCABILITA%>.disabled = false;
   				document.RevocaIndultoCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_IRREVOCABILITA%>.disabled = false; 
       		}	
       	}	
        
     } // End function Inizia 

</script>
</head>

<body class="corpo" onLoad="Inizia()">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      	<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
   		BeneficioCumuloModel lBeneficio = new BeneficioCumuloModel();
   		if( modalita.equals("I") )
   		{
%>
     		<font class="campo">Revoca Indulto concesso in altro provvedimento</font>
<%
   		}
   		else if( modalita.equals("M") )
   		{
     		lBeneficio = beneficioCumulo;
%>
     		<font class="campo">Modifica Revoca Indulto concesso in altro provvedimento</font>
<%
    	}
%>
		</td>
	  <td class="LBG">
      <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaRevocheBeneficiCumulo')">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
      </a>
    </td>			
	</tr>
  </table>

    <br>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
    	<jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
    <br>
<%
  	//==============================================
  	//               INSERIMENTO
  	//==============================================
%>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="RevocaIndultoCumulo">
  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRevocaBeneficioCumulo">
  	
<!--  			campi valorizzati dalla Popup di Elenco Benefici Concessi da Revocare		-->
   	<input type="HIDDEN" name="<%= ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO %>" >
  	<input type="HIDDEN" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_ID_PROVVEDIMENTO%>" >
  	<input type="HIDDEN" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_SENTENZA%>" >
  	<input type="HIDDEN" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_NUMERO_SENTENZA%>" >
  	<input type="HIDDEN" name="<%= ICostantiBeneficiCumulo.CAMPO_DEASSOCIA_TITOLO%>" >
  	<input type="HIDDEN" name="<%= ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_DA_REVOCARE %>" >
  	<input type="HIDDEN" name="<%= ICostantiBeneficiCumulo.CAMPO_TIT_ID_TITOLO_CUMULO_COLLEGATO%>" >

<!-- 			campi relativi al Beneficio (Revoca) Corrente  -->
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
    <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO %>" 	value="<%=StringUtils.toStringJSP(lBeneficio.getIdBeneficioCumulo()) %>">
    <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_FLAG_STATO %>"          	value="<%=StringUtils.toStringJSP(lBeneficio.getFlagStato(),"") %>">
    <input type="hidden" name="<%=ICostantiModuloCumulo.MODALITA %>" 				value="<%=modalita%>">
    <input type="hidden" name="<%=ICostantiBeneficiCumulo.TIPO_FORM_BENEFICIO %>" 	value="<%=tipoFormBeneficio%>">
   	

	<div id="divDeassocia" style="display:none; position:relative; ">
	<table width=95%>
 		<tr>
			<td class="l" colspan="4">
			Deassocia Titolo di concessione Revoca 
			<a href="Javascript:EliminaAssociazione();" title="Elimina associazione"><img src="/images/delete.gif" border=0></a>
			</td>
			<td>
			<input type="text" name="testodeassocia" style="color:red;" maxlength="100" size="100" value="Al momento della modifica il provvedimento di Revoca serà DEASSOCIATO dal titolo di concessione" disabled>
			</td>
 		</tr>
	</table>
	</div>
		
	<div id="divList" style="display:none; position:relative; ">
	<table>
 		<tr>
   			<td class="l" colspan="4" width=40% >
     		<a href="Javascript:ListaProcAssociatiIndu('RevocaIndultoCumulo');">
       		Elenco Indulti Concessi Sui Titoli In Istruttoria <img src="/images/filefolder.gif" border=0>
   			</a>
   			</td>
 		</tr>
	</table>
	</div>
	
	<table cellspacing=2 cellpadding=2 width=90%>
		<tr><td class="titolo" colspan=3 width=100%>Tipo Beneficio Revocato</td></tr>
	</table>
      
	<table cellspacing=2 cellpadding=2 width=90%>
		<tr>
			<td class="l" colspan=3 width=20%>Estremi Beneficio<font class=ob>(*)</font></td>
			<td class="L" colspan=3 width=20%> Indulto</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 			<td class="l" colspan=3 width=30%>Provvedimento di Concessione<font class=ob>(*)</font></td>
 			<td class="L" colspan=3 width=30%>
  				<select class=small name="<%= ICostantiBeneficiCumulo.CAMPO_COD_DPR%>">
   				<%= listaDPR %>
   				</select>
 			</td>
		</tr>
		<tr><td>&nbsp;</td></tr>
	</table> 

	<table cellspacing=2 cellpadding=2 width=90%>
		<tr><td class="titolo" colspan=3 width=100%>Estremi del Provvedimento di Concessione</td></tr>
	</table>  

	<table cellspacing=1 cellpadding=1 width=90%>
 	  <tr>
		<td class="l" width=20%>Tipo Provvedimento<font class=ob>(*)</font></td>
   		<td class="l" >
     		<select Title="Tipo Provvedimento" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_COD_TIPO_PROVVEDIMENTO %>" onChange="Inizia()">
       		<%= TipoProvvedimento %>
     		</select>
  		</td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
   		<td class="l">Data Provvedimento<font class=ob>(*)</font></td>
   		<td class="L" >
   			<input type="text" size="2" maxlength="2" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_PROVVEDIMENTO %>"
   				value = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(lBeneficio.getRifDataProvvedimento(),"dd") )%>"  
   				onFocus="javascript:textboxSelect(this)" 
   				onkeypress="return TicTabNumField(this,event)"  
   				onBlur="javascript:value=FillDM(value)"> -
   			<input type="text" size="2" maxlength="2" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_MESE_PROVVEDIMENTO %>"
   				value = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(lBeneficio.getRifDataProvvedimento(),"MM") )%>"   
   				onFocus="javascript:textboxSelect(this)" 
   				onkeypress="return TicTabNumField(this,event)"  
   				onBlur="javascript:value=FillDM(value)"> -
   			<input type="text" size="4" maxlength="4" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_PROVVEDIMENTO %>"
   				value = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(lBeneficio.getRifDataProvvedimento(),"yyyy") )%>"  
   				onFocus="javascript:textboxSelect(this)" 
   				onkeypress="return TicTabNumField(this,event)" 
   				onBlur="javascript:value=FillYear(value)" >
   		</td>
	  </tr>
	</table>	
        		
  <div id="divDadef" style="display:none; position:relative; ">
 
	<table cellspacing=1 cellpadding=1  width=90%>
		<tr>       
   		<td class="l" width=20%>Definitivo in Data<font class=ob>(*)</font></td>
   		<td class="L" colspan=3>
   			<input type="text" size="2" maxlength="2" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_IRREVOCABILITA %>"
   				value = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(lBeneficio.getRifDataIrrevocabilita(),"dd") )%>"   
   				onFocus="javascript:textboxSelect(this)" 
   				onkeypress="return TicTabNumField(this,event)"  
   				onBlur="javascript:value=FillDM(value)"> -
   			<input type="text" size="2" maxlength="2" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_MESE_IRREVOCABILITA %>"
   				value = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(lBeneficio.getRifDataIrrevocabilita(),"MM") )%>"   
   				onFocus="javascript:textboxSelect(this)" 
   				onkeypress="return TicTabNumField(this,event)"  
   				onBlur="javascript:value=FillDM(value)"> -
   			<input type="text" size="4" maxlength="4" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_IRREVOCABILITA %>"
   				value = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(lBeneficio.getRifDataIrrevocabilita(),"yyyy") )%>"  
   				onFocus="javascript:textboxSelect(this)" 
   				onkeypress="return TicTabNumField(this,event)" 
   				onBlur="javascript:value=FillYear(value)" >
   		</td>
 		</tr>
	</table>
	</div>    
		     
 	<table cellspacing=1 cellpadding=1 width=90%>  
		<tr>
   		<td class="l" width=20%>Emessa da<font class=ob>(*)</font></td>
   		<td class="l" colspan=3>
   			<select class=small name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_COD_TIPO_AUTORITA_EMITTENTE %>">
    			<%= AutoritaEmittente %>
   			</select>
   		</td>
				
   		<td class="l">Luogo<font class=ob>(*)</font></td>
   		<td class="L" colspan="3">
   			<input title="Luogo Emissione"  type="text" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_COD_LUOGO_EMITTENTE %>"
   				value="<%=StringUtils.toStringJSP(lBeneficio.getDescrLuogoEmittente() ) %>" maxlength="35" size="35">
     		<a href="Javascript:ListaComuni('RevocaIndultoCumulo','<%= ICostantiBeneficiCumulo.CAMPO_RIF_COD_LUOGO_EMITTENTE %>');">
    		<img src="/images/filefolder.gif" border=0></a>
   		</td>
		</tr>  
  			
  	<tr>  
    	<td class="l">Sezione</td>
    	<td class="L" colspan="3">
    		<input title="Num Sezione" value="<%=StringUtils.toStringJSP(lBeneficio.getRifNumSezioneAutoEmittente())%>" type="text" name="<%=ICostantiBeneficiCumulo.CAMPO_RIF_NUM_SEZIONE_AUTORITA_EMITTENTE%>" size="40" maxlength="100">
    	</td>       		
		</tr>
	</table>
	<br>	  	
<%
//==============================================================================
//                    Sezione con i quantum da imputare
//==============================================================================
%>
   	<table cellspacing=2 cellpadding=2 width=90%>
		<tr><td class="titolo" colspan=3 width=100%>Eventuale Quantum</td></tr>
	</table>
	
	<table cellspacing=2 cellpadding=2 width=90%>
		<tr>
 			<td class=titolo colspan=2>Reclusione</td>
  			<td width=25>&nbsp;</td>
  			<td class="titolo" colspan=2>Arresto</td>
  		</tr>			
 <!--  - - - - - - -   // RECLUSIONE e MULTA   - - - - - - - - - - - -->	
		<tr>
			<td class="c">
				<font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
				<font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
				<font class="label">Giorni</font><br>
				<input type="text" name="<%= ICostantiBeneficiCumulo.CAMPO_NUM_ANNI_RECLUSIONE %>" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lBeneficio.getNumAnniReclusione()) %>">&nbsp;
				<input type="text" name="<%= ICostantiBeneficiCumulo.CAMPO_NUM_MESI_RECLUSIONE %>" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lBeneficio.getNumMesiReclusione()) %>">&nbsp;
				<input type="text" name="<%= ICostantiBeneficiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE %>" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lBeneficio.getNumGiorniReclusione()) %>">
			</td>
 
    		<td class="c">
				<font class="label">Multa</font><br>
				<input type="text" style="text-align:right" maxlength="8" size="8" name="<%= ICostantiBeneficiCumulo.CAMPO_IMPORTO_MULTA%>INT"
					value="<%=StringUtils.getParteIntera(lBeneficio.getImportoMulta()) %>" onkeypress="return TicTabNumField(this,event)"> ,
				<input type="text" style="text-align:left" maxlength="2" size="2" name="<%= ICostantiBeneficiCumulo.CAMPO_IMPORTO_MULTA%>DEC"
					value="<%=StringUtils.getParteDecimale(lBeneficio.getImportoMulta()) %>" onkeypress="return TicTabNumField(this,event)">
    		</td>	
      
<!--  - - - - - - -   // ARRESTO e AMMENDA   - - - - - - - - - - - -->						
			
			<td width=25>&nbsp;</td>
			<td class="c">
				<font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
				<font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
				<font class="label">Giorni</font><br>
					<input type="text" name="<%= ICostantiBeneficiCumulo.CAMPO_NUM_ANNI_ARRESTO %>" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lBeneficio.getNumAnniArresto()) %>">&nbsp;
					<input type="text" name="<%= ICostantiBeneficiCumulo.CAMPO_NUM_MESI_ARRESTO %>" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lBeneficio.getNumMesiArresto()) %>">&nbsp;
					<input type="text" name="<%= ICostantiBeneficiCumulo.CAMPO_NUM_GIORNI_ARRESTO %>" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lBeneficio.getNumGiorniArresto()) %>">
			</td>

   			<td class="c">
				<font class="label">Ammenda</font><br>
					<input type="text" style="text-align:right" maxlength="8" size="8" name="<%= ICostantiBeneficiCumulo.CAMPO_IMPORTO_AMMENDA%>INT"
						value="<%=StringUtils.getParteIntera(lBeneficio.getImportoAmmenda() ) %>" onkeypress="return TicTabNumField(this,event)"> ,
					<input type="text" style="text-align:left" maxlength="2" size="2" name="<%= ICostantiBeneficiCumulo.CAMPO_IMPORTO_AMMENDA%>DEC"
						value="<%=StringUtils.getParteDecimale(lBeneficio.getImportoAmmenda() ) %>" onkeypress="return TicTabNumField(this,event)">						
			</td>
		</tr>
		
   		<tr>
       		<td class="l" class="label" style="text-align:right">Note</td>
   			<td class="l" colspan=3> 
        		<textarea cols="60" rows=2 name="<%=ICostantiBeneficiCumulo.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(lBeneficio.getNote(),"") %></textarea>
       		</td>
   		</tr>			
			        
	</table>
   	<br>
 
   	<table cellspacing="2" cellpadding="2" width="90%">
   	    <%
    //==========================================================================
    // Descrizione dello stato in cui si trova il dato (solo per modifica)
    //==========================================================================
    if (lBeneficio!=null && lBeneficio.getIdBeneficioCumulo()!=null)
    {
      String lDescStato = "";
      if      ( lBeneficio.getFlagStato().equals("E")){lDescStato = "Dato Estratto dal fascicolo originale";}
      else if ( lBeneficio.getFlagStato().equals("I")){lDescStato = "Dato Inserito manualmente dopo l'estrazione";}
      else if ( lBeneficio.getFlagStato().equals("M")){lDescStato = "Dato estratto modificato";}
      else if ( lBeneficio.getFlagStato().equals("C")){lDescStato = "Dato estratto cancellato";}
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
	        <textarea cols="100" rows="4" name="<%=ICostantiBeneficiCumulo.CAMPO_MOTIVO_MODIFICA%>"><%=StringUtils.toStringJSP(lBeneficio.getMotivoModifica(), "") %></textarea>
	      </td>
	    </tr>
 
	 	<tr>
<%		if(modalita.equals("I") )
		{	%>         
    		<td colspan=3>
    			<input class=bottone  type="submit" title="Inserisci Revoca" name="Inserisci" value="Conferma" >
    		</td>
<%		}
		else if(modalita.equals("M") )
		{	%>   
			<td colspan=3>
    			<input class=bottone  type="submit" title="Modifica Revoca" name="Inserisci" value="Conferma" >
    		</td>		
<%		} %>		     		      
    	</tr>
	</table> 
    	    
</form>
  		
<% // --- - - - - - - - - -- - - - -- - - - - - - // %>

 	<script language="JavaScript" type="text/javascript">
     var frmvalidator  = new Validator("RevocaIndultoCumulo");

     // Data Provvedimento
   	 frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_PROVVEDIMENTO %>","req","Il campo GIORNO PROVVEDIMENTO è obbligatorio");
   	 frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_RIF_MESE_PROVVEDIMENTO %>","req","Il campo MESE PROVVEDIMENTO è obbligatorio");
   	 frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_PROVVEDIMENTO %>","req","Il campo ANNO PROVVEDIMENTO è obbligatorio");
   	 
     frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_PROVVEDIMENTO %>","numeric","Il GIORNO PROVVEDIMENTO è un campo numerico");
     frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_RIF_MESE_PROVVEDIMENTO %>","numeric","Il MESE PROVVEDIMENTO è un campo numerico");
     frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_PROVVEDIMENTO %>","numeric","Il ANNO PROVVEDIMENTO è un campo numerico");

     frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_PROVVEDIMENTO %>","minlen=4","La lunghezza minima per ANNO PROVVEDIMENTO è di 4 caratteri");
     frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_PROVVEDIMENTO %>","gt=1900", "ANNO PROVVEDIMENTO deve essere Maggiore di 1900");
     frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_PROVVEDIMENTO %>","lt=3000", "ANNO PROVVEDIMENTO deve essere Minore di 3000");
   	 
    // Quantum Reclusione/Multa 
   	frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_NUM_ANNI_RECLUSIONE %>","numeric","Il campo Anni Reclusione può contenere solo caratteri numerici");
  	frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_NUM_MESI_RECLUSIONE %>","numeric","Il campo Mesi Reclusione può contenere solo caratteri numerici");
  	frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE %>","numeric","Il campo Giorni Reclusione può contenere solo caratteri numerici");
  
  	frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_IMPORTO_MULTA%>INT","numeric","Il campo Multa Reclusione può contenere solo caratteri numerici");
  	frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_IMPORTO_MULTA%>DEC","numeric","Il campo Multa Reclusione può contenere solo caratteri numerici");
 
  	// Quantum Arresto/Ammenda
  	frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_NUM_ANNI_ARRESTO %>","numeric","Il campo Anni Arresto può contenere solo caratteri numerici");
  	frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_NUM_MESI_ARRESTO %>","numeric","Il campo Mesi Arresto può contenere solo caratteri numerici");
  	frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_NUM_GIORNI_ARRESTO %>","numeric","Il campo Giorni Arresto può contenere solo caratteri numerici");
  
  	frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_IMPORTO_AMMENDA%>INT","numeric","Il campo Ammenda Arresto può contenere solo caratteri numerici");
  	frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_IMPORTO_AMMENDA%>DEC","numeric","Il campo Ammenda Arresto può contenere solo caratteri numerici");

  	frmvalidator.setAddnlValidationFunction("verify");

   </script>
</body>
</html>
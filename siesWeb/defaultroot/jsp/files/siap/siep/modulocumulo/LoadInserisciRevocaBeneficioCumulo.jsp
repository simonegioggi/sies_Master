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

<jsp:useBean id="modalita"                 	scope="request" class="java.lang.String"/>
<jsp:useBean id="AutoritaEmittente" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoProvvedimento" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="beneficioCumulo"			scope="request" class="siap.siep.modulocumulo.model.BeneficioCumuloModel"/>


<%
//==============================================================================
// Form utilizzata nell'INSERIMENTO/MODIFICA dei benefici revocati in CUMULO 
// (Revoca SOSPENSIONE CONDIZIONALE e NON MENZIONE) 
//==============================================================================

String CodTipoBene="";
if(beneficioCumulo!=null && beneficioCumulo.getIdBeneficioCumulo()!=null)
{
	if(beneficioCumulo.getCodTipoBeneficio()!=null)
	{
		CodTipoBene=beneficioCumulo.getCodTipoBeneficio();	
	}
}
%>

<!--  	LoadInserisciRevocaBeneficioCumulo		 -->
<html>
<head>
	<title>[S.I.E.S.] Gestione Cumulo - Revoca Beneficio Sospensione/Non Menzione - </title>

	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>

	<script language="JavaScript">

	var Modo = "<%=modalita%>";
	var TipoBene = "<%=CodTipoBene%>";
	
	
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
    
    
   function init()
   {
   		//alert('SospNomenz - Modo = '+Modo+' -  TipoBene = '+TipoBene);
   		if(Modo=="M")
   		{
   			
		<%	if(beneficioCumulo.getTitIdTitoloCumulatoCollegato()!=null)
   			{  %>
   			
	   			if(TipoBene == "01")
	   			{
	   				// Checkbox per selezione Sospensione Condizionale
	   	        	document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_SOSP_COND %>.disabled = false;
	   	        	document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_SOSP_COND %>.checked = true;
	   	       		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_NON_MENZIONE %>.disabled = true;
	   	       		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_NON_MENZIONE %>.checked = false;
	   			}	
	   			
	   			if(TipoBene == "02")
	   			{
	   	        	// Checkbox per selezioneNon Menzione
	   	        	document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_NON_MENZIONE %>.disabled = false;
	   	       		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_NON_MENZIONE %>.checked = true;
	   	       		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_SOSP_COND %>.disabled = true;
	   	       		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_SOSP_COND %>.checked = false;
	   			}
	   			
	   	<%	}
			else
			{	%>	
			
				if(TipoBene == "01")
				{
					// Checkbox per selezione Sospensione Condizionale
		        	document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_SOSP_COND %>.checked = true;
		       		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_NON_MENZIONE %>.checked = false;
				}	
				
				if(TipoBene == "02")
				{
		        	// Checkbox per selezioneNon Menzione
		       		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_NON_MENZIONE %>.checked = true;
		       		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_SOSP_COND %>.checked = false;
				}
			
		<%	}  %>
	   	
   		}	
   }
   
   function EliminaAssociazione()
   {
   		document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_DEASSOCIA_TITOLO%>.value = "SI";
   		document.RevocaBeneficioCumulo.testodeassocia.disabled = false;
   		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_SOSP_COND %>.disabled = false;
   		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_NON_MENZIONE %>.disabled = false;
   }

  	function verify()
  	{
  		//alert('verify');
  		
		var CodAut=document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_COD_TIPO_AUTORITA_EMITTENTE%>.value;
		var LuoAut=document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_COD_LUOGO_EMITTENTE%>.value;

 // Anno e Numero Provvedimento		
		if(document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_SENTENZA%>.value == "" )
    	{
    		alert('ANNO Provvedimento è Obbligatorio');
    		document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_SENTENZA%>.focus();
    		return false;
    	}
 
		if(document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_SENTENZA%>.value.length < 4 )
    	{
    		alert('ANNO Provvedimento deve essere NUMERICO di 4 caratteri');
    		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_SENTENZA%>.focus();
    		return false;
    	}
		
		if(document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_NUMERO_SENTENZA%>.value == "" )
    	{
    		alert('Numero Provvedimento  è Obbligatorio');
    		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_NUMERO_SENTENZA%>.focus();
    		return false;
    	}

// Data Provvedimento   	
    	if(isNaN(document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_PROVVEDIMENTO%>.value) )
    	{
    		alert('Giorno Provvedimento deve essere NUMERICO');
    		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_PROVVEDIMENTO%>.focus();
    		return false;
    	}
   		
    	if(isNaN(document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_MESE_PROVVEDIMENTO%>.value) )
    	{
    		alert('Mese Provvedimento deve essere NUMERICO');
    		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_MESE_PROVVEDIMENTO%>.focus();
    		return false;
    	}
    	
    	if(isNaN(document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_PROVVEDIMENTO%>.value) )
    	{
    		alert('ANNO Provvedimento deve essere NUMERICO');
    		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_PROVVEDIMENTO%>.focus();
    		return false;
    	}
    	
    	if (document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_PROVVEDIMENTO%>.value.length==1)
		  	document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_PROVVEDIMENTO%>.value='0'+document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_PROVVEDIMENTO%>.value;
	  	if (document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_MESE_PROVVEDIMENTO%>.value.length==1)
		  	document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_MESE_PROVVEDIMENTO%>.value='0'+document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_MESE_PROVVEDIMENTO%>.value;

	  	var data_to_verify = document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_PROVVEDIMENTO%>.value+'/'+document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_MESE_PROVVEDIMENTO%>.value+'/'+document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_PROVVEDIMENTO%>.value;

    	if (!ControllaData(data_to_verify) )
	  	{
    		alert('Data Provvedimento Errata');
    		document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_PROVVEDIMENTO%>.focus();
		   	return false;     	
	  	}
    	
    	if(document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_PROVVEDIMENTO%>.value.length<4 )
    	{
    		alert('ANNO Provvedimento deve essere NUMERICO di 4 caratteri');
    		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_PROVVEDIMENTO%>.focus();
    		return false;
    	}
    	
// Data Irrevocabilità    	
     	if (document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_IRREVOCABILITA%>.value.length==1)
		  	document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_IRREVOCABILITA%>.value='0'+document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_IRREVOCABILITA%>.value;
	  	if (document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_MESE_IRREVOCABILITA%>.value.length==1)
		  	document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_MESE_IRREVOCABILITA%>.value='0'+document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_MESE_IRREVOCABILITA%>.value;

	  	var data_to_verify = document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_IRREVOCABILITA%>.value+'/'+document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_MESE_IRREVOCABILITA%>.value+'/'+document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_IRREVOCABILITA%>.value;

    	if (!ControllaData(data_to_verify) )
	  	{
    		alert('Data Irrevocabilita Errata');
    		document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_IRREVOCABILITA%>.focus();
		   	return false;
	  	}
     	
// Autorità Emittente    	
      	if(CodAut == "-")
      	{
  	      	alert("Il campo AUTORITA EMITTENTE è OBBLIGATORIO");
  	    	document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_COD_TIPO_AUTORITA_EMITTENTE%>.focus(); 
   	      	return false;
   	  	}
      	
      	if(LuoAut == "" || LuoAut == "-" )
      	{
  	      	alert("Il campo LUOGO AUTORITA EMITTENTE è OBBLIGATORIO");
  	    	document.RevocaBeneficioCumulo.<%=ICostantiBeneficiCumulo.CAMPO_RIF_COD_LUOGO_EMITTENTE%>.focus(); 
   	      	return false;
   	  	}

// Sospensione / Non Menzione      	
		if( document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_SOSP_COND %>.checked == false &&
	 		document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_NON_MENZIONE %>.checked == false)
		{
       			alert("Selezionare UNO tra Non Menzione e Sospensione Condizionale");
       			return false;
    	}
		
		if(document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_NON_MENZIONE %>.checked == true && 
			document.RevocaBeneficioCumulo.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_SOSP_COND %>.checked == true )
		{
			alert("Selezionare SOLO UNO tra Non Menzione e Sospensione Condizionale");
 			return false;
		}	
			
    	
    	return true;     

	} // Chiude function verify


 	function ListaBeneficiAssociati(a_formname)
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
    
</script>
</head>
					
<body class="corpo" OnLoad="Javascript:init();">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      	<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%			String Desc="";
   			if( modalita.equals("I") )
   			{
     			 Desc="Revoca Benefici concessi in altro provvedimento";
			}
   			else if( modalita.equals("M") )
   			{	
   				Desc="Modifica Revoca Benefici concessi in altro provvedimento";
   			} %>
   			
   			<font class="campo"><%= Desc %></font>
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
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="RevocaBeneficioCumulo">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRevocaBeneficioCumulo">

<!--  			campi valorizzati dalla Popup di Elenco Benefici Concessi da Revocare		-->
  <input type="HIDDEN" name="<%= ICostantiBeneficiCumulo.CAMPO_COD_NATURA_BENEFICIO %>" >
  <input type="HIDDEN" name="<%= ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO %>" >
  <input type="HIDDEN" name="<%= ICostantiBeneficiCumulo.CAMPO_COD_DPR %>" >
  <input type="HIDDEN" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_ID_PROVVEDIMENTO%>" >
  <input type="HIDDEN" name="<%= ICostantiBeneficiCumulo.CAMPO_DEASSOCIA_TITOLO%>" >
  <input type="HIDDEN" name="<%= ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_DA_REVOCARE %>" >
  <input type="HIDDEN" name="<%= ICostantiBeneficiCumulo.CAMPO_TIT_ID_TITOLO_CUMULO_COLLEGATO%>" >

<!-- 			campi relativi al Beneficio (Revoca) Corrente  -->    	
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO %>" value="<%=StringUtils.toStringJSP(beneficioCumulo.getIdBeneficioCumulo()) %>">
  <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_FLAG_STATO %>"          value="<%=StringUtils.toStringJSP(beneficioCumulo.getFlagStato(),"") %>">

  <input type="hidden" name="<%=ICostantiModuloCumulo.MODALITA %>" value="<%=modalita%>">
  <input type="hidden" name="<%=ICostantiBeneficiCumulo.TIPO_FORM_BENEFICIO %>" value="<%=tipoFormBeneficio%>">
    	
	<table>
		<tr>
 <%	if (modalita.equals("M") ) {
		if (beneficioCumulo.getTitIdTitoloCumulatoCollegato()!=null)	{	%>
			<td class="l" colspan="4">
				Deassocia Titolo di concessione Revoca&nbsp;
				<a href="Javascript:EliminaAssociazione();" title="Elimina associazione"><img src="/images/delete.gif" border="0"></a>
<!-- 			</td> -->
				&nbsp;&nbsp;&nbsp;&nbsp;
<!-- 			<td> -->
				<input type="text" name="testodeassocia" style="color:red;" maxlength="100" size="100" value="Al momento della modifica il provvedimento di Revoca sarà DEASSOCIATO dal titolo di concessione" disabled>
			</td>
<%		
    } else {
%>
   		<td class="l" colspan="4">
 		<a href="Javascript:ListaBeneficiAssociati('RevocaBeneficioCumulo');">
 		Elenco delle Pene Sospese/Non Menzione nel Cumulo&nbsp;<img src="/images/filefolder.gif" border="0">
 		</a>
   		</td>
<%    }
	}
 	else if (modalita.equals("I")) {	%>
   			<td class="l" colspan="4">
 				<a href="Javascript:ListaBeneficiAssociati('RevocaBeneficioCumulo');">
 					Elenco delle Pene Sospese/Non Menzione nel Cumulo&nbsp;<img src="/images/filefolder.gif" border="0">
 				</a>
			</td>
<%	} %> 
  		
	 </tr>
	</table>	
	<table cellspacing=2 cellpadding=2 width=95%>
		<tr><td class="titolo" colspan=3 width=100%>Tipo Beneficio Revocato</td></tr>
	</table>
      
	<table cellspacing=2 cellpadding=2>
	  <tr>
		<td class="l" width=30% >Sospensione Condizionale</td>
		<td class="l" colspan=3><input type="checkbox" name="<%= ICostantiBeneficiCumulo.CAMPO_FLAG_SOSP_COND %>" value="1"></td>
	  </tr>
	  <tr>
   		<td class="l" width=30% >Non Menzione</td>
   		<td class="l" colspan=3><input type="checkbox" name="<%= ICostantiBeneficiCumulo.CAMPO_FLAG_NON_MENZIONE %>" value="1"></td>
	  </tr>
	  <tr><td>&nbsp;</td></tr>
	</table>
   
   	<table cellspacing=2 cellpadding=2 width=95%>
 	  <tr><td class="titolo" colspan=3 width=100%>Estremi del provvedimento di Concessione</td></tr>
   	</table>  

   	<table cellspacing=1 cellpadding=1 width=95%>
	<tr>
		<td class="l">Tipo Provvedimento<font class=ob>(*)</font></td>
   		<td class="l" colspan=3>
      		<select  name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_COD_TIPO_PROVVEDIMENTO %>" Title="Tipo Provvedimento" >
			<%= TipoProvvedimento %>
       		</select>
  		</td>
  		
  		<td class="l">Anno/Numero Provvedimento <font class=ob>(*)</font></td>
		<td class="L" colspan=3>
			<input type="text" size="4" maxlength="4" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_SENTENZA%>"
				value = "<%=StringUtils.toStringJSP(beneficioCumulo.getRifAnnoProvvedimento(),"")%>"   
				onFocus="javascript:textboxSelect(this)" 
				onkeypress="return TicTabNumField(this,event)"  
				onBlur="javascript:value=FillDM(value)"> -
			<input type="text" size="8" maxlength="8" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_NUMERO_SENTENZA%>"
				value = "<%=StringUtils.toStringJSP(beneficioCumulo.getRifNumeroProvvedimento(),"")%>"  
				onFocus="javascript:textboxSelect(this)" 
				onkeypress="return TicTabNumField(this,event)" >

		</td> 
	
	</tr>
     
	<tr>
   		<td class="l">Data Provvedimento<font class=ob>(*)</font></td>
   		<td class="L" colspan=3>
   			<input type="text" size="2" maxlength="2" name="<%=ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_PROVVEDIMENTO %>" 
   				value = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(beneficioCumulo.getRifDataProvvedimento(),"dd") )%>"
   				onFocus="javascript:textboxSelect(this)"
   				onkeypress="return TicTabNumField(this,event)" 
   				onBlur="javascript:value=FillDM(value)"> -

   			<input type="text" size="2" maxlength="2" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_MESE_PROVVEDIMENTO %>"
   				value = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(beneficioCumulo.getRifDataProvvedimento(),"MM") )%>"   
   				onFocus="javascript:textboxSelect(this)" 
				onkeypress="return TicTabNumField(this,event)"  
				onBlur="javascript:value=FillDM(value)"> -
			<input type="text" size="4" maxlength="4" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_PROVVEDIMENTO %>"
				value = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(beneficioCumulo.getRifDataProvvedimento(),"yyyy") )%>"  
				onFocus="javascript:textboxSelect(this)" 
				onkeypress="return TicTabNumField(this,event)" 
				onBlur="javascript:value=FillYear(value)" >
		</td>
        
		<td class="l">Definitivo in Data<font class=ob>(*)</font></td>
		<td class="L" colspan=3>
			<input type="text" size="2" maxlength="2" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_IRREVOCABILITA %>"
				value = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(beneficioCumulo.getRifDataIrrevocabilita(), "dd"), "")%>"   
				onFocus="javascript:textboxSelect(this)" 
				onkeypress="return TicTabNumField(this,event)"  
				onBlur="javascript:value=FillDM(value)"> -
			<input type="text" size="2" maxlength="2" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_MESE_IRREVOCABILITA %>"
				value = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(beneficioCumulo.getRifDataIrrevocabilita(), "MM"), "")%>"   
				onFocus="javascript:textboxSelect(this)" 
				onkeypress="return TicTabNumField(this,event)"  
				onBlur="javascript:value=FillDM(value)"> -
			<input type="text" size="4" maxlength="4" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_IRREVOCABILITA %>"
				value = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(beneficioCumulo.getRifDataIrrevocabilita(), "yyyy"), "")%>"  
				onFocus="javascript:textboxSelect(this)" 
				onkeypress="return TicTabNumField(this,event)" 
				onBlur="javascript:value=FillYear(value)" >
		</td>        
	</tr>
    
	<tr>
    	<td class="l">Pronunciata da<font class=ob>(*)</font></td>
        <td class="l" colspan=3>
        	<select class=small name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_COD_TIPO_AUTORITA_EMITTENTE %>">
        	<%= AutoritaEmittente %>
        	</select>
        </td>
 
        <td class="l">Luogo<font class=ob>(*)</font></td>
        <td class="L" colspan="3">
        	<input title="Luogo Emissione"  type="text" name="<%= ICostantiBeneficiCumulo.CAMPO_RIF_COD_LUOGO_EMITTENTE %>" 
        			value="<%=StringUtils.toStringJSP(beneficioCumulo.getDescrLuogoEmittente() ) %>" maxlength="35" size="35">
        	<a href="Javascript:ListaComuni('RevocaBeneficioCumulo','<%= ICostantiBeneficiCumulo.CAMPO_RIF_COD_LUOGO_EMITTENTE %>');">
        	<img src="/images/filefolder.gif" border=0>
        	</a>
        </td>
  	</tr>  
  			
  	<tr>  
    	<td class="l">Sezione</td>
    	<td class="L" colspan="3">
    		<input title="Num Sezione" value="<%=StringUtils.toStringJSP(beneficioCumulo.getRifNumSezioneAutoEmittente())%>" type="text" name="<%=ICostantiBeneficiCumulo.CAMPO_RIF_NUM_SEZIONE_AUTORITA_EMITTENTE%>" size="40" maxlength="100">
    	</td>       		
	</tr>        
  </table>
  <br>

  <table cellspacing="2" cellpadding="2" width="95%">

    <%
    //==========================================================================
    // Descrizione dello stato in cui si trova il dato (solo per modifica)
    //==========================================================================
    if (beneficioCumulo!=null && beneficioCumulo.getIdBeneficioCumulo()!=null)
    {
      String lDescStato = "";
      if      ( beneficioCumulo.getFlagStato().equals("E")){lDescStato = "Dato Estratto dal fascicolo originale";}
      else if ( beneficioCumulo.getFlagStato().equals("I")){lDescStato = "Dato Inserito manualmente dopo l'estrazione";}
      else if ( beneficioCumulo.getFlagStato().equals("M")){lDescStato = "Dato estratto modificato";}
      else if ( beneficioCumulo.getFlagStato().equals("C")){lDescStato = "Dato estratto cancellato";}
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
        <textarea cols="100" rows="4" name="<%=ICostantiBeneficiCumulo.CAMPO_MOTIVO_MODIFICA%>"><%=StringUtils.toStringJSP(beneficioCumulo.getMotivoModifica(),"") %></textarea>
      </td>
    </tr>
 
	 <tr>
<%	if(modalita.equals("I") )
	{	%>         
    	<td colspan=3>
    	<input class=bottone  type="submit" title="Inserisci Revoca" name="Inserisci" value="Conferma" >
    	</td>
<%	}
	else if(modalita.equals("M") )
	{	%>   
		<td colspan=3>
    	<input class=bottone  type="submit" title="Modifica Revoca" name="Inserisci" value="Conferma" >
    	</td>		
<%	} %>		     		      
    </tr>
  </table> 
</form>
	<script language="JavaScript" type="text/javascript">
     var frmvalidator  = new Validator("RevocaBeneficioCumulo");
     
     frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_SENTENZA %>","gt=1900", "ANNO PROVVEDIMENTO deve essere Maggiore di 1900");
     frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_SENTENZA %>","lt=3000", "ANNO PROVVEDIMENTO deve essere Minore di 3000");
     
     frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_PROVVEDIMENTO %>","gt=1900", "ANNO PROVVEDIMENTO deve essere Maggiore di 1900");
     frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_PROVVEDIMENTO %>","lt=3000", "ANNO PROVVEDIMENTO deve essere Minore di 3000");
   	 
     frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_IRREVOCABILITA %>","gt=1900", "ANNO IRREVOCABILITA deve essere Maggiore di 1900");
     frmvalidator.addValidation("<%= ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_IRREVOCABILITA %>","lt=3000", "ANNO IRREVOCABILITA deve essere Minore di 3000");
   	 
     frmvalidator.setAddnlValidationFunction("verify");
  	</script>
</body>
</html>
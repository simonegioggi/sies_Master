<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.beneficio.model.BeneficioModel"%>
<%@ page import="siap.siep.beneficio.action.ICostantiBeneficio"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>

<jsp:useBean id="modalita"                 	scope="request" class="java.lang.String"/>
<jsp:useBean id="soggetto" 					scope="session" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="concesso_revocato"        	scope="request" class="java.lang.String"/>
<jsp:useBean id="ComingFromInsert"         	scope="request" class="java.lang.String"/>
<jsp:useBean id="CodTipoAutoritaEmittente" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="CodTipoProvvedimento" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="lCognome"            		scope="request" class="java.lang.String"/>
<jsp:useBean id="lNome"            			scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Form utilizzata nell'INSERIMENTO dei benefici revocati  
// (In particolare Revoca SOSPENSIONE CONDIZIONALE ALTRO PROVVEDIMENTO) 
//==============================================================================
//
%>

<html>
<head>
<title>[S.I.E.S.] -Inserimento Revoca Beneficio - Sospensione Condizionale- </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>

<script language="JavaScript">

  	function verify()
  	{
   		var indice=5;
   		var vuotoCodAut = true;
   		//vero se tutta la griglia del dpr è vuoto.

		var Tiprov=document.LoadInsRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value;
		var CodAut=document.LoadInsRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value;
		var LuoAut=document.LoadInsRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_COD_LUOGO_EMITTENTE%>.value;

// Data Provvedimento   	
    	var AAprovv=document.LoadInsRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_ANNO_PROVVEDIMENTO%>.value;
    	var MMprovv=document.LoadInsRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_MESE_PROVVEDIMENTO%>.value;
    	var GGprovv=document.LoadInsRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_GIORNO_PROVVEDIMENTO%>.value;
		var data_to_verify = GGprovv+'/'+MMprovv+'/'+AAprovv;
     	if (!ControllaData(data_to_verify) && data_to_verify.length>2)
      	{ 
        	alert('Data Provvedimento NON corretta'); 
        	document.LoadInsRevocaSospCondAP.<%=ICostantiBeneficio.CAMPO_GIORNO_PROVVEDIMENTO%>.focus(); 
        	return false; 
      	}     	

// Data Irrevocabilità    	
   	 	var AAirr=document.LoadInsRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_ANNO_IRREVOCABILITA%>.value;
    	var MMirr=document.LoadInsRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_MESE_IRREVOCABILITA%>.value;
    	var GGirr=document.LoadInsRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_GIORNO_IRREVOCABILITA%>.value;   	
    	var data_to_verify = GGirr+'/'+MMirr+'/'+AAirr;
     	if (!ControllaData(data_to_verify) && data_to_verify.length>2)
      	{ 
        	alert('Data Irrevocabilità NON corretta'); 
        	document.LoadInsRevocaSospCondAP.<%=ICostantiBeneficio.CAMPO_GIORNO_IRREVOCABILITA%>.focus(); 
        	return false; 
      	}     	

      	if(CodAut == "-")
      	{
  	      alert("Il campo PRONUNCIATA DA è OBBLIGATORIO");
   	      return false;
   	  	} 	

		if( document.LoadInsRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_FLAG_SOSP_COND %>.checked == false &&
	 		document.LoadInsRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_FLAG_NON_MENZIONE %>.checked == false)
		{
       			alert("Selezionare Almeno un campo tra Non Menzione e Sospensione Condizionale");
       			return false;
    	}
    	
    	return true;     

	} // Chiude function verify

  		function VerifyChiamate(id)
		{
   			if(id==1)
  			{
  				// nella form c'è un solo bottone, non sarà mai id=1
          		document.LoadInsRevocaSospCondAP.<%=IWebConstants.ACTION_FIELD%>.value = "";
  			}
  			else if(id==2)
  			{
          		document.LoadInsRevocaSospCondAP.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.beneficio.action.ActInserisciRevocaSospCondAP";
  			}

		} //  Chiude function verifyChiamate 

 	function ListaProcAssociati(a_formname)
    {
    	var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.beneficio.action.ActListaProcAssociati&formname="+a_formname+"&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>&", "Lista_Procedimenti_Associati", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }

    function ListaComuni(a_formname,a_fieldname)
    {
      	var desktop1;
      	desktop1 = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

</script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      	<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
   			BeneficioModel lBeneficio = new BeneficioModel();
   			if( modalita.equals("I") )
   			{
     			String desc=new String("Revoca della Sospensione Condizionale della Pena/Non Menzione concessa in altro provvedimento");
%>
     			<font class="campo"><%= desc %></font>
	<%
   			}
 	%>
		</td>
	</tr>
  </table>

    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
<%

  	//==============================================
  	//               INSERIMENTO
  	//==============================================
  	if( modalita.equals("I") )
  	{
%>
    	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInsRevocaSospCondAP">
    	<input type="HIDDEN" name="<%= ICostantiBeneficio.CAMPO_COD_NATURA_BENEFICIO %>" value="<%= concesso_revocato %>">
    	<input type="HIDDEN" name="<%= ICostantiBeneficio.CAMPO_SEN_ID_SENTENZA %>" value="">
    	
<%
    	if( !ComingFromInsert.equals("YES") )
    	{
%>
      		<table cellspacing=2 cellpadding=2 width=85%>
      			<tr><td class="titolo" colspan=3 width=100%>Tipo Beneficio Revocato</td></tr>
      		</table>
      
      		<table cellspacing=2 cellpadding=2>
      			<tr>
        			<td class="l" width=30% >Sospensione Condizionale</td>
        			<td class="l" colspan=3><input type="checkbox" name="<%= ICostantiBeneficio.CAMPO_FLAG_SOSP_COND %>" value="1"></td>
     			</tr>
     			<tr>
        			<td class="l" width=30% >Non Menzione</td>
        			<td class="l" colspan=3><input type="checkbox" name="<%= ICostantiBeneficio.CAMPO_FLAG_NON_MENZIONE %>" value="1"></td>
      			</tr>
       
      			<tr><td>&nbsp;</td></tr>
      		</table>
<%
    	}  // end coming from insert
%>
		<table>
	  		<tr>
        		<td class="l" colspan="4">
        			<a href="Javascript:ListaProcAssociati('LoadInsRevocaSospCondAP');">
        			Elenco delle Pene Sospese/Non Menzione nel Distretto <img src="/images/filefolder.gif" border=0>
        			</a>
        		</td>
      		</tr>
		</table>      
   
    	<table cellspacing=2 cellpadding=2 width=85%>
      		<tr><td class="titolo" colspan=3 width=100%>Estremi del provvedimento</td></tr>
    	</table>  

    	<table cellspacing=1 cellpadding=1>
    		<tr>
 				<td class="l">Tipo Provvedimento<font class=ob>(*)</font></td>
        		<td class="l" colspan=3>
             		<select  name="<%= ICostantiBeneficio.CAMPO_COD_TIPO_PROVVEDIMENTO %>" Title="Tipo Provvedimento" >
     				<%= CodTipoProvvedimento %>
             		</select>
        		</td>
    		</tr>
     
    		<tr>
        		<td class="l">Data Provvedimento<font class=ob>(*)</font></td>
        		<td class="L" colspan=3>
          			<input type="text" size="2" maxlength="2" name="<%= ICostantiBeneficio.CAMPO_GIORNO_PROVVEDIMENTO %>"  
          				onFocus="javascript:textboxSelect(this)" 
          				onkeypress="return TicTabNumField(this,event)"  
          				onBlur="javascript:value=FillDM(value)"> -
          			<input type="text" size="2" maxlength="2" name="<%= ICostantiBeneficio.CAMPO_MESE_PROVVEDIMENTO %>"  
          				onFocus="javascript:textboxSelect(this)" 
          				onkeypress="return TicTabNumField(this,event)"  
          				onBlur="javascript:value=FillDM(value)"> -
          			<input type="text" size="4" maxlength="4" name="<%= ICostantiBeneficio.CAMPO_ANNO_PROVVEDIMENTO %>" 
          				onFocus="javascript:textboxSelect(this)" 
          				onkeypress="return TicTabNumField(this,event)" 
          				onBlur="javascript:value=FillYear(value)" >
        		</td>
        
        		<td class="l">Definitivo in Data<font class=ob>(*)</font></td>
        		<td class="L" colspan=3>
          			<input type="text" size="2" maxlength="2" name="<%= ICostantiBeneficio.CAMPO_GIORNO_IRREVOCABILITA %>"  
          				onFocus="javascript:textboxSelect(this)" 
          				onkeypress="return TicTabNumField(this,event)"  
          				onBlur="javascript:value=FillDM(value)"> -
          			<input type="text" size="2" maxlength="2" name="<%= ICostantiBeneficio.CAMPO_MESE_IRREVOCABILITA %>"  
          				onFocus="javascript:textboxSelect(this)" 
          				onkeypress="return TicTabNumField(this,event)"  
          				onBlur="javascript:value=FillDM(value)"> -
          			<input type="text" size="4" maxlength="4" name="<%= ICostantiBeneficio.CAMPO_ANNO_IRREVOCABILITA %>" 
          				onFocus="javascript:textboxSelect(this)" 
          				onkeypress="return TicTabNumField(this,event)" 
          				onBlur="javascript:value=FillYear(value)" >
        		</td>        
     		</tr>
     	</table>
     
     	<table>        
			<tr>
        		<td class="l">Pronunciata da<font class=ob>(*)</font></td>
        		<td class="l" colspan=3>
        			<select class=small name="<%= ICostantiBeneficio.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
        			<%= CodTipoAutoritaEmittente %>
        			</select>
        		</td>
 
        		<td class="l">Luogo<font class=ob>(*)</font></td>
        		<td class="L" colspan="3">
          			<input title="Luogo Emissione"  type="text" name="<%= ICostantiBeneficio.CAMPO_COD_LUOGO_EMITTENTE %>" value="" maxlength="35" size="35">
          			<a href="Javascript:ListaComuni('LoadInsRevocaSospCondAP','<%= ICostantiBeneficio.CAMPO_COD_LUOGO_EMITTENTE %>');">
            			<img src="/images/filefolder.gif" border=0>
          			</a>
        		</td>
  			</tr>  
  			
  			<tr>  
        		<td class="l">Sezione</td>
        		<td class="L" colspan="3">
          			<input title="Num Sezione" value="<%=StringUtils.toStringJSP(lBeneficio.getRifNumSezioneAutoEmittente())%>" type="text" name="<%=ICostantiBeneficio.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE%>" size="10">
        		</td>       		
			</tr>        
		</table>
    	<br>
 
   <% //CONFERMA o RICERCA %>  
   							<% // Esempio di gestione di 2 bottoni (anche se qui mi serve solo CONFERMA) %>	

    	<table>
      		<tr>
       			<!--td colspan=3>
       			<input class=bottone  type="submit" name="Ricerca" value="Ricerca" onclick="javascript:return VerifyChiamate(1);">
        		</td-->
        
        		<td colspan=3>
        		<input class=bottone  type="submit" name="Inserisci" value="Conferma" onclick="javascript:return VerifyChiamate(2);">
        		</td>      
      		</tr>
    	</table> 

    	<input value="<%=lBeneficio.getIdBeneficio() %>" type="HIDDEN" name="<%= ICostantiBeneficio.CAMPO_ID_BENEFICIO %>">
    	<input type="HIDDEN" name="<%=ICostantiBeneficio.CAMPO_PROVENIENZA%>" value="<%=concesso_revocato%>">
    	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    
  		</form>
  		
  <%}  // Chiude If Inserimento   %>

 	<script language="JavaScript" type="text/javascript">
     var frmvalidator  = new Validator("LoadInsRevocaSospCondAP");
		
    	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_GIORNO_PROVVEDIMENTO%>","req","Il campo DATA PROVVEDIMENTO è obbligatorio");
    	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_MESE_PROVVEDIMENTO%>","req","Il campo DATA PROVVEDIMENTO è obbligatorio");
    	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_ANNO_PROVVEDIMENTO%>","req","Il campo DATA PROVVEDIMENTO è obbligatorio");
    	 
    	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_GIORNO_IRREVOCABILITA	%>","req","Il campo DEFINITIVO IN DATA è obbligatorio");
    	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_MESE_IRREVOCABILITA	%>","req","Il campo DEFINITIVO IN DATA è obbligatorio");
    	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_ANNO_IRREVOCABILITA	%>","req","Il campo DEFINITIVO IN DATA è obbligatorio");
    	 
    	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_COD_LUOGO_EMITTENTE%>","req","Il campo LUOGO è obbligatorio");
    
<%--
  //================================================================
  // Aggiungere le opportune chiamate al genvalidator
  //================================================================
  //frmvalidator.addValidation("","req","Il campo XXXX è obbligatorio");
  //frmvalidator.addValidation("","numeric","Il XXXX è un campo numerico");
  //frmvalidator.addValidation("","maxlen=4","La lunghezza massima per 
XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","minlen=4","La lunghezza minima per 
XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","gt=1900");
  //frmvalidator.addValidation("","lt=3000");
  //frmvalidator.addValidation("","alphanumeric");
  //frmvalidator.addValidation("","numeric");
  //frmvalidator.addValidation("","alpha");
  //frmvalidator.addValidation("","alnumhyphen");
  //frmvalidator.addValidation("","email");
  //frmvalidator.addValidation("","regexp");
  //frmvalidator.addValidation("","dontselect");
--%>

  frmvalidator.setAddnlValidationFunction("verify");

   </script>
</body>
</html>
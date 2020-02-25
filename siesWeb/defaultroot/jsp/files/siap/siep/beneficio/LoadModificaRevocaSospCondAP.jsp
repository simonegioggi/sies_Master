<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="siap.siep.beneficio.model.BeneficioModel"%>
<%@ page import="siap.siep.beneficio.action.ICostantiBeneficio"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>

<jsp:useBean id="modalita"                 scope="request" class="java.lang.String"/>
<jsp:useBean id="CodTipoAutoritaEmittente" scope="request" class="java.lang.String"/>
<jsp:useBean id="CodTipoProvvedimento" 		scope="request" class="java.lang.String"/>

<jsp:useBean id="soggetto" scope="session" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="beneficio" scope="request" class="siap.siep.beneficio.model.BeneficioModel"/>
<%
//==============================================================================
// Form utilizzata nella MODIFICA dei benefici revocati  
// (di tipo Revoca SOSPENSIONE CONDIZIONALE / NON MENZIONE in ALTRO PROVVEDIMENTO) 
//==============================================================================

//	FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");
%>
<html>
	<head>
		<title>[S.I.E.S.] -Modifica Revoca Beneficio - Sospensione Condizionale/Non Menzione - </title>

			<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

			<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
			<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
			<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
		
	<script language="JavaScript">

  	function verify()
  	{
   		var indice=5;
   		var vuotoSospSub = true;
   		var vuotoCodAut = true;
   		//vero se tutta la griglia del dpr è vuoto.

		var Tiprov=document.LoadModificaRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value;
		var CodAut=document.LoadModificaRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value;
		var LuoAut=document.LoadModificaRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_COD_LUOGO_EMITTENTE%>.value;

// Data Provvedimento   	
    	var AAprovv=document.LoadModificaRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_ANNO_PROVVEDIMENTO%>.value;
    	var MMprovv=document.LoadModificaRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_MESE_PROVVEDIMENTO%>.value;
    	var GGprovv=document.LoadModificaRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_GIORNO_PROVVEDIMENTO%>.value;
		var data_to_verify = GGprovv+'/'+MMprovv+'/'+AAprovv;
     	if (!ControllaData(data_to_verify) && data_to_verify.length>2)
      	{ 
        	alert('Data Provvedimento NON corretta'); 
        	document.LoadModificaRevocaSospCondAP.<%=ICostantiBeneficio.CAMPO_GIORNO_PROVVEDIMENTO%>.focus(); 
        	return false; 
      	}     	

// Data Irrevocabilità   	 	
   	 	var AAirr=document.LoadModificaRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_ANNO_IRREVOCABILITA%>.value;
    	var MMirr=document.LoadModificaRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_MESE_IRREVOCABILITA%>.value;
    	var GGirr=document.LoadModificaRevocaSospCondAP.<%= ICostantiBeneficio.CAMPO_GIORNO_IRREVOCABILITA%>.value;   	
    	var data_to_verify = GGirr+'/'+MMirr+'/'+AAirr;
     	if (!ControllaData(data_to_verify) && data_to_verify.length>2)
      	{ 
        	alert('Data Irrevocabilità NON corretta'); 
        	document.LoadModificaRevocaSospCondAP.<%=ICostantiBeneficio.CAMPO_GIORNO_IRREVOCABILITA%>.focus(); 
        	return false; 
      	}
      	
      	if(CodAut == "-")
      	{
  	      alert("Il campo PRONUNCIATA DA è OBBLIGATORIO");
   	      return false;
   	  	} 
   	  	
   	  	return true;	
	
	} // Chiude function verify

  		function VerifyChiamate(id)
		{
   			if(id==1)
  			{
          		document.LoadModificaRevocaSospCondAP.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.beneficio.action.ActModificaRevocaSospCondAP";
  			}
  			else if(id==2)
  			{
  				// nella form c'è un solo bottone, non sarà mai id=2
          		document.LoadModificaRevocaSospCondAP.<%=IWebConstants.ACTION_FIELD%>.value = "";
  			}

		} //  Chiude function verifyChiamate 

    function ListaComuni(a_formname,a_fieldname)
    {
      	var desktop1;
      	desktop1 = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

 	function ListaProcAssociati(a_formname)
    {
    	var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.beneficio.action.ActListaProcAssociati&formname="+a_formname+"&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>&", "Lista_Procedimenti_Associati", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }

   	function Sosnom()
    {
    	var valueSel = document.LoadModificaRevocaSospCondAP.<%=ICostantiBeneficio.CAMPO_COD_TIPO_BENEFICIO%>.value;
       	var DivSos =document.getElementById('DivSos');
       	var DivNom =document.getElementById('DivNom');
       
       	if(valueSel == '01') 
       	{
           	DivSos.style.display='block';
           	DivNom.style.display='none';
        }
		else
		{
			DivSos.style.display='none';
			DivNom.style.display='block';
        }
         
    }    // chiude Sosnom
        
		</script>
	</head>

	<body class="corpo" onLoad="Sosnom()">
  		<table>
    		<tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      			<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
		<%
   				BeneficioModel lBeneficio = new BeneficioModel();
   				if( modalita.equals("M") )
   				{
     				lBeneficio = beneficio;
		%>
     				<font class="campo">Modifica Revoca Beneficio - Sospensione Condizionale della Pena/Non Menzione concessa in altro provvedimento</font>
			<%  } %>

				</td>
			</tr>
  		</table>

    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
<%

  	//==============================================
  	//               MODIFICA
  	//==============================================
  	if( modalita.equals("M") )
  	{

%>
    	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadModificaRevocaSospCondAP">
    	<input type="HIDDEN" name="<%= ICostantiBeneficio.CAMPO_COD_NATURA_BENEFICIO %>" value="<%=lBeneficio.getCodNaturaBeneficio()%>">
    	<input type="hidden" name="<%= ICostantiBeneficio.CAMPO_COD_TIPO_BENEFICIO %>" value="<%=lBeneficio.getCodTipoBeneficio()%>">
    	<input type="HIDDEN" name="<%= ICostantiBeneficio.CAMPO_SEN_ID_SENTENZA %>" value="">    	
	
      		<table cellspacing=2 cellpadding=2 width=100%>
      			<tr><td class="titolo" colspan=3 width=100%>Tipo Beneficio Revocato</td></tr>
      		</table>
  <%  // - - - - - - - - - - -  Sospensione Condizionale - - - - - - - - -- - - - - -- - - -   %>
  
		<div id="DivSos" style="display:none; position:relative; ">
      		<table cellspacing=2 cellpadding=2>
      			<tr>
        			<td class="l">Sospensione Condizionale</td>
        			<td class="l" colspan=3><input disabled type="checkbox" name="<%= ICostantiBeneficio.CAMPO_FLAG_SOSP_COND %>"
		<% 			if(lBeneficio.getCodTipoBeneficio().equals("01")) 
    				{ %>        			
        				checked
        		<%	} %>		
        			>
        			</td>
      			</tr>
      		</table>
      	</div>
      			      			
     <%  // - - - - - - - - - - -  Non Menzione - - - - - - - - -- - - - - -- - - -  %>
     
     	<div id="DivNom" style="display:none; position:relative; ">
			<table>
      			<tr>
        			<td class="l">Non Menzione</td>
 					<td class="l" colspan=3><input disabled type="checkbox" name="<%= ICostantiBeneficio.CAMPO_FLAG_NON_MENZIONE %>"
    		<% 		if (lBeneficio.getCodTipoBeneficio().equals("02")) 
    				{ %>
       					checked
     			<%	} %>		
					>
					</td>        				
      			</tr>
      		</table>
         </div>		

<%
     // - - - - - - - - - FINE PARTE RELATIVA ALLA REVOCA DA MODIFICARE - - - - - - - - - - - - - %>

		<table>
	  		<tr>
        		<td class="l" colspan="4">
        			<a href="Javascript:ListaProcAssociati('LoadModificaRevocaSospCondAP');">
          			Seleziona Lista di Procedimenti Associati <img src="/images/filefolder.gif" border=0>
        			</a>
        		</td>
      		</tr>
		</table>
   
    	<table cellspacing=2 cellpadding=2 width=100%>
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
          				value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(lBeneficio.getRifDataProvvedimento(),"dd"))%>  
          					onFocus="javascript:textboxSelect(this)" 
          					onkeypress="return TicTabNumField(this,event)"  
          					onBlur="javascript:value=FillDM(value)"> -
          			<input type="text" size="2" maxlength="2" name="<%= ICostantiBeneficio.CAMPO_MESE_PROVVEDIMENTO %>"
          				value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(lBeneficio.getRifDataProvvedimento(),"MM"))%>  
          					onFocus="javascript:textboxSelect(this)" 
          					onkeypress="return TicTabNumField(this,event)"  
          					onBlur="javascript:value=FillDM(value)"> -
          			<input type="text" size="4" maxlength="4" name="<%= ICostantiBeneficio.CAMPO_ANNO_PROVVEDIMENTO %>"
          				value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(lBeneficio.getRifDataProvvedimento(),"yyyy"))%> 
          					onFocus="javascript:textboxSelect(this)" 
          					onkeypress="return TicTabNumField(this,event)" 
          					onBlur="javascript:value=FillYear(value)" >
        		</td>
        
        		<td class="l">Definitivo in Data<font class=ob>(*)</font></td>
        		<td class="L" colspan=3>
          			<input type="text" size="2" maxlength="2" name="<%= ICostantiBeneficio.CAMPO_GIORNO_IRREVOCABILITA %>"
          				value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(lBeneficio.getRifDataIrrevocabilita(),"dd"))%>  
          					onFocus="javascript:textboxSelect(this)" 
          					onkeypress="return TicTabNumField(this,event)"  
          					onBlur="javascript:value=FillDM(value)"> -
          			<input type="text" size="2" maxlength="2" name="<%= ICostantiBeneficio.CAMPO_MESE_IRREVOCABILITA %>"
          				value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(lBeneficio.getRifDataIrrevocabilita(),"MM"))%>  
          					onFocus="javascript:textboxSelect(this)" 
          					onkeypress="return TicTabNumField(this,event)"  
          					onBlur="javascript:value=FillDM(value)"> -
          			<input type="text" size="4" maxlength="4" name="<%= ICostantiBeneficio.CAMPO_ANNO_IRREVOCABILITA %>"
          				value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(lBeneficio.getRifDataIrrevocabilita(),"yyyy"))%> 
          					onFocus="javascript:textboxSelect(this)" 
          					onkeypress="return TicTabNumField(this,event)" 
          					onBlur="javascript:value=FillYear(value)" >
        		</td>        
     		</tr>
     	</table>
     
     	<table>        
			<tr>
        		<td class="l">Pronunciata da<font class=ob>(*)</font></td>
        		<td class="L" colspan=3>
        			<select class=big name="<%= ICostantiBeneficio.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
        			<%= CodTipoAutoritaEmittente %>
        			</select>
        		</td>
 
        		<td class="l">Luogo<font class=ob>(*)</font></td>
        		<td class="L" colspan="3">
          			<input title="Luogo Emissione"  type="text" name="<%= ICostantiBeneficio.CAMPO_COD_LUOGO_EMITTENTE %>" value="<%=StringUtils.toStringJSP(lBeneficio.getDescrLuogoAutoritaEmittente()) %>" maxlength="35" size="35">
          			<a href="Javascript:ListaComuni('LoadModificaRevocaSospCondAP','<%= ICostantiBeneficio.CAMPO_COD_LUOGO_EMITTENTE %>');">
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
   							<% // Esempio di gestione di 2 bottoni (anche se qui mi serve solo MODIFICA) %>	

    	<table>
      		<tr>
       			<!--td colspan=3>
       			<input class=bottone  type="submit" name="Ricerca" value="Ricerca" onclick="javascript:return VerifyChiamate(2);">
        		</td-->
        
        		<td colspan=3>
        		<input class=bottone  type="submit" name="Inserisci" value="Modifica" onclick="javascript:return VerifyChiamate(1);">
        		</td>      
      		</tr>
    	</table> 

   		<input type="HIDDEN" name="<%=ICostantiBeneficio.CAMPO_ID_BENEFICIO %>" value="<%=lBeneficio.getIdBeneficio()%>">
    	<input type="HIDDEN" name="<%=ICostantiBeneficio.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" value="<%=lBeneficio.getFasSieIdFascicoloSiep()%>">
    	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
   
  		</form>

<%
	} // End MODIFICA
%>

 	<script language="JavaScript" type="text/javascript">
     var frmvalidator  = new Validator("LoadModificaRevocaSospCondAP");
		
    	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_GIORNO_PROVVEDIMENTO%>","req","Il campo DATA PROVVEDIMENTO è obbligatorio");
    	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_MESE_PROVVEDIMENTO%>","req","Il campo DATA PROVVEDIMENTO è obbligatorio");
    	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_ANNO_PROVVEDIMENTO%>","req","Il campo DATA PROVVEDIMENTO è obbligatorio");
    	 
    	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_GIORNO_IRREVOCABILITA	%>","req","Il campo DEFINITIVO IN DATA è obbligatorio");
    	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_MESE_IRREVOCABILITA	%>","req","Il campo DEFINITIVO IN DATA è obbligatorio");
    	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_ANNO_IRREVOCABILITA	%>","req","Il campo DEFINITIVO IN DATA è obbligatorio");
    	 
    	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_COD_LUOGO_EMITTENTE%>","req","Il campo LUOGO è obbligatorio");

  		frmvalidator.setAddnlValidationFunction("verify");

   </script>
</body>
</html>
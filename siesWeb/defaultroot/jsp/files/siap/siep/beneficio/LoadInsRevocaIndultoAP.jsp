<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.beneficio.model.BeneficioModel"%>
<%@ page import="siap.siep.beneficio.action.ICostantiBeneficio"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>

<jsp:useBean id="modalita"                 scope="request" class="java.lang.String"/>
<jsp:useBean id="listaDPR"                 scope="request" class="java.lang.String"/>
<jsp:useBean id="CodTipoProvvedimento"     scope="request" class="java.lang.String"/>
<jsp:useBean id="beneficio"                scope="request" class="siap.siep.beneficio.model.BeneficioModel"/>
<jsp:useBean id="concesso_revocato"        scope="request" class="java.lang.String"/>
<jsp:useBean id="ComingFromInsert"         scope="request" class="java.lang.String"/>
<jsp:useBean id="CodTipoAutoritaEmittente" scope="request" class="java.lang.String"/>
<jsp:useBean id="lCognome"            		scope="request" class="java.lang.String"/>
<jsp:useBean id="lNome"            			scope="request" class="java.lang.String"/>

<jsp:useBean id="soggetto" scope="session" class="siap.sico.soggetto.model.SoggettoModel"/>
<%
//==============================================================================
// Form utilizzata nell'inserimento dei benefici revocati
// (In particolare Revoca SOSPENSIONE INDULTO ALTRO PROVVEDIMENTO) 
//==============================================================================

%>

<html>
<head>
<title>[S.I.E.S.] -Inserimento Revoca Beneficio - Indulto - </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>

<script language="JavaScript">

  	function verify()
  	{
		var Tiprov=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value;
		var CodAut=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value;
		var LuoAut=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_COD_LUOGO_EMITTENTE%>.value;

// Data Provvedimento    	
    	var AAprovv=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_ANNO_PROVVEDIMENTO%>.value;
    	var MMprovv=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_MESE_PROVVEDIMENTO%>.value;
    	var GGprovv=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_GIORNO_PROVVEDIMENTO%>.value;
    	var data_to_verify = GGprovv+'/'+MMprovv+'/'+AAprovv;
     	if (!ControllaData(data_to_verify) && data_to_verify.length>2)
      	{ 
        	alert('Data Provvedimento NON corretta'); 
        	document.LoadInsRevocaIndultoAP.<%=ICostantiBeneficio.CAMPO_GIORNO_PROVVEDIMENTO%>.focus(); 
        	return false; 
      	}     	

// Data Irrevocabilità    	
		var AAirr=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_ANNO_IRREVOCABILITA%>.value;
    	var MMirr=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_MESE_IRREVOCABILITA%>.value;
    	var GGirr=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_GIORNO_IRREVOCABILITA%>.value;    	
    	var data_to_verify = GGirr+'/'+MMirr+'/'+AAirr;
     	if (!ControllaData(data_to_verify) && data_to_verify.length>2)
      	{ 
        	alert('Data Irrevocabilità NON corretta'); 
        	document.LoadInsRevocaIndultoAP.<%=ICostantiBeneficio.CAMPO_GIORNO_IRREVOCABILITA%>.focus(); 
        	return false; 
      	}     	
	
		var Motivo=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_NOTE %>.value;
		var Dpr=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_COD_DPR %>.value;
		
		var AArecl=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_NUM_ANNI_RECLUSIONE %>.value;
		var MMrecl=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_NUM_MESI_RECLUSIONE %>.value;
		var GGrecl=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_NUM_GIORNI_RECLUSIONE %>.value;
		var AAarr=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_NUM_ANNI_ARRESTO %>.value;
		var MMarr=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_NUM_MESI_ARRESTO %>.value;
		var GGarr=document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_NUM_GIORNI_ARRESTO %>.value;
		
   		var Multa_INT = document.LoadInsRevocaIndultoAP.<%=ICostantiBeneficio.CAMPO_IMPORTO_MULTA%>INT.value;
    	var Multa_DEC = document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_IMPORTO_MULTA%>DEC.value;
   		var Ammenda_INT = document.LoadInsRevocaIndultoAP.<%=ICostantiBeneficio.CAMPO_IMPORTO_AMMENDA%>INT.value;
    	var Ammenda_DEC = document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_IMPORTO_AMMENDA%>DEC.value;    				

      	if(CodAut == "-")
      	{
  	      	alert("Il campo EMESSA DA è OBBLIGATORIO");
   	      	return false;
   	  	} 	
	

      	if(Dpr == "-")
      	{
  	      	alert("Il Campo PROVVEDIMENTO DI CONCESSIONE è OBBLIGATORIO");
   	     	return false;
   	  	}
   	  	
   	  	if (Tiprov != "03") 
   	  	{
   	  		 if((AAirr == "") ||
   	  			(MMirr == "") ||
   	  			(GGirr == "" ))
   	  		 {
 	      			alert("IL campo DEFINITIVO in DATA è OBBLIGATORIO ");
   	     			return false;
   	  		 }
   	  	}

   	  	if((Multa_INT == "") &&
   	  	   (Multa_DEC != "")) 
  	  	{
 	    		alert("Inserire la Parte Intera (prima della virgola) del campo MULTA");
   	    		return false;
   	  	}
   	  		
  	  	if((Ammenda_INT == "") &&
   	  	   (Ammenda_DEC != "")) 
  	  	{
 	    		alert("Inserire la Parte Intera (prima della virgola) del campo AMMENDA");
   	    		return false;
   	  	} 

		return true;   	  	
   	  																															    	  		 		
	} //- - - - - - - - - - --  Chiude function verify

  		function VerifyChiamate(id)
		{
   			if(id==1)
  			{
          		document.LoadInsRevocaIndultoAP.<%=IWebConstants.ACTION_FIELD%>.value = "";
  			}
  			else if(id==2)
  			{
          		document.LoadInsRevocaIndultoAP.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.beneficio.action.ActInserisciRevocaIndultoAP";
  			}

		} //  Chiude function verifyChiamate 

 	function ListaProcAssociatiIndu(a_formname)
    {
    	var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.beneficio.action.ActListaProcAssociatiIndu&formname="+a_formname+"&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>&", "Lista_Procedimenti_Associati_Indu", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }

    function ListaComuni(a_formname,a_fieldname)
    {
      	var desktop1;
      	desktop1 = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function Dadef()
    {
    	var valueSel = document.LoadInsRevocaIndultoAP.<%=ICostantiBeneficio.CAMPO_COD_TIPO_PROVVEDIMENTO%>.options.value;
       	var nodeDadef =document.getElementById('divDadef');
       	var nodeList =document.getElementById('divList');
       	if(valueSel != '03') 
       	{
           	nodeDadef.style.display='block';
           	nodeList.style.display='block';
			document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_ANNO_IRREVOCABILITA%>.disabled = false;
			document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_MESE_IRREVOCABILITA%>.disabled = false;
			document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_GIORNO_IRREVOCABILITA%>.disabled = false;           	
        }
		else
		{
            nodeDadef.style.display='none';
            nodeList.style.display='none';
			document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_ANNO_IRREVOCABILITA%>.disabled = true;
			document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_MESE_IRREVOCABILITA%>.disabled = true;
			document.LoadInsRevocaIndultoAP.<%= ICostantiBeneficio.CAMPO_GIORNO_IRREVOCABILITA%>.disabled = true;	
        }
        
     } // End function Dadef 

</script>
</head>

<body class="corpo" onLoad="Dadef()">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      	<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
   		BeneficioModel lBeneficio = new BeneficioModel();
   		if( modalita.equals("I") )
   		{
     		String desc=new String("Revoca Indulto concesso in altro provvedimento");
%>
     		<font class="campo"><%= desc %></font>
<%
   		}
   		else if( modalita.equals("M") )
   		{
     		lBeneficio = beneficio;
%>
     		<font class="campo">Modifica Beneficio</font>
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
    lCognome = soggetto.getCognome();
    lNome = soggetto.getNome();

  	//==============================================
  	//               INSERIMENTO
  	//==============================================
  	if( modalita.equals("I") )
  	{
%>
    	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInsRevocaIndultoAP">
    	<input type="HIDDEN" name="<%= ICostantiBeneficio.CAMPO_COD_NATURA_BENEFICIO %>" value="<%= concesso_revocato %>">
    	<input type="HIDDEN" name="<%= ICostantiBeneficio.CAMPO_SEN_ID_SENTENZA %>">
<%
    	if( !ComingFromInsert.equals("YES") )
    	{
%>
      		<table cellspacing=2 cellpadding=2 width=90%>
      			<tr><td class="titolo" colspan=3 width=100%>Tipo Beneficio Revocato</td></tr>
      		</table>
      
      		<table cellspacing=2 cellpadding=2 width=90%>
      			<tr>
					<td class="l" colspan=3 width=20%>Estremi Beneficio<font class=ob>(*)</font></td>
					<td class="L" colspan=3 width=20%> : Indulto</td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
        			<td class="l" colspan=3 width=30%>Provvedimento di Concessione<font class=ob>(*)</font></td>
        			<td class="L" colspan=3 width=30%>
        				<select class=small name="<%= ICostantiBeneficio.CAMPO_COD_DPR%>">
        				<%= listaDPR %>
        				</select>
        			</td>
      			</tr>
      			<tr><td>&nbsp;</td></tr>
      		</table>
<%
    	}  // end coming from insert
%>
		<div id="divList" style="display:none; position:relative; ">
		<table>
	  		<tr>
        		<td class="l" colspan="4" width=40%>
        		<a href="Javascript:ListaProcAssociatiIndu('LoadInsRevocaIndultoAP');">
        		Elenco Indulti nel Distretto  <img src="/images/filefolder.gif" border=0>
       			</a>
        		</td>
      		</tr>
		</table>
		</div> 

      		<table cellspacing=2 cellpadding=2 width=90%>
      			<tr><td class="titolo" colspan=3 width=100%>Estremi del Provvedimento da Revocare</td></tr>
      		</table>  

		<table cellspacing=1 cellpadding=1 width=90%>
    		<tr>
 				<td class="l">Tipo Provvedimento<font class=ob>(*)</font></td>
        		<td class="l" colspan=3>
             		<select Title="Tipo Provvedimento" name="<%= ICostantiBeneficio.CAMPO_COD_TIPO_PROVVEDIMENTO %>" onChange="Dadef()">
             		<%= CodTipoProvvedimento %>
             		</select>
        		</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
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
 			</tr>
 		</table>	
        		
        <div id="divDadef" style="display:none; position:relative; ">
 
 		<table cellspacing=1 cellpadding=1>
    		<tr>       
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
		</div>    
		     
     	<table cellspacing=1 cellpadding=1 width=90%>  
			<tr>
        		<td class="l">Emessa da<font class=ob>(*)</font></td>
        		<td class="l" colspan=3>
        			<select class=small name="<%= ICostantiBeneficio.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
        			<%= CodTipoAutoritaEmittente %>
        			</select>
        		</td>
 				
        		<td class="l">Luogo<font class=ob>(*)</font></td>
        		<td class="L" colspan="3">
          			<input title="Luogo Emissione"  type="text" name="<%= ICostantiBeneficio.CAMPO_COD_LUOGO_EMITTENTE %>" value="" maxlength="35" size="35">
          			<a href="Javascript:ListaComuni('LoadInsRevocaIndultoAP','<%= ICostantiBeneficio.CAMPO_COD_LUOGO_EMITTENTE %>');">
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
					<input type="text" name="<%= ICostantiBeneficio.CAMPO_NUM_ANNI_RECLUSIONE %>" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lBeneficio.getNumAnniReclusione()) %>">&nbsp;
					<input type="text" name="<%= ICostantiBeneficio.CAMPO_NUM_MESI_RECLUSIONE %>" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lBeneficio.getNumMesiReclusione()) %>">&nbsp;
					<input type="text" name="<%= ICostantiBeneficio.CAMPO_NUM_GIORNI_RECLUSIONE %>" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lBeneficio.getNumGiorniReclusione()) %>">
				</td>
 
      			<td class="c">
					<font class="label">Multa</font><br>
					<input type="text" style="text-align:right" maxlength="8" size="8" name="<%= ICostantiBeneficio.CAMPO_IMPORTO_MULTA%>INT"
						value="" onkeypress="return TicTabNumField(this,event)"> ,
					<input type="text" style="text-align:left" maxlength="2" size="2" name="<%= ICostantiBeneficio.CAMPO_IMPORTO_MULTA%>DEC"
						value="" onkeypress="return TicTabNumField(this,event)">
    			</td>	
      
<!--  - - - - - - -   // ARRESTO e AMMENDA   - - - - - - - - - - - -->						
			
				<td width=25>&nbsp;</td>
				<td class="c">
					<font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
					<font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
					<font class="label">Giorni</font><br>
					<input type="text" name="<%= ICostantiBeneficio.CAMPO_NUM_ANNI_ARRESTO %>" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lBeneficio.getNumAnniArresto()) %>">&nbsp;
					<input type="text" name="<%= ICostantiBeneficio.CAMPO_NUM_MESI_ARRESTO %>" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lBeneficio.getNumMesiArresto()) %>">&nbsp;
					<input type="text" name="<%= ICostantiBeneficio.CAMPO_NUM_GIORNI_ARRESTO %>" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lBeneficio.getNumGiorniArresto()) %>">
				</td>

      			<td class="c">
					<font class="label">Ammenda</font><br>
					<input type="text" style="text-align:right" maxlength="8" size="8" name="<%= ICostantiBeneficio.CAMPO_IMPORTO_AMMENDA%>INT"
						value="" onkeypress="return TicTabNumField(this,event)"> ,
					<input type="text" style="text-align:left" maxlength="2" size="2" name="<%= ICostantiBeneficio.CAMPO_IMPORTO_AMMENDA%>DEC"
						value="" onkeypress="return TicTabNumField(this,event)">						
				</td>
			</tr>
		
      		<tr>
        		<td class="l" class="label" style="text-align:right">Note</td>
        		<td class="l" colspan=3> 
        		<textarea cols="60" rows=2 name="<%= ICostantiBeneficio.CAMPO_NOTE %>"><%=StringUtils.toStringJSP(lBeneficio.getNote()) %>
        		</textarea>
        		</td>
      		</tr>			
			        
		</table>
    	<br>
 
   <% //CONFERMA o RICERCA %>  
   							<% // Esempio di gestione di 2 bottoni (anche se qui mi serve solo CONFERMA) %>	

    	<table>
      		<tr>
        		<td colspan=3>
        		<input class=bottone  type="submit" name="Inserisci" value="Conferma" onclick="javascript:return VerifyChiamate(2);">
        		</td>      
      		</tr>
    	</table> 
    	
    	<input value="<%=lBeneficio.getIdBeneficio() %>" type="HIDDEN" name="<%= ICostantiBeneficio.CAMPO_ID_BENEFICIO %>">
    	<input type="HIDDEN" name="<%=ICostantiBeneficio.CAMPO_PROVENIENZA%>" value="<%=concesso_revocato%>">
    	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    
  	</form>
  		
  <%}  // Chiude If Inserimento   
  
  // --- - - - - - - - - -- - - - -- - - - - - -
  %>
 	<script language="JavaScript" type="text/javascript">
     var frmvalidator  = new Validator("LoadInsRevocaIndultoAP");

   	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_GIORNO_PROVVEDIMENTO %>","req","Il campo DATA PROVVEDIMENTO è obbligatorio");
   	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_MESE_PROVVEDIMENTO %>","req","Il campo DATA PROVVEDIMENTO è obbligatorio");
   	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_ANNO_PROVVEDIMENTO %>","req","Il campo DATA PROVVEDIMENTO è obbligatorio");
   	 
   	 frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo LUOGO è obbligatorio");
 
  	frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_NUM_ANNI_RECLUSIONE %>","numeric","Il campo Anni Reclusione può contenere solo caratteri numerici");
  	frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_NUM_MESI_RECLUSIONE %>","numeric","Il campo Mesi Reclusione può contenere solo caratteri numerici");
  	frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_NUM_GIORNI_RECLUSIONE %>","numeric","Il campo Giorni Reclusione può contenere solo caratteri numerici");
  
  	frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_IMPORTO_MULTA%>INT","numeric","Il campo Multa Reclusione può contenere solo caratteri numerici");
  	frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_IMPORTO_MULTA%>DEC","numeric","Il campo Multa Reclusione può contenere solo caratteri numerici");
 
  	frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_NUM_ANNI_ARRESTO %>","numeric","Il campo Anni Arresto può contenere solo caratteri numerici");
  	frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_NUM_MESI_ARRESTO %>","numeric","Il campo Mesi Arresto può contenere solo caratteri numerici");
  	frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_NUM_GIORNI_ARRESTO %>","numeric","Il campo Giorni Arresto può contenere solo caratteri numerici");
  
  	frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_IMPORTO_AMMENDA%>INT","numeric","Il campo Ammenda Arresto può contenere solo caratteri numerici");
  	frmvalidator.addValidation("<%= ICostantiBeneficio.CAMPO_IMPORTO_AMMENDA%>DEC","numeric","Il campo Ammenda Arresto può contenere solo caratteri numerici");

  	frmvalidator.setAddnlValidationFunction("verify");

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

   </script>
</body>
</html>
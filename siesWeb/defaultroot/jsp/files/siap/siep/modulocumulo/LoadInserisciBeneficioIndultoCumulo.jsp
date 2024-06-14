<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiBeneficiCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.BeneficioCumuloModel" %>

<%@ page import="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaAccessoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.PenaComplessivaCumuloModel"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"     scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"        scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="tipoFormBeneficio" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="listaDPR" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoBeneficio" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="sottotipoBeneficio" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"          	scope="request" class="java.lang.String"/>
<jsp:useBean id="beneficioCumulo"      	scope="request" class="siap.siep.modulocumulo.model.BeneficioCumuloModel"/>
<jsp:useBean id="penacomplessiva"     	scope="request" class="siap.siep.modulocumulo.model.PenaComplessivaCumuloModel"/>
<jsp:useBean id="peneaccessorie"      	scope="request" class="java.util.Vector"/>

<%
// if(penacomplessiva == null)
// 	   penacomplessiva = new PenaComplessivaCumuloModel();

// if(beneficioCumulo == null)
// 	beneficioCumulo = new BeneficioCumuloModel();

String lSTipo="";
if(modalita.compareTo("M") == 0 )
	lSTipo = beneficioCumulo.getCodSottotipoBeneficio();
 
// MULTA
String lParteInteraMulta = "";
String lParteDecimaleMulta = "";
int lIndexMulta = StringUtils.toStringJSP(penacomplessiva.getImportoMulta()).indexOf(".");
if(lIndexMulta == -1)
{
	lParteInteraMulta = StringUtils.toStringJSP(penacomplessiva.getImportoMulta());
    lParteDecimaleMulta = "";
}
else
{
	lParteInteraMulta = StringUtils.toStringJSP(penacomplessiva.getImportoMulta()).substring(0,lIndexMulta);
    lParteDecimaleMulta = StringUtils.toStringJSP(penacomplessiva.getImportoMulta()).substring(lIndexMulta+1);
}
         
String lParteInteraAmmenda = "";
String lParteDecimaleAmmenda = "";
int lIndexAmmenda = StringUtils.toStringJSP(penacomplessiva.getImportoAmmenda()).indexOf(".");
if(lIndexAmmenda == -1)
{
    lParteInteraAmmenda = StringUtils.toStringJSP(penacomplessiva.getImportoAmmenda());
    lParteDecimaleAmmenda = "";
}
else
{
  	lParteInteraAmmenda = StringUtils.toStringJSP(penacomplessiva.getImportoAmmenda()).substring(0,lIndexAmmenda);
  	lParteDecimaleAmmenda = StringUtils.toStringJSP(penacomplessiva.getImportoAmmenda()).substring(lIndexAmmenda+1);
}

String lFunzione="";
if(modalita.equals("M") )
{
	lFunzione = "Modifica Beneficio Indulto in Sentenza";
}
else if(modalita.equals("I") )
{      
	lFunzione = "Inserimento Beneficio Indulto in Sentenza";
}      
%>

<!-- 		LoadInserisciBeneficioIndultoCumulo			 -->
<html>
<head>
<title>[S.I.E.S.] - Gestione Cumulo - Beneficio Indulto/Amnistia in sentenza </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>" ></script>
<script language="JavaScript">

  var lPrima = true;
  var modifica = '<%=modalita%>';
  var SottoTipoBeneficioinModifica = '<%=lSTipo%>';
  
 function CaricaPena()
 {
   	//gestione div dipendete dalla voce selezionata nella combo sotto tipo beneficio
    var nodeelenco = document.getElementById('elencopeneaccessorie'); 
    if (document.BeneficiIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.value == '08')  // Condono pene Accessorie
    {	 
    	nodeelenco.style.display='block';
    }
    else	
    {
      	nodeelenco.style.display='none';    
    }

   	//caricamento della reclusione e arresto dipendente dalla voce selezionata nella combo sotto tipo beneficio e dal tipo di Operazione (I o M)
   	if (document.BeneficiIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.value == '07'		// Condono Pena Principale
       || document.BeneficiIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.value == '09'	//   =	=	=	=  + pena sostitutiva
       || document.BeneficiIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.value == '10')	//  = 	= 	=	+ inapplicabilita di mis.sic.
   {  
   		if(modifica=="I")
   		{	
	    	document.BeneficiIndultoCumulo.AArr.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumAnniArresto(),"0")%>";
	    	document.BeneficiIndultoCumulo.MArr.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumMesiArresto(),"0")%>";
	    	document.BeneficiIndultoCumulo.GArr.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumGiorniArresto(),"0")%>";
	    	document.BeneficiIndultoCumulo.Ammenda.value = "<%=lParteInteraAmmenda%>";
	    	document.BeneficiIndultoCumulo.Amm_dec.value = "<%=lParteDecimaleAmmenda%>";
	   
	    	document.BeneficiIndultoCumulo.ARec.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumAnniReclusione(),"0")%>";
	    	document.BeneficiIndultoCumulo.MRec.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumMesiReclusione(),"0")%>";
	    	document.BeneficiIndultoCumulo.GRec.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumGiorniReclusione(),"0")%>";
	    	document.BeneficiIndultoCumulo.Multa.value = "<%=lParteInteraMulta%>";
	    	document.BeneficiIndultoCumulo.Mul_dec.value = "<%=lParteDecimaleMulta%>"; 
   		}
   		else if(modifica=="M")
   		{
   			if(document.BeneficiIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.value == SottoTipoBeneficioinModifica)
   			{
   				document.BeneficiIndultoCumulo.AArr.value = "<%=StringUtils.toStringJSP(beneficioCumulo.getNumAnniArresto(),"0")%>";
   		    	document.BeneficiIndultoCumulo.MArr.value = "<%=StringUtils.toStringJSP(beneficioCumulo.getNumMesiArresto(),"0")%>";
   		    	document.BeneficiIndultoCumulo.GArr.value = "<%=StringUtils.toStringJSP(beneficioCumulo.getNumGiorniArresto(),"0")%>";
   		    	document.BeneficiIndultoCumulo.Ammenda.value = "<%=StringUtils.getParteIntera(beneficioCumulo.getImportoAmmenda()) %>";
   		    	document.BeneficiIndultoCumulo.Amm_dec.value = "<%=StringUtils.getParteDecimale(beneficioCumulo.getImportoAmmenda()) %>";
   		   
   		    	document.BeneficiIndultoCumulo.ARec.value = "<%=StringUtils.toStringJSP(beneficioCumulo.getNumAnniReclusione(),"0")%>";
   		    	document.BeneficiIndultoCumulo.MRec.value = "<%=StringUtils.toStringJSP(beneficioCumulo.getNumMesiReclusione(),"0")%>";
   		    	document.BeneficiIndultoCumulo.GRec.value = "<%=StringUtils.toStringJSP(beneficioCumulo.getNumGiorniReclusione(),"0")%>";
   		    	document.BeneficiIndultoCumulo.Multa.value = "<%=StringUtils.getParteIntera(beneficioCumulo.getImportoMulta()) %>";
   		    	document.BeneficiIndultoCumulo.Mul_dec.value = "<%=StringUtils.getParteDecimale(beneficioCumulo.getImportoMulta()) %>";

   			}
   			else
   			{
   				document.BeneficiIndultoCumulo.AArr.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumAnniArresto(),"0")%>";
   		    	document.BeneficiIndultoCumulo.MArr.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumMesiArresto(),"0")%>";
   		    	document.BeneficiIndultoCumulo.GArr.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumGiorniArresto(),"0")%>";
   		    	document.BeneficiIndultoCumulo.Ammenda.value = "<%=lParteInteraAmmenda%>";
   		    	document.BeneficiIndultoCumulo.Amm_dec.value = "<%=lParteDecimaleAmmenda%>";
   		   
   		    	document.BeneficiIndultoCumulo.ARec.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumAnniReclusione(),"0")%>";
   		    	document.BeneficiIndultoCumulo.MRec.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumMesiReclusione(),"0")%>";
   		    	document.BeneficiIndultoCumulo.GRec.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumGiorniReclusione(),"0")%>";
   		    	document.BeneficiIndultoCumulo.Multa.value = "<%=lParteInteraMulta%>";
   		    	document.BeneficiIndultoCumulo.Mul_dec.value = "<%=lParteDecimaleMulta%>";	
   			}	
   		}	

   	} 
   	else if(document.BeneficiIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.value == '08')
   	{
   		if(modifica=="I")
   		{	
		    document.BeneficiIndultoCumulo.AArr.value = "";
		    document.BeneficiIndultoCumulo.MArr.value = "";
		    document.BeneficiIndultoCumulo.GArr.value = "";
		    document.BeneficiIndultoCumulo.Ammenda.value = "";
		    document.BeneficiIndultoCumulo.Amm_dec.value = "";
		   
		    document.BeneficiIndultoCumulo.ARec.value = "";
		    document.BeneficiIndultoCumulo.MRec.value = "";
		    document.BeneficiIndultoCumulo.GRec.value = "";
		    document.BeneficiIndultoCumulo.Multa.value = "";
		    document.BeneficiIndultoCumulo.Mul_dec.value = "";
   		}
   		else if(modifica=="M")
   		{
   			document.BeneficiIndultoCumulo.AArr.value = "<%=StringUtils.toStringJSP(beneficioCumulo.getNumAnniArresto(),"0")%>";
	    	document.BeneficiIndultoCumulo.MArr.value = "<%=StringUtils.toStringJSP(beneficioCumulo.getNumMesiArresto(),"0")%>";
		    document.BeneficiIndultoCumulo.GArr.value = "<%=StringUtils.toStringJSP(beneficioCumulo.getNumGiorniArresto(),"0")%>";
		    document.BeneficiIndultoCumulo.Ammenda.value = "<%=StringUtils.getParteIntera(beneficioCumulo.getImportoAmmenda()) %>";
		    document.BeneficiIndultoCumulo.Amm_dec.value = "<%=StringUtils.getParteDecimale(beneficioCumulo.getImportoAmmenda()) %>";
		   
		    document.BeneficiIndultoCumulo.ARec.value = "<%=StringUtils.toStringJSP(beneficioCumulo.getNumAnniReclusione(),"0")%>";
		    document.BeneficiIndultoCumulo.MRec.value = "<%=StringUtils.toStringJSP(beneficioCumulo.getNumMesiReclusione(),"0")%>";
		    document.BeneficiIndultoCumulo.GRec.value = "<%=StringUtils.toStringJSP(beneficioCumulo.getNumGiorniReclusione(),"0")%>";
		    document.BeneficiIndultoCumulo.Multa.value = "<%=StringUtils.getParteIntera(beneficioCumulo.getImportoMulta()) %>";
		    document.BeneficiIndultoCumulo.Mul_dec.value = "<%=StringUtils.getParteDecimale(beneficioCumulo.getImportoMulta()) %>";
   		}	
   	}
   	else if(document.BeneficiIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.value == '-')
   	{
   		document.BeneficiIndultoCumulo.AArr.value = "";
	    document.BeneficiIndultoCumulo.MArr.value = "";
	    document.BeneficiIndultoCumulo.GArr.value = "";
	    document.BeneficiIndultoCumulo.Ammenda.value = "";
	    document.BeneficiIndultoCumulo.Amm_dec.value = "";
	   
	    document.BeneficiIndultoCumulo.ARec.value = "";
	    document.BeneficiIndultoCumulo.MRec.value = "";
	    document.BeneficiIndultoCumulo.GRec.value = "";
	    document.BeneficiIndultoCumulo.Multa.value = "";
	    document.BeneficiIndultoCumulo.Mul_dec.value = "";
   	}
   
   	lPrima = false;                       
 }
 
 //==========================================================================
 // Ritorna alla lista delle Misure di Sicurezza per il Titolo
 //==========================================================================
 function eseguiFunzione(action)
 {
   document.BeneficiIndultoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
   document.BeneficiIndultoCumulo.submit();
 }

 </script>
</head>
<body class="corpo" onload="CaricaPena();">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
     				  <font class="campo"><%=lFunzione%></font>
	  </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaBeneficiCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
	</tr>
  </table>

  <% // INCLUDE DEL DETTAGLIO DEL TITOLO e DELL'ISTRUTTORIA%>
<br>
 <table align="center" width="95%" style="border: 0" cellspacing="1" cellpadding="1">
	<tr>
		<td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      	</td>
    </tr>  
  	<tr>
  	  	<td>	
    	<jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
      	</td>
    </tr>  	
</table>
<br>

    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="BeneficiIndultoCumulo">
     <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciBeneficiCumulo">
     <input type="HIDDEN" name="<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO%>" value="<%=beneficioCumulo.getIdBeneficioCumulo()%>">
     
     <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  	 <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  	 <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_FLAG_STATO %>"          value="<%=StringUtils.toStringJSP(beneficioCumulo.getFlagStato()) %>">
  	 <input type="hidden" name="<%=ICostantiModuloCumulo.MODALITA %>" value="<%=modalita%>">
  	 <input type="hidden" name="<%=ICostantiBeneficiCumulo.TIPO_FORM_BENEFICIO %>" value="<%=tipoFormBeneficio%>">
        

<table width=90%>
	<tr>
    	<td class="l">Tipo Beneficio</td>
        <td class="l">
        <select name="<%=ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO %>" >
        <%=tipoBeneficio%>
        </select>
        </td>        
        <td class="l">Provvedimento di Concessione</td>
        <td class="l">
        <select name="<%=ICostantiBeneficiCumulo.CAMPO_COD_DPR%>">
        <%=listaDPR%>
        </select>
        </td>
	</tr>
    <tr>
        <td class="c" colspan=2>Applicazione del beneficio</td>
        <td class="l" colspan=2>
          <select name="<%=ICostantiBeneficiCumulo.CAMPO_COD_SOTTOTIPO_BENEFICIO%>" onchange="CaricaPena();">
           <%=sottotipoBeneficio%>
          </select>
        </td>   
    </tr>     
    <tr><td>&nbsp;</td></tr>
 </table>
  
 <table width=90%>         
  <tr>
    <td class="titolo" colspan=2>Reclusione</td>
    <td width="25">&nbsp;</td>
    <td class="titolo" colspan=2>Arresto</td>
  </tr>
  
	  
  <tr>
    <td class="c">
      <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font class="label">Giorni</font><br>
      <input type="text" name="ARec" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(beneficioCumulo.getNumAnniReclusione()) %>">&nbsp;
      <input type="text" name="MRec" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(beneficioCumulo.getNumMesiReclusione()) %>">&nbsp;
      <input type="text" name="GRec" maxlength="4" size="4" value="<%=StringUtils.toStringJSP(beneficioCumulo.getNumGiorniReclusione()) %>">
    </td>
    <td class="c">
      <font  class="label">Multa</font><br>
      <input style="align:right" type="text" name="Multa" maxlength="7" size="7" value="<%=StringUtils.getParteIntera(beneficioCumulo.getImportoMulta()) %>">
      ,
      <input style="align:right" type="text" name="Mul_dec" maxlength="2" size="2" value="<%=StringUtils.getParteDecimale(beneficioCumulo.getImportoMulta()) %>">
    </td>
    <td width="25">&nbsp;</td>
    <td class=c>
      <font  class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font  class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font  class="label">Giorni</font><br>
      <input type="text" name="AArr" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(beneficioCumulo.getNumAnniArresto()) %>">&nbsp;
      <input type="text" name="MArr" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(beneficioCumulo.getNumMesiArresto()) %>">&nbsp;
      <input type="text" name="GArr" maxlength="4" size="4" value="<%=StringUtils.toStringJSP(beneficioCumulo.getNumGiorniArresto()) %>">

    </td>
    <td class="c">
      <font  class="label">Ammenda</font><br>
      <input style="align:right" type="text" name="Ammenda" maxlength="7" size="7" value="<%=StringUtils.getParteIntera(beneficioCumulo.getImportoAmmenda()) %>">
      ,
      <input style="align:right" type="text" name="Amm_dec" maxlength="2" size="2" value="<%=StringUtils.getParteDecimale(beneficioCumulo.getImportoAmmenda()) %>">
    </td>
  </tr>

 </table> 
 <table width=90%>    
	 <tr>
        <td class="c" colspan ='2'>Note</td>
        <td class="l" colspan ='2'>
             <textarea cols="50" rows=3  name="<%= ICostantiBeneficiCumulo.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(beneficioCumulo.getNote())%></textarea>
        </td>
      </tr>        
     
</table>
<br>
<div id="elencopeneaccessorie" style="width: 100%; display:none; position:relative;" > 
 <table width=90%>
    <tr>
      <td class="int">Tipo Pena Accessoria</td>
      <td class="int">Durata Pena</td>
      <td class="int">Azioni</td>
    </tr>
<%
    if( peneaccessorie != null && !peneaccessorie.isEmpty() )
    {
      	Iterator itx = peneaccessorie.iterator();
      	for (int i = 0; itx.hasNext(); i++)
      	{
    		PenaAccessoriaCumuloModel lPenAcMod = (PenaAccessoriaCumuloModel)itx.next();
%>
        <tr>
          <td class="c">
            <%=StringUtils.toStringJSP(lPenAcMod.getDescrTipoPenaAccessoria(), "-")%>
          </td>
          <td class="l">
<%			if(lPenAcMod.getDurata() != null && !lPenAcMod.getDurata().equals("-")) 
			{	%>          
            	<%=StringUtils.toStringJSP(lPenAcMod.getDescrDurata(),"-")%> 
<%			}
			else
			{ %>   
				Anni <font class="campo"><%=StringUtils.toStringJSP(lPenAcMod.getNumAnni(),"0") %></font>&nbsp;
				Mesi <font class="campo"><%=StringUtils.toStringJSP(lPenAcMod.getNumMesi(),"0") %></font>&nbsp;
				Giorni <font class="campo"><%=StringUtils.toStringJSP(lPenAcMod.getNumGiorni(),"0") %></font>
<%			} %>         
          </td>

          <td class="c">
 <%		if( modalita.equals("M") ) 
    	{  
    		if(lPenAcMod.getBenIdBeneficioCumulo() != null && lPenAcMod.getBenIdBeneficioCumulo().compareTo(beneficioCumulo.getIdBeneficioCumulo() )==0 )
    		{  %>       
	 			<input type="checkbox" checked name="<%=ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO %>" value="<%=lPenAcMod.getIdPenaAccessoriaCumulo()%>">
	 	<%	}
    		else
    		{
    			if(lPenAcMod.getBenIdBeneficioCumulo()!=null)
    			{ %>
       				<input type="checkbox" disabled name="<%=ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO %>" value="<%=lPenAcMod.getIdPenaAccessoriaCumulo()%>">
       				<font class="crosso">(già condonata con altro Beneficio)</font>
    		<% 	}
    			else
    			{	%>
     				<input type="checkbox" name="<%=ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO %>" value="<%=lPenAcMod.getIdPenaAccessoriaCumulo()%>">	        
<%  			}
			}
		}
 		else if( modalita.equals("I"))
 		{
 			if(lPenAcMod.getBenIdBeneficioCumulo()!=null)
 			{ %>
 	 			<input type="checkbox"  disabled name="<%=ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO %>" value="<%=lPenAcMod.getIdPenaAccessoriaCumulo()%>">
  				<font class="crosso">(già condonata con altro Beneficio)</font>
 <% 		}
 			else
 			{  %>
 				<input type="checkbox" checked name="<%=ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO %>" value="<%=lPenAcMod.getIdPenaAccessoriaCumulo()%>">
 <% 		}
 		}%> 
          </td>
        </tr>
<%
      }
    }
%>
 </table>  
</div>

 <br>

<table cellspacing="2" cellpadding="2" width="90%">

    <%
    //==========================================================================
    // Descrizione dello stato visualizzata solo in fase di modifica del dato
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
        <textarea cols="100" rows="6" name="<%=ICostantiBeneficiCumulo.CAMPO_MOTIVO_MODIFICA%>">
        	<%=StringUtils.toStringJSP(beneficioCumulo.getMotivoModifica()) %>
        </textarea>
      </td>
    </tr> 
<%	if(modalita.equals("I") )
	{	%>     
	    <tr>
	      <td align="left">
	        <input class="bottone" type="submit" name="conferma" title="Inserisci Beneficio" value="Conferma">
	      </td>
	    </tr>
<%	}
	else if(modalita.equals("M") )
	{	%>
	    <tr>
	      <td align="left">
	        <input class="bottone" type="submit" name="conferma" title="Modifica beneficio" value="Conferma">
	      </td>
	    </tr>		    
<%	} %>   
 </table>
</form>


 <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("BeneficiIndultoCumulo")
           
       frmvalidator.addValidation("ARec","numeric","Il campo Anni Reclusione è numerico!");
       frmvalidator.addValidation("MRec","numeric","Il campo Mesi Reclusione è numerico!");
       frmvalidator.addValidation("GRec","numeric","Il campo Giorni Reclusione è numerico!");
       frmvalidator.addValidation("Multa","numeric","Il campo Multa è numerico!");
       frmvalidator.addValidation("Mul_dec","numeric","Il campo Multa è numerico!");  
   
       frmvalidator.addValidation("AArr","numeric","Il campo Mesi Arresto è numerico!");
       frmvalidator.addValidation("MArr","numeric","Il campo Giorni Arresto è numerico!");
       frmvalidator.addValidation("GArr","numeric","Il campo Ore Arresto è numerico!"); 
       frmvalidator.addValidation("Ammenda","numeric","Il campo Multa è numerico!");
       frmvalidator.addValidation("Amm_dec","numeric","Il campo Multa è numerico!");       
            
   </script>
</body>
</html>
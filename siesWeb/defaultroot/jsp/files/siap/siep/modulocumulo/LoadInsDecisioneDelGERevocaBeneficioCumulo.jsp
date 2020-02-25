<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>
<%@page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.BeneficioCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiesteInviateCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="RichiestaGE"	       scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="TitoloRichiesta"      scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="TitoloRiferimento"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="articolo"		       scope="request" class="java.lang.String"/>
<jsp:useBean id="motivazione"		   scope="request" class="java.lang.String"/>
<jsp:useBean id="UfficioEmittente"	   scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"		   	   scope="request" class="java.lang.String"/>

<!-- 						LoadInsDecisioneDelGERevocaBeneficioCumulo						 -->
<% 
//============================================================================== 
// Form per l'Inserimento/Modifica della Decisione del G.E. su richieste di 
// Revoca Benefici (amnistia/Indulto/Sospensione/Non Menzione)
//============================================================================== 

// Costruzione del Model per Le Eventuali Richieste Unviate
RichiesteInviateCumModel lRicInv = null;
if(RichiestaGE!=null && RichiestaGE.getRichiesteInviateCum()!=null)
{
	lRicInv = (RichiesteInviateCumModel)RichiestaGE.getRichiesteInviateCum();
}

//Costruzione dei Model per le descrizioni dei Benefici da Revocare
BeneficioCumuloModel lBenMod1 = null;
BeneficioCumuloModel lBenMod2 = null;
String lBenCod1="";
String lBenCod2="";
boolean isConProvvedimento = false; 

if(TitoloRichiesta.getBeneficiCumulo()!=null && TitoloRichiesta.getBeneficiCumulo().size()>0)
{	
	List ListaBen = TitoloRichiesta.getBeneficiCumulo();
	lBenMod1 = (BeneficioCumuloModel)ListaBen.get(0);
	lBenCod1 = lBenMod1.getCodTipoBeneficio();

	if(ListaBen.size() > 1)
	{
		lBenMod2 = (BeneficioCumuloModel)ListaBen.get(1);
		lBenCod2 = lBenMod2.getCodTipoBeneficio();
	}
}

//Preparo i model con i Benefici concessi con Provvedimento
StatoEsecTitoloCumulatoModel lStatEsecMod = null;
ComputiCumuloModel lCompMod = null;
String lCodProv = "";

if(TitoloRichiesta.getStatoEsecTitoloCumulato()!=null)
{
	isConProvvedimento = true;
	lStatEsecMod = (StatoEsecTitoloCumulatoModel) TitoloRichiesta.getStatoEsecTitoloCumulato();
	
	if(lStatEsecMod!=null && lStatEsecMod.getIdStatoEsecTitoloCumulato()!=null) {
		if(lStatEsecMod.getListaComputi()!=null && lStatEsecMod.getListaComputi().size()>0) 
		{
			lCompMod = (ComputiCumuloModel) lStatEsecMod.getListaComputi().get(0);
			lBenCod1 = lCompMod.getCodTipoAnnotazione();
		}
	}

}

// Costruzione del Model per L'eventuale Decisione del GE (in caso di Modifica Decisione del GE)
ProvvedimentoGeSorvCumModel lDecisioneGE = new ProvvedimentoGeSorvCumModel();
if("M".equals(modalita) && RichiestaGE.getDecisioneGeSorvCum() != null )
{
	lDecisioneGE = RichiestaGE.getDecisioneGeSorvCum();
}

// Data Richiesta al GE
String lDataRich = StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaGE.getDataEmissione(),"dd/MM/yyyy"));

%>

<html>
<head>
  <title> Gestione Richieste al Ge - Revoca Benefici - Decisione del G.E.</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" >

     // Torna indietro alla Griglia decisioni del GE
    function tornaIndietro(action)
    {
      document.indietroForm.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.indietroForm.submit();
    }
    
  	//Apre la finestra con la Lista delle Sedi uffici in base alla tipologia di Ufficio Selezionata
   	function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
   	{
     	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
   	}
  	
  	function Verify() 
    { 
     	/* Anno e Numero Procedimento SIGE */
          if(document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.value == '' && 
          	 document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.value != ''	 )
          {
          		alert('Anno Procedimento G.E. NON valido');
              document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.focus();
              return false;
          } 
           
          if(document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.value != '' && 
          	 document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.value == ''	 )
          {
          	alert('Numero Procedimento G.E. NON valido');
              document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.focus();
              return false;
          }
           
          if(document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.value == '' && 
             document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.value == ''	 )
          {
    			alert('digitare Anno e Numero Procedimento G.E.');
              document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.focus();
              return false;
          }

   	   // Data Emissione Procedimento
         if (document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value.length==1)
           	document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value='0'+document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value;
         if (document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value.length==1)
           	document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value='0'+document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value;

         var data_to_verify = document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value+'/'+document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value+'/'+document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE_D%>.value;
         if (data_to_verify=='//' )
         {
             alert('Indicare la Data Emissione Procedimento');
             document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.focus();
             return false;
         }
         
         if (!ControllaData(data_to_verify) )
         {
             alert('Data Emissione Procedimento NON valida');
             document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.focus();
             return false;
         }
         
      	 // Data Emissione Procedimento SIGE deve essere <= Data del Giorno
         var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
    	 if(!CompareDate(data_to_verify, data_od))
    	 {
    		alert('Data Emissione Procedimento SIGE \nNON può essere superiore alla Data Odierna');
    		document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D %>.focus();
          	return false;
    	 }
         
    	 // Data della Richiesta deve essere <= Data Emissione Procedimento SIGE 
    	 var data_Ric = '<%=lDataRich%>';
    	 if(!CompareDate(data_Ric, data_to_verify))
   	 	 {
   			alert('Data Emissione Procedimento SIGE DEVE essere superiore o uguale \nalla Data Richiesta del PM al G.E.');
   			document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D %>.focus();
         	return false;
   	 	 }
    	 
         // Tipo Ufficio e sede Ufficio Emittente
         if(document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.value == '-' || 
        	 document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.value.length == 1	 )
         {
        	 	alert('Ufficio Emittente non valido');
            	document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.focus();
            	return false;
         } 
         
         if(document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.value == '-' || 
        	 document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.value.length == 1 ||
        	 document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.value.length == 0 )
         {
        	 	alert('Sede Ufficio Emittente non valida');
            	document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
            	return false;
         }
         
         
       // Controlli su check beneficio e quantum
       var jsBenCod1 = '<%=lBenCod1%>';
       var jsBenCod2 = '<%=lBenCod2%>';
       
     var tipoDec = $('input[name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>"]:checked').val();
     if(tipoDec == "C" || tipoDec == "D")
     {
       if(jsBenCod2 == '' )
       {
         // Caso di Amnistia / Indulto
         if(jsBenCod1 == '03' || jsBenCod1 == '04' || jsBenCod1 == '002' || jsBenCod1 == '003')
         { 
           // check Obbligatorio sull'unico beneficio presente
           if(!document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_REVOCA_BEN_1%>.checked  )
           {
              document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_REVOCA_BEN_1%>.checked = true;
              //alert('guarda il check');
           }   
           
           // Quantum
           var pos = document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_D%>INT.value.indexOf('.');
           if(pos > 0)
           {
             alert('inserire correttamente il valore INTERO della Multa');
             document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_D%>INT.focus();
             return false;
           }
        
            pos = document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_D%>INT.value.indexOf('.');
            if(pos > 0)
            {
              alert('inserire correttamente il valore INTERO della Ammenda');
              document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_D%>INT.focus();
              return false;
            }
             
            if(document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_D%>.value == ''  )
            {
              alert('digitare il segno + / - quantum pena');
              document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_D%>.focus();
              return false;
            }
             
             if( ( document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_D%>.value == '' ||
                 document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_D%>.value.length == 0 )  &&
               ( document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_D%>.value == '' ||
                   document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_D%>.value.length == 0  ) &&  
                 ( document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_D%>.value == '' ||
                   document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_D%>.value.length == 0 ) &&
                   
                 ( document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_D%>.value == '' ||
                   document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_D%>.value.length == 0 ) &&
                 ( document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_D%>.value == '' ||
                   document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_D%>.value.length == 0 ) &&
                 ( document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_D%>.value == '' ||
                   document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_D%>.value.length == 0 ) &&  
                   
                 ( document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_D%>INT.value.length == 0 ) &&
                 ( document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_D%>DEC.value.length == 0 ) &&  
                 ( document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_D%>INT.value.length == 0 ) &&
                 ( document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_D%>DEC.value.length == 0 ) 
               )  
             {
                alert('digitare una quantità di pena');
                document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_D%>.focus();
                return false;
             }
           }
       }
       else
       {
         if(!document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_REVOCA_BEN_1%>.checked  &&
            !document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_REVOCA_BEN_2%>.checked  )
         {
           alert('Selezionare con un check il Beneficio che interessa \nSospensione Condizionale o Non Menzione o entrambi');
           document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_REVOCA_BEN_1%>.focus();
           return false;
         }
       }
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
 <%	if("I".equals(modalita))
 	{ %>       
        <font class="campo">Inserimento Decisione del G.E. su Richiesta Revoca Benefici&nbsp;</font>
<%	}
 	else if("M".equals(modalita))
 	{	%>   
 		<font class="campo">Modifica Decisione del G.E. su Richiesta Revoca Benefici&nbsp;</font>
<%	} %> 	     
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
      	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  		<br>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="indietroForm">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
</form> 

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInsDecGERevoca">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciDecisioneDelGECumulo">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="<%=RichiestaGE.getIdRichiestePmInCumulo()%>">
  <input type="hidden" name="modalita" value="<%=modalita%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PROVVEDIMENTO %>" value="03" >

<!-- 								DATI SUI BENEFICI DA REVOCARE									 -->
<table width="95%" align="center">
    <tr><td colspan="2" class="Titolonocap" style="text-align:left" >Beneficio da Revocare:</td></tr>
</table> 
<%
if(!lBenCod2.equals(""))
{
	// Sospensione		%>

	<table width="95%" align="center" id="tabBen_S">
      <tr>
        <td class="l" width="20%">Tipo Beneficio</td>
        <td class="l" ><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrTipoBeneficio(),"") %></font></td>
      </tr>
      <tr>   
        <td class="l">Natura Beneficio</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrSottotipoBeneficio(), "") %></font></td>
      </tr>
      <tr>  
        <td class="l">Durata sospensione</td>
        <td class="l"><font class="campo">Anni:&nbsp;<%=StringUtils.toStringJSP(lBenMod1.getNumAnniSospensione(), "-" ) %></font></td>
      </tr>
	</table>

<%
//  Non Menzione
%>
	<table width="95%" align="center" id="tabBen_NM">
	  <tr>
	  	<td class="l" width="20%">Tipo Beneficio</td>
        <td class="l" ><font class="campo"><%=StringUtils.toStringJSP(lBenMod2.getDescrTipoBeneficio(),"") %></font></td>
	    <%--  
	  	<td class="l" width="150px"><font class="campo"><%=StringUtils.toStringJSP(lBenMod2.getDescrTipoBeneficio(),"") %></font></td>
	  	<td class="l" colspan="2"><img src="/images/V.gif"> </td>
	  	--%>
	  </tr>
	</table>
<%	 
 }	
 else
 {
 	if(lBenCod1.equals("01") )
	{
	// Sospensione		%>
	<table width="95%" align="center" id="tabBen_S">
      <tr>
        <td class="l" width="20%">Tipo Beneficio</td>
        <td class="l" ><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrTipoBeneficio(),"") %></font></td>
      </tr>
      <tr>   
        <td class="l">Natura Beneficio</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrSottotipoBeneficio(), "") %></font></td>
      </tr>
      <tr>  
        <td class="l">Durata sospensione</td>
        <td class="l"><font class="campo">Anni:&nbsp;<%=StringUtils.toStringJSP(lBenMod1.getNumAnniSospensione(), "-" ) %></font></td>
      </tr>
	</table>
<%	}
 	else if(lBenCod1.equals("02") )
 	{	
 		// Non Menzione	%>
 	<table width="95%" align="center" id="tabBen_NM">
	  <tr>
	  	<td class="l" width="20%">Tipo Beneficio</td>
        <td class="l" ><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrTipoBeneficio(),"") %></font></td>
	    <%-- 
	  	<td class="l" width="150px"><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrTipoBeneficio(),"") %></font></td>
	  	<td class="l" colspan="2"><img src="/images/V.gif"> </td>
	  	--%>
	  </tr>
	</table>
<%	}
 	else if(lBenCod1.equals("03") || lBenCod1.equals("04") || lBenCod1.equals("002") || lBenCod1.equals("003")  )
 	{	
 		// Amnistia/ Indulto dati con provvedimento
 	    if(isConProvvedimento) {  %>

	<table width="95%" align="center" id="tabBen_I" >
      <tr>
        <td class="l" width="20%" >Tipo Beneficio</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lCompMod.getDescrTipoAnnotazione(),"") %> </font></td>
      </tr>
      <tr>  
        <td class="l">Provvedimento di Concessione</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lCompMod.getDescDpr() ,"") %></font></td>
      </tr>
      <tr>  
        <td class="l">Concesso Nella misura di</td>
   		<td class="l">
   		
<%  	if( !lCompMod.isQuantumReclusioneZero() ){ %>     
			<font class="campo">Reclusione&nbsp;&nbsp;Anni: <%=StringUtils.toStringJSP(lCompMod.getNumAnniReclusione(), "0") %>&nbsp;
        											mesi: <%=StringUtils.toStringJSP(lCompMod.getNumMesiReclusione(), "0") %>&nbsp;
        											Giorni: <%=StringUtils.toStringJSP(lCompMod.getNumGiorniReclusione(), "0") %>&nbsp;
	        </font>&nbsp;											
<%		}	
	
		if( lCompMod.getImportoMulta() != null && lCompMod.getImportoMulta().compareTo(BigDecimal.ZERO) > 0  ) {
%>        															  
			<font class="campoLow">Multa:&nbsp;<%=StringUtils.toStringJSP(lCompMod.getImportoMulta(), "-") %>&nbsp;&euro;</font>				        
<%		}	%>
    

<% 		if ( !lCompMod.isQuantumArrestoZero()) {			%>     
			<font class="campoLow">Arresto&nbsp;&nbsp;Anni: <%=StringUtils.toStringJSP(lCompMod.getNumAnniArresto(), "0") %>&nbsp;
	        										mesi: <%=StringUtils.toStringJSP(lCompMod.getNumMesiArresto(), "0") %>&nbsp;
	        										Giorni: <%=StringUtils.toStringJSP(lCompMod.getNumGiorniArresto(), "0") %>&nbsp;
	        </font>&nbsp;											
<%		}

		if( lCompMod.getImportoAmmenda() != null && lCompMod.getImportoAmmenda().compareTo(BigDecimal.ZERO) > 0 )
		{
%>        															  
			<font class="campoLow">Ammenda:&nbsp;<%=StringUtils.toStringJSP(lCompMod.getImportoAmmenda(), "-") %>&nbsp;&euro;</font>				        
<%		}	%> 

		</td>
      </tr>
    </table> 		
 		
 		
<%  	} else {  // Amnistia/ Indulto dati in Sentenza 	 %>

    <table width="95%" align="center" id="tabBen_I">
      <tr>
        <td class="l" width="20%" >Tipo Beneficio</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrTipoBeneficio(),"") %> </font></td>
      </tr>
      <tr>  
        <td class="l">Provvedimento di Concessione</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrDpr() ,"") %></font></td>
      </tr>
      <tr>  
        <td class="l">Concesso Nella misura di</td>
   		<td class="l">
   		
<% 		if( !lBenMod1.isQuantumReclusioneZero() ){				%>     
			<font class="campo">Reclusione&nbsp;&nbsp;Anni: <%=StringUtils.toStringJSP(lBenMod1.getNumAnniReclusione(), "0") %>&nbsp;
        											mesi: <%=StringUtils.toStringJSP(lBenMod1.getNumMesiReclusione(), "0") %>&nbsp;
        											Giorni: <%=StringUtils.toStringJSP(lBenMod1.getNumGiorniReclusione(), "0") %>&nbsp;
	        </font>&nbsp;											
<%		}

	
		if( lBenMod1.getImportoMulta() != null && lBenMod1.getImportoMulta().compareTo(BigDecimal.ZERO) > 0  )
		{
%>        															  
			<font class="campoLow">Multa:&nbsp;<%=StringUtils.toStringJSP(lBenMod1.getImportoMulta(), "-") %>&nbsp;&euro;</font>				        
<%		}	%>
    

<% 		if ( !lBenMod1.isQuantumArrestoZero()) {  %>     
			<font class="campoLow">Arresto&nbsp;&nbsp;Anni: <%=StringUtils.toStringJSP(lBenMod1.getNumAnniArresto(), "0") %>&nbsp;
	        										mesi: <%=StringUtils.toStringJSP(lBenMod1.getNumMesiArresto(), "0") %>&nbsp;
	        										Giorni: <%=StringUtils.toStringJSP(lBenMod1.getNumGiorniArresto(), "0") %>&nbsp;
	        </font>&nbsp;											
<%		}

		if( lBenMod1.getImportoAmmenda() != null && lBenMod1.getImportoAmmenda().compareTo(BigDecimal.ZERO) > 0 )
		{
%>        															  
			<font class="campoLow">Ammenda:&nbsp;<%=StringUtils.toStringJSP(lBenMod1.getImportoAmmenda(), "-") %>&nbsp;&euro;</font>				        
<%		}	%> 

		</td>
      </tr>
    </table>    		

<% 		} 		
	}
 }		%> 

<!-- 				TITOLO  SU  CUI  è  CONCESSO  IL  BENEFICIO  		 -->				
<% 	
  String AnnoNumero ="";
  AnnoNumero = TitoloRichiesta.getAnnoSentenza() +"/"+TitoloRichiesta.getNumeroSentenza();
 %> 
  <table width="95%" align="center">
    <tr><td colspan="8" class="Titolonocap" style="text-align:left" >Concesso sul Titolo </td></tr>
    <tr>
        <td class="l" colspan="8">
          <font class="label"><%=StringUtils.toStringJSP(TitoloRichiesta.getDescrTipoProvvedimento() )%>&nbsp;N. </font>
          <font class="campo"><%=AnnoNumero%></font>&nbsp;
          &nbsp;<font class="label"> del </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(TitoloRichiesta.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
          &nbsp;<font class="label"> Emessa da </font>
          <font class="campo"><%=StringUtils.toStringJSP(TitoloRichiesta.getDescrTipoAutoritaEmittente(), "") %></font>
          <font class="label">&nbsp;di&nbsp; </font>
          <font class="campo"><%=StringUtils.toStringJSP(TitoloRichiesta.getDescrLuogoEmittente(), "") %></font>

          &nbsp;<font class="label"> Irrevocabile il  </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(TitoloRichiesta.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
        </td>
      </tr>
  </table>

<!-- 									DATI 	DELLA 	RICHIESTA 	DI  REVOCA								 -->
  <br>
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap" >Dati della richiesta di Revoca</td></tr>
    <tr>
      <td class="l" width="200px">Data Richiesta</td>
      <td class="l" colspan="2">
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaGE.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>
      </td>	
    <tr>
      <td class="l" width="200px">Computo beneficio :</td>
      <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrTipoAnnotazione() )%></font>
      </td>
      
<%	if(RichiestaGE.getCodDpr()!=null )
	{ %>      
      <td class="l">DPR :</td>
      <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrDpr() )%></font>
      </td>
<%	}
	else
	{	%>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
<%	} %>			      
    </tr>

<% 	if(RichiestaGE.getFlagAppProvvisoria()!=null)
	{	%>    
      <tr>
      	<td class="l">Anticipazione degli effetti :</td>
 <%	  if("A".equals(RichiestaGE.getFlagAppProvvisoria() ))
 	  { %>     
      	<td class="l" colspan="3"><img src="/images/V.gif"> </td>
<%	  }
 	  else if("R".equals(RichiestaGE.getFlagAppProvvisoria() ))
 	  {  %>
 		<td class="l" colspan="3"><font class="label" style="color:red" > NO </font></td>
<% 	  }  %>

      </tr>
<%	} %>

<%
	if(!articolo.equals("") && !motivazione.equals("") )
	{	%>
	  <tr>
    	<td class="l" >Articolo</td>
    	<td class="l">
		  <font class="campo">
    		<%=articolo%>   
    		&nbsp;</font> 		
    	</td>
      </tr>
      <tr>
    	<td class="l">Motivazione</td>
    	<td class="l">
		  <font class="campo">
    		<%=motivazione%>   
    		&nbsp;</font> 		
    	</td>
      </tr>
<%	} %>

<%
if(lBenCod2.equals(""))
{
	if(lBenCod1.equals("03") || lBenCod1.equals("04") || lBenCod1.equals("002") || lBenCod1.equals("003") )
	{	%>

    <tr>
      <td class="l" colspan="1">Reclusione :  </td>
      <td class="l" colspan="3">&nbsp;
      	<font class="campo" style="color:red"><%=StringUtils.toStringJSP(RichiestaGE.getFlagPiuMenoR(),"")%></font>&nbsp;&nbsp;
      	<font class="label"> Anni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumAnniReclusioneR(), " - ") %></font>&nbsp;
		<font class="label"> Mesi </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumMesiReclusioneR(), " - ") %></font>&nbsp;
        <font class="label"> Giorni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumGiorniReclusioneR(), " - ") %></font>&nbsp;
         &nbsp;&nbsp;&nbsp;	<font class="label"> Multa : </font>&nbsp;
         <%					if(RichiestaGE.getImportoMultaR()!=null && RichiestaGE.getImportoMultaR().compareTo(BigDecimal.ZERO) > 0 )
         					{	%>
        						<font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getImportoMultaR()) %></font>&nbsp;&euro;&nbsp;
        			<%		}
         					else
         					{	%>
         						<font class="campo">&nbsp; - &nbsp; </font>
         				<%	}	 %>				

      </td>
    </tr>
	<tr>
      <td class="l" colspan="1">Arresto :  </td>
      <td class="l" colspan="3">&nbsp;
        <font class="campo" style="color:red"><%=StringUtils.toStringJSP(RichiestaGE.getFlagPiuMenoR(),"")%></font>&nbsp;&nbsp;
      	<font class="label"> Anni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumAnniArrestoR(), " - " ) %></font>&nbsp;
		<font class="label"> Mesi </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumMesiArrestoR(), " - ") %></font>&nbsp;
        <font class="label"> Giorni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumGiorniArrestoR(), " - ") %></font>&nbsp;
         &nbsp;&nbsp;&nbsp;	<font class="label"> Ammenda : </font>&nbsp;
         <%					if(RichiestaGE.getImportoAmmendaR()!=null && RichiestaGE.getImportoAmmendaR().compareTo(BigDecimal.ZERO) > 0 )
         					{	%>
        						<font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getImportoAmmendaR()) %></font>&nbsp;&euro;&nbsp;
        			<%		}
         					else
         					{	%>
         						<font class="campo">&nbsp; - &nbsp; </font>
         				<%	}	 %>				

      </td>
    </tr>
<%
	}
 }	
%>
    
<%	if(RichiestaGE.getMotivazioni()!=null && !RichiestaGE.getMotivazioni().equals("") )
	{	%>
	<tr>
      <td class="l" colspan="1">Motivazioni :  </td>
      <td class="l" colspan="3">&nbsp;
      	<font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getMotivazioni(),"" )%></font>
      </td>
    </tr>  		
<%	} %>	    
  </table>

<!-- 									DATI 	INVIO 	RICHIESTA 								 -->  
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

<!-- 									DATI 	TITOLO 	RIFERIMENTO 								 -->  
<% 	
  String AnnoNumeroRev ="";
  AnnoNumeroRev = TitoloRiferimento.getAnnoSentenza() +"/"+TitoloRiferimento.getNumeroSentenza();
 %> 
  <table width="95%" align="center">
    <tr><td colspan="8" class="Titolonocap" style="text-align:left" >In Relazione al Titolo </td></tr>
    <tr>
        <td class="l" colspan="8">
          <font class="label"><%=StringUtils.toStringJSP(TitoloRiferimento.getDescrTipoProvvedimento() )%>&nbsp;N. </font>
          <font class="campo"><%=AnnoNumeroRev%></font>&nbsp;
          &nbsp;<font class="label"> del </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(TitoloRiferimento.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
          &nbsp;<font class="label"> Emessa da </font>
          <font class="campo"><%=StringUtils.toStringJSP(TitoloRiferimento.getDescrTipoAutoritaEmittente(), "") %></font>
          <font class="label">&nbsp;di&nbsp; </font>
          <font class="campo"><%=StringUtils.toStringJSP(TitoloRiferimento.getDescrLuogoEmittente(), "") %></font>

          &nbsp;<font class="label"> Irrevocabile il  </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(TitoloRiferimento.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
        </td>
      </tr>
  </table>
<br>
<!-- 							Inizio Dati Inseribili/Modificabili									 -->
<%
  //========================================================================
  //  Dati esclusivi della Decisione da Inserire / Modificare
  //========================================================================
 %>
<tr>
  <td colspan="100%" align="center">

<div id="idDivDecisioneGE"  style="display:block" >   
  <table width="95%" align="center">
    <tr><td colspan="3" class="Titolonocap">Decisione del GE</td></tr>      
    <tr>
      <td class="l">Ordinanza <font class="ob">(*)</font> :</td>
		<td class="l">
          Anno/Numero Procedimento SIGE
          <input type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>" size=4 maxlength=4 value="<%=StringUtils.toStringJSP(lDecisioneGE.getAnnoProvv(),"") %>" 
          		onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
          /
          <input type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>" size=8 maxlength=6 value="<%=StringUtils.toStringJSP(lDecisioneGE.getNumeroProvv(),"") %>">
      	</td>
      	<td class="l">
          <font class="label">Data emissione Procedimento</font>
          &nbsp;&nbsp;
           <input type="text"  title="Giorno di arrivo documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D %>" maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDecisioneGE.getDataD(), "dd"), "") %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
             /
           <input type="text"  title="Mese di arrivo documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D %>" maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDecisioneGE.getDataD(), "MM"), "") %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
           /
           <input type="text"  title="Anno di arrivo documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE_D %>" maxlength="4" size="4" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDecisioneGE.getDataD(), "yyyy"), "") %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        </td>	
     
    </tr>
    
    <tr>
      <td class="l">Ufficio Emittente <font class="ob">(*)</font> :</td>
      <td class="l" colspan="1">
          <select Title="Ufficio Emittente" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>">
            <%=UfficioEmittente%>
          </select>
      </td>
      <td class="l">Sede Ufficio Emittente <font class="ob">(*)</font> :
          <font class="campo">
            <input type="text" Title="Luogo Ufficio Emittente" size="35"
                   name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%=StringUtils.toStringJSP(lDecisioneGE.getDescrLuogoEmittente(), "") %>" >
            <a href="Javascript:ListaUfficiComuni('LoadInsDecGERevoca','<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>',
            					document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>[document.LoadInsDecGERevoca.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.options.selectedIndex].value);">
              <img src="/images/filefolder.gif" border=0>
            </a>
          </font>
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
      
      	if("C".equals(lDecisioneGE.getFlagConforme()) ){
        	checkC = "checked";
      	}
      	else if("D".equals(lDecisioneGE.getFlagConforme()) ) { 
        	checkD = "checked";
      	}
      	else if("I".equals(lDecisioneGE.getFlagConforme()) ) { 
        	checkI = "checked";
      	}
      	else if("R".equals(lDecisioneGE.getFlagConforme()) ) { 
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
       	<td class="l" colspan=3>&nbsp;
       		<font class="label">Revoca : </font>&nbsp;&nbsp;
<%		
		String spuntaS = "";
		String spuntaN = "";
		if("M".equals(modalita) && "S".equals(lDecisioneGE.getBenSospCond()) )
		{
			spuntaS = "checked";
		}
		
		if("M".equals(modalita) && "N".equals(lDecisioneGE.getBenNonMenzione()) )
		{
			spuntaN = "checked";
		}
		
		if(!lBenCod2.equals(""))
		{	%>
			<input type="checkBox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_REVOCA_BEN_1%>" value="S" <%=spuntaS%> >&nbsp;
   			  <font class="label">Sospensione condizionale della Pena</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   			<input type="checkBox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_REVOCA_BEN_2%>" value="N" <%=spuntaN%> >&nbsp;  
       		  <font class="label">Non Menzione</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
<%		}
		else
		{	
			if(lBenCod1.equals("01") )
			{	%>
				<input type="checkBox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_REVOCA_BEN_1%>" value="S" checked >&nbsp;
   			  	  <font class="label">Sospensione condizionale della Pena</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
<%			}
			else if(lBenCod1.equals("02") )
			{	%>
				<input type="checkBox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_REVOCA_BEN_1%>" value="N" checked >&nbsp;  
    		  	  <font class="label">Non Menzione</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
<%			}
			else if(lBenCod1.equals("03") || lBenCod1.equals("002")  )
			{	%>   
				<input type="checkBox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_REVOCA_BEN_1%>" value="I" checked >&nbsp;
       		  	  <font class="label">Indulto</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
<%		    }
			else if(lBenCod1.equals("04") || lBenCod1.equals("003") )
			{	%>			
			    <input type="checkBox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_REVOCA_BEN_1%>" value="A" checked >&nbsp;
       		  	  <font class="label">Amnistia</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%			} 
		}	%>       		  	  		  		      		  
       	</td>				
      </tr>
    </table>    

<%
if(lBenCod2.equals("") )
{	
	if("03".equals(lBenCod1) || "04".equals(lBenCod1) || "002".equals(lBenCod1) || "003".equals(lBenCod1) ) 
	{	%>    
    <table width="95%" align="center">
    <tr>
      <td colspan=6>
        <hr width="100%">
      </td>
    </tr>
    <tr>
      <td valign="middle" class="c" rowspan=3>+/- <font class="ob">(*)</font><br>
        <select name="<%= ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_D %>">
   <%	if( "-".equals(lDecisioneGE.getFlagPiuMenoD()) ) 
    	{	%>    
          <option value = "" ></option>
          <option value = "+" >+</option>
          <option value = "-" selected >-</option>
   <%	}
    	else if( "+".equals(lDecisioneGE.getFlagPiuMenoD()) ) 
    	{ %>
    	  <option value = "" ></option>
          <option value = "+" selected >+</option>
          <option value = "-" >-</option>
  <%	}
    	else
    	{ %>
    	  <option value = "" ></option>
          <option value = "+" >+</option>
          <option value = "-" >-</option>
  <%	} %> 
	
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
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_D%>" 
               value="<%=StringUtils.toStringJSP(lDecisioneGE.getNumAnniReclusioneD(),"")%>"
               onkeypress="return TicTabNumField(this,event)">&nbsp;
        <input type="text" maxlength="2" size="2"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_D %>"  
               value="<%=StringUtils.toStringJSP(lDecisioneGE.getNumMesiReclusioneD(),"") %>"
               onkeypress="return TicTabNumField(this,event)">&nbsp;
        <input type="text" maxlength="4" size="4"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_D %>"                 
               value="<%=StringUtils.toStringJSP(lDecisioneGE.getNumGiorniReclusioneD(),"") %>"
               onkeypress="return TicTabNumField(this,event)">
      </td>
      <td class="c">
        <font  class="label">Multa</font><br>
        <input type="text" maxlength="7" size="7" style="align:right"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_D%>INT"
               value="<%=StringUtils.getParteIntera(lDecisioneGE.getImportoMultaD()) %>"
               onkeypress="return TicTabNumField(this,event)" >
        ,
        <input type="text" maxlength="2" size="2"  style="align:right"
               name="<%= ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_D %>DEC"
               value="<%=StringUtils.getParteDecimale(lDecisioneGE.getImportoMultaD()) %>"
               onkeypress="return TicTabNumField(this,event)">
      </td>
      <td width="25">&nbsp;</td>
      <td class=c>
        <font  class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font  class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font  class="label">Giorni</font><br>
        <input type="text" maxlength="2" size="2"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_D %>"  
               value="<%=StringUtils.toStringJSP(lDecisioneGE.getNumAnniArrestoD(),"") %>"
               onkeypress="return TicTabNumField(this,event)" >&nbsp;
        <input type="text" maxlength="2" size="2" 
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_D %>"  
               value="<%=StringUtils.toStringJSP(lDecisioneGE.getNumMesiArrestoD(),"") %>"
               onkeypress="return TicTabNumField(this,event)" >&nbsp;
        <input type="text" maxlength="4" size="4"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_D %>"          
               value="<%=StringUtils.toStringJSP(lDecisioneGE.getNumGiorniArrestoD(),"") %>"
               onkeypress="return TicTabNumField(this,event)">
      </td>
      <td class="c">
        <font  class="label">Ammenda</font><br>
        <input type="text" maxlength="7" size="7" style="align:right"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_D%>INT"         
               value="<%=StringUtils.getParteIntera(lDecisioneGE.getImportoAmmendaD()) %>"               
               onkeypress="return TicTabNumField(this,event)">
        ,
        <input type="text" maxlength="2" size="2" style="align:right" 
               name="<%= ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_D %>DEC"  
               value="<%=StringUtils.getParteDecimale(lDecisioneGE.getImportoAmmendaD()) %>"
               onkeypress="return TicTabNumField(this,event)">
      </td>
    </tr>
    <tr>
      <td class="c" colspan=5>
        <font class="label" style="vertical-align: top;">Motivazioni </font>
        <textarea cols="60" rows="2" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI_D %>"><%=StringUtils.toStringJSP(lDecisioneGE.getMotivazioniD(),"") %></textarea>
      </td>
      <td width=20>&nbsp;</td>
    </tr>
  </table>
<%
	}
 }	%>  
  <br>
  
  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>
  </table>
     
</div>

</td>
</tr>
<!-- 					end dati inseribili / modificabili													--> 

</FORM>
</body>

<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("LoadInsDecGERevoca");
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>

</html>

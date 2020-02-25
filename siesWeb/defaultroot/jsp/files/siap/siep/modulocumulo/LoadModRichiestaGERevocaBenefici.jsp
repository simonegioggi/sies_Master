<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>
<%@page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>
<%@page import="java.math.BigDecimal"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiBeneficiCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.penasospesa.action.ICostantiPenaSospesa" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.siep.modulocumulo.model.BeneficioCumuloModel" %>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloRichiesta"    	scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="TitoloRiferimento"     scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="RichiestaAlGE"			scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="articolo"		       	scope="request" class="java.lang.String"/>
<jsp:useBean id="motivazione"		   	scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>

  
<!-- 			LoadModRichiestaGERevocaBenefici -->  
<%
//============================================================= 
// Form per la modifica delle richieste al GE di Revoca
// Benefici
//=============================================================

// Preparazione delle conboBox Articolo e Motivazione
Collection oggetto =(Collection) request.getAttribute("oggetto");
String strOggetto ="";
int element = 0;
int eledaModificare = 0;
Iterator itxOggetto = oggetto.iterator();
while(itxOggetto.hasNext())
{
   DecodificheModel lDecMod = (DecodificheModel)itxOggetto.next();
  
   strOggetto += lDecMod.getFiltro() +";";
   strOggetto += lDecMod.getCode()+";";
   strOggetto += lDecMod.getDescription()+"#";
   //LogF3B.getLogger().debug("strOggett="+strOggetto);
   
   if(lDecMod.getFiltro().equals(articolo))
   {	
	   	element++;
   		if(lDecMod.getCode().equals(RichiestaAlGE.getCodMotivo()))
   		{
	   		eledaModificare = element;
   		}
   }
}

// Costruzione dei model con i benefici interessati alla revoca
BeneficioCumuloModel lBenMod1 = null;
BeneficioCumuloModel lBenMod2 = null;
String lBenCod1="";
String lBenCod2="";

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
boolean isConProvvedimento = false;

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

%>

<html>
<head>
  <title> Gestione Richieste Revoca Benefici </title>
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
   
 // Caricamento POPUP
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    function ElencoTitoliPopup (a_form_name, a_form_type)
    {
      <%
      String lStrParametri = "";
      lStrParametri +="&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo();
      lStrParametri +="&"+ICostantiRichiestePmInCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO+"="+TitoloRichiesta.getIdTitoloCumulato().toString();
      %>
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActCaricaElencoTitoliPerIstruttorieRichieste&ParentFormName="+a_form_name+"&ParentFormType="+a_form_type+"<%=lStrParametri%>"		
                          , "Elenco_Titoli"
                          , "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=yes, width=990, height=400");
    } 
	//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    
    var TipodaModificare ="<%=eledaModificare%>";
    function caricacomborichiesta()
    {
      <%  if(!articolo.equals("") && !motivazione.equals("") ) { %>      
      caricatuttecombo();
      var comboMotivo = document.getElementById('<%=ICostantiRichiestePmInCumulo.CAMPO_COD_MOTIVO %>');
      comboMotivo.selectedIndex = TipodaModificare-1;
      <% } %>
    }
    
    function caricatuttecombo()
    {
      var strOggetto = "<%=strOggetto%>";
      var articolo = $('#Articolo option:selected').text();
      caricaCombo(strOggetto,';','#',articolo,document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_MOTIVO%>);
    }
    
    function caricaCombo (valueTextStr, sep1, sep2, filtro, selField)
    {
      // valueTextStr = stringa nel formato richiesto
      // sep1 = separatore interno alla coppia di valori
      // sep2 = separatore tra coppie
      // filtro = valore su cui fare il test
      // selField = oggetto combo da caricare
  
      clearDropDown(selField);
      var aPairs = valueTextStr.split(sep2);
      if (valueTextStr.substr(valueTextStr.length - 1) == sep2)
      {
        aPairs[aPairs.length - 1] = null;
        aPairs.length--;
      }
  
      for (var i=0; i < aPairs.length; i++)
      {
        aValueText = aPairs[i].split(sep1);
        if (filtro=='null' || filtro==aValueText[0])
        {
          oItem = new Option;
          oItem.value = aValueText[1];
          oItem.text = aValueText[2];
          selField.options[selField.options.length] = oItem;
        }
      }
      selField.options.selectedIndex = 0;
    }  
      
    function clearDropDown (selField)
    {
      while (selField.options.length > 0)
        selField.options[0] = null;
    }

    function Verify() 
    { 
   	   // Data Richiesta Revoca
       if (document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
           document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
       if (document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
           document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value;

       var data_to_verify = document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE%>.value;
       if (data_to_verify=='//' )
       {
           alert('Indicare la Data Richiesta');
           document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       }
       
       if (!ControllaData(data_to_verify) )
       {
           alert('Data Richiesta non valida');
           document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       }
       
       // Controllo inserimento estremi Sentenza di riferimento
	   // 20/05/2019 MEV70 Il suddetto controllo non va applicato per l'articolo 'Revoca Beneficio ex art. 165 c.p.p.'.
	   /*MAC 20200110018 - monica - 15/01/2020 - 11.2.5
	    // aggiunto controllo sul tipo di beneficio: se il beneficio è un Indulto (Codice='002') 
         o un Indulto in Sentenza (Codice '03') non è obbligatorio l'articolo di revoca e il campo ICostantiRichiestePmInCumulo.CAMPO_COD_ARTICOLO non è presente
         nella pagina
	   */
	   var lCodBeneficio = '<%=lBenCod1%>';
        var articolo_Revoca = "";
        if(lCodBeneficio != '002' && lCodBeneficio != '03'){	
        	document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_ARTICOLO%>.value;
        }
      //FINE MAC 20200110018 - monica - 15/01/2020 - 11.2.5
      
    	//if (articolo_Revoca.length != 4  &&
       	// 	lCodBeneficio!='002' &&  // Se il beneficio è un Indulto (Codice='002') o un Indulto in Sentenza (Codice '03') non è obbligatorio l'articolo di revoca.
       	// 	lCodBeneficio!='03' ) { 
    	//	alert('Selezionare un articolo di Revoca');		
    	//      return false;
    	//}
		if (articolo_Revoca!='1108') {
	       if(   document.ModRichGERevocaBen.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value == '' 
	          || document.ModRichGERevocaBen.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value.length == 0 )
	        {
	           alert('Selezionare il Titolo Esecutivo di Riferimento che determina la revoca');
	           return false;
	        }       
		} else {
		     //alert ('articolo_Revoca = 1108. Non effettuo il controllo di selezione del Titolo Esecutivo');
		}
       
       
       if(document.getElementById('divTitRev').style.display=="none") 
       {
      	 	//alert("Non ho selezionato il Link");
      	 	document.ModRichGERevocaBen.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO %>.value = <%=TitoloRiferimento.getIdTitoloCumulato()%>
       }
       
       var jsBenCod1 = '<%=lBenCod1%>';
       var jsBenCod2 = '<%=lBenCod2%>';
	   
 	 	if(jsBenCod2 == '' )
 	 	{
 	 		// Caso di Aministia / Indulto
 	 		if(jsBenCod1 == '03' || jsBenCod1 == '04' || jsBenCod1 == '002' || jsBenCod1 == '003')
 	 		{	
 	 		 	// Quantum
 	 		 	var pos = document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT.value.indexOf('.');
				if(pos > 0)
				{
					alert('inserire correttamente il valore INTERO della Multa');
		        	document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT.focus();
		        	return false;
				}
				
				pos = document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT.value.indexOf('.');
				if(pos > 0)
				{
					alert('inserire correttamente il valore INTERO della Ammenda');
		        	document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT.focus();
		        	return false;
				}
 	 		 	
 	        	if(document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_R%>.value == ''  )
 	        	{
 	    			alert('digitare il segno + / - quantum pena');
 	             	document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_R%>.focus();
 	             	return false;
 	        	}
 	        
	 	        if( ( document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>.value == '' ||
	 	       		  document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>.value.length == 0 )  &&
	 	       	    ( document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_R%>.value == '' ||
	 	              document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_R%>.value.length == 0  ) &&	
	 	            ( document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_R%>.value == '' ||
	 	              document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_R%>.value.length == 0 ) &&
	 	            	 
	 	            ( document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_R%>.value == '' ||
	 	           	  document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_R%>.value.length == 0 ) &&
	 	            ( document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_R%>.value == '' ||
	 	              document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_R%>.value.length == 0 ) &&
	 	            ( document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_R%>.value == '' ||
	 	              document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_R%>.value.length == 0 ) &&	
	 	              
	 	            ( document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT.value.length == 0 ) &&
	 	            ( document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>DEC.value.length == 0 ) &&	
	 	            ( document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT.value.length == 0 ) &&
	 	            ( document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>DEC.value.length == 0 ) 
	 	          )	
	 	        {
	 	       	 	alert('digitare una quantità di pena da Revocare');
	 	           	document.ModRichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>.focus();
	 	           	return false;
	 	        }
  	 		}
 	 	}

        return true; 
    }
    
  </script>
</head>

<body class="corpo" onload="caricacomborichiesta();">
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
        <font class="campo">Inserimento Richiesta Revoca Beneficio &nbsp;</font>
        <%
          } else if( modalita.equals("M") ) {
        %>
        <font class="campo">Modifica Richiesta Revoca Beneficio &nbsp;</font>
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
 
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ModRichGERevocaBen">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRichiestaGERevocaBenefici">
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" 		value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO %>" 	value="<%=TitoloRichiesta.getIdTitoloCumulato()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" 	value="<%=RichiestaAlGE.getIdRichiestePmInCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA %>" 		value="01">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>" 	value="021">
  <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO %>"  			value="<%=lBenCod1%>">
  
 <% if(isConProvvedimento) { %>
  		<input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>" 			value="<%=lCompMod.getIdComputiCumulo()%>">
  		<input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_STATOESEC_CUM_SEL%>" 	value="<%=lStatEsecMod.getIdStatoEsecTitoloCumulato()%>">
 <% }
 	else {	%>
 		<input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_BENEFICIO_CUM_SEL_1%>" 	value="<%=lBenMod1.getIdBeneficioCumulo()%>">
<%  } %> 	 		
<%if(!lBenCod2.equals(""))
  {	%>
	<input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_BENEFICIO_CUM_SEL_2%>" 	value="<%=lBenMod2.getIdBeneficioCumulo() %>">
<%} %>    
  
  <%//	 Campi Hidden valorizzati dalla Popup  
  //MAC_20200110018 CORREZIONE DEL 12/02/2020
  String idTitCumulato = "";
  if(TitoloRiferimento != null){
	  if(TitoloRiferimento.getIdTitoloCumulato() != null){
	   idTitCumulato = TitoloRiferimento.getIdTitoloCumulato().toString();
	  }
  }
  %>
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO %>"	value="<%=idTitCumulato %>" >
   <%//FINE MAC_20200110018 CORREZIONE DEL 12/02/2020 %>

<!-- 				BENEFICI DA REVOCARE			 -->  
  <table width="95%" align="center">
    <tr><td colspan="5" class="Titolonocap">Beneficio da Revocare</td></tr>
  </table>  

<%
if(!lBenCod2.equals(""))
{
	//  Sospensione Condizionale	%>
    <table width="95%" align="center" id="tabBen_S">
      <tr>
        <td class="c">Tipo Beneficio</td>
        <td class="c">Natura Beneficio</td>
        <td class="c">Durata sospensione</td>
      </tr>
      
      <tr>
      <% if(lBenCod1.equals("01") ){ %>
        <td class="l" nowrap><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrTipoBeneficio(),"") %></font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrSottotipoBeneficio(), "") %></font></td>
        <td class="l"><font class="campo">Anni:&nbsp;<%=StringUtils.toStringJSP(lBenMod1.getNumAnniSospensione(), "-" ) %></font></td>
      <% } else { %>
        <td class="l" nowrap><font class="campo"><%=StringUtils.toStringJSP(lBenMod2.getDescrTipoBeneficio(),"") %></font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lBenMod2.getDescrSottotipoBeneficio(), "") %></font></td>
        <td class="l"><font class="campo">Anni:&nbsp;<%=StringUtils.toStringJSP(lBenMod2.getNumAnniSospensione(), "-" ) %></font></td>
      <% } %>      
      </tr>
	</table>

<%//  Non Menzione	%>

	<table width="95%" align="center" id="tabBen_NM">
	  <tr>
	    <% if(lBenCod1.equals("02") ){ %>
	  	<td class="l" width="150px"><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrTipoBeneficio(),"") %></font></td>
      <% } else { %>
	  	<td class="l" width="150px"><font class="campo"><%=StringUtils.toStringJSP(lBenMod2.getDescrTipoBeneficio(),"") %></font></td>
      <% } %>      
	  	<td class="l" colspan="2"><img src="/images/V.gif"> </td>
	  </tr>
	</table> 
<%
 }
 else
 {
 	if(lBenCod1.equals("01") )
	{
	// Sospensione		%>	 
	<table width="95%" align="center"  id="tabBen_S">
      <tr>
        <td class="c">Tipo Beneficio</td>
        <td class="c">Natura Beneficio</td>
        <td class="c">Durata sospensione</td>
      </tr>
      
      <tr>
        <td class="l" nowrap><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrTipoBeneficio(),"") %></font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrSottotipoBeneficio(), "") %></font></td>
        <td class="l"><font class="campo">Anni:&nbsp;<%=StringUtils.toStringJSP(lBenMod1.getNumAnniSospensione(), "-" ) %></font></td>
      </tr>
	</table>
<%	}
 	else if(lBenCod1.equals("02") )
 	{	
 	// Non Menzione	%>
	<table width="95%" align="center"  id="tabBen_NM">
	  <tr>
	  	<td class="l" width="150px"><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrTipoBeneficio(),"") %></font></td>
	  	<td class="l" colspan="2"><img src="/images/V.gif"> </td>
	  </tr>
	</table> 
<%	}
 	else if(lBenCod1.equals("03") || lBenCod1.equals("04") || lBenCod1.equals("002") || lBenCod1.equals("003") )
 	{	
 	    if(isConProvvedimento) {  // Amnistia/ Indulto dati con provvedimento   %>
	    <table width="95%" align="center" id="tabBen_I">
	      <tr>
	        <td class="c">Tipo Beneficio</td>
	        <td class="c">Provvedimento di Concessione</td>
	        <td class="c">Concesso Nella misura di</td>
	      </tr>
	      
	      <tr>
	        <td class="l"><font class="campoLow"><%=StringUtils.toStringJSP(lCompMod.getDescrTipoAnnotazione(),"") %> </font></td>
	        <td class="l"><font class="campoLow"><%=StringUtils.toStringJSP(lCompMod.getDescDpr() ,"") %></font></td>
	        <td class="l">
	      
	        <%  if( !lCompMod.isQuantumReclusioneZero()) {  %>
	        <font class="campoLow">Reclusione&nbsp;&nbsp;Anni: <%=StringUtils.toStringJSP(lCompMod.getNumAnniReclusione(), "0") %>&nbsp;
	                                mesi: <%=StringUtils.toStringJSP(lCompMod.getNumMesiReclusione(), "0") %>&nbsp;
	                                Giorni: <%=StringUtils.toStringJSP(lCompMod.getNumGiorniReclusione(), "0") %>&nbsp;
	            </font>&nbsp;
	        <% } %>
	
	    
	        <% if( lCompMod.getImportoMulta() != null && lCompMod.getImportoMulta().compareTo(BigDecimal.ZERO) > 0  ) { %>
	          <font class="campoLow">Multa:&nbsp;<%=StringUtils.toStringJSP(lCompMod.getImportoMulta(), "-") %>&nbsp;&euro;</font>                
	        <%  } %>
	      
	  
	        <% if( !lCompMod.isQuantumArrestoZero() ) { %>
	        <font class="campoLow">Arresto&nbsp;&nbsp;Anni: <%=StringUtils.toStringJSP(lCompMod.getNumAnniArresto(), "0") %>&nbsp;
	                                mesi: <%=StringUtils.toStringJSP(lCompMod.getNumMesiArresto(), "0") %>&nbsp;
	                                Giorni: <%=StringUtils.toStringJSP(lCompMod.getNumGiorniArresto(), "0") %>&nbsp;
	            </font>&nbsp;
	        <% } %>
	    
	  
	        <% if( lCompMod.getImportoAmmenda() != null && lCompMod.getImportoAmmenda().compareTo(BigDecimal.ZERO) > 0 ) { %>
	          <font class="campoLow">Ammenda:&nbsp;<%=StringUtils.toStringJSP(lCompMod.getImportoAmmenda(), "-") %>&nbsp;&euro;</font>                
	        <%  } %> 
	
	      </td>
	      </tr>
	    </table>
<%		} else { 
				// Amnistia/ Indulto dati in Sentenza %>
		<table width="95%" align="center" id="tabBen_I">
	      <tr>
	        <td class="c">Tipo Beneficio</td>
	        <td class="c">Provvedimento di Concessione</td>
	        <td class="c">Concesso Nella misura di</td>
	      </tr>
	      
	      <tr>
	        <td class="l"><font class="campoLow"><%=StringUtils.toStringJSP(lBenMod1.getDescrTipoBeneficio(),"") %> </font></td>
	        <td class="l"><font class="campoLow"><%=StringUtils.toStringJSP(lBenMod1.getDescrDpr() ,"") %></font></td>
	        <td class="l">
	      
	        <%  if( !lBenMod1.isQuantumReclusioneZero()) {  %>
	        <font class="campoLow">Reclusione&nbsp;&nbsp;Anni: <%=StringUtils.toStringJSP(lBenMod1.getNumAnniReclusione(), "0") %>&nbsp;
	                                mesi: <%=StringUtils.toStringJSP(lBenMod1.getNumMesiReclusione(), "0") %>&nbsp;
	                                Giorni: <%=StringUtils.toStringJSP(lBenMod1.getNumGiorniReclusione(), "0") %>&nbsp;
	            </font>&nbsp;
	        <% } %>
	
	    
	        <% if( lBenMod1.getImportoMulta() != null && lBenMod1.getImportoMulta().compareTo(BigDecimal.ZERO) > 0  ) { %>
	          <font class="campoLow">Multa:&nbsp;<%=StringUtils.toStringJSP(lBenMod1.getImportoMulta(), "-") %>&nbsp;&euro;</font>                
	        <%  } %>
	      
	  
	        <% if( !lBenMod1.isQuantumArrestoZero() ) { %>
	        <font class="campoLow">Arresto&nbsp;&nbsp;Anni: <%=StringUtils.toStringJSP(lBenMod1.getNumAnniArresto(), "0") %>&nbsp;
	                                mesi: <%=StringUtils.toStringJSP(lBenMod1.getNumMesiArresto(), "0") %>&nbsp;
	                                Giorni: <%=StringUtils.toStringJSP(lBenMod1.getNumGiorniArresto(), "0") %>&nbsp;
	            </font>&nbsp;
	        <% } %>
	    
	  
	        <% if( lBenMod1.getImportoAmmenda() != null && lBenMod1.getImportoAmmenda().compareTo(BigDecimal.ZERO) > 0 ) { %>
	          <font class="campoLow">Ammenda:&nbsp;<%=StringUtils.toStringJSP(lBenMod1.getImportoAmmenda(), "-") %>&nbsp;&euro;</font>                
	        <%  } %> 
	
	      </td>
	      </tr>
	    </table>

<%		}	    
  	}
 }    %> 

<!-- 				TITOLO  SU CUI è STATA FATTA LA REVOCA -->    
    <table width="95%" align="center" id="tabTitoloConc">
      <tr><td colspan="8" class="Titolonocap">Concesso sul Titolo</td></tr>
<%
	String AnnoNumero = TitoloRichiesta.getAnnoSentenza() +"/"+TitoloRichiesta.getNumeroSentenza();
%>      
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
  
<% //================================================================================================ %>
<!-- 								Titolo che determina la revoca									 -->
<% //================================================================================================ %>

  <br>
  <table width="95%" align="center">
    <tr><td class="Titolonocap">In relazione al Titolo</td></tr>
  </table>

  <table width="95%" align="center">
    <tr>
      <td class="l">
        <a href="Javascript:ElencoTitoliPopup('ModRichGERevocaBen','<%=ICostantiRichiestePmInCumulo.FORM_TYPE_RIC_REV_BENEFICI%>');">
          Revocato in relazione al Titolo Esecutivo <img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>
  </table>

<!-- 				ESTREMI TITOLO di riferimento SCELTO IN INSERIMENTO 					 -->
  <div id="divTitRevOld" style="display:block">	  
    <table width="95%" align="center" id="tabDatiTitRevOld">
<%
	if(TitoloRiferimento.getAnnoSentenza()!=null)  {
	String AnnoNumeroRif = TitoloRiferimento.getAnnoSentenza() +"/"+TitoloRiferimento.getNumeroSentenza();
%>      
      <tr>
        <td class="l" colspan="8">
          <font class="label"><%=StringUtils.toStringJSP(TitoloRiferimento.getDescrTipoProvvedimento() )%>&nbsp;N. </font>
          <font class="campo"><%=AnnoNumeroRif%></font>&nbsp;
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
<% 	} %>      
    </table>
  </div>  

<!-- 				ESTREMI TITOLO di riferimento MODIFICATO				 -->
  <div id="divTitRev" style="display:none">
    <table width="95%" align="center" id="tabDatiTitRev" >
      <tr>
        <td class="l" width="15%" >Estremi Titolo </td>
        <td class="L" id="descTitoloRevocante">&nbsp;</td>
      </tr>
    </table>
  </div>

<!-- 			 DATI   MODIFICABILI   DELLA  REVOCA				 -->

    <table width="95%" align="center" id="tabBen_Revo"> 
      <tr><td class="Titolonocap" colspan="1">Revoca</td></tr> 
      <tr>
        <td colspan="100%">
            
<%	if(!articolo.equals("") && !motivazione.equals("") )
	{ %> 
          <table width="100%">           
            <tr>
              <td class="l">Articolo</td>
              <td class="l">
                <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_ARTICOLO %>" 
                        id="Articolo"
                        Title="Tipo Provvedimento" 
                        onchange="javascript:caricatuttecombo()">
 <%
			  Iterator itxArticolo = oggetto.iterator();
			  while(itxArticolo.hasNext())
			  {
			     DecodificheModel lDecMod = (DecodificheModel)itxArticolo.next();
			     if(    (lDecMod.getCode()).equals("1101")
			    	|| (lDecMod.getCode()).equals("1102")|| (lDecMod.getCode()).equals("1103")
			    	|| (lDecMod.getCode()).equals("1104")|| (lDecMod.getCode()).equals("1105")
			    	|| (lDecMod.getCode()).equals("1106")|| (lDecMod.getCode()).equals("1107")
			    	|| (lDecMod.getCode()).equals("1108")
			      )
			     {
			   		if( (lDecMod.getCode()).equals(lDecMod.getCodiceAlt2()))
			   		{
			   			if(lDecMod.getCode().equals(RichiestaAlGE.getCodMotivo() ) )
			   			{
 %>
			       		<option value = <%=lDecMod.getCode()%> selected ><%=lDecMod.getFiltro()%></option>  
 <%						}
			   			else
			   			{	%>
			   			<option value = <%=lDecMod.getCode()%>><%=lDecMod.getFiltro()%></option>
<% 			   				
			   		 	}
			      	}
			      }	
			  }
 %>
                </select>
          </td>
    </tr>
    <tr>
      <td class="l">Motivazione</td>
      <td class="l">
            <select class="small"  Title="Tipo Provvedimento" 
              name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_MOTIVO %>"
              id="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_MOTIVO %>"
              >

            </select>
      </td>
    </tr>     
  </table>

<%	} %>        

  <br>
  <table width="100%" >
    <tr>
      <td class="l" width="15%" >Data Richiesta </td>
      <td class="L" >
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaAlGE.getDataEmissione(),"dd"), "")%>"   type="text" size="2" maxlength="2" 
        	name="<%= ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" 
        	onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaAlGE.getDataEmissione(),"MM"), "")%>"   type="text" size="2" maxlength="2" 
        	name="<%= ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" 
        	onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaAlGE.getDataEmissione(),"yyyy"), "")%>" type="text" size="4" maxlength="4" 
        	name="<%= ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" 
        	onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l" width="15%" >Motivazioni </td>  	
      <td class="c" style="text-align:left">
        <textarea cols="100" rows="4" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI%>"><%=StringUtils.toStringJSP(RichiestaAlGE.getMotivazioni(), "") %></textarea>
      </td>
    </tr>
  </table>
  
  
  <%
  //============================================================================
  // DIV da visualizzre solo nel caso di Revoca Amnistia/Indulto
  //============================================================================
if(lBenCod2.equals(""))
{
	if(lBenCod1.equals("03") || lBenCod1.equals("04") || lBenCod1.equals("002") || lBenCod1.equals("003") )
	{	%>
  
  <div id="divQuantum" >
    <br>
    <table width="100%" >
      <tr>
        <td class="Titolonocap" colspan="3" >Beneficio da Revocare nella misura di</td>
      </tr>
    </table> 
  
    <table width="100%" >
      <tr>
        <td colspan=6>
          <hr width="100%">
        </td>
      </tr>
      <tr>
        <td valign="middle" class="c" rowspan=2>+/- <font class="ob">(*)</font><br>
          <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_R %>">
 <%	if( "-".equals(RichiestaAlGE.getFlagPiuMenoR()) ) 
   	{	%>    
          <option value = "" ></option>
          <option value = "+" >+</option>
          <option value = "-" selected >-</option>
 <%	}
    else if( "+".equals(RichiestaAlGE.getFlagPiuMenoR()) ) 
    { %>
    	  <option value = "" ></option>
          <option value = "+" selected >+</option>
          <option value = "-" >-</option>
 <%}
   else
   { %>
    	  <option value = "" ></option>
          <option value = "+" >+</option>
          <option value = "-" >-</option>
<%} %>
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
               value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumAnniReclusioneR(),"")%>"
               onkeypress="return TicTabNumField(this,event)">&nbsp;
          <input type="text" maxlength="2" size="2"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_R %>"  
               value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumMesiReclusioneR(),"") %>"
               onkeypress="return TicTabNumField(this,event)">&nbsp;
          <input type="text" maxlength="4" size="4"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_R %>"                 
               value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumGiorniReclusioneR(),"") %>"
               onkeypress="return TicTabNumField(this,event)">
        </td>
        <td class="c">
           <font  class="label">Multa</font><br>
           <input type="text" maxlength="7" size="7" style="align:right"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT"
               value="<%=StringUtils.getParteIntera(RichiestaAlGE.getImportoMultaR()) %>"
               onkeypress="return TicTabNumField(this,event)" >
           ,
           <input type="text" maxlength="2" size="2"  style="align:right"
               name="<%= ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R %>DEC"
               value="<%=StringUtils.getParteDecimale(RichiestaAlGE.getImportoMultaR()) %>"
               onkeypress="return TicTabNumField(this,event)">
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
               onkeypress="return TicTabNumField(this,event)">
        ,
        <input type="text" maxlength="2" size="2" style="align:right" 
               name="<%= ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R %>DEC"  
               value="<%=StringUtils.getParteDecimale(RichiestaAlGE.getImportoAmmendaR()) %>"
               onkeypress="return TicTabNumField(this,event)">
        </td>
      </tr>
    </table>
    
    <table width="100%" >
      <tr>
        <td class="l">
          Anticipazione degli effetti&nbsp;&nbsp;
    <%	if( "A".equals(RichiestaAlGE.getFlagAppProvvisoria()) ) 
    	{	%>     
          	<input type="checkbox" name="FlagAppProvvisoria" value="A" checked >
    <%	}
    	else
    	{	%>
    		<input type="checkbox" name="FlagAppProvvisoria" value="A" >
    <%	} %>	      	
        </td>
      </tr>
    </table>
  
  </div>
<%
	}
 }
  %>
</td>
</tr>
</table>
 
 <!-- 	end dati modificabili REVOCA		 --> 
  <br>
  
  <%
  //============================================================================
  // DIV da visualizzre solo nel caso di Sospensione Condizionale/Non Menzione
  //============================================================================
  if(lBenCod1.equals("01") || lBenCod1.equals("02") )
  {	%>
  
  <div id="divAnticipazione" >
    <table width="100%" >
      <tr>
        <td class="l">
          Anticipazione degli effetti&nbsp;&nbsp;
    <%	if( "A".equals(RichiestaAlGE.getFlagAppProvvisoria()) ) 
    	{	%>     
          	<input type="checkbox" name="FlagAnticipazione" value="A" checked >
    <%	}
    	else
    	{	%>
    		<input type="checkbox" name="FlagAnticipazione" value="A" >
    <%	} %>	      	
        </td>
      </tr>
    </table>
  
  </div>
<%
  }
%>
 
 <!-- 	end DIV  --> 

  
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
  var frmvalidator  = new Validator("ModRichGERevocaBen");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
 
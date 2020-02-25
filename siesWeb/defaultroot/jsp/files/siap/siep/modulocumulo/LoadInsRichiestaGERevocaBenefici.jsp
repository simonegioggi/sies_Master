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
<%@ page import="java.util.Vector" %>
<%@ page import="org.apache.log4j.Logger"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiBeneficiCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.siep.modulocumulo.model.BeneficioCumuloModel" %>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="Titolo"    			scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="RichiestaAlGE"			scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>

<jsp:useBean id="modalita" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="lQuantiBenefici" 	scope="request" class="java.lang.String"/>
  
  
<!-- 			LoadInsRichiestaGERevocaBenefici -->  
<%
//============================================================================== 
// Form per l'inserimento delle richieste al GE di Revoca
// Benefici
//==============================================================================

// preparo le ComboBox Articolo e Motivzaione
Collection oggetto =(Collection) request.getAttribute("oggetto");
String strOggetto ="";
Iterator itxOggetto = oggetto.iterator();
while(itxOggetto.hasNext())
{
   DecodificheModel lDecMod = (DecodificheModel)itxOggetto.next();
  
   strOggetto += lDecMod.getFiltro() +";";
   strOggetto += lDecMod.getCode()+";";
   strOggetto += lDecMod.getDescription()+"#";
//	LogF3B.getLogger().debug("strOggett="+strOggetto);
}

// Preparo i Model con i benefici selezionati (Nax 2 Benefici) 

Vector<BeneficioCumuloModel> lVecBen = null;
BeneficioCumuloModel lBenMod = null;
BeneficioCumuloModel lSosp = new BeneficioCumuloModel();
BeneficioCumuloModel lNonMenz = new BeneficioCumuloModel();
String lCodBen = "";
boolean isConProvvedimento = false; 

if(lQuantiBenefici.equals("1") )
{
	if(Titolo.getBeneficioCumulato()!=null) {
		lBenMod = (BeneficioCumuloModel)Titolo.getBeneficioCumulato();
		lCodBen = lBenMod.getCodTipoBeneficio();
	}	
}
else if(lQuantiBenefici.equals("2"))
{
	lVecBen = new Vector<BeneficioCumuloModel>(Titolo.getBeneficiCumulo());
	Iterator itx = lVecBen.iterator();
	while(itx.hasNext())
	{
		lBenMod = (BeneficioCumuloModel)itx.next();
		if(lBenMod.getCodTipoBeneficio().equals("01"))
		{
			lSosp = lBenMod;
		}
		else if(lBenMod.getCodTipoBeneficio().equals("02"))
		{
			lNonMenz = lBenMod;
		}
	}
}
	

// Preparo i model con i Benefici concessi con Provvedimento
StatoEsecTitoloCumulatoModel lStatEsecMod = null;
ComputiCumuloModel lCompMod = null;
String lCodProv = "";

if(Titolo.getStatoEsecTitoloCumulato()!=null)
{
	isConProvvedimento = true;
	lStatEsecMod = (StatoEsecTitoloCumulatoModel) Titolo.getStatoEsecTitoloCumulato();
	
	if(lStatEsecMod!=null && lStatEsecMod.getIdStatoEsecTitoloCumulato()!=null) {
		if(lStatEsecMod.getListaComputi()!=null && lStatEsecMod.getListaComputi().size()>0) 
		{
			lCompMod = (ComputiCumuloModel) lStatEsecMod.getListaComputi().get(0);
			lCodBen =  lCompMod.getCodTipoAnnotazione();
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
   	 	//$('#divTitRev').show();
  	 	//$('#tabDatiTitRev').show();
   	 	
      <%
      String lStrParametri = "";
      lStrParametri +="&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo();
      lStrParametri +="&"+ICostantiRichiestePmInCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO+"="+Titolo.getIdTitoloCumulato().toString();
      %>
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActCaricaElencoTitoliPerIstruttorieRichieste&ParentFormName="+a_form_name+"&ParentFormType="+a_form_type+"<%=lStrParametri%>"		
                          , "Elenco_Titoli"
                          , "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=yes, width=990, height=400");
    } 
	//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    
    
    function SelezionaBeneficio (tipoBeneficio)
    {
      $('#tabBen_S').hide();    // desc
      $('#tabBen_NM').hide();   // desc
      $('#tabBen_I').hide();    // desc
      
      
      $('#tabBen_Revo').hide(); // Input solo SOSP COND
      $('#tabBen_Revo input').prop('disabled',true);
      $('#tabBen_Revo select').prop('disabled',true);
      
      $('#divQuantum').hide();  // Input solo Amnistia/Indulto
      $('#divQuantum input').prop('disabled',true);
      $('#divQuantum select').prop('disabled',true);
      
      
      if (tipoBeneficio=='S'){ // Solo SOSP COND
        $('#tabBen_S').show();
        $('#tabBen_Revo').show();
        $('#tabBen_Revo input').prop('disabled',false);
        $('#tabBen_Revo select').prop('disabled',false);
        $('#divAnticipazione').show();
        $('#divAnticipazione input').prop('disabled',false);
        $('#divAnticipazione select').prop('disabled',false);        
      }
      else if (tipoBeneficio=='NM'){ // Solo NON MENZ
        $('#tabBen_NM').show();
        $('#tabBen_Revo').show();
        $('#tabBen_Revo input').prop('disabled',false);
        $('#tabBen_Revo select').prop('disabled',false);
        $('#divAnticipazione').show();
        $('#divAnticipazione input').prop('disabled',false);
        $('#divAnticipazione select').prop('disabled',false);        
      }
      else if (tipoBeneficio=='I' || tipoBeneficio=='A' ){
        $('#tabBen_I').show();
        $('#divQuantum').show();
        $('#divQuantum input').prop('disabled',false);
        $('#divQuantum select').prop('disabled',false);        
      }
      else if (tipoBeneficio=='SNM'){ // Sospe Cond E Non Menz
        $('#tabBen_S').show();
        $('#tabBen_NM').show();
        $('#tabBen_Revo').show();
        $('#tabBen_Revo input').prop('disabled',false);
        $('#tabBen_Revo select').prop('disabled',false);
      }      
    }

    function SelezionaTitoliRevocante (idTitolo)
    {
      $('#divTitRev table').hide();
      $('#divTitRev table[id=tabTitRev_id_'+idTitolo+']').show();         
    }
    
    function caricatuttecombo()
    {
      var strOggetto = "<%=strOggetto%>";

      var articolo = $('#Articolo option:selected').text();
      
      caricaCombo(strOggetto,';','#',articolo,document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_MOTIVO%>);
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
       if (document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
         document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
       if (document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
         document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value;

       var data_to_verify = document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE%>.value;
       if (data_to_verify=='//' )
       {
           alert('Indicare la Data Richiesta');
           document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       }
       
       if (!ControllaData(data_to_verify) )
       {
           alert('Data Richiesta non valida');
           document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       }
       
       // Controllo inserimento estremi Sentenza di riferimento
	   // 03/05/2019 MEV70 Il suddetto controllo non va applicato per l'articolo 'Revoca Beneficio ex art. 165 c.p.p.'.
        var articolo_Revoca = document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_ARTICOLO%>.value;
   	 	var lCodBeneficio = '<%=lCodBen%>';
    	if (articolo_Revoca.length != 4  &&
       	 	lCodBeneficio!='002' &&  // 09/05/2019  Se il beneficio è un Indulto (Codice='002') o un Indulto in Sentenza (Codice '03') non è obbligatorio l'articolo di revoca.
       	 	lCodBeneficio!='03' ) { 
    		alert('Selezionare un articolo di Revoca');		
    	      return false;
    	}
		if (articolo_Revoca!='1108') {
       if(   document.RichGERevocaBen.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value == '' 
          || document.RichGERevocaBen.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value.length == 0 )
       {
          alert('Selezionare il Titolo Esecutivo di Riferimento che determina la revoca');
          return false;
       }       
		} else {
		     //alert ('articolo_Revoca = 1108. Non effettuo il controllo di selezione del Titolo Esecutivo');
		}

       var lCodBene = '<%=lCodBen%>';
	   
       	if(lCodBene == '' || lCodBene == '01')		// Sospensione
       	{	 
       		// controllo valore accoppiata Combo Articolo - Motivazione
       		if(document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_MOTIVO %>.value == '' || 
       	  		document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_MOTIVO %>.value.length == 0 )
       		{
         	 	alert('selezionare Articolo e motivazione');
             	document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_ARTICOLO%>.focus();
             	return false;
       		}	
       	}
 	 	
 	 	if(lCodBene != '' )
 	 	{
 	 		// Caso di Aministia / Indulto
 	 		if(lCodBene == '03' || lCodBene == '04' || lCodBene == '002' || lCodBene == '003' )
 	 		{	
 	 		 	// Quantum
 	 		 	var pos = document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT.value.indexOf('.');
				if(pos > 0)
				{
					alert('inserire correttamente il valore INTERO della Multa');
		        	document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT.focus();
		        	return false;
				}
				
				pos = document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT.value.indexOf('.');
				if(pos > 0)
				{
					alert('inserire correttamente il valore INTERO della Ammenda');
		        	document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT.focus();
		        	return false;
				}
 	 		 	
 	        	if(document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_R%>.value == ''  )
 	        	{
 	    			alert('digitare il segno + / - quantum pena');
 	             	document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_R%>.focus();
 	             	return false;
 	        	}
 	        
	 	        if( ( document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>.value == '' ||
	 	       		  document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>.value.length == 0 )  &&
	 	       	    ( document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_R%>.value == '' ||
	 	              document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_R%>.value.length == 0  ) &&	
	 	            ( document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_R%>.value == '' ||
	 	              document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_R%>.value.length == 0 ) &&
	 	            	 
	 	            ( document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_R%>.value == '' ||
	 	           	  document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_R%>.value.length == 0 ) &&
	 	            ( document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_R%>.value == '' ||
	 	              document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_R%>.value.length == 0 ) &&
	 	            ( document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_R%>.value == '' ||
	 	              document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_R%>.value.length == 0 ) &&	
	 	              
	 	            ( document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT.value.length == 0 ) &&
	 	            ( document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>DEC.value.length == 0 ) &&	
	 	            ( document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT.value.length == 0 ) &&
	 	            ( document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>DEC.value.length == 0 ) 
	 	          )	
	 	        {
	 	       	 	alert('digitare una quantità di pena da Revocare');
	 	           	document.RichGERevocaBen.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>.focus();
	 	           	return false;
	 	        }
  	 		}
 	 	}
      
       return true; 
    }
    
    function controlla()
    {
   	 	var lCod = '<%=lCodBen%>';
   	 	//alert('controlla - lCod = '+lCod);
   	 	if(lCod=="")
   	 	{
   	 		// Caso di Sospensione/Non Menzione
   	 		SelezionaBeneficio("SNM");
   	 	}
   	 	else
   	 	{
   	 		// Caso di 1 solo Beneficio
   	 		if(lCod=="01")
   	 		{
   	 			SelezionaBeneficio("S");
   	 		}
   	 		else if(lCod=="02")
   	 		{
   	 			SelezionaBeneficio("NM");
   	 		}
   	 		else if(lCod=="03")
   	 		{
   	 			SelezionaBeneficio("I");
   	 		}
   	 		else if(lCod=="04")
   	 		{
   	 			SelezionaBeneficio("A");
   	 		}
   	 		else if(lCod=="002") // Cod_Annotazione : Provvedimento Indulto
   	 		{
   	 			SelezionaBeneficio("I");
   	 		}
   	 		else if(lCod=="003") // Cod_Annotazione : Provvedimento Amnistia
   	 		{
   	 			SelezionaBeneficio("A");
   	 		}
   	 	}
   	 	
   	 	//return false;
    }

  </script>
</head>

<body class="corpo" onload="controlla();">
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
 
 
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="RichGERevocaBen">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRichiestaGERevocaBenefici">
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" 		value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO %>" 	value="<%=Titolo.getIdTitoloCumulato()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA %>" 		value="01">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>" 	value="021">
  <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO %>"  			value="<%=lCodBen%>">

<% 	if(lQuantiBenefici.equals("1"))
  	{ 
  		if(isConProvvedimento) { %>
  			<input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>" 			value="<%=lCompMod.getIdComputiCumulo()%>">
  			<input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_STATOESEC_CUM_SEL%>" 	value="<%=lStatEsecMod.getIdStatoEsecTitoloCumulato()%>">
<%		} else {	 %>
  			<input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_BENEFICIO_CUM_SEL_1%>" 	value="<%=lBenMod.getIdBeneficioCumulo()%>">
<%		}
  	}	
  	else if(lQuantiBenefici.equals("2"))
  	{	%>
  		<input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_BENEFICIO_CUM_SEL_1%>" 	value="<%=lSosp.getIdBeneficioCumulo()%>">	
  		<input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_BENEFICIO_CUM_SEL_2%>" 	value="<%=lNonMenz.getIdBeneficioCumulo() %>">
<%	} %>  
  
  <%//	 Campi Hidden valorizzati dalla Popup  %>
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO %>"	value="" >
  
<!-- 					DATI SUI BENEFICI DA REVOCARE							 -->  
  <table width="95%" align="center">
    <tr><td colspan="5" class="Titolonocap">Beneficio da Revocare</td></tr>
  </table>  
      
  <div id="idTitolo_"  style="display:block" >

<%
//  Sospensione Condizionale
	if(lQuantiBenefici.equals("1"))
	{
		if("01".equals(lCodBen))
		{	
			lSosp = lBenMod; 
		}	
	}
%>
    <table width="95%" align="center" style="display:none" id="tabBen_S">
      <tr>
        <td class="c">Tipo Beneficio</td>
        <td class="c">Natura Beneficio</td>
        <td class="c">Durata sospensione</td>
      </tr>
 
      <tr>
        <td class="l" nowrap><font class="campo"><%=StringUtils.toStringJSP(lSosp.getDescrTipoBeneficio(),"") %></font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lSosp.getDescrSottotipoBeneficio(), "") %></font></td>
        <td class="l"><font class="campo">Anni:&nbsp;<%=StringUtils.toStringJSP(lSosp.getNumAnniSospensione(), "-" ) %></font></td>
      </tr>
    </table>

<%
//  Non Menzione
	if(lQuantiBenefici.equals("1"))
	{
		if("02".equals(lCodBen))
		{	
			lNonMenz = lBenMod; 
		}	
	}
%>
	<table width="95%" align="center" style="display:none" id="tabBen_NM">
	  <tr>
	  	<td class="l" width="150px"><font class="campo"><%=StringUtils.toStringJSP(lNonMenz.getDescrTipoBeneficio(),"") %></font></td>
	  	<td class="l" colspan="2"><img src="/images/V.gif"> </td>
	  </tr>
	</table> 

<%
//  Indulto / Amnistia
%>
    <table width="95%" align="center" style="display:none" id="tabBen_I">
      <tr>
        <td class="c">Tipo Beneficio</td>
        <td class="c">Provvedimento di Concessione</td>
        <td class="c">Concesso Nella misura di</td>
      </tr>
      
      <tr>
<%	if(isConProvvedimento) {  	 %>      
        <td class="l"><font class="campoLow"><%=StringUtils.toStringJSP(lCompMod.getDescrTipoAnnotazione(),"") %> </font></td>
        <td class="l"><font class="campoLow"><%=StringUtils.toStringJSP(lCompMod.getDescDpr() ,"") %></font></td>
        <td class="l">
      
        <%  if( !lCompMod.isQuantumReclusioneZero() ) { %>
        <font class="campoLow">Reclusione&nbsp;&nbsp;Anni: <%=StringUtils.toStringJSP(lCompMod.getNumAnniReclusione(), "0") %>&nbsp;
                              mesi: <%=StringUtils.toStringJSP(lCompMod.getNumMesiReclusione(), "0") %>&nbsp;
                              Giorni: <%=StringUtils.toStringJSP(lCompMod.getNumGiorniReclusione(), "0") %>&nbsp;
          </font>&nbsp;
        <% } %>
  
        <% if( lCompMod.getImportoMulta() != null && lCompMod.getImportoMulta().compareTo(BigDecimal.ZERO) > 0  ){ %>
          <font class="campoLow">Multa:&nbsp;<%=StringUtils.toStringJSP(lCompMod.getImportoMulta(), "-") %>&nbsp;&euro;</font>               
        <%  } %>
    

        <% if( lCompMod.isQuantumArrestoZero()) { %>
        <font class="campoLow">Arresto&nbsp;&nbsp;Anni: <%=StringUtils.toStringJSP(lCompMod.getNumAnniArresto(), "0") %>&nbsp;
                              mesi: <%=StringUtils.toStringJSP(lCompMod.getNumMesiArresto(), "0") %>&nbsp;
                              Giorni: <%=StringUtils.toStringJSP(lCompMod.getNumGiorniArresto(), "0") %>&nbsp;
          </font>&nbsp;
        <% } %>   

        <% if( lCompMod.getImportoAmmenda() != null && lCompMod.getImportoAmmenda().compareTo(BigDecimal.ZERO) > 0 ) { %>
          <font class="campoLow">Ammenda:&nbsp;<%=StringUtils.toStringJSP(lCompMod.getImportoAmmenda(), "-") %>&nbsp;&euro;</font>               
        <% } %> 

        </td>
        
<%	} else {	 %>        

        <td class="l"><font class="campoLow"><%=StringUtils.toStringJSP(lBenMod.getDescrTipoBeneficio(),"") %> </font></td>
        <td class="l"><font class="campoLow"><%=StringUtils.toStringJSP(lBenMod.getDescrDpr() ,"") %></font></td>
        <td class="l">
      
        <%  if( !lBenMod.isQuantumReclusioneZero() ) { %>
        <font class="campoLow">Reclusione&nbsp;&nbsp;Anni: <%=StringUtils.toStringJSP(lBenMod.getNumAnniReclusione(), "0") %>&nbsp;
                              mesi: <%=StringUtils.toStringJSP(lBenMod.getNumMesiReclusione(), "0") %>&nbsp;
                              Giorni: <%=StringUtils.toStringJSP(lBenMod.getNumGiorniReclusione(), "0") %>&nbsp;
          </font>&nbsp;
        <% } %>
  
        <% if( lBenMod.getImportoMulta() != null && lBenMod.getImportoMulta().compareTo(BigDecimal.ZERO) > 0  ){ %>
          <font class="campoLow">Multa:&nbsp;<%=StringUtils.toStringJSP(lBenMod.getImportoMulta(), "-") %>&nbsp;&euro;</font>               
        <%  } %>
    

        <% if( lBenMod.isQuantumArrestoZero()) { %>
        <font class="campoLow">Arresto&nbsp;&nbsp;Anni: <%=StringUtils.toStringJSP(lBenMod.getNumAnniArresto(), "0") %>&nbsp;
                              mesi: <%=StringUtils.toStringJSP(lBenMod.getNumMesiArresto(), "0") %>&nbsp;
                              Giorni: <%=StringUtils.toStringJSP(lBenMod.getNumGiorniArresto(), "0") %>&nbsp;
          </font>&nbsp;
        <% } %>   

        <% if( lBenMod.getImportoAmmenda() != null && lBenMod.getImportoAmmenda().compareTo(BigDecimal.ZERO) > 0 ) { %>
          <font class="campoLow">Ammenda:&nbsp;<%=StringUtils.toStringJSP(lBenMod.getImportoAmmenda(), "-") %>&nbsp;&euro;</font>               
        <% } %> 

        </td>
<%	} %>        
      </tr>
    </table>

<!-- 			 	TITOLO 	SU	CUI	è  STATO   CONCESSO  IL  BENEFICIO					-->    
    <table width="95%" align="center" style="display:block" id="tabTitoloConc">
      <tr><td colspan="8" class="Titolonocap">Concesso sul Titolo</td></tr>
<%
	String AnnoNumero = Titolo.getAnnoSentenza() +"/"+Titolo.getNumeroSentenza();
%>      
      <tr>
        <td class="l" colspan="8">
          <font class="label"><%=StringUtils.toStringJSP(Titolo.getDescrTipoProvvedimento() )%>&nbsp;N. </font>
          <font class="campo"><%=AnnoNumero%></font>&nbsp;
          &nbsp;<font class="label"> del </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Titolo.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
          &nbsp;<font class="label"> Emessa da </font>
          <font class="campo"><%=StringUtils.toStringJSP(Titolo.getDescrTipoAutoritaEmittente(), "") %></font>
          <font class="label">&nbsp;di&nbsp; </font>
          <font class="campo"><%=StringUtils.toStringJSP(Titolo.getDescrLuogoEmittente(), "") %></font>

          &nbsp;<font class="label"> Irrevocabile il  </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Titolo.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
        </td>
      </tr>

    </table>
  </div>
  
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
        <a href="Javascript:ElencoTitoliPopup('RichGERevocaBen','<%=ICostantiRichiestePmInCumulo.FORM_TYPE_RIC_REV_BENEFICI%>');">
          Revocato in relazione al Titolo Esecutivo <img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>
  </table> 

  <div id="divTitRev" style="display:block">
    <table width="95%" align="center" id="tabDatiTitRev" style="display:none">
      <tr>
        <td class="l" width="15%" >Estremi Titolo </td>
        <td class="L" id="descTitoloRevocante">&nbsp;</td>
      </tr>
    </table>
  </div>

    <table width="95%" align="center" style="display:none" id="tabBen_Revo">  
      <tr>
        <td colspan="100%">
          <table width="100%">
            <tr><td class="Titolo" colspan=4>Revoca</td></tr>
            <tr>
              <td class="l">Articolo</td>
              <td class="l">
                <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_ARTICOLO %>" 
                        id="Articolo"
                        Title="Tipo Provvedimento" 
                        onchange="javascript:caricatuttecombo()">
                  <option value ="">-</option> 
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
 %>
			       <option value = <%=lDecMod.getCode()%> ><%=lDecMod.getFiltro()%></option> 
 <%
			   		}
			     }
			  }
 %>
                </select>
          </td>
    </tr>
    <tr>
      <td class="l" style="width:85px">Motivazione</td>
      <td class="l">
            <select class="small"  name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_MOTIVO %>" Title="Tipo Provvedimento" >
        
            </select>
      </td>
    </tr>     
  </table>
</td>
</tr>      
</table>

  
  <br>
  <table width="95%" align="center">
    <tr>
      <td class="l" width="15%" >Data Richiesta </td>
      <td class="L" >
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l" width="15%" >Motivazioni </td>  	
      <td class="c" style="text-align:left">
        <textarea cols="100" rows="4" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI%>"></textarea>
      </td>
    </tr>
  </table>
  
  <%
  //============================================================================
  // DIV da visualizzre solo nel caso di Revoca Amnistia/Indulto
  //============================================================================
  %>
  <div id="divQuantum"  style="display:none" >
    <br>
    <table width="95%" align="center" >
      <tr>
        <td class="Titolonocap" colspan="3" >Beneficio da Revocare nella misura di</td>
      </tr>
    </table> 
  
    <table width="95%" align="center" >
      <tr>
        <td colspan=6>
          <hr width="100%">
        </td>
      </tr>
      <tr>
        <td valign="middle" class="c" rowspan=2>+/- <font class="ob">(*)</font><br>
          <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_R %>" id="PiuMeno">
            <option value=""></option>
            <option value="+">+</option>
            <option value="-">-</option>
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
    
    <table width="95%" align="center" >
      <tr>
        <td class="l">
          Anticipazione degli effetti&nbsp;&nbsp;
        
          <input type="checkbox" name="FlagAppProvvisoria" value="A"  >
        </td>
      </tr>
    </table>
  </div>
 
  <br>
  
  <%
  //============================================================================
  // DIV da visualizzare nei casi di Sospensione Condizionale o Non Menzione
  //============================================================================
  %>
  <div id="divAnticipazione"  style="display:none" >
    <br>
    
    <table width="95%" align="center" >
      <tr>
        <td class="l">
          Anticipazione degli effetti&nbsp;&nbsp;
        
          <input type="checkbox" name="FlagAnticipazione" value="A"  >
        </td>
      </tr>
    </table>
  </div>
 
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
  var frmvalidator  = new Validator("RichGERevocaBen");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
 
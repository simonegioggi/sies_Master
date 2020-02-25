<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.reato.action.ICostantiReato"%>

<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>

<jsp:useBean id="IstruttoriaCumulo"     scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"        scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="aProvvedimento" 		scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoProvvedimento" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioEmittente" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="ltipoSospensione" 		scope="request" class="java.lang.String"/>

<% 

//============================================================================== 
// Form per l'inserimento e la modifica dei Provvedimenti di annotazione 
// Interruzione
//============================================================================== 
	if ( modalita.equals("M") ) {
  		//aComputo = aProvvedimento.getListaComputi().elementAt(0);
	}
	
	String strCheckedSosp = "";
	String strCheckedMenz = "";
	if ( modalita.equals("M") ) {
		if(aProvvedimento.getFlagStato().equals("I")) {
			if (aProvvedimento.getFlagTipoSosp() != null ) {
			    if(aProvvedimento.getFlagTipoSosp().compareTo("S")==0 ) {
			  		strCheckedSosp="checked";
			    } else if(aProvvedimento.getFlagTipoSosp().compareTo("M")==0 ) {
			  		strCheckedMenz="checked";
				} else if(aProvvedimento.getFlagTipoSosp().compareTo("SM")==0 ) {
					 strCheckedSosp="checked";
				  	 strCheckedMenz="checked";
				}		
			}
		} else if(aProvvedimento.getFlagStato().equals("E")) {	
			if(aProvvedimento.getCodMotivo().equals("0818")) {
				strCheckedSosp="checked";
   	  		} else if(aProvvedimento.getCodMotivo().equals("0822")) {
   	  			strCheckedMenz="checked";
   	  		}
		}
		
	} else if ( modalita.equals("I") ) {
		
		if(ltipoSospensione.compareTo("S")==0 ) {
	  		strCheckedSosp="checked";
	    } else if(ltipoSospensione.compareTo("M")==0 ) {
	  		strCheckedMenz="checked";
		} else if(ltipoSospensione.compareTo("SM")==0 ) {
			 strCheckedSosp="checked";
		  	 strCheckedMenz="checked";
		}
	}
%> 

<html>
<head>
  <title> Gestione Attività de GE - Cumulo</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>

  <script language="JavaScript" >
    var desktop;
  
      function eseguiFunzione(action)
      {
        document.f.<%=IWebConstants.ACTION_FIELD%>.value = action;
        document.f.submit();
      }

    function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  

	function Verify()
    {
     	// Tipo Provvedimento
      	if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value == "-") {
      		alert('Selezionare il tipo di Provvedimento');
	       	document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO %>.focus();
	       	return false;	
      	}

	    //DATA EMISSIONE PROVVEDIMENTO (obbligatoria)
	    if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value.length==1)
	        document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value;
	    if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value.length==1)
	        document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value;
	
	    var data_to_verify = document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value;
	
	    //if (!ControllaDataPassaVuota(data_to_verify) )
	   	if (!ControllaData(data_to_verify) )
	    {
	       	alert('Data emissione provvedimento non valida');
	       	document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO %>.focus();
	      	return false;
	    }
	        
	    // Anno e Numero Provvedimento
     	if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>.value == "" || 
     		document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>.value == "" ) 
     	{
     		alert('Valorizzare Anno e Numero Provvedimento');
	       	document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO %>.focus();
	       	return false;	
     	}
	     
     	// Tipo e luogo Autorità 
   		if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_UFFICIO_EMITTENTE%>.value == "-") {
   			alert('Selezionare il tipo di Ufficio Emittente');
          	document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_UFFICIO_EMITTENTE %>.focus();
          	return false;	
   		}
     	
   		if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>.value == "") {
   			alert('Selezionare il Luogo Ufficio Emittente');
          	document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>.focus();
          	return false;	
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
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <% if( modalita.equals("I") ) { %>
        <font class="campo">Inserimento Revoca Benefici concessi nel Titolo &nbsp;</font>
        <% } else if( modalita.equals("M") || modalita.equals("NP") ) { %>
        <font class="campo">Modifica Revoca Benefici concessi nel Titolo &nbsp;</font>
        <%}%>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaAnnotazioneRevocaBeneficioCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
  	<jsp:include page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>" />
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciAnnotazioneRevocaBeneficioCumulo">
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  <input type="HIDDEN" name="operazione" value="">
  
  <input type="HIDDEN" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="HIDDEN" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(aProvvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>" 					  value="<%=StringUtils.toStringJSP(aProvvedimento.getFlagStato()) %>">



  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td colspan="4" class="titolo"> Tipo  Beneficio  Revocato</td>
    </tr>
    
  	<tr>
      <td class="l" width="30%" >&nbsp;&nbsp; Sospensione Condizionale </td>
      <td class="l"> &nbsp;&nbsp;
     	<input type="checkbox" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_CHECK_SOSPENSIONE%>" 
		    	id="idFlagTrib" <%=strCheckedSosp%> value="S"  >
	  </td>
	</tr> 
	 
	<tr>
	  <td class="l" width="30%" >&nbsp;&nbsp; Non  Menzione </td>
	  <td class="l"> &nbsp;&nbsp; 	    								
		<input type="checkbox" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_CHECK_NON_MENZIONE%>"
		    	id="idFlagTribMin" <%=strCheckedMenz%> value="M"  >
	  </td>
	</tr>
 </table>
 
 <table cellspacing="2" cellpadding="2" width="95%" align="center">	
    <tr>
      <td colspan="4" class="titolo">Estremi del provvedimento di Revoca del Giudice dell'esecuzione della pena</td>
    </tr>
    
    <tr>
      <td class="l" width="15%">
        Tipo Provvedimento <font class="ob">(*)</font>
      </td>
      <td class="l">
        <select Title="Tipo Provvedimento"  name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>">
        <%=tipoProvvedimento%>
        </select>
      </td>
    </tr>
    
    <tr>
      <td class="l" width="15%">
        Data Provvedimento <font class="ob">(*)</font>
      </td>
      <td class="l">
        <input type="text" Title="Giorno Provvedimento" 
          name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO %>" maxlength="2" size="2"  
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"dd"))%>"  
          <%=IWebConstants.UTIL_DATA%>>
        /
        <input type="text" Title="Mese Provvedimento"
          name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO %>"
          maxlength="2" size="2"
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"MM"))%>"
          <%=IWebConstants.UTIL_DATA%>> /
        <input type="text" Title="Anno Provvedimento" 
          name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO %>" maxlength="4" size="4"  
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"yyyy"))%>"  
          <%=IWebConstants.UTIL_DATA_ANNO%>>
      </td>
      
      <td class="l" width="15%">
        Anno e Numero Provvedimento <font class="ob">(*)</font>
      </td>
      <td class="l">
      	<input type="text" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>" size=4 maxlength=4 
			value="<%=StringUtils.toStringJSP (aProvvedimento.getAnnoProvvedimento() )%>" Title="Anno Provvedimento"     
      	<%=IWebConstants.UTIL_DATA_ANNO%> >
      	/
      	<input type="text" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>" size=8 maxlength=38 
			value="<%=StringUtils.toStringJSP (aProvvedimento.getProgrProvvedimento() )%>" Title="Numero Provvedimento"     
      		onkeypress="return TicTabNumField(this,event)" >
      </td>
    </tr>

  <tr>
	<td class="l" width="15%">
        Pronunciata da <font class="ob">(*)</font>
    </td>
    <td class="l">
      <select Title="Tipo Ufficio Emittente"  name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_UFFICIO_EMITTENTE%>">
      	<%=tipoUfficioEmittente%> 
      </select>
    </td>
    
    <td class="l" width="15%">
        Luogo <font class="ob">(*)</font>
    </td>
    <td class="l">
        <font class="campo">
          <input Title="Luogo Ufficio Emittente" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>" 
         	value="<%=StringUtils.toStringJSP (aProvvedimento.getDescrLuogoEmittente() )%>"  size=35 type="text">
          <a href="Javascript:ListaUfficiComuni('f','<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>'
          ,document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_UFFICIO_EMITTENTE%>[document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_UFFICIO_EMITTENTE%>.options.selectedIndex].value);">
            <img src="/images/filefolder.gif" border=0>
          </a>
          
        </font>
    </td>
  </tr>
  
  <tr>
 	<td class="l" width="15%"> sezione </td>
    <td class="l">
      <input type="text" Title="sezione Ufficio Emittente"  name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_SEZIONE_ALTRO%>"
      		 value="<%=StringUtils.toStringJSP (aProvvedimento.getSezioneAltro() )%>" >
    </td>
  </tr>

  <tr>
    <td class="l" width="15%"> Motivo </td>
    <td class="l" colspan="3">
      <textarea cols="70" rows="3" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TEXT_AREA_MOTIVO%>"><%=StringUtils.toStringJSP(aProvvedimento.getNote() )%></textarea>
    </td>
  </tr>
  </table>

  <table cellspacing="2" cellpadding="2" width="95%" align="center">
  	<tr><td>&nbsp;</td></tr>
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>
  </table>

</form>

<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("f");

// Data emissione provvedimento
frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");
frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","gt=1");
frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","lt=31");

frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");
frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","gt=1");
frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","lt=12");

frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno emissione provvedimento è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno emissione provvedimento è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");
frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","gt=1900");
frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","lt=3000");

frmvalidator.setAddnlValidationFunction("Verify");
</script>

</body>
</html>
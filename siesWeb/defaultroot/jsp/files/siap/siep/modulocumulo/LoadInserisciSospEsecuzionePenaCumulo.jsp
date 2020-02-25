<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       	scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="aProvvedimento" 		scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="aIdComputo" 			scope="request" class="java.lang.String"/>

<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="luogoUfficioEmittente" scope="request" class="java.lang.String"/>

<jsp:useBean id="tipoProvvedimento" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="oggettoProvvedimento" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioEmittente" 	scope="request" class="java.lang.String"/>

<jsp:useBean id="lPenaResidua" 			scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<% 

//============================================================================== 
// Form per l'inserimento e la modifica delle Sosp. Misure Alternative - 
// Provvedimenti della Sorveglianza - modulo cumulo. 
//============================================================================== 
ComputiCumuloModel aComputo = new ComputiCumuloModel();
Vector <ComputiCumuloModel> lListaComputi = aProvvedimento.getListaComputi();
if ( modalita.equals("M") )
{
  Iterator itxComputi = lListaComputi.iterator();
  while ( itxComputi.hasNext()) 
  {
    ComputiCumuloModel lComputo = (ComputiCumuloModel) itxComputi.next();
    BigDecimal lIdCompDaModificare = new BigDecimal (aIdComputo);
    if (lComputo.getIdComputiCumulo().compareTo(lIdCompDaModificare)==0){
      aComputo = lComputo;
      break;
    }
  }
} 

%> 

<html>
<head>
  <title> Gestione Provvedimento Sospensione Esecuzione Misure </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>

  <script language="JavaScript" src="/html/jsrsClient.js"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_JQUERY%>></script>  

  <script language="JavaScript" >
	var desktop;
	
    function ListaComuniperTipoUfficio(a_formname,a_fieldname,codTipoUfficio)
    {
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function eseguiFunzione(action)
    {
      document.f.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.f.submit();
    }
	
    function showDivForOggetto()
    {
      var codOggetto = document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.value ;
      var arrayEmpty = ["-"];
      var arrayDataScarcerazione = [ "2000","2001","2480"];


      if ($.inArray(codOggetto, arrayDataScarcerazione)>-1) {
        $('#divDataScarcerazione').show();
      }
      else {
        $('#divDataScarcerazione').hide();
      }
    }
	
    function Verify()
    {
        //DATA EMISSIONE PROVVEDIMENTO (obbligatoria)
        if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
            document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
        if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
            document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) ||
        	data_to_verify.length < 10 )
        {
          alert('Data emissione provvedimento non valida');
          document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE %>.focus();

          return false;
        }

        if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>[document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.selectedIndex].value == '-')
        {
          alert("Il Campo Tipo Provvedimento è obbligatorio");
          return false;
        }

        // Anno e Numero Provvedimento Obbligatori.
        if( document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO %>.value.length == 0)
        {
          alert("Valorizzare Anno Provvedimento");
          document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO %>.focus();

          return false;
        }
        
        if(document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO %>.value.length == 0)
        {
          alert("Valorizzare Numero Provvedimento");
          document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO %>.focus();

          return false;
        }

        if (document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>[document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value=="-")
        {
          alert("Selezionare l'Autorità Emittente");
          document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.focus();
          return false;
        }
        
        if(document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>.value.length == 0)
        {
          alert("Valorizzare la sede Autorità Emittente");
          document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>.focus();

          return false;
        }

        // Controllo Combo Oggetto Procedimento e data scarcerazione.
        if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>[document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.selectedIndex].value == '-')
        {
          alert("Il Campo Oggetto Procedimento è obbligatorio");
          return false;
        }
        
        if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>[document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.selectedIndex].value != '2297')
		{        
	        if (document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value.length==1)
	            document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value='0'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value;
	        if (document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value.length==1)
	            document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value='0'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value;
	
	        var data_to_verify = document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value+'/'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value+'/'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value;
	
	        if (!ControllaDataPassaVuota(data_to_verify) ||
	        	data_to_verify.length < 10 )
	        {
	          alert('Data sospensione esecuzione non valida');
	          document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE %>.focus();
	
	          return false;
	        }
        }        
      return true;
      }
  </script>

</head>

<body class="corpo" onLoad="showDivForOggetto();" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <% if( modalita.equals("I") ) { %>
        <font class="campo">Inserimento sospensione Esecuzione Pena &nbsp;</font>
        <% } else if( modalita.equals("M") || modalita.equals("NP") ) { %>
        <font class="campo">Modifica Sospensione Esecuzione Pena &nbsp;</font>
        <%}%>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaSospEsecuzionePenaCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciSospEsecuzionePenaCumulo">
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  
  <input type="HIDDEN" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="HIDDEN" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(aProvvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="HIDDEN" name="<%= ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO %>"                       value="<%=StringUtils.toStringJSP(aComputo.getIdComputiCumulo()) %>">
  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>" 					  value="<%=StringUtils.toStringJSP(aProvvedimento.getFlagStato()) %>">


  <table width="95%" align="center">
    <tr>
      <td colspan="4" class="titolo">Dati del provvedimento </td>
    </tr>
    <tr>
      <td class="l" width="240" >
        Data emissione provvedimento <font class="ob">(*)</font>
      </td>
      <td class="l">
        <input type="text" Title="Giorno emissione provvedimento" 
          			name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        <input type="text" Title="Mese emissione provvedimento"
					name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        <input type="text" Title="Anno emissione provvedimento" 
          			name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
      </td>
      <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
      <td class="l" colspan="1">
        <select  Title="Tipo Provvedimento" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO %>" >
          <%=tipoProvvedimento%>
        </select>
      </td>
	</tr>
	
	<tr>
	  <td class="l">
	  	Anno / Numero provvedimento <font class="ob">(*)</font> :</td>
	  <td class="l">
	  	<input type="text" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>" size=4 maxlength=4 
			value="<%=StringUtils.toStringJSP (aComputo.getAnnoProvv() )%>"      
	 				<%=IWebConstants.UTIL_DATA_ANNO%> >
	    		/
	    	<input type="text" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>" size=8 maxlength=38 
		value="<%=StringUtils.toStringJSP (aComputo.getProgrProvv() )%>"      
	    		onkeypress="return TicTabNumField(this,event)" >
	    </td>
	    <td class="l">Anno / Numero SIUS </td>
	    <td class="l">
	    	<input type="text" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>" size=4 maxlength=4 
				value="<%=StringUtils.toStringJSP (aComputo.getAnnoProc() )%>"      
	    		<%=IWebConstants.UTIL_DATA_ANNO%> >
	    		/
	    	<input type="text" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>" size=8 maxlength=38 
				value="<%=StringUtils.toStringJSP (aComputo.getProgrProc() )%>"      
	    		onkeypress="return TicTabNumField(this,event)" >
	  </td>
	</tr>     
	
	<tr>
		<td class="l">Autorità Emittente <font class="ob">(*)</font> </td>	  
	    <td class="L">
	      	<select Title="Autorità Emittente" name="<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
	        <%=tipoUfficioEmittente%>
	      	</select>
	    </td>
	    <td class="l">Sede <font class="ob">(*)</font> </td>
	    <td class="L" nowrap>
	      	<input Title="Sede Autorità Emittente" name="<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%= aProvvedimento.getDescrLuogoEmittente()%>" type="text" maxlength="35" size="35" class="small"> 
	        <a href="Javascript:ListaComuniperTipoUfficio('f','<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>',document.f.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>[document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
	          <img src="/images/filefolder.gif" border=0> 
	        </a>
	    </td>
	</tr>
	
	<tr>
	    <td class="l">Oggetto Procedimento <font class=ob>(*)</font></td>
	    <td class="l" colspan="3">
	      <select  Title="Oggetto Provvedimento" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO %>" onChange="Javascript:showDivForOggetto();" >
	        <%=oggettoProvvedimento%>
	      </select>
	    </td>
	    
	</tr>     

  </table>

  <table width="95%" align="center">

	<div id="blank" style="display:block;">
	  <table width="95%" align="center" >
	  <tr>
	   </tr>
	  </table>
	</div>  

	<div id="divDataScarcerazione" style="display:block;">
	  <table width="95%" align="center" >

  		<tr>
		    <td class="l" width="240" >
		        Data Decorrenza Pena <font class=ob>(*)</font>
		    </td>
		    <td class="l">
<%			if(lPenaResidua!=null && lPenaResidua.getIdPenaResidua()!=null )	{ %>  		    
         		<input type="text" Title="Giorno Decorrenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA %>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataInizio(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Mese Decorrenza Pena"
					name="<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_DA %>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataInizio(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Anno Decorrenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_DA %>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataInizio(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
<%			} else { %>  
				<input type="text" Title="Giorno Decorrenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA %>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneDa(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Mese Decorrenza Pena"
					name="<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_DA %>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneDa(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Anno Decorrenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_DA %>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneDa(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
<%			} %>        			
          	</td>

			<td class="l" width="240" >
		        Data Scadenza Pena <font class=ob>(*)</font>
		    </td>
		    <td class="l">
<%			if(lPenaResidua!=null && lPenaResidua.getIdPenaResidua()!=null )	{ %> 		    
		        <input type="text" Title="Giorno Scadenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A %>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataFine(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Mese Scadenza Pena"
					name="<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_A %>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataFine(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Anno Scadenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_A %>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataFine(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
<%			} else { %>
				<input type="text" Title="Giorno Scadenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A %>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneA(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Mese Scadenza Pena"
					name="<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_A %>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneA(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Anno Scadenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_A %>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneA(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
<%			} %>          			
		    </td>
		</tr>

  		<tr>
		    <td class="l" width="240" >
		        Data Sospensione Esecuzione <font class=ob>(*)</font></td>

		    <td class="l" colspan="3">
		        <input type="text" Title="Giorno data Sospensione Esecuzione" 
		          			name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" maxlength="2" size="2"  
		          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataScarcerazione(),"dd"))%>"  
		          			<%=IWebConstants.UTIL_DATA%>> /
		        <input type="text" Title="Mese data Sospensione Esecuzione"
							name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>"
							maxlength="2" size="2"
							value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataScarcerazione(),"MM"))%>"
							<%=IWebConstants.UTIL_DATA%>> /
		        <input type="text" Title="Anno data Sospensione Esecuzione" 
		          			name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>" maxlength="4" size="4"  
		          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataScarcerazione(),"yyyy"))%>"  
		          			<%=IWebConstants.UTIL_DATA_ANNO%>>
		    </td>
		  </tr>
	  </table>
	</div>
	  
</table>
<br>

  <table cellspacing="2" cellpadding="2" width="95%" >
  <tr><td>&nbsp;</td></tr>
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>
  </table>

</form>
</body>
</html>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");
	
    // Data emissione provvedimento
    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>","minlen=1","La lunghezza minima per il giorno emissione provvedimento è di 1 carattere");
	frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
	frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
	frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");
	
	frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
	frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
	frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");
	
	frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>","maxlen=4","La lunghezza massima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza minima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>","lt=3000");


    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
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

<jsp:useBean id="tipoProvvedimento" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="oggettoProvvedimento" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioEmittente" 	scope="request" class="java.lang.String"/>

<jsp:useBean id="aComputoDate" 			scope="request" class="siap.siep.modulocumulo.model.ComputiCumuloModel"/>

<% 

//============================================================================== 
// Form per l'inserimento e la modifica delle Revoche Misure Alternative - 
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
  <title> Gestione Provvedimento Revoca Misure Alternative </title>
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
      var arrayQuantum = [ "0086","0014", "0015", "0196", "0316", "2640"];

      if ($.inArray(codOggetto, arrayQuantum)>-1) {
        $('#divQuantum').show();
        $('#divQuantum input[type=text]').prop('disabled',false);
      }
      else {
        $('#divQuantum').hide();
        $('#divQuantum input[type=text]').prop('disabled',true);
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

        // Controllo Combo Oggetto Procedimento.
        if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>[document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.selectedIndex].value == '-')
        {
          alert("Il Campo Oggetto Procedimento è obbligatorio");
          return false;
        }
        
        if(document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>.value.length == 0)
        {
          alert("Valorizzare la sede Autorità Emittente");
          document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>.focus();

          return false;
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
        <font class="campo">Inserimento revoca Misure Alternative &nbsp;</font>
        <% } else if( modalita.equals("M") || modalita.equals("NP") ) { %>
        <font class="campo">Modifica revoca Misure Alternative &nbsp;</font>
        <%}%>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaRevocheMisureAlternativeCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRevocaMisuraAlternativaCumulo">
  
  <input type="HIDDEN" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="HIDDEN" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(aProvvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="HIDDEN" name="<%= ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO %>"                       value="<%=StringUtils.toStringJSP(aComputo.getIdComputiCumulo()) %>">
  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>" 					  value="<%=StringUtils.toStringJSP(aProvvedimento.getFlagStato()) %>">

  <input type="HIDDEN" name="modalita" value="<%=modalita%>">

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

    <tr>
      <td class="l" width="240" >
        Data revoca
      </td>
      <td class="l" colspan="3">
        <input type="text" Title="Giorno inizio revoca" 
          			name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataInizioRevoca(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        <input type="text" Title="Mese inizio revoca"
					name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataInizioRevoca(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        <input type="text" Title="Anno inizio revoca" 
          			name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA%>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataInizioRevoca(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
      </td>

  </table>

  <table width="95%" align="center">

	<div id="blank" style="display:block;">
	  <table width="95%" align="center" >
	  <tr>
	   </tr>
	  </table>
	</div>  

	<div id="divQuantum" style="display:block;">
	  <table width="95%" align="center" >

  		<tr>
		    <td class="l" width="240"></td>
  			<td class="titolo">Reclusione</td>
  			<td class="titolo">Arresto</td>
  		</tr>
  		<tr>
		    <td class="l" width="240" >
		        Residuo pena da espiare
		    </td>
      		<td class="c">
        		<font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
        		<font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
        		<font class="label">Giorni</font><br>
				<input type="text" maxlength="2" size="2"
             		value="<%=StringUtils.toStringJSP(aComputo.getNumAnniRevocaReclusione()) %>"
             		name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE %>" 
             		ONKEYPRESS="return TicTabNumField(this,event)">&nbsp;
				<input type="text" maxlength="2" size="2" 
             		value="<%=StringUtils.toStringJSP(aComputo.getNumMesiRevocaReclusione()) %>"
             		name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE %>"  
             		ONKEYPRESS="return TicTabNumField(this,event)" >&nbsp;
				<input type="text" maxlength="4" size="4"
             		value="<%=StringUtils.toStringJSP(aComputo.getNumGiorniRevocaReclusione()) %>"
             		name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE %>"        
             		ONKEYPRESS="return TicTabNumField(this,event)">
      		</td>

		    <td class="c">
		        <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
		        <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
		        <font class="label">Giorni</font><br>
				<input type="text" maxlength="2" size="2" 
		             value="<%=StringUtils.toStringJSP(aComputo.getNumAnniRevocaArresto()) %>"
		             name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO %>" 
		             ONKEYPRESS="return TicTabNumField(this,event)" >&nbsp;
				<input type="text" maxlength="2" size="2" 
		             value="<%=StringUtils.toStringJSP(aComputo.getNumMesiRevocaArresto()) %>"
		             name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO %>" 
		             ONKEYPRESS="return TicTabNumField(this,event)" >&nbsp;
				<input type="text" maxlength="4" size="4" 
		             value="<%=StringUtils.toStringJSP(aComputo.getNumGiorniRevocaArresto()) %>"
		             name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO %>"
		             ONKEYPRESS="return TicTabNumField(this,event)" >
		    </td>
		  </tr>

		<%-- 		MISURA CONCESSA			--%>
		<tr><td>&nbsp;</td></tr>		
  		  <tr>
		    <td class="l" width="240" >
		        Periodo Concessione Misura
		    </td>

      		<td class="l">
        		<font class="label"> Data inizio Misura </font>&nbsp;&nbsp;&nbsp;
<%			if(aComputoDate!=null && aComputoDate.getIdComputiCumulo()!=null )	{ %>        		
        		<input type="text" Title="Giorno inizio Misura" 
          			name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputoDate.getDataInizioMisura(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Mese inizio Misura"
					name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputoDate.getDataInizioMisura(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Anno inizio Misura" 
          			name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputoDate.getDataInizioMisura(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
<%			} else { %> 
				<input type="text" Title="Giorno inizio Misura" 
          			name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataInizioMisura(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Mese inizio Misura"
					name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataInizioMisura(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Anno inizio Misura" 
          			name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataInizioMisura(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
<%			} %>          			
          	</td>

		    <td class="l">
		        <font class="label"> Data Fine Misura</font>&nbsp;&nbsp;&nbsp;
<%			if(aComputoDate!=null && aComputoDate.getIdComputiCumulo()!=null )	{ %>  		        
		        <input type="text" Title="Giorno Fine Misura" 
          			name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputoDate.getDataFineMisura(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Mese Fine Misura"
					name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputoDate.getDataFineMisura(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Anno Fine Misura" 
          			name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputoDate.getDataFineMisura(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
<%			} else { %> 
				<input type="text" Title="Giorno Fine Misura" 
          			name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataFineMisura(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Mese Fine Misura"
					name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataFineMisura(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Anno Fine Misura" 
          			name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataFineMisura(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
<%			} %>          			
		    </td>
		  </tr>	
		  
		  <tr>
		  	<td class="l"> Periodo espiato in Istituto da detrarre </font>&nbsp;&nbsp;&nbsp;
		  	<td class="l">
		  		<font class="label"> giorni </font>&nbsp;&nbsp;
	  			<input type="text" name="<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_MAP%>" maxlength="4" size="4" 
	  				 value="<%=StringUtils.toStringJSP (aComputo.getNumGiorniMap())%>" >      
			</td>
		  </tr>		
	  </table>
	</div>

	  
</table>
<br>
  <input type="HIDDEN" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(aProvvedimento.getIdStatoEsecTitoloCumulato()) %>">

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
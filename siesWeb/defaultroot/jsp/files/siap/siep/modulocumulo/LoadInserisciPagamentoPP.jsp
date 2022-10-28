<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.statoesecuzione.model.LAModel"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraSicurezzaCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="aProvvedimento" scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="aIdComputo" scope="request" class="java.lang.String"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioEmittente" scope="request" class="java.lang.String"/>
<jsp:useBean id="luogoUfficioEmittente" scope="request" class="java.lang.String"/>

<% 
//============================================================================== 
// Form per l'inserimento e la modifica dei Provvedimento di annotazione Pagamento
// Pena Pecuniaria
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


int maxNumComputi = 4;

%> 

<html>
<head>
  <title> Gestione Pagamento Pena Pecuniaria </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" >

  	var lMaxNumComputi = <%=maxNumComputi%>;
  
    //============================================================================
    // Funzione per il controllo dei dati prima della submit
    //============================================================================
    function Verify()
    {

      // - Quantum obbligatori (almeno uno): segno e quantità o importi
      // - Se provvedimento altra autorità obbligatori:
      //   * Tipo Provvedimento
      //   * Oggetto provvedimento
      //   * Autorità emittente tipo e sede
      // - Magistrato Firmatario o Funzio0nario
      var obbligatori;

  	  // Data Emissione Comunicazione
      if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
      {
          document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+
          document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      }   
      if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
      {
          document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+
          document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
      }
      
	  var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
      var data_emissione = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+
                   		   document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+
                   		   document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_emissione) )
      {
          alert('Data di emissione non valida');
          document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
          return false;
      }

      // Controllo : data di sistema deve essere >= Data di Emissione.
	  if( !CompareDate( data_emissione, data_sistema) )
	  {
	    alert('Data Emissione non può essere superiore alla data odierna!');
	    document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
	    return false;
	  }
      
      //===========================================
      // Controllo sui campi altra autorità
      //===========================================
      if (document.f.TipoOrd[1].checked)
      {        
	        // Data Ricezione Comunicazione  Altra Autorità
	        if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value.length==1)
	        {
	            document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value='0'+
	            document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value;
	        }   
	        if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value.length==1)
	        {
	            document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value='0'+
	            document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value;
	        }   
	
	        var data_ricezione = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value+'/'+
	                   document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value+'/'+
	                   document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>.value;
	
	        if (!ControllaData(data_ricezione) )
	        {
	            alert('Data di ricezione Provvedimento Altra Autorità non valida');
	            document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.focus();
	            return false;
	        }
	        // Controllo : data di emissione deve essere >= Data di Ricezione.
	  	  	if( !CompareDate( data_ricezione, data_emissione) )
	  	  	{
	  	    	alert('Data di ricezione Provvedimento non può essere superiore alla data emissione!');
	  	    	document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.focus();
	  	    	return false;
	  	  	}
	        
			// Controllo data emissione Comunicazione.
	        var data_emissioneAA = document.f.GiornoDataEmissione_AA.value+'/'+
	                   document.f.MeseDataEmissione_AA.value+'/'+
	                   document.f.AnnoDataEmissione_AA.value;
			//alert ('data_emissioneAA = '+ data_to_verify );
	
	        if (!ControllaData(data_emissioneAA) )
	        {
	            alert('Data di emissione Provvedimento Altra Autorità non valida');
	            document.f.GiornoDataEmissione_AA.focus();
	            return false;
	        }
	        // Controllo : data di emissione deve essere >= Data di Ricezione.
	  	  	if( !CompareDate( data_emissioneAA, data_ricezione) )
	  	  	{
	  	    	alert('Data emissione Comunicazione non può essere superiore alla data ricezione!');
	  	    	document.f.GiornoDataEmissione_AA.focus();
	  	    	return false;
	  	  	}
			
	        
	        // Partita di Credito ed Ex Campione Penale : Per inserirli in Evento 
			// Partita di Credito va in CAMPO_ANNO_PROTOCOLLO,CAMPO_PROGR_PROTOCOLLO
			// Ex Campione Penale va in CAMPO_PROTOCOLLO_RES       
	  
			if (document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.value=="")
			{
				alert('Anno Partita di Credito Obbligatorio');
				document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.focus();
				return false;
			}
			else if (document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.value<1950)
			{
				alert('Anno Partita di Credito non valido');
				document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.focus();
				return false;
			}
	        
			if (document.f.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.value=="")
			{
				alert('Numero Partita di Credito obbligatorio');
				document.f.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.focus();
				return false;
			}
	          
			if( document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.value==""
	       	   && document.f.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO %>.value==""
	           && document.f.<%=ICostantiEvento.CAMPO_NOME_SOGGETTO_PRESENTANTE %>.value==""  )
	      	{
	                 alert("Inserire Almeno un campo tra Partita di Credito e EX Campione Penale");
	                 document.f.<%= ICostantiEvento.CAMPO_ANNO_PROTOCOLLO %>.focus(); 
	                 return false;
	      	}         

			// Sede 
	        if (document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.value=="" ||
	            document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.value=="-")
	        {        
	                alert("Selezionare Sede Autorità Emittente");
	                document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
	                return false;
	        }
      }
  
      // Quantum di computo
      var numComputi = 0;
      
      // Per ogni rigo visibile verifico la coerenza dei dati. 
      // Se presente il segno vanno specificati anche i quantum.
      // Se presenti i quantum è obbligatorio il segno.
      //
      // E' accettato il rigo vuoto anche se visibile.
      
      for (i=0; i<lMaxNumComputi; i++)
      {

          idComputo = "annotazione_"+i;
          display = document.getElementById(idComputo).style.display;
          if (display=="block")
          {
       	  		//   Rigo visibile, quindi controllo la presenza e coerenza dei dati
                if (   trimStringa(document.getElementById('Multa_'+i).value) == "" 
                    && trimStringa(document.getElementById('Mul_dec_'+i).value) == "" 
                    && trimStringa(document.getElementById('Ammenda_'+i).value) == "" 
                    && trimStringa(document.getElementById('Amm_dec_'+i).value) == "" 
                   )
                {
                	if (i==0) {
                    	alert ("Indicare gli importi");
                    } else {
                    	alert ("Indicare gli importi o eliminare l'ulteriore computo");                    	
                    }
                    document.getElementById('Multa_'+i).focus();
                    return false;
                }
                else
                {
                    numComputi=numComputi+1;
                }
          
          }  // chiude if (display=="block")
      }  // Chiude ciclo for
      
      if (numComputi==0)
      {
          alert ("Inserire almeno un computo con multa e/o ammenda ");
          return false;
      }

      return true;
    }

    //==========================================================================
    // 
    //==========================================================================
    function eseguiFunzione(action)
    {
      document.f.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.f.submit();
    }
    
    function radioBase()
    {
      if(document.f.TipoOrd[0].checked) {
	   	  // Pulizia campi
	   	  document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value="";
	   	  document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value="";
	   	  document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>.value="";
	   	  
	   	  document.f.GiornoDataRicezioneAtti.value="";
	   	  document.f.MeseDataRicezioneAtti.value="";
	   	  document.f.AnnoDataRicezioneAtti.value="";
	
	   	  document.f.GiornoDataEmissione_AA.value="";
	   	  document.f.MeseDataEmissione_AA.value="";
	   	  document.f.AnnoDataEmissione_AA.value="";
	
	   	  document.f.AnnoProtocollo.value="";
	   	  document.f.ProgrProtocollo.value="";
	   	  document.f.NumExCampionePenale.value="";
	   	  document.f.CodUfficioEmittente.value="-";
	   	  document.f.CodLuogoEmittente.value="";
	   	  document.f.SezioneProvv.value="";
        document.getElementById('DivAltraAutorita').style.display = "none";
      }
      else {
        document.getElementById('DivAltraAutorita').style.display = "block";
      }
    }
    
    function ListaUfficiPerTipo (a_formname,a_fieldname,codTipoUfficio)
    {
         var desktop;
         desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,top=170,left=90,width=300,height=500");
    }

    //==========================================================
    // Visualizza una nuova riga per l'inserimento dei quantum
    //==========================================================
    function addQuantum()
    {
        for (i=0; i<lMaxNumComputi; i++)
        {
          idComputo = "annotazione_"+i;
          display = document.getElementById(idComputo).style.display;
          if (display=="none")
          {
              Rigo = i;
              // Multa
              document.getElementById('Multa_'+Rigo).disabled = false ;
              document.getElementById('Mul_dec_'+Rigo).disabled = false ;
        
              //Ammenda
              document.getElementById('Ammenda_'+Rigo).disabled = false ;
              document.getElementById('Amm_dec_'+Rigo).disabled = false ;
        
              strTdCancella = '<a href="Javascript:cancellaComputo(\''+Rigo+'\');"><img src="/images/delete.gif" border="0" title="Cancella computo"></a>';
    
              document.getElementById('tdCancella_'+Rigo).innerHTML = strTdCancella;
              document.getElementById('tdCancella_'+(Rigo-1)).innerHTML = '<br>';
              
              // Visualizzo
              document.getElementById(idComputo).style.display = "block";
              break;
          }
        }
    }
    
    function cancellaComputo(Rigo)
    {
      
      //===========================================================
      // Cancello il contenuto delle celle
      //===========================================================
    	  
      // Multa
        document.getElementById('Multa_'+Rigo).value = "" ;
        document.getElementById('Mul_dec_'+Rigo).value = "" ;
      //Ammenda
        document.getElementById('Ammenda_'+Rigo).value = "" ;
        document.getElementById('Amm_dec_'+Rigo).value = "" ;

      //===========================================================
      // Disabilito le celle in modo che non venga fatta la submit
      //===========================================================
      
     // Multa
     //   document.getElementById('Multa_'+Rigo).disabled = true ;
     //   document.getElementById('Mul_dec_'+Rigo).disabled = true ;
     //Ammenda
     //   document.getElementById('Ammenda_'+Rigo).disabled = true ;
     //   document.getElementById('Amm_dec_'+Rigo).disabled = true ;

      //===================
      // Nascondo la riga
      //===================
          document.getElementById('annotazione_'+Rigo).style.display = "none";
      
          if (Rigo>1)
          {
            strTdCancella = '<a href="Javascript:cancellaComputo(\''+(Rigo-1)+'\');"><img src="/images/delete.gif" border="0" title="Cancella computo"></a>';
    
            document.getElementById('tdCancella_'+(Rigo-1)).innerHTML = strTdCancella;
            document.getElementById('tdCancella_'+(Rigo)).innerHTML = '<br>';
          }
    }

        
  </script>
</head>

<body class="corpo" onload="radioBase();">
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
        <font class="campo">Inserimento annotazione Pagamento Pena Pecuniaria &nbsp;</font>
        <% } else if( modalita.equals("M") || modalita.equals("NP") ) { %>
        <font class="campo">Modifica annotazione Pagamento Pena Pecuniaria &nbsp;</font>
        <%}%>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaPagamentiPP')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciPagamentoPP">
  <input type="hidden" name="maxNumComputi" value="<%=maxNumComputi%>" >
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  
  <input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(aProvvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="hidden" name="<%= ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO %>"                       value="<%=StringUtils.toStringJSP(aComputo.getIdComputiCumulo()) %>">
  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>" 					  value="<%=StringUtils.toStringJSP(aProvvedimento.getFlagStato()) %>">

  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td class="titolo" colspan="100%">Provvedimento di Annotazione</td>
    </tr>
  
    <tr>
      <td class="l">Provvedimento</td>
      <td class="l">Rideterminazione della pena a seguito di Avvenuto pagamento di Pena Pecuniaria</td>
      <td class="l">Data Emissione</td>
      <td class="L">
	      <input title = "Giorno Data Emissione" 
	             type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>
	             value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"dd"))%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
	      /
	      <input title = "Mese Data Emissione" 
	             type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>
	             value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"MM"))%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
	      /
	      <input title = "Anno Data Emissione" 
	             type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%>
	             value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"yyyy"))%>"  
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      
    </tr>
  </table>

  <br>

  <table width="95%" align="center">
    <tr>
      <td class="l" colspan=10 > 
        <input type="radio" name="TipoOrd" value="dufficio" 
        	<%=(modalita.equals("I") || aComputo.getDataEmissioneProvv()==null) ?"checked":"" %>
        	onClick="javascript:radioBase();">D'ufficio &nbsp;&nbsp;

        <input type="radio" name="TipoOrd" value="altroUfficio" 
        	<%=(modalita.equals("M") && aComputo.getDataEmissioneProvv()!=null) ?"checked":"" %>
        	onClick="javascript:radioBase();">In esecuzione di provvedimento altro ufficio &nbsp;&nbsp;
      </td>
    </tr>
  </table>
<%
//==============================================================================
// 
//==============================================================================
%>
<div id="DivAltraAutorita" style="display:none">
  <table width="95%" align="center">
    <tr>
      <td class="titolo" colspan="4" width="95%">Dati Provvedimento Altra Autorità</td>
    </tr>

    <tr>
      <td class="l" colspan="1">Data ricezione Comunicazione </td>
      <td class="l" colspan="1">
        <input type="text" Title="Giorno Ricezione" 
          name="GiornoDataRicezioneAtti" maxlength="2" size="2"  
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataRicezioneProvv(),"dd"))%>"  
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Ricezione"   
          name="MeseDataRicezioneAtti" maxlength="2" size="2"  
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataRicezioneProvv(),"MM"))%>"  
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Ricezione" 
          name="AnnoDataRicezioneAtti" maxlength="4" size="4"  
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataRicezioneProvv(),"yyyy"))%>"  
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>

      <td class="l" colspan="1">Data emissione Comunicazione</td>
      <td class="l" colspan="1">
        <input type="text" Title="Giorno Emissione provvedimento"
          name="GiornoDataEmissione_AA" maxlength="2" size="2" 
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataEmissioneProvv(),"dd"))%>"  
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Emissione provvedimento"   
          name="MeseDataEmissione_AA"   maxlength="2" size="2" 
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataEmissioneProvv(),"MM"))%>"  
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Emissione provvedimento" 
          name="AnnoDataEmissione_AA"   maxlength="4" size="4" 
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataEmissioneProvv(),"yyyy"))%>"  
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
     
    <tr> 
      <td class="l" colspan="1">Anno / Numero Partita di Credito<font class="ob">(*)</font></td>
      <td class="l" colspan="1">
         <input Title="Anno Provvedimento" value="<%=StringUtils.toStringJSP (aComputo.getAnnoProvv() )%>"
         name="AnnoProtocollo"  type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
         <input Title="Numero Provvedimento"  value="<%=StringUtils.toStringJSP (aComputo.getProgrProvv() )%>"
         name="ProgrProtocollo" type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)">
      </td>
      
      <td class="l" colspan="1">Numero ex campione penale</font></td>
      <td class="l" colspan="1">
        <input type="text" maxlength="20" size="20" name="NumExCampionePenale"
         value="<%=StringUtils.toStringJSP (aComputo.getNote() )%>">
      </td>
    </tr>

    <tr>
      <td class="l" colspan="1">Ufficio Recupero Crediti<font class="ob">(*)</font></td>
      <td class="l" colspan="1">
        <select Title="Autorità Emittente" name="CodUfficioEmittente">
          <option value="-" />-
          <%=tipoUfficioEmittente%>
        </select>
      </td>
    </tr>
     
    <tr> 
       <td class="l" colspan="1">Sede &nbsp;</td>
       <td class="l" colspan="1">
         <input title="Sede Autorita"  type="text" name="CodLuogoEmittente"  maxlength="35" size="35"
         	value="<%=luogoUfficioEmittente%>">
         <a href="Javascript:ListaUfficiPerTipo('f','CodLuogoEmittente',document.f.CodUfficioEmittente[document.f.CodUfficioEmittente.selectedIndex].value);">
           <img src="/images/filefolder.gif" border="0">
         </a>
       </td>
       <td class="l" colspan="1"> Sezione &nbsp;</td> 
       <td class="l" colspan="1">
         <input type="text" maxlength="20" size="20" name="SezioneProvv"
			value="<%=StringUtils.toStringJSP (aComputo.getSezioneProvv() )%>">
       </td>
    </tr>
  </table>
</div>
  
  
  
  
  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td class="titolo" colspan="100%">Pena Pecuniaria versata</td>
    </tr>
  </table>
      
  <table width="95%" style="border: 0;" align="center"> 
  <% for (int i=0;i< maxNumComputi ;i++ ) 
  {
	  String lMulta_="";
	  String lAmmenda_="";

      if (lListaComputi!=null &&  i<lListaComputi.size()) 
      { 
    	  aComputo = lListaComputi.get(i);      
      %>
          <tr style="display:block" id="annotazione_<%=i%>">
<%    }
      else
      { 
    	  aComputo = new ComputiCumuloModel();
    	  if (i==0) {
%>	
          	<tr style="display:block" id="annotazione_<%=i%>">
<%		  }  else { %>          
          	<tr style="display:none" id="annotazione_<%=i%>">
<%    } } %>

      <td>
        <table>
          <tr>
            <td class="c">
                <font class="label"><%=i+1%> )</font>
  				<input type="hidden" name="PM_<%=i%>" id="PM_<%=i%>" value="-">
  				<input type="hidden" name="IdComputiCumulo_<%=i%>" id="IdComputiCumulo_<%=i%>" value="<%=StringUtils.toStringJSP(aComputo.getIdComputiCumulo()) %>">
            </td>
            <td class="c" width="40%">
                <font class="label">Multa</font>
                  <input style="text-align:right" type="text" name="Multa_<%=i%>"   id="Multa_<%=i%>" maxlength="8" size="6" 
                  		 value="<%=StringUtils.getParteIntera(aComputo.getImportoMulta()) %>" 
                  		 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
                  ,
                  <input style="align:right" type="text" name="Mul_dec_<%=i%>" id="Mul_dec_<%=i%>" maxlength="2" size="2" 
                  		 value="<%=StringUtils.getParteDecimale(aComputo.getImportoMulta()) %>" 
                  		 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            </td>

            <td class="c" width="40%">
                <font class="label">Ammenda</font>
                  <input style="text-align:right" type="text" name="Ammenda_<%=i%>" id="Ammenda_<%=i%>" maxlength="8" size="6" 
                  		 value="<%=StringUtils.getParteIntera(aComputo.getImportoAmmenda()) %>" 
                  		 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
                  ,
                  <input style="align:right" type="text" name="Amm_dec_<%=i%>" id="Amm_dec_<%=i%>" maxlength="2" size="2" 
                  		 value="<%=StringUtils.getParteDecimale(aComputo.getImportoAmmenda()) %>" 
                  		 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            </td>

		    <% if (i==0) 
		       { %>
		            <td valign="middle" class="c"  id="tdCancella_<%=i%>" width="10%">&nbsp;</td>
		    <% }
		       else
		       { %>
		            <td valign="middle" class="c"  id="tdCancella_<%=i%>" width="10%">&nbsp;</td>
		
		    <% } %>             
          </tr>
        </table>
      </td>
    </tr>
  <% }  %>

    <tr>
      <td class="l" colspan="100%">
        <a href="Javascript:addQuantum('f');">
          Aggiungi ulteriore computo
        </a>
      </td>
    </tr>
    
  </table>


  <table cellspacing="2" cellpadding="2" width="95%" align="center">
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
  var frmvalidator  = new Validator("f");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
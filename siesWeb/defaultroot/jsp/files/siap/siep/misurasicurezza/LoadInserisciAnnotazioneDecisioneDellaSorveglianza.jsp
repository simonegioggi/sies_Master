<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="f3b.log.LogF3B"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<!-- %@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriFascicoloSiusModel" %> -->
<%@ page import="siap.sius.misurasicurezza.model.ProvvedimentoEventoTenoreFascicoloSiusModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %> 

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="dataeditabile"				scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra"		scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="StrdataInizioPena"			scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"				scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<!-- jsp:useBean id="ListaOrd"					scope="request" class="java.util.Vector"/> -->
<jsp:useBean id="ListaProvv"				scope="request" class="java.util.Vector"/>
<jsp:useBean id="ListaNewMisSic"			scope="request" class="java.util.Vector"/>
<%

	String FlagIstanza="";
	FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  
  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
  UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
  
  int provvDaElab = 0;
  Iterator ItxrL = ListaProvv.iterator();
  while (ItxrL.hasNext() )
  {
	  ProvvedimentoEventoTenoreFascicoloSiusModel lProvvMod = (ProvvedimentoEventoTenoreFascicoloSiusModel)ItxrL.next();
	  EventoModel lEveMod = lProvvMod.getEvento(); 
	  if((lProvvMod.getEvento().getCodTipoProvvedimento().compareTo("03")==0	&&
	  	 (lProvvMod.getOrdinanza()==null	||
		  lProvvMod.getOrdinanza().getFlagElaborato() == null ||
		  lProvvMod.getOrdinanza().getFlagElaborato().compareTo("S")!=0) ) ||
	     (lProvvMod.getEvento().getCodTipoProvvedimento().compareTo("02")==0	&&
	  	 (lProvvMod.getDecreto()==null	||
		  lProvvMod.getDecreto().getFlagElaborato() == null ||
		  lProvvMod.getDecreto().getFlagElaborato().compareTo("S")!=0) ) )
	  {
		 provvDaElab ++;
	  }
  }
  int sommaProvv = ListaProvv.size()+provvDaElab;
  

%>
<!-- LoadInserisciAnnotazioneDecisioneDellaSorveglianza -->
<html>
  <head>
    <title>[S.I.E.S.] -Gestione Misure sicurezza- Annotazione Decisione della Sorveglianza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript">
	
    	var desktop;
    	function ListaComuni(a_formname,a_fieldname)
    	{
      		desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    	}
   		
        function refreshListaProvv()
        {
			var numProvvDaEla ='<%= provvDaElab%>';
			var sommaProvv = '<%= sommaProvv%>';
			//alert(" sommaProvv = "+sommaProvv+" - provvDaElab = "+numProvvDaEla);
			var j = 0;
			var y = 0;
            // Visualizzazione Provvedimenti da Elaborare 
			if(document.LoadInsAnnotazioneSorv.radioTipoProvv[0].checked )
         	{ 
        		document.getElementById("divOdE").style.display = "block";
        		document.getElementById("divOt").style.display = "none";
				if (sommaProvv > 1) {
        			for( j = 0; j < numProvvDaEla; j++) {
                		document.LoadInsAnnotazioneSorv.Seleziona[j].disabled=false;
        			}
        			for( y = numProvvDaEla; y < sommaProvv; y++) {
        				document.LoadInsAnnotazioneSorv.Seleziona[y].checked = false;
                		document.LoadInsAnnotazioneSorv.Seleziona[y].disabled=true;
        			}
				} else if (sommaProvv!=0){ 	
					// C'è un solo provvedimento già elaborato
               		document.LoadInsAnnotazioneSorv.Seleziona.disabled=true;
				}
        		document.getElementById("divOdE").style.display = "block";
        		document.getElementById("divOt").style.display = "none";
         	}
            
            // Visualizzazione di tutti i provvedimenti. 
			if(document.LoadInsAnnotazioneSorv.radioTipoProvv[1].checked )
          	{ 
        		document.getElementById("divOdE").style.display = "none";
        		document.getElementById("divOt").style.display = "block";
				if (sommaProvv > 1) {
        			for( j = 0; j < numProvvDaEla; j++) {
        				document.LoadInsAnnotazioneSorv.Seleziona[j].checked = false;
                		document.LoadInsAnnotazioneSorv.Seleziona[j].disabled=true;
        			}
        			for( y = numProvvDaEla; y < sommaProvv; y++) {
                		document.LoadInsAnnotazioneSorv.Seleziona[y].disabled=false;
        			}
				} else if (sommaProvv!=0) { 	// C'è un solo provvedimento già elaborato
    				document.LoadInsAnnotazioneSorv.Seleziona.checked = false;
               		document.LoadInsAnnotazioneSorv.Seleziona.disabled=false;
				}
        		document.getElementById("divOdE").style.display = "none";
        		document.getElementById("divOt").style.display = "block";
          	}
        }     

        function Verify()
  		{
  			//var Misu ='<%= ListaProvv.size()%>';
  			var Misu ='<%= sommaProvv%>';
  			var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
  			
		  	if (document.LoadInsAnnotazioneSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value.length==1)
			  	document.LoadInsAnnotazioneSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value='0'+document.LoadInsAnnotazioneSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value;
		  	if (document.LoadInsAnnotazioneSorv.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value.length==1)
			  	document.LoadInsAnnotazioneSorv.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value='0'+document.LoadInsAnnotazioneSorv.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value;

		  	var data_to_verify = document.LoadInsAnnotazioneSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value+'/'+document.LoadInsAnnotazioneSorv.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value+'/'+document.LoadInsAnnotazioneSorv.<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>.value;

        	if (!ControllaData(data_to_verify) )
		  	{
        		alert('Data di Ricezione NON Valida');
        		document.LoadInsAnnotazioneSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.focus();
			   	return false;
		  	}
        	
		    //1) Controllo : data di sistema deve essere >= Data Emissione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
		      alert('Data Ricezione non può essere superiore alla data odierna!');
		      document.LoadInsAnnotazioneSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.focus();
		      return false;
		    }
<% 
				if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))))
	 			{
		  			if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
		  			{
		%>
					      if (document.LoadInsAnnotazioneSorv.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==1)
							  document.LoadInsAnnotazioneSorv.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value='0'+document.LoadInsAnnotazioneSorv.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
						  if (document.LoadInsAnnotazioneSorv.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==1)
							  document.LoadInsAnnotazioneSorv.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'+document.LoadInsAnnotazioneSorv.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;
				
						  var data_to_verifica = document.LoadInsAnnotazioneSorv.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInsAnnotazioneSorv.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInsAnnotazioneSorv.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;
		
				      	  if (!ControllaData(data_to_verifica) )
						  {
			          			alert('Data fine pena non valida');
			          			document.LoadInsAnnotazioneSorv.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.focus();
					 			return false;
				  		  }
		<%
					}
		 		}
%>

		<% // Controllo provvedimenti selezionati %>
		
		var i = 0;
		var k = 0;

		if(Misu == 1)
		{
			if(!document.LoadInsAnnotazioneSorv.Seleziona.checked)
			{
				alert("Selezionare almeno 1 Provvedimento");
				return false;				
			}	
		}
		else
		{			
			for( i = 0; i < Misu; i++)
			{
				if(document.LoadInsAnnotazioneSorv.Seleziona[i].checked)
				{
					k = k + 1;
				}	
			}
			
			if(k == 0)
			{
				alert("Selezionare almeno 1 Provvedimento");
				return false;
			}	
			
			if(k > 1)
			{
				alert("Selezionare solo 1 Provvedimento");
				return false;
			}
		}
		
    }	<% // Chiude function Verify %>
    
    function Selezionato(eveKey)
    {
     	document.LoadInsAnnotazioneSorv.<%=ICostantiEvento.CAMPO_ID_EVENTO%>.value =eveKey ;
    }	// Chiude Selezionato
     
  // Caricamento POPUP	
      function ListaMisureSIUS(a_formname, aIdFasSIUS)
     {
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sius.misurasicurezza.action.ActLoadPopupDettaglioMisuraSic&formname="+a_formname+"&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>="+aIdFasSIUS, "Dettaglio_Misure_Sicurezza", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=400");
     }
   
 </script>
</head>
<body class="corpo" onLoad="refreshListaProvv();">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      	<font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Annotazione Decisione della Sorveglianza</font>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" name="LoadInsAnnotazioneSorv" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActInserisciAnnotazioneDecisioneDellaSorveglianza">
    <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=5>
          <font class="campo">
          <%=lPosizione.getDescrPosizioneGiuridica()%>
        </font>
        </td>
        <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
      	<input type="HIDDEN" title="id Evento" value="" type="text" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" >
      	
      </tr>
  
<%	//fine modifica relativa al tipo istituto
    if(penaresidua.getIdPenaResidua() != null && 
    ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null
    	&& !penaresidua.getFlagErgastolo().equals("S") 
    	&& !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        	if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            	(penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            	(penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0) )
        	{
        		
        	}
        	else
        	{
%>
			<tr>
		          <td class="l">Reclusione</td>
		          <td class="l" colspan=2>
		            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
		            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
		            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
		          </td>
		          <td class="l">Multa</td>
		          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
			</tr>
<%
        	}
%>

<% 
			if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
         		(penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
             	(penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
     	 	{
				
     	 	}
			else
     	 	{
%>
			   <tr>
			      <td class="l" >Arresto</td>
			      <td class="l" colspan=2>
			         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
			         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
			         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
			      </td>
			      <td class="l">Ammenda</td>
			      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
			    	</td>
			    </tr>	
<%
      		}

     }  // CHIUDO if(penaresidua...)
%>
    <tr>
<%
      if (penaresidua.getDataInizio() != null)
      {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StrdataInizioPena%>&nbsp;</font></td>
<%
      }
 
       if (penaresidua.getFlagErgastolo() != null)
       {
	         if(penaresidua.getFlagErgastolo().equals("S"))
	         {
	%>
	           	<td class="l">Pena Detentiva</td>
	           	<td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
	<%
	         }
	         else if(penaresidua.getFlagErgastolo().equals("D"))
	         {
	%>
	           	<td class="l">Pena Detentiva</td>
	           	<td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
	<%
	         }
       }
%>
<%
        if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
        {
           if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
           {
%>
		         <td class="l">Data Fine Pena</td>
		         <td class="L" colspan=2>
		           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		           -
		           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		           -
		           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		         </td>
<%
          }
          else if( penaresidua.getDataFine() != null)
          {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
%>
	             <td class="l">Data Fine Pena</td>
	             <td class="L" colspan=2>
	               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
	               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
	               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
	               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
	            </td>
<%
          }
          else
          {
%>
             <td class="l">Data Fine Pena</td>
             <td class="lRosso" colspan=2>
               <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>

<%       	}
        }
      }
%>
	</tr>
</table>

 <!-- Misure di Sicurezza già presenti -->  
<%
	List lMisure =(List) request.getAttribute("listaMisurePrec");
	if(lMisure != null && lMisure.size() != 0)
	{ %>
		<table>
<% 		for(int i=0;i<lMisure.size();i++)
		{	
			//MisuraSicurezzaModel lMis = (MisuraSicurezzaModel)lMisure.get(lMisure.size()-1);
			MisuraSicurezzaModel lMis = (MisuraSicurezzaModel)lMisure.get(i);
%>
		    <tr>
		    	<td class=C>Misura di Sicurezza da espiare</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%>&nbsp;</font></td>
		      	<td class=C> Anni</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(),"0")%>&nbsp;</font></td>
		      	<td class=C> Mesi</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(),"0")%>&nbsp;</font></td>
		      	<td class=C> Giorni</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(),"0")%>&nbsp;</font></td>	
		    </tr>
<%		} %>		      		
	    </table>
<%	}
	else
	{	
%>  
			<tr><td class="l" > Fascicolo privo di Misure di Sicurezza </td></tr>		
<%	} %>		
	<br>	
	<table>
		<tr>
        <td class="l">Data Ricezione <font class="ob">(*)</font></td>
        <td class="L" colspan=2 >
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        </td>

      </tr>
     <tr>
   	  <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
    </tr>
  </table>
 
 <!--Provvedimenti Sorveglianza -->  

    <table cellspacing=2 cellpadding=2>
      <tr>
	    <td class="l" colspan="2">Visualizza provvedimenti da elaborare &nbsp;<input type="radio" name="radioTipoProvv" value="daElaborare" onClick="javascript:refreshListaProvv();" checked >
	      &nbsp; Tutti  &nbsp; <input type="radio" name="radioTipoProvv" value="Tutte" onClick="javascript:refreshListaProvv();">
	    </td>

      </tr>
  </table>

   	<table align="center" cellspacing=2 cellpadding=2 width="100%">
   		<tr><td class="Titolo" colspan=6> Dati provvedimento Ufficio di Sorveglianza</td></tr>

		<div id="divEmpty" style="display: none; position: relative; width: 100%;">
   			<table width="100%">
			</table>
		</div>

		<div id="divOdE" style="display:none; position:relative; width:100%;" >
   			<table width="100%">
    	<tr>
    		<td class="int" width="3%">Prog.</td>
    		<td class="int" width="10%">Anno / Numero SIUS</td>
    		<td class="int" width="12%">Anno / Numero Provv</td>
    		<td class="int" width="20%">Autorità emittente</td>
    		<td class="int" width="10%"> Data </td>
    		<td class="int" width="25%"> Oggetto </td>
    		<td class="int" width="25%"> Esito</td>
    		<td class="int" width="4%">Visualizza Misure</td>
    		<td class="int" width="4%">Seleziona</td>
    	</tr>
<%
		int idR = 0;
		int idR1 = 0;
		Iterator Itx0 = ListaProvv.iterator();
		while (Itx0.hasNext() )
		{
			idR = idR + 1;
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				//siesLogger.debug("--XX-- idr = "+idR);
			ProvvedimentoEventoTenoreFascicoloSiusModel lProvvMod = (ProvvedimentoEventoTenoreFascicoloSiusModel)Itx0.next();
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				//siesLogger.debug("--XX-- lProvvMod.eve = "+lProvvMod.getEvento());
			EventoModel lEveMod = lProvvMod.getEvento();
			String annoS3S72 = ""; 
			String numS3S72 = "";
			if(lProvvMod.getEvento().getCodTipoProvvedimento().compareTo("02")==0 )
			{
				annoS3S72 = "Decr. "+StringUtils.toStringJSP(lProvvMod.getDecreto().getAnnoS72()); 
				numS3S72 =  StringUtils.toStringJSP(lProvvMod.getDecreto().getNumS72()); 
			}
			if(lProvvMod.getEvento().getCodTipoProvvedimento().compareTo("03")==0 )
			{
				annoS3S72 = "Ord. "+StringUtils.toStringJSP(lProvvMod.getOrdinanza().getAnnoS3()); 
				numS3S72 =  StringUtils.toStringJSP(lProvvMod.getOrdinanza().getNumS3()); 
			}
			if((lProvvMod.getEvento().getCodTipoProvvedimento().compareTo("03")==0	&&
			   (lProvvMod.getOrdinanza()==null	||
				lProvvMod.getOrdinanza().getFlagElaborato() == null ||
				lProvvMod.getOrdinanza().getFlagElaborato().compareTo("S")!=0) ) ||
			   (lProvvMod.getEvento().getCodTipoProvvedimento().compareTo("02")==0	&&
			   (lProvvMod.getDecreto()==null	||
				lProvvMod.getDecreto().getFlagElaborato() == null ||
				lProvvMod.getDecreto().getFlagElaborato().compareTo("S")!=0) ) )
			{
				idR1 = idR1 + 1;
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				//siesLogger.debug("--XX-- idr1 = "+idR1);
%>			
				<tr>
					<td class="l"><%=idR1 %></td>
					<td class="l"><%=StringUtils.toStringJSP(lProvvMod.getFascicoloSiusModel().getChiaveAnno())%>
								/ <%=StringUtils.toStringJSP(lProvvMod.getFascicoloSiusModel().getChiaveProgr())%>
					</td>
					<td class="l"><%=annoS3S72 %> / <%=numS3S72 %>
					</td>
					<td class="l"><%=lProvvMod.getDescrTipoUfficio()+" di "+lProvvMod.getDescrComuneUfficio() %></td>
					<td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvvMod.getEvento().getDataEmissione(),"dd-MM-yyyy")) %></td>
					<td class="l"><%=lProvvMod.getDescrOggetto() %></td>
					<td class="l"><%=lProvvMod.getDescrEsito() %></td>
					<td class = "l" style="text-align:center">
						<input type="hidden" value="" name="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>">
       					<a href="Javascript:ListaMisureSIUS('LoadInsAnnotazioneSorv','<%=lProvvMod.getFascicoloSiusModel().getIdFascicoloSius()%>');">
       					<img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Misure" border=0>
       				</a>
					</td>
					<td class="l" width="4%">
						<input type="checkbox" name="Seleziona" onclick="Javascript:Selezionato('<%=lProvvMod.getEvento().getIdEvento()%>');" >
					</td>
				</tr>
		<%	} } %>				
			</table>
		</div>

     <div id="divOt" style="display:none; position:relative; width:100%;" > 
			<table width="100%">
    	<tr>
    		<td class="int" width="3%">Prog.</td>
    		<td class="int" width="10%">Anno / Numero SIUS</td>
    		<td class="int" width="12%">Anno / Numero Provv</td>
    		<td class="int" width="20%">Autorità emittente</td>
    		<td class="int" width="10%"> Data </td>
    		<td class="int" width="25%"> Oggetto </td>
    		<td class="int" width="25%"> Esito</td>
    		<td class="int" width="4%">Visualizza Misure</td>
    		<td class="int" width="4%">Seleziona</td>
    	</tr>
<%
			int idR2 = 0;
			Iterator Itx1 = ListaProvv.iterator();
			while (Itx1.hasNext() )
			{
				idR2 = idR2 + 1;
				ProvvedimentoEventoTenoreFascicoloSiusModel lProvvMod = (ProvvedimentoEventoTenoreFascicoloSiusModel)Itx1.next();
				String lineColor = "l";
				if((lProvvMod.getEvento().getCodTipoProvvedimento().compareTo("03")==0	&&
				   (lProvvMod.getOrdinanza()!=null	&&
				    lProvvMod.getOrdinanza().getFlagElaborato() != null &&
					lProvvMod.getOrdinanza().getFlagElaborato().compareTo("S")==0) ) ||
				   (lProvvMod.getEvento().getCodTipoProvvedimento().compareTo("02")==0	&&
				   (lProvvMod.getDecreto()!=null	&&
					lProvvMod.getDecreto().getFlagElaborato() != null &&
					lProvvMod.getDecreto().getFlagElaborato().compareTo("S")==0) ) )
				lineColor = "lRosso";

				String annoS3S72 = ""; 
				String numS3S72 = "";
				if(lProvvMod.getEvento().getCodTipoProvvedimento().compareTo("02")==0 )
				{
					annoS3S72 = "Decr. "+StringUtils.toStringJSP(lProvvMod.getDecreto().getAnnoS72()); 
					numS3S72 =  StringUtils.toStringJSP(lProvvMod.getDecreto().getNumS72()); 
				}
				if(lProvvMod.getEvento().getCodTipoProvvedimento().compareTo("03")==0 )
				{
					annoS3S72 = "Ord. "+StringUtils.toStringJSP(lProvvMod.getOrdinanza().getAnnoS3()); 
					numS3S72 =  StringUtils.toStringJSP(lProvvMod.getOrdinanza().getNumS3());
				}
%>			
			<tr>
				<td class="<%=lineColor%>"><%=idR2 %></td>
				<td class="<%=lineColor%>"><%=StringUtils.toStringJSP(lProvvMod.getFascicoloSiusModel().getChiaveAnno())%>
										  /<%=StringUtils.toStringJSP(lProvvMod.getFascicoloSiusModel().getChiaveProgr())%>
				</td>
				<td class="<%=lineColor%>"><%=annoS3S72%>
										  /<%=numS3S72%>
				</td>
				<td class="<%=lineColor%>"><%=lProvvMod.getDescrTipoUfficio()+" di "+lProvvMod.getDescrComuneUfficio() %></td>
				<td class="<%=lineColor%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvvMod.getEvento().getDataEmissione(),"dd-MM-yyyy")) %></td>
				<td class="<%=lineColor%>"><%=lProvvMod.getDescrOggetto() %></td>
				<td class="<%=lineColor%>"><%=lProvvMod.getDescrEsito() %></td>
				<td class="<%=lineColor%>" style="text-align:center">
				     	<input type="hidden" value="" name="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>">
       					<a href="Javascript:ListaMisureSIUS('LoadInsAnnotazioneSorv','<%=lProvvMod.getFascicoloSiusModel().getIdFascicoloSius() %>');">
       					<img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Misure" border=0>
       					</a>
				</td>				
				<td class="<%=lineColor%>">
					<input type="checkbox" name="Seleziona" onclick="Javascript:Selezionato('<%=lProvvMod.getEvento().getIdEvento()%>');" >
				</td>
			</tr>
		<%	} %>
		<%	if (idR2 > 0) { %>	
				<tr>
	      			<td class="lVerdeNB" colspan=9>
	        			<font class="lVerde">N.B.: &nbsp;&nbsp; In rosso sono visualizzate i provvedimenti già elaborati.</font>
	      			</td>
				</tr>
		<%	} %>
			</table>
		</div>
		
					
	</table>								
	
<!-- Bottone di Conferma, in DIV perchè deve cambiare posizione -->

<div id="divbottone" style="display:block; position:relative; width:100%;" >  	
 <table width="90%">
	<tr> 
    	<td class="lNoBord" colspan="2">
      		<br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    	</td>
  	</tr>
</table>
</div>

</form> 
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInsAnnotazioneSorv");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>","lt=2050");

<%
 if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))))
 {
	  if ( dataeditabile.equals("S")  && penaresidua.getDataFinePresunta() != null )
	  {%>
	  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena è obbligatorio");
	  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");
	
	  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena è obbligatorio");
	  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");
	
	  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena è obbligatorio");
	  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
	  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
	  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2050");
	<%}
 }
%>

</script>
</body>
</html>
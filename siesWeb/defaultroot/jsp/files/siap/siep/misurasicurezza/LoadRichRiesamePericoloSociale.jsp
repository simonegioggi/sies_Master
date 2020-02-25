<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.List" %>

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
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>


<jsp:useBean id="dataeditabile"				scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra"		scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="autoritaEsternaE"			scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"				scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="StrdataInizioPena"			scope="request" class="java.lang.String"/>
<jsp:useBean id="StrdataFinePenaA"			scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"				scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="MisuraModel"				scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<jsp:useBean id="tipoMisuraSicurezza" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUDS"                   scope="request" class="java.lang.String"/>
<jsp:useBean id="comuneUDS"                 scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaProc"              scope="request" class="java.lang.String"/>

<%
	String FlagIstanza="";
	FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  
  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
  UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
%>

<html>
  <head>
    <title>[S.I.E.S.] -Gestione Misure sicurezza- Richiesta Riesame pericolosità sociale</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript">
	
      var desktop;

    	var desktop;
        function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio){
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
        }
    	
    	function ListaComuni(a_formname,a_fieldname)
    	{
      		desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    	}
   		
    	//Funzione utile per impostare la data corrente.
    	function impostaDataOdierna(campo_giorno, campo_mese,campo_anno, dataOdierna){    
    		day=dataOdierna.substring(0,2);
    		month=dataOdierna.substring(3,5);
    		year=dataOdierna.substring(6,10);
    	    document.getElementsByName(campo_giorno).item(0).value = day;
    	    document.getElementsByName(campo_mese).item(0).value = month;
    	    document.getElementsByName(campo_anno).item(0).value = year;      
    	}

    	function Verify()
  		{
  			var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
  			
		  	if (document.LoadRicRiesamePericolo.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  	document.LoadRicRiesamePericolo.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadRicRiesamePericolo.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  	if (document.LoadRicRiesamePericolo.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  	document.LoadRicRiesamePericolo.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadRicRiesamePericolo.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  	var data_to_verify = document.LoadRicRiesamePericolo.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadRicRiesamePericolo.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadRicRiesamePericolo.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        	if (!ControllaData(data_to_verify) )
		  	{
        		alert('Data di emissione non valida');
        		document.LoadRicRiesamePericolo.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
			   	return false;
		  	}
        	
		    //1) Controllo : data di sistema deve essere >= Data Emissione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
		      alert('Data Emissione non può essere superiore alla data odierna!');
		      document.LoadRicRiesamePericolo.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		      return false;
		    }

        	if (document.LoadRicRiesamePericolo.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
			  	document.LoadRicRiesamePericolo.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadRicRiesamePericolo.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
		  	if (document.LoadRicRiesamePericolo.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
			  	document.LoadRicRiesamePericolo.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadRicRiesamePericolo.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

		  	var data_to_verify = document.LoadRicRiesamePericolo.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'/'+document.LoadRicRiesamePericolo.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'/'+document.LoadRicRiesamePericolo.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
			
		  	if (!ControllaData(data_to_verify) )
		  	{
        		alert('Data Trasmissione non valida');
        		document.LoadRicRiesamePericolo.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
			   	return false;
		  	}

	    	if(document.LoadRicRiesamePericolo.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadRicRiesamePericolo.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
	      	{
		        alert("Il Magistrato è obbligatorio");
		        document.LoadRicRiesamePericolo.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
		        return false;
	      	}
	    	
<% 
				if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))))
	 			{
		  			if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
		  			{
		%>
					      if (document.LoadRicRiesamePericolo.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==1)
							  document.LoadRicRiesamePericolo.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value='0'+document.LoadRicRiesamePericolo.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
						  if (document.LoadRicRiesamePericolo.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==1)
							  document.LoadRicRiesamePericolo.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'+document.LoadRicRiesamePericolo.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;
				
						  var data_to_verifica = document.LoadRicRiesamePericolo.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadRicRiesamePericolo.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadRicRiesamePericolo.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;
		
				      	  if (!ControllaData(data_to_verifica) )
						  {
			          			alert('Data fine pena non valida');
			          			document.LoadRicRiesamePericolo.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.focus();
					 			return false;
				  		  }
		<%
					}
		 		}
%>
		
	//	if( document.LoadRicRiesamePericolo.< %=ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_NOTIFICA%>.value == ""  )
		if( document.LoadRicRiesamePericolo.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value == ""  || 
			 document.LoadRicRiesamePericolo.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value == "-")
		{
            	alert(" UFFICIO di SORVEGLIANZA è OBBLIGATORIO");
            	document.LoadRicRiesamePericolo.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.focus();
            	return false;
    	}
		
		if( document.LoadRicRiesamePericolo.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>[document.LoadRicRiesamePericolo.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E %>.selectedIndex].value != '-') 
	    {
			if( document.LoadRicRiesamePericolo.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>.value == ""  )
	    	{
	            	alert(" SEDE Altra Autorita OBBLIGATORIA");
	            	document.LoadRicRiesamePericolo.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>.focus();
	            	return false;
	    	}
	    }
		
		if(document.LoadRicRiesamePericolo.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_PM %>.value != '-')
		{
			if(document.LoadRicRiesamePericolo.<%=ICostantiNotifica.CAMPO_SEDE_UFFICIO_PM %>.value == "")
			{	
	            	alert(" SEDE UFFICIO PROCURA OBBLIGATORIO ");
	            	document.LoadRicRiesamePericolo.<%=ICostantiNotifica.CAMPO_SEDE_UFFICIO_PM %>.focus();
	            	return false;
			}    	
    	}
		
    }	<% // Chiude function Verify %>

    function ListaMagistrati(a_formname)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }

    // 02/12/2010 Lista Uffici della Sorveglianza ( solo UDS)
    function ListaUDS(a_formname,a_fieldname)
    {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Uffici di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
    
    //  10/02/2015	Altre Procure

    function ListaComuniAutoritaProc(a_formname,a_fieldname,codTipoUfficio)
    {
       if (codTipoUfficio=='PM')
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
       else if (codTipoUfficio=='PGCAP')
          desktop = window.open("/jsp/Main.jsp?Action=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
       else 
         desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function resetSede()
    {
        document.LoadRicRiesamePericolo.<%=ICostantiNotifica.CAMPO_SEDE_UFFICIO_PM%>.value="";
    //    loadUfficiAccorpati('');
      
    }

  </script>
</head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      	<font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Richiesta Accertamento Pericolosità Sociale</font>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" name="LoadRicRiesamePericolo" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActInserisciRichRiesamePericoloSociale">
    <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=5>
          <font class="campo">
          <%=lPosizione.getDescrPosizioneGiuridica()%>
        </font>
        </td>
        <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
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
<%  // Eventuali Misura di Sicurezza  %>

<%
	List lMisure =(List) request.getAttribute("listaMisure");
	if(lMisure.size() > 0)
	{
%>
	    <table>
<%
	    Iterator itx = lMisure.iterator();
	    while ( itx.hasNext())
	    {
		      MisuraSicurezzaModel lMis = (MisuraSicurezzaModel)itx.next();
	%>
		    <tr>
		    	<td class=C>Misura di Sicurezza da espiare</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%>&nbsp;</font></td>
		      	<td class=C>Anni</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(),"0")%>&nbsp;</font></td>
		      	<td class=C>Mesi</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(),"0")%>&nbsp;</font></td>
		      	<td class=C>Giorni</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(),"0")%>&nbsp;</font></td>	
		    </tr>  		
	<% 
	    } %>
	    
	    </table>
<%	}
%>  
	<br>	
	<table>
		<tr>
        <td class="l">Data Emissione <font class="ob">(*)</font></td>
        <td class="L" colspan=2 >
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
	        <a href="Javascript:impostaDataOdierna('<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>','<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>','<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
	          <img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
	        </a>
        </td>
        <td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
        <td class="l">Data Trasmissione <font class="ob">(*)</font></td>
        <td class="L" colspan=2>
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        </td>
      </tr>

     <tr>
   	  <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
    </tr>
  </table>
 
 <table width="90%">
   <tr>
        <td class="l">Tipo Richiesta  <font class="ob">(*)</font></td>
        <td class="l">
          <select title="Tipo Richiesta Misura" name="<%= ICostantiMisuraSicurezza.CAMPO_COD_RICH_MISURA_SIC%>">
          <%=tipoMisuraSicurezza%>
          </select>
        </td>
        <td class="l">Note</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>"  cols=40 ></textarea>
      	</td>
  </tr>
 </table> 
  
  <table width="90%">
     <tr><td class="Titolo" colspan=6> Magistrato Firmatario</td></tr>
<!--Magistrato Firmatario -->     
     <tr>
     <td class="L">Magistrato Firmatario <font class=ob>(*)</font></td>
        <td class="L" colspan="3">
         <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="35">
         <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="35">
           <a href="Javascript:ListaMagistrati('LoadRicRiesamePericolo');">
            <img src="/images/filefolder.gif" border=0>
            </a>
      </td>
      <td>
        <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      </td>
     </tr>

	<tr><td class="Titolo" colspan=6>Destinatari per Sorveglianza</td></tr>
<!--Notifica per l'Ente di Sorveglianza (Revisione del 24/10/2014)-->
  <tr>
	<td class="L">Magistrato di Sorveglianza <font class=ob>(*)</font></td>
    <td class="L">
      <select Title="Magistrato di Sorveglianza" class="small" name="tipoUDS" >
      <%=tipoUDS%>
      </select>
   	</td>
   	<td class="L" COLSPAN=2>
      <input type="text" title="ufficio" value="<%=StringUtils.toStringJSP(comuneUDS,"")%>"  name="<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>" maxlength="35" size="25">
      <a href="Javascript:ListaUfficiComuni('LoadRicRiesamePericolo','<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>'
                                           ,document.LoadRicRiesamePericolo.tipoUDS[document.LoadRicRiesamePericolo.tipoUDS.options.selectedIndex].value);">
         <img src="/images/filefolder.gif" border=0>
      </a> 
    </td>
  </tr>

 <tr><td class="Titolo" colspan=6>Procure</td></tr> 
 <!-- 	Notifica per le Procure --> 
 <tr>
    <td class=l width=25%>Autorità </td>
    <td class=l>
      <select Title="Autorità" name="<%=ICostantiNotifica.CAMPO_COD_UFFICIO_PM %>" onchange="resetSede()">
        <%=autoritaProc%>
      </select>
    </td>
  </tr>
  <tr>
    <td class=l width=25%>Sede</td>
    <td class=l>
      <input type="text" Title="Sede proc" name="<%=ICostantiNotifica.CAMPO_SEDE_UFFICIO_PM %>" value=""  maxlength="35" size="35" readonly="readonly">
      <a href="Javascript:ListaComuniAutoritaProc
      	('LoadRicRiesamePericolo','<%=ICostantiNotifica.CAMPO_SEDE_UFFICIO_PM %>',document.LoadRicRiesamePericolo.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_PM %>[document.LoadRicRiesamePericolo.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_PM %>.selectedIndex].value);"><img src="/images/filefolder.gif" border=0></a>
    </td>
  </tr>

<tr><td class="Titolo" colspan=6>Altri Destinatari</td></tr>  	
<!--Notifica per Altra Autorità -->
	<tr>	
		<td class="L">Altra Autorità </td>
 		<td class="L" >
       		<select  Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
        		<%=autoritaEsternaE%>
       		</select>
      	</td>
      	<td class="l">Note</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols=40 ></textarea>
      	</td>
    </tr>
    
    <tr>
      <td class="l">Sede </td>
      <td class="L">
        <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>" size="35">
        <a href="Javascript:ListaComuni('LoadRicRiesamePericolo','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
	</tr>
	
	<tr> 
    	<td class="lNoBord" colspan="2">
      		<br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    	</td>
  	</tr>
</table>
</form> 
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadRicRiesamePericolo");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","req","Il campo Giorno Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%=  ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","req","Il campo Mese Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","req","Il campo Anno Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2050");

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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.util.Date"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.richiesta.action.ICostantiRichiestaSige"  %>
<%@ page import="siap.sige.detenzione.action.ICostantiFasSigeDetenzione"  %>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel"%>
<%@ page import="siap.sige.richiesta.model.RichiestaSigeModel"%>
<%@ page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige"%>

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAtto" scope="request" class="java.lang.String"/>
<jsp:useBean id="mittenteAtto" scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneGiuridica" scope="request" class="java.lang.String"/>
<jsp:useBean id="posGiuridica" scope="request" class="java.lang.String"/>
<jsp:useBean id="luogoDetenzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="idLuogoDetenzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="idAltraCausa" scope="request" class="java.lang.String"/>
<jsp:useBean id="IDfascicoloSIEP" scope="request" class="java.lang.String"/>
<jsp:useBean id="IDSoggetto" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoGiudizio" scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoSezioni" scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="Modificabile" scope="request" class="java.lang.String" />
<jsp:useBean id="comuneUfficio" scope="request" class="java.lang.String" />

<jsp:useBean id="dataDecisione" scope="request" class="java.util.Date" />
<jsp:useBean id="isFascicoloCollegato" scope="request" class="java.lang.String" />
<jsp:useBean id="idFascicoloSigeOrigine" scope="request" class="java.lang.String" />
<jsp:useBean id="codTenoreDecisione" scope="request" class="java.lang.String" />

<%
// in questa maniera la data non viene istanziata se null !!
Date dataFinePena = (Date) request.getAttribute("dataFinePena");
Date dataAtto = (Date) request.getAttribute("dataAtto");
Date dataArrivoCancelleria = (Date) request.getAttribute("dataArrivoCancelleria");

String descrizioneUfficio=comuneUfficio;
%>

<html>
  <head>
    <title>[S.I.E.S.] - Load Inserisci Procedimento SIGE</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

      function calendario(a_formname,a_field_year,a_field_month,a_field_day)
      {
        desktop = 
            window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
      }
    </script>
 
	<script language="JavaScript">
	function Verify() {
        if (document.LoadInserisciFascicolo.<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>.value.length==1)
            document.LoadInserisciFascicolo.<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>.value;
        if (document.LoadInserisciFascicolo.<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>.value.length==1)
            document.LoadInserisciFascicolo.<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>.value;

        if (document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_FINE_PENA%>.value.length==1)
            document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_FINE_PENA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_FINE_PENA%>.value;
        if (document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_FINE_PENA%>.value.length==1)
            document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_FINE_PENA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_FINE_PENA%>.value;

        // Controllo obbligatorietà tipo atto.
        var tipoAtto=document.LoadInserisciFascicolo.<%= ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO%>[document.LoadInserisciFascicolo.<%= ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO%>.selectedIndex].value;
        var modalita='<%=modalita%>';
        if(tipoAtto =='-' && modalita!='M')
        {
          alert("Il Campo Tipo Atto è obbligatorio");
          return false;
        }

        // Controllo della data atto solo se valorizzata.
        var data_atto=document.LoadInserisciFascicolo.<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiRichiestaSige.CAMPO_ANNO_DATA_EMISSIONE%>.value;
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
        if ( data_atto!='//')
        {
          if (! ControllaData(data_atto))
          {
            alert('Data emissione atto non valida');
            return false;
          }
          // Controllo della data atto <= data di sistema
          if (!CompareDate(data_atto, data_sistema)) {
            alert('Data emissione atto non può essere successiva alla data odierna');
            return false;
          }
        }

        // Controllo della data fine pena solo se valorizzata.
        var data_finepena=document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_FINE_PENA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_FINE_PENA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_FINE_PENA%>.value;
        if (data_finepena!='//') {
          	if (! ControllaData(data_finepena)) {
            	alert('Data fine pena non valida');
            	return false;
          	}
          	// Controllo della data di sistema <= data fine pena
          	// STUB 17/01/2005 aggiunta richiesta di proseguimento.
			if (!CompareDate(data_sistema, data_finepena)) {
            	if (!confirm("Data fine pena precede Data odierna! Si vuole continuare?"))
              		return false;
          	}
		}

        // Controllo della data Arrivo in Cancelleria
        var data_Arrivo=(document.LoadInserisciFascicolo.<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>.value)+'/'+document.LoadInserisciFascicolo.<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiRichiestaSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>.value;
        if (!ControllaData(data_Arrivo)) {
          	alert('Data di Arrivo in Cancelleria non valida');
          	return false;
        }

        // Controllo della data Arrivo <= data di sistema
        if (!CompareDate(data_Arrivo, data_sistema)) {
          	alert('Data di Arrivo in Cancelleria non può essere successiva alla data odierna');
          	return false;
        }

        // Controllo della data atto <= data Arrivo
        if (data_atto!='//' && !CompareDate(data_atto, data_Arrivo)) {
          	alert('Data atto non può essere successiva alla data Arrivo in Cancelleria');
          	return false;
        }
      	return true;
	}

	function ListaMagistrati(a_formname) {
     	var a_codnum = "";
     	window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sige.magistrato.action.ActLoadRicercaMagistratoAssegnazioneLista&formname="+a_formname+"&codnum="+a_codnum, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
	}

	// 20170703: aggiunta funzione di controllo; aggiunta chiamata al caricamento della pagina (onload)
	function controllaDataFinePena() {
		<%
		if (Utils.isPresent(luogoDetenzione) && luogoDetenzione.trim().length() > 0
				&& Utils.isPresent(idLuogoDetenzione)
				&& (idLuogoDetenzione.trim().length() > 0 || idLuogoDetenzione.trim().length() > 0)) {
   		%>
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_VALIDA_LUOGO_DET%>.checked = true;
		<%}%>
		var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
		var data_finepena=document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_FINE_PENA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_FINE_PENA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_FINE_PENA%>.value;
		var luogo;
        if (data_finepena!='//') {
			// se data_finepena è < data_sistema
        	if (!CompareDate(data_sistema, data_finepena)) {        		
				// svuoto i campi data fine pena e cambio posizione giuridica				 
				document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_FINE_PENA%>.value = "";
				document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_FINE_PENA%>.value = "";
				document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_FINE_PENA%>.value = "";
				document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_COD_POSIZIONE_GIURIDICA%>.value = "10";
				document.LoadInserisciFascicolo.<%=ICostantiFascicoloSige.CAMPO_VALIDA_LUOGO_DET%>.checked = false;
				luogo=document.getElementById('myLuogo');				
				luogo.style.display='none';				
			}
        }
	}
	
 	</script>
  	</head>

  	<body class="corpo" onload="controllaDataFinePena();">
  	<table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione:</font>&nbsp;
<%
        Date lDataArrivo = new Date();
        String lAction = new String();
        FascicoloSigeModel lFascicolo = new FascicoloSigeModel();
        RichiestaSigeModel lRichiesta = new RichiestaSigeModel();
        MagistratoModel lMagistrato = new MagistratoModel();
        
        if (FascicoloSigeEsteso.getFascicoloSige() != null)
        	lFascicolo = FascicoloSigeEsteso.getFascicoloSige();
        if (FascicoloSigeEsteso.getRichiestaSige() != null) {
        	lRichiesta = FascicoloSigeEsteso.getRichiestaSige();
        	descrizioneUfficio=lRichiesta.getDescrSedeRichiedente();
        	dataAtto=lRichiesta.getDataEmissione();
        	dataArrivoCancelleria=lRichiesta.getDataArrivoCancelleria();
        } if (FascicoloSigeEsteso.getMagAssegnatario() != null && FascicoloSigeEsteso.getMagAssegnatario().getMagistrato() != null)
        	lMagistrato = FascicoloSigeEsteso.getMagAssegnatario().getMagistrato();
      
        // Si consente la modifica della DATA FINE PENA e POS. GIURIDICA in assenza di titolo esecutivo.
        //MEV 15 - Revisione SIGE
        // La combo Posizione Giuridica rimane disabilitata solo nel caso in cui 
        // per il Procedimento selezionato sia "Espiazione di Pena ...."
        String lDisable = "";  
		String lDisablePosGiu = "";
		String lReadOnly = "";
		String lModificaSoloNoteR = "";
		String lModificaSoloNoteD = "";
		// Iscrizione da Fascicolo SIEP
        if( modalita.equals("IF") )
        {
          lAction = "siap.sige.fascicolo.action.ActInserisciFascicolo";
          
  		  if(lFascicolo.getDataFinePena() != null){
          	dataFinePena = lFascicolo.getDataFinePena();
  		  }
  		  
          if(posGiuridica != null &&
        	(posGiuridica.equals("03") || posGiuridica.equals("11") || posGiuridica.equals("12") || posGiuridica.equals("13") ||
        	 posGiuridica.equals("14") || posGiuridica.equals("15") || posGiuridica.equals("18") || posGiuridica.equals("19") ||
        	 posGiuridica.equals("24") || posGiuridica.equals("25") || posGiuridica.equals("41") || posGiuridica.equals("42") ||
        	 posGiuridica.equals("43") || posGiuridica.equals("44") || posGiuridica.equals("60") || posGiuridica.equals("61"))
          ){
 				// quando la Data Fine Pena è inferiore alla data di sistema la combo 
 				// deve essere abilitata
 				if(dataFinePena != null && DateUtils.isGreater(dataFinePena, DateUtils.getSysDate())){
 					lDisablePosGiu = "disabled";
 				}
          }
          
          // lReadOnly="readonly"; 
%>			
          <font class="campo">Iscrizione Procedimento SIGE da Procedimento SIEP</font>
<%
        } // Iscrizione da Soggetto
        else if( modalita.equals("IS") )
        {
            lAction = "siap.sige.fascicolo.action.ActInserisciFascicolo";
            if(isFascicoloCollegato != null && !isFascicoloCollegato.equals("") && isFascicoloCollegato.equals("true")){
%>
				<font class="campo">Iscrizione Procedimento</font>
<%  
            } else {
%>
          		<font class="campo">Iscrizione Procedimento SIGE da Soggetto</font>
<%
            }
        } // Modifica
        else if( modalita.equals("M") )
        {
        	
        	if (Modificabile.equalsIgnoreCase("NO"))
        	{
        		// Caso in cui è possibile modificare solo NOTE
        		lModificaSoloNoteR = "readonly";
        		lModificaSoloNoteD = "disabled";
        	}
        	
        	
       		dataFinePena = lFascicolo.getDataFinePena();
         
        	// Si consente la modifica della DATA FINE PENA e POS. GIURIDICA in assenza di titolo esecutivo.
        	//MEV 15 - Revisione SIGE
        	// La combo Posizione Giuridica rimane disabilitata solo nel caso in cui 
        	// per il Procedimento selezionato sia "Espiazione di Pena ...."
        	if (FascicoloSigeEsteso.getFascicoloSiep() != null || lModificaSoloNoteR.trim().length() > 0) 
        	{
   				// quando la Data Fine Pena è inferiore alla data di sistema la combo 
   				// deve essere abilitata
   				if(dataFinePena != null && DateUtils.isGreater(dataFinePena, DateUtils.getSysDate())){
	        		lDisablePosGiu = "disabled";
	          		lReadOnly = "readonly";
   				}
   				else{
   					lReadOnly = "";
   				}
      		}
        	lAction = "siap.sige.fascicolo.action.ActModificaFascicolo";
        	lDisable="readonly";
%>
      		<font class="campo">Modifica Procedimento SIGE  </font>
<%  
		// Inserimento Fascicolo per Soggetto Ignoto
        } else if (modalita.equals("ISI")){
            lAction = "siap.sige.fascicolo.action.ActInserisciFascicoloSoggettoIgnoto";
%>
          <font class="campo">Iscrizione Procedimento SIGE da Soggetto Ignoto</font>
<%        	
		}
%>		
        
      </td>
      
      <!-- BOTTONE DI RITORNO -->
      <td class="LBG">
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>

    </tr>
  </table>

<%
  if( modalita.equals("IF") )
  {
%>
    <jsp:include page="/jsp/files/siap/sius/fascicolo/DettaglioSoggettoSentenzaSius.jsp"/>
<%
  }
  if( modalita.equals("IS") || modalita.equals("ISI") )
  {
%>
    <jsp:include page="/jsp/files/siap/sius/fascicolo/DettaglioSoggettoSentenzaSius.jsp"/>
<%
  }
  if( modalita.equals("M") )
  {
%>
    <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
      <table cellspacing=0 cellpadding=0 width=95%>
              <jsp:include page="/jsp/files/siap/sige/fascicolo/IncludeFasSiepRif.jsp"/>   	 
      </table>
 <%
  }
%>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciFascicolo">

  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="label">Fine pena </font>

          <input type="text" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_FINE_PENA%>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"dd"))%>" type="text" maxlength="2" size="2"    onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  <%=lReadOnly%>>
          /
          <input type="text" name="<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_FINE_PENA%>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"MM"))%>" type="text" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" <%=lReadOnly%>>
          /
          <input type="text" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_FINE_PENA%>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"yyyy"))%>" type="text" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)"  <%=lReadOnly%>>

<%
	  // MEV 15 - Revisione SIGE
	  if(lReadOnly == ""){
%>
		<a href="javascript:calendario('LoadInserisciFascicolo','<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_FINE_PENA%>','<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_FINE_PENA%>','<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_FINE_PENA%>');">
      			<img src="/images/calendario.gif" border=0>
       	</a>
<%		  
	  }
%>
     
        <font class="l">&nbsp;&nbsp;Pos. Giuridica </font>
<%
        if( lDisablePosGiu.equals("disabled"))
        {
%>
       <input type=hidden name="<%=ICostantiFascicoloSige.CAMPO_COD_POSIZIONE_GIURIDICA%>" value=<%=posGiuridica%>>
<%
        }
%>
          <select title="posGiuridica" class=small name="<%=ICostantiFascicoloSige.CAMPO_COD_POSIZIONE_GIURIDICA%>" <%=lDisablePosGiu%>>
            <%=posizioneGiuridica%>
          </select>
      </td>
    </tr>
 </table>
<%
    // Gestione del luogo detenzione.
    if(  (luogoDetenzione.trim().length() > 0)  && (idLuogoDetenzione.trim().length() > 0 || idLuogoDetenzione.trim().length() > 0))
    {
%>
 <div id="myLuogo" style="width: 100%; display: block; position: relative;">
  <table>
      <tr>
        <td class="L">
          <font class="label">Detenuto in &nbsp;&nbsp;&nbsp;</font>
          <font class="campo"><%=luogoDetenzione%> &nbsp;&nbsp;&nbsp;&nbsp;</font>
          <input type=checkbox name="<%=ICostantiFascicoloSige.CAMPO_VALIDA_LUOGO_DET%>" value=1 title="Valida il Luogo Detenzione" <%=lDisable%>>
        </td>
      </tr>
   </table>
 </div>       
<%
    }
%> 

  <br>
   <table style="width: 95%;">
   <tr>
      <td class="Titolo" colspan=6> Estremi Atto </td>
   </tr>
</table>
  <table cellspacing="2" cellpadding="2">

  <tr>
    <td class="l">Tipo Atto <font class=ob>(*)</font></td>
    <td class="L">
      <select title="tipoAtto" class=small name="<%=ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO%>" <%=lModificaSoloNoteD%>>
        <%= tipoAtto %>
      </select>
    </td>
  </tr>

  <tr>
    <td class="l">Data Atto</td>
    <td class="L">
      <input  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataAtto,"dd")) %>" type="text" name="<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_EMISSIONE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  <%=lModificaSoloNoteR%>>
      /
      <input  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataAtto,"MM")) %>" type="text" name="<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_EMISSIONE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  <%=lModificaSoloNoteR%>>
      /
      <input  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataAtto,"yyyy"))%>" type="text" name="<%=ICostantiRichiestaSige.CAMPO_ANNO_DATA_EMISSIONE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)"  <%=lModificaSoloNoteR%>>

<%
	  // MEV 15 - Revisione SIGE
	  if(lModificaSoloNoteR == ""){
%>
			<a href="javascript:calendario('LoadInserisciFascicolo','<%=ICostantiRichiestaSige.CAMPO_ANNO_DATA_EMISSIONE%>','<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_EMISSIONE%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
    </td>
  </tr>

  <tr>
    <td class="l">Mittente </td>
    <td class="L">
      <select title="mittenteAtto" class=small name="<%=ICostantiRichiestaSige.CAMPO_COD_TIPO_RICHIEDENTE%>" <%=lModificaSoloNoteD%>>
        <%= mittenteAtto%>
      </select>
      &nbsp;&nbsp;
<%
      if( modalita.equals("M") )
      {
%>
        <input Title="descrMittente" value="<%=StringUtils.toStringJSP(lRichiesta.getDescRichiedente(), "")%>" name="<%=ICostantiRichiestaSige.CAMPO_DESC_RICHIEDENTE%>"type="text" maxlength="200" size="38" <%=lModificaSoloNoteR%> >
        <%}else{%>
        <input Title="descrMittente" name="<%=ICostantiRichiestaSige.CAMPO_DESC_RICHIEDENTE%>"type="text" maxlength="200" size="38">
        <%}%>
    </td>
  </tr>

  <tr>
    <td class="l">Sede Mittente </td>
    <td class="l">
      <input Title="Sede Mittente" name="<%=ICostantiRichiestaSige.CAMPO_SEDE_RICHIEDENTE %>"
         value="<%=StringUtils.toStringJSP(descrizioneUfficio)%>" type="text" maxlength="35" size="35"  <%=lModificaSoloNoteR%> >
      <%if( lModificaSoloNoteR.length() == 0) {%>
         <a href="Javascript:ListaComuni('LoadInserisciFascicolo','<%=ICostantiRichiestaSige.CAMPO_SEDE_RICHIEDENTE%>');">
        <img src="/images/filefolder.gif" border=0></a> <%} %>
     </td>
  </tr>
    <tr>
    <td class="l">Data arrivo in cancelleria <font class=ob>(*)</font></td>
    <td class="L">
      <input  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataArrivoCancelleria,"dd"))%>" type="text" name="<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" <%=lModificaSoloNoteR%>>
      /
      <input  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataArrivoCancelleria,"MM"))%>" type="text" name="<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" <%=lModificaSoloNoteR%>>
      /
      <input  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataArrivoCancelleria,"yyyy"))%>" type="text" name="<%=ICostantiRichiestaSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" <%=lModificaSoloNoteR%>>

<%
	  // MEV 15 - Revisione SIGE
	  if(lModificaSoloNoteR == ""){
%>
			<a href="javascript:calendario('LoadInserisciFascicolo','<%=ICostantiRichiestaSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>','<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>','<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
    </td>
  </tr>
  
</table>
 <br>
   <table style="width: 95%;">
   <tr>
      <td class="Titolo" colspan=6> Estremi Procedimento  </td>
   </tr>
</table>
 <table cellspacing="2" cellpadding="2">
     <tr>
       <td class="l">Magistrato</td>
       <td class="L">
         <input title="Cognome" readonly type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" value="<%=modalita.equals("M") ? StringUtils.toStringJSP(lMagistrato.getCognome()) : ""%>" maxlength="35" size="25">
         <input title= "Nome" readonly  type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>"   value="<%=modalita.equals("M") ? StringUtils.toStringJSP(lMagistrato.getNome()) : ""%>" maxlength="35" size="25">
 <%if( ! modalita.equals("M") ) {%>
  
         <a href="Javascript:ListaMagistrati('LoadInserisciFascicolo');">
           <img src="/images/filefolder.gif" border=0>
         </a>
<%}%>         
       </td>
     </tr>

    <tr>
      <td class="l">Sezione </td>
      <td class="L" >
        <select title="sezione" class=small name="<%=ICostantiFascicoloSige.CAMPO_SEZ_ID_SEZIONE%>"   <%=lModificaSoloNoteD%>>
        <option value = ""  />-
         <%=elencoSezioni%>
        </select>
    </tr>
      <tr>
      <td class="l">Tipo Rito </td>
      <td class="L" >
        <select title="TipoGiudizio" class=small name="<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>"  <%=lModificaSoloNoteD%>>
		<option value = ""  />-
		<%=tipoGiudizio %>       
 		</select>
    	</tr>

  <tr>
    <td class="l">Note</td>
    <td class="l">
      <Textarea Title="Note" name="<%= ICostantiFascicoloSige.CAMPO_NOTE %>" cols=80 rows=5><%=StringUtils.toStringJSP(lFascicolo.getNote()) %></textarea>
    </td>
  </tr>

  <tr>
    <td>
      <input class="bottone" type="submit" value="Conferma">
    </td>
  </tr>

  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
  <input type="HIDDEN" name="<%=ICostantiRichiestaSige.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" value="<%=IDfascicoloSIEP%>" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSige.CAMPO_SOG_ID_SOGGETTO%>" value="<%=IDSoggetto%>"  >
  <input type="HIDDEN" name="<%=ICostantiFasSigeDetenzione.CAMPO_LD_ID_LUOGO_DETENZIONE%>" value="<%=idLuogoDetenzione%>">
  <input type="HIDDEN" name="<%=ICostantiFasSigeDetenzione.CAMPO_AC_ID_ALTRA_CAUSA%>" value="<%=idAltraCausa%>">
  <input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>"  value="">
  <input type="HIDDEN" name="<%=ICostantiFascicoloSige.CAMPO_IS_FASCICOLO_COLLEGATO_RICORSO%>"  value="<%=isFascicoloCollegato%>">
  <input type="HIDDEN" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_DECISIONE_RICORSO_COLLEGATO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataDecisione,"dd"))%>">
  <input type="HIDDEN" name="<%=ICostantiFascicoloSige.CAMPO_MESE_DECISIONE_RICORSO_COLLEGATO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataDecisione,"MM"))%>">
  <input type="HIDDEN" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_DECISIONE_RICORSO_COLLEGATO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataDecisione,"yyyy"))%>">
  <input type="HIDDEN" name="<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE_ORIGINE%>" value="<%=idFascicoloSigeOrigine%>" >
  <input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_COD_TENORE_DECISIONE%>" value="<%=codTenoreDecisione%>" >  
  
  
  <%if(modalita.equals("M")){ %>
  <input type="HIDDEN" name="<%=ICostantiFascicoloSige.CAMPO_COD_STATO_FASCICOLO%>" value="<%=lFascicolo.getCodStatoFascicolo()%>"  >
<%}%>

</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciFascicolo");

    frmvalidator.addValidation("<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiRichiestaSige.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiRichiestaSige.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno Data Atto deve essere di 4 caratteri");

    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_FINE_PENA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_FINE_PENA %>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_FINE_PENA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_FINE_PENA%>","minlen=4","La lunghezza del campo Anno Data Fine Pena Atto deve essere di 4 caratteri");


    frmvalidator.addValidation("<%= ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>","req", "Il campo Giorno Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>","req", "Il campo Mese Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiRichiestaSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","req", "Il campo Anno Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiRichiestaSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiRichiestaSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","minlen=4","La lunghezza del campo Anno Data Arrivo in cancelleria deve essere di 4 caratteri");

    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

  </body>
</html>
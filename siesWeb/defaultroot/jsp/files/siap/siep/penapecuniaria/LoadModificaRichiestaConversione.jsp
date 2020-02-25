<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria"%>

<jsp:useBean id="modalita"           scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaConv" scope="request" class="java.lang.String" />
<jsp:useBean id="richiestaconversione" scope="request" class="siap.siep.penapecuniaria.model.RichiestaConversioneModel"/>

<html>
<head>
  <title> Modifica Richiesta Conversione </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" >
    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================

	var desktop;
	function ListaComuni(a_formname,a_fieldname){
	  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	 }
	   
	
    function Verify() { 
      // Inserire i controlli che non possono essere effettuati dal genvalidator 
      
	// ANNO/NUMERO PARTITA o ci sono entrambi o nessuno 
	if((document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>.value==""
		&& !document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_NUM_PARTITA %>.value=="")
		|| (!document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>.value==""
		&& document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_NUM_PARTITA %>.value=="")){
        alert("anno e numero partita non corretti")
        document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>.focus(); 
         return false;
		}
  
	// Almeno una delle due informazioni tra ANNO/NUMERO PARTITA, NUMERO EX CAMPIONE 
	// deve essere valorizzato.
	if(document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>.value==""
		&& document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_NUM_PARTITA %>.value==""
	   	&& document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_NUM_EX_CAMPIONE %>.value==""
	){
		 		 // 12/05/2010 Eliminato controllo bloccante sui dati di inserimento obbligatori.	
         <%--alert("Almeno uno fra Anno/Numero Partita, Numero Ex Campione, Prot. Circoscrizione Doganale va inserito")
         document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>.focus(); 
         return false; --%>
		 		 if(! confirm("Confermi l'assenza di tutti i dati fra Anno/Numero Partita, Numero Ex Campione, Prot. Circoscrizione Doganale ?" ) )
 		 		 {
      		 document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA%>.focus(); 
   				 return false;
	 	 		 }
	}
	if(document.LoadModificaRichiestaConversione.CodTipoAutoritaEmittente.value == "-"){
	   alert("Autorità obbligatoria");
	   return false;
	}
      //=============================================================
      // controllo correttezza campo 'Data Ricezione Atto' 
      //=============================================================
      var data_to_verify = document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_RICEZIONE_ATTO%>.value+'/'+ 
                           document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_RICEZIONE_ATTO%>.value+'/'+ 
                           document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_RICEZIONE_ATTO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2)
      { 
        alert('Data Ricezione Atto non corretta'); 
        document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_RICEZIONE_ATTO%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Iscrizione Atto' 
      //=============================================================
      var data_to_verify = document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO%>.value+'/'+ 
                           document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ISCRIZIONE_ATTO%>.value+'/'+ 
                           document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ISCRIZIONE_ATTO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Iscrizione Atto non corretta'); 
        document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Esazione' 
      //=============================================================
      var data_to_verify = document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ESAZIONE%>.value+'/'+ 
                           document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ESAZIONE%>.value+'/'+ 
                           document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ESAZIONE%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Esazione non corretta'); 
        document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ESAZIONE%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Prescrizione Multa' 
      //=============================================================
      var data_to_verify = document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_PRESCRIZIONE_MULTA%>.value+'/'+ 
                           document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_PRESCRIZIONE_MULTA%>.value+'/'+ 
                           document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_PRESCRIZIONE_MULTA%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Prescrizione Multa non corretta'); 
        document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_PRESCRIZIONE_MULTA%>.focus(); 
        return false; 
      } 
     //--------------------------------------------------------
      // se multa = vuoto and (data o flag presenti errore)
      // se multa = pieno and (data e flag assenti o presenti errore) 
	if (document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_IMPORTO_MULTA%>INT.value==""
	&& document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_IMPORTO_MULTA%>DEC.value=="")
	{
		if (data_to_verify.length>2 
		|| document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_FLAG_IMPRESCRITTIBILE_MULTA%>.checked == true)
		{
			// se l'importo della multa è vuoto non posso inserire la data prescrizione o avvalorare il flag imprescrittibile
			alert("Importo della multa non inserito");
			document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_MULTA%>INT.focus(); 
			return false;
		}	
	}else{
			// Paolo Cherubini 07/03/2011 eliminato controllo su richiesta di Michele Testa
			if (data_to_verify.length>2 
			&& document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_FLAG_IMPRESCRITTIBILE_MULTA%>.checked == true)
			{
				// se l'importo della multa è pieno devo inserire la data prescrizione o avvalorare il flag imprescrittibile
				// alert("Indicare la data di prescrizione o avvalorare il flag imprescrittibile")
				// document.LoadModificaRichiestaConversione.<-%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_MULTA%>INT.focus(); 
				// return false;
			}
			if(data_to_verify.length<3 
			&& !document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_FLAG_IMPRESCRITTIBILE_MULTA%>.checked == true)
			{
				// se l'importo della multa è pieno devo inserire la data prescrizione o avvalorare il flag imprescrittibile
				// alert("Indicare la data di prescrizione o avvalorare il flag imprescrittibile")
				// document.LoadModificaRichiestaConversione.<-%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_MULTA %>INT.focus(); 
				// return false;
			}
		}
		
      //=============================================================
      // controllo correttezza campo 'Data Prescrizione Ammenda' 
      //=============================================================
      var data_to_verify = document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_PRESCRIZIONE_AMMENDA%>.value+'/'+ 
                           document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_PRESCRIZIONE_AMMENDA%>.value+'/'+ 
                           document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_PRESCRIZIONE_AMMENDA%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Prescrizione Ammenda non corretta'); 
        document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_PRESCRIZIONE_AMMENDA%>.focus(); 
        return false; 
      } 
      
      //--------------------------------------------------------
      // se ammenda = vuoto and (data o flag presenti errore)
      // se ammenda = pieno and (data e flag assenti o presenti errore) 
	if (document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_IMPORTO_AMMENDA%>INT.value==""
	&& document.LoadModificaRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_IMPORTO_AMMENDA%>DEC.value=="")
	{	
		if (data_to_verify.length>2 
		|| document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA%>.checked == true)
		{
			// se l'importo della multa è vuoto non posso inserire la data prescrizione o avvalorare il flag imprescrittibile
			alert("Importo della Ammenda non inserito");
			document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_AMMENDA%>INT.focus(); 
			return false;
		}	
	}else{
			// Paolo Cherubini 07/03/2011 eliminato controllo su richiesta di Michele Testa
			if (data_to_verify.length>2 
			&& document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA%>.checked == true)
			{
				// se l'importo della Ammenda è pieno devo inserire la data prescrizione o avvalorare il flag imprescrittibile
				// alert("Indicare la data di prescrizione o avvalorare il flag imprescrittibile")
				// document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_AMMENDA%>INT.focus(); 
				// return false;
			}
			if(data_to_verify.length<3 
			&& !document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA%>.checked == true)
			{
				// se l'importo della Ammenda è pieno devo inserire la data prescrizione o avvalorare il flag imprescrittibile
				// alert("Indicare la data di prescrizione o avvalorare il flag imprescrittibile")
				// document.LoadModificaRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_AMMENDA %>INT.focus(); 
				// return false;
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
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <%
        String lAzione = new String();
        if( modalita.equals("M") ) {
          lAzione = "siap.siep.penapecuniaria.action.ActModificaRichiestaConversione"; 
        }
        %>
        <font class="campo">Modifica Richiesta Conversione</font>
      </td>
    </tr>
  </table>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN %>" name="LoadModificaRichiestaConversione">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  <input type="HIDDEN" name="<%=ICostantiPenaPecuniaria.CAMPO_ID_RICHIESTA_CONVERSIONE%>" value="<%=richiestaconversione.getIdRichiestaConversione()%>">

  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>     
	<table>
	<tr><td>&nbsp;</td></tr>
    <tr><td class="Titolo" colspan=6>Dati Richiesta Conversione</td></tr>
    <tr>
      <td class="l" colspan="2">Anno/Numero Partita</td>
      <td class="l" colspan="2"> 
        <input type="text" maxlength="4" size="4" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(richiestaconversione.getAnnoPartita()) %>"
               name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>"  
               > /  
        <input type="text" maxlength="9" size="11" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(richiestaconversione.getNumPartita()) %>"
               name="<%= ICostantiPenaPecuniaria.CAMPO_NUM_PARTITA %>"  
               > 
      </td> 
      <td class="l" colspan="2"> 
        <input type="text" maxlength="20" size="20" 
               value="<%=StringUtils.toStringJSP(richiestaconversione.getNumExCampione()) %>"
               name="<%= ICostantiPenaPecuniaria.CAMPO_NUM_EX_CAMPIONE %>"  
               > 
      </td> 
    </tr>
    <tr>
		<td class="l" colspan="2">Autorità<font class="ob">(*)</font></td>
		<td class="L" colspan=4>
			<select Title="Autorità" name="<%= ICostantiPenaPecuniaria.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
			<%=autoritaConv%>
		</select></td>
	</tr>
    <tr>
		<td class="l" colspan="2" >Sede <font class=ob>(*)</font></td>
		<td class="L"colspan="4" ><input Title="Sede"
			name="<%=ICostantiPenaPecuniaria.CAMPO_COD_LUOGO_EMITTENTE%>"
			value="<%=richiestaconversione.getDescrLuogoEmittente()%>" type="text"
			maxlength="35" size="35"> <a
			href="Javascript:ListaComuni('LoadModificaRichiestaConversione','<%= ICostantiPenaPecuniaria.CAMPO_COD_LUOGO_EMITTENTE %>');">
		<img src="/images/filefolder.gif" border=0> </a></td>
	</tr>
    <tr>
      <td class="l" colspan="2">Data Ricezione Atto  <font class=ob>(*)</font></td>
      <td class="l" colspan="4"> 
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataRicezioneAtto(),"dd")) %>" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_RICEZIONE_ATTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataRicezioneAtto(),"MM")) %>" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_RICEZIONE_ATTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataRicezioneAtto(),"yyyy")) %>" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_RICEZIONE_ATTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l" colspan="2">Data Iscrizione Atto  <font class=ob>(*)</font></td>
      <td class="l" colspan="4"> 
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataIscrizioneAtto(),"dd")) %>" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataIscrizioneAtto(),"MM")) %>" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ISCRIZIONE_ATTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataIscrizioneAtto(),"yyyy")) %>" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ISCRIZIONE_ATTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l" colspan="2">Data Richiesta Impossibilità Esazione  <font class=ob>(*)</font></td>
      <td class="l" colspan="4"> 
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataEsazione(),"dd")) %>" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ESAZIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataEsazione(),"MM")) %>" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ESAZIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataEsazione(),"yyyy")) %>" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ESAZIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Multa</td>
      <td class="l"> 
        <input type="text"  maxlength="14" size="16"  ONKEYPRESS="return TicTabNumField(this,event)" 
        	   style="text-align:right" 
               value="<%=StringUtils.getParteIntera(richiestaconversione.getImportoMulta()) %>"
               name="<%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_MULTA%>INT" 
                ><strong>&nbsp;,&nbsp;</strong>
        <input type="text"  maxlength="2" size="2"  ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.getParteDecimale(richiestaconversione.getImportoMulta()) %>"
               name="<%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_MULTA %>DEC"   
               > 
      </td> 
      <td class="l">Data Prescrizione</td>
      <td class="l"> 
        <input type="text" size="2"  maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataPrescrizioneMulta(),"dd")) %>" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_PRESCRIZIONE_MULTA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2"  maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataPrescrizioneMulta(),"MM")) %>" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_PRESCRIZIONE_MULTA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4"  maxlength="4"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataPrescrizioneMulta(),"yyyy")) %>" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_PRESCRIZIONE_MULTA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l">Imprescrittibile</td>
      <td class="l"> 
        <input type="checkbox" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_FLAG_IMPRESCRITTIBILE_MULTA %>"  
                <% if (richiestaconversione.getFlagImprescrittibileMulta().equals("S")){%>
               		checked
               <%}%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Ammenda</td>
      <td class="l"> 
        <input type="text"  maxlength="14" size="16"  ONKEYPRESS="return TicTabNumField(this,event)" style="text-align:right" 
               value="<%=StringUtils.getParteIntera(richiestaconversione.getImportoAmmenda()) %>"
               name="<%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_AMMENDA%>INT" 
               ><strong>&nbsp;,&nbsp;</strong>
        <input type="text"  maxlength="2" size="2"  ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.getParteDecimale(richiestaconversione.getImportoAmmenda()) %>"
               name="<%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_AMMENDA %>DEC"  
               > 
      </td> 
      <td class="l">Data Prescrizione</td>
      <td class="l"> 
        <input type="text" size="2"  maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataPrescrizioneAmmenda(),"dd")) %>" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_PRESCRIZIONE_AMMENDA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2"  maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataPrescrizioneAmmenda(),"MM")) %>" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_PRESCRIZIONE_AMMENDA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4"  maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataPrescrizioneAmmenda(),"yyyy")) %>" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_PRESCRIZIONE_AMMENDA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l">Imprescrittibile</td>
      <td class="l"> 
        <input type="checkbox" 
               name="<%= ICostantiPenaPecuniaria.CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA %>" 
               <% if (richiestaconversione.getFlagImprescrittibileAmmenda().equals("S")){%>
               		checked
               <%}%> 
               > 
      </td> 
    </tr>
    <tr><td>&nbsp;</td></tr>
   <tr>
    	<td class="lNoBord" colspan="2">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>

  </table>
</form>


<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadModificaRichiestaConversione");

  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","req","Il campo Autorità è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Sede è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_RICEZIONE_ATTO %>","req","Il campo Data Ricezione Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_RICEZIONE_ATTO %>","req","Il campo Data Ricezione Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_RICEZIONE_ATTO %>","req","Il campo Data Ricezione Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO %>","req","Il campo Data Iscrizione Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ISCRIZIONE_ATTO %>","req","Il campo Data Iscrizione Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ISCRIZIONE_ATTO %>","req","Il campo Data Iscrizione Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ESAZIONE %>","req","Il campo Data Esazione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ESAZIONE %>","req","Il campo Data Esazione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ESAZIONE %>","req","Il campo Data Esazione è obbligatorio");

<%--
  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  //frmvalidator.addValidation("","req","Il campo XXXX è obbligatorio");
  //frmvalidator.addValidation("","numeric","Il XXXX è un campo numerico");
  //frmvalidator.addValidation("","maxlen=4","La lunghezza massima per XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","minlen=4","La lunghezza minima per XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","gt=1900");
  //frmvalidator.addValidation("","lt=3000");
  //frmvalidator.addValidation("","alphanumeric");
  //frmvalidator.addValidation("","numeric");
  //frmvalidator.addValidation("","alpha");
  //frmvalidator.addValidation("","alnumhyphen");
  //frmvalidator.addValidation("","email");
  //frmvalidator.addValidation("","regexp");
  //frmvalidator.addValidation("","dontselect");
--%>
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
</body>
</html>
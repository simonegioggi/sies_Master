<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria"%>
<%@ page import="siap.siep.penapecuniaria.model.RichiestaConversioneModel"%>
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaConv" scope="request" class="java.lang.String" />
<%
//==============================================================================
// Form utilizzata per L'iscrizione Annotazione Annullamento Partita di Credito
// ( Evento e Richietsa Conversione su un n. procedimento Classe I) 
// - Conversione Pene Pecuniarie 
//==============================================================================

%>

<html>
<head>
<title>Annullamento Partita di credito (Conversione Pene Pecuniarie)</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
<script language="JavaScript">
    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================

	var desktop;
	function ListaComuni(a_formname,a_fieldname)
	{
	  	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}
	   
	
    function Verify() 
    { 
      	// Inserire i controlli che non possono essere effettuati dal genvalidator 
       	//--------------------------------------------------------
 		// ANNO/NUMERO PARTITA o ci sono entrambi o nessuno 
		
		if((document.LoadInserisciAnnullaPartita.<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>.value==""
			&& !document.LoadInserisciAnnullaPartita.<%= ICostantiPenaPecuniaria.CAMPO_NUM_PARTITA %>.value=="")
			|| (!document.LoadInserisciAnnullaPartita.<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>.value==""
			&& document.LoadInserisciAnnullaPartita.<%= ICostantiPenaPecuniaria.CAMPO_NUM_PARTITA %>.value==""))
		{
        		alert("anno e numero partita non corretti")
        		document.LoadInserisciAnnullaPartita.<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>.focus(); 
         		return false;
		}
  
	// Almeno una delle due informazioni tra ANNO/NUMERO PARTITA, NUMERO EX CAMPIONE  
	// deve essere valorizzato.
		if(document.LoadInserisciAnnullaPartita.<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>.value==""
			&& document.LoadInserisciAnnullaPartita.<%= ICostantiPenaPecuniaria.CAMPO_NUM_PARTITA %>.value==""
	   		&& document.LoadInserisciAnnullaPartita.<%= ICostantiPenaPecuniaria.CAMPO_NUM_EX_CAMPIONE %>.value==""
	 )
	    {
         		alert("Almeno uno dei due campi va inserito")
         		document.LoadInserisciAnnullaPartita.<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>.focus(); 
         		return false;
		}
		
		if(document.LoadInserisciAnnullaPartita.CodTipoAutoritaEmittente.value == "-")
		{
	   		alert("Autorità Emittente obbligatoria");
	   		return false;
		}
      //=============================================================
      // controllo correttezza campo 'Data Ricezione Atto' 
      //=============================================================
      	var data_to_verify = document.LoadInserisciAnnullaPartita.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_RICEZIONE_ATTO%>.value+'/'+ 
                           document.LoadInserisciAnnullaPartita.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_RICEZIONE_ATTO%>.value+'/'+ 
                           document.LoadInserisciAnnullaPartita.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_RICEZIONE_ATTO%>.value; 
      	if (!ControllaData(data_to_verify) && data_to_verify.length>2)
      	{ 
        	alert('Data Ricezione Atto non corretta'); 
        	document.LoadInserisciAnnullaPartita.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_RICEZIONE_ATTO%>.focus(); 
        	return false; 
      	} 

      //=============================================================
      // controllo correttezza campo 'Data Iscrizione Atto' 
      //=============================================================
      	var data_to_verify = document.LoadInserisciAnnullaPartita.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO%>.value+'/'+ 
                           document.LoadInserisciAnnullaPartita.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ISCRIZIONE_ATTO%>.value+'/'+ 
                           document.LoadInserisciAnnullaPartita.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ISCRIZIONE_ATTO%>.value; 
      	if (!ControllaData(data_to_verify) && data_to_verify.length>2)
      	{ 
        	alert('Data Iscrizione Atto non corretta'); 
        	document.LoadInserisciAnnullaPartita.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO%>.focus(); 
        	return false; 
      	} 

      //===============================================================
      // controllo correttezza campo 'Data Annulla Partita di Credito' 
      //===============================================================
      	var data_to_verify = document.LoadInserisciAnnullaPartita.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ANNULLA%>.value+'/'+ 
                           document.LoadInserisciAnnullaPartita.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ANNULLA%>.value+'/'+ 
                           document.LoadInserisciAnnullaPartita.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ANNULLA%>.value; 
      	if (!ControllaData(data_to_verify) && data_to_verify.length>2)
      	{ 
        	alert('Data Annulla Partita non corretta'); 
        	document.LoadInserisciAnnullaPartita.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ANNULLA%>.focus(); 
        	return false; 
      	} 
	
      	return true; 
      
	}
  
  </script>
</head>

<body class="corpo">
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();">
			<img align="middle"
			src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
			alt="Stampa questa videata" border=0> </a></td>
		<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
		<font class="campo">Annotazione Annullamento Partita di Credito</font></td>
	</tr>
</table>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN %>"
	name="LoadInserisciAnnullaPartita">
	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.penapecuniaria.action.ActInserisciAnnullaPartita"> 
 
 <jsp:include
	page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp" />
<table>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td class="Titolo" colspan=6>Dati Annullamento Partita di Credito</td>
	</tr>
	<tr>
		<td class="l" colspan="2">Anno/Numero Partita</td>
		<td class="l" colspan="3"><input type="text" maxlength="4"
			size="4" ONKEYPRESS="return TicTabNumField(this,event)"
			name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>"> / <input
			type="text" maxlength="9" size="11"
			ONKEYPRESS="return TicTabNumField(this,event)"
			name="<%= ICostantiPenaPecuniaria.CAMPO_NUM_PARTITA %>"></td>

		<td class="l"><input type="text" maxlength="20" size="20"
			name="<%= ICostantiPenaPecuniaria.CAMPO_NUM_EX_CAMPIONE %>">
		</td>
	</tr>

	<tr>
		<td class="l" colspan="2">Autorità Richiedente <font class="ob">(*)</font></td>
		<td class="L" colspan="4"><select Title="Autorità Emittente"
			name="<%= ICostantiPenaPecuniaria.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
			<%=autoritaConv%>
		</select></td>
	</tr>
	<tr>
		<td class="l" colspan="2">Sede <font class=ob>(*)</font></td>
		<td class="L" colspan="4"><input Title="Sede"
			name="<%=ICostantiPenaPecuniaria.CAMPO_COD_LUOGO_EMITTENTE%>"
			type="text" maxlength="35" size="35"> <a
			href="Javascript:ListaComuni('LoadInserisciAnnullaPartita','<%= ICostantiPenaPecuniaria.CAMPO_COD_LUOGO_EMITTENTE %>');">
		<img src="/images/filefolder.gif" border=0> </a></td>
	</tr>
	<tr>
		<td class="l" colspan="2">Data Ricezione Atto <font class=ob>(*)</font></td>
		<td class="l" colspan="4"><input type="text" size="2" maxlength="2"
			name="<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_RICEZIONE_ATTO %>"
			onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp; <input
			type="text" size="2" maxlength="2"
			name="<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_RICEZIONE_ATTO %>"
			onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp; <input
			type="text" size="4" maxlength="4"
			name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_RICEZIONE_ATTO %>"
			onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillYear(value)"></td>
	</tr>
	<tr>
		<td class="l" colspan="2">Data Iscrizione Atto <font class=ob>(*)</font></td>
		<td class="l" colspan="4"><input type="text" size="2"
			maxlength="2"
			name="<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO %>"
			onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp; <input
			type="text" size="2" maxlength="2"
			name="<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ISCRIZIONE_ATTO %>"
			onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp; <input
			type="text" size="4" maxlength="4"
			name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ISCRIZIONE_ATTO %>"
			onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillYear(value)"></td>
	</tr>
	<tr>
		<td class="l" colspan="2">Data Annullamento</td>
		<td class="l" colspan="4"><input type="text" size="2"
			maxlength="2"
			name="<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ANNULLA %>"
			onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp; <input
			type="text" size="2" maxlength="2"
			name="<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ANNULLA %>"
			onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp; <input
			type="text" size="4" maxlength="4"
			name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ANNULLA %>"
			onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillYear(value)"></td>
	</tr>

	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td class="lNoBord" colspan="2"><input class="bottone"
			type="submit" name="conferma" value="Conferma"></td>
	</tr>

</table>
</form>
</body>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciAnnullaPartita");
	frmvalidator.setAddnlValidationFunction("Verify"); 
	
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","req","Il campo Autorita Emittente è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Sede è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_RICEZIONE_ATTO %>","req","Il campo Data Ricezione Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_RICEZIONE_ATTO %>","req","Il campo Data Ricezione Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_RICEZIONE_ATTO %>","req","Il campo Data Ricezione Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO %>","req","Il campo Data Iscrizione Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ISCRIZIONE_ATTO %>","req","Il campo Data Iscrizione Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ISCRIZIONE_ATTO %>","req","Il campo Data Iscrizione Atto è obbligatorio");
 
	

</script>
</html>
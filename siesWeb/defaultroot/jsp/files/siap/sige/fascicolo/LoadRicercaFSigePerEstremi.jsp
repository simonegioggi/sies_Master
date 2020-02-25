<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.richiesta.action.ICostantiRichiestaSige"%>
<%@ page import="siap.sige.tenore.action.ICostantiTenoreSige"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>

<jsp:useBean id="tipoUfficioSige"       scope="request" class="java.lang.String"/>
<jsp:useBean id="ComuneUfficioConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAtto"              scope="request" class="java.lang.String"/>
<jsp:useBean id="oggettoSige"           scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoSezioni"         scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoRito"              scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"            scope="request" class="java.lang.String"/>

<%
	// Valore di default della funzione
	String lNomeFunzione = "Ricerca Procedimento SIGE per Estremi Atto";
%>
<script language="JavaScript">
  function Init()
  {
		if (document.f.<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>[0].checked)
			VisualizzaEstremiTutti();
		else if (document.f.<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>[1].checked)
			VisualizzaEstremiPendenti();
		else
			VisualizzaEstremiDefiniti();
  }
</script>
<script language="JavaScript">
   var desktop;
  function VisualizzaEstremiTutti()
 	{
		DisabilitaDiv ("EstremiPendenti");
		DisabilitaDiv ("EstremiDefiniti");
 	}

  function VisualizzaEstremiPendenti()
 	{
		AbilitaDiv ("EstremiPendenti");
		DisabilitaDiv ("EstremiDefiniti");
 	}

  function VisualizzaEstremiDefiniti()
 	{
		DisabilitaDiv ("EstremiPendenti");
		AbilitaDiv ("EstremiDefiniti");
 	}
  
  function calendario(a_formname,a_field_year,a_field_month,a_field_day)
  {
    desktop = 
        window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
  }
  </script>

<%@page import="siap.sige.tenore.action.ICostantiTenoreSige"%>
<html>
<head>
  <title>[S.I.E.S.] - Ricerca Procedimento SIGE per Estremi Atto </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=ICostantiFascicoloSige.JS_RICERCA_FASCICOLO%>"></script>
  <script language="JavaScript" src="/html/gestisciUploadStampa2.js"></script>  

</head>
  <body class="corpo" onLoad="Init();">
  <form name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>">
  <input type="HIDDEN" name="<%=ICostantiFascicoloSige.CAMPO_TIPO_UFFICIO%>" value="siap.sige.fascicolo.action.ActRicercaFSigePerEstremi">
  <input type="HIDDEN" name="selFunc" value="" />

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo"><%=lNomeFunzione%></font>

      </td>
    </tr>
  </table>
  <br>
    
  <table width="99%">   
   	<tr>
    	<td class="LBG" > Indicare gli Estremi dell' atto </td>
    </tr>
    <tr>
    	<table width="99%">
    	<tr>
    		<td class="l" width="20%">Tipo Atto/Richiesta pervenuti </td>
      	<td class="l">
      		<select Title="tipoAtto" name="<%=ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO%>" >
        		<%=tipoAtto%>
     			</select>
      	</td>
  		</tr>

  		<tr>
     		<td class="l">Oggetto </td>
      	<td class="l">
      		<select Title="Oggetto" name="<%=ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE%>" >
        		<%=oggettoSige%>
     			</select>
    		</td>
  		</tr>
  		
  		<tr>
     		<td class="l">Magistrato </td>
      	<td class="l">
      		<select Title="Magistrato" name="<%=ICostantiFascicoloSige.CAMPO_COD_MAG_ASS%>" >
        		<%=magistrato%>
     			</select>
    		</td>
  		</tr>

	    <tr>
	      <td class="l">Sezione </td>
	      <td class="L" >
	        <select Title="Sezione" name="<%=ICostantiFascicoloSige.CAMPO_SEZ_ID_SEZIONE%>" >
	         <%=elencoSezioni%>
	        </select>
	    </tr>
	    <tr>
	      <td class="l">Tipo Rito </td>
	      <td class="L" >
	        <select Title="Tipo Rito" name="<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>" >
	         <%=tipoRito%>
	        </select>
	    </tr>
  	</table>
		
	    <tr><td>&nbsp;</td></tr>

  	<table width="99%">
	    <tr>
	      <td colspan='2' class="LBG">Intervallo date di Iscrizione </td>
	    </tr>

      <tr>
        <td colspan='2' class="l">
          <font class="label"> Data Iniziale</font>
          <input type="text" title="Giorno Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" title="Mese Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_MESE_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" title="Anno Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('f','<%=ICostantiFascicoloSige.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>','<%=ICostantiFascicoloSige.CAMPO_MESE_ISCRIZIONE_INIZIALE%>','<%=ICostantiFascicoloSige.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>');">
       		  <img src="/images/calendario.gif" border=0>
          </a> 

          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <font class="label">Data Finale</font>
          <input type="text" title="Giorno Finale" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" title="Mese Finale" name="<%=ICostantiFascicoloSige.CAMPO_MESE_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" title="Anno Finale" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_ISCRIZIONE_FINALE%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('f','<%=ICostantiFascicoloSige.CAMPO_ANNO_ISCRIZIONE_FINALE%>','<%=ICostantiFascicoloSige.CAMPO_MESE_ISCRIZIONE_FINALE%>','<%=ICostantiFascicoloSige.CAMPO_GIORNO_ISCRIZIONE_FINALE%>');">
       	   	  <img src="/images/calendario.gif" border=0>
          </a>
        </td>
      </tr>
  </table>

  </tr>
  </table>
  
  <br>
  <table width="99%">
     <tr>
      <td class="Titolo" width="30%" > Stato Procedimento: </td>
         <td class="Titolo" >
           Tutti <input type="radio" name="<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA %>" value="T"   onClick="VisualizzaEstremiTutti();" checked>&nbsp;&nbsp;&nbsp;&nbsp; 
        		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
        		 
					Solo Pendenti <input type="radio" name="<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>" value="P"   onClick="VisualizzaEstremiPendenti();">&nbsp;
        		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
        		 
					Solo Definiti <input type="radio" name="<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>" value="D"   onClick="VisualizzaEstremiDefiniti();">&nbsp;
				</td>
     </tr>
   </table>
  
   <div id="EstremiPendenti" style="position:relative;  top: 0; left: 0; visibility:hidden; " >  
     <table  width="99%">
	    <tr>
	      <td colspan='2' class="LBG">Indicare la data di fine Pendenza </td>
	    </tr>
      <tr>
        <td class="l">
          <font class="label">Data Fine Pendenza <font class=ob>(*)</font></font>
        		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
          <input type="text" title="Giorno Fine Pendenza" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" title="Mese Fine Pendenza" name="<%=ICostantiFascicoloSige.CAMPO_MESE_FINE_PENDENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" title="Anno Fine Pendenza" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE_PENDENZA%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('f','<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE_PENDENZA%>','<%=ICostantiFascicoloSige.CAMPO_MESE_FINE_PENDENZA%>','<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>');">
       	  	  <img src="/images/calendario.gif" border=0>
          </a>
        </td>
       </tr>
    </table>
  </div>

  <div id="EstremiDefiniti" style="position:relative;  top: -53; left: 0; visibility:hidden; " >  
    <table  width="99%">
	    <tr>
	      <td colspan='2' class="LBG">Intervallo Date Definizione </td>
	    </tr>
      <tr>
        <td class="l">
          <font class="label">Data Iniziale <font class=ob>(*)</font></font>
          <input type="text" title="Giorno Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_DEFINIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" title="Mese Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_MESE_DEFINIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" title="Anno Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_DEFINIZIONE_INIZIALE%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('f','<%=ICostantiFascicoloSige.CAMPO_ANNO_DEFINIZIONE_INIZIALE%>','<%=ICostantiFascicoloSige.CAMPO_MESE_DEFINIZIONE_INIZIALE%>','<%=ICostantiFascicoloSige.CAMPO_GIORNO_DEFINIZIONE_INIZIALE%>');">
       		  <img src="/images/calendario.gif" border=0>
          </a>

          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
          <font class="label">Data Finale <font class=ob>(*)</font></font>
          <input type="text" title="Giorno Finale" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_DEFINIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" title="Mese Finale" name="<%=ICostantiFascicoloSige.CAMPO_MESE_DEFINIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" title="Anno Finale" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_DEFINIZIONE_FINALE%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('f','<%=ICostantiFascicoloSige.CAMPO_ANNO_DEFINIZIONE_FINALE%>','<%=ICostantiFascicoloSige.CAMPO_MESE_DEFINIZIONE_FINALE%>','<%=ICostantiFascicoloSige.CAMPO_GIORNO_DEFINIZIONE_FINALE%>');">
       		  <img src="/images/calendario.gif" border=0>
          </a>
        </td>
       </tr>
    </table>
  </div>
  <br>
  <table style="position:relative;  top: -53; left: 0;">
    <tr>
      <td class="label">
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="ValidatorEstremiAtto('R');">
        <input class="bottone" type="submit" name="STATISTICA" value="Statistica" onClick="ValidatorEstremiAtto('S');">
      </td>
    </tr>
	</table>

 </form>
   <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");
   </script>
   <script language="JavaScript" type="text/javascript">
		// Funzione di Validazione per la Ricerca per Estremi Atto.
		// Viene usata questa funzione per definire i campi da controllare che cambiano dalle opzioni scelte.
		function ValidatorEstremiAtto(azione)
		{
			
			// azione pulsante "Ricerca"
			document.f.selFunc.value=azione;
			if( azione == 'R' ){
				document.f.<%=IWebConstants.ACTION_FIELD%>.value = 'siap.sige.fascicolo.action.ActRicercaFSigePerEstremi';
			} else {
			// azione pulsante "Statistica"
				document.f.<%=IWebConstants.ACTION_FIELD%>.value = 'siap.sige.fascicolo.action.ActRicercaFSigePerEstremiStatistica';
			}
			
			if ( (document.f.<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>)[1].checked)
	     	{
	     		frmvalidator.clearAllValidations();
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>","req","Il campo Giorno Fine Pendenza è obbligatorio");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>","numeric");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>","minlen=2","La lunghezza del campo giorno Fine Pendenza deve essere di 2 caratteri");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_FINE_PENDENZA%>","req","Il campo Mese Fine Pendenza è obbligatorio");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_FINE_PENDENZA%>","numeric");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_FINE_PENDENZA%>","minlen=2","La lunghezza del campo mese Fine Pendenza deve essere di 2 caratteri");
	     		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE_PENDENZA%>","req","Il campo Anno Fine Pendenza è obbligatorio");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE_PENDENZA%>","numeric");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE_PENDENZA%>","minlen=4","La lunghezza del campo Anno Fine Pendenza deve essere di 4 caratteri");
	      		frmvalidator.setAddnlValidationFunction("VerificaEstremiPendenti");    	
	     	}
	  		if ( (document.f.<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>)[2].checked)
	     	{
	     		frmvalidator.clearAllValidations();
	   	    	frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_DEFINIZIONE_INIZIALE%>","req","Il campo Giorno Data Iniziale è obbligatorio");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_DEFINIZIONE_INIZIALE%>","numeric");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_DEFINIZIONE_INIZIALE%>","minlen=2","La lunghezza del campo Giorno Data Iniziale deve essere di 2 caratteri");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_DEFINIZIONE_FINALE%>","req","Il campo Giorno Data Finale è obbligatorio");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_DEFINIZIONE_FINALE%>","numeric");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_DEFINIZIONE_FINALE%>","minlen=2","La lunghezza del campo Giorno Data Finale deve essere di 2 caratteri");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_DEFINIZIONE_INIZIALE%>","req","Il campo Mese Data Iniziale è obbligatorio");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_DEFINIZIONE_INIZIALE%>","numeric");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_DEFINIZIONE_INIZIALE%>","minlen=2","La lunghezza del campo Mese Data Iniziale deve essere di 2 caratteri");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_DEFINIZIONE_FINALE%>","req","Il campo Mese Data Finale è obbligatorio");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_DEFINIZIONE_FINALE%>","numeric");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_DEFINIZIONE_FINALE%>","minlen=2","La lunghezza del campo Mese Data Finale deve essere di 2 caratteri");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_DEFINIZIONE_INIZIALE%>","req","Il campo Anno Data Iniziale è obbligatorio");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_DEFINIZIONE_INIZIALE%>","numeric");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_DEFINIZIONE_INIZIALE%>","minlen=4","La lunghezza del campo Anno Data Iniziale deve essere di 4 caratteri");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_DEFINIZIONE_FINALE%>","req","Il campo Anno Data Finale è obbligatorio");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_DEFINIZIONE_FINALE%>","numeric");
	    		frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_DEFINIZIONE_FINALE%>","minlen=4","La lunghezza del campo Anno Data Finale deve essere di 4 caratteri");
	     		frmvalidator.setAddnlValidationFunction("VerificaEstremiDate");
	     	}
	  		
	  		if ( (document.f.<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>)[0].checked)
	     	{
	     		frmvalidator.setAddnlValidationFunction("CallSincronous");
	     	}
    }
  </script>
  
	<script language="JavaScript" type="text/javascript">
	function VerificaEstremiDate()
 	{
		var ritorno = true;
		var data_iniziale = document.f.<%=ICostantiFascicoloSige.CAMPO_GIORNO_DEFINIZIONE_INIZIALE%>.value + '/' + document.f.<%=ICostantiFascicoloSige.CAMPO_MESE_DEFINIZIONE_INIZIALE%>.value + '/'+ document.f.<%=ICostantiFascicoloSige.CAMPO_ANNO_DEFINIZIONE_INIZIALE%>.value;
		var data_finale = document.f.<%=ICostantiFascicoloSige.CAMPO_GIORNO_DEFINIZIONE_FINALE%>.value + '/' + document.f.<%=ICostantiFascicoloSige.CAMPO_MESE_DEFINIZIONE_FINALE%>.value + '/'+ document.f.<%=ICostantiFascicoloSige.CAMPO_ANNO_DEFINIZIONE_FINALE%>.value;
   		var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

		if (! ControllaData(data_iniziale))
		{
       alert('Data iniziale non valida!');
       ritorno =  false;
		}
		else if (! ControllaData(data_finale))
		{
			alert('Data finale non valida!');
       ritorno =  false;
		}
		// Controllo data finale >= Data iniziale .
		else if( !CompareDate( data_iniziale, data_finale ) )
		{
       alert('Data finale < Data Iniziale!');
       ritorno =  false;
		}
		else if( !CompareDate( data_iniziale, data_sistema ) )
		{
       alert('Data iniziale non può essere superiore alla data di sistema!');
       ritorno =  false;
		}
		else if( !CompareDate( data_finale, data_sistema ) )
		{
			alert('Data finale non può essere superiore alla data di sistema!');
       ritorno =  false;
		}
		
		if (!ritorno)
			return ritorno;

		if (document.f.selFunc.value == 'R')
			return ritorno;
		
		
		//document.f.submit();
		return CallSincronous();
	}

	function VerificaEstremiPendenti()
 	{
   	
		var data_fine_pendenza = document.f.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>.value + '/' + document.f.<%=ICostantiFascicoloSige.CAMPO_MESE_FINE_PENDENZA%>.value + '/'+ document.f.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE_PENDENZA%>.value;
   	    var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

		if (! ControllaData(data_fine_pendenza)) {
           alert('Data fine pendenza non valida!');
           return  false;
		}

		if (document.f.selFunc.value == 'R')
			return true;

		return CallSincronous();
	}
	
	function CallSincronous() {
		if (document.f.selFunc.value == 'R')
			return true;
		
		var params=buildParameters();
		//alert (params);
		stampa2( '<%=ISIAPCostantiWeb.PG_STATISTICA%>', params)
		return false;
	}
	
	function buildParameters () {
		tipoAtto='<%=ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO%>='+document.f.<%=ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO%>.value+'&';
		oggettoSige='<%=ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE%>='+document.f.<%=ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE%>.value+'&';
		magistrato='<%=ICostantiFascicoloSige.CAMPO_COD_MAG_ASS%>='+document.f.<%=ICostantiFascicoloSige.CAMPO_COD_MAG_ASS%>.value+'&';
		sezione='<%=ICostantiFascicoloSige.CAMPO_SEZ_ID_SEZIONE%>='+document.f.<%=ICostantiFascicoloSige.CAMPO_SEZ_ID_SEZIONE%>.value+'&';
		tipoRito='<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>='+document.f.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value+'&';
		
		giornoInizialeIscirizione='<%=ICostantiFascicoloSige.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>=' + document.f.<%=ICostantiFascicoloSige.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value+'&';
		meseInizialeIscirizione='<%=ICostantiFascicoloSige.CAMPO_MESE_ISCRIZIONE_INIZIALE%>=' + document.f.<%=ICostantiFascicoloSige.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value+'&';
		annoInizialeIscirizione='<%=ICostantiFascicoloSige.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>=' + document.f.<%=ICostantiFascicoloSige.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value+'&';
		
		
		giornoFinaleIscirizione='<%=ICostantiFascicoloSige.CAMPO_GIORNO_ISCRIZIONE_FINALE%>=' + document.f.<%=ICostantiFascicoloSige.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value+'&';
		meseFinaleIscirizione='<%=ICostantiFascicoloSige.CAMPO_MESE_ISCRIZIONE_FINALE%>=' + document.f.<%=ICostantiFascicoloSige.CAMPO_MESE_ISCRIZIONE_FINALE%>.value+'&';
		annoFinaleIscrizione='<%=ICostantiFascicoloSige.CAMPO_ANNO_ISCRIZIONE_FINALE%>=' + document.f.<%=ICostantiFascicoloSige.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value+'&';
        	
		tipoRicerca='<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>=T&';
				
		if ( (document.f.<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>)[1].checked)
     	{
			tipoRicerca='<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>=P&'
			tipoRicerca+='<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>='+document.f.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>.value+'&';
			tipoRicerca+='<%=ICostantiFascicoloSige.CAMPO_MESE_FINE_PENDENZA%>='+document.f.<%=ICostantiFascicoloSige.CAMPO_MESE_FINE_PENDENZA%>.value+'&';
			tipoRicerca+='<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE_PENDENZA%>='+document.f.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE_PENDENZA%>.value+'&';
			
        }
  		if ( (document.f.<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>)[2].checked)
     	{
  			tipoRicerca='<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>=D&'
  			tipoRicerca+='<%=ICostantiFascicoloSige.CAMPO_GIORNO_DEFINIZIONE_INIZIALE%>='+document.f.<%=ICostantiFascicoloSige.CAMPO_GIORNO_DEFINIZIONE_INIZIALE%>.value+'&';
  			tipoRicerca+='<%=ICostantiFascicoloSige.CAMPO_MESE_DEFINIZIONE_INIZIALE%>='+document.f.<%=ICostantiFascicoloSige.CAMPO_MESE_DEFINIZIONE_INIZIALE%>.value+'&';
  			tipoRicerca+='<%=ICostantiFascicoloSige.CAMPO_ANNO_DEFINIZIONE_INIZIALE%>='+document.f.<%=ICostantiFascicoloSige.CAMPO_ANNO_DEFINIZIONE_INIZIALE%>.value+'&';
  			tipoRicerca+='<%=ICostantiFascicoloSige.CAMPO_GIORNO_DEFINIZIONE_FINALE%>='+document.f.<%=ICostantiFascicoloSige.CAMPO_GIORNO_DEFINIZIONE_FINALE%>.value+'&';
  			tipoRicerca+='<%=ICostantiFascicoloSige.CAMPO_MESE_DEFINIZIONE_FINALE%>='+document.f.<%=ICostantiFascicoloSige.CAMPO_MESE_DEFINIZIONE_FINALE%>.value+'&';
  			tipoRicerca+='<%=ICostantiFascicoloSige.CAMPO_ANNO_DEFINIZIONE_FINALE%>='+document.f.<%=ICostantiFascicoloSige.CAMPO_ANNO_DEFINIZIONE_FINALE%>.value;
     	}
  		
  		var ret=tipoAtto+sezione+oggettoSige+tipoRito+magistrato+giornoInizialeIscirizione+meseInizialeIscirizione+annoInizialeIscirizione+giornoFinaleIscirizione+meseFinaleIscirizione+annoFinaleIscrizione+tipoRicerca;
  		return ret;
  		
	}
  </script>
  </body>
</html>
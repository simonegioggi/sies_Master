<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>

<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<jsp:useBean id="tipoGiudizio" scope="request" class="java.lang.String"/>
<jsp:useBean id="tiporicerca"  scope="request" class="java.lang.String"/>
<jsp:useBean id="isCollegiale" scope="request" class="java.lang.String"/>

<html>
<head>
  	<title> [S.I.E.S.] - Ricerca Udienza Procedimento - </title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="/html/ControllaData.js"></script>
  	<script language="JavaScript">
  	function Verify() {	  
  		var ritorno = true;
    	var dataUdienzaInizio = 
        	document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+
        	document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value + '/' +
        	document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value;
		if (!controlloTipoRito()) {
			return false;
		}
    	if (dataUdienzaInizio.length <= 2) {
    		alert('Occorre inserire Data Udienza');
      		ritorno = false;
      		return false;
    	} else if (!ControllaData( dataUdienzaInizio)) {
      		alert('Data Udienza non valida');
      		ritorno = false;
      		return false;
    	}
    	return ritorno;
	}

	// Chiamata funzione elenco Udienze.
   	function ListaUdienze(aNomeForm, aNomeCampoGG, aNomeCampoMM, aNomeCampoAA, aNomeCampoLuogo, 
			aNomeCampoIdUdienza, aNomeCampoCollegio, aTipoRito) {
		// Compone il link URL per passare i parametri alla ElencoUdienza.JSP
     	var lLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.udienza.action.ActLoadRicercaUdienzePerProcedimento";
         	lLink += "&formname="+ aNomeForm;
         	lLink += "&campoGG=" + aNomeCampoGG;
         	lLink += "&campoMM=" + aNomeCampoMM;
         	lLink += "&campoAA=" + aNomeCampoAA;
         	lLink += "&campoLuogo=" + aNomeCampoLuogo;
         	lLink += "&campoID=" + aNomeCampoIdUdienza;
         	lLink += "&campoColl=" + aNomeCampoCollegio;
         	lLink += "&tipoRito=" + aTipoRito;

         	if(controlloTipoRito()){
     	desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=800,height=550" );
   	}

   	}

	// Controllo tipo Rito
	function controlloTipoRito() {
		if (document.LoadRicercaUdienzaProcedimento.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value == "-") {
	  		alert('Selezionare il tipo rito!');
	  		document.LoadRicercaUdienzaProcedimento.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.focus();
	    	return false;
	  	}
	  	return true;
	}

	function calendario(a_formname,a_field_year,a_field_month,a_field_day) {
		desktop = window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
	}
  	</script>
</head>

<body class="corpo" onLoad="document.forms['LoadRicercaUdienzaProcedimento'].elements['<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>'].focus()">
  	<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaUdienzaProcedimento">
  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.udienzaprocedimento.action.ActRicercaUdienzaProcedimento">
  	<input type="HIDDEN" name="<%=ICostantiUdienzaProcedimentoSige.CAMPO_UDI_ID_UDIENZA_SIGE%>" value="">
  	<input type="HIDDEN" name="<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>" value="">
  	<input type="HIDDEN" name="<%=ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA%>" value="">
  	<input type="HIDDEN" name="tiporicerca" value="<%=tiporicerca%>">
  	<%-- 20171003: [SG] aggiunti campi nascosti --%>
  	<input type="HIDDEN" name="dataUdienza" value="">
  	<input type="HIDDEN" name="codMagis" value="">
  	<input type="HIDDEN" name="idSezione" value="">
  	<input type="HIDDEN" name="tipoRito" value="">
  	<%-- INTERVENTO PER 11.2.1 --%>
  	<input type="HIDDEN" name="listaIdUdienze" value="">
  	
  	<table width=100%>
    	<tr>
			<td class="LBG">
				<a href="Javascript:window.print();">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
				</a>
			</td>
      		<td class=LBG >
				<font class="label">Funzione :</font>&nbsp;
				<font class="campo">Visualizza Procedimenti fissati per Udienza</font>
			</td>
    	</tr>
  	</table>
  	<br>
  <table cellpadding=2 cellspacing=2 align=center width=100%>
   	<tr>
    <td class="label">Tipo rito <font class="ob">(*)</font> &nbsp;&nbsp;&nbsp;&nbsp;
      <select <%--=isCollegiale.equalsIgnoreCase("true")?"disabled":""--%> title="Tipo rito" name="<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>">
        <%=tipoGiudizio%>
      </select>
    </td>
   </tr>

    <tr>
      <td class="L" colspan=2 >
        <font class="label">Data udienza</font>
        <input onchange="document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienzaProcedimentoSige.CAMPO_UDI_ID_UDIENZA_SIGE%>.value=0"  
									value="" type="text" name="<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>" 
									maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" 
									onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input onchange="document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienzaProcedimentoSige.CAMPO_UDI_ID_UDIENZA_SIGE%>.value=0" 
									value="" type="text" name="<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>"   
									maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" 
									onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input onchange="document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienzaProcedimentoSige.CAMPO_UDI_ID_UDIENZA_SIGE%>.value=0" 
									value="" type="text" name="<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>"	
									maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" 
									onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        
		<!-- MEV 15 - Revisione SIGE -->
		<a href="javascript:calendario('LoadRicercaUdienzaProcedimento','<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>','<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>','<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>');">
			<img src="/images/calendario.gif" border=0>
       	</a>
		<%-- 20171003: impostati i valori com parametri di passaggio --%>
       	<a href="Javascript:ListaUdienze('LoadRicercaUdienzaProcedimento',
           		document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value,
           		document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value,
           		document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value,
           		document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA%>.value,
           		document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienzaProcedimentoSige.CAMPO_UDI_ID_UDIENZA_SIGE%>.value,
           		document.LoadRicercaUdienzaProcedimento.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>.value, 
				document.LoadRicercaUdienzaProcedimento.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value);">
			Seleziona l'Udienza dalla lista
        	<img src="/images/filefolder.gif" border=0>
        </a>       
  
      </td>
		</tr>
		<tr>
			<td class="label" colspan=2>
				<br>
			</td>
		</tr>
		<tr>
      <td class="titolo" colspan=2>
        Selezionare Tipo Ordinamento della Visualizzazione:
      </td>
		</tr>
		<tr>
			<td class="l" colspan=2>
				<input type="radio" name="tipo" value="PP"> Per Progressivo Procedimento
			</td>
		</tr>
		<tr>
			<td class="l" colspan=2>
				<input type="radio" name="tipo" value="CNS"> Per Cognome/Nome Soggetto
				</td>
		</tr>
		<tr>
			<td class="l" colspan=2>
				<input type="radio" name="tipo" value="PGCNS"> Per Posizione Giuridica e Cognome/Nome Soggetto
			</td>
		</tr>
		<tr>
			<td class="l" colspan=2>
				<input type="radio" name="tipo" value="PGPP" CHECKED> Per Posizione Giuridica e Progressivo Procedimento
			</td>
		</tr>
		<tr>
			<td class="label" colspan=2>
				<br>
			</td>
		</tr>
		<tr>
			<td class="titolo" colspan=2>
				<font class="titolo">Selezionare la Tipologia dei Procedimenti da Visualizzare:</font>
			</td>
		</tr>
		<tr>
			<td class="l" colspan=2>Visualizza anche procedimenti unificati
				<input name="CheckUnificazione" type=checkbox value="U">
      </td>
    </tr>
  	</table>
  <table cellpadding=2 cellspacing=2 align=center width=100%>
		<tr>
			<td class="label" colspan=2>
				<br>
			</td>
		</tr>
		<tr>
      <td class="lVerdeNB" colspan=2>
        <font class="lVerde">N.B.: &nbsp;&nbsp; Di norma il sistema visualizza solo procedimenti fissati e non unificati; modificare impostazioni per diverso tipo di visualizzazione.</font>
      </td>
		</tr>

		<tr><td class="label" colspan=2><br></td></tr>
    <tr>
      <td class="label" colspan="2" >
        <input onclick="Javascript:return Verify();" class="bottone" type="submit" name="Conferma" value="Conferma">
      </td>
    </tr>
    <tr>
			<td class="L" colspan=2></td>
		</tr>
	</table>
</form>
</body>
</html>
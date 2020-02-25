<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>
<%@ page import="siap.siep.statis.action.ICostantiStatis"%>

<jsp:useBean id="sysdate" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="listaUfficiAccorpati"	scope="request" class="java.util.Vector"/>
<jsp:useBean id="classe" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="tipologia" 			scope="request" class="java.lang.String"/>

<!-- MEV_39: aggiunta pagina --- LoadRicercaProcedimentiClasseIVeVII.jsp -->
<html>
<head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" type="text/javascript">
    function Verify() {
	    if (document.c.<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.value.length == 1)
	       	document.c.<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.value = '0'
				+ document.c.<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.value;
	    if (document.c.<%= ICostantiStatis.CAMPO_MESE_INIZIALE%>.value.length == 1)
			document.c.<%= ICostantiStatis.CAMPO_MESE_INIZIALE%>.value = '0'
	       		+ document.c.<%= ICostantiStatis.CAMPO_MESE_INIZIALE%>.value;
	    if (document.c.<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>.value.length == 1)
	       	document.c.<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>.value = '0'
	       		+ document.c.<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>.value;
	    if (document.c.<%= ICostantiStatis.CAMPO_MESE_FINALE%>.value.length == 1)
	        document.c.<%= ICostantiStatis.CAMPO_MESE_FINALE%>.value = '0'
	        	+ document.c.<%= ICostantiStatis.CAMPO_MESE_FINALE%>.value;
	    var data_inizio = document.c.<%=ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.value
	    	+ '/' + document.c.<%=ICostantiStatis.CAMPO_MESE_INIZIALE%>.value
	    	+ '/' + document.c.<%=ICostantiStatis.CAMPO_ANNO_INIZIALE%>.value;
	    var data_fine = document.c.<%=ICostantiStatis.CAMPO_GIORNO_FINALE%>.value
	    	+ '/' + document.c.<%=ICostantiStatis.CAMPO_MESE_FINALE%>.value
	    	+ '/' + document.c.<%=ICostantiStatis.CAMPO_ANNO_FINALE%>.value;
		var data_sistema = "<%=sysdate%>";
		var anno_sistema = data_sistema.substring(6);
		// Controllo Form Senza Nessun parametro di ricerca selezionato
		if (document.c.<%= ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>.value.length == 0
				&& document.c.<%= ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>.value.length == 0
				&& document.c.<%= ICostantiStatis.CAMPO_ANNO_SEMESTRE%>.value.length == 0
				&& document.c.<%= ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>.value.length == 0
				&& !ControllaData(data_inizio)
				&& !ControllaData(data_fine)) {
			alert('Nessun Parametro di Ricerca impostato nella pagina');
			document.c.<%=ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.focus();
	    	return false;
		}
		var tipodiStat = 0;
		// Controllo Parametri Data Inizio Periodo/Data Finale Solo se NON sono VUOTE
		if (data_fine == '//' && data_inizio == '//') {
			// Date Vuote
		} else {
			// 20191021 [SG]: rimossa obbligatorieta' solo per Movimento Procedimenti
			// 20191115 [SG]: rimossa obbligatorieta' solo per Procedimenti Pendenti nel Periodo
			if (!("<%=classe%>" == 'IV' && ("<%=tipologia%>" == 'Movimento Procedimenti (Riepilogo Procedimenti Pendenti)'
					|| "<%=tipologia%>" == 'Procedimenti Pendenti nel Periodo'))) {
				if (!ControllaData(data_inizio)) {
		        	alert('Data di inizio periodo non valida - ' + data_inizio);
		        	document.c.<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.focus();
		        	return false;
		      	}
				if (!CompareDate(data_inizio, data_sistema)) {
		        	alert('La Data di inizio periodo non può essere superiore alla Data odierna - ' + data_inizio);
		        	document.c.<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.focus();
		        	return false;
		      	}
				if (!CompareDate(data_inizio, data_fine)) {
		        	alert('La Data di fine periodo non può essere inferiore alla Data di inizio periodo');
		        	document.c.<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>.focus();
		        	return false;
		      	}
			}
	      	if (!ControllaData(data_fine)) {
	        	alert('Data di fine periodo non valida - ' + data_fine);
	        	document.c.<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>.focus();
	        	return false;
	      	}
	      	if (!CompareDate(data_fine, data_sistema)) {
	        	alert('La Data di fine periodo non può essere superiore alla Data odierna');
	        	document.c.<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>.focus();
	        	return false;
	      	}
	      	tipodiStat++;
		}
		// Controllo Parametri Solo Anno Iniziale/Solo Anno Finale Solo se NON sono VUOTI
		if (document.c.<%= ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>.value.length == 0
				&& document.c.<%= ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>.value.length == 0) {
			// Anni Iniziale e Finale VUOTI
		} else {
			// 20191021 [SG]: rimossa obbligatorieta' solo per Movimento Procedimenti
			// 20191115 [SG]: rimossa obbligatorieta' solo per Procedimenti Pendenti nel Periodo
			if (!("<%=classe%>" == 'IV' && ("<%=tipologia%>" == 'Movimento Procedimenti (Riepilogo Procedimenti Pendenti)'
					|| "<%=tipologia%>" == 'Procedimenti Pendenti nel Periodo'))) {
				if (document.c.<%= ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>.value.length == 0) {
					alert('Inserire Anno Iniziale');
		        	document.c.<%= ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>.focus();
		        	return false;
				}
			}
			if (document.c.<%= ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>.value.length == 0) {
				alert('Inserire Anno Finale');
	        	document.c.<%= ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>.focus();
	        	return false;
			}
			if (document.c.<%= ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>.value < document.c.<%= ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>.value) {
				alert('Anno Iniziale NON può essere Minore di Anno Finale');
	        	document.c.<%= ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>.focus();
	        	return false;
			}
			if (anno_sistema < document.c.<%= ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>.value) {
				alert("Anno Iniziale NON può essere Maggiore dell'Anno corrente");
	        	document.c.<%= ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>.focus();
	        	return false;
			}
			if (anno_sistema < document.c.<%= ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>.value) {
				alert("Anno Finale NON può essere Maggiore dell'Anno corrente");
	        	document.c.<%= ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>.focus();
	        	return false;
			}
			tipodiStat++;
		}
		// Controllo parametri Trimestre
		if (document.c.<%= ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>.value.length > 0) {
			// Selezionare almeno 1 trimestre
			if (!document.c.Trimestre[0].checked
					&& !document.c.Trimestre[1].checked
					&& !document.c.Trimestre[2].checked
					&& !document.c.Trimestre[3].checked) {
				alert('Selezionare il Trimestre \n (oppure i Trimestri purchè CONSECUTIVI)');
	        	document.c.<%= ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>.focus();
	        	return false;
			}
			// selezionare più trimestri, ma CONSECUTIVI
			if (document.c.Trimestre[0].checked
					&& !document.c.Trimestre[1].checked
					&& document.c.Trimestre[2].checked) {
				alert('Selezionare SOLO Trimestri CONSECUTIVI)');
	        	document.c.<%= ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>.focus();
	        	return false;
			}
			if (document.c.Trimestre[0].checked
					&& document.c.Trimestre[3].checked
					&& (!document.c.Trimestre[1].checked
							|| !document.c.Trimestre[2].checked)) {
				alert('Selezionare SOLO Trimestri CONSECUTIVI)');
		       	document.c.<%= ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>.focus();
	        	return false;
			}
			if (document.c.Trimestre[1].checked
					&& document.c.Trimestre[3].checked
					&& !document.c.Trimestre[2].checked) {
				alert('Selezionare SOLO Trimestri CONSECUTIVI)');
		       	document.c.<%= ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>.focus();
	        	return false;
			}
			if (anno_sistema < document.c.<%= ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>.value) {
				alert("Anno Trimestre NON può essere Maggiore dell'Anno corrente");
	        	document.c.<%= ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>.focus();
	        	return false;
			}
			tipodiStat++;
		}
		// Controllo parametri Semestre
		if (document.c.<%= ICostantiStatis.CAMPO_ANNO_SEMESTRE%>.value.length > 0) {
			if (!document.c.Semestre[0].checked
					&& !document.c.Semestre[1].checked) {
				alert('Selezionare Primo e/o Secondo Semestre');
	        	document.c.<%= ICostantiStatis.CAMPO_ANNO_SEMESTRE%>.focus();
	        	return false;
			}
			if (anno_sistema < document.c.<%= ICostantiStatis.CAMPO_ANNO_SEMESTRE%>.value) {
				alert("Anno Semestre NON può essere Maggiore dell'Anno corrente");
	        	document.c.<%= ICostantiStatis.CAMPO_ANNO_SEMESTRE%>.focus();
	        	return false;
			}
			tipodiStat++;
		}
		// Controllo Form con + di un parametro di ricerca selezionato
		if (tipodiStat > 1) {
			alert('Scegliere solo un tipo di ricerca\n1) Data Inizio Periodo / Data Fine Periodo;\n2) Anno Iniziale / Anno Finale;\n3) Anno Trimeste;\n4) Anno Semestre;');
			document.c.<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.focus();
        	return false;		
		}
		// INDIRIZZAMENTO
		if ("<%=classe%>" == 'VII') {
			// Riepilogo Iscrizioni ed Attività
			document.c.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.statis.action.ActCreaRiepilogoIscrizioniAttivitaCPP";
			if ("<%=tipologia%>" == 'Tempi Iscrizione Fascicoli') {
				document.c.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.statis.action.ActCreaStatisticaTempiIscrizioneFascicoliCPP";
			} else if ("<%=tipologia%>" == 'Riepilogo Procedimenti Pendenti') {
				document.c.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.statis.action.ActLoadCreaRiepilogo_CPP";
			}
		} else if ("<%=classe%>" == 'IV') {
			// Riepilogo Iscrizioni e Tipologia Misura
			document.c.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.statis.action.ActCreaRiepilogoIscrizioniTipologiaMisura";
			if ("<%=tipologia%>" == 'Procedimenti Pendenti nel Periodo') {
				document.c.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.statis.action.ActCreaRiepilogoProcedimentiPendentiPeriodo";
			} else if ("<%=tipologia%>" == 'Movimento Procedimenti (Riepilogo Procedimenti Pendenti)') {
				document.c.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.statis.action.ActCreaRiepilogoMovimentoProcedimenti";
			} else if ("<%=tipologia%>" == 'Riepilogo Ispettivo') {
				document.c.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.statis.action.ActCreaRiepilogoIspettivo";
			} else if ("<%=tipologia%>" == 'Attività Magistrati') {
				document.c.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.statis.action.ActCreaRiepilogoAttivitaMagistrati";
			}
		}

		// valore di ritorno
        return true;
    } // CHIUDE Verify()

    function enableBtn() {
    	document.c.btnconf.disabled = false;
    }

    function calendario(a_formname,a_field_year, a_field_month, a_field_day) {
        window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }
    </script>
</head>

<BODY class="corpo">
  	<table>
	  	<tr>
	      	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
	        <td class="LBG">
	          <font class="label">Funzione:</font>&nbsp;
	          <font class="campo">RICERCA PROCEDIMENTI CLASSE&nbsp;<%=classe%></font>
	        </td>
<!-- 	        <td class="LBG"> -->
<!-- 				<a href="javascript:history.go(-1);"> -->
<!-- 					<img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"> -->
<!-- 				</a> -->
<!-- 			</td> -->
	     </tr>
	</table>
	<br>
	<FORM method="POST" name="c">
	<table style="width: 95%;">
		<tr>
	        <td class="l">Classe Procedimento:&nbsp;<%=classe%></td>
	    </tr>
	    <tr>
	 	    <td class="l">Tipologia Statistica:&nbsp;<%=tipologia%></td>
	   	</tr>
	</table>
	<br>
	<table style="width: 95%;">
		<tr><td class="Titolo" colspan="4">Intervallo Date</td></tr>
		<tr>
	        <td class="l" width="20%">Data Inizio Periodo</td>
	      	<td class="L" width="20%">
	        	<input type="text" name="<%=ICostantiStatis.CAMPO_GIORNO_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
	        	<input type="text" name="<%=ICostantiStatis.CAMPO_MESE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
	        	<input type="text" name="<%=ICostantiStatis.CAMPO_ANNO_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	        	<a href="javascript:calendario('c','<%=ICostantiStatis.CAMPO_ANNO_INIZIALE%>','<%=ICostantiStatis.CAMPO_MESE_INIZIALE%>','<%=ICostantiStatis.CAMPO_GIORNO_INIZIALE%>');">
       				<img src="/images/calendario.gif" border="0">
        		</a>
		   	</td>
	 	    <td class="l" width="20%">Data Finale</td>
	      	<td class="L">
	        	<input type="text" name="<%=ICostantiStatis.CAMPO_GIORNO_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
	        	<input type="text" name="<%=ICostantiStatis.CAMPO_MESE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
	        	<input type="text" name="<%=ICostantiStatis.CAMPO_ANNO_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
	        	<a href="javascript:calendario('c','<%=ICostantiStatis.CAMPO_ANNO_FINALE%>','<%=ICostantiStatis.CAMPO_MESE_FINALE%>','<%=ICostantiStatis.CAMPO_GIORNO_FINALE%>');">
       				<img src="/images/calendario.gif" border="0">
        		</a>
	      	</td>
	   	</tr>
	</table>
	<br>
	<table style="width: 95%;">
		<tr><td class="Titolo" colspan="4">Intervallo Anno</td></tr>
		<tr>
	        <td class="l" width="20%">Anno Iniziale</td>
	      	<td class="L" width="20%">
	        	<input type="text" name="<%=ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		   	</td>
	 	    <td class="l" width="20%">Anno Finale</td>
	      	<td class="L">
	        	<input type="text" name="<%=ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
	      	</td>
	   	</tr>
	</table>
	<br>
	<table style="width: 95%;">
		<tr><td class="Titolo" colspan="6">Trimestre</td></tr>
	   	<tr>
	        <td class="l" width="20%">Anno</td>
	      	<td class="L" width="20%">
	        	<input type="text" name="<%=ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		   	</td>
		   	<td class="l">Primo&nbsp;<input type="checkbox" name="Trimestre" value="1"/></td>
		   	<td class="l">Secondo&nbsp;<input type="checkbox" name="Trimestre" value="2"/></td>
		   	<td class="l">Terzo&nbsp;<input type="checkbox" name="Trimestre" value="3"/></td>
		   	<td class="l">Quarto&nbsp;<input type="checkbox" name="Trimestre" value="4"/></td>
		</tr>
	</table>
	<br>
	<table style="width: 95%;">
		<tr><td class="Titolo" colspan="4">Semestre</td></tr>
		<tr>
	        <td class="l" width="20%">Anno</td>
	      	<td class="L" width="20%">
	        	<input type="text" name="<%=ICostantiStatis.CAMPO_ANNO_SEMESTRE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		   	</td>
		   	<td class="l">Primo&nbsp;<input type="checkbox" name="Semestre" value="1"/></td>
		   	<td class="l">Secondo&nbsp;<input type="checkbox" name="Semestre" value="2"/></td>
	   	</tr>
	</table>
	<br>
	<table style="width: 95%;">
		<tr>
			<td class="L">
	     		<font class="label" style="color:green; font-size: 10pt">N.B. Se non si seleziona un Ufficio Accorpato, la funzione effettua l'elaborazione per Ufficio Accorpante</font>
	     	</td>
		</tr>
		<tr>	
	     	<td class="L">
	     		<font class="label" style="color:green; font-size: 10pt">Se si desidera l'elaborazione relativa al solo Ufficio Accorpato al momento della chiusura, selezionarlo nella successiva combo</font>
	     	</td>
		</tr>
	</table> 
	<br>
	<table>
		<tr>
			<td class="l">Ufficio Accorpato</td>
<%
if (listaUfficiAccorpati.size() == 0) {
%>
			<td>
				<select name="<%=ICostantiStatis.CAMPO_COD_ACCORPATO_1%>">
					<option value="-" selected="selected">-</option>
				</select>
			</td>
<%
} else {
%>
			<td>
				<select name="<%=ICostantiStatis.CAMPO_COD_ACCORPATO_1%>">
					<option value="-" selected="selected">-</option>
<%
	Iterator itx = listaUfficiAccorpati.iterator();
	while (itx.hasNext()) {
		UfficioAccorpatoModel lUff = (UfficioAccorpatoModel) itx.next();
%>
					<option value="<%=lUff.getCodUfficio()%>"><%=lUff.getDescrizione()%></option>
<%
	}
%>
				</select>
			</td>
<%
}
%>		
		</tr>
	</table>	  		
	<br>
	<table>
		<tr>
	     	<td>
	      		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
	        	<input type="submit" value="Conferma" class="bottone" name="btnconf">
	      	</td>
		</tr>
	</table>
	</FORM>
	<script language="JavaScript" type="text/javascript">
  	var frmvalidator  = new Validator("c");

	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_GIORNO_INIZIALE%>","numeric","Il campo Giorno Data Inizio Periodo può avere solo caratteri numerici");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_GIORNO_INIZIALE%>","maxlen=2","La lunghezza massima per il giorno Data Inizio Periodo è di 2 caratteri");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_GIORNO_INIZIALE%>","gt=1", "Giorno Data Inizio Periodo non valido");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_GIORNO_INIZIALE%>","lt=31", "Giorno Data Inizio Periodo non valido");

	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_GIORNO_FINALE%>","numeric","Il campo Giorno Data Finale può avere solo caratteri numerici");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_GIORNO_FINALE%>","maxlen=2","La lunghezza massima per il giorno Data Finale è di 2 caratteri");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_GIORNO_FINALE%>","gt=1", "Giorno Data finale non valido");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_GIORNO_FINALE%>","lt=31", "Giorno Data finale non valido");

	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_MESE_INIZIALE%>","numeric","Il campo Mese Data Inizio Periodo può avere solo caratteri numerici");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_MESE_INIZIALE%>","maxlen=2","La lunghezza massima per il mese Data Inizio Periodo è di 2 caratteri");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_MESE_INIZIALE%>","gt=1", "Mese Data Inizio Periodo non valido");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_MESE_INIZIALE%>","lt=12", "Mese Data Inizio Periodo non valido");

	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_MESE_FINALE%>","numeric","Il campo Mese Data Finale può avere solo caratteri numerici");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_MESE_FINALE%>","maxlen=2","La lunghezza massima per il mese Data Finale è di 2 caratteri");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_MESE_FINALE%>","gt=1", "Mese Data finale non valido");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_MESE_FINALE%>","lt=12", "Mese Data finale non valido");

	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_INIZIALE%>","numeric","Il campo Anno Data Inizio Periodo può avere solo caratteri numerici");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_INIZIALE%>","maxlen=4","La lunghezza massima per anno Data Inizio Periodo è di 4 caratteri");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_INIZIALE%>","minlen=4","La lunghezza minima per anno Data Inizio Periodo è di 4 caratteri");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_INIZIALE%>","gt=1900", "Anno Data Inizio Periodo non valido");

	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_FINALE%>","numeric","Il campo Anno Data Finale può avere solo caratteri numerici");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_FINALE%>","maxlen=4","La lunghezza massima per anno Data Finale è di 4 caratteri");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_FINALE%>","minlen=4","La lunghezza minima per anno Data Finale è di 4 caratteri");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_FINALE%>","gt=1900", "Anno Data finale non valido");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_FINALE%>","lt=3000", "Anno Data finale non valido");

	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>","numeric","Il campo Anno Iniziale può avere solo caratteri numerici");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>","maxlen=4","La lunghezza massima per Anno Iniziale è di 4 caratteri");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>","minlen=4","La lunghezza minima per Anno Iniziale è di 4 caratteri");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>","gt=1900", "Anno Iniziale non valido");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>","lt=3000", "Anno Iniziale non valido");

	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>","numeric","Il campo Anno Finale può avere solo caratteri numerici");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>","maxlen=4","La lunghezza massima per Anno Finale è di 4 caratteri");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>","minlen=4","La lunghezza minima per Anno Finale è di 4 caratteri");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>","gt=1900", "Anno Finale non valido");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>","lt=3000", "Anno Finale non valido");

	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>","numeric","Il campo Anno Trimestre può avere solo caratteri numerici");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>","maxlen=4","La lunghezza massima per Anno Trimestre è di 4 caratteri");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>","minlen=4","La lunghezza minima per Anno Trimestre è di 4 caratteri");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>","gt=1900", "Anno Trimestre non valido");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>","lt=3000", "Anno Trimestre non valido");

	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_SEMESTRE%>","numeric","Il campo Anno Semestre può avere solo caratteri numerici");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_SEMESTRE%>","maxlen=4","La lunghezza massima per Anno Semestre è di 4 caratteri");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_SEMESTRE%>","minlen=4","La lunghezza minima per Anno Semestre è di 4 caratteri");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_SEMESTRE%>","gt=1900", "Anno Semestre non valido");
	frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_SEMESTRE%>","lt=3000", "Anno Semestre non valido");

	frmvalidator.setAddnlValidationFunction("Verify");
	</script>
</body>
</html>
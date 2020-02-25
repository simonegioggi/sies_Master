<%-- MEV_65: aggiunta pagina per gestire nuova funzionalita' --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.cancassfascsius.action.ICostantiCancAssFascSius"%>
<%@ page import="siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel"%>

<jsp:useBean id="cancellerie" scope="request" class="java.util.Vector"/>

<html>
<head>
  	<title>[S.I.E.S.] - Ricerca  Copertine Fascicoli SIUS</title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

    <script language="JavaScript">
    function Verify() {
    	var data_iniziale = document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value+'/'+document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value+'/'+document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value;
		var data_finale = document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value+'/'+document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>.value+'/'+document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value;
		var data_sistema = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

		var annoFinale = document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.value;
		var annoIniziale = document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.value;
		var numeroFinale = document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>.value;
		var numeroIniziale = document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>.value;

		if (data_iniziale == "//" && data_finale == "//" && annoIniziale.length == 0 && numeroIniziale.length == 0
				&& annoFinale.length == 0 && numeroFinale.length == 0) {
			alert('Valorizzare obbligatoriamente Data Iniziale e Finale oppure i campi Anno/Numero Iniziale e Finale!');
			if (data_iniziale == "//")
				document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.focus();
			else
				document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.focus();
		   	return false;
		}

		// 20191021: [SG] aggiunto controllo di esclusione (o uno o l'altro)
		if (data_iniziale != "//" && data_finale != "//" && annoIniziale.length != 0 && numeroIniziale.length != 0
				&& annoFinale.length != 0 && numeroFinale.length != 0) {
			alert('Eseguire la ricerca per Intervallo Procedimenti o per Intervallo Date Procedimenti!');
			document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.focus();
		   	return false;
		}

		// se solo una delle due date e' valorizzata
		if (data_iniziale == "//" && data_finale != "//") {
			alert('Valorizzare la Data Iniziale!');
			document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.focus();
		   	return false;
		}
		if (data_iniziale != "//" && data_finale == "//") {
			alert('Valorizzare la Data Finale!');
			document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.focus();
		   	return false;
		}

		// se solo uno dei due campi Anno/Numero Iniziale e' valorizzato
		if (annoIniziale.length == 0 && numeroIniziale.length != 0) {
			alert('Valorizzare l\'Anno Iniziale!');
			document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.focus();
		   	return false;
		}
		if (annoIniziale.length != 0 && numeroIniziale.length == 0) {
			alert('Valorizzare il Numero Iniziale!');
			document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>.focus();
		   	return false;
		}
		// se solo uno dei due campi Anno/Numero Finale e' valorizzato
		if (annoFinale.length == 0 && numeroFinale.length != 0) {
			alert('Valorizzare l\'Anno Finale!');
			document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.focus();
		   	return false;
		}
		if (annoFinale.length != 0 && numeroFinale.length == 0) {
			alert('Valorizzare il Numero Finale!');
			document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>.focus();
		   	return false;
		}
		// se solo uno dei due campi Anno/Numero Iniziale o Finale e' valorizzato
		if (annoIniziale.length == 0 && numeroIniziale.length == 0 && annoFinale.length != 0 && numeroFinale.length != 0) {
			alert('Valorizzare l\'Anno/Numero Iniziale!');
			document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.focus();
		   	return false;
		}
		if (annoIniziale.length != 0 && numeroIniziale.length != 0 && annoFinale.length == 0 && numeroFinale.length == 0) {
			alert('Valorizzare l\'Anno/Numero Finale!');
			document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.focus();
		   	return false;
		}

		// controllo sugli anni/numeri
      	if (parseInt(annoIniziale) > parseInt(annoFinale)) {
			alert('Anno finale minore dell\'anno iniziale.');
			document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.focus();
         	return false;
       	}
      	if ((parseInt(annoIniziale) == parseInt(annoFinale))
      			&& (parseInt(numeroIniziale) > parseInt(numeroFinale))) {
         	alert('Progressivo finale minore del progressivo iniziale.');
         	document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>.focus();
         	return false;
       	}
      	var annoSistema = <%=DateUtils.getSysDate("yyyy")%>;
      	if (parseInt(annoFinale) > annoSistema) {
         	alert('Anno finale maggiore dell\'anno corrente.');
         	document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.focus();
         	return false;
       	}

		if (data_iniziale != "//" && data_finale != "//") {
	 		if (!ControllaData(data_iniziale)) {
	    		alert('Data iniziale non valida!');
	    		document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.focus();
	    		return false;
	  		} else if (!ControllaData(data_finale)) {
	    		alert('Data finale non valida!');
	    		document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.focus();
	    		return false;
	  		} else if (!CompareDate(data_iniziale, data_finale)) {
	    		alert('Data finale minore della Data Iniziale!');
	    		document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.focus();
	    		return false;
	  		} else if (!CompareDate(data_iniziale, data_sistema) ) {
	      	  	alert('Data iniziale maggiore della Data attuale!');
	      	  	document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.focus();
	      	  	return false;
	        } else if (!CompareDate(data_finale, data_sistema) ) {
	      	  	alert('Data finale maggiore della Data attuale!');
	      	  	document.lrcfs.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.focus();
	      	  	return false;
	        }
		}

		return true;
    }

    function valorizzaCancelleria() {
    	document.lrcfs.descCancelleria.value = document.lrcfs.<%=ICostantiCancAssFascSius.CAMPO_COD_CANCELLERIA_ASSEGNATARIA%>[document.lrcfs.<%=ICostantiCancAssFascSius.CAMPO_COD_CANCELLERIA_ASSEGNATARIA%>.selectedIndex].text;
    }

    function calendario(a_formname, a_field_year, a_field_month, a_field_day) {
    	desktop = window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }
	</script>
</head>
<body class="corpo">
	<table>
       	<tr>
       		<td class="LBG">
       			<a href="Javascript:window.print();">
       				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
       			</a>
       		</td>
           	<td class="LBG">
         	<td class="LBG">
           		<font class="label">Funzione:</font>&nbsp;<font class="campo">Ricerca Copertine Fascicoli SIUS</font>
			</td>
		</tr>
	</table>
	<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="lrcfs">
	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.stampa.action.ActRicercaCopertineFascicoliSius">
   	<table style="width: 96%;">
       	<tr><td class="Titolo" colspan="4">Intervallo Procedimenti</td></tr>
       	<tr>
         	<td class="c" width="15%">
           		<font class="label">Anno/Numero Iniziale</font>
         	</td>
         	<td class="l" width="30%">
           		<input type="text" title="Anno Procedimento Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
           		/
           		<input type="text" title="Numero Procedimento Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
         	</td>
         	<td class="c" width="15%">
           		<font class="label">Anno/Numero Finale</font>
         	</td>
         	<td class="l">
           		<input type="text" title="Anno Procedimento Finale" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
           		/
           		<input type="text" title="Numero Procedimento Finale" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
         	</td>
		</tr>
	</table>
	<br>
	<table style="width: 96%;">
		<tr>
			<td class="Titolo" colspan ="4">Intervallo Date Procedimenti</td>
        </tr>
        <tr>
          	<td class="c" width="15%">
            	<font class="label">Data Iniziale</font>
          	</td>
          	<td class="l" width="30%">
            	<input type="text" title="Giorno Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            	<input type="text" title="Mese Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            	<input type="text" title="Anno Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
            	<a href="javascript:calendario('lrcfs','<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>','<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>','<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>');">
					<img src="/images/calendario.gif" border="0">
	          	</a>
          	</td>
          	<td class="c" width="15%">
            	<font class="label">Data Finale</font>
          	</td>
          	<td class="l">
            	<input type="text" title="Giorno Finale" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            	<input type="text" title="Mese Finale" name="<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            	<input type="text" title="Anno Finale" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
            	<a href="javascript:calendario('lrcfs','<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>','<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>','<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>');">
					<img src="/images/calendario.gif" border="0">
	          	</a>
          	</td>
		</tr>
	</table>
	<br>
	<table style="width: 96%;">
		<tr>
			<td class="cliccabile">Scelta competenza territoriale dei procedimenti</td>
		</tr>
		<tr>
        	<td class="l" width="45%">Visualizza solo i procedimenti dell'Ufficio:</td>
       		<td class="l">
            	<input type="radio" name="<%=ICostantiFascicoloSius.CAMPO_INCLUDE_UFFICIO%>" value="0">
       		</td>
		</tr>
		<tr>
        	<td class="l" width="45%">Visualizza solo i procedimenti dell'Utente:</td>
       		<td class="l">
            	<input type="radio" name="<%=ICostantiFascicoloSius.CAMPO_INCLUDE_UFFICIO%>" checked="checked" value="1">
       		</td>
		</tr>
		<tr>
			<td class="l">Visualizza i procedimenti assegnati alla Cancelleria:</td>
            <td class="l">
            	<input type="HIDDEN" name="descCancelleria" value="">
            	<select name="<%=ICostantiCancAssFascSius.CAMPO_COD_CANCELLERIA_ASSEGNATARIA%>" class="small">
            		<option value="" selected> - </option>
<%
String cod;
String desc;
for (int i = 0; i < cancellerie.size(); i++) {
	cod = ((CancelleriaAssegnatariaModel) cancellerie.get(i)).getCodCancelleriaAssegnataria();
    desc= cod + " - " + ((CancelleriaAssegnatariaModel) cancellerie.get(i)).getDescCancelleriaAssegnataria();
%>
         			<option value="<%=cod%>"><%=desc%></option>
<%
}
%>
      			</select>
         	</td>
      	</tr>
	</table>
	<br>
	<table>
		<tr>
    		<td class="l">
           		<input class="bottone" type="submit" name="STAMPA" value="Stampa" onclick="valorizzaCancelleria();">
			</td>
		</tr>
	</table>
	</form>

	<script language="javascript">
	var frmvalidator  = new Validator("lrcfs");

	// ANNO e NUMERO
<%-- 	frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>","req"); --%>
<%-- 	frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>","req"); --%>
<%-- 	frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>","req"); --%>
<%-- 	frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>","req"); --%>
	frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>","numeric");
<%-- 	frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>","maxlen=6","La lunghezza massima per il Numero Procedimento Iniziale è di 6 caratteri"); --%>
	frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>","numeric");
<%-- 	frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>","maxlen=6","La lunghezza massima per il Numero Procedimento Finale è di 6 caratteri"); --%>
	frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>","numeric");
<%-- 	frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>","maxlen=4","La lunghezza massima per l'Anno Procedimento Iniziale è di 4 caratteri"); --%>
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>","minlen=4","La lunghezza minima per l'Anno Procedimento Iniziale è di 4 caratteri");
	frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>","numeric");
<%-- 	frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>","maxlen=4","La lunghezza massima per l'Anno Procedimento Finale è di 4 caratteri"); --%>
	frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>","minlen=4","La lunghezza minima per l'Anno Procedimento Finale è di 4 caratteri");

	// 	DATE
<%-- 	frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>","req","Il campo Giorno Data Iniziale è obbligatorio"); --%>
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>","minlen=2","La lunghezza del campo Giorno Data Iniziale deve essere di 2 caratteri");
<%--     frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>","req","Il campo Giorno Data Finale è obbligatorio"); --%>
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>","minlen=2","La lunghezza del campo Giorno Data Finale deve essere di 2 caratteri");
<%--     frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>","req","Il campo Mese Data Iniziale è obbligatorio"); --%>
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>","minlen=2","La lunghezza del campo Mese Data Iniziale deve essere di 2 caratteri");
<%--     frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>","req","Il campo Mese Data Finale è obbligatorio"); --%>
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>","minlen=2","La lunghezza del campo Mese Data Finale deve essere di 2 caratteri");
<%--     frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","req","Il campo Anno Data Iniziale è obbligatorio"); --%>
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","minlen=4","La lunghezza del campo Anno Data Iniziale deve essere di 4 caratteri");
<%--     frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>","req","Il campo Anno Data Finale è obbligatorio"); --%>
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>","minlen=4","La lunghezza del campo Anno Data Finale deve essere di 4 caratteri");

	frmvalidator.setAddnlValidationFunction("Verify");
	</script>
	
</body>
</html>
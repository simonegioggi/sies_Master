<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2019-09: aggiunta pagina per le statistiche --%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"%>

<html>
<head>
<title> [S.I.E.S.] - Ricerca Procedimenti Misure Alternative (Art. 678 Comma 1 Ter c.p.p.)</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src="/html/ControllaData.js"></script>
<script language="JavaScript">
function init() {
	document.LoadRicercaStatisticaMisureAlter678c1tercpp.<%=ICostantiStatistiche.CAMPO_ANNO_INI%>.focus();
}
function Verify() {																																																	
 	var data_to_verify = document.LoadRicercaStatisticaMisureAlter678c1tercpp.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIO%>.value
 		+ '/' + document.LoadRicercaStatisticaMisureAlter678c1tercpp.<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_INIZIO%>.value
 		+ '/' + document.LoadRicercaStatisticaMisureAlter678c1tercpp.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIO%>.value;
	if (!ControllaData(data_to_verify) && data_to_verify.length > 2) {
 		alert('Data Iscrizione iniziale non valida!');
 		return false;
	}
	var data_to_verify = document.LoadRicercaStatisticaMisureAlter678c1tercpp.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_FINE%>.value
		+ '/' + document.LoadRicercaStatisticaMisureAlter678c1tercpp.<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_FINE%>.value
		+ '/' + document.LoadRicercaStatisticaMisureAlter678c1tercpp.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_FINE%>.value;
	if (!ControllaData(data_to_verify) && data_to_verify.length > 2) {
    	alert('Data Iscrizione finale non valida!');
    	return false;
  	}
	return true;
}
</script>
</head>

<body class="corpo" onLoad="javascript:init()">
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaStatisticaMisureAlter678c1tercpp">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.statistiche.action.ActRicercaStatisticaMisureAlter678c1tercpp">
<table>
 	<tr>
 		<td class="LBG">
 			<a href="Javascript:window.print();">
 				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
 			</a>
 		</td>
   		<td class="LBG">
	   		<font class="label">Funzione :</font> 
	   		<font class="campo">Statistiche - Procedimenti Misure Alternative (Art. 678 Comma 1 Ter c.p.p.)</font>
   		</td>
 	</tr>
</table>
<br>
<table width="100%">
	<tr>
   		<td class="Titolo">Intervallo Estremi Procedimenti</td>
   	</tr>
   	<tr>
       	<td class="c" width="61%">Anno/Numero Iniziale 
       		<input Title="Anno Iniziale" type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_INI%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
       		/<input Title="Numero Iniziale" type="text" name="<%=ICostantiStatistiche.CAMPO_NUM_INI%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
			&nbsp;&nbsp; Anno/Numero Finale &nbsp;&nbsp;<input Title="Anno Finale" type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_FINE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
			/<input Title="Numero Finale" type="text" name="<%=ICostantiStatistiche.CAMPO_NUM_FINE%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
		</td>
	</tr>
</table>
<br>
<table width="100%">
	<tr>
  		<td class="Titolo">Intervallo Date Iscrizione</td>
  	</tr>
  	<tr>
      	<td class="c" width="61%">Data Iscrizione Iniziale <input Title="dalla Data" type="text" name="<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	      	/<input Title="dalla Data" type="text" name="<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_INIZIO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	      	/<input Title="dalla Data" type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			&nbsp;&nbsp;Data Iscrizione Finale&nbsp;&nbsp;<input Title="alla Data" type="text" name="<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_FINE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			/<input Title="alla Data" type="text" name="<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_FINE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			/<input Title="alla Data" type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_FINE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
       	</td>
	</tr>
</table>
<br>
<table width="100%">
 	<tr>
 		<td class="Titolo" colspan="2">Tipologia Statistica</td>
 	</tr>
 	<tr>
     	<td class="l" width="50%">  
        	Procedimenti Privi di Provvedimenti
        </td>
        <td class="l">
        	<input type="radio" name="<%=ICostantiStatistiche.RADIO_RICERCA_STATISTICA_MA%>" value="<%=ICostantiStatistiche.VALUE_RICERCA_PROCEDIMENTI_PRIVI_PROVVEDIMENTI%>" checked/>
        </td>
	</tr>
 	<tr>
     	<td class="l" width="50%">  
        	Ordinanze Non Emesse - Atti al Presidente
        </td>
        <td class="l">
        	<input type="radio" name="<%=ICostantiStatistiche.RADIO_RICERCA_STATISTICA_MA%>" value="<%=ICostantiStatistiche.VALUE_RICERCA_ORD_NON_EMESSE_ATTI_AL_PRESIDENTE%>"/>
        </td>
	</tr>
 	<tr>
        <td class="l">
      		Procedimenti con Magistrato Designato - Ordinanze Non emesse
		</td>
		<td class="l">
        	<input type="radio" name="<%=ICostantiStatistiche.RADIO_RICERCA_STATISTICA_MA%>" value="<%=ICostantiStatistiche.VALUE_RICERCA_ORD_NON_EMESSE%>"/>
        </td>
	</tr>
 	<tr>
 		<%-- MEV_2024-092: cambio messaggio da Provvisoria a Misure Alternative Dl 123/2018 ed eliminata l'ultima voce 
 		VALUE_RICERCA_ORD_APPLICAZIONE_PROVVISORIA_EMESSE_NO_DECISIONE_COLLEGIO --%>
		<td class="l">
			Ordinanze Applicazione Misure Alternative Dl 123/2018 Emesse ma prive di Data di Esecutivita&#768;
		</td>
		<td class="l">
        	<input type="radio" name="<%=ICostantiStatistiche.RADIO_RICERCA_STATISTICA_MA%>" value="<%=ICostantiStatistiche.VALUE_RICERCA_ORD_APPLICAZIONE_PROVVISORIA_EMESSE_NO_DATA_ESECUTIVITA%>"/>
        </td>
	</tr>
<!--  	<tr> -->
<!-- 		<td class="l"> -->
<!-- 			Ordinanze Emesse con Data Esecutività Inserita ma Prive di Decisione del Collegio -->
<!-- 		</td> -->
<!-- 		<td class="l"> -->
<%--         	<input type="radio" name="<%=ICostantiStatistiche.RADIO_RICERCA_STATISTICA_MA%>" value="<%=ICostantiStatistiche.VALUE_RICERCA_ORD_APPLICAZIONE_PROVVISORIA_EMESSE_NO_DECISIONE_COLLEGIO%>"/> --%>
<!--         </td> -->
<!--    	</tr> -->
</table>
<br>
<table cellspacing="2" cellpadding="2">
	<tr><td>&nbsp;</td></tr>
   	<tr>
		<td>
       		<INPUT onclick="Javascript:return Verify();" class="bottone" type="submit" name="RICERCA" value="Ricerca">
      	</td>
	</tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadRicercaStatisticaMisureAlter678c1tercpp");

frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIO%>","numeric");
frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_INIZIO%>","numeric");
frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIO%>","numeric");
frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIO%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_FINE%>","numeric");
frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_FINE%>","numeric");
frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_FINE%>","numeric");
frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_FINE%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");
</script>
</body>
</html>
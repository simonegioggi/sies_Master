<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.magistratocompetente.action.ICostantiMagistratoCompetente"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="aMagistrato"        scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="aListaProcedimenti" scope="request" class="java.util.Vector"/>
<jsp:useBean id="AzioneChiamante"    scope="request" class="java.lang.String"/>
<%-- MEV_2025-48: aggiunta nuova funzionalita': paginata la ricerca --%>
<jsp:useBean id="totaleProcedimenti"	scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Form per la visualizzazione del risultato della ricerca dei procedimenti 
// attivi per un certo magistrato
//==============================================================================
int totale = new Integer(totaleProcedimenti).intValue();
%>
<html>
<head>
<title>[S.I.E.S.] - Elenco Procedimenti per Magistrato</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
function Verify() {
	if (document.ElencoProcedimentiPerMagistratoCompetente.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value == "") {
		alert("Il Cognome del Magistrato è obbligatorio");
		return false;
	}
    if (document.ElencoProcedimentiPerMagistratoCompetente.<%=ICostantiMagistrato.CAMPO_NOME %>.value == "") {
		alert("Il Nome del Magistrato è obbligatorio");
		return false;
	}
    if (document.ElencoProcedimentiPerMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>.value.length == 1)
		document.ElencoProcedimentiPerMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>.value = '0' +
		document.ElencoProcedimentiPerMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>.value;
	if (document.ElencoProcedimentiPerMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>.value.length == 1)
		document.ElencoProcedimentiPerMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>.value = '0' +
		document.ElencoProcedimentiPerMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>.value;
	var data_to_verify = document.ElencoProcedimentiPerMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>.value
		+ '/' + document.ElencoProcedimentiPerMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>.value
		+ '/' + document.ElencoProcedimentiPerMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO%>.value;
	if (!ControllaDataPassaVuota(data_to_verify)) {
		alert('Data di inzio competenza non valida');
		return false;
	}
	var contaSelezionati = 0;
	if (typeof (document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.length) == "undefined") {
		contaSelezionati = contaSelezionati + 1;
    } else {
		for (var i = 0; i < document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.length; i++) {
			if (document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare[i].checked)
				contaSelezionati = contaSelezionati + 1;
		}
    }
<%
if (totale > 20) {
%>
	if (contaSelezionati == 0 && !document.ElencoProcedimentiPerMagistratoCompetente.selezionaAll.checked) {
<%
} else {
%>
	if (contaSelezionati == 0) {
<%
}
%>
		alert('Selezionare almeno un procedimento');
		return false;
	}
	return true;
}

function ListaMagistrati(a_formname) {
	var desktop;
	// n.b. viene passato il parametro codnum=xxx per evitare che la pop up precarichi il campo Data Inizio Competenza
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoAssegnazioneLista&formname="+a_formname+"&codnum=xxx", "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}
      
function selezionaTutti() {
	if (typeof (document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.length) == "undefined") {
		document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.checked = true;
    } else {
	  	for (var i = 0; i < document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.length; i++) {
	    	document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare[i].checked = true;
	  	}
    }
}

function deselezionaTutti() {
	if (typeof (document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.length) == "undefined") {
		document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.checked = false;
    } else {
		for (var i = 0; i < document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.length; i++) {
	    	document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare[i].checked = false;
		}
    }
}

<%
if (totale > 20) {
%>
function selezionaTuttiProcedimenti() {
	if (!document.ElencoProcedimentiPerMagistratoCompetente.selezionaAll.checked) {
		deselezionaTutti();
		document.ElencoProcedimentiPerMagistratoCompetente.selezionaAll.checked = true;
		document.ElencoProcedimentiPerMagistratoCompetente.nomeSelezionaAll.value = 'Deseleziona Tutti i ' + <%=totaleProcedimenti%> + ' Procedimenti del Magistrato Competente';
		document.ElencoProcedimentiPerMagistratoCompetente.nomeSelezionaTutti.disabled = true;
		document.ElencoProcedimentiPerMagistratoCompetente.nomeDeselezionaTutti.disabled = true;
		if (typeof (document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.length) == "undefined") {
			document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.disabled = true;
	    } else {
		  	for (var i = 0; i < document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.length; i++) {
		    	document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare[i].disabled = true;
		  	}
	    }
	} else {
		document.ElencoProcedimentiPerMagistratoCompetente.selezionaAll.checked = false;
		document.ElencoProcedimentiPerMagistratoCompetente.nomeSelezionaAll.value = 'Seleziona Tutti i ' + <%=totaleProcedimenti%> + ' Procedimenti del Magistrato Competente';
		document.ElencoProcedimentiPerMagistratoCompetente.nomeSelezionaTutti.disabled = false;
		document.ElencoProcedimentiPerMagistratoCompetente.nomeDeselezionaTutti.disabled = false;
		if (typeof (document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.length) == "undefined") {
			document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.disabled = false;
	    } else {
		  	for (var i = 0; i < document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.length; i++) {
		    	document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare[i].disabled = false;
		  	}
	    }
	}
}

function selezionaTuttiProcedimentiDaCheckbox() {
	if (document.ElencoProcedimentiPerMagistratoCompetente.selezionaAll.checked) {
		deselezionaTutti();
		document.ElencoProcedimentiPerMagistratoCompetente.nomeSelezionaAll.value = 'Deseleziona Tutti i ' + <%=totaleProcedimenti%> + ' Procedimenti del Magistrato Competente';
		document.ElencoProcedimentiPerMagistratoCompetente.nomeSelezionaTutti.disabled = true;
		document.ElencoProcedimentiPerMagistratoCompetente.nomeDeselezionaTutti.disabled = true;
		if (typeof (document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.length) == "undefined") {
			document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.disabled = true;
	    } else {
		  	for (var i = 0; i < document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.length; i++) {
		    	document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare[i].disabled = true;
		  	}
	    }
	} else {
		document.ElencoProcedimentiPerMagistratoCompetente.nomeSelezionaAll.value = 'Seleziona Tutti i ' + <%=totaleProcedimenti%> + ' Procedimenti del Magistrato Competente';
		document.ElencoProcedimentiPerMagistratoCompetente.nomeSelezionaTutti.disabled = false;
		document.ElencoProcedimentiPerMagistratoCompetente.nomeDeselezionaTutti.disabled = false;
		if (typeof (document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.length) == "undefined") {
			document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.disabled = false;
	    } else {
		  	for (var i = 0; i < document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare.length; i++) {
		    	document.ElencoProcedimentiPerMagistratoCompetente.idFascicoloDaAggiornare[i].disabled = false;
		  	}
	    }
	}
}
<%
}
%>
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
	        <font class="label">Funzione :</font>&nbsp;&nbsp;
	        <font class="campo">Cambio Magistrato Competente</font>
      	</td>
      	<td class="LBG">
        	<a href="javascript:history.back()">
          		<img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        	</a>
      	</td>
	</tr>
</table>
<FORM method="POST" name="ElencoProcedimentiPerMagistratoCompetente" action="<%= IWebConstants.PG_MAIN%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.magistratocompetente.action.ActModificaMultiplaMagistratoAssegnatario">
<input type="HIDDEN" name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value=<%=AzioneChiamante%>>
<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" value="">
<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>_OLD" value="<%=StringUtils.toStringJSP(aMagistrato.getCodMagistrato())%>">
<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_NOME%>_OLD" value="<%=StringUtils.toStringJSP(aMagistrato.getNome())%>">
<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>_OLD" value="<%=StringUtils.toStringJSP(aMagistrato.getCognome())%>">
<%
if (aListaProcedimenti.size() > 0) {
%>
<table>
	<tr>
		<td class="Titolo" colspan="2">
			Elenco Procedimenti Assegnati a: <%=StringUtils.toStringJSP(aMagistrato.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(aMagistrato.getNome())%>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="Titolo" colspan="2"> Nuovo Magistrato Assegnatario </td>
	</tr>
	<tr>
		<td class="l">Magistrato</td>
		<td class="L">
			<input title="Cognome Magistrato" readonly value="" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25">
			<input title="Nome Magistrato" readonly value="" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="25">
			<a href="Javascript:ListaMagistrati('ElencoProcedimentiPerMagistratoCompetente');">
				<img src="/images/filefolder.gif" border="0">
			</a>
		</td>
	</tr>
	<tr>
		<td class="l">Data Inizio Competenza</td>
		<td class="L">
			<input value="<%=DateUtils.getDateToString(DateUtils.getSysDate(), "dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getDateToString(DateUtils.getSysDate(), "MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getDateToString(DateUtils.getSysDate(), "yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
</table>
<br>
<%
//========================================================================
// Elenco dei fascicoli assegnati
//========================================================================
%>
<%-- MEV_2025-48: aggiunta nuova funzionalita': paginata la ricerca --%>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<br>
<table>
	<tr>
		<td class="c">
          	<input type="button" onclick="javascript:selezionaTutti();" name="nomeSelezionaTutti" value="Seleziona Tutti nella pagina">
          	&nbsp;
          	<input type="button" onclick="javascript:deselezionaTutti();" name="nomeDeselezionaTutti" value="Deseleziona Tutti nella pagina">
<%
	if (totale > 20) {
%>
          	&nbsp;oppure&nbsp;
          	<input type="button" onclick="javascript:selezionaTuttiProcedimenti();" name="nomeSelezionaAll" value="Seleziona Tutti i <%=totaleProcedimenti%> Procedimenti del Magistrato Competente">
          	&nbsp;<input type="checkbox" name="selezionaAll" value="selezionaAll" onclick="javascript:selezionaTuttiProcedimentiDaCheckbox();">
<%
	}
%>
		</td>
	</tr>
</table>
<%
}
%>
<br>
<table cellspacing="2" cellpadding="2" align="center" width="95%">
	<tr>
	  	<td class="int" colspan="3">Totale Procedimenti Trovati: <%=aListaProcedimenti.size()%></td>
	</tr>
	<tr>
		<td class="int">Numero SIEP</td>
		<td class="int">Magistrato Assegnatario</td>
		<td class="int">Cambio Magistrato</td>
	</tr>
<%
if (aListaProcedimenti.size() == 0) {
%>
	<tr>
  		<td class="c" colspan="3">
  			<font class="label">Nessun procedimento risulta attualmente assegnato al Magistrato <%=StringUtils.toStringJSP(aMagistrato.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(aMagistrato.getNome())%></font>
  		</td>
	</tr>
<%
}
for (int i = 0; i < aListaProcedimenti.size(); i++) {
	FascicoloSiepModel lFascicolo = (FascicoloSiepModel) aListaProcedimenti.elementAt(i);
%>
	<tr>
		<td class="c"><font class="label"><%=StringUtils.toStringJSP(lFascicolo.getChiaveAnno(),"")%>/<%=StringUtils.toStringJSP(lFascicolo.getChiaveProgr(),"")%></font></td>
		<td class="c"><font class="label"><%=StringUtils.toStringJSP(aMagistrato.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(aMagistrato.getNome())%></font></td>
		<td class="c"><font class="label"><input type="checkbox" name="idFascicoloDaAggiornare" value="<%=StringUtils.toStringJSP(lFascicolo.getIdFascicoloSiep())%>;<%=StringUtils.toStringJSP(lFascicolo.getChiaveAnno(),"")%>/<%=StringUtils.toStringJSP(lFascicolo.getChiaveProgr(),"")%>"></font></td>
	</tr>
<%
}
%>
	<tr><td>&nbsp;</td></tr>
<%
if (aListaProcedimenti.size() > 0) {
%>
	<tr>
	  	<td class="lNoBord" colspan="2">
	    	<INPUT class="bottone" type="submit" name="I" value="Conferma">
	  	</td>
	</tr>
<%
}
%>
</table>
</form>
<%
if (aListaProcedimenti.size() > 0) {
%>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("ElencoProcedimentiPerMagistratoCompetente");

frmvalidator.addValidation("<%=ICostantiMagistrato.CAMPO_COGNOME %>","req","Il Cognome del Magistrato è obbligatorio");
frmvalidator.addValidation("<%=ICostantiMagistrato.CAMPO_COGNOME %>","req","Il Nome del Magistrato è obbligatorio");

frmvalidator.addValidation("<%=ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO%>","numeric");
frmvalidator.addValidation("<%=ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO%>","lt=3000");

frmvalidator.addValidation("<%= ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>","req","Il campo Giorno Inizio Validità è obbligatorio");
frmvalidator.addValidation("<%= ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>","numeric");

frmvalidator.addValidation("<%= ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>","req","Il campo Mese Inizio Validità è obbligatorio");
frmvalidator.addValidation("<%= ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>","numeric");

frmvalidator.setAddnlValidationFunction("Verify");
</script>
<%
}
%>
</body>
</html>
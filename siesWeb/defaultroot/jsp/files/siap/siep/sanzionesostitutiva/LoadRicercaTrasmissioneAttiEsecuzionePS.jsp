<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-33: aggiunta pagina per Gestione Trasmissione Atti per l'Esecuzione (pena sostitutiva) --%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.jms.action.ICostantiSicoJMS"%>
<%@ page import="siap.siep.jms.action.ICostantiSiepJMS"%>

<jsp:useBean id="UtenteConnesso"	scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="tipoUfficio"    	scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"       	scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Riscontro Trasmissioni Atti per Esecuzione Pena Sostitutiva</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
function Verify() {
	var data_inizio = document.LoadRicercaTrasmissioneAttiEsecuzione.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>.value
		+ '/' + document.LoadRicercaTrasmissioneAttiEsecuzione.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>.value
		+ '/' + document.LoadRicercaTrasmissioneAttiEsecuzione.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>.value;
	var data_fine = document.LoadRicercaTrasmissioneAttiEsecuzione.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>.value
		+ '/' + document.LoadRicercaTrasmissioneAttiEsecuzione.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>.value
		+ '/' + document.LoadRicercaTrasmissioneAttiEsecuzione.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>.value;
	if (!ControllaDataPassaVuota(data_inizio)) {
		alert('Data iniziale non valida');
		return false;
	}
	if (!ControllaDataPassaVuota(data_fine)) {
		alert('Data finale non valida');
		return false;
	}
	if (data_inizio.length>2 && data_fine.length > 2) {
  		if (!CompareDate(data_inizio,data_fine)) {
			alert('La Data di trasmissione finale non può essere inferiore alla data iniziale');
			return false;
  		}
	}
	// Non è possibile specificare solo il tipo ufficio o solo la sede del Fascicolo SIEP.
	if ((document.LoadRicercaTrasmissioneAttiEsecuzione.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value.length > 1)
			&& (document.LoadRicercaTrasmissioneAttiEsecuzione.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.value.length == 0)) {
		alert("Valorizzare la Sede Ufficio");
		return false;
	}
	if ((document.LoadRicercaTrasmissioneAttiEsecuzione.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value.length == 1)
			&& (document.LoadRicercaTrasmissioneAttiEsecuzione.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.value.length != 0)) {
		alert("Valorizzare il Tipo Ufficio");
		return false;
	}
	// Non è possibile specificare solo il progressivo o solo l'anno del Fascicolo SIEP.
	if ((document.LoadRicercaTrasmissioneAttiEsecuzione.<%=ICostantiSiepJMS.CHIAVE_PROGR_SIEP%>.value.length != 0)
			&& (document.LoadRicercaTrasmissioneAttiEsecuzione.<%=ICostantiSiepJMS.CHIAVE_ANNO_SIEP%>.value.length == 0)) {
		alert("Valorizzare Anno SIEP");
		return false;
	}
	if ((document.LoadRicercaTrasmissioneAttiEsecuzione.<%=ICostantiSiepJMS.CHIAVE_PROGR_SIEP%>.value.length == 0)
			&& (document.LoadRicercaTrasmissioneAttiEsecuzione.<%=ICostantiSiepJMS.CHIAVE_ANNO_SIEP%>.value.length != 0)) {
		alert("Valorizzare Progressivo SIEP");
		return false;
  	}
	return true;
}

var desktop;
function ListaUffici(a_formname,a_fieldname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}
</script>
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
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
        	<font class="label">Funzione&nbsp;:</font>&nbsp;<font class="campo">Ricerca Atti Trasmessi per esecuzione Pena Sostitutiva</font>
        </td>
	</tr>
</table>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadRicercaTrasmissioneAttiEsecuzione'>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActRiscontroTrasmissioneAttiEsecuzione">
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
	  	<td class="Titolo" colspan="6">Selezione della Data di trasmissione</td>
	</tr>
	<tr>
		<td class="l" width="25%">Dalla data</td>
		<td class="l">
			<input Title="Data di trasmissione inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			&nbsp;-&nbsp;<input Title="Data di trasmissione inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			&nbsp;-&nbsp;<input Title="Data di trasmissione inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
		<td class="l">Alla data</td>
		<td class="l">
			<input Title="Data di trasmissione fine" type="text" name="<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			&nbsp;-&nbsp;<input Title="Data di trasmissione fine" type="text" name="<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			&nbsp;-&nbsp;<input Title="Data di trasmissione fine" type="text" name="<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
</table>

<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
		<td class="lVerdeNB" >
			N.B.: Se le date di Trasmissione non sono valorizzate, il sistema estrae gli atti relativi all'ultimo mese
		</td>
	</tr>
</table>
<BR>
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
		<td class="Titolo" colspan="6">Selezione dell' Ufficio destinatario</td>
  	</tr>
	<tr>
        <td class="l" width="25%">Tipo Ufficio<font class=ob></font></td>
        <td class="L">
          	<select title="tipoUfficio" class=small name="<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>" >
            	<%= tipoUfficio %>
          	</select>
        </td>
	</tr>
	<tr>
		<td class="l">Sede Ufficio <font class=ob></font></td>
        <td class="l">
          	<input Title="Sede Ufficio" name="<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>" type="text" maxlength="35" size="35">
            <a href="Javascript:ListaUffici('LoadRicercaTrasmissioneAttiEsecuzione','<%= ICostantiSiepJMS.CAMPO_SEDE_UFFICIO %>');">
				<img src="/images/filefolder.gif" border="0">
			</a>
        </td>
	</tr>
</table>
<BR>
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
	    <td class="Titolo" colspan="6"> Selezione del Fascicolo SIEP Inviato</td>
  	</tr>
	<tr>
		<td class="l" width="25%">Numero SIEP(Anno/Progressivo)</td>
        <td class="l" colspan ='2'>
          	<input Title="Anno SIEP" type="text" name="<%=ICostantiSiepJMS.CHIAVE_ANNO_SIEP %>" maxlength="4" size="4">
          	&nbsp;/&nbsp;<input Title="Numero SIEP" type="text" name="<%=ICostantiSiepJMS.CHIAVE_PROGR_SIEP %>" maxlength="14" size="14">
        </td>
	</tr>
</table>
<BR>
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
  		<td class="Titolo">Selezione del Tipo Esito</td>
	</tr>
	<tr>
  		<%-- Esito --%>
 		<td class="l">
			<table cellspacing=2 cellpadding=2>
     			<tr>
					<td class="label"><input type="radio" name="<%=ICostantiSicoJMS.CAMPO_TIPO_ESITO%>" value="0" CHECKED></td>
					<td class="label">Tutti</td>
     			</tr>
     			<tr>
					<td class="label"><input type="radio" name="<%=ICostantiSicoJMS.CAMPO_TIPO_ESITO%>" value="1"></td>
					<td class="label">In attesa di risposta</td>
    			</tr>
     			<tr>
					<td class="label"><input type="radio" name="<%=ICostantiSicoJMS.CAMPO_TIPO_ESITO%>" value="2"></td>
					<td class="label">Esito Positivo</td>
      			</tr>
    		</table>
   		</td>
	</tr>
</table>
<BR>
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
  		<td class="Titolo">Selezione dell'utente che ha effettuato la trasmissione</td>
	</tr>
	<tr>
		<td class="l">
			<table cellspacing=2 cellpadding=2>
				<tr>
					<td class="label"><input type="radio" name="<%=ICostantiSicoJMS.CAMPO_TIPO_UTENTE%>" value="0"></td>
					<td class="label">Tutti</td>
     			</tr>
     			<tr>
					<td class="label"><input type="radio" name="<%=ICostantiSicoJMS.CAMPO_TIPO_UTENTE%>" value="1" CHECKED></td>
					<td class="label">Utente Collegato</td>
     			</tr>
     			<tr>
					<td class="label">
						<input type="radio" name="<%=ICostantiSicoJMS.CAMPO_TIPO_UTENTE%>" value="2">
					</td>
					<td class="label">Utente con codice:</td>
					<td>
						<input Title="Codice Utente" type="text" name="<%= ICostantiSicoJMS.CAMPO_COD_UTENTE%>" maxlength="11" size="8">
					</td>
      			</tr>
    		</table>
  		</td>
	</tr>
</table>
<BR>
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
    	<td>
      		<input onclick="Javascript:return Verify();" class="bottone" type="submit" name="RICERCA" value="Ricerca">
  		</td>
	</tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">
</form>

<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadRicercaTrasmissioneAttiEsecuzione");

frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","minlen=2","La lunghezza minima per il giorno di inizio è di 2 caratteri");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","numeric");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","gt=1");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","lt=31");

frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","minlen=2","La lunghezza minima per il mese di inizio è di 2 caratteri");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","numeric");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","gt=1");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","lt=12");

frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","numeric");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","gt=1900");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","lt=3000");

frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","maxlen=2","La lunghezza massima per il giorno di fine è di 2 caratteri");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","minlen=2","La lunghezza minima per il giorno di fine è di 2 caratteri");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","numeric");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","gt=1");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","lt=31");

frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","maxlen=2","La lunghezza massima per il mese di fine è di 2 caratteri");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","minlen=2","La lunghezza minima per il mese di fine è di 2 caratteri");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","numeric");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","gt=1");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","lt=12");

frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","minlen=4","La lunghezza minima per l'anno di fine è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","numeric");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","gt=1900");
frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","lt=3000");
</script>
</body>
</html>
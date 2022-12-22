<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_9: creata nuova pagina di caricamento dati --%>
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="fascicoloSiusGP" 		scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="TornaQui"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="dataRestituzioneStr"	scope="request" class="java.lang.String"/>
<jsp:useBean id="descrRestituzione"		scope="request" class="java.lang.String"/>
<jsp:useBean id="dataEmissioneDD"		scope="request" class="java.lang.String"/>

<%
/* Estrazione della data udienza o data iscrizione */
String data1;
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
	data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(), "dd/MM/yyyy");
else  if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataArrivoCancelleria() != null)
	data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataArrivoCancelleria(), "dd/MM/yyyy");
else
	data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(), "dd/MM/yyyy");
String actionModifica = "siap.sius.fascicolo.action.ActLoadModificaRestituzioneAttiPresidente";
String actionCancella = "siap.sius.fascicolo.action.ActCancellaRestituzioneAttiPresidente";
Date dataRestituzione = DateUtils.getDate(dataRestituzioneStr, "dd/MM/yyyy");
%>

<html>
<head>
<title>[S.I.E.S.] - Load Gestione Restituzione Atti al Presidente</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript">
function  Verifica() {
	var ritorno = true;
	var data_minima = '<%=data1%>';
	var data_sistema = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
	var data_emissione_DD = '<%=dataEmissioneDD%>';
	var data_restituzione = document.LoadGestioneRestituzioneAttiPresidente.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_RESTITUZIONE%>.value+'/'+document.LoadGestioneRestituzioneAttiPresidente.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_RESTITUZIONE%>.value+'/'+document.LoadGestioneRestituzioneAttiPresidente.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_RESTITUZIONE%>.value;
    // Controllo della data di Restituzione
    if (ritorno && (!ControllaData(data_restituzione))) {
		alert('Data Restituzione non valida: '+ data_restituzione );
		ritorno = false;
    }
    // Controllo data di sistema >= Data Restituzione .
    else if (!CompareDate(data_restituzione, data_sistema)) {
		alert('Data Restituzione non può essere superiore alla data odierna!');
		ritorno = false;
    } else if (!CompareDate(data_emissione_DD, data_restituzione)) {
		alert("Data Restituzione non può precedere la Data di Emissione del Decreto di Designazione: " + data_emissione_DD);
		ritorno = false;
   	} else if (!CompareDate(data_minima, data_restituzione)) {
		alert("Data Restituzione non può precedere la Data Arrivo in Cancellaria: " + data_minima);
		ritorno = false;
   	}
  	return ritorno;
}
</script>
</head>
<%
String action = "siap.sius.fascicolo.action.ActModificaRestituzioneAttiPresidente";
String testo = "Dettaglio";
if (modalita.equals("inserimento"))
	testo = "Inserimento";
else if (modalita.equals("modifica"))
	testo = "Modifica";
%>
<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
		<td class="LBG"><font class="label">Funzione : <%=testo%> Restituzione Atti al Presidente</font></td>
<%
if (!(modalita.equals("inserimento") || modalita.equals("readOnly"))) {
%>
		<td class="LBG">
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=actionModifica%>&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>&TornaQui=<%=TornaQui%>">
            	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
          	</a>
          	<a href="Javascript:conferma('<%=actionCancella%>','<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>','<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>');">
            	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
          	</a>
     	</td>
<%
}
%>
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	</tr>
    <tr>
       	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
</table>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadGestioneRestituzioneAttiPresidente">
<table cellspacing="2" cellpadding="2">
<%
if (modalita.equals("dettaglio") || modalita.equals("readOnly")) {
%>
	<tr>
		<td class="l">Data Restituzione<font class="ob">(*)</font></td>
		<td class="L">
			<input readonly value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataRestituzione, "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_RESTITUZIONE%>"> /
			<input readonly value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataRestituzione, "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_RESTITUZIONE%>"> /
			<input readonly value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataRestituzione, "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_RESTITUZIONE%>">
		</td>
	</tr>
	<tr>
		<td class="l">Note</td>
	    <td class="l">
			<textarea readonly title="Note" name="<%=ICostantiFascicoloSius.CAMPO_NOTE%>" cols=80 rows=5><%=StringUtils.toStringJSP(descrRestituzione)%></textarea>
	    </td>
 	 </tr>
<%
} else {
%>
	<tr>
		<td class="l">Data Restituzione<font class="ob">(*)</font></td>
		<td class="L">
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataRestituzione, "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_RESTITUZIONE%>" onBlur="javascript:value=FillDM(value)" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"> /
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataRestituzione, "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_RESTITUZIONE%>" onBlur="javascript:value=FillDM(value)" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"> /
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataRestituzione, "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_RESTITUZIONE%>" onBlur="javascript:value=FillYear(value)" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
		</td>
	</tr>
	<tr>
		<td class="l">Note</td>
	    <td class="l">
			<textarea title="Note" name="<%=ICostantiFascicoloSius.CAMPO_NOTE%>" cols=80 rows=5><%=StringUtils.toStringJSP(descrRestituzione)%></textarea>
	    </td>
 	 </tr>
	<tr>
    	<td>
      		<input class="bottone" type="submit" value="Conferma">
    	</td>
  	</tr>
<%
}
%>
</table>
<input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=action%>">
</FORM>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadGestioneRestituzioneAttiPresidente");
// Chiama la funzione di Verify().
frmvalidator.setAddnlValidationFunction("Verifica");
</script>
</body>
</html>
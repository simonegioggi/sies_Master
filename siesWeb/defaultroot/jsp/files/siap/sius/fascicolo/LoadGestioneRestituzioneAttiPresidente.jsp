<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_9: creata nuova pagina di caricamento dati --%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>


<jsp:useBean id="fascicoloSiusGP" 	scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="TornaQui"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="data_restituzione" scope="request" class="java.util.Date"/>
<jsp:useBean id="modalita" 			scope="request" class="java.lang.String"/>

<%
boolean readonly = false;
if (modalita.equalsIgnoreCase("dettaglio"))
	readonly = true;
/* Estrazione della data udienza o data iscrizione */
String data1;
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
	data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd/MM/yyyy");
else
	data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(),"dd/MM/yyyy");
%>

<html>
<head>
<title>[S.I.E.S.] - Load Gestione Restituzione Atti al Presidente</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript">
var desktop;
function  Verifica() {
	var ritorno = true;
	var data_minima = '<%=data1%>';
	var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
	var data_restituzione = document.LoadGestioneRestituzioneAttiPresidente.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_RESTITUZIONE%>.value+'/'+document.LoadGestioneRestituzioneAttiPresidente.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_RESTITUZIONE%>.value+'/'+document.LoadGestioneRestituzioneAttiPresidente.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_RESTITUZIONE%>.value;
    // Controllo della data di Restituzione
    if (ritorno && (!ControllaData(data_restituzione))) {
		alert('Data Restituzione non valida: '+ data_restituzione );
		return false;
    }
    // Controllo data di sistema >= Data Restituzione .
    else if (!CompareDate(data_restituzione, data_sistema)) {
		alert('Data Restituzione non può essere superiore alla data odierna!');
		ritorno =  false;
    } else if (!CompareDate(data_minima, data_restituzione)) {
		alert("Data Restituzione non può precedere: " + data_minima);
		ritorno =  false;
   	}
  	return ritorno;
}
</script>
</head>
<%
String lAction = "";
String lDocumento = null;
String lTitolo = "";
String lActRet = null;
if (modalita.equalsIgnoreCase("inserimento")) {
	lAction = "siap.sius.fascicolo.action.ActInserisciGestioneRestituzioneAttiPresidente";
	lTitolo = "Inserimento Gestione Restituzione Atti al Presidente";
} else if (modalita.equalsIgnoreCase("dettaglio")) {
	lTitolo = "Dettaglio Gestione Restituzione Atti al Presidente";
} else if (modalita.equalsIgnoreCase("modifica")) {
	lTitolo = "Modifica Gestione Restituzione Atti al Presidente";
	lAction = "siap.sius.fascicolo.action.ActInserisciGestioneRestituzioneAttiPresidente";
}
%>
<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
		<td class="LBG"><font class="label">Funzione : </font>&nbsp;<font class="campo"><%=lTitolo%></font></td>
<%
if (modalita.equalsIgnoreCase("dettaglio")) {
%>
		<td class="LBG">
			<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
				<jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>" />
				<jsp:param name="ValoreIdEntita" value="<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>" />
			</jsp:include>
     	</td>
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
<%
}
%>
	</tr>
    <tr>
       	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
</table>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadGestioneRestituzioneAttiPresidente">
<table cellspacing="2" cellpadding="2">
	<tr>
		<td class="l">Data Restituzione<font class="ob">(*)</font></td>
		<td class="L">
			<input <%if (readonly) {%> readonly <%}%> value="<%=DateUtils.getDateToString(data_restituzione,"dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_RESTITUZIONE%>" onBlur="javascript:value=FillDM(value)"> /
			<input <%if (readonly) {%> readonly <%}%> value="<%=DateUtils.getDateToString(data_restituzione,"MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_RESTITUZIONE%>" onBlur="javascript:value=FillDM(value)"> /
			<input <%if (readonly) {%> readonly <%}%> value="<%=DateUtils.getDateToString(data_restituzione,"yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_RESTITUZIONE%>">
		</td>
	</tr>
	<tr>
		<td class="l">Note</td>
	    <td class="l">
			<textarea <% if (readonly) {%> readonly <%}%>Title="Note" name="<%=ICostantiFascicoloSius.CAMPO_NOTE%>" cols=80 rows=5>
				<%=StringUtils.toStringJSP(fascicoloSiusGP.getGeneraleProcedimentoModel().getAnnotazione())%>
			</textarea>
	    </td>
 	 </tr>
<%
if (!readonly) {
%>
	<tr>
    	<td>
      		<input class="bottone" type="submit" value="Conferma">
    	</td>
  	</tr>
<%
}
%>
</table>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
<input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >
</FORM>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadGestioneRestituzioneAttiPresidente");
// Chiama la funzione di Verify().
frmvalidator.setAddnlValidationFunction("Verifica");
</script>
</body>
</html>
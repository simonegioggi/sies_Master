<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_9: creata nuova pagina di inserimento dati --%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>

<jsp:useBean id="dataUdienzaStr"		scope="request" class="java.lang.String"/>
<jsp:useBean id="dopcm"					scope="request" class="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel"/>
<jsp:useBean id="TornaQui"				scope="request" class="java.lang.String"/>
<jsp:useBean id="descContenuto"			scope="request" class="java.lang.String"/>
<jsp:useBean id="codContenuto"			scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoOrdinanza"		scope="request" class="java.lang.String"/>
<jsp:useBean id="dataEsecutivitaStr"	scope="request" class="java.lang.String"/>

<%
boolean retFlag = false;
retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<html>
<head>
<title>[S.I.E.S.] - Emissione Ordinanza Conferma Decisione Magistrato Relatore</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
var desktop;

// Funzione dei controlli formali della form
function Verify() {
	var dataEmissione = document.LoadInserisciConfermaDecisioneMagistratoRelatore.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_EMISSIONE%>.value + '/' +
    					document.LoadInserisciConfermaDecisioneMagistratoRelatore.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_EMISSIONE%>.value + '/' +
    					document.LoadInserisciConfermaDecisioneMagistratoRelatore.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_EMISSIONE%>.value;
	var dataUdienza = '<%=dataUdienzaStr%>';
	var dataEsecutivita = '<%=dataEsecutivitaStr%>';

	// Controllo validità data Emissione
  	if (!ControllaData(dataEmissione)) {
	    alert('Data Emissione non valida!');
	    return false;
  	}
  	// Controllo data di emissione >= data udienza
    else if (!CompareDate(dataUdienza, dataEmissione)) {
		alert('Data Emissione non può essere inferiore alla Data Udienza del ' + dataUdienza + '!');
		return false;
    }
	// Controllo data di emissione >= data Esecutivita
//     else if (!CompareDate(dataEsecutivita, dataEmissione)) {
// 		alert('Data Emissione non può essere inferiore alla Data Esecutività del ' + dataEsecutivita + '!');
// 		return false;
//     }

   	return true;
}
</script>
</head>

<%
// Imposta l'azione da Chiamare.
// String azione = "siap.sius.depositoordinanzapc.action.ActInserisciConfermaDecisioneMagistratoRelatore";
String azione = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS";
%>

<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
      	<td class="LBG"><font class="label">Funzione:</font>&nbsp;
        	<font class="campo">Emissione Ordinanza Conferma Decisione Magistrato Relatore</font>
      	</td>
	</tr>
    <tr>
       	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    <tr>
    	<jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>">
       		<jsp:param name="MagRelRitorno" value="siap.sius.depositoordinanzapc.action.ActLoadInserisciConfermaDecisioneMagistratoRelatore"/>
    	</jsp:include>
	</tr>
</table>
<jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>">
	<jsp:param name="AvvRitorno" value="siap.sius.depositoordinanzapc.action.ActLoadInserisciConfermaDecisioneMagistratoRelatore"/>
</jsp:include>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciConfermaDecisioneMagistratoRelatore">
<table cellspacing="2" cellpadding="2" style="width: 95%;">
	<tr>
		<td class="l" width="25%">Data Emissione <font class="ob">(*)</font></td>
		<td class="L">
			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    	</td>
	</tr>
</table>
<table cellspacing="2" cellpadding="2" style="width: 95%;">
	<tr>
		<td class="l">Oggetto</td>
		<td class="l">Esito</td>
	</tr>
<%
String[] descrMotiviProvvedimento = (String[]) request.getAttribute("descrMotiviProvvedimento");
String[] descrEsitiTenore = (String[]) request.getAttribute("descrEsitiTenore");
String[] codEsitiTenore = (String[]) request.getAttribute("codEsitiTenore");
String[] codOggetti = (String[]) request.getAttribute("codOggetti");
String[] descOggetti = (String[]) request.getAttribute("descOggetti");
String[] codDettagli = (String[]) request.getAttribute("codDettagli");
String[] codMotiviProvvedimento = (String[]) request.getAttribute("codMotiviProvvedimento");
for (int i = 0; i < descrMotiviProvvedimento.length; i++) {
%>
	<tr>
		<td class="l" width="60%">
    		<input readonly="readonly" value="<%=descrMotiviProvvedimento[i]%>" type="text" size="100" name="descrOggettoProvv">
    	</td>
      	<td class="l">
      		<input readonly="readonly" value="<%=descrEsitiTenore[i]%>" type="text" size="50" name="descrEsitoProvv">
      		<input type="HIDDEN" name="<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>" value="<%=codOggetti[i]%>">
      		<input type="HIDDEN" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE%>" value="<%=descOggetti[i]%>">
      		<input type="HIDDEN" name="<%=ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=codDettagli[i]%>">
      		<input type="HIDDEN" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" value="<%=codEsitiTenore[i]%>">
      		<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="<%=codMotiviProvvedimento[i]%>">
      	</td>
	</tr>
<%
}
%>
</table>
<table cellspacing="2" cellpadding="2" style="width: 95%;">
	<tr>
      	<td class="l" width="25%">Ulteriore descrizione della decisione</td>
      	<td class="l">
			<TEXTAREA title="Ulteriore descrizione della decisione" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE%>" cols="70" rows="4"></textarea>
      	</td>
    </tr>
    <tr><td>&nbsp;</td></tr>
	<tr>
		<td class="Titolo" colspan="2">Provvedimento da Confermare</td>
 	</tr>
	<tr>
		<td class="l" colspan="2">Ordinanza N.&nbsp;
      		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.depositoordinanzapc.action.ActLoadInserisciDataDeposito&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=dopcm.getIdEventoGenerato()%><%=retParam%>">
         		<%=StringUtils.toStringJSP(dopcm.getAnnoS3())%>/<%=StringUtils.toStringJSP(dopcm.getNumS3())%>
      		</a>
      		&nbsp;del&nbsp;<font class="campo"><%=DateUtils.getDateToString(dopcm.getDataDeposito(),"dd/MM/yyyy")%></font>
    	</td>
	</tr>
	<tr>
		<td class="l" colspan="2">
			<%=dopcm.getDescrTipoOrdinanza()%>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
  		<td>
    		<input class="bottone" type="submit" value="Conferma" onclick="Javascript:return Verify();">
  		</td>
	</tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=azione%>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_CONTENUTO%>" value="<%=descContenuto%>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=codContenuto%>">
<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=codTipoOrdinanza%>">
</form>

<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadInserisciConfermaDecisioneMagistratoRelatore");

// Controllo data emissione.
frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_EMISSIONE%>","req", "Il campo Giorno Data Emissione è obbligatorio");
frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_EMISSIONE%>","req", "Il campo Mese Data Emissione é obbligatorio");
frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_EMISSIONE%>","req", "Il campo Anno Data Emissione é obbligatorio");
frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno Data Emissione deve essere di 4 caratteri");
</script>

</body>
</html>
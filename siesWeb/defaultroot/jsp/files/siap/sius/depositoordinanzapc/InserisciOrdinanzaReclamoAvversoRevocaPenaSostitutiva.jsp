<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-35: aggiunta pagina di inserimento Ordinanza Reclamo Avverso Revoca Pena Sostitutiva --%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.esecuzionesanzionesostitutiva.action.ICostantiEsecuzioneSS"%>

<jsp:useBean id="fascicoloSiusGP" 				scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="contenuto" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione" 				scope="request" class="java.util.Date"/>
<jsp:useBean id="tipoPeneSostitutive" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="procedimentoCollegato" 		scope="request" class="siap.sius.fascicolo.model.FascicoloSiusModel"/>
<jsp:useBean id="eventoProcedimentoCollegato"	scope="request" class="siap.sico.evento.model.EventoModel"/>

<%
TenoreModel[] tenori = (TenoreModel[]) request.getAttribute("tenori");
String[] esiti  = (String[]) request.getAttribute("esiti");
%>

<html>
<head>
<title>[S.I.E.S.] - Emissione Ordinanza Reclamo Avverso Revoca Pena Sostitutiva</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="/html/verifyCombo.js"></script>

<script language="JavaScript">
function AbilitaCampiEsiti() {
	var nodeReclamoAvversoRevocaPenaSostitutiva = document.getElementById('idDivReclamoAvversoRevocaPenaSostitutiva');
  	var isReclamo = "false";
  	if (typeof (document.InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) == "undefined") {
    	for (j = 0; j < document.InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++) {
      		if (document.InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].selected
      				&& (document.InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "3127")) {
      			isReclamo = "true";
      		}
    	} // fine ciclo for
	} // Fine caso singolo oggetto
//   	else { // Nel caso di più oggetti, la div d rideterminazione è visibile se almeno un esito è di Revoca
//     	// Scorro gli Oggetti
<%--     	for (j = 0; j < document.InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++) { --%>
//       		// Scorro gli esiti
<%--       		for (i = 0; i < document.InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].length ; i++) { --%>
<%--         		if (document.InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].selected --%>
<%--         				&& (document.InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].value == "3127")) { --%>
//         			isReclamo = "true";
//         		}
//       		} 
//     	}
// 	} // Fine caso più oggetti
	if (isReclamo == "true")
    	nodeReclamoAvversoRevocaPenaSostitutiva.style.display = "block";
  	else
    	nodeReclamoAvversoRevocaPenaSostitutiva.style.display = "none";
}     

function Verify() {
	var lEsiti = document.InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
	var ritorno = VerifyCombo(lEsiti, "Esito");
	var nodeReclamoAvversoRevocaPenaSostitutiva = document.getElementById('idDivReclamoAvversoRevocaPenaSostitutiva');
	if (nodeReclamoAvversoRevocaPenaSostitutiva.style.display == 'block') {
		// Pena Sostitutiva Piu' Grave
		if (document.InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.<%=ICostantiEsecuzioneSS.CAMPO_COD_TIPO_SANZIONE%>.value == '-') {
			alert('Pena Sostitutiva più grave obbligatoria!');
			document.InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.<%=ICostantiEsecuzioneSS.CAMPO_COD_TIPO_SANZIONE%>.focus();
			return false;
		}
		// Quantum Pena Da Espiare
		anniRideterminati = document.InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.<%=ICostantiEsecuzioneSS.CAMPO_NUM_ANNI_SANZIONE%>.value;
		mesiRideterminati = document.InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.<%=ICostantiEsecuzioneSS.CAMPO_NUM_MESI_SANZIONE%>.value;
		giorniRideterminati = document.InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.<%=ICostantiEsecuzioneSS.CAMPO_NUM_GIORNI_SANZIONE%>.value;
		if (anniRideterminati == '' && mesiRideterminati == '' && giorniRideterminati == '') {
			alert('Indicare i quantum di pena da espiare!');
			document.InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.<%=ICostantiEsecuzioneSS.CAMPO_NUM_ANNI_SANZIONE%>.focus();
			return false;
    	}
  	}
	return ritorno;
}
</script >
</head>
<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
		<td class=LBG>
			<font class="label">Funzione : </font>
			<font class="campo">Emissione Ordinanza Reclamo Avverso Revoca Pena Sostitutiva</font>
		</td>
	</tr>
	<tr>
		<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
	</tr>
</table>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>">
<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>">
<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione, "dd/MM/yyyy")%>>
<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP%>">
<table cellspacing="2" cellpadding="2" width="95%">
    <tr>
		<td class="l" width="30%">Data Emissione</td>
		<td class="l"><%=DateUtils.getDateToString(data_emissione, "dd/MM/yyyy")%></td>
    </tr>  
    <tr>
		<td class="l">Eventuale Motivazione</td>
		<td class="l">
		  	<TEXTAREA title="Eventuale Motivazione" cols="70" rows="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE%>"></textarea>
		</td>
    </tr>      
</table>
<br>     
<table cellspacing="2" cellpadding="2" width="95%">
    <tr>
      	<td class="Titolo" colspan="2"> Specificare esito per ciascun oggetto: </td>
    </tr>
    <tr>
		<td class="c" width="30%"> Oggetto </td>
		<td class="c"> Esito </td>
    </tr>
<%
for (int i = 0; i < tenori.length; i++) {
%>
    <tr>
		<td class="l">
		  	<input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE%>" value="<%=tenori[i].getDescrOggettoTenore()%>" readonly size="40">
			<input type="hidden" name="<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>" value="<%=tenori[i].getCodOggettoTenore()%>">
			<input type="hidden" name="<%=ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=tenori[i].getCodDettaglioOggetto()%>">
		</td>
		<td class="l">
		  	<select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onchange="Javascript:return AbilitaCampiEsiti();"><%=esiti[i]%></select>
		</td>
    </tr>
<%
}
%>
</table>
<br>
<%-- 
====================================================================================
  NUOVO Reclamo Avverso Revoca Pena Sostitutiva 
====================================================================================
3127	C063	0273	Accoglie reclamo e converte in altra pena sostitutiva
--%>
<div id="idDivReclamoAvversoRevocaPenaSostitutiva" style="display:none;"> 
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
		<td class="l" width="30%"><font class="label">Pena Sostitutiva piu' grave</font></td>  
        <td class="l">          
          	<select title="Tipo Pena Sostitutiva" name="<%=ICostantiEsecuzioneSS.CAMPO_COD_TIPO_SANZIONE%>">
          		<option value="-">-</option>
          		<%=tipoPeneSostitutive%>
          	</select>
		</td>
	</tr>
	<tr>
		<td class="l"><font class="label">Rideterminazione Quantum Pena Da Espiare</font></td>
		<td class="l">
			<font class="label">Anni</font>&nbsp;
			<input type="text" title="Anni" size="4" maxlength="2" name="<%=ICostantiEsecuzioneSS.CAMPO_NUM_ANNI_SANZIONE%>" value="" ONKEYPRESS="return TicTabNumField(this,event)">
			<font class="label">Mesi</font>&nbsp;
			<input type="text" title="Mesi" size="4" maxlength="2" name="<%=ICostantiEsecuzioneSS.CAMPO_NUM_MESI_SANZIONE%>" value="" ONKEYPRESS="return TicTabNumField(this,event)">
			<font class="label">Giorni</font>&nbsp;
			<input type="text" title="Giorni" size="4" maxlength="2" name="<%=ICostantiEsecuzioneSS.CAMPO_NUM_GIORNI_SANZIONE%>" value="" ONKEYPRESS="return TicTabNumField(this,event)">
		</td>
	</tr>
</table>
</div>
<%
if (!Utils.isNullObj(procedimentoCollegato) && !Utils.isNullObj(procedimentoCollegato.getChiaveAnno())) {
%>
<br>     
<table cellspacing="2" cellpadding="2" width="95%">
    <tr>
      	<td class="Titolo" colspan="2"> Provvedimento di Revoca Pena Sostitutiva </td> <%-- U131 / U132 --%>
    </tr>
	<tr>
		<td class="l" width="30%"><font class="label">Procedimento Collegato N°</font></td>  
        <td class="l">          
          	<font class="label"><%=StringUtils.toStringJSP(procedimentoCollegato.getChiaveAnno())%></font>
          	&nbsp;/&nbsp;
          	<font class="label"><%=StringUtils.toStringJSP(procedimentoCollegato.getChiaveProgr())%></font>
		</td>
	</tr>
	<tr>
		<td class="l"><font class="label">Provvedimento Emesso in data</font></td>  
        <td class="l">          
          	<font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoProcedimentoCollegato.getDataEmissione(), "dd/MM/yyyy"), "-")%></font>
		</td>
	</tr>
</table>
<%
}
%>
<br>
<table cellspacing="2" cellpadding="2" width="90%">
    <tr>
      	<td>
        	<input class="bottone" type="submit" value="Conferma">
      	</td>
    </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("InserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva");
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
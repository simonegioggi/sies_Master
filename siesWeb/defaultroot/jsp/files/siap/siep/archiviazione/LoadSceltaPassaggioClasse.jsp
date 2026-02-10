<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2025-48: consento la scelta della classe tranne quella da cui provengo --%>
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>

<jsp:useBean id="fascicolo"				scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="dataArrivoAtto"		scope="request" class="java.util.Date"/>
<jsp:useBean id="dataIscrizione"		scope="request" class="java.util.Date"/>
<jsp:useBean id="progressivo"			scope="request" class="java.lang.String"/>
<jsp:useBean id="dataEmissione"			scope="request" class="java.util.Date"/>
<jsp:useBean id="dataTrasmissione"		scope="request" class="java.util.Date"/>
<jsp:useBean id="dataDefinizione"		scope="request" class="java.util.Date"/>
<jsp:useBean id="oggettoDefinizione"	scope="request" class="java.lang.String"/>
<jsp:useBean id="note"					scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratoFirmatario"	scope="request" class="java.lang.String"/>
<jsp:useBean id="casellarioGiudiziale"	scope="request" class="java.lang.String"/>
<jsp:useBean id="UtenteConnesso"        scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="penaresidua" 			scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<html>
<head>
<title>[S.I.E.S.] - Definizione Procedimento - Scelta Passaggio di Classe</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<style>
	#dhtmltooltip {
		position: absolute;
		width: 150px;
		border: 2px solid black;
		padding: 2px;
		background-color: lightyellow;
		visibility: hidden;
		z-index: 100;
		/*Remove below line to remove shadow. Below line should always appear last within this CSS*/
		filter: progid:DXImageTransform.Microsoft.Shadow(color=gray,direction=135);
	}
</style>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
function Verify() {
	// Data arrivo atto
	var d2 = document.LoadSceltaPassaggioClasse.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_ATTO%>.value
		+ '/' + document.LoadSceltaPassaggioClasse.<%=ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_ATTO%>.value
		+ '/' + document.LoadSceltaPassaggioClasse.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ARRIVO_ATTO%>.value;
	if (!ControllaData(d2)) {
		alert('Data Arrivo Atto non valida '+ d2);
		return false;
	}
	// Data Irrevocabilita'
	var d3 = document.LoadSceltaPassaggioClasse.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value
		+ '/' + document.LoadSceltaPassaggioClasse.<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value
		+ '/' + document.LoadSceltaPassaggioClasse.<%=ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
	// Data Iscrizione provvedimento
	var d4 = document.LoadSceltaPassaggioClasse.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value
		+ '/' + document.LoadSceltaPassaggioClasse.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>.value
		+ '/' + document.LoadSceltaPassaggioClasse.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>.value;
	if (!ControllaData(d3) && d3.length > 2) {
		alert('Data Irrevocabilità non valida');
		return false;
	}
	if (!CompareDate(d2, d4)) {
		alert('la Data iscrizione Procedimento deve essere successiva alla Data Arrivo Atto');
		return false;
	}
	if (!CompareDate(d3, d2)) {
		alert('La Data Irrevocabilità deve essere precedente alla Data Arrivo Atto');
		return false;
	}
	var d5 = d2; 
	if (!CompareDate(d5, d2)) {
		alert('Data Arrivo Atto deve essere successiva alla Data sentenza');
		return false;
	}
	if (document.LoadSceltaPassaggioClasse.tipoV[0].checked == false
			&& document.LoadSceltaPassaggioClasse.tipoV[1].checked == false
			&& document.LoadSceltaPassaggioClasse.tipoV[2].checked == false
			&& document.LoadSceltaPassaggioClasse.tipoV[3].checked == false
			&& document.LoadSceltaPassaggioClasse.tipoV[4].checked == false
			&& document.LoadSceltaPassaggioClasse.tipoV[5].checked == false
			&& document.LoadSceltaPassaggioClasse.tipoV[6].checked == false) {
		alert('Attenzione! Selezionare la Classe di Iscrizione del nuovo Procedimento.');
		return false;
	}
	return conferma();
}

function conferma() {
	return confirm("Si sta per procedere all'Archiviazione del fascicolo corrente ed all'iscrizione di un nuovo procedimento" +
		" nella classe selezionata! Vuoi proseguire?");
}

function checkTipoClasse() {
	var tc = <%=progressivo%>;
	document.LoadSceltaPassaggioClasse.tipoV[tc].disabled = true;
}
</script>
</head>
<body class="corpo" onload="checkTipoClasse()">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
		<td class=LBG><font class="label">Funzione :</font>&nbsp;
			<font class="campo">Definizione Procedimento - Scelta Passaggio di Classe</font>
      	</td>
	</tr>
</table>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
      	<td class="L">
        	<font class="label">Procedimento : N.</font>
<%
if ("90".equals(UtenteConnesso.getUserProfile().getProfileId().toString())) {
%>
			<%=fascicolo.getChiaveAnno()%> / <%=fascicolo.getChiaveProgr()%>
<%
} else {
%>
          	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>" title="Procedimento">
	            <%=fascicolo.getChiaveAnno()%>
	            /
	            <%=fascicolo.getChiaveProgr()%>
          	</a>          
<%
}
%>
          	&nbsp;
<%
if (fascicolo.getFlagCumulante() != null && fascicolo.getFlagCumulante().equals("S")) {
%>
			<font class="cRossoCumulo"> &nbsp;C&nbsp; </font> &nbsp;
<%
}
if (fascicolo.getCodOperatoreInserimento() != null && fascicolo.getCodOperatoreInserimento().startsWith("res-")) {
%>
            <font class="cRossoCumulo"> &nbsp;Migrato&nbsp; </font> &nbsp;
<%
}
if (fascicolo.getCodStatoFascicolo() != null && (fascicolo.getCodStatoFascicolo().equals("01"))) {
%>
            <font class="cRossoCumulo"> &nbsp;Archiviato&nbsp;</font> &nbsp;
<%
}
if ((penaresidua != null && penaresidua.getFlagPenaSospesa() != null && penaresidua.getFlagPenaSospesa().equals("S"))
		|| (fascicolo != null && fascicolo.getChiaveProgr() != null && (fascicolo.getChiaveProgr().intValue() >= 30000 && fascicolo.getChiaveProgr().intValue() < 40000))) {
	if (fascicolo!= null && fascicolo.getChiaveProgr() != null && (fascicolo.getChiaveProgr().intValue() >= 30000 && fascicolo.getChiaveProgr().intValue() < 40000)) {
%>
            <font class="cRossoCumulo"> &nbsp;Pena Sospesa Condizionalmente&nbsp;</font>&nbsp;
<%
	} else {
%>
			<font class="cRossoCumulo"> &nbsp;Esecuzione Sospesa&nbsp;</font>&nbsp;
<%
	}
}
if (penaresidua != null && penaresidua.getFlagPenaSospesa() != null && penaresidua.getFlagPenaSospesa().equals("I")) {
%>
          	<font class="cRossoCumulo"> &nbsp;Pena Interrotta&nbsp;</font>&nbsp;
<%
}
if (penaresidua != null && penaresidua.getFlagPenaSospesa() != null && penaresidua.getFlagPenaSospesa().equals("D")) {
%>
          	<font class="cRossoCumulo"> &nbsp;Pena Differita&nbsp;</font>&nbsp;
<%
}
if (!(UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio()))) {
%>
			&nbsp;<font class="label"><%=fascicolo.getDescrTipoUfficio() + " DI " + fascicolo.getDescrComuneUfficio() %></font>
<%
}
%>
		</td>
	</tr>
</table>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenzaAttribuzione.jsp"/>
<br>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.archiviazione.action.ActInserisciPassaggioClasse" name="LoadSceltaPassaggioClasse">

<input type="hidden" value="<%=DateUtils.getDateToString(dataEmissione, "dd")%>" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>">
<input type="hidden" value="<%=DateUtils.getDateToString(dataEmissione, "MM")%>" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>">
<input type="hidden" value="<%=DateUtils.getDateToString(dataEmissione, "yyyy")%>" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>">

<input type="hidden" value="<%=DateUtils.getDateToString(dataTrasmissione, "dd")%>" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>">
<input type="hidden" value="<%=DateUtils.getDateToString(dataTrasmissione, "MM")%>" name="<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>">
<input type="hidden" value="<%=DateUtils.getDateToString(dataTrasmissione, "yyyy")%>" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>">

<input type="hidden" value="<%=DateUtils.getDateToString(dataDefinizione, "dd")%>" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>">
<input type="hidden" value="<%=DateUtils.getDateToString(dataDefinizione, "MM")%>" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>">
<input type="hidden" value="<%=DateUtils.getDateToString(dataDefinizione, "yyyy")%>" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>">

<input type="HIDDEN" name="<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>" value="<%=StringUtils.toStringJSP(oggettoDefinizione)%>">
<input type="HIDDEN" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" value="<%=StringUtils.toStringJSP(note)%>">
<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" value="<%=StringUtils.toStringJSP(magistratoFirmatario)%>">
<input type="HIDDEN" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS%>" value="<%=StringUtils.toStringJSP(casellarioGiudiziale)%>">

<table cellspacing="2" cellpadding="2">
	<tr>
	    <td class="l">Data Iscrizione Procedimento</td>
	    <td class="c">
	      	<%=DateUtils.getSysDate("dd")%>/<%=DateUtils.getSysDate("MM")%>/<%=DateUtils.getSysDate("yyyy")%>
	      	<input type="hidden" value="<%=DateUtils.getDateToString(dataIscrizione, "dd")%>" name="<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>">
			<input type="hidden" value="<%=DateUtils.getDateToString(dataIscrizione, "MM")%>" name="<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>">
			<input type="hidden" value="<%=DateUtils.getDateToString(dataIscrizione, "yyyy")%>" name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>">
	    </td>
	</tr>  
	<tr>
	    <td class="l">Data Arrivo Atto</td>
	    <td class="l">
	        <input title="Data Arrivo Atto" value="<%=DateUtils.getDateToString(dataArrivoAtto, "dd")%>" name="<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_ATTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			/
			<input title="Data Arrivo Atto" value="<%=DateUtils.getDateToString(dataArrivoAtto, "MM")%>" name="<%=ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_ATTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			/
			<input title="Data Arrivo Atto" value="<%=DateUtils.getDateToString(dataArrivoAtto, "yyyy")%>" name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_ARRIVO_ATTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>    
	<tr>
    	<td class="l">Data Irrevocabilità</td>
		<td class="L">               
			<input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "dd")%>" name="<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			/
			<input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "MM")%>" name="<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			/
			<input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "yyyy")%>" name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
     	</td>
 	</tr>      
</table>
<div id="dhtmltooltip"></div>
<script type="text/javascript">
/***********************************************
* Cool DHTML tooltip script- © Dynamic Drive DHTML code library (www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
***********************************************/
var offsetxpoint=-60 //Customize x offset of tooltip
var offsetypoint=-120 //Customize y offset of tooltip
var ie=document.all
var enabletip=false
var tipobj=document.all? document.all["dhtmltooltip"] : document.getElementById? document.getElementById("dhtmltooltip") : ""

function ietruebody() {
	return (document.compatMode && document.compatMode!="BackCompat")? document.documentElement : document.body
}

function ddrivetip(thetext, thecolor, thewidth) {
	if (ie) {
		if (typeof thewidth!="undefined")
			tipobj.style.width=thewidth+"px"
		if (typeof thecolor!="undefined" && thecolor!="")
			tipobj.style.backgroundColor=thecolor
		tipobj.innerHTML=thetext
		enabletip=true
		return false
	}
}

function positiontip(e) {
	if (enabletip) {
  		var curX= event.clientX+ietruebody().scrollLeft;
  		var curY=event.clientY+ietruebody().scrollTop;
		// Find out how close the mouse is to the corner of the window
		var rightedge=ie&&!window.opera? ietruebody().clientWidth-event.clientX-offsetxpoint : window.innerWidth-e.clientX-offsetxpoint-20
		var bottomedge=ie&&!window.opera? ietruebody().clientHeight-event.clientY-offsetypoint : window.innerHeight-e.clientY-offsetypoint-20
		var leftedge=(offsetxpoint<0)? offsetxpoint*(-1) : -1000
		// if the horizontal distance isn't enough to accomodate the width of the context menu
		if (rightedge<tipobj.offsetWidth)
			// move the horizontal position of the menu to the left by it's width
			tipobj.style.left=ie? ietruebody().scrollLeft+event.clientX-tipobj.offsetWidth+"px" : window.pageXOffset+e.clientX-tipobj.offsetWidth+"px"
		else if (curX<leftedge)
			tipobj.style.left="5px"
		else
			//position the horizontal position of the menu where the mouse is positioned
			tipobj.style.left=curX+offsetxpoint+"px"

		// same concept with the vertical position
		if (bottomedge<tipobj.offsetHeight)
			tipobj.style.top=ie? ietruebody().scrollTop+event.clientY-tipobj.offsetHeight-offsetypoint+"px" : window.pageYOffset+e.clientY-tipobj.offsetHeight-offsetypoint+"px"
		else
			tipobj.style.top=curY+offsetypoint+"px"
		tipobj.style.visibility="visible"
	}
}

function hideddrivetip() {
	if (ie) {
		enabletip=false
		tipobj.style.visibility="hidden"
		tipobj.style.left="-1000px"
		tipobj.style.backgroundColor=''
		tipobj.style.width=''
	}
}
document.onmousemove=positiontip
</script> 
<table cellspacing="2" cellpadding="2">
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="l" ONMOUSEOVER="ddrivetip('Pena Detentiva','yellow')" ONMOUSEOUT="hideddrivetip()">Classe I</td>
		<td class="l"><input type="radio" name="tipoV" value="1"></td>
		<td>&nbsp;</td><td>&nbsp;</td>
		<td class="l" ONMOUSEOVER="ddrivetip('Pena Pecuniaria','yellow')" ONMOUSEOUT="hideddrivetip()">Classe II</td>
		<td class="l"><input type="radio" name="tipoV" value="2"></td>
	</tr>
	<tr>
		<td class="l" ONMOUSEOVER="ddrivetip('Pena Sospesa','yellow')" ONMOUSEOUT="hideddrivetip()">Classe III</td>
		<td class="l"><input type="radio" name="tipoV" value="3"></td>
		<td>&nbsp;</td><td>&nbsp;</td>
		<td class="l" ONMOUSEOVER="ddrivetip('Misura Sicurezza','yellow')" ONMOUSEOUT="hideddrivetip()">Classe IV</td>
		<td class="l"><input type="radio" name="tipoV" value="4"></td>
	</tr>
	<tr>
		<td class="l" ONMOUSEOVER="ddrivetip('Persona Giuridica','yellow')" ONMOUSEOUT="hideddrivetip()">Classe V</td>
		<td class="l"><input type="radio" name="tipoV" value="5"></td>
		<td>&nbsp;</td><td>&nbsp;</td>
		<td class="l" ONMOUSEOVER="ddrivetip('Giudice di Pace','yellow')" ONMOUSEOUT="hideddrivetip()">Classe VI</td>
		<td class="l"><input type="radio" name="tipoV" value="6"></td>
     </tr>
     <tr>
		<td class="l" ONMOUSEOVER="ddrivetip('Conversione Pena Pecuniaria','yellow')" ONMOUSEOUT="hideddrivetip()">Classe VII</td>
		<td class="l"><input type="radio" name="tipoV" value="7"></td>
	</tr>
</table>
<br>
<table>
	<tr>
      	<td class="l">Note Procedimento</td>
      	<td class="l">
        	<textarea title="Note Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_NOTE%>" cols="40" rows="5"></textarea>
    	</td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      	<td><input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma"></td>
    </tr>
</table>
</form>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione" %>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna" %>

<jsp:useBean id="comune"              scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"            scope="request" class="java.lang.String"/>
<jsp:useBean id="foro"                scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocato"            scope="request" class="siap.siep.avvocato.model.AvvocatoSiepModel"/>
<jsp:useBean id="tipoAvvocato"        scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsterna"     scope="request" class="java.lang.String"/>
<jsp:useBean id="motivoDesignazione"  scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaDif"  scope="request" class="java.lang.String"/>
<jsp:useBean id="nazione"			  scope="request" class="java.lang.String"/>		<!--   20210627 MEV_21 -->
<jsp:useBean id="statoAvv"			  scope="request" class="java.lang.String"/>		<!--   20210627 MEV_21 -->

<html>
<head>
<title>[S.I.E.S.] - Gestione Avvocato </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>jsrsClient.js"></script>
<script language="JavaScript">
var desktop;

function Inserisci() {
	document.LoadModificaAvvocato.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.avvocato.action.ActLoadInserisciDifensore";
	document.LoadModificaAvvocato.submit();
}

function ListaComuni(a_formname,a_fieldname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}
    
function ListaAvvocati(a_formname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname, "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=550,height=500");
}

function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

function Verify() {
	var tipo=document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COD_TIPO%>[document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COD_TIPO%>.selectedIndex].value;
	// MEV 29 - 07/2015 Aggiunto controllo su presenza Foro
	if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_FORO%>.value=="") {
		alert('Selezionare il Foro');
		return false;
	}
	if (tipo =="-") {
		alert("Campo Tipo Avvocato è obbligatorio");
		return false;
	}
	if (document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value=="") {
		document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value=document.LoadModificaAvvocato.idAvvVecchio.value;
	}
	if (tipo =="01" && document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COD_MOTIVO_DESIGNAZIONE%>[document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COD_MOTIVO_DESIGNAZIONE%>.selectedIndex].value=='-') {
		alert("Indicare il motivo della designazione");
		return false;
	}
	document.LoadModificaAvvocato.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.avvocato.action.ActSostituzioneDifensore";  
}
 
function cambiaMotivo() {
	var note = document.getElementById('note');
  	var idxSelMotivo = document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_MOTIVO_DESIGNAZIONE%>.selectedIndex;
	var valoreMotivo=document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_MOTIVO_DESIGNAZIONE%>[idxSelMotivo].value;
  	if (valoreMotivo=='0008'){
	    note.style.visibility='visible';
	    ufficioSotto.style.top='-90px';
	    conferma.style.top='-90px';
  	} else {
	    note.style.visibility='hidden';
	    ufficioSotto.style.top='-135px';
	    conferma.style.top='-130px';
  	}
}

function caricamento() {
	var idxSel       = document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_TIPO%>.selectedIndex;
	var valore       = document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_TIPO%>[idxSel].value;
	var fiducia      = document.getElementById('fiducia');
	var ufficio      = document.getElementById('ufficio');
	var ufficioSotto = document.getElementById('ufficioSotto');
	var conferma     = document.getElementById('conferma');
	var motivoDes    = document.getElementById('motivoDes');
	note.style.visibility='hidden';
	ufficioSotto.style.top='-135px';

	if ( valore == '01') {
    <%-- MEV_21 --%>
     caricaDescComuneForo(document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_FORO%>.value);
<%--   	 document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF%>.value=document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_FORO%>.value; --%>
  	 <%-- MEV_21 --%>
    }

	conferma.style.visibility='visible';
	fiducia.style.visibility='hidden';

  	if (valore == '01') {
	    fiducia.style.visibility='hidden';
	    ufficio.style.visibility='visible';
	    ufficioSotto.style.visibility='visible';
	    conferma.style.visibility='visible';
	    motivoDes.style.visibility='visible';
    	conferma.style.top='-130px';
  	}
	if (valore == '02') {
        fiducia.style.visibility='visible';
        ufficio.style.visibility='hidden';
        ufficioSotto.style.visibility='hidden';
        motivoDes.style.visibility='hidden';
        conferma.style.visibility='visible';
        conferma.style.top='-400px';
	}
	if (valore == '-') {
        fiducia.style.visibility='hidden';
        ufficio.style.visibility='hidden';
        ufficioSotto.style.visibility='hidden';
        motivoDes.style.visibility='hidden';
        conferma.style.visibility='visible';
        conferma.style.top='-400px';
	}
	if (valore == '03') {
		fiducia.style.visibility='hidden';
        ufficio.style.visibility='hidden';
        ufficioSotto.style.visibility='hidden';
        motivoDes.style.visibility='hidden';
        conferma.style.visibility='visible';
        conferma.style.top='-400px';
	}
}

<%-- MEV_21: aggiunta chiamata a WS per individuare lista avvocato in RegInde --%>
function ListaAvvocatiRegInde(a_formname) {
	var inserimento = document.getElementById('inserimento');
	inserimento.style.visibility = 'hidden';
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocatoRegInde&formname="+a_formname,"Ricerca_Avvocato_RegInde","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=550,height=600");
}

function caricaDescComuneForo (foro) {
  var myParams = new Array(foro);

  jsrsExecute("/CaricaHTML_Servlet", loadDescComuneForo, "getDescComuneForo",myParams);      
}

function loadDescComuneForo(descComuneForo) {
  document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF%>.value = descComuneForo;
}

 <%-- MEV_21 --%>

</script>
</head>

<body class="corpo" onLoad="caricamento();">
<table>
  	<tr>
    	<td class="LBG">
    		<a href="Javascript:window.print();">
    			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
    		</a>
    	</td>
    	<td class=LBG>
    		<font class="label">Funzione : </font>&nbsp;&nbsp;
			<font class="campo">Sostituzione Difensore</font>
    	</td>
  	</tr>
</table>

<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadModificaAvvocato">

<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">

<table>
	<tr>
      	<td class="l">
      		<%-- MEV_21: aggiunta chiamata a WS per individuare lista avvocato in RegInde --%>
<!--         	<a href="Javascript:ListaAvvocati('LoadModificaAvvocato');"> -->
<!--           		Seleziona dalla lista <img src="/images/filefolder.gif" border=0> -->
<!--        		</a> -->
       		<a href="Javascript:ListaAvvocatiRegInde('LoadModificaAvvocato');">
         		Seleziona da RegInde <img src="/images/filefolder.gif" border=0>
       		</a>
      	</td>
	</tr>
</table>

<input type="hidden" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" value="">
<input type="hidden" name="idAvvVecchio" value="<%=avvocato.getAvvocato().getIdAvvocato()%>">

<table>
	<tr>
      	<td class="l" >Cognome </td>
      	<td class="l"  ><input  size=35 maxlength=35 readonly title="Campo Cognome" type="text"  value="<%=avvocato.getAvvocato().getCognome()%>" name="<%= ICostantiAvvocato.CAMPO_COGNOME %>" ></td>
    </tr>
    <tr>
      	<td class="l">Nome </td>
      	<td class="l"><input size=35 maxlength=35 readonly title="Campo Nome" type="text"  value="<%=avvocato.getAvvocato().getNome()%>" name="<%= ICostantiAvvocato.CAMPO_NOME %>"  ></td>
    </tr>
	<%-- MEV_21: aggiunti campi per chiamata a WS per individuare lista avvocato in RegInde --%>
<%
	String comuneNascita = "", comuneNascitaEstero = "";
	if (Utils.isPresent(avvocato.getAvvocato().getDescrStatoNascita())) {
		if ("ITALIA".equalsIgnoreCase(avvocato.getAvvocato().getDescrStatoNascita()))
		comuneNascita = avvocato.getAvvocato().getDescLuogoNascita();
	else
		comuneNascitaEstero = avvocato.getAvvocato().getDescLuogoNascitaReginde();
} else if (Utils.isPresent(avvocato.getAvvocato().getDescLuogoNascita())) {
	comuneNascita = avvocato.getAvvocato().getDescLuogoNascita();
}
%>
    <tr>
      	<td class="l">Comune di Nascita </td>
      	<td class="L">
        	<input title="Comune di Nascita"  readonly value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getDescLuogoNascita())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>"  maxlength="35" size="35">
      	</td>
    </tr>
    
  	<tr>
        <td class="l">Stato di Nascita</td>
		<td class="L">
          	<select name="<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>" size="1"><%=nazione%></select>
        </td>
	</tr>
  	<tr>
        <td class="l">Luogo di Nascita Estero</td>
        <td class="l">
			<input title="Luogo di Nascita Estero" readonly value="<%=StringUtils.toStringJSP(comuneNascitaEstero)%>" type="text" name="<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>" maxlength="35" size="35">
		</td>
	</tr>
    
    <tr>
      	<td class="l">Data di nascita </td>
      	<td class="L">
<%
if (avvocato.getAvvocato().getDataNascita() != null) {
%>
			<input type="text" readonly title="Giorno Data di nascita" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(avvocato.getAvvocato().getDataNascita()))%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" readonly title="Mese Data di nascita" name="<%= ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA %>" value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(avvocato.getAvvocato().getDataNascita()))%>"maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" readonly  title="Anno Data di nascita" name="<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA %>" value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(avvocato.getAvvocato().getDataNascita()))%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
} else {
%>
            <input type="text" readonly title="Giorno Data di nascita" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>" value="" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" readonly title="Mese Data di nascita" name="<%= ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA %>" value="" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" readonly  title="Anno Data di nascita" name="<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA %>" value="" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
}
%>
		&nbsp;</td>
	</tr>
   	<tr>
      	<td class="l">Foro : </td>
      	<td class="l">
        	<select name="<%=ICostantiAvvocato.CAMPO_FORO%>" size="1">
           		<%=foro%>
        	</select>
		</td> 
	</tr>
    <tr>
		<td class="l">Indirizzo</td>
        <td class="l"><input size=80 maxlength=200  title="Indirizzo" value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getIndirizzo())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_INDIRIZZO %>"  ></td>
    </tr>
    <tr>
        <td class="l">Con Studio in </td>
        <td class="L">
<%--          	<input title="Comune di Residenza"   value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getDescComuneResidenza())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA %>"  maxlength="35" size="35"> --%>
			<input title="Comune Sede dello Studio" value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getDescrComuneStudio())%>" type="text" name="<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO%>" maxlength="35" size="35">
        </td>
    </tr>
    <tr>
		<td class="l">Telefono</td>
		<td class="l"><input size=12 maxlength=12   title="Telefono" value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getTelefono())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_TELEFONO %>"  ></td>
    </tr>
    <tr>
		<td class="l">Fax</td>
		<td class="l"><input size=12 maxlength=12  title="Fax" value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getFax())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_FAX %>"  ></td>
    </tr>
    <tr>
		<td class="l">e-mail</td>
		<td class="l"><input size=50 maxlength=50  value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getEMail()) %>" title="e-mail" type="text" name="<%= ICostantiAvvocato.CAMPO_E_MAIL %>"  ></td>
    </tr>
   	<%-- MEV_21: aggiunto campo per chiamata a WS per individuare lista avvocato in RegInde --%>
    <tr>
		<td class="l">pec</td>
		<td class="l"><input size=50 maxlength=50 value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getPec()) %>" title="pec" type="text" name="<%=ICostantiAvvocato.CAMPO_PEC%>"></td>
	</tr>
	<tr>
		<td class="l">Codice Fiscale</td>
		<td class="l"><input size=20 maxlength=16 readonly value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getCodiceFiscale()) %>" title="Codice Fiscale" type="text" name="<%= ICostantiAvvocato.CAMPO_CODICE_FISCALE %>"  ></td>
    </tr>
	<tr>
		<td class="l">Stato Difensore</td>
		<td class="L">
           	<select title="Stato Difensore" name="<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>"><%=statoAvv%></select>
		</td>
	</tr>
    <tr>
		<td class="l">Tipo Difensore <font class=ob>(*)</font></td>
		<td class="L">
			<select title="attività" name="<%=ICostantiAvvocato.CAMPO_COD_TIPO%>"  onChange="caricamento();" >
            	<%=tipoAvvocato%>
          	</select>
         </td>
	</tr>
    <tr><td>&nbsp;</td></tr>
</table>

<%
/****************************************************************************************
Div da Visualizzare nel caso dei avvocato di fiducia contenente la data della nomina
****************************************************************************************/
%>
<div id="fiducia"  style="visibility:hidden; position:relative;  top:-50px; left:330px;">
<table cellspacing=2 cellpadding=2>
	<tr>
  		<td class="label">Nomina in Data</td>
  		<td>
    		<input type="text"  title="Giorno Data di Nomina" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NOMINA%>" value="<%=DateUtils.getDayToString(avvocato.getAvvocatoFascicoloSiepModel().getDataInizioValidita())%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
			-
			<input type="text" title="Mese Data di Nomina" name="<%= ICostantiAvvocato.CAMPO_MESE_DATA_NOMINA %>" value="<%=DateUtils.getMonthToString(avvocato.getAvvocatoFascicoloSiepModel().getDataInizioValidita())%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
			-
			<input type="text"  title="Anno Data di Nomina" name="<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NOMINA %>" value="<%=DateUtils.getYearToString(avvocato.getAvvocatoFascicoloSiepModel().getDataInizioValidita())%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
</table>
</div>

<%
/****************************************************************************************
Div da Visualizzare nel caso dei avvocato d'ufficio contenente la data della nomina
****************************************************************************************/
%>
<div id="ufficio"  style="visibility:hidden; position:relative;  top:-80px; left:320px  ">
<table cellspacing=2 cellpadding=2>
  	<tr>
    	<td class="label">Designato in Data</td>
    	<td>
      		<input type="text" title="Giorno Data di Designazione" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_DESIGNAZIONE%>" value="<%=DateUtils.getDayToString(avvocato.getAvvocatoFascicoloSiepModel().getDataInizioValidita())%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
  			-
  			<input type="text" title="Mese Data di Designazione" name="<%= ICostantiAvvocato.CAMPO_MESE_DATA_DESIGNAZIONE %>" value="<%=DateUtils.getMonthToString(avvocato.getAvvocatoFascicoloSiepModel().getDataInizioValidita())%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
  			-
  			<input type="text" title="Anno Data di Designazione" name="<%= ICostantiAvvocato.CAMPO_ANNO_DATA_DESIGNAZIONE %>" value="<%=DateUtils.getYearToString(avvocato.getAvvocatoFascicoloSiepModel().getDataInizioValidita())%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
</table>
</div>

<div id="motivoDes"  style="visibility:hidden; position:relative;  top:-77px ">
<table>
	<tr>
		<td class="l" >Motivo della Designazione</td>
		<td class="L" width="70%">
			<select title="designazione" name="<%=ICostantiAvvocato.CAMPO_COD_MOTIVO_DESIGNAZIONE%>" onChange="cambiaMotivo();">
				<%=motivoDesignazione%>
			</select>
		</td>
	</tr>
</table>
</div>

<div id="note" style="visibility:hidden; position:relative; top:-80px;">
<table width="68%">
	<tr>
		<td class="l"  width="30%" >Note</td>
		<td class="L">
		   <TEXTAREA title="Note" name="<%=ICostantiAvvocato.CAMPO_NOTE%>" cols=40 ></textarea>
		</td>
	</tr>
</table>
</div>
<div id="ufficioSotto"  style="visibility:hidden; position:relative;  top:-90px ">

<table>
	<tr><td>&nbsp;</td></tr>
  	<tr><td class="Titolo" colspan='8'>Autorità per la notifica al Condannato</td></tr>
	<tr>
		<!--autorità di polizia-->
		<td class="l" width=30%>Autorità</td>
		<td class="L" colspan="3">
			<select  Title="Autorita Esterna"  class="small"  name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
				<%=autoritaEsterna%>
            </select>
		</td>
  	</tr>
	<tr>
		<td class="l">Sede</td>
      	<td class="L">
        	<input title="Sede Autorita Esterna" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
       		<a href="Javascript:ListaComuni('LoadModificaAvvocato','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
          		<img src="/images/filefolder.gif" border=0>
        	</a>
		</td>
	</tr>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="L">
			<TEXTAREA title="Note" cols=40></textarea>
		 </td>
    </tr>
 	<tr>
		<td class="l" width=30%>Istituto di Detenzione </td>
     	<td class="l">
        	<input readonly Title="Istituto" name="Comune" value="" size=50>
        	<input type="hidden"  Title="Istituto"  name ="<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>"size=50>
			<a href="Javascript:ListaIstitutoDetenzione('LoadModificaAvvocato','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
				<img src="/images/filefolder.gif" border=0>
			</a>
     	</td>
 	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr><td class="Titolo" colspan='8'>Autorità per la notifica al Difensore</td></tr>
	<tr>
		<!--autorità di polizia-->
		<td class="l" width=30%>Autorità </td>
		<td class="L" colspan="3">
		  	<select  Title="Autorita Esterna"  class="small"  name="<%=ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF%>">
				<%=autoritaEsternaDif%>
		 	</select>
		</td>
  	</tr>
	<tr>
	 	<td class="l">Sede</td>
	 	<td class="L">
	   		<input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF%>"  maxlength="35" size="35">
			<a href="Javascript:ListaComuni('LoadModificaAvvocato','<%=ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF%>');">
	      		<img src="/images/filefolder.gif" border=0>
	    	</a>
		</td>
	</tr>
</table>
</div>

<div id="conferma"  style="visibility:hidden; position:relative;  top:-110px ">
<table cellspacing=2 cellpadding=2>
	<tr>
		<td colspan=2>
        	<input class=bottone  type="submit" value="Conferma" onclick="return Verify();">
        </td>
		<!-- 20210627 MEV_21 -->
   		<td colspan=2 id="inserimento" style="visibility:hidden;">
        	<input class="bottone"  type="button" value="Inserimento" name="IN" onClick="Javascript:Inserisci();">
        </td>
	</tr>
</table>
</div>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadModificaAvvocato");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_COGNOME %>","req","Il campo Cognome Avvocato è obbligatorio");

frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_NOME %>","req","Il campo Nome Avvocato è obbligatorio");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_NOME %>","alpha");

frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_FORO %>","req","Il campo Foro è obbligatorio");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_FORO %>","alpha");
</script>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.html.Option"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.penasospesa.action.ICostantiPenaSospesa"%>
<%@ page import="siap.siep.reato.action.ICostantiReato"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.siep.beneficio.model.BeneficioModel"%>

<%@ page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>

<jsp:useBean id="modalita"			scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"        	scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="fascicolo" 		scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="autoritaSentenza"  scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaOrdinanza" scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"     scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiFontiReato"    scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="benefici"			scope="request" class="java.util.Vector"/>

<% 
Collection oggetto =(Collection) request.getAttribute("oggetto");
String strOggetto ="";
Iterator itxOggetto = oggetto.iterator();
while (itxOggetto.hasNext()) {
	DecodificheModel lDecMod = (DecodificheModel)itxOggetto.next();
	strOggetto += lDecMod.getFiltro() + ";";
	strOggetto += lDecMod.getCode() + ";";
	strOggetto += lDecMod.getDescription() + "#";
}
// 19/04/2019 - MEV70  Eventuale Impostazione del tipo di beneficio in Revoca
String lCheckSospCond ="  ";
String lCheckNonMenzione = "";
BeneficioModel beneficio = new BeneficioModel();
Iterator itx = benefici.iterator();
while (itx.hasNext()) {
	beneficio = (BeneficioModel)itx.next();
	if ("01".equals(beneficio.getCodTipoBeneficio()) )
		lCheckSospCond="checked";
	if ("02".equals(beneficio.getCodTipoBeneficio()) )
		lCheckNonMenzione="checked";
}
%>
<html>
<head>
<title>[S.I.E.S.] - Gestione Annotazione Revoca Beneficio ex art.168 c.p. - 674 c.p.p. </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
<script language="JavaScript">
var desktop;
function ListaComuni(a_formname,a_fieldname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function ControllaDataCopia(Data) {
	if (Data.length != 10)
		return false;
	day = Data.substring(0,2);
	month = Data.substring(3,5);
	year = Data.substring(6,10);
	var Miadata = new Date(year, month-1, day);
	var rgiorno = Miadata.getDate();
	var rmese = Miadata.getMonth();
	var ranno = Miadata.getYear();
	return (((day==rgiorno) && (day>0)) && ((month-1==rmese) && (month>0)) && year>0);
}
function Verifica() {
	var ind = document.getElementById('<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_PROVVEDIMENTO%>').selectedIndex;
    var cod = document.getElementById('<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_PROVVEDIMENTO%>').options[ind].text;
    if (cod == "Sentenza") {
    	var data_sentenza=document.LoadInserisciAnnotazioneRevoca.<%=ICostantiPenaSospesa.CAMPO_GIORNO_DATA_SENTENZA_REVOCA%>.value
    		+ '/' + document.LoadInserisciAnnotazioneRevoca.<%=ICostantiPenaSospesa.CAMPO_MESE_DATA_SENTENZA_REVOCA%>.value
    		+ '/' + document.LoadInserisciAnnotazioneRevoca.<%=ICostantiPenaSospesa.CAMPO_ANNO_DATA_SENTENZA_REVOCA%>.value;
	    if (!ControllaDataCopia(data_sentenza) || data_sentenza.length == 2) {
			alert('Data Sentenza non valida');
			return false;
	    }
		if (document.LoadInserisciAnnotazioneRevoca.<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_SENTENZA_REVO%>.value.length == 1) {
	        alert('Il campo Autorità Emittente è obbligatorio');
	        return false;
      	}
	} else {
    	var data_ordi = document.LoadInserisciAnnotazioneRevoca.<%=ICostantiPenaSospesa.CAMPO_GIORNO_DATA_ORDINANZA_REVOCA%>.value
    		+ '/' + document.LoadInserisciAnnotazioneRevoca.<%=ICostantiPenaSospesa.CAMPO_MESE_DATA_ORDINANZA_REVOCA%>.value
    		+ '/' + document.LoadInserisciAnnotazioneRevoca.<%=ICostantiPenaSospesa.CAMPO_ANNO_DATA_ORDINANZA_REVOCA%>.value;
	    if (!ControllaDataCopia(data_ordi) || data_ordi.length == 2) {
			alert('Data Emissione non valida');
			return false;
	    }
		if (document.LoadInserisciAnnotazioneRevoca.<%=ICostantiPenaSospesa.CAMPO_NUMERO_ORDINANZA_REVOCA%>.value.length == 0) {
	        alert('Il campo Numero Provvedimento è obbligatorio');
	        return false;
      	}
		if (document.LoadInserisciAnnotazioneRevoca.<%=ICostantiPenaSospesa.CAMPO_ANNO_ORDINANZA_REVOCA%>.value.length == 0) {
	        alert('Il campo Anno Provvedimento è obbligatorio');
	        return false;
      	}
		if (document.LoadInserisciAnnotazioneRevoca.<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_ORDINANZA_REVO%>.value.length == 1) {
	        alert('Il campo Autorità Emittente è obbligatorio');
	        return false;
      	}
		if (document.LoadInserisciAnnotazioneRevoca.<%=ICostantiPenaSospesa.CAMPO_COD_LUOGO_ORDINANZA_REVOCA%>.value.length == 0) {
	        alert('Il campo Luogo Autorità Emittente è obbligatorio');
	        return false;
      	}
	}
    return true;
}

//***************************
function inizia() {
	caricatuttecombo();
	cambia();
}

function conferma() {
	// MEV_2025-48: cambiato il msg "in classe I"
	var risposta = confirm('Viene ora creato un nuovo procedimento nella classe di seguito selezionata mentre viene archiviato il relativo procedimento di classe III!\nVuoi proseguire?');
	return risposta;
}

function concludi() {
	var risposta = Verifica();
	if (risposta)
		risposta = conferma();
	return risposta;
}
function caricaCombo (valueTextStr, sep1, sep2, filtro, selField) {
    // valueTextStr = stringa nel formato richiesto
    // sep1 = separatore interno alla coppia di valori
    // sep2 = separatore tra coppie
    // filtro = valore su cui fare il test
    // selField = oggetto combo da caricare
    clearDropDown(selField);
    var aPairs = valueTextStr.split(sep2);
    if (valueTextStr.substr(valueTextStr.length - 1) == sep2) {
		aPairs[aPairs.length - 1] = null;
		aPairs.length--;
    }
    for (var i = 0; i < aPairs.length; i++) {
		aValueText = aPairs[i].split(sep1);
      	if (filtro == 'null' || filtro == aValueText[0]) {
			oItem = new Option;
    		oItem.value = aValueText[1];
    		oItem.text = aValueText[2];
    		selField.options[selField.options.length] = oItem;
      	}
    }
    selField.options.selectedIndex = 0;
}

function clearDropDown (selField) {
  	while (selField.options.length > 0)
  		selField.options[0] = null;
}

function caricatuttecombo() {
	var strOggetto = "<%=strOggetto%>";
  	var art = document.getElementById("<%=ICostantiPenaSospesa.CAMPO_COD_ARTICOLO%>").selectedIndex;
    caricaCombo(strOggetto, ';', '#', document.getElementById("<%=ICostantiPenaSospesa.CAMPO_COD_ARTICOLO%>").options[art].text, document.LoadInserisciAnnotazioneRevoca.<%=ICostantiPenaSospesa.CAMPO_COD_MOTIVO%>);
}

function cambia() {
	var ind = document.getElementById('<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_PROVVEDIMENTO%>').selectedIndex;
    var cod = document.getElementById('<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_PROVVEDIMENTO%>').options[ind].text;
    if (cod == "Ordinanza") {
		document.getElementById("DivOrdinanza").style.display = "block";
		document.getElementById("DivOrdinanza").style.visibility = "visible";
		document.getElementById("DivSentenza").style.display = "none";
		document.getElementById("DivSentenza").style.visibility = "hidden";
    } else {
		document.getElementById("DivOrdinanza").style.display = "none";
		document.getElementById("DivOrdinanza").style.visibility = "hidden";
		document.getElementById("DivSentenza").style.display = "block";
		document.getElementById("DivSentenza").style.visibility = "visible";
	}
}

function ListaProcedimenti(a_formname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penasospesa.action.ActListaFascicoliDelSoggetto&TipoDoc=Sentenza&formname="+a_formname+"&IdSoggetto=<%=fascicolo.getSoggetto().getIdSoggetto()%>&", "Lista_Procedimenti_Associati", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=900, height=500");
}
    
function ListaOrdinanzeSIGE(a_formname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penasospesa.action.ActListaFascicoliDelSoggetto&TipoDoc=Ordinanza&formname="+a_formname+"&IdSoggetto=<%=fascicolo.getSoggetto().getIdSoggetto()%>&", "Lista_Procedimenti_Associati", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=900, height=500");    }
</script>
</head>

<body class="corpo" onLoad="javascript:inizia()">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
EventoModel lEvento = new EventoModel();
String lAzione = new String();
if (modalita.equals("I")) {
	lAzione = "siap.siep.penasospesa.action.ActInserisciAnnotazioneRevoca";
%>
			<font class="campo">Inserimento Annotazione Revoca Beneficio ex art.168 c.p. - 674 c.p.p.</font>
<%
} else if (modalita.equals("M")) {
	lAzione = "siap.siep.penasospesa.action.ActModificaAnnotazioneRevoca";
%>
         	<font class="campo">Modifica Annotazione Revoca Beneficio ex art.168 c.p. - 674 c.p.p.</font>
<%
}
%>
		</td>
  	 	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciAnnotazioneRevoca" >
<table width='95%' cellspacing=2 cellpadding=2>
	<tr>
		<td  width='25%' class="l">Tipo di Provvedimento (*)</td>
   		<td class="l">
     			<select name="<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_PROVVEDIMENTO%>" onchange="JavaScript:cambia();">
          			<option selected>Sentenza</option>
          			<option>Ordinanza</option>          
        		</select>
      	</td>
	</tr>
</table><br>
<!-- DIV del TIPO SENTENZA -->
<div id=DivSentenza class="label" style="visibility:block; position:relative;">
<table width="95%">
	<tr>
   		<td class="l" colspan="4">
   			<a href="Javascript:ListaProcedimenti('LoadInserisciAnnotazioneRevoca');">
   				Elenco Procedimenti nel Distretto  <img src="/images/filefolder.gif" border=0>
  			</a>
   		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
<!--     <tr> -->
<!--       	<td colspan="2"> -->
	<tr><td class="Titolo" colspan="4">Estremi del Provvedimento</td></tr>
	<tr>
   		<td class="l">Data Arrivo Atto</td>
  		<td class="l">
	       	<input Title="Giorno Arrivo Atto" type="text" value="" name="<%=ICostantiPenaSospesa.CAMPO_GIORNO_DATA_ARRIVO_ATTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	       	-
	       	<input Title="Mese Arrivo Atto" type="text" value="" name="<%=ICostantiPenaSospesa.CAMPO_MESE_DATA_ARRIVO_ATTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	       	-
	       	<input Title="Anno Arrivo Atto" type="text" value="" name="<%=ICostantiPenaSospesa.CAMPO_ANNO_DATA_ARRIVO_ATTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	       	<input type="hidden" value="" name="giornoIrrevocabilita">
	       	<input type="hidden" value="" name="meseIrrevocabilita">
	       	<input type="hidden" value="" name="annoIrrevocabilita">
		</td>
   	</tr>
   	<tr>
      	<td class="l">Anno/Numero R.G.N.R.</td>
      	<td class="l">
        	<input Title="Anno R.G.N.R." value="" name="<%=ICostantiPenaSospesa.CAMPO_ANNO_RGNR_REVOCA%>" maxlength="4" size="4">
        	/
        	<input Title="Numero R.G.N.R." value="" type="text" name="<%=ICostantiPenaSospesa.CAMPO_NUMERO_RGNR_REVOCA%>" maxlength="6" size="6">
      	</td>
      	<td class="l">Anno/Numero Reg.Gen.</td>
      	<td class="l">
        	<input Title="Anno Reg.Gen." value="" type="text" name="<%=ICostantiPenaSospesa.CAMPO_ANNO_RegGen_REVOCA%>" maxlength="4" size="4">
        	/
        	<input Title="Numero Reg.Gen." value="" type="text" name="<%=ICostantiPenaSospesa.CAMPO_NUMERO_RegGen_REVOCA%>" maxlength="6" size="6">
      	</td>
   	</tr>
   	<tr>
      	<td class="l">Data Sentenza (*)</td>
   	  	<td class="l">
        	<input Title="Giorno Data Sentenza" type="text" value="" name="<%=ICostantiPenaSospesa.CAMPO_GIORNO_DATA_SENTENZA_REVOCA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        	-
        	<input Title="Mese Data Sentenza" type="text" value="" name="<%=ICostantiPenaSospesa.CAMPO_MESE_DATA_SENTENZA_REVOCA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        	-
        	<input Title="Anno Data Sentenza" type="text" value="" name="<%=ICostantiPenaSospesa.CAMPO_ANNO_DATA_SENTENZA_REVOCA%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
     		</td>
      	<td class="l" width="25%">Anno/Numero Sentenza</td>
      	<td class="l">
        	<input Title="Anno Sentenza" value="" type="text" name="<%=ICostantiPenaSospesa.CAMPO_ANNO_SENTENZA_REVOCA%>" maxlength="4" size="4">
        	/
        	<input Title="Numero Sentenza" value="" type="text" name="<%=ICostantiPenaSospesa.CAMPO_NUMERO_SENTENZA_REVOCA%>" maxlength="6" size="6">
      	</td>
   	</tr>
   	<tr>
      	<td class="l">Autorità Emittente (*)</td>
        <td class="l">
          	<select Title="Autorità Sentenza" name="<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_SENTENZA_REVO%>">
            	<%=autoritaSentenza%>
          	</select>
        </td>
   	</tr>
    <tr>
   		<td class="l">Luogo Emittente</td>
      	<td class="l">
        	<input Title="Luogo Autorità Emittente" name="<%=ICostantiPenaSospesa.CAMPO_COD_LUOGO_SENTENZA_REVOCA%>" value="" type="text" maxlength="35" size="35">
        	<a href="Javascript:ListaComuni('LoadInserisciAnnotazioneRevoca','<%=ICostantiPenaSospesa.CAMPO_COD_LUOGO_SENTENZA_REVOCA%>');">
          	<img src="/images/filefolder.gif" border=0>
        	</a>
      	</td>
     		<td class="l">Sez. Emittente</td>
      	<td class="l">
        	<input Title="Sezione" name="<%=ICostantiPenaSospesa.CAMPO_SEZIONE_SENTENZA_REVOCA%>" value="" type="text" maxlength="35" size="35">
      	</td>
   	</tr>
   	<tr><td>&nbsp;</td></tr>
   	<tr><td class="Titolo" colspan=4>Tipo Beneficio Revocato</td></tr>
	<tr>
		<td class="l" width=30% >Sospensione Condizionale</td>
		<td class="l" colspan=3><input type="checkbox" name="<%= ICostantiPenaSospesa.CAMPO_FLAG_SOSP_COND %>" value="1" <%=lCheckSospCond%>></td>
	</tr>
	<tr>
		<td class="l" width=30% >Non Menzione</td>
		<td class="l" colspan=3><input type="checkbox" name="<%= ICostantiPenaSospesa.CAMPO_FLAG_NON_MENZIONE %>" value="1" <%=lCheckNonMenzione%>></td>
	</tr>
</table>
<br>
<table width="95%">
	<tr>
      	<td>
        	<input type="submit" value="Conferma" class="bottone" name="Inserisci" onclick="javascript:return concludi()">
      	</td>
<%
if (!lTipoFunzione.equals("")) {
%>
   		<td>
     		<input type="button" class="bottone" name="AggAtt" value="Prosegui" onClick="javascript:return Benefici();">
   		</td>
<%
}
%>
	</tr>
</table>
</div>
<!-- DIV del TIPO ORDINANZA  -->
<div id="DivOrdinanza" class="label" style="visibility:none; position:relative;">
<table width="95%">
	<tr>
		<td class="l"  colspan="3">
			<a href="Javascript:ListaOrdinanzeSIGE('LoadInserisciAnnotazioneRevoca');">
				Elenco Ordinanze nel Distretto  <img src="/images/filefolder.gif" border=0>
			</a>
		</td>
	</tr>
<!--     <tr> -->
<!--       <td colspan="2"> -->
    <tr><td class="Titolo" colspan=4>Estremi del Provvedimento</td></tr>
   	<tr>
      	<td class="l">Data Emissione Provvedimento (*)</td>
   	  	<td class="l">
        	<input Title="Giorno Emissione Provvedimento" name="<%=ICostantiPenaSospesa.CAMPO_GIORNO_DATA_ORDINANZA_REVOCA%>" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lEvento.getDataRichiesta(),"dd"))%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        	-
        	<input Title="Mese Emissione Provvedimento" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lEvento.getDataRichiesta(),"MM")) %>" name="<%=ICostantiPenaSospesa.CAMPO_MESE_DATA_ORDINANZA_REVOCA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        	-
        	<input Title="Anno Emissione Provvedimento" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lEvento.getDataRichiesta(),"yyyy")) %>" name="<%=ICostantiPenaSospesa.CAMPO_ANNO_DATA_ORDINANZA_REVOCA%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
     		</td>
      	<td class="l">Anno/Numero Provvedimento (*)</td>
      	<td class="l">
        	<input Title="Anno Provvedimento" value="" type="text" name="<%=ICostantiPenaSospesa.CAMPO_ANNO_ORDINANZA_REVOCA%>" maxlength="4" size="4">
        	/
        	<input Title="Numero Provvedimento" value="" type="text" name="<%=ICostantiPenaSospesa.CAMPO_NUMERO_ORDINANZA_REVOCA%>" maxlength="6" size="6">
      	</td>
   	</tr>
   	<tr>
      	<td class="l">Autorità Emittente (*)</td>
        <td class="l">
          <select Title="Autorità Sentenza" name="<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_ORDINANZA_REVO%>">
            <%=autoritaSentenza%>
          </select>
        </td>
   	</tr>
    <tr>
   		<td class="l">Luogo Emittente (*)</td>
      	<td class="l">
        	<input Title="Luogo Autorità Emittente" name="<%=ICostantiPenaSospesa.CAMPO_COD_LUOGO_ORDINANZA_REVOCA%>" value="" type="text" maxlength="35" size="35">
        	<a href="Javascript:ListaComuni('LoadInserisciAnnotazioneRevoca','<%=ICostantiPenaSospesa.CAMPO_COD_LUOGO_ORDINANZA_REVOCA%>');">
          	<img src="/images/filefolder.gif" border=0>
        	</a>
      	</td>
     		<td class="l">Sez. Emittente</td>
      	<td class="l">
        	<input Title="Sezione" name="<%=ICostantiPenaSospesa.CAMPO_SEZIONE_ORDINANZA_REVOCA%>" value="" type="text" maxlength="35" size="35">
      	</td>
   	</tr>
</table>
<table width="95%">
	<tr>
      	<td class="l">Oggetto Decisione</td>
      	<td class="l">REVOCA SOSPENSIONE CONDIZIONALE DELLA PENA</td>
	</tr>
	<tr>
      	<td class="l">Articolo (*)</td>
      	<td class="l">
   			<select name="<%= ICostantiPenaSospesa.CAMPO_COD_ARTICOLO %>" Title="Tipo Provvedimento" onchange="javascript:caricatuttecombo()">
				<option value ="">-</option> 
<%
Iterator itxArticolo = oggetto.iterator();
while (itxArticolo.hasNext()) {
   DecodificheModel lDecMod = (DecodificheModel) itxArticolo.next();
   // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   siesLogger.debug("lDecMod.getCode() = " + lDecMod.getCode());
   // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   siesLogger.debug("lDecMod.getCodiceAlt2() = " + lDecMod.getCodiceAlt2());
   if ((lDecMod.getCode()).equals("1100") || (lDecMod.getCode()).equals("1101")
		   || (lDecMod.getCode()).equals("1102") || (lDecMod.getCode()).equals("1103")
		   || (lDecMod.getCode()).equals("1104") || (lDecMod.getCode()).equals("1105")
		   || (lDecMod.getCode()).equals("1106") || (lDecMod.getCode()).equals("1107")
		   || (lDecMod.getCode()).equals("1108")) {
		if ((lDecMod.getCode()).equals(lDecMod.getCodiceAlt2())) {
%>
				<option value = <%=lDecMod.getCode()%> ><%=lDecMod.getFiltro() %></option> 
<%  
		}
	}
}
%>
			</select>
		</td>
	</tr>
	<tr>
		<td class="l">Motivazione (*)</td>
		<td class="l">
			<select class="small" name="<%= ICostantiPenaSospesa.CAMPO_COD_MOTIVO %>" Title="Tipo Provvedimento"></select>
		</td>
	</tr>
	<tr>
		<td class="l">Note</td>
		<td class="l">
		 	<Textarea Title="Note" name="<%= ICostantiPenaSospesa.CAMPO_NOTE %>" cols="80" rows="5"></textarea>
		</td>
	</tr>
	<tr>
		<td class="l" width="30%">Non Menzione</td>
		<td class="l" colspan="3"><input type="checkbox" name="<%= ICostantiPenaSospesa.CAMPO_FLAG_NON_MENZIONE %>" value="1"></td>
	</tr>
</table>
<br>
<table width="95%">
	<tr>
      	<td>
        	<input type="submit" value="Conferma" class="bottone" name="Inserisci" onclick="javascript:return concludi()">
      	</td>
<%
if (!"".equals(lTipoFunzione)) {
%>
		<td>
			<input type="button" class="bottone" name="AggAtt" value="Prosegui" onClick="javascript:return Benefici();">
		</td>
<%
}
%>
	</tr>
</table>
</div>
<input value="" type="HIDDEN" name="">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
<input type="HIDDEN" name="flagDaLista" value="">
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadInserisciAnnotazioneRevoca");

if (document.getElementById("DivSentenza").style.visibility == "visible") {
	var data_sentenza = document.LoadInserisciAnnotazioneRevoca.<%=ICostantiPenaSospesa.CAMPO_GIORNO_DATA_SENTENZA_REVOCA%>.value
		+ '/' + document.LoadInserisciAnnotazioneRevoca.<%=ICostantiPenaSospesa.CAMPO_MESE_DATA_SENTENZA_REVOCA%>.value
		+ '/' + document.LoadInserisciAnnotazioneRevoca.<%=ICostantiPenaSospesa.CAMPO_ANNO_DATA_SENTENZA_REVOCA%>.value;
	if (data_sentenza.length < 10) {
		frmvalidator.addValidation("<%= ICostantiPenaSospesa.CAMPO_GIORNO_DATA_SENTENZA_REVOCA%>","req", "Il campo Data Sentenza è obbligatorio");
	}
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_ANNO_SENTENZA_REVOCA%>","numeric");
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_ANNO_SENTENZA_REVOCA%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_ANNO_SENTENZA_REVOCA%>","lt=3000");
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_NUMERO_SENTENZA_REVOCA%>","numeric");
	
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_ANNO_RGNR_REVOCA%>","numeric");
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_ANNO_RGNR_REVOCA%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_ANNO_RGNR_REVOCA%>","lt=3000");
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_NUMERO_RGNR_REVOCA%>","numeric");
	
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_ANNO_RegGen_REVOCA%>","numeric");
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_ANNO_RegGen_REVOCA%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_ANNO_RegGen_REVOCA%>","lt=3000");
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_NUMERO_RegGen_REVOCA%>","numeric");
	
	frmvalidator.setAddnlValidationFunction("Verify");
}
if (document.getElementById("DivOrdinanza").style.visibility == "visible") {
	frmvalidator.addValidation("<%= ICostantiPenaSospesa.CAMPO_GIORNO_DATA_ORDINANZA_REVOCA%>","req", "Il campo Data Emissione è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiPenaSospesa.CAMPO_MESE_DATA_ORDINANZA_REVOCA%>","req", "Il campo Data Emissione è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiPenaSospesa.CAMPO_ANNO_DATA_ORDINANZA_REVOCA%>","req", "Il campo Data Emissione è obbligatorio");

	frmvalidator.addValidation("<%= ICostantiPenaSospesa.CAMPO_NUMERO_ORDINANZA_REVOCA%>","req", "Il campo Anno Provvedimento è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiPenaSospesa.CAMPO_ANNO_ORDINANZA_REVOCA%>","req", "Il campo Anno Provvedimento è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_ORDINANZA_REVO%>","req", "Il campo Autorità Emittente è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiPenaSospesa.CAMPO_COD_LUOGO_ORDINANZA_REVOCA%>","req", "Il campo Luogo Autorità Emittente è obbligatorio");

	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_ANNO_ORDINANZA_REVOCA%>","numeric");
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_ANNO_ORDINANZA_REVOCA%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_ANNO_ORDINANZA_REVOCA%>","lt=3000");
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_NUMERO_ORDINANZA_REVOCA%>","numeric");
	
	frmvalidator.setAddnlValidationFunction("Verify");
}
</script>
</body>
</html>
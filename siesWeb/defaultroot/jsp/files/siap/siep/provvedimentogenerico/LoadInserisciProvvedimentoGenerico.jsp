<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="f3b.web.IWebConstants" %>

<%@ page import="java.math.BigDecimal"%>

<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.provvedimentogenerico.action.ICostantiProvvedimentoGenerico"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="penaresidua" 			scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="AzioneChiamante" 		scope="request" class="java.lang.String" />
<jsp:useBean id="tipoprovvedimento" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="dataeditabile"       	scope="request" class="java.lang.String"/>
<jsp:useBean id="tiporegistro"       	scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroMinorenni" 		scope="request" class="java.lang.String"/>

<% 
Collection contenuto = (Collection) request.getAttribute("contenuto");
Collection esito = (Collection) request.getAttribute("esito");
Collection oggetto = (Collection) request.getAttribute("oggetto");

FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
if (lPosizione == null)
	lPosizione = new PosizioneGiuridicaModel();

if (lLuogoDetenzione == null)
	lLuogoDetenzione = new LuogoDetenzioneModel();

if (lAltraCausa == null)
	lAltraCausa = new AltraCausaModel();

String strContenuto = "";
Iterator itx = contenuto.iterator();

while (itx.hasNext()) {
	DecodificheModel lDecMod = (DecodificheModel) itx.next();
   	strContenuto += lDecMod.getCodiceAlternativo() +";";
   	strContenuto += lDecMod.getCode()+";";
   	strContenuto += lDecMod.getDescription()+"#";
   	if ("TDS".equals(lDecMod.getCodiceAlternativo())
   			|| "UDS".equals(lDecMod.getCodiceAlternativo())) {
   		strContenuto += lDecMod.getCodiceAlternativo() + "M;";
       	strContenuto += lDecMod.getCode() + ";";
       	strContenuto += lDecMod.getDescription() + "#";
   	}
}

// 20190606 [SG]: per questo contenuto filtro oggetti ed esiti
List<String> oggettiU086 = Arrays.asList(new String[] {"2116","2117","2118","2119"});
List<String> esitiU086 = Arrays.asList(new String[] {"0003","0005","0004","0002"});
String strOggetto = "";
Iterator itxOggetto = oggetto.iterator();
while (itxOggetto.hasNext()) {
	DecodificheModel lDecMod = (DecodificheModel) itxOggetto.next();
	if ("U086".equals(lDecMod.getCodiceAlternativo())
			&& !oggettiU086.contains(lDecMod.getCode())) {
		continue;
	} else {
		strOggetto += lDecMod.getCodiceAlternativo() +";";
		strOggetto += lDecMod.getCode()+";";
		strOggetto += lDecMod.getDescription()+"#";
	}
}

String strEsito = "";
Iterator itxEs = esito.iterator();
while (itxEs.hasNext()) {
	DecodificheModel lDecMod = (DecodificheModel) itxEs.next();
	if ("U086".equals(lDecMod.getCodiceAlternativo())
			&& !esitiU086.contains(lDecMod.getFiltro())) {
		continue;
	} else {
		strEsito += lDecMod.getCodiceAlternativo() +";";
		strEsito += lDecMod.getFiltro()+";";
		strEsito += lDecMod.getDescription()+"#";
	}
}
%>

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<title>[S.I.E.S.] - Annotazione Provvedimento</title>

<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
<script language="JavaScript">
var desktop;

function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

var strContenuto = "<%=strContenuto%>";
var strOggetto = "<%=strOggetto%>"
var strEsito = "<%=strEsito%>";

function caricaCombo(valueTextStr, sep1, sep2, filtro, selField) {
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

function clearDropDown(selField) {
	while (selField.options.length > 0)
  	selField.options[0] = null;
}

function caricatuttecombo() {
<%
if (tiporegistro.equals("0001")) {
%>
	caricaCombo(strContenuto,';','#',document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_COD_AUTORITA%>.value,document.f.<%= ICostantiProvvedimentoGenerico.CAMPO_CONTENUTO%>);
<%
} else {
%>
<%-- 20200625 [SG]: Ticket#20200625017 - siep-iachetta # ripristinato valore GE --%>
<%-- caricaCombo(strContenuto,';','#',"UDS",document.f.<%= ICostantiProvvedimentoGenerico.CAMPO_CONTENUTO%>); --%>
	caricaCombo(strContenuto,';','#',"GE",document.f.<%= ICostantiProvvedimentoGenerico.CAMPO_CONTENUTO%>);
<%
}
%>
    caricaCombo(strOggetto,';','#',document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_CONTENUTO%>.value,document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_OGGETTO%>);
    caricaCombo(strEsito,';','#',document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_CONTENUTO%>.value,document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_ESITO%>);
}

function caricatuttecomboContenuto() {
    caricaCombo(strOggetto,';','#',document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_CONTENUTO%>.value,document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_OGGETTO%>);
    caricaCombo(strEsito,';','#',document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_CONTENUTO%>.value,document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_ESITO%>);
}

function Verify() {
	// DATA EMISSIONE PROVVEDIMENTO (obbligatoria)
 	if (document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
   		document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
 	if (document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
   		document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_MESE_DATA_EMISSIONE%>.value;
	var data_to_verify = document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_ANNO_DATA_EMISSIONE%>.value;
	if (!ControllaData(data_to_verify)) {
   		alert('Data emissione provvedimento non valida');
   		document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_GIORNO_DATA_EMISSIONE %>.focus();
   		return false;
 	}
 	// Non è possibile specificare solo il numero o solo l'anno per il provvedimento
 	if ((document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_NUMERO_PROVVEDIMENTO%>.value.length != 0)
 			&& (document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_ANNO_PROVVEDIMENTO%>.value.length == 0)) {
   		alert("Valorizzare Anno Provvedimento");
   		document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_ANNO_PROVVEDIMENTO %>.focus();
   		return false;
 	}
	if ((document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_NUMERO_PROVVEDIMENTO%>.value.length == 0)
			&& (document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_ANNO_PROVVEDIMENTO%>.value.length != 0)) {
		alert("Valorizzare Numero Provvedimento");
		document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_NUMERO_PROVVEDIMENTO %>.focus();
   		return false;
 	}
	if (document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_COD_TIPO_PROVVEDIMENTO%>[document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_COD_TIPO_PROVVEDIMENTO%>.selectedIndex].value == '-') {
 		alert("Il Campo Tipo Provvedimento è obbligatorio");
 		return false;
	}
	if (document.f.<%=ICostantiProvvedimentoGenerico.CAMPO_SEDE_AUTORITA%>.value == "") {
   		alert("La Sede dell'Autorità è obbligatoria");
   		return false;
 	}
	return true;
}
</script>

<jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>

</head>
<body class="corpo" onLoad="javascript:caricatuttecombo()">
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
           	<font  class="label">Funzione :&nbsp;</font>
        	<font class="campo">Annotazione Provvedimento</font>
		</td>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.provvedimentogenerico.action.ActInserisciProvvedimentoGenerico">
<input type="HIDDEN" name="AzioneChiamante" value="<%=AzioneChiamante%>">
<input type="HIDDEN" name="tiporegistro" value="<%=tiporegistro%>">
<table>
	<tr>
     	<td class="l">Posizione Giuridica </td>
      	<td class="L" colspan=5>
          	<font class="campo">
<%
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
%>
				DETENUTO PER ALTRA CAUSA
<%
} else {
%>
         		<%=lPosizione.getDescrPosizioneGiuridica()%>
<%
}
%>
         	</font>
   		</td>
	</tr>
<%
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
	if (lAltraCausa.getIstitutoDetenzione() != null) {
%>
	<tr>
		<td class="l">Detenuto presso </td>
		<td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font></td>
	</tr>
<%
		if (lAltraCausa.getAltroLuogo() != null) {
%>
	<tr>
	  	<td class="l">Altro Luogo </td >
	  	<td class="L" colspan=5>
	    	<font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
	  	</td>
	</tr>
<%
		}
	}
} else if (lLuogoDetenzione.getIstitutoDetenzione() != null) {
%>
	<tr>
 		<td class="l">Detenuto presso </td>
 		<td class="L" colspan=5>
  			<font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
			di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
  		</td>
	</tr>
<%
}
%>
	<input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
<%
// Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI (02, 04)
if (lPosizione.getCodPosizioneGiuridica() != null
		&& (lPosizione.getCodPosizioneGiuridica().equals("02")
				|| lPosizione.getCodPosizioneGiuridica().equals("04"))) {
	if (lLuogoDetenzione.getAltroLuogo() != null) {
%>
	<tr>
	  	<td class="l">Indirizzo</td>
	  	<td class="L" colspan=5>
	    	<font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
	 	</td>
	</tr>
<%
	}
}
%>
   	<tr>
<%
if (penaresidua.getIdPenaResidua() != null
		&& ((penaresidua.getFlagErgastolo() == null)
				|| (penaresidua.getFlagErgastolo() != null
				&& !penaresidua.getFlagErgastolo().equals("S")
				&& !penaresidua.getFlagErgastolo().equals("D")))) {
	if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0)) {
		// niente da dichiarare
	} else {
%>
		<td class="l">Reclusione</td>
		<td class="l" colspan=2>
			<font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
		</td>
		<td class="l">Multa</td>
		<td class="l" colspan=2>
			<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;
			<font class="l">Euro</font>
		</td>
<%
	}
%>
	</tr>
   	<tr>
<%
	if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0)) {
		// niente da dichiarare
	} else {
%>
		<td class="l" >Arresto</td>
		<td class="l" colspan=2>
			<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
		</td>
		<td class="l">Ammenda</td>
		<td class="l" colspan=2>
			<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;
			<font class="l">Euro</font>
		</td>
<%
	}
}
%>
	</tr>
	<tr>
<%
if (penaresidua.getDataInizio() != null) {
%>
		<td class="l">Data Decorrenza Pena</td>
		<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font>
		</td>
<%
}
if (penaresidua.getFlagErgastolo() != null) {
	if (penaresidua.getFlagErgastolo().equals("S")) {
%>
		<td class="l">Pena Detentiva</td>
		<td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
	} else if (penaresidua.getFlagErgastolo().equals("D")) {
%>
		<td class="l">Pena Detentiva</td>
		<td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
	 }
}

if ((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa() != null
		&& lFascicoloAssociato.getFlagAltraCausa().equals("S"))) {
	if ((penaresidua.getFlagErgastolo() == null)
			|| (penaresidua.getFlagErgastolo() != null
			&& !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) {
  		if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan=2>
		  	<input title = "Giorno Data Fine Pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>"  <%=IWebConstants.UTIL_DATA%> >
			-
			<input title = "Mese Data Fine Pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>"  <%=IWebConstants.UTIL_DATA%> >
			-
			<input title = "Anno Data Fine Pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>"  <%=IWebConstants.UTIL_DATA_ANNO%> >
		</td>
<%
		} else if (penaresidua.getDataFine() != null) {
			if (penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan=2>
			<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
		</td>
<%
          	} else {
%>
		<td class="l">Data Fine Pena</td>
		<td class="lRosso" colspan=2>
			<font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
		</td>
<%
			}
		}
	}
}
%>
		<input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
	</tr>
</table>
<br>
<table style="width: 95%;">
	<tr>
  		<td colspan=3 class="titolo">Dati Ordinanza/Decreto</td>
	</tr>
	<tr>
  		<td class="l">
    		Data emissione provvedimento <font class="ob">(*)</font>&nbsp;
    		<input type="text" Title="Giorno emissione provvedimento" value="" name="<%= ICostantiProvvedimentoGenerico.CAMPO_GIORNO_DATA_EMISSIONE %>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
			/
			<input type="text" Title="Mese emissione provvedimento" value="" name="<%= ICostantiProvvedimentoGenerico.CAMPO_MESE_DATA_EMISSIONE%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
			/
			<input type="text" Title="Anno emissione provvedimento" value="" name="<%= ICostantiProvvedimentoGenerico.CAMPO_ANNO_DATA_EMISSIONE%>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
			&nbsp;&nbsp;&nbsp;&nbsp;
			Anno/Numero provvedimento
			<input type="text" Title="Anno Provvedimento" value="" name="<%= ICostantiProvvedimentoGenerico.CAMPO_ANNO_PROVVEDIMENTO %>" size=4 maxlength=4>
			/
			<input type="text" Title="Numero Provvedimento" value="" name="<%= ICostantiProvvedimentoGenerico.CAMPO_NUMERO_PROVVEDIMENTO %>" size=6 maxlength=6>&nbsp;&nbsp;
  		</td>
	</tr>
	<tr>
  		<td class="l">
		    Tipo provvedimento <font class="ob">(*)</font>
		    &nbsp;&nbsp;&nbsp;&nbsp;
		    &nbsp;&nbsp;&nbsp;&nbsp;
		    &nbsp;&nbsp;&nbsp;&nbsp;
		    &nbsp;&nbsp;
    		<select Title="Tipo Provvedimento" name="<%=ICostantiProvvedimentoGenerico.CAMPO_COD_TIPO_PROVVEDIMENTO%>">
				<%=tipoprovvedimento%>
    		</select>
  		</td>
	</tr>
</table>
<table style="width: 95%;">
	<tr>
	  	<td class="l">Autorità Emittente <font class="ob">(*)</font></td>
	  	<td class="l">
	    	<%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff, "onchange='javascript:caricatuttecombo()'", ICostantiProvvedimentoGenerico.CAMPO_COD_AUTORITA, autorita)%>&nbsp;
			Sede&nbsp;<font class="ob">(*)</font>&nbsp;
			<input title="Sede Autorita Esterna"  type="text" value="" name="<%=ICostantiProvvedimentoGenerico.CAMPO_SEDE_AUTORITA%>"  maxlength="35" size="35">
			<a href="Javascript:ListaComuniEmitUTMinor('f','<%=ICostantiProvvedimentoGenerico.CAMPO_SEDE_AUTORITA%>');">
	      		<img src="/images/filefolder.gif" border=0>
    		</a>
	  	</td>
	</tr>
	<tr>
	  	<td class="l" nowrap>Contenuto <font class="ob">(*)</font></td>
	  	<td class="l">
	    	<select Title="Contenuto Decisione" name="<%=ICostantiProvvedimentoGenerico.CAMPO_CONTENUTO%>" onchange="javascript:caricatuttecomboContenuto();"></select>
	  	</td>
	</tr>
	<tr>
    	<td class="l" nowrap>Oggetto decisione <font class="ob">(*)</font></td>
    	<td class="l">
      		<select Title="Oggetto Decisione" class="small" name="<%=ICostantiProvvedimentoGenerico.CAMPO_OGGETTO%>"></select>
    	</td>
  	</tr>
  	<tr>
      	<td class="l" nowrap>Esito <font class="ob">(*)</font></td>
      	<td class="l">
          	<select Title="Tipologia Decisione" class="small" name="<%=ICostantiProvvedimentoGenerico.CAMPO_ESITO%>"></select>
      </td>
  	</tr>
  	<tr>
    	<td class="l">Note</td>
    	<td class="l">
      		<textarea cols="80" rows="3" name="<%=ICostantiProvvedimentoGenerico.CAMPO_NOTE%>"></textarea>
    	</td>
  	</tr>

  	<tr><td>&nbsp;</td></tr>
  	<tr>
    	<td class="lNoBord" colspan="2">
      	<br><INPUT class="bottone" type="submit" name="I" value="Conferma">
    	</td>
  	</tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("f");
// Sede
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_SEDE_AUTORITA %>","req","Il campo Sede è obbligatorio");

// Anno provvedimento
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_ANNO_PROVVEDIMENTO%>","numeric");
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_ANNO_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno provvedimento è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_ANNO_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno provvedimento è di 4 caratteri");

// Numero provvedimento
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_NUMERO_PROVVEDIMENTO%>","numeric");

// Data emissione provvedimento
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno emissione provvedimento è obbligatorio");
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31")

frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese emissione provvedimento è obbligatorio");
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno emissione provvedimento è obbligatorio");
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_ANNO_DATA_EMISSIONE%>","maxlen=4","La lunghezza massima per l'anno emissione provvedimento è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza minima per l'anno emissione provvedimento è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
frmvalidator.addValidation("<%= ICostantiProvvedimentoGenerico.CAMPO_ANNO_DATA_EMISSIONE%>","lt=3000");

frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
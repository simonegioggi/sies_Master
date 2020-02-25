<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="java.util.Iterator"%>
<%@ page import=" java.util.Collection"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="siap.siep.penasospesa.action.ICostantiPenaSospesa" %>
<%@ page import="f3b.web.html.Option"%>

<jsp:useBean id="modalita"              scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="lTipoFunzione"         scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     			scope="request" class="java.lang.String"/>

<% 
Collection oggetto =(Collection) request.getAttribute("oggetto");
String strOggetto ="";
Iterator itxOggetto = oggetto.iterator();
while(itxOggetto.hasNext()) {
   DecodificheModel lDecMod = (DecodificheModel)itxOggetto.next();
   strOggetto += lDecMod.getFiltro() +";";
   strOggetto += lDecMod.getCode()+";";
   strOggetto += lDecMod.getDescription()+"#";
}
%>

<html>
	<head>
		<title>[S.I.E.S.] - Gestione Richiesta Estinzione di Pena </title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>"/>
		
		<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
		<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
		<script language="JavaScript">
		function ListaComuni(a_formname,a_fieldname) {
		  	var desktop;
			desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
		}
		
		function inizia() {
			caricatuttecombo();
		}
			
		function conferma() {
			var risposta=confirm('Viene ora creato un nuovo preocedimento in classe I mentre\nviene archiviato il relativo procedimento di classe III!\nVuoi proseguire?');
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
		
		    for (var i=0; i < aPairs.length; i++) {
				aValueText = aPairs[i].split(sep1);
		      	if (filtro=='null' || filtro==aValueText[0]) {
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
		  	var art=document.getElementById("<%=ICostantiPenaSospesa.CAMPO_COD_ARTICOLO%>").selectedIndex;
		    caricaCombo(strOggetto,';','#',document.getElementById("<%=ICostantiPenaSospesa.CAMPO_COD_ARTICOLO%>").options[art].text,document.getElementById("<%=ICostantiPenaSospesa.CAMPO_COD_MOTIVO%>"));
		}
		</script>
	</head>
	<body></body>
</html>
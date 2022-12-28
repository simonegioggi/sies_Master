<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.siep.util.MinorMask"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="fascicoloSiusGP" 		scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="contenuto"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"     	scope="request" class="java.util.Date"/>
<jsp:useBean id="inFormaDiPanelTDSM" 	scope="request" class="java.lang.String"/>

<jsp:useBean id="tipoUfficioProcure" 	scope="request" class="java.lang.String"/>

<%
TenoreModel[] tenori = (TenoreModel[]) request.getAttribute("tenori");
String[] esiti = (String[]) request.getAttribute("esiti");

/* Estrazione della data udienza */
String data1;
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
	data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(), "dd/MM/yyyy");
else
	data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(), "dd/MM/yyyy");

//INIZIO: MEV_9 (D.lgs. 123/2018)
boolean is678 = false;
if (ICostantiDepositoOrdinanzaPc.COD_OGGETTO_CONCESSIONE_MISURE_ALTERNATIVA_678.equals(contenuto)
		|| ICostantiDepositoOrdinanzaPc.COD_OGGETTO_CONCESSIONE_MISURE_ALTERNATIVA_678_MINORI.equals(contenuto))
	is678 = true;
//FINE: MEV_9
%>

<html>
<head>
<title>[S.I.E.S.] - Emissione Ordinanza di Misura Alternativa</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="/html/verifyCombo.js"></script>

<script language="JavaScript">
// STUB 21/07/2004 Controllo obbligatorietà esiti.
function Verify() {
	var lEsiti = document.InserisciOrdinanzaMA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
	if (!VerifyCombo(lEsiti, "Esito"))
  		return false;
	// Se l'oggetto è tra 0001, 0002, 0003, 0005, 0010, 0012, 0013, 0195, 0361, 0362, 0610 e l'esito è di Concessione (0001),
	// il Luogo di Svolgimento della prova è obbligatorio
	var lTenori = document.InserisciOrdinanzaMA.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>;
    if (typeof (lTenori[0]) == "undefined") {
		if (((lTenori.value in {'0001':1, '0002':1, '0003':1, '0005':1, '0010':1, '0012':1, '0013':1, '0195':1, '0361':1, '0362':1, '0610':1})
				&& lEsiti.value == '0001')
				// MEV_9: aggiunti 6 (3+3) motivi provvedimento per nuovi contenuti C050 e C051
				|| ((lTenori.value in {'0680':1, '0681':1, '0682':1}) && (lEsiti.value in {'0680':1, '0685':1}))
				|| ((lTenori.value in {'0690':1, '0691':1, '0692':1}) && (lEsiti.value in {'0690':1, '0695':1}))) {
			if (document.InserisciOrdinanzaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.value == "") {
       			alert ('Luogo di svolgimento della prova obbligatorio!');
         		return false;
         	}
      	}
   	}
	for (jTenori = 0; jTenori < lTenori.length; jTenori++) {
		if (lTenori[jTenori].value in {'0001':1, '0002':1, '0003':1, '0005':1, '0010':1, '0012':1, '0013':1, '0195':1, '0361':1, '0362':1, '0610':1}) {
     	  	for (jEsiti = 0; jEsiti < lEsiti[jTenori].length ; jEsiti++) {
				if ((lEsiti[jTenori][jEsiti].selected) && (lEsiti[jTenori][jEsiti].value == '0001')) {
               		if (document.InserisciOrdinanzaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.value == "") {
          				alert ('Luogo di svolgimento della prova obbligatorio!');
            			return false;
            		}
          		}
        	}
    	}
		// MEV_9: aggiunti 6 (3+3) motivi provvedimento per nuovi contenuti C050 e C051
		else if (lTenori[jTenori].value in {'0680':1, '0681':1, '0682':1}) {
     	  	for (jEsiti = 0; jEsiti < lEsiti[jTenori].length ; jEsiti++) {
				if ((lEsiti[jTenori][jEsiti].selected) && (lEsiti[jTenori][jEsiti].value in {'0680':1, '0685':1})) {
               		if (document.InserisciOrdinanzaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.value == "") {
          				alert ('Luogo di svolgimento della prova obbligatorio!');
            			return false;
            		}
          		}
        	}
    	} else if (lTenori[jTenori].value in {'0690':1, '0691':1, '0692':1}) {
     	  	for (jEsiti = 0; jEsiti < lEsiti[jTenori].length ; jEsiti++) {
				if ((lEsiti[jTenori][jEsiti].selected) && (lEsiti[jTenori][jEsiti].value in {'0690':1, '0695':1})) {
               		if (document.InserisciOrdinanzaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.value == "") {
          				alert ('Luogo di svolgimento della prova obbligatorio!');
            			return false;
            		}
          		}
        	}
    	}
 	}

	var ritorno = true;
	var data_camera = '<%=data1%>';
 	// Controllo della data termine misura.
	var data_termine = document.InserisciOrdinanzaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value
		+ '/' + document.InserisciOrdinanzaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value
		+ '/' + document.InserisciOrdinanzaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value;
 	if (data_termine.length > 2) {
   		if (!ControllaData(data_termine)) {
		    alert('Data Termine Misura non valida!');
		    ritorno =  false;
   		} else if (!CompareDate( data_camera, data_termine)) {
		    alert("Data Termine Misura non può precedere " + data_camera + " !");
		    ritorno =  false;
   		}
 	}
	return ritorno;
}

var desktop;
// Chiamata all'elenco degli UDS
function ListaUDS(a_formname,a_fieldname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}
// Chiamata all'elenco degli UEPE
function ListaCSSA(a_formname,a_fieldname, a_fieldcode) {
  	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSA&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldcode="+a_fieldcode, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}
// Chiamata all'elenco degli USSM
function ListaUSSM (a_formname,a_fieldname) {
	desktop = window.open("/jsp/Main.jsp?Action=siap.sico.cssa.action.ActLoadListaUSSM&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}


function updateCkCtrlE() {
	if (document.InserisciOrdinanzaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[0].checked == true) {
		document.InserisciOrdinanzaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[1].checked = false;
 	}
}
 
function updateCkCtrlT() {
	if (document.InserisciOrdinanzaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[1].checked == true) {
		document.InserisciOrdinanzaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[0].checked = false;
	}	
}

<%-- MEV63: cambiata gestione in caso di inserimento di più oggetti (ramo else) --%>
function enableForma() {
	var lEsiti = document.InserisciOrdinanzaMA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
	var node = document.getElementById('tableInForma');
	node.style.display = 'none';
	var forma = '<%=inFormaDiPanelTDSM%>';
	var testVisibilita = (forma == 'visible') ? true : false;
	var len = <%=tenori.length%>;
	if (len == 1) {
		if (lEsiti.options[lEsiti.selectedIndex].selected
				&& lEsiti.options[lEsiti.selectedIndex].value == '0001'
				&& testVisibilita) {
			node.style.display = 'block';
			return;
		}
	} else {
		for (var x = 0; x < len; x++) {
  			for (var jEsiti = 0; jEsiti < lEsiti.length; jEsiti++) {
  				var item = lEsiti[x];
	  			if (item.options[item.selectedIndex].selected
	  					&& item.options[item.selectedIndex].value == '0001'
	  					&& testVisibilita) {
	  				node.style.display = 'block';
	  				return;
	  			}
  			}
		}
	}
}

<%-- INIZIO: MEV_9 (D.lgs. 123/2018) --%>
function checkEsiti() {
	var listComboEsiti = document.getElementsByName('<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>');
	if (typeof (listComboEsiti[1]) == "undefined") {
		var comboEsito = document.InserisciOrdinanzaMA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
		for (j = 0; j < comboEsito.length; j++) {
			if (comboEsito.options[j].value == '0680' || comboEsito.options[j].value == '0690') {
				<%-- Si elimina APPLICA PROVVISORIAMENTE x ordinanza normale --%>
	       		comboEsito.remove(j);
	       		j--;
     		}
   		}
	} else {
		for (i = 0; i < listComboEsiti.length; i++) {
	   		var comboEsito = listComboEsiti[i];
	   		for (j = 0; j < comboEsito.length; j++) {
				if (comboEsito.options[j].value == '0680' || comboEsito.options[j].value == '0690') {
					<%-- Si elimina APPLICA PROVVISORIAMENTE x ordinanza normale --%>
	        		comboEsito.remove(j);
	        		j--;
	      		}
	     	}
   		}
	}
}

</script>
<jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>
</head>
<%
String lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS";
%>

<body class="corpo" onload="checkEsiti();">
<table>
	<tr>
  		<td class="LBG">
  			<a href="Javascript:window.print();">
  				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
  			</a>
  		</td>
 		<td class=LBG>
 			<font class="label">Funzione : </font>
 			<font class="campo">Emissione Ordinanza Misura Alternativa</font>
 		</td>
	</tr>
	<tr>
		<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
	</tr>
</table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaMA">
<table style="width: 95%;">
	<tr>
 		<td class="l" style="width: 35%;">Data Emissione</td>
 		<td class="l"><%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
	</tr>
</table>
<br>
<table cellspacing="2" cellpadding="2" style="width: 95%;">
    <tr>
        <td class="Titolo" colspan="6"> Specificare esito per ciascun oggetto: </td>
    </tr>
    <tr>
        <td class="l" colspan="2"> Oggetto </td>
        <td class="l" colspan="2"> Esito </td>
    </tr>
<%
for (int i=0; i< tenori.length;i++) {
%>
	<tr>
		<td class="l" colspan="2">
          	<input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>" readonly size="85">
          	<input Title="Cod Oggetto" type="hidden" name="<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>">
          	<input Title="Cod Dettaglio Oggetto" type="hidden" name="<%=ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>">
        </td>
		<td class="l" colspan="2">
           	<select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onChange="enableForma();">
             	<%=esiti[i]%>
          	</select>
		</td>
	</tr>
<%
}
%>
</table>
<br>
<table cellspacing="2" cellpadding="2" style="width: 95%;">
	<tr>
		<td class="l">Ulteriore descrizione della decisione</td>
	 	<td class="l"><TEXTAREA title="Ulteriore descrizione della decisione" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4" ></textarea></td>
	</tr>
    <tr>
      	<td class="l">UEPE Competente </td>
      	<td class="l">
        	<input Title="UEPE Competente" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COMUNE_CSSA_COMP%>" value="" size="35">
        	<a href="Javascript:ListaCSSA('InserisciOrdinanzaMA','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COMUNE_CSSA_COMP %>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP %>');">
        		<img src="/images/filefolder.gif" border=0>
        	</a>
      	</td>
    </tr>
<%
/* Visibile solo per gli utenti TDSM e UDSM */
if ("TDSM".equalsIgnoreCase(fascicoloSiusGP.getFascicoloSiusModel().getCodTipoUfficio())
		|| "UDSM".equalsIgnoreCase(fascicoloSiusGP.getFascicoloSiusModel().getCodTipoUfficio()) ){
%>
	<tr>
       	<td class="l">USSM Competente </td>
       	<td class="l">
			<input Title="USSM" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_UFFICIO_USSM%>" value="" size=35 >
			<a href="Javascript:ListaUSSM('InserisciOrdinanzaMA','<%=ICostantiDepositoOrdinanzaPc.CAMPO_UFFICIO_USSM%>');">
	          	<img src="/images/filefolder.gif" border=0>
			</a>        
		</td>
	</tr>
<%
}
if ("UDS".equalsIgnoreCase(fascicoloSiusGP.getFascicoloSiusModel().getCodTipoUfficio())) {
%>
	<tr>
	    <td class="l">Tribunale di Sorveglianza Competente </td>
	    <td class="l">
	      	<input Title="Tribunale" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>" value="" size=35 >
			<a href="Javascript:ListaProcure('InserisciOrdinanzaMA','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>');">
	      		<img src="/images/filefolder.gif" border=0>
	      	</a>
	    </td>
	</tr>
<%
} else if ("UDSM".equalsIgnoreCase(fascicoloSiusGP.getFascicoloSiusModel().getCodTipoUfficio())) {
%>   
	<tr>
	 	<td class="l">Tribunale di sorveglianza per i Minorenni competente </td>
	 	<td class="l">
	   		<input type="text" Title="Magistrato" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP %>" size=35  >
			<a href="Javascript:ListaComuniEmitTdsMinor('InserisciOrdinanzaMA','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>');">
	     		<img src="/images/filefolder.gif" border=0>
	   		</a>
	 	</td>
	</tr>
<%
} else if (fascicoloSiusGP.getFascicoloSiusModel().getCodTipoUfficio().equalsIgnoreCase("TDS")) {
%>   
	<tr>
	  	<td class="l">Ufficio di sorveglianza Competente </td>
	  	<td class="l">
	    	<input Title="Magistrato" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP %>" value="" size=35 >
			<a href="Javascript:ListaUDS('InserisciOrdinanzaMA','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>');">
	    		<img src="/images/filefolder.gif" border=0>
	    	</a>
	  	</td>
	</tr>
<%
} else if (fascicoloSiusGP.getFascicoloSiusModel().getCodTipoUfficio().equalsIgnoreCase("TDSM")) {
%>
	<tr>
     	<td class="l">Ufficio di Sorveglianza per i Minorenni competente </td>     
		<td class="l">
			<input Title="Magistrato" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP %>" size=35 type="text" onChange="pulisciId();">
  			<a href="Javascript:ListaComuniEmitUdsMinor('InserisciOrdinanzaMA','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>');">
	        	<img src="/images/filefolder.gif" border=0>
	      	</a>
		</td>
	</tr>
<%
}
%>
	<tr>
      	<td class="l">Luogo svolgimento della prova </td>
      	<td class="l">
        	<input Title="Luogo svolgimento della prova" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>" value="" size="35">
      	</td>
    </tr>
	<tr>
      	<td class="l">Servizio terapeutico competente </td>
      	<td class="l">
        	<input Title="Servizio terapeutico competente " name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_SERVIZIO_TERAPEUTICO_COMP%>" value="" size="35">
      	</td>
    </tr>
    <tr>
      	<td class="l" colspan="2">In caso di Differimento Pena/Detenz. Dom. speciale indicare:</td>
    </tr>
    <tr>
      	<td class="l">Data Termine Misura (gg-mm-aaaa)</td>
      	<td class="L">
	        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
	        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
	        <input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      	</td>
    </tr>
    <tr>
   		<td class="l" colspan="2">oppure (solo per Differimento Pena)</td>
	</tr>
	<tr>
		<td class="l">Durata Misura(AA-MM-GG)</td>
		<td class="L">
			<input value="" title="Numero Anni Detenzione" type="text" size="3" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM%>"> -
	        <input value="" title="Numero Mesi Detenzione" type="text" size="3" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM%>"> -
	        <input value="" title="Numero Giorni Detenzione" type="text" size="4" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM%>">
      	</td>
    </tr>
  	<tr><td>&nbsp;</td></tr>
    <tr>
    	<td class="l">Controllo tramite mezzi elettronici 
      		<input value="E" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>" onClick="javascript:updateCkCtrlE()">	
      	</td>
      	<td class="l">Controllo tramite altri strumenti tecnici 
      		<input value="T" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>" onClick="javascript:updateCkCtrlT()">	
      	</td>
    </tr>
  	<tr><td>&nbsp;</td></tr>
	<tr>
    	<td class="l" colspan="2">Inserimento Prescrizioni <input value="06" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_PRESCRIZIONI%>"></td>
  	</tr>
	<%-- MEV10-s3: refactoring del layout della pagina --%>
	<tr><td>&nbsp;</td></tr>
</table>
<table title="tableInForma" id="tableInForma" style="display: none; width: 95%;" cellspacing="2" cellpadding="2">
   		<tr>
   			<td class="l" colspan="3">Indicare se la misura deve essere eseguita nelle forme della</td>
   		</tr>
   		<tr>
   			<td class="l">
   				<input value="1" type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>">
   				Permanenza in casa
   			</td>
   			<td class="l">
   				<input value="2" type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>">
   				Collocamento in Comunità
   			</td>
   			<td class="l">
   				<input value="" type="text" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA%>" size="75">
   			</td>
   		</tr>
</table>
<table cellspacing="2" cellpadding="2" style="width: 95%;">
  	<tr><td>&nbsp;</td></tr>
    <tr>
      	<td>
        	<input class="bottone" type="submit" value="Conferma" >
      	</td>
    </tr>
</table>

<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>">
<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>">
<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%>>
<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP%>">

</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("InserisciOrdinanzaMA");
frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM%>","numeric");
frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM%>","numeric");
frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM%>","numeric");
/*frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_PERMESSO_ACCORDATI%>","numeric"); */
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
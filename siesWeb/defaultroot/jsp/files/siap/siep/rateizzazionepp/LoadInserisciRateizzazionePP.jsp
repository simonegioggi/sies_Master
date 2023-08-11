<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.siep.rateizzazionepp.model.RateizzazionePPModel"%>
<%@ page import="siap.siep.rateizzazionepp.action.ICostantiRateizzazionePP"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>

<jsp:useBean id="modalita"           		scope="request" class="java.lang.String"/>
<jsp:useBean id="listaRateizzazioni" 		scope="request" class="java.util.Vector"/>
<jsp:useBean id="dettaglioPenaComplessiva" 	scope="request" class="siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel"/>
<jsp:useBean id="TornaQui"  				scope="request" class="java.lang.String"/>

<%
PenaComplessivaModel     lPenCom = (dettaglioPenaComplessiva!= null && dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva() != null) ? dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva() : null;
SanzioneSostitutivaModel lSanSos = (dettaglioPenaComplessiva!= null && dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva() != null) ? dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva().getSanzioneSostitutiva() : null;

BigDecimal importoTotale = new BigDecimal(0);

if (lPenCom.getImportoMulta() != null)
    importoTotale = importoTotale.add(lPenCom.getImportoMulta());
if (lPenCom.getImportoAmmenda() != null)
    importoTotale = importoTotale.add(lPenCom.getImportoAmmenda());

if (lSanSos != null && lSanSos.getSanzionePecuniariaMulta() != null)
    importoTotale = importoTotale.add(lSanSos.getSanzionePecuniariaMulta());
if (lSanSos != null && lSanSos.getSanzionePecuniariaAmmenda() != null)
    importoTotale = importoTotale.add(lSanSos.getSanzionePecuniariaAmmenda());

int maxNumRate = ICostantiRateizzazionePP.NUM_MAX_RATE;
String tipoRateizzazione = "U";
int numRateDaModificare = 0;
String importoDaPagareI = "";
String importoDaPagareD = "";

String importoRataUnicaI = "";
String importoRataUnicaD = "";
String scadenzaRataUnica  = "90";
boolean isPresentiBollettini = false;
if ("M".equals(modalita)) {
	RateizzazionePPModel primaRata = (RateizzazionePPModel) listaRateizzazioni.elementAt(0);
	tipoRateizzazione = primaRata.getTipoRateizzazione();
	numRateDaModificare = listaRateizzazioni.size();
	importoDaPagareI = StringUtils.getParteIntera(primaRata.getImportoDaPagare());
	importoDaPagareD = StringUtils.getParteDecimale(primaRata.getImportoDaPagare());
	if ("U".equals(primaRata.getTipoRateizzazione())) {
		importoRataUnicaI = StringUtils.getParteIntera(primaRata.getImportoRata());
		importoRataUnicaD = StringUtils.getParteDecimale(primaRata.getImportoRata()); 
		scadenzaRataUnica = StringUtils.toStringJSP(primaRata.getScadenzaGiorni(), ""); 
	}
	for (int i = 0; i < listaRateizzazioni.size(); i++ ) {
	  	RateizzazionePPModel  lRata = (RateizzazionePPModel)listaRateizzazioni.elementAt(i); 
	  	if (lRata.getListaBollettini() != null && lRata.getListaBollettini().size() > 0)
			isPresentiBollettini = true;
  	}
} else {
  importoDaPagareI = StringUtils.getParteIntera   (importoTotale);
  importoDaPagareD = StringUtils.getParteDecimale (importoTotale);    
}
%>

<%
//==============================================================================
// Form per inserimento e modifica della Rateizzazione Pena Pecuniaria
//==============================================================================
%>
<html>
<head>
<title>[S.I.E.S.] - Rateizzazione Pena Pecuniaria</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
<script language="JavaScript">

var maxNumRate = <%=maxNumRate%>;
var tipoRateizzazione = "<%=tipoRateizzazione%>";
var numRateDaModificare = <%=numRateDaModificare%>;

function Verify() {
<%
if ("M".equals(modalita) && isPresentiBollettini) {
%>
  	var msgConfirm = "La modifica delle rate comportera' la cancellazione dei bollettini gia' emessi. Si vuole procedere?"; 
  	if (!window.confirm(msgConfirm)) {
		return false;
  	}
<%
}
%>
	var importoDaPagare = document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_I%>.value
			+ "." + document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_D%>.value;
  	if (importoDaPagare == "." || parseFloat(importoDaPagare) == "0.0") {
	    alert("Indicare l'importo da pagare");
	    document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_I%>.focus();
	    return false;
  	}
	var sommaRate = 0.0;
	if ($('$<%=ICostantiRateizzazionePP.CAMPO_TIPO_RATEIZZAZIONE%>:checked').val() == "U") {
	    var valoreRata = document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_UNICA_I%>.value
				+ "." + document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_UNICA_D%>.value;
	    if (valoreRata == "." || parseFloat(valoreRata) == "0.0") {
			alert("Indicare l'importo della rata");
			document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_UNICA_I%>.focus();
			return false;
	    }
	    var scadenzaGiorniRu = document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_SCADENZA_GIORNI_RATA_UNICA%>.value;
	    if (isNaN(scadenzaGiorniRu) || scadenzaGiorniRu < 1) {
			alert("Indicare la scadenza pagamento");
			document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_SCADENZA_GIORNI_RATA_UNICA%>.focus();
			return false;
	    }
	    sommaRate = parseFloat(valoreRata);
  	} else if ($('$<%=ICostantiRateizzazionePP.CAMPO_TIPO_RATEIZZAZIONE%>:checked').val() == "R") {
  		for (i = 0; i < maxNumRate; i++) {
      		idrata = "rata_" + i;
      		display = document.getElementById(idrata).style.display;
      		if (display == "block") {
        		Rigo = i;
        		var numRate = document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_NUM_RATE%>_' + Rigo).value;
        		if (isNaN(numRate) || numRate < 1) {
					alert("Indicare il numero di rate per la rata N. " + (Rigo + 1));
					document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_NUM_RATE%>_' + Rigo).focus();
					return false;
        		}
        		var valoreRata = document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I%>_' + Rigo).value
						+ "." + document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D%>_' + Rigo).value;
        		if (valoreRata == "." || parseFloat(valoreRata) == "0.0") {
					alert("Indicare l'importo della rata per la rata N. " + (Rigo + 1));
					document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I%>_' + Rigo).focus();
					return false;
        		}
        		sommaRate = sommaRate + (numRate*parseFloat(valoreRata));
		        if (Rigo == 0) {
	        		var scadenzaGiorni = document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_SCADENZA_GIORNI%>_' + Rigo).value;
	        		if (isNaN(scadenzaGiorni) || scadenzaGiorni < 1) {
						alert("Indicare la scadenza pagamento per la rata N. " + (Rigo + 1));
						document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_SCADENZA_GIORNI%>_' + Rigo).focus();
						return false;
	        		}
        		}
      		}
   		}
  	}
	if (sommaRate < parseFloat(importoDaPagare)) {
		var msg = "Attenzione la somma delle rate da pagare (" + sommaRate + " euro) "
				+ "risulta inferiore al valore indicato come Importo Da Pagare " + parseFloat(importoDaPagare) + " euro. "
	           	+ "Si vuole procedere comunque?";
	  	if (!window.confirm(msg))
			return false;
  	} else if (sommaRate > parseFloat(importoDaPagare)) {
		var msg = "Attenzione la somma delle rate da pagare (" + sommaRate + " euro) "
				+ "risulta superiore al valore indicato come Importo Da Pagare " + parseFloat(importoDaPagare) + " euro. "
               	+ "Si vuole procedere comunque?";
      	if (!window.confirm(msg))
			return false;
  	}
	return true;
}

function inizializza () {
<%
if (modalita.equals("I")) {
%>
	radioTipoPagamento();
<%
} else {
%>
	$("$<%=ICostantiRateizzazionePP.CAMPO_TIPO_RATEIZZAZIONE%>[value='<%=tipoRateizzazione%>']").attr('checked', 'checked');
	if ($('$<%=ICostantiRateizzazionePP.CAMPO_TIPO_RATEIZZAZIONE%>:checked').val() == "R") {
  		var conta = 0;
  		for (i = (maxNumRate - 1); i > 0; i--) {
    		conta++;      
    		if (conta > 15)
    			break;
			idrata = "rata_" + i;
			display = document.getElementById(idrata).style.display;
			if (display == "block") {
				strTdCancella = '<a href="Javascript:cancellaRata(\''+i+'\');"><img src="/images/delete.gif" border="0" title="Cancella rata"></a>';
				document.getElementById('tdCancella_'+i).innerHTML = strTdCancella;
				break;
			}
		} 
	}
	radioTipoPagamento();
<%
}
%>
}

function radioTipoPagamento() {
	if ($('$<%=ICostantiRateizzazionePP.CAMPO_TIPO_RATEIZZAZIONE%>:checked').val() == "U") {
	    document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_UNICA_I%>.disabled = false;
	    document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_UNICA_D%>.disabled = false; 
	    document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_SCADENZA_GIORNI_RATA_UNICA%>.disabled = false;
		// disabilito tutte le righe attive
		var conta = 0;
		for (i = (maxNumRate - 1); i > 0; i--) {
			conta++;
		  	if (conta > 15)
		  		break;      
		  	idrata = "rata_" + i;
		  	display = document.getElementById(idrata).style.display;
		  	if (display == "block") {
		    	cancellaRata(i);
		  	}
		}
		document.getElementById('tabAggiungi').style.display = "none" ;
		document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_NUM_RATE%>_0.disabled = true;    
		document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I%>_0.disabled = true;
		document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D%>_0.disabled = true;
		document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_SCADENZA_GIORNI%>_0.disabled = true;
	} else if ($('$<%=ICostantiRateizzazionePP.CAMPO_TIPO_RATEIZZAZIONE%>:checked').val() == "R") {
	    document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_UNICA_I%>.disabled = true;
	    document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_UNICA_D%>.disabled = true; 
	    document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_SCADENZA_GIORNI_RATA_UNICA%>.disabled = true;
	    document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_NUM_RATE%>_0.disabled = false;    
	    document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I%>_0.disabled = false;
	    document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D%>_0.disabled = false;
	    document.LoadInserisciRateizzazionePP.<%=ICostantiRateizzazionePP.CAMPO_SCADENZA_GIORNI%>_0.disabled = false;    
    	document.getElementById('tabAggiungi').style.display = "block" ;
  	}
}
     
function addUlterioreRata() {
	for (i = 0; i < maxNumRate; i++) {
		idrata = "rata_"+i;
		display = document.getElementById(idrata).style.display;
		if (display == "none") {
			Rigo = i;
			document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_NUM_RATE%>_' + Rigo).disabled = false ;
			document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I%>_' + Rigo).disabled = false ;
			document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D%>_' + Rigo).disabled = false ;
			if (Rigo == 0)
				document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_SCADENZA_GIORNI%>_' + Rigo).disabled = false ;
			strTdCancella = '<a href="Javascript:cancellaRata(\''+Rigo+'\');"><img src="/images/delete.gif" border="0" title="Cancella rata"></a>';
			document.getElementById('tdCancella_' + Rigo).innerHTML = strTdCancella;
			if (Rigo > 1)
			 	document.getElementById('tdCancella_' + (Rigo - 1)).innerHTML = '<br>';
			document.getElementById(idrata).style.display = "block";
			break;
		}
	}
}

function cancellaRata(Rigo) {
	document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_NUM_RATE%>_'+Rigo).value = "" ;
	document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I%>_'+Rigo).value = "" ;
	document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D%>_'+Rigo).value = "" ;
	<%-- document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_SCADENZA_GIORNI%>_'+Rigo).value = "" ; --%>
	document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_NUM_RATE%>_'+Rigo).disabled = true ;
	document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I%>_'+Rigo).disabled = true ;
	document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D%>_'+Rigo).disabled = true ;
	<%-- document.getElementById('<%=ICostantiRateizzazionePP.CAMPO_SCADENZA_GIORNI%>_'+Rigo).disabled = true ; --%>
	if (Rigo > 1) {
    	strTdCancella = '<a href="Javascript:cancellaRata(\'' + (Rigo -1 ) + '\');"><img src="/images/delete.gif" border="0" title="Cancella computo"></a>';
   		document.getElementById('tdCancella_' + (Rigo - 1)).innerHTML = strTdCancella;
    	document.getElementById('tdCancella_' + (Rigo)).innerHTML = '<br>';
  	}
	document.getElementById("rata_" + Rigo).style.display = "none";
}
</script>
</head>

<%
String lAzione = "";
if (modalita.equals("I"))
  	lAzione = "siap.siep.rateizzazionepp.action.ActInserisciRateizzazione";
else 
  	lAzione = "siap.siep.rateizzazionepp.action.ActModificaRateizzazione";
%>

<body class="corpo" onload="inizializza()">
<table>
	<tr>
	  	<td class="LBG">
	    	<a href="Javascript:window.print();">
	      		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
	  		</a>
		</td>
		<td class="LBG">
	  		<font class="label">Funzione :</font>&nbsp;&nbsp;
<%
if (modalita.equals("I")) {
%>
			<font class="campo">Inserimento Modalita' pagamento Pena Pecuniaria</font>
<%
} else {
%>
			<font class="campo">Modifica Modalita' pagamento Pena Pecuniaria</font>
<%
}
%>
		</td>
	</tr>
</table>

<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciRateizzazionePP">
<input type="HIDDEN" name="Action" value="<%=lAzione%>">
<input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">
<table width="50%">
	<tr>
      	<td class="L">
        	<font class="label">Pena Pecuniaria: </font>&nbsp;
<%
if (lPenCom.getImportoMulta() != null && lPenCom.getImportoMulta().compareTo(new BigDecimal(0)) > 0) {
%>
        	<font class="label">MULTA</font> <font class="campo"><%=StringUtils.toEuroFormat((lPenCom != null ? lPenCom.getImportoMulta() : null))%></font>&nbsp;<font class="label">&euro;</font>
<%
}
if (lPenCom.getImportoAmmenda() != null && lPenCom.getImportoAmmenda().compareTo(new BigDecimal(0)) > 0) {
	if (lPenCom.getImportoMulta() != null && lPenCom.getImportoMulta().compareTo(new BigDecimal(0))>0) {
%>
			,&nbsp;
<%
	}
%>
        	<font class="label">AMMENDA</font> <font class="campo"><%=StringUtils.toEuroFormat((lPenCom != null ? lPenCom.getImportoAmmenda() : null))%></font>&nbsp;<font class="label">&euro;</font>  
<%
}
%>
		</td>
    </tr>
<%
if (lSanSos != null && lSanSos.getIdSanzioneSostitutiva() != null
		&& (lSanSos.getCodTipoSanzione().equals("P") || lSanSos.getCodTipoSanzione().equals("Z"))) {
%>
	<tr>
		<td class="L">
			<font class="label">Pena Pecuniaria Sostitutiva: </font>&nbsp;
<%
	if (lSanSos.getSanzionePecuniariaMulta() != null) {
%>
			<font class="label"><% if (lSanSos.isPenaSostitutiva()) {%>IMPORTO<%} else { %>MULTA<% } %></font> <font class="campo"><%=StringUtils.toEuroFormat((lSanSos != null ? lSanSos.getSanzionePecuniariaMulta() : null))%></font>&nbsp;<font class="label">&euro;</font>
<%
	}
	if (lSanSos.getSanzionePecuniariaAmmenda() != null) {
		if (lSanSos.getSanzionePecuniariaMulta() != null) {
%>
			,&nbsp;
<%
		}
%>
			<font class="label">AMMENDA</font> <font class="campo"><%=StringUtils.toEuroFormat((lSanSos != null ? lSanSos.getSanzionePecuniariaAmmenda() : null))%></font>&nbsp;<font class="label">&euro;</font>  
<%
	}
%>
		</td>
	</tr>
<%
}
%>
</table>
<br>
<table width="90%">
	<tr>
		<td class="L">
	   		<font class="label">Importo da pagare</font>&nbsp;
	    	<input type="text" title="Importo Intero" maxlength="10" size="10" style="text-align:right;"
				name="<%=ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_I%>"
				value="<%=importoDaPagareI%>" onkeypress="return TicTabNumField(this,event)">
			,
			<input type="text" title="Importo Decimale" maxlength="2" size="2"
	       		name="<%=ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_D%>"
	       		value="<%=importoDaPagareD%>" onkeypress="return TicTabNumField(this,event)" > &nbsp;&euro;
		</td>
	</tr>
</table>
<%
if (isPresentiBollettini) {
%>
<br>
<table width="90%">
	<tr>
     	<td class="L"><font style="color:red">Attenzione. Sono gia stati emessi dei bollettini. La modifica comportera' la concellazione degli stessi.</font></td>
    </tr>
</table>
<br>
<%
}
%>
<table width="90%">
    <tr>
      	<td class="Titolo" >Tipo Rateizzazione</td>
	</tr>
    <tr>
       	<td class="c">
	         Unica Soluzione &nbsp;<input type="radio" name="<%=ICostantiRateizzazionePP.CAMPO_TIPO_RATEIZZAZIONE%>" id="<%=ICostantiRateizzazionePP.CAMPO_TIPO_RATEIZZAZIONE%>"
	         value="U" checked  onClick="radioTipoPagamento();">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	         Pagamento Rateizzato  &nbsp; <input type="radio" name="<%=ICostantiRateizzazionePP.CAMPO_TIPO_RATEIZZAZIONE%>" id="<%=ICostantiRateizzazionePP.CAMPO_TIPO_RATEIZZAZIONE%>"
	         value="R"  onClick="radioTipoPagamento();">
       	</td>
    </tr>
</table>
<br>
<table width="90%">
    <tr>
      	<td class="Titolo" colspan="4">Pagamento Unica Soluzione</td>
    </tr>
    <tr>
      	<td class="L" >
        	<font class="label">Importo</font>
      	</td>
      	<td class="l">
	        <input type="text" title="Importo Intero" maxlength="10" size="10" style="text-align:right;"
	               name="<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_UNICA_I%>" 
	               value="<%=importoRataUnicaI%>" onkeypress="return TicTabNumField(this,event)">
	        ,
	        <input type="text" title="Importo Decimale" maxlength="2" size="2"
	               name="<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_UNICA_D%>"
	               value="<%=importoRataUnicaD%>" onkeypress="return TicTabNumField(this,event)" > &nbsp;&euro;
      	</td>
      	<td class="L">
	        <font class="label">termine di pagamento fissato entro</font>&nbsp;
	        <input type="text" title="giorni" maxlength="4" size="4" readonly="readonly"
	               name="<%=ICostantiRateizzazionePP.CAMPO_SCADENZA_GIORNI_RATA_UNICA%>" 
	               value="<%=scadenzaRataUnica%>" onkeypress="return TicTabNumField(this,event)">&nbsp;
	        giorni dalla notifica dell'avviso di pagamento        
		</td>
	</tr>
</table>
<br>  
<table width="90%">
	<tr>
		<td class="Titolo" colspan="3">Pagamento Rateizzato</td>
    </tr>
<%
for (int i = 0; i < maxNumRate; i++) {
	String nRate = "";
	String importoI = "";
	String importoD = "";
	String scadenza = "30";
	String disabled = "";
	if (i > 0)
		disabled = "disabled";
	String display = "";
	if (i == 0)
		display = "block";
	if (i > 0)
		display = "none";
	if (tipoRateizzazione.equals("R")) {
		if (i < numRateDaModificare) {
			RateizzazionePPModel rata = (RateizzazionePPModel) listaRateizzazioni.elementAt(i);
		    nRate    = StringUtils.toStringJSP(rata.getNumeroRate(),"");
		    importoI = StringUtils.getParteIntera   (rata.getImportoRata());
		    importoD = StringUtils.getParteDecimale (rata.getImportoRata());
		    scadenza = StringUtils.toStringJSP(rata.getScadenzaGiorni(), "");
		    disabled = "";
		    display = "block";
	    }
	}
%>
	<tr style="display:<%=display%>" id="rata_<%=i%>">
		<td class="L">
	        <font class="label">N.ro rate</font>
	        <input type="text" title="Numero rate" maxlength="2" size="10" <%=disabled%>
	               name="<%=ICostantiRateizzazionePP.CAMPO_NUM_RATE%>_<%=i%>" 
	               id="<%=ICostantiRateizzazionePP.CAMPO_NUM_RATE%>_<%=i%>"
	               value="<%=StringUtils.toStringJSP(nRate,"")%>" onkeypress="return TicTabNumField(this,event)">      
      	</td>
      	<td class="L">
	        <font class="label">Importo ciascuna rata</font>
	        <input type="text" title="Importo Intero" maxlength="10" size="10" <%=disabled%> style="text-align:right;"
	               name="<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I%>_<%=i%>" 
	               id="<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I%>_<%=i%>" 
	               value="<%=StringUtils.toStringJSP(importoI,"")%>" onkeypress="return TicTabNumField(this,event)">
	        ,
	        <input type="text" title="Importo Decimale" maxlength="2" size="2" <%=disabled%>
	               name="<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D%>_<%=i%>"
	               id="<%=ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D%>_<%=i%>"
	               value="<%=StringUtils.toStringJSP(importoD,"")%>" onkeypress="return TicTabNumField(this,event)">      
		</td>
<%
	if (i == 0) {
%>
		<td class="L" nowrap>
	        <font class="label">termine di pagamento della prima rata fissato entro</font>
	          <input type="text" title="giorni" maxlength="4" size="4" <%=disabled%>
	                 name="<%=ICostantiRateizzazionePP.CAMPO_SCADENZA_GIORNI%>_<%=i%>"
	                 id="<%=ICostantiRateizzazionePP.CAMPO_SCADENZA_GIORNI%>_<%=i%>"
	                 value="<%=StringUtils.toStringJSP(scadenza,"")%>" readonly="readonly"
	                 onkeypress="return TicTabNumField(this,event)">
	          giorni dalla notifica dell'avviso di pagamento
		</td>
<%
	} else {
%>
		<td valign="middle" class="c" id="tdCancella_<%=i%>" width="15px">&nbsp;</td>
<%
	}
%>
	</tr>
<%
}
%>
</table>
<table width="90%" id="tabAggiungi">
	<tr>
		<td class="l" colspan="100%">
        	<a href="Javascript:addUlterioreRata();">Aggiungi ulteriore rata</a>
      	</td>
	</tr>
</table>
<br> 
<table cellspacing="2" cellpadding="2" width="90%">
	<tr>
      	<td colspan="2">
        	<input class="bottone" type="submit" name="INSERISCI" value="Conferma">
      	</td>
    </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadInserisciRateizzazionePP");
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
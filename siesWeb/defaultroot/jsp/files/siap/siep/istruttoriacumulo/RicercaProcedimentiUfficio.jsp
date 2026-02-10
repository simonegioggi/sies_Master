<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.lang.Integer" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>

<jsp:useBean id="TornaQui" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="ListaProcedimenti" 		scope="request" class="java.util.Vector"/>
<jsp:useBean id="IstruttoriaCumulo" 		scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="lsoggetto" 				scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="StatoNaschita" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="ListaTitoliInIstruttoria"  scope="request" class="java.util.Vector"/>
 
<%
//==============================================================================
// Jsp per la visualizzazione delle richieste atti ricevute
//==============================================================================
int TotaleIscrivibili=0;
Iterator itx1 = ListaProcedimenti.iterator();
while (itx1.hasNext()) {
	FascicoloSiepModel lProc = (FascicoloSiepModel) itx1.next();
	if (!"SI".equals(lProc.getgiaInIstruttoria()))
		TotaleIscrivibili ++;
}
// 26/04/2019 MEV70 Controllo eventuale presenza del titolo esecutivo legato al Procedimento da Importare tra i Procedimenti già in istrutttoria.  
String stessoTitolo = ""; 
Vector<TitoloCumulatoModel> VecTitCum = new Vector(ListaTitoliInIstruttoria);
String[] aTitoli = new String[ListaProcedimenti.size()]; 
Integer aNumCheck = ListaProcedimenti.size();
for (int k = 0; k < ListaProcedimenti.size(); k++) {
	FascicoloSiepModel lfascicolo = (FascicoloSiepModel) ListaProcedimenti.get(k);
	SentenzaModel lsentenza = lfascicolo.getSentenza();
	if (lfascicolo.getgiaInIstruttoria().compareTo("SI") != 0) {
		if (VecTitCum != null) {
			for (int ii = 0; ii < VecTitCum.size(); ii++) {
				TitoloCumulatoModel lTitoloCumModel = (TitoloCumulatoModel) VecTitCum.elementAt(ii);
	        	if (lsentenza != null && lTitoloCumModel.isStessoTitolo(lsentenza)) {
	        		// Ticket#202602100127 - si gestisce il caso di lTitoloCumModel.getProcedimentoCumulato()==null
	        		//                       che andava in null pointer
	        		if (lTitoloCumModel.getProcedimentoCumulato()!=null) { 
	             	    stessoTitolo += " " + lTitoloCumModel.getProcedimentoCumulato().getChiaveAnnoFasCumulato() + "/" + lTitoloCumModel.getProcedimentoCumulato().getChiaveProgrFasCumulato() + " ";
	             	    aTitoli[k] = lTitoloCumModel.getProcedimentoCumulato().getChiaveAnnoFasCumulato() + "/" + lTitoloCumModel.getProcedimentoCumulato().getChiaveProgrFasCumulato();
	        		} else {
	        			stessoTitolo += " n.d./n.d. ";
	        			aTitoli[k] = "n.d./n.d.";	             		
	        		}
	        		break;
	        		// Ticket#202602100127
	        	} else {
	             	aTitoli[k] = "";
	        	}
			}
		}
	} else {
	 	aTitoli[k] = "p";
	 	aNumCheck--; // 14/06/2019 MEV70 I fascicoli selezionabili (con presenza del checkBox) non sono già presenti
	}
}
%>
<html>
<head>
<title>[S.I.E.S.] - </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
<script language="JavaScript">
window.focus();
var desktop;
function ListaComuni(a_formname,a_fieldname) {
	desktop = window.open("/jsp/Main.jsp?Action=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

// Torna indietro su ElencoFascicoli coinvolti
function eseguiFunzione(action) {
	document.f.<%=IWebConstants.ACTION_FIELD%>.value = action;
	document.f.submit();
}

// azzeramento dei campi Ricerca
function pulisciCogno() {
	document.f.<%=ICostantiSoggetto.CAMPO_COGNOME%>.value = "";
}

function pulisciNome() {
	document.f.<%=ICostantiSoggetto.CAMPO_NOME%>.value = "";
}

function pulisciCui() {
	document.f.<%=ICostantiSoggetto.CAMPO_COD_AFIS%>.value = "";
}

function pulisciData() {
	document.f.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value= "";
   	document.f.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value= "";
   	document.f.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value= "";
}

function pulisciComune() {
	document.f.<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>.value = "";
}

function pulisciStato() {
	document.f.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA %>.value = "";
}

// Funzione utile per impostare la data corrente. ?????
function impostaDataOdierna(campo_giorno, campo_mese, campo_anno, dataOdierna) {
	day = dataOdierna.substring(0,2);
	month = dataOdierna.substring(3,5);
	year = dataOdierna.substring(6,10);
	document.getElementsByName(campo_giorno).item(0).value = day;
	document.getElementsByName(campo_mese).item(0).value = month;
	document.getElementsByName(campo_anno).item(0).value = year;      
}

function espandi(idTabella) {
	var collapseGif = "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
  	var hrefNew = "Javascript:collassa('"+idTabella+"');";
	$('#idHrefRicerca').attr('href',hrefNew);
  	$('#idHrefRicerca').children().attr('src',collapseGif);
  	var tabella = $('#' + idTabella).fadeIn();
}

function collassa(idTabella) {
	var collapseGif = "<%=IWebConstants.IMAGES_DIR%>expand.gif";
  	var hrefNew = "Javascript:espandi('" + idTabella + "');";
	$('#idHrefRicerca').attr('href',hrefNew);
	$('#idHrefRicerca').children().attr('src',collapseGif);
  	var tabella = $('#' + idTabella).fadeOut();
}

function Verify() {
	if (document.f.<%=ICostantiSoggetto.CAMPO_COGNOME%>.value == ""
			&& document.f.<%=ICostantiSoggetto.CAMPO_COD_AFIS%>.value == ""
			&& document.f.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value == "") {
		alert('Per impostare la Ricerca inserire almeno uno tra: \nCognome, Codice CUI, Data Nascita');      
     	document.f.<%=ICostantiSoggetto.CAMPO_COGNOME%>.focus();
     	return false;
 	}
	var dataOdierna = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
	// Data Nascita
 	var data_nasc = document.f.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value
		+ '/' + document.f.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value
		+ '/' + document.f.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
 	if (!ControllaDataPassaVuota(data_nasc)) {
		alert('Data Nascita non corretta');      
		document.f.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.focus();
		return false;
   	}
   	// Comune e Stato Nascita
	if (document.f.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>.value != "" && document.f.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>.value != "-") {
   		if (document.f.<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>.value != ""
   				&& document.f.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>[document.f.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value != '039') {
			alert('Il campo Stato Nascita e comune nascita incongruenti');
			return false;
		}
	}	
    // Ripulisco la lista
    $('#divRisultatoRicerca').hide();
    return true;
} // Chiude Verify()
      
<%-- Ticket#20220127012 - Funzione riscritta --%>
function IscrizioneinIstru() {
// 29/04/2019  MEV70 Elaborazione dell'Array contrenente i riferimenti ad eventuali titoli giàpresenti in Istruttoria.
// - Se un procedimento e già presente in Istruttoria il relativo elemento nell'Array è contrassegnato con "p"; 
// - Se un procedimento non e presente ma afferisce a un Titolo già presente in Istruttoria, il relativo elemento nell'Array è contrassegnato con "Anno/Numero procedimento"; 
// - Se un procedimento e il relativo Titolo Esecutivo non sono presenti in Istruttoria, il relativo elemento nell'Array è contrassegnato con ""; 
<%
StringBuffer titoliSB1 = new StringBuffer();
for (int i = 0; i < aTitoli.length; ++i) {
	if (titoliSB1.length() > 0) {
    	titoliSB1.append(',');
  	}
    titoliSB1.append('"').append(aTitoli[i]).append('"');
}
%>
	var titoliJS = [ <%= titoliSB1.toString() %> ];
	var aNumCheckBox = <%=aNumCheck%>;   	 
	// MAC 20200110018 - 20200122 - MG: errore NON segnalato da utente ma rilevato durante l'esecuzione dei test 
	//in fase di valorizzazione della variabile aStessoTitolo va tenuto conto della dimensione della lista
	//dei procedimenti iscrivibili ( sizelista== 1 oppure sizelista > 1)
	var sizelista = <%=TotaleIscrivibili%>;	
	// Scrorro la lista dei titoli in tabella:
	// verificao che almeno un titolo sia stato selezionato
	// verifico tra i selezionati quali titoli sono già in istruttoria
	// verifica tra i selezionati se esistono titoli non caricato da NSC
	var NSC = "SI";
	var aStessoTitolo = '';
	var contaSelezionati = 0;
	for (var j = 0; j < <%=aTitoli.length%>; j++) {
<%
// Ticket#20251104017: casistica di un solo elemento in lista
if (aTitoli.length == 1) {
%>
		if (document.f.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.checked)
<%
} else {
%>
		if (document.f.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>[j].checked)
<%
}
%>
		{
			contaSelezionati++;
            // Testo se in istruttoria
            if (titoliJS[j] !="p" // selezionabile
					&& titoliJS[j] != "") { // titolo già in iestruttoria
                aStessoTitolo+= titoliJS[j]+' ';
            }
// Testo se iscritto a NSC
<%
if (aTitoli.length == 1) {
%>
			if (document.f.<%=ICostantiFascicoloSiep.CAMPO_KEY_PROVV_NSC%>.value != "SI")
<%
} else {
%>
            if (document.f.<%=ICostantiFascicoloSiep.CAMPO_KEY_PROVV_NSC%>[j].value != "SI")
<%
}
%>
            {
				NSC = "NO";
            }
		}
	}
    if (sizelista == 0) {
		alert("Nessun Fascicolo / Titolo da Inserire in Istruttoria");
		document.f.<%=ICostantiSoggetto.CAMPO_COGNOME%>.focus();
		return false;	 
	} else if (contaSelezionati == 0) {
		alert("Spuntare la checkBox del relativo Fascicolo / Titolo da Inserire in Istruttoria");
		return false;          
	}      
var msgConfirm="";
var esegui = true;
if (aStessoTitolo.length > 0 )
{
  msgConfirm = "Attenzione! Già è presente in Istruttoria Cumulo \nil Procedimento "+aStessoTitolo+" con estremi del Titolo Esecutivo\n";
  msgConfirm += "uguali a quelli di un procedimento che si sta per iscrivere in istruttoria.";
  msgConfirm += "\nSi vuole procedere all'iscrizione del/dei Titolo/i selezionato/i?"; 
  
  esegui = window.confirm(msgConfirm);
} 

if (esegui) 
{
  if (NSC == "NO")
  {
    msgConfirm = "Attenzione! Almeno uno dei Procedimenti selezionati non risulta ancora trasmesso a NSC.\n";
    msgConfirm += "Per procedere all'iscrizione in Instruttoria, è consigliabile prima affettuare lo scarico su NSC\n";
    msgConfirm += "\nSi vuole procedere all'iscrizione del/dei Titolo/i selezionato?"; 

    esegui = window.confirm(msgConfirm);
  }	 
} else {
  msgConfirm="";
}

if (esegui && msgConfirm=="") {
  msgConfirm = "\nSi vuole procedere all'iscrizione del/dei Titolo/i selezionato?"; 
  esegui = window.confirm(msgConfirm);
}

if ( esegui ) 
{
  lAzione = "siap.siep.istruttoriacumulo.action.ActInserisciFascicoloProprioUfficioInIstruttoria";
  document.f.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
    document.f.submit();
    document.f.AGGIUNGI.disabled=true;
    document.body.style.cursor='wait';
  }
}
<%-- Ticket#20220127012 - FINE --%>
<%-- Ticket#20220127012 - Funzione rivista --%>
function CtrStato(checkObject) {
	alert("Attenzione: Il Procedimento selezionato NON è mai stato Validato.\nPer procedere all'iscrizione in Instruttoria è necessario prima Validarlo");
	checkObject.checked = false;
}
<%-- Ticket#20220127012 - FINE --%>
</script>
</head>
  
<body class="corpo" style="margin-top: 0px; margin-left: 0px;">
<table>
  	<tr>
    	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
    	<td class="LBG">
      		<font class="label">Funzione : </font><font class="campo">Ricerca Procedimenti per Titolo e Soggetto</font>&nbsp;&nbsp;
    	</td>
    	<td class="LBG">
        	<a href="javascript:eseguiFunzione('siap.siep.istruttoriacumulo.action.ActLoadElencoFascicoliCoinvolti')">
          		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        	</a>
      	</td>
  	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
<br>
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='f'>
<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoriacumulo.action.ActRicercaPropriProcedimenti">
<input type="hidden" name="<%=IWebConstants.NUM_PAGE%>" value="1">
<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
<table cellspacing="2" cellpadding="2" width="100%">
    <tr>
      	<td class="Titolo" colspan="6"> Criteri di ricerca 
        	<a id="idHrefRicerca" href="Javascript:collassa('tabCriteriRicerca');"><img align="middle" alt="Espandi" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif" border="0"/></a>
      	</td>
	</tr>
</table>
<table id="tabCriteriRicerca" cellspacing="2" cellpadding="2" width="100%" >
	<tr>
      	<td class="l">Cognome</td>
      	<td class="l" colspan="1">
        	<input type="text" title="Cognome" maxlength="35" size="35" name="<%=ICostantiSoggetto.CAMPO_COGNOME%>" value="<%=StringUtils.toStringJSP(lsoggetto.getCognome(),"")%>">
       		<a href="Javascript:pulisciCogno();">
		 		<img src="/images/delete.gif" border="0">
		 	</a>       
      	</td>
      	<td class="l">Nome &nbsp;
        	<input type="text" title="Nome"  maxlength="35" size="35" name="<%=ICostantiSoggetto.CAMPO_NOME%>" value="<%=StringUtils.toStringJSP(lsoggetto.getNome(),"")%>">
        	<a href="Javascript:pulisciNome();">
		 		<img src="/images/delete.gif" border="0">
		 	</a>       
      	</td>
      	<td class="l" colspan="2">Codice CUI &nbsp;
          	<input title="Codice CUI" type="text" name="<%=ICostantiSoggetto.CAMPO_COD_AFIS %>" value="<%=StringUtils.toStringJSP(lsoggetto.getCodAfis(),"")%>" maxlength="7" size="7">
       		<a href="Javascript:pulisciCui();">
		   		<img src="/images/delete.gif" border="0">
		   	</a>	
      	</td>
	</tr>
    <tr>
		<td class="l">Data Nascita</td>
		<td class="l"> 
  			<input type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lsoggetto.getDataNascita(), "dd"), "")%>"
				name="<%= ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>" 
				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
			<input type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lsoggetto.getDataNascita(),"MM" ),"")%>"
				name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>" 
				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
			<input type="text" size="4" maxlength="4" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lsoggetto.getDataNascita(),"yyyy" ),"")%>"
				name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>" 
				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			&nbsp;<a href="Javascript:pulisciData();"><img src="/images/delete.gif" border="0"></a>
		</td>
		<td class="l">Comune di nascita  &nbsp;
  			<input title="Comune di Nascita" value="<%=StringUtils.toStringJSP(lsoggetto.getDescrComuneNascita(),"")%>" type="text" 
				name="<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>" maxlength="30" size="30">
			<a href="Javascript:ListaComuni('f','<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>');"><img src="/images/filefolder.gif" border="0"></a>
   			<a href="Javascript:pulisciComune();"><img src="/images/delete.gif" border="0"></a>
		</td>
		<td class="l">Stato di Nascita</td>
		<td class="L">
  			<select  title="Stato di Nascita" name=<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>><%=StatoNaschita%></select>
       		<a href="Javascript:pulisciStato();"><img src="/images/delete.gif" border="0"></a>
    	</td>
	</tr>
	<tr>
	    <td><input class="bottone" type="submit" name="Ricerca" value="Ricerca"></td>
  	</tr>
</table>
<div id="divRisultatoRicerca">
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<table cellspacing="2" cellpadding="2" width="100%">
	<tr><td class="Titolo" colspan="8">Elenco Procedimenti trovati</td></tr>
    <tr>
		<td class="int">Data Titolo <br> Esecutivo</td>
		<td class="int">Anno e Numero</td>
		<td class="int">Autorità Titolo Esecutivo</td>
		<td class="int">Data Irrevocabilità</td>
		<td class="int">Numero SIEP</td>
		<td class="int">Data di Iscrizione</td>
		<td class="int">Stato </td>
		<td class="int">Iscrivi</td>
	</tr>
<% 
if (ListaProcedimenti == null || ListaProcedimenti.size() == 0) {
%>
	<tr><td class="c" colspan="7"><center>Nessun procedimento trovato con i criteri di ricerca selezionati</center></td></tr>
<%
} else {	 
	String lNsc = "";
	Iterator itx = ListaProcedimenti.iterator();
	while(itx.hasNext()) {
		FascicoloSiepModel lfascicolo = (FascicoloSiepModel) itx.next();
		SentenzaModel lsentenza = lfascicolo.getSentenza();
		lNsc = "";
%>  
	<tr>
		<td class="c">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lsentenza.getDataProvvedimento(), "dd-MM-yyyy"))%>
			<br> <%=StringUtils.toStringJSP(lsentenza.getDescrTipoProvvedimento())%></font>
<%
		if (lfascicolo.getKeyProvvNsc() == null) {
%> 
			&nbsp;<font class=crosso> (*)</font>&nbsp;
<%
		} else {
			lNsc = "SI";
		}
%>				  	 
		</td>
		<td class="c" nowrap ><font class="campo"><%=StringUtils.toStringJSP(lsentenza.getAnnoSentenza())%> / <%=StringUtils.toStringJSP(lsentenza.getNumeroSentenza())%></font></td>
		<td class="c">
			<font class="campo"><%=StringUtils.toStringJSP(lsentenza.getDescrTipoAutoritaEmittente())%></font>
			&nbsp;di&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lsentenza.getDescrLuogoEmittente())%></font>
		</td>		 
		<td class="c"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lfascicolo.getDataIrrevocabilita(),"dd-MM-yyyy"))%></font></td>
		<td class="c" nowrap><font class="campo"><%=StringUtils.toStringJSP(lfascicolo.getChiaveAnno())%> / <%=StringUtils.toStringJSP(lfascicolo.getChiaveProgr())%></font></td>
		<td class="c"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lfascicolo.getDataIscrizione(),"dd-MM-yyyy"))%></font></td>
<%
		if ("02".equals(lfascicolo.getCodStatoFascicolo())) {
%>
		<td class="c"><font class="campo" style="color:red" ><%=StringUtils.toStringJSP(lfascicolo.getDescrStatoFascicolo())%></font></td>
<%
		} else {
%>	      
		<td class="c"><font class="campo"><%=StringUtils.toStringJSP(lfascicolo.getDescrStatoFascicolo())%></font></td>
<%
		}
		if ("SI".equals(lfascicolo.getgiaInIstruttoria())) {
%>
		<td class="c">
			<font style="color:green"><img src="/images/V.gif"></font>
			<font style="font-size: 12">già in <br> istruttoria </font>
          	<%-- Ticket#20220127012 SI aggiunge sempre il campo con IdFascicolo ed NSC per evere la tabella complata per i controlli JS --%>
	        <input type="hidden" disabled name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=lfascicolo.getIdFascicoloSiep()%>" title="Iscrivi in Istruttoria">     	  
          	<input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_KEY_PROVV_NSC %>" value="<%=lNsc%>" > 
          	<%-- Ticket#20220127012 FINE --%>
		</td>
 <%
 		} else {
 %>     	  
		<td class="c">
			<input type="checkbox" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP %>" value="<%=lfascicolo.getIdFascicoloSiep()%>" 
				title="Iscrivi in Istruttoria" 
				<%-- Ticket#20220127012 Modificata la chiamata alla funzione CtrStato onclick1="javascript:CtrStato('<%=lfascicolo.getCodStatoFascicolo()%>');" --%>
<%
			if ("02".equals(lfascicolo.getCodStatoFascicolo())) {
%>
				onclick="javascript:CtrStato(this);" 
<%
			}
%>
				<%-- Ticket#20220127012 - FINE --%>
			>
			<input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_KEY_PROVV_NSC %>" value="<%=lNsc%>" >
		</td>
<%
		}
%>

	</tr>
<%
	} // end while
}
%>
    	<tr>
      	<td>
        	<INPUT class="bottone" type="button" name="AGGIUNGI" value="Iscrivi in istruttoria" onClick="javascript:IscrizioneinIstru();">
      	</td>
      	<td class="l" colspan="7">&nbsp;&nbsp;&nbsp;
      		<font class="label"> N.B.: &nbsp; I Fascicoli segnati con</font>
      		&nbsp;<font class=crosso> (*)</font>&nbsp;
      		<font class="label"> risultano non ancora trasmessi a N.S.C.  &nbsp;</font>
      	</td>	    
	</tr>
</table>
</div> <%-- divRisultatoRicerca --%>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("f");
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
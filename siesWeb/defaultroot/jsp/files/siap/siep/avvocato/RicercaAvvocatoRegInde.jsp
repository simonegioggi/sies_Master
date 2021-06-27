<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_21: aggiunta pagina per chiamata a WS per individuare lista avvocato in ReGIndE --%>

<%@ page import="it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente"%>
<%@ page import="it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Indirizzo"%>
<%@ page import="it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetti"%>
<%@ page import="it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetto"%>

<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel"%>

<jsp:useBean id="avvocato" 	scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="msg"		scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Lista Avvocati ReGIndE</title>
<link rel="STYLESHEET" type="text/css" href="/css/style.css">
  
<script language="JavaScript">
function avvocati() {
	var theFrame = window.parent.document.getElementsByTagName("frame")[1];
	var theFrameDocument = theFrame.contentDocument || theFrame.contentWindow.document;
	var button = theFrameDocument.getElementById("go");
	button.disabled = false;
	var buttonSies = theFrameDocument.getElementById("sies");
	var msg = "<%=msg%>";
	if (msg != "") {
		alert(msg);
		if (msg.indexOf("occorrenze") !== -1) {
			var cf = theFrameDocument.getElementById("cf");
	    	cf.style.display = "block";
		} else {
			buttonSies.style.visibility = "visible";
			buttonSies.disabled = false;
			button.disabled = true;
		}
		return;
	}
	if ("<%=avvocato.size()%>" == 0) {
  		alert("Attenzione! Nessun Difensore trovato in ReGIndE.\nE' possibile effettuare la ricerca del Difensore su SIES!");
  		buttonSies.style.visibility = "visible";
		buttonSies.disabled = false;
		return;
	}
}

function insertIT(id,cognome,nome,foro,indirizzo,telefono,fax,email,pec,codicefiscale,luogoNascita,nazione,giornoNascita,meseNascita,annoNascita,descComuneStudio,stato) {
   	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value = id;
	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value = cognome;
	if (nome == "-") {
 			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_NOME%>.value = "";
	} else {
 			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_NOME%>.value = nome;
	}
	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_FORO%>.value = foro;
	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_INDIRIZZO%>.value = indirizzo;
	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_TELEFONO%>.value = telefono;
	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_FAX%>.value = fax;
	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_E_MAIL%>.value = email;
	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_PEC%>.value = pec;
	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>.value = codicefiscale;
	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.value = nazione;
	if (nazione == "039") {
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value = luogoNascita;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>.value = "";
	} else {
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value = "";
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>.value = luogoNascita;
	}
	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value = giornoNascita;
	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value = meseNascita;
	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>.value = annoNascita;
	<%-- 20210607	MEV Scheda-21  --%>
	<%-- 	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA%>.value = residenza; --%>
	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO%>.value = descComuneStudio;
	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>.value = stato;

   	window.parent.close();
}

function altreInfo(idRecord) {
	var riga = document.getElementById(idRecord);
	if (riga.style.display =="none") {
	  	riga.style.display = "block";
	  	document.images["image_"+idRecord].src = "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
	  	document.images["image_"+idRecord].alt = "Collassa";
	} else {
	  	riga.style.display = "none";
	  	document.images["image_"+idRecord].src = "<%=IWebConstants.IMAGES_DIR%>expand.gif";
	  	document.images["image_"+idRecord].alt = "Espandi";
	}
}
</script>
</head>

<body class="corpo" onload="avvocati();">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
      	<td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Avvocati ReGIndE</font></td>
	</tr>
</table>
  
<form name="f">
<%
if (avvocato.size() > 0 && avvocato.size() < 201) {
%>
<table width="100%">
  	<tr>
		<td class="int">Cognome e Nome</td>
		<td class="int">Codice Fiscale</td>
		<td class="int">Foro</td>
		<td class="int">Luogo e Data Nascita</td>
		<td class="int">Indirizzo Studio</td>
		<td class="int">Stato</td>
    	<td class="int">Seleziona</td>
  	</tr>
<%
	int id_record = 0;
	Iterator iter = avvocato.iterator();
	boolean testAvvocatiValidi = false;
    while (iter.hasNext()) {
    	id_record += 1;
		Soggetto so = (Soggetto) iter.next();
		// sottoinsiemi di Soggetto
		Soggetti si = so.getSoggetto();
		Indirizzo[] i = so.getIndirizzi();
		Ruoloente[] r = so.getRuoliente();
		String foro = "-";
		String stato = "-"; // attivo, radiato, sospeso, cessato
		String codice = "";
		String comune = "";
		String indirizzo = "";
		String fax = "";
		String email = "";
		String telefono = "";
		String indirizzoStudio = "-";
		String nazione = "-";	// 20210614	MEV_21 Recupero codStatoNascita.
		boolean testStato = false;

		if (Utils.isPresent(i)) {
			for (int cnt0 = 0; cnt0 < i.length; cnt0++) {
				if ("D".equalsIgnoreCase(i[cnt0].getTp_indirizzo())) {
					comune = StringUtils.toStringJSP(i[cnt0].getComune());
					indirizzo = StringUtils.toStringJSP(i[cnt0].getIndirizzo());
					fax = StringUtils.toStringJSP(i[cnt0].getFax());
					email = StringUtils.toStringJSP(i[cnt0].getEmail());
					telefono = StringUtils.toStringJSP(i[cnt0].getTelefono());
					indirizzoStudio = indirizzo + " - " + comune;
					break;
				}
			}
		}
		if (Utils.isPresent(r)) {
			for (int cnt1 = 0; cnt1 < r.length; cnt1++) {
				codice = StringUtils.toStringJSP(r[cnt1].getCodice());
				stato = StringUtils.toStringJSP(r[cnt1].getStato());
				if (Utils.isPresent(codice) && codice.contains("COA")) {
					String codComune = r[cnt1].getCodice().substring(3);
					foro = DecodificheUtils.getCodebyCodAlt2(DecodificheManager.getInstance().getForoAll(), codComune);
				}
				if ("attivo".equalsIgnoreCase(stato))
					break;
				else // radiato, sospeso, cessato
					testStato = true;
			}
		}
		Date dn = (Utils.isPresent(si.getDataNascita())) ? si.getDataNascita().getTime() : null;
		if ("-".equals(foro))
			continue;
%>
  	<tr>
  		<!-- Cognome e Nome -->
    	<td class=l><%=StringUtils.toStringJSP(si.getCognome()) + " " +  StringUtils.toStringJSP(si.getNome())%>
    		<a href="javascript:altreInfo('rec_<%=id_record%>')">
    			<img name="image_rec_<%=id_record%>" style="vertical-align: middle;" align="middle" src="<%=IWebConstants.IMAGES_DIR%>expand.gif" alt="Espandi" border="0">
    		</a>
    	</td>
    	<!-- Codice Fiscale -->
    	<td class=l><%=StringUtils.toStringJSP(si.getCodFisc())%></td>
<%
		// 20210624	MEV_21 - Recupero Stato di Nascita dal Codice Comune Catastale.
		if ("Z".equals(si.getCodFisc().substring(11,12)) ) {
			nazione = DecodificheUtils.getCodebyCodAlt2(DecodificheManager.getInstance().getNazioni(), si.getCodFisc().substring(11,15) );
			
		} else {
			nazione = "039";
		}
		// 20210626	MEV_21 - Recupero Codice Stato di Servizio dell'Avvocato.
    	String statoAvv = DecodificheUtils.getCodebyDescUpCase(DecodificheManager.getInstance().getListaAttivitaAvvocato(), stato.toUpperCase());
%>    	
    	<!-- Foro -->
<%
		testAvvocatiValidi = true;
    	Collection listaFori = DecodificheManager.getInstance().getForoAll();
    	String statoForo = DecodificheUtils.getCodAltebyCode(listaFori, foro);
		if ("SOPPRESSO".equals(statoForo)) {
%>
		<td class=l><font class="cRosso"><%=StringUtils.toStringJSP(foro)%> (soppresso)</font></td>
<%
		} else {
%>
    	<td class=l><%=StringUtils.toStringJSP(foro)%></td>
<%
		}
%>
		<!-- Luogo e Data Nascita -->
		<td class=l><%=StringUtils.toStringJSP(si.getLuogoNascita())%>,&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(dn,"dd-MM-yyyy"))%></td>
		<!-- Indirizzo Studio -->
    	<td class=l><%=StringUtils.toStringJSP(indirizzoStudio)%></td>
    	<!-- Stato -->
<%
		if (testStato) {
%>
		<td class=l><font class="cRosso"><%=stato%></font></td>
<%
		} else {
%>
    	<td class=l><%=stato%></td>
<%
		}
%>
		<td class=c>
<%
		if (!testStato) {
%>
			<a href="Javascript:insertIT(
				'<%=codice%>','<%=StringUtils.cStrForJS(si.getCognome())%>',
				'<%=StringUtils.cStrForJS(si.getNome())%>','<%=StringUtils.cStrForJS(foro)%>',
				'<%=StringUtils.cStrForJS(indirizzo)%>','<%=StringUtils.cStrForJS(telefono)%>',
				'<%=StringUtils.cStrForJS(fax)%>','<%=StringUtils.cStrForJS(email)%>',
				'<%=StringUtils.cStrForJS(si.getPec())%>','<%=StringUtils.cStrForJS(si.getCodFisc())%>',
				'<%=StringUtils.cStrForJS(si.getLuogoNascita())%>','<%=StringUtils.cStrForJS(nazione)%>',
				'<%=StringUtils.cStrForJS(DateUtils.getDateToString(si.getDataNascita().getTime(),"dd"))%>',
				'<%=StringUtils.cStrForJS(DateUtils.getDateToString(si.getDataNascita().getTime(),"MM"))%>',
				'<%=StringUtils.cStrForJS(DateUtils.getDateToString(si.getDataNascita().getTime(),"yyyy"))%>',
				'<%=StringUtils.cStrForJS(comune)%>','<%=StringUtils.cStrForJS(statoAvv)%>');">
				<img align="middle" src="/images/fileselected.gif" border="0" style="vertical-align: super;" alt="Inserisci">
			</a>
<%
		} else {
%>
			&nbsp;
<%
		}
%>
		</td>
	</tr>

	<tr id="rec_<%=id_record%>" style="display:none;">
		<td class=l colspan="7">
			pec:&nbsp;<%=StringUtils.toStringJSP(si.getPec())%>&nbsp;&nbsp;&nbsp;
			Tel:&nbsp;<%=telefono%>&nbsp;&nbsp;&nbsp;
			Fax:&nbsp;<%=fax%>&nbsp;&nbsp;&nbsp;
			e-mail:&nbsp;<%=email%>
		</td>
	</tr>
<%
  	}
%>
</table>
<%
	if (!testAvvocatiValidi) {
%>
<script>
alert("Attenzione! Nessun Difensore trovato in ReGIndE.\nE' possibile effettuare la ricerca del Difensore su SIES!");
buttonSies.style.visibility = "visible";
buttonSies.disabled = false;
button.disabled = true;
</script>
<%
    }
}
%>
</form>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.jms.ICostantiJMS"%>

<%@ page import="siap.siep.penapecuniaria.model.RichiestaConversioneModel"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>

<jsp:useBean id="Messaggio" 			scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="soggetto"  			scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="fascicolo" 			scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="evento"    			scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<%-- 2010-04-30 - Modifica per NuovaIstanza --%>
<jsp:useBean id="nuovaistanza"          scope="request" class="siap.siep.nuovaistanza.model.NuovaIstanzaModel"/>
<jsp:useBean id="sanzione_sostitutiva"  scope="request" class="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"/>
<jsp:useBean id="sanzione_residua"      scope="request" class="siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel"/>
<jsp:useBean id="richiesteConversioni"  scope="request" class="java.util.Vector"/>
<%-- Trasferimento x richiesta pericolosità sociale misure di sicurezza --%>
<jsp:useBean id="misureSicurezza"  		scope="request" class="java.util.ArrayList"/>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Atti Siep</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/conferma.js"></script>
</head>
<body class="corpo">
<FORM name="comandi">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
		<td class="LBG">
			<font class="label">Funzione :</font>&nbsp;
<%
String strTitle = "";
String strObject = "";
if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_ISTANZA) == 0) {
	strTitle = "Dettaglio Istanza Ricevuta";
  	strObject = "Oggetto dell'Istanza";
} else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_PROVVEDIMENTO) == 0) {
	strTitle = "Dettaglio Provvedimento Ricevuto";
	strObject = "Oggetto del Provvedimento";
} else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_SANZIONE_SOSTITUTIVA) == 0) {
	strTitle = "Dettaglio Sanzione Sostitutiva Ricevuta";
	strObject = "Oggetto della Sanzione";
} 
// MEV_2023-33: aggiunto trasferimento pena sostitutiva
else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_PENA_SOSTITUTIVA) == 0) {
	strTitle = "Dettaglio Pena Sostitutiva Ricevuta";
	strObject = "Oggetto della Pena";
} else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_ATTI_CONVERSIONE) == 0) {
  strTitle = "Dettaglio Richiesta Conversione Pene Pecuniarie Ricevuta";
  strObject = "Oggetto del Provvedimento";
} else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_RICHIESTA_ACCERTA_PERICOLO_SOCIALE) == 0) {
	strTitle = "Dettaglio Richiesta Accertamento Pericolosità Sociale";
	strObject = "Oggetto del Provvedimento";
} else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.RICHIESTA_CESSAZIONE_MISURA) == 0) {
	strTitle = "Dettaglio Richiesta Cessazione Misura Per Sopravvenienza Nuovo Titolo Esecutivo (ex Art. 51 Bis)";
	strObject = "Oggetto del Provvedimento";
} else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.RICHIESTA_PROSECUZIONE_MISURA) == 0) {
	strTitle = "Dettaglio Richiesta Prosecuzione Misura Per Sopravvenienza Nuovo Titolo Esecutivo (ex Art. 51 Bis)";
	strObject = "Oggetto del Provvedimento";
} else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_RICHIESTA_ACCERTA_PERICOLO_SOCIALE) == 0) {
	strTitle = "Dettaglio Richiesta Accertamento Pericolosità Sociale";
	strObject = "Oggetto del Provvedimento";
} else {
	strTitle = "Dettaglio Atto non Previsto";
	strObject = "Oggetto dell'Atto";
}
%>
			<font class="campo"><%=strTitle%></font>
		</td>
	</tr>
</table>
</FORM>
<table cellspacing=2 cellpadding=2>	
<!----------- SOGGETTO --------------------->
	<tr>
		<td class="Titolo" colspan=4><%=strObject%></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Oggetto</font></td>
<%
if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_ISTANZA) == 0) {
%>
		<td colspan="3" class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getDescrContenuto())%>&nbsp;</font></td>
<%
} else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_PROVVEDIMENTO) == 0) {
%>
		<td colspan="3" class="l"><font class="campo"><%=StringUtils.toStringJSP(evento.getEvento().getDescrMotivo())%>&nbsp;</font></td>
<%
} else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_SANZIONE_SOSTITUTIVA) == 0) {
%>
		<td colspan="3" class="l"><font class="campo">Richiesta Applicazione Sanzione Sostitutiva&nbsp;</font></td>
<%-- MEV_2023-33: aggiunto trasferimento pena sostitutiva --%>
<%
} else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_PENA_SOSTITUTIVA) == 0) {
%>
		<td colspan="3" class="l"><font class="campo">Richiesta Applicazione Pena Sostitutiva&nbsp;</font></td>
<%
} else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_RICHIESTA_ACCERTA_PERICOLO_SOCIALE) == 0) {
%>
		<td colspan="3" class="l"><font class="campo"><%=StringUtils.toStringJSP(evento.getEvento().getDescrMotivo())%>&nbsp;</font></td>
<%
} else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.RICHIESTA_CESSAZIONE_MISURA) == 0) {
%>
		<td colspan="3" class="l"><font class="campo"><%=StringUtils.toStringJSP(evento.getEvento().getDescrMotivo())%>&nbsp;</font></td>
<%
} else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.RICHIESTA_PROSECUZIONE_MISURA) == 0) {
%>
		<td colspan="3" class="l"><font class="campo"><%=StringUtils.toStringJSP(evento.getEvento().getDescrMotivo())%>&nbsp;</font></td>
<%
} else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_RICHIESTA_ACCERTA_PERICOLO_SOCIALE) == 0) {
%>
		<td colspan="3" class="l"><font class="campo"><%=StringUtils.toStringJSP(evento.getEvento().getDescrMotivo())%>&nbsp;</font></td>
<%
}
%>
	</tr>
    <tr>
    	<td class="Titolo" colspan=4>Soggetto di riferimento</td>
    </tr>
    <tr>
        <td class="l"><font class="label">Cognome</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCognome())%>&nbsp;</font></td>
        <td class="l"><font class="label">Nome</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getNome())%>&nbsp;</font></td>
	</tr>
	<tr>
        <td class="l"><font class="label">Sesso</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getSesso())%>&nbsp;</font></td>
	</tr>
	<tr>
        <td class="l" width="25%"><font  class="label">Data di nascita</font></td>
        <td class="l" width="25%"><font  class="campo"><%= StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%>&nbsp;</font></td>
        <td class="l" width="25%"><font  class="label">Presunta</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDataNascitaPresunta())%>&nbsp;</font></td>
	</tr>
	<tr>
        <td class="l"><font  class="label">Comune Nascita</font></td>
        <td class="l">
			<font class="campo">
            	<%=StringUtils.toStringJSP(soggetto.getDescrComuneNascita())%>
<%
if (soggetto.getDescrComuneNascita() != null
		&& !(soggetto.getDescrComuneNascita().equals(""))
		&& !(soggetto.getDescrComuneNascita().equals("-"))) {
%>
				&nbsp;(<%=soggetto.getCodProvinciaNascita()%>)
<%
}
%>
            	&nbsp;
			</font>
		</td>
	</tr>
	<tr>
		<td class="l"><font class="label">Nazionalità</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrNazionalita())%>&nbsp;</font></td>
        <td class="l"><font  class="label">Stato Nascita</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrStatoNascita())%>&nbsp;</font></td>
	</tr>
	<!----------- SENTENZA --------------------->
	<tr><td class="Titolo" colspan=4>Estremi della sentenza</td></tr>
	<tr>
        <td class="l">Anno/Numero</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getSentenza().getAnnoSentenza())%>/<%=StringUtils.toStringJSP(fascicolo.getSentenza().getNumeroSentenza())%></font></td>
	</tr>
	<tr>
        <td class="l">Data</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getSentenza().getDataSentenza(),"dd-MM-yyyy"))%>&nbsp;</font></td>
	</tr>
	<tr>
        <td class="l">Autorita Emittente</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getSentenza().getDescrTipoAutoritaEmittente())%>&nbsp;</font></td>
	</tr>
	<tr>
        <td class="l">Luogo Emittente</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getSentenza().getDescrLuogoEmittente())%>&nbsp;</font></td>
	</tr>
	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	<%--
	// modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
	tr>
	  <td class="l">Data Irrevocabilità</td>
	  <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getSentenza().getDataIrrevocabilita(),"dd-MM-yyyy"), "-" )%>&nbsp;</font></td>
	</tr>
	--%>
	<!----------- FASCICOLO -------------------->
	<tr><td class="Titolo" colspan=4>Procedimento (N.SIEP)</td></tr>
	<tr>
        <td class="L">Anno/Numero</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getChiaveAnno())%>/<%=StringUtils.toStringJSP(fascicolo.getChiaveProgr())%></font></td>
	</tr>
	<!----------- SANZIONE/PENA SOSTITUTIVA DA ESPIARE -------------------->
<%
if ((sanzione_residua != null && ((sanzione_residua.getNumAnni() != null)
		|| (sanzione_residua.getNumMesi() != null)
		|| (sanzione_residua.getNumGiorni() != null)))
		|| (sanzione_sostitutiva != null && ((sanzione_sostitutiva.getNumAnni() != null)
				|| (sanzione_sostitutiva.getNumMesi() != null)
				|| (sanzione_sostitutiva.getNumGiorni() != null)))) {
	String Anni = "", Mesi = "", Giorni = "", FlagReadOnly = "", TipoSanzione="";
	if (sanzione_residua != null && ((sanzione_residua.getNumAnni() != null)
    		|| (sanzione_residua.getNumMesi() != null)
    		|| (sanzione_residua.getNumGiorni() != null))) {
		Anni = StringUtils.toStringJSP(sanzione_residua.getNumAnni(), "0");
		Mesi = StringUtils.toStringJSP(sanzione_residua.getNumMesi(), "0");
		Giorni = StringUtils.toStringJSP(sanzione_residua.getNumGiorni(), "0");
		FlagReadOnly = "readonly";
		TipoSanzione = sanzione_residua.getCodTipoSanzione();
	} else if (sanzione_sostitutiva != null && ((sanzione_sostitutiva.getNumAnni() != null)
			|| (sanzione_sostitutiva.getNumMesi() != null)
			|| (sanzione_sostitutiva.getNumGiorni() != null))) {
		Anni = StringUtils.toStringJSP(sanzione_sostitutiva.getNumAnni(), "0");
		Mesi = StringUtils.toStringJSP(sanzione_sostitutiva.getNumMesi(), "0");
		Giorni = StringUtils.toStringJSP(sanzione_sostitutiva.getNumGiorni(), "0");
		FlagReadOnly = "readonly";
		TipoSanzione = sanzione_sostitutiva.getCodTipoSanzione();
	}
	// MEV_2023-33: aggiunto trasferimento pena sostitutiva (T,V)
	if (TipoSanzione.compareTo("L") == 0)
		TipoSanzione = "Libertà Controllata";
	else if (TipoSanzione.compareTo("S") == 0 )
		TipoSanzione = "Semidetenzione";
	else if (TipoSanzione.compareTo("T") == 0)
 		TipoSanzione = "Semiliberta' sostitutiva";
	else if (TipoSanzione.compareTo("V") == 0 )
 		TipoSanzione = "Detenzione Domiciliare sostitutiva";
	String tipo = ("T".equals(TipoSanzione) || "V".equals(TipoSanzione)) ? "Pena" : "Sanzione";
%>
	<tr><td class="Titolo" colspan=4><%=tipo%> Sostitutiva da espiare: </td></tr>
	<tr>
        <td class="L">Tipo:&nbsp;<font class="campo"><%=TipoSanzione%></font></td>
        <td class="L">Anni:&nbsp;<font class="campo"><%=Anni%></font></td>
        <td class="L">Mesi:&nbsp;<font class="campo"><%=Mesi%></font></td>
        <td class="L">Giorni:&nbsp;<font class="campo"><%=Giorni%></font></td>
	</tr>
<%
}
%>
<!----------- RICHIESTE di CONVERSIONE PENE PECUNIARIE -------------------->
<%
if (richiesteConversioni.size() > 0) {
%>
<table cellspacing="2" cellpadding="2" width="87%" >
	<tr><td>&nbsp;</td></tr>
	<tr><td class="Titolo" colspan=6>Dati Richiesta Conversione</td></tr>
<%
	Iterator itx = richiesteConversioni.iterator();
	while (itx.hasNext()) {
		RichiestaConversioneModel richiestaconversione = (RichiestaConversioneModel) itx.next();
%>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="l">Anno/Numero Partita</td>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getAnnoPartita()) %></font>&nbsp;
			&nbsp;/&nbsp;<font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getNumPartita()) %></font>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="l">Num Ex Campione</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getNumExCampione()) %></font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Prot Circosrizione Doganale</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getProtCircosrizioneDoganale()) %></font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Autorità</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getDescrTipoAutoritaEmittente()) %></font>&nbsp;</td>
	</tr>
	<tr>
	    <td class="l">Sede</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getDescrLuogoEmittente()) %></font>&nbsp;</td>
	</tr>
	<tr>
	    <td class="l">Data Ricezione Atto</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataRicezioneAtto(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
	</tr>
	<tr>
	    <td class="l">Data Iscrizione Atto</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataIscrizioneAtto(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
	</tr>
	<tr>
	    <td class="l">Data Esazione</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataEsazione(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
	</tr>
	<tr>
	    <td class="l">Multa: Importo</td>
	    <td class="l">
	    	<font class="campo"><%=StringUtils.toEuroFormat(richiestaconversione.getImportoMulta()) %></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Data Prescrizione
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataPrescrizioneMulta(),"dd-MM-yyyy"))%> </font>&nbsp;&nbsp;&nbsp;
<%
		if (richiestaconversione.getFlagImprescrittibileMulta().equals("S")) {
%>
		<td class="l">Imprescrittibile</td>
<%
		}
%>
	</tr>
	<tr>
		<td class="l">Ammenda: Importo</td>
		<td class="l">
			<font class="campo"><%=StringUtils.toEuroFormat(richiestaconversione.getImportoAmmenda()) %></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            Data Prescrizione     
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataPrescrizioneAmmenda(),"dd-MM-yyyy"))%> </font>&nbsp;&nbsp;&nbsp;
<%
		if (richiestaconversione.getFlagImprescrittibileAmmenda().equals("S")) {
%>
		<td class="l">Imprescrittibile</td>
<%
		}
%>
	</tr>
<%
	}
%>
</table>
<%
}
//==============================================================================
// Se Richiesta Accertamento pericolosità sociale, visualizzo le Misure SIcurezza
//==============================================================================
if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_RICHIESTA_ACCERTA_PERICOLO_SOCIALE) == 0
		&& misureSicurezza != null && misureSicurezza.size() > 0) {
%>
  	<tr><td class="Titolo" colspan=4>Misure di Sicurezza</td></tr>
<%
	Iterator itxMS = misureSicurezza.iterator();
	while (itxMS.hasNext()) {
		MisuraSicurezzaModel lMisuraSicModel = (MisuraSicurezzaModel) itxMS.next();
%>
	<tr>
		<td class="L">Misura di Sicurezza da espiare</td>
		<td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lMisuraSicModel.getDescrTipo(),"")%>&nbsp;</font></td>
	<td class="l">            
<%
		if (lMisuraSicModel.getNumAnni() != null && lMisuraSicModel.getNumAnni().intValue() > 0) {
%>
		Anni:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lMisuraSicModel.getNumAnni(),"0") %>&nbsp;</font>
<%
		}
		if (lMisuraSicModel.getNumMesi() != null && lMisuraSicModel.getNumMesi().intValue() > 0) {
%>
		Mesi:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lMisuraSicModel.getNumMesi(),"0") %>&nbsp;</font>
<%
		}
		if (lMisuraSicModel.getNumGiorni() != null && lMisuraSicModel.getNumGiorni().intValue() > 0) {
%>
		Giorni:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lMisuraSicModel.getNumGiorni(),"0") %>&nbsp;</font>
<%
		}
%>
		</td>
	</tr> 
<%
	}
}
%>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="ConfermaPresaInCaricoAttiSiep" onsubmit="javascript:document.ConfermaPresaInCaricoAttiSiep.I.disabled=true;">
	<tr>
	    <td>
            <input class=bottone name="I" type="submit" value="Conferma Presa in Carico">
		</td>
	</tr>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.presaincarico.action.ActConfermaPresaInCaricoAttiSiep">
    <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
</form>
</table>
</body>
</html>
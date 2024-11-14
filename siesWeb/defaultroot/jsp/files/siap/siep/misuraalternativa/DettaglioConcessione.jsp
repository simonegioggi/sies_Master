<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="posizioneluogoaltra"	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"         	scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="penaresidua"        	scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="autoritaEsternaE"   	scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="autoritaEsternaC"   	scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="UffTDS"       			scope="request" class="java.lang.String"/>
<jsp:useBean id="UffUDS"       			scope="request" class="java.lang.String"/>
<jsp:useBean id="misuraalternativa"     scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="verbale"      			scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="daticssa"      		scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="flagmisura"      		scope="request" class="java.lang.String"/>
<jsp:useBean id="sedeUfficioEmittente"	scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="eventonotifica" 		scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="lPosGiuModificata" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="NoteCssa"      		scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteAutC"      		scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteTDS"      			scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteAutE"      		scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteUDS"      			scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisura"      		scope="request" class="java.lang.String"/>
<jsp:useBean id="eventoammissioneprovvisoriaaffidamento"	scope="request" class="siap.sico.evento.model.EventoModel"/>
<%-- MEV10-s3: aggiunto useBean --%>
<jsp:useBean id="codiceTipoUfficio" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="descrTipoUfficioTDS" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="descrTipoUfficioUDS" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoUfficioUDS" 	scope="request" class="java.lang.String"/>
<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");

PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

// Instanzia il model dell'EVENTO (per  posizione giuridica "13" motivo "2006" e 
// tipo provvedimento "4" o "9" o "12" quando proviene da ammissione provvisoria))
EventoModel lEventoAmmProvvAff = eventoammissioneprovvisoriaaffidamento;

if (lPosizione == null)
	lPosizione = new PosizioneGiuridicaModel();

if (lLuogoDetenzione == null)
	lLuogoDetenzione = new LuogoDetenzioneModel();

if (lAltraCausa == null)
	lAltraCausa = new AltraCausaModel();
%>
<html>
<head>
<title>[S.I.E.S.] - Dettaglio Concessione</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>
<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
		<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
MisuraAlternativaModel lModel = new MisuraAlternativaModel();
if (tipoMisura.equals("AFFIDAMENTO")) {
%>
			<font class="campo">Dettaglio Concessione Affidamento In Prova</font>
<%
} else if (tipoMisura.equals("DETENZIONE")) {
%>
			<font class="campo">Dettaglio Concessione Detenzione Domiciliare</font>
<%
} else if (tipoMisura.equals("SEMILIBERTA")) {
%>
			<font class="campo">Dettaglio Concessione Semilibert&agrave;</font>
<%
} else if (tipoMisura.equals("INDULTINO")) {
%>
			<font class="campo">Dettaglio Concessione L.207/2003</font>
<%
} else if (tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)) {
%>
			<font class="campo">Espiazione Pena presso Domicilio</font>
<%
}
%>
		</td>
<%
if (((eventonotifica.getEvento().getFlagDocumentoRegistrato() != null
		&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0)
		|| eventonotifica.getEvento().getFlagDocumentoRegistrato() == null)
		// MEV_2019-09-SIEP: aggiunto pulsante di modifica diversificato per tipo misura
		&& (tipoMisura.equals("DETENZIONE")
				|| tipoMisura.equals("SEMILIBERTA")
				|| tipoMisura.equals("AFFIDAMENTO"))) {
	String action = "ActLoadInserisciMAAffidamentoInProva";
	if (tipoMisura.equals("DETENZIONE"))
		action = "ActLoadInserisciMADetenzioneDomiciliare";
	else if (tipoMisura.equals("SEMILIBERTA"))
		action = "ActLoadInserisciMASemiliberta";
%>
     	<!-- BOTTONE DI MODIFICA -->
     	<td class="LBG">
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.<%=action%>
			&<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>=<%=misuraalternativa.getIdMisuraAlternativa()%>&tipoOperazione=MODIFICA
			&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventonotifica.getEvento().getIdEvento()%>">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
			</a>
		</td>
		<!-- BOTTONE DI STAMPA -->
   		<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     		<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActStampaConcessione&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&tipoMisura="+tipoMisura+"&IdEventoAmmProvvAff="+StringUtils.toStringJSP(lEventoAmmProvvAff.getIdEvento())%>"/>
		</jsp:include>
		<%-- MEV_2019-09-SIEP: aggiunto pulsante di validazione diretta --%>
		<!-- BOTTONE DI VALIDAZIONE DIRETTA -->
  		<td class="LBG">
    		<a href="/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActUploadMA&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.misuraalternativa.action.ActDettaglioConcessione&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
      			<img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
    		</a>
  		</td>
<%
// MEV_2019-09-SIEP: stava dentro if (eventonotifica.getEvento().getFlagDocumentoRegistrato() != null) quindi era dead code
// if (eventonotifica.getEvento().getFlagDocumentoRegistrato() == null)
// spostato nel ramo else
} else {
%>
		<!-- BOTTONE DI STAMPA -->
   		<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     		<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActStampaConcessione&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&tipoMisura="+tipoMisura+"&IdEventoAmmProvvAff="+StringUtils.toStringJSP(lEventoAmmProvvAff.getIdEvento())%>"/>
   		</jsp:include>
<%
}
%>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td width="20%">
<%
if (flagmisura.equals("N")) {
%>
			<input type="HIDDEN" name="flagmisura" value="N">
<%
} else {
%>
			<input type="HIDDEN" name="flagmisura" value="S">
<%
}
%>
			<input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=misuraalternativa.getIdMisuraAlternativa()%>">
			<input type="HIDDEN" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>">
			<input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA%>" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>">
		</td>
	</tr>
    <tr>
      	<td class="l">Posizione Giuridica</td>
      	<td class="L" colspan="3">
			<font class="campo">
<%
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
%>
			DETENUTO PER ALTRA CAUSA
<%
} else {
	if (lPosGiuModificata != null && lPosGiuModificata.getIdPosizioneGiuridica() != null) {
%>
			<%=lPosGiuModificata.getDescrPosizioneGiuridica()%>
<%
	} else {
%>
			<%=lPosizione.getDescrPosizioneGiuridica()%>
<%
	}
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
		<td class="L" colspan="3">
			<font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
			&nbsp;di&nbsp;<font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
		</td>
	</tr>
<%
		if (lAltraCausa.getAltroLuogo() != null) {
%>
	<tr>
		<td class="l">Altro Luogo</td>
		<td class="L" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>
		</td>
	</tr>
<%
		}
	}
} else if (lLuogoDetenzione.getIstitutoDetenzione() != null) {
%>
	<tr>
		<td class="l">Detenuto presso </td>
		<td class="L" colspan="3">
			<font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
			&nbsp;di&nbsp;<font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
		</td>
	</tr>
<%
}
// Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
if (lPosizione.getCodPosizioneGiuridica() != null
		&& (lPosizione.getCodPosizioneGiuridica().equals("02")
				|| lPosizione.getCodPosizioneGiuridica().equals("04"))) {
	if (lLuogoDetenzione.getIstitutoDetenzione() != null) {
%>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="L" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>
		</td>
	</tr>
<%
	}
}
if (penaresidua.getDataInizio() != null) {
%>
	<tr>
		<td class="l">Data Decorrenza Pena</td>
		<td class="L" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%></font>
		</td>
 	</tr>
<%
}
if (penaresidua.getFlagErgastolo() != null) {
	if (penaresidua.getFlagErgastolo().equals("S")) {
%>
	<tr>
	  	<td class="l">Pena Detentiva</td>
	  	<td class="L" colspan="3"><font class="campo">ERGASTOLO</font></td>
	</tr>
<%
	} else if (penaresidua.getFlagErgastolo().equals("D")) {
%>
	<tr>
	  	<td class="l">Pena Detentiva</td>
	  	<td class="L" colspan="3"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font></td>
	</tr>
<%
	}
}
%>
	<tr>
<%
if (((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null
		&& !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
		&& penaresidua.getDataFine() != null) {
	if (penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan="3">
			<font class="campo">
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>
			-
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>
			-
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%>
			</font>
		</td>
<%
	} else {
%>
		<td class="l">Data Fine Pena</td>
		<td class="lRosso" colspan="3">
			<font class="lRosso">
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>
			-
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>
			-
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%>
			</font>
		</td>
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
	if (penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0) {
		// do nothing
	} else {
%>
		<td class="l">Reclusione</td>
		<td class="l">
			<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(), "0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(), "0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(), "0")%></font>
		</td>
<%
		if (penaresidua.getImportoMulta().compareTo(new BigDecimal(0)) != 0) {
%>
		<td class="l">Multa</td>
		<td class="l">
			<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font>
		</td>
<%
		}
	}
%>
	</tr>
	<tr>
<%
	if (penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0) {
		// do nothing
	} else {
%>
		<td class="l">Arresto</td>
      	<td class="l">
			<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      	</td>
<%
		if (penaresidua.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0) {
%>
		<td class="l">Ammenda</td>
		<td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
		}
	}
}
%>
	</tr>
<%       
//==============================================================================      
//                          SANZIONE SOSTITUTIVA
//==============================================================================      
if (penaresidua != null && penaresidua.getFlagSanzioneSostitutiva() != null) {
%>
	<tr>
		<td class="l">Sanzione sostitutiva</td>  
		<td class="L" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(penaresidua.getDescrTipoSanzione())%>&nbsp;</font>
			<font class="label">Anni:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniSS(), "0")%>&nbsp;</font>
			<font class="label">Mesi:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiSS(), "0")%>&nbsp;</font>
			<font class="label">Giorni:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniSS(), "0")%></font>
<%
	if ((penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0)
			|| (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0)) {
%>
			<font class="label">&nbsp;Sanz.Pec.&nbsp;</font>
<%
		if (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0) {
%>
			<font class="campo">Multa&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoMultaSS())%>&nbsp;</font>&euro;
<%
		}
		if (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0) {
%>
			<font class="campo">Ammenda&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoAmmendaSS())%>&nbsp;</font>&euro;
<%
		}
	}
%>  
		</td>
    </tr>
<%
}
//==============================================================================      
//FINE SANZIONE SOSTITUTIVA
//==============================================================================      
%>         
	<tr>
<%
if (eventonotifica.getEvento().getDataEmissione() != null) {
%>
		<td class="l">Data Emissione</td>
        <td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy"))%></font>
		</td>
<%
}
if (eventonotifica != null && eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0
		&& eventonotifica.getNotifiche()[0] != null && eventonotifica.getNotifiche()[0].getDataInvio() != null) {
%>
		<td class="l">Data Trasmissione</td>
		<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy"))%></font>
		</td>
<%
}
%>
	</tr>
	<tr>
<%
if (misuraalternativa.getChiaveAnnoFascicoloSius() != null) {
%>
	<td class="l">Anno / Numero SIUS</td>
	<td class="l">
		<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
	</td>
<%
}
if (misuraalternativa.getAnnoRegistro() != null) {
%>
		<td class="l">Anno / Numero Ordinanza</td>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
		</td>
<%
}
%>
	</tr>
	<tr>
    	<td class="l">Ufficio che ha emesso l'Ordinanza</td>
    	<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
<%
String descrTipoUfficio = StringUtils.toStringJSP(sedeUfficioEmittente.getDescrTipoUfficio());
if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio))
		&& "UDSM".equals(sedeUfficioEmittente.getCodTipoUfficio())) {
	descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
}
// MEV_62 [EC] 15/05/2018 - INIZIO
if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio))
		&& "TDSM".equals(sedeUfficioEmittente.getCodTipoUfficio())) {
	descrTipoUfficio = "Tribunale per i minorenni in funzione di Tribunale di Sorveglianza";
}
// MEV_62 [EC] 15/05/2018 - FINE
%>
		<td class="l" colspan="3"> <font class="campo"><%=descrTipoUfficio%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescProvincia())%></font></td>
 	</tr>
 	<tr>
		<td class="l">Oggetto Ordinanza</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font></td>
		<td class="l">Data Emissione Ordinanza</td>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(), "dd-MM-yyyy"))%></font>
		</td>
	</tr>
<%
if (misuraalternativa.getDescrLuogoProva() != null) {
%>
	<tr>
		<td class="l">Luogo della Prova</td>
		<td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrLuogoProva())%></font></td>
	</tr>
<%
}
// MEV_2019-09-SIEP: aggiunte etichette x tre tipo misura
// MEV_2024-092: rimosse le etichette x tre tipo misura
// if ((tipoMisura.equals("AFFIDAMENTO") || tipoMisura.equals("DETENZIONE") || tipoMisura.equals("SEMILIBERTA"))
// 		&&!Utils.isNullObj(misuraalternativa.getAnnoRegistroMaAt())) {
%>
<!-- 	<tr> -->
<!-- 		<td class="l">Anno / Numero Ordinanza Provvisoria</td> -->
<!-- 		<td class="l" colspan="3"> -->
<%-- 			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistroMaAt())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistroMaAt())%></font> --%>
<!-- 		</td> -->
<!-- 	</tr> -->
<!-- 	<tr> -->
<!-- 	  	<td class="l">Data Emissione Ordinanza Provvisoria</td> -->
<!-- 	  	<td class="L" colspan="3"> -->
<%-- 			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisioneMaAt(), "dd-MM-yyyy"))%></font> --%>
<!-- 		</td> -->
<!-- 	</tr> -->
<%
// }
// FINE MEV_2024-092
// FINE MEV_2019-09-SIEP
if (verbale.getDataEmissione() != null) {
%>
	<tr>
		<td class="l">Data Sottoscrizione Verbale Obblighi</td>
        <td class="l" colspan="3">
        	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%></font>
        	<input type="hidden" name="flag9" value="1">
       	</td>
	</tr>
<%
}
%>
	<tr>
<%
	if (misuraalternativa.getDataInizioMisura() != null) {
		// MEV_2019-09-SIEP: aggiunta diversificazione dell'etichetta
		// MEV_2024-092: rimossa diversificazione dell'etichetta
		boolean testDataFineMisura = misuraalternativa.getDataFineMisura() == null;
// 		if (tipoMisura.equals("AFFIDAMENTO")) {
%>
<!-- 		<td class="l">Data Applicazione Provvisoria</td> -->
<%
// 		} else {
%>
		<td class="l">Data Inizio Misura</td>
<%
// 		}
%>
		<td class="l" <%if (testDataFineMisura){%>colspan="3"<%}%>>
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(), "dd-MM-yyyy"))%></font>
		</td>
<%
	}
	if (misuraalternativa.getDataFineMisura() != null) {
%>
		<td class="l">Data Fine Misura</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataFineMisura(), "dd-MM-yyyy"))%></font></td>
<%
	}
%>
	</tr>
<%
	if (misuraalternativa != null && misuraalternativa.getCodTipoUfficioScarcerazione() != null
			&& !misuraalternativa.getCodTipoUfficioScarcerazione().equals("-")
			&& misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV")) {
%>
	<tr>
<%
		// Controllo per posizione giuridica 4
		// CAMBIA LA LABEL
		if (tipoMisura.equals("DETENZIONE") && lPosizione.getCodPosizioneGiuridica().equals("04")) {
%>
		<td class="l">Data Esecuzione</td>
<%
		} else {
%>
		<td class="l">Scarcerato in Data</td>
<%
		} // FINE - Controllo per posizione giuridica 4
%>
		<td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataScarcerazione(),"dd-MM-yyyy"))%></FONT></td>
	</tr>
<%
	} else if (misuraalternativa != null && misuraalternativa.getDataScarcerazione() != null) {
%>
	<tr>
<%
		// Controllo per posizione giuridica 4 - CAMBIA LA LABEL
		if (tipoMisura.equals("DETENZIONE") && lPosizione.getCodPosizioneGiuridica().equals("04")) {
%>
		<td class="l">Da Eseguire in Data</td>
<%
		} else {
%>
		<td class="l">Da Scarcerare in Data</td >
<%
		} // FINE - Controllo per posizione giuridica 4
%>
		<td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataScarcerazione(), "dd-MM-yyyy"))%></FONT></td>
	</tr>
<%
	}
	if (misuraalternativa != null && misuraalternativa.getNote() != null) {
%>
	<tr>
		<td class="l">Note</td>
	    <td class="l" colspan="3"><font class="campo"><%=misuraalternativa.getNote()%></font><td>
	</tr>
<%
	}
	// DATA AMMISSIONE PROVVISORIA A DETENZIONE DOMICILIARE
	if (misuraalternativa != null && lPosizione.getCodPosizioneGiuridica().equals("29") && tipoMisura.equals("DETENZIONE")) {
%>
	<tr>
    	<td class="l">Data Ammissione Provvisoria</td >
    	<td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy"))%></font></td>
	</tr>
<%
  	}
	// DATA AMMISSIONE PROVVISORIA AD AFFIDAMENTO IN PROVA
	if (lEventoAmmProvvAff.getIdEvento() != null && misuraalternativa != null && lPosizione.getCodPosizioneGiuridica().equals("13")
			&& tipoMisura.equals("AFFIDAMENTO")) {
%>
	<tr>
	 	<td class="l">Data Ammissione Provvisoria ad Affidamento in Prova</td >
	 	<td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy"))%></font></td>
	</tr>
<%
	}
	if (misuraalternativa != null && (misuraalternativa.getNumAnniRevocaArresto() != null
			|| misuraalternativa.getNumMesiRevocaArresto() != null
			|| misuraalternativa.getNumGiorniRevocaArresto() != null
			|| misuraalternativa.getNumAnniRevocaReclusione() != null
			|| misuraalternativa.getNumMesiRevocaReclusione() != null
			|| misuraalternativa.getNumGiorniRevocaReclusione() != null)) {
%>
	<tr>
		<td class="l">Pena Residua da espiare</td>
		<td class="L">Arresto:
			&nbsp;Anni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumAnniRevocaArresto())%></font>
			&nbsp;Mesi&nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumMesiRevocaArresto())%></font>
			&nbsp;Giorni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumGiorniRevocaArresto())%></font>
		</td>
		<td class="L" colspan="2">Reclusione:
			&nbsp;Anni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumAnniRevocaReclusione())%></font>
			&nbsp;Mesi&nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumMesiRevocaReclusione())%></font>
			&nbsp;Giorni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumGiorniRevocaReclusione())%></font>
		</td>
	</tr>
<%
	}
  	if (magistrato != null) {
%>
	<tr>
		<td class="l">Magistrato Firmatario</td>
		<td class="L" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome())%></font>
			<font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome())%></font>
		</td>
	</tr>
<%
	}
	int cont = 0;
	if (eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0) {
		while (cont < eventonotifica.getNotifiche().length) {
			if (eventonotifica.getNotifiche()[cont].getCodTipoNotifica().equals("E")
					&& eventonotifica.getNotifiche()[cont].getIstitutoDetenzione() != null) {
%>
	<tr>
	   	<td class="l">Istituto Detenzione</td>
	    <td class="l" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getIstitutoDetenzione().getDescrTipoIstituto())%></font>
			&nbsp;di&nbsp;<font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getIstitutoDetenzione().getDescrComune())%></font>
		</td>
	</tr>
<%
				if (eventonotifica.getNotifiche()[cont].getNote() != null && !eventonotifica.getNotifiche()[cont].getNote().equals("")) {
%>
	<tr>
		<td class="l">Note</td>
		<td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getNote())%></font></td>
	</tr>
<%
				}
			}
			cont++;
		}
	}
	if (autoritaEsternaE != null && autoritaEsternaE.getCodTipoAutorita() != null && autoritaEsternaE.getCodSede() != null) {
%>
	<tr>
<%
		if (misuraalternativa != null && misuraalternativa.getIdMisuraAlternativa() != null
				&& misuraalternativa.getDataInizioMisura() != null) {
%>
		<td class="l">Autorità di Polizia Competente per territorio</td>
<%
 		} else {
%>
		<td class="l">Destinatario per l'esecuzione</td>
<%
		}
%>
    	<td class="L" colspan="3">
	    	<font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaE.getDescrTipoAutorita())%></font>
<%
		if (autoritaEsternaE != null && !autoritaEsternaE.getDescrSede().equals("-")) {
%>
			&nbsp;di&nbsp;<font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaE.getDescrSede())%></font>
<%
		}
%>
		</td>
	</tr>
<%
		if (NoteAutE != null && !NoteAutE.equals("")) {
%>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="l" colspan="3">
			<font class="campo"><%=NoteAutE%></font>
		<td>
	</tr>
<%
		}
	}
	if (daticssa != null && daticssa.getComune() != null && !daticssa.getIndirizzo().equals("")) {
%>
	<%-- MEV10-s3: nuova gestione, invece che stringa fissa inserisco valore dalla combo (x 3 occorrenze) --%>
	<tr>
      	<td class="l"><%=StringUtils.toStringJSP(daticssa.getTipoDesc())%> Competente</td>
		<td class="l" colspan="3">
        	<font class="campo"><%=StringUtils.toStringJSP(daticssa.getComune())%>-<%=StringUtils.toStringJSP(daticssa.getIndirizzo())%></font>
      	</td>
  	</tr>
<%
		if (NoteCssa != null && !NoteCssa.equals("")) {
%>
	<tr>
		<td class="l">Note</td>
		<td class="L" colspan="3"><font class="campo"><%=NoteCssa%></font></td>
	</tr>
<%
		}
	}
	if (UffUDS != null && !UffUDS.equals("")) {
%>
   	<tr>
     	<td class="l">Magistrato Preposto al controllo</td >
<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
<%
		String descTipoUfficioUDS = StringUtils.toStringJSP(descrTipoUfficioUDS);
		if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio))
				&& "UDSM".equals(StringUtils.toStringJSP(codTipoUfficioUDS))) {
			descTipoUfficioUDS = "Magistrato di Sorveglianza per i Minorenni";
		}
%>
     	<td class="L" colspan="3">
      		<font class="campo"><%=descTipoUfficioUDS%></font>&nbsp;di&nbsp;<font class="campo"><%=StringUtils.toStringJSP(UffUDS)%></font>
      	</td>
    </tr>
<%
		if (NoteUDS != null && !NoteUDS.equals("")) {
%>
	<tr>
		<td class="l">Note</td>
		<td class="L" colspan="3"><font class="campo"><%=NoteUDS%></font><td>
	</tr>
<%
		}
	}
	if (UffTDS != null && !UffTDS.equals("")) {
%>
   	<tr>
     	<td class="l">TDS che ha emesso l'ordinanza</td >
     	<td class="L" colspan="3">
      		<font class="campo"><%=StringUtils.toStringJSP(descrTipoUfficioTDS)%></font>&nbsp;di&nbsp;<font class="campo"><%=StringUtils.toStringJSP(UffTDS)%></font>
      	</td>
	</tr>
<%
		if (NoteTDS != null && !NoteTDS.equals("")) {
%>
	<tr>
	 	<td class="l">Note</td>
	   	<td  class="L" colspan="3">
	      	<font class="campo"><%=NoteTDS%></font>
	   	</td>
	</tr>
<%
		}
	}
	if (autoritaEsternaC != null && autoritaEsternaC.getCodTipoAutorita()!= null && autoritaEsternaC.getCodSede()!= null) {
%>
	<tr>
	    <td class="l">Autorità di Polizia Competente per territorio</td>
	    <td class="L" colspan="3">
      		<font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaC.getDescrTipoAutorita())%></font>
<%
		if (autoritaEsternaC != null && !autoritaEsternaC.getDescrSede().equals("-")) {
%>
			&nbsp;di&nbsp;<font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaC.getDescrSede())%>
     		</font>
<%
		}
%>
		</td>
 	</tr>
<%
		if (NoteAutC != null && !NoteAutC.equals("")) {
%>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="L" colspan="3"><font class="campo"><%=NoteAutC%></font></td>
	</tr>
<%
		}
	}
	int count = 0;
	if (eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0) {
		while (count < eventonotifica.getNotifiche().length) {
			NotificaModel lNotMod = eventonotifica.getNotifiche()[count];
 			if (lNotMod.getCodTipoNotifica().equals("N") && lNotMod.getAutoritaEsterna() != null
 					&& lNotMod.getAvvIdAvvocatoFascicoloSiep() != null) {
				AvvocatoSiepModel lAvvMod = eventonotifica.getNotifiche()[count].getAvvSiep();
				AutoritaEsternaModel lAuMod = eventonotifica.getNotifiche()[count].getAutoritaEsterna();
%>
	<tr>
	  	<td class="l">Notifica per difensore</td>
	  	<td class="L" colspan="3">
		   	<font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome()) + " " + StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>
			&nbsp;Foro di&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getForo())%></font>
			&nbsp;Difensore di&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getDescrTipo())%></font>
	    </td>
	</tr>
	<tr>
	  	<td class="l">Autorità Notifica</td>
		<td class="L" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrTipoAutorita() )%></font>
	 		&nbsp;di&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>
		</td>
	</tr>
<%
				if (lNotMod.getNote() != null && !lNotMod.getNote().equals("")) {
%>
	<tr>
	 	<td class="l">Note</td>
	 	<td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font></td>
	</tr>
<%
				}
			}
			count++;
		}
	}
%>
</table>
<br>
<div align="left" style="visibility: hidden" id="upld">
<FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
<table>
<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>"/>
	<tr>
		<td class="L">
			<input class="bottone" type="submit" value="Conferma">
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActUploadMA">
			<input type="HIDDEN" name="tipoMisura" value="<%=tipoMisura%>">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>">
			<input type="HIDDEN" name="IdPosizioneGiuridica" value="<%=lPosizione.getIdPosizioneGiuridica()%>">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misuraalternativa.action.ActDettaglioConcessione">
			<input type="HIDDEN" name="IdEventoAmmProvvAff" value="<%=StringUtils.toStringJSP(lEventoAmmProvvAff.getIdEvento())%>">
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>
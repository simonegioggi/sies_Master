<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-33: aggiunta pagina per Gestione Trasmissione Atti per l'Esecuzione (pena sostitutiva) --%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="magistrato"         	scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="evento" 				scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"        	scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="penaCompPenaSost"     	scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"/>
<jsp:useBean id="misurecautelari"     	scope="request" class="java.util.Vector"/>
<jsp:useBean id="residenzaassociata"  	scope="request" class="siap.sico.residenza.model.ResidenzaAssociataModel"/>
<jsp:useBean id="notificaUDS" 			scope="request" class="siap.siep.notifica.model.NotificaModel"/>

<%
EventoNotificaModel enm = evento;
FascicoloSiepModel fsm = (FascicoloSiepModel) session.getAttribute("fascicolo");
PosizioneGiuridicaModel pgm = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel ldm = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel acm = posizioneluogoaltra.getAltraCausa();
if (Utils.isNullObj(pgm))
	pgm = new PosizioneGiuridicaModel();
if (Utils.isNullObj(ldm))
	ldm = new LuogoDetenzioneModel();
if (Utils.isNullObj(acm))
	acm = new AltraCausaModel();
PenaComplessivaSanzioneSostitutivaModel pcssm = penaCompPenaSost;
%>

<html>
<head>
<title> [S.I.E.S.] - Dettaglio Trasmissione Atti Esecuzione Pena Sostituiva </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
<script language="JavaScript">
function conferma(azione) {
	document.comandi.<%=IWebConstants.ACTION_FIELD%>.value = azione;
	document.comandi.submit();
}
</script>
</head>
<BODY class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
		<td class="LBG">
			<font class="label">Funzione:</font>&nbsp;
			<font class="campo">Dettaglio Trasmissione Atti Esecuzione Pena Sostituiva</font>
		</td>
<%
if (Utils.isNullObj(enm.getEvento().getFlagDocumentoRegistrato())
		|| (!Utils.isNullObj(enm.getEvento().getFlagDocumentoRegistrato())
		&& enm.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0)) {
%>
		<!-- BOTTONE DI MODIFICA -->
     	<td class="LBG">
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActLoadModificaTrasmissioneAttiEsecuzione&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=enm.getEvento().getIdEvento()%>">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
			</a>
		</td>
		<!-- BOTTONE DI STAMPA -->
		<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
			<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActStampaTrasmissioneAttiEsecuzionePS&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+enm.getEvento().getIdEvento()%>"/>
		</jsp:include>
<%
}
%>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
  		<td class="l" width="20%">Posizione Giuridica</td>
  		<td class="L" colspan=5>
      		<font class="campo">
<%
if (!Utils.isNullObj(fsm.getFlagAltraCausa()) && fsm.getFlagAltraCausa().equals("S")) {
%>
				DETENUTO PER ALTRA CAUSA
<%
} else {
%>
				<%=pgm.getDescrPosizioneGiuridica()%>
<%
}
%>
			</font>
<%
if (pgm.isLibero() && !Utils.isNullObj(residenzaassociata) && !Utils.isNullObj(residenzaassociata.getResidenza())) {
%>   
			&nbsp;Residenza&nbsp;
    		<font class="campo"><%=residenzaassociata.getResidenza().getIndirizzo()%>&nbsp;<%=residenzaassociata.getResidenza().getDescrComune()%></font>
<%
}
%>           
		</td>
	</tr>
<%
if (!Utils.isNullObj(fsm.getFlagAltraCausa()) && fsm.getFlagAltraCausa().equals("S")) {
	if (!Utils.isNullObj(acm.getIstitutoDetenzione())) {
%>
	<tr>
		<td class="l">Detenuto presso </td>
		<td class="L" colspan=5><font class="campo"><%=acm.getIstitutoDetenzione().getDescrTipoIstituto()%></font></td>
	</tr>
<%
		if (!Utils.isNullObj(acm.getAltroLuogo())) {
%>
	<tr>
		<td class="l">Altro Luogo </td >
		<td class="L" colspan=5>
			<font class="campo"><%=StringUtils.toStringJSP(acm.getAltroLuogo())%></font>
		</td>
	</tr>
<%
		}
	}
} else if (!Utils.isNullObj(ldm.getIstitutoDetenzione())) {
%>
	<tr>
		<td class="l">Detenuto presso </td>
		<td class="L" colspan=5>
			<font class="campo"><%=ldm.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
			&nbsp;di&nbsp;<font class="campo"><%=ldm.getIstitutoDetenzione().getDescrComune()%></font>
		</td>
	</tr>
<%
}
// Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI (02, 04)
if (!Utils.isNullObj(pgm.getCodPosizioneGiuridica()) && (pgm.getCodPosizioneGiuridica().equals("02")
		|| pgm.getCodPosizioneGiuridica().equals("04"))) {
	if (!Utils.isNullObj(ldm.getAltroLuogo())) {
%>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="L" colspan=5>
			<font class="campo"><%=StringUtils.toStringJSP(ldm.getAltroLuogo())%></font>
		</td>
	</tr>
<%
	}
}
PenaComplessivaModel pcm = pcssm.getPenaComplessiva();
if (!Utils.isNullObj(pcm)) {
%>
	<tr>
  		<td class="L"><font class="label">Pena irrogata in sentenza:</font></td>
     	<td class="L" colspan="5">
<%
	if ((!Utils.isNullObj(pcm.getNumAnniReclusione()) && pcm.getNumAnniReclusione().compareTo(new BigDecimal(0)) != 0)
			|| (!Utils.isNullObj(pcm.getNumMesiReclusione()) && pcm.getNumMesiReclusione().compareTo(new BigDecimal(0)) != 0)
			|| (!Utils.isNullObj(pcm.getNumGiorniReclusione()) && pcm.getNumGiorniReclusione().compareTo(new BigDecimal(0)) != 0)) {
%>
			<font class="label">Reclusione</font>&nbsp;
			<font class="label">Anni</font>&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(pcm.getNumAnniReclusione(), "0")%></font>&nbsp;
			<font class="label">Mesi</font>&nbsp;
			<font class="campo"> <%=StringUtils.toStringJSP(pcm.getNumMesiReclusione(), "0")%></font>&nbsp;
			<font class="label">Giorni</font>&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(pcm.getNumGiorniReclusione(), "0")%></font>
<%
	}
	if (!Utils.isNullObj(pcm.getImportoMulta()) && pcm.getImportoMulta().compareTo(new BigDecimal(0)) != 0) {
%>
			&nbsp;<font class="label">Multa</font>&nbsp;
			<font class="campo"><%=StringUtils.toEuroFormat(pcm.getImportoMulta())%></font>&nbsp;&euro;
<%
	}
	if ((!Utils.isNullObj(pcm.getNumAnniArresto()) && pcm.getNumAnniArresto().compareTo(new BigDecimal(0)) != 0)
		    || (!Utils.isNullObj(pcm.getNumMesiArresto()) && pcm.getNumMesiArresto().compareTo(new BigDecimal(0)) != 0)
		    || (!Utils.isNullObj(pcm.getNumGiorniArresto()) && pcm.getNumGiorniArresto().compareTo(new BigDecimal(0)) != 0)) {
%>
			&nbsp;<font class="label">Arresto</font>&nbsp;
			<font class="label">Anni</font>&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(pcm.getNumAnniArresto(),"0")%></font>&nbsp;
			<font class="label">Mesi</font>&nbsp;
			<font class="campo"> <%=StringUtils.toStringJSP(pcm.getNumMesiArresto(),"0")%></font>&nbsp;
			<font class="label">Giorni</font>&nbsp;
			<font class="campo"> <%=StringUtils.toStringJSP(pcm.getNumGiorniArresto(),"0")%></font>
<%
	}
	if (!Utils.isNullObj(pcm.getImportoAmmenda()) && pcm.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0) {
%>
			&nbsp;<font class="label">Ammenda</font>&nbsp;
			<font class="campo"><%=StringUtils.toEuroFormat(pcm.getImportoAmmenda())%></font>&nbsp;&euro;
<%
	}
	if (pcm.getCodTipoPenaDetentiva().equals("03") || pcm.getCodTipoPenaDetentiva().equals("04")) {
%>
			&nbsp;<font class="campo"><%=StringUtils.toStringJSP(pcm.getDescrTipoPenaDetentiva())%></font>
<%
		if (pcm.getCodTipoPenaDetentiva().equals("04")) {
			if (!Utils.isNullObj(pcm.getNumAnniIsolamentoDiurno())) {
%>
			&nbsp;<font class="label">Anni</font>&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(pcm.getNumAnniIsolamentoDiurno(), "0")%></font>
<%
			}
			if (!Utils.isNullObj(pcm.getNumMesiIsolamentoDiurno())) {
%>
			&nbsp;<font class="label">Mesi</font>&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(pcm.getNumMesiIsolamentoDiurno(), "0")%></font>
<%
			}
			if (!Utils.isNullObj(pcm.getNumGiorniIsolamentoDiurno())) {
%>
			&nbsp;<font class="label">Giorni</font>&nbsp;
			<font class="campo"> <%=StringUtils.toStringJSP(pcm.getNumGiorniIsolamentoDiurno(), "0")%></font>
<%
			}
		}
	}
%>
		</td>
	</tr>
<%
}
if (!Utils.isNullObj(pcssm)) {
	SanzioneSostitutivaModel ssm = pcssm.getSanzioneSostitutiva();
	if (!Utils.isNullObj(ssm) && !Utils.isNullObj(ssm.getIdSanzioneSostitutiva())) {
%>
	<tr>
		<td class="L"><font class="label">Pena Sostitutiva applicata:</font></td>
		<td class="L" colspan="5">
<%
		if ((!Utils.isNullObj(ssm.getNumAnni()) && ssm.getNumAnni().compareTo(new BigDecimal(0)) != 0)
				|| (!Utils.isNullObj(ssm.getNumMesi()) && ssm.getNumMesi().compareTo(new BigDecimal(0)) != 0)
				|| (!Utils.isNullObj(ssm.getNumGiorni()) && ssm.getNumGiorni().compareTo(new BigDecimal(0)) != 0)) {
%>
			<font class="campo"><%=StringUtils.toStringJSP(ssm.getDescrTipoSanzione())%>&nbsp;</font>
			<font class="label">Anni:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(ssm.getNumAnni(), "0")%>&nbsp;</font>
			<font class="label">Mesi:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(ssm.getNumMesi(), "0")%>&nbsp;</font>
			<font class="label">Giorni:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(ssm.getNumGiorni(), "0")%></font>
<%
		}
		if (!Utils.isNullObj(ssm.getSanzionePecuniariaMulta()) && ssm.getSanzionePecuniariaMulta().intValue() != 0) {
%>
			<font class="label">&nbsp;Pena&nbsp;Pec.&nbsp;Multa&nbsp;</font><font class="campo"><%=StringUtils.toEuroFormat(ssm.getSanzionePecuniariaMulta())%>&nbsp;</font>&euro;
<%
		}
		if (!Utils.isNullObj(ssm.getSanzionePecuniariaAmmenda()) && ssm.getSanzionePecuniariaAmmenda().intValue() != 0) {
%>
			<font class="label">&nbsp;Pena&nbsp;Pec.&nbsp;Ammenda&nbsp;</font><font class="campo"><%=StringUtils.toEuroFormat(ssm.getSanzionePecuniariaAmmenda())%>&nbsp;</font>&euro;
<%
		}
%>
		</td>
	</tr>
<%
	}
}  
if (!Utils.isNullObj(misurecautelari) && !misurecautelari.isEmpty()) {
%>	 
	<tr>
  		<td class="l">Misure Cautelari Computate:</td>
<%
	MisuraCautelareModel mcm = null;
	Iterator imcm = misurecautelari.iterator();
   	while (imcm.hasNext()) {
		mcm = (MisuraCautelareModel) imcm.next();
       	if ("S".equals(mcm.getFlagComputabile())) {
%>
		<td class="l" colspan="5">
			<font class="campo"><%=StringUtils.toStringJSP(mcm.getDescrTipoMisura())%>&nbsp;</font>
			<font class="label">Anni:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(mcm.getNumAnni(), "0")%>&nbsp;</font>
			<font class="label">Mesi:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(mcm.getNumMesi(), "0")%>&nbsp;</font>
			<font class="label">Giorni:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(mcm.getNumGiorni(), "0")%></font>
		</td>
<%
    	}
	}
%>   
	</tr>
<%
}
if (!Utils.isNullObj(penaresidua.getIdPenaResidua())) {
%>
	<tr>
  		<td class="l">Pena da espiare:</td>
<% 
	if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0)) {
		// NOTHING TO DO!!!
	} else {
%>
		<td class="l" colspan=2>
			<font class="l">Reclusione</font>&nbsp;
			<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
			&nbsp;Multa&nbsp;
 			<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">&euro;</font>
 		</td>
<%
    }
	if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0)) {
		// NOTHING TO DO!!!
	} else {
%>
		<td class="l">
			<font class="l">Arresto</font>&nbsp;
   			<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
			&nbsp;Ammenda&nbsp;
			<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">&euro;</font>
		</td>
<%
	}
%>
	</tr>
<%
}
if (!Utils.isNullObj(penaresidua) && !Utils.isNullObj(penaresidua.getFlagSanzioneSostitutiva())
		&& "S".equals(penaresidua.getFlagSanzioneSostitutiva())) {
%>
	<tr>
		<td class="l">Sanzione sostitutiva da espiare:</td>
		<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(penaresidua.getDescrTipoSanzione())%>&nbsp;</font>
			<font class="label">Anni:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniSS(), "0")%>&nbsp;</font>
			<font class="label">Mesi:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiSS(), "0")%>&nbsp;</font>
			<font class="label">Giorni:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniSS(), "0")%></font>
<% 
	if ((!Utils.isNullObj(penaresidua.getImportoMultaSS()) && penaresidua.getImportoMultaSS().intValue() != 0)
			|| (!Utils.isNullObj(penaresidua.getImportoAmmendaSS()) && penaresidua.getImportoAmmendaSS().intValue() != 0)) {
%>
			<font class="label">&nbsp;Sanz.Pec.&nbsp;</font>
<%
		if (!Utils.isNullObj(penaresidua.getImportoMultaSS()) && penaresidua.getImportoMultaSS().intValue() != 0) {
%>
			<font class="campo">Multa&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoMultaSS())%>&nbsp;</font>&euro;
<%
		}
		if (!Utils.isNullObj(penaresidua.getImportoAmmendaSS()) && penaresidua.getImportoAmmendaSS().intValue() != 0) {
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
%>
</table>  
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
        <td class="l" width="20%">Data Emissione</td>
        <td class="L">
          	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(enm.getEvento().getDataEmissione(), "dd/MM/yyyy"))%></font>
		</td>
		<td class="l">Data Trasmissione</td>  
 		<td class="L" colspan=1><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(notificaUDS.getDataInvio(),"dd/MM/yyyy"))%></font></td>
	</tr>
<%
if (!Utils.isNullObj(magistrato)) {
%>
	<tr>
		<td class="l">Magistrato
		<td class="L" colspan="3">
     		<font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome())%></font>
			<font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome())%></font>
 		</td>
	</tr>
<%
}
if (!Utils.isNullObj(enm.getNotifiche()) && enm.getNotifiche().length > 0 && !Utils.isNullObj(notificaUDS.getUfficio())) {
%>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="Titolo" colspan="4"> Notifica alla Sorveglianza </td>
	</tr>
	<tr>
		<td class="l">Autorit&agrave; Destinazione</td>
		<td class="L" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(notificaUDS.getUfficio().getDescrTipoUfficio())%></font>
			&nbsp;di&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(notificaUDS.getUfficio().getDescrComune())%></font>
		</td>
	</tr>
<%
}
for (int i = 0; i < enm.getNotifiche().length; i++) {
	NotificaModel nmfor = enm.getNotifiche()[i];
	String codTipoNotifica = "Notifica al condannato";
	if ("AA".equals(nmfor.getCodTipoNotifica()))
		codTipoNotifica = "Notifica altro Destinatario";
	if (!Utils.isPresent(nmfor.getUffCodUfficio())) {
		if (!"ND".equals(nmfor.getCodTipoNotifica())) {
%>
	<tr>
 		<td class="Titolo" colspan="4"><%=codTipoNotifica%></td>
	</tr>
<%
		}
		if (!Utils.isNullObj(nmfor.getAutoritaEsterna()) && Utils.isNullObj(nmfor.getAvvIdAvvocatoFascicoloSiep())) {
%>
	<tr>
     	<td class="l">Autorit&agrave; Destinazione</td>
     	<td class="L" colspan="3">
      		<font class="campo"><%=StringUtils.toStringJSP(nmfor.getAutoritaEsterna().getDescrTipoAutorita())%></font>
      		&nbsp;di&nbsp;
      		<font class="campo"><%=StringUtils.toStringJSP(nmfor.getAutoritaEsterna().getDescrSede())%></font>
		</td>
    </tr>
<%
			if (!Utils.isNullObj(nmfor.getNote())) {
%>
	<tr>
		<td class="l">Indirizzo</td>
      	<td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(nmfor.getNote())%></font></td>
	</tr>
<%
			}
		}
		if (!Utils.isNullObj(nmfor.getIstitutoDetenzione())) {
			String notificaIstituto = nmfor.getIstitutoDetenzione().getDescrTipoIstituto();
			if (!Utils.isNullObj(nmfor.getIstitutoDetenzione().getDescrComune()))
				notificaIstituto += " di " + nmfor.getIstitutoDetenzione().getDescrComune();
			if (!Utils.isNullObj(nmfor.getIstitutoDetenzione().getIndirizzo()))
				notificaIstituto += " - " + nmfor.getIstitutoDetenzione().getIndirizzo();
%>
	<tr>
      	<td class="L">Istituto Notifica</td>
      	<td class="L" colspan="3">
        	<font class="campo"><%=StringUtils.toStringJSP(notificaIstituto)%></font>&nbsp;
      	</td>
	</tr>
<%
		}
		if (!Utils.isNullObj(nmfor.getAvvIdAvvocatoFascicoloSiep())) {
			if (Utils.isNullObj(nmfor.getAvvSiep())) {
				nmfor.setAvvSiep(new AvvocatoSiepModel());
    		}
%>
	<tr>
		<td class="Titolo" colspan="4">Notifica al Difensore</td>
	</tr>
	<tr>
		<td class="l">Avvocato per Notifica</td>
       	<td class="L" colspan="3">
        	<font class="campo"><%=StringUtils.toStringJSP(nmfor.getAvvSiep().getAvvocato().getCognome()) + " " + StringUtils.toStringJSP(nmfor.getAvvSiep().getAvvocato().getNome())%></font>
        	&nbsp;Foro di&nbsp;
        	<font class="campo"><%=StringUtils.toStringJSP(nmfor.getAvvSiep().getAvvocato().getForo())%></font>
        	&nbsp;Difensore di&nbsp;
        	<font class="campo"><%=StringUtils.toStringJSP(nmfor.getAvvSiep().getAvvocato().getDescrTipo())%></font>
		</td>
	</tr>
<%
			if (Utils.isNullObj(nmfor.getAutoritaEsterna())) {
        		nmfor.setAutoritaEsterna(new AutoritaEsternaModel());
      		}
%>
	<tr>
		<td class="l">Autorita Notifica</td>
		<td class="L" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(nmfor.getAutoritaEsterna().getDescrTipoAutorita())%></font>
			&nbsp;di&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(nmfor.getAutoritaEsterna().getDescrSede())%></font>
		</td>
	</tr>
<%
			if (!Utils.isNullObj(nmfor.getNote())) {
%>
	<tr>
		<td class="l">Note</td>
		<td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(nmfor.getNote())%></font>&nbsp;</td>
	</tr>
<%
			}
		}
	}
}
%>
</table>
<%
if (Utils.isNullObj(enm.getEvento().getFlagDocumentoRegistrato())
		|| (!Utils.isNullObj(enm.getEvento().getFlagDocumentoRegistrato())
		&& enm.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0)) {
%>
<br>
<div align="left" style="visibility:hidden" id="upld">
<FORM name="comandi" enctype="multipart/form-data" method="post" action="<%= IWebConstants.PG_MAIN%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=enm.getEvento().getIdEvento()%>">
<input type="HIDDEN" name="<%=ICostantiUfficio.CAMPO_COD_UFFICIO%>" value="<%=notificaUDS.getUfficio().getCodUfficio()%>">
<table>
	<tr>
    	<td class="l" rowspan="2">Documento da salvare</td>
    	<td class="L">
      		<font class="campo"><input type=file size="35" name="<%=ICostantiEvento.CAMPO_BLOB%>"></font>
     	</td>
	</tr>
   	<tr>
	<tr>
  		<td class="lNoBord">
   			<br><br><INPUT class="bottone" type="button" name="I" value="Conferma" onClick="javascript:return conferma('siap.siep.sanzionesostitutiva.action.ActUploadTrasmissioneAttiEsecuzionePS');">
		</td>
		<td class="lNoBord">
 			<br><br><INPUT class="bottone" type="button" name="I" value="Conferma Trasmissione" onClick="javascript:return conferma('siap.siep.sanzionesostitutiva.action.ActConfermaTrasmissioneAttiEsecuzionePS');">
     	</td>
	</tr>
</table>
</FORM>
</div>
<%
}
if (!Utils.isNullObj(enm.getEvento().getFlagDocumentoRegistrato())
		&& enm.getEvento().getFlagDocumentoRegistrato().equals("S")
		&& !Utils.isNullObj(enm.getEvento().getCodUfficioDestinatario())
		&& enm.getEvento().getCodUfficioDestinatario().equals("-")) {
%>
<br>
<div align="left" style="visibility:visible" id="upld">
<FORM name="comandi" enctype="multipart/form-data" method="post" action="<%= IWebConstants.PG_MAIN%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=enm.getEvento().getIdEvento()%>">
<input type="HIDDEN" name="<%=ICostantiUfficio.CAMPO_COD_UFFICIO%>" value="<%=notificaUDS.getUfficio().getCodUfficio()%>">
<table>
	<tr>
   		<td class="lNoBord">
    		<br><br><INPUT class="bottone" type="button" name="I" value="Conferma Trasmissione" onClick="javascript:return conferma('siap.siep.sanzionesostitutiva.action.ActConfermaTrasmissioneAttiEsecuzionePS');">
     	</td>
	</tr>
</table>
</FORM>
</div>
<%
}
%>
</body>
</html>
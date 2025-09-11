<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.RedirectTo"%>

<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Collection"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sico.residenza.model.ResidenzaModel"%>
<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.note.model.NoteModel"%>

<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.DettaglioFascicoloModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloSiusModel"%>
<%@ page import="siap.sius.udienza.model.UdienzaModel"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.esecuzionemisuraalternativa.action.ICostantiEsecuzioneMA"%>
<%@ page import="siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.cancassfascsius.model.CancAssFascSiusModel"%>
<%@ page import="siap.sius.esperto.model.EspertoModel"%>

<jsp:useBean id="UtenteConnesso" 			scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="fascicoloSiusGP" 			scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="fascicolo" 				scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="dettagliofascicolo" 		scope="request" class="siap.siep.fascicolo.model.DettaglioFascicoloModel"/>
<jsp:useBean id="residenza" 				scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="udienza" 					scope="request" class="siap.sius.udienza.model.UdienzaModel"/>
<jsp:useBean id="sentenza" 					scope="request" class="siap.siep.sentenza.model.SentenzaModel"/>
<jsp:useBean id="luogodet" 					scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>
<jsp:useBean id="istitutodet" 				scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="magistrato" 				scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="avvocato" 					scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="isModificabile" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoFasUnificati" 		scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicoloUnificante" 		scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="elencoNote" 				scope="request" class="java.util.Vector"/>
<jsp:useBean id="posizione_materiale" 		scope="request" class="siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel"/>
<jsp:useBean id="elencoFasCollegati" 		scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicoloPadre" 			scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="elencoTenoriStralcio" 		scope="request" class="java.util.Vector"/>
<jsp:useBean id="cancelleria_assegnataria" 	scope="request" class="siap.sius.cancassfascsius.model.CancAssFascSiusModel"/>
<jsp:useBean id="ulterioriistanze" 			scope="request" class="java.util.Vector"/>
<jsp:useBean id="collaboratore" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="licenza" 					scope="request" class="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"/>
<jsp:useBean id="fascEMSdaAMS" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="dataInvioCertificato" 		scope="request" class="java.util.Date"/>
<jsp:useBean id="certificatoPenale" 		scope="request" class="java.lang.String"/>
<%-- MEV10-s3; aggiunti attributi nella richiesta --%>
<jsp:useBean id="etichettaEta" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="oscuraEta" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="esperto" 					scope="request" class="siap.sius.esperto.model.EspertoModel"/>

<%
String isVALIGN = "top";
String isBorder = "0";
// [SG]: refactoring della pagina
String lWidth = "94%";
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Procedimento</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/gestisciUploadStampa2.js"></script>
<script language="JavaScript">
var node;
function effettoTree(a) {
    node=document.getElementById("elenco"+a);
    node.style.display = (node.style.display == "none")? "block" : "none";
    document.images["image"+a].src = (node.style.display == "none")? "<%=IWebConstants.IMAGES_DIR%>expand.gif" : "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
    return false;
}

function warnEMS() {
	var messWarnEMS='<%=fascEMSdaAMS%>';
	if (messWarnEMS != null && messWarnEMS != "" && messWarnEMS != "OK")
		alert(messWarnEMS);
	return true;
}

function VisualizzaCertificato(lAzione) {
	var hrefStampa = lAzione;
	var lIndice = hrefStampa.indexOf("?");
	var parametri = hrefStampa.substring(lIndice + 1, lAzione.length);
	stampa2("/jsp/files/Stampa.jsp", parametri);
}

// MEV10-s3: aggiunta funzione per gestire passaggio alla maggiore età del soggetto
function disattiva(a_action, a_entityname, a_entityvalue) {
	str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname + "=" + a_entityvalue;
	if (window.confirm('Disattivare Etichetta Minorenne?'))
		window.location.href = str;
}
</script>
</head>

<body class="corpo" onload="javascript:warnEMS();">
<FORM name="comandi">
<table>
	<tr>
        <td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"	alt="Stampa questa videata" border="0">
			</a>
        </td>
		<td class="LBG">
			<font class="label">Funzione:</font>&nbsp;
			<font class="campo">Dettaglio Procedimento SIUS</font>
		</td>
		<td class="LBG">
			<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
				<jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>"/>
				<jsp:param name="ValoreIdEntita" value="<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>"/>
				<jsp:param name="Modificabile" value="<%=isModificabile%>"/>
			</jsp:include>
		</td>
		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	</tr>
</table>
</FORM>

<%
if (fascicoloSiusGP != null && fascicoloSiusGP.getFascicoloSiusModel() != null && certificatoPenale != null
		&& certificatoPenale.equals("SI")) {
%>
<table cellspacing=0 cellpadding=0 style="width: 95%; border: 0;">
	<tr>
		<td class="rNoBord">
			<font class="campo">
				<input type="button" name="VisualizzaCertificato" value="Visualizza Certificato Penale del <%=StringUtils.toStringJSP(DateUtils.getDateToString(dataInvioCertificato,"dd-MM-yyyy"))%>" onClick="javascript:VisualizzaCertificato('/jsp/Main.jsp?Action=siap.sico.webservice.action.ActLoadCertificatoCasellarioGiudiziale&IDFascicolo=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>&TipoFascicolo=SIUS')">
			</font>
		</td>
	</tr>
</table>
<%
}
%>
<table cellspacing="1" cellpadding="1" style="width: 100%;" border=<%=isBorder%>>
	<tr>
		<td class="label" width="15%" valign=<%=isVALIGN%>>Procedimento</td>
		<td colspan="3">
			<table cellspacing="1" cellpadding="1" width="<%=lWidth%>" style="border: 0;">
				<tr>
					<td class="L" width="29%">
						<font class="label">Numero </font>
						<font class="campo">
							<%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnno()%>/<%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveProgr()%>
<%
if (!(UtenteConnesso.getUfficioUtente().getCodUfficio().compareTo(fascicoloSiusGP.getFascicoloSiusModel().getChiaveUfficio()) == 0)) {
%>
							&nbsp;-&nbsp;<%=fascicoloSiusGP.getFascicoloSiusModel().getDescrTipoUfficio()%>&nbsp;<%=fascicoloSiusGP.getFascicoloSiusModel().getDescrComuneUfficio()%>
<%
}
%>
						</font>
					</td>
					<td class="L">
						<font class="label">Data Iscrizione:</font>&nbsp;
						<font class="campo">
<%
if ((fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione() != null)) {
%>
							<%=DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(), "dd-MM-yyyy")%>
<%
} else {
%>
							-&nbsp;
<%
}
%>
						</font>
					</td>
				</tr>
<%
if (posizione_materiale != null && posizione_materiale.getDescrPosizioneMateriale() != null
		&& posizione_materiale.getDescrPosizioneMateriale().length() > 0) {
%>
				<tr>
					<td class="L" colspan="2">
						<font class="label">Posizione Materiale:</font>&nbsp;
						<font class="cRosso"><%=posizione_materiale.getDescrPosizioneMateriale()%></font>
					</td>
				</tr>
<%
}
%>
				<tr>
					<td class="L" colspan="2">
						<font class="cRosso"><%=collaboratore%></font>
					</td>
				</tr>
<%
String lDescrLink = "";
RedirectTo lRedir = new RedirectTo();
lRedir.setPage(IWebConstants.PG_MAIN);
	lRedir.setParameter("TornaQui",TornaQui);
if (licenza != null) {
	String lCodTipoLic = licenza.getCodTipoLicenza();
  	// Licenze
	// LC = Licenza
  	// LI = Licenza per Internato
  	// LP = Licenza Pene Sostitutive // MEV_2023-35
  	// Permessi
	// PP = Permesso Premio
	// PI = Permesso Internato
  	if ("LC".equalsIgnoreCase(lCodTipoLic) || "LI".equalsIgnoreCase(lCodTipoLic) || "LP".equalsIgnoreCase(lCodTipoLic)) {
  		// Tipo Licenza
		lDescrLink = "Dettaglio Esecuzione Licenza";
		lRedir.setAction("siap.sius.permesso.action.ActLoadDettaglioEsecuzioneLicenza");
	} else if (lCodTipoLic.equalsIgnoreCase("PP") || lCodTipoLic.equalsIgnoreCase("PI")) {
		// Tipo Permesso 
		lDescrLink = "Dettaglio Esecuzione Permesso";
		lRedir.setAction("siap.sius.permesso.action.ActLoadDettaglioEsecuzionePermesso");
	}
}
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U070") == 0) {
	lDescrLink = "Dettaglio Conversione Pena Pecuniaria";
	lRedir.setAction("siap.sius.penapecuniaria.action.ActRicercaSiusRichiestaConversione");
}
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U011") == 0) {
	lDescrLink = "Dettaglio Remissione Debito";
	lRedir.setAction("siap.sius.remissionedebito.action.ActRicercaSiusRichiestaRemissione");
}
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U023") == 0) {
	lDescrLink = "Dettaglio Misure Sicurezza";
	lRedir.setAction("siap.sius.misurasicurezza.action.ActRicercaSiusMisuraSicurezza");
}
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U086") == 0) {
	lDescrLink = "Dettaglio Misure Sicurezza";
	lRedir.setAction("siap.sius.misurasicurezza.action.ActRicercaSiusMisuraSicurezza");
}
// MEV_39: aggiunto oggetto procedimento C029: appello contro provv su MS
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("C029") == 0) {
	lDescrLink = "Dettaglio Misure Sicurezza";
	lRedir.setAction("siap.sius.misurasicurezza.action.ActRicercaSiusMisuraSicurezza");
}
// Modifica del 25/09/2013 
// Mev "Revisione Misure di Sicurezza SIUS"
// Nella pagina di Dettaglio Procedimento SIUS avente contenuto Riesame 
// Pericolosità Sociale (U067) e oggetto Unificazione delle misure di 
// sicurezza (art. 209 C.P.) (2442), inserire link Dettaglio Misure di Sicurezza.
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U067") == 0) {
	if (fascicoloSiusGP.getTenori() != null) {
		int lSize = fascicoloSiusGP.getTenori().length;
		if (lSize != 0) {
   			for (int x = 0; x < lSize; x++) {
				if (fascicoloSiusGP.getTenori()[x].getCodOggettoTenore().compareTo("2442") == 0) {
					lDescrLink = "Dettaglio Misure Sicurezza";
	    			lRedir.setAction("siap.sius.misurasicurezza.action.ActRicercaSiusMisuraSicurezza");
				}
			}
		}
	}
}
boolean testDescrLink = false;
if (lDescrLink.length() > 1)
	testDescrLink = true;
%>
				<tr>
					<td class="L" <%if (!testDescrLink) {%> colspan="2" <%}%>>
						<font class="label">Stato:</font>&nbsp;
						<font class="cRosso"><%=fascicoloSiusGP.getFascicoloSiusModel().getDescrStatoFascicolo()%></font>
					</td>
<%
if (testDescrLink) {
%>
					<td class="L">
						<font class="cRosso">
							<a class="cliccabile" href="<%=lRedir%>"><%=lDescrLink%></a>
						</font>
					</td>
<% 
}
%>
				</tr>
				<tr>
<%
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getTipoDefinizione() != null
		&& fascicoloSiusGP.getGeneraleProcedimentoModel().getTipoDefinizione().length() > 0) {
	// Decodifica di TIPO_DEFINIZIONE
	Collection lCol = (DecodificheManager.getInstance()).getTipoDefinizione();
	String lTipoDefinizione = DecodificheUtils.getDescbyCode(lCol,fascicoloSiusGP.getGeneraleProcedimentoModel().getTipoDefinizione());
    if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrDefinizione() != null
    		&& fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrDefinizione().length() > 0) {
		lTipoDefinizione += " - " + fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrDefinizione();
	}
%>
					<td class="L" nowrap>
						<font class="label">Tipo definizione:</font>&nbsp;
						<font class="campo"><%=lTipoDefinizione%></font>
					</td>
					<td class="L">
						<font class="label">Data di definizione:</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataDefinizione(),"dd-MM-yyyy"), "-")%></font>
					</td>
				</tr>
				<tr>
<%
}
if (fascicoloUnificante != null && fascicoloUnificante.getFascicoloSiusModel() != null
		&& fascicoloUnificante.getFascicoloSiusModel().getIdFascicoloSius() != null) {
%>
					<td colspan="2">
						<font class="label"> al </font>
						<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicoloUnificante.getFascicoloSiusModel().getIdFascicoloSius()%>&TornaQui=<%=TornaQui%>">
						<%=fascicoloUnificante.getFascicoloSiusModel().getChiaveAnno()%>
						/ <%=fascicoloUnificante.getFascicoloSiusModel().getChiaveProgr()%>
						</a>
					</td>
<%
}
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getCodTipoRegistro() != null) {
	if (fascicoloSiusGP.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S22") == 0
			|| fascicoloSiusGP.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S12") == 0
			|| fascicoloSiusGP.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S09") == 0
			// MEV_2023-35
			|| fascicoloSiusGP.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S30") == 0) {
		String lTipoEsecuzione = "E.M.A.";
		String lAzioneDiEsecuzione = "siap.sius.esecuzionemisuraalternativa.action.ActRicercaEsecuzioneMA";
		if (fascicoloSiusGP.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S12") == 0) {
		    lTipoEsecuzione="E.S.S.";
		    lAzioneDiEsecuzione="siap.sius.esecuzionesanzionesostitutiva.action.ActRicercaEsecuzioneSS&"+ICostantiFascicoloSius.CAMPO_COD_CONTENUTO+"=U019";
    	} else if (fascicoloSiusGP.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S09") == 0) {
			lTipoEsecuzione="E.M.S.";
			lAzioneDiEsecuzione="siap.sius.esecuzionemisurasicurezza.action.ActRicercaEsecuzioneMS";
		// MEV_2023-35    
		} else if (fascicoloSiusGP.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S30") == 0) {
		    lTipoEsecuzione="E.P.S.";
		    lAzioneDiEsecuzione="siap.sius.esecuzionesanzionesostitutiva.action.ActRicercaEsecuzioneSS&"+ICostantiFascicoloSius.CAMPO_COD_CONTENUTO+"=U126";
		}
		// MEV_2023-35 - FINE 
%>
				</tr>
				<tr>
					<td class="L" colspan="2">
						<font class="cVerde">Numero Procedimento&nbsp;<%=lTipoEsecuzione%>&nbsp;-&nbsp;&nbsp;
							<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lAzioneDiEsecuzione%>&<%=ICostantiEsecuzioneMA.CAMPO_CHIAVE_ANNO%>=<%=fascicoloSiusGP.getGeneraleProcedimentoModel().getAnnoS1()%>&<%=ICostantiEsecuzioneMA.CAMPO_CHIAVE_PROGR%>=<%=fascicoloSiusGP.getGeneraleProcedimentoModel().getProgrS1()%>&<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveUfficio()%>&TornaQui=<%=TornaQui%>">
								<%=fascicoloSiusGP.getGeneraleProcedimentoModel().getAnnoS1() %>/<%=fascicoloSiusGP.getGeneraleProcedimentoModel().getProgrS1()%>
							</a> &nbsp;&nbsp;(<%=fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrTipoRegistro()%>)
						</font>
					</td>
				</tr>
<%
	}
}
%>
			</table>
		</td>
	</tr>
<%
if (elencoFasUnificati != null && fascicoloSiusGP.getFascicoloSiusModel().getNumeroFascicoliUnificati() != null
		&& fascicoloSiusGP.getFascicoloSiusModel().getNumeroFascicoliUnificati().intValue() > 0) {
%>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="label" width="15%" valign=<%=isVALIGN%>>Proc. Unificati</td>
		<td colspan="3">
			<table cellspacing="1" cellpadding="1" width="85%" style="border: 0;">
				<tr>
					<td class="L">
<%
	Iterator itx4 = elencoFasUnificati.iterator();
    while (itx4.hasNext()) {
		FascicoloSiusModel lFasUnificati = (FascicoloSiusModel)itx4.next();
%>
						<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=lFasUnificati.getIdFascicoloSius()%>&TornaQui=<%=TornaQui%>">
							<%=lFasUnificati.getChiaveAnno()%> / <%=lFasUnificati.getChiaveProgr()%>
						</a>&nbsp;&nbsp;&nbsp;
<%
	}
%>
					</td>
				</tr>
			</table>
		</td>
	</tr>
<%
}
if (elencoFasCollegati != null && elencoFasCollegati.size() > 0) {
%>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="label" width="15%" valign=<%=isVALIGN%>>Proc. Collegati</td>
		<td class="Label" colspan="3">
			<table cellspacing="1" cellpadding="1" width="<%=lWidth%>" style="border: 0;">
				<tr>
					<td class="L" ><font class="label"></font>
<%
	Iterator itx4 = elencoFasCollegati.iterator();
	while (itx4.hasNext()) {
		FascicoloGPModel lFasCollegati = (FascicoloGPModel)itx4.next();
%>
						<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=lFasCollegati.getFascicoloSiusModel().getIdFascicoloSius()%>&TornaQui=<%=TornaQui%>">
							<%=lFasCollegati.getFascicoloSiusModel().getChiaveAnno()%>/<%=lFasCollegati.getFascicoloSiusModel().getChiaveProgr()%>&nbsp;
							<font class="label">
								<%=lFasCollegati.getFascicoloSiusModel().getCodTipoUfficio()%>&nbsp;
								<%=lFasCollegati.getFascicoloSiusModel().getDescrComuneUfficio()%>
							</font>
						</a>&nbsp;&nbsp;&nbsp;
<%
	}
%>
					</td>
				</tr>
			</table>
		</td>
	</tr>
<%
}
if (fascicoloPadre != null && fascicoloPadre.getFascicoloSiusModel().getChiaveAnno() != null) {
%>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="label" width="15%" valign=<%=isVALIGN%>>Collegato al Proc.</td>
		<td class="Label" colspan="3">
			<table cellspacing="1" cellpadding="1" width="<%=lWidth%>" style="border: 0;">
				<tr>
					<td class="L" >
						<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicoloPadre.getFascicoloSiusModel().getIdFascicoloSius()%>&TornaQui=<%=TornaQui%>">
							<%=fascicoloPadre.getFascicoloSiusModel().getChiaveAnno()%>/<%=fascicoloPadre.getFascicoloSiusModel().getChiaveProgr()%>&nbsp;
							<font class="label">
								<%=fascicoloPadre.getFascicoloSiusModel().getCodTipoUfficio()%>&nbsp;
								<%=fascicoloPadre.getFascicoloSiusModel().getDescrComuneUfficio()%>
						</font>
						</a>&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
			</table>
		</td>
	</tr>
<%
}
%>
	<tr>
		<td class="label" width="15%" valign=<%=isVALIGN%>>Udienza</td>
		<td colspan="3">
			<table cellspacing="1" cellpadding="1" width="<%=lWidth%>" style="border: 0;">
				<tr>
					<td class="L" >
						<font class="label">Data:</font>&nbsp;
<%
if (udienza.getDataUdienza() == null) {
	if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null) {
%>
						<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(), "dd-MM-yyyy"))%></font>&nbsp;
<%
	} else {
%>
						<font class="campo">-</font>&nbsp;
<%
	}
} else {
%>
						<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(udienza.getDataUdienza(), "dd-MM-yyyy"))%></font>&nbsp;
<%
}
if (fascicoloSiusGP.getUdiPro() != null && fascicoloSiusGP.getUdiPro().getFlagRinviata() != null
		&& "P".equalsIgnoreCase(fascicoloSiusGP.getUdiPro().getFlagRinviata())) {
%>
						<font class="cRosso"> ( Prefissata ) </font>
<%
}
%>
					</td>
				</tr>
			</table>
		</td>
	</tr>
<%
//==============================================================================
//                                 SOGGETTO
//==============================================================================
// MEV10-s3: aggiunto codice per etichettare la minore età
String colspanSoggetto = "2";
if (etichettaEta != null && !"".equals(etichettaEta)) {
	colspanSoggetto = "1";
}
%>
	<tr>
		<td class="label" width="15%" valign="<%=isVALIGN%>">Soggetto</td>
		<td colspan="3">
			<table cellspacing="1" cellpadding="1" width="<%=lWidth%>" style="border: 0;">
				<tr>
					<td class="L" colspan="<%=colspanSoggetto%>">
						<font class="label">Cognome Nome:</font>&nbsp;
						<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
						<font class="campo">
							<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getSoggetto().getIdSoggetto()%>&TornaQui=<%=TornaQui%>">
								<%=fascicoloSiusGP.getFascicoloSiusModel().getSoggetto().getCognome()%>&nbsp;
								<%=fascicoloSiusGP.getFascicoloSiusModel().getSoggetto().getNome()%>
							</a>
						</font>
<%
if (fascicoloSiusGP.getFascicoloSiusModel().getSoggetto().getSesso().compareTo("F") == 0) {
%>
						<font class="label">&nbsp;nata il </font>
<%
} else {
%>
						<font class="label">&nbsp;nato il </font>
<%
}
%>
						<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getSoggetto().getDataNascita(), "dd-MM-yyyy"), "-")%></font>

						<font class="label">&nbsp;in </font>
<%
if (fascicoloSiusGP.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita().compareTo("-") == 0) {
%>
						<font class="campo"><%=fascicoloSiusGP.getFascicoloSiusModel().getSoggetto().getDescrStatoNascita()%></font>
<%
} else {
%>
						<font class="campo"><%=fascicoloSiusGP.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita() + " ("+fascicoloSiusGP.getFascicoloSiusModel().getSoggetto().getCodProvinciaNascita()+")"%></font>
<%
}
%>
					</td>
<%-- MEV10-s3: aggiunto codice per etichettare la minore età --%>
<%
if ("1".equals(colspanSoggetto)) {
%>
					<td class="L" align="right"><%=etichettaEta%>
<%
	if ("SI".equals(oscuraEta)) {
%>
						<a href="Javascript:disattiva('siap.sius.fascicolo.action.ActModificaFascicolo&oscuraEtichettaMinore=yes','<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>','<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>');">
							<img src="/images/delete.gif" width="12" height="12" alt="Disattiva" border="0">
						</a>
<%
	}
%>
					</td>
<%
}
%>
				</tr>
				<tr>
					<td class="L" colspan="2" >
						<font class="label">Residenza:</font>&nbsp;
<%
boolean trovato = false;
if (residenza != null) {
	Iterator itx = residenza.iterator();
	while (itx.hasNext()) {
		ResidenzaModel lRes = (ResidenzaModel) itx.next();
		if (lRes.getCodTipoResidenza().compareTo("R") == 0) {
			trovato = true;
  			if (lRes.getCodStato().compareTo("039") == 0) {
%>
						<font class="campo"><%=lRes.getDescrComune()%> (<%=lRes.getCodProvincia()%>)</font>
						<font class="label"> - </font>
						<font class="campo"><%=lRes.getIndirizzo()%></font>
<%
			} else {
%>
						<font class="campo"><%=lRes.getDescComuneEstero()%> ( <%=lRes.getDescrStato()%> )</font>
						<font class="label"> - </font>
						<font class="campo"><%=lRes.getIndirizzo()%> </font>
<%
			}
  		}
	}
} // endif residenza
if (trovato == false) {
%>
						<font class="label"> - </font>
<%
}
trovato = false;
%>
					</td>
				</tr>
				<tr>
					<td class="L" colspan="2"  valign=<%=isVALIGN%>>
						<font class="label">Domicilio:</font>&nbsp;
<%
if (residenza != null) {
    Iterator itx2 = residenza.iterator();
    while (itx2.hasNext()) {
      	ResidenzaModel lRes = (ResidenzaModel)itx2.next();
      	if (lRes.getCodTipoResidenza().compareTo("D") == 0) {
			trovato = true;
			if (lRes.getCodStato().compareTo("039") == 0) {
%>
						<font class="campo"><%=lRes.getDescrComune()%> (<%=lRes.getCodProvincia()%>)</font>
						<font class="label"> - </font>
						<font class="campo"><%=lRes.getIndirizzo()%></font>
<%
			} else {
%>
						<font class="campo"><%=lRes.getDescComuneEstero()%> (<%=lRes.getDescrStato()%>)</font>
						<font class="label"> - </font>
						<font class="campo"><%=lRes.getIndirizzo()%></font>
<%
			}
		}
	}
    if (trovato == false) {
%>
						<font class="label"> - </font>
<%
    }
    trovato = false;
} // endif residenza
%>
					</td>
				</tr>
				<tr>
					<td class="L" colspan="2" width=30%>
						<font class="label">Posizione Giuridica SIUS:</font>&nbsp;
						<font class="campo"><%=fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrPosGiuridica()%></font>
					</td>
				</tr>
				<tr>
					<td class="L" colspan="2" width=70%>
						<font class="label">Luogo Detenzione:</font>&nbsp;
<%
if (Utils.isNullObj(luogodet.getIstitutoDetenzione())) {
%>
						<font class="campo"><%=luogodet.getDescrTipoIstituto()%></font>
<%
} else {
%>
						<font class="campo"><%=luogodet.getIstitutoDetenzione().getDescrTipoIstituto() + " di " + luogodet.getIstitutoDetenzione().getDescrizione()%></font>
<%
}
%>
					</td>
				</tr>
				<tr>
					<td class="L" colspan="2" width=30%>
						<font class="label">Data Fine Pena SIUS:</font>&nbsp;
<%
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataFinePena()!= null) {
%>
						<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataFinePena(), "dd-MM-yyyy"))%></font>
<%
} else {
%>
						<font class="campo">-</font>
<%
}
%>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="label" width="15%" valign=<%=isVALIGN%>>Atto</td>
		<td colspan="3">
			<table cellspacing="1" cellpadding="1" width="<%=lWidth%>" style="border: 0;">
				<tr>
					<td class="L" colspan="2">
<%
if (!Utils.isNullObj(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataRichiesta())) {
%>
						<font class="label">Tipo e Data:</font>
<%
} else {
%>
						<font class="label">Tipo:</font>
<%
}
%>
						<font class="campo"><%=fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrTipoAtto()%></font>
<%
if (!Utils.isNullObj(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataRichiesta())) {
%>
						<font class="L">&nbsp;del&nbsp;</font>
						<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataRichiesta(), "dd-MM-yyyy")) %></font>
<%
}
%>
						<font class="label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Data Arrivo in Cancelleria:</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataArrivoCancelleria(), "dd-MM-yyyy")) %></font>
					</td>
				</tr>
				<tr>
					<td class="L" width=50%>
						<font class="label">Mittente:</font>&nbsp;
						<font class="campo"><%=fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrTipoMittenteAtto()%></font>
					</td>
					<td class="L" width=50%>
						<font class="label">Sede:</font>&nbsp;
						<font class="campo"><%=fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrSedeMittente()%></font>
					</td>
				</tr>
<%
if ((fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrMittente().length() > 1)) {
%>
				<tr>
					<td class="L" width=40%>
						<font class="label">Descrizione:</font>&nbsp;
						<font class="campo"><%=fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrMittente()%></font>
					</td>
				</tr>
<%
}
%>
			</table>
		</td>
	</tr>
	<tr>
		<td class="label" width="15%" valign=<%=isVALIGN%>>Titolo Esecutivo</td>
		<td colspan="3">
			<table cellspacing="1" cellpadding="1" width="<%=lWidth%>" style="border: 0;">
				<tr>
					<td class="L" colspan="2" width=70%>
						<font class="label">N° SIEP</font>&nbsp;
						<font class="campo">
<%
if ((fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnnoSIEP()!= null)) {
%>
							<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep()%>&TornaQui=<%=TornaQui%>">
								<%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnnoSIEP()%>/<%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveProgrSIEP()%>
							</a>
<%
	if (fascicolo.getFlagCumulante() != null && "S".equals(fascicolo.getFlagCumulante())) {
%>
							<font class="cRosso">&nbsp;*CUMULO*&nbsp;</font>&nbsp;
<%
	}
%>
							&nbsp;-&nbsp;<%=fascicolo.getDescrTipoUfficio()%>&nbsp;<%=fascicolo.getDescrComuneUfficio()%>
<%
} else {
%>
							<font class="campo">-&nbsp;</font>
<%
}
%>
						</font>
					</td>
				</tr>
				<tr>
					<td class="L" colspan="2" width=30%>
						<font class="label">&nbsp;del&nbsp;</font>
						<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIscrizione(), "dd-MM-yyyy"))%></font>
					</td>
				<tr>
				<tr>
<%
if (sentenza.getIdSentenza() == null) {
%>
					<td class="L" width=50%>
						<font class="label">Sentenza n°:</font>
					</td>
					<td class="L" width=50%>
						<font class="label">Emessa da: - </font>
					</td>
<%
} else {
%>
					<td class="L" width=45%>
						<font class="label">Sentenza n°:</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(sentenza.getNumeroSentenza())%>/<%=StringUtils.toStringJSP(sentenza.getAnnoSentenza())%></font>&nbsp;
						<font class="label"> del </font>
						<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>&TornaQui=<%=TornaQui%>" title="Sentenza">
							<%=StringUtils.toStringJSP(DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy"))%>
						</a>
					</td>
					<td class="L" width=55%>
						<font class="label">Emessa da:</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%></font>
					</td>
<%
}
%>
				</tr>
			</table>
			<table cellspacing="1" cellpadding="1" width="<%=lWidth%>" style="border: 0;">
<%       
if (fascicoloSiusGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
	PenaResiduaModel lPenResMod=dettagliofascicolo.getPenaResidua();
%>
				<tr>
<%
	if (dettagliofascicolo.getPosizioneGiuridica() != null) {
%>
					<td class="L" width="45%">
						<font class="label">Posizione Giuridica:</font>&nbsp;
						<font class="campo"><%=dettagliofascicolo.getPosizioneGiuridica().getDescrPosizioneGiuridica()%></font>
					</td>
<%
	} else {
%>
					<td class="L" width="45%">
						<font class="label">Posizione Giuridica:</font>&nbsp;
						<font class="campo">-</font>
					</td>
<% 
	}
	if (dettagliofascicolo.getPenaResidua() != null) {
%>
					<td class="L" width="28%">
						<font class="label">Inizio Pena:</font>&nbsp;
<%
		if (dettagliofascicolo.getPenaResidua().getDataInizio()!= null) {
%>
						<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(dettagliofascicolo.getPenaResidua().getDataInizio(), "dd-MM-yyyy"))%></font>
<%
		} else {
%>
						<font class="campo">-</font>
<%
       	}
%>
					</td>
					<td class="L" width="27%">
						<font class="label">Fine Pena:</font>&nbsp;
<%
		if (dettagliofascicolo.getPenaResidua().getDataFine() != null) {
%>
						<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(dettagliofascicolo.getPenaResidua().getDataFine(), "dd-MM-yyyy"))%></font>
<%
		} else {
%>
						<font class="campo">-</font>
<%
		}
%>
					</td>
<%
	} else {
%>
					<td class="L" width=28%>
						<font class="label">Inizio Pena:</font>&nbsp;
						<font class="campo">-</font>
					</td>
					<td class="L" width=27%>
						<font class="label">Fine Pena:</font>&nbsp;
						<font class="campo">-</font>
					</td>
<%
	}
%>
				</tr>
<%
	if (lPenResMod != null) {
		// ultima pena validata
%>
				<tr>
					<td class="L" colspan="3">
<%
		if (!"S".equals(lPenResMod.getFlagSanzioneSostitutiva())) {
%>
						<font class="label">Pena da espiare:</font>&nbsp;<!--ERGASTOLO--->
<%
    		if ("S".equals(lPenResMod.getFlagErgastolo())) {
%>
						<font class="campo">ERGASTOLO</font>&nbsp;
<%
			} else if ("D".equals(lPenResMod.getFlagErgastolo())) {
%>
						<font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font>&nbsp;
<%
				if (lPenResMod.getNumAnniIsolamentoDiurno() != null && lPenResMod.getNumAnniIsolamentoDiurno().intValue() != 0) {
%>
						<font class="label">Anni</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumAnniIsolamentoDiurno(), "0")%></font>&nbsp;
<%
				}
				if (lPenResMod.getNumMesiIsolamentoDiurno() != null && lPenResMod.getNumMesiIsolamentoDiurno().intValue() != 0) {
%>
						<font class="label">Mesi</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumMesiIsolamentoDiurno(),"0")%></font>&nbsp;
<%
				}
   				if (lPenResMod.getNumGiorniIsolamentoDiurno() != null && lPenResMod.getNumGiorniIsolamentoDiurno().intValue() != 0) {
%>
						<font class="label">Giorni</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumGiorniIsolamentoDiurno(), "0")%></font>&nbsp;
<%
				}
   			}
			if ((lPenResMod.getNumAnniReclusione() !=null && lPenResMod.getNumAnniReclusione().compareTo(new BigDecimal(0)) != 0)
       				|| (lPenResMod.getNumMesiReclusione() !=null && lPenResMod.getNumMesiReclusione().compareTo(new BigDecimal(0)) != 0)
       				|| (lPenResMod.getNumGiorniReclusione() != null && lPenResMod.getNumGiorniReclusione().compareTo(new BigDecimal(0)) != 0)) { 
%>
						<font class="campo">Reclusione</font>&nbsp;
						<font class="label">Anni</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumAnniReclusione(), "0")%></font>&nbsp;
						<font class="label">Mesi</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumMesiReclusione(), "0")%></font>&nbsp;
						<font class="label">Giorni</font>&nbsp;
						<font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;
<%
			}
			if (lPenResMod.getImportoMulta() != null && lPenResMod.getImportoMulta().compareTo(new BigDecimal(0)) != 0) {
%>
						<font class="label">Multa</font>&nbsp;
						<font class="campo"><%=StringUtils.toEuroFormat(lPenResMod.getImportoMulta())%></font>&nbsp;&euro;&nbsp;
<%
			}
			if ((lPenResMod.getNumAnniArresto() !=null && lPenResMod.getNumAnniArresto().compareTo(new BigDecimal(0)) != 0)
        			|| (lPenResMod.getNumMesiArresto() !=null && lPenResMod.getNumMesiArresto().compareTo(new BigDecimal(0)) != 0)
        			|| (lPenResMod.getNumGiorniArresto() != null && lPenResMod.getNumGiorniArresto().compareTo(new BigDecimal(0)) != 0)) {
%>
						<font class="campo">Arresto</font>&nbsp;
						<font class="label">Anni</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumAnniArresto(), "0")%></font>&nbsp;
						<font class="label">Mesi</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumMesiArresto(), "0")%></font>&nbsp;
						<font class="label">Giorni</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumGiorniArresto(), "0")%></font>&nbsp;&nbsp;
<%
			}
			if (lPenResMod.getImportoAmmenda() != null && lPenResMod.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0) {
%>
						<font class="label">Ammenda</font>&nbsp;
						<font class="campo"><%=StringUtils.toEuroFormat(lPenResMod.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;
<%
			}
%>
					</td>
				</tr>
<%
		}
	} else {
		// Se non c'è pena residua inseriamo la pena irrogata in sentenza
		if (dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva() != null) {
    		PenaComplessivaSanzioneSostitutivaModel lPenaSostMod=dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva();
    		if (lPenaSostMod != null) {
      		PenaComplessivaModel lPenCompMod=lPenaSostMod.getPenaComplessiva();
      			if (lPenCompMod != null) {
%>
				<tr>
					<td class="L" colspan="3">
						<font class="label">Pena irrogata in sentenza: </font>
<%
          			if ((lPenCompMod.getNumAnniReclusione() !=null && lPenCompMod.getNumAnniReclusione().compareTo(new BigDecimal(0)) != 0)
        		  			|| (lPenCompMod.getNumMesiReclusione() !=null && lPenCompMod.getNumMesiReclusione().compareTo(new BigDecimal(0)) != 0)
        		  			|| (lPenCompMod.getNumGiorniReclusione() != null && lPenCompMod.getNumGiorniReclusione().compareTo(new BigDecimal(0)) != 0)) {
%>
						<font class="campo">Reclusione</font>&nbsp;
						<font class="label">Anni</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniReclusione(), "0")%></font>&nbsp;
						<font class="label">Mesi</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumMesiReclusione(), "0")%></font>&nbsp;
						<font class="label">Giorni</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniReclusione(), "0")%></font>&nbsp;&nbsp;
<%
					}
					if (lPenCompMod.getImportoMulta() != null && lPenCompMod.getImportoMulta().compareTo(new BigDecimal(0)) != 0) {
%>
						<font class="label">Multa</font>&nbsp;
						<font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoMulta())%></font>&nbsp;&euro;&nbsp;
<%
					}
          			if ((lPenCompMod.getNumAnniArresto() !=null && lPenCompMod.getNumAnniArresto().compareTo(new BigDecimal(0)) != 0)
              				|| (lPenCompMod.getNumMesiArresto() !=null && lPenCompMod.getNumMesiArresto().compareTo(new BigDecimal(0)) != 0)
              				|| (lPenCompMod.getNumGiorniArresto() != null && lPenCompMod.getNumGiorniArresto().compareTo(new BigDecimal(0)) != 0)) {
%>
						<font class="campo">Arresto</font>&nbsp;
						<font class="label">Anni</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniArresto(), "0")%></font>&nbsp;
						<font class="label">Mesi</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumMesiArresto(), "0")%></font>&nbsp;
						<font class="label">Giorni</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniArresto(), "0")%></font>&nbsp;&nbsp;
<%
					}
					if (lPenCompMod.getImportoAmmenda() != null && lPenCompMod.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0) {
%>
						<font class="label">Ammenda</font>&nbsp;
						<font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;
<%
					}
					if ("03".equals(lPenCompMod.getCodTipoPenaDetentiva()) || "04".equals(lPenCompMod.getCodTipoPenaDetentiva())) {
%>
						<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getDescrTipoPenaDetentiva())%></font>&nbsp;
<%
						if ("04".equals(lPenCompMod.getCodTipoPenaDetentiva())) {
							if (lPenCompMod.getNumAnniIsolamentoDiurno() != null) {
%>
						<font class="label">Anni</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniIsolamentoDiurno(), "0")%></font>&nbsp;
<%
							}
							if (lPenCompMod.getNumMesiIsolamentoDiurno() != null) {
%>
						<font class="label">Mesi</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumMesiIsolamentoDiurno(), "0")%></font>&nbsp;
<%
							}
							if (lPenCompMod.getNumGiorniIsolamentoDiurno() != null) {
%>
						<font class="label">Giorni</font>&nbsp;
						<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniIsolamentoDiurno(), "0")%></font>
<%
							}
						}
					}
%>
					</td>
				</tr>
<%
				}
			}
		}
		// fine inserimento eventuale della pena irrogata in sentenza
	}
} else {
%>
				<tr>
					<td class="L" width="45%">
						<font class="label">Posizione Giuridica:</font>&nbsp;
						<font class="campo">-</font>
					</td>
					<td class="L" width="28%">
						<font class="label">Inizio Pena:</font>&nbsp;
						<font class="campo">-</font>
					</td>
					<td class="L" width="27%">
						<font class="label">Fine Pena:</font>&nbsp;
						<font class="campo">-</font>
					</td>
				</tr>
				<tr>
					<td class="L" colspan="3">
						<font class="label">Pena da espiare:</font>&nbsp;
						<font class="campo">-</font>
					</td>
				</tr>
<%
}
%>
					
				<tr>
<%
if (dettagliofascicolo.getFascicoloSiep()!= null && dettagliofascicolo.getFascicoloSiep().getNote() != null) {
%>
					<td class="L" colspan=3><font class="label">Note Titolo Esecutivo :</font>
						<font class="campo"><%=dettagliofascicolo.getFascicoloSiep().getNote() %></font>
					</td>
<%
} else {
%>
					<td class="L" colspan=3><font class="label">Note Titolo Esecutivo :</font>
						<font class="campo">-</font>
					</td>
<% 
}
%>
				</tr>
<%-- 				<tr>
<%
if (dettagliofascicolo.getFascicoloSiep() != null && dettagliofascicolo.getFascicoloSiep().getNote() != null) {
<%-- %>
<!-- 					<td class="L" colspan="3"> -->
<!-- 						<font class="label">Note Titolo Esecutivo:</font> -->
<%-- 						<font class="campo"><%=dettagliofascicolo.getFascicoloSiep().getNote()%></font> --%>
<!-- 					</td> -->
<%-- <%
} else {
<%-- %> --%>
<!-- 					<td class="L" colspan="3"> -->
<!-- 						<font class="label">Note Titolo Esecutivo:</font> -->
<!-- 						<font class="campo">-</font> -->
<!-- 					</td> -->
<%-- <%
}
<%-- %>
 					</tr> --%>
			</table>
		</td>
	</tr>
	<tr>
		<td class="label" width="15%" valign=<%=isVALIGN%>>
			<font class="label">Contenuto</font>
		</td>
		<td colspan="3">
			<table cellspacing="1" cellpadding="1" width="<%=lWidth%>" style="border: 0;">
				<tr>
					<td class="L">
						<font class="campo"><%=fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font>
					</td>
				<tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="label" width="15%" valign=<%=isVALIGN%>>Oggetto</td>
		<td colspan="3">
			<table cellspacing="1" cellpadding="1" width="<%=lWidth%>" style="border: 0;">
				<tr>
					<td class="L">
						<font class="campo">
<%
// 05/11/2003 REWORK FascicoloGPModel (Elenco Tenori)
if (fascicoloSiusGP.getTenori() != null) {
	int lSize = fascicoloSiusGP.getTenori().length;
  	if (lSize == 0) {
%>
							-&nbsp;
<%
	}
	for (int x = 0; x < lSize; x++) {
%>
							<font class="campo"><%=fascicoloSiusGP.getTenori()[x].getDescrOggettoTenore()%>
<%
		if (fascicoloSiusGP.getTenori()[x].getCodDettaglioOggetto().length() > 1) {
%>
						</font>
						<font class="descr">&nbsp;-&nbsp;<%=fascicoloSiusGP.getTenori()[x].getDescrDettaglioOggetto()%></font>
<%
		}
%>
							<br>
<%
	}
} else {
%>
							-&nbsp;
<%
}
%>
						</font>
					</td>
				</tr>
			</table>
		</td>
	</tr>
<%
if (elencoTenoriStralcio != null && elencoTenoriStralcio.size() > 0) {
%>
	<tr>
		<td class="label" width="15%" valign=<%=isVALIGN%>>Oggetti Stralciati</td>
		<td class="Label" colspan="3">
			<table cellspacing="1" cellpadding="1" width="<%=lWidth%>" style="border: 0;">
				<tr>
					<td class="L">
						<font class="label"></font>
<%
	Iterator itx6 = elencoTenoriStralcio.iterator();
	while (itx6.hasNext()) {
		TenoreModel lTenoreStralciato = (TenoreModel)itx6.next();
%>
						<font class="campo">
							<%=lTenoreStralciato.getDescrOggettoTenore()%>
							&nbsp;&nbsp;( nel <%=lTenoreStralciato.getNote()%> )
						</font>
						<br>
<%
	}
%>
					</td>
				</tr>
			</table>
		</td>
	</tr>
<%
}
%>
<!--
Modifica del 15/11/2016 MEV_32
Per gli uffici TDS e TDSM è possibile inserire il Magistrato o l'Esperto.
Pertanto nel dettaglio deve essere possibile identificarlo.
N.B. Modifica del 12/01/2017 La descrizione deve rimanere "Magistrato" e
nel caso specifico deve comparire la dicutura (Esperto) dopo il nominativo
del Magistrato
-->
<%
if (esperto != null && esperto.getIdEsperto() != null) {
%>		
	<!-- Esperto -->
	<tr>
		<td class="label" width="15%" valign=<%=isVALIGN%>>
			<font class="label">Magistrato</font>
		</td>
		<td colspan="3">
			<table cellspacing="1" cellpadding="1" width="<%=lWidth%>" style="border: 0;">
				<tr>
					<td class="L">
						<font class="campo">
							<%=StringUtils.toStringJSP(esperto.getCognome()) + " " + StringUtils.toStringJSP(esperto.getNome())%>&nbsp;(Esperto)
						</font>
					</td>
				<tr>
			</table>
		</td>
	</tr>
<%
} else {
%>		
	<!-- Magistrato -->
	<tr>
		<td class="label" width="15%" valign=<%=isVALIGN%>>Magistrato</td>
		<td colspan="3">
			<table cellspacing="1" cellpadding="1" width="<%=lWidth%>" style="border: 0;">
<%
		if (magistrato != null) {
			trovato = false;
	       	Iterator itx3 = magistrato.iterator();
	       	while (itx3.hasNext()) {
	         	MagistratoModel lMagistrato = (MagistratoModel) itx3.next();
	         	trovato = true;
%>
				<tr>
					<td class="L">
						<font class="campo"><%=StringUtils.toStringJSP(lMagistrato.getCognome()) + " " + StringUtils.toStringJSP(lMagistrato.getNome())%></font>
					</td>
				</tr>
<%
			}
		}  // endif magistrato
		if (trovato == false) {
%>
				<tr>
					<td class="L">
						<font class="label">&nbsp;-&nbsp;</font>
					</td>
				</tr>
<%
		}
%>
			</table>
		</td>
	</tr>
<%
} // chiude if esperto else magistrato
if (cancelleria_assegnataria != null && cancelleria_assegnataria.getDescCancelleriaAssegnataria() != null
		&& cancelleria_assegnataria.getDescCancelleriaAssegnataria().length() > 0) {
%>
	<tr>
		<td class="label" width="15%" valign=<%=isVALIGN%>>Cancelleria Assegnataria</td>
		<td colspan="3">
			<table cellspacing="1" cellpadding="1" width="<%=lWidth%>" style="border: 0;">
				<tr>
					<td class="L">
						<font class="campo"><%=cancelleria_assegnataria.getDescCancelleriaAssegnataria()%></font>
					</td>
				</tr>
			</table>
		</td>
	</tr>
<%
}
%>
</table>
<table cellspacing="1" cellpadding="1" style="width: 100%;" border=<%=isBorder%>>
	<tr>
		<td class="label" width="15%" valign=<%=isVALIGN%>>Note</td>
		<td colspan="3">
			<table cellspacing="1" cellpadding="1" width="<%=lWidth%>" style="border: 0;">
				<tr>
					<td class="L">
						<font class="campo">
<%
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getAnnotazione() != null
		&& fascicoloSiusGP.getGeneraleProcedimentoModel().getAnnotazione().length()>0) {
%>
							<%=fascicoloSiusGP.getGeneraleProcedimentoModel().getAnnotazione()%>
<%
} else {
%>
							-&nbsp;
<%
}
%>
						</font>
					</td>
				</tr>
				<tr>
<%
if (elencoNote != null && elencoNote.size() > 0) {
%>
				<tr>
					<td class="L">
<%
	Iterator itx5 = elencoNote.iterator();
	while (itx5.hasNext()) {
		NoteModel lNote = (NoteModel)itx5.next();
%>
						&bull;&nbsp;
						<font class="cVerde">
							<%=DateUtils.getDateToString(lNote.getData(), "dd-MM-yyyy")%>
							&nbsp;-&nbsp;<%=lNote.getDescrizione()%>
						</font>
						<br>
<%
	}
%>
					</td>
				<tr>
<%
}
%>
			</table>
		</td>
	</tr>
</table>
<%
if (ulterioriistanze != null && ulterioriistanze.size() > 0) {
%>
<table cellspacing="1" cellpadding="1" style="width: 100%;" border=<%=isBorder%>>
	<tr>
		<td class="label" width="15%" valign=<%=isVALIGN%>>Att.ne</td>
		<td class="Label" colspan="3">
			<table cellspacing="1" cellpadding="1" width="<%=lWidth%>" style="border: 0;">
				<tr>
					<td class="L">
						<font class="label"></font>
						<font class="campo">
							<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.ulterioreistanza.action.ActRicercaUlterioreIstanza&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>&TornaQui=<%=TornaQui%>">Presenza Ulteriori Istanze</a>
						</font>
						<br>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<%
}
%>
<table cellspacing="1" cellpadding="1" width="100%" border=<%=isBorder%>>
	<tr>
		<td class="label" width="15%" colspan="2">
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.rifasiep.action.ActRicercaRifFascicoloSiep&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>&TornaQui=<%=TornaQui%>">Altri Titoli Esecutivi</a>
		</td>
		<td class="label" width="85%">
			<a>
				<img align="left" name="image5" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif" onClick="return effettoTree(5);" alt="" border="0">
			</a>
		</td>
	</tr>
</table>
<div id="elenco5" style="width: 95%; display: block">
	<jsp:include page="/jsp/files/siap/sius/rifasiep/ElencoRifFascicoloSiepShort.jsp"/>
	<br>
</div>
<table cellspacing="1" cellpadding="1" style="width: 95%;" border=<%=isBorder%>>
	<tr>
		<td class="label" width="15%" colspan="2">
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.avvocato.action.ActRicercaAvvocatoFascicoloSius&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>&TornaQui=<%=TornaQui%>">Difensori</a>
		</td>
		<td class="label" width="85%">
			<a>
				<img align="left" name="image4" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif" onClick="return effettoTree(4);" alt="" border="0">
			</a>
		</td>
	</tr>
</table>
<div id="elenco4" style="width: 95%; display: block">
			<jsp:include page="/jsp/files/siap/sius/avvocato/ElencoAvvocati.jsp"/>
	<br>
</div>
<table cellspacing="1" cellpadding="1" style="width: 95%;" border=<%=isBorder%>>
	<tr>
		<td class="label" width="15%" colspan="2">
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.provvedimento.action.ActRicercaProvvedimenti&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>&TornaQui=<%=TornaQui%>">Provvedimenti</a>
		</td>
		<td class="label" width="85%">
			<a>
				<img align="left" name="image3" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif" onClick="return effettoTree(3);" alt="" border="0">
			</a>
		</td>
	</tr>
</table>
<div id="elenco3" style="width: 95%; display: block">
	<jsp:include page="/jsp/files/siap/sius/provvedimento/ElencoProvvedimentiShort.jsp"/>
	<br>
</div>
<table cellspacing="1" cellpadding="1" style="width: 95%;" border=<%=isBorder%>>
	<tr>
		<td class="label" width="15%" colspan="2">
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.provvedimento.action.ActRicercaAltriProvvedimenti&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>&TornaQui=<%=TornaQui%>">Altri Atti</a>
		</td>
		<td class="label" width="85%">
			<a>
				<img align="left" name="image2" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif" onClick="return effettoTree(2);" alt="" border="0">
			</a>
		</td>
	</tr>
</table>
<div id="elenco2" style="width: 95%; display: block">
	<jsp:include page="/jsp/files/siap/sius/provvedimento/ElencoAltriProvvedimentiShort.jsp"/>
	<br>
</div>
<table cellspacing="1" cellpadding="1" style="width: 95%;" border=<%=isBorder%>>
	<tr>
		<td class="label" width="15%" colspan="2">
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.richiestaatti.action.ActVisualizzaStatoAtti&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>&TornaQui=<%=TornaQui%>">Richieste Istruttorie</a>
		</td>
		<td class="label" width="85%">
			<a>
				<img align="left" name="image1" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif" onClick="return effettoTree(1);" alt="" border="0">
			</a>
		</td>
	</tr>
</table>
<div id="elenco1" style="width: 95%; display: block">
	<jsp:include page="/jsp/files/siap/sius/richiestaatti/ElencoAttiRichiestiShort.jsp"/>
	<br>
</div>
<table cellspacing="1" cellpadding="1" style="width: 95%;" border=<%=isBorder%>>
	<tr>
		<td class="label" width="15%" colspan="2">
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.udienzaprocedimento.action.ActElencoUdienzeProcedimento&TornaQui=<%=TornaQui%>">Movimenti Udienza</a>
		</td>
		<td class="label" width="85%">
			<a>
				<img align="left" name="image6" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif" onClick="return effettoTree(6);" alt="" border="0">
			</a>
		</td>
	</tr>
</table>
<div id="elenco6" style="width: 95%; display: block">
	<jsp:include page="<%=ICostantiUdienzaProcedimento.PG_LISTAUDIENZEXPROCEDIMENTO%>"/>
</div>
</body>
</html>
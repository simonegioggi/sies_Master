<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza"%>
<%@ page import="siap.sius.esecuzionemisurasicurezza.action.ICostantiEsecuzioneMS"%>
<%@ page import="siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.sius.avvocatura.action.ICostantiAvvisiAvvocato" %>

<jsp:useBean id="Modificabile"       	scope="request" class="java.lang.String"/>
<jsp:useBean id="Stampabile"         	scope="request" class="java.lang.String"/>
<jsp:useBean id="ElencoTemplate"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="flag"               	scope="request" class="java.lang.String"/>
<jsp:useBean id="acdest"             	scope="request" class="java.lang.String"/>
<jsp:useBean id="datiOrdinanza"      	scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="TornaQui"           	scope="request" class="java.lang.String"/>
<jsp:useBean id="OrdinanzaRevocata"		scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="modalita"           	scope="request" class="java.lang.String"/>
<jsp:useBean id="AbilitaModifica"      	scope="request" class="java.lang.String"/>
<jsp:useBean id="misuresicurezza"      	scope="request" class="java.util.Vector"/>
<jsp:useBean id="PeriodoAltraMisura"	scope="request" class="siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel"/>
<jsp:useBean id="misuraSicurezza"      	scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<jsp:useBean id="misuraAlternativa"		scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="LicenzePeriodi"      	scope="request" class="java.util.Vector"/>

<%
//==============================================================================
// Jsp utilizzata per la visualizzazione del Dettaglio e anche in caso di 
// Modifica Ordinanza
//==============================================================================
BigDecimal IdEvento = (BigDecimal) request.getAttribute("IdEvento");
UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
UfficioModel lUffMod = lUteMod.getUfficioUtente();
String CodUff = new String(lUffMod.getCodTipoUfficio());
boolean retFlag = false;
retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
boolean ulterioreDescrizione = false;
// Flag per indicare la modalità di Modifica Ordinanza
boolean modificaOrdinanza = false;
if (modalita != null && modalita.trim().equalsIgnoreCase("M"))
	modificaOrdinanza = true;
String titolo = "Dettaglio Ordinanza";
if (modificaOrdinanza)
	titolo = "Modifica Ordinanza";

// MEV_2023-35: recupero info sul fascicolo per oggetto procedimento
String codOggettoProcedimento = "", codice = "";
if (datiOrdinanza != null && datiOrdinanza.getOrdinanza() != null && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza() != null)
	codice = datiOrdinanza.getOrdinanza().getCodTipoOrdinanza();
if (datiOrdinanza != null && datiOrdinanza.getOrdinanza() != null && datiOrdinanza.getOrdinanza().getOggettoProcedimento() != null)
	codOggettoProcedimento = datiOrdinanza.getOrdinanza().getOggettoProcedimento();
if (!Utils.isPresent(codOggettoProcedimento)) {
	FascicoloGPModel fgpm = (FascicoloGPModel) session.getAttribute("fascicoloSiusGP");
	GeneraleProcedimentoModel gpm = new GeneraleProcedimentoModel();
	if (!Utils.isNullObj(fgpm.getGeneraleProcedimentoModel()))
		gpm = fgpm.getGeneraleProcedimentoModel();
	if (Utils.isPresent(gpm.getCodOggettoProcedimento()))
		codOggettoProcedimento = gpm.getCodOggettoProcedimento();
}

String UlterioreTitolo="";
if (codice.compareTo("VC") == 0)
	UlterioreTitolo = " Rimedi Risarcitori Violazione Art.3 CEDU";
if (UlterioreTitolo.compareTo("") != 0)
	titolo += UlterioreTitolo;
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Ordinanza</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
<script language="JavaScript">
function Verify() {
	return true;
}
</script>
</head>

<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
      	<td class=LBG>
        	<font class="label">Funzione : </font>
        	<font class="campo"><%=titolo%></font>
      	</td>
<%
if (Modificabile == null || Modificabile.trim().length() < 1)
    Modificabile = "SI";
if (Stampabile == null || Stampabile.trim().length() < 1)
    Stampabile = "SI";
// Nel caso di Modifica Ordinanza si elimina stampa e cancellazione
if (modificaOrdinanza) {
	Modificabile = "NO";
  	Stampabile = "NO";
}
if (Stampabile.compareTo("SI") == 0) {
	// Deve esistere il template : da list o predefinito.
  	if (((ElencoTemplate != null) && (ElencoTemplate.trim().length() > 0))
  			|| (datiOrdinanza.getEvento().getTemIdTemplate() != null && datiOrdinanza.getEvento().getTemIdTemplate().trim().length() > 1)) {
%>
		<!-- BOTTONE DI STAMPA -->
	    <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIUS%>">
	      	<jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"/>
	      	<jsp:param name="ValoreIdEntita" value="<%=IdEvento%>"/>
	    </jsp:include>
<%
	} /* endif esistenza ElencoTemplate */
}  /* endif Stampabile = SI */
/* ANGELA è stato aggiunto il bottone di modifica*/
if (Modificabile.compareTo("SI") == 0)  {
// if (AbilitaModifica.compareTo("SI") == 0)
%>
	  	<td class="LBG">
	    	<a href="/jsp/Main.jsp?Action=siap.sius.provvedimento.action.ActLoadModificaProvvedimento&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=IdEvento%>&<%=ICostantiProvvedimento.CAMPO_TIPO_PROVVEDIMENTO%>=03<%=retParam%>">
	        	<img  align="middle" src="/images/modifica24.gif" alt="Modifica Ordinanza" width="24" height="24" border="0">
	      	</a>
		</td>
<%
}
boolean rimessioneAtti = false;
if (codice.compareTo(ICostantiDepositoOrdinanzaPc.RIMESSIONE_ATTI) == 0)
	rimessioneAtti = true;
if ((Modificabile.compareTo("SI") == 0) && (!rimessioneAtti)) {
%>
		<!-- BOTTONE DI CANCELLAZIONE -->
		<td class="LBG">
	      	<a href="Javascript:conferma('siap.sius.depositoordinanzapc.action.ActCancellaEmissioneOrdinanza','<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=IdEvento%>','TornaQui','<%=TornaQui%>');">
	        	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
	      	</a>
	    </td>
<%
}
if ((Modificabile.compareTo("SI") == 0) && (rimessioneAtti)) {
%>
		<!-- BOTTONE DI CANCELLAZIONE -->
	    <td class="LBG">
	      	<a href="Javascript:conferma('siap.sius.depositoordinanzapc.action.ActCancellaRimessioneAtti','<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=IdEvento%>','TornaQui','<%=TornaQui%>');">
	        	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
	      	</a>
	    </td>
<%
}
%>
		<!-- BOTTONE DI RITORNO -->
		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
    <tr>
      	<td>&nbsp;</td>
    </tr>
    <tr>
     	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    <tr>
      	<jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>"/>
    </tr>
</table>
<%
if (codice.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_ORDINANZA) != 0
		|| OrdinanzaRevocata == null || OrdinanzaRevocata.getOrdinanza() == null
		|| OrdinanzaRevocata.getOrdinanza().getIdDepositoOrdinanzaPc() == null) {
%>
<jsp:include page="<%=ICostantiFascicoloSius.PG_SINTESIPROCEDIMENTOORIGINESIUS%>"/>
<%
}
%>
<table cellspacing="4" cellpadding="4" width="95%">
	<tr>
      	<td>
      		<input type="hidden" name="<%= ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=IdEvento%>">
   		</td>
    </tr>
	<tr>
    	<td class="l">Tipo di Ordinanza</td>
<%
// MEV_2023-35: aggiunto controllo per gestione codice Oggetto Procedimento
String tipoOrdinanza = "";
if (Utils.isPresent(codice))
	tipoOrdinanza = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoOrdinanza(), codice);

if ("U134".equals(codOggettoProcedimento) || "U126".equals(codOggettoProcedimento))
	tipoOrdinanza = tipoOrdinanza.replace("Sanzioni", "Pene");
else if ("C064".equals(codOggettoProcedimento))
	tipoOrdinanza += " Sostitutiva";
else if ("C065".equals(codOggettoProcedimento))
	tipoOrdinanza += " Sostitutiva Derivante da Conversione Pene Pecuniarie";
else if  ("U143".equals(codOggettoProcedimento))
	tipoOrdinanza = "Ordinanza Conversione Pene Pecuniarie irrogate dal GdP";
else if ("U145".equals(codOggettoProcedimento))
	tipoOrdinanza = "Ordinanza Rateizzazione Pena Pecuniaria Sostitutiva";
else if ("U140".equals(codOggettoProcedimento))
  tipoOrdinanza += " - Pene Sostitutive ";
else if ("U146".equals(codOggettoProcedimento))
  tipoOrdinanza = " Ordinanza Revoca pena sostitutiva conseguente alla conversione pena pecuniaria per avvenuto pagamento";
// FINE MEV_2023-35

if (codice.compareTo(ICostantiDepositoOrdinanzaPc.RINVIO_ESECUZIONE_MSIC) == 0)
	codice = "42";
%>
    	<td class="l"><font class="campo"><%=tipoOrdinanza%></font></td>
<%
if (!modificaOrdinanza) {
%>
	</tr>
    <tr>
      	<td class="L"><font class="label">Data Emissione</font></td>
      	<td class="L"><font class="campo"><%=DateUtils.getDateToString(datiOrdinanza.getEvento().getDataEmissione(),"dd/MM/yyyy")%></font></td>
    </tr>
<%
}
if (datiOrdinanza.getOrdinanza().getDataDeposito() != null) {
%>
  	<tr>
    	<td class="l"> Anno / Numero Ordinanza</td>
    	<td class="l">
      		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.depositoordinanzapc.action.ActLoadInserisciDataDeposito&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=datiOrdinanza.getOrdinanza().getIdEventoGenerato()%><%=retParam%>">
         		<%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getAnnoS3())%>
          		/
         		<%=StringUtils.toStringJSP(   datiOrdinanza.getOrdinanza().getNumS3())%>
      		</a>
    	</td>
  	</tr>
  	<tr>
	    <td class="l"> Data Deposito in Cancelleria</td>
	    <td class="l"><font class="campo"> <%=DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataDeposito(),"dd/MM/yyyy")%></font></td>
  	</tr>
<%
}
if (ulterioreDescrizione) {
%>
  	<tr>
	    <td class="l">Ulteriore descrizione della decisione </td>
	    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
  	</tr>
<%
}
%>
  	<tr>
    	<td class="l">Stato del provvedimento</td>
<%
if (datiOrdinanza.getEvento().getFlagDocumentoRegistrato() != null
		&& datiOrdinanza.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("A")) {
%>
		<td class="l"><font class="cRosso">ANNULLATO</font></td>
<%
} else if (datiOrdinanza.getEvento().getFlagDocumentoRegistrato() != null
		&& datiOrdinanza.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("S")) {
%>
    	<td class="l"><font class="campo">Validato</font></td>
<%
} else {
%>
    	<td class="l"><font class="campo">Da Validare</font></td>
<%
}
%>
	</tr>
<%
if (datiOrdinanza.getEvento().getEveIdEventoRevoca() != null) {
%>
  	<tr>
    	<td class="l"><font class="crosso">Revocato</font></td>
	</tr>
<%
}
if (codice != null) {
	if (codice.compareTo(ICostantiDepositoOrdinanzaPc.LIBERAZIONE_ANTICIPATA) == 0
			|| codice.compareTo(ICostantiDepositoOrdinanzaPc.LICENZA) == 0
			|| codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_LIBERAZIONE_ANTICIPATA) == 0
			|| codice.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_LIBERAZIONE_ANTICIPATA) == 0) {
%>
	<tr>
      	<td class="L"><font class="label">Totale giorni concessi</font></td>
      	<td class="L"><font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getNumGiorniLibanticipata(), "-")%></font></td>
    </tr>
<%
	} else if(codice.compareTo(ICostantiDepositoOrdinanzaPc.VIOLAZIONE_CEDU) == 0
			|| codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMI_CEDU) == 0) {
		if (datiOrdinanza.getOrdinanza().getNumGiorniRiduzionePena() != null
				&& datiOrdinanza.getOrdinanza().getNumGiorniRiduzionePena().intValue() > 0) {
%>
	<tr>
		<td class="L"><font class="label">Totale giorni Riduzione pena</font></td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getNumGiorniRiduzionePena(), "-")%></font></td>
	</tr>
<%
		}
   		if (datiOrdinanza.getOrdinanza().getSommaRisarcimento() != null
   				&& datiOrdinanza.getOrdinanza().getSommaRisarcimento().intValue() > 0) {
%>
	<tr>
		<td class="L"><font class="label">Somma Liquidata a titolo Risarcimento</font></td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getSommaRisarcimento(), "-")%>&nbsp;&euro;</font></td>
	</tr>
<%
		}
   		if (datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp() != null
   				&& !datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp().equals("-")
   				&& datiOrdinanza.getOrdinanza().getCodUfficioMagistratoComp() != null
   				&& !datiOrdinanza.getOrdinanza().getCodUfficioMagistratoComp().equals("-")) {
%>
	<tr>
	  	<td class="L"><font class="label">Ufficio di sorveglianza Competente</font></td>
	  	<td class="L"><font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp() , "-")%></font></td>
	</tr>
<%
		}
	} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.GENERICA) == 0 ) {
%>
	<tr>
      	<td class="L"><font class="label">Dispositivo</font></td>
      	<td class="L"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento(),"-")%></font></td>
    </tr>
    <tr>
      	<td class="l">Ulteriore descrizione della decisione</td>
      	<td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
    </tr>
<%
	} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.RICHIESTA_OTTEMPERANZA) == 0 ) {
%>
    <tr>
      	<td class="L"><font class="label">Dispositivo</font></td>
      	<td class="L"><font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento(),"-")%></font></td>
    </tr>
    <tr>
      	<td class="l">Ulteriore descrizione della decisione</td>
      	<td class="l"><font class="campo"><%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
    </tr>
    <tr>
      	<td class="l">Nomina Commissario ad Acta</td>
<%
		if ("S".equals(datiOrdinanza.getOrdinanza().getFlagNominaComActa())) {
%>
      	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      	<%--td class="l"><font class="campo"><img src="<%=IWebConstants.IMAGES_DIR%>TickRed.gif" alt="SI"></font></td--%>
      	<td class="l"><font class="campo">SI</font></td>
<%
		} else {
%>
      	<td class="l"><font class="campo">NO</font></td>
<%
		}
%>      
	</tr>
    <tr>
      	<td class="l">Descrizione Commissario Ad Acta</td>
      	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getDescrCommActa(), "-")%></font></td>
    </tr>
<%
	} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.REMISSIONE_DEBITO) == 0) {
%>
    <tr>
      	<td class="L"><font class="label">Dispositivo</font></td>
      	<td class="L"><font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento(),"-")%></font></td>
    </tr>
    <tr>
      	<td class="l">Ulteriore descrizione della decisione </td>
      	<td class="l"><font class="campo"><%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
    </tr>
<%
	} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_ORDINANZA) == 0
			|| codice.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_SANZIONE_SOSTITUTIVA) == 0) {
%>
    <tr>
      	<td class="l">Ulteriore descrizione della decisione</td>
      	<td class="l"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
    </tr>
<%
	} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.MISURA_SICUREZZA) == 0
			|| codice.compareTo(ICostantiDepositoOrdinanzaPc.TRASFORMA_MISURA_SICUREZZA) == 0
			|| codice.compareTo(ICostantiDepositoOrdinanzaPc.ORD_SOSPENSIONE_ESECUZIONE_MS) == 0
        	// MEV_39: aggiunto codice 42
        	|| (codice.compareTo(ICostantiDepositoOrdinanzaPc.RINVIO_ESECUZIONE_MS) == 0
        	|| codice.compareTo(ICostantiDepositoOrdinanzaPc.RINVIO_ESECUZIONE_MSIC) == 0)) {
		if (datiOrdinanza.getEvento().getCodMotivo() != null && (datiOrdinanza.getEvento().getCodMotivo().equals("2670")
				|| datiOrdinanza.getEvento().getCodMotivo().equals("2440") || datiOrdinanza.getEvento().getCodMotivo().equals("2441")
				|| datiOrdinanza.getEvento().getCodMotivo().equals("2442"))
				&& datiOrdinanza.getOrdinanza().getDataFineMisura() != null) {
%>
    <tr>
      	<td class="l">Data cessazione </td>
      	<td class="l"><font class="campo"> <%=DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataFineMisura(),"dd/MM/yyyy")%></font></td>
    </tr>
<%
		}
		if (datiOrdinanza.getEvento().getCodMotivo() != null
				&& (datiOrdinanza.getEvento().getCodMotivo().equals("2610")
						|| datiOrdinanza.getEvento().getCodMotivo().equals("2611")
						// MEV_39: aggiunto codice 42
        				|| (codice.compareTo(ICostantiDepositoOrdinanzaPc.RINVIO_ESECUZIONE_MS) == 0
       		        	|| codice.compareTo(ICostantiDepositoOrdinanzaPc.RINVIO_ESECUZIONE_MSIC) == 0))) {
        	if (datiOrdinanza.getOrdinanza().getDataInizioPeriodo() != null) {
%>
	<tr>
<%
				if (codice.compareTo(ICostantiDepositoOrdinanzaPc.RINVIO_ESECUZIONE_MSIC) == 0) {
%>
		<td class="l">Data Decorrenza Differimento Esecuzione</td>
<%
				} else {
%>
   		<td class="l">Data rinvio</td>
<%
				}
%>
		<td class="l"><font class="campo"><%=DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataInizioPeriodo(), "dd/MM/yyyy")%></font></td>
	</tr>
<%
			}
        	if (datiOrdinanza.getOrdinanza().getDataFineMisura() != null) {
%>
	<tr>
		<td class="l">Rinvio fino al</td>
		<td class="l"><font class="campo"><%=DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataFineMisura(), "dd/MM/yyyy")%></font></td>
	</tr>
<%
			}
        	if ((datiOrdinanza.getOrdinanza().getSospensioneAASS() != null &&  datiOrdinanza.getOrdinanza().getSospensioneAASS().intValue() > 0)
        			|| (datiOrdinanza.getOrdinanza().getSospensioneMMSS() != null && datiOrdinanza.getOrdinanza().getSospensioneMMSS().intValue() > 0)
        			|| (datiOrdinanza.getOrdinanza().getSospensioneGGSS() != null && datiOrdinanza.getOrdinanza().getSospensioneGGSS().intValue() > 0)) {
%>
	<tr>
		<td class="l">Rinvio nella misura di </td>
		<td class="l">
			<font class="campo">
				Anni&nbsp;<%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getSospensioneAASS())%>&nbsp;
	          	Mesi&nbsp;<%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getSospensioneMMSS())%>&nbsp;
	          	Giorni&nbsp;<%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getSospensioneGGSS())%>
			</font>
		</td>
	</tr>
<%
			}
        	// MEV_39: aggiunto codice 42
        	if (codice.compareTo(ICostantiDepositoOrdinanzaPc.RINVIO_ESECUZIONE_MS) == 0
        		|| codice.compareTo(ICostantiDepositoOrdinanzaPc.RINVIO_ESECUZIONE_MSIC) == 0) {
        		if (Utils.isPresent(datiOrdinanza.getOrdinanza().getLuogoSvolgimentoProva())) {
%>
	<tr>
	  	<td class="l">Luogo Ricovero</td>
	  	<td class="l">
	  		<font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getLuogoSvolgimentoProva(), "-")%></font>
	  	</td>
	</tr>
<%
				}
        	}
   		}
		if (datiOrdinanza.getEvento().getCodMotivo() != null && (datiOrdinanza.getEvento().getCodMotivo().equals("2410")
				|| datiOrdinanza.getEvento().getCodMotivo().equals("2411") || datiOrdinanza.getEvento().getCodMotivo().equals("2412"))) { 
        	if (datiOrdinanza.getOrdinanza().getDataSospensioneSS() != null) {
%>
	<tr>
	  	<td class="l">Data Sospensione</td>
	  	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataSospensioneSS(), "dd/MM/yyyy"))%></font></td>
	</tr>
<%
			}
		}
        if (PeriodoAltraMisura != null && PeriodoAltraMisura.getIdPeriodoAltraMisura() != null) {
%>
	<tr>
	  	<td colspan=2>&nbsp;</td>
	</tr>
	<tr>
	  	<td class="l">Durata minima misura alla ripresa</td>
	  	<td class="l">
	  		<font class="campo">ANNI&nbsp;<%=PeriodoAltraMisura.getResiduaAA()%>&nbsp;MESI&nbsp;<%=PeriodoAltraMisura.getResiduaMM()%>&nbsp;GIORNI&nbsp;<%=PeriodoAltraMisura.getResiduaGG()%></font>
 		</td>
	</tr>
<%
		}
		if (datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp() != null
				&& !datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp().equals("-")) {
%>
	<tr>
<%
			if (CodUff.equals("TDSM") || CodUff.equals("UDSM")) {
%>
		<td class="l">Ufficio di Sorveglianza presso il Tribunale per Minorenni Competente</td>
<%
			} else {
%>      
		<td class="l">Magistrato di Sorveglianza Competente</td>
<%
			}
%>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp(), "-")%></font>
		</td>
	</tr>
<%
		}
		if (datiOrdinanza.getOrdinanza().getDescrUffTdsConcessoRiduzione() != null
				&& !datiOrdinanza.getOrdinanza().getDescrUffTdsConcessoRiduzione().equals("-")) {
%>
	<tr>
<%
			if (CodUff.equals("TDSM") || CodUff.equals("UDSM")) {
%>
		<td class="l">Tribunale per i Minorenni in funzione di Tribunale Sorveglianza Competente</td>
<%
			} else {
%>      
		<td class="l">Tribunale di Sorveglianza Competente</td>
<%
			}
%>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getDescrUffTdsConcessoRiduzione(), "-")%></font>
		</td>
	</tr>
<%
		}
		if (datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento() != null
				&& !datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento().equals("-")) {
%>
	<tr>
		<td class="l">Motivo della richiesta</td>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento(), "-")%></font>
		</td>
    </tr>
<%
		}
    	if (datiOrdinanza.getOrdinanza().getUlterioreDescrizione() != null
    			&& !datiOrdinanza.getOrdinanza().getUlterioreDescrizione().equals("-")) {
%>
    <tr>
      	<td class="l">Ulteriore descrizione della decisione</td>
      	<td class="l">
      		<font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font>
      	</td>
    </tr>
<%
		}
	}
}
if (!modificaOrdinanza) {
	if ((codice != null)
			&& (codice.compareTo(ICostantiDepositoOrdinanzaPc.RIMESSIONE_ATTI) == 0)
			&& (datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento() != null)) {
%>
	<tr>
		<td class="l">Motivazioni</td>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento(), "-")%></font>
		</td>
	</tr>
<%
	}
   	if (codice != null
   			&& codice.compareTo(ICostantiDepositoOrdinanzaPc.MISURA_SICUREZZA ) == 0) {
   		if (misuraSicurezza != null && misuraSicurezza.getDataDecorrenza() != null) {
%>
	<tr>
	  	<td class="l">Data Decorrenza per la Misura di Sicurezza:</td>
	  	<td class="l">
	  		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraSicurezza.getDataDecorrenza(), "dd/MM/yyyy"))%></font>
	  	</td>
	</tr>
<%
		}
   	}
	// MERGE v10: aggiunte or condition
 		if (datiOrdinanza.getTenori() != null && datiOrdinanza.getTenori().length > 0) {
   		if (codice != null
   				&& codice.equals(ICostantiDepositoOrdinanzaPc.TRASFORMA_MISURA_SICUREZZA)
   				&& ("0133".equals(datiOrdinanza.getTenori()[0].getCodEsitoTenore())
   						|| "0134".equals(datiOrdinanza.getTenori()[0].getCodEsitoTenore())
   						|| "0053".equals(datiOrdinanza.getTenori()[0].getCodEsitoTenore()))) {
   			String dataDecorrenza="&nbsp;";
   			try {
   				dataDecorrenza=StringUtils.toStringJSP(DateUtils.getDateToString(misuraSicurezza.getDataDecorrenza(),"dd/MM/yyyy"));
   			} catch (Exception e) {
   				dataDecorrenza="&nbsp;";	
   			}
%>
	<tr>
	  	<td class="l">Data Decorrenza per la Misura di Sicurezza:</td> 
	  	<td class="l"><font class="campo"><%=dataDecorrenza%></font></td>
	</tr>
	<tr>
	  	<td class="l">Durata:</td> 
	  	<td class="l">
	  		<font class="campo">
	  			Anni:&nbsp;<%=(misuraSicurezza.getNumAnni() == null ? "" : misuraSicurezza.getNumAnni())%>,
	  			&nbsp;Mesi:&nbsp;<%=(misuraSicurezza.getNumMesi() == null ? "" : misuraSicurezza.getNumMesi())%>,
	  			&nbsp;Giorni:&nbsp;<%=(misuraSicurezza.getNumGiorni() == null ? "" : misuraSicurezza.getNumGiorni())%></font>
	  	</td>
	</tr>
<% 
		}
	}
%>
	<tr>
     	<td colspan=2>&nbsp;</td>
   	</tr>
   	<tr>
     	<td class="Titolo" colspan="2">Esiti</td>
	</tr>
</table>
<table cellspacing="4" cellpadding="4"  width="95%">
<%
	// Elenco Tenori.
   	int lSize = datiOrdinanza.getTenori().length;
   	for (int x = 0; x < lSize; x++) {
%>
	<tr>
		<td class="l" width="35%"><%=datiOrdinanza.getTenori()[x].getDescrOggettoTenore()%></td>
		<td class="l" width="45%"><%=datiOrdinanza.getTenori()[x].getDescrEsitoTenore()%></td>
<%
		// Totale giorni per L.A. SPECIALE
        if (datiOrdinanza.getTenori()[x].getCodOggettoTenore().equals("2131") // L.A. Speciale
        		|| datiOrdinanza.getTenori()[x].getCodOggettoTenore().equals("1013") // Reclamo su L.A. Speciale
        		|| datiOrdinanza.getTenori()[x].getCodOggettoTenore().equals("0620") // Revoca su L.A. Speciale	TDS
        		|| datiOrdinanza.getTenori()[x].getCodOggettoTenore().equals("2136")) { // Revoca su L.A. Speciale	UDS
			int GiorniLS=0;
          	Iterator Itrx = LicenzePeriodi.iterator();
          	while (Itrx.hasNext()) {
				LicenzaPeriodiLibAnticipataModel lLicMod = (LicenzaPeriodiLibAnticipataModel) Itrx.next();
            	if (lLicMod.getLicenza().getDescrStatoPermesso() != null) {
              		if (lLicMod.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LS")) {
                		GiorniLS = GiorniLS + lLicMod.getLicenza().getNumeroGiorni().intValue(); 
					}
           		}
          	}
	        if (GiorniLS > 0) {
%>
		<td class="l" width="20%"><%=StringUtils.toStringJSP(GiorniLS)%>&nbsp;giorni</td>
<%
			} else {
%>  
		<td class="l" width="20%">&nbsp;</td>
<%
			}
		}
        // Totale giorni per L.A. INTEGRAZIONE
        else if (datiOrdinanza.getTenori()[x].getCodOggettoTenore().equals("2132") // Ordinanza L.A. Integrazione
        		|| datiOrdinanza.getTenori()[x].getCodOggettoTenore().equals("1014") // Ordinanza Reclamo L.A. Integrazione
        		|| datiOrdinanza.getTenori()[x].getCodOggettoTenore().equals("0621") // Ordinanza Revoca L.A. Integrazione TDS
        		|| datiOrdinanza.getTenori()[x].getCodOggettoTenore().equals("2137")) { //Ordinanza Revoca L.A. Integrazione UDS
			int GiorniLI = 0;
          	Iterator Itrx = LicenzePeriodi.iterator();
          	while (Itrx.hasNext()) {
            	LicenzaPeriodiLibAnticipataModel lLicMod = (LicenzaPeriodiLibAnticipataModel) Itrx.next();
            	if (lLicMod.getLicenza().getDescrStatoPermesso() != null) {
              		if (lLicMod.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LI")) {
                		GiorniLI = GiorniLI + lLicMod.getLicenza().getNumeroGiorni().intValue();
					}
            	}
          	}
        	if (GiorniLI > 0) {
%>  
		<td class="l" width="20%"><%=StringUtils.toStringJSP(GiorniLI)%>&nbsp;giorni</td>
<%
			} else {
%>
		<td class="l" width="20%">&nbsp;</td>      
<%
			}
       	}
        // Totale giorni per L.A.normale
        else if (datiOrdinanza.getTenori()[x].getCodOggettoTenore().equals("2130") // Ordinanza L.A.
        		|| datiOrdinanza.getTenori()[x].getCodOggettoTenore().equals("0113") // Ordinanza Reclamo L.A.
        		|| datiOrdinanza.getTenori()[x].getCodOggettoTenore().equals("0028") // Ordinanza revoca L.A. TDS
        		|| datiOrdinanza.getTenori()[x].getCodOggettoTenore().equals("2135")) { // Ordinanza Revoca L.A. UDS 
			int GiorniLA = 0;
          	Iterator Itrx = LicenzePeriodi.iterator();
          	while (Itrx.hasNext()) {
            	LicenzaPeriodiLibAnticipataModel lLicMod = (LicenzaPeriodiLibAnticipataModel) Itrx.next();
            	if (lLicMod.getLicenza().getDescrStatoPermesso() != null) {
              		if (lLicMod.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LA")) {
              			GiorniLA = GiorniLA + lLicMod.getLicenza().getNumeroGiorni().intValue();
					}
           		} else { 
                	GiorniLA = GiorniLA+lLicMod.getLicenza().getNumeroGiorni().intValue(); 
				}
			}
          	if (GiorniLA > 0) {
%>  
		<td class="l" width="20%" ><%=StringUtils.toStringJSP(GiorniLA)%> giorni</td> 
<%
			} else {
%>  
		<td class="l" width="20%">&nbsp;</td>
<%
			}
		}
        // Inserimento del Destinatario nel caso della Rimessione Atti 
        if ((datiOrdinanza.getOrdinanza() != null)
        		&& (codice != null)
        		&& (codice.compareTo(ICostantiDepositoOrdinanzaPc.RIMESSIONE_ATTI)) == 0) {
%>
		<td class="l" width=30% ><%=datiOrdinanza.getOrdinanza().getOggettoProcedimento()%></td>
<%
		}
%>
	</tr>
<%
	}
} // Chiude if (!modificaOrdinanza)
%>
	<tr>
      	<td colspan="2">&nbsp;</td>
    </tr>
</table>
<%
// Controllo su tipo Ordinanza per determinare se visualizzare le Misure Sicurezza
if (codice != null
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.MISURA_SICUREZZA) == 0) {
%>
<jsp:include page="<%=ICostantiSiusMisuraSicurezza.PG_INCLUDE_ELENCO_MISURE%>">
	<jsp:param name="EveIdEvento" value="<%=IdEvento%>"/>
	<jsp:param name="elencoMisure" value="<%=misuresicurezza%>"/>
</jsp:include>
<%
}
// Controllo su tipo Ordinanza per determinare se visualizzare le Esecuzioni Misure Sicurezza
if (codice != null
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.TRASFORMA_MISURA_SICUREZZA) == 0) {
%>
<jsp:include page="<%=ICostantiEsecuzioneMS.PG_ELENCO_ESECUZIONI_MS%>"/>
<%
   	// Elenco Tenori.
   	int lSize = datiOrdinanza.getTenori().length;
   	String unificazione = "";
   	for (int x = 0; x < lSize; x++) {
       	// verifico se si tratta di un oggetto 
       	// "Unificazione delle misure di sicurezza (art. 209 C.P.)" (2442), 
     		if (datiOrdinanza.getTenori()[x].getCodOggettoTenore().equals("2442")) {
     			unificazione = "SI";
   		}
   	}
   	if (unificazione.equals("SI")) {
%>
<jsp:include page="<%=ICostantiSiusMisuraSicurezza.PG_INCLUDE_ELENCO_MISURE%>"/>
<jsp:include page="<%=ICostantiEsecuzioneMS.PG_ELENCO_ESEC_MS_RIDETERMINATE%>"/>
<%
	}
}
// Controllo su tipo Ordinanza per determinare se visualizzare le Prescrizioni
if (codice != null
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_MA) != 0
 		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.RICOVERO_OPG) != 0
 		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.ESTINZIONE_PENA) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_LC) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.EST_PENA_LIB_CONDIZIONALE) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.CONC_RINVIO_EP) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.PROROGA_DETENZIONE_SPECIALE) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.PROROGA_DETENZIONE_DOMICILIARE) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.SOSPENSIONE_ESECUTIVA_ORDINANZA) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_PERMESSO) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_LICENZA) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_SCOMPUTO) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_REVOCA_LICENZA_PERMESSO) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.LIBERAZIONE_ANTICIPATA) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_ORDINANZA) != 0
		&& codice.compareTo(ICostantiDepositoDecreto.RICOVERO_OPG_OSS_PSICHE) != 0
		&& codice.compareTo(ICostantiDepositoDecreto.DECLARATORIA_ESTINZIONE_SS) != 0
		&& codice.compareTo(ICostantiDepositoDecreto.MODIFICA_PERMANENTE_SS) != 0
		&& codice.compareTo(ICostantiDepositoDecreto.SOSPENSIONE_ESECUZIONE_SS) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_SANZIONE_SOSTITUTIVA) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_SANZIONI_SOSTITUTIVE) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.RINVIO_SANZIONI_SOSTITUTIVE) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.RIMESSIONE_ATTI) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.VIOLAZIONE_CEDU) != 0
		&& codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMI_CEDU) != 0) {
	// 12-03-2009 Caso Applicazione Sanzione Sostitutiva o Conversione Pene Pecuniarie (Tipo di Prescrizioni diverso)
	// 08-04-2011 Stessa gestione per Applicazione Misure Sicurezza
   	if (codice.compareTo(ICostantiDepositoOrdinanzaPc.APPLICAZIONE_SANZIONI_SOSTITUTIVE) == 0
   	    	// MEV_2023-35 - Si aggiungono le PENE SOSTITUTIVE
   	    	|| codice.compareTo(ICostantiDepositoOrdinanzaPc.APPLICAZIONE_PENE_SOSTITUTIVE) == 0
   	    	// MEV_2023-35 - FINE
   			|| codice.compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_PENE_PECUNIARIE) == 0
   			|| codice.compareTo(ICostantiDepositoOrdinanzaPc.MISURA_SICUREZZA) == 0
			// MEV_2023-35: aggiunto codice per 'SR' (U142,U143,U145)
			|| codice.compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_PENE_PECUNIARIE_MANCATO_PAGAMENTO) == 0) {
%>
<jsp:include page="<%=ICostantiPrescrizione.PG_INCLUDE_PRESCRIZIONI%>">
	<jsp:param name="EveIdEvento" value="<%=IdEvento%>" />
	<jsp:param name="nextaction" value="siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza" />
	<jsp:param name="PrescrizioniSSePP" value="SSoPP" />
</jsp:include>
<%
	} else {
%>
<jsp:include page="<%=ICostantiPrescrizione.PG_INCLUDE_PRESCRIZIONI%>">
	<jsp:param name="EveIdEvento" value="<%=IdEvento%>"/>
	<jsp:param name="nextaction" value="siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza"/>
</jsp:include>
<%
	}
}
if (!modificaOrdinanza) {
	if (misuraAlternativa != null && misuraAlternativa.getFlFormaMisura() != null) {
   		String labelFormaMisura="&nbsp;";
   		if (misuraAlternativa.getFlFormaMisura() != null) {
   			labelFormaMisura="Permanenza in casa";
   			if (misuraAlternativa.getFlFormaMisura().compareTo(new BigDecimal(2)) == 0) {
   				labelFormaMisura="Collocamento in comunità";
   			}
   		}
%>
<table cellspacing="4" cellpadding="4" width="95%">
	<tr>
   		<td class="l">La misura deve essere eseguita nelle forme della:</td> 
   		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(labelFormaMisura)%></font></td>
 	</tr>
<%
		if (misuraAlternativa.getFlFormaMisura().compareTo(new BigDecimal(2)) == 0
				&& misuraAlternativa.getDescrizioneComunita() != null) {
%>
 	<tr>
	   	<td class="l">Comunità:</td> 
	   	<td class="l">
	   		<font class="campo"><%=(misuraAlternativa.getDescrizioneComunita() == null ? "" : StringUtils.toStringJSP(misuraAlternativa.getDescrizioneComunita()))%></font>
	   	</td>
 	</tr>
<%
		}
%>
</table>
<%	
	}
	if (misuraSicurezza != null && misuraSicurezza.getFlFormaMisura() != null) {
		String labelFormaMisura = "&nbsp;";
		if (misuraSicurezza.getFlFormaMisura() != null) {
			labelFormaMisura = "Permanenza in casa";
			if (misuraSicurezza.getFlFormaMisura().compareTo(new BigDecimal(2)) == 0) {
				labelFormaMisura = "Collocamento in comunità";
			}
		}
%>
<table cellspacing="4" cellpadding="4" width="95%">
	<tr>
	    <td class="l">La misura deve essere eseguita nelle forme della:</td> 
	    <td class="l">
	    	<font class="campo"><%=StringUtils.toStringJSP(labelFormaMisura)%></font>
	    </td>
	</tr>
<%
		if (misuraSicurezza.getFlFormaMisura().compareTo(new BigDecimal(2)) == 0
				&& misuraSicurezza.getDescrizioneComunita() != null) {
%>
	<tr>
		<td class="l">Comunità:</td> 
		<td class="l">
			<font class="campo"><%=(misuraSicurezza.getDescrizioneComunita() == null ? "" : StringUtils.toStringJSP(misuraSicurezza.getDescrizioneComunita()))%></font>
		</td>
	</tr>
<%
		}
%>
</table>
<%	
	}
%>
<form name="dettaglio">
<%
	if (codice != null) {
  		if (codice.compareTo(ICostantiDepositoOrdinanzaPc.LIBERAZIONE_ANTICIPATA) == 0) {
			if (datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp() != null && datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp().length() > 0) {
%>
<table cellspacing="4" cellpadding="4">
	<tr>
<%
				if (CodUff.equals("TDSM") || CodUff.equals("UDSM")) {
%>       
		<td class="l">Ufficio di Sorveglianza presso il Tribunale per minorenni destinatario di </td>
<%
				} else {
%>
		<td class="l">Ufficio di Sorveglianza destinatario di</td>
<%
				}
%>
		<td class="l">
			<font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp(), "-")%></font>
		</td>
	</tr>
</table>
<%
			}
%>
<table cellspacing="4" cellpadding="4">
   	<tr>
   		<td class="l">Ulteriore descrizione della decisione</td>
   		<td class="l">
   			<font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font>
   		</td>
 	</tr>
</table>  
<jsp:include page="<%=ICostantiLibertaAnticipata.PG_DETTAGLIO_LIBANTICIPATA%>"/>
<%
		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.LICENZA) == 0) {
%>
<jsp:include page="<%=ICostantiLicenzaLibanticipata.PG_LOAD_DETTAGLIOLICENZALIBANTICIPATA%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.INDULTINO) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_INDULTINO%>"/>
<%
		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.MISURA_ALTERNATIVA) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_MA%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_MA) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_REVOCA_MA%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.RICOVERO_OPG) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_RICOVERO_OPG%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.ESTINZIONE_PENA) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_ESTINZIONE_PENA%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_LC) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_REVOCA_LC%>"/>
<%
	   	} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.EST_PENA_LIB_CONDIZIONALE) == 0) {
%>
      	 	<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_EST_PENA_LIB_CONDIZIONALE%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.CONC_RINVIO_EP) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_CONC_RINVIO_EP%>"/>	
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.PROROGA_DETENZIONE_SPECIALE) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_PROROGA_DETENZIONE_SPE%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.PROROGA_DETENZIONE_DOMICILIARE) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_PROROGA_DETENZIONE_DOM%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.SOSPENSIONE_ESECUTIVA_ORDINANZA) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_SOSP_ESEC_ORD%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_PERMESSO) == 0
   				|| codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_LICENZA) == 0
   				|| codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMI_CEDU) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_RECLAMO_PERMESSO%>"/>
<jsp:include page="<%=ICostantiLicenzaLibanticipata.PG_LOAD_DETTAGLIOPERMESSO%>"/>
<!-- MERGE v10: aggiunta pagina di inclusione che era messa in fondo ma in un "else if" mai raggiungibile -->
<%
			if (codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMI_CEDU) == 0) {
%>
<br />
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_DETTAGLIO_ORDINANZA_RECLAMI_VIOLAZIONE_CEDU %>"/>
<%
			}
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_SCOMPUTO) == 0
   				|| codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_REVOCA_LICENZA_PERMESSO) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_RECLAMO_PERMESSO%>"/>
<jsp:include page="<%=ICostantiLicenzaLibanticipata.PG_LOAD_DETTAGLIOSCOMPUTO%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_LIBERAZIONE_ANTICIPATA) == 0
   				|| codice.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_LIBERAZIONE_ANTICIPATA) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_RECLAMATA%>"/>
<%
			if (codice.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_LIBERAZIONE_ANTICIPATA) == 0) {
%>
<jsp:include page="<%=ICostantiLibertaAnticipata.PG_DETTAGLIO_REVOCA_LIBANTICIPATA%>"/>
<%
    		} else {
%>
<jsp:include page="<%=ICostantiLibertaAnticipata.PG_DETTAGLIO_LIBANTICIPATA%>"/>
<%
			}
   		} else if (codice.compareTo(ICostantiDepositoDecreto.SOPRAVVENIENZA_NT) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_SOPRAVVENIENZA_NT%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoDecreto.RICOVERI) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_RICOVERI%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_ORDINANZA) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_REVOCA%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.APPLICAZIONE_SANZIONI_SOSTITUTIVE) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_APPLICAZIONE_SS %>"/>
<% // MEV_2023-35 
      } else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.APPLICAZIONE_PENE_SOSTITUTIVE) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_APPLICAZIONE_SP%>"/>
<%
      } else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_PENA_SOSTITUTIVA) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_REVOCA_PENA_SOSTITUTIVA%>"/>
<% 
      } else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_AVVERSO_REVOCA_PENA_SOSTITUTIVA) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_RECLAMO_AVVERSO_REVOCA_PENA_SOSTITUTIVA%>"/>
<% // MEV_2023-35 - FINE
   		} else if (codice.compareTo(ICostantiDepositoDecreto.RICOVERO_OPG_OSS_PSICHE) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_RICOVERO_OPG_OSS_PSICHE%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoDecreto.DECLARATORIA_ESTINZIONE_SS) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_DECLARATORIA_ESTINZIONE_SS%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoDecreto.MODIFICA_PERMANENTE_SS) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_MODIFICA_PERMANENTE_SS%>"/>	
<%
   		} else if (codice.compareTo(ICostantiDepositoDecreto.SOSPENSIONE_ESECUZIONE_SS) == 0
   			// MEV_2023-35: aggiunto codice per 'PA' (U141 e C066)
			|| codice.compareTo(ICostantiDepositoOrdinanzaPc.SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_SOSPENSIONE_ESECUZIONE_SANZIONI_SOSTITUTIVE%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_SANZIONI_SOSTITUTIVE) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_CONVERSIONE_SS%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.RINVIO_SANZIONI_SOSTITUTIVE) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_RINVIO_SS %>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_PENE_PECUNIARIE) == 0
   				// MEV_2023-35: aggiunto codice per 'SR' (U142,U143,U145)
   				|| codice.compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_PENE_PECUNIARIE_MANCATO_PAGAMENTO) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_CONVERSIONE_PP %>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.RIMESSIONE_ATTI) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_RIMESSIONE_ATTI %>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.ESEC_PRESSO_DOMICILIO) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_ESEC_PRESSO_DOMICILIO%>"/>	
<%
// MEV_2023-35 - Revoca e Conversione Pena Pecuniaria Sostitutiva
        } else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_REVOCA_PENA_SOST) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_DETTAGLIO_ORDINANZA_REV_CONV_PPS%>"/>
<%
   		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.VIOLAZIONE_CEDU) == 0) { // DL 92/2014 : Violazione CEDU
%>
<table cellspacing="4" cellpadding="4">
   	<tr>
   		<td class="l">Ulteriore descrizione della decisione</td>
   		<td class="l">
   			<font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font>
   		</td>
   	</tr>
</table>
<br/>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_DETTAGLIO_ORDINANZA_RISARCIMENTO_VIOLAZIONE_CEDU %>"/>
<%
   		}
   		if (datiOrdinanza.getEvento().getEveIdEventoRevoca() != null) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_SINTESI_ORDINANZA_REVOCA%>"/>
<%
		}
  		if (datiOrdinanza.getOrdinanza().getCodTipoControlloEsecuzione() != null) {
%>
<table style="width: 95%;" cellspacing="4" cellpadding="4">
	<tr>
	  	<td class="l" width="30%">Tipo Controllo Esecuzione</td>
	  	<td class="l" width="70%">
	  		<font class="campo">
	     		<%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrTipoControlloEsecuzione() )%>
     		</font>
      	</td>
	</tr>
</table>
<br/>
<%
		}
  		if (codice.trim().length() > 1) {
   			// Combo template di stampa solo sulle Emissioni di Ordinanza Nuove
   			// E se non esiste il template predefinito.
     		if (datiOrdinanza.getEvento().getTemIdTemplate() == null || datiOrdinanza.getEvento().getTemIdTemplate().trim().length() < 2) {
%>
<jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
<%
     		}
   	 	}
	} // endif cod tipo ordinanza != null
%>
</form>

<div align=left style="visibility:hidden" id="upld">
   	<FORM name="comandi" enctype="multipart/form-data" method="post">
		<table>
       		<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
   	 		<tr>
       			<td class="L">
		            <input  class=bottone  type="submit" value="Conferma">
		            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
		            <input type="HIDDEN" name="IdEvento"  value="<%= IdEvento%>">
		            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza">
       				<input type="HIDDEN" name="FlagAvvocatura" value="<%=ICostantiAvvisiAvvocato.EMISSIONE_ORDINANZA%>">
       			</td>
       		</tr>
       	</table>
	</FORM>
</div>
<%
} // end if(!modificaOrdinanza)
else {
  	//===========================
  	// Sono in modifica
  	//===========================
	if (codice.compareTo(ICostantiDepositoOrdinanzaPc.LIBERAZIONE_ANTICIPATA) == 0
  			|| codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_LIBERAZIONE_ANTICIPATA ) == 0
      		|| codice.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_LIBERAZIONE_ANTICIPATA ) == 0) {
		if (datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp() != null && datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp().length() > 0) {
%>
<table cellspacing="4" cellpadding="4">
	<tr>
<%
			if(CodUff.equals("TDSM") || CodUff.equals("UDSM")) {
%>       
		<td class="l">Ufficio di Sorveglianza presso il Tribunale per minorenni destinatario di</td>
<%
			} else {
%>
		<td class="l">Ufficio di Sorveglianza destinatario di</td>
<%
			}
%>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp(), "-")%></font>
    	</td>
  	</tr>
</table>
<%
		}
%>
<table cellspacing="4" cellpadding="4">
 	<tr>
   		<td class="l">Ulteriore descrizione della decisione</td>
   		<td class="l">
   			<font class="campo"><%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font>
   		</td>
 	</tr>
</table> 
<% // Nuova Ordinanza L.A - Decreto 146  %>
<%
		if (codice.compareTo(ICostantiDepositoOrdinanzaPc.LIBERAZIONE_ANTICIPATA) == 0) {
%>     
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_MODIFICA_ORDINANZA_LIBERAZIONE_ANTICIPATA%>"/>
<%
		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_LIBERAZIONE_ANTICIPATA) == 0) {
%>    
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_MODIFICA_ORDINANZA_RECLAMO_LIBERAZIONE_ANTICIPATA%>"/>
<%
		} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_LIBERAZIONE_ANTICIPATA) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_MODIFICA_ORDINANZA_REVOCA_LA%>"/>
<%
		}
 		// End Decreto 146
	}
  	// DL 92/2014 . Violazione Cedu
    else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.VIOLAZIONE_CEDU) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_MODIFICA_ORDINANZA_RISARCIMENTO_VIOLAZIONE_CEDU%>"/>
<%
	} else if (codice.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMI_CEDU) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_MODIFICA_ORDINANZA_RECLAMI_VIOLAZIONE_CEDU%>"/>
<%
	} else if (datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.RIMESSIONE_ATTI) == 0) {
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_MODIFICA_RIMESSIONE_ATTI%>"/>
<%
	} else { 
%>
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_MODIFICA_ORDINANZA%>"/>
<%
	}
} // end else --> if(!modificaOrdinanza)
%>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sius.avvocatura.action.ICostantiAvvisiAvvocato"%>

<jsp:useBean id="depositoDecretoMotivazioni"	scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="tenori"                      	scope="request" class="java.util.Vector"/>
<jsp:useBean id="prescrizioni"                	scope="request" class="java.util.Vector"/>
<jsp:useBean id="AutoTemplate"                	scope="request" class="java.lang.String"/>
<jsp:useBean id="StatoPermesso"               	scope="request" class="java.util.Vector"/>
<jsp:useBean id="revoca"                     	scope="request" class="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"/>
<jsp:useBean id="decretoRevocato"  				scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>

<%
// MEV_2023-35: aggiunti controlli sul contenuto
String codOggettoProcedimento = "", tipoContenuto = "";
FascicoloGPModel fgpm = (FascicoloGPModel) session.getAttribute("fascicoloSiusGP");
GeneraleProcedimentoModel gpm = new GeneraleProcedimentoModel();
if (!Utils.isNullObj(fgpm.getGeneraleProcedimentoModel()))
	gpm = fgpm.getGeneraleProcedimentoModel();
if (Utils.isPresent(gpm.getCodOggettoProcedimento()))
	codOggettoProcedimento = gpm.getCodOggettoProcedimento();
if ("U136".equals(codOggettoProcedimento))
	tipoContenuto = " Pene Sostitutive";

// La stessa jsp viene usata per Decreto Revoca Permesso e per Decreto Esclusione Computo
String nomeFunzione = "Dettaglio Decreto Revoca Permesso";
String nomeCampoGiorni = "Periodo revocato";
String pgDecretoRif = ICostantiDepositoDecreto.PG_SINTESI_DEC_PER_RIF;

if (depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto().compareTo(ICostantiDepositoDecreto.ESCLUSIONE_COMPUTO) == 0) {
	nomeFunzione = "Dettaglio Decreto Esclusione Computo Permesso";
	nomeCampoGiorni = "Periodo scomputato";
}
else if (depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto().compareTo(ICostantiDepositoDecreto.ESCLUSIONE_COMPUTO_LICENZA) == 0) {
	nomeFunzione = "Dettaglio Decreto Esclusione Computo Licenza" + tipoContenuto;
	nomeCampoGiorni = "Periodo scomputato";
	pgDecretoRif = ICostantiDepositoDecreto.PG_SINTESI_DEC_LIC_RIF;
}
else if (depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto().compareTo(ICostantiDepositoDecreto.REVOCA_LICENZA) == 0) {
	nomeFunzione = "Dettaglio Decreto Revoca Licenza";
	nomeCampoGiorni = "Periodo revocato";
	pgDecretoRif = ICostantiDepositoDecreto.PG_SINTESI_DEC_LIC_RIF;
}

UtenteModel lUteMod = (UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
UfficioModel lUffMod = lUteMod.getUfficioUtente();
String CodUff = new String(lUffMod.getCodTipoUfficio());
String labelUfficioProc = "";
if (CodUff.equals("TDSM") || CodUff.equals("UDSM")) {
	labelUfficioProc = "Sede Procura della Repubblica presso il Tribunale per Minorenni";
} else {
	labelUfficioProc = "Sede Procura";
}
%>

<html>
<head>
<title>[S.I.A.P.] - Dettaglio Decreto Revoca Permesso </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript" src="/html/gestisciUploadStampa.js"></script>
</head>
<body class="corpo">
<form name="dettaglio">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
        <td class="LBG">
        	<font class="label">Funzione : </font>
          	<font class="campo"><%=nomeFunzione%></font>
        </td>
        <jsp:include page="<%=ICostantiDepositoDecreto.BOTTONI_DETTAGLIO_DECRETO%>"/>
	</tr>
</table>
<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
<jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>"/>
<jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>"/>
<table>
	<jsp:include page="<%=ICostantiDepositoDecreto.PG_DETTAGLIO_DATA%>"/>
<%
if (decretoRevocato != null && decretoRevocato.getIdDepositoDecreto() != null) {
%>
	<jsp:include page="<%=pgDecretoRif%>"/>
<%
} else {
%>
	<tr>
	 	<td class="l"><font color="red">Decreto di Riferimento non disponibile!</font></td>
	</tr>
<%
}
%>
</table>
<jsp:include page="<%=ICostantiLicenzaLibanticipata.PG_LOAD_DETTAGLIOLICENZALIBANTICIPATA%>"/>
<table cellspacing="4" cellpadding="4" width="95%">
	<tr>
	  	<td class="Titolo" colspan="2"> Esiti</td>
	</tr>
<%
Iterator lInd = tenori.iterator();
while (lInd.hasNext()) {
%>
	<tr>
<%
	TenoreModel lTen = (TenoreModel) lInd.next();
%>
		<td class="l" width="50%"><%=lTen.getDescrOggettoTenore()%></td>
		<td class="l" width="50%"><%=lTen.getDescrEsitoTenore()%></td>
	</tr>
<%
}
%>
	<tr>
	   	<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Rilevato:</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getNote(), "-")%></font></td>
	</tr>
    <tr>
		<td class="l"><%=nomeCampoGiorni%></td>
      	<td class="l">
      		giorni:  &nbsp; <font class="campo"> <%=StringUtils.toStringJSP(revoca.getNumeroGiorni(), "-")%></font>
			ore:  &nbsp; <font class="campo"> <%=StringUtils.toStringJSP(revoca.getNumeroOre(), "-")%></font>
      	</td>
	</tr>
	<tr>
		<td class="l"><%=labelUfficioProc%></td>
		<td class="l">
			<font class="campo"><%=depositoDecretoMotivazioni.getDepositoDecreto().getDescrProcuraEsecuzione()%></font>
		</td>
    </tr>
	<tr>
		<td>
      		<input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>">
      	</td>
    </tr>
</table>
<jsp:include page="<%=ICostantiPrescrizione.PG_INCLUDE_PRESCRIZIONI%>">
	<jsp:param name="EveIdEvento" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>"/>
	<jsp:param name="nextaction" value="siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito"/>
</jsp:include>
<jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
</form>
<div align=left style="visibility:hidden" id="upld">
<FORM name="comandi" enctype="multipart/form-data" method="post">
<table>
	<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>"/>
	<tr>
		<td class="L">
			<input class="bottone" type="submit" value="Conferma">
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
			<input type="HIDDEN" name="IdEvento" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito">
			<input type="HIDDEN" name="FlagAvvocatura" value="<%=ICostantiAvvisiAvvocato.EMISSIONE_DECRETO%>">
    	</td>
	</tr>
</table>
</FORM>
</div>
</body>
</html>
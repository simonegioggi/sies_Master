<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.RedirectTo"%>

<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.permesso.model.ProvvedimentoPermessoLicenzaModel"%>
<%@ page import="siap.sius.permesso.action.ICostantiPermesso"%>

<jsp:useBean id="totali" 			scope="request" class="siap.sius.permesso.model.TotaliPermessiLicenzeModel"/>
<jsp:useBean id="criteriRicerca"	scope="request" class="siap.sius.permesso.model.CriteriRicercaProvPermessiLicenzeModel"/>

<%
Collection elenco = (Collection) request.getAttribute("elenco");
%>

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<title>[S.I.E.S.] - Ricerca Provvedimenti Concessione Permessi/Licenza</title>
<script language="JavaScript" src="/html/conferma.js"></script>
</head>
<body class="corpo">
<table>
	<tr>
 		<td class="LBG">
	  		<a href="Javascript:window.print();">
	   			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
		<td class="LBG">
			<font class="label">Funzione :</font> 
			<font class="campo">Ricerca Provvedimenti Concessione Permessi/Licenza</font>
 		</td>
 		<td class="LBG">
 			<jsp:include page="<%=ICostantiPermesso.PG_BUTTON_STAMPA_PROVV_PERMESSI_LICENZE%>"/>
		</td>
	</tr>
</table>
<table>
	<tr>
		<td>
			<table cellspacing="2" cellpadding="2"> 
				<tr>
					<td class="Titolo" colspan="2">Criteri Ricerca</td>
				</tr>    
	  			<tr>
		  			<td class="L">
		    			<font class="label">Data Iniziale</font>
		    		</td>
	  				<td class="l">
	    				<font class="campo"><%=DateUtils.getDateToString(criteriRicerca.getDataDepositoIniziale(), "dd-MM-yyyy")%></font>
					</td>
				</tr>
				<tr>
					<td class="L"><font class="label">Data Finale</font></td>
	  				<td class="l"><font class="campo"><%=DateUtils.getDateToString(criteriRicerca.getDataDepositoFinale(), "dd-MM-yyyy")%></font></td>
				</tr>
				<tr>
					<td class="L"><font class="label">Tipo Ricerca</font></td>
					<td class="L"><font class="campo"><%=criteriRicerca.getDescrTipoRicerca()%></font></td>
				</tr>
	  			<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	  		</table>
 		</td>
 		<td>&nbsp;</td>
		<td>
			<table cellspacing="2" cellpadding="2"> 
				<tr>
 					<td class="Titolo" colspan="2">Totali</td>
				</tr>    
				<tr>
					<td class="L"><font class="label">Totale Permessi Necessità</font></td>
					<td class="l"><font class="campo"><%=totali.getNumPN()%></font></td>
				</tr>
				<tr>
					<td class="L"><font class="label">Totale Permessi Premio</font></td>
					<td class="l"><font class="campo"><%=totali.getNumPP()%></font></td>
				</tr>
				<tr>
					<td class="L"><font class="label">Totale Permessi Internati</font></td>
					<td class="l"><font class="campo"><%=totali.getNumPI()%></font></td>
				</tr>
				<tr>
					<td class="L"><font class="label">Totale Licenze</font></td>
					<td class="l"><font class="campo"><%=totali.getNumLC()%></font></td>
				</tr>
				<tr>
					<td class="L"><font class="label">Totale Licenze Internati</font></td>
					<td class="l"><font class="campo"><%=totali.getNumLI()%></font></td>
				</tr>
				<%-- MEV_2023-35: aggiungo Licenza pene sostitutive (LP) --%>
				<tr>
					<td class="L"><font class="label">Totale Licenze Pene Sostitutive</font></td>
					<td class="l"><font class="campo"><%=totali.getNumLP()%></font></td>
				</tr>
				<tr>
					<td class="L"><font class="label">Totale Provvedimenti</font></td>
					<td class="l"><font class="campo"><%=totali.getNumTot()%></font></td>
				</tr>
			</table>
		</td>
		<%-- MEV_2025-48: aggiunte due righe per 'Regime 41 bis O.P.' ed una colonna 'Reati 51 c. 3 bis e 3 quater c.p.p.' --%>
		<td>
			<table cellspacing="2" cellpadding="2"> 
				<tr><td>&nbsp;</td></tr>    
				<tr>
					<td class="L">
						<font class="label">(di cui per art. 51 c. 3 bis e 3 quater c.p.p.)</font>&nbsp;&nbsp;&nbsp;
						<font class="campo"><%=totali.getNumPN51()%></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<font class="label">(di cui per art. 41 bis O.P.)</font>&nbsp;&nbsp;&nbsp;
						<font class="campo"><%=totali.getNumPN41bis()%></font>
					</td>
				</tr>
				<tr>
					<td class="L">
						<font class="label">(di cui per art. 51 c. 3 bis e 3 quater c.p.p.)</font>&nbsp;&nbsp;&nbsp;
						<font class="campo"><%=totali.getNumPP51()%></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<font class="label">(di cui per art. 41 bis O.P.)</font>&nbsp;&nbsp;&nbsp;
						<font class="campo"><%=totali.getNumPP41bis()%></font>
					</td>
				</tr>
				<tr><td>&nbsp;</td></tr>
				<tr><td>&nbsp;</td></tr>
				<tr><td>&nbsp;</td></tr>
				<tr><td>&nbsp;</td></tr>
				<tr><td>&nbsp;</td></tr>
				<tr><td>&nbsp;</td></tr>
			</table>
		</td>
	</tr>
</table>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<table cellspacing="2" cellpadding="2" width="95%">
 	<tr>
		<td class="int" width="8%">Numero SIUS</td>
		<td class="int" width="12%">Cognome Nome</td>
		<td class="int" width="8%">Data di nascita</td>
		<td class="int" width="12%">Luogo Detenzione</td>
		<td class="int" width="10%">Data Deposito</td>
		<td class="int" width="14%">Oggetto</td>
		<td class="int" width="10%">GG/ORE Concessi</td>
		<%-- MEV_2025-48: la colonna 'Esito' viene sostituita con una colonna 'Regime 41 bis O.P.' ed una colonna 'Reati 51 c. 3 bis e 3 quater c.p.p.' --%>
<!-- 		<td class="int" width="15%">Esito</td> -->
		<td class="int" width="10%">Regime 41 bis O.P.</td>
		<td class="int" width="26%">Reati 51 c. 3 bis e 3 quater c.p.p.</td>
		<td class="int">Azioni</td>
   </tr>
<%
Iterator itx = elenco.iterator();
while (itx.hasNext()) {
	ProvvedimentoPermessoLicenzaModel lModel = (ProvvedimentoPermessoLicenzaModel) itx.next();
%>
	<tr>
  		<td class="c">
<% 
	RedirectTo lRedirDettFasSius = new RedirectTo();
	lRedirDettFasSius.setPage(IWebConstants.PG_MAIN);
	lRedirDettFasSius.setAction("siap.sius.fascicolo.action.ActLoadDettaglioFascicolo");
	lRedirDettFasSius.setParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS, lModel.getFascicolo().getIdFascicoloSius().toString());
	lRedirDettFasSius.setParameter( IWebConstants.LINK_RITORNO, "20");
%>      
			<a class="cliccabile" href="<%=lRedirDettFasSius%>">
				<%=StringUtils.toStringJSP(lModel.getFascicolo().getChiaveAnno())%>/<%=StringUtils.toStringJSP(lModel.getFascicolo().getChiaveProgr())%>
			</a>
		</td>
		<td class="c">
			<%=StringUtils.toStringJSP(lModel.getSoggetto().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lModel.getSoggetto().getNome())%>
		</td>
		<td class="c">
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(lModel.getSoggetto().getDataNascita(), "dd-MM-yyyy"))%>
		</td>
		<td class="c">
			<%=StringUtils.toStringJSP(lModel.getIstitutoDetenzione().getDescrizione(), "-")%>
		</td>
		<td class="c">
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(lModel.getDepositoDecreto().getDataDeposito(), "dd-MM-yyyy"))%>
		</td>
		<td class="c">
			<%=StringUtils.toStringJSP(lModel.getEvento().getDescrMotivo(), "-")%>
		</td>
		<td class="c">
			<%=StringUtils.toStringJSP(lModel.getLicenza().getNumeroGiorni(), "-" )%> /
			<%=StringUtils.toStringJSP(lModel.getLicenza().getNumeroOre(), "-" )%>
		</td>
<%-- 		<%=StringUtils.toStringJSP(lModel.getLicenza().getDescrEsito(), "-")%> --%>
		<td class="c">
			<%=StringUtils.toStringJSP(("02").equals(lModel.getLicenza().getCodMotivoDetenzione()) ? "SI" : "-")%>
		</td>
				<td class="c">
			<%=StringUtils.toStringJSP(("01").equals(lModel.getLicenza().getCodMotivoDetenzione()) ? "SI" : "-")%>
		</td>
		<td class="c">
<% 
	RedirectTo lRedirDettEsec = new RedirectTo();
	lRedirDettEsec.setPage(IWebConstants.PG_MAIN);
	// MEV_2023-35: aggiungo codici per l'esclusione
	if (lModel.getLicenza().getCodTipoLicenza().startsWith("P") // Tutti i tipi di Permessi
			|| lModel.getLicenza().getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.ESCLUSIONE_COMPUTO_PERMESSO))
		lRedirDettEsec.setAction("siap.sius.permesso.action.ActLoadDettaglioEsecuzionePermesso");
	else if (lModel.getLicenza().getCodTipoLicenza().startsWith("L") // tutti i tipi di licenza
			|| lModel.getLicenza().getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.ESCLUSIONE_COMPUTO_LICENZA)) 
	  	lRedirDettEsec.setAction("siap.sius.permesso.action.ActLoadDettaglioEsecuzioneLicenza");			
	// 20110525 - PM : Commentato e sostituita con la precedente, ove le condizioni di switch
	//				  sono la lattera iniziale come aggregato.	
	/*
	if (lModel.getLicenza().getCodTipoLicenza().equalsIgnoreCase("PP"))
	  	lRedirDettEsec.setAction("siap.sius.permesso.action.ActLoadDettaglioEsecuzionePermesso");
	else if (lModel.getLicenza().getCodTipoLicenza().equalsIgnoreCase("LC"))
		lRedirDettEsec.setAction("siap.sius.permesso.action.ActLoadDettaglioEsecuzioneLicenza");			
	*/
	lRedirDettEsec.setParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO, lModel.getFascicolo().getChiaveAnno().toString());
	lRedirDettEsec.setParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR, lModel.getFascicolo().getChiaveProgr().toString());
	lRedirDettEsec.setParameter(IWebConstants.LINK_RITORNO, "20");
%>
			<a href="<%=lRedirDettEsec%>">
   				<img src="/images/dettagli.gif" alt="Dettaglio Esecuzione" width="12" height="12" border="0">
    		</a>
  		</td>
	</tr>
<%
}
%>
</table>
</body>
</html>
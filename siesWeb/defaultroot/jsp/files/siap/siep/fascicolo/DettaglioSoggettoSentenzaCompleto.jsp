<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.HashSet"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.util.SiapStringUtil"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.IDecodifiche"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.util.SICOLookupRemote"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.camponota.model.CampoNotaModel"%>
<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.sico.evento.action.ActGestisciButtonsProvvedimento"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.siep.penapresunta.model.PenaPresuntaModel"%>
<%@ page import="siap.siep.penacumulo.model.PenaCumuloModel"%>
<%@ page import="siap.siep.misurasicurezza.model.FascMsToFascSiepModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>
<%@ page import="siap.siep.scambiosanzione.model.ScambioSanzioneModel"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza"%>
<%@ page import="siap.siep.penapecuniaria.controller.RichiestaConversioneController"%>
<%@ page import="siap.siep.modulocumulo.model.PenaRideterminataCumuloModel"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel"%>

<%@ page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);%>

<jsp:useBean id="UtenteConnesso"          	scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="fascicolo"               	scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="dettagliofascicolo"      	scope="request" class="siap.siep.fascicolo.model.DettaglioFascicoloModel"/>
<jsp:useBean id="fascicoloCollMod"        	scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="richiestaconversione"    	scope="request" class="siap.siep.penapecuniaria.model.RichiestaConversioneModel"/>
<jsp:useBean id="vectfasc"                	scope="request" class="java.util.Vector"/>
<jsp:useBean id="registroIstanzaCollMod"  	scope="request" class="java.util.Vector"/>
<jsp:useBean id="IstruttoriaCumuloAperta" 	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="vecFascicoloIV"      		scope="request" class="java.util.Vector"/>
<jsp:useBean id="vediLinkSorv"         	  	scope="request" class="java.lang.String"/>
<jsp:useBean id="etichettaEta" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="oscuraEta" 				scope="request" class="java.lang.String"/>
<%-- MEV26 cumulo --%>
<jsp:useBean id="LastEveTrasm"            	scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="AnnotazioneEsitoCumulo"  	scope="request" class="siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel"/>
<jsp:useBean id="CompetenzaCumulo"        	scope="request" class="siap.siep.competenza.model.CompetenzaModel"/>
<jsp:useBean id="FascCompetenteCumulo"    	scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<%-- MEV_2023-33: aggiunti useBean x gestione Civilmente Obbligato ed elenco stato pagamenti --%>
<jsp:useBean id="existCivilmenteObbligato"	scope="request" class="java.lang.Boolean"/>
<jsp:useBean id="existPagamenti"			scope="request" class="java.lang.Boolean"/>

<%
SoggettoModel soggetto = fascicolo.getSoggetto();
SentenzaModel sentenza = fascicolo.getSentenza();
%>
<%
//==============================================================================
// I dati visualizzati nella form sono i seguenti
// - PROCEDIMENTO
// - SOGGETTO
// - SENTENZA
// - DATA IRREVOCABILITA'/NOTE
// - stato del procedimneto ???
// - CUMULO
// - POSIZIONE GIURIDICA/istituto
// - LISTA AVVOCATI
// - PENA IRROGATA IN SENTENZA (solo se non cumulante)
// - PENA DA ESPIARE (quantum ultima pena validata)
// - DECORRENZA/SCADENZA (solo data inizio e data fine (no date intermedie))
// - PENA RESIDUA (calcolata al volo tra la data di systema e il fine pena previsto) solo se pena effettivamente in decorrenza
// - LIBERAZIONE ANTICIPATA
// - Stato differimento (pena differita)
// - MISURA ALTERNATIVA
// - POSIZIONE MATERIALE
// - ULTIMI EVENTI
//==============================================================================
// 26/01/2015 MEV023 Conversioni Pene Pecuniarie - Modifiche descrizioni.
int lFasPro = fascicolo.getChiaveProgr().intValue();
String lDescPena1 = "Pena irrogata in sentenza : ";
String lDescPena2 = "Pena da espiare : ";
String lDescPena3 = "Richiesta conversione : ";
if (lFasPro > 70000 && lFasPro < 80000) {
	lDescPena1 = "Pena pecuniaria da recuperare : ";
	lDescPena2 = "Sanzione pecuniaria residua : ";
	lDescPena3 = "Richiesta conversione pena pecuniaria : ";
}
%>
<script language="JavaScript" src="/html/gestisciUploadStampa2.js"></script>
<script language="JavaScript">
var desktop;
function ListaSanzioni() {
	desktop = window.open("/jsp/Main.jsp?Action=siap.sius.sanzionesostitutiva.action.ActLoadListaSanzioniSostitutiveUDS" , "Lista_Date", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=600, height=500");
}                                                                                         
<%-- MEV_39: rimossa funzione, scrivo direttamente sulla pagina --%>
<%-- function ListaMisure() { --%>
<%-- desktop = window.open("/jsp/Main.jsp?Action=siap.sius.misurasicurezza.action.ActLoadListaMisureSicurezzaPM&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>" , "Lista_Date", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=600, height=450"); --%>
<%-- } --%>																																							  
</script>

<table cellspacing="0" cellpadding="0" width="95%">
<%--
==============================================================================
                                PROCEDIMENTO
==============================================================================
--%>
	<tr>
		<td class="L" colspan="2" width=100%>
        	<font class="label">Procedimento : N.</font>
        	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>" title="Procedimento">
          		<%=StringUtils.toStringJSP(fascicolo.getChiaveAnno())%>
          		/
          		<%=StringUtils.toStringJSP(fascicolo.getChiaveProgr())%>
        	</a>&nbsp;
<%
if (!(UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio()))) {
%>
          	<font class="label"><%=fascicolo.getDescrTipoUfficio() + " DI " + fascicolo.getDescrComuneUfficio()%></font>
          	<br>
<%
}
if (fascicolo.getFlagCumulante() != null && fascicolo.getFlagCumulante().equals("S")) {
%>
          	<font class="cRossoCumulo"> &nbsp;C&nbsp; </font>&nbsp;
<%
}
if (fascicolo.getCodOperatoreInserimento() != null && fascicolo.getCodOperatoreInserimento().startsWith("res-")) {
%>
          	<font class="cRossoCumulo"> &nbsp;Migrato&nbsp; </font>&nbsp;
<%
}
if (fascicolo.getCodStatoFascicolo() != null && (fascicolo.getCodStatoFascicolo().equals("01"))) {
%>
          	<font class="cRossoCumulo"> &nbsp;Archiviato&nbsp;</font>&nbsp;
<%
}
boolean isPenaSospesa = false;
if ((dettagliofascicolo.getPenaResidua() != null
		&& dettagliofascicolo.getPenaResidua().getFlagPenaSospesa() != null
     	&& dettagliofascicolo.getPenaResidua().getFlagPenaSospesa().equals("S"))
     	|| (fascicolo!= null && fascicolo.getChiaveProgr() != null
        && (fascicolo.getChiaveProgr().intValue() >= 30000 && fascicolo.getChiaveProgr().intValue() < 40000))) {
	if (fascicolo!= null && fascicolo.getChiaveProgr() != null
    		&& (fascicolo.getChiaveProgr().intValue() >= 30000 && fascicolo.getChiaveProgr().intValue() < 40000)) {
		isPenaSospesa = true;
%>
			<font class="cRossoCumulo"> &nbsp;Pena Sospesa Condizionalmente&nbsp;</font>&nbsp;
<%
	} else {
		isPenaSospesa = true;
%>
			<font class="cRossoCumulo"> &nbsp;Esecuzione Sospesa&nbsp;</font>&nbsp;
<%
	}
}
if (dettagliofascicolo.getPenaResidua() != null
		&& dettagliofascicolo.getPenaResidua().getFlagPenaSospesa() != null
		&& dettagliofascicolo.getPenaResidua().getFlagPenaSospesa().equals("I")) {
%>
          	<font class="cRossoCumulo"> &nbsp;Pena Interrotta&nbsp;</font>&nbsp;
<%
}
if (dettagliofascicolo.getPenaResidua() != null
		&& dettagliofascicolo.getPenaResidua().getFlagPenaSospesa() != null
		&& dettagliofascicolo.getPenaResidua().getFlagPenaSospesa().equals("D")) {
%>
          	<font class="cRossoCumulo"> &nbsp;Pena Differita&nbsp;</font>&nbsp;
<%
}
%>
        	<font class="label">Data Iscrizione :</font>
<%
if (fascicolo.getDataIscrizione() != null) {
%>
          	<font class="campo">
            	<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIscrizione(),"dd-MM-yyyy"))%>
          	</font>
<%
} else {
%>
          	-
<%
}
%>

<%-- inizio modifica marzo 2010 --%>
        	<font class="label"> Data Arrivo Atto :</font>
<%
if (fascicolo.getDataArrivoAtto() != null) {
%>
          	<font class="campo">
            	<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataArrivoAtto(),"dd-MM-yyyy"))%>
          	</font>
<%
} else {
%>
          	-
<%
}
/* fine modifica marzo 2010 */
if (UtenteConnesso.getUfficioUtente().isUfficioDiCompetenza(fascicolo.getChiaveUfficio())) {
// Visualizza l'istruttoria Aperta SOLO se Fascicolo di competenza. Se non di competenza
// sono consultabili solo le istruttorie chiuse
	if (IstruttoriaCumuloAperta.getIdIstruttoriaCumulo() != null) {
%>
			<font class="label" style="color:red">Aperta Istruttoria Cumulo N. :</font>
        	<a class="cliccabile" href="/jsp/Main.jsp?Action=siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=IstruttoriaCumuloAperta.getIdIstruttoriaCumulo()%>" title="Istruttoria">
          		<%=IstruttoriaCumuloAperta.getAnnoProtocollo()%>
          		/
          		<%=IstruttoriaCumuloAperta.getNumProtocollo()%>
        	</a>&nbsp;
<%
	}
}
%>
		</td>
	</tr>
<%
// Ticket#20200525017 - archiviazione - versione 12.x: se archiviato (01) non faccio vedere il msg
if (fascicolo.getCodStatoFascicolo() != null && !fascicolo.getCodStatoFascicolo().equals("01")) {
	// MEV26 Cumulo - Se fascicolo trasmesso x competenza x cumulo
	if (LastEveTrasm.getIdEvento() != null) {
		String strAlertCumulo = "";
%>
	<tr>
      	<td class="L">
<% 
		if ("0340".equals(LastEveTrasm.getCodMotivo()) // Atti per Competenza per Emissione Cumulo
				|| "5403".equals(LastEveTrasm.getCodMotivo())) { // Emissione Cumulo per Revoca Beneficio
			strAlertCumulo += "Attenzione! Il procedimento risulta trasmesso in data " + DateUtils.getDateToString(LastEveTrasm.getDataTrasmissioneAtti(),"dd-MM-yyyy");
	       	strAlertCumulo += " a " + CompetenzaCumulo.getDescrTipoAutoritaComp() + " di " + CompetenzaCumulo.getDescrLuogoAutoritaComp();
	       	strAlertCumulo += " per assorbimento in cumulo";
	       	if (CompetenzaCumulo.getChiaveAnno() != null) {
	         	strAlertCumulo += " sul procedimento " + CompetenzaCumulo.getChiaveAnno() + "/" + CompetenzaCumulo.getChiaveProgr();
	       	}
	       	strAlertCumulo += ".";
%>
			<font color="red"> Procedimento trasmesso per assorbimento in cumulo il <%=StringUtils.toStringJSP(DateUtils.getDateToString(LastEveTrasm.getDataTrasmissioneAtti(),"dd-MM-yyyy"))%>
          	a <%=StringUtils.toStringJSP(CompetenzaCumulo.getDescrTipoAutoritaComp())%> di <%=StringUtils.toStringJSP(CompetenzaCumulo.getDescrLuogoAutoritaComp())%>
<%
				if (FascCompetenteCumulo.getIdFascicoloSiep() != null) {
%>
        	(titolo che determina la competenza: 
        	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=FascCompetenteCumulo.getIdFascicoloSiep()%>" title="Procedimento Competente al Cumulo">
          	<%=StringUtils.toStringJSP(FascCompetenteCumulo.getChiaveAnno())%>
          	/
          	<%=StringUtils.toStringJSP(FascCompetenteCumulo.getChiaveProgr())%>
        	</a>
        	)
<%
				} else if (CompetenzaCumulo.getChiaveAnno() != null) {
%>
        	(titolo che determina la competenza: <%=CompetenzaCumulo.getChiaveAnno() + "/" + CompetenzaCumulo.getChiaveProgr()%>)
<%
				}
%>
        	</font>
<%
		} else if ("5202".equals(LastEveTrasm.getCodMotivo())) { // Esito Trasmissione Atti per Competenza (ex artt. 663 e 665 comma 4 c.p.p.)
			strAlertCumulo += "Attenzione! Il procedimento risulta " + AnnotazioneEsitoCumulo.getDescrEsito() + " in data " + DateUtils.getDateToString(AnnotazioneEsitoCumulo.getDataEsito(),"dd-MM-yyyy");
	       	strAlertCumulo += " da " + AnnotazioneEsitoCumulo.getDescrTipoUfficioEsito() + " di "+AnnotazioneEsitoCumulo.getDescrComuneUfficioEsito();
	      	//  strAlertCumulo += " per assorbimento in cumulo";
	       	if (AnnotazioneEsitoCumulo.getChiaveAnno() != null) {
	          	strAlertCumulo += " sul procedimento " + AnnotazioneEsitoCumulo.getChiaveAnno() + "/" + AnnotazioneEsitoCumulo.getChiaveProgr();
	       	}
	       	strAlertCumulo +=".";        
%>
        	<font color="red"> Procedimento <%=AnnotazioneEsitoCumulo.getDescrEsito()%> il <%=StringUtils.toStringJSP(DateUtils.getDateToString(AnnotazioneEsitoCumulo.getDataEsito(),"dd-MM-yyyy"))%>
          	da <%=AnnotazioneEsitoCumulo.getDescrTipoUfficioEsito()%> di <%=AnnotazioneEsitoCumulo.getDescrComuneUfficioEsito()%>
<%
			if (FascCompetenteCumulo.getIdFascicoloSiep() != null) {
%>
        	(titolo che determina la competenza:
       		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=FascCompetenteCumulo.getIdFascicoloSiep()%>" title="Procedimento Competente al Cumulo">
          	<%=StringUtils.toStringJSP(FascCompetenteCumulo.getChiaveAnno())%>/<%=StringUtils.toStringJSP(FascCompetenteCumulo.getChiaveProgr())%>
        	</a>
        	)
<%
			}
%>
        	</font>
<%
		} else if ("1040".equals(LastEveTrasm.getCodMotivo())) { // Iscritto in Istruttoria PROPRIO UFFICIO
			strAlertCumulo += "Attenzione! Il procedimento risulta " + AnnotazioneEsitoCumulo.getDescrEsito() + " in data " + DateUtils.getDateToString(AnnotazioneEsitoCumulo.getDataEsito(),"dd-MM-yyyy");
	       	// strAlertCumulo += " da "+AnnotazioneEsitoCumulo.getDescrTipoUfficioEsito()+" di "+AnnotazioneEsitoCumulo.getDescrComuneUfficioEsito();
	       	strAlertCumulo += " per assorbimento in cumulo";
	       	if (AnnotazioneEsitoCumulo.getChiaveAnno() != null) {
				strAlertCumulo += " sul procedimento " + AnnotazioneEsitoCumulo.getChiaveAnno() + "/" + AnnotazioneEsitoCumulo.getChiaveProgr();
	       	}
	       	strAlertCumulo +=".";
%>
        	<font color="red"> Procedimento <%=AnnotazioneEsitoCumulo.getDescrEsito()%> per assorbimento in cumulo il <%=StringUtils.toStringJSP(DateUtils.getDateToString(AnnotazioneEsitoCumulo.getDataEsito(),"dd-MM-yyyy"))%>
<%
			if (FascCompetenteCumulo.getIdFascicoloSiep() != null) {
%>
        	(titolo che determina la competenza:
        	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=FascCompetenteCumulo.getIdFascicoloSiep()%>" title="Procedimento Competente al Cumulo">
          	<%=StringUtils.toStringJSP(FascCompetenteCumulo.getChiaveAnno())%>
          	/
          	<%=StringUtils.toStringJSP(FascCompetenteCumulo.getChiaveProgr())%>
        	</a>
        	)
<%
			}
%>
        	</font>
<%
		}
%>
		</td>
	</tr>
<script language="JavaScript">
<%
if (!"01".equals(fascicolo.getCodStatoFascicolo())
		&& (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio()))) {
	// Messaggio visualizzato solo se di competenza
%>
	alert("<%=strAlertCumulo%>");
<%
}
%>  
</script>
<%
	} // END MEV26
} // FINE Ticket#20200525017
//==============================================================================
//                                 SOGGETTO
//==============================================================================
String colspanSoggetto = "2";
if (etichettaEta != null && !"".equals(etichettaEta)) {
	colspanSoggetto = "1";
}
%>
	<tr>
		<td class="L" colspan="<%=colspanSoggetto%>"><font class="label">Soggetto:</font>
      		<font class="campo">
	       		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>" title="Soggetto">
	          		<%=StringUtils.toStringJSP(soggetto.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(soggetto.getNome())%>
	        	</a>
      		</font>&nbsp;
<%
if (soggetto.getDataNascita() == null) {
	if (soggetto.getDataNascitaPresunta().equals("S")) {
    	if (soggetto.getSesso().compareTo("F") == 0) {
%>
      		<font class="label">nata il :</font>&nbsp;
<%
    	} else {
%>
      		<font class="label">nato il :</font>&nbsp;
<%
    	}
    	if (soggetto.getAnnoNascita() != null) {
%>
        	<font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
<%
    	} else {
%>
			<font class="campo">**-**-****</font>&nbsp;
<%    	
   		}	
	} else if (soggetto.getEtaPresuntaAnni() != null || soggetto.getEtaPresuntaMesi() != null ) {
%>
      		<font class="label">Eta' Presunta: </font>
<%
		if (soggetto.getEtaPresuntaAnni() != null) {
%>
			anni <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaAnni())%>&nbsp;</font>
<%
		}
		if (soggetto.getEtaPresuntaMesi() != null) {
%>
			mesi <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaMesi())%>&nbsp;</font>
<%			
		}
	} else {
%>
      		<font class="campo">**-**-****</font>&nbsp;
<%
	}
} else {
    if (soggetto.getSesso().compareTo("F") == 0) {
%>
      		<font class="label">nata il :</font>&nbsp;
<%
	} else {
%>
      		<font class="label">nato il :</font>&nbsp;
<%
    }
%>
      		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
<%
} // chiude else presenza data nascita
%>
			<font class="label">in : </font>
			<font class="campo">
<%
if (soggetto.getDescrComuneNascita().compareTo("-") == 0) {
%>
      			<%=soggetto.getDescComuneNascitaEstero()%> (<%=soggetto.getDescrStatoNascita().toUpperCase()%>)
<%
} else {
%>
        		<%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)
<%
}
%>
      		</font>
      		<font class="label">Codice CUI : </font>
      		<font class="campo"> <%=StringUtils.toStringJSP(soggetto.getCodAfis())%></font>&nbsp;&nbsp;&nbsp;
		</td>
<%
if ("1".equals(colspanSoggetto)) {
%>
    	<td class="L" align="right">
    		<%=etichettaEta%>
<%
	if ("SI".equals(oscuraEta)) {
%>
         	<a href="Javascript:disattiva('siap.siep.fascicolo.action.ActModificaFascicolo&oscuraEtichettaMinore=yes','<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>','<%=fascicolo.getIdFascicoloSiep()%>');">
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
<%--
==============================================================================
                               SENTENZA
==============================================================================
--%>
	<tr>
      	<td class="L" colspan="2">
        	<font class="label"><%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:<font class="label"> N.</font>
<%
if (fascicolo != null && fascicolo.getChiaveProgr() != null
		&& (fascicolo.getChiaveProgr().intValue() >= 40000
		&& fascicolo.getChiaveProgr().intValue() < 50000) 
		&& (sentenza!=null && sentenza.getCodTipoProvvedimento() != null)
		&& (sentenza.getCodTipoProvvedimento().compareTo("02") == 0
			|| sentenza.getCodTipoProvvedimento().compareTo("03") == 0)) {
%>    
			<font class="campo">
				<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioDatiProvvedimentoMSFuoriSent&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
					<%=StringUtils.toStringJSP(sentenza.getAnnoSentenza())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroSentenza())%>
          		</a>&nbsp;
<%
} else {
%>
        	<font class="campo">
        		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
        			<%=StringUtils.toStringJSP(sentenza.getAnnoSentenza())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroSentenza())%>
          		</a>&nbsp;
<%
}
%>        	
          	</font>
          	<font class="label">del</font>&nbsp;
            	<%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>
        	</font>
<%
if (!sentenza.getCodTipoProvvedimento().equals("02")) {
%>
			&nbsp;<font class="label"> Emessa da: </font>
<% 
} else {
%>
			&nbsp;<font class="label"> Emesso da: </font>
<%
}
%>
        	<font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%></font>&nbsp;
<%
if (sentenza.getNumSezioneAutoritaEmittente() != null) {
%>
			<font class="label">(Sez.</font> <font class="campo"><%=StringUtils.toStringJSP(sentenza.getNumSezioneAutoritaEmittente())%> </font> <font class="label">) </font>
<%
}
%>
        	<font class="label"> di </font>
        	<font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%></font>
<%
if (sentenza.getAnnoRegeGip() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeGip())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeGip())%></font>
			<font class="label"> GIP) </font>
<%
} else {
	if (sentenza.getAnnoRegeDib() != null) {
%>
          	&nbsp;<font class="label"> (N.Reg.Gen. </font>
          	<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeDib())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeDib())%></font>
          	<font class="label"> DIB) </font>
<%
			// MEV_66: aggiunte quattro nuove proprietà
	} else if (sentenza.getAnnoRegeGup() != null) {
%>
          	&nbsp;<font class="label"> (N.Reg.Gen. </font>
          	<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeGup())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeGup())%></font>
          	<font class="label"> GUP) </font>
<%
	} else if (sentenza.getAnnoRegeCapsm() != null) {
%>
	          	&nbsp;<font class="label"> (N.Reg.Gen. </font>
	          	<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCapsm())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCapsm())%></font>
	          	<font class="label"> CAPSM) </font>
<%
	// MEV_66: aggiunti anche CAS, CAP e CASAP
	} else if (sentenza.getAnnoRegeCap() != null) {
%>
				&nbsp;<font class="label"> (N.Reg.Gen. </font>
				<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCap())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCap())%></font>
				<font class="label"> CAP) </font>
<%
	} else if (sentenza.getAnnoRegeCas() != null) {
%>
				&nbsp;<font class="label"> (N.Reg.Gen. </font>
				<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCas())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCas())%></font>
				<font class="label"> CAS) </font>
<%
	} else if (sentenza.getAnnoRegeCasap() != null) {
%>
				&nbsp;<font class="label"> (N.Reg.Gen. </font>
				<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCasap())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCasap())%></font>
				<font class="label"> CASAP) </font>
<%
	}
}
%>
		</td>
	</tr>
<%--
==============================================================================
                              DATA IRREVOCABILITA'/NOTE
==============================================================================
--%>
<%
if (fascicolo.getDataIrrevocabilita() != null) {
%>
	<tr>
      	<td class="L" colspan="2">
<% // paolo cherubini 05/01/2011 
	if (!sentenza.getCodTipoProvvedimento().equals("02")) {
%>
      		<font class="label">Data irrevocabilita' : </font>
<% 
	} else {
%>
      		<font class="label">Esecutivo il : </font>
<%    
    }
%>
        	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "dd-MM-yyyy"))%></font>
		</td>
	</tr>
<%
}
%>
</table>

<table width="95%">
<%--
Ambros 25/03/2009  Collegamento Classi I e VII 
  ==============================================================================
       Richieste Conversione e Procedimenti di Classe VII
  ==============================================================================
--%>
	<tr>
<%
int lFascProg = fascicolo.getChiaveProgr().intValue();
if (lFascProg > 70000 && lFascProg < 80000 && fascicoloCollMod.getChiaveProgr() != null) {
%>
		<td class="L" colspan=1>
			<font class="label">Collegato al Procedimento: N.</font>
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicoloCollMod.getIdFascicoloSiep()%>" title="Procedimento">
				<%=StringUtils.toStringJSP(fascicoloCollMod.getChiaveAnno())%>/
                <%=StringUtils.toStringJSP(fascicoloCollMod.getChiaveProgr())%>
			</a>
		</td>
<%
}
if (lFascProg > 70000 && lFascProg < 80000 && richiestaconversione.getIdRichiestaConversione() !=null) {
	String lDescrLink = "Dettaglio Conversione Pena Pecuniaria";
%>
		<td align="right">
			<font class="cRosso">
				<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penapecuniaria.action.ActLoadDettaglioRichiestaConversione&<%=ICostantiPenaPecuniaria.CAMPO_ID_RICHIESTA_CONVERSIONE%>=<%=richiestaconversione.getIdRichiestaConversione()%>">
					<%=lDescrLink%>
	            </a>
			</font>
		</td>
<%
}
// ANNA per associazione a Reg. Istanza
Iterator itxReg = registroIstanzaCollMod.iterator();
int nRegColl = 0;
while (itxReg.hasNext()) {
	nRegColl++;
    FascicoloSiepModel regIstCollMod = (FascicoloSiepModel) itxReg.next();
    // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("MODEL PERVENUTO ALLA JSP: " + regIstCollMod);
  	if (regIstCollMod.getChiaveProgr() != null
  			&& regIstCollMod.getChiaveProgr().intValue() > 90000
  			&& regIstCollMod.getChiaveProgr().intValue() < 100000) {
		if (nRegColl==1) {
%>
	<tr>
		<td class="L" colspan=1>
			<font class="label">Collegato al Registro Istanza: N.</font>
<%
		}  else {
%>
			<font class="label"> , </font>
<%
		} 
%>
				<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=regIstCollMod.getIdFascicoloSiep()%>" title="Procedimento">
	                <%=StringUtils.toStringJSP(regIstCollMod.getChiaveAnno())%>/
	                <%=StringUtils.toStringJSP(regIstCollMod.getChiaveProgr())%>
              	</a>
<%
		if (!itxReg.hasNext()) {
%>
		</td>
</tr>
<%
		}
	} // Chiusura If 
} // Chiusura While 
// fine ANNA  
%>
</table>

<table cellspacing="0" cellpadding="0" width="95%">
<%
int Conta = 0;
FascicoloSiepModel lFasciMod = new FascicoloSiepModel();
Iterator itx = vectfasc.iterator();
while (itx.hasNext()) {
	lFasciMod = (FascicoloSiepModel)itx.next();
	if (lFasciMod.getChiaveProgr().intValue() > 70000 && lFasciMod.getChiaveProgr().intValue() < 80000) {
		Conta = Conta + 1;
    }
}
if (Conta > 0) {
%>
	<tr>
		<td class="L">
			<font class="label">Collegato al Procedimento: N.</font>
<%            
	lFasciMod = new FascicoloSiepModel();
	itx = vectfasc.iterator();
    while (itx.hasNext()) {
		lFasciMod = (FascicoloSiepModel)itx.next();
      	if (lFasciMod.getChiaveProgr().intValue() > 70000 && lFasciMod.getChiaveProgr().intValue() < 80000) {
%>
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFasciMod.getIdFascicoloSiep()%>" title="Procedimento">
                <%=StringUtils.toStringJSP(lFasciMod.getChiaveAnno())%>/
                <%=StringUtils.toStringJSP(lFasciMod.getChiaveProgr())%>
           	</a>
           	<!-- 26/01/2015	Visualizzazione quantum Richiesta conversione. -->
<%
			if (richiestaconversione != null
					&& (richiestaconversione.getImportoMulta() != null
					&& richiestaconversione.getImportoMulta().compareTo(new BigDecimal(0)) != 0)
					|| (richiestaconversione.getImportoAmmenda() != null
					&& richiestaconversione.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0)) {
%>
			<font class="label">&nbsp;&nbsp;&nbsp;(<%=lDescPena3%></font>
<%
				boolean existMulta = false;
				if (richiestaconversione.getImportoMulta() != null
						&& richiestaconversione.getImportoMulta().compareTo(new BigDecimal(0)) != 0) {
					existMulta = true;
%>
			<font class="label">Multa</font>&nbsp;
			<font class="campo"><%=StringUtils.toEuroFormat(richiestaconversione.getImportoMulta())%></font>&nbsp;&euro;
<%
				}
        		if (richiestaconversione.getImportoAmmenda() != null
        				&& richiestaconversione.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0) {
        			if (existMulta) {
%>
			&nbsp;&nbsp;&nbsp;
<%
        			}
%>      
   			<font class="label">Ammenda</font>&nbsp;
   			<font class="campo"><%=StringUtils.toEuroFormat(richiestaconversione.getImportoAmmenda())%></font>&nbsp;&euro;)
<%          	} else {
%>
			)
<%
}
			}
%>
		</td>
<%
		}
	}
%>
	</tr>
<%    
}
%>
<!--  MISURE di SICUREZZA - Collegamento tra classi I e IV -->
<%
if (vecFascicoloIV.size() > 0) {
	Iterator iteIV = vecFascicoloIV.iterator();
    while (iteIV.hasNext()) {
		FascMsToFascSiepModel FascMSMod = (FascMsToFascSiepModel) iteIV.next();
%>
	<tr>
		<td class="L" colspan=1>
<%
		if (ICostantiMisuraSicurezza.COD_TIPO_RELAZIONE_MS_ISCRITTO_AL.equals(FascMSMod.getCodTipoRelazioneMS())) {
%>
            <font class="label">Iscritto al procedimento Misure di Sicurezza: N.</font>
<%
			if (FascMSMod.getFasSieIdFascicoloCollegato() != null) {
%>
            <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=FascMSMod.getFasSieIdFascicoloCollegato()%>" title="Procedimento">
				<%=StringUtils.toStringJSP(FascMSMod.getChiaveAnnoSiepCollegato() )%>/
				<%=StringUtils.toStringJSP(FascMSMod.getChiaveProgrSiepCollegato() )%>
			</a>
<%
			} else {
%>
            <font class="campo"><%=StringUtils.toStringJSP(FascMSMod.getChiaveAnnoSiepCollegato() )%>/<%=StringUtils.toStringJSP(FascMSMod.getChiaveProgrSiepCollegato() )%></font>
<%
			}
			if (!UtenteConnesso.getUfficioUtente().getCodUfficio().equals(FascMSMod.getChiaveUfficioSiepCollegato())) {
%>
            <font class="label"><%=FascMSMod.getDescTipoUfficioSiepCollegato() + " DI " + FascMSMod.getDescComuneUfficioSiepCollegato()%></font>
<%
			} else {
%>
            <font class="label"> (di questo Ufficio) </font>
<%
			}
		} else {
%>
            <font class="label">Collegato al Procedimento: N.</font>
            <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=FascMSMod.getFasSieIdFascicoloCollegato()%>" title="Procedimento">
              	<%=StringUtils.toStringJSP(FascMSMod.getChiaveAnnoSiepCollegato() )%>/
              	<%=StringUtils.toStringJSP(FascMSMod.getChiaveProgrSiepCollegato() )%>
			</a>
<%
			if (!UtenteConnesso.getUfficioUtente().getCodUfficio().equals(FascMSMod.getChiaveUfficioSiepCollegato())) {
%>
            <font class="label"><%=FascMSMod.getDescTipoUfficioSiepCollegato() + " DI " + FascMSMod.getDescComuneUfficioSiepCollegato()%></font>
<%
			} else {
%>
            <font class="label"> (di questo Ufficio) </font>
<%
			}
		}
%>
		</td>
	</tr> 
<%
	}
}
//  ==============================================================================
//            STATO PROCEDIMENTO E STATO FASCICOLO                     
//  ==============================================================================
List lListStatProc = dettagliofascicolo.getStatoProcedimento();
if (lListStatProc != null && lListStatProc.size() != 0) {
%>
	<tr>
		<td class="L" colspan="2">
			<jsp:include page="/jsp/files/siap/siep/statoprocedimento/IncludeStatoProcedimento.jsp"/>
      	</td>
	</tr>
<%
} else {
	if (fascicolo.getDescrStatoFascicolo() != null) {
%>
	<tr>
		<td class="L" colspan="2">
          	<font class="label">Stato Procedimento : </font>          
          	<font color=red><%=StringUtils.toStringJSP(fascicolo.getDescrStatoFascicolo())%></font>
		</td>
	</tr>
<%
	}
}
//==============================================================================
// CUMULO
//==============================================================================
if (fascicolo!= null && fascicolo.getCodStatoFascicolo() != null && fascicolo.getCodStatoFascicolo().equals("01")
  		&& fascicolo.getCodMotivoArchiviazione() != null && fascicolo.getCodMotivoArchiviazione().equals("01")) {
	if (!UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getCodUfficioUnione())) {
%>
	<tr>
		<td class="L">
		  <font class="label">Data Cumulo : </font>
		  <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataUnione(),"dd-MM-yyyy"))%></font>
			<font class="label">Ufficio : </font>
			<font class="campo"><%=StringUtils.toStringJSP(fascicolo.getDescrTipoUfficioUnione())%> di <%=StringUtils.toStringJSP(fascicolo.getDescrComuneUfficioUnione())%></font>
			<font class="label">N.Siep : </font>
			<font class="campo"><%=StringUtils.toStringJSP(fascicolo.getAnnoFascicoloUnione())%>/<%=StringUtils.toStringJSP(fascicolo.getNumFascicoloUnione())%></font>
		</td>
	</tr>
<%
  	}
}
Set<String> elencoPosGiurAltraCausa = new HashSet<String>();
elencoPosGiurAltraCausa.add("07");
elencoPosGiurAltraCausa.add("74");
elencoPosGiurAltraCausa.add("75");
elencoPosGiurAltraCausa.add("76");
elencoPosGiurAltraCausa.add("77");
elencoPosGiurAltraCausa.add("78");
elencoPosGiurAltraCausa.add("79");
elencoPosGiurAltraCausa.add("80");
elencoPosGiurAltraCausa.add("81");
//==============================================================================
// POSIZIONE GIURIDICA
//==============================================================================
if (dettagliofascicolo.getPosizioneGiuridica() != null) {
%>
<%
	// Se detenuto altra causa, visualizzo Istituito di detenzione o Indirizzo
	if (fascicolo.getFlagAltraCausa() != null && fascicolo.getFlagAltraCausa().equals("S")
			&& (dettagliofascicolo.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("10") // libero
			// || dettagliofascicolo.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("07") // libero
			// libero - altra causa
			|| elencoPosGiurAltraCausa.contains(dettagliofascicolo.getPosizioneGiuridica().getCodPosizioneGiuridica()))) {
%>
	<tr>
    	<td class="L">
      		<font class="label">Posizione Giuridica : </font>&nbsp;
			<font color=red><%=StringUtils.toStringJSP(dettagliofascicolo.getPosizioneGiuridica().getDescrPosizioneGiuridica())%></font>
		</td>
	</tr>
<%
		if (dettagliofascicolo.getAltraCausa() != null) {
			if (dettagliofascicolo.getAltraCausa().getIstDetIdIstitutoDetenzione() != null) {
%>
	<tr>
		<td class="L">
			<font class="label">Tipo Istituto : </font>
			<font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getAltraCausa().getIstitutoDetenzione().getDescrTipoIstituto())%></font>
			<font class="label">Luogo Detenzione</font>
			<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
			<%--font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getAltraCausa().getIstitutoDetenzione().getDescrComune())%></font--%>
			<font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getAltraCausa().getIstitutoDetenzione().getDescrizione())%></font>
			<font class="label">Indirizzo </font>
			<font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getAltraCausa().getIstitutoDetenzione().getIndirizzo())%></font>
		</td>
	</tr>
<%
          	} else if (dettagliofascicolo.getAltraCausa().getAltroLuogo() != null
          			&& !dettagliofascicolo.getAltraCausa().getAltroLuogo().equals("")) {
%>
	<tr>
	 	<td class="L">
		    <font class="label">Indirizzo :</font>
		    <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getAltraCausa().getAltroLuogo())%></font>
	  	</td>
	</tr>
<%
			}
		}
	} else {
        //===================================
        // Non detenuto altra causa
        //===================================
%>
	<tr>
    	<td class="L">
      		<font class="label">Posizione Giuridica : </font>&nbsp;
			<font color=red><%=StringUtils.toStringJSP(dettagliofascicolo.getPosizioneGiuridica().getDescrPosizioneGiuridica())%></font>
		</td>
	</tr>
<%
		if (dettagliofascicolo.getLuogoDetenzione() != null) {
			if (dettagliofascicolo.getLuogoDetenzione().getIstDetIdIstitutoDetenzione() != null
					&& !dettagliofascicolo.getLuogoDetenzione().getIstDetIdIstitutoDetenzione().equals("")) {
%>
	<tr>
		<td class="L">
			<font class="label">Tipo Istituto : </font>
			<font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%></font>
			<font class="label">Luogo Detenzione</font>
			<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
			<%--font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%></font--%>
			<font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getIstitutoDetenzione().getDescrizione())%></font>
			<font class="label">Indirizzo :</font>
			<font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getIstitutoDetenzione().getIndirizzo())%></font>
		</td>
	</tr>
<%
			} else if (dettagliofascicolo.getLuogoDetenzione().getAltroLuogo() != null
					&& !dettagliofascicolo.getLuogoDetenzione().getAltroLuogo().equals("")) {
%>
	<tr>
	  	<td class="L">
		    <font class="label">Indirizzo :</font>
		    <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getAltroLuogo())%></font>
	 	</td>
	</tr>
<%
			}
		}
	}
} // end if (dettagliofascicolo.getPosizioneGiuridica() != null)
if (fascicolo.getCodTipoPosLibero().equals("I")) {
%>
	<tr>
	  	<td class="L">
	    	<font class="campo">Irreperibile</font>
	  	</td>
	</tr>
<%
}

//==============================================================================
//  LISTA AVVOCATI
//==============================================================================
List lListaAvvocati = new Vector();
lListaAvvocati=dettagliofascicolo.getAvvocati();
boolean isAvvocatoForoSoppresso = false;
String strAlertAvvocato = "";
int contaSoppressi = 0;
if (lListaAvvocati.size() > 0) {
	Collection listaFori = DecodificheManager.getInstance().getForoAll();
  	for (int i = 0; i < lListaAvvocati.size(); i++) {
	    AvvocatoModel lAvvocatoModel=(AvvocatoModel)lListaAvvocati.get(i);
	    String attributeForo = "";
	    String lSoppresso = "";
    	if (!"01".equals(fascicolo.getCodStatoFascicolo())
    			&& (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio()))) {
      		// Il controllo viene effettuato SOLO se procedimento di competenza e NON archiviato    
     		String lStatoForo = DecodificheUtils.getCodAltebyCode(listaFori, lAvvocatoModel.getForo());      
      		if ("SOPPRESSO".equals(lStatoForo)) {
				isAvvocatoForoSoppresso = true;
				lSoppresso = " (soppresso) ";
				attributeForo = "foroSoppresso='S'";
				contaSoppressi++;
				strAlertAvvocato+= " L'Avvocato "+StringUtils.toStringJSP(lAvvocatoModel.getCognome()) + " "
				                   + StringUtils.toStringJSP(lAvvocatoModel.getNome())
				                   + " risulta iscritto al Foro di "
				                   + StringUtils.toStringJSP(lAvvocatoModel.getForo())
				                   + " soppresso a seguito dell'accorpamento degli uffici giudiziari.";
      		}
   		}
%>
	<tr>
		<td class="L">
			<font class="label">Avvocato :</font>
			<font class="campo"><%=StringUtils.toStringJSP(lAvvocatoModel.getCognome())%></font>&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(lAvvocatoModel.getNome())%></font>
			<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
			<font class="label" <%=attributeForo%> >Foro : </font><font class="campo" <%=attributeForo%> ><%=StringUtils.toStringJSP(lAvvocatoModel.getForo())%>&nbsp;<%=lSoppresso%></font>&nbsp;
			<font class="label">Indirizzo :</font>
			<font class="campo"><%=StringUtils.toStringJSP(lAvvocatoModel.getIndirizzo())%></font>&nbsp;
			<font class="label">Luogo Studio :</font>
			<font class="campo"><%=StringUtils.toStringJSP(lAvvocatoModel.getDescComuneResidenza())%></font>&nbsp;
			<font class="label">Tipo Avvocato :</font>
			<font class="campo"><%=StringUtils.toStringJSP(lAvvocatoModel.getDescrTipo())%></font>&nbsp;
		</td>
	</tr>
<%
  	}
}
if (isAvvocatoForoSoppresso) {
	if (contaSoppressi == 1)
		strAlertAvvocato += "Prima di procedere con l'emissione di nuovi provvedimenti è necessario provvedere ad aggiornare i dati dell'Avvocato utilizzando le opportune funzioni.";
    else
      	strAlertAvvocato += "Prima di procedere con l'emissione di nuovi provvedimenti è necessario provvedere ad aggiornare i dati degli Avvocati utilizzando le opportune funzioni.";
}
%>
<script language="JavaScript">
<%
if (isAvvocatoForoSoppresso) {
%>
function blinkForo() {
  	var blinks = document.getElementsByTagName('font');
  	for (var i = blinks.length - 1; i >= 0; i--) {
    	var s = blinks[i];
    	if (s.getAttribute("foroSoppresso")=="S")
      		s.style.backgroundColor  = (s.style.backgroundColor == '') ? '#FFFF00' : '';
  	}
  	window.setTimeout(blinkForo, 1000);  
}
	if (document.addEventListener)
		document.addEventListener("DOMContentLoaded", blinkForo, false);
	else if (window.addEventListener)
		window.addEventListener("load", blinkForo, false);
	else if (window.attachEvent)
		window.attachEvent("onload", blinkForo);
	else
		window.onload = blinkForo;
	alert("Attenzione!! <%=strAlertAvvocato%>");
<%
}
%>
</script>

<%-- MEV_2023-33: aggiunto link cliccabile --%>
<%
if (existCivilmenteObbligato) {
%>
	<tr>
		<td class="L">
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.pagoPA.action.ActDettaglioCivilmenteObbligato&IdFascicoloSiep=<%=fascicolo.getIdFascicoloSiep()%>"
					title="Civilmente Obbligati al Pagamento Pena Pecuniaria">
				Civilmente Obbligati al Pagamento Pena Pecuniaria
			</a>
		</td>
	</tr>
<%
}
%>
<%-- FINE MEV_2023-33 --%>

<%
//==============================================================================
// PENA IRROGATA IN SENTENZA (solo se non cumulante)
//==============================================================================
if (!"S".equals(fascicolo.getFlagCumulante())) {
	if (dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva() != null) {
    	PenaComplessivaSanzioneSostitutivaModel lPenaSostMod=dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva();
    	if (lPenaSostMod != null) {
      		PenaComplessivaModel lPenCompMod = lPenaSostMod.getPenaComplessiva();
     		if (lPenCompMod != null) {
%>
	<tr>
		<td class="L">
          	<!--font class="label">Pena irrogata in sentenza :</font> -->
          	<font class="label"><%=lDescPena1%></font>
<%
				if ((lPenCompMod.getNumAnniReclusione() != null
						&& lPenCompMod.getNumAnniReclusione().compareTo(new BigDecimal(0)) != 0)
						|| (lPenCompMod.getNumMesiReclusione() != null
						&& lPenCompMod.getNumMesiReclusione().compareTo(new BigDecimal(0)) != 0)
              			|| (lPenCompMod.getNumGiorniReclusione() != null
              			&& lPenCompMod.getNumGiorniReclusione().compareTo(new BigDecimal(0)) != 0)) {
%>
			<font class="campo">Reclusione</font>
			<font class="label">Anni</font>
			<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniReclusione(),"0")%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiReclusione(),"0")%></font>
			<font class="label">Giorni</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;&nbsp;&nbsp;
<%
				}
				if (lPenCompMod.getImportoMulta() != null && lPenCompMod.getImportoMulta().compareTo(new BigDecimal(0)) != 0) {
%>
          	<font class="label">Multa </font>
          	<font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoMulta())%></font>&nbsp;&euro;&nbsp;&nbsp;&nbsp;&nbsp;
<%
				}
          		if ((lPenCompMod.getNumAnniArresto() != null
          				&& lPenCompMod.getNumAnniArresto().compareTo(new BigDecimal(0)) != 0)
          				|| (lPenCompMod.getNumMesiArresto() != null
          				&& lPenCompMod.getNumMesiArresto().compareTo(new BigDecimal(0)) != 0)
          				|| (lPenCompMod.getNumGiorniArresto() != null
          				&& lPenCompMod.getNumGiorniArresto().compareTo(new BigDecimal(0)) != 0)) {
%>
			<font class="campo">Arresto</font>
			<font class="label">Anni</font>
			<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniArresto(),"0")%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiArresto(),"0")%></font>
			<font class="label">Giorni</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;&nbsp;&nbsp;
<%
				}
				if (lPenCompMod.getImportoAmmenda() != null
						&& lPenCompMod.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0) {
%>
			<font class="label">Ammenda </font>
			<font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;&nbsp;&nbsp;&nbsp;
<%
				}
				if (lPenCompMod.getCodTipoPenaDetentiva().equals("03")
						|| lPenCompMod.getCodTipoPenaDetentiva().equals("04")) {
%>
			<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getDescrTipoPenaDetentiva())%></font>
<%
					if (lPenCompMod.getCodTipoPenaDetentiva().equals("04")) {
						if (lPenCompMod.getNumAnniIsolamentoDiurno() != null) {
%>
			<font class="label">Anni</font>
			<font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniIsolamentoDiurno(),"0")%></font>
<%
						}
						if (lPenCompMod.getNumMesiIsolamentoDiurno() != null) {
%>
			<font class="label">Mesi</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiIsolamentoDiurno(),"0")%></font>
<%
						}
						if (lPenCompMod.getNumGiorniIsolamentoDiurno() != null) {
%>
			<font class="label">Giorni</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniIsolamentoDiurno(),"0")%></font>
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
} else {
	//============================================================================
	// Aggiunta nuova versione calcolo pena: viene visualizzata la pena in Cumulo
	//============================================================================  
	if (dettagliofascicolo.getPenaCumuloNew() != null) {
    	PenaRideterminataCumuloModel lPenaCumulo = dettagliofascicolo.getPenaCumuloNew();
%>
	<tr>
  		<td class="L">
    		<font class="label">Pena Irrogata in Cumulo : </font>
<%
		if (!lPenaCumulo.isReclusione() && !lPenaCumulo.isArresto() && !lPenaCumulo.isErgastolo()) {
%>
			<font color="red">Nulla residua da espiare</font>
<%
		}
		if (lPenaCumulo.isReclusione()) {
%>
			<font class="campo">Reclusione</font>
			<font class="label">Anni</font>
			<font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getNumAnniReclusione(),"0")%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumMesiReclusione(),"0")%></font>
			<font class="label">Giorni</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;&nbsp;&nbsp;
<%
		}
		if (lPenaCumulo.isMulta()) {
%>
			<font class="label">Multa </font>
			<font class="campo"><%=StringUtils.toEuroFormat(lPenaCumulo.getImportoMulta())%></font>&nbsp;&euro;&nbsp;&nbsp;&nbsp;&nbsp;
<%
		}
		if (lPenaCumulo.isArresto()) {
%>
			<font class="campo">Arresto</font>
			<font class="label">Anni</font>
			<font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getNumAnniArresto(),"0")%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumMesiArresto(),"0")%></font>
			<font class="label">Giorni</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;&nbsp;&nbsp;
<%
		}
		if (lPenaCumulo.isAmmenda()) {
%>
			<font class="label">Ammenda </font>
			<font class="campo"><%=StringUtils.toEuroFormat(lPenaCumulo.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;&nbsp;&nbsp;&nbsp;
<%
		}
		if (lPenaCumulo.isErgastolo()) {
%>
			<font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getDescrTipoPenaDetentiva())%></font>
<%
			if (lPenaCumulo.getCodTipoPenaDetentiva().equals("04")) {
				if (lPenaCumulo.getNumAnniIsolamentoDiurno() != null) {
%>
			<font class="label">Anni</font>
			<font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getNumAnniIsolamentoDiurno(),"0")%></font>
<%
				}
				if (lPenaCumulo.getNumMesiIsolamentoDiurno() != null) {
%>
			<font class="label">Mesi</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumMesiIsolamentoDiurno(),"0")%></font>
<%
				}
				if (lPenaCumulo.getNumGiorniIsolamentoDiurno() != null) {
%>
			<font class="label">Giorni</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumGiorniIsolamentoDiurno(),"0")%></font>
<%
				}
			}
		}
%>
		</td>
	</tr>
<%  
	} else if (dettagliofascicolo.getPenaCumulo() != null) {
    	PenaCumuloModel lPenaCumulo = dettagliofascicolo.getPenaCumulo();
%>
	<tr>
		<td class="L">
  			<font class="label">Pena Irrogata in Cumulo : </font>
<%
		if ((lPenaCumulo.getNumAnniReclusione() != null && lPenaCumulo.getNumAnniReclusione().compareTo(new BigDecimal(0)) != 0)
				|| (lPenaCumulo.getNumMesiReclusione() != null && lPenaCumulo.getNumMesiReclusione().compareTo(new BigDecimal(0)) !=0 )
    			|| (lPenaCumulo.getNumGiorniReclusione() != null && lPenaCumulo.getNumGiorniReclusione().compareTo(new BigDecimal(0)) != 0)) {
%>
			<font class="campo">Reclusione</font>
			<font class="label">Anni</font>
			<font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getNumAnniReclusione(),"0")%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumMesiReclusione(),"0")%></font>
			<font class="label">Giorni</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;&nbsp;&nbsp;
<%
		}
		if (lPenaCumulo.getImportoMulta() != null && lPenaCumulo.getImportoMulta().compareTo(new BigDecimal(0)) != 0) {
%>
			<font class="label">Multa </font>
			<font class="campo"><%=StringUtils.toEuroFormat(lPenaCumulo.getImportoMulta())%></font>&nbsp;&euro;&nbsp;&nbsp;&nbsp;&nbsp;
<%
		}
		if ((lPenaCumulo.getNumAnniArresto() != null && lPenaCumulo.getNumAnniArresto().compareTo(new BigDecimal(0)) != 0)
				|| (lPenaCumulo.getNumMesiArresto() != null && lPenaCumulo.getNumMesiArresto().compareTo(new BigDecimal(0)) != 0)
    			|| (lPenaCumulo.getNumGiorniArresto() != null && lPenaCumulo.getNumGiorniArresto().compareTo(new BigDecimal(0)) != 0)) {
%>
			<font class="campo">Arresto</font>
			<font class="label">Anni</font>
			<font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getNumAnniArresto(),"0")%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumMesiArresto(),"0")%></font>
			<font class="label">Giorni</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;&nbsp;&nbsp;
<%
		}
		if (lPenaCumulo.getImportoAmmenda() != null && lPenaCumulo.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0) {
%>
			<font class="label">Ammenda </font>
			<font class="campo"><%=StringUtils.toEuroFormat(lPenaCumulo.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;&nbsp;&nbsp;&nbsp;
<%
		}
		if (lPenaCumulo.getCodTipoPenaDetentiva().equals("E") || lPenaCumulo.getCodTipoPenaDetentiva().equals("I")) {
%>
			<font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getDescrTipoPenaDetentiva())%></font>
<%
			if (lPenaCumulo.getCodTipoPenaDetentiva().equals("I")) {
				if (lPenaCumulo.getNumAnniIsolamentoDiurno() != null) {
%>
			<font class="label">Anni</font>
			<font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getNumAnniIsolamentoDiurno(),"0")%></font>
<%
				}
				if (lPenaCumulo.getNumMesiIsolamentoDiurno() != null) {
%>
			<font class="label">Mesi</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumMesiIsolamentoDiurno(),"0")%></font>
<%
				}
				if (lPenaCumulo.getNumGiorniIsolamentoDiurno() != null) {
%>
			<font class="label">Giorni</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumGiorniIsolamentoDiurno(),"0")%></font>
<%
				}
			}
		}
%>
		</td>
	</tr>
<%
  	}
} // chiude ramo else

//=================================================================================================
// IMPOSSIBILITA' ESAZIONE PENA PECUNIARIA (solo classe VII)
//=================================================================================================   
if (lFascProg > 70000 && lFascProg < 80000) { 
	String descrTipoSanzione = "Richiesta conversione pena pecuniaria : ";
	if ((richiestaconversione.getImportoMulta() != null
			&& richiestaconversione.getImportoMulta().compareTo(new BigDecimal(0)) != 0)
			|| (richiestaconversione.getImportoAmmenda() != null
			&& richiestaconversione.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0)) {
%> 
	<tr>      
		<td class="L" colspan=1>
			<font class="label"><%=descrTipoSanzione %></font>
<%
		if (richiestaconversione.getImportoMulta() != null
				&& richiestaconversione.getImportoMulta().compareTo(new BigDecimal(0)) != 0) {
%> 
			<font class="label">Multa </font>
			<font class="campo"><%=StringUtils.toEuroFormat(richiestaconversione.getImportoMulta())%></font>&nbsp;&euro;&nbsp;&nbsp;&nbsp;&nbsp;
<%
		}
		if (richiestaconversione.getImportoAmmenda() != null
				&& richiestaconversione.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0) {
%> 
			<font class="label">Ammenda </font>
			<font class="campo"><%=StringUtils.toEuroFormat(richiestaconversione.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;&nbsp;&nbsp;&nbsp;
<%
		}
%>
		</td>
	</tr>
<%
	}
	//=======================================================================================
	// Pena convertita in liberta' controllata/Semidetenzione (a seconda del CodTipoSanzione)
	//=======================================================================================   
	if ((richiestaconversione.getCodTipoSanzione() != null)
			&& 	// 17/02/2016 Inizio
    			// (richiestaconversione.getCodTipoSanzione().compareTo("2470") == 0))
				(richiestaconversione.getCodTipoSanzione().compareTo("01") == 0)
				|| (richiestaconversione.getCodTipoSanzione().compareTo("02") == 0)) { // 17/02/2016 Fine
		// Descrizione Tipo Sanzione da RICHIESTA_CONVERSIONE.
		Collection lColTipoSanzione = null;
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TIPO_SANZIONE_CONVERTITA");
		lColTipoSanzione = lDecodifiche.ExRicercaDecodifiche(lModel);
		descrTipoSanzione = "Pena convertita in " + DecodificheUtils.getDescbyCode(lColTipoSanzione, richiestaconversione.getCodTipoSanzione());
%>
	<tr>      
		<td class="L" colspan=1>
			<font class="label"><%=descrTipoSanzione %></font>
<%
		if (richiestaconversione.getDurataEsitoAnni() != null
				&& richiestaconversione.getDurataEsitoAnni().compareTo(new BigDecimal(0)) != 0) {
%>              
			<font class="label">Anni </font>
			<font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getDurataEsitoAnni())%></font>&nbsp;&nbsp;&nbsp;
<%
		}
		if (richiestaconversione.getDurataEsitoMesi() != null
				&& richiestaconversione.getDurataEsitoMesi().compareTo(new BigDecimal(0)) != 0) {
%>              
			<font class="label">Mesi </font>
			<font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getDurataEsitoMesi())%></font>&nbsp;&nbsp;&nbsp;
<%
		}
		if (richiestaconversione.getDurataEsitoGiorni() != null
				&& richiestaconversione.getDurataEsitoGiorni().compareTo(new BigDecimal(0)) != 0) {
%>              
   			<font class="label">Giorni </font>
 			<font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getDurataEsitoGiorni())%></font>&nbsp;&nbsp;&nbsp;
<%
		}
%>
		</td>
	</tr>
<%
	}
	//=======================================================================================
	// Pena rateizzata tot rate di tot  (CodTipoSanzione=2471)
	//=======================================================================================   
	ScambioSanzioneModel lScambioMod = dettagliofascicolo.getScambioSanzione();
	if (lScambioMod != null
			&& lScambioMod.getIdScambioSanzione() != null
			&& lScambioMod.getCodNaturaSanzione().compareTo("0159") == 0
			&& lScambioMod.getCodTipoSanzione().compareTo("2471") == 0)	{
		// Descrizione Tipo Sanzione da RICHIESTA_CONVERSIONE.
		Collection lColTipoSanzione = null;
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TIPO_SANZIONE_CONVERTITA");
		lColTipoSanzione = lDecodifiche.ExRicercaDecodifiche(lModel);
%>
	<tr>      
		<td class="L" colspan=1>
			<font class="label">Pena rateizzata : </font>
       		<font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getNumeroRate())%></font>
			<font class="label"> rate da </font>
      		<font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getValoreRata())%></font>
			<font class="label">&euro; Ultima rata da </font>
      		<font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getValoreUltimaRata())%></font>
			<font class="label">&euro;</font>
<%
		if (richiestaconversione.getDurataEsitoAnni() != null
				&& richiestaconversione.getDurataEsitoAnni().compareTo(new BigDecimal(0)) != 0) {
%>              
			<font class="label">Anni </font>
			<font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getDurataEsitoAnni())%></font>&nbsp;&nbsp;&nbsp;
<%
		}
		if (richiestaconversione.getDurataEsitoMesi() != null
				&& richiestaconversione.getDurataEsitoMesi().compareTo(new BigDecimal(0)) != 0) {
%>              
			<font class="label">Mesi </font>
			<font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getDurataEsitoMesi())%></font>&nbsp;&nbsp;&nbsp;
<%
		}
		if (richiestaconversione.getDurataEsitoGiorni() != null
				&& richiestaconversione.getDurataEsitoGiorni().compareTo(new BigDecimal(0)) != 0) {
%>              
   			<font class="label">Giorni </font>
   			<font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getDurataEsitoGiorni())%></font>&nbsp;&nbsp;&nbsp;
<%
		}
%>
		</td>
	</tr>
<%
	}
}

//==============================================================================      
// SANZIONE SOSTITUTIVA
//==============================================================================      
PenaComplessivaSanzioneSostitutivaModel lPenComSanSost = dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva();
boolean lEsisteSanSost = false;
if (lPenComSanSost != null) {
	SanzioneSostitutivaModel lSanSos = lPenComSanSost.getSanzioneSostitutiva();
	if (lSanSos != null && lSanSos.getIdSanzioneSostitutiva() != null) {
%>
	<tr>
<%
// MEV_2023-13
if (lSanSos.isPenaSostitutiva ()) {
%>
		<td class="L">
			<font class="label">Pena sostitutiva applicata: </font>
<%
} else {
%>
		<td class="L"><font class="label">Sanzione Sostitutiva applicata: </font>
<%
}
if ((lSanSos.getNumAnni() != null && lSanSos.getNumAnni().compareTo(new BigDecimal(0)) != 0)
				|| (lSanSos.getNumMesi() != null && lSanSos.getNumMesi().compareTo(new BigDecimal(0)) != 0)
				|| (lSanSos.getNumGiorni() != null && lSanSos.getNumGiorni().compareTo(new BigDecimal(0)) != 0)) {
			lEsisteSanSost = true;
%>
			<font class="campo"><%=StringUtils.toStringJSP(lSanSos.getDescrTipoSanzione())%>&nbsp;</font>
<%
			if ("E".equals(lSanSos.getCodTipoSanzione())) {
%>
			per un periodo di
<%
			}
%>
			<font class="label">&nbsp;Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumAnni(), "0")%>&nbsp;</font>
			<font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumMesi(), "0")%>&nbsp;</font>
			<font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumGiorni(), "0")%></font>
<%
		}

		if (lSanSos.getSanzionePecuniariaMulta() != null && lSanSos.getSanzionePecuniariaMulta().intValue() != 0) {
			// MEV_2023-33: aggiunto link cliccabile per multa o ammenda
%>
<%-- 			<font class="label"> <%=StringUtils.toStringJSP(lSanSos.getDescrTipoSanzione())%> Multa&nbsp;</font> --%>
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.rateizzazionepp.action.ActLoadDettagloRateizzazione"
					title="Pena Pecuniaria Sostitutiva">
				<%=StringUtils.toStringJSP(lSanSos.getDescrTipoSanzione())%>
			</a>
			<font class="label">&nbsp;Importo&nbsp;</font>
			<font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaMulta())%>&nbsp;</font>&euro;&nbsp;&nbsp;&nbsp;
<%
		}
		
		if (lSanSos.getSanzionePecuniariaAmmenda() != null && lSanSos.getSanzionePecuniariaAmmenda().intValue() != 0) {
%>
<%-- 			<font class="label"> <%=StringUtils.toStringJSP(lSanSos.getDescrTipoSanzione())%> Ammenda&nbsp;</font> --%>
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.rateizzazionepp.action.ActLoadDettagloRateizzazione"
					title="Pena Pecuniaria Sostitutiva">
				<%=StringUtils.toStringJSP(lSanSos.getDescrTipoSanzione())%>
			</a>
			<font class="label">&nbsp;Ammenda&nbsp;</font>
			<font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaAmmenda())%>&nbsp;</font>&euro;
<%
		}
%>
		</td>
	</tr>
<%
	}
}
//==============================================================================      
//                          SANZIONE SOSTITUTIVA
//==============================================================================     
PenaResiduaModel lPenResSanzSostMod = dettagliofascicolo.getPenaResidua();
if (lPenResSanzSostMod != null && lPenResSanzSostMod.getFlagSanzioneSostitutiva() != null
		&& "S".equals(lPenResSanzSostMod.getFlagSanzioneSostitutiva())) {
%>
	<tr>
 		<td class="l">Sanzione Sostitutiva da espiare :
      		<font class="campo"><%=StringUtils.toStringJSP(lPenResSanzSostMod.getDescrTipoSanzione())%>&nbsp;</font>
			<font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lPenResSanzSostMod.getNumAnniSS(), "0")%>&nbsp;</font>
			<font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lPenResSanzSostMod.getNumMesiSS(), "0")%>&nbsp;</font>
			<font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lPenResSanzSostMod.getNumGiorniSS(), "0")%></font>
<% 
	if ((lPenResSanzSostMod.getImportoMultaSS() != null && lPenResSanzSostMod.getImportoMultaSS().intValue() != 0)
			|| (lPenResSanzSostMod.getImportoAmmendaSS() != null && lPenResSanzSostMod.getImportoAmmendaSS().intValue() != 0)) {
%>
			<font class="label"> Sanz.Pec.&nbsp;</font>
<%
		if (lPenResSanzSostMod.getImportoMultaSS() != null && lPenResSanzSostMod.getImportoMultaSS().intValue() != 0) {
%>
			<font class="campo">Multa&nbsp;<%=StringUtils.toEuroFormat(lPenResSanzSostMod.getImportoMultaSS())%>&nbsp;</font>&euro;&nbsp;&nbsp;&nbsp;
<%
		}
		if (lPenResSanzSostMod.getImportoAmmendaSS() != null && lPenResSanzSostMod.getImportoAmmendaSS().intValue() != 0) {
%>
			<font class="campo">Ammenda&nbsp;<%=StringUtils.toEuroFormat(lPenResSanzSostMod.getImportoAmmendaSS())%>&nbsp;</font>&euro;
<%
		}
	}
%>
		</td>
	</tr>
<%
}
//==============================================================================
//                                 PENA DA ESPIARE
// - Pena residua (ultima pena validata)
//   - <tr>pena da espiare</tr>
//   - <tr>decorrenza</tr>
//   - <tr>pena residua</tr>
// o
// - Pena presunta
//   - <tr>pena da espiare</tr>
//   - <tr>decorrenza</tr>
//   - <tr>pena residua</tr>
//==============================================================================
PenaResiduaModel lPenResMod=dettagliofascicolo.getPenaResidua();
if (dettagliofascicolo.getPenaResidua() != null) {
	// ultima pena validata
	// MEV_39: se fasc. è di classe IV allora no faccio vedere pena da espiare, inizio e fine pena
	if (!(lFascProg >= 40000 && lFascProg < 50000)) {
%>
	<tr>
  		<td class="L">
<%
		if (!"S".equals(lPenResMod.getFlagSanzioneSostitutiva())) {
%>  
			<font class="label"><%=lDescPena2 %></font>
<%
			if ("S".equals(lPenResMod.getFlagErgastolo())) {
%>
			<!--ERGASTOLO--->
      		<font class="campo">ERGASTOLO</font>
<%
    		} else if ("D".equals(lPenResMod.getFlagErgastolo())) {
%>
      		<font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font>
<%
				if (lPenResMod.getNumAnniIsolamentoDiurno() != null && lPenResMod.getNumAnniIsolamentoDiurno().intValue() != 0) {
%>
			<font class="label">Anni</font>
			<font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumAnniIsolamentoDiurno(),"0")%></font>
<%
      			}
     			if (lPenResMod.getNumMesiIsolamentoDiurno() != null && lPenResMod.getNumMesiIsolamentoDiurno().intValue() != 0) {
%>
			<font class="label">Mesi</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumMesiIsolamentoDiurno(),"0")%></font>
<%
      			}
      			if (lPenResMod.getNumGiorniIsolamentoDiurno() != null && lPenResMod.getNumGiorniIsolamentoDiurno().intValue() != 0) {
%>
	        <font class="label">Giorni</font>
	        <font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumGiorniIsolamentoDiurno(),"0")%></font>
<%
      			}
    		}
    		if ((lPenResMod.getNumAnniReclusione() != null && lPenResMod.getNumAnniReclusione().compareTo(new BigDecimal(0)) != 0)
    				|| (lPenResMod.getNumMesiReclusione() != null && lPenResMod.getNumMesiReclusione().compareTo(new BigDecimal(0)) != 0)
       				|| (lPenResMod.getNumGiorniReclusione() != null && lPenResMod.getNumGiorniReclusione().compareTo(new BigDecimal(0)) != 0)) { 
%>
			<font class="campo">Reclusione</font>
			<font class="label">Anni</font>
			<font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumAnniReclusione(),"0")%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumMesiReclusione(),"0")%></font>
			<font class="label">Giorni</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;
<%
    		}
    		if (lPenResMod.getImportoMulta() != null && lPenResMod.getImportoMulta().compareTo(new BigDecimal(0)) != 0) {
%>
			<font class="label">Multa </font>
			<font class="campo"><%=StringUtils.toEuroFormat(lPenResMod.getImportoMulta())%></font>&nbsp;&euro;&nbsp;&nbsp;&nbsp;&nbsp;
<%
    		}
    		if ((lPenResMod.getNumAnniArresto() != null && lPenResMod.getNumAnniArresto().compareTo(new BigDecimal(0)) != 0)
    				|| (lPenResMod.getNumMesiArresto() != null && lPenResMod.getNumMesiArresto().compareTo(new BigDecimal(0)) != 0)
       				|| (lPenResMod.getNumGiorniArresto() != null && lPenResMod.getNumGiorniArresto().compareTo(new BigDecimal(0)) != 0)) {
%>
			<font class="campo">Arresto</font>
			<font class="label">Anni</font>
			<font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumAnniArresto(),"0")%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumMesiArresto(),"0")%></font>
			<font class="label">Giorni</font>
			<font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;
<%
			}
    		if (lPenResMod.getImportoAmmenda() != null && lPenResMod.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0) {
%>
	        <font class="label">Ammenda </font>
	        <font class="campo"><%=StringUtils.toEuroFormat(lPenResMod.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;
<%
			}
%>
  		</td>
	</tr>
<%
		}
		//=============================================================================
		//  IMPOSSIBILITA ESAZIONE PEN PECUNIARIA (solo classe VII)
		//=============================================================================   
		lFascProg = fascicolo.getChiaveProgr().intValue();
		if (lFascProg > 70000 && lFascProg < 80000) { 
  			if ((richiestaconversione.getImportoMulta() != null
  					&& richiestaconversione.getImportoMulta().compareTo(new BigDecimal(0)) != 0)
  					|| (richiestaconversione.getImportoAmmenda() != null
  					&& richiestaconversione.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0)) {
%>
	<tr>      
		<td class="L" colspan=1>
          	<font class="label">Richiesta Conversione :</font>
<%
				if (richiestaconversione.getImportoMulta() != null
						&& richiestaconversione.getImportoMulta().compareTo(new BigDecimal(0)) != 0) {
%>              
            <font class="label">Multa </font>
          	<font class="campo"><%=StringUtils.toEuroFormat(richiestaconversione.getImportoMulta())%></font>&nbsp;&euro;&nbsp;&nbsp;&nbsp;&nbsp;
<%
				}
        		if (richiestaconversione.getImportoAmmenda() != null
        				&& richiestaconversione.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0) {
%>      
           	<font class="label">Ammenda </font>
           	<font class="campo"><%=StringUtils.toEuroFormat(richiestaconversione.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;&nbsp;&nbsp;&nbsp;
<%
				}
%>
		</td>
	</tr>
<%
			}
		}
		//==============================================================================
		//                            DECORRENZA PENA
		// solo data inizio e data fine (no date intermedie)
		//==============================================================================
%>
	<tr>
	  	<td class="L">
<%
		if (lPenResMod.getDataInizio() != null) {
%>
			<font class="label">Inizio Pena : </font>
			<font class="cVerde"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenResMod.getDataInizio(),"dd-MM-yyyy"))%></font>&nbsp;
<%
		}
      	PenaComplessivaSanzioneSostitutivaModel lPenaSostMod = null;
      	PenaComplessivaModel lPenCompMod = null;
      	if (dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva() != null) {
        	lPenaSostMod=dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva();
        	if (lPenaSostMod != null) {
          		lPenCompMod=lPenaSostMod.getPenaComplessiva();
        	}
      	}
      	if (lPenCompMod != null
      			&& (lPenCompMod.getCodTipoPenaDetentiva() != null
      			&& (lPenCompMod.getCodTipoPenaDetentiva().equals("03")
      					|| lPenCompMod.getCodTipoPenaDetentiva().equals("04")))) { // ERGASTOLO
%>
			<font class="label">Fine Pena : </font> <font color=red>MAI</font>
<%
		} else if (lPenResMod.getDataFine() != null
				&& lPenResMod.getDataFinePresunta() != null
              	&& (!lPenResMod.getDataFine().equals(lPenResMod.getDataFinePresunta()))) {
%>
        	<font class="label">Fine Pena : </font>
        	<font  color=red><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenResMod.getDataFine(),"dd-MM-yyyy"))%></font>
        	<input type="hidden" name="dataFinePena" value="<%=DateUtils.getDateToString(lPenResMod.getDataFine(),"dd-MM-yyyy")%>"/>
<%
      	} else {
        	if (lPenResMod.getDataFine() != null) {
%>
           	<font class="label">Fine Pena : </font>
           	<font class="cVerde"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenResMod.getDataFine(),"dd-MM-yyyy"))%></font>
           	<input type="hidden" name="dataFinePena" value="<%=DateUtils.getDateToString(lPenResMod.getDataFine(),"dd-MM-yyyy")%>"/>
<%
			}
      	}
%>
		</td>
  	</tr>
<%
	}
	// MEV_39: fine modifica
	//==============================================================================
	// PENA RESIDUA calcolata al volo tra la data di systema e il fine pena previsto
	// Se data inizio > sysdate non viene visualizzato (pena a decorrenza futura - detenuto altra causa)
	// Se data fine < sysdate non viene visualizzato (detenuto scarcerato o comunque fungibile)
	//==============================================================================
  	CalendarModel lCalMod = new CalendarModel();
	// MEV_39: replicato codice per fasc di classe IV
	PenaComplessivaSanzioneSostitutivaModel lPenaSostMod = null;
	PenaComplessivaModel lPenCompMod = null;
	if (dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva() != null) {
		lPenaSostMod = dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva();
	  	if (lPenaSostMod != null) {
	    	lPenCompMod=lPenaSostMod.getPenaComplessiva();
	  	}
	}
	// fine MEV_39
  	if (lPenCompMod != null
  			&& (lPenCompMod.getCodTipoPenaDetentiva() != null
         	&& (lPenCompMod.getCodTipoPenaDetentiva().equals("03")
         			|| lPenCompMod.getCodTipoPenaDetentiva().equals("04")))) {
    	// ERGASTOLO
    	// Nel caso id ergastolo non visualizzo la pena da espiare
	} else {
    	if (lPenResMod.getDataInizio() != null && lPenResMod.getDataFine() != null) {
    		if (!DateUtils.isGreater(lPenResMod.getDataInizio(),DateUtils.getSysDate())
    				&& (DateUtils.isGreater(lPenResMod.getDataFine(), DateUtils.getSysDate())
    						|| DateUtils.isEquals(lPenResMod.getDataFine(), DateUtils.getSysDate()))) {
    			lCalMod.setDataInizio(DateUtils.getSysDate());
        		lCalMod.setDataFine(lPenResMod.getDataFine());
        		CalendarUtil lCalUtil = new CalendarUtil();
        		lCalMod = lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod, true);
		        // Se il quantum e' >0
		        // 25-01-2007 ennesimo rework, questa volta la pena residua non viene 
		        // visualizzata, ma calcolata su richiesta non piu' tra data systema e fine pena
		        // ma con la procedura della calcolatrice: quantum residuo=quantum che bisognerebbe
		        // sottrarre per ottenere come data di scarcerazione la data odierna
		        if (lCalUtil.getTotGiorni(lCalMod) >= 0) {
%>
	<tr>
	  	<td class="L">
	    	<font class="label">Pena Residua :</font>
	    	<a href="Javascript:CalcoloResiduoPena();">Calcolo Pena Residua da Espiare ad Oggi</a>
	    	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
			<%--
			<font class="label">Anni</font>
			<font class="campo"><%=lCalMod.getNumAnni()%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=lCalMod.getNumMesi()%></font>
			<font class="label">Giorni</font>
			<font class="campo"> <%=lCalMod.getNumGiorni()%></font>&nbsp;
			--%>
		</td>
	</tr>
<%
				}
      		}
   		}
  	}
} // end pena Residua
else if (dettagliofascicolo.getPenaPresunta() != null) {
	//==============================================================================
	//                                  PENA PRESUNTA
	// - <tr>pena da espiare</tr>
	// - <tr>decorrenza</tr>
	// - <tr>pena residua</tr>
	//==============================================================================
  	PenaPresuntaModel lPenPresMod = dettagliofascicolo.getPenaPresunta();
%>
	<tr>
    	<td class="L">
      		<font class="label"><%=lDescPena2 %></font>
<%
	if ((lPenPresMod.getNumAnniReclusione() != null && lPenPresMod.getNumAnniReclusione().compareTo(new BigDecimal(0)) != 0)
          || (lPenPresMod.getNumMesiReclusione() != null && lPenPresMod.getNumMesiReclusione().compareTo(new BigDecimal(0)) != 0)
          || (lPenPresMod.getNumGiorniReclusione() != null && lPenPresMod.getNumGiorniReclusione().compareTo(new BigDecimal(0)) != 0)) {
%>
	        <font class="campo">Reclusione</font>
	        <font class="label">Anni</font>
	        <font class="campo"><%=StringUtils.toStringJSP(lPenPresMod.getNumAnniReclusione(),"0")%></font>
	        <font class="label">Mesi</font>
	        <font class="campo"> <%=StringUtils.toStringJSP(lPenPresMod.getNumMesiReclusione(),"0")%></font>
	        <font class="label">Giorni</font>
	        <font class="campo"> <%=StringUtils.toStringJSP(lPenPresMod.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;
<%
	}
	if (lPenPresMod.getImportoMulta() != null && lPenPresMod.getImportoMulta().compareTo(new BigDecimal(0)) != 0) {
%>
			<font class="label">Multa </font>
			<font class="campo"><%=StringUtils.toEuroFormat(lPenPresMod.getImportoMulta())%></font>&nbsp;&euro;&nbsp;
<%
	}
	if ((lPenPresMod.getNumAnniArresto() != null && lPenPresMod.getNumAnniArresto().compareTo(new BigDecimal(0)) != 0)
          || (lPenPresMod.getNumMesiArresto() != null && lPenPresMod.getNumMesiArresto().compareTo(new BigDecimal(0)) != 0)
          || (lPenPresMod.getNumGiorniArresto() != null && lPenPresMod.getNumGiorniArresto().compareTo(new BigDecimal(0)) != 0)) {
%>
	        <font class="campo">Arresto</font>
	        <font class="label">Anni</font>
	        <font class="campo"><%=StringUtils.toStringJSP(lPenPresMod.getNumAnniArresto(),"0")%></font>
	        <font class="label">Mesi</font>
	        <font class="campo"> <%=StringUtils.toStringJSP(lPenPresMod.getNumMesiArresto(),"0")%></font>
	        <font class="label">Giorni</font>
	        <font class="campo"> <%=StringUtils.toStringJSP(lPenPresMod.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;
<%
	}
	if (lPenPresMod.getImportoAmmenda() != null && lPenPresMod.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0) {
%>
			<font class="label">Ammenda </font>
			<font class="campo"><%=StringUtils.toEuroFormat(lPenPresMod.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;
<%
	}
%>
		</td>
	</tr>
<%
	//==============================================================================
	// Decorrenza
	//==============================================================================
%>
  	<tr>
    	<td class="L">
<%
	if (lPenPresMod.getDataInizio() != null) {
%>
			<font class="label">Inizio Pena</font>
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenPresMod.getDataInizio(),"dd-MM-yyyy"))%></font>&nbsp;
<%
	}
	if (lPenPresMod.getDataFine() != null && !lPenPresMod.getDataFine().toString().equals("")) {
%>
			<font class="label">Fine Pena</font>
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenPresMod.getDataFine(),"dd-MM-yyyy"))%></font>
<%
	}
%>
    	</td>
	</tr>
<%
    //==========================================================================
    // PENA RESIDUA calcolata al volo tra la data di systema e il fine pena
    // previsto.
    // Se data inizio > sysdate non viene visualizzato (pena a decorrenza futura - detenuto altra causa)
    // Se data fine < sysdate non viene visualizzato (detenuto scarcerato o comunque fungibile)
    //==========================================================================
    CalendarModel lCalMod = new CalendarModel();
    if (lPenPresMod.getDataFine() != null) {
      	if (!DateUtils.isGreater(lPenPresMod.getDataInizio(),DateUtils.getSysDate())
				&& DateUtils.isLower(lPenPresMod.getDataFine(),DateUtils.getSysDate())) {
	        // data inizio<=sysdate (pena in decorrenza)
	        lCalMod.setDataInizio(DateUtils.getSysDate());
	        lCalMod.setDataFine(lPenResMod.getDataFine());
	        CalendarUtil lCalUtil=new CalendarUtil();
	        lCalMod=lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod);
        	// Se il quantum e' positivo e non e' 0
        	if (lCalUtil.getTotGiorni(lCalMod) > 0) {
%>
	<tr>
		<td class="L">
			<font class="label">Pena Residua :</font>
			<font class="label">Anni</font>
			<font class="campo"><%=lCalMod.getNumAnni()%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=lCalMod.getNumMesi()%></font>
			<font class="label">Giorni</font>
			<font class="campo"> <%=lCalMod.getNumGiorni()%></font>&nbsp;
		</td>
	</tr>
<%
        	}
      	} // end if pena in decorrenza
   	} // if (lPenPresMod.getDataFine() != null)
} // chiusura ramo else
%>
<!------- Fine PENA PRESUNTA -------->
<%
//==============================================================================
// Liberazione Anticipata
//==============================================================================
%>
<!-- 20/05/2014  NUOVA LIBERAZIONE ANTICIPATA DL 2013/146 -->
<%
int ggConcLS = 0;
ggConcLS = dettagliofascicolo.getGiorniLibConcessaLS().intValue();
int ggConcLI = 0;
ggConcLI = dettagliofascicolo.getGiorniLibConcessaLI().intValue();
if (dettagliofascicolo.getGiorniLibConcessa() != null
		&& dettagliofascicolo.getGiorniLibConcessa().compareTo(new BigDecimal(0)) != 0) {
%>
	<tr>
		<td class="L">
		  	<font class="label">Liberazione Anticipata Totale Concessa gia' detratta in giorni:</font>&nbsp;
<%
	if (dettagliofascicolo.getGiorniLibConcessa().intValue() >= 0) {
%>
			<font class="campo"><%=dettagliofascicolo.getGiorniLibConcessa().intValue()%></font>
<%
	} else {
%>
			<font color="red"><%=dettagliofascicolo.getGiorniLibConcessa().intValue()%></font>
<%
	}
	if (ggConcLS != 0 || ggConcLI != 0) {
%>
			<font class="label">&nbsp;di cui</font>
<%
	}
%>
		</td>
    </tr>
<%
}
%>
<!--  LIBERAZIONE   ANTICIPATA   GIA'   CONCESSA  -->
<%
if (ggConcLS != 0 || ggConcLI != 0) {
	if (dettagliofascicolo.getGiorniLibConcessaLA() != null
			&& dettagliofascicolo.getGiorniLibConcessaLA().compareTo(new BigDecimal(0)) != 0) {
%>
	<tr>
	  	<td class="L">
	    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%
		if (dettagliofascicolo.getGiorniLibConcessaLA().intValue() >= 0) {
%>
			<font class="campo"><%=dettagliofascicolo.getGiorniLibConcessaLA().intValue()%></font>
<%
		} else {
%>
			<font color="red"><%=dettagliofascicolo.getGiorniLibConcessaLA().intValue()%></font>
<%
		}
%>
	    	<font class="label"> giorni di Liberazione Anticipata</font>&nbsp;
		</td>
	</tr>
<%
	}
%>
<!-- LIBERAZIONE  ANTICIPATA  SPECIALE  GIA' CONCESSA -->
<%
	if (dettagliofascicolo.getGiorniLibConcessaLS() != null
			&& dettagliofascicolo.getGiorniLibConcessaLS().compareTo(new BigDecimal(0)) != 0) {
%>
	<tr>
	  	<td class="L">
	    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%
		if (dettagliofascicolo.getGiorniLibConcessaLS().intValue() >= 0) {
%>
			<font class="campo"><%=dettagliofascicolo.getGiorniLibConcessaLS().intValue()%></font>
<%
		} else {
%>
			<font color="red"><%=dettagliofascicolo.getGiorniLibConcessaLS().intValue()%></font>
<%
		}
%>
	    	<font class="label"> giorni di Liberazione Anticipata Speciale</font>&nbsp;
		</td>
	</tr>
<%
	}
	// INTEGRAZIONE  LIBERAZIONE  ANTICIPATA  GIA' CONCESSA
	if (dettagliofascicolo.getGiorniLibConcessaLI() != null
			&& dettagliofascicolo.getGiorniLibConcessaLI().compareTo(new BigDecimal(0)) != 0) {
%>
	<tr>
	  	<td class="L">
	    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%
		if (dettagliofascicolo.getGiorniLibConcessaLI().intValue() >= 0) {
%>
			<font class="campo"><%=dettagliofascicolo.getGiorniLibConcessaLI().intValue()%></font>
<%
		} else {
%>
			<font color="red"><%=dettagliofascicolo.getGiorniLibConcessaLI().intValue()%></font>
<%
		}
%>
	    	<font class="label"> giorni di Integrazione Liberazione Anticipata</font>&nbsp;
		</td>
	</tr>
<%
	}
} // fine if (ggConcLS != 0 || ggConcLI != 0)
%>
<!--  LIBERAZIONE  ANTICIPATA  DA  CONCEDERE -->
<%
int ggDaConcLS = 0;
ggDaConcLS = dettagliofascicolo.getGiorniLibNONConcessaLS().intValue();
int ggDaConcLI = 0;
ggDaConcLI = dettagliofascicolo.getGiorniLibNONConcessaLI().intValue();
if (dettagliofascicolo.getGiorniLibNonConcessa() != null
		&& dettagliofascicolo.getGiorniLibNonConcessa().compareTo(new BigDecimal(0)) != 0) {
%>
	<tr>
      	<td class="L">
        	<font class="label">Liberazione Anticipata Totale Concessa da detrarre in giorni:</font>&nbsp;
<%
	if (dettagliofascicolo.getGiorniLibNonConcessa().intValue() >= 0) {
%>
        	<font class="cVerde"><%=dettagliofascicolo.getGiorniLibNonConcessa().intValue() %></font>
<%
	} else {
%>
        	<font color="red"><%=dettagliofascicolo.getGiorniLibNonConcessa().intValue()%></font>
<%
	}
	if (ggDaConcLS != 0 || ggDaConcLI != 0) {
%>
			<font class="label">&nbsp;di cui</font>
<%
	}
%>
		</td>
    </tr>
<%
}
if (ggDaConcLS != 0 || ggDaConcLI != 0) {
  	if (dettagliofascicolo.getGiorniLibNONConcessaLA() != null
  			&& dettagliofascicolo.getGiorniLibNONConcessaLA().compareTo(new BigDecimal(0)) != 0) {
%>
	<tr>
		<td class="L">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%
		if (dettagliofascicolo.getGiorniLibNONConcessaLA().intValue() >= 0) {
%>
          	<font class="campo"><%=dettagliofascicolo.getGiorniLibNONConcessaLA().intValue() %></font>
<%
		} else {
%>
          	<font color="red"><%=dettagliofascicolo.getGiorniLibNONConcessaLA().intValue() %></font>
<%
		}
%>
          	<font class="label"> giorni di Liberazione Anticipata</font>&nbsp;
		</td>
	</tr>
<%
  	}
%>
	<!--  LIBERAZIONE  ANTICIPATA  SPECIALE  DA  CONCEDERE  -->
<%
	if (dettagliofascicolo.getGiorniLibNONConcessaLS() != null
			&& dettagliofascicolo.getGiorniLibNONConcessaLS().compareTo(new BigDecimal(0)) != 0) {
%>
	<tr>
		<td class="L">
          	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%
		if (dettagliofascicolo.getGiorniLibNONConcessaLS().intValue() >= 0) {
%>
          	<font class="campo"><%=dettagliofascicolo.getGiorniLibNONConcessaLS().intValue() %></font>
<%
		} else {
%>
          	<font color="red"><%=dettagliofascicolo.getGiorniLibNONConcessaLS().intValue() %></font>
<%
		}
%>
          	<font class="label"> giorni di Liberazione Anticipata Speciale</font>&nbsp;
		</td>
	</tr>
<%
  	}
%>
	<!-- INTEGRAZIONE LIBERAZIONE ANTICIPATA DA CONCEDERE -->
<%
  	if (dettagliofascicolo.getGiorniLibNONConcessaLI() != null
  			&& dettagliofascicolo.getGiorniLibNONConcessaLI().compareTo(new BigDecimal(0)) != 0) {
%>
	<tr>
		<td class="L">
          	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%
		if (dettagliofascicolo.getGiorniLibNONConcessaLI().intValue() >= 0) {
%>
          	<font class="campo"><%=dettagliofascicolo.getGiorniLibNONConcessaLI().intValue() %></font>
<%
		} else {
%>
          	<font color="red"><%=dettagliofascicolo.getGiorniLibNONConcessaLI().intValue() %></font>
<%
		}
%>
          	<font class="label"> giorni di Integrazione Liberazione Anticipata</font>&nbsp;
		</td>
	</tr>
<%
	}
} // chiude if (ggDaConcLS != 0 || ggDaConcLI != 0)
%>
<!-- END Nuova L.A - DL 2013/146 -->
<%
//==============================================================================
// Scomputi
//==============================================================================
int s_giaconcessi= dettagliofascicolo.getCalcoloPenaModel().getScomputiGiaConcessi();
if (s_giaconcessi != 0) {
%>
	<tr>
	  	<td class="L">
			<font class="label">Revoca permesso giorni</font>
			<font class="campo"><%=Math.abs(s_giaconcessi)%></font>
			<font class="label">gia' scomputati</font>
	  	</td>
	</tr>
<%    
}
int s_daconcedere = dettagliofascicolo.getCalcoloPenaModel().getScomputiDaConcedere();
if (s_daconcedere != 0) {
%>
	<tr>
	  	<td class="L">
			<font class="label">Revoca permesso giorni</font>
			<font class="campo"><%=Math.abs(s_daconcedere)%></font>
			<font class="label">da scomputare</font>
	  	</td>
	</tr>
<%
}
if (dettagliofascicolo.getGiorniDL92Detratti() != null
		&& dettagliofascicolo.getGiorniDL92Detratti().intValue() > 0) {
%>
	<tr>
	  	<td class="l">
		    <font class="label">Riduzione pena per risarcimento danni D.L. 92/2014 gia' detratta in giorni: </font>
		    <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getGiorniDL92Detratti())%></font>
	  	</td>
	</tr>
<%
}
if (dettagliofascicolo.getGiorniDL92NONDetratti() != null 
    && dettagliofascicolo.getGiorniDL92NONDetratti().intValue() > 0) {
%>
	<tr>
	  	<td class="l">
		    <font class="label">Riduzione pena per risarcimento danni D.L. 92/2014 da detrarre in giorni: </font>
		    <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getGiorniDL92NONDetratti())%></font>
	  	</td>
	</tr>
<%
}
%>
<!------------------differimento------------------->
<%
if (dettagliofascicolo.getDecretoOrdinanzaSiep() != null) {
%>
	<tr>
    	<td class="L">
      		<font class="label">Pena Differita</font>
<%
	if (dettagliofascicolo.getDecretoOrdinanzaSiep().getFlagDecisioneTribunale().equals("S")) {
%>
      		<font class="label">:</font><font class="campo"> fino alla decisione Del Tribunale di Sorveglianza</font>&nbsp;
<%
	} else {
		if (dettagliofascicolo.getDecretoOrdinanzaSiep().getDataDifferimento() != null ) {
%>
        	<font class="label">il :</font>
        	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(dettagliofascicolo.getDecretoOrdinanzaSiep().getDataDifferimento(),"dd-MM-yyyy"))%></font>
<%
		}
        if (dettagliofascicolo.getDecretoOrdinanzaSiep().getDataRinvio() != null ) {
%>
	        <font class="label">fino al :</font>
	        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(dettagliofascicolo.getDecretoOrdinanzaSiep().getDataRinvio(),"dd-MM-yyyy"))%></font>
<%
		}
	}
}
%>
		</td>
	</tr>
<%
//==============================================================================
//    MISURA ALTERNATIVA
//==============================================================================
MisuraAlternativaModel lMisMod = dettagliofascicolo.getMisuraAlternativa();
// Se esiste una Ma e il fascicolo non e' la pena non e' sospesa
// 10/04/06 Daniele-Viviana
// se Flag Situazione e' uguale a N vuole dire che non devo visualizzare la situazione
// misura alternativa,viene impostata ad N nel caso di ripristino detenzione in carcere
// e ordine esecuzione proveniente dalla detenzione domiciliare a termine
// 2010-2011-0030-0031-0032-0033-0201-0202-0203-0204-0205-0206-0207-0208--MOTIVI DEL DIFFERIMENTO
if (lMisMod != null && lMisMod.getIdMisuraAlternativa() != null && !"N".equals(lMisMod.getFlagSituazione())
		&& !isPenaSospesa && !"2010".equals(lMisMod.getCodTipoMisura()) && !"2011".equals(lMisMod.getCodTipoMisura())
		&& !"0030".equals(lMisMod.getCodTipoMisura()) && !"0031".equals(lMisMod.getCodTipoMisura())
		&& !"0032".equals(lMisMod.getCodTipoMisura()) && !"0033".equals(lMisMod.getCodTipoMisura())
		&& !"0201".equals(lMisMod.getCodTipoMisura()) && !"0202".equals(lMisMod.getCodTipoMisura())
		&& !"0203".equals(lMisMod.getCodTipoMisura()) && !"0204".equals(lMisMod.getCodTipoMisura())
		&& !"0205".equals(lMisMod.getCodTipoMisura()) && !"0206".equals(lMisMod.getCodTipoMisura())
		&& !"0207".equals(lMisMod.getCodTipoMisura()) && !"0208".equals(lMisMod.getCodTipoMisura())) {
%>
	<tr>
    	<td class="L">
			<font class="label">Situazione misura alternativa : </font>
			<font class="campo">
<%
	if (lMisMod.getCodNaturaDecisione() != null && lMisMod.getCodTipoMisura() != null && !lMisMod.getCodTipoMisura().equals("9000")
			&& !lMisMod.getCodTipoMisura().equals("9001") && !lMisMod.getCodNaturaDecisione().equals("DD")
			&& !lMisMod.getCodNaturaDecisione().equals("PE") && !lMisMod.getCodNaturaDecisione().equals("RE")
			&& !lMisMod.getCodNaturaDecisione().equals("AP")
			&& !lMisMod.getCodTipoMisura().equals("2145") && !lMisMod.getCodTipoMisura().equals("2146")
			&& !lMisMod.getCodTipoMisura().equals("2147") && !lMisMod.getCodTipoMisura().equals("2148")
			&& !lMisMod.getCodTipoMisura().equals("2149") && !lMisMod.getCodTipoMisura().equals("2150")
			&& !lMisMod.getCodTipoMisura().equals("2151") && !lMisMod.getCodTipoMisura().equals("2153")
			&& !lMisMod.getCodTipoMisura().equals("2005") && !lMisMod.getCodTipoMisura().equals("2006")) {
%>
				<%=StringUtils.toStringJSP(lMisMod.getDescrNaturaDecisione())%>
<%
	}
%>
        		&nbsp;<%=lMisMod.getDescrTipoMisura()%>
      		</font>
    	</td>
	</tr>
  	<tr>
<%
	if (!lMisMod.getCodNaturaDecisione().equals("RE")) {
%>
		<td class="L">
<%
		if (lMisMod.getDataInizioMisura() != null) {
        	if (lMisMod.getCodNaturaDecisione() != null && lMisMod.getCodNaturaDecisione().equals("SP")) {
%>
			<font class="label">Data decorrenza sospensione misura :</font>
<%
        	} else {
%>
          	<font class="label">Data decorrenza misura : </font>
<%
        	}
%>
       		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisMod.getDataInizioMisura(),"dd-MM-yyyy"))%></font>
<%
		}
    	if (lMisMod.getDataFineMisura() != null) {
%>
			<font class="label">Data scadenza misura : </font>
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisMod.getDataFineMisura(),"dd-MM-yyyy"))%></font>
<%
    	} else if (lMisMod.getDataScadenzaProroga() != null && "2340".equals(lMisMod.getCodTipoMisura())) {
%>
			<font class="label">Fino alla data</font>
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisMod.getDataScadenzaProroga(),"dd-MM-yyyy"))%></font>
<%
		} else if (lMisMod.getFlagDecisioneTribunale() != null && "2340".equals(lMisMod.getCodTipoMisura())) {
%>
      		<font class="label">Fino alla decisione del TDS</font>
<%
   		}
%>
		</td>
	</tr>
<%
	}
}

//==============================================================================
//  SITUAZIONE SANZIONE SOSTITUTIVA
//==============================================================================
ScambioSanzioneModel lScambioMod = dettagliofascicolo.getScambioSanzione();
if (lScambioMod != null && lScambioMod.getIdScambioSanzione() != null
		&& lScambioMod.getCodTipoSanzione().compareTo("-") != 0
		&& lScambioMod.getCodTipoSanzione().compareTo("2471") != 0) {
	// Inizio 01/02/2016
  	// Descrizione Natura Sanzione da RICHIESTA_CONVERSIONE.
	Collection lColNaturaSanzione = null;
	DecodificheModel lModel = new DecodificheModel();
	IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
	lModel.setContesto("ESITO_PROVVEDIMENTO");
	lColNaturaSanzione = lDecodifiche.ExRicercaDecodifiche(lModel);
	String naturaSanzione = DecodificheUtils.getDescbyCode(lColNaturaSanzione, lScambioMod.getCodNaturaSanzione());
  	// Fine 01/02/2016
%>
	<tr>
		<td class="L">
          	<font class="label">Situazione sanzione sostitutiva : </font>
          	<font class="campo">
	    		<!--  01/02/2016 modificata riga che segue -->
            	<%=StringUtils.toStringJSP(DecodificheUtils.getDescbyCode(lColNaturaSanzione, lScambioMod.getCodNaturaSanzione()))%>
          	</font>
          	<font class="label">Procedimento SIUS : </font>
          	<font class="campo">
<%
	if (vediLinkSorv.compareTo("S") == 0) {
%>          
	            <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicoloPerAnnoProgUfficio&<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>=<%=lScambioMod.getChiaveAnnoFascicoloSius()%>&<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>=<%=lScambioMod.getChiaveProgrFascicoloSius()%>&<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>=<%=lScambioMod.getCodUfficioSorveglianza()%>" title="Procedimento Sorveglianza">
		            <%=StringUtils.toStringJSP(lScambioMod.getChiaveAnnoFascicoloSius())%>/<%=StringUtils.toStringJSP(lScambioMod.getChiaveProgrFascicoloSius())%>
				</a>
<%
	} else {
%>
	            	<%=StringUtils.toStringJSP(lScambioMod.getChiaveAnnoFascicoloSius())%>/<%=StringUtils.toStringJSP(lScambioMod.getChiaveProgrFascicoloSius())%>
<%
	}
%>
          	</font>
          	<font class="campo">
            	&nbsp;&nbsp;<%=StringUtils.toStringJSP(lScambioMod.getDescrUfficioEmittente())%>
          	</font>
			<font class="label"> di </font>
			<font class="campo">
            	<%=StringUtils.toStringJSP(lScambioMod.getComuneUfficioEmittente())%>
          	</font>
		</td>
	</tr>
    <tr>
    	<td class="l">
      		<a href="Javascript:ListaSanzioni();">Periodi Sanzione Sostitutiva</a>
    	</td>
  	</tr>
<%
} // Chiude if (lScambiomod != null ...
 
//==============================================================================
//	SITUAZIONE PERIODI_MISURE_SICUREZZA (MEV 39: RICHIESTA TESTA IN TRASFERTA TORINO: LA SEZIONE NON DEVE APPARIRE SE LA POSIZIONE GIURIDICA = 89 OPPURE 90)
//==============================================================================
if (fascicolo != null && fascicolo.getChiaveProgr() != null 
		&& (fascicolo.getChiaveProgr().intValue() >= 40000
		&& fascicolo.getChiaveProgr().intValue() < 50000)) {
	if (dettagliofascicolo!= null
			&& dettagliofascicolo.getPosizioneGiuridica() != null
			&& !"89".equals(dettagliofascicolo.getPosizioneGiuridica().getCodPosizioneGiuridica()) 
			&& !"90".equals(dettagliofascicolo.getPosizioneGiuridica().getCodPosizioneGiuridica())) {
		List misuresicurezza = (List) request.getAttribute("misuresicurezza");
		if (!misuresicurezza.isEmpty()) {
%>
<%-- MEV_39: modificata gestione lista MS: non più popup ma direttamente sulla pagina --%>
</table>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="C" colspan="7">
			<font color="red">Periodi Misure Sicurezza</font>
		</td>
  	</tr>
    <tr>
		<td class="int">Data Decorrenza</td>
      	<td class="int">Data Scadenza </td>
      	<td class="int">Durata Misura Sicurezza</td>
      	<td class="int">Note</td>
      	<td class="int">Motivo</td>
	</tr>
<%
			Iterator msIterator = misuresicurezza.iterator();
    		while (msIterator.hasNext()) {
    			PeriodoAltraMisuraModel lPeriodoAltraMisuraModel = (PeriodoAltraMisuraModel) msIterator.next();
%>
	<tr>
		<td class="c">
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodoAltraMisuraModel.getDataInizioEsecuzione(), "dd-MM-yyyy"), "-")%>
		</td>
		<td class="c">
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodoAltraMisuraModel.getDataScadenza(),"dd-MM-yyyy"), "-")%>
		</td>
<%
				CalendarModel lCal = new CalendarModel();
				lCal.setDataInizio(lPeriodoAltraMisuraModel.getDataInizioEsecuzione());
				lCal.setDataFine(lPeriodoAltraMisuraModel.getDataScadenza());
				CalendarUtil lCalUtil = new CalendarUtil();
				lCal = lCalUtil.CalcolaNumGiorniMesiAnni(lCal, false);
				if (CalendarUtil.getTotGiorni(lCal) >= 0) {
%>
		<td class="c">
			<font class="label">Anni</font>
			<font class="campo"><%=lCal.getNumAnni()%></font>
			<font class="label">Mesi</font>
			<font class="campo"> <%=lCal.getNumMesi()%></font>
			<font class="label">Giorni</font>
			<font class="campo"><%=lCal.getNumGiorni()%></font>
		</td>
		<td class="c">
			<%=StringUtils.toStringJSP(lPeriodoAltraMisuraModel.getMotivazione(), "-")%>
		</td>
		<td class="c">
            <%=StringUtils.toStringJSP(lPeriodoAltraMisuraModel.getDescrMotivo(), "-")%>
		</td>
	  </tr> 
<%
				}
			} // end while ...
%>
</table>
<br>
<table cellspacing="0" cellpadding="0" width="95%">
<%
		}
	}
}
// fine MEV_39

//==============================================================================
//  POSIZIONE MATERIALE
//==============================================================================
if (dettagliofascicolo.getPosizioneMateriale() != null) {
%>
	<tr>
	  	<td class="L">
		    <font class="label">Posizione Materiale :</font>&nbsp;
		    <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getPosizioneMateriale().getDescrPosizioneMateriale())%></font>
	  	</td>
	</tr>
<%
}
if (fascicolo.getNote() != null) {
%>
	<tr>
	 	<td class="L">
	   		<font class="label">Note : </font>
	   		<font class="cVerde"><%=StringUtils.toStringJSP(fascicolo.getNote())%></font>
	  	</td>
	</tr>
<%
}
%>

	<%-- MEV_2023-33: aggiunto link cliccabile se presenti --%>
<%
if (existPagamenti) {
%>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="L">
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActVerificaStatoBollettino" title="Verifica Stato Pagamenti">
				Verifica Stato Pagamenti
			</a>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
<%
}
%>
	<%-- FINE MEV_2023-33 --%>

<%
//==============================================================================
//  ULTIMI EVENTI
//==============================================================================
List lListEve = dettagliofascicolo.getEventi();
if (lListEve != null && lListEve.size() != 0) {
%>
	<tr>
      	<td class="Titolo" colspan=3>Ultimi Eventi</td>
    </tr>
<%
    Iterator lIterEventi = lListEve.iterator();
    for (int i = 0; i < Math.min(lListEve.size(), 2); i++) {
    	EventoNotificaModel lEveNotificaMod = (EventoNotificaModel)lIterEventi.next();
		EventoModel lEveMod = lEveNotificaMod.getEvento();
		CampoNotaModel[] lListCampoNota = null;
		lListCampoNota = lEveNotificaMod.getCampoNote();
		CampoNotaModel lCampoMod = null;
      	// Vengono filtrati provvedimenti non validati se di altro ufficio. Luigi 14-04-2011
      	if (UtenteConnesso.getUfficioUtente().isUfficioDiCompetenza(lEveMod.getCodUfficioInserimento())
      			|| (lEveMod.getFlagDocumentoRegistrato() != null
      			&& lEveMod.getFlagDocumentoRegistrato().equalsIgnoreCase("S"))) {
%>
	<tr>
		<td class="L">
			<font class="label">Data Emissione : </font>
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEveMod.getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
			<font class="label"><%=StringUtils.toStringJSP(lEveMod.getDescrTipoProvvedimento())%>: </font>
			<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
			<%-- <font class="campo"><%=StringUtils.toStringJSP(lEveMod.getDescrEsito())%></font>&nbsp; --%>
<%
			if (lEveMod.getCodTipoEvento() != null
					&& lEveMod.getCodTipoEvento().equals("03")
					&& ("S").equals(lEveMod.getFlagDocumentoRegistrato())) {
				String lActDettaglio = null;
				ActGestisciButtonsProvvedimento lAction = new ActGestisciButtonsProvvedimento();
				lActDettaglio = lAction.getActionDettaglioProvvedimento(lEveMod.getCodMotivo(),
				lEveMod.getCodTipoEvento(),
				lEveMod.getCodTipoProvvedimento(),
				lEveMod.getTemIdTemplate(),
				lEveMod.getFlagDocumentoRegistrato());           
				// 19/11/2011 aggiunto parametro TipoVis per Inoltro e Disposizione al PM della Nuova Istanza.
				String lParametro = "";
				if (lEveMod.getCodMotivo().equals("1001"))
					lParametro += "&TipoVis=Inoltro";
				if (lEveMod.getCodMotivo().equals("1002"))
				 	lParametro += "&TipoVis=Disposizione";
%>
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActDettaglio%>&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=lEveMod.getIdEvento()%><%=lParametro%>">
				<font color="red"><%=StringUtils.toStringJSP(lEveMod.getDescrMotivo())%></font>&nbsp;<br>
        	</a>
<%
			} else {
				// MEV10-s3: modificato codice
				if ("1100".equals(lEveMod.getCodMotivo()) || "1101".equals(lEveMod.getCodMotivo())
						|| "1102".equals(lEveMod.getCodMotivo()) || "1103".equals(lEveMod.getCodMotivo())
						|| "1104".equals(lEveMod.getCodMotivo()) || "1105".equals(lEveMod.getCodMotivo())
						|| "1106".equals(lEveMod.getCodMotivo()) || "1107".equals(lEveMod.getCodMotivo())
						|| "1108".equals(lEveMod.getCodMotivo()) || "1114".equals(lEveMod.getCodMotivo())
						|| "1115".equals(lEveMod.getCodMotivo()) || "1116".equals(lEveMod.getCodMotivo())
						|| "1117".equals(lEveMod.getCodMotivo()) || "1118".equals(lEveMod.getCodMotivo())
						|| "1119".equals(lEveMod.getCodMotivo())) {
%>
			<font class="campo"><%=StringUtils.toStringJSP(lEveMod.getLegge())%></font>&nbsp;<br>
<%
				} else {
%>
			<font class="campo"><%=StringUtils.toStringJSP(lEveMod.getDescrMotivo())%></font>
<%
					if (lEveMod.getCodTipoProvvedimento() != null
							&& "04".equals(lEveMod.getCodTipoProvvedimento())
							&& ("0284").equals(lEveMod.getCodMotivo())) {
						if (lEveMod.getCodEsito().equals("-")) {
%>
			<font class="label">Esito : </font>
			<font class="campo">Richiesta Accolta </font>
<%
						} else if (lEveMod.getCodEsito().equals("C")) {
%>
			<font class="label">Esito : </font>
			<font class="campo">Richiesta Accolta in Conformita' </font>
<%
						} else if (lEveMod.getCodEsito().equals("D")) {
%>
			<font class="label">Esito : </font>
			<font class="campo">Richiesta Accolta in Difformita' </font>
<%
						} else if (lEveMod.getCodEsito().equals("R")) {
%>
   			<font class="label">Esito : </font>
   			<font class="campo">Richiesta Rigettata </font>
<%
						} else if (lEveMod.getCodEsito().equals("I")) {
%>
  			<font class="label">Esito : </font>
			<font class="campo">Richiesta Inammissibile </font>
<%        		
						} else if (lEveMod.getCodEsito().equals("U")) {
%>    
   			<font class="label">Esito : </font>
   			<font class="campo">Riunisce</font>
<%        				}
      				}
				}
			}
%>
 			<br>
<% 
			if (lEveMod.getCodTipoEvento().equals("01") && lEveMod.getCodTipoProvvedimento().equals("04")
					&& lEveMod.getCodMotivo().equals("7777")) {
            	if (lListCampoNota != null  && !(lListCampoNota.length == 0)) {
            		lCampoMod = (CampoNotaModel) lListCampoNota[0]; // e' previsto un solo campo nota
%>
			<font class="label">Nota : </font>
			<font class="campoNoCap"><%=SiapStringUtil.formattaCampoNote(lCampoMod.getDescr(), "<br>")%><br></font>
<%
            	}
          	}
%>
			<font class="label">Ufficio : </font>
			<font class="campo"><%=StringUtils.toStringJSP(lEveMod.getDescrUfficioEmittente())%></font>
			<font class="label"> di </font>
			<font class="campo"><%=StringUtils.toStringJSP(lEveMod.getDescrLuogoEmittente())%></font>
<%
          	if ((lEveMod.getCodTipoEvento() != null && !lEveMod.getCodTipoEvento().equals("03"))
          			&& (lEveMod.getFlagDocumentoRegistrato() == null || lEveMod.getFlagDocumentoRegistrato().equals("N"))) {
				// Modifica 06-03-06 -- Dario -- Viviana
				// modifica per permettere il link dal provvedimento non validato direttamente sul proprio dettaglio
           		String lActDettaglio = null;
           		ActGestisciButtonsProvvedimento lAction = new ActGestisciButtonsProvvedimento();
 				// MEV10-s3: modificato il parametro di passaggio per gestire nullpointerexception
				lActDettaglio = lAction.getActionDettaglioProvvedimento(lEveMod.getCodMotivo() != null ? lEveMod.getCodMotivo() : "",
				lEveMod.getCodTipoEvento(),
				lEveMod.getCodTipoProvvedimento(),
				lEveMod.getTemIdTemplate(),
				lEveMod.getFlagDocumentoRegistrato());
           		if (lActDettaglio == null || lActDettaglio.equals("")) {
               		lActDettaglio = "siap.siep.ordineesecuzione.action.ActRicercaProvvedimenti";
             	}
%>
        	- <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActDettaglio%>&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=lEveMod.getIdEvento()%>">
            	<font color="red"> NON VALIDATO</font>
        	</a>&nbsp;
<%
			}
%>
		</td>
	</tr>
<%
		} // end if ...
	} // end for ...
} // end if (lListEve ...
%>
</table>
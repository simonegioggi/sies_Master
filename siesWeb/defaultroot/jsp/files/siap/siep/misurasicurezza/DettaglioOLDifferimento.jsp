<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina per gestione OE per Differimento MS --%>
<%@page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza"%>

<jsp:useBean id="eventonotifica"      	scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizioneAltra"		scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="MisuraModel"			scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<jsp:useBean id="istitutodetenzione"	scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="avvocati"				scope="request" class="java.util.Vector"/>
<jsp:useBean id="provvSorv"				scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriFascicoloSiusModel"/>
<jsp:useBean id="misuraAlternativa"		scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="ufficioEmittente"   	scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="dataScadenza"			scope="request" class="java.util.Date"/>
<%-- MEV_39: aggiunto e gestito useBean su Istituto Detenzione --%>
<jsp:useBean id="strutturaDesignataModel"   scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");

UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();

MagistratoModel lMagistrato = eventonotifica.getMagistrato();
if (lMagistrato == null)
	lMagistrato = new MagistratoModel();

// Gestione Autorità Esterne sulle Notifiche 
String lCodTipoAutorita1 = "-";
NotificaModel lPrimaNotifica = new NotificaModel();
AutoritaEsternaModel lPrimaAutoritaEsterna = new AutoritaEsternaModel();

if (eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0) {
	if (eventonotifica.getNotifiche()[0].getAutoritaEsterna() != null
			&& "E".equals(eventonotifica.getNotifiche()[0].getCodTipoNotifica()))
		lCodTipoAutorita1 = eventonotifica.getNotifiche()[0].getAutoritaEsterna().getCodTipoAutorita();
	lPrimaNotifica = eventonotifica.getNotifiche()[0];
    lPrimaAutoritaEsterna = lPrimaNotifica.getAutoritaEsterna();
}

// Misura Sicurezza: valori da passare alla action di Stampa
String CodMisura = "";
String MisidMisura = "";
if (MisuraModel != null && MisuraModel.getCodTipo() != null && MisuraModel.getIdMisuraSicurezza() != null) {
	CodMisura = MisuraModel.getCodTipo();
	// id_Misura della misura Corrente = Mis_id_Misura della eventuale Misura precedente  
	MisidMisura = MisuraModel.getIdMisuraSicurezza().toString();
}	

// Posizione Giuridica
String CodPos = "";
if (posizioneAltra != null && posizioneAltra.getPosizioneGiuridica() != null
		&& posizioneAltra.getPosizioneGiuridica().getCodPosizioneGiuridica() != null)
	CodPos = posizioneAltra.getPosizioneGiuridica().getCodPosizioneGiuridica();

String dataRinvioCalcolata = "";
%>

<!--  DettaglioOLDifferimento -->
<html>
  	<head>
    	<title>[S.I.E.S.] - Esecuzione Misure sicurezza - Dettaglio Ordine di Liberazione per Differimento della Misura di Sicurezza</title>
    	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    	
    	<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
		<script language="JavaScript">
		
		   function stampaSiep(lAzione)
		   {
		      var  hrefStampa = lAzione;
		      var lIndice = hrefStampa.indexOf("?");
		
		      var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
		
		      stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  parametri);
		   }	   
		</script>
	
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
			      	<font class="label">Funzione:</font>&nbsp;&nbsp;&nbsp;
			        <font class="campo">Dettaglio Ordine di Liberazione per Differimento della Misura di Sicurezza</font>
      			</td>
<%
if ((eventonotifica.getEvento().getFlagDocumentoRegistrato() == null)
		|| eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
%>

    			<!-- BOTTONE DI STAMPA -->
<%-- 				<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>"> --%>
<%-- 					<jsp:param name="ActionLink" value="<%= "/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActStampaOLDifferimento&MisIdMisuraSicurezza="+MisidMisura+"&CodTipo="+CodMisura+"&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&CodMotivo="+eventonotifica.getEvento().getCodMotivo()+"&CodPosizioneGiuridica="+CodPos%>"/> --%>
<%-- 				</jsp:include> --%>
				<!-- BOTTONE DI STAMPA -->
				<td class="LBG">
	       			<a  href="Javascript:stampaSiep('/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActStampaOLDifferimento&MisIdMisuraSicurezza=<%=MisidMisura%>&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&CodMotivo=<%=eventonotifica.getEvento().getCodMotivo()%>&CodPosizioneGiuridica=<%=CodPos%>&CodTipo=<%=CodMisura%>')" onclick="javascript:lookUpload();">
	        			 <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
	       			</a>
	     		</td>
     		
				<!-- BOTTONE DI VALIDAZIONE -->
      			<td class="LBG">
        			<a href="/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActUploadOLDifferimento&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.misurasicurezza.action.ActDettaglioOLDifferimento&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
          				<img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
        			</a>
      			</td>
				<!-- ICONA DI MODIFICA -->
				<td class="LBG">
					<a href="Main.jsp?Action=siap.siep.misurasicurezza.action.ActLoadModificaOLDifferimento&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&TornaQui=10&idFascSius=<%=provvSorv.getFascicoloSiusModel() != null ? provvSorv.getFascicoloSiusModel().getIdFascicoloSius() : ""%>&idEveFascSius=<%=provvSorv.getEvento() != null ? provvSorv.getEvento().getIdEvento() : eventonotifica.getEvento().getIdEvento()%>">
	         			<img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
	        		</a>
        		</td>
        		<!-- ICONA DI ELIMINAZIONE -->
				<td class="LBG">
					<a href="Main.jsp?Action=siap.siep.misurasicurezza.action.ActModificaOLDifferimento&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&modalita=D&TornaQui=10">
	         			<img align="middle" src="/images/delete24.gif" alt="Elimina" width="24" height="24" border="0">
	        		</a>
	        	</td>
	        	<%-- 20190918 [SG]: collaudo 11.3 elimino torna indietro --%>
				<!-- ICONA DI TORNA INDIETRO -->
<!-- 				<td class="LBG"> -->
<!-- 					<a href="javascript:history.go(-1);"> -->
<%-- 				   		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"> --%>
<!-- 				  	</a> -->
<!-- 				</td> -->
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
	      		<td class="l" width="25%">Posizione Giuridica</td>
	      		<td class="L">
	        		<font class="campo">
	         			<%=posizioneAltra.getPosizioneGiuridica().getDescrPosizioneGiuridica()%>
	  	    		</font>
	      		</td>
	      	</tr>	
    	</table>	
		<input type="HIDDEN" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=CodPos%>">
       	<input type="HIDDEN" name="<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO%>" value="<%=CodMisura%>">
       	<input type="HIDDEN" name="<%=ICostantiMisuraSicurezza.CAMPO_MIS_ID_MISURA_SICUREZZA%>" value="<%=MisidMisura%>">
		<!--  Eventuali Misura di Sicurezza -->
		<table cellspacing="0" cellpadding="0" width="95%">
<%
List lMisure = (List) request.getAttribute("listaMisure");
if (lMisure.size() > 0) {
	Iterator itx = lMisure.iterator();
    while (itx.hasNext()) {
    	MisuraSicurezzaModel lMis = (MisuraSicurezzaModel) itx.next();
%>	
			<tr>
				<td class="L" width="25%">Misura di Sicurezza da espiare</td>
			    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%></font></td>
			    <td class="c">Anni</td>
			    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(),"0")%></font></td>
			    <td class="c">Mesi</td>
			    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(),"0")%></font></td>
			    <td class="c">Giorni</td>
			    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(),"0")%></font></td>	
			</tr>
<%
	}
}
%>
   		</table>
		<table cellspacing="0" cellpadding="0" width="95%">
	  		<tr>
	        	<td class="l" width="25%">Tipo Provvedimento</td>
	        	<td class="L">
        			<font class="campo">
        				<%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrTipoProvvedimento())%>
        				&nbsp;
        				<%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%>
       				</font>
<%
if (eventonotifica != null && eventonotifica.getEvento() != null && eventonotifica.getEvento().getFlagPiuMeno() != null
		&& eventonotifica.getEvento().getFlagPiuMeno().compareTo("D") == 0) {
%>
        			&nbsp;<font color="red">(Disposta Esecuzione Immediata)</font>
<%
}
%>
        		</td>
        	</tr>	
	  		<tr>
	    		<td class="l">Data Emissione</td>
	    		<td class="L">
	      			<font class="campo">
	      				<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%>
	      			</font>
	    		</td>
	  		</tr>
	  		<tr>   
	    		<td class="l">Data Trasmissione</td>
	    		<td class="L">
	      			<font class="campo">
	        			<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy"))%>
	      			</font>
	    		</td>
     		</tr>
  		</table>
		<br>
		<!-- Estremi Provvedimento della Sorveglianza -->
		<table cellspacing="0" cellpadding="0" width="95%">
			<tr>
				<td class="Titolo"colspan="2">Estremi Provvedimento della Sorveglianza</td>
			</tr>
			<tr>
    			<td class="l" width="25%">Autorita' Emittente</td>
       			<td class="L">
      				<font class="campo">
      					<%=StringUtils.toStringJSP(Utils.isPresent(provvSorv.getDescrTipoUfficio()) ? provvSorv.getDescrTipoUfficio() : ufficioEmittente.getDescrTipoUfficio())%>
      				</font>
        		</td>
    		</tr>
    		<tr>
    			<td class="l">Sede Autorita' Emittente</td>
       			<td class="L">
      				<font class="campo">
      					<%=StringUtils.toStringJSP(Utils.isPresent(provvSorv.getDescrComuneUfficio()) ? provvSorv.getDescrComuneUfficio() : ufficioEmittente.getDescrComune())%>
      				</font>
        		</td>
    		</tr>
    		<tr>
    			<td class="l">Data Emissione Provvedimento</td>
       			<td class="L">
      				<font class="campo">
	      				<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getEvento() != null ? provvSorv.getEvento().getDataEmissione() : misuraAlternativa.getDataDecisione(), "dd-MM-yyyy"))%>
	      			</font>
        		</td>
    		</tr>
<%
String afs = "", nfs = "", anfs = "";
if (provvSorv.getFascicoloSiusModel() != null) {
	afs = provvSorv.getFascicoloSiusModel().getChiaveAnno() != null ? provvSorv.getFascicoloSiusModel().getChiaveAnno().toString() : "";
	nfs = provvSorv.getFascicoloSiusModel().getChiaveProgr() != null ? provvSorv.getFascicoloSiusModel().getChiaveProgr().toString() : "";
}
else if (misuraAlternativa != null) {
	afs = misuraAlternativa.getChiaveAnnoFascicoloSius() != null ? misuraAlternativa.getChiaveAnnoFascicoloSius().toString() : "";
	nfs = misuraAlternativa.getChiaveProgrFascicoloSius() != null ? misuraAlternativa.getChiaveProgrFascicoloSius().toString() : "";
}
anfs = afs;
if (Utils.isPresent(afs) && Utils.isPresent(nfs))
	anfs += "/" + nfs;
else if (Utils.isPresent(nfs))
	anfs = nfs;
%>
    		<tr>
    			<td class="l">Anno/Numero Fascicolo SIUS</td>
       			<td class="L">
      				<font class="campo">
      					<%=anfs%>
      				</font>
        		</td>
    		</tr>
    		<tr>
    			<td class="l">Tipo Provvedimento</td>
       			<td class="L">
      				<font class="campo">
      					<%=StringUtils.toStringJSP(Utils.isPresent(provvSorv.getDescrProvvedimento()) ? provvSorv.getDescrProvvedimento() : misuraAlternativa.getDescrTipoDecisione())%>
      				</font>
        		</td>
    		</tr>
<%
String ap = "", np = "", anp = "";
if (provvSorv.getEvento() != null) {
	ap = provvSorv.getEvento().getAnnoProtocollo() != null ? provvSorv.getEvento().getAnnoProtocollo().toString() : "";
	np = provvSorv.getEvento().getProgrProtocollo() != null ? provvSorv.getEvento().getProgrProtocollo().toString() : "";
}
else if (misuraAlternativa != null) {
	ap = misuraAlternativa.getAnnoRegistro() != null ? misuraAlternativa.getAnnoRegistro().toString() : "";
	np = misuraAlternativa.getNumeroRegistro() != null ? misuraAlternativa.getNumeroRegistro().toString() : "";
}
anp = ap;
if (Utils.isPresent(ap) && Utils.isPresent(np))
	anp += "/" + np;
else if (Utils.isPresent(np))
		anp = np;
%>
    		<tr>
    			<td class="l">Anno/Numero Provvedimento</td>
       			<td class="L">
      				<font class="campo">
      					<%=anp%>
      				</font>
        		</td>
    		</tr>
    		<tr>
    			<td class="l">Oggetto</td>
       			<td class="L">
      				<font class="campo">
      					<%=StringUtils.toStringJSP(Utils.isPresent(provvSorv.getDescrOggetto()) ? provvSorv.getDescrOggetto() : misuraAlternativa.getDescrTipoMisura())%>
      				</font>
        		</td>
    		</tr>
    		<tr>
    			<td class="l">Esito</td>
       			<td class="L">
      				<font class="campo">
      					<%=StringUtils.toStringJSP(Utils.isPresent(provvSorv.getDescrEsito()) ? provvSorv.getDescrEsito() : misuraAlternativa.getDescrNaturaDecisione())%>
      				</font>
        		</td>
    		</tr>
    		<tr>
    			<td class="l">Data Fine Rinvio</td>
    			<%if(provvSorv!=null && provvSorv.getOrdinanza() != null && provvSorv.getOrdinanza().getDataFineMisura() != null){ 
    				dataRinvioCalcolata = DateUtils.getDateToString(provvSorv.getOrdinanza().getDataFineMisura(), "dd-MM-yyyy");
    			   }else if(provvSorv!=null && provvSorv.getMisuraAlternativa()!= null && provvSorv.getMisuraAlternativa().getDataFineMisura() != null ){
    				   dataRinvioCalcolata = DateUtils.getDateToString(provvSorv.getMisuraAlternativa().getDataFineMisura(), "dd-MM-yyyy");
    			   }else if (dataScadenza != null){
    				   dataRinvioCalcolata = DateUtils.getDateToString(dataScadenza, "dd-MM-yyyy");
    			   }
    			%>
       			<td class="L">
      				<font class="campo">
      					<%=StringUtils.toStringJSP(dataRinvioCalcolata)%>
      				</font>
        		</td>
    		</tr>
<%
if (provvSorv!= null && provvSorv.getMisuraAlternativa() != null) {
	if (!"S".equals(provvSorv.getMisuraAlternativa().getFlagDecisioneTribunale())) { // flag decisione TDS
		if (provvSorv.getOrdinanza() != null
				&& ((provvSorv.getOrdinanza().getSospensioneAASS() != null && !provvSorv.getOrdinanza().getSospensioneAASS().equals(new BigDecimal(0)))
					|| (provvSorv.getOrdinanza().getSospensioneMMSS() != null && !provvSorv.getOrdinanza().getSospensioneMMSS().equals(new BigDecimal(0)))
					|| (provvSorv.getOrdinanza().getSospensioneGGSS() != null && !provvSorv.getOrdinanza().getSospensioneGGSS().equals(new BigDecimal(0))))) {
%>
			<tr>
    			<td class="l">Durata</td>
       			<td class="L">
      				<font class="campo">
      					Anni&nbsp;<%=StringUtils.toStringJSP(provvSorv.getOrdinanza().getSospensioneAASS())%>&nbsp;
      					Mesi&nbsp;<%=StringUtils.toStringJSP(provvSorv.getOrdinanza().getSospensioneMMSS())%>&nbsp;
      					Giorni&nbsp;<%=StringUtils.toStringJSP(provvSorv.getOrdinanza().getSospensioneGGSS())%>
      				</font>
        		</td>
    		</tr>
<%
		}
	} else {
%>
    		<tr>
    			<td class="l">Durata</td>
       			<td class="L">
      				<font class="campo">
      					Fino alla Decisione del Tribunale di Sorveglianza
      				</font>
        		</td>
    		</tr>
<%
	}
} else {
	if (!"S".equals(misuraAlternativa.getFlagDecisioneTribunale())) { // flag decisione TDS
		if (misuraAlternativa.getNumAnniMisura() != null || misuraAlternativa.getNumMesiMisura() != null
			|| misuraAlternativa.getNumGiorniMisura() != null) {
%>
			<tr>
    			<td class="l">Durata</td>
       			<td class="L">
      				<font class="campo">
      					Anni&nbsp;<%=StringUtils.toStringJSP(misuraAlternativa.getNumAnniMisura())%>&nbsp;
      					Mesi&nbsp;<%=StringUtils.toStringJSP(misuraAlternativa.getNumMesiMisura())%>&nbsp;
      					Giorni&nbsp;<%=StringUtils.toStringJSP(misuraAlternativa.getNumGiorniMisura())%>
      				</font>
        		</td>
    		</tr>
<%
		}
	} else {
%>
    		<tr>
    			<td class="l">Durata</td>
       			<td class="L">
      				<font class="campo">
      					Fino alla Decisione del Tribunale di Sorveglianza
      				</font>
        		</td>
    		</tr>
<%
	}
}
%>
    		<tr>
    			<td class="l">Casa o Altro Luogo di Cura</td>
       			<td class="L">
      				<font class="campo">
      					<%=StringUtils.toStringJSP(provvSorv!=null && provvSorv.getOrdinanza() != null ? provvSorv.getOrdinanza().getLuogoSvolgimentoProva() : misuraAlternativa.getDescrLuogoProva(), "-")%>
      				</font>
        		</td>
    		</tr>
<%
if (!posizioneAltra.getPosizioneGiuridica().isLibero()) {
%>
    		<tr>
    			<td class="l">Data Scarcerazione</td>
       			<td class="L">
      				<font class="campo">
      					<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getMisuraAlternativa() != null ? provvSorv.getMisuraAlternativa().getDataScarcerazione() : misuraAlternativa.getDataScarcerazione(), "dd-MM-yyyy"),"-")%>
<%
	if (provvSorv.getMisuraAlternativa() != null && provvSorv.getMisuraAlternativa().getDataScarcerazione() != null) {
		if ("PROC".equals(provvSorv.getMisuraAlternativa().getCodTipoUfficioScarcerazione())) {
%>
						&nbsp;(Eseguita dalla Procura)
<%
		} else {
%>
						&nbsp;(Eseguita dalla Sorveglianza)
<%
		}
	} else {
		if (misuraAlternativa != null && misuraAlternativa.getDataScarcerazione() != null) {
			if ("PROC".equals(misuraAlternativa.getCodTipoUfficioScarcerazione())) {
%>
						&nbsp;(Eseguita dalla Procura)
<%
			} else {
%>
						&nbsp;(Eseguita dalla Sorveglianza)
<%
			}
		}
	}
%>
      				</font>
        		</td>
    		</tr>
<%
}
%>
    		<tr>
    			<td class="l">Data Differimento</td>
       			<td class="L">
      				<font class="campo">
						<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getMisuraAlternativa() != null ? provvSorv.getMisuraAlternativa().getDataInizioMisura() : misuraAlternativa.getDataInizioMisura(), "dd-MM-yyyy"))%>
      				</font>
        		</td>
    		</tr>
		</table>
		<br>
  		<!--Magistrato Firmatario -->   
 		<table cellspacing="0" cellpadding="0" width="95%">
 			<tr>
				<td class="Titolo"colspan="2">Magistrato Firmatario</td>
			</tr>
  			<tr>
		     	<td class="l" width="25%">Magistrato</td>
		     	<td class="L">
		          	<font class="campo">
		            	<%=StringUtils.toStringJSP(lMagistrato.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lMagistrato.getNome())%>
		          	</font>
		          	<input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(lMagistrato.getCodMagistrato() )%>" type="text" name="<%=ICostantiEvento.CAMPO_COD_MAGISTRATO%>">
		      	</td>
			</tr>
		</table>
		<br>
		<table cellspacing="0" cellpadding="0" width="95%">
			<tr>
				<td class="Titolo"colspan="2">Destinatari</td>
			</tr>

<!--Struttura Designata-->
	
	<%
	if (strutturaDesignataModel != null && Utils.isPresent(strutturaDesignataModel.getIdIstitutoDetenzione())) {
	%><tr>	
		<td class="L">Struttura Designata</td>
			<td class="l" colspan="3">		
              <font class="campo"><%=StringUtils.toStringJSP(strutturaDesignataModel.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(strutturaDesignataModel.getDescrizione())%> - <%=StringUtils.toStringJSP(strutturaDesignataModel.getIndirizzo())%></font>&nbsp;
			</td>
	<%
	} else {
	%>
			<td class="l" colspan="3">
		        <font class="campo">&nbsp;&nbsp;</font>				      
		    </td>
	<%
		} 
	%>
	</tr>
			
 			<!--Autorita' per Esecuzione -->
<%
if (lPrimaNotifica.getAutoritaEsterna() != null) {
%>
		   	<tr>
				<td class="l" width="25%">Autorita' per Esecuzione</td>
			    <td class="L">
			      	<font class="campo"><%=StringUtils.toStringJSP(lPrimaNotifica.getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp;
			      	di
			      	<font class="campo"><%=StringUtils.toStringJSP(lPrimaNotifica.getAutoritaEsterna().getDescrSede())%></font>
			    </td>
			</tr>
<%  
	if (lPrimaNotifica != null && lPrimaNotifica.getNote() != null) {
%>
			<tr>
				<td class="l" width="25%">Note</td>
				<td class="L">
					<font class="campo"><%=StringUtils.toStringJSP(lPrimaNotifica.getNote())%></font>
				</td>
			</tr>
<%
	}
}
%>
<!-- Destinatario MDS -->
<%
if (eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0) {
	for (int i = 0; i < eventonotifica.getNotifiche().length; i++) {
		if (eventonotifica.getNotifiche()[i] != null
				&& ("MS").equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica())) {
%>
			<tr>
    			<td class="l" width="25%">Destinatario Sorveglianza</td>
       			<td class="L">
          			<font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getUfficio().getDescrTipoUfficio())%>
        			</font>&nbsp;
					di 
      				<font class="campo">
      					<%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getUfficio().getDescrComune())%>
      				</font>
        		</td>
    		</tr>
<%
		}
	}
}
%>
		</table>
<!-- Autorita ESTERNA per Notifica AVVOCATO Difensore (se presente) -->
<%
Iterator lItxAvv = avvocati.iterator();
%>
<%
for (int ind = 0; ind < eventonotifica.getNotifiche().length; ind++) {
	if (eventonotifica.getNotifiche()[ind].getCodTipoNotifica().equals("ND")) {
%>
		<br>
		<table cellspacing="0" cellpadding="0" width="95%">
			<tr>
				<td class="Titolo"colspan="2">Destinatari per Notifica</td>
			</tr>
<%
		if (lItxAvv.hasNext()) {
			AvvocatoSiepModel lAvv = (AvvocatoSiepModel) lItxAvv.next();
%>
			<tr>
            	<td class="l" width="25%">Notifica al Difensore</td>
            	<td class="L">
              		<font class="campo" >
                		<%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
              		</font>
              		&nbsp;Foro di&nbsp;
              		<font class="campo">
                		<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
              		</font>
              		&nbsp;Difensore di&nbsp;
              		<font class="campo">
                		<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
              		</font>
            	</td>
          	</tr>
 <%
  		}
%>
			<tr>
<%
		if (StringUtils.toStringJSP(eventonotifica.getNotifiche()[ind].getAutoritaEsterna().getCodTipoAutorita().trim()).compareTo("C0") == 0) {
%>
				<td class="l">tramite</td>
				<td class="L">
					<font class="campo">
						<%=eventonotifica.getNotifiche()[ind].getAutoritaEsterna().getDescrTipoAutorita().trim()%>
					</font>
<%
		} else {
%>
				<td class="l">presso</td>
				<td class="L">
					<font class="campo">
						<%=eventonotifica.getNotifiche()[ind].getAutoritaEsterna().getDescrTipoAutorita().trim()%>
					</font>
					&nbsp;di&nbsp;
					<font class="campo">
						<%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[ind].getAutoritaEsterna().getDescrSede())%>
					</font>
				</td>
<%
		}
%>
   			</tr>
<%					
		if (eventonotifica.getNotifiche()[ind] != null && eventonotifica.getNotifiche()[ind].getNote() != null) {
%>		   			
			<tr>
	    		<td class="l">Indirizzo</td>
	    		<td class="L">
	    			<font class="campo">
	    				<%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[ind].getNote())%>
	    			</font>
	    		</td>
	   		</tr>
<%
		}
%>
		</table>
<%
	}
}
%>
		<br>
  		<div align="left" style="visibility:hidden" id="upld">
    		<FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      			<table>
          			<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        			<tr>
          				<td class="L">
				            <input class="bottone" type="submit" value="Conferma">
				            <input type="HIDDEN" name="motivo" value="<%=eventonotifica.getEvento().getCodMotivo()%>">
				            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActUploadEsecuzioneMS">
				            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento()%>">
				            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misurasicurezza.action.ActDettaglioOLDifferimento">
          				</td>
        			</tr>
      			</table>
    		</FORM>
  		</div>
	</body>
</html>
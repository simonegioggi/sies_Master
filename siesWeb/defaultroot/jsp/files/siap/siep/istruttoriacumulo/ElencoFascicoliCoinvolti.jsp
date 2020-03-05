<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.F3BProperties"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Arrays"%>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>

<%@ page import="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiSoggettoCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiProcedimentoCumulato"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.SoggettoCumulatoModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<%@ page import="f3b.log.LogF3B"%>
<%@ page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>

<jsp:useBean id="IstruttoriaCumulo" 	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="ListaTitoli"       	scope="request" class="java.util.Vector"/>
<jsp:useBean id="Ordinamento"       	scope="request" class="java.lang.String"/>
<jsp:useBean id="ProvvedimentoCumulo" 	scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="ParentFormName" 		scope="request" class="java.lang.String"/>

<%
//==============================================================================
//     FORM di visualizzazione dell'elenco dei fascicoli coinvolti (CUMULI)
//==============================================================================
FascicoloSiepModel lFascicolo = (FascicoloSiepModel) session.getAttribute("fascicolo");
SoggettoModel lSoggettoFascicolo = lFascicolo.getSoggetto();
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Cumulo</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
<script language="JavaScript">
    //==========================================================================
    // Visualizza, nasconde il record con il dettaglio dell'istruttoria
    //==========================================================================
    function dettaglioTitolo(idRecord)
    {
      var riga = document.getElementById(idRecord);  
      if (riga.style.display =="none" )
      {
        riga.style.display = "block";
      }
      else 
      {
        riga.style.display = "none";
      }
    }
    
    //==========================================================================
    // Richiama l'opportuna action
    //==========================================================================
    function caricaDatiAnalitici(aIdTitolo)
    {
      lAzione = "siap.siep.modulocumulo.action.ActLoadGrigliaDatiAnalitici";
      document.ElencoFascicoliCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.ElencoFascicoliCumulo.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value = aIdTitolo;
      document.ElencoFascicoliCumulo.submit();
    }
    
    function escludiTitolo(aIdTitolo, adocReg)
    {
      var conferma = "S";
      if(adocReg=='N')
      { 
        var msgConfirm = "Attenzione. Risulta già prodotta la stampa del provvedimento di unificazione, "
          + "prima della validazione sarà necessario includere nuovamente il titolo "
          + "o escluderlo definitivamente; Confermi? ";
          
        if (window.confirm(msgConfirm))
          {conferma = "S"}
        else
          {conferma = "N"}
      } 
      
      if (conferma=="S") {
        lAzione = "siap.siep.istruttoriacumulo.action.ActEscludiTitolo";
        document.ElencoFascicoliCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ElencoFascicoliCumulo.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value = aIdTitolo;
        document.ElencoFascicoliCumulo.submit();
      }
    }

    function reincludiTitolo(aIdTitolo, adocReg)
    {
      var conferma = "S";
      if(adocReg=='N')
      {   
        var msgConfirm = "Attenzione. Risulta già prodotta la stampa del provvedimento di unificazione, "
                       + "verificare la congruità del provvedimento a seguito della inclusione del titolo; Confermi?";
      
        if (window.confirm(msgConfirm))
          {conferma = "S"}
        else
          {conferma = "N"}
      }
      
      if (conferma=="S") {
        lAzione = "siap.siep.istruttoriacumulo.action.ActReincludiTitolo";
        document.ElencoFascicoliCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ElencoFascicoliCumulo.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value = aIdTitolo;
        document.ElencoFascicoliCumulo.submit();
      }
    }
    
    
    function eliminaTitolo(aIdTitolo, adocReg)
    {
      var msgConfirm = "Tutti i dati estratti, inseriti e modificati del titolo selezionato verranno eliminati."
                      +" Tale operazione avrà effetto SOLO sui dati presenti in questa istruttoria. Si vuole procedere?"; 
      
      var msgConfirmN = "Tutti i dati estratti, inseriti e modificati del titolo selezionato verranno eliminati."
              +" Tale operazione avrà effetto SOLO sui dati presenti in questa istruttoria. "
              +" Inoltre, si dovrà verificare la congruità del provvedimento in quanto "
              +" Risulta già prodotta la stampa del provvedimento di unificazione. Si vuole procedere?";
      
      var messaggiodiconferma="";
      if(adocReg=='N')
      {
        messaggiodiconferma += msgConfirmN;
      } 
      else
      {
        messaggiodiconferma += msgConfirm;
      } 
      
      if (window.confirm(messaggiodiconferma)) {
        lAzione = "siap.siep.istruttoriacumulo.action.ActCancellaTitolo";
        document.ElencoFascicoliCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ElencoFascicoliCumulo.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value = aIdTitolo;
        //alert('Elimina... - lAzione = '+lAzione);
        document.ElencoFascicoliCumulo.submit();
      }
    }
    
    
    function blink(selector){
      $(selector).fadeOut('slow', function(){
        $(this).fadeIn('slow', function(){
          blink(this);
        });
      });
    }
    
    //==========================================================================
    // Ritorna alla Griglia Della Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {
      document.ElencoFascicoliCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.ElencoFascicoliCumulo.submit();
    }

    //==========================================================================
    // Richiama la funzione di inserimento fascicoli coinvolti
    //==========================================================================
    function nuovoTitolo(aIdIstru)
    {
      lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciTitoloCumulato";
      document.ElencoFascicoliCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.ElencoFascicoliCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>.value = aIdIstru;
      document.ElencoFascicoliCumulo.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value = "";
      document.ElencoFascicoliCumulo.submit();
    }
    
    function ricercaTitoloUfficio (aIdIstru)
    {
      var desktop;
      var stringaChiamata = "/jsp/Main.jsp?Action=siap.siep.istruttoriacumulo.action.ActRicercaPropriProcedimenti";
      //stringaChiamata += "&formname=DettaglioRichiesta";
      
      <%
      SoggettoModel soggetto = (SoggettoModel) session.getAttribute("soggetto"); 
      %>
      
      //stringaChiamata += "&< %=ICostantiSoggetto.CAMPO_COGNOME%>=< %=soggetto.getCognome()%>";
      //stringaChiamata += "&< %=ICostantiSoggetto.CAMPO_NOME%>=< %=soggetto.getNome()%>";
      
      stringaChiamata += "&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>";
      //stringaChiamata += "&ICostantiIstruttoriaCumulo=ChiaveProgrSiep";
      
      document.location.href=stringaChiamata;
      
      <%--
      lAzione = "siap.siep.istruttoriacumulo.action.ActRicercaPropriProcedimenti";
      document.ElencoFascicoliCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.ElencoFascicoliCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>.value = aIdIstru;
      document.ElencoFascicoliCumulo.submit();
      --%>
    }
    
    //==========================================================================
    // Richiama la form di selezione Fascicoli Presi in Carico / Trasmessi
    //==========================================================================
    function selezionaTitoloDaPresaInCarico(){
      lAzione = "siap.siep.istruttoriacumulo.action.ActLoadCaricaFascicoliTrasmessi";
      document.ElencoFascicoliCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.ElencoFascicoliCumulo.submit();
    }
    
    //==========================================================================
    // Richiama la Funzionalità di Iscrizione da NSC
    //==========================================================================
    function selezionaTitoloDaNsc() {
      	// MEV 16 CUMULO: aggiunta funzione x collegamento con NSC
      	var lAzione = "siap.sico.webservice.action.ActCollegamentoNscCumulo";
      	<% session.setAttribute("IdIstruttoriaCumulo", IstruttoriaCumulo.getIdIstruttoriaCumulo()); %>
      	var desktop = window.open("/jsp/Main.jsp?Action=" + lAzione, "SelezionaTitoloDaNsc", "toolbar = no, location = no, status = no, menubar = no, scrollbars = yes, resizable = yes, width = " + screen.availWidth +", height = " + screen.availHeight + ", top = 0, left = 0");
    }
    
$(document).ready(function() {
	blink('.alertIcon');
});
</script>
</head>

<body class="corpo">
<table>
  	<tr>
		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
  		<td class="LBG">
			<font class="label">Funzione :</font>&nbsp;
			<font class="campo">Elenco Provvedimenti Esecutivi Coinvolti</font>
  		</td>
<%
if (ParentFormName.equals("ElencoEstesoIstruttorieCumulo")) {
%>
		<td class="LBG"><!-- Tasto indietro alla Ricerca Estesa Istruttorie -->
			<a href="Javascript:history.go(-1);"><img src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="Ritorna su Elenco Istruttorie" border=0></a>
  		</td>
<%
} else {
%>
  		<td class="LBG"><!-- Tasto indietro alla Griglia Della Gestione Cumulo -->
    		<a href="javascript:tornaIndietro('siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo')">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
    		</a>
  		</td>
<%
}
%>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
<br>

<%
String lRet = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.istruttoriacumulo.action.ActLoadElencoFascicoliCoinvolti";
lRet += "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" + IstruttoriaCumulo.getIdIstruttoriaCumulo();

String lNuovoOrdinamentoDataIrrevoc = "";
String lNuovoOrdinamentoDataTitolo = "";

// Nuovo Ordinamento per DATA IRREVOCABILITA: se la lista è ordinata per data irrevocabilità,
// cambio ordinamento. Altrimenti ordino per default per data Desc
if (Ordinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_ASC))
  	lNuovoOrdinamentoDataIrrevoc = ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC;
else if (Ordinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC))
  	lNuovoOrdinamentoDataIrrevoc = ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_ASC;
else
 	lNuovoOrdinamentoDataIrrevoc = ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC;

// Nuovo Ordinamento per DATA PROVVEDIMENTO: se la lista è ordinata per data provvedimento,
// cambio ordinamento. Altrimenti ordino per default per data Desc
if (Ordinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_ASC))
  	lNuovoOrdinamentoDataTitolo = ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_DESC;
else if (Ordinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_DESC))
  	lNuovoOrdinamentoDataTitolo = ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_ASC;
else
  	lNuovoOrdinamentoDataTitolo = ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_DESC;

String lEtichettaDataTitolo = "";
if (Ordinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_ASC)
		|| Ordinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_DESC))
	lEtichettaDataTitolo = "<font class='cRosso'>Data Titolo</font>";
else
  	lEtichettaDataTitolo = "Data Titolo";

String lEtichettaDefinitivoIl = "";
if (Ordinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_ASC)
		|| Ordinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC))
	lEtichettaDefinitivoIl = "<font class='cRosso'>Definitivo il</font>";
else
  	lEtichettaDefinitivoIl = "Definitivo il";

// Testo Eventuale Provvedimento di Cumulo (Dati Finali)
String lDocReg = "";

EventoModel lEvento = ProvvedimentoCumulo;
if (lEvento!=null && lEvento.getIdEvento() != null) {
    // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("---XXXX--- Elencofascicoli.jsp - IdEve = "+lEvento.getIdEvento());
    if (lEvento.getFlagDocumentoRegistrato() != null) {
       lDocReg = lEvento.getFlagDocumentoRegistrato();
    } 
}
%>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ElencoFascicoliCumulo">
<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
<input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>" value="">
<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">

<table cellspacing="2" cellpadding="2" align="center" width="95%">
	<tr>
		<%-- Inserire qui le intestazioni delle colonne che si vogliono visualizzare --%>
      	<td class="int">Titolo</td><!-- Sentenza/Decreto/Cumulo-->
      	<td class="int">N°</td>
      	<td class="int"><%=lEtichettaDataTitolo%><br>
        	<a href="<%=lRet%>&Ordinamento=<%=lNuovoOrdinamentoDataTitolo%>">
         		<img align="bottom" src="<%=IWebConstants.IMAGES_DIR%>/bottoni/modifica.gif" alt="ordina per" border="0">
         	</a>
      	</td>
      	<td class="int">Autorità Emittente</td>
      	<td class="int">Anno/Numero <br>Reg.Gen.</td>
      	<td class="int"><%=lEtichettaDefinitivoIl%><br>
        	<a href="<%=lRet%>&Ordinamento=<%=lNuovoOrdinamentoDataIrrevoc%>">
        		<img align="bottom" src="<%=IWebConstants.IMAGES_DIR%>/bottoni/modifica.gif" alt="ordina per" border="0">
        	</a>
      	</td>
		<td class="int">Anno/Numero <br>SIEP</td>
		<td class="int">Autorità</td>
		<td class="int">Azioni</td>
	</tr>
<%
boolean isTitoloManuale = false;
int id_record = 0;
Iterator itx = ListaTitoli.iterator();
while (itx.hasNext()) {
	id_record = id_record+1;
    TitoloCumulatoModel lTitoloModel = (TitoloCumulatoModel) itx.next();
    boolean isTitoloCumulante = false;
    if (lTitoloModel.getProcedimentoCumulato() != null
    		&& lTitoloModel.getProcedimentoCumulato().getIdFascicoloSiepOrigine() != null
        	&& lTitoloModel.getProcedimentoCumulato().getIdFascicoloSiepOrigine().compareTo(IstruttoriaCumulo.getFasSieIdFascicoloSiep()) == 0) {
		isTitoloCumulante = true;
    }
    String reg = StringUtils.toStringJSP(lTitoloModel.getTipoRegGen(), "");
    String anno_reg = StringUtils.toStringJSP(lTitoloModel.getAnnoRegGen(), "");
    String num_reg = StringUtils.toStringJSP(lTitoloModel.getNumeroRegGen(), "");
    String AutEmi = lTitoloModel.getDescrTipoAutoritaEmittente() + " di " + lTitoloModel.getDescrLuogoEmittente();
    if (lTitoloModel.getNumSezioneAutoritaEmittente() != null)
		AutEmi += " - sez. "+lTitoloModel.getNumSezioneAutoritaEmittente();
    String nSiep = "";
    String AutoritaSiep = "";
    ProcedimentoCumulatoModel lProcedimentoCumulatoModel = lTitoloModel.getProcedimentoCumulato();
    if (lProcedimentoCumulatoModel != null) {
		if ("S".equals(lProcedimentoCumulatoModel.getFlagAccorpato())) {
        	UfficioModel lUfficioOrigine = lProcedimentoCumulatoModel.getUfficioOrigine();
			nSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() + "/" + lProcedimentoCumulatoModel.getChiaveProgrOrigine();
			nSiep += "<br> <font class=\"cRosso\">(Ex " + lUfficioOrigine.getCodTipoUfficio() + " di " + lUfficioOrigine.getDescrComune() + ")</font>";
      	} else {
        	nSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() +"/"+ lProcedimentoCumulatoModel.getChiaveProgrFasCumulato();
      	}
		AutoritaSiep = StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrTipoUfficioFasCumulato())+" di "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrLuogoUfficioFasCumulato());
	}
	if (isTitoloCumulante) {
%> 
	<tr style="background-color: rgb(255,255,153);">
<%
	} else {
%>
	<tr>
<%
	}
	// Grigio i dati dei procedimenti momentaneamente esclusi dal cumulo
	String lFontColor = "";
	if ("S".equals(lTitoloModel.getFlagEscluso())) {
		lFontColor = "style='color:grey;'";
	}
%>
		<td class="c" nowrap>
			<a href="javascript:dettaglioTitolo('rec_<%=id_record%>')" title="Dettaglio Titolo">
<% 
	String lDescrtipoTitolo = lTitoloModel.getDescrTipoProvvedimento();
	if ("02".equals(lTitoloModel.getCodTipoProvvedimento())) {
		String [] lUfficiSorv = new String[] {"UDS", "TDS", "UDSM"};
	  	if (!Arrays.asList(lUfficiSorv).contains(lTitoloModel.getCodTipoAutoritaEmittente())) {
	    	lDescrtipoTitolo = "Decreto Penale";
	  	}
	}
	String tipoCaricamento = "";
	// MEV 16 CUMULO: aggiunta or condition per i titoli provenienti da NSC
	if (lTitoloModel.getIdSentenzaOrigine() != null || "03".equals(lTitoloModel.getTipoIscrizione())) {
		tipoCaricamento = "";
	} else {
	  	tipoCaricamento = " <font class=\"label\"  style=\"font-size:8px;vertical-align: super;\" >(*)</font>";
	  	isTitoloManuale = true;
	}
%>
				<%=lDescrtipoTitolo%></a><%=" " + tipoCaricamento%>
		</td>
		<td class="c" <%=lFontColor%> nowrap>          
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActLoadDettaglioTitoloCumulato&<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>=<%=lTitoloModel.getIdTitoloCumulato()%>&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=lTitoloModel.getIstrIdIstruttoriaCumulo()%>" title="Dettaglio Titolo">
				<%=lTitoloModel.getAnnoSentenza()%> / <%=lTitoloModel.getNumeroSentenza()%>
			</a>
		</td>
		<td class="c" <%=lFontColor%> nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloModel.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></td>
		<td class="c" <%=lFontColor%>>&nbsp;<%=AutEmi%></td>
		<td class="c" <%=lFontColor%> nowrap>&nbsp;<%=anno_reg%>/<%=num_reg%>&nbsp;<%=reg%></td>
		<td class="c" <%=lFontColor%> nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloModel.getDataIrrevocabilita(),"dd-MM-yyyy"))%></td>
<%
	if (lProcedimentoCumulatoModel != null) {
%>
		<td class="c" <%=lFontColor%> nowrap>
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActLoadDettaglioProcedimentoCumulato&<%=ICostantiProcedimentoCumulato.CAMPO_ID_PROCEDIMENTO_CUMULATO%>=<%=lProcedimentoCumulatoModel.getIdProcedimentoCumulato()%>&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=lTitoloModel.getIstrIdIstruttoriaCumulo()%>" title="Dettaglio Procedimento">
            	<%=nSiep%>
            </a>
		</td>
<%
	} else {
%>
		<td class="c" <%=lFontColor%> nowrap>&nbsp;</td>
<%
	}
%>
		<td class="c" <%=lFontColor%> >&nbsp;<%=AutoritaSiep%></td>
		<td class="c" style="text-align:left" nowrap>
			<a href="javascript:caricaDatiAnalitici(<%=lTitoloModel.getIdTitoloCumulato()%>)"><img src="/images/vedi24.gif" title="Dettaglio dati Analitici" border="0"></a>
<%
	if ("A".equals(IstruttoriaCumulo.getFlagStato()) && !isTitoloCumulante) {
		if ("S".equals(lTitoloModel.getFlagEscluso())) {
%>
			<a href="javascript:reincludiTitolo(<%=lTitoloModel.getIdTitoloCumulato()%>,'<%=lDocReg%>')"><img src="/images/lucchettoaperto24.gif" title="Reincludi Titolo" border="0"></a>
<%
		} else {
%>
			<a href="javascript:escludiTitolo(<%=lTitoloModel.getIdTitoloCumulato()%>,'<%=lDocReg%>')"><img src="/images/lucchetto24.gif" title="Escludi Titolo" border="0"></a> 
<%
		}
%>
			<a href="javascript:eliminaTitolo(<%=lTitoloModel.getIdTitoloCumulato()%>,'<%=lDocReg%>')"><img src="/images/delete24.gif" title="Elimina Titolo" border="0"></a> 
<%
	}
%>  
		</td>
	</tr>
	<!-- Record Hidden con il dettaglio del Titolo-->
<%
	SoggettoCumulatoModel lSoggettoCumulato = lTitoloModel.getSoggettoCumulato();
	boolean isStessoSoggetto = true;
	String lStrDisplay = "style=\"display:none\"";
	String lStrSoggetto = "";
	if (lSoggettoCumulato != null) {
  		isStessoSoggetto = lSoggettoCumulato.isStessoSoggetto(lSoggettoFascicolo);
  		if (!isStessoSoggetto) 
    		lStrDisplay = "style=\"display:block\"";        
  		lStrSoggetto += "<font class=\"label\">Soggetto</font> ";
		String lLinkDettagliSoggetto = "";
		lLinkDettagliSoggetto += "<a class=\"cliccabile\" href=\""+IWebConstants.PG_MAIN;
		lLinkDettagliSoggetto += "?"+IWebConstants.ACTION_FIELD+"=siap.siep.modulocumulo.action.ActDettaglioSoggettoCumulato";
		lLinkDettagliSoggetto += "&"+ICostantiSoggettoCumulato.CAMPO_ID_SOGGETTO_CUMULATO+"="+lSoggettoCumulato.getIdSoggettoCumulato();
		lLinkDettagliSoggetto += "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+lTitoloModel.getIstrIdIstruttoriaCumulo();
		lLinkDettagliSoggetto += "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO+"="+lTitoloModel.getIdTitoloCumulato();
		lLinkDettagliSoggetto += "\" title=\"Dettaglio Soggetto\" >";
		lLinkDettagliSoggetto += StringUtils.toStringJSP(lSoggettoCumulato.getCognome()) +"&nbsp;"+StringUtils.toStringJSP(lSoggettoCumulato.getNome());
		lLinkDettagliSoggetto +="</a>&nbsp;";
		lStrSoggetto += lLinkDettagliSoggetto;
		// lStrSoggetto += "<font class=\"campo\">"+StringUtils.toStringJSP(lSoggettoCumulato.getCognome()) +"&nbsp;"+StringUtils.toStringJSP(lSoggettoCumulato.getNome())+"</font>";
		// Sesso
		if (lSoggettoCumulato.getSesso().compareTo("F")==0){
		  	lStrSoggetto += " <font class=\"label\">nata il :</font>&nbsp;";
		} else {
		  	lStrSoggetto += " <font class=\"label\">nato il :</font>&nbsp;";
		}
		// Data Nascita
		if (lSoggettoCumulato.getDataNascita() == null) {
		  	if ("S".equals(lSoggettoCumulato.getDataNascitaPresunta())) {
		    	lStrSoggetto += " <font class=\"campo\">" + StringUtils.toStringJSP(lSoggettoCumulato.getAnnoNascita()) + "</font>&nbsp;";
		  	} else {
		    	lStrSoggetto += " <font class=\"campo\">***</font>&nbsp;";
		  	}
		} else {
		  	lStrSoggetto += " <font class=\"campo\">"+StringUtils.toStringJSP(DateUtils.getDateToString(lSoggettoCumulato.getDataNascita(), "dd-MM-yyyy")) + "</font>&nbsp;";
		}
		// Luogo di nascita
		lStrSoggetto += " <font class=\"label\">in : </font>";
		lStrSoggetto += " <font class=\"campo\">";
		if (lSoggettoCumulato.getDescrComuneNascita().compareTo("-") == 0) {
		 	lStrSoggetto += StringUtils.toStringJSP(lSoggettoCumulato.getDescComuneNascitaEstero(), "") + " (" + lSoggettoCumulato.getDescrStatoNascita().toUpperCase() + ")";
		}
		else {
		  	lStrSoggetto += StringUtils.toStringJSP(lSoggettoCumulato.getDescrComuneNascita(), "" ) + " (" + lSoggettoCumulato.getCodProvinciaNascita() + ")";
		}
		lStrSoggetto += "</font>";
		
		lStrSoggetto +=" <font class=\"label\">Codice CUI: </font>";
  		lStrSoggetto +=" <font class=\"campo\">" + StringUtils.toStringJSP(lSoggettoCumulato.getCodAfis()) + "</font>";
	}
%>
	<tr <%=lStrDisplay%> id="rec_<%=id_record%>">
		<td class="l" colspan="100%">
<%
	if (!isStessoSoggetto) {
%>
			<font color="red"><b><img src="/images/attenzione.jpg" width="12" height="12" class="alertIcon" alt="Attenzione. L'Anagrafica del Soggetto del titolo cumulato differisce in parte dall'Anagrafica del Soggetto del Titolo Cumulante" border="0"></b></font>
<%
	}
%>
			<!--font class="campoSmall"-->
			<!-- VERIFICARE SE E' IL CASO DI INSERIRE QUI ANCHE ALCUNI DATI ANALITICI -->
				<%=StringUtils.toStringJSP(lStrSoggetto,"&nbsp;")%>
			<!--/font-->
	  	</td>
	</tr>
<%
} // end while su iterator
if (ListaTitoli.size() == 0) {
%>
	<tr>
		<td>Nessun dato presente</td>
    </tr>
<%
}
if (isTitoloManuale) {
%>
	<tr>
      	<td colspan="100%">
        	<font class="label"> (*) Titolo iscritto manualmente</font>
      	</td>
    </tr>
<%
}
%>
</table>
<%
if ("A".equals(IstruttoriaCumulo.getFlagStato())) {
	// MEV 16 CUMULO: gestita casistica per cui far vedere il bottone di "Iscrizione Nuovo Titolo da NSC"
	boolean onOffButtonINTDNSC = "off".equalsIgnoreCase(F3BProperties.getProperty("onOffButtonINTDNSC")) ? true : false;
%>
<table cellspacing="2" cellpadding="2" align="center" width="95%">
	<tr>
      	<td style="text-align:left">
        	<INPUT class="bottone" type="button" name="AGGIUNGI" style="width:200" value="Iscrizione Titolo Pervenuto" onClick="javascript:selezionaTitoloDaPresaInCarico();" title="Iscrizione/Presa in carico">
<%
	if (!onOffButtonINTDNSC) {
%>
        	<INPUT class="bottone" type="button" name="AGGIUNGI" style="width:190" value="Iscrizione Nuovo Titolo da NSC" onClick="javascript:selezionaTitoloDaNsc();" title="Iscrizione/NSC">
<%
	}
%>
      	</td>      
      	<td style="text-align:right">
        	<INPUT class="bottone" type="button" name="INSERISCI" value="Iscrizione Proprio Procedimento" onClick="javascript:ricercaTitoloUfficio(<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>);" title="Ricerca Procedimenti Nell'ufficio">
        	<INPUT class="bottone" type="button" name="INSERISCI" value="Iscrizione Manuale Titolo" onClick="javascript:nuovoTitolo(<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>);" title="Iscrizione Manuale Titolo Coinvolto">
      	</td>      
	</tr>
</table>
<%
}
%>
</FORM>
</body>
</html>
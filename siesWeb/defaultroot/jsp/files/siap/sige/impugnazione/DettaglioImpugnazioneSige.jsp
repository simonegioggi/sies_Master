<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>
<%@ page import="siap.sige.impugnazione.model.ImpugnazioneSigeModel"%>
<%@ page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>

<jsp:useBean id="FascicoloSigeEsteso" 		 scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="impugnazione" 				 scope="request" class="siap.sige.impugnazione.model.ImpugnazioneSigeModel"/>
<jsp:useBean id="ElencoTemplate"     		 scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"        			 scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoImpugnazione" 		 scope="session" class="java.lang.String"/>
<jsp:useBean id="showComboTemplate" 		 scope="request" class="java.lang.String"/>
<jsp:useBean id="showDestinatari" 			 scope="request" class="java.lang.String"/>
<jsp:useBean id="UtenteConnesso" 			 scope="session" class="siap.sico.utente.model.UtenteModel" />
<%-- MAC 2017/04/21 --%>
<jsp:useBean id="fascicoloCollegato" 		 scope="request" class="java.lang.String" />
<jsp:useBean id="showDestinatariOpposizione" scope="request" class="java.lang.String"/>
<!-- @emma 12072018 intervento post COLLAUDO 11.2  -->
<jsp:useBean id="validazioneEsito" scope="request" class="java.lang.String"/>


<%
FascicoloSigeModel fascicolo=FascicoloSigeEsteso.getFascicoloSige();
ProvvedimentoSigeEventoModel provvedimento = impugnazione.getProvvedimentoSige();
String codTipoUfficio = UtenteConnesso.getUfficioUtente().getCodTipoUfficio();
// 20190603 [SG]: aggiunto controllo preventivo --- Ticket#201911050112
String validato = "N";
if (impugnazione.getProvvedimentoSigeGenerato() != null
		&& impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica() != null
		&& impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento() != null
		&& impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento().getFlagDocumentoRegistrato() != null)
	validato = impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento().getFlagDocumentoRegistrato();
//@emma 13072018 intervento post COLLAUDO 11.2
String MYvalidazioneEsito = validazioneEsito;
%>

<html>
  <head>
    <title>[S.I.E.S.] - Ricorso  / Impugnazione</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

 <script language="JavaScript">
   function stampaImpugnazioneSige(titolo) {
	      var template = "&CodTemplate=";
	      var  hrefStampa = "<%=IWebConstants.ACTION_FIELD%>=siap.sige.impugnazione.action.ActStampaImpugnazioneSige&IdImpugnazione=<%=impugnazione.getIdImpugnazioneSige()%>";        	      
	       //INIZIO @emma 13072018 intervento post COLLAUDO 11.2
	      if(titolo == 'esito'){
	    	  if(<%=!"S".equals(MYvalidazioneEsito)%>){
			   	document.comandi.validazioneEsito.value = 'S';
	    	  }
		  }
	      //FINE @emma 13072018 intervento post COLLAUDO 11.2
	      
	      // Se nel dettaglio esiste la Lista di Template si legge il valore
	      if (document.DettaglioImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_COD_TEMPLATE%> != undefined)
	      {
	          template =  template + document.DettaglioImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_COD_TEMPLATE%>.value;
	          hrefStampa += template;
	      }
	     stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  hrefStampa);
   }
   
   function IscriviProcedimentoCollegato() {
	   lLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadInserisciFascicoloCollegato&<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE%>=<%=impugnazione.getIdImpugnazioneSige().toString()%>&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=fascicolo.getIdFascicoloSige().toString()%>";
	   window.location=lLink;
   }
   
   function FissazioneUdienza() {
	   lLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.udienza.action.ActLoadInserisciFissazioneUdienza&<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>=<%=fascicolo.getChiaveAnno().toString()%>&<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR%>=<%=fascicolo.getChiaveProgr().toString()%>&inserisci=Si&provenienza=EsitoImpugnazione";
	   window.location=lLink;
   }

   // carica la nota da stampare a seconda dell'ufficio dell'utente connesso
   // Gli utenti Tribunale, Gip Tribunale mandano al PM
   // Gli utenti Corte Appello e Corte Assise di Appello mandano al PG
   // I minorenni al PMM
   function caricaNota(codTipoUfficio) {
	  if(codTipoUfficio == 'CAP' || codTipoUfficio == 'CASAP'){
	      // PG
	  	 document.DettaglioImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_COD_TEMPLATE%>.value = 'SIGE_IM_002';
 	  } else if(codTipoUfficio == 'DIBM' || codTipoUfficio == 'GIPM'){
		 // PMM
  		 document.DettaglioImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_COD_TEMPLATE%>.value = 'SIGE_IM_005';
 	  } else if (<%=impugnazione.getSoggettoImpugnante() != null && impugnazione.getSoggettoImpugnante().startsWith("Opposizione")%>) {
 	 	 // Siamo nel caso di un Opposizione convertita in Ricorso, di default visualizzo 
 	 	 // la nota "Nota trasmissione alla Cassazione presentazione ricorso"   
 		 document.DettaglioImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_COD_TEMPLATE%>.value = 'SIGE_IM_004';
 	  // MAC 2017/04/21 Inizio
 	  } else if (<%=impugnazione.getCodTipoImpugnazione() != null && impugnazione.getCodTipoImpugnazione().equals("04") && 
 			     impugnazione.getCodTenoreDecisione() != null && (impugnazione.getCodTenoreDecisione().equals("05") ||
 			      impugnazione.getCodTenoreDecisione().equals("13") ) %>) {
		 // Siamo nel caso di Opposizione rigettata/inammissibile
 		 document.DettaglioImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_COD_TEMPLATE%>.value = 'SIGE_IM_006';	
      } else if (<%=impugnazione.getCodTipoImpugnazione() != null && impugnazione.getCodTipoImpugnazione().equals("01") && 
			     impugnazione.getCodTenoreDecisione() != null && (impugnazione.getCodTenoreDecisione().equals("05") ||
			      impugnazione.getCodTenoreDecisione().equals("04") ) %>) {
    	// Siamo nel caso di Ricorso rigettato/inammissibile
  		 document.DettaglioImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_COD_TEMPLATE%>.value = 'SIGE_IM_007';	
      // MAC 2017/04/21 Fine
      } else {
		 // PM
   		 document.DettaglioImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_COD_TEMPLATE%>.value = 'SIGE_IM_003';
 	  }
   }

   function IscriviEsito() {
	   lLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.impugnazione.action.ActLoadInserisciEsitoImpugnazioneSige&<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE%>=<%=impugnazione.getIdImpugnazioneSige().toString()%>&<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>=<%=provvedimento.getProvvedimento().getIdProvvedimentoSige().toString()%>&tipoOper=A&TornaQui=20";
	   window.location=lLink;
   }
   
   //@emma 13072018 intervento post COLLAUDO 11.2
   function mylookUpload(titolo){	  
	   if(titolo == 'esito'){
		   if(<%=!"S".equals(MYvalidazioneEsito)%>){
			   	document.comandi.validazioneEsito.value = 'S';
	       }
	   }
	   return lookUpload();
   }
	
 	</script>
	</head>

	<body class="corpo" onLoad="javascript:caricaNota('<%=codTipoUfficio%>');">
  	<FORM name='DettaglioImpugnazioneSige'>
    	<input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=(impugnazione.getProvvedimentoSigeGenerato()==null?"":impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento().getIdEvento().toString())%>">
    	<table>
      		<tr>
      			<td class="LBG">
      				<a href="Javascript:window.print();">
      					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
      				</a>
      			</td>
        		<td class=LBG><font class="label">Funzione :</font>&nbsp;
<%
String lTitolo = new String();
//@emma 10072018 intervento post COLLAUDO 11.2 (mi salvo se sto arrivando da un ricorso da opposizione)
String lTitoloNascosto = new String();
String fromOpposione = (impugnazione.getSoggettoImpugnante() != null && impugnazione.getSoggettoImpugnante().startsWith("Opposizione"))==true?"S":"N";
if (impugnazione.getCodTipoImpugnazione().equals("04") && "N".equals(fromOpposione))
	lTitolo = "Opposizione";
else
	lTitolo = "Ricorso";
lTitoloNascosto=lTitolo;
%>
          			<font class="campo">Dettaglio <%=lTitolo%></font>
        		</td>
<%-- 20170703: eliminata icona di stampa sempre presente e modificata gestione visualizzazione icone stampa-upload --%>
<%
//20190603 [SG]: aggiunto controllo preventivo --- Ticket#201911050112
String esito = "-";
if (impugnazione.getProvvedimentoSigeGenerato() != null
		&& impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica() != null
		&& impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento() != null
		&& impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento().getCodEsito() != null)
	esito = impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento().getCodEsito();
boolean existEsito = (esito != null && esito.length() > 0 && !"-".equals(esito)) ? true : false;
boolean isTrasmissibile = false;
if(existEsito)
	lTitoloNascosto = "esito";
if (showComboTemplate.equalsIgnoreCase("true")) {	
%>
				<!-- BOTTONE DI STAMPA -->
        		<td class="LBG"><a href="Javascript:stampaImpugnazioneSige('<%=lTitoloNascosto%>');" onclick="javascript:lookUpload();">
						<img  align="middle" src="/images/print24.gif" alt="Generazione Stampa <%=lTitolo%>" width="24" height="24" border="0">
					</a>
				</td>
<%
	}
%>
        		<%-- Icona di modifica è presente nell'elenco delle opposizioni
        		<td class="LBG">
          			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.impugnazione.action.ActLoadModificaImpugnazioneSige&<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE %>=<%=impugnazione.getIdImpugnazioneSige()%>&TornaQui=20">
            			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica <%=lTitolo%>" width="24" height="24" border="0">
          			</a>
        		</td>
 				--%>
<%	  
 if ( (!validato.equalsIgnoreCase("S") && impugnazione.isValidabile() && !existEsito)
		|| ("S".equals(validato) && existEsito && "N".equals(MYvalidazioneEsito))) {
 %>   
        		<td class="LBG">
          			<a href="javascript:mylookUpload('<%=lTitoloNascosto%>');" >
            			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>upload24.gif" alt="Valida <%=lTitoloNascosto%>" width="24" height="24" border="0">
          			</a>
        		</td>
<%
	}
   	if (impugnazione.isCancellabile() && impugnazione.isAnnullabile()) {
%>
	 			<td class="LBG">
          			<a href="Javascript:conferma('siap.sige.impugnazione.action.ActEliminaImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE %>','<%=impugnazione.getIdImpugnazioneSige().toString() %>','TornaQui','10');">
	         	 		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella <%=lTitoloNascosto %>" width="24" height="24" border="0">
 	      			</a>
        		</td>
 <%
 	}
   	
    //@emma 12072018 intervento post COLLAUDO 11.2 (gestione cancellazione ESITO)
   	if ("S".equals(validato) && existEsito && "N".equals(MYvalidazioneEsito)) {
%>
	 			<td class="LBG">
          			<a href="Javascript:conferma('siap.sige.impugnazione.action.ActEliminaEsitoImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE %>','<%=impugnazione.getIdImpugnazioneSige().toString() %>','TornaQui','10');">
	         	 		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella <%=lTitoloNascosto %>" width="24" height="24" border="0">
 	      			</a>
        		</td>
 <%
 	}
   	
//}  // end if (!validato.equalsIgnoreCase("S")) {
if (impugnazione.isTrasmissibile() || isTrasmissibile) {
%>
        		<td class="LBG">
          			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.impugnazione.action.ActLoadTrasmissioneImpugnazioneSige&IdImpugnazione=<%=impugnazione.getIdImpugnazioneSige()%>&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=(impugnazione.getProvvedimentoSigeGenerato()==null?"":impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento().getIdEvento().toString())%>" >
            			<img  align="middle" src="/images/net24.gif" alt="Trasmissione <%=lTitolo%>" width="24" height="24" border="0">
          			</a>
        		</td>
<%
}
%>
        		<!-- BOTTONE DI RITORNO -->
          		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      		</tr>
		</table>
    	<br />
    	<jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    	<br />

    	<table cellspacing=2 cellpadding=2>
      		<tr>
        		<td class="label">Avverso il Provvedimento:</td>
      		</tr>
      		<tr>
         		<td>
         			<font class="campo"><%=provvedimento.getProvvedimento().getDescrTipoProvvedimento()%> N. <%=(provvedimento.getProvvedimento().getChiaveAnno()==null?"-": provvedimento.getProvvedimento().getChiaveAnno())%>/<%=(provvedimento.getProvvedimento().getChiaveProgr()==null?"-": provvedimento.getProvvedimento().getChiaveProgr())%></font>
         			<font class="Label"> del </font>
         			<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getProvvedimento().getDataEmissione(),"dd/MM/yyyy"))%></font>
         			<font class="Label"> depositato il </font>
         			<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getProvvedimento().getDataDeposito(),"dd/MM/yyyy"),"-")%></font>
         		</td>
      		</tr>
      	</table>

    <%-- LISTA DEI RICORSI ANNULLATI -->
    <jsp:include page="<%=ICostantiImpugnazione.PG_LISTA_ANNULLATE%>">
    <jsp:param name="ValoreIdEvento" value="<%=provvedimento.getIdEvento()%>" />
    <jsp:param name="ValoreTipoProv" value="<%=provvedimento.getCodTipoProvvedimento()%>" />
     </jsp:include--%>

    <table cellspacing=2 cellpadding=2 width="100%">
<% if(impugnazione.getDataAnnullamento() != null && impugnazione.getFlagAnnullamento() != null && impugnazione.getFlagAnnullamento().equalsIgnoreCase("S"))
   {
%>
 <tr>
      <td class="l"><font class="cRosso"><%=impugnazione.getDescrTipoImpugnazione() + " "%> ANNULLATO</font></td>
 </tr>
      <tr>
        <td class="l"> Data di annullamento </td>
        <td class="L"><%=DateUtils.getDateToString(impugnazione.getDataAnnullamento(),"dd-MM-yyyy")%>
        </td>
      </tr>
      <tr>
        <td class="l">Motivo annullamento</td>
        <td class="l"><%=(impugnazione.getMotivoAnnullamento() == null ) ? "-" : impugnazione.getMotivoAnnullamento()%>
        </td>
      </tr>
<%
   }
%>
       <tr>
        <td class="l">Anno/Numero&nbsp; - <%=impugnazione.getDescrTipoImpugnazione()%></td>
        <td class="L"><%=impugnazione.getAnnoS7()%>/<%=impugnazione.getProgrS7()%>
        </td>
      </tr>

     <%--  <tr>
        <td class="l">Tipo </td>
        <td class="L"><%=impugnazione.getDescrTipoImpugnazione()%>
        </td>
      </tr> --%>

<!-- Siamo nel caso di un Opposizione convertita in Ricorso, visualizzo il campo 
     Soggetto Impugnante che contiene la descrizione "Opposizione N. annoOpp/numOpp", 
     mentre non dovranno essere visualizzati i campi Data Atto, Data Arrivo 
     in Cancelleria e Presentato Da -->
	<% if(impugnazione.getSoggettoImpugnante() != null && impugnazione.getSoggettoImpugnante().startsWith("Opposizione")){ %>
		  <tr>
	        <td class="l">Proveniente da </td>
	        <td class="L"><%=impugnazione.getSoggettoImpugnante()%>
	        </td>
	      </tr>
	<% } else { %>      
		  <tr>
	        <td class="l">Presentato da </td>
	        <td class="L"><%=impugnazione.getDescrSoggettoImpugnante()%>
	        </td>
	      </tr>
	<% } %>
	
	<tr>
       <td class="l">Data atto </td>
       <td class="L"><%=DateUtils.getDateToString(impugnazione.getDataRicorso(),"dd-MM-yyyy")%>
       </td>
     </tr>

     <tr>
       <td class="l">Data arrivo in cancelleria </td>
       <td class="L"><%=DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria(),"dd-MM-yyyy")%>
     </tr>

<%
if (impugnazione.getCodTipoImpugnazione().equals("01")) {
    String labelAutorita = "Autorità Destinataria";
    
    if (impugnazione.getCodTenoreDecisione() != null && (impugnazione.getCodTenoreDecisione().equals("12") || impugnazione.getCodTenoreDecisione().equals("02")))
    	labelAutorita = "Autorità Mittente";
%>
      <tr>
        <td class="l">Data Trasmissione Atti </td>
        <td class="L"><%=DateUtils.getDateToString(impugnazione.getDataTrasmissioneAtti(),"dd-MM-yyyy") == null ? "-" : DateUtils.getDateToString(impugnazione.getDataTrasmissioneAtti(),"dd-MM-yyyy")%>
        </td>
      </tr>

      <tr>
        <td class="l"><%=labelAutorita%> </td>
        <td class="L"><%=impugnazione.getDescrAutoritaDestinataria() == null ? "-" : impugnazione.getDescrAutoritaDestinataria() %>
        </td>
      </tr>
<%
}

// 20191112 [SG]: refactoring
if (impugnazione.getDataDecisione() != null) {
%> 
	<tr>
   		<td class="l">Data decisione</td>
		<td class="L">
			<%=DateUtils.getDateToString(impugnazione.getDataDecisione(),"dd-MM-yyyy") == null ? "-" : DateUtils.getDateToString(impugnazione.getDataDecisione(),"dd-MM-yyyy")%>
		</td>
<%-- <% --%>
<!-- } else {  -->
<%-- %> --%>
<!-- 		<td class="L">-</td> -->
	</tr>
<%
}

if (impugnazione.getDescrTenoreDecisione() != null) {
%>
 	<tr>
   		<td class="l">Tenore decisione</td>
		<td class="L">
			<%=(impugnazione.getDescrTenoreDecisione() == null ? "-" : impugnazione.getDescrTenoreDecisione())%>
        </td>
<%-- <% --%>
<!-- } else { -->
<%-- %>  --%>
<!-- 		<td class="L">-</td> -->
	</tr>
<%
}

if (impugnazione.getDataRestituzioneAtti() != null) {
%>
 	<tr>
   		<td class="l">Data restituzione atti</td>
		<td class="L">
			<%=DateUtils.getDateToString(impugnazione.getDataRestituzioneAtti(),"dd-MM-yyyy") == null ? "-" : DateUtils.getDateToString(impugnazione.getDataRestituzioneAtti(),"dd-MM-yyyy")%>
		</td>
<%-- <% --%>
<!-- } else { -->
<%-- %>  --%>
<!-- 		<td class="L">-</td> -->
	</tr>
<%
}
%>

 	<tr>
		<td class="l">Note</td>
        <td class="l">
        	<%=(impugnazione.getAnnotazione() == null || impugnazione.getAnnotazione().equalsIgnoreCase("null")) ? "-" : impugnazione.getAnnotazione()%>
        </td>
	</tr>

 <%
 if (showDestinatari.equalsIgnoreCase("true")) {
 %>
 <tr>
 	<td colspan=2>     
    	<jsp:include page="<%=ICostantiImpugnazioneSige.PG_LOAD_DESTINATARI%>"/>
	</td>
</tr>
<%
}
%>      

 <%
 if (showDestinatariOpposizione.equalsIgnoreCase("true")) {
 %>
 <tr>
 	<td colspan=2>     
    	<jsp:include page="<%=ICostantiImpugnazioneSige.PG_LOAD_DESTINATARI_OPPOSIZIONE%>"/>
	</td>
</tr>
<%
}
//if (!validato.equalsIgnoreCase("S") ||  ("S".equals(validato) && existEsito && "N".equals(MYvalidazioneEsito))) {
	// //@emma 24072018 intervento post COLLAUDO 11.2 (combo dei template sempre visibile) richiesta da Nunzia
	if (showComboTemplate.equalsIgnoreCase("true")) {
%>     
      <tr>
        <td class="L">Documento da stampare </td>
        <td class="L">
          <select title="Documento da stampare" class=small name="<%=ICostantiImpugnazioneSige.CAMPO_COD_TEMPLATE%>" >
            <%= ElencoTemplate %>
          </select>
        </td>
      </tr>
<%	}


if(impugnazione.getDataAnnullamento() == null && impugnazione.getFlagAnnullamento() == null) {
      if (impugnazione.getCodTenoreDecisione() != null && (impugnazione.getCodTenoreDecisione().equals("02") || impugnazione.getCodTenoreDecisione().equals("12"))) {
      	  // MAC 2017/04/21	
    	  if(fascicoloCollegato != null && fascicoloCollegato.equals("N")) {
%>
		      <tr>
			      <td>
			        <input class="bottone" type="button" value="Iscrizione Procedimento Collegato" onclick="IscriviProcedimentoCollegato()" />
			      </td>
<%
    			if (impugnazione.getCodTenoreDecisione() != null && impugnazione.getCodTenoreDecisione().equals("12")) {
%>
			       	<td>
			        	<input class="bottone" type="button" value="Fissazione Udienza" onclick="FissazioneUdienza()" />
			      	</td>
<%
	    		}
%>
      		  </tr>
<%
      		} // end if(fascicoloCollegato == null) {
      }
      
	// MEV_15_S4 modifica del 27/02/2017
	// Il pulsante "Iscrivi Esito" deve essere visibile solo dopo la validazione
	// 20170703: la validazione è stata spostata lato esito: quindi levo il controllo
	
	//@emma 09072018 intervento post COLLAUDO 11.2 (il tasto iscrivi esito deve apparire solo se il provvedimento è validato)
	if ((validato.equalsIgnoreCase("S") )
			&& (impugnazione.getCodTenoreDecisione() == null
			|| (impugnazione.getCodTenoreDecisione() != null && impugnazione.getCodTenoreDecisione().equals("-")))) {
%>
	      <tr>
		      <td>
		        <input class="bottone" type="button" value="Iscrivi Esito" onclick="IscriviEsito()" />
		      </td>
		  </tr>
<%
	}
}
      %>
    </table>
  </form>

<!-- @emma 11072018 intervento post COLLAUDO 11.2 -->
<%  if ( (!validato.equalsIgnoreCase("S") && impugnazione.isValidabile() && !existEsito)
		|| ("S".equals(validato) && existEsito && "N".equals(MYvalidazioneEsito)) ) {
 
 %>   
	  	<div align=left style="visibility:hidden" id="upld">
	    	<FORM name="comandi" enctype="multipart/form-data" method="post">
	    	<!-- @emma 12072018 intervento post COLLAUDO 11.2 --> 
	    	<input type="HIDDEN" name="validazioneEsito" value="N">
	    	
	     	<table>
	          	<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
	        	<tr>
	          		<td class="L">
						<input  class=bottone  type="submit" value="Conferma">
						<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.impugnazione.action.ActUploadImpugnazione">
						<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=(impugnazione.getProvvedimentoSigeGenerato()==null?"":impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento().getIdEvento().toString())%>">
						<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.sige.impugnazione.action.ActUploadImpugnazione">
						<input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE %>" value="<%=impugnazione.getIdImpugnazioneSige().toString() %>">
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
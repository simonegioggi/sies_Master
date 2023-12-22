<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*"%>

<%@ page import="f3b.util.F3BException"%>
<%@ page import="f3b.web.Action"%>
<%@ page import="f3b.web.util.*"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.lock.model.LockModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<%@ page import="org.apache.log4j.MDC"%>
<%@ page import="org.apache.log4j.NDC"%>
<%@ page import="org.apache.log4j.Logger"%>

<%@ page language="java" session="true" errorPage="ErrorPage.jsp"%>

<%
final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
%>

<jsp:useBean id="action" class="f3b.web.Action" scope="session">
<%
action.setServletContext(application);
%>
</jsp:useBean>
<jsp:useBean id="lockTable" class="java.util.Hashtable" scope="application"/>
<%
/**
 * Controllo sull'attivazione dei Cookies.
 * Il controllo viene fatto solo dala seconda volta in poi.
 * Per il momento da commentare !!!!!
 */
//============================================================================
// n.b. in alcuni casi, quando viene chiusa la finestra di IE con il doc di 
//      stampa aperto 'inline', il browser invia delle request HTTP con metodo 
//      OPTIONS alla main.jsp e alla login.jsp (il motivo è ignoto)
//      In questi casi mancando il parametro Action, viene loggata una eccezione
//      'Parametro action mancante'. Su alcuni distretti (A8RR211 vicenza)
//      viene anche aperta una finestra di errore prima della stampa.
//============================================================================
if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
%>
<jsp:forward page="/html/blank.htm"/>
<%
}
if ((request.getCookies() == null) || (request.getCookies().length == 0)) {
   	session.invalidate();
   	request.setAttribute("LinkTo", IWebConstants.PG_LOGIN);
   	request.setAttribute("Messagge", "Attivare i Cookies sul browser per utilizzare l'applicazione.");
%>
<jsp:forward page="<%=IWebConstants.PG_SEND_TO%>"/>
<%
}
String lStrAction = null;
if (MultipartContent.isMultipartContent(request)) {
	try {
   		MultipartContent lMC = new MultipartContent( request ) ;
   		lMC.parseRequest();
   		if (lMC.getParameter(IWebConstants.ACTION_FIELD) != null)
     		lStrAction = (String)lMC.getParameter(IWebConstants.ACTION_FIELD);
   		request.setAttribute( "MultipartContent", lMC );
 	} catch( Exception ex) {
   		throw ex;
  	}
} else {
  	if (request.getParameter(IWebConstants.ACTION_FIELD) != null)
   		lStrAction = (String)request.getParameter(IWebConstants.ACTION_FIELD);
}
if (lStrAction == null) {
	// [SG] Ticket#20200818015 - errori JBWEB000236 su server.log e Debug.log
	// throw new Exception("Parametro Action mancante!");
	siesLogger.info("lStrAction == null --> java.lang.Exception: Parametro Action mancante!");
	return; // esco invece di rilanciare l'eccezione
}
/** 
 * Implementazione tabella di lock per le modifiche delle entità
 * 1) Entrando nella main, unlock per default tutti i lock dell'utente
 */

if (!lStrAction.equals("siap.sico.sessionstate.action.ActVediSessioneSIEP")) {
  	if (lockTable.containsKey(session.getId()))
		lockTable.remove(session.getId());
}

String lPage = null;
// Controllo sull'avvenuto accesso attraverso il login
if (session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO) == null
		&& (!lStrAction.equals("siap.sico.security.action.ActLogin"))
		// MEV INTEGRAZIONE SIES ADN: nuove pagine di login
		&& (!lStrAction.equals("siap.sico.utenzaAdn.action.ActUtenzaAdn"))
		&& (!lStrAction.equals("siap.sico.utenzaAdn.action.ActAssocUtenteSiesAdn"))) {
	session.invalidate();
    request.setAttribute("LinkTo", IWebConstants.PG_LOGIN);
    request.setAttribute("Messagge", "Sessione utente terminata, effettuare di nuovo il login...");
%>
<script language=Javascript>
top.document.location.href="<%=IWebConstants.PAGE_LOGOUT%>?Messagge=<%=response.encodeURL("Sessione utente terminata, effettuare di nuovo il login...")%>";
</script>
<%
   	response.flushBuffer();
} else {
    //============================================================================
    // Codice per TEST MCD e NDC
    //============================================================================
    UtenteModel lUteMod = (UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
    UfficioModel lUffMod = null;
    String CodUff = "";
    String CodUte = "";
    if (lUteMod != null) {
      	lUffMod= lUteMod.getUfficioUtente();
      	CodUff = lUffMod.getCodUfficio();
      	CodUte = lUteMod.getUserId();
    }
    MDC.put("utente", CodUte);
    MDC.put("ufficio", CodUff);
    NDC.remove();
   	NDC.push(session.getId());
    Action actionObj = action.get(lStrAction);
    actionObj.setReqSes(request, session);
    actionObj.init(); // 2010-12-26 per eventuali inizializzazioni.
    // Controllo che risolve i problemi di sessione nel caso un dettaglio di un evento venga richiamato da
    // una delle funzioni di ricerca senza passare dal Dettaglio del fascicolo
    if (actionObj instanceof siap.siep.web.ActSIESDettaglioProvvedimento) { 
      	siap.siep.web.ActSIESDettaglioProvvedimento aActionDettaglio = (siap.siep.web.ActSIESDettaglioProvvedimento) actionObj;
      	aActionDettaglio.caricaFascicoloInSessione(request);
    }
    // Per l'ActLogin e per l'ActLoadOrizontalMenu non vengono eseguiti controlli di abilitazione
    // sul profilo dell'utente perchè sono operazioni abilitate per qualsiasi utente
    if ((!lStrAction.equals("siap.sico.security.action.ActLogin")) // ogni utente puo effettuare il login
    		// MEV INTEGRAZIONE SIES ADN: nuove pagine di login
    		&& (!lStrAction.equals("siap.sico.utenzaAdn.action.ActUtenzaAdn"))
    		&& (!lStrAction.equals("siap.sico.utenzaAdn.action.ActAssocUtenteSiesAdn"))
			&& (!lStrAction.equals("siap.sico.security.action.ActLoadOrizontalMenu")) // supposto che il menu abbia almeno due livelli
			&& (!lStrAction.equals("siap.siep.richiesta.action.ActLoadContaAttiCompetenzaRicevuti"))) { // cruscotto atti ricevuti: call automatica temporizzata
		actionObj.setFunctionsAvailableToRequest(lStrAction);
	}
    //==========================================================================
    // Registrazione della request nella LOG_ATTIVITA
    //==========================================================================
    if (!lStrAction.equals("siap.sico.security.action.ActLoadOrizontalMenu")) {
    	// Iniziamo ad eliminare dalla System.out le action non significative
      	siesLogger.info(lStrAction);
    }
    if (!lStrAction.equals("siap.sico.security.action.ActLoadOrizontalMenu")
			&& !lStrAction.equals("siap.siep.calcolopena.action.ActCalcolatrice")        // calcolatrice
         	&& (!lStrAction.equals("siap.siep.calcolopena.action.ActLoadCalcolatrice"))  // LoadCalcolatrice
         	&& !lStrAction.equals("siap.siep.misuracautelare.action.ActInserisciMisuraCautelare")        // iscrizione misure Cautelari
         	&& !lStrAction.equals("siap.siep.misuracautelare.action.ActRicercaMisuraCautelare")        // iscrizione misure Cautelari
         	&& (!lStrAction.equals("siap.siep.calcolopena.action.ActInserisciAnnotazioniManualiMC"))  // iscrizione annotazioni Manuali
         	&& (!lStrAction.equals("siap.siep.calcolopena.action.ActInserisciAnnotazioniManualiCompAltroTitolo"))  // iscrizione annotazioni Manuali
         	&& (!lStrAction.equals("siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniAltroTitolo"))  // iscrizione annotazioni Manuali
         	&& (!lStrAction.equals("siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniStessoTitolo")) // iscrizione annotazioni Manuali
			&& (!lStrAction.equals("siap.siep.richiesta.action.ActLoadContaAttiCompetenzaRicevuti"))) { // cruscotto atti ricevuti: call automatica temporizzata
		actionObj.WriteActivityLog(lStrAction);
	}
    lPage = actionObj.processRequest(); // ritorna la pagina responsabile della VIEW
    response.setHeader("expires", "0");
	try {
%>
<jsp:forward page="<%=lPage%>"/>
<%
    } catch(Exception ex) {
    	// 2019 [SG]: gestione degli errori (non errori) più ricorrenti
      	if (ex.getMessage().contains("getOutputStream() has already been called for this response"))
        	siesLogger.info("org.apache.jasper.JasperException" + ex.getMessage());
      	else if (ex.getMessage().contains("Parametro Action mancante"))
        	siesLogger.info("java.lang.Exception" + ex.getMessage());
      	else
        	throw ex;
    }
}
%>
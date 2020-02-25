<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>


<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.jms.ICostantiJMS"%>


<% // Vector <MessaggioModel> %>
<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="Messaggi" scope="request" class="java.util.Vector"/>

<jsp:useBean id="aUfficioDestinatario" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="aEsito"               scope="request" class="java.lang.String"/>
<jsp:useBean id="aDataTrasmissioneDal" scope="request" class="java.lang.String"/>
<jsp:useBean id="aDataTrasmissioneAl"  scope="request" class="java.lang.String"/>
<jsp:useBean id="ChiaveAnno"           scope="request" class="java.lang.String"/>
<jsp:useBean id="ChiaveProgr"          scope="request" class="java.lang.String"/>




<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>

<%
/* 
JSP per la visualizzazione dei messaggi inviati e delle relative risposte 
ricevute (esito)


// solo per DEBUG
Iterator itxappo = Messaggi.iterator();
while ( itxappo.hasNext())
{
  MessaggioModel lMess = (MessaggioModel)itxappo.next();
  
  
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("JSP - getIdMessaggio = "+lMess.getIdMessaggio());
  
  Vector lMessCorrel = lMess.getMessaggiCorrelati();
  
  Iterator iterCorrelati = lMessCorrel.iterator();
  
  while ( iterCorrelati.hasNext())
  {
    MessaggioModel lMessX = (MessaggioModel)iterCorrelati.next();
    // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("  JSP - IdMessaggioCorrelato = "+lMessX.getIdMessaggio());
    // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("  JSP - "+lMessX);
  }

  
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  //siesLogger.debug("JSP - getMessaggiSollecito = "+lMess.getMessaggiSollecito() );
}

*/
%>



<html>
  <head>
    <title>[S.I.E.S.] - Atti Trasmessi</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione : </font><font class="campo">Gestione Misure di Sicurezza - Riscontro Trasmissioni</font>&nbsp;&nbsp;
      </td>
      <!-- BOTTONE DI RITORNO -->
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%-- jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/ --%>
      
      <td class="LBG">
        <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadRicercaAttiTrasmessi&<%=(String)request.getAttribute("linkRitorno")%>">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
      
    </tr>
  </table>
  <br><br>





  <div align="left">
  
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
    </tr>
    <% if (aUfficioDestinatario.getCodUfficio()!=null && aUfficioDestinatario.getCodUfficio().length()>0) { %>
    <tr>
      <td class="lVerdeNB">Ufficio Mittente: &nbsp;<%=aUfficioDestinatario.getDescrTipoUfficio()%>&nbsp;di&nbsp;<%=aUfficioDestinatario.getDescrComune()%>
    </tr>
    <% } %>
    
    <% if (aEsito.length()>0) {%>
    <tr>
      <td class="lVerdeNB">Esito: &nbsp;<%=StringUtils.toStringJSP(aEsito)%>
    </tr>
    <% } %>
    
    <% if( !aDataTrasmissioneDal.equals("") || !aDataTrasmissioneAl.equals("") ) { %>
    <tr>
      <td class="lVerdeNB">Procedimenti con Data di Trasmissione :&nbsp;
      <% if(!aDataTrasmissioneDal.equals("") ) { %>
            Dal <%=aDataTrasmissioneDal%>&nbsp;&nbsp;
      <% } %>
      <% if(!aDataTrasmissioneAl.equals("") ) { %>
            &nbsp;Al&nbsp;&nbsp;<%=aDataTrasmissioneAl%>
      <% } %>
      </td>
    </tr>
    <% } %>    
    
    <% if( ChiaveAnno.length()>0 || ChiaveProgr.length()>0 ) { %>
    <tr>
      <td class="lVerdeNB">
        Fascicolo: &nbsp;<%=(ChiaveAnno.length()>0)?StringUtils.toStringJSP(ChiaveAnno):"____"%>/<%=(ChiaveProgr.length()>0)?StringUtils.toStringJSP(ChiaveProgr):"__"%>
      </td>
    </tr>
    <% } %> 
  </table>
  
  
  <%
  //============================================================================
  //
  //============================================================================
  %>
  <br>  
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <br>
  
  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td colspan="5"><font class="label">
        Elenco degli atti trasmessi per competenza</font>
      </td>
    <tr>
    <tr>
      <td class="int">Anno/Numero <br>SIEP</td>
      <td class="int">Oggetto</td>
      <td class="int">Ufficio Destinatario Atti</td>
      <td class="int">Soggetto</td>
      <td class="int">Data Invio</td>
      <td class="int">Data Esito</td>
      <td class="int">Esito</td>
      <td class="int">Azioni</td>
    </tr>
    
    <% 
    if (Messaggi.size() == 0)  { %>
    <tr>
      <td class="label" colspan="6"> &nbsp;</td>
    </tr>
    <tr>
      <td class="label" colspan="6"> Nessun Elemento soddisfa i criteri di ricerca impostati! </td>
    </tr>
    <tr>
      <td class="label" colspan="6"> &nbsp;</td>
    </tr>
    <% 
    } 
    else 
    { 
      //========================================================================
      // Se presente solo la richiesta si visualizza la richiesta sul primo rigo
      // con stato in attesa di risposta
      // Se presente almeno una risposta (esito) non si visualizza la richiesta ma 
      // direttamente l'esito
      //========================================================================
      String coloreLinea = "c"; 
      Iterator itx = Messaggi.iterator();
      while ( itx.hasNext())
      {
        MessaggioModel lMess = (MessaggioModel)itx.next();
        coloreLinea = "c";
        
        // In assenza di risposte visualizzo il messaggio inviato
        //   Rigo 1) Messaggio di Richiesta (in attesa di risposta)
        // In presenza di risposte visualizzo solo le risposte  
              
        // Sul primo rigo visualizzo il messaggio inviato solo se non ho risposta
        // o se presente risposta di inoltro
        
        boolean isPrimoRigoInserito = false;
        if (   lMess.getMessaggiCorrelati()==null 
            || lMess.getMessaggiCorrelati().size()==0
           // || (ICostantiJMS.TRASFERITO).equals(   ((MessaggioModel)lMess.getMessaggiCorrelati().elementAt(0) ).getCodEsito())
           ) 
        { 
          isPrimoRigoInserito = true;
          String descEsito = null;
          if (lMess.getMessaggiCorrelati()!=null && lMess.getMessaggiCorrelati().size()>0){
            descEsito = "INOLTRATO PER COMPETENZA";
          } else if ("-".equals(lMess.getCodEsito())) {
            descEsito = "<font color='red'>In attesa di risposta...</font>";
          } else {
            descEsito = lMess.getDescrEsito();
          }
          // ATTENZIONE in realtà si entra solo se:In attesa di risposta...
        %>
        <!-- Primo rigo -->
        <tr>
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
          	<td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%>&nbsp;<%="<!--br>"+lMess.getIdMessaggio()+"-->"%></td>
          <% if (ICostantiJMS.TRASFERIMENTO_COMPETENZA_MS.equals(lMess.getCodTipoOperazione())) {%>
          <td class="<%=coloreLinea%>">Trasmissione Atti per competenza  ex artt. 658 e 679 comma 1 c.p.p.</td>
          <% } else if (ICostantiJMS.TRASFERIMENTO_ESECUZIONE_MS.equals(lMess.getCodTipoOperazione())){%>
          <td class="<%=coloreLinea%>">Trasmissione Atti ai fini dell'esecuzione della misura di sicurezza ex art. 658 e 679 comma 2 c.p.p.</td>
          <% } %>
          
          
          
          <td class="<%=coloreLinea%>" >
            <%= lMess.getDescrUfficioDestinatario() +" "+ lMess.getDescrSedeUfficioDestinatario()%>
            <% 
            if (lMess.getMessaggiSollecito()!=null && lMess.getMessaggiSollecito().size()>0) 
            {
              Vector lMessaggiSollecito = lMess.getMessaggiSollecito();
              Iterator itxSoll = lMessaggiSollecito.iterator();
              while ( itxSoll.hasNext())
              {
                MessaggioModel lMessSoll = (MessaggioModel) itxSoll.next();   
                %>
                <br>
                <font color="red">Inviato Sollecito il <%= StringUtils.toStringJSP(DateUtils.getDateToString(lMessSoll.getDataInvio(),"dd-MM-yyyy HH:mm"),"") %></font>
              <% } %>
            <% } %>
          </td>          
          
          <td class="<%=coloreLinea%>"><%= lMess.getNomeSoggetto()%>&nbsp;<%= lMess.getCognomeSoggetto()%></td>
          <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
          <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataEsito(),"dd-MM-yyyy HH:mm:ss"),"&nbsp;")%></td>
          <% //if ("-".equals(lMess.getCodEsito()) ) { %>
          <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
          <%-- td class="<%=coloreLinea%>"><font color="red">In attesa di risposta...</font --%>
          <% //} else { %>
          <td class="<%=coloreLinea%>"><%= descEsito%>
          <% //} %>
          </td>
          
          <td class="c">
            <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadDettaglioAttoTrasmesso&<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>=<%=lMess.getIdMessaggio()%>&TornaQui=20">
              <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio" border="0">
            </a>
          </td>
        </tr>
        <% 
        } 
        else
        {   
          //====================================================================
          // Messaggi Correlati: sono presenti risposte. Sul primo rigo non
          // visualizzo il messaggio di richiesta ma direttamente la prima risposta 
          // che 'chiude' l'esito della richiesta
          //====================================================================

          Iterator lIterMessCorrelati = lMess.getMessaggiCorrelati().iterator();
          int contaCorrelati = 0;
          boolean isInoltroPresente = false;         
          String lLastEsito="";

          while (lIterMessCorrelati.hasNext())
          {
            contaCorrelati++;
            MessaggioModel lMessCorr = (MessaggioModel)lIterMessCorrelati.next();

            if (lMessCorr.getCodEsito().equals(ICostantiJMS.TRASFERITO))
              isInoltroPresente = true;
        %>
        <tr>
          <% 
          if (!isPrimoRigoInserito) 
          { 
            isPrimoRigoInserito = true;
          %>
          <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
            <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(lMessCorr.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMessCorr.getChiaveProgrSiep())%>&nbsp;<%="<!--br>"+lMess.getIdMessaggio()+"-->"%></td>
            <% if (ICostantiJMS.TRASFERIMENTO_COMPETENZA_MS.equals(lMess.getCodTipoOperazione())) {%>
            <td class="<%=coloreLinea%>">Trasmissione Atti per competenza  ex artt. 658 e 679 comma 1 c.p.p.</td>
            <% } else if (ICostantiJMS.TRASFERIMENTO_ESECUZIONE_MS.equals(lMess.getCodTipoOperazione())){%>
            <td class="<%=coloreLinea%>">Trasmissione Atti ai fini dell'esecuzione della misura di sicurezza ex art. 658 e 679 comma 2 c.p.p.</td>
            <% } %>   
          <% } else { %>
          <td class="<%=coloreLinea%>"></td>
          <td class="<%=coloreLinea%>"></td>
          <% } %>
          

          <td class="<%=coloreLinea%>" >
            <% if (lLastEsito.equals(ICostantiJMS.TRASFERITO)) {%>
            <% //if ( isInoltroPresente && contaCorrelati>1 && !lMessCorr.getCodEsito().equals(ICostantiJMS.ISCRITTO_CLASSE_IV)) { %>
               <% // se chi ha risposto ha ricevuto il messaggio come inoltro visualizzo la scritta "Inoltrato a" %>
              <font color="red">Inoltrato a:&nbsp;</font><%= lMessCorr.getDescrUfficioMittente() +" "+ lMessCorr.getDescrSedeUfficioMittente()%>
            <% } else { %>
               <%// n.b. è il messaggio di risposta per cui il destinatario da visualizzare nella lista è proprio l'ufficio che ha risposto%>
              <%=lMessCorr.getDescrUfficioMittente() +" "+ lMessCorr.getDescrSedeUfficioMittente()%>
            <% } %>
            
            <% 
             lLastEsito = lMessCorr.getCodEsito();

            //  eventuali solleciti
            if (   lMessCorr.getMessaggiSollecito()!=null 
                && lMessCorr.getMessaggiSollecito().size()>0
                && "-".equals(lMessCorr.getCodEsito()) // visualizzo i solleciti solo se il messaggio è pending
               ) 
            {
              Vector lMessaggiSollecito = lMessCorr.getMessaggiSollecito();
              Iterator itxSoll = lMessaggiSollecito.iterator();
              while ( itxSoll.hasNext())
              {
                MessaggioModel lMessSoll = (MessaggioModel) itxSoll.next();   
                %>
                <br>
                <font color="red">Inviato Sollecito il <%= StringUtils.toStringJSP(DateUtils.getDateToString(lMessSoll.getDataInvio(),"dd-MM-yyyy HH:mm"),"") %></font>
              <% } %>
            <% } %>
          </td>
          
          <td class="<%=coloreLinea%>"><%= lMessCorr.getNomeSoggetto()%>&nbsp;<%= lMessCorr.getCognomeSoggetto()%></td>

          <% // Data Invio %>
          <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
          <% // Data Esito %>
          <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMessCorr.getDataInvio(),"dd-MM-yyyy HH:mm:ss"),"&nbsp;")%></td>
          
          <% if ("-".equals(lMessCorr.getCodEsito()) ) { %>
          <td class="<%=coloreLinea%>"><font color="red">In attesa di risposta...</font>
          <% } else { %>
          <td class="<%=coloreLinea%>"><%= lMessCorr.getDescrEsito()%>
          <% } %>
          
          <% if(ICostantiJMS.ISCRITTO_CLASSE_IV.equals(lMessCorr.getCodEsito())) { %>
            <br>
            <%=lMessCorr.getChiaveAnnoSiep()%>
            /
            <%=lMessCorr.getChiaveProgrSiep()%>
          <% } %>
          </td>
          <% 
          String isRecordInoltroSimulato = "";
          if ("-".equals(lMessCorr.getCodEsito())) { 
            isRecordInoltroSimulato = "&RecordInoltroSimulato=true";
          } %>
          <td class="c">
            <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadDettaglioAttoTrasmesso&<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>=<%=lMessCorr.getIdMessaggio()%><%=isRecordInoltroSimulato%>&TornaQui=20">
              <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio" border="0">
            </a>
          </td>
        </tr>
        <%
          } // end while correlati
        } // end if (lMess.getMessaggiCorrelati()==null ) 
      }// end while ( itx.hasNext())
    } // end if (Messaggi.size() == 0)
    %>
    </table>
  </div>

  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >
  </body>
</html>
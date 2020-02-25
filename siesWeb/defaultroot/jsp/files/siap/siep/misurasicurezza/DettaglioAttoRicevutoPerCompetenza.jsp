<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>

<%@ page import="siap.sico.util.CalendarUtil" %>
<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>

<%@ page import="siap.sico.evento.model.EventoModel" %>
<%@ page import="siap.sico.evento.model.EventoNotificaModel" %>
<%@ page import="siap.sico.residenza.model.ResidenzaModel" %>

<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel" %>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel" %>
<%@ page import="siap.siep.penacumulo.model.PenaCumuloModel" %>
<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel" %>
<%@ page import="siap.siep.reato.model.ReatoModel" %>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel" %>
<%@ page import="siap.siep.beneficio.model.BeneficioModel" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel" %>
<%@ page import="siap.jms.ICostantiJMS"%>


<jsp:useBean id="Messaggio"        scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="dettaglioFasSIEP" scope="request" class="siap.siep.fascicolo.model.DettaglioFascicoloModel" />

<jsp:useBean id="isErrParser"      scope="request" class="java.lang.String" />
<jsp:useBean id="uffFascicolo"     scope="request" class="siap.sico.ufficio.model.UfficioModel" />
<jsp:useBean id="comuneNascita"    scope="request" class="siap.sico.decodifiche.model.ComuneModel" />
      
      
<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Atto Ricevuto</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
    
    <script language="javascript">

      function invia(aAction){
        disComm();  
        document.azioniPresaIncarico.<%=IWebConstants.ACTION_FIELD%>.value=aAction;   
        document.azioniPresaIncarico.submit();    
      }
      function restAtti(){ 
        //disComm();
        document.azioniPresaIncarico.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.misurasicurezza.action.ActRestituzioneAttoRicevuto';  
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadRestituzioneAttoRicevuto", "Restituzione_Atti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=300");
      }
      
      function disComm(){
        document.azioniPresaIncarico.PresaInCaricoButton.disabled=true;
        document.azioniPresaIncarico.RestituzioneButton.disabled=true;
        document.azioniPresaIncarico.InoltroButton.disabled=true;
        document.azioniPresaIncarico.IscrizioneButton.disabled=true;
        document.azioniPresaIncarico.IscrizioneManualeButton.disabled=true;
      } 
      
      function iscrizione(){
        <% if ("S".equalsIgnoreCase(Messaggio.getFlagVisto())) { %>
        if (window.confirm("Confermi l'iscrizione del procedimento di classe IV?")){
          document.azioniPresaIncarico.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.misurasicurezza.action.ActLoadInserisciFascicoloClasseIVdaClasseI';   
          document.azioniPresaIncarico.submit(); 
        }
        <% } else { %>
          alert("Per procedere all'iscrizione del procedimento di Classe IV è prima necessario effettuare la Presa in Carico.");
        <% } %>
      }
      
      function iscrizionemanual(){
          <% if ("S".equalsIgnoreCase(Messaggio.getFlagVisto())) { %>
          if (window.confirm("Confermi l'iscrizione del procedimento di classe IV? (Anno e Numero Manuale)")){
        	document.azioniPresaIncarico.NumerazioneManuale.value='S';  
            document.azioniPresaIncarico.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.misurasicurezza.action.ActLoadInserisciFascicoloClasseIVdaClasseI';   
            document.azioniPresaIncarico.submit(); 
          }
          <% } else { %>
            alert("Per procedere all'iscrizione del procedimento di Classe IV è prima necessario effettuare la Presa in Carico.");
          <% } %>
        }
      
    </script>
  </head>
  
  
<body class="corpo">
  <FORM name="comandi" >
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Misure di Sicurezza - Dettaglio Atti Ricevuti per Competenza </font>
        </td>
        <!-- BOTTONE DI RITORNO -->
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
    </table>
  </FORM>
        


<% if ("SI".equals(isErrParser)) { %>
  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td class="L" colspan=4>
      <% if (   !ICostantiJMS.ISCRITTO_CLASSE_IV.equals(Messaggio.getCodEsito()) 
             && !ICostantiJMS.RESTITUITO.equals(Messaggio.getCodEsito()) 
             && !ICostantiJMS.TRASFERITO.equals(Messaggio.getCodEsito()) 
             && !ICostantiJMS.PRESAINCARICO.equals(Messaggio.getCodEsito())
            )
          {
       %>
        <font color="red">Attenzione! Non è possibile procedere all'elaborazione degli atti ricevuti in quanto inviati con una versione SIES differente da quella attualmente in uso in questo Distretto. E' necessario restituire gli atti e richiedere un nuovo invio.</font>
        <% } else { %>
        <font color="red">Attenzione! Non è possibile visualizzare il Dettaglio degli atti ricevuti in quanto inviati con una versione SIES differente da quella attualmente in uso in questo Distretto.</font>
        <% } %>
      </td>
    </tr>
    
    <tr>
      <td class="Titolo" colspan=4>Oggetto della Trasmissione</td>
    </tr>
    
    <tr>
      <td class="l"><font class="label">Oggetto</font></td>
      <% if (Messaggio.getCodTipoOperazione().equals(ICostantiJMS.TRASFERIMENTO_COMPETENZA_MS)) { %>
      <td class="l"><font class="campo">Trasmissione Atti per competenza ex artt. 658 e 679 comma 1 c.p.p.</font></td>
      <% } else if (Messaggio.getCodTipoOperazione().equals(ICostantiJMS.TRASFERIMENTO_ESECUZIONE_MS)) { %>
      <td class="l"><font class="campo">Trasmissione Atti ai fini dell'esecuzione della misura di sicurezza ex art. 658 e 679 comma 2 c.p.p.</font></td>
      <% } else { %>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrTipoOperazione())%>&nbsp;</font></td>
      <% } %>
    </tr>
    <tr><td>&nbsp;</td></tr>
    
    <%
    // Se non si è riusciti a recuperare i dati dal DB visualizzo un dettaglio
    // con i solo estremi dell'atto presi direttamente dalla tabella MESSAGGIO
    %>
    <% if (dettaglioFasSIEP.getFascicoloSiep()==null) {%>
    <tr>
      <td class="Titolo" colspan=4>Procedimento Ricevuto</td>
    </tr>
    <tr>
      <td class="L" colspan=4>
        <font class="label">Procedimento N.</font>
        <font class="campo">
          <%=Messaggio.getChiaveAnnoSiep()%>/<%=Messaggio.getChiaveProgrSiep()%>
        </font>
<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <font class="campo">
          - <%=uffFascicolo.getDescrTipoUfficio()%>&nbsp;<%=uffFascicolo.getDescrComune()%><BR>
        </font>

      </td>
    </tr>
    
    <tr>
      <td class="L" width=100% colspan=4>
        <font class="label">Soggetto :</font>&nbsp;
        <font class="campo"><%=Messaggio.getCognomeSoggetto()%>&nbsp;<%=Messaggio.getNomeSoggetto()%></font>
      
        <% if(Messaggio.getDataNascita() != null){ %>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Messaggio.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
        <%}%>
        
        <font class="label">in :</font>&nbsp;
        <font class="campo"> 
        <%if (comuneNascita.getCodComune()!=null && comuneNascita.getCodComune().length()>0){%>
            <%=comuneNascita.getDescrizione()%> 
        <%} else {%>
            <%=Messaggio.getCodStatoNascita()%> 
        <% } %>      
        </font>
      </td>
    </tr>
    <% } %>
    
  </table>

<% } %>
<% //} else { %>


<% if (dettaglioFasSIEP.getFascicoloSiep()!=null) { %>

  
<%
  FascicoloSiepModel fascicoloSIEP  = dettaglioFasSIEP.getFascicoloSiep();
  SoggettoModel soggettoRicevuto    = fascicoloSIEP.getSoggetto();
  SentenzaModel sentenzaRicevuta    = fascicoloSIEP.getSentenza();
  
  PosizioneGiuridicaModel posRicevuta = dettaglioFasSIEP.getPosizioneGiuridica();
  PenaResiduaModel penaRicevuta       = dettaglioFasSIEP.getPenaResidua();

%>             

  <br>
  
  <table cellspacing=2 cellpadding=2 width="95%">
  
    <tr>
      <td class="Titolo" colspan=4>Oggetto della Trasmissione</td>
    </tr>
    
    <tr>
      <td class="l"><font class="label">Oggetto</font></td>
      <% if (Messaggio.getCodTipoOperazione().equals(ICostantiJMS.TRASFERIMENTO_COMPETENZA_MS)) { %>
      <td class="l"><font class="campo">Trasmissione Atti per competenza ex artt. 658 e 679 comma 1 c.p.p.</font></td>
      <% } else if (Messaggio.getCodTipoOperazione().equals(ICostantiJMS.TRASFERIMENTO_ESECUZIONE_MS)) { %>
      <td class="l"><font class="campo">Trasmissione Atti ai fini dell'esecuzione della misura di sicurezza ex art. 658 e 679 comma 2 c.p.p.</font></td>
      <% } else { %>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrTipoOperazione())%>&nbsp;</font></td>
      <% } %>
    </tr>
    <tr><td>&nbsp;</td></tr>
  
  
    <tr>
      <td class="Titolo" colspan=4>Procedimento Ricevuto</td>
    </tr>
    <tr>
      <td class="L" colspan=4>
        <font class="label">Procedimento N.</font>
        <font class="campo">
          <%=fascicoloSIEP.getChiaveAnno()%>/<%=fascicoloSIEP.getChiaveProgr()%>
        </font>
        <% if(   fascicoloSIEP.getFlagCumulante() != null && fascicoloSIEP.getFlagCumulante().equals("S") ) { %>
        <font class="cRossoCumulo"> &nbsp;C&nbsp; </font>&nbsp;
        <% } %>
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <font class="campo">
          - <%=fascicoloSIEP.getDescrTipoUfficio()%>&nbsp;<%=fascicoloSIEP.getDescrComuneUfficio()%><BR>
        </font>

      </td>
    </tr>
    
    <tr>
      <td class="L" colspan=4>
        <font class="label">Data Iscrizione :</font>&nbsp;
        <font class="campo">
          <%=DateUtils.getDateToString(fascicoloSIEP.getDataIscrizione(), "dd-MM-yyyy") %>
        </font>&nbsp;
        <font class="label">Data Irrevocabilità :</font>&nbsp;
        <font class="campo"><%=DateUtils.getDateToString(fascicoloSIEP.getDataIrrevocabilita(), "dd-MM-yyyy") %></font>
      </td> 
    </tr>
    
    <tr>
      <td class="L" width=100% colspan=4>
        <font class="label">Soggetto :</font>&nbsp;
        <font class="campo"><%=soggettoRicevuto.getCognome()%>&nbsp;<%=soggettoRicevuto.getNome()%></font>
        <%if (soggettoRicevuto.getSesso().compareTo("F")==0){%>&nbsp;
        <font class="label">nata il :</font>&nbsp;
        <%}else{%>
        <font class="label">nato il :</font>&nbsp;
        <% } %>
      
        <%
        if(soggettoRicevuto.getDataNascita() == null){
          if(soggettoRicevuto.getDataNascitaPresunta().equals("S")) {%>
            <font class="campo"><%=StringUtils.toStringJSP(soggettoRicevuto.getAnnoNascita())%></font>&nbsp;
          <%} else{%>
            <font class="campo">***</font>&nbsp;
          <%}
        }else{%>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggettoRicevuto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
        <%}%>
        
        <font class="label">in :</font>&nbsp;
        <font class="campo"> 
        <%if (soggettoRicevuto.getDescrComuneNascita().compareTo("-")==0){%>
            <%=soggettoRicevuto.getDescComuneNascitaEstero()%>  (<%=soggettoRicevuto.getDescrStatoNascita().toUpperCase()%>)
        <%} else {%>
            <%=soggettoRicevuto.getDescrComuneNascita()%> (<%=soggettoRicevuto.getCodProvinciaNascita()%>)
        <% } %>      
        </font>
      </td>
    </tr>
  </table>
  
  <%
  //============================================================================  
  // Se CUMULANTE visualizzo i dati dell'ULTIMO provvedimento di cumulo
  //  - DatiSiepPerTrasferimentoModel datiSIEP = dettaglioFasSIEP.getDatiSiepPerTrasferimento();
  //  - List <CumuloModel> listaCumuli = datiSIEP.getListCumulo();
  //  - List <EventoNotificaModel> listaEventiNotifiche = dettaglioFasSIEP.getEventi();
  //    -- Eventi di cumulo: (01 - 04 - 0222,0223,0224,0277)
  //  n.b. la lista degli eventi è caricata con il seguente order by 
  //  - ORDER BY DATA_INSERIMENTO ASC ,ID_EVENTO ASC
  //============================================================================  
  %>
  <% 
  if(fascicoloSIEP.getFlagCumulante() != null && fascicoloSIEP.getFlagCumulante().equals("S") ) 
  { 
    Date dataEmissioneCumulo = null;
    String lProvvedimentoCumulo = "";
    List /*EventoNotificaModel*/ listaEventi = dettaglioFasSIEP.getEventi();
    if (listaEventi!=null) {
      Iterator lIterEventi = listaEventi.iterator();
      while (lIterEventi.hasNext())
      {
        EventoNotificaModel lEventoNotificaModel = (EventoNotificaModel) lIterEventi.next();
        EventoModel lEventoModel = lEventoNotificaModel.getEvento();  
        if (   lEventoModel.getCodTipoEvento().equals("01")
            && lEventoModel.getCodTipoProvvedimento().equals("04")
            && (   lEventoModel.getCodMotivo().equals("0222")
                || lEventoModel.getCodMotivo().equals("0223")
                || lEventoModel.getCodMotivo().equals("0224")
                || lEventoModel.getCodMotivo().equals("0277")
               )
           )
        { // Provedimento di cumulo
          lProvvedimentoCumulo = lEventoModel.getDescrTipoProvvedimento()+" "+lEventoModel.getDescrMotivo();
          dataEmissioneCumulo = lEventoModel.getDataEmissione();
          // non esco dal ciclo. Se presenta altro porvvedimento di cumulo prendo
          // l'ultimo la lista è ordinata per data inserimento asc
        }
      }    
    }
  %>  
  <br>
  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td class="Titolo" colspan=4>Provvedimento di Cumulo</td>
    </tr>
    <tr>
      <td class="L" colspan=4>
        <!--font class="label">Provvedimento di determinazione pene concorrenti emesso in data: </font-->
        <font class="campo"><%=StringUtils.toStringJSP(lProvvedimentoCumulo)%></font>
        <font class="label">&nbsp;emesso in data:</font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissioneCumulo,"dd-MM-yyyy"))%></font>
      </td>
    </tr>
  </table>

  <% } %>
        
        
  <br>
  <%
  //=================================
  //           SENTENZA
  //=================================
  %>
  <table class="L" width="95%">
    <tr>
      <td class="Titolo" colspan=4>Sentenza</td>
    </tr>
    <tr>    
      <td class="L" colspan=4>
        <font class="label"><%=sentenzaRicevuta.getDescrTipoProvvedimento().substring(0,1).toUpperCase()
          +sentenzaRicevuta.getDescrTipoProvvedimento().substring(1).toLowerCase()%>
        </font>
        <font class="label"> N.</font>
        <font class="campo">
          <%=sentenzaRicevuta.getAnnoSentenza()%>/<%=sentenzaRicevuta.getNumeroSentenza()%>
        </font>
        <font class="label">del</font>&nbsp;
        <font class="campo">
          <%=DateUtils.getDateToString(sentenzaRicevuta.getDataProvvedimento(), "dd-MM-yyyy")%>
        </font>
        &nbsp;<font class="label"> Emessa da :</font>&nbsp;
        <font class="campo"><%=sentenzaRicevuta.getDescrTipoAutoritaEmittente()%></font>&nbsp;
        <% if (sentenzaRicevuta.getNumSezioneAutoritaEmittente() != null){%>
          <font class="label">(Sez.</font> <font class="campo"><%=sentenzaRicevuta.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
        <% } %>
        <font class="label"> di </font>&nbsp;
        <font class="campo"><%=sentenzaRicevuta.getDescrLuogoEmittente()%></font>
      </td>
    </tr>
  </table>
  
  <table>
  <%
  //============================================================================
  // PENA IRROGATA IN SENTENZA
  // n.b. se Cumulante viene visualizzata la pena in cumulo
  //============================================================================
  //============================================================================
  // Aggiunta nuova versione calcolo pena: viene visualizzata la pena in Cumulo
  //============================================================================
  if (!"S".equals(dettaglioFasSIEP.getFascicoloSiep().getFlagCumulante()))
  {
    if(dettaglioFasSIEP.getPenaComplessivaSanzioneSostitutiva()!=null)
    {
      PenaComplessivaSanzioneSostitutivaModel lPenaSostMod=dettaglioFasSIEP.getPenaComplessivaSanzioneSostitutiva();

      if(lPenaSostMod!=null)
      {
        PenaComplessivaModel lPenCompMod=lPenaSostMod.getPenaComplessiva();
        
        if(lPenCompMod!=null)
        {
        %>
        <tr>
          <td class="L" colspan=4>
            <font class="label">Pena irrogata in sentenza : </font>&nbsp;
            <% // RECLUSIONE
            if (   (lPenCompMod.getNumAnniReclusione()!=null && lPenCompMod.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0)
                || (lPenCompMod.getNumMesiReclusione()!=null && lPenCompMod.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0)
                || (lPenCompMod.getNumGiorniReclusione()!=null && lPenCompMod.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0)
               )
            {%>
            <font class="campo">Reclusione</font>&nbsp;
            <font class="label">Anni</font>
            <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniReclusione(),"0")%></font>&nbsp;
            <font class="label">Mesi</font>
            <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiReclusione(),"0")%></font>&nbsp;
            <font class="label">Giorni</font>
            <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;
            <%}%>

            <%if(lPenCompMod.getImportoMulta()!=null && lPenCompMod.getImportoMulta().compareTo(new BigDecimal(0))!=0){%>
            <font class="label">Multa </font>&nbsp;
            <font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoMulta())%></font>&nbsp;&euro;&nbsp;
            <%}%>
          
            <%  // ARRESTO
            if (   (lPenCompMod.getNumAnniArresto()!=null && lPenCompMod.getNumAnniArresto().compareTo(new BigDecimal(0))!=0)
                || (lPenCompMod.getNumMesiArresto()!=null && lPenCompMod.getNumMesiArresto().compareTo(new BigDecimal(0))!=0)
                || (lPenCompMod.getNumGiorniArresto()!=null && lPenCompMod.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0)
               )
            {%>
            <font class="campo">Arresto</font>&nbsp;
            <font class="label">Anni</font>
            <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniArresto(),"0")%></font>&nbsp;
            <font class="label">Mesi</font>
            <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiArresto(),"0")%></font>&nbsp;
            <font class="label">Giorni</font>
            <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;
            <%}%>
          
            <%if(lPenCompMod.getImportoAmmenda()!=null && lPenCompMod.getImportoAmmenda().compareTo(new BigDecimal(0))!=0) {%>
            <font class="label">Ammenda </font>&nbsp;
            <font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;
            <%}%>
          
            <% // ERGASTOLO
            if (lPenCompMod.getCodTipoPenaDetentiva().equals("03") || lPenCompMod.getCodTipoPenaDetentiva().equals("04")) {%>
              <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getDescrTipoPenaDetentiva())%></font>
            
              <% if(lPenCompMod.getCodTipoPenaDetentiva().equals("04")) {%>
                <% if(lPenCompMod.getNumAnniIsolamentoDiurno()!=null) {%>
                <font class="label">Anni</font>
                <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniIsolamentoDiurno(),"0")%></font>
                <%}%>
            
                <% if(lPenCompMod.getNumMesiIsolamentoDiurno()!=null) {%>
                <font class="label">Mesi</font>
                <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiIsolamentoDiurno(),"0")%></font>
                <%}%>
  
                <% if(lPenCompMod.getNumGiorniIsolamentoDiurno()!=null){%>
                <font class="label">Giorni</font>
                <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniIsolamentoDiurno(),"0")%></font>
                <%}%>
              <%}%>
            <%}  // end ERGASTOLO
         %>
           </td>
        </tr>
        <%
        } // end if (lPenCompMod!=null)
      } // end if(lPenaSostMod!=null)
    } // end if(dettaglioFasSIEP.getPenaComplessivaSanzioneSostitutiva()!=null)
  }  // end if (!"S".equals(dettaglioFasSIEP.getFascicoloSiep().getFlagCumulante()))
  else 
  {
    //============================================================================
    // Aggiunta nuova versione calcolo pena: viene visualizzata la pena in Cumulo
    //============================================================================
    if(dettaglioFasSIEP.getPenaCumulo()!=null)
    {
      PenaCumuloModel lPenaCumulo = dettaglioFasSIEP.getPenaCumulo();
    %>
      <tr>
        <td class="L" colspan=4>
          <font class="label">Pena Irrogata in Cumulo : </font>
          <%
          if (   (lPenaCumulo.getNumAnniReclusione()!=null && lPenaCumulo.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0)
              || (lPenaCumulo.getNumMesiReclusione()!=null && lPenaCumulo.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0)
              || (lPenaCumulo.getNumGiorniReclusione()!=null && lPenaCumulo.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0)
             )
          {%>
          <font class="campo">Reclusione</font>&nbsp;
          <font class="label">Anni</font>
          <font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getNumAnniReclusione(),"0")%></font>&nbsp;
          <font class="label">Mesi</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumMesiReclusione(),"0")%></font>&nbsp;
          <font class="label">Giorni</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;
          <%}%>

          <%if(lPenaCumulo.getImportoMulta()!=null && lPenaCumulo.getImportoMulta().compareTo(new BigDecimal(0))!=0){%>
          <font class="label">Multa </font>&nbsp;
          <font class="campo"><%=StringUtils.toEuroFormat(lPenaCumulo.getImportoMulta())%></font>&nbsp;&euro;&nbsp;
          <%}%>

          <%
          if (   (lPenaCumulo.getNumAnniArresto()!=null && lPenaCumulo.getNumAnniArresto().compareTo(new BigDecimal(0))!=0)
              || (lPenaCumulo.getNumMesiArresto()!=null && lPenaCumulo.getNumMesiArresto().compareTo(new BigDecimal(0))!=0)
              || (lPenaCumulo.getNumGiorniArresto()!=null && lPenaCumulo.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0)
             )
          {%>
          <font class="campo">Arresto</font>&nbsp;
          <font class="label">Anni</font>
          <font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getNumAnniArresto(),"0")%></font>&nbsp;
          <font class="label">Mesi</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumMesiArresto(),"0")%></font>&nbsp;
          <font class="label">Giorni</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;
          <%}%>

          <%if(lPenaCumulo.getImportoAmmenda()!=null && lPenaCumulo.getImportoAmmenda().compareTo(new BigDecimal(0))!=0) {%>
          <font class="label">Ammenda </font>&nbsp;
          <font class="campo"><%=StringUtils.toEuroFormat(lPenaCumulo.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;
          <%}%>

          <% if (lPenaCumulo.getCodTipoPenaDetentiva().equals("E") || lPenaCumulo.getCodTipoPenaDetentiva().equals("I")) 
          {
          %>
          <font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getDescrTipoPenaDetentiva())%></font>
            <%if(lPenaCumulo.getCodTipoPenaDetentiva().equals("I")){%>
              <%if(lPenaCumulo.getNumAnniIsolamentoDiurno()!=null){%>
              <font class="label">Anni</font>
              <font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getNumAnniIsolamentoDiurno(),"0")%></font>&nbsp;
              <%}%>

              <%if(lPenaCumulo.getNumMesiIsolamentoDiurno()!=null){%>
              <font class="label">Mesi</font>
              <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumMesiIsolamentoDiurno(),"0")%></font>&nbsp;
              <%}%>

              <%if(lPenaCumulo.getNumGiorniIsolamentoDiurno()!=null){%>
              <font class="label">Giorni</font>
              <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumGiorniIsolamentoDiurno(),"0")%></font>&nbsp;
              <%}%>
            <%}%>
         <%}%>
      </td>
    </tr>
<%
  }
}%>     
  </table>
  
  
  <br>
  <%
  //============================================================================
  //                POSIZIONE GIURIDICA e PENA RESIDUA
  // Se 'Libero': residenza + domicilio
  // Se 'In Espiazione': tipologia di espiazione + Luogo Esecuzione (se in misura) o Istituto
  //============================================================================
  %>
  <table cellspacing=2 cellpadding=2 >
  <% if(dettaglioFasSIEP.getPosizioneGiuridica() != null) { %>
  <tr>
    <td class="L" colspan=4>
      <font class="label">Posizione Giuridica : </font>&nbsp;
      <%
      // Se detenuto altra causa, visualizzo Istituito di detenzione o Indirizzo
      if (   fascicoloSIEP.getFlagAltraCausa() != null && fascicoloSIEP.getFlagAltraCausa().equals("S")
          && (   dettaglioFasSIEP.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("10") // libero
              || dettaglioFasSIEP.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("07") // libero
             )
         )
      {
      %>
      <font class="campo">DETENUTO PER ALTRA CAUSA</font>
    </td>
  </tr>
        <% 
        if(dettaglioFasSIEP.getAltraCausa() != null) 
        {
          if(dettaglioFasSIEP.getAltraCausa().getIstDetIdIstitutoDetenzione() != null)
          {%>
            <tr>
              <td class="L" colspan=4>
                <font class="label">Tipo Istituto : </font>
                <font class="campo"><%=StringUtils.toStringJSP(dettaglioFasSIEP.getAltraCausa().getIstitutoDetenzione().getDescrTipoIstituto())%></font>
                <font class="label">Luogo Detenzione</font>
                <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
                <%--font class="campo"><%=StringUtils.toStringJSP(dettaglioFasSIEP.getAltraCausa().getIstitutoDetenzione().getDescrComune())%></font--%>
                <font class="campo"><%=StringUtils.toStringJSP(dettaglioFasSIEP.getAltraCausa().getIstitutoDetenzione().getDescrizione())%></font>
                <font class="label">Indirizzo </font>
                <font class="campo"><%=StringUtils.toStringJSP(dettaglioFasSIEP.getAltraCausa().getIstitutoDetenzione().getIndirizzo())%></font>
              </td>
            </tr>
          <%
          }
          else if(dettaglioFasSIEP.getAltraCausa().getAltroLuogo() != null && !dettaglioFasSIEP.getAltraCausa().getAltroLuogo().equals(""))
          {
          %>
              <tr>
                <td class="L" colspan=4>
                  <font class="label">Indirizzo :</font>
                  <font class="campo"><%=StringUtils.toStringJSP(dettaglioFasSIEP.getAltraCausa().getAltroLuogo())%></font>
                </td>
              </tr>
          <%
          }
        }
      }
      else
      { 
        //===================================
        // Non detenuto altra causa
        //===================================
        %>
        <font class="campo"><%=StringUtils.toStringJSP(dettaglioFasSIEP.getPosizioneGiuridica().getDescrPosizioneGiuridica())%></font>
        </td>
      </tr>
        <%
       
if (dettaglioFasSIEP.getPosizioneGiuridica().isLibero())
{ // Visualizzo residenza e domicilio
  ResidenzaModel lResidenza = dettaglioFasSIEP.getResidenza();
  ResidenzaModel lDomicilio = dettaglioFasSIEP.getDomicilio();
  
  if (lResidenza!=null) 
  {
  %>
        <tr>
          <td class="l"><font class="label">Residenza: </font></td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(lResidenza.getIndirizzo())%>&nbsp;
              <%
              String lStato = lResidenza.getDescrStato();
              if(lStato != null && lStato.equalsIgnoreCase("ITALIA")) {%>
                 <%=StringUtils.toStringJSP(lResidenza.getDescrComune())%>
                (<%=StringUtils.toStringJSP(lResidenza.getCodProvincia())%>)
              <% } else {%>              
                <%=StringUtils.toStringJSP(lResidenza.getDescComuneEstero())%>
                  -
                <%=StringUtils.toStringJSP(lStato)%>
              <% } %>
            </font>
          </td>
        </tr>
  <%
  }
   
  if (lDomicilio!=null) {
  %>
        <tr>
          <td class="l"><font class="label">Domicilio: </font></td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(lDomicilio.getIndirizzo())%>&nbsp;
              <%
              String lStato = lDomicilio.getDescrStato();
              if(lStato != null && lStato.equalsIgnoreCase("ITALIA")) { %>
                <%=StringUtils.toStringJSP(lDomicilio.getDescrComune())%>
                (<%=StringUtils.toStringJSP(lDomicilio.getCodProvincia())%>)
              <% } else { %>              
                <%=StringUtils.toStringJSP(lDomicilio.getDescComuneEstero())%> -
                <%=StringUtils.toStringJSP(lStato)%>
              <% } %>
            </font>
          </td>
        </tr>  
  <%
  }
}
else if (dettaglioFasSIEP.getPosizioneGiuridica().isMisuraAlternativa())
{
  
  MisuraAlternativaModel lMisAltModel = dettaglioFasSIEP.getMisuraAlternativa();
  if (lMisAltModel!=null){
  %>
   <tr>
    <td class="L" colspan=4>
      <font class="label">Luogo Misura : </font>
      <font class="campo"><%=StringUtils.toStringJSP(lMisAltModel.getDescrLuogoProva())%></font>
    </td>
  </tr>
  <%
  }
}
//else if (dettaglioFasSIEP.getPosizioneGiuridica().isDetenuto())
else
{
  if(dettaglioFasSIEP.getLuogoDetenzione() != null )
  {
    if(dettaglioFasSIEP.getLuogoDetenzione().getIstDetIdIstitutoDetenzione() != null && !dettaglioFasSIEP.getLuogoDetenzione().getIstDetIdIstitutoDetenzione().equals(""))
    {
    %>
    <tr>
      <td class="L" colspan=4>
        <font class="label">Tipo Istituto : </font>
        <font class="campo"><%=StringUtils.toStringJSP(dettaglioFasSIEP.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%></font>
        <font class="label">Luogo Detenzione</font>
        <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--font class="campo"><%=StringUtils.toStringJSP(dettaglioFasSIEP.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%></font--%>
        <font class="campo"><%=StringUtils.toStringJSP(dettaglioFasSIEP.getLuogoDetenzione().getIstitutoDetenzione().getDescrizione())%></font>
        <font class="label">Indirizzo :</font>
        <font class="campo"><%=StringUtils.toStringJSP(dettaglioFasSIEP.getLuogoDetenzione().getIstitutoDetenzione().getIndirizzo())%></font>
      </td>
    </tr>
    <%
    }
    else if(dettaglioFasSIEP.getLuogoDetenzione().getAltroLuogo() != null && !dettaglioFasSIEP.getLuogoDetenzione().getAltroLuogo().equals(""))
    {
    %>
    <tr>
      <td class="L" colspan=4>
        <font class="label">Indirizzo :</font>
        <font class="campo"><%=StringUtils.toStringJSP(dettaglioFasSIEP.getLuogoDetenzione().getAltroLuogo())%></font>
      </td>
    </tr>
    <%
    }
  }
}
      
      } // end if detenuto altra causa
    }// end if(dettaglioFasSIEP.getPosizioneGiuridica() != null)  
    %>
  </table>
  
  <%
  //============================================================================
  //
  //============================================================================
  %>
  <%
  PenaResiduaModel lPenResMod=dettaglioFasSIEP.getPenaResidua();
  if(lPenResMod != null)
  {  // ultima pena validata
  %>
  <table cellspacing=2 cellpadding=2 >
    <tr>
      <td class="L"> 
        <font class="label">Pena da espiare : </font>
        <!--ERGASTOLO--->
        <% if( "S".equals(lPenResMod.getFlagErgastolo()) ) { %>
          <font class="campo">ERGASTOLO</font>
        <% } else if( "D".equals(lPenResMod.getFlagErgastolo()) ){ %>
          <font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font>
          <% if(lPenResMod.getNumAnniIsolamentoDiurno()!=null && lPenResMod.getNumAnniIsolamentoDiurno().intValue()!=0 ) { %>
            <font class="label">Anni</font>
            <font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumAnniIsolamentoDiurno(),"0")%></font>
          <% } %>
    
          <% if(lPenResMod.getNumMesiIsolamentoDiurno()!=null && lPenResMod.getNumMesiIsolamentoDiurno().intValue()!=0) { %>
            <font class="label">Mesi</font>
            <font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumMesiIsolamentoDiurno(),"0")%></font>
          <% } %>
    
          <% if(lPenResMod.getNumGiorniIsolamentoDiurno()!=null && lPenResMod.getNumGiorniIsolamentoDiurno().intValue()!=0) { %>
            <font class="label">Giorni</font>
            <font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumGiorniIsolamentoDiurno(),"0")%></font>
          <% } %>
        <% } %>
    
        <%if(   (lPenResMod.getNumAnniReclusione()!=null && lPenResMod.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0)
             || (lPenResMod.getNumMesiReclusione()!=null && lPenResMod.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0)
             || (lPenResMod.getNumGiorniReclusione()!=null && lPenResMod.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0)
            )
        { 
        %>
            <font class="campo">Reclusione</font>
            <font class="label">Anni</font>
            <font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumAnniReclusione(),"0")%></font>
            <font class="label">Mesi</font>
            <font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumMesiReclusione(),"0")%></font>
            <font class="label">Giorni</font>
            <font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;
        <% } %>
    
        <% if ( lPenResMod.getImportoMulta()!=null && lPenResMod.getImportoMulta().compareTo(new BigDecimal(0))!=0) { %>
          <font class="label">Multa </font>
          <font class="campo"><%=StringUtils.toEuroFormat(lPenResMod.getImportoMulta())%></font>&nbsp;&euro;&nbsp;&nbsp;&nbsp;&nbsp;
        <% } %>
    
        <%if (   (lPenResMod.getNumAnniArresto()!=null && lPenResMod.getNumAnniArresto().compareTo(new BigDecimal(0))!=0)
              || (lPenResMod.getNumMesiArresto()!=null && lPenResMod.getNumMesiArresto().compareTo(new BigDecimal(0))!=0)
              || (lPenResMod.getNumGiorniArresto()!=null && lPenResMod.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0)
             )
        {
        %>
            <font class="campo">Arresto</font>
            <font class="label">Anni</font>
            <font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumAnniArresto(),"0")%></font>
            <font class="label">Mesi</font>
            <font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumMesiArresto(),"0")%></font>
            <font class="label">Giorni</font>
            <font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;
        <%}%>
    
        <% if(lPenResMod.getImportoAmmenda()!=null && lPenResMod.getImportoAmmenda().compareTo(new BigDecimal(0))!=0) {%>
            <font class="label">Ammenda </font>
            <font class="campo"><%=StringUtils.toEuroFormat(lPenResMod.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;
        <%}%>
      </td>
    </tr>  
    <tr>
      <td class="L">
        <%if(lPenResMod.getDataInizio()!= null){%>
          <font class="label">Inizio Pena : </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenResMod.getDataInizio(),"dd-MM-yyyy"))%></font>&nbsp;
        <%}%>
        <%
          PenaComplessivaSanzioneSostitutivaModel lPenaSostMod = dettaglioFasSIEP.getPenaComplessivaSanzioneSostitutiva();
          PenaComplessivaModel lPenCompMod= null;
    
          if(lPenaSostMod!=null){
            lPenCompMod=lPenaSostMod.getPenaComplessiva();
          }
    
          if(   lPenCompMod != null 
             && lPenCompMod.getCodTipoPenaDetentiva() != null
             && (   lPenCompMod.getCodTipoPenaDetentiva().equals("03")
                 || lPenCompMod.getCodTipoPenaDetentiva().equals("04")
                )            
            )
          { // ERGASTOLO
          %>
            <font class="label">Fine Pena : </font> <font color=red>MAI</font>
          <%
          }
          else if(   lPenResMod.getDataFine() != null
                  && lPenResMod.getDataFinePresunta() != null
                  && (!lPenResMod.getDataFine().equals(lPenResMod.getDataFinePresunta()))
                 )
          {
          %>
            <font class="label">Fine Pena : </font>   <font  color=red><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenResMod.getDataFine(),"dd-MM-yyyy"))%></font>
          <%
          }
          else
          {
            if(lPenResMod.getDataFine()!= null){ %>
               <font class="label">Fine Pena : </font><font  class="cVerde"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenResMod.getDataFine(),"dd-MM-yyyy"))%></font>
         <% }
          }
          %>
        </td>
      </tr>    
    
  </table>
  <% } // end if(lPenResMod != null)  %>
  
  

  
  <br>
   
<%    
//==============================================================================    
//                                  BENEFICI
//==============================================================================    
List lBenefici = dettaglioFasSIEP.getBenefici();
if(lBenefici != null && lBenefici.size() != 0)
{
%>
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr><td class="Titolo" colspan=4>Benefici</td></tr>
    <tr>
      <td class="l">
        <center><font class="label">Tipo</font></center>
      </td>
      <td class="l">
        <center><font class="label">Subordinata</font></center>
      </td>
      <td class="l">
        <center><font class="label">DPR</font></center>
      </td>
       <td class="l">
        <center><font class="label">Pena</font></center>
      </td>
    </tr>
    <%
    Iterator lIterBenefici = lBenefici.iterator();
    while (lIterBenefici.hasNext())
    {
      BeneficioModel lBene = (BeneficioModel)lIterBenefici.next();
      %>
    <tr>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrNaturaBeneficio(), "-")%></font>
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrTipoBeneficio(), "-")%></font>
      </td>
      <td class="l">
        <%if(lBene.getDescrTipoSospSubordinata()!= null && !lBene.getDescrTipoSospSubordinata().equals("")){%>
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrTipoSospSubordinata(),"-")%></font>
        <%}%>
      </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrDpr(), "-")%></font>
      </td>
      <td class="l">
      <% 
      if((lBene.getNumAnniReclusione()!=null && lBene.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0) || (lBene.getNumMesiReclusione()!=null && lBene.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0) || (lBene.getNumGiorniReclusione()!=null && lBene.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0))
      {
      %>
        <font class="campo">Reclusione</font>
        <font class="label">Anni</font>
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getNumAnniReclusione(), "0")%></font>
        <font class="label">Mesi</font>
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getNumMesiReclusione(), "0")%></font>
        <font class="label">Giorni</font>
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getNumGiorniReclusione(), "0")%></font>
        <%
        }
              
              
        if(lBene.getImportoMulta()!=null && lBene.getImportoMulta().compareTo(new BigDecimal(0))!=0)
        {
        %>
        <font class="label">Multa </font>
        <font class="campo"><%=StringUtils.toEuroFormat(lBene.getImportoMulta())%></font>&nbsp;€&nbsp;
        <%
        }
        
        if((lBene.getNumAnniArresto()!=null && lBene.getNumAnniArresto().compareTo(new BigDecimal(0))!=0) || (lBene.getNumMesiArresto()!=null && lBene.getNumMesiArresto().compareTo(new BigDecimal(0))!=0) || (lBene.getNumGiorniArresto()!=null && lBene.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0))
        {
        %>
        <font class="campo">Arresto</font>
        <font class="label">Anni</font>
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getNumAnniArresto(), "0")%></font>
        <font class="label">Mesi</font>
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getNumMesiArresto(), "0")%></font>
        <font class="label">Giorni</font>
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getNumGiorniArresto(), "0")%></font>
        <%
        }
        
        if(lBene.getImportoAmmenda()!=null && lBene.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
        {
        %>
        <font class="label">Ammenda </font>
        <font class="campo"><%=StringUtils.toEuroFormat(lBene.getImportoAmmenda())%></font>&nbsp;€&nbsp;
        <%
        }
        %>
            &nbsp;
      </td>
    </tr>
    <% 
     }  // end while su benefici
    %>
</table>
<%
} // end if Benefici
%>      
  
   
<% 
//==========================================================================
//                               REATI
//==========================================================================
List lReatiCirostanze = dettaglioFasSIEP.getReatiCircostanze();
if(lReatiCirostanze != null && lReatiCirostanze.size() != 0)
{
%>
    <table cellspacing=1 cellpadding=1 width=95%>
      <tr>
        <td class="Titolo">Reati</td>
      </tr>
<%
      Iterator lIterReati = lReatiCirostanze.iterator();
      while(lIterReati.hasNext())
      {
        ReatoCircostanzaModel lReatoCircostanza = (ReatoCircostanzaModel)lIterReati.next();
        ReatoModel lReato = lReatoCircostanza.getReato();
        ReatoModel[] lCircostanze = lReatoCircostanza.getCircostanze();

        boolean lFlagAnnoNumero = false;
        if(   lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals("")
           && lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals("") 
          )
        {
          lFlagAnnoNumero = true;
        }
%>
        <tr>
          <td class="l">
<%
          if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals(""))
          {%>
            <font class="label">
            <%out.println("Reato " + lReato.getProgrNumeroManuale()+": ");%>
            </font>
          <%
          } 
          else 
          {
            out.println("Reato " + lReato.getProgrReato()+": ");
          }
          %>
          
          <font class="campo">
          <%
            if(lFlagAnnoNumero)
            {
              if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
                out.println(lReato.getDescrFonte()+" ");
              if(lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals(""))
                out.println(lReato.getAnnoFonte());
              if(lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
                out.println("/"+lReato.getNumeroFonte());
            }

            if(lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
              out.println("art."+lReato.getArticolo());
            if(lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("") && !lReato.getDescrSottonumerazione().equals("-"))
              out.println(" "+lReato.getDescrSottonumerazione());

            if(!lFlagAnnoNumero)
            {
              if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
                out.println(lReato.getDescrFonte());
            }

            if(lReato.getComma() != null && !lReato.getComma().equals(""))
              out.println(" c. "+lReato.getComma());
            if(lReato.getLettera() != null && !lReato.getLettera().equals(""))
              out.println(" l. "+lReato.getLettera());
            if(lReato.getNumero() != null && !lReato.getNumero().equals(""))
              out.println(" n. "+lReato.getNumero());

            //CIRCOSTANZE
            if(lCircostanze != null)
            {
              ReatoModel lCirc = null;
              for(int i=0; i<lCircostanze.length; i++)
              {
                lCirc = lCircostanze[i];
%>
                      ,
<%
                boolean lFlagAnnoNumeroCirc = false;
                if( lCirc.getAnnoFonte() != null
                    && !lCirc.getAnnoFonte().equals("")
                    && lCirc.getNumeroFonte() != null
                    && !lCirc.getNumeroFonte().equals("") )
                {
                  lFlagAnnoNumeroCirc = true;
                }
                if(lFlagAnnoNumeroCirc)
                {
                  if(lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("") && !lCirc.getDescrFonte().equals("-"))
                    out.println(lCirc.getDescrFonte()+" ");
                  if(lCirc.getAnnoFonte() != null && !lCirc.getAnnoFonte().equals(""))
                    out.println(lCirc.getAnnoFonte());
                  if(lCirc.getNumeroFonte() != null && !lCirc.getNumeroFonte().equals(""))
                    out.println("/"+lCirc.getNumeroFonte());
                }

                if(lCirc.getArticolo() != null && !lCirc.getArticolo().equals(""))
                  out.println("art."+lCirc.getArticolo());
                if(lCirc.getDescrSottonumerazione() != null && !lCirc.getDescrSottonumerazione().equals("") && !lCirc.getDescrSottonumerazione().equals("-"))
                  out.println(" "+lCirc.getDescrSottonumerazione());

                if(!lFlagAnnoNumeroCirc)
                {
                  if(lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("") && !lCirc.getDescrFonte().equals("-"))
                    out.println(lCirc.getDescrFonte());
                }

                if(lCirc.getComma() != null && !lCirc.getComma().equals(""))
                  out.println(" c. "+lCirc.getComma());
                if(lCirc.getLettera() != null && !lCirc.getLettera().equals(""))
                  out.println(" l. "+lCirc.getLettera());
                if(lCirc.getNumero() != null && !lCirc.getNumero().equals(""))
                  out.println(" n. "+lCirc.getNumero());
              }
            } // end if CIRCOSTANZE
%>
                </font>
        <%

        if(lReato.getStringaConsumazione()!= null) { %>
          <!--  <font class="label">Data</font> -->
          <font class="campo"><%=StringUtils.toStringJSP(lReato.getStringaConsumazione())%>,</font>
        <% }

        if(lReato.getNote() != null && !lReato.getNote().equals("")) { %>
          <font class="campo">&nbsp;<%=StringUtils.toStringJSP(lReato.getNote())%>&nbsp;</font>
        <% } 

        if(lReato.getDescLuogo()!= null && !lReato.getDescLuogo().equals("")) { %>
          <font class="label">Luogo</font>&nbsp;
          <font class="campo"><%=StringUtils.toStringJSP(lReato.getDescLuogo())%></font>
        <% }
        
        %>
          </td>
        </tr>
<%
        } // end While sui reati
  }
%>
</table>



  
<%    
//==============================================================================      
//                             MISURE CAUTELARI
//==============================================================================      

List lMisureCautelari = dettaglioFasSIEP.getMisureCautelari();
if(lMisureCautelari != null && lMisureCautelari.size() != 0)
{
%>

  <table cellspacing=0 cellpadding=0 width=95%>
    <tr><td class="Titolo" colspan="4">Misure Cautelari</td></tr>
    <tr>
      <td class="l">
        <center><font class="label">Misura</font></center>
      </td>
      <td class="l">
        <center><font class="label">Data Inizio</font></center>
      </td>
      <td class="l">
        <center><font class="label">Data Fine</font></center>
      </td>    
      <td class="l">
        <center><font class="label">Totale</font></center>
      </td>     
    </tr>

    <%
    
      CalendarUtil cu=new CalendarUtil();
      CalendarModel cm;
      CalendarModel ctot=new CalendarModel();

      Iterator lIterMisureCautelari = lMisureCautelari.iterator();
      while (lIterMisureCautelari.hasNext())
      {
        MisuraCautelareModel lMisCau = (MisuraCautelareModel)lIterMisureCautelari.next();
        String classFont = "CVerde";
        String isComputabile = "";
 
        if (lMisCau.getDataFine()!=null && lMisCau.getDataInizio()!=null)
        {
          cm=new CalendarModel();
          cm.setDataFine(lMisCau.getDataFine());
          cm.setDataInizio(lMisCau.getDataInizio());
          cm=cu.ricalcolaGAM(cu.CalcolaNumGiorniMesiAnni(cm));
          
          isComputabile="Anni " +cm.getNumAnni()+" Mesi "+cm.getNumMesi()+" Giorni "+cm.getNumGiorni();
          
          if (lMisCau.getFlagComputabile().equals("S"))
          {
            ctot=cu.sommaGiorni(ctot,cm);
            classFont="CVerde";
          } 
          else
          {
            isComputabile="NON COMPUTABILE";
            classFont="C";
          }
        } 
        else
        {
          if (lMisCau.getFlagComputabile().equals("S"))
            classFont="CVerde";
            
          isComputabile="-";
        }   
    %>
    <tr>
      <td class="l">
        <font class="<%=classFont%>"><%=StringUtils.toStringJSP(lMisCau.getDescrTipoMisura(), "-")%>&nbsp;</font>
      </td>
      <td class="l">
        <font class="<%=classFont%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisCau.getDataInizio(), "dd-MM-yyyy"))%>&nbsp;</font>
      </td>
      <td class="l">
        <font class="<%=classFont%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisCau.getDataFine(), "dd-MM-yyyy"))%>&nbsp;</font>
      </td>
      <td class=<%=classFont%>><%=isComputabile%></td>
    </tr>
    <% } %>
  </table>
<%
}  // end misure cautelari
%>

<%
//==============================================================================
//  Visualzzazione delle Misure di Sicurezza: in sentenza o in cumulo
//==============================================================================
%>
<%
//==============================================================================    
//                            MISURE SICUREZZA
//==============================================================================    
if(   fascicoloSIEP.getFlagCumulante() != null 
   && fascicoloSIEP.getFlagCumulante().equals("S") 
   && dettaglioFasSIEP.getPenaCumulo() != null && dettaglioFasSIEP.getPenaCumulo().getMisuraSicurezza() != null  
   && !dettaglioFasSIEP.getPenaCumulo().getMisuraSicurezza().equals("")
  )
{
  // Fascicolo cumulante con misure di sicurezza
%>
  <table cellspacing="0" cellpadding="0" width="95%">
    <tr><td class="Titolo" colspan="3">Misure Sicurezza in Cumulo</td></tr>
    <tr>
      <td class="l">
        <font class="campo">        
        <%=StringUtils.toStringJSP(dettaglioFasSIEP.getPenaCumulo().getMisuraSicurezza())%>
        </font>
      </td>
    </tr>
  </table>
<%
} 
else 
{
  List lMisureSicurezza = dettaglioFasSIEP.getMisureSicurezza();
  if(lMisureSicurezza != null && lMisureSicurezza.size() != 0)
  {
  %>
  <table cellspacing="0" cellpadding="0" width="95%">
    <tr>
      <td class="Titolo" colspan="3">Misure Sicurezza</td>
    </tr>
    <%
    if(lMisureSicurezza != null && lMisureSicurezza.size() != 0)
    {
    %>
      <tr>
        <td class="l">
          <center><font class="label">Natura Misura</font></center>
        </td>
        <td class="l">
          <center><font class="label">Tipo Misura</font></center>
        </td>
        <td class="l">
          <center><font class="label">Durata Misura</font></center>
        </td>
      </tr>
      <%
      Iterator lIter = lMisureSicurezza.iterator();
      while (lIter.hasNext())
      {
        MisuraSicurezzaModel lMisSicu = (MisuraSicurezzaModel)lIter.next();
      %>
        <tr>
          <td class="l">
            <font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getDescrNatura(),"-")%></font>
          </td>
          <td class="l">
            <font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getDescrTipo(),"-")%></font>
          </td>
          <td class="l">
            <font class="campo">
              AA:&nbsp;<%=StringUtils.toStringJSP(lMisSicu.getNumAnni(), "0")%>&nbsp;
              MM:&nbsp;<%=StringUtils.toStringJSP(lMisSicu.getNumMesi(), "0")%>&nbsp;
              GG:&nbsp;<%=StringUtils.toStringJSP(lMisSicu.getNumGiorni(), "0")%>
            </font>
          </td>
        </tr>
      <%
      }  // end while
    } // end if
%>
      </table>
<%
    }
  }
%>



<%
//==============================================================================
//==============================================================================
%>

<% } // end if isErrParser %>    

  <br>
  <form name="azioniPresaIncarico">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
    <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_FLAG_VISTO%>" value="">
    <input type="HIDDEN" name="MotivoRestituzione" value="">
    <input type="HIDDEN" name="NumerazioneManuale" value="">

    <input type="HIDDEN" name="codEsito" value="<%=Messaggio.getCodEsito()%>">

<% 
if (   !ICostantiJMS.ISCRITTO_CLASSE_IV.equals(Messaggio.getCodEsito()) 
    && !ICostantiJMS.RESTITUITO.equals(Messaggio.getCodEsito()) 
    && !ICostantiJMS.TRASFERITO.equals(Messaggio.getCodEsito()) 
   )
{
  // Il messaggio è ancora 'pending' sono possibili azioni, devo visualizzare gli 
  // opportuni tasti
  boolean buttonPresaInCaricoDisabled = false;
  boolean buttonRestAttiDisabled = false;
  boolean buttonInoltroAttiDisabled = false;
  boolean buttonIscrizioneDisabled = false;
  boolean buttonIscrizioneManualeDisabled = false;


  if ("SI".equals(isErrParser)) {
    // errore nel parsing del blob posso solo retituire gli atti
    buttonPresaInCaricoDisabled = true;
    buttonRestAttiDisabled = false;
    buttonInoltroAttiDisabled = true;
    buttonIscrizioneDisabled = true;
    buttonIscrizioneManualeDisabled = true;
    
    if (ICostantiJMS.PRESAINCARICO.equals(Messaggio.getCodEsito()) ){
      // se ho già preso in carico gli atti posso procedere comunque all'iscrizione
      //buttonRestAttiDisabled = true;
      buttonRestAttiDisabled = false;
      buttonIscrizioneDisabled = false;
      buttonIscrizioneManualeDisabled = false;
    }
  }  
  else if (ICostantiJMS.PRESAINCARICO.equals(Messaggio.getCodEsito()) ) { 
    buttonPresaInCaricoDisabled = true;
    //buttonRestAttiDisabled = true;
    buttonRestAttiDisabled = false;
    //buttonInoltroAttiDisabled = true;
    buttonInoltroAttiDisabled = false;
    buttonIscrizioneDisabled = false;
    buttonIscrizioneManualeDisabled = false;
  } 
  else if (!ICostantiJMS.PRESAINCARICO.equals(Messaggio.getCodEsito()) ) { 
    // Disabilito i tasti di iscrizione se non ancora preso in carico
    buttonIscrizioneDisabled = true;
    buttonIscrizioneManualeDisabled = true;
  } 
  %>
    <table cellspacing=2 cellpadding=2  width="95%" align="left">
      <tr>
        <td>
          <input class="bottone" type="button" name="PresaInCaricoButton" value="Presa in Carico&#10; " <%= buttonPresaInCaricoDisabled ? "disabled":""%>
                 onclick="invia('siap.siep.misurasicurezza.action.ActPresaInCaricoAttoRicevuto')"
                 style="width: 120px;">
        </td>
        <td>
          <input class="bottone" type="button" name="RestituzioneButton" value="Restituzione Atti&#10; "  <%= buttonRestAttiDisabled ? "disabled":""%>
                 onclick="Javascript:restAtti();"
                 style="width: 130px;">
        </td>
        <td>
          <input class="bottone" type="button" name="InoltroButton" value="Trasferimento atti per competenza&#10;(altro Ufficio) "  <%= buttonInoltroAttiDisabled ? "disabled":""%>
                 onclick="Javascript:invia('siap.siep.misurasicurezza.action.ActLoadInoltraAttoRicevuto');"
                 style="width: 250px;">
        </td>
        <td>
          <input class="bottone" type="button" name="IscrizioneButton" value="Iscrizione Procedimento Classe IV&#10; "  <%= buttonIscrizioneDisabled ? "disabled":""%>
                 onclick="Javascript:iscrizione();"  
                 style="width: 250px;">
        </td>
        <td>
          <input class="bottone" type="button" name="IscrizioneManualeButton" value="Iscrizione Procedimento Classe IV&#10;(Anno e Numero manuale)"  <%= buttonIscrizioneManualeDisabled ? "disabled":""%>
                 onclick="Javascript:iscrizionemanual();"  
                 style="width: 250px;">
        </td>        
      </tr>
    </table> 
<% } %>  
  
  </form>



   
</body>
</html>
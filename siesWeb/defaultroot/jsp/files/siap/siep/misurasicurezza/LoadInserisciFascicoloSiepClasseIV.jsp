<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio" %>
<%@ page import="siap.siep.misurasicurezza.model.FascMsToFascSiepModel" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="fascicolo" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="soggetto"  scope="request" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="sentenza"  scope="request" class="siap.siep.sentenza.model.SentenzaModel" />

<jsp:useBean id="IdMessaggio"           scope="request" class="java.lang.String"/>
<jsp:useBean id="NumerazioneManuale"    scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoliClasseIVIscritti" scope="request" class="java.util.Vector"/>

<jsp:useBean id="penacumulo"            scope="request" class="siap.siep.penacumulo.model.PenaCumuloModel"/>
<jsp:useBean id="listaMisure"           scope="request" class="java.util.Vector"/>

<% // S/N indica l'esistenza di almeno un procedimento di classe IV per l'anno corrente %>
<jsp:useBean id="EsisteFascicoloClasseIVAnnoCorrente" scope="request" class="java.lang.String"/>
<jsp:useBean id="AnnoCorrente" scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Form per l'iscrizione di un Procedimento di Esecuzione delle Misure di Sicurezza
// utilizzata nel caso in cui un Procedimento nasce da altro fascicolo es classe I
//==============================================================================
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//siesLogger.debug("jsp: NumerazioneManuale = "+NumerazioneManuale);

String assegnazione_manuale = "N";
if ("S".equals(NumerazioneManuale))
  assegnazione_manuale = "S";
else 
  assegnazione_manuale = "N";

// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//siesLogger.debug("jsp: assegnazione_manuale = "+assegnazione_manuale);

String lTitoloFunzione = "";

if (IdMessaggio!=null && IdMessaggio.length()>0){
  // Iscrizione da presa in carico
  lTitoloFunzione = "Inserimento Procedimento Esecuzione Misure di Sicurezza da Atti Presa in Carico";
}
else {
  // Iscrizione da fascicolo di propria competenza
  lTitoloFunzione = "Inserimento Procedimento Esecuzione Misure di Sicurezza da Atti di Propria Competenza";
}


String lMessaggio = null;
if (listaMisure.size() == 0 &&
		(penacumulo.getMisuraSicurezza()==null ||
    		penacumulo.getMisuraSicurezza().length()==0)) {
// --> 10-11-2015 - Questo controllo è spostato a monte della form (sulla ActLoad..)
// lMessaggio = "Attenzione!! Sul procedimento di origine ("+fascicolo.getChiaveAnno()+"/"+fascicolo.getChiaveProgr()+") non sono iscritte Misure di Sicurezza da eseguire";
}
// --> 02-07-2015 - Interventi urgenti su STEP1 - 
//				 Questo controllo è spostato a monte della form (sulla ActLoad..) con
//                 inserimento MSG BOX di warning con possibilità di ANNULLARE l'operazione di iscizione
// else if ( listaMisure.size()==0 && penacumulo.getMisuraSicurezza()!=null && penacumulo.getMisuraSicurezza().length()>0){
// Presenti solo in cumulo
// lMessaggio = "Attenzione!! Sul procedimento di origine ("+fascicolo.getChiaveAnno()+"/"+fascicolo.getChiaveProgr()+") sono presenti Misure di Sicurezza disposte in cumulo "
//	          + "e iscritte a testo libero che non è possibile riportare in automatico sul procedimento di classe IV che si sta iscrivendo. "
//    	      + "Prima di validare il procedimento di Classe IV sarà necessario provvedere alla loro iscrizione.";
//	}
else if (listaMisure.size()>0) {
  boolean isDatiMisureIncompleti = false;
  Iterator lIterMis = listaMisure.iterator();
  while (lIterMis.hasNext()) {
    MisuraSicurezzaModel lMisSicu = (MisuraSicurezzaModel)lIterMis.next();
    if (  "-".equals(lMisSicu.getCodTipo()) ) {
      isDatiMisureIncompleti = true;
    }
  }
  if (isDatiMisureIncompleti)
    lMessaggio = "Attenzione!! Verificare i dati delle Misure di Sicurezza iscritte sul Procedimento. Il Tipo misura non è specificato. Se si iscrive il procedimento di classe IV sarà necessario procedere alla correzione prima di poter validare il procedimento.";
}

%>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Inserimento Fascicolo Siep da classe IV</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
      
    function Verify()
    {
      //=================================
      // Data Iscrizione provvedimento
      //=================================
      var dataIscrizione = '//' ;
      if (document.LoadInserisciFascicolo.assegnazione_manuale.value == 'S'){
          dataIscrizione = document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value+
                       '/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>.value+
                       '/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>.value;
      }
      else {
        dataIscrizione = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
      }
      if (!ControllaData(dataIscrizione))
      {
        alert('Data Iscrizione Procedimento non valida '+ dataIscrizione);
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.focus();
        return false;
      }
      
      //=================================
      // Data Arrivo Atto
      //=================================
      var dataArrivoAtto = document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_ATTO%>.value+
                       '/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_ATTO%>.value+
                       '/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ARRIVO_ATTO%>.value;

      if (! ControllaData(dataArrivoAtto))
      {
        alert('Data Arrivo Atto non valida '+ dataArrivoAtto);
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_ATTO%>.focus();
        return false;
      }

      //=================================
      // Data Irrevocabilità
      //=================================
      var dataIrrevocabilita=document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value+
                         '/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value+
                         '/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
   
      if (! ControllaData(dataIrrevocabilita) )
      {
        alert('Data Irrevocabilità non valida '+dataIrrevocabilita);
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.focus();
        return false;
      }
  
      if (!CompareDate(dataArrivoAtto,dataIscrizione))
      {
        alert('la Data iscrizione Procedimento deve essere successiva alla Data Arrivo Atto');
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.focus();
        return false;
      }

      if (!CompareDate(dataIrrevocabilita,dataArrivoAtto))
      {
        alert('La Data Irrevocabilità deve essere precedente alla Data Arrivo Atto');
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.focus();
        return false;
      }
   
      var dataSentenza= '<%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd/MM/yyyy")%>'; 
      if (!CompareDate(dataSentenza,dataArrivoAtto))
      {
        alert('Data Arrivo Atto deve essere successiva alla Data sentenza');
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_ATTO%>.focus();
        return false;
      }       

      if (CompareDate(dataIrrevocabilita,dataSentenza))
      {
        alert('Data Irrevocabilità deve essere successiva alla Data sentenza');
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.focus();
        return false;
      }       
      

      if (document.LoadInserisciFascicolo.assegnazione_manuale.value == 'S'){
        // Controlla la data Iscrizione
        var dataOdierna = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
        if (!CompareDate(dataIscrizione,dataOdierna))
        {
          alert('La Data Iscrizione non può essere una data futura');
          document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.focus();
          return false;
        }
        
        // Controllo campo chiave anno.
        var anno_sistema = '<%=DateUtils.getSysDate("yyyy")%>';
        var chiave_anno = document.LoadInserisciFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value;
        
        if (chiave_anno==''){
          alert("Valorizzare Il campo Anno Procedimento in caso di numerazione manuale");
          document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus();
          return false;
        }
        else if (chiave_anno<1900) {
          alert("Verificare il campo Anno Procedimento, il valore digitato è trobbo basso ");
          document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus();
          return false;
        }
        
        if (chiave_anno > anno_sistema) {          
          alert("Il campo Anno Procedimento non può superare l'anno corrente");
          document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus();
          return false;
        }

        // Verifica delle congruenza del numero digitato con la classe scelta
        var minrange = 40000;
        var maxRange = 50000;
        

        // Controllo il progressivo 
        if (document.LoadInserisciFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value > maxRange) 
        {
          alert ("Valore superiore al massimo consentito ("+minrange+") nel campo Numero Procedimento");
          document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.focus();
          return false;
        }
      
        if (parseInt(document.LoadInserisciFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value) == 0) {
          alert ("Valore non consentito nel campo Numero Procedimento");
          document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.focus();
          return false;
        }
      
        if ((document.LoadInserisciFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value < minrange) ||
            (document.LoadInserisciFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value > maxRange) )
        {
          alert ("Valore INCONGRUENTE con il 'Tipo Classe' nel campo Numero Procedimento. Valori consentiti da "+minrange+" a "+maxRange);
          document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.focus();
          return false;
        }
      }
    }
    
    
    function VisualizzaNumerazioneManuale()
    {
      if(document.LoadInserisciFascicolo.checkNumMan.checked == true)
      {
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.disabled = false;
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.disabled = false;
        document.LoadInserisciFascicolo.assegnazione_manuale.value = "S";
        document.getElementById("dataManuale").style.display = "block";
        document.getElementById("dataAutomatica").style.display = "none";
      }
      else {
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value="";
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value="";
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.disabled = true;
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.disabled = true;
        document.LoadInserisciFascicolo.assegnazione_manuale.value = "N";
        document.getElementById("dataManuale").style.display = "none";
        document.getElementById("dataAutomatica").style.display = "block";
      }
    }
    
    function VisualizzaMessaggio() {
    <% if (lMessaggio!=null) { %>
      alert ('<%=lMessaggio%>');
    <% } %>
    
    }
    
    </script>
  </head>



  <body class="corpo" onLoad="Javascript:VisualizzaMessaggio();">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :</font>&nbsp;
                    <font class="campo"><%=lTitoloFunzione%></font>
      </td>
      <!-- BOTTONE DI RITORNO -->
      <% if (IdMessaggio!=null && IdMessaggio.length()>0){ %>
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      <% } else { %>
      <td class="LBG">
        <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActGestioneMisureSicurezza">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
      <% } %>
    </tr>
  </table>

  <br>
  
  <br>
  
  <% 
  //========================================================================
  //                    Dettaglio Soggetto e Sentenza   
  //======================================================================== 
  %>
   <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L" width=100%>
        <font class="label">Iscrizione dal Procedimento N.</font>&nbsp;<font class="campo"><%=fascicolo.getChiaveAnno()%>/<%=fascicolo.getChiaveProgr()%></font>
        <!--% if (IdMessaggio!=null && IdMessaggio.length()>0){ % -->  
         di <font class="campo"><%=fascicolo.getDescrTipoUfficio()%> di <%=fascicolo.getDescrComuneUfficio()%> </font>
        <!--  % } % -->
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
       <td class="L" width=100%><font class="label">Soggetto: </font>&nbsp;
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>" title="Soggetto">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
        </a>
      </font>&nbsp;
      <% if (soggetto.getSesso().compareTo("F")==0) { %>
        <font class="label">nata il :</font>&nbsp;
      <% } else { %>
        <font class="label">nato il :</font>&nbsp;
      <% } %>
      <font class="campo"><%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%></font>&nbsp;
      <font class="label">in : </font>&nbsp;
      <font class="campo">
      <% if (soggetto.getDescrComuneNascita().compareTo("-")==0) { %>
         <%=soggetto.getDescComuneNascitaEstero()%>  (<%=soggetto.getDescrStatoNascita().toUpperCase()%>)
      <% } else { %>
        <%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)
      <% } %>

      </font>
    </tr>
    <tr>
      <td class="L">
        <font class="campo"><%=sentenza.getDescrTipoProvvedimento()%></font>&nbsp;<font class="label">N. </font>&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(sentenza.getAnnoSentenza())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroSentenza())%> &nbsp;   
          <font class="label">del</font>&nbsp;
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
            <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>
          </a>
        </font>
        <%if(!sentenza.getCodTipoProvvedimento().equals("02")) { %> 
        &nbsp;<font class="label"> Emessa da: </font> 
        <% }else{%>
        &nbsp;<font class="label"> Emesso da: </font>
        <%} %>
        <font class="campo"><%=sentenza.getDescrTipoAutoritaEmittente()%></font> &nbsp;
        <% if (sentenza.getNumSezioneAutoritaEmittente() != null) { %>
          <font class="label">(Sez.</font> <font class="campo"><%=sentenza.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
        <% } %>
        <font class="label"> di </font>&nbsp;
        <font class="campo"><%=sentenza.getDescrLuogoEmittente()%></font>
      </td>
    </tr>
  </table> 
  <% //======================================================================== %>


<% if (fascicoliClasseIVIscritti!=null && fascicoliClasseIVIscritti.size()>0){ %>
  <br>
  <table cellspacing=2 cellpadding=2>
    <tr>
        <td class="l"><font color="red">Attenzione! Per il fascicolo <%=StringUtils.toStringJSP(fascicolo.getChiaveAnno())%>/<%=StringUtils.toStringJSP(fascicolo.getChiaveProgr())%> risulta già iscritto su questo Ufficio un procedimento di esecuzione delle misure di sicurezza (classe IV) </font> </td>
    </tr>
    <tr>
      <td class="titolo">Procedimenti già iscritti</td>
    </tr>
    <% for (int i=0;i<fascicoliClasseIVIscritti.size();i++) { %>
    <tr>
      <td class="l">
        Procedimento già presente: N.&nbsp;
        <a class="cliccabile" href="/jsp/Main.jsp?Action=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&ChiaveFascicolo=<%= ((FascMsToFascSiepModel)fascicoliClasseIVIscritti.elementAt(i)).getFasSieIdFascicoloCollegato()%>" title="Procedimento">
          <%=StringUtils.toStringJSP( (  (FascMsToFascSiepModel)fascicoliClasseIVIscritti.elementAt(i)).getChiaveAnnoSiepCollegato()) %>
          /
          <%=StringUtils.toStringJSP( (  (FascMsToFascSiepModel)fascicoliClasseIVIscritti.elementAt(i)).getChiaveProgrSiepCollegato()) %>
        </a>&nbsp;
      </td>
    </tr>
    <% } %>
  </table>
<% } %>

<% if ("N".equals(EsisteFascicoloClasseIVAnnoCorrente) ){ %>
  <br>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l">
        <font color="red">Attenzione! Si sta procedendo all'iscrizione del primo procedimento di Classe IV per l'anno corrente. 
         <br>Il progressivo indicato in questa fase sarà il valore dal quale partirà la numerazione automatica per i successivi procedimenti di classe IV.
         <br>Una volta indicato il progressivo iniziale, sarà possibile acquisire il pregresso per l'anno corrente (assegnare numerazione manuale) solo per procedimenti con numerazione inferiore a quella indicata i questa fase.
         <br>Come prima iscrizione è necessario quindi registrare o un nuovo procedimento assegnandogli opportuno progressivo secondo quanto prevede l'attuale registro Misure di Sicurezza,
         oppure registrare l'ultimo procedimento presente sul registro Misure di Sicurezza.
        </font> 
      </td>
    </tr>
  </table>
<% } %>

<%
//===================================================================
// Misure di sicurezza
//===================================================================
%>
<% if (lMessaggio!=null) { %>
<br>
<table>
  <% if (penacumulo!=null && penacumulo.getIdPenaCumulo()!=null) { %>
    <tr><td class="Titolo" colspan="3">Misure Sicurezza in Cumulo</td></tr>
    <tr>
      <td class="l">
        <font class="campo">
        <%=StringUtils.toStringJSP(penacumulo.getMisuraSicurezza())%>
        </font>
      </td>
    </tr>
  <% } else if (listaMisure != null && listaMisure.size() != 0){ %>
    <tr>
      <td class="Titolo" colspan="3">Misure Sicurezza</td>
    </tr>
    <tr>
      <td class="l">
        <center><font class="label">Natura Misura</font></center>
      </td>
      <td class="l">
        <center><font class="label" style="color:red;">Tipo Misura</font></center>
      </td>
      <td class="l">
        <center><font class="label" >Durata Misura</font></center>
      </td>
    </tr>
    <%
    Iterator lIterMis = listaMisure.iterator();
    while (lIterMis.hasNext())
    {
      MisuraSicurezzaModel lMisSicu = (MisuraSicurezzaModel)lIterMis.next();
    %>
      <tr>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getDescrNatura(),"-")%></font>
        </td>
        <td class="l">
          <font color="red">
          <font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getDescrTipo(),"-")%></font>
        </td>
        <td class="l">            
            <font class="l">AA:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getNumAnni(), "0")%>&nbsp;</font>
            <font class="l">MM:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getNumMesi(), "0")%>&nbsp;</font>
            <font class="l">GG:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getNumGiorni(), "0")%></font>
          </font>
        </td>
      </tr>
    <% } %>
  <% } %>
</table>
<% } %>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciFascicolo">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActInserisciFascicoloClasseIVdaClasseI" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=StringUtils.toStringJSP(fascicolo.getIdFascicoloSiep())%>">
  <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=IdMessaggio%>">  
  <input type="HIDDEN" name="assegnazione_manuale" value="<%=assegnazione_manuale%>">  
  
  
  
  <table cellspacing=2 cellpadding=2>
  <tr>
      <td class="l">Data Iscrizione Procedimento <font class=ob>(*)</font></td>
      <% if ("N".equals(assegnazione_manuale)) { %>
      <td class="l" id="dataAutomatica">      
        <%=DateUtils.getSysDate("dd") %>/<%=DateUtils.getSysDate("MM") %>/<%=DateUtils.getSysDate("yyyy") %>
      </td>
      <% } else { %>
      <td class="l" id="dataManuale">
          <input title="Data Iscrizione Procedimento" value="<%=DateUtils.getDateToString(fascicolo.getDataIscrizione(),"dd")%>" name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
          /
          <input title="Data Iscrizione Procedimento" value="<%=DateUtils.getDateToString(fascicolo.getDataIscrizione(),"MM")%>" name="<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
          /
          <input title="Data Iscrizione Procedimento" value="<%=DateUtils.getDateToString(fascicolo.getDataIscrizione(),"yyyy")%>" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <% } %>
    </tr>
    
    <%
    // La Data Arrivo Atto va valorizzata con la data del procedimento di partenza
    // solo se si sta inserendo da atti di propria competenza (iscrizione procedimento
    // da titolo esecutivo). Se invece si iscrive da atti ricevuti, la data va lasciata
    // libera.
    Date dataArrivoAtto = null;
    if (IdMessaggio!=null && IdMessaggio.length()>0){
      // Iscrizione da presa in carico
      dataArrivoAtto = null;
    } else {
      // Iscrizione da fascicolo di propria competenza
      dataArrivoAtto = fascicolo.getDataArrivoAtto();
    }
    %>
    <tr>
      <td class="l">Data Arrivo Atto <font class=ob>(*)</font></td>
      <td class="l">
        <input title="Data Arrivo Atto" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataArrivoAtto,"dd"),"")%>" name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_ATTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
        /
        <input title="Data Arrivo Atto" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataArrivoAtto,"MM"),"")%>" name="<%= ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_ATTO%>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
        /
        <input title="Data Arrivo Atto" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataArrivoAtto,"yyyy"),"")%>" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_ARRIVO_ATTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
        
    <tr>
      <% if(!sentenza.getCodTipoProvvedimento().equals("02")) { %>
      <td class="l">Data Irrevocabilità <font class=ob>(*)</font></td>
      <% } else { %>
      <td class="l">Esecutivo il</td>  <%// "02" = decreto penale %>
      <% } %>
      
      <td class="L">               
        <input title="Data Irrevocabilità" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"dd"))%>" name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
        /
        <input title="Data Irrevocabilità" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"MM"))%>" name="<%= ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
        /
        <input title="Data Irrevocabilità" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"yyyy"))%>" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>
    
    <% if ("S".equals(assegnazione_manuale)) {%>
    <tr height="15"><td> </td></tr>
    <tr>
      <td class="l">Anno e Numero Procedimento</td>
      <td class="l">
        <% 
        String lReadOnly = "";
        if ("N".equals(EsisteFascicoloClasseIVAnnoCorrente) ){ 
          lReadOnly = "readonly";
        } %>
        <input type="text" title="Anno Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(AnnoCorrente,"")%>"
               <%=lReadOnly%>
               onkeypress="return TicTabNumField(this,event)" 
               onFocus="javascript:textboxSelect(this)"  onBlur="javascript:value=FillYear(value)">
        /
        <input type="text" title="Numero Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" maxlength="13" size="15" 
               onkeypress="return TicTabNumField(this,event)">
      </td>
      <%
      // Nota: eliminata la numerazione speciale (res e pretura) previste solo 
      //       per la classe I
      %>
    </tr>
    <% } %>
    <tr height="15"><td> </td></tr>
  </table>
    
  <table cellspacing=2 cellpadding=2>
     <tr><td>&nbsp;</td></tr>
     <tr>
       <td class="l" title="Misura Sicurezza">Classe IV</td>
       <td class="l"><input type="radio" name="tipo" value="4" checked></td>
     </tr>
     <tr><td>&nbsp;</td></tr>
  </table>
  
  <table>
    <tr>
      <td class="l">Note Procedimento</td>
      <td class="l">
        <textarea title="Note Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_NOTE%>" cols=40 rows=5></textarea>
    </tr>
    <tr>
      <td>
        <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
      </td>
    </tr>
  </table>

</FORM>

<script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciFascicolo");
    
</script>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraSicurezzaCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="ListaMisureSicurezza" scope="request" class="java.util.Vector"/>

<%
//==============================================================================
// Form per la visualizzazione delle Misure di Sicurezza legate a un certo Titolo.
//
// La form presenta un elenco delle misure già presenti con la possibilità di 
// modificarle, cancellarle o inserirne delle nuove 
//==============================================================================
%>

<html>
<head>
  <title> Elenco Misure Sicurezza</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
    //==========================================================================
    // Visualizza, nasconde il record con il dettaglio dell'istruttoria
    //==========================================================================
    function visualizzaMotivo(idRecord)
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
    
    function visualizzaLuogo(idRecord)
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
    // Richiama l'opportuna azione
    //==========================================================================
    function eseguiAzione(aTipoAzione, aIdMisura, aStato, aMotivoModifica)
    {
      if (aTipoAzione=='Dettaglio'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadDettaglioMisuraSicurezzaCumulo";
        document.ListaMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_ID_MISURA_SICUREZZA_CUMULO%>.value = aIdMisura;
        document.ListaMisuraSicurezzaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ListaMisuraSicurezzaCumulo.submit();
      }
      else if (aTipoAzione=='Modifica'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadModificaMisuraSicurezzaCumulo";
        document.ListaMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_ID_MISURA_SICUREZZA_CUMULO%>.value = aIdMisura;
        document.ListaMisuraSicurezzaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ListaMisuraSicurezzaCumulo.submit();
      }
      else if (aTipoAzione=='Cancella'){
        if (aStato=='E' || aStato=='M'){
          // Cancellazione Logica Richiedo Motivazione
          lAzione = "siap.siep.modulocumulo.action.ActCancellaMisuraSicurezzaCumulo";
          document.ListaMisuraSicurezzaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.ListaMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_ID_MISURA_SICUREZZA_CUMULO%>.value = aIdMisura;
          document.ListaMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_FLAG_STATO%>.value = aStato;
          var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                     + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"ListaMisuraSicurezzaCumulo"
                                     + "&" + "<%=ICostantiMisuraSicurezzaCumulo.CAMPO_MOTIVO_MODIFICA%>" + "="+aMotivoModifica
                                     , "CancellaDatoAnalitico","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
          window.parent.close();
          
          // N.B. la submit viene effettuare direttamnete dalla finestra di popup
        }
        else if (aStato=='I'){
          // Cancellazione fisica richiedo conferma
          var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
          if (window.confirm(msgConfirm)) {
            lAzione = "siap.siep.modulocumulo.action.ActCancellaMisuraSicurezzaCumulo";
            document.ListaMisuraSicurezzaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;

            document.ListaMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_ID_MISURA_SICUREZZA_CUMULO%>.value = aIdMisura;
            document.ListaMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_FLAG_STATO%>.value = aStato;

            document.ListaMisuraSicurezzaCumulo.submit();
          }
        }
      }
    }
    
    //==========================================================================
    // Richiama la funzione di inserimento
    //==========================================================================
    function nuovaAnnotazione(){
      lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciMisuraSicurezzaCumulo";
      document.ListaMisuraSicurezzaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.ListaMisuraSicurezzaCumulo.submit();
    }
    
    //==========================================================================
    // Ritorna alla Griglia dei dati analitici
    //==========================================================================
    function tornaIndietro(action)
    {
      document.ListaMisuraSicurezzaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.ListaMisuraSicurezzaCumulo.submit();
    }

  </script>
  
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco Misure Sicurezza</font>
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia dei dati analitici -->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaDatiAnalitici')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="<%=ICostantiTitoloCumulato.PG_INCLUDE_DETTAGLIO_TITOLO%>"/>
      </td>
    </tr>
  </table>

<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="ListaMisuraSicurezzaCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  
  <!-- Campi valorizzati dinamicamente dalla eseguiAzione() -->
  <input type="hidden" name="<%=ICostantiMisuraSicurezzaCumulo.CAMPO_ID_MISURA_SICUREZZA_CUMULO%>" value="">
  <input type="hidden" name="<%=ICostantiMisuraSicurezzaCumulo.CAMPO_MOTIVO_MODIFICA%>"            value="">
  <input type="hidden" name="<%=ICostantiMisuraSicurezzaCumulo.CAMPO_FLAG_STATO%>"                 value="">

  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int" width="8%">Natura Misura</td>
      <td class="int" width="30%">Tipo Misura</td>
      <td class="int" width="5%">Anni</td>
      <td class="int" width="5%">Mesi</td>
      <td class="int" width="5%">Giorni</td>
      
      <td class="int" width="10%">Data Fine Validita</td>
      <td class="int" width="8%">Luogo Esecuzione</td>
      <td class="int" width="8%" title="Indica la natura dei dati rispetto a quelli importati">Stato</td>
      <!--  td class="int">Note</td -->
      <td class="int" width="8%">Azioni</td>
    </tr>
    <%
      int id_record = 0;

      Iterator itx = ListaMisureSicurezza.iterator();
      while ( itx.hasNext()) {
        id_record = id_record +1;
        MisuraSicurezzaCumuloModel lMisuraSicurezzaCumulo = (MisuraSicurezzaCumuloModel)itx.next();
        
        String lStato = "";
        String lDescStato = "";
        String lFontColor = "";
        String lAnnullata="";
        
        if      ( lMisuraSicurezzaCumulo.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
        else if ( lMisuraSicurezzaCumulo.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
        else if ( lMisuraSicurezzaCumulo.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
        else if ( lMisuraSicurezzaCumulo.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}
        
        if(lMisuraSicurezzaCumulo.getFlagAnnullaMisura()!=null && lMisuraSicurezzaCumulo.getFlagAnnullaMisura().compareTo("A")==0)
        {
            lAnnullata="SI";
            lFontColor="style=\"color:red\"";
        }
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getDescrNatura(),"&nbsp;")%></td>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getDescrTipo(),"&nbsp;")%></td>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumAnni(),"&nbsp;")%></td>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumMesi(),"&nbsp;")%></td>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumGiorni(),"&nbsp;")%></td>

      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisuraSicurezzaCumulo.getDataFineValidita(),"dd-MM-yyyy"),"-") %></td>
      
<%  if (lMisuraSicurezzaCumulo.getLuogoEsecuzioneMisura()!=null && lMisuraSicurezzaCumulo.getLuogoEsecuzioneMisura().length()>0) 
  { %>
    <td class="c">
        <a href="javascript:visualizzaLuogo('Luorec_<%=id_record%>')" title="Luogo esecuziome">
        Descrizione
        </a>
      </td>  
 <% }
  else 
  {%>
    <td class=c>&nbsp; - </td>
 <% } %>
      
   <!--     Motivo Inserimento/Modifica   -->
<%  if (lMisuraSicurezzaCumulo.getMotivoModifica()!=null && lMisuraSicurezzaCumulo.getMotivoModifica().length()>0) 
  { %>
    <td class="c">
        <a href="javascript:visualizzaMotivo('rec_<%=id_record%>')" title="<%=lDescStato%>">
        <%=lStato%>
        </a>
      </td>  
 <% }
  else 
  {%>
      <td class="c" title="<%=lDescStato%>">&nbsp;<%=lStato%></td>
 <% } %>
 
 <!--       Azioni       -->
      <td class="c" style="text-align:center"> &nbsp;
      <%
        //======================================================================
        // Azioni possibili: Cancellazione/Annullamento, Modifica, Dettaglio
       //======================================================================
      %>
        <a href="javascript:eseguiAzione('Dettaglio',<%=lMisuraSicurezzaCumulo.getIdMisuraSicurezzaCumulo() %> )">
          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Misura" border="0"></a>
        
        <% 
        //======================================================================
        // Modifiche consentite solo ad istrittoria aperta e su dati non Cancellati
        //======================================================================
        if(IstruttoriaCumulo.getFlagStato().equals("A") && !lMisuraSicurezzaCumulo.getFlagStato().equals("C")){ %>
        <a href="javascript:eseguiAzione('Modifica',<%=lMisuraSicurezzaCumulo.getIdMisuraSicurezzaCumulo() %>)">
          <img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
        <a href="javascript:eseguiAzione('Cancella',<%=lMisuraSicurezzaCumulo.getIdMisuraSicurezzaCumulo() %>,'<%=lMisuraSicurezzaCumulo.getFlagStato() %>','<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lMisuraSicurezzaCumulo.getMotivoModifica()),"") %>')">
          <img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
        <% } %>
      </td>
    </tr>
   <!-- Record Hidden con la descrizione del Luogo esecuzione Misura-->
    <tr style="display:none" id="Luorec_<%=id_record%>">
      <td class="l" colspan="100%">
        <font class="campoSmall">
        <%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getLuogoEsecuzioneMisura(),"&nbsp;")%>
        </font>
      </td>
    </tr>
    <!-- Record Hidden con le note di Motivo/Modifica-->
    <tr style="display:none" id="rec_<%=id_record%>">
      <td class="l" colspan="100%">
        <font class="campoSmall">
        <%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getMotivoModifica(),"&nbsp;")%>
        </font>
      </td>
    </tr>
    <% } // end while su iterator %>
    
    <% if (ListaMisureSicurezza.size()==0){ %>
    <tr>
      <td>Nessun dato presente</td>
    </tr>
    <% } %>
    
    <% if (IstruttoriaCumulo.getFlagStato().equals("A")) { %>
    <tr>
      <td>
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Inserisci nuova Misura" onClick="javascript:nuovaAnnotazione();">
      </td>      
    </tr>
    <% } %>
  </table>
</FORM>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.modulocumulo.model.MisuraCautelareCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraCautelareCumulo"%>

<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="ListaMisureCautelari"     scope="request" class="java.util.Vector"/>

<%
//==============================================================================
// Form per la visualizzazione delle Misure Cautelari legate a un certo Titolo.
//
// La form presenta un elenco delle Misura già presenti con la possibilità di 
// modificarle, cancellarle o inserirne delle nuove 
//
//==============================================================================
%>

<html>
<head>
  <title> Elenco Misure Cautelari</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript">
    //==========================================================================
    // Visualizza, nasconde il record con il dettaglio dell'istruttoria
    //==========================================================================
    function visualizzaNota(idRecord)
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
        lAzione = "siap.siep.modulocumulo.action.ActLoadDettaglioMisuraCautelareCumulo";
        document.ListaMisureCautelariCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ID_MISURA_CAUTELARE_CUMULO%>.value = aIdMisura;
        document.ListaMisureCautelariCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ListaMisureCautelariCumulo.submit();
      }
      else if (aTipoAzione=='Modifica'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciMisuraCautelareCumulo";
        document.ListaMisureCautelariCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ID_MISURA_CAUTELARE_CUMULO%>.value = aIdMisura;
        document.ListaMisureCautelariCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ListaMisureCautelariCumulo.modalita.value = "M";
        document.ListaMisureCautelariCumulo.submit();
      }
      else if (aTipoAzione=='Cancella'){
        if (aStato=='E' || aStato=='M'){
          // Cancellazione Logica Richiedo Motivazione
          lAzione = "siap.siep.modulocumulo.action.ActInserisciMisuraCautelareCumulo";
          document.ListaMisureCautelariCumulo.modalita.value = "C";
          document.ListaMisureCautelariCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.ListaMisureCautelariCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ID_MISURA_CAUTELARE_CUMULO%>.value = aIdMisura;
          document.ListaMisureCautelariCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_FLAG_STATO%>.value = aStato;
          var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                     + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"ListaMisureCautelariCumulo"
                                     + "&" + "<%=ICostantiMisuraCautelareCumulo.CAMPO_MOTIVO_MODIFICA%>" + "="+aMotivoModifica
                                     , "CancellaDatoAnalitico","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
          window.parent.close();
          
          // N.B. la submit viene effettuare direttamnete dalla finestra di popup
        }
        else if (aStato=='I'){
          // Cancellazione fisica richiedo conferma
          var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
          if (window.confirm(msgConfirm)) {
            lAzione = "siap.siep.modulocumulo.action.ActInserisciMisuraCautelareCumulo";
            document.ListaMisureCautelariCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
            document.ListaMisureCautelariCumulo.modalita.value = "C";

            document.ListaMisureCautelariCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ID_MISURA_CAUTELARE_CUMULO%>.value = aIdMisura;
            document.ListaMisureCautelariCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_FLAG_STATO%>.value = aStato;

            document.ListaMisureCautelariCumulo.submit();
          }
        }
      }
    }
    
    //==========================================================================
    // Richiama la funzione di inserimento
    //==========================================================================
    function nuovaAnnotazione(){
      lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciMisuraCautelareCumulo";
      document.ListaMisureCautelariCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.ListaMisureCautelariCumulo.modalita.value = "I";
      document.ListaMisureCautelariCumulo.submit();
    }
    
    //==========================================================================
    // Ritorna alla Griglia dei dati analitici
    //==========================================================================
    function tornaIndietro(action)
    {
      document.ListaMisureCautelariCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.ListaMisureCautelariCumulo.submit();
    }
    
    function blink(selector){
      $(selector).fadeOut('slow', function(){
        $(this).fadeIn('slow', function(){
          blink(this);
        });
      });
    }
    
    $(document).ready(function(){
      blink ('[inCorso="S"]');
    });

  </script>
  
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco Misure Cautelari &nbsp;</font>
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

<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="ListaMisureCautelariCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">


  <!-- Campi valorizzati dinamicamente dalla eseguiAzione() -->
  <input type="hidden" name="<%=ICostantiMisuraCautelareCumulo.CAMPO_ID_MISURA_CAUTELARE_CUMULO%>" value="">
  <input type="hidden" name="<%=ICostantiMisuraCautelareCumulo.CAMPO_MOTIVO_MODIFICA%>"            value="">
  <input type="hidden" name="<%=ICostantiMisuraCautelareCumulo.CAMPO_FLAG_STATO%>"                 value="">

  <input type="hidden" name="modalita" value="">

  <%
  //============================================================================
  // Lista da caricare con i dati reali  
  //============================================================================
  %>
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int">Tipo Misura</td>
      <td class="int" nowrap>Periodo dal</td>
      <td class="int" nowrap>Periodo al</td>
      <td class="int">Anni</td>
      <td class="int">Mesi</td>
      <td class="int">Giorni</td>
      <td class="int">Tot Giorni</td>
      <td class="int" title="Indica la natura dei dati rispetto a quelli importati">Stato</td>
      <td class="int">Note</td>
      <td class="int">Azioni</td>
    </tr>
    
    <%
    Date lDataFinePrecedente = null;
    boolean isSovrapposti = false;
    
    int id_record = 0;
    
    Iterator lIteratorMisure = ListaMisureCautelari.iterator();
    while (lIteratorMisure.hasNext())
    {
      id_record = id_record +1;
      
      String lFontColorDataInizio = "";
      String lFontColorDataFine = "";
      
      MisuraCautelareCumuloModel lMisuraCautelare = (MisuraCautelareCumuloModel) lIteratorMisure.next();
      
      String lStato = "";
      String lDescStato = "";
      String lFontColor = "";
      
      if      ( lMisuraCautelare.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
      else if ( lMisuraCautelare.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
      else if ( lMisuraCautelare.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
      else if ( lMisuraCautelare.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}
      
      String lFontStyle = ""; 
      if ( lMisuraCautelare.getIsInContinuazione() )
        lFontStyle = "style=\"font-size=10px\"";
      
      if (lMisuraCautelare.getDataFine()==null) {
        lMisuraCautelare.setNumAnni(null);
        lMisuraCautelare.setNumMesi(null);
        lMisuraCautelare.setNumGiorni(null);
      }
      
      // Evidenziazione periodi sovrapposti
      if (!lMisuraCautelare.getIsInContinuazione())
      {
        if (   lDataFinePrecedente!=null 
            && DateUtils.isLower(lMisuraCautelare.getDataInizio(), lDataFinePrecedente)
           )
        {
          isSovrapposti = true;
          lFontColorDataInizio="style=\"color:red\"";
        }
        
        // Cerco il primo periodo successivo non in continuazione
        MisuraCautelareCumuloModel lMisuraCautelareSucc = null;
        for (int j = id_record; j< ListaMisureCautelari.size(); j++){
          MisuraCautelareCumuloModel lMisuraCautelareAppo = (MisuraCautelareCumuloModel) ListaMisureCautelari.elementAt(j);
          if (!lMisuraCautelareAppo.getIsInContinuazione()){
            lMisuraCautelareSucc = lMisuraCautelareAppo;
            break;
          }          
        }
        
        if (lMisuraCautelareSucc!=null){
          if (   lMisuraCautelare.getDataFine()!=null
              && DateUtils.isGreater (lMisuraCautelare.getDataFine(), lMisuraCautelareSucc.getDataInizio())
             )
          {
            isSovrapposti = true;
            lFontColorDataFine="style=\"color:red\"";
          }
  
          if (lMisuraCautelare.getDataFine()==null) 
          {
            // La MC corrente non ha data fine quindi è in corso di espiazione,
            // ma è presente un periodo successivo
            isSovrapposti = true;
            lFontColorDataInizio="style=\"color:red\"";
          }        
        }
          
        lDataFinePrecedente = lMisuraCautelare.getDataFine();
      }
      
      %>
    <tr>
      <td class="l" <%=lFontColor%> <%=lFontStyle%> >&nbsp;
        <% if (lMisuraCautelare.getIsInContinuazione()) {%>&nbsp;&nbsp;&nbsp;<% } %>
        <%=StringUtils.toStringJSP(lMisuraCautelare.getDescrTipoMisura(),"&nbsp;")%>
        <% if (lMisuraCautelare.getIsInContinuazione()) {%>*<% } %>
      </td>
      <td class="r" <%=lFontColorDataInizio%> <%=lFontStyle%>>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisuraCautelare.getDataInizio(),"dd-MM-yyyy"))%></td>
      <% if (lMisuraCautelare.getDataFine()!=null) { %>
      <td class="r" <%=lFontColorDataFine%> <%=lFontStyle%>>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisuraCautelare.getDataFine(),"dd-MM-yyyy"))%></td>
      <% } else if (!"C".equals(lMisuraCautelare.getFlagStato())){ %>
      <td class="l" style="color:red" <%=lFontStyle%> ><font style="color:red">in corso...</font></td>
      <% } else { %>
      <td class="l">&nbsp;</td>
      <% } %>
      
      <% if (lMisuraCautelare.getIsAggregato() || lMisuraCautelare.getIsInContinuazione()) {%>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelare.getNumAnniContinuativi(),"")%></td>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelare.getNumMesiContinuativi(),"")%></td>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelare.getNumGiorniContinuativi(),"")%></td>
      <% } else { %>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelare.getNumAnni(),"&nbsp;")%></td>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelare.getNumMesi(),"&nbsp;")%></td>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelare.getNumGiorni(),"&nbsp;")%></td>
      <% } %>
      
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelare.getGiorni(),"&nbsp;")%></td>
      
      <td class="c" title="<%=lDescStato%>">&nbsp;<%=lStato%></td>
      <td class="c">
        <% if (lMisuraCautelare.getMotivoModifica()!=null && lMisuraCautelare.getMotivoModifica().length()>0) {%>
        <a href="javascript:visualizzaNota('rec_<%=id_record%>')" title="Note Misura">
          note
        </a>
        <% } else {%>
        &nbsp;
        <% } %>
      </td>
      <td class="c" style="text-align:center">
        
      <%
      //======================================================================
      // Azioni possibili: Cancellazione/Annullamento, Modifica, Dettaglio
      //======================================================================
      if ( ! lMisuraCautelare.getIsAggregato()) 
      {
      %>
        <a href="javascript:eseguiAzione('Dettaglio',<%=lMisuraCautelare.getIdMisuraCautelareCumulo() %> )">
          <img src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" width="12" height="12" alt="Dettaglio Misura" border="0"></a>
        
        <% 
        //======================================================================
        // Modifiche consentite solo ad istruttoria aperta e su dati non Cancellati
        //======================================================================
        if(IstruttoriaCumulo.getFlagStato().equals("A") && !lMisuraCautelare.getFlagStato().equals("C")){ %>
        <a href="javascript:eseguiAzione('Modifica',<%=lMisuraCautelare.getIdMisuraCautelareCumulo() %>)">
          <img src="<%=IWebConstants.IMAGES_DIR%>modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
        <a href="javascript:eseguiAzione('Cancella',<%=lMisuraCautelare.getIdMisuraCautelareCumulo() %>,'<%=lMisuraCautelare.getFlagStato() %>','<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lMisuraCautelare.getMotivoModifica()),"") %>')">
          <img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
        <% } %>
      <% } else { %>
     &nbsp;
      <% } %>
      </td>
    </tr>
    <!-- Record Hidden con le note -->
    <tr style="display:none" id="rec_<%=id_record%>">
      <td class="l" colspan="100%">
        <font class="campoSmall">
        <%=StringUtils.toStringJSP(lMisuraCautelare.getMotivoModifica(),"&nbsp;")%>
        </font>
      </td>
    </tr>

      
      <%
    } // end while
    
    %>
    
    <tr>
      <td>
        (*) Periodi continuativi
      </td>
    </tr>
    <% if (isSovrapposti) {%>
    <tr>
      <td>
        <font class="cRosso">Attenzione! Controllare i dati. Alcuni dei periodi inseriti risultano sovrapposti</font>
      </td>
    </tr>    
    <% } %>
   </table>
    
    
  <% if (IstruttoriaCumulo.getFlagStato().equals("A")) { %>
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <td>
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Inserisci Nuovo Periodo" onClick="javascript:nuovaAnnotazione();">
      </td>
    </tr>
  </table>
  <% } %>

</FORM>
</body>
</html>
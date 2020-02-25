<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>
<%@ page import="siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils "%>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       	scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="ListaSospMisureAlt" 	scope="request" class="java.util.Vector"/>

<%
//====================================================================================================
// Form per la visualizzazione dell'elenco di Sospensioni Misure Alternative legate a un certo Titolo.
//
// La form presenta un elenco delle Mis. Alt. già presenti con la possibilità di 
// modificarle, cancellarle o inserirne delle nuove 
//
//====================================================================================================
%>

<html>
<head>
  <title> Elenco Provvedimenti di Sospensione Misura Alternativa </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
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
    function eseguiAzione(aTipoAzione, aIdStat, aIdComp, aStato, aMotivoModifica)
    {
      if (aTipoAzione=='Dettaglio'){
        lAzione = "siap.siep.modulocumulo.action.ActDettaglioSospMisuraAlternativaCumulo";
        document.ListaSospMisureAlt.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>.value = aIdStat;
        document.ListaSospMisureAlt.<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>.value = aIdComp;
        document.ListaSospMisureAlt.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ListaSospMisureAlt.submit();
      }
      else if (aTipoAzione=='Modifica'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciSospMisuraAlternativaCumulo";
        document.ListaSospMisureAlt.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>.value = aIdStat;
        document.ListaSospMisureAlt.<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>.value = aIdComp;
        document.ListaSospMisureAlt.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ListaSospMisureAlt.modalita.value = 'M';
        
        document.ListaSospMisureAlt.submit();
      }
      else if (aTipoAzione=='Cancella'){
        // Cancellazione fisica richiedo conferma
        var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
        if (window.confirm(msgConfirm)) {
          lAzione = "siap.siep.modulocumulo.action.ActInserisciSospMisuraAlternativaCumulo";
          document.ListaSospMisureAlt.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;

          document.ListaSospMisureAlt.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>.value = aIdStat;
          document.ListaSospMisureAlt.<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>.value = aIdComp;
          document.ListaSospMisureAlt.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO%>.value = aStato;
          document.ListaSospMisureAlt.modalita.value = 'C';


          document.ListaSospMisureAlt.submit();
        }
      }
    }
    
    //==========================================================================
    // Richiama la funzione di inserimento
    //==========================================================================
    function nuovoProvvedimento(){
      lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciSospMisuraAlternativaCumulo";
      document.ListaSospMisureAlt.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.ListaSospMisureAlt.modalita.value = 'I';
      document.ListaSospMisureAlt.submit();
    }
    
    //==========================================================================
    // Ritorna alla Griglia dei dati analitici
    //==========================================================================
    function tornaIndietro(action)
    {
      document.ListaSospMisureAlt.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.ListaSospMisureAlt.submit();
    }

  </script>
  
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco Provvedimenti di Sospensione Misura Alternativa &nbsp;</font>
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
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
      </td>
    </tr>
  </table>

<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="ListaSospMisureAlt">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">


  <!-- Campi valorizzati dinamicamente dalla eseguiAzione() -->
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>" value="">
  <input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>" value="">
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>"              value="">
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO%>"                   value="">
  
  <input type="hidden" name="modalita" value="">

  <%
  //============================================================================
  // Lista da caricare con i dati reali  
  //============================================================================
  %>
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int">Provvedimento</td>
      <td class="int">Emesso in Data</td>
      <td class="int">Autorità Emittente</td>

      <td class="int" title="Indica la natura dei dati rispetto a quelli importati">Stato</td>
      <!--td class="int">Note</td-->
      <td class="int">Azioni</td>
    </tr>

    <%
      int id_record = 0;

      Iterator itx = ListaSospMisureAlt.iterator();
      while ( itx.hasNext()) {
        id_record = id_record +1;
        StatoEsecTitoloCumulatoModel lStatoEsec = (StatoEsecTitoloCumulatoModel)itx.next();
        
        String lStato = "";
        String lDescStato = "";
        String lFontColor = "";
        
        if      ( lStatoEsec.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
        else if ( lStatoEsec.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
        else if ( lStatoEsec.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
        else if ( lStatoEsec.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}
        
        Vector <ComputiCumuloModel> lListaComputi = lStatoEsec.getListaComputi();
        int contaComputi = 0;
        
        Iterator itxComputi = lListaComputi.iterator();
        while ( itxComputi.hasNext()) 
        {
          contaComputi++;
          ComputiCumuloModel lComputo = (ComputiCumuloModel) itxComputi.next();
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class="l" <%=lFontColor%> >
        &nbsp;<%=StringUtils.toStringJSP(lStatoEsec.getDescrTipoProvvedimento(),"&nbsp;")%>
        <% if (StatoEsecuzioneCumuloUtils.isSospensioneC5Istanza  (lStatoEsec.getCodMotivo())) {%>
        &nbsp;<%=StringUtils.toStringJSP(lStatoEsec.getDescrContenutoIstanza(),"&nbsp;")%>
        <% } else { %>
        &nbsp;<%=StringUtils.toStringJSP(lStatoEsec.getDescrMotivo(),"&nbsp;")%>
        <% } %>
      </td>
      <td class="c" <%=lFontColor%> >
        &nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lStatoEsec.getDataEmissione(),"dd-MM-yyyy"))%>
      </td>
      <td class="c" <%=lFontColor%> >
        &nbsp;<%=StringUtils.toStringJSP(lStatoEsec.getDescrUfficioEmittente())+ " " + StringUtils.toStringJSP(lStatoEsec.getDescrLuogoEmittente())%>&nbsp;
      </td>
      <td class="c" title="<%=lDescStato%>">&nbsp;<%=lStato%></td>
      <!--
      <td class="c">
        <% if (lStatoEsec.getMotivoModifica()!=null && lStatoEsec.getMotivoModifica().length()>0) {%>
        <a href="javascript:visualizzaNota('rec_<%=id_record%>')" title="Note Misura">
          note
        </a>
        <% } else {%>
        &nbsp;
        <% } %>
      </td>
      -->
      
      <td class="c" style="text-align:center"> &nbsp;
      <%
        //======================================================================
        // Azioni possibili: Cancellazione/Annullamento, Modifica, Dettaglio
       //======================================================================
      %>
        <a href="javascript:eseguiAzione('Dettaglio',<%=lStatoEsec.getIdStatoEsecTitoloCumulato() %> )">
          <img src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" width="12" height="12" alt="Dettaglio" border="0"></a>
        
        <% 
        //=========================================================================
        // Modifiche consentite solo ad istruttoria aperta e su dati non Cancellati
        //=========================================================================
        if(IstruttoriaCumulo.getFlagStato().equals("A") && !lStatoEsec.getFlagStato().equals("C")){ %>
        <a href="javascript:eseguiAzione('Modifica',<%=lStatoEsec.getIdStatoEsecTitoloCumulato() %>,<%=lComputo.getIdComputiCumulo() %>)">
          <img src="<%=IWebConstants.IMAGES_DIR%>modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
        <a href="javascript:eseguiAzione('Cancella',<%=lStatoEsec.getIdStatoEsecTitoloCumulato() %>,<%=lComputo.getIdComputiCumulo() %>,'<%=lStatoEsec.getFlagStato() %>','<%=StringUtils.toStringJSP(lStatoEsec.getMotivoModifica(),"") %>')">
          <img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
        <% } %>
      </td>
    </tr>
    
    <!-- Record Hidden con le note -->
    <tr style="display:none" id="rec_<%=id_record%>">
      <td class="l" colspan="100%">
        <font class="campoSmall">
        <%=StringUtils.toStringJSP(lStatoEsec.getMotivoModifica(),"&nbsp;")%>
        </font>
      </td>
    </tr>
    <% } } // end while su iterator %>
    
    <% if (ListaSospMisureAlt.size()==0){ %>
    <tr>
      <td>Nessun provvedimento presente</td>
    </tr>
    <% } %>
    
    <% if (IstruttoriaCumulo.getFlagStato().equals("A")) { %>
    <tr>
      <td>
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Inserisci nuovo Provvedimento" onClick="javascript:nuovoProvvedimento();">
      </td>
    </tr>
    <% } %>
  </table>
</FORM>
</body>
</html>
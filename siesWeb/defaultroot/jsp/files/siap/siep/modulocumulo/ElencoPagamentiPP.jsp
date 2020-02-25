<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="ListaPagamentiPP"     scope="request" class="java.util.Vector"/>

<%
//==============================================================================
// Form per la visualizzazione dei Provvedimenti di Annotazione Pagamento Pena 
// Pecuniaria legati a un certo Titolo
//
// La form presenta un elenco dei Provvedimenti già presenti con la possibilità di 
// modificarli, cancellarli o inserirne dei nuovi 
//==============================================================================
%>

<html>
<head>
  <title> Elenco Pagamenti Pena Pecuniaria</title>
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
        lAzione = "siap.siep.modulocumulo.action.ActDettaglioPagamentoPP";
        document.ListaPagamentiPP.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>.value = aIdStat;
        document.ListaPagamentiPP.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ListaPagamentiPP.submit();
      }
      else if (aTipoAzione=='Modifica'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciPagamentoPP";
        document.ListaPagamentiPP.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>.value = aIdStat;
        document.ListaPagamentiPP.<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>.value = aIdComp;
        document.ListaPagamentiPP.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ListaPagamentiPP.modalita.value='M';
        document.ListaPagamentiPP.submit();
	
      }
      else if (aTipoAzione=='Cancella'){
        // Cancellazione fisica richiedo conferma
        var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
        if (window.confirm(msgConfirm)) {
          lAzione = "siap.siep.modulocumulo.action.ActInserisciPagamentoPP";
          document.ListaPagamentiPP.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;

          document.ListaPagamentiPP.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>.value = aIdStat;
          document.ListaPagamentiPP.<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>.value = aIdComp;
          document.ListaPagamentiPP.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO%>.value = aStato;
          document.ListaPagamentiPP.modalita.value = "C";

          document.ListaPagamentiPP.submit();
        }
      }
    }
    
    //==========================================================================
    // Richiama la funzione di inserimento
    //==========================================================================
    function nuovaAnnotazione(){
      lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciPagamentoPP";
      document.ListaPagamentiPP.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.ListaPagamentiPP.modalita.value = "I";
      document.ListaPagamentiPP.submit();
    }
    
    //==========================================================================
    // Ritorna alla Griglia dei dati analitici
    //==========================================================================
    function tornaIndietro(action)
    {
      document.ListaPagamentiPP.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.ListaPagamentiPP.submit();
    }

  </script>
  
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco Pagamenti Pena Pecuniaria &nbsp;</font>
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

<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="ListaPagamentiPP">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"      value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

 
  <!-- Campi valorizzati dinamicamente dalla eseguiAzione() -->
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>" value="">
  <input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>" value="">
  
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>"            value="">
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO%>"                 value="">

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
      <td class="int">Emesso in data</td>
      <td class="int">Multa </td>
      <td class="int">Ammenda </td>
      <td class="int" title="Indica la natura dei dati rispetto a quelli importati">Stato</td>
      <td class="int">Azioni</td>
    </tr>

    <%
      int id_record = 0;

      Iterator itx = ListaPagamentiPP.iterator();
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
      <% if (contaComputi==1) {%>
	      <td class="l"><%=StringUtils.toStringJSP(lStatoEsec.getDescrTipoProvvedimento())+" "+StringUtils.toStringJSP(lStatoEsec.getDescrMotivo())%></td>
	      <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lStatoEsec.getDataEmissione(),"dd-MM-yyyy"))%></td>
      <% } else {%>
	        <td class="c"></td>
	        <td class="c"></td>
      <% } %>
      <td class="r"><%=lComputo.getImportoMulta()!=null?StringUtils.toEuroFormat(lComputo.getImportoMulta()):""%>&nbsp;</td>
      <td class="r"><%=lComputo.getImportoAmmenda()!=null?StringUtils.toEuroFormat(lComputo.getImportoAmmenda()):""%>&nbsp;</td>
      <td class="c" title="<%=lDescStato%>">&nbsp;<%=lStato%></td>

      <td class="c" style="text-align:center"> &nbsp;
      <%
        //======================================================================
        // Azioni possibili: Cancellazione/Annullamento, Modifica, Dettaglio
        //======================================================================
      %>
        <a href="javascript:eseguiAzione('Dettaglio',<%=lStatoEsec.getIdStatoEsecTitoloCumulato() %> )">
          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Misura" border="0"></a>
        
        <% 
        //=========================================================================
        // Modifiche consentite solo ad istruttoria aperta e su dati non Cancellati
        //=========================================================================
        if(IstruttoriaCumulo.getFlagStato().equals("A") && !lStatoEsec.getFlagStato().equals("C")){ %>
        <a href="javascript:eseguiAzione('Modifica',<%=lStatoEsec.getIdStatoEsecTitoloCumulato() %>,<%=lComputo.getIdComputiCumulo() %>)">
          <img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
        <a href="javascript:eseguiAzione('Cancella',<%=lStatoEsec.getIdStatoEsecTitoloCumulato() %>,<%=lComputo.getIdComputiCumulo() %>,'<%=lStatoEsec.getFlagStato() %>','<%=StringUtils.toStringJSP(lStatoEsec.getMotivoModifica(),"") %>')">
          <img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
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
    <% } // end while su iterator sui Computi%>
    <% } // end while su iterator sugli eventi%>
    
    <% if (ListaPagamentiPP.size()==0){ %>
    <tr>
      <td>Nessuna annotazione pagamento presente</td>
    </tr>
    <% } %>
    
    <% if (IstruttoriaCumulo.getFlagStato().equals("A")) { %>
    <tr>
      <td>
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Inserisci nuovo Provvedimento" onClick="javascript:nuovaAnnotazione();">
      </td>
    </tr>
    <% } %>
  </table>
</FORM>
</body>
</html>
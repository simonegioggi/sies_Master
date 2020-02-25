<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPosizioneGiuridicaCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>
<%@ page import="siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils"%>

<jsp:useBean id="IstruttoriaCumulo"		scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"		scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="ListaEspiati"			scope="request" class="java.util.Vector"/>
<jsp:useBean id="ListaEspPregressa"		scope="request" class="java.util.Vector"/>

<%
//==============================================================================
// Form per la visualizzazione dell'elenco dei periodi sofferti
//
// La form presenta un elenco dei Decreti già presenti con la possibilità di 
// modificarle, cancellarle o inserirne delle nuove 
//
//==============================================================================
%>

<html>
<head>
  <title> Elenco Espiato Cumulo</title>
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
    // Richiama l'opportuna azione (Espiazione Attuale)
    //==========================================================================
    function eseguiAzioneEA(aTipoAzione, aId, aIdTitolo, aStato)
    {
      if (aTipoAzione=='Dettaglio'){
        lAzione = "siap.siep.modulocumulo.action.ActDettaglioEspiazioneAttuale";
        document.formName.<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ID_POSIZIONE_GIURIDICA_CUM%>.value = aId;
        document.formName.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value = aIdTitolo;
        document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formName.submit();
      }
      else if (aTipoAzione=='Modifica'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInsEspiazioneAttuale";
        document.formName.<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ID_POSIZIONE_GIURIDICA_CUM%>.value = aId;
        document.formName.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value = aIdTitolo;
        document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formName.modalita.value = 'M';
        
        document.formName.submit();
      }
      else if (aTipoAzione=='Cancella'){
        // Cancellazione fisica richiedo conferma
        var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
        if (window.confirm(msgConfirm)) {
          lAzione = "siap.siep.modulocumulo.action.ActInserisciEspiazioneAttuale";
          document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.formName.<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ID_POSIZIONE_GIURIDICA_CUM%>.value = aId;
          document.formName.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value = aIdTitolo;
          document.formName.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO%>.value = aStato;
          document.formName.modalita.value = 'C';
          document.formName.submit();
        }
      }
    }

    //==========================================================================
    // Richiama l'opportuna azione (Espiazione Pregressa)
    //==========================================================================
    function eseguiAzioneEP(aTipoAzione, aIdStat, aIdComp, aStato)
    {
        if (aTipoAzione=='Dettaglio'){
            lAzione = "siap.siep.modulocumulo.action.ActDettaglioEspiazionePregressa";
            document.formName.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>.value = aIdStat;
            document.formName.<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>.value = aIdComp;
            document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
            document.formName.submit();
        } else if (aTipoAzione=='Modifica'){
            lAzione = "siap.siep.modulocumulo.action.ActLoadInsEspiazionePregressa";
            document.formName.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>.value = aIdStat;
            document.formName.<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>.value = aIdComp;
            document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
            document.formName.modalita.value = 'M';
            document.formName.submit();
        } else if (aTipoAzione=='Cancella'){
          	// Cancellazione fisica richiedo conferma
          	var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
          	if (window.confirm(msgConfirm)) {
	  			lAzione = "siap.siep.modulocumulo.action.ActInserisciEspiazionePregressa";
            	document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;

            	document.formName.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>.value = aIdStat;
            	document.formName.<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>.value = aIdComp;
            	document.formName.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO%>.value = aStato;
            	document.formName.modalita.value = 'C';
            	document.formName.submit();
          	}
        }
    }
    
    //==========================================================================
    // Richiama la funzione di inserimento
    //==========================================================================
    function nuovaAnnotazione(tipo){
      if (tipo=="attuale")
        lAzione = "siap.siep.modulocumulo.action.ActLoadInsEspiazioneAttuale";
      else 
        lAzione = "siap.siep.modulocumulo.action.ActLoadInsEspiazionePregressa";
              
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.formName.modalita.value = 'I';
      document.formName.submit();
    }
    
    //==========================================================================
    // Ritorna alla Griglia dei dati analitici
    //==========================================================================
    function tornaIndietro(action)
    {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formName.submit();
    }

  </script>
  
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco Espiazioni attuali e pregresse &nbsp;</font>
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

<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="formName">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">


  <!-- Campi valorizzati dinamicamente dalle eseguiAzione(EA & EP) -->
  <input type="hidden" name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ID_POSIZIONE_GIURIDICA_CUM%>" 	value="">
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>" value="">
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>"            	value="">
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO%>"                 	value="">
  <input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>" 						value="">
  
  <input type="hidden" name="modalita" value="">


  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <td class="titolo" colspan="3">Espiazione Attuale</td>
    </tr>
    <tr>
      <td class="int">Tipologia</td>
      <td class="int" width="100px">Dal</td>
      <td class="int" width="100px">Azioni</td>
    </tr>

    <%
      Iterator itx = ListaEspiati.iterator();
      while ( itx.hasNext()) {
        PosizioneGiuridicaCumuloModel lPosGiuCumulo = (PosizioneGiuridicaCumuloModel)itx.next();
		
        //String lStato = "";
        //String lDescStato = "";
        String lFontColor = "";
        
        //if      ( lPosGiuCumulo.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
        //else if ( lPosGiuCumulo.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
        //else if ( lPosGiuCumulo.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
        //else if ( lPosGiuCumulo.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}
        
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lPosGiuCumulo.getDescrPosizioneGiuridica(),"&nbsp;")%></td>
      <td class="c" <%=lFontColor%> nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosGiuCumulo.getDataInizio(),"dd-MM-yyyy"),"-") %> </td>
      <td class="c" style="text-align:center" nowrap> &nbsp;

        <a href="javascript:eseguiAzioneEA('Dettaglio',<%=lPosGiuCumulo.getIdPosizioneGiuridicaCum() %>,<%=lPosGiuCumulo.getTitIdTitoloCumulato() %> )">
          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Misura" border="0"></a>
        <% 
        //======================================================================
        // Modifiche consentite solo ad istruttoria aperta e su dati non Cancellati
        //======================================================================
        if(IstruttoriaCumulo.getFlagStato().equals("A") ){ %>
        <a href="javascript:eseguiAzioneEA('Modifica',<%=lPosGiuCumulo.getIdPosizioneGiuridicaCum() %>,<%=lPosGiuCumulo.getTitIdTitoloCumulato() %> )">
          <img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
        <a href="javascript:eseguiAzioneEA('Cancella',<%=lPosGiuCumulo.getIdPosizioneGiuridicaCum() %>, <%=lPosGiuCumulo.getTitIdTitoloCumulato() %> )">
          <img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
        <% } %>
      </td>
    </tr>
    <% } // end while su iterator %>
    
    <% if (ListaEspiati.size()==0) { %>
    <tr>
      <td>
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Espiazione Attuale" onClick="javascript:nuovaAnnotazione('attuale');">
      </td>
    </tr>
    <% } %>    
  </table>
  
  <br>

  <%
  //============================================================================
  // Lista da caricare con i dati reali  
  //============================================================================
  %>
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <td class="titolo" colspan="9">Espiazione Pregressa</td>
    </tr>
    <tr>
      <td class="int">Provvedimento</td>
      <td class="int">Emesso in Data</td>
      <td class="int">Periodo dal</td>
      <td class="int">Periodo al</td>
      <td class="int">Anni</td>
      <td class="int">Mesi</td>
      <td class="int">Giorni</td>
      
      <td class="int" title="Indica la natura dei dati rispetto a quelli importati">Stato</td>
      <!--td class="int">Note</td-->
      <td class="int">Azioni</td>
    </tr>


    
    <%
      int id_record = 0;

      Iterator itx1 = ListaEspPregressa.iterator();
      while ( itx1.hasNext()) {
        id_record = id_record +1;
        StatoEsecTitoloCumulatoModel lStatoEsec = (StatoEsecTitoloCumulatoModel)itx1.next();
        
        String lStato = "";
        String lDescStato = "";
        String lFontColor = "";

        if      ( lStatoEsec.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
        else if ( lStatoEsec.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
        else if ( lStatoEsec.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
        else if ( lStatoEsec.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}
        
        Vector <ComputiCumuloModel> lListaComputi = lStatoEsec.getListaComputi();
        int contaComputi = 0;
        
if (lListaComputi!=null) { 
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
		      <td class="c" <%=lFontColor%> nowrap>
		        &nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lStatoEsec.getDataEmissione(),"dd-MM-yyyy"))%>
		      </td>
		      <td class="c" <%=lFontColor%> nowrap>
		        &nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataReclusioneDa(),"dd-MM-yyyy"))%>
		      </td>
		      <td class="c" <%=lFontColor%> nowrap>
		        &nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataReclusioneA(),"dd-MM-yyyy"))%>
		      </td>
		      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lComputo.getNumAnniReclusione(),"&nbsp;")%></td>
		      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lComputo.getNumMesiReclusione(),"&nbsp;")%></td>
		      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lComputo.getNumGiorniReclusione(),"&nbsp;")%></td>
		
		      <td class="c" title="<%=lDescStato%>">&nbsp;<%=lStato%></td>
		      <td class="c" style="text-align:center" nowrap> &nbsp;
		      <%
		      //======================================================================
		      // Azioni possibili: Cancellazione, Modifica, Dettaglio
		      //======================================================================
		      %>
		        <a href="javascript:eseguiAzioneEP('Dettaglio',<%=lStatoEsec.getIdStatoEsecTitoloCumulato() %> )">
		          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Espiazione Pregressa" border="0"></a>
		        <a href="javascript:eseguiAzioneEP('Modifica',<%=lStatoEsec.getIdStatoEsecTitoloCumulato() %>,<%=lComputo.getIdComputiCumulo() %> )">
		          <img src="<%=IWebConstants.IMAGES_DIR%>modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
		        <a href="javascript:eseguiAzioneEP('Cancella','<%=lStatoEsec.getIdStatoEsecTitoloCumulato() %>','<%=lComputo.getIdComputiCumulo()%>','<%=lStatoEsec.getFlagStato() %>','<%=StringUtils.toStringJSP(lStatoEsec.getMotivoModifica() ) %>' )">
		          <img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
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
	  <% } // end while su iterator lListaComputi %>
	  
<% } // end if computi presenti %>
	  
	<% } // end while su iterator ListaEspPregressa %>
    
    <% if (ListaEspPregressa.size()==0){ %>
    <tr>
      <td>Nessun dato presente</td>
    </tr>
    <% } %>
    
    <% if (IstruttoriaCumulo.getFlagStato().equals("A")) { %>
    <tr>
      <td>
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Espiazione Pregressa" onClick="javascript:nuovaAnnotazione('pregressa');">
      </td>
    </tr>
    <% } %>
  </table>
</FORM>
</body>
</html>
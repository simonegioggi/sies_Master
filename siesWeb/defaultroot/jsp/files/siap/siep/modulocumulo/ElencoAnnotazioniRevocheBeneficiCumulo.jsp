<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"     	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"        	scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="ListaRevocheSospCond" 		scope="request" class="java.util.Vector"/>
<jsp:useBean id="lTipopresenzaBenefici" 	scope="request" class="java.lang.String"/>

<%
//=============================================================================================
// Form per la visualizzazione delle Annotazioni di Revoca Beneficio legate a un certo Titolo.
// ( i Benefici in questione sono: SOSPENSIONE CONDIZIONALE DELLA PENA e NON MENZIONE )
//
// La form presenta un elenco dei Provvedimenti già presenti con la possibilità 
// di modifica, cancellazione o inserimento di nuovi.
//=============================================================================================
%>

<html>
<head>
  <title> Elenco Provvedimenti di Interruzione</title>
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
    function eseguiAzione(aTipoAzione, aIdStat, aStato)
    {
      if (aTipoAzione=='Dettaglio'){
        lAzione = "siap.siep.modulocumulo.action.ActDettaglioAnnotazioneRevocaBeneficioCumulo";
        document.ListaRevocheSosp.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>.value = aIdStat;
        document.ListaRevocheSosp.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ListaRevocheSosp.submit();
      }
      else if (aTipoAzione=='Modifica'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciAnnotazioneRevocaBeneficioCumulo";
        document.ListaRevocheSosp.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>.value = aIdStat;
        document.ListaRevocheSosp.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ListaRevocheSosp.modalita.value='M';
        document.ListaRevocheSosp.submit();
      }
      else if (aTipoAzione=='Cancella'){
        // Cancellazione fisica richiedo conferma
        var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
        if (window.confirm(msgConfirm)) {
          lAzione = "siap.siep.modulocumulo.action.ActInserisciAnnotazioneRevocaBeneficioCumulo";
          document.ListaRevocheSosp.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;

          document.ListaRevocheSosp.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>.value = aIdStat;
          document.ListaRevocheSosp.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO%>.value = aStato;
          document.ListaRevocheSosp.modalita.value = "C";

          document.ListaRevocheSosp.submit();
        }
      }
    }
    
    //==========================================================================
    // Richiama la funzione di inserimento
    //==========================================================================
    function nuovaAnnotazione(){
   	  
   	  if('<%=lTipopresenzaBenefici%>' != "") {
   	 
      	lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciAnnotazioneRevocaBeneficioCumulo";
      	document.ListaRevocheSosp.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      	document.ListaRevocheSosp.modalita.value = "I";
      	document.ListaRevocheSosp.tipoSospensione.value = '<%=lTipopresenzaBenefici%>';
      	document.ListaRevocheSosp.submit();
   	  } else {
   		  alert("Nel Procedimento NON è presente alcun Beneficio di \nSospensione Condizionale della Pena  \n"+
   				"o di Non Menzione. \nImpossibile procedere con la Revoca!");
   		  return false;
   	  } 	
    }
    
    //==========================================================================
    // Ritorna alla Griglia dei dati analitici
    //==========================================================================
    function tornaIndietro(action)
    {
      document.ListaRevocheSosp.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.ListaRevocheSosp.submit();
    }

  </script>
  
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco Revoche Benefici &nbsp;</font>
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
  <table align="center" width="95%" border="0" cellspacing="1" cellpadding="1">
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

<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="ListaRevocheSosp">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">


  <!-- Campi valorizzati dinamicamente dalla eseguiAzione() -->
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>" value="">
  
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>"            value="">
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO%>"                 value="">
  
  <input type="hidden" name="tipoSospensione" >
  <input type="hidden" name="modalita" value="">
  <%
  //============================================================================
  // Lista da caricare con i dati reali  
  //============================================================================
  %>
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int">Beneficio Revocato</td>
      <td class="int">Provvedimento di Revoca<br>Emesso in data</td>
      <td class="int">Autorita Emittente</td>
      <td class="int" title="Indica la natura dei dati rispetto a quelli importati">Stato</td>
      <td class="int">Azioni</td>
    </tr>

   <%
   	  
      Iterator itx = ListaRevocheSospCond.iterator();
      while ( itx.hasNext()) {
        StatoEsecTitoloCumulatoModel lStatoEsec = (StatoEsecTitoloCumulatoModel)itx.next();
        
        String lStato = "";
        String lDescStato = "";
        String lFontColor = "";
        String lBeneRevo = "";
        
        if      ( lStatoEsec.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
        else if ( lStatoEsec.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
        else if ( lStatoEsec.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
        else if ( lStatoEsec.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}
        
        lBeneRevo = "";
        if(lStatoEsec.getFlagStato().equals("I")) {
	        if("S".equals(lStatoEsec.getFlagTipoSosp()) ) {
	      	  lBeneRevo = "Sopensione Condizionale della pena";
	        } else if("M".equals(lStatoEsec.getFlagTipoSosp()) ) {
	      	  lBeneRevo = "Non Menzione";
	        } else if("SM".equals(lStatoEsec.getFlagTipoSosp()) ) {
	      	  lBeneRevo = "Sopensione Condizionale della pena, Non Menzione";
	        }
        } else if(lStatoEsec.getFlagStato().equals("E")) {
      	  	if(lStatoEsec.getCodMotivo().equals("0818")) {
      	  		lBeneRevo = "Sopensione Condizionale della pena";
      	  	} else if(lStatoEsec.getCodMotivo().equals("0822")) {
      	  		lBeneRevo = "Non Menzione";
      	  	}  
        }

    %>
    <tr>
	  <td class="l"><%=lBeneRevo%></td>	
      <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lStatoEsec.getDataEmissione(),"dd-MM-yyyy"))%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(lStatoEsec.getDescrUfficioEmittente())+" di "+StringUtils.toStringJSP(lStatoEsec.getDescrLuogoEmittente() )%></td>
      <td class="c" title="<%=lDescStato%>">&nbsp;<%=lStato%></td>
      <td class="c" style="text-align:center"> &nbsp;
      <%
        //======================================================================
        // Azioni possibili: Cancellazione/Annullamento, Modifica, Dettaglio
        //======================================================================
      %>
        <a href="javascript:eseguiAzione('Dettaglio',<%=lStatoEsec.getIdStatoEsecTitoloCumulato() %>,'<%=lStatoEsec.getFlagStato()%>')">
          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Misura" border="0"></a>
        
        <% 
        //======================================================================
        // Modifiche consentite solo ad istruttoria aperta e su dati non Cancellati
        //======================================================================
        if(IstruttoriaCumulo.getFlagStato().equals("A") && !lStatoEsec.getFlagStato().equals("C")){ %>
        <a href="javascript:eseguiAzione('Modifica',<%=lStatoEsec.getIdStatoEsecTitoloCumulato()%>,'<%=lStatoEsec.getFlagStato()%>' )">
          <img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
        <a href="javascript:eseguiAzione('Cancella',<%=lStatoEsec.getIdStatoEsecTitoloCumulato()%>,'<%=lStatoEsec.getFlagStato()%>' )">
          <img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
        <% } %>
      </td>
    </tr>
     
    <% } // end while su iterator sugli eventi%>
    
    <% if (ListaRevocheSospCond.size()==0){ %>
    <tr>
      <td>Nessun dato presente</td>
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

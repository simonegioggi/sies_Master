<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.util.SIESSwitch"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.log.LogF3B"%>


<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<%@ page import="siap.sico.util.SICOLookupRemote"%>
<%@ page import="siap.sico.ufficio.controller.IUfficio"%>

<%@ page import="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>

<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.util.ParserMessage"%>
<%@ page import="siap.siep.fascicolo.model.DettaglioFascicoloModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>

<%@page import="org.apache.log4j.Logger"%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="ListaTitoliInIstruttoria"  scope="request" class="java.util.Vector"/>
<jsp:useBean id="ListaTitoli"       scope="request" class="java.util.Vector"/>
<jsp:useBean id="HashFascicoli"     scope="request" class="java.util.Hashtable"/>

<%
//==============================================================================
// FORM di visualizzazione dell'elenco dei fascicoli Trasmessi, 
// Presi in carico ma non ancora Inscritti in Istruttoria.
//==============================================================================

// Stabilisce la funzione da innescare dopo l'eventuale messaggio di restituzione Atti
String newPage  = (String)request.getAttribute(IWebConstants.GOTO_PAGE);

String stessoTitolo = ""; 

Vector<TitoloCumulatoModel> VecTitCum = new Vector(ListaTitoliInIstruttoria);

%>


<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  
  <script language="JavaScript">
    
    //==========================================================================
    // Richiama l'opportuna action
    //==========================================================================
    function IscriviFascicoloInIstruttoria(aIdFascicolo, aIdMessaggio, aStessoTitolo)
    {
      if (aStessoTitolo.length > 0)
	      var msgConfirm = "Attenzione! Già è presente in Istruttoria Cumulo\n il Procedimento "+aStessoTitolo+" con estremi del Titolo Esecutivo\n uguali a quelli del procedimento che si sta per prendere in carico.\n Si vuole procedere all'iscrizione in Instruttoria del Titolo selezionato?";
      else
	      var msgConfirm = "Si vuole procedere all'iscrizione in Istruttoria del Titolo selezionato?";

      if (window.confirm(msgConfirm)) {
        lAzione = "siap.siep.istruttoriacumulo.action.ActInserisciFascicoloInIstruttoria";
        document.ElencoFascicoliCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ElencoFascicoliCumulo.idFasDaCumulare.value=aIdFascicolo;
        document.ElencoFascicoliCumulo.<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>.value = aIdMessaggio;
        document.ElencoFascicoliCumulo.submit();
      }
    }
    
    function RestituisciFascicolo(aIdFascicolo, aIdMessaggio)
    {
      var msgConfirm = "Il Titolo selezionato verrà restituito all'ufficio di Origine \n che ne riprenderà la piena titolarità "; 
      if (window.confirm(msgConfirm)) {
        lAzione = "siap.siep.istruttoriacumulo.action.ActLoadRestituzioneFascicolo";
        document.ElencoFascicoliCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ElencoFascicoliCumulo.idFasDaCumulare.value=aIdFascicolo;
        document.ElencoFascicoliCumulo.<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>.value = aIdMessaggio;
        document.ElencoFascicoliCumulo.submit();
      }
    }
    
    //==========================================================================
    // Ritorna all'Elenco Titoli Coinvolti
    //==========================================================================
    function tornaIndietro(action)
    {
      document.ElencoFascicoliCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.ElencoFascicoliCumulo.submit();
    }
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Elenco Procedimenti Ricevuti per Competenza</font>
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia Della Gestione Cumulo -->
        <a href="javascript:tornaIndietro('siap.siep.istruttoriacumulo.action.ActLoadElencoFascicoliCoinvolti')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ElencoFascicoliCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=IWebConstants.GOTO_PAGE%>"  value="<%=newPage%>">
  <input type="hidden" name="idFasDaCumulare" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="">
 	
  <%
  //============================================================================
  //                   Tabella con l'elenco dei Fascicoli
  //============================================================================
  %>
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr><td colspan="100%" width="100%" class="Titolo">Procedimenti ricevuti per Competenza Non Ancora Iscritti in Istruttoria</td></tr>
    <tr><td>&nbsp;</td></tr>
    
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int">Data Trasmissione</td>
      <td class="int">Anno/Numero <br>SIEP</td>
      <td class="int">Da</td>
      <td class="int">Titolo</td><!-- Sentenza/Decreto/Cumulo-->
      <!--td class="int">N°</td-->
      <!--td class="int">Emesso in Data</td-->
      <td class="int">Autorità Emittente</td>
      <td class="int">Definitiva in data</td>
      <td class="int">Stato</td>
      <td class="int">Azioni</td>
    </tr>


    <% 
      int id_record = 0;
      Iterator itx = ListaTitoli.iterator();
      while ( itx.hasNext()) 
      {
        MessaggioModel lMessaggio = (MessaggioModel) itx.next();
//        ParserMessage lParser = null;  
//        lParser = new ParserMessage(lMessaggio.getTreeModel());
//        DettaglioFascicoloModel lDettFascModel = lParser.getDettaglioFascicoloSiep();
//        FascicoloSiepModel lFascModel = lDettFascModel.getFascicoloSiep();
        FascicoloSiepModel lFascModel = (FascicoloSiepModel) HashFascicoli.get(lMessaggio.getIdMessaggio());

        SoggettoModel      lSoggModel = lFascModel.getSoggetto();
        SentenzaModel      lSentModel = lFascModel.getSentenza();

		// 24/04/2019 MEV70 Controllo eventuale presenza del titolo esecutivo legato al Procedimento da Importare tra i Procedimenti in Cumulo.  
		stessoTitolo = ""; 
		if (VecTitCum != null) {
			for (int i=0; i<VecTitCum.size();i++){
				TitoloCumulatoModel lTitoloCumModel = (TitoloCumulatoModel) VecTitCum.elementAt(i);
	           	if (lSentModel != null &&
	           		lTitoloCumModel.isStessoTitolo(lSentModel) ) {
	                stessoTitolo = lTitoloCumModel.getProcedimentoCumulato().getChiaveAnnoFasCumulato()+"/"+lTitoloCumModel.getProcedimentoCumulato().getChiaveProgrFasCumulato();
	       			break;
	           	}
			}
		}
        String lStrFascicolo = "" ;
        
        if (lFascModel.getChiaveProgrOrig()!=null) {
        
          lStrFascicolo = lFascModel.getChiaveAnno()+"/"+lFascModel.getChiaveProgrOrig();
          
          BigDecimal lIncrement = null;
          lIncrement = lFascModel.getChiaveProgr().subtract (lFascModel.getChiaveProgrOrig());
          
          UfficioModel lUfficioAccorpato = new UfficioModel();
    
          try {
            IUfficio lUff = SICOLookupRemote.getUfficioRemote();
            lUfficioAccorpato = lUff.getUfficioAccorpatoByAccorpanteIncrement (lFascModel.getChiaveUfficio(), ""+lIncrement);
          } catch (Exception e) {}          
          
          lStrFascicolo  += "<br> <font class='cRosso'>(ex ";
          lStrFascicolo += " "+lUfficioAccorpato.getCodTipoUfficio()+" di "+lUfficioAccorpato.getDescrComune();
          lStrFascicolo += ") </font>";
        }
        else {
          lStrFascicolo = lFascModel.getChiaveAnno()+"/"+lFascModel.getChiaveProgr();
        }


        %>
        <tr>
          <td class="c" nowrap><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMessaggio.getDataInvio(),"dd-MM-yyyy"),"-")%></td>
          <td class="c" nowrap><%=lStrFascicolo%></td>
          <td class="c"><%=lMessaggio.getDescrUfficioMittente() +" "+ lMessaggio.getDescrSedeUfficioMittente()%></td>
	<td class="c"><%=lSentModel.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+lSentModel.getDescrTipoProvvedimento().substring(1).toLowerCase()%></td>
          <%--
          <td class="c" nowrap><%=StringUtils.toStringJSP(lSentModel.getAnnoSentenza(),"")%>/<%=StringUtils.toStringJSP(lSentModel.getNumeroSentenza(),"")%></td>
          <td class="c" nowrap><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentModel.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></td>
          --%>
          <td class="c"><%=lSentModel.getDescrTipoAutoritaEmittente()%> di <%=lSentModel.getDescrLuogoEmittente()%></td>
          <td class="c" nowrap><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascModel.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></td>
          
          <%if (lMessaggio.getCodEsito()!=null && lMessaggio.getCodEsito().compareTo("01003")==0 ) { %>
          <td class="cRosso" nowrap>Restituito</td>
          <% } else { %>
          <td class="c" nowrap>In Carico</td>
          <% } %>
          
          <!-- Tasti Azioni -->
          <% if ( (lMessaggio.getCodEsito()!=null && lMessaggio.getCodEsito().compareTo("-")==0) || 
          		  (lMessaggio.getCodEsito()!=null && lMessaggio.getCodEsito().compareTo("01001")==0) ) { %>
          <td class="c" style="text-align:center" nowrap>
            <a href="javascript:IscriviFascicoloInIstruttoria(<%=lFascModel.getIdFascicoloSiep()%>,<%=lMessaggio.getIdMessaggio()%>,'<%=stessoTitolo%>')">
              <img src="/images/esegui.gif" title="Iscrivi Fascicolo" border="0" width="16" height="16"></a>
            <a href="javascript:RestituisciFascicolo(<%=lFascModel.getIdFascicoloSiep()%>,<%=lMessaggio.getIdMessaggio()%>)">
              <img src="/images/Restituzione.gif" title="Restituzione Fascicolo" border="0" width="18" height="18"></a>
          </td>
          <% } else { %>
          <td class="c" style="text-align:center" nowrap> - </td>
          <% } %>
        </tr>
        
   <%
      }
    %>
    <% if (ListaTitoli.size()==0){ %>
    <tr>
      <td>Nessun dato presente</td>
    </tr>
    <% } %>
    
  </table>
</FORM>
</body>
</html>
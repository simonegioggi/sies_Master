<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%@ page import="f3b.log.LogF3B"%>
<%@ page import="org.apache.log4j.Logger"%>

<jsp:useBean id="fascicoloNotInSession" scope="request" class="java.lang.String"/>
<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="CountRisultati" scope="request" class="java.lang.String"/>

<jsp:useBean id="ParentFormName" scope="request" class="java.lang.String"/>

<%
final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

//==============================================================================
//                Form con le funzioni di gestione cumulo
//==============================================================================
//
//La form consente la navigazione tra le funzioni di gestione del cumulo. 
//

String lTotaleMessaggi = "<font class=\"cRosso\">("+CountRisultati+")</font>"; 


%>
<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  
  <script language="JavaScript">
    function eseguiFunzione(action)
    {
      if (   action=='siap.siep.modulocumulo.action.ActStampaProspettoProvvedimentiCumulo'
          || action=='siap.siep.modulocumulo.action.ActStampaProspettoPropostaCumulo'
         ) 
      {
        //alert("Stampa");
        <% if (IstruttoriaCumulo.getIdIstruttoriaCumulo()!=null) { %>
          var parametro = "Action="+action+"&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=StringUtils.toStringJSP(IstruttoriaCumulo.getIdIstruttoriaCumulo())%>";
        stampa2("/jsp/files/Stampa.jsp",  parametro);
        <% } else { %>
          alert ("Nessuna istruttoria selezionata");
        <% } %>
      } else {
        document.GrigliaGestioneCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
        document.GrigliaGestioneCumulo.submit();
      }
    }
    
  </script>

  <script language="JavaScript1.2">

      function over_effect(e,state)
      {
          if (document.all)
              source4=event.srcElement
          else if (document.getElementById)
              source4=e.target
          if (source4.className=="menulines")
              source4.style.borderStyle=state
          else
          {
              while(source4.tagName!="TABLE")
              {
                source4=document.getElementById? source4.parentNode : source4.parentElement
                  if (source4.className=="menulines")
                      source4.style.borderStyle=state
              }
          }
      }

  </script>

  <STYLE>
    .menulines
    {
      border:2.5px solid #BEC6FC;
      text-align : center;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      text-decoration : none;
      height:100%;
    }

    .menulines a
    {
      text-align : center;
      text-decoration:none;
      color:black;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      width:100%;
      height:100%;
    }
  </STYLE>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Gestione Cumulo</font>
      </td>
<%	if(ParentFormName.equals("ElencoEstesoIstruttorieCumulo") )		{ %>
	  <td class="LBG">
	  	<a href="Javascript:history.go(-1);"><img src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="Ritorna su Elenco Istruttorie" border=0></a>
      </td>
<%	} %>      
    </tr>
  </table>
  <br>
<%
    // Se il Fascicolo è in Sessione fa l'include del DettaglioSoggettoSentenza
    if( !fascicoloNotInSession.equals("S") )
    {
%>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<%
    }
%>

<% if(IstruttoriaCumulo !=null && IstruttoriaCumulo.getIdIstruttoriaCumulo()!=null){%>
    <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
<%}%>
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="GrigliaGestioneCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <% if(IstruttoriaCumulo!=null && IstruttoriaCumulo.getIdIstruttoriaCumulo()!=null){%>
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <%} else { %>
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="">
  <%}%>

    <table cellpadding="4" cellspacing="4" width="95%" align="center"
           onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
      <%
      //========================================================================
      //                            ISTRUTTORIA
      //========================================================================
      %>
      <tr>
        <td colspan=3 class="Titolonocap">ISTRUTTORIA</td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoriacumulo.action.ActLoadInserisciIstruttoriaCumulo">Apertura Istruttoria</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="javascript:eseguiFunzione('siap.siep.istruttoriacumulo.action.ActRicercaIstruttoriaCumulo')">Elenco Istruttorie</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadRicercaTitoloDaRichiedere')">Richiesta Trasmissione Atti</a>
        </td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap>
          <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaRichiesteAttiTrasmessiCumulo')">Riscontro Richieste</a>
        </td>
        
        <td width="32%" class="menulines" nowrap>
      <%if(CountRisultati.compareTo("0")!= 0) { %>
          <a title="Ci sono nuovi Atti non ancora elaborati relativi al procedimento corrente"  
             href="javascript:eseguiFunzione('siap.siep.richiesta.action.ActLoadRicercaAttiCompetenzaRicevuti')">Atti Ricevuti Per Competenza <%=StringUtils.toStringJSP(lTotaleMessaggi)%></a>
      <%} else { %> 
      	  <a href="javascript:eseguiFunzione('siap.siep.richiesta.action.ActLoadRicercaAttiCompetenzaRicevuti')">Atti Ricevuti Per Competenza </a>
      <%}  %> 	     
        </td>
                
        <td width="32%" class="menulines" nowrap>
          <a href="javascript:eseguiFunzione('siap.siep.istruttoriacumulo.action.ActLoadElencoFascicoliCoinvolti')">Titoli Coinvolti</a>
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <%
      //========================================================================
      //                             Attività Cumulo
      //========================================================================
      %>
      <tr>
        <td colspan=3 class="Titolonocap">Attività Cumulo</td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap>
          <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteIstruttorie')">Istruttoria Cumulo</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPM')">Richieste del PM</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">Attività del PM dell'Esecuzione</a>
        </td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">Gestione della Pena da Eseguire</a>
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <%
      //========================================================================
      //                              PROSPETTI
      //========================================================================
      %>
      <tr>
        <td colspan=3 class="Titolonocap">PROSPETTI</td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap>
          <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActStampaProspettoProvvedimentiCumulo')">Prospetto Provvedimenti Coinvolti</a>
        </td>
        <td width="32%" class="menulines" nowrap>
         <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActStampaProspettoPropostaCumulo')">Prospetto cumulo (proposta)</a>
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <%
      //========================================================================
      //                              CHIUSURA
      //========================================================================
      %>
      <tr>
        <td colspan=3 class="Titolonocap">Chiusura cumulo</td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap>
          <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadInserisciDatiFinaliCumulo')">Dati Finali Cumulo</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActGrigliaComunicazioni')">Comunicazioni</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="javascript:eseguiFunzione('siap.siep.istruttoriacumulo.action.ActLoadCancellaIstruttoriaCumulo')">Annullamento</a>
        </td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap>
          <a href="javascript:eseguiFunzione('siap.siep.istruttoriacumulo.action.ActLoadTrasferisciIstruttoria')">Trasferimento e Chiusura Istruttoria</a>
        </td>
      </tr>
    </table>
</form>    
</body>
</html>
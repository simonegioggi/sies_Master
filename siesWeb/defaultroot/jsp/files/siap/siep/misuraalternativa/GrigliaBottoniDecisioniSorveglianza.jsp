<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata" %>

<jsp:useBean id="strFunzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloNotInSession" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - <%=strFunzione%></title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

  <script language="JavaScript1.2">
  <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
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
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo"><%=strFunzione%></font>
      </td>
      <td class="LBG">
      </td>
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
  <br>
    <table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
    <%
    //==========================================================================
    //                             MISURE ALTERNATIVE
    //==========================================================================
    %>
    <tr>
      <td colspan=3 class="Titolonocap">Misure Alternative</td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActMisuraAlternativaGrigliaAffInPro">Affidamento in Prova al Servizio Sociale</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActMisuraAlternativaGrigliaDetDom">Detenzione Domiciliare</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActMisuraAlternativaGrigliaSemLib">Semilibertà</a>
      </td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActDetenzioneDomiciliareSpeciale">Detenzione Domiciliare Speciale</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActMisuraAlternativaGrigliaIndultino">L. 207/2003</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActMisuraAlternativaGrigliaLibCond">Liberazione Condizionale</a>
      </td>
    </tr>
    <tr>
       <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.verbale.action.ActLoadInserisciVerbaleSottoscrizione">Registrazione Data Inizio Misura</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadInserisciMARigetto">Rigetto Misure Alternative</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActMisuraAlternativaGrigliaDetDomATempo">Differimento pena nelle forme della detenzione domiciliare</a>
        </td>
    </tr>
    <tr>
       <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.verbale.action.ActLoadInserisciVariazioneVerbaleSottoscrizione">Variazione Data Inizio Misura</a> 
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActMisuraAlternativaGrigliaEspPressoDom">Esecuzione Pena Presso Domicilio</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActMisuraAlternativaGrigliaArrestiDomiciliari">Gestione arresti domiciliari 656 comma 10</a>
        </td>
    </tr>    
    <tr><td>&nbsp;</td></tr>
    <%
    //==========================================================================
    //                             SOSPENSIONI
    //==========================================================================
    %>
    <tr>
      <td colspan=3 class="Titolonocap">Sospensioni</td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <%-- MEV_9-SIEP - Si aggancia la nuova griglia della sospensioni dove viene spostata l'attuale bottone
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sospensione.action.ActLoadInserisciSospensioneDecisioniSorv">Sospensione Esecuzione Pena</a>
        --%>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActMisuraAlternativaGrigliaSospensione">Sospensione Esecuzione Pena</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <!-- VECCHIA GESTIONE DIFFERIMENTO-->
        <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sospensione.action.ActLoadInserisciSospensioneDifferimento">Differimento</a--%>
        <!-- NUOVA GESTIONE DIFFERIMENTO-->
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sospensione.action.ActSospensioneGrigliaDiffer">Differimento</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <!-- VECCHIA GESTIONE DIFFERIMENTO-->
        <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sospensione.action.ActLoadInserisciEspulsione">Espulsione</a--%>
        <!-- NUOVA GESTIONE DIFFERIMENTO-->
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sospensione.action.ActGrigliaEspulsione">Espulsione</a>

      </td>
    </tr>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <%
    //==========================================================================
    //                          LIBERAZIONE ANTICIPATA
    //==========================================================================
    %>
    <tr>
      <td colspan=3 class="Titolonocap">Liberazione Anticipata</td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.libertaanticipata.action.ActLoadInserisciLiberazioneAnticipata">Liberazione Anticipata</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.libertaanticipata.action.ActLoadInsRideterminazionePena">Scomputo Permesso</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActLoadInserisciRidimensionamentoLA&<%=ICostantiLibertaAnticipata.CAMPO_TIPO_COMPUTO_LA%>=<%=ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_REVOCA%>">Revoca Liberazione Anticipata</a>
      </td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActLoadInserisciRidimensionamentoLA&<%=ICostantiLibertaAnticipata.CAMPO_TIPO_COMPUTO_LA%>=<%=ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_RIDIM%>">Ridimensionamento LA</a>
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <%
    //==========================================================================
    //                          ALTRE DECISIONI
    //==========================================================================
    %>
    <tr>
      <td colspan=3 class="Titolonocap">Altre Decisioni</td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.provvedimentogenerico.action.ActLoadInserisciProvvGenericoDecSorv">Altre Ordinanze/Decreti</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.libertaanticipata.action.ActLoadInserisciRimediRisarcitori">Rimedi Risarcitori D.L. 26 giugno 2014, n. 92</a>
      </td>
      
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.libertaanticipata.action.ActLoadInserisciReclamo35Ter">Reclamo Rimedi Risarcitori D.L. 26 giugno 2014, n. 92</a>
      </td>
      
    </tr>
  </table>
</body>
</html>
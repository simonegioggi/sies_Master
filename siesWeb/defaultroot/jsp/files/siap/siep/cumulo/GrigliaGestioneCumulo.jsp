<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<jsp:useBean id="fascicoloNotInSession" scope="request" class="java.lang.String"/>

<jsp:useBean id="EvIstruttoria" scope="request" class="siap.sico.evento.model.EventoModel"/>

<jsp:useBean id="EsisteIstruttoriaAperta" scope="request" class="java.lang.String"/>


<%
//==============================================================================
//                Form con le funzioni di gestione cumulo
//==============================================================================

/*
La form consente la navigazione tra le funzioni di gestione del cumulo. Possono
verificarsi quattro casi:
1) non esiste alcuna istruttoria
2) sto visualizzando una istruttoria aperta
3) sto visualizzando una istruttoria chiusa (non esiste una aperta)
4) sto visualizzando una istruttoria chiusa (esiste una aperta)

EvIstruttoria = istruttoria corrente. Casi 2,3,4.
EvIstruttoria = null solo se non esiste una istruttoria 
EsisteIstruttoriaAperta = "SI" se esiste una istruttoria aperta, se sto visualizzando
una vecchia istruttoria chiusa devo comunque 
*/
%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  
  <script language="JavaScript">
    function alertIstruttoria()
    {
      alert("Attenzione! Esiste già un'istruttoria aperta");
    }
  </script>

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
        <font class="campo">Gestione Cumulo</font>
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

<% if(EvIstruttoria!=null && EvIstruttoria.getIdEvento()!=null){%>
 		<jsp:include page="/jsp/files/siap/siep/cumulo/DettaglioIstruttoriaCumulo.jsp"/>
<%}%>

  <br>
    <table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
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
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoriacumulo.action.ActLoadInserisciIstruttoriaCumulo">Inizio Istruttoria</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">Inserimento Fascicoli Coinvolti</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">Visualizzazione Provvedimenti Coinvolti</a>
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
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">Istruttoria Cumulo</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstructiont">Richieste del PM dell'Esecuzione</a>
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
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">Prospetto Provvedimenti Coinvolti</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">Prospetto cumulo (proposta)</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">Prospetto cumulo (creazione)</a>
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
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">Annotazione Dati Finali</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">Provvedimenti di Cumulo</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoriacumulo.action.ActLoadAnnullaIstruttoriaCumulo">Annullamento</a>
        </td>
      </tr>

    </table>
</body>
</html>
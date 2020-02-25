<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

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
      <tr>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActLoadInserisciTrasmissioneAttiEsecuzione">Trasmissione Atti per Esecuzione</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActGestioneRiscontroTrasmissioneAtti">Riscontro Trasmissione Atti per Esecuzione</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActLoadInserisciAnnotazioneProvvedimento">Annotazione Provvedimento Sanzione Sostitutiva</a>
        </td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActLoadAnnotazioneAffidamento">Affidamento in prova al <br>Servizio Sociale</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActLoadAnnotazioneRevocaConversione">Annotazione Revoca / Conversione</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActLoadRichiestaRevSS">Richiesta Revoca Sanzione Sostitutiva al GE</a>
        </td>        
      </tr>

      <tr><td>&nbsp;</td></tr>

    </table>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>

<!-- 		GrigliaBottoniEstrazioneDatiStatisticheUfficio		 -->
<html>
<head>
  <title>[S.I.E.S.] - Estrazioni Dati per Statistiche </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

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
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo"> Estrazioni Dati Statistiche Ufficio  </font>
      </td>
      <td class="LBG">
      </td>
     </tr>
  </table>

 <table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td colspan=3 class="Titolonocap"> Estrazione Dati </td>
    </tr>

    <tr>
       <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.statis.action.ActLoadStatisticaRiepilogoIscrizionieAttivitaCPP"> Riepilogo Iscrizioni e Attività </a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.statis.action.ActLoadStatisticaTempiIscrizioneFascicoliCPP"> Tempi Iscrizione Fascicoli </a>
      </td>
      <td width="32%" class="menulines" nowrap>
      <%// 07-06-2016 - Riciclo dopo primo collaudo V.10 %>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.statis.action.ActLoadPreRiepilogoCPP"> Riepilogo Procedimenti Pendenti </a>
      <%// 07-06-2016 - END Riciclo %>   
      </td>      
    </tr>
        <tr>
       <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction"> Movimento procedimenti </a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction"> Attività Pubblico Ministero </a>
      </td>
      <td width="32%" class="menulines" nowrap>
      <%// 07-06-2016 - Riciclo dopo primo collaudo V.10 %>
        <%-- a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penapecuniaria.action.ActLoadEstrazioneMagistratiPenaPecuniaria"> Procedimenti Assegnati al Magistrato </a --%>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction"> Procedimenti Assegnati al Magistrato </a>
      <%// 07-06-2016 - END Riciclo %>  
      </td>      
    </tr>
  </table>
  
</body>
</html>
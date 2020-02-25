<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>

<jsp:useBean id="fascicoloNotInSession" scope="request" class="java.lang.String"/>
<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="eventonotificaIrreperibilità" scope="request" class="siap.sico.evento.model.EventoNotificaModel" />

<html>
<head>
  <title>[S.I.E.S.] - Notifiche </title>

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
        <font class="campo">Gestione Decreto di Sospensione</font>
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
<%
	if( eventonotifica.getEvento() != null && eventonotifica.getEvento().getIdEvento() != null )
	{
%>
	  <table cellspacing=0 cellpadding=0 width=95%>
	    <tr>
	    <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
	      <td class="L">
	        <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrTipoProvvedimento())%>&nbsp;<%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%> emesso in data <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
	      </td>
	    </tr>
	  </table>  
		<br>
<%
	}
%>
<%
	if( eventonotificaIrreperibilità.getEvento() != null && eventonotificaIrreperibilità.getEvento().getIdEvento() != null )
	{
%>
	  <table cellspacing=0 cellpadding=0 width=95%>
	    <tr>
	    <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
	      <td class="L">
	        <font class="campo"><%=StringUtils.toStringJSP(eventonotificaIrreperibilità.getEvento().getDescrTipoProvvedimento())%>&nbsp;<%=StringUtils.toStringJSP(eventonotificaIrreperibilità.getEvento().getDescrMotivo())%> emesso in data <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotificaIrreperibilità.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
	      </td>
	    </tr>
	  </table>  
		<br>
<%
	}
%>
    <table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">

      <tr>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.notifica.action.ActGrigliaNotifiche">Notifiche</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.notifica.action.ActLoadOmessaNotifica">Rinnovo Ricerche per Omesse Notifiche</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.notifica.action.ActLoadInserisciRich8Bis">Richiesta Informazioni comma 8 bis</a>

        </td>
      </tr>
    	<tr>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.notifica.action.ActLoadRinnovazioneNotifica">Rinnovazione Notifica</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.notifica.action.ActGrigliaIrreperibilita">Irreperibilità</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.notifica.action.ActLoadInserisciSolleciti">Solleciti</a>
        </td>
      </tr>
  </table>
</body>
</html>
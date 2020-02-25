<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<jsp:useBean id="ufficioDest" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="eventoSollecito" scope="request" class="siap.sico.evento.model.EventoModel"/>


<html>
<head>
  <title>[S.I.E.S.] - Gestione Trasmissione per Competenza Misure di Sicurezza</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>


<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Trasferimento Sollecito Esito Trasmissione per Competenza Misure di Sicurezza</font>
    </tr>
    <td class="LBG">
      <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadDettaglioSollecitoEsitoTrasmissione&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventoSollecito.getIdEvento()%>">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
      </a>
    </td>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="TrasferisciCompetenza" onsubmit="document.TrasferisciCompetenza.conferma.disabled=true">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActTrasferisciSollecitoEsito">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventoSollecito.getIdEvento()%>">

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Destinatario </td >
        <td class="L">
          <font class="campo">
          <%=ufficioDest.getDescrTipoUfficio()%>
          </font>
        </td>
      </tr>
      <tr>
        <td class="l">Sede Destinatario </td>
        <td class="L">
          <font class="campo">
          <%=ufficioDest.getDescrComune()%>
          </font>
        </td>
      </tr>
    </table>
    <br>
    <table>
      <tr>
        <td>
          <input name="conferma" class="bottone"  type="submit" value="Conferma">
        </td>
      </tr>
    </table>
  </form>

</body>
</html>
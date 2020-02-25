<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel"%>
<%@ page import="siap.sico.assistentegiudiziario.action.ICostantiAssistenteGiudiziario"%>

<jsp:useBean id="assistentegiudiziario" scope="request" class="siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio AssistenteGiudiziario </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>
  <body class="corpo">
    <FORM name="comandi" >
      <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Assistente Udienza</font>
      </td>
      <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiAssistenteGiudiziario.CAMPO_ID_ASSISTENTE_GIUDIZIARIO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=assistentegiudiziario.getIdAssistenteGiudiziario()%>" />
       </jsp:include>
      </td>
      </tr>
      </table>
    </FORM>
    <table cellspacing=4 cellpadding=4>
      <tr>
        <td class="l">Cognome</td>
        <td class="l"><font class="campo"><%=assistentegiudiziario.getCognome() %></font></td>
      </tr>
      <tr>
        <td class="l">Nome</td>
        <td class="l"><font class="campo"><%=assistentegiudiziario.getNome() %></font></td>
      </tr>
      <tr>
        <td class="l">Disponibilità</td>
        <td class="l"><font class="campo"><%=assistentegiudiziario.getFlagStato() %></font></td>
      </tr>
      <tr>
        <td class="l">DataInizioValidità</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(assistentegiudiziario.getDataInizioValidita(),"dd-MM-yyyy"),"-")%> </font></td>
      </tr>
      <tr>
        <td class="l">DataFineValidità</td>
        <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(assistentegiudiziario.getDataFineValidita(),"dd-MM-yyyy"),"-")%> </font></td>
      </tr>
    </table>
  </body>
</html>
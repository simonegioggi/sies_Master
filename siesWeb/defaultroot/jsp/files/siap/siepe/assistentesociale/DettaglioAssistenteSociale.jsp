<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.siepe.assistentesociale.model.AssistenteSocialeModel"%>
<%@ page import="siap.siepe.assistentesociale.action.ICostantiAssistenteSociale"%>

<jsp:useBean id="assistentesociale" scope="request" class="siap.siepe.assistentesociale.model.AssistenteSocialeModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio AssistenteSociale </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>


  <body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Assistente Sociale</font>
          </td>
          <td class="LBG">
            <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiAssistenteSociale.CAMPO_ID_ASSISTENTE_SOCIALE%>" />
            <jsp:param name="ValoreIdEntita" value="<%=assistentesociale.getIdAssistenteSociale()%>" />
            </jsp:include>
          </td>
        </tr>
      </table>
    </FORM>

    <table cellspacing=4 cellpadding=4>
          <tr>
            <td class="l">Cognome</td>
            <td class="l"><font class="campo"><%=assistentesociale.getCognome() %></font></td>
          </tr>
          <tr>
            <td class="l">Nome</td>
            <td class="l"><font class="campo"><%=assistentesociale.getNome() %></font></td>
          </tr>
          <tr>
            <td class="l">Codice Fiscale</td>
            <td class="l"><font class="campo"><%=StringUtils.toStringJSP(assistentesociale.getCodiceFiscale(),"-") %></font></td>
          </tr>
          <tr>
            <td class="l">Indirizzo</td>
            <td class="l"><font class="campo"><%=StringUtils.toStringJSP(assistentesociale.getIndirizzo(),"-") %></font></td>
          </tr>
          <tr>
            <td class="l">Telefono</td>
            <td class="l"><font class="campo"><%=StringUtils.toStringJSP(assistentesociale.getTelefono(),"-") %></font></td>
          </tr>
          <tr>
            <td class="l">Email</td>
            <td class="l"><font class="campo"><%=StringUtils.toStringJSP(assistentesociale.getEmail(),"-") %></font></td>
          </tr>
          <tr>
            <td class="l">Fax</td>
            <td class="l"><font class="campo"><%=StringUtils.toStringJSP(assistentesociale.getFax(),"-") %></font></td>
          </tr>
          <tr>
            <td class="l">Cellulare</td>
            <td class="l"><font class="campo"><%=StringUtils.toStringJSP(assistentesociale.getCellulare(),"-") %></font></td>
          </tr>
          <tr>
            <td class="l">Data Inizio Validità</td>
            <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(assistentesociale.getDataInizioValidita(),"dd-MM-yyyy"),"-") %> </font></td>
          </tr>
          <tr>
            <td class="l">Data Fine Validità</td>
            <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(assistentesociale.getDataFineValidita(),"dd-MM-yyyy"),"-")%> </font></td>
          </tr>
          <tr>
            <td class="l">Disponibilità</td>
            <td class="l"><font class="campo"><%=StringUtils.toStringJSP(assistentesociale.getDescrFlagStato(),"-") %></font></td>
          </tr>
    </table>
   </body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="f3b.util.StringUtils"%>
<%@page import="java.util.Vector"%>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" %>

<%
Vector <FascicoloSigeEstesoModel>fascicoli = (Vector <FascicoloSigeEstesoModel>)request.getAttribute("fascicoli");
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca  Procedimento per Numero SIGE</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Ricerca Procedimento per Numero SIGE&nbsp;</font></td>
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%-- td class="LBG">
          <a href="javascript:history.go(-1);" onMouseOver="hiLite('img01','clickme2')" onMouseOut="hiLite('img01','clickme1')">
            <IMG SRC="<%=IWebConstants.IMAGES_DIR %>/arrowleft24.gif" BORDER="0" ALT="" NAME="img01">
          </a>
      </font></td --%>
    </tr>
  </table>

  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>


  <br>

  <table cellspacing=2 cellpadding=2>
    <tr>

      <td class="int">Numero SIGE</td>
      <td class="int">Ufficio</td>
      <td class="int">TipoAtto</td>
      <td class="int">Data di Iscrizione</td>
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Data di nascita</td>
      <td class="int">Sezione</td>
      <td class="int">Magistrato Assegnatario</td>
      <td class="int">Stato</td>
      <td class="int">Azioni</td>
    </tr>
<%
  for (FascicoloSigeEstesoModel fascicolo : fascicoli ) {
      String aDataIscrizione = (DateUtils.getDateToString(fascicolo.getFascicoloSige().getDataIscrizione(),"dd-MM-yyyy") != null) ? DateUtils.getDateToString(fascicolo.getFascicoloSige().getDataIscrizione(),"dd-MM-yyyy") : "-";
      String aDataNascita = (DateUtils.getDateToString(fascicolo.getSoggetto().getDataNascita(),"dd-MM-yyyy") != null) ? DateUtils.getDateToString(fascicolo.getSoggetto().getDataNascita(),"dd-MM-yyyy") : "-";
%>
      <tr>
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSige().getChiaveAnno()%>/<%=fascicolo.getFascicoloSige().getChiaveProgr()%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSige().getDescrUfficio()%> </font></td>
        <td class="c"><font class="label"><%=fascicolo.getRichiestaSige().getDescrTipoAtto()%> </font></td>
        <td class="c"><font class="label"><%=aDataIscrizione%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getSoggetto().getCognome()%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getSoggetto().getNome()%></font></td>
        <td class="c"><font class="label"><%=aDataNascita%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSige().getDescrSezione() %></font></td>
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSige().getNomeCognomeMagistrato() %></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getFascicoloSige().getDescrStatoFascicolo(),"-")%></font></td>
        <td class="c">
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>" />
           <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getFascicoloSige().getIdFascicoloSige()%>" />
        </jsp:include>
        </td>
      </tr>
<%
  }
%>
    </table>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

  </FORM>
  <br>

  </body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe" %>

<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.siepe.fascicolo.model.FascicoloSoggAttModel" %>

<%@ page import="siap.web.LoadPaginazione" %>
<%@ page import="siap.web.PaginazioneModel" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca  Procedimenti SIEPE</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Ricerca Procedimenti SIEPE&nbsp;</font></td>
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

      <td class="int">Numero SIEPE</td>
      <td class="int">Descr. Ufficio</td>
      <td class="int">Numero UEPE</td>
      <td class="int">Tipo Incarico</td>
      <td class="int">Data Iscriz. Proc.</td>
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Data di nascita</td>
      <td class="int">Azioni</td>
    </tr>
<%

    //Iterator itx = PagModel.getRisultatiRicerca().iterator();
   Iterator itx = fascicoli.iterator();
    String sUfficio = "Procura";
    String aDataIscrizione = "";
    String aDataNascita = "";
    while ( itx.hasNext())
    {
      FascicoloSoggAttModel fascicolo = (FascicoloSoggAttModel)itx.next();
      // Si evita di presentare le Date = "null"
      aDataIscrizione = (DateUtils.getDateToString(fascicolo.getFascicoloSiepeRicercaModel().getDataIscrizione(),"dd-MM-yyyy") != null) ? DateUtils.getDateToString(fascicolo.getFascicoloSiepeRicercaModel().getDataIscrizione(),"dd-MM-yyyy") : "-";
      aDataNascita = (DateUtils.getDateToString(fascicolo.getSoggettoModel().getDataNascita(),"dd-MM-yyyy") != null) ? DateUtils.getDateToString(fascicolo.getSoggettoModel().getDataNascita(),"dd-MM-yyyy") : "-";
%>
      <tr>
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSiepeRicercaModel().getChiaveAnno()%>/<%=fascicolo.getFascicoloSiepeRicercaModel().getChiaveProgr()%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSiepeRicercaModel().getDescrTipoUfficio()%> - <%=fascicolo.getFascicoloSiepeRicercaModel().getDescrComuneUfficio()%></font></td>
        <td class="c"><font class="label">
						<%if (fascicolo.getFascicoloSiepeRicercaModel().getAnnoUepe() == null) {%>-<%}
						else{%><%=fascicolo.getFascicoloSiepeRicercaModel().getAnnoUepe()%><%}%>
						/<%if (fascicolo.getFascicoloSiepeRicercaModel().getNumUepe() == null){%>-<%}
						else{%><%=fascicolo.getFascicoloSiepeRicercaModel().getNumUepe()%><%}%>
						/<%if (fascicolo.getFascicoloSiepeRicercaModel().getProgrUepe() == null){%>-<%}
						else{%><%=fascicolo.getFascicoloSiepeRicercaModel().getProgrUepe()%><%}%>
				</font></td>
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSiepeRicercaModel().getDescrIncarico()%></font></td>
        <td class="c"><font class="label"><%=aDataIscrizione%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getSoggettoModel().getCognome()%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getSoggettoModel().getNome()%></font></td>
        <td class="c"><font class="label"><%=aDataNascita%></font></td>
        <td class="c">
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSiepe.CAMPO_ID_FASCICOLO_SIEPE%>" />
           <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getFascicoloSiepeRicercaModel().getIdFascicoloSiepe()%>" />
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
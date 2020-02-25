<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.esecuzionesanzionesostitutiva.action.ICostantiEsecuzioneSS" %>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel" %>
<%@ page import="siap.sius.esecuzionesanzionesostitutiva.model.ESSFascGPModel" %>

<jsp:useBean id="esecuzioni" scope="request" class="java.util.Vector" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Elenco Procedimenti di Esecuzione Sanzioni Sostitutive</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Procedimenti di Esecuzione Sanzioni Sostitutive</font></td>
   </tr>
  </table>

  <br>
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Procedimento SIUS</td>
      <td class="int">Ordinanza</td>
      <td class="int">UDS Emittente</td>
      <td class="int">Data Emissione</td>
      <td class="int">Sanzione da eseguire</td>
      <td class="int">Data inizio sanzione</td>
      <td class="int">Soggetto</td>
      <td class="int">Azioni</td>
    </tr>
<%
    Iterator itx = esecuzioni.iterator();
    while ( itx.hasNext())
    {
      ESSFascGPModel esecuzione = (ESSFascGPModel)itx.next();
%>

      <tr>
        <td class="c"><font class="label">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=esecuzione.getFascicoloSiusModel().getIdFascicoloSius()%><%=retParam%>" Title="Dettaglio Procedimento SIUS">
            <%=esecuzione.getFascicoloSiusModel().getChiaveAnno()%>/<%=esecuzione.getFascicoloSiusModel().getChiaveProgr()%>
          </a></font>
        </td>
        <td class="c">
          <font class="label"><%=esecuzione.getEsecuzioneSSModel().getAnnoS07()%>/<%=esecuzione.getEsecuzioneSSModel().getProgrS07()%> </font>
        </td>
        <td class="c">
          <font class="label"><%=esecuzione.getEsecuzioneSSModel().getDescrLuogoAutoritaEmittOrd()%> </font>
        </td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(esecuzione.getEsecuzioneSSModel().getDataOrdinanza(),"dd-MM-yyyy"),"-")%></font></td>
        <td class="c"><font class="label"><%=esecuzione.getGeneraleProcedimentoModel().getDescrDefinizione()%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(esecuzione.getEsecuzioneSSModel().getDataOrdinanza(),"dd-MM-yyyy"),"-")%></font></td>
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <td class="c"><font class="label"><%=esecuzione.getFascicoloSiusModel().getSoggetto().getCognome()%>&nbsp;<%=esecuzione.getFascicoloSiusModel().getSoggetto().getNome()%></font></td>
        <td class="c">
          <jsp:include page="<%=ICostantiEsecuzioneSS.PG_BUTTONS_ESS%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiEsecuzioneSS.CAMPO_ID_ESECUZIONE_SS%>" />
             <jsp:param name="ValoreIdEntita" value="<%=esecuzione.getEsecuzioneSSModel().getIdEsecuzioneSanzioneSost()%>" />
             <jsp:param name="IdSoggetto" value="<%=esecuzione.getFascicoloSiusModel().getSoggetto().getIdSoggetto()%>" />
             <jsp:param name="IdFascicoloSius" value="<%=esecuzione.getFascicoloSiusModel().getIdFascicoloSius()%>" />
             <jsp:param name="IdFascicoloSIEP" value="<%=esecuzione.getFascicoloSiusModel().getFasSieIdFascicoloSiep()%>" />
             <jsp:param name="TornaQui" value="<%=TornaQui%>" />
          </jsp:include>
        </td>
      </tr>
<%
  }
%>
    </table>
  </FORM>
  <br>

  </body>
</html>
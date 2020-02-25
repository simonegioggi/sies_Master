<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.istanza.model.IstanzaModel"%>
<%@ page import="siap.siep.istanza.model.IstanzaSoggettoEventoFascicoloSiepModel"%>
<%@ page import="siap.siep.istanza.action.ICostantiIstanza"%>

<jsp:useBean id="istanzaSoggettoEventoFascicoloSiep" scope="request" class="siap.siep.istanza.model.IstanzaSoggettoEventoFascicoloSiepModel"/>

<%
  IstanzaModel lIstanza = istanzaSoggettoEventoFascicoloSiep.getIstanza();
  SoggettoModel lSoggetto = istanzaSoggettoEventoFascicoloSiep.getSoggetto();
  EventoModel lEvento = istanzaSoggettoEventoFascicoloSiep.getEvento();

  FascicoloSiepModel lFascicolo = istanzaSoggettoEventoFascicoloSiep.getFascicoloSiep();
  if(lFascicolo == null)
  {
    lFascicolo = new FascicoloSiepModel();
  }

  String lFlagEvento = "S";
  if (lEvento == null)
  {
    lFlagEvento = "N";
    lEvento = new EventoModel();
  }

%>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Istanza Annotazione Trasmissione</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>
  <body class="corpo">

    <FORM name="comandi" >
      <table>
        <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Istanza Annotazione Trasmissione</font>
          </td>
         <td class="LBG">
            <jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_ISTANZA%>">
              <jsp:param name="CampoIdEntita" value="<%=ICostantiIstanza.CAMPO_ID_ISTANZA%>" />
              <jsp:param name="ValoreIdEntita" value="<%=lIstanza.getIdIstanza()%>" />
              <jsp:param name="FlagEvento" value="<%=lFlagEvento%>" />
              <jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
              <jsp:param name="CampoIdEntitaEvento" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
              <jsp:param name="ValoreIdEntitaEvento" value="<%=lEvento.getIdEvento()%>" />
            </jsp:include>
          </td>
        </tr>
      </table>
    </FORM>
 <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <table>
<!----------- FASCICOLO -------------------->
      <tr><td class="Titolo" colspan=4>Procedimento (N.SIEP)</td></tr>
      <tr>
        <td class="L">Anno/Numero</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lFascicolo.getChiaveAnno())%>/<%=StringUtils.toStringJSP(lFascicolo.getChiaveProgr())%></font></td>
      </tr>
<!------------------------------------------>
<!----------- OGGETTO ---------------------->
     <tr><td class="Titolo" colspan=4>Dati dell'istanza</td></tr>
     <tr>
        <td class="l">Data</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lIstanza.getDataPresentazione(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l">Oggetto</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lIstanza.getDescrMotivo())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font  class="label">Note</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lIstanza.getNote())%>&nbsp;</font></td>
      </tr>
<!------------------------------------------>
    </table>
  </body>
</html>
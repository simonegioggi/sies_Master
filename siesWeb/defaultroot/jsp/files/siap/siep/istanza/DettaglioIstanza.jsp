<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.istanza.model.IstanzaModel"%>
<%@ page import="siap.siep.istanza.model.IstanzaSoggettoEventoFascicoloSiepModel"%>
<%@ page import="siap.siep.istanza.action.ICostantiIstanza"%>

<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>

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
    String lAnnullato = "N";
    if(lIstanza != null && lIstanza.getCodStatoIstanza()!= null && lIstanza.getCodStatoIstanza().compareTo("C")==0)
     {
       lAnnullato = "S";
     }
%>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Istanza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>
  <body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Istanza</font>
          </td>
          <td class="LBG">
            <jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_ISTANZA%>">
              <jsp:param name="CampoIdEntita" value="<%=ICostantiIstanza.CAMPO_ID_ISTANZA%>" />
              <jsp:param name="ValoreIdEntita" value="<%=lIstanza.getIdIstanza()%>" />
              <jsp:param name="FlagEvento" value="<%=lFlagEvento%>" />
              <jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
              <jsp:param name="CampoIdEntitaEvento" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
              <jsp:param name="ValoreIdEntitaEvento" value="<%=lEvento.getIdEvento()%>" />
              <jsp:param name="annullato" value="<%=lAnnullato%>" />

            </jsp:include>
          </td>
        </tr>
      </table>
    </FORM>

    <table>
<!----------- SOGGETTO --------------------->
      <tr><td class="Titolo" colspan=4>Soggetto di riferimento</td></tr>
      <tr>
        <td class="l"><font class="label">Cognome</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lSoggetto.getCognome())%>&nbsp;</font></td>
        <td class="l"><font class="label">Nome</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lSoggetto.getNome())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Sesso</font></td>
        <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lSoggetto.getSesso())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font  class="label">Data di nascita</font></td>
        <td class="l" colspan=3><font  class="campo"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lSoggetto.getDataNascita(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
        if(lSoggetto.getDataNascitaPresunta() != null && !lSoggetto.getDataNascitaPresunta().equals("N"))
        {
%>
          <td class="l"><font  class="label">Presunta</font></td>
          <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lSoggetto.getDataNascitaPresunta())%>&nbsp;</font></td>
<%
        }
%>
      </tr>
      <tr>
        <td class="l"><font  class="label">Comune Nascita</font></td>
        <td class="l" colspan=3>
          <font class="campo">
            <%=StringUtils.toStringJSP(lSoggetto.getDescrComuneNascita() )%>
<%
            if( (lSoggetto.getDescrComuneNascita() != null) && (!(lSoggetto.getDescrComuneNascita().equals(""))) && (!(lSoggetto.getDescrComuneNascita().equals("-"))) )
            {
%>
              (<%=lSoggetto.getCodProvinciaNascita()%>)
<%
            }
%>
            &nbsp;
          </font>
        </td>
      </tr>
      <tr>
        <td class="l"><font class="label">Nazionalità</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lSoggetto.getDescrNazionalita())%>&nbsp;</font></td>
        <td class="l"><font  class="label">Stato Nascita</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lSoggetto.getDescrStatoNascita())%>&nbsp;</font></td>
      </tr>
<!----------------------------------------------------->
<!----------- SOGGETTO PRESENTANTE--------------------->
        <tr><td class="Titolo" colspan=4>Soggetto presentante</td></tr>
        <tr>
          <td class="l">Cognome</td>
          <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lIstanza.getCognomeSoggettoPresentante())%>&nbsp;</font></td>
          <td class="l">Nome</td>
          <td class="l" ><font class="campo"><%=StringUtils.toStringJSP(lIstanza.getNomeSoggettoPresentante())%>&nbsp;</font></td>
        </tr>
<!------------------------------------------>
<!----------- AVVOCATO --------------------->
      <tr><td class="Titolo" colspan=4>Avvocato</td></tr>
      <tr>
        <td class="l">Cognome</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lIstanza.getCognomeAvvocato())%>&nbsp;</font></td>
        <td class="l">Nome</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lIstanza.getNomeAvvocato())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l">Foro Competenza</td>
        <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lIstanza.getForoCompetenza())%>&nbsp;</font></td>
      </tr>
<!------------------------------------------>
<!----------- SENTENZA --------------------->
      <tr><td class="Titolo" colspan=4>Estremi della sentenza</td></tr>
      <tr>
        <td class="l">Anno/Numero</td>
        <td class="l" colspan=3><font class="campo">
     <%=StringUtils.toStringJSP(lIstanza.getAnnoSentenza())%>/<%=StringUtils.toStringJSP(lIstanza.getNumeroSentenza())%></font></td>
      </tr>
      <tr>
        <td class="l">Data</td>
        <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lIstanza.getDataSentenza(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l">Autorità Emittente</td>
        <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lIstanza.getDescrTipoAutoritaEmittente())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l">Luogo Emittente</td>
        <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lIstanza.getDescrLuogoEmittente())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l">Data Irrevocabilità</td>
        <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lIstanza.getDataIrrevocabilita(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      </tr>
<!------------------------------------------>
<!----------- FASCICOLO -------------------->
      <tr><td class="Titolo" colspan=4>Procedimento (N.SIEP)</td></tr>
      <tr>
        <td class="L">Anno/Numero</td>
        <td class="l" colspan=3>
         <font class="campo">
<%=StringUtils.toStringJSP(lFascicolo.getChiaveAnno())%>/<%=StringUtils.toStringJSP(lFascicolo.getChiaveProgr())%></font></td>
      </tr>
<!------------------------------------------>
<!----------- OGGETTO ---------------------->
     <tr><td class="Titolo" colspan=4>Dati dell'istanza</td></tr>
     <tr>
        <td class="l">Data</td>
        <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lIstanza.getDataPresentazione(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l">Oggetto</td>
        <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lIstanza.getDescrMotivo())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font  class="label">Note</font></td>
        <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lIstanza.getNote())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font  class="label">Stato istanza</font></td>
        <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lIstanza.getDescrEsito())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font  class="label">Ufficio Destinario</font></td>
        <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lIstanza.getDescrTipoUfficioDestinatario())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font  class="label">Luogo Destinatario</font></td>
        <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lIstanza.getDescrLuogoDestinatario())%>&nbsp;</font></td>
      </tr>
<!------------------------------------------>
    </table>
  </body>
</html>
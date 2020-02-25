<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="soggetto" scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="fascicolo" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="istanza" scope="request" class="siap.siep.istanza.model.IstanzaModel"/>

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
            <font class="campo">Dettaglio Istanza Ricevuta</font>
          </td>
         </tr>
      </table>
    </FORM>
    <table cellspacing=2 cellpadding=2>
<!----------- SOGGETTO --------------------->

 <tr><td class="Titolo" colspan=4>Oggetto dell'Istanza</td></tr>
      <tr>
        <td class="l"><font class="label">Oggetto</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(istanza.getDescrMotivo())%>&nbsp;</font></td>
       </tr>


      <tr><td class="Titolo" colspan=4>Soggetto di riferimento</td></tr>
      <tr>
        <td class="l"><font class="label">Cognome</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCognome())%>&nbsp;</font></td>
        <td class="l"><font class="label">Nome</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getNome())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Sesso</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getSesso())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l" width="25%"><font  class="label">Data di nascita</font></td>
        <td class="l" width="25%"><font  class="campo"><%= StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%>&nbsp;</font></td>
        <td class="l" width="25%"><font  class="label">Presunta</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDataNascitaPresunta())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font  class="label">Comune Nascita</font></td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(soggetto.getDescrComuneNascita() )%>
<%
            if( (soggetto.getDescrComuneNascita() != null) && (!(soggetto.getDescrComuneNascita().equals(""))) && (!(soggetto.getDescrComuneNascita().equals("-"))) )
            {
%>
              (<%=soggetto.getCodProvinciaNascita()%>)
<%
            }
%>
            &nbsp;
          </font>
        </td>
      </tr>
      <tr>
        <td class="l"><font class="label">Nazionalità</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrNazionalita())%>&nbsp;</font></td>
        <td class="l"><font  class="label">Stato Nascita</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrStatoNascita())%>&nbsp;</font></td>
      </tr>

<!------------------------------------------>
<!------------------------------------------>
<!----------- SENTENZA --------------------->
      <tr><td class="Titolo" colspan=4>Estremi della sentenza</td></tr>
      <tr>
        <td class="l">Anno/Numero</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getSentenza().getAnnoSentenza())%>/<%=StringUtils.toStringJSP(fascicolo.getSentenza().getNumeroSentenza())%></font></td>
      </tr>
      <tr>
        <td class="l">Data</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getSentenza().getDataSentenza(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l">Autorita Emittente</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getSentenza().getDescrTipoAutoritaEmittente())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l">Luogo Emittente</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getSentenza().getDescrLuogoEmittente())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l">Data Irrevocabilita</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      </tr>

<!----------- FASCICOLO -------------------->
      <tr><td class="Titolo" colspan=4>Procedimento (N.SIEP)</td></tr>
      <tr>
        <td class="L">Anno/Numero</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getChiaveAnno())%>/<%=StringUtils.toStringJSP(fascicolo.getChiaveProgr())%></font></td>
      </tr>
     <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="ConfermaPresaInCaricoIstanza" >
     <tr>

          <td>
            <input class=bottone  type="submit" value="Conferma Presa in Carico">
          </td>
        </tr>

        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istanza.action.ActConfermaPresaInCarico">
        <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
<!------------------------------------------>
</form >

</table>
  </body>
</html>
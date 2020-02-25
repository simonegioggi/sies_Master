<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="istanza" scope="request" class="siap.siep.istanza.model.IstanzaModel"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />

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
            <font class="campo">Dettaglio Istanza Spedita</font>
          </td>
          <!-- BOTTONE DI RITORNO -->
          <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
         </tr>
      </table>
    </FORM>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <table cellspacing=2 cellpadding=2>
<!----------- MESSAGGIO --------------------->
      <tr>
        <td class="Titolo" colspan=4>Oggetto dell'Istanza</td>
      </tr>
      <tr>
        <td class="l"><font class="label">Oggetto</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(istanza.getDescrMotivo())%>&nbsp;</font></td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="Titolo" colspan=4>Dati Messaggio</td></tr>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
      <tr>
        <td class="l"><font class="label">Id JMS</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getJmsIdMessaggio())%>&nbsp;</font></td>
     </tr>
--%>
     <tr>
        <td class="l"><font class="label">Stato Messaggio</font></td>
<%
        if (Messaggio.getMessaggioCorrelato() == null)
        {
%>
          <td class="lRosso">In Attesa di risposta...&nbsp;</td>
<%
        }
        else
        {
%>
          <td class="lVerde"><font class="campo">Risposta Ricevuta&nbsp;</font></td>
<%
        }
%>
      </tr>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
      <tr>
        <td class="l"><font class="label">Tipo Messaggio</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrTipoMessaggio())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Tipo Operazione</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getTipoOperazione())%>&nbsp;</font></td>
      </tr>
--%>
      <tr>
        <td class="l"><font class="label">Data Invio</font></td>
        <td class="l"><font class="campo"><%=DateUtils.getDateToString(Messaggio.getDataInvio(),"dd-MM-yyyy  HH:mm:ss")%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Utente Mittente</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getCodiceUtenteMittente())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Ufficio Mittente</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrUfficioMittente() +" "+Messaggio.getDescrSedeUfficioMittente())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Ufficio Destinatario</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrUfficioDestinatario()+" "+Messaggio.getDescrSedeUfficioDestinatario())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Data Ricezione</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Messaggio.getDataEsito(),"dd-MM-yyyy HH:mm:ss"))%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Esito</font></td>
        <td class="l">
<%
        if (Messaggio.getMessaggioCorrelato() != null)
        {
%>
            <font class="campo"><%=Messaggio.getMessaggioCorrelato().getDescrEsito()%>&nbsp;</font>
<%
        }
%>
            &nbsp;
          </td>
        </tr>
      </table>
     <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="ConfermaPresaInCaricoIstanza">
     <tr>
         <!-- <td>
            <input class=bottone  type="submit" value="Conferma Presa in Carico">
          </td>
        </tr>-->

        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istanza.action.ActConfermaPresaInCarico">
        <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
        <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >
<!------------------------------------------>
</form >

</table>
  </body>
</html>
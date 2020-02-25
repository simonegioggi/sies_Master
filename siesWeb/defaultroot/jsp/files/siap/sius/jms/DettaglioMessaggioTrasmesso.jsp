<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.jms.ICostantiJMS"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="eventoNotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>

<%
String strTitle="Dettaglio Messaggio Trasmesso";
if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_ORDINANZA)==0 )
  strTitle = "Dettaglio Ordinanza Trasmessa";
else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_DECRETO)==0 )
  strTitle = "Dettaglio Decreto Trasmesso";
else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_RICORSO)==0 )
  strTitle = "Dettaglio Ricorso Trasmesso";
else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_SENTENZA)==0 )
	strTitle = "Dettaglio Sentenza Trasmessa";
%>
<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Messaggio Trasmesso</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>
  <body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo"><%=strTitle%></font>
          </td>

          <!-- BOTTONE DI RITORNO -->
          <td class="LBG">
            <a href="javascript:history.go(-1);">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
            </a>
          </td>
        </tr>

      </table>
    </FORM>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    <br>
    <table cellspacing=2 cellpadding=2 width="70%">
<!--------------------- EVENTO ---------------------->
      <tr>
        <td class="l">Data Emissione</td>
        <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoNotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
        </td>
      </tr>
      <tr>
        <td class="l"><font class="label">Natura Provvedimento</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(eventoNotifica.getEvento().getDescrEsito())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Oggetto Procedimento</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(eventoNotifica.getEvento().getDescrMotivo())%>&nbsp;</font></td>
      </tr>
<!------------------- MESSAGGIO --------------------->
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
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrUfficioMittente() +" "+Messaggio.getDescrUfficioMittente())%>&nbsp;</font></td>
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
    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="ConfermaPresaInCaricoOrdinanza">
    <tr>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istanza.action.ActConfermaPresaInCarico">
    <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
<!------------------------------------------>
</form >

</table>
  </body>
</html>
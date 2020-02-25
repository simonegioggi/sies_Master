<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.jms.ICostantiJMS"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe"%>

<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="eventoNotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="lAttivita" scope="request" class="siap.siepe.attivita.model.AttivitaModel"/>
<jsp:useBean id="lRichiesta" scope="request" class="siap.siepe.richiesta.model.RichiestaModel"/>
<jsp:useBean id="lRelazione" scope="request" class="siap.siepe.relazione.model.RelazioneModel"/>

<%
String strTitle="Dettaglio Messaggio Trasmesso";
if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_ATTIVITA)==0 )
  strTitle = "Dettaglio Attivita Trasmessa";
else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_RICHIESTA_UEPE)==0 )
  strTitle = "Dettaglio Richiesta Trasmessa";
else if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_RELAZIONE_UEPE)==0 )
  strTitle = "Dettaglio Relazione Trasmessa";

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
			<!-- Include la sintesi del Soggetto/Fascicolo -->
    	<jsp:include page="<%=ICostantiFascicoloSiepe.PG_SINTESI_SOGG_FASCICOLI%>"/>

    <br>
    <table cellspacing=2 cellpadding=2 width="70%">
<!------------------- ATTIVITA --------------------->
<%
		if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_ATTIVITA)==0 )
		{%>
      <tr>
        <td class="l">Data Chiusura</td>
        <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAttivita.getDataChiusura(),"dd-MM-yyyy"))%></font>&nbsp;
        </td>
      </tr>
      <tr>
        <td class="l"><font class="label">Tipo Attività</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAttivita.getDescrTipoAttivita())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Esito Attività</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAttivita.getDescrEsitoAttivita())%>&nbsp;</font></td>
      </tr>
	<%}%>
<!------------------- RICHIESTA --------------------->
<%
		if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_RICHIESTA_UEPE)==0 )
		{%>
      <tr>
        <td class="l">Data Richiesta</td>
        <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRichiesta.getDataRichiesta(),"dd-MM-yyyy"))%></font>&nbsp;
        </td>
      </tr>
      <tr>
        <td class="l"><font class="label">Tipo Richiesta</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lRichiesta.getDescrTipoRichiesta())%>&nbsp;</font></td>
      </tr>
	<%}%>
<!------------------- RELAZIONE --------------------->
<%
		if (Messaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_RELAZIONE_UEPE)==0 )
		{%>
      <tr>
        <td class="l">Data Relazione</td>
        <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRelazione.getDataInserimento(),"dd-MM-yyyy"))%></font>&nbsp;
        </td>
      </tr>
      <tr>
        <td class="l"><font class="label">Titolo Relazione</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lRelazione.getNote())%>&nbsp;</font></td>
      </tr>
	<%}%>
<!------------------- MESSAGGIO --------------------->

			<tr><td class="Titolo" colspan=4>Dati Messaggio</td></tr>
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
    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="">
    <tr>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.fascicolo.action.ActLoadDettaglioFascicolo">
    <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
<!------------------------------------------>
</form >

</table>
  </body>
</html>
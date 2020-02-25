<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>

<jsp:useBean id="fascicolo" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="TornaQui"  scope="request" class="java.lang.String"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
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
            <font class="campo">Dettaglio Ricerca altre BDI</font>
          </td>
	  <!-- BOTTONE DI RITORNO -->
	  <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	</tr>

      </table>
    </FORM>
    <br>
    <table cellspacing=2 cellpadding=2>
    <!----------- MESSAGGIO --------------------->
      <tr>
        <td class="Titolo" colspan=4>Dati Messaggio</td>
      </tr>
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
        {%>
          <td class="l">In Attesa di risposta...&nbsp;</td>
      <%}else{%>
          <td class="lVerde"><font class="campo">Risposta Ricevuta&nbsp;</font></td>
      <%}%>
      </tr>
      <tr>
        <td class="l"><font class="label">Tipo Operazione</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrTipoOperazione())%>&nbsp;</font></td>
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


        <td class="lRosso">

        <table cellspacing=2 cellpadding=2>
        <tr>
        <td class="lRosso">
<%
        if (Messaggio.getMessaggioCorrelato() != null)
        {%>
          <%=Messaggio.getMessaggioCorrelato().getDescrEsito()%>&nbsp;
      <%}%>
      </tr>

      <tr>
	<td class="lRosso">
	  <font class="label">Procedimento N.</font>
	    <%=Messaggio.getChiaveAnnoSiep()%>
	    /
	    <%=Messaggio.getChiaveProgrSiep()%>
	  </td>
      </tr>
      <tr>
	<td class="lRosso">  <font class="label">Presso l'Ufficio </font>
	  <%=StringUtils.toStringJSP(Messaggio.getDescrUfficioDestinatario()+" "+Messaggio.getDescrSedeUfficioDestinatario())%>&nbsp;
	</td>
      </tr>
    </table>
    &nbsp;
  </td>
        </tr>
      </table>


  </body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.xml.TreeModel" %>

<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.sico.evento.model.XModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siepe.ricezioneatti.action.ICostantiRicezioneAtti"%>

<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="fascicolo" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="soggetto" scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="sentenza" scope="request" class="siap.siep.sentenza.model.SentenzaModel"/>

<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Richiesta Relazione</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>
  <body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Richiesta Relazione Ricevuta</font>
          </td>

      <!-- TOOLBAR HEADER -->
      <td class="LBG">
          <jsp:include page="<%=ICostantiRicezioneAtti.PG_TOOLBAR_HEADER_RICHIESTA_REL%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=Messaggio.getIdMessaggio()%>" />
          <jsp:param name="FlagVisto" value="<%=Messaggio.getFlagVisto()%>" />
          </jsp:include>
     </td>

<%
  		String lStato="Stato Messaggio : ";
  		if (Messaggio.getFlagVisto().compareTo("N") == 0) lStato+="Ricevuto";
  		else if (Messaggio.getFlagVisto().compareTo("V") == 0) lStato+="Preso in Visione";
  		else if (Messaggio.getFlagVisto().compareTo("S") == 0) lStato+="Preso in Carico";
  		else if (Messaggio.getFlagVisto().compareTo("R") == 0)
      {
        lStato+="Restituito al Mittente";
        if (Messaggio.getMessaggioCorrelato() != null)
        {
        	XModel lXMod = (XModel)Messaggio.getMessaggioCorrelato().getTreeModel().getModel();
					if (lXMod != null && lXMod.getMessage()!=null)
        		lStato += "  -    Motivo : "+lXMod.getMessage();
        }
      }
%>
     <td class="LBG">
       <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>news.gif" alt="<%=lStato%>" width="24" height="24" border="0">
     </td>

      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
         </tr>


      </table>
    </FORM>
      <jsp:include page="/jsp/files/siap/siepe/ricezioneatti/SintesiProcedimentoSiusRicevuto.jsp"/>
    <br>
    <table cellspacing=2 cellpadding=2>
<!----------- MESSAGGIO --------------------->
      <tr>
        <td class="Titolo" colspan=4>Oggetto della Richiesta</td>
      </tr>
      <tr>
        <td class="l"><font class="label">Oggetto</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(evento.getDescrMotivo())%>&nbsp;</font></td>
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
     <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="ConfermaPresaInCaricoRichiestaRel">
     <tr>
     <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
     <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >
</form >

</table>
  </body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>

<!--jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" /-->
<jsp:useBean id="fascicolo" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>

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
        <td class="l"><font class="campo">< %=StringUtils.toStringJSP(Messaggio.getJmsIdMessaggio())%>&nbsp;</font></td>
     </tr>
--%>
     <tr>
        <td class="l"><font class="label">Stato Messaggio</font></td>
<%
        if (Messaggio.getMessaggioCorrelato() == null)
        {%>
          <td class="lRosso">In Attesa di risposta...&nbsp;</td>
      <%}
        else
        {%>
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
        <td class="l">
<%
        if (Messaggio.getMessaggioCorrelato() != null)
        {%>
	  <font class="campo"><%=Messaggio.getMessaggioCorrelato().getDescrEsito()%>&nbsp;</font>
<%
	  SoggettoModel soggetto = fascicolo.getSoggetto();
	  SentenzaModel sentenza = fascicolo.getSentenza();
%>
	  <table cellspacing=0 cellpadding=0 width=95%>

	    <tr>
	      <td class="L">
		<font class="label">Procedimento N.</font>
		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>" title="Procedimento">
		  <%=fascicolo.getChiaveAnno()%>
		  /
		  <%=fascicolo.getChiaveProgr()%>
		</a>
	      </td>
	    </tr>
	    <tr>
	      <td class="L" width=100%><font class="label">Soggetto:</font>
	      <font class="campo">
		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>" title="Soggetto">
		  <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
		</a>
	      </font>&nbsp;
<%
	      if (soggetto.getSesso().compareTo("F")==0)
	      {%>
		<font class="label">nata il :</font>&nbsp;
	    <%}
	      else
	      {%>
		<font class="label">nato il :</font>&nbsp;
	    <%}
	      if(soggetto.getDataNascita() == null)
	      {
		if(soggetto.getDataNascitaPresunta().equals("S"))
		{%>
		  <font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
	      <%}else
		{%>
		  <font class="campo">***</font>&nbsp;
	      <%}
	      }else{%>
		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
	    <%}%>
	      <font class="label">in : </font>
	      <font class="campo">
<%
		if (soggetto.getDescrComuneNascita().compareTo("-")==0)
		{%>
		  <%=soggetto.getDescrStatoNascita()%>
	      <%}else{%>
		  <%=soggetto.getDescrComuneNascita()+ "  ("+soggetto.getCodProvinciaNascita()+")" %>
	      <%}%>
	      </font>
	    </td>
	  </tr>
	  <tr>
	    <td class="L">
	      <font class="campo"><%=sentenza.getDescrTipoProvvedimento()%></font>&nbsp;<font class="label">N.</font>
	      <font class="campo">
	        <%=sentenza.getAnnoSentenza()%> / <%=sentenza.getNumeroSentenza()%>&nbsp;
	      <font class="label">del</font>&nbsp;
		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
		  <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>
		</a>
	      </font>
	      &nbsp;<font class="label"> Emessa da: </font>
	      <font class="campo"><%=sentenza.getDescrTipoAutoritaEmittente()%></font>&nbsp;
<%
	      if (sentenza.getNumSezioneAutoritaEmittente() != null)
	      {%>
		<font class="label">(Sez.</font> <font class="campo"><%=sentenza.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
	    <%}%>
		<font class="label"> di </font>
		<font class="campo"><%=sentenza.getDescrLuogoEmittente()%></font>
	    </td>
	  </tr>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--  
	  //  modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
	  tr>
	    <td class="L">
	      <font class="label">Data irrevocabilità : </font>&nbsp;
	      <font class="campo">< %=DateUtils.getDateToString(sentenza.getDataIrrevocabilita(), "dd-MM-yyyy")%></font>
	    </td>
	  </tr>
--%>
	</table>

      <%}%>&nbsp;
      			</td>
      		</tr>
      	</table>
		<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="ConfermaPresaInCaricoIstanza">
			<!--
			<tr>
	        	<td>
	            	<input class=bottone  type="submit" value="Conferma Presa in Carico">
	          	</td>
	        </tr>
	        -->
	        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istanza.action.ActConfermaPresaInCarico">
	        <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
		</form >
	</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica" %>
<%@ page import="siap.sico.camponota.model.CampoNotaModel" %>

<jsp:useBean id="IDEvento" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="eventonotifica" 	scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="competenza" 		scope="request" class="siap.siep.competenza.model.CompetenzaModel"/>
<jsp:useBean id="fascCompetenza" 	scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Richieste Atti e Trasmissione per Competenza </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
 
</head>
  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        String lAction = new String();
        lAction = "siap.siep.richiesta.action.ActTrasferisciRigettoRichiestaAtti";
%>
        <font class="campo">Trasmissione Rigetto Richiesta Atti per Competenza</font>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadTrasferisciRigetto" onsubmit="document.forms[0].go.disabled=true">
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Destinatario </td>
        <td class="L"><font class="campo">
         <%=competenza.getDescrTipoAutoritaComp()%></font>
        </td>
      </tr>
      <tr>
        <td class="l">Sede Destinatario </td>
        <td class="L"><font class="campo">
            <%=competenza.getDescrLuogoAutoritaComp()%></font>
        </td>
      </tr>
    </table>  
<% if(eventonotifica.getCampoNote()!= null && eventonotifica.getCampoNote().length >0 && eventonotifica.getCampoNote()[0] != null && eventonotifica.getCampoNote()[0].getDescr() != null)
   {	%>
	<br>	
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Motivazioni</td>
        <td  class="L" >
     		<TEXTAREA title="motivazioni" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols=90 rows=5 readonly><%=StringUtils.toStringJSP(eventonotifica.getCampoNote()[0].getDescr())%></textarea>
    	</td>
      </tr>
    </table>  			
	<br>
<%	} %>
	<table>	           
      <tr>
        <td>
		<br/>
          <input name="go" class="bottone"  type="submit" value="Conferma">
        </td>
      </tr>      
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
      <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=IDEvento%>">
    </table>
  </form>

</body>
</html>
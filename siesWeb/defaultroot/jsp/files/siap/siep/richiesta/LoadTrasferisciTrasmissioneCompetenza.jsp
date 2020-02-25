<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<jsp:useBean id="IDEvento" scope="request" class="java.lang.String"/>
<jsp:useBean id="competenza" scope="request" class="siap.siep.competenza.model.CompetenzaModel"/>
<jsp:useBean id="fascCompetenza" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<%
// Seguito Atti
String lTitolo="Trasferimento Trasmissione per Competenza";
if(eventonotifica!=null && eventonotifica.getEvento()!=null && eventonotifica.getEvento().getCodMotivo()!=null )
{
	  if(eventonotifica.getEvento().getCodMotivo().compareTo("0740")==0 )
	  {
		  lTitolo +=" - Seguito Atti";
	  }
}
%>
<html>
<head>
  <title>[S.I.E.S.] - Gestione Trasmissione per Competenza </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
 
</head>
  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        String lAction = new String();
        lAction = "siap.siep.richiesta.action.ActTrasferisciTrasmissioneCompetenza";
%>
        <font class="campo"><%=lTitolo%></font>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="TrasferisciCompetenza" onsubmit="document.forms[0].go.disabled=true">
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
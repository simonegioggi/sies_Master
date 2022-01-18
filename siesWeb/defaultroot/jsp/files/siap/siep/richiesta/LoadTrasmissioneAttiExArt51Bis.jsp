<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>

<jsp:useBean id="UfficioDestinatario" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="eventonotifica" 	scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="UEPE" 				scope="request" class="java.lang.String"/>

<jsp:useBean id="ufficiouds"      scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficiotds"         scope="request" class="siap.sico.ufficio.model.UfficioModel"/>

<!-- LoadTrasmissioneAttiExArt51Bis -->
<html>
<head>
  <title>[S.I.E.S.] - Trasmissione Atti Richieste Cessazione/prosecuzione Mis Alt - Ex art 51 Bis </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
</head>
  <body class="corpo">
  <table>
  	<tr>
  		<td class="LBG">
  			<a href="Javascript:window.print();">
  				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
  			</a>
  		</td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
String lAction = new String();
// lAction = "siap.siep.richiesta.action.ActLoadConfermaTrasferisciAttiExArt51Bis";
lAction = "siap.siep.richiesta.action.ActConfermaTrasmissioneAttiExArt51Bis";
String Rich = eventonotifica.getEvento().getDescrMotivo();
%>
        	<font class="campo">Trasferimento Atti - <%=Rich%> </font>
		</td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadTrasferisciAtti" onsubmit="document.forms[0].go.disabled=true">
    <table cellspacing=2 cellpadding=2>
      <tr>

<%
if (ufficiouds != null && ufficiouds.getDescrComune() != null && !ufficiouds.getDescrComune().equals("")) {
%>
<!-- 		MAGISTRATO di SORVEGLIANZA -->
		<td class="l">Destinatario: <%=UfficioDestinatario.getDescrTipoUfficio()%> di</td >
        	<td class="L"> 
			<input readonly type="text" title="Tipo Sede Destinatario" name="<%=ICostantiNotifica.CAMPO_SEDE_MDS %>" value=<%=StringUtils.toStringJSP(ufficiouds.getDescrComune())%> maxlength="35" size="25">
			</td>
<%
} else if(ufficiotds != null && ufficiotds.getDescrComune() != null && !ufficiotds.getDescrComune().equals("")) {
%>
<!-- 		TRIBUNALE di SORVEGLIANZA -->
		<td class="l">Destinatario: <%=UfficioDestinatario.getDescrTipoUfficio()%> di</td>
        	<td class="L"> 		
			<input readonly type="text" title="Tipo Sede Destinatario" name="<%=ICostantiNotifica.CAMPO_SEDE_TDS %>" value=<%=StringUtils.toStringJSP(ufficiotds.getDescrComune())%> maxlength="35" size="25">
			</td>
<%
}
%>		        
      </tr>
	  <tr><td>&nbsp;</td></tr>	
      <tr>
        <td>
          <input name=go class=bottone  type="submit" value="Conferma Trasmissione">
        </td>
      </tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento() %>">
<input type="HIDDEN" name="CodTipoUfficioDestinatario" value="<%=UfficioDestinatario.getCodTipoUfficio()%>">
<input type="HIDDEN" name="CodLuogoDestinatario" value="<%=UfficioDestinatario.getDescrComune()%>">			    
</form>
</body>
</html>
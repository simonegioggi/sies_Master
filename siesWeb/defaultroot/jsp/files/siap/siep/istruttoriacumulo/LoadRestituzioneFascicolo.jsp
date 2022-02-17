<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.controller.IUfficio"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.fascicolo.model.DettaglioFascicoloModel"%>

<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.util.ParserMessage"%>

<jsp:useBean id="IstruttoriaCumulo" 	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="IdMessaggio" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="messaggio" 			scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="UfficioDestinatario" 	scope="request" class="siap.sico.ufficio.model.UfficioModel"/>

<%
// Stabilisce la funzione da innescare dopo il messaggio di restituzione Atti
String newPage  = (String)request.getAttribute(IWebConstants.GOTO_PAGE);
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Cumulo - Restituzione Atti Ricevuti per Competenza </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
function verify() {
	return true;
}
</script>
</head>
<body class="corpo">
<table>
    <tr>
      	<td class="LBG">
      		<font class="label">Funzione :</font>&nbsp;&nbsp;
		    <font class="campo">Restituzione Atti </font>
      	</td>
	</tr>
</table>
<%--
Ticket#202202160112 -€” cumulo restituzione atti pervenuti: si commenta l'include 
che va in errore in caso di mancanza del fascicolo in sessione (se si proviene dalla ricerca atti presi in carico) 
e che comunque visaulizza dati non pertinenti.       
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
--%>
<FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoriacumulo.action.ActRestituzioneFascicolo" >
<input type="HIDDEN" name="<%=IWebConstants.GOTO_PAGE%>"  value="<%=newPage%>">
<input type="hidden" name="<%=ICostantiMessaggio.CAMPO_COD_UFFICIO_DESTINATARIO%>" 		value="<%=messaggio.getCodUfficioMittente()%>">
<input type="hidden" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" 					value="<%=messaggio.getIdMessaggio()%>">
<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=StringUtils.toStringJSP(IstruttoriaCumulo.getIdIstruttoriaCumulo(),"")%>">
<table width="80%" >
	<tr>
  		<td class="l" width="20%">Destinatario</td>
  		<td class="L">
    		<input type="text" readonly name="DescUfficioDest" value="<%=StringUtils.toStringJSP(UfficioDestinatario.getDescrTipoUfficio(), "")%>" size="80">
		</td>
 	</tr>
 	<tr>
   		<td class="l" width="20%">Sede</td>
   		<td class="L">
     		<input type="text" readonly name="DescSedeDest" value="<%=StringUtils.toStringJSP(UfficioDestinatario.getDescrComune(), "")%>" size="60">
		</td>
 	</tr>    
 	<tr>
		<td class="l" width="20%">Motivazioni</td>
		<td  class="L">
      		<TEXTAREA title="Note" name="<%=ICostantiIstruttoriaCumulo.CAMPO_NOTE%>" cols=60 rows=3></textarea>
    	</td>
  	</tr>
	<tr>
     	<td class="lNoBord">
     		<br><br>
    		<INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="verify()">
		</td>
	</tr>
</table>
</form>
</body>
</html>
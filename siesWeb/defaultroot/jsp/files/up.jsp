<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.security.model.FunctionModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<jsp:useBean id="FunRadiceMenuSceltaRapida" scope="session" class="f3b.security.model.FunctionModel" />
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="ErroreBatchPagoPa" scope="session" class="java.lang.String" />
<jsp:useBean id="BatchPagoPa" scope="session" class="siap.siep.pagoPaBatch.model.BatchPagopaModel" />

<html>
<head>
<META http-equiv=Content-Type content="text/html; charset=windows-1252">
<title>Up</title>
<link rel="STYLESHEET" type="text/css" href="/css/style.css">
<script language="JavaScript">
var icontahelp = 0;
function openHelp(ActionName) {
	var desktop;
	//==========================================================================
	//n.b. Sulla request viene passato un parametro fittizio icontahelp per 
	//     evitare che la pagina resti nella cache del browser o del Proxy.
	//     Infatti ci si è accorti che passando per il proxy la pagina restituita
	//     era sempre la stessa. Aggiungendo un parametro random si genera una
	//     GET sempre diversa per cui la pagina restituita non viene "meccia" mai 
	//     con quella nella cache.
	//==========================================================================
	var icontahelp=Math.random();
	desktop=window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>="+ActionName+"&ContaHelp="+icontahelp, "HelpOnLine", "toolbar=no,location=yes,status=no,menubar=no,resizable=yes,scrollbars=yes,top=0,left=0");
	desktop.window.resizeTo(screen.availWidth,screen.availHeight);
	desktop.focus();
}
</script>
</head>
<body leftmargin="0" class=menu topmargin="0" marginwidth="0">
<div style="position: absolute; left: 0px; top: 5px;">
<table cellspacing=0 cellpadding="0" width=100% border=1>
	<tr>
  		<td width="50%">
    		<table width=100% cellpadding="0" cellspacing="0" border=1 bordercolor="#BEC6FC">
      			<tr>
        			<td width=10% bordercolor=#BEC6FC>
          				<font class=label>Utente:</font>
          			</td>
        			<td width=90% bordercolor=#BEC6FC>
          				<font class=campo><%=UtenteConnesso.getNome()+" "+UtenteConnesso.getCognome()%> (<%=UtenteConnesso.getUserId()%>)</font>
					</td>
				</tr>
				<tr>
  					<td bordercolor=#BEC6FC><font class=label>Ufficio:</font></td>
<%
UfficioModel lUfficioUtente = UtenteConnesso.getUfficioUtente();
%>
					<td bordercolor=#BEC6FC>
 						<font class=campo><%=lUfficioUtente.getDescrTipoUfficio() +" - "+ lUfficioUtente.getDescrComune()%></font>
      				</td>
    			</tr>
  			</table>
		</td>
		<td width="50%" bordercolor="#BEC6FC" align="right">
<%
//==========================================================================
// Verifica abilitazione funzione help On-line per l'utente connesso
//==========================================================================
ArrayList lFunFiglie = FunRadiceMenuSceltaRapida.getDaughtersFunctions();
Iterator lIter = lFunFiglie.iterator();
FunctionModel lFun = null;
while (lIter.hasNext()) {
	lFun = (FunctionModel)lIter.next();
	String label = null;
	String image = null;
	label = lFun.getLabelFunction();
	if (lFun.getImmagine()!=null && lFun.getImmagine().length() > 0) {
		image = lFun.getImmagine();
	} else {
	  	image = "/images/help.gif";
	}
 	if (lFun.getNameAction().equals("siap.sico.helponline.action.ActLoadDettaglioHelponline")) {
 		// L'help on line viene aperto a tutto schermo su una pagina distinta come ultimo link allineato a destra
%>
			<a href="Javascript:openHelp('<%=lFun.getNameAction()%>');">
  				<img align="middle" src="/images/help.gif" width="32" height="32" alt="" border="0" title="Help On Line">
 				<font class=label>Help On Line</font>
 			</a>
<%
	}
}
%>

<%-- 
MEV_2023-33 
Se Amministratore di Sistema (99) e sono presenti errori o segnalazioni nell'ultimo 
lancio del batchPagopa si visualizza l'icona di alert
--%>
<% 
if ( "S".equals(ErroreBatchPagoPa)
		&& (   UtenteConnesso.getUserProfile().getProfileId().intValue() == 99
				|| UtenteConnesso.getUserProfile().getProfileId().intValue() == 90
		    || UtenteConnesso.getUserProfile().getProfileId().intValue() == 4
		    || UtenteConnesso.getUserProfile().getProfileId().intValue() == 40
		    || UtenteConnesso.getUserProfile().getProfileId().intValue() == 50
		   )
   ) 	
{%>
			<a href="<%=IWebConstants.PG_MAIN+"?"+IWebConstants.ACTION_FIELD+"=siap.siep.pagoPA.action.ActLoadAvvisoBatchPagoPA&IdBatchPagoPa="+BatchPagoPa.getIdBatchPagopa()%>" target="body">
  				<img src="/images/attenzione.jpg" alt="" width="32" height="32" border="0" align="middle" title="Avviso Batch PagoPA">
 				<font class=label>Avviso Batch PagoPA</font>
 			</a>
<% } %>
<%-- MEV_2023-33 - FINE --%>

			<a href="<%=IWebConstants.PG_MAIN+"?"+IWebConstants.ACTION_FIELD+"=siap.sico.utente.action.ActLoadModificaPassword"%>" target="body">
				<img src="/images/Personal.gif" alt="" width="32" height="32" border="0" align="middle">
				<font class=label>Cambio Password</font>
			</a>
			<a href="<%=IWebConstants.PG_MAIN+"?"+IWebConstants.ACTION_FIELD+"=siap.sico.security.action.ActLogout"%>" target="_top">
				<img align="middle" src="/images/logout.gif" width="32" height="32" alt="" border="0">
				<font class=label>Logout</font>
			</a>
			<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
			<%--a href="<%=IWebConstants.PG_MAIN+"?"+IWebConstants.ACTION_FIELD+"=siap.sico.web.ActLoadHome"%>"" target="_top"><img align="middle" src="/images/home.gif" width="32" height="32" alt="" border="0"><font class=label>Home</font></a--%>
			<a href="/frame.htm" target="_top">
				<img align="middle" src="/images/home.gif" width="32" height="32" alt="" border="0">
				<font class=label>Home</font>
			</a>
		</td>
	</tr>
</table>
</div>
</body>
</html>
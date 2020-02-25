<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.ArrayList"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.security.model.FunctionModel"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<jsp:useBean id="FunRadiceMenuVrt" scope="session" class="f3b.security.model.FunctionModel"/>
<jsp:useBean id="FunRadiceMenuSceltaRapida" scope="session" class="f3b.security.model.FunctionModel"/>

<HTML>
<HEAD>
<META http-equiv=Content-Type content="text/html; charset=windows-1252">
<link rel="STYLESHEET" type="text/css" href="/css/menu.css">
<link rel="STYLESHEET" type="text/css" href="/css/style.css">
<link rel="STYLESHEET" type="text/css" href="/css/buttons_menu.css">
<script language="JavaScript">
var desktop;
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
function BoldIT(id) {
	var currentLink=eval(document.id);
	for (var i=0;i<document.links.length;i++) {
			document.links(i).style.color = 'navy';
	}
	document.links(id*1).style.color = 'blue';
}

function ViewSessionState(subsystem) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.sessionstate.action.ActVediSessione"+subsystem, "Session_state","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=530,top=0,left=0");
}

function SendMailToHelpDesk() {
  	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.help.action.LoadSendMailToHelpDesk", "HelpDesk","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=530,top=0,left=0");
}

// MEV10-s3: aggiunta funzione per invio segnalazione
function InviaSegnalazione() {
	window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.inviosegnalazione.action.PreparaInvioSegnalazione", "InvioSegnalazione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=800,height=600,top=0,left=0");
}

function PrenBdmc() {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.bdmc.sbpren.action.ActCollegamentoBdmcSsl", "Session_state","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,top=0,left=0");
}

<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
function over_effect(e, state) {
	if (document.all)
		source4 = event.srcElement;
	else if (document.getElementById)
		source4 = e.target;
	if (source4.className == "menulines")
		source4.style.borderStyle = state;
	else{
		while(source4.tagName != "TABLE"){
			source4 = document.getElementById ? source4.parentNode : source4.parentElement;
			if (source4.className == "menulines")
				source4.style.borderStyle = state;
		}
	}
}

function ViewCalcoloRapidoDellaPena() {
	desktop=window.open("/jsp/Main.jsp?Action=siap.siep.calcolopena.action.ActLoadCalcolatrice", "Calcolatrice","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=850,height=580,top=0,left=0");
}
</script>
</HEAD>

<BODY class=menu marginheight="0" topmargin="0" leftmargin="0">
<table width="100%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
<%
Iterator lIterVociMenu = FunRadiceMenuVrt.getDaughtersFunctions().iterator();
FunctionModel lFun = null;
int i = 0;
// MEV10-s3: refactoring pagina ed aggiunto link per invio segnalazioni
String stato = "";
UtenteModel lUteMod=(UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
UfficioModel lUffMod= lUteMod.getUfficioUtente();
String codUff = new String(lUffMod.getCodTipoUfficio());
String img = new String("");
while (lIterVociMenu.hasNext()) {
	lFun = (FunctionModel) lIterVociMenu.next();
	if (lFun.getFunctionId().compareTo(new BigDecimal(70101016)) == 0) {
%>
	<tr>
       	<td class="menulines" align="left">
           	<a style="" onclick="Javascript:BoldIT('<%=i%>');" href="Javascript:PrenBdmc();">
             		Prenotazione Misure Cautelari
           	</a>
         	</td>
	</tr>
<%
		// 20190814 [SG]: incremento se e solo se inserisco riga
		i++;
	} else {
		if (lFun.getFunctionId().compareTo(new BigDecimal(90110690)) == 0 && !codUff.equals("TDSM")) {
			// per gli uffici diversi da TDSM la funzione 
			// "Sentenze" non viene visualizzata
		} else {
%>
	<tr>
 		<td class="menulines">
   			<a style="" onclick="Javascript:BoldIT('<%=i%>');" href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.security.action.ActLoadOrizontalMenu&<%=ICostantiSecurity.CAMPO_ID_FUNZIONE%>=<%=lFun.getFunctionId()%>" target="navigazione"><%=lFun.getLabelFunction()%></a>
 		</td>
	</tr>
<%
			// 20190814 [SG]: incremento se e solo se inserisco riga
			i++;
		}
	}
}
if (lUteMod.getUserProfile().getProfileId().intValue() != 99
		&& lUteMod.getUserProfile().getProfileId().intValue() != 90) { // USERPROFILE
	if (codUff.equals("UEPE") || codUff.equals("UEPESS")) {
		stato = "SIEPE";
  	} else if (codUff.startsWith("TDS") || codUff.startsWith("UDS")) {// MEV10-s3: variato "equals" con "startsWith"
  		stato = "SIUS";
	} else if (codUff.equals("PGCAP") || codUff.equals("PM") || codUff.equals("PMM")) {
		stato = "SIEP";
 	} else {
 		stato = "SIGE";
	}
	if (codUff.equals("UEPE") || codUff.equals("UEPESS")) {
%>
	<tr><td><br><br><br>&nbsp;</td></tr>
    <tr>
      	<td class="menulines" align="left">
        	<a style="" onclick="Javascript:BoldIT('<%=i%>');" href="Javascript:ViewSessionState('SIEPE');">
          		<img align="middle" src="../../images/rosa3.gif" width="32" height="32" alt="" border="0">
          		&nbsp;&nbsp;&nbsp;&nbsp;Dati Correnti
        	</a>
      	</td>
	</tr>
<%
	// MEV10-s3: variato "equals" con "startsWith"
	} else if (codUff.startsWith("TDS") || codUff.startsWith("UDS")) {
%>
    <tr><td><br><br><br>&nbsp;</td></tr>
    <tr>
      	<td class="menulines" align="left">
        	<a style="" onclick="Javascript:BoldIT('<%=i%>');" href="Javascript:ViewSessionState('SIUS');">
          		<img align="middle" src="../../images/rosa3.gif" width="32" height="32" alt="" border="0">
          		&nbsp;&nbsp;&nbsp;&nbsp;Dati Correnti
        	</a>
      	</td>
    </tr>
<%
	} else if (codUff.equals("PGCAP") || codUff.equals("PM") || codUff.equals("PMM")) {
%>
    <tr><td><br><br><br>&nbsp;</td></tr>
    <tr>
      	<td class="menulines" align="left">
        	<a style="" onclick="Javascript:BoldIT('<%=i%>');" href="Javascript:ViewSessionState('SIEP');">
          		<img align="middle" src="../../images/rosa3.gif" width="32" height="32" alt="" border="0">
          		&nbsp;&nbsp;&nbsp;&nbsp;Dati Correnti
        	</a>
      	</td>
    </tr>
<%
	} else {
%>
    <tr><td><br><br><br>&nbsp;</td></tr>
    <tr>
      	<td class="menulines" align="left">
        	<a style="" onclick="Javascript:BoldIT('<%=i%>');" href="Javascript:ViewSessionState('SIGE');">
          		<img align="middle" src="../../images/rosa3.gif" width="32" height="32" alt="" border="0">
          		&nbsp;&nbsp;&nbsp;&nbsp;Dati Correnti
       		</a>
      	</td>
   	</tr>
<%
		//////// Calcolatrice
 		//==========================================================================
 		// Ciclo di caricamento dei bottoni di accesso rapido previsti
 		//==========================================================================
 		ArrayList lFunFiglie = FunRadiceMenuSceltaRapida.getDaughtersFunctions();
 		Iterator lIter = lFunFiglie.iterator();
 		FunctionModel lFunRapid = null;
 		while (lIter.hasNext()) {
			lFunRapid = (FunctionModel) lIter.next();
			String label = null;
			String image = null;
			label = lFunRapid.getLabelFunction();
			if (lFunRapid.getImmagine() != null && lFunRapid.getImmagine().length() > 0)
		  		image = lFunRapid.getImmagine();
			else
		  		image = "/images/help.gif";
			if (lFunRapid.getNameAction().equals("siap.siep.calcolopena.action.ActLoadCalcolatrice")) { // la calcolatrice viene aperta come pop up per essere sempre disponibile
%>
	<tr>
		<td class="menulines" align="left">
 			<a href="Javascript:ViewCalcoloRapidoDellaPena();">
   				<img align="middle" src="<%=image%>" width="32" height="32" alt="" border="0" title="<%=label%>">
				&nbsp;&nbsp;&nbsp;&nbsp;Calcolo pena
 			</a>
		</td>
 	</tr>
<%
			} // end if
		}  // end while
		///////	fine Calcolatrice	
	} // end else
	// INVIO SEGNALAZIONI VIA MAIL
	if (!"SIEPE".equals(stato)) {
%>
	<tr><td><br>&nbsp;</td></tr>
	<table cellspacing="0" align="center" width="98%">
	  	<tr>
	    	<td class="menulines" align="left" style="border-right: none; width: 25%;">
	      		<a style="" onclick="Javascript:BoldIT('<%=i%>');" href="Javascript:InviaSegnalazione();">
	      			<img align="middle" src="../../images/e_mail.png" width="32" height="32" alt="" border="0"/>
	      		</a>
	    	</td>
	    	<td class="menulines" align="left" style="border-left: none;">
	      		<a style="" onclick="Javascript:BoldIT('<%=i%>');" href="Javascript:InviaSegnalazione();">
	         		Richieste di supporto e di assistenza formativa
	      		</a>
	    	</td>
	  	</tr>
  	</table>
<%
	}
} // end if USERPROFILE
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%-- Commentato Viviana-Daniele 20/06/2006
<tr><td><br>&nbsp;</td></tr>
<tr>
	<td class="menulines" align="left">
		<a onclick="Javascript:BoldIT('< %=i%>');" href="Javascript:SendMailToHelpDesk();">
			<img align="middle" src="../../images/logobull1.gif" width="32" height="32" alt="Segnalazione all'HelpDesk" border="0">
			&nbsp;&nbsp;&nbsp;&nbsp;Segnalazioni anomalie
		</a>
	</td>
</tr>
--%>
</table>
</BODY>
</HTML>
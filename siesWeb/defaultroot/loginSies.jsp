<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV INTEGRAZIONE SIES ADN: questa era la pagina di login originale --%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.utenzaAdn.util.UtenzaAdnUtils"%>
<%@ page import="siap.sico.utenzaAdn.model.AssocUtenteSiesAdnModel"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.jms.config.JMSProperties"%>
<%@ page import="siap.sico.versione.util.VersionProperties"%>

<%@ page language="java" session="true" errorPage="/jsp/ErrorPage.jsp"%>

<jsp:useBean id="username" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="usernameDB" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="msg" 			scope="request" class="java.lang.String"/>

<%
String Server  = JMSProperties.getInstance().getProperty("JMS_LOCAL_MITTENTE");
String Version = VersionProperties.getVersion();
JMSProperties.getInstance().getChekProgressivo();
%>

<html>
<head>
<title>Login S.I.E.S.</title>
<link rel="stylesheet" type="text/css" href="/css/style.css">

<script language=Javascript>
function exLogin() {
	document.FormLogin.submit();
	document.FormLogin.<%=ICostantiSecurity.CAMPO_PASSWORD%>.value = "";
}

function exLoginSies(e) {
	document.FormLoginSies.submit();
}

function controllaMsg() {
<%
if (Utils.isPresent(msg)) {
%>
	alert("<%=msg%>");
<%
}
%>
}

function setUserIdPwd(userId, pwd) {
	document.FormLoginSies.<%=ICostantiSecurity.CAMPO_USER_ID%>.value = userId;
	document.FormLoginSies.<%=ICostantiSecurity.CAMPO_PASSWORD%>.value = pwd;
}

function espandi() {
	var node1 = document.getElementById('login');
	if (node1.style.visibility == 'hidden')
		node1.style.visibility = 'visible';
	else
		node1.style.visibility = 'hidden';
	var node2 = document.getElementById('modo');
	if (node1.style.visibility == 'hidden')
		node2.style.background = "url('/images/expand.gif') no-repeat left top #EEEEEE";
	else
		node2.style.background = "url('/images/collapse.gif') no-repeat left top #EEEEEE";
}

function esci() {
<%
String thisServer = request.getServerName();
int thisServerPort = request.getServerPort();
String thisServerProtocol = request.getScheme();
String s = thisServerProtocol + "://" + thisServer + ":" + thisServerPort + "/";
%>
	location.replace("<%=s%>");
}

<%-- 20260317 [SG]: Aggiunta funzione per bloccare pressione tasto INVIO che scatenava login multipli --%>
function blockEnter(e) {
	e = e || window.event;
	var key = e.keyCode || e.which || e.charCode;
	if (key == 13) {
		if (e.preventDefault) e.preventDefault();
		if (e.stopPropagation) e.stopPropagation();
		e.returnValue = false;
		e.cancelBubble = true;
		return false;
	}
}
document.onkeydown  = blockEnter;
document.onkeypress = blockEnter;
document.onkeyup    = blockEnter;
</script>
<script language="JavaScript" src="/html/SubmitIT.js"></script>
</head>

<body bgcolor="#0383C0" topmargin="0" marginwidth="0" leftmargin="0" marginheight="0" background="/images/bckline.gif" onload="javascript:controllaMsg();" onkeydown="return blockEnter(event);" onkeypress="return blockEnter(event);">
<table height="85%" width="100%">
   	<tr>
   		<td width="100%" height="100%" align="center" valign="middle">
       		<table border="1" bordercolor="#ffffff" cellpadding="0" cellspacing="0" width="800" align="center">
	  			<tr>
           			<td width="700" height="75" valign="top" class="rNoBord" background="/images/login.gif">
            			<div align=left style="position:relative; left=490px;">
            				<br>
            				<table>
            					<tr>
            						<td>
            							<strong>
											<font color="#FFFFFF" size="2">Server&nbsp;di&nbsp;:&nbsp;&nbsp;&nbsp;<%=Server%><br>Versione&nbsp;:&nbsp;&nbsp;&nbsp;<%=Version%></font>
			              				</strong>
            						</td>
            						<td>&nbsp;</td><td>&nbsp;</td>
            						<td class="LBG">
							          	<a href="javascript:esci();">
							            	<img align="middle" src="/images/logout.gif" width="32" height="32" alt="Torna alla Pagina Iniziale" border="0">
						            		<font color="#0000FF" size="2" style="font-weight: bold;">Logout</font>
							          	</a>
							        </td>
            					</tr>
            				</table>
            			</div>
	            	</td>
            	</tr>
            	<tr>
            		<td>
            			<br>
            			<center><strong><font color="#FFFFFF">Benvenuto <%=username%></font></strong></center>
            			<br>
            			<%-- FORM 0: accesso effettivo al SIES --%>
            			<%-- onkeyup="javascript:if(window.event.keyCode==13){exLoginSies();}" --%>
            			<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="FormLoginSies" target="_blank">
            				<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.security.action.ActLogin">
           					<input type="HIDDEN" name="<%=ICostantiSecurity.CAMPO_USER_ID%>" value="">
            				<input type="HIDDEN" name="<%=ICostantiSecurity.CAMPO_PASSWORD%>" value="">
            				<input type="HIDDEN" name="usernameDB" value="<%=usernameDB%>">
<%
// 3)
List<AssocUtenteSiesAdnModel> listaUtenzeAdn = UtenzaAdnUtils.verificaAssociazioneSiesAdn(usernameDB);
Iterator<AssocUtenteSiesAdnModel> i = listaUtenzeAdn.iterator();
List<UtenteModel> utenti = new ArrayList<UtenteModel>();
while (i.hasNext()) {
	AssocUtenteSiesAdnModel ausam = i.next();
	UtenteModel um = new UtenteModel();
	um.setUserId(ausam.getUteCodUtente());
	um = UtenzaAdnUtils.preLogin(um, false);
	utenti.add(um);
}
if (!utenti.isEmpty()) {
	Iterator<UtenteModel> it = utenti.iterator();
	while (it.hasNext()) {
		UtenteModel utente = it.next();
%>
            					<center><strong>
            						<font color="#FFFFFF"><%-- Hai effettuato con successo l'associazione dell'Utenza:<br> --%>
            							<%=utente.getUserId()%>&nbsp;-&nbsp;<%=utente.getUfficioUtente().getDescrTipoUfficio()%>
            							&nbsp;di&nbsp;<%=utente.getUfficioUtente().getDescrComune()%>
            						</font>&nbsp;
									<button class="siesbutton" onclick="Javascript:setUserIdPwd('<%=utente.getUserId()%>', '<%=utente.getPwd()%>'); exLoginSies(event);">ENTRA</button>
            					</strong></center>
<%
	}
}
%>
            			</form>
            			<%-- FORM 1: associazione SIES-ADN --%>
            			<%-- onkeyup="javascript:if(window.event.keyCode==13){exLogin();}" --%>
            			<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="FormLogin">
	            			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.utenzaAdn.action.ActAssocUtenteSiesAdn">
	            			<input type="HIDDEN" name="usernameDB" value="<%=usernameDB%>">
	            			<center>
            				<input type="button" id="modo" name="modo" value="Seleziona/Configura un Account" onclick="javascript:espandi();"
            					style="background-image:url('<%=IWebConstants.IMAGES_DIR%>expand.gif'); background-repeat: no-repeat; background-position: left top; border-radius: 10px;">
	            			<div id="login" style="visibility: hidden; width:100%;">
	            				<br>
	            				<table cellpadding="0" cellspacing="0" width="250" align="center">
	  								<tr>
           								<td>
				            				<strong>
					              				<font color="#FFFFFF">Utente :</font>
					            			</strong>
					            		</td>
					            		<td>
					            			<input type="text" name="<%=ICostantiSecurity.CAMPO_USER_ID%>" style="border: 0px none; color: #044A7E;" onChange="Javascript:<%=ICostantiSecurity.CAMPO_PASSWORD%>.value='';">
            							</td>
            						</tr>
            						<tr>
            							<td>
            								<strong>
					              				<font color="#FFFFFF">Password :</font>
					            			</strong>
					            		</td>
					            		<td>
					            			<input type="password" name="<%=ICostantiSecurity.CAMPO_PASSWORD%>" style="border: 0px none; color: #044A7E;">
            							</td>
            						</tr>
            						<tr>
            							<td>&nbsp;</td>
            							<td align="center">
											<button class="siesbutton" onclick="Javascript:exLogin();">LOGIN</button>
            							</td>
            						</tr>
            					</table>
            				</div>
            				</center>
	            		</form>
       				</td>
       			</tr>
			</table>
       		<p style="margin-top: 0; margin-bottom: 0">&nbsp;</p>
       		<div align="center" style="width: 800">
       			<div align="left">
           			<table border="1" width="300" id="table1" cellspacing="0" cellpadding="0" bordercolor="#FFFFFF" bgcolor="#0383C0">
       					<tr>
               				<td>
               					<p align="center" style="margin-top: 0; margin-bottom: 0"/>
         						<strong><font color="#FFFFFF" size="3">COMUNICAZIONI DI SERVIZIO</font></strong>
         						<hr color="#FFFFFF" width="100%" size="1" align="center"/>
               					<iframe name="comunicazioni" marginwidth="1" marginheight="1" height="200" width=800 style="border:0" frameborder="0" src="<%="http://"+request.getServerName()+":"+request.getServerPort()%>/comunicazioni/news.htm" scrolling="no">
               						Il browser in uso non supporta frame non ancorati oppure è configurato in modo che i frame non ancorati non siano visualizzati.
               					</iframe>
               				</td>
           				</tr>
           			</table>
       			</div>
       		</div>
		</td>
	</tr>
</table>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV INTEGRAZIONE SIES ADN: modificata la pagina di login per accedere dapprima con le credenziali ADN --%>
<%@ page import="f3b.web.IWebConstants"%>
<%-- <%@ page import="f3b.util.F3BProperties"%> --%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.versione.util.VersionProperties"%>
<%@ page import="siap.jms.config.JMSProperties"%>

<%@ page language="java" session="true" errorPage="/jsp/ErrorPage.jsp"%>

<%
String server  = JMSProperties.getInstance().getProperty("JMS_LOCAL_MITTENTE");
// String version = F3BProperties.getProperty("CurrentVersion");
String version = VersionProperties.getVersion();
JMSProperties.getInstance().getChekProgressivo();
%>

<html>
<head>
<title>Login S.I.E.S. - ADN</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<script language=Javascript>
function exLogin() {
	if (document.FormLogin.<%=ICostantiSecurity.CAMPO_USER_ID%>.value == "") {
		alert("Attenzione! Campo User ADN Obbligatorio.")
		document.FormLogin.<%=ICostantiSecurity.CAMPO_USER_ID%>.focus();
		return false;
	} else if (document.FormLogin.<%=ICostantiSecurity.CAMPO_PASSWORD%>.value == "") {
		alert("Attenzione! Campo Password ADN Obbligatorio.")
		document.FormLogin.<%=ICostantiSecurity.CAMPO_PASSWORD%>.focus();
		return false;
	}

	document.forms[0].submit();
	document.FormLogin.<%=ICostantiSecurity.CAMPO_PASSWORD%>.value = "";
}
</script>
<script language="JavaScript" src="/html/SubmitIT.js"></script>
</head>

<body bgcolor="#0383C0" topmargin="0" marginwidth="0" leftmargin="0" marginheight="0" background="/images/bckline.gif">
<table height="80%" width="100%">
	<tr>
		<td width="100%" height="100%" align="center" valign="middle">
        	<table border=1 bordercolor="#ffffff" cellpadding="0" cellspacing="0" width="800" align="center">
		  		<tr>
            		<td width="700" valign="top" class="rNoBord" background="/images/login.gif">
            			<br><%-- dentro <form...> target="_blank" fa uscire una nuova scheda --%>
			            <form onkeyup="javascript:if(window.event.keyCode==13){exLogin();}" action="<%=IWebConstants.PG_MAIN%>" method="post" name="FormLogin">
			       			<div align=left style="position:relative; left=490px;">
			       				<strong>
									<!-- <font color="#FFFFFF"> -->
									<%-- Server di&nbsp;:&nbsp;&nbsp;&nbsp;<%=server%><br> --%>
									<%-- Versione &nbsp;:&nbsp;&nbsp;&nbsp;<%=VersionFS%>&nbsp;(DB:&nbsp;<%=VersionDB%>)<br> --%>
									<!-- </font> -->
									<font color="#FFFFFF">Server&nbsp;di&nbsp;:&nbsp;&nbsp;&nbsp;<%=server%><br>Versione&nbsp;:&nbsp;&nbsp;&nbsp;<%=version%></font>
			       				</strong>
			       			</div>
			       			<br>
							<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.utenzaAdn.action.ActUtenzaAdn">
			       			<strong>
								<font color="#FFFFFF">User ADN :</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			       			</strong>
			       			<input type="text" name="<%=ICostantiSecurity.CAMPO_USER_ID%>" style="border: 0px none; color: #044A7E;" onChange="Javascript:<%=ICostantiSecurity.CAMPO_PASSWORD%>.value='';">
			       			<br>
			       			<strong>
			         			<font color="#FFFFFF">Password ADN :</font>&nbsp;
			       			</strong>
			       			<input style="border: 0px none; color: #044A7E;" type="password" name="<%=ICostantiSecurity.CAMPO_PASSWORD%>">
			       			<br>
		       				<img style="cursor:hand;" align="middle" onclick="Javascript:exLogin();" src="images/tastologin.gif" width="80" height="30" alt="" border="0">
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
                				<p align="center" style="margin-top: 0; margin-bottom: 0">
	                				<strong>
	                  					<font color="#FFFFFF" size="3">COMUNICAZIONI DI SERVIZIO</font>
	                				</strong>
	                				<hr color="#FFFFFF" width="100%" size="1" align="center">
								</p>
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
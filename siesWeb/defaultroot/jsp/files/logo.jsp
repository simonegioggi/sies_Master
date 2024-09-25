<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<html>
<head>
<title>S.I.E.S. - home page</title>
<link rel="STYLESHEET" type="text/css" href="/css/style.css">
</head>

<body class="menu" leftmargin="0" topmargin="3">
<table height="100%" width="100%">
	<tr>
		<td valign="top" width="100%">
<%
UtenteModel lUteMod = (UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
UfficioModel lUffMod = lUteMod.getUfficioUtente();
String CodUff = new String(lUffMod.getCodTipoUfficio());
String img = new String("");
if (CodUff.startsWith("TDS") || CodUff.startsWith("UDS")
		|| CodUff.equals("DDA") || CodUff.equals("DNA")) {
	img = "/images/LOGOSius.gif";
} else if ((CodUff.equals("UEPE") || CodUff.equals("UEPESS"))) {
	img = "/images/LOGOSiepe.gif";
} else  if ((CodUff.equals("PM") || CodUff.equals("PMM") || CodUff.equals("PGCAP"))) {
	img = "/images/LOGOSiep.gif";
} else
	img = "/images/logoSIGE.gif";
%>
			<img src="<%=img%>" alt="" width="160" height="35" border="0" align="middle">
		</td>
	</tr>
</table>
</body>
</html>
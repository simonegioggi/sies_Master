<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="siap.sico.saml.util.SamlMaker" %>
<%@ page import="siap.sico.saml.model.SamlModel" %>

<html>
<head>
	<title>S.I.A.P. - home page</title>
	<link rel="STYLESHEET" type="text/css" href="/css/style.css">
</head>

<body class=menu leftmargin="0" topmargin="3">
<table height="100%" width="100%"><tr>
<td valign="top" width=100%>
<%
SamlModel lModel = new SamlModel("0011223344", "PM", "A1234", "10.0.20.30",
                    "Rossi", "Mario", "Torino", "SIEP", "nome.cognome");
SamlMaker lmaker = new SamlMaker();
String lSamkCriptata = lmaker.createSamlAssertion(lModel);
%>
<%=lSamkCriptata%>
</td>
</tr></table>
</body>
</html>
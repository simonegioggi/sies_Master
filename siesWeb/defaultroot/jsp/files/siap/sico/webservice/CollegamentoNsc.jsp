<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="IndirizzoNsc" scope="request" class="java.lang.String"/>
<jsp:useBean id="CodiceSedeUfficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="CodiceTipoUfficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="Utente" scope="request" class="java.lang.String"/>
<jsp:useBean id="HostAddress" scope="request" class="java.lang.String"/>
<jsp:useBean id="CognomeUtente" scope="request" class="java.lang.String"/>
<jsp:useBean id="NomeUtente" scope="request" class="java.lang.String"/>
<jsp:useBean id="Distretto" scope="request" class="java.lang.String"/>
<jsp:useBean id="Sistema" scope="request" class="java.lang.String"/>
<jsp:useBean id="UseSAML" scope="request" class="java.lang.String"/>
<jsp:useBean id="TokenSAML" scope="request" class="java.lang.String"/>

<html>
	<head>
		<title> Collegamento Nsc </title>
		<script language="JavaScript">
  			function Collega() 
  			{ 	
 						document.LoadCollegaNsc.submit();
				}
  	</script>
	</head>
	
<% 
	if (UseSAML.equals("SI"))
	{
	%>
		<body onLoad="Collega()" class="corpo">
			<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
			<%--  <FORM method="POST"  action="<%=IndirizzoNsc%>" name="LoadCollegaNsc" target = "_new">--%>
			<FORM method="POST"  action="<%=IndirizzoNsc%>" name="LoadCollegaNsc">
				<input type="hidden" name="TokenSAML" value="<%=StringUtils.toStringJSP(TokenSAML) %>" />
			</FORM>
		</body>
	<%
	}
	else
	{  
	%>
			<body onLoad="Collega()" class="corpo">
				<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
				<%--<FORM method="POST"  action="<%=IndirizzoNsc%>" name="LoadCollegaNsc" target = "_new">--%>
				<FORM method="POST"  action="<%=IndirizzoNsc%>" name="LoadCollegaNsc">
						<input type="hidden" name="hCodiceSedeUfficio" value="<%=StringUtils.toStringJSP(CodiceSedeUfficio) %>" />
						<input type="hidden" name="hCodiceTipoUfficio" value="<%=StringUtils.toStringJSP(CodiceTipoUfficio) %>" />
						<input type="hidden" name="hUtente" value="<%=StringUtils.toStringJSP(Utente) %>" />
						<input type="hidden" name="hHostAddress" value="<%=StringUtils.toStringJSP(HostAddress) %>" />
						<input type="hidden" name="hCognomeUtente" value="<%=StringUtils.toStringJSP(CognomeUtente) %>" />
						<input type="hidden" name="hDistretto" value="<%=StringUtils.toStringJSP(Distretto) %>" />
						<input type="hidden" name="hNomeUtente" value="<%=StringUtils.toStringJSP(NomeUtente) %>" />
						<input type="hidden" name="hSistema" value="<%=StringUtils.toStringJSP(Sistema) %>" />
				</FORM>
			</body>
	<%
	}
	%>
</html>
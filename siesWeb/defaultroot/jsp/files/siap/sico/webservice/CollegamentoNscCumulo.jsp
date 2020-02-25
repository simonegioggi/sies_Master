<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.StringUtils"%>

<%-- <jsp:useBean id="CodiceSedeUfficio" 	scope="request" class="java.lang.String"/> --%>
<%-- <jsp:useBean id="CodiceTipoUfficio" 	scope="request" class="java.lang.String"/> --%>
<%-- <jsp:useBean id="Utente"            	scope="request" class="java.lang.String"/> --%>
<%-- <jsp:useBean id="HostAddress"       	scope="request" class="java.lang.String"/> --%>
<%-- <jsp:useBean id="CognomeUtente"     	scope="request" class="java.lang.String"/> --%>
<%-- <jsp:useBean id="NomeUtente" 			scope="request" class="java.lang.String"/> --%>
<%-- <jsp:useBean id="Distretto" 			scope="request" class="java.lang.String"/> --%>
<%-- <jsp:useBean id="Sistema" 				scope="request" class="java.lang.String"/> --%>

<jsp:useBean id="ProgrFascSiep" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="AnnoFascSiep" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="IdFascicoloSiep" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="IdSoggettoSiep" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="IndirizzoNsc" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="UseSAML" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="TokenSAML" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="SiepToNsc" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="CognomeSoggetto" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="NomeSoggetto" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="DataNascita" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="Sesso" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="LuogoNascita" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="StatoNascita" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="DescLuogoNascita" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="DescStatoNascita" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="Paternita" 			scope="request" class="java.lang.String"/>
<!-- MEV 16 CUMULO: aggiunto useBean -->
<jsp:useBean id="IdIstruttoriaCumulo" 	scope="request" class="java.lang.String"/>

<html>
	<head>
		<title> Collegamento Nsc </title>
		<script language="JavaScript">
  			function Collega() {
				document.LoadCollegaNsc.submit();
			}
  		</script>
	</head>

<%--
	MEV 16: aggiunta nuova proprietà e modificata l'intera gestione dei dati da inviare ad NSC.
--%>
<% 
	if (UseSAML.equals("SI")) {
%>
		<body onLoad="Collega()" class="corpo">
			<FORM method="POST"  action="<%=IndirizzoNsc%>" name="LoadCollegaNsc">
				<input type="hidden" name="ProgrFascSiep" 		value="<%=StringUtils.toStringJSP(ProgrFascSiep) 	%>" />
				<input type="hidden" name="AnnoFascSiep" 		value="<%=StringUtils.toStringJSP(AnnoFascSiep)		%>" />
				<input type="hidden" name="IdFascicoloSiep" 	value="<%=StringUtils.toStringJSP(IdFascicoloSiep) 	%>" />
				<input type="hidden" name="IdSoggettoSiep" 		value="<%=StringUtils.toStringJSP(IdSoggettoSiep)	%>" />
				<input type="hidden" name="IndirizzoNsc" 		value="<%=StringUtils.toStringJSP(IndirizzoNsc)		%>" />
				<input type="hidden" name="UseSAML" 			value="<%=StringUtils.toStringJSP(UseSAML) 			%>" />
				<input type="hidden" name="TokenSAML" 			value="<%=StringUtils.toStringJSP(TokenSAML) 		%>" />
				<input type="hidden" name="SiepToNsc" 			value="<%=StringUtils.toStringJSP(SiepToNsc) 		%>" />
				<input type="hidden" name="CognomeSoggetto" 	value="<%=StringUtils.toStringJSP(CognomeSoggetto) 	%>" />
				<input type="hidden" name="NomeSoggetto" 		value="<%=StringUtils.toStringJSP(NomeSoggetto) 	%>" />
				<input type="hidden" name="DataNascita" 		value="<%=StringUtils.toStringJSP(DataNascita) 		%>" />
				<input type="hidden" name="Sesso" 				value="<%=StringUtils.toStringJSP(Sesso) 			%>" />
				<input type="hidden" name="LuogoNascita" 		value="<%=StringUtils.toStringJSP(LuogoNascita) 	%>" />
				<input type="hidden" name="StatoNascita" 		value="<%=StringUtils.toStringJSP(StatoNascita) 	%>" />
				<input type="hidden" name="DescLuogoNascita" 	value="<%=StringUtils.toStringJSP(DescLuogoNascita) %>" />
				<input type="hidden" name="DescStatoNascita"	value="<%=StringUtils.toStringJSP(DescStatoNascita) %>" />
				<input type="hidden" name="Paternita" 			value="<%=StringUtils.toStringJSP(Paternita) 		%>" />
				<!-- MEV 16 CUMULO: aggiunto campo nascosto -->
				<input type="hidden" name="IdIstruttoriaCumulo" value="<%=StringUtils.toStringJSP(IdIstruttoriaCumulo) 	%>" />
			</FORM>
		</body>
<%
	}
%>
<%-- 	else --%>
<%-- 	{ --%>
<%-- 			<body onLoad="Collega()" class="corpo">   --%>
<%-- 					<FORM method="POST"  action="<%=IndirizzoNsc%>" name="LoadCollegaNsc"> --%>
<%-- 							<input type="hidden" name="hCodiceSedeUfficio" 	value="<%=StringUtils.toStringJSP(CodiceSedeUfficio) %>" 	/> --%>
<%-- 							<input type="hidden" name="hCodiceTipoUfficio" 	value="<%=StringUtils.toStringJSP(CodiceTipoUfficio) %>" 	/> --%>
<%-- 							<input type="hidden" name="hUtente"				value="<%=StringUtils.toStringJSP(Utente) %>" 				/> --%>
<%-- 							<input type="hidden" name="hHostAddress" 		value="<%=StringUtils.toStringJSP(HostAddress) %>" 			/> --%>
<%-- 							<input type="hidden" name="hCognomeUtente" 		value="<%=StringUtils.toStringJSP(CognomeUtente) %>" 		/> --%>
<%-- 							<input type="hidden" name="hDistretto" 			value="<%=StringUtils.toStringJSP(Distretto) %>" 			/> --%>
<%-- 							<input type="hidden" name="hNomeUtente" 		value="<%=StringUtils.toStringJSP(NomeUtente) %>" 			/> --%>
<%-- 							<input type="hidden" name="hSistema"			value="<%=StringUtils.toStringJSP(Sistema) %>" 				/> --%>
<%-- 							<input type="hidden" name="hCognomeSoggetto" 	value="<%=StringUtils.toStringJSP(CognomeSoggetto) %>" 		/> --%>
<%-- 							<input type="hidden" name="hNomeSoggetto"    	value="<%=StringUtils.toStringJSP(NomeSoggetto) %>" 		/> --%>
<%-- 							<input type="hidden" name="hProgrFascSiep"   	value="<%=StringUtils.toStringJSP(ProgrFascSiep) %>" 		/> --%>
<%-- 							<input type="hidden" name="hAnnoFascSiep"    	value="<%=StringUtils.toStringJSP(AnnoFascSiep) %>" 		/> --%>
<%-- 					</FORM> --%>
<%-- 			</body> --%>
<%-- 	<% --%>
<%--	} --%>
<%-- 	%> --%>
</html>
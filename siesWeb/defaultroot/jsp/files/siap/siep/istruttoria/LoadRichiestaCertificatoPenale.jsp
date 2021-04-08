<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="IdIstruttoriaCumulo" scope="request" class="java.lang.String"/>

<%-- MEV INTEGRAZIONE SIES ADN: eliminate userId e psw per NSC; inviamo solo utenza ADN --%>
<%-- <%@ page import="siap.sico.utente.model.UtenteModel"%> --%>
<%-- <%@ page import="siap.sico.utente.action.ICostantiUtente"%> --%>
<%-- <jsp:useBean id="utente" scope="request" class="siap.sico.utente.model.UtenteModel"/> --%>
<%--
<%
String userIdNSC = utente.getUseridNSC();
String pwdNSC = utente.getPwdNSC();
%>
--%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Richiesta Certificato Penale</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<%--     
<script language="JavaScript">
function Verify() {
	// il campo Userid è obbligatorio
	if (document.LoadRichiestaCertificatoPenale.<%=ICostantiUtente.CAMPO_USERID_NSC%>.value == "") {
		alert("Userid è obbligatoria");
		document.LoadRichiestaCertificatoPenale.<%=ICostantiUtente.CAMPO_USERID_NSC%>.focus();
		return false;
	}
    // il campo Password è obbligatorio
  	if (document.LoadRichiestaCertificatoPenale.<%=ICostantiUtente.CAMPO_PWD_NSC%>.value == "") {
        alert("Password è obbligatoria");
        document.LoadRichiestaCertificatoPenale.<%=ICostantiUtente.CAMPO_PWD_NSC%>.focus();
        return false;
	}
	return true;
}
</script>
 --%>
 </head>
 <body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
		<td class="LBG">
      		<font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Richiesta Certificato Penale</font>
      	</td>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>

<FORM method="POST" name="LoadRichiestaCertificatoPenale" action="<%= IWebConstants.PG_MAIN%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoria.action.ActInserisciCertificatoPenale">
<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO %>" value="<%= IdIstruttoriaCumulo%>">

<table>
<%--
<%
if (userIdNSC == null || userIdNSC.equals("")) {
%>
   <tr>
      <td class="Titolo" colspan=2>Dati Accesso NSC </td>
   </tr>

   <tr>
      <td class="l">Userid <font class=ob>(*)</font></td>
      <td class="l">
      	 <input type="text" Title="useridNSC" name="<%=ICostantiUtente.CAMPO_USERID_NSC%>" value="" maxlength="100" size="30" />
      </td>
   </tr>

   <tr>
      <td class="l">Password <font class=ob>(*)</font></td>
      <td class="l">
      	 <input type="password" Title="pwdNSC" name="<%=ICostantiUtente.CAMPO_PWD_NSC%>" value="" maxlength="30" size="30" />
      </td>
   </tr>

   <tr>
       <td class="lNoBord" colspan="2">
       <br><br><INPUT class="bottone" type="submit" name="I" value="Richiesta Certificato" onClick="javascript:return Verify();">
       </td>
   </tr>

<%
} else {
%>
--%>
	<tr>
		<td class="lNoBord">
       		<INPUT class="bottone" type="submit" name="I" value="Richiesta Certificato">
       	</td>
	</tr>
<%--
<%
}
%>
--%>
</table>
</form>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.utente.action.ICostantiUtente"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<jsp:useBean id="utente" scope="request" class="siap.sico.utente.model.UtenteModel"/>

<%
	String userIdNSC = utente.getUseridNSC();
	String pwdNSC = utente.getPwdNSC();
%>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Richiesta Certificato Penale</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript">
		function Verify() {
	    	// NUOVA INFRASTRUTTURA: controllo preventivo
   	if (document.LoadRichiestaCertificatoPenale.<%=ICostantiUtente.CAMPO_USERID_NSC%>
   			&& document.LoadRichiestaCertificatoPenale.<%=ICostantiUtente.CAMPO_PWD_NSC%>) {
		        // il campo Userid è obbligatorio
		    	if(document.LoadRichiestaCertificatoPenale.<%=ICostantiUtente.CAMPO_USERID_NSC%>.value=="") {
		          	alert("Userid è obbligatoria");
		          	document.LoadRichiestaCertificatoPenale.<%=ICostantiUtente.CAMPO_USERID_NSC%>.focus();
		          	return false;
		        }
	     	    // il campo Password è obbligatorio
		    	if(document.LoadRichiestaCertificatoPenale.<%=ICostantiUtente.CAMPO_PWD_NSC%>.value=="") {
		          	alert("Password è obbligatoria");
		         	document.LoadRichiestaCertificatoPenale.<%=ICostantiUtente.CAMPO_PWD_NSC%>.focus();
		          	return false;
		        }
	    	}
	    }

function pulisciCampi() {
	document.LoadRichiestaCertificatoPenale.<%=ICostantiUtente.CAMPO_USERID_NSC%>.value = "";
	document.LoadRichiestaCertificatoPenale.<%=ICostantiUtente.CAMPO_PWD_NSC%>.value = "";
}
  </script>

 </head>
 <body class="corpo">
   <table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0"">
			</a>
		</td>
      <td class="LBG">
      	<font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Richiesta Certificato Penale</font>
      </td>
    </tr>
  </table>
  
  <br>
   <table>
      <tr>
        <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
      </tr>
   </table>
  <br>
  
  <FORM method="POST" name="LoadRichiestaCertificatoPenale" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.fascicolo.action.ActInserisciRichiestaCertificatoPenale">

  <table>
<%-- <% --%>
<!--MEV_2025-48: aggiunta nuova funzionalita': userid e psw sempre visibili -->
<!-- if (userIdNSC == null || userIdNSC.equals("")) { -->
<!-- %> -->
   <tr>
      <td class="Titolo" colspan=2>Dati Accesso NSC </td>
   </tr>

   <tr>
      <td class="l">Userid <font class=ob>(*)</font></td>
      <td class="l">
      	 	<input type="text" Title="useridNSC" name="<%=ICostantiUtente.CAMPO_USERID_NSC%>" value="<%=Utils.isPresent(userIdNSC) ? userIdNSC : ""%>" maxlength="100" size="30"/>
      	 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      	 	<font style="color: red;">N.B. E' possibile ridigitare i dati, in caso di modifica dell'utenza e/o password</font>
      </td>
   </tr>

   <tr>
      <td class="l">Password <font class=ob>(*)</font></td>
      <td class="l">
      	 	<input type="password" Title="pwdNSC" name="<%=ICostantiUtente.CAMPO_PWD_NSC%>" 
      	 	<%-- Ticket#202602190150 - la pwd deve essere caricata decodificata 
      	 	value="<%=Utils.isPresent(pwdNSC) ? pwdNSC : ""%>"
      	 	--%>
      	 	value="<%=Utils.isPresent(pwdNSC) ? Utils.pwdNSCDecode(pwdNSC) : ""%>" 
      	 	maxlength="30" size="30"/>
      	 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      	 	<input type="button" onclick="javascript:pulisciCampi();" value="Pulisci i Campi">
      </td>
   </tr>
<%-- <% --%>
<!-- } -->
<%-- %> --%>
   <tr>
       <td class="lNoBord" colspan="2">
       <br><br><INPUT class="bottone" type="submit" name="I" value="RichiestaCertificato" onClick="javascript:return Verify();">
       </td>
   </tr>

</table>
	</form>

	</body>
</html>
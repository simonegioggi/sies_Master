<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.sico.utente.action.ICostantiUtente" %>
<html>
<head>
	<title>Cambio Password Utente</title>
	<link rel="stylesheet" type="text/css" href="/css/style.css">
	<script language="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
function MatchPwd()
{
	if (document.forms[0].oldPwd.value!=document.forms[0].Pwd.value)
	{
		if (document.forms[0].Pwd.value!=document.forms[0].rePwd.value)
		{
			alert("Le due nuove password non coincidono");
			document.forms[0].Pwd.focus();
    	return false;
		}else
			if (document.forms[0].Pwd.value.length<6)
			{
				alert("La password deve essere almeno di 6 caratteri");
				return false;
			} else
				return true;
	}
	else {
		alert("La nuova password deve essere diversa dalla vecchia");
		return false;
		}
}
</script>

<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>

</head>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="msg" scope="request" class="java.lang.String" />

<body leftmargin="5" class=corpo>


<table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Cambio Password</font>
        </td>
      </tr>
</table>
<br>
<br>
<br>
<br>
<div align="center">
<table width=100% cellpadding="0" cellspacing="0" border=1 bordercolor="#BEC6FC">
              <tr>
                <td width=100% bordercolor=#BEC6FC class=c>
                  <font class=label>Utente:</font>&nbsp;&nbsp;
                  <font class=campo><%=UtenteConnesso.getUserId()%></font>&nbsp;&nbsp;
                  <font class=label>Ufficio:</font>

<%
                  UfficioModel lUfficioUtente = UtenteConnesso.getUfficioUtente();
%>
                  <font class=campo><%=lUfficioUtente.getDescrTipoUfficio() +" - "+ lUfficioUtente.getDescrComune()%></font>
                </td>
              </tr>
            </table>

<br>
<br>
<table>
      <tr>
	  <td class=LBG><%= msg %></td>
	  </tr>
</table>
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name=f>
<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.utente.action.ActModificaPassword">
<input type="hidden" name="<%=ICostantiUtente.CAMPO_COD_UTENTE%>" value="<%= UtenteConnesso.getUserId() %>">
<table cellspacing=4 cellpadding=4>
	<tr>
		<td class=l>Vecchia Password : </td>
		<td class=l><input type="password" name="oldPwd" size="20" maxlength="20"></td>
	</tr>
	<tr>
		<td class=l>Nuova Password : </td>
		<td class=l><input type="password" name="Pwd" size="20" maxlength="20"></td>
	</tr>
	<tr>
		<td class=l>Conferma Nuova Password : </td>
		<td class=l><input type="password" name="rePwd" size="20" maxlength="20"></td>
	</tr>
	<tr>
		<td class=c colspan=2><input type="submit" name="Salva" value="Salva modifiche" class="bottone"></td>
	</tr>
</table>
</form>

</div>

<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("f");

  frmvalidator.addValidation("Pwd","req","Il campo Nuova Password è obbligatorio");
  frmvalidator.addValidation("Pwd","maxlen=20","La lunghezza massima per la Password è di 20 caratteri");

  frmvalidator.addValidation("rePwd","req","Il campo Conferma Password è obbligatorio");

  //frmvalidator.addValidation("-oldPwd","-req","-Il campo Vecchia Password è obbligatorio");

  frmvalidator.setAddnlValidationFunction("MatchPwd");
</script>

</BODY>
</HTML>
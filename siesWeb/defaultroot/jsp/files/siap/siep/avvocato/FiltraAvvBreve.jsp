<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>> </script>

    <script language="JavaScript">
      function  Verify()
      {
        var ritorno = true;
        return ritorno;
      }
    </script>
<title>[S.I.E.S.] - Lista Province</title>
  </head>

  <body class="corpo" onload="focus();">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f" target=listaAvvocati>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.avvocato.action.ActRicercaAvvocato">
  <input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
  <input type="HIDDEN" name="modalita" value="BREVE">

   <br>
   <table>
		<tr>
		<td class=LBG><font class="campo">Filtra la lista  per : </font></td>
		<td>
		<input type="text" name="<%=ICostantiAvvocato.CAMPO_COGNOME%>" value="" onFocus="javascript:document.f.go.disabled=false;">
		</td>
		<td><input type="submit" name="go" value="Filtra >>"></td>
	</tr>
	</table>
	</form>
 <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("f");

      frmvalidator.setAddnlValidationFunction("Verify");
    </script>
  </body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.w_magistrato.action.ICostantiWMagistrato" %>


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript">
      function trim(string)
      {
       return string.replace(/(^\s*)|(\s*$)/g,'');
      }
      function  Verify()
      {
        var ritorno = true;

        if (trim(document.f.<%=ICostantiWMagistrato.CAMPO_COGNOME%>.value).length < 1)
        {
          alert("Occorre inserire almeno la lettera iniziale del COGNOME.");
          ritorno = false;
        }
        return ritorno;
      }
    </script>

    <title>[S.I.E.S.] - Filtro Magistrati</title>
  </head>

  <body class="corpo" onload="Javascript:focus();">
    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f" target=listaWMagistrati>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.w_magistrato.action.ActRicercaWMagistrato">
    <input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
   <table>
    <tr>
      <td class=LBG><font class="campo">Filtra la lista  per : </font></td>
    </tr>
    <tr>
      <td class="l">Cognome</td>
      <td class="l"> <input value="" type="text" name="<%=ICostantiWMagistrato.CAMPO_COGNOME%>" > </td>
      <td><input onclick="Javascript:return Verify();" type="submit" name="go" value="Filtra >>"></td>
    </tr>



  </table>
</form>
  </body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato" %>


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript">
      function  Verify()
      {
        var ritorno = true;

        /*if (document.f.<%=ICostantiMagistrato.CAMPO_COGNOME%>.value.length < 3)
        {
          alert("Occorre inserire almeno 3 caratteri iniziali del COGNOME.");
          ritorno = false;
        }*/
        return ritorno;
      }
    </script>

    <title>[S.I.E.S.] - Filtro Magistrati</title>
  </head>

  <body class="corpo" onload="document.f.submit();">
    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f" target=listaMagistrati>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.magistrato.action.ActRicercaMagistratoLista">
    <input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
    <input type="HIDDEN" name="fieldname" value="<%=request.getParameter("fieldname")%>">
   <table>
    <tr>
      <td class=LBG><font class="campo">Filtra la lista  per : </font></td>
    </tr>
    <tr>
      <td class="l">Cognome</td>
      <td class="l"> <input value="" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" > </td>
      <td><input onclick="Javascript:return Verify();" type="submit" name="go" value="Filtra >>"></td>
    </tr>



  </table>
</form>
  </body>
</html>
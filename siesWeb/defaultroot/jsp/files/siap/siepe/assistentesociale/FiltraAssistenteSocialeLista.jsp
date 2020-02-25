<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.siepe.assistentesociale.action.ICostantiAssistenteSociale" %>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript">
      function  Verify()
      {
        var ritorno = true;

        /*if (document.f.<--%=ICostantiAssistenteSociale.CAMPO_COGNOME--%>.value.length < 3)
        {
          alert("Occorre inserire almeno 3 caratteri iniziali del COGNOME.");
          ritorno = false;
        }*/
        return ritorno;
      }
    </script>

    <title>[S.I.E.S.] - Filtro Assistenti Sociali</title>
  </head>

  <body class="corpo" onload="document.f.submit();">
    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f" target="listaAssistentiSociali">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.assistentesociale.action.ActRicercaAssistenteSocialeLista">
    <input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
   <table>
    <tr>
    	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      	<%--td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td --%>
      <td class=LBG><font class="campo">Filtra la lista per : </font></td>
    </tr>
    <tr>
      <td class="l">Cognome </td>
      <td class="l"> <input value="" type="text" name="<%=ICostantiAssistenteSociale.CAMPO_COGNOME%>" > </td>
      <td><input onclick="Javascript:return Verify();" type="submit" name="go" value="Filtra >>"></td>
    </tr>

  </table>
</form>
  </body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sige.sezione.action.ICostantiSezione" %>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript">
      function  Verify()
      {
        var ritorno = true;
        return ritorno;
      }
    </script>

    <title>[S.I.E.S.] - Filtro Sezioni</title>
  </head>

  <body class="corpo">
    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f" target=listaSezioni>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.sezione.action.ActRicercaSezioneLista">
    <input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
   <table>
    <tr>
    	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      	<%--td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td --%>
      <td class=LBG>
      	<font class="campo">Filtra la lista  per : </font>
      </td>
    </tr>
    <tr>
      <td class="l">Codice</td>
      <td class="l">
      	<input value="" type="text" name="<%=ICostantiSezione.CAMPO_CODICE%>" >
      </td>
      <td>
      	<input onclick="Javascript:return Verify();" type="submit" name="go" value="Filtra >>">
      </td>
    </tr>

  </table>
</form>
  </body>
</html>
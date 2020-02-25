<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA" %>


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

    <title>[S.I.E.S.] - Filtro UEPE</title>
  </head>

  <body class="corpo" onload="focus();">
    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f" target=listaCssa>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.cssa.action.ActListaCSSAFiltroComune">
    <input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
    <input type="HIDDEN" name="fieldname" value="<%=request.getParameter("fieldname")%>">
    <input type="HIDDEN" name="field2" value="<%=request.getParameter("field2")%>">
    <input type="HIDDEN" name="typename" value="<%=request.getParameter("typename")%>">
   <table>
    <tr>
      <td class=LBG colspan=2 ><font class="campo">Filtra la lista  per : </font></td>
    </tr>
    <tr>
      <td class="l">Comune</td>
      <td class="l"> <input value="" type="text" name="<%=ICostantiCSSA.CAMPO_COD_COMUNE%>"  onFocus="javascript:document.f.go.disabled=false;">  </td>
      <td><input onclick="Javascript:return Verify();" type="submit" name="go" value="Filtra >>"></td>
    </tr>



  </table>
</form>
 <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("f");

      frmvalidator.setAddnlValidationFunction("Verify");
    </script>
  </body>
</html>
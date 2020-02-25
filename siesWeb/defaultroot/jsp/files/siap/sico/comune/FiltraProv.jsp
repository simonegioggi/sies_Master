<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="siap.sico.decodifiche.model.ComuneProvinciaModel" %>

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

  <body class="corpo" onload="Javascript:focus();document.f.submit();">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f" target=listacomuni>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.decodifiche.action.ActVisualizzaComuni">
    <input type="HIDDEN" name="codProv" value="<%=request.getParameter("provincia")%>">
    <input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
    <input type="HIDDEN" name="fieldname" value="<%=request.getParameter("fieldname")%>">
    <br>
    <table>
      <tr>
      <td class=LBG>o Filtra i Comuni per</td>
      <td>
      <input type="text" name="comune" value="" onFocus="javascript:document.f.go.disabled=false;">
      </td>
      <td><input type="submit" name="go" value="Filtra"></td>
      </tr>
    </table>
  </form>
  <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("f");

      frmvalidator.setAddnlValidationFunction("Verify");
    </script>
  </body>
</html>
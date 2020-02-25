<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<jsp:useBean id="ListaDistretti" scope="request" class="java.util.Vector" />
<jsp:useBean id="codTipoUff" scope="request" class="java.lang.String" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
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

  <body class="corpo" onload="javascript:focus();document.f.submit();">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f" target=comuni>
    <input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
    <input type="HIDDEN" name="fieldname" value="<%=request.getParameter("fieldname")%>">
    <input type="HIDDEN" name="fieldname2" value="<%=request.getParameter("fieldname2")%>">

    <input type="HIDDEN" name="codTipoUff" value="<%=codTipoUff%>">

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.decodifiche.action.ActLoadUffSorvPerDistretto">
<br>

  <table cellpadding=0 cellspacing=0>
    <tr>
      <td class=LBG valign="middle">Seleziona il Distretto</td>
      <td valign="middle">
      <select name="<%=ICostantiUfficio.CAMPO_COD_DISTRETTO%>" onclick="javascript:document.f.go.disabled=false;">
<%
        Iterator itx = ListaDistretti.iterator();
        while ( itx.hasNext())
        {
         UfficioModel uff = (UfficioModel)itx.next();


%>

           <option value="<%=uff.getCodDistretto()%>">
             <%= uff.getDescProvincia()%>
              </option>
       <%}%>
      </select>
      </td>
      <td valign="middle"><input type="submit" name="go" value="Seleziona"></td>
    </tr>
  </table>
  </form>
  <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("f");

      frmvalidator.setAddnlValidationFunction("Verify");
    </script>
  </body>
</html>
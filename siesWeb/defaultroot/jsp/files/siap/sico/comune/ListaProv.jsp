<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="siap.sico.decodifiche.model.ComuneProvinciaModel" %>
<jsp:useBean id="ProvList" scope="request" class="java.util.Vector" />
<jsp:useBean id="codProvinciaUtenteConnesso" scope="request" class="java.lang.String"/>

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

  <body class="corpo" onload="javascript:focus();document.f.submit();">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f" target=comuni>
    <input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
    <input type="HIDDEN" name="fieldname" value="<%=request.getParameter("fieldname")%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.decodifiche.action.ActLoadProvinciaComune">
<br>

  <table cellpadding=0 cellspacing=0>
    <tr>
      <td class=LBG valign="middle">Seleziona la Provincia</td>
      <td valign="middle">
      <select name="<%=ICostantiComune.CAMPO_COD_PROVINCIA%>" onclick="javascript:document.f.go.disabled=false;">
<%
        Iterator itx = ProvList.iterator();
        while ( itx.hasNext())
        {
          ComuneProvinciaModel prov = (ComuneProvinciaModel)itx.next();
          if (!(prov.getCodProvincia() .equals("-")))
          {
%>
            <option value="<%=prov.getCodProvincia()%>"
<%            if (prov.getCodProvincia().compareTo(codProvinciaUtenteConnesso)==0)
              {
                %>SELECTED<%
              }
%>            ><%= prov.getProvincia() %>
            </option>
<%
          }
        }
%>
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
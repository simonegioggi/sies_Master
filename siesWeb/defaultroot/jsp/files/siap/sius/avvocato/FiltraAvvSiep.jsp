<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocato"%>

<html>
 <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Province</title>
 </head>

 <body class="corpo" onload="document.f.submit();">

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f" target="listaAvvocati">
   <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.avvocato.action.ActRicercaAvvocatoSiep">
   <input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
   <input type="HIDDEN" name="modalita" value="LUNGO">

   <input type="hidden" name="<%=ICostantiAvvocato.CAMPO_COGNOME%>" value="">
   <input type="hidden" name="<%=ICostantiAvvocato.CAMPO_FORO%>" value="">

  </FORM>
 </body>
</html>
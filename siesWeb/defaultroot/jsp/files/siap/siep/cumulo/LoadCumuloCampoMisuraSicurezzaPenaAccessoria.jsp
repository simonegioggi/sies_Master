<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.penacumulo.action.ICostantiPenaCumulo"%>


<html>
  <head>
<%
      if(request.getParameter("fieldname") != null && request.getParameter("fieldname").equals("MisuraSicurezza"))
       {
%>
    <title>[S.I.E.S.] -MISURE DI SICUREZZA </title>
<%
       }else{
%>

    <title>[S.I.E.S.] -PENE ACCESSORIE </title>
<%
       }
%>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
   <script language="JavaScript">

    function insertIT(cod)
    {
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=cod;
      window.parent.close();
      return;
    }


</script>

  </head>
  <body class="corpo">
  <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
     <table width="100%">
      <tr>
<%
      if(request.getParameter("fieldname") != null && request.getParameter("fieldname").equals("MisuraSicurezza"))
       {
%>
        <td class="l" width="25%">Misure di Sicurezza </td>

           <td  class="l">
             <TEXTAREA title="Misura sicurezza" name="<%=ICostantiPenaCumulo.CAMPO_MISURA_SICUREZZA%>" cols=60 rows=5><%=StringUtils.cStrForJS(request.getParameter("fieldvalue"))%></textarea>
            </td>
<%
       }else
       {
%>
        <td class="l" width="25%">Pene Accessorie </td>

           <td  class="l">
             <TEXTAREA title="Pena Accessoria" name="<%=ICostantiPenaCumulo.CAMPO_PENA_ACCESSORIA%>" cols=60 rows=5><%=StringUtils.cStrForJS(request.getParameter("fieldvalue"))%></textarea>
            </td>
<%
       }
%>

  </tr>
   <tr>
       <td class="lNoBord" colspan="2">
       <br><br>
<%
      if(request.getParameter("fieldname") != null && request.getParameter("fieldname").equals("MisuraSicurezza"))
       {
%>
      <INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="insertIT(document.f.<%=ICostantiPenaCumulo.CAMPO_MISURA_SICUREZZA%>.value);">
<%
       }else{
%>
      <INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="insertIT(document.f.<%=ICostantiPenaCumulo.CAMPO_PENA_ACCESSORIA%>.value);">
<%
       }
%>
       </td>
   </tr>

  </table>
	</form>
	</body>
</html>
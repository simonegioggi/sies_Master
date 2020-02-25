<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="f3b.web.IWebConstants" %>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>F3B - ( Framework Bull Building Blocks ) </title>
    <script language="JavaScript">
    	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        function redirigi()
        {
          if (window.name=="null")
          {
            window.close();
          }
          else
          {
            window.opener.top.close();
            window.close();
          }
        }
    </script>
  </head>
  <body class="corpo" onLoad="javascript:window.setTimeout('redirigi()', 3000)">
    <br><br><br>
    <table width="300"  cellspacing="0" align="center" class="tab" border="1">
      <tr align="center" valign="middle">
        <td align="center" colspan="2" class="tab">
          <p>&nbsp;<p>
          <b><%=request.getParameter("Messagge")%></b>
          <p>&nbsp;<p>
        </td>
      </tr>
    </table>
  </body>
</html>
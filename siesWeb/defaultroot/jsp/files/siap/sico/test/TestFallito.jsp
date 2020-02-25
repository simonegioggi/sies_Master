<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="test"     scope="request" class="siap.sico.test.model.TestModel"/>
<jsp:useBean id="errore"   scope="request" class="java.lang.String"/>

<html>
  <head>
    <title> [S.I.E.S.] - Test sistema SIES - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  </head>
  <body class="corpo">
    <table>
      <tr>
        <td>
          <h1><%=errore%></h1>
        </td>
      </tr>
      <tr>
        <td>
          <h2>Contattare l'Help Desk descrivendo l'errore riportato.</h2>
        </td>
      </tr>
      <tr>
        <td>
          <h2>Cliccare
            <a href="Javascript:window.print();">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
            </a>
              per stampare questa videata
          </h2>
        </td>
      </tr>
    </table>
    <table>
      <tr>
        <td class="l">
<%
          out.println("<h2>Tutti i parametri di sistema</h2>");
          java.util.Enumeration e  = System.getProperties().propertyNames();
          if( e!=null )
          {
            out.println("<pre>");
            for (;e.hasMoreElements();)
            {
              String key = (String) e.nextElement();
              out.println("<font color=\"#FF8080\">"+key+"</font>" + ":=" +
              "<font color=\"#8080FF\">"+System.getProperty(key)+"</font>");
            }

            out.println("</pre><p>");
          }
          else
          {
            out.println("Properties are not accessible<p>");
          }
%>
        </td>
      </tr>
    </table>
  </body>
</html>
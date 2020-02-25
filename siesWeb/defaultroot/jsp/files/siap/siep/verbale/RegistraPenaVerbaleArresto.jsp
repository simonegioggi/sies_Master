<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<jsp:useBean id="flagVerbaleNotifica" scope="request" class="java.lang.String" />
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <% if (flagVerbaleNotifica.equalsIgnoreCase("verbale")) {%>
    	<title>[S.I.E.S.] - Dettaglio Verbale Arresto</title>
    <% } else if (flagVerbaleNotifica.equalsIgnoreCase("notifica")) { %>
    	<title>[S.I.E.S.] - Dettaglio Notifica Carcere</title>
    <%}%>	
  </head>

  <body class="corpo">
  <FORM name="comandi" method="POST" action="/jsp/Main.jsp">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordinescarcerazione.action.ActLoadInserisciOSFuturaMemoria">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG>
        <% if (flagVerbaleNotifica.equalsIgnoreCase("verbale")) {%>
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Verbale Arresto</font>
        <%} else if (flagVerbaleNotifica.equalsIgnoreCase("notifica")) {%>
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Notifica Carcere</font> 	
        <%}%>  
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>

<table>
      <tr>
      <td class=C width=50%>Calcolo del Fine Pena Correttamente Effettuato!</td>
      </tr>
</table>

<table>
      <tr height=10><td></td></tr>
      <tr>
       <td>
        <INPUT  class="bottone" type="submit" name="ORDSCARC" value="Ordine Di Scarcerazione">
       </td>
      </tr>
</table>
     
</FORM>
  </body>
</html>
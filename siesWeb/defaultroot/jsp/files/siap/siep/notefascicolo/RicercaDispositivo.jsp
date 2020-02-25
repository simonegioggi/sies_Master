<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="siap.sico.decodifiche.action.ICostantiComune" %>
<%@ page import="siap.sico.decodifiche.model.ComuneModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>

<jsp:useBean id="note" scope="request" class="java.lang.String" />
<html>
  <head>
    <title>Dispositivo</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  </head>

  <body class="corpo">
  <br>
    <table  width="100%">
      <tr>
        <td class="LBG"><font class="label">Dispositivo</font></td>
        <td class="l"><font class="campo"><%=note%></font></td>

      </tr>
      </table>
      <br> <br> <br> <br>
      <table  width="100%">
      <tr>
        <td align="center">
           <input type="button" name="vedi" value="Chiudi"
           onClick="javascript:self.close();"></td>
        </td>
      </tr>
    </table>
   </body>
</html>
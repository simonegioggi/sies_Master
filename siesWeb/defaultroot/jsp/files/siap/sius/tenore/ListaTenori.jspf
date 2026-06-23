<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.sius.tenore.model.TenoreModel" %>
<table align="center">
    <tr>
      <td class="int">Oggetto Esito</td>
      <td class="int">Esito</td>
    </tr>

<%
  Iterator itxlista = lTenori.iterator();
  while (itxlista.hasNext())
  {
    TenoreModel lTenMod = (TenoreModel)itxlista.next();
%>
    <tr>
      <td class="l"><%=StringUtils.toStringJSP(lTenMod.getDescrOggettoTenore())%></td>
      <td class="c"><%=StringUtils.toStringJSP(lTenMod.getDescrEsitoTenore())%></td>
    </tr>
<%
  }
%>
</table>
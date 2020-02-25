<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<jsp:useBean id="EvIstruttoria" scope="request" class="siap.sico.evento.model.EventoModel"/>

<br>
<table cellspacing=0 cellpadding=0 width=95%>
  <tr>
    <td class="L">
      <font class="label">Istruttoria N. </font>
      <font class="campo">
      <%=EvIstruttoria.getAnnoProtocollo()%>
      /
      <%=EvIstruttoria.getProgrProtocollo()%>
      &nbsp;
      </font>
      <font class="label">Del </font>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(EvIstruttoria.getDataEmissione(),"dd-MM-yyyy"))%></font>
    </td>
  </tr>
</table>  
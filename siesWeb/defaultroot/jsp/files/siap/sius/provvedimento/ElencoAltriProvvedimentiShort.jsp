<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento" %>

<jsp:useBean id="provvedimentiAltri"     scope="request" class="java.util.Vector"/>

<html>
  <table width="100%" >

<%
  if ( provvedimentiAltri.size() == 0 )
  {
%>
        <td class="int" align="left">Non ci sono altri provvedimenti allegati al procedimento.</td>
<%
  } else
  {
%>
  <div align=center>
    <tr>
      <td class="int" width=15%>Data emissione</td>
      <td class="int" width=20%>Tipo </td>
      <td class="int" width=45%>Motivo </td>
      <td class="int" width=20%>Esito </td>
    </tr>
  </div>
<%
    Iterator itx = provvedimentiAltri.iterator();
    while ( itx.hasNext())
    {
      EventoModel lProv = (EventoModel)itx.next();
%>
    <tr>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lProv.getDataEmissione(),"dd-MM-yyyy"),"-") %></font>     </td>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(lProv.getDescrTipoProvvedimento(),"-")%></font>     </td>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(lProv.getDescrMotivo(),"-")%></font>     </td>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(lProv.getDescrEsito(),"-")%></font>     </td>
    </tr>
  <%
   } // endwhile
  %>
<%
  }  // endif provvedimentiAltri.size()
%>
  </table>
</html>
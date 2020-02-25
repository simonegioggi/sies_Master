<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="f3b.util.DateUtils"%>
<%@page import="f3b.util.StringUtils"%>
<table cellspacing=2 cellpadding=2>
  <tr>
<%if (lPosizione.getDescrPosizioneGiuridica()!=null   && !lPosizione.getDescrPosizioneGiuridica().equals("")){%>
    <td class="l">Posizione Giuridica</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lPosizione.getDescrPosizioneGiuridica())%>&nbsp;</font></td>
  </tr>
<% } %>

<%if (lPosizione.getDataInizio()!=null){%>
  <tr>
    <td class="l">Data di Decorrenza Pena</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizione.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
  </tr>
<% } %>

<% if (lPosizione.getNote()!=null   && !lPosizione.getNote().equals("")){ %>
  <tr>
    <td class="l">Note</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lPosizione.getNote())%>&nbsp;</font></td>
  </tr>
<% } %>
</table>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="f3b.util.StringUtils"%>
<%@page import="f3b.util.DateUtils"%>
<table cellspacing=2 cellpadding=2>
<%if (lPosizione.getDescrPosizioneGiuridica()!=null   && !lPosizione.getDescrPosizioneGiuridica().equals("")){%>
  <tr>
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
<%if(lLuogoDetenzione.getIstDetIdIstitutoDetenzione() != null && !lLuogoDetenzione.getIstDetIdIstitutoDetenzione().equals("")){%>
  <tr>
    <td class="l">Istituto</td>
    <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%>&nbsp;</font>
        di
        <font class="campo"> <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrizione())%>&nbsp;</font>
        -
        <font class="campo"> <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%>&nbsp;</font>
      <%}%>
    &nbsp;
    </td>
  </tr>
<%if (lPosizione.getNote()!=null  && !lPosizione.getNote().equals("")){%>
  <tr>
    <td class="l">Note</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lPosizione.getNote())%>&nbsp;</font></td>
  </tr>
<% } %>
</table>
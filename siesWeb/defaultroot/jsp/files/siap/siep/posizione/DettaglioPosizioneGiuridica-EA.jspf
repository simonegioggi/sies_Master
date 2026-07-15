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
<%if (lPosizione.getLuogoEspiazione()!=null   && !lPosizione.getLuogoEspiazione().equals("")){%>
  <tr>
    <td class="l">Luogo di Espiazione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lPosizione.getLuogoEspiazione())%>&nbsp;</font></td>
  </tr>
<% } %>
<%if (autoritaCompetenteDesc!=null   && !autoritaCompetenteDesc.equals("")){%>
  <tr>
    <td class="l">Autorità Competente per territorio</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(autoritaCompetenteDesc)%>&nbsp;</font></td>
  </tr>
<% } %>

  <tr>
  	<%if (lPosizione.getAutoritaCompetenteSedeDesc()!=null   && !lPosizione.getAutoritaCompetenteSedeDesc().equals("") && !lPosizione.getAutoritaCompetenteSedeDesc().equals("-")){%>
    	<td class="l">Sede</td>
    	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lPosizione.getAutoritaCompetenteSedeDesc())%>&nbsp;</font></td>
    <% } %>
    <%if (lPosizione.getAutoritaCompetenteIndirizzo()!=null   && !lPosizione.getAutoritaCompetenteIndirizzo().equals("")){%>
    	<td class="l">Indirizzo</td>
    	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lPosizione.getAutoritaCompetenteIndirizzo())%>&nbsp;</font></td>
    <% } %>
  </tr>

<%if (lPosizione.getNote()!=null   && !lPosizione.getNote().equals("")){%>
  <tr>
    <td class="l">Note</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lPosizione.getNote())%>&nbsp;</font></td>
  </tr>
<% } %>
</table>
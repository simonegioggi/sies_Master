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

  <%
  //======================================================================
  // Detenuto altra causa
  //======================================================================
  %>
  
  <tr><td class="Titolo" colspan=4>Detenuto per altra causa</td></tr>
  
<%if (lAltraCausa.getDescrTipoPosGiuridica()!=null   && !lAltraCausa.getDescrTipoPosGiuridica().equals("")){%>
  <tr>
    <td class="l">Tipo Misura</td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getDescrTipoPosGiuridica())%>&nbsp;</font>
    </td>
  </tr>
<% } %>
<%if(lAltraCausa.getIstitutoDetenzione() != null){%>
  <tr>
    <td class="l">Istituto</td>
    <td class="l">     
        <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto())%>&nbsp;</font>
        di
        <font class="campo"> <%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getDescrizione())%>&nbsp;</font>
        -
        <font class="campo"> <%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getIndirizzo())%>&nbsp;</font>    
      &nbsp;
    </td>
  </tr>
 <%} else if(lLuogoDetenzione.getIstDetIdIstitutoDetenzione() != null && !lLuogoDetenzione.getIstDetIdIstitutoDetenzione().equals("")){%>
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
  
 <%if ( (lAltraCausa.getAnno()!=null   && !lAltraCausa.getAnno().equals("")) ||
	    (lAltraCausa.getNumero()!=null   && !lAltraCausa.getNumero().equals("")) ){%>
  <tr>
    <td class="l">Anno/Numero SIEP</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAnno())%>/<%=StringUtils.toStringJSP(lAltraCausa.getNumero())%></font></td>
  </tr>
  <%}%>
  <tr>  
	<%if (lAltraCausa.getDescrAutorita()!=null   && !lAltraCausa.getDescrAutorita().equals("") && !lAltraCausa.getDescrAutorita().equals("-")){%>
	    <td class="l">Autorità</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getDescrAutorita())%>&nbsp;</font></td>
	<%}%>
	<%if (lAltraCausa.getDescrLuogo()!=null   && !lAltraCausa.getDescrLuogo().equals("") && !lAltraCausa.getDescrLuogo().equals("-")){%>
	    <td class="l">Luogo</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getDescrLuogo())%>&nbsp;</font></td>
	<%}%>
  </tr>

  <%if (lAltraCausa.getDataScadenza()!=null   && !lAltraCausa.getDataScadenza().equals("")){%>
  <tr>
    <td class="l">Data Scadenza Altra Pena</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getDataScadenza(), "dd-MM-yyyy"))%>&nbsp;</font></td>
  </tr>
  <%}%>
  <%if (lPosizione.getNote()!=null   && !lPosizione.getNote().equals("")){%>
  <tr>
    <td class="l">Note</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lPosizione.getNote())%>&nbsp;</font></td>
  </tr>
  <%}%>
</table>
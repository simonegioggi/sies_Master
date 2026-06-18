<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="f3b.util.StringUtils"%>
<%@page import="f3b.util.DateUtils"%>
<table cellspacing=2 cellpadding=2>
  <tr>
    <td class="l">Posizione Giuridica</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lPosizione.getDescrPosizioneGiuridica())%>&nbsp;</font></td>
  </tr>
  <tr>
    <td class="l">Data di Decorrenza</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizione.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
  </tr>
  
  <!-- OSCURATA POSIZIONE PROCESSUALE - modifica del 06-10-2006
  <tr>
    <td class="l">Posizione Processuale</font></td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lPosizione.getDescrPosizioneProcessuale())%>&nbsp;</font></td>
  </tr>
  -->
  
  <tr>
    <td class="l">Istituto</td>
    <td class="l">
      <%if(lLuogoDetenzione.getIstDetIdIstitutoDetenzione() != null && !lLuogoDetenzione.getIstDetIdIstitutoDetenzione().equals("")){%>
        <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%>&nbsp;</font>
        di
        <!--font class="campo"> <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%>&nbsp;</font-->
        <font class="campo"> <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrizione())%>&nbsp;</font>
        -
        <font class="campo"> <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%>&nbsp;</font>
      <%}%>
    &nbsp;
    </td>

    <td class="l">Altro Luogo</td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%>&nbsp;</font>
    </td>
  </tr>
  <tr>
    <td class="l">Presso</td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(lPosizione.getLuogoProvaAffidamento())%>&nbsp;</font>
    </td>
  </tr>
  <tr>
    <td class="l">Luogo</td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(lPosizione.getLuogoLavoroSemiliberta())%>&nbsp;</font>
    </td>
  </tr>
  
  
  <%
  //======================================================================
  // Detenuto altra causa
  //======================================================================
  %>
  <tr><td class="Titolo" colspan=4>Detenuto per altra causa</td></tr>
  <tr>
    <td class="l">Tipo Misura</td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getDescrTipoPosGiuridica())%>&nbsp;</font>
    </td>
  </tr>
  <tr>
    <td class="l">Data di Decorrenza</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getDataDecorrenza(), "dd-MM-yyyy"))%>&nbsp;</font></td>
  </tr>
  <tr>
    <td class="l">Data di Scadenza</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getDataScadenza(), "dd-MM-yyyy"))%>&nbsp;</font></td>
  </tr>
  <tr>
    <td class="l">Istituto</td>
    <td class="l">
      <%if(lAltraCausa.getIstitutoDetenzione() != null){%>
        <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto())%>&nbsp;</font><!--/td-->
        di
        <font class="campo"> <%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getDescrizione())%>&nbsp;</font>
        -
        <font class="campo"> <%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getIndirizzo())%>&nbsp;</font>
      <%}%>
      &nbsp;
    </td>
    <td class="l">Altro Luogo</td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%>&nbsp;</font>
    </td>
  </tr>
  <tr>
    <td class="l">Anno/Numero Tit. Esec.</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAnno())%>/<%=StringUtils.toStringJSP(lAltraCausa.getNumero())%></font></td>
    <td class="l">Data</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getData(), "dd-MM-yyyy"))%>&nbsp;</font></td>
  </tr>
  <tr>
    <td class="l">Autorità</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getDescrAutorita())%>&nbsp;</font></td>
    <td class="l">Luogo</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getDescrLuogo())%>&nbsp;</font></td>
  </tr>
  <tr>
    <td class="l">Note</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lPosizione.getNote())%>&nbsp;</font></td>
  </tr>
</table>
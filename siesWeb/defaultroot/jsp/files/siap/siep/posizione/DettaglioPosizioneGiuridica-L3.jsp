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
  <tr><td class="Titolo" colspan=4>Detenuto per altra causa</td></tr>

  <%
  //======================================================================
  // Misura Cautelare
  //======================================================================
  %>


  <%if ((lMisuraCautelare.getAnnoRgnr()!=null   && !lMisuraCautelare.getAnnoRgnr().equals("")) ||
	    (lMisuraCautelare.getNumeroRgnr()!=null   && !lMisuraCautelare.getNumeroRgnr().equals("")) ){%> 
  <tr>
    <td class="l">Anno/Numero RG.N.R. </td>
    <td class="l">
    	<font class="campo"><%=StringUtils.toStringJSP(lMisuraCautelare.getAnnoRgnr())%>/<%=StringUtils.toStringJSP(lMisuraCautelare.getNumeroRgnr())%></font>&nbsp;
    </td>
  </tr>
  <% } %>
  
  <tr>
    <%if (ufficioPmTipoDesc!=null   && !ufficioPmTipoDesc.equals("")){%>
    <td class="l">Tipo Ufficio PM</td>
    <td class="l">
    	<font class="campo"><%=StringUtils.toStringJSP(ufficioPmTipoDesc)%></font>&nbsp;
    </td>
    <% } %>
    <%if (ufficioPmSedeDesc!=null   && !ufficioPmSedeDesc.equals("")){%>
    <td class="l">Sede PM</td>
    <td class="l">
    	<font class="campo"><%=StringUtils.toStringJSP(ufficioPmSedeDesc)%></font>&nbsp;
    </td>
    <% } %>
  </tr>
  
    <%if ((lMisuraCautelare.getAnnoFascBdmc()!=null   && !lMisuraCautelare.getAnnoFascBdmc().equals("")) ||
	    (lMisuraCautelare.getNumeFascBdmc()!=null   && !lMisuraCautelare.getNumeFascBdmc().equals("")) ){%> 
  <tr>
    <td class="l">Anno/Numero B.D.M.C. </td> 
    <td class="l">
    	<font class="campo"><%=StringUtils.toStringJSP(lMisuraCautelare.getAnnoFascBdmc())%>/<%=StringUtils.toStringJSP(lMisuraCautelare.getNumeFascBdmc())%></font>&nbsp;
    </td>
  </tr>
  <% } %>
  
  <%if ((lMisuraCautelare.getAnnoRegGen()!=null   && !lMisuraCautelare.getAnnoRegGen().equals("")) ||
	    (lMisuraCautelare.getNumeroRegGen()!=null   && !lMisuraCautelare.getNumeroRegGen().equals("")) ){%> 
  <tr>
    <td class="l">Anno/Numero Reg.Gen.</td>
    <td class="l">
    	<font class="campo"><%=StringUtils.toStringJSP(lMisuraCautelare.getAnnoRegGen())%>/<%=StringUtils.toStringJSP(lMisuraCautelare.getNumeroRegGen())%></font>&nbsp;
    </td>
  </tr>
  <% } %> 
  <%if (tipoUfficioRegGenDesc!=null   && !tipoUfficioRegGenDesc.equals("")  && !tipoUfficioRegGenDesc.equals("-")){%>
  <tr>
    <td class="l">Tipo Ufficio Reg.Gen.</td>
    <td class="l">
    	<font class="campo"><%=StringUtils.toStringJSP(tipoUfficioRegGenDesc)%></font>&nbsp;
    </td>
  </tr>
  <% } %> 
  <tr>
    <%if (autoritaEmittenteCautelareDesc!=null   && !autoritaEmittenteCautelareDesc.equals("")){%>
	    <td class="l">Autorità Emittente</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(autoritaEmittenteCautelareDesc)%>&nbsp;</font></td>
    <% } %> 
    <%if (lMisuraCautelare.getAutoritaEmittenteLuogoDesc()!=null && !lMisuraCautelare.getAutoritaEmittenteLuogoDesc().equals("") && !lMisuraCautelare.getAutoritaEmittenteLuogoDesc().equals("-")){%>
	    <td class="l">Luogo Emittente</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lMisuraCautelare.getAutoritaEmittenteLuogoDesc())%>&nbsp;</font></td>
    <% } %> 
  </tr>
  
  <%if (lMisuraCautelare!=null && lMisuraCautelare.getDataEmissioneOrdinanza()!=null){%>
  <tr>
    <td class="l">Data emissione Ordinanza</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisuraCautelare.getDataEmissioneOrdinanza(),"dd-MM-yyyy"))%>&nbsp;</font></td>
  </tr>
  <% } %>

  <%if (lMisuraCautelare.getAltroLuogoDetenzione()!=null && !lMisuraCautelare.getAltroLuogoDetenzione().equals("") && !lMisuraCautelare.getAltroLuogoDetenzione().equals("-")){%>
  <tr>
    <td class="l">Luogo di Espiazione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lMisuraCautelare.getAltroLuogoDetenzione())%>&nbsp;</font></td>
  </tr>
  <% } %> 
  
  <%if (autoritaCompetenteCautelareDesc!=null   && !autoritaCompetenteCautelareDesc.equals("")){%>
  <tr>
    <td class="l">Autorità Competente per territorio</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(autoritaCompetenteCautelareDesc)%>&nbsp;</font></td>
  </tr>
  <% } %> 
  
  <tr>
    <%if (lMisuraCautelare.getAutoritaCompetenteSedeDesc()!=null && !lMisuraCautelare.getAutoritaCompetenteSedeDesc().equals("") && !lMisuraCautelare.getAutoritaCompetenteSedeDesc().equals("-")){%>
	    <td class="l">Sede</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lMisuraCautelare.getAutoritaCompetenteSedeDesc())%>&nbsp;</font></td>
	<% } %> 
	<%if (lMisuraCautelare.getAutoritaCompetenteIndirizzo()!=null   && !lMisuraCautelare.getAutoritaCompetenteIndirizzo().equals("")){%>
	    <td class="l">Indirizzo</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lMisuraCautelare.getAutoritaCompetenteIndirizzo())%>&nbsp;</font></td>
	<% } %> 
  </tr>

  <%if (lMisuraCautelare.getDescrTipoMisura()!=null   && !lMisuraCautelare.getDescrTipoMisura().equals("")){%>
  <tr>
    <td class="l">Tipo Misura</td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(lMisuraCautelare.getDescrTipoMisura())%>&nbsp;</font>
    </td>
  </tr>
  <% } 
     else if (lAltraCausa!=null && lAltraCausa.getDescrTipoPosGiuridica()!=null   && !lAltraCausa.getDescrTipoPosGiuridica().equals("")){%>
  <tr>
    <td class="l">Tipo Misura</td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getDescrTipoPosGiuridica())%>&nbsp;</font>
    </td>
  </tr>
  <% } %> 
  
  <%
  //======================================================================
  // Detenuto altra causa
  //======================================================================
  %>
  
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
  <%}%>
  <%if (lPosizione.getNote()!=null   && !lPosizione.getNote().equals("")){%>
  <tr>
    <td class="l">Note</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lPosizione.getNote())%>&nbsp;</font></td>
  </tr>
  <%}%>
</table>
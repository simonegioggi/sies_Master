<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<jsp:useBean id="soggetto" scope="session" class="siap.sico.soggetto.model.SoggettoModel"/>



  <table width="100%">
  	<tr>
		<td class="Titolo" colspan=6>Soggetto di Riferimento</td>
	</tr>
    <tr>
      <td class="l"><font class="label">Cognome e Nome</font></td>
      <td class="l"><font class="campo"><%=soggetto.getCognome() %>&nbsp;&nbsp;<%=soggetto.getNome() %></font></td>
     </tr>
    <tr>
      <td class="l"><font class="label">Sesso</font></td>
      <td class="l"><font class="campo"><%=soggetto.getSesso()%>&nbsp;</font></td>
    </tr>
	<tr>
      <td class="l" width="25%">
        <font  class="label">Data di nascita</font>
      </td>
      <td class="l" width="25%">
        <font class="campo">
<%
        if(soggetto.getDataNascita() != null)
        {
%>
          <%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
        }
        else
        {
%>
          <%="**-"+StringUtils.toStringJSP(soggetto.getMeseNascita(), "**")+"-"+StringUtils.toStringJSP(soggetto.getAnnoNascita())%>&nbsp;
<%
        }
%>
        </font>
      </td>
      <td class="l" width="25%">
        <font class="label">Presunta</font>
      </td>
      <td class="l" width="25%">
        <font class="campo"><%=StringUtils.toStringJSP(soggetto.getDataNascitaPresunta())%></font>
      </td>
    </tr>
    <tr>
      <td class="l"><font  class="label">Comune Nascita</font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(soggetto.getDescrComuneNascita() )%>
<%        if( (soggetto.getDescrComuneNascita() != null)
              && (!(soggetto.getDescrComuneNascita().equals("")))
              && (!(soggetto.getDescrComuneNascita().equals("-"))) )
          {
%>
            (<%=soggetto.getCodProvinciaNascita()%>)
<%
          }
%>
          &nbsp;
        </font>
      </td>
    </tr>
	<tr>
	  <td class="l"><font class="label">Nazionalità</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrNazionalita())%>&nbsp;</font></td>
      <td class="l"><font  class="label">Stato Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrStatoNascita())%>&nbsp;</font></td>
    </tr>
	 <tr>
      <td class="l"><font class="label">Comune Di Nascita Estero</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescComuneNascitaEstero()).toUpperCase()%>&nbsp;</font></td>
    </tr>
	  <tr>
      <td class="l"><font class="label">Paternità</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getPaternita())%>&nbsp;</font></td>
    </tr>
	 <tr>
      <td class="l"><font class="label">Nome Madre</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getNomeMadre())%>
      <%=StringUtils.toStringJSP(soggetto.getCognomeMadre())%>&nbsp;</font></td>
    </tr>
	    <tr>
       <td class="l"><font class="label">Codice CUI</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodAfis() )%>&nbsp;</font></td>
      <td>&nbsp;</td><td>&nbsp;</td>
      </tr>

    <tr>
      <td class="l"><font class="label">Atto Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getAttoNascita()) %>&nbsp;</font></td>
    </tr>

  </table>

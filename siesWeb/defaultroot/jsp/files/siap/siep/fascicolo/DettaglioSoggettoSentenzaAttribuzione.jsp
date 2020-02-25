<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<jsp:useBean id="soggetto" scope="session" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="sentenza" scope="session" class="siap.siep.sentenza.model.SentenzaModel" />

  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
       <td class="L" width=100%><font class="label">Soggetto: </font>&nbsp;
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>" title="Soggetto">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
        </a>
      </font>&nbsp;
<%
if(soggetto.getDataNascita() == null){
   if(soggetto.getDataNascitaPresunta().equals("S")) {
    	if (soggetto.getSesso().compareTo("F")==0)
    	{
%>
      		<font class="label">nata il :</font>&nbsp;
<%
    	} else {
%>
      		<font class="label">nato il :</font>&nbsp;
<%
    	}
    	
    	if(soggetto.getAnnoNascita() != null){
%>
        	<font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
<%
    	} else {
%>
			<font class="campo">**-**-****</font>&nbsp;
<%    	
   		}	
    	
   }else if (soggetto.getEtaPresuntaAnni() != null || soggetto.getEtaPresuntaMesi()!=null ){ %>
      	<font class="label">Età Presunta: </font>
<%
		if (soggetto.getEtaPresuntaAnni() != null){
%>
			anni <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaAnni())%>&nbsp;</font>
<%
		}
		if (soggetto.getEtaPresuntaMesi() != null){
%>
			mesi <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaMesi())%>&nbsp;</font>
<%			
		}
%>

<% }else {%>
      <font class="campo">**-**-****</font>&nbsp;
<% }

}else {
    if (soggetto.getSesso().compareTo("F")==0)
    {
%>
      <font class="label">nata il :</font>&nbsp;
<%
    }
    else
    {
%>
      <font class="label">nato il :</font>&nbsp;
<%
    }
%>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
<%
} // chiude else presenza data nascita
%>
      <font class="label">in : </font>&nbsp;
      <font class="campo">
<%
      if (soggetto.getDescrComuneNascita().compareTo("-")==0)
      {
%>
       <%=soggetto.getDescComuneNascitaEstero()%>  (<%=soggetto.getDescrStatoNascita().toUpperCase()%>)
<%
      }
      else
      {
%>
        <%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)
<%
      }
%>

      </font>
    </tr>
    <tr>
      <td class="L">
        <font class="campo"><%=sentenza.getDescrTipoProvvedimento()%></font>&nbsp;<font class="label">N. </font>&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(sentenza.getAnnoSentenza())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroSentenza())%> &nbsp;   
          <font class="label">del</font>&nbsp;
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
            <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>
          </a>
        </font>
        <%if(!sentenza.getCodTipoProvvedimento().equals("02")) { %> &nbsp;<font class="label"> Emessa da: </font> <% 
 		}else{%>&nbsp;<font class="label"> Emesso da: </font><%} %>
        <font class="campo"><%=sentenza.getDescrTipoAutoritaEmittente()%></font> &nbsp;
<%
        if (sentenza.getNumSezioneAutoritaEmittente() != null)
        {
%>
          <font class="label">(Sez.</font> <font class="campo"><%=sentenza.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
<%
        }
%>
        <font class="label"> di </font>&nbsp;
        <font class="campo"><%=sentenza.getDescrLuogoEmittente()%></font>
      </td>
    </tr>
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--tr>
      <td class="L">
        <font class="label">Data irrevocabilità : </font>&nbsp;
        <font class="campo"><%=DateUtils.getDateToString(sentenza.getDataIrrevocabilita(), "dd-MM-yyyy")%></font>
      </td>
    </tr--%>
  </table>
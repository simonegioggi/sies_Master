<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<%
  SoggettoModel soggetto = fascicolo.getSoggetto();
  SentenzaModel sentenza = fascicolo.getSentenza();
%>
  <table cellspacing=0 cellpadding=0 width=95%>

    <tr>
      <td class="L">
        <font class="label">Procedimento N.</font>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>" title="Procedimento">
          <%=fascicolo.getChiaveAnno()%>
          /
          <%=fascicolo.getChiaveProgr()%>
          &nbsp;-&nbsp; <%=fascicolo.getDescrTipoUfficio()%> &nbsp; <%=fascicolo.getDescrComuneUfficio()%>
        </a>
      </td>
    </tr>
    <tr>
      <td class="L" width=100%><font class="label">Soggetto:</font>
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>" title="Soggetto">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
        </a>
      </font>&nbsp;
<%
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

if(soggetto.getDataNascita() == null){
   if(soggetto.getDataNascitaPresunta().equals("S")) {%>
      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
<%}else
   {%>
      <font class="campo">***</font>&nbsp;
<%}}else{%>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
<%}%>
      <font class="label">in : </font>
      <font class="campo">

 <%     if (soggetto.getDescrComuneNascita().compareTo("-")==0)
      {
%>
        <%=soggetto.getDescrStatoNascita()%>
<%
      }
      else
      {
%>
        <%=soggetto.getDescrComuneNascita()+ "  ("+soggetto.getCodProvinciaNascita()+")" %>
<%
      }
%>
      </font>
     </td>
    </tr>
    <tr>
      <td class="L">
        <font class="campo"><%=sentenza.getDescrTipoProvvedimento()%></font>&nbsp;<font class="label">N.</font>
        <font class="campo">
          <%=sentenza.getAnnoSentenza()%> / <%=sentenza.getNumeroSentenza()%>&nbsp;
          <font class="label">del</font>&nbsp;
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
            <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>
          </a>
        </font>
        &nbsp;<font class="label"> Emessa da: </font>
        <font class="campo"><%=sentenza.getDescrTipoAutoritaEmittente()%></font>&nbsp;
<%
        if (sentenza.getNumSezioneAutoritaEmittente() != null)
        {
%>
          <font class="label">(Sez.</font> <font class="campo"><%=sentenza.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
<%
        }
%>
        <font class="label"> di </font>
        <font class="campo"><%=sentenza.getDescrLuogoEmittente()%></font>
      </td>
    </tr>
    <tr>
      <td class="L">
        <font class="label">Data irrevocabilità : </font>&nbsp;
        <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--
        // modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009  
		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sentenza.getDataIrrevocabilita(), "dd-MM-yyyy"), "-" )%></font>
		--%>
      </td>
    </tr>
  </table>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="dettagliofascicolo" scope="request" class="siap.siep.fascicolo.model.DettaglioFascicoloModel" />

<%
  FascicoloSiepModel fascicolo = dettagliofascicolo.getFascicoloSiep();
  SoggettoModel soggetto =  dettagliofascicolo.getFascicoloSiep().getSoggetto();
  SentenzaModel sentenza =  dettagliofascicolo.getFascicoloSiep().getSentenza();
%>
  <table cellspacing=1 cellpadding=1 width=95%>
      <tr>
        <td class="Titolo" colspan=4>Dati Procedimento&nbsp;&nbsp;&nbsp;&nbsp;</td>
      </tr>
    <tr>
      <td class="L">
        <font class="label">Procedimento N.</font>
  <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>" title="Procedimento">
    <%=StringUtils.toStringJSP(fascicolo.getChiaveAnno())%>
    /
    <%=StringUtils.toStringJSP(fascicolo.getChiaveProgr())%>
  </a>&nbsp;

  <font class="label">Data Iscrizione :</font>
        <font class="campo">
         <%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataInserimento(),"dd-MM-yyyy"))%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="L" width=100%><font class="label">Soggetto:</font>
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>" title="Soggetto">
          <%=StringUtils.toStringJSP(soggetto.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(soggetto.getNome())%>
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
<%
      if (soggetto.getDescrComuneNascita().compareTo("-")==0)
      {
%>
        <%=StringUtils.toStringJSP(soggetto.getDescrStatoNascita())%>
<%
      }
      else
      {
%>
        <%=StringUtils.toStringJSP(soggetto.getDescrComuneNascita())+ "  ("+StringUtils.toStringJSP(soggetto.getCodProvinciaNascita())+")" %>
<%
      }
%>
      </font>
     </td>
    </tr>
    <tr>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrTipoProvvedimento())%></font>&nbsp;<font class="label">N.</font>
        <font class="campo">
          <%=StringUtils.toStringJSP(sentenza.getAnnoSentenza())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroSentenza())%>&nbsp;
          <font class="label">del</font>&nbsp;
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy"))%>
          </a>
        </font>
        <%if(!sentenza.getCodTipoProvvedimento().equals("02")) { %> &nbsp;<font class="label"> Emessa da: </font> <% 
    }else{%>&nbsp;<font class="label"> Emesso da: </font><%} %>
        <font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%></font>&nbsp;
<%
        if (sentenza.getNumSezioneAutoritaEmittente() != null)
        {
%>
          <font class="label">(Sez.</font> <font class="campo"><%=StringUtils.toStringJSP(sentenza.getNumSezioneAutoritaEmittente())%> </font> <font class="label">) </font>
<%
        }
%>
        <font class="label"> di </font>
        <font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%></font>
      </td>
    </tr>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%-- 
//  modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
%if(sentenza.getDataIrrevocabilita()!=null){%>
    <tr>
      <td class="L">
        <font class="label">Data irrevocabilità : </font>
        <font class="campo">< %=StringUtils.toStringJSP(DateUtils.getDateToString(sentenza.getDataIrrevocabilita(), "dd-MM-yyyy"))%></font>
      </td>
    </tr>
< %}%
--%>
<%if(fascicolo.getNote()!=null){%>
     <tr>
      <td class="L">
        <font class="label">Note : </font>
        <font class="campo"><%=StringUtils.toStringJSP(fascicolo.getNote())%></font>
      </td>
    </tr>
<%}%>

  </table>
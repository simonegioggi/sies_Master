<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicolo" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="soggetto" scope="request" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="sentenza" scope="request" class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="penaresidua" scope="session" class="siap.siep.penaresidua.model.PenaResiduaModel" />

  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="label">Procedimento : N.</font>&nbsp;
        	<font class="cRosso"> <%=fascicolo.getChiaveAnno()%> / <%=fascicolo.getChiaveProgr()%></font>&nbsp;
<%
          if(fascicolo.getFlagCumulante()!=null && fascicolo.getFlagCumulante().equals("S"))
          {
%>
            <font class="cRossoCumulo"> &nbsp;C&nbsp; </font> &nbsp;
<%
          }
          if(fascicolo.getCodOperatoreInserimento() != null && fascicolo.getCodOperatoreInserimento().startsWith("res-"))
          {
%>
            <font class="cRossoCumulo"> &nbsp;Migrato&nbsp; </font> &nbsp;
<%
          }

          if(   fascicolo.getCodStatoFascicolo() != null
             && (fascicolo.getCodStatoFascicolo().equals("01"))
             )
          {
%>
            <font class="cRossoCumulo"> &nbsp;Archiviato&nbsp;</font> &nbsp;
<%
          }

        if((    penaresidua != null
             && penaresidua.getFlagPenaSospesa()!= null
             && penaresidua.getFlagPenaSospesa().equals("S"))
             || (     fascicolo!= null && fascicolo.getChiaveProgr() != null
                  && (fascicolo.getChiaveProgr().intValue() >= 30000
                  && fascicolo.getChiaveProgr().intValue() < 40000)))
        {
          if( fascicolo!= null && fascicolo.getChiaveProgr() != null
            && ( fascicolo.getChiaveProgr().intValue() >= 30000
            &&   fascicolo.getChiaveProgr().intValue() < 40000) )
          {
%>
            <font class="cRossoCumulo"> &nbsp;Pena Sospesa Condizionalmente&nbsp;</font>&nbsp;
<%
          }
          else
          {
%>
            <font class="cRossoCumulo"> &nbsp;Esecuzione Sospesa&nbsp;</font>&nbsp;
<%
          }
        }

        if(   penaresidua != null
           && penaresidua.getFlagPenaSospesa()!= null
           && penaresidua.getFlagPenaSospesa().equals("I"))
        {
%>
          <font class="cRossoCumulo"> &nbsp;Pena Interrotta&nbsp;</font>&nbsp;
<%
        }
        if(   penaresidua != null
           && penaresidua.getFlagPenaSospesa()!= null
           && penaresidua.getFlagPenaSospesa().equals("D"))
        {
%>
          <font class="cRossoCumulo"> &nbsp;Pena Differita&nbsp;</font>&nbsp;
<%
        }
%>
<%
          if (!(UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio())))
          {
%>
            &nbsp;
            <font class="label"><%=fascicolo.getDescrTipoUfficio() + " DI " + fascicolo.getDescrComuneUfficio() %></font>
<%
          }
%>
      </td>
    </tr>
    <tr>
      <td class="L" width=100%><font class="label">Soggetto : </font>
        <font class="cRosso" title="Soggetto" > <%=soggetto.getCognome()%> / <%=soggetto.getNome()%>
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
     </td>
    </tr>
    <tr>
      <td class="L">
        <font class="label"><%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:<font class="label"> N.</font>
        <font class="cRosso" title="Sentenza" > <%=sentenza.getAnnoSentenza()%> / <%=sentenza.getNumeroSentenza()%>
        </font>&nbsp;
        <font class="label">del</font>&nbsp;
          <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>
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
        <font class="campo"><%=DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "dd-MM-yyyy")%></font>
      </td>
    </tr>
  </table>
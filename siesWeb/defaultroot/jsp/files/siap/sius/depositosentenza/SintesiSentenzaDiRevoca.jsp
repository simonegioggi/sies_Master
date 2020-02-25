<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<%@ page import="siap.sius.depositosentenza.model.SentenzaEventoTenoriPrescrizioniModel"%>
<%@ page import="siap.sius.depositosentenza.action.ICostantiDepositoSentenza"%>

<jsp:useBean id="SentenzaDiRevoca"   scope="request" class="siap.sius.depositosentenza.model.SentenzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="TornaQui"     		 scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloDiRevoca"  scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>


<% if (SentenzaDiRevoca.getSentenza() != null &&  SentenzaDiRevoca.getSentenza().getIdDepositoSentenza() != null)  { %>

<table  border=1>
    <tr>
      <td class="Titolo" colspan="2"> Estremi della Sentenza di Revoca</td>
    </tr>
  <tr>
    <td class="l"> Data Emissione</td>
    <td class="l"> <font class="campo"><%=DateUtils.getDateToString(SentenzaDiRevoca.getEvento().getDataEmissione(), "dd/MM/yyyy")%></font></td>
  </tr>

  <tr>
    <td class="l">
           Anno / Numero Sentenza
    </td>
    <td class="l"><font class="campo">
           <%=StringUtils.toStringJSP(SentenzaDiRevoca.getSentenza().getAnnoSentenza())%> / <%=StringUtils.toStringJSP(SentenzaDiRevoca.getSentenza().getNumSentenza())%>
        </font></td>
   </tr>

  <tr>
    <td class="l"> Data Deposito in Cancelleria</td>
    <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(SentenzaDiRevoca.getSentenza().getDataDeposito(), "dd/MM/yyyy"))%></font></td>
  </tr>

    <tr>
        <td class="L">
            <font class="label">Procedimento N.</font> </td>
         <td class="l"> <font class="campo">
           <%=FascicoloDiRevoca.getFascicoloSiusModel().getChiaveAnno()%>
           /
           <%=FascicoloDiRevoca.getFascicoloSiusModel().getChiaveProgr()%>&nbsp;
          </font></td>
    </tr>
    <tr>
        <td class="l">
         <font class="label">&nbsp;  relativo a </font>
        </td>
        <td class="l">
         <font class="campo"><%=FascicoloDiRevoca.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font>
       </td>
    </tr>
    <tr>
        <td class="l">
        <font class="label">&nbsp; emesso da </font>
        </td>
        <td class="l">
        <font class="campo">
          <%=FascicoloDiRevoca.getFascicoloSiusModel().getDescrTipoUfficio()%>&nbsp;<%=FascicoloDiRevoca.getFascicoloSiusModel().getDescrComuneUfficio()%>
         </font></td>
     </tr>

  <% } else { %>
 <tr><td> <font class="crosso">Mancano i dati relativi alla Sentenza di Revoca !</font></td></tr>
  <% } %>
</table>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>


<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="OrdinanzaRevocata" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>


<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>



<% if (OrdinanzaRevocata.getOrdinanza() != null &&  OrdinanzaRevocata.getOrdinanza().getIdDepositoOrdinanzaPc() != null)  { %>

<jsp:useBean id="fascicoloRevocato"  scope="request" class="siap.sius.fascicolo.model.FascicoloSiusModel"/>

<jsp:include page="<%=ICostantiFascicoloSius.PG_SINTESIPROCEDIMENTOORIGINESIUS%>">
 <jsp:param name="Titolo" value="Dati dell'Ordinanza Revocata" />
</jsp:include>
<table>
  <tr>
    <input Title="Id Evento" type="hidden" name="<%= ICostantiEvento.CAMPO_ID_EVENTO %>" value="" >
  </tr>
  <tr>
    <td class="l"> Tipo di Ordinanza</td>
    <td class="l"> <font class="campo"><%=DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoOrdinanza(), OrdinanzaRevocata.getOrdinanza().getCodTipoOrdinanza())%>
</font></td>
  </tr>

  <tr>
    <td class="l">
           Anno / Numero Ordinanza
    </td>
    <td class="l"><font class="campo">
           <%=StringUtils.toStringJSP(OrdinanzaRevocata.getOrdinanza().getAnnoS3())%> / <%=StringUtils.toStringJSP(OrdinanzaRevocata.getOrdinanza().getNumS3())%>
        </font></td>
   </tr>

  <tr>
    <td class="l"> Data Emissione</td>
    <td class="l"> <font class="campo"><%=DateUtils.getDateToString(OrdinanzaRevocata.getEvento().getDataEmissione(),"dd/MM/yyyy")%></font></td>
  </tr>
  <tr>
    <td class="l"> Data Deposito in Cancelleria</td>
    <td class="l"> <font class="campo"><%=DateUtils.getDateToString(OrdinanzaRevocata.getOrdinanza().getDataDeposito(),"dd/MM/yyyy")%></font></td>
  </tr>

  <% } else { %>
 <tr><td> <font class="crosso">Mancano i dati relativi all'Ordinanza Revocata !</font></td></tr>
  <% } %>
  <tr> <td> <br></td></tr>

</table>


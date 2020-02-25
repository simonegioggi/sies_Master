<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>


<jsp:useBean id="decretoRevocato"  scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>
<jsp:useBean id="permessiRevocati"                    scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicoloRevocato"  scope="request" class="siap.sius.fascicolo.model.FascicoloSiusModel"/>

    <tr>
      <td class="Titolo" colspan=6> Dettaglio Del Decreto Licenza di Riferimento </td>
      <input Title="Id Evento" type="hidden" name="<%=ICostantiEvento.CAMPO_EVE_ID_EVENTO%>" value="<%=decretoRevocato.getIdEventoGenerato()%>" >
    </tr>
    <tr>
      <td class="l">
        <font class="label">Procedimento N.</font>
      </td>
      <td class="l">
        <font class="campo"><%=fascicoloRevocato.getChiaveAnno()%> / <%=fascicoloRevocato.getChiaveProgr()%>&nbsp;</font>
      </td>
    </tr>
  <tr>
    <td class="l"> Data Emissione</td>
    <td class="l"><font class="campo"> <%=DateUtils.getDateToString(decretoRevocato.getDataEmissione(),"dd/MM/yyyy")%></font></td>
  </tr>
<% if (decretoRevocato.getDataDeposito() != null)
{ %>
  <tr>
    <td class="l"> Anno / Numero del Decreto</td>
    <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(decretoRevocato.getAnnoS72())%> / <%=StringUtils.toStringJSP(decretoRevocato.getNumS72())%></font></td>
  </tr>
  <tr>
    <td class="l"> Data Deposito in Cancelleria</td>
    <td class="l"> <font class="campo"><%=DateUtils.getDateToString(decretoRevocato.getDataDeposito(),"dd/MM/yyyy")%></font></td>
  </tr>
<% } %>
<%
        Iterator itx1 = permessiRevocati.iterator();

        while ( itx1.hasNext())
        {
          LicenzaLibAnticipataModel lLic = (LicenzaLibAnticipataModel)itx1.next();
%>
          <tr>
            <td class="l">Durata</td>
            <td class="l"> <% if(lLic.getNumeroGiorni() != null) {%> giorni <font class="campo"> <%=" " + StringUtils.toStringJSP(lLic.getNumeroGiorni(),"-") + " "%> </font> <% } if(lLic.getNumeroOre() != null) {%> ore <font class="campo"> <%=" " + StringUtils.toStringJSP(lLic.getNumeroOre(),"-")%> </font> <%}%></td>
          </tr>
          <tr>
            <td class="l">Luogo fruizione</td>
            <td class="l"> <font class="campo"> <%=StringUtils.toStringJSP(lLic.getLuogoSvolgimentoProva(),"-")%></font></td>
         </tr>
<%
        }
%>
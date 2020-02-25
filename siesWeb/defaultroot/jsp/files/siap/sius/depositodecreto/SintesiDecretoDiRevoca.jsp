<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>


<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloDiRevoca"   scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="decretoDiRevoca"  scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>


<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<table  border=1>

<% if (decretoDiRevoca != null &&  decretoDiRevoca.getIdDepositoDecreto() != null)  { %>

    <tr>
      <td class="Titolo" colspan="2"> Estremi del Decreto di Revoca</td>
    </tr>
  <tr>
    <td class="l"> Data Emissione</td>
    <td class="l"> <font class="campo"><%=DateUtils.getDateToString(decretoDiRevoca.getDataEmissione(),"dd/MM/yyyy")%></font></td>
  </tr>

  <tr>
    <td class="l">
           Anno / Numero Decreto
    </td>
    <td class="l"><font class="campo">
           <%=StringUtils.toStringJSP(decretoDiRevoca.getAnnoS72())%> / <%=StringUtils.toStringJSP(decretoDiRevoca.getNumS72())%>
        </font></td>
   </tr>

  <tr>
    <td class="l"> Data Deposito in Cancelleria</td>
    <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoDiRevoca.getDataDeposito(),"dd/MM/yyyy"))%></font></td>
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
 <tr><td> <font class="crosso">Mancano i dati relativi al Decreto di Revoca !</font></td></tr>
  <% } %>
</table>


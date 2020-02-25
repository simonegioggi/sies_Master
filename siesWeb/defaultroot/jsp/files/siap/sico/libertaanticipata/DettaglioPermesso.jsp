<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.lang.String" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>


<jsp:useBean id="Licenze"     scope="request" class="java.util.Vector"/>

<%
  if (Licenze.size() > 0)
  {
    Iterator itx = Licenze.iterator();
    LicenzaLibAnticipataModel permesso = (LicenzaLibAnticipataModel) Licenze.get(0);
%>
    <table cellspacing="2" cellpadding="2">
    <tr> <td> <br></td></tr>
    <tr>
        <td class="Titolo" colspan=6> Permesso <td>
    </tr>
          <tr>
            <td class="l">Durata</td>
            <td class=l><%= "Giorni "+StringUtils.toStringJSP(permesso.getNumeroGiorni(),"-") + " ore " + StringUtils.toStringJSP(permesso.getNumeroOre(),"-")%></td>
          </tr>
    <tr> <td> <br></td></tr>
   </table>
<%
  }
%>




<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.regesies.regesentenza.action.ICostantiRegeSentenza" %>
<%@ page import="siap.regesies.regeavvocato.model.RegeAvvocatoModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>


<jsp:useBean id="provvedimento" scope="request" class="siap.regesies.regesentenza.model.ProvvedimentoModel"/>


<html>
<%
      Vector lAvvocati = provvedimento.getDifensori();
      if(lAvvocati != null && lAvvocati.size() != 0)
      {%>
        <table cellspacing=1 cellpadding=1  width="50%">
  <tr>
      <td class="LBGISIV" width="10%"><font class="campoLow">Tipo</font>  </td>
      <td class="LBGISIV" width="70%"><font class="campoLow">Cognome Nome </font></td>
      <td class="LBGISIV" width="20%"><font class="campoLow">Foro </font> </td>
 </tr>

<%
        Iterator lIterAvv = lAvvocati.iterator();
        while(lIterAvv.hasNext())
        {
          RegeAvvocatoModel lAvvocato = (RegeAvvocatoModel)lIterAvv.next();

%>
            <tr>
            <td class="L">  <font class="label">
            <%=lAvvocato.getDescrTipoAvvocato() %></font>
            </td>
              <td class="l">
                <font class="campo">
                <%=lAvvocato.getCognome() %> &nbsp; <%=lAvvocato.getNome() %> </font>
                 </td> <td class="l">
                  <font class="campo"><%=lAvvocato.getForo() %></font>
              </td>
            </tr>
<%
        }
      }
else
      {%>
       <table cellspacing=1 cellpadding=1 width="50%">
       <tr>
        <td class="l">
       <font class="cGrigio">Nessun Difensore per il provvedimento.</font>
      </td>
    </tr>
 <%}%>
        </table>
</html>
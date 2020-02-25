<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.regesies.regesentenza.action.ICostantiRegeSentenza" %>
<%@ page import="siap.regesies.regesentenza.model.RegeSentenzaModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.regesies.regeresidenza.model.RegeResidenzaModel" %>


<jsp:useBean id="provvedimento" scope="request" class="siap.regesies.regesentenza.model.ProvvedimentoModel"/>


<html>
<table width="50%">

<%
    Vector residenza = provvedimento.getResidenze();

    if (residenza != null && residenza.size()>0)
    {
    %>
      <tr>
      <td class="LBGISIV" width="10%"><font class="campoLow">Tipo</font>  </td>
      <td class="LBGISIV" width="70%"><font class="campoLow">Indirizzo </font></td>
 </tr><%
      Iterator itx = residenza.iterator();
     while ( itx.hasNext())
    {
      RegeResidenzaModel lRes = (RegeResidenzaModel)itx.next();
     %>
     <tr>
       <td class="L"><font class="label"><%=lRes.getDescrTipoResidenza()%></font>
       </td>
      <td class="l"><font class="campo"><%=lRes.getStringaResidenza()%></font>
      </td>
    </tr>
<%
    }

  } // endif residenza
  else
  {%>
  <tr>
  <td class="l">
       <font class="cGrigio">Nessuna Residenza/Domicilio per il provvedimento.</font>
    </td>
    </tr>
 <%}%>



</table>
</html>
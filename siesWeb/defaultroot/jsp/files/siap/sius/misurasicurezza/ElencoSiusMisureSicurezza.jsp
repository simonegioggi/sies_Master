<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@page import="siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza"%>

<jsp:useBean id="misuresicurezza" scope="request" class="java.util.Vector" />

    <table cellspacing=4 cellpadding=4 width=95%>
<%
      // Elenco Misure Sicurezza
      if (misuresicurezza != null && (misuresicurezza.size()> 0))
      {
%>
        <tr>
          <td class="Titolo" colspan=4> Misure Sicurezza </td>
        </tr>
<%
        // Controllo Misure Sicurezza
			Iterator lIndMisure = misuresicurezza.iterator();
        	while (lIndMisure.hasNext())
        	{
        		MisuraSicurezzaModel lMisura = (MisuraSicurezzaModel) lIndMisure.next();
%>
          		<tr>
          		<%  if (lMisura.getEveIdEvento() == null) { %>
            		<td class=l colspan=2>Precedente</td>
            		<td class=l colspan=2>
            	<%  }
          			else if (lMisura.getEvento().getCodEsito() != null && lMisura.getEvento().getCodEsito().equals("0192")) { %>
          			<tr> <td>&nbsp;</td> </tr>	
          			<td class=l colspan=2>Nuova (rideterminata)</td>
            		<td class=l colspan=2><font class="crosso">
				<%
          			} else {
				%>
          			<tr> <td>&nbsp;</td> </tr>	
          			<td class=l colspan=2>Nuova (applicata)</td>
            		<td class=l colspan=2><font class="crosso">
             	<%  }  %>
          			
            		<% if (lMisura.getDescrNatura() != null) %> <%=lMisura.getDescrNatura()%> - 
            		<% if (lMisura.getDescrTipo() != null) %> <%=lMisura.getDescrTipo()%> - 
            		<% if (lMisura.getNumAnni() != null)   { %> Anni <%=lMisura.getNumAnni()%>  <% } 
            		   if (lMisura.getNumMesi() != null)   { %> Mesi <%=lMisura.getNumMesi()%>  <% }
					   if (lMisura.getNumGiorni() != null) { %> Giorni <%=lMisura.getNumGiorni()%>   <% } %>    
            		</font></td>    		
          		</tr>
<%
        	}
      }
%>
      <tr>  <td> </td> </tr>
      <tr>  <td> </td> </tr>
    </table>
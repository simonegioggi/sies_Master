<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel"%>
<%@ page import="siap.sius.esecuzionemisurasicurezza.action.ICostantiEsecuzioneMS"%>

<jsp:useBean id="misureSicurezzaRideterminate" scope="request" class="java.util.Vector" />

    <table cellspacing=4 cellpadding=4 width=95%>
<%
      // Elenco Misure Sicurezza
      if (misureSicurezzaRideterminate != null && (misureSicurezzaRideterminate.size()> 0))
      {
%>
        <tr>
          <td class="Titolo" colspan=4> Misure di Sicurezza Rideterminate a seguito unificazione </td>
        </tr>
<%
            // Controllo Esecuzione Misure Sicurezza Rideterminate
			Iterator lIndMisureSicRid = misureSicurezzaRideterminate.iterator();
        	while (lIndMisureSicRid.hasNext())
        	{
        		EsecuzioneMisuraSicurezzaModel lEseMisRid = (EsecuzioneMisuraSicurezzaModel) lIndMisureSicRid.next();
%>
          		<tr>
 					<td class=l colspan=4>           		
	            		<% if (lEseMisRid.getDescrTipoMisura() != null) {%> <%=lEseMisRid.getDescrTipoMisura()%> - <% }%>
	            		<% if (lEseMisRid.getNumAnniMisura() != null)   {%> Anni <%=lEseMisRid.getNumAnniMisura()%><%  }%>
	            		<% if (lEseMisRid.getNumMesiMisura() != null)   {%> Mesi <%=lEseMisRid.getNumMesiMisura()%><%  }%>
	            		<% if (lEseMisRid.getNumGiorniMisura() != null) {%> Giorni <%=lEseMisRid.getNumGiorniMisura()%><%  }%>
	            	</td>    		
          		</tr>
<%
        	}
      }
%>
      <tr>  <td> </td> </tr>
      <tr>  <td> </td> </tr>
    </table>
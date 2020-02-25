<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@page import="siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel"%>
<%@page import="siap.sius.esecuzionemisurasicurezza.action.ICostantiEsecuzioneMS"%>

<jsp:useBean id="precedenteMisuraSicurezza" scope="request" class="siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel" />
<jsp:useBean id="attualeMisuraSicurezza" 	scope="request" class="siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel" />

<%
String lLabelPrecedente = "";
if (attualeMisuraSicurezza != null && attualeMisuraSicurezza.getDescrTipoMisura() != null &&
		attualeMisuraSicurezza.getDescrTipoMisura().length() > 0 )
	lLabelPrecedente = "Prima della trasformazione";
%>

    <table cellspacing=4 cellpadding=4 width=95%>
        <tr>
          <td class="Titolo" colspan=4> Misura di sicurezza in esecuzione </td>

        </tr>
          		<tr>
          		<%  if (precedenteMisuraSicurezza != null && precedenteMisuraSicurezza.getDescrTipoMisura() != null) { %>
          			<tr>
            		<td class=l colspan=2><%=lLabelPrecedente%></td>
            		<td class=l colspan=2>
            		<% if (precedenteMisuraSicurezza.getDescrTipoMisura() != null) {%> <%=precedenteMisuraSicurezza.getDescrTipoMisura()%> - <% }%>
            		<% if (precedenteMisuraSicurezza.getNumAnniMisura() != null)  {%> Anni <%=precedenteMisuraSicurezza.getNumAnniMisura()%><%  }%>
            		<% if (precedenteMisuraSicurezza.getNumMesiMisura() != null)  {%> Mesi <%=precedenteMisuraSicurezza.getNumMesiMisura()%><%  }%>
            		<% if (precedenteMisuraSicurezza.getNumGiorniMisura() != null)  {%> Giorni <%=precedenteMisuraSicurezza.getNumGiorniMisura()%><%  }%>
            		</td>
            		</tr>
					<%  }
          			if (attualeMisuraSicurezza != null && attualeMisuraSicurezza.getDescrTipoMisura() != null &&
          				attualeMisuraSicurezza.getDescrTipoMisura().length() > 0 ) { %>
          		    <tr>
            		<td class=l colspan=2>Dopo la trasformazione</td>
            		<td class=l colspan=2>
            		<% if (attualeMisuraSicurezza.getDescrTipoMisura() != null) {%> <%=attualeMisuraSicurezza.getDescrTipoMisura()%> - <% }%>
            		<% if (attualeMisuraSicurezza.getNumAnniMisura() != null)  {%> Anni <%=attualeMisuraSicurezza.getNumAnniMisura()%><%  }%>
            		<% if (attualeMisuraSicurezza.getNumMesiMisura() != null)  {%> Mesi <%=attualeMisuraSicurezza.getNumMesiMisura()%><%  }%>
            		<% if (attualeMisuraSicurezza.getNumGiorniMisura() != null)  {%> Giorni <%=attualeMisuraSicurezza.getNumGiorniMisura()%><%  }%>
            		</td>    		
          			</tr>
            	<%  }
          			
 				%>    
      <tr>  <td> </td> </tr>
      <tr>  <td> </td> </tr>
    </table>
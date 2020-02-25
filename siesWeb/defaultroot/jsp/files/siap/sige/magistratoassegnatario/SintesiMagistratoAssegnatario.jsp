<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>

<jsp:useBean id="magistratoassegnatario"   scope="request" class="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel"/>
<jsp:useBean id="TornaQui"   scope="request" class="java.lang.String"/>

<table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      	<td class="L">
      		<font class="label">Magistrato Assegnatario:  </font>
    <%  if (magistratoassegnatario != null && magistratoassegnatario.getMagistrato() != null) {
    %>
        	<font class="campo">
        		<%-- 20170908: [SG] aggiunto spazio tra nome e cognome --%>
            	<%=StringUtils.toStringJSP(magistratoassegnatario.getMagistrato().getCognome())%>&nbsp;
            	<%=StringUtils.toStringJSP(magistratoassegnatario.getMagistrato().getNome())%>&nbsp;
       		</font>
    <%   } %>
<%    	if (request.getParameter("MagAssRitorno") != null) {
       		// Inseriamo la nuova modalita' di Bottone di ritorno
         	if (TornaQui.trim().length() > 1) {
    %>
        	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.magistratoassegnatario.action.ActLoadInserisciMagistratoAssegnatario&TornaQui=<%=request.getParameter("TornaQui")%>">
         		Assegnazione/Cambio Magistrato Assegnatario
        	</a>
    <%
         	} else {
    %>
        	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.magistratoassegnatario.action.ActLoadInserisciMagistratoAssegnatario&acdest=<%=request.getParameter("MagAssRitorno")%>">
         		Assegnazione/Cambio Magistrato Assegnatario
        	</a>
    <%  	}
      	}
%>
   	 	</td>
    </tr>
    <tr><td>&nbsp;</td></tr>
</table>
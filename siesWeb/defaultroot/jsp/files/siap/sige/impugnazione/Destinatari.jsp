<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Vector"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>


<%
Vector  <NotificaModel> notifiche = (Vector  <NotificaModel>) request.getAttribute("notifiche");
%>

<table cellspacing=2 cellpadding=2 width="100%">
	<tr>
		<td class="Titolo" colspan=2>Destinatari</td>
	</tr>

	<%
		for (NotificaModel notifica : notifiche) {
	%>
	
	<tr>
		<%
			if (notifica.getUfficio() != null) {
					// UFFICIO
		%>
		<td class="l">
		Per la Notifica al Ufficio Pubblico Ministero
		</td>
		<td class="campo">
			<font class="campo"> <%=notifica.getUfficio().getDescrTipoUfficio()%></font>&nbsp;di&nbsp;
			<font class="campo"><%=notifica.getUfficio().getDescrComune()%> </font>
		</td>
		

		<%
			}
		
		    if (notifica.getAutoritaEsterna() != null) {
			
		%>
		<td class="l">
		Per la Notifica all'Ufficio Recupero Crediti
		</td>
		<td class="campo">
			<font class="campo"><%=notifica.getAutoritaEsterna().getDescrTipoAutorita()%></font> &nbsp;di&nbsp; 
			<font class="campo"><%=notifica.getAutoritaEsterna().getDescrSede()%></font>&nbsp;&nbsp; 
		</td>
	</tr>
	<%
		    }
		}    
	%>

	<tr>
		<td colspan=2>&nbsp;</td>
	</tr>
</table>
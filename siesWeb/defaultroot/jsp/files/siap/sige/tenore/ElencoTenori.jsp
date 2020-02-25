<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.decodifiche.action.ICostantiDecodifiche"%>

<%@ page import="siap.sige.tenore.model.TenoreSigeModel"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeEstesoModel"%>
<%@ page import="siap.sige.tenore.action.ICostantiTenoreSige"%>
<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel"%>

<jsp:useBean id="TenoriSige" scope="request" class="java.util.Vector"/>

<%@page import="f3b.log.LogF3B"%>
<html>
  <table width="100%" >
<%

	if (TenoriSige != null &&  TenoriSige.size() == 0 )
	{%>
		<td class="int" align="left">Non ci sono oggetti assegnati all'Atto.</td>
<%} 
  else
  {
		String lCodOggetto = "";
		String lIdTenore = "";
		String descrContenutoSige= "";
		
		Iterator itx = TenoriSige.iterator();
		while ( itx.hasNext())
		{
			TenoreSigeEstesoModel  lTenore = (TenoreSigeEstesoModel)itx.next();

			if (!lTenore.getTenoreSige().getCodOggettoSige().equalsIgnoreCase(lCodOggetto))
			{
				lIdTenore = lTenore.getTenoreSige().getIdTenoreSige().toString();
				lCodOggetto = lTenore.getTenoreSige().getCodOggettoSige();
				
%>      
			<tr></tr>
			<%
			if (!lTenore.getTenoreSige().getDescrContenutoSige().equalsIgnoreCase(descrContenutoSige))
			{
				descrContenutoSige = lTenore.getTenoreSige().getDescrContenutoSige();
			%> 
			<!-- riga per la descrizione del contenuto -->
			<tr>
				<td class="l" width=29%><font class="campo">&nbsp;<%=lTenore.getTenoreSige().getDescrContenutoSige()%></font>
				</td>
  		  	</tr>
			<%
			} 
			%>
			<tr>
				<td class="l" width=29%><font class="campo">&nbsp;&nbsp;&nbsp; - <%=lTenore.getTenoreSige().getDescrOggettoSige()%></font>
      		<!-- BOTTONE DI CANCELLAZIONE -->
					<jsp:include page="<%=ICostantiTenoreSige.BOTTONE_CANCELLA_OGGETTO%>">
						<jsp:param name="CampoIdEntita" value="<%=ICostantiTenoreSige.CAMPO_ID_TENORE_SIGE%>"/>
						<jsp:param name="ValoreIdEntita" value="<%=lIdTenore%>"/>
					</jsp:include> 					
				</td>
  		</tr>
<%
			} // Endif sul cambio del tenore
		} // endwhile
	} // endif
%>
  </table>
</html>
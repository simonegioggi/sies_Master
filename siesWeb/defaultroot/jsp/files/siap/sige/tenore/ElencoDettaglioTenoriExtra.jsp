<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sige.tenore.model.TenoreSigeModel"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeEstesoModel"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.tenore.action.ICostantiTenoreSige"%>

<jsp:useBean id="tenoriEstesi" scope="request" class="java.util.Vector"/>
<jsp:useBean id="ProvvedimentoEvento"		scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />
<%
BigDecimal idProvvedimento = (BigDecimal) request.getAttribute("idProvvedimentoTitoliEsecutivi");
 %>

<html>
<%
  if (tenoriEstesi != null &&  tenoriEstesi.size() == 0 )
  {
%>
        <td class="int" align="left">Non ci sono oggetti assegnati all'Atto.</td>
<%
  } 
  else
  {
	   Iterator itx = tenoriEstesi.iterator();
	   String lIdTenore = "";
	   String lCodTenore = "";

	   while ( itx.hasNext())
     {
				TenoreSigeEstesoModel lTenoreEsteso = (TenoreSigeEstesoModel)itx.next();
		  	    TenoreSigeModel lTenore=lTenoreEsteso.getTenoreSige();
		  	    lTenore.decodifica();  // 07/01/2010
				// Raggruppamento X ID Tenore
				if (lTenore != null &&  !lTenore.getIdTenoreSige().toString().equalsIgnoreCase(lIdTenore)) 
				{
					lIdTenore = lTenore.getIdTenoreSige().toString();
					lCodTenore = lTenore.getCodOggettoSige();
%>
      <tr>
        <td class="L"><font class="label">
					<%=lTenore.getDescrOggettoSige()%></font>
				</td>
        <td class="c"><font class="label">
<%			if (lTenore.getCodEsitoSige() != null && 
						lTenore.getCodEsitoSige().length()>1){%> 
					<font class="label">
						<%=StringUtils.toStringJSP(lTenore.getDescrEsitoSige())%></font>
				<%}else{%>-<%}%>
					</font>
				</td>
        <td class="c"><font class="label">
		        <jsp:include page="<%=ICostantiTenoreSige.PG_BUTTONS_PROVVEDIMENTO_TENORI%>">
		          <jsp:param name="CampoIdEntita" value="<%=ICostantiTenoreSige.CAMPO_ID_TENORE_SIGE%>" />
		          <jsp:param name="ValoreIdEntita" value="<%=lTenore.getIdTenoreSige()%>" />
		          <jsp:param name="Modifica" value="NO" />
		          <jsp:param name="esitoTenore" value="<%=lTenore.getCodEsitoSige() %>" />
		          <jsp:param name="idSenSentenza" value="<%=lTenoreEsteso.getSentenza().getIdSentenza() %>" />
		          <jsp:param name="idProvvedimento" value="<%=idProvvedimento.toString()%>" />
		          <jsp:param name="codOggettoSige" value="<%=lCodTenore%>" />
		        </jsp:include>
					</font>
				</td>
 		</tr>
<%
 		} // endif
       } // endwhile
  }  // endif provvedimenti.size()
%>
</html>